package cma.cimiss2.dpc.indb.sevp;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
/**
 * 
 * <br>
 * @Title:  ReportInfoService.java
 * @Package org.cimiss2.dwp.z_sevp
 * @Description:    TODO(服务产品入报文库)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月4日 下午10:24:03   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */

public class ReportInfoService {
	public static final Logger logger = LoggerFactory.getLogger(ReportInfoService.class);
	@SuppressWarnings({ "resource", "deprecation" })
	/**
	 * 
	 * @Title: reportInfoToDb
	 * @Description: TODO(服务产品入报文库函数)
	 * @param reportInfos 报文库集合
	 * @param connection 数据库连接
	 * @param recv_time 接收时间
	 * @param sod_code void 四级编码
	 * @throws：
	 */
	public static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, java.sql.Connection connection, Date recv_time, String sod_code ) {
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO sevp_rep_tab"
				+ " (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V_CCCC,V04001_02,V04002_02,V04003_02,"
				+ "V04004_02,V_TT,V_AA,V_II,V_MIMJ,V01301,"
				+ "V01300,V05001,V06001,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT) "
				+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		
		try {
			connection.setAutoCommit(true);
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {	
				try {
					AgmeReportHeader agmeReportHeader = (AgmeReportHeader) reportInfos.get(i).getT();		
					String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+agmeReportHeader.getCropType()+"_"+agmeReportHeader.getCorrectsign();
					pStatement.setString(1, primkey);
					pStatement.setString(2, sod_code);
					pStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(4, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(6, new Timestamp(agmeReportHeader.getReport_time().getTime()));
					pStatement.setString(7, agmeReportHeader.getCorrectsign());
					pStatement.setString(8, agmeReportHeader.getBul_center());
					pStatement.setBigDecimal(9, new BigDecimal(agmeReportHeader.getForecast_time().getYear() + 1900));
					pStatement.setBigDecimal(10, new BigDecimal(agmeReportHeader.getForecast_time().getMonth()+1));
					pStatement.setBigDecimal(11, new BigDecimal(agmeReportHeader.getForecast_time().getDate()));
					pStatement.setBigDecimal(12, new BigDecimal(agmeReportHeader.getForecast_time().getHours()));
					pStatement.setString(13, agmeReportHeader.getTTAAii().substring(0, 2));
					pStatement.setString(14, agmeReportHeader.getTTAAii().substring(2, 4));
					pStatement.setString(15, agmeReportHeader.getTTAAii().substring(4, 6));
					pStatement.setString(16, agmeReportHeader.getCropType());
					pStatement.setString(17, agmeReportHeader.getStationNumberChina());
					pStatement.setInt(18 ,Integer.parseInt(StationCodeUtil.stringToAscii(agmeReportHeader.getStationNumberChina())));
					pStatement.setDouble(19, agmeReportHeader.getLatitude());
					pStatement.setDouble(20, agmeReportHeader.getLongitude());
					pStatement.setInt(21, agmeReportHeader.getReport_time().getYear()+1900);
					pStatement.setInt(22, agmeReportHeader.getReport_time().getMonth()+1);
					pStatement.setInt(23, agmeReportHeader.getReport_time().getDate());
					pStatement.setInt(24, agmeReportHeader.getReport_time().getHours());
					pStatement.setInt(25, agmeReportHeader.getReport_time().getMinutes());
					pStatement.setInt(26, reportInfos.get(i).getReport().length());
					pStatement.setString(27, reportInfos.get(i).getReport());		
					pStatement.execute();
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
					pStatement = null;
				}catch (Exception e) {
					logger.error("\n Close statement error!");
				}
			}
		}
	}

}
