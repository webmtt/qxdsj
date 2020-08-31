package cma.cimiss2.dpc.indb.sand;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.nutz.castor.castor.String2Integer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class SurfReportInfoService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String packagePath = "cma.cimiss2.dpc.indb.sand.SurfReportInfoService.java";
	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static void reportInfoToDb( List<ReportInfo> reportInfos,java.sql.Connection connection,String acodeNo, String v_bbb,
			String type,String cts_code, Date recv_time,  String v_cccc, String v_tt,String filepath,List<StatDi> listDi) {
		String sql = "INSERT INTO surf_wea_chn_rep_tab"
				+ " (D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V_CCCC,V_TT,V01301, "
				+ " V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT,D_SOURCE_ID) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		//java.sql.Connection connection  = null;
		Calendar calendar = Calendar.getInstance();
		PreparedStatement pStatement = null;
		File file=new File(filepath);
		String fileN=file.getName();
		try {
			//connection=JdbcUtilsSing.getInstance(druidPooledConnection).getConnection();
			pStatement = new LoggableStatement(connection, sql);
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(type);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(v_tt);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	

					ArrayList<String> headList = (ArrayList<String>) reportInfos.get(i).getT();
					String stationNumberChina = headList.get(0);//区站号
					String longitude = headList.get(1);//经度
					String latitude = headList.get(2);//纬度
					String observationTime = headList.get(3);//资料时间
					
					
					Date yearMoDate = TimeUtil.String2Date(observationTime, "yyyyMMddHHmmss");
					calendar.setTime(yearMoDate);
				//	pStatement.setString(1, observationTime + "_" + stationNumberChina);
					pStatement.setString(2, type);
					pStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(4, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(6, new Timestamp(yearMoDate.getTime()));
					pStatement.setString(7, v_bbb);//V_BBB,
					pStatement.setString(8,v_cccc);//V_CCCC,编报(加工)中心，确认录入站号
					pStatement.setString(9, v_tt);//V_TT,报告类别
					pStatement.setString(10, stationNumberChina);// 区站号(字符)
				
					pStatement.setBigDecimal(11, new BigDecimal(StationCodeUtil.stringToAscii(stationNumberChina)));// 区站号(数字)
				
					pStatement.setBigDecimal(12, new BigDecimal(latitude));// 纬度
					pStatement.setBigDecimal(13, new BigDecimal(longitude));// 经度
					pStatement.setBigDecimal(14, new BigDecimal("2250"));// V_NCODE,国家代码
					pStatement.setBigDecimal(15, new BigDecimal(StationInfo.getAdminCode(stationNumberChina, acodeNo)));// V_ACODE
					pStatement.setBigDecimal(16, new BigDecimal(calendar.get(Calendar.YEAR)));// 年
					pStatement.setBigDecimal(17, new BigDecimal(calendar.get(Calendar.MONTH) + 1));// 月
					pStatement.setBigDecimal(18, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH)));// 日
					pStatement.setBigDecimal(19, new BigDecimal(calendar.get(Calendar.HOUR)));
					pStatement.setBigDecimal(20, new BigDecimal(calendar.get(Calendar.MINUTE)));
					pStatement.setInt(21, reportInfos.get(i).getReport().length());
					pStatement.setString(22, reportInfos.get(i).getReport());
					pStatement.setString(23, cts_code);
					
					di.setIIiii(stationNumberChina);
					di.setDATA_TIME(TimeUtil.date2String(yearMoDate, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(latitude));
					di.setLATITUDE(String.valueOf(longitude));

					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(v_bbb);
					di.setHEIGHT(String.valueOf("999999"));
//					listDi.add(di);
					try{
						pStatement.execute();
						connection.commit();
						listDi.add(di);
					}catch (Exception e) {
						di.setPROCESS_STATE("0");//1成功，0失败
						listDi.add(di);
					}
				} catch (Exception e) {
					infoLogger.error("\n "+packagePath +"\n execute sql  error："
							+ ((LoggableStatement) pStatement).getQueryString() + "\n "+"sql error:" + e.getMessage());
					continue;
				}
			}
		} catch (SQLException e) {
			infoLogger.error(packagePath + "Database connection exception:" + e.getMessage());
			return;
		} catch (Exception e) {
			infoLogger.error(packagePath + "other abnormal information:" + e.getMessage());
			return;
		}finally {
			try {
				if (pStatement != null)
					pStatement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				infoLogger.error("\n "+packagePath+"close database error" + e.getMessage());
			}
		}

	}
}
