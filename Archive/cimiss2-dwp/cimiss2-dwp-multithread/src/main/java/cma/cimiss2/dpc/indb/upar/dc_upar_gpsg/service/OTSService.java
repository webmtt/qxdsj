package cma.cimiss2.dpc.indb.upar.dc_upar_gpsg.service;

import java.text.SimpleDateFormat;
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

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.GPS_MET;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

// TODO: Auto-generated Javadoc
/**
 * The Class OTSService.
 */
public class OTSService {
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();
	
	/** The sod code. */
	public static String sod_code = StartConfig.sodCode();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	
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
	 * Insert ots.
	 *
	 * @param parseResult the parse result
	 * @param tablename the tablename
	 * @param recv_time the recv time
	 * @param loggerBuffer the logger buffer
	 * @param fileN the file N
	 * @return the data base action
	 */
	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<GPS_MET> parseResult, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<GPS_MET> list = parseResult.getData();
		
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	        	GPS_MET gps_MET = list.get(i);
	            Map<String, Object> row = new HashMap<String, Object>();
	            int stationNumberN = 999999;
				String stat = gps_MET.getStationNumberChina();
				if (Pattern.matches("\\d{5}", stat))
					stationNumberN = Integer.parseInt(stat);					
				else
					stationNumberN = Integer.parseInt(StationCodeUtil.stringToAscii(stat));
	        	String lat = String.valueOf((int)(gps_MET.getLatitude() * 1e6));
				String lon = String.valueOf((int)(gps_MET.getLongtitude() * 1e6));
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");
				row.put("D_RECORD_ID", sdf.format(gps_MET.getObservationTime())+"_"+gps_MET.getStationNumberChina()+"_"+lat+"_"+lon);
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(gps_MET.getObservationTime(),"yyyy-MM-dd HH:mm:ss"));  
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				row.put("V01301", stat);
				row.put("V01300", stationNumberN);
				row.put("V_STATIONCODE", gps_MET.getStationCode());
				row.put("V05001", gps_MET.getLatitude()); // 纬度
				row.put("V06001", gps_MET.getLongtitude()); // 经度
				row.put("V07001", gps_MET.getHeightOfSationGroundAboveMeanSeaLevel()); // 测站高度
				Date date = new Date(gps_MET.getObservationTime().getTime());  //资料时间
				row.put("V04001", date.getYear() + 1900);
				row.put("V04002", date.getMonth() + 1);
				row.put("V04003", date.getDate());
				row.put("V04004", date.getHours());
				row.put("V04005", date.getMinutes());

				//总的天顶延迟|气压|气温|相对湿度|可降水量
				row.put("V07350", gps_MET.getTotalZenithDelay());  //总的
				row.put("V10004", gps_MET.getPressure());
				row.put("V12001", gps_MET.getAirTemperature());
				row.put("V13003", gps_MET.getRelativeHumidity());
				row.put("V13016", gps_MET.getPrecipitationAmount());
				row.put("V_BBB", "000");
	            
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
			
				di.setTT("GPS/MET水汽数据产品");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setLATITUDE(String.valueOf(gps_MET.getLatitude()));
				di.setLONGTITUDE(String.valueOf(gps_MET.getLongtitude()));
				di.setIIiii(gps_MET.getStationNumberChina());
				di.setDATA_TIME(TimeUtil.date2String(gps_MET.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
					
		        try {	            	
					OTSDbHelper.getInstance().insert(tablename, row);
					diQueues.offer(di);
				}  catch (Exception e) {
					loggerBuffer.append(e.getMessage());
					successRowCount = successRowCount -1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					if(e.getClass() == ClientException.class) {
						return DataBaseAction.CONNECTION_ERROR;
					}else {
						loggerBuffer.append(e.getMessage());
						continue;
					}
				}

	        }
	        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
	        
	       }
		return DataBaseAction.SUCCESS;
	}
	

}
