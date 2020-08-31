package cma.cimiss2.dpc.indb.cawn.dc_cawn_aap.service;

import java.io.File;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import cma.cimiss2.dpc.decoder.bean.cawn.cawnAAP;
import cma.cimiss2.dpc.decoder.cawn.DecodeAAP;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

/**
 * 
 * <br>
 * 
 * @Title: DbService.java
 * @Package cma.cimiss2.dpc.indb.cawn.aap.service
 * @Description: TODO(黑碳气溶胶解码入库)
 * 
 *               <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年6月1日 上午9:56:48   cuihongyuan    Initial creation.
 *               </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class DbService {
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	static String cts_code = StartConfig.ctsCode();
	static String sod_code = StartConfig.sodCode();
//	public static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	/**
	 * 
	 * @Title: processSuccessReport
	 * @Description: TODO(黑碳气溶胶解码入库函数)
	 * @param parseResult
	 * @param filepath
	 * @param recv_time
	 * @param loggerBuffer
	 * @param fileN
	 * @return DataBaseAction @throws：
	 */
	public static void main(String[] args) {
		DecodeAAP decodeAAP = new DecodeAAP();
		String filepath = "C:\\BaiduNetdiskDownload\\test\\G.0002.0001.R001\\Z_CAWN_I_58659_20191224040000_O_AER-FLD-AAP.TXT";
		File file = new File(filepath);
		long length = StartConfig.maxFileSize();
		long filesize = file.length();
		if (file.length() > StartConfig.maxFileSize()) {
			System.out.println(file.length());
		}

		ParseResult<cawnAAP> parseResult = decodeAAP.DecodeFile(file);
		DataBaseAction action = null;
		Date recv_time = new Date();
		StringBuffer loggerBuffer = new StringBuffer();

//		if (StartConfig.getDatabaseType() == 1) {
		action = DbService.processSuccessReport(parseResult, filepath, recv_time, loggerBuffer);
		System.out.println("insertDBService over!");
//		}

	}

	public static DataBaseAction processSuccessReport(ParseResult<cawnAAP> parseResult, String filepath, Date recv_time,
			StringBuffer loggerBuffer) {
		java.sql.Connection connection = null;
//		java.sql.Connection reportConnection = null;

		try {
			// 获取数据库连接
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			List<cawnAAP> cawnAAPs = parseResult.getData();
			// 入库
			insertDB(cawnAAPs, connection, recv_time, filepath, loggerBuffer);

			return DataBaseAction.SUCCESS;
		} catch (Exception e) {
			loggerBuffer.append("\n Database connection error!");
			return DataBaseAction.CONNECTION_ERROR;
		} finally {
		
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close databse connection error: " + e.getMessage());
				}
			}

		}
	}

	private static void insertDB(List<cawnAAP> cawnAAPs, java.sql.Connection connection, Date recv_time,
			String filepath, StringBuffer loggerBuffer) {
		Map<String, Object> proMap = StationInfo.getProMap();
		PreparedStatement prestmt = null;
		String table_name = StartConfig.valueTable();
//		String table_name = "CAWN_CHN_BC_TAB";
		String sql = "insert into " + table_name + "(D_RECORD_ID,D_DATA_ID,D_DATETIME,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,"
				+ "V01301,V01300,V_BBB,V04001,V04002,V04003,V04004,V04005,V05001,V06001,"
				+ "V07001,V_ACODE,V_ITEM_CODE,V70012,V_BC_370nm,V_BC_470nm,V_BC_520nm,V_BC_590nm,V_BC_660nm,V_BC_880nm,"
				+ "V_BC_950nm,V_SZ_370nm,V_SB_370nm,V_RZ_370nm,V_RB_370nm,V_Fri_370nm,V_Attn_370nm,V_SZ_470nm,V_SB_470nm,V_RZ_470nm,"
				+ "V_RB_470nm,V_Fri_470nm,V_Attn_470nm,V_SZ_520nm,V_SB_520nm,V_RZ_520nm,V_RB_520nm,V_Fri_520nm,V_Attn_520nm,V_SZ_590nm,"
				+ "V_SB_590nm,V_RZ_590nm,V_RB_590nm,V_Fri_590nm,V_Attn_590nm,V_SZ_660nm,V_SB_660nm,V_RZ_660nm,V_RB_660nm,V_Fri_660nm,"
				+ "V_Attn_660nm,V_SZ_880nm,V_SB_880nm,V_RZ_880nm,V_RB_880nm,V_Fri_880nm,V_Attn_880nm,V_SZ_950nm,V_SB_950nm,V_RZ_950nm,"
				+ "V_RB_950nm,V_Fri_950nm,V_Attn_950nm,"
				+ "V_TIME_BASE,V_RC_370nm,V_S1C_370nm,V_S2C_370nm,V_RC_470nm,V_S1C_470nm,V_S2C_470nm,"
				+ "V_RC_520nm,V_S1C_520nm,V_S2C_520nm,V_RC_590nm,V_S1C_590nm,V_S2C_590nm,V_RC_660nm,V_S1C_660nm,V_S2C_660nm,"
				+ "V_RC_880nm,V_S1C_880nm,V_S2C_880nm,V_RC_950nm,V_S1C_950nm,V_S2C_950nm,Flow1,Flow2,V_PRESSURE,V_TEMP,V_BBCR,"
				+ "V_CONT_TEMP,V_SUPPLY_TEMP,V_STATUS,V_CONT_STATUS,V_DETECT_STATUS,V_LED_STATUS,V_VALUE_STATUS,V_LED_TEMP,"
				+ "V_SBC1_370nm,V_SBC2_370nm,V_SBC1_470nm,V_SBC2_470nm,V_SBC1_520nm,V_SBC2_520nm,V_SBC1_590nm,V_SBC2_590nm,"
				+ "V_SBC1_660nm,V_SBC2_660nm,V_SBC1_880nm,V_SBC2_880nm,V_SBC1_950nm,V_SBC2_950nm,"
				+ "V_K_370nm,V_K_470nm,V_K_520nm,V_K_590nm,V_K_660nm,V_K_880nm,V_K_950nm,V_TAPE_ADV_COUNT,D_SOURCE_ID)";

		sql += "values(?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?," + "?,?,?," + "?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,?,?," + "?,?,?,?,?,?,?,?,?)";
		if (connection != null) {
			try {
				prestmt = new LoggableStatement(connection, sql);
				if (StartConfig.getDatabaseType() == 1) {
					prestmt.execute("select last_txc_xid()");
				}
				connection.setAutoCommit(false);
				List<String> sqls = new ArrayList<>();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String info = null;
				for (int i = 0; i < cawnAAPs.size(); i++) {
					cawnAAP cawnAAP = cawnAAPs.get(i);
					StatDi di = new StatDi();
					di.setFILE_NAME_O(filepath);
					di.setDATA_TYPE(sod_code);
					di.setDATA_TYPE_1(cts_code);
					di.setTT("黑碳");
					di.setTRAN_TIME(TimeUtil.date2String(recv_time, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_START_TIME(TimeUtil.getSysTime());
					di.setFILE_NAME_N(filepath);
					di.setBUSINESS_STATE("1"); // 1成功，0失败
					di.setPROCESS_STATE("1"); // 1成功，0失败

					String station = cawnAAP.getV01301();
					Date date = cawnAAP.getObservationTime();
					info = (String) proMap.get(station + "+16");
					double latitude = 999999;
					double longtitude = 999999;
					double height = 999999;
					double adminCode = 999999;
					if (info == null) {
						loggerBuffer.append("\n In configuration file, this station does not exist: " + station);
					} else {
						String[] infos = info.split(",");
						if (infos[1].equals("null"))
							loggerBuffer.append("\n In configuration file, longtitude is null!");
						else
							longtitude = Double.parseDouble(infos[1]);
						if (infos[2].equals("null"))
							loggerBuffer.append("\n In configuration file, latitude is null!");
						else
							latitude = Double.parseDouble(infos[2]);
						if (infos[3].equals("null"))
							loggerBuffer.append("\n In configuration file, station height is null!");
						else
							height = Double.parseDouble(infos[3]);
						if (infos[5].equals("null"))
							loggerBuffer.append("\n In configuration file, V_CCCC is null!");
						else
							adminCode = Integer.parseInt(infos[5]);
					}

					int ii = 1;
					prestmt.setString(ii++, sdf.format(date) + "_" + station + "_" + cawnAAP.getItemCode());
					prestmt.setString(ii++, sod_code);
					prestmt.setTimestamp(ii++, new Timestamp(date.getTime()));// 资料时间
					prestmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));// 入库时间
					prestmt.setTimestamp(ii++, new Timestamp(recv_time.getTime()));// 收到时间
					prestmt.setTimestamp(ii++, new Timestamp(new Date().getTime()));// 更新时间

					// + "V01301,V01300,V_BBB,V04001,V04002,V04003,V04004,V04005,V05001,V06001,"
					prestmt.setString(ii++, station);
					prestmt.setInt(ii++, Integer.parseInt(StationCodeUtil.stringToAscii(station)));
					prestmt.setString(ii++, "000");
					prestmt.setInt(ii++, date.getYear() + 1900);
					prestmt.setInt(ii++, date.getMonth() + 1);
					prestmt.setInt(ii++, date.getDate());
					prestmt.setInt(ii++, date.getHours());
					prestmt.setInt(ii++, date.getMinutes());
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(latitude)));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(longtitude)));
					// +
					// "V07001,V_ACODE,V_ITEM_CODE,V70012,V_370nmBC,V_470nmBC,V_520nmBC,V_590nmBC,V_660nmBC,V_880nmBC,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(height)));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(adminCode)));
					prestmt.setInt(ii++, cawnAAP.getItemCode());
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFlow())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getConcentration370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getConcentration470())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getConcentration520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getConcentration590())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getConcentration660())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getConcentration880())));
					// +
					// "V_950nmBC,V_SZ_370nm,V_SB_370nm,V_RZ_370nm,V_RB_370nm,V_Fri_370nm,V_Attn_370nm,V_SZ_470nm,V_SB_470nm,V_RZ_470nm,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getConcentration950())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneZeroPointSignal370())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneMeasureSignal370())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getReferZoneZeroPointSignal370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferZoneMeasureSignal370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSplitRatio370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getLightLossRate370())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneZeroPointSignal470())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneMeasureSignal470())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getReferZoneZeroPointSignal470())));
					// +
					// "V_RB_470nm,V_Fri_470nm,V_Attn_470nm,V_SZ_520nm,V_SB_520nm,V_RZ_520nm,V_RB_520nm,V_Fri_520nm,V_Attn_520nm,V_SZ_590nm,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferZoneMeasureSignal470())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSplitRatio470())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getLightLossRate470())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneZeroPointSignal520())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneMeasureSignal520())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getReferZoneZeroPointSignal520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferZoneMeasureSignal520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSplitRatio520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getLightLossRate520())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneZeroPointSignal590())));
					// +
					// "V_SB_590nm,V_RZ_590nm,V_RB_590nm,V_Fri_590nm,V_Attn_590nm,V_SZ_660nm,V_SB_660nm,V_RZ_660nm,V_RB_660nm,V_Fri_660nm,"
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneMeasureSignal590())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getReferZoneZeroPointSignal590())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferZoneMeasureSignal590())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSplitRatio590())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getLightLossRate590())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneZeroPointSignal660())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneMeasureSignal660())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getReferZoneZeroPointSignal660())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferZoneMeasureSignal660())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSplitRatio660())));
					// +
					// "V_Attn_660nm,V_SZ_880nm,V_SB_880nm,V_RZ_880nm,V_RB_880nm,V_Fri_880nm,V_Attn_880nm,V_SZ_950nm,V_SB_950nm,V_RZ_950nm,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getLightLossRate660())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneZeroPointSignal880())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneMeasureSignal880())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getReferZoneZeroPointSignal880())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferZoneMeasureSignal880())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSplitRatio880())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getLightLossRate880())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneZeroPointSignal950())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getSampleZoneMeasureSignal950())));
					prestmt.setBigDecimal(ii++,
							new BigDecimal(String.valueOf(cawnAAP.getReferZoneZeroPointSignal950())));
					// + "V_RB_950nm,V_Fri_950nm,V_Attn_950nm)";
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferZoneMeasureSignal950())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSplitRatio950())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getLightLossRate950())));

					// +
					// "V_TIME_BASE,V_RC_370nm,V_S1C_370nm,V_S2C_370nm,V_RC_470nm,V_S1C_470nm,V_S2C_470nm,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getTimebase())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferenceSignal370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSampleSignal370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSampleSignal370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferenceSignal470())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSampleSignal470())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSampleSignal470())));
					// +"V_RC_520nm,V_S1C_520nm,V_S2C_520nm,V_RC_590nm,V_S1C_590nm,V_S2C_590nm,V_RC_660nm,V_S1C_660nm,V_S2C_660nm,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferenceSignal520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSampleSignal520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSampleSignal520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferenceSignal590())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSampleSignal590())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSampleSignal590())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferenceSignal660())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSampleSignal660())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSampleSignal660())));
					// +"V_RC_880nm,V_S1C_880nm,V_S2C_880nm,V_RC_950nm,V_S1C_950nm,V_S2C_950nm,Flow1,Flow2,V_PRESSURE,V_TEMP,V_BBCR,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferenceSignal880())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSampleSignal880())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSampleSignal880())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getReferenceSignal950())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSampleSignal950())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSampleSignal950())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFlow1())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFlow2())));

					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getPressure())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getTemperature())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getBB())));
					// +"V_CONT_TEMP,V_SUPPLY_TEMP,V_STATUS,V_CONT_STATUS,V_DETECT_STATUS,V_LED_STATUS,V_VALUE_STATUS,V_LED_TEMP,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getContTemp())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSupplyTemp())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getStatus())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getContStatus())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getDetectStatus())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getLedStatus())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getValveStatus())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getLedTemp())));
					// +"V_SBC1_370nm,V_SBC2_370nm,V_SBC1_470nm,V_SBC2_470nm,V_SBC1_520nm,V_SBC2_520nm,V_SBC1_590nm,V_SBC2_590nm,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSanmpleCon370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSanmpleCon370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSanmpleCon470())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSanmpleCon470())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSanmpleCon520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSanmpleCon520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSanmpleCon590())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSanmpleCon590())));
					// +"V_SBC1_660nm,V_SBC2_660nm,V_SBC1_880nm,V_SBC2_880nm,V_SBC1_950nm,V_SBC2_950nm,"
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSanmpleCon660())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSanmpleCon660())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSanmpleCon880())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSanmpleCon880())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getFirstSanmpleCon950())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getSecondSanmpleCon950())));
					// +
					// "V_K_370nm,V_K_470nm,V_K_520nm,V_K_590nm,V_K_660nm,V_K_880nm,V_K_950nm,V_TAPE_ADV_COUNT,D_SOURCE_ID)";
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getCompCoeffi370())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getCompCoeffi470())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getCompCoeffi520())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getCompCoeffi590())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getCompCoeffi660())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getCompCoeffi880())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getCompCoeffi950())));
					prestmt.setBigDecimal(ii++, new BigDecimal(String.valueOf(cawnAAP.getTapeAdvCount())));
					prestmt.setString(ii++, cts_code);

					di.setIIiii(station);
					di.setLONGTITUDE(String.valueOf(longtitude));
					di.setLATITUDE(String.valueOf(latitude));
					di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
					di.setPROCESS_END_TIME(TimeUtil.getSysTime());
					di.setRECORD_TIME(TimeUtil.getSysTime());
					
					di.setSEND("BFDB");
					di.setSEND_PHYS("DRDS");
					di.setFILE_SIZE(String.valueOf(new File(filepath).length()));
					di.setDATA_UPDATE_FLAG("000");
					di.setHEIGHT(String.valueOf(height));

					prestmt.addBatch();
					sqls.add(((LoggableStatement) prestmt).getQueryString()); // 批量入库冲突时，单条入库
					listDi.add(di);
				}
				try {
					prestmt.executeBatch();
					connection.commit();
					sqls.clear();
					System.out.println("批量插入语句提交成功！");
					loggerBuffer.append("\r\n " + "批量插入语句提交成功！");
				} catch (SQLException e) {
					prestmt.clearParameters();
					prestmt.clearBatch();
					execute_sql(sqls, connection, filepath, loggerBuffer); // 此中，入库失败的会将对应的DI的ProcessState置为1 //
																			// 1:失败，0：成功
					loggerBuffer.append("\n Batch commit failed: " + filepath);
				}
			} catch (SQLException e) {
				loggerBuffer.append("\n Create Statement failed: " + e.getMessage());
			} finally {
				if (prestmt != null) {
					try {
						prestmt.close();
					} catch (SQLException e) {
						loggerBuffer.append("\n Close Statement failed: " + e.getMessage());
					}
				}
			}
		}
	}

	/**
	 * @param loggerBuffer @param fileN
	 * 
	 * @Title: execute_sql @Description:(批量入库失败时，采用逐条提交) @param: @param
	 * sqls @param: @param connection @return: void @throws
	 */
	private static void execute_sql(List<String> sqls, java.sql.Connection connection, String fileN,
			StringBuffer loggerBuffer) {
		Statement pStatement = null;
		try {
			connection.setAutoCommit(true);
			pStatement = connection.createStatement();
			for (int i = 0; i < sqls.size(); i++) {
//				pStatement = new LoggableStatement(connection, sqls.get(i));
				try {
					pStatement.execute(sqls.get(i));
					connection.commit();
					loggerBuffer.append("\r\n " + "	单条插入语句提交成功！");
				} catch (Exception e) {
					loggerBuffer.append("\n File name：" + fileN + "\n " + listDi.get(i).getIIiii() + " "
							+ listDi.get(i).getDATA_TIME() + "\n execute sql error: " + sqls.get(i) + "\n "
							+ e.getMessage());
					listDi.get(i).setPROCESS_STATE("0");
				}
			}
		} catch (SQLException e) {
			loggerBuffer.append("\n Create Statement error: " + e.getMessage());
		} finally {
			if (pStatement != null) {
				try {
					pStatement.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n Close Statement error: " + e.getMessage());
				}
			}
		}

	}
}