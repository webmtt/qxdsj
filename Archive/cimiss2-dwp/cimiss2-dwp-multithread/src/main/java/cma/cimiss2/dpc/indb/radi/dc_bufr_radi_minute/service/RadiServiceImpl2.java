package cma.cimiss2.dpc.indb.radi.dc_bufr_radi_minute.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrRadiMMDecoder;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.BufrConfig;
import jnr.ffi.Struct.int16_t;


public class RadiServiceImpl2 {
	int batchSize = 500;
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> DI = new ArrayList<StatDi>();
	boolean dateTimeOK =true;
//	public BufrRadiMMDecoder decoder = new BufrRadiMMDecoder();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		RadiServiceImpl2.diQueues = diQueues;
	}
	public RadiServiceImpl2() {
		BufrConfig.init();
	}
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		String tableSection = "D.0001.0006.R001";
		String fileName = "D:\\TEMP\\D.1.6.1\\9-10\\Z_RADI_I_59948_20190910090000_O_ARS-MM_FTM_PQC.bin";
		RadiServiceImpl2 impl = new RadiServiceImpl2();
		int length=10;
		Date recv = new Date();
		impl.decodeFile(fileName, tableSection,length,recv);
		System.out.println("解码、入库总共耗时：" + (System.currentTimeMillis() - start));
	}

	public void decodeStream(String filename, byte[] dataBytes, String cts_code, Date recv){
		DruidPooledConnection connection = null;
		connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		
		List<String> tableList = new ArrayList<String>();
		tableList = BufrConfig.getTableMap().get(cts_code);
		Map<String, XmlBean> configMap = BufrConfig.get(cts_code);
		
		try{
			BufrRadiMMDecoder decoder = new BufrRadiMMDecoder();
			decoder.decodeFile(filename, dataBytes);
			ReportInfoToDb(decoder, cts_code, filename,dataBytes,recv);
			for (Map<String, Object> parseMap : decoder.parseList) {
				dateTimeOK =true;
				for (int i = 0; i < tableList.size(); i++) {
					dateTimeOK =true;
					String tableName = tableList.get(i);
					processNormalTable(cts_code, connection, filename, tableName, parseMap, configMap, dataBytes.length, recv);
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
	public  void decodeFile(String filename, String cts_code, int length, Date recv) {
		DruidPooledConnection connection = null;
		connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		
		List<String> tableList = new ArrayList<String>();
		tableList = BufrConfig.getTableMap().get(cts_code);
		Map<String, XmlBean> configMap = BufrConfig.get(cts_code);
		
		try{
			BufrRadiMMDecoder decoder = new BufrRadiMMDecoder();
			decoder.decodeFile(filename);
			byte[] dataBytes={0};
			ReportInfoToDb(decoder, cts_code, filename,dataBytes,recv);
			for (Map<String, Object> parseMap : decoder.parseList) {
				for (int i = 0; i < tableList.size(); i++) {
					String tableName = tableList.get(i);
					processNormalTable(cts_code, connection, filename, tableName, parseMap, configMap, length, recv);
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

	private void processNormalTable(String cts_code, DruidPooledConnection connection, String filename, String tableName, Map<String, Object> members, Map<String, XmlBean> configMap, int length ,Date recv) {
		File fileN =new File(filename);
		StringBuffer valBuffer = new StringBuffer();
		StringBuffer keyBuffer = new StringBuffer();
		XmlBean xmlBean = configMap.get(tableName);
		Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
		Statement statement = null;
		dateTimeOK =true;
	
		// DI 初始化
		StatDi di = new StatDi();
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setTT("");
		di.setDATA_TYPE_1(StartConfig.ctsCode());
		di.setFILE_NAME_N(filename);
		di.setFILE_NAME_O(filename);
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
				// chy  修改
				if(!column.equalsIgnoreCase("D_record_id")){
					keyBuffer.append(",`").append(column).append("`");
					valBuffer.append(",'").append(value.toString().trim()).append("'");
				}
				if(column.equals("D_DATETIME")){
					int idx01 = value.toString().lastIndexOf(":");
					if(idx01 > 16){
						dateTimeOK = false;
						infoLogger.error("资料时间错误！  " + filename);
						break;
					}
					else{
						di.setDATA_TIME(value.toString().substring(0, idx01));
						try{
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						    Date dataTime=simpleDateFormat.parse(value.toString());
							if(!TimeCheckUtil.checkTime(dataTime)){
								dateTimeOK = false;
								infoLogger.info("DataTime out of range:"+dataTime+" "+filename);
								break;
							}
						}catch (Exception e) {
							dateTimeOK = false;
							infoLogger.error("资料时间错误！  " + filename);
							break;
						}
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
			value = calExpressionValue(obj, members, entityMap, entityBean);
			
			if(column.toUpperCase().startsWith("V07032") && value.toString().startsWith("-0.01"))
				value = "999999";
			
			//  chy 修改
			if(!column.equalsIgnoreCase("D_RECORD_ID")){
				keyBuffer.append(",`").append(column).append("`");
				this.appendValue(valBuffer, datatype, value.toString().trim());
			}
			
			if(column.equals("D_DATETIME")){
				int idx01 = value.toString().lastIndexOf(":");
				di.setDATA_TIME(value.toString().substring(0, idx01));
				try{
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    Date dataTime=simpleDateFormat.parse(value.toString());
					if(!TimeCheckUtil.checkTime(dataTime)){
						dateTimeOK = false;
						infoLogger.info("DataTime out of range:"+dataTime+" "+filename);
						break;
					}
				}catch (Exception e) {
					dateTimeOK = false;
					infoLogger.error("资料时间错误！  " + filename);
					break;
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
		}// end for
		if(dateTimeOK =false){
			return;
		}
			
		di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
		di.setPROCESS_END_TIME(TimeUtil.getSysTime());
		di.setRECORD_TIME(TimeUtil.getSysTime());
		di.setFILE_SIZE(String.valueOf(length));
		di.setBUSINESS_STATE("1");
		di.setPROCESS_STATE("1");
		
		di.setSEND("BFDB");
		di.setSEND_PHYS("DRDS");
		
		DI.add(di);
		// 键表入库
		String sql = "";
		sql = "insert into " + tableName //
				+ " (" //
				+ keyBuffer.toString().substring(1) //
				+ ") values ("//
				+ valBuffer.toString().substring(1) //
				+ ")";
		try{
			statement = connection.createStatement();
			try {// 插入
				if(connection.getAutoCommit() == false)
					connection.setAutoCommit(true);
				statement.execute(sql);	
				infoLogger.info("插入一条数据成功！"+ filename);
			}catch (Exception e) {
				infoLogger.error("插入失败:" + sql + "\n " + e.getMessage() + filename);
				di.setPROCESS_STATE("0");
			}
		}catch (Exception e) {
			infoLogger.error("connection.createStatement() failed!");
		}
		finally{
			try{
				if(statement!=null){
					statement.close();
				}
			}catch (Exception e) {
				infoLogger.error("statement.close() Failed!");
			}
		}
		if(DI != null){
			for (int j = 0; j < DI.size(); j++) {
				diQueues.offer(DI.get(j));
			}
			DI.clear();
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

	/**
	 * 入服务库报告表
	 * @param bufrDecoder
	 * @param tableSection
	 * @param fileName
	 */
	 public  void ReportInfoToDb(BufrRadiMMDecoder bufrDecoder,String tableSection, String fileName,byte[] dataBytes,Date recv) {
		 	int successOrNot=bufrDecoder.successOrNot;
			DruidPooledConnection report_connection = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mmm:ss");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//			File file = new File(fileName);
//			String filename = file.getName();
//			Date recv = new Date(file.lastModified());
			String ReportTableName=StartConfig.reportTable();
//			String ReportTableName="RADI_WEA_GLB_BULL_BUFR_TAB";
//			String sod_code="D.0001.0006.S001";
			// cuihongyuan 2019-11-21
			String sod_code = "D.0001.0003.S002";
			//  chy 去掉 D_RECORD_ID
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
			int length=0;
			if(StartConfig.getDataPattern()==0){
			  length=dataBytes.length;
			}else{
				File file=new File(fileName);
				length=(int) file.length();
			}
			
			PreparedStatement statement = null;
			try {
				report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
				statement = new LoggableStatement(report_connection, sql);
				report_connection.setAutoCommit(false);
				StatDi di = new StatDi();
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setTT("");
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(StartConfig.ctsCode());
				di.setFILE_NAME_N(fileName);
				di.setFILE_NAME_O(fileName);
				
				if(successOrNot==1){//解码成功
					if(bufrDecoder.parseList.size()>0){//解码有值
						for(Map<String, Object> members: bufrDecoder.parseList){
							//查站号
							String stafxy="0-1-1-0";
							String sta="999999";
							if(members.get(stafxy)!=null){
								sta=((BufrBean)(members.get(stafxy))).getValue().toString();
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
							 
							 
							//查datetime
							StringBuffer datetime=new StringBuffer();//存单个datetime
							String[] date_time={"9999","01","01","00","00","00"};
							for(int i=0;i<6;i++){
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
							datetime.append(date_time[0]).append("-").append(date_time[1]).append("-").append(date_time[2]).append(" ").append(date_time[3]).append(":").append(date_time[4]).append(":").append(date_time[5]);
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
//						byte[] databyte=getContent(fileName);//报文二进制数组
						int ii = 1;
//						statement.setString(ii++, station_1 + "_" + sdf.format(datetime1));// d_record_id
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
						statement.setLong(ii++, length);//V_LEN    BUFR报文长度 ?????????????
						statement.setLong(ii++, length);//V_file_size 文件大小
						statement.setBytes(ii++, dataBytes);//V_BULLETIN_B 公报内容
//							statement.setBinaryStream(ii++, new ByteArrayInputStream(databyte));
						statement.setString(ii++,fileName);//V_FILE_NAME 文件名
						statement.setInt(ii++, bufrDecoder.parseList.size());//V_NUM 观测记录数
						statement.setString(ii++,stations);//V01301_list 观测记录站号列表
						statement.setString(ii++,datetimes);//d_datetime_list 观测记录时间列表
						statement.setInt(ii++,1);//V_decode_flag 解码是否成功标志
						
						di.setDATA_TIME(TimeUtil.date2String(datetime1, "yyyy-MM-dd HH:mm"));
						di.setIIiii(station_1);
						di.setLATITUDE("");
						di.setLONGTITUDE("");
						di.setHEIGHT("");
						di.setDATA_UPDATE_FLAG(V_BBB);
						
					}else{
						
					}
				}else if(successOrNot==2){//解码失败
					V_BBB=bufrDecoder.V_BBB;
					V_CCCC=bufrDecoder.V_CCCC;
					V_TT=bufrDecoder.V_TTAAii;
					V_YYGGgg=bufrDecoder.V_YYGGgg;
//					byte[] databyte=getContent(fileName);//报文二进制数组
					int ii = 1;
//					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
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
						statement.setLong(ii++, length);//V_LEN    BUFR报文长度 ?????????????
					}else{//--------------报文不包含正常的BUFR消息，即文件打不开，或空文件
						statement.setLong(ii++, 0);//V_LEN    BUFR报文长度 ?????????????
					}
					statement.setLong(ii++, length);//V_file_size 文件大小
					statement.setBytes(ii++, dataBytes);//V_BULLETIN_B 公报内容
					statement.setString(ii++,fileName);//V_FILE_NAME 文件名
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
					di.setDATA_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm"));
					di.setIIiii("999999");
					di.setLATITUDE("");
					di.setLONGTITUDE("");
					di.setHEIGHT("");
					di.setDATA_UPDATE_FLAG(V_BBB);
				}else {//未做解码， 即文件不存在
					byte[] databyte=new byte[0];
					int ii = 1;
//					statement.setString(ii++, "999999" + "_" + sdf.format(new Date()));// d_record_id
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
					statement.setString(ii++,fileName);//V_FILE_NAME 文件名
					statement.setInt(ii++, 0);//V_NUM 观测记录数
					statement.setString(ii++,null);//V01301_list 观测记录站号列表
					statement.setString(ii++,null);//d_datetime_list 观测记录时间列表
					statement.setInt(ii++,0);//V_decode_flag 解码是否成功标志
					di.setDATA_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm"));
					di.setIIiii("999999");
					di.setLATITUDE("");
					di.setLONGTITUDE("");
					di.setHEIGHT("");
					di.setDATA_UPDATE_FLAG(V_BBB);
				}

				di.setTRAN_TIME(TimeUtil.date2String(recv, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setFILE_SIZE(String.valueOf(length));
				di.setBUSINESS_STATE("1");
				di.setPROCESS_STATE("1");
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				DI.add(di);
				try{
					statement.execute();
					report_connection.commit();
					infoLogger.info("ReportInfoToDb 数据入报告表"+StartConfig.reportTable()+"成功！:"+fileName);
				}catch (Exception e) {
					di.setPROCESS_STATE("0");
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
//		public byte[] getContent(String filePath)throws IOException {  
//	        File file = new File(filePath);  
//	        long fileSize = file.length();  
//	        if (fileSize > Integer.MAX_VALUE) {  
//	            System.out.println("file too big...");  
//	            return null;  
//	        } 
//	       
//	        FileInputStream fi = new FileInputStream(file);  
//	        byte[] buffer = new byte[(int) fileSize];  
//	        int offset = 0;  
//	        int numRead = 0;  
//	        while (offset < buffer.length  
//	        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
//	            offset += numRead;  
//	        }  
//	        // 确保所有数据均被读取  
//	        if (offset != buffer.length) {  
//	        throw new IOException("Could not completely read file "  + file.getName());  
//	        }  
//	        fi.close();  
//	        return buffer;  
//	    }	

}
