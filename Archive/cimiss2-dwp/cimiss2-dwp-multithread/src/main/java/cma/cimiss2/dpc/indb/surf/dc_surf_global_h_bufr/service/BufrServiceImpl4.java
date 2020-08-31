package cma.cimiss2.dpc.indb.surf.dc_surf_global_h_bufr.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.BufrParseBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrSurfGlobalDecoder2;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class BufrServiceImpl.
 */
public class BufrServiceImpl4 implements BufrService {
	
	/** The pro map. */
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
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
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		String tableSection = "A.0001.0018.R001";
//		tableList.add("SURF_WEA_GBF_MUL_HOR_TAB");
//		tableList.add("SURF_WEA_GLB_MUL_HOR_TAB");
		BufrConfig.init();
		tableList = BufrConfig.getTableMap().get(tableSection);
//		String fileName = "D:\\TEMP\\A.1.18.1\\8-21\\A_ISMS02NSAP210600_C_BABJ_20190821062126_63144.bin";
//		String fileName = "D:\\TEMP\\A.1.18.1\\8-26\\A_ISMB33CWAO260600RRD_C_BABJ_20190826064816_21319.bin";
//		String fileName = "D:\\TEMP\\A.1.18.1\\8-26\\A_ISSF02LFPW260700_C_BABJ_20190826064920_216222.bin";//空文件
//		String fileName = "D:\\TEMP\\A.1.18.1\\9-9\\A_ISAX30LOWM090806RRE_C_BABJ_20190909080931_81116.bin";
//		String fileName = "D:\\tmp\\surf\\dataout\\A_ISAX30LOWM100008RRB_C_BABJ_20191110001130_90661.bin";
//		String fileName = "D:\\TEMP\\A.1.18.1\\11-26out\\3-1-1\\A_ISAX30LOWM260006RRA_C_BABJ_20191126000926_81887.bin";
//		String fileName = "F:\\HX2020\\A_ISAI01SBBR162100RRD_C_BABJ_20200316212106_11144.bin";
		String fileName ="/Users/zhangliang/Documents/test/A_ISMD02LLBD240000CCA_C_BABJ_20200424001431_55301.bin";
//		fileName = "F:\\HX2020\\A_ISAA20KWNB202200_C_BABJ_20200321005941_20402.bin";
//		fileName = "F:\\HX2020\\A_ISAN02DEMS100000_C_BABJ_20200310002625_84661.bin";
//		fileName = "F:\\HX2020\\A18\\A_ISNS46EGRR270100_C_BABJ_20200227012916_57998.bin";
//		String fileName = "F:\\HX2020\\A_ISAI01SBBR162100RRD_C_BABJ_20200316212106_11144.bin";
//		fileName = "F:\\HX2020\\A_ISND62EHDB160800_C_BABJ_20200316115525_62609.bin";
//		fileName = "F:\\HX2020\\A_ISMD62YRBK160600_C_BABJ_20200316062105_21280.bin";
		BufrServiceImpl4 bufrServiceImpl = new BufrServiceImpl4();
		boolean decode = bufrServiceImpl.decode(fileName, tableSection, tableList);
		if(decode){
			System.out.println("入库成功！");
		}else
			System.err.println("入库失败！");
		
	}
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	
	/**
	 * Sets the di queues.
	 *
	 * @param diQueue the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueue){
		BufrServiceImpl4.diQueues = diQueue;
	}
	
	/* (non-Javadoc)
	 * @see cma.cimiss2.dpc.indb.surf.dc_surf_global_h_bufr.service.BufrService#decode(java.lang.String, byte[], java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrSurfGlobalDecoder2 bufrDecoder = new BufrSurfGlobalDecoder2();
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
//				ReportInfoToDb(bufrDecoder, name);
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
						ei.setKObject("cma.cimiss2.dpc.indb.surf.dc_surf_global_h_bufr.service.BufrServiceImpl4");
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
	 * @see cma.cimiss2.dpc.indb.surf.dc_surf_global_h_bufr.service.BufrService#decode(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
		BufrSurfGlobalDecoder2 bufrDecoder = new BufrSurfGlobalDecoder2();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
//			ReportInfoToDb(bufrDecoder, fileName);
//			return this.decode(bufrDecoder, fileName, tableSection, tables);
			boolean flag = this.decode(bufrDecoder, fileName, tableSection, tables);
			return flag;
		} else {
			infoLogger.error("\n Read file error: "+ fileName);
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.surf.dc_surf_global_h_bufr.service.BufrServiceImpl4");
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

	/**
	 * @Title: decode_sub   
	 * @Description: 处理模板301001 （延迟重复1-18-0-0） 的报文解码入库  
	 * @param bufrDecoder
	 * @param fileName
	 * @param tableSection
	 * @param tables
	 * @return boolean      
	 */
	public boolean decode_sub(BufrSurfGlobalDecoder2 bufrDecoder, String fileName, String tableSection, List<String> tables) {
		Map<String, Object> proMap = StationInfo.getProMap();
		// 获取数据库连接
		DruidPooledConnection connection = null;
		DruidPooledConnection connection1 = null;
		DruidPooledConnection connection2 = null;
		Statement statement = null;
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		File file = new File(fileName);
		String fileSimple = file.getName();
		boolean dateTimeOK = true;
		Date recv = new Date(file.lastModified());
		try {
			// 2020-3-19 chy
			connection1 = ConnectionPoolFactory.getInstance().getConnection("rdb");
			connection2 = ConnectionPoolFactory.getInstance().getConnection("cimiss");
//			statement = connection.createStatement();
//			connection.setAutoCommit(false);
			
			List<BufrParseBean> parseBeans = bufrDecoder.getParseBeans();
			for(int j = 0; j < parseBeans.size(); j ++){
				List<Map<String , Object>> parseList = parseBeans.get(j).getParseList();
				int insertSucceedOrNot = -2;
				for(Map<String, Object> members: parseList){
	//				for(Map<String, Object> members: bufrDecoder.getParseList()){
					dateTimeOK = true;
					String sta = "";
					double latitude = 999999;
					double longitude = 999999;
					String datetime = "";
					String adminCode = "999999";
					String countrycode = "999999";
					String V_BBB = "000";
					double [] cal=calcuLatAndLon(members,fileName,latitude,longitude);
					latitude=cal[0];
					longitude=cal[1];
					Collections.sort(tables);
					for (String tableName : tables) {
						if(tableName.contains("ORI")){
							connection=connection2;
							statement = connection.createStatement();
							connection.setAutoCommit(false);
						}else{
							connection=connection1;
							statement = connection.createStatement();
							connection.setAutoCommit(false);
						}
						//-------2020-3-21 更新的字段 change by liyamin------------------------------------
						StringBuffer glb_tab_update = new StringBuffer();//存放更新字段
						//-------end 2020-3-21 更新的字段 change by liyamin--------------------
						dateTimeOK = true;
						Map<String, EntityBean> entityMap = null;
						XmlBean xmlBean = configMap.get(tableName.trim());
						Map<String, Map<String, EntityBean>> specialFileMapList = xmlBean.getSpecialFileList();
						if(specialFileMapList != null && specialFileMapList.size() > 0){
							entityMap = specialFileMapList.get("3-1-1");
						}
						
						try{
							sta = ((BufrBean)(members.get("0-1-1-0"))).getValue().toString();
						}catch (Exception e) {
							infoLogger.error("台站号获取失败！");
						}
						String info = (String) proMap.get(sta + "+01");
						if(info == null) {
							infoLogger.error("\n In configuration file, this station does not exist!" + sta);
						}
						else{
							String[] infos = info.split(",");
							if(infos[5].equals("null")){
								infoLogger.error("\n In configuration file, admin code is null!");
							}
							else
								adminCode = infos[5].trim();
						}
						int delay = 21;
						List<BufrBean> list_1_18_0 = (List<BufrBean>)members.get("1-18-0-0");
						int length_1_18_0 = 0;
						if(list_1_18_0 != null){
							length_1_18_0 = list_1_18_0.size();
						}
						int records = length_1_18_0 / delay;
						if(length_1_18_0 != 0 && length_1_18_0 % delay == 0){ // 有延迟重复而且重复片段长度为21
							for(int i = 0; i < records; i ++){
								members.put("1-18-0-0", list_1_18_0.subList(i * delay, (i + 1) * delay));
								StatDi di = new StatDi();
								di.setPROCESS_START_TIME(TimeUtil.getSysTime());
								di.setDATA_TYPE_1(StartConfig.ctsCode());
								// 用于存储入库的字段
								StringBuffer keyBuffer = new StringBuffer();
								// 用于存储入库的字段值
								StringBuffer valueBuffer = new StringBuffer();
								// 遍历配置信息
								Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
								//获取每个字段的值
								//获取每个字段的值
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
												value = calExpressionValue(obj, members, entityMap, entityBean,latitude,longitude);
											}
										} else {
											value = defaultValue;
											if(column.equalsIgnoreCase("D_SOURCE_ID"))
												value += "_" + fileSimple;
										}
										if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
											value = members.get("CCCC");
										else if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
											value = members.get("TTAAii");
										else if(column.equalsIgnoreCase("V_BBB"))
											value = members.get("BBB");
										else if(column.equalsIgnoreCase("D_RYMDHM")){
											value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recv);
										}
										else if(column.toUpperCase().equals("V05001")){
											value=latitude;
										}
										else if(column.toUpperCase().equals("V06001")){
											value=longitude;
										}
										keyBuffer.append(",`").append(column).append("`");
										valueBuffer.append(",'").append(value.toString().trim()).append("'");
										//-------2020-3-21 更新的字段 change by liyamin------------------------------------
										String notUpdateData="D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,V_BBB,D_DATETIME,D_DATA_DPCID,V01301,V01300";
										if(!notUpdateData.contains(column)){
											updateSql(glb_tab_update, column, datatype, value);
										}
										//-------end 2020-3-21 更新的字段 change by liyamin-----------
										if(column.equalsIgnoreCase("D_DATETIME")){
											int idx01 = value.toString().lastIndexOf(":");
											if(idx01 > 16||idx01==-1){
												dateTimeOK = false;
												infoLogger.error("资料时间错误！  " + fileName);
												break;   
											}
											else{
												di.setDATA_TIME(value.toString().substring(0, idx01));
												datetime = value.toString();
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
										else if(column.equalsIgnoreCase("V01301"))
											di.setIIiii(value.toString().trim());														
										else if(column.equalsIgnoreCase("D_DATA_ID"))
											di.setDATA_TYPE(value.toString());
										else if(column.equalsIgnoreCase("V_BBB")){
											di.setDATA_UPDATE_FLAG(value.toString());
											V_BBB = value.toString();
										}
										else if(column.equalsIgnoreCase("V_NCODE")){
											countrycode = value.toString();
										}
										continue;
									}
									else{
										value = calExpressionValue(obj, members, entityMap, entityBean,latitude,longitude);
									}
									//降水量的处理
									if((column.equalsIgnoreCase("V13019") || column.equalsIgnoreCase("V13020")
											|| column.equalsIgnoreCase("V13021") || column.equalsIgnoreCase("V13022")
											|| column.equalsIgnoreCase("V13023") || column.equalsIgnoreCase("V13011"))&& value.toString().startsWith("-0.1"))
										value = "999990";
									else if(column.equalsIgnoreCase("V13013") && value.toString().startsWith("-0.02")){
										value = "999990";
									}
									// （低或中云的）云量（对应编报云量）Nh、云属1、2、3的云量 单位换算
									else if(column.toUpperCase().startsWith("V20011")){
										value = getLowOrMidCloudAmount(value.toString());
									}
									else if(column.toUpperCase().equals("V05001")){
										value=latitude;
									}
									else if(column.toUpperCase().equals("V06001")){
										value=longitude;
									}
									try{ // 风向 判断
										if((column.equalsIgnoreCase("V11001")) && Double.parseDouble(value.toString()) == 0){
											if( ((BufrBean)members.get("0-11-2-0")) != null && !((BufrBean)members.get("0-11-2-0")).getValue().toString().equals("0.0")){
												
												value = "999997";
											}
											else if(((List<BufrBean>)(members.get("1-18-0-0"))) != null && !((List<BufrBean>)(members.get("1-18-0-0"))).get(19).getValue().toString().equals("0.0"))
												value = "999997";
										}
									}catch (Exception e) {
										System.err.println("风向值不为数值型，转换错误！");
									}
									try{
										if(datatype.equalsIgnoreCase("double") && (
												column.equalsIgnoreCase("V07001") || column.equalsIgnoreCase("V07031") || column.equalsIgnoreCase("V12001") ||
												column.equalsIgnoreCase("V12003") || column.equalsIgnoreCase("V10009") || column.equalsIgnoreCase("V12014") ||
												column.equalsIgnoreCase("V12015") || column.equalsIgnoreCase("V12016") || column.equalsIgnoreCase("V12017") ||
												column.startsWith("V07032_") || column.startsWith("V14032_"))){ // 日照时数
											
											value = String.format("%.4f", Double.parseDouble(value.toString()));
											if(Double.parseDouble(value.toString()) > 999999){
												value = defaultValue;
												infoLogger.error("double type value out of range: " + column + "value = " + value.toString());
											}
										}
									}catch (Exception e) {
										infoLogger.error("字段:" + column + "是double类型，数值超过 999999，存入999999！");
									}
									if(column.equalsIgnoreCase("V01015") && value.toString().contains("'")){
										String tmp = value.toString();
										tmp = tmp.replaceAll("'", "''");
										value = tmp;
									}
									keyBuffer.append(",`").append(column).append("`");
									this.appendValue(valueBuffer, datatype, value.toString().trim());
									
									//-------2020-3-21 更新的字段 change by liyamin------------------------------------
									String notUpdateData="D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,V_BBB,D_DATETIME,D_DATA_DPCID,V01301,V01300";
									if(!notUpdateData.contains(column)){
										updateSql(glb_tab_update, column, datatype, value);
									}
									//-------end 2020-3-21 更新的字段 change by liyamin------------
									
									if(column.equals("V01301"))
										di.setIIiii(value.toString().trim());
									else if(column.equals("V05001")){
										di.setLATITUDE(value.toString());
									}
									else if(column.equals("V06001")){
										di.setLONGTITUDE(value.toString());
									}
									else if(column.equals("V07001"))
										di.setHEIGHT(value.toString());
									else if(column.equals("D_DATA_ID"))
										di.setDATA_TYPE(value.toString());
									else if(column.equals("V_BBB")){
										di.setDATA_UPDATE_FLAG(value.toString());
										V_BBB = value.toString();
									}
									
								} // end while
	//							list_1_18_0 = list_1_18_0.subList(delay * (i + 1), list_1_18_0.size());
	//							for(int t = 0; t < delay; t ++){
	//								list_1_18_0.remove(0);
	//							}
								if(dateTimeOK == false)
									continue;
								
								//台站号缺测 时，不入库
								if(di.getIIiii().startsWith("999999") || di.getDATA_TIME().startsWith("9999")){
									infoLogger.error("台站号缺测 或者 资料时间缺测！ " + fileName);
									break;  // 站号缺测，跳出for table loop，不入库
								}
								
								di.setFILE_SIZE(String.valueOf(file.length()));
								di.setSEND("BFDB");
								di.setSEND_PHYS("DRDS");
								di.setTRAN_TIME(ymdhm.format(new Date()));
								di.setTT("");
								di.setPROCESS_END_TIME(TimeUtil.getSysTime());
								di.setRECORD_TIME(TimeUtil.getSysTime());
								di.setFILE_NAME_N(fileName);
								di.setFILE_NAME_O(fileName);
								di.setBUSINESS_STATE("1");
								di.setPROCESS_STATE("1");
								if(!tableName.toUpperCase().contains("ORI")){
									DI.add(di);
								}
								String sql = "insert into " + tableName //
										+ " (" //
										+ keyBuffer.toString().substring(1) //
										+ ") values ("//
										+ valueBuffer.toString().substring(1) //
										+ ")";
	//							System.out.println(sql);
								/*
								 * 处理规则：
									（1）地面资料入库时先根据站号检查是否为国内的地面站点，如果是国内的地面站点，则不再进行入库处理。
									（2）入库处理时，先检查是否有重复记录，确认重复记录的原则如下：
									以datetime+站号+纬度范围+经度范围检查。
									纬度范围=[待入库记录纬度-给定范围, 待入库记录纬度+给定范围]
									经度范围=[待入库记录经度-给定范围, 待入库记录经度+给定范围]
									示例：
									假设解码记录要入库纬度40.2，经度100.1，给定范围为0.1度。则需要查询的经纬度范围为：
									 [40.2-0.1,40.2+0.1]=[40.1,40.3]
									[100.1-0.1,100.1+0.1]=[100.0,100.2]
									
									（3）如果没有检索到记录，直接入库；
									（4）如果有重复记录，BUFR和TAC数据按照如下规则分别处理：
									BUFR处理规则
										检查库中已有记录的V_TT字段TTAAii，
									    如果是I开头的表示为BUFR入库，后续处理按照正常的流程处理；再进行BBB项比较处理入库。
										如果是S开头的表示为TAC入库，先删除该条记录（用datetime+站号为条件），再入库。
									
									TAC处理规则
										检查库中已有记录的V_TT字段TTAAii
										如果是I开头的表示为BUFR入库，后续不再处理；跳过当前记录，继续下一条记录处理。
										如果是S开头的表示为TAC入库，遵循正常的处理入库流程。
								 */
								if(tableName.toUpperCase().equals("SURF_WEA_GLB_MUL_HOR_ORI_TAB")){ //ORI表直接入库
									try { 
										statement.execute(sql);
										connection.commit();
										insertSucceedOrNot = 1;
										infoLogger.info("SURF_WEA_GLB_MUL_HOR_ORI_TAB表成功插入一条记录！\n " + tableName + " " + fileName);
									} 
									catch (Exception e) {
										infoLogger.error("SURF_WEA_GLB_MUL_HOR_ORI_TAB表插入失败:\n " + fileName + "\n" + sql + e.getMessage());
										di.setPROCESS_STATE("0");
									}
								}else if(!isChinaSta(sta, countrycode, adminCode)){ //国外的站，且是 SURF_WEA_GLB_MUL_HOR_TAB
									// 2020-3-21 yamin 
									boolean needUpdateData=false;
									///
									String rnt [] = selectV_TT (sta, latitude, longitude, datetime, tableName, connection);
									String V_TT = rnt[0];
									double latu=latitude+StartConfig.getLatitudeRange();
									double latd=latitude-StartConfig.getLatitudeRange();
									double longu=longitude+StartConfig.getLatitudeRange();
									double longd=longitude-StartConfig.getLatitudeRange();
									String deleteSQL = "Delete from " + tableName + " where D_datetime=" + "'" +datetime+ "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu ;
									 if(V_TT!=null&&V_TT.startsWith("S")){
										// “S" 标识源为TAC 先删除 
										try{
											int dsql1=statement.executeUpdate(deleteSQL);
											connection.commit();
											insertSucceedOrNot = 1;
											infoLogger.info("SURF_WEA_GLB_MUL_HOR_TAB表成功删除"+dsql1+"条TAC数据: " + deleteSQL + "\n" );
										}catch (Exception e) {
	//										e.printStackTrace();
											infoLogger.error("Delete record failed: " + deleteSQL + "\n" + e.getMessage());
										}
									}
									else if(V_TT!=null&&V_TT.startsWith("I")){//库里的是BUFR数据
										// BUFR更正报处理
										String BBB = rnt[1];
										if(V_BBB.startsWith("CC")&& (V_BBB.compareTo(BBB) > 0 ||BBB.startsWith("RR"))){//新到的数据是更正报，且大于库里的更正报，则更正库里数据（先删再插入）
											
											//新到的数据是更正报，且大于库里的更正报，则更正库里数据（先删再插入）
											//-------2020-3-21 更新的字段 change by liyamin------------------------------------
											needUpdateData=true;
//											try{
//												int dsql1=statement.executeUpdate(deleteSQL);
//												connection.commit();
//												insertSucceedOrNot = 1;
//												infoLogger.info("SURF_WEA_GLB_MUL_HOR_TAB表成功删除"+dsql1+"条需更正数据: " + deleteSQL + "\n" );
//											}catch (Exception e) {
//	//											e.printStackTrace();
//												infoLogger.error("Delete record failed: " + deleteSQL + "\n" + e.getMessage());
//											}
											if(!glb_tab_update.toString().isEmpty()){
												SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
												String update = "update " + tableName + " set " +glb_tab_update.toString() + 
														" D_UPDATE_TIME = '"+ dateFormat.format(new Date()) + "'" +" , V_BBB = '"+V_BBB+ "'" +" where d_datetime=" + "'" + datetime + "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu ; 
												try{
													int usql1=statement.executeUpdate(update);
													connection.commit();
													insertSucceedOrNot = 1;
													infoLogger.info(tableName+"表成功更新"+usql1+"条需更正数据！"+fileName+" sql:"+update+"\n");
												}catch (Exception e) {
													e.printStackTrace();
													infoLogger.info(tableName+"表更正数据失败！"+fileName+" sql:"+update+"\n"+ e.getMessage());
													di.setPROCESS_STATE("0");
												}
											}
											//-------end 2020-3-21 更新的字段 change by liyamin---
										}
										
									}
									 if(!needUpdateData){//2020-3-21 add by liyamin
									    try { //入库
											statement.execute(sql);
											connection.commit();
											insertSucceedOrNot = 1;
											infoLogger.info("SURF_WEA_GLB_MUL_HOR_TAB表成功插入一条记录！\n " + tableName + " " + fileName);
										} 
										catch (Exception e) {
											infoLogger.error("SURF_WEA_GLB_MUL_HOR_TAB表插入失败:\n " + fileName + "\n" + sql + e.getMessage());
											di.setPROCESS_STATE("0");
										} 
									 }
								}
							}
						}
						members.put("1-18-0-0", list_1_18_0);
					}
				} // end for
				// 入库报告表
				ReportInfoToDb(bufrDecoder, fileName, j, insertSucceedOrNot);
			} // end for
			if(parseBeans == null || parseBeans.size()==0){
				ReportInfoToDb(bufrDecoder, fileName, 0, 0);
			}
		} catch (Exception e) {
			infoLogger.error("数据库异常: " + e.getMessage());
			return false;
		} finally {
			if(statement != null){
				try {
					statement.close();
				} catch (SQLException e) {
					infoLogger.error("statement.close() Failed!");
				}
			}
			if(connection != null){
				try {
					connection.close();
				} catch (SQLException e) {
					infoLogger.error("connection.close() Failed!");
				}
			}
			if(connection1 != null){
				try {
					connection1.close();
				} catch (SQLException e) {
					infoLogger.error("connection1.close() Failed!");
				}
			}
			if(connection2 != null){
				try {
					connection2.close();
				} catch (SQLException e) {
					infoLogger.error("connection2.close() Failed!");
				}
			}
			if(BufrServiceImpl4.getDiQueues() == null)
				diQueues = new LinkedBlockingQueue<StatDi>();
			for (int j = 0; j < DI.size(); j++) {
				diQueues.offer(DI.get(j));
			}
			DI.clear();
		}
		return true;
	
	}
	/**
	 * Decode.
	 *
	 * @param bufrDecoder the bufr decoder
	 * @param fileName the file name
	 * @param tableSection the table section
	 * @param tables the tables
	 * @return true, if successful
	 */
	public boolean decode(BufrSurfGlobalDecoder2 bufrDecoder, String fileName, String tableSection, List<String> tables) {
		if(bufrDecoder.entrance != null && bufrDecoder.entrance.equals("3-1-1")){
			return decode_sub(bufrDecoder, fileName, tableSection, tables);
		}
		else{
			Map<String, Object> proMap = StationInfo.getProMap();
			// 获取数据库连接
			DruidPooledConnection connection = null;
			DruidPooledConnection connection1 = null;
			DruidPooledConnection connection2 = null;
			Statement statement = null;
			Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
			File file = new File(fileName);
			String fileSimple = file.getName();
			boolean dateTimeOK = true;
			Date recv = new Date(file.lastModified());
			try {
				//2020-3-22 chy
				connection1 = ConnectionPoolFactory.getInstance().getConnection("rdb");
				connection2 = ConnectionPoolFactory.getInstance().getConnection("cimiss");
//				statement = connection.createStatement();
//				connection.setAutoCommit(false);
				
				List<BufrParseBean> parseBeans = bufrDecoder.getParseBeans();
				for(int j = 0; j < parseBeans.size(); j ++){
					List<Map<String , Object>> parseList = parseBeans.get(j).getParseList();
					int insertSucceedOrNot = -2; // 每个parseList 一条入库即成功
					for(Map<String, Object> members: parseList){
	//					for(Map<String, Object> members: bufrDecoder.getParseList()){
						dateTimeOK = true;
						String sta = "";
						double latitude = 999999;
						double longitude = 999999;
						String datetime = "";
						String adminCode = "999999";
						String countrycode = "999999";
						String V_BBB = "000";
						double [] cal=calcuLatAndLon(members,fileName,latitude,longitude);
						latitude=cal[0];
						longitude=cal[1];
						for (String tableName : tables) {
							if(tableName.contains("ORI")){
								connection=connection2;
								statement = connection.createStatement();
								connection.setAutoCommit(false);
							}else{
								connection=connection1;
								statement = connection.createStatement();
								connection.setAutoCommit(false);
							}
							//-------2020-3-21 更新的字段 change by liyamin------------------------------------
							StringBuffer glb_tab_update = new StringBuffer();//存放更新字段
							//-------end 2020-3-21 更新的字段 change by liyamin--------------------
							dateTimeOK = true;
							StatDi di = new StatDi();
							di.setPROCESS_START_TIME(TimeUtil.getSysTime());
							di.setDATA_TYPE_1(StartConfig.ctsCode());
							
							XmlBean xmlBean = configMap.get(tableName.trim());
							Map<String, EntityBean> entityMap = null; //xmlBean.getEntityMap();
							Map<String, Map<String, EntityBean>> specialFileMapList = xmlBean.getSpecialFileList();
							String selectedKeyMaker = ""; 
							boolean isFounded = false;
							if(specialFileMapList != null && specialFileMapList.size() > 0){
								for(String key : specialFileMapList.keySet()){
									String []sp = key.split(",");
									selectedKeyMaker = key;
									for(String keySP : sp){
										if(bufrDecoder.entrance.equals(keySP.trim())){
											entityMap = specialFileMapList.get(key);
											isFounded = true;
											break;
										}
									}
									if(isFounded)
										break;
								}
								entityMap = specialFileMapList.get(selectedKeyMaker);
								if((entityMap == null || entityMap.isEmpty()) && fileSimple.indexOf("ISS") < 0){ // ISS为海洋资料
									infoLogger.error("This Template is not included in the table XML! Go to configurate it!");
									return true;
								}
							}
							else {
								infoLogger.error("Table XML file error!");
								return false; //
							}
							try{
								sta = "";
								sta = ((BufrBean)(members.get("0-1-1-0"))).getValue().toString();
								if(sta.contains("999999") &&  members.get("CCCC").toString().equalsIgnoreCase("KWNB")){
									try{
										sta = ((BufrBean)(members.get("0-1-10-0"))).getValue().toString().trim();
									}catch(Exception e1){
										infoLogger.error("台站号获取失败！");
									}
								}
							}catch (Exception e) {
								try{
									sta = ((BufrBean)(members.get("0-1-11-0"))).getValue().toString().trim();
								}catch(Exception e1){
//									infoLogger.error("从0-1-11-0 获取台站号失败！");
								}
							}
							//2020-3-19 chy  0-1-19-0 站名作为站号，未取到时，用0-1-101\0-1-102拼接
							if(members.get("CCCC").toString().equalsIgnoreCase("LLBD")) {
								sta=getStationForLLBD(sta,members);
							}else
								if(members.get("0-1-19-0") != null && (sta.startsWith("999999") || sta.isEmpty())){
								sta = ((BufrBean)(members.get("0-1-19-0"))).getValue().toString().trim();
							}
							else  if(sta.startsWith("999999") || sta.isEmpty()){
								try{
									String s1 = ((BufrBean)(members.get("0-1-101-0"))).getValue().toString().trim();
									String s2 = ((BufrBean)(members.get("0-1-102-0"))).getValue().toString().trim();
									Double d1 = Double.parseDouble(s1);
									Double d2 = Double.parseDouble(s2);
									
									sta = d1.intValue() + "_" + String.format("%05d", d2.intValue());
								}catch(Exception e11){
									infoLogger.error("台站号获取失败！");
								}
							}
							String info = (String) proMap.get(sta + "+01");
							if(info == null) {
								infoLogger.error("\n In configuration file, this station does not exist!" + sta);
							}
							else{
								String[] infos = info.split(",");
								if(infos[5].equals("null")){
									infoLogger.error("\n In configuration file, admin code is null!");
								}
								else
									adminCode = infos[5].trim();
							}
							// 用于存储入库的字段
							StringBuffer keyBuffer = new StringBuffer();
							// 用于存储入库的字段值
							StringBuffer valueBuffer = new StringBuffer();
							// 遍历配置信息
							Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
							//获取每个字段的值
							while (it.hasNext()) {
								Entry<String, EntityBean> next = it.next();
								String column = next.getKey(); // 配置文件配置的字段名称
								EntityBean entityBean = next.getValue(); // 配置的每个字段属性
								String datatype = entityBean.getDatatype(); // 字段类型
								String expression = entityBean.getExpression(); // 此字段的表达式
								String fxy = entityBean.getFxy(); // 解码时存储数据的key
								String defaultValue = entityBean.getDefaultValue(); // 缺省值
								if(column.equals("V01301")){
									System.out.print("");
								}

								Object obj = members.get(fxy); // 根据fxy获取每一行数据
								// 如果获取的对象为null
								Object value = null;
//								if(column.equals("D_DATETIME"))
//									System.out.println(column);					
								if(column.toUpperCase().equals("V01301") && members.get("CCCC").toString().equalsIgnoreCase("KWNB")){
									fxy = "0-1-10-0";
									obj = members.get(fxy);
								}
								else if(column.toUpperCase().equals("V01301") && obj == null){  // 默认情况下，取0-1-1-0
									fxy = "0-1-11-0";
									obj = members.get(fxy);
								}
								//chy 2020-3-19 
								if(column.toUpperCase().equals("V01301") && obj == null){ // 取0-1-19-0，否则,取 0-1-101\0-1-102
									fxy = "0-1-19-0";
									obj = members.get(fxy);
								}
								if(column.toUpperCase().equals("V01301") && obj == null){
									fxy = "0-1-101-0";
									obj = members.get(fxy);
								}
//								if(column.toUpperCase().equals("V01301") && obj == null &&)
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
										if(column.equalsIgnoreCase("D_SOURCE_ID"))
											value += "_" + fileSimple;
									}
									//20200529 zhangliang 
									/*
									 * 取WIGOS站号0-01-125, 0-01-126, 0-01-127, 0-01-128 四个描述符的组合拼接。
										前三个描述符为数字型，0-01-128 描述符为字符型。
										拼接方式为每段中间增加中划线"-"，前三个数字型描述符按原始解码值拼接，无需补0。

									 */
									if(column.equalsIgnoreCase("V01301") && members.get("CCCC").toString().equalsIgnoreCase("LLBD")) {
										value=getStationForLLBD(sta,members);
									}else 
									if(column.equalsIgnoreCase("V01301") && value.toString().startsWith("999999")){
										try{
											String s1 = ((BufrBean)(members.get("0-1-101-0"))).getValue().toString().trim();
											String s2 = ((BufrBean)(members.get("0-1-102-0"))).getValue().toString().trim();
											Double d1 = Double.parseDouble(s1);
											Double d2 = Double.parseDouble(s2);
											value = d1.intValue() + "_" + String.format("%05d", d2.intValue());
										}catch(Exception e){
											infoLogger.error("台站号获取失败！");
										}
									}
									
										
									if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
										value = members.get("CCCC");
									else if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
										value = members.get("TTAAii");
									else if(column.equalsIgnoreCase("V_BBB"))
										value = members.get("BBB");
									else if(column.equalsIgnoreCase("D_RYMDHM")){
										value = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(recv);
									}
									else if(column.toUpperCase().equals("V05001")){
										value=latitude;
									}
									else if(column.toUpperCase().equals("V06001")){
										value=longitude;
									}
									keyBuffer.append(",`").append(column).append("`");
									valueBuffer.append(",'").append(value.toString().trim()).append("'");
									//-------2020-3-21 更新的字段 change by liyamin------------------------------------
									String notUpdateData="D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,V_BBB,D_DATETIME,D_DATA_DPCID,V01301,V01300";
									if(!notUpdateData.contains(column)){
										updateSql(glb_tab_update, column, datatype, value);
									}
									//-------end 2020-3-21 更新的字段 change by liyamin-----------
									
									if(column.equalsIgnoreCase("D_DATETIME")){
										int idx01 = value.toString().lastIndexOf(":");
										if(idx01 > 16||idx01==-1){
											dateTimeOK = false;
											infoLogger.error("资料时间错误！  " + fileName);
											break;
										}
										else{
											di.setDATA_TIME(value.toString().substring(0, idx01));
											datetime = value.toString();
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
									else if(column.equalsIgnoreCase("V01301"))
										di.setIIiii(value.toString().trim());														
									else if(column.equalsIgnoreCase("D_DATA_ID"))
										di.setDATA_TYPE(value.toString());
									else if(column.equalsIgnoreCase("V_BBB")){
										di.setDATA_UPDATE_FLAG(value.toString());
										V_BBB = value.toString();
									}
									else if(column.equalsIgnoreCase("V_NCODE")){
										countrycode = value.toString();
									}
									continue;
								}
								else{
									value = calExpressionValue(obj, members, entityMap, entityBean,latitude,longitude);
								}
								//2020-3-19  台站号处理
								if(column.equalsIgnoreCase("V01301") && members.get("CCCC").toString().equalsIgnoreCase("LLBD")) {
									value=getStationForLLBD(sta,members);
								}else 
								if(column.equalsIgnoreCase("V01301") && value.toString().startsWith("999999")){
									try{
										String s1 = ((BufrBean)(members.get("0-1-101-0"))).getValue().toString().trim();
										String s2 = ((BufrBean)(members.get("0-1-102-0"))).getValue().toString().trim();
										Double d1 = Double.parseDouble(s1);
										Double d2 = Double.parseDouble(s2);
										value = d1.intValue() + "_" + String.format("%05d", d2.intValue());
									}catch(Exception e){
										infoLogger.error("台站号获取失败！");
									}
								}
								//降水量的处理
								if((column.equalsIgnoreCase("V13019") || column.equalsIgnoreCase("V13020")
										|| column.equalsIgnoreCase("V13021") || column.equalsIgnoreCase("V13022")
										|| column.equalsIgnoreCase("V13023") || column.equalsIgnoreCase("V13011"))&& value.toString().startsWith("-0.1"))
									value = "999990";
								else if(column.equalsIgnoreCase("V13013") && value.toString().startsWith("-0.02")){
									value = "999990";
								}
								// （低或中云的）云量（对应编报云量）Nh、云属1、2、3的云量 单位换算
								else if(column.toUpperCase().startsWith("V20011")){
									value = getLowOrMidCloudAmount(value.toString());
								}
								else if(column.toUpperCase().equals("V05001")){
									value=latitude;
								}
								else if(column.toUpperCase().equals("V06001")){
									value=longitude;
								}
								try{ // 风向 判断
									if((column.equalsIgnoreCase("V11001")) && Double.parseDouble(value.toString()) == 0){
										if( ((BufrBean)members.get("0-11-2-0")) != null && !((BufrBean)members.get("0-11-2-0")).getValue().toString().equals("0.0")){
											
											value = "999997";
										}
										else if(((List<BufrBean>)(members.get("1-18-0-0"))) != null && !((List<BufrBean>)(members.get("1-18-0-0"))).get(19).getValue().toString().equals("0.0"))
											value = "999997";
									}
								}catch (Exception e) {
									System.err.println("风向值不为数值型，转换错误！");
								}
								try{
									if(datatype.equalsIgnoreCase("double") && (
											column.equalsIgnoreCase("V07001") || column.equalsIgnoreCase("V07031") || column.equalsIgnoreCase("V12001") ||
											column.equalsIgnoreCase("V12003") || column.equalsIgnoreCase("V10009") || column.equalsIgnoreCase("V12014") ||
											column.equalsIgnoreCase("V12015") || column.equalsIgnoreCase("V12016") || column.equalsIgnoreCase("V12017") ||
											column.startsWith("V07032_") || column.startsWith("V14032_"))){ // 日照时数
										
										value = String.format("%.4f", Double.parseDouble(value.toString()));
										if(Double.parseDouble(value.toString()) > 999999){
											value = defaultValue;
											infoLogger.error("double type value out of range: " + column + "value = " + value.toString());
										}
									}
								}catch (Exception e) {
									infoLogger.error("字段:" + column + "是double类型，数值超过 999999，存入999999！");
								}
								if(column.equalsIgnoreCase("V01015") && value.toString().contains("'")){
									String tmp = value.toString();
									tmp = tmp.replaceAll("'", "''");
									value = tmp;
								}
								
								
								
								keyBuffer.append(",`").append(column).append("`");
								this.appendValue(valueBuffer, datatype, value.toString().trim());
								//-------2020-3-21 更新的字段 change by liyamin------------------------------------
								String notUpdateData="D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,V_BBB,D_DATETIME,D_DATA_DPCID,V01301,V01300";
								if(!notUpdateData.contains(column)){
									updateSql(glb_tab_update, column, datatype, value);
								}
								//-------end 2020-3-21 更新的字段 change by liyamin-----------
			
								if(column.equals("V01301"))
									di.setIIiii(value.toString().trim());
								else if(column.equals("V05001")){
									di.setLATITUDE(value.toString());
								}
								else if(column.equals("V06001")){
									di.setLONGTITUDE(value.toString());
								}
								else if(column.equals("V07001"))
									di.setHEIGHT(value.toString());
								else if(column.equals("D_DATA_ID"))
									di.setDATA_TYPE(value.toString());
								else if(column.equals("V_BBB")){
									di.setDATA_UPDATE_FLAG(value.toString());
									V_BBB = value.toString();
								}
							} // end while
							
							if(dateTimeOK == false)
								continue;
							
							//台站号缺测 时，不入库
							if(di.getIIiii().startsWith("999999") || di.getDATA_TIME().startsWith("9999")){
								infoLogger.error("台站号缺测 或者 资料时间缺测！ " + fileName);
								break;  // 站号缺测，跳出for table loop，不入库
							}
							
							di.setFILE_SIZE(String.valueOf(file.length()));
							di.setSEND("BFDB");
							di.setSEND_PHYS("DRDS");
							di.setTRAN_TIME(ymdhm.format(new Date()));
							di.setTT("");
							di.setPROCESS_END_TIME(TimeUtil.getSysTime());
							di.setRECORD_TIME(TimeUtil.getSysTime());
							di.setFILE_NAME_N(fileName);
							di.setFILE_NAME_O(fileName);
							di.setBUSINESS_STATE("1");
							di.setPROCESS_STATE("1");
							if(!tableName.toUpperCase().contains("ORI")){
								DI.add(di);
							}
							String sql = "insert into " + tableName //
									+ " (" //
									+ keyBuffer.toString().substring(1) //
									+ ") values ("//
									+ valueBuffer.toString().substring(1) //
									+ ")";
						System.out.println(sql);
							/*
							 * 处理规则：
								（1）地面资料入库时先根据站号检查是否为国内的地面站点，如果是国内的地面站点，则不再进行入库处理。
								（2）入库处理时，先检查是否有重复记录，确认重复记录的原则如下：
								以datetime+站号+纬度范围+经度范围检查。
								纬度范围=[待入库记录纬度-给定范围, 待入库记录纬度+给定范围]
								经度范围=[待入库记录经度-给定范围, 待入库记录经度+给定范围]
								示例：
								假设解码记录要入库纬度40.2，经度100.1，给定范围为0.1度。则需要查询的经纬度范围为：
								 [40.2-0.1,40.2+0.1]=[40.1,40.3]
								[100.1-0.1,100.1+0.1]=[100.0,100.2]
								
								（3）如果没有检索到记录，直接入库；
								（4）如果有重复记录，BUFR和TAC数据按照如下规则分别处理：
								BUFR处理规则
									检查库中已有记录的V_TT字段TTAAii，
								    如果是I开头的表示为BUFR入库，后续处理按照正常的流程处理；再进行BBB项比较处理入库。
									如果是S开头的表示为TAC入库，先删除该条记录（用datetime+站号为条件），再入库。
								
								TAC处理规则
									检查库中已有记录的V_TT字段TTAAii
									如果是I开头的表示为BUFR入库，后续不再处理；跳过当前记录，继续下一条记录处理。
									如果是S开头的表示为TAC入库，遵循正常的处理入库流程。
							 */
							if(tableName.toUpperCase().equals("SURF_WEA_GLB_MUL_HOR_ORI_TAB")){ //ORI表直接入库
								try { 
									statement.execute(sql);
									connection.commit();
									insertSucceedOrNot = 1;
									infoLogger.info("SURF_WEA_GLB_MUL_HOR_ORI_TAB表成功插入一条记录！\n " + tableName + " " + fileName);
								} 
								catch (Exception e) {
									infoLogger.error("SURF_WEA_GLB_MUL_HOR_ORI_TAB表插入失败:\n " + fileName + "\n" + sql + e.getMessage());
									di.setPROCESS_STATE("0");
								}
							}else if(!isChinaSta(sta, countrycode, adminCode)){ //国外的站，且是 SURF_WEA_GLB_MUL_HOR_TAB
								// 2020-3-21 yamin 
								boolean needUpdateData=false;
								
								String rnt [] = selectV_TT (sta, latitude, longitude, datetime, tableName, connection);
								String V_TT = rnt[0];
								double latu=latitude+StartConfig.getLatitudeRange();
								double latd=latitude-StartConfig.getLatitudeRange();
								double longu=longitude+StartConfig.getLatitudeRange();
								double longd=longitude-StartConfig.getLatitudeRange();
								String deleteSQL = "Delete from " + tableName + " where D_datetime=" + "'" +datetime+ "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu ;
								 if(V_TT!=null&&V_TT.startsWith("S")){
									// “S" 标识源为TAC 先删除 
									try{
										int dsql1=statement.executeUpdate(deleteSQL);
										connection.commit();
										insertSucceedOrNot = 1;
										infoLogger.info(tableName+"表成功删除"+dsql1+"条TAC数据: " + deleteSQL + "\n" );
									}catch (Exception e) {
		//								e.printStackTrace();
										infoLogger.error("Delete record failed: " + deleteSQL + "\n" + e.getMessage());
									}
								}
								else if(V_TT!=null&&V_TT.startsWith("I")){//库里的是BUFR数据
									// BUFR更正报处理
									String BBB = rnt[1];
									if(V_BBB.startsWith("CC")&& (V_BBB.compareTo(BBB) > 0 ||BBB.startsWith("RR"))){//新到的数据是更正报，且大于库里的更正报，则更正库里数据（先删再插入）
										//-------2020-3-21 更新的字段 change by liyamin------------------------------------
										needUpdateData=true;
//										try{
//											int dsql1=statement.executeUpdate(deleteSQL);
//											connection.commit();
//											insertSucceedOrNot = 1;
//											infoLogger.info(tableName+"表成功删除"+dsql1+"条需更正数据: " + deleteSQL + "\n" );
//										}catch (Exception e) {
//		//									e.printStackTrace();
//											infoLogger.error("Delete record failed: " + deleteSQL + "\n" + e.getMessage());
//										}
										if(!glb_tab_update.toString().isEmpty()){
											SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
											String update = "update " + tableName + " set " +glb_tab_update.toString() + 
													" D_UPDATE_TIME = '"+ dateFormat.format(new Date()) + "'" +" , V_BBB = '"+V_BBB+ "'" +" where d_datetime=" + "'" + datetime + "'" + " and V01301 = " + "'" + sta + "'"+" and V05001 >= "+latd+" and V05001 <= "+latu+" and V06001 >="+longd+" and V06001 <="+longu ; 
											try{
												int usql1=statement.executeUpdate(update);
												connection.commit();
												insertSucceedOrNot = 1;
												infoLogger.info(tableName+"表成功更新"+usql1+"条需更正数据！"+fileName+" sql:"+update+"\n");
											}catch (Exception e) {
												e.printStackTrace();
												infoLogger.info(tableName+"表更正数据失败！"+fileName+" sql:"+update+"\n"+ e.getMessage());
												di.setPROCESS_STATE("0");
											}
										}
										//-------end 2020-3-21 更新的字段 change by liyamin---
										
									}
									
								}
								if(!needUpdateData){//2020-3-21 add by liyamin
								    try { //入库
										statement.execute(sql);
										connection.commit();
										insertSucceedOrNot = 1;
										infoLogger.info(tableName+"表成功插入一条记录！\n " + tableName + " " + fileName);
									} 
									catch (Exception e) {
										infoLogger.error(tableName+"表插入失败:\n " + fileName + "\n" + sql + e.getMessage());
										di.setPROCESS_STATE("0");
									} 
								}
								
							}
							
						}
					} // end for
					// 报告表入库
					ReportInfoToDb(bufrDecoder, fileName, j, insertSucceedOrNot);
					
				}// END FOR
				if(parseBeans == null || parseBeans.size() == 0){
					// 报告表入库
					ReportInfoToDb(bufrDecoder, fileName, 0, 0);// 解码失败
				}
			} catch (Exception e) {
				e.printStackTrace();
				infoLogger.error("数据库异常: " + e.getMessage());
				return false;
			} finally {
				if(statement != null){
					try {
						statement.close();
					} catch (SQLException e) {
						infoLogger.error("statement.close() Failed!");
					}
				}
				if(connection != null){
					try {
						connection.close();
					} catch (SQLException e) {
						infoLogger.error("connection.close() Failed!");
					}
				}
				if(connection1 != null){
					try {
						connection1.close();
					} catch (SQLException e) {
						infoLogger.error("connection1.close() Failed!");
					}
				}
				if(connection2 != null){
					try {
						connection2.close();
					} catch (SQLException e) {
						infoLogger.error("connection2.close() Failed!");
					}
				}
				if(BufrServiceImpl4.getDiQueues() == null)
					diQueues = new LinkedBlockingQueue<StatDi>();
				for (int j = 0; j < DI.size(); j++) {
					diQueues.offer(DI.get(j));
				}
				DI.clear();
			}
			return true;
		}
	}
	/*
	 * 编报中心为LLBD的台站号取值方法
	 */
	private String getStationForLLBD(String sta, Map<String, Object> members) {
		if(members.get("0-1-125-0")!=null&&members.get("0-1-126-0")!=null&&members.get("0-1-127-0")!=null&&members.get("0-1-128-0")!=null) {
			String v1= ((BufrBean)(members.get("0-1-125-0"))).getValue().toString().trim().replaceAll("0+?$", "").replaceAll("[.]$", "");
			String v2= ((BufrBean)(members.get("0-1-126-0"))).getValue().toString().trim().replaceAll("0+?$", "").replaceAll("[.]$", "");
			String v3= ((BufrBean)(members.get("0-1-127-0"))).getValue().toString().trim().replaceAll("0+?$", "").replaceAll("[.]$", "");
			String v4= ((BufrBean)(members.get("0-1-128-0"))).getValue().toString().trim();
		  sta=v1+"-"+v2+"-"+v3+"-"+v4;
		}else if(members.get("0-1-1-0")!=null&&!members.get("0-1-1-0").toString().equals("00000")) {
			Double v1 = (Double.parseDouble(((BufrBean) members.get("0-1-1-0")).getValue().toString()));
			sta = String.format("%05d", v1.intValue()) ;
		}else if(members.get("0-1-15-0")!=null||members.get("0-1-18-0")!=null||members.get("0-1-19-0")!=null) {
			if(members.get("0-1-15-0")!=null) {
				sta=((BufrBean) members.get("0-1-15-0")).getValue().toString();
			}
			if(members.get("0-1-18-0")!=null) {
				sta=((BufrBean) members.get("0-1-18-0")).getValue().toString();
			}
			if(members.get("0-1-19-0")!=null) {
				sta=((BufrBean) members.get("0-1-19-0")).getValue().toString();
			}
		}else {
			String s1 = ((BufrBean)(members.get("0-1-101-0"))).getValue().toString().trim();
			String s2 = ((BufrBean)(members.get("0-1-102-0"))).getValue().toString().trim();
			Double d1 = Double.parseDouble(s1);
			Double d2 = Double.parseDouble(s2);
			
			sta = String.format("%03d", d1.intValue())+ "_" + String.format("%05d", d2.intValue());
		}
		return sta;
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
	/**
	 * Gets the low or mid cloud amount.
	 *
	 * @param string the string
	 * @return the low or mid cloud amount
	 */
	String getLowOrMidCloudAmount(String string){
		String str = string;
		switch(string){
			case "0.0":
				str = "0";
				break;
			case "1.0":
				str = "10";
				break;
			case "2.0":
				str = "25";
				break;
			case "3.0":
				str = "40";
				break;
			case "4.0":
				str = "50";
				break;
			case "5.0":
				str = "60";
				break;
			case "6.0":
				str = "75";
				break;
			case "7.0":
				str = "90";
				break;
			case "8.0":
				str = "100";
				break;
			case "999999.0":
				str = "999999";
			    break;
			default:
				str = "999998";
		}
		return str;
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
	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean,double latitude,double longitude) {
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
				if (obj2 == null && expCol.toUpperCase().equals("V01301")) { // 配置中 0-1-1-0
					fxy = "0-1-11-0";
					obj2 = members.get(fxy);
				}
				// 2020-3-19 chy
				if(obj2 == null && expCol.toUpperCase().equals("V01301")){
					fxy = "0-1-19-0";
					obj2 = members.get(fxy);
				}
				if((obj2 == null)&& expCol.toUpperCase().equals("V01301")){
					fxy = "0-1-101-0";
					obj2 = members.get(fxy);
				}
				if(obj2 == null)
					// 如果表达式获取字段值为空，直接返回默认值
					return entityBean.getDefaultValue();
				else {
					// 如果不为空，获取其对应的值
					if(expCol.toUpperCase().equals("V01301") && members.get("CCCC").toString().equalsIgnoreCase("KWNB")){
						fxy = "0-1-10-0";
						obj2 = members.get(fxy);
					}
					String columnValue = getColumnValue(obj2, ebean).toString();
//					if(ebean.getColumn().equalsIgnoreCase("V05001") || ebean.getColumn().equalsIgnoreCase("V06001"))
//						columnValue = String.format("%.4f", Double.parseDouble(columnValue));
					
					if(expCol.toUpperCase().equals("V05001")){
						columnValue=String.valueOf(latitude);
					}
					if(expCol.toUpperCase().equals("V06001")){
						columnValue=String.valueOf(longitude);
					}
					
					if (columnValue.equals(ebean.getDefaultValue())) {
						return columnValue;
					}
					if(ebean.getDatatype().equals("int")){
						int idx = columnValue.indexOf(".");
						if(idx >= 1)
							columnValue = columnValue.substring(0, idx);
					}
					//2020-3-19 chy
					if(expCol.toUpperCase().equals("V01301") && columnValue.startsWith("999999")){
						try{
							String s1 = ((BufrBean)(members.get("0-1-101-0"))).getValue().toString().trim();
							String s2 = ((BufrBean)(members.get("0-1-102-0"))).getValue().toString().trim();
							Double d1 = Double.parseDouble(s1);
							Double d2 = Double.parseDouble(s2);
							columnValue = d1.intValue() + "_" + String.format("%05d", d2.intValue());
						}catch(Exception e){
							infoLogger.error("获取台站号失败！");
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
						columnValue = columnValue.trim();
						expression = expression.replaceFirst(cExpReg, columnValue);
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
				if(value != null && StringUtil.string2Regex(thisMatcher.group(0)) != null)
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
		try{
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
						
						if(entityBean.getDatatype().equalsIgnoreCase("double") ){
							BigDecimal bigDecimal = new BigDecimal(v);
							bufr.setValue(bigDecimal.toPlainString());
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
							Object value2 = bufrBean.getValue();						
							if(entityBean.getDatatype().equalsIgnoreCase("double") ){
								BigDecimal bigDecimal = new BigDecimal(value2.toString());
								bufrBean.setValue(bigDecimal.toPlainString());
							}
							ts.add(bufrBean.getValue().toString());
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
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			if (isQc) {
				return 9;
			}else{
				return 999999;
			}
			// TODO: handle exception
		}
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
	//计算经纬度
	 double [] calcuLatAndLon(Map<String, Object> parseMap,String filename,double latitude,double longitude){
		  Map<String, Object> proMap = StationInfo.getProMap();
		  latitude=999999;
		  longitude=999999;
		  String station ="999999";
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
		  if(parseMap.get("0-1-1-0")!=null){
			  station=((BufrBean)parseMap.get("0-1-1-0")).getValue().toString();
		  }
		  if(latitude==999999.0||longitude==999999.0){
				String info = (String) proMap.get(station + "+01");
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
					ei.setKObject("cma.cimiss2.dpc.indb.upar.dc_upar_bufr_global_temp.service.UparGlobalBufrServiceImpl4");
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
	 
	 public  void ReportInfoToDb(BufrSurfGlobalDecoder2 bufrDecoder0, String fileName, int idx, int insertSucceedOrNot) {
		 	int successOrNot=bufrDecoder0.successOrNot;
			DruidPooledConnection report_connection = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			String entrance=bufrDecoder0.entrance;
			File file = new File(fileName);
			String filename = file.getName();
			Date recv = new Date(file.lastModified());
//			String ReportTableName="SURF_WEA_GLB_BULL_BUFR_TAB";
			String ReportTableName=StartConfig.reportTable();//"SURF_WEA_GLB_BULL_BUFR_TAB";
			String sql="insert into "+ReportTableName+"(D_RECORD_ID,D_DATA_ID,D_SOURCE_id,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
					+ "V01301,V_BBB,V_CCCC,V_YYGGgg,V_TT,V_LEN,V_FILE_SIZE,V_BULLETIN_B,V_FILE_NAME,V_NUM,V01301_list,d_datetime_list,"
					+ "V_decode_flag) values ("
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?)";
			
			StringBuffer station_s=new StringBuffer();//存站号list集合
			StringBuffer datetime_s=new StringBuffer();//存站号list集合
			String V_CCCC ="000";
			String V_BBB="000";
			String V_TT="999999";
			String V_YYGGgg="999999";
			
			PreparedStatement statement = null;
			try {
				//2020-3-19
				report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
				statement = new LoggableStatement(report_connection, sql);
				report_connection.setAutoCommit(false);
				List<BufrParseBean> parseBeans = bufrDecoder0.getParseBeans();
				List<Map<String , Object>> parseList = null;
				if(successOrNot==1){//解码成功
					parseList = parseBeans.get(idx).getParseList();
					if(parseList.size() > 0){
//						if(bufrDecoder0.getParseList().size()>0){//解码有值
//							for(Map<String, Object> members: bufrDecoder0.getParseList()){
						for(Map<String, Object> members: parseList){
							String sta="999999";
							if(members.get("CCCC").toString().equalsIgnoreCase("LLBD")) {
								sta=getStationForLLBD(sta,members);
							}else {
								if(members.get("0-1-1-0")!=null){
								  sta=((BufrBean)(members.get("0-1-1-0"))).getValue().toString();
								}
								if(sta.contains("999999") && members.get("0-1-10-0") !=null){
									 sta=((BufrBean)(members.get("0-1-10-0"))).getValue().toString().trim();
								}
								if(sta.contains("999999") && members.get("0-1-11-0") != null){
									sta = ((BufrBean)(members.get("0-1-11-0"))).getValue().toString().trim();
								}
							    // 2020-3-19 chy
								if(sta.contains("999999") && members.get("0-1-19-0") != null ){
									sta = ((BufrBean)(members.get("0-1-19-0"))).getValue().toString().trim();
								}
								else if(sta.contains("999999") && members.get("0-1-101-0") != null && members.get("0-1-102-0") != null){
									String s1 = ((BufrBean)(members.get("0-1-101-0"))).getValue().toString().trim();
									String s2 = ((BufrBean)(members.get("0-1-102-0"))).getValue().toString().trim();
									Double d1 = Double.parseDouble(s1);
									Double d2 = Double.parseDouble(s2);
									sta = d1.intValue() + "_" + String.format("%05d", d2.intValue());
								}///////////
								if(sta.contains(".")){
									sta=sta.substring(0,sta.lastIndexOf("."));
								}
								if(sta.startsWith("999999")){
									sta="999999";
								}
							}
							station_s.append(sta).append(",");
							
							 V_CCCC = members.get("CCCC").toString();
							 V_BBB=members.get("BBB").toString();
							 V_TT=members.get("TTAAii").toString();
							 V_YYGGgg=members.get("YYGGgg").toString();
							
							StringBuffer datetime=new StringBuffer();//存单个datetime
							if("3-1-1".equals(entrance)){
								Object obj=members.get("1-18-0-0");
								if(obj!=null){
									datetime_s=findValue(obj,datetime_s);
								}else{
									datetime_s.append("9999-01-01 00:00:00,");
								}
							}else{
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
						statement.setString(ii++, "A.0001.0025.S007");//D_DATA_ID
						statement.setString(ii++, "A.0001.0018.R001");//D_source_id
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
						if("3-1-1".equals(entrance)){
							statement.setInt(ii++, datetimes.split(",").length);//V_NUM 观测记录数
						}else{
//							statement.setInt(ii++, bufrDecoder0.getParseList().size());//V_NUM 观测记录数
							statement.setInt(ii++, parseList.size()); // V_NUM 观测记录数
						}
						statement.setString(ii++,stations);//V01301_list 观测记录站号列表
						statement.setString(ii++,datetimes);//d_datetime_list 观测记录时间列表
						if(insertSucceedOrNot == -2)
							statement.setInt(ii++, -2);//V_decode_flag 解码是否成功标志
						else
							statement.setInt(ii++,1);//V_decode_flag 解码是否成功标志
					}else{
						
					}
				}else if(successOrNot==2){//解码失败
					V_BBB=bufrDecoder0.V_BBB;
					V_CCCC=bufrDecoder0.V_CCCC;
					V_TT=bufrDecoder0.V_TTAAii;
					V_YYGGgg=bufrDecoder0.V_YYGGgg;
					byte[] databyte=getContent(fileName);//报文二进制数组
					int ii = 1;
					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
					statement.setString(ii++, "A.0001.0025.S007");//D_DATA_ID
					statement.setString(ii++, "A.0001.0018.R001");//D_source_id
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));// D_IYMDHM
					statement.setTimestamp(ii++, new Timestamp(recv.getTime()));// D_RYMDHM
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_UPDATE_TIME
					statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//D_DATETIME
					statement.setString(ii++, "999999");//V01301
					statement.setString(ii++, V_BBB);//V_BBB
					statement.setString(ii++, V_CCCC);//V_CCCC
					statement.setString(ii++, V_YYGGgg);//V_YYGGgg
					statement.setString(ii++, V_TT);//V_TT
					if(bufrDecoder0.normalFlag==1){//---------------报文有正常BUFR消息，但解码失败
						statement.setLong(ii++, file.length());//V_LEN    BUFR报文长度 ?????????????
					}else{//--------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setLong(ii++, 0);//V_LEN    BUFR报文长度 ?????????????
					}
					statement.setLong(ii++, file.length());//V_file_size 文件大小
					statement.setBytes(ii++, databyte);//V_BULLETIN_B 公报内容
//					statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
					statement.setString(ii++,filename);//V_FILE_NAME 文件名
					if(bufrDecoder0.normalFlag==1){//--------------报文有正常BUFR消息，但解码失败
						statement.setInt(ii++, -1);//V_NUM 观测记录数
					}else{//-----------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setInt(ii++, 0);//V_NUM 观测记录数
					}
					statement.setString(ii++,null);//V01301_list 观测记录站号列表
					statement.setString(ii++,null);//d_datetime_list 观测记录时间列表
					if(bufrDecoder0.normalFlag==1){//---------------------报文有正常BUFR消息，但解码失败
						statement.setInt(ii++,-1);//V_decode_flag 解码是否成功标志
				}else{//---------------------------------报文不包含正常的BUFR消息，即文件打不开，或空文件
					statement.setInt(ii++,0);//V_decode_flag 解码是否成功标志
				}
			}else {//未做解码， 即文件不存在
				byte[] databyte=new byte[0];
				int ii = 1;
				statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
				statement.setString(ii++, "A.0001.0025.S007");//D_DATA_ID
				statement.setString(ii++, "A.0001.0018.R001");//D_source_id
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
//					statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
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
				infoLogger.error("ReportInfoToDb 数据入报告表"+StartConfig.reportTable()+"失败！:"+fileName+((LoggableStatement)statement).getQueryString()+e.getMessage());
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
		int span=21;//报文重复间隔
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
	
	//-------2020-3-21 拼接更新的字段 add by liyamin---------
	private void updateSql(StringBuffer buffer, String col, String dataType, Object value) {
		buffer.append(col + "=");
		if ("int".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("short".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("byte".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("double".equalsIgnoreCase(dataType)) {
			BigDecimal bigDecimal = (new BigDecimal(value.toString())).setScale(4, BigDecimal.ROUND_HALF_UP);
			buffer.append(bigDecimal.toPlainString()).append(",");
		} else if ("float".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("long".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("date".equalsIgnoreCase(dataType)) {
			buffer.append("'").append(value).append("'").append(","); // TODO
		} else {
			buffer.append("'").append(value).append("'").append(",");
		}
	}
}
