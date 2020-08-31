package cma.cimiss2.dpc.indb.upar.dc_upar_bufr_global_temp.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.cimiss2.dwp.tools.utils.UparLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrDecoder2;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class OTSUparBufrServiceImpl {
	final  String defaultValue = "999999";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public BufrDecoder2 decoder = new BufrDecoder2();
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static List<StatDi> DI = new ArrayList<StatDi>();
	public int levelNumOfNSEC = 0;
		
	public OTSUparBufrServiceImpl() {
		BufrConfig.init();
	}
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSUparBufrServiceImpl.diQueues = diQueues;
	}

	public  boolean decodeFile(String filename, String cts_code) {
		List<String> tableList = new ArrayList<String>();
		tableList = BufrConfig.getTableMap().get(cts_code);
		Map<String, XmlBean> configMap = BufrConfig.get(cts_code);
		
		try{
			decoder.decodeFile(filename);
			UparLevel uparLevel = new UparLevel();
			for (Map<String, Object> parseMap : decoder.parseList) {
				for (int i = 0; i < tableList.size(); i++) {
					String tableName = tableList.get(i);
					if(tableName.toUpperCase().contains("UPAR_WEA_GBF_MUL_FTM_TAB")){
						processSecondElementTable(cts_code, uparLevel, filename, tableName, parseMap, configMap);
					}
					else {
						processNormalTable(cts_code, uparLevel, filename, tableName, parseMap, configMap);
					}
				}
			}
			return true;
		}catch (Exception e) {
			infoLogger.error(e.getMessage());
			return false;
		}
	}

	// 处理入库除了要素表之外的表
		private void processNormalTable(String cts_code, UparLevel uparLevel, String filename, String tableName, Map<String, Object> members, Map<String, XmlBean> configMap) {
			File fileN=new File(filename);
			StringBuffer valBuffer = new StringBuffer();
			StringBuffer keyBuffer = new StringBuffer();
			XmlBean xmlBean = configMap.get(tableName);
			Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
			Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
			String D_record_id = "";
			if(helpMap != null)
				entityMap.putAll(helpMap);
			
//			if(diQueues == null)
//				diQueues = new LinkedBlockingQueue<StatDi>();
			// DI 初始化
			StatDi di = new StatDi();
			di.setPROCESS_START_TIME(TimeUtil.getSysTime());
			di.setDATA_TYPE_1(cts_code);
			
			Calendar calendar = Calendar.getInstance();
			try{
				BufrBean beanYear = (BufrBean)members.get(entityMap.get("V04001_02").getFxy());
				BufrBean beanMonth = (BufrBean)members.get(entityMap.get("V04002_02").getFxy());
				BufrBean beanDayOfMonth = (BufrBean)members.get(entityMap.get("V04003_02").getFxy());
				BufrBean beanHourOfDay = (BufrBean)members.get(entityMap.get("V04004_02").getFxy());
				calendar.set(Calendar.YEAR, (int)Double.parseDouble((beanYear.getValue().toString())));
				calendar.set(Calendar.MONTH, (int)Double.parseDouble((beanMonth.getValue().toString())) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble((beanDayOfMonth.getValue().toString())));
				calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble((beanHourOfDay.getValue().toString())));
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				if(simpleDateFormat.format(calendar.getTime()).length() > 14){
					infoLogger.error("资料时间错误：" + filename);
					return ;
				}
				calendar.setTime(transDateTime(calendar.getTime()));
			}catch (Exception e) {
				infoLogger.error("资料时间错误：" + filename);
				return;
			}
			Map<String, Object> row = new HashMap<>();
			// 遍历xml表格字段，赋值
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
						if(column.equalsIgnoreCase("D_SOURCE_ID"))
							value += "_" + fileN.getName();
					}
					if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
						value = members.get("CCCC");
					if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
						value = members.get("TTAAii");
					if(column.equalsIgnoreCase("V_BBB"))
						value = members.get("BBB");
					// 资料时间规整
					if(column.equalsIgnoreCase("D_DATETIME")){
						value = simpleDateFormat2.format(calendar.getTime());
					}
					else if(column.equalsIgnoreCase("V02421") && value.toString().startsWith("9999"))
						value = "999999";
					if(tableName.toUpperCase().contains("UPAR_WEA_GBF_MUL_FTM_K_TAB"))
						// 层次值赋值
						value = getLevel(column, uparLevel, value);
					
					
					if(column.equalsIgnoreCase("D_RECORD_ID")){
						D_record_id = value.toString();
						String dt = D_record_id.split("_")[0];
						D_record_id = D_record_id.replace(dt, simpleDateFormat.format(calendar.getTime()));
						value = D_record_id;
					}
					
					else if(column.equalsIgnoreCase("V01300")){
						 Pattern pattern = Pattern.compile("[0-9]*");
				         Matcher isNum = pattern.matcher(((BufrBean)members.get("0-1-2-0")).getValue().toString());
				           if( !isNum.matches() ){
				        	   value = 999999;
				           }
					}
					
					keyBuffer.append(",`").append(column).append("`");
					valBuffer.append(",'").append(value.toString().trim()).append("'");
					
					row.put(column, value);
					
					if(column.equals("D_DATETIME")){
						value = simpleDateFormat2.format(calendar.getTime());
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
					
					continue;
				}
				if(column.equals("V04006")){
//					System.out.println(column);
				}
				// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
//				System.err.println(column);
				value = calExpressionValue(obj, members, entityMap, entityBean);
				
				if(column.equalsIgnoreCase("V04001_02")){
					value = calendar.get(Calendar.YEAR);
				}else if(column.equalsIgnoreCase("V04002_02")){
					value = calendar.get(Calendar.MONTH) + 1;
				}else if(column.equalsIgnoreCase("V04003_02")){
					value = calendar.get(Calendar.DAY_OF_MONTH);
				}else if(column.equalsIgnoreCase("V04004_02")){
					value = calendar.get(Calendar.HOUR_OF_DAY);
				}
				
				else if(column.equalsIgnoreCase("V02011")){
					try{
						double a = Double.parseDouble(value.toString());
						if(a == -1.0){
							value = "999999";
						}
						else if(a < -1.0){
							value = String.valueOf(a + 256);
						}
					}catch (Exception e) {
						
					}
				}
					
				
				if(helpMap != null){
					if(!helpMap.containsKey(column)){
						keyBuffer.append(",`").append(column).append("`");
						this.appendValue(valBuffer, datatype, value.toString().trim());
						row.put(column, value.toString().trim());
					}
				}
				else{
					keyBuffer.append(",`").append(column).append("`");
					this.appendValue(valBuffer, datatype, value.toString().trim());
					row.put(column, value.toString().trim());
				}
				
				if(column.equals("D_DATETIME")){
					value = simpleDateFormat2.format(calendar.getTime());
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
			}// end for
			
			File file = new File(filename);
			di.setFILE_SIZE(String.valueOf(file.length()));
			di.setSEND("BFDB");
			di.setSEND_PHYS("DRDS");
			di.setTRAN_TIME(ymdhm.format(new Date()));
			di.setTT("");
			di.setPROCESS_END_TIME(TimeUtil.getSysTime());
			di.setRECORD_TIME(TimeUtil.getSysTime());
			di.setFILE_NAME_N(filename);
			di.setFILE_NAME_O(filename);
			di.setBUSINESS_STATE("1");
			di.setPROCESS_STATE("1");
			if(DI != null)
				DI.add(di);
			// 键表入库
			try {
				if(cts_code.toUpperCase().equals("B.0001.0017.R001") || cts_code.toUpperCase().equals("B.0001.0019.R001") ||
						(members.get("CCCC").toString().equalsIgnoreCase("RJTD") && members.get("TTAAii").toString().equalsIgnoreCase("IUSC70")))
					OTSDbHelper.getInstance().insert(tableName, row, true);
				else{
					OTSDbHelper.getInstance().insert(tableName, row, false);
				}
				infoLogger.info(tableName + ": Insert one line successfully!\n");
				diQueues.offer(di);
			}catch (Exception e) {
				di.setPROCESS_STATE("0");
				diQueues.offer(di);
				infoLogger.error(filename + ": " + e.getMessage() + "\n");
				infoLogger.error(row + "\n");
				if(e.getClass() == ClientException.class) {
					infoLogger.error("DataBase connection error!\n");
				}
			}
			if(DI != null){
				for (int j = 0; j < DI.size(); j++) {
					diQueues.offer(DI.get(j));
				}
				DI.clear();
			}
			
		}
		
		// 返回要素表NSEC条数
		private void processSecondElementTable(String cts_code, UparLevel uparLevel, String fileName, String table, Map<String, Object> parseMap, Map<String, XmlBean> configMap) {
			StringBuffer headercol = new StringBuffer();
			StringBuffer headerval = new StringBuffer();
			
			String record_id = "";
			Map<String, Object> row = new HashMap<>();
			record_id = processheader(cts_code, fileName, table, headercol, headerval, parseMap, configMap, row);
			Long tLong = System.currentTimeMillis();
			if((!record_id.equals("")) && table.toUpperCase().contains("_FTM_"))
				processsSecondElementFTM(cts_code, uparLevel, record_id, fileName, table, headercol, headerval, parseMap, configMap, row);
			
			System.out.println("要素表构建SQL+入库耗时：" + (System.currentTimeMillis() - tLong));
			
		}

		private String processheader(String cts_code, String fileName, String table, StringBuffer headercol, StringBuffer headerval, Map<String, Object> parseMap, Map<String, XmlBean> configMap, Map<String, Object> row) {
			File fileN=new File(fileName);
			XmlBean xmlBean = configMap.get(table);
			Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
			Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
			String d_record_id = "";
			if(helpMap != null)
				entityMap.putAll(helpMap);
			Calendar calendar = Calendar.getInstance();
			try{
				BufrBean beanYear = (BufrBean)parseMap.get(entityMap.get("V04001_02").getFxy());
				BufrBean beanMonth = (BufrBean)parseMap.get(entityMap.get("V04002_02").getFxy());
				BufrBean beanDayOfMonth = (BufrBean)parseMap.get(entityMap.get("V04003_02").getFxy());
				BufrBean beanHourOfDay = (BufrBean)parseMap.get(entityMap.get("V04004_02").getFxy());
				calendar.set(Calendar.YEAR, (int)Double.parseDouble((beanYear.getValue().toString())));
				calendar.set(Calendar.MONTH, (int)Double.parseDouble((beanMonth.getValue().toString())) - 1);
				calendar.set(Calendar.DAY_OF_MONTH, (int)Double.parseDouble((beanDayOfMonth.getValue().toString())));
				calendar.set(Calendar.HOUR_OF_DAY, (int)Double.parseDouble((beanHourOfDay.getValue().toString())));
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				calendar.setTime(transDateTime(calendar.getTime()));
				System.out.println(simpleDateFormat.format(calendar.getTime()));
				if(simpleDateFormat.format(calendar.getTime()).length() > 14){
					infoLogger.error("资料时间错误：" + fileName);
					return d_record_id;
				}
			}catch (Exception e) {
				infoLogger.error("资料时间错误：" + fileName);
				return d_record_id;
			}
		
			for(String key : entityMap.keySet()){
				EntityBean entityBean = entityMap.get(key);
				String fxy = entityBean.getFxy();
				String datatype = entityBean.getDatatype();
				String column = entityBean.getColumn();
				Object obj = parseMap.get(fxy);
				Object value = null;
				String expression = entityBean.getExpression(); // 此字段的表达式
				if (obj == null) {
					// 如果表达式不为空说明此值需要根据表达式进行计算value
					if (StringUtils.isNotBlank(expression)) {
						// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
						Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
						if (em.find()) {
							value = defaultValue;
						} else {// 如果表达式存在，且其中没有以index取值
							value = calExpressionValue(obj, parseMap, entityMap, entityBean);
						}
					} else {
						value = entityBean.getDefaultValue();
					}
					if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
						value = parseMap.get("CCCC");
					if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
						value = parseMap.get("TTAAii");
					if(column.equalsIgnoreCase("V_BBB"))
						value = parseMap.get("BBB");
					// 资料时间规整
					if(column.equalsIgnoreCase("D_DATETIME")){
						value = simpleDateFormat2.format(calendar.getTime());
					}
					
					else if(column.equalsIgnoreCase("D_RECORD_ID")){
						d_record_id = value.toString();
						String dt = d_record_id.split("_")[0];
						d_record_id = d_record_id.replace(dt, simpleDateFormat.format(calendar.getTime()));
						value = d_record_id;
					}
					else if(column.equalsIgnoreCase("V01300")){
						 Pattern pattern = Pattern.compile("[0-9]*");
				         Matcher isNum = pattern.matcher(((BufrBean)parseMap.get("0-1-2-0")).getValue().toString());
				           if( !isNum.matches() ){
				        	   value = 999999;
				           }
					}
					else if(column.equalsIgnoreCase("D_SOURCE_ID")){
						value = cts_code + "_" + fileN.getName();;
					}
					
					headercol.append(",`").append(column).append("`");
					headerval.append(",'").append(value.toString().trim()).append("'");
					row.put(column, value.toString().trim());
					
					continue;
				}
				// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
				value = calExpressionValue(obj, parseMap, entityMap, entityBean);
				
				if(column.equalsIgnoreCase("V04001_02")){
					value = calendar.get(Calendar.YEAR);
				}else if(column.equalsIgnoreCase("V04002_02")){
					value = calendar.get(Calendar.MONTH) + 1;
				}else if(column.equalsIgnoreCase("V04003_02")){
					value = calendar.get(Calendar.DAY_OF_MONTH);
				}else if(column.equalsIgnoreCase("V04004_02")){
					value = calendar.get(Calendar.HOUR_OF_DAY);
				}
				
				if(!helpMap.containsKey(column)){
					headercol.append(",`").append(column).append("`");
					this.appendValue(headerval, datatype, value.toString().trim());
					row.put(column, value.toString().trim());
				}
				
			} // end while
			return d_record_id;
		}

		//FTM 要素表SQL，返回层次数对象
		private UparLevel processsSecondElementFTM(String cts_code, UparLevel uparLevel, String record_id, String fileName, String table, StringBuffer headercol, StringBuffer headerval, Map<String, Object> parseMap, Map<String, XmlBean> configMap, Map<String,Object> headRow){
			XmlBean xmlBean = configMap.get(table);
			Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
//			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			for(String groupKey : groupMapList.keySet()){
				Map<String, EntityBean> group = groupMapList.get(groupKey);
				String groupFXY = groupKey.split("_")[0];
				List<Map<String, BufrBean>> BufrBeanList = (List<Map<String, BufrBean>>) parseMap.get(groupFXY);
				for (Map<String, BufrBean> map : BufrBeanList) {
					StringBuffer keyBuffer = new StringBuffer();
					StringBuffer valBuffer = new StringBuffer();
					Map<String, Object> row = new HashMap<>();
					
					for(String key : group.keySet()){
						EntityBean bean = group.get(key);
						if(bean.isQc()){
							String col = bean.getColumn();
							String fxy = bean.getFxy();
							BufrBean bufrBean = map.get(fxy);
							Object val = null;
							if(bufrBean != null && bufrBean.getQcValues() != null){
								val = bufrBean.getQcValues().get(0);
							}
							else{
								if(bufrBean==null && "Q10009".equals(col)){
									BufrBean bufrBean2 = map.get("0-7-9");
									if(bufrBean2 != null && bufrBean2.getQcValues() != null){
										val = bufrBean2.getQcValues().get(0);
									}else{
										val = "9";
									}
								}else{
									val = "9";
								}
							}
								
							keyBuffer.append(",`").append(col).append("`");
							valBuffer.append(",'").append(val.toString().trim()).append("'");
							row.put(col, val);
						}else{
							String col = bean.getColumn();
							String fxy = bean.getFxy();
							String datatype = bean.getDatatype(); 
							String exp = bean.getExpression();
							Object val = null;
							if(col.equalsIgnoreCase("D_ELE_ID")){
								val = record_id ;
								String []exprSp = exp.split(",");
								for(int e = 0; e < exprSp.length; e ++){
									if(map.get(exprSp[e].trim()) != null){
										val += "_" + map.get(exprSp[e].trim()).getValue().toString();
									}else {
										if("0-10-9".equals(exprSp[e].trim())&&map.get("0-7-9")!=null){
											val += "_" + map.get("0-7-9").getValue().toString();
										}else{
										    val += "_" + bean.getDefaultValue();
										}
									}
								}
							}
							else if (col.equalsIgnoreCase("V10009")) {//位势高度
								BufrBean bufrBean = map.get(fxy);
								if(bufrBean != null){
									val = bufrBean.getValue();
								}else if(bufrBean==null &&map.get("0-7-9")!=null){
									val=map.get("0-7-9").getValue();
								}else {
									val = bean.getDefaultValue();
								}
							}
							else if(col.equalsIgnoreCase("V12001") || col.equalsIgnoreCase("V12003")){
								BufrBean bufrBean = map.get(fxy);
								if(bufrBean != null){
									val = bufrBean.getValue();
									if(val.toString().startsWith("-0.01"))
										val = defaultValue;
									else{
										if(exp != null && !exp.isEmpty() && !val.toString().startsWith("999999"))
											val = getExprValue(exp, val, datatype);
									}
								}
								else
									val = bean.getDefaultValue();
							}
							else{
								BufrBean bufrBean = map.get(fxy);
								if(bufrBean != null){
									val = bufrBean.getValue();
									if(exp != null && !exp.isEmpty() && !val.toString().startsWith("999999"))
										val = getExprValue(exp, val, datatype);
								}
								else
									val = bean.getDefaultValue();
							}
							
							// 层次数赋值（标准层层数、对流层层数、最大风层层数、温度特性层层数、风特性层层数、零度层层数、高度层层数）
							if(table.toUpperCase().contains("_FTM_"))
								if(col.equalsIgnoreCase("V08042"))
									UparLevel.setLevels(uparLevel, val.toString(), 18);
							
							keyBuffer.append(",`").append(col).append("`");
							valBuffer.append(",'").append(val.toString().trim()).append("'");
							row.put(col, val);
						}
					}
					
					keyBuffer.append(headercol.toString());
					valBuffer.append(headerval.toString().trim());
					
					row.putAll(headRow);
//					batchs.add(row);
					try {
						if(cts_code.toUpperCase().equals("B.0001.0017.R001") || cts_code.toUpperCase().equals("B.0001.0019.R001") || 
								(parseMap.get("CCCC").toString().equalsIgnoreCase("RJTD") && parseMap.get("TTAAii").toString().equalsIgnoreCase("IUSC70")))
							OTSDbHelper.getInstance().insert(table, row, true);
						else{
							OTSDbHelper.getInstance().insert(table, row, false);
						}
						infoLogger.info(table + ": Insert one line successfully!\n");
					}catch (Exception e) {
						infoLogger.error(fileName + ": " + e.getMessage() + "\n");
						infoLogger.error(row + "\n");
						if(e.getClass() == ClientException.class) {
							infoLogger.error("DataBase connection error!\n");
						}
					}
				}// end BufrBeanList
			}// end groupMapList
			return uparLevel;
		}

		public Object getExprValue(String expr, Object value, String datatype){
			double base = 0;
			double val = 0;
			String exprSp = expr.substring(0, 1);
			if(value.toString().startsWith("999999"))
				return val;
			switch (exprSp) {
			case "+":
				base = Double.parseDouble(value.toString());
				val = Double.parseDouble(expr.substring(1));
				val = base + val;
				break;
			case "-":
				base = Double.parseDouble(value.toString());
				val = Double.parseDouble(expr.substring(1));
				val = base - val;
				break;
			case "*":
				base = Double.parseDouble(value.toString());
				val = Double.parseDouble(expr.substring(1));
				val = base * val;
				break;
			
			default:
				val = 999999;
				break;
			}
			if(datatype.equalsIgnoreCase("int")){
				return (int)val;
			}else if(datatype.equalsIgnoreCase("string")){
				return String.valueOf(val);
			}
			else{
				return val;
			}
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
						if(columnValue.startsWith("999999.0") && ebean.getDefaultValue() != null)
							columnValue = ebean.getDefaultValue();
						if(ebean.getDatatype().equals("int")){
							int idx = columnValue.indexOf(".");
							if(idx >= 1)
								columnValue = columnValue.substring(0, idx);
						}
//						System.out.println(expression + "====" + cExp);
						
						String afterText = expression.substring(expression.indexOf(cExp));
//						String cExpReg = StringUtil.string2Regex(cExp);
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
							if(!ebean.getColumn().equalsIgnoreCase("V01301") && !ebean.getColumn().equalsIgnoreCase("V01300")){
								try{
									sp[0] = String.format("%d", Integer.parseInt(sp[0]));
								}
								catch (Exception e1) {
									try{
										sp[0] = String.format("%.3f", Double.parseDouble(sp[0]));
									}
									catch (Exception e2) {
	//									System.out.println(expression);
									}
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
//					value = bufr.getValue();
					String expression = entityBean.getExpression();
					if (StringUtils.isNotBlank(expression)) {
						Matcher numMatcher = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
						if (numMatcher.find()) {
							List<String> ts = new ArrayList<String>();
							value = entityBean.getDefaultValue();
							if(!bufr.getValue().toString().equals("NaN"))
								value = bufr.getValue();
							ts.add(value.toString());
							value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
						}else {
							infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
						}
					}else {
						value = bufr.getValue();
					}
					if(value != null && value.toString().equals("999999.0")){
						value = entityBean.getDefaultValue();
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
		private Object getLevel(String column, UparLevel uparLevel, Object value){
			Object val = value;
			if(column.equalsIgnoreCase("V31001_S"))
				val = (Object)uparLevel.getStandardLvs();
			else if(column.equalsIgnoreCase("V31001_C"))
				val = (Object)uparLevel.getConvectionLvs();
			else if(column.equalsIgnoreCase("V31001_X"))
				val = (Object)uparLevel.getMaxWindLvs();
			else if(column.equalsIgnoreCase("V31001_N"))
				val = (Object)uparLevel.getTemperatureLvs();
			else if(column.equalsIgnoreCase("V31001_W"))
				val = (Object)uparLevel.getWindLvs();
			else if(column.equalsIgnoreCase("V31001_Z"))
				val = (Object)uparLevel.getZeroLvs();
			else if(column.equalsIgnoreCase("V31001_H"))
				val = (Object)uparLevel.getHeightLvs();
			return val;
		}
		private static Date transDateTime(Date srcDate){
			Date src = srcDate;
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(src);
			if(calendar.get(Calendar.HOUR_OF_DAY) % 3 == 1){
				calendar.add(Calendar.HOUR_OF_DAY, -1);
			}else if(calendar.get(Calendar.HOUR_OF_DAY) % 3 == 2){
				calendar.add(Calendar.HOUR_OF_DAY, 1);
			}
			return calendar.getTime();
		}

}
