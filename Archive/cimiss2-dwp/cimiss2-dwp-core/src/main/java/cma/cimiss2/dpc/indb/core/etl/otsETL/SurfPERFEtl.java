package cma.cimiss2.dpc.indb.core.etl.otsETL;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.PrecipitationObservationDataReg;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.surf.DecodePRFREG;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import cma.cimiss2.dpc.indb.core.tools.DataMapMaker;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 雨量站数据格式化类 ots缓冲库
 */
public class SurfPERFEtl extends AbstractEtl<PrecipitationObservationDataReg> implements Serializable{

    private static final long serialVersionUID = -6868594135467050141L;
    static Log log = Logs.get();

    public SurfPERFEtl() {
    }

    @Override
    public ParseResult<PrecipitationObservationDataReg> extract(File file) {
        DecodePRFREG decodePRFREG = new DecodePRFREG();
        return decodePRFREG.DecodePRF(file);
    }

//    @Override
//    public Map<String, Object> transform(ParseResult<PrecipitationObservationDataReg> result, File file, String... tables) {
//
//        return null;
//    }
    
    /**
     * 报文格式化
     * @param result   报文集合
     * @param file      报文文件
     * @param tablesHelper  ots入库实体类  
     * @return  格式化后的实体类
     */
    @Override
    public StatTF transform(ParseResult<PrecipitationObservationDataReg> result, File file, OTSHelper... tablesHelper){
//        reload(intervalTime);
        //解码数据集合
        List<PrecipitationObservationDataReg> beans = result.getData();
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
            PrecipitationObservationDataReg bean = beans.get(i);
            Date observationTime = bean.getObservationTime();
            String format = new SimpleDateFormat("yyyyMMddHHmmss").format(observationTime);
            String stationNumberChina = bean.getStationNumberChina().toUpperCase();
            int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();

            String D_RECORD_ID = format + "_" + stationNumberChina;
            // 如果是 00 分的数据，入小时表
            if (observationTime.getMinutes() == 00) {
                Map<String, Object> dataHour;
                try {
                    dataHour = DataMapMaker.make(bean, 1);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.debug("PrecipitationObservationDataReg is a illegal bean.");
                    return null;
                }
                dataHour.put("D_RECORD_ID", D_RECORD_ID);//记录标识
                dataHour.put("D_DATA_ID", D_DATA_ID_hor);//资料标识
                dataHour.put("D_IYMDHM", new Date());//入库时间
                dataHour.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
                dataHour.put("D_UPDATE_TIME", new Date());//更新时间
                dataHour.put("V01301", stationNumberChina);
                if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                    //System.out.println("=========查到到==========="+sid);
                    String s = (String) getStationInfo().get(stationNumberChina + "+01");
                    dataHour.put("V07001", s.split(",")[3]);
                    dataHour.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                    dataHour.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
                } else {
                    dataHour.put("V07001", "999999");
                    dataHour.put("V02301", "999999");
                    dataHour.put("V_ACODE", "999999");
                }

                if (num >= 48 & num <= 57) {
                    dataHour.put("V01300", stationNumberChina);
                } else {
                    dataHour.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
                }
                dataHour.put("V13019", bean.getPrecipitation1Hour().getValue());
                dataHour.put("Q13019", bean.getPrecipitation1Hour().getQuality().get(0).getCode());
                dataHour.put("V_BBB", "000");

                dataHour.put("V04001", observationTime.getYear() + 1900);//资料观测年
                dataHour.put("V04002", observationTime.getMonth() + 1);//资料观测月
                dataHour.put("V04003", observationTime.getDate());//资料观测日
                dataHour.put("V04004", observationTime.getHours());//资料观测时
                //不为空的值赋值
                dataHour.put("V05001", "999999");
                dataHour.put("V06001", "999999");
                dataHour.put("V07031", "999999");
                dataHour.put("V07032_04", "999999");
                dataHour.put("V07032_01", "999999");
                dataHour.put("V07032_02", "999999");
                dataHour.put("V02001", "999999");
                dataHour.put("V08010", "999999");
                dataHour.put("V02183", "999999");
                dataHour.put("V10004", "999999");
                dataHour.put("V10051", "999999");
                dataHour.put("V10061", "999999");
                dataHour.put("V10062", "999999");
                dataHour.put("V10301", "999999");
                dataHour.put("V10301_052", "999999");
                dataHour.put("V10302", "999999");
                dataHour.put("V10302_052", "999999");
                dataHour.put("V12001", "999999");
                dataHour.put("V12011", "999999");
                dataHour.put("V12011_052", "999999");
                dataHour.put("V12012", "999999");
                dataHour.put("V12012_052", "999999");
                dataHour.put("V12405", "999999");
                dataHour.put("V12016", "999999");
                dataHour.put("V12017", "999999");
                dataHour.put("V12003", "999999");
                dataHour.put("V13003", "999999");
                dataHour.put("V13007", "999999");
                dataHour.put("V13007_052", "999999");
                dataHour.put("V13004", "999999");
                dataHour.put("V13019", "999999");
                dataHour.put("V13020", "999999");
                dataHour.put("V13021", "999999");
                dataHour.put("V13022", "999999");
                dataHour.put("V13023", "999999");
                dataHour.put("V04080_04", "999999");
                dataHour.put("V13033", "999999");
                dataHour.put("V11290", "999999");
                dataHour.put("V11291", "999999");
                dataHour.put("V11292", "999999");
                dataHour.put("V11293", "999999");
                dataHour.put("V11296", "999999");
                dataHour.put("V11042", "999999");
                dataHour.put("V11042_052", "999999");
                dataHour.put("V11201", "999999");
                dataHour.put("V11202", "999999");
                dataHour.put("V11211", "999999");
                dataHour.put("V11046", "999999");
                dataHour.put("V11046_052", "999999");
                dataHour.put("V11503_06", "999999");
                dataHour.put("V11504_06", "999999");
                dataHour.put("V11503_12", "999999");
                dataHour.put("V11504_12", "999999");
                dataHour.put("V12120", "999999");
                dataHour.put("V12311", "999999");
                dataHour.put("V12311_052", "999999");
                dataHour.put("V12121", "999999");
                dataHour.put("V12121_052", "999999");
                dataHour.put("V12013", "999999");
                dataHour.put("V12030_005", "999999");
                dataHour.put("V12030_010", "999999");
                dataHour.put("V12030_015", "999999");
                dataHour.put("V12030_020", "999999");
                dataHour.put("V12030_040", "999999");
                dataHour.put("V12030_080", "999999");
                dataHour.put("V12030_160", "999999");
                dataHour.put("V12030_320", "999999");
                dataHour.put("V12314", "999999");
                dataHour.put("V12315", "999999");
                dataHour.put("V12315_052", "999999");
                dataHour.put("V12316", "999999");
                dataHour.put("V12316_052", "999999");
                dataHour.put("V20001_701_01", "999999");
                dataHour.put("V20001_701_10", "999999");
                dataHour.put("V20059", "999999");
                dataHour.put("V20059_052", "999999");
                dataHour.put("V20001", "999999");
                dataHour.put("V20010", "999999");
                dataHour.put("V13011", "999999");
                dataHour.put("V20051", "999999");
                dataHour.put("V20011", "999999");
                dataHour.put("V20013", "999999");
                dataHour.put("V20350_01", "999999");
                dataHour.put("V20350_02", "999999");
                dataHour.put("V20350_03", "999999");
                dataHour.put("V20350_04", "999999");
                dataHour.put("V20350_05", "999999");
                dataHour.put("V20350_06", "999999");
                dataHour.put("V20350_07", "999999");
                dataHour.put("V20350_08", "999999");
                dataHour.put("V20350_11", "999999");
                dataHour.put("V20350_12", "999999");
                dataHour.put("V20350_13", "999999");
                dataHour.put("V20003", "999999");
                dataHour.put("V04080_05", "999999");
                dataHour.put("V20004", "999999");
                dataHour.put("V20005", "999999");
                dataHour.put("V20062", "999999");
                dataHour.put("V13013", "999999");
                dataHour.put("V13330", "999999");
                dataHour.put("V20330_01", "999999");
                dataHour.put("V20331_01", "999999");
                dataHour.put("V20330_02", "999999");
                dataHour.put("V20331_02", "999999");

                statTF.getHorList().add(dataHour);
            }
            Map<String, Object> dataMinute = new HashMap<String, Object>();
            dataMinute.put("D_RECORD_ID", format + "_" + stationNumberChina);//记录标识
            dataMinute.put("D_DATA_ID", D_DATA_ID_min);//资料标识
            dataMinute.put("D_IYMDHM", new Date());//入库时间
            dataMinute.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            dataMinute.put("D_UPDATE_TIME", new Date());//更新时间
            dataMinute.put("D_DATETIME", bean.getObservationTime());
            dataMinute.put("V01301", stationNumberChina);
            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                dataMinute.put("V07001", s.split(",")[3]);
                dataMinute.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                dataMinute.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                dataMinute.put("V07001", "999999");
                dataMinute.put("V02301", "999999");
                dataMinute.put("V_ACODE", "999999");
            }
            dataMinute.put("V01301", stationNumberChina);
            if (num >= 48 & num <= 57) {
                dataMinute.put("V01300", stationNumberChina);
            } else {
                dataMinute.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
            int size = bean.getPrecipitationEveryMinutes().size();
            dataMinute.put("V13011", bean.getPrecipitationEveryMinutes().get(size - 1).getValue());
            dataMinute.put("Q13011", bean.getPrecipitationEveryMinutes().get(size - 1).getQuality().get(0).getCode());
            dataMinute.put("V_BBB", "000");

            dataMinute.put("V04001", observationTime.getYear() + 1900);//资料观测年
            dataMinute.put("V04002", observationTime.getMonth() + 1);//资料观测月
            dataMinute.put("V04003", observationTime.getDate());//资料观测日
            dataMinute.put("V04004", observationTime.getHours());//资料观测时
            dataMinute.put("V04005", observationTime.getMinutes());//资料观测分 小时不入
            statTF.getMinList().add(dataMinute);
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
