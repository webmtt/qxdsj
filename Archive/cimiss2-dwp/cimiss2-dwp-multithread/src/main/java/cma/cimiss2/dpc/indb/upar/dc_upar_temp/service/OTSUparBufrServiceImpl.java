package cma.cimiss2.dpc.indb.upar.dc_upar_temp.service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.cimiss2.dwp.tools.utils.UparLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrUparCHNDecoder;
import com.hitec.bufr.util.StringUtil;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class OTSUparBufrServiceImpl {
	final  String MissingValue = "999999";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public BufrUparCHNDecoder decoder = new BufrUparCHNDecoder();
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	private  List<StatDi> DI = new ArrayList<StatDi>();
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


	public static void main(String[] args) {
		long start = System.currentTimeMillis();
//		String tableSection = "B.0001.0030.R001";
		String fileName = "D:\\tt2\\Z_UPAR_I_57328_20190221123859_O_TEMP-L-00_19022112_PQC.BIN";
		OTSUparBufrServiceImpl impl3 = new OTSUparBufrServiceImpl();
		String cts = "B.0001.0030.R003";
		impl3.decodeFile(fileName, cts);
		
		System.out.println("解码、入库总共耗时：" + (System.currentTimeMillis() - start));
		
	}
	
	
	public  boolean decodeFile(String filename, String cts_code) {
		DruidPooledConnection connection = null;
		connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		
		List<String> tableList = new ArrayList<String>();
		tableList = BufrConfig.getTableMap().get(cts_code);
		//Z_UPAR_I_59265_20181205005759_O_TEMP-L-00_18120500_PQC.BIN
		// tableList 已经排序，要素表在前，键表在后。
		boolean isDecode = isDecode(filename,  "UPAR_WEA_CBF_MUL_NSEC_K_TAB",  connection);
//		boolean isDecode=true;
		if(isDecode){
			Map<String, XmlBean> configMap = BufrConfig.get(cts_code);
			decoder.decodeFile(filename);
			try{
				UparLevel uparLevel = new UparLevel();
				for (Map<String, Object> parseMap : decoder.parseList) {
					for (int i = 0; i < tableList.size(); i++) {
						String tableName = tableList.get(i);
						if(tableName.equalsIgnoreCase("UPAR_WEA_CBF_MUL_NSEC_TAB")){
							processSecondElementTable(cts_code,connection, uparLevel, filename, tableName, parseMap, configMap);
						}else if(tableName.equalsIgnoreCase("UPAR_WEA_GBF_MUL_FTM_TAB")){
							processSecondElementTable(cts_code, connection, uparLevel, filename, tableName, parseMap, configMap);
						}
						else {
							processNormalTable(cts_code, uparLevel, connection, filename, tableName, parseMap, configMap);
						}
					}
				}
				return true;
			}catch (Exception e) {
				System.out.println(e.getMessage());
				infoLogger.error(e.getMessage());
				e.printStackTrace();
				return false;
			}finally{
				try{
					connection.close();
				}catch (Exception e) {
					infoLogger.error("connection.close() Failed!");
				}
			}
		}
		else{
			try{
				connection.close();
			}catch (Exception e) {
				infoLogger.error("connection.close() Failed!");
			}
			return true;
		}
	}
	
	private boolean isDecode(String filename, String table, DruidPooledConnection connection){
		SimpleDateFormat format = new  SimpleDateFormat("yyyyMMddHH");
		//Z_UPAR_I_59265_20181205005759_O_TEMP-L-00_18120500_PQC-CCB.BIN
		File file = new File(filename);
		String timestr = "";
		String prioFromDB = "";
		String bbbFromDB = "";
		String bbbFromFile = "000";
		if(file.exists()){
			String fileSimple = file.getName();
			String []sp = fileSimple.split("_|\\.|-");
			if(sp.length >= 12){
				String station = sp[3].trim();
				String prioFromFile = sp[8].trim();
				String datetimeStr = "20" + sp[9].trim();
				Statement statement = null;
				ResultSet resultSet = null;
				try{
					Date time = format.parse(datetimeStr);
					timestr = simpleDateFormat2.format(time);
					if(sp.length == 13){
						bbbFromFile = sp[11].trim();
					}
					
					String query = "select v_priority, v_bbb from " + table + 
							" where V01301=" + "'" + station + "'" + " and d_datetime = " + "'" + timestr + "'";
						statement = connection.createStatement();
						resultSet = statement.executeQuery(query);
						if(resultSet.next()){
							prioFromDB = resultSet.getString(1);
							bbbFromDB = resultSet.getString(2);
						}
						//优先级高低：00>10>50
						if(prioFromDB.isEmpty() || (!prioFromDB.isEmpty() && prioFromDB.compareTo(prioFromFile) > 0)){
							return true; //未入库,需要入库 ,或者 待处理文件优先级高，需要入库（delete再入库）
						}else if(prioFromDB.compareTo(prioFromFile) == 0){
							// 已入库，但是待入库文件优先级等于库中记录
							if(sp.length == 12)
								return false; // 待处理文件不是更正报，不再处理
							// 待处理文件是更正报，且更正报标识大于已入库
							else if(bbbFromDB.compareTo(bbbFromFile) < 0){ 
								// 是处理文件是更正报并且，更正标识较大,进行更正
								return true;
							}
							else //待处理文件更正标识较小
								return false;
						}
						else{
							// 已入库，但是待入库文件优先级低于库中记录，不再处理文件
							return false;
						}
						
				}catch (Exception e) {
					infoLogger.error(e.getMessage() + "\n" + filename);
					return false;
				}
				finally{
					try {
						if (resultSet != null) 
							resultSet.close();
						resultSet = null;
					} catch (SQLException e) {
						infoLogger.error("	resultSet.close() " + filename);
					}
					try{
						if(statement != null)
							statement.close();
					}catch (Exception e) {
						infoLogger.error("statement.close() Failed! "  + filename);
					}
				}
			}
			else{
				infoLogger.error("文件名格式异常！ " + filename);
				return true;
			}
		}
		else {
			return false;
		}
		
	}
	
	// 处理入库除了要素表之外的表
	private void processNormalTable(String cts, UparLevel uparLevel, DruidPooledConnection connection, String filename, String tableName, Map<String, Object> members, Map<String, XmlBean> configMap) {
		// 获取文件 优先级信息
		int l = filename.toUpperCase().indexOf("-L-");
		String Priority = filename.substring(l + 3, l + 5);
		
		StringBuffer valBuffer = new StringBuffer();
		StringBuffer keyBuffer = new StringBuffer();
		XmlBean xmlBean = configMap.get(tableName);
		Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
		Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
		Statement statement = null;
		if(helpMap != null)
			entityMap.putAll(helpMap);
		
		
		// DI 初始化
		StatDi di = new StatDi();
		di.setPROCESS_START_TIME(TimeUtil.getSysTime());
		di.setDATA_TYPE_1(cts);
		String D_record_id = "";
		// 新加 2019 年1月24
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
//			if("V20011".equals(column)||"V20013".equals(column)||"V20350_11".equals(column)||"V20350_12".equals(column)||"V20350_13".equals(column)){
//				System.out.println(121212112);
//			}
//			if("V07030".equals(column)){
//				System.out.println(000);
//			}
//			if("V02067".equals(column)){
//			System.out.println(000);
//		    }
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
				if(column.equalsIgnoreCase("V31001") && tableName.toUpperCase().contains("MUL_NSEC_K_TAB")){
					value = String.valueOf(levelNumOfNSEC);
				}
				if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
					value = members.get("CCCC");
				if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT")){
					value = members.get("TTAAii");
				}
				if(column.equalsIgnoreCase("V_BBB")){
					value = members.get("BBB");
					di.setDATA_UPDATE_FLAG(value.toString());
				}
				if(column.equalsIgnoreCase("V_PRIORITY"))
					value = Priority;
				// 将世界时转化为北京时
				if(column.equalsIgnoreCase("V04300_017_02")){
					Calendar calendar2 = Calendar.getInstance();
					try {
						calendar2.setTime(simpleDateFormat.parse(value.toString()));
						calendar2.add(Calendar.HOUR, 8);
						value = simpleDateFormat.format(calendar2.getTime());
						calendar2 = null;
					} catch (ParseException e) {
						calendar2 = null;
						infoLogger.error("施放时间世界时转化为地方时错误！");
					}
				}
				// 资料时间规整
				if(column.equalsIgnoreCase("D_DATETIME")){
					value = simpleDateFormat2.format(calendar.getTime());
				}
				else if(column.equalsIgnoreCase("D_RECORD_ID")){
					D_record_id = value.toString();
					String dt = D_record_id.split("_")[0];
					D_record_id = D_record_id.replace(dt, simpleDateFormat.format(calendar.getTime()));
					value = D_record_id;
				}else if(column.equalsIgnoreCase("V02421")){
					BufrBean bufryear = (BufrBean)(members.get("0-4-1-0"));
					String year=String.valueOf(bufryear.getValue());
					int idx1=year.indexOf(".");
					if(idx1>=0){
						 year=year.split("\\.")[0];
					}
					BufrBean bufrmonth = (BufrBean)( members.get("0-4-2-0"));
					String month=String.valueOf(bufrmonth.getValue());
					int idx2=month.indexOf(".");
					if(idx2>=0){
						month=month.split("\\.")[0];
					}
					int imonth=Integer.parseInt(month);
					month=String.format("%02d", imonth);
					
					BufrBean bufrday = (BufrBean)( members.get("0-4-3-0"));
					String day=String.valueOf(bufrday.getValue());
					int idx3=day.indexOf(".");
					if(idx3>=0){
						day=day.split("\\.")[0];
					}
					int iday=Integer.parseInt(day);
					day=String.format("%02d", iday);
					
					if(!year.startsWith("9999")&&!month.startsWith("9999")&&!day.startsWith("9999")){
						value=year+month+day;
					}
					if(!year.startsWith("9999")&&!month.startsWith("9999")&&day.startsWith("9999")){
						value=year+month;
					}
					if (year.startsWith("9999")||month.startsWith("9999")) {
						value = "999999";
					}
					
				}
				else if(column.equalsIgnoreCase("V01300")){
					 Pattern pattern = Pattern.compile("[0-9]*");
			         Matcher isNum = pattern.matcher(((BufrBean)members.get("0-1-1-0")).getValue().toString());
			           if( !isNum.matches() ){
			        	   value = 999999;
			           }
				}else if(column.equalsIgnoreCase("D_SOURCE_ID")){
					value = cts + "_" + filename;
				}
				if(tableName.toUpperCase().contains("UPAR_WEA_CBF_PARA_TAB")){
					if (column.equalsIgnoreCase("D_DATA_DPCID")){
						if("B.0001.0030.R001".equals(cts)){
							value="B.0008.0001.P002";
						}else if("B.0001.0030.R002".equals(cts)){
							value="B.0008.0001.P003";
						}else if("B.0001.0030.R003".equals(cts)){
							value="B.0008.0001.P004";
						}
					}
				}
				
				if(tableName.toUpperCase().contains("UPAR_WEA_GBF_MUL_FTM_K_TAB")){
					if (column.equalsIgnoreCase("D_DATA_DPCID")){
						if("B.0001.0030.R001".equals(cts)){
							value="B.0011.0001.P011";
						}else if("B.0001.0030.R002".equals(cts)){
							value="B.0011.0001.P012";
						}else if("B.0001.0030.R003".equals(cts)){
							value="B.0011.0001.P013";
						}
					}
				}
				
				if(tableName.toUpperCase().contains("UPAR_WEA_GBF_MUL_FTM_K_TAB"))
					// 层次值赋值
					value = getLevel(column, uparLevel, value);
				
				keyBuffer.append(",`").append(column).append("`");
				valBuffer.append(",'").append(value.toString().trim()).append("'");
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

			// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
			value = calExpressionValue(obj, members, entityMap, entityBean);
			if(column.equalsIgnoreCase("V04001_02")){
				value = calendar.get(Calendar.YEAR);
			}else if(column.equalsIgnoreCase("V04002_02")){
				value = calendar.get(Calendar.MONTH) + 1;
			}else if(column.equalsIgnoreCase("V04003_02")){
				value = calendar.get(Calendar.DAY_OF_MONTH);
			}else if(column.equalsIgnoreCase("V04004_02")){
				value = calendar.get(Calendar.HOUR_OF_DAY);
			}else if(column.equalsIgnoreCase("V02011")){ //无线电探空仪的类型（代码表）
				try{
					int v = (int) Double.parseDouble(value.toString());
					if(v < 0){
						v += 256;
						value = v;
					}
				}catch (Exception e) {
					infoLogger.error("V02011 （无线电探空仪的类型）字段解析出来不是数值，有误！" + filename);
				}
			}
				
			if(helpMap != null){
				if(!helpMap.containsKey(column)){
					keyBuffer.append(",`").append(column).append("`");
//					System.out.println("column is :"+column+"   value is :"+value);
					this.appendValue(valBuffer, datatype, value.toString().trim());
				}
			}
			else{
				keyBuffer.append(",`").append(column).append("`");
				this.appendValue(valBuffer, datatype, value.toString().trim());
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
		String sql = "insert into " + tableName //
				+ " (" //
				+ keyBuffer.toString().substring(1) //
				+ ") values ("//
				+ valBuffer.toString().substring(1) //
				+ ")";
		 
		String deleteSQL = "Delete from " + tableName + " where D_Record_id = " + "'" + D_record_id +  "'" ;
		try{
			statement = connection.createStatement();
			try{
				// 先删除
				if(connection.getAutoCommit() == false)
					connection.setAutoCommit(true);
				statement.execute(deleteSQL);
			}catch (Exception e) {
				infoLogger.info("删除失败：" + deleteSQL + "\n " + e.getMessage());
			}
			try {// 插入
				if(connection.getAutoCommit() == false)
					connection.setAutoCommit(true);
				statement.execute(sql);
				infoLogger.info("成功插入一条，" + tableName + " "+ filename);
			} catch (MySQLIntegrityConstraintViolationException e) {
				infoLogger.error("主键冲突: " + sql + "\n" + e.getMessage());
				di.setPROCESS_STATE("0");
				e.printStackTrace();
			}catch (Exception e) {
				infoLogger.error("插入失败:" + sql + "\n " + e.getMessage());
				di.setPROCESS_STATE("0");
				e.printStackTrace();
			}
		}catch (Exception e) {
			infoLogger.error("connection.createStatement() failed!" );
			e.printStackTrace();
		}
		finally{
			try{
				statement.close();
			}catch (Exception e) {
				infoLogger.error("statement.close() Failed!");
				e.printStackTrace();
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
	private void processSecondElementTable(String cts_code, DruidPooledConnection connection, UparLevel uparLevel, String fileName, String table, Map<String, Object> parseMap, Map<String, XmlBean> configMap) {
		StringBuffer headercol = new StringBuffer();
		StringBuffer headerval = new StringBuffer();
		// 秒级探空次数（要素表记录数）
		String record_id = "";
		record_id = processheader(cts_code, fileName, table, headercol, headerval, parseMap, configMap);
		Long tLong = System.currentTimeMillis();
		if((!record_id.equals("")) && table.toUpperCase().contains("_NSEC_"))
			processsSecondElementNSEC(connection, record_id, fileName, table, headercol, headerval, parseMap, configMap);
		else if((!record_id.equals("")) && table.toUpperCase().contains("_FTM_"))
			processsSecondElementFTM(connection, uparLevel, record_id, fileName, table, headercol, headerval, parseMap, configMap);
		
		System.out.println("要素表构建SQL+入库耗时：" + (System.currentTimeMillis() - tLong));
		
	}

	private String processheader(String cts_code, String fileName, String table, StringBuffer headercol, StringBuffer headerval, Map<String, Object> parseMap, Map<String, XmlBean> configMap) {
		XmlBean xmlBean = configMap.get(table);
		Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
		Map<String, EntityBean> helpMap = xmlBean.getHelpMap();
		
		if(helpMap != null)
			entityMap.putAll(helpMap);
		int l = fileName.toUpperCase().indexOf("-L-");
		String Priority = fileName.substring(l + 3, l + 5);
		String d_record_id = "";
		
		
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
						value = MissingValue;
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
				if(column.equalsIgnoreCase("V_PRIORITY"))
					value = Priority;
				// 资料时间规整
				if(column.equalsIgnoreCase("D_DATETIME")){
					value = simpleDateFormat2.format(calendar.getTime());
				}else if(column.equalsIgnoreCase("D_RECORD_ID")){
					d_record_id = value.toString();
					String dt = d_record_id.split("_")[0];
					d_record_id = d_record_id.replace(dt, simpleDateFormat.format(calendar.getTime()));
					value = d_record_id;
				}
				else if(column.equalsIgnoreCase("V01300")){
					 Pattern pattern = Pattern.compile("[0-9]*");
			         Matcher isNum = pattern.matcher(((BufrBean)parseMap.get("0-1-1-0")).getValue().toString());
			           if( !isNum.matches() ){
			        	   value = 999999;
			           }
				}
				else if(column.equalsIgnoreCase("D_SOURCE_ID")){
					value = cts_code + "_" + fileName;
				}
				headercol.append(",`").append(column).append("`");
				headerval.append(",'").append(value.toString().trim()).append("'");
				
				if(column.equalsIgnoreCase("D_RECORD_ID"))
					d_record_id = value.toString();
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
			}
			
		} // end while
		return d_record_id;
	}

	//FTM 要素表SQL，返回层次数对象
	private UparLevel processsSecondElementFTM(DruidPooledConnection connection, UparLevel uparLevel, String record_id, String fileName, String table, StringBuffer headercol, StringBuffer headerval, Map<String, Object> parseMap, Map<String, XmlBean> configMap){
		List<String> sqlList = new ArrayList<>();
		XmlBean xmlBean = configMap.get(table);
		Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
		Statement statement = null;
		Boolean isDeleteExecute = false;
		try{
			statement = connection.createStatement();
			for(String groupKey : groupMapList.keySet()){
				Map<String, EntityBean> group = groupMapList.get(groupKey);
				String groupFXY = groupKey.split("_")[0];
				List<Map<String, BufrBean>> BufrBeanList = (List<Map<String, BufrBean>>) parseMap.get(groupFXY);
				if(BufrBeanList==null){
					continue;
				}
				int count = 0;
//				for (Map<String, BufrBean> map : BufrBeanList) {
				Map<String, BufrBean> map = null;
				for (int bufr = 0; bufr < BufrBeanList.size(); bufr ++) {
					map = BufrBeanList.get(bufr);
					StringBuffer keyBuffer = new StringBuffer();
					StringBuffer valBuffer = new StringBuffer();
					
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
							else
								val = bean.getDefaultValue();
							
							keyBuffer.append(",`").append(col).append("`");
							valBuffer.append(",'").append(val.toString().trim()).append("'");
						}else{
							String col = bean.getColumn();
							String fxy = bean.getFxy();
							String datatype = bean.getDatatype(); 
							String exp = bean.getExpression();
							Object val = null;
							if("V08042".equals(col)){
								System.out.println(0000000000);
							}
							if(col.equalsIgnoreCase("D_ELE_ID")){
								val = record_id ;
								String []exprSp = exp.split(",");
								for(int e = 0; e < exprSp.length; e ++){
									if(map.get(exprSp[e].trim()) != null)
										val += "_" + map.get(exprSp[e].trim()).getValue().toString();
									else {
										val += "_" + MissingValue;
									}
								}
							}
							else if(col.equalsIgnoreCase("V12001") || col.equalsIgnoreCase("V12003")){
								BufrBean bufrBean = map.get(fxy);
								if(bufrBean != null){
									val = bufrBean.getValue();
									if(val.toString().startsWith("-0.01"))
										val = MissingValue;
									else{
										if(exp != null && !exp.isEmpty() && !val.toString().startsWith("999999"))
											val = getExprValue(exp, val, datatype);
									}
								}
								else
									val = bean.getDefaultValue();
							}
							else if(col.equalsIgnoreCase("V12301")){
								BufrBean bufrBean1 = map.get("0-12-101-0");//温度
								BufrBean bufrBean2 = map.get("0-12-103-0");//露点温度
								if (bufrBean1 !=null&&bufrBean2 !=null&& (!bufrBean1.getValue().toString().startsWith("999999"))&&(!bufrBean2.getValue().toString().startsWith("999999"))){
									val=Double.parseDouble(bufrBean1.getValue().toString())-Double.parseDouble(bufrBean2.getValue().toString())-273.15;
								}else{
									val = bean.getDefaultValue();
								}
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
						}
					}
					
					keyBuffer.append(headercol.toString());
					valBuffer.append(headerval.toString().trim());
					
					String sql = "insert into " + table //
							+ " (" //
							+ keyBuffer.toString().substring(1) //
							+ ") values ("//
							+ valBuffer.toString().substring(1) //
							+ ")";
					
					System.out.println(sql);
					// 如果未执行，那么先执行delete
					if(isDeleteExecute == false){
						String deleteSQL = "DELETE from " + table + " where d_record_id = " + "'" + record_id + "'";
						try{
							if(connection.getAutoCommit() == false)
								connection.setAutoCommit(true);
							statement.execute(deleteSQL);
							isDeleteExecute = true;
						}catch (Exception e) {
							infoLogger.error("要素表记录删除失败：" + fileName + "\n" + deleteSQL + "\n" +  e.getMessage());
						}
					}
					
					// 该表会出现入库冲突的情况，所以逐条插入
//					try{
//						if(connection.getAutoCommit() == false)
//							connection.setAutoCommit(true);
//						statement.execute(sql);
//					}catch (Exception e) {
//						infoLogger.error("FTM 要素表 插入失败：" + sql + "\n" + e.getMessage());
//					}
					sqlList.add(sql); 
					statement.addBatch(sql);
					count ++;
					if(count == 100 || bufr == BufrBeanList.size() - 1){
						
						try{
							if(connection.getAutoCommit() == true)
								connection.setAutoCommit(false);
							statement.executeBatch();
							connection.commit();
							infoLogger.info("FTM 要素表批量 插入成功，插入条数 =" + count + " " + fileName);
						}catch (Exception e) {
							infoLogger.error("FTM 要素表批量 插入失败：" + fileName + "\n" + e.getMessage());
							// 该表会出现入库冲突的情况，所以逐条插入
							if(connection.getAutoCommit() == false)
								connection.setAutoCommit(true);
							for(int s = 0; s < sqlList.size(); s ++){
								try{
									statement.execute(sqlList.get(s));
									infoLogger.info("FTM 要素表插入一条记录成功！");
								}catch (Exception e1) {
									infoLogger.error("FTM 要素表 插入失败：" + sqlList.get(s) + "\n" + e1.getMessage());
								}
							}
						}
						sqlList.clear();
						statement.clearBatch();
						count = 0;
					}
				}// end BufrBeanList
			}// end groupMapList
//			try{
//				if(connection.getAutoCommit() == true)
//					connection.setAutoCommit(false);
//				statement.executeBatch();
//				connection.commit();
//				infoLogger.info("FTM 要素表批量 插入成功：" + fileName);
//			}catch (Exception e) {
//				infoLogger.error("FTM 要素表批量 插入失败：" + "\n" + e.getMessage());
//				// 该表会出现入库冲突的情况，所以逐条插入
//				if(connection.getAutoCommit() == false)
//					connection.setAutoCommit(true);
//				for(int s = 0; s < sqlList.size(); s ++){
//					try{
//						statement.execute(sqlList.get(s));
//						infoLogger.info("FTM 要素表插入一条记录成功！");
//					}catch (Exception e1) {
//						infoLogger.error("FTM 要素表 插入失败：" + sqlList.get(s) + "\n" + e1.getMessage());
//					}
//				}
//			}
		}
		catch (Exception e) {
			infoLogger.error(e.getMessage());
			e.printStackTrace();
		}
		finally{
			try{
				statement.close();
			}
			catch (Exception e) {
				infoLogger.info("statement.close() failed!");
			}
		}
		return uparLevel;
	}
	// NSEC 要素表SQL， 返回探测次数（要素表记录数）
	private void processsSecondElementNSEC(DruidPooledConnection connection,String record_id, String fileName, String table, StringBuffer headercol, StringBuffer headerval, Map<String, Object> parseMap, Map<String, XmlBean> configMap) {
		int threadSize = 100;
		XmlBean xmlBean = configMap.get(table);
		Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
		boolean isDeleteExecute = false;
		
		for(String key : groupMapList.keySet()){
			String groupFXY = key.split("_")[0];
			List<Map<String, BufrBean>> list = (List<Map<String, BufrBean>>) parseMap.get(groupFXY);
//			System.out.println(groupFXY);
			if(list==null){
				continue;
			}
			int dataSize = list.size();
			if(dataSize > 0){
				levelNumOfNSEC = dataSize;
				// 线程数
				int threadNum = dataSize / threadSize + 1;
				// 创建一个线程池
				ExecutorService exec = Executors.newFixedThreadPool(threadNum);
				List<Callable<List<String>>> tasks = new ArrayList<Callable<List<String>>>();
				Callable<List<String>> task = null;
				List<Map<String, BufrBean>> cutList = null;
				boolean special = dataSize % threadSize == 0;
				Long startT = System.currentTimeMillis();
				for (int i = 0; i < threadNum; i++) {
					if (i == threadNum - 1) {
						if (special) {
							break;
						}
						cutList = list.subList(threadSize * i, dataSize);
					}else {
						cutList = list.subList(threadSize * i, threadSize * (i + 1));
					}
					final List<Map<String, BufrBean>> list2 = cutList;
					task = new Callable<List<String>>() {
					
						@Override
						public List<String> call() throws Exception {
							System.out.println(Thread.currentThread().getName() + "线程：" + list2.size());
							List<String> list = new ArrayList<>();
							Map<String, EntityBean> group = groupMapList.get(key);
							for (Map<String, BufrBean> map : list2) {
								StringBuffer keyBuffer = new StringBuffer();
								StringBuffer valBuffer = new StringBuffer();
								
								try {
									for(String key : group.keySet()){
										EntityBean bean = group.get(key);
										if(bean.isQc()){
											String col = bean.getColumn();
											String fxy = bean.getFxy();
											BufrBean bufrBean = map.get(fxy);
											Object val = null;
											if(bufrBean != null && bufrBean.getQcValues() != null){
												val = bufrBean.getQcValues().get(0);
											}else{
												val = "9";
											}
											keyBuffer.append(",`").append(col).append("`");
											valBuffer.append(",'").append(val.toString().trim()).append("'");
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
													
//													System.out.println(exprSp[e].trim());
//													System.out.println(map.keySet());
													val += "_" + map.get(exprSp[e].trim()).getValue().toString();
												}
											}
											else if(col.equalsIgnoreCase("V12001") || col.equalsIgnoreCase("V12003")){
												BufrBean bufrBean = map.get(fxy);
												if(bufrBean != null){
													val = bufrBean.getValue();
													if(val.toString().startsWith("-0.01"))
														val = MissingValue;
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
											
											keyBuffer.append(",`").append(col).append("`");
											valBuffer.append(",'").append(val.toString().trim()).append("'");
										}
									}
								} catch (Exception e11) {
									e11.printStackTrace();
								}
								
								keyBuffer.append(headercol.toString());
								valBuffer.append(headerval.toString().trim());
								
								String sql = "insert into " + table //
										+ " (" //
										+ keyBuffer.toString().substring(1) //
										+ ") values ("//
										+ valBuffer.toString().substring(1) //
										+ ")";
								list.add(sql);
							}
							return list;
						}
					};
					
					tasks.add(task);
				}
				Statement statement = null;
				try {
					statement = connection.createStatement();
					// 如果未执行，那么先执行delete
					if(isDeleteExecute == false){
						String deleteSQL = "DELETE from " + table + " where d_record_id = " + "'" + record_id + "'";
						try{
							if(connection.getAutoCommit() == false)
								connection.setAutoCommit(true);
							statement.execute(deleteSQL);
							isDeleteExecute = true;
						}catch (Exception e) {
							infoLogger.error("要素表记录删除失败, " + fileName +"：\n" + deleteSQL + "\n" +  e.getMessage());
						}
					}
					List<Future<List<String>>> results = exec.invokeAll(tasks);
					
					for(Future<List<String>> future: results){
						try {
							List<String> sqList = future.get();
							for(String sql : sqList){
								statement.addBatch(sql);
							}
							try{
								if(connection.getAutoCommit() == true)
									connection.setAutoCommit(false);
								statement.executeBatch();
								connection.commit();
								infoLogger.info("NSEC 要素表 批量提交成功 ！" + fileName);
							}catch (Exception e) {
								infoLogger.error("NSEC 要素表 批量提交失败 ！" + fileName);
								infoLogger.error(e.getMessage());
								if(connection.getAutoCommit() == false)
									connection.setAutoCommit(true);
								for(String sql: sqList){
									try{
										statement.execute(sql);
										infoLogger.info("NSEC 要素表插入一条记录成功！" + fileName);
									}catch (Exception e1) {
										infoLogger.error("NSEC 要素表单条插入失败！"  + fileName);
										infoLogger.error(e1.getMessage());
									}
								}
							}
							try{
								statement.clearBatch();
							}
							catch (Exception e) {
								infoLogger.error("statement.clearBatch() Failed!  " + fileName);
							}
						} catch (Exception e) {
							e.printStackTrace();
							infoLogger.error(fileName + "  " + e.getMessage());
						}
						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					infoLogger.error(fileName + "  " +e.getMessage());
				}
				try{
					exec.shutdown();
				}catch (Exception e) {
					e.printStackTrace();
					infoLogger.error("exec.shutdown() Failed! ");
				}
				finally{
					try{
						statement.close();
					}catch (Exception e) {
						infoLogger.error("statement.close() Failed!");
					}
				}
			}
		}
	}

	public  void decodeFile(String filename, byte[] dataBytes) {
		decoder.decodeFile(filename, dataBytes);
		for (Map<String, Object> parseMap : decoder.parseList) {
			
		}
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
//					return entityBean.getDefaultValue();
				} else {
					// 如果不为空，获取其对应的值
					String columnValue = getColumnValue(obj2, ebean).toString();
					
//					if (columnValue.equals(ebean.getDefaultValue())) {
//						expression = expression.replaceFirst(cExpReg, columnValue);
//					}
					if(columnValue.startsWith("999999.0") && ebean.getDefaultValue() != null)
						columnValue = ebean.getDefaultValue();
					if(ebean.getDatatype().equals("int")){
						int idx = columnValue.indexOf(".");
						if(idx >= 1)
							columnValue = columnValue.substring(0, idx);
					}
//					System.out.println(expression + "====" + cExp);
					
					String afterText = expression.substring(expression.indexOf(cExp));
//					String cExpReg = StringUtil.string2Regex(cExp);
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
						try{
							sp[0] = String.format("%d", Integer.parseInt(sp[0]));
						}
						catch (Exception e1) {
							try{
								sp[0] = String.format("%.3f", Double.parseDouble(sp[0]));
							}
							catch (Exception e2) {
								System.out.println(expression);
							}
						}
						expression = expression.replaceFirst(cExpReg, sp[0]);
					}
				}
			}

			// 替换所有默认表达式
//			System.out.println(expression);
			expression = StringUtil.formatExpressionContent(expression);
			// StringUtil.replaceNumberExp(expression, texts);
			// 替换${this}
			Matcher thisMatcher = Pattern.compile(THIS_VALUE_EXP).matcher(expression);
			while (thisMatcher.find()) {
				if (StringUtils.isBlank(value)) {
					return entityBean.getDefaultValue();
				}
				if(StringUtil.string2Regex(thisMatcher.group(0)) != null && value != null)
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
		if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 5){
			calendar.add(Calendar.HOUR_OF_DAY, 1);
		}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 1){
			calendar.add(Calendar.HOUR_OF_DAY, -1);
		}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 2){
			calendar.add(Calendar.HOUR_OF_DAY, -2);
		}
		return calendar.getTime();
	}
}
