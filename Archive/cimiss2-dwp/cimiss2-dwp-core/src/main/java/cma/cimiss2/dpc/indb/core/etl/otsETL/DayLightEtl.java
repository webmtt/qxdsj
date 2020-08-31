package cma.cimiss2.dpc.indb.core.etl.otsETL;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.QCElement;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfWeaChnSsdHor;
import cma.cimiss2.dpc.decoder.surf.DecodeSS;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import cma.cimiss2.dpc.indb.core.tools.DataMapMaker;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日照数据格式化类 ots缓冲库
 */
public class DayLightEtl extends AbstractEtl<SurfWeaChnSsdHor> {

    static Log log = Logs.get();


    public DayLightEtl() {
    }

    @Override
    public ParseResult<SurfWeaChnSsdHor> extract(File file) {
        DecodeSS decodeSS = new DecodeSS();
        String path = file.getPath();
        ParseResult<SurfWeaChnSsdHor> decode = (ParseResult<SurfWeaChnSsdHor>) decodeSS.decode(file, new HashSet<String>());

        return decode;
    }

    /**
     * 报文格式化
     * @param result   报文集合
     * @param file      报文文件
     * @param tablesHelper  ots入库实体类  
     * @return  格式化后的实体类
     */
    @Override
    public StatTF transform(ParseResult<SurfWeaChnSsdHor> result, File file, OTSHelper... tablesHelper) {
//        reload(intervalTime);
        //解码数据集合
        List<SurfWeaChnSsdHor> beans = result.getData();
        //报文集合
        List<ReportInfo> reports = result.getReports();
        //数据表
        OTSHelper dataTable = tablesHelper[0];
        //日值表
        OTSHelper dataDayTable = tablesHelper[1];
        //报文表
        OTSHelper repTable = tablesHelper[2];

        StatTF statTF = new StatTF();

        //String D_DATA_ID = "A.0001.0012.S001";
        String D_DATA_ID = "A.0011.0001.S001";
        //数据处理
        for (int i = 0; i < beans.size(); i++) {
            SurfWeaChnSsdHor bean = beans.get(i);
            String V_BBB = bean.getCorrectionIndicator();
            Date observationTime = bean.getObservationTime();
            String format = new SimpleDateFormat("yyyyMMdd").format(observationTime);
            String stationNumberChina = bean.getStationNumberChina().toUpperCase();
            String D_RECORD_ID = format + "000000_" + stationNumberChina;
            int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();

            //日照时数需要插入到日值表中
            Double V14032 = bean.getTotalSunshinDay().getValue();
            Map<String, Object> dataDay = new HashMap<>();
            dataDay.put("D_RECORD_ID", D_RECORD_ID);//记录标识
            //dataDay.put("D_DATA_ID", "A.0001.0011.S001");//资料标识
            dataDay.put("D_DATA_ID", "A.0016.0001.S001");//资料标识
            dataDay.put("D_IYMDHM", new Date());//入库时间
            dataDay.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            dataDay.put("D_UPDATE_TIME", new Date());//更新时间
            dataDay.put("D_DATETIME", observationTime);//观测时间
            dataDay.put("V_BBB", "000");
//            dataDay.put("V04001", observationTime.getYear() + 1990);
//            dataDay.put("V04002", observationTime.getMonth() + 1);
//            dataDay.put("V04003", observationTime.getDate());
            dataDay.put("V04001", format.substring(0,4));
            dataDay.put("V04002", format.substring(4,6));
            dataDay.put("V04003", format.substring(6,8));
            dataDay.put("V01301", stationNumberChina);
            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                dataDay.put("V07001", "null".equals(s.split(",")[3]) ? "999999" : s.split(",")[3]);
                dataDay.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                dataDay.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                dataDay.put("V07001", "999999");
                dataDay.put("V02301", "999999");
                dataDay.put("V_ACODE", "999999");
            }

            if (num >= 48 & num <= 57) {
                dataDay.put("V01300", stationNumberChina);
            } else {
                dataDay.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
            dataDay.put("V14032", V14032);///主要是这个额
            statTF.getHorList().add(dataDay);

            Map<String, Object> data;
            try {
                data = DataMapMaker.make(bean, 0);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.debug("SurfWeaChnSsdHor is a illegal bean.");
                return null;
            }
            data.put("D_RECORD_ID", D_RECORD_ID);//记录标识
            data.put("D_DATA_ID", D_DATA_ID);//资料标识
            data.put("D_IYMDHM", new Date());//入库时间
            data.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            data.put("D_UPDATE_TIME", new Date());//更新时间
            data.put("V01301", stationNumberChina);
            data.put("V04001", format.substring(0,4));
            data.put("V04002", format.substring(4,6));
            data.put("V04003", format.substring(6,8));

            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                data.put("V07001", "null".equals(s.split(",")[3]) ? "999999" : s.split(",")[3]);
                data.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                data.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                data.put("V07001", "999999");
                data.put("V02301", "999999");
                data.put("V_ACODE", "999999");
            }

            if (num >= 48 & num <= 57) {
                data.put("V01300", stationNumberChina);
            } else {
                data.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }

            List<QCElement<Double>> totalSunshineveryHour = bean.getTotalSunshineveryHour();
            data.put("V14032_001", totalSunshineveryHour.get(0).getValue());
            data.put("V14032_002", totalSunshineveryHour.get(1).getValue());
            data.put("V14032_003", totalSunshineveryHour.get(2).getValue());
            data.put("V14032_004", totalSunshineveryHour.get(3).getValue());
            data.put("V14032_005", totalSunshineveryHour.get(4).getValue());
            data.put("V14032_006", totalSunshineveryHour.get(5).getValue());
            data.put("V14032_007", totalSunshineveryHour.get(6).getValue());
            data.put("V14032_008", totalSunshineveryHour.get(7).getValue());
            data.put("V14032_009", totalSunshineveryHour.get(8).getValue());
            data.put("V14032_010", totalSunshineveryHour.get(9).getValue());
            data.put("V14032_011", totalSunshineveryHour.get(10).getValue());
            data.put("V14032_012", totalSunshineveryHour.get(11).getValue());
            data.put("V14032_013", totalSunshineveryHour.get(12).getValue());
            data.put("V14032_014", totalSunshineveryHour.get(13).getValue());
            data.put("V14032_015", totalSunshineveryHour.get(14).getValue());
            data.put("V14032_016", totalSunshineveryHour.get(15).getValue());
            data.put("V14032_017", totalSunshineveryHour.get(16).getValue());
            data.put("V14032_018", totalSunshineveryHour.get(17).getValue());
            data.put("V14032_019", totalSunshineveryHour.get(18).getValue());
            data.put("V14032_020", totalSunshineveryHour.get(19).getValue());
            data.put("V14032_021", totalSunshineveryHour.get(20).getValue());
            data.put("V14032_022", totalSunshineveryHour.get(21).getValue());
            data.put("V14032_023", totalSunshineveryHour.get(22).getValue());
            data.put("V14032_024", totalSunshineveryHour.get(23).getValue());


            data.put("Q14032_001", totalSunshineveryHour.get(0).getQuality().get(0).getCode());
            data.put("Q14032_002", totalSunshineveryHour.get(1).getQuality().get(0).getCode());
            data.put("Q14032_003", totalSunshineveryHour.get(2).getQuality().get(0).getCode());
            data.put("Q14032_004", totalSunshineveryHour.get(3).getQuality().get(0).getCode());
            data.put("Q14032_005", totalSunshineveryHour.get(4).getQuality().get(0).getCode());
            data.put("Q14032_006", totalSunshineveryHour.get(5).getQuality().get(0).getCode());
            data.put("Q14032_007", totalSunshineveryHour.get(6).getQuality().get(0).getCode());
            data.put("Q14032_008", totalSunshineveryHour.get(7).getQuality().get(0).getCode());
            data.put("Q14032_009", totalSunshineveryHour.get(8).getQuality().get(0).getCode());
            data.put("Q14032_010", totalSunshineveryHour.get(9).getQuality().get(0).getCode());
            data.put("Q14032_011", totalSunshineveryHour.get(10).getQuality().get(0).getCode());
            data.put("Q14032_012", totalSunshineveryHour.get(11).getQuality().get(0).getCode());
            data.put("Q14032_013", totalSunshineveryHour.get(12).getQuality().get(0).getCode());
            data.put("Q14032_014", totalSunshineveryHour.get(13).getQuality().get(0).getCode());
            data.put("Q14032_015", totalSunshineveryHour.get(14).getQuality().get(0).getCode());
            data.put("Q14032_016", totalSunshineveryHour.get(15).getQuality().get(0).getCode());
            data.put("Q14032_017", totalSunshineveryHour.get(16).getQuality().get(0).getCode());
            data.put("Q14032_018", totalSunshineveryHour.get(17).getQuality().get(0).getCode());
            data.put("Q14032_019", totalSunshineveryHour.get(18).getQuality().get(0).getCode());
            data.put("Q14032_020", totalSunshineveryHour.get(19).getQuality().get(0).getCode());
            data.put("Q14032_021", totalSunshineveryHour.get(20).getQuality().get(0).getCode());
            data.put("Q14032_022", totalSunshineveryHour.get(21).getQuality().get(0).getCode());
            data.put("Q14032_023", totalSunshineveryHour.get(22).getQuality().get(0).getCode());
            data.put("Q14032_024", totalSunshineveryHour.get(23).getQuality().get(0).getCode());

            statTF.getMinList().add(data);
            statTF.getBuffer().append(stationNumberChina + "  " + format + "  " + "000" + "\n");

        }
        try {
            //statTF.setRepList(transformReports_ats(reports, D_DATA_ID, file));
        	statTF.setRepList(transformReports_ats(reports, "A.0001.0025.S004", file));
        } catch (ParseException e) {
            statTF.getBuffer().append("reports inssrt error!");
        }
        return statTF;
    }

}
