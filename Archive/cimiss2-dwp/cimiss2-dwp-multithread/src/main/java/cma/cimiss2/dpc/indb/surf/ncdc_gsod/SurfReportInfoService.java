package cma.cimiss2.dpc.indb.surf.ncdc_gsod;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.cimiss2.dwp.tools.utils.WmoStationInfo;
import org.cimiss2.dwp.tools.utils.WmoStationInfo.NcdoStationRuleBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ReportInfo;

public class SurfReportInfoService {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String packagePath = "cma.cimiss2.dpc.indb.surf.ncdc_gsod.SurfReportInfoService";
	public static void reportInfoToDb( @SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, String v_bbb,
			String type, Date recv_time,  String v_cccc, String v_tt, StringBuffer loggerBuffer) {
		String sql = "INSERT INTO "+StartConfig.reportTable()+" "
				+ " (D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V_CCCC,V_TT,V01301, "
				+ " V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		java.sql.Connection connection  = null;
		Calendar calendar = Calendar.getInstance();
//		ArrayList<String> staList = WmoStationInfo.getStaList();
//		Map<String, NcdoStationRuleBean> staMap = WmoStationInfo.getStaMap();
		PreparedStatement pStatement = null;
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");;
			pStatement = new LoggableStatement(connection, sql);
			for (int i = 0; i < reportInfos.size(); i++) {
				try {
					@SuppressWarnings("unchecked")
					ArrayList<String> surList = (ArrayList<String>) reportInfos.get(i).getT();
					String stn = surList.get(0);//原始STN站号
					String wban = surList.get(1);//原始WBAN站号
					String yearMoDa = surList.get(2);//资料时间
					Date yearMoDate = TimeUtil.String2Date(yearMoDa, "yyyyMMdd");
					calendar.setTime(yearMoDate);
					pStatement.setString(1, yearMoDa + "_" + stn.substring(0, 5));
					pStatement.setString(2, type);
					pStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(4, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(5, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(6, new Timestamp(yearMoDate.getTime()));
					pStatement.setString(7, v_bbb);//V_BBB,
					pStatement.setString(8,v_cccc);//V_CCCC,
					pStatement.setString(9, v_tt);//V_TT

//					if (staList.contains(stn)) {
						if (stn.length() >= 5) {
							stn = stn.substring(0, 5);
							pStatement.setString(10, stn);// 区站号(字符)
						} else {
							pStatement.setString(10, stn);// 区站号(字符)
						}

//					} else {
//						stn = "999999";
//						pStatement.setString(10, stn);// 区站号(字符)
//					}
					pStatement.setBigDecimal(11, new BigDecimal(StationCodeUtil.stringToAscii(stn)));// 区站号(数字)
//					NcdoStationRuleBean ncdoBean = staMap.get(
//							surList.get(0) + "_" + wban);
					String longitude=getStationInfoFromConfig(stn, "01", "longtitude");
					String latitude=getStationInfoFromConfig(stn, "01", "latitude");
//					if (null != ncdoBean) {
						pStatement.setBigDecimal(12, new BigDecimal(latitude));// 纬度
						pStatement.setBigDecimal(13, new BigDecimal(longitude));// 经度
//					} else {
//						pStatement.setBigDecimal(12, new BigDecimal("999999"));// 纬度
//						pStatement.setBigDecimal(13, new BigDecimal("999999"));// 经度
//					}
					pStatement.setBigDecimal(14, new BigDecimal("2250"));// V_NCODE
					pStatement.setBigDecimal(15, new BigDecimal("5"));// V_ACODE
					pStatement.setBigDecimal(16, new BigDecimal(calendar.get(Calendar.YEAR)));// 年
					pStatement.setBigDecimal(17, new BigDecimal(calendar.get(Calendar.MONTH) + 1));// 月
					pStatement.setBigDecimal(18, new BigDecimal(calendar.get(Calendar.DAY_OF_MONTH)));// 日
					pStatement.setBigDecimal(19, new BigDecimal(calendar.get(Calendar.HOUR)));
					pStatement.setBigDecimal(20, new BigDecimal(calendar.get(Calendar.MINUTE)));
					pStatement.setInt(21, reportInfos.get(i).getReport().length());
					pStatement.setString(22, reportInfos.get(i).getReport());

					pStatement.execute();
					if(connection.getAutoCommit() == false)
						connection.commit();
				} catch (Exception e) {
					loggerBuffer.append("\n "+packagePath +"\n execute sql error: "
							+ ((LoggableStatement) pStatement).getQueryString() + "\n "+"sql error: " + e.getMessage());
					continue;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			loggerBuffer.append(packagePath + " Database connection error: " + e.getMessage());
			return;
		} catch (Exception e) {
			e.printStackTrace();
			loggerBuffer.append(packagePath + " error:" + e.getMessage());
			return;
		}finally {
			try {
				if (pStatement != null)
					pStatement.close();
			}catch (Exception e) {
				loggerBuffer.append("\n " + packagePath + " Close statement error: " + e.getMessage());
			}
			try{
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				loggerBuffer.append("\n " + packagePath + " Close database connection error: " + e.getMessage());
			}
		}

	}
	/**
     * 获取经纬度信息
     * 
     * @param stationNumberChina
     * @param stationTypeNo
     * @param key
     * @return String
     */
    private static String getStationInfoFromConfig(String stationNumberChina, String stationTypeNo,
            String key) {
        String resValue = "999999";
        try {
            Map<String, Object> proMap = StationInfo.getProMap();
//        	 Map<String, Object> proMap =getStationMap();
            String info = (String) proMap.get(stationNumberChina + "+" + stationTypeNo);
            if(info != null){
	            String[] infos = info.split(",");
	           
	            if (infos.length < 10) {
	                return resValue;
	            } else {
	                key = key.toLowerCase();
	                if(key.equals("longtitude")) {
	                	 resValue = "null".equals(infos[1]) ? "999999" : infos[1];
	                }else if(key.equals("latitude")) {
	                	 resValue = "null".equals(infos[2]) ? "999999" : infos[2];
	                }
	            }
            }
            else
            	System.out.println("Station " + stationNumberChina + " not exist in StationInfo_Config.lua");
        } catch (Exception e) {
        	System.err.println("获取StationInfo_Config.lua 配置信息失败:"+e.getMessage());
        }
        return resValue;
    }	

}
