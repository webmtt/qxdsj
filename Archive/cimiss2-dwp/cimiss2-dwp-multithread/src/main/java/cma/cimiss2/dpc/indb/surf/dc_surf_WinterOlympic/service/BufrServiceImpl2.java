package cma.cimiss2.dpc.indb.surf.dc_surf_WinterOlympic.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BUFRHourDecoder;
import com.hitec.bufr.util.Calculator;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class BufrServiceImpl2{
	public static final String DESCRIPTOR_REGEX = "\\{(\\d+)-(\\d+)-(\\d+)-(\\d+)\\}";
	public static final Logger Log = LoggerFactory.getLogger("loggerInfo");
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	BUFRHourDecoder bufrDecoder = new BUFRHourDecoder();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	/** The di. */
	private static List<StatDi> DI = new ArrayList<StatDi>();
	/**
	 * Sets the di queues.
	 *
	 * @param diQueue the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueue){
		BufrServiceImpl2.diQueues = diQueue;
	}
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		String tableSection = "A.0001.0042.R003";
		tableList.add("SURF_WEA_CHN_MUL_HOR_WinOlym_TAB");
		
//		String fileName = "D:\\HUAXIN\\dongao\\2020-01-14\\Z_SURF_I_A1490-REG_20200113050000_O_AWS_FTM.BIN";
		String fileName = "D:\\HUAXIN\\dongao\\2020-01-14\\Z_SURF_C_BEPK-REG_20200114000057_O_AWS_FTM.BIN";
		BufrServiceImpl2 bufrServiceImpl2 = new BufrServiceImpl2();
		boolean decode = false;
		Date rcv_time = new Date((new File(fileName)).lastModified());
		decode = bufrServiceImpl2.decode(fileName, tableSection, tableList, rcv_time);
		if(decode){
			System.out.println("入库成功！");
		}else
			System.err.println("入库失败！");
	}

	public boolean decode(String fileName,String tableSection, List<String> tables, Date recv_time) {
		String cts_code = tableSection;
		// 1 解码
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		// 2 入库
		if (isRead) {
			try {
				insertReport(bufrDecoder, fileName, cts_code, recv_time, getContent(fileName));
			} catch (IOException e) {
				Log.error("Read report error!");
			}
			
			return this.decode(recv_time, bufrDecoder, fileName, tableSection, tables);
		} else {
			return false;
		}
	}
	public byte[] getContent(String filePath) throws IOException {  
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
	/**
	 * @Title: getDateTimeStr   
	 * @Description: 资料时间转换  
	 * @param yyyy
	 * @param MM
	 * @param dd
	 * @param hh
	 * @return String      
	 */
	public String getDateTimeStr(String yyyy, String MM, String dd, String hh){
		if(yyyy.startsWith("999999"))
			return "9999-01-01 00:00:00";
		else if(yyyy.length() >= 4){
			yyyy = yyyy.substring(0, 4);
		}
		if(MM.startsWith("999999"))
			MM = "01";
		if(dd.startsWith("999999"))
			dd = "01";
		if(hh.startsWith("999999"))
			hh = "00";
		try{
			int MM_i = (int)Double.parseDouble(MM);
			int dd_i = (int)Double.parseDouble(dd);
			int hh_i = (int)Double.parseDouble(hh);
			return yyyy + "-" + String.valueOf(String.format("%02d", MM_i)) + "-" + String.valueOf(String.format("%02d", dd_i)) 
			+ " "  + String.valueOf(String.format("%02d", hh_i)) + ":00:00";
		}catch (Exception e) {
			return "999999";
		}
		
	}
	/**
	 * @Title: insertReport   
	 * @Description: 报文表入库   
	 * @param bufrDecoder
	 * @param fileName 文件名 不是路径
	 * @param cts_code 
	 * @param recv 接收时间
	 * @param len  消息长度 报文长度
	 */
	public void insertReport(BUFRHourDecoder bufrDecoder, String fileName, String cts_code, Date recv, byte[] msg){
		// 获取数据库连接
		DruidPooledConnection connection = null;
		PreparedStatement statement = null;
		String tableName = "SURF_WEA_CHN_BULL_BUFR_TAB";
		String value = "999999";
		String station = "999999";
		String datatime = simpleDateFormat2.format(new Date());
		Object object = null;
		List<String> datetime_list = new ArrayList<>();
		List<String> station_list = new ArrayList<>();
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			// chy 去掉 D_RECORD_ID
			String sql="insert into "+tableName+"(D_DATA_ID,D_SOURCE_id,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
					+ "V01301,V_BBB,V_CCCC,V_YYGGgg,V_TT,V_LEN,V_FILE_SIZE,V_BULLETIN_B,V_FILE_NAME,V_NUM,V01301_list,d_datetime_list,"
					+ "V_decode_flag) values ("
					+ "?,?,?,?,"
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?,"
					+ "?,?,?,?,?)";
			connection.setAutoCommit(true);
			statement = new LoggableStatement(connection, sql);
			for(Map<String, Object> mems: bufrDecoder.getParseList()){
				station_list.add(((BufrBean)mems.get("0-1-1-0")).getValue().toString());
				datetime_list.add(getDateTimeStr(((BufrBean)(mems.get("0-4-1-0"))).getValue().toString(), 
						((BufrBean)(mems.get("0-4-2-0"))).getValue().toString(), 
						((BufrBean)(mems.get("0-4-3-0"))).getValue().toString(), 
						((BufrBean)(mems.get("0-4-4-0"))).getValue().toString()));
			}
			Map<String, Object> members = null;
			//
			if(bufrDecoder.getParseList() != null && bufrDecoder.getParseList().size() > 0){
				members = bufrDecoder.getParseList().get(0); // 取首个
					// 台站号
				object = members.get("0-1-1-0");
				if(object != null){
					try{
						value = ((BufrBean)object).getValue().toString();
						station = value;
					}catch (Exception e){
						Log.error("Station error: 0-1-1-0");
					}
				}
				int ii = 1;
				//资料时间
				try{
					value = getDateTimeStr(((BufrBean)(members.get("0-4-1-0"))).getValue().toString(), 
							((BufrBean)(members.get("0-4-2-0"))).getValue().toString(), 
							((BufrBean)(members.get("0-4-3-0"))).getValue().toString(), 
							((BufrBean)(members.get("0-4-4-0"))).getValue().toString());
					datatime = value;
				}catch (Exception e) {
					Log.error("DateTime conversion error!");
				}
//				statement.setString(ii ++, station  + "_" + simpleDateFormat.format(simpleDateFormat2.parse(datatime)));
				statement.setString(ii ++, "A.0001.0025.S006");
				statement.setString(ii ++, cts_code);
				statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				statement.setTimestamp(ii++, new Timestamp(recv.getTime()));
				statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				statement.setTimestamp(ii++, new Timestamp(simpleDateFormat2.parse(datatime).getTime()));
				
				statement.setString(ii ++, station);
				statement.setString(ii++, members.get("BBB").toString());
				statement.setString(ii++, members.get("CCCC").toString());
				statement.setString(ii++, members.get("YYGGgg").toString());
				statement.setString(ii++, members.get("TTAAii").toString());
				statement.setInt(ii++, msg.length);
				statement.setInt(ii++, msg.length);
				statement.setBytes(ii++, msg);
				try{
					statement.setString(ii++, (new File(fileName)).getName());
				}catch (Exception e) {
					statement.setString(ii++, fileName);
				}
				statement.setInt(ii++, bufrDecoder.getParseList().size());
				statement.setString(ii++, String.join(",", station_list));
				statement.setString(ii++, String.join(",", datetime_list));
				statement.setInt(ii++, bufrDecoder.isParsedOk);
				try{
					statement.execute();
				}catch (Exception e) {
					Log.error("Insert Report table error: " + e.getMessage());
				}
			}
			else{
				// 解码失败
				int ii = 1;
				//资料时间
//				statement.setString(ii ++, "999999"  + "_" + simpleDateFormat.format(simpleDateFormat2.parse(datatime)));
				statement.setString(ii ++, "A.0001.0025.S006");
				statement.setString(ii ++, cts_code);
				statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				statement.setTimestamp(ii++, new Timestamp(recv.getTime()));
				statement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
				statement.setTimestamp(ii++, new Timestamp(simpleDateFormat2.parse(datatime).getTime()));
				
				statement.setString(ii ++, station);
				statement.setString(ii++, "000");
				statement.setString(ii++, "9999");
				statement.setString(ii++, "999999");
				statement.setString(ii++, "999999");
				statement.setInt(ii++, msg.length);
				statement.setInt(ii++, msg.length);
				statement.setBytes(ii++, msg);
				try{
					statement.setString(ii++, (new File(fileName)).getName());
				}catch (Exception e) {
					statement.setString(ii++, fileName);
				}
				statement.setInt(ii++, -1);
				statement.setString(ii++,null);
				statement.setString(ii++, null);
				statement.setInt(ii++, bufrDecoder.isParsedOk);
				try{
					statement.execute();
				}catch (Exception e) {
					Log.error("Insert Report table error: " + e.getMessage());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			Log.error(fileName + ", " + e.getMessage());
		} 
		finally {
			try {
				if(statement != null)
					statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(connection != null)
					connection.close();
			} catch (Exception e) {
				Log.error("DBCloseError!");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * @Title: decode   
	 * @Description: 入库方法  
	 * @param rcv_time
	 * @param bufrDecoder
	 * @param CCCC
	 * @param fileName
	 * @param tableSection
	 * @param tables
	 * @param DI
	 * @param EI
	 * @return boolean      
	 * @throws：
	 */
	public boolean decode(Date rcv_time, BUFRHourDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		// 获取数据库连接
		DruidPooledConnection connection1 = null;
		DruidPooledConnection connection2 = null;
		DruidPooledConnection connection = null;
		Statement statement = null;
		File file = new File(fileName);
		String temp_tableSection = tableSection;
		Map<String, XmlBean> configMap = BufrConfig.get(temp_tableSection);
		int missedSunRecord = 0;
		boolean dateTimeOK = true;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		try {
			connection1 = ConnectionPoolFactory.getInstance().getConnection("rdb");
			connection1.setAutoCommit(true);
			connection2 = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			connection2.setAutoCommit(true);
			for(Map<String, Object> members: bufrDecoder.getParseList()){
				dateTimeOK = true;
				String NowHour="0";//当前报文的小时
				BufrBean Hourbean=(BufrBean)members.get("0-4-4-0");
				if(Hourbean!=null){
					NowHour=Hourbean.getValue().toString();//获取当前报文的小时
				}
				int NowHourI=(int)Double.parseDouble(NowHour);
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
				StringBuffer hour_tab_update = new StringBuffer();//小时表更新数据
				for (String tableName : tables) {
					try {
						connection = connection1;
						statement = connection.createStatement();
//						connection.setAutoCommit(true);
					} catch (Exception e) {
						Log.error("连接connection(rdb)异常！");
						continue;
					}
					String BBB=members.get("BBB").toString();//更正标识
					String D_RECORD_ID = "";
					String time = "";
					dateTimeOK = true;
					StatDi di = new StatDi();
					di.setPROCESS_START_TIME(dateFormat.format(new Date()));
					
					XmlBean xmlBean = configMap.get(tableName);
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					// 用于存储入库的字段
					StringBuffer keyBuffer = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer = new StringBuffer();
					// 遍历配置信息
					Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
					BufrBean evaporMarker = (BufrBean)members.get("0-2-201-2");
					BufrBean roadStateMaker = (BufrBean)members.get("0-2-201-6");
					BufrBean snowMarker = (BufrBean)members.get("0-2-201-7");
					BufrBean frozenSoil = (BufrBean)members.get("0-2-201-8");
	//				long sqlStart = System.currentTimeMillis();
					//获取每个字段的值
					while (it.hasNext()) {
						Entry<String, EntityBean> next = it.next();
						String column = next.getKey(); // 配置文件配置的字段名称
						EntityBean entityBean = next.getValue(); // 配置的每个字段属性
						String datatype = entityBean.getDatatype(); // 字段类型
						String expression = entityBean.getExpression(); // 此字段的表达式
						String fxy = entityBean.getFxy(); // 解码时存储数据的key
						String defaultValue = entityBean.getDefaultValue(); // 缺省值
						Object obj = members.get(fxy); // 根据fxy获取每一行数据
//						System.out.println(column);
						// 如果获取的对象为null
						Object value = null;
						if(column.equals("V12016")){
							System.out.println("");
						}
						if (obj == null) {
							// 如果表达式不为空说明此值需要根据表达式进行计算value
							if (StringUtils.isNotBlank(expression)) {
								// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
								Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
								if (em.find()) {
									value = defaultValue;
								} else if (Pattern.compile(DESCRIPTOR_REGEX).matcher(expression).find()) {
									if(entityBean.isQc())
										value =  entityBean.getDefaultValue();
										
									else
										value = "999998"; // 未编报
								}else {// 如果表达式存在，且其中没有以index取值
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
							else if(column.equalsIgnoreCase("D_RYMDHM"))
								value = dateFormat.format(rcv_time);
							else if(column.equalsIgnoreCase("D_SOURCE_ID")){
								value = String.valueOf(value) + "_" + file.getName();
							}
							
							// chy  去掉 D_Record_id 拼sql
							if(!column.equalsIgnoreCase("D_Record_id")){
								keyBuffer.append(",`").append(column).append("`");
								if(entityBean.getDatatype()!= null && entityBean.getDatatype().equalsIgnoreCase("double")){
									BigDecimal bigDecimal = (new BigDecimal(value.toString()).setScale(4, BigDecimal.ROUND_HALF_UP)) ;
									value = bigDecimal.toPlainString();
								}
								valueBuffer.append(",'").append(value.toString()).append("'");
							}
							
							if(column.equals("D_RECORD_ID")){
								D_RECORD_ID = value.toString().trim();
							}
							else if(column.equals("D_DATETIME")){
								int idx01 = value.toString().lastIndexOf(":");
								if(idx01 > 16){
									dateTimeOK = false;
									Log.error("资料时间错误！  " + fileName);
									break;
								}
								else{
									Date date = simpleDateFormat2.parse(value.toString());
									try{
										double dayInMonth = Double.parseDouble(((BufrBean)members.get("0-4-4-0")).getValue().toString());
										date.setHours((int)dayInMonth);
									}catch (Exception e) {
										// TODO: handle exception
									}
									di.setDATA_TIME(simpleDateFormat2.format(date).substring(0, idx01));
									time = value.toString();
									
									if(!TimeCheckUtil.checkTime(dateFormat.parse(value.toString()))){
										Log.error("TIME CHECK ERROR! " + fileName);
										dateTimeOK = false;
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
						// obj != null, obj 类型可能为：String\BufrBean\Map,
						value = getValueByMapDirectly(obj, entityBean);
						
						//降水量的处理
						if((column.equalsIgnoreCase("V13019") || column.equalsIgnoreCase("V13020")
								|| column.equalsIgnoreCase("V13021") || column.equalsIgnoreCase("V13022")
								|| column.equalsIgnoreCase("V13023") || column.equalsIgnoreCase("V13011"))&& value.toString().startsWith("-0.1"))
							value = "999990";
						else if(column.equalsIgnoreCase("V13013") && value.toString().startsWith("-1")){//积雪深度为-1时表示积雪微量，赋值999990
							value = "999990";
						}
						else if(column.equalsIgnoreCase("V13013") && value.toString().startsWith("999999") && snowMarker != null && 
								(snowMarker.getValue().toString().equals("0.0") || 
										snowMarker.getValue().toString().equals("3.0") ||
										snowMarker.getValue().toString().equals("4.0") ||
										snowMarker.getValue().toString().equals("5.0") ||
										snowMarker.getValue().toString().equals("6.0") )){//积雪深度
							value = "999998";
						}
						// 风向的处理
						//如果风向，风速均为0，那么静风 999017，
					   //2019-4-8 改为 ：如果风速<=0.2m/s, 风向赋值999017
						else if((column.equalsIgnoreCase("V11290") || column.equalsIgnoreCase("V11292")
								|| column.equalsIgnoreCase("V11201") || column.equalsIgnoreCase("V11001")) ){
								Map<String, BufrBean> wind = (Map<String, BufrBean>)obj;
								String id = expression.replaceAll("-1-", "-2-");
								BufrBean wspeed = wind.get(id.replaceAll("\\{|\\}", ""));
								if(wspeed != null && Double.parseDouble(wspeed.getValue().toString()) <= 0.2)
									value = "999017";
						}
//						else if(column.equalsIgnoreCase("V11296") && Double.parseDouble(value.toString()) == 0){
						else if(column.equalsIgnoreCase("V11296")){
							Map<String, BufrBean> wind = (Map<String, BufrBean>)obj;
							// 取风速
							BufrBean wspeed = wind.get("0-11-42-0");
							if(wspeed != null && Double.parseDouble(wspeed.getValue().toString()) <= 0.2)
								value = "999017";
						}
//						else if(column.equalsIgnoreCase("V11211") && Double.parseDouble(value.toString()) == 0){
						else if(column.equalsIgnoreCase("V11211")){
							Map<String, BufrBean> wind = (Map<String, BufrBean>)obj;
							// 取风速
							BufrBean wspeed = wind.get("0-11-46-0");
							if(wspeed != null && Double.parseDouble(wspeed.getValue().toString()) <= 0.2)
								value = "999017";
						}
//						else if(( column.equalsIgnoreCase("V11503_06") || column.equalsIgnoreCase("V11503_12") ) && Double.parseDouble(value.toString()) == 0){
						else if(( column.equalsIgnoreCase("V11503_06") || column.equalsIgnoreCase("V11503_12")) ){
							//如果风向，风速均为0，那么静风 999017 
							//2019-4-8 改为 ：如果风速<=0.2m/s, 风向赋值999017
							Map<String, BufrBean> wind = (Map<String, BufrBean>)obj;
							// 取风速
							String id = expression.replaceAll("-10-", "-46-");
							BufrBean wspeed = wind.get(id.replaceAll("\\{|\\}", ""));
							if(wspeed != null && Double.parseDouble(wspeed.getValue().toString()) <= 0.2)
								value = "999017";
						}
//						}catch (Exception e) {
//							System.err.println("风向值不为数值型，转换错误！");
//						}
						// added on 2019-01-08
						// 台站名称 或者 分钟连续天气现象  将 ‘ 符号转义
						else if((column.equalsIgnoreCase("V01015") || column.equalsIgnoreCase("V02320")) && value.toString().contains("'")){
							String tmp = value.toString();
							tmp = tmp.replaceAll("'", "''");
							value = tmp;
						}
						// end added
						//冻土深度第1、2层上、下限值
						else if( (column.equalsIgnoreCase("V20330_01") || column.equalsIgnoreCase("V20331_01") ||  
								column.equalsIgnoreCase("V20330_02") || column.equalsIgnoreCase("V20331_02") )){//冻土深度第1、2层上、下限值
							
							if(value.toString().startsWith("999999") && frozenSoil != null && 
								   (frozenSoil.getValue().toString().equals("0.0") || 
									frozenSoil.getValue().toString().equals("3.0") ||
									frozenSoil.getValue().toString().equals("4.0") ||
									frozenSoil.getValue().toString().equals("5.0") ||
									frozenSoil.getValue().toString().equals("6.0")  )){
									value = "999998";
							}
							if(!value.toString().startsWith("99999") && Double.parseDouble(value.toString()) >= 500 && Double.parseDouble(value.toString())<1022){//当大于500且小于1022时，赋值998000+原值-500
								value = 998000 + Double.parseDouble(value.toString()) - 500;
							}
							if( Double.parseDouble(value.toString()) ==1022){//值等于1022时表示微量，赋值999990
								value = "999990";
							}
							if(!value.toString().startsWith("99999") && Double.parseDouble(value.toString()) >1022){//大于1022时赋值999999
								value = "999999";
							}
						}
						// 地面状态
						else if(column.equalsIgnoreCase("V20062") && value.toString().startsWith("999999") && roadStateMaker != null && 
								(roadStateMaker.getValue().toString().equals("0.0") ||
										roadStateMaker.getValue().toString().equals("3.0") ||
										roadStateMaker.getValue().toString().equals("4.0") ||
										roadStateMaker.getValue().toString().equals("5.0") ||
										roadStateMaker.getValue().toString().equals("6.0") ) ){
//							||
//							roadStateMaker.getValue().toString().equals("999999.0")
							value = "999998";
						}
						// 日蒸发量 全球地面逐小时表
						else if(column.equalsIgnoreCase("V13340") && value.toString().startsWith("999999") && evaporMarker != null &&
								(evaporMarker.getValue().toString().equals("0.0") ||
										evaporMarker.getValue().toString().equals("3.0") ||
										evaporMarker.getValue().toString().equals("4.0") ||
										evaporMarker.getValue().toString().equals("5.0") ||
										evaporMarker.getValue().toString().equals("6.0") )){
//							||
//							evaporMarker.getValue().toString().equals("999999.0")
							value = "999998";
						}
						//字符型 ：台站名称（场地名称）、天气现象、分钟天气现象
						else if(column.equalsIgnoreCase("V01015") || column.equalsIgnoreCase("V20304") || column.equalsIgnoreCase("V20211")){ 
							try {
								String staName= "";
								staName = new String(value.toString().getBytes(), "gb2312");
								if(column.equalsIgnoreCase("V20304") && staName.equalsIgnoreCase("NaN"))
									staName = "999999";
								value = staName.replace("\u0000", "").trim().replaceAll("'", "''");
							} catch (UnsupportedEncodingException e) {
								Log.error(column + " 赋值异常：" + fileName + "\n" + e.getMessage());
							}
						}
						// end add
						// chy 去掉 D_record_id 拼sql
						if(!column.equalsIgnoreCase("D_record_id")){
							keyBuffer.append(",`").append(column).append("`");
							this.appendValue(valueBuffer, datatype, value);
						}
						if((tableName.toUpperCase().indexOf("_MUL_HOR_TAB") > -1)&&tableSection.startsWith("BUFR_SURF_WEA_MUL_HOR")){//中国
							String hourData="D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_DATETIME,V01300,V02301,V01301,V_ACODE,V_NCODE,V04001,V04001_02,V04002,V04002_02,V04003,V04003_02,V04004,V04004_02,V04005";
							if(!hourData.contains(column)){
								 if(!entityBean.isQc() && !value.toString().startsWith("99999")){
									updateSql(hour_tab_update, column, datatype, value);
									 EntityBean entityBean2 = entityMap.get("Q" + column.substring(1));
									 if(entityBean2 != null)
										 updateSql(hour_tab_update, entityBean2.getColumn(), entityBean2.getDatatype(), getValueByMapDirectly(obj, entityBean2));
								  }
							}
						}
						if(column.equals("D_DATETIME")){
							int idx01 = value.toString().lastIndexOf(":");
							Date date = simpleDateFormat2.parse(value.toString());
							try{
								double dayInMonth = Double.parseDouble(((BufrBean)members.get("0-4-4-0")).getValue().toString());
								date.setHours((int)dayInMonth);
							}catch (Exception e) {
								// TODO: handle exception
							}
//							di.setDATA_TIME(value.toString().substring(0, idx01));
							di.setDATA_TIME(simpleDateFormat2.format(date).substring(0, idx01));
							time = value.toString();
							if(!TimeCheckUtil.checkTime(dateFormat.parse(value.toString()))){
								Log.error("TIME CHECK ERROR! " + fileName);
								dateTimeOK = false;
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
					} // end while
					
					if(dateTimeOK == false)
						continue;
					di.setDATA_TYPE_1(tableSection);
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
//					System.out.println(sql);
					try {
						// 如果有后到的，删除之前插入的，再插入
						statement.execute(sql);
						Log.info(" \n "+NowHourI+"时成功插入"+tableName+"表一条数据!" + fileName);
					}catch (SQLException e) {
						Log.error("Error:\n " + sql + e.getMessage() + " " + fileName);
						di.setPROCESS_STATE("0");
					}//catch end
				} //table for end
			} // list for end
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			Log.error(fileName + ", " + e.getMessage() );
			return false;
		} finally {
			for (int j = 0; j < DI.size(); j++) {
				diQueues.offer(DI.get(j));
			}
			DI.clear();
			try {
				if(statement != null)
					statement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
//				System.out.println("DB connection = " + connection);
				if(connection != null){
					connection.close();
				}
				if(connection1 != null){
					connection1.close();
				}
				if(connection2 != null){
					connection2.close();
				}
			} catch (Exception e) {
				Log.error("DBCloseError!");
				e.printStackTrace();
			}
			
		}
	}
	
	// 电线积冰现象V20305的处理
	// obj 是一个Map
	private Object getWireIceValue(Object obj, EntityBean entityBean){
		Object value = "999998";
		String expression = entityBean.getExpression(); // {0-20-192-0}*100+{0-20-192-1}
		Map<String, Object> map = (Map<String, Object>)obj;
		if(map != null){
			BufrBean b1 = (BufrBean)map.get("0-20-192-0");
			BufrBean b2 = (BufrBean)map.get("0-20-192-1");
			if(b1 != null && b2 != null){
				String v1 = b1.getValue().toString();
				String v2 = b2.getValue().toString();
				// 两个有效值
				if(!v1.startsWith("999999") && !v2.startsWith("999999")){
					if(!v1.equals(v2.toString())) //不相等
						return Calculator.conversion(v1 + "*100+" + v2);
					else  // 相等
						return v1;
				}//两个缺测
				else if(v1.startsWith("999999") && v2.startsWith("999999")){
					return v1; // 返回缺测值；
				}else{//一个缺测，一个有效值
					if(v1.startsWith("999999"))
						return v2;
					else 
						return v1;
				}
			}else{
				return value;
			}
		}
		else 
			return value;
	}
	
	// 处理obj不为null的情况
	private Object getValueByMapDirectly(Object obj, EntityBean entityBean){
		Object value = "999998";
		String expression = entityBean.getExpression();
		if(entityBean.getColumn().equalsIgnoreCase("V20305")){
			return getWireIceValue(obj, entityBean);
		}
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
					
//				if(value.toString().startsWith("999999"))
//					value = entityBean.getDefaultValue();
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
//								return entityBean.getDefaultValue();
								return value;
						}else if(map.get(fxy) instanceof BufrBean){
							if(!entityBean.isQc()){
								value = ((BufrBean)map.get(fxy)).getValue();
								if(value.toString().startsWith("999999"))
//									return entityBean.getDefaultValue();
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
//		System.out.println(value);
		return value;
	}
	
	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean) {
		String value = null;
		String expression = entityBean.getExpression();
		if (obj != null) {
			expression = getValueByMapDirectly(obj, entityBean).toString();
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
					String columnValue = getValueByMapDirectly(obj2, ebean).toString();
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
			BigDecimal bigDecimal = (new BigDecimal(value.toString())).setScale(4, BigDecimal.ROUND_HALF_UP);
			buffer.append(",").append(bigDecimal.toPlainString());
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
	private void updateSql(StringBuffer buffer, String col, String dataType, Object value) {
		buffer.append(col + "=");
		if ("int".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("short".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("byte".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("double".equalsIgnoreCase(dataType)) {
			BigDecimal bigDecimal = (new BigDecimal(value.toString())).setScale(4, BigDecimal.ROUND_HALF_UP);
			buffer.append(bigDecimal.toPlainString()).append(",");
		} else if ("float".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("long".equalsIgnoreCase(dataType)) {
			buffer.append(value).append(",");
		} else if ("date".equalsIgnoreCase(dataType)) {
			buffer.append("'").append(value).append("'").append(","); // TODO
		} else {
			buffer.append("'").append(value).append("'").append(",");
		}
	}
	//查找库里原有数据
	static	String[] findExistData(String datetimeToday,String D_RECORD_ID,String tableName,Connection connection){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String v_bbb = null;
		String d_datetime=null;
		String rntString [] = {d_datetime, v_bbb};
		String sql = "select D_DATETIME,V_BBB from "+tableName+" "
				+ "where  D_DATETIME = ? and D_RECORD_ID = ? ";
		try{
			if(connection != null){	
				Pstmt = connection.prepareStatement(sql);
				int ii = 1;
				Pstmt.setString(ii++, datetimeToday);//资料时间
				Pstmt.setString(ii++, D_RECORD_ID);//记录标识
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					d_datetime = resultSet.getString(1);
					v_bbb = resultSet.getString(2);
				}
			}
		}catch(SQLException e){
			Log.error("\n findExistData create Statement error " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					Log.error("\n findExistData close Statement error " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}catch(SQLException e){
					Log.error("\n findExistData close resultSet error " + e.getMessage());
				}
			}
		}
		rntString[0] = d_datetime;
		rntString[1] = v_bbb;
		return rntString;
	}

}
