package cma.cimiss2.dpc.indb.storm.bufr.service;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrDecoder;
import com.hitec.bufr.util.StringUtil;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.storm.tools.BufrConfig;
import cma.cimiss2.dpc.indb.storm.tools.ConnectionPoolFactory;

public class BufrServiceImpl1 implements BufrService {
	
	public static final Logger Log = LoggerFactory.getLogger("loggerInfo");
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	BufrDecoder bufrDecoder = new BufrDecoder();
	
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		String tableSection = "BUFR_SURF_WEA_MUL_HOR";
		tableList.add("SURF_WEA_CBF_SSD_HOR_TAB");
		tableList.add("SURF_WEA_CBF_MUL_HOR_TAB");
		tableList.add("SURF_WEA_CBF_MUL_DAY_TAB");
		tableList.add("SURF_WEA_GBF_MUL_HOR_TAB");
//		tableList.add("SURF_WEA_CBF_MUL_MIN_MAIN_TAB");
//		tableList.add("SURF_WEA_CBF_PRE_MIN_TAB");
//		String fileName = "D:\\HUAXIN\\DataProcess\\A.0001.0044.R001.1\\Z_SURF_I_58402_20181026120000_O_AWS_FTM_PQC.BIN";
		String fileName = "D:\\DataTest\\ground_hor\\Z_SURF_I_52908_20190115000000_O_AWS_FTM_PQC.1.BIN";//Z_SURF_I_51708_20181013040000_O_AWS_FTM_PQC.BIN
		fileName = "D:\\DataTest\\ground_min\\Z_SURF_I_58369_20190117070000_O_AWS-MM_FTM_PQC.BIN";
		BufrServiceImpl1 bufrServiceImpl = new BufrServiceImpl1();
		boolean decode = false;
		List<StatDi> dis = new ArrayList<>();
		List<RestfulInfo> rests = new ArrayList<>();
		String CCCC = "";
		int useBBB=0;
		Date rcv_time = new Date((new File(fileName)).lastModified());
		decode = bufrServiceImpl.decode(rcv_time,fileName, CCCC, tableSection, tableList, dis, rests, useBBB);
		if(decode){
			System.out.println("入库成功！");
		}else
			System.err.println("入库失败！");
//		bufrDecoder.bufrDecoder("D:\\A_ISAC01VHHH200300_C_BABJ_20180920031410_90199.bin");
	}

	@Override
	public boolean decode(Date rcv_time, String name, String CCCC, byte[] dataBytes, String tableSection, List<String> tables, List<StatDi> DI, List<RestfulInfo> EI,int useBBB) {
		try {
			
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
				return this.decode(rcv_time, bufrDecoder, CCCC, name, tableSection, tables, DI, EI);
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean decode(Date rcv_time, String fileName,String CCCC, String tableSection, List<String> tables, List<StatDi> DI, List<RestfulInfo> EI,int useBBB) {
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
			return this.decode(rcv_time, bufrDecoder, CCCC, fileName, tableSection, tables, DI, EI);
		} else {
			return false;
		}
	}

	private final String PRIMARY_KEY_CONFLICT_KEY = "PRIMARY_KEY_CONFLICT";
	
	public boolean decode(Date rcv_time, BufrDecoder bufrDecoder, String CCCC, String fileName, String tableSection, List<String> tables, List<StatDi> DI, List<RestfulInfo> EI) {
		// 获取已解析的集合
		
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
		
		String temp_tableSection = tableSection;
		if(bufrDecoder.local_version == 3 && tableSection.equalsIgnoreCase("BUFR_SURF_WEA_MUL_HOR")){
			temp_tableSection = tableSection+"_V3";
		}
		Map<String, XmlBean> configMap = BufrConfig.get(temp_tableSection);
		
		int missedSunRecord = 0;
		boolean dateTimeOK = true;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
//			connection = MySqlPool.getConnection("mysql.rdb");
			statement = connection.createStatement();
			connection.setAutoCommit(true);
			for(Map<String, Object> members: bufrDecoder.getParseList()){
	    //		Map<String, Object> members = bufrDecoder.getMembers();
			
	//			if(tables == null){
	//				System.out.println("\n\n Tables is null!\n\n");
	//				tables = BufrConfig.getTableMap().get(tableSection);
	//			}
				dateTimeOK = true;
				try{
					List<BufrBean> windBufrBeans = (List<BufrBean>) members.get("1-3-2-0");
					if((double)windBufrBeans.get(0).getValue() != -10.0) {
						List<BufrBean> suBeans = windBufrBeans.subList(0, 3);
						List<BufrBean> suBeans2 = windBufrBeans.subList(3, 6);
						suBeans2.addAll(suBeans);
						members.replace("1-3-2-0", suBeans2);
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
				try{
					List<BufrBean> windsBufrBeans1 = (List<BufrBean>) members.get("1-3-2-1");
					if((double)windsBufrBeans1.get(0).getValue() != -6.0) {
						List<BufrBean> suBeans = windsBufrBeans1.subList(0, 3);
						List<BufrBean> suBeans2 = windsBufrBeans1.subList(3, 6);
						suBeans2.addAll(suBeans);
						members.replace("1-3-2-1", suBeans2);
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
				try{
					List<BufrBean> visBufrBeans = (List<BufrBean>) members.get("1-6-2-0");
					if((double)visBufrBeans.get(0).getValue() != -1.0) {
						List<BufrBean> suBeans = visBufrBeans.subList(0, 3);
						List<BufrBean> suBeans2 = visBufrBeans.subList(3, 6);
						suBeans2.addAll(suBeans);
						members.replace("1-6-2-0", suBeans2);
					}
				}catch (Exception e) {
					// TODO: handle exception
				}
				for (String tableName : tables) {
					dateTimeOK = true;
					StatDi di = new StatDi();
					di.setPROCESS_START_TIME(dateFormat.format(new Date()));
					// 根据表名获取配置文件配置
	//				Map<String, EntityBean> entityMap = BufrConfig.get(tableName);
	//				Map<String, EntityBean> entityMap = configMap.get(tableName);
	//				System.out.println(configMap + " " + tableSection+ "\n\n");
//					System.out.println(tableName);
					if(tableName.toUpperCase().startsWith("SURF_WEA_CBF_SSD_HOR_TAB")){
						Object sun = members.get("1-7-0-0"); //1-3-24-0-
						List<BufrBean> bufrs= (List<BufrBean>) sun;
						if(bufrs == null || bufrs.size() == 0) {
							missedSunRecord++;
							continue;
						}
						sun = members.get("1-3-24-0");
						bufrs = (List<BufrBean>) sun;
						if(bufrs == null || bufrs.size() == 0){
							missedSunRecord++;
							continue;
						}
					}
					XmlBean xmlBean = configMap.get(tableName);
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					// 用于存储入库的字段
					StringBuffer keyBuffer = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer = new StringBuffer();
					// 遍历配置信息
					Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
	//				long sqlStart = System.currentTimeMillis();
					//获取每个字段的值
					while (it.hasNext()) {
						Entry<String, EntityBean> next = it.next();
						String column = next.getKey(); // 配置文件配置的字段名称
//						if(column.endsWith("_052"))
//							System.out.println(column);
						EntityBean entityBean = next.getValue(); // 配置的每个字段属性
						
//						if(column.equalsIgnoreCase("V10301_052")){
//							System.out.println(column);
//						}
						
						String datatype = entityBean.getDatatype(); // 字段类型
	//					boolean isQc = entityBean.isQc(); // 此字段是否为质控码
						String expression = entityBean.getExpression(); // 此字段的表达式
						String fxy = entityBean.getFxy(); // 解码时存储数据的key
	//					String name = entityBean.getName();// 字段名称
	//					String dec = entityBean.getDec();// 字段描述
	//					int index = entityBean.getIndex();// 同一个fxy中存储的list中的值的序号
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
							keyBuffer.append(",`").append(column).append("`");
							valueBuffer.append(",'").append(value.toString()).append("'");
							
							if(column.equals("D_DATETIME")){
								int idx01 = value.toString().lastIndexOf(":");
								if(idx01 > 16){
									dateTimeOK = false;
									Log.error("资料时间错误！  " + fileName);
									break;
								}
								else
									di.setDATA_TIME(value.toString().substring(0, idx01));
							}
							if(column.equals("V01301"))
								di.setIIiii(value.toString());
							if(column.equals("V05001"))
								di.setLATITUDE(value.toString());
							if(column.equals("V06001"))
								di.setLONGTITUDE(value.toString());
							if(column.equals("V07001"))
								di.setHEIGHT(value.toString());
							if(column.equals("D_DATA_ID"))
								di.setDATA_TYPE(value.toString());
							if(column.equals("V_BBB"))
								di.setDATA_UPDATE_FLAG(value.toString());
							continue;
						}
						
						// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
	//					long s2 = System.currentTimeMillis();
						value = calExpressionValue(obj, members, entityMap, entityBean);
						//降水量的处理
						if((column.equalsIgnoreCase("V13019") || column.equalsIgnoreCase("V13020")
								|| column.equalsIgnoreCase("V13021") || column.equalsIgnoreCase("V13022")
								|| column.equalsIgnoreCase("V13023") || column.equalsIgnoreCase("V13011"))&& value.toString().startsWith("-0.1"))
							value = "999990";
						if(column.equalsIgnoreCase("V13013") && value.toString().startsWith("-0.02")){
							value = "999990";
						}
						// 风向的处理
						try{
							if((column.equalsIgnoreCase("V11290") || column.equalsIgnoreCase("V11292")
									|| column.equalsIgnoreCase("V11296") || column.equalsIgnoreCase("V11201")
									|| column.equalsIgnoreCase("V11211") || column.equalsIgnoreCase("V11503_06")
									|| column.equalsIgnoreCase("V11503_12") || column.equalsIgnoreCase("V11001")
									|| column.equalsIgnoreCase("V11046") || column.equalsIgnoreCase("V11288") 
									|| column.equalsIgnoreCase("V11043")) && Double.parseDouble(value.toString()) == 0){
								//如果风向，风速均为0，那么静风 999017
								try{
									if(((List<BufrBean>)(members.get("1-11-0-0"))) != null && ((List<BufrBean>)(members.get("1-11-0-0"))).get(8).getValue().toString().equals("0.0"))
										value = "999017";
								}catch (Exception e) {
									
								}
							}
						}catch (Exception e) {
							System.err.println("风向值不为数值型，转换错误！");
						}
						// added on 2019-01-08
						// 台站名称 或着 分钟连续天气现象
						if((column.equalsIgnoreCase("V01015") || column.equalsIgnoreCase("V02320")) && value.toString().contains("'")){
							String tmp = value.toString();
							tmp = tmp.replaceAll("'", "''");
							value = tmp;
						}
						// end added
						// 冻土深度第一层上限值、冻土深度第一层下限值、冻土深度第二层上限值、冻土深度第二层下限值
						//added 2019-1-15
						if((column.equalsIgnoreCase("V20330_01") || column.equalsIgnoreCase("V20331_01") || 
								column.equalsIgnoreCase("V20330_02") || column.equalsIgnoreCase("V20331_02"))&& value.toString().startsWith("999999")){
							BufrBean tBean = (BufrBean)members.get("0-2-201-9");
							if(tBean != null && tBean.getValue().toString().startsWith("999999"))
								//无需观测
								value = "999998";
						}
						// end add
						keyBuffer.append(",`").append(column).append("`");
						this.appendValue(valueBuffer, datatype, value);
						
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
						if(column.equals("V07001"))
							di.setHEIGHT(value.toString());
						if(column.equals("D_DATA_ID"))
							di.setDATA_TYPE(value.toString());
					} // end while
					if(dateTimeOK == false)
						continue;
					if(tableSection.toUpperCase().indexOf("MUL_HOR") > -1)
						di.setDATA_TYPE_1("A.0001.0044.R001");
					else if(tableSection.toUpperCase().indexOf("MUL_MIN") > -1)
						di.setDATA_TYPE_1("A.0001.0043.R001");
					else if(tableSection.toUpperCase().indexOf("REG_HOR") > -1)
						di.setDATA_TYPE_1("A.0001.0042.R001");
					else if(tableSection.toUpperCase().indexOf("REG_MIN") > -1)
						di.setDATA_TYPE_1("A.0001.0041.R001 ");
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setTRAN_TIME(ymdhm.format(new Date()));
					
					di.setTT("");
					di.setRECORD_TIME(dateFormat.format(new Date()));
					di.setPROCESS_END_TIME(dateFormat.format(new Date()));
					di.setFILE_NAME_N(fileName);
					di.setFILE_NAME_O(fileName);
					di.setBUSINESS_STATE("1");
					di.setPROCESS_STATE("1");
					DI.add(di);
					
					String sql = "insert into " + tableName //
							+ " (" //
							+ keyBuffer.toString().substring(1) //
							+ ") values ("//
							+ valueBuffer.toString().substring(1) //
							+ ")";
	//				long sqlEnd2 = System.currentTimeMillis();
	//				Log.debug("sql to string: " + (sqlEnd2 - sqlEnd) + "ms");
	//				Log.debug(sql);
	//				FileUtil.writeString(ICalendar.getSysTime() + "  " + sql + "\n time: " + (sqlEnd - sqlStart) + "ms\n", "./"+ tableName + ".sql", true);
//					System.out.println(sql);
					try {
						boolean execute = statement.execute(sql);
//						if (execute) {
							Log.info(" Sucessfulley insert one record!\n" + sql + " " + fileName);
							
//							Integer succeededNum = tmpRecord.get(SUCCEEDED_KEY);
//							succeededNum = (succeededNum == null ? 1 : (succeededNum +=1));
//							tmpRecord.put(SUCCEEDED_KEY, succeededNum);
							
//						} 
//						else {
//							Log.info("Insert DB failed: \n  " + sql + " " + fileName);
//							
//							Integer otherExceptionNum = tmpRecord.get(OTHER_EXCEPTION_KEY);
//							otherExceptionNum = (otherExceptionNum == null ? 1 : (otherExceptionNum +=1));
//							tmpRecord.put(OTHER_EXCEPTION_KEY, otherExceptionNum);
//							di.setPROCESS_STATE("0");
//	//						return false;
//						}
					} catch (MySQLIntegrityConstraintViolationException e) {
	//					Log.error("主键冲突:\n  " + sql, e);
						Log.error("MySQLIntegrityConstraintViolationException: \n  " + sql + e.getMessage() + " " + fileName);
						Integer conflictNum = tmpRecord.get(PRIMARY_KEY_CONFLICT_KEY);
						conflictNum = (conflictNum == null ? 1 : (conflictNum +=1));
						tmpRecord.put(PRIMARY_KEY_CONFLICT_KEY, conflictNum);
						di.setPROCESS_STATE("0");
	//					return true;
					}catch (Exception e) {
						Log.error("Error:\n " + sql + e.getMessage() + " " + fileName);
						di.setPROCESS_STATE("0");
					}
	
				}
				
//				Integer conflict = tmpRecord.get(PRIMARY_KEY_CONFLICT_KEY);
//				Integer succeeded = tmpRecord.get(SUCCEEDED_KEY);
//				int tmpnum = ((conflict == null ? 0 : conflict) + (succeeded == null ? 0 : succeeded));
//				if(tmpnum == tables.size()) {
//					return true;
//				}else {
//					return false;
//				}
			} // for
//			Integer conflict = tmpRecord.get(PRIMARY_KEY_CONFLICT_KEY);
//			Integer succeeded = tmpRecord.get(SUCCEEDED_KEY);
//			int tmpnum = ((conflict == null ? 0 : conflict) + (succeeded == null ? 0 : succeeded));
//			if(tmpnum + missedSunRecord == tables.size() * bufrDecoder.getParseList().size()) {
//				return true;
//			}else {
//				return false;
//			}
			return true;
		} catch (SQLException e) {
			Log.error("Database exception: " + e.getMessage());
			return false;
		} finally {
			try {
				if(statement != null)
					statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				System.out.println("DB connection = " + connection);
				if(connection != null)
					connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
//		return true;
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
						Log.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
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
					Log.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
				}
			} else {
				Log.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
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

//	@Override
//	public boolean decode(String fileName, String tableSection, List<String> tables, String Priority) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables, String Priority) {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
