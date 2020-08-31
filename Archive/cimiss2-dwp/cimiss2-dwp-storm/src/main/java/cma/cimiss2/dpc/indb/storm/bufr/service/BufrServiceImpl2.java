package cma.cimiss2.dpc.indb.storm.bufr.service;

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
import com.hitec.bufr.decoder.BUFRMessageDecoder;
import com.hitec.bufr.util.Calculator;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil2;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.core.tools.ConfigurationManager;
import cma.cimiss2.dpc.indb.core.tools.DIEISender;
import cma.cimiss2.dpc.indb.storm.bufr.BufrTopology;
import cma.cimiss2.dpc.indb.storm.tools.BufrConfig;
import cma.cimiss2.dpc.indb.storm.tools.ConnectionPoolFactory;
import cma.cimiss2.dpc.indb.storm.tools.LoggableStatement;

public class BufrServiceImpl2 implements BufrService{
	public static final String DESCRIPTOR_REGEX = "\\{(\\d+)-(\\d+)-(\\d+)-(\\d+)\\}";
	public static final Logger Log = LoggerFactory.getLogger("loggerInfo");
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	BUFRMessageDecoder bufrDecoder = new BUFRMessageDecoder();
	
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		String tableSection = "BUFR_SURF_WEA_MUL_HOR";
		tableSection = "BUFR_SURF_WEA_MUL_MIN";
//		tableList.add("SURF_WEA_CHN_MUL_HOR_TAB");
//		tableList.add("SURF_WEA_GLB_MUL_HOR_TAB");
//		tableList.add("SURF_WEA_CHN_SSD_HOR_TAB");
//		tableList.add("SURF_WEA_CHN_MUL_DAY_TAB");
//		tableList.add("SURF_WEA_C_MUL_FTM_WSET_TAB");
		tableList.add("SURF_WEA_CHN_MUL_MIN_TAB");
		tableList.add("SURF_WEA_CHN_PRE_MIN_TAB");
		
//		tableList.add("SURF_WEA_CBF_MUL_DAY_TAB");
//		String fileName = "D:\\usr\\data\\hor\\Z_SURF_I_59954_20190716100000_O_AWS_FTM_PQC.BIN";
//		String fileName = "D:\\tmp\\surf\\Z_SURF_I_59981_20191205000000_O_AWS_FTM_PQC.BIN";
		String fileName = "D:\\HUAXIN\\empty.bin";
		BufrServiceImpl2 bufrServiceImpl2 = new BufrServiceImpl2();
		boolean decode = false;
		List<StatDi> dis = new ArrayList<>();
		List<RestfulInfo> rests = new ArrayList<>();
		String CCCC = "";
		int useBBB=0;
		Date rcv_time = new Date((new File(fileName)).lastModified());
		decode = bufrServiceImpl2.decode(rcv_time, fileName, CCCC, tableSection, tableList, dis, rests, useBBB);
		if(decode){
			System.out.println("入库成功！");
		}else
			System.err.println("入库失败！");
	}

	@Override
	public boolean decode(Date recv_time, String name, String CCCC, byte[] dataBytes, String tableSection, List<String> tables, List<StatDi> DI, List<RestfulInfo> EI,int useBBB) {
		try {
			String cts_code = "";
			// 1 解码
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (bufrDecoder.isParsedOk == -1){
				try{
					HashMap<String, String> map1 = new HashMap<>();
		            map1.put("message", "Decode failed");
		            map1.put("path",  StringUtil.getConfigPath()); // 默认路径
		            EI.addAll(DIEISender.makeEI(map1));
				}catch (Exception e1) {
					System.out.println("ei error!");
				}
			}
			if (isRead) {
				if(tableSection.toUpperCase().indexOf("MUL_HOR") > -1)
					cts_code = "A.0001.0044.R001";
				else if(tableSection.toUpperCase().indexOf("MUL_MIN") > -1)
					cts_code = "A.0001.0043.R001";
				else if(tableSection.toUpperCase().indexOf("REG_HOR") > -1)
					cts_code = "A.0001.0042.R002";
				else if(tableSection.toUpperCase().indexOf("REG_MIN") > -1)
					cts_code = "A.0001.0041.R002";
				insertReport(EI, bufrDecoder, name, cts_code, recv_time, dataBytes);
				// 2 入库
				return this.decode(recv_time, bufrDecoder, CCCC, name, tableSection, tables, DI, EI,useBBB);
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean decode(Date recv_time, String fileName,String CCCC, String tableSection, List<String> tables, List<StatDi> DI, List<RestfulInfo> EI,int useBBB) {
		String cts_code = "";
		// 1 解码
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (bufrDecoder.isParsedOk == -1){
			try{
				HashMap<String, String> map1 = new HashMap<>();
	            map1.put("message", "Decode failed");
	            map1.put("path", StringUtil.getConfigPath()); // 默认路径
	            EI.addAll(DIEISender.makeEI(map1));
			}catch (Exception e1) {
				System.out.println("ei error!");
			}
		}
		// 2 入库
		if (isRead) {
			if(tableSection.toUpperCase().indexOf("MUL_HOR") > -1)
				cts_code = "A.0001.0044.R001";
			else if(tableSection.toUpperCase().indexOf("MUL_MIN") > -1)
				cts_code = "A.0001.0043.R001";
			else if(tableSection.toUpperCase().indexOf("REG_HOR") > -1)
				cts_code = "A.0001.0042.R002";
			else if(tableSection.toUpperCase().indexOf("REG_MIN") > -1)
				cts_code = "A.0001.0041.R002";
			try {
				insertReport(EI, bufrDecoder, fileName, cts_code, recv_time, getContent(fileName));
			} catch (IOException e) {
				Log.error("Read report error!");
			}
			
			return this.decode(recv_time, bufrDecoder, CCCC, fileName, tableSection, tables, DI, EI,useBBB);
		} else {
			return false;
		}
	}
//	private final String PRIMARY_KEY_CONFLICT_KEY = "PRIMARY_KEY_CONFLICT";
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
	public void insertReport(List<RestfulInfo> EI, BUFRMessageDecoder bufrDecoder, String fileName, String cts_code, Date recv, byte[] msg){
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
			String sql="insert into "+tableName+"(D_record_id,D_DATA_ID,D_SOURCE_id,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
					+ "V01301,V_BBB,V_CCCC,V_YYGGgg,V_TT,V_LEN,V_FILE_SIZE,V_BULLETIN_B,V_FILE_NAME,V_NUM,V01301_list,d_datetime_list,"
					+ "V_decode_flag) values ("
					+ "?,?,?,?,?,"
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
				statement.setString(ii ++, station  + "_" + simpleDateFormat.format(simpleDateFormat2.parse(datatime)));
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
				statement.setString(ii ++, "999999"  + "_" + simpleDateFormat.format(simpleDateFormat2.parse(datatime)));
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
			try{
				HashMap<String, String> map1 = new HashMap<>();
	            map1.put("message", e.getMessage());
	            map1.put("path",  StringUtil.getConfigPath()); // 默认路径
	            EI.addAll(DIEISender.makeDBEI(map1));
			}catch (Exception e1) {
				System.out.println("ei error!");
			}
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
	public boolean decode(Date rcv_time, BUFRMessageDecoder bufrDecoder, String CCCC, String fileName, String tableSection, List<String> tables, List<StatDi> DI, List<RestfulInfo> EI,int useBBB) {
		// 获取数据库连接
		DruidPooledConnection connection1 = null;
		DruidPooledConnection connection2 = null;
		DruidPooledConnection connection = null;
		Statement statement = null;
		Map<String, Integer> tmpRecord = new HashMap<String, Integer>();
		File file = new File(fileName);
		String temp_tableSection = tableSection;
		if(bufrDecoder.local_version == 3 && tableSection.equalsIgnoreCase("BUFR_SURF_WEA_MUL_HOR")){
			temp_tableSection = tableSection+"_V3";
		}
		else if(bufrDecoder.local_version == 3 && tableSection.equalsIgnoreCase("BUFR_SURF_WEA_REG_HOR"))
			temp_tableSection = tableSection+"_V3";
		
		Map<String, XmlBean> configMap = BufrConfig.get(temp_tableSection);
		
		int missedSunRecord = 0;
		boolean dateTimeOK = true;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		try {
			connection1 = ConnectionPoolFactory.getInstance().getConnection("rdb");
//			statement = connection1.createStatement();
			connection1.setAutoCommit(true);
			connection2 = ConnectionPoolFactory.getInstance().getConnection("cimiss");
//			statement = connection2.createStatement();
			connection2.setAutoCommit(true);
			for(Map<String, Object> members: bufrDecoder.getParseList()){
				String currentBBB=members.get("BBB").toString();//更正标识
				if(useBBB==0){//不使用V_BBB项更正
					if(currentBBB.startsWith("C")){
						continue;
					}
				}
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
				Object visibility = null;
				Object weather = null;
				Object extremeWindSpeed = null;
				StringBuffer glb_tab_update = new StringBuffer();//日值更新数据
				StringBuffer ssd_tab_update = new StringBuffer();//日照更新数据
				StringBuffer hour_tab_update = new StringBuffer();//小时表更新数据
				for (String tableName : tables) {
					if(tableName.toUpperCase().indexOf("SSD_HOR_TAB")>-1||tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1){//日值与日照表入服务库
						try {
							connection = connection2;
							statement = connection.createStatement();
//							connection.setAutoCommit(true);
						} catch (Exception e) {
							Log.error("连接connection(cimiss)异常！");
							continue;
						}
					}else {
						try {
							connection = connection1;
							statement = connection.createStatement();
//							connection.setAutoCommit(true);
						} catch (Exception e) {
							Log.error("连接connection(rdb)异常！");
							continue;
						}
					}
					
//					String WeatherPhenoYesterday=null;//昨天的天气现象
//					String WeatherPhenoYesterdayQC=null;//昨天的天气现象质控码
//					String WeatherPhenoToday=null;//今天的天气现象
//					String WeatherPhenoTodayQC=null;//今天的天气现象质控码
					String BBB=members.get("BBB").toString();//更正标识
					String D_RECORD_ID = "";
					String time = "";
					dateTimeOK = true;
					StatDi di = new StatDi();
					di.setPROCESS_START_TIME(dateFormat.format(new Date()));
//					if(bufrDecoder.local_version == 3 && tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1){//日值表的版本3
//						Object weatherY = members.get("1-1-0-0");//昨天的天气现象
//						Map<String, Object> weatherbufrs= (Map<String, Object>) weatherY;
//						if(weatherbufrs != null && weatherbufrs.size() != 0) {
//							BufrBean bean=(BufrBean)weatherbufrs.get("0-20-212-0");
//							if(bean!=null){
//								WeatherPhenoYesterday=bean.getValue().toString();
//								WeatherPhenoYesterdayQC=bean.getQcValues().get(0).toString();
//								
//								if(WeatherPhenoYesterday!=null){
//									try {
//										String staName= "";
//										staName = new String(WeatherPhenoYesterday.toString().getBytes(), "gb2312");
//										if(staName.equalsIgnoreCase("NaN"))
//											staName = "999999";
//										WeatherPhenoYesterday = staName.replace("\u0000", "").trim().replaceAll("'", "''");
//									} catch (UnsupportedEncodingException e) {
//										Log.error("V20304  WeatherPhenoYesterday 赋值异常：" + fileName + "\n" + e.getMessage());
//									}
//								}
//							}
//						}
//					}
					
//					if(tableName.toUpperCase().startsWith("SURF_WEA_CBF_SSD_HOR_TAB")){
					if(tableName.toUpperCase().indexOf("_SSD_HOR_TAB")  > -1){
						Object sun = members.get("1-7-0-0"); //1-3-24-0
						Map<String, Object> bufrs= (Map<String, Object>) sun;
						if(bufrs == null || bufrs.size() == 0) {
							missedSunRecord++;
							continue;
						}
						sun = members.get("1-3-24-0");
						bufrs = (Map<String, Object>) sun;
						if(bufrs == null || bufrs.size() == 0){
							missedSunRecord++;
							continue;
						}
					}
					
					if(tableName.toUpperCase().equals("SURF_WEA_C_MUL_FTM_WSET_TAB")){
						 visibility = ((Map<String, Object>)members.get("1-29-0-0")).get("0-20-1-0");
						 
						if(temp_tableSection.equals("BUFR_SURF_WEA_MUL_HOR_V3"))
							weather = ((Map<String, Object>)members.get("1-8-0-0")).get("0-20-3-0");
						else 
							weather = ((Map<String, Object>)members.get("1-7-0-0")).get("0-20-3-0");
						
						 extremeWindSpeed = ((Map<String, Object>)members.get("1-25-0-0")).get("0-11-46-0");
					}
					
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
//							if(!column.equalsIgnoreCase("D_Record_id")){
								keyBuffer.append(",`").append(column).append("`");
								if(entityBean.getDatatype()!= null && entityBean.getDatatype().equalsIgnoreCase("double")){
									BigDecimal bigDecimal = (new BigDecimal(value.toString()).setScale(4, BigDecimal.ROUND_HALF_UP)) ;
									value = bigDecimal.toPlainString();
								}
								valueBuffer.append(",'").append(value.toString()).append("'");
//							}
							
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
//									di.setDATA_TIME(value.toString().substring(0, idx01));
									if(tableName.toUpperCase().indexOf("_SSD_HOR_TAB") > -1){
										double year = Double.parseDouble(((BufrBean)members.get("0-4-1-0")).getValue().toString());
										double month = Double.parseDouble(((BufrBean)members.get("0-4-2-0")).getValue().toString());
										double day = Double.parseDouble(((BufrBean)members.get("0-4-3-0")).getValue().toString());
										double hour = Double.parseDouble(((BufrBean)members.get("0-4-4-0")).getValue().toString());
										double min = Double.parseDouble(((BufrBean)members.get("0-4-5-0")).getValue().toString());
										String datetime_ssd=(int)year+"-"+(int)month+"-"+(int)day+" "+(int)hour+":00:00";
									    date = simpleDateFormat2.parse(datetime_ssd.toString());
										di.setDATA_TIME(simpleDateFormat2.format(date).substring(0, idx01));
									}else{
										di.setDATA_TIME(simpleDateFormat2.format(date).substring(0, idx01));
									}
									time = value.toString();
									
									if(!TimeCheckUtil2.checkTime(dateFormat.parse(value.toString()))){
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
						
						if(tableName.toUpperCase().equals("SURF_WEA_C_MUL_FTM_WSET_TAB") && column.equalsIgnoreCase("V20001")){
							if(weather != null){
								double vis =  Double.parseDouble(value.toString());
								double wea = Double.parseDouble(((BufrBean)weather).getValue().toString());
								if( ( !((wea >= 30 && wea <= 35) || (wea >= 40 && wea <= 49)) ) && 
										( !(((wea >= 4 && wea <= 8) || (wea >= 10 && wea <= 12) || (wea >= 36 && wea <= 39))) ) ){
									value = "999998";
								}
								else if(!( (((wea >= 30 && wea <= 35) || (wea >= 40 && wea <= 49)) && vis < 1000) || 
										(((wea >= 4 && wea <= 8) || (wea >= 10 && wea <= 12) || (wea >= 36 && wea <= 39)) && vis < 10000) )){
									value = "999998";  // 不存在天气现象障碍
								}
							}
						}
						else if(tableName.toUpperCase().equals("SURF_WEA_C_MUL_FTM_WSET_TAB") && column.equalsIgnoreCase("V20003")){
							if(visibility != null){
								double wea =  Double.parseDouble(value.toString());
								double vis = Double.parseDouble(((BufrBean)visibility).getValue().toString());
								if( ( !((wea >= 30 && wea <= 35) || (wea >= 40 && wea <= 49)) ) && 
										( !(((wea >= 4 && wea <= 8) || (wea >= 10 && wea <= 12) || (wea >= 36 && wea <= 39))) ) ){
									value = "999998";
								}
								else if(!( (((wea >= 30 && wea <= 35) || (wea >= 40 && wea <= 49)) && vis < 1000) || 
										(((wea >= 4 && wea <= 8) || (wea >= 10 && wea <= 12) || (wea >= 36 && wea <= 39)) && vis < 10000) )){
									value = "999998";  // 不存在天气现象障碍
								}
							}
							
						}
						else if(tableName.toUpperCase().equals("SURF_WEA_C_MUL_FTM_WSET_TAB") && ((column.equalsIgnoreCase("V11211") || column.equalsIgnoreCase("V11046")))){
							if(column.equalsIgnoreCase("V11211") && extremeWindSpeed != null){ //极大风向
								if(Double.parseDouble(((BufrBean)extremeWindSpeed).getValue().toString()) < 17){
									value = "999998"; //风速<17m/s，不是重要天气，不用赋风向有效值
								}
							}else if(column.equalsIgnoreCase("V11046") && extremeWindSpeed != null){// 极大风速
								if(Double.parseDouble(((BufrBean)extremeWindSpeed).getValue().toString()) < 17){
									value = "999998"; //风速<17m/s，不是重要天气，不用赋风向有效值
								}
							}
						}
						
						//降水量的处理
						else if((column.equalsIgnoreCase("V13019") || column.equalsIgnoreCase("V13020")
								|| column.equalsIgnoreCase("V13021") || column.equalsIgnoreCase("V13022")
								|| column.equalsIgnoreCase("V13023") || column.equalsIgnoreCase("V13011"))&& value.toString().startsWith("-0.1"))
							value = "999990";
						
						//20200609 add by liym 小时数据入日值表的20-08时、08-20时的雨量筒观测降水量时，进行特征值的转换
						else if(tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1 &&((column.equalsIgnoreCase("V13308")&&NowHourI==0)||(column.equalsIgnoreCase("V13309")&&NowHourI==12)) && value.toString().startsWith("-0.1")){
							value = "999990";
						}
						
						else if(column.equalsIgnoreCase("V13013") && value.toString().startsWith("-1")){//积雪深度为-1时表示积雪微量，赋值999990
							value = "999990";
						}
						else if(column.equalsIgnoreCase("V13013") && value.toString().startsWith("999999") && snowMarker != null && 
								(snowMarker.getValue().toString().equals("0.0") || 
										snowMarker.getValue().toString().equals("3.0") ||
										snowMarker.getValue().toString().equals("4.0") ||
										snowMarker.getValue().toString().equals("5.0") ||
										snowMarker.getValue().toString().equals("6.0") )){//积雪深度
							//||snowMarker.getValue().toString().equals("999999.0"))
							value = "999998";
						}
						
						else if(column.equalsIgnoreCase("V13013") && value.toString().equals("0.0") && tableName.toUpperCase().equals("SURF_WEA_C_MUL_FTM_WSET_TAB")){//积雪深度
							value = "999999";
						}
						// 风向的处理
						//如果风向，风速均为0，那么静风 999017，
//						else if((column.equalsIgnoreCase("V11290") || column.equalsIgnoreCase("V11292")
//									|| column.equalsIgnoreCase("V11201") || column.equalsIgnoreCase("V11001")) && Double.parseDouble(value.toString()) == 0){
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
						// 冻土深度第一层下限值、冻土深度第二层下限值
						//added 2019-2-20
//						else if( (column.equalsIgnoreCase("V20330_01") || column.equalsIgnoreCase("V20331_01") ||  
//								column.equalsIgnoreCase("V20330_02") || column.equalsIgnoreCase("V20331_02") ) && value.toString().startsWith("999999") && frozenSoil != null && 
//								(frozenSoil.getValue().toString().equals("0.0") || 
//										frozenSoil.getValue().toString().equals("3.0") ||
//										frozenSoil.getValue().toString().equals("4.0") ||
//										frozenSoil.getValue().toString().equals("5.0") ||
//										frozenSoil.getValue().toString().equals("6.0")  )){
////							||
////							frozenSoil.getValue().toString().equals("999999.0")
//							value = "999998";
//						}
//						else if(column.equalsIgnoreCase("V20331_01") && !value.toString().startsWith("99999") && Double.parseDouble(value.toString()) >= 500){
//							value = Double.parseDouble(value.toString()) - 500;
//						}else if(column.equalsIgnoreCase("V20331_02") && !value.toString().startsWith("99999") && Double.parseDouble(value.toString()) >= 500){
//							value = Double.parseDouble(value.toString()) - 500;
//						}
						
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
						// 日蒸发量（小型)\日蒸发量（大型）
//						else if(tableName.equalsIgnoreCase("SURF_WEA_CBF_MUL_DAY_TAB") && 
						else if(tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1 && 
								(column.equalsIgnoreCase("V13032") || column.equalsIgnoreCase("V13033") ) && evaporMarker != null){
//							Map<String, Object> tMap = ((Map<String, Object>)members.get("1-1-2-2"));//蒸发量 地面传感器标识
//							if(tMap != null && tMap.size() > 0){
//								Object sensorMarker = tMap.get("0-2-201-0");  //本地地面传感器标识
//								String marker = "";
//								if(sensorMarker instanceof String){
//									 marker = sensorMarker.toString();
//								}else{
//									marker = ((BufrBean)sensorMarker).getValue().toString();
//								}
//								if(column.equalsIgnoreCase("V13032") && !marker.equals("2.0")){ //只有为2时取编码值，否则取999998
//									value = "999998.0";
//								}
//								else if(column.equalsIgnoreCase("V13033") && !marker.equals("1.0")){ //只有为1时取编码值，否则取999998
//									value = "999998.0";
//								}
//							}
//							else{ // 如果没有 蒸发量 地面传感器标识
//								value = "999999.0";
//							}
//							evaporMarker;//蒸发量 地面传感器标识
							String marker = evaporMarker.getValue().toString();
							if(column.equalsIgnoreCase("V13032") && !marker.equals("2.0")){ //只有为2时取编码值，否则取999998
								value = "999998.0";
							}
							else if(column.equalsIgnoreCase("V13033") && !marker.equals("1.0")){ //只有为1时取编码值，否则取999998
								value = "999998.0";
							}
						}
						//字符型 ：台站名称（场地名称）、天气现象、分钟天气现象
						else if(column.equalsIgnoreCase("V01015") || column.equalsIgnoreCase("V20304") || column.equalsIgnoreCase("V20211")){ 
							try {
								String staName= "";
								staName = new String(value.toString().getBytes(), "gb2312");
								if(column.equalsIgnoreCase("V20304") && staName.equalsIgnoreCase("NaN"))
									staName = "999999";
								value = staName.replace("\u0000", "").trim().replaceAll("'", "''");
								
//								if(column.equalsIgnoreCase("V20304")){
//									WeatherPhenoToday=value.toString();
//								}
							} catch (UnsupportedEncodingException e) {
								Log.error(column + " 赋值异常：" + fileName + "\n" + e.getMessage());
							}
						}
//						if(column.equalsIgnoreCase("Q20304")){//今天天气现象质控码
//							WeatherPhenoTodayQC=value.toString();
//						}
						if(tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1){//日值表
							String commentdata="V_BBB,V01301,V01300,V05001,V06001,V07001,V07031,V02301,V_ACODE,V04001,V04002,V04003";
							String dayData12="V20305,V20326_NS,V20306_NS,V20307_NS,V20326_WE,V20306_WE,V20307_WE,V12001,V11001,V11002,V13309";
							String dayData12_Q="Q20305,Q20326_NS,Q20306_NS,Q20307_NS,Q20326_WE,Q20306_WE,Q20307_WE,Q12001,Q11001,Q11002,Q13309";
							String dayData16="V13032,V20304,V14032";
							String dayData16_Q="Q13032,Q20304,Q14032";
							if(NowHourI==0 && !column.equalsIgnoreCase("V13308") && !column.equalsIgnoreCase("Q13308")&&!commentdata.contains(column)){//0时只入V13308 “20-08时雨量筒观测降水量”
								value=entityBean.getDefaultValue();
							}
							if(NowHourI==12 && !dayData12.contains(column) && !dayData12_Q.contains(column) &&!commentdata.contains(column)){//12时的V13308 入999998
								value=entityBean.getDefaultValue();
							}
							if(NowHourI==16 && !dayData16.contains(column) && !dayData16_Q.contains(column) &&!commentdata.contains(column)){
								value=entityBean.getDefaultValue();
							}
						}	
						// end add
						
						// chy 去掉 D_record_id 拼sql
//						if(!column.equalsIgnoreCase("D_record_id")){
							keyBuffer.append(",`").append(column).append("`");
							this.appendValue(valueBuffer, datatype, value);
//						}
//						if(tableName.toUpperCase().equals("SURF_WEA_CBF_MUL_DAY_TAB")){
						if(tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1){//日值表12时与16时需更新的字段
							String dayData="";
							if(NowHourI==0){//0时取“过去12小时降水”更新日值表的“20-08时雨量筒观测降水量字段”
								dayData="V13308";
							}
							if(NowHourI==12){
								dayData="V20305,V20326_NS,V20306_NS,V20307_NS,V20326_WE,V20306_WE,V20307_WE,V12001,V11001,V11002,V13309";
							}else if (NowHourI==16) {
								dayData="V13032,V20304,V14032";
							}
							 if(!entityBean.isQc() && !value.toString().startsWith("99999") && dayData.contains(column)){
								updateSql(glb_tab_update, column, datatype, value);
								 EntityBean entityBean2 = entityMap.get("Q" + column.substring(1));
								 if(entityBean2 != null)
									 updateSql(glb_tab_update, entityBean2.getColumn(), entityBean2.getDatatype(), getValueByMapDirectly(obj, entityBean2));
							 }
						}
						if(tableName.toUpperCase().indexOf("_SSD_HOR_TAB") > -1){//日照表16时及其他时次需更新的字段
							if((NowHourI!=16 && column.contains("14032")&& !column.equals("V14032")&& !column.equals("Q14032"))||(NowHourI==16 && column.contains("14032"))){
							 if(!entityBean.isQc() && !value.toString().startsWith("99999")){
								updateSql(ssd_tab_update, column, datatype, value);
								 EntityBean entityBean2 = entityMap.get("Q" + column.substring(1));
								 if(entityBean2 != null)
									 updateSql(ssd_tab_update, entityBean2.getColumn(), entityBean2.getDatatype(), getValueByMapDirectly(obj, entityBean2));
							  }
							}
						}
						if((tableName.toUpperCase().indexOf("_MUL_HOR_TAB") > -1||tableName.toUpperCase().indexOf("_FTM_WSET_TAB") > -1)&&tableSection.startsWith("BUFR_SURF_WEA_MUL_HOR")){//中国/全球小时表、重要天气
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
//							di.setDATA_TIME(value.toString().substring(0, idx01));
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
							if(!TimeCheckUtil2.checkTime(dateFormat.parse(value.toString()))){
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
					
					// 全球地面逐小时表，只取00、03、06、09、12、15、18、21时记录入库
//					if(tableName.toUpperCase().equals("SURF_WEA_GBF_MUL_HOR_TAB") && (time.length() >= 13 && Integer.parseInt(time.substring(11, 13)) % 3 != 0)){
					if(tableName.toUpperCase().indexOf("_MUL_HOR_TAB") > -1 && tableName.toUpperCase().indexOf("SURF_WEA_G") > -1 && (time.length() >= 13 && Integer.parseInt(time.substring(11, 13)) % 3 != 0)){
						continue;
					}
					
					if(dateTimeOK == false)
						continue;
					if(tableSection.toUpperCase().indexOf("MUL_HOR") > -1)
						di.setDATA_TYPE_1("A.0001.0044.R001");
					else if(tableSection.toUpperCase().indexOf("MUL_MIN") > -1)
						di.setDATA_TYPE_1("A.0001.0043.R001");
					else if(tableSection.toUpperCase().indexOf("REG_HOR") > -1)
						di.setDATA_TYPE_1("A.0001.0042.R002");
					else if(tableSection.toUpperCase().indexOf("REG_MIN") > -1)
						di.setDATA_TYPE_1("A.0001.0041.R002");
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
					if(!(tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1 && NowHourI!=12 && NowHourI!=16)){
						DI.add(di);
					}
					
					String sql = "insert into " + tableName //
							+ " (" //
							+ keyBuffer.toString().substring(1) //
							+ ") values ("//
							+ valueBuffer.toString().substring(1) //
							+ ")";
//					System.out.println(sql);
					try {
						// 如果有后到的，删除之前插入的，再插入
//						if(tableName.toUpperCase().equals("SURF_WEA_CBF_SSD_HOR_TAB")){
//						if(tableName.toUpperCase().indexOf("_SSD_HOR_TAB") > -1){
//							String datetime = D_RECORD_ID.substring(D_RECORD_ID.indexOf("_") + 1);
//							Date date = new Date();
//							try{
//								date = simpleDateFormat.parse(datetime);
//							}catch (Exception e) {
////								e.printStackTrace();
//								Log.error("Datetime tranform error!");
//							}
//							datetime = simpleDateFormat2.format(date);
//							
//							String delete = "delete from " + tableName + " where d_datetime=" + "'" + datetime + "'" +" and D_RECORD_ID=" + "'" + D_RECORD_ID + "'";
//							statement.execute(delete);
//						}
						if(!(tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1 && NowHourI!=12 && NowHourI!=16 && NowHourI!=0)){//日值表只入0时12时与16时的数据
							statement.execute(sql);
	//						Log.info(" Sucessfulley insert one record!\n" + sql + " " + fileName);
							Log.info(" \n "+NowHourI+"时成功插入"+tableName+"表一条数据!" + fileName);
						}
					}catch (SQLException e) {
						// 如果是SURF_WEA_CBF_MUL_DAY_TAB，更新。
//						if(tableName.toUpperCase().equals("SURF_WEA_CBF_MUL_DAY_TAB") && !glb_tab_update.toString().isEmpty()){
//						if(tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1 && !glb_tab_update.toString().isEmpty()){
//							String datetime = D_RECORD_ID.substring(D_RECORD_ID.indexOf("_") + 1);
//							Date date = new Date();
//							try{
//								date = simpleDateFormat.parse(datetime);
//							}catch (Exception e2) {
////								e.printStackTrace();
//								Log.error("Datetime tranform error!");
//							}
//							datetime = simpleDateFormat2.format(date);
//							
//							String update = "update " + tableName + " set " + glb_tab_update.toString() + 
//									" D_UPDATE_TIME = '"+ dateFormat.format(new Date()) + "'" +" where d_datetime=" + "'" + datetime + "'" +" and D_RECORD_ID= '" + D_RECORD_ID + "'"; 
//							try{
//								statement.executeUpdate(update);
//							}catch (Exception e1) {
//								Log.error("Error:\n " + sql + e1.getMessage() + " " + fileName);
//							}
////							System.out.println(a);
//						}
						if (e.getErrorCode() == 13001 || e.getErrorCode() == 1062) {//主键冲突异常
							if(tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1 ||tableName.toUpperCase().indexOf("_SSD_HOR_TAB") > -1){//A.0001.0044.R001的日值表
								String datetimeToday = D_RECORD_ID.substring(D_RECORD_ID.indexOf("_") + 1);//报文当天的yyyyMMddHHmmss
								Date datetimeToday_Date = new Date();
								try{
									datetimeToday_Date = simpleDateFormat.parse(datetimeToday);
								}catch (Exception e2) {
									Log.error("\n datetimeToday tranform error!"+datetimeToday);
								}
								datetimeToday = simpleDateFormat2.format(datetimeToday_Date);//报文当天的yyyy-MM-dd HH:mm:ss
								String[] Existresult=findExistData(datetimeToday,D_RECORD_ID,tableName,connection);//查找日值/日照表库里原有数据
								String d_datetime=Existresult[0];
								String v_bbb=Existresult[1];//库里原有更正标识
								String tab_update="";
								if(tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1){
									 tab_update=glb_tab_update.toString();
								}else if(tableName.toUpperCase().indexOf("_SSD_HOR_TAB") > -1){
									 tab_update=ssd_tab_update.toString();
								}
								
								if(NowHourI==16){//日值或日照表16时更新特定字段
									if (d_datetime!=null) {//库里有记录
										if(((BBB.equals("000")||BBB.startsWith("RR"))&&(v_bbb==null||v_bbb.equals("000")||v_bbb.startsWith("RR")))
												||(BBB.startsWith("C")&& (v_bbb==null||BBB.compareTo(v_bbb) > 0||v_bbb.startsWith("RR")))){
											////////////////16时更新特定字段（缺测的或未观测的不更新）//////////////////////
											if(!tab_update.isEmpty()){
												String update = "update " + tableName + " set " + tab_update.toString() + 
														" D_UPDATE_TIME = '"+ dateFormat.format(new Date()) + "'" +" , V_BBB = '"+BBB+ "'" +" where d_datetime=" + "'" + datetimeToday + "'" +" and D_RECORD_ID= '" + D_RECORD_ID + "'"; 
												try{
													statement.executeUpdate(update);
													Log.info("\n"+ NowHourI+"时更新"+tableName+"表数据成功！"+fileName);
												}catch (Exception e1) {
													Log.error("\n"+ NowHourI+"时更新"+tableName+"表数据失败！Error:\n " + sql + e1.getMessage() + " " + fileName);
													di.setPROCESS_STATE("0");
												}
											}
											///////////////日值表16时更新昨天的天气现象及其质控码////////////////////////////////////
//											if(WeatherPhenoYesterday!=null && !WeatherPhenoYesterday.startsWith("99999")){//更新昨天的天气现象及其质控码
//												Date datetime = new Date();
//												try{
//													 datetime = simpleDateFormat.parse(datetimeToday);
//												}catch (Exception e2) {
//													Log.error("datetimeToday tranform error!");
//												}
//												Calendar calendar = Calendar.getInstance();
//												calendar.setTime(datetime);
//												calendar.add(calendar.DAY_OF_MONTH, -1);
//												datetime=calendar.getTime();
//												String datetimeYesterday=simpleDateFormat2.format(datetime);
//												String recordIdYesterday=D_RECORD_ID.substring(0,D_RECORD_ID.indexOf("_")+1)+simpleDateFormat.format(datetime);//record_id:站号_时间
//												String updateYesterdayWeather = "update " + tableName + " set V20304 = "+ "'" + WeatherPhenoYesterday + "'" +" where d_datetime=" + "'" + datetimeYesterday + "'" +" and D_RECORD_ID= '" + recordIdYesterday + "'"; 
//												try{
//													statement.executeUpdate(updateYesterdayWeather);
//												}catch (Exception e3) {
//													Log.error("16时数据更新昨天天气现象失败！Error:\n " + sql + e3.getMessage() + " " + fileName);
//												}
//												if(WeatherPhenoYesterdayQC!=null){
//													String updateYesterdayWeatherQC = "update " + tableName + " set Q20304 ="+ "'" + WeatherPhenoYesterdayQC + "'" +" where d_datetime=" + "'" + datetimeYesterday + "'" +" and D_RECORD_ID= '" + recordIdYesterday + "'"; 
//													try{
//														statement.executeUpdate(updateYesterdayWeatherQC);
//													}catch (Exception e4) {
//														Log.error("16时数据更新昨天天气现象质控码失败！ Error:\n " + sql + e4.getMessage() + " " + fileName);
//													}
//												}
//											}
									    }
										
									}else{//库里无数据
										Log.error("Error:\n " + sql + e.getMessage() + " " + fileName);
										di.setPROCESS_STATE("0");
									}
								}else if((tableName.toUpperCase().indexOf("_MUL_DAY_TAB") > -1 &&(NowHourI==12||NowHourI==0))||(tableName.toUpperCase().indexOf("_SSD_HOR_TAB") > -1 && NowHourI!=16)){//日值表0时、12时以及日照表除去16时的数据不处理更正报
									if (d_datetime!=null) {//库里有记录
										if((BBB.equals("000")||BBB.startsWith("RR"))&&(v_bbb==null||v_bbb.equals("000")||v_bbb.startsWith("RR"))){
											if(!tab_update.isEmpty()){
												String update = "update " + tableName + " set " +tab_update.toString() + 
														" D_UPDATE_TIME = '"+ dateFormat.format(new Date()) + "'" +" , V_BBB = '"+BBB+ "'" +" where d_datetime=" + "'" + datetimeToday + "'" +" and D_RECORD_ID= '" + D_RECORD_ID + "'"; 
												try{
													statement.executeUpdate(update);
													Log.info("\n"+ NowHourI+"时更新"+tableName+"表数据成功！"+fileName);
												}catch (Exception e1) {
													Log.error("\n"+ NowHourI+"时更新"+tableName+"表数据失败！Error:\n " + sql + e1.getMessage() + " " + fileName);
													di.setPROCESS_STATE("0");
												}
											}
										}
									}
								}else{//其他情况的
		//							System.out.println(sql);
		//							System.out.println(e.getMessage());
									Log.error("Error:\n " + sql + e.getMessage() + " " + fileName);
									di.setPROCESS_STATE("0");
								}
							}//日值、日照表end
							if((tableName.toUpperCase().indexOf("_MUL_HOR_TAB") > -1||tableName.toUpperCase().indexOf("_FTM_WSET_TAB") > -1)&&tableSection.startsWith("BUFR_SURF_WEA_MUL_HOR")){//中国/全球小时表、重要天气表
								String datetimeToday = time;//报文当天的yyyy-MM-dd HH:mm:ss
								String[] Existresult=findExistData(datetimeToday,D_RECORD_ID,tableName,connection);//查找库里原有数据
								String d_datetime=Existresult[0];
								String v_bbb=Existresult[1];//库里原有更正标识
								if(d_datetime!=null){
									if(BBB.startsWith("C")&& (v_bbb==null||BBB.compareTo(v_bbb) > 0||v_bbb.startsWith("RR"))){
										if(!hour_tab_update.toString().isEmpty()){
											String update = "update " + tableName + " set " +hour_tab_update.toString() + 
													" D_UPDATE_TIME = '"+ dateFormat.format(new Date()) + "'" +" , V_BBB = '"+BBB+ "'" +" where d_datetime=" + "'" + datetimeToday + "'" +" and D_RECORD_ID= '" + D_RECORD_ID + "'"; 
											try{
												statement.executeUpdate(update);
												Log.info("\n"+ NowHourI+"时更新"+tableName+"表数据成功！"+fileName);
											}catch (Exception e1) {
												Log.error("\n"+ NowHourI+"时更新"+tableName+"表数据失败！Error:\n " + sql + e1.getMessage() + " " + fileName);
												di.setPROCESS_STATE("0");
											}
										}
									}else{//非更正报，或更正报小于库里v_bbb
										Log.error("\n Error: " + sql + e.getMessage() + " " + fileName);
										di.setPROCESS_STATE("0");
									}
								}
							}
							
						}else{//非主键冲突
							Log.error("Error:\n " + sql + e.getMessage() + " " + fileName);
							di.setPROCESS_STATE("0");
						}
					}//catch end
				} //table for end
			} // list for end
			
			return true;
		} catch (Exception e) {
			try{
				HashMap<String, String> map1 = new HashMap<>();
	            map1.put("message", e.getMessage());
	            map1.put("path",  StringUtil.getConfigPath()); // 默认路径
	            EI.addAll(DIEISender.makeDBEI(map1));
			}catch (Exception e1) {
				System.out.println("ei error!");
			}
			e.printStackTrace();
			Log.error(fileName + ", " + e.getMessage() );
			return false;
		} finally {
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
//			try {
//				if(connectionCimiss != null)
//					connectionCimiss.close();
//			} catch (Exception e) {
//				Log.error("connectionCimiss Close Error!");
//				e.printStackTrace();
//			}
			
		}
//		return true;
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

//	@Override
//	public boolean decode(Date recv_time, String fileName, String tableSection, List<String> tables, String Priority) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public boolean decode(Date recv_time, String name, byte[] dataBytes, String tableSection, List<String> tables,
//			String Priority) {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
