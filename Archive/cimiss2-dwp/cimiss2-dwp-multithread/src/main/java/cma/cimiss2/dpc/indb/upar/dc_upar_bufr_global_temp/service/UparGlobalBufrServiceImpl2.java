package cma.cimiss2.dpc.indb.upar.dc_upar_bufr_global_temp.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.cimiss2.dwp.tools.utils.UparLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrDecoder2;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

/**  
 * *********************************************************************** 
 * @ClassName:  DecoderBufr   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 吴佐强
 * @date:   2018年11月28日 下午10:21:06   
 *     
 * @Copyright: 2018 www.huaxin-hitec.com Inc. All rights reserved. 
 * 注意：本内容仅限于华云信息技术工程有限公司内部传阅，禁止外泄以及用于其他的商业目 
 * ***********************************************************************
 */
public class UparGlobalBufrServiceImpl2 {
	final  String defaultValue = "999999";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	public BufrDecoder2 decoder = new BufrDecoder2();
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static List<StatDi> DI = new ArrayList<StatDi>();
	public int levelNumOfNSEC = 0;
	public Boolean isNSECInsert = false;
	public Boolean ischeckTACexist = false;
	String V_BBB="000";
	double latitude=999999;
	double longitude=999999;
	String station="999999";
	
//	static Map<String, Object> proMap = StationInfo.getProMap();
	public UparGlobalBufrServiceImpl2() {
		
		BufrConfig.init();
	}
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		UparGlobalBufrServiceImpl2.diQueues = diQueues;
	}

	public static void main(String[] args) {
//		Date date = new Date();
//		date.setHours(6);
//		date = transDateTime(date);
		
//		String fileName = "D:\\TEMP\\B.1.16.1\\8-2\\A_IUKD15ESWI020000_C_BABJ_20190802002925_05948.bin";
//		String fileName = "D:\\tmp\\A_IUKC03RKSL0000_4_C_BABJ_20191105135625_73775.bin";
//		String fileName = "D:\\TEMP\\B.1.16-17.1\\NEWMODEL2\\A_IUSN74LFPW281900_C_BABJ_20200128200510_99704.bin";
//		String fileName = "D:\\TEMP\\B.1.16-17.1\\NEWMODEL2\\A_IUSN74LFPW301600_C_BABJ_20200130182020_02568.bin";
//		String fileName = "D:\\TEMP\\B.1.16.1\\2-24\\A_IUKC02RKSL0000_4_C_BABJ_20200108070456_91340.bin";
//		String fileName = "D:\\TEMP\\B.1.16-17.1\\liran\\A_IUKG03AMMC260000_C_BABJ_20200226010021_55596.bin";
//		String fileName = "D:\\TEMP\\B.1.19.1\\11-4\\A_IUJC01VTBB280600_C_BABJ_20191028064611_86217.bin";
		String fileName = "D:\\TEMP\\B.1.16-17.1\\liran\\A_IUKG03AMMC260000_C_BABJ_20200226010021_55596.bin";
//		String fileName = "D:\\TEMP\\A_IUWB01CWLT030000_C_BABJ_20190303000830_56116.bin";
		UparGlobalBufrServiceImpl2 impl = new UparGlobalBufrServiceImpl2();
		String cts = "B.0001.0016.R001";
		impl.decodeFile(fileName, cts);
		
//		System.out.println("解码、入库总共耗时：" + (System.currentTimeMillis() - start));
		
	}
	
	
	public  boolean decodeFile(String filename, String cts_code) {
		DruidPooledConnection connection = null;
		connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		
		List<String> tableList = new ArrayList<String>();
		tableList = BufrConfig.getTableMap().get(cts_code);
		//Z_UPAR_I_59265_20181205005759_O_TEMP-L-00_18120500_PQC.BIN
		// tableList 已经排序，要素表在前，键表在后。
//		boolean isDecode = isDecode(filename,  tableList.get(0),  connection);
//		if(isDecode){
		Map<String, XmlBean> configMap = BufrConfig.get(cts_code);
		
		try{
			BufrDecoder2 decoder = new BufrDecoder2();
			decoder.decodeFile(filename);
			ReportInfoToDb(decoder, cts_code, filename);
			Date recv = new Date(new File(filename).lastModified());
			UparLevel uparLevel = new UparLevel();
			for (Map<String, Object> parseMap : decoder.parseList) {
				ischeckTACexist=false;
				isNSECInsert = false;
				V_BBB=parseMap.get("BBB").toString();
				
				double []cal=calcuLatAndLon(parseMap,filename);
				latitude=cal[0];
				longitude=cal[1];
//				if(latitude==999999.0||longitude==999999.0){
//					continue;
//				}
				for (int i = 0; i < tableList.size(); i++) {
					String tableName = tableList.get(i);
					if(tableName.toUpperCase().contains("UPAR_WEA_GLB_MUL_FTM_TAB")||tableName.toUpperCase().contains("UPAR_WEA_GLB_MUL_FTM_ORI_TAB")){
						processSecondElementTable(cts_code, connection, uparLevel, filename, tableName, parseMap, configMap,recv);
					}else if(tableName.equalsIgnoreCase("UPAR_WEA_CHN_MUL_NSEC_TAB")||tableName.equalsIgnoreCase("UPAR_WEA_CHN_MUL_NSEC_ORI_TAB")){
						processSecondElementTable(cts_code,connection, uparLevel, filename, tableName, parseMap, configMap,recv);
					}
					else if(tableName.equalsIgnoreCase("UPAR_WEA_CHN_MUL_NSEC_K_TAB")||tableName.equalsIgnoreCase("UPAR_WEA_CHN_MUL_NSEC_K_ORI_TAB")){
						if(isNSECInsert||tableName.contains("ORI"))
							processNormalTable(cts_code, uparLevel, connection, filename, tableName, parseMap, configMap,recv);
					}else{
						processNormalTable(cts_code, uparLevel, connection, filename, tableName, parseMap, configMap,recv);
					}
				}
			}
			return true;
		}catch (Exception e) {
//			e.printStackTrace();
			infoLogger.error(e.getMessage());
			return false;
		}finally{
			try{
				if(connection != null)
					connection.close();
			}catch (Exception e) {
//				e.printStackTrace();
				infoLogger.error("connection.close() Failed!");
			}
		}
	}
	
	// 处理入库除了要素表之外的表
	private void processNormalTable(String cts_code, UparLevel uparLevel, DruidPooledConnection connection, String filename, String tableName, Map<String, Object> members, Map<String, XmlBean> configMap, Date recv) {
		Map<String, Object> proMap = StationInfo.getProMap();
		StringBuffer valBuffer = new StringBuffer();
		StringBuffer keyBuffer = new StringBuffer();
		XmlBean xmlBean = configMap.get(tableName);
		Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
		Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
		String D_record_id = "";
		String V01301="";
		String D_datetime="";
		Statement statement = null;
		if(helpMap != null)
			entityMap.putAll(helpMap);
		
		File file = new File(filename);
//		if(diQueues == null)
//			diQueues = new LinkedBlockingQueue<StatDi>();
		// DI 初始化
		StatDi di = new StatDi();
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setDATA_TYPE_1(cts_code);
		
		Calendar calendar = Calendar.getInstance();
		try{
			BufrBean beanYear = (BufrBean)members.get(entityMap.get("V04001_02").getFxy());
			BufrBean beanMonth = (BufrBean)members.get(entityMap.get("V04002_02").getFxy());
			BufrBean beanDayOfMonth = (BufrBean)members.get(entityMap.get("V04003_02").getFxy());
			BufrBean beanHourOfDay = (BufrBean)members.get(entityMap.get("V04004_02").getFxy());
			calendar.set(Calendar.YEAR, (int)Double.parseDouble((beanYear.getValue().toString())));
			calendar.set(Calendar.MONTH, (int)Double.parseDouble((beanMonth.getValue().toString())) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble((beanDayOfMonth.getValue().toString())));
			calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble((beanHourOfDay.getValue().toString())));
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			if(simpleDateFormat.format(calendar.getTime()).length() > 14){
				infoLogger.error("资料时间错误：" + filename);
				return ;
			}
			calendar.setTime(transDateTime(calendar.getTime()));
		}catch (Exception e) {
//			e.printStackTrace();
			infoLogger.error("资料时间错误：" + filename);
			return;
		}
		// 遍历xml表格字段，赋值
		for(String key: entityMap.keySet()){
			EntityBean entityBean = entityMap.get(key);
			String column = entityBean.getColumn();
			String datatype = entityBean.getDatatype(); // 字段类型
			String expression = entityBean.getExpression(); // 此字段的表达式
			String fxy = entityBean.getFxy(); // 解码时存储数据的key
			String defaultValue = entityBean.getDefaultValue(); // 缺省值
			Object obj = members.get(fxy); // 根据fxy获取每一行数据
			// 如果获取的对象为null
			Object value = null;
			
			if (obj == null) {
				// 如果表达式不为空说明此值需要根据表达式进行计算value
				if (StringUtils.isNotBlank(expression)) {
					// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
					Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
					if (em.find()) {
						value = defaultValue;
					} else {// 如果表达式存在，且其中没有以index取值
						value = calExpressionValue(obj, members, entityMap, entityBean);
					}
				} else {
					value = defaultValue;
					if(column.equalsIgnoreCase("D_SOURCE_ID")){
						value += "_" + file.getName();
					}
				}
				if(column.equalsIgnoreCase("V31001") && tableName.toUpperCase().contains("MUL_NSEC_K")){
					value = String.valueOf(levelNumOfNSEC);
				}
				else if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
					value = members.get("CCCC");
				else if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
					value = members.get("TTAAii");
				else if(column.equalsIgnoreCase("V_BBB"))
					value = members.get("BBB");
				else if(column.equalsIgnoreCase("D_RYMDHM")){
					value = simpleDateFormat2.format(recv);
				}
				// 资料时间规整
				if(column.equalsIgnoreCase("D_DATETIME")){
					value = simpleDateFormat2.format(calendar.getTime());
					try{
					    Date dataTime=simpleDateFormat2.parse(value.toString());
						if(!TimeCheckUtil.checkTime(dataTime)){
							infoLogger.info("DataTime out of range:"+dataTime+" "+ filename);
							return;
						}
					}catch (Exception e) {
						infoLogger.error("资料时间错误！  " + filename);
						return;
					}
				}
				else if(column.equalsIgnoreCase("V02421") && value.toString().startsWith("9999"))
					value = "999999";
				if(tableName.toUpperCase().contains("UPAR_WEA_GLB_MUL_FTM_K"))//原UPAR_WEA_GBF_MUL_FTM_K_TAB
					// 层次值赋值
					value = getLevel(column, uparLevel, value);
				
				
				if(column.equalsIgnoreCase("D_RECORD_ID")){
					D_record_id = value.toString();
					String dt = D_record_id.split("_")[0];
					D_record_id = D_record_id.replace(dt, simpleDateFormat.format(calendar.getTime()));
					value = D_record_id;
				}
				
				else if(column.equalsIgnoreCase("V01300")){
					 Pattern pattern = Pattern.compile("[0-9]*");
			         Matcher isNum = pattern.matcher(((BufrBean)members.get("0-1-2-0")).getValue().toString());
			           if( !isNum.matches() ){
			        	   value = 999999;
			           }
				}
				if("V05001".equalsIgnoreCase(column)){
					value=latitude;
				}
				if("V06001".equalsIgnoreCase(column)){
					value=longitude;
				}
				keyBuffer.append(",`").append(column).append("`");
				valBuffer.append(",'").append(value.toString().trim()).append("'");
				if(column.equals("D_DATETIME")){
					value = simpleDateFormat2.format(calendar.getTime());
					D_datetime=value.toString();
					int idx01 = value.toString().lastIndexOf(":");
					di.setDATA_TIME(value.toString().substring(0, idx01));
					try{
					    Date dataTime=simpleDateFormat2.parse(value.toString());
						if(!TimeCheckUtil.checkTime(dataTime)){
							infoLogger.info("DataTime out of range:"+dataTime+" "+filename);
							return;
						}
					}catch (Exception e) {
						infoLogger.error("资料时间错误！  " + filename);
						return;
					}
				}
				else if(column.equals("V01301")){
					di.setIIiii(value.toString());
					V01301=value.toString();
				}
				else if(column.equals("V05001"))
					di.setLATITUDE(String.valueOf(latitude));
				else if(column.equals("V06001"))
					di.setLONGTITUDE(String.valueOf(longitude));
				else if(column.equals("V07001"))
					di.setHEIGHT(value.toString());
				else if(column.equals("D_DATA_ID"))
					di.setDATA_TYPE(value.toString());
				else if(column.equals("V_BBB"))
					di.setDATA_UPDATE_FLAG(value.toString());
				
				continue;
			}
			if(column.equals("V04006")){
//				System.out.println(column);
			}
			// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//			System.err.println(column);
			value = calExpressionValue(obj, members, entityMap, entityBean);
			
			if(column.equalsIgnoreCase("V04001_02")){
				value = calendar.get(Calendar.YEAR);
			}else if(column.equalsIgnoreCase("V04002_02")){
				value = calendar.get(Calendar.MONTH) + 1;
			}else if(column.equalsIgnoreCase("V04003_02")){
				value = calendar.get(Calendar.DAY_OF_MONTH);
			}else if(column.equalsIgnoreCase("V04004_02")){
				value = calendar.get(Calendar.HOUR_OF_DAY);
			}
			
			else if(column.equalsIgnoreCase("V02011")){
				try{
					double a = Double.parseDouble(value.toString());
					if(a == -1.0){
						value = "999999";
					}
					else if(a < -1.0){
						value = String.valueOf(a + 256);
					}
				}catch (Exception e) {
//					e.printStackTrace();
				}
			}
			if("V05001".equalsIgnoreCase(column)){
				value=latitude;
			}
			if("V06001".equalsIgnoreCase(column)){
				value=longitude;
			}	
			
			if(helpMap != null){
				if(!helpMap.containsKey(column)){
					keyBuffer.append(",`").append(column).append("`");
					this.appendValue(valBuffer, datatype, value.toString().trim());
				}
			}
			else{
				keyBuffer.append(",`").append(column).append("`");
				this.appendValue(valBuffer, datatype, value.toString().trim());
			}
			
			if(column.equals("D_DATETIME")){
				value = simpleDateFormat2.format(calendar.getTime());
				D_datetime=value.toString();
				int idx01 = value.toString().lastIndexOf(":");
				di.setDATA_TIME(value.toString().substring(0, idx01));
				try{
				    Date dataTime=simpleDateFormat2.parse(value.toString());
					if(!TimeCheckUtil.checkTime(dataTime)){
						infoLogger.info("DataTime out of range:"+dataTime+" "+filename);
						return;
					}
				}catch (Exception e) {
					infoLogger.error("资料时间错误！  " + filename);
					return;
				}
			}
			else if(column.equals("V01301")){
				di.setIIiii(value.toString());
				V01301=value.toString();
			}
			else if(column.equals("V05001"))
				di.setLATITUDE(String.valueOf(latitude));
			else if(column.equals("V06001"))
				di.setLONGTITUDE(String.valueOf(longitude));
			else if(column.equals("V07001"))
				di.setHEIGHT(value.toString());
			else if(column.equals("D_DATA_ID"))
				di.setDATA_TYPE(value.toString());
			else if(column.equals("V_BBB"))
				di.setDATA_UPDATE_FLAG(value.toString());
		}// end for
		
//		File file = new File(filename);
		di.setFILE_SIZE(String.valueOf(file.length()));
		di.setSEND("BFDB");
		di.setSEND_PHYS("DRDS");
		di.setTRAN_TIME(ymdhm.format(new Date()));
		di.setTT("");
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setFILE_NAME_N(filename);
		di.setFILE_NAME_O(filename);
		di.setBUSINESS_STATE("1");
		di.setPROCESS_STATE("1");
		if(DI != null)
			DI.add(di);
		// 键表入库
		String sql = "";
 		sql = "insert into " + tableName //
				+ " (" //
				+ keyBuffer.toString().substring(1) //
				+ ") values ("//
				+ valBuffer.toString().substring(1) //
				+ ")";
		//优先级：17>16>19>18
// 		String deleteSQL = "Delete from " + tableName + " where D_Record_id = " + "'" + D_record_id +  "'" ;
//		String selectSQL = "Select D_SOURCE_ID from " + tableName + " where D_Record_id = " + "'" + D_record_id +  "'" ;
// 		String datetime = D_record_id.substring(0, 14);
//		Date date = new Date();
//		try {
//			date = simpleDateFormat.parse(datetime);
//		} catch (ParseException e1) {
//			infoLogger.error("Date transform error!");
////			e1.printStackTrace();
//		}
//		datetime = simpleDateFormat2.format(date);
 		String v01301S=V01301;
		if(v01301S.contains(".")){
			 v01301S=v01301S.substring(0,v01301S.indexOf("."));
		}
 		String datetime=D_datetime;
		
//		String deleteSQL = "Delete from " + tableName + " where D_Record_id = " + "'" + D_record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'";
 		String deleteSQL = "Delete from " + tableName + " where  D_datetime=" + "'" +datetime+ "'"+ " and V01301=" + "'" +v01301S+ "'";
		
//		String selectSQL = "Select D_SOURCE_ID from " + tableName + " where D_Record_id = " + "'" + D_record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'" + " limit 1";
 		String selectSQL = "Select D_SOURCE_ID from " + tableName + " where  D_datetime=" + "'" +datetime+ "'" + " and V01301=" + "'" +v01301S+ "'"+ " limit 1";
//		String selectSQL2 = "Select C_CCCC,V_TT from  UPAR_WEA_GLB_MUL_FTM_K_TAB  where D_Record_id = " + "'" + D_record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'" + " limit 1";
 		String selectSQL2 = "Select C_CCCC,V_TT from  UPAR_WEA_GLB_MUL_FTM_K_TAB  where  D_datetime=" + "'" +datetime+ "'" + " and V01301=" + "'" +v01301S+ "'"+ " limit 1";
 		
		boolean needDeleteAndInsert = false; // = true时，先删除再入库
		boolean justInsert = false; // =true, 不删除直接入库
		boolean noNeedInsert = false; // 不需要入库
		try{
			statement = connection.createStatement();
			ResultSet src = null;
			ResultSet srcselectRst2=null;
			try{
				if(!tableName.contains("ORI")){//原始表不判定优先级，直接入库
					src = statement.executeQuery(selectSQL);
					String source_id = "";
					if(src.next()){
						source_id = src.getString(1);
						if(source_id.length() >= 16)
							source_id = source_id.substring(0, 16);
					}
					
					String C_CCCC="999999";
					String V_TT="999999";
					srcselectRst2 = statement.executeQuery(selectSQL2);
					if(srcselectRst2.next()){
						C_CCCC = srcselectRst2.getString(1);
						V_TT = srcselectRst2.getString(2);
					}
					if(!source_id.isEmpty()){
						// 按主键，检索到了入库数据
						if(members.get("CCCC").toString().equalsIgnoreCase("RJTD") && members.get("TTAAii").toString().equalsIgnoreCase("IUSC70") ){//最高优先级
							//1.17.1 IUS
							needDeleteAndInsert = true; 
						}
						else if(cts_code.equals("B.0001.0017.R001") && (source_id.equals("B.0001.0016.R001") || source_id.equals("B.0001.0019.R001")
								|| source_id.equals("B.0001.0018.R001")) ){
							needDeleteAndInsert = true; // 1.17优先级高于1.16
						}
						else if(cts_code.equals("B.0001.0016.R001") && (source_id.equals("B.0001.0018.R001") || source_id.equals("B.0001.0019.R001"))){
							needDeleteAndInsert = true;
						}
						else if(cts_code.equals("B.0001.0019.R001") && source_id.equals("B.0001.0018.R001")){ // 优先级最高，且source_id 不为17
							needDeleteAndInsert = true;
						}
						else if(cts_code.equals("B.0001.0017.R001")&&(C_CCCC.equalsIgnoreCase("RJTD") && V_TT.equalsIgnoreCase("IUSC70"))){//要入库的优先级低，直接不入库
							noNeedInsert = true; 
						}
						else if(cts_code.equals("B.0001.0016.R001")&&source_id.equals("B.0001.0017.R001")){//要入库的优先级低，直接不入库
							noNeedInsert = true; 
						}
						else if(cts_code.equals("B.0001.0019.R001")&&(source_id.equals("B.0001.0017.R001")||source_id.equals("B.0001.0016.R001"))){//要入库的优先级低，直接不入库
							noNeedInsert = true; 
						}
						else if(cts_code.equals("B.0001.0018.R001")&&(source_id.equals("B.0001.0017.R001")||source_id.equals("B.0001.0016.R001")||source_id.equals("B.0001.0019.R001"))){//要入库的优先级低，直接不入库
							noNeedInsert = true; 
						}
						else{ // 优先级相同，或者库里的是TAC数据时，先留下来待后面判断是更正还是替换
							justInsert = true;
						}
					}
					else{
						justInsert = true; //第一次入库 或库里是TAC数据
					}
					if(needDeleteAndInsert == true){ // 先删除
						try{
							statement.execute(deleteSQL);
							if(connection.getAutoCommit() == false)
								connection.commit();
						}catch (Exception e) {
	//						e.printStackTrace();
							infoLogger.error("Delete record failed: " + deleteSQL + "\n" + e.getMessage());
						}
					}
				}
				if(tableName.contains("ORI")){
					justInsert = true;
				}
				//***************检查是否是国内站*************************/
//				int index0=D_record_id.lastIndexOf("_");
////				int v01301I=(int)Double.parseDouble(D_record_id.substring(index0+1));
//				String v01301S=D_record_id.substring(index0+1);
				
				String sta=v01301S;//站号
				String adminCode="999999";
				String countrycode="999999";
				String V_TT="999999";
				String BBB="999999";
				String info = (String) proMap.get(sta + "+04");
				if(info == null) {
					infoLogger.error("\n In configuration file, this station does not exist!" + sta);
				}
				else{
					String[] infos = info.split(",");
					if(infos[5].equals("null")){
						infoLogger.error("\n In configuration file, admin code is null!");
					}else{
						adminCode = infos[5].trim();
					}
					if(infos[4].equals("null")){
						infoLogger.error("\n In configuration file, countrycode code is null!");
					}else{
						countrycode = infos[4].trim();
					}
				}
				boolean isChinaSta=isChinaSta(sta, countrycode, adminCode);
				//***************检查是否是国内站**end***********************/
				
				if(justInsert == true || needDeleteAndInsert == true || noNeedInsert == false){
					
					if(!tableName.contains("ORI")&&ischeckTACexist==false){//若不是原始表，且还未进行TAC重复值检查，则进行如下检查、删除
						//**************判断库里是否有TAC重复记录***********/
						
						if(!isChinaSta){//若不是国内站，要进行检查是否有重复TAC记录
							ischeckTACexist=true;
							String selectSQL0 = "Select V_TT,V_BBB from UPAR_WEA_GLB_MUL_FTM_K_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta+ "'" + " limit 1";
							ResultSet selectRst = statement.executeQuery(selectSQL0);
							if(selectRst.next()){
								V_TT = selectRst.getString(1);
								BBB = selectRst.getString(2);
							}
							if(V_TT.startsWith("U")||(V_TT.startsWith("I")&&V_BBB.startsWith("CC")&& (V_BBB.compareTo(BBB) > 0||BBB.startsWith("RR")))){//有TAC记录，删除要素表和键表
								String deleteSQL1="delete from UPAR_WEA_GLB_MUL_FTM_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
								String deleteSQL2="delete from UPAR_WEA_GLB_MUL_FTM_K_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
								String deleteSQL3="delete from UPAR_WEA_CHN_MUL_NSEC_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
								String deleteSQL4="delete from UPAR_WEA_CHN_MUL_NSEC_K_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
								String deleteSQL5="delete from UPAR_WEA_CHN_PARA_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";

								try{
									int ds1=statement.executeUpdate(deleteSQL1);
									connection.commit();
									infoLogger.info("UPAR_WEA_GLB_MUL_FTM_TAB表成功删除"+ds1+"条数据: " + deleteSQL1 + "\n" );
									
								}catch (Exception e) {
//									e.printStackTrace();
									infoLogger.error("Delete record failed: " + deleteSQL1 + "\n" + e.getMessage());
								}
								try{
									int ds2=statement.executeUpdate(deleteSQL2);
									connection.commit();
									infoLogger.info("UPAR_WEA_GLB_MUL_FTM_K_TAB表成功删除"+ds2+"条数据: " + deleteSQL2 + "\n" );
								}catch (Exception e) {
//									e.printStackTrace();
									infoLogger.error("Delete record failed: " + deleteSQL2 + "\n" + e.getMessage());
								}
								try{
									int ds3=statement.executeUpdate(deleteSQL3);
									connection.commit();
									infoLogger.info("UPAR_WEA_CHN_MUL_NSEC_TAB表成功删除"+ds3+"条数据: " + deleteSQL3 + "\n" );
								}catch (Exception e) {
//									e.printStackTrace();
									infoLogger.error("Delete record failed: " + deleteSQL3 + "\n" + e.getMessage());
								}
								try{
									int ds4=statement.executeUpdate(deleteSQL4);
									connection.commit();
									infoLogger.info("UPAR_WEA_CHN_MUL_NSEC_K_TAB表成功删除"+ds4+"条数据: " + deleteSQL4 + "\n" );
								}catch (Exception e) {
//									e.printStackTrace();
									infoLogger.error("Delete record failed: " + deleteSQL4 + "\n" + e.getMessage());
								}
								try{
									int ds5=statement.executeUpdate(deleteSQL5);
									connection.commit();
									infoLogger.info("UPAR_WEA_CHN_PARA_TAB表成功删除"+ds5+"条数据: " + deleteSQL5 + "\n" );
								}catch (Exception e) {
//									e.printStackTrace();
									infoLogger.error("Delete record failed: " + deleteSQL5 + "\n" + e.getMessage());
								}
							}
							if(selectRst != null){
								try{
									selectRst.close();
									selectRst = null;
								}catch (Exception e) {
//									e.printStackTrace();
									infoLogger.error("Close ResultSet error! ");
								}
							}
						}
					}
						//**************判断库里是否有TAC重复记录**end***/
					if(!(!tableName.contains("ORI")&&isChinaSta)){//非原始表的国内站点数据不入库
						statement.execute(sql);	
						if(connection.getAutoCommit() == false)
							connection.commit();
						infoLogger.info(tableName+"表成功插入一条数据: " + filename + "\n" );
					}
				}
			}catch (Exception e) {
//				e.printStackTrace();
				infoLogger.error("Insert failed: " + sql + "\n"+ e.getMessage());
			}
			finally{
				if(src != null){
					try{
						src.close();
						src = null;
					}catch (Exception e) {
//						e.printStackTrace();
						infoLogger.error("Close ResultSet error! ");
					}
				}
				if(srcselectRst2 != null){
					try{
						srcselectRst2.close();
						srcselectRst2 = null;
					}catch (Exception e) {
//						e.printStackTrace();
						infoLogger.error("Close ResultSet error! ");
					}
				}
				
			}
		}catch (Exception e) {
//			e.printStackTrace();
			infoLogger.error("connection.createStatement() failed!");
		}
		finally{
			try{
				if(statement != null)
					statement.close();
			}catch (Exception e) {
//				e.printStackTrace();
				infoLogger.error("statement.close() Failed!");
			}
		}
		if(DI != null && diQueues != null){
			for (int j = 0; j < DI.size(); j++) {
				diQueues.offer(DI.get(j));
			}
			DI.clear();
		}
	}
	
	// 返回要素表NSEC条数
	private void processSecondElementTable(String cts_code, DruidPooledConnection connection, UparLevel uparLevel, String fileName, String table, Map<String, Object> parseMap, Map<String, XmlBean> configMap,Date recv) {
		StringBuffer headercol = new StringBuffer();
		StringBuffer headerval = new StringBuffer();
		
		String record_id = "";
//		isNSECInsert = false;
		record_id = processheader(cts_code, fileName, table, headercol, headerval, parseMap, configMap, recv);
		Long tLong = System.currentTimeMillis();
		if((!record_id.equals("")) && table.toUpperCase().contains("_FTM_"))
			processsSecondElementFTM(cts_code, connection, uparLevel, record_id, fileName, table, headercol, headerval, parseMap, configMap);
		else if((!record_id.equals("")) && table.toUpperCase().contains("_NSEC_"))
			processsSecondElementNSEC(cts_code, connection, record_id, fileName, table, headercol, headerval, parseMap, configMap);
		
		System.out.println("要素表构建SQL+入库耗时：" + (System.currentTimeMillis() - tLong));
		
	}

	private String processheader(String cts_code, String fileName, String table, StringBuffer headercol, StringBuffer headerval, Map<String, Object> parseMap, Map<String, XmlBean> configMap, Date recv) {
		File fileN=new File(fileName);
		XmlBean xmlBean = configMap.get(table);
		Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
		Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
		String d_record_id = "";
		if(helpMap != null)
			entityMap.putAll(helpMap);
		Calendar calendar = Calendar.getInstance();
		try{
			BufrBean beanYear = (BufrBean)parseMap.get(entityMap.get("V04001_02").getFxy());
			BufrBean beanMonth = (BufrBean)parseMap.get(entityMap.get("V04002_02").getFxy());
			BufrBean beanDayOfMonth = (BufrBean)parseMap.get(entityMap.get("V04003_02").getFxy());
			BufrBean beanHourOfDay = (BufrBean)parseMap.get(entityMap.get("V04004_02").getFxy());
			calendar.set(Calendar.YEAR, (int)Double.parseDouble((beanYear.getValue().toString())));
			calendar.set(Calendar.MONTH, (int)Double.parseDouble((beanMonth.getValue().toString())) - 1);
			calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble((beanDayOfMonth.getValue().toString())));
			calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble((beanHourOfDay.getValue().toString())));
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			calendar.setTime(transDateTime(calendar.getTime()));
			System.out.println(simpleDateFormat.format(calendar.getTime()));
			if(simpleDateFormat.format(calendar.getTime()).length() > 14){
				infoLogger.error("资料时间错误：" + fileName);
				return d_record_id;
			}
		}catch (Exception e) {
//			e.printStackTrace();
			infoLogger.error("资料时间错误：" + fileName);
			return d_record_id;
		}
	
		for(String key : entityMap.keySet()){
			EntityBean entityBean = entityMap.get(key);
			String fxy = entityBean.getFxy();
			String datatype = entityBean.getDatatype();
			String column = entityBean.getColumn();
			Object obj = parseMap.get(fxy);
			Object value = null;
			String expression = entityBean.getExpression(); // 此字段的表达式
			if(column.equals("V04006")){
				System.out.println();
			}
			if (obj == null) {
				// 如果表达式不为空说明此值需要根据表达式进行计算value
				if (StringUtils.isNotBlank(expression)) {
					// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
					Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
					if (em.find()) {
						value = defaultValue;
					} else {// 如果表达式存在，且其中没有以index取值
						value = calExpressionValue(obj, parseMap, entityMap, entityBean);
					}
				} else {
					value = entityBean.getDefaultValue();
					if(column.equalsIgnoreCase("D_SOURCE_ID"))
						value += "_" + fileN.getName();
				}
				if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
					value = parseMap.get("CCCC");
				if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
					value = parseMap.get("TTAAii");
				if(column.equalsIgnoreCase("V_BBB"))
					value = parseMap.get("BBB");
				else if(column.equalsIgnoreCase("D_RYMDHM")){
					value = simpleDateFormat2.format(recv);
				}
				// 资料时间规整
				if(column.equalsIgnoreCase("D_DATETIME")){
					value = simpleDateFormat2.format(calendar.getTime());
					try{
					    Date dataTime=simpleDateFormat2.parse(value.toString());
						if(!TimeCheckUtil.checkTime(dataTime)){
							infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
							break;
						}
					}catch (Exception e) {
						infoLogger.info("资料时间错误！"+fileName);
						break;
					}
				}
				
				else if(column.equalsIgnoreCase("D_RECORD_ID")){
					d_record_id = value.toString();
					String dt = d_record_id.split("_")[0];
					d_record_id = d_record_id.replace(dt, simpleDateFormat.format(calendar.getTime()));
					value = d_record_id;
				}
				else if(column.equalsIgnoreCase("V01300")){
					 Pattern pattern = Pattern.compile("[0-9]*");
			         Matcher isNum = pattern.matcher(((BufrBean)parseMap.get("0-1-2-0")).getValue().toString());
			           if( !isNum.matches() ){
			        	   value = 999999;
			           }
				}
				else if(column.equalsIgnoreCase("D_SOURCE_ID")){
					value = cts_code + "_" + fileN.getName();;
				}
				
				if(column.equalsIgnoreCase("V_PART") ){
					if(cts_code.equals("B.0001.0016.R001") || cts_code.equals("B.0001.0018.R001")){
						value = "AB";
					}else if(cts_code.equals("B.0001.0017.R001") || cts_code.equals("B.0001.0019.R001") ){
						value = "ABCD";
					}
				}
				if("V05001".equalsIgnoreCase(column)){
					value=latitude;
				}
				if("V06001".equalsIgnoreCase(column)){
					value=longitude;
				}
				if(!helpMap.containsKey(column)){
					headercol.append(",`").append(column).append("`");
					headerval.append(",'").append(value.toString().trim()).append("'");
				}
				continue;
			}
			// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
			value = calExpressionValue(obj, parseMap, entityMap, entityBean);
			
			if(column.equalsIgnoreCase("V04001_02")){
				value = calendar.get(Calendar.YEAR);
			}else if(column.equalsIgnoreCase("V04002_02")){
				value = calendar.get(Calendar.MONTH) + 1;
			}else if(column.equalsIgnoreCase("V04003_02")){
				value = calendar.get(Calendar.DAY_OF_MONTH);
			}else if(column.equalsIgnoreCase("V04004_02")){
				value = calendar.get(Calendar.HOUR_OF_DAY);
			}
			if("V05001".equalsIgnoreCase(column)){
				value=latitude;
			}
			if("V06001".equalsIgnoreCase(column)){
				value=longitude;
			}
			if(!helpMap.containsKey(column)){
				headercol.append(",`").append(column).append("`");
				this.appendValue(headerval, datatype, value.toString().trim());
			}
			
		} // end while
		return d_record_id;
	}

	
	private void processsSecondElementNSEC(String cts_code, DruidPooledConnection connection,String record_id, String fileName, String table, StringBuffer headercol, StringBuffer headerval, Map<String, Object> parseMap, Map<String, XmlBean> configMap) {
		Map<String, Object> proMap = StationInfo.getProMap();
		List<String> sqlList = new ArrayList<>();
		XmlBean xmlBean = configMap.get(table);
		Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
		Statement statement = null;
		if(!table.contains("ORI")){
		 isNSECInsert = false;
		}
		Boolean isDeleteExecute = false;
		boolean needDeleteAndInsert = false; // = true时，先删除再入库
		boolean justInsert = false; // =true, 不删除直接入库
		boolean noNeedInsert = false; // 不需要入库
		try{
			statement = connection.createStatement();
			for(String groupKey : groupMapList.keySet()){
				Map<String, EntityBean> group = groupMapList.get(groupKey);
				String groupFXY = groupKey.split("_")[0];
				List<Map<String, BufrBean>> BufrBeanList = (List<Map<String, BufrBean>>) parseMap.get(groupFXY);
				boolean verticalDetectEquals0 = false;
				if(!table.contains("ORI")){
					for(Map<String, BufrBean> map : BufrBeanList){
						if(((BufrBean)map.get("0-8-42")).getValue().toString().equals("0.0")){
							verticalDetectEquals0 = true;
							break;
						}
					}
				}
				if((verticalDetectEquals0 == true && BufrBeanList.size() > 500)||table.contains("ORI")){
					if(!table.contains("ORI")){
						isNSECInsert = true;
					}
					levelNumOfNSEC = BufrBeanList.size();
					int count = 0;
//					for (Map<String, BufrBean> map : BufrBeanList) {
					Map<String, BufrBean> map = null;
					for(int bufr = 0; bufr < BufrBeanList.size(); bufr ++){
						map = BufrBeanList.get(bufr);
						StringBuffer keyBuffer = new StringBuffer();
						StringBuffer valBuffer = new StringBuffer();
						
						for(String key : group.keySet()){
							EntityBean bean = group.get(key);
							if(bean.isQc()){
								String col = bean.getColumn();
								String fxy = bean.getFxy();
								BufrBean bufrBean = map.get(fxy);
								Object val = null;
								if(bufrBean != null && bufrBean.getQcValues() != null){
									val = bufrBean.getQcValues().get(0);
								}
								else{
									if(bufrBean==null && "Q10009".equals(col)){
										BufrBean bufrBean2 = map.get("0-7-9");
										if(bufrBean2 != null && bufrBean2.getQcValues() != null){
											val = bufrBean2.getQcValues().get(0);
										}else{
											val = "9";
										}
									}else{
										val = "9";
									}
								}
									
								keyBuffer.append(",`").append(col).append("`");
								valBuffer.append(",'").append(val.toString().trim()).append("'");
							}else{
								String col = bean.getColumn();
								String fxy = bean.getFxy();
								String datatype = bean.getDatatype(); 
								String exp = bean.getExpression();
								Object val = null;
								if(col.equalsIgnoreCase("D_ELE_ID")){
									val = record_id ;
									String []exprSp = exp.split(",");
									for(int e = 0; e < exprSp.length; e ++){
										if(map.get(exprSp[e].trim()) != null){
											val += "_" + map.get(exprSp[e].trim()).getValue().toString();
										}else {
											val += "_" + bean.getDefaultValue();
										}
									}
								}
								else if (col.equalsIgnoreCase("V10009")) {//位势高度
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null){
										val = bufrBean.getValue();
									}else if(bufrBean==null &&map.get("0-7-9")!=null){
										val=map.get("0-7-9").getValue();
									}else {
										val = bean.getDefaultValue();
									}
								}
								else if(col.equalsIgnoreCase("V12001") || col.equalsIgnoreCase("V12003")){
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null){
										val = bufrBean.getValue();
										if(val.toString().startsWith("-0.01"))
											val = defaultValue;
										else{
											if(exp != null && !exp.isEmpty() && !val.toString().startsWith("999999")){
												val = getExprValue(exp, val, datatype);
												val = String.format("%.4f", Double.parseDouble(val.toString()));
											}
										}
									}
									else
										val = bean.getDefaultValue();
								}
								else{
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null){
										val = bufrBean.getValue();
										if(exp != null && !exp.isEmpty() && !val.toString().startsWith("999999"))
											val = getExprValue(exp, val, datatype);
									}
									else
										val = bean.getDefaultValue();
								}
								keyBuffer.append(",`").append(col).append("`");
								valBuffer.append(",'").append(val.toString().trim()).append("'");
							}
						}
						
						keyBuffer.append(headercol.toString());
						valBuffer.append(headerval.toString().trim());
						
						String sql = "";
						sql = "insert into " + table //
							+ " (" //
							+ keyBuffer.toString().substring(1) //
							+ ") values ("//
							+ valBuffer.toString().substring(1) //
							+ ")";
							
						if(!table.contains("ORI")){
	//					 如果未执行，那么判断优先级 执行delete
							if(isDeleteExecute == false){
	//							String deleteSQL = "Delete from " + table + " where D_Record_id = " + "'" + record_id +  "'" ;
	//							String selectSQL = "Select D_SOURCE_ID from " + table + " where D_Record_id = " + "'" + record_id +  "'" ;
								
								String datetime = record_id.substring(0, 14);
								Date date = new Date();
								try {
									date = simpleDateFormat.parse(datetime);
								} catch (ParseException e1) {
									infoLogger.error("Datetime transform error!");
	//								e1.printStackTrace();
								}
								datetime = simpleDateFormat2.format(date);
								String tableKey = table.toUpperCase().replaceAll("_TAB", "_K_TAB");
								String deleteSQL = "Delete from " + table + " where D_Record_id = " + "'" + record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'";
								
								String deleteSOLKey = "Delete from " + tableKey + " where D_Record_id = " + "'" + record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'";
								
								String selectSQL = "Select D_SOURCE_ID from " + table + " where D_Record_id = " + "'" + record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'" + " limit 1";
								String selectSQL2 = "Select C_CCCC,V_TT from UPAR_WEA_GLB_MUL_FTM_K_TAB where D_Record_id = " + "'" + record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'" + " limit 1";
								
								ResultSet src = statement.executeQuery(selectSQL);
								String source_id = "";
								if(src.next()){
									source_id = src.getString(1);
									if(source_id.length() >= 16)
										source_id = source_id.substring(0, 16);
								}
								String C_CCCC="999999";
								String V_TT="999999";
								ResultSet srcselectRst2 = statement.executeQuery(selectSQL2);
								if(srcselectRst2.next()){
									C_CCCC = srcselectRst2.getString(1);
									V_TT = srcselectRst2.getString(2);
								}
								if(!source_id.isEmpty()){
									// 按主键，检索到了入库数据
									if(parseMap.get("CCCC").toString().equalsIgnoreCase("RJTD") && parseMap.get("TTAAii").toString().equalsIgnoreCase("IUSC70") ){
										//1.17.1 IUS
										needDeleteAndInsert = true; 
									}
									else if(cts_code.equals("B.0001.0017.R001") && source_id.equals("B.0001.0016.R001")){
										needDeleteAndInsert = true; // 1.17优先级高于1.16
									}
									else if(cts_code.equals("B.0001.0017.R001")&&(C_CCCC.equalsIgnoreCase("RJTD") && V_TT.equalsIgnoreCase("IUSC70"))){//要入库的优先级低，直接不入库
										noNeedInsert = true; 
									}
									else if(cts_code.equals("B.0001.0016.R001")&&source_id.equals("B.0001.0017.R001")){//要入库的优先级低，直接不入库
										noNeedInsert = true; 
									}
									else { // 优先级相同或者库里的是TAC数据，先留下来待后面确定是更正还是替换
										justInsert = true;
									}
								}
								else{
									justInsert = true; //第一次入库 或库里是TAC数据
								}
								if(needDeleteAndInsert == true){ // 先删除
									try{
										statement.execute(deleteSQL);
										if(connection.getAutoCommit() == false)
											connection.commit();
										statement.execute(deleteSOLKey);
										if(connection.getAutoCommit() == false)
											connection.commit();
									}catch (Exception e) {
										infoLogger.error("秒级表记录删除失败：\n" + deleteSQL + "\n" +  e.getMessage());
	//									e.printStackTrace();
									}
								}
								isDeleteExecute = true; // 表示已经进行了删除判断，需要删除时已经删除操作
								if(src != null){
									try{
										src.close();
										src = null;
									}catch (Exception e) {
										infoLogger.error("Close ResultSet error!");
	//									e.printStackTrace();
									}
								}
								if(srcselectRst2 != null){
									try{
										srcselectRst2.close();
										srcselectRst2 = null;
									}catch (Exception e) {
										infoLogger.error("Close ResultSet error!");
	//									e.printStackTrace();
									}
								}
							} // end if
						}
						
						if(table.contains("ORI")){
							justInsert = true;
						}
						/////////////////////////////
						sqlList.add(sql); 
						statement.addBatch(sql);
						count ++;
						
						//*********检查是否是国家站******************/
						int index0=record_id.lastIndexOf("_");
//						int v01301I=(int)Double.parseDouble(record_id.substring(index0+1));
						String v01301S=record_id.substring(index0+1);
						if(v01301S.contains(".")){
							 v01301S=v01301S.substring(0,v01301S.indexOf("."));
						}
						String sta=v01301S;//站号
						String adminCode="999999";
						String countrycode="999999";
						String V_TT="999999";
						String BBB="999999";
						String info = (String) proMap.get(sta + "+04");
						if(info == null) {
							infoLogger.error("\n In configuration file, this station does not exist!" + sta);
						}
						else{
							String[] infos = info.split(",");
							if(infos[5].equals("null")){
								infoLogger.error("\n In configuration file, admin code is null!");
							}else{
								adminCode = infos[5].trim();
							}
							if(infos[4].equals("null")){
								infoLogger.error("\n In configuration file, countrycode code is null!");
							}else{
								countrycode = infos[4].trim();
							}
						}
						String datetime = record_id.substring(0, 14);
						Date date = new Date();
						try{
							date = simpleDateFormat.parse(datetime);
						}catch (Exception e) {
//							e.printStackTrace();
							infoLogger.error("Datetime tranform error!");
						}
						boolean isChinaSta=isChinaSta(sta, countrycode, adminCode);
						//*********检查是否是国家站****end**************/
						
						if(justInsert == true || needDeleteAndInsert == true || noNeedInsert == false){
							
							if(!table.contains("ORI")&&ischeckTACexist==false){//若不是原始表，且还未进行TAC重复值检查，则进行如下检查、删除
								//**************判断库里是否有TAC重复记录***********/
//								int v01301I=(int)Double.parseDouble(parseMap.get("0-2-1-0").toString());
								
								datetime = simpleDateFormat2.format(date);
									if(!isChinaSta){//若不是国内站，要进行检查是否有重复TAC记录
										ischeckTACexist=true;
										String selectSQL = "Select V_TT,V_BBB from UPAR_WEA_GLB_MUL_FTM_K_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta+ "'" + " limit 1";
										ResultSet selectRst = statement.executeQuery(selectSQL);
										if(selectRst.next()){
											V_TT = selectRst.getString(1);
											BBB = selectRst.getString(2);
										}
										if(V_TT.startsWith("U")||(V_TT.startsWith("I")&&V_BBB.startsWith("CC")&& (V_BBB.compareTo(BBB) > 0||BBB.startsWith("RR")))){//有TAC记录，删除要素表和键表
											String deleteSQL1="delete from UPAR_WEA_GLB_MUL_FTM_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
											String deleteSQL2="delete from UPAR_WEA_GLB_MUL_FTM_K_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
											String deleteSQL3="delete from UPAR_WEA_CHN_MUL_NSEC_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
											String deleteSQL4="delete from UPAR_WEA_CHN_MUL_NSEC_K_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
											String deleteSQL5="delete from UPAR_WEA_CHN_PARA_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
			
											try{
												int ds1=statement.executeUpdate(deleteSQL1);
												connection.commit();
												infoLogger.info("UPAR_WEA_GLB_MUL_FTM_TAB表成功删除"+ds1+"条数据: " + deleteSQL1 + "\n" );
												
											}catch (Exception e) {
			//									e.printStackTrace();
												infoLogger.error("Delete record failed: " + deleteSQL1 + "\n" + e.getMessage());
											}
											try{
												int ds2=statement.executeUpdate(deleteSQL2);
												connection.commit();
												infoLogger.info("UPAR_WEA_GLB_MUL_FTM_K_TAB表成功删除"+ds2+"条数据: " + deleteSQL2 + "\n" );
											}catch (Exception e) {
			//									e.printStackTrace();
												infoLogger.error("Delete record failed: " + deleteSQL2 + "\n" + e.getMessage());
											}
											try{
												int ds3=statement.executeUpdate(deleteSQL3);
												connection.commit();
												infoLogger.info("UPAR_WEA_CHN_MUL_NSEC_TAB表成功删除"+ds3+"条数据: " + deleteSQL3 + "\n" );
											}catch (Exception e) {
			//									e.printStackTrace();
												infoLogger.error("Delete record failed: " + deleteSQL3 + "\n" + e.getMessage());
											}
											try{
												int ds4=statement.executeUpdate(deleteSQL4);
												connection.commit();
												infoLogger.info("UPAR_WEA_CHN_MUL_NSEC_K_TAB表成功删除"+ds4+"条数据: " + deleteSQL4 + "\n" );
											}catch (Exception e) {
			//									e.printStackTrace();
												infoLogger.error("Delete record failed: " + deleteSQL4 + "\n" + e.getMessage());
											}
											try{
												int ds5=statement.executeUpdate(deleteSQL5);
												connection.commit();
												infoLogger.info("UPAR_WEA_CHN_PARA_TAB表成功删除"+ds5+"条数据: " + deleteSQL5 + "\n" );
											}catch (Exception e) {
			//									e.printStackTrace();
												infoLogger.error("Delete record failed: " + deleteSQL5 + "\n" + e.getMessage());
											}
										}
										if(selectRst != null){
											try{
												selectRst.close();
												selectRst = null;
											}catch (Exception e) {
												infoLogger.error("Close ResultSet error!");
											}
										}
									}
								}
								//**************判断库里是否有TAC重复记录**end***/
							if(!(!table.contains("ORI")&&isChinaSta)){//非ORI表且是国内站的数据不入库
								if(count == 100 || bufr == BufrBeanList.size() - 1){				
									try{
										if(connection.getAutoCommit() == true)
											connection.setAutoCommit(false);
										statement.executeBatch();
										connection.commit();
										infoLogger.info(table+"要素表批量 插入成功，插入条数 =" + count + " " + fileName);
									}catch (Exception e) {
										infoLogger.error(table+"要素表批量 插入失败：" + fileName + "\n" + e.getMessage());
										// 该表会出现入库冲突的情况，所以逐条插入
										for(int s = 0; s < sqlList.size(); s ++){
											try{
												statement.execute(sqlList.get(s));
												connection.commit();
												infoLogger.info(table+"要素表插入一条记录成功！");
											}catch (Exception e1) {
		//										e1.printStackTrace();
												infoLogger.error(table+"要素表 插入失败：" + sqlList.get(s) + "\n" + e1.getMessage());
											}
										}
		//								e.printStackTrace();
									}
									sqlList.clear();
									statement.clearBatch();
									count = 0;
								}
							}
						}
						//////////////
						
					}// end BufrBeanList for
				} // end if
			}// end groupMapList for						
			
//			if(connection.getAutoCommit() == false)
//				connection.setAutoCommit(true);
//			for(int s = 0; s < sqlList.size(); s ++){
//				try{
//					statement.execute(sqlList.get(s));
//				}catch (Exception e1) {
//					infoLogger.error("NSEC 要素表 插入失败：" + sqlList.get(s) + "\n" + e1.getMessage());
//				}
//			}
		}
		catch (Exception e) {
			infoLogger.error(e.getMessage());
//			e.printStackTrace();
		}
		finally{
			try{
				if(statement != null)
					statement.close();
			}
			catch (Exception e) {
				infoLogger.info("statement.close() failed!");
//				e.printStackTrace();
			}
		}
	}

	//FTM 要素表SQL，返回层次数对象
	private UparLevel processsSecondElementFTM(String cts_code, DruidPooledConnection connection, UparLevel uparLevel, String record_id, String fileName, String table, StringBuffer headercol, StringBuffer headerval, Map<String, Object> parseMap, Map<String, XmlBean> configMap){
		Map<String, Object> proMap = StationInfo.getProMap();
		List<String> sqlList = new ArrayList<>();
		XmlBean xmlBean = configMap.get(table);
		Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
		Statement statement = null;
		Boolean isDeleteExecute = false;
		boolean needDeleteAndInsert = false; // = true时，先删除再入库
		boolean justInsert = false; // =true, 不删除直接入库
		boolean noNeedInsert = false; // 不需要入库
		
		boolean checkTACexist=false;//true表示已经检查库里是否有TAC记录
		try{
			statement = connection.createStatement();
			for(String groupKey : groupMapList.keySet()){//遍历XML中每个group
				
				int count = 0;
				
				Map<String, EntityBean> group = groupMapList.get(groupKey);
				String groupFXY = groupKey.split("_")[0];
				List<Map<String, BufrBean>> BufrBeanList = (List<Map<String, BufrBean>>) parseMap.get(groupFXY);
				Map<String, BufrBean> map = null;
				if(BufrBeanList != null){
					for (int bufr = 0; bufr < BufrBeanList.size(); bufr ++) {//一个group中的几条值
						map = BufrBeanList.get(bufr);
						StringBuffer keyBuffer = new StringBuffer();
						StringBuffer valBuffer = new StringBuffer();
						
						for(String key : group.keySet()){//xml中一个group的字段
							EntityBean bean = group.get(key);
							if(bean.isQc()){
								String col = bean.getColumn();
								String fxy = bean.getFxy();
								BufrBean bufrBean = map.get(fxy);
								Object val = null;
								if(bufrBean != null && bufrBean.getQcValues() != null){
									val = bufrBean.getQcValues().get(0);
								}
								else{
									if(bufrBean==null && "Q10009".equals(col)){
										BufrBean bufrBean2 = map.get("0-7-9");
										if(bufrBean2 != null && bufrBean2.getQcValues() != null){
											val = bufrBean2.getQcValues().get(0);
										}else{
											val = "9";
										}
									}else{
										val = "9";
									}
								}
									
								keyBuffer.append(",`").append(col).append("`");
								valBuffer.append(",'").append(val.toString().trim()).append("'");
							}else{
								String col = bean.getColumn();
								String fxy = bean.getFxy();
								String datatype = bean.getDatatype(); 
								String exp = bean.getExpression();
								Object val = null;
								if(col.equalsIgnoreCase("D_ELE_ID")){
									val = record_id ;
									String []exprSp = exp.split(",");
									for(int e = 0; e < exprSp.length; e ++){
										if(map.get(exprSp[e].trim()) != null){
											val += "_" + map.get(exprSp[e].trim()).getValue().toString();
										}else {
											if("0-10-9".equals(exprSp[e].trim())&&map.get("0-7-9")!=null){
												val += "_" + map.get("0-7-9").getValue().toString();
											}else{
											    val += "_" + bean.getDefaultValue();
											}
										}
									}
								}
								else if (col.equalsIgnoreCase("V10009")) {//位势高度
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null){
										val = bufrBean.getValue();
									}else if(bufrBean==null &&map.get("0-7-9")!=null){
										val=map.get("0-7-9").getValue();
									}else {
										val = bean.getDefaultValue();
									}
								}
								else if(col.equalsIgnoreCase("V12001") || col.equalsIgnoreCase("V12003")){
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null){
										val = bufrBean.getValue();
										if(val.toString().startsWith("-0.01"))
											val = defaultValue;
										else{
											if(exp != null && !exp.isEmpty() && !val.toString().startsWith("999999")){
												val = getExprValue(exp, val, datatype);
												val = String.format("%.4f", Double.parseDouble(val.toString()));
											}
										}
									}
									else
										val = bean.getDefaultValue();
								}
								else{
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null){
										val = bufrBean.getValue();
										if(exp != null && !exp.isEmpty() && !val.toString().startsWith("999999"))
											val = getExprValue(exp, val, datatype);
									}
									else
										val = bean.getDefaultValue();
								}
								
								// 层次数赋值（标准层层数、对流层层数、最大风层层数、温度特性层层数、风特性层层数、零度层层数、高度层层数）
								if(table.toUpperCase().contains("_FTM_"))
									if(col.equalsIgnoreCase("V08042"))
										UparLevel.setLevels(uparLevel, val.toString(), 18);
								
								
								keyBuffer.append(",`").append(col).append("`");
								valBuffer.append(",'").append(val.toString().trim()).append("'");
							}
						}
						
						keyBuffer.append(headercol.toString());
						valBuffer.append(headerval.toString().trim());
						
						String sql = "";
						sql = "insert into " + table //
							+ " (" //
							+ keyBuffer.toString().substring(1) //
							+ ") values ("//
							+ valBuffer.toString().substring(1) //
							+ ")";
						
						if(!table.contains("ORI")){
	//					 如果未执行，那么判断优先级 执行delete
							if(isDeleteExecute == false){
								
								String datetime = record_id.substring(0, 14);
								Date date = new Date();
								try{
									date = simpleDateFormat.parse(datetime);
								}catch (Exception e) {
	//								e.printStackTrace();
									infoLogger.error("Datetime tranform error!");
								}
								datetime = simpleDateFormat2.format(date);
								String tableKey = table.toUpperCase().replaceAll("_TAB", "_K_TAB");
								String deleteSQL = "Delete from " + table + " where D_Record_id = " + "'" + record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'";
								String deleteSOLKey = "Delete from " + tableKey + " where D_Record_id = " + "'" + record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'";
								String selectSQL = "Select D_SOURCE_ID from " + table + " where D_Record_id = " + "'" + record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'" + " limit 1";
								String selectSQL2 = "Select C_CCCC,V_TT from " + tableKey + " where D_Record_id = " + "'" + record_id +  "'" + " and D_datetime=" + "'" +datetime+ "'" + " limit 1";
	
								ResultSet src = statement.executeQuery(selectSQL);
								String source_id = "";
								if(src.next()){
									source_id = src.getString(1);
									if(source_id.length() >= 16)
										source_id = source_id.substring(0, 16);
								}
								String C_CCCC="999999";
								String V_TT="999999";
								ResultSet srcselectRst2 = statement.executeQuery(selectSQL2);
								if(srcselectRst2.next()){
									C_CCCC = srcselectRst2.getString(1);
									V_TT = srcselectRst2.getString(2);
								}
								if(!source_id.isEmpty()){
									// 按主键，检索到了入库数据
									if(parseMap.get("CCCC").toString().equalsIgnoreCase("RJTD") && parseMap.get("TTAAii").toString().equalsIgnoreCase("IUSC70") ){
										//1.17.1 IUS
										needDeleteAndInsert = true; 
									}
									else if(cts_code.equals("B.0001.0017.R001") && (source_id.equals("B.0001.0016.R001") || source_id.equals("B.0001.0019.R001")
											|| source_id.equals("B.0001.0018.R001")) ){
										needDeleteAndInsert = true; // 1.17优先级高于1.16
									}
									else if(cts_code.equals("B.0001.0016.R001") && (source_id.equals("B.0001.0018.R001") || source_id.equals("B.0001.0019.R001"))){
										needDeleteAndInsert = true;
									}
									else if(cts_code.equals("B.0001.0019.R001") && source_id.equals("B.0001.0018.R001")){ // 优先级最高，且source_id 不为17
										needDeleteAndInsert = true;
									}
									else if(cts_code.equals("B.0001.0017.R001")&&(C_CCCC.equalsIgnoreCase("RJTD") && V_TT.equalsIgnoreCase("IUSC70"))){//要入库的优先级低，直接不入库
										noNeedInsert = true; 
									}
									else if(cts_code.equals("B.0001.0016.R001")&&source_id.equals("B.0001.0017.R001")){//要入库的优先级低，直接不入库
										noNeedInsert = true; 
									}
									else if(cts_code.equals("B.0001.0019.R001")&&(source_id.equals("B.0001.0017.R001")||source_id.equals("B.0001.0016.R001"))){//要入库的优先级低，直接不入库
										noNeedInsert = true; 
									}
									else if(cts_code.equals("B.0001.0018.R001")&&(source_id.equals("B.0001.0017.R001")||source_id.equals("B.0001.0016.R001")||source_id.equals("B.0001.0019.R001"))){//要入库的优先级低，直接不入库
										noNeedInsert = true; 
									}
									else{ // 优先级相等或库里的是TAC数据的，先留下来待后续判断
										justInsert = true;
									}
								}
								else{
									justInsert = true; //第一次入库或库里是TAC数据
								}
								if(needDeleteAndInsert == true){ // 先删除
									try{
										statement.execute(deleteSQL);
										if(connection.getAutoCommit() == false)
											connection.commit();
										statement.execute(deleteSOLKey);
										if(connection.getAutoCommit() == false)
											connection.commit();
									}catch (Exception e) {
										infoLogger.error("FTM 表记录删除失败：\n" + deleteSQL + "\n" +  e.getMessage());
	//									e.printStackTrace();
									}
								}
								isDeleteExecute = true; // 表示已经进行了删除判断，需要删除时已经删除操作
								if(src != null){
									try{
										src.close();
										src = null;
									}catch (Exception e) {
										infoLogger.error("Close ResultSet error!");
	//									e.printStackTrace();
									}
								}
								if(srcselectRst2 != null){
									try{
										srcselectRst2.close();
										srcselectRst2 = null;
									}catch (Exception e) {
										infoLogger.error("Close ResultSet error!");
	//									e.printStackTrace();
									}
								}
							} // end if
						}
						if(table.contains("ORI")){
							justInsert = true;
						}
						sqlList.add(sql); 
						/////////////////////////////
						statement.addBatch(sql);
						count ++;
						
						//******检查是否是国内站*************/
						int index0=record_id.lastIndexOf("_");
	//					int v01301I=(int)Double.parseDouble(record_id.substring(index0+1));
						String v01301S=record_id.substring(index0+1);
						if(v01301S.contains(".")){
							 v01301S=v01301S.substring(0,v01301S.indexOf("."));
						}
						String sta=v01301S;//站号
						String adminCode="999999";
						String countrycode="999999";
						String V_TT="999999";
						String BBB="999999";
						String info = (String) proMap.get(sta + "+04");
						if(info == null) {
							infoLogger.error("\n In configuration file, this station does not exist!" + sta);
						}
						else{
							String[] infos = info.split(",");
							if(infos[5].equals("null")){
								infoLogger.error("\n In configuration file, admin code is null!");
							}else{
								adminCode = infos[5].trim();
							}
							if(infos[4].equals("null")){
								infoLogger.error("\n In configuration file, countrycode code is null!");
							}else{
								countrycode = infos[4].trim();
							}
						}
						String datetime = record_id.substring(0, 14);
						Date date = new Date();
						try{
							date = simpleDateFormat.parse(datetime);
						}catch (Exception e) {
							infoLogger.error("Datetime tranform error!");
						}
						datetime = simpleDateFormat2.format(date);
						boolean isChinaSta=isChinaSta(sta, countrycode, adminCode);//国内站true，国外站false
						//******检查是否是国内站***end**********/
						
						
						if(justInsert == true || needDeleteAndInsert == true || noNeedInsert == false){//确定可以入库的
							
							if(!table.contains("ORI")&&ischeckTACexist==false){//若不是原始表，且还未进行TAC重复值检查，则进行如下检查、删除
							//**************判断库里是否有TAC重复记录***********/
							
								if(!isChinaSta){//若不是国内站，要进行检查是否有重复TAC记录
									ischeckTACexist=true;
									String selectSQL = "Select V_TT,V_BBB from UPAR_WEA_GLB_MUL_FTM_K_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta+ "'" + " limit 1";
									ResultSet selectRst = statement.executeQuery(selectSQL);
									if(selectRst.next()){
										V_TT = selectRst.getString(1);
										BBB = selectRst.getString(2);
									}
									if(V_TT.startsWith("U")||(V_TT.startsWith("I")&&V_BBB.startsWith("CC")&&( V_BBB.compareTo(BBB) > 0||BBB.startsWith("RR")))){//有TAC记录，删除要素表和键表
										String deleteSQL1="delete from UPAR_WEA_GLB_MUL_FTM_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
										String deleteSQL2="delete from UPAR_WEA_GLB_MUL_FTM_K_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
										String deleteSQL3="delete from UPAR_WEA_CHN_MUL_NSEC_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
										String deleteSQL4="delete from UPAR_WEA_CHN_MUL_NSEC_K_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
										String deleteSQL5="delete from UPAR_WEA_CHN_PARA_TAB where d_Datetime = " + "'" + datetime +  "'" + " and V01301=" + "'" +sta + "'";
		
										try{
											int ds1=statement.executeUpdate(deleteSQL1);
											connection.commit();
											infoLogger.info("UPAR_WEA_GLB_MUL_FTM_TAB表成功删除"+ds1+"条数据: " + deleteSQL1 + "\n" );
											
										}catch (Exception e) {
		//									e.printStackTrace();
											infoLogger.error("Delete record failed: " + deleteSQL1 + "\n" + e.getMessage());
										}
										try{
											int ds2=statement.executeUpdate(deleteSQL2);
											connection.commit();
											infoLogger.info("UPAR_WEA_GLB_MUL_FTM_K_TAB表成功删除"+ds2+"条数据: " + deleteSQL2 + "\n" );
										}catch (Exception e) {
		//									e.printStackTrace();
											infoLogger.error("Delete record failed: " + deleteSQL2 + "\n" + e.getMessage());
										}
										try{
											int ds3=statement.executeUpdate(deleteSQL3);
											connection.commit();
											infoLogger.info("UPAR_WEA_CHN_MUL_NSEC_TAB表成功删除"+ds3+"条数据: " + deleteSQL3 + "\n" );
										}catch (Exception e) {
		//									e.printStackTrace();
											infoLogger.error("Delete record failed: " + deleteSQL3 + "\n" + e.getMessage());
										}
										try{
											int ds4=statement.executeUpdate(deleteSQL4);
											connection.commit();
											infoLogger.info("UPAR_WEA_CHN_MUL_NSEC_K_TAB表成功删除"+ds4+"条数据: " + deleteSQL4 + "\n" );
										}catch (Exception e) {
		//									e.printStackTrace();
											infoLogger.error("Delete record failed: " + deleteSQL4 + "\n" + e.getMessage());
										}
										try{
											int ds5=statement.executeUpdate(deleteSQL5);
											connection.commit();
											infoLogger.info("UPAR_WEA_CHN_PARA_TAB表成功删除"+ds5+"条数据: " + deleteSQL5 + "\n" );
										}catch (Exception e) {
		//									e.printStackTrace();
											infoLogger.error("Delete record failed: " + deleteSQL5 + "\n" + e.getMessage());
										}
									}
									if(selectRst != null){
										try{
											selectRst.close();
											selectRst = null;
										}catch (Exception e) {
											infoLogger.error("Close ResultSet error!");
	//										e.printStackTrace();
										}
									}
								}
							}
							//**************判断库里是否有TAC重复记录**end***/
							if(!(!table.contains("ORI")&&isChinaSta)){//非原始表的国内站点数据不入库
						
								if(count == 100 || bufr == BufrBeanList.size() - 1){				
									try{
										if(connection.getAutoCommit() == true)
											connection.setAutoCommit(false);
										statement.executeBatch();
										connection.commit();
										infoLogger.info(table+"要素表批量 插入成功，插入条数 =" + count + " " + fileName);
									}catch (Exception e) {
										infoLogger.error(table+"要素表批量 插入失败：" + fileName + "\n" + e.getMessage());
										// 该表会出现入库冲突的情况，所以逐条插入
										for(int s = 0; s < sqlList.size(); s ++){
											try{
												statement.execute(sqlList.get(s));
												connection.commit();
												infoLogger.info(table+"要素表插入一条记录成功！");
											}catch (Exception e1) {
			//									e1.printStackTrace();
												infoLogger.error(table+"要素表 插入失败：" + sqlList.get(s) + "\n" + e1.getMessage());
											}
										}
			//							e.printStackTrace();
									}
									sqlList.clear();
									statement.clearBatch();
									count = 0;
								}
							} 
						}
						//////////////
						
					}// end BufrBeanList
				}
			}// end groupMapList
			
//			if(connection.getAutoCommit() == false)
//				connection.setAutoCommit(true);
//			for(int s = 0; s < sqlList.size(); s ++){
//				try{
//					statement.execute(sqlList.get(s));
//				}catch (Exception e1) {
//					infoLogger.error("FTM 要素表 插入失败：" + sqlList.get(s) + "\n" + e1.getMessage());
//				}
//			}
		}
		catch (Exception e) {
			infoLogger.error(e.getMessage());
//			e.printStackTrace();
		}
		finally{
			try{
				if(statement != null)
					statement.close();
			}
			catch (Exception e) {
				infoLogger.info("statement.close() failed!");
//				e.printStackTrace();
			}
		}
		return uparLevel;
	}

	public  void decodeFile(String filename, byte[] dataBytes) {
		BufrDecoder2 decoder = new BufrDecoder2();
		decoder.decodeFile(filename, dataBytes);
		for (Map<String, Object> parseMap : decoder.parseList) {
			
		}
	}
	
	public Object getExprValue(String expr, Object value, String datatype){
		double base = 0;
		double val = 0;
		String exprSp = expr.substring(0, 1);
		if(value.toString().startsWith("999999"))
			return val;
		switch (exprSp) {
		case "+":
			base = Double.parseDouble(value.toString());
			val = Double.parseDouble(expr.substring(1));
			val = base + val;
			break;
		case "-":
			base = Double.parseDouble(value.toString());
			val = Double.parseDouble(expr.substring(1));
			val = base - val;
			break;
		case "*":
			base = Double.parseDouble(value.toString());
			val = Double.parseDouble(expr.substring(1));
			val = base * val;
			break;
		
		default:
			val = 999999;
			break;
		}
		if(datatype.equalsIgnoreCase("int")){
			return (int)val;
		}else if(datatype.equalsIgnoreCase("string")){
			return String.valueOf(val);
		}
		else{
			return val;
		}
	}
	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean) {
		String value = null;
		
		String expression = entityBean.getExpression();
		if (obj != null) {
			expression = getColumnValue(obj, entityBean).toString();
		}
		if (StringUtils.isNotBlank(expression)) {

			// ${col:V01301}
			// 将带有col的表达式替换掉
			Matcher columnMatcher = Pattern.compile(COLUMN_EXP_REGEX).matcher(expression);
			while (columnMatcher.find()) {
				String cExp = columnMatcher.group(0); // 带有${col:}的表达式
				String expCol = columnMatcher.group(1); // 字段
				// 获取表达式代表的配置
				EntityBean ebean = entityMap.get(expCol);
				String fxy = ebean.getFxy();
				// 获取表达式代表的对象
				Object obj2 = members.get(fxy);
				String cExpReg = StringUtil.string2Regex(cExp);
				if (obj2 == null) {
					// 如果表达式获取字段值为空，直接返回默认值
					expression = expression.replaceFirst(cExpReg, entityBean.getDefaultValue());
//					return entityBean.getDefaultValue();
				} else {
					// 如果不为空，获取其对应的值
					String columnValue = getColumnValue(obj2, ebean).toString();
					
//					if (columnValue.equals(ebean.getDefaultValue())) {
//						expression = expression.replaceFirst(cExpReg, columnValue);
//					}
					if(columnValue.startsWith("999999.0") && ebean.getDefaultValue() != null)
						columnValue = ebean.getDefaultValue();
					if(ebean.getDatatype().equals("int")){
						int idx = columnValue.indexOf(".");
						if(idx >= 1)
							columnValue = columnValue.substring(0, idx);
					}
//					System.out.println(expression + "====" + cExp);
					
					String afterText = expression.substring(expression.indexOf(cExp));
//					String cExpReg = StringUtil.string2Regex(cExp);
					afterText = afterText.replaceFirst(cExpReg, "");
					
					if (afterText.startsWith(StringUtil.SPLIT_FLAG) || afterText.startsWith(StringUtil.SUBSTRING_FLAG)) {
						// 存储整串表达式代表的值,list最后一个值即为最终结果
						List<String> res = new ArrayList<String>();
						// 用于存储一整串substring和split表达式，最后替换直值用
						StringBuffer regSBuffer = new StringBuffer();
						
						StringUtil.formatString(columnValue, afterText, res, regSBuffer);
						String resByStation = res.get(res.size() - 1); // 最终结果
						expression = expression.replaceFirst(StringUtil.string2Regex(cExp + regSBuffer.toString()), resByStation);
					} else {
						String sp [] = columnValue.split("[+\\*]");
						sp[0] = sp[0].replaceAll("\\$|\\{", "");
						if(!ebean.getColumn().equalsIgnoreCase("V01301") && !ebean.getColumn().equalsIgnoreCase("V01300")){
							try{
								sp[0] = String.format("%d", Integer.parseInt(sp[0]));
							}
							catch (Exception e1) {
								try{
									sp[0] = String.format("%.3f", Double.parseDouble(sp[0]));
								}
								catch (Exception e2) {
//									e2.printStackTrace();
	//								System.out.println(expression);
								}
//								e1.printStackTrace();
							}
						}
						expression = expression.replaceFirst(cExpReg, sp[0]);
					}
				}
			}

			// 替换所有默认表达式
//			System.out.println(expression);
			expression = StringUtil.formatExpressionContent(expression);
			// StringUtil.replaceNumberExp(expression, texts);
			// 替换${this}
			Matcher thisMatcher = Pattern.compile(THIS_VALUE_EXP).matcher(expression);
			while (thisMatcher.find()) {
				if (StringUtils.isBlank(value)) {
					return entityBean.getDefaultValue();
				}
				if(value!=null)
				expression = expression.replaceFirst(StringUtil.string2Regex(thisMatcher.group(0)), value); // 如果value为null怎么办？
			}
			//转换字符串为ascii码
			expression = StringUtil.formatStringAsciiExp(expression);

		}
		
		value = StringUtil.calculteExp(expression);
		if(value.equalsIgnoreCase("null"))
			value = "999999";
		return value;
	}

	private Object getColumnValue(Object obj, EntityBean entityBean) throws IndexOutOfBoundsException {
		boolean isQc = entityBean.isQc();
		// int index = entityBean.getIndex();
		Object value = null;
		// 如果是bufrBean对象，直接强制转换
		
		if (obj instanceof BufrBean) {
			BufrBean bufr = (BufrBean) obj;
			if (isQc) {
				if(bufr.getQcValues() != null)
					value = bufr.getQcValues().get(0);
				else 
					value = 9;
				int parseValue = Integer.parseInt(value.toString());
				value = parseValue > 9 ? 9 : parseValue;
			} else {
				// commented by cuihongyuan
//				value = bufr.getValue();
				String expression = entityBean.getExpression();
				if (StringUtils.isNotBlank(expression)) {
					Matcher numMatcher = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
					if (numMatcher.find()) {
						List<String> ts = new ArrayList<String>();
						value = entityBean.getDefaultValue();
						if(!bufr.getValue().toString().equals("NaN"))
							value = bufr.getValue();
						ts.add(value.toString());
						value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
					}else {
						infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
					}
				}else {
					value = bufr.getValue();
				}
				
			
				if(value!=null&&"999999.0".equals(value.toString())){
					value = entityBean.getDefaultValue();
				}
				
			}
		}
		else if(obj instanceof String){
			value = String.valueOf(obj);
		}
		else { // 不是bufrBean对象，那就是存储对象的list集合
			String expression = entityBean.getExpression();
			if(entityBean.getFxy().substring(0, 1).equals("1") && Integer.parseInt(entityBean.getFxy().substring(2, 3)) > 1){
				@SuppressWarnings("unchecked")
				List<Map<String, BufrBean>> bufrList = (List<Map<String, BufrBean>> ) obj;
				String []expSp = expression.split("_");
				BufrBean bean = new BufrBean();
				if(expSp.length == 2){
					int idx = Integer.parseInt(expSp[0]);
					String fxy = expSp[1];
					bean = bufrList.get(idx).get(fxy);
					value = entityBean.getDefaultValue();
					if(!bean.getValue().toString().equals("NaN"))
						value = bean.getValue();
				}
			}
			else{
				@SuppressWarnings("unchecked")
				List<BufrBean> bufrList = (List<BufrBean>) obj;
				
				if (StringUtils.isNotBlank(expression)) {
					Matcher numMatcher = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
					if (numMatcher.find()) {
	
						String _index = numMatcher.group(1);
						int index = Integer.parseInt(_index);
						// 如果list大小小于index，说明index代表的值不存在，直接返回默认值即可
						if ((index + 1) > bufrList.size()) {
							return entityBean.getDefaultValue();
						}
	
						List<String> ts = new ArrayList<String>();
						if (isQc) {
							for (BufrBean bufrBean : bufrList) {
								Object value2 = null;
								if(bufrBean.getQcValues() != null)
									value2 = bufrBean.getQcValues().get(0);
								else 
									value2 = 9;
								int parseValue = Integer.parseInt(value2.toString());
								value2 = parseValue > 9 ? 9 : parseValue;
								ts.add(value2.toString());
							}
						} else {
							for (BufrBean bufrBean : bufrList) {
								Object value2 = bufrBean.getValue();
								ts.add(value2.toString());
							}
						}
	
						value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
					} else {
						infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
					}
				} else {
					infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
				}	
			}
		}
		return value;
	}

	private void appendValue(StringBuffer buffer, String dataType, Object value) {
		if ("int".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("short".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("byte".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("double".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("float".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("long".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("date".equalsIgnoreCase(dataType)) {
			buffer.append(",'").append(value).append("'"); // TODO
		} else {
			buffer.append(",'").append(value).append("'");
		}
	}
	private Object getLevel(String column, UparLevel uparLevel, Object value){
		Object val = value;
		if(column.equalsIgnoreCase("V31001_S"))
			val = (Object)uparLevel.getStandardLvs();
		else if(column.equalsIgnoreCase("V31001_C"))
			val = (Object)uparLevel.getConvectionLvs();
		else if(column.equalsIgnoreCase("V31001_X"))
			val = (Object)uparLevel.getMaxWindLvs();
		else if(column.equalsIgnoreCase("V31001_N"))
			val = (Object)uparLevel.getTemperatureLvs();
		else if(column.equalsIgnoreCase("V31001_W"))
			val = (Object)uparLevel.getWindLvs();
		else if(column.equalsIgnoreCase("V31001_Z"))
			val = (Object)uparLevel.getZeroLvs();
		else if(column.equalsIgnoreCase("V31001_H"))
			val = (Object)uparLevel.getHeightLvs();
		return val;
	}
	/*
	private boolean isDecode(String filename, String table, DruidPooledConnection connection){
		SimpleDateFormat format = new  SimpleDateFormat("yyyyMMddHH");
		//Z_UPAR_I_59265_20181205005759_O_TEMP-L-00_18120500_PQC-CCB.BIN
		File file = new File(filename);
		String timestr = "";
		String prioFromDB = "";
		String bbbFromDB = "";
		String bbbFromFile = "000";
		if(file.exists()){
			String fileSimple = file.getName();
			String []sp = fileSimple.split("_|\\.|-");
			if(sp.length >= 12){
				String station = sp[3].trim();
				String prioFromFile = sp[8].trim();
				String datetimeStr = "20" + sp[9].trim();
				Statement statement = null;
				ResultSet resultSet = null;
				try{
					Date time = format.parse(datetimeStr);
					timestr = simpleDateFormat2.format(time);
					if(sp.length == 13){
						bbbFromFile = sp[11].trim();
					}
					
					String query = "select v_priority, v_bbb from " + table + 
							" where V01301=" + "'" + station + "'" + " and d_datetime = " + "'" + timestr + "'";
						statement = connection.createStatement();
						resultSet = statement.executeQuery(query);
						if(resultSet.next()){
							prioFromDB = resultSet.getString(1);
							bbbFromDB = resultSet.getString(2);
						}
						//优先级高低：00>10>50
						if(prioFromDB.isEmpty() || (!prioFromDB.isEmpty() && prioFromDB.compareTo(prioFromFile) > 0)){
							return true; //未入库,需要入库 ,或者 待处理文件优先级高，需要入库（delete再入库）
						}else if(prioFromDB.compareTo(prioFromFile) == 0){
							// 已入库，但是待入库文件优先级等于库中记录
							if(sp.length == 12)
								return false; // 待处理文件不是更正报，不再处理
							// 待处理文件是更正报，且更正报标识大于已入库
							else if(bbbFromDB.compareTo(bbbFromFile) < 0){ 
								// 是处理文件是更正报并且，更正标识较大,进行更正
								return true;
							}
							else //待处理文件更正标识较小
								return false;
						}
						else{
							// 已入库，但是待入库文件优先级低于库中记录，不再处理文件
							return false;
						}
						
				}catch (Exception e) {
					infoLogger.error(e.getMessage() + "\n" + filename);
					return false;
				}
				finally{
					try {
						if (resultSet != null) 
							resultSet.close();
						resultSet = null;
					} catch (SQLException e) {
						infoLogger.error("	resultSet.close() " + filename);
					}
					try{
						if(statement != null)
							statement.close();
					}catch (Exception e) {
						infoLogger.error("statement.close() Failed! "  + filename);
					}
				}
			}
			else{
				infoLogger.error("文件名格式异常！ " + filename);
				return true;
			}
		}
		else {
			return false;
		}
		
	}
	*/
	private static Date transDateTime(Date srcDate){
		Date src = srcDate;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(src);
//		if(calendar.get(Calendar.HOUR_OF_DAY) % 3 == 1){
//			calendar.add(Calendar.HOUR_OF_DAY, -1);
//		}else if(calendar.get(Calendar.HOUR_OF_DAY) % 3 == 2){
//			calendar.add(Calendar.HOUR_OF_DAY, 1);
//		}
		if(calendar.get(Calendar.HOUR_OF_DAY) % 3 == 1){
		calendar.add(Calendar.HOUR_OF_DAY, 2);
		}else if(calendar.get(Calendar.HOUR_OF_DAY) % 3 == 2){
			calendar.add(Calendar.HOUR_OF_DAY, 1);
		}
		return calendar.getTime();
	}
	
	boolean isChinaSta(String sta, String countryCode, String AdminCode){
		//国家代码是2250且行政代码小于710000的是国内站
		if(sta != null && countryCode != null && AdminCode != null){
			if(countryCode.equals("2250") && Double.parseDouble(AdminCode)!=999999 && Double.parseDouble(AdminCode) < 710000)
				return true; //国家站
			else 
				return false; // 国外站
		}else{
			return true; // 默认是国内的站
		}
		
	}
	//计算经纬度
	 double [] calcuLatAndLon(Map<String, Object> parseMap,String filename){
		  Map<String, Object> proMap = StationInfo.getProMap();
		  latitude=999999;
		  longitude=999999;
		  station="999999";
		  double[] LatAndLon={latitude,longitude};
		  
		  if(parseMap.get("0-5-1-0")!=null){
			  double lat=Double.parseDouble(((BufrBean)parseMap.get("0-5-1-0")).getValue().toString());
			  if(lat>=-90 && lat<=90){
				  latitude=lat;
			  } 
		  }
		  if(parseMap.get("0-6-1-0")!=null){
			  double lon=Double.parseDouble(((BufrBean)parseMap.get("0-6-1-0")).getValue().toString());
			  if(lon>=-180 && lon<=180){
				  longitude=lon;
			  }
		  }
		  if(parseMap.get("0-1-2-0")!=null){
			  station=((BufrBean)parseMap.get("0-1-2-0")).getValue().toString();
		  }
		  if(latitude==999999.0||longitude==999999.0){
				String info = (String) proMap.get(station + "+04");
				if(info == null||"".equals(info)) {
					infoLogger.error("经纬度无法解析！"  + filename);
					sendEI(station);
					return LatAndLon;
					
				}else{
					String[] infos = info.split(",");
					if(!(infos[1].trim().equals("null") || infos[1].trim().equals(""))){
						try{
						 longitude = Double.parseDouble(infos[1]);//经度
						}catch (Exception e) {
							infoLogger.error("经纬度无法解析！"  + filename);
							sendEI(station);
							return LatAndLon;
						}
					}else{
						infoLogger.error("经纬度无法解析！"  + filename);
						sendEI(station);
						return LatAndLon;
					}
					if(!(infos[2].trim().equals("null") || infos[2].trim().equals(""))){
						try{
						 latitude = Double.parseDouble(infos[2]);//纬度
						}catch (Exception e) {
							infoLogger.error("经纬度无法解析！"  + filename);
							sendEI(station);
							return LatAndLon;
						}
					}else{
						infoLogger.error("经纬度无法解析！"  + filename);
						sendEI(station);
						return LatAndLon;
					}
					latitude= Double.parseDouble(String.format("%.6f", latitude));
					longitude= Double.parseDouble(String.format("%.6f", longitude));
					LatAndLon[0]=latitude;
					LatAndLon[1]=longitude;
				}
				
			}else{
				latitude= Double.parseDouble(String.format("%.6f", latitude));
				longitude= Double.parseDouble(String.format("%.6f", longitude));
				LatAndLon[0]=latitude;
				LatAndLon[1]=longitude;
			}
		  return LatAndLon;
	  }
	 public void sendEI(String station){
			String event_type = EIEventType.STATION_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.upar.dc_upar_bufr_global_temp.service.UparGlobalBufrServiceImpl2");
					ei.setKEvent("获取台站信息异常");
					ei.setKIndex("全球高空台站："+station+"未能获取台站信息配置，无法获取经纬度");
					RestfulInfo restfulInfo = new RestfulInfo();
					restfulInfo.setType("SYSTEM.ALARM.EI ");
					restfulInfo.setName("获取台站信息EI告警信息");
					restfulInfo.setMessage("获取台站信息EI告警信息");
					restfulInfo.setFields(ei);
					List<RestfulInfo> restfulInfos = new ArrayList<>();
					restfulInfos.add(restfulInfo);
					RestfulSendData.SendData(restfulInfos, SendType.EI);
				}
			}
	 }
	 
	 public  void ReportInfoToDb(BufrDecoder2 bufrDecoder,String tableSection, String fileName) {
		 	int successOrNot=bufrDecoder.successOrNot;
			DruidPooledConnection report_connection = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			File file = new File(fileName);
			String filename = file.getName();
			Date recv = new Date(file.lastModified());
			String ReportTableName=StartConfig.reportTable();
//			String ReportTableName="UPAR_WEA_GLB_BULL_BUFR_TAB";
			String sod_code="B.0001.0024.S003";
			
			String sql="insert into "+ReportTableName+"(D_RECORD_ID,D_DATA_ID,D_SOURCE_id,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
					+ "V01301,V_BBB,V_CCCC,V_YYGGgg,V_TT,V_LEN,V_FILE_SIZE,V_BULLETIN_B,V_FILE_NAME,V_NUM,V01301_list,d_datetime_list,"
					+ "V_decode_flag) values ("
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?)";
			
			StringBuffer station_s=new StringBuffer();//存站号list集合
			StringBuffer datetime_s=new StringBuffer();//存站号list集合
			int sizes=0;
			String V_CCCC ="000";
			String V_BBB="000";
			String V_TT="999999";
			String V_YYGGgg="999999";
			
			
			PreparedStatement statement = null;
			try {
				report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
				statement = new LoggableStatement(report_connection, sql);
				report_connection.setAutoCommit(false);
				
				
				if(successOrNot==1){//解码成功
					if(bufrDecoder.parseList.size()>0){//解码有值
						for(Map<String, Object> members: bufrDecoder.parseList){
							//查站号
							String stafxy="0-1-2-0";
							String sta="999999";
							if(members.get(stafxy)!=null){
								sta=((BufrBean)(members.get(stafxy))).getValue().toString();
							}
							if(sta.contains(".")){
								sta=sta.substring(0,sta.lastIndexOf("."));
							}
							if(sta.startsWith("999999")){
								sta="999999";
							}
							station_s.append(sta).append(",");
							
							 V_CCCC = members.get("CCCC").toString();
							 V_BBB=members.get("BBB").toString();
							 V_TT=members.get("TTAAii").toString();
							 V_YYGGgg=members.get("YYGGgg").toString();
							 
							 //查总共多少条记录
							 String groupFXY="";
							 for(int i=0;i<2;i++){
								  groupFXY="1-1-0-"+i;
								  List<Map<String, BufrBean>> BufrBeanList = (List<Map<String, BufrBean>>) members.get(groupFXY);
								  if(BufrBeanList!=null){
									  sizes+=BufrBeanList.size();
								  }
							 }
							 
							//查datetime
							StringBuffer datetime=new StringBuffer();//存单个datetime
							String[] date_time={"9999","01","01","00","00"};
							for(int i=0;i<5;i++){
								int j=i+1;
								String fxy="0-4-"+j+"-0";
								Object obj=members.get(fxy);
								if(obj!=null){
									String ele=((BufrBean)(members.get(fxy))).getValue().toString();
									 try{
										 if(i==0){
											 if(!ele.startsWith("99999")){
												 date_time[i] = String.format("%04d", (int)Double.parseDouble(ele));
											 }
										 }else{
											 if(!ele.startsWith("99999")){
												 date_time[i]=String.format("%02d", (int)Double.parseDouble(ele)); 
											 }
										 }
									}catch (Exception e) {
										System.out.println("ReportInfoToDb datetime 转换异常："+e.getMessage());
									}
								}
							}
//							datetime.append(date_time[0]).append("-").append(date_time[1]).append("-").append(date_time[2]).append(" ").append(date_time[3]).append(":").append(date_time[4]).append(":").append("00");
							Calendar calendar = Calendar.getInstance();
							calendar.set(Calendar.YEAR, (int)Double.parseDouble(date_time[0]));
							calendar.set(Calendar.MONTH, (int)Double.parseDouble((date_time[1])) - 1);
							calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble(date_time[2]));
							calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble(date_time[3]));
							calendar.set(Calendar.MINUTE, 0);
							calendar.set(Calendar.SECOND, 0);
							calendar.set(Calendar.MILLISECOND, 0);
							calendar.setTime(transDateTime(calendar.getTime()));
							datetime.append(simpleDateFormat2.format(calendar.getTime()));
							datetime_s.append(datetime).append(",");
							
							
						}//end ParseList for
						String stations=station_s.substring(0,station_s.length()-1);//站号集合
						stations=stations.length()>800?stations.substring(0,800):stations;//长度若超过800，则截断处理
						String station_1=stations.split(",")[0];//第一个站号
						String datetimes=datetime_s.substring(0,datetime_s.length()-1);//datetime集合
						datetimes=datetimes.length()>1500?datetimes.substring(0,1500):datetimes;//长度若超过1500，则截断处理
						String datetime_1=datetimes.split(",")[0];
						Date datetime1=new Date();
						datetime1=simpleDateFormat.parse(datetime_1);//第一个datetime
						byte[] databyte=getContent(fileName);//报文二进制数组
						int ii = 1;
						statement.setString(ii++, station_1 + "_" + sdf.format(datetime1));// d_record_id
						statement.setString(ii++, sod_code);//D_DATA_ID
						statement.setString(ii++, tableSection);//D_source_id
						statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
						statement.setTimestamp(ii++, new Timestamp(recv.getTime()));// D_RYMDHM
						statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
						statement.setTimestamp(ii++, new Timestamp(datetime1.getTime()));//D_DATETIME
						statement.setString(ii++, station_1);//V01301
						statement.setString(ii++, V_BBB);//V_BBB
						statement.setString(ii++, V_CCCC);//V_CCCC
						statement.setString(ii++, V_YYGGgg);//V_YYGGgg
						statement.setString(ii++, V_TT);//V_TT
						statement.setLong(ii++, file.length());//V_LEN    BUFR报文长度 ?????????????
						statement.setLong(ii++, file.length());//V_file_size 文件大小
						statement.setBytes(ii++, databyte);//V_BULLETIN_B 公报内容
//							statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
						statement.setString(ii++,filename);//V_FILE_NAME 文件名
						statement.setInt(ii++, sizes);//V_NUM 观测记录数
						statement.setString(ii++,stations);//V01301_list 观测记录站号列表
						statement.setString(ii++,datetimes);//d_datetime_list 观测记录时间列表
						statement.setInt(ii++,1);//V_decode_flag 解码是否成功标志
					}else{
						
					}
				}else if(successOrNot==2){//解码失败
					V_BBB=bufrDecoder.V_BBB;
					V_CCCC=bufrDecoder.V_CCCC;
					V_TT=bufrDecoder.V_TTAAii;
					V_YYGGgg=bufrDecoder.V_YYGGgg;
					byte[] databyte=getContent(fileName);//报文二进制数组
					int ii = 1;
					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
					statement.setString(ii++, sod_code);//D_DATA_ID
					statement.setString(ii++, tableSection);//D_source_id
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
					statement.setTimestamp(ii++, new Timestamp(recv.getTime()));// D_RYMDHM
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_DATETIME
					statement.setString(ii++, "999999");//V01301
					statement.setString(ii++, V_BBB);//V_BBB
					statement.setString(ii++, V_CCCC);//V_CCCC
					statement.setString(ii++, V_YYGGgg);//V_YYGGgg
					statement.setString(ii++, V_TT);//V_TT
					if(bufrDecoder.normalFlag==1){//---------------报文有正常BUFR消息，但解码失败
						statement.setLong(ii++, file.length());//V_LEN    BUFR报文长度 ?????????????
					}else{//--------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setLong(ii++, 0);//V_LEN    BUFR报文长度 ?????????????
					}
					statement.setLong(ii++, file.length());//V_file_size 文件大小
					statement.setBytes(ii++, databyte);//V_BULLETIN_B 公报内容
					statement.setString(ii++,filename);//V_FILE_NAME 文件名
					if(bufrDecoder.normalFlag==1){//--------------报文有正常BUFR消息，但解码失败
						statement.setInt(ii++, -1);//V_NUM 观测记录数
					}else{//-----------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setInt(ii++, 0);//V_NUM 观测记录数
					}
					statement.setString(ii++,null);//V01301_list 观测记录站号列表
					statement.setString(ii++,null);//d_datetime_list 观测记录时间列表
					if(bufrDecoder.normalFlag==1){//---------------------报文有正常BUFR消息，但解码失败
						statement.setInt(ii++,-1);//V_decode_flag 解码是否成功标志
					}else{//---------------------------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setInt(ii++,0);//V_decode_flag 解码是否成功标志
					}
				}else {//未做解码， 即文件不存在
					byte[] databyte=new byte[0];
					int ii = 1;
					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
					statement.setString(ii++, sod_code);//D_DATA_ID
					statement.setString(ii++, tableSection);//D_source_id
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
					statement.setTimestamp(ii++, new Timestamp(recv.getTime()));// D_RYMDHM
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_DATETIME
					
					statement.setString(ii++, "999999");//V01301
					statement.setString(ii++, V_BBB);//V_BBB
					statement.setString(ii++, V_CCCC);//V_CCCC
					statement.setString(ii++, V_YYGGgg);//V_YYGGgg
					statement.setString(ii++, V_TT);//V_TT
					statement.setLong(ii++, 0);//V_LEN    BUFR报文长度 ?????????????
					statement.setLong(ii++, 0);//V_file_size 文件大小
					statement.setBytes(ii++, databyte);//V_BULLETIN_B 公报内容
					statement.setString(ii++,filename);//V_FILE_NAME 文件名
					statement.setInt(ii++, 0);//V_NUM 观测记录数
					statement.setString(ii++,null);//V01301_list 观测记录站号列表
					statement.setString(ii++,null);//d_datetime_list 观测记录时间列表
					statement.setInt(ii++,0);//V_decode_flag 解码是否成功标志
				}
				try{
					statement.execute();
					report_connection.commit();
					infoLogger.info("ReportInfoToDb 数据入报告表"+StartConfig.reportTable()+"成功！:"+fileName);
				}catch (Exception e) {
					infoLogger.error("ReportInfoToDb 数据入报告表"+StartConfig.reportTable()+"失败！:"+fileName+((LoggableStatement)statement).getQueryString()+e.getMessage());
				}
			}catch (Exception e) {
				infoLogger.error("ReportInfoToDb 数据入报告表"+StartConfig.reportTable()+"失败！:"+fileName+e.getMessage());
			}
			finally{
				if(report_connection!=null){
					try {
						report_connection.close();
					} catch (SQLException e) {
						infoLogger.error("report_connection关闭失败！:"+fileName+e.getMessage());
						e.printStackTrace();
					}
				}
				if(statement!=null){
					try {
						statement.close();
					} catch (SQLException e) {
						infoLogger.error("statement关闭失败！:"+fileName+e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		public byte[] getContent(String filePath)throws IOException {  
	        File file = new File(filePath);  
	        long fileSize = file.length();  
	        if (fileSize > Integer.MAX_VALUE) {  
	            System.out.println("file too big...");  
	            return null;  
	        } 
	       
	        FileInputStream fi = new FileInputStream(file);  
	        byte[] buffer = new byte[(int) fileSize];  
	        int offset = 0;  
	        int numRead = 0;  
	        while (offset < buffer.length  
	        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
	            offset += numRead;  
	        }  
	        // 确保所有数据均被读取  
	        if (offset != buffer.length) {  
	        throw new IOException("Could not completely read file "  + file.getName());  
	        }  
	        fi.close();  
	        return buffer;  
	    }
}
