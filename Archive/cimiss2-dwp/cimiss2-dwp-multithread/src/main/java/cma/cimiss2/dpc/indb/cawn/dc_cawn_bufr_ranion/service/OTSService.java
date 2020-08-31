package cma.cimiss2.dpc.indb.cawn.dc_cawn_bufr_ranion.service;

import java.io.File;
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
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrDecoder2;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class OTSService implements BufrService {
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
		tableList.add("CAWN_CBF_AR_TAB");
//		String fileName = "D:\\usr\\data\\20181210\\01\\Z_CAWN_I_58402_20181210015400_O_AR_FTM_PQC.BIN";
		String fileName = "D:\\Z_CAWN_I_58203_20180920010400_O_AR_FTM_PQC.BIN";
		OTSService otsService = new OTSService();
		boolean decode = otsService.decode(fileName, tableSection, tableList);
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
		OTSService.diQueues = diQueues;
	}

	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrDecoder2 bufrDecoder2 = new BufrDecoder2();
			bufrDecoder2.decodeFile(name, dataBytes);
			
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
		File fileN=new File(fileName);
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		// 使要素表在键表之前，    xx_tab 在  xx_k_tab 之前
		Collections.sort(tables); // 升序
		Collections.reverse(tables); // 倒序
		boolean isValueTableRecordNull = true; // 插入要素表记录数是否为0
		int successRowCount = 0;
		try {
			
			Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
			
//			statement.execute("select last_txc_xid()");
	
			//infoLogger.debug("\n connection: " + connection);
			//infoLogger.info("\n connection: " + connection);
			for(Map<String, Object> members: bufrDecoder2.parseList){
				for (String tableName : tables) {
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
									if(tableName.toUpperCase().startsWith("CAWN_CBF_AR_TAB")){ //资料 C.0001.0013.R001，只入库这一张表，一个台站，时间不同，多条记录
										di = new StatDi();
										di.setPROCESS_START_TIME(TimeUtil.getSysTime());
										di.setTT("BUFR");
										di.setDATA_TYPE_1(StartConfig.ctsCode());
										di.setFILE_NAME_N(fileName);
										di.setFILE_NAME_O(fileName);
										File file = new File(fileName);
										file.length();
										di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
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
											if(tableName.toUpperCase().startsWith("CAWN_CBF_AR_TAB")){
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
											}
											System.out.println(fxy  + "-300------" + value);
											continue;
										}
										// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
										value = calExpressionValue(obj, members, entityMap, entityBean);
										if(tableName.toLowerCase().startsWith("CAWN_CBF_AR_TAB") && helpMap.containsKey(column)){ // 要素表
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
										
										keyBuffer.append(",`").append(column).append("`");
										this.appendValue(valueBuffer, datatype, value.toString().trim());
									
									}
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
							
								
						}// end if
					}
					// 除了要素表之外的表
					else{
						if(isValueTableRecordNull == true && tableName.toLowerCase().startsWith("CAWN_CBF_AR"))
							continue;
						StatDi di = new StatDi();
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setTT("BUFR");
						di.setDATA_TYPE_1(StartConfig.ctsCode());
						di.setFILE_NAME_N(fileName);
						di.setFILE_NAME_O(fileName);
						Map<String, Object> row = new HashMap<>();
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

								row.put(column, value);
								
								
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
								System.out.println(column+ "  "  + fxy  + expression + "--423-----" + value);
								continue;
								
							}
		
							// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//							System.err.println(column);
							value = calExpressionValue(obj, members, entityMap, entityBean);
							//入map
							row.put(column, value);
							
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
							//infoLogger.info("\n connection: fileName  " + fxy  + "-------" + value);
							System.out.println(column+ "  "  + fxy  +expression+ "--445-----" + value);
						}// end while
						File file = new File(fileName);
						file.length();
						//infoLogger.info("\n connection: fileName 495 " + fileName);
						di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setFILE_SIZE(String.valueOf((new File(fileName)).length()));
						di.setBUSINESS_STATE("1");
						di.setPROCESS_STATE("1");
						DI.add(di);
						
						//入OTS表
						try {
							OTSDbHelper.getInstance().insert(tableName, row);
							infoLogger.info("Insert one line successfully!\n");
							diQueues.offer(di);
							successRowCount ++;
						}catch (Exception e) {
							e.printStackTrace();
							di.setPROCESS_STATE("0");
							diQueues.offer(di);
							infoLogger.error(row + "\n");
							infoLogger.error(e.getMessage() + "\n");
							if(e.getClass() == ClientException.class) {
								infoLogger.error("DataBase connection error!\n");
								return false;
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
		} catch (Exception e) {
			infoLogger.error("数据库异常: " + e.getMessage());
			return false;
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
				if (obj2 == null) {
					// 如果表达式获取字段值为空，直接返回默认值
					return entityBean.getDefaultValue();
				} else {
					// 如果不为空，获取其对应的值
					String columnValue = getColumnValue(obj2, ebean).toString();
					if (columnValue.equals(ebean.getDefaultValue())) {
						return columnValue;
					}
					if(ebean.getDatatype().equals("int")){
						int idx = columnValue.indexOf(".");
						if(idx >= 1)
							columnValue = columnValue.substring(0, idx);
					}
					if(expCol.toUpperCase().equals("V01301") && columnValue.trim().startsWith("999999")){
						try{
							Double area = (Double)((BufrBean) members.get("0-1-3-0")).getValue();
							Double subArea = (Double)((BufrBean) members.get("0-1-20-0")).getValue();
							Double buoy_platformID = (Double)((BufrBean) members.get("0-1-5-0")).getValue();
							columnValue = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
						}catch (Exception e) {
							infoLogger.error("WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
						}
					}
					
					String afterText = expression.substring(expression.indexOf(cExp));
					String cExpReg = StringUtil.string2Regex(cExp);
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
						try{
							sp[0] = String.format("%d", Integer.parseInt(sp[0]));
						}
						catch (Exception e1) {
							try{
								sp[0] = String.format("%.3f", Double.parseDouble(sp[0]));
							}
							catch (Exception e2) {
							}
						}
						expression = expression.replaceFirst(cExpReg, sp[0]);
					}

				}
			}

			// 替换所有默认表达式
			expression = StringUtil.formatExpressionContent(expression);
			// StringUtil.replaceNumberExp(expression, texts);
			// 替换${this}
			Matcher thisMatcher = Pattern.compile(THIS_VALUE_EXP).matcher(expression);
			while (thisMatcher.find()) {
				if (StringUtils.isBlank(value)) {
					return entityBean.getDefaultValue();
				}
				if(StringUtil.string2Regex(thisMatcher.group(0)) != null && value != null)
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



}