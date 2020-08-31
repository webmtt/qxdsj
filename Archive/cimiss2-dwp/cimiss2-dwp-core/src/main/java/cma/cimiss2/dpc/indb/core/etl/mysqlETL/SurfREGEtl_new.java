package cma.cimiss2.dpc.indb.core.etl.mysqlETL;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataReg;
import cma.cimiss2.dpc.decoder.surf.DecodeREG;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.tools.DataMapMaker;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;


import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 区域站数据格式化类 mysql缓冲库
 */
@IocBean(fields = {"rdb","cimiss"},singleton = false)
public class SurfREGEtl_new extends AbstractEtl<SurfaceObservationDataReg> {
	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final long serialVersionUID = -6914120843747532109L;
    static Log log = Logs.get();
    public SurfREGEtl_new() {
    }


    @Override
    public ParseResult<SurfaceObservationDataReg> extract(File file) {
        DecodeREG decodeREG = new DecodeREG();
        return (ParseResult<SurfaceObservationDataReg>) decodeREG.decode(file, new HashSet<String>());
    }

    /**
     * 报文格式化
     * @param result   报文集合
     * @param file      报文文件
     * @param tables    表名
     * @return  格式化后的实体类
     */
    @SuppressWarnings("deprecation")
    @Override
    public StatTF transform(ParseResult<SurfaceObservationDataReg> result, File file, Map<String,String> codeMap, String... tables) {
        //返回集合
        Map<String, Object> allMap = new HashMap<>();
        //解码数据集合
        List<SurfaceObservationDataReg> beans = result.getData();
        //报文集合
        List<ReportInfo> reports = result.getReports();

        //小时表
        String horTable = tables[0];
        //分钟表
        String minTable = tables[1];
        //分钟降水表
        String minPreTable = tables[2];
        //累计降水表
        String cumPreTable = tables[3];
        //报文表
        String repTable = tables[4];

        StatTF statTF = new StatTF();

        String cts_code = codeMap.get("cts_code");
        String hor_sod_code = codeMap.get("hor_sod_code");
        String mul_sod_code = codeMap.get("mul_sod_code");
        String pre_sod_code = codeMap.get("pre_sod_code");
        String accu_sod_code = codeMap.get("accu_sod_code");
        String report_sod_code = codeMap.get("report_sod_code");
        
        for (int i = 0; i < beans.size(); i++) {
            SurfaceObservationDataReg bean = beans.get(i);
            String V_BBB = bean.getCorrectionIndicator();
            Date observationTime = bean.getObservationTime();
            String format = new SimpleDateFormat("yyyyMMddHHmmss").format(observationTime);
            String stationNumberChina = bean.getStationNumberChina().toUpperCase();
            String D_RECORD_ID = format + "_" + stationNumberChina;
            int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();
            if (!"000".equals(V_BBB)) {
            	String D_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(observationTime);
                corrMessage(D_RECORD_ID, V_BBB, horTable,"rdb",getEvent(),D_DATETIME);
                corrMessage(D_RECORD_ID, V_BBB, minTable,"rdb",getEvent(),D_DATETIME);
                String fileName = file.getName();
                String[] fileNameSplit = fileName.split("_");
                String V_TT = fileNameSplit[6].split("-")[0];
                corrMessage(D_RECORD_ID+"_"+getEvent()+"_"+ V_TT, V_BBB, repTable,"cimiss",getEvent(),D_DATETIME);
              //分钟降水
                for(int j = 0; j < 5; j++){
                	 Date date = new Date(observationTime.getTime());
                     date.setMinutes(observationTime.getMinutes() - j);
                     String D_DATETIME_PRE = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                     String format_pre = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
                     corrMessage(format_pre + "_" + stationNumberChina, V_BBB, minPreTable, "rdb",getEvent(),D_DATETIME_PRE);
                }
                //累计降水
                corrMessage(D_RECORD_ID, V_BBB, cumPreTable, "rdb",getEvent(),D_DATETIME);
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
                data.put(".table", horTable);
                data.put("D_DATA_ID", hor_sod_code);//资料标识
                data.put("D_RECORD_ID", D_RECORD_ID);//记录标识
                data.put("D_IYMDHM", new Date());//入库时间
                data.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
                //data.put("D_UPDATE_TIME", new Date());//更新时间
                data.put("D_SOURCE_ID",getEvent());//CTS编码
                data.put("V01301", stationNumberChina);
                if (num >= 48 & num <= 57) {
                    data.put("V01300", stationNumberChina);
                } else {
                    data.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
                }
                //Map<String, Object> proMap = StationInfo.getProMap();
                if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                    String s = (String) getStationInfo().get(stationNumberChina + "+01");
                    data.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                    data.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
                } else {
                    data.put("V02301", "999999");
                    //data.put("V_ACODE", "999999");
                    data.put("V_ACODE",getVacode(file.getName(),stationNumberChina));
                }
                //data.put("D_DATETIME", obsTimeCalendar);//资料时间
                data.put("V04001", observationTime.getYear() + 1900);//资料观测年
                data.put("V04002", observationTime.getMonth() + 1);//资料观测月
                data.put("V04003", observationTime.getDate());//资料观测日
                data.put("V04004", observationTime.getHours());//资料观测时
                //data.put("V04005", observationTime.getMinutes());//资料观测分 小时不入
                data.put("V07032_04", "999999");
                data.put("V07032_01", "999999");
                data.put("V07032_02", "999999");
                data.put("V02001", "0");

                data.put("V08010", "999999");//地面限定符（温度数据）
                data.put("V02183", "999999");//云探测系统

                data.put("V10061", "999999");//3小时变压
                data.put("V10062", "999999");//24小时变压
                data.put("V12405", "999999");//24小时变温
                data.put("V12016", "999999");//过去24小时最高气温
                data.put("V12017", "999999");//过去24小时最低气温
                data.put("V13020", "999999");//过去3小时降水量
                data.put("V13021", "999999");//过去6小时降水量
                data.put("V13022", "999999");//过去12小时降水量
                data.put("V13023", "999999");//过去24小时降水量
                data.put("V04080_04", "999999");//人工加密观测降水量描述时间周期
                data.put("V13011", "999999");//人工加密观测降水量
                data.put("V11503_06", "999999");//过去6小时极大瞬时风向
                data.put("V11504_06", "999999");//过去6小时极大瞬时风速
                data.put("V11503_12", "999999");//过去12小时极大瞬时风向
                data.put("V11504_12", "999999");//过去12小时极大瞬时风速
                data.put("V12013", "999999");//过去12小时最低地面温度
                //data.put("V20001_701_01", "999999");//1分钟平均水平能见度
                data.put("V20001_701_10", "999998");//10分钟平均水平能见度
                data.put("V04080_05", "999999");//过去天气描述时间周期
                data.put("V20004", "999999");//过去天气1
                data.put("V20005", "999999");//过去天气2
                data.put("V20330_01", "999999");//冻土第1冻结层上限值
                data.put("V20331_01", "999999");//冻土第1冻结层下限值
                data.put("V20330_02", "999999");//冻土第2冻结层上限值
                data.put("V20331_02", "999999");//冻土第2冻结层下限值
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
                //data.put("Q20001_701_01", "7");//1分钟平均水平能见度质量标志
                data.put("Q20001_701_10", "9");//10分钟平均水平能见度质量标志
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
                /*data.put("V20350_11", bean.getCloudType1Detail(0));//
                data.put("V20350_12", bean.getCloudType1Detail(1));//
                data.put("V20350_13", bean.getCloudType1Detail(2));//*/

                statTF.getHorList().add(data);
            }
            //入分钟表
            Map<String, Object> dataMin = new HashMap<>();
            dataMin.put(".table", minTable);
            dataMin.put("D_RECORD_ID", D_RECORD_ID);//记录标识
            dataMin.put("D_DATA_ID", mul_sod_code);//资料标识
            dataMin.put("D_IYMDHM", new Date());//入库时间
            dataMin.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            //dataMin.put("D_UPDATE_TIME", new Date());//更新时间
            dataMin.put("D_SOURCE_ID",getEvent());//CTS编码
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
                //dataMin.put("V_ACODE", "999999");
                dataMin.put("V_ACODE",getVacode(file.getName(),stationNumberChina));
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

            dataMin.put("V_BBB", bean.getCorrectionIndicator());//更正报标志???????/更改 原来为000
            dataMin.put("V08010", "999999");//地面限定符 (温度数据)
            dataMin.put("V07031", bean.getHeightOfBarometerAboveMeanSeaLevel());//气压传感器海拔高度
            dataMin.put("V07032_02", "999999");//能见度传感器离本地地面（或海上平台甲板）的高度
            dataMin.put("V07032_01", "999999");//温度传感器距地面高度
            dataMin.put("V07032_04", "999999");//风传感器距地面高度
            dataMin.put("V02180", "999999");//主要天气现况检测系统
            dataMin.put("V02183", "999999");//云探测系统
            dataMin.put("V02175", "999999");//降水测量方法
            dataMin.put("V02177", "7");//雪深的测量方法
            dataMin.put("V10004", bean.getStationPressure().getValue());//本站气压
            dataMin.put("V10051", bean.getPressureReducedToMeanSeaLevel().getValue());//海平面气压
            dataMin.put("V11041", "999999");//1分钟内极大风速
            dataMin.put("V11043", "999999");//1分钟内极大风速的风向
            //dataMin.put("V11201", "999998");//瞬时风向
            dataMin.put("V11201", bean.getWindDirectionCurrent().getValue());
            dataMin.put("V11202", bean.getWindSpeedCurrent().getValue());//瞬时风速
            dataMin.put("V11288", "999999");//1分钟平均风向
            dataMin.put("V11289", "999999");//1分钟平均风速
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
            //dataMin.put("V13011", "999999");//分钟降水量
            //分钟降水量
            int size = bean.getPrecipitationEveryMinutes().size();
            Double V13011 = 0.0;
            Integer Q13011 = 0;
            if(observationTime.getMinutes() == 00){
            	//0点时入最后两位
            	V13011 = bean.getPrecipitationEveryMinutes().get(size - 1).getValue();
            	Q13011 = bean.getPrecipitationEveryMinutes().get(size - 1).getQuality().get(0).getCode();
            }else{
            	V13011 = bean.getPrecipitationEveryMinutes().get(observationTime.getMinutes() - 1).getValue();
            	Q13011 = bean.getPrecipitationEveryMinutes().get(observationTime.getMinutes() - 1).getQuality().get(0).getCode();
            }
        	dataMin.put("V13011", V13011);
        	dataMin.put("Q13011",Q13011);
            dataMin.put("V13013", bean.getSnowDepth());//总雪深
            dataMin.put("V13033", bean.getEvaporation().getValue());//小时蒸发量
            //dataMin.put("V20001_701_01", "999999");//1分钟平均水平能见度
            dataMin.put("V20001_701_01", bean.getHorizontalVisibilityCurrent().getValue());
            dataMin.put("V20001_701_10", "999998");//10分钟平均水平能见度
            dataMin.put("V20010", bean.getCloudAmount());//云量
            dataMin.put("V20013", bean.getCloudHeightLowAndMiddle());//云底高度
            dataMin.put("V20211", "999999");//分钟连续观测天气现象
            dataMin.put("Q10004", bean.getStationPressure().getQuality().get(0).getCode());//本站气压质量标志
            dataMin.put("Q10051", bean.getPressureReducedToMeanSeaLevel().getQuality().get(0).getCode());//海平面气压质量控制标志
            //dataMin.put("Q11201", bean.getTemperature().getQuality().get(0).getCode());//瞬时风向质量标志
            dataMin.put("Q11201", bean.getWindDirectionCurrent().getQuality().get(0).getCode());//瞬时风向质量标志
            dataMin.put("Q11202", bean.getWindSpeedCurrent().getQuality().get(0).getCode());//瞬时风速质量标志
            dataMin.put("Q11288", "7");//1分钟平均风向质量标志
            dataMin.put("Q11289", "7");//1分钟平均风速质量标志
            dataMin.put("Q11290", bean.getWindDirectionAt2m().getQuality().get(0).getCode());//2分钟平均风向质量标志
            dataMin.put("Q11291", bean.getWindSpeedAvg2m().getQuality().get(0).getCode());//2分钟平均风速质量标志
            dataMin.put("Q11292", bean.getWindDirectionAt10().getQuality().get(0).getCode());//10分钟平均风向质量标志
            dataMin.put("Q11293", bean.getWindSpeedAt10().getQuality().get(0).getCode());//10分钟平均风速质量标志
            dataMin.put("Q11296", bean.getWindDirectionOfMaxSpeed().getQuality().get(0).getCode());//最大风速的风向质量标志
            //dataMin.put("Q12001", bean.getWindDirectionCurrent().getQuality().get(0).getCode());//气温质量标志
            dataMin.put("Q12001", bean.getTemperature().getQuality().get(0).getCode());//气温质量标志
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
            //dataMin.put("Q13011", "7");//分钟降水量质量标志
            dataMin.put("Q13013", "7");//总雪深质量标志
            //dataMin.put("Q20001_701_01", "7");//1分钟平均水平能见度质量标志
            dataMin.put("Q20001_701_01", bean.getHorizontalVisibilityCurrent().getQuality().get(0).getCode());//1分钟平均水平能见度质量标志
            dataMin.put("Q20001_701_10", "9");//10分钟平均水平能见度质量标志
            dataMin.put("Q20010", "7");//云量质量标志
            dataMin.put("Q20013", "7");//云底高度质量标志
            statTF.getMinList().add(dataMin);
            
            //入分钟降水表,是5的整倍数,入前五分钟的数据
            if(observationTime.getMinutes() != 00 && observationTime.getMinutes()%5 == 00) {
                for(int j = 0; j < 5; j++) {
                	Map<String, Object> dataMinPre = new HashMap<String, Object>();
                	dataMinPre.put(".table", minPreTable);
                    dataMinPre.put("D_DATA_ID", pre_sod_code);//资料标识
                    dataMinPre.put("D_IYMDHM", new Date());//入库时间
                    dataMinPre.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
                    dataMinPre.put("D_UPDATE_TIME", new Date());//更新时间
                    dataMinPre.put("D_SOURCE_ID",getEvent());//CTS编码
                    
                    Date date = new Date(observationTime.getTime());
                    // 2020-3-31 chy 1-5, 6-10
                    date.setMinutes(observationTime.getMinutes() - j); // 5,4,3,2,1
//                    date.setMinutes(observationTime.getMinutes() - j - 1);  // 0-4 5-9 10-14
                    dataMinPre.put("D_DATETIME", date);//资料时间
                    String formatMinPre = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
                    dataMinPre.put("D_RECORD_ID", formatMinPre + "_" + stationNumberChina);//记录标识
                    
                    dataMinPre.put("V_BBB", bean.getCorrectionIndicator());
                    dataMinPre.put("V01301", stationNumberChina);
                    dataMinPre.put("V05001", bean.getLatitude());//纬度
                    dataMinPre.put("V06001", bean.getLongitude());//经度
                    dataMinPre.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
                    dataMinPre.put("V08010", "999999");//地面限定符 (温度数据)
                    dataMinPre.put("V02001", "0");//测站类型
                    dataMinPre.put("V02301", dataMin.get("V02301"));//台站级别
                    dataMinPre.put("V_ACODE", dataMin.get("V_ACODE"));//中国行政区划代码
                    dataMinPre.put("V04001", (date.getYear() + 1900));//资料观测年
                    dataMinPre.put("V04002", (date.getMonth() + 1));//资料观测月
                    dataMinPre.put("V04003", date.getDate());//资料观测日
                    dataMinPre.put("V04004", date.getHours());//资料观测时
                    dataMinPre.put("V04005", date.getMinutes());//资料观测分 小时不入
                    //2020-3-31 chy 
                    dataMinPre.put("V13011", bean.getPrecipitationEveryMinutes().get(date.getMinutes() - 1).getValue());
                    dataMinPre.put("Q13011", bean.getPrecipitationEveryMinutes().get(date.getMinutes() - 1).getQuality().get(0).getCode());
                 
//                    dataMinPre.put("V13011", bean.getPrecipitationEveryMinutes().get(date.getMinutes()).getValue());
//                    dataMinPre.put("Q13011", bean.getPrecipitationEveryMinutes().get(date.getMinutes()).getQuality().get(0).getCode());
                    statTF.getMinPreList().add(dataMinPre);
                }
             }
//             整点入前一小时55- 59分钟的数据
            // 2020-3-31 chy 56 ` 0 分钟的数据
            if(observationTime.getMinutes() == 00 ) {
            	 Calendar calendar = Calendar.getInstance();
                 calendar.setTime(observationTime);
                 calendar.add(Calendar.HOUR_OF_DAY, -1);
                 Date date = new Date(calendar.getTime().getTime());
            	 List<Integer> minutes0 = selectInsert(report_sod_code, observationTime, bean.getStationNumberChina(),
            			cimiss);
            	 //add by liym
            	 List<Integer> minutes = new ArrayList<>(Arrays.asList(5,10,15,20,25,30,35,40,45,50,55));
            	 minutes.removeAll(minutes0);
            	 //end--add by liym
            	 
            	 minutes.add(60);
            	 for(int m = 0; m < minutes.size(); m ++){  // 5, 15,..., 55
					for(int t = minutes.get(m) - 4; t <= minutes.get(m); t ++ ){//如5，则入1,2,3,4,5
						if(t == 60){
							date.setTime(observationTime.getTime());
						}else{
							date.setMinutes(t);
						}
	                	Map<String, Object> dataMinPre = new HashMap<String, Object>();
	                	dataMinPre.put(".table", minPreTable);
	                    dataMinPre.put("D_DATA_ID", pre_sod_code);//资料标识
	                    dataMinPre.put("D_IYMDHM", new Date());//入库时间
	                    dataMinPre.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
	                    dataMinPre.put("D_UPDATE_TIME", new Date());//更新时间
	                    dataMinPre.put("D_SOURCE_ID",getEvent());//CTS编码
	
	                    dataMinPre.put("D_DATETIME", new Date(date.getTime()));//资料时间
	                    String formatMinPre = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
	                    dataMinPre.put("D_RECORD_ID", formatMinPre + "_" + stationNumberChina);//记录标识
	                    
	                    dataMinPre.put("V_BBB", bean.getCorrectionIndicator());
	                    dataMinPre.put("V01301", stationNumberChina);
	                    dataMinPre.put("V05001", bean.getLatitude());//纬度
	                    dataMinPre.put("V06001", bean.getLongitude());//经度
	                    dataMinPre.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
	                    dataMinPre.put("V08010", "999999");//地面限定符 (温度数据)
	                    dataMinPre.put("V02001", "0");//测站类型
	                    dataMinPre.put("V02301", dataMin.get("V02301"));//台站级别
	                    dataMinPre.put("V_ACODE", dataMin.get("V_ACODE"));//中国行政区划代码
	                    dataMinPre.put("V04001", (date.getYear() + 1900));//资料观测年
	                    dataMinPre.put("V04002", (date.getMonth() + 1));//资料观测月
	                    dataMinPre.put("V04003", date.getDate());//资料观测日
	                    dataMinPre.put("V04004", date.getHours());//资料观测时
	                    dataMinPre.put("V04005", date.getMinutes());//资料观测分 小时不入
	             
	                	dataMinPre.put("V13011", bean.getPrecipitationEveryMinutes().get(t - 1).getValue());
	                	dataMinPre.put("Q13011", bean.getPrecipitationEveryMinutes().get(t - 1).getQuality().get(0).getCode());
	                   
	                    statTF.getMinPreList().add(dataMinPre);
					}
					//add by liym 整点数据在查询报告表后，也要补入累计降水表相应分钟（一定是5的倍数）的数据
					Map<String, Object> dataCumPre = new HashMap<>();
	                dataCumPre.put(".table", cumPreTable);
	                dataCumPre.put("D_RECORD_ID", D_RECORD_ID);//记录标识
	                dataCumPre.put("D_DATA_ID", accu_sod_code);//资料标识
	                dataCumPre.put("D_IYMDHM", new Date());//入库时间
	                dataCumPre.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
	                dataCumPre.put("D_UPDATE_TIME", new Date());//更新时间
	                dataCumPre.put("D_SOURCE_ID",getEvent());//CTS编码
	                dataCumPre.put("D_DATETIME", new Date(date.getTime()));//资料时间
	                dataCumPre.put("V_BBB", bean.getCorrectionIndicator());
	                dataCumPre.put("V01301", stationNumberChina);
	                dataCumPre.put("V05001", bean.getLatitude());//纬度
	                dataCumPre.put("V06001", bean.getLongitude());//经度
	                dataCumPre.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
	                dataCumPre.put("V02001", "0");//测站类型
	                dataCumPre.put("V02301", dataMin.get("V02301"));//台站级别
	                dataCumPre.put("V_ACODE", dataMin.get("V_ACODE"));//中国行政区划代码
	                dataCumPre.put("V04001", (date.getYear() + 1900));//资料观测年
	                dataCumPre.put("V04002", (date.getMonth() + 1));//资料观测月
	                dataCumPre.put("V04003", date.getDate());//资料观测日
	                dataCumPre.put("V04004", date.getHours());//资料观测时
	                dataCumPre.put("V04005", date.getMinutes());//资料观测分 小时不入
	                List<Object> cumPre = getCumPre5And10(bean,date);
	                dataCumPre.put("V13392_005",Double.valueOf((String)cumPre.get(0)));//5分钟累计降水量
	                dataCumPre.put("Q13392_005",(Integer)cumPre.get(1));//5分钟累计降水量的指控码
	                dataCumPre.put("V13392_010",Double.valueOf((String)cumPre.get(2)));//10分钟累计降水量
	                dataCumPre.put("Q13392_010",(Integer)cumPre.get(3));//10分钟累计降水量的指控码
	                statTF.getCumPreList().add(dataCumPre);
            	 }
            }
             //入5分钟和10分钟累计降水表
            if(observationTime.getMinutes()%5 == 00 && observationTime.getMinutes() != 00) {
            	Map<String, Object> dataCumPre = new HashMap<>();
                dataCumPre.put(".table", cumPreTable);
                dataCumPre.put("D_RECORD_ID", D_RECORD_ID);//记录标识
                dataCumPre.put("D_DATA_ID", accu_sod_code);//资料标识
                dataCumPre.put("D_IYMDHM", new Date());//入库时间
                dataCumPre.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
                dataCumPre.put("D_UPDATE_TIME", new Date());//更新时间
                dataCumPre.put("D_SOURCE_ID",getEvent());//CTS编码
                dataCumPre.put("D_DATETIME", observationTime);
                dataCumPre.put("V_BBB", bean.getCorrectionIndicator());
                dataCumPre.put("V01301", stationNumberChina);
                dataCumPre.put("V05001", bean.getLatitude());//纬度
                dataCumPre.put("V06001", bean.getLongitude());//经度
                dataCumPre.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
                dataCumPre.put("V02001", "0");//测站类型
                dataCumPre.put("V02301", dataMin.get("V02301"));//台站级别
                dataCumPre.put("V_ACODE", dataMin.get("V_ACODE"));//中国行政区划代码
                dataCumPre.put("V04001", (observationTime.getYear() + 1900));//资料观测年
                dataCumPre.put("V04002", (observationTime.getMonth() + 1));//资料观测月
                dataCumPre.put("V04003", observationTime.getDate());//资料观测日
                dataCumPre.put("V04004", observationTime.getHours());//资料观测时
                dataCumPre.put("V04005", observationTime.getMinutes());//资料观测分 小时不入
                List<Object> cumPre = getCumPre5And10(bean,observationTime);
                dataCumPre.put("V13392_005",Double.valueOf((String)cumPre.get(0)));//5分钟累计降水量
                dataCumPre.put("Q13392_005",(Integer)cumPre.get(1));//5分钟累计降水量的指控码
                dataCumPre.put("V13392_010",Double.valueOf((String)cumPre.get(2)));//10分钟累计降水量
                dataCumPre.put("Q13392_010",(Integer)cumPre.get(3));//10分钟累计降水量的指控码
                statTF.getCumPreList().add(dataCumPre);
            }
            statTF.getBuffer().append(stationNumberChina + "  " + format + "  " + "000" + "\n");
        }
        try {
            statTF.setRepList(transformReports(reports, report_sod_code, getEvent(), file, repTable));
        } catch (ParseException e) {
            statTF.getBuffer().append("reports inssrt error!");
        }
        return statTF;
    }
    /*
     * 获取5分钟和10分钟的累计降水量
     * 
     */
    @SuppressWarnings("deprecation")
	public List<Object> getCumPre5And10(SurfaceObservationDataReg bean,Date observationTime){
    	List<Object> resultList = new ArrayList<Object>();
    	Double cumPre5 = 0.0;//5分钟累计降水
        Integer cumPre5Q = 0;//5分钟累计降水的指控码
        Double cumPre10 = 0.0;//10分钟累计降水
        Integer cumPre10Q = 0;//10分钟累计降水的指控码
        int missV = 0;
        int missQ = 0;
        
        //5分钟累计降水
        Date date = new Date(observationTime.getTime());
        date.setMinutes(date.getMinutes() - 1);//转换下兼容00点时的取值
        for(int j = 0;j < 5; j++) {
        	Double cumPreV = bean.getPrecipitationEveryMinutes().get(date.getMinutes() - j).getValue();
        	int cumPreQ = bean.getPrecipitationEveryMinutes().get(date.getMinutes() - j).getQuality().get(0).getCode();
        	if(cumPreV != 999999 && cumPreV != 999990){
        		cumPre5 += cumPreV;
        	}else{
        		missV++;
        	}
        	if(cumPreQ > 4 || cumPreQ == 2) {
        		missQ++;
        	}else{
        		cumPre5Q = getCommandCode(cumPre5Q,cumPreQ);
        	}
        }
        //要素值为缺测的数目达到2或者质控码不为0、1、3、4的数目达到2时，则该累积降水赋值为缺测999999,指控码为9
        if(missV > 1 || missQ >1) {
        	cumPre5 = 999999.0;
        	cumPre5Q = 9;
        }
        
        //10分钟累计降水
        cumPre10Q = cumPre5Q;
        if(observationTime.getMinutes() != 05) {
        	for(int j = 5;j < 10; j++) {
            	Double cumPreV = bean.getPrecipitationEveryMinutes().get(date.getMinutes() - j).getValue();
            	int cumPreQ = bean.getPrecipitationEveryMinutes().get(date.getMinutes() - j).getQuality().get(0).getCode();
            	if(cumPreV != 999999 && cumPreV != 999990){
            		cumPre10 += cumPreV;
            	}else{
            		missV++;
            	}
            	if(cumPreQ > 4 || cumPreQ == 2) {
            		missQ++;
            	}else{
            		cumPre10Q = getCommandCode(cumPre10Q,cumPreQ);
            	}
            }
        	if(missV > 1 || missQ >1) {
        		cumPre10 = 999999.0;
        		cumPre10Q = 9;
        	}else{
        		cumPre10 += cumPre5;
        	}
        }else{
        	//05分的10分钟累计降水为缺测
        	cumPre10 = 999999.0;
        	cumPre10Q = 9;
        }
        //resultList.add(0, cumPre5);
        resultList.add(0,  String.format("%.1f", cumPre5));
        resultList.add(1, cumPre5Q);
        //resultList.add(2, cumPre10);
        resultList.add(2,  String.format("%.1f", cumPre10));
        resultList.add(3, cumPre10Q);
    	return resultList;
    }
// 2020-4-3 chy
    /**
	 * 整点时，查询报告表，返回要入库 的分钟
	 * @param date
	 * @return
	 */
	public List<Integer> selectInsert(String report_sod_code, Date datetime, String V01301,
			Dao cimiss){
		List<Integer> minutes = new ArrayList<>();
		List<Integer> miniteList = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.add(Calendar.HOUR_OF_DAY, -1); // 例如 2:00->1:00
		//add by liym
		StringBuffer stringBuffer=new StringBuffer();
		
		for(int i = 0; i < 11; i ++){
			cal.add(Calendar.MINUTE, 5);// 1:00->1:05->1:10  ... 55(第11次)
			Date dt = new Date(cal.getTime().getTime());
			stringBuffer.append(" select distinct(V04005) from SURF_WEA_CHN_REP_TAB where d_datetime='" + sdf2.format(dt) + "'"
					+ " and V01301=" + "'" + V01301 + "' "
					+ " and d_data_id='" + report_sod_code + "' ");
			if(i!=10){
				stringBuffer.append(" union ");
			}
		}
		String select0 =stringBuffer.toString();
//		Date dt0 = new Date(cal.getTime().getTime());
//		String select0 = "select distinct(v04005) from SURF_WEA_CHN_REP_TAB where " + 
//				"d_datetime>" + "'" + sdf2.format(dt0) + "'" +
//				"and d_datetime<" + "'" + sdf2.format(datetime) + "'" +
//				" and V01301=" + "'" + V01301 + "'" + 
//				" and d_data_id='" + report_sod_code + "' "+
//				" and mod(v04005,5)=0";
		Sql repSelectSql0 = Sqls.create(select0);
		repSelectSql0.setCallback(new SqlCallback() {
          @Override
          public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
              List<Integer> list = new LinkedList<Integer>();
              while (rs.next())
                  list.add(Integer.parseInt(rs.getString(1)));// 数量
              return list;
          }
      });
		cimiss.execute(repSelectSql0);
		System.out.println(select0);
		miniteList = repSelectSql0.getList(Integer.class);
		//end ---add by liym
//		for(int i = 0; i < 11; i ++){
//			cal.add(Calendar.MINUTE, 5);// 1:00->1:05->1:10  ... 55(第11次)
//			Date dt = new Date(cal.getTime().getTime());
//			String select = "select count(*) from SURF_WEA_CHN_REP_TAB where " + 
//				"d_datetime=" + "'" + sdf2.format(dt) + "'" +
//				" and V01301=" + "'" + V01301 + "'" + 
//				" and d_data_id='" + report_sod_code + "' limit 1";
//			Sql repSelectSql = Sqls.create(select);
//			repSelectSql.setCallback(new SqlCallback() {
//                @Override
//                public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
//                    List<String> list = new LinkedList<String>();
//                    while (rs.next())
//                        list.add(rs.getString(1));// 数量
//                    return list;
//                }
//            });
//			cimiss.execute(repSelectSql);
//			System.out.println(select);
//			List<String> miniteList = repSelectSql.getList(String.class);
//			if(!miniteList.isEmpty()){
//				if(miniteList.get(0).equals("0"))
//					minutes.add(dt.getMinutes()); //库中没有，需要入库
//				}
//			}
		return miniteList;
	}// end method
    // end 2020-4-3 chy
}
