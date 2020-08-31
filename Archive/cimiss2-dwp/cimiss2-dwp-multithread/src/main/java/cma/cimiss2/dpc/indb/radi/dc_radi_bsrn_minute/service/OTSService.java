package cma.cimiss2.dpc.indb.radi.dc_radi_bsrn_minute.service;

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
import cma.cimiss2.dpc.decoder.bean.radi.MinutesReferenceRadiationData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {

	public static BlockingQueue<StatDi> diQueues;
	public Map<String, Object> proMap = StationInfo.getProMap();
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	static String v_tt = "RAMN";

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<MinutesReferenceRadiationData> parseResult, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		List<MinutesReferenceRadiationData> list = parseResult.getData();

		if (list != null && list.size() > 0) {
			int successRowCount = list.size();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				MinutesReferenceRadiationData bsrnMinute = list.get(i);

				String PrimaryKey = sdf.format(bsrnMinute.getObservationTime()) + "_"
						+ bsrnMinute.getStationNumberChina();
				row.put("D_RECORD_ID", PrimaryKey);// 记录标识
				row.put("D_DATA_ID", sod_code);// 资料标识
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss")); // 收到时间
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间
				row.put("D_DATETIME", TimeUtil.date2String(bsrnMinute.getObservationTime(), "yyyy-MM-dd HH:mm:ss")); // 资料时间
				row.put("V01301", bsrnMinute.getStationNumberChina());// 区站号(字符)
				row.put("V01300", StationCodeUtil.stringToAscii(bsrnMinute.getStationNumberChina()));// 区站号(数字)
				row.put("V05001", bsrnMinute.getLatitude());// 纬度
				row.put("V06001", bsrnMinute.getLongitude());// 经度
				row.put("V07001", bsrnMinute.getHeightOfSationGroundAboveMeanSeaLevel());// 测站高度
				row.put("V07032_1", bsrnMinute.getDRA_Sensor_Heigh());// 太阳直接辐射辐射表距地高度
				row.put("V07032_2", bsrnMinute.getSRA_Sensor_Heigh());// 散射辐射辐射表距地高度
				row.put("V07032_3", bsrnMinute.getQRA_Sensor_Heigh());// 总辐射辐射表距地高度
				row.put("V07032_4", bsrnMinute.getRRA_Sensor_Heigh());// 反射辐射辐射表距地高度
				row.put("V07032_5", bsrnMinute.getLR_Atmo_Sensor_Heigh());// 大气长波辐射辐射表距地高度
				row.put("V07032_6", bsrnMinute.getLR_Earth_Sensor_Heigh());// 地球长波辐射辐射表距地高度
				row.put("V07032_7", bsrnMinute.getUV_Sensor_Heigh());// 紫外辐射辐射表距地高度
				row.put("V07032_8", bsrnMinute.getPAR_Sensor_Heigh());// 光合有效辐射辐射表距地高度
				row.put("V04001", bsrnMinute.getObservationTime().getYear() + 1900);// 资料观测年
				row.put("V04002", bsrnMinute.getObservationTime().getMonth() + 1);// 资料观测月
				row.put("V04003", bsrnMinute.getObservationTime().getDate());// 资料观测日
				row.put("V04004", bsrnMinute.getObservationTime().getHours());// 资料观测时
				row.put("V04005", bsrnMinute.getObservationTime().getMinutes());// 资料观测分
				row.put("V14313_701_01", bsrnMinute.getDRA_Avg_Mi());// 太阳直接辐射辐照度1分钟平均值
				row.put("V14313_06_01", bsrnMinute.getDRA_Min_Mi());// 太阳直接辐射辐照度1分钟最小值
				row.put("V14313_05_01", bsrnMinute.getDRA_Max_Mi());// 太阳直接辐射辐照度1分钟最大值
				row.put("V14313_04_01", bsrnMinute.getDRA_Mi_SD());// 太阳直接辐射辐照度1分钟标准差
				row.put("V14314_701_01", bsrnMinute.getSRA_Avg_Mi());// 散射辐射辐照度1分钟平均值
				row.put("V14314_06_01", bsrnMinute.getSRA_Min_Mi());// 散射辐射辐照度1分钟最小值
				row.put("V14314_05_01", bsrnMinute.getSRA_Max_Mi());// 散射辐射辐照度1分钟最大值
				row.put("V14314_04_01", bsrnMinute.getSRA_Mi_SD());// 散射辐射辐照度1分钟标准差
				row.put("V14402", bsrnMinute.getWIND_S_Avg_SRA_Mi());// 散射辐射辐射表1分钟平均通风速度
				row.put("V14412", bsrnMinute.getTEM_Avg_SRA_Mi());// 散射辐射辐射表1分钟平均辐射表体温度
				row.put("V14311_701_01", bsrnMinute.getQRA_Avg_Mi());// 总辐射辐照度1分钟平均值
				row.put("V14311_06_01", bsrnMinute.getQRA_Min_Mi());// 总辐射辐照度1分钟最小值
				row.put("V14311_05_01", bsrnMinute.getQRA_Max_Min());// 总辐射辐照度1分钟最大值
				row.put("V14311_04_01", bsrnMinute.getQRA_Mi_SD());// 总辐射辐照度1分钟标准差
				row.put("V14401", bsrnMinute.getWIND_S_Avg_QRA_Mi());// 总辐射辐射表1分钟平均通风速度
				row.put("V14411", bsrnMinute.getTEM_Avg_QRA_Mi());// 总辐射辐射表1分钟平均辐射表体温度
				row.put("V14315_701_01", bsrnMinute.getRRA_Avg_Mi());// 反射辐射辐照度1分钟平均值
				row.put("V14315_06_01", bsrnMinute.getRRA_Min_Mi());// 反射辐射辐照度1分钟最小值
				row.put("V14315_05_01", bsrnMinute.getRRA_Max_Mi());// 反射辐射辐照度1分钟最大值
				row.put("V14315_04_01", bsrnMinute.getRRA_Mi_SD());// 反射辐射辐照度1分钟标准差
				row.put("V14403", bsrnMinute.getWIND_S_Avg_RRA_Mi());// 反射辐射辐射表1分钟平均通风速度
				row.put("V14413", bsrnMinute.getTEM_Avg_RRA_Mi());// 反射辐射辐射表1分钟平均辐射表体温度
				row.put("V14318_701_01", bsrnMinute.getALR_Avg_Mi());// 大气长波辐射辐照度1分钟平均值
				row.put("V14318_06_01", bsrnMinute.getALR_Min_Mi());// 大气长波辐射辐照度1分钟最小值
				row.put("V14318_05_01", bsrnMinute.getALR_Max_Mi());// 大气长波辐射辐照度1分钟最大值
				row.put("V14318_04_01", bsrnMinute.getALR_Mi_SD());// 大气长波辐射辐照度1分钟标准差
				row.put("V14404", bsrnMinute.getWIND_S_Avg_ALR_Mi());// 大气长波辐射辐射表1分钟平均通风速度
				row.put("V14414", bsrnMinute.getTEM_Avg_ALR_Mi());// 大气长波辐射辐射表1分钟平均腔体温度
				row.put("V14319_701_01", bsrnMinute.getELR_Avg_Mi());// 地球长波辐射辐照度1分钟平均值
				row.put("V14319_06_01", bsrnMinute.getELR_Min_Mi());// 地球长波辐射辐照度1分钟最小值
				row.put("V14319_05_01", bsrnMinute.getELR_Max_Mi());// 地球长波辐射辐照度1分钟最大值
				row.put("V14319_04_01", bsrnMinute.getELR_Mi_SD());// 地球长波辐射辐照度1分钟标准差
				row.put("V14405", bsrnMinute.getWIND_S_Avg_ELR_Mi());// 地球长波辐射辐射表1分钟平均通风速度
				row.put("V14415", bsrnMinute.getTEM_Avg_ELR_Mi());// 地球长波辐射辐射表1分钟平均腔体温度
				row.put("V14316_701_01", bsrnMinute.getUVA_Avg_Mi());// 紫外辐射（UVA）辐照度1分钟平均值
				row.put("V14316_06_01", bsrnMinute.getUVA_Min_Mi());// 紫外辐射（UVA）辐照度1分钟最小值
				row.put("V14316_05_01", bsrnMinute.getUV_Max_Mi());// 紫外辐射（UVA）辐照度1分钟最大值
				row.put("V14316_04_01", bsrnMinute.getUVA_Mi_SD());// 紫外辐射（UVA）辐照度1分钟标准差
				row.put("V14317_701_01", bsrnMinute.getUVB_Avg_Mi());// 紫外辐射（UVB）辐照度1分钟平均值
				row.put("V14317_06_01", bsrnMinute.getUVB_Min_Mi());// 紫外辐射（UVB）辐照度1分钟最小值
				row.put("V14317_05_01", bsrnMinute.getUVB_Max_Mi());// 紫外辐射（UVB）辐照度1分钟最大值
				row.put("V14317_04_01", bsrnMinute.getUVB_Mi_SD());// 紫外辐射（UVB）辐照度1分钟标准差
				row.put("V14416", bsrnMinute.getTEM_Avg_UV_Mi());// 紫外辐射辐射表恒温器1分钟平均温度
				row.put("V14340_701_01", bsrnMinute.getPAR_Avg_Mi());// 光合有效辐射辐照度1分钟平均值
				row.put("V14340_06_01", bsrnMinute.getPAR_Min_Mi());// 光合有效辐射辐照度1分钟最小值
				row.put("V14340_05_01", bsrnMinute.getPAR_Max_Mi());// 光合有效辐射辐照度1分钟最大值
				row.put("V14340_04_01", bsrnMinute.getPAR_Mi_SD());// 光合有效辐射辐照度1分钟标准差
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
				di.setBUSINESS_STATE("0"); // 0成功，1失败
				di.setPROCESS_STATE("0"); // 0成功，1失败
				di.setIIiii(bsrnMinute.getStationNumberChina());
				di.setLATITUDE(String.valueOf(bsrnMinute.getLatitude()));
				di.setLONGTITUDE(String.valueOf(bsrnMinute.getLongitude()));
				di.setDATA_TIME(TimeUtil.date2String(bsrnMinute.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());

				try {
					OTSDbHelper.getInstance().insert(StartConfig.valueTable(), row);
					diQueues.offer(di);
				} catch (Exception e) {
					loggerBuffer.append(e.getMessage());
					successRowCount = successRowCount - 1;
					di.setPROCESS_STATE("1");
					diQueues.offer(di);
					loggerBuffer.append(row);
					if (e.getClass() == ClientException.class) {
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
		if (reportInfos != null && reportInfos.size() > 0) {
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date oTime = new Date();
			for (int i = 0; i < reportInfos.size(); i++) {
				Map<String, Object> row = new HashMap<String, Object>();
				AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();
				oTime = agmeReportHeader.getReport_time();
				String primkey = sdf.format(agmeReportHeader.getReport_time()) + "_"
						+ agmeReportHeader.getStationNumberChina() + "_" + "9999";
				row.put("D_RECORD_ID", primkey);
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_DATETIME", TimeUtil.date2String(oTime, "yyyy-MM-dd HH:mm:ss"));
				// "V_BBB,V_CCCC,V_TT,V01301,V01300,V05001,V06001,V_NCODE,V_ACODE,"
				row.put("V_BBB", "000");
				row.put("V_CCCC", "9999");
				row.put("V_TT", v_tt);
				row.put("V01301", agmeReportHeader.getStationNumberChina());
				row.put("V01300",
						Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
				row.put("V05001", agmeReportHeader.getLatitude());
				row.put("V06001", agmeReportHeader.getLongitude());
				row.put("V_NCODE", 2250);// V_NCODE
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
