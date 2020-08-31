package cma.cimiss2.dpc.indb.radi.dc_bufr_radi_hour.service;

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
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrRadiHHDecoder;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
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

public class RadiServiceImpl {
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
		String fileName = "D:\\TEMP\\D.1.7.1\\9-10\\Z_RADI_I_59316_20190910091300_O_ARS_FTM_PQC.BIN";
		RadiServiceImpl radiServiceImpl = new RadiServiceImpl();
		Date recv = new Date();
		boolean decode = radiServiceImpl.decode(fileName, tableSection, tableList,10,recv);
		if(decode){
			System.out.println("入库成功！");
		}else
			System.err.println("入库失败！");
	}

	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables, Date recv) {
		try {
			BufrRadiHHDecoder bufrDecoder = new BufrRadiHHDecoder();
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
				ReportInfoToDb(bufrDecoder, tableSection, name,dataBytes, recv);
				return this.decode(bufrDecoder, name, tableSection, tables, dataBytes.length, recv);
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

	public boolean decode(String fileName, String tableSection, List<String> tables, int length, Date recv) {
		BufrRadiHHDecoder bufrDecoder = new BufrRadiHHDecoder();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
			byte[] dataBytes={0};
			ReportInfoToDb(bufrDecoder, tableSection, fileName,dataBytes, recv);
			//System.out.println("\n Read file : "+ fileName);
			return this.decode(bufrDecoder, fileName, tableSection, tables, length, recv);
					
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
	
	public boolean decode(BufrRadiHHDecoder bufrDecoder, String fileName, String tableSection, List<String> tables, int length, Date recv) {
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		File fileN=new File(fileName);
		String V_BBB="000";
		int  idx=fileName.lastIndexOf(".");
		String prename=fileName.substring(0, idx-1);
		String endstr =fileName.substring(idx-3,idx);
		if(prename.endsWith("CC")){
			V_BBB=endstr;
		}
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
			boolean dateTimeOK=true;
			for(Map<String, Object> members: bufrDecoder.getParseList()){
				dateTimeOK=true;
				for (String tableName : tables) {
					dateTimeOK=true;
					String d_record_id="999999";
					String d_datetime="999999";
					tableName = tableName.trim();
					XmlBean xmlBean = configMap.get(tableName);
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					// 用于存储入库的字段
					StringBuffer keyBuffer = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer = new StringBuffer();
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
							
							if(("V_BBB").equals(column)){
								value=V_BBB;
							}
							
							// chy 修改
							if(!column.equalsIgnoreCase("D_RECORD_ID")){
								keyBuffer.append(",`").append(column).append("`");
								valueBuffer.append(",'").append(value.toString().trim()).append("'");
							}
							
							if(column.equals("D_DATETIME")){
								int idx01 = value.toString().lastIndexOf(":");
								if(idx01 > 16){
									dateTimeOK = false;
									infoLogger.error("资料时间错误！  " + fileName);
									break;
								}
								else{
									di.setDATA_TIME(value.toString().substring(0, idx01));
									d_datetime=value.toString();
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
							if(column.equals("V01301"))
								di.setIIiii(value.toString());
							if(column.equals("V05001"))
								di.setLATITUDE(value.toString());
							if(column.equals("V06001"))
								di.setLONGTITUDE(value.toString());
							else if(column.equals("V07001"))
								di.setHEIGHT(value.toString());
							if(column.equals("D_DATA_ID"))
								di.setDATA_TYPE(value.toString());
							else if(column.equals("V_BBB"))
								di.setDATA_UPDATE_FLAG(value.toString());
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
								
								if(("V_BBB").equals(column)){
									value=V_BBB;
								}
								// chy  修改
								if(!column.equalsIgnoreCase("D_RECORD_ID")){
									keyBuffer.append(",`").append(column).append("`");
									valueBuffer.append(",'").append(value.toString()).append("'");
								}
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
								hhmm = "999999";
								
								// chy 修改
								if(!column.equalsIgnoreCase("D_RECORD_ID")){
									keyBuffer.append(",`").append(column).append("`");
									this.appendValue(valueBuffer, datatype, hhmm);
								}
							} else {
								if (hhmm.length() < 4){
									hhmm = "0" + hhmm;
								}
								// chy  修改
								if(!column.equalsIgnoreCase("D_RECORD_ID")){
									keyBuffer.append(",`").append(column).append("`");
									this.appendValue(valueBuffer, datatype, hhmm);
								}
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
								if(("V_BBB").equals(column)){
									value=V_BBB;
								}
								if(("D_RECORD_ID").equals(column)){
									d_record_id=value.toString();
								}
								if(("D_DATETIME").equals(column)){
									d_datetime=value.toString();
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
								// chy 修改
								if(!column.equalsIgnoreCase("D_RECORD_ID")){
									keyBuffer.append(",`").append(column).append("`");
									valueBuffer.append(",'").append(value.toString().trim()).append("'");
								}
								if(column.equals("D_DATETIME")){
									int idx01 = value.toString().lastIndexOf(":");
									di.setDATA_TIME(value.toString().substring(0, idx01));
								}
								if(column.equals("V01301"))
									di.setIIiii(value.toString());
								if(column.equals("V05001"))
									di.setLATITUDE(value.toString());
								if(column.equals("V06001"))
									di.setLONGTITUDE(value.toString());
								else if(column.equals("V07001"))
									di.setHEIGHT(value.toString());
								if(column.equals("D_DATA_ID"))
									di.setDATA_TYPE(value.toString());
								else if(column.equals("V_BBB"))
									di.setDATA_UPDATE_FLAG(value.toString());
								continue;
							}
		
							// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//								System.err.println(column);
							value = calExpressionValue(obj, members, entityMap, entityBean);
							if("V14032".equals(column)&&!(value.toString().startsWith("999999"))){
								value=Double.parseDouble(value.toString())/60;
							}	
							if(("V_BBB").equals(column)){
								value=V_BBB;
							}
							if(("D_RECORD_ID").equals(column)){
								d_record_id=value.toString();
							}
							if(("D_DATETIME").equals(column)){
								d_datetime=value.toString();
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
							// chy  修改
							if(!column.equalsIgnoreCase("D_RECORD_ID")){
								keyBuffer.append(",`").append(column).append("`");
								this.appendValue(valueBuffer, datatype, value.toString().trim());
							}
							if(column.equals("D_DATETIME")){
								int idx01 = value.toString().lastIndexOf(":");
								di.setDATA_TIME(value.toString().substring(0, idx01));
							}
							if(column.equals("V01301"))
								di.setIIiii(value.toString());
							if(column.equals("V05001"))
								di.setLATITUDE(value.toString());
							if(column.equals("V06001"))
								di.setLONGTITUDE(value.toString());
							else if(column.equals("V07001"))
								di.setHEIGHT(value.toString());
							if(column.equals("D_DATA_ID"))
								di.setDATA_TYPE(value.toString());
							else if(column.equals("V_BBB"))
								di.setDATA_UPDATE_FLAG(value.toString());
							//infoLogger.info("\n connection: fileName  " + fxy  + "-------" + value);
						}
						
					}// end while
					if(dateTimeOK == false)
						continue;
					di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setFILE_SIZE(String.valueOf(length));
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
					
					String selectSQL = "Select V_BBB from " + tableName + " where  D_datetime=" + "'" +d_datetime+ "'"+" and  D_Record_id = " + "'" + d_record_id +  "'" +  " limit 1";
					ResultSet selectRst = statement.executeQuery(selectSQL);
					String BBB = null;
					if(selectRst.next()){
						BBB = selectRst.getString(1);
					}
					if(BBB!=null&&V_BBB.startsWith("CC")&&V_BBB.compareTo(BBB) > 0){
						String deleteSQL="delete from " + tableName + " where  D_datetime=" + "'" +d_datetime+ "'"+" and  D_Record_id = " + "'" + d_record_id +  "'" ;
						try{
							int ds1=statement.executeUpdate(deleteSQL);
							connection.commit();
							infoLogger.info(tableName+"表成功删除"+ds1+"条待更正数据: " + deleteSQL + "\n" );
							
						}catch (Exception e) {
//							e.printStackTrace();
							infoLogger.error("Delete record failed: " + deleteSQL + "\n" + e.getMessage());
						}
					
					}
					if(selectRst != null){
						try{
							selectRst.close();
							selectRst = null;
						}catch (Exception e) {
//							e.printStackTrace();
							infoLogger.error("Close ResultSet error! ");
						}
					}
					try {
						int executeUpdate = statement.executeUpdate(sql);
						connection.commit();
						if (executeUpdate > 0) {
							infoLogger.info(tableName + " Insert a record successfully！ " + fileName);
							Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
							succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
							tmpRecord.put(SUCCEEDED_KEY, succeededNum);
						} else {
							infoLogger.info("插入失败:\n  " + sql);
							di.setPROCESS_STATE("0");
							Integer otherExceptionNum = tmpRecord.get(OTHER_EXCEPTION_KEY);
							otherExceptionNum = (otherExceptionNum == null ? 1 : (otherExceptionNum +=1));
							tmpRecord.put(OTHER_EXCEPTION_KEY, otherExceptionNum);
						}
					}catch (Exception e) {
						infoLogger.error("插入失败:\n " + sql + e.getMessage() + " " + fileName);
						di.setPROCESS_STATE("0");
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
	/**
	 * 入服务库报告表
	 * @param bufrDecoder
	 * @param tableSection
	 * @param fileName
	 */
	 public  void ReportInfoToDb(BufrRadiHHDecoder bufrDecoder,String tableSection, String fileName,byte[] dataBytes,Date recv) {
		 	int successOrNot=bufrDecoder.successOrNot;
			DruidPooledConnection report_connection = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//			File file = new File(fileName);
//			String filename = file.getName();
//			Date recv = new Date(file.lastModified());
			String ReportTableName=StartConfig.reportTable();
//			String ReportTableName="RADI_WEA_GLB_BULL_BUFR_TAB";
//			String sod_code="D.0001.0007.S001";
			// cuihongyuan 2019-11-21
			String sod_code = "D.0001.0003.S002";
			// chy 去掉 d_record_id
			String sql="insert into "+ReportTableName+"(D_DATA_ID,D_SOURCE_id,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
					+ "V01301,V_BBB,V_CCCC,V_YYGGgg,V_TT,V_LEN,V_FILE_SIZE,V_BULLETIN_B,V_FILE_NAME,V_NUM,V01301_list,d_datetime_list,"
					+ "V_decode_flag) values ("
					+ "?,?,?,?,"
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
			int length=0;
			if(StartConfig.getDataPattern()==0){//二进制流格式
				length=dataBytes.length;
			}else if(StartConfig.getDataPattern()==1){//文件格式
				File file=new File(fileName);
				length=(int)file.length();
			}
			
			PreparedStatement statement = null;
			try {
				report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
				statement = new LoggableStatement(report_connection, sql);
				report_connection.setAutoCommit(false);
				
				StatDi di = new StatDi();
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setTT("");
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(StartConfig.ctsCode());
				di.setFILE_NAME_N(fileName);
				di.setFILE_NAME_O(fileName);
				if(successOrNot==1){//解码成功
					if(bufrDecoder.getParseList().size()>0){//解码有值
						for(Map<String, Object> members: bufrDecoder.getParseList()){
							//查站号
							String stafxy="0-1-1-0";
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
						int ii = 1;
//						statement.setString(ii++, station_1 + "_" + sdf.format(datetime1));// d_record_id
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
						statement.setLong(ii++, length);//V_LEN    BUFR报文长度 ?????????????
						statement.setLong(ii++, length);//V_file_size 文件大小
						statement.setBytes(ii++, dataBytes);//V_BULLETIN_B 公报内容
//							statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
						statement.setString(ii++,fileName);//V_FILE_NAME 文件名
						statement.setInt(ii++, bufrDecoder.getParseList().size());//V_NUM 观测记录数
						statement.setString(ii++,stations);//V01301_list 观测记录站号列表
						statement.setString(ii++,datetimes);//d_datetime_list 观测记录时间列表
						statement.setInt(ii++,1);//V_decode_flag 解码是否成功标志
						
						di.setDATA_TIME(TimeUtil.date2String(datetime1, "yyyy-MM-dd HH:mm"));
						di.setIIiii(station_1);
						di.setLATITUDE("");
						di.setLONGTITUDE("");
						di.setHEIGHT("");
						di.setDATA_UPDATE_FLAG(V_BBB);
						
					}else{
						
					}
				}else if(successOrNot==2){//解码失败
					V_BBB=bufrDecoder.V_BBB;
					V_CCCC=bufrDecoder.V_CCCC;
					V_TT=bufrDecoder.V_TTAAii;
					V_YYGGgg=bufrDecoder.V_YYGGgg;
//					byte[] databyte=getContent(fileName);//报文二进制数组
					int ii = 1;
//					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
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
					
					di.setDATA_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm"));
					di.setIIiii("999999");
					di.setLATITUDE("");
					di.setLONGTITUDE("");
					di.setHEIGHT("");
					di.setDATA_UPDATE_FLAG(V_BBB);
				}else {//未做解码， 即文件不存在
					byte[] databyte=new byte[0];
					int ii = 1;
//					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
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
					statement.setString(ii++,fileName);//V_FILE_NAME 文件名
					statement.setInt(ii++, 0);//V_NUM 观测记录数
					statement.setString(ii++,null);//V01301_list 观测记录站号列表
					statement.setString(ii++,null);//d_datetime_list 观测记录时间列表
					statement.setInt(ii++,0);//V_decode_flag 解码是否成功标志
					
					di.setDATA_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm"));
					di.setIIiii("999999");
					di.setLATITUDE("");
					di.setLONGTITUDE("");
					di.setHEIGHT("");
					di.setDATA_UPDATE_FLAG(V_BBB);
				}
				
				di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setFILE_SIZE(String.valueOf(length));
				di.setBUSINESS_STATE("1");
				di.setPROCESS_STATE("1");
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				DI.add(di);
				try{
					statement.execute();
					report_connection.commit();
					infoLogger.info("ReportInfoToDb 数据入报告表"+StartConfig.reportTable()+"成功！:"+fileName);
				}catch (Exception e) {
					di.setPROCESS_STATE("0");
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
