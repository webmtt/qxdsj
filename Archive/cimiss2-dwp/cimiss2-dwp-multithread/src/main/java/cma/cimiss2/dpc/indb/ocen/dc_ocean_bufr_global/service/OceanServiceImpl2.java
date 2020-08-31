package cma.cimiss2.dpc.indb.ocen.dc_ocean_bufr_global.service;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;
import org.cimiss2.dwp.tools.utils.GlbQcUtil;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrDecoder;
import com.hitec.bufr.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * The Class OceanServiceImpl.
 */
public class OceanServiceImpl2 implements BufrService {

	//protected static final Logger DPCLOG = LoggerFactory.getLogger("timeInfoLogger");
	
	/** The batch size. */
	int batchSize = 500;
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The Constant COLUMN_EXP_REGEX. */
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	
	/** The Constant THIS_VALUE_EXP. */
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The di. */
	private static List<StatDi> DI = new ArrayList<StatDi>();
	
	/** The ymdhm. */
	SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		String tableSection = "C.0001.0013.R001";
		tableList.add("OCEN_SHB_GLB_MUL_TAB");
////		tableList.add("SURF_WEA_GLB_MUL_HOR_TAB");
////		tableList.add("SURF_WEA_GLB_MUL_HOR_ORI_TAB");
//		tableList.add("ocen_tsc_glb_mul_tab");
//		tableList.add("ocen_tsc_glb_mul_k_tab");
//		tableList.add("OCEN_SHB_GLB_MUL_ORI_TAB");
//		tableList.add("OCEN_TSC_GLB_MUL_ORI_TAB");
//		tableList.add("OCEN_TSC_GLB_MUL_K_ORI_TAB");
		BufrConfig.init();
		tableList = BufrConfig.getTableMap().get(tableSection);
		
//		String fileName = "D:\\TEMP\\A.1.21.1\\9-2\\A_ISSD05LFPW020900RRA_C_BABJ_20190902090515_94389.bin";
		String fileName = "D:\\TEMP\\C.1.13.1\\8-30\\A_IORX01RJTD181200_C_BABJ_20190618144950_59893.bin";
		OceanServiceImpl2 oceanServiceImpl = new OceanServiceImpl2();
		boolean decode = oceanServiceImpl.decode(fileName, tableSection, tableList);
		if(decode){
			System.out.println("处理成功！");
		}else
			System.err.println("处理失败！");
//		bufrDecoder.bufrDecoder("D:\\A_ISAC01VHHH200300_C_BABJ_20180920031410_90199.bin");
	}

	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		OceanServiceImpl2.diQueues = diQueues;
	}
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	
	/* (non-Javadoc)
	 * @see cma.cimiss2.dpc.indb.ocen.dc_ocean_bufr_global.service.BufrService#decode(java.lang.String, byte[], java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrDecoder bufrDecoder = new BufrDecoder();
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
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
						ei.setKObject("cma.cimiss2.dpc.indb.ocen.bufr.service.OceanServiceImpl");
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

	/* (non-Javadoc)
	 * @see cma.cimiss2.dpc.indb.ocen.dc_ocean_bufr_global.service.BufrService#decode(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
		BufrDecoder bufrDecoder = new BufrDecoder();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
			ReportInfoToDb(bufrDecoder,tableSection,fileName);
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
					ei.setKObject("cma.cimiss2.dpc.indb.ocen.bufr.service.OceanServiceImpl");
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

	/** The primary key conflict key. */
	private final String PRIMARY_KEY_CONFLICT_KEY = "PRIMARY_KEY_CONFLICT";
	
	/**
	 * Decode.
	 *
	 * @param bufrDecoder the bufr decoder
	 * @param fileName the file name
	 * @param tableSection the table section
	 * @param tables the tables
	 * @return true, if successful
	 */
	@SuppressWarnings("unchecked")
	public boolean decode(BufrDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		// 获取数据库连接
		DruidPooledConnection connection = null;
		DruidPooledConnection connectionORI = null;
		Statement statement = null;
		Statement statementORI = null;
		Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		// 使要素表在键表之前，    xx_tab 在  xx_k_tab 之前
		boolean isValueTableRecordNull = true; // 插入要素表记录数是否为0
		boolean dateTimeOK = true;
		Map<String, Object> proMap = StationInfo.getProMap();
		try {
			File file = new File(fileName);
			if(OceanServiceImpl2.getDiQueues() == null)
				diQueues = new LinkedBlockingQueue<StatDi>();
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			connectionORI = ConnectionPoolFactory.getInstance().getConnection("rdb");
			statement = connection.createStatement();
			statementORI = connectionORI.createStatement();
			connection.setAutoCommit(false);
			connectionORI.setAutoCommit(false);
			GlbQcUtil glbQcUtil = new GlbQcUtil("3", fileName);
			for(Map<String, Object> members: bufrDecoder.getParseList()){
				String sta = "";
				double latitude = 999999;
				double longitude = 999999;
				String adminCode = "999999";
				String countrycode = "999999";
				String datetime = "";
				String V_BBB = "000";
				V_BBB = members.get("BBB").toString();
				boolean checkexist=false;
				
				dateTimeOK = true;
				isValueTableRecordNull = true; // 插入要素表记录数是否为0
				if(!"C.0001.0013.R001".equals(tableSection)){
					double [] cal=calcuLatAndLon(members,fileName,latitude,longitude,sta,tableSection);
					latitude=cal[0];
					longitude=cal[1];
				}
				for (String tableName : tables) {
					dateTimeOK = true;
					tableName = tableName.trim();
					XmlBean xmlBean = configMap.get(tableName);
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
					if(helpMap != null)
						entityMap.putAll(helpMap);
					
					// 用于存储入库的字段
					StringBuffer keyBuffer = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer = new StringBuffer();
					// 遍历配置信息
					Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
//					List<String> sqList = new ArrayList<>();
					//要素表
					Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
//					
					if(groupMapList != null && groupMapList.size() > 0){
						Set groupKey = groupMapList.keySet();
						Iterator<String> groupItor = null;
						for(groupItor = groupKey.iterator(); groupItor.hasNext(); ){
							dateTimeOK = true;
							String key = groupItor.next();
							int span = Integer.parseInt(key.split("_")[1]);
							String repeated = key.split("_")[0];
							Map<String, EntityBean> groupMap = groupMapList.get(key);
							entityMap.putAll(groupMap); // 后面覆盖前面的
							int repteats = 0;
							String prefix =  repeated.substring(0, repeated.lastIndexOf("-") + 1);
							if((List<BufrBean>)members.get(repeated) != null && ((List<BufrBean>)members.get(repeated)).size() > 0)
								isValueTableRecordNull = false; 
							while((List<BufrBean>)members.get(repeated) != null){
								dateTimeOK = true;
								
								List<BufrBean> list = (List<BufrBean>) members.get(repeated);
								int size = list.size() / span;
								StatDi di = null;
								for(int p = 0; p < size; p ++){
									if(tableName.toUpperCase().startsWith("OCEN_SHB_GLB_MUL")){ //资料 C.0001.0013.R001，只入库这一张表，一个台站，时间不同，多条记录
										di = new StatDi();
										di.setPROCESS_START_TIME(TimeUtil.getSysTime());
										di.setDATA_TYPE_1(tableSection);
									}
									String recordId="";
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
										if("D_RECORD_ID".equals(column)){
											System.out.println();
										}
										if("D_ELE_ID".equals(column)){
											System.out.println();
										}
										
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
													value = calExpressionValue(obj, members, entityMap, entityBean,latitude,longitude);
												}
											} else {
												value = defaultValue;
												if(column.equalsIgnoreCase("D_SOURCE_ID")){
													value += "_" + file.getName();
												}
											}
											
											if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
												value = members.get("CCCC");
											else if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
												value = members.get("TTAAii");
											else if(column.equalsIgnoreCase("V_BBB"))
												value = members.get("BBB");
											
											if(("V02001").equals(column.toUpperCase())&&!"A.0001.0021.R001".equals(tableSection)&&!"C.0001.0013.R001".equals(tableSection)){
												value=CalculateV02001 (column,tableSection,members, fileName);
											}
											if(column.equalsIgnoreCase("D_RYMDHM"))
												value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
											
											if(column.toUpperCase().equals("V11001")&&members.get("0-11-2-0")!=null){//计算风向
												double V11002D= Double.valueOf(((BufrBean)members.get("0-11-2-0")).getValue().toString());//取风速的值
												if(V11002D<=0.2){
													value=999017;
												}else if( V11002D >0.2 && Double.valueOf(value.toString())==0){
													value=999997;
												}
											}
											
											if((("D_RECORD_ID").equals(column.toUpperCase())||("D_ELE_ID").equals(column.toUpperCase()))&&!value.toString().startsWith("99999")){//替换d_record_id与d_ele_id中站号为999999的情况
												int index1=value.toString().indexOf("_");
												int index2=value.toString().indexOf("_",index1+1);
												String valueV01301="999999";
												if(value.toString().substring(index1+1).startsWith("999999")){
													try{
														Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
														Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
														Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));
//														value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
														//改正于2019-1-29
														valueV01301 = String.format("%01d", area.intValue()) + String.format("%01d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
														
													}catch (Exception e) {
														try{
															valueV01301 = ((BufrBean)members.get("0-1-11-0")).getValue().toString(); // C.0001.0014.R001
														}catch (Exception e1) {
															infoLogger.error(fileName + ": WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
														}
													}
													try{
														int v01301I=(int)Double.parseDouble(valueV01301);
														valueV01301=String.valueOf(v01301I);
													}catch (Exception e) {
														
													}
												 
													StringBuilder vb = new StringBuilder(value.toString());
													value=vb.replace(index1+1,index2, valueV01301);
												}
											}
											if("C.0001.0013.R001".equals(tableSection)&&column.equalsIgnoreCase("D_RECORD_ID")){
												recordId=value.toString();
											}
											
											// chy  修改
//											if(column.equalsIgnoreCase("D_RECORD_ID") 
//													&& (tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_TAB") || tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_ORI_TAB"))){
//												// 不入D_record_id
//												
//											}else if(column.equalsIgnoreCase("D_ELE_ID") 
//													&& (tableName.equalsIgnoreCase("ocen_tsc_glb_mul_tab") || (tableName.equalsIgnoreCase("OCEN_TSC_GLB_MUL_ORI_TAB")))){
//												// 不入 D_ELE_ID
//											}
//											else 
											if(!("C.0001.0013.R001".equals(tableSection)&&column.equalsIgnoreCase("D_RECORD_ID"))){
												keyBuffer.append(",`").append(column).append("`");
												valueBuffer.append(",'").append(value.toString().trim()).append("'");
											}
											
											if(column.equals("V01301")){
												sta=value.toString();
											}else if(column.equals("D_DATETIME")){
												datetime=value.toString();
												try{
													SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												    Date dataTime=simpleDateFormat.parse(value.toString());
													if(!TimeCheckUtil.checkTime(dataTime)){
//														dateTimeOK = false;
//														infoLogger.info("DataTime out of range:"+dataTime);
//														break;
													}
												}catch (Exception e) {
//													dateTimeOK = false;
//													infoLogger.error("资料时间错误！  " + fileName);
//													break;
												}
											}
											
											
											if(tableName.toUpperCase().startsWith("OCEN_SHB_GLB_MUL")){
												if(column.equals("D_DATETIME")){
													int idx01 = value.toString().lastIndexOf(":");
													if(idx01 == 16){
														di.setDATA_TIME(value.toString().substring(0, idx01));
														try{
															SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
														    Date dataTime=simpleDateFormat.parse(value.toString());
															if(!TimeCheckUtil.checkTime(dataTime)){
//																dateTimeOK = false;
//																infoLogger.info("DataTime out of range:"+dataTime);
//																break;
															}
														}catch (Exception e) {
															e.printStackTrace();
														}
													}else{
//														dateTimeOK = false;
//														infoLogger.error("资料时间错误！  " + fileName);
//														break;
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
											continue;
										}
										// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
										value = calExpressionValue(obj, members, entityMap, entityBean,latitude,longitude);
										if("C.0001.0013.R001".equals(tableSection)){
											if(column.equals("V05001")){
												latitude=Double.parseDouble(value.toString());
											}else if(column.equals("V06001")){
												longitude = Double.parseDouble(value.toString());
											}
										}else{
											if(column.equals("V05001")){
												value=latitude;
											}else if(column.equals("V06001")){
												value=longitude;
											}
										}
										
										if(tableName.toLowerCase().startsWith("ocen_tsc_glb_mul_tab") && helpMap.containsKey(column)){ // 要素表
											continue;
										}
										//测站
										if(column.toUpperCase().equals("V01301") && value.toString().trim().startsWith("999999")){
											try{
												Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
												Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
												Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));
										//		value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
												value = String.format("%01d", area.intValue()) + String.format("%01d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
												
											}catch (Exception e) {
												try{
													value = ((BufrBean)members.get("0-1-11-0")).getValue(); // C.0001.0014.R001
												}catch (Exception e1) {
													infoLogger.error(fileName + ": WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
												}
											}
										}
										if(column.toUpperCase().equals("V01301")){
											try{ // 如果是数字站号，转化为整数，去掉末尾的'.0'
												value = (int)Double.parseDouble(value.toString());
											}catch (Exception e) {
												
											}
										}
										if(column.equalsIgnoreCase("D_RYMDHM"))
											value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
										
										if(column.toUpperCase().equals("V11001")&&members.get("0-11-2-0")!=null){//计算风向
											double V11002D= Double.valueOf(((BufrBean)members.get("0-11-2-0")).getValue().toString());//取风速的值
											if(V11002D<=0.2){
												value=999017;
											}else if( V11002D >0.2 && Double.valueOf(value.toString())==0){
												value=999997;
											}
										}
										
										// chy  修改
//										if(column.equalsIgnoreCase("D_RECORD_ID") 
//												&& (tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_TAB") || tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_ORI_TAB"))){
//											// 不入D_record_id
//											
//										}else if(column.equalsIgnoreCase("D_ELE_ID") 
//												&& (tableName.equalsIgnoreCase("ocen_tsc_glb_mul_tab") || (tableName.equalsIgnoreCase("OCEN_TSC_GLB_MUL_ORI_TAB")))){
//											// 不入 D_ELE_ID
//										}
//										else 
										if(!("C.0001.0013.R001".equals(tableSection)&&("V05001".equalsIgnoreCase(column)||"V06001".equalsIgnoreCase(column)))){
											keyBuffer.append(",`").append(column).append("`");
											this.appendValue(valueBuffer, datatype, value.toString().trim());
										}
										if(column.equals("V01301")){
											sta=value.toString();
										}else if(column.equals("D_DATETIME")){
											datetime=value.toString();
											try{
												SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											    Date dataTime=simpleDateFormat.parse(value.toString());
												if(!TimeCheckUtil.checkTime(dataTime)){
//													dateTimeOK = false;
//													infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
//													break;
												}
											}catch (Exception e) {
//												dateTimeOK = false;
//												infoLogger.info("资料时间错误！  " + fileName);
//												break;
											}
										}
										
										if(tableName.toUpperCase().startsWith("OCEN_SHB_GLB_MUL")){
											if(column.equals("D_DATETIME")){
												int idx01 = value.toString().lastIndexOf(":");
												if(idx01 == 16){
													di.setDATA_TIME(value.toString().substring(0, idx01));
													try{
														SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													    Date dataTime=simpleDateFormat.parse(value.toString());
														if(!TimeCheckUtil.checkTime(dataTime)){
//															dateTimeOK = false;
//															infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
//															break;
														}
													}catch (Exception e) {
														e.printStackTrace();
													}
												}else{
//													dateTimeOK = false;
//													infoLogger.error("资料时间错误！  " + fileName);
//													break;
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
									}
									if("C.0001.0013.R001".equals(tableSection)){//经纬度换算后再拼到valueBuffer
										double [] cal0=calcuLatAndLon(members,fileName,latitude,longitude,sta,tableSection);
										latitude=cal0[0];
										longitude=cal0[1];
										
										String [] rec=recordId.split("_");
										recordId.replace(rec[2],String.valueOf(latitude));
										recordId.replace(rec[3],String.valueOf(longitude));
										
										keyBuffer.append(",`").append("V05001").append("`");
										this.appendValue(valueBuffer, "double", latitude);
										keyBuffer.append(",`").append("V06001").append("`");
										this.appendValue(valueBuffer, "double", longitude);
										
										// chy 修改 OCEN_SHB_GLB_MUL_TAB=bufr_ocen_shb_glb_mul_tab_13.xml 不入D_record_id 
										keyBuffer.append(",`").append("D_RECORD_ID").append("`");
										this.appendValue(valueBuffer, "string", recordId);
										
									}
									if(tableName.toUpperCase().startsWith("OCEN_SHB_GLB_MUL")){
										di.setLATITUDE(String.valueOf(latitude));
										di.setLONGTITUDE(String.valueOf(longitude));
										di.setFILE_SIZE(String.valueOf(file.length()));
										di.setSEND("BFDB");
										di.setSEND_PHYS("DRDS");
										di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
										di.setTT("");
										di.setPROCESS_END_TIME(TimeUtil.getSysTime());
										di.setRECORD_TIME(TimeUtil.getSysTime());
										di.setFILE_NAME_N(fileName);
										di.setFILE_NAME_O(fileName);
										di.setBUSINESS_STATE("1");
										di.setPROCESS_STATE("1");
									}
									// 集成质控,用数据中的0,1下标值分别代替keyBuffer和valBuffer
									String sql = null;
									if(StartConfig.isQC()){
										String[] qcAfterSql = glbQcUtil.getQcAfterSql(keyBuffer.toString().substring(1), valueBuffer.toString().substring(1));
										sql = "insert into " + tableName //
												+ " ("
												+ qcAfterSql[0] //
												+ ") values ("//
												+ qcAfterSql[1] //
												+ ")";
									}else {
										sql = "insert into " + tableName //
											+ " (" //
											+ keyBuffer.toString().substring(1) //
											+ ") values ("//
											+ valueBuffer.toString().substring(1) //
											+ ")";
									}
//									DPCLOG.info("质控结束1" + " | " + fileName + " | " + recordId + " | " + "bufr" + " | " +  System.currentTimeMillis());

									//入TSC要素原始表的SQL(C.0001.0012.R001或C.0001.0014.R001)
//									String sqlORI1 = "insert into " + "OCEN_TSC_GLB_MUL_ORI_TAB "//
//											+ " (" //
//											+ qcAfterSql[0] //
//											+ ") values ("//
//											+ qcAfterSql[1] //
//											+ ")";
									String sqlORI1 = "insert into " + "OCEN_TSC_GLB_MUL_ORI_TAB "//
											+ " (" //
											+ keyBuffer.toString().substring(1) //
											+ ") values ("//
											+ valueBuffer.toString().substring(1) //
											+ ")";
									//入SHB原始表的SQL(C.0001.0013.R001)
//									String sqlORI2 = "insert into " + "OCEN_SHB_GLB_MUL_ORI_TAB "//
//											+ " (" //
//											+ qcAfterSql[0]  //
//											+ ") values ("//
//											+ qcAfterSql[1] //
//											+ ")";
									String sqlORI2 = "insert into " + "OCEN_SHB_GLB_MUL_ORI_TAB "//
											+ " (" //
											+ keyBuffer.toString().substring(1) //
											+ ") values ("//
											+ valueBuffer.toString().substring(1) //
											+ ")";
		//							System.out.println(sql);
//									sqList.add(sql);
									for(int c = 0; c < span; c++)
										list.remove(0);
									
									if(dateTimeOK == false)
										break;
									
									if(tableName.toUpperCase().startsWith("OCEN_SHB_GLB_MUL")){
										DI.add(di);
									}
									if("C.0001.0012.R001".equals(tableSection)||"C.0001.0014.R001".equals(tableSection)){
										try{//TSC要素表的原始表入库
											statementORI.execute(sqlORI1);
											connectionORI.commit();
											infoLogger.info("成功插入一条记录:  OCEN_TSC_GLB_MUL_ORI_TAB 文件名："+fileName);
										}catch (Exception e) {
											infoLogger.error("插入失败：" + "OCEN_TSC_GLB_MUL_ORI_TAB "+ "  "+ sqlORI1 + "\n" + e.getMessage());
										}
									}else if("C.0001.0013.R001".equals(tableSection)){
										try{//SHB表的原始表入库
											statementORI.execute(sqlORI2);
											connectionORI.commit();
											infoLogger.info("成功插入一条记录:  OCEN_SHB_GLB_MUL_ORI_TAB 文件名："+fileName);
										}catch (Exception e) {
											infoLogger.error("插入失败：" + "OCEN_SHB_GLB_MUL_ORI_TAB " + "  "+ sqlORI2 + "\n" + e.getMessage());
										}
									}
//									if(tableName.toUpperCase().contains("ORI")||"A.0001.0021.R001".equals(tableSection)){//原始表或A.0001.0021.R001的资料直接入库操作
//										try{
//											statement.execute(sql);
//											connection.commit();
//											infoLogger.info("成功插入一条记录: " + tableName+" 文件名："+fileName+"sql:"+sql);
//										}catch (Exception e) {
//											if(tableName.toUpperCase().startsWith("OCEN_SHB_GLB_MUL"))
//												di.setPROCESS_STATE("0");
//											infoLogger.error("插入失败：" + fileName + "  "+ sql + "\n" + e.getMessage());
//										}
//									}else{//非原始表
									if(!tableName.toUpperCase().contains("ORI")&&!"A.0001.0021.R001".equals(tableSection)){
										//检查ocen_tsc_glb_mul_k_tab表中是否有重复记录(C.0001.0013.R001的数据检查OCEN_SHB_GLB_MUL_TAB表)
										if(checkexist==false){//未检查过是否有重复记录
											String taN="OCEN_TSC_GLB_MUL_K_TAB";
											if("C.0001.0013.R001".equals(tableSection)){
												taN="OCEN_SHB_GLB_MUL_TAB";
											}
											String rnt [] = selectV_TT (sta, latitude, longitude, datetime, taN, connection);//检查库里的重复记录
											String V_TT = rnt[0];
											String BBB = rnt[1];
											if(!"C.0001.0013.R001".equals(tableSection)){
											    checkexist=true;
											}
											double latu=latitude+StartConfig.getLatitudeRange();
											double latd=latitude-StartConfig.getLatitudeRange();
											double longu=longitude+StartConfig.getLatitudeRange();
											double longd=longitude-StartConfig.getLatitudeRange();
											String deleteSQL1 = "Delete from OCEN_SHB_GLB_MUL_TAB where D_datetime=" + "'" +datetime+ "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu ;
											String deleteSQL2="Delete from OCEN_TSC_GLB_MUL_TAB  where  d_record_id =(select d_record_id from OCEN_TSC_GLB_MUL_K_TAB   where D_datetime=" + "'" +datetime+ "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu +" limit 1)";
											String deleteSQL3 = "Delete from OCEN_TSC_GLB_MUL_K_TAB  where D_datetime=" + "'" +datetime+ "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu ;
											
											if(V_TT!=null&&(V_TT.startsWith("S"))){//库里有TAC的数据，删除三张表中TAC的数据
												// “S" 标识源为TAC 先删除 
												try{
													int ds1=statement.executeUpdate(deleteSQL1);
													connection.commit();
													infoLogger.info("OCEN_SHB_GLB_MUL_TAB表成功删除"+ds1+"条TAC数据: " + deleteSQL1 + "\n" );
													
												}catch (Exception e) {
	//													e.printStackTrace();
													infoLogger.error("Delete record failed: " + deleteSQL1 + "\n" + e.getMessage());
												}
												if("C.0001.0012.R001".equals(tableSection)||"C.0001.0014.R001".equals(tableSection)){
													try{
														int ds2=statement.executeUpdate(deleteSQL2);
														connection.commit();
														infoLogger.info("OCEN_TSC_GLB_MUL_TAB表成功删除"+ds2+"条TAC数据: " + deleteSQL2 + "\n" );
													}catch (Exception e) {
														infoLogger.error("Delete record failed: " + deleteSQL2 + "\n" + e.getMessage());
													}
													try{
														int ds3=statement.executeUpdate(deleteSQL3);
														connection.commit();
														infoLogger.info("OCEN_TSC_GLB_MUL_K_TAB表成功删除"+ds3+"条TAC数据: " + deleteSQL3 + "\n" );
													}catch (Exception e) {
														infoLogger.error("Delete record failed: " + deleteSQL3 + "\n" + e.getMessage());
													}
												}
											}else if(V_TT!=null&&V_TT.startsWith("I")){//库里是BUFR数据
												if(V_BBB.startsWith("CC")&&( V_BBB.compareTo(BBB) > 0||BBB.startsWith("RR"))){
													try{
														int ds1=statement.executeUpdate(deleteSQL1);
														connection.commit();
														infoLogger.info("OCEN_SHB_GLB_MUL_TAB表成功删除"+ds1+"条待更正数据: " + deleteSQL1 + "\n" );
														
													}catch (Exception e) {
		//													e.printStackTrace();
														infoLogger.error("Delete record failed: " + deleteSQL1 + "\n" + e.getMessage());
													}
													if("C.0001.0012.R001".equals(tableSection)||"C.0001.0014.R001".equals(tableSection)){
														try{
															int ds2=statement.executeUpdate(deleteSQL2);
															connection.commit();
															infoLogger.info("OCEN_TSC_GLB_MUL_TAB表成功删除"+ds2+"条待更正数据: " + deleteSQL2 + "\n" );
														}catch (Exception e) {
															infoLogger.error("Delete record failed: " + deleteSQL2 + "\n" + e.getMessage());
														}
														try{
															int ds3=statement.executeUpdate(deleteSQL3);
															connection.commit();
															infoLogger.info("OCEN_TSC_GLB_MUL_K_TAB表成功删除"+ds3+"条待更正数据: " + deleteSQL3 + "\n" );
														}catch (Exception e) {
															infoLogger.error("Delete record failed: " + deleteSQL3 + "\n" + e.getMessage());
														}
													}
												}
												
											}
										}
									}
									//入库操作
									try{
										statement.execute(sql);
										connection.commit();
										infoLogger.info("成功插入一条记录: " + tableName+" 文件名："+fileName);
									}catch (Exception e) {
										if(tableName.toUpperCase().startsWith("OCEN_SHB_GLB_MUL"))
											di.setPROCESS_STATE("0");
										infoLogger.error("插入失败：" + fileName + "  "+ sql + "\n" + e.getMessage());
									}
								
								}// end for
								repteats ++;
								repeated = prefix + String.valueOf(repteats);
								if(tableSection.equals("C.0001.0018.R001"))
									break;
							} // end while
//							int len = sqList.size();
//							// 剩余未入库记录数 
//							int remain = sqList.size();
//							if(remain > batchSize)
//								len = batchSize;
//							for(int t = 0; t < sqList.size();){
//								for(int tt = t; tt < t + len; tt ++){
//									statement.addBatch(sqList.get(tt));
//								}
//								try{
//									statement.executeBatch();
//									connection.commit();
//								}catch (Exception e) {
//									infoLogger.error("海洋要素表批量提交失败：\n" + e.getMessage());
//									for(int tt = t; tt < t + len; tt ++){
//										try{
//											statement.executeUpdate(sqList.get(tt));
//											connection.commit();
//										}catch (Exception e1) {
//											if(tableName.toUpperCase().startsWith("OCEN_SHB_GLB_MUL") && tt < DI.size())
//												DI.get(tt).setPROCESS_STATE("0");
//											infoLogger.error("海洋要素表单条记录插入失败： \n" + sqList.get(tt) +"\n" + e1.getMessage());
//										}
//									}
//								}
//								t += len;
//								remain -= len;
//								len = remain;
//								if(remain > batchSize)
//									len = batchSize;
//							}
						}// end if
					}
					// 除了要素表之外的表
					else{
						dateTimeOK = true;
						if(isValueTableRecordNull == true && tableName.toLowerCase().startsWith("ocen_tsc_glb_mul_tab"))
							continue;
						StatDi di = new StatDi();
						di.setPROCESS_START_TIME(TimeUtil.getSysTime());
						di.setDATA_TYPE_1(tableSection);
						boolean is012001 = false; // 此时温度为取值-273.02
						boolean is012003 = false; //此时温度为取值-273.02
						while (it.hasNext()) {
							Entry<String, EntityBean> next = it.next();
							String column = next.getKey(); // 配置文件配置的字段名称
							EntityBean entityBean = next.getValue(); // 配置的每个字段属性
							String datatype = entityBean.getDatatype(); // 字段类型
							String expression = entityBean.getExpression(); // 此字段的表达式
							String fxy = entityBean.getFxy(); // 解码时存储数据的key
							if("D_RECORD_ID".equals(column)){
								System.err.println();
							}
							//有效波高，有两个fxy可以获取
							if(fxy.equals("1-1-0-1") &&((List<BufrBean>)members.get("1-1-0-1") == null || ((List<BufrBean>)members.get("1-1-0-1")).size() == 0)){
								entityBean.setFxy("1-1-0-2");
								fxy = "1-1-0-2";
							}
//							if("V02001".equals(column)){
//								System.out.println(222);
//							}
							String defaultValue = entityBean.getDefaultValue(); // 缺省值
							Object obj = members.get(fxy); // 根据fxy获取每一行数据
							// 如果获取的对象为null
							Object value = null;
							if(obj == null && fxy.equals("0-12-101-0") && tableSection.equals("A.0001.0021.R001")){
								entityBean.setFxy("0-12-1-0");
								fxy = "0-12-1-0";
								is012001 = true;
							}else if(obj == null && fxy.equals("0-12-103-0") && tableSection.equals("A.0001.0021.R001")){
								entityBean.setFxy("0-12-3-0");
								fxy = "0-12-3-0";
								is012003 = true;
							}
							if (obj == null) {
								// 如果表达式不为空说明此值需要根据表达式进行计算value
								if (StringUtils.isNotBlank(expression)) {
									// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
									Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
									if (em.find()) {
										value = defaultValue;
									} else {// 如果表达式存在，且其中没有以index取值
										value = calExpressionValue(obj, members, entityMap, entityBean,latitude,longitude);
									}
								} else {
									value = defaultValue;
									if(column.equalsIgnoreCase("D_SOURCE_ID")){
										value += "_" + file.getName();
									}
								}
								// 水温可能从两个fxy取值
								if(column.toUpperCase().equals("V22049") && value.toString().trim().startsWith("999999") && !tableSection.equals("A.0001.0021.R001")){//
									obj = members.get("0-22-43-0");
									if(obj != null)
										value = calExpressionValue(obj, members, entityMap, entityBean,latitude,longitude);
								}
								else if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
									value = members.get("CCCC");
								else if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
									value = members.get("TTAAii");
								else if(column.equalsIgnoreCase("V_BBB"))
									value = members.get("BBB");
								
								if(("V02001").equals(column.toUpperCase())&&!"A.0001.0021.R001".equals(tableSection)&&!"C.0001.0013.R001".equals(tableSection)){
									value=CalculateV02001 (column,tableSection,members, fileName);
								}
								if(column.equalsIgnoreCase("D_RYMDHM"))
									value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
								
								if(column.toUpperCase().equals("V11001")&&members.get("0-11-2-0")!=null){//计算风向
									double V11002D= Double.valueOf(((BufrBean)members.get("0-11-2-0")).getValue().toString());//取风速的值
									if(V11002D<=0.2){
										value=999017;
									}else if( V11002D >0.2 && Double.valueOf(value.toString())==0){
										value=999997;
									}
								}
								if(("D_RECORD_ID").equals(column.toUpperCase())&&!value.toString().startsWith("99999")){//替换d_record_id中站号为999999的情况
									int index1=value.toString().indexOf("_");
									int index2=value.toString().indexOf("_",index1+1);
									String valueV01301="999999";
									if(value.toString().substring(index1+1).startsWith("999999")){
										try{
											Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
											Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
											Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));
//											value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
											//改正于2019-1-29
											valueV01301 = String.format("%01d", area.intValue()) + String.format("%01d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
											
										}catch (Exception e) {
											try{
												valueV01301 = ((BufrBean)members.get("0-1-11-0")).getValue().toString(); // C.0001.0014.R001
											}catch (Exception e1) {
												infoLogger.error(fileName + ": WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
											}
										}
										try{
											int v01301I=(int)Double.parseDouble(valueV01301);
											valueV01301=String.valueOf(v01301I);
										}catch (Exception e) {
											
										}
										StringBuilder vb = new StringBuilder(value.toString());
										value=vb.replace(index1+1,index2, valueV01301);
									}
								}
								if("V05001".equals(column)){
									value=latitude;
								}
								if("V06001".equals(column)){
									value=longitude;
								}
								
								// chy  修改
//								if(column.equalsIgnoreCase("D_RECORD_ID") 
//										&& (tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_TAB") || tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_ORI_TAB"))){
//									// 不入D_record_id
//									
//								}else if(column.equalsIgnoreCase("D_ELE_ID") 
//										&& (tableName.equalsIgnoreCase("ocen_tsc_glb_mul_tab") || (tableName.equalsIgnoreCase("OCEN_TSC_GLB_MUL_ORI_TAB")))){
//									// 不入 D_ELE_ID
//								}else{
									keyBuffer.append(",`").append(column).append("`");
									valueBuffer.append(",'").append(value.toString().trim()).append("'");
//								}
								
								if(column.equals("V01301")){
									sta=value.toString();
								}else if(column.equals("D_DATETIME")){
									datetime=value.toString();
								}
								
								
								if(column.equals("D_DATETIME")){
									int idx01 = value.toString().lastIndexOf(":");
									if(idx01 != 16){
//										dateTimeOK = false;
//										infoLogger.error("资料时间错误！  " + fileName);
//										break;
									}
									else{
										di.setDATA_TIME(value.toString().substring(0, idx01));
										try{
											SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										    Date dataTime=simpleDateFormat.parse(value.toString());
											if(!TimeCheckUtil.checkTime(dataTime)){
//												dateTimeOK = false;
//												infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
//												break;
											}
										}catch (Exception e) {
											e.printStackTrace();
										}
									}
								}
								else if(column.equals("V01301"))
									di.setIIiii(value.toString());
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
		
							// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//							System.err.println(column);
							value = calExpressionValue(obj, members, entityMap, entityBean,latitude,longitude);
							// 台站号，区域+子区域+浮标  或者  浮标扩展ID
							if(column.toUpperCase().equals("V01301") && value.toString().trim().startsWith("999999")){
								try{
									Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
									Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
									Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));
//									value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
									//改正于2019-1-29
									value = String.format("%01d", area.intValue()) + String.format("%01d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
									
								}catch (Exception e) {
									try{
										value = ((BufrBean)members.get("0-1-11-0")).getValue(); // C.0001.0014.R001
									}catch (Exception e1) {
										infoLogger.error(fileName + ": WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
									}
								}
							}
							if(column.toUpperCase().equals("V01301")){
								try{ // 如果是数字站号，转化为整数，去掉末尾的'.0'
									value = (int)Double.parseDouble(value.toString());
								}catch (Exception e) {
									
								}
							}
							else if(value.toString().startsWith("99999") && ((column.toUpperCase().equals("V12001") && is012001) || (column.toUpperCase().equals("V12003") && is012003))){
								try{
									double temp = Double.parseDouble(value.toString());
									temp -= 0.05;
									value = temp;
								}catch (Exception e) {
									infoLogger.error("温度值数据类型错误");
								}
							}
							if(column.equalsIgnoreCase("D_RYMDHM"))
								value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
							
							if(column.toUpperCase().equals("V11001")&&members.get("0-11-2-0")!=null){//计算风向
								double V11002D= Double.valueOf(((BufrBean)members.get("0-11-2-0")).getValue().toString());//取风速的值
								if(V11002D<=0.2){
									value=999017;
								}else if( V11002D >0.2 && Double.valueOf(value.toString())==0){
									value=999997;
								}
							}
							
							if("V05001".equals(column)){
								value=latitude;
							}
							if("V06001".equals(column)){
								value=longitude;
							}
							
							// chy  修改
//							if(column.equalsIgnoreCase("D_RECORD_ID") 
//									&& (tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_TAB") || tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_ORI_TAB"))){
//								// 不入D_record_id
//								
//							}else if(column.equalsIgnoreCase("D_ELE_ID") 
//									&& (tableName.equalsIgnoreCase("ocen_tsc_glb_mul_tab") || (tableName.equalsIgnoreCase("OCEN_TSC_GLB_MUL_ORI_TAB")))){
//								// 不入 D_ELE_ID
//							}
//							else{
								keyBuffer.append(",`").append(column).append("`");
								this.appendValue(valueBuffer, datatype, value.toString().trim());
//							}
								
							if(column.equals("V01301")){
								sta=value.toString();
							}else if(column.equals("D_DATETIME")){
								datetime=value.toString();
							}
							
							if(column.equals("D_DATETIME")){
								int idx01 = value.toString().lastIndexOf(":");
								if(idx01 == 16){
									di.setDATA_TIME(value.toString().substring(0, idx01));
									try{
										SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
									    Date dataTime=simpleDateFormat.parse(value.toString());
										if(!TimeCheckUtil.checkTime(dataTime)){
//											dateTimeOK = false;
//											infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
//											break;
										}
									}catch (Exception e) {
										e.printStackTrace();
									}
								}else{
//									dateTimeOK = false;
//									infoLogger.error("资料时间错误！  " + fileName);
//									break;
								}
							}
							else if(column.equals("V01301"))
								di.setIIiii(value.toString());
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
							
						}// end while
//						File file = new File(fileName);
						di.setFILE_SIZE(String.valueOf(file.length()));
						di.setSEND("BFDB");
						di.setSEND_PHYS("DRDS");
						di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
						di.setTT("");
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
						di.setFILE_NAME_N(fileName);
						di.setFILE_NAME_O(fileName);
						di.setBUSINESS_STATE("1");
						di.setPROCESS_STATE("1");
						
						if(dateTimeOK == false)
							continue;
						
						DI.add(di);
						// 键表入库
						// 集成质控,用数据中的0,1下标值分别代替keyBuffer和valBuffer
						String sql = null;
						if(StartConfig.isQC()&&!tableName.toUpperCase().contains("ORI")){
							String[] qcAfterSql = glbQcUtil.getQcAfterSql(keyBuffer.toString().substring(1), valueBuffer.toString().substring(1));
							sql = "insert into " + tableName //
									+ " ("
									+ qcAfterSql[0] //
									+ ") values ("//
									+ qcAfterSql[1] //
									+ ")";
						}else {
							sql = "insert into " + tableName //
									+ " (" //
									+ keyBuffer.toString().substring(1) //
									+ ") values ("//
									+ valueBuffer.toString().substring(1) //
									+ ")";
						}
//						DPCLOG.info("质控结束2" + " | " + fileName + " | " + recordId + " | " + "bufr" + " | " +  System.currentTimeMillis());
//						System.out.println(sql);
						
						/*
						      （1）入库处理时，先检查是否有重复记录，确认重复记录的原则如下：
							以datetime+站号+纬度范围+经度范围检查。
							纬度范围=[待入库记录纬度-给定范围, 待入库记录纬度+给定范围]
							经度范围=[待入库记录经度-给定范围, 待入库记录经度+给定范围]
							（2）如果没有重复记录，直接入库；
							（3）如果有重复记录，BUFR和TAC数据按照如下规则分别处理：
							
							BUFR处理:
								在发生冲突时，检查库中已有记录的V_TT字段TTAAii，
							    如果是I开头的表示为BUFR入库，后续处理按照正常的流程处理；再进行BBB项比较处理入库。更正报更新时采用datetime+站号+经度范围+纬度范围为条件.
								如果是S开头的表示为TAC入库，删除该条键表相关的记录，再入库。用datetime+站号为条件+纬度范围+经度范围为条件删除。深水表同时删除键表和要素表。
							纬度范围=[待入库记录纬度-给定删除范围, 待入库记录纬度+给定删除范围]
							经度范围=[待入库记录经度-给定删除范围, 待入库记录经度+给定删除范围]
							
							TAC处理:
								在发生冲突时，检查库中已有记录的V_TT字段TTAAii
								如果是I开头的表示为BUFR入库，后续不再处理；跳过当前记录，继续下一条记录处理。
								如果是S开头的表示为TAC入库，遵循正常的处理入库流程。更正报更新时采用datetime+站号+经度范围+纬度范围为条件.

						 */
						if(tableName.toUpperCase().contains("ORI")){//原始表直接入库
							try {
//								statementORI = connectionORI.createStatement();
								statementORI.execute(sql);
								connectionORI.commit();
	//							infoLogger.info("成功插入一条记录" + "  " + tableName);
								infoLogger.info("成功插入一条记录: " + tableName+" 文件名："+fileName);
							} catch (Exception e) {
								infoLogger.error("插入失败："+ fileName+ "\n"  + sql + "\n" + e.getMessage());
								di.setPROCESS_STATE("0");
							}
						 }else{
							 //A.0001.0021.R001资料先检查是否是国内地面站点
							 Boolean  isChinaSta=false;
							 if("A.0001.0021.R001".equals(tableSection)){
							 	String info = (String) proMap.get(sta + "+01");
								if(info == null) {
									infoLogger.error("\n In configuration file, this station does not exist!" + sta);
								}else{
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
								isChinaSta=isChinaSta(sta, countrycode, adminCode);
							 }
							 
							if(!("A.0001.0021.R001".equals(tableSection)&&tableName.toUpperCase().equals("SURF_WEA_GLB_MUL_HOR_TAB")&&isChinaSta )){//A.0001.0021.R001资料的国内站点不入地面表
								if(checkexist==false){//若未进行检查重复数据操作，进行如下检查
									String rnt [] = selectV_TT (sta, latitude, longitude, datetime, tableName, connection);//查询库里是否已经有数据
									String V_TT = rnt[0];
									String BBB=rnt[1];
									checkexist=true;
									
									double latu=latitude+StartConfig.getLatitudeRange();
									double latd=latitude-StartConfig.getLatitudeRange();
									double longu=longitude+StartConfig.getLatitudeRange();
									double longd=longitude-StartConfig.getLatitudeRange();
									String deleteSQL1 = "Delete from OCEN_SHB_GLB_MUL_TAB where D_datetime=" + "'" +datetime+ "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu ;
									String deleteSQL2="Delete from OCEN_TSC_GLB_MUL_TAB  where  d_record_id =(select d_record_id from OCEN_TSC_GLB_MUL_K_TAB   where D_datetime=" + "'" +datetime+ "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu +" limit 1)";
									String deleteSQL3 = "Delete from OCEN_TSC_GLB_MUL_K_TAB  where D_datetime=" + "'" +datetime+ "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu ;
									String deleteSQL4 = "Delete from SURF_WEA_GLB_MUL_HOR_TAB  where D_datetime=" + "'" +datetime+ "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu ;

									if(V_TT!=null){//库里有重复数据
										if(V_TT.startsWith("S")||(V_TT.startsWith("I")&&V_BBB.startsWith("CC")&& (V_BBB.compareTo(BBB) > 0||BBB.startsWith("RR")))){
	
										// “S" 标识源为TAC 先删除 
										try{
											int ds1=statement.executeUpdate(deleteSQL1);
											connection.commit();
											infoLogger.info("OCEN_SHB_GLB_MUL_TAB表成功删除"+ds1+"条TAC或BUFR待更正数据: " + deleteSQL1 + "\n" );
											
										}catch (Exception e) {
		//									e.printStackTrace();
											infoLogger.error("Delete record failed: " + deleteSQL1 + "\n" + e.getMessage());
										}
										if(!"A.0001.0021.R001".equals(tableSection)){
											try{
												int ds2=statement.executeUpdate(deleteSQL2);
												connection.commit();
												infoLogger.info("OCEN_TSC_GLB_MUL_TAB表成功删除"+ds2+"条TAC或BUFR待更正数据: " + deleteSQL2 + "\n" );
											}catch (Exception e) {
												infoLogger.error("Delete record failed: " + deleteSQL2 + "\n" + e.getMessage());
											}
											try{
												int ds3=statement.executeUpdate(deleteSQL3);
												connection.commit();
												infoLogger.info("OCEN_TSC_GLB_MUL_K_TAB表成功删除"+ds3+"条TAC或BUFR待更正数据: " + deleteSQL3 + "\n" );
											}catch (Exception e) {
												infoLogger.error("Delete record failed: " + deleteSQL3 + "\n" + e.getMessage());
											}
										}else{//A.0001.0021.R001资料删除地面表
											try{
												int ds4=statement.executeUpdate(deleteSQL4);
												connection.commit();
												infoLogger.info("SURF_WEA_GLB_MUL_HOR_TAB表成功删除"+ds4+"条TAC或BUFR待更正数据: " + deleteSQL4 + "\n" );
											}catch (Exception e) {
												infoLogger.error("Delete record failed: " + deleteSQL4 + "\n" + e.getMessage());
											}
										}
									  }
									}
								}
								try { //入库
									statement.execute(sql);
									connection.commit();
									infoLogger.info("成功插入一条记录！\n " + tableName + " " + fileName);
								}
								catch (Exception e) {
									infoLogger.error("插入失败:\n " + fileName + "\n" + sql + e.getMessage());
									di.setPROCESS_STATE("0");
								}
								
						 }
						}//非原始表end
						
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
			if(statement != null){
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(statementORI != null){
				try {
					statementORI.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connectionORI != null){
				try {
					connectionORI.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Cal expression value.
	 *
	 * @param obj the obj
	 * @param members the members
	 * @param entityMap the entity map
	 * @param entityBean the entity bean
	 * @return the object
	 */
	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean,double latitude,Double longitude) {
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
							Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
							Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
							Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));
					//		columnValue = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
							columnValue = String.format("%01d", area.intValue()) + String.format("%01d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
							
						}catch (Exception e) {
							try{
								columnValue = ((BufrBean)members.get("0-1-11-0")).getValue().toString(); // C.0001.0014.R001
							}catch (Exception e1) {
								infoLogger.error("台站号解析失败！\n");
							}
						}
					}
					if(expCol.toUpperCase().equals("V05001")){
						columnValue=String.valueOf(latitude);
					}
					if(expCol.toUpperCase().equals("V06001")){
						columnValue=String.valueOf(longitude);
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
						if(expCol.toUpperCase().equals("V01301")){
							if(columnValue.contains(".")){
								columnValue=columnValue.substring(0,columnValue.lastIndexOf("."));
							}
						}
						String sp [] = columnValue.split("[+\\*]");
//						if(expression.contains("V07065"))
//							System.out.println(expression);
						try{
							sp[0] = String.format("%d", Integer.parseInt(sp[0].replaceAll("\\{|\\}|\\$", "")));
						}
						catch (Exception e1) {
							try{
								sp[0] = String.format("%.3f", Double.parseDouble(sp[0].replaceAll("\\{|\\}|\\$", "")));
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

	/**
	 * Gets the column value.
	 *
	 * @param obj the obj
	 * @param entityBean the entity bean
	 * @return the column value
	 * @throws IndexOutOfBoundsException the index out of bounds exception
	 */
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
						String v = bufr.getValue().toString();
						if(entityBean.getDatatype().equalsIgnoreCase("double")){//double类型的字段结果可能是科学计数法，要将其变为普通的数字
							BigDecimal big = new BigDecimal(v);
							bufr.setValue(big.toPlainString());
						}
						
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
							
							String v = bufrBean.getValue().toString();
							if(entityBean.getDatatype().equalsIgnoreCase("double")){//double类型的字段结果可能是科学计数法，要将其变为普通的数字
								BigDecimal big = new BigDecimal(v);
								bufrBean.setValue(big.toPlainString());
							}
							
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

	/**
	 * Append value.
	 *
	 * @param buffer the buffer
	 * @param dataType the data type
	 * @param value the value
	 */
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

	public  boolean isNumeric0(String str){

		for(int i=0;i<str.length();i++){
			if(!Character.isDigit(str.charAt(i))){
				return false;
			}
		}
		return true;
	}
	public int CalculateV02001 (String column,String tableSection,Map<String, Object> members,String fileName){
			Object value=0;
			String v01301S="";
			try{
				if("C.0001.0012.R001".equals(tableSection)){
					v01301S=((BufrBean)members.get("0-1-87-0")).getValue().toString().trim();
				}else if("C.0001.0014.R001".equals(tableSection)){
					v01301S=((BufrBean)members.get("0-1-103-0")).getValue().toString().trim();
				}
			}catch (Exception e) {
				
			}
			if(v01301S.startsWith("999999")){
				try{
					Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
					Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
					Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));
//						value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
					//改正于2019-1-29
					v01301S = String.format("%01d", area.intValue()) + String.format("%01d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
				
				}catch (Exception e) {
					try{
						v01301S = ((BufrBean)members.get("0-1-11-0")).getValue().toString().trim(); // C.0001.0014.R001
					}catch (Exception e1) {
						infoLogger.error(fileName + ": WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
					}
				}
			}
			try{
				int v01301I=(int)Double.parseDouble(v01301S);
				v01301S=String.valueOf(v01301I);
			}catch (Exception e) {
				
			}
			String v01301ST=v01301S.trim();
			if(!isNumeric0(v01301ST)||(v01301ST.length()>5&&v01301ST.length()!=7)||(v01301ST.length()==7&&(v01301ST.startsWith("8")||v01301ST.startsWith("9")))){//船舶站，测站类型取10
				value=10;
			}else{
				value=21;
			}
		return (int)value;
	}
			
	String[] selectV_TT (String sta, double lat, double lon, String datetime, String tableName, java.sql.Connection connection){
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String V_TT = null;
		String V_BBB = null;
		String rntString [] = {V_TT, V_BBB};
		String sql = "select V_TT,V_BBB from "+tableName+" "
				+ "where  D_DATETIME = ? and V01301 = ? "
				+ " and V05001 >=? and V05001 <=? and V06001 >=? and V06001 <=?" + " limit 1";
		try{
			if(connection != null){	
				Pstmt = connection.prepareStatement(sql);
				int ii = 1;
				Pstmt.setString(ii++, datetime);
				Pstmt.setString(ii++, sta);
				Pstmt.setBigDecimal(ii++, new BigDecimal(lat - StartConfig.getLatitudeRange()).setScale(4, BigDecimal.ROUND_HALF_UP));
				Pstmt.setBigDecimal(ii++, new BigDecimal(lat + StartConfig.getLatitudeRange()).setScale(4, BigDecimal.ROUND_HALF_UP));
				Pstmt.setBigDecimal(ii++, new BigDecimal(lon - StartConfig.getLatitudeRange()).setScale(4, BigDecimal.ROUND_HALF_UP));
				Pstmt.setBigDecimal(ii++, new BigDecimal(lon + StartConfig.getLatitudeRange()).setScale(4, BigDecimal.ROUND_HALF_UP));
				
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					V_TT = resultSet.getString(1);
					V_BBB = resultSet.getString(2);
				}
			}
		}catch(SQLException e){
			infoLogger.error("\n create Statement error " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n close Statement error " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}catch(SQLException e){
					infoLogger.error("\n close resultSet error " + e.getMessage());
				}
			}
		}
		rntString[0] = V_TT;
		rntString[1] = V_BBB;
		return rntString;
		
	}
	boolean isChinaSta(String sta, String countryCode, String AdminCode){
		//国家代码是2250且行政代码小于710000的是国内站
		if(sta != null && countryCode != null && AdminCode != null){
			if(countryCode.equals("2250") && Double.parseDouble(countryCode)!=999999 && Double.parseDouble(countryCode) < 710000)
				return true; //国家站
			else 
				return false; // 国外站
		}else{
			return true; // 默认是国内的站
		}
		
	}
	//计算经纬度
	 double [] calcuLatAndLon(Map<String, Object> parseMap,String filename,double latitude,double longitude,String sta,String ctsCode){
		  Map<String, Object> proMap = StationInfo.getProMap();
		  String station ="999999";
		  double[] LatAndLon={latitude,longitude};
		  if("C.0001.0012.R001".equals(ctsCode)||"C.0001.0014.R001".equals(ctsCode)){
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
			  
			  if("C.0001.0012.R001".equals(ctsCode) && parseMap.get("0-1-87-0")!=null){
				  station=((BufrBean)parseMap.get("0-1-87-0")).getValue().toString();
				  if(station.startsWith("999999")){
					  station=transStation(parseMap,filename);//重新计算站号的组成
					}
			  }
			  if("C.0001.0014.R001".equals(ctsCode) && parseMap.get("0-1-103-0")!=null){
				  station=((BufrBean)parseMap.get("0-1-103-0")).getValue().toString();
				  if(station.startsWith("999999")){
					  station=transStation(parseMap,filename);//重新计算站号的组成
					}
			  }
			  
		  }else if("A.0001.0021.R001".equals(ctsCode)){
			  
			  if(parseMap.get("0-5-2-0")!=null){
				  double lat=Double.parseDouble(((BufrBean)parseMap.get("0-5-2-0")).getValue().toString());
				  if(lat>=-90 && lat<=90){
					  latitude=lat;
				  } 
			  }
			  if(parseMap.get("0-6-2-0")!=null){
				  double lon=Double.parseDouble(((BufrBean)parseMap.get("0-6-2-0")).getValue().toString());
				  if(lon>-180 && lon<180){
					  longitude=lon;
				  }
			  }
			  if(parseMap.get("0-1-11-0")!=null){
				  station=((BufrBean)parseMap.get("0-1-11-0")).getValue().toString();
				  if(station.startsWith("999999")){
					  station=transStation(parseMap,filename);//重新计算站号的组成
					}
			  }
		  }else if("C.0001.0013.R001".equalsIgnoreCase(ctsCode)){
			  if(latitude<-90 || latitude>90){
				  latitude=999999;
			  } 
			  if(longitude<-180 || longitude>180){
				  longitude=999999;
			  } 
			  station=sta;
		  }
		  if(latitude==999999.0||longitude==999999.0){
				String info = (String) proMap.get(station + "+10");
				if(info== null || "".equals(info)) {
					info = (String) proMap.get(station + "+09");
				}
				if(info==null|| "".equals(info)) {
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
	 String transStation(Map<String, Object> members,String filename){
		 String val="999999";
		 try{
				Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
				Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
				Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));
		//		value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
				val = String.format("%01d", area.intValue()) + String.format("%01d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
				
			}catch (Exception e) {
				try{
					val = ((BufrBean)members.get("0-1-11-0")).getValue().toString(); // C.0001.0014.R001
				}catch (Exception e1) {
					infoLogger.error(filename + ": WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
				}
			}
		 return val;
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
	public  void ReportInfoToDb(BufrDecoder bufrDecoder,String tableSection, String fileName) {
	 	int successOrNot=bufrDecoder.successOrNot;
		DruidPooledConnection report_connection = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		File file = new File(fileName);
		String filename = file.getName();
		Date recv = new Date(file.lastModified());
		String ReportTableName=StartConfig.reportTable();
//		String ReportTableName="OCEN_WEA_GLB_BULL_BUFR_TAB";
		String sod_code="";
		 if("A.0001.0021.R001".equals(tableSection)){
			 sod_code="A.0001.0025.S007";
		 }else{
			 sod_code="C.0001.0017.S003";
		 }
		
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
		
		
		PreparedStatement statement = null;
		try {
			report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			statement = new LoggableStatement(report_connection, sql);
			report_connection.setAutoCommit(false);
			
			
			if(successOrNot==1){//解码成功
				if(bufrDecoder.getParseList().size()>0){//解码有值
					for(Map<String, Object> members: bufrDecoder.getParseList()){
						//查站号
						String stafxy="";
						if("C.0001.0012.R001".equals(tableSection)){
							stafxy="0-1-87-0";
						}else if("C.0001.0013.R001".equals(tableSection)){
							stafxy="0-1-11-0";
						}else if("C.0001.0014.R001".equals(tableSection)){
							stafxy="0-1-103-0";
						}else if("A.0001.0021.R001".equals(tableSection)){
							stafxy="0-1-11-0";
						}
						String sta="999999";
						if(members.get(stafxy)!=null){
							sta=((BufrBean)(members.get(stafxy))).getValue().toString();
						}
						if(sta.trim().startsWith("999999")){
							try{
								Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
								Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
								Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));
						//		value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
								sta = String.format("%01d", area.intValue()) + String.format("%01d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
								
							}catch (Exception e) {
								try{
									sta = ((BufrBean)members.get("0-1-11-0")).getValue().toString(); // C.0001.0014.R001
								}catch (Exception e1) {
									infoLogger.error(fileName + ": ReportInfoToDb 站号解析失败！\n");
								}
							}
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
						 if("A.0001.0021.R001".equals(tableSection)){
							 sizes=bufrDecoder.getParseList().size();
						 }else{
							 int k=0;
							 String pre="";
							 String pre2="";
							 int span=0;
							 int span2=0;
							 if("C.0001.0012.R001".equals(tableSection)){
								 pre="1-9-0-";
								 span=9;
							 }else if("C.0001.0013.R001".equals(tableSection)){
								 pre="1-13-0-";
								 span=17;
							 }else if ("C.0001.0014.R001".equals(tableSection)) {
								 pre="1-6-0-";
								 span=6;
								 pre2="1-12-0-";
								 span2=12;
								 while((members.get(pre2+k))!=null){
										List<BufrBean> bufrList = (List<BufrBean>) members.get(pre2+k);
										BigDecimal a=new BigDecimal(bufrList.size()/span2).setScale(0,BigDecimal.ROUND_UP);
										int size=a.intValue();//每个group包含的记录数
										sizes+=size;
										k++;
									 }
							}
							 while((members.get(pre+k))!=null){
								List<BufrBean> bufrList = (List<BufrBean>) members.get(pre+k);
								BigDecimal a=new BigDecimal(bufrList.size()/span).setScale(0,BigDecimal.ROUND_UP);
								int size=a.intValue();//每个group包含的记录数
								sizes+=size;
								k++;
							 }
						 }
						 //查datetime
						if("C.0001.0013.R001".equals(tableSection)){
							Object obj=members.get("1-13-0-0");
							if(obj!=null){
								datetime_s=findValue(obj,datetime_s);
							}else{
								datetime_s.append("9999-01-01 00:00:00,");
							}
						}else{
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
						}
						
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
//					statement.setString(ii++, station_1 + "_" + sdf.format(datetime1));// d_record_id
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
//				statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
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
//				statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
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
	
	public StringBuffer findValue(Object obj,StringBuffer datetime_s){
		List<BufrBean> bufrList = (List<BufrBean>) obj;
		int span=17;//报文重复间隔
		BigDecimal a=new BigDecimal(bufrList.size()/span).setScale(0,BigDecimal.ROUND_UP);
		int n=a.intValue();//n条数据
		for(int j=0;j<n;j++){
			String[] date_time={"9999","01","01","00","00"};
			for(int i=0;i<5;i++){
	//			List<BufrBean> bufrList = (List<BufrBean>) obj;
				if ((i + 1) > bufrList.size()) {
					if(i==0)
						date_time[i]="9999";
					else if(i==1||i==2){
						date_time[i]="01";
					}else{
						date_time[i]="00";
					}
				}else{
					List<String> ts = new ArrayList<String>();
					
					List<BufrBean> bufrListCopy=new ArrayList<BufrBean>();
					BufrBean beancopy=new BufrBean();
					for(int p=j*span;p<bufrList.size();p++){
						beancopy=bufrList.get(p);
						bufrListCopy.add(beancopy);
					}
					
					for (BufrBean bufrBean : bufrListCopy) {
						ts.add(bufrBean.getValue().toString());
					}
					String expression="{"+i+"}";
					String ele = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
					try {
						if(i==0){
							if(!ele.startsWith("99999")){
								date_time[i] = String.format("%04d", (int)Double.parseDouble(ele));
							}
						}else{
							if(!ele.startsWith("99999")){
								date_time[i] = String.format("%02d", (int)Double.parseDouble(ele));
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			StringBuffer datetime=new StringBuffer();
			datetime.append(date_time[0]).append("-").append(date_time[1]).append("-").append(date_time[2]).append(" ").append(date_time[3]).append(":").append(date_time[4]).append(":").append("00");
			datetime_s.append(datetime).append(",");
//			for(int c = 0; c < span; c++)
//				bufrList.remove(0);
		}//循环span条数据
		return datetime_s;
	}
}
