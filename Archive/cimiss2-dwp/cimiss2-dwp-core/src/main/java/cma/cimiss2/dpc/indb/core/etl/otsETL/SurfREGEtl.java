package cma.cimiss2.dpc.indb.core.etl.otsETL;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataReg;
import cma.cimiss2.dpc.decoder.surf.DecodeREG;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import cma.cimiss2.dpc.indb.core.tools.DataMapMaker;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 区域站数据格式化类 ots缓冲库
 */
public class SurfREGEtl extends AbstractEtl<SurfaceObservationDataReg> {

    private static final long serialVersionUID = -6914120843747532109L;
    static Log log = Logs.get();

    public SurfREGEtl() {
    }

    @Override
    public ParseResult<SurfaceObservationDataReg> extract(File file) {
        DecodeREG decodeREG = new DecodeREG();
        return (ParseResult<SurfaceObservationDataReg>) decodeREG.decode(file, new HashSet<String>());
    }

    @Override
    public StatTF transform(ParseResult<SurfaceObservationDataReg> result, File file, OTSHelper... tablesHelper) {
//        reload(intervalTime);
        //解码数据集合
        List<SurfaceObservationDataReg> beans = result.getData();
        //报文集合
        List<ReportInfo> reports = result.getReports();

        //小时表
        OTSHelper horTable = tablesHelper[0];
        //分钟表
        OTSHelper minTable = tablesHelper[1];
        //报文表
        OTSHelper repTable = tablesHelper[2];

        StatTF statTF = new StatTF();

        String D_DATA_ID_hor = "A.0012.0001.S001";
        String D_DATA_ID_min = "A.0010.0001.S001";
        for (int i = 0; i < beans.size(); i++) {
            SurfaceObservationDataReg bean = beans.get(i);
            String V_BBB = bean.getCorrectionIndicator();
            Date observationTime = bean.getObservationTime();
            String format = new SimpleDateFormat("yyyyMMddHHmmss").format(observationTime);
            String stationNumberChina = bean.getStationNumberChina().toUpperCase();
            String D_RECORD_ID = format + "_" + stationNumberChina;
            int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();
            boolean corHor = true;
            boolean corMin = true;
            //更正报判断
            if (!"000".equals(V_BBB)) {
                corHor = corrMessage(D_RECORD_ID, V_BBB, horTable);
                corMin = corrMessage(D_RECORD_ID, V_BBB, minTable);
            }
            // 如果是 00 分的数据，入小时表
            if (observationTime.getMinutes() == 00) {
                Map<String, Object> data;
                try {
                    data = DataMapMaker.make(bean, 0);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.debug("SurfaceObservationDataReg is a illegal bean.");
                    return null;
                }

                data.put("D_DATA_ID", D_DATA_ID_hor);//资料标识
                data.put("D_RECORD_ID", D_RECORD_ID);//记录标识
                data.put("D_IYMDHM", new Date());//入库时间
                data.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
                data.put("D_UPDATE_TIME", new Date());//更新时间
                data.put("D_DATETIME",observationTime );//
                data.put("V01301", stationNumberChina);
                if (num >= 48 & num <= 57) {
                    data.put("V01300", stationNumberChina);
                } else {
                    data.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
                }
                if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                    String s = (String) getStationInfo().get(stationNumberChina + "+01");
                    data.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                    data.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
                } else {
                    data.put("V02301", "999999");
                    data.put("V_ACODE", "999999");
                }
//                data.put("D_DATETIME", obsTimeCalendar);//资料时间
                data.put("V04001", observationTime.getYear() + 1900);//资料观测年
                data.put("V04002", observationTime.getMonth() + 1);//资料观测月
                data.put("V04003", observationTime.getDate());//资料观测日
                data.put("V04004", observationTime.getHours());//资料观测时
                data.put("V07032_04", "999998");
                data.put("V07032_01", "999998");
                data.put("V07032_02", "999998");
                data.put("V02001", "0");

                data.put("V08010", "999998");//地面限定符（温度数据）
                data.put("V02183", "999998");//云探测系统

                data.put("V10061", "999998");//3小时变压
                data.put("V10062", "999998");//24小时变压
                data.put("V12405", "999998");//24小时变温
                data.put("V12016", "999998");//过去24小时最高气温
                data.put("V12017", "999998");//过去24小时最低气温
                data.put("V13020", "999998");//过去3小时降水量
                data.put("V13021", "999998");//过去6小时降水量
                data.put("V13022", "999998");//过去12小时降水量
                data.put("V13023", "999998");//过去24小时降水量
                data.put("V04080_04", "999998");//人工加密观测降水量描述时间周期
                data.put("V13011", "999998");//人工加密观测降水量
                data.put("V11503_06", "999998");//过去6小时极大瞬时风向
                data.put("V11504_06", "999998");//过去6小时极大瞬时风速
                data.put("V11503_12", "999998");//过去12小时极大瞬时风向
                data.put("V11504_12", "999998");//过去12小时极大瞬时风速
                data.put("V12013", "999998");//过去12小时最低地面温度
                data.put("V20001_701_01", "999998");//1分钟平均水平能见度
                data.put("V20001_701_10", "999998");//10分钟平均水平能见度
                data.put("V04080_05", "999998");//过去天气描述时间周期
                data.put("V20004", "999998");//过去天气1
                data.put("V20005", "999998");//过去天气2
                data.put("V20330_01", "999998");//冻土第1冻结层上限值
                data.put("V20331_01", "999998");//冻土第1冻结层下限值
                data.put("V20330_02", "999998");//冻土第2冻结层上限值
                data.put("V20331_02", "999998");//冻土第2冻结层下限值
                data.put("Q10061", "7");//3小时变压质量标志
                data.put("Q10062", "7");//24小时变压质质量标志
                data.put("Q12405", "7");//24小时变温质量标志
                data.put("Q12016", "7");//过去24小时最高气温质量标志
                data.put("Q12017", "7");//过去24小时最低气温质量标志
                data.put("Q13020", "7");//过去3小时降水量质量标志
                data.put("Q13021", "7");//过去6小时降水量质量标志
                data.put("Q13022", "7");//过去12小时降水量质量标志
                data.put("Q13023", "7");//24小时降水量质
                data.put("Q04080_04", "7");//人工加密观测降水量描述时间周期质量标志
                data.put("Q13011", "7");//人工加密观测降水量质量标志
                data.put("Q11503_06", "7");//过去6小时极大瞬时风向质量标志
                data.put("Q11504_06", "7");//过去6小时极大瞬时风速质量标志
                data.put("Q11503_12", "7");//过去12小时极大瞬时风向质量标志
                data.put("Q11504_12", "7");//过去12小时极大瞬时风速质量标志
                data.put("Q12013", "7");//过去12小时最低地面温度质量标志
                data.put("Q20001_701_01", "7");//1分钟平均水平能见度质量标志
                data.put("Q20001_701_10", "7");//10分钟平均水平能见度质量标志
                data.put("Q20001", "7");//水平能见度（人工）质量标志
                data.put("Q20010", "7");//总云量质量标志
                data.put("Q20051", "7");//低云量质量标志
                data.put("Q20011", "7");//低云或中云的云量质量标志
                data.put("Q20013", "7");//低云或中云的云高质量标志
                data.put("Q20350_01", "7");//云状1质量标志
                data.put("Q20350_02", "7");//云状2质量标志
                data.put("Q20350_03", "7");//云状3质量标志
                data.put("Q20350_04", "7");//云状4质量标志
                data.put("Q20350_05", "7");//云状5质量标志
                data.put("Q20350_06", "7");//云状6质量标志
                data.put("Q20350_07", "7");//云状7质量标志
                data.put("Q20350_08", "7");//云状8质量标志
                data.put("Q20350_11", "7");//低云状质量标志
                data.put("Q20350_12", "7");//中云状质量标志
                data.put("Q20350_13", "7");//高云状质量标志
                data.put("Q20003", "7");//现在天气质量标志
                data.put("Q04080_05", "7");//过去天气描述时间周期质量标志
                data.put("Q20004", "7");//过去天气1质量标志
                data.put("Q20005", "7");//过去天气2质量标志
                data.put("Q20062", "7");//地面状态质量标志
                data.put("Q13013", "7");//积雪深度质量标志
                data.put("Q13330", "7");//雪压质量标志
                data.put("Q20330_01", "7");//冻土第1冻结层上限值质量标志
                data.put("Q20331_01", "7");//冻土第1冻结层下限值质量标志
                data.put("Q20330_02", "7");//冻土第2冻结层上限值质量标志
                data.put("Q20331_02", "7");//冻土第2冻结层下限值质量标志
                data.put("Q12120", "7");//地面温度质量标志
                //云状
                String[] cloudType = bean.getCloudType();
                data.put("V20350_01", cloudType[0]);//
                data.put("V20350_02", cloudType[1]);//
                data.put("V20350_03", cloudType[2]);//
                data.put("V20350_04", cloudType[3]);//
                data.put("V20350_05", cloudType[4]);//
                data.put("V20350_06", cloudType[5]);//
                data.put("V20350_07", cloudType[6]);//
                data.put("V20350_08", cloudType[7]);//
                //云状编码
                String cloudType1 = bean.getCloudType1();
                data.put("V20350_11", getCloudType1Detail(cloudType1, 0));//
                data.put("V20350_12", getCloudType1Detail(cloudType1, 1));//
                data.put("V20350_13", getCloudType1Detail(cloudType1, 2));//

                if (corHor) {
                    statTF.getHorList().add(data);
                } else {
                    try {
                        horTable.update(data);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //入分钟表
            Map<String, Object> dataMin = new HashMap<>();

            dataMin.put("D_RECORD_ID", format + "_" + stationNumberChina);//记录标识
            dataMin.put("D_DATA_ID", D_DATA_ID_min);//资料标识
            dataMin.put("D_IYMDHM", new Date());//入库时间
            dataMin.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            dataMin.put("D_UPDATE_TIME", new Date());//更新时间
            dataMin.put("V01301", stationNumberChina);
            if (num >= 48 & num <= 57) {
                dataMin.put("V01300", stationNumberChina);
            } else {
                dataMin.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                dataMin.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                dataMin.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                dataMin.put("V02301", "999999");
                dataMin.put("V_ACODE", "999999");
            }

            dataMin.put("D_DATETIME", observationTime);//资料时间
            dataMin.put("V04001", observationTime.getYear() + 1900);//资料观测年
            dataMin.put("V04002", observationTime.getMonth() + 1);//资料观测月
            dataMin.put("V04003", observationTime.getDate());//资料观测日
            dataMin.put("V04004", observationTime.getHours());//资料观测时
            dataMin.put("V04005", observationTime.getMinutes());//资料观测分 小时不入
            dataMin.put("V05001", bean.getLatitude());//纬度
            dataMin.put("V06001", bean.getLongitude());//经度
            dataMin.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
            dataMin.put("V02001", "0");//测站类型

            dataMin.put("V_BBB", V_BBB);//更正报标志???????/更改 原来为000
            dataMin.put("V08010", "999999");//地面限定符 (温度数据)
            dataMin.put("V07031", bean.getHeightOfBarometerAboveMeanSeaLevel());//气压传感器海拔高度
            dataMin.put("V07032_02", "999999");//能见度传感器离本地地面（或海上平台甲板）的高度
            dataMin.put("V07032_01", "999999");//温度传感器距地面高度
            dataMin.put("V07032_04", "999999");//风传感器距地面高度
            dataMin.put("V02180", "999998");//主要天气现况检测系统
            dataMin.put("V02183", "999999");//云探测系统
            dataMin.put("V02175", "999998");//降水测量方法
            dataMin.put("V02177", "7");//雪深的测量方法
            dataMin.put("V10004", bean.getStationPressure().getValue());//本站气压
            dataMin.put("V10051", bean.getPressureReducedToMeanSeaLevel().getValue());//海平面气压
            dataMin.put("V11041", "999998");//1分钟内极大风速
            dataMin.put("V11043", "999998");//1分钟内极大风速的风向
            dataMin.put("V11201", "999998");//瞬时风向
            dataMin.put("V11202", bean.getWindSpeedCurrent().getValue());//瞬时风速
            dataMin.put("V11288", "999998");//1分钟平均风向
            dataMin.put("V11289", "999998");//1分钟平均风速
            dataMin.put("V11290", bean.getWindDirectionAt2m().getValue());//2分钟平均风向
            dataMin.put("V11291", bean.getWindSpeedAvg2m().getValue());//2分钟平均风速
            dataMin.put("V11292", bean.getWindDirectionAt10().getValue());//10分钟平均风向
            dataMin.put("V11293", bean.getWindSpeedAt10().getValue());//10分钟平均风速"
            dataMin.put("V11296", bean.getWindDirectionOfMaxSpeed().getValue());//最大风速的风向
            dataMin.put("V12001", bean.getTemperature().getValue());//气温
            dataMin.put("V12003", bean.getWetBulbTemperature().getValue());//露点温度
            dataMin.put("V12030_005", bean.getSoilTemperature5CM().getValue());//5cm地温
            dataMin.put("V12030_010", bean.getSoilTemperature10CM().getValue());//10cm地温
            dataMin.put("V12030_015", bean.getSoilTemperature15CM().getValue());//15cm地温
            dataMin.put("V12030_020", bean.getSoilTemperature20CM().getValue());//20cm地温
            dataMin.put("V12030_040", bean.getSoilTemperature40CM().getValue());//40cm地温
            dataMin.put("V12030_080", bean.getSoilTemperature80CM().getValue());//80cm地温
            dataMin.put("V12030_160", bean.getSoilTemperature160CM().getValue());//160cm地温
            dataMin.put("V12030_320", bean.getSoilTemperature320CM().getValue());//320cm地温
            dataMin.put("V12120", bean.getLandSurfaceTemperature().getValue());//地面温度
            dataMin.put("V12314", bean.getGrassTemperature().getValue());//草面（雪面）温度
            dataMin.put("V13003", bean.getRelativeHumidity().getValue());//相对湿度
            dataMin.put("V13004", bean.getVapourPressur().getValue());//水汽压
            dataMin.put("V13011", "999998");//分钟降水量
            dataMin.put("V13013", bean.getSnowDepth());//总雪深
            dataMin.put("V13033", bean.getEvaporation().getValue());//小时蒸发量
            dataMin.put("V20001_701_01", "999998");//1分钟平均水平能见度
            dataMin.put("V20001_701_10", "999998");//10分钟平均水平能见度
            dataMin.put("V20010", bean.getCloudAmount());//云量
            dataMin.put("V20013", bean.getCloudHeightLowAndMiddle());//云底高度
            dataMin.put("V20211", "999998");//分钟连续观测天气现象
            dataMin.put("Q10004", bean.getStationPressure().getQuality().get(0).getCode());//本站气压质量标志
            dataMin.put("Q10051", bean.getPressureReducedToMeanSeaLevel().getQuality().get(0).getCode());//海平面气压质量控制标志
            dataMin.put("Q11201", bean.getTemperature().getQuality().get(0).getCode());//瞬时风向质量标志
            dataMin.put("Q11202", bean.getWindSpeedCurrent().getQuality().get(0).getCode());//瞬时风速质量标志
            dataMin.put("Q11288", "7");//1分钟平均风向质量标志
            dataMin.put("Q11289", "7");//1分钟平均风速质量标志
            dataMin.put("Q11290", bean.getWindDirectionAt2m().getQuality().get(0).getCode());//2分钟平均风向质量标志
            dataMin.put("Q11291", bean.getWindSpeedAvg2m().getQuality().get(0).getCode());//2分钟平均风速质量标志
            dataMin.put("Q11292", bean.getWindDirectionAt10().getQuality().get(0).getCode());//10分钟平均风向质量标志
            dataMin.put("Q11293", bean.getWindSpeedAt10().getQuality().get(0).getCode());//10分钟平均风速质量标志
            dataMin.put("Q11296", bean.getWindDirectionOfMaxSpeed().getQuality().get(0).getCode());//最大风速的风向质量标志
            dataMin.put("Q12001", bean.getWindDirectionCurrent().getQuality().get(0).getCode());//气温质量标志
            dataMin.put("Q12003", bean.getWetBulbTemperature().getQuality().get(0).getCode());//露点温度质量标志
            dataMin.put("Q12030_005", bean.getSoilTemperature5CM().getQuality().get(0).getCode());//5cm地温质量标志
            dataMin.put("Q12030_010", bean.getSoilTemperature10CM().getQuality().get(0).getCode());//10cm地温质量标志
            dataMin.put("Q12030_015", bean.getSoilTemperature15CM().getQuality().get(0).getCode());//15cm地温质量标志
            dataMin.put("Q12030_020", bean.getSoilTemperature20CM().getQuality().get(0).getCode());//20cm地温质量标志
            dataMin.put("Q12030_040", bean.getSoilTemperature40CM().getQuality().get(0).getCode());//40cm地温质量标志
            dataMin.put("Q12030_080", bean.getSoilTemperature80CM().getQuality().get(0).getCode());//80cm地温质量标志
            dataMin.put("Q12030_160", bean.getSoilTemperature160CM().getQuality().get(0).getCode());//160cm地温质量标志
            dataMin.put("Q12030_320", bean.getSoilTemperature320CM().getQuality().get(0).getCode());//320cm地温质量标志
            dataMin.put("Q12120", bean.getLandSurfaceTemperature().getQuality().get(0).getCode());//地面温度质量标志
            dataMin.put("Q12314", bean.getGrassTemperature().getQuality().get(0).getCode());//草面（雪面）温度质量标志
            dataMin.put("Q13003", bean.getRelativeHumidity().getQuality().get(0).getCode());//相对湿度质量标志
            dataMin.put("Q13004", bean.getVapourPressur().getQuality().get(0).getCode());//水汽压质量标志
            dataMin.put("Q13011", "7");//分钟降水量质量标志
            dataMin.put("Q13013", "7");//总雪深质量标志
            dataMin.put("Q20001_701_01", "7");//1分钟平均水平能见度质量标志
            dataMin.put("Q20001_701_10", "7");//10分钟平均水平能见度质量标志
            dataMin.put("Q20010", "7");//云量质量标志
            dataMin.put("Q20013", "7");//云底高度质量标志

            if (corMin) {
                statTF.getMinList().add(dataMin);
            } else {
                try {
                    minTable.update(dataMin);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            statTF.getBuffer().append(stationNumberChina + "  " + format + "  " + "000" + "\n");
        }
        try {
            statTF.setRepList(transformReports_ats(reports, D_DATA_ID_min, file));
        } catch (ParseException e) {
            statTF.getBuffer().append("reports inssrt error!");
        }
        return statTF;
    }


}
