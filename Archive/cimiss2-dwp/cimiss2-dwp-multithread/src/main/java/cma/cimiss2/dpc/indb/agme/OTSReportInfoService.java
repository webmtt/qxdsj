package cma.cimiss2.dpc.indb.agme;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.NumberUtil;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;

public class OTSReportInfoService {
	private static final double[] NINE_ARRAYS = {999, 9999, 99999 };
	
	@SuppressWarnings("deprecation")
	public static void reportInfoToDb(List<ReportInfo> reportInfos, String v_bbb, Date recv_time,String v_cccc, String v_tt, StringBuffer loggerBuffer, List<CTSCodeMap> codeMaps){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(reportInfos != null && reportInfos.size() > 0) {
			List<Map<String, Object>> batchs = new ArrayList<Map<String, Object>>();
	        for (int i = 0; i < reportInfos.size(); i++) {
	        	Map<String, Object> row = new HashMap<String, Object>();
	        	AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();
	        	String CropType = agmeReportHeader.getCropType().replaceAll("-", "");
				int length = CropType.length();
				int type = Integer.parseInt(CropType.substring(length -2));
				String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+CropType+"_"+v_tt+"_"+v_bbb;
				row.put("D_RECORD_ID", primkey);
				row.put("D_DATA_ID", codeMaps.get(type - 1).getReport_sod_code());
				row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_RYMDHM", TimeUtil.date2String(recv_time,"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));
		        row.put("D_DATETIME", TimeUtil.date2String(agmeReportHeader.getReport_time(),"yyyy-MM-dd HH:mm:ss"));  
				row.put("V_BBB", v_bbb);
				row.put("V_CCCC", v_cccc);
				row.put("V01301", agmeReportHeader.getStationNumberChina());
				row.put("V01300", Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
				double latitude = agmeReportHeader.getLatitude();
				double longitude =  agmeReportHeader.getLongitude();
				if(CropType.toUpperCase().startsWith("SOIL")){
					latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
					longitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
				}
				row.put("V05001", latitude);
				row.put("V06001", longitude);
				row.put("V_ACODE", 0);
				row.put("V04001", agmeReportHeader.getReport_time().getYear()+1900);
				row.put("V04002", agmeReportHeader.getReport_time().getMonth()+1);
				row.put("V04003", agmeReportHeader.getReport_time().getDate());
				row.put("V04004", agmeReportHeader.getReport_time().getHours());
				row.put("V04005", agmeReportHeader.getReport_time().getMinutes());
				row.put("V04006", agmeReportHeader.getReport_time().getSeconds());
				row.put("V_TT", v_tt);
				row.put("V_ELE_KIND", CropType.substring(0, length - 2));
				row.put("V01323", CropType);
				row.put("V_LEN", reportInfos.get(i).getReport().length());
				row.put("V_REPORT", reportInfos.get(i).getReport());

	            batchs.add(row);
	        }
	        OTSBatchResult result = OTSDbHelper.getInstance().insert(codeMaps.get(0).getReport_table_name(), batchs);
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
