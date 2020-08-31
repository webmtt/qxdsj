package cma.cimiss2.dpc.indb.core.etl.mysqlETL;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataDay;
import cma.cimiss2.dpc.decoder.surf.DecodeDAY;
import cma.cimiss2.dpc.indb.core.bean.StatTF;
import cma.cimiss2.dpc.indb.core.tools.DataMapMaker;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * 日值数据格式化类 mysql缓冲库
 */
@IocBean(fields = {"rdb", "cimiss"},singleton = false)
 public  class  DayValueEtl extends AbstractEtl<SurfaceObservationDataDay> {
    Log log = Logs.get();

    public DayValueEtl() {
    }



    @Override
    public ParseResult<SurfaceObservationDataDay> extract(File file) {
        DecodeDAY decodeDAY = new DecodeDAY();
        ParseResult<SurfaceObservationDataDay> decode;
        if (QC != null && QC.equals("0")) {//1为有QC,0为没有QC
        	decode = (ParseResult<SurfaceObservationDataDay>) decodeDAY.decodeNoQC(file, new HashSet<String>());
        } else {
        	decode = (ParseResult<SurfaceObservationDataDay>) decodeDAY.decode(file, new HashSet<String>());
        }
        return decode;
    }

    /**
     * 报文格式化
     * @param result   报文集合
     * @param file      报文文件
     * @param tables    表名
     * @return  格式化后的实体类
     */
    @Override
    public StatTF transform(ParseResult<SurfaceObservationDataDay> result, File file, Map<String,String> codeMap, String... tables) {
        //返回集合
        Map<String, Object> allMap = new HashMap<>();
        //解码数据集合
        List<SurfaceObservationDataDay> beans = result.getData();
        //报文集合
        List<ReportInfo> reports = result.getReports();

        //数据表
        String dataTable = tables[0];
        //报文表
        String repTable = tables[1];

        StatTF statTF = new StatTF();


        //String D_DATA_ID = "A.0016.0001.S001";
        //String D_SOURCE_ID = "A.0001.0030.R001";
        String cts_code = codeMap.get("cts_code");
        String day_sod_code = codeMap.get("day_sod_code");
        String report_sod_code = codeMap.get("report_sod_code");
        for (int i = 0; i < beans.size(); i++) {
            SurfaceObservationDataDay bean = beans.get(i);
            String V_BBB =bean.getCorrectionIndicator();
            Date observationTime = bean.getObservationTime();
            String format = new SimpleDateFormat("yyyyMMdd").format(observationTime);
            String stationNumberChina = bean.getStationNumberChina().toUpperCase();
            String D_RECORD_ID = format + "000000_" + stationNumberChina;

            int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();
            Map<String, Object> data;
            try {
                data = DataMapMaker.make(bean, 0);
                //没有QC时需要把假的质控字段移除
                if (QC != null && QC.equals("0")) {
                	Iterator<String> iterator = data.keySet().iterator();
            		while (iterator.hasNext()) {
            		    String key = iterator.next();
            		    if (key.contains("Q")) {
            		        iterator.remove();
            		    }
            		}
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                log.debug("SurfaceObservationDataDay is a illegal bean.");
                return null;
            }
            //主键冲入 已经存在当天本站点数据?????地面日值统计用到
//            Map<String, String> map = existMessage(D_RECORD_ID, dataTable, "rdb");

            //更正报判断
            if (!"000".equals(V_BBB)) {
            	String D_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(observationTime);
                corrMessage(D_RECORD_ID, V_BBB, dataTable, "rdb",getEvent(),D_DATETIME);
                String fileName = file.getName();
                String[] fileNameSplit = fileName.split("_");
                String V_TT = fileNameSplit[6].split("-")[0];
                corrMessage(D_RECORD_ID+"_"+getEvent()+"_"+ V_TT, V_BBB, repTable,"cimiss",getEvent(),D_DATETIME);
            }
            data.put(".table", dataTable);
            data.put("D_RECORD_ID", D_RECORD_ID);//记录标识
            data.put("D_DATA_ID", day_sod_code);//资料标识
            data.put("D_IYMDHM", new Date());//入库时间
            data.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            data.put("D_UPDATE_TIME", new Date());//更新时间
            data.put("D_SOURCE_ID", getEvent());//CTS编码
            data.put("V_BBB", V_BBB);//默认值000 同步程序需要用到
//            data.put("V04001", observationTime.getYear() + 1990);
//            data.put("V04002", observationTime.getMonth() + 1);
//            data.put("V04003", observationTime.getDate());
            data.put("V04001", format.substring(0,4));
            data.put("V04002", format.substring(4,6));
            data.put("V04003", format.substring(6,8));
            data.put("V05001",bean.getLatitude());
            data.put("V06001",bean.getLongitude());
//            Object d_datetime = data.get("D_DATETIME");
            data.put("V01301", stationNumberChina);
            //data.put("V13033", bean.getEvaporation().getValue());//日蒸发量(大型)和提蒸发量(小型)入同样的数据
            //data.put("Q13033", bean.getEvaporation().getQuality().get(0).getCode());
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
            data.put("V20304",((String)data.get("V20304")).replaceAll("\t", " "));
//            dataList.add(data);
            statTF.getMinList().add(data);
//            buffer.append(stationNumberChina + "  " + format + "  " + V_BBB + "\n");
            statTF.getBuffer().append(stationNumberChina + "  " + format + "  " + "000" + "\n");
        }
        try {
//            repList = transformReports(reports, D_DATA_ID, file, repTable);
            //statTF.setRepList(transformReports(reports, D_DATA_ID, file, repTable));
        	statTF.setRepList(transformReports(reports, report_sod_code, getEvent(), file,repTable));
        } catch (ParseException e) {
            statTF.getBuffer().append("reports inssrt error!");
        }


        return statTF;
    }

}
