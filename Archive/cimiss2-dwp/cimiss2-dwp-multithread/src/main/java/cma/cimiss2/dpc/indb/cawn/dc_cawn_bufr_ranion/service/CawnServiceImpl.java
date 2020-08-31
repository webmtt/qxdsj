package cma.cimiss2.dpc.indb.cawn.dc_cawn_bufr_ranion.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.nutz.castor.castor.String2DateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrDecoder2;
import com.hitec.bufr.util.StringUtil;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class CawnServiceImpl implements BufrService {
	int batchSize = 500;
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	private final static String FXY_VALUE_EXP = "\\{(\\d+)(\\_)(\\d+\\-\\d+\\-\\d+)\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> DI = new ArrayList<StatDi>();
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();  
		String tableSection = "G.0003.0004.R001";
		tableList.add("CAWN_CHN_AR_TAB");
//		String fileName = "D:\\usr\\data\\20181210\\01\\Z_CAWN_I_58402_20181210015400_O_AR_FTM_PQC.BIN";
		String fileName = "D:\\TEMP\\G.3.4.1\\10-22out\\Z_CAWN_I_58847_20191011080000_O_AR_FTM_PQC.BIN";
//		String fileName = "D:\\TEMP\\G.3.4.1\\6-18-2\\Z_CAWN_I_54568_20190617000000_O_AR_FTM_PQC.BIN";
		CawnServiceImpl cawnServiceImpl = new CawnServiceImpl();
		boolean decode = cawnServiceImpl.decode(fileName, tableSection, tableList);
		if(decode){
			System.out.println("入库成功！");
		}else
			System.err.println("入库失败！");
//		bufrDecoder.bufrDecoder("D:\\A_ISAC01VHHH200300_C_BABJ_20180920031410_90199.bin");
	}
	
	

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		CawnServiceImpl.diQueues = diQueues;
	}



	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrDecoder2 bufrDecoder2 = new BufrDecoder2();
			bufrDecoder2.decodeFile(name, dataBytes);
			ReportInfoToDb(bufrDecoder2, tableSection, name,dataBytes);
			return this.decode(bufrDecoder2, name, tableSection, tables);
				
		} catch (Exception e) {
			e.printStackTrace();
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.cawn.bufr1.ar.service.OceanServiceImpl");
					ei.setKEvent("解码入库源文件异常：" + name);
					ei.setKIndex("详细信息：" + name);
					ei.setEVENT_EXT1(name);
					RestfulInfo restfulInfo = new RestfulInfo();
					restfulInfo.setType("SYSTEM.ALARM.EI ");
					restfulInfo.setName("数据解码入库EI告警信息");
					restfulInfo.setMessage("数据解码入库EI告警信息");
					
					restfulInfo.setFields(ei);
					List<RestfulInfo> restfulInfos = new ArrayList<>();
					restfulInfos.add(restfulInfo);
					RestfulSendData.SendData(restfulInfos, SendType.EI);
				}
			}
			return false;
			
		}
	}

	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {

		
		try {
			BufrDecoder2 bufrDecoder2 = new BufrDecoder2();
			bufrDecoder2.decodeFile(fileName);
//			ReportInfoToDb(bufrDecoder2, tableSection, fileName);
			byte[] dataBytes={0};
			ReportInfoToDb(bufrDecoder2, tableSection, fileName,dataBytes);
			return this.decode(bufrDecoder2, fileName, tableSection, tables);
				
		} catch (Exception e) {
			e.printStackTrace();
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.cawn.bufr1.ar.service.OceanServiceImpl");
					ei.setKEvent("解码入库源文件异常：" + fileName);
					ei.setKIndex("详细信息：" + fileName);
					ei.setEVENT_EXT1(fileName);
					RestfulInfo restfulInfo = new RestfulInfo();
					restfulInfo.setType("SYSTEM.ALARM.EI ");
					restfulInfo.setName("数据解码入库EI告警信息");
					restfulInfo.setMessage("数据解码入库EI告警信息");
					
					restfulInfo.setFields(ei);
					List<RestfulInfo> restfulInfos = new ArrayList<>();
					restfulInfos.add(restfulInfo);
					RestfulSendData.SendData(restfulInfos, SendType.EI);
				}
			}
			return false;
			
		}
	}

	private final String SUCCEEDED_KEY = "SUCCEEDED";
	private final String PRIMARY_KEY_CONFLICT_KEY = "PRIMARY_KEY_CONFLICT";
	private final String OTHER_EXCEPTION_KEY = "OTHER_EXCEPTION";
	
	@SuppressWarnings("unchecked")
	public boolean decode(BufrDecoder2 bufrDecoder2, String fileName, String tableSection, List<String> tables) {
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		File fileN = new File(fileName);
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		// 使要素表在键表之前，    xx_tab 在  xx_k_tab 之前
		Collections.sort(tables); // 升序
		Collections.reverse(tables); // 倒序
		boolean isValueTableRecordNull = true; // 插入要素表记录数是否为0
		
//		String V_BBB=String.valueOf(bufrDecoder2.updateSequence);//更正报
		try {
		
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb"); // c.a.druid.pool.DruidDataSource - {dataSource-1} inited -11-28
			//connection = MySqlPool.getConnection("mysql.rdb");
			statement = connection.createStatement();
			Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
			if(connection.getAutoCommit() == true)
				connection.setAutoCommit(false);
//			statement.execute("select last_txc_xid()");
	
			//infoLogger.debug("\n connection: " + connection);
			//infoLogger.info("\n connection: " + connection);
			boolean dateTimeOK=true;
			for(Map<String, Object> members: bufrDecoder2.parseList){
				dateTimeOK=true;
				String sta = "";
				double latitude = 999999;
				double longitude = 999999;
				String datetime = "";
				String starttime="";
				String endtime="";
				String V_BBB="";
				for (String tableName : tables) {
					dateTimeOK=true;
					tableName = tableName.trim();
					XmlBean xmlBean = configMap.get(tableName);
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
					if(helpMap != null)
						entityMap.putAll(helpMap);
					//**********************************
					//infoLogger.info("\n connection176: " + connection);
					String isInsert = xmlBean.getIsInsert();
					//如果此字段为空，则表示可以直接入库，如果不为空，需判断此值代表的字段值是否为空，如果不为空，可入库，为空不入库
					//infoLogger.debug("\n connection: " + connection);
					if(StringUtils.isNotBlank(isInsert)) {
						Matcher columnMatcher = Pattern.compile(COLUMN_EXP_REGEX).matcher(isInsert);
						//表达式暂时只写一个${col:column}
						if (columnMatcher.find()) {
							String expCol = columnMatcher.group(1); // 字段
							// 获取表达式代表的配置
							EntityBean ebean = entityMap.get(expCol);
							String fxy = ebean.getFxy();
							// 获取表达式代表的对象
							Object object = members.get(fxy);
							if (object == null) {
								Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
								succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
								tmpRecord.put(SUCCEEDED_KEY, succeededNum);
								continue;
							}
							
							Object isValue = calExpressionValue(object, members, entityMap, ebean);
							if(isValue == null || ebean.getDefaultValue().equals(isValue.toString())) {
								Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
								succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
								tmpRecord.put(SUCCEEDED_KEY, succeededNum);
								continue;
							}
						//	infoLogger.debug("\n connection1: " + connection);
	
						}else {
							infoLogger.error("表配置文件根属性配置错误:" + isInsert);
							return false;
						}
					}
					//**********************************
					// 用于存储入库的字段
					StringBuffer keyBuffer = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer = new StringBuffer();
					// 遍历配置信息
					Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
					List<String> sqList = new ArrayList<>();
					//要素表
					Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();

					if(groupMapList != null && groupMapList.size() > 0){
						Set groupKey = groupMapList.keySet();
						Iterator<String> groupItor = null;
						for(groupItor = groupKey.iterator(); groupItor.hasNext(); ){
							String key = groupItor.next();
							int span = Integer.parseInt(key.split("_")[1]);
							String repeated = key.split("_")[0];
							Map<String, EntityBean> groupMap = groupMapList.get(key);
							entityMap.putAll(groupMap); // 后面覆盖前面的
							int repteats = 0;
							String prefix =  repeated.substring(0, repeated.lastIndexOf("-") + 1);
							if((List<BufrBean>)members.get(repeated) != null && ((List<BufrBean>)members.get(repeated)).size() > 0)
								isValueTableRecordNull = false; // 要素表入库条数0，  不再入键表
							while((List<BufrBean>)members.get(repeated) != null){
								List<BufrBean> list = (List<BufrBean>) members.get(repeated);
								int size = list.size() / span;
								StatDi di = null;
								for(int p = 0; p < size; p ++){
									dateTimeOK=true;
									if(tableName.toUpperCase().startsWith("CAWN_CHN_AR_TAB")){ //资料 C.0001.0013.R001，只入库这一张表，一个台站，时间不同，多条记录
										di = new StatDi();
										di.setPROCESS_START_TIME(TimeUtil.getSysTime());
										di.setTT("BUFR");
										di.setDATA_TYPE_1(StartConfig.ctsCode());
										di.setFILE_NAME_N(fileName);
										di.setFILE_NAME_O(fileName);
										File file = new File(fileName);
										di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm"));
										di.setFILE_SIZE(String.valueOf((new File(fileName)).length()));
										di.setBUSINESS_STATE("1");
										di.setPROCESS_STATE("1");
									}
									
									keyBuffer = new StringBuffer();
									valueBuffer = new StringBuffer();
									it = entityMap.entrySet().iterator();
									while(it.hasNext()){
										Entry<String, EntityBean> next = it.next();
										String column = next.getKey(); // 配置文件配置的字段名称
										EntityBean entityBean = next.getValue(); // 配置的每个字段属性
										String datatype = entityBean.getDatatype(); // 字段类型
										String expression = entityBean.getExpression(); // 此字段的表达式
										String fxy = entityBean.getFxy();
										if(fxy.startsWith(prefix)){
											entityBean.setFxy(repeated);
											fxy = repeated;
										}
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
												if(column.equalsIgnoreCase("D_SOURCE_ID"))
													value =value+"_"+fileN.getName();
											}
											keyBuffer.append(",`").append(column).append("`");
											valueBuffer.append(",'").append(value.toString().trim()).append("'");
											if(tableName.toUpperCase().startsWith("CAWN_CHN_AR_TAB")){
												if(column.equals("D_DATETIME")){
													int idx01 = value.toString().lastIndexOf(":");
													if(idx01 > 16){
														infoLogger.error("资料时间错误！  " + fileName);
														break;
													}
													else{
														di.setDATA_TIME(value.toString().substring(0, idx01));
														try{
															SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
														    Date dataTime=simpleDateFormat.parse(value.toString());
															if(!TimeCheckUtil.checkTime(dataTime)){
																dateTimeOK = false;
																infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
																break;
															}
														}catch (Exception e) {
															dateTimeOK = false;
															infoLogger.error("资料时间错误！  " + fileName);
															break;
														}
													}
												}
												else if(column.equals("V01301"))
													di.setIIiii(value.toString());
												else if(column.equals("V05001"))
													di.setLATITUDE(value.toString());
												else if(column.equals("V06001"))
													di.setLONGTITUDE(value.toString());
												else if(column.equals("V07001"))
													di.setHEIGHT(value.toString());
												else if(column.equals("D_DATA_ID"))
													di.setDATA_TYPE(value.toString());
												else if(column.equals("V_BBB"))
													di.setDATA_UPDATE_FLAG(value.toString());
											}
											System.out.println(fxy  + "-300------" + value);
											continue;
										}
										// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
										value = calExpressionValue(obj, members, entityMap, entityBean);
										if(tableName.toLowerCase().startsWith("CAWN_CHN_AR_TAB") && helpMap.containsKey(column)){ // 要素表
											continue;
										}
										//测站
										if(column.toUpperCase().equals("V01301") && value.toString().trim().startsWith("999999")){
											try{
												Double area = (Double)((BufrBean) members.get("0-1-3-0")).getValue();
												Double subArea = (Double)((BufrBean) members.get("0-1-20-0")).getValue();
												Double buoy_platformID = (Double)((BufrBean) members.get("0-1-5-0")).getValue();
												value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
											}catch (Exception e) {
												infoLogger.error("WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
											}
										}
										if(column.equals("D_DATETIME")){
											int idx01 = value.toString().lastIndexOf(":");
											if(idx01 > 16){
												infoLogger.error("资料时间错误！  " + fileName);
												break;
											}
											else{
												di.setDATA_TIME(value.toString().substring(0, idx01));
												try{
													SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												    Date dataTime=simpleDateFormat.parse(value.toString());
													if(!TimeCheckUtil.checkTime(dataTime)){
														dateTimeOK = false;
														infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
														break;
													}
												}catch (Exception e) {
													dateTimeOK = false;
													infoLogger.error("资料时间错误！  " + fileName);
													break;
												}
											}
										}
										else if(column.equals("V01301"))
											di.setIIiii(value.toString());
										else if(column.equals("V05001"))
											di.setLATITUDE(value.toString());
										else if(column.equals("V06001"))
											di.setLONGTITUDE(value.toString());
										else if(column.equals("V07001"))
											di.setHEIGHT(value.toString());
										else if(column.equals("D_DATA_ID"))
											di.setDATA_TYPE(value.toString());
										else if(column.equals("V_BBB"))
											di.setDATA_UPDATE_FLAG(value.toString());
										
										
										keyBuffer.append(",`").append(column).append("`");
										this.appendValue(valueBuffer, datatype, value.toString().trim());
									
									}
									if(dateTimeOK == false)
										continue;
									di.setSEND("BFDB");
									di.setSEND_PHYS("DRDS");
									di.setPROCESS_END_TIME(TimeUtil.getSysTime());
									di.setRECORD_TIME(TimeUtil.getSysTime());
									
									String sql = "insert into " + tableName //
											+ " (" //
											+ keyBuffer.toString().substring(1) //
											+ ") values ("//
											+ valueBuffer.toString().substring(1) //
											+ ")";
		  							System.out.println(sql);
									sqList.add(sql);
									for(int c = 0; c < span; c++)
										list.remove(0);
								
								}// end for
								repteats ++;
								repeated = prefix + String.valueOf(repteats);
								if(tableSection.equals("G.0003.0004.R001"))
									break;
							} // end while
							int len = sqList.size();
							// 剩余未入库记录数 
							int remain = sqList.size();
							if(remain > batchSize)
								len = batchSize;
							for(int t = 0; t < sqList.size();){
								for(int tt = t; tt < t + len; tt ++){
									statement.addBatch(sqList.get(tt));
								}
								try{
									statement.executeBatch();
									connection.commit();
									infoLogger.info("酸雨bufr要素表批量提交成功！"+fileName);
								}catch (Exception e) {
									infoLogger.error("酸雨bufr要素表批量提交失败：\n" + e.getMessage());
									for(int tt = t; tt < t + len; tt ++){
										try{
											statement.execute(sqList.get(tt));
											connection.commit();
										}catch (Exception e1) {
											if(tableName.toUpperCase().startsWith("G.0003.0004.R001") && tt < DI.size())
												DI.get(tt).setPROCESS_STATE("0");
											infoLogger.error("要素表单条记录插入失败： \n" + sqList.get(tt) +"\n" + e1.getMessage());
										}
									}
								}
								t += len;
								remain -= len;
								len = remain;
								if(remain > batchSize)
									len = batchSize;
							}
						}// end if
					}
					// 除了要素表之外的表
					else{
						if(isValueTableRecordNull == true && tableName.toLowerCase().startsWith("CAWN_CHN_AR"))
							continue;
						StatDi di = new StatDi();
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setTT("BUFR");
						di.setDATA_TYPE_1(StartConfig.ctsCode());
						di.setFILE_NAME_N(fileName);
						di.setFILE_NAME_O(fileName);
						while (it.hasNext()) {
							Entry<String, EntityBean> next = it.next();
							String column = next.getKey(); // 配置文件配置的字段名称
							EntityBean entityBean = next.getValue(); // 配置的每个字段属性
							String datatype = entityBean.getDatatype(); // 字段类型
							String expression = entityBean.getExpression(); // 此字段的表达式
							String fxy = entityBean.getFxy(); // 解码时存储数据的key
//							if("1-1-3-1".equals(fxy)){
//								System.out.println(1122);
//							}
							
							String defaultValue = entityBean.getDefaultValue(); // 缺省值
							Object obj = members.get(fxy); // 根据fxy获取每一行数据
							// 如果获取的对象为null
							Object value = null;
							if (obj == null) {
								// 如果表达式不为空说明此值需要根据表达式进行计算value
								if (StringUtils.isNotBlank(expression)) {
									// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
									Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
									Matcher em2 = Pattern.compile(FXY_VALUE_EXP).matcher(expression);
									if (em.find()) {
										value = defaultValue;
									} else if(em2.find()||"V04307".equals(column)||"V04308".equals(column)||"V12501_01".equals(column)||"V12501_02".equals(column)){
										value = defaultValue;
									}else {// 如果表达式存在，且其中没有以index取值
										value = calExpressionValue(obj, members, entityMap, entityBean);
									}
								} else {
									value = defaultValue;
									if(column.equalsIgnoreCase("D_SOURCE_ID"))
										value =value+"_"+fileN.getName();
								}
								if(column.equalsIgnoreCase("V_BBB")){
									String filename=fileN.getName();
									String BBB=filename.substring(0,filename.indexOf(".")-1);
									if(BBB.endsWith("-CC")){
										value=filename.substring(filename.indexOf("-")+1,filename.indexOf("."));
									}else{
										value=defaultValue;
									}
									V_BBB=value.toString();
								}
								if(column.equalsIgnoreCase("V01301")){
									sta=value.toString();
								}
								if(column.equalsIgnoreCase("D_DATETIME")){
										datetime = value.toString();
								}
								if(column.equalsIgnoreCase("V04307")){
									starttime = value.toString();
								}
								if(column.equalsIgnoreCase("V04308")){
									endtime = value.toString();
								}
								if(column.equalsIgnoreCase("V05001")){
									latitude = Double.parseDouble(value.toString());
								}
								if(column.equalsIgnoreCase("V06001")){
									longitude = Double.parseDouble(value.toString());
								}
								
								keyBuffer.append(",`").append(column).append("`");
								valueBuffer.append(",'").append(value.toString().trim()).append("'");
								
								if(column.equals("D_DATETIME")){
									int idx01 = value.toString().lastIndexOf(":");
									if(idx01 > 16){
										infoLogger.error("资料时间错误！  " + fileName);
										break;
									}
									else{
										di.setDATA_TIME(value.toString().substring(0, idx01));
										try{
											SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    Date dataTime=simpleDateFormat.parse(value.toString());
											if(!TimeCheckUtil.checkTime(dataTime)){
												dateTimeOK = false;
												infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
												break;
											}
										}catch (Exception e) {
											dateTimeOK = false;
											infoLogger.error("资料时间错误！  " + fileName);
											break;
										}
									}
								}
								else if(column.equals("V01301"))
									di.setIIiii(value.toString());
								else if(column.equals("V05001"))
									di.setLATITUDE(value.toString());
								else if(column.equals("V06001"))
									di.setLONGTITUDE(value.toString());
								else if(column.equals("V07001"))
									di.setHEIGHT(value.toString());
								else if(column.equals("D_DATA_ID"))
									di.setDATA_TYPE(value.toString());
								else if(column.equals("V_BBB"))
									di.setDATA_UPDATE_FLAG(value.toString());
								
								System.out.println(column+ "  "  + fxy  + expression + "--423-----" + value);
								continue;
								
							}
		
							// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//							System.err.println(column);
							value = calExpressionValue(obj, members, entityMap, entityBean);
								
							if(column.equalsIgnoreCase("V_BBB")){
								String filename=fileN.getName();
								String BBB=filename.substring(0,filename.indexOf(".")-1);
								if(BBB.endsWith("-CC")){
									value=filename.substring(filename.indexOf("-")+1,filename.indexOf("."));
								}else{
									value=defaultValue;
								}
								V_BBB=value.toString();
							}
							if(column.equalsIgnoreCase("V01301")){
								sta=value.toString();
							}
							if(column.equalsIgnoreCase("D_DATETIME")){
								datetime = value.toString();
							}
							if(column.equalsIgnoreCase("V04307")){
								starttime = value.toString();
							}
							if(column.equalsIgnoreCase("V04308")){
								endtime = value.toString();
							}
							if(column.equalsIgnoreCase("V05001")){
								latitude = Double.parseDouble(value.toString());
							}
							if(column.equalsIgnoreCase("V06001")){
								longitude = Double.parseDouble(value.toString());
							}
							keyBuffer.append(",`").append(column).append("`");
							this.appendValue(valueBuffer, datatype, value.toString().trim());
							
							if(column.equals("D_DATETIME")){
								int idx01 = value.toString().lastIndexOf(":");
								if(idx01 > 16){
									infoLogger.error("资料时间错误！  " + fileName);
									break;
								}
								else{
									di.setDATA_TIME(value.toString().substring(0, idx01));
									try{
										SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    Date dataTime=simpleDateFormat.parse(value.toString());
										if(!TimeCheckUtil.checkTime(dataTime)){
											dateTimeOK = false;
											infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
											break;
										}
									}catch (Exception e) {
										dateTimeOK = false;
										infoLogger.error("资料时间错误！  " + fileName);
										break;
									}
								}
							}
							else if(column.equals("V01301"))
								di.setIIiii(value.toString());
							else if(column.equals("V05001"))
								di.setLATITUDE(value.toString());
							else if(column.equals("V06001"))
								di.setLONGTITUDE(value.toString());
							else if(column.equals("V07001"))
								di.setHEIGHT(value.toString());
							else if(column.equals("D_DATA_ID"))
								di.setDATA_TYPE(value.toString());
							else if(column.equals("V_BBB"))
								di.setDATA_UPDATE_FLAG(value.toString());
							
							//infoLogger.info("\n connection: fileName  " + fxy  + "-------" + value);
							System.out.println(column+ "  "  + fxy  +expression+ "--445-----" + value);
						}// end while
						if(dateTimeOK == false)
							continue;
						File file = new File(fileName);
						//infoLogger.info("\n connection: fileName 495 " + fileName);
						di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setFILE_SIZE(String.valueOf((new File(fileName)).length()));
						di.setBUSINESS_STATE("1");
						di.setPROCESS_STATE("1");
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						
						DI.add(di);
						// 键表入库
						String sql = "insert into " + tableName //
								+ " (" //
								+ keyBuffer.toString().substring(1) //
								+ ") values ("//
								+ valueBuffer.toString().substring(1) //
								+ ")";
						System.out.println(sql);
						//infoLogger.debug("\n insert into sql: " + sql);
						//infoLogger.info("\n insert into sql: " + sql);
						
						//检查库里是否有数据，做更正判断
						double latiup=latitude+0.0001;
						double latidown=latitude-0.0001;
						double longup=longitude+0.0001;
						double longdown=longitude-0.0001;
						String sqlCheck = "select V_BBB from "+tableName+" where  D_DATETIME = '"+datetime+"' and V01301 = '"+sta+"' and "
								+ "V04307 ='"+starttime+"' and V04308='" +endtime+ "' and V05001 >="+latidown+" and V05001<="+latiup+" and "
								+ "V06001 >="+longdown+" and V06001 <="+longup+" limit 1";
						ResultSet resultSet = statement.executeQuery(sqlCheck);
						String bbb=null;
						if(resultSet.next()){
							bbb=resultSet.getString(1);
						}
						if(bbb!=null){//库里原先有数据
							if(V_BBB.startsWith("CC")&& V_BBB.compareTo(bbb) > 0){//更正报到来需要删除库里原有数据
								String deleteSQL="delete from "+tableName+" where  D_DATETIME = '"+datetime+"' and V01301 = '"+sta+"' and "
										+ "V04307 ='"+starttime+"' and V04308='" +endtime+ "' and V05001 >="+latidown+" and V05001<="+latiup+" and "
										+ "V06001 >="+longdown+" and V06001 <="+longup;
								try{
									statement.execute(deleteSQL);
									connection.commit();
									infoLogger.info("成功删除一条需更正数据: " + deleteSQL + "\n" );
								}catch (Exception e) {
	//								e.printStackTrace();
									infoLogger.error("Delete record failed: " + deleteSQL + "\n" + e.getMessage());
								}
							}
						}
						//报文入库
						try {
							int executeUpdate = statement.executeUpdate(sql);
							connection.commit();
							if (executeUpdate > 0) {
								infoLogger.info(tableName + " Insert a data successfully！ " + fileName);
								
								Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
								succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
								tmpRecord.put(SUCCEEDED_KEY, succeededNum);
							} else {
								infoLogger.info("入库失败:\n  " + sql);
								di.setPROCESS_STATE("0");
								Integer otherExceptionNum = tmpRecord.get(OTHER_EXCEPTION_KEY);
								otherExceptionNum = (otherExceptionNum == null ? 1 : (otherExceptionNum +=1));
								tmpRecord.put(OTHER_EXCEPTION_KEY, otherExceptionNum);
							}
						} catch (MySQLIntegrityConstraintViolationException e) {
							infoLogger.error("主键冲突:\n  " + sql + e.getMessage());
							Integer conflictNum = tmpRecord.get(PRIMARY_KEY_CONFLICT_KEY);
							conflictNum = (conflictNum == null ? 1 : (conflictNum +=1));
							tmpRecord.put(PRIMARY_KEY_CONFLICT_KEY, conflictNum);
							di.setPROCESS_STATE("0");
						}catch (Exception e) {
							infoLogger.error("插入失败:\n " + sql + e.getMessage());
							di.setPROCESS_STATE("0");
						}
					}// end else
				} // end tables loop
				if(DI != null){
					for (int j = 0; j < DI.size(); j++) {
						diQueues.offer(DI.get(j));
					}
					DI.clear();
				}
			} // end parseList loop 
			return true;
		} catch (SQLException e) {
			infoLogger.error("数据库异常: " + e.getMessage());
			return false;
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

//	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean) {
//		String value = null;
////		if(entityBean.getColumn().equals("V06001")){
////			System.out.println("经度");
////		}
//		
//		String expression = entityBean.getExpression();
//		if (obj != null) {
//			expression = getColumnValue(obj, entityBean).toString();
//		}
//		if (StringUtils.isNotBlank(expression)) {
//
//			// ${col:V01301}
//			// 将带有col的表达式替换掉
//			Matcher columnMatcher = Pattern.compile(COLUMN_EXP_REGEX).matcher(expression);
//			while (columnMatcher.find()) {
//				String cExp = columnMatcher.group(0); // 带有${col:}的表达式
//				String expCol = columnMatcher.group(1); // 字段
//				// 获取表达式代表的配置
//				EntityBean ebean = entityMap.get(expCol);
//				String fxy = ebean.getFxy();
//				// 获取表达式代表的对象
//				Object obj2 = members.get(fxy);
//				if (obj2 == null) {
//					// 如果表达式获取字段值为空，直接返回默认值
//					return entityBean.getDefaultValue();
//				} else {
//					// 如果不为空，获取其对应的值
//					String columnValue = getColumnValue(obj2, ebean).toString();
//					if (columnValue.equals(ebean.getDefaultValue())) {
//						return columnValue;
//					}
//					if(ebean.getDatatype().equals("int")){
//						int idx = columnValue.indexOf(".");
//						if(idx >= 1)
//							columnValue = columnValue.substring(0, idx);
//					}
//					if(expCol.toUpperCase().equals("V01301") && columnValue.trim().startsWith("999999")){
//						try{
//							Double area = (Double)((BufrBean) members.get("0-1-3-0")).getValue();
//							Double subArea = (Double)((BufrBean) members.get("0-1-20-0")).getValue();
//							Double buoy_platformID = (Double)((BufrBean) members.get("0-1-5-0")).getValue();
//							columnValue = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
//						}catch (Exception e) {
//							infoLogger.error("WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
//						}
//					}
//					
//					String afterText = expression.substring(expression.indexOf(cExp));
//					String cExpReg = StringUtil.string2Regex(cExp);
//					afterText = afterText.replaceFirst(cExpReg, "");
//					if (afterText.startsWith(StringUtil.SPLIT_FLAG) || afterText.startsWith(StringUtil.SUBSTRING_FLAG)) {
//						// 存储整串表达式代表的值,list最后一个值即为最终结果
//						List<String> res = new ArrayList<String>();
//						// 用于存储一整串substring和split表达式，最后替换直值用
//						StringBuffer regSBuffer = new StringBuffer();
//						
//						StringUtil.formatString(columnValue, afterText, res, regSBuffer);
//						String resByStation = res.get(res.size() - 1); // 最终结果
//						expression = expression.replaceFirst(StringUtil.string2Regex(cExp + regSBuffer.toString()), resByStation);
//					} else {
//						String sp [] = columnValue.split("[+\\*]");
//						try{
//							sp[0] = String.format("%d", Integer.parseInt(sp[0]));
//						}
//						catch (Exception e1) {
//							try{
//								sp[0] = String.format("%.3f", Double.parseDouble(sp[0]));
//							}
//							catch (Exception e2) {
//							}
//						}
//						expression = expression.replaceFirst(cExpReg, sp[0]);
//					}
//
//				}
//			}
//
//			// 替换所有默认表达式
//			expression = StringUtil.formatExpressionContent(expression);
//			// StringUtil.replaceNumberExp(expression, texts);
//			// 替换${this}
//			Matcher thisMatcher = Pattern.compile(THIS_VALUE_EXP).matcher(expression);
//			while (thisMatcher.find()) {
//				if (StringUtils.isBlank(value)) {
//					return entityBean.getDefaultValue();
//				}
//				if(StringUtil.string2Regex(thisMatcher.group(0)) != null && value != null)
//				expression = expression.replaceFirst(StringUtil.string2Regex(thisMatcher.group(0)), value); // 如果value为null怎么办？
//			}
//			//转换字符串为ascii码
//			expression = StringUtil.formatStringAsciiExp(expression);
//
//		}
//		
//		value = StringUtil.calculteExp(expression);
//		if(value.equalsIgnoreCase("null"))
//			value = "999999";
//		return value;
//	}
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
//	private Object getColumnValue(Object obj, EntityBean entityBean) throws IndexOutOfBoundsException {
//		boolean isQc = entityBean.isQc();
//		// int index = entityBean.getIndex();
//		Object value = null;
//		// 如果是bufrBean对象，直接强制转换
//		if (obj instanceof BufrBean) {
//			BufrBean bufr = (BufrBean) obj;
//			if (isQc) {
//				if(bufr.getQcValues() != null)
//					value = bufr.getQcValues().get(0);
//				else 
//					value = 9;
//				int parseValue = Integer.parseInt(value.toString());
//				value = parseValue > 9 ? 9 : parseValue;
//			} else {
//				// commented by cuihongyuan
////				value = bufr.getValue();
//				String expression = entityBean.getExpression();
//				if (StringUtils.isNotBlank(expression)) {
//					Matcher numMatcher = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
//					if (numMatcher.find()) {
//						List<String> ts = new ArrayList<String>();
//						ts.add(bufr.getValue().toString());
//						value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
//					}else {
//						infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
//					}
//				}else {
//					value = bufr.getValue();
//				}
//			}
//		}else if(obj instanceof String){
//			value = String.valueOf(obj);
//		}else { // 不是bufrBean对象，那就是存储对象的list集合
//			@SuppressWarnings("unchecked")
//			List<BufrBean> bufrList = (List<BufrBean>) obj;
//
//			String expression = entityBean.getExpression();
//			if (StringUtils.isNotBlank(expression)) {
//				Matcher numMatcher = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
//				if (numMatcher.find()) {
//
//					String _index = numMatcher.group(1);
//					int index = Integer.parseInt(_index);
//					// 如果list大小小于index，说明index代表的值不存在，直接返回默认值即可
//					if ((index + 1) > bufrList.size()) {
//						return entityBean.getDefaultValue();
//					}
//
//					List<String> ts = new ArrayList<String>();
//					if (isQc) {
//						for (BufrBean bufrBean : bufrList) {
//							Object value2 = null;
//							if(bufrBean.getQcValues() != null)
//								value2 = bufrBean.getQcValues().get(0);
//							else 
//								value2 = 9;
//							int parseValue = Integer.parseInt(value2.toString());
//							value2 = parseValue > 9 ? 9 : parseValue;
//							ts.add(value2.toString());
//						}
//					} else {
//						for (BufrBean bufrBean : bufrList) {
//							Object value2 = bufrBean.getValue();
//							ts.add(value2.toString());
//						}
//					}
//
//					value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
//				} else {
//					infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
//				}
//			} else {
//				infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
//			}
//
//		}
//		return value;
//	}
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
			}
		}
		else if(obj instanceof String){
			value = String.valueOf(obj);
		}
		else { // 不是bufrBean对象，那就是存储对象的list集合
//			if("1-1-2-1".equals(entityBean.getFxy())){
//				System.out.println("aaa");
//			}
			String expression = entityBean.getExpression();
			List<Object> bufrList0 = (List<Object>) obj;
			if(bufrList0.get(0) instanceof BufrBean){//若list中为BufrBean格式
//			if(obj instanceof List<BufrBean>){
				List<BufrBean> bufrList1 = (List<BufrBean>) obj;
				if (StringUtils.isNotBlank(expression)) {
					Matcher numMatcher = Pattern.compile(FXY_VALUE_EXP).matcher(expression);
					if (numMatcher.find()) {
						String _index = numMatcher.group(1);
						int index = Integer.parseInt(_index);
						String fxy1 = numMatcher.group(3);
						// 如果list大小小于index，说明index代表的值不存在，直接返回默认值即可
						if ((index + 1) > bufrList1.size()) {
							return entityBean.getDefaultValue();
						}
						BufrBean bufrbean=(BufrBean)bufrList1.get(index);
						value=bufrbean.getValue();
					}else {
						infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
					}
				}else {
					infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
				}	
			}else{//若list中为Map<String, BufrBean>格式
				@SuppressWarnings("unchecked")
				List<Map<String, BufrBean>> bufrList = (List<Map<String, BufrBean>>) obj;
				if (StringUtils.isNotBlank(expression)) {
					String column = entityBean.getColumn();
					if("V04307".equals(column)||"V04308".equals(column)){
						String[] aar=expression.split(",");
						String value2="" ;
						String value3="";
						for(int i=0;i<aar.length;i++){
						Matcher numMatcher = Pattern.compile(FXY_VALUE_EXP).matcher(aar[i]);
							if (numMatcher.find()) {
								String _index = numMatcher.group(1);
								int index = Integer.parseInt(_index);
								String fxy1 = numMatcher.group(3);
								// 如果list大小小于index，说明index代表的值不存在，直接返回默认值即可
								if ((index + 1) > bufrList.size()) {
									return entityBean.getDefaultValue();
								}
								Map<String, BufrBean> map =  bufrList.get(index);
								BufrBean bufrBean=map.get(fxy1);
								if(bufrBean!=null){
									int bufrBeanValue=(int)(Double.parseDouble(bufrBean.getValue().toString()));//获取的值转换为int型
									try{
										value2=String.format("%02d", bufrBeanValue);//将int型的值转换为两位整数
									}catch(Exception e){
										infoLogger.error("日期数值转换失败！\n"+e.getMessage());
									}
									value3 +=value2;
								}else{
									value2="00";
									value3 +=value2;
								}
							}else{
								value2="00";
								value3 +=value2;
							}
						}
						 value=(Object)value3;
					}else{
						Matcher numMatcher = Pattern.compile(FXY_VALUE_EXP).matcher(expression);
						if (numMatcher.find()) {
						
							String _index = numMatcher.group(1);
							int index = Integer.parseInt(_index);
							String fxy1 = numMatcher.group(3);
							// 如果list大小小于index，说明index代表的值不存在，直接返回默认值即可
							if ((index + 1) > bufrList.size()) {
								return entityBean.getDefaultValue();
							}
							Map<String, BufrBean> map =  bufrList.get(index);
							Object value2 = null;
							BufrBean bufrBean=map.get(fxy1);
							if(bufrBean!=null){
								if(isQc){//是质控码
									if(bufrBean.getQcValues() != null)
										value2 = bufrBean.getQcValues().get(0);
									else 
										value2 = 9;
									int parseValue = Integer.parseInt(value2.toString());
									value2 = parseValue > 9 ? 9 : parseValue;
								}else{//不是质控码
									if("V13371_01_1".equals(column)||"V13371_01_2".equals(column)||"V13371_01_3".equals(column)||"V13371_01_701".equals(column)||"V13371_02_1".equals(column)||"V13371_02_2".equals(column)||"V13371_02_3".equals(column)||"V13371_02_701".equals(column)){
										int valuetmp=(int)(Double.parseDouble(bufrBean.getValue().toString()));
										if(valuetmp!=999999){
										double valuetmp2 =Double.parseDouble(bufrBean.getValue().toString());
										double valuetem3=(float)(valuetmp2*10000);
										value2=valuetem3;
										}else{
											value2=bufrBean.getValue();
										}
									}else{
									  value2 = bufrBean.getValue();
									}
								}
							}else{//bufrBean为null
								value2=entityBean.getDefaultValue();
							}
	//						value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
							value = value2;
							
							if("V12501_01".equals(column)||"V12501_02".equals(column)){//初次/复测样品温度
								int valuetmp=(int)(Double.parseDouble(bufrBean.getValue().toString()));
								 if(valuetmp!=999999){
									 double value22=Double.parseDouble(value.toString());
									 double value33=value22-273.2;
									 value=(Object)value33;
								 }
								
							}
						} else {
							infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
						}
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
	/**
	 * 入服务库报告表
	 * @param bufrDecoder
	 * @param tableSection
	 * @param fileName
	 */
	 public  void ReportInfoToDb(BufrDecoder2 bufrDecoder,String tableSection, String fileName,byte[] dataBytes) {
		 	int successOrNot=bufrDecoder.successOrNot;
			DruidPooledConnection report_connection = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//			File file = new File(fileName);
//			String filename = file.getName();
//			Date recv = new Date(file.lastModified());
			String ReportTableName=StartConfig.reportTable();
			String sod_code=StartConfig.reportSodCode();
//			String ReportTableName="CAWN_WEA_GLB_BULL_BUFR_TAB";
//			String sod_code="G.0003.0004.S001";
			
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
			String Latitude="999999";
			String Longitude="999999";
			String height="999999";
			int length=0;
			if(StartConfig.getDataPattern()==0){
				length=dataBytes.length;
			}else if(StartConfig.getDataPattern()==1){
				File file=new File(fileName);
				length=(int)file.length();
			}
			
			PreparedStatement statement = null;
			try {
				report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
				statement = new LoggableStatement(report_connection, sql);
				report_connection.setAutoCommit(false);
				File file0=new File(fileName);
				String fileN=file0.getName();
				StatDi di = new StatDi();	
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(tableSection);
				di.setTRAN_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
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
							
							String latifxy="0-5-1-0";
							String longfxy="0-6-1-0";
							String heightfxy="0-7-30-0";
							if(members.get(latifxy)!=null){
								Latitude=((BufrBean)(members.get(latifxy))).getValue().toString();
							}
							if(members.get(longfxy)!=null){
								Longitude=((BufrBean)(members.get(longfxy))).getValue().toString();
							}
							if(members.get(heightfxy)!=null){
								height=((BufrBean)(members.get(heightfxy))).getValue().toString();
							}
							 V_CCCC = members.get("CCCC").toString();
							 V_BBB=members.get("BBB").toString();
							 V_TT=members.get("TTAAii").toString();
							 V_YYGGgg=members.get("YYGGgg").toString();
							 
							//查datetime
							StringBuffer datetime=new StringBuffer();//存单个datetime
							String[] date_time={"9999","01","01","00","00","00"};
							for(int i=0;i<6;i++){
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
							datetime.append(date_time[0]).append("-").append(date_time[1]).append("-").append(date_time[2]).append(" ").append(date_time[3]).append(":").append(date_time[4]).append(":").append(date_time[5]);
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
//						byte[] databyte=getContent(fileName);//报文二进制数组
						di.setIIiii(station_1);
						di.setDATA_TIME(TimeUtil.date2String(datetime1, "yyyy-MM-dd HH:mm"));
						
						int ii = 1;
						statement.setString(ii++, station_1 + "_" + sdf.format(datetime1));// d_record_id
						statement.setString(ii++, sod_code);//D_DATA_ID
						statement.setString(ii++, tableSection);//D_source_id
						statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
						statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_RYMDHM
						statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
						statement.setTimestamp(ii++, new Timestamp(datetime1.getTime()));//D_DATETIME
						statement.setString(ii++, station_1);//V01301
						statement.setString(ii++, V_BBB);//V_BBB
						statement.setString(ii++, V_CCCC);//V_CCCC
						statement.setString(ii++, V_YYGGgg);//V_YYGGgg
						statement.setString(ii++, V_TT);//V_TT
						statement.setLong(ii++, length);//V_LEN    BUFR报文长度 ?????????????
						statement.setLong(ii++, length);//V_file_size 文件大小
						statement.setBytes(ii++, dataBytes);//V_BULLETIN_B 公报内容
//							statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
						statement.setString(ii++,fileName);//V_FILE_NAME 文件名
						statement.setInt(ii++, bufrDecoder.parseList.size());//V_NUM 观测记录数
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
					
					di.setIIiii("999999");
					di.setDATA_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm"));
					
//					byte[] databyte=getContent(fileName);//报文二进制数组
					int ii = 1;
					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
					statement.setString(ii++, sod_code);//D_DATA_ID
					statement.setString(ii++, tableSection);//D_source_id
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_RYMDHM
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_DATETIME
					statement.setString(ii++, "999999");//V01301
					statement.setString(ii++, V_BBB);//V_BBB
					statement.setString(ii++, V_CCCC);//V_CCCC
					statement.setString(ii++, V_YYGGgg);//V_YYGGgg
					statement.setString(ii++, V_TT);//V_TT
					if(bufrDecoder.normalFlag==1){//---------------报文有正常BUFR消息，但解码失败
						statement.setLong(ii++, length);//V_LEN    BUFR报文长度 ?????????????
					}else{//--------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setLong(ii++, 0);//V_LEN    BUFR报文长度 ?????????????
					}
					statement.setLong(ii++, length);//V_file_size 文件大小
					statement.setBytes(ii++, dataBytes);//V_BULLETIN_B 公报内容
					statement.setString(ii++,fileName);//V_FILE_NAME 文件名
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
					di.setIIiii("999999");
					di.setDATA_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm"));
					byte[] databyte=new byte[0];
					int ii = 1;
					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
					statement.setString(ii++, sod_code);//D_DATA_ID
					statement.setString(ii++, tableSection);//D_source_id
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_RYMDHM
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
					statement.setString(ii++,fileName);//V_FILE_NAME 文件名
					statement.setInt(ii++, 0);//V_NUM 观测记录数
					statement.setString(ii++,null);//V01301_list 观测记录站号列表
					statement.setString(ii++,null);//d_datetime_list 观测记录时间列表
					statement.setInt(ii++,0);//V_decode_flag 解码是否成功标志
				}
				di.setTT(V_TT);	
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setLONGTITUDE(String.valueOf(Longitude));
				di.setLATITUDE(String.valueOf(Latitude));
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setFILE_SIZE(String.valueOf(new File(fileName).length()));
				di.setDATA_UPDATE_FLAG(V_BBB);
				di.setHEIGHT(String.valueOf(height));
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
//		public byte[] getContent(String filePath)throws IOException {  
//	        File file = new File(filePath);  
//	        long fileSize = file.length();  
//	        if (fileSize > Integer.MAX_VALUE) {  
//	            System.out.println("file too big...");  
//	            return null;  
//	        } 
//	       
//	        FileInputStream fi = new FileInputStream(file);  
//	        byte[] buffer = new byte[(int) fileSize];  
//	        int offset = 0;  
//	        int numRead = 0;  
//	        while (offset < buffer.length  
//	        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
//	            offset += numRead;  
//	        }  
//	        // 确保所有数据均被读取  
//	        if (offset != buffer.length) {  
//	        throw new IOException("Could not completely read file "  + file.getName());  
//	        }  
//	        fi.close();  
//	        return buffer;  
//	    }


}