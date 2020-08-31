package cma.cimiss2.dpc.indb.surf.dc_surf_day2.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.StationCodeUtil;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataDay;
import cma.cimiss2.dpc.decoder.surf.DecodeDAY;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static String cts_code = StartConfig.ctsCode();
	public static String sod_code = StartConfig.sodCode();
	public static String rep_sod_code = StartConfig.reportSodCode();
	public int defaultInt = 999999;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}
	/**
	 * @param loggerBuffer 
	 * @param fileN
	 * @Title: processSuccessReport 
	 * @Description:(质控后地面气象要素日值_一体化观测数据解码结果集，正确解码的报文入库) 
	 * @param parseResult  解码结果集
	 * @param filepath  文件的绝对路径
	 * @param recv_time  消息接收时间
	 * @return  返回值说明
	 * @throws
	 */

	public static DataBaseAction processSuccessReport(ParseResult<SurfaceObservationDataDay> parseResult, Date recv_time, String fileN, StringBuffer loggerBuffer, String filepath) {
		java.sql.Connection connection = null;
		java.sql.Connection reportConnection = null;
		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			List<SurfaceObservationDataDay> surfDayDatas = parseResult.getData();
			insertDB(surfDayDatas, connection, recv_time, fileN, loggerBuffer,filepath);
		 
			@SuppressWarnings("rawtypes")
			List<ReportInfo> reportInfos = parseResult.getReports();
			reportConnection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
			
			reportInfoToDb(reportInfos, reportConnection, recv_time, filepath ,loggerBuffer);
			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		}
		finally{
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close databse connection error: "+e.getMessage());
				}
			}
			
			if(reportConnection != null) {
				try {
					reportConnection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close database connection error: "+e.getMessage());
				}
			}
		}
	}
	/**
	 * @param loggerBuffer 
	 * @param fileN 
	 * 
	 * @Title: insertDB   
	 * @Description: (质控后地面气象要素日值_一体化观测数据资料入库)   
	 * @param: @param acidrain 入库对象集合
	 * @param: @param connection 数据库连接
	 * @param: @param recv_time  接收时间
	 * @return: void      
	 * @throws
	 */
	@SuppressWarnings("deprecation")
	private static void insertDB(List<SurfaceObservationDataDay> surfDayDatas, java.sql.Connection connection,
			Date recv_time,String fileN, StringBuffer loggerBuffer, String filepath) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		String sql = "INSERT INTO "+StartConfig.valueTable()+"(D_RECORD_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,"
				+ "V01300,V05001,V06001,V04001,V04002,V04003,V07001,V02301,V_ACODE,V13308,V13309,V13032,V20305,V20326_NS,V20306_NS,V20307_NS,V20326_WE,V20306_WE,V20307_WE,V12001,V11001,V11002,V20304,Q13308,Q13309,Q13032,Q20305,Q20326_NS,Q20306_NS,Q20307_NS,Q20326_WE,Q20306_WE,Q20307_WE,Q12001,Q11001,Q11002,Q20304,V_BBB,D_SOURCE_ID) "
				+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ " ?, ?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?, ?, ?) ";
		if(connection != null){		
			try {	
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");		
				for(int i = 0; i <surfDayDatas.size(); i ++){
					pStatement = new LoggableStatement(connection, sql);
					connection.setAutoCommit(false);
					SurfaceObservationDataDay surfDayData = surfDayDatas.get(i);
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("质控后地面日值资料");			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					String datetime=sdf.format(surfDayData.getObservationTime());
					
					int ii = 1;
					pStatement.setString(ii++, sdf.format(surfDayData.getObservationTime())+"_"+surfDayData.getStationNumberChina());//记录标识
					pStatement.setString(ii++, sod_code);//资料标识
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//入库时间
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));//收到时间
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
					pStatement.setTimestamp(ii++, new Timestamp(surfDayData.getObservationTime().getTime()));//资料时间
					pStatement.setString(ii++, surfDayData.getStationNumberChina());//区站号(字符)
					pStatement.setInt(ii++,Integer.parseInt(StationCodeUtil.stringToAscii(surfDayData.getStationNumberChina())) );//区站号(数字)
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getLatitude())));//纬度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getLongitude())));//经度
					pStatement.setInt(ii++, Integer.parseInt(datetime.substring(0, 4)));//V04001
					pStatement.setInt(ii++, Integer.parseInt(datetime.substring(4, 6)));//V04002
					pStatement.setInt(ii++, Integer.parseInt(datetime.substring(6, 8)));//V04003
					
					String info = (String) proMap.get(surfDayData.getStationNumberChina() + "+01");
					String V07001="999999";
					String V02301="999999";
					String V_ACODE="999999";
					if(info == null||"".equals(info)) {
						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));
						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));
						pStatement.setBigDecimal(ii++, new BigDecimal("999999"));
					}else{
						String[] infos = info.split(",");
						 V07001=("null".equals(infos[3])||"".equals(infos[3])) ? "999999" : infos[3];
						 V02301=("null".equals(infos[6])||"".equals(infos[6])) ? "999999" : infos[6];
						 V_ACODE=("null".equals(infos[5])||"".equals(infos[5])) ? "999999" : infos[5];
						
						pStatement.setBigDecimal(ii++,  new BigDecimal(V07001));//V07001
						pStatement.setBigDecimal(ii++,  new BigDecimal(V02301));//V02301
						pStatement.setBigDecimal(ii++,  new BigDecimal(V_ACODE));//V_ACODE
					}
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getPrecipitationBetween20And8().getValue())));//V13308  20-8时雨量筒观测降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getPrecipitationBetween8And20().getValue())));//V13309  8-20时雨量筒观测降水量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getEvaporation().getValue())));//V13032 蒸发量
//					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getEvaporation().getValue())));//V13033 蒸发量
					pStatement.setString(ii++, surfDayData.getOverheadWireIce().getValue());//V20305 电线积冰-现象 
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getDiameterOfOverheadWireIceNS().getValue())));//V20326_NS 电线积冰-南北方向直径
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getThicknessOfOverheadWireIceNS().getValue())));//  V20306_NS 电线积冰-南北方向厚度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeightOfOverheadWireIceNS().getValue())));//V20307_NS 电线积冰-南北方向重量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getDiameterOfOverheadWireIceEW().getValue())));//V20326_WE 电线积冰-东西方向直径
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getThicknessOfOverheadWireIceEW().getValue())));//V20306_WE 电线积冰-东西方向厚度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeightOfOverheadWireIceEW().getValue())));//V20307_WE 电线积冰-东西方向重量
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceTemperature().getValue())));//V12001 电线积冰-温度
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceWindDirection().getValue())));//V11001 电线积冰-风向
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceWindSpeed().getValue())));//V11002 电线积冰-风速
					pStatement.setString(ii++, String.valueOf(surfDayData.getWeather().getValue().replaceAll("\t", " ")));//V20304天气现象记录
					
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getPrecipitationBetween20And8().getQuality().get(0).getCode())));//V13308  20-8时雨量筒观测降水量质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getPrecipitationBetween8And20().getQuality().get(0).getCode())));//V13309  8-20时雨量筒观测降水量质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getEvaporation().getQuality().get(0).getCode())));//Q13032 蒸发量质控码
//					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getEvaporation().getQuality().get(0).getCode())));//Q13033 蒸发量质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIce().getQuality().get(0).getCode())));//Q20305 电线积冰-现象 质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getDiameterOfOverheadWireIceNS().getQuality().get(0).getCode())));//Q20326_NS 电线积冰-南北方向直径质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getThicknessOfOverheadWireIceNS().getQuality().get(0).getCode())));//  Q20306_NS 电线积冰-南北方向厚度质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeightOfOverheadWireIceNS().getQuality().get(0).getCode())));//Q20307_NS 电线积冰-南北方向重量质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getDiameterOfOverheadWireIceEW().getQuality().get(0).getCode())));//Q20326_WE 电线积冰-东西方向直径质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getThicknessOfOverheadWireIceEW().getQuality().get(0).getCode())));//Q20306_WE 电线积冰-东西方向厚度质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeightOfOverheadWireIceEW().getQuality().get(0).getCode())));//Q20307_WE 电线积冰-东西方向重量质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceTemperature().getQuality().get(0).getCode())));//Q12001 电线积冰-温度质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceWindDirection().getQuality().get(0).getCode())));//Q11001 电线积冰-风向质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceWindSpeed().getQuality().get(0).getCode())));//Q11002 电线积冰-风速质控码
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeather().getQuality().get(0).getCode())));//Q20304天气现象记录质控码
					
					pStatement.setString(ii++, surfDayData.getCorrectionIndicator());//文件更正标识
					pStatement.setString(ii++, cts_code);
					
					di.setIIiii(surfDayData.getStationNumberChina());
					di.setDATA_TIME(TimeUtil.date2String(surfDayData.getObservationTime(), "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());	
					di.setLATITUDE(String.valueOf(surfDayData.getLatitude()));
					di.setLONGTITUDE(String.valueOf(surfDayData.getLongitude()));
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(surfDayData.getCorrectionIndicator());
					di.setHEIGHT(String.valueOf("999999"));
					listDi.add(di);
					try{
						pStatement.execute();
						connection.commit();
						loggerBuffer.append("\n 成功入库一条数据 ！: "+fileN+" :"+((LoggableStatement)pStatement).getQueryString());
					}catch(SQLException e){
						if (e.getErrorCode() == 13001 || e.getErrorCode() == 1062) {//主键冲突，则进一步检查是否是更正报
							String[] Existresult=findExistData(surfDayData,connection);
							String d_datetime=Existresult[0];
							String v_bbb=Existresult[1];//库里原有更正标识
							String V_BBB=surfDayData.getCorrectionIndicator();//当前报文的更正标识
							if (d_datetime!=null) {//库里有记录
								if(V_BBB.equals("000")&&(v_bbb==null||(v_bbb!=null&&v_bbb.equals("000"))||(v_bbb!=null&&v_bbb.startsWith("RR")))){
									updateData(surfDayData,connection,fileN,loggerBuffer,di);
								}
								if(V_BBB.startsWith("RR")&&(v_bbb==null||(v_bbb!=null&&v_bbb.equals("000"))||(v_bbb!=null&&v_bbb.startsWith("RR")))){
									updateData(surfDayData,connection,fileN,loggerBuffer,di);
								}
								if(V_BBB.startsWith("C")&& (v_bbb==null||(v_bbb!=null&&(V_BBB.compareTo(v_bbb) > 0)) ||v_bbb.startsWith("RR"))){
									updateData(surfDayData,connection,fileN,loggerBuffer,di);
								}
							}
						}else{
							loggerBuffer.append("\n 插入一条数据失败 ！: "+fileN+" :"+e.getMessage());
							di.setPROCESS_STATE("0");
						}
					}finally {
						if(pStatement != null) {
							try {
								pStatement.close();
							} catch (SQLException e) {
								loggerBuffer.append("\n insertDB Close Statement failed: "+e.getMessage());
							}
						}
					}
				} // end for
			}catch (SQLException e) {
				loggerBuffer.append("\n insertDB Create Statement failed: "+e.getMessage());
			}catch (Exception e) {
				loggerBuffer.append("\n insertDB error: "+e.getMessage());
			}finally {
				if(pStatement != null) {
					try {
						pStatement.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n insertDB Close Statement failed: "+e.getMessage());
					}
				}
			}
		} 	
	}
	private static void  updateData(SurfaceObservationDataDay surfDayData,Connection connection,String fileN,StringBuffer loggerBuffer,StatDi di){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement pStat = null;
		try{
			String updateSql="update "+StartConfig.valueTable()+" set D_UPDATE_TIME=?, V13308= ?,V13309=?,V13032=?, V20305=?, "
					+ "V20326_NS=?, V20306_NS=?, V20307_NS=?, V20326_WE=?, V20306_WE=?, V20307_WE=?, V12001=?, V11001=?,"
					+ " V11002=? ,V20304=?, Q13308= ?,Q13309=?,Q13032=?, Q20305=?,Q20326_NS=?, Q20306_NS=?, Q20307_NS=?, Q20326_WE=?, "
					+ "Q20306_WE=?, Q20307_WE=?, Q12001=?, Q11001=?, Q11002=? ,Q20304=?, V_BBB=?  where d_datetime=? and V01301=?";
			pStat = new LoggableStatement(connection, updateSql);
			int ii = 1;
			pStat.setTimestamp(ii++, new Timestamp(new Date().getTime()));//更新时间
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getPrecipitationBetween20And8().getValue())));//V13308  20-8时雨量筒观测降水量
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getPrecipitationBetween8And20().getValue())));//V13309  8-20时雨量筒观测降水量
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getEvaporation().getValue())));//V13032 蒸发量
//			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getEvaporation().getValue())));//V13033 蒸发量
			pStat.setString(ii++, surfDayData.getOverheadWireIce().getValue());//V20305 电线积冰-现象 
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getDiameterOfOverheadWireIceNS().getValue())));//V20326_NS 电线积冰-南北方向直径
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getThicknessOfOverheadWireIceNS().getValue())));//  V20306_NS 电线积冰-南北方向厚度
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeightOfOverheadWireIceNS().getValue())));//V20307_NS 电线积冰-南北方向重量
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getDiameterOfOverheadWireIceEW().getValue())));//V20326_WE 电线积冰-东西方向直径
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getThicknessOfOverheadWireIceEW().getValue())));//V20306_WE 电线积冰-东西方向厚度
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeightOfOverheadWireIceEW().getValue())));//V20307_WE 电线积冰-东西方向重量
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceTemperature().getValue())));//V12001 电线积冰-温度
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceWindDirection().getValue())));//V11001 电线积冰-风向
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceWindSpeed().getValue())));//V11002 电线积冰-风速
			pStat.setString(ii++, String.valueOf(surfDayData.getWeather().getValue().replaceAll("\t", " ")));//V20304天气现象记录
			
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getPrecipitationBetween20And8().getQuality().get(0).getCode())));//Q13308  20-8时雨量筒观测降水量质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getPrecipitationBetween8And20().getQuality().get(0).getCode())));//Q13309  8-20时雨量筒观测降水量质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getEvaporation().getQuality().get(0).getCode())));//Q13032 蒸发量质控码
//			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getEvaporation().getQuality().get(0).getCode())));//Q13033 蒸发量质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIce().getQuality().get(0).getCode())));//Q20305 电线积冰-现象 质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getDiameterOfOverheadWireIceNS().getQuality().get(0).getCode())));//Q20326_NS 电线积冰-南北方向直径质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getThicknessOfOverheadWireIceNS().getQuality().get(0).getCode())));//  Q20306_NS 电线积冰-南北方向厚度质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeightOfOverheadWireIceNS().getQuality().get(0).getCode())));//Q20307_NS 电线积冰-南北方向重量质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getDiameterOfOverheadWireIceEW().getQuality().get(0).getCode())));//Q20326_WE 电线积冰-东西方向直径质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getThicknessOfOverheadWireIceEW().getQuality().get(0).getCode())));//Q20306_WE 电线积冰-东西方向厚度质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeightOfOverheadWireIceEW().getQuality().get(0).getCode())));//Q20307_WE 电线积冰-东西方向重量质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceTemperature().getQuality().get(0).getCode())));//Q12001 电线积冰-温度质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceWindDirection().getQuality().get(0).getCode())));//Q11001 电线积冰-风向质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getOverheadWireIceWindSpeed().getQuality().get(0).getCode())));//Q11002 电线积冰-风速质控码
			pStat.setBigDecimal(ii++, new BigDecimal(String.valueOf(surfDayData.getWeather().getQuality().get(0).getCode())));//Q20304天气现象记录质控码
			pStat.setString(ii++, surfDayData.getCorrectionIndicator());//文件更正标识
			
//			pStat.setString(ii++, sdf.format(surfDayData.getObservationTime())+"_"+surfDayData.getStationNumberChina());//记录标识
			pStat.setTimestamp(ii++, new Timestamp(surfDayData.getObservationTime().getTime()));//资料时间
			pStat.setString(ii++, surfDayData.getStationNumberChina());//站号
			try {
				pStat.execute();
				connection.commit();
				loggerBuffer.append("\n 成功更新一条数据！"+fileN+"ObservationTime:"+surfDayData.getObservationTime()+" StationNumber:"+surfDayData.getStationNumberChina());
			} catch (Exception e) {
				loggerBuffer.append("\n 更新条数据 失败！"+((LoggableStatement)pStat).getQueryString()+e.getMessage());
				di.setPROCESS_STATE("0");// 1成功，0失败
			}
		}catch (Exception e) {
			loggerBuffer.append("\n updateData create Statement failed: "+e.getMessage());
		}finally {
			if(pStat != null) {
				try {
					pStat.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n updateData Close Statement failed: "+e.getMessage());
				}
			}
		}
	}
	public static void reportInfoToDb(@SuppressWarnings("rawtypes") List<ReportInfo> reportInfos, Connection connection, Date recv_time, String filepath, StringBuffer loggerBuffer ) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement pStatement = null;
		File file=new File(filepath);
		String fileN=file.getName();
		String[] fileNameSplit = fileN.split("_");
		String V_TT = fileNameSplit[6].split("-")[0];
        String V_CCCC = fileNameSplit[3].substring(0, 4);
		String sql = "INSERT INTO "+StartConfig.reportTable()+" "
				+ " (D_RECORD_ID,D_DATA_ID,D_SOURCE_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V_BBB,V_CCCC,V_TT,V01301, "
				+ " V01300,V05001,V06001,V_NCODE,V_ACODE,V04001,V04002,V04003,V04004,V04005,V_LEN,V_REPORT) "
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";		
		try {
			pStatement = new LoggableStatement(connection, sql);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			for (int i = 0; i < reportInfos.size(); i++) {	
				try {
					StatDi di = new StatDi();	
					di.setFILE_NAME_O(fileN);
					di.setDATA_TYPE(rep_sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT(V_TT);			
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(fileN);
					di.setBUSINESS_STATE("1"); //1成功，0失败
					di.setPROCESS_STATE("1");  //1成功，0失败	
					
					Map<String, Object> repmap = (Map<String, Object>) reportInfos.get(i).getT();//报文头
					String observationTime = (String) repmap.get("D_DATETIME");
					String stationNumberChina = (String) repmap.get("V01301");
			        int num = stationNumberChina.substring(0, 1).hashCode();
			        String D_RECORD_ID = observationTime + "_" + stationNumberChina + "_" +cts_code+  "_" + V_TT;
//					String primkey = sdf.format(agmeReportHeader.getReport_time())+"_"+agmeReportHeader.getStationNumberChina()+"_"+v_cccc;
			        int ii=1;
					pStatement.setString(ii++, D_RECORD_ID);
					pStatement.setString(ii++, rep_sod_code);
					pStatement.setString(ii++, cts_code);
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(recv_time.getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new Date().getTime()));
					pStatement.setTimestamp(ii++, new Timestamp(new SimpleDateFormat("yyyyMMddHHmmss").parse(observationTime).getTime()));
					pStatement.setString(ii++, repmap.get("V_BBB").toString());
					pStatement.setString(ii++, V_CCCC);
					pStatement.setString(ii++, V_TT);
					pStatement.setString(ii++, repmap.get("V01301").toString());
					if (num >= 48 & num <= 57) {
						pStatement.setString(ii++,  stationNumberChina);
		            } else {
		            	pStatement.setString(ii++,  String.valueOf(num) + stationNumberChina.substring(1));
		            }
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(repmap.get("V5001"))));
					pStatement.setBigDecimal(ii++, new BigDecimal(String.valueOf(repmap.get("V6001"))));
					pStatement.setInt(ii++, 1);//V_NCODE
					pStatement.setBigDecimal(ii++, new BigDecimal(StationInfo.getAdminCode(stationNumberChina, "01")));// V_ACODE
					pStatement.setInt(ii++, Integer.parseInt(observationTime.substring(0, 4)));
					pStatement.setInt(ii++, Integer.parseInt(observationTime.substring(4, 6)));
					pStatement.setInt(ii++, Integer.parseInt(observationTime.substring(6, 8)));
					pStatement.setInt(ii++, Integer.parseInt(observationTime.substring(8, 10)));
					pStatement.setInt(ii++, Integer.parseInt(observationTime.substring(10,12)));
					pStatement.setInt(ii++, reportInfos.get(i).getReport().length());
					pStatement.setString(ii++, reportInfos.get(i).getReport());
					
					Date obstime=new SimpleDateFormat("yyyyMMddHHmmss").parse(observationTime);
					String obstime1=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(obstime);
					
					String HeightOfSation="999999";
					String info = (String) proMap.get(stationNumberChina + "+01");
					if(info != null && !"".equals(info)) {
						String[] infos = info.split(",");
						HeightOfSation=("null".equals(infos[3])||"".equals(infos[3])) ? "999999" : infos[3];
					}
					di.setIIiii(stationNumberChina);
					di.setDATA_TIME(obstime1);
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					di.setLONGTITUDE(String.valueOf(repmap.get("V5001")));
					di.setLATITUDE(String.valueOf(repmap.get("V6001")));
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG(repmap.get("V_BBB").toString());
					di.setHEIGHT(String.valueOf(HeightOfSation));
					try {
						pStatement.execute();
						connection.commit();
						loggerBuffer.append("\n 成功插入报告表一条数据！"+fileN);
						listDi.add(di);
					} catch (SQLException e) {
						loggerBuffer.append("\n reportInfoToDb sql error:"+e.getMessage()+fileN);
						di.setPROCESS_STATE("0");//1成功，0失败
						listDi.add(di);
						continue;
					}
				} catch (Exception e) {
					loggerBuffer.append("\n reportInfoToDb  error:"+e.getMessage()+fileN);
					continue;
				} 
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n reportInfoToDb Database connection error:" + e.getMessage());
			return;
		}
		finally {
			if(pStatement != null){
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n close statement: " + e.getMessage());
				}
			}
		}
	}
	static	String[] findExistData(SurfaceObservationDataDay surfDayData,Connection connection){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		PreparedStatement Pstmt = null;
		ResultSet resultSet  = null;
		String v_bbb = null;
		String d_datetime=null;
		String rntString [] = {d_datetime, v_bbb};
		String sql = "select D_DATETIME,V_BBB from "+StartConfig.valueTable()+" "
				+ "where  D_DATETIME = ? and V01301 = ? ";
		try{
			if(connection != null){	
				Pstmt = connection.prepareStatement(sql);
				int ii = 1;
				Pstmt.setTimestamp(ii++, new Timestamp(surfDayData.getObservationTime().getTime()));//资料时间
				Pstmt.setString(ii++, surfDayData.getStationNumberChina());//站号
				resultSet = Pstmt.executeQuery();
				if(resultSet.next()){
					d_datetime = resultSet.getString(1);
					v_bbb = resultSet.getString(2);
				}
			}
		}catch(SQLException e){
			infoLogger.error("\n findExistData create Statement error " + e.getMessage());
		}
		finally {
			if(Pstmt != null) {
				try {
					Pstmt.close();
				} catch (SQLException e) {
					infoLogger.error("\n findExistData close Statement error " + e.getMessage());
				}
			}
			if(resultSet != null){
				try{
					resultSet.close();
				}catch(SQLException e){
					infoLogger.error("\n findExistData close resultSet error " + e.getMessage());
				}
			}
		}
		rntString[0] = d_datetime;
		rntString[1] = v_bbb;
		return rntString;
	}
	
	public static void main(String[] args) {
		File file = new File("D:\\TEMP\\G.3.1.1\\Z_CAWN_I_57518_20190314013028_O_AR_FTM.txt");
		String fileN = file.getName();
		Date recv_time = new Date();
		StringBuffer loggerBuffer = new StringBuffer();
		DecodeDAY decodeSurfDay = new DecodeDAY();
		ParseResult<SurfaceObservationDataDay> parseResult= (ParseResult<SurfaceObservationDataDay>) decodeSurfDay.decode(file, new HashSet<String>());
		if(parseResult.isSuccess()){
			DataBaseAction action = DbService.processSuccessReport(parseResult, recv_time, fileN, loggerBuffer,file.getPath());
		}
	}
}
