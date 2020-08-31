package cma.cimiss2.dpc.indb.core.etl.mysqlETL;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.PrecipitationObservationDataReg;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.surf.DecodePRFREG;
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
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 雨量站数据格式化类 mysql缓冲库
 */
@IocBean(fields = {"rdb", "cimiss"},singleton = false)
public class SurfPERFEtl_new extends AbstractEtl<PrecipitationObservationDataReg> implements Serializable{
	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final long serialVersionUID = -6888594115464050141L;
    static Log log = Logs.get();

    //    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public SurfPERFEtl_new() {
    }

    @Override
    public ParseResult<PrecipitationObservationDataReg> extract(File file) {
        DecodePRFREG decodePRFREG = new DecodePRFREG();
        return decodePRFREG.DecodePRF(file);
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
    public StatTF transform(ParseResult<PrecipitationObservationDataReg> result, File file, Map<String,String> codeMap, String... tables) {
//        reload(intervalTime);
        //解码数据集合
        List<PrecipitationObservationDataReg> beans = result.getData();
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
                //dataHour.put(".table", "surf_wea_chn_hor_all_reg_tab");
                dataHour.put(".table", horTable);
                dataHour.put("D_RECORD_ID", D_RECORD_ID);//记录标识
                dataHour.put("D_DATA_ID", hor_sod_code);//资料标识
                dataHour.put("D_IYMDHM", new Date());//入库时间
                dataHour.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
                dataHour.put("D_UPDATE_TIME", new Date());//更新时间
                dataHour.put("D_SOURCE_ID", getEvent());//CTS编码
                dataHour.put("V01301", stationNumberChina);
                if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                    String s = (String) getStationInfo().get(stationNumberChina + "+01");
                    //dataHour.put("V07001", s.split(",")[3]);
                    dataHour.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                    dataHour.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
                } else {
                    //dataHour.put("V07001", "999999");
                    dataHour.put("V02301", "999999");
                    //dataHour.put("V_ACODE", "999999");
                    dataHour.put("V_ACODE",getVacode(file.getName(),stationNumberChina));
                    
                }

                if (num >= 48 & num <= 57) {
                    dataHour.put("V01300", stationNumberChina);
                } else {
                    dataHour.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
                }
                //1小时降水量
                dataHour.put("V13019", bean.getPrecipitation1Hour().getValue());
                dataHour.put("Q13019", bean.getPrecipitation1Hour().getQuality().get(0).getCode());
                dataHour.put("V_BBB", "000");

                dataHour.put("V04001", observationTime.getYear() + 1900);//资料观测年
                dataHour.put("V04002", observationTime.getMonth() + 1);//资料观测月
                dataHour.put("V04003", observationTime.getDate());//资料观测日
                dataHour.put("V04004", observationTime.getHours());//资料观测时
                //不为空的值赋值
                //dataHour.put("V05001", "999999");
                //dataHour.put("V06001", "999999");
                dataHour.put("V07031", "999998");
                dataHour.put("V07032_04", "999998");
                dataHour.put("V07032_01", "999998");
                dataHour.put("V07032_02", "999998");
                dataHour.put("V02001", "0");
                dataHour.put("V08010", "999998");
                dataHour.put("V02183", "999998");
                dataHour.put("V10004", "999998");
                dataHour.put("V10051", "999998");
                dataHour.put("V10061", "999998");
                dataHour.put("V10062", "999998");
                dataHour.put("V10301", "999998");
                dataHour.put("V10301_052", "999998");
                dataHour.put("V10302", "999998");
                dataHour.put("V10302_052", "999998");
                dataHour.put("V12001", "999998");
                dataHour.put("V12011", "999998");
                dataHour.put("V12011_052", "999998");
                dataHour.put("V12012", "999998");
                dataHour.put("V12012_052", "999998");
                dataHour.put("V12405", "999998");
                dataHour.put("V12016", "999998");
                dataHour.put("V12017", "999998");
                dataHour.put("V12003", "999998");
                dataHour.put("V13003", "999998");
                dataHour.put("V13007", "999998");
                dataHour.put("V13007_052", "999998");
                dataHour.put("V13004", "999998");
                //dataHour.put("V13019", "999999");
                dataHour.put("V13020", "999998");
                dataHour.put("V13021", "999998");
                dataHour.put("V13022", "999998");
                dataHour.put("V13023", "999998");
                dataHour.put("V04080_04", "999998");
                dataHour.put("V13011", "999998");
                dataHour.put("V13033", "999998");
                dataHour.put("V11290", "999998");
                dataHour.put("V11291", "999998");
                dataHour.put("V11292", "999998");
                dataHour.put("V11293", "999998");
                dataHour.put("V11296", "999998");
                dataHour.put("V11042", "999998");
                dataHour.put("V11042_052", "999998");
                dataHour.put("V11201", "999998");
                dataHour.put("V11202", "999998");
                dataHour.put("V11211", "999998");
                dataHour.put("V11046", "999998");
                dataHour.put("V11046_052", "999998");
                dataHour.put("V11503_06", "999998");
                dataHour.put("V11504_06", "999998");
                dataHour.put("V11503_12", "999998");
                dataHour.put("V11504_12", "999998");
                dataHour.put("V12120", "999998");
                dataHour.put("V12311", "999998");
                dataHour.put("V12311_052", "999998");
                dataHour.put("V12121", "999998");
                dataHour.put("V12121_052", "999998");
                dataHour.put("V12013", "999998");
                dataHour.put("V12030_005", "999998");
                dataHour.put("V12030_010", "999998");
                dataHour.put("V12030_015", "999998");
                dataHour.put("V12030_020", "999998");
                dataHour.put("V12030_040", "999998");
                dataHour.put("V12030_080", "999998");
                dataHour.put("V12030_160", "999998");
                dataHour.put("V12030_320", "999998");
                dataHour.put("V12314", "999998");
                dataHour.put("V12315", "999998");
                dataHour.put("V12315_052", "999998");
                dataHour.put("V12316", "999998");
                dataHour.put("V12316_052", "999998");
                dataHour.put("V20001_701_01", "999998");
                dataHour.put("V20001_701_10", "999998");
                dataHour.put("V20059", "999998");
                dataHour.put("V20059_052", "999998");
                dataHour.put("V20001", "999998");
                dataHour.put("V20010", "999998");
                dataHour.put("V20051", "999998");
                dataHour.put("V20011", "999998");
                dataHour.put("V20013", "999998");
                dataHour.put("V20350_01", "999998");
                dataHour.put("V20350_02", "999998");
                dataHour.put("V20350_03", "999998");
                dataHour.put("V20350_04", "999998");
                dataHour.put("V20350_05", "999998");
                dataHour.put("V20350_06", "999998");
                dataHour.put("V20350_07", "999998");
                dataHour.put("V20350_08", "999998");
                dataHour.put("V20350_11", "999998");
                dataHour.put("V20350_12", "999998");
                dataHour.put("V20350_13", "999998");
                dataHour.put("V20003", "999998");
                dataHour.put("V04080_05", "999998");
                dataHour.put("V20004", "999998");
                dataHour.put("V20005", "999998");
                dataHour.put("V20062", "999998");
                dataHour.put("V13013", "999998");
                dataHour.put("V13330", "999998");
                dataHour.put("V20330_01", "999998");
                dataHour.put("V20331_01", "999998");
                dataHour.put("V20330_02", "999998");
                dataHour.put("V20331_02", "999998");

                dataHour.put("Q10004", "9");
                dataHour.put("Q10051", "9");
                dataHour.put("Q10061", "9");
                dataHour.put("Q10062", "9");
                dataHour.put("Q10301", "9");
                dataHour.put("Q10301_052", "9");
                dataHour.put("Q10302", "9");
                dataHour.put("Q10302_052", "9");
                dataHour.put("Q12001", "9");
                dataHour.put("Q12011", "9");
                dataHour.put("Q12011_052", "9");
                dataHour.put("Q12012", "9");
                dataHour.put("Q12012_052", "9");
                dataHour.put("Q12405", "9");
                dataHour.put("Q12016", "9");
                dataHour.put("Q12017", "9");
                dataHour.put("Q12003", "9");
                dataHour.put("Q13003", "9");
                dataHour.put("Q13007", "9");
                dataHour.put("Q13007_052", "9");
                dataHour.put("Q13004", "9");
                dataHour.put("Q13020", "9");
                dataHour.put("Q13021", "9");
                dataHour.put("Q13022", "9");
                dataHour.put("Q13023", "9");
                dataHour.put("Q04080_04", "9");
                dataHour.put("Q13011", "9");
                dataHour.put("Q13033", "9");
                dataHour.put("Q11290", "9");
                dataHour.put("Q11291", "9");
                dataHour.put("Q11292", "9");
                dataHour.put("Q11293", "9");
                dataHour.put("Q11296", "9");
                dataHour.put("Q11042", "9");
                dataHour.put("Q11042_052", "9");
                dataHour.put("Q11201", "9");
                dataHour.put("Q11202", "9");
                dataHour.put("Q11211", "9");
                dataHour.put("Q11046", "9");
                dataHour.put("Q11046_052", "9");
                dataHour.put("Q11503_06", "9");
                dataHour.put("Q11504_06", "9");
                dataHour.put("Q11503_12", "9");
                dataHour.put("Q11504_12", "9");
                dataHour.put("Q12311", "9");
                dataHour.put("Q12311_052", "9");
                dataHour.put("Q12121", "9");
                dataHour.put("Q12121_052", "9");
                dataHour.put("Q12013", "9");
                dataHour.put("Q12030_005", "9");
                dataHour.put("Q12030_010", "9");
                dataHour.put("Q12030_015", "9");
                dataHour.put("Q12030_020", "9");
                dataHour.put("Q12030_040", "9");
                dataHour.put("Q12030_080", "9");
                dataHour.put("Q12030_160", "9");
                dataHour.put("Q12030_320", "9");
                dataHour.put("Q12314", "9");
                dataHour.put("Q12315", "9");
                dataHour.put("Q12315_052", "9");
                dataHour.put("Q12316", "9");
                dataHour.put("Q12316_052", "9");
                dataHour.put("Q20001_701_01", "9");
                dataHour.put("Q20001_701_10", "9");
                dataHour.put("Q20059", "9");
                dataHour.put("Q20059_052", "9");
                dataHour.put("Q20001", "9");
                dataHour.put("Q20010", "9");
                dataHour.put("Q20051", "9");
                dataHour.put("Q20011", "9");
                dataHour.put("Q20013", "9");
                dataHour.put("Q20350_01", "9");
                dataHour.put("Q20350_02", "9");
                dataHour.put("Q20350_03", "9");
                dataHour.put("Q20350_04", "9");
                dataHour.put("Q20350_05", "9");
                dataHour.put("Q20350_06", "9");
                dataHour.put("Q20350_07", "9");
                dataHour.put("Q20350_08", "9");
                dataHour.put("Q20350_11", "9");
                dataHour.put("Q20350_12", "9");
                dataHour.put("Q20350_13", "9");
                dataHour.put("Q20003", "9");
                dataHour.put("Q04080_05", "9");
                dataHour.put("Q20004", "9");
                dataHour.put("Q20005", "9");
                dataHour.put("Q20062", "9");
                dataHour.put("Q13013", "9");
                dataHour.put("Q13330", "9");
                dataHour.put("Q20330_01", "9");
                dataHour.put("Q20331_01", "9");
                dataHour.put("Q20330_02", "9");
                dataHour.put("Q20331_02", "9");
                dataHour.put("Q12120", "9");
                statTF.getHorList().add(dataHour);

            }
            Map<String, Object> dataMinute = new HashMap<String, Object>();
            dataMinute.put(".table", minTable);
            dataMinute.put("D_RECORD_ID", D_RECORD_ID);//记录标识
            dataMinute.put("D_DATA_ID", mul_sod_code);//资料标识
            dataMinute.put("D_IYMDHM", new Date());//入库时间
            dataMinute.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            dataMinute.put("D_UPDATE_TIME", new Date());//更新时间
            dataMinute.put("D_SOURCE_ID", getEvent());//CTS编码
            dataMinute.put("D_DATETIME", bean.getObservationTime());
            dataMinute.put("V01301", stationNumberChina);
            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                //dataMinute.put("V07001", s.split(",")[3]);
                dataMinute.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                dataMinute.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                //dataMinute.put("V07001", "999999");
                dataMinute.put("V02301", "999999");
                //dataMinute.put("V_ACODE", "999999");
                dataMinute.put("V_ACODE",getVacode(file.getName(),stationNumberChina));
            }
            dataMinute.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());
            dataMinute.put("V01301", stationNumberChina);
            if (num >= 48 & num <= 57) {
                dataMinute.put("V01300", stationNumberChina);
            } else {
                dataMinute.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
            int size = bean.getPrecipitationEveryMinutes().size();
            Double V13011 = 0.0;
            Integer Q13011 = 0;
            //分钟降水量
            if(observationTime.getMinutes() == 00){
            	//0点时入最后两位
            	V13011 = bean.getPrecipitationEveryMinutes().get(size - 1).getValue();
            	Q13011 = bean.getPrecipitationEveryMinutes().get(size - 1).getQuality().get(0).getCode();
            }else{
            	V13011 = bean.getPrecipitationEveryMinutes().get(observationTime.getMinutes() - 1).getValue();
            	Q13011 = bean.getPrecipitationEveryMinutes().get(observationTime.getMinutes() - 1).getQuality().get(0).getCode();
            }
            //封装实体类时已经做过除10处理了
            dataMinute.put("V13011", V13011);
            dataMinute.put("Q13011",Q13011);
        	
            dataMinute.put("V_BBB", "000");
            dataMinute.put("V04001", observationTime.getYear() + 1900);//资料观测年
            dataMinute.put("V04002", observationTime.getMonth() + 1);//资料观测月
            dataMinute.put("V04003", observationTime.getDate());//资料观测日
            dataMinute.put("V04004", observationTime.getHours());//资料观测时
            dataMinute.put("V04005", observationTime.getMinutes());//资料观测分 小时不入
            dataMinute.put("V05001", bean.getLatitude());//纬度
            dataMinute.put("V06001", bean.getLongitude());//经度
            dataMinute.put("V02001", "0");
            dataMinute.put("V07031", "999999");
            dataMinute.put("V11201", "999999");
            dataMinute.put("V11202", "999999");
            dataMinute.put("V11290", "999999");
            dataMinute.put("V11291", "999999");
            dataMinute.put("V11292", "999999");
            dataMinute.put("V11293", "999999");
            dataMinute.put("V11296", "999999");
            dataMinute.put("V12003", "999999");
            dataMinute.put("V12030_080", "999999");
            dataMinute.put("V12030_160", "999999");
            dataMinute.put("V12030_320", "999999");
            dataMinute.put("V13004", "999999");
            dataMinute.put("V13033", "999999");
            dataMinute.put("V20001_701_01", "999999");
            dataMinute.put("Q11201", "9");
            dataMinute.put("Q20001_701_10", "9");
            dataMinute.put("Q11202", "9");
            dataMinute.put("Q13004", "9");
            dataMinute.put("Q12030_320", "9");
            dataMinute.put("Q12030_080", "9");
            dataMinute.put("Q12030_160", "9");
            dataMinute.put("Q12003", "9");
            dataMinute.put("Q11292", "9");
            dataMinute.put("Q11293", "9");
            dataMinute.put("Q11290", "9");
            dataMinute.put("Q11291", "9");
            dataMinute.put("Q11296", "9");
            statTF.getMinList().add(dataMinute);

            //入分钟降水表,是5的整倍数
            if(observationTime.getMinutes() != 00 && observationTime.getMinutes()%5 == 00){
                for(int j = 0;j < 5; j++) {
                	Map<String, Object> dataMinPre = new HashMap<String, Object>();
                	dataMinPre.put(".table", minPreTable);
                    dataMinPre.put("D_DATA_ID", pre_sod_code);//资料标识
                    dataMinPre.put("D_IYMDHM", new Date());//入库时间
                    dataMinPre.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
                    dataMinPre.put("D_UPDATE_TIME", new Date());//更新时间
                    dataMinPre.put("D_SOURCE_ID",getEvent());//CTS编码
                    
                    Date date = new Date(observationTime.getTime());
                    // 2020-3-31 chy 
//                    date.setMinutes(observationTime.getMinutes() - j - 1);
                    date.setMinutes(observationTime.getMinutes() - j); // 5 4 3 2 1
                    dataMinPre.put("D_DATETIME", date);//资料时间
                    String formatMinPre = new SimpleDateFormat("yyyyMMddHHmmss").format(date);
                    dataMinPre.put("D_RECORD_ID", formatMinPre + "_" + stationNumberChina);//记录标识
                    
                    dataMinPre.put("V_BBB", "000");
                    dataMinPre.put("V01301", stationNumberChina);
                    dataMinPre.put("V05001", bean.getLatitude());//纬度
                    dataMinPre.put("V06001", bean.getLongitude());//经度
                    dataMinPre.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
                    dataMinPre.put("V08010", "999999");//地面限定符 (温度数据)
                    dataMinPre.put("V02001", "0");//测站类型
                    dataMinPre.put("V02301", dataMinute.get("V02301"));//台站级别
                    dataMinPre.put("V_ACODE", dataMinute.get("V_ACODE"));//中国行政区划代码
                    dataMinPre.put("V04001", (date.getYear() + 1900));//资料观测年
                    dataMinPre.put("V04002", (date.getMonth() + 1));//资料观测月
                    dataMinPre.put("V04003", date.getDate());//资料观测日
                    dataMinPre.put("V04004", date.getHours());//资料观测时
                    dataMinPre.put("V04005", date.getMinutes());//资料观测分 小时不入
                    
                    dataMinPre.put("V13011", bean.getPrecipitationEveryMinutes().get(date.getMinutes() - 1).getValue());
                    dataMinPre.put("Q13011", bean.getPrecipitationEveryMinutes().get(date.getMinutes() - 1).getQuality().get(0).getCode());
                    statTF.getMinPreList().add(dataMinPre);
                }
             }
            // 入过去1小时的逐分钟降水
            if(observationTime.getMinutes() == 00){
            	 Calendar calendar = Calendar.getInstance();
                 calendar.setTime(observationTime);
                 calendar.add(Calendar.HOUR_OF_DAY, -1);
                 Date date = new Date(calendar.getTime().getTime());
            	 List<Integer> minutes = selectInsert(report_sod_code, observationTime, bean.getStationNumberChina(),
            			cimiss);
            	 minutes.add(60);
            	 for(int m = 0; m < minutes.size(); m ++){  // 5, 15,..., 55
					for(int t = minutes.get(m) - 4; t <= minutes.get(m); t ++ ){
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
	                    
	                    dataMinPre.put("V_BBB", "000");
	                    dataMinPre.put("V01301", stationNumberChina);
	                    dataMinPre.put("V05001", bean.getLatitude());//纬度
	                    dataMinPre.put("V06001", bean.getLongitude());//经度
	                    dataMinPre.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
	                    dataMinPre.put("V08010", "999999");//地面限定符 (温度数据)
	                    dataMinPre.put("V02001", "0");//测站类型
	                    dataMinPre.put("V02301", dataMinute.get("V02301"));//台站级别
	                    dataMinPre.put("V_ACODE", dataMinute.get("V_ACODE"));//中国行政区划代码
	                    dataMinPre.put("V04001", (date.getYear() + 1900));//资料观测年
	                    dataMinPre.put("V04002", (date.getMonth() + 1));//资料观测月
	                    dataMinPre.put("V04003", date.getDate());//资料观测日
	                    dataMinPre.put("V04004", date.getHours());//资料观测时
	                    dataMinPre.put("V04005", date.getMinutes());//资料观测分 小时不入
	                    
	                    // 2020-3-31 chy date.getMinutes() 改为 date.getMinutes() - 1
                    	dataMinPre.put("V13011", bean.getPrecipitationEveryMinutes().get(t - 1).getValue());
                    	dataMinPre.put("Q13011", bean.getPrecipitationEveryMinutes().get(t - 1).getQuality().get(0).getCode());
                    	statTF.getMinPreList().add(dataMinPre);
					}
                }
             }
            
             //入5分钟和10分钟累计降水表
            if(observationTime.getMinutes()%5 == 00) {
            	Map<String, Object> dataCumPre = new HashMap<>();
                dataCumPre.put(".table", cumPreTable);
                dataCumPre.put("D_RECORD_ID", D_RECORD_ID);//记录标识
                dataCumPre.put("D_DATA_ID", accu_sod_code);//资料标识
                dataCumPre.put("D_IYMDHM", new Date());//入库时间
                dataCumPre.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
                dataCumPre.put("D_UPDATE_TIME", new Date());//更新时间
                dataCumPre.put("D_SOURCE_ID",getEvent());//CTS编码
                dataCumPre.put("D_DATETIME", observationTime);
                dataCumPre.put("V_BBB", "000");
                dataCumPre.put("V01301", stationNumberChina);
                dataCumPre.put("V05001", bean.getLatitude());//纬度
                dataCumPre.put("V06001", bean.getLongitude());//经度
                dataCumPre.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
                dataCumPre.put("V02001", "0");//测站类型
                dataCumPre.put("V02301", dataMinute.get("V02301"));//台站级别
                dataCumPre.put("V_ACODE", dataMinute.get("V_ACODE"));//中国行政区划代码
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
	public List<Object> getCumPre5And10(PrecipitationObservationDataReg bean,Date observationTime){
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
		Calendar cal = Calendar.getInstance();
		cal.setTime(datetime);
		cal.add(Calendar.HOUR_OF_DAY, -1); // 例如 2:00->1:00
		
		for(int i = 0; i < 11; i ++){
			cal.add(Calendar.MINUTE, 5);// 1:00->1:05->1:10  ... 55(第11次)
			Date dt = new Date(cal.getTime().getTime());
			String select = "select count(*) from SURF_WEA_CHN_REP_TAB where " + 
				"d_datetime=" + "'" + sdf2.format(dt) + "'" +
				" and V01301=" + "'" + V01301 + "'" + 
				" and d_data_id='" + report_sod_code + "' limit 1";
			Sql repSelectSql = Sqls.create(select);
			repSelectSql.setCallback(new SqlCallback() {
                @Override
                public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
                    List<String> list = new LinkedList<String>();
                    while (rs.next())
                        list.add(rs.getString(1));// 数量
                    return list;
                }
            });
			cimiss.execute(repSelectSql);
			System.out.println(select);
			List<String> miniteList = repSelectSql.getList(String.class);
			if(!miniteList.isEmpty()){
				if(miniteList.get(0).equals("0"))
					minutes.add(dt.getMinutes()); //库中没有，需要入库
				}
			}
		return minutes;
	}// end method
    // end 2020-4-3 chy
}
