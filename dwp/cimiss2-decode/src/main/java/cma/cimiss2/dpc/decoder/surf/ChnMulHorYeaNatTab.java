package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.SurfChnMulHorYeaNatTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.DayValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.HourValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.MonthValueTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.SunLightValueTab;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChnMulHorYeaNatTab {
    /**
     * 结果集
     */
    private ParseResult<SurfChnMulHorYeaNatTab> parseResult = new ParseResult<SurfChnMulHorYeaNatTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public Map<String, Object> decode(File file, String ctsCode, String filename) {
        Map<String, Object> resultMap = new HashMap<>();
        ParseResult<String> decodingInfo = new ParseResult<String>(false);
        if (file != null && file.exists() && file.isFile()) {
            if (file == null || (!file.exists()) || (file.isFile()) || (!filename.startsWith("A6"))) {
                if (filename.length() <= 0) {
                    decodingInfo.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                    resultMap.put("decodingInfo", decodingInfo);
                    return resultMap;
                }
            }
            try {
                // get file encode
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
                fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                List<DayValueTab> dayList = new ArrayList<>();
                List<HourValueTab> hourList = new ArrayList<>();
                List<SunLightValueTab> sunLightList = new ArrayList<>();
                List<MonthValueTab> monthList = new ArrayList<>();
                String[] messages = txtFileContent.get(0).split("\\s+");


                String V01300 = changeType(messages[0]);
                String V01301 = changeType(messages[0]);

                String V05001 = messages[1].substring(0, 4);
                String V06001 = messages[1].substring(4, 9);
                String V07001 = messages[2];

                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    for (int i = 1; i < txtFileContent.size(); i++) {
                        if (txtFileContent.get(i).startsWith("SZ")) {
                            int num = 0;
                            for (int j = i + 1; j < txtFileContent.size(); j++) {
                                num += 1;
                                String[] sz = txtFileContent.get(j).split("\\s+");
                                SunLightValueTab sunLightValueTab = new SunLightValueTab();
                                String uuid = UUID.randomUUID().toString();
                                sunLightValueTab.setD_RECORD_ID(uuid); // 记录标识 ( 系统自动生成的流水号 )
                                sunLightValueTab.setD_DATA_ID(ctsCode); // 资料标识 ( 资料的4级编码 )
                                if (txtFileContent.get(i).endsWith("0")) {
                                    //sunLightValueTab.setD_DATETIME(sdf.format(calendar.getTime()));
                                    sunLightValueTab.setD_DATETIME(messages[5] + "-" + messages[6] + "-" + num + " 00:00:00"); // 资料时间 ( 由V04001、04002、V04003组成 )
                                    sunLightValueTab.setV14032_004(Float.parseFloat(sz[0])); // 03-04时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_005(Float.parseFloat(sz[1])); // 04-05时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_006(Float.parseFloat(sz[2])); // 05-06时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_007(Float.parseFloat(sz[3])); // 06-07时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_008(Float.parseFloat(sz[4])); // 07-08时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_009(Float.parseFloat(sz[5])); // 08-09时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_010(Float.parseFloat(sz[6])); // 09-10时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_011(Float.parseFloat(sz[7])); // 10-11时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_012(Float.parseFloat(sz[8])); // 11-12时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_013(Float.parseFloat(sz[9])); // 12-13时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_014(Float.parseFloat(sz[10])); // 13_14时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_015(Float.parseFloat(sz[11])); // 14-15时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_016(Float.parseFloat(sz[12])); // 15-16时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_017(Float.parseFloat(sz[13])); // 16-17时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_018(Float.parseFloat(sz[14])); // 17-18时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_019(Float.parseFloat(sz[15])); // 18-19时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_020(Float.parseFloat(sz[16])); // 19-20时日照时数 ( 单位：小时 )
                                    try {
                                        sunLightValueTab.setV14032_021(Float.parseFloat(sz[17])); // 20-21时日照时数 ( 单位：小时 )
                                    } catch (Exception e) {
                                        sunLightValueTab.setV14032_021(Float.parseFloat(sz[17].substring(0, sz[17].length() - 1))); // 20-21时日照时数 ( 单位：小时 )
                                    }
                                } else if (txtFileContent.get(i).endsWith("1")) {
                                    //sunLightValueTab.setD_DATETIME(sdf.format(calendar.getTime()));
                                    sunLightValueTab.setD_DATETIME(messages[5] + "-" + messages[6] + "-" + sz[0] + " 00:00:00"); // 资料时间 ( 由V04001、04002、V04003组成 )
                                    sunLightValueTab.setV14032_004(Float.parseFloat(sz[1])); // 03-04时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_005(Float.parseFloat(sz[2])); // 04-05时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_006(Float.parseFloat(sz[3])); // 05-06时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_007(Float.parseFloat(sz[4])); // 06-07时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_008(Float.parseFloat(sz[5])); // 07-08时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_009(Float.parseFloat(sz[6])); // 08-09时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_010(Float.parseFloat(sz[7])); // 09-10时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_011(Float.parseFloat(sz[8])); // 10-11时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_012(Float.parseFloat(sz[9])); // 11-12时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_013(Float.parseFloat(sz[10])); // 12-13时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_014(Float.parseFloat(sz[11])); // 13_14时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_015(Float.parseFloat(sz[12])); // 14-15时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_016(Float.parseFloat(sz[13])); // 15-16时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_017(Float.parseFloat(sz[14])); // 16-17时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_018(Float.parseFloat(sz[15])); // 17-18时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_019(Float.parseFloat(sz[16])); // 18-19时日照时数 ( 单位：小时 )
                                    sunLightValueTab.setV14032_020(Float.parseFloat(sz[17])); // 19-20时日照时数 ( 单位：小时 )
                                    try {
                                        sunLightValueTab.setV14032_021(Float.parseFloat(sz[18])); // 20-21时日照时数 ( 单位：小时 )
                                    } catch (Exception e) {
                                        sunLightValueTab.setV14032_021(Float.parseFloat(sz[18].substring(0, sz[18].length() - 1))); // 20-21时日照时数 ( 单位：小时 )
                                    }
                                }
                                try {
                                    sunLightValueTab.setD_IYMDHM(sdf.format(calendar.getTime())); // 入库时间 ( 插表时的系统时间 )
                                    sunLightValueTab.setD_RYMDHM(sdf.format(calendar.getTime())); // 收到时间 ( DPC消息生成时间 )
                                    sunLightValueTab.setD_UPDATE_TIME(sdf.format(calendar.getTime())); // 更新时间 ( 根据CCx对记录更新时的系统时间 )
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                sunLightValueTab.setV_BBB("000"); // 更正报标志 ( 报头行BBB )
                                sunLightValueTab.setV01301(V01301); // 区站号(字符)
                                sunLightValueTab.setV01300(V01300); // 区站号(数字)
                                sunLightValueTab.setV05001(Float.parseFloat(V05001)); // 纬度 ( 单位：度 )
                                sunLightValueTab.setV06001(Float.parseFloat(V06001)); // 经度 ( 单位：度 )
                                sunLightValueTab.setV07001(Float.parseFloat(V07001)); // 测站高度 ( 单位：米 )
                                sunLightValueTab.setV02301(999999); // 台站级别 ( 代码表 )
                                sunLightValueTab.setV_ACODE(999999); // 中国行政区划代码 ( 代码表 )
                                sunLightValueTab.setV14332(999999); // 日照时制方式 ( 代码表 )
                                sunLightValueTab.setV04001(Integer.parseInt(messages[5])); // 资料观测年
                                sunLightValueTab.setV04002(Integer.parseInt(messages[6])); // 资料观测月
                                sunLightValueTab.setV04003(0); // 资料观测日
                                sunLightValueTab.setV14032_001(Float.parseFloat("999999")); // 00-01时日照时数 ( 单位：小时 )
                                sunLightValueTab.setV14032_002(Float.parseFloat("999999")); // 01-02时日照时数 ( 单位：小时 )
                                sunLightValueTab.setV14032_003(Float.parseFloat("999999")); // 02-03时日照时数 ( 单位：小时 )
                                sunLightValueTab.setV14032_022(Float.parseFloat("999999")); // 21-22时日照时数 ( 单位：小时 )
                                sunLightValueTab.setV14032_023(Float.parseFloat("999999")); // 22-23时日照时数 ( 单位：小时 )
                                sunLightValueTab.setV14032_024(Float.parseFloat("999999")); // 23-24时日照时数 ( 单位：小时 )
                                sunLightValueTab.setV14032(Float.parseFloat("999999")); // 日日照时数 ( 单位：小时 )
                                sunLightValueTab.setQ14032_001(999999); // 00-01时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_002(999999); // 01-02时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_003(999999); // 02-03时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_004(999999); // 03-04时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_005(999999); // 04-05时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_006(999999); // 05-06时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_007(999999); // 06-07时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_008(999999); // 07-08时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_009(999999); // 08-09时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_010(999999); // 09-10时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_011(999999); // 10-11时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_012(999999); // 11-12时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_013(999999); // 12-13时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_014(999999); // 13_14时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_015(999999); // 14-15时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_016(999999); // 15-16时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_017(999999); // 16-17时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_018(999999); // 17-18时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_019(999999); // 18-19时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_020(999999); // 19-20时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_021(999999); // 20-21时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_022(999999); // 21-22时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_023(999999); // 22-23时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032_024(999999); // 23-24时日照时数质控码 ( 代码表 )
                                sunLightValueTab.setQ14032(999999); // 23-24时日照时数质控码 ( 代码表 )
                                sunLightList.add(sunLightValueTab);
                                try {
                                    if (sz[18].endsWith("=")) {
                                        break;
                                    }
                                } catch (Exception e) {
                                    if (sz[17].endsWith("=")) {
                                        break;
                                    }
                                }
                            }
                        }
                        if (txtFileContent.get(i).startsWith("TD")) {
                            List hList = new ArrayList();
                            if (txtFileContent.get(i).endsWith("A")) {
                                for (int j = i + 1; j < txtFileContent.size(); j++) {
                                    String[] sz = txtFileContent.get(j).split("\\s+");
                                    hList.add(sz[0]);
                                    hList.add(sz[1]);
                                    hList.add(sz[2]);
                                    hList.add(sz[3]);
                                    hList.add(sz[4]);
                                    hList.add(sz[5]);
                                    hList.add(sz[6]);
                                    hList.add(sz[7]);
                                    hList.add(sz[8]);
                                    hList.add(sz[9]);
                                    hList.add(sz[10]);
                                    hList.add(sz[11]);
                                    if (sz[11].endsWith("=")) {
                                        break;
                                    }
                                }
                                int days = 0;
                                int hour = 0;
                                for (int k = 0; k < hList.size(); k++) {
                                    hour += 1;
                                    if (hour > 12) {
                                        hour = 1;
                                    }
                                    if (k % 12 == 0) {
                                        days += 1;
                                    }

                                    String uuid = UUID.randomUUID().toString();
                                    HourValueTab HourValueTab = new HourValueTab();
                                    HourValueTab.setD_MAIN_ID(uuid); // 记录标识 ( 系统自动生成的流水号 )
                                    HourValueTab.setD_IYMDHM(sdf.format(calendar.getTime())); // 入库时间 ( 插表时的系统时间 )
                                    HourValueTab.setD_RYMDHM(sdf.format(calendar.getTime())); // 收到时间 ( DPC消息生成时间 )
                                    HourValueTab.setD_UPDATE_TIME(sdf.format(calendar.getTime())); // 更新时间 ( 根据CCx对记录更新时的系统时间 )
                                    HourValueTab.setD_DATETIME(sdf.format(calendar.getTime()));

            //                        HourValueTab.setD_DATETIME(messages[5] + "-" + messages[6] + "-" + days + " " + hour + ":00:00"); // 资料时间 ( 由V04001、V04002、V04003、V04004 )
                                    HourValueTab.setV_BBB("000"); // 更正标志 ( 组成 )
                                    HourValueTab.setV01301(V01301); // 区站号(字符)
                                    HourValueTab.setV01300(V01300); // 区站号(数字)
                                    HourValueTab.setV05001(Float.parseFloat(V05001)); // 纬度
                                    HourValueTab.setV06001(Float.parseFloat(V06001)); // 经度 ( 单位：度 )
                                    HourValueTab.setV07001(Float.parseFloat(V07001)); // 测站高度 ( 单位：度 )
                                    HourValueTab.setV07031(Float.parseFloat("999999")); // 气压传感器海拔高度 ( 单位：米 )
                                    HourValueTab.setV07032_04(Float.parseFloat("999999")); // 风速传感器距地面高度 ( 单位：米 )
                                    HourValueTab.setV02001("999999"); // 测站类型 ( 单位：米 )
                                    HourValueTab.setV02301(999999); // 台站级别 ( 代码表 )
                                    HourValueTab.setV_ACODE(999999); // 中国行政区划代码 ( 代码表 )
                                    HourValueTab.setV04001(Integer.parseInt(messages[5])); // 资料观测年 ( 代码表 )
                                    HourValueTab.setV04002(Integer.parseInt(messages[6])); // 资料观测月
                                    HourValueTab.setV04003(days); // 资料观测日
                                    HourValueTab.setV04004(hour); // 资料观测时
                                    HourValueTab.setV10004(Float.parseFloat("999999")); // 本站气压
                                    HourValueTab.setV10051(Float.parseFloat("999999")); // 海平面气压 ( 单位：百帕 )
                                    HourValueTab.setV10061(Float.parseFloat("999999")); // 3小时变压 ( 单位：百帕 )
                                    HourValueTab.setV10062(Float.parseFloat("999999")); // 24小时变压 ( 单位：百帕 )
                                    HourValueTab.setV10301(Float.parseFloat("999999")); // 小时内最高本站气压 ( 单位：百帕 )
                                    HourValueTab.setV10301_052(999999); // 小时内最高本站气压出现时间 ( 格式：hhmm )
                                    HourValueTab.setV10302(Float.parseFloat("999999")); // 小时内最低本站气压 ( 单位：百帕  )
                                    HourValueTab.setV10302_052(999999); // 小时内最低本站气压出现时间 ( 格式：hhmm )
                                    HourValueTab.setV12001(Float.parseFloat("999999")); // 气温 ( 单位：摄氏度 )
                                    HourValueTab.setV12011(Float.parseFloat("999999")); // 小时内最高气温 ( 单位：摄氏度 )
                                    HourValueTab.setV12011_052(999999); // 小时内最高气温出现时间 ( 单位：摄氏度 )
                                    HourValueTab.setV12012(Float.parseFloat("999999")); // 小时内最低气温 (单位：摄氏度  )
                                    HourValueTab.setV12012_052(999999); // 小时内最低气温出现时间 ( 格式：hhmm )
                                    HourValueTab.setV12405(Float.parseFloat("999999")); // 24小时变温 (单位：摄氏度  )
                                    HourValueTab.setV12016(Float.parseFloat("999999")); // 过去24小时最高气温 ( 单位：摄氏度 )
                                    HourValueTab.setV12017(Float.parseFloat("999999")); // 过去24小时最低气温 ( 单位：摄氏度 )
//                                HourValueTab.setBalltemp(Float.parseFloat("00"));//湿球温度
                                    try {
                                        HourValueTab.setV12003(Float.parseFloat(hList.get(k).toString())); // 露点温度 ( 单位：摄氏度 )
                                    } catch (Exception e) {
                                        HourValueTab.setV12003(Float.parseFloat(hList.get(k).toString().substring(0, hList.get(k).toString().length() - 1))); // 露点温度 ( 单位：摄氏度 )
                                    }
                                    HourValueTab.setV13003(999999); // 相对湿度 (单位：% )
                                    HourValueTab.setV13007(Float.parseFloat("999999")); // 小时内最小相对湿度 ( 单位：% )
                                    HourValueTab.setV13007_052(999999); // 小时内最小相对湿度出现时间 (格式：hhmm)
                                    HourValueTab.setV13004(Float.parseFloat("999999")); // 水汽压 (单位：百帕 )
                                    HourValueTab.setV13019(Float.parseFloat("999999")); // 1小时降水量 ( 单位：毫米   )
                                    HourValueTab.setV13020(Float.parseFloat("999999")); // 过去3小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV13021(Float.parseFloat("999999")); // 过去6小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV13022(Float.parseFloat("999999")); // 过去12小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV13023(Float.parseFloat("999999")); // 过去24小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV04080_04(999999); // 人工加密观测降水量描述时间周期 ( 单位：毫米 )
                                    HourValueTab.setV13011(Float.parseFloat("999999")); // 人工加密观测降水量 ( 单位：小时 )
                                    HourValueTab.setV13033(Float.parseFloat("999999")); // 小时蒸发量 ( 单位：毫米 )
                                    HourValueTab.setV11290("999999"); // 2分钟平均风向 ( 单位：毫米 )
                                    HourValueTab.setV11291(Float.parseFloat("999999")); // 2分钟平均风速 ( 单位：度 )
                                    HourValueTab.setV11292("999999"); // 10分钟平均风向 ( 单位：米/秒 )
                                    HourValueTab.setV11293(Float.parseFloat("999999")); // 10分钟平均风速 ( 单位：度 )
                                    HourValueTab.setV11296("999999"); // 小时内最大风速的风向 ( 单位：米/秒 )
                                    HourValueTab.setV11042(Float.parseFloat("999999")); // 小时内最大风速 ( 单位：度 )
                                    HourValueTab.setV11042_052(999999); // 小时内最大风速出现时间 ( 格式：hhmm )
                                    HourValueTab.setV11201("999999"); // 瞬时风向 ( 单位：度 )
                                    HourValueTab.setV11202(Float.parseFloat("999999")); // 瞬时风速 ( 单位：米/秒 )
                                    HourValueTab.setV11211(Float.parseFloat("999999")); // 小时内极大风速的风向 ( 单位：米/秒 )
                                    HourValueTab.setV11046(Float.parseFloat("999999")); // 小时内极大风速 ( 单位：度 )
                                    HourValueTab.setV11046_052(999999); // 小时内极大风速出现时间 (格式：hhmm  )
                                    HourValueTab.setV11503_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风向 (单位：度  )
                                    HourValueTab.setV11504_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风速 (单位：米/秒  )
                                    HourValueTab.setV11503_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风向 ( 单位：度  )
                                    HourValueTab.setV11504_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风速 ( 单位：米/秒)
                                    HourValueTab.setV12120(Float.parseFloat("999999")); // 地面温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12311(Float.parseFloat("999999")); // 小时内最高地面温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12311_052(999999); // 小时内最高地面温度出现时间 (  格式：hhmm)
                                    HourValueTab.setV12121(Float.parseFloat("999999")); // 小时内最低地面温度 (单位：摄氏度  )
                                    HourValueTab.setV12121_052(999999); // 小时内最低地面温度出现时间 ( 格式：hhmm)
                                    HourValueTab.setV12013(Float.parseFloat("999999")); // 过去12小时最低地面温度 (单位：摄氏度  )
                                    HourValueTab.setV12030_005(Float.parseFloat("999999")); // 5cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_010(Float.parseFloat("999999")); // 10cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_015(Float.parseFloat("999999")); // 15cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_020(Float.parseFloat("999999")); // 20cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_040(Float.parseFloat("999999")); // 40cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_080(Float.parseFloat("999999")); // 80cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_160(Float.parseFloat("999999")); // 160cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_320(Float.parseFloat("999999")); // 320cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12314(Float.parseFloat("999999")); // 草面（雪面）温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12315(Float.parseFloat("999999")); // 小时内草面（雪面）最高温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12315_052(999999); // 小时内草面（雪面）最高温度出现时间 ( 格式：hhmm )
                                    HourValueTab.setV12316(Float.parseFloat("999999")); // 小时内草面（雪面）最低温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12316_052(999999); // 小时内草面（雪面）最低温度出现时间 ( 格式：hhmm  )
                                    HourValueTab.setV20001_701_01(Float.parseFloat("999999")); // 1分钟平均水平能见度 ( 单位：米 )
                                    HourValueTab.setV20001_701_10(Float.parseFloat("999999")); // 10分钟平均水平能见度 ( 单位：米 )
                                    HourValueTab.setV20059(Float.parseFloat("999999")); // 最小水平能见度 ( 单位：米 )
                                    HourValueTab.setV20059_052("999999"); // 最小水平能见度出现时间 ( 格式：hhmm  )
                                    HourValueTab.setV20001(Float.parseFloat("999999")); // 水平能见度（人工） ( 单位：千米 )
//                                HourValueTab.setVisibility2(0);//能见度级别
                                    HourValueTab.setV20010(999999); // 总云量 (单位：%  )
                                    HourValueTab.setV20051(999999); // 低云量 ( 单位：% )
                                    HourValueTab.setV20011(Float.parseFloat("999999")); // 低云或中云的云量 ( 单位：% )
                                    HourValueTab.setV20013(Float.parseFloat("999999")); // 低云或中云的云高 ( 单位：% )
                                    HourValueTab.setV20350_01(999999); // 云状1 ( 单位：米 )
                                    HourValueTab.setV20350_02("999999"); // 云状2 ( 代码表 )
                                    HourValueTab.setV20350_03(999999); // 云状3 ( 代码表 )
                                    HourValueTab.setV20350_04(999999); // 云状4 ( 代码表 )
                                    HourValueTab.setV20350_05(999999); // 云状5 ( 代码表 )
                                    HourValueTab.setV20350_06(999999); // 云状6 ( 代码表 )
                                    HourValueTab.setV20350_07(999999); // 云状7 ( 代码表 )
                                    HourValueTab.setV20350_08(999999); // 云状8 ( 代码表 )
                                    HourValueTab.setV20350_11(999999); // 低云状 ( 代码表 )
                                    HourValueTab.setV20350_12(999999); // 中云状 ( 代码表 )
                                    HourValueTab.setV20350_13(999999); // 高云状 ( 代码表 )
                                    HourValueTab.setV20003(999999); // 现在天气 ( 代码表 )
                                    HourValueTab.setV04080_05(999999); // 过去天气描述时间周期 ( 代码表 )
                                    HourValueTab.setV20004(999999); // 过去天气1 ( 单位:小时 )
                                    HourValueTab.setV20005(999999); // 过去天气2 ( 代码表 )
                                    HourValueTab.setV20062(999999); // 地面状态 ( 代码表 )
                                    HourValueTab.setV13013(Float.parseFloat("999999")); // 积雪深度 ( 代码表 )
                                    HourValueTab.setV13330(Float.parseFloat("999999")); // 雪压 ( 单位：厘米 )
                                    HourValueTab.setV20330_01(Float.parseFloat("999999")); // 冻土第1冻结层上限值 ( 单位：g/cm2 )
                                    HourValueTab.setV20331_01(Float.parseFloat("999999")); // 冻土第1冻结层下限值 ( 单位：厘米 )
                                    HourValueTab.setV20330_02(Float.parseFloat("999999")); // 冻土第2冻结层上限值 ( 单位：厘米 )
                                    HourValueTab.setV20331_02(Float.parseFloat("999999")); // 冻土第2冻结层下限值 ( 单位：厘米 )
                                    HourValueTab.setQ10004(999999); // 本站气压质量标志 ( 单位：厘米 )
                                    HourValueTab.setQ10051(999999); // 海平面气压质量标志 ( 代码表 )
                                    HourValueTab.setQ10061(999999); // 3小时变压质量标志 ( 代码表 )
                                    HourValueTab.setQ10062(999999); // 24小时变压质质量标志 ( 代码表 )
                                    HourValueTab.setQ10301(999999); // 小时内最高本站气压质量标志 ( 代码表 )
                                    HourValueTab.setQ10301_052(999999); // 小时内最高本站气压出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ10302(999999); // 小时内最低本站气压质量标志 ( 代码表 )
                                    HourValueTab.setQ10302_052(999999); // 小时内最低本站气压出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12001(999999); // 气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12011(999999); // 小时内最高气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12011_052(999999); // 小时内最高气温出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12012(999999); // 小时内最低气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12012_052(999999); // 小时内最低气温出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12405(999999); // 24小时变温质量标志 ( 代码表 )
                                    HourValueTab.setQ12016(999999); // 过去24小时最高气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12017(999999); // 过去24小时最低气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12003(999999); // 露点温度质量标志 ( 代码表 )
                                    HourValueTab.setQ13003(999999); // 相对湿度质量标志 ( 代码表 )
                                    HourValueTab.setQ13007(999999); // 小时内最小相对湿度质量标志 ( 代码表 )
                                    HourValueTab.setQ13007_052(999999); // 小时内最小相对湿度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ13004(999999); // 水汽压质量标志 ( 代码表 )
                                    HourValueTab.setQ13019(999999); // 1小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13020(999999); // 过去3小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13021(999999); // 过去6小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13022(999999); // 过去12小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13023(999999); // 24小时降水量质 ( 代码表 )
                                    HourValueTab.setQ04080_04(999999); // 人工加密观测降水量描述时间周期质量标志 ( 代码表 )
                                    HourValueTab.setQ13011(999999); // 人工加密观测降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13033(999999); // 小时蒸发量质量标志 ( 代码表 )
                                    HourValueTab.setQ11290(999999); // 2分钟平均风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11291(999999); // 2分钟平均风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11292(999999); // 10分钟平均风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11293(999999); // 10分钟平均风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11296(999999); // 小时内最大风速的风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11042(999999); // 小时内最大风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11042_052(999999); // 小时内最大风速出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ11201(999999); // 瞬时风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11202(999999); // 瞬时风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11211(999999); // 小时内极大风速的风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11046(999999); // 小时内极大风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11046_052(999999); // 小时内极大风速出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ11503_06(999999); // 过去6小时极大瞬时风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11504_06(999999); // 过去6小时极大瞬时风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11503_12(999999); // 过去12小时极大瞬时风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11504_12(999999); // 过去12小时极大瞬时风速质量标志 ( 代码表 )
                                    HourValueTab.setQ12120(999999); // 地面温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12311(999999); // 小时内最高地面温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12311_052(999999); // 小时内最高地面温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12121(999999); // 小时内最低地面温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12121_052(999999); // 小时内最低地面温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12013(999999); // 过去12小时最低地面温度 ( 代码表 )
                                    HourValueTab.setQ12030_005(999999); // 5cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_010(999999); // 10cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_015(999999); // 15cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_020(999999); // 20cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_040(999999); // 40cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_080(999999); // 80cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_160(999999); // 160cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_320(999999); // 320cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12314(999999); // 草面（雪面）温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12315(999999); // 小时内草面（雪面）最高温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12315_052(999999); // 小时内草面（雪面）最高温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12316(999999); // 小时内草面（雪面）最低温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12316_052(999999); // 小时内草面（雪面）最低温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ20001_701_01(999999); // 1分钟平均水平能见度质量标志 ( 代码表 )
                                    HourValueTab.setQ20001_701_10(999999); // 10分钟平均水平能见度质量标志 ( 代码表 )
                                    HourValueTab.setQ20059(999999); // 最小水平能见度质量标志 ( 代码表 )
                                    HourValueTab.setQ20059_052(999999); // 最小水平能见度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ20001(999999); // 水平能见度（人工）质量标志 ( 代码表 )
                                    HourValueTab.setQ20010(999999); // 总云量质量标志 ( 代码表 )
                                    HourValueTab.setQ20051(999999); // 低云量质量标志 ( 代码表 )
                                    HourValueTab.setQ20011(999999); // 低云或中云的云量质量标志 ( 代码表 )
                                    HourValueTab.setQ20013(999999); // 低云或中云的云高质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_01(999999); // 云状1质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_02(999999); // 云状2质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_03(999999); // 云状3质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_04(999999); // 云状4质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_05(999999); // 云状5质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_06(999999); // 云状6质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_07(999999); // 云状7质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_08(999999); // 云状8质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_11(999999); // 低云状质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_12(999999); // 中云状质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_13(999999); // 高云状质量标志 ( 代码表 )
                                    HourValueTab.setQ20003(999999); // 现在天气质量标志 ( 代码表 )
                                    HourValueTab.setQ04080_05(999999); // 过去天气描述时间周期质量标志 ( 代码表 )
                                    HourValueTab.setQ20004(999999); // 过去天气1质量标志 ( 代码表 )
                                    HourValueTab.setQ20005(999999); // 过去天气2质量标志 ( 代码表 )
                                    HourValueTab.setQ20062(999999); // 地面状态质量标志 ( 代码表 )
                                    HourValueTab.setQ13013(999999); // 积雪深度质量标志 ( 代码表 )
                                    HourValueTab.setQ13330(999999); // 雪压质量标志 ( 代码表 )
                                    HourValueTab.setQ20330_01(999999); // 冻土第1冻结层上限值质量标志 ( 代码表 )
                                    HourValueTab.setQ20331_01(999999); // 冻土第1冻结层下限值质量标志 ( 代码表 )
                                    HourValueTab.setQ20330_02(999999); // 冻土第2冻结层上限值质量标志 ( 代码表 )
                                    HourValueTab.setQ20331_02(999999); // 冻土第2冻结层下限值质量标志 ( 代码表 )
                                    hourList.add(HourValueTab);
                                }
                            } else if (txtFileContent.get(i).endsWith("0")) {
                                for (int j = i + 1; j < txtFileContent.size(); j++) {
                                    String[] sz = txtFileContent.get(j).split("\\s+");
                                    hList.add(sz[0]);
                                    hList.add(sz[1]);
                                    hList.add(sz[2]);
                                    hList.add(sz[3]);
                                    if (sz[3].endsWith("=")) {
                                        break;
                                    }
                                }
                                int days = 0;
                                int hours = 02;
                                for (int k = 0; k < hList.size(); k++) {
                                    if (k % 4 == 0) {
                                        days += 1;
                                    }
                                    String uuid = UUID.randomUUID().toString();
                                    HourValueTab HourValueTab = new HourValueTab();
                                    HourValueTab.setD_MAIN_ID(uuid); // 记录标识 ( 系统自动生成的流水号 )
                                    HourValueTab.setD_IYMDHM(sdf.format(calendar.getTime())); // 入库时间 ( 插表时的系统时间 )
                                    HourValueTab.setD_RYMDHM(sdf.format(calendar.getTime())); // 收到时间 ( DPC消息生成时间 )
                                    HourValueTab.setD_DATETIME(sdf.format(calendar.getTime()));
                                    //HourValueTab.setD_DATETIME(messages[5] + "-" + messages[6] + "-" + days + " " + hours + ":00:00"); // 资料时间 ( 由V04001、V04002、V04003、V04004 )
                                    hours += 6;
                                    if (hours > 20) {
                                        hours = 02;
                                    }
                                    HourValueTab.setV_BBB("000"); // 更正标志 ( 组成 )
                                    HourValueTab.setV01301(V01301); // 区站号(字符)
                                    HourValueTab.setV01300(V01300); // 区站号(数字)
                                    HourValueTab.setV05001(Float.parseFloat(V05001)); // 纬度
                                    HourValueTab.setV06001(Float.parseFloat(V06001)); // 经度 ( 单位：度 )
                                    HourValueTab.setV07001(Float.parseFloat(V07001)); // 测站高度 ( 单位：度 )
                                    HourValueTab.setV07031(Float.parseFloat("999999")); // 气压传感器海拔高度 ( 单位：米 )
                                    HourValueTab.setV07032_04(Float.parseFloat("999999")); // 风速传感器距地面高度 ( 单位：米 )
                                    HourValueTab.setV02001("999999"); // 测站类型 ( 单位：米 )
                                    HourValueTab.setV02301(999999); // 台站级别 ( 代码表 )
                                    HourValueTab.setV_ACODE(999999); // 中国行政区划代码 ( 代码表 )
                                    HourValueTab.setV04001(Integer.parseInt(messages[5])); // 资料观测年 ( 代码表 )
                                    HourValueTab.setV04002(Integer.parseInt(messages[6])); // 资料观测月
                                    HourValueTab.setV04003(days); // 资料观测日
                                    HourValueTab.setV04004(hours); // 资料观测时
                                    HourValueTab.setV10004(Float.parseFloat("999999")); // 本站气压
                                    HourValueTab.setV10051(Float.parseFloat("999999")); // 海平面气压 ( 单位：百帕 )
                                    HourValueTab.setV10061(Float.parseFloat("999999")); // 3小时变压 ( 单位：百帕 )
                                    HourValueTab.setV10062(Float.parseFloat("999999")); // 24小时变压 ( 单位：百帕 )
                                    HourValueTab.setV10301(Float.parseFloat("999999")); // 小时内最高本站气压 ( 单位：百帕 )
                                    HourValueTab.setV10301_052(999999); // 小时内最高本站气压出现时间 ( 格式：hhmm )
                                    HourValueTab.setV10302(Float.parseFloat("999999")); // 小时内最低本站气压 ( 单位：百帕  )
                                    HourValueTab.setV10302_052(999999); // 小时内最低本站气压出现时间 ( 格式：hhmm )
                                    HourValueTab.setV12001(Float.parseFloat("999999")); // 气温 ( 单位：摄氏度 )
                                    HourValueTab.setV12011(Float.parseFloat("999999")); // 小时内最高气温 ( 单位：摄氏度 )
                                    HourValueTab.setV12011_052(999999); // 小时内最高气温出现时间 ( 单位：摄氏度 )
                                    HourValueTab.setV12012(Float.parseFloat("999999")); // 小时内最低气温 (单位：摄氏度  )
                                    HourValueTab.setV12012_052(999999); // 小时内最低气温出现时间 ( 格式：hhmm )
                                    HourValueTab.setV12405(Float.parseFloat("999999")); // 24小时变温 (单位：摄氏度  )
                                    HourValueTab.setV12016(Float.parseFloat("999999")); // 过去24小时最高气温 ( 单位：摄氏度 )
                                    HourValueTab.setV12017(Float.parseFloat("999999")); // 过去24小时最低气温 ( 单位：摄氏度 )
//                                HourValueTab.setBalltemp(Float.parseFloat("00"));//湿球温度
                                    try {
                                        HourValueTab.setV12003(Float.parseFloat(hList.get(k).toString())); // 露点温度 ( 单位：摄氏度 )
                                    } catch (Exception e) {
                                        HourValueTab.setV12003(Float.parseFloat(hList.get(k).toString().substring(0, hList.get(k).toString().length() - 1))); // 露点温度 ( 单位：摄氏度 )
                                    }
                                    HourValueTab.setV13003(999999); // 相对湿度 (单位：% )
                                    HourValueTab.setV13007(Float.parseFloat("999999")); // 小时内最小相对湿度 ( 单位：% )
                                    HourValueTab.setV13007_052(999999); // 小时内最小相对湿度出现时间 (格式：hhmm)
                                    HourValueTab.setV13004(Float.parseFloat("999999")); // 水汽压 (单位：百帕 )
                                    HourValueTab.setV13019(Float.parseFloat("999999")); // 1小时降水量 ( 单位：毫米   )
                                    HourValueTab.setV13020(Float.parseFloat("999999")); // 过去3小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV13021(Float.parseFloat("999999")); // 过去6小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV13022(Float.parseFloat("999999")); // 过去12小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV13023(Float.parseFloat("999999")); // 过去24小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV04080_04(999999); // 人工加密观测降水量描述时间周期 ( 单位：毫米 )
                                    HourValueTab.setV13011(Float.parseFloat("999999")); // 人工加密观测降水量 ( 单位：小时 )
                                    HourValueTab.setV13033(Float.parseFloat("999999")); // 小时蒸发量 ( 单位：毫米 )
                                    HourValueTab.setV11290("999999"); // 2分钟平均风向 ( 单位：毫米 )
                                    HourValueTab.setV11291(Float.parseFloat("999999")); // 2分钟平均风速 ( 单位：度 )
                                    HourValueTab.setV11292("999999"); // 10分钟平均风向 ( 单位：米/秒 )
                                    HourValueTab.setV11293(Float.parseFloat("999999")); // 10分钟平均风速 ( 单位：度 )
                                    HourValueTab.setV11296("999999"); // 小时内最大风速的风向 ( 单位：米/秒 )
                                    HourValueTab.setV11042(Float.parseFloat("999999")); // 小时内最大风速 ( 单位：度 )
                                    HourValueTab.setV11042_052(999999); // 小时内最大风速出现时间 ( 格式：hhmm )
                                    HourValueTab.setV11201("999999"); // 瞬时风向 ( 单位：度 )
                                    HourValueTab.setV11202(Float.parseFloat("999999")); // 瞬时风速 ( 单位：米/秒 )
                                    HourValueTab.setV11211(Float.parseFloat("999999")); // 小时内极大风速的风向 ( 单位：米/秒 )
                                    HourValueTab.setV11046(Float.parseFloat("999999")); // 小时内极大风速 ( 单位：度 )
                                    HourValueTab.setV11046_052(999999); // 小时内极大风速出现时间 (格式：hhmm  )
                                    HourValueTab.setV11503_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风向 (单位：度  )
                                    HourValueTab.setV11504_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风速 (单位：米/秒  )
                                    HourValueTab.setV11503_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风向 ( 单位：度  )
                                    HourValueTab.setV11504_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风速 ( 单位：米/秒)
                                    HourValueTab.setV12120(Float.parseFloat("999999")); // 地面温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12311(Float.parseFloat("999999")); // 小时内最高地面温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12311_052(999999); // 小时内最高地面温度出现时间 (  格式：hhmm)
                                    HourValueTab.setV12121(Float.parseFloat("999999")); // 小时内最低地面温度 (单位：摄氏度  )
                                    HourValueTab.setV12121_052(999999); // 小时内最低地面温度出现时间 ( 格式：hhmm)
                                    HourValueTab.setV12013(Float.parseFloat("999999")); // 过去12小时最低地面温度 (单位：摄氏度  )
                                    HourValueTab.setV12030_005(Float.parseFloat("999999")); // 5cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_010(Float.parseFloat("999999")); // 10cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_015(Float.parseFloat("999999")); // 15cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_020(Float.parseFloat("999999")); // 20cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_040(Float.parseFloat("999999")); // 40cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_080(Float.parseFloat("999999")); // 80cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_160(Float.parseFloat("999999")); // 160cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_320(Float.parseFloat("999999")); // 320cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12314(Float.parseFloat("999999")); // 草面（雪面）温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12315(Float.parseFloat("999999")); // 小时内草面（雪面）最高温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12315_052(999999); // 小时内草面（雪面）最高温度出现时间 ( 格式：hhmm )
                                    HourValueTab.setV12316(Float.parseFloat("999999")); // 小时内草面（雪面）最低温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12316_052(999999); // 小时内草面（雪面）最低温度出现时间 ( 格式：hhmm  )
                                    HourValueTab.setV20001_701_01(Float.parseFloat("999999")); // 1分钟平均水平能见度 ( 单位：米 )
                                    HourValueTab.setV20001_701_10(Float.parseFloat("999999")); // 10分钟平均水平能见度 ( 单位：米 )
                                    HourValueTab.setV20059(Float.parseFloat("999999")); // 最小水平能见度 ( 单位：米 )
                                    HourValueTab.setV20059_052("999999"); // 最小水平能见度出现时间 ( 格式：hhmm  )
                                    HourValueTab.setV20001(Float.parseFloat("999999")); // 水平能见度（人工） ( 单位：千米 )
//                                HourValueTab.setVisibility2(0);//能见度级别
                                    HourValueTab.setV20010(999999); // 总云量 (单位：%  )
                                    HourValueTab.setV20051(999999); // 低云量 ( 单位：% )
                                    HourValueTab.setV20011(Float.parseFloat("999999")); // 低云或中云的云量 ( 单位：% )
                                    HourValueTab.setV20013(Float.parseFloat("999999")); // 低云或中云的云高 ( 单位：% )
                                    HourValueTab.setV20350_01(999999); // 云状1 ( 单位：米 )
                                    HourValueTab.setV20350_02("999999"); // 云状2 ( 代码表 )
                                    HourValueTab.setV20350_03(999999); // 云状3 ( 代码表 )
                                    HourValueTab.setV20350_04(999999); // 云状4 ( 代码表 )
                                    HourValueTab.setV20350_05(999999); // 云状5 ( 代码表 )
                                    HourValueTab.setV20350_06(999999); // 云状6 ( 代码表 )
                                    HourValueTab.setV20350_07(999999); // 云状7 ( 代码表 )
                                    HourValueTab.setV20350_08(999999); // 云状8 ( 代码表 )
                                    HourValueTab.setV20350_11(999999); // 低云状 ( 代码表 )
                                    HourValueTab.setV20350_12(999999); // 中云状 ( 代码表 )
                                    HourValueTab.setV20350_13(999999); // 高云状 ( 代码表 )
                                    HourValueTab.setV20003(999999); // 现在天气 ( 代码表 )
                                    HourValueTab.setV04080_05(999999); // 过去天气描述时间周期 ( 代码表 )
                                    HourValueTab.setV20004(999999); // 过去天气1 ( 单位:小时 )
                                    HourValueTab.setV20005(999999); // 过去天气2 ( 代码表 )
                                    HourValueTab.setV20062(999999); // 地面状态 ( 代码表 )
                                    HourValueTab.setV13013(Float.parseFloat("999999")); // 积雪深度 ( 代码表 )
                                    HourValueTab.setV13330(Float.parseFloat("999999")); // 雪压 ( 单位：厘米 )
                                    HourValueTab.setV20330_01(Float.parseFloat("999999")); // 冻土第1冻结层上限值 ( 单位：g/cm2 )
                                    HourValueTab.setV20331_01(Float.parseFloat("999999")); // 冻土第1冻结层下限值 ( 单位：厘米 )
                                    HourValueTab.setV20330_02(Float.parseFloat("999999")); // 冻土第2冻结层上限值 ( 单位：厘米 )
                                    HourValueTab.setV20331_02(Float.parseFloat("999999")); // 冻土第2冻结层下限值 ( 单位：厘米 )
                                    HourValueTab.setQ10004(999999); // 本站气压质量标志 ( 单位：厘米 )
                                    HourValueTab.setQ10051(999999); // 海平面气压质量标志 ( 代码表 )
                                    HourValueTab.setQ10061(999999); // 3小时变压质量标志 ( 代码表 )
                                    HourValueTab.setQ10062(999999); // 24小时变压质质量标志 ( 代码表 )
                                    HourValueTab.setQ10301(999999); // 小时内最高本站气压质量标志 ( 代码表 )
                                    HourValueTab.setQ10301_052(999999); // 小时内最高本站气压出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ10302(999999); // 小时内最低本站气压质量标志 ( 代码表 )
                                    HourValueTab.setQ10302_052(999999); // 小时内最低本站气压出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12001(999999); // 气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12011(999999); // 小时内最高气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12011_052(999999); // 小时内最高气温出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12012(999999); // 小时内最低气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12012_052(999999); // 小时内最低气温出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12405(999999); // 24小时变温质量标志 ( 代码表 )
                                    HourValueTab.setQ12016(999999); // 过去24小时最高气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12017(999999); // 过去24小时最低气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12003(999999); // 露点温度质量标志 ( 代码表 )
                                    HourValueTab.setQ13003(999999); // 相对湿度质量标志 ( 代码表 )
                                    HourValueTab.setQ13007(999999); // 小时内最小相对湿度质量标志 ( 代码表 )
                                    HourValueTab.setQ13007_052(999999); // 小时内最小相对湿度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ13004(999999); // 水汽压质量标志 ( 代码表 )
                                    HourValueTab.setQ13019(999999); // 1小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13020(999999); // 过去3小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13021(999999); // 过去6小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13022(999999); // 过去12小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13023(999999); // 24小时降水量质 ( 代码表 )
                                    HourValueTab.setQ04080_04(999999); // 人工加密观测降水量描述时间周期质量标志 ( 代码表 )
                                    HourValueTab.setQ13011(999999); // 人工加密观测降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13033(999999); // 小时蒸发量质量标志 ( 代码表 )
                                    HourValueTab.setQ11290(999999); // 2分钟平均风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11291(999999); // 2分钟平均风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11292(999999); // 10分钟平均风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11293(999999); // 10分钟平均风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11296(999999); // 小时内最大风速的风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11042(999999); // 小时内最大风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11042_052(999999); // 小时内最大风速出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ11201(999999); // 瞬时风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11202(999999); // 瞬时风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11211(999999); // 小时内极大风速的风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11046(999999); // 小时内极大风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11046_052(999999); // 小时内极大风速出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ11503_06(999999); // 过去6小时极大瞬时风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11504_06(999999); // 过去6小时极大瞬时风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11503_12(999999); // 过去12小时极大瞬时风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11504_12(999999); // 过去12小时极大瞬时风速质量标志 ( 代码表 )
                                    HourValueTab.setQ12120(999999); // 地面温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12311(999999); // 小时内最高地面温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12311_052(999999); // 小时内最高地面温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12121(999999); // 小时内最低地面温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12121_052(999999); // 小时内最低地面温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12013(999999); // 过去12小时最低地面温度 ( 代码表 )
                                    HourValueTab.setQ12030_005(999999); // 5cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_010(999999); // 10cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_015(999999); // 15cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_020(999999); // 20cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_040(999999); // 40cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_080(999999); // 80cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_160(999999); // 160cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_320(999999); // 320cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12314(999999); // 草面（雪面）温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12315(999999); // 小时内草面（雪面）最高温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12315_052(999999); // 小时内草面（雪面）最高温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12316(999999); // 小时内草面（雪面）最低温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12316_052(999999); // 小时内草面（雪面）最低温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ20001_701_01(999999); // 1分钟平均水平能见度质量标志 ( 代码表 )
                                    HourValueTab.setQ20001_701_10(999999); // 10分钟平均水平能见度质量标志 ( 代码表 )
                                    HourValueTab.setQ20059(999999); // 最小水平能见度质量标志 ( 代码表 )
                                    HourValueTab.setQ20059_052(999999); // 最小水平能见度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ20001(999999); // 水平能见度（人工）质量标志 ( 代码表 )
                                    HourValueTab.setQ20010(999999); // 总云量质量标志 ( 代码表 )
                                    HourValueTab.setQ20051(999999); // 低云量质量标志 ( 代码表 )
                                    HourValueTab.setQ20011(999999); // 低云或中云的云量质量标志 ( 代码表 )
                                    HourValueTab.setQ20013(999999); // 低云或中云的云高质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_01(999999); // 云状1质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_02(999999); // 云状2质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_03(999999); // 云状3质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_04(999999); // 云状4质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_05(999999); // 云状5质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_06(999999); // 云状6质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_07(999999); // 云状7质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_08(999999); // 云状8质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_11(999999); // 低云状质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_12(999999); // 中云状质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_13(999999); // 高云状质量标志 ( 代码表 )
                                    HourValueTab.setQ20003(999999); // 现在天气质量标志 ( 代码表 )
                                    HourValueTab.setQ04080_05(999999); // 过去天气描述时间周期质量标志 ( 代码表 )
                                    HourValueTab.setQ20004(999999); // 过去天气1质量标志 ( 代码表 )
                                    HourValueTab.setQ20005(999999); // 过去天气2质量标志 ( 代码表 )
                                    HourValueTab.setQ20062(999999); // 地面状态质量标志 ( 代码表 )
                                    HourValueTab.setQ13013(999999); // 积雪深度质量标志 ( 代码表 )
                                    HourValueTab.setQ13330(999999); // 雪压质量标志 ( 代码表 )
                                    HourValueTab.setQ20330_01(999999); // 冻土第1冻结层上限值质量标志 ( 代码表 )
                                    HourValueTab.setQ20331_01(999999); // 冻土第1冻结层下限值质量标志 ( 代码表 )
                                    HourValueTab.setQ20330_02(999999); // 冻土第2冻结层上限值质量标志 ( 代码表 )
                                    HourValueTab.setQ20331_02(999999); // 冻土第2冻结层下限值质量标志 ( 代码表 )
                                    hourList.add(HourValueTab);
                                }
                            } else if (txtFileContent.get(i).endsWith("9")) {
                                for (int j = i + 1; j < txtFileContent.size(); j++) {
                                    String[] sz = txtFileContent.get(j).split("\\s+");
                                    hList.add(sz[0]);
                                    hList.add(sz[1]);
                                    hList.add(sz[2]);
                                    if (sz[2].endsWith("=")) {
                                        break;
                                    }
                                }
                                int days = 0;
                                int hours = 8;
                                for (int k = 0; k < hList.size(); k++) {
                                    if (k % 4 == 0) {
                                        days += 1;
                                    }
                                    String uuid = UUID.randomUUID().toString();
                                    HourValueTab HourValueTab = new HourValueTab();
                                    HourValueTab.setD_MAIN_ID(uuid); // 记录标识 ( 系统自动生成的流水号 )
                                    HourValueTab.setD_IYMDHM(sdf.format(calendar.getTime())); // 入库时间 ( 插表时的系统时间 )
                                    HourValueTab.setD_RYMDHM(sdf.format(calendar.getTime())); // 收到时间 ( DPC消息生成时间 )
                                    HourValueTab.setD_UPDATE_TIME(sdf.format(calendar.getTime())); // 更新时间 ( 根据CCx对记录更新时的系统时间 )
                                    HourValueTab.setD_DATETIME(sdf.format(calendar.getTime()));
                                    //HourValueTab.setD_DATETIME(messages[5] + "-" + messages[6] + "-" + days + " " + hours + ":00:00"); // 资料时间 ( 由V04001、V04002、V04003、V04004 )
                                    hours += 6;
                                    if (hours > 20) {
                                        hours = 8;
                                    }
                                    HourValueTab.setV_BBB("000"); // 更正标志 ( 组成 )
                                    HourValueTab.setV01301(V01301); // 区站号(字符)
                                    HourValueTab.setV01300(V01300); // 区站号(数字)
                                    HourValueTab.setV05001(Float.parseFloat(V05001)); // 纬度
                                    HourValueTab.setV06001(Float.parseFloat(V06001)); // 经度 ( 单位：度 )
                                    HourValueTab.setV07001(Float.parseFloat(V07001)); // 测站高度 ( 单位：度 )
                                    HourValueTab.setV07031(Float.parseFloat("999999")); // 气压传感器海拔高度 ( 单位：米 )
                                    HourValueTab.setV07032_04(Float.parseFloat("999999")); // 风速传感器距地面高度 ( 单位：米 )
                                    HourValueTab.setV02001("999999"); // 测站类型 ( 单位：米 )
                                    HourValueTab.setV02301(999999); // 台站级别 ( 代码表 )
                                    HourValueTab.setV_ACODE(999999); // 中国行政区划代码 ( 代码表 )
                                    HourValueTab.setV04001(Integer.parseInt(messages[5])); // 资料观测年 ( 代码表 )
                                    HourValueTab.setV04002(Integer.parseInt(messages[6])); // 资料观测月
                                    HourValueTab.setV04003(days); // 资料观测日
                                    HourValueTab.setV04004(hours); // 资料观测时
                                    HourValueTab.setV10004(Float.parseFloat("999999")); // 本站气压
                                    HourValueTab.setV10051(Float.parseFloat("999999")); // 海平面气压 ( 单位：百帕 )
                                    HourValueTab.setV10061(Float.parseFloat("999999")); // 3小时变压 ( 单位：百帕 )
                                    HourValueTab.setV10062(Float.parseFloat("999999")); // 24小时变压 ( 单位：百帕 )
                                    HourValueTab.setV10301(Float.parseFloat("999999")); // 小时内最高本站气压 ( 单位：百帕 )
                                    HourValueTab.setV10301_052(999999); // 小时内最高本站气压出现时间 ( 格式：hhmm )
                                    HourValueTab.setV10302(Float.parseFloat("999999")); // 小时内最低本站气压 ( 单位：百帕  )
                                    HourValueTab.setV10302_052(999999); // 小时内最低本站气压出现时间 ( 格式：hhmm )
                                    HourValueTab.setV12001(Float.parseFloat("999999")); // 气温 ( 单位：摄氏度 )
                                    HourValueTab.setV12011(Float.parseFloat("999999")); // 小时内最高气温 ( 单位：摄氏度 )
                                    HourValueTab.setV12011_052(999999); // 小时内最高气温出现时间 ( 单位：摄氏度 )
                                    HourValueTab.setV12012(Float.parseFloat("999999")); // 小时内最低气温 (单位：摄氏度  )
                                    HourValueTab.setV12012_052(999999); // 小时内最低气温出现时间 ( 格式：hhmm )
                                    HourValueTab.setV12405(Float.parseFloat("999999")); // 24小时变温 (单位：摄氏度  )
                                    HourValueTab.setV12016(Float.parseFloat("999999")); // 过去24小时最高气温 ( 单位：摄氏度 )
                                    HourValueTab.setV12017(Float.parseFloat("999999")); // 过去24小时最低气温 ( 单位：摄氏度 )
//                                HourValueTab.setBalltemp(Float.parseFloat("00"));//湿球温度
                                    try {
                                        HourValueTab.setV12003(Float.parseFloat(hList.get(k).toString())); // 露点温度 ( 单位：摄氏度 )
                                    } catch (Exception e) {
                                        HourValueTab.setV12003(Float.parseFloat(hList.get(k).toString().substring(0, hList.get(k).toString().length() - 1))); // 露点温度 ( 单位：摄氏度 )
                                    }
                                    HourValueTab.setV13003(999999); // 相对湿度 (单位：% )
                                    HourValueTab.setV13007(Float.parseFloat("999999")); // 小时内最小相对湿度 ( 单位：% )
                                    HourValueTab.setV13007_052(999999); // 小时内最小相对湿度出现时间 (格式：hhmm)
                                    HourValueTab.setV13004(Float.parseFloat("999999")); // 水汽压 (单位：百帕 )
                                    HourValueTab.setV13019(Float.parseFloat("999999")); // 1小时降水量 ( 单位：毫米   )
                                    HourValueTab.setV13020(Float.parseFloat("999999")); // 过去3小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV13021(Float.parseFloat("999999")); // 过去6小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV13022(Float.parseFloat("999999")); // 过去12小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV13023(Float.parseFloat("999999")); // 过去24小时降水量 ( 单位：毫米 )
                                    HourValueTab.setV04080_04(999999); // 人工加密观测降水量描述时间周期 ( 单位：毫米 )
                                    HourValueTab.setV13011(Float.parseFloat("999999")); // 人工加密观测降水量 ( 单位：小时 )
                                    HourValueTab.setV13033(Float.parseFloat("999999")); // 小时蒸发量 ( 单位：毫米 )
                                    HourValueTab.setV11290("999999"); // 2分钟平均风向 ( 单位：毫米 )
                                    HourValueTab.setV11291(Float.parseFloat("999999")); // 2分钟平均风速 ( 单位：度 )
                                    HourValueTab.setV11292("999999"); // 10分钟平均风向 ( 单位：米/秒 )
                                    HourValueTab.setV11293(Float.parseFloat("999999")); // 10分钟平均风速 ( 单位：度 )
                                    HourValueTab.setV11296("999999"); // 小时内最大风速的风向 ( 单位：米/秒 )
                                    HourValueTab.setV11042(Float.parseFloat("999999")); // 小时内最大风速 ( 单位：度 )
                                    HourValueTab.setV11042_052(999999); // 小时内最大风速出现时间 ( 格式：hhmm )
                                    HourValueTab.setV11201("999999"); // 瞬时风向 ( 单位：度 )
                                    HourValueTab.setV11202(Float.parseFloat("999999")); // 瞬时风速 ( 单位：米/秒 )
                                    HourValueTab.setV11211(Float.parseFloat("999999")); // 小时内极大风速的风向 ( 单位：米/秒 )
                                    HourValueTab.setV11046(Float.parseFloat("999999")); // 小时内极大风速 ( 单位：度 )
                                    HourValueTab.setV11046_052(999999); // 小时内极大风速出现时间 (格式：hhmm  )
                                    HourValueTab.setV11503_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风向 (单位：度  )
                                    HourValueTab.setV11504_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风速 (单位：米/秒  )
                                    HourValueTab.setV11503_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风向 ( 单位：度  )
                                    HourValueTab.setV11504_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风速 ( 单位：米/秒)
                                    HourValueTab.setV12120(Float.parseFloat("999999")); // 地面温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12311(Float.parseFloat("999999")); // 小时内最高地面温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12311_052(999999); // 小时内最高地面温度出现时间 (  格式：hhmm)
                                    HourValueTab.setV12121(Float.parseFloat("999999")); // 小时内最低地面温度 (单位：摄氏度  )
                                    HourValueTab.setV12121_052(999999); // 小时内最低地面温度出现时间 ( 格式：hhmm)
                                    HourValueTab.setV12013(Float.parseFloat("999999")); // 过去12小时最低地面温度 (单位：摄氏度  )
                                    HourValueTab.setV12030_005(Float.parseFloat("999999")); // 5cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_010(Float.parseFloat("999999")); // 10cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_015(Float.parseFloat("999999")); // 15cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_020(Float.parseFloat("999999")); // 20cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_040(Float.parseFloat("999999")); // 40cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_080(Float.parseFloat("999999")); // 80cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_160(Float.parseFloat("999999")); // 160cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12030_320(Float.parseFloat("999999")); // 320cm地温 ( 单位：摄氏度 )
                                    HourValueTab.setV12314(Float.parseFloat("999999")); // 草面（雪面）温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12315(Float.parseFloat("999999")); // 小时内草面（雪面）最高温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12315_052(999999); // 小时内草面（雪面）最高温度出现时间 ( 格式：hhmm )
                                    HourValueTab.setV12316(Float.parseFloat("999999")); // 小时内草面（雪面）最低温度 ( 单位：摄氏度 )
                                    HourValueTab.setV12316_052(999999); // 小时内草面（雪面）最低温度出现时间 ( 格式：hhmm  )
                                    HourValueTab.setV20001_701_01(Float.parseFloat("999999")); // 1分钟平均水平能见度 ( 单位：米 )
                                    HourValueTab.setV20001_701_10(Float.parseFloat("999999")); // 10分钟平均水平能见度 ( 单位：米 )
                                    HourValueTab.setV20059(Float.parseFloat("999999")); // 最小水平能见度 ( 单位：米 )
                                    HourValueTab.setV20059_052("999999"); // 最小水平能见度出现时间 ( 格式：hhmm  )
                                    HourValueTab.setV20001(Float.parseFloat("999999")); // 水平能见度（人工） ( 单位：千米 )
//                                HourValueTab.setVisibility2(0);//能见度级别
                                    HourValueTab.setV20010(999999); // 总云量 (单位：%  )
                                    HourValueTab.setV20051(999999); // 低云量 ( 单位：% )
                                    HourValueTab.setV20011(Float.parseFloat("999999")); // 低云或中云的云量 ( 单位：% )
                                    HourValueTab.setV20013(Float.parseFloat("999999")); // 低云或中云的云高 ( 单位：% )
                                    HourValueTab.setV20350_01(999999); // 云状1 ( 单位：米 )
                                    HourValueTab.setV20350_02("999999"); // 云状2 ( 代码表 )
                                    HourValueTab.setV20350_03(999999); // 云状3 ( 代码表 )
                                    HourValueTab.setV20350_04(999999); // 云状4 ( 代码表 )
                                    HourValueTab.setV20350_05(999999); // 云状5 ( 代码表 )
                                    HourValueTab.setV20350_06(999999); // 云状6 ( 代码表 )
                                    HourValueTab.setV20350_07(999999); // 云状7 ( 代码表 )
                                    HourValueTab.setV20350_08(999999); // 云状8 ( 代码表 )
                                    HourValueTab.setV20350_11(999999); // 低云状 ( 代码表 )
                                    HourValueTab.setV20350_12(999999); // 中云状 ( 代码表 )
                                    HourValueTab.setV20350_13(999999); // 高云状 ( 代码表 )
                                    HourValueTab.setV20003(999999); // 现在天气 ( 代码表 )
                                    HourValueTab.setV04080_05(999999); // 过去天气描述时间周期 ( 代码表 )
                                    HourValueTab.setV20004(999999); // 过去天气1 ( 单位:小时 )
                                    HourValueTab.setV20005(999999); // 过去天气2 ( 代码表 )
                                    HourValueTab.setV20062(999999); // 地面状态 ( 代码表 )
                                    HourValueTab.setV13013(Float.parseFloat("999999")); // 积雪深度 ( 代码表 )
                                    HourValueTab.setV13330(Float.parseFloat("999999")); // 雪压 ( 单位：厘米 )
                                    HourValueTab.setV20330_01(Float.parseFloat("999999")); // 冻土第1冻结层上限值 ( 单位：g/cm2 )
                                    HourValueTab.setV20331_01(Float.parseFloat("999999")); // 冻土第1冻结层下限值 ( 单位：厘米 )
                                    HourValueTab.setV20330_02(Float.parseFloat("999999")); // 冻土第2冻结层上限值 ( 单位：厘米 )
                                    HourValueTab.setV20331_02(Float.parseFloat("999999")); // 冻土第2冻结层下限值 ( 单位：厘米 )
                                    HourValueTab.setQ10004(999999); // 本站气压质量标志 ( 单位：厘米 )
                                    HourValueTab.setQ10051(999999); // 海平面气压质量标志 ( 代码表 )
                                    HourValueTab.setQ10061(999999); // 3小时变压质量标志 ( 代码表 )
                                    HourValueTab.setQ10062(999999); // 24小时变压质质量标志 ( 代码表 )
                                    HourValueTab.setQ10301(999999); // 小时内最高本站气压质量标志 ( 代码表 )
                                    HourValueTab.setQ10301_052(999999); // 小时内最高本站气压出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ10302(999999); // 小时内最低本站气压质量标志 ( 代码表 )
                                    HourValueTab.setQ10302_052(999999); // 小时内最低本站气压出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12001(999999); // 气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12011(999999); // 小时内最高气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12011_052(999999); // 小时内最高气温出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12012(999999); // 小时内最低气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12012_052(999999); // 小时内最低气温出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12405(999999); // 24小时变温质量标志 ( 代码表 )
                                    HourValueTab.setQ12016(999999); // 过去24小时最高气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12017(999999); // 过去24小时最低气温质量标志 ( 代码表 )
                                    HourValueTab.setQ12003(999999); // 露点温度质量标志 ( 代码表 )
                                    HourValueTab.setQ13003(999999); // 相对湿度质量标志 ( 代码表 )
                                    HourValueTab.setQ13007(999999); // 小时内最小相对湿度质量标志 ( 代码表 )
                                    HourValueTab.setQ13007_052(999999); // 小时内最小相对湿度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ13004(999999); // 水汽压质量标志 ( 代码表 )
                                    HourValueTab.setQ13019(999999); // 1小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13020(999999); // 过去3小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13021(999999); // 过去6小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13022(999999); // 过去12小时降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13023(999999); // 24小时降水量质 ( 代码表 )
                                    HourValueTab.setQ04080_04(999999); // 人工加密观测降水量描述时间周期质量标志 ( 代码表 )
                                    HourValueTab.setQ13011(999999); // 人工加密观测降水量质量标志 ( 代码表 )
                                    HourValueTab.setQ13033(999999); // 小时蒸发量质量标志 ( 代码表 )
                                    HourValueTab.setQ11290(999999); // 2分钟平均风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11291(999999); // 2分钟平均风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11292(999999); // 10分钟平均风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11293(999999); // 10分钟平均风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11296(999999); // 小时内最大风速的风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11042(999999); // 小时内最大风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11042_052(999999); // 小时内最大风速出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ11201(999999); // 瞬时风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11202(999999); // 瞬时风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11211(999999); // 小时内极大风速的风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11046(999999); // 小时内极大风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11046_052(999999); // 小时内极大风速出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ11503_06(999999); // 过去6小时极大瞬时风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11504_06(999999); // 过去6小时极大瞬时风速质量标志 ( 代码表 )
                                    HourValueTab.setQ11503_12(999999); // 过去12小时极大瞬时风向质量标志 ( 代码表 )
                                    HourValueTab.setQ11504_12(999999); // 过去12小时极大瞬时风速质量标志 ( 代码表 )
                                    HourValueTab.setQ12120(999999); // 地面温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12311(999999); // 小时内最高地面温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12311_052(999999); // 小时内最高地面温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12121(999999); // 小时内最低地面温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12121_052(999999); // 小时内最低地面温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12013(999999); // 过去12小时最低地面温度 ( 代码表 )
                                    HourValueTab.setQ12030_005(999999); // 5cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_010(999999); // 10cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_015(999999); // 15cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_020(999999); // 20cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_040(999999); // 40cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_080(999999); // 80cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_160(999999); // 160cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12030_320(999999); // 320cm地温质量标志 ( 代码表 )
                                    HourValueTab.setQ12314(999999); // 草面（雪面）温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12315(999999); // 小时内草面（雪面）最高温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12315_052(999999); // 小时内草面（雪面）最高温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ12316(999999); // 小时内草面（雪面）最低温度质量标志 ( 代码表 )
                                    HourValueTab.setQ12316_052(999999); // 小时内草面（雪面）最低温度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ20001_701_01(999999); // 1分钟平均水平能见度质量标志 ( 代码表 )
                                    HourValueTab.setQ20001_701_10(999999); // 10分钟平均水平能见度质量标志 ( 代码表 )
                                    HourValueTab.setQ20059(999999); // 最小水平能见度质量标志 ( 代码表 )
                                    HourValueTab.setQ20059_052(999999); // 最小水平能见度出现时间质量标志 ( 代码表 )
                                    HourValueTab.setQ20001(999999); // 水平能见度（人工）质量标志 ( 代码表 )
                                    HourValueTab.setQ20010(999999); // 总云量质量标志 ( 代码表 )
                                    HourValueTab.setQ20051(999999); // 低云量质量标志 ( 代码表 )
                                    HourValueTab.setQ20011(999999); // 低云或中云的云量质量标志 ( 代码表 )
                                    HourValueTab.setQ20013(999999); // 低云或中云的云高质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_01(999999); // 云状1质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_02(999999); // 云状2质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_03(999999); // 云状3质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_04(999999); // 云状4质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_05(999999); // 云状5质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_06(999999); // 云状6质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_07(999999); // 云状7质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_08(999999); // 云状8质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_11(999999); // 低云状质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_12(999999); // 中云状质量标志 ( 代码表 )
                                    HourValueTab.setQ20350_13(999999); // 高云状质量标志 ( 代码表 )
                                    HourValueTab.setQ20003(999999); // 现在天气质量标志 ( 代码表 )
                                    HourValueTab.setQ04080_05(999999); // 过去天气描述时间周期质量标志 ( 代码表 )
                                    HourValueTab.setQ20004(999999); // 过去天气1质量标志 ( 代码表 )
                                    HourValueTab.setQ20005(999999); // 过去天气2质量标志 ( 代码表 )
                                    HourValueTab.setQ20062(999999); // 地面状态质量标志 ( 代码表 )
                                    HourValueTab.setQ13013(999999); // 积雪深度质量标志 ( 代码表 )
                                    HourValueTab.setQ13330(999999); // 雪压质量标志 ( 代码表 )
                                    HourValueTab.setQ20330_01(999999); // 冻土第1冻结层上限值质量标志 ( 代码表 )
                                    HourValueTab.setQ20331_01(999999); // 冻土第1冻结层下限值质量标志 ( 代码表 )
                                    HourValueTab.setQ20330_02(999999); // 冻土第2冻结层上限值质量标志 ( 代码表 )
                                    HourValueTab.setQ20331_02(999999); // 冻土第2冻结层下限值质量标志 ( 代码表 )
                                    hourList.add(HourValueTab);
                                }
                            }

                        }
                        if (txtFileContent.get(i).startsWith("FZ")) {
                            List lists = new ArrayList();
                            List list1 = new ArrayList();
                            for (int j = i + 1; j < txtFileContent.size(); j++) {
                                String[] sz = txtFileContent.get(j).split("\\s+");
                                if (sz.length > 3) {
                                    continue;
                                } else {
                                    lists.add(sz[0] + "," + sz[1] + "," + sz[2]);
                                    if (sz[2].endsWith("=")) {
                                        break;
                                    }
                                }
                            }
                            for (int j = i + 1; j < txtFileContent.size(); j++) {
                                String[] sz = txtFileContent.get(j).split("\\s+");
                                if (sz.length <= 3) {
                                    continue;
                                } else {
                                    try {
                                        list1.add(txtFileContent.get(j).split("\\s+")[0] + "," + txtFileContent.get(j + 2).split("\\s+")[0]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[1] + "," + txtFileContent.get(j + 2).split("\\s+")[1]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[2] + "," + txtFileContent.get(j + 2).split("\\s+")[2]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[3] + "," + txtFileContent.get(j + 2).split("\\s+")[3]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[4] + "," + txtFileContent.get(j + 2).split("\\s+")[4]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[5] + "," + txtFileContent.get(j + 2).split("\\s+")[5]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[6] + "," + txtFileContent.get(j + 2).split("\\s+")[6]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[7] + "," + txtFileContent.get(j + 2).split("\\s+")[7]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[8] + "," + txtFileContent.get(j + 2).split("\\s+")[8]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[9] + "," + txtFileContent.get(j + 2).split("\\s+")[9]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[10] + "," + txtFileContent.get(j + 2).split("\\s+")[10]);
                                        list1.add(txtFileContent.get(j).split("\\s+")[11] + "," + txtFileContent.get(j + 2).split("\\s+")[11]);
                                    } catch (Exception e) {
                                        break;
                                    }
                                }
                            }
                            int day = 1;
                            int hour = 0;
                            for (int k = 0; k < list1.size(); k++) {
                                hour = hour + 1;
                                day = day + 1;
                                if (hour > 24) {
                                    hour = 1;
                                    day = day + 1;
                                }
                                String[] ms = list1.get(k).toString().split(",");
                                String uuid = UUID.randomUUID().toString();
                                HourValueTab HourValueTab = new HourValueTab();
                                HourValueTab.setD_MAIN_ID(uuid); // 记录标识 ( 系统自动生成的流水号 )
                                HourValueTab.setD_IYMDHM(sdf.format(calendar.getTime())); // 入库时间 ( 插表时的系统时间 )
                                HourValueTab.setD_RYMDHM(sdf.format(calendar.getTime())); // 收到时间 ( DPC消息生成时间 )
                                HourValueTab.setD_UPDATE_TIME(sdf.format(calendar.getTime())); // 更新时间 ( 根据CCx对记录更新时的系统时间 )
                                HourValueTab.setD_DATETIME(sdf.format(calendar.getTime()));
                                //HourValueTab.setD_DATETIME(messages[5] + "-" + messages[6]+ "-00 00:00:00"); // 资料时间 ( 由V04001、V04002、V04003、V04004 )
                                HourValueTab.setV_BBB("000"); // 更正标志 ( 组成 )
                                HourValueTab.setV01301(V01301); // 区站号(字符)
                                HourValueTab.setV01300(V01300); // 区站号(数字)
                                HourValueTab.setV05001(Float.parseFloat(V05001)); // 纬度
                                HourValueTab.setV06001(Float.parseFloat(V06001)); // 经度 ( 单位：度 )
                                HourValueTab.setV07001(Float.parseFloat(V07001)); // 测站高度 ( 单位：度 )
                                HourValueTab.setV07031(Float.parseFloat("999999")); // 气压传感器海拔高度 ( 单位：米 )
                                HourValueTab.setV07032_04(Float.parseFloat("999999")); // 风速传感器距地面高度 ( 单位：米 )
                                HourValueTab.setV02001("999999"); // 测站类型 ( 单位：米 )
                                HourValueTab.setV02301(999999); // 台站级别 ( 代码表 )
                                HourValueTab.setV_ACODE(999999); // 中国行政区划代码 ( 代码表 )
                                HourValueTab.setV04001(Integer.parseInt(messages[5])); // 资料观测年 ( 代码表 )
                                HourValueTab.setV04002(Integer.parseInt(messages[6])); // 资料观测月
                                HourValueTab.setV04003(day); // 资料观测日
                                HourValueTab.setV04004(hour); // 资料观测时
                                HourValueTab.setV10004(Float.parseFloat("999999")); // 本站气压
                                HourValueTab.setV10051(Float.parseFloat("999999")); // 海平面气压 ( 单位：百帕 )
                                HourValueTab.setV10061(Float.parseFloat("999999")); // 3小时变压 ( 单位：百帕 )
                                HourValueTab.setV10062(Float.parseFloat("999999")); // 24小时变压 ( 单位：百帕 )
                                HourValueTab.setV10301(Float.parseFloat("999999")); // 小时内最高本站气压 ( 单位：百帕 )
                                HourValueTab.setV10301_052(999999); // 小时内最高本站气压出现时间 ( 格式：hhmm )
                                HourValueTab.setV10302(Float.parseFloat("999999")); // 小时内最低本站气压 ( 单位：百帕  )
                                HourValueTab.setV10302_052(999999); // 小时内最低本站气压出现时间 ( 格式：hhmm )
                                HourValueTab.setV12001(Float.parseFloat("999999")); // 气温 ( 单位：摄氏度 )
                                HourValueTab.setV12011(Float.parseFloat("999999")); // 小时内最高气温 ( 单位：摄氏度 )
                                HourValueTab.setV12011_052(999999); // 小时内最高气温出现时间 ( 单位：摄氏度 )
                                HourValueTab.setV12012(Float.parseFloat("999999")); // 小时内最低气温 (单位：摄氏度  )
                                HourValueTab.setV12012_052(999999); // 小时内最低气温出现时间 ( 格式：hhmm )
                                HourValueTab.setV12405(Float.parseFloat("999999")); // 24小时变温 (单位：摄氏度  )
                                HourValueTab.setV12016(Float.parseFloat("999999")); // 过去24小时最高气温 ( 单位：摄氏度 )
                                HourValueTab.setV12017(Float.parseFloat("999999")); // 过去24小时最低气温 ( 单位：摄氏度 )
//                                HourValueTab.setBalltemp(Float.parseFloat("999999"));//湿球温度
                                HourValueTab.setV12003(Float.parseFloat("999999")); // 露点温度 ( 单位：摄氏度 )
                                HourValueTab.setV13003(999999); // 相对湿度 (单位：% )
                                HourValueTab.setV13007(Float.parseFloat("999999")); // 小时内最小相对湿度 ( 单位：% )
                                HourValueTab.setV13007_052(999999); // 小时内最小相对湿度出现时间 (格式：hhmm)
                                HourValueTab.setV13004(Float.parseFloat("999999")); // 水汽压 (单位：百帕 )
                                HourValueTab.setV13019(Float.parseFloat("999999")); // 1小时降水量 ( 单位：毫米   )
                                HourValueTab.setV13020(Float.parseFloat("999999")); // 过去3小时降水量 ( 单位：毫米 )
                                HourValueTab.setV13021(Float.parseFloat("999999")); // 过去6小时降水量 ( 单位：毫米 )
                                HourValueTab.setV13022(Float.parseFloat("999999")); // 过去12小时降水量 ( 单位：毫米 )
                                HourValueTab.setV13023(Float.parseFloat("999999")); // 过去24小时降水量 ( 单位：毫米 )
                                HourValueTab.setV04080_04(999999); // 人工加密观测降水量描述时间周期 ( 单位：毫米 )
                                HourValueTab.setV13011(Float.parseFloat("999999")); // 人工加密观测降水量 ( 单位：小时 )
                                HourValueTab.setV13033(Float.parseFloat("999999")); // 小时蒸发量 ( 单位：毫米 )
                                HourValueTab.setV11290("999999"); // 2分钟平均风向 ( 单位：毫米 )
                                HourValueTab.setV11291(Float.parseFloat("999999")); // 2分钟平均风速 ( 单位：度 )
                                HourValueTab.setV11292("999999"); // 10分钟平均风向 ( 单位：米/秒 )
                                try {
                                    if (ms[1].endsWith(".") || ms[1].endsWith("=")) {
                                        ms[1] = ms[1].substring(0, ms[1].length() - 1);
                                    }
                                    HourValueTab.setV11293(Float.parseFloat(ms[1])); // 10分钟平均风速 ( 单位：度 )
                                } catch (Exception e) {
                                    HourValueTab.setV11293(Float.parseFloat("999999")); // 10分钟平均风速 ( 单位：度 )
                                }
//                                HourValueTab.setV11293(Float.parseFloat(ms[1])); // 10分钟平均风速 ( 单位：度 )
                                HourValueTab.setV11296(ms[0]); // 小时内最大风速的风向 ( 单位：米/秒 )
                                HourValueTab.setV11042(Float.parseFloat("999999")); // 小时内最大风速 ( 单位：度 )
                                HourValueTab.setV11042_052(999999); // 小时内最大风速出现时间 ( 格式：hhmm )
                                HourValueTab.setV11201("999999"); // 瞬时风向 ( 单位：度 )
                                HourValueTab.setV11202(Float.parseFloat("999999")); // 瞬时风速 ( 单位：米/秒 )
                                HourValueTab.setV11211(Float.parseFloat("999999")); // 小时内极大风速的风向 ( 单位：米/秒 )
                                HourValueTab.setV11046(Float.parseFloat("999999")); // 小时内极大风速 ( 单位：度 )
                                HourValueTab.setV11046_052(999999); // 小时内极大风速出现时间 (格式：hhmm  )
                                HourValueTab.setV11503_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风向 (单位：度  )
                                HourValueTab.setV11504_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风速 (单位：米/秒  )
                                HourValueTab.setV11503_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风向 ( 单位：度  )
                                HourValueTab.setV11504_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风速 ( 单位：米/秒)
                                HourValueTab.setV12120(Float.parseFloat("999999")); // 地面温度 ( 单位：摄氏度 )
                                HourValueTab.setV12311(Float.parseFloat("999999")); // 小时内最高地面温度 ( 单位：摄氏度 )
                                HourValueTab.setV12311_052(999999); // 小时内最高地面温度出现时间 (  格式：hhmm)
                                HourValueTab.setV12121(Float.parseFloat("999999")); // 小时内最低地面温度 (单位：摄氏度  )
                                HourValueTab.setV12121_052(999999); // 小时内最低地面温度出现时间 ( 格式：hhmm)
                                HourValueTab.setV12013(Float.parseFloat("999999")); // 过去12小时最低地面温度 (单位：摄氏度  )
                                HourValueTab.setV12030_005(Float.parseFloat("999999")); // 5cm地温 ( 单位：摄氏度 )
                                HourValueTab.setV12030_010(Float.parseFloat("999999")); // 10cm地温 ( 单位：摄氏度 )
                                HourValueTab.setV12030_015(Float.parseFloat("999999")); // 15cm地温 ( 单位：摄氏度 )
                                HourValueTab.setV12030_020(Float.parseFloat("999999")); // 20cm地温 ( 单位：摄氏度 )
                                HourValueTab.setV12030_040(Float.parseFloat("999999")); // 40cm地温 ( 单位：摄氏度 )
                                HourValueTab.setV12030_080(Float.parseFloat("999999")); // 80cm地温 ( 单位：摄氏度 )
                                HourValueTab.setV12030_160(Float.parseFloat("999999")); // 160cm地温 ( 单位：摄氏度 )
                                HourValueTab.setV12030_320(Float.parseFloat("999999")); // 320cm地温 ( 单位：摄氏度 )
                                HourValueTab.setV12314(Float.parseFloat("999999")); // 草面（雪面）温度 ( 单位：摄氏度 )
                                HourValueTab.setV12315(Float.parseFloat("999999")); // 小时内草面（雪面）最高温度 ( 单位：摄氏度 )
                                HourValueTab.setV12315_052(999999); // 小时内草面（雪面）最高温度出现时间 ( 格式：hhmm )
                                HourValueTab.setV12316(Float.parseFloat("999999")); // 小时内草面（雪面）最低温度 ( 单位：摄氏度 )
                                HourValueTab.setV12316_052(999999); // 小时内草面（雪面）最低温度出现时间 ( 格式：hhmm  )
                                HourValueTab.setV20001_701_01(Float.parseFloat("999999")); // 1分钟平均水平能见度 ( 单位：米 )
                                HourValueTab.setV20001_701_10(Float.parseFloat("999999")); // 10分钟平均水平能见度 ( 单位：米 )
                                HourValueTab.setV20059(Float.parseFloat("999999")); // 最小水平能见度 ( 单位：米 )
                                HourValueTab.setV20059_052("999999"); // 最小水平能见度出现时间 ( 格式：hhmm  )
                                HourValueTab.setV20001(Float.parseFloat("999999")); // 水平能见度（人工） ( 单位：千米 )
//                                HourValueTab.setVisibility2(999999);//能见度级别
                                HourValueTab.setV20010(999999); // 总云量 (单位：%  )
                                HourValueTab.setV20051(999999); // 低云量 ( 单位：% )
                                HourValueTab.setV20011(Float.parseFloat("999999")); // 低云或中云的云量 ( 单位：% )
                                HourValueTab.setV20013(Float.parseFloat("999999")); // 低云或中云的云高 ( 单位：% )
                                HourValueTab.setV20350_01(999999); // 云状1 ( 单位：米 )
                                HourValueTab.setV20350_02("999999"); // 云状2 ( 代码表 )
                                HourValueTab.setV20350_03(999999); // 云状3 ( 代码表 )
                                HourValueTab.setV20350_04(999999); // 云状4 ( 代码表 )
                                HourValueTab.setV20350_05(999999); // 云状5 ( 代码表 )
                                HourValueTab.setV20350_06(999999); // 云状6 ( 代码表 )
                                HourValueTab.setV20350_07(999999); // 云状7 ( 代码表 )
                                HourValueTab.setV20350_08(999999); // 云状8 ( 代码表 )
                                HourValueTab.setV20350_11(999999); // 低云状 ( 代码表 )
                                HourValueTab.setV20350_12(999999); // 中云状 ( 代码表 )
                                HourValueTab.setV20350_13(999999); // 高云状 ( 代码表 )
                                HourValueTab.setV20003(999999); // 现在天气 ( 代码表 )
                                HourValueTab.setV04080_05(999999); // 过去天气描述时间周期 ( 代码表 )
                                HourValueTab.setV20004(999999); // 过去天气1 ( 单位:小时 )
                                HourValueTab.setV20005(999999); // 过去天气2 ( 代码表 )
                                HourValueTab.setV20062(999999); // 地面状态 ( 代码表 )
                                HourValueTab.setV13013(Float.parseFloat("999999")); // 积雪深度 ( 代码表 )
                                HourValueTab.setV13330(Float.parseFloat("999999")); // 雪压 ( 单位：厘米 )
                                HourValueTab.setV20330_01(Float.parseFloat("999999")); // 冻土第1冻结层上限值 ( 单位：g/cm2 )
                                HourValueTab.setV20331_01(Float.parseFloat("999999")); // 冻土第1冻结层下限值 ( 单位：厘米 )
                                HourValueTab.setV20330_02(Float.parseFloat("999999")); // 冻土第2冻结层上限值 ( 单位：厘米 )
                                HourValueTab.setV20331_02(Float.parseFloat("999999")); // 冻土第2冻结层下限值 ( 单位：厘米 )
                                HourValueTab.setQ10004(999999); // 本站气压质量标志 ( 单位：厘米 )
                                HourValueTab.setQ10051(999999); // 海平面气压质量标志 ( 代码表 )
                                HourValueTab.setQ10061(999999); // 3小时变压质量标志 ( 代码表 )
                                HourValueTab.setQ10062(999999); // 24小时变压质质量标志 ( 代码表 )
                                HourValueTab.setQ10301(999999); // 小时内最高本站气压质量标志 ( 代码表 )
                                HourValueTab.setQ10301_052(999999); // 小时内最高本站气压出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ10302(999999); // 小时内最低本站气压质量标志 ( 代码表 )
                                HourValueTab.setQ10302_052(999999); // 小时内最低本站气压出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ12001(999999); // 气温质量标志 ( 代码表 )
                                HourValueTab.setQ12011(999999); // 小时内最高气温质量标志 ( 代码表 )
                                HourValueTab.setQ12011_052(999999); // 小时内最高气温出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ12012(999999); // 小时内最低气温质量标志 ( 代码表 )
                                HourValueTab.setQ12012_052(999999); // 小时内最低气温出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ12405(999999); // 24小时变温质量标志 ( 代码表 )
                                HourValueTab.setQ12016(999999); // 过去24小时最高气温质量标志 ( 代码表 )
                                HourValueTab.setQ12017(999999); // 过去24小时最低气温质量标志 ( 代码表 )
                                HourValueTab.setQ12003(999999); // 露点温度质量标志 ( 代码表 )
                                HourValueTab.setQ13003(999999); // 相对湿度质量标志 ( 代码表 )
                                HourValueTab.setQ13007(999999); // 小时内最小相对湿度质量标志 ( 代码表 )
                                HourValueTab.setQ13007_052(999999); // 小时内最小相对湿度出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ13004(999999); // 水汽压质量标志 ( 代码表 )
                                HourValueTab.setQ13019(999999); // 1小时降水量质量标志 ( 代码表 )
                                HourValueTab.setQ13020(999999); // 过去3小时降水量质量标志 ( 代码表 )
                                HourValueTab.setQ13021(999999); // 过去6小时降水量质量标志 ( 代码表 )
                                HourValueTab.setQ13022(999999); // 过去12小时降水量质量标志 ( 代码表 )
                                HourValueTab.setQ13023(999999); // 24小时降水量质 ( 代码表 )
                                HourValueTab.setQ04080_04(999999); // 人工加密观测降水量描述时间周期质量标志 ( 代码表 )
                                HourValueTab.setQ13011(999999); // 人工加密观测降水量质量标志 ( 代码表 )
                                HourValueTab.setQ13033(999999); // 小时蒸发量质量标志 ( 代码表 )
                                HourValueTab.setQ11290(999999); // 2分钟平均风向质量标志 ( 代码表 )
                                HourValueTab.setQ11291(999999); // 2分钟平均风速质量标志 ( 代码表 )
                                HourValueTab.setQ11292(999999); // 10分钟平均风向质量标志 ( 代码表 )
                                HourValueTab.setQ11293(999999); // 10分钟平均风速质量标志 ( 代码表 )
                                HourValueTab.setQ11296(999999); // 小时内最大风速的风向质量标志 ( 代码表 )
                                HourValueTab.setQ11042(999999); // 小时内最大风速质量标志 ( 代码表 )
                                HourValueTab.setQ11042_052(999999); // 小时内最大风速出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ11201(9999990); // 瞬时风向质量标志 ( 代码表 )
                                HourValueTab.setQ11202(999999); // 瞬时风速质量标志 ( 代码表 )
                                HourValueTab.setQ11211(999999); // 小时内极大风速的风向质量标志 ( 代码表 )
                                HourValueTab.setQ11046(999999); // 小时内极大风速质量标志 ( 代码表 )
                                HourValueTab.setQ11046_052(999999); // 小时内极大风速出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ11503_06(999999); // 过去6小时极大瞬时风向质量标志 ( 代码表 )
                                HourValueTab.setQ11504_06(999999); // 过去6小时极大瞬时风速质量标志 ( 代码表 )
                                HourValueTab.setQ11503_12(999999); // 过去12小时极大瞬时风向质量标志 ( 代码表 )
                                HourValueTab.setQ11504_12(999999); // 过去12小时极大瞬时风速质量标志 ( 代码表 )
                                HourValueTab.setQ12120(999999); // 地面温度质量标志 ( 代码表 )
                                HourValueTab.setQ12311(999999); // 小时内最高地面温度质量标志 ( 代码表 )
                                HourValueTab.setQ12311_052(999999); // 小时内最高地面温度出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ12121(999999); // 小时内最低地面温度质量标志 ( 代码表 )
                                HourValueTab.setQ12121_052(999999); // 小时内最低地面温度出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ12013(999999); // 过去12小时最低地面温度 ( 代码表 )
                                HourValueTab.setQ12030_005(999999); // 5cm地温质量标志 ( 代码表 )
                                HourValueTab.setQ12030_010(999999); // 10cm地温质量标志 ( 代码表 )
                                HourValueTab.setQ12030_015(999999); // 15cm地温质量标志 ( 代码表 )
                                HourValueTab.setQ12030_020(999999); // 20cm地温质量标志 ( 代码表 )
                                HourValueTab.setQ12030_040(999999); // 40cm地温质量标志 ( 代码表 )
                                HourValueTab.setQ12030_080(999999); // 80cm地温质量标志 ( 代码表 )
                                HourValueTab.setQ12030_160(999999); // 160cm地温质量标志 ( 代码表 )
                                HourValueTab.setQ12030_320(999999); // 320cm地温质量标志 ( 代码表 )
                                HourValueTab.setQ12314(999999); // 草面（雪面）温度质量标志 ( 代码表 )
                                HourValueTab.setQ12315(999999); // 小时内草面（雪面）最高温度质量标志 ( 代码表 )
                                HourValueTab.setQ12315_052(999999); // 小时内草面（雪面）最高温度出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ12316(999999); // 小时内草面（雪面）最低温度质量标志 ( 代码表 )
                                HourValueTab.setQ12316_052(999999); // 小时内草面（雪面）最低温度出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ20001_701_01(999999); // 1分钟平均水平能见度质量标志 ( 代码表 )
                                HourValueTab.setQ20001_701_10(999999); // 10分钟平均水平能见度质量标志 ( 代码表 )
                                HourValueTab.setQ20059(999999); // 最小水平能见度质量标志 ( 代码表 )
                                HourValueTab.setQ20059_052(999999); // 最小水平能见度出现时间质量标志 ( 代码表 )
                                HourValueTab.setQ20001(999999); // 水平能见度（人工）质量标志 ( 代码表 )
                                HourValueTab.setQ20010(999999); // 总云量质量标志 ( 代码表 )
                                HourValueTab.setQ20051(999999); // 低云量质量标志 ( 代码表 )
                                HourValueTab.setQ20011(999999); // 低云或中云的云量质量标志 ( 代码表 )
                                HourValueTab.setQ20013(999999); // 低云或中云的云高质量标志 ( 代码表 )
                                HourValueTab.setQ20350_01(999999); // 云状1质量标志 ( 代码表 )
                                HourValueTab.setQ20350_02(999999); // 云状2质量标志 ( 代码表 )
                                HourValueTab.setQ20350_03(999999); // 云状3质量标志 ( 代码表 )
                                HourValueTab.setQ20350_04(999999); // 云状4质量标志 ( 代码表 )
                                HourValueTab.setQ20350_05(999999); // 云状5质量标志 ( 代码表 )
                                HourValueTab.setQ20350_06(999999); // 云状6质量标志 ( 代码表 )
                                HourValueTab.setQ20350_07(999999); // 云状7质量标志 ( 代码表 )
                                HourValueTab.setQ20350_08(999999); // 云状8质量标志 ( 代码表 )
                                HourValueTab.setQ20350_11(999999); // 低云状质量标志 ( 代码表 )
                                HourValueTab.setQ20350_12(999999); // 中云状质量标志 ( 代码表 )
                                HourValueTab.setQ20350_13(999999); // 高云状质量标志 ( 代码表 )
                                HourValueTab.setQ20003(999999); // 现在天气质量标志 ( 代码表 )
                                HourValueTab.setQ04080_05(999999); // 过去天气描述时间周期质量标志 ( 代码表 )
                                HourValueTab.setQ20004(999999); // 过去天气1质量标志 ( 代码表 )
                                HourValueTab.setQ20005(999999); // 过去天气2质量标志 ( 代码表 )
                                HourValueTab.setQ20062(999999); // 地面状态质量标志 ( 代码表 )
                                HourValueTab.setQ13013(999999); // 积雪深度质量标志 ( 代码表 )
                                HourValueTab.setQ13330(999999); // 雪压质量标志 ( 代码表 )
                                HourValueTab.setQ20330_01(999999); // 冻土第1冻结层上限值质量标志 ( 代码表 )
                                HourValueTab.setQ20331_01(999999); // 冻土第1冻结层下限值质量标志 ( 代码表 )
                                HourValueTab.setQ20330_02(999999); // 冻土第2冻结层上限值质量标志 ( 代码表 )
                                HourValueTab.setQ20331_02(999999); // 冻土第2冻结层下限值质量标志 ( 代码表 )
                                hourList.add(HourValueTab);
                            }
                            for (int k = 0; k < lists.size(); k++) {
                                day = day + 1;
                                DayValueTab dayValueTab = new DayValueTab();
                                String[] ms = lists.get(k).toString().split(",");
                                if (ms[2].endsWith(".") || ms[2].endsWith("=")) {
                                    ms[2] = ms[2].substring(0, ms[2].length() - 1);
                                }
                                String uuid = UUID.randomUUID().toString();
                                dayValueTab.setD_RECORD_ID(uuid); // 记录标识 ( 系统自动生成的流水号 )
                                dayValueTab.setD_IYMDHM(sdf.format(calendar.getTime())); // 入库时间 ( 插表时的系统时间 )
                                dayValueTab.setD_RYMDHM(sdf.format(calendar.getTime())); // 收到时间 ( DPC消息生成时间 )
                                dayValueTab.setD_UPDATE_TIME(sdf.format(calendar.getTime())); // 更新时间 ( 根据CCx对记录更新时的系统时间 )
                                dayValueTab.setD_DATETIME(sdf.format(calendar.getTime()));
                                //dayValueTab.setD_DATETIME(messages[5] + "-" + messages[6] + "-" + day + " 00:00:00"); // 资料时间 ( 由V04001、V04002、V04003组成 )
                                dayValueTab.setV_BBB("000"); // 更正报标志
                                dayValueTab.setV01301(V01301); // 区站号(字符)
                                dayValueTab.setV01300(V01300); // 区站号(数字)
                                dayValueTab.setV05001(Float.parseFloat(V05001)); // 纬度
                                dayValueTab.setV06001(Float.parseFloat(V06001)); // 经度 ( 单位：度 )
                                dayValueTab.setV07001(Float.parseFloat(V07001)); // 测站高度 ( 单位：度 )
                                dayValueTab.setV07031(Float.parseFloat("999999")); // 气压传感器海拔高度 ( 单位：米 )
                                dayValueTab.setV02301(999999); // 台站级别 ( 代码表 )
                                dayValueTab.setV_ACODE(999999); // 中国行政区划代码 ( 代码表 )
                                dayValueTab.setV04001(Integer.parseInt(messages[5]));// 年
                                dayValueTab.setV04002(Integer.parseInt(messages[6])); // 月
                                dayValueTab.setV04003(day); // 日
                                dayValueTab.setV12303_701(Float.parseFloat("999999"));
                                dayValueTab.setV13004_MAX(Float.parseFloat("999999"));
                                dayValueTab.setV13004_MIN(Float.parseFloat("999999"));
                                dayValueTab.setHtem100(Float.parseFloat("999999"));
                                dayValueTab.setHtem200(Float.parseFloat("999999"));
                                dayValueTab.setHtem300(Float.parseFloat("999999"));
                                dayValueTab.setObversetype("999999");
                                dayValueTab.setObsercecode("999999");
                                dayValueTab.setArcaninehight("999999");
                                dayValueTab.setRain1(Float.parseFloat("999999"));
                                dayValueTab.setRain10(Float.parseFloat("999999"));
                                dayValueTab.setWetherEndTime("999999");
                                dayValueTab.setWetherStartTime("999999");
                                dayValueTab.setWetherSymbol("999999");
                                dayValueTab.setGlazeNSDia(999999);
                                dayValueTab.setGlazeNSHight(999999);
                                dayValueTab.setGlazeNSWeight(999999);
                                dayValueTab.setGlazeWEDia(999999);
                                dayValueTab.setGlazeNSDia(999999);
                                dayValueTab.setGlazeWEHight(999999);
                                dayValueTab.setGlazeWEWeight(999999);
                                dayValueTab.setV10004_701(Float.parseFloat("999999")); // 日平均本站气压 ( 单位：百帕 )
                                dayValueTab.setV10301(Float.parseFloat("999999")); // 日最高本站气压 ( 单位：百帕 )
                                dayValueTab.setV10301_052("999999"); // 日最高本站气压出现时间 ( 格式：时分 )
                                dayValueTab.setV10302(Float.parseFloat("999999")); // 日最低本站气压 ( 单位：百帕 )
                                dayValueTab.setV10302_052("999999"); // 日最低本站气压出现时间 ( 格式：时分 )
                                dayValueTab.setV10051_701(Float.parseFloat("999999")); // 日平均海平面气压 ( 单位：百帕 )
                                dayValueTab.setV12001_701(Float.parseFloat("999999")); // 日平均气温 ( 单位：摄氏度 )
                                dayValueTab.setV12011(Float.parseFloat("999999")); // 日最高气温 ( 单位：摄氏度 )
                                dayValueTab.setV12011_052("999999"); // 日最高气温出现时间 ( 格式：时分 )
                                dayValueTab.setV12012(Float.parseFloat("999999")); // 日最低气温 ( 单位：摄氏度 )
                                dayValueTab.setV12012_052("999999"); // 日最低气温出现时间 ( 格式：时分 )
                                dayValueTab.setV13004_701(Float.parseFloat("999999")); // 日平均水汽压 ( 单位：百帕 )
                                dayValueTab.setV13003_701(999999); // 日平均相对湿度 ( 单位：% )
                                dayValueTab.setV13007(999999); // 日最小相对湿度 ( 单位：% )
                                dayValueTab.setV13007_052("999999"); // 日最小相对湿度出现时间 ( 格式：时分 )
                                dayValueTab.setV20010_701(Integer.parseInt("999999")); // 日平均总云量 ( 单位：% )
                                dayValueTab.setV20051_701(Integer.parseInt("999999")); // 日平均低云量 ( 单位：% )
                                dayValueTab.setV20059(Float.parseFloat("999999")); // 日最小水平能见度 ( 单位：米 )
                                dayValueTab.setV20059_052("999999"); // 日最小水平能见度出现时间 ( 格式：时分 )
                                dayValueTab.setV13302_01(Float.parseFloat("999999")); // 日小时最大降水量 ( 单位：毫米  )
                                dayValueTab.setV13302_01_052(999999); // 日小时最大降水量出现时间 ( 格式：时分 )
                                dayValueTab.setV13308(Float.parseFloat("999999")); // 20-08时雨量筒观测降水量 ( 单位：毫米 )
                                dayValueTab.setV13309(Float.parseFloat("999999")); // 08-20时雨量筒观测降水量 ( 单位：毫米 )
                                dayValueTab.setV13305(Float.parseFloat("999999")); // 20-20时降水量 ( 单位：毫米 )
                                dayValueTab.setV13306(Float.parseFloat("999999")); // 08-08时降水量 ( 单位：毫米 )
                                dayValueTab.setV13032(Float.parseFloat("999999")); // 日蒸发量（小型） ( 单位：毫米 )
                                dayValueTab.setV13033(Float.parseFloat("999999")); // 日蒸发量（大型） ( 单位：毫米 )
                                dayValueTab.setV13013(Float.parseFloat("999999")); // 积雪深度 ( 单位：厘米 )
                                dayValueTab.setV13330(Float.parseFloat("999999")); // 雪压 ( 单位：g/cm2 )
                                dayValueTab.setV20305(999999); // 电线积冰-现象 ( 代码表 )
                                dayValueTab.setV20326_NS(Float.parseFloat("999999")); // 电线积冰-南北方向直径 ( 单位：毫米 )
                                dayValueTab.setV20306_NS(Float.parseFloat("999999")); // 电线积冰-南北方向厚度 ( 单位：毫米 )
                                dayValueTab.setV20307_NS(Float.parseFloat("999999")); // 电线积冰-南北方向重量 ( 单位：克 )
                                dayValueTab.setV20326_WE(Float.parseFloat("999999")); // 电线积冰-东西方向直径 ( 单位：毫米 )
                                dayValueTab.setV20306_WE(Float.parseFloat("999999")); // 电线积冰-东西方向厚度 ( 单位：毫米 )
                                dayValueTab.setV20307_WE(Float.parseFloat("999999")); // 电线积冰-东西方向重量 ( 单位：克 )
                                dayValueTab.setV12001(Float.parseFloat("999999")); // 电线积冰－温度 ( 单位：摄氏度 )
                                dayValueTab.setV11001("999999"); // 电线积冰－风向 ( 单位：度 )
                                dayValueTab.setV11002(Float.parseFloat("999999")); // 电线积冰－风速 ( 单位：米/秒 )
                                dayValueTab.setV11290_CHAR("999999"); // 逐小时2分钟平均风向 ( 格式：字符串  )
                                dayValueTab.setV11291_701(Float.parseFloat("999999")); // 日平均2分钟风速 ( 单位：米/秒 )
                                dayValueTab.setV11293_701(Float.parseFloat("999999")); // 日平均10分钟风速 ( 单位：米/秒 )
                                dayValueTab.setV11296(ms[1]); // 日最大风速的风向 ( 单位：度 )
                                dayValueTab.setV11042(Float.parseFloat(ms[0])); // 日最大风速 ( 单位：米/秒 )
                                dayValueTab.setV11042_052(ms[2]); // 日最大风速出现时间 ( 格式：时分 )
                                dayValueTab.setV11211("999999"); // 日极大风速的风向 ( 单位：度 )
                                dayValueTab.setV11046(Float.parseFloat("999999")); // 日极大风速 ( 单位：米/秒 )
                                dayValueTab.setV11046_052("999999"); // 日极大风速出现时间 ( 格式：时分 )
                                dayValueTab.setV12120_701(Float.parseFloat("999999")); // 日平均地面温度 ( 单位：摄氏度 )
                                dayValueTab.setV12311(Float.parseFloat("999999")); // 日最高地面温度 ( 单位：摄氏度 )
                                dayValueTab.setV12311_052("999999"); // 日最高地面温度出现时间  ( 格式：时分 )
                                dayValueTab.setV12121(Float.parseFloat("999999")); // 日最低地面温度 ( 单位：摄氏度 )
                                dayValueTab.setV12121_052("999999"); // 日最低地面温度出现时间 ( 格式：时分 )
                                dayValueTab.setV12030_701_005(Float.parseFloat("999999")); // 日平均5cm地温 ( 单位：摄氏度 )
                                dayValueTab.setV12030_701_010(Float.parseFloat("999999")); // 日平均10cm地温 ( 单位：摄氏度 )
                                dayValueTab.setV12030_701_015(Float.parseFloat("999999")); // 日平均15cm地温 ( 单位：摄氏度 )
                                dayValueTab.setV12030_701_020(Float.parseFloat("999999")); // 日平均20cm地温 ( 单位：摄氏度 )
                                dayValueTab.setV12030_701_040(Float.parseFloat("999999")); // 日平均40cm地温 ( 单位：摄氏度 )
                                dayValueTab.setV12030_701_080(Float.parseFloat("999999")); // 日平均80cm地温 ( 单位：摄氏度 )
                                dayValueTab.setV12030_701_160(Float.parseFloat("999999")); // 日平均160cm地温 ( 单位：摄氏度 )
                                dayValueTab.setV12030_701_320(Float.parseFloat("999999")); // 日平均320cm地温 ( 单位：摄氏度 )
                                dayValueTab.setV20330_01(Float.parseFloat("999999")); // 第一冻土层上界值 ( 单位：厘米 )
                                dayValueTab.setV20331_01(Float.parseFloat("999999")); // 第一冻土层下界值 ( 单位：厘米 )
                                dayValueTab.setV20330_02(Float.parseFloat("999999")); // 第二冻土层上界值 ( 单位：厘米 )
                                dayValueTab.setV20331_02(Float.parseFloat("999999")); // 第二冻土层下界值 ( 单位：厘米 )
                                dayValueTab.setV14032(Float.parseFloat("999999")); // 日总日照时数 ( 单位：小时 )
                                dayValueTab.setV20411("999999"); // 日出时间 ( 格式：时分 )
                                dayValueTab.setV20412("999999"); // 日落时间 ( 格式：时分 )
                                dayValueTab.setV12314_701(Float.parseFloat("999999")); // 日平均草面（雪面）温度 ( 单位：摄氏度 )
                                dayValueTab.setV12315(Float.parseFloat("999999")); // 日草面（雪面）最高温度 ( 单位：摄氏度 )
                                dayValueTab.setV12315_052("999999"); // 日草面（雪面）最高温度出现时间 ( 格式：时分 )
                                dayValueTab.setV12316(Float.parseFloat("999999")); // 日草面（雪面）最低温度 ( 单位：摄氏度 )
                                dayValueTab.setV12316_052("999999"); // 日草面（雪面）最低温度出现时间 ( 格式：时分 )
                                dayValueTab.setV20062(999999); // 地面状态（0-31数字） ( 见代码表020062 )
                                dayValueTab.setV20302_060(999999); // 雨 ( 代码表20302(下同) )
                                dayValueTab.setV20302_060_052("999999"); // 雨出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_070(999999); // 雪 ( 代码表 )
                                dayValueTab.setV20302_070_052("999999"); // 雪出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_089(999999); // 冰雹 ( 代码表 )
                                dayValueTab.setV20302_089_052("999999"); // 冰雹出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_042(999999); // 雾 ( 代码表 )
                                dayValueTab.setV20302_042_052("999999"); // 雾出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_010(999999); // 轻雾 ( 代码表 )
                                dayValueTab.setV20302_001(999999); // 露 ( 代码表 )
                                dayValueTab.setV20302_002(999999); // 霜 ( 代码表 )
                                dayValueTab.setV20302_056(999999); // 雨凇 ( 代码表 )
                                dayValueTab.setV20302_056_052("999999"); // 雨凇出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_048(999999); // 雾凇 ( 代码表 )
                                dayValueTab.setV20302_048_052("999999"); // 雾凇出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_038(999999); // 吹雪 ( 代码表 )
                                dayValueTab.setV20302_038_052("999999"); // 吹雪出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_039(999999); // 雪暴 ( 代码表 )
                                dayValueTab.setV20302_039_052("999999"); // 雪暴出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_019(999999); // 龙卷风 ( 代码表 )
                                dayValueTab.setV20302_019_052("999999"); // 龙卷风出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_016(999999); // 积雪 ( 代码表 )
                                dayValueTab.setV20302_003(999999); // 结冰 ( 代码表 )
                                dayValueTab.setV20302_031(999999); // 沙尘暴 ( 代码表 )
                                dayValueTab.setV20302_031_052("999999"); // 沙尘暴出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_007(999999); // 扬沙 ( 代码表 )
                                dayValueTab.setV20302_007_052("999999"); // 扬沙出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_006(999999); // 浮尘 ( 代码表 )
                                dayValueTab.setV20302_006_052("999999"); // 浮尘出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_004(999999); // 烟 ( 代码表 )
                                dayValueTab.setV20302_005(999999); // 霾 ( 代码表 )
                                dayValueTab.setV20302_008(999999); // 尘卷风 ( 代码表 )
                                dayValueTab.setV20302_076(999999); // 冰针 ( 代码表 )
                                dayValueTab.setV20302_017(999999); // 雷暴 ( 代码表 )
                                dayValueTab.setV20302_017_052("999999"); // 雷暴出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_013(999999); // 闪电 ( 代码表 )
                                dayValueTab.setV20302_014(999999); // 极光 ( 代码表 )
                                dayValueTab.setV20302_014_052("999999"); // 极光出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_015(999999); // 大风 ( 代码表 )
                                dayValueTab.setV20302_015_052("999999"); // 大风出现时间 ( 格式：字符串 )
                                dayValueTab.setV20302_018(999999); // 飑 ( 代码表 )
                                dayValueTab.setV20302_018_052("999999"); // 飑出现时间 ( 格式：字符串 )
                                dayValueTab.setV20303("999999"); // 天气现象摘要 ( 字符 )
                                dayValueTab.setV20304("999999"); // 天气现象记录 ( 字符 )
                                dayValueTab.setQ10004_701(999999); // 日平均本站气压质控码 ( 代码表 )
                                dayValueTab.setQ10301(999999); // 日最高本站气压质控码 ( 代码表 )
                                dayValueTab.setQ10301_052(999999); // 日最高本站气压出现时间质控码 ( 代码表 )
                                dayValueTab.setQ10302(999999); // 日最低本站气压质控码 ( 代码表 )
                                dayValueTab.setQ10302_052(999999); // 日最低本站气压出现时间质控码 ( 代码表 )
                                dayValueTab.setQ10051_701(999999); // 日平均海平面气压质控码 ( 代码表 )
                                dayValueTab.setQ12001_701(999999); // 日平均气温质控码 ( 代码表 )
                                dayValueTab.setQ12011(999999); // 日最高气温质控码 ( 代码表 )
                                dayValueTab.setQ12011_052(999999); // 日最高气温出现时间质控码 ( 代码表 )
                                dayValueTab.setQ12012(999999); // 日最低气温质控码 ( 代码表 )
                                dayValueTab.setQ12012_052(999999); // 日最低气温出现时间质控码 ( 代码表 )
                                dayValueTab.setQ13004_701(999999); // 日平均水汽压质控码 ( 代码表 )
                                dayValueTab.setQ13003_701(999999); // 日平均相对湿度质控码 ( 代码表 )
                                dayValueTab.setQ13007(999999); // 日最小相对湿度质控码 ( 代码表 )
                                dayValueTab.setQ13007_052(999999); // 日最小相对湿度出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20010_701(999999); // 日平均总云量质控码 ( 代码表 )
                                dayValueTab.setQ20051_701(999999); // 日平均低云量质控码 ( 代码表 )
                                dayValueTab.setQ20059(999999); // 日最小水平能见度质控码 ( 代码表 )
                                dayValueTab.setQ20059_052(999999); // 日最小水平能见度出现时间质控码 ( 代码表 )
                                dayValueTab.setQ13302_01(999999); // 日小时最大降水量质控码 ( 代码表 )
                                dayValueTab.setQ13302_01_052(999999); // 日小时最大降水量出现时间质控码 ( 代码表 )
                                dayValueTab.setQ13308(999999); // 20-08时雨量筒观测降水量质控码 ( 代码表 )
                                dayValueTab.setQ13309(999999); // 08-20时雨量筒观测降水量质控码 ( 代码表 )
                                dayValueTab.setQ13305(999999); // 20-20时降水量质控码 ( 代码表 )
                                dayValueTab.setQ13306(999999); // 08-08时降水量质控码 ( 代码表 )
                                dayValueTab.setQ13032(999999); // 日蒸发量（小型）质控码 ( 代码表 )
                                dayValueTab.setQ13033(999999); // 日蒸发量（大型）质控码 ( 代码表 )
                                dayValueTab.setQ13013(999999); // 积雪深度质控码 ( 代码表 )
                                dayValueTab.setQ13330(999999); // 雪压质控码 ( 代码表 )
                                dayValueTab.setQ20305(999999); // 电线积冰-现象质控码 ( 代码表 )
                                dayValueTab.setQ20326_NS(999999); // 电线积冰-南北方向直径质控码 ( 代码表 )
                                dayValueTab.setQ20306_NS(999999); // 电线积冰-南北方向厚度质控码( 代码表 )
                                dayValueTab.setQ20307_NS(999999); // 电线积冰-南北方向重量质控码 ( 代码表 )
                                dayValueTab.setQ20326_WE(999999); // 电线积冰-东西方向直径质控码 ( 代码表 )
                                dayValueTab.setQ20306_WE(999999); // 电线积冰-东西方向厚度质控码 ( 代码表 )
                                dayValueTab.setQ20307_WE(999999); // 电线积冰-东西方向重量质控码 ( 代码表 )
                                dayValueTab.setQ12001(999999); // 电线积冰－温度质控码 ( 代码表 )
                                dayValueTab.setQ11001(999999); // 电线积冰－风向质控码 ( 代码表 )
                                dayValueTab.setQ11002(999999); // 电线积冰－风速质控码 ( 代码表 )
                                dayValueTab.setQ11290_CHAR(999999); // 日平均2分钟风向质控码 ( 代码表 )
                                dayValueTab.setQ11291_701(999999); // 日平均2分钟风速质控码 ( 代码表 )
                                dayValueTab.setQ11293_701(999999); // 日平均10分钟风速质控码 ( 代码表 )
                                dayValueTab.setQ11296(999999); // 日最大风速的风向质控码 ( 代码表 )
                                dayValueTab.setQ11042(999999); // 日最大风速质控码 ( 代码表 )
                                dayValueTab.setQ11042_052(999999); // 日最大风速出现时间质控码 ( 代码表 )
                                dayValueTab.setQ11211(999999); // 日极大风速的风向质控码 ( 代码表 )
                                dayValueTab.setQ11046(999999); // 日极大风速质控码 ( 代码表 )
                                dayValueTab.setQ11046_052(999999); // 日极大风速出现时间质控码 ( 代码表 )
                                dayValueTab.setQ12120_701(999999); // 日平均地面温度质控码 ( 代码表 )
                                dayValueTab.setQ12311(999999); // 日最高地面温度质控码 ( 代码表 )
                                dayValueTab.setQ12311_052(999999); // 日最高地面温度出现时间质控码 ( 代码表 )
                                dayValueTab.setQ12121(999999); // 日最低地面温度质控码 ( 代码表 )
                                dayValueTab.setQ12121_052(999999); // 日最低地面温度出现时间质控码 ( 代码表 )
                                dayValueTab.setQ12030_701_005(999999); // 日平均5cm地温质控码 ( 代码表 )
                                dayValueTab.setQ12030_701_010(999999); // 日平均10cm地温质控码 ( 代码表 )
                                dayValueTab.setQ12030_701_015(999999); // 日平均15cm地温质控码 ( 代码表 )
                                dayValueTab.setQ12030_701_020(999999); // 日平均20cm地温质控码 ( 代码表 )
                                dayValueTab.setQ12030_701_040(999999); // 日平均40cm地温质控码 ( 代码表 )
                                dayValueTab.setQ12030_701_080(999999); // 日平均80cm地温质控码 ( 代码表 )
                                dayValueTab.setQ12030_701_160(999999); // 日平均160cm地温质控码 ( 代码表 )
                                dayValueTab.setQ12030_701_320(999999); // 日平均320cm地温质控码 ( 代码表 )
                                dayValueTab.setQ20330_01(999999); // 第一冻土层上界值质控码 ( 代码表 )
                                dayValueTab.setQ20331_01(999999); // 第一冻土层下界值质控码 ( 代码表 )
                                dayValueTab.setQ20330_02(999999); // 第二冻土层上界值质控码 ( 代码表 )
                                dayValueTab.setQ20331_02(999999); // 第二冻土层下界值质控码 ( 代码表 )
                                dayValueTab.setQ14032(999999); // 日总日照时数质控码 ( 代码表 )
                                dayValueTab.setQ20411(999999); // 日出时间质控码 ( 代码表 )
                                dayValueTab.setQ20412(999999); // 日落时间质控码 ( 代码表 )
                                dayValueTab.setQ12314_701(999999); // 日平均草面（雪面）温度质控码 ( 代码表 )
                                dayValueTab.setQ12315(999999); // 日草面（雪面）最高温度质控码 ( 代码表 )
                                dayValueTab.setQ12315_052(999999); // 日草面（雪面）最高温度出现时间质控码 ( 代码表 )
                                dayValueTab.setQ12316(999999); // 日草面（雪面）最低温度质控码 ( 代码表 )
                                dayValueTab.setQ12316_052(999999); // 日草面（雪面）最低温度出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20062(999999); // 地面状态质控码 ( 代码表 )
                                dayValueTab.setQ20302_060(999999); // 雨质控码 ( 代码表 )
                                dayValueTab.setQ20302_060_052(999999); // 雨出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_070(999999); // 雪质控码 ( 代码表 )
                                dayValueTab.setQ20302_070_052(999999); // 雪出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_089(999999); // 冰雹质控码 ( 代码表 )
                                dayValueTab.setQ20302_089_052(999999); // 冰雹出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_042(999999); // 雾质控码 ( 代码表 )
                                dayValueTab.setQ20302_042_052(999999); // 雾出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_010(999999); // 轻雾质控码 ( 代码表 )
                                dayValueTab.setQ20302_001(999999); // 露质控码 ( 代码表 )
                                dayValueTab.setQ20302_002(999999); // 霜质控码 ( 代码表 )
                                dayValueTab.setQ20302_056(999999); // 雨凇质控码 ( 代码表 )
                                dayValueTab.setQ20302_056_052(999999); // 雨凇出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_048(999999); // 雾凇质控码 ( 代码表 )
                                dayValueTab.setQ20302_048_052(999999); // 雾凇出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_038(999999); // 吹雪质控码 ( 代码表 )
                                dayValueTab.setQ20302_038_052(999999); // 吹雪出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_039(999999); // 雪暴质控码 ( 代码表 )
                                dayValueTab.setQ20302_039_052(999999); // 雪暴出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_019(999999); // 龙卷风质控码 ( 代码表 )
                                dayValueTab.setQ20302_019_052(999999); // 龙卷风出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_016(999999); // 积雪质控码 ( 代码表 )
                                dayValueTab.setQ20302_003(999999); // 结冰质控码 ( 代码表 )
                                dayValueTab.setQ20302_031(999999); // 沙尘暴质控码 ( 代码表 )
                                dayValueTab.setQ20302_031_052(999999); // 沙尘暴出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_007(999999); // 扬沙质控码 ( 代码表 )
                                dayValueTab.setQ20302_007_052(999999); // 扬沙出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_006(999999); // 浮尘质控码 ( 代码表 )
                                dayValueTab.setQ20302_006_052(999999); // 浮尘出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_004(999999); // 烟质控码 ( 代码表 )
                                dayValueTab.setQ20302_005(999999); // 霾质控码 ( 代码表 )
                                dayValueTab.setQ20302_008(999999); // 尘卷风质控码 ( 代码表 )
                                dayValueTab.setQ20302_076(999999); // 冰针质控码 ( 代码表 )
                                dayValueTab.setQ20302_017(999999); // 雷暴质控码 ( 代码表 )
                                dayValueTab.setQ20302_017_052(999999); //雷暴出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_013(999999); // 闪电质控码 ( 代码表 )
                                dayValueTab.setQ20302_014(999999); // 极光质控码 ( 代码表 )
                                dayValueTab.setQ20302_014_052(999999); // 极光出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_015(999999); // 大风质控码 ( 代码表 )
                                dayValueTab.setQ20302_015_052(999999); // 大风出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20302_018(999999); // 飑质控码 ( 代码表 )
                                dayValueTab.setQ20302_018_052(999999); // 飑出现时间质控码 ( 代码表 )
                                dayValueTab.setQ20303(999999); // 天气现象摘要质控码 ( 代码表 )
                                dayValueTab.setQ20304(999999); // 天气现象记录质控码 ( 代码表 )
                                dayList.add(dayValueTab);
                            }

                        }
                        if (!dayList.isEmpty()) {
                            resultMap.put("dayValue", dayList);
                        }
                        if (!hourList.isEmpty()) {
                            resultMap.put("hourValue", hourList);
                        }
                        if (!sunLightList.isEmpty()) {
                            resultMap.put("sunLight", sunLightList);
                        }
                        if (!monthList.isEmpty()) {
                            resultMap.put("monthValue", monthList);
                        }
                    }
                } else {
                    if (txtFileContent == null || txtFileContent.size() == 0) {
                        // 空文件
                        parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                    } else {
                        // 数据只有一行，格式不正确
                        parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
            } catch (FileNotFoundException e) {
                parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
            }
        } else {
            // file not exsit
            parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
        }
        decodingInfo.setSuccess(true);
        resultMap.put("decodingInfo", decodingInfo);
        return resultMap;
    }

    public static String changeType(String dates) {
        if (dates == null || dates == "" || dates.indexOf("/") > -1 || dates.equals('"')) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        return dates;
    }


}
