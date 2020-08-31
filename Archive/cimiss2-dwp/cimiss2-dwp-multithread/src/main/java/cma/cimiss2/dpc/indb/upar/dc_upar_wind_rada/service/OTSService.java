package cma.cimiss2.dpc.indb.upar.dc_upar_wind_rada.service;

import cma.cimiss2.dpc.decoder.StationData;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.WindData;
import cma.cimiss2.dpc.decoder.bean.upar.WindRada;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.DataBaseAction;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSBatchResult;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.BlockingQueue;

import static org.cimiss2.dwp.tools.utils.QCUtil.getTStation;
import static org.cimiss2.dwp.tools.utils.QCUtil.getWSpeed;
import static org.cimiss2.dwp.tools.utils.QCUtil.isExist;

/**
 * ************************************
 * @ClassName: OTSService
 * @Auther: dangjinhu
 * @Date: 2019/3/27 10:47
 * @Description: 风廓线雷达数据ots入库
 * @Copyright: All rights reserver.
 * ************************************
 */

public class OTSService {
    private static List<StatDi> listDi = new ArrayList<StatDi>();
    public static BlockingQueue<StatDi> diQueues;
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    
    
    public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	/**
     * ots入库
     * @param pa
     * @param rece_time
     * @param filePath
     * @param log
     * @return
     */
    public static DataBaseAction processSuccessReport(ParseResult<WindRada> pa, Date rece_time, String filePath, StringBuffer log) {
        String tableName = StartConfig.valueTable();
        File file = new File(filePath);
        String fileN = file.getName();
        List<WindRada> windRadas = pa.getData();
        List<Boolean> list = new ArrayList<>();
        // 读取WinRada记录
        for (int i = 0; i < windRadas.size(); i++) {
            WindRada windRada = windRadas.get(i);
            try {
                boolean value = insertTable(fileN, log, tableName, windRada, rece_time);
                list.add(value);
            } catch (Exception e) {
                log.append("第" + i + "个站点").append("插入异常").append(e.getMessage());
            }
        }
        long count = list.stream().filter(t -> t).count();
        if (count >= 1) { // 至少一个站点成功,返回
            return DataBaseAction.SUCCESS;
        } else {
            return DataBaseAction.CONNECTION_ERROR;
        }
    }

    /**
     * 入库
     * @param fileN
     * @param log
     * @param tableName
     * @param windRada
     */
    private static boolean insertTable(String fileN,StringBuffer log, String tableName, WindRada windRada,Date rece_time) {
        // 质控步骤
        // 1：根据风廓线雷达站号查找探空站号
        String wStation = windRada.getStaionId();
        // 探空站点参数对象
        StationData tStation = null;
        boolean exist = isExist(wStation,log);
        if (exist) {
            tStation = getTStation();
        } else {
            // 不存在,记录区站号
            log.append("\nNo corresponding sounding station was found").append(windRada.getStaionId());
        }
        String recId =windRada.getStaionId()+ "_" + windRada.getObeTime() + "_" + windRada.get_OBS() + "_";
        List<WindData> windData = windRada.getwData();
        String[] ymdhm = TimeUtil.getYmdhm(windRada.getObeTime());
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 0; i < windData.size(); i++) {
            WindData data = windData.get(i);
            Map<String, Object> row = new HashMap<>();
            // di
            StatDi di = new StatDi();
            di.setFILE_NAME_O(fileN);
            di.setDATA_TYPE(getDataID(windRada.get_OBS()));
            di.setDATA_TYPE_1(getDataID(windRada.get_OBS()));
            di.setTT("风廓线雷达");
            di.setTRAN_TIME(TimeUtil.date2String(rece_time, "yyyy-MM-dd HH:mm:ss"));
            di.setPROCESS_START_TIME(TimeUtil.getSysTime());
            di.setFILE_NAME_N(fileN);
            di.setBUSINESS_STATE("0"); //0成功，1失败
            di.setPROCESS_STATE("0");  //0成功，1失败
            // 写入值
            row.put("D_RECORD_ID", recId + data.getHeight());// 记录标识
            row.put("D_IYMDHM", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));// 入库时间
            row.put("D_RYMDHM", TimeUtil.date2String(rece_time,"yyyy-MM-dd HH:mm:ss"));// 收到时间
            row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss"));// 更新时间
            row.put("D_DATETIME", TimeUtil.date2String(TimeUtil.String2Date(windRada.getObeTime(),"yyyyMMddHHmmss"),"yyyy-MM-dd HH:mm:ss"));// 观测时间
            row.put("D_DATA_ID", getDataID(windRada.get_OBS()));// 资料标识
            row.put("V01301", windRada.getStaionId());// WMO区站号
            row.put("V_RADAR_MODEL", windRada.getRadaModel());// 风廓线雷达型号
            row.put("V01398", windRada.get_OBS());// 产品类型
            row.put("V04001", ymdhm[0]);// 年
            row.put("V04002", ymdhm[1]);// 月
            row.put("V04003", ymdhm[2]);// 日
            row.put("V04004", ymdhm[3]);// 时
            row.put("V04005", ymdhm[4]);// 分
            row.put("V04006", ymdhm[5]);// 秒
            row.put("V05001", windRada.getLatitude());// 纬度
            row.put("V06001", windRada.getLongitude());// 经度
            row.put("V07001", windRada.getAltitude());// 测站高度
            row.put("V07031", 999998);// 气压传感器高度
            row.put("V07002", data.getHeight());// 采样高度
            row.put("V11001", data.getWindDirection());// 水平风向
            row.put("V11002", data.getWindSpeed());// 水平风速
            row.put("V11006", data.getVwindSpeed());// 垂直风速
            row.put("V73107", data.getHcredibility());// 水平方向可信度
            row.put("V73108", data.getVcredibility());// 垂直方向可信度
            row.put("V25091", data.getCn2());// 折射率结构常数Cn2
            // 风向质控
            int windDQC = DQC(data.getWindDirection());
            row.put("Q11001", windDQC);// 水平风向质控码
            // 风速质控
            int windSQC;
            // 高度为采样高度+测站海拔高度
            int hight = data.getHeight() + (int) windRada.getAltitude();
            if (exist) {
                windSQC = SQC(hight, data.getWindSpeed(), tStation, windRada);
            } else {
                windSQC = SQC(hight, data.getWindSpeed());
            }
            row.put("Q11002", windSQC);// 水平风速质控码
            row.put("Q11006", 9);// 垂直风速质控码
            row.put("V_RETAIN1_C", 999999);// 保留字段1
            row.put("V_RETAIN2_C", 999999);// 保留字段2
            row.put("V_RETAIN3_C", 999999);// 保留字段3
            row.put("V_RETAIN4_C", 999999);// 保留字段4
            row.put("V_RETAIN5_C", 999999);// 保留字段5
            row.put("V_RETAIN6_C", 999999);// 保留字段6
            row.put("V_RETAIN7_C", 999999);// 保留字段7
            row.put("V_RETAIN8_C", 999999);// 保留字段8
            row.put("V_RETAIN9_C", 999999);// 保留字段9
            row.put("V_RETAIN10_F", 999999);// 保留字段10
            row.put("V_RETAIN11_F", 999999);// 保留字段11
            row.put("V_RETAIN12_F", 999999);// 保留字段12
            row.put("V_RETAIN13_F", 999999);// 保留字段13
            row.put("V_RETAIN14_F", 999999);// 保留字段14
            row.put("V_RETAIN15_F", 999999);// 保留字段15
            row.put("D_SOURCE_ID", getSourceID(windRada.get_OBS()));//资料四级编码
            
            // 补全di
            di.setIIiii(wStation);
            di.setLONGTITUDE(String.valueOf(windRada.getLongitude()));
            di.setLATITUDE(String.valueOf(windRada.getLatitude()));
            di.setDATA_TIME(TimeUtil.date2String(TimeUtil.String2Date(windRada.getObeTime(), "yyyyMMddHHmmss"), "yyyy-MM-dd HH:mm:ss"));
            di.setPROCESS_END_TIME(TimeUtil.getSysTime());
            di.setRECORD_TIME(TimeUtil.getSysTime());
            diQueues.offer(di);
            rows.add(row);
        }
        try {
            OTSBatchResult result = OTSDbHelper.getInstance().insert(tableName, rows);
            log.append(" INSERT SUCCESS FINISH TIME : " + TimeUtil.date2String(new Date(),"yyyy-MM-dd HH:mm:ss") + "\n");
            log.append(" INSERT SUCCESS COUNT : " + result.getSuccessRowCount() + "\n");
            log.append(" INSERT FAILED COUNT : " + result.getFailedRowCount() + "\n");
            log.append(" INSERT FAILED CONTENT : " + result.getFailedRows() + "\n");
            return result.getSuccessRowCount() > 0;
        } catch (Exception e) {
            log.append("ots insert error:").append(e.getMessage());
            return false;
        }
    }

    /**
     * 获取资料标识
     * @param obs
     * @return
     */
    private static String getDataID(String obs) {
        String d_data_id = null;//_OBS(包括R,H,O)
        switch (obs.charAt(0)) {
            case 'R':
                d_data_id = "B.0007.0003.S001";
                break;
            case 'H':
                d_data_id = "B.0007.0004.S001";
                break;
            case 'O':
                d_data_id = "B.0007.0005.S001";
                break;
        }
        return d_data_id;
    }
    /**
     * 获取资料四级编码
     * @param obs
     * @return
     */
    private static String getSourceID(String obs) {
        String d_source_id = null;//_OBS(包括R,H,O)
        switch (obs.charAt(0)) {
            case 'R':
            	d_source_id = "B.0007.0003.R001";
                break;
            case 'H':
            	d_source_id = "B.0007.0004.R001";
                break;
            case 'O':
            	d_source_id = "B.0007.0005.R001";
                break;
        }
        return d_source_id;
    }

    /**
     * 风速质控
     * 0 正确
     * 1 可疑
     * 8 缺测
     * 2 数据错误
     * @return
     */
    private static int SQC(int h, double s) {
        if (999999 == s) {
            return 8; // 数据缺测
        }
        int[] height = new int[]{-600, 3000, 5500, 7000, 14000, 22000, 30000, 36000, 39000, 99999};
        int[] value = new int[]{0, 100, 120, 150, 180, 170, 110, 140, 170, 220};
        int c = -1;
        // 找出采样高度所在区间
        for (int i = 1; i < height.length; i++) {
            if (h > height[i - 1] && h <= height[i]) {
                c = i - 1;
                break;
            }
        }
        // 为-1,值不在范围内
        if (c == -1) {
            return 2;
        }
        // 通过区间去允许值范围查找
        if (s >= 0 && s <= value[c + 1]) {
            return 0;
        }
        return 2;
    }

    /**
     * 风速质控
     * @param height
     * @param windSpeed
     * @param tStation
     * @param windRada
     * @return
     */
    private static int SQC(int height, double windSpeed, StationData tStation, WindRada windRada) {
        // 风速质控1
        int windSQC1 = SQC(height, windSpeed);
        // 风速质控2
        if (windSQC1 == 0) {
            int wind = getWSpeed(height);
            if (windSpeed <= wind) {
                return windSQC1;
            } else {
                return 2;
            }
        }
        return windSQC1;
    }

    /**
     * 风向质控
     * 允许值范围 0 ~ 360
     * 单位：度
     * @param a
     * @return
     */
    private static int DQC(double a) {
        if (999999 == (int) a) {
            return 8; // 数据缺测
        }
        if (a >= 0 && a <= 360) {
            return 0;
        }
        return 2;
    }
}
