package cma.cimiss2.dpc.indb.bufrsate.insat.service;

import java.io.File;
import java.math.BigDecimal;
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
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alicloud.openservices.tablestore.ClientException;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrDecoder;
import com.hitec.bufr.util.StringUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class OTSService implements BufrService {
	int batchSize = 500;
	public static BlockingQueue<StatDi> diQueues = null;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> DI = new ArrayList<StatDi>();

	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrDecoder bufrDecoder = new BufrDecoder();
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
				return this.decode(bufrDecoder, name, tableSection, tables);
			} else {
				infoLogger.error("\n Read file error: " + name);
				String event_type = EIEventType.OP_FILE_ERROR.getCode();
				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
				if (ei == null) {
					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
				} else {
					if (StartConfig.isSendEi()) {
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

	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
		BufrDecoder bufrDecoder = new BufrDecoder();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
			return this.decode(bufrDecoder, fileName, tableSection, tables);
		} else {
			infoLogger.error("\n Read file error: " + fileName);
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if (ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: " + event_type);
			} else {
				if (StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.bufrsate.insat.service.InsatServiceImpl");
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

	public boolean decode(BufrDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		File file = new File(fileName);

		for (Map<String, Object> members : bufrDecoder.getParseList()) {

			for (String tableName : tables) {
				StatDi di = new StatDi();
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setTT("BUFR");
				di.setDATA_TYPE_1(StartConfig.ctsCode());
				di.setFILE_NAME_N(fileName);
				di.setFILE_NAME_O(fileName);
				XmlBean xmlBean = configMap.get(tableName.trim());
				Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
				// 用于存储入库的字段
				StringBuffer keyBuffer = new StringBuffer();
				// 用于存储入库的字段值
				StringBuffer valueBuffer = new StringBuffer();
				// 遍历配置信息
				Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
				// 获取每个字段的值
				Map<String, Object> row = new HashMap<>();
				while (it.hasNext()) {
					Entry<String, EntityBean> next = it.next();
					String column = next.getKey(); // 配置文件配置的字段名称
					EntityBean entityBean = next.getValue(); // 配置的每个字段属性
					String datatype = entityBean.getDatatype(); // 字段类型
					String expression = entityBean.getExpression(); // 此字段的表达式
					String fxy = entityBean.getFxy(); // 解码时存储数据的key
					String defaultValue = entityBean.getDefaultValue(); // 缺省值
//						if(column.equals("V02154")){
//							System.out.println("");
//						}
					Object obj = members.get(fxy); // 根据fxy获取每一行数据
					// 如果获取的对象为null
					Object value = null;
					if (obj == null) {
						if (fxy.equals("1-2-2-0") || fxy.equals("1-2-5-0")) {
							value = defaultValue;
						} else {
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
						}
						if (column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
							value = members.get("CCCC");
						if (column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
							value = members.get("TTAAii");
						if (column.equalsIgnoreCase("V_BBB"))
							value = members.get("BBB");
						if(column.equalsIgnoreCase("D_SOURCE_ID"))
							value =value+"_"+file.getName();

						row.put(column, value);

						if (column.equals("D_DATETIME"))
							di.setDATA_TIME(String.valueOf(value));
						if (column.equals("V01301"))
							di.setIIiii(value.toString());
						if (column.equals("V05001"))
							di.setLATITUDE(value.toString());
						if (column.equals("V06001"))
							di.setLONGTITUDE(value.toString());
						if (column.equals("D_DATA_ID"))
							di.setDATA_TYPE(value.toString());
						continue;
					}
//						else if(fxy.equals("1-2-2-0") || fxy.equals("1-2-5-0")){
//							value = getCycleValue(expression, obj, entityBean);
//						}
					else {
						// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
						value = calExpressionValue(obj, members, entityMap, entityBean);
					}
//						//降水量的处理
//						if((column.equalsIgnoreCase("V13019") || column.equalsIgnoreCase("V13020")
//								|| column.equalsIgnoreCase("V13021") || column.equalsIgnoreCase("V13022")
//								|| column.equalsIgnoreCase("V13023") || column.equalsIgnoreCase("V13011"))&& value.toString().startsWith("-0.1"))
//							value = "999990";
//						if(column.equalsIgnoreCase("V13013") && value.toString().startsWith("-0.02")){
//							value = "999990";
//						}
//						// 风向的处理
//						try{
//							if((column.equalsIgnoreCase("V11290") || column.equalsIgnoreCase("V11292")
//									|| column.equalsIgnoreCase("V11296") || column.equalsIgnoreCase("V11201")
//									|| column.equalsIgnoreCase("V11211") || column.equalsIgnoreCase("V11503_06")
//									|| column.equalsIgnoreCase("V11503_12") || column.equalsIgnoreCase("V11001")
//									|| column.equalsIgnoreCase("V11046") || column.equalsIgnoreCase("V11288") 
//									|| column.equalsIgnoreCase("V11043")) && Double.parseDouble(value.toString()) == 0){
//								value = "999017";
//							}
//						}catch (Exception e) {
//							System.err.println("风向值不为数值型，转换错误！");
//						}
//						try{
//							if(datatype.equalsIgnoreCase("double") && Double.parseDouble(value.toString()) > 999999)
//								value = defaultValue;
//						}catch (Exception e) {
//							infoLogger.error("字段:" + column + "是double类型，数值超过 999999，存入999999！");
//						}
					if (column.equalsIgnoreCase("V01015") && value.toString().contains("'")) {
						String tmp = value.toString();
						tmp = tmp.replaceAll("'", "''");
						value = tmp;
					}

					row.put(column, value);

					if (column.equals("D_DATETIME"))
						di.setDATA_TIME(String.valueOf(value));
					if (column.equals("V01301"))
						di.setIIiii(value.toString());
					if (column.equals("V05001"))
						di.setLATITUDE(value.toString());
					if (column.equals("V06001"))
						di.setLONGTITUDE(value.toString());
					if (column.equals("D_DATA_ID"))
						di.setDATA_TYPE(value.toString());

				} // end while

				file.length();
				di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setFILE_SIZE(String.valueOf((new File(fileName)).length()));
				di.setBUSINESS_STATE("1");
				di.setPROCESS_STATE("1");
				DI.add(di);

				// 入OTS库
				try {
					OTSDbHelper.getInstance().insert(tableName, row);
					System.out.println("!!!成功");
					infoLogger.info("Insert one line successfully!\n");
					diQueues.offer(di);
				} catch (Exception e) {
					e.printStackTrace();
					di.setPROCESS_STATE("0");
					diQueues.offer(di);
					infoLogger.error(row + "\n");
					infoLogger.error(e.getMessage() + "\n");
					if (e.getClass() == ClientException.class) {
						infoLogger.error("DataBase connection error!\n");
						return false;
					}
				}
			}
		} // for
		diQueues.clear();
		return true;
	}

	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap,
			EntityBean entityBean) {
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
				String cExpReg = StringUtil.string2Regex(cExp);
				if (obj2 == null) {
					// 如果表达式获取字段值为空，直接返回默认值
					expression = expression.replaceFirst(cExpReg, entityBean.getDefaultValue());
//						return entityBean.getDefaultValue();
				} else {
					// 如果不为空，获取其对应的值
					String columnValue = getColumnValue(obj2, ebean).toString();

//						if (columnValue.equals(ebean.getDefaultValue())) {
//							expression = expression.replaceFirst(cExpReg, columnValue);
//						}
					if (columnValue.startsWith("999999.0") && ebean.getDefaultValue() != null)
						columnValue = ebean.getDefaultValue();
					if (ebean.getDatatype().equals("int")) {
						int idx = columnValue.indexOf(".");
						if (idx >= 1)
							columnValue = columnValue.substring(0, idx);
					}
//						System.out.println(expression + "====" + cExp);

					String afterText = expression.substring(expression.indexOf(cExp));
//						String cExpReg = StringUtil.string2Regex(cExp);
					afterText = afterText.replaceFirst(cExpReg, "");

					if (afterText.startsWith(StringUtil.SPLIT_FLAG)
							|| afterText.startsWith(StringUtil.SUBSTRING_FLAG)) {
						// 存储整串表达式代表的值,list最后一个值即为最终结果
						List<String> res = new ArrayList<String>();
						// 用于存储一整串substring和split表达式，最后替换直值用
						StringBuffer regSBuffer = new StringBuffer();

						StringUtil.formatString(columnValue, afterText, res, regSBuffer);
						String resByStation = res.get(res.size() - 1); // 最终结果
						expression = expression.replaceFirst(StringUtil.string2Regex(cExp + regSBuffer.toString()),
								resByStation);
					} else {
						String sp[] = columnValue.split("[+\\*]");
						sp[0] = sp[0].replaceAll("\\$|\\{", "");
						try {
							sp[0] = String.format("%d", Integer.parseInt(sp[0]));
						} catch (Exception e1) {
							try {
								sp[0] = String.format("%.3f", Double.parseDouble(sp[0]));
							} catch (Exception e2) {
								System.out.println(expression);
							}
						}
						expression = expression.replaceFirst(cExpReg, sp[0]);
					}
				}
			}

			// 替换所有默认表达式
//				System.out.println(expression);
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
			// 转换字符串为ascii码
			expression = StringUtil.formatStringAsciiExp(expression);

		}

		value = StringUtil.calculteExp(expression);
		if (value.equalsIgnoreCase("null"))
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
				if (bufr.getQcValues() != null)
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
						if (entityBean.getDatatype().equalsIgnoreCase("double")) {// double类型的字段结果可能是科学计数法，要将其变为普通的数字
							BigDecimal big = new BigDecimal(v);
							bufr.setValue(big.toPlainString());
						}
						ts.add(bufr.getValue().toString());
						value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
					} else {
						infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
					}
				} else {
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
							if (bufrBean.getQcValues() != null)
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

}
