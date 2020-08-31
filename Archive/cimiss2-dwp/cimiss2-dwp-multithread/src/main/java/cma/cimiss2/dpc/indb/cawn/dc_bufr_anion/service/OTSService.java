package cma.cimiss2.dpc.indb.cawn.dc_bufr_anion.service;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrAnionDecoder;
import com.hitec.bufr.util.Calculator;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class OTSService implements BufrService{
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
		OTSService.diQueues = diQueue;
	}
	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {

		BufrAnionDecoder bufrDecoder = new BufrAnionDecoder();
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
					ei.setKObject("cma.cimiss2.dpc.indb.cawn.dc_bufr_anion.service.OTSService");
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
			return true;
		}
	

		
	}

	public boolean decode(BufrAnionDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		
		File file = new File(fileName);
		if(CawnAnionServiceImpl.getDiQueues() == null)
			diQueues = new LinkedBlockingQueue<StatDi>();
		
		List<String> tableList = new ArrayList<String>();
		tableList = BufrConfig.getTableMap().get(tableSection);
		
		for(Map<String, Object> members: bufrDecoder.getParseList()){
			for(String table : tableList){
				XmlBean xmlBean = configMap.get(table.trim()); // 这种资料只入一张表
				Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
				StatDi di = new StatDi();
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setDATA_TYPE_1(StartConfig.ctsCode());
				
				// 用于存储入库的字段
//				StringBuffer keyBuffer = new StringBuffer();
				// 用于存储入库的字段值
//				StringBuffer valueBuffer = new StringBuffer();
				
				Map<String, Object> row = new HashMap<>();
				for(String key: entityMap.keySet()){
					EntityBean entityBean = entityMap.get(key);
					String column = entityBean.getColumn();
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
						}
						if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
							value = members.get("CCCC");
						else if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
							value = members.get("TTAAii");
						else if(column.equalsIgnoreCase("V_BBB"))
							value = members.get("BBB");
						else if(column.equals("D_RYMDHM")){
							value = TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
						}
						else if(column.equalsIgnoreCase("D_SOURCE_ID")){
							value=value+"_"+file.getName();
						}
//						keyBuffer.append(",`").append(column).append("`");
//						valueBuffer.append(",'").append(value.toString().trim()).append("'");
						row.put(column, value.toString().trim());
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
						continue;
					}
					
					// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
					value = getValueByMapDirectly(obj, entityBean);
					
					if(column.equalsIgnoreCase("V01015") && value.toString().contains("'")){
						String tmp = value.toString();
						tmp = tmp.replaceAll("'", "''");
						value = tmp;
					}
					
//					keyBuffer.append(",`").append(column).append("`");
//					this.appendValue(valueBuffer, datatype, value.toString().trim());
					row.put(column, value.toString().trim());
					
					if(column.equals("D_DATETIME")){
						int idx01 = value.toString().lastIndexOf(":");
						di.setDATA_TIME(value.toString().substring(0, idx01));
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
				} // end for entityMap
				//台站号缺测 时，不入库
				if(di.getIIiii().startsWith("999999") || di.getDATA_TIME().startsWith("9999")){
					infoLogger.error("台站号缺测 或者 资料时间缺测！ " + fileName);
					break;  // 站号缺测，跳出for table loop，不入库
				}
				
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
				DI.add(di);
//				String sql = "insert into " + table //
//						+ " (" //
//						+ keyBuffer.toString().substring(1) //
//						+ ") values ("//
//						+ valueBuffer.toString().substring(1) //
//						+ ")";
//				System.out.println(sql);
				try {
					OTSDbHelper.getInstance().insert(table, row);
					infoLogger.error("成功插入一条记录！\n " + table + " " + fileName);
				} 
				catch (Exception e) {
					infoLogger.error("插入失败:\n " + fileName + "\n" + e.getMessage());
					di.setPROCESS_STATE("0");
					if(e.getClass() == ClientException.class) {
						infoLogger.error(fileName + ": CONNECTION_ERROR!");
						return false;
					}else {
						continue;
					}
				}					
			} // end for(String table : tableList)
			
		}// end for(Map<String, Object> members: bufrDecoder.getParseList())
	
		if(CawnAnionServiceImpl.getDiQueues() == null)
			diQueues = new LinkedBlockingQueue<StatDi>();
		for (int j = 0; j < DI.size(); j++) {
			diQueues.offer(DI.get(j));
		}
		if(DI != null)
			DI.clear();
		
		return true;
	}
	
	
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return true;
	}
	
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

	private Object getColumnValue(Object obj, EntityBean entityBean) throws IndexOutOfBoundsException {
		boolean isQc = entityBean.isQc();
		Object value = null;
		// 如果是bufrBean对象，直接强制转换
		if (obj instanceof BufrBean) {
			BufrBean bufr = (BufrBean) obj;
			if (isQc) {
				if(bufr.getQcValues() != null){
					int qc = bufr.getQcValues().get(0);
					if(qc == 15)
						value = 8;
					else
						value = qc > 9 ? 9 : qc;
				}
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
							if(bufrBean.getQcValues() != null){
								int qc = bufrBean.getQcValues().get(0);
								if(qc == 15)
									value2 = 8;
								else
									value2 = qc > 9 ? 9 : qc;
							}
								
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
	
	// 处理obj不为null的情况
		private Object getValueByMapDirectly(Object obj, EntityBean entityBean){
			Object value = "999998";
			String expression = entityBean.getExpression();
			if(expression == null || expression.equals("")){ // 当obj为String 或BufrBean时
				if(obj instanceof String){
					value = obj;
				}else if(obj instanceof BufrBean){
					BufrBean bean = (BufrBean)obj;
					if(!entityBean.isQc())
						value = bean.getValue();
					else {
						int qc = bean.getQcValues().get(0);
						if(qc == 15)
							value = 8;
						else
							value = qc > 9 ? 9 : qc;
					}
						
//					if(value.toString().startsWith("999999"))
//						value = entityBean.getDefaultValue();
				}
			}else{ // 当Obj为Map时
				Map<String, Object> map = (Map<String, Object>) obj;
				String exps[] = expression.trim().split("\\{|\\}");
				// 去掉数组中的空串
				List<String> tmp = new ArrayList<String>();
		        for(String str : exps){
		            if(str != null && str.length() != 0){
		                tmp.add(str);
		            }
		        }
		        exps = tmp.toArray(new String[0]);

		        for(int e = 0; e < exps.length; e++){
					String fxy = exps[e].trim();
					if(fxy.split("\\-").length >= 3){ //如果是描述符
						if(map.get(fxy) != null){
							if(map.get(fxy) instanceof String){
								value = map.get(fxy);
								expression = expression.replace(exps[e], value.toString());
								if(value.toString().startsWith("999999"))
//									return entityBean.getDefaultValue();
									return value;
							}else if(map.get(fxy) instanceof BufrBean){
								if(!entityBean.isQc()){
									value = ((BufrBean)map.get(fxy)).getValue();
									if(value.toString().startsWith("999999"))
//										return entityBean.getDefaultValue();
										return value;
								}
								else {
									int qc = ((BufrBean)map.get(fxy)).getQcValues().get(0);
									if(qc == 15)
										value = 8;
									else
										value = qc > 9 ? 9 : qc;
								}
								expression = expression.replace(exps[e], value.toString());
							}
						}
						else{ //未编报
							if(entityBean.isQc()){ 
								return entityBean.getDefaultValue();
							}
							else 
								return "999998";
						}
					}
				}
				if(!entityBean.isQc() && expression.split("\\+|-|\\*|\\/").length >= 2){
					expression = expression.replaceAll("\\{|\\}", "");
					value = Calculator.conversion(expression);
				}
			}
//			System.out.println(value);
			return value;
		}
	
	
}
