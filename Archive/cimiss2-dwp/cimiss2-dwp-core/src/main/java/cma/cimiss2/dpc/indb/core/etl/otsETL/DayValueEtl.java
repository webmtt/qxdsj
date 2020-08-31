package cma.cimiss2.dpc.indb.core.etl.otsETL;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataDay;
import cma.cimiss2.dpc.decoder.surf.DecodeDAY;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.ots.OTSHelper;
import cma.cimiss2.dpc.indb.core.tools.DataMapMaker;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * 日值数据格式化类 ots缓冲库
 */
 public  class  DayValueEtl extends AbstractEtl<SurfaceObservationDataDay> {
    Log log = Logs.get();

    public DayValueEtl() {
    }

    @Override
    public ParseResult<SurfaceObservationDataDay> extract(File file) {
        DecodeDAY decodeDAY = new DecodeDAY();
        ParseResult<SurfaceObservationDataDay> decode = (ParseResult<SurfaceObservationDataDay>) decodeDAY.decode(file, new HashSet<String>());
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
    public StatTF transform(ParseResult<SurfaceObservationDataDay> result, File file, OTSHelper... tablesHelper) {
//        reload(intervalTime);
        //解码数据集合
        List<SurfaceObservationDataDay> beans = result.getData();
        //报文集合
        List<ReportInfo> reports = result.getReports();

        //小时表
        OTSHelper dataTable = tablesHelper[0];
        //报文表
        OTSHelper repTable = tablesHelper[1];

        StatTF statTF = new StatTF();


        //String D_DATA_ID = "A.0001.0011.S001";
        String D_DATA_ID = "A.0016.0001.S001";
        for (int i = 0; i < beans.size(); i++) {
            SurfaceObservationDataDay bean = beans.get(i);
            String V_BBB ="000";
            Date observationTime = bean.getObservationTime();
            String format = new SimpleDateFormat("yyyyMMdd").format(observationTime);
            String stationNumberChina = bean.getStationNumberChina().toUpperCase();
            String D_RECORD_ID = format + "000000_" + stationNumberChina;

            int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();
            Map<String, Object> data;
            try {
                data = DataMapMaker.make(bean, 0);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.debug("SurfaceObservationDataDay is a illegal bean.");
                return null;
            }
            //主键冲入 已经存在当天本站点数据?????地面日值统计用到

//            data.put(".table", dataTable);
            data.put("D_RECORD_ID", D_RECORD_ID);//记录标识
            data.put("D_DATA_ID", D_DATA_ID);//资料标识
            data.put("D_IYMDHM", new Date());//入库时间
            data.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            data.put("D_UPDATE_TIME", new Date());//更新时间
            data.put("V_BBB", V_BBB);//默认值000 同步程序需要用到
            data.put("V01301", stationNumberChina);
//            data.put("V04001", observationTime.getYear() + 1990);
//            data.put("V04002", observationTime.getMonth() + 1);
//            data.put("V04003", observationTime.getDate());
            data.put("V04001", format.substring(0,4));
            data.put("V04002", format.substring(4,6));
            data.put("V04003", format.substring(6,8));
            data.put("V05001",bean.getLatitude());
            data.put("V06001",bean.getLongitude());
            if (num >= 48 & num <= 57) {
                data.put("V01300", stationNumberChina);
            } else {
                data.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                //System.out.println("=========查到到==========="+sid);
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                data.put("V07001", "null".equals(s.split(",")[3]) ? "999999" : s.split(",")[3]);
                data.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                data.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                data.put("V07001", "999999");
                data.put("V02301", "999999");
                data.put("V_ACODE", "999999");
            }
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
