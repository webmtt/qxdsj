package cma.cimiss2.dpc.indb.agme.dc_agme_manl_grass.service;
import java.io.File;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.CTSCodeMap;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_04;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_05;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_06;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_07;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_08;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_09;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_10;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.agme.ReportInfoService;


public class DbService {
	HashMap<String, Integer> retryMap;
	private static List<StatDi> listDi = new ArrayList<StatDi>();
	public static BlockingQueue<StatDi> diQueues;
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static double defaultF = 999999.0;
//	static Map<String, Object> proMap = StationInfo.getProMap();
	
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues){
		DbService.diQueues = diQueues;
	}
	
	/**
	 * @Title: processSuccessReport 
	 * @Description:(解码成功的报文段入库操作)
	 * @param  parseResult 解码结果集
	 * @param recv_time 文件全路径
	 * @param fileN2 消息接收时间 
	 * @param loggerBuffer 
	 * @param packagePath 
	 * @param fileN 
	 * @param v_bbb
	 * @return返回值说明 @throws
	 * 
	 */
	public static DataBaseAction processSuccessReport(ParseResult<Agme_Grass> parseResult, Date recv_time, String v_bbb, String filepath, boolean isRevised, String packagePath, StringBuffer loggerBuffer, List<CTSCodeMap> ctsCodeMaps) {
		java.sql.Connection connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
		File file = new File(filepath);
		String fileN = file.getName();
		List<Agme_Grass> agmeGrassS = parseResult.getData();
		try {
			for (String cropType : agmeGrassS.get(0).grassTypes) {
				switch (cropType) {
				case "GRASS-01":
					List<Agme_Grass_01> agme_Grass_01s = agmeGrassS.get(0).getAgmeGrass_01();
					if (isRevised)
						EleUpdateGrass.updateGRASS01(agme_Grass_01s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer, ctsCodeMaps.get(0));
					else
						Insert_db_Grass.insert_db_GRASS01(agme_Grass_01s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(0));
					break;
				case "GRASS-02":
					List<Agme_Grass_02> agme_Grass_02s = agmeGrassS.get(0).getAgmeGrass_02();
					if (isRevised)
						EleUpdateGrass.updateGRASS02(agme_Grass_02s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer, ctsCodeMaps.get(1));
					else
						Insert_db_Grass.insert_db_GRASS02(agme_Grass_02s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(1));
					break;
				case "GRASS-03":
					List<Agme_Grass_03> agme_Grass_03s = agmeGrassS.get(0).getAgmeGrass_03();
					if (isRevised)
						EleUpdateGrass.updateGRASS03(agme_Grass_03s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer, ctsCodeMaps.get(2));
					else
						Insert_db_Grass.insert_db_GRASS03(agme_Grass_03s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(2));
					break;
				case "GRASS-04":
					List<Agme_Grass_04> agme_Grass_04s = agmeGrassS.get(0).getAgmeGrass_04();
					if (isRevised)
						EleUpdateGrass.updateGRASS04(agme_Grass_04s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer, ctsCodeMaps.get(3));
					else
						Insert_db_Grass.insert_db_GRASS04(agme_Grass_04s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(3));
					break;
				case "GRASS-05":
					List<Agme_Grass_05> agme_Grass_05s = agmeGrassS.get(0).getAgmeGrass_05();
					if (isRevised)
						EleUpdateGrass.updateGRASS05(agme_Grass_05s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer, ctsCodeMaps.get(4));
					else
						Insert_db_Grass.insert_db_GRASS05(agme_Grass_05s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(4));
					break;
				case "GRASS-06":
					List<Agme_Grass_06> agme_Grass_06s = agmeGrassS.get(0).getAgmeGrass_06();
					if (isRevised)
						EleUpdateGrass.updateGRASS06(agme_Grass_06s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer, ctsCodeMaps.get(5));
					else
						Insert_db_Grass.insert_db_GRASS06(agme_Grass_06s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(5));
					break;
				case "GRASS-07":
					List<Agme_Grass_07> agme_Grass_07s = agmeGrassS.get(0).getAgmeGrass_07();
					if (isRevised)
						EleUpdateGrass.updateGRASS07(agme_Grass_07s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer, ctsCodeMaps.get(6));
					else
						Insert_db_Grass.insert_db_GRASS07(agme_Grass_07s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(6));
					break;
				case "GRASS-08":
					List<Agme_Grass_08> agme_Grass_08s = agmeGrassS.get(0).getAgmeGrass_08();
					if (isRevised)
						EleUpdateGrass.updateGRASS08(agme_Grass_08s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer,ctsCodeMaps.get(7));
					else
						Insert_db_Grass.insert_db_GRASS08(agme_Grass_08s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(7));
					break;
				case "GRASS-09":
					List<Agme_Grass_09> agme_Grass_09s = agmeGrassS.get(0).getAgmeGrass_09();
					if (isRevised)
						EleUpdateGrass.updateGRASS09(agme_Grass_09s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer, ctsCodeMaps.get(8));
					else
						Insert_db_Grass.insert_db_GRASS09(agme_Grass_09s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(8));
					break;
				case "GRASS-10":
					List<Agme_Grass_10> agme_Grass_10s = agmeGrassS.get(0).getAgmeGrass_10();
					if (isRevised)
						EleUpdateGrass.updateGRASS10(agme_Grass_10s, connection, recv_time, listDi, filepath, v_bbb,loggerBuffer, ctsCodeMaps.get(9));
					else
						Insert_db_Grass.insert_db_GRASS10(agme_Grass_10s, connection, recv_time, listDi, filepath,loggerBuffer, ctsCodeMaps.get(9));
					break;
				default:
					break;
				}
			}
			
			// 日志信息插入数据库
			try {
				List<ReportInfo> reportInfos = parseResult.getReports();
				String[] fnames = fileN.split("_");
				java.sql.Connection report_connection = ConnectionPoolFactory.getInstance().getConnection("cimiss");
				ReportInfoService.reportInfoToDb(reportInfos, report_connection, v_bbb, recv_time, fnames[3], fnames[1],ctsCodeMaps, filepath, listDi);
			} catch (Exception e) {
				loggerBuffer.append("\n " + loggerBuffer + "Log information insert database table  error：" + e.getMessage());
			}
			for (int j = 0; j < listDi.size(); j++) {
				diQueues.offer(listDi.get(j));
			}
			listDi.clear();
			
		} catch (Exception e) {
			loggerBuffer.append("\n  " + loggerBuffer + "Database connection close error：" + e.getMessage());
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					loggerBuffer.append("\n  " + loggerBuffer + "Database connection finally close error：" + e.getMessage());
				}
			}
		}
		return DataBaseAction.SUCCESS;
	}

}
