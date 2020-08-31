package cma.cimiss2.dpc.indb.radi.dc_bufr_radi_hour.service;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alicloud.openservices.tablestore.ClientException;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrRadiHHDecoder;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;




/**  
 * *********************************************************************** 
 * @ClassName:  RadiBufrServiceImpl   
 * @Description:TODO(这里用一句话描述这个类的作用)   
 * @author: 唐怀瓯
 * @date:   2018年11月08日 下午1:44:19   
 *     
 * @Copyright: 2018  Inc. All rights reserved. 
 * 注意：本内容仅限于内部传阅，禁止外泄以及用于其他的商业目 
 * ***********************************************************************
 */

public class OTSRadiServiceImpl implements BufrService {
	int batchSize = 500;
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> DI = new ArrayList<StatDi>();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		RadiServiceImpl.diQueues = diQueues;
	}

	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();  
		String tableSection = "D.0001.0007.R001";
		tableList.add("RADI_MUL_CBF_HOR_TAB");
		String fileName = "D:\\TEMP\\Z_RADI_I_57290_20190312002500_O_ARS_FTM_PQC.BIN";
		RadiServiceImpl radiServiceImpl = new RadiServiceImpl();
//		boolean decode = radiServiceImpl.decode(fileName, tableSection, tableList);
//		if(decode){
//			System.out.println("入库成功！");
//		}else
//			System.err.println("入库失败！");
	}

	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrRadiHHDecoder bufrDecoder = new BufrRadiHHDecoder();
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
				return this.decode(bufrDecoder, name, tableSection, tables);
			} else {
				infoLogger.info("\n Read file error: "+ name);
				String event_type = EIEventType.OP_FILE_ERROR.getCode();
				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
				if(ei == null) {
					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
				}else {
					if(StartConfig.isSendEi()) {
						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
						ei.setKObject("cma.cimiss2.dpc.indb.radi.bufr.hour.service.RadiServiceImpl");
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
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
		BufrRadiHHDecoder bufrDecoder = new BufrRadiHHDecoder();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
			//System.out.println("\n Read file : "+ fileName);
			return this.decode(bufrDecoder, fileName, tableSection, tables);
					
		} else {
			infoLogger.info("\n Read file error: "+ fileName);
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.radi.bufr.hour.service.RadiServiceImpl");
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
	private final String OTHER_EXCEPTION_KEY = "OTHER_EXCEPTION";
	
	public boolean decode(BufrRadiHHDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		File fileN=new File(fileName);
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		try {
//			if(diQueues == null)
//				diQueues = new LinkedBlockingQueue<StatDi>();
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			statement = connection.createStatement();
			Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
			if(connection.getAutoCommit() == true)
				connection.setAutoCommit(false);
//			statement.execute("select last_txc_xid()");
			
			for(Map<String, Object> members: bufrDecoder.getParseList()){
				for (String tableName : tables) {
					tableName = tableName.trim();
					XmlBean xmlBean = configMap.get(tableName);
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					// 用于存储入库的字段
					StringBuffer keyBuffer = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer = new StringBuffer();
					
					Map<String, Object> row = new HashMap<>();
					
					// 遍历配置信息
					Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
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
						String defaultValue = entityBean.getDefaultValue(); // 缺省值
//						if("Q14210".equals(column)||"V14210".equals(column)){
//							System.out.println(11111);
//						}
//						if("V01101".equals(column)){
//							System.out.println(11111);
//						}
						if (fxy.equals("V14_052") ){
							// ${0-4-4-n}:${0-4-5-n}
							// 获取时间hhmm的表达式 // 获取表达式代表的配置
							fxy = expression.substring(0, expression.indexOf(":"));
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
							
							keyBuffer.append(",`").append(column).append("`");
							valueBuffer.append(",'").append(value.toString().trim()).append("'");
							row.put(column, value.toString().trim());
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
							continue;
						}
	
						// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//							System.err.println(column);
						value = calExpressionValue(obj, members, entityMap, entityBean);
						//hh
						String hhmm = value.toString();
						hhmm = hhmm.substring(0,hhmm.indexOf("."));			
						//mm fxy
						fxy = expression.substring(expression.indexOf(":")+1);
						obj = members.get(fxy); // 根据fxy获取每一行数据
						// 如果获取的对象为null
							 value = null;			
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
								keyBuffer.append(",`").append(column).append("`");
								valueBuffer.append(",'").append(value.toString()).append("'");
								row.put(column, value.toString().trim());
								continue;
							}
							value = calExpressionValue(obj, members, entityMap, entityBean);
							//value = calExpressionValue(obj, members, entityMap, entityBean);
							
							String mmhh=value.toString().substring(0,value.toString().indexOf("."));							
							if (mmhh.length() < 2){
								hhmm =  hhmm + "0" + mmhh;
							} else {
								hhmm =  hhmm +  mmhh;
							}
							if (hhmm.indexOf("9999")!= -1){
								keyBuffer.append(",`").append(column).append("`");
								hhmm = "999999";
								this.appendValue(valueBuffer, datatype, hhmm);
								row.put(column, hhmm.toString().trim());
							} else {
								if (hhmm.length() < 4){
									hhmm = "0" + hhmm;
								}
								keyBuffer.append(",`").append(column).append("`");
								this.appendValue(valueBuffer, datatype, hhmm);
								row.put(column, hhmm.toString().trim());
							}
						//	infoLogger.info("\n Read file error:   "+ column + "  " + fxy + " ------ "  + value);
						}else{
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
								// 水温可能从两个fxy取值
								if(column.toUpperCase().equals("V22049") && value.toString().trim().startsWith("999999")){//
									obj = members.get("0-22-43-0");
									if(obj != null)
										value = calExpressionValue(obj, members, entityMap, entityBean);
								}
								if("V01101".equals(column)&&value.toString().startsWith("99999")){//若国家和地区标识在lua的+11中没有取到值，则取+01的值
									entityBean.setExpression("${stationInfo(${col:V01301},01,999999)}.split(',')[4]");
									value = calExpressionValue(obj, members, entityMap, entityBean);
								}
								keyBuffer.append(",`").append(column).append("`");
								valueBuffer.append(",'").append(value.toString().trim()).append("'");
								row.put(column, value.toString().trim());
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
								continue;
							}
		
							// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//								System.err.println(column);
							value = calExpressionValue(obj, members, entityMap, entityBean);
							if("V14032".equals(column)&&!(value.toString().startsWith("999999"))){
								value=Double.parseDouble(value.toString())/60;
							}							
							keyBuffer.append(",`").append(column).append("`");
							this.appendValue(valueBuffer, datatype, value.toString().trim());
							row.put(column, value.toString().trim());
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
						}
						
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
					// 键表入库
//					String sql = "insert into " + tableName //
//							+ " (" //
//							+ keyBuffer.toString().substring(1) //
//							+ ") values ("//
//							+ valueBuffer.toString().substring(1) //
//							+ ")";
					try {
//						int executeUpdate = statement.executeUpdate(sql);
//						connection.commit();
//						if (executeUpdate > 0) {
//							infoLogger.info(tableName + " Insert a record successfully！ " + fileName);
//							Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
//							succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
//							tmpRecord.put(SUCCEEDED_KEY, succeededNum);
//						} else {
//							infoLogger.info("插入失败:\n  " + sql);
//							di.setPROCESS_STATE("0");
//							Integer otherExceptionNum = tmpRecord.get(OTHER_EXCEPTION_KEY);
//							otherExceptionNum = (otherExceptionNum == null ? 1 : (otherExceptionNum +=1));
//							tmpRecord.put(OTHER_EXCEPTION_KEY, otherExceptionNum);
//						}
						OTSDbHelper.getInstance().insert(tableName, row, true);
						infoLogger.info(tableName + ": Insert one line successfully!\n");
						diQueues.offer(di);
					}catch (Exception e) {
//						infoLogger.error("插入失败:\n " + sql + e.getMessage() + " " + fileName);
						di.setPROCESS_STATE("0");
						diQueues.offer(di);
						infoLogger.error(fileName + ": " + e.getMessage() + "\n");
						infoLogger.error(row + "\n");
						if(e.getClass() == ClientException.class) {
							infoLogger.error("DataBase connection error!\n");
						}
						
					}
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
				if(statement!=null){
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if(connection!=null)
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean) {
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
						expression = expression.replaceFirst(cExpReg, columnValue);
					}

				}
			}
			
		

			// 替换所有默认表达式
			expression = StringUtil.formatExpressionContent(expression);
			// 替换${this}
			Matcher thisMatcher = Pattern.compile(THIS_VALUE_EXP).matcher(expression);
			while (thisMatcher.find()) {
				if (StringUtils.isBlank(value)) {
					return entityBean.getDefaultValue();
				}
				if(value!=null && StringUtil.string2Regex(thisMatcher.group(0))!=null){
					expression = expression.replaceFirst(StringUtil.string2Regex(thisMatcher.group(0)), value); // 如果value为null怎么办？
				}
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
				value = bufr.getQcValues().get(0);
				int parseValue = Integer.parseInt(value.toString());
				value = parseValue > 9 ? 9 : parseValue;
			} else {
				value = bufr.getValue();
			}
		} else { // 不是bufrBean对象，那就是存储对象的list集合
			@SuppressWarnings("unchecked")
			List<BufrBean> bufrList = (List<BufrBean>) obj;

			String expression = entityBean.getExpression();
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
							Object value2 = bufrBean.getQcValues().get(0);
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



}
