package cma.cimiss2.dpc.indb.surf.dc_surf_tow.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.NumberUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.bean.surf.Tower;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.surf.dc_surf_tow.ReadIni;

public class OTSService {
	private static final int DEFAULT_VALUE = 999999;
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	// 是否是更新文件
	Map<String, Object> proMap = StationInfo.getProMap();
	private static String sod_code = StartConfig.sodCode();
	private static String cts_code = StartConfig.ctsCode();
	
	public static void setDiQueues(BlockingQueue<StatDi> diQueue){
		OTSService.diQueues = diQueue;
	}
	
	public static DataBaseAction insert_ots(List<Tower> towers, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		int successRowCount = towers.size();
		StatDi di = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
		for (Tower tower : towers) {
			Map<String, Object> row = new HashMap<String, Object>();
			int stationNumberN = DEFAULT_VALUE;
			String stationNumberC = tower.getStationNumberC();	//字符站号
			Double hight = tower.getHight();//观测平台距地面的高度。单位-米
			Double windSpeedNW = tower.getWindSpeedNW();	//西北方向伸臂上风速传感器观测值。单位-米/秒
			Double windSpeedSE = tower.getWindSpeedSE();	//东南方向伸臂上风速传感器观测值。单位-米/秒
			Double windDirection = tower.getWindDirection();//风向传感器观测值。单位－度
			Double relativeHumidity = tower.getRelativeHumidity();//相对湿度传感器观测值。单位-百分比
			Double temperature = tower.getTemperature();	//大气温度传感器观测值。单位-摄氏度
			Date obsTime = tower.getObsTime();	//观测时间
			// 入库时间
			Double latitudeBJ = 39.9744; // 纬度
			Double longitudeBJ = 116.3712; // 经度
			Double heightBJ = 325.0; // 台站高度
			Double latitudeXH = 39.7534; // 纬度
			Double longitudeXH = 116.9604; // 经度
			Double heightXH = 102.0; // 台站高度
			double latitude = latitudeBJ;
			double longitude = longitudeBJ;
			double height = heightBJ;
			if(stationNumberC.contains("XH")){
				latitude = latitudeXH;
				longitude = longitudeXH;
				height = heightXH;
			}
			String lengthStr = "00000";
			Calendar calendar = Calendar.getInstance();
            calendar.setTime(obsTime);
            calendar.add(Calendar.HOUR_OF_DAY, -8);
            Date date = calendar.getTime();
//            date.setMinutes(0);
//            date.setSeconds(0);
			String primkey = TimeUtil.date2String(calendar.getTime(), TimeUtil.DATE_FMT_YMDHMS) //
					+ "_" + (lengthStr.substring(0, 5 - stationNumberC.length()) + stationNumberC) //
//					+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(longitude * 1000000), 10) //
//					+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(latitude * 1000000), 9)//
					+ "_" + NumberUtil.Num2DesignatedLenWithMark(Math.round(hight * 100), 8);
			row.put("D_RECORD_ID", primkey);
            row.put("D_DATA_ID", sod_code);
            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
            row.put("D_DATETIME", TimeUtil.date2String(date,"yyyy-MM-dd HH:mm:ss"));
           
            row.put("V_BBB", "000"); // 默认000，首次入库可不填，数据库字段默认000
            row.put("V01301", stationNumberC); // 区站号（字符）
			row.put("V01300", stationNumberN); // 区站号（数字）
			row.put("V05001", latitude); // 纬度
			row.put("V06001", longitude); // 经度
			row.put("V07001", height); // 测站海拔高度
			row.put("V07002", hight); // 气压传感器海拔高度
			row.put("V02320", ReadIni.getIni().getValue(ReadIni.SECTION_TOWER, "collector_model"));	//采集器型号
			row.put("V02001", "999998");	//测站类型
			row.put("V02169", "999998");	//风速表类型
			
			row.put("V02096", "999998");	//温度传感器类型
			row.put("V02097", "999998");	//湿度传感器类型
			row.put("V04001", calendar.get(Calendar.YEAR)); // 资料观测年
			row.put("V04002", calendar.get(Calendar.MONTH) + 1); // 资料观测月
			row.put("V04003", calendar.get(Calendar.DAY_OF_MONTH)); // 资料观测日
			row.put("V04004", calendar.get(Calendar.HOUR_OF_DAY)); // 资料观测时
			row.put("V04005", calendar.get(Calendar.MINUTE)); // 资料观测分
			row.put("V04006", calendar.get(Calendar.SECOND)); // 资料观测秒
			
			row.put("V12001", temperature); // 气温
			row.put("V13003", relativeHumidity); // 相对湿度
			row.put("V11001", windDirection);	//风向
			row.put("V11351_NW", windSpeedNW);	
			row.put("V11351_SE", windSpeedSE);
			row.put("D_SOURCE_ID", cts_code);
			
			di = new StatDi();
			di.setFILE_NAME_O(fileN);
			di.setDATA_TYPE(sod_code);
			di.setDATA_TYPE_1(cts_code);
			di.setTT("中科院铁塔");
			di.setTRAN_TIME(TimeUtil.date2String(recv_time, TimeUtil.DEFAULT_DATETIME_FORMAT));
			di.setPROCESS_START_TIME(TimeUtil.getSysTime());
			di.setFILE_NAME_N(fileN);
			di.setBUSINESS_STATE("0"); // 0成功，1失败
			di.setPROCESS_STATE("0"); // 0成功，1失败
			di.setIIiii(stationNumberC);
			di.setLATITUDE(String.valueOf(latitude));
			di.setLONGTITUDE(String.valueOf(longitude));
			di.setDATA_TIME(TimeUtil.date2String(calendar.getTime(), TimeUtil.DEFAULT_DATETIME_FORMAT));
			di.setPROCESS_END_TIME(TimeUtil.getSysTime());
			di.setRECORD_TIME(TimeUtil.getSysTime());
			
			try {
				OTSDbHelper.getInstance().insert(tablename, row);
				diQueues.offer(di);
			}  catch (Exception e) {
				successRowCount = successRowCount -1;
				di.setPROCESS_STATE("1");
				diQueues.offer(di);
				loggerBuffer.append(row);
				loggerBuffer.append(e.getMessage());
			}
        }
		loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
	    loggerBuffer.append(" INSERT FAILED COUNT : " + (towers.size() - successRowCount) + "\n");
		return DataBaseAction.SUCCESS;
	}
}
