package com.thinkgem.jeesite.mybatis.modules.report.dao;

import com.thinkgem.jeesite.mybatis.common.utils.ConnectionPoolFactory;

import com.thinkgem.jeesite.mybatis.common.utils.DateUtils;
import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.YearValueTab;
import com.thinkgem.jeesite.mybatis.modules.subject.entity.ProductImgDef;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * 连接虚谷数据库查询信息
 * @author yangkq
 * @version 1.0
 * @date 2020/3/25
 */
@Repository
public class XuGuSearchDao {
    private JdbcTemplate jdbcTemplate =  ConnectionPoolFactory.getInstance().getJdbcTemplate("xugu");
    private JdbcTemplate jdbcTemplate2 =  ConnectionPoolFactory.getInstance().getJdbcTemplate("xugu2");
    public  Map<String,Object> getQueryDataList( List<String> station, String starTime, String endTime,  List<String> queryDatas,String dataType,String type,int pageno,int pagesize) {
        String sql = "select V01300";
        String orderbySql="";
        String tableName=null;
        if ("1".equals(dataType)) { // 定时
            tableName="VIEW_PARAM_HOUR";
            sql+=",V04001,V04002,V04003,V04004";
            orderbySql="V04001,V04002,V04003,V04004";
        } else if ("2".equals(dataType)) { // 日值
            tableName="VIEW_PARAM_DAY";
            sql+=",V04001,V04002,V04003";
            orderbySql="V04001,V04002,V04003";
        } else if ("3".equals(dataType)) { // 旬
            tableName="VIEW_PARAM_MEADOW";
            sql+=",V04001,V04002,V04290";
            orderbySql="V04001,V04002,V04290";
        }else if("4".equals(dataType)){//月
            tableName="VIEW_PARAM_MONTH";
            sql+=",V04001,V04002";
            orderbySql="V04001,V04002";
        }else if ("5".equals(dataType)) { // 年
            tableName="VIEW_PARAM_YEAR";
            sql+=",V04001";
            orderbySql="V04001";
        }else {//日照
            tableName="VIEW_PARAM_SUNLIGHT";
        }
        String stations="";
        for(int i=0,size=station.size();i<size;i++){
            String t=station.get(i);
            if(i==size-1){
                stations+="'"+t+"'";
            }else{
                stations+="'"+t+"',";
            }

        }
        for(int i=0,size=queryDatas.size();i<size;i++){
            String field=queryDatas.get(i);
            if(i==size-1){
                sql+=","+field+",rownum";
            }else {
                sql += ","+field;
            }
        }
        String sql1=sql;
        sql+=" from( "+sql1+" from "+tableName;
        sql+=" where V01300 in ("+stations+") and D_DateTIME between '"+starTime+"' and '"+endTime+"' order by "+orderbySql+")";
//        if(startDay==null) {
//         sql+=getYearAndMonth(startYear, startMonth, endYear, endMonth);
//        }else {
//            if (startHour == null) {
//                SeparatedDate sd = new SeparatedDate(Integer.parseInt(startYear), Integer.parseInt(startMonth), Integer.parseInt(startDay)
//                        , Integer.parseInt(endYear), Integer.parseInt(endMonth), Integer.parseInt(endDay));
//                sql += " where" + sd;
//            }else{
//
//            }
//        }

        String countsql="select count(*) from ("+sql+")";
       long coount=jdbcTemplate.queryForLong(countsql);
        if ("1".equals(type)) {//分页
            long start=(pageno-1)*pagesize;
            sql+=" limit "+start+","+(start+pagesize+1);
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        Map<String,Object> map=new HashMap<>();
        map.put("dataList",list);
        map.put("countTotal",coount);
        return map;
    }

    /**
     * 查询月值或年值拼写的sql
     * @param startYear
     * @param startMonth
     * @param endYear
     * @param endMonth
     * @return
     */
    private String getYearAndMonth(String startYear,String startMonth,String endYear,String endMonth){
        String sql="";
        if(startMonth!=null){
            if(Integer.parseInt(startYear) < Integer.parseInt(endYear)){
                int newYear=Integer.parseInt(startYear)+1;
                if(newYear==Integer.getInteger(endYear)){
                    sql+=" where V04001 = "+startYear+" and V04002 between "+startMonth+" and 12";
                    sql+=" or V04001 = "+endYear+" and V04002 between 1 and "+endMonth;
                }else{
                    sql+=" where V04001 = "+startYear+" and V04002 between "+startMonth+" and 12";
                    sql+=" or V04001 between "+newYear +" and "+endYear;
                    sql+=" or V04001 = "+endYear+" and V04002 between 1 and"+endMonth;
                }
            }else {
                sql += " where V04001 = "+startYear;
                if(startMonth.equals(endMonth)){
                    sql+=" and V04002 = "+startMonth;
                }else {
                    sql += " and V04002 between " + startMonth + " and " + endMonth + " ";
                }
            }
        }else{
            if(startYear.equals(endYear)){
                sql += " where V04001 = " + startYear ;
            }else {
                sql += " where V04001 between " + startYear + " and " + endYear;
            }
        }
        return sql;
    }

    public List<Map<String, Object>> getLastYearMonthData(int year) {
        String sql="select * from View_param_month where V04001="+year+" order by V04002";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return  list;
    }

    public void insertYearData(List<YearValueTab> list,String yearTableName) {
        PreparedStatement pstm = null;
        Connection conn=null;
        try {
            conn = jdbcTemplate.getDataSource().getConnection();
            String sql=null;
            if(list.size()>0){
                sql="insert into "+yearTableName+"(" +
                        "D_RETAIN_ID,D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V01300,V05001,V06001,V07001,V07031,V02301,V_ACODE,V04001,V10004_701,V10301,V10301_040,V10301_067,V10302,V10302_040,V10302_067,V10051_701,V10301_avg,V10302_avg,V12001_701,V12306,maxMonthTemp,minMonthTemp,V12011_701,V12012_701," +
                        "V12011,V12011_040,V12011_067,V12012,V12012_040,V12012_067,V12303_701,V12304,V12304_040,V12304_067,V12305,V12305_040,V12305_067,V12605_30,V12710_30,V12712_30,V12711_30,V12605_35,V12710_35,V12712_35,V12711_35,V12605_40,V12710_40,V12712_40,V12711_40,V12607_02,V12713_02,V12715_02,V12714_02,V12603,V12713_00," +
                        "V12715_00,V12714_00,V12606_02,V12716_02,V12718_02,V12717_02,V12606_05,V12716_05,V12718_05,V12717_05,V12606_10,V12716_10,V12718_10,V12717_10,V12606_15,V12716_15,V12718_15,V12717_15,V12606_30,V12716_30,V12718_30,V12717_30,V12606_40,V12716_40,V12718_40,V12717_40,V12705_0,V12705_00,V12703_00,V12706_00,V12704_00," +
                        "V12700_00,V12701_00,V12702_00,V12705_5,V12705_05,V12703_05,V12706_05,V12704_05,V12700_05,V12701_05,V12702_05,V12705_10C,V12705_10,V12703_10,V12706_10,V12704_10,V12700_10,V12701_10,V12702_10,V12705_15C,V12705_15,V12703_15,V12706_15,V12704_15,V12700_15,V12701_15,V12702_15,V12705_20C,V12705_20,V12703_20,V12706_20," +
                        "V12704_20,V12700_20,V12701_20,V12702_20,V12705_22C,V12705_22,V12703_22,V12706_22,V12704_22,V12700_22,V12701_22,V12702_22,V12610_26,V12611_18,V13004_701,balltemp_avg,V13004_MAX,V13004_MAX_M,V13004_MAX_D,V13004_MIN,V13004_MIN_M,V13004_MIN_D,V13003_701,V13007,V13007_040,V13007_067,V20010_701,V20051_701,V20501_02,V20500_08,V20503_02," +
                        "V20502_08,V13305,V13306,V13305_SD,V13306_SD,V13052,V13052_040,V13052_067,V13353,V13355_001,V13355_005,V13355_010,V13355_025,V13355_050,V13355_100,V13355_150,V13355_250,V20430,V13380,V20438,V20431,V20432,V20437,V20433,V13381,V20434,V20436,V20435,V13302_01,V13302_01_040,V13302_01_067," +
                        "V13302_01_058,V13302_01_060,V13382_005,V13382_005_STIME,V13382_010,V13382_010_STIME,V13382_015,V13382_015_STIME,V13382_020,V13382_020_STIME,V13382_030,V13382_030_STIME,V13382_045,V13382_045_STIME,V13382_060,V13382_060_STIME,V13382_090,V13382_090_STIME,V13382_120,V13382_120_STIME,V13382_180,V13382_180_STIME,V13382_240,V13382_240_STIME,V13382_360,V13382_360_STIME,V13382_540,V13382_540_STIME,V13382_720,V13382_720_STIME,V13382_144," +
                        "V13382_144_STIME,V04330_060,V04330_089,V04330_010,V04330_001,V04330_056,V04330_048,V04330_003,V04330_007,V04330_006,V04330_005,V04330_008,V04330_015,V04330_031,V04330_042,V04330_017,V04330_002,V04330_070,V04330_016,V20305_540,V20309_010,V20309_005,V20309_001,V13032,V13033,V13032_MAX,V13032_MAX_M,V13032_MAX_D,V13033_MAX,V13033_MAX_M,V13033_MAX_D," +
                        "V13334,V13334_040,V13334_067,V13330,V13330_040,V13330_067,V13356_001,V13356_005,V13356_010,V13356_020,V13356_030,V20440_NS,V20441_NS,V20442_NS,V20440_WE,V20441_WE,V20442_WE,V20440_040_NS,V20440_067_NS,V20440_058_NS,V20440_060_NS,V20440_040_WE,V20440_067_WE,V20440_058_WE,V20440_060_WE,V11291_701,V11042,V11296_CHAR,V11042_040,V11042_067,V11042_05," +
                        "V11042_10,V11042_12,V11042_15,V11042_17,V11046,V11211_CHAR,V11046_040,V11046_067_CHAR,V11350_NNE,V11350_NE,V11350_ENE,V11350_E,V11350_ESE,V11350_SE,V11350_SSE,V11350_S,V11350_SSW,V11350_SW,V11350_WSW,V11350_W,V11350_WNW,V11350_NW,V11350_NNW,V11350_N,V11350_C,V11314_CHAR,V11314_061,V11315_CHAR,V11315_061,V11351_NNE,V11351_NE," +
                        "V11351_ENE,V11351_E,V11351_ESE,V11351_SE,V11351_SSE,V11351_S,V11351_SSW,V11351_SW,V11351_WSW,V11351_W,V11351_WNW,V11351_NW,V11351_NNW,V11351_N,V11042_NNE,V11042_NE,V11042_ENE,V11042_E,V11042_ESE,V11042_SE,V11042_SSE,V11042_S,V11042_SSW,V11042_SW,V11042_WSW,V11042_W,V11042_WNW,V11042_NW,V11042_NNW,V11042_N,V12120_701," +
                        "V12311_701,V12121_701,V12620,V12311,V12311_040,V12311_067,V12121,V12121_040,V12121_067,V12030_701_005,V12030_701_010,V12030_701_015,V12030_701_020,V12030_701_040,V12030_701_080,V12030_701_160,V12030_701_320,V12314_701,V12315_701,V12316_701,V12315,V12315_040,V12315_067,V12316,V12316_040,V12316_067,V20334,V20334_040,V20334_067,V14032,sunTime," +
                        "V14033,V20302_171,V20302_172,v14311,v14312,v14311_05,v14311_05_04_002,v14311_05_04_003,v14311_05_04_004,v14312_05,v14312_05_04_002,v14312_05_04_003,v14312_05_04_004" +
                        ")values(" +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?);";
                pstm = conn.prepareStatement(sql);
            }
            conn.setAutoCommit(false);
            String currenttime=DateUtils.getDateTime();
            for(YearValueTab yearValueTab:list){
                int ii=1;
                pstm.setString(ii++, yearValueTab.getD_RECORD_ID());//记录标识 ( 系统自动生成的流水号 )
                pstm.setString(ii++, yearValueTab.getD_DATA_ID());//资料标识 ( 资料的4级编码 )
                pstm.setString(ii++, currenttime);//入库时间 ( 插表时的系统时间 )
                pstm.setString(ii++, currenttime);//收到时间 ( DPC消息生成时间 )
                pstm.setString(ii++, currenttime);//更新时间 ( 根据CCx对记录更新时的系统时间 )
                pstm.setString(ii++, yearValueTab.getD_DATETIME());//资料时间 ( 由V04001组成 )
                pstm.setString(ii++, yearValueTab.getV01300());//区站号(字符)
                if(yearValueTab.getV01300()==null){
                    pstm.setString(ii++, "999999");
                }else{
                    pstm.setString(ii++, yearValueTab.getV01300());//区站号(数字)
                }
                if(yearValueTab.getV05001()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV05001()+"",1));//纬度 ( 单位：度 )
                }
                if(yearValueTab.getV06001()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV06001()+"",1));//经度 ( 单位：度 )
                }
                if(yearValueTab.getV07001()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV07001()+"",1));//测站高度 ( 单位：米 )
                }
                if(yearValueTab.getV07031()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV07031()+"",1));//气压传感器海拔高度 ( 单位：米 )
                }
                if(yearValueTab.getV02301()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV02301());//台站级别 ( 代码表 )
                }
                if(yearValueTab.getV_ACODE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV_ACODE());//中国行政区划代码 ( 代码表 )
                }
                if(yearValueTab.getV04001()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04001());//年
                }
                if(yearValueTab.getV10004_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV10004_701()+"",1));//平均本站气压 ( 单位：百帕 )
                }
                if(yearValueTab.getV10301()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV10301()+"",1));//极端最高本站气压 ( 单位：百帕 )
                }
                if(yearValueTab.getV10301_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV10301_040());//极端最高本站气压出现日数
                }
                pstm.setString(ii++, yearValueTab.getV10301_067());//极端最高本站气压出现月日 ( 字符 )
                if(yearValueTab.getV10302()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV10302()+"",1));//极端最低本站气压 ( 单位：百帕 )
                }
                if(yearValueTab.getV10302_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV10302_040());//极端最低本站气压出现日数
                }
                pstm.setString(ii++, yearValueTab.getV10302_067());//极端最低本站气压出现月日 ( 字符 )
                if(yearValueTab.getV10051_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV10051_701()+"",1));//平均海平面气压 ( 单位：百帕 )
                }
                if(yearValueTab.getV10301_avg()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV10301_avg()+"",1));//年平均最高本站气压
                }
                if(yearValueTab.getV10302_avg()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV10302_avg()+"",1));//年平均最低本站气压
                }
                if(yearValueTab.getV12001_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12001_701()+"",1));//平均气温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12306()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12306()+"",1));//气温年较差 ( 单位：摄氏度 )
                }
                if(yearValueTab.getMaxMonthTemp()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getMaxMonthTemp()+"",1));//月平均最高气温
                }
                if(yearValueTab.getMinMonthTemp()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getMinMonthTemp()+"",1));//月平均最低气温
                }
                if(yearValueTab.getV12011_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12011_701()+"",1));//年平均日最高气温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12012_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12012_701()+"",1));//年平均日最低气温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12011()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12011()+"",1));//极端最高气温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12011_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12011_040());//极端最高气温出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV12011_067());//极端最高气温出现月日 ( 字符 )
                if(yearValueTab.getV12012()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12012()+"",1));//极端最低气温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12012_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12012_040());//极端最低气温出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV12012_067());//极端最低气温出现月日 ( 字符 )
                if(yearValueTab.getV12303_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12303_701()+"",1));//平均气温日较差 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12304()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12304()+"",1));//最大气温日较差 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12304_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12304_040());//最大气温日较差出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV12304_067());//最大气温日较差出现月日 ( 字符 )
                if(yearValueTab.getV12305()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12305()+"",1));//最小气温日较差 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12305_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12305_040());//最小气温日较差出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV12305_067());//最小气温日较差出现月日 ( 字符 )
                if(yearValueTab.getV12605_30()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12605_30());//日最高气温≥30℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12710_30()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12710_30());//日最高气温≥30.0℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12712_30()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12712_30());//日最高气温≥30.0℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12711_30()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12711_30());//日最高气温≥30.0℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12605_35()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12605_35());//日最高气温≥35℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12710_35()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12710_35());//日最高气温≥35.0℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12712_35()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12712_35());//日最高气温≥35.0℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12711_35()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12711_35());//日最高气温≥35.0℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12605_40()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12605_40());//日最高气温≥40℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12710_40()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12710_40());//日最高气温≥40.0℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12712_40()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12712_40());//日最高气温≥40.0℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12711_40()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12711_40());//日最高气温≥40.0℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12607_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12607_02());//日最低气温＜2℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12713_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12713_02());//日最低气温＜2℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12715_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12715_02());//日最低气温＜2℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12714_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12714_02());//日最低气温＜2℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12603()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12603());//日最低气温＜0℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12713_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12713_00());//日最低气温＜0℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12715_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12715_00());//日最低气温＜0℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12714_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12714_00());//日最低气温＜0℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12606_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12606_02());//日最低气温＜-2℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12716_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12716_02());//日最低气温＜-2℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12718_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12718_02());//日最低气温＜-2℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12717_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12717_02());//日最低气温＜-2℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12606_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12606_05());//日最低气温＜-5℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12716_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12716_05());//日最低气温＜-5℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12718_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12718_05());//日最低气温＜-5℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12717_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12717_05());//日最低气温＜-5℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12606_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12606_10());//日最低气温＜-10℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12716_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12716_10());//日最低气温＜-10℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12718_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12718_10());//日最低气温＜-10℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12717_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12717_10());//日最低气温＜-10℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12606_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12606_15());//日最低气温＜-15℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12716_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12716_15());//日最低气温＜-15℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12718_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12718_15());//日最低气温＜-15℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12717_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12717_15());//日最低气温＜-15℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12606_30()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12606_30());//日最低气温＜-30℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12716_30()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12716_30());//日最低气温＜-30℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12718_30()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12718_30());//日最低气温＜-30℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12717_30()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12717_30());//日最低气温＜-30℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12606_40()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12606_40());//日最低气温＜-40℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12716_40()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12716_40());//日最低气温＜-40℃最长连续日数 ( 单位：日 )
                }
                if(yearValueTab.getV12718_40()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12718_40());//日最低气温＜-40℃最长连续日数之止月 (  )
                }
                if(yearValueTab.getV12717_40()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12717_40());//日最低气温＜-40℃最长连续日数之止日 (  )
                }
                if(yearValueTab.getV12705_0()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_0());//日平均气温稳定通过0.0℃日数
                }
                if(yearValueTab.getV12705_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_00());//日平均气温稳定通过0.0℃起始月 (  )
                }
                if(yearValueTab.getV12703_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12703_00());//日平均气温稳定通过0.0℃起始日 (  )
                }
                if(yearValueTab.getV12706_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12706_00());//日平均气温稳定通过0.0℃止月 (  )
                }
                if(yearValueTab.getV12704_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12704_00());//日平均气温稳定通过0.0℃止日 (  )
                }
                if(yearValueTab.getV12700_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12700_00());//日平均气温稳定通过0.0℃之积温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12701_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12701_00());//日平均气温稳定通过0.0℃之降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV12702_00()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12702_00());//日平均气温稳定通过0.0℃之日照时数 ( 单位：小时 )
                }
                if(yearValueTab.getV12705_5()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_5());//日平均气温稳定通过5.0℃日数
                }
                if(yearValueTab.getV12705_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_05());//日平均气温稳定通过5.0℃起始月 (  )
                }
                if(yearValueTab.getV12703_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12703_05());//日平均气温稳定通过5.0℃起始日 (  )
                }
                if(yearValueTab.getV12706_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12706_05());//日平均气温稳定通过5.0℃止月 (  )
                }
                if(yearValueTab.getV12704_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12704_05());//日平均气温稳定通过5.0℃止日 (  )
                }
                if(yearValueTab.getV12700_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12700_05());//日平均气温稳定通过5.0℃之积温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12701_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12701_05());//日平均气温稳定通过5.0℃之降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV12702_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12702_05());//日平均气温稳定通过5.0℃之日照时数 ( 单位：小时 )
                }
                if(yearValueTab.getV12705_10C()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_10C());//日平均气温稳定通过10.0℃日数
                }
                if(yearValueTab.getV12705_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_10());//日平均气温稳定通过10.0℃起始月 (  )
                }
                if(yearValueTab.getV12703_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12703_10());//日平均气温稳定通过10.0℃起始日 (  )
                }
                if(yearValueTab.getV12706_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12706_10());//日平均气温稳定通过10.0℃止月 (  )
                }
                if(yearValueTab.getV12704_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12704_10());//日平均气温稳定通过10.0℃止日 (  )
                }
                if(yearValueTab.getV12700_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12700_10());//日平均气温稳定通过10.0℃之积温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12701_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12701_10());//日平均气温稳定通过10.0℃之降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV12702_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12702_10());//日平均气温稳定通过10.0℃之日照时数 ( 单位：小时 )
                }
                if(yearValueTab.getV12705_15C()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_15C());//日平均气温稳定通过15.0℃日数
                }
                if(yearValueTab.getV12705_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_15());//日平均气温稳定通过15.0℃起始月 (  )
                }
                if(yearValueTab.getV12703_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12703_15());//日平均气温稳定通过15.0℃起始日 (  )
                }
                if(yearValueTab.getV12706_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12706_15());//日平均气温稳定通过15.0℃止月 (  )
                }
                if(yearValueTab.getV12704_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12704_15());//日平均气温稳定通过15.0℃止日 (  )
                }
                if(yearValueTab.getV12700_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12700_15());//日平均气温稳定通过15.0℃之积温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12701_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12701_15());//日平均气温稳定通过15.0℃之降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV12702_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12702_15());//日平均气温稳定通过15.0℃之日照时数 ( 单位：小时 )
                }
                if(yearValueTab.getV12705_20C()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_20C());//日平均气温稳定通过20.0℃日数
                }
                if(yearValueTab.getV12705_20()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_20());//日平均气温稳定通过20.0℃起始月 (  )
                }
                if(yearValueTab.getV12703_20()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12703_20());//日平均气温稳定通过20.0℃起始日 (  )
                }
                if(yearValueTab.getV12706_20()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12706_20());//日平均气温稳定通过20.0℃止月 (  )
                }
                if(yearValueTab.getV12704_20()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12704_20());//日平均气温稳定通过20.0℃止日 (  )
                }
                if(yearValueTab.getV12700_20()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12700_20());//日平均气温稳定通过20.0℃之积温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12701_20()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12701_20());//日平均气温稳定通过20.0℃之降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV12702_20()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12702_20());//日平均气温稳定通过20.0℃之日照时数 ( 单位：小时 )
                }
                if(yearValueTab.getV12705_22C()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_22C());//日平均气温稳定通过0.0℃日数
                }
                if(yearValueTab.getV12705_22()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12705_22());//日平均气温稳定通过22.0℃起始月 (  )
                }
                if(yearValueTab.getV12703_22()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12703_22());//日平均气温稳定通过22.0℃起始日 (  )
                }
                if(yearValueTab.getV12706_22()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12706_22());//日平均气温稳定通过22.0℃止月 (  )
                }
                if(yearValueTab.getV12704_22()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12704_22());//日平均气温稳定通过22.0℃止日 (  )
                }
                if(yearValueTab.getV12700_22()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12700_22());//日平均气温稳定通过22.0℃之积温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12701_22()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12701_22());//日平均气温稳定通过22.0℃之降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV12702_22()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12702_22());//日平均气温稳定通过22.0℃之日照时数 ( 单位：小时 )
                }
                if(yearValueTab.getV12610_26()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12610_26());//冷度日数（日平均气温≥26.0℃） ( 单位：度日 )
                }
                if(yearValueTab.getV12611_18()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12611_18());//暖度日数（日平均气温≤18.0℃） ( 单位：度日 )
                }
                if(yearValueTab.getV13004_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13004_701()+"",1));//平均水汽压 ( 单位：百帕 )
                }
                if(yearValueTab.getBalltemp_avg()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getBalltemp_avg()+"",1));//年平均湿球温度
                }
                if(yearValueTab.getV13004_MAX()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13004_MAX()+"",1));//年最大水汽压
                }
                if(yearValueTab.getV13004_MAX_M()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13004_MAX_M()+"",1));//年最大水汽压出现月份
                }
                if(yearValueTab.getV13004_MAX_D()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13004_MAX_D()+"",1));//年最大水汽压出现日期
                }
                if(yearValueTab.getV13004_MIN()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13004_MIN()+"",1));//年最小水气压
                }
                if(yearValueTab.getV13004_MIN_M()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13004_MIN_M()+"",1));// 年最小水汽压出现月份
                }
                if(yearValueTab.getV13004_MIN_D()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13004_MIN_D()+"",1));// 年最小水汽压出现日期
                }
                if(yearValueTab.getV13003_701()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13003_701());//平均相对湿度 ( 单位：% )
                }
                if(yearValueTab.getV13007()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13007());//最小相对湿度 ( 单位：% )
                }
                if(yearValueTab.getV13007_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13007_040());//最小相对湿度出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV13007_067());//最小相对湿度出现月日 ( 字符 )
                if(yearValueTab.getV20010_701()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20010_701());//平均总云量 ( 单位：% )
                }
                if(yearValueTab.getV20051_701()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20051_701());//平均低云量 ( 单位：% )
                }
                if(yearValueTab.getV20501_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20501_02());//日平均总云量< 2.0日数 ( 单位：日 )
                }
                if(yearValueTab.getV20500_08()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20500_08());//日平均总云量> 8.0日数 ( 单位：日 )
                }
                if(yearValueTab.getV20503_02()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20503_02());//日平均低云量< 2.0日数 ( 单位：日 )
                }
                if(yearValueTab.getV20502_08()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20502_08());//日平均低云量> 8.0日数 ( 单位：日 )
                }
                if(yearValueTab.getV13305()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13305()+"",1));//20-20时年降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13306()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13306()+"",1));//08-08时年降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13305_SD()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13305_SD()+"",1));//20-20时年固态降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13306_SD()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13306_SD()+"",1));//08-08时年固态降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13052()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13052()+"",1));//最大日降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13052_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13052_040());//最大日降水量出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV13052_067());//最大日降水量出现月日 ( 字符 )
                if(yearValueTab.getV13353()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13353());//日降水量≥0.1mm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13355_001()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13355_001());//日降水量≥1mm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13355_005()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13355_005());//日降水量≥5mm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13355_010()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13355_010());//日降水量≥10mm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13355_025()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13355_025());//日降水量≥25mm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13355_050()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13355_050());//日降水量≥50mm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13355_100()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13355_100());//日降水量≥100mm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13355_150()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13355_150());//日降水量≥150mm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13355_250()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13355_250());//日降水量≥250mm日数 ( 单位：日 )
                }
                if(yearValueTab.getV20430()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20430());//最长连续降水日数 ( 单位：日 )
                }
                if(yearValueTab.getV13380()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13380());//最长连续降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV20438()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20438());//最长连续降水止月 (  )
                }
                if(yearValueTab.getV20431()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20431());//最长连续降水止日 (  )
                }
                if(yearValueTab.getV20432()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20432());//最长连续无降水日数 ( 单位：日 )
                }
                if(yearValueTab.getV20437()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20437());//最长连续无降水止月 (  )
                }
                if(yearValueTab.getV20433()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20433());//最长连续无降水止日 (  )
                }
                if(yearValueTab.getV13381()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13381());//最大连续降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV20434()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20434());//最大连续降水日数 ( 单位：日 )
                }
                if(yearValueTab.getV20436()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20436());//最大连续降水止月 (  )
                }
                if(yearValueTab.getV20435()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20435());//最大连续降水止日 (  )
                }
                if(yearValueTab.getV13302_01()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13302_01()+"",1));//1小时最大降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13302_01_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13302_01_040());//1小时最大降水量出现日数? (  )
                }
                pstm.setString(ii++, yearValueTab.getV13302_01_067());//1小时最大降水量出现月日 (  )
                if(yearValueTab.getV13302_01_058()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13302_01_058());//1小时最大降水量出现月 (  )
                }
                if(yearValueTab.getV13302_01_060()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, Integer.parseInt(yearValueTab.getV13302_01_060()));//1小时最大降水量出现日 (  )
                }
                if(yearValueTab.getV13382_005()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_005()+"",1));//年最大5分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_005_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_005_STIME());//年最大5分钟降水起始时间（MMddhhmm） ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_010()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_010()+"",1));//年最大10分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_010_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_010_STIME());//年最大10分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_015()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_015()+"",1));//年最大15分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_015_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_015_STIME());//年最大15分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_020()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_020()+"",1));//年最大20分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_020_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_020_STIME());//年最大20分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_030()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_030()+"",1));//年最大30分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_030_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_030_STIME());//年最大30分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_045()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_045()+"",1));//年最大45分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_045_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_045_STIME());//年最大45分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_060()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_060()+"",1));//年最大60分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_060_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_060_STIME());//年最大60分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_090()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_090()+"",1));//年最大90分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_090_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_090_STIME());//年最大90分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_120()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_120()+"",1));//年最大120分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_120_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_120_STIME());//年最大120分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_180()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_180()+"",1));//年最大180分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_180_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_180_STIME());//年最大180分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_240()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_240()+"",1));//年最大240分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_240_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_240_STIME());//年最大240分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_360()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_360()+"",1));//年最大360分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_360_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_360_STIME());//年最大360分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_540()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_540()+"",1));//年最大540分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_540_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_540_STIME());//年最大540分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_720()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_720()+"",1));//年最大720分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_720_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_720_STIME());//年最大720分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV13382_144()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13382_144()+"",1));//年最大1440分钟降水量 ( 单位：毫米 )
                }
                if(yearValueTab.getV13382_144_STIME()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13382_144_STIME());//年最大1440分钟降水起始时间 ( 格式：MMddhhmm )
                }
                if(yearValueTab.getV04330_060()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_060());//雨日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_089()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_089());//冰雹日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_010()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_010());//轻雾日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_001()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_001());//露日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_056()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_056());//雨凇日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_048()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_048());//雾凇日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_003()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_003());//结冰日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_007()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_007());//扬沙日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_006()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_006());//浮尘日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_005()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_005());//霾日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_008()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_008());//龙卷风日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_015()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_015());//大风日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_031()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_031());//沙尘暴日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_042()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_042());//雾日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_017()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_017());//雷暴日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_002()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_002());//霜日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_070()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_070());//降雪日数 ( 单位：日 )
                }
                if(yearValueTab.getV04330_016()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV04330_016());//积雪日数 ( 单位：日 )
                }
                if(yearValueTab.getV20305_540()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20305_540());//电线积冰（雨凇+雾凇）日数 ( 单位：日 )
                }
                if(yearValueTab.getV20309_010()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20309_010());//能见度＜≤10km出现频率 ( 单位：% )
                }
                if(yearValueTab.getV20309_005()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20309_005());//能见度＜5km出现频率 ( 单位：% )
                }
                if(yearValueTab.getV20309_001()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20309_001());//能见度＜≤1km出现频率 ( 单位：% )
                }
                if(yearValueTab.getV13032()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13032()+"",1));//蒸发量(小型) ( 单位：毫米 )
                }
                if(yearValueTab.getV13033()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13033()+"",1));//蒸发量(大型) ( 单位：毫米 )
                }
                if(yearValueTab.getV13032_MAX()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13032_MAX());//年最大小型蒸发量
                }
                if(yearValueTab.getV13032_MAX_M()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13032_MAX_M());// 年最大小型蒸发量出现月份
                }
                if(yearValueTab.getV13032_MAX_D()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13032_MAX_D());// 年最大小型蒸发量出现日期
                }
                if(yearValueTab.getV13033_MAX()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13033_MAX());//年最大大型蒸发量
                }
                if(yearValueTab.getV13033_MAX_M()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13033_MAX_M());//年最大大型蒸发量出现月份
                }
                if(yearValueTab.getV13033_MAX_D()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13033_MAX_D());//年最大大型蒸发量出现日期
                }
                if(yearValueTab.getV13334()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13334()+"",1));//最大积雪深度 ( 单位：厘米 )
                }
                if(yearValueTab.getV13334_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13334_040());//最大积雪深度出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV13334_067());//最大积雪深度出现月日 ( 字符 )
                if(yearValueTab.getV13330()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV13330()+"",1));//最大雪压 ( 单位：克/平方厘米 )
                }
                if(yearValueTab.getV13330_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13330_040());//最大雪压出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV13330_067());//最大雪压出现月日 ( 字符 )
                if(yearValueTab.getV13356_001()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13356_001());//积雪深度≥1cm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13356_005()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13356_005());//积雪深度≥5cm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13356_010()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13356_010());//积雪深度≥10cm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13356_020()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13356_020());//积雪深度≥20cm日数 ( 单位：日 )
                }
                if(yearValueTab.getV13356_030()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV13356_030());//积雪深度≥30cm日数 ( 单位：日 )
                }
                if(yearValueTab.getV20440_NS()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20440_NS());//电线积冰南北最大重量 ( 单位：克 )
                }
                if(yearValueTab.getV20441_NS()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20441_NS());//电线积冰南北最大重量的相应直径 ( 单位：毫米 )
                }
                if(yearValueTab.getV20442_NS()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20442_NS());//电线积冰南北最大重量的相应厚度 ( 单位：毫米 )
                }
                if(yearValueTab.getV20440_WE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20440_WE());//电线积冰东西最大重量 ( 单位：克 )
                }
                if(yearValueTab.getV20441_WE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20441_WE());//电线积冰东西最大重量的相应直径 ( 单位：毫米 )
                }
                if(yearValueTab.getV20442_WE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20442_WE());//电线积冰东西最大重量的相应厚度 ( 单位：毫米 )
                }
                if(yearValueTab.getV20440_040_NS()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20440_040_NS());//电线积冰南北最大重量出现日数 ( 单位：日 )
                }
                if(yearValueTab.getV20440_067_NS()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, Integer.parseInt(yearValueTab.getV20440_067_NS()));//电线积冰南北最大重量出现月日 ( 格式：mmdd )
                }
                if(yearValueTab.getV20440_058_NS()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20440_058_NS());//电线积冰南北最大重量出现月 (  )
                }
                if(yearValueTab.getV20440_060_NS()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, Integer.parseInt(yearValueTab.getV20440_060_NS()));// 电线积冰南北最大重量出现日 (  )
                }
                if(yearValueTab.getV20440_040_WE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20440_040_WE());//电线积冰东西最大重量出现日数 ( 单位：日 )
                }
                if(yearValueTab.getV20440_067_WE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, Integer.parseInt(yearValueTab.getV20440_067_WE()));//电线积冰东西最大重量出现月日 ( 格式：mmdd )
                }
                if(yearValueTab.getV20440_058_WE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20440_058_WE());//电线积冰东西最大重量出现月 (  )
                }
                if(yearValueTab.getV20440_060_WE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, Integer.parseInt(yearValueTab.getV20440_060_WE()));//电线积冰东西最大重量出现日 (  )
                }
                if(yearValueTab.getV11291_701()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11291_701());//平均风速（2分钟） ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV11042()+"",1));//最大风速 ( 单位：米/秒 )
                }
                pstm.setString(ii++, yearValueTab.getV11296_CHAR());//最大风速的风向 ( 代码表 )
                if(yearValueTab.getV11042_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_040());//最大风速之出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV11042_067());//最大风速之出现月日 ( 字符 )
                if(yearValueTab.getV11042_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_05());//日最大风速≥5.0m/s日数 ( 单位：日 )
                }
                if(yearValueTab.getV11042_10()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_10());//日最大风速≥10.0m/s日数 ( 单位：日 )
                }
                if(yearValueTab.getV11042_12()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_12());//日最大风速≥12.0m/s日数 ( 单位：日 )
                }
                if(yearValueTab.getV11042_15()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_15());//日最大风速≥15.0m/s日数 ( 单位：日 )
                }
                if(yearValueTab.getV11042_17()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_17());//日最大风速≥17.0m/s日数 ( 单位：日 )
                }
                if(yearValueTab.getV11046()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11046());//极大风速 ( 单位：米/秒 )
                }
                pstm.setString(ii++, yearValueTab.getV11211_CHAR());//极大风速之风向 ( 代码表 )
                if(yearValueTab.getV11046_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11046_040());//极大风速之出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV11046_067_CHAR());//极大风速之出现月日 ( 字符 )
                if(yearValueTab.getV11350_NNE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_NNE());//NNE风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_NE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_NE());//NE风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_ENE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_ENE());//ENE风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_E()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_E());//E风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_ESE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_ESE());//ESE风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_SE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_SE());//SE风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_SSE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_SSE());//SSE风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_S()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_S());//S风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_SSW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_SSW());//SSW风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_SW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_SW());//SW风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_WSW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_WSW());//WSW风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_W()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_W());//W风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_WNW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_WNW());//WNW风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_NW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_NW());//NW风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_NNW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_NNW());//NNW风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_N()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_N());//N风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11350_C()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11350_C());//C风向（静风）出现频率 ( 单位：% )
                }
                pstm.setString(ii++, yearValueTab.getV11314_CHAR());//最多风向 ( 代码表 )
                if(yearValueTab.getV11314_061()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11314_061());//最多风向出现频率 ( 单位：% )
                }
                pstm.setString(ii++, yearValueTab.getV11315_CHAR());//次多风向 ( 代码表 )
                if(yearValueTab.getV11315_061()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11315_061());//次多风向出现频率 ( 单位：% )
                }
                if(yearValueTab.getV11351_NNE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_NNE());//NNE风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_NE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_NE());//NE风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_ENE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_ENE());//ENE的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_E()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_E());//E风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_ESE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_ESE());//ESE风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_SE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_SE());//SE风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_SSE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_SSE());//SSE风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_S()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_S());//S风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_SSW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_SSW());//SSW风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_SW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_SW());//SW风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_WSW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_WSW());//WSW风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_W()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_W());//W风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_WNW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_WNW());//WNW风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_NW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_NW());//NW风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_NNW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_NNW());//NNW风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11351_N()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11351_N());//N风的平均风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_NNE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_NNE());//NNE风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_NE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_NE());//NE风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_ENE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_ENE());//ENE的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_E()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_E());//E风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_ESE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_ESE());//ESE风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_SE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_SE());//SE风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_SSE()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_SSE());//SSE风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_S()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_S());//S风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_SSW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_SSW());//SSW风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_SW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_SW());//SW风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_WSW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_WSW());//WSW风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_W()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_W());//W风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_WNW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_WNW());//WNW风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_NW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_NW());//NW风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_NNW()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_NNW());//NNW风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV11042_N()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV11042_N());//N风的最大风速 ( 单位：米/秒 )
                }
                if(yearValueTab.getV12120_701()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12120_701());//平均地面温度 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12311_701()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12311_701());//平均最高地面温度 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12121_701()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12121_701());//平均最低地面温度 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12620()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12620());//日最低地面温度≤0.0℃日数 ( 单位：日 )
                }
                if(yearValueTab.getV12311()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12311());//极端最高地面温度 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12311_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12311_040());//极端最高地面温度出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV12311_067());//极端最高地面温度出现月日 ( 字符 )
                if(yearValueTab.getV12121()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12121());//极端最低地面温度 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12121_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12121_040());//极端最低地面温度出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV12121_067());//极端最低地面温度出现月日 ( 字符 )
                if(yearValueTab.getV12030_701_005()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12030_701_005()+"",1));//平均5cm地温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12030_701_010()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12030_701_010()+"",1));//平均10cm地温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12030_701_015()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12030_701_015()+"",1));//平均15cm地温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12030_701_020()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                    pstm.setFloat(ii++, NumberParse(yearValueTab.getV12030_701_020()+"",1));//平均20cm地温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12030_701_040()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12030_701_040()+"",1));//平均40cm地温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12030_701_080()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12030_701_080()+"",1));//平均80cm地温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12030_701_160()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12030_701_160()+"",1));//平均160cm地温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12030_701_320()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12030_701_320()+"",1));//平均320cm地温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12314_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12314_701()+"",1));//平均草面（雪面）温度 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12315_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12315_701()+"",1));//平均最高草面（雪面）温度 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12316_701()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12316_701()+"",1));//平均最低草面（雪面）温度 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12315()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12315()+"",1));//极端最高草面（雪面）气温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12315_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12315_040());//极端最高草面（雪面）气温出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV12315_067());//极端最高草面（雪面）气温出现月日 ( 字符 )
                if(yearValueTab.getV12316()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV12316()+"",1));//极端最低草面（雪面）气温 ( 单位：摄氏度 )
                }
                if(yearValueTab.getV12316_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV12316_040());//极端最低草面（雪面）气温出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV12316_067());//极端最低草面（雪面）气温出现月日 ( 字符 )
                if(yearValueTab.getV20334()==null){
                    pstm.setFloat(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV20334()+"",1));//最大冻土深度 ( 单位：厘米 )
                }
                if(yearValueTab.getV20334_040()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20334_040());//最大冻土深度出现日数 (  )
                }
                pstm.setString(ii++, yearValueTab.getV20334_067());//最大冻土深度出现月日 ( 字符 )
                if(yearValueTab.getV14032()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getV14032()+"",1));//日照总时数 ( 单位：小时 )
                }
                if(yearValueTab.getSunTime()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                     pstm.setFloat(ii++, NumberParse(yearValueTab.getSunTime()+"",1));//日出总时长
                }
                if(yearValueTab.getV14033()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV14033());//日照百分率 ( 单位：% )
                }
                if(yearValueTab.getV20302_171()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20302_171());//日照百分率≥60%日数 ( 单位：日 )
                }
                if(yearValueTab.getV20302_172()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV20302_172());//日照百分率≤20%日数 ( 单位：日 )
                }
                if(yearValueTab.getV14311()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setBigDecimal(ii++, yearValueTab.getV14311());//总辐射辐照度
                }
                if(yearValueTab.getV14312()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setBigDecimal(ii++, yearValueTab.getV14312());//净辐射辐照度
                }
                if(yearValueTab.getV14311_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setBigDecimal(ii++, yearValueTab.getV14311_05());//日总辐射辐照度最大值
                }
                if(yearValueTab.getV14311_05_04_002()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV14311_05_04_002());//日总辐射辐照度最大出现月
                }
                if(yearValueTab.getV14311_05_04_003()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV14311_05_04_003());//日总辐射辐照度最大出现日
                }
                if(yearValueTab.getV14311_05_04_004()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV14311_05_04_004());//日总辐射辐照度最大出现时
                }
                if(yearValueTab.getV14312_05()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setBigDecimal(ii++, yearValueTab.getV14312_05());//日净辐射辐照度最大值
                }
                if(yearValueTab.getV14312_05_04_002()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV14312_05_04_002());//日总辐射辐照度最大出现月
                }
                if(yearValueTab.getV14312_05_04_003()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV14312_05_04_003());//日总辐射辐照度最大出现日
                }
                if(yearValueTab.getV14312_05_04_004()==null){
                    pstm.setInt(ii++, 999999);
                }else{
                    pstm.setInt(ii++, yearValueTab.getV14312_05_04_004());//日总辐射辐照度最大出现时
                }
                pstm.addBatch();
            }
            pstm.executeBatch();
            conn.commit();
            pstm.clearBatch();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(pstm!=null){
                    pstm.close();
                }
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Map<String, Object>> getLastYearDayData(String stationNum, Integer year) {
        String sql="select * from View_param_day where V04001="+year+" and V01300="+stationNum+" order by V04002,V04003";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return  list;
    }
    public List<Map<String, Object>> getLastYearHourData(String stationNum, Integer year) {
        String sql="select * from View_param_hour where V04001="+year+" and V01300="+stationNum+" order by V04002,V04003";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return  list;
    }

    public List<Map<String, Object>> getLastYearRadiData(String stationNum, Integer year) {
        String sql="select * from radi_dig_chn_mul_day_tab where V04001="+year+" and V01300="+stationNum+" order by V04002,V04003";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return  list;
    }

    public List<Map<String, Object>> getRadiDataList(String sql) {
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);
        return  list;
    }

    public void updateProTable(String oldProCode, String oldKind, String procode, String kind) {
        String sql="update SEVP_NMG_THEMATIC_TAB set V_PROD_CODE="+procode
                +",V_ELE_KIND="+kind+" where V_PROD_CODE"+oldProCode+" and V_ELE_KIND="+oldKind;
        jdbcTemplate.update(sql);
    }
    private static Float NumberParse(String value, int t){
        //保留1位小数，4舍5入
        BigDecimal bigD = new BigDecimal(value);
        float newValue = bigD.setScale(t, BigDecimal.ROUND_HALF_UP).floatValue();
        return newValue;
    }
    public void insertIntoProductImg(List<ProductImgDef> list) {
        PreparedStatement pstm = null;
        Connection conn=null;
        try {
            conn = jdbcTemplate2.getDataSource().getConnection();
            String sql=null;
            if(list.size()>0){
                sql="insert into SEVP_NMG_THEMATIC_TAB(" +
                            "D_FILE_ID,D_DATA_ID,V_FILE_NAME,D_STORAGE_SITE,D_FILE_SIZE,D_FILE_FORMAT,D_IYMDHM" +
                        ",D_RYMDHM,D_UPDATE_TIME,D_DATETIME,PRODATE,V04004,ISPUBLISH,V_PROD_CODE,V_ELE_KIND)values(" +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pstm = conn.prepareStatement(sql);
            }
            conn.setAutoCommit(false);
            int index=0;
            for(ProductImgDef pi:list){
                int ii=1;
                index++;
                pstm.setString(ii++, pi.getD_file_id());//记录标识 ( 系统自动生成的流水号 )
                pstm.setString(ii++, pi.getD_data_id());//资料标识 ( 资料的4级编码 )
                pstm.setString(ii++, pi.getV_file_name());//图片名称
                pstm.setString(ii++, pi.getD_storage_site());//图片路径
                pstm.setLong(ii++, pi.getD_file_size());//图片大小
                pstm.setString(ii++, pi.getD_file_format());//图片类型
                pstm.setTimestamp(ii++,pi.getD_iymdhm());//入库时间
                pstm.setTimestamp(ii++,pi.getD_rymdhm());//创建时间
                pstm.setTimestamp(ii++,pi.getD_update_time());//更新时间
                pstm.setTimestamp(ii++,pi.getD_datetime());//产品时间
                pstm.setString(ii++,pi.getProdate());//产品日期
                pstm.setInt(ii++,pi.getV04004());//产品时间-时
                pstm.setInt(ii++,pi.getIspublish());//发布状态
                pstm.setString(ii++,pi.getV_prod_code());//产品代号
                pstm.setString(ii++,pi.getV_ele_kind());//要素种类
                pstm.addBatch();
                if (index % 1000 == 0) {
                    pstm.executeBatch();
                    conn.commit();
                    pstm.clearBatch();
                }
            }
            pstm.executeBatch();
            conn.commit();
            pstm.clearBatch();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if(pstm!=null){
                    pstm.close();
                }
                if(conn!=null){
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
