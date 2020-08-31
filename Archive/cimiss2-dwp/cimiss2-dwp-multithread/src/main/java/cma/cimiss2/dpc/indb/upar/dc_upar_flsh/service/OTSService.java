package cma.cimiss2.dpc.indb.upar.dc_upar_flsh.service;

import java.text.SimpleDateFormat;
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
import cma.cimiss2.dpc.decoder.bean.upar.LightingData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

// TODO: Auto-generated Javadoc
/**
 * The Class OTSService.
 */
public class OTSService {
	
	/** The cts code. */
	static String cts_code = StartConfig.ctsCode();
	
	/** The type. */
	static String type = StartConfig.sodCode();
	
	/** The di queues. */
	public static BlockingQueue<StatDi> diQueues;
	
	/**
	 * Gets the di queues.
	 *
	 * @return the di queues
	 */
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	
	/**
	 * Sets the di queues.
	 *
	 * @param diQueues the new di queues
	 */
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
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
	public static DataBaseAction insert_ots(ParseResult<LightingData> parseResult, String tablename, Date recv_time,
			StringBuffer loggerBuffer, String fileN) {
		List<LightingData> list = parseResult.getData();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(list != null && list.size() > 0) {
			int successRowCount = list.size();
	        for (int i = 0; i < list.size(); i++) {
	            Map<String, Object> row = new HashMap<String, Object>();
	            int strYear = list.get(i).getObservationTime().getYear() + 1900;
				int strMonth = list.get(i).getObservationTime().getMonth() + 1;
				int strDate = list.get(i).getObservationTime().getDate();
				String primkey = sdf.format(list.get(i).getObservationTime()) + list.get(i).getMillis() + "_";
				if(list.get(i).getLatitude() > 0) {
					primkey = primkey + "0"+(int)(list.get(i).getLatitude()*1000000) + "_";
				}else {
					primkey = primkey + "1"+(int)(list.get(i).getLatitude()*1000000) + "_";
				}
				
				if(list.get(i).getLongitude() > 0) {
					primkey = primkey + "0"+(int)(list.get(i).getLongitude()*1000000) + "_";
				}else {
					primkey = primkey + "1"+(int)(list.get(i).getLongitude()*1000000) + "_";
				}
				
				primkey = primkey + list.get(i).getLocateMode();
				row.put("D_RECORD_ID", primkey);
				row.put("D_DATA_ID", type);
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
				row.put("D_RYMDHM",  TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
				row.put("D_DATETIME", TimeUtil.date2String(list.get(i).getObservationTime(),"yyyy-MM-dd HH:mm:ss"));// 一会改ee

				/*
				 * 闪电位置纬度 闪电位置经度
				 */
				row.put("V05001", list.get(i).getLatitude());
				row.put("V06001",list.get(i).getLongitude());
				/*
				 * 资料观测年 资料观测月 资料观测日 资料观测时 资料观测分 资料观测秒 资料观测毫秒
				 */

				row.put("V04001", strYear);
				row.put("V04002", strMonth);
				row.put("V04003", strDate);
				row.put("V04004", list.get(i).getObservationTime().getHours());
				row.put("V04005", list.get(i).getObservationTime().getMinutes());
				row.put("V04006", list.get(i).getObservationTime().getSeconds());
//				row.put("V04007", NumberUtil.addZeroForNum(list.get(i).getMillis(), 7));
				row.put("V04007", list.get(i).getMillis());
				/*
				 * 雷电序号 回击峰值强度 回击最大陡度 定位误差 定位方式
				 */
				row.put("V08300", list.get(i).getLdpId());
				row.put("V73016", list.get(i).getIntensity());
				row.put("V73023", list.get(i).getSteepness());
				row.put("V73011", list.get(i).getErrorValue());
				row.put("V73110", list.get(i).getLocateMode());

				/*
				 * 雷电地理位置信息省 雷电地理位置信息市 雷电地理位置信息县
				 */

				row.put("V01015_1", list.get(i).getProvince());
				row.put("V01015_2", list.get(i).getCity());
				row.put("V01015_3", list.get(i).getCounty());
				row.put("V_BBB", "000");
				row.put("D_SOURCE_ID", cts_code);
				
				StatDi di = new StatDi();					
				di.setFILE_NAME_O(fileN);
				di.setDATA_TYPE(type);
				di.setDATA_TYPE_1(cts_code);
				di.setTT("ADTD闪电定位");			
				di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm:ss"));
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileN);
				di.setBUSINESS_STATE("0"); //0成功，1失败
				di.setPROCESS_STATE("0");  //0成功，1失败	
				di.setLATITUDE(list.get(i).getLatitude().toString());
				di.setLONGTITUDE(list.get(i).getLongitude().toString());
				di.setDATA_TIME(TimeUtil.date2String(list.get(i).getObservationTime(), "yyyy-MM-dd HH:mm:ss"));
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
	

}
