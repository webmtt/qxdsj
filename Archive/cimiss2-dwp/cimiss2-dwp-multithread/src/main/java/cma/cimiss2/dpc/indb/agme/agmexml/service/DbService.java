package cma.cimiss2.dpc.indb.agme.agmexml.service;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.agme.PropertiesUtil;
import cma.cimiss2.dpc.decoder.agme.PropertiesUtil.AgmeType;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_XML_Bean;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  DbService.java   
 * @Package cma.cimiss2.dpc.indb.agme.agmexml.service   
 * @Description:    TODO(数据库操作service 主要是操作结构化数据)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年10月11日 下午2:45:04   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *---------------------------------------------------------------------------------
 */
public class DbService {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	static Map<String, AgmeType> agmeDataMap = PropertiesUtil.getAgmeDataMap();
	/**
	 * @Title: processSuccessReport   
	 * @Description: 农气XML资料解码入库方法   
	 * @param parseResult
	 * @param recv_time
	 * @param fileN
	 * @param loggerBuffer
	 * @return DataBaseAction      
	 */
	public static DataBaseAction processSuccessReport(ParseResult<Agme_XML_Bean> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<Agme_XML_Bean> agme_XML_Beans = parseResult.getData();
			insert(agme_XML_Beans, connection,recv_time, fileN, loggerBuffer);
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n database connection error");
			return DataBaseAction.CONNECTION_ERROR;
		}
		finally{
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n database connection close error"+e.getMessage());
				}
			}
		}
	}
	/**
	 * @Title: insert   
	 * @Description:   
	 * @param buoys
	 * @param connection
	 * @param recv_time
	 * @param fileN
	 * @param loggerBuffer void      
	 * @throws：
	 */
	private static void insert(List<Agme_XML_Bean> agmes, java.sql.Connection connection, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		List<String> sqList = new ArrayList<>();
		// 按资料类型遍历,生成sql的List
		for(Agme_XML_Bean agme_XML_Bean: agmes){
			List<Map<String, Object>> list = agme_XML_Bean.getSql();
			Iterator<Map<String, Object>> iterator = null;
			// 遍历一种资料的要素编码-要素值sql
			List<String> eles = null;
			for(iterator = list.iterator(); iterator.hasNext();){
				String type = agme_XML_Bean.getType();
				String sql = "insert into " + agmeDataMap.get(type).getTableName() + "(";
				Map<String, Object> map = iterator.next();
				eles = new ArrayList<String>(map.keySet());
				sql += StringUtils.join(eles, ",");
				sql += ")values(";
				for(int i = 0; i < eles.size(); i ++){
					String valExpr =  map.get(eles.get(i)).toString();
					String v[] = valExpr.split("\\{|\\}");
					String val = "";
					if(valExpr.contains("{")){
						for(int p = 0; p < v.length; p ++){
							if(!v[p].equals("") && p != v.length - 1) {
								val += map.get(v[p]).toString() + "_";
							}else if(!v[p].equals("")) {
								val += map.get(v[p]).toString();
							}
								
						}
						val = val.replaceAll("'|-|:|\\s+", "");
						val = "'" + val + "'";
					}
					else
						val = valExpr;
					if(i != eles.size() - 1)
						sql += (val + ",");
					else
						sql += val;
				}
				sql += ")";
				sqList.add(sql);
				
				StatDi di = new StatDi();	
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(agmeDataMap.get(type).getSod_type());
				di.setDATA_TYPE_1(agmeDataMap.get(type).getCts_type());
				di.setTT(type);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				if(map.containsKey("V01301"))
					di.setIIiii(map.get("V01301").toString());
				else
					di.setIIiii("999998");
				if(map.containsKey("D_DATETIME"))
					di.setDATA_TIME(map.get("D_DATETIME").toString().replaceAll("'", ""));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				if(map.containsKey("V05001"))
					di.setLATITUDE(map.get("V05001").toString().replaceAll("'", ""));
				if(map.containsKey("V06001"))
					di.setLONGTITUDE(map.get("V06001").toString().replaceAll("'", ""));
				listDi.add(di);
			}
		}
		Statement pStatement = null;
		if(connection != null){		
			try {	
				connection.setAutoCommit(true);
				pStatement = connection.createStatement();
				for (int i = 0; i < sqList.size(); i++) {
//					pStatement = new LoggableStatement(connection, sqList.get(i));
					try {
						pStatement.execute(sqList.get(i));
					} catch (Exception e) {
						loggerBuffer.append("\n filename："+fileN
								+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
								+"\n execute sql error："+sqList.get(i)+"\n "+e.getMessage());
						listDi.get(i).setPROCESS_STATE("1");
					}
				}
			}catch (SQLException e) {
				loggerBuffer.append("\n create Statement error" + e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n close Statement error" + e.getMessage());
					}
				}
			}
		}
		
	}
}
