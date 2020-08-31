package cma.cimiss2.dpc.indb.surf.dc_surf_hyd.service;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.HdsqBean;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.core.tools.TimeUtil;

/**
 * ************************************
 * @ClassName: OTSService
 * @Auther: fengmingyang
 * @Date: 2019/4/3 10:47
 * @Description: 水利部河道水情资料解析--OTS入库
 * @Copyright: All rights reserver.
 * ************************************
 */

public class OTSService {

    public static BlockingQueue<StatDi> diQueues;
    private static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    private static final String TABLE_NAME = StartConfig.valueTable();

    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
    	OTSService.diQueues = diQueues;
    }
    public static BlockingQueue<StatDi> getDiQueues(){
    	return diQueues;
    }
    /**
     * ots入库
     * @param parseResult
     * @param filePath
     * @return DataBaseAction
     */
    public static DataBaseAction processSuccessReport(ParseResult<HdsqBean> parseResult, String filePath) {
        List<HdsqBean> hdsqBeans = parseResult.getData();
        int successCount = 0;
        try {
            successCount = insertValueTable(hdsqBeans,filePath);
        }catch (Exception e) {
            e.printStackTrace();
            infoLogger.error("数据库连接失败！", e);
            return DataBaseAction.CONNECTION_ERROR;
        }
        if(successCount > 0 ) {
            return DataBaseAction.SUCCESS;// 至少一条成功,返回SUCCESS
        }else {
            return DataBaseAction.INSERT_ERROR;
        }
    }

    /**
     * 要素表入库
     * @param hdsqBeans
     * @param filePath
     * @return OTSBatchResult
     * @throws ParseException 
     */
    private static int insertValueTable(List<HdsqBean> hdsqBeans,String filePath) throws ParseException {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
        File file = new File(filePath);
        String fileName = file.getName();
        String ctsCode = StartConfig.ctsCode();
        String sodCode = StartConfig.sodCode();
        Date date = new Date();
        int successCount = 0;
        for (HdsqBean hdsqBean : hdsqBeans) {
            //获取站点经纬度信息
            String longtitude =
                    getStationInfoFromConfig(hdsqBean.getStationNumber(), "20", "longtitude");// 经度
            String latitude =
                    getStationInfoFromConfig(hdsqBean.getStationNumber(), "20", "latitude");// 纬度
            //记录标识 时间+ 经纬度 + 区站号
            String recId =  sdf2.format(sdf.parse(hdsqBean.getObsTime()))+ "_" + longtitude + "_" + latitude + "_"
                    + hdsqBean.getStationNumber();
            Map<String, Object> row = new HashMap<>();
            row.put("D_RECORD_ID", recId);// 记录标识
            row.put("D_DATA_ID", sodCode);// 资料标识
            
            row.put("D_IYMDHM", sdf.format(date));// 入库时间
            row.put("D_RYMDHM", sdf.format(new Date(file.lastModified())));// 收到时间
            row.put("D_UPDATE_TIME", sdf.format(date));// 更新时间
            row.put("D_DATETIME", hdsqBean.getObsTime());// 资料时间
            
            row.put("V_CYMD", hdsqBean.getFileInfo());// 文件年月日
            row.put("V01301", hdsqBean.getStationNumber());// 区站号（字符）
            row.put("V01300", getNumberTypeStationNumber(hdsqBean.getStationNumber()));// 区站号（数字）
            row.put("V05001", latitude);// 纬度
            row.put("V06001", longtitude);// 经度
            row.put("V07001", 999999);// 测站高度
            row.put("V02001", "+20");// 测站类型
            row.put("V02301", 999999);// 台站级别
            row.put("V_ACODE", 999999);// 中国行政区划代码
            String obsTime = hdsqBean.getObsTime();
            if(obsTime.length() > 13) {
                row.put("V04001", Integer.valueOf("".equals(obsTime) ? "999999" : obsTime.substring(0, 4)));// 年
                row.put("V04002", Integer.valueOf("".equals(obsTime) ? "999999" : obsTime.substring(5, 7)));// 月
                row.put("V04003", Integer.valueOf("".equals(obsTime) ? "999999" : obsTime.substring(8, 10)));// 日
                row.put("V04004", Integer.valueOf("".equals(obsTime) ? "999999" : obsTime.substring(11, 13)));// 时
            }else {
                row.put("V04001", "999999");// 年
                row.put("V04002", "999999");// 月
                row.put("V04003", "999999");// 日
                row.put("V04004", "999999");// 时
            }
            row.put("V70011", hdsqBean.getWaterLevel());// 水位（或闸上水位）
            row.put("V70012", hdsqBean.getTraffic());// 流量
            row.put("V70013", hdsqBean.getChaoJing());// 超警戒水位
            row.put("V70017", getShuishiCode(hdsqBean.getShuiShi()));// 水势（或闸上水势）
            row.put("V70014", hdsqBean.getInflow());// 入流
            row.put("V70015", hdsqBean.getOutflow());// 出流
            row.put("V70016", hdsqBean.getPondage());// 蓄水量
            row.put("D_SOURCE_ID", ctsCode);// CTS编码
            StatDi di = new StatDi();
            di.setFILE_NAME_O(fileName);
            di.setDATA_TYPE(sodCode);
            di.setDATA_TYPE_1(ctsCode);
            di.setTT("水利部河道水情");
            di.setTRAN_TIME(hdsqBean.getObsTime());
            di.setPROCESS_START_TIME(TimeUtil.getSysTime());
            di.setFILE_NAME_N(fileName);
            di.setBUSINESS_STATE("0"); // 0成功，1失败
            di.setPROCESS_STATE("0"); // 0成功，1失败
            // 补全di
            di.setIIiii(hdsqBean.getStationNumber());
            di.setLONGTITUDE(longtitude);
            di.setLATITUDE(latitude);
            di.setDATA_TIME(hdsqBean.getObsTime());
            di.setPROCESS_END_TIME(TimeUtil.getSysTime());
            di.setRECORD_TIME(TimeUtil.getSysTime());
            try {
                OTSDbHelper.getInstance().insert(TABLE_NAME, row);
                successCount++;
            }catch (Exception e) {
                di.setBUSINESS_STATE("1");// 0成功，1失败
                di.setPROCESS_STATE("1"); // 0成功，1失败
            }
            diQueues.offer(di);
        }
        infoLogger.info(" INSERT SUCCESS FINISH TIME : " + sdf.format(date) + "\n");
        infoLogger.info(" INSERT TOTAL COUNT : " + hdsqBeans.size() + "\n");
        infoLogger.info(" INSERT SUCCESSED COUNT : " + successCount + "\n");
        infoLogger.info(" INSERT FAILED CONTENT : " + (hdsqBeans.size() - successCount) + "\n");
        return successCount;
    }
    
    /**
     * 获取水情码
     * @param shuishiValue
     * @return
     */
    private static int getShuishiCode(String shuishiValue) {
        int code = 999999;
        switch (shuishiValue) {
            case "平":
                code = 0;
                break;
            case "涨":
                code = 1;
                break;
            case "落":
                code = 2;
                break;
            case "缺测":
                code = 9;
                break;
            default:
                break;
        }
        return code;
    }

    /**
     * 获取经纬度信息
     * @param stationNumberChina
     * @param stationTypeNo
     * @param key
     * @return
     */
    private static String getStationInfoFromConfig(String stationNumberChina, String stationTypeNo,
            String key) {
        String resValue = "999999";
        try {
            Map<String, Object> proMap = StationInfo.getProMap();
            String info = (String) proMap.get(stationNumberChina + "+" + stationTypeNo);
            String[] infos = info.split(",");
            if (infos.length < 10) {
                return resValue;
            } else {
                key = key.toLowerCase();
                switch (key) {
                    case "longtitude":
                        resValue = "null".equals(infos[1]) ? "999999" : infos[1];
                        break;
                    case "latitude":
                        resValue = "null".equals(infos[2]) ? "999999" : infos[2];
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            infoLogger.error("获取StationInfo_Config.lua 配置信息失败", e);
        }
        return resValue;
    }
    /**
     * 获取数字类型区站号 区站号中存在字母，将字母进行ascii码转换
     * @param stationNumber
     * @return
     */
    private static String getNumberTypeStationNumber(String stationNumber) {
        String number = "999999";
        try {
            StringBuffer sb = new StringBuffer();
            char[] chars = stationNumber.toCharArray();
            for (char c : chars) {
                if (isLetter(c)) {
                    sb.append((int) c);
                } else {
                    sb.append(c);
                }
            }
            number = sb.toString();
        } catch (Exception e) {
            infoLogger.error("区站号不符合规则->", e);
        }
        return number;
    }

    /**
     * 字符是否为字母
     * @param c
     * @return
     */
    private static boolean isLetter(char c) {
        int i = (int) c;
        if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
            return true;
        }
        return false;
    }
}
