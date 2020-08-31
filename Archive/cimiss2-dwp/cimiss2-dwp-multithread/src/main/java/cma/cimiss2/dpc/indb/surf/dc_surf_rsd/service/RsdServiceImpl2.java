package cma.cimiss2.dpc.indb.surf.dc_surf_rsd.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrDecoder;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class RsdServiceImpl2 implements BufrService {
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
		RsdServiceImpl2.diQueues = diQueues;
	}
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrDecoder bufrDecoder = new BufrDecoder();
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
				ReportInfoToDb(bufrDecoder,name);
				return this.decode(bufrDecoder, name, tableSection, tables);
			} else {
				infoLogger.error("\n Read file error: "+ name);
				String event_type = EIEventType.OP_FILE_ERROR.getCode();
				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
				if(ei == null) {
					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
				}else {
					if(StartConfig.isSendEi()) {
						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
						ei.setKObject("cma.cimiss2.dpc.indb.surf.rsd.service.RsdServiceImpl");
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
	/**
	 * 中国地面雨滴谱观测数据处理
	 */
	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
		BufrDecoder bufrDecoder = new BufrDecoder();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
			ReportInfoToDb(bufrDecoder,fileName);
			return this.decode(bufrDecoder, fileName, tableSection, tables);
		} else {			
			infoLogger.error("\n Read file error: "+ fileName);
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.surf.rsd.service.RsdServiceImpl");
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

	@SuppressWarnings("unchecked")
	public boolean decode(BufrDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		try {
			
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			statement = connection.createStatement();
			if(connection.getAutoCommit() == false)
				connection.setAutoCommit(true);
//			statement.execute("select last_txc_xid()");
			boolean dateTimeOK = true;
			File file0 = new File(fileName);
			Date recv = new Date(file0.lastModified());
			for(Map<String, Object> members: bufrDecoder.getParseList()){
				dateTimeOK = true;
				int timeupsize=5;
				int shorttime=1;
				String groupdatetime ="";
				
				for (String tableName : tables) {
					dateTimeOK = true;
					tableName = tableName.trim();
					XmlBean xmlBean = configMap.get(tableName);
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
					if(helpMap != null)
						entityMap.putAll(helpMap);
					//**********************************
					// 用于存储入库的字段
					StringBuffer keyBuffer = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer = new StringBuffer();
					// 遍历配置信息
					Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
					//要素表
					Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
					if(groupMapList != null && groupMapList.size() > 0){//若groupMap有值
						/*********获取时间增量与短时间增量***********/
							Object timeValue=null;
							Map<String, EntityBean> groupMapTime = groupMapList.get("1-11-0-0_2");
							it = groupMapTime.entrySet().iterator();
							while(it.hasNext()){//遍历groupMap
								Entry<String, EntityBean> next = it.next();
								String columntime = next.getKey(); // 配置文件配置的字段名称
								EntityBean entityBean = next.getValue(); // 配置的每个字段属性
								String timefxy = entityBean.getFxy();
								Object obj = members.get(timefxy);
								if(obj!=null){
									timeValue = calExpressionValue(obj, members, entityMap, entityBean);
									double tmp=Double.parseDouble((timeValue.toString()));
									if("V04015".equals(columntime)){//时间增量
										timeupsize=-(int)tmp;
									}
									else if("V04065".equals(columntime)){
										shorttime=(int)tmp;
									}
								}
							}
						
						/***********************/
						Set<String> groupKey = groupMapList.keySet();
						Iterator<String> groupItor = null;
						for(groupItor = groupKey.iterator(); groupItor.hasNext(); ){//遍历每个groupMap的Key
							String key = groupItor.next();//获取每个groupMap的Key
							
							if(key.startsWith("1-11-0-0")){
								continue;	
							}
							int span = Integer.parseInt(key.split("_")[1]);//获取delay的值
							String repeated = key.split("_")[0];//获取groupMap的fxy值
							Map<String, EntityBean> groupMap = groupMapList.get(key);//获取每个groupMap中所有字段和对应的属性组成的map
							entityMap.putAll(groupMap); // 后面覆盖前面的，将entityMap与groupMap合并
							int repteats = 0;
							String prefix =  repeated.substring(0, repeated.lastIndexOf("-") + 1);
							if((List<BufrBean>)members.get(repeated) != null && ((List<BufrBean>)members.get(repeated)).size() > 0)
							 {
							}
//							repeated = prefix + String.valueOf(repteats);
							List<String> sqlList = new ArrayList<>();
							int count=0;
//							int subtime=timeupsize-shorttime;//每个group减去的时间量
							int subtime=0;
							while((List<BufrBean>)members.get(repeated) != null){
								List<BufrBean> list = (List<BufrBean>) members.get(repeated);
								int size = list.size() / span;//获取每个group中有多少组数据
								StatDi di = null;
								/**************每个group都获取d_datetime时间后减去短时间增量，再赋值给V04001-V04005*********************/
								Object objtime=members.get("D_DATETIME");
								EntityBean entityBeantime=entityMap.get("D_DATETIME");
								Object grouptimeValue = calExpressionValue(objtime, members, entityMap, entityBeantime);
								int idx0 = grouptimeValue.toString().lastIndexOf(":");
								if(idx0 > 16){
									dateTimeOK = false;
									infoLogger.error("资料时间错误！  " + fileName);
									repteats ++;
									repeated = prefix + String.valueOf(repteats);
//									subtime-=shorttime;//每循环一次group，datetime减去相应的“短时间增量”
									subtime +=shorttime;
									continue;
								}
								else{
									try{
										SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    Date dataTime=simpleDateFormat.parse(grouptimeValue.toString());
									    Calendar calendar = Calendar.getInstance();
					                    calendar.setTime(dataTime);
					                    calendar.add(Calendar.MINUTE, -subtime);
					                    dataTime = calendar.getTime();
					                    String datetimeafter=simpleDateFormat.format(dataTime);
					                    groupdatetime=datetimeafter;
//					                    di.setDATA_TIME(groupdatetime.toString().substring(0, idx0));
										if(!TimeCheckUtil.checkTime(dataTime)){
											dateTimeOK = false;
											infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
											repteats ++;
											repeated = prefix + String.valueOf(repteats);
//											subtime-=shorttime;//每循环一次group，datetime减去相应的“短时间增量”
											subtime +=shorttime;
											continue;
										}
									}catch (Exception e) {
										dateTimeOK = false;
										infoLogger.info("资料时间错误！  " + fileName);
										repteats ++;
										repeated = prefix + String.valueOf(repteats);
//										subtime-=shorttime;//每循环一次group，datetime减去相应的“短时间增量”
										subtime +=shorttime;
										continue;
									}
								} 
								/*****************************************************/
								boolean ifExistData=true;//标志当前group中是否有数据
								if(size==0){
//									di = new StatDi();
//									di.setPROCESS_START_TIME(TimeUtil.getSysTime());
//									di.setTT("BUFR");
//									di.setDATA_TYPE_1(StartConfig.ctsCode());
//									di.setFILE_NAME_N(fileName);
//									di.setFILE_NAME_O(fileName);
//									File file = new File(fileName);
//									di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm"));
//									di.setFILE_SIZE(String.valueOf((new File(fileName)).length()));
//									di.setBUSINESS_STATE("1");
//									di.setPROCESS_STATE("1");
//									di.setIIiii(((BufrBean)(members.get("0-1-2-0"))).getValue().toString());
//									di.setLATITUDE(((BufrBean)(members.get("0-5-1-0"))).getValue().toString());
//									di.setLONGTITUDE(((BufrBean)(members.get("0-6-1-0"))).getValue().toString());
//									di.setHEIGHT("999998");
//									di.setDATA_TYPE(StartConfig.sodCode());
//									di.setDATA_UPDATE_FLAG("000");
//									di.setDATA_TIME(groupdatetime.substring(0,groupdatetime.lastIndexOf(":")));
//									di.setSEND("BFDB");
//									di.setSEND_PHYS("DRDS");
//									di.setPROCESS_END_TIME(TimeUtil.getSysTime());
//									di.setRECORD_TIME(TimeUtil.getSysTime());
//									DI.add(di);
									size=1;
									ifExistData=false;
								}
								if(size>0){
									boolean isDI=false;
									for(int p = 0; p < size; p ++){
											if(isDI==false){
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
										
										keyBuffer = new StringBuffer();	// 用于存储入库的字段
										valueBuffer = new StringBuffer();// 用于存储入库的字段值
										it = entityMap.entrySet().iterator();
										while(it.hasNext()){//遍历entityMap
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
												}
												
												if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
													value = members.get("CCCC");
												if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
													value = members.get("TTAAii");
												if(column.equalsIgnoreCase("V_BBB"))
													value = members.get("BBB");
												if(column.equalsIgnoreCase("D_SOURCE_ID")){
													File file = new File(fileName);
													value =value+"_"+file.getName();
												}
												if("D_RYMDHM".equals(column)){
													SimpleDateFormat simpledf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													value=simpledf.format(recv.getTime());
												}
												if(column.equals("D_DATETIME")){
//													int idx01 = value.toString().lastIndexOf(":");
//													if(idx01 > 16){
//														dateTimeOK = false;
//														infoLogger.error("资料时间错误！  " + fileName);
//														break;
//													}
//													else{
////														di.setDATA_TIME(value.toString().substring(0, idx01));
//														try{
//															SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//														    Date dataTime=simpleDateFormat.parse(value.toString());
//														    Calendar calendar = Calendar.getInstance();
//										                    calendar.setTime(dataTime);
//										                    calendar.add(Calendar.MINUTE, -subtime);
//										                    dataTime = calendar.getTime();
//										                    String datetimeafter=simpleDateFormat.format(dataTime);
//										                    value=datetimeafter;
//										                    di.setDATA_TIME(value.toString().substring(0, idx01));
////															if(!TimeCheckUtil.checkTime(dataTime)){
////																dateTimeOK = false;
////																infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
////																break;
////															}
//														}catch (Exception e) {
//															dateTimeOK = false;
//															infoLogger.info("资料时间错误！  " + fileName);
//															break;
//														}
//													}
													int idx01 = groupdatetime.toString().lastIndexOf(":");
													if(idx01 > 16){
														dateTimeOK = false;
														infoLogger.error("资料时间错误！  " + fileName);
														break;
													}else{
														value=groupdatetime;
														if(isDI==false){
															di.setDATA_TIME(groupdatetime.toString().substring(0, idx01));
														}
													}
												}
												if("D_RECORD_ID".equals(column)){//转换d_record_id中的时间与雨滴粒子级别编号
													String recordvalue=value.toString();
													if(recordvalue.contains("_")){
														SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
														try {
															String recordtime=recordvalue.split("_")[1];//d_record_id中的时间
															String V13205=recordvalue.split("_")[2];//d_record_id中的雨滴粒子级别编号
															Object ObjV13205=members.get(repeated);
															EntityBean entityBeanV13205=entityMap.get("V13205");
															entityBeanV13205.setFxy(repeated);
															String expressionV13205 = entityBeanV13205.getExpression(); // 此字段的表达式
															String defaultV13205 = entityBeanV13205.getDefaultValue(); 
															Object V13205Value = calExpressionValue(ObjV13205, members, entityMap, entityBeanV13205);
															if (ObjV13205 == null) {
																// 如果表达式不为空说明此值需要根据表达式进行计算value
																if (StringUtils.isNotBlank(expressionV13205)) {
																	// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
																	Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expressionV13205);
																	if (em.find()) {
																		V13205Value = defaultV13205;
																	} else {// 如果表达式存在，且其中没有以index取值
																		V13205Value = calExpressionValue(ObjV13205, members, entityMap, entityBeanV13205);
																	}
																} else {
																	V13205Value = defaultV13205;
																}
															}else{
																V13205Value=calExpressionValue(ObjV13205, members, entityMap, entityBeanV13205);
															}
															int idxp = V13205Value.toString().indexOf(".");
															if(idxp >= 1)
																V13205Value = V13205Value.toString().substring(0, idxp);
															Date dataTime;
															dataTime = sdf.parse(recordtime);
															Calendar calendar = Calendar.getInstance();
										                    calendar.setTime(dataTime);
										                    calendar.add(Calendar.MINUTE, -subtime);
										                    dataTime = calendar.getTime();
										                    String datetimeafter=sdf.format(dataTime);
										                    String valuetemp=recordvalue.replace(recordtime, datetimeafter);
										                    StringBuffer sbr=new StringBuffer(valuetemp);
										                    sbr.replace(recordvalue.lastIndexOf(V13205),recordvalue.length(), V13205Value.toString());
										                    value=sbr.toString();
														} catch (ParseException e) {
															// TODO Auto-generated catch block
															e.printStackTrace();
														}
														    
													}else{
														infoLogger.info("资料d_record_id格式错误！  " + fileName);
														break;
													}
												}
												if("V04001".equals(column)){
													value=groupdatetime.split("-|\\s+|:")[0];
												}else if("V04002".equals(column)){
													value=groupdatetime.split("-|\\s+|:")[1];
												}else if("V04003".equals(column)){
													value=groupdatetime.split("-|\\s+|:")[2];
												}else if("V04004".equals(column)){
													value=groupdatetime.split("-|\\s+|:")[3];
												}else if("V04005".equals(column)){
													value=groupdatetime.split("-|\\s+|:")[4];
												}else if("V20302_060".equals(column)){
													if(ifExistData){
														value=1;
													}else{
														value=0;
													}
												}
												
												if(isDI==false){
												 if(column.equals("V01301"))
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
												
												keyBuffer.append(",`").append(column).append("`");
												valueBuffer.append(",'").append(value.toString().trim()).append("'");
												
												continue;
											}
											// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
											value = calExpressionValue(obj, members, entityMap, entityBean);
											
											if("V04001".equals(column)){
												value=groupdatetime.split("-|\\s+|:")[0];
											}else if("V04002".equals(column)){
												value=groupdatetime.split("-|\\s+|:")[1];
											}else if("V04003".equals(column)){
												value=groupdatetime.split("-|\\s+|:")[2];
											}else if("V04004".equals(column)){
												value=groupdatetime.split("-|\\s+|:")[3];
											}else if("V04005".equals(column)){
												value=groupdatetime.split("-|\\s+|:")[4];
											}
											
											keyBuffer.append(",`").append(column).append("`");
											this.appendValue(valueBuffer, datatype, value.toString().trim());
											
											if(isDI==false){
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
											}
											
										}
										if(dateTimeOK == false)
											continue;
										String sql = "insert into " + tableName //
												+ " (" //
												+ keyBuffer.toString().substring(1) //
												+ ") values ("//
												+ valueBuffer.toString().substring(1) //
												+ ")";
			//							System.out.println(sql);
										sqlList.add(sql);
										statement.addBatch(sql);
										count ++;
										
										if(ifExistData==true){//group下无数据的不remove
											for(int c = 0; c < span; c++){
												list.remove(0);
											}
										}
										if(isDI==false){
											di.setSEND("BFDB");
											di.setSEND_PHYS("DRDS");
											di.setPROCESS_END_TIME(TimeUtil.getSysTime());
											di.setRECORD_TIME(TimeUtil.getSysTime());
											DI.add(di);
											isDI=true;
										}
//										try{
//											statement.execute(sql);
//											
//										}catch (Exception e) {
//											di.setPROCESS_STATE("0");
//											infoLogger.error("插入失败：" +fileName+": "+ sql + "\n" + e.getMessage());
//										}
										if(count == 100 || p == size- 1){				
											try{
												if(connection.getAutoCommit() == true)
													connection.setAutoCommit(false);
												statement.executeBatch();
												connection.commit();
												infoLogger.info("批量插入成功，插入条数 =" + count + " " + fileName);
											}catch (Exception e) {
												statement.clearBatch();
									            connection.rollback();
												infoLogger.error("批量 插入失败：" + fileName + "\n" + e.getMessage());
												// 该表会出现入库冲突的情况，所以逐条插入
												for(int s = 0; s < sqlList.size(); s ++){
													try{
														statement.execute(sqlList.get(s));
														connection.commit();
														infoLogger.info("单条插入一条记录成功！");
													}catch (Exception e1) {
					//									e1.printStackTrace();
														infoLogger.error("单条 插入失败：" + sqlList.get(s) + "\n" + e1.getMessage());
													}
												}
					//							e.printStackTrace();
											}
											sqlList.clear();
											statement.clearBatch();
											count = 0;
										}
									}// end for
								}
								repteats ++;
								repeated = prefix + String.valueOf(repteats);
//								subtime-=shorttime;//每循环一次group，datetime减去相应的“短时间增量”
								subtime+=shorttime;
							} // end while

						}// end if
					}

				} // end tables loop
				if(DI != null && DI.size()>0){
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
//					if (columnValue.equals(ebean.getDefaultValue())) {
//						return columnValue;
//					}
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
				if(value != null)
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
	
	public  void ReportInfoToDb(BufrDecoder bufrDecoder, String fileName) {
	 	int successOrNot=bufrDecoder.successOrNot;
		DruidPooledConnection report_connection = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		File file = new File(fileName);
		String filename = file.getName();
		Date recv = new Date(file.lastModified());
		String ReportTableName=StartConfig.reportTable();//"SURF_WEA_CHN_BULL_BUFR_TAB";
//		String ReportTableName="SURF_WEA_GLB_BULL_BUFR_TAB";
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
				if(bufrDecoder.getParseList().size()>0){//解码有值
					for(Map<String, Object> members: bufrDecoder.getParseList()){
						String sta="999999";
						if(members.get("0-1-2-0")!=null){
						 sta=((BufrBean)(members.get("0-1-2-0"))).getValue().toString();
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
						 int k=0;
						 while((members.get("1-5-0-"+k))!=null){
							List<BufrBean> bufrList = (List<BufrBean>) members.get("1-5-0-"+k);
							int span=3;
							BigDecimal a=new BigDecimal(bufrList.size()/span).setScale(0,BigDecimal.ROUND_UP);
							int size=a.intValue();//每个group包含的记录数
							sizes+=size;
							k++;
						 }
						
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
						datetime.append(date_time[0]).append("-").append(date_time[1]).append("-").append(date_time[2]).append(" ").append(date_time[3]).append(":").append(date_time[4]).append(":").append("00");

						datetime_s.append(datetime).append(",");
						
					}//end for
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
					statement.setString(ii++, "A.0001.0025.S006");//D_DATA_ID
					statement.setString(ii++, "A.0001.0049.R001");//D_source_id
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
//					statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
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
				statement.setString(ii++, "A.0001.0025.S006");//D_DATA_ID
				statement.setString(ii++, "A.0001.0049.R001");//D_source_id
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
//				statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
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
				String name=filename.split("_")[3];
				String sta=name.length()==5?name:"999999";
				int ii = 1;
				statement.setString(ii++, sta + "_" + sdf.format(new Date()));// d_record_id
				statement.setString(ii++, "A.0001.0025.S006");//D_DATA_ID
				statement.setString(ii++, "A.0001.0049.R001");//D_source_id
				statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
				statement.setTimestamp(ii++, new Timestamp(recv.getTime()));// D_RYMDHM
				statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
				statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_DATETIME
				
				statement.setString(ii++, sta);//V01301
				statement.setString(ii++, V_BBB);//V_BBB
				statement.setString(ii++, V_CCCC);//V_CCCC
				statement.setString(ii++, V_YYGGgg);//V_YYGGgg
				statement.setString(ii++, V_TT);//V_TT
				statement.setLong(ii++, 0);//V_LEN    BUFR报文长度 ?????????????
				statement.setLong(ii++, 0);//V_file_size 文件大小
				statement.setBytes(ii++, databyte);//V_BULLETIN_B 公报内容
//				statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
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
	public static void main(String[] args) {
		RsdServiceImpl2 rsdserviceImpl=new RsdServiceImpl2();
//		String fileName="D:\\TEMP\\A.1.49.1\\7-17\\Z_SURF_I_58846_20190717110000_O_AWS-RSD-MM_FTM.BIN";//无有效数据
//		String fileName="D:\\TEMP\\A.1.49.1\\6-6\\Z_SURF_I_58251_20190606043500_O_AWS-RSD-MM_FTM.BIN";//正常
//		String fileName="D:\\TEMP\\A.1.49.1\\7-18\\Z_SURF_I_56202_20190630230500_O_AWS-RSD-MM_FTM.BIN";//解码错
//		String fileName="D:\\TEMP\\A.1.49.1\\7-18\\Z_SURF_I_00000_20190630230500_O_AWS-RSD-MM_FTM.BIN";//文件不存在
//		String fileName="D:\\TEMP\\A.1.49.1\\testData\\Z_SURF_I_56836_20190723071500_O_AWS-RSD-MM_FTM.BIN";//正常
//		String fileName="D:\\TEMP\\A.1.49.1\\10-12\\Z_SURF_I_57957_20190606082000_O_AWS-RSD-MM_FTM.BIN";
//		String fileName="D:\\TEMP\\A.1.49.1\\test\\xiaoweiqing_new\\Z_SURF_I_53599_20190714160000_O_AWS-RSD-MM_FTM.BIN";
//		String fileName="D:\\TEMP\\A.1.49.1\\test\\Z_SURF_I_50353_20191001010000_O_AWS-RSD-MM_FTM.BIN";
		String fileName="D:\\TEMP\\A.1.49.1\\10-21\\Z_SURF_I_57348_20180110000000_O_AWS-RSD-MM_FTM.BIN";
		
		
		String tableSection="A.0001.0049.R001";
		List<String> tableList = new ArrayList<String>();
		 tableList.add("surf_wea_chn_rsd_tab");
		Boolean decode = rsdserviceImpl.decode(fileName, tableSection, tableList);
	}


}
