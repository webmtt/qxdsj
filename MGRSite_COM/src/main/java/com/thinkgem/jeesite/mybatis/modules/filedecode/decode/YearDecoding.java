package com.thinkgem.jeesite.mybatis.modules.filedecode.decode;


import com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean.*;
import com.thinkgem.jeesite.mybatis.modules.filedecode.service.YearInfoHandleService;
import com.thinkgem.jeesite.mybatis.modules.filedecode.util.ReflectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 公共信息封装类
 * @author yangkq
 * @version 1.0
 * @date 2020/4/9
 */
public class YearDecoding {

public List<YearValueTab> packYearValue(List<MonthValueTab> monthList, String yearCts, YearInfoHandleService yearInfoHandle){
    List<YearValueTab> list=new ArrayList<>();
    YearValueTab yearValueTab=null;
    String stationNum=null;
    for(MonthValueTab monthValueTab:monthList){
        String station=monthValueTab.getV01300();
        if(stationNum==null||(!station.equals(stationNum))){
            if(yearValueTab!=null){
                list.add(yearValueTab);
            }
            stationNum=station;
            yearValueTab=new YearValueTab();
            yearValueTab.setD_DATA_ID(yearCts);
            yearValueTab.setD_RECORD_ID(UUID.randomUUID().toString());
            yearValueTab.setV01301(monthValueTab.getV01301());
            yearValueTab.setV01300(monthValueTab.getV01300());
            yearValueTab.setV05001(monthValueTab.getV05001());
            yearValueTab.setV06001(monthValueTab.getV06001());
            yearValueTab.setV07001(monthValueTab.getV07001());
            yearValueTab.setV07031(monthValueTab.getV07031());
            yearValueTab.setV04001(monthValueTab.getV04001());
            String dataTime=monthValueTab.getV04001()+"";
            yearValueTab.setD_DATETIME(dataTime);
        }
        // 平均本站气压 ( 单位：百帕 )
        averageHandle(yearValueTab,"V10004_701", "V10004_701", monthValueTab, 12);
        // 极端最高本站气压 ( 单位：百帕 )
        extremeHandle(yearValueTab, "V10301","V10301",null,null, monthValueTab,"b");
        // 极端最高本站气压出现日数、极端最高本站气压出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V10301_040,V10301_067","V10301", monthValueTab,"V10301_040,V10301_060_CHAR","V10301");
        // 极端最低本站气压 ( 单位：百帕 )
        extremeHandle(yearValueTab, "V10302","V10302",null,null, monthValueTab,"s");
        // 极端最低本站气压出现日数、极端最低本站气压出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V10302_040,V10302_067","V10302",monthValueTab,"V10302_040,V10302_060_CHAR","V10302");
        averageHandle(yearValueTab,"V10301_avg", "V10301_avg", monthValueTab, 12);
        averageHandle(yearValueTab,"V10302_avg", "V10302_avg", monthValueTab, 12);
        // 平均海平面气压 ( 单位：百帕 )
        averageHandle(yearValueTab,"V10051_701", "V10051_701", monthValueTab, 12);
        // 平均气温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12001_701", "V12001_701", monthValueTab, 12);
        //月平均最高
        extremeHandle(yearValueTab, "maxMonthTemp","V12001_701",null,null, monthValueTab,"b");
        //月平均最低
        extremeHandle(yearValueTab, "minMonthTemp","V12001_701",null,null, monthValueTab,"b");

        // 气温年较差 ( 单位：摄氏度 )
        Float maxavgtemp=yearValueTab.getMaxMonthTemp();
        Float minavgtemp=yearValueTab.getMinMonthTemp();
        if(maxavgtemp!=null&&minavgtemp!=null) {
            yearValueTab.setV12306(maxavgtemp - minavgtemp);
        }
        // 年平均日最高气温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12011_701", "V12011_701", monthValueTab, 12);
        // 年平均日最低气温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12012_701", "V12012_701", monthValueTab, 12);
        // 极端最高气温 ( 单位：摄氏度 )
        extremeHandle(yearValueTab, "V12011","V12011",null,null, monthValueTab,"b");
        // 极端最高气温出现日数、 极端最高气温出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V12011_040,V12011_067","V12011",monthValueTab,"V12011_040,V12011_060_CHAR","V12011");
        // 极端最低气温 ( 单位：摄氏度 )
        extremeHandle(yearValueTab, "V12012","V12012",null,null, monthValueTab,"s");
        // 极端最低气温出现日数、极端最低气温出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V12012_040,V12012_067","V12012",monthValueTab,"V12012_040,V12012_060_CHAR","V12012");
        // 平均气温日较差 ( 单位：摄氏度 )
        if(yearValueTab.getV12011_701()!=null&&yearValueTab.getV12012_701()!=null) {
            yearValueTab.setV12303_701(yearValueTab.getV12011_701() - yearValueTab.getV12012_701());
        }
        // 最大气温日较差 ( 单位：摄氏度 )
        extremeHandle(yearValueTab, "V12304","V12304",null,null, monthValueTab,"b");
        // 最大气温日较差出现日数、最大气温日较差出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V12304_040,V12304_067","V12304",monthValueTab,"V12304_040,V12304_060_CHAR","V12304");
        // 最小气温日较差 ( 单位：摄氏度 )
        extremeHandle(yearValueTab, "V12305","V12305",null,null, monthValueTab,"s");
        // 最小气温日较差出现日数、最小气温日较差出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V12305_040,V12305_067","V12305",monthValueTab,"V12305_040,V12305_060_CHAR","V12305");
        // 日最高气温≥30℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12605_30",monthValueTab);
        // 日最高气温≥35℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12605_35",monthValueTab);
        // 日最高气温≥40℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12605_40",monthValueTab);
        // 日最低气温＜2℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12607_02",monthValueTab);
        // 日最低气温＜0℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12603",monthValueTab);
        // 日最低气温＜-2℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12606_02",monthValueTab);
        // 日最低气温＜-15℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12606_15",monthValueTab);
        // 日最低气温＜-5℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12606_05",monthValueTab);
        // 日最低气温＜-10℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12606_10",monthValueTab);
        //	日最低气温＜-30℃日数
        totalHandle(yearValueTab,"V12606_30",monthValueTab);
        //	日最低气温＜-40℃日数
        totalHandle(yearValueTab,"V12606_40",monthValueTab);
        // 冷度日数（日平均气温≥26.0℃） ( 单位：度日 )
        totalHandle(yearValueTab,"V12610_26",monthValueTab);
        // 暖度日数（日平均气温≤18.0℃） ( 单位：度日 )
        totalHandle(yearValueTab,"V12611_18",monthValueTab);
        // 平均水汽压 ( 单位：百帕 )
        averageHandle(yearValueTab,"V13004_701", "V13004_701", monthValueTab, 12);
        // 平均相对湿度 ( 单位：% )
        averageHandle(yearValueTab,"V13003_701", "V13003_701", monthValueTab, 12);
        // 最小相对湿度 ( 单位：% )
        extremeHandle(yearValueTab, "V13007","V13007",null,null, monthValueTab,"s");
        // 最小相对湿度出现日数、最小相对湿度出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V13007_040,V13007_067","V13007",monthValueTab,"V13007_040,V13007_060_CHAR","V13007");
        // 平均总云量 ( 单位：% )
        averageHandle(yearValueTab,"V20010_701", "V20010_701", monthValueTab, 12);
        // 平均低云量 ( 单位：% )
        averageHandle(yearValueTab,"V20051_701", "V20051_701", monthValueTab, 12);
        // 日平均总云量< 2.0日数 ( 单位：日 )
        totalHandle(yearValueTab,"V20501_02",monthValueTab);
        // 日平均总云量> 8.0日数 ( 单位：日 )
        totalHandle(yearValueTab,"V20500_08",monthValueTab);
        // 日平均低云量< 2.0日数 ( 单位：日 )
        totalHandle(yearValueTab,"V20503_02",monthValueTab);
        // 日平均低云量> 8.0日数 ( 单位：日 )
        totalHandle(yearValueTab,"V20502_08",monthValueTab);
        // 20-20时年降水量 ( 单位：毫米 )
        totalHandle(yearValueTab,"V13305",monthValueTab);
        // 08-08时年降水量 ( 单位：毫米 )
        totalHandle(yearValueTab,"V13306",monthValueTab);
        // 最大日降水量 ( 单位：毫米 )
        extremeHandle(yearValueTab, "V13052","V13052",null,null, monthValueTab,"b");
        // 最大日降水量出现日数、最大日降水量出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V13052_040,V13052_067","V13052",monthValueTab,"V13052_040,V13052_060_CHAR","V13052");
        // 日降水量≥0.1mm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13353",monthValueTab);
        // 日降水量≥1mm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13355_001",monthValueTab);
        // 日降水量≥5mm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13355_005",monthValueTab);
        // 日降水量≥10mm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13355_010",monthValueTab);
        // 日降水量≥25mm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13355_025",monthValueTab);
        // 日降水量≥50mm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13355_050",monthValueTab);
        // 日降水量≥100mm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13355_100",monthValueTab);
        // 日降水量≥150mm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13355_150",monthValueTab);
        // 日降水量≥250mm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13355_250",monthValueTab);
        // 1小时最大降水量 ( 单位：毫米 )
        extremeHandle(yearValueTab, "V13302_01","V13302_01",null,null, monthValueTab,"b");
        // 1小时最大降水量出现日数、1小时最大降水量出现月日、1小时最大降水量出现月、1小时最大降水量出现日
        statisticalDays(yearValueTab,"V13302_01_040,V13302_01_067,V13302_01_058,V13302_01_060","V13302_01",monthValueTab,"V13302_01_040,V13302_01_060_CHAR","V13302_01");
        // 冰雹日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_089",monthValueTab);
        // 轻雾日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_010",monthValueTab);
        // 露日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_001",monthValueTab);
        // 雨凇日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_056",monthValueTab);
        // 雾凇日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_048",monthValueTab);
        // 结冰日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_003",monthValueTab);
        // 扬沙日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_007",monthValueTab);
        // 浮尘日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_006",monthValueTab);
        // 霾日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_005",monthValueTab);
        // 龙卷风日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_008",monthValueTab);
        // 大风日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_015",monthValueTab);
        // 沙尘暴日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_031",monthValueTab);
        // 雾日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_042",monthValueTab);
        // 雷暴日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_017",monthValueTab);
        // 霜日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_002",monthValueTab);
        // 降雪日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_070",monthValueTab);
        // 积雪日数 ( 单位：日 )
        totalHandle(yearValueTab,"V04330_016",monthValueTab);
        // 电线积冰（雨凇+雾凇）日数 ( 单位：日 )
        totalHandle(yearValueTab,"V20305_540",monthValueTab);
        // 蒸发量(小型) ( 单位：毫米 )
        totalHandle(yearValueTab,"V13032",monthValueTab);
        // 蒸发量(大型) ( 单位：毫米 )
        totalHandle(yearValueTab,"V13033",monthValueTab);
        //年最大小型蒸发量、年最大小型蒸发量出现月份、年最大小型蒸发量出现日期
        extremeHandle(yearValueTab, "V13032_MAX","V13032_MAX","V13032_MAX_M,V13032_MAX_D","V04002,V13032_MAX_D", monthValueTab,"b");
        // 年最大大型蒸发量、年最大大型蒸发量出现月份、年最大大型蒸发量出现日期
        extremeHandle(yearValueTab, "V13033_MAX","V13033_MAX","V13033_MAX_M,V13033_MAX_D","V04002,V13033_MAX_D", monthValueTab,"b");
        // 最大积雪深度 ( 单位：厘米 )
        extremeHandle(yearValueTab, "V13334","V13334",null,null, monthValueTab,"b");
        // 最大积雪深度出现日数、最大积雪深度出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V13334_040,V13334_067","V13334",monthValueTab,"V13334_040,V13334_060_CHAR","V13334");
        // 最大雪压 ( 单位：克/平方厘米 )
        extremeHandle(yearValueTab, "V13330","V13330",null,null, monthValueTab,"b");
        // 最大雪压出现日数、最大雪压出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V13330_040,V13330_067","V13330",monthValueTab,"V13330_040,V13330_060","V13334");
        // 积雪深度≥1cm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13356_001",monthValueTab);
        // 积雪深度≥5cm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13356_005",monthValueTab);
        // 积雪深度≥10cm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13356_010",monthValueTab);
        // 积雪深度≥20cm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13356_020",monthValueTab);
        // 积雪深度≥30cm日数 ( 单位：日 )
        totalHandle(yearValueTab,"V13356_030",monthValueTab);
        // 电线积冰南北最大重量 ( 单位：克 )
        extremeHandle(yearValueTab, "V20440_NS","V20307_NS",null,null, monthValueTab,"b");
        // 电线积冰南北最大重量出现日数 ( 单位：日 )、电线积冰南北最大重量出现月日 ( 格式：mmdd )、电线积冰南北最大重量出现月、电线积冰南北最大重量出现日
        statisticalDays(yearValueTab,"V20440_040_NS,V20440_067_NS,V20440_058_NS,V20440_060_NS","V20440_NS",monthValueTab,"V20440_040_NS,V20440_060_NS_CHAR","V20307_NS");
        // 电线积冰南北最大重量的相应直径 ( 单位：毫米 )
        extremeHandle(yearValueTab, "V20441_NS","V20326_NS",null,null, monthValueTab,"b");
        // 电线积冰南北最大重量的相应厚度 ( 单位：毫米 )
        extremeHandle(yearValueTab, "V20442_NS","V20306_NS",null,null, monthValueTab,"b");
        // 电线积冰东西最大重量 ( 单位：克 ）
        extremeHandle(yearValueTab, "V20440_WE","V20307_NS",null,null, monthValueTab,"b");
        // 电线积冰东西最大重量出现日数 ( 单位：日 )、电线积冰东西最大重量出现月日 ( 格式：mmdd )、电线积冰东西最大重量出现月、电线积冰东西最大重量出现日
        statisticalDays(yearValueTab,"V20440_040_WE,V20440_067_WE,V20440_058_WE,V20440_060_WE","V20440_WE",monthValueTab,"V20440_040_WE,V20440_060_WE_CHAR","V20307_NS");
        // 电线积冰东西最大重量的相应直径 ( 单位：毫米 )
        extremeHandle(yearValueTab, "V20441_WE","V20326_WE",null,null, monthValueTab,"b");
        // 电线积冰东西最大重量的相应厚度 ( 单位：毫米 )
        extremeHandle(yearValueTab, "V20442_WE","V20306_WE",null,null, monthValueTab,"b");
        // 平均风速（2分钟） ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11291_701", "V11291_701", monthValueTab, 12);
        // 最大风速 ( 单位：米/秒 )、最大风速的风向 ( 代码表 )
        extremeHandle(yearValueTab, "V11042","V11042","V11296_CHAR","V11296_CHAR", monthValueTab,"b");
        // 最大风速之出现日数、最大风速之出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V11042_040,V11042_067","V11042",monthValueTab,"V11042_040,V11042_060_CHAR","V11042");
        // 日最大风速≥5.0m/s日数 ( 单位：日 )
        compareDaysHandle(yearValueTab, "V11042_05","V11042_05",monthValueTab,5,"b");
        // 日最大风速≥10.0m/s日数 ( 单位：日 )
        compareDaysHandle(yearValueTab, "V11042_10","V11042_10",monthValueTab,10,"b");
        // 日最大风速≥12.0m/s日数 ( 单位：日 )
        compareDaysHandle(yearValueTab, "V11042_12","V11042_12",monthValueTab,12,"b");
        // 日最大风速≥15.0m/s日数 ( 单位：日 )
        compareDaysHandle(yearValueTab, "V11042_15","V11042_15",monthValueTab,15,"b");
        // 日最大风速≥17.0m/s日数 ( 单位：日 )
        compareDaysHandle(yearValueTab, "V11042_17","V11042_17",monthValueTab,17,"b");
        //年平均湿球温度
        averageHandle(yearValueTab,"balltemp_avg", "balltemp_avg", monthValueTab, 12);
        //年最大水汽压、年最大水汽压出现月份、 年最大水汽压出现日期
        extremeHandle(yearValueTab, "V13004_MAX","V13004_MAX","V13004_MAX_M,V13004_MAX_D","V04002,V13004_MAX_D", monthValueTab,"b");
        //年最小水气压、年最小水汽压出现月份、年最小水汽压出现日期
        extremeHandle(yearValueTab, "V13004_MIN","V13004_MIN","V13004_MIN_M,V13004_MIN_D","V04002,V13004_MIN_D", monthValueTab,"s");
        // 极大风速 ( 单位：米/秒 )、极大风速之风向 ( 代码表 )
        extremeHandle(yearValueTab, "V11046","V11046","V11211_CHAR","V11211", monthValueTab,"b");
        // 极大风速之出现日数、极大风速之出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V11046_040,V11046_067_CHAR","V11046",monthValueTab,"V11046_040,V11046_060_CHAR","V11046");
        // NNE风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_NNE", "V11351_NNE", monthValueTab, 12);
        // NE风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_NE", "V11351_NE", monthValueTab, 12);
        // ENE的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_ENE", "V11351_ENE", monthValueTab, 12);
        // E风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_E", "V11351_E", monthValueTab, 12);
        // ESE风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_ESE", "V11351_ESE", monthValueTab, 12);
        // SE风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_SE", "V11351_SE", monthValueTab, 12);
        // SSE风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_SSE", "V11351_SSE", monthValueTab, 12);
        // S风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_S", "V11351_S", monthValueTab, 12);
        // SSW风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_SSW", "V11351_SSW", monthValueTab, 12);
        // SW风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_SW", "V11351_SW", monthValueTab, 12);
        // WSW风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_WSW", "V11351_WSW", monthValueTab, 12);
        // W风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_W", "V11351_W", monthValueTab, 12);
        // WNW风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_WNW", "V11351_WNW", monthValueTab, 12);
        // NW风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_NW", "V11351_NW", monthValueTab, 12);
        // NNW风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_NNW", "V11351_NNW", monthValueTab, 12);
        // N风的平均风速 ( 单位：米/秒 )
        averageHandle(yearValueTab,"V11351_N", "V11351_N", monthValueTab, 12);
        // NNE风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_NNE","V11042_NNE",null,null, monthValueTab,"b");
        // NE风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_NE","V11042_NE",null,null, monthValueTab,"b");
        // ENE的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_ENE","V11042_ENE",null,null, monthValueTab,"b");
        // E风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_E","V11042_E",null,null, monthValueTab,"b");
        // ESE风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_ESE","V11042_ESE",null,null, monthValueTab,"b");
        // SE风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_SE","V11042_SE",null,null, monthValueTab,"b");
        // SSE风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_SSE","V11042_SSE",null,null, monthValueTab,"b");
        // S风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_S","V11042_S",null,null, monthValueTab,"b");
        // SSW风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_SSW","V11042_SSW",null,null, monthValueTab,"b");
        // SW风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_SW","V11042_SW",null,null, monthValueTab,"b");
        // WSW风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_WSW","V11042_WSW",null,null, monthValueTab,"b");
        // W风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_W","V11042_W",null,null, monthValueTab,"b");
        // WNW风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_WNW","V11042_WNW",null,null, monthValueTab,"b");
        // NW风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_NW","V11042_NW",null,null, monthValueTab,"b");
        // NNW风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_NNW","V11042_NNW",null,null, monthValueTab,"b");
        // N风的最大风速 ( 单位：米/秒 )
        extremeHandle(yearValueTab, "V11042_N","V11042_N",null,null, monthValueTab,"b");
        // 平均地面温度 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12120_701", "V12120_701", monthValueTab, 12);
        // 平均最高地面温度 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12311_701", "V12311_701", monthValueTab, 12);
        // 平均最低地面温度 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12121_701", "V12121_701", monthValueTab, 12);
        // 日最低地面温度≤0.0℃日数 ( 单位：日 )
        totalHandle(yearValueTab,"V12620",monthValueTab);
        // 极端最高地面温度 ( 单位：摄氏度 )
        extremeHandle(yearValueTab, "V12311","V12311",null,null, monthValueTab,"b");
        // 极端最高地面温度出现日数、极端最高地面温度出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V12311_040,V12311_067","V12311",monthValueTab,"V12311_040,V12311_060_CHAR","V12311");
        // 极端最低地面温度 ( 单位：摄氏度 )
        extremeHandle(yearValueTab, "V12121","V12121",null,null, monthValueTab,"s");
        // 极端最低地面温度出现日数、极端最低地面温度出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V12121_040,V12121_067","V12121",monthValueTab,"V12121_040,V12121_060_CHAR","V12121");
        // 平均5cm地温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12030_701_005", "V12030_701_005", monthValueTab, 12);
        // 平均10cm地温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12030_701_010", "V12030_701_010", monthValueTab, 12);
        // 平均15cm地温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12030_701_015", "V12030_701_015", monthValueTab, 12);
        // 平均20cm地温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12030_701_020", "V12030_701_020", monthValueTab, 12);
        // 平均40cm地温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12030_701_040", "V12030_701_040", monthValueTab, 12);
        // 平均80cm地温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12030_701_080", "V12030_701_080", monthValueTab, 12);
        // 平均160cm地温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12030_701_160", "V12030_701_160", monthValueTab, 12);
        // 平均320cm地温 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12030_701_320", "V12030_701_320", monthValueTab, 12);
        // 平均草面（雪面）温度 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12314_701", "V12314_701", monthValueTab, 12);
        // 平均最高草面（雪面）温度 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12315_701", "V12315_701", monthValueTab, 12);
        // 平均最低草面（雪面）温度 ( 单位：摄氏度 )
        averageHandle(yearValueTab,"V12316_701", "V12316_701", monthValueTab, 12);
        // 极端最高草面（雪面）气温 ( 单位：摄氏度 )
        extremeHandle(yearValueTab, "V12315","V12315",null,null, monthValueTab,"b");
        // 极端最低草面（雪面）气温 ( 单位：摄氏度 )
        extremeHandle(yearValueTab, "V12316","V12316",null,null, monthValueTab,"b");
        // 最大冻土深度 ( 单位：厘米 )
        extremeHandle(yearValueTab, "V20334","V20334",null,null, monthValueTab,"b");
        // 最大冻土深度出现日数、最大冻土深度出现月日 ( 字符 )
        statisticalDays(yearValueTab,"V20334_040,V20334_067","V20334",monthValueTab,"V20334_040,V20334_060_CHAR","V20334");
        // 日照总时数 ( 单位：小时 )
        totalHandle(yearValueTab,"V14032",monthValueTab);
        //日出总时长
        totalHandle(yearValueTab,"sunTime",monthValueTab);
        // 日照百分率 ( 单位：% )
        if(yearValueTab.getV14032()!=null&&yearValueTab.getSunTime()!=null&&yearValueTab.getSunTime()!=0) {
            yearValueTab.setV14033(Integer.parseInt(yearValueTab.getV14032()/ yearValueTab.getSunTime()+""));
        }
        // 日照百分率≥60%日数 ( 单位：日 )
        totalHandle(yearValueTab,"V20302_171",monthValueTab);
        // 日照百分率≤20%日数 ( 单位：日 )
        totalHandle(yearValueTab,"V20302_172",monthValueTab);
        /**
         * 年值与日值有关统计
         */
        List<DayValueTab> dayList=yearInfoHandle.getLastYearDayValue(stationNum,yearValueTab.getV04001());
        packYearValueByHour(yearValueTab,dayList);
        /**
         * 年值与小时值有关统计
         */
        List<HourValueTab> hourList=yearInfoHandle.getLastYearHourValue(stationNum, yearValueTab.getV04001());
        yearFFrequencyHandle(yearValueTab,hourList);
        /**
         * 年值与辐射有关统计
         */
        List<RadiDigChnMulTab> radiList=yearInfoHandle.getLastYearRadiValue(stationNum, yearValueTab.getV04001());
        yearRadiHandle(yearValueTab,radiList);
    }
    list.add(yearValueTab);
    return list;
}

    /**
     * 年值与辐射有关统计
     * @param yearValueTab
     * @param radiList
     */
    private void yearRadiHandle(YearValueTab yearValueTab,List<RadiDigChnMulTab> radiList){
        for(RadiDigChnMulTab radiDigChnMulTab:radiList){
            //总辐射辐照度
            totalHandle(yearValueTab, "v14311", radiDigChnMulTab);
            //净辐射辐照度
            totalHandle(yearValueTab, "v14312", radiDigChnMulTab);
            //日总辐射辐照度最大值、 日总辐射辐照度最大出现月、日总辐射辐照度最大出现日、日总辐射辐照度最大出现时
            extremeHandle(yearValueTab, "v14311_05", "v14311_05","v14311_05_04_002,v14311_05_04_003,v14311_05_04_004","v04002,v04003,v04004",radiDigChnMulTab,"b");
            //日净辐射辐照度最大值、日总辐射辐照度最大出现月、日总辐射辐照度最大出现日、日总辐射辐照度最大出现时
            extremeHandle(yearValueTab, "v14312_05", "v14312_05","v14312_05_04_002,v14312_05_04_003,v14312_05_04_004","v04002,v04003,v04004",radiDigChnMulTab,"b");
        }
    }
    /**
     * 风频统计
     * @param yearValueTab
     * @param hourList
     */
    private void yearFFrequencyHandle(YearValueTab yearValueTab,List<HourValueTab> hourList){

        //	NNE风向出现频率
        frequencyHandle(yearValueTab,"NNE","V11350_NNE",hourList,"f");
        maxOriMaxHandle(yearValueTab,"NNE","V11350_NNE","f");
        //	NE风向出现频率
        frequencyHandle(yearValueTab,"NE","V11350_NE",hourList,"f");
        maxOriMaxHandle(yearValueTab,"NE","V11350_NE","f");
        //	ENE风向出现频率
        frequencyHandle(yearValueTab,"ENE","V11350_ENE",hourList,"f");
        maxOriMaxHandle(yearValueTab,"ENE","V11350_ENE","f");
        //	E风向出现频率
        frequencyHandle(yearValueTab,"E","V11350_E",hourList,"f");
        maxOriMaxHandle(yearValueTab,"E","V11350_E","f");
        //	ESE风向出现频率
        frequencyHandle(yearValueTab,"ESE","V11350_ESE",hourList,"f");
        maxOriMaxHandle(yearValueTab,"ESE","V11350_ESE","f");
        //	SE风向出现频率
        frequencyHandle(yearValueTab,"SE","V11350_SE",hourList,"f");
        maxOriMaxHandle(yearValueTab,"","V11350_SE","f");
        //	SSE风向出现频率
        frequencyHandle(yearValueTab,"SSE","V11350_SSE",hourList,"f");
        maxOriMaxHandle(yearValueTab,"SSE","V11350_SSE","f");
        //	S风向出现频率
        frequencyHandle(yearValueTab,"S","V11350_S",hourList,"f");
        maxOriMaxHandle(yearValueTab,"S","V11350_S","f");
        //	SSW风向出现频率
        frequencyHandle(yearValueTab,"SSW","V11350_SSW",hourList,"f");
        maxOriMaxHandle(yearValueTab,"SSW","V11350_SSW","f");
        //	SW风向出现频率
        frequencyHandle(yearValueTab,"SW","V11350_SW",hourList,"f");
        maxOriMaxHandle(yearValueTab,"SW","V11350_SW","f");
        //	WSW风向出现频率
        frequencyHandle(yearValueTab,"WSW","V11350_WSW",hourList,"f");
        maxOriMaxHandle(yearValueTab,"WSW","V11350_WSW","f");
        //	W风向出现频率
        frequencyHandle(yearValueTab,"W","V11350_W",hourList,"f");
        maxOriMaxHandle(yearValueTab,"W","V11350_W","f");
        //	WNW风向出现频率
        frequencyHandle(yearValueTab,"WNW","V11350_WNW",hourList,"f");
        maxOriMaxHandle(yearValueTab,"WNW","V11350_WNW","f");
        //	NW风向出现频率
        frequencyHandle(yearValueTab,"NW","V11350_NW",hourList,"f");
        maxOriMaxHandle(yearValueTab,"NW","V11350_NW","f");
        //	NNW风向出现频率
        frequencyHandle(yearValueTab,"NNW","V11350_NNW",hourList,"f");
        maxOriMaxHandle(yearValueTab,"NNW","V11350_NNW","f");
        //	N风向出现频率
        frequencyHandle(yearValueTab,"N","V11350_N",hourList,"f");
        maxOriMaxHandle(yearValueTab,"N","V11350_N","f");
        //	C风向（静风）出现频率
        frequencyHandle(yearValueTab,"C","V11350_C",hourList,"f");
        maxOriMaxHandle(yearValueTab,"C","V11350_C","f");

    }
    /**
     * 频率统计
     * @param monthValueTab
     * @param hourList
     * @param type
     */
    private void frequencyHandle(Object monthValueTab,String fType,String mfield,List<HourValueTab> hourList,String type){
        int count=0;
        for(HourValueTab hourValueTab:hourList){
            String value=null;
            if("f".equals(type)) {
                value=hourValueTab.getV11290();
            }
            if(value!=null){
                count++;
            }
        }
        try {
            int countParam=0;
            for (HourValueTab hourValueTab : hourList) {
                String value=null;
                if ("f".equals(type)) {//风频统计
                    value = hourValueTab.getV11290();
                    if (value!=null&&fType.equals(value)) {
                        countParam++;
                    }
                }

            }
            if(count!=0) {
                ReflectUtil.setValue(monthValueTab, mfield, countParam / count);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 年值中与日值有关统计
     * @param yearValueTab
     */
    private void packYearValueByHour(YearValueTab yearValueTab,List<DayValueTab> dayList){
         OldYearValueTab oldYearValueTab=new OldYearValueTab();
        OldYearValueTab lastYearValueTab=new OldYearValueTab();
        boolean isend=false;
        int count=0;
        for(DayValueTab dayValueTab:dayList){
            if(count==dayList.size()-1){
                isend=true;
            }
            // 日最高气温≥30.0℃最长连续日数 ( 单位：日 )、日最高气温≥30.0℃最长连续日数之止月 、日最高气温≥30.0℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12710_30,V12712_30,V12711_30","V12011",dayValueTab, (float) 30.0,oldYearValueTab,lastYearValueTab,"b",isend,false);
            // 日最高气温≥35.0℃最长连续日数 ( 单位：日 )、日最高气温≥35.0℃最长连续日数之止月 、日最高气温≥35.0℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12710_35,V12712_35,V12711_35","V12011",dayValueTab, (float) 35.0,oldYearValueTab,lastYearValueTab,"b",isend,false);
            // 日最高气温≥40.0℃最长连续日数 ( 单位：日 )、日最高气温≥40.0℃最长连续日数之止月 、日最高气温≥40.0℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12710_30,V12712_30,V12711_30","V12011",dayValueTab, (float) 40.0,oldYearValueTab,lastYearValueTab,"b",isend,false);
            // 日最低气温<2℃最长连续日数 ( 单位：日 )、日最低气温<2℃最长连续日数之止月 、日最低气温<2℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12713_02,V12714_02,V12715_02","V12012",dayValueTab, (float) 2.0,oldYearValueTab,lastYearValueTab,"s",isend,false);
            // 日最低气温<0℃最长连续日数 ( 单位：日 )、日最低气温<0℃最长连续日数之止月 、日最低气温<2℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12713_02,V12714_02,V12715_02","V12012",dayValueTab, (float) 0.0,oldYearValueTab,lastYearValueTab,"s",isend,false);
            // 日最低气温<-2℃最长连续日数 ( 单位：日 )、日最低气温<-2℃最长连续日数之止月 、日最低气温<-2℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12716_02,V12718_02,V12717_02","V12012",dayValueTab, (float) -2.0,oldYearValueTab,lastYearValueTab,"s",isend,false);
            // 日最低气温＜-5℃最长连续日数 ( 单位：日 )、 日最低气温＜-5℃最长连续日数之止月、日最低气温＜-5℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12716_05,V12718_05,V12717_05","V12012",dayValueTab, (float) -15.0,oldYearValueTab,lastYearValueTab,"s",isend,false);
            // 日最低气温＜-10℃最长连续日数 ( 单位：日 )、日最低气温＜-10℃最长连续日数之止月、日最低气温＜-10℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12716_10,V12718_10,V12717_10,","V12012",dayValueTab, (float) -15.0,oldYearValueTab,lastYearValueTab,"s",isend,false);
            // 日最低气温＜-15℃最长连续日数 ( 单位：日 )、 日最低气温＜-15℃最长连续日数之止月、日最低气温＜-15℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12716_15,V12718_15,V12717_15","V12012",dayValueTab, (float) -15.0,oldYearValueTab,lastYearValueTab,"s",isend,false);
            // 日最低气温＜-30℃最长连续日数 ( 单位：日 )、 日最低气温＜-30℃最长连续日数之止月、日最低气温＜-30℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12716_30,V12718_30,V12717_30","V12012",dayValueTab, (float) -30.0,oldYearValueTab,lastYearValueTab,"s",isend,false);
            // 日最低气温＜-40℃最长连续日数 ( 单位：日 )、 日最低气温＜-40℃最长连续日数之止月、日最低气温＜-40℃最长连续日数之止日
            maxContinuousHandle(yearValueTab,"V12716_40,V12718_40,V12717_40","V12012",dayValueTab, (float) -30.0,oldYearValueTab,lastYearValueTab,"s",isend,false);
            // 最长连续降水日数 ( 单位：日 )、最长连续降水止月、最长连续降水止日、最长连续降水量 ( 单位：毫米 )
            maxContinuousHandle(yearValueTab,"V20430,V20438,V20431,V13380","V13305",dayValueTab, (float) 0.1,oldYearValueTab,lastYearValueTab,"b",isend,false);
            // 最大连续降水日数 ( 单位：日 )、最大连续降水止月、最大连续降水止日、最大连续降水量 ( 单位：毫米 )
            maxContinuousHandle(yearValueTab,"V20434,V20436,V20435,V13381","V13305",dayValueTab, (float) 0.1,oldYearValueTab,lastYearValueTab,"b",isend,true);
            // 日平均气温稳定通过0.0℃日数、日平均气温稳定通过0.0℃起始月、日平均气温稳定通过0.0℃起始日、 日平均气温稳定通过0.0℃止月、 日平均气温稳定通过0.0℃止日
            continuousHandle(yearValueTab,"V12705_0,V12705_00,V12703_00,V12706_00,V12704_00","V12001_701",dayValueTab, (float) 0.0,oldYearValueTab,lastYearValueTab,"b",isend);
            // 日平均气温稳定通过5.0℃日数、 日平均气温稳定通过5.0℃起始月、日平均气温稳定通过5.0℃起始日、日平均气温稳定通过5.0℃止月、日平均气温稳定通过5.0℃止日
            continuousHandle(yearValueTab,"V12705_5,V12705_05,V12703_05,V12706_05,V12704_05","V12001_701",dayValueTab, (float) 5.0,oldYearValueTab,lastYearValueTab,"b",isend);
            // 日平均气温稳定通过10.0℃日数、日平均气温稳定通过10.0℃起始月、 日平均气温稳定通过10.0℃起始日、日平均气温稳定通过10.0℃止月、日平均气温稳定通过10.0℃止日
            continuousHandle(yearValueTab,"V12705_10C,V12705_10,V12703_10,V12706_10,V12704_10","V12001_701",dayValueTab, (float) 10.0,oldYearValueTab,lastYearValueTab,"b",isend);
            // 日平均气温稳定通过15.0℃日数、日平均气温稳定通过15.0℃起始月、日平均气温稳定通过15.0℃起始日、日平均气温稳定通过15.0℃止月、日平均气温稳定通过15.0℃止日
            continuousHandle(yearValueTab,"V12705_15C,V12705_15,V12703_15,V12706_15,V12704_15","V12001_701",dayValueTab, (float) 15.0,oldYearValueTab,lastYearValueTab,"b",isend);
            // 日平均气温稳定通过20.0℃日数、日平均气温稳定通过20.0℃起始月、日平均气温稳定通过20.0℃起始日、日平均气温稳定通过20.0℃止月、日平均气温稳定通过20.0℃止日
            continuousHandle(yearValueTab,"V12705_20C,V12705_20,V12703_20,V12706_20,V12704_20","V12001_701",dayValueTab, (float) 20.0,oldYearValueTab,lastYearValueTab,"b",isend);
            // 日平均气温稳定通过0.0℃日数、日平均气温稳定通过22.0℃起始月、日平均气温稳定通过22.0℃起始日、 日平均气温稳定通过22.0℃止月、日平均气温稳定通过22.0℃止日
            continuousHandle(yearValueTab,"V12705_22C,V12705_22,V12703_22,V12706_22,V12704_22","V12001_701",dayValueTab, (float) 22.0,oldYearValueTab,lastYearValueTab,"b",isend);

        }

    }

    /**
     * 稳定通过统计
     * @param yearValueTab-年值对象
     * @param yField-统计参数 ps:日数、连续起始月、连续起始日、连续之止月、连续之止日
     * @param dField-日值参数
     * @param dayValueTab-小时值对象
     * @param compareField-比较值
     * @param oldYearValueTab-上次统计的最大连续日数
     * @param lastYearValueTab-前一个统计值
     * @param comparaType-比较类型 s-小时值小于等于比较值，b-小时值大于等于比较值
     * @param isend ---最后一个比较
     */
    private void continuousHandle(Object yearValueTab,String yField,String dField,Object dayValueTab,float compareField,Object oldYearValueTab,Object lastYearValueTab,String comparaType,boolean isend){
        try {
            Object dvalue1=ReflectUtil.getValue(dayValueTab, dField);
            if(dvalue1!=null){
                float dValue=Float.parseFloat(dvalue1+"");
                //是否统计标识
                boolean flag=true;
                //是否连续
                boolean isContinue=false;

                String[] y=yField.split(",");
                Object yValue=null;
                if("b".equals(comparaType)) {
                    if (dValue >= compareField) {
                        flag = true;
                    }
                }else if("s".equals(comparaType)){
                    if(dValue<=compareField){
                        flag=true;
                    }
                }
                if(flag){
                    yValue=ReflectUtil.getValue(yearValueTab, y[0]);
                    if(yValue!=null) {
                        Integer yvalue1 = Integer.parseInt(yValue + "");
                        Object lastValue=ReflectUtil.getValue(lastYearValueTab, y[0]);
                        Integer lastYvalue=Integer.parseInt(lastValue+"");
                        if(yvalue1-lastYvalue==1){//连续
                            isContinue=true;
                        }
                        //获取止月
                        Object m = ReflectUtil.getValue(dayValueTab, "V04002");
                        //获取止日
                        Object d = ReflectUtil.getValue(dayValueTab, "V04003");
                        if(isContinue) {
                            yvalue1++;
                            ReflectUtil.setValue(yearValueTab, y[0], yvalue1);
                            ReflectUtil.setValue(lastYearValueTab, y[0], yvalue1-1);
                            if (m != null) {
                                ReflectUtil.setValue(yearValueTab, y[3], m);
                                ReflectUtil.setValue(lastYearValueTab, y[3], m);
                            }
                            if (d != null) {
                                ReflectUtil.setValue(yearValueTab, y[4], d);
                                ReflectUtil.setValue(lastYearValueTab, y[4], d);
                            }
                        }else{//不连续
                            //储存上次最大值
                            Object oldValue=ReflectUtil.getValue(oldYearValueTab, y[0]);
                            Integer oldValue1=Integer.parseInt(oldValue+"");
                            //上次起始月
                            Object lastSm=ReflectUtil.getValue(lastYearValueTab, y[1]);
                            //上次起始日
                            Object lastSd=ReflectUtil.getValue(lastYearValueTab, y[2]);
                            //上次止月
                            Object lastEm=ReflectUtil.getValue(lastYearValueTab, y[3]);
                            //上次止日
                            Object lastEd=ReflectUtil.getValue(lastYearValueTab, y[4]);
                            //统计最长连续标识
                            String type=null;
                            if(yvalue1>oldValue1) {//以日数判断最长统计
                                type = "1";
                            }
                            if("1".equals(type)) {
                                ReflectUtil.setValue(oldYearValueTab, y[0], yvalue1);
                                ReflectUtil.setValue(oldYearValueTab, y[1],lastSm);
                                ReflectUtil.setValue(oldYearValueTab, y[1],lastSd);
                                ReflectUtil.setValue(oldYearValueTab, y[3],lastEm);
                                ReflectUtil.setValue(oldYearValueTab, y[4], lastEd);
                            }
                            /**
                             * 新连续计数
                             */
                            ReflectUtil.setValue(yearValueTab, y[0], 1);
                            ReflectUtil.setValue(lastYearValueTab, y[0], 0);
                            ReflectUtil.setValue(oldYearValueTab,y[0], 1);
                            /**
                             * 设置起始和截止月日
                             */
                            ReflectUtil.setValue(lastYearValueTab, y[1], m);
                            ReflectUtil.setValue(lastYearValueTab, y[2], d);
                            ReflectUtil.setValue(lastYearValueTab, y[3], m);
                            ReflectUtil.setValue(lastYearValueTab, y[4], d);
                            ReflectUtil.setValue(yearValueTab, y[1],m);
                            ReflectUtil.setValue(yearValueTab, y[2], d);
                            ReflectUtil.setValue(yearValueTab, y[3], m);
                            ReflectUtil.setValue(yearValueTab, y[4], d);
                        }
                    }else{
                        ReflectUtil.setValue(yearValueTab, y[0], 1);
                        //上次最大连续日初始化0
                        ReflectUtil.setValue(lastYearValueTab, y[0], 0);
                        //获取月
                        Object m=ReflectUtil.getValue(dayValueTab, "V04002");
                        if(m!=null){
                            ReflectUtil.setValue(yearValueTab, y[1],m);
                            ReflectUtil.setValue(lastYearValueTab, y[1], m);
                            ReflectUtil.setValue(yearValueTab, y[3],m);
                            ReflectUtil.setValue(lastYearValueTab, y[3], m);
                        }
                        Object d=ReflectUtil.getValue(dayValueTab, "V04003");
                        if(d!=null){
                            ReflectUtil.setValue(yearValueTab, y[2],d);
                            ReflectUtil.setValue(lastYearValueTab, y[2], d);
                            ReflectUtil.setValue(yearValueTab, y[4],d);
                            ReflectUtil.setValue(lastYearValueTab, y[4], d);
                        }
                    }
                }else {
                    Object lastValue = ReflectUtil.getValue(lastYearValueTab, y[0]);
                    Integer lastYvalue = Integer.parseInt(lastValue + "");
                    lastYvalue++;
                    ReflectUtil.setValue(lastYearValueTab, y[0], lastYvalue);
                }
                if(isend){//最后一条记录
                    yValue=ReflectUtil.getValue(oldYearValueTab, y[0]);
                    Object smvalue=ReflectUtil.getValue(oldYearValueTab, y[1]);
                    Object sdvalue=ReflectUtil.getValue(oldYearValueTab, y[2]);
                    Object emvalue=ReflectUtil.getValue(oldYearValueTab, y[3]);
                    Object edvalue=ReflectUtil.getValue(oldYearValueTab, y[4]);
                    ReflectUtil.setValue(yearValueTab, y[0], yValue);
                    ReflectUtil.setValue(yearValueTab, y[1], smvalue);
                    ReflectUtil.setValue(yearValueTab, y[2], sdvalue);
                    ReflectUtil.setValue(yearValueTab, y[3], emvalue);
                    ReflectUtil.setValue(yearValueTab, y[4], edvalue);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
}
        /**
         * 最长连续值统计
         * @param yearValueTab-年值对象
         * @param yField-统计参数 ps:连续日数、连续日数之止月、连续日数之止日
         * @param dField-日值参数
         * @param dayValueTab-小时值对象
         * @param compareField-比较值
         * @param oldYearValueTab-上次统计的最大连续日数
         * @param lastYearValueTab-前一个统计值
         * @param comparaType-比较类型 s-小时值小于等于比较值，b-小时值大于等于比较值
         * @param isend ---最后一个比较
         * @param ismax --最大值连续统计标识
         */
    private void maxContinuousHandle(Object yearValueTab,String yField,String dField,Object dayValueTab,float compareField,Object oldYearValueTab,Object lastYearValueTab,String comparaType,boolean isend,boolean ismax){
      try {
          Object dvalue1=ReflectUtil.getValue(dayValueTab, dField);
          if(dvalue1!=null){
              float dValue=Float.parseFloat(dvalue1+"");
              //是否统计标识
              boolean flag=true;
              //是否连续
              boolean isContinue=false;

              String[] y=yField.split(",");
              Object yValue=null;
              if("b".equals(comparaType)) {
                  if (dValue >= compareField) {
                      flag = true;
                  }
              }else if("s".equals(comparaType)){
                  if(dValue<=compareField){
                      flag=true;
                  }
              }
              if(flag){
                  yValue=ReflectUtil.getValue(yearValueTab, y[0]);
                      if(yValue!=null) {
                          Integer yvalue1 = Integer.parseInt(yValue + "");
                          Object lastValue=ReflectUtil.getValue(lastYearValueTab, y[0]);
                          Integer lastYvalue=Integer.parseInt(lastValue+"");
                          if(yvalue1-lastYvalue==1){//连续
                              isContinue=true;
                          }
                          //获取月
                          Object m = ReflectUtil.getValue(dayValueTab, "V04002");
                          Object d = ReflectUtil.getValue(dayValueTab, "V04003");
                          if(isContinue) {
                              yvalue1++;
                              ReflectUtil.setValue(yearValueTab, y[0], yvalue1);
                              ReflectUtil.setValue(lastYearValueTab, y[0], yvalue1-1);
                              if (m != null) {
                                  ReflectUtil.setValue(yearValueTab, y[1], m);
                              }
                              if (d != null) {
                                  ReflectUtil.setValue(yearValueTab, y[2], d);
                              }
                              if(yField.length()==4){//连续总值
                                  Object oldda=ReflectUtil.getValue(oldYearValueTab, y[3]);
                                  Object da=ReflectUtil.getValue(yearValueTab, y[3]);
                                  if(oldda==null) {
                                      ReflectUtil.setValue(oldYearValueTab, y[3], dValue);
                                      ReflectUtil.setValue(yearValueTab, y[3], dValue);
                                  }else{
                                      Integer oldvalu=Integer.parseInt(oldda+"");
                                      Integer davalu=Integer.parseInt(da+"");
                                      ReflectUtil.setValue(oldYearValueTab, y[3],oldvalu+dValue);
                                      ReflectUtil.setValue(yearValueTab, y[3],davalu+dValue);
                                  }
                              }
                          }else{//不连续
                              //储存上次最大值
                              Object oldValue=ReflectUtil.getValue(oldYearValueTab, y[0]);
                              Integer oldValue1=Integer.parseInt(oldValue+"");
                              //统计最长连续标识
                              String type=null;
                              if(ismax){//以连续最大量统计
                                  if(yField.length()==4){
                                     Object old= ReflectUtil.getValue(oldYearValueTab, y[3]);
                                     Object mva=ReflectUtil.getValue(yearValueTab, y[3]);
                                     Integer mva1=0;
                                     Integer old1=0;
                                     if(mva!=null){
                                         mva1=Integer.parseInt(mva+"");
                                     }
                                     if(old!=null){
                                         old1= Integer.parseInt(old+"");
                                     }
                                     if(mva1>old1){
                                         type="3";
                                     }
                                  }
                              }else{
                                  if(yvalue1>oldValue1){//以日数判断最长统计
                                      type="1";
                                  }else if(yvalue1==oldValue1){
                                      type="2";
                                  }
                              }
                              //上次止月
                              Object lastEm=ReflectUtil.getValue(lastYearValueTab, y[1]);
                              //上次止日
                              Object lastEd=ReflectUtil.getValue(lastYearValueTab, y[2]);
                              if("1".equals(type)||"3".equals(type)) {
                                 ReflectUtil.setValue(oldYearValueTab, y[0], yvalue1);
                                 ReflectUtil.setValue(oldYearValueTab, y[1], lastEm);
                                 ReflectUtil.setValue(oldYearValueTab, y[2], lastEd);
                                 if(yField.length()==4){//连续总值
                                     ReflectUtil.setValue(oldYearValueTab, y[3], dValue);
                                 }
                              }else if("2".equals(type)){
                                  ReflectUtil.setValue(oldYearValueTab, y[1], null);
                                  ReflectUtil.setValue(oldYearValueTab, y[2], null);
                              }
                              /**
                                * 新连续计数
                                */
                              ReflectUtil.setValue(yearValueTab, y[0], 1);
                              ReflectUtil.setValue(lastYearValueTab, y[0], 0);

                          }
                      }else{
                          ReflectUtil.setValue(yearValueTab, y[0], 1);
                          //上次最大连续日初始化0
                          ReflectUtil.setValue(lastYearValueTab, y[0], 0);
                          //获取月
                          Object m=ReflectUtil.getValue(dayValueTab, "V04002");
                          if(m!=null){
                              ReflectUtil.setValue(yearValueTab, y[1],m);
                          }
                          Object d=ReflectUtil.getValue(dayValueTab, "V04003");
                          if(d!=null){
                              ReflectUtil.setValue(yearValueTab, y[2],d);
                          }
                          if(yField.length()==4){//连续总值
                              ReflectUtil.setValue(oldYearValueTab, y[3], dValue);
                          }
                      }

                  }else {
                    Object lastValue = ReflectUtil.getValue(lastYearValueTab, y[0]);
                    Integer lastYvalue = Integer.parseInt(lastValue + "");
                    lastYvalue++;
                    ReflectUtil.setValue(lastYearValueTab, y[0], lastYvalue);
                  }
                 if(isend){//最后一条记录
                    yValue=ReflectUtil.getValue(oldYearValueTab, y[0]);
                    Object mvalue=ReflectUtil.getValue(oldYearValueTab, y[1]);
                    Object dvalue=ReflectUtil.getValue(oldYearValueTab, y[2]);
                    ReflectUtil.setValue(yearValueTab, y[0], yValue);
                    ReflectUtil.setValue(yearValueTab, y[1], mvalue);
                    ReflectUtil.setValue(yearValueTab, y[2], dvalue);
                     if(yField.length()==4){//连续总值
                         Object maxva=ReflectUtil.getValue(oldYearValueTab, y[3]);
                         Integer maxva1=Integer.parseInt(maxva+"");
                         ReflectUtil.setValue(yearValueTab, y[3], maxva1);
                     }
                }
              }
          } catch (Exception ex) {
                 ex.printStackTrace();
            }
    }
    /**
     * 最多、次多参数统计
     * @param monthValueTab
     * @param fType
     * @param mfield
     * @param type
     */
    private void maxOriMaxHandle(Object monthValueTab,String fType,String mfield,String type){
        try {
            Object monthValue = ReflectUtil.getValue(monthValueTab, mfield);
            if("f".equals(type)) {
                if (monthValue != null) {
                    Integer value=Integer.parseInt(monthValue+"");
                    //最多频率
                    Object max = ReflectUtil.getValue(monthValueTab, "V11314_061");
                    //最多风向
                    Object maxParam= ReflectUtil.getValue(monthValueTab, "V11314_CHAR");
                    //次多频率
                    Object mix = ReflectUtil.getValue(monthValueTab, "V11315_061");
                    //次多风向
                    Object mixParam = ReflectUtil.getValue(monthValueTab, "V11315_CHAR");
                    if(max!=null) {
                        Integer maxvalue = Integer.parseInt(max + "");
                        Integer mixvalue = Integer.parseInt(mix + "");
                        if (maxvalue < value) {
                            ReflectUtil.setValue(monthValueTab, "V11314_CHAR", fType);
                            ReflectUtil.setValue(monthValueTab, "V11314_061", value);//最多
                            ReflectUtil.setValue(monthValueTab, "V11315_061", maxvalue);//次多
                            ReflectUtil.setValue(monthValueTab, "V11315_CHAR", maxParam);
                        }else if(mixvalue<value){
                            ReflectUtil.setValue(monthValueTab, "V11315_061", value);//次多
                            ReflectUtil.setValue(monthValueTab, "V11315_CHAR", fType);
                        }
                    }else{
                        ReflectUtil.setValue(monthValueTab, "V11314_CHAR", fType);
                        ReflectUtil.setValue(monthValueTab, "V11314_061", value);//最多
                        ReflectUtil.setValue(monthValueTab, "V11315_061", value);//次多
                        ReflectUtil.setValue(monthValueTab, "V11315_CHAR", fType);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 总值
     * @param monthValueTab 月值
     * @param name 属性名
     * @param dayValueTab 日值
     * @throws Exception
     */
    private void totalHandle(Object monthValueTab, String name, Object dayValueTab) {
        try {
            Object monthValue = ReflectUtil.getValue(monthValueTab, name);
            Object dayValue = ReflectUtil.getValue(dayValueTab, name);
            Object currentValue = null;
            if (dayValue != null) {
                if (dayValue instanceof Integer) {
                    Integer dayEntity1 = Integer.parseInt(dayValue + "");
                    Integer monthEntity1 = 0;
                    if (monthValue != null) {
                        monthEntity1 = Integer.parseInt(monthValue + "");
                    }
                    Integer currentValue1 = monthEntity1 + dayEntity1;
                    currentValue = currentValue1;
                } else if (dayValue instanceof Float) {
                    Float dayEntity1 = Float.parseFloat(dayValue + "");
                    Float monthEntity1 = Float.parseFloat(0 + "");
                    if (monthValue != null) {
                        monthEntity1 = Float.parseFloat(monthValue + "");
                    }
                    Float currentValue1 = monthEntity1 + dayEntity1;
                    currentValue = currentValue1;
                }
                if(currentValue!=null) {
                    ReflectUtil.setValue(monthValueTab, name, currentValue);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 平均值
     * @param monthValueTab 月值
     * @param mfield 月属性名
     * @param dfield 月属性名
     * @param dayValueTab 日值
     * @param dayMonth 月日总数
     * @throws Exception
     */
    private  void averageHandle(Object monthValueTab, String mfield, String dfield, Object dayValueTab, int dayMonth) {
        try {
            Object monthValue = ReflectUtil.getValue(monthValueTab, mfield);
            Object dayValue = ReflectUtil.getValue(dayValueTab, dfield);
            Object currentValue = null;
            if (dayValue != null) {
                if (dayValue instanceof Integer) {
                    Integer dayEntity1 = Integer.parseInt(dayValue + "");
                    Integer monthEntity1 = 0;
                    if (monthValue != null) {
                        monthEntity1 = Integer.parseInt(monthValue + "");
                    }
                    Integer currentValue1 = monthEntity1 + dayEntity1/dayMonth;
                    currentValue = currentValue1;
                } else if (dayValue instanceof Float) {
                    Float dayEntity1 = Float.parseFloat(dayValue + "");
                    Float monthEntity1 = Float.parseFloat(0 + "");
                    if (monthValue != null) {
                        monthEntity1 = Float.parseFloat(monthValue + "");
                    }
                    Float currentValue1 = monthEntity1 + dayEntity1/dayMonth;
                    currentValue = currentValue1;
                }
                if(currentValue!=null) {
                    ReflectUtil.setValue(monthValueTab, mfield, currentValue);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 极值日数、日组装
     * @param yearValueTab
     * @param yearAdditional
     * @param yField
     * @param dayValueTab
     * @param monthAdditional
     * @param mField
     */
    private void  statisticalDays(Object yearValueTab,String yearAdditional,String yField, Object dayValueTab,String monthAdditional,String mField) {
        try {
            Object yearValue = ReflectUtil.getValue(yearValueTab, yField);
            Object monthValue = ReflectUtil.getValue(yearValueTab, mField);
            String type=null;
            if(yearValue==monthValue){
                type="1";
            }else if(Float.parseFloat(yearValue+"")>Float.parseFloat(monthValue+"")){
                type="2";
            }
            String[] yditions=yearAdditional.split(",");
            String[] mditions=monthAdditional.split(",");
            if("1".equals(type)){
                if(yditions.length>1) {//月日（mmdd）
                    String y = yditions[1];
                    Object mday = ReflectUtil.getValue(dayValueTab, y);
                    //月
                    Object month = ReflectUtil.getValue(dayValueTab, "V04002");
                    //日
                    Object day=ReflectUtil.getValue(dayValueTab, mditions[1]);
                    if (mday == null) {
                        if(month!=null&&day!=null) {
                            ReflectUtil.setValue(yearValueTab, y, month + "" + day);
                            if (yditions.length > 2) {
                                //月拼接
                                ReflectUtil.setValue(yearValueTab, yditions[2], month);
                                //日拼接
                                ReflectUtil.setValue(yearValueTab, yditions[3], day);
                            }
                        }
                    } else {
                        String value = mday + "," +month+""+day;
                        ReflectUtil.setValue(yearValueTab, y, value);
                        if(yditions.length>2){
                            Object mon1=ReflectUtil.getValue(dayValueTab, yditions[2]);
                            if(mon1==null){
                                ReflectUtil.setValue(yearValueTab, yditions[2], month);
                            }else{
                                ReflectUtil.setValue(yearValueTab, yditions[2], mon1+","+month);
                            }
                            Object day1=ReflectUtil.getValue(dayValueTab, yditions[3]);
                            if(day1==null){
                                ReflectUtil.setValue(yearValueTab, yditions[3], day);
                            }else{
                                ReflectUtil.setValue(yearValueTab, yditions[3], day1+","+day);
                            }
                        }
                    }
                }
                if(yditions.length>0){//日数
                    String y=yditions[0];
                    Object yday=ReflectUtil.getValue(dayValueTab, y);
                    String m=mditions[0];
                    Object months=ReflectUtil.getValue(dayValueTab, m);
                    if(yday==null) {
                        ReflectUtil.setValue(yearValueTab, y, months);
                    }else{
                        int mdays=Integer.parseInt(yday+"");
                        Integer monthCount=Integer.parseInt( months+"");
                        Integer value= mdays+monthCount;
                        ReflectUtil.setValue(yearValueTab, m, value);
                    }
                }

            }else if("2".equals(type)){
                if(yditions.length>1) {//月日（mmdd）
                    String y = yditions[1];
                    Object mday = ReflectUtil.getValue(dayValueTab, y);
                    //月
                    Object month = ReflectUtil.getValue(dayValueTab, "V04002");
                    //日
                    String day=mditions[1];
                    ReflectUtil.setValue(yearValueTab, y, month+""+day);
                    if(yditions.length>2){
                        //月拼接
                        ReflectUtil.setValue(yearValueTab, yditions[2], month);
                        //日拼接
                        ReflectUtil.setValue(yearValueTab, yditions[3], day);
                    }
                }
                if(yditions.length>0){//日数
                    String y=yditions[0];
                    Object yday=ReflectUtil.getValue(dayValueTab, y);
                    String m=mditions[0];
                    Object months=ReflectUtil.getValue(dayValueTab, m);
                    ReflectUtil.setValue(yearValueTab, y, months);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 极值处理
     * @param monthValueTab
     * @param mField 月值信息极值字段
     * @param dField 日值信息极值字段
     * @param mAdditional 月极值出现的附加字段，多个逗号隔开
     * @param dAdditional 日极值出现的附加字段，多个逗号隔开
     * 日、月极值出现的附加字段一一对应
     * @param dayValueTab
     * @param type
     */
    private void extremeHandle(Object monthValueTab, String mField,String dField,String mAdditional,String dAdditional, Object dayValueTab,String type){
        try {
            Object monthValue = ReflectUtil.getValue(monthValueTab, mField);
            Object dayValue = ReflectUtil.getValue(dayValueTab, dField);
            Object currentValue = null;
            if (dayValue != null) {
                if (monthValue != null) {
                    if (dayValue instanceof Integer) {
                        Integer monthValue1=Integer.parseInt(monthValue+"");
                        Integer dayValue1=Integer.parseInt(dayValue+"");
                        Integer currentValue1=null;
                        if("b".equals(type)) {//极端最大
                            if (monthValue1 <= dayValue1) {
                                currentValue1 = dayValue1;
                            }
                        }else if("s".equals(type)){//极端最小
                            if (monthValue1 >=dayValue1) {
                                currentValue1 = dayValue1;
                            }
                        }
                        currentValue=currentValue1;
                    } else if (dayValue instanceof Float) {
                        Float monthValue1=Float.parseFloat(monthValue+"");
                        Float dayValue1=Float.parseFloat(dayValue+"");
                        Float currentValue1=null;
                        if("b".equals(type)) {//极端最大
                            if (monthValue1 <= dayValue1) {
                                currentValue1 = dayValue1;
                            }
                        }else if("s".equals(type)){//极端最小
                            if (monthValue1 >=dayValue1) {
                                currentValue1 = dayValue1;
                            }
                        }
                        currentValue=currentValue1;
                    }
                 }else{
                    currentValue=dayValue;
                }
                if (currentValue != null) {
                    ReflectUtil.setValue(monthValueTab, mField, currentValue);
                    if(mAdditional!=null) {
                        //附加字段处理
                        String[] mDitions=mAdditional.split(",");
                        String[] dDitions=dAdditional.split(",");
                        for(int i=0,size=dDitions.length;i<size;i++) {
                            String m=mDitions[i];
                            String d=dDitions[i];
                            Object time1 = ReflectUtil.getValue(dayValueTab, d);
                            ReflectUtil.setValue(monthValueTab, m, time1);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 比较值处理
     * @param monthValueTab 月实体类
     * @param dField 当前值
     * @param mField 统计值
     * @param dayValueTab 日实体类
     * @param comparaValue 比较值
     * @param type 比较类型 s：当前值小于比较值，b：当前值大于比较值
     */
   private void compareDaysHandle(Object monthValueTab, String dField, String mField,Object dayValueTab,float comparaValue,String type){
       try {
           Object monthValue = ReflectUtil.getValue(monthValueTab, mField);
           Object dayValue = ReflectUtil.getValue(dayValueTab, dField);
           Object currentValue = null;
           if (dayValue != null) {
               Integer monthValue1=null;
               Integer currentValue1=null;
               boolean flag=false;
                if (dayValue instanceof Integer) {
                    Integer dayValue1=Integer.parseInt(dayValue+"");
                    if("s".equals(type)) {//  name1小于comparaValue
                        if (dayValue1 < comparaValue) {
                            flag=true;
                        }
                    }else if("b".equals(type)){  //name1大于comparaValue
                        if (dayValue1 > comparaValue) {
                            flag=true;
                        }
                    }
                    if(flag){
                        if (monthValue != null) {
                            monthValue1 =Integer.parseInt(monthValue+"");
                            currentValue1 = monthValue1 + 1;
                        } else {
                            currentValue1 = 1;
                        }
                    }
                    if(currentValue1!=null){
                        currentValue=currentValue1;
                    }
                }
               if (currentValue != null) {
                   ReflectUtil.setValue(monthValueTab, mField, currentValue);
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

}
