package cma.cimiss2.dpc.indb.ocen.dc_ocean_ori_ship.service;

import java.io.File;
import java.math.BigDecimal;
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
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ocean.OriShip;
import cma.cimiss2.dpc.decoder.ocen.DecodeOriShip;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;


// TODO: Auto-generated Javadoc
/**
 * The Class DbService.
 */
public class OTSService {
	
	/** The retry map. */
	HashMap<String, Integer> retryMap;
	
	/** The list di. */
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The sod report. */
	public static String sod_report = StartConfig.reportSodCode();
	
	/** The sod code. */
	public static String sod_code = StartConfig.sodCode();
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();
	
	/** The v cccc. */
	public static String V_CCCC = "9999";
	
	/** The default int. */
	public static int defaultInt = 999999;
	
	/** The pro map. */
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	
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
	
	/**
	 * Process success report.
	 *
	 * @param parseResult the parse result
	 * @param recv_time 解码文件的路径
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @param V_BBB the v bbb
	 * @return the data base action
	 * @Title: processSuccessReport
	 * @Description:(中远人工观测船舶资料解码入库) 
	 * @return: DataBaseAction
	 * @throws: 
	 */
	
	
	/**
	 * Insert ship.
	 *
	 * @param ships 待入库对象集
	 * @param connection the connection
	 * @param recv_time the recv time
	 * @param fileN the file N
	 * @param loggerBuffer the logger buffer
	 * @param V_TT the v tt
	 * @Title: insertShip
	 * @Description:(国内海上船舶资料入库) 
	 * @return: void
	 * @throws: 
	 */
	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<OriShip> parseResult, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<OriShip> list = parseResult.getData();
		
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	        	OriShip ship = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            
				Date date = ship.getShiptime();
				String stat = ship.getShipCallSign();
				double latitude=ship.getLatitude();
				double longtitude=ship.getLongtitude();
				//计算经纬度
				double [] cal=calcuLatAndLon(stat,latitude,longtitude,fileN);
				latitude=cal[0];
				longtitude=cal[1];
				String lat = String.valueOf((int)(latitude * 1e6));
				String lon = String.valueOf((int)(longtitude * 1e6));
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");
				row.put("D_RECORD_ID", sdf.format(date) + "_" + stat + "_" + lat + "_" + lon);
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        Date newDate = new Date(date.getTime());
//		        newDate.setMinutes(0);
//		        newDate.setSeconds(0);
		        row.put("D_DATETIME", TimeUtil.date2String(newDate,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				// "V01011,V05001,V06001,V07001,V07304,V07301,V07305,V02001,V02141,V04001,"
				row.put("V01011", stat);
				row.put("V05001", ship.getLatitude());
				row.put("V06001", ship.getLongtitude());
				row.put("V07001", "999998");
				row.put("V07304", "999998");
				row.put("V07301", "999998");
				row.put("V07305", "999998");
				row.put("V02001", "999998");
				row.put("V02141", "999998");
				row.put("V04001", date.getYear() + 1900);
				// "V04002,V04003,V04004,V04005,V04006,V01012,V01013,V05021,V20003,V10051,"
				row.put("V04002", date.getMonth() + 1);
				row.put("V04003", date.getDate());
				row.put("V04004", date.getHours());
				row.put("V04005", date.getMinutes());
				row.put("V04006", date.getSeconds());
				row.put("V01012", "999998");
				row.put("V01013", ship.getShipMovingSpeed());
				row.put("V05021", ship.getBowAzimuth());
				row.put("V20003", ship.getWeatherCondition());
				row.put("V10051", ship.getSeaLevelPressure());
				// "V12001,V12002,V11001,V11002,V11301,V22042,V20001,V20012,V20011,V22022_1,"
				row.put("V12001", ship.getDryballTemperature());
				row.put("V12002", ship.getWetballTemperature());
				row.put("V11001", ship.getWindDir());
				row.put("V11002", "999998");
				row.put("V11301", ship.getWindLevel());
				row.put("V22042", ship.getSeaTemperature());
				row.put("V20001", "999998");
				row.put("V20012", ship.getCloudShape());
				row.put("V20011", ship.getCloudAmount());
				row.put("V22022_1", ship.getWaveHeightManually());  // V22022_1
				// "V22022_2,V22303, V_BBB,V20256"
				row.put("V22022_2", "999998");
				row.put("V22303", "999998");
				row.put("V_BBB","000");
				row.put("V20256",ship.getVisibilityLevel());
				
	
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
		
				di.setTT("船舶观测资料");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("1"); //1成功，0失败
				di.setPROCESS_STATE("1");  //1成功，0失败	
				di.setLATITUDE(String.valueOf(latitude));
				di.setLONGTITUDE(String.valueOf(longtitude));
				di.setIIiii(stat);
				di.setDATA_TIME(TimeUtil.date2String(ship.getShiptime(), "yyyy-MM-dd HH:mm"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				
	            try {        	
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("0");
					diQueues.offer(di);
					loggerBuffer.append(row);
					loggerBuffer.append(e.getMessage());
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}
				}
	        }
            loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
	
	

	
	//计算经纬度
	public	static double [] calcuLatAndLon(String station,double latitude,double longitude,String filename){
	  Map<String, Object> proMap = StationInfo.getProMap();
	  double[] LatAndLon={latitude,longitude};
	  if (latitude<-90||latitude>90) {
		  latitude=999999;
	  }
	  if(longitude<-180||longitude>180){
		  longitude=999999;
	  }
	  if(latitude==999999.0||longitude==999999.0){
			String info = (String) proMap.get(station + "+09");
			if(info == null||"".equals(info)) {
				infoLogger.error("经纬度无法解析！"  + filename);
				sendEI(station);
				return LatAndLon;
				
			}else{
				String[] infos = info.split(",");
				if(!(infos[1].trim().equals("null") || infos[1].trim().equals(""))){
					try{
					 longitude = Double.parseDouble(infos[1]);//经度
					}catch (Exception e) {
						infoLogger.error("经纬度无法解析！"  + filename);
						sendEI(station);
						return LatAndLon;
					}
				}else{
					infoLogger.error("经纬度无法解析！"  + filename);
					sendEI(station);
					return LatAndLon;
				}
				if(!(infos[2].trim().equals("null") || infos[2].trim().equals(""))){
					try{
					 latitude = Double.parseDouble(infos[2]);//纬度
					}catch (Exception e) {
						infoLogger.error("经纬度无法解析！"  + filename);
						sendEI(station);
						return LatAndLon;
					}
				}else{
					infoLogger.error("经纬度无法解析！"  + filename);
					sendEI(station);
					return LatAndLon;
				}
				latitude= Double.parseDouble(String.format("%.4f", latitude));
				longitude= Double.parseDouble(String.format("%.4f", longitude));
				LatAndLon[0]=latitude;
				LatAndLon[1]=longitude;
			}
		}else{
			latitude= Double.parseDouble(String.format("%.4f", latitude));
			longitude= Double.parseDouble(String.format("%.4f", longitude));
			LatAndLon[0]=latitude;
			LatAndLon[1]=longitude;
		}
	  return LatAndLon;
	}
	public static void sendEI(String station){
		String event_type = EIEventType.STATION_FILE_ERROR.getCode();
		EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
		if(ei == null) {
			infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
		}else {
			if(StartConfig.isSendEi()) {
				ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				ei.setKObject("cma.cimiss2.dpc.indb.ocen.dc_ocean_ori_ship.service.DbService");
				ei.setKEvent("获取台站信息异常");
				ei.setKIndex("全球高空台站："+station+"未能获取台站信息配置，无法获取经纬度");
				RestfulInfo restfulInfo = new RestfulInfo();
				restfulInfo.setType("SYSTEM.ALARM.EI ");
				restfulInfo.setName("获取台站信息EI告警信息");
				restfulInfo.setMessage("获取台站信息EI告警信息");
				restfulInfo.setFields(ei);
				List<RestfulInfo> restfulInfos = new ArrayList<>();
				restfulInfos.add(restfulInfo);
				RestfulSendData.SendData(restfulInfos, SendType.EI);
			}
		}
	}
	public static void main(String[] args) {
		DecodeOriShip decodeShip = new DecodeOriShip();
		ParseResult<OriShip> parseResult = decodeShip.DecodeFile(new File("D:\\中远人工观测船舶资料\\中远船舶样例数据\\ZY_M_20190724140030.txt"));
		List<OriShip> list = parseResult.getData();
		System.out.println(list.size());
	}
}
