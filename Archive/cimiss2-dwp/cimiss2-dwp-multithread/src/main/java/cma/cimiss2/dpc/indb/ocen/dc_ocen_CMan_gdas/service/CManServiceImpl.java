package cma.cimiss2.dpc.indb.ocen.dc_ocen_CMan_gdas.service;

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
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.CManBufrDecoder;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2020年1月8日 下午2:59:49   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class CManServiceImpl implements BufrService{
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The di. */
	private static List<StatDi> DI = new ArrayList<StatDi>();
	
	/** The ymdhm. */
	SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	/** The Constant COLUMN_EXP_REGEX. */
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	
	/** The Constant THIS_VALUE_EXP. */
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		String tableSection = "A.0001.0050.R001";
		BufrConfig.init();
		tableList = BufrConfig.getTableMap().get(tableSection);
//		String fileName = "D:\\HUAXIN\\Gdas-ship\\W_NAFP_C_KWBC_20191231120000_P_gdas.t12z.sfcshp.tm00.bufr_d.nr.bin";
		String fileName = "D:\\HUAXIN\\Gdas-ship\\W_NAFP_C_KWBC_20191231180000_P_gdas.t18z.sfcshp.tm00.bufr_d.nr.bin";
		CManServiceImpl oceanServiceImpl = new CManServiceImpl();
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
		CManServiceImpl.diQueues = diQueues;
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
	 * @see cma.cimiss2.dpc.indb.ocen.dc_ocen_CMan_gdas.service.BufrService#decode(java.lang.String, java.lang.String, java.util.List)
	 */
	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
		CManBufrDecoder bufrDecoder = new CManBufrDecoder();
		boolean isRead = bufrDecoder.decoder(fileName);
		if (isRead) {
			ReportInfoToDb(bufrDecoder, tableSection, fileName);
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
					ei.setKObject("cma.cimiss2.dpc.indb.ocen.dc_ocen_CMan_gdas.service.CManServiceImpl");
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
	public boolean decode(CManBufrDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);  // 四级编码
		boolean dateTimeOK = true;
		int insertCNT = 0;
		int failedCNT = 0;
		try {
			File file = new File(fileName);
			if(CManServiceImpl.getDiQueues() == null)
				diQueues = new LinkedBlockingQueue<StatDi>();
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			statement = connection.createStatement();
			
			// 缺省是 手动提交
			for(Map<String, Object> members: bufrDecoder.getParseList()){
				double latitude = 999999;
				double longitude = 999999;
				double [] cal = calcuLatAndLon(members,fileName,latitude,longitude,tableSection);
				latitude=cal[0];
				longitude=cal[1];
				
				for (String tableName : tables) {
					dateTimeOK = true;
					tableName = tableName.trim();
					XmlBean xmlBean = configMap.get(tableName); // 获取xml配置
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					// 用于存储入库的字段
					StringBuffer keyBuffer = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer = new StringBuffer();
					// 遍历配置信息
					Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
					StatDi di = new StatDi();
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setDATA_TYPE_1(tableSection);
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
						if(column.equals("D_DATETIME")){
							System.out.print("");
						}
						if (obj == null) {
							// 如果表达式不为空说明此值需要根据表达式进行计算value
							if (StringUtils.isNotBlank(expression)) {
								// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
								Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
								if (em.find()) {
									value = defaultValue;
								} else {// 如果表达式存在，且其中没有以index取值
									value = calExpressionValue(obj, members, entityMap, entityBean, latitude, longitude);
								}
							} else {
								value = defaultValue;
								if(column.equalsIgnoreCase("D_SOURCE_ID")){
									value += "_" + file.getName();
								}
							}
							if(column.equalsIgnoreCase("D_RYMDHM"))
								value=TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
							
							if(column.equalsIgnoreCase("D_RECORD_ID") 
									&& (tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_GDAS_TAB"))){
								// 不入D_record_id
							}
							else{
								keyBuffer.append(",`").append(column).append("`");
								valueBuffer.append(",'").append(value.toString().trim()).append("'");
							}
							
							if(column.equals("D_DATETIME")){
								try{
									SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								    Date dataTime=simpleDateFormat.parse(value.toString());
//									if(!TimeCheckUtil.checkTime(dataTime)){
//										dateTimeOK = false;
//										infoLogger.info("DataTime out of range:"+dataTime+" "+fileName);
//										break;
//									}
								}catch (Exception e) {
									dateTimeOK = false;
									infoLogger.error("资料时间错误！  " + fileName);
									break;
								}
							}
							
							
							if(column.equals("D_DATETIME"))
								di.setDATA_TIME(String.valueOf(value));
							if(column.equals("V01301"))
								di.setIIiii(value.toString());
							if(column.equals("V05001"))
								di.setLATITUDE(String.valueOf(latitude) );
							if(column.equals("V06001"))
								di.setLONGTITUDE(String.valueOf(longitude));
							if(column.equals("D_DATA_ID"))
								di.setDATA_TYPE(value.toString());
							continue;
						} // end if 
						// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//						System.err.println(column);
						value = calExpressionValue(obj, members, entityMap, entityBean,latitude,longitude);
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
						
						if(column.equals("V05001")){
							value=latitude;
						}
						if(column.equals("V06001")){
							value=longitude;
						}
						// chy  修改
						if(column.equalsIgnoreCase("D_RECORD_ID") 
								&& (tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_GDAS_TAB") || tableName.equalsIgnoreCase("OCEN_SHB_GLB_MUL_GDAS_ORI_TAB"))){
							// 不入D_record_id
							
						}else{
							keyBuffer.append(",`").append(column).append("`");
							this.appendValue(valueBuffer, datatype, value.toString().trim());
						}
						if(column.equals("D_DATETIME"))
							di.setDATA_TIME(String.valueOf(value));
						if(column.equals("V01301"))
							di.setIIiii(value.toString());
						if(column.equals("V05001"))
							di.setLATITUDE(String.valueOf(latitude));
						if(column.equals("V06001"))
							di.setLONGTITUDE(String.valueOf(longitude));
						if(column.equals("D_DATA_ID"))
							di.setDATA_TYPE(value.toString());
					}// end while
					di.setFILE_SIZE(String.valueOf(file.length()));
					di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setFILE_SIZE(String.valueOf((new File(fileName)).length()));
					di.setBUSINESS_STATE("1");
					di.setPROCESS_STATE("1");
					DI.add(di);
					
					if(dateTimeOK == false)
						continue;
					
					// 键表入库
					String sql = "insert into " + tableName //
							+ " (" //
							+ keyBuffer.toString().substring(1) //
							+ ") values ("//
							+ valueBuffer.toString().substring(1) //
							+ ")";
//					System.out.println(sql);
					try{
						statement.execute(sql);
						connection.commit();
						infoLogger.info("成功插入一条记录: " + tableName+" 文件名："+fileName+"sql:"+sql);
						insertCNT ++;
					}catch (Exception e) {
						infoLogger.error("插入失败：" + fileName + "  "+ sql + "\n" + e.getMessage());
						failedCNT ++;
					}
					
				}// end table for loop
				
				if(DI != null){
					for (int j = 0; j < DI.size(); j++) {
						diQueues.offer(DI.get(j));
					}
					DI.clear();
				}
			} // end parselist for loop
			infoLogger.info("Insert record#: " + insertCNT + ", Failed record#: " + failedCNT);
			System.out.println("Insert record#: " + insertCNT + ", Failed record#: " + failedCNT);
			return true;
		}catch (Exception e) {
			infoLogger.error(e.getMessage());
			e.printStackTrace();
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
//						System.out.println(entityBean.getColumn() + ", " + expression + ", " + ts.toArray() + "-----------------");
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

	/**
	 * @Title: calcuLatAndLon   
	 * @Description:计算经纬度 
	 * @param parseMap
	 * @param filename
	 * @param latitude
	 * @param longitude
	 * @param sta
	 * @param ctsCode
	 * @return double[]      
	 * @throws：
	 */
	 double [] calcuLatAndLon(Map<String, Object> parseMap,String filename,double latitude,double longitude,String ctsCode){
		  Map<String, Object> proMap = StationInfo.getProMap();
		  String station ="999999";
		  if(parseMap.get("0-1-10-0") != null){
			  station = ((BufrBean)parseMap.get("0-1-10-0")).toString();
		  }
		  double[] LatAndLon={latitude,longitude}; 
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
	 /**
	  * @Title: sendEI   
	  * @Description: 发送EI  
	  * @param station void      
	  */
	 public void sendEI(String station){
			String event_type = EIEventType.STATION_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.ocen.dc_ocen_CMan_gdas.service.CManServiceImpl.java");
					ei.setKEvent("获取台站信息异常");
					ei.setKIndex("C-Man 台站："+station+"未能获取台站信息配置，无法获取经纬度");
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
	 
	 
    /**
     * @Title: ReportInfoToDb   
     * @Description: 全球地面BUFR公报入库
     * @param bufrDecoder
     * @param tableSection
     * @param fileName void      
     */
	public  void ReportInfoToDb(CManBufrDecoder bufrDecoder,String tableSection, String fileName) {
	 	int successOrNot=bufrDecoder.successOrNot;
		DruidPooledConnection report_connection = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
		File file = new File(fileName);
		String filename = file.getName();
		Date recv = new Date(file.lastModified());
		String ReportTableName=StartConfig.reportTable();
		if(ReportTableName == null || ReportTableName.equalsIgnoreCase("null") || ReportTableName.isEmpty())
			ReportTableName="SURF_WEA_GLB_BULL_BUFR_TAB";
		
		String sod_code = StartConfig.reportSodCode();
		if(sod_code == null || sod_code.equalsIgnoreCase("null") || sod_code.isEmpty())
			sod_code="A.0001.0025.S007";
		
		// chy 去掉 D_RECORD_ID
		String sql="insert into "+ReportTableName+"(D_RECORD_ID,D_DATA_ID,D_SOURCE_id,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V01301,V_BBB,V_CCCC,V_YYGGgg,V_TT,V_LEN,V_FILE_SIZE,V_BULLETIN_B,V_FILE_NAME,V_NUM,V01301_list,d_datetime_list,"
				+ "V_decode_flag) values ("
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?)";
		
		StringBuffer station_s=new StringBuffer();//存站号list集合
		StringBuffer datetime_s=new StringBuffer();//存站号list集合
//		| BUHD     | 035021 | BULLETIN BEING MONITORED (TTAAii)
//		| BULTIM   | 035022 | BULLETIN BEING MONITORED (YYGGgg) 
//		| BORG     | 035023 | BULLETIN BEING MONITORED (CCCC)
//		| BBB      | 035194 | BULLETIN BEING MONITORED (BBB)
		PreparedStatement statement = null;
		try {
			report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			statement = new LoggableStatement(report_connection, sql);
			String V_CCCC ="000";
			String V_BBB="000";
			String V_TT="999999";
			String V_YYGGgg="999999";
			// 手动提交
			if(successOrNot==1){//解码成功
				if(bufrDecoder.getParseList().size()>0){//解码有值
					V_CCCC ="000";
					V_BBB="000";
					V_TT="999999";
					V_YYGGgg="999999";
					for(Map<String, Object> members: bufrDecoder.getParseList()){
						//查站号
						String stafxy="0-1-10-0";
						String sta="999999";
						if(members.get(stafxy)!=null){
							sta=((BufrBean)(members.get(stafxy))).getValue().toString();
						}
						station_s.append(sta).append(",");
						V_CCCC = ((BufrBean)members.get("0-35-23-0")).getValue().toString();
						V_BBB=((BufrBean)members.get("0-35-194-0")).getValue().toString();
						V_TT=((BufrBean)members.get("0-35-21-0")).getValue().toString();
						V_YYGGgg=((BufrBean)members.get("0-35-22-0")).getValue().toString();
						//查datetime
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
					statement.setString(ii++, station_1 + "_" + simpleDateFormat.format(datetime1));// d_record_id
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
//							statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
					statement.setString(ii++,filename);//V_FILE_NAME 文件名
					statement.setInt(ii++, bufrDecoder.getParseList().size());//V_NUM 观测记录数
					statement.setString(ii++,stations);//V01301_list 观测记录站号列表
					statement.setString(ii++,datetimes);//d_datetime_list 观测记录时间列表
					statement.setInt(ii++,1);//V_decode_flag 解码是否成功标志
				}else{
					
				}
			}else if(successOrNot==2){//解码失败
				byte[] databyte=getContent(fileName);//报文二进制数组
				int ii = 1;
				statement.setString(ii++, "999999" + "_" + simpleDateFormat.format(new Date()));// d_record_id
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
				statement.setString(ii++, "999999" + "_" + simpleDateFormat.format(new Date()));// d_record_id
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
		
	@Override
	public boolean decode(String filename, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			CManBufrDecoder bufrDecoder = new CManBufrDecoder();
			boolean isRead = bufrDecoder.decoder(filename, dataBytes);
			if (isRead) {
				return this.decode(bufrDecoder, filename, tableSection, tables);
			} else {
				infoLogger.error("\n Read file error: "+filename);
				String event_type = EIEventType.OP_FILE_ERROR.getCode();
				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
				if(ei == null) {
					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
				}else {
					if(StartConfig.isSendEi()) {
						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
						ei.setKObject("cma.cimiss2.dpc.indb.ocen.dc_ocen_CMan_gdas.service.CManServiceImpl");
						ei.setKEvent("解码入库源文件异常：" + filename);
						ei.setKIndex("详细信息：" + filename);
						ei.setEVENT_EXT1(filename);
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
}