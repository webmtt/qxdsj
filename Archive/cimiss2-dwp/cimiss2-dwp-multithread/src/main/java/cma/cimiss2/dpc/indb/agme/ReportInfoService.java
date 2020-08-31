package cma.cimiss2.dpc.indb.agme;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.utils.NumberUtil;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;


public class ReportInfoService {
	public static final Logger logger = LoggerFactory.getLogger(ReportInfoService.class);
	private static final double[] NINE_ARRAYS = {999, 9999, 99999 };
	public static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, String v_bbb, Date recv_time, String v_cccc, String v_tt, List<CTSCodeMap> codeMaps,String filepath,List<StatDi> listDi) {
		PreparedStatement pStatement = null;
		// chy  去掉 D_record_Id
		String sql = "INSERT INTO "+codeMaps.get(0).getReport_table_name()+" (D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,"
				+ "V_BBB,V_CCCC,V01301,V01300,V05001,V06001,V_ACODE,"
				+ "V04001,V04002,V04003,V04004,V04005,V04006,"
				+ "V_TT,V_ELE_KIND,V01323,V_LEN,V_REPORT,D_SOURCE_ID) VALUES"
				+ "(?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?,?,"
				+ "?,?,?,?)";
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			File file=new File(filepath);
			String fileN=file.getName();
			for (int i = 0; i < reportInfos.size(); i++) {
				
				try {
					AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(codeMaps.get(0).getReport_sod_code());
					di.setDATA_TYPE_1(codeMaps.get(0).getCts_code());
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					String CropType = agmeReportHeader.getCropType().replaceAll("-", "");
					int length = CropType.length();
					int type = Integer.parseInt(CropType.substring(length -2));
					String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+CropType+"_"+v_tt+"_"+v_bbb;
//					pStatement.setString(1, primkey);
					int ii = 1;
					pStatement.setString(ii++, codeMaps.get(type - 1).getReport_sod_code());
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(agmeReportHeader.getReport_time().getTime()));
					pStatement.setString(ii++, v_bbb);
					pStatement.setString(ii++, v_cccc);
					pStatement.setString(ii++, agmeReportHeader.getStationNumberChina());
					pStatement.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
					
					double latitude = agmeReportHeader.getLatitude();
					double longitude =  agmeReportHeader.getLongitude();
					if(CropType.toUpperCase().startsWith("SOIL") || CropType.toUpperCase().startsWith("PHENO")){
						latitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, latitude) ? latitude : ElementValUtil.getlatitude(String.valueOf(latitude))).doubleValue();
						longitude = NumberUtil.FormatNumOrNine(ArrayUtils.contains(NINE_ARRAYS, longitude) ? longitude : ElementValUtil.getLongitude(String.valueOf(longitude))).doubleValue();
					}
					pStatement.setDouble(ii++, latitude);
					pStatement.setDouble(ii++, longitude);
					pStatement.setInt(ii++, 0);
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getYear()+1900);
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getMonth()+1);
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getDate());
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getHours());
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getMinutes());
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getSeconds());
					pStatement.setString(ii++, v_tt);
					pStatement.setString(ii++, CropType.substring(0, length - 2));
					pStatement.setString(ii++, CropType);
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					pStatement.setString(ii++, codeMaps.get(0).getCts_code());
					
					di.setIIiii(agmeReportHeader.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agmeReportHeader.getReport_time(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(agmeReportHeader.getLongitude()));
					di.setLATITUDE(String.valueOf(agmeReportHeader.getLatitude()));

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(String.valueOf(agmeReportHeader.getHeightOfSationGroundAboveMeanSeaLevel()));
					try{
						pStatement.execute();
						connection.commit();
						listDi.add(di);
					}catch (Exception e) {
						di.setPROCESS_STATE("0");//1成功，0失败
						listDi.add(di);
					}
				} catch (Exception e) {
					logger.error("sql error:"+e.getMessage());
					continue;
				}
			}
		} catch (SQLException e) {
			logger.error("Database connection error: " + e.getMessage());
		}finally {
			if(pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					
				}
			}
			
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					
				}
			}
			
		}
		
	}

}
