package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;

import cma.cimiss2.dpc.decoder.fileDecode.common.bean.HourValueTab;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author ：YCK
 * @date ：Created in 2019/10/28 0028 11:26
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class HourValueTabs {
    /**
     * 结果集
     */
    private ParseResult<HourValueTab> parseResult = new ParseResult<HourValueTab>(false);

    /**
     * 解码方法
     *
     * @return
     */
    public ParseResult<HourValueTab> decode(File file) {
        if (file != null && file.exists() && file.isFile()) {
            if (file.length() <= 0) {
                parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
                return parseResult;
            }
            try {
                // get file encode
                FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
                String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
                fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
                // 首先判断文件不是空的，然后需要判断最少有一行数据
                if (txtFileContent != null && txtFileContent.size() >= 1) {
                    String messages = txtFileContent.get(0);
                    String V01300 = changeType(messages.substring(0, 5));
                    String V01301 = changeType(messages.substring(0, 5));
                    String year = changeType(messages.substring(5, 10));
                    String month = changeType(messages.substring(10, 15));
                    String V05001 = changeType(messages.substring(15, 20));
                    String V06001 = changeType(messages.substring(20, 25));
                    String V07001 = changeType(messages.substring(25, 30));
                    String V07031 = changeType(messages.substring(30, 35));
                    String V07032_04 = changeType(messages.substring(35, 40));
                    for (int i = 1; i < txtFileContent.size(); i++) {
                        HourValueTab hourValueTab = new HourValueTab();
                        String ms = txtFileContent.get(i);
                        String day = changeType(ms.substring(0, 2));
                        String hour = changeType(ms.substring(2, 4));

                        String V11290 = changeType(ms.substring(4, 8));

                        String V11291 = changeType(ms.substring(8, 12));
                        String uuid = UUID.randomUUID().toString();

                        hourValueTab.setD_MAIN_ID(uuid); // 记录标识 ( 系统自动生成的流水号 )
                        hourValueTab.setD_IYMDHM(sdf.format(calendar.getTime())); // 入库时间 ( 插表时的系统时间 )
                        hourValueTab.setD_RYMDHM(sdf.format(calendar.getTime())); // 收到时间 ( DPC消息生成时间 )
                        hourValueTab.setD_UPDATE_TIME(sdf.format(calendar.getTime())); // 更新时间 ( 根据CCx对记录更新时的系统时间 )
                        if(i<4){
                            if("21".equals(hour)||"22".equals(hour)||"23".equals(hour)){
                                if(Integer.parseInt(day)>27){//29,28,30,31代表上月数据
                                    int month1=Integer.parseInt(month)-1;
                                    int year1=Integer.parseInt(year);
                                    if(month1<1){
                                        year1=year1-1;
                                        month1=12;
                                    }
                                    hourValueTab.setD_DATETIME(year1 + "-" + month1 + "-" + day + " " + hour + ":00:00"); // 资料时间 ( 由V04001、V04002、V04003、V04004 )
                                    hourValueTab.setV04001(year1); // 资料观测年 ( 代码表 )
                                    hourValueTab.setV04002(month1); // 资料观测月
                                }
                            }
                        }else {
                            hourValueTab.setD_DATETIME(year + "-" + month + "-" + day + " " + hour + ":00:00"); // 资料时间 ( 由V04001、V04002、V04003、V04004 )
                            hourValueTab.setV04001(Integer.parseInt(year)); // 资料观测年 ( 代码表 )
                            hourValueTab.setV04002(Integer.parseInt(month)); // 资料观测月
                        }
                        hourValueTab.setV04003(Integer.parseInt(day)); // 资料观测日
                        hourValueTab.setV04004(Integer.parseInt(hour)); // 资料观测时
                        hourValueTab.setV_BBB("000"); // 更正标志 ( 组成 )
                        hourValueTab.setV01301(V01301); // 区站号(字符)
                        hourValueTab.setV01300(V01300); // 区站号(数字)
                        String v0511=V05001.substring(0,3);
                        String v0512=V05001.substring(3);
                        hourValueTab.setV05001(Integer.parseInt(v0511)+Float.parseFloat(v0512)/60); // 纬度
                        String v0611=V06001.substring(0,3);
                        String v0612=V06001.substring(3);
                        hourValueTab.setV06001(Integer.parseInt(v0611)+Float.parseFloat(v0612)/60); // 经度 ( 单位：度 )
                        hourValueTab.setV07001(Float.parseFloat(V07001)); // 测站高度 ( 单位：度 )
                        hourValueTab.setV07031(Float.parseFloat(V07031)); // 气压传感器海拔高度 ( 单位：米 )
                        hourValueTab.setV07032_04(Float.parseFloat(V07032_04)); // 风速传感器距地面高度 ( 单位：米 )
                        hourValueTab.setV02001("999999"); // 测站类型 ( 代码表 )
                        hourValueTab.setV02301(999999); // 台站级别 ( 代码表 )
                        hourValueTab.setV_ACODE(999999); // 中国行政区划代码 ( 代码表 )

                        if(changeType(ms.substring(104, 108)).equals("999999")){
                            hourValueTab.setV10004(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV10004(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(ms.substring(104, 108)))/10)));
                        }// 本站气压
                        hourValueTab.setV10051(Float.parseFloat("999999")); // 海平面气压 ( 单位：百帕 )
                        hourValueTab.setV10061(Float.parseFloat("999999")); // 3小时变压 ( 单位：百帕 )
                        hourValueTab.setV10062(Float.parseFloat("999999")); // 24小时变压 ( 单位：百帕 )
                        if(changeType(ms.substring(108, 112)).equals("999999")){
                            hourValueTab.setV10301(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV10301(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(ms.substring(108, 112)))/10)));
                        } // 小时内最高本站气压 ( 单位：百帕 )
                        hourValueTab.setV10301_052(Integer.parseInt(changeType(ms.substring(112, 116)))); // 小时内最高本站气压出现时间 ( 格式：hhmm )
                        if(changeType(ms.substring(116, 120)).equals("999999")){
                            hourValueTab.setV10302(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV10302(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(ms.substring(116, 120)))/10)));
                        } // 小时内最低本站气压 ( 单位：百帕  )
                        hourValueTab.setV10302_052(Integer.parseInt(changeType(ms.substring(120, 124)))); // 小时内最低本站气压出现时间 ( 格式：hhmm )
                        try{
                            if(ms.substring(56, 60).equals("999999")){
                                hourValueTab.setV12001(Float.parseFloat("999999"));
                            }else {
                                hourValueTab.setV12001(Float.parseFloat(String.valueOf(Double.parseDouble(ms.substring(56, 60))/10)));
                            }
                        }catch (Exception e){
                            hourValueTab.setV12001(Float.parseFloat("999999")); // 气温 ( 单位：摄氏度 )
                        }
                        try{
                            if(ms.substring(60, 64).equals("999999")){
                                hourValueTab.setV12011(Float.parseFloat("999999"));
                            }else {
                                hourValueTab.setV12011(Float.parseFloat(String.valueOf(Double.parseDouble(ms.substring(60, 64))/10)));
                            }// 小时内最高气温 ( 单位：摄氏度 )
                        }catch (Exception e){
                            hourValueTab.setV12011(Float.parseFloat("999999")); // 小时内最高气温 ( 单位：摄氏度 )
                        }

                        hourValueTab.setV12011_052(Integer.parseInt(changeType(ms.substring(64, 68)))); // 小时内最高气温出现时间 ( 单位：摄氏度 )
                        try{
                            if(ms.substring(68, 72).equals("999999")){
                                hourValueTab.setV12012(Float.parseFloat("999999"));
                            }else {
                                hourValueTab.setV12012(Float.parseFloat(String.valueOf(Double.parseDouble(ms.substring(68, 72))/10)));
                            }// 小时内最低气温 (单位：摄氏度  )
                        }catch (Exception e){
                            hourValueTab.setV12012(Float.parseFloat("999999")); // 小时内最低气温 (单位：摄氏度  )
                        }

                        hourValueTab.setV12012_052(Integer.parseInt(changeType(ms.substring(72, 76)))); // 小时内最低气温出现时间 ( 格式：hhmm )
                        hourValueTab.setV12405(Float.parseFloat("999999")); // 24小时变温 (单位：摄氏度  )
                        hourValueTab.setV12016(Float.parseFloat("999999")); // 过去24小时最高气温 ( 单位：摄氏度 )
                        hourValueTab.setV12017(Float.parseFloat("999999")); // 过去24小时最低气温 ( 单位：摄氏度 )
                        hourValueTab.setBalltemp(Float.parseFloat(changeType(ms.substring(76, 80))));//湿球温度
                        if(changeType(ms.substring(100, 104)).equals("999999")){
                            hourValueTab.setV12003(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV12003(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(ms.substring(100, 104)))/10)));
                        }
                        hourValueTab.setV13003(Integer.parseInt(changeType(ms.substring(84, 88)))); // 相对湿度 (单位：% )
                        hourValueTab.setV13007(Float.parseFloat(changeType(ms.substring(88, 92)))); // 小时内最小相对湿度 ( 单位：% )
                        hourValueTab.setV13007_052(Integer.parseInt(changeType(ms.substring(92, 96)))); // 小时内最小相对湿度出现时间 (格式：hhmm)
                        if(changeType(ms.substring(96, 100)).equals("999999")){
                            hourValueTab.setV13004(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV13004(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(ms.substring(96, 100)))/10)));
                        }
//                        if(changeType(ms.substring(52, 56)).equals("0")){
//                            hourValueTab.setV13019(Float.parseFloat(ms.substring(52, 56)));
//                        }else {
                            String t=changeType(ms.substring(52, 56));
                            if("999999".equals(t)){
                                hourValueTab.setV13019(Float.parseFloat("999999"));
                            }else {
                                hourValueTab.setV13019(Float.parseFloat(String.valueOf(Double.parseDouble(t)/10)));
                            }
//                        }

                        hourValueTab.setV13020(Float.parseFloat("999999")); // 过去3小时降水量 ( 单位：毫米 )
                        hourValueTab.setV13021(Float.parseFloat("999999")); // 过去6小时降水量 ( 单位：毫米 )
                        hourValueTab.setV13022(Float.parseFloat("999999")); // 过去12小时降水量 ( 单位：毫米 )
                        hourValueTab.setV13023(Float.parseFloat("999999")); // 过去24小时降水量 ( 单位：毫米 )
                        hourValueTab.setV04080_04(0); // 人工加密观测降水量描述时间周期 ( 单位：毫米 )
                        hourValueTab.setV13011(Float.parseFloat("999999")); // 人工加密观测降水量 ( 单位：小时 )
                        hourValueTab.setV13033(Float.parseFloat("999999")); // 小时蒸发量 ( 单位：毫米 )
                        hourValueTab.setV11290(V11290); // 2分钟平均风向 ( 单位：毫米 )
                        if(changeType(V11291).equals("999999")){
                            hourValueTab.setV11291(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV11291(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(V11291))/10)));
                        }
                        hourValueTab.setV11292(changeType(ms.substring(12, 16))); // 10分钟平均风向 ( 单位：米/秒 )
                        if(changeType(ms.substring(16, 20)).equals("999999")){
                            hourValueTab.setV11293(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV11293(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(ms.substring(16, 20)))/10)));
                        }
                        hourValueTab.setV11296(changeType(ms.substring(20, 24))); // 小时内最大风速的风向 ( 单位：米/秒 )
                        if(changeType(ms.substring(24, 28)).equals("999999")){
                            hourValueTab.setV11042(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV11042(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(ms.substring(24, 28)))/10)));
                        }
                        hourValueTab.setV11042_052(Integer.parseInt(changeType(ms.substring(28, 32)))); // 小时内最大风速出现时间 ( 格式：hhmm )
                        hourValueTab.setV11201(changeType(ms.substring(32, 36))); // 瞬时风向 ( 单位：度 )
                        if(changeType(ms.substring(36, 40)).equals("999999")){
                            hourValueTab.setV11202(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV11202(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(ms.substring(36, 40)))/10)));
                        }
                        hourValueTab.setV11211(Float.parseFloat(changeType(ms.substring(40, 44)))); // 小时内极大风速的风向 ( 单位：米/秒 )
                        if(changeType(ms.substring(44, 48)).equals("999999")){
                            hourValueTab.setV11046(Float.parseFloat("999999"));
                        }else {
                            hourValueTab.setV11046(Float.parseFloat(String.valueOf(Double.parseDouble(changeType(ms.substring(44, 48)))/10)));
                        }
                        hourValueTab.setV11046_052(Integer.parseInt(changeType(ms.substring(48, 52)))); // 小时内极大风速出现时间 (格式：hhmm  )
                        hourValueTab.setV11503_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风向 (单位：度  )
                        hourValueTab.setV11504_06(Float.parseFloat("999999")); // 过去6小时极大瞬时风速 (单位：米/秒  )
                        hourValueTab.setV11503_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风向 ( 单位：度  )
                        hourValueTab.setV11504_12(Float.parseFloat("999999")); // 过去12小时极大瞬时风速 ( 单位：米/秒)
                        hourValueTab.setV12120(Float.parseFloat(changeType(ms.substring(144, 148)))); // 地面温度 ( 单位：摄氏度 )
                        hourValueTab.setV12311(Float.parseFloat(changeType(ms.substring(148, 152)))); // 小时内最高地面温度 ( 单位：摄氏度 )
                        hourValueTab.setV12311_052(Integer.parseInt(changeType(ms.substring(144, 148)))); // 小时内最高地面温度出现时间 (  格式：hhmm)
                        hourValueTab.setV12121(Float.parseFloat(changeType(ms.substring(148, 152)))); // 小时内最低地面温度 (单位：摄氏度  )
                        hourValueTab.setV12121_052(Integer.parseInt(changeType(ms.substring(152, 156)))); // 小时内最低地面温度出现时间 ( 格式：hhmm)
                        hourValueTab.setV12013(Float.parseFloat("999999")); // 过去12小时最低地面温度 (单位：摄氏度  )
                        hourValueTab.setV12030_005(Float.parseFloat(changeType(ms.substring(156, 160)))); // 5cm地温 ( 单位：摄氏度 )
                        hourValueTab.setV12030_010(Float.parseFloat(changeType(ms.substring(160, 164)))); // 10cm地温 ( 单位：摄氏度 )
                        hourValueTab.setV12030_015(Float.parseFloat(changeType(ms.substring(164, 168)))); // 15cm地温 ( 单位：摄氏度 )
                        hourValueTab.setV12030_020(Float.parseFloat(changeType(ms.substring(168, 172)))); // 20cm地温 ( 单位：摄氏度 )
                        hourValueTab.setV12030_040(Float.parseFloat(changeType(ms.substring(172, 176)))); // 40cm地温 ( 单位：摄氏度 )
                        hourValueTab.setV12030_080(Float.parseFloat(changeType(ms.substring(176, 180)))); // 80cm地温 ( 单位：摄氏度 )
                        hourValueTab.setV12030_160(Float.parseFloat(changeType(ms.substring(180, 184)))); // 160cm地温 ( 单位：摄氏度 )
                        hourValueTab.setV12030_320(Float.parseFloat(changeType(ms.substring(184, 188)))); // 320cm地温 ( 单位：摄氏度 )
                        hourValueTab.setV12314(Float.parseFloat(changeType(ms.substring(124, 128)))); // 草面（雪面）温度 ( 单位：摄氏度 )
                        hourValueTab.setV12315(Float.parseFloat(changeType(ms.substring(128, 132)))); // 小时内草面（雪面）最高温度 ( 单位：摄氏度 )
                        hourValueTab.setV12315_052(Integer.parseInt(changeType(ms.substring(132, 136)))); // 小时内草面（雪面）最高温度出现时间 ( 格式：hhmm )
                        hourValueTab.setV12316(Float.parseFloat(changeType(ms.substring(136, 140)))); // 小时内草面（雪面）最低温度 ( 单位：摄氏度 )
                        hourValueTab.setV12316_052(Integer.parseInt(changeType(ms.substring(140, 144)))); // 小时内草面（雪面）最低温度出现时间 ( 格式：hhmm  )
                        hourValueTab.setV20001_701_01(Float.parseFloat("999999")); // 1分钟平均水平能见度 ( 单位：米 )
                        hourValueTab.setV20001_701_10(Float.parseFloat("999999")); // 10分钟平均水平能见度 ( 单位：米 )
                        hourValueTab.setV20059(Float.parseFloat("999999")); // 最小水平能见度 ( 单位：米 )
                        hourValueTab.setV20059_052("999999"); // 最小水平能见度出现时间 ( 格式：hhmm  )
                        hourValueTab.setV20001(Float.parseFloat("999999")); // 水平能见度（人工） ( 单位：千米 )
                        hourValueTab.setVisibility2(999999);//能见度级别
                        hourValueTab.setV20010(999999); // 总云量 (单位：%  )
                        hourValueTab.setV20051(999999); // 低云量 ( 单位：% )
                        hourValueTab.setV20011(Float.parseFloat("999999")); // 低云或中云的云量 ( 单位：% )
                        hourValueTab.setV20013(Float.parseFloat("999999")); // 低云或中云的云高 ( 单位：% )
                        hourValueTab.setV20350_01(999999); // 云状1 ( 单位：米 )
                        hourValueTab.setV20350_02("999999"); // 云状2 ( 代码表 )
                        hourValueTab.setV20350_03(999999); // 云状3 ( 代码表 )
                        hourValueTab.setV20350_04(999999); // 云状4 ( 代码表 )
                        hourValueTab.setV20350_05(999999); // 云状5 ( 代码表 )
                        hourValueTab.setV20350_06(999999); // 云状6 ( 代码表 )
                        hourValueTab.setV20350_07(999999); // 云状7 ( 代码表 )
                        hourValueTab.setV20350_08(999999); // 云状8 ( 代码表 )
                        hourValueTab.setV20350_11(999999); // 低云状 ( 代码表 )
                        hourValueTab.setV20350_12(999999); // 中云状 ( 代码表 )
                        hourValueTab.setV20350_13(999999); // 高云状 ( 代码表 )
                        hourValueTab.setV20003(999999); // 现在天气 ( 代码表 )
                        hourValueTab.setV04080_05(999999); // 过去天气描述时间周期 ( 代码表 )
                        hourValueTab.setV20004(999999); // 过去天气1 ( 单位:小时 )
                        hourValueTab.setV20005(999999); // 过去天气2 ( 代码表 )
                        hourValueTab.setV20062(999999); // 地面状态 ( 代码表 )
                        hourValueTab.setV13013(Float.parseFloat("999999")); // 积雪深度 ( 代码表 )
                        hourValueTab.setV13330(Float.parseFloat("999999")); // 雪压 ( 单位：厘米 )
                        hourValueTab.setV20330_01(Float.parseFloat("999999")); // 冻土第1冻结层上限值 ( 单位：g/cm2 )
                        hourValueTab.setV20331_01(Float.parseFloat("999999")); // 冻土第1冻结层下限值 ( 单位：厘米 )
                        hourValueTab.setV20330_02(Float.parseFloat("999999")); // 冻土第2冻结层上限值 ( 单位：厘米 )
                        hourValueTab.setV20331_02(Float.parseFloat("999999")); // 冻土第2冻结层下限值 ( 单位：厘米 )
                        hourValueTab.setQ10004(9); // 本站气压质量标志 ( 单位：厘米 )
                        hourValueTab.setQ10051(9); // 海平面气压质量标志 ( 代码表 )
                        hourValueTab.setQ10061(9); // 3小时变压质量标志 ( 代码表 )
                        hourValueTab.setQ10062(9); // 24小时变压质质量标志 ( 代码表 )
                        hourValueTab.setQ10301(9); // 小时内最高本站气压质量标志 ( 代码表 )
                        hourValueTab.setQ10301_052(9); // 小时内最高本站气压出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ10302(9); // 小时内最低本站气压质量标志 ( 代码表 )
                        hourValueTab.setQ10302_052(9); // 小时内最低本站气压出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ12001(9); // 气温质量标志 ( 代码表 )
                        hourValueTab.setQ12011(9); // 小时内最高气温质量标志 ( 代码表 )
                        hourValueTab.setQ12011_052(9); // 小时内最高气温出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ12012(9); // 小时内最低气温质量标志 ( 代码表 )
                        hourValueTab.setQ12012_052(9); // 小时内最低气温出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ12405(9); // 24小时变温质量标志 ( 代码表 )
                        hourValueTab.setQ12016(9); // 过去24小时最高气温质量标志 ( 代码表 )
                        hourValueTab.setQ12017(9); // 过去24小时最低气温质量标志 ( 代码表 )
                        hourValueTab.setQ12003(9); // 露点温度质量标志 ( 代码表 )
                        hourValueTab.setQ13003(9); // 相对湿度质量标志 ( 代码表 )
                        hourValueTab.setQ13007(9); // 小时内最小相对湿度质量标志 ( 代码表 )
                        hourValueTab.setQ13007_052(9); // 小时内最小相对湿度出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ13004(9); // 水汽压质量标志 ( 代码表 )
                        hourValueTab.setQ13019(9); // 1小时降水量质量标志 ( 代码表 )
                        hourValueTab.setQ13020(9); // 过去3小时降水量质量标志 ( 代码表 )
                        hourValueTab.setQ13021(9); // 过去6小时降水量质量标志 ( 代码表 )
                        hourValueTab.setQ13022(9); // 过去12小时降水量质量标志 ( 代码表 )
                        hourValueTab.setQ13023(9); // 24小时降水量质 ( 代码表 )
                        hourValueTab.setQ04080_04(9); // 人工加密观测降水量描述时间周期质量标志 ( 代码表 )
                        hourValueTab.setQ13011(9); // 人工加密观测降水量质量标志 ( 代码表 )
                        hourValueTab.setQ13033(9); // 小时蒸发量质量标志 ( 代码表 )
                        hourValueTab.setQ11290(9); // 2分钟平均风向质量标志 ( 代码表 )
                        hourValueTab.setQ11291(9); // 2分钟平均风速质量标志 ( 代码表 )
                        hourValueTab.setQ11292(9); // 10分钟平均风向质量标志 ( 代码表 )
                        hourValueTab.setQ11293(9); // 10分钟平均风速质量标志 ( 代码表 )
                        hourValueTab.setQ11296(9); // 小时内最大风速的风向质量标志 ( 代码表 )
                        hourValueTab.setQ11042(9); // 小时内最大风速质量标志 ( 代码表 )
                        hourValueTab.setQ11042_052(9); // 小时内最大风速出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ11201(9); // 瞬时风向质量标志 ( 代码表 )
                        hourValueTab.setQ11202(9); // 瞬时风速质量标志 ( 代码表 )
                        hourValueTab.setQ11211(9); // 小时内极大风速的风向质量标志 ( 代码表 )
                        hourValueTab.setQ11046(9); // 小时内极大风速质量标志 ( 代码表 )
                        hourValueTab.setQ11046_052(9); // 小时内极大风速出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ11503_06(9); // 过去6小时极大瞬时风向质量标志 ( 代码表 )
                        hourValueTab.setQ11504_06(9); // 过去6小时极大瞬时风速质量标志 ( 代码表 )
                        hourValueTab.setQ11503_12(9); // 过去12小时极大瞬时风向质量标志 ( 代码表 )
                        hourValueTab.setQ11504_12(9); // 过去12小时极大瞬时风速质量标志 ( 代码表 )
                        hourValueTab.setQ12120(9); // 地面温度质量标志 ( 代码表 )
                        hourValueTab.setQ12311(9); // 小时内最高地面温度质量标志 ( 代码表 )
                        hourValueTab.setQ12311_052(9); // 小时内最高地面温度出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ12121(9); // 小时内最低地面温度质量标志 ( 代码表 )
                        hourValueTab.setQ12121_052(9); // 小时内最低地面温度出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ12013(9); // 过去12小时最低地面温度 ( 代码表 )
                        hourValueTab.setQ12030_005(9); // 5cm地温质量标志 ( 代码表 )
                        hourValueTab.setQ12030_010(9); // 10cm地温质量标志 ( 代码表 )
                        hourValueTab.setQ12030_015(9); // 15cm地温质量标志 ( 代码表 )
                        hourValueTab.setQ12030_020(9); // 20cm地温质量标志 ( 代码表 )
                        hourValueTab.setQ12030_040(9); // 40cm地温质量标志 ( 代码表 )
                        hourValueTab.setQ12030_080(9); // 80cm地温质量标志 ( 代码表 )
                        hourValueTab.setQ12030_160(9); // 160cm地温质量标志 ( 代码表 )
                        hourValueTab.setQ12030_320(9); // 320cm地温质量标志 ( 代码表 )
                        hourValueTab.setQ12314(9); // 草面（雪面）温度质量标志 ( 代码表 )
                        hourValueTab.setQ12315(9); // 小时内草面（雪面）最高温度质量标志 ( 代码表 )
                        hourValueTab.setQ12315_052(9); // 小时内草面（雪面）最高温度出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ12316(9); // 小时内草面（雪面）最低温度质量标志 ( 代码表 )
                        hourValueTab.setQ12316_052(9); // 小时内草面（雪面）最低温度出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ20001_701_01(9); // 1分钟平均水平能见度质量标志 ( 代码表 )
                        hourValueTab.setQ20001_701_10(9); // 10分钟平均水平能见度质量标志 ( 代码表 )
                        hourValueTab.setQ20059(9); // 最小水平能见度质量标志 ( 代码表 )
                        hourValueTab.setQ20059_052(9); // 最小水平能见度出现时间质量标志 ( 代码表 )
                        hourValueTab.setQ20001(9); // 水平能见度（人工）质量标志 ( 代码表 )
                        hourValueTab.setQ20010(9); // 总云量质量标志 ( 代码表 )
                        hourValueTab.setQ20051(9); // 低云量质量标志 ( 代码表 )
                        hourValueTab.setQ20011(9); // 低云或中云的云量质量标志 ( 代码表 )
                        hourValueTab.setQ20013(9); // 低云或中云的云高质量标志 ( 代码表 )
                        hourValueTab.setQ20350_01(9); // 云状1质量标志 ( 代码表 )
                        hourValueTab.setQ20350_02(9); // 云状2质量标志 ( 代码表 )
                        hourValueTab.setQ20350_03(9); // 云状3质量标志 ( 代码表 )
                        hourValueTab.setQ20350_04(9); // 云状4质量标志 ( 代码表 )
                        hourValueTab.setQ20350_05(9); // 云状5质量标志 ( 代码表 )
                        hourValueTab.setQ20350_06(9); // 云状6质量标志 ( 代码表 )
                        hourValueTab.setQ20350_07(9); // 云状7质量标志 ( 代码表 )
                        hourValueTab.setQ20350_08(9); // 云状8质量标志 ( 代码表 )
                        hourValueTab.setQ20350_11(9); // 低云状质量标志 ( 代码表 )
                        hourValueTab.setQ20350_12(9); // 中云状质量标志 ( 代码表 )
                        hourValueTab.setQ20350_13(9); // 高云状质量标志 ( 代码表 )
                        hourValueTab.setQ20003(9); // 现在天气质量标志 ( 代码表 )
                        hourValueTab.setQ04080_05(9); // 过去天气描述时间周期质量标志 ( 代码表 )
                        hourValueTab.setQ20004(9); // 过去天气1质量标志 ( 代码表 )
                        hourValueTab.setQ20005(9); // 过去天气2质量标志 ( 代码表 )
                        hourValueTab.setQ20062(9); // 地面状态质量标志 ( 代码表 )
                        hourValueTab.setQ13013(9); // 积雪深度质量标志 ( 代码表 )
                        hourValueTab.setQ13330(9); // 雪压质量标志 ( 代码表 )
                        hourValueTab.setQ20330_01(9); // 冻土第1冻结层上限值质量标志 ( 代码表 )
                        hourValueTab.setQ20331_01(9); // 冻土第1冻结层下限值质量标志 ( 代码表 )
                        hourValueTab.setQ20330_02(9); // 冻土第2冻结层上限值质量标志 ( 代码表 )
                        hourValueTab.setQ20331_02(9); // 冻土第2冻结层下限值质量标志 ( 代码表 )
                        parseResult.put(hourValueTab);
                        parseResult.setSuccess(true);

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
        return parseResult;
    }

    public static String changeType(String dates) {
        if (dates == null || dates == ""|| dates.indexOf("/") > -1 || dates.indexOf("-") > -1 || dates.equals('"')) {
            dates = "999999";
        }
        dates = dates.replaceAll(" ", "");
        return dates;
    }

    public static void main(String[] args) {
        File file=new File("E:\\filedecode\\unManData\\ZC500301.011");
        HourValueTabs h=new HourValueTabs();
        h.decode(file);
    }
}
