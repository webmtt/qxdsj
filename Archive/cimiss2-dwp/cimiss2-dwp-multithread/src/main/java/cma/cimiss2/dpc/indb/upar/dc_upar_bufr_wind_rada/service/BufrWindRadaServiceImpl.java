package cma.cimiss2.dpc.indb.upar.dc_upar_bufr_wind_rada.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrUparWindDecoder2;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class BufrWindRadaServiceImpl implements BufrService {
	int batchSize = 500;
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> DI = new ArrayList<StatDi>();
//	public BufrUparWindDecoder2 decoder = new BufrUparWindDecoder2();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		BufrWindRadaServiceImpl.diQueues = diQueues;
	}
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
//		try {
//			BufrUparWindDecoder2 bufrDecoder = new BufrUparWindDecoder2();
//			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
//			if (isRead) {
//				return this.decode(bufrDecoder, name, tableSection, tables);
//			} else {
//				infoLogger.error("\n Read file error: "+ name);
//				String event_type = EIEventType.OP_FILE_ERROR.getCode();
//				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
//				if(ei == null) {
//					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
//				}else {
//					if(StartConfig.isSendEi()) {
//						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
//						ei.setKObject("cma.cimiss2.dpc.indb.upar.dc_upar_bufr_wind_rada.service.BufrWindRadaServiceImpl");
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
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
		try{
			BufrUparWindDecoder2 decoder = new BufrUparWindDecoder2();
			decoder.decodeFile(name, dataBytes);
			ReportInfoToDb(decoder, tableSection, name);
			return this.decode(decoder, name, tableSection, tables);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
			infoLogger.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 风廓线BUFR
	 */
	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
//		BufrUparWindDecoder2 bufrDecoder = new BufrUparWindDecoder2();
//		decoder.decodeFile(filename);
//		boolean isRead = bufrDecoder.bufrDecoder(fileName);
//		if (isRead) {
//			return this.decode(bufrDecoder, fileName, tableSection, tables);
//		} else {			
//			infoLogger.error("\n Read file error: "+ fileName);
//			String event_type = EIEventType.OP_FILE_ERROR.getCode();
//			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
//			if(ei == null) {
//				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
//			}else {
//				if(StartConfig.isSendEi()) {
//					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
//					ei.setKObject("cma.cimiss2.dpc.indb.upar.dc_upar_bufr_wind_rada.service.BufrWindRadaServiceImpl");
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
//			return false;
//		}
		try{
			BufrUparWindDecoder2 decoder = new BufrUparWindDecoder2();
			decoder.decodeFile(fileName);
			ReportInfoToDb(decoder, tableSection, fileName);
			File bufrFile = new File(fileName);
			// 判断文件是否存在
			if (!bufrFile.exists() ||!bufrFile.isFile()) {
				infoLogger.info("文件不存在："+fileName);
				return true;
			}else {	
				if(bufrFile.length() <= 0){
					infoLogger.info("空文件："+fileName);
					return true;
				}else{
					return this.decode(decoder, fileName, tableSection, tables);
				}
			}
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
			infoLogger.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean decode(BufrUparWindDecoder2 bufrDecoder, String fileName, String tableSection, List<String> tables) {
		File file = new File(fileName);
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		String enter=bufrDecoder.entrance+":"+bufrDecoder.local_version+"_"+bufrDecoder.master_table+"_"+bufrDecoder.subcenter_id+"_"+bufrDecoder.center_id;
		String xmlKey1="3-1-1:5_13_255_255,3-1-1:0_11_0_85,3-1-1:4_11_0_218,3-1-1:6_13_255_255,3-1-1:0_12_0_86,3-1-1:0_14_0_53,3-1-1:0_17_0_74,3-1-1:0_13_0_88,3-1-1:0_11_215_215,3-1-1:4_11_255_255,3-1-1:0_14_0_80,3-1-1:0_14_0_89,3-1-1:4_11_255_-9999,3-1-1:6_11_255_255,0-1-18:0_14_10_78,3-1-1:0_11_0_69";
		String xmlKey2="3-1-32:0_9_0_1,3-1-32:0_14_0_74,3-1-32:3_9_0_85,3-1-32:3_9_0_215,3-1-32:0_9_0_59,3-1-32:0_9_0_89,3-1-32:0_9_0_78";
		String xmlKey3="3-1-32:0_9_3_74,3-1-32:0_9_0_74";
		String xmlKey4="3-9-51:0_21_0_110";
		String xmlKey5="3-1-32:0_17_0_74";
		
		if(xmlKey1.contains(enter)){
			tableSection+="_V1";
		}else if(xmlKey2.contains(enter)){
			tableSection+="_V2";
		}else if(xmlKey3.contains(enter)){
			tableSection+="_V3";
		}else if(xmlKey4.contains(enter)){
			tableSection+="_V4";
		}else if(xmlKey5.contains(enter)){
			tableSection+="_V5";
		}else {
			infoLogger.error("Table XML file error!");
			return true;
		}
		
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		tables = BufrConfig.getTableMap().get(tableSection);
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			statement = connection.createStatement();
			List<String> sqlList = new ArrayList<>();
			if(connection.getAutoCommit() == false)
				connection.setAutoCommit(true);
//			statement.execute("select last_txc_xid()");
			boolean dateTimeOK=true;
			for(Map<String, Object> members: bufrDecoder.parseList){
				dateTimeOK=true;
				for (String tableName : tables) {
					dateTimeOK=true;
					tableName = tableName.trim();
					XmlBean xmlBean = configMap.get(tableName);
					
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					StatDi di = null;
					di = new StatDi();
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setTT("BUFR");
					di.setDATA_TYPE_1(StartConfig.ctsCode());
					di.setDATA_TYPE(StartConfig.sodCode());
					di.setFILE_NAME_N(fileName);
					di.setFILE_NAME_O(fileName);
					di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm"));
					di.setFILE_SIZE(String.valueOf((new File(fileName)).length()));
					di.setBUSINESS_STATE("1");//1成功，0失败
					di.setPROCESS_STATE("1");//1成功，0失败

					//**********************************
					// 用于存储入库的字段
					StringBuffer keyBuffer0 = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer0 = new StringBuffer();
					//用于存D_RECORD_ID的值
					String recordvalue="";
					//用于存风廓线站号
					String wstation="";
					//用于存测站高度
					double stationHeight=999999;
					//用于存纬度
					double Latitude=999999;
					//用于存经度
					double Longitude=999999;
					//用于存D_DATETIME
					String datetime="";
					
					// 遍历entityMap的配置信息
					for(String key : entityMap.keySet()){
						EntityBean entityBean = entityMap.get(key);// 配置的每个字段属性
						String fxy = entityBean.getFxy();
						String datatype = entityBean.getDatatype();// 字段类型
						String column = entityBean.getColumn();// 配置文件配置的字段名称
						String expression = entityBean.getExpression(); // 此字段的表达式
						String defaultValue = entityBean.getDefaultValue(); // 缺省值
						Object obj = members.get(fxy);
						Object value = null;
						if("D_RECORD_ID".equals(column)){
							System.out.println();
						}
						if("V02121".equals(column)){
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
									value = calExpressionValue(obj, members, entityMap, entityBean);
								}
							} else {
								value = entityBean.getDefaultValue();
							}
							
							if(column.equalsIgnoreCase("D_SOURCE_ID"))
								value =value+"_"+file.getName();
							if(column.equalsIgnoreCase("D_RYMDHM"))
								value =TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
							if(column.equalsIgnoreCase("D_DATETIME")){
								int idx0 = value.toString().lastIndexOf(":");
								if(idx0 != 16){
									infoLogger.error("资料时间错误！  " + fileName);
								    break;
								}else{
									datetime= value.toString();
									int idx01 = datetime.lastIndexOf(":");
									di.setDATA_TIME(datetime.substring(0, idx01));
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
							if(column.equals("V_CCCC"))
								value=members.get("CCCC");
							if(column.equals("V_TT")){
								value=members.get("TTAAii");
								di.setTT(value.toString());
							}
							
							if(datatype.equalsIgnoreCase("double")){
								BigDecimal b = new BigDecimal(value.toString());  
								double val =b.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
								value=val;
							}
							
							if("D_RECORD_ID".equals(column)){//D_RECORD_ID需要在group中的V07002赋值后拼接
								recordvalue=String.valueOf(value);
							}else{
								keyBuffer0.append(",`").append(column).append("`");
								valueBuffer0.append(",'").append(value.toString().trim()).append("'");
							}
								
							continue;
						}
						// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
						value = calExpressionValue(obj, members, entityMap, entityBean);
						
						if("V01301".equals(column)){//获取风廓线站号
							wstation=value.toString();
							di.setIIiii(wstation);
						}
						if("V07001".equals(column)){//获取测站高度
							stationHeight=Double.parseDouble(value.toString());
							di.setHEIGHT(String.valueOf(stationHeight));
						}
						if("V05001".equals(column)){//获取纬度
							Latitude=Double.parseDouble(value.toString());
							di.setLATITUDE(String.valueOf(Latitude));
						}
						if("V06001".equals(column)){//获取经度
							Longitude=Double.parseDouble(value.toString());
							di.setLONGTITUDE(String.valueOf(Longitude));
						}
						if(datatype.equalsIgnoreCase("double")){
							BigDecimal b = new BigDecimal(value.toString());  
							double val =b.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
							value=val;
						}
						
						keyBuffer0.append(",`").append(column).append("`");
						this.appendValue(valueBuffer0, datatype, value.toString().trim());
					}
					if(dateTimeOK == false)
						continue;
					//遍历GroupMap的配置信息
					Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
					for(String groupKey : groupMapList.keySet()){
						Map<String, EntityBean> group = groupMapList.get(groupKey);
						String groupFXY = groupKey.split("_")[0];
						List<Map<String, BufrBean>> BufrBeanList = (List<Map<String, BufrBean>>) members.get(groupFXY);
						if(BufrBeanList==null){
							continue;
						}
						
//						for (Map<String, BufrBean> map : BufrBeanList) {
						Map<String, BufrBean> map = null;
						int countfailed=0;
						for (int i = 0; i < BufrBeanList.size(); i ++) {//循环多少条数据
							map = BufrBeanList.get(i);
							StringBuffer keyBuffer = new StringBuffer();
							StringBuffer valBuffer = new StringBuffer();
							
							double V07002=0.0;//采样海拔高度=测站高度+采样高度
							for(String key : group.keySet()){
								EntityBean bean = group.get(key);
								String col = bean.getColumn();
								String fxy = bean.getFxy();
								String datatype = bean.getDatatype(); 
								String exp = bean.getExpression();
								Object val = null;
								if(col.equals("V07007")){
									System.out.println("");
								}
								if(bean.isQc()){//XML中 qc=true的
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null && bufrBean.getQcValues() != null){
										val = bufrBean.getQcValues().get(0);
									}
									else
										val = bean.getDefaultValue();
									
									keyBuffer.append(",`").append(col).append("`");
									valBuffer.append(",'").append(val.toString().trim()).append("'");
								}else{
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null){
										val = bufrBean.getValue();
										if(col.startsWith("Q")&&val.toString().startsWith("99999")){//XML中qc不等于true的质控码
											val = bean.getDefaultValue();
										}
									}
									else{
										val = bean.getDefaultValue();
									}
									if("V07007".equals(col)){//采样高度
										
										if(fxy.equals("0-7-9-0")){//取007009的需要将单位从gpm转换为m
											double g=9.8;
											double valH=Double.parseDouble(val.toString());
											val=9.8*valH/g;	
										}
										if(fxy.equals("0-10-7-0") && val.toString().equals("999999")){
											BufrBean bufrBeanH = map.get("0-7-7-0");
											if(bufrBeanH != null){
												val = bufrBeanH.getValue();
											}else{
												val=bean.getDefaultValue();
											}
										}
										recordvalue+="_"+val;//拼接到d_record_id中
										keyBuffer.append(",`D_RECORD_ID`");
										valBuffer.append(",'").append(recordvalue.toString().trim()).append("'");
										recordvalue=recordvalue.substring(0,recordvalue.lastIndexOf("_"));//恢复d_record_id 头
									}
									if("V07002".equals(col)){//采样海拔高度=测站高度+采样高度
										Object SampleHeight=null;//存采样高度的值
										EntityBean EntityBeanSampleHeight = group.get("V07007");
										String FxySampleHeight=EntityBeanSampleHeight.getFxy();
										BufrBean bufrBeanSampleHeight = map.get(FxySampleHeight);
										if(bufrBeanSampleHeight != null){
											SampleHeight = bufrBeanSampleHeight.getValue();
										}else{
											SampleHeight = EntityBeanSampleHeight.getDefaultValue();
										}
										double SamHeight=Double.parseDouble(SampleHeight.toString());
										if(SamHeight!=999999 && stationHeight!=999999){
											val=SamHeight+stationHeight;
										}
									}
									if(datatype.equalsIgnoreCase("double")){
										BigDecimal b = new BigDecimal(val.toString());  
										double val0 =b.setScale(6,BigDecimal.ROUND_HALF_UP).doubleValue();
										val=val0;
									}
//									if("Q11001".equals(col)){//水平风向质控码计算
//										Object valwd=null;//存水平风向的值
//										EntityBean EntityBeanWindDirection = group.get("V11001");
//										String FxyWindDirection=EntityBeanWindDirection.getFxy();
//										BufrBean bufrBeanWindDirection = map.get(FxyWindDirection);
//										if(bufrBeanWindDirection != null){
//											valwd = bufrBeanWindDirection.getValue();
//										}else{
//											valwd = EntityBeanWindDirection.getDefaultValue();
//										}
//										int windDQC = DQC(Double.parseDouble(valwd.toString()));//水平风向质控码
//										val=windDQC;
//									}
//									if("Q11002".equals(col)){//水平风速质控码计算
//										Object valsh=null;//存采样高度的值
//										Object valws=null;//存水平风速的值
//										
//										EntityBean EntityBeanSampleHeight = group.get("V07002");
//										String FxySampleHeight=EntityBeanSampleHeight.getFxy();
//										BufrBean bufrBeanSampleHeight = map.get(FxySampleHeight);//获取采样高度
//										if(bufrBeanSampleHeight != null){
//											valsh = bufrBeanSampleHeight.getValue();
//										}else{
//											valsh = EntityBeanSampleHeight.getDefaultValue();
//										}
//										
//										EntityBean EntityBeanHWindSpeed = group.get("V11002");
//										String FxyHWindSpeed=EntityBeanHWindSpeed.getFxy();
//										BufrBean bufrBeanHWindSpeed = map.get(FxyHWindSpeed);//获取水平风速
//										if(bufrBeanHWindSpeed != null){
//											valws = bufrBeanHWindSpeed.getValue();
//										}else{
//											valws = EntityBeanHWindSpeed.getDefaultValue();
//										}
//										double sampleHeight=Double.parseDouble(valsh.toString());//采样高度
//										double HWindSpeed=Double.parseDouble(valws.toString());//水平风速
//										
////										RapidQualityControl rapidQualityControl=new RapidQualityControl( wstation,  stationHeight, sampleHeight, HWindSpeed);
//										int WindSpeedQ=RapidQualityControl.GetHWindSpeed(wstation,stationHeight,sampleHeight,HWindSpeed);
//										val=WindSpeedQ;
//									}

									
									keyBuffer.append(",`").append(col).append("`");
									valBuffer.append(",'").append(val.toString().trim()).append("'");
								}
							}
							keyBuffer.append(keyBuffer0.toString());
							valBuffer.append(valueBuffer0.toString().trim());
							
							

							String sql = "insert into " + tableName //
									+ " (" //
									+ keyBuffer.toString().substring(1) //
									+ ") values ("//
									+ valBuffer.toString().substring(1) //
									+ ")";
							
							try{
								statement.execute(sql);
								infoLogger.info("插入"+tableName+"表成功："+fileName+": "+ sql);
							}catch (Exception e) {
								countfailed+=1;
								infoLogger.error("插入"+tableName+"表失败：" +fileName+": "+ sql + "\n" + e.getMessage());
							}finally {
								if(countfailed==BufrBeanList.size()){
									di.setPROCESS_STATE("0");
								}
							}
						}//结束循环入一个groupsize条数据
					} // 结束循环groupMapList
					
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setDATA_UPDATE_FLAG("000");
					di.setSEND_PHYS("DRDS");
					DI.add(di);
				} // 结束table循环 
				
				if(DI != null && DI.size()>0){
//					for (int j = 0; j < DI.size(); j++) {
						diQueues.offer(DI.get(0));
//					}
					DI.clear();
				}
			}//结束ParseList循环
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			infoLogger.error("数据库异常: " + e.getMessage());
			return false;
		}catch (Exception e) {
			e.printStackTrace();
			infoLogger.error(e.getMessage());
			return false;
		}
		finally {
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
		
		String expression = entityBean.getExpression();
		if("D_RECORD_ID".equals(entityBean.getColumn())){
			expression=expression.substring(0, expression.lastIndexOf("_"));
		}
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
						try{
							if(!"string".equals(ebean.getDatatype()))
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
						if(!bufr.getValue().toString().equals("NaN")){
							value = bufr.getValue();
						
							if(entityBean.getDatatype().equalsIgnoreCase("double")){//double类型的字段结果可能是科学计数法，要将其变为普通的数字
								BigDecimal big = new BigDecimal(value.toString());
								bufr.setValue(big.toPlainString());
								value = bufr.getValue();
							}
						}
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
	
	 /**
     * 风向质控
     * 允许值范围 0 ~ 360
     * 单位：度
     * @param a
     * @return
     */
    private static int DQC(double a) {
        if (999999 == (int) a) {
            return 8; // 数据缺测
        }
        if (a >= 0 && a <= 360) {
            return 0;
        }
        return 2;
    }
    
    public  void ReportInfoToDb(BufrUparWindDecoder2 bufrDecoder,String tableSection, String fileName) {
	 	int successOrNot=bufrDecoder.successOrNot;
		DruidPooledConnection report_connection = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		File file = new File(fileName);
		String filename = file.getName();
		Date recv = new Date(file.lastModified());
		String ReportTableName=StartConfig.reportTable();
//		String ReportTableName="SURF_WEA_GLB_BULL_BUFR_TAB";
		String sod_code="B.0015.0001.S001";
		
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
						 
						 //查总共多少条记录
						 String [] groupFXY={"1-8-0-0","1-6-0-0","1-3-0-0","1-4-0-0","1-5-0-0","1-11-0-0","1-1-0-0"};
//						 String groupFXY="";
						 for(int i=0;i<groupFXY.length;i++){
							  List<Map<String, BufrBean>> BufrBeanList = (List<Map<String, BufrBean>>) members.get(groupFXY[i]);
							  if(BufrBeanList!=null){
								  sizes+=BufrBeanList.size();
							  }
						 }
						 
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
//						statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
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
    
	public static void main(String[] args) {
		BufrWindRadaServiceImpl bufrWindRadaServiceImpl=new BufrWindRadaServiceImpl();
//		String fileName="D:\\TEMP\\fengkuoxianBufrData\\testdatanew2\\20190528\\A_IUPA54LFPW280000_C_BABJ_20190528001020_77100.bin";
//		String fileName="D:\\TEMP\\fengkuoxianBufrData\\A_IUPA54LFPW280000_C_BABJ_20190528001020_00000.bin";
		String fileName="D:\\TEMP\\B.1.15.1\\3-11\\A_IUPN57LFPW091520_C_BABJ_20200310151321_63149.bin";
		String tableSection="B.0001.0015.R001";
		List<String> tableList = new ArrayList<String>();
		 tableList.add("UPAR_WPF_GLB_TAB");
		  BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
		  bufrWindRadaServiceImpl.setDiQueues(diQueues);
		 
		Boolean decode = bufrWindRadaServiceImpl.decode(fileName, tableSection, tableList);
	}


}
