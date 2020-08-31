package cma.cimiss2.dpc.indb.radi;

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
 * <br>
 * @Title:  ReportInfoService.java
 * @Package org.cimiss2.dwp.z_radi
 * @Description:(辐射资料报文入库)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月22日 下午5:33:13   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */

public class ReportInfoService {
	public static final Logger logger = LoggerFactory.getLogger(ReportInfoService.class);
	@SuppressWarnings({ "resource", "deprecation" })
	public static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, String v_bbb, Date recv_time, String v_cccc, String v_tt,String report_sod_code,String cts_code,String filepath,List<StatDi> listDi,String acode ) {
		File file=new File(filepath);
		String fileN=file.getName();
		PreparedStatement pStatement = null;
		// chy 去掉 D_RECORD_ID
		String sql = "INSERT INTO " + StartConfig.reportTable()
				+ " (D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V_CCCC,V_TT,V01301, "
				+ " V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT,D_SOURCE_ID) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {	
				try {
					AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();
					
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(report_sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+v_cccc+"_"+v_bbb;
					int ii = 1;
//					pStatement.setString(1, primkey);
					pStatement.setString(ii++, report_sod_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(agmeReportHeader.getReport_time().getTime()));
					pStatement.setString(ii++, v_bbb);
					pStatement.setString(ii++, v_cccc);
					pStatement.setString(ii++, v_tt);
					pStatement.setString(ii++, agmeReportHeader.getStationNumberChina());
					pStatement.setInt(ii++ ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
					pStatement.setDouble(ii++, agmeReportHeader.getLatitude());
					pStatement.setDouble(ii++, agmeReportHeader.getLongitude());
					pStatement.setInt(ii++, 2250);//V_NCODE
					pStatement.setBigDecimal(ii++, new BigDecimal(StationInfo.getAdminCode(agmeReportHeader.getStationNumberChina(), acode)));// V_ACODE
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getYear()+1900);
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getMonth()+1);
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getDate());
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getHours());
					pStatement.setInt(ii++, agmeReportHeader.getReport_time().getMinutes());
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					pStatement.setString(ii++, cts_code);
					
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
			logger.error("数据库连接异常:" + e.getMessage());
			return;
		}
		finally {
			if(pStatement != null){
				try{
					pStatement.close();
				}catch (Exception e) {
					logger.error("close statement error!");
				}
			}
		}
	}


}
