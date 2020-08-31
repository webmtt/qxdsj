package cma.cimiss2.dpc.indb.agme.agmexml.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.agme.PropertiesUtil;
import cma.cimiss2.dpc.decoder.agme.PropertiesUtil.AgmeType;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_XML_Bean;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  OTSService.java   
 * @Package cma.cimiss2.dpc.indb.agme.agmexml.service   
 * @Description:  农气XML格式观测资料入OTS处理类
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年10月11日 下午2:49:50   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *---------------------------------------------------------------------------------
 */
public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}
	static Map<String, AgmeType> agmeDataMap = PropertiesUtil.getAgmeDataMap();
	public static DataBaseAction insert_ots(ParseResult<Agme_XML_Bean> parseResult, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		// 获取解码实体类结果集
		List<Agme_XML_Bean> list = parseResult.getData();
		
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
			// 按资料类型遍历
	        for (int i = 0; i < list.size(); i++) {
	        	String type = list.get(i).getType();
	        	List<Map<String, Object>> list2 = list.get(i).getSql();
	        	// 某一类资料的遍历，1个或多个map(sql)
	        	for(Map<String, Object> map : list2){
	        		Map<String, Object> row = map;
	        		String ID = row.get("D_RECORD_ID").toString();
	        		String v[] = ID.split("\\{|\\}");
					String val = "";
					if(ID.contains("{")){
						for(int p = 0; p < v.length; p ++){
							if(!v[p].equals("") && p != v.length - 1)
								val += map.get(v[p]).toString() + "_";
							else if(!v[p].equals(""))
								val += map.get(v[p]).toString();
						}
						val = val.replaceAll("'|-|:|\\s+", "");
						val = "'" + val + "'";
					}
					else
						val = ID;
	        		
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
					if(map.containsKey("V05001"))
						di.setLATITUDE(map.get("V05001").toString());
					if(map.containsKey("V06001"))
						di.setLONGTITUDE(map.get("V06001").toString());
					if(map.containsKey("V01301"))
						di.setIIiii(map.get("V01301").toString());
					else
						di.setIIiii("999998");
					if(map.containsKey("D_DATETIME"))
						di.setDATA_TIME(map.get("D_DATETIME").toString());
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					
					try {
						String tablename = agmeDataMap.get(type).getTableName();
						OTSDbHelper.getInstance().insert(tablename, row);
						diQueues.offer(di);
					}  catch (Exception e) {
						successRowCount = successRowCount -1;
						di.setPROCESS_STATE("1");
						diQueues.offer(di);
						loggerBuffer.append(row);
						loggerBuffer.append(e.getMessage());
						if(e.getClass() == ClientException.class) {
							return DataBaseAction.CONNECTION_ERROR;
						}
					}
	        	} // end for
	        } // end for
	        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
	    }
	    return DataBaseAction.SUCCESS;
	}
	
}
