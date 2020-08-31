package cma.cimiss2.dpc.indb.sand;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
/**
 * 
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  ReportInfoService.java   
 * @Package org.cimiss2.dwp.z_sand   
 * @Description:    TODO(沙尘暴监测资料入报文库流程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月6日 下午4:55:57   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 *---------------------------------------------------------------------------------
 */

public class ReportInfoService {
	public static final Logger logger = LoggerFactory.getLogger(ReportInfoService.class);
	@SuppressWarnings({ "deprecation" })
	public static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, String v_bbb, Date recv_time, String v_cccc, String v_tt,String sod_code,String cts_code,String acode, StringBuffer loggerBuffer,String filepath,List<StatDi> listDi ) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.reportTable()+" "
				+ " (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V_CCCC,V_TT,V01301, "
				+ " V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT,D_SOURCE_ID) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {	
				try {
					File file=new File(filepath);
					String fileN=file.getName();
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败
					
					AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();		
					String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+v_cccc;
					pStatement.setString(1, primkey);
					pStatement.setString(2, sod_code);
					pStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(4, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(6, new Timestamp(agmeReportHeader.getReport_time().getTime()));
					pStatement.setString(7, v_bbb);
					pStatement.setString(8, v_cccc);
					pStatement.setString(9, v_tt);
					pStatement.setString(10, agmeReportHeader.getStationNumberChina());
					pStatement.setInt(11 ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
					pStatement.setDouble(12, agmeReportHeader.getLatitude());
					pStatement.setDouble(13, agmeReportHeader.getLongitude());
					pStatement.setInt(14, 2250);//V_NCODE
					pStatement.setBigDecimal(15, new BigDecimal(StationInfo.getAdminCode(agmeReportHeader.getStationNumberChina(), acode)));// V_ACODE
					pStatement.setInt(16, agmeReportHeader.getReport_time().getYear()+1900);
					pStatement.setInt(17, agmeReportHeader.getReport_time().getMonth()+1);
					pStatement.setInt(18, agmeReportHeader.getReport_time().getDate());
					pStatement.setInt(19, agmeReportHeader.getReport_time().getHours());
					pStatement.setInt(20, agmeReportHeader.getReport_time().getMinutes());
					pStatement.setInt(21, reportInfos.get(i).getReport().length());
					pStatement.setString(22, reportInfos.get(i).getReport());
					pStatement.setString(23, cts_code);
					
					di.setIIiii(agmeReportHeader.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(agmeReportHeader.getReport_time(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(agmeReportHeader.getLatitude()));
					di.setLATITUDE(String.valueOf(agmeReportHeader.getLongitude()));

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(String.valueOf(agmeReportHeader.getHeightOfSationGroundAboveMeanSeaLevel()));
					
					try{
						pStatement.execute();
						connection.commit();
						listDi.add(di);
					}catch (SQLException e) {
						di.setPROCESS_STATE("0");//1成功，0失败
						listDi.add(di);
					}
				} catch (Exception e) {
					logger.error("sql error:"+e.getMessage());
					continue;
				}
			}
		} catch (SQLException e) {
			logger.error("Database connection error:" + e.getMessage());
			return;
		}
		finally {
			if(pStatement != null){
				try {
					pStatement.close();
				} catch (SQLException e) {
					logger.error("close statement: " + e.getMessage());
				}
			}
		}
	}

}
