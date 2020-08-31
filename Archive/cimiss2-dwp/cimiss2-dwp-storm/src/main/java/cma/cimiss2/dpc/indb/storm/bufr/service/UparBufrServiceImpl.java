/**  
 * All rights Reserved, Designed By www.huaxin-hitec.com
 * @Title:  UparBufrServiceImpl.java   
 * @Package com.hitec.bufr.service.impl   
 * @Description:    TODO(用一句话描述该文件做什么)   
 * @author: 吴佐强     
 * @date:   2018年10月18日 下午1:44:19   
 * @version V1.0 
 * @Copyright: 2018 www.huaxin-hitec.com Inc. All rights reserved. 
 * 注意：本内容仅限于华云信息技术工程有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package cma.cimiss2.dpc.indb.storm.bufr.service;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrDecoder;
import com.hitec.bufr.util.StringUtil;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.storm.tools.BufrConfig;
import cma.cimiss2.dpc.indb.storm.tools.ConnectionPoolFactory;

/**  
 * *********************************************************************** 
 * @ClassName:  UparBufrServiceImpl   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 吴佐强
 * @date:   2018年10月18日 下午1:44:19   
 *     
 * @Copyright: 2018 www.huaxin-hitec.com Inc. All rights reserved. 
 * 注意：本内容仅限于华云信息技术工程有限公司内部传阅，禁止外泄以及用于其他的商业目 
 * ***********************************************************************
 */
public class UparBufrServiceImpl {
	/*
	int batchSize = 500;
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> DI = new ArrayList<StatDi>();
	public static void main(String[] args) {
		
		List<String> tableList = new ArrayList<String>();
		String tableSection = "B.0001.0030.R001";
//		tableList.add("UPAR_WEA_GBF_MUL_FTM_K_TAB");
//		tableList.add("UPAR_WEA_GBF_MUL_FTM_TAB");
		tableList.add("UPAR_WEA_CBF_MUL_NSEC_K_TAB");
		tableList.add("UPAR_WEA_CBF_MUL_NSEC_TAB");
		tableList.add("upar_wea_cbf_para_tab");
		String fileName = "D:\\B30R1\\Z_UPAR_I_50527_20181123004527_O_TEMP-L-00_18112300_PQC.BIN";
		UparBufrServiceImpl ServiceImpl  = new UparBufrServiceImpl();
		String Priority = "0";
		boolean decode = ServiceImpl.decode(fileName, tableSection, tableList, Priority);
		if(decode){
			System.out.println("处理成功！");
		}else
			System.err.println("处理失败！");
//		bufrDecoder.bufrDecoder("D:\\A_ISAC01VHHH200300_C_BABJ_20180920031410_90199.bin");
	}

	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables, String Priority) {
		try {
			BufrDecoder bufrDecoder = new BufrDecoder();
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
				return this.decode(bufrDecoder, name, tableSection, tables, Priority);
			} else {
				infoLogger.error("\n Read file error: "+ name);
//				String event_type = EIEventType.OP_FILE_ERROR.getCode();
//				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
//				if(ei == null) {
//					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
//				}else {
//					if(StartConfig.isSendEi()) {
//						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
//						ei.setKObject("cma.cimiss2.dpc.indb.upar.bufr.service.UparBufrServiceImpl");
//						ei.setKEvent("解码入库源文件异常：" + name);
//						ei.setKIndex("详细信息：" + name);
//						ei.setEVENT_EXT1(name);
//						RestfulInfo restfulInfo = new RestfulInfo();
//						restfulInfo.setType("SYSTEM.ALARM.EI ");
//						restfulInfo.setName("数据解码入库EI告警信息");
//						restfulInfo.setMessage("数据解码入库EI告警信息");
//						
//						restfulInfo.setFields(ei);
//						List<RestfulInfo> restfulInfos = new ArrayList<>();
//						restfulInfos.add(restfulInfo);
//						RestfulSendData.SendData(restfulInfos, SendType.EI);
//					}
//				}
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables, String Priority) {
		BufrDecoder bufrDecoder = new BufrDecoder();
		Long start = System.currentTimeMillis();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		infoLogger.info("解码花费时间：" + (System.currentTimeMillis() - start) + " ms!");
		if (isRead) {
			return this.decode(bufrDecoder, fileName, tableSection, tables, Priority);
		} else {
			infoLogger.error("\n Read file error: "+ fileName);
//			String event_type = EIEventType.OP_FILE_ERROR.getCode();
//			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
//			if(ei == null) {
//				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
//			}else {
//				if(StartConfig.isSendEi()) {
//					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
//					ei.setKObject("cma.cimiss2.dpc.indb.upar.bufr.service.UparBufrServiceImpl");
//					ei.setKEvent("解码入库源文件异常：" + fileName);
//					ei.setKIndex("详细信息：" + fileName);
//					ei.setEVENT_EXT1(fileName);
//					RestfulInfo restfulInfo = new RestfulInfo();
//					restfulInfo.setType("SYSTEM.ALARM.EI ");
//					restfulInfo.setName("数据解码入库EI告警信息");
//					restfulInfo.setMessage("数据解码入库EI告警信息");
//					
//					restfulInfo.setFields(ei);
//					List<RestfulInfo> restfulInfos = new ArrayList<>();
//					restfulInfos.add(restfulInfo);
//					RestfulSendData.SendData(restfulInfos, SendType.EI);
//				}
//			}
			return false;
		}
	}

	private final String SUCCEEDED_KEY = "SUCCEEDED";
	private final String PRIMARY_KEY_CONFLICT_KEY = "PRIMARY_KEY_CONFLICT";
	private final String OTHER_EXCEPTION_KEY = "OTHER_EXCEPTION";
	
	@SuppressWarnings("unchecked")
	public boolean decode(BufrDecoder bufrDecoder, String fileName, String tableSection, List<String> tables, String Prio) {
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		// 使要素表在键表之前，    xx_tab 在  xx_k_tab 之前
//		Collections.sort(tables); // 升序
//		Collections.reverse(tables); // 倒序
		boolean isFTMValueTableRecordNull = true; // 插入要素表记录数是否为0
		boolean isSecValueTableRecordNull = true; // 插入妙级要素表记录数是否为0
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if(diQueues == null)
				diQueues = new LinkedBlockingQueue<StatDi>();
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			statement = connection.createStatement();
//			LinkedList<Map<String, Object>> memberss = new LinkedList<Map<String, Object>>( bufrDecoder.getParseList());
			for(Map<String, Object> members: bufrDecoder.getParseList()){ //
				int levelNumber = 0;
				for (String tableName : tables) {
					boolean isDeleteExecuted = false;
					tableName = tableName.trim();
					System.out.println(tableName);
					XmlBean xmlBean = configMap.get(tableName);
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
					if(helpMap != null)
						entityMap.putAll(helpMap);
					//**********************************
					String isInsert = xmlBean.getIsInsert();
					//如果此字段为空，则表示可以直接入库，如果不为空，需判断此值代表的字段值是否为空，如果不为空，可入库，为空不入库
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
//					List<String> sqList = new ArrayList<>();
					//要素表
					Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
					//要素表入库
					
					if(groupMapList != null && groupMapList.size() > 0){
						Set groupKey = groupMapList.keySet();  // "延迟重复编码_重复要素数目" 作为key
						Iterator<String> groupItor = null;
						for(groupItor = groupKey.iterator(); groupItor.hasNext(); ){
							String key = groupItor.next();
							int span = Integer.parseInt(key.split("_")[1]);
							String groupFXY = key.split("_")[0];
							Map<String, EntityBean> groupMap = groupMapList.get(key);
							entityMap.putAll(groupMap); // 后面覆盖前面的
							List<BufrBean> list = new ArrayList<BufrBean>((List<BufrBean>) members.get(groupFXY));
							String d_record_id = "";
							if(list != null && list.size() > 0){
								if(tableName.toUpperCase().contains("FTM"))
									isFTMValueTableRecordNull = false;
								if(tableName.toUpperCase().contains("NSEC"))
									isSecValueTableRecordNull = false; 
								int fullLength = list.size();
								int size = fullLength / span;
								levelNumber = size;
								keyBuffer = new StringBuffer();
								valueBuffer = new StringBuffer();
								String sql = "";
								Long StartT = System.currentTimeMillis();
								Entry<String, EntityBean> next = null;
								EntityBean entityBean = null;
								Object obj = null;
								int sqlCount = 0;
								List<BufrBean> listt = null;
								for(int loop = 0; loop < fullLength; loop += span){
									listt = list.subList(loop, loop + span);			
									members.put(groupFXY, listt);
									it = entityMap.entrySet().iterator();
									while(it.hasNext()){
										next = it.next();
										String column = next.getKey(); // 配置文件配置的字段名称
										entityBean = next.getValue(); // 配置的每个字段属性
										String datatype = entityBean.getDatatype(); // 字段类型
										String expression = entityBean.getExpression(); // 此字段的表达式
										String fxy = entityBean.getFxy();
										String defaultValue = entityBean.getDefaultValue(); // 缺省值
										obj = members.get(fxy); // 根据fxy获取每一行数据
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
											}
											if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
												value = members.get("CCCC");
											if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
												value = members.get("TTAAii");
											if(column.equalsIgnoreCase("V_BBB"))
												value = members.get("BBB");
											if(column.equalsIgnoreCase("V_PRIORITY"))
												value = Prio;
											// 资料时间规整
											if(column.equalsIgnoreCase("D_DATETIME")){
												try {
													Date dataTime = new Date(simpleDateFormat2.parse(value.toString()).getTime());
													int hours = dataTime.getHours();
													hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
													dataTime.setHours(hours);
													value = simpleDateFormat2.format(dataTime.getTime());
												} catch (ParseException e) {
													infoLogger.error("资料时间格式转换错误！" + fileName);
													break;
												}
											}
											
											keyBuffer.append(",`").append(column).append("`");
											valueBuffer.append(",'").append(value.toString().trim()).append("'");
											
											if(column.equalsIgnoreCase("D_RECORD_ID"))
												d_record_id = value.toString();
											
											continue;
										}
										// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
										value = calExpressionValue(obj, members, entityMap, entityBean);
										
										if(column.equalsIgnoreCase("V04004_02")){
											value = ((Integer.parseInt(value.toString()) + 23) % 24 / 3 + 1) * 3 % 24;
										}
										if(!helpMap.containsKey(column)){
											keyBuffer.append(",`").append(column).append("`");
											this.appendValue(valueBuffer, datatype, value.toString().trim());
										}
									} // end while
									sql = "insert into " + tableName //
											+ " (" //
											+ keyBuffer.toString().substring(1) //
											+ ") values ("//
											+ valueBuffer.toString().substring(1) //
											+ ")";
									valueBuffer.setLength(0);
									keyBuffer.setLength(0);
									// 如果未执行，那么先执行delete
									Statement DelStatement = null;
									if(isDeleteExecuted == false){
										String deleteSQL = "DELETE from " + tableName + " where d_record_id = " + "'" + d_record_id + "'";
										try{
											DelStatement = connection.createStatement();
											if(connection.getAutoCommit() == false)
												connection.setAutoCommit(true);
											DelStatement.execute(deleteSQL);
											isDeleteExecuted = true;
										}catch (Exception e) {
											infoLogger.error("要素表记录删除失败：\n" + deleteSQL + "\n" +  e.getMessage());
										}
										finally{
											try{
												DelStatement.close();
											}catch (Exception e) {
												infoLogger.equals("statement.close(); failed!");
											}
										}
									}
									sqlCount ++;
									statement.addBatch(sql);
									if(sqlCount == batchSize || loop == fullLength - span){
										sqlCount = 0;
										try{
											if(connection.getAutoCommit() == true)
												connection.setAutoCommit(false);
											statement.executeBatch();
											connection.commit();
										}catch (Exception e) {
											infoLogger.error("高空要素表批量提交失败：\n" + e.getMessage());
										}
									}
								}// end for
								System.out.println("要素表批量提交花费时间：" + (System.currentTimeMillis() - StartT) + " ms.");
								infoLogger.info("要素表批量提交花费时间：" + (System.currentTimeMillis() - StartT) + " ms.");
							}// end if
							
						} // end for
					}// end if
					// 除了要素表之外的表
					else{
						if(isFTMValueTableRecordNull == true && tableName.toUpperCase().contains("UPAR_WEA_GBF_MUL_FTM_K_TAB"))
							continue;
						if(isSecValueTableRecordNull == true && tableName.toUpperCase().contains("UPAR_WEA_CBF_MUL_NSEC_K_TAB"))
							continue;
						StatDi di = new StatDi();
						di.setPROCESS_START_TIME(simpleDateFormat2.format(new Date()));
						di.setTT("BUFRUPAR");
//						di.setDATA_TYPE_1(StartConfig.ctsCode());
						di.setFILE_NAME_N(fileName);
						di.setFILE_NAME_O(fileName);
						String D_record_id = "";
						while (it.hasNext()) {
							Entry<String, EntityBean> next = it.next();
							String column = next.getKey(); // 配置文件配置的字段名称
							EntityBean entityBean = next.getValue(); // 配置的每个字段属性
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
								}
								if(column.equalsIgnoreCase("V31001") && tableName.toUpperCase().contains("MUL_NSEC_K_TAB")){
									value = String.valueOf(levelNumber);
								}
								if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
									value = members.get("CCCC");
								if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
									value = members.get("TTAAii");
								if(column.equalsIgnoreCase("V_BBB"))
									value = members.get("BBB");
								if(column.equalsIgnoreCase("V_PRIORITY"))
									value = Prio;
								// 将世界时转化为北京时
								if(column.equalsIgnoreCase("V04300_017_02")){
									Calendar calendar = Calendar.getInstance();
									try {
										calendar.setTime(simpleDateFormat.parse(value.toString()));
										calendar.add(Calendar.HOUR, 8);
										value = simpleDateFormat.format(calendar.getTime());
										calendar = null;
									} catch (ParseException e) {
										calendar = null;
										infoLogger.error("施放时间世界时转化为地方时错误！");
									}
								}
								// 资料时间规整
								if(column.equalsIgnoreCase("D_DATETIME")){
									try {
										Date dataTime = new Date(simpleDateFormat2.parse(value.toString()).getTime());
										int hours = dataTime.getHours();
										hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
										dataTime.setHours(hours);
										value = simpleDateFormat2.format(dataTime.getTime());
									} catch (ParseException e) {
										infoLogger.error("资料时间格式转换错误！" + fileName);
										break;
									}
								}
								
								if(column.equalsIgnoreCase("V02421") && value.toString().startsWith("9999"))
									value = "999999";
								
								keyBuffer.append(",`").append(column).append("`");
								valueBuffer.append(",'").append(value.toString().trim()).append("'");
								if(column.equals("D_DATETIME"))
									di.setDATA_TIME(String.valueOf(value));
								if(column.equals("V01301"))
									di.setIIiii(value.toString());
								if(column.equals("V05001"))
									di.setLATITUDE(value.toString());
								if(column.equals("V06001"))
									di.setLONGTITUDE(value.toString());
								if(column.equals("D_DATA_ID"))
									di.setDATA_TYPE(value.toString());
								
								
								if(column.equalsIgnoreCase("D_RECORD_ID"))
									D_record_id = value.toString();
								
								continue;
							}
		
							// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//							System.err.println(column);
							value = calExpressionValue(obj, members, entityMap, entityBean);
							// 年、月、日、时、分、秒 值缺测时的设置
							if((column.toUpperCase().startsWith("V04004") || column.toUpperCase().startsWith("V04005")
									|| column.toUpperCase().startsWith("V04006"))&& value.toString().startsWith("999999"))
								value = 12;
							if((column.toUpperCase().startsWith("V04002") || column.toUpperCase().startsWith("V04003")) && value.toString().startsWith("999999"))
								value = 31;
							if(column.toUpperCase().startsWith("V04001") && value.toString().startsWith("999999"))
								value = 9999;
							
							if(column.equalsIgnoreCase("V04004_02")){
								value = ((Integer.parseInt(value.toString()) + 23) % 24 / 3 + 1) * 3 % 24;
							}
							
							if(helpMap != null){
								if(!helpMap.containsKey(column)){
									keyBuffer.append(",`").append(column).append("`");
									this.appendValue(valueBuffer, datatype, value.toString().trim());
								}
							}
							else{
								keyBuffer.append(",`").append(column).append("`");
								this.appendValue(valueBuffer, datatype, value.toString().trim());
							}
							
							if(column.equals("D_DATETIME"))
								di.setDATA_TIME(String.valueOf(value));
							if(column.equals("V01301"))
								di.setIIiii(value.toString());
							if(column.equals("V05001"))
								di.setLATITUDE(value.toString());
							if(column.equals("V06001"))
								di.setLONGTITUDE(value.toString());
							if(column.equals("D_DATA_ID"))
								di.setDATA_TYPE(value.toString());
						}// end while
						File file = new File(fileName);
						file.length();
//						di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
						di.setPROCESS_END_TIME(simpleDateFormat2.format(new Date()));
						di.setRECORD_TIME(simpleDateFormat2.format(new Date()));
						di.setFILE_SIZE(String.valueOf((new File(fileName)).length()));
						di.setBUSINESS_STATE("0");
						di.setPROCESS_STATE("0");
						DI.add(di);
						// 键表入库
						String sql = "insert into " + tableName //
								+ " (" //
								+ keyBuffer.toString().substring(1) //
								+ ") values ("//
								+ valueBuffer.toString().substring(1) //
								+ ")";
//						System.out.println(sql);
						String deleteSQL = "Delete from " + tableName + " where D_Record_id = " + "'" + D_record_id +  "'";
						Statement delStateKey = null;
						Statement insertStatKey = null;
						try{
							// 先删除
							delStateKey = connection.createStatement();
							if(connection.getAutoCommit() == false)
								connection.setAutoCommit(true);
							delStateKey.execute(deleteSQL);
						}catch (Exception e) {
							infoLogger.equals("删除失败：" + deleteSQL + "\n " + e.getMessage());
						}
						finally{
							try{
								delStateKey.close();
							}catch (Exception e) {
								infoLogger.error("delStateKey.close() failed.");
							}
						}
						try {// 手动提交
							//插入
							insertStatKey = connection.createStatement();
							if(connection.getAutoCommit() == false)
								connection.setAutoCommit(true);
							insertStatKey.execute(sql);
//							connection.commit();
						} catch (MySQLIntegrityConstraintViolationException e) {
							infoLogger.error("主键冲突: " + sql + "\n" + e.getMessage());
							Integer conflictNum = tmpRecord.get(PRIMARY_KEY_CONFLICT_KEY);
							conflictNum = (conflictNum == null ? 1 : (conflictNum +=1));
							tmpRecord.put(PRIMARY_KEY_CONFLICT_KEY, conflictNum);
							di.setPROCESS_STATE("1");
						}catch (Exception e) {
							infoLogger.error("插入失败:" + sql + "\n " + e.getMessage());
							di.setPROCESS_STATE("1");
						}
						finally {
							try{
								insertStatKey.close();
							}catch (Exception e) {
								infoLogger.error("insertStatKey.close() failed.");
							}
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
			try {
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean) {
		String value = null;
//		if(entityBean.getColumn().equals("V06001")){
//			System.out.println("经度");
//		}
		
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
					
					if (columnValue.equals(ebean.getDefaultValue())) {
						expression = expression.replaceFirst(cExpReg, columnValue);
//						continue;
//						return columnValue;
					}
					if(columnValue.startsWith("999999.0") && ebean.getDefaultValue() != null)
						columnValue = ebean.getDefaultValue();
					if(ebean.getDatatype().equals("int")){
						int idx = columnValue.indexOf(".");
						if(idx >= 1)
							columnValue = columnValue.substring(0, idx);
					}
//					System.out.println(expression + "====" + cExp);、
					if(expression.indexOf(cExp) == -1)
						infoLogger.info("expression.indexOf(cExp)" + expression + "\t " + cExp + "" + expression.indexOf(cExp));
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
						try{
							sp[0] = String.format("%d", Integer.parseInt(sp[0]));
						}
						catch (Exception e1) {
							try{
								sp[0] = String.format("%.3f", Double.parseDouble(sp[0]));
							}
							catch (Exception e2) {
								System.out.println(expression);
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
						ts.add(bufr.getValue().toString());
						value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
					}else {
						infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
					}
				}else {
					value = bufr.getValue();
				}
			}
		} else { // 不是bufrBean对象，那就是存储对象的list集合
			
			String expression = entityBean.getExpression();
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

	@Override
	public boolean decode(String fileName, String CCCC, String tableSection, List<String> tables, List<StatDi> DI,
			List<RestfulInfo> EI) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean decode(String name, String CCCC, byte[] dataBytes, String tableSection, List<String> tables,
			List<StatDi> DI, List<RestfulInfo> EI) {
		// TODO Auto-generated method stub
		return false;
	}

*/

}
