package cma.cimiss2.dpc.indb.radi.dc_bufr_radi_minute.service;

import java.io.File;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.alicloud.openservices.tablestore.ClientException;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrRadiMMDecoder;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.BufrConfig;


public class OTSRadiServiceImpl {
	int batchSize = 500;
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> DI = new ArrayList<StatDi>();
	public BufrRadiMMDecoder decoder = new BufrRadiMMDecoder();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		RadiServiceImpl2.diQueues = diQueues;
	}
	public OTSRadiServiceImpl() {
		BufrConfig.init();
	}
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String tableSection = "D.0001.0006.R001";
		String fileName = "D:\\TEMP\\radimin\\Z_RADI_I_57290_20190312020000_O_ARS-MM_FTM_PQC.BIN";
		RadiServiceImpl2 impl = new RadiServiceImpl2();
//		impl.decodeFile(fileName, tableSection);
//		System.out.println("解码、入库总共耗时：" + (System.currentTimeMillis() - start));
	}

	public void decodeStream(String filename, byte[] dataBytes, String cts_code){
		DruidPooledConnection connection = null;
		connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		
		List<String> tableList = new ArrayList<String>();
		tableList = BufrConfig.getTableMap().get(cts_code);
		Map<String, XmlBean> configMap = BufrConfig.get(cts_code);
		
		try{
			decoder.decodeFile(filename, dataBytes);
			for (Map<String, Object> parseMap : decoder.parseList) {
				for (int i = 0; i < tableList.size(); i++) {
					String tableName = tableList.get(i);
					processNormalTable(cts_code, connection, filename, tableName, parseMap, configMap);
				}
			}
		}catch (Exception e) {
			infoLogger.error(e.getMessage() + "  " + filename);
			e.printStackTrace();
		}finally{
			try{
				connection.close();
			}catch (Exception e) {
				infoLogger.error("connection.close() Failed!");
			}
		}
	}
	public  void decodeFile(String filename, String cts_code) {
		DruidPooledConnection connection = null;
		connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		
		List<String> tableList = new ArrayList<String>();
		tableList = BufrConfig.getTableMap().get(cts_code);
		Map<String, XmlBean> configMap = BufrConfig.get(cts_code);
		
		try{
			decoder.decodeFile(filename);
			for (Map<String, Object> parseMap : decoder.parseList) {
				for (int i = 0; i < tableList.size(); i++) {
					String tableName = tableList.get(i);
					processNormalTable(cts_code, connection, filename, tableName, parseMap, configMap);
				}
			}
		}catch (Exception e) {
			infoLogger.error(e.getMessage() + "  " + filename);
			e.printStackTrace();
		}finally{
			try{
				connection.close();
			}catch (Exception e) {
				infoLogger.error("connection.close() Failed!");
			}
		}
	}

	private void processNormalTable(String cts_code, DruidPooledConnection connection, String filename, String tableName, Map<String, Object> members, Map<String, XmlBean> configMap) {
		File fileN =new File(filename);
		StringBuffer valBuffer = new StringBuffer();
		StringBuffer keyBuffer = new StringBuffer();
		XmlBean xmlBean = configMap.get(tableName);
		Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
		Statement statement = null;
		
	
		// DI 初始化
		StatDi di = new StatDi();
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setTT("");
		di.setDATA_TYPE_1(StartConfig.ctsCode());
		di.setFILE_NAME_N(filename);
		di.setFILE_NAME_O(filename);
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
//			System.out.println(fxy+"  "+expression+"  "+column);
			if("V01101".equals(column)){
				System.out.println(expression);
			}
//			System.out.println(column);
//			if(column.startsWith("V14311")){
//				System.err.println(column);
//			}
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
				if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
					value = members.get("CCCC");
				if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
					value = members.get("TTAAii");
				if(column.equalsIgnoreCase("V_BBB"))
					value = members.get("BBB");
				if("V01101".equals(column)&&value.toString().startsWith("99999")){//若国家和地区标识在lua的+11中没有取到值，则取+01的值
					entityBean.setExpression("${stationInfo(${col:V01301},01,999999)}.split(',')[4]");
					value = calExpressionValue(obj, members, entityMap, entityBean);
				}
				keyBuffer.append(",`").append(column).append("`");
				valBuffer.append(",'").append(value.toString().trim()).append("'");
				row.put(column, value.toString().trim());
				if(column.equals("D_DATETIME"))
					di.setDATA_TIME(String.valueOf(value));
				if(column.equals("V01301"))
					di.setIIiii(value.toString());
				if(column.equals("V05001"))
					di.setLATITUDE(value.toString());
				if(column.equals("V06001"))
					di.setLONGTITUDE(value.toString());
				if(column.equals("D_DATA_ID"))
					di.setDATA_TYPE(value.toString());
				continue;
			}

			// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
			value = calExpressionValue(obj, members, entityMap, entityBean);
			
			if(column.toUpperCase().startsWith("V07032") && value.toString().startsWith("-0.01"))
				value = "999999";
			
			keyBuffer.append(",`").append(column).append("`");
			this.appendValue(valBuffer, datatype, value.toString().trim());
			row.put(column, value.toString().trim());
			if(column.equals("D_DATETIME"))
				di.setDATA_TIME(String.valueOf(value));
			if(column.equals("V01301"))
				di.setIIiii(value.toString());
			if(column.equals("V05001"))
				di.setLATITUDE(value.toString());
			if(column.equals("V06001"))
				di.setLONGTITUDE(value.toString());
			if(column.equals("D_DATA_ID"))
				di.setDATA_TYPE(value.toString());
		}// end for
		
		File file = new File(filename);
		file.length();
		di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setFILE_SIZE(String.valueOf((new File(filename)).length()));
		di.setBUSINESS_STATE("0");
		di.setPROCESS_STATE("0");
		
		// 键表入库
//		String sql = "";
//		sql = "insert into " + tableName //
//				+ " (" //
//				+ keyBuffer.toString().substring(1) //
//				+ ") values ("//
//				+ valBuffer.toString().substring(1) //
//				+ ")";
		try{
//			statement = connection.createStatement();
//			try {// 插入
//				if(connection.getAutoCommit() == false)
//					connection.setAutoCommit(true);
//				statement.execute(sql);		
//			}catch (Exception e) {
//				infoLogger.error("插入失败:" + sql + "\n " + e.getMessage() + filename);
//				di.setPROCESS_STATE("1");
//			}
			OTSDbHelper.getInstance().insert(tableName, row, true);
			infoLogger.info(tableName + ": Insert one line successfully!\n");
			diQueues.offer(di);
		}catch (Exception e) {
//			infoLogger.error("connection.createStatement() failed!");
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
		}
		
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
					if(expCol.toUpperCase().equals("V01301") && columnValue.trim().startsWith("999999")){
						try{
							Double area = (Double)((BufrBean) members.get("0-1-3-0")).getValue();
							Double subArea = (Double)((BufrBean) members.get("0-1-20-0")).getValue();
							Double buoy_platformID = (Double)((BufrBean) members.get("0-1-5-0")).getValue();
							columnValue = String.format("%02d", area.intValue()) + String.format("%02d", subArea.intValue()) + String.format("%03d", buoy_platformID.intValue());
						}catch (Exception e) {
							infoLogger.error("WMO marine observing platform extended identifier 解析失败：数值转换失败！\n");
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
				if(value != null)
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
				Map<String, BufrBean> newBufrList = new HashMap<>();
				for(Map<String, BufrBean> maps : bufrList){
					for(String keyT : maps.keySet())
						newBufrList.put(keyT, maps.get(keyT));
				}
				
				BufrBean bean = new BufrBean();
				String fxy = expression;
				bean = newBufrList.get(fxy);
//				System.out.println(fxy);
				if(isQc){
					if(bean.getQcValues() != null)
						value = bean.getQcValues().get(0);
					else 
						value = 9;
					int parseValue = Integer.parseInt(value.toString());
					value = parseValue > 9 ? 9 : parseValue;
				}else{
					value = entityBean.getDefaultValue();
					if(!bean.getValue().toString().equals("NaN"))
						value = bean.getValue();
				}
			}
			else{
				@SuppressWarnings("unchecked")
				List<Map<String, BufrBean>> bufrList = (List<Map<String, BufrBean>>) obj;
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
						Map<String, BufrBean> map =  bufrList.get(index);
						Object value2 = null;
						if (isQc) {
							for (String key : map.keySet()) {
								BufrBean bufrBean = map.get(key);
								
								if(bufrBean.getQcValues() != null)
									value2 = bufrBean.getQcValues().get(0);
								else 
									value2 = 9;
								int parseValue = Integer.parseInt(value2.toString());
								value2 = parseValue > 9 ? 9 : parseValue;
								ts.add(value2.toString());
							}
						} else {
							for (String key : map.keySet()) {
								BufrBean bufrBean = map.get(key);
								value2 = bufrBean.getValue();
								ts.add(value2.toString());
							}
						}
						value = value2;
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



}
