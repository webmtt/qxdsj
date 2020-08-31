package cma.cimiss2.dpc.indb.radi.dc_radi_bsrn_hour.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.radi.PositiveReferenceRadiationData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {

	public static BlockingQueue<StatDi> diQueues;
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	static String v_tt="RAHR";

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<PositiveReferenceRadiationData> parseResult, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {	
		List<PositiveReferenceRadiationData> list = parseResult.getData();		
		int successRowCount = 0;
		if(list != null)
			successRowCount = list.size();
		if(list != null && list.size() > 0) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");	
			for(int i = 0; i <list.size(); i ++){
				Map<String, Object> row = new HashMap<String, Object>();
				PositiveReferenceRadiationData bsrnHour = list.get(i);
				
				String PrimaryKey = sdf.format(bsrnHour.getObservationTime())+"_"+bsrnHour.getStationNumberChina();		
				row.put("D_RECORD_ID", PrimaryKey);// 记录标识
				row.put("D_DATA_ID", sod_code);// 资料标识
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间
				row.put("D_DATETIME", TimeUtil.date2String(bsrnHour.getObservationTime(), "yyyy-MM-dd HH:mm:ss")); // 资料时间
				row.put("V01301", bsrnHour.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(bsrnHour.getStationNumberChina()));// 区站号(数字)
				row.put("V05001", bsrnHour.getLatitude());// 纬度
				row.put("V06001", bsrnHour.getLongitude());// 经度
				row.put("V07001", bsrnHour.getHeightOfSationGroundAboveMeanSeaLevel());// 测站高度
				row.put("V07032_1", bsrnHour.getDRA_Sensor_Heigh());// 太阳直接辐射辐射表距地高度
				row.put("V07032_2", bsrnHour.getSRA_Sensor_Heigh());//散射辐射辐射表距地高度
				row.put("V07032_3", bsrnHour.getQRA_Sensor_Heigh());// 总辐射辐射表距地高度
				row.put("V07032_4", bsrnHour.getRRA_Sensor_Heigh());// 反射辐射辐射表距地高度
				row.put("V07032_5", bsrnHour.getLR_Atmo_Sensor_Heigh());// 大气长波辐射辐射表距地高度
				row.put("V07032_6", bsrnHour.getLR_Earth_Sensor_Heigh());// 地球长波辐射辐射表距地高度
				row.put("V07032_7", bsrnHour.getUV_Sensor_Heigh());// 紫外辐射辐射表距地高度
				row.put("V07032_8", bsrnHour.getPAR_Sensor_Heigh());// 光合有效辐射辐射表距地高度
				row.put("V04001", bsrnHour.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", bsrnHour.getObservationTime().getMonth()+1);// 资料观测月
				row.put("V04003", bsrnHour.getObservationTime().getDate());// 资料观测日
				row.put("V04004", bsrnHour.getObservationTime().getHours());// 资料观测时
				row.put("V14313_701_60", bsrnHour.getDRA_Avg_Hour());//太阳直接辐射辐照度小时平均值
				row.put("V14322", bsrnHour.getDirectRadiationExposure());//直接辐射(曝辐量)
				row.put("V14313_05_60", bsrnHour.getDRA_Max_Hour());//太阳直接辐射辐照度小时内最大值
				row.put("V14313_05_052", bsrnHour.getDRA_Max_OTime());//直接辐射辐照度最大出现时间
				row.put("V14314_701_60", bsrnHour.getSRA_Avg_Hour());//散射辐射辐照度小时平均值
				row.put("V14309", bsrnHour.getScatteredRadiationExposure());//散射辐射(曝辐量)
				row.put("V14314_05_60", bsrnHour.getSRA_Max_Hour());//散射辐射辐照度小时内最大值
				row.put("V14314_05_052", bsrnHour.getSRA_Max_OTime());//散射辐射辐照度最大出现时间
				row.put("V14311_701_60", bsrnHour.getQRA_Avg_Hour());//总辐射辐照度小时平均值
				row.put("V14320", bsrnHour.getTotalRadiationExposure());//总辐射(曝辐量)
				row.put("V14311_05_60", bsrnHour.getQRA_Max_Hour());//总辐射辐照度小时内最大值
				row.put("V14311_05_052", bsrnHour.getQRA_Max_Hour_OTime());//总辐射辐照度小时内最大值出现时间
				row.put("V14315_701_60", bsrnHour.getRRA_Avg_Hour());//反射辐射辐照度小时平均值
				row.put("V14305", bsrnHour.getRRA_Hour());//反射辐射小时曝辐量
				row.put("V14315_05_60", bsrnHour.getRRA_Max_Hour());//反射辐射辐照度小时内最大值
				row.put("V14315_05_052", bsrnHour.getRRA_Max_OTime());//反射辐射辐照度最大出现时间
				row.put("V14318_701_60", bsrnHour.getALR_Avg_Hour());//大气长波辐射辐照度小时平均值
				row.put("V14323", bsrnHour.getALR_Hour());//大气长波辐射小时曝辐量
				row.put("V14318_06_60", bsrnHour.getALR_Min_Hour());//大气长波辐射辐照度小时内最小值
				row.put("V14318_06_052", bsrnHour.getALR_Min_Mi_OTIime());//大气长波辐射辐照度小时内最小值出现时间
				row.put("V14318_05_60", bsrnHour.getALR_Max_Hour());//大气长波辐射辐照度小时内最大值
				row.put("V14318_05_052", bsrnHour.getALR_Max_Mi_OTime());//大气长波辐射辐照度小时内最大值出现时间
				row.put("V14319_701_60", bsrnHour.getELR_Avg_Hour());//地球长波辐射辐照度小时平均值
				row.put("V14324", bsrnHour.getELR_Hour());//地球长波辐射小时曝辐量
				row.put("V14319_06_60", bsrnHour.getELR_Min_Hour());//地球长波辐射辐照度60分钟最小值
				row.put("V14319_06_052", bsrnHour.getELR_Min_Mi_Otime());//地球长波辐射辐照度小时内最小值出现时间
				row.put("V14319_05_60", bsrnHour.getELR_Max_Hour());//地球长波辐射辐照度小时最大值
				row.put("V14319_05_052", bsrnHour.getELR_Max_Mi_OTime());//地球长波辐射辐照度小时内最大值出现时间
				row.put("V14316_701_60", bsrnHour.getUVA_Avg_Hour());//紫外辐射（UVA）辐照度小时平均值
				row.put("V14307", bsrnHour.getUltravioletRadiationExposure());//紫外辐射(曝辐量)
				row.put("V14316_05_01", bsrnHour.getUV_Max_Mi());//紫外辐射（UVA）辐照度1分钟最大值
				row.put("V14316_05_052", bsrnHour.getUV_Max_OTime());//紫外辐射辐照度最大出现时间
				row.put("V14317_701_60", bsrnHour.getUVB_Avg_Hour());//紫外辐射（UVB）辐照度小时平均值
				row.put("V14310", bsrnHour.getUV_Hour());//紫外辐射（UVB）小时曝辐量
				row.put("V14317_05_60", bsrnHour.getUVB_Max_Hour());//紫外辐射（UVB）辐照度小时内最大值
				row.put("V14317_05_052", bsrnHour.getUVB_Max_Hour_OTime());//紫外辐射（UVB）辐照度小时内最大值出现时间
				row.put("V14340_701_60", bsrnHour.getPAR_Avg_Hour());//光合有效辐射辐照度小时平均值
				row.put("V14340_05_60", bsrnHour.getPAR_Max_Hour());//光合有效辐射辐照度小时内最大值
				row.put("V14340_05_052", bsrnHour.getPAR_Max_Mi_OTime());//光合有效辐射小时内最大值出现时间
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();	
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);
				di.setTT(v_tt);			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setIIiii(bsrnHour.getStationNumberChina());
				di.setLATITUDE(String.valueOf(bsrnHour.getLatitude()));
				di.setLONGTITUDE(String.valueOf(bsrnHour.getLongitude()));
				di.setDATA_TIME(TimeUtil.date2String(bsrnHour.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());	
				
				try {
					OTSDbHelper.getInstance().insert(StartConfig.valueTable(), row);
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
		    loggerBuffer.append(" INSERT FAILED COUNT : " + (list.size() - successRowCount) + "\n");
		}
		
		
		return DataBaseAction.SUCCESS;
	}

	@SuppressWarnings("deprecation")
	public static void reportInfoToDb(List<ReportInfo> reportInfos, Date recv_time, StringBuffer loggerBuffer) {
		if(reportInfos != null && reportInfos.size() > 0){
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date oTime = new Date();
			for(int i = 0; i < reportInfos.size(); i++){
				Map<String, Object> row = new HashMap<String, Object>();
				AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();		
				oTime = agmeReportHeader.getReport_time();
				String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+"9999";
				row.put("D_RECORD_ID", primkey);
	            row.put("D_DATA_ID", sod_code);
	            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
	            row.put("D_DATETIME", TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm:ss"));
	           //"V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
	            row.put("V_BBB", "000");
	            row.put("V_CCCC", "9999");
	            row.put("V_TT", v_tt);
	            row.put("V01301", agmeReportHeader.getStationNumberChina());
	            row.put("V01300" ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
	            row.put("V05001", agmeReportHeader.getLatitude());
	            row.put("V06001", agmeReportHeader.getLongitude());
	            row.put("V_NCODE", 2250);//V_NCODE
	            row.put("V_ACODE", StationInfo.getAdminCode(agmeReportHeader.getStationNumberChina(), "11"));// V_ACODE
	            // "V04001,V04002,V04003,V04004,V04005,"
	            row.put("V04001", oTime.getYear() + 1900);
	            row.put("V04002", oTime.getMonth() + 1);
	            row.put("V04003", oTime.getDate());
	            row.put("V04004", oTime.getHours());
	            row.put("V04005", oTime.getMinutes());
				// "V_LEN,V_REPORT)
	            row.put("V_LEN", reportInfos.get(i).getReport().length());
	            row.put("V_REPORT", reportInfos.get(i).getReport());
	            
	            batchs.add(row);
			}
			OTSBatchResult result = OTSDbHelper.getInstance().insert(StartConfig.reportTable(), batchs);
	        System.out.println(result.getSuccessRowCount());
	        System.out.println(result.getFailedRowCount());
	        System.out.println(result.getFailedRows());
	        loggerBuffer.append(" INSERT SUCCESS FINISH TIME : " + sdf.format(new Date()) + "\n");
	        loggerBuffer.append(" INSERT SUCCESS COUNT : " + result.getSuccessRowCount() + "\n");
	        loggerBuffer.append(" INSERT FAILED COUNT : " + result.getFailedRowCount() + "\n");
	        loggerBuffer.append(" INSERT FAILED CONTENT : " + result.getFailedRows() + "\n");
		}
		
	}

}
