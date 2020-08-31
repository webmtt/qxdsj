package cma.cimiss2.dpc.indb.cawn.dc_cawn_gas.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
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
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrSurfCawnDecoder;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

// TODO: Auto-generated Javadoc
/**
 * The Class BufrServiceImpl.
 */
public class BufrServiceImpl implements BufrService {
	
	/** The pro map. */
	static Map<String, Object> proMap = StationInfo.getProMap();
	
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
		String tableSection = "G.0016.0002.R001";
		tableList.add("CAWN_CHN_PMM_BUFR_TAB");
		String fileName = "/Users/zhangliang/Documents/test/Z_CAWN_I_58549_20191003090000_O_FLD.bin";
		BufrServiceImpl bufrServiceImpl = new BufrServiceImpl();
		boolean decode = bufrServiceImpl.decode(fileName, tableSection, tableList);
		if(decode){
			System.out.println("入库成功！");
		}else
			System.err.println("入库失败！");
//		bufrDecoder.bufrDecoder("D:\\A_ISAC01VHHH200300_C_BABJ_20180920031410_90199.bin");
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
		BufrServiceImpl.diQueues = diQueue;
	}
	
	/* (non-Javadoc)
	 * @see cma.cimiss2.dpc.indb.surf.dc_surf_global_h_bufr.service.BufrService#decode(java.lang.String, byte[], java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrSurfCawnDecoder bufrDecoder = new BufrSurfCawnDecoder();
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
						ei.setKObject("cma.cimiss2.dpc.indb.surf.bufr.service.BufrServiceImpl");
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
		BufrSurfCawnDecoder bufrDecoder = new BufrSurfCawnDecoder();
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
					ei.setKObject("cma.cimiss2.dpc.indb.surf.bufr.service.BufrServiceImpl");
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
	/**
	 * Decode.
	 *
	 * @param bufrDecoder the bufr decoder
	 * @param fileName the file name
	 * @param tableSection the table section
	 * @param tables the tables
	 * @return true, if successful
	 */
	public boolean decode(BufrSurfCawnDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		// 获取数据库连接
				DruidPooledConnection connection = null;
				Statement statement = null;
				File fileN=new File(fileName);
//				int  idx=fileName.lastIndexOf(".");
//				String prename=fileName.substring(0, idx-1);
//				String endstr =fileName.substring(idx-3,idx);
				
				Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
				try {
//					if(diQueues == null)
//						diQueues = new LinkedBlockingQueue<StatDi>();
					connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
					statement = connection.createStatement();
					Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
					if(connection.getAutoCommit() == true)
						connection.setAutoCommit(false);
//					statement.execute("select last_txc_xid()");
					boolean dateTimeOK=true;
					int num=1;
					for(Map<String, Object> members: bufrDecoder.getParseList()){
						dateTimeOK=true;
						for (String tableName : tables) {
							dateTimeOK=true;
							String d_record_id="999999";
							String d_datetime="999999";
							tableName = tableName.trim();
							System.out.println("tableName:"+tableName);
							XmlBean xmlBean = configMap.get(tableName);
							Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
							
							int recileNum=0;
							if(tableName.equalsIgnoreCase("CAWN_CHN_PMM_BUFR_TAB")) {
								recileNum=32;
							}
							
							// 用于存储入库的字段
							StringBuffer keyBuffer = new StringBuffer();
							// 用于存储入库的字段值
							StringBuffer valueBuffer = new StringBuffer();
							// 遍历配置信息
							Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
							Map<String, Map<String, EntityBean>> groupMaps = xmlBean.getGroupMap();
//							for(int i=0;i<recileNum;i++) {
								if(groupMaps != null && groupMaps.size() > 0) {
									Set groupKey = groupMaps.keySet();
									Iterator<String> groupItor = null;
									for(groupItor = groupKey.iterator(); groupItor.hasNext(); ) {
										String key = groupItor.next();
										int span = Integer.parseInt(key.split("_")[1]);
										String repeated = key.split("_")[0];
										Map<String, EntityBean> groupMap = groupMaps.get(key);
										entityMap.putAll(groupMap); // 后面覆盖前面的
										System.out.println(entityMap.size());
										it = entityMap.entrySet().iterator();
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
												
												if(("D_RECORD_ID").equals(column)){
													System.out.println(value+"1");
													d_record_id=value.toString();
												}
												if(("D_DATETIME").equals(column)){
													d_datetime=value.toString();
													try{
														SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
														Date dataTime=simpleDateFormat.parse(value.toString());
//											if(!TimeCheckUtil.checkTime(dataTime)){
//												dateTimeOK = false;
//												infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
//												break;
//											}
													}catch (Exception e) {
														dateTimeOK = false;
														infoLogger.error("资料时间错误！  " + fileName);
														break;
													}
												}
												if("V_CAWN_TYPE".equals(column)) {
													if(num%3==1) {
														value="PM10";
													}else if(num%3==2) {
														value="PM2.5";
													}else {
														value="PM1";
													}
													
												}
												keyBuffer.append(",`").append(column).append("`");
												valueBuffer.append(",'").append(value.toString().trim()).append("'");
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
//										System.err.println(column);
											value = calExpressionValue(obj, members, entityMap, entityBean);
											if("V14032".equals(column)&&!(value.toString().startsWith("999999"))){
												value=Double.parseDouble(value.toString());
											}	
											
											if(("D_RECORD_ID").equals(column)){
												System.out.println(value+"2");
												d_record_id=value.toString();
											}
											if(("D_DATETIME").equals(column)){
												d_datetime=value.toString();
												try{
													SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
													Date dataTime=simpleDateFormat.parse(value.toString());
//										if(!TimeCheckUtil.checkTime(dataTime)){
//											dateTimeOK = false;
//											infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
//											break;
//										}
												}catch (Exception e) {
													dateTimeOK = false;
													infoLogger.error("资料时间错误！  " + fileName);
													break;
												}
											}
											if("V_CAWN_TYPE".equals(column)) {
												if(num%3==1) {
													value="PM10";
												}else if(num%3==2) {
													value="PM2.5";
												}else {
													value="PM1";
												}
											}
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
											if(column.equals("D_DATA_ID"))
												di.setDATA_TYPE(value.toString());
											else if(column.equals("V_BBB"))
												di.setDATA_UPDATE_FLAG(value.toString());
//								infoLogger.info("\n connection: fileName  " + fxy  + "-------" + value);
											
											
										}// end while
										Date recv=new Date();
										di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
										di.setPROCESS_END_TIME(TimeUtil.getSysTime());
										di.setRECORD_TIME(TimeUtil.getSysTime());
										di.setFILE_SIZE(String.valueOf(fileN.length()));
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
										
//							String selectSQL = "Select V_BBB from " + tableName + " where  D_datetime=" + "'" +d_datetime+ "'"+" and  D_Record_id = " + "'" + d_record_id +  "'" +  " limit 1";
										System.out.println("sql:"+sql);
//							ResultSet selectRst = statement.executeQuery(selectSQL);
//							String BBB = null;
//							if(selectRst.next()){
//								BBB = selectRst.getString(1);
//							}
//							
//							if(selectRst != null){
//								try{
//									selectRst.close();
//									selectRst = null;
//								}catch (Exception e) {
////									e.printStackTrace();
//									infoLogger.error("Close ResultSet error! ");
//								}
//							}
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
										
									}
								}
//							}
					} // end tables loop
//					if(DI != null){
//						for (int j = 0; j < DI.size(); j++) {
//							diQueues.offer(DI.get(j));
//						}
//						DI.clear();
//					}
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
	 * Gets the cycle value.
	 *
	 * @param expression the expression
	 * @param obj the obj
	 * @param entityBean the entity bean
	 * @return the cycle value
	 */
	private Object getCycleValue(String expression, Object obj, EntityBean entityBean) {
		List<BufrBean> bufrList = (List<BufrBean>) obj;
		int index = Integer.parseInt(expression);
		Object value = entityBean.getDefaultValue();
		for(int i = 0; i < bufrList.size(); i += 2){ // 拿到时间周期数
			int cycle = (int)Double.parseDouble(bufrList.get(i).getValue().toString());
			if(index == cycle){
				if(!entityBean.isQc())
					value = bufrList.get(i + 1).getValue();
				else
					if(bufrList.get(i + 1).getQcValues() != null) // 省级质控码
						value = bufrList.get(i + 1).getQcValues().get(0);
				break;
			}
		}
		return value;
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
				String fxy =null;
				EntityBean ebean =null;
				if(expCol.equalsIgnoreCase("V04003")) {}
				else {
					ebean = entityMap.get(expCol);
					fxy = ebean.getFxy();
				}
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
