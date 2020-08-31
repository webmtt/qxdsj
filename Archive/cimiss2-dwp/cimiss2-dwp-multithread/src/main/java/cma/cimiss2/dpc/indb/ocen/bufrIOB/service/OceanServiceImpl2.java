package cma.cimiss2.dpc.indb.ocen.bufrIOB.service;

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
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
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
import com.hitec.bufr.decoder.BufrOcenIOBDecoder2;
import com.hitec.bufr.util.StringUtil;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class OceanServiceImpl.
 */
public class OceanServiceImpl2 implements BufrService {
	
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
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		String tableSection = "C.0001.0018.R001";
		tableList.add("OCEN_SHB_GLB_MUL_TAB");
		tableList.add("ocen_tsc_glb_mul_tab");
		tableList.add("ocen_tsc_glb_mul_k_tab");
		String fileName = "D:\\DataTest\\ocen_bufr\\c18\\A_IOBX10LFVW160252_C_BABJ_20190516025340_26915.bin";
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
	 * @see cma.cimiss2.dpc.indb.ocen.bufrIOB.service.BufrService#decode(java.lang.String, byte[], java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrOcenIOBDecoder2 bufrDecoder = new BufrOcenIOBDecoder2();
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
						ei.setKObject("cma.cimiss2.dpc.indb.ocen.bufrIOB.service.OceanServiceImpl");
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
	 * @see cma.cimiss2.dpc.indb.ocen.bufrIOB.service.BufrService#decode(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
		
		BufrOcenIOBDecoder2 bufrDecoder = new BufrOcenIOBDecoder2();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
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
					ei.setKObject("cma.cimiss2.dpc.indb.ocen.bufrIOB.service.OceanServiceImpl");
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
	public boolean decode(BufrOcenIOBDecoder2 bufrDecoder, String fileName, String tableSection, List<String> tables) {
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
		if(bufrDecoder.entrance.equals("3-15-9")){
			tableSection += "_V1";
		}
			
		else {// bufrDecoder.entrance.equals("3-15-8") 和 0-1-3
			tableSection += "_V2";
		}
		if(tables == null){
			tables = BufrConfig.getTableMap().get(tableSection);
		}
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		// 使要素表在键表之前，    xx_tab 在  xx_k_tab 之前
		boolean isValueTableRecordNull = true; // 插入要素表记录数是否为0
		try {
			File file = new File(fileName);
			String simpleName = file.getName();
			if(OceanServiceImpl2.getDiQueues() == null)
				diQueues = new LinkedBlockingQueue<StatDi>();
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			statement = connection.createStatement();
			if(connection.getAutoCommit() == false)
				connection.setAutoCommit(true);
//			statement.execute("select last_txc_xid()");
			
			for(Map<String, Object> members: bufrDecoder.getParseList()){
				boolean dateTimeOK = true;
				isValueTableRecordNull = true; // 插入要素表记录数是否为0
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
					Map<String, Map<String, EntityBean>> orMapList = xmlBean.getOrMapList();
					
					//要素表
					Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
					if(groupMapList != null && groupMapList.size() > 0 ){
						if(((List<BufrBean>)members.get("1-3-0-0") != null && ((List<BufrBean>)members.get("1-3-0-0")).size() > 0)||
								((List<BufrBean>)members.get("1-3-0-1") != null && ((List<BufrBean>)members.get("1-3-0-1")).size() > 0) ||
								((List<BufrBean>)members.get("1-2-0-1") != null && ((List<BufrBean>)members.get("1-2-0-1")).size() > 0)){
							
//							isValueTableRecordNull = false; 
							Set groupKey = groupMapList.keySet();
							Iterator<String> groupItor = null;
							for(groupItor = groupKey.iterator(); groupItor.hasNext(); ){
								dateTimeOK = true;
								String key = groupItor.next();
								int span = Integer.parseInt(key.split("_")[1]);
								String repeated = key.split("_")[0];
								Map<String, EntityBean> groupMap = groupMapList.get(key);
								entityMap.putAll(groupMap); // 后面覆盖前面的
//								int repteats = 0;
//								String prefix =  repeated.substring(0, repeated.lastIndexOf("-") + 1);
								if((List<BufrBean>)members.get(repeated) != null){
									dateTimeOK = true;
									List<BufrBean> list = (List<BufrBean>) members.get(repeated);
									
									if(!list.get(0).getFxy().startsWith("0-7-62"))
										continue;
									
									isValueTableRecordNull = false; 
									int size = list.size() / span;
									for(int p = 0; p < size; p ++){
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
//											if(fxy.startsWith(prefix)){
//												entityBean.setFxy(repeated);
//												fxy = repeated;
//											}
											if(column.equalsIgnoreCase("D_ELE_ID")){
											System.out.println();
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
													if(column.equalsIgnoreCase("D_SOURCE_ID")){
														value += "_" + simpleName;
													}
												}
												
												if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
													value = members.get("CCCC");
												if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
													value = members.get("TTAAii");
												if(column.equalsIgnoreCase("V_BBB"))
													value = members.get("BBB");
												if(column.equalsIgnoreCase("D_RYMDHM"))
													value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
												
												if(("D_RECORD_ID").equals(column.toUpperCase())||("D_ELE_ID").equals(column.toUpperCase())){//替换d_record_id与d_ele_id中站号为999999的情况
													int index1=value.toString().indexOf("_");
													int index2=value.toString().indexOf("_",index1+1);
													String valueV01301="999999";
													if(value.toString().substring(index1+1).startsWith("999999")){
														try{
															Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
															Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
															Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));

//															value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
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
												
												keyBuffer.append(",`").append(column).append("`");
												valueBuffer.append(",'").append(value.toString().trim()).append("'");
												continue;
											}
											// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
											value = calExpressionValue(obj, members, entityMap, entityBean);
											if(tableName.toLowerCase().startsWith("ocen_tsc_glb_mul_tab") && helpMap.containsKey(column)){ // 要素表
												continue;
											}
											//测站
											if(column.toUpperCase().equals("V01301") && value.toString().trim().startsWith("999999")){
												try{
													Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
													Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
													Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));

//													value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
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
											if(column.equalsIgnoreCase("D_RYMDHM"))
												value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
											
											keyBuffer.append(",`").append(column).append("`");
											this.appendValue(valueBuffer, datatype, value.toString().trim());
										}
										String sql = "insert into " + tableName //
												+ " (" //
												+ keyBuffer.toString().substring(1) //
												+ ") values ("//
												+ valueBuffer.toString().substring(1) //
												+ ")";
			//							System.out.println(sql);
	//									sqList.add(sql);
										for(int c = 0; c < span; c++)
											list.remove(0);
										
										try{
											statement.execute(sql);
											infoLogger.info("成功插入一条记录，" + tableName);
										}catch (Exception e) {
											infoLogger.error("插入失败：" + fileName + "  "+ sql + "\n" + e.getMessage());
										}
									}// end for
//									repteats ++;
//									repeated = prefix + String.valueOf(repteats);
//									if(tableSection.equals("C.0001.0018.R001"))
//										break;
								} // end while
							}// end for
						}
						else{
							if(simpleName.toUpperCase().indexOf("IOBX06LFVW") >= 0 || simpleName.toUpperCase().indexOf("IOBX10LFVW") >= 0){
								if(simpleName.toUpperCase().contains("IOBX06LFVW")){
									entityMap.putAll(orMapList.get("IOBX06LFVW"));
								}
								else if(simpleName.toUpperCase().contains("IOBX10LFVW")){
									entityMap.putAll(orMapList.get("IOBX10LFVW"));
								}
								isValueTableRecordNull = false;
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
												value += "_" + simpleName;
											}
										}
										
										if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
											value = members.get("CCCC");
										if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
											value = members.get("TTAAii");
										if(column.equalsIgnoreCase("V_BBB"))
											value = members.get("BBB");
										if(column.equalsIgnoreCase("D_RYMDHM"))
											value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
										
										if(("D_RECORD_ID").equals(column.toUpperCase())||("D_ELE_ID").equals(column.toUpperCase())){//替换d_record_id与d_ele_id中站号为999999的情况
											int index1=value.toString().indexOf("_");
											int index2=value.toString().indexOf("_",index1+1);
											String valueV01301="999999";
											if(value.toString().substring(index1+1).startsWith("999999")){
												try{
													Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
													Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
													Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));

//													value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
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
										
										keyBuffer.append(",`").append(column).append("`");
										valueBuffer.append(",'").append(value.toString().trim()).append("'");
										continue;
									}
									// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
									value = calExpressionValue(obj, members, entityMap, entityBean);
									if(tableName.toLowerCase().startsWith("ocen_tsc_glb_mul_tab") && helpMap.containsKey(column)){ // 要素表
										continue;
									}
									//测站
									if(column.toUpperCase().equals("V01301") && value.toString().trim().startsWith("999999")){
										try{
											Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
											Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
											Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));

//											value = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
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
									if(column.equalsIgnoreCase("D_RYMDHM"))
										value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
									
									keyBuffer.append(",`").append(column).append("`");
									this.appendValue(valueBuffer, datatype, value.toString().trim());
								}
								String sql = "insert into " + tableName //
										+ " (" //
										+ keyBuffer.toString().substring(1) //
										+ ") values ("//
										+ valueBuffer.toString().substring(1) //
										+ ")";
								try{
									statement.execute(sql);
									infoLogger.info("成功插入一条记录，" + tableName);
								}catch (Exception e) {
									infoLogger.error("插入失败：" + fileName + "  "+ sql + "\n" + e.getMessage());
								}
							}
						}
					}
					// 除了要素表之外的表
					else{
						if(isValueTableRecordNull == true && tableName.toLowerCase().startsWith("ocen_tsc_glb_mul"))
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
//							if("V22380".equals(column)||"V22381_061".equals(column)||"V22073".equals(column)){
//								System.out.println(2228);
//							}
							//有效波高，有两个fxy可以获取
							if(fxy.equals("1-1-0-1") &&((List<BufrBean>)members.get("1-1-0-1") == null || ((List<BufrBean>)members.get("1-1-0-1")).size() == 0)){
								entityBean.setFxy("1-1-0-2");
								fxy = "1-1-0-2";
							}
							
							String defaultValue = entityBean.getDefaultValue(); // 缺省值
							Object obj = members.get(fxy); // 根据fxy获取每一行数据
							if(column.equals("V11001")){
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
										value = calExpressionValue(obj, members, entityMap, entityBean);
									}
								} else {
									value = defaultValue;
									if(column.equalsIgnoreCase("D_SOURCE_ID")){
										value += "_" + simpleName;
									}
								}
								// 水温可能从两个fxy取值
								if(column.toUpperCase().equals("V22049") && value.toString().trim().startsWith("999999")){//
									obj = members.get("0-22-43-0");
									if(obj != null)
										value = calExpressionValue(obj, members, entityMap, entityBean);
								}
								if(column.toUpperCase().equals("V11001")&&members.get("0-11-2-0")!=null){//计算风向
									double V11002D= Double.valueOf(((BufrBean)members.get("0-11-2-0")).getValue().toString());//取风速的值
									if(V11002D<=0.2){
										value=999017;
									}else if( V11002D >0.2 && Double.valueOf(value.toString())==0){
										value=999997;
									}
								}
								
								if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
									value = members.get("CCCC");
								if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
									value = members.get("TTAAii");
								if(column.equalsIgnoreCase("V_BBB"))
									value = members.get("BBB");
								if(column.equalsIgnoreCase("D_RYMDHM"))
									value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
								
								if(("D_RECORD_ID").equals(column.toUpperCase())||("D_ELE_ID").equals(column.toUpperCase())){//替换d_record_id与d_ele_id中站号为999999的情况
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
								else if(column.equals("D_DATA_ID"))
									di.setDATA_TYPE(value.toString());
								else if(column.equals("V_BBB"))
									di.setDATA_UPDATE_FLAG(value.toString());
								continue;
							}
		
							// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//							System.err.println(column);
							value = calExpressionValue(obj, members, entityMap, entityBean);
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
							
							if(column.toUpperCase().equals("V11001")&&members.get("0-11-2-0")!=null){//计算风向
								double V11002D= Double.valueOf(((BufrBean)members.get("0-11-2-0")).getValue().toString());//取风速的值
								if(V11002D<=0.2){
									value=999017;
								}else if( V11002D >0.2 && Double.valueOf(value.toString())==0){
									value=999997;
								}
							}
							if(column.equalsIgnoreCase("D_RYMDHM"))
								value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
							
							keyBuffer.append(",`").append(column).append("`");
							this.appendValue(valueBuffer, datatype, value.toString().trim());
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
							else if(column.equals("D_DATA_ID"))
								di.setDATA_TYPE(value.toString());
							else if(column.equals("V_BBB"))
								di.setDATA_UPDATE_FLAG(value.toString());
						}// end while
						di.setFILE_SIZE(String.valueOf(file.length()));
						di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm"));
						di.setPROCESS_END_TIME(TimeUtil.getSysTime());
						di.setRECORD_TIME(TimeUtil.getSysTime());
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
//						System.out.println(sql);
						try {
							statement.execute(sql);
							infoLogger.info("成功插入一条记录" + "  " + tableName);
						} catch (MySQLIntegrityConstraintViolationException e) {
							infoLogger.error("主键冲突:" + sql + "\n"  + e.getMessage());
							Integer conflictNum = tmpRecord.get(PRIMARY_KEY_CONFLICT_KEY);
							conflictNum = (conflictNum == null ? 1 : (conflictNum +=1));
							tmpRecord.put(PRIMARY_KEY_CONFLICT_KEY, conflictNum);
							di.setPROCESS_STATE("0");
						}catch (Exception e) {
							infoLogger.error("插入失败：" + sql + "\n" + e.getMessage());
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
			if(statement != null){
				try {
					statement.close();
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
							Double area = (Double.parseDouble(((BufrBean) members.get("0-1-3-0")).getValue().toString()));
							Double subArea = (Double.parseDouble(((BufrBean) members.get("0-1-20-0")).getValue().toString()));
							Double buoy_platformID = (Double.parseDouble(((BufrBean) members.get("0-1-5-0")).getValue().toString()));

							//columnValue = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
							columnValue = String.format("%01d", area.intValue()) + String.format("%01d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
							
						}catch (Exception e) {
							try{
								value = ((BufrBean)members.get("0-1-11-0")).getValue().toString(); // C.0001.0014.R001
							}catch (Exception e1) {
								infoLogger.error("台站号解析失败！\n");
							}
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



}
