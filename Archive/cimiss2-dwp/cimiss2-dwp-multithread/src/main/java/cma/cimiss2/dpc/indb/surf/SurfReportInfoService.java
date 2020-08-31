package cma.cimiss2.dpc.indb.surf;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.cimiss2.dwp.tools.utils.WmoStationInfo;
import org.cimiss2.dwp.tools.utils.WmoStationInfo.NcdoStationRuleBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.tools.database.JdbcUtilsSing;

public class SurfReportInfoService {
	public static final Logger logger = LoggerFactory.getLogger(SurfReportInfoService.class);
	public static String packagePath = "cimiss2.dwp.z_surf.ncdc_gsod.SurfReportInfoService.java";

	@SuppressWarnings({ "resource" })
	public static void reportInfoToDb( List<ReportInfo> reportInfos, String v_bbb,
			String type, Date recv_time,  String v_cccc, String v_tt) {
		String sql = "INSERT INTO surf_wea_chn_rep_tab"
				+ " (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V_CCCC,V_TT,V01301, "
				+ " V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		java.sql.Connection connection  = null;
		Calendar calendar = Calendar.getInstance();
		Map<String, Object> proMap = StationInfo.getProMap();
		ArrayList<String> staList = WmoStationInfo.getStaList();
		Map<String, NcdoStationRuleBean> staMap = WmoStationInfo.getStaMap();
		PreparedStatement pStatement = null;
		try {
			connection=JdbcUtilsSing.getInstance("config/suf_ncdc_gsod/z_surf_ncdc_gsod_db_config.xml").getConnection();
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {

				try {
					ArrayList<String> surList = (ArrayList<String>) reportInfos.get(i).getT();
					String stn = surList.get(0);//原始STN站号
					String wban = surList.get(1);//原始WBAN站号
					String yearMoDa = surList.get(2);//资料时间
					Date yearMoDate = TimeUtil.String2Date(yearMoDa, "yyyyMMdd");
					calendar.setTime(yearMoDate);
					pStatement.setString(1, yearMoDa + "_" + stn);
					pStatement.setString(2, type);
					pStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(4, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(6, new Timestamp(yearMoDate.getTime()));
					pStatement.setString(7, v_bbb);//V_BBB,
					pStatement.setString(8,v_cccc);//V_CCCC,
					pStatement.setString(9, v_tt);//V_TT

					if (staList.contains(stn)) {
						if (stn.length() >= 5) {
							stn = stn.substring(0, 5);
							pStatement.setString(10, stn);// 区站号(字符)
						} else {
							pStatement.setString(10, stn);// 区站号(字符)
						}

					} else {
						stn = "999999";
						pStatement.setString(10, stn);// 区站号(字符)
					}
					pStatement.setBigDecimal(11, new BigDecimal(StationCodeUtil.stringToAscii(stn)));// 区站号(数字)
					NcdoStationRuleBean ncdoBean = staMap.get(
							surList.get(0) + "_" + wban);
					if (null != ncdoBean) {
						pStatement.setBigDecimal(12, new BigDecimal(ncdoBean.getLat()));// 纬度
						pStatement.setBigDecimal(13, new BigDecimal(ncdoBean.getLon()));// 经度
					} else {
						pStatement.setBigDecimal(12, new BigDecimal("999999"));// 纬度
						pStatement.setBigDecimal(13, new BigDecimal("999999"));// 经度
					}
					pStatement.setBigDecimal(14, new BigDecimal("0"));// V_NCODE
					pStatement.setBigDecimal(15, new BigDecimal("5"));// V_ACODE
					pStatement.setBigDecimal(16, new BigDecimal(calendar.get(Calendar.YEAR)));// 年
					pStatement.setBigDecimal(17, new BigDecimal(calendar.get(Calendar.MONTH) + 1));// 月
					pStatement.setBigDecimal(18, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH)));// 日
					pStatement.setBigDecimal(19, new BigDecimal(calendar.get(Calendar.HOUR)));
					pStatement.setBigDecimal(20, new BigDecimal(calendar.get(Calendar.MINUTE)));
					pStatement.setInt(21, reportInfos.get(i).getReport().length());
					pStatement.setString(22, reportInfos.get(i).getReport());

					pStatement.execute();
				} catch (Exception e) {
					logger.error(packagePath + "sql error:" + e.getMessage());
					continue;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(packagePath + "数据库连接异常:" + e.getMessage());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(packagePath + "其他异常信息:" + e.getMessage());
			return;
		}finally {
			try {
				if (pStatement != null)
					pStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.error("\n "+packagePath+"关闭数据库异常" + e.getMessage());
			}
		}

	}


}
