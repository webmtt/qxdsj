package cma.cimiss2.dpc.indb.core.etl.otsETL;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.decoder.surf.DecodeBABJ;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import cma.cimiss2.dpc.indb.core.tools.DataMapMaker;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * 国家站数据格式化类 ots缓冲库
 */
public class SurfBABJEtl extends AbstractEtl<SurfaceObservationDataNation> implements Serializable {
    private static final long serialVersionUID = 6960936997874687961L;
    //    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    public SurfBABJEtl() {
    }


    @Override
    public ParseResult<SurfaceObservationDataNation> extract(File file) {
        DecodeBABJ decodeBABJ = new DecodeBABJ();
        ParseResult<SurfaceObservationDataNation> decode = decodeBABJ.decode(file, new HashSet<>());
        return decode;
    }

    @Override
    public StatTF transform(ParseResult<SurfaceObservationDataNation> result, File file, OTSHelper... tablesHelper) {
//        reload(intervalTime);

        //解码数据集合
        List<SurfaceObservationDataNation> beans = result.getData();
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
            SurfaceObservationDataNation bean = beans.get(i);
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
                    data = DataMapMaker.make(bean, 1);
                } catch (IllegalArgumentException | IllegalAccessException e) {

                    return null;
                }
                data.put("D_RECORD_ID", D_RECORD_ID);//记录标识
                data.put("D_DATA_ID", D_DATA_ID_hor);//资料标识
                data.put("D_IYMDHM", new Date());//入库时间
                data.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
                data.put("D_DATETIME", observationTime);//
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

                data.put("V04001", (observationTime.getYear() + 1900));//资料观测年
                data.put("V04002", (observationTime.getMonth() + 1));//资料观测月
                data.put("V04003", observationTime.getDate());//资料观测日
                data.put("V04004", observationTime.getHours());//资料观测时
                data.put("V07032_04", "999998");//风速传感器距地面高度
                data.put("V07032_01", "999998");//温度传感器距地面高度
                data.put("V07032_02", "999998");//能见度传感器距地面高度
                data.put("V02001", "0");//测站类型

                data.put("V08010", "999998");//地面限定符（温度数据）
                data.put("V02183", "999998");//云探测系统

                //云状
                String[] cloudType = bean.getCloudType().getValue();
//                List<Quality> cloudTypeQuality = bean.getCloudType().getQuality();
                data.put("V20350_01", cloudType[0]);//
                data.put("V20350_02", cloudType[1]);//
                data.put("V20350_03", cloudType[2]);//
                data.put("V20350_04", cloudType[3]);//
                data.put("V20350_05", cloudType[4]);//
                data.put("V20350_06", cloudType[5]);//
                data.put("V20350_07", cloudType[6]);//
                data.put("V20350_08", cloudType[7]);//
                //云状编码
                String cloudType1 = bean.getCloudType1().getValue();
                data.put("V20350_11", getCloudType1Detail(cloudType1, 0));//
                data.put("V20350_12", getCloudType1Detail(cloudType1, 1));//
                data.put("V20350_13", getCloudType1Detail(cloudType1, 2));//
                //云状质量标志 二级质控
                int cloudTypeQuality = bean.getCloudType().getQuality().get(1).getCode();
                data.put("Q20350_01", cloudTypeQuality);//
                data.put("Q20350_02", cloudTypeQuality);//
                data.put("Q20350_03", cloudTypeQuality);//
                data.put("Q20350_04", cloudTypeQuality);//
                data.put("Q20350_05", cloudTypeQuality);//
                data.put("Q20350_06", cloudTypeQuality);//
                data.put("Q20350_07", cloudTypeQuality);//
                data.put("Q20350_08", cloudTypeQuality);//
                //云状编码质量标志 二级质控
                int cloudType1Quality = bean.getCloudType1().getQuality().get(1).getCode();
                data.put("Q20350_11", cloudType1Quality);//
                data.put("Q20350_12", cloudType1Quality);//
                data.put("Q20350_13", cloudType1Quality);//
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
            dataMin.put("D_DATETIME", observationTime);//
            if (num >= 48 & num <= 57) {
                dataMin.put("V01300", stationNumberChina);
            } else {
                dataMin.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
//            Map<String, Object> proMap = StationInfo.getProMap();
            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                //System.out.println("=========查到到==========="+sid);
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                dataMin.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                dataMin.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                //System.out.println("=========未查到到==========="+sid);
                dataMin.put("V02301", "999999");
                dataMin.put("V_ACODE", "999999");
            }

            dataMin.put("D_DATETIME", observationTime);//资料时间
            dataMin.put("V04001", (observationTime.getYear() + 1900));//资料观测年
            dataMin.put("V04002", (observationTime.getMonth() + 1));//资料观测月
            dataMin.put("V04003", observationTime.getDate());//资料观测日
            dataMin.put("V04004", observationTime.getHours());//资料观测时
            dataMin.put("V04005", observationTime.getMinutes());//资料观测分 小时不入
            dataMin.put("V05001", bean.getLatitude());//纬度
            dataMin.put("V06001", bean.getLongitude());//经度
            dataMin.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
            dataMin.put("V02001", "0");//测站类型

            dataMin.put("V_BBB", bean.getCorrectionIndicator());//更正报标志
            dataMin.put("V08010", "999999");//地面限定符 (温度数据)
            dataMin.put("V07031", bean.getHeightOfBarometerAboveMeanSeaLevel());//气压传感器海拔高度
            dataMin.put("V07032_02", "999999");//能见度传感器离本地地面（或海上平台甲板）的高度
            dataMin.put("V07032_01", "999999");//温度传感器距地面高度
            dataMin.put("V07032_04", "999999");//风传感器距地面高度
            dataMin.put("V02180", "999998");//主要天气现况检测系统
            dataMin.put("V02183", "999999");//云探测系统
            dataMin.put("V02175", "999998");//降水测量方法
            dataMin.put("V02177", "9");//雪深的测量方法
            dataMin.put("V10004", bean.getStationPressure().getValue());//本站气压
            dataMin.put("V10051", bean.getPressureReducedToMeanSeaLevel().getValue());//海平面气压
            dataMin.put("V11041", "999998");//1分钟内极大风速
            dataMin.put("V11043", "999998");//1分钟内极大风速的风向
            dataMin.put("V11201", bean.getWindDirectionCurrent().getValue());//瞬时风向  ?????????
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
            dataMin.put("V13011", bean.getPrecipitationAdditionalManualObservational().getValue());//分钟降水量
            dataMin.put("V13013", bean.getSnowDepth().getValue());//总雪深
            dataMin.put("V13033", bean.getEvaporation().getValue());//小时蒸发量
            dataMin.put("V20001_701_01", bean.getHorizontalVisibility1Minitue().getValue());//1分钟平均水平能见度
            dataMin.put("V20001_701_10", bean.getHorizontalVisibility10Minitues().getValue());//10分钟平均水平能见度
            dataMin.put("V20010", bean.getCloudAmount().getValue());//云量
            dataMin.put("V20013", bean.getCloudHeightLowAndMiddle().getValue());//云底高度
            dataMin.put("V20211", "999998");//分钟连续观测天气现象
            dataMin.put("Q10004", bean.getStationPressure().getQuality().get(1).getCode());//本站气压质量标志
            dataMin.put("Q10051", bean.getPressureReducedToMeanSeaLevel().getQuality().get(1).getCode());//海平面气压质量控制标志
            dataMin.put("Q11201", bean.getTemperature().getQuality().get(1).getCode());//瞬时风向质量标志
            dataMin.put("Q11202", bean.getWindSpeedCurrent().getQuality().get(1).getCode());//瞬时风速质量标志
            dataMin.put("Q11288", "9");//1分钟平均风向质量标志
            dataMin.put("Q11289", "9");//1分钟平均风速质量标志
            dataMin.put("Q11290", bean.getWindDirectionAt2m().getQuality().get(1).getCode());//2分钟平均风向质量标志
            dataMin.put("Q11291", bean.getWindSpeedAvg2m().getQuality().get(1).getCode());//2分钟平均风速质量标志
            dataMin.put("Q11292", bean.getWindDirectionAt10().getQuality().get(1).getCode());//10分钟平均风向质量标志
            dataMin.put("Q11293", bean.getWindSpeedAt10().getQuality().get(1).getCode());//10分钟平均风速质量标志
            dataMin.put("Q11296", bean.getWindDirectionOfMaxSpeed().getQuality().get(1).getCode());//最大风速的风向质量标志
            dataMin.put("Q12001", bean.getWindDirectionCurrent().getQuality().get(1).getCode());//气温质量标志
            dataMin.put("Q12003", bean.getWetBulbTemperature().getQuality().get(1).getCode());//露点温度质量标志
            dataMin.put("Q12030_005", bean.getSoilTemperature5CM().getQuality().get(1).getCode());//5cm地温质量标志
            dataMin.put("Q12030_010", bean.getSoilTemperature10CM().getQuality().get(1).getCode());//10cm地温质量标志
            dataMin.put("Q12030_015", bean.getSoilTemperature15CM().getQuality().get(1).getCode());//15cm地温质量标志
            dataMin.put("Q12030_020", bean.getSoilTemperature20CM().getQuality().get(1).getCode());//20cm地温质量标志
            dataMin.put("Q12030_040", bean.getSoilTemperature40CM().getQuality().get(1).getCode());//40cm地温质量标志
            dataMin.put("Q12030_080", bean.getSoilTemperature80CM().getQuality().get(1).getCode());//80cm地温质量标志
            dataMin.put("Q12030_160", bean.getSoilTemperature160CM().getQuality().get(1).getCode());//160cm地温质量标志
            dataMin.put("Q12030_320", bean.getSoilTemperature320CM().getQuality().get(1).getCode());//320cm地温质量标志
            dataMin.put("Q12120", bean.getLandSurfaceTemperature().getQuality().get(1).getCode());//地面温度质量标志
            dataMin.put("Q12314", bean.getGrassTemperature().getQuality().get(1).getCode());//草面（雪面）温度质量标志
            dataMin.put("Q13003", bean.getRelativeHumidity().getQuality().get(1).getCode());//相对湿度质量标志
            dataMin.put("Q13004", bean.getVapourPressur().getQuality().get(1).getCode());//水汽压质量标志
            dataMin.put("Q13011", bean.getPrecipitationAdditionalManualObservational().getQuality().get(1).getCode());//分钟降水量质量标志
            dataMin.put("Q13013", bean.getSnowDepth().getQuality().get(1).getCode());//总雪深质量标志
            dataMin.put("Q20001_701_01", bean.getHorizontalVisibility1Minitue().getQuality().get(1).getCode());//1分钟平均水平能见度质量标志
            dataMin.put("Q20001_701_10", bean.getHorizontalVisibility10Minitues().getQuality().get(1).getCode());//10分钟平均水平能见度质量标志
            dataMin.put("Q20010", bean.getCloudAmount().getQuality().get(1).getCode());//云量质量标志
            dataMin.put("Q20013", bean.getCloudHeightLowAndMiddle().getQuality().get(1).getCode());//云底高度质量标志


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

                List<Map<String, Object>> repMaps = transformReports_ats(reports, D_DATA_ID_min, file);
                statTF.setRepList(repMaps);

        } catch (ParseException e) {
            System.out.println("报文转换错误");
            statTF.getBuffer().append("reports inssrt error!");
        }

        return statTF;
    }

}