package cma.cimiss2.dpc.indb.surf.ncdc_gsod.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.SurfWeaGlbMulGsoddayData;
import cma.cimiss2.dpc.decoder.surf.DecodeSurfWeaGlbMulGsodday;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

// TODO: Auto-generated Javadoc
/**
 * The Class DbService.
 */
public class DbService {
	
	/** The list di. */
	private static List<StatDi> listDi = null;
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The v bbb. */
	public static String v_bbb = "000";
	
	/** The sod code. */
	public static String sod_code = StartConfig.sodCode();
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();
	
	/** The v tt. */
	public static String v_tt = "GSOD";
	
	/** The package path. */
	public static String packagePath= "cma.cimiss2.dpc.indb.surf.ncdc_gsod.GSODSubThread";
	
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	public static void main(String[] args) {
		String filepath ="C:\\BaiduNetdiskDownload\\test\\A.0017.0001.R001\\W_NAFP_C_BABJ_20200502013930_P_122100-99999-2020.op.gz";
		File file = new File(filepath);
		DecodeSurfWeaGlbMulGsodday decodeSFGG = new DecodeSurfWeaGlbMulGsodday();
		Date recv_time = new Date(file.lastModified());
		StringBuffer loggerBuffer = new StringBuffer();
		ParseResult<SurfWeaGlbMulGsoddayData> parseResult = decodeSFGG.DecodeFile(filepath);	
		DataBaseAction dataBaseAction =  DbService.insert_db(parseResult.getData(), recv_time, file.getPath(), loggerBuffer);
	    System.out.println(dataBaseAction);
	}
	/**
	 * Insert db.
	 *
	 * @param list the list
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @return the data base action
	 * @Title: insert_db
	 * @Description: TODO(入库函数)
	 * @param: recv_time 消息接收时间
	 * @param: is_Batch 是否Batch提交
	 * @return: DataBaseAction sql 语句执行状态 @throws
	 */
	public static DataBaseAction insert_db(List<SurfWeaGlbMulGsoddayData> list, Date recv_time, String fileN, StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
		connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		String valueTable=StartConfig.valueTable();
//		valueTable="SURF_WEA_GLB_MUL_GSODDAY_TAB";
		String sql = "INSERT INTO "+valueTable+" "
				+ "(D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_STN,V_WBAN,V01301,V05001, "
				+ "V06001,V07001,V07031,V04001,V04002,V04003,V12001_701,V12001_040,V12011,V12011_MARK,V12012, "
				+ "V12012_MARK,V12003,V12003_040,V10051_701,V10051_040,V10004_701,V10004_040,V20001_701,V20001_040,V11291,"
				+ "V11291_040,V11320,V11041,V13023,V13023_MARK,V13013,V20302_042,V20302_060,V20302_070,V20302_089,"
				+ "V20302_017,V20302_019,V_BBB,D_SOURCE_ID)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		ArrayList<String> staList = WmoStationInfo.getStaList();
//		Map<String, NcdoStationRuleBean> staMap = WmoStationInfo.getStaMap();
		//1.创建装临时Map的集合
		ArrayList<Map<String, String>>  listStationMap = new ArrayList<Map<String, String>>();
		PreparedStatement pStatement = null;
		try {
		
			if (connection != null) {

				// 获取数据库连接
				pStatement = new LoggableStatement(connection, sql);
				if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}
				Calendar calendar = Calendar.getInstance();
				// 关闭自动提交，手动批量提交
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				listDi = new ArrayList<StatDi>();
				for (int i = 0; i < list.size(); i++) {
					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(v_tt);
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败
					int ii = 1;
					//2.创建临时的map保存站号对应的最大日期
					Map<String, String> tempMap = new HashMap<String, String>();
					SurfWeaGlbMulGsoddayData surfBean=list.get(i);
					String stn = surfBean.getStationNumberLocation();
					String wban = surfBean.getWeatherBureauAirForceNavy();
					String yearMoDa = surfBean.getYearMoDa();
					//3.判断是不是是最新的日期，如果是最新的数据才解码入库
//					String datatTime = compareMaxDateMap.get(stn+"—"+wban);
//					if(null != datatTime&&Integer.valueOf(yearMoDa)<=Integer.valueOf(datatTime)){
//						continue;
//					}
					
//					String primkey =yearMoDa +"_"+stn.substring(0, 5);
//					pStatement.setString(ii++, primkey);
					pStatement.setString(ii++, sod_code);

					Date yearMoDa1 = TimeUtil.String2Date(surfBean.getYearMoDa(), "yyyyMMdd");
					calendar.setTime(yearMoDa1);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime())); // 入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime())); // 收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime())); // 更新时间
					pStatement.setTimestamp(ii++, new Timestamp(yearMoDa1.getTime()));// 资料时间

					pStatement.setString(ii++, stn);// 原始STN站号
					pStatement.setString(ii++, wban);// 原始WBAN站号

					/**
					 * 首先判断STN是不是WMO的站号： 1。首先看最末尾是不是0，如果不是0，则不是wmo的站号
					 * 2。末尾是0，去掉末尾的0，读取stas_wmo_zzq.txt，看是否在stas_wmo_zzq.txt中，如果在则是wmo站号，否则不是。
					 * 再得到站号值：
					 * 
					 *  1。如果不是wmo站号，则赋值“999999”
					 * 2。如果是wmo站号，则只取前5位。同时，如果首位也是0，去掉首位。 这个是跟研究室做这个资料的人沟通过后这样解的
					 */

//					if (staList.contains(stn)) {
						if (stn.length() >= 5) {
							stn = stn.substring(0, 5);
							pStatement.setString(ii++, stn);// 区站号(字符)
						} else {
							pStatement.setString(ii++, String.format("%05d", stn));// 区站号(字符)
						}
                
//					} else {
//						stn = "999999";
//						pStatement.setString(ii++, stn);// 区站号(字符)
//					}
					String longitude=getStationInfoFromConfig(stn, "01", "longtitude");
					String latitude=getStationInfoFromConfig(stn, "01", "latitude");
					String stationhight=getStationInfoFromConfig(stn, "01", "stationhight");		
//					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(StationCodeUtil.stringToAscii(stn))));// 区站号(数字)
//					NcdoStationRuleBean ncdoBean = staMap.get(
//							surfBean.getStationNumberLocation() + "_" + surfBean.getWeatherBureauAirForceNavy());
//					if (null != ncdoBean) {
						pStatement.setBigDecimal(ii++, new BigDecimal(longitude));// 纬度
						pStatement.setBigDecimal(ii++, new BigDecimal(latitude));// 经度
						pStatement.setBigDecimal(ii++, new BigDecimal(stationhight));// 测站高度
//					} else {
//						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));// 纬度
//						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));// 经度
//						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));// 测站高度
//					}
					pStatement.setBigDecimal(ii++, new BigDecimal("999999"));// 气压传感器海拔高度
//					pStatement.setBigDecimal(ii++, new BigDecimal("6"));// 台站级别
//					pStatement.setBigDecimal(ii++, new BigDecimal("5"));// 中国行政区划代码

					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(calendar.get(Calendar.YEAR))));// 年
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(calendar.get(Calendar.MONTH) + 1)));// 月
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));// 日

					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getDailyAvgTemperature())));// 日平均气温
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getDailyAvgTemperatureCount())));// 日气温记录数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getMaxTemperature())));// 日最高气温
					pStatement.setString(ii++, surfBean.getMaxFlag());// 日最高气温取值标志
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getMinTemperature())));// 日最低气温
					pStatement.setString(ii++, surfBean.getMinFlag());// 日最低气温取值标志
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getDewPointTemperature())));// 日平均露点温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getDewPointTemperatureCount())));// 日露点温度记录数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getSeaLevelPressure())));// 日平均海平面气压
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getSeaLevelPressureCount())));// 日海平面气压记录数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getStationPressure())));// 日平均本站气压
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getStationPressureCount())));// 日本站气压记录数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getVisibility())));// 日平均能见度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getVisibilityCount())));// 日能见度记录数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getWindSpeed())));// 日平均风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getWindSpeedCount())));// 日风速记录数
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getMaxSustainedWindSpeed())));// 日最大持续风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getMaxWindGust())));// 日最大阵风风速
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getPrecipitationDaily())));// 日降水量
					pStatement.setString(ii++, surfBean.getPrecipitationDailyFlag());// 日降水量取值标志
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getSnowDepth())));// 日(总)雪深
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getFog())));// 雾
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getRain())));// 雨
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getSnow())));// 雪
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getHail())));// 冰雹
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getThunder())));// 雷
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfBean.getTornado())));// 龙卷风
					pStatement.setString(ii++, "000");
					pStatement.setString(ii++, cts_code+"_"+new File(fileN).getName());
					
					 System.out.println(((LoggableStatement)pStatement).getQueryString());
					di.setIIiii(surfBean.getStationNumberLocation());
//					if(ncdoBean != null){
						di.setLATITUDE(longitude);
						di.setLONGTITUDE(latitude);
						di.setHEIGHT(stationhight);
//					}
//					else{
//						di.setLATITUDE("999999");
//						di.setLONGTITUDE("999999");
//						di.setHEIGHT("999999");
//					}
					di.setDATA_TIME(TimeUtil.date2String(yearMoDa1, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(fileN).length()));
					di.setDATA_UPDATE_FLAG("000");
					
					// 批量提交
					pStatement.addBatch();
					sqls.add(((LoggableStatement) pStatement).getQueryString());
					listDi.add(di);
					//4.将更新的站号数据放入静态代码块中
//					compareMaxDateMap.put(surfBean.getStationNumberLocation()+"—"+surfBean.getWeatherBureauAirForceNavy(), yearMoDa);
					tempMap.put(surfBean.getStationNumberLocation()+"—"+surfBean.getWeatherBureauAirForceNavy(), yearMoDa);
					//5.将map装入list中准备写入配置文件
					listStationMap.add(tempMap);

				} 
				try {
					pStatement.executeBatch();
					connection.commit();
					sqls.clear();
					//6.将更新的数据站号日期写入Properties文件
//					if(listStationMap.size() > 0){
//						CompareStaMaxDate.AddOrUpdateProperties(listStationMap);
//					}
					return DataBaseAction.SUCCESS;
				} catch (Exception e) {
					pStatement.clearParameters();
					pStatement.clearBatch();
					execute_sql(sqls, connection, fileN, loggerBuffer);
					// execute_sql(list, connection, type, recv_time);
					loggerBuffer.append("\n " + packagePath + " Batch commit failed: " + fileN);
					return DataBaseAction.BATCH_ERROR;
				}
			} else {
				loggerBuffer.append("\n " + packagePath + " Database connection error!");
				return DataBaseAction.CONNECTION_ERROR;
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n " + packagePath + " Create statement failed: " + e.getMessage());
		} finally {
//			for (int j = 0; j < listDi.size(); j++) {
//				diQueues.offer(listDi.get(j));
//			}
			listDi.clear();
			try {
				if (pStatement != null)
					pStatement.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n " + packagePath + " Close statement failed: " + e.getMessage());
			}
			try{
				if(connection != null)
					connection.close();
			}catch (Exception e) {
				loggerBuffer.append("\n " + packagePath + " Close database failed: " + e.getMessage());
			}
		}
		return DataBaseAction.SUCCESS;
	}
	/**
     * 获取经纬度信息
     * 
     * @param stationNumberChina
     * @param stationTypeNo
     * @param key
     * @return String
     */
    private static String getStationInfoFromConfig(String stationNumberChina, String stationTypeNo,
            String key) {
        String resValue = "999999";
        try {
            Map<String, Object> proMap = StationInfo.getProMap();
//        	 Map<String, Object> proMap =getStationMap();
            String info = (String) proMap.get(stationNumberChina + "+" + stationTypeNo);
            if(info != null){
	            String[] infos = info.split(",");
	           
	            if (infos.length < 10) {
	                return resValue;
	            } else {
	                key = key.toLowerCase();
	                if(key.equals("longtitude")) {
	                	 resValue = "null".equals(infos[1]) ? "999999" : infos[1];
	                }else if(key.equals("latitude")) {
	                	 resValue = "null".equals(infos[2]) ? "999999" : infos[2];
	                }else if(key.equals("stationhight")) {
	                	 resValue = "null".equals(infos[3]) ? "999999" : infos[3];
	                }
	            }
            }
            else
            	System.out.println("Station " + stationNumberChina + " not exist in StationInfo_Config.lua");
        } catch (Exception e) {
        	System.err.println("获取StationInfo_Config.lua 配置信息失败:"+e.getMessage());
        }
        return resValue;
    }	
	/**
	 * Execute sql.
	 *
	 * @param sqls the sqls
	 * @param connection the connection
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN, StringBuffer loggerBuffer) {
		PreparedStatement pStatement = null;
		try {
			connection.setAutoCommit(true);
			for (int i = 0; i < sqls.size(); i++) {
				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute();
				} catch (Exception e) {
					loggerBuffer.append("\n "+packagePath + "File name: " + fileN + "\n " + listDi.get(i).getIIiii() + " "
							+ listDi.get(i).getDATA_TIME() + "\n execute sql error: "
							+ ((LoggableStatement) pStatement).getQueryString() + "\n " + e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n "+packagePath+" Create Statement error: " + e.getMessage());
		} finally {
			if (pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n "+packagePath+" Close Statement error: " + e.getMessage());
				}
			}
		}
	}
}
