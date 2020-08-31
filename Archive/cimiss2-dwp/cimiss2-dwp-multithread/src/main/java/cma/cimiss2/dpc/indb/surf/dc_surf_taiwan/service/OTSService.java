package cma.cimiss2.dpc.indb.surf.dc_surf_taiwan.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.TwSiteData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import com.alicloud.openservices.tablestore.AsyncClient;
import com.alicloud.openservices.tablestore.TableStoreException;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.Config;
import org.cimiss2.dwp.tools.ots.OTSHelper;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;

import static cma.cimiss2.dpc.decoder.bean.surf.TwSiteData.*;
import static cma.cimiss2.dpc.indb.surf.dc_surf_taiwan.MainThread.HourA2Count;
import static cma.cimiss2.dpc.indb.surf.dc_surf_taiwan.MainThread.updateCount;

/**
 * ************************************
 * @ClassName: OTSService
 * @Auther: dangjinhu
 * @Date: 2019/8/8 09:54
 * @Description: ots入库
 * @Copyright: All rights reserver.
 * ************************************
 */

public class OTSService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		OTSService.diQueues = diQueues;
	}

	/**
     * ots入库
     * @param pr       ParseResult 结果集
     * @param receTime 资料接受时间
     * @param filepath 文件路径
     * @param log      日志信息
     * @return DataBaseAction
     */
    public static DataBaseAction processSuccessReport(ParseResult<TwSiteData> pr, Date receTime, String filepath, StringBuffer log) {
        String reportTable = StartConfig.reportTable();
        String keyTable = StartConfig.keyTable();
        String sodCode = StartConfig.sodCode();
        String ctsCode = StartConfig.ctsCode();
        File file = new File(filepath);
        String fileName = file.getName();
        List<TwSiteData> twSiteDatas = pr.getData();
        OTSHelper otsHelperRTable = new OTSHelper(new AsyncClient(Config.endpoint, Config.accessId, Config.accessKey, Config.instance), reportTable);
        OTSHelper otsHelperKTable = null;
        boolean flagA2 = false;
        int insert = 0, update = 0;
        int size = twSiteDatas.size();
        String config = StartConfig.stationInfoPath();
        File configPath = new File(config);
        if (!configPath.exists()) {
            if (!configPath.mkdirs()) {
                throw new RuntimeException("cannot create configPath!" + configPath);
            }
        }
        // 读取文件名日期 Z_SURF_C_RCTP_YYYYMMDDhhmmss_O_AWS_A0001.XML
        String name;
        String[] filenames = fileName.split("_");
        if (filenames.length == 8) {
            String datetime = filenames[4];
            if (datetime.length() == 14) {
                name = config + TimeUtil.date2String(TimeUtil.String2Date(datetime, "yyyyMMddHHmmss"), "yyyy-MM-dd") + ".txt";
            } else {
                infoLogger.error("this filename time format error: time length not equals 14!" + datetime);
                return DataBaseAction.INSERT_ERROR;
            }
        } else {
            infoLogger.error("this filename format error!" + fileName);
            return DataBaseAction.INSERT_ERROR;
        }
        File configFile = new File(name);
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                infoLogger.error("createNewFile exception!" + e.getMessage());
                return DataBaseAction.INSERT_ERROR;
            }
        }
        StringBuffer stationInfos = new StringBuffer();
        for (int i = 0; i < size; i++) {
            TwSiteData twSiteData = twSiteDatas.get(i);
            Map<String, Object> row = new HashMap<>();
            // di
            StatDi di = new StatDi();
            di.setFILE_NAME_O(fileName);
            di.setDATA_TYPE(sodCode);
            di.setDATA_TYPE_1(ctsCode);
            di.setTT("台湾站点资料");
            di.setTRAN_TIME(TimeUtil.date2String(receTime, "yyyy-MM-dd HH:mm:ss"));
            di.setPROCESS_START_TIME(TimeUtil.getSysTime());
            di.setFILE_NAME_N(fileName);
            di.setBUSINESS_STATE("1"); //1成功,0失败
            di.setPROCESS_STATE("1");  //1成功,0失败
            di.setIIiii(twSiteData.getStationId());
            di.setLONGTITUDE(String.valueOf(twSiteData.getLongitude()));
            di.setLATITUDE(String.valueOf(twSiteData.getLatitude()));
            di.setFILE_SIZE(String.valueOf(file.length()));
            di.setDATA_UPDATE_FLAG("000");
            di.setHEIGHT(String.valueOf(twSiteData.getWeatherElement("ELEV")));
            di.setSEND_PHYS("DRDS");
            Date date = null;
            try {
                date = TimeUtil.String2Date(twSiteData.getObsTime(), "yyyy-MM-dd HH:mm:ss");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.HOUR, -8);
                date = calendar.getTime();
            } catch (Exception e) {
                log.append("\ntime conversion exception:").append(twSiteData.getObsTime());
                continue;
            }
            stationInfos.append(twSiteData.getStationId()).append(",").
                    append(twSiteData.getLocationName()).append(",").
                    append(twSiteData.getLatitude()).append(",").
                    append(twSiteData.getLongitude()).append(",").
                    append(twSiteData.getWeatherElement("ELEV")).append(",").
                    append(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss")).append(System.lineSeparator());
            // 记录标识
            String recordId = TimeUtil.date2String(date, "yyyyMMddHHmm") + "_" + twSiteData.getStationId();
            row.put("D_RECORD_ID", recordId);
            try {
                // 先判断数据是否是A0002资料
                if (twSiteData.getResourceId().equals("O-A0002-001")) {
                    // 入库keyTable
                    otsHelperKTable = new OTSHelper(new AsyncClient(Config.endpoint, Config.accessId, Config.accessKey, Config.instance), keyTable);
                    Map<String, Object> rowKey = new HashMap<>();
                    rowKey.put("D_RECORD_ID", recordId);
                    Map<String, Object> query = otsHelperKTable.query(rowKey);
                    if (query.size() == 0) {
                        // 不存在
                        setKeyTableValue(twSiteData, rowKey, receTime, date, sodCode, ctsCode);
                        try {
                            otsHelperKTable.insert(rowKey, true);
                            insert++;
                        } catch (InterruptedException | ExecutionException e) {
                            log.append("\ninsert map exception!").append(e.getMessage());
                        }
                    } else {
                        // 分钟数据已存在(若资料数据异常,可能存在这种情况)
                        query.put("V07001", twSiteData.getWeatherElement("ELEV"));
                        query.put("V13392_010", twSiteData.getWeatherElement("MIN_10"));
                        try {
                            otsHelperKTable.update(query, true);
                            update++;
                            log.append("\nupdate map success!").append("stationId is ").append(twSiteData.getStationId());
                        } catch (TableStoreException | ExecutionException | InterruptedException e) {
                            log.append("\nupdate map exception!").append("stationId is ").append(twSiteData.getStationId());
                        }
                    }
                    // A0002降雨整小时资料同时入reportTable表
                    if (TimeUtil.getMinute(date) == 0) {
                        flagA2 = true;
                        Map<String, Object> queryA2 = otsHelperRTable.query(row);
                        if (queryA2.size() == 0) {
                            setReportTableValue(twSiteData, row, receTime, date, sodCode, ctsCode);
                            try {
                                otsHelperRTable.insert(row, false);
                                insert++;
                            } catch (InterruptedException | ExecutionException e) {
                                log.append("\ninsert map exception!").append(e.getMessage());
                            }
                        } else {
                            // 数据已存在,修改观测字段
                            queryA2.put("V13019", twSiteData.getWeatherElement("RAIN"));
                            queryA2.put("V13020", twSiteData.getWeatherElement("HOUR_3"));
                            queryA2.put("V13021", twSiteData.getWeatherElement("HOUR_6"));
                            queryA2.put("V13022", twSiteData.getWeatherElement("HOUR_12"));
                            queryA2.put("V13023", twSiteData.getWeatherElement("HOUR_24"));
                            try {
                                otsHelperRTable.update(queryA2, true);
                                update++;
                                log.append("\nupdate map success!").append("stationId is ").append(twSiteData.getStationId());
                            } catch (TableStoreException | ExecutionException | InterruptedException e) {
                                log.append("\nupdate map exception!").append("stationId is ").append(twSiteData.getStationId());
                            }
                        }
                    }
                } else {
                    // A0001、A0003资料。查询是否已存在此记录标识,若存在则size>0
                    Map<String, Object> query = otsHelperRTable.query(row);
                    if (query.size() == 0) {
                        setReportTableValue(twSiteData, row, receTime, date, sodCode, ctsCode);
                        try {
                            otsHelperRTable.insert(row, false);
                            insert++;
                        } catch (InterruptedException | ExecutionException e) {
                            log.append("\ninsert map exception!").append(e.getMessage());
                        }
                    } else {
                        // 存在只需改观测值字段部分
                        String[] columns;
                        String[] elements;
                        if (fileName.contains("A0001")) {
                            elements = new String[]{"ELEV", "WDIR", "WDSD", "TEMP", "HUMD", "PRES", "H_FX", "H_XD", "H_FXT"};
                            columns = new String[]{"V07001", "V11201", "V11202", "V12001", "V13003", "V10004", "V11046", "V11211", "V11046_052"};
                        } else if (fileName.contains("A0002")) {
                            elements = new String[]{"ELEV", "MIN_10"};
                            columns = new String[]{"V07001", "V13392_010"};
                        } else if (fileName.contains("A0003")) {
                            elements = new String[]{"ELEV", "WDIR", "WDSD", "TEMP", "HUMD", "PRES", "H_FX", "H_XD", "H_FXT", "H_F10", "H_10D", "H_F10T", "H_VIS"};
                            columns = new String[]{"V07001", "V11201", "V11202", "V12001", "V13003", "V10004", "V11046", "V11211", "V11046_052", "V11042", "V11296", "V11042_052", "V20001"};
                        } else {
                            log.append("this is data fileName error!");
                            return DataBaseAction.INSERT_ERROR;
                        }
                        for (int j = 0; j < columns.length; j++) {
                            Double ele = twSiteData.getWeatherElement(elements[j]);
                            if (elements[j].equals("HUMD") && ele != 999999) {
                                // 计算相对湿度值
                                query.put(columns[j], Math.round(ele * 100));
                            } else if (elements[j].equals("ELEV")) {
                                // 针对A0002(雨量数据文件)高度值不做修改
                                if (!twSiteData.getResourceId().equals("O-A0002-001")) {
                                    query.put(columns[j], ele);
                                }
                            } else {
                                query.put(columns[j], ele);
                            }
                        }
                        try {
                            otsHelperRTable.update(query, true);
                            update++;
                            log.append("\nupdate map success!").append("stationId is ").append(twSiteData.getStationId());
                        } catch (TableStoreException | InterruptedException | ExecutionException e) {
                            log.append("\nupdate map exception!").append("stationId is ").append(twSiteData.getStationId());
                        }
                    }
                }
            } catch (Exception e) {
                di.setPROCESS_STATE("0");
                log.append("\nots query data exception!").append(e.getMessage());
            }
            // 补全di
            di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
            di.setPROCESS_END_TIME(TimeUtil.getSysTime());
            di.setRECORD_TIME(TimeUtil.getSysTime());
            diQueues.offer(di);
        }
        writeStationInfoToFile(stationInfos, configFile);
        // A0002整点资料入两张表 size*2
        if (flagA2) {
            HourA2Count += size;
            log.append("\nA0002 data total:").append(2 * size).append(",single insert success:").append(insert).append(",single update success:").append(update).append(",error row :").append(2 * size - insert - update);
        } else {
            log.append("\n data total:").append(size).append(",single insert success:").append(insert).append(",single update success:").append(update).append(",error row :").append(size - insert - update);
        }
        updateCount += update;
        return insert + update > 0 ? DataBaseAction.SUCCESS : DataBaseAction.INSERT_ERROR;
    }

    /**
     * 赋值
     * @param twSiteData 观测值对象
     * @param row        map
     * @param receTime   资料接受时间
     * @param date       观测时间(世界时)
     * @param sodCode    sodCode
     * @param ctsCode    ctsCode
     */
    private static void setReportTableValue(TwSiteData twSiteData, Map<String, Object> row, Date receTime, Date date, String sodCode, String ctsCode) {
        String recordId = twSiteData.getResourceId() + "_" + TimeUtil.date2String(date, "yyyyMMddHH");
        row.put("D_RECORD_ID", recordId); // 记录标识
        row.put("D_DATA_ID", sodCode);// 资料标识
        row.put("D_IYMDHM", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 入库时间
        row.put("D_RYMDHM", TimeUtil.date2String(receTime, "yyyy-MM-dd HH:mm:ss"));  // 收到时间
        row.put("D_UPDATE_TIME", TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss")); // 更新时间
        row.put("D_DATETIME", TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss"));  // 资料时间
        row.put("D_SOURCE_ID", ctsCode); // 数据来源
        row.put("V04001", TimeUtil.getYear(date));// 年
        row.put("V04002", TimeUtil.getMonth(date));// 月
        row.put("V04003", TimeUtil.getDayOfMonth(date));// 日
        row.put("V04004", TimeUtil.getHourOfDay(date));// 时
        row.put("V01301", twSiteData.getStationId());// 区站号/观测平台标识(字符)
        row.put("V01300", DEFAULT9); // 区站号/观测平台标识(数字)
        row.put("V05001", twSiteData.getLatitude());// 纬度
        row.put("V06001", twSiteData.getLongitude());// 经度
        row.put("V07001", twSiteData.getWeatherElement("ELEV"));// 测站高度
        row.put("V07031", DEFAULT8); // 气压传感器海拔高度
        row.put("V07032_04", DEFAULT8); // 风速传感器距地面高度
        row.put("V07032_01", DEFAULT8); // 温湿传感器离地面高度
        row.put("V07032_02", DEFAULT8); // 能见度传感器离地面高度
        row.put("V02001", DEFAULT8); // 测站类型
        row.put("V02301", DEFAULT8); // 测站级别
        row.put("V_ACODE", 710000);// 中国行政区划代码
        row.put("V08010", DEFAULT8); // 地面限定符（温度数据）
        row.put("V02183", DEFAULT8); // 云探测系统
        row.put("V_BBB", 000);// 更正报标志
        row.put("V10004", twSiteData.getWeatherElement("PRES"));// 气压
        row.put("V10051", DEFAULT8); // 海平面气压
        row.put("V10061", DEFAULT8); // 3小时变压
        row.put("V10062", DEFAULT8); // 24小时变压
        row.put("V10301", DEFAULT8); // 最高本站气压
        row.put("V10301_052", DEFAULT8); // 最高本站气压出现时间
        row.put("V10302", DEFAULT8); // 最低本站气压
        row.put("V10302_052", DEFAULT8); // 最低本站气压出现时间
        row.put("V12001", twSiteData.getWeatherElement("TEMP"));// 温度/气温
        row.put("V12011", DEFAULT8);// 最高气温
        row.put("V12011_052", DEFAULT8);// 最高气温出现时间
        row.put("V12012", DEFAULT8);// 最低气温
        row.put("V12012_052", DEFAULT8);// 最低气温出现时间
        row.put("V12405", DEFAULT8); // 过去24小时变温
        row.put("V12016", DEFAULT8); // 过去24小时最高气温
        row.put("V12017", DEFAULT8); // 过去24小时最低气温
        row.put("V12003", DEFAULT8); // 露点温度
        if (!twSiteData.getWeatherElement("HUMD").toString().contains("99999")) {
            row.put("V13003", Math.round(twSiteData.getWeatherElement("HUMD") * 100));// 相对湿度
        } else {
            row.put("V13003", twSiteData.getWeatherElement("HUMD"));// 相对湿度
        }
        row.put("V13007", DEFAULT8); // 最小相对湿度
        row.put("V13007_052", DEFAULT8); // 最小相对湿度出现时间
        row.put("V13004", DEFAULT8); // 水汽压
        row.put("V13019", twSiteData.getWeatherElement("RAIN"));// 过去1小时降水量
        row.put("V13020", twSiteData.getWeatherElement("HOUR_3"));// 过去3小时降水量
        row.put("V13021", twSiteData.getWeatherElement("HOUR_6"));// 过去6小时降水量
        row.put("V13022", twSiteData.getWeatherElement("HOUR_12"));// 过去12小时降水量
        row.put("V13023", twSiteData.getWeatherElement("HOUR_24"));// 过去24小时降水量
        row.put("V04080_04", DEFAULT8); // 人工加密观测降水量描述周期
        row.put("V13011", DEFAULT8); // 降水量
        row.put("V13033", DEFAULT8); // 蒸发(大型)
        row.put("V11290", DEFAULT8); // 2分钟平均风向(角度)
        row.put("V11291", DEFAULT8); // 2分钟平均风速
        row.put("V11292", DEFAULT8); // 10分钟平均风向(角度)
        row.put("V11293", DEFAULT8); // 10分钟平均风速
        row.put("V11296", twSiteData.getWeatherElement("H_10D")); // 日最大风速的风向(角度)
        row.put("V11042", twSiteData.getWeatherElement("H_F10")); // 最大风速
        // 时间转为世界时
        if (twSiteData.getWeatherElement("H_F10T") == 999999 || twSiteData.getWeatherElement("H_F10T") == 999998) {
            row.put("V11042_052", twSiteData.getWeatherElement("H_F10T"));// 最大风速出现时间
        } else {
            String hourMinute = hourMinuteToUT(twSiteData.getWeatherElement("H_F10T"));
            row.put("V11042_052", hourMinute); // 最大风速出现时间
            // 同时修改值
            twSiteData.setWeatherElement("H_F10T", Double.parseDouble(hourMinute));
        }
        row.put("V11201", twSiteData.getWeatherElement("WDIR").intValue());// 瞬时风向(角度)
        row.put("V11202", twSiteData.getWeatherElement("WDSD"));// 瞬时风速
        row.put("V11211", twSiteData.getWeatherElement("H_XD").intValue());// 极大风速的风向(角度)
        row.put("V11046", twSiteData.getWeatherElement("H_FX"));// 极大风速
        // 时间转为世界时
        if (twSiteData.getWeatherElement("H_FXT") == 999999 || twSiteData.getWeatherElement("H_FXT") == 999998) {
            row.put("V11046_052", twSiteData.getWeatherElement("H_FXT"));// 极大风速出现时间
        } else {
            String hourMinute = hourMinuteToUT(twSiteData.getWeatherElement("H_FXT"));
            row.put("V11046_052", hourMinute); // 极大风速出现时间
            // 同时修改值
            twSiteData.setWeatherElement("H_FXT", Double.parseDouble(hourMinute));
        }
        row.put("V11503_06", DEFAULT8); // 过去6小时极大瞬时风向
        row.put("V11504_06", DEFAULT8); // 过去6小时极大瞬时风速
        row.put("V11503_12", DEFAULT8); // 过去12小时极大瞬时风向
        row.put("V11504_12", DEFAULT8); // 过去12小时极大瞬时风速
        row.put("V12120", DEFAULT8); // 地面温度
        row.put("V12311", DEFAULT8); // 最高地面温度
        row.put("V12311_052", DEFAULT8); // 最高地面温度出现时间
        row.put("V12121", DEFAULT8); // 最低地面温度
        row.put("V12121_052", DEFAULT8); // 最低地面温度出现时间
        row.put("V12013", DEFAULT8); // 过去12小时地面最低温度
        row.put("V12030_005", DEFAULT8); // 5cm地温
        row.put("V12030_010", DEFAULT8); // 10cm地温
        row.put("V12030_015", DEFAULT8); // 15cm地温
        row.put("V12030_020", DEFAULT8); // 20cm地温
        row.put("V12030_040", DEFAULT8); // 40cm地温
        row.put("V12030_080", DEFAULT8); // 80cm地温
        row.put("V12030_160", DEFAULT8); // 160cm地温
        row.put("V12030_320", DEFAULT8); // 320cm地温
        row.put("V12314", DEFAULT8); // 草面(雪面)温度
        row.put("V12315", DEFAULT8); // 草面(雪面)最高温度
        row.put("V12315_052", DEFAULT8); // 草面(雪面)最高温度出现时间
        row.put("V12316", DEFAULT8); // 草面(雪面)最低温度
        row.put("V12316_052", DEFAULT8); // 草面(雪面)最低温度出现时间
        row.put("V20001_701_01", DEFAULT8); // 1分钟平均能见度
        row.put("V20001_701_10", DEFAULT8); // 10分钟平均能见度
        row.put("V20059", DEFAULT8); // 最小水平能见度
        row.put("V20059_052", DEFAULT8); // 最小水平能见度出现时间
        row.put("V20001", twSiteData.getWeatherElement("H_VIS"));// 水平能见度(人工)
        row.put("V20010", DEFAULT8); // 总云量
        row.put("V20051", DEFAULT8); // 低云量
        row.put("V20011", DEFAULT8); // 云量(低云或中云)
        row.put("V20013", DEFAULT8); // 云底高度
        row.put("V20350_01", DEFAULT8); // 云状1
        row.put("V20350_02", DEFAULT8); // 云状2
        row.put("V20350_03", DEFAULT8); // 云状3
        row.put("V20350_04", DEFAULT8); // 云状4
        row.put("V20350_05", DEFAULT8); // 云状5
        row.put("V20350_06", DEFAULT8); // 云状6
        row.put("V20350_07", DEFAULT8); // 云状7
        row.put("V20350_08", DEFAULT8); // 云状8
        row.put("V20350_11", DEFAULT8); // 低云状
        row.put("V20350_12", DEFAULT8); // 中云状
        row.put("V20350_13", DEFAULT8); // 高云状
        row.put("V20003", DEFAULT8);// 现在天气
        row.put("V04080_05", DEFAULT8); // 过去天气描述事件周期
        row.put("V20004", DEFAULT8); // 过去天气1
        row.put("V20005", DEFAULT8); // 过去天气2
        row.put("V20062", DEFAULT8); // 地面状态
        row.put("V13013", DEFAULT8); // 积雪深度
        row.put("V13330", DEFAULT8); // 雪压
        row.put("V20330_01", DEFAULT8); // 第一冻土层上界值
        row.put("V20331_01", DEFAULT8); // 第一冻土层下界值
        row.put("V20330_02", DEFAULT8); // 第二冻土层上界值
        row.put("V20331_02", DEFAULT8); // 第二冻土层下界值
        row.put("Q10004", DEFAULTQC); // 气压质控码
        row.put("Q10051", DEFAULTQC); // 海平面气压质量控制标志
        row.put("Q10061", DEFAULTQC); // 3小时变压质控码
        row.put("Q10062", DEFAULTQC); // 24小时变压质控码
        row.put("Q10301", DEFAULTQC); // 日最高本站气压质控码
        row.put("Q10301_052", DEFAULTQC); // 日最高本站气压出现时间质控码
        row.put("Q10302", DEFAULTQC); // 日最低本站气压质控码
        row.put("Q10302_052", DEFAULTQC); // 日最低本站气压出现时间质控码
        row.put("Q12001", DEFAULTQC); // 温度/气温质控码
        row.put("Q12011", DEFAULTQC); // 日最高气温质控码
        row.put("Q12011_052", DEFAULTQC); // 日最高气温出现时间质控码
        row.put("Q12012", DEFAULTQC); // 1小时内最低气温质控码
        row.put("Q12012_052", DEFAULTQC); // 小时内最低气温出现时间质控码
        row.put("Q12405", DEFAULTQC); // 24小时变温质控码
        row.put("Q12016", DEFAULTQC); // 过去24小时最高气温质控码
        row.put("Q12017", DEFAULTQC); // 过去24小时最低气温质控码
        row.put("Q12003", DEFAULTQC); // 露点温度质控码
        row.put("Q13003", DEFAULTQC); // 相对湿度质控码
        row.put("Q13007", DEFAULTQC); // 最小相对湿度质控码
        row.put("Q13007_052", DEFAULTQC); // 最小相对湿度出现时间质控码
        row.put("Q13004", DEFAULTQC); // 水汽压质控码
        row.put("Q13019", DEFAULTQC); // 小时降水量质控码
        row.put("Q13020", DEFAULTQC); // 过去3小时降水量质控码
        row.put("Q13021", DEFAULTQC); // 过去6小时降水量质控码
        row.put("Q13022", DEFAULTQC); // 过去12小时降水量质控码
        row.put("Q13023", DEFAULTQC); // 24小时降水量质控码
        row.put("Q04080_04", DEFAULTQC); // 人工加密观测降水量描述时间周期质控码
        row.put("Q13011", DEFAULTQC); // 分钟降水量质控码
        row.put("Q13033", DEFAULTQC); // 日蒸发量（大型）质控码
        row.put("Q11290", DEFAULTQC); // 2分钟平均风向质控码值
        row.put("Q11291", DEFAULTQC); // 2分钟平均风速成质控码值
        row.put("Q11292", DEFAULTQC); // 10分钟风向质控码
        row.put("Q11293", DEFAULTQC); // 10分钟平均风速质控码
        row.put("Q11296", DEFAULTQC); // 日最大风速的风向质控码
        row.put("Q11042", DEFAULTQC); // 日最大风速质控码
        row.put("Q11042_052", DEFAULTQC); // 日最大风速出现时间质控码
        row.put("Q11201", DEFAULTQC); // 瞬时风向(角度)质控码
        row.put("Q11202", DEFAULTQC); // 瞬时风速质控码
        row.put("Q11211", DEFAULTQC); // 日极大风速的风向质控码
        row.put("Q11046", DEFAULTQC); // 日极大风速质控码
        row.put("Q11046_052", DEFAULTQC); // 日极大风速出现时间质控码
        row.put("Q11503_06", DEFAULTQC); // 过去6小时极大瞬时风向质控码
        row.put("Q11504_06", DEFAULTQC); // 过去6小时极大瞬时风速质控码
        row.put("Q11503_12", DEFAULTQC); // 过去12小时极大瞬时风向质控码
        row.put("Q11504_12", DEFAULTQC); // 过去12小时极大瞬时风速质控码
        row.put("Q12120", DEFAULTQC); // 地面温度质控码
        row.put("Q12311", DEFAULTQC); // 日最高地面温度质控码
        row.put("Q12311_052", DEFAULTQC); // 日最高地面温度出现时间质控码
        row.put("Q12121", DEFAULTQC); // 日最低地面温度质控码
        row.put("Q12121_052", DEFAULTQC); // 日最低地面温度出现时间质控码
        row.put("Q12013", DEFAULTQC); // 过去12小时最低地面温度质控码
        row.put("Q12030_005", DEFAULTQC); // 5cm地温质控码
        row.put("Q12030_010", DEFAULTQC); // 10cm地温质控码
        row.put("Q12030_015", DEFAULTQC); // 15cm地温质控码
        row.put("Q12030_020", DEFAULTQC); // 20cm地温质控码
        row.put("Q12030_040", DEFAULTQC); // 40cm地温质控码
        row.put("Q12030_080", DEFAULTQC); // 80cm地温质控码
        row.put("Q12030_160", DEFAULTQC); // 160cm地温质控码
        row.put("Q12030_320", DEFAULTQC); // 320cm地温质控码
        row.put("Q12314", DEFAULTQC); // 草面（雪面）温度质控码
        row.put("Q12315", DEFAULTQC); // 日草面（雪面）最高温度质控码
        row.put("Q12315_052", DEFAULTQC); // 日草面（雪面）最高温度出现时间质控码
        row.put("Q12316", DEFAULTQC); // 日草面（雪面）最低温度质控码
        row.put("Q12316_052", DEFAULTQC); // 日草面（雪面）最低温度出现时间质控码
        row.put("Q20001_701_01", DEFAULTQC); // 1分钟平均水平能见度质控码
        row.put("Q20001_701_10", DEFAULTQC); // 10分钟平均水平能见度质控码
        row.put("Q20059", DEFAULTQC); // 日最小水平能见度质控码
        row.put("Q20059_052", DEFAULTQC); // 日最小水平能见度出现时间质控码
        row.put("Q20001", DEFAULTQC); // 水平能见度质控码
        row.put("Q20010", DEFAULTQC); // 总云量质控码
        row.put("Q20051", DEFAULTQC); // 低云量质控码
        row.put("Q20011", DEFAULTQC); // 低云或中云的云量质控码
        row.put("Q20013", DEFAULTQC); // 云底高度质控码
        row.put("Q20350_01", DEFAULTQC); // 云状1质控码
        row.put("Q20350_02", DEFAULTQC); // 云状2质控码
        row.put("Q20350_03", DEFAULTQC); // 云状3质控码
        row.put("Q20350_04", DEFAULTQC); // 云状4质控码
        row.put("Q20350_05", DEFAULTQC); // 云状5质控码
        row.put("Q20350_06", DEFAULTQC); // 云状6质控码
        row.put("Q20350_07", DEFAULTQC); // 云状7质控码
        row.put("Q20350_08", DEFAULTQC); // 云状8质控码
        row.put("Q20350_11", DEFAULTQC); // 低云状质控码
        row.put("Q20350_12", DEFAULTQC); // 中云状质控码
        row.put("Q20350_13", DEFAULTQC); // 高云状质控码
        row.put("Q20003", DEFAULTQC); // 现在天气质控码
        row.put("Q04080_05", DEFAULTQC); // 过去天气描述时间周期质控码
        row.put("Q20004", DEFAULTQC); // 过去天气1质控码
        row.put("Q20005", DEFAULTQC); // 过去天气2质控码
        row.put("Q20062", DEFAULTQC); // 地面状态质控码
        row.put("Q13013", DEFAULTQC); // 路面雪层厚度质控码
        row.put("Q13330", DEFAULTQC); // 雪压质控码
        row.put("Q20330_01", DEFAULTQC); // 第一冻土层上界值质控码
        row.put("Q20331_01", DEFAULTQC); // 第一冻土层下界值质控码
        row.put("Q20330_02", DEFAULTQC); // 第二冻土层上界值质控码
        row.put("Q20331_02", DEFAULTQC); // 第二冻土层下界值质控码
        row.put("V_RETAIN1", DEFAULT8); // 保留字段1
        row.put("V_RETAIN2", DEFAULT8); // 保留字段2
        row.put("V_RETAIN3", DEFAULT8); // 保留字段3
        row.put("V_RETAIN4", DEFAULT8); // 保留字段4
        row.put("V_RETAIN5", DEFAULT8); // 保留字段5
        row.put("V_RETAIN6", DEFAULT8); // 保留字段6
        row.put("V_RETAIN7", DEFAULT8); // 保留字段7
        row.put("V_RETAIN8", DEFAULT8); // 保留字段8
        row.put("V_RETAIN9", DEFAULT8); // 保留字段9
        row.put("V_RETAIN10", DEFAULT8);// 保留字段10
    }

    /**
     * 报文中的时间转为世界时
     * @param value 观测时间,格式 "0000"小时分钟
     * @return 观测世界时, 格式"0000"
     */
    private static String hourMinuteToUT(Double value) {
        DecimalFormat format = new DecimalFormat("0000");
        String hourMinute = format.format(value);
        int hour = Integer.parseInt(hourMinute.substring(0, 2));
        if (hour - 8 < 0) {
            hour += 16;
        } else {
            hour -= 8;
        }
        return hour + hourMinute.substring(2, 4);
    }

    /**
     * 赋值
     * @param twSiteData 观测值对象
     * @param rowKey     map
     * @param receTime   资料接受时间
     * @param date       观测时间(世界时)
     * @param sodCode    sodCode
     * @param ctsCode    ctsCode
     */
    private static void setKeyTableValue(TwSiteData twSiteData, Map<String, Object> rowKey, Date receTime, Date date, String sodCode, String ctsCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        rowKey.put("D_DATA_ID", sodCode); // 资料标识
        rowKey.put("D_IYMDHM", sdf.format(new Date())); // 入库时间
        rowKey.put("D_RYMDHM", sdf.format(receTime.getTime()));  // 收到时间
        rowKey.put("D_UPDATE_TIME", sdf.format(new Date().getTime())); // 更新时间
        rowKey.put("D_DATETIME", sdf.format(date.getTime())); // 资料时间
        rowKey.put("V01301", twSiteData.getStationId());// 区站号/观测平台标识(字符)
        rowKey.put("V05001", twSiteData.getLatitude());// 纬度
        rowKey.put("V06001", twSiteData.getLongitude());// 经度
        rowKey.put("V07001", twSiteData.getWeatherElement("ELEV"));// 测站高度
        rowKey.put("V02001", DEFAULT8);// 水平能见度(人工)
        rowKey.put("V02301", DEFAULT8);// 测站级别
        rowKey.put("V_ACODE", 710000);// 中国行政区划代码
        rowKey.put("V04001", TimeUtil.getYear(date));// 年
        rowKey.put("V04002", TimeUtil.getMonth(date));// 月
        rowKey.put("V04003", TimeUtil.getDayOfMonth(date));// 日
        rowKey.put("V04004", TimeUtil.getHourOfDay(date));// 时
        rowKey.put("V04005", TimeUtil.getMinute(date));// 分
        rowKey.put("V13392_005", DEFAULT8);// 5分钟雨量
        rowKey.put("V13392_010", twSiteData.getWeatherElement("MIN_10"));// 10分钟雨量
        rowKey.put("Q_V13392_005", DEFAULTQC);// 5分钟雨量质控码
        rowKey.put("Q_V13392_010", DEFAULTQC);// 10分钟雨量质控码
        rowKey.put("D_SOURCE_ID", ctsCode);// D_SOURCE_ID
        rowKey.put("V_BBB", "000");
    }

    /**
     * 将站点信息写入文件
     * @param content 站点信息
     * @param file 文件路径
     */
    private static void writeStationInfoToFile(StringBuffer content, File file) {
        RandomAccessFile rw = null;
        FileLock fileLock = null;
        try {
            rw = new RandomAccessFile(file, "rw");
            fileLock = rw.getChannel().lock();
            rw.seek(rw.length());
            rw.write(content.toString().getBytes());
        } catch (Exception e) {
            infoLogger.error(e.getMessage());
        } finally {
            try {
                if (fileLock != null) fileLock.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (rw != null) rw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
