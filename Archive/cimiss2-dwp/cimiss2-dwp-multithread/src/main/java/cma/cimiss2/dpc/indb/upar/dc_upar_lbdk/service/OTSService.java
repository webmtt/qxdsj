package cma.cimiss2.dpc.indb.upar.dc_upar_lbdk.service;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.UparLBand;
import cma.cimiss2.dpc.decoder.bean.upar.UparMinute;
import cma.cimiss2.dpc.decoder.bean.upar.UparSecond;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.upar.DecodeLbandUparDetection;


// TODO: Auto-generated Javadoc
/**
 * The Class OTSService.
 */
public class OTSService {
	
	/** The list di. */
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The v tt. */
	public static String V_TT = "L波段探空系统秒级探测资料";
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();

	
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
		OTSService.diQueues = diQueues;
	}


	/**
	 * 
	 * @Title: processSuccessReport   
	 * @Description:(报文解码入库函数)   
	 * @param  parseResult  存放解码结果
	 * @param filepath 文件路径
	 * @param  recv_time  报文接收时间   
	 * @return: DataBaseAction      
	 * @throws:
	 */
  public static DataBaseAction insertDBService(ParseResult<UparLBand> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer) {
	   
		String sodSs[] = StartConfig.sodCodes();
		String second_sod_code = sodSs[0];
		String minute_sod_code=sodSs[1];
		String para_sod_code=sodSs[2];
			
			try {
			List<UparLBand> uparLBand = parseResult.getData();	
//			String ktableName_second = "UPAR_WEA_CHN_MUL_NSEC_K_TAB";//秒级键表名
//			String ktableName_Minute = "UPAR_WEA_C_MUL_MIN_K_TAB";//分钟级键表名
//			String ParaTableName="UPAR_WEA_CHN_PARA_TAB";//基本参数表名
			
			String[] kTables =StartConfig.keyTable().trim().split(",");
			String ktableName_second =kTables[0];//秒级键表名
			String ktableName_Minute =kTables[1];//分钟级键表名
			String ParaTableName=kTables[2];//基本参数表名
				//秒级 
				boolean insertEleDB = insertdb_second(uparLBand, recv_time, fileN,loggerBuffer);//插入要素表				
				if(insertEleDB) { // 批量插入要素表成功				
					boolean insertKeyDB = insertKeyDB(uparLBand,ktableName_second,second_sod_code,recv_time,loggerBuffer,cts_code,fileN);//批量插入秒级键表

				}
				boolean insertParaTab = insertParaTab(uparLBand,ParaTableName,para_sod_code,recv_time,loggerBuffer,cts_code,fileN);//批量插入基本参数表
				
				//分钟级
//				insertEleDB = insertdb_minute(uparLBand, connection2,recv_time, fileN,loggerBuffer);//插入要素表
//				if(insertEleDB) {
//					DataBaseAction insertKeyDB =insertKeyDB(uparLBand,ktableName_Minute,minute_sod_code,connection2,recv_time,loggerBuffer,cts_code,fileN);//批量插入分级键表
//								
//				}
														
			return DataBaseAction.SUCCESS;
		    }catch (Exception e) {
		    	loggerBuffer.append("\n database connection  error");
		    	return DataBaseAction.CONNECTION_ERROR;
		    }
					
		}
			
			
				
	
  /**
	 * 
	 * @Title: insertdb_second   
	 * @Description: (L波段探空系统秒级探测资料入库)   
	 * @param:  uparLBand 待入库的对象集合
	 * @param:  connection 数据库连接
	 * @param:  recv_time 资料接收时间
	 * @param:  fileN
	 * @param:  loggerBuffer      
	 * @return: void      
	 * @throws
	 */
	private static boolean insertdb_second(List<UparLBand> uparLBand,Date recv_time, String fileN,StringBuffer loggerBuffer) {	
		String second_sod_code = StartConfig.sodCodes()[0];
		String ValueTables=StartConfig.valueTable();
		String secondValueTable=ValueTables.trim().split(",")[0];
//		PreparedStatement pStatement = null;
		try {
		String sql = "INSERT INTO "+secondValueTable+"(D_RECORD_ID,d_ele_id,D_DATETIME,d_update_time,"
				+ "V01301,V04086,V05015,V06015,V12001,V07004,V13003,V07021,V05021,V06021,V11001,V11002,V10009,"
				+ "Q05015,Q06015,Q12001,Q07004,Q13003,Q07021,Q05021,Q06021,Q11001,Q11002,Q10009,D_SOURCE_ID"
				+ ")"
				+ "VALUES (?, ?, ?, ?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?"
				+ ") ";
		
		Map<String, Object> row = new HashMap<>();
//		List<String> sqls = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		int successRowCount = 0;
		
		for(int idx = 0; idx < uparLBand.size(); idx ++){
			
			List<UparSecond> uparSeconds = uparLBand.get(idx).getUparsecond();				
			
			for (int i = 0; i < uparSeconds.size(); i++) {
				
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				//di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE(second_sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(V_TT);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败
				
				int stationNumberN = 999999;
				String stat = uparLBand.get(idx).getStationNumberChina();//获取头文件的台站号
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);
				else
					stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
				String lat = String.valueOf((int)(uparLBand.get(idx).getLatitude() * 1e6));//获取头文件的纬度
				String lon = String.valueOf((int)(uparLBand.get(idx).getLongitude() * 1e6));//获取头文件的经度
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");
				
				Date date = new Date();
				date = uparLBand.get(idx).getCastingUtcTime();//获取头文件的施放时间（世界时）
				
	
				UparSecond uparSecond = uparSeconds.get(i);//获取单条秒级数据
				int ii = 1;
				Date dataTime = new Date(date.getTime());
				dataTime.setMinutes(0);
				dataTime.setSeconds(0);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dataTime);
				if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 5){
					calendar.add(Calendar.HOUR_OF_DAY, 1);
				}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 1){
					calendar.add(Calendar.HOUR_OF_DAY, -1);
				}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 2){
					calendar.add(Calendar.HOUR_OF_DAY, -2);
				}
//				pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon);//D_RECORD_ID	
				row.put("D_RECORD_ID", sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon);
//				pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon+"_"+uparSecond.getRelativeTime());//d_ele_id	
				row.put("d_ele_id", sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon+"_"+uparSecond.getRelativeTime());
//				pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar.getTime().getTime()).getTime()));//D_DATETIME 资料时间
				row.put("D_DATETIME", new Timestamp(new Date(calendar.getTime().getTime()).getTime()));
//				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间d_update_time
				row.put("d_update_time", new Timestamp(new Date().getTime()));
//				pStatement.setString(ii++, stat);//区站号V01301
				row.put("V01301", stat);
//				pStatement.setDouble(ii++, uparSecond.getRelativeTime());//时间差V04086
				row.put("V04086", uparSecond.getRelativeTime());
//				pStatement.setDouble(ii++,uparSecond.getLatitudeeDev());//纬度偏差V05015
				row.put("V05015", uparSecond.getLatitudeeDev());
//			    pStatement.setDouble(ii++, uparSecond.getLongitudeDev());//经度偏差V06015
				row.put("V06015", uparSecond.getLongitudeDev());
//				pStatement.setDouble(ii++, uparSecond.getTemperature());//温度V12001
				row.put("V12001", uparSecond.getTemperature());
//				pStatement.setDouble(ii++, uparSecond.getPressure());//气压V07004
				row.put("V07004", uparSecond.getPressure());
//				pStatement.setDouble(ii++, uparSecond.getRelativeHumidity());//相对湿度V13003
				row.put("V13003", uparSecond.getRelativeHumidity());
//				pStatement.setDouble(ii++, uparSecond.getLookUpAngle());//仰角V07021
				row.put("V07021", uparSecond.getLookUpAngle());
//				pStatement.setDouble(ii++, uparSecond.getBearing());//方位角V05021
				row.put("V05021", uparSecond.getBearing());
//				pStatement.setDouble(ii++, uparSecond.getDistance());//距离V06021
				row.put("V06021", uparSecond.getDistance());
//				pStatement.setDouble(ii++, uparSecond.getWindDir());//风向V11001
				row.put("V11001", uparSecond.getWindDir());
//				pStatement.setDouble(ii++, uparSecond.getWindSpeed());//风速V11002
				row.put("V11002", uparSecond.getWindSpeed());
//				pStatement.setDouble(ii++, uparSecond.getHeight());//位势高度V10009
				row.put("V10009", uparSecond.getHeight());
//				pStatement.setInt(ii++, 9);//纬度偏差质量标志Q05015
				row.put("Q05015", 9);
//				pStatement.setInt(ii++, 9);//经度偏差质量标志Q06015
				row.put("Q06015", 9);
//				pStatement.setInt(ii++, 9);//温度质量标志Q12001
				row.put("Q12001", 9);
//				pStatement.setInt(ii++, 9);//气压（标准层）质量标志Q07004
				row.put("Q07004", 9);
//				pStatement.setInt(ii++, 9);//相对湿度质量标志Q13003
				row.put("Q13003", 9);
//				pStatement.setInt(ii++, 9);//仰角质量标志Q07021
				row.put("Q07021", 9);
//				pStatement.setInt(ii++, 9);//方位角质量标志Q05021
				row.put("Q05021", 9);
//				pStatement.setInt(ii++, 9);//距离质量标志Q06021
				row.put("Q06021", 9);
//				pStatement.setInt(ii++, 9);//风向质量标志Q11001
				row.put("Q11001", 9);
//				pStatement.setInt(ii++, 9);//风速质量标志Q11002
				row.put("Q11002", 9);
//				pStatement.setInt(ii++, 9);//位势高度质量标志Q10009
				row.put("Q10009", 9);
//				pStatement.setString(ii++, cts_code);//资料四级编码	
				row.put("D_SOURCE_ID", cts_code);
				//System.out.println(((LoggableStatement)pStatement).getQueryString());
				
				di.setIIiii(uparLBand.get(idx).getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(uparLBand.get(idx).getLatitude().toString());
				di.setLONGTITUDE(uparLBand.get(idx).getLongitude().toString());
				
				//入OTS
				try {
					OTSDbHelper.getInstance().insert(secondValueTable, row);
					System.out.println("!!second!!成功");
					infoLogger.info("Insert one line successfully!\n");
					diQueues.offer(di);
					successRowCount ++;
				}catch (Exception e) {
					e.printStackTrace();
					di.setPROCESS_STATE("0");
					diQueues.offer(di);
					infoLogger.error(row + "\n");
					infoLogger.error(e.getMessage() + "\n");
					if(e.getClass() == ClientException.class) {
						infoLogger.error("DataBase connection error!\n");
					}
					System.out.println(row.toString());
					return false;
				}
			}//end for uparLTempSeconds
				
		
		    }// end for
		
		} catch (Exception e) {
				e.printStackTrace();
				return false;
		}
		diQueues.clear();
		return true;
				
	}
	/**
	 * 
	 * @Title: insertKeyDB   
	 * @Description: TODO(中国高空探测秒/分数据键表)   
	 * @param: @param uparLTemps
	 * @param: @param ktablename
	 * @param: @param connection
	 * @param: @param recv_time
	 * @param: @param loggerBuffer
	 * @param: @param cts_code2
	 * @param: @param fileN
	 * @param: @return      
	 * @return: DataBaseAction      
	 * @throws
	 */
	private static boolean insertKeyDB(List<UparLBand> uparLBands,String ktablename,String sodcode, Date recv_time, StringBuffer loggerBuffer,String cts_code2, String fileN) {
		int year,month,day,hour,minute,second;
//		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+ktablename+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,"
					+ "V04001_02,V04002_02,V04003_02,V04004_02,V01301,V01300,V05001,V06001,V07001,V04001,V04002,V04003,V04004,V04005,V04006,V31001,D_SOURCE_ID) VALUES("
					+"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
	
			Map<String, Object> row = new HashMap<>();
			int successRowCount = 0;
//			List<String> sqls = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();//year,month,day,hour,minute,second				

			for(int idx = 0; idx < uparLBands.size(); idx ++){
				
				Date date = uparLBands.get(idx).getCastingUtcTime();//获取施放时间（世界时）
				calendar.setTime(date);//Date类型转int
				year = calendar.get(Calendar.YEAR);//获取年
				month = calendar.get(Calendar.MONTH) + 1;//获取月
				day = calendar.get(Calendar.DAY_OF_MONTH);//获取日
				hour = calendar.get(Calendar.HOUR_OF_DAY);//获取日
				minute = calendar.get(Calendar.MINUTE);//获取分
				second = calendar.get(Calendar.SECOND);//获取秒
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				//di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE(sodcode);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(V_TT);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败
				
				int stationNumberN = 999999;
				String stat = uparLBands.get(idx).getStationNumberChina();//获取台站号
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);
				else
					stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
				String lat = String.valueOf((int)(uparLBands.get(idx).getLatitude() * 1e6));
				String lon = String.valueOf((int)(uparLBands.get(idx).getLongitude() * 1e6));
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");						
				di.setIIiii(uparLBands.get(idx).getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(uparLBands.get(idx).getLatitude().toString());
				di.setLONGTITUDE(uparLBands.get(idx).getLongitude().toString());
				int ii=1;
				Date dataTime = new Date(date.getTime());
				dataTime.setMinutes(0);
				dataTime.setSeconds(0);
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(dataTime);
				if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 5){
					calendar2.add(Calendar.HOUR_OF_DAY, 1);
				}else if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 1){
					calendar2.add(Calendar.HOUR_OF_DAY, -1);
				}else if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 2){
					calendar2.add(Calendar.HOUR_OF_DAY, -2);
				}
//				pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar2.getTime())+"_"+stat+"_"+lat+"_"+lon);//记录标识D_RECORD_ID	
				row.put("D_RECORD_ID	", sdf.format(date.getTime())+"_"+sdf.format(calendar2.getTime())+"_"+stat+"_"+lat+"_"+lon);
//				pStatement.setString(ii++,sodcode);//资料标识	
				row.put("D_DATA_ID", sodcode);
//				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
				row.put("D_IYMDHM", new Timestamp(new Date().getTime()));
//				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
				row.put("D_RYMDHM", new Timestamp(recv_time.getTime()));
//				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间d_update_id
				row.put("D_UPDATE_TIME", new Timestamp(new Date().getTime()));
//				pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar2.getTime().getTime()).getTime()));//资料时间D_DATETIME
				row.put("D_DATETIME", new Timestamp(new Date(calendar2.getTime().getTime()).getTime()));
//				pStatement.setString(ii++, uparLBands.get(idx).getCorrectSign());//更正标识
				row.put("V_BBB", uparLBands.get(idx).getCorrectSign());
//				pStatement.setInt(ii++, calendar2.getTime().getYear()+1900);//资料年
				row.put("V04001_02", calendar2.getTime().getYear()+1900);
//				pStatement.setInt(ii++, calendar2.getTime().getMonth()+1);//资料月
				row.put("V04002_02", calendar2.getTime().getMonth()+1);
//				pStatement.setInt(ii++,calendar2.getTime().getDate());//资料日
				row.put("V04003_02", calendar2.getTime().getDate());
//				pStatement.setInt(ii++, calendar2.getTime().getHours());//资料时
				row.put("V04004_02", calendar2.getTime().getHours());
//				pStatement.setString(ii++, stat);//字符区站号V01301
				row.put("V01301", stat);
//				pStatement.setInt(ii++, stationNumberN);//数字区站号V01300
				row.put("V01300", stationNumberN);
//				pStatement.setDouble(ii++, uparLBands.get(idx).getLatitude());//纬度
				row.put("V05001", uparLBands.get(idx).getLatitude());
//  			pStatement.setDouble(ii++, uparLBands.get(idx).getLongitude());//经度
				row.put("V06001", uparLBands.get(idx).getLongitude());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getHeightOfSationGroundAboveMeanSeaLevel());//测站海拔高度
				row.put("V07001", uparLBands.get(idx).getHeightOfSationGroundAboveMeanSeaLevel());
//				pStatement.setInt(ii++, year);//气球施放时间（年）
				row.put("V04001", year);
//				pStatement.setInt(ii++, month);//气球施放时间（月）
				row.put("V04002", month);
//				pStatement.setInt(ii++, day);//气球施放时间（日）
				row.put("V04003", day);
//				pStatement.setInt(ii++, hour);//气球施放时间（时）
				row.put("V04004", hour);
//				pStatement.setInt(ii++, minute);//气球施放时间（分）
				row.put("V04005", minute);
//				pStatement.setInt(ii++, second);//气球施放时间（秒）
				row.put("V04006", second);
				
				if("UPAR_WEA_CHN_MUL_NSEC_K_TAB".equals(ktablename)){
//					pStatement.setInt(ii++, uparLBands.get(idx).getUparsecond().size());//秒数据层数
					row.put("V31001", uparLBands.get(idx).getUparsecond().size());
				}else if("UPAR_WEA_C_MUL_MIN_K_TAB".equals(ktablename)){
//					pStatement.setInt(ii++, uparLBands.get(idx).getUparminute().size());//分钟数据层数
					row.put("V31001", uparLBands.get(idx).getUparminute().size());
				}
//				pStatement.setString(ii++, cts_code);//资料四级编码
				row.put("D_SOURCE_ID", cts_code);
				
				//入OTS
				try {
					OTSDbHelper.getInstance().insert(ktablename, row);
					System.out.println("!!key!!成功");
					infoLogger.info("Insert one line successfully!\n");
					diQueues.offer(di);
					successRowCount ++;
				}catch (Exception e) {
					e.printStackTrace();
					di.setPROCESS_STATE("0");
					diQueues.offer(di);
					infoLogger.error(row + "\n");
					infoLogger.error(e.getMessage() + "\n");
					if(e.getClass() == ClientException.class) {
						infoLogger.error("DataBase connection error!\n");
					}
					System.out.println(row.toString());
					return false;
				}
			}// end for
				 	
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		diQueues.clear();
		return true;
	}
	/**
	 * 
	 * @Title: insertdb_minute   
	 * @Description: (L波段探空系统分钟级探测资料入库)   
	 * @param: uparLBands 待入库的对象集合
	 * @param:  connection 数据库连接
	 * @param:  recv_time 资料接收时间
	 * @param:  fileN
	 * @param:  loggerBuffer      
	 * @return: void      
	 * @throws
	 */
	private static boolean insertdb_minute(List<UparLBand> uparLBands, Date recv_time, String fileN,StringBuffer loggerBuffer) {
		String minute_sod_code = StartConfig.sodCodes()[1];
		String ValueTables=StartConfig.valueTable();
		String minuteValueTable=ValueTables.trim().split(",")[1];
//		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+minuteValueTable+"(D_RECORD_ID,d_ele_id,D_DATETIME,d_update_time,"
					+ "V01301,V01300,V04086,V05015,V06015,V12001,V07004,V13003,V11001,V11002,V10009,"
					+ "Q05015,Q06015,Q12001,Q07004,Q13003,Q11001,Q11002,Q10009,D_SOURCE_ID"
					+ ")"
					+ "VALUES (?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?,?"
					+ ") ";
			
			Map<String, Object> row = new HashMap<>();
			int successRowCount = 0;
//			List<String> sqls = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			
			for(int idx = 0; idx < uparLBands.size(); idx ++){
				
				List<UparMinute> uparMinutes = uparLBands.get(idx).getUparminute();
				
				for (int i = 0; i < uparMinutes.size(); i++) {
					StatDi di = new StatDi();
					di.setFILE_NAME_O(fileN);
					//di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE(minute_sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("0"); //0成功，1失败
					di.setPROCESS_STATE("0");  //0成功，1失败
					
					int stationNumberN = 999999;
					String stat = uparLBands.get(idx).getStationNumberChina();//获取台站号
					if (Pattern.matches("\\d{5}", stat))
						stationNumberN = Integer.parseInt(stat);
					else
						stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
					String lat = String.valueOf((int)(uparLBands.get(idx).getLatitude() * 1e6));//纬度
					String lon = String.valueOf((int)(uparLBands.get(idx).getLongitude() * 1e6));//经度
					lat = lat.replaceAll("-", "0");
					lon = lon.replaceAll("-", "0");
					
					Date date = new Date();
					date = uparLBands.get(idx).getCastingUtcTime();//施放时间（世界时）
					
					
					UparMinute uparMinute = uparMinutes.get(i);//获取单条分钟级数据
					int ii = 1;
					Date dataTime = new Date(date.getTime());
					dataTime.setMinutes(0);
					dataTime.setSeconds(0);
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(dataTime);
					if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 5){
						calendar.add(Calendar.HOUR_OF_DAY, 1);
					}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 1){
						calendar.add(Calendar.HOUR_OF_DAY, -1);
					}else if(calendar.get(Calendar.HOUR_OF_DAY) % 6 == 2){
						calendar.add(Calendar.HOUR_OF_DAY, -2);
					}
					
//					pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon);//D_RECORD_ID	
					row.put("D_RECORD_ID", sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon);
//					pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon+"_"+uparMinute.getRelativeTime());//d_ele_id
					row.put("d_ele_id", sdf.format(date.getTime())+"_"+sdf.format(calendar.getTime())+"_"+stat+"_"+lat+"_"+lon+"_"+uparMinute.getRelativeTime());
//					pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar.getTime().getTime()).getTime()));//D_DATETIME
					row.put("D_DATETIME", new Timestamp(new Date(calendar.getTime().getTime()).getTime()));
//					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间d_update_id
					row.put("d_update_id", new Timestamp(new Date().getTime()));
//					pStatement.setString(ii++, stat);//区站号V01301
					row.put("V01301", stat);
//					pStatement.setInt(ii++, stationNumberN);//区站号ascii V01300
					row.put("V01300", stationNumberN);
//					pStatement.setDouble(ii++, uparMinute.getRelativeTime());//时间差V04086
					row.put("V04086", uparMinute.getRelativeTime());
//					pStatement.setDouble(ii++, uparMinute.getLatitudeeDev());//纬度偏差V05015
					row.put("V05015", uparMinute.getLatitudeeDev());
//					pStatement.setDouble(ii++, uparMinute.getLongitudeDev());//经度偏差V06015
					row.put("V06015", uparMinute.getLongitudeDev());
//  			    pStatement.setDouble(ii++, uparMinute.getTemperature());//温度V12001
					row.put("V12001", uparMinute.getTemperature());
//					pStatement.setDouble(ii++, uparMinute.getPressure());//气压V07004
					row.put("V07004", uparMinute.getPressure());
//					pStatement.setDouble(ii++, uparMinute.getRelativeHumidity());//相对湿度V13003
					row.put("V13003", uparMinute.getRelativeHumidity());
//					pStatement.setDouble(ii++, uparMinute.getWindDir());//风向V11001
					row.put("V11001", uparMinute.getWindDir());
//					pStatement.setDouble(ii++, uparMinute.getWindSpeed());//风速V11002
					row.put("V11002", uparMinute.getWindSpeed());
//					pStatement.setDouble(ii++, uparMinute.getHeight());//位势高度V10009	
					row.put("V10009", uparMinute.getHeight());
//					pStatement.setInt(ii++, 9);//纬度偏差质量标志Q05015
					row.put("Q05015", 9);
//					pStatement.setInt(ii++, 9);//经度偏差质量标志Q06015
					row.put("Q06015", 9);
//					pStatement.setInt(ii++, 9);//温度质量标志Q12001
					row.put("Q12001", 9);
//					pStatement.setInt(ii++, 9);//气压（标准层）质量标志Q07004
					row.put("Q07004", 9);
//					pStatement.setInt(ii++, 9);//相对湿度质量标志Q13003
					row.put("Q13003", 9);
//			    	pStatement.setInt(ii++, 9);//风向质量标志Q11001
					row.put("Q11001", 9);
//					pStatement.setInt(ii++, 9);//风速质量标志Q11002
					row.put("Q11002", 9);
//					pStatement.setInt(ii++, 9);//位势高度质量标志Q10009
					row.put("Q10009", 9);
//					pStatement.setString(ii++, cts_code);//资料四级编码
					row.put("D_SOURCE_ID", cts_code);
					
					di.setIIiii(uparLBands.get(idx).getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(uparLBands.get(idx).getLatitude().toString());
					di.setLONGTITUDE(uparLBands.get(idx).getLongitude().toString());
					
				
					//入OTS
					try {
						OTSDbHelper.getInstance().insert(minuteValueTable, row);
						System.out.println("!!minute!!成功");
						infoLogger.info("Insert one line successfully!\n");
						diQueues.offer(di);
						successRowCount ++;
					}catch (Exception e) {
						e.printStackTrace();
						di.setPROCESS_STATE("0");
						diQueues.offer(di);
						infoLogger.error(row + "\n");
						infoLogger.error(e.getMessage() + "\n");
						if(e.getClass() == ClientException.class) {
							infoLogger.error("DataBase connection error!\n");
						}
						System.out.println(row.toString());
						return false;
					}
				}//end for uparLTempMinutes									
			}// end for uparLTemps.size()
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		diQueues.clear();
		return true;
		
	}
	/**
	 * (L波段探空系统基本参数表入库) 
	 * @param uparLBands
	 * @param ktablename
	 * @param sodcode
	 * @param connection
	 * @param recv_time
	 * @param loggerBuffer
	 * @param cts_code2
	 * @param fileN
	 * @return
	 */
	private static boolean insertParaTab(List<UparLBand> uparLBands,String ParaTableName,String sodcode, Date recv_time, StringBuffer loggerBuffer,String cts_code2, String fileN) {
		int year,month,day,hour,minute,second;
		String defaultS="999998";
		double  defaultF=999998;
		int defaultInt=999998;
//		PreparedStatement pStatement = null;
		try {
			String sql = "INSERT INTO "+ParaTableName+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,"
					+ "V04001_02,V04002_02,V04003_02,V04004_02,V01301,V01300,V05001,V06001,V07001,V04001,"
					+ "V04002,V04003,V04004,V04005,V04006,V01081,V01082,V02067,V02081,V02084,"
					+ "V02095,V02096,V02097,V02303,V02304,V02420,V02421,V02305,V31001,V02082,"
					+ "V02102,V02307,V02308,V02309,V02422,V02310,V12312,V12313,V12401,V10303,"
					+ "V10304,V10402,V13360,V13361,V13362,V02311,V04300_017_01,V04300_017_02,V04300_018_01,V04300_018_02,"
					+ "V35035_01,V35035_02,V35300_01,V35300_02,V07022_01,V07022_02,V12001,V10004,V13003,V11001,"
					+ "V11002,V20001,V20012_01,V20012_02,V20012_03,V20010,V20051,V20003_01,V20003_02,V20003_03,"
					+ "V05021,V07021,V06021,D_SOURCE_ID) VALUES("
					+"?, ?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
					+ " ?, ?, ?, ?)";
			
			Map<String, Object> row = new HashMap<>();
			int successRowCount = 0;
//			List<String> sqls = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Calendar calendar = Calendar.getInstance();//year,month,day,hour,minute,second					
			for(int idx = 0; idx < uparLBands.size(); idx ++){
				
				Date date = uparLBands.get(idx).getCastingUtcTime();//获取施放时间（世界时）
				calendar.setTime(date);//Date类型转int
				year = calendar.get(Calendar.YEAR);//获取年
				month = calendar.get(Calendar.MONTH) + 1;//获取月
				day = calendar.get(Calendar.DAY_OF_MONTH);//获取日
				hour = calendar.get(Calendar.HOUR_OF_DAY);//获取日
				minute = calendar.get(Calendar.MINUTE);//获取分
				second = calendar.get(Calendar.SECOND);//获取秒
				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				//di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE(sodcode);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(V_TT);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败
				
				int stationNumberN = 999999;
				String stat = uparLBands.get(idx).getStationNumberChina();//获取台站号
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);
				else
					stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
				String lat = String.valueOf((int)(uparLBands.get(idx).getLatitude() * 1e6));
				String lon = String.valueOf((int)(uparLBands.get(idx).getLongitude() * 1e6));
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");						
				di.setIIiii(uparLBands.get(idx).getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(uparLBands.get(idx).getLatitude().toString());
				di.setLONGTITUDE(uparLBands.get(idx).getLongitude().toString());
				int ii=1;
				Date dataTime = new Date(date.getTime());
				dataTime.setMinutes(0);
				dataTime.setSeconds(0);
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTime(dataTime);
				if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 5){
					calendar2.add(Calendar.HOUR_OF_DAY, 1);
				}else if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 1){
					calendar2.add(Calendar.HOUR_OF_DAY, -1);
				}else if(calendar2.get(Calendar.HOUR_OF_DAY) % 6 == 2){
					calendar2.add(Calendar.HOUR_OF_DAY, -2);
				}
				//D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,
//				pStatement.setString(ii++, sdf.format(date.getTime())+"_"+sdf.format(calendar2.getTime())+"_"+stat+"_"+lat+"_"+lon);//记录标识D_RECORD_ID	
				row.put("D_RECORD_ID", sdf.format(date.getTime())+"_"+sdf.format(calendar2.getTime())+"_"+stat+"_"+lat+"_"+lon);
//				pStatement.setString(ii++,sodcode);//资料标识	
				row.put("D_DATA_ID", sodcode);
//				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
				row.put("D_IYMDHM", new Timestamp(new Date().getTime()));
//				pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
				row.put("D_RYMDHM", new Timestamp(recv_time.getTime()));
//				pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间d_update_id
				row.put("D_UPDATE_TIME", new Timestamp(new Date().getTime()));
//				pStatement.setTimestamp(ii++, new Timestamp(new Date(calendar2.getTime().getTime()).getTime()));//资料时间D_DATETIME
				row.put("D_DATETIME", new Timestamp(new Date(calendar2.getTime().getTime()).getTime()));
//				pStatement.setString(ii++, uparLBands.get(idx).getCorrectSign());//更正标识
				row.put("V_BBB", uparLBands.get(idx).getCorrectSign());
				
				//"V04001_02,V04002_02,V04003_02,V04004_02,V01301,V01300,V05001,V06001,V07001,V04001"
//				pStatement.setInt(ii++, calendar2.getTime().getYear()+1900);//资料年
				row.put("V04001_02", calendar2.getTime().getYear()+1900);
//				pStatement.setInt(ii++, calendar2.getTime().getMonth()+1);//资料月
				row.put("V04002_02", calendar2.getTime().getMonth()+1);
//		 		pStatement.setInt(ii++,calendar2.getTime().getDate());//资料日
				row.put("V04003_02", calendar2.getTime().getDate());
//				pStatement.setInt(ii++, calendar2.getTime().getHours());//资料时
				row.put("V04004_02", calendar2.getTime().getHours());
//				pStatement.setString(ii++, stat);//字符区站号V01301
				row.put("V01301", stat);
//				pStatement.setInt(ii++, stationNumberN);//数字区站号V01300
				row.put("V01300", stationNumberN);
//				pStatement.setDouble(ii++, uparLBands.get(idx).getLatitude());//纬度
				row.put("V05001", uparLBands.get(idx).getLatitude());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getLongitude());//经度
				row.put("V06001", uparLBands.get(idx).getLongitude());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getHeightOfSationGroundAboveMeanSeaLevel());//测站海拔高度V07001
				row.put("V07001", uparLBands.get(idx).getHeightOfSationGroundAboveMeanSeaLevel());
//				pStatement.setInt(ii++, year);//气球施放时间（年）	
				row.put("V04001", year);
				
				//"V04002,V04003,V04004,V04005,V04006,V01081,V01082,V02067,V02081,V02084,"
//				pStatement.setInt(ii++, month);//气球施放时间（月）
				row.put("V04002", month);
//				pStatement.setInt(ii++, day);//气球施放时间（日）
				row.put("V04003", day);
//				pStatement.setInt(ii++, hour);//气球施放时间（时）
				row.put("V04004", hour);
//				pStatement.setInt(ii++, minute);//气球施放时间（分）
				row.put("V04005", minute);
//				pStatement.setInt(ii++, second);//气球施放时间（秒）
				row.put("V04006", second);
//				pStatement.setString(ii++, defaultS);//无线电探空仪系列号V01081
				row.put("V01081", defaultS);
//				pStatement.setInt(ii++, defaultInt);//无线电探空仪上升序列号V01082
				row.put("V01082", defaultInt);
//				pStatement.setDouble(ii++, defaultF);//无线电探空仪业务频率V02067
				row.put("V02067", defaultF);
//				pStatement.setInt(ii++, defaultInt);//气球类型V02081
				row.put("V02081", defaultInt);
//				pStatement.setInt(ii++, defaultInt);//气球中所用的气体类型V02084
				row.put("V02084", defaultInt);
				
				//"V02095,V02096,V02097,V02303,V02304,V02420,V02421,V02305,V31001,V02082,"
//				pStatement.setInt(ii++, defaultInt);//气压传感器类型V02095
				row.put("V02095", defaultInt);
//				pStatement.setInt(ii++, defaultInt);//温度传感器类型V02096
				row.put("V02096", defaultInt);
//				pStatement.setInt(ii++, defaultInt);//湿度传感器类型V02097
				row.put("V02097", defaultInt);
//				pStatement.setString(ii++, uparLBands.get(idx).getTypeOfDetectionSystem());//探测系统型号V02303
				row.put("V02303", uparLBands.get(idx).getTypeOfDetectionSystem());
//				pStatement.setString(ii++, uparLBands.get(idx).getRadiosondeModel());//探空仪型号V02304
				row.put("V02304", uparLBands.get(idx).getRadiosondeModel());
//				pStatement.setInt(ii++, defaultInt);//探空仪生产厂家V02420
				row.put("V02420", defaultInt);
//				pStatement.setInt(ii++, defaultInt);//探空仪生产日期V02421
				row.put("V02421", defaultInt);
//				pStatement.setString(ii++, uparLBands.get(idx).getInstrumentNumber());//探空仪编号V02305
				row.put("V02305", uparLBands.get(idx).getInstrumentNumber());
//				pStatement.setInt(ii++, uparLBands.get(idx).getCastCount());//施放计数V31001
				row.put("V31001", uparLBands.get(idx).getCastCount());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getBallWeight());//气球重量V02082
				row.put("V02082", uparLBands.get(idx).getBallWeight());
				
				//"V02102,V02307,V02308,V02309,V02422,V02310,V12312,V12313,V12401,V10303,"
//				pStatement.setDouble(ii++, uparLBands.get(idx).getAntennaHeight());//探测系统天线高度V02102
				row.put("V02102", uparLBands.get(idx).getAntennaHeight());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getAdditiveWeight());//附加物重量V02307
				row.put("V02307", uparLBands.get(idx).getAdditiveWeight());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getTotalForce());//总举力V02308
				row.put("V02308", uparLBands.get(idx).getTotalForce());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getNetLifting());//净举力V02309
				row.put("V02309", uparLBands.get(idx).getNetLifting());
//				pStatement.setDouble(ii++, defaultF);//球与探空仪间实际绳长V02422
				row.put("V02422", defaultF);
//				pStatement.setDouble(ii++, uparLBands.get(idx).getAverageSpeedOfLift());//平均升速V02310
				row.put("V02310", uparLBands.get(idx).getAverageSpeedOfLift());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getBaseTemperatureValue());//温度基测值V12312
				row.put("V12312", uparLBands.get(idx).getBaseTemperatureValue());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getInstrumentTemperatureValue());//温度仪器值V12313
				row.put("V12313", uparLBands.get(idx).getInstrumentTemperatureValue());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getTemperatureDeviation());//温度偏差V12401
				row.put("V12401", uparLBands.get(idx).getTemperatureDeviation());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getBasePressureValue());//气压基测值V10303
				row.put("V10303", uparLBands.get(idx).getBasePressureValue());
				
				//"V10304,V10402,V13360,V13361,V13362,V02311,V04300_017_01,V04300_017_02,V04300_018_01,V04300_018_02,"
//				pStatement.setDouble(ii++, uparLBands.get(idx).getInstrumentPressureValue());//气压仪器值V10304
				row.put("V10304", uparLBands.get(idx).getInstrumentPressureValue());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getPressureDeviation());//气压偏差V10402
				row.put("V10402", uparLBands.get(idx).getPressureDeviation());
//				pStatement.setInt(ii++, uparLBands.get(idx).getBaseRelativeHumidityValue());//相对湿度基测值V13360
				row.put("V13360", uparLBands.get(idx).getBaseRelativeHumidityValue());
//				pStatement.setInt(ii++,  uparLBands.get(idx).getInstrumentRelativeHumidityValue());//相对湿度仪器值V13361
				row.put("V13361", uparLBands.get(idx).getInstrumentRelativeHumidityValue());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getRelativeHumidityDeviation());//相对湿度偏差V13362
				row.put("V13362", uparLBands.get(idx).getRelativeHumidityDeviation());
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstrumentTestConclusion());//仪器检测结论V02311
				row.put("V02311", uparLBands.get(idx).getInstrumentTestConclusion());
//				pStatement.setLong(ii++, Long.valueOf(sdf.format(uparLBands.get(idx).getCastingUtcTime().getTime())));//施放时间（世界时）V04300_017_01
				row.put("V04300_017_01", Long.valueOf(sdf.format(uparLBands.get(idx).getCastingUtcTime().getTime())));
//				pStatement.setLong(ii++, Long.valueOf(sdf.format(uparLBands.get(idx).getCastingLocalTime().getTime())));//施放时间（地方时）	V04300_017_02
				row.put("V04300_017_02", Long.valueOf(sdf.format(uparLBands.get(idx).getCastingLocalTime().getTime())));
//				pStatement.setLong(ii++, Long.valueOf(sdf.format(uparLBands.get(idx).getSoundingTerminationUtcTime().getTime())));//探空终止时间（世界时）	V04300_018_01
				row.put("V04300_018_01", Long.valueOf(sdf.format(uparLBands.get(idx).getSoundingTerminationUtcTime().getTime())));
//				pStatement.setLong(ii++, Long.valueOf(sdf.format(uparLBands.get(idx).getAnemometryTerminationUtcTime().getTime())));//测风终止时间（世界时）	V04300_018_02
				row.put("V04300_018_02", Long.valueOf(sdf.format(uparLBands.get(idx).getAnemometryTerminationUtcTime().getTime())));
				
				// "V35035_01,V35035_02,V35300_01,V35300_02,V07022_01,V07022_02,V12001,V10004,V13003,V11001,"
//				pStatement.setInt(ii++, uparLBands.get(idx).getReasonForSoundingTermination());//探空终止原因	V35035_01
				row.put("V35035_01", uparLBands.get(idx).getReasonForSoundingTermination());
//				pStatement.setInt(ii++, uparLBands.get(idx).getReasonForAnemometryTermination());//测风终止原因	V35035_02
				row.put("V35035_02", uparLBands.get(idx).getReasonForAnemometryTermination());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getSoundingTerminationHeight());//探空终止高度	V35300_01
				row.put("V35300_01", uparLBands.get(idx).getSoundingTerminationHeight());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getAnemometryTerminationHeight());//测风终止高度	V35300_02
				row.put("V35300_02", uparLBands.get(idx).getAnemometryTerminationHeight());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getSolarElevationAngle());//施放时太阳高度角	V07022_01
				row.put("V07022_01", uparLBands.get(idx).getSolarElevationAngle());
//				pStatement.setDouble(ii++, defaultF);//终止时太阳高度角	V07022_02
				row.put("V07022_02", defaultF);
//				pStatement.setDouble(ii++, uparLBands.get(idx).getInstantGroundTemperature());//施放瞬间地面气温	V12001
				row.put("V12001", uparLBands.get(idx).getInstantGroundTemperature());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getInstantGroundPressure());//施放瞬间地面气压	V10004
				row.put("V10004", uparLBands.get(idx).getInstantGroundPressure());
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstantGroundRelativeHumidity());//施放瞬间地面相对湿度	V13003
				row.put("V13003", uparLBands.get(idx).getInstantGroundRelativeHumidity());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getInstantGroundWindDirection());//施放瞬间地面风向	V11001
				row.put("V11001", uparLBands.get(idx).getInstantGroundWindDirection());
				
				//"V11002,V20001,V20012_01,V20012_02,V20012_03,V20010,V20051,V20003_01,V20003_02,V20003_03,"
//				pStatement.setDouble(ii++, uparLBands.get(idx).getInstantGroundWindSpeed());//施放瞬间地面风速	V11002
				row.put("V11002", uparLBands.get(idx).getInstantGroundWindSpeed());
				double temp=uparLBands.get(idx).getInstantVisibility().doubleValue()*1000;
				int instantVisibility=new Double(temp).intValue();
//				pStatement.setInt(ii++, instantVisibility);//施放瞬间能见度	V20001
				row.put("V20001", instantVisibility);
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstantCloudGenus1());//施放瞬间云属1	V20012_01
				row.put("V20012_01", uparLBands.get(idx).getInstantCloudGenus1());
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstantCloudGenus2());//施放瞬间云属2	V20012_02
				row.put("V20012_02", uparLBands.get(idx).getInstantCloudGenus2());
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstantCloudGenus3());//施放瞬间云属3	V20012_03
				row.put("V20012_03", uparLBands.get(idx).getInstantCloudGenus3());
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstantTotalCloudCover());//施放瞬间总云量	V20010
				row.put("V20010", uparLBands.get(idx).getInstantTotalCloudCover());
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstantLowCloudCover());//施放瞬间低云量	V20051
				row.put("V20051", uparLBands.get(idx).getInstantLowCloudCover());
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstantWeatherPhenomenon1());//施放瞬间天气现象1	V20003_01
				row.put("V20003_01", uparLBands.get(idx).getInstantWeatherPhenomenon1());
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstantWeatherPhenomenon2());//施放瞬间天气现象2	V20003_02
				row.put("V20003_02", uparLBands.get(idx).getInstantWeatherPhenomenon2());
//				pStatement.setInt(ii++, uparLBands.get(idx).getInstantWeatherPhenomenon3());//施放瞬间天气现象3	V20003_03
				row.put("V20003_03", uparLBands.get(idx).getInstantWeatherPhenomenon3());
				
				//V05021,V07021,V06021,D_SOURCE_ID
//				pStatement.setDouble(ii++, uparLBands.get(idx).getCastPointAzimuth());//施放点方位角	V05021
				row.put("V05021", uparLBands.get(idx).getCastPointAzimuth());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getCastPointElevation());//施放点仰角	V07021
				row.put("V07021", uparLBands.get(idx).getCastPointElevation());
//				pStatement.setDouble(ii++, uparLBands.get(idx).getCastPointDistance());//施放点距离（探测仪器与天线之间距离）	V06021
				row.put("V06021", uparLBands.get(idx).getCastPointDistance());
//				pStatement.setString(ii++, cts_code);//资料四级编码D_SOURCE_ID
				row.put("D_SOURCE_ID", cts_code);
	
				System.out.println(row.toString());
	
				//入OTS
				try {
					OTSDbHelper.getInstance().insert(ParaTableName, row);
					System.out.println("!!para!!成功");
					infoLogger.info("Insert one line successfully!\n");
					diQueues.offer(di);
					successRowCount ++;
				}catch (Exception e) {
					e.printStackTrace();
					di.setPROCESS_STATE("0");
					diQueues.offer(di);
					infoLogger.error(row + "\n");
					infoLogger.error(e.getMessage() + "\n");
					if(e.getClass() == ClientException.class) {
						infoLogger.error("DataBase connection error!\n");
					}
					System.out.println(row.toString());
					return false;
				}
				
			}// end for			
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		diQueues.clear();
		return true;
	}
	/**
	 * 
	 * @Title: execute_sql   
	 * @Description: TODO(批量入库失败时，采用逐条提交)   
	 * @param: sqls 待执行的查询语句
	 * @param: connection      数据库连接
	 * @param:  fileN
	 * @param:  loggerBuffer      
	 * @return: void      
	 * @throws
	 */
	private static void execute_sql(List<String> sqls, Connection connection, String fileN, StringBuffer loggerBuffer) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				/*if(StartConfig.getDatabaseType() == 1) {
					pStatement.execute("select last_txc_xid()");
				}*/
				try {
					pStatement.execute(sqls.get(i));
				} catch (Exception e) {
					loggerBuffer.append("\n filename："+fileN
							+"\n " + listDi.get(i).getIIiii() + " " + listDi.get(i).getDATA_TIME()
							+"\n excute sql error："+sqls.get(i)+"\n "+e.getMessage());
					listDi.get(i).setPROCESS_STATE("1");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n create Statement error"+e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close Statement error"+e.getMessage());
				}
			}
		}
		
	}
		
		/**
		 * The main method.
		 *
		 * @param args the arguments
		 */
		public static void main(String[] args) {
			DecodeLbandUparDetection decodeLbandUparDetection = new DecodeLbandUparDetection();
//			String filepath = "D:\\HUAXIN\\DataProcess\\B.0003.0001.R001\\ss\\Z_UPAR_I_44373_20181223231845_O_TEMP-L.txt";
			String filepath = "D:\\ll\\Z_UPAR_I_54374_20190227231522_O_TEMP-L.txt";
			
			File file = new File(filepath);
			ParseResult<UparLBand> parseResult = decodeLbandUparDetection.DecodeFile(file);
			DataBaseAction action = null;
			Date recv_time = new Date();
			StringBuffer loggerBuffer = new StringBuffer();
			String fileN1 = null;
		    fileN1 = file.getName();
//			if (StartConfig.getDatabaseType() == 1) {
				action = OTSService.insertDBService(parseResult, recv_time,fileN1,loggerBuffer);
				System.out.println("insertDBService over!");
//			}
		
		}
}
