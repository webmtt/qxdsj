package cma.cimiss2.dpc.indb.core.etl.mysqlETL;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.decoder.surf.DecodeBABJ;
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
 * 国家站数据格式化类 mysql缓冲库
 */
@IocBean(fields = {"rdb", "cimiss"},singleton = false)
public class SurfBABJEtl_new extends AbstractEtl<SurfaceObservationDataNation> {
	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final long serialVersionUID = 6440083997874287961L;
    static Log log = Logs.get();

    public SurfBABJEtl_new() {
    }


    @Override
    public ParseResult<SurfaceObservationDataNation> extract(File file) {
        DecodeBABJ decodeBABJ = new DecodeBABJ();
        ParseResult<SurfaceObservationDataNation> decode = decodeBABJ.decode(file, new HashSet<>());
        return decode;
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
    public StatTF transform(ParseResult<SurfaceObservationDataNation> result, File file, Map<String,String> codeMap, String... tables) {
//        reload(intervalTime);
        //返回集合
        Map<String, Object> allMap = new HashMap<>();
        //解码数据集合
        List<SurfaceObservationDataNation> beans = result.getData();
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
        //全球表 2019-11-4 chy
        String glbHorTable = tables[5];
     // 重要天气表 2020-3-3 chy
        String weatherTable = tables[6];
        
        StatTF statTF = new StatTF();

        String cts_code = codeMap.get("cts_code");
        String hor_sod_code = codeMap.get("hor_sod_code");
        String mul_sod_code = codeMap.get("mul_sod_code");
        String pre_sod_code = codeMap.get("pre_sod_code");
        String accu_sod_code = codeMap.get("accu_sod_code");
        String report_sod_code = codeMap.get("report_sod_code");

        // 全球表 2019-11-4 chy
        String glb_hor_sod_code = codeMap.get("glb_hor_sod_code");
        // 重要天气表 2020-3-3 chy
        String weather_sod_code = codeMap.get("weather_sod_code");
        
        for (int i = 0; i < beans.size(); i++) {
            SurfaceObservationDataNation bean = beans.get(i);
            String V_BBB = bean.getCorrectionIndicator();
            Date observationTime = bean.getObservationTime();
            String format = new SimpleDateFormat("yyyyMMddHHmmss").format(observationTime);
            String stationNumberChina = bean.getStationNumberChina().toUpperCase();
            String D_RECORD_ID = format + "_" + stationNumberChina;

            int num = stationNumberChina.substring(0, 1).toUpperCase().hashCode();
            //更正报判断
            if (!"000".equals(V_BBB)) {
            	String D_DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(observationTime);
                corrMessage(D_RECORD_ID, V_BBB, horTable, "rdb",getEvent(),D_DATETIME);
                corrMessage(D_RECORD_ID, V_BBB, minTable, "rdb",getEvent(),D_DATETIME);
                // 2019-11-5 chy
                corrMessage(D_RECORD_ID, V_BBB, glbHorTable, "rdb", getEvent(), D_DATETIME);
                // 2020-3-4 chy
                corrMessage(D_RECORD_ID, V_BBB, weatherTable, "rdb", getEvent(), D_DATETIME);
                
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
            
	        if (observationTime.getMinutes() == 00 && observationTime.getHours() % 3 == 0) {
	            // 全球表 2019-11-4 chy
	        	Map<String, Object> dataGLB;
	        	try{
	        		dataGLB = DataMapMaker.make(bean, 1);
	        	}
	        	catch (IllegalArgumentException | IllegalAccessException e) {
	                log.debug("SurfaceObservationDataNation is a illegal bean.");
	                return null;
	            }
	        	if(cts_code.equalsIgnoreCase(getEvent())){
	        		dataGLB.put("D_data_dpcid", "A.0013.0001.P015");
	        	}
	        	else{ //"A.0001.0040.R001"
	        		dataGLB.put("D_data_dpcid", "A.0013.0001.P017");
	        	}
	        	dataGLB.remove("V20051");
	        	dataGLB.remove("Q20051");
	        	dataGLB.remove("V13011");
	        	dataGLB.remove("Q13011");
	        	
	        	dataGLB.remove("V12030_005");
	        	dataGLB.remove("V12030_010");
	        	dataGLB.remove("V12030_015");
	        	dataGLB.remove("V12030_020");
	        	dataGLB.remove("V12030_040");
	        	dataGLB.remove("V12030_080");
	        	dataGLB.remove("V12030_160");
	        	dataGLB.remove("V12030_320");
	        	dataGLB.remove("Q12030_005");
	        	dataGLB.remove("Q12030_010");
	        	dataGLB.remove("Q12030_015");
	        	dataGLB.remove("Q12030_020");
	        	dataGLB.remove("Q12030_040");
	        	dataGLB.remove("Q12030_080");
	        	dataGLB.remove("Q12030_160");
	        	dataGLB.remove("Q12030_320");
	        	
	        	dataGLB.remove("V10301");
	        	dataGLB.remove("Q10301");
	        	dataGLB.remove("V10301_052");
	        	dataGLB.remove("Q10301_052");
	        	dataGLB.remove("V10302");
	        	dataGLB.remove("Q10302");
	        	dataGLB.remove("V10302_052");
	        	dataGLB.remove("Q10302_052");
	        	dataGLB.remove("V12011");
	        	dataGLB.remove("Q12011");
	        	dataGLB.remove("V12011_052");
	        	dataGLB.remove("Q12011_052");
	        	dataGLB.remove("V12011_052");
	        	dataGLB.remove("Q12011_052");
	        	dataGLB.remove("V12012");
	        	dataGLB.remove("Q12012");
	        	dataGLB.remove("V12012_052");
	        	dataGLB.remove("Q12012_052");
	        	dataGLB.remove("V13007");
	        	dataGLB.remove("Q13007");
	        	dataGLB.remove("V13007_052");
	        	dataGLB.remove("Q13007_052");
	        	dataGLB.remove("V13004");
	        	dataGLB.remove("Q13004");
	        	dataGLB.remove("V04080_04");
	        	dataGLB.remove("Q04080_04");
	        	dataGLB.remove("V13033");
	        	dataGLB.remove("Q13033");
	        	dataGLB.remove("V11290");
	        	dataGLB.remove("Q11290");
	        	dataGLB.remove("V11291");
	        	dataGLB.remove("Q11291");
	        	dataGLB.remove("V11292");
	        	dataGLB.remove("Q11292");
	        	dataGLB.remove("V11293");
	        	dataGLB.remove("Q11293");
	        	dataGLB.remove("V11296");
	        	dataGLB.remove("Q11296");
	        	
	        	dataGLB.remove("V11042");
	            dataGLB.remove("V11042_052");
	            dataGLB.remove("V11201");
	            dataGLB.remove("V11202");
	            dataGLB.remove("V11211");
	            dataGLB.remove("V11046");
	            dataGLB.remove("V11046_052");
	            dataGLB.remove("V11504_06");
	            dataGLB.remove("V11503_06");
	            dataGLB.remove("V11504_12");
	            dataGLB.remove("V11503_12");
	            dataGLB.remove("V12120");
	            dataGLB.remove("V12311");
	            dataGLB.remove("V12311_052");
	            dataGLB.remove("V12121");
	            dataGLB.remove("V12121_052");
	            dataGLB.remove("V12314");
	            dataGLB.remove("V12315");
	            dataGLB.remove("V12315_052");
	            dataGLB.remove("V12316");
	            dataGLB.remove("V12316_052");
	            dataGLB.remove("V20001_701_01");
	            dataGLB.remove("V20001_701_10");
	            
	            dataGLB.remove("Q11042");
	            dataGLB.remove("Q11042_052");
	            dataGLB.remove("Q11201");
	            dataGLB.remove("Q11202");
	            dataGLB.remove("Q11211");
	            dataGLB.remove("Q11046");
	            dataGLB.remove("Q11046_052");
	            dataGLB.remove("Q11504_06");
	            dataGLB.remove("Q11503_06");
	            dataGLB.remove("Q11504_12");
	            dataGLB.remove("Q11503_12");
	            dataGLB.remove("Q12120");
	            dataGLB.remove("Q12311");
	            dataGLB.remove("Q12311_052");
	            dataGLB.remove("Q12121");
	            dataGLB.remove("Q12121_052");
	            dataGLB.remove("Q12314");
	            dataGLB.remove("Q12315");
	            dataGLB.remove("Q12315_052");
	            dataGLB.remove("Q12316");
	            dataGLB.remove("Q12316_052");
	            dataGLB.remove("Q20001_701_01");
	            dataGLB.remove("Q20001_701_10");
	            
	            dataGLB.remove("V20059");
	            dataGLB.remove("Q20059");
	            dataGLB.remove("V20059_052");
	        	dataGLB.remove("Q20059_052");
	        	
	        	dataGLB.remove("V04080_05");
	        	dataGLB.remove("V13330");
	        	dataGLB.remove("V20330_01");
	        	dataGLB.remove("V20331_01");
	        	dataGLB.remove("V20330_02");
	        	dataGLB.remove("V20331_02");
	        	
	        	dataGLB.remove("Q04080_05");
	        	dataGLB.remove("Q13330");
	        	dataGLB.remove("Q20330_01");
	        	dataGLB.remove("Q20331_01");
	        	dataGLB.remove("Q20330_02");
	        	dataGLB.remove("Q20331_02");
	        	
	        	dataGLB.put(".table", glbHorTable);
	        	dataGLB.put("D_RECORD_ID", D_RECORD_ID);//记录标识
	        	dataGLB.put("D_DATA_ID", glb_hor_sod_code);//资料标识
	        	dataGLB.put("D_IYMDHM", new Date());//入库时间
	        	dataGLB.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
	        	dataGLB.put("D_SOURCE_ID",getEvent());//CTS编码
	        	dataGLB.put("V01301", stationNumberChina);
	        	 if (num >= 48 & num <= 57) {
	        		 dataGLB.put("V01300", stationNumberChina);
	             } else {
	            	 dataGLB.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
	             }
	        	 if (getStationInfo().containsKey(stationNumberChina + "+01")) {
	                 //System.out.println("=========查到到==========="+sid);
	                 String s = (String) getStationInfo().get(stationNumberChina + "+01");
	                 dataGLB.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
	                 dataGLB.put("V_NCODE", "null".equals(s.split(",")[4]) ? "999999" : s.split(",")[4]); // 国家代码
	             } else {
	                 //System.out.println("=========未查到到==========="+sid);
	            	 dataGLB.put("V02301", "999999");
	            	 dataGLB.put("V_NCODE", "999999");
	             }
	        	 
	        	 if(Double.parseDouble(dataGLB.get("V07031").toString()) == 99999.9){
	        		 dataGLB.put("V07031", "999999");
	        	 }
	        	 dataGLB.put("V11001", bean.getWindDirectionAt2m().getValue());
	        	 dataGLB.put("V11002", bean.getWindSpeedAvg2m().getValue());
	        	 dataGLB.put("Q11001", bean.getWindDirectionAt2m().getQuality().get(1).getCode());
	        	 dataGLB.put("Q11002", bean.getWindSpeedAvg2m().getQuality().get(1).getCode());
	        	 
	        	 dataGLB.put("V04001", (observationTime.getYear() + 1900));//资料观测年
	        	 dataGLB.put("V04002", (observationTime.getMonth() + 1));//资料观测月
	        	 dataGLB.put("V04003", observationTime.getDate());//资料观测日
	        	 dataGLB.put("V04004", observationTime.getHours());//资料观测时
	        	 dataGLB.put("V04005", observationTime.getMinutes());
	        	 dataGLB.put("V04001_02", (observationTime.getYear() + 1900));//资料观测年
	        	 dataGLB.put("V04002_02", (observationTime.getMonth() + 1));//资料观测月
	        	 dataGLB.put("V04003_02", observationTime.getDate());//资料观测日
	        	 dataGLB.put("V04004_02", observationTime.getHours());//资料观测时
	        	 dataGLB.put("V07032_04", "999998");//风速传感器距地面高度
	        	 dataGLB.put("V07032_03", "999998");//降水传感器距地面高度
	        	 dataGLB.put("V07032_01", "999998");//温度传感器距地面高度
	        	 dataGLB.put("V07032_02", "999998");//能见度传感器距地面高度
	        	 dataGLB.put("V02002", "999998");// 测风仪类型
	        	 dataGLB.put("V02004", "999998");// 测量蒸发的仪器类型或作物类型
	        	 dataGLB.put("V02001", "0");//测站类型
	        	 dataGLB.put("V01015", "999998");//台站名称
	        	
	        	 dataGLB.put("V07004", "999998"); //气压（测站最接近的标准层）
	        	 dataGLB.put("V10009", "999998");  //位势高度（标准层）
	        	 dataGLB.put("V10063", "999998"); // 气压倾向的特征
	        	 dataGLB.put("V13011_02", "999998"); //过去2小时降水量
	        	 dataGLB.put("V13011_09", "999998"); //过去9小时降水量
	        	 dataGLB.put("V13011_15", "999998"); //过去15小时降水量
	        	 dataGLB.put("V13011_18", "999998"); //过去18小时降水量
	        	 //云状编码
	             String cloudType11 = bean.getCloudType1().getValue();
	             dataGLB.put("V20350_11", getCloudType1Detail(cloudType11, 0));//低云状
	             dataGLB.put("V20350_12", getCloudType1Detail(cloudType11, 1));//中云状
	             dataGLB.put("V20350_13", getCloudType1Detail(cloudType11, 2));//高云状
	             dataGLB.put("V12014", "999998"); //过去12小时最高气温
	             dataGLB.put("V12015", "999998");//过去12小时最低气温
	        	 dataGLB.put("V13340", "999998");  //日蒸发量
	        	 dataGLB.put("V14032_01", "999998");
	        	 dataGLB.put("V14032_24", "999998");
	        	 dataGLB.put("V14016_01", "999998");
	        	 dataGLB.put("V14015", "999998");
	        	 dataGLB.put("V14021_01", "999998");
	        	 dataGLB.put("V14020", "999998");
	        	 dataGLB.put("V14023_01", "999998");
	        	 dataGLB.put("V14022", "999998");
	        	 dataGLB.put("V14002_01", "999998");
	        	 dataGLB.put("V14001", "999998");
	        	 dataGLB.put("V14004_01", "999998");
	        	 dataGLB.put("V14003", "999998");
	        	 dataGLB.put("V14025_01", "999998");
	        	 dataGLB.put("V14024", "999998");
	        	 dataGLB.put("V20054_01", "999998");
	        	 dataGLB.put("V20054_02", "999998");
	        	 dataGLB.put("V20054_03", "999998");
	        	 dataGLB.put("V20012", "999998"); //地形云或垂直发展云的云属
	        	 dataGLB.put("V05021", "999998");//地形云或垂直发展云的方位
	        	 dataGLB.put("V07021", "999998"); //地形云或垂直发展云的高度角
	        	 dataGLB.put("V20012_01", "999998"); //云属1	V20012_01
	        	 dataGLB.put("V20011_01", "999998");//云属1的云量	V20011_01
	        	 dataGLB.put("V20013_01", "999998");// 云属1的云高	V20013_01
	        	 dataGLB.put("V20012_02", "999998");// 云属2	V20012_02
	        	 dataGLB.put("V20011_02", "999998");//云属2的云量	V20011_02
	        	 dataGLB.put("V20013_02", "999998");//云属2的云高	V20013_02
	        	 dataGLB.put("V20012_03", "999998");// 云属3	V20012_03
	        	 dataGLB.put("V20011_03", "999998");// 云属3的云量	V20011_03
	        	 dataGLB.put("V20013_03", "999998");//云属3的云高	V20013_03
	        	 dataGLB.put("V20012_04", "999998");//积雨云 云属
	        	 dataGLB.put("V20011_04", "999998"); //积雨云云量
	        	 dataGLB.put("V20013_04", "999998"); //积雨云云高
	        	 dataGLB.put("V20063_01", "999998");//        	 特殊天气现象1	V20063_01
	        	 dataGLB.put("V20063_02", "999998");//        	 特殊天气现象2	V20063_02
	        	 dataGLB.put("V20063_03", "999998");//        	 特殊天气现象3	V20063_03
	        	 
	        	 dataGLB.put("Q07004", "9");
	        	 dataGLB.put("Q10009", "9");
	        	 dataGLB.put("Q10063", "9");
	        	 dataGLB.put("Q13011_02", "9"); //过去2小时降水量
	        	 dataGLB.put("Q13011_09", "9"); //过去9小时降水量
	        	 dataGLB.put("Q13011_15", "9"); //过去15小时降水量
	        	 dataGLB.put("Q13011_18", "9"); //过去18小时降水量
	        	//云状编码质量标志 二级质控
	             int cloudType1Quality11 = bean.getCloudType1().getQuality().get(1).getCode();
	             dataGLB.put("Q20350_11", cloudType1Quality11);//
	             dataGLB.put("Q20350_12", cloudType1Quality11);//
	             dataGLB.put("Q20350_13", cloudType1Quality11);//
	             dataGLB.put("Q12014", "9"); //过去12小时最高气温
	             dataGLB.put("Q12015", "9");//过去12小时最低气温
	        	 dataGLB.put("Q13340", "9");  //日蒸发量
	        	 dataGLB.put("Q14032_01", "9");
	        	 dataGLB.put("Q14032_24", "9");
	        	 dataGLB.put("Q14016_01", "9");
	        	 dataGLB.put("Q14015", "9");
	        	 dataGLB.put("Q14021_01", "9");
	        	 dataGLB.put("Q14020", "9");
	        	 dataGLB.put("Q14023_01", "9");
	        	 dataGLB.put("Q14022", "9");
	        	 dataGLB.put("Q14002_01", "9");
	        	 dataGLB.put("Q14001", "9");
	        	 dataGLB.put("Q14004_01", "9");
	        	 dataGLB.put("Q14003", "9");
	        	 dataGLB.put("Q14025_01", "9");
	        	 dataGLB.put("Q14024", "9");
	        	 dataGLB.put("Q20054_01", "9");
	        	 dataGLB.put("Q20054_02", "9");
	        	 dataGLB.put("Q20054_03", "9");
	        	 dataGLB.put("Q20012", "9"); //地形云或垂直发展云的云属
	        	 dataGLB.put("Q05021", "9");//地形云或垂直发展云的方位
	        	 dataGLB.put("Q07021", "9"); //地形云或垂直发展云的高度角
	        	 dataGLB.put("Q20012_01", "9"); //云属1	V20012_01
	        	 dataGLB.put("Q20011_01", "9");//云属1的云量	V20011_01
	        	 dataGLB.put("Q20013_01", "9");// 云属1的云高	V20013_01
	        	 dataGLB.put("Q20012_02", "9");// 云属2	V20012_02
	        	 dataGLB.put("Q20011_02", "9");//云属2的云量	V20011_02
	        	 dataGLB.put("Q20013_02", "9");//云属2的云高	V20013_02
	        	 dataGLB.put("Q20012_03", "9");// 云属3	V20012_03
	        	 dataGLB.put("Q20011_03", "9");// 云属3的云量	V20011_03
	        	 dataGLB.put("Q20013_03", "9");//云属3的云高	V20013_03
	        	 dataGLB.put("Q20012_04", "9");//积雨云 云属
	        	 dataGLB.put("Q20011_04", "9"); //积雨云云量
	        	 dataGLB.put("Q20013_04", "9"); //积雨云云高
	        	 dataGLB.put("Q20063_01", "9");//        	 特殊天气现象1	V20063_01
	        	 dataGLB.put("Q20063_02", "9");//        	 特殊天气现象2	V20063_02
	        	 dataGLB.put("Q20063_03", "9");//        	 特殊天气现象3	V20063_03
	        	 
	        	 dataGLB.put("V_TT", "AWS");
	        	 dataGLB.put("C_CCCC", "BABJ");
	        	 statTF.getGlbHorList().add(dataGLB);
	        	//end: 全球表 2019-11-4 chy
	        }
        	 
            // 如果是 00 分的数据，入小时表
            if (observationTime.getMinutes() == 00) {
                Map<String, Object> data;
                try {
                    data = DataMapMaker.make(bean, 1);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    log.debug("SurfaceObservationDataNation is a illegal bean.");
                    return null;
                }
                data.put(".table", horTable);
                data.put("D_RECORD_ID", D_RECORD_ID);//记录标识
                data.put("D_DATA_ID", hor_sod_code);//资料标识
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

                if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                    //System.out.println("=========查到到==========="+sid);
                    String s = (String) getStationInfo().get(stationNumberChina + "+01");
                    data.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                    data.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
                } else {
                    //System.out.println("=========未查到到==========="+sid);
                    data.put("V02301", "999999");
                    //data.put("V_ACODE", "999999");
                    data.put("V_ACODE",getVacode(file.getName(),stationNumberChina));
                }

                data.put("V04001", (observationTime.getYear() + 1900));//资料观测年
                data.put("V04002", (observationTime.getMonth() + 1));//资料观测月
                data.put("V04003", observationTime.getDate());//资料观测日
                data.put("V04004", observationTime.getHours());//资料观测时
                //data.put("V04005", observationTime.getMinutes());//资料观测分 小时不入

                data.put("V07032_04", "999998");//风速传感器距地面高度
                data.put("V07032_01", "999998");//温度传感器距地面高度
                data.put("V07032_02", "999998");//能见度传感器距地面高度
                data.put("V02001", "0");//测站类型

                data.put("V08010", "999998");//地面限定符（温度数据）
                data.put("V02183", "999998");//云探测系统

                //云状
                String[] cloudType = bean.getCloudType().getValue();
                //List<Quality> cloudTypeQuality = bean.getCloudType().getQuality();
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
                statTF.getHorList().add(data);
                }
            
            // 入库重要天气表 begin 2020-3-3 chy
         // 如果是 00 分的数据，并且要素字段不是全部缺测入库重要天气表
		        if (observationTime.getMinutes() == 00 && (
		        		(bean.getExtremeWindSpeed1Hour().getValue() >= 17 && bean.getExtremeWindSpeed1Hour().getValue() <999998)
		        		|| bean.getTornadoDistance().getValue() < 999998
		        		|| bean.getTornadoDirection().getValue()< 999998 ||  bean.getSnowDepth().getValue()< 999998 ||
			            bean.getDiameterOfOverheadWireIce().getValue()< 999998 || bean.getLargestHailDiameter().getValue()< 999998)) {
		            Map<String, Object> dataWeather = new HashMap<>();
		            dataWeather.put(".table", weatherTable);
		            dataWeather.put("D_RECORD_ID", D_RECORD_ID);//记录标识
		            dataWeather.put("D_DATA_ID", weather_sod_code);//资料标识
		            dataWeather.put("D_IYMDHM", new Date());//入库时间
		            dataWeather.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
		            dataWeather.put("D_UPDATE_TIME", new Date());//更新时间
		            dataWeather.put("D_DATETIME", observationTime);//资料时间
		            dataWeather.put("V_BBB", bean.getCorrectionIndicator());//更正报标志
		            if (num >= 48 & num <= 57) {
		            	dataWeather.put("V01300", stationNumberChina);
		            } else {
		            	dataWeather.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
		            }  
		            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
		                String s = (String) getStationInfo().get(stationNumberChina + "+01");
		                dataWeather.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
		                dataWeather.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
		            } else {
		            	dataWeather.put("V02301", "999999");
		            	dataWeather.put("V_ACODE",getVacode(file.getName(),stationNumberChina));
		            }
		            dataWeather.put("V01301", stationNumberChina);
		            dataWeather.put("V04001", (observationTime.getYear() + 1900));//资料观测年
		            dataWeather.put("V04002", (observationTime.getMonth() + 1));//资料观测月
		            dataWeather.put("V04003", observationTime.getDate());//资料观测日
		            dataWeather.put("V04004", observationTime.getHours());//资料观测时
		            dataWeather.put("V04005", observationTime.getMinutes());//资料观测分 小时不入
		            dataWeather.put("V05001", bean.getLatitude());//纬度
		            dataWeather.put("V06001", bean.getLongitude());//经度
		            dataWeather.put("V07001", bean.getHeightOfSationGroundAboveMeanSeaLevel());//测站高度
		            //如果风数据段极大风速11046≥17m/s，属于重要天气
		            if(bean.getExtremeWindSpeed1Hour().getValue() < 17){
		            	dataWeather.put("V11211", 999998); // 风向
		            	dataWeather.put("Q11211", 9);
		            	dataWeather.put("V11046", 999998); // 风速
		            	dataWeather.put("Q11046", 9);
		            }
		            else{
		            	dataWeather.put("V11211", bean.getWindDirectionWhenSpeedMax().getValue());
		            	dataWeather.put("Q11211", bean.getWindDirectionWhenSpeedMax().getQuality().get(1).getCode());
		            	dataWeather.put("V11046", bean.getExtremeWindSpeed1Hour().getValue());
		            	dataWeather.put("Q11046", bean.getExtremeWindSpeed1Hour().getQuality().get(1).getCode());
		            }
		            dataWeather.put("V20401", bean.getTornadoDistance().getValue());// 龙卷类型和距离
		            dataWeather.put("Q20401", bean.getTornadoDistance().getQuality().get(1).getCode());
		            dataWeather.put("V20402", bean.getTornadoDirection().getValue()); //龙卷所在方位
		            dataWeather.put("Q20402", bean.getTornadoDirection().getQuality().get(1).getCode());
		            dataWeather.put("V13013", bean.getSnowDepth().getValue()); //雪深
		            dataWeather.put("Q13013", bean.getSnowDepth().getQuality().get(1).getCode());
		            dataWeather.put("V20326", bean.getDiameterOfOverheadWireIce().getValue()); // 电线积冰直径
		            dataWeather.put("Q20326", bean.getDiameterOfOverheadWireIce().getQuality().get(1).getCode());
		            dataWeather.put("V20066", bean.getLargestHailDiameter().getValue()); // 冰雹最大直径
		            dataWeather.put("Q20066", bean.getLargestHailDiameter().getQuality().get(1).getCode()); 
		//            dataWeather.put("V13019", bean.getPrecipitation1Hour().getValue()); // 过去1小时降水
		//            dataWeather.put("Q13019", bean.getPrecipitation1Hour().getQuality().get(1).getCode());
		//            dataWeather.put("V13020", bean.getPrecipitation3Hours().getValue()); // 过去3小时降水
		//            dataWeather.put("Q13020", bean.getPrecipitation3Hours().getQuality().get(1).getCode());
		//            dataWeather.put("V13021", bean.getPrecipitation6Hours());// 过去6小时降水
		//            dataWeather.put("Q13021", bean.getPrecipitation6Hours().getQuality().get(1).getCode());
		//            dataWeather.put("V13011_24", bean.getPrecipitation24Hours().getValue()); // 日降水量
		//            dataWeather.put("Q13011_24", bean.getPrecipitation24Hours().getQuality().get(1).getCode());
			        dataWeather.put("V13019", 999998); // 过去1小时降水
			        dataWeather.put("Q13019", 9);
			        dataWeather.put("V13020", 999998); // 过去3小时降水
			        dataWeather.put("Q13020", 9);
			        dataWeather.put("V13021", 999998);// 过去6小时降水
			        dataWeather.put("Q13021", 9);
			        dataWeather.put("V13011_24", 999998); // 日降水量
			        dataWeather.put("Q13011_24", 9);
		            dataWeather.put("V20302_017", 999998); // 雷暴
		            dataWeather.put("Q20302_017", 9); 
		//            dataWeather.put("V20001", bean.getHorizontalVisibilityHourly().getValue()); // 视程障碍能见度             
		//            dataWeather.put("Q20001", bean.getHorizontalVisibilityHourly().getQuality().get(1).getCode());
		//            dataWeather.put("V20003", bean.getPresentWeather().getValue());// 视程障碍天气现象
		//            dataWeather.put("Q20003", bean.getPresentWeather().getQuality().get(1).getCode());
		            dataWeather.put("V20001", 999998); // 视程障碍能见度             
		            dataWeather.put("Q20001", bean.getHorizontalVisibilityHourly().getQuality().get(1).getCode());
		            dataWeather.put("V20003", 999998);// 视程障碍天气现象
		            dataWeather.put("Q20003", bean.getPresentWeather().getQuality().get(1).getCode());
		            dataWeather.put("V20214",999998); //最大冰雹质量
		            dataWeather.put("Q20214",9); 
		            
		            dataWeather.put("D_SOURCE_ID",getEvent());//CTS编码
		            
		            statTF.getWeatherList().add(dataWeather);
		         }
            // end 2020-3-3 chy 
            //入分钟表
            Map<String, Object> dataMin = new HashMap<>();
            dataMin.put(".table", minTable);
            dataMin.put("D_RECORD_ID", D_RECORD_ID);//记录标识
            dataMin.put("D_DATA_ID", mul_sod_code);//资料标识
            dataMin.put("D_IYMDHM", new Date());//入库时间
            dataMin.put("D_RYMDHM", new Date(file.lastModified()));//收到时间
            dataMin.put("D_UPDATE_TIME", new Date());//更新时间
            dataMin.put("D_SOURCE_ID",getEvent());//CTS编码
            dataMin.put("V01301", stationNumberChina);
            if (num >= 48 & num <= 57) {
                dataMin.put("V01300", stationNumberChina);
            } else {
                dataMin.put("V01300", String.valueOf(num) + stationNumberChina.substring(1));
            }
            //Map<String, Object> proMap = StationInfo.getProMap();
            if (getStationInfo().containsKey(stationNumberChina + "+01")) {
                //System.out.println("=========查到到==========="+sid);
                String s = (String) getStationInfo().get(stationNumberChina + "+01");
                dataMin.put("V02301", "null".equals(s.split(",")[6]) ? "999999" : s.split(",")[6]);
                dataMin.put("V_ACODE", "null".equals(s.split(",")[5]) ? "999999" : s.split(",")[5]);
            } else {
                //System.out.println("=========未查到到==========="+sid);
                dataMin.put("V02301", "999999");
                //dataMin.put("V_ACODE", "999999");
                dataMin.put("V_ACODE",getVacode(file.getName(),stationNumberChina));
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
            //分钟降水量
            int size = bean.getPrecipitationEveryMinutes().size();
            Double V13011 = 0.0;
            Integer Q13011 = 0;
            if(observationTime.getMinutes() == 00){
            	//0点时入最后两位
            	V13011 = bean.getPrecipitationEveryMinutes().get(size - 1).getValue();
                Q13011 = bean.getPrecipitationEveryMinutes().get(size - 1).getQuality().get(1).getCode();
            }else{
            	V13011 = bean.getPrecipitationEveryMinutes().get(observationTime.getMinutes() - 1).getValue();
            	Q13011 = bean.getPrecipitationEveryMinutes().get(observationTime.getMinutes() - 1).getQuality().get(1).getCode();
            }
            dataMin.put("V13011", V13011);
            dataMin.put("Q13011",Q13011);
            
            dataMin.put("V13013", bean.getSnowDepth().getValue());//总雪深
            dataMin.put("V13033", bean.getEvaporation().getValue());//小时蒸发量
            dataMin.put("V20001_701_01", bean.getHorizontalVisibility1Minitue().getValue());//1分钟平均水平能见度
            dataMin.put("V20001_701_10", bean.getHorizontalVisibility10Minitues().getValue());//10分钟平均水平能见度
            dataMin.put("V20010", bean.getCloudAmount().getValue());//云量
            dataMin.put("V20013", bean.getCloudHeightLowAndMiddle().getValue());//云底高度
            dataMin.put("V20211", "999998");//分钟连续观测天气现象
            dataMin.put("Q10004", bean.getStationPressure().getQuality().get(1).getCode());//本站气压质量标志
            dataMin.put("Q10051", bean.getPressureReducedToMeanSeaLevel().getQuality().get(1).getCode());//海平面气压质量控制标志
            dataMin.put("Q11201", bean.getWindDirectionCurrent().getQuality().get(1).getCode());//瞬时风向质量标志
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
            //dataMin.put("Q13011", bean.getPrecipitationAdditionalManualObservational().getQuality().get(1).getCode());//分钟降水量质量标志
            dataMin.put("Q13013", bean.getSnowDepth().getQuality().get(1).getCode());//总雪深质量标志
            dataMin.put("Q20001_701_01", bean.getHorizontalVisibility1Minitue().getQuality().get(1).getCode());//1分钟平均水平能见度质量标志
            dataMin.put("Q20001_701_10", bean.getHorizontalVisibility10Minitues().getQuality().get(1).getCode());//10分钟平均水平能见度质量标志
            dataMin.put("Q20010", bean.getCloudAmount().getQuality().get(1).getCode());//云量质量标志
            dataMin.put("Q20013", bean.getCloudHeightLowAndMiddle().getQuality().get(1).getCode());//云底高度质量标志
            statTF.getMinList().add(dataMin);
            
            //入分钟降水表,是5的整倍数
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
                    // 2020-3-31 chy
                    date.setMinutes(observationTime.getMinutes() - j);
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
                    
                    dataMinPre.put("V13011", bean.getPrecipitationEveryMinutes().get(date.getMinutes() - 1).getValue());
                    dataMinPre.put("Q13011", bean.getPrecipitationEveryMinutes().get(date.getMinutes() - 1).getQuality().get(1).getCode());
                    statTF.getMinPreList().add(dataMinPre);
                }
            }
            // 入过去1小时的逐分钟降水表
            if(observationTime.getMinutes() == 00) {
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
	                    
	                 // 2020-3-31 chy date.getMinutes() 改为 date.getMinutes() - 1
                    	dataMinPre.put("V13011", bean.getPrecipitationEveryMinutes().get(t - 1).getValue());
                    	dataMinPre.put("Q13011", bean.getPrecipitationEveryMinutes().get(t - 1).getQuality().get(1).getCode());
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
            System.out.println("报文转换错误");
            statTF.getBuffer().append("reports inssrt error!");
        }

        return statTF;
    }
    /*
     * 获取5分钟和10分钟的累计降水量
     * 
     */
    @SuppressWarnings("deprecation")
	public List<Object> getCumPre5And10(SurfaceObservationDataNation bean,Date observationTime){
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
        	int cumPreQ = bean.getPrecipitationEveryMinutes().get(date.getMinutes() - j).getQuality().get(1).getCode();
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
            	int cumPreQ = bean.getPrecipitationEveryMinutes().get(date.getMinutes() - j).getQuality().get(1).getCode();
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