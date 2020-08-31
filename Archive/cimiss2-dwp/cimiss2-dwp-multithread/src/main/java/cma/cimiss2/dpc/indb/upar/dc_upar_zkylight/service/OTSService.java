package cma.cimiss2.dpc.indb.upar.dc_upar_zkylight.service;

import java.sql.Timestamp;
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
import org.cimiss2.dwp.tools.utils.TimeUtil;

import com.alicloud.openservices.tablestore.ClientException;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.Upar_zkyLight;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class OTSService {
	public static BlockingQueue<StatDi> diQueues;
	public static String sod_code = StartConfig.sodCode();
	public static String cts_code = StartConfig.ctsCode();
	

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}


	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}


	@SuppressWarnings("deprecation")
	public static DataBaseAction insert_ots(ParseResult<Upar_zkyLight> parseResult, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		List<Upar_zkyLight> list = parseResult.getData();

		if (list != null && list.size() > 0) {
			int successRowCount = list.size();
			for (int i = 0; i < list.size(); i++) {
				Upar_zkyLight upar_zkyLight = list.get(i);
				Map<String, Object> row = new HashMap<String, Object>();
				String lat = String.valueOf((int) (upar_zkyLight.getLatitude() * 1e6));
				String lon = String.valueOf((int) (upar_zkyLight.getLongtitude() * 1e6));
				lat = lat.replaceAll("-", "0");
				lon = lon.replaceAll("-", "0");
				Date date = new Date();
				date = upar_zkyLight.getObservationTime();

				row.put("D_RECORD_ID", sdf.format(date) + "_" + upar_zkyLight.getMillisecond() + "_" + lat + "_" + lon);
				row.put("D_DATA_ID", sod_code);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
				row.put("D_RYMDHM", TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));

				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
//					calendar.set(Calendar.MILLISECOND, 0);
//					calendar.set(Calendar.SECOND, 0);
				Timestamp timestamp = new Timestamp(calendar.getTime().getTime());
				row.put("D_DATETIME", TimeUtil.date2String(timestamp, "yyyy-MM-dd HH:mm:ss")); // dateTime

				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // updateTime

				row.put("V05001", upar_zkyLight.getLatitude()); // 闪电位置纬度
				row.put("V06001", upar_zkyLight.getLongtitude());// 闪电位置经度
				row.put("V07001", upar_zkyLight.getHeight());// 闪电位置高度
				row.put("V04001", date.getYear() + 1900);// 资料观测年
				row.put("V04002", date.getMonth() + 1);// 资料观测月
				row.put("V04003", date.getDate());// 资料观测日
				row.put("V04004", date.getHours());// 资料观测时
				row.put("V04005", date.getMinutes());// 资料观测分
				row.put("V04006", date.getSeconds());// 资料观测秒
				row.put("V04007", upar_zkyLight.getMillisecond());// 资料观测毫秒
				row.put("V73016", upar_zkyLight.getCurrentIntensity());// 电流强度
				row.put("V73023", upar_zkyLight.getMaxSlope());// 回击最大陡度
				row.put("V73011", upar_zkyLight.getError());// 定位误差
				row.put("V73110", upar_zkyLight.getLocationType());// 定位方式
				row.put("V01015_1", upar_zkyLight.getProv());// 雷电地理位置信息省
				row.put("V01015_2", upar_zkyLight.getDistrict());// 雷电地理位置信息市
				row.put("V01015_3", upar_zkyLight.getCountry());// 雷电地理位置信息县
				row.put("V_PROCESSFLAG", upar_zkyLight.getProcessFlag());// 标志位
				row.put("V_USEDIDS", upar_zkyLight.getUsedIDs());// 定位仪编号
				row.put("V_CG_IC", upar_zkyLight.getCG_IC());// 云/地闪
				row.put("V_BBB", "000");

				StatDi di = new StatDi();
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(sod_code);
				di.setDATA_TYPE_1(cts_code);

				di.setTT("闪电定位");
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); // 0成功，1失败
				di.setPROCESS_STATE("0"); // 0成功，1失败
				di.setLATITUDE(upar_zkyLight.getLatitude().toString());
				di.setLONGTITUDE(upar_zkyLight.getLongtitude().toString());
				di.setDATA_TIME(TimeUtil.date2String(upar_zkyLight.getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());

				try {

					OTSDbHelper.getInstance().insert(tablename, row);
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

}
