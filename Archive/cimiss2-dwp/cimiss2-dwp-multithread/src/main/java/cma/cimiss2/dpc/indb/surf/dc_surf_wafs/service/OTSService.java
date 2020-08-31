package cma.cimiss2.dpc.indb.surf.dc_surf_wafs.service;

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
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.upar.WAFS;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

// TODO: Auto-generated Javadoc
/**
 * The Class OTSService.
 */
public class OTSService {
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The sdf. */
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	/** The cts code. */
	public static String cts_code = StartConfig.ctsCode();
	
	/** The sod code. */
	public static String sod_code = StartConfig.sodCode();
	
	/** The sod report code. */
	public static String sod_report_code = StartConfig.reportSodCode();
	
	/** The pro map. */
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		OTSService.diQueues = diQueues;
	}
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	
	/**
	 * Insert ots.
	 *
	 * @param wafss the wafss
	 * @param tablename the tablename
	 * @param recv_time the recv time
	 * @param loggerBuffer the logger buffer
	 * @param fileN the file N
	 * @return the data base action
	 */
	public static DataBaseAction insert_ots(List<WAFS> wafss, String tablename, Date recv_time, StringBuffer loggerBuffer, String fileN) {
		Map<String, Object> proMap = StationInfo.getProMap();
		if(wafss != null && wafss.size() > 0) {
			int successRowCount = wafss.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
			WAFS wafs = new WAFS();
			for (int i = 0; i < wafss.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				wafs = wafss.get(i);
				String V_TT = "";
				if(wafs.getReportType().equals("METAR"))
					V_TT = "SA";
				else V_TT = "SP";
				String stat = wafs.getTerminalSign();
				String info = (String) proMap.get(stat + "+01");
				double latitude = 999999.0;
				double longtitude = 999999.0;
				double statHeight = 999999.0;
				int countryCode = 999999;
				if(info == null) {
					loggerBuffer.append("\n  In configuration file, this station does not exist: " + stat);
//					continue ;
				}
				else{
					String[] infos = info.split(",");
					if(infos[1].equals("null")){
						loggerBuffer.append("\n In configuration file, longtitude is null!");
//						continue ;
					}
					else
						longtitude = Double.parseDouble(infos[1]);
					if(infos[2].equals("null")){
						loggerBuffer.append("\n  In configuration file, latitude is null!");
//						continue;
					}
					else
						latitude = Double.parseDouble(infos[2]);
					if(!infos[3].equals("null"))
						statHeight = Double.parseDouble(infos[3]);
					if(!infos[4].equals("null"))
						countryCode = Integer.parseInt(infos[4]);
				}
				boolean isRevised = false; 
				String v_bbb = wafs.getCorrectSign();
				if(v_bbb.compareTo("000") > 0)
					isRevised = true;
				String primkey = sdf.format(wafs.getObservationTime())+"_"+wafs.getTerminalSign()+"_"+wafs.getReportCenter()+"_"+wafs.getReportType();
				
				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            Date date = wafs.getObservationTime();
	            Calendar calendar = Calendar.getInstance();
	            calendar.setTime(date);
	            calendar.set(Calendar.MINUTE, 0);
	            calendar.set(Calendar.SECOND, 0);
	            calendar.set(Calendar.MILLISECOND, 0);
	            row.put("D_DATETIME", TimeUtil.date2String(calendar.getTime(),"yyyy-MM-dd HH:mm:ss"));
        	    // "V_CCCC,V_OBCC,C_TYPE,C05021_001,C05021_002,C_RW1,C_RWVT1,C_RW2,C_RWVT2,C_RW3,"
	            row.put("V_CCCC", wafs.getReportCenter().replaceAll("\u0000", ""));
	            row.put("V_OBCC", wafs.getTerminalSign().replaceAll("\u0000", ""));
	            row.put("C_TYPE", wafs.getReportType().replaceAll("\u0000", ""));
	            row.put("C05021_001", wafs.getDirectionOfHorizontalVisibility().replaceAll("\u0000", "")); // 水平能见度方向 
	            row.put("C05021_002", wafs.getDirOfMaxHorizontalVisibility().replaceAll("\u0000", "")); // 最大水平能见度的方向
	            row.put("C_RW1", wafs.getRunwayNumbers().get(0).replaceAll("\u0000", "")); // 跑道编号1 
	            row.put("C_RWVT1", wafs.getTrendsOfRunwayViusalRange().get(0).toString().replaceAll("\u0000", "")); //跑道视程变化趋势1 
	            row.put("C_RW2", wafs.getRunwayNumbers().get(1).replaceAll("\u0000", "")); // 跑道编号2 
	            row.put("C_RWVT2", wafs.getTrendsOfRunwayViusalRange().get(1).toString().replaceAll("\u0000", "")); //跑道视程变化趋势2
	            row.put("C_RW3", wafs.getRunwayNumbers().get(2).replaceAll("\u0000", "")); // 跑道编号3 
				
	            // "C_RWVT3,C_RW4,C_RWVT4,C_RW5,C_RWVT5,C20003_001,C20003_002,C20003_003,C20011_001,C20011_002,"
	            row.put("C_RWVT3", wafs.getTrendsOfRunwayViusalRange().get(2).toString().replaceAll("\u0000", "")); //跑道视程变化趋势3
	            row.put("C_RW4", wafs.getRunwayNumbers().get(3).replaceAll("\u0000", "")); // 跑道编号4 
	            row.put("C_RWVT4", wafs.getTrendsOfRunwayViusalRange().get(3).toString().replaceAll("\u0000", "")); //跑道视程变化趋势4
	            row.put("C_RW5", wafs.getRunwayNumbers().get(4).replaceAll("\u0000", "")); // 跑道编号5 
	            row.put("C_RWVT5", wafs.getTrendsOfRunwayViusalRange().get(4).toString().replaceAll("\u0000", "")); //跑道视程变化趋势5
	            row.put("C20003_001", wafs.getWeatherPhenomenons().get(0).replaceAll("\u0000", ""));//天气现象1 ~ 3
	            row.put("C20003_002", wafs.getWeatherPhenomenons().get(1).replaceAll("\u0000", ""));
	            row.put("C20003_003", wafs.getWeatherPhenomenons().get(2).replaceAll("\u0000", ""));
	            row.put("C20011_001", wafs.getCloudShapeAndAmount().get(0).replaceAll("\u0000", ""));//云状及云量1 ~ 3
	            row.put("C20011_002", wafs.getCloudShapeAndAmount().get(1).replaceAll("\u0000", ""));
	            //"C20011_003,C20003_004,C20003_005,C20003_006,V07001,V05001,V06001,V10004,V11001,V11002,"
	            row.put("C20011_003", wafs.getCloudShapeAndAmount().get(2).replaceAll("\u0000", ""));
				row.put("C20003_004", wafs.getRecentWeatherPhenomenons().get(0).replaceAll("\u0000", ""));// 近时天气现象1 ~ 3
				row.put("C20003_005", wafs.getRecentWeatherPhenomenons().get(1).replaceAll("\u0000", ""));
				row.put("C20003_006", wafs.getRecentWeatherPhenomenons().get(2).replaceAll("\u0000", ""));
				row.put("V07001", statHeight);// 测站高度
				row.put("V05001", latitude); // 纬度
				row.put("V06001", longtitude); //经度
				row.put("V10004", 999998); // 本站气压
				row.put("V11001", wafs.getWindDirection()); // 风向 
				row.put("V11002", wafs.getWindSpeed()); // 风速
	            // "V11041,V11041_001,V11041_002,V20001_001,V20001_002,V20002,V_RWVMIN1,V_RWVMAX1,V_RWVMIN2,V_RWVMAX2,"
				row.put("V11041", wafs.getMaxWindSpeed()); // 最大风速
				row.put("V11041_001", wafs.getExtremeChangeValueOfWindDireInCounterClockwise());//风向反时针变化极值 
				row.put("V11041_002", wafs.getExtremeChangeValueOfWindDireInClockwise());//风向顺时针变化极值
				row.put("V20001_001", wafs.getHorizontalVisibility());// 水平能见度（或最小能见度）
				row.put("V20001_002", wafs.getMaxHorizontalVisibility());// 最大水平能见度
				row.put("V20002", wafs.getVerticalVisibility());// 垂直能见度
				row.put("V_RWVMIN1", wafs.getRunwayVisualRange().get(0));//跑道视程（或10分钟内最小平均跑道视程）1 ~ 5； 10分钟内最大平均跑道视程1  ~ 5
				row.put("V_RWVMAX1", wafs.getMaxRunwayVisualRangeEvery10mins().get(0));
				row.put("V_RWVMIN2", wafs.getRunwayVisualRange().get(1));
				row.put("V_RWVMAX2", wafs.getMaxRunwayVisualRangeEvery10mins().get(1));
	            // "V_RWVMIN3,V_RWVMAX3,V_RWVMIN4,V_RWVMAX4,V_RWVMIN5,V_RWVMAX5,V20013_001,V20013_002,V20013_003,V12001,"
				row.put("V_RWVMIN3", wafs.getRunwayVisualRange().get(2));
				row.put("V_RWVMAX3", wafs.getMaxRunwayVisualRangeEvery10mins().get(2));
				row.put("V_RWVMIN4", wafs.getRunwayVisualRange().get(3));
				row.put("V_RWVMAX4", wafs.getMaxRunwayVisualRangeEvery10mins().get(3));
				row.put("V_RWVMIN5", wafs.getRunwayVisualRange().get(4));
				row.put("V_RWVMAX5", wafs.getMaxRunwayVisualRangeEvery10mins().get(4));
				row.put("V20013_001", wafs.getHeightOfCloudBase().get(0));// 云底高1 ~ 3
				row.put("V20013_002", wafs.getHeightOfCloudBase().get(1));
				row.put("V20013_003", wafs.getHeightOfCloudBase().get(2));
				row.put("V12001", wafs.getTemperature()); // 温度
	            // "V12003,V10051,V_NCODE,V02001,V04001,V04002,V04003,V04004,V04005,V_BBB,"
				row.put("V12003", wafs.getDewPoint()); // 露点 
				row.put("V10051", wafs.getPressureAboveSeaLevel()); // 订正海平面气压
				row.put("V_NCODE", countryCode);  // 国家代码 
				row.put("V02001", wafs.getAutoStationMark()); // 自动站标识
				row.put("V04001", wafs.getObservationTime().getYear() + 1900);
				row.put("V04002", wafs.getObservationTime().getMonth() + 1);
				row.put("V04003", wafs.getObservationTime().getDate());
				row.put("V04004", wafs.getObservationTime().getHours());
				row.put("V04005", wafs.getObservationTime().getMinutes());
				row.put("V_BBB", wafs.getCorrectSign());
	            // "C_RWY1,C_RWY2,C_RWY3,C_RMK)
				row.put("C_RWY1", wafs.getWindShearRunwayNumbers().get(0).replaceAll("\u0000", ""));  // 风切变跑道号1 ~ 3
				row.put("C_RWY2", wafs.getWindShearRunwayNumbers().get(1).replaceAll("\u0000", ""));
				row.put("C_RWY3", wafs.getWindShearRunwayNumbers().get(2).replaceAll("\u0000", ""));
				row.put("C_RMK", wafs.getAnnotations().replaceAll("\u0000", ""));
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();	
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(V_TT);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(wafs.getTerminalSign());
				di.setDATA_TIME(TimeUtil.date2String(wafs.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				di.setLATITUDE(String.valueOf(latitude));
				di.setLONGTITUDE(String.valueOf(longtitude));
				
				try {
					if(isRevised) 
						OTSDbHelper.getInstance().update(tablename, row);
					else
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
					}
				}
				
	        }
			loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
		    loggerBuffer.append(" INSERT SUCCESS COUNT : " + successRowCount + "\n");
		    loggerBuffer.append(" INSERT FAILED COUNT : " + (wafss.size() - successRowCount) + "\n");
		}
		return DataBaseAction.SUCCESS;
	}
}
