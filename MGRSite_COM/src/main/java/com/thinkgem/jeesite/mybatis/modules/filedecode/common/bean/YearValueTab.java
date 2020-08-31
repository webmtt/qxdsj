package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 年值信息
 * @author yangkq
 * @version 1.0
 * @date 2020/4/16
 */
public class YearValueTab {
    private String D_RECORD_ID;//记录标识 ( 系统自动生成的流水号 )
    private String D_DATA_ID;//资料标识 ( 资料的4级编码 )
    private String D_IYMDHM;//入库时间 ( 插表时的系统时间 )
    private String D_RYMDHM;//收到时间 ( DPC消息生成时间 )
    private String D_UPDATE_TIME;//更新时间 ( 根据CCx对记录更新时的系统时间 )
    private String D_DATETIME;//资料时间 ( 由V04001组成 )
    private String V01301;//区站号(字符)
    private String V01300;//区站号(数字)
    private Float V05001;//纬度 ( 单位：度 )
    private Float V06001;//经度 ( 单位：度 )
    private Float V07001;//测站高度 ( 单位：米 )
    private Float V07031;//气压传感器海拔高度 ( 单位：米 )
    private Integer V02301;//台站级别 ( 代码表 )
    private Integer V_ACODE;//中国行政区划代码 ( 代码表 )
    private Integer V04001;//年
    private Float V10004_701;//平均本站气压 ( 单位：百帕 )
    private Float V10301;//极端最高本站气压 ( 单位：百帕 )
    private Integer V10301_040;//极端最高本站气压出现日数
    private String V10301_067;//极端最高本站气压出现月日 ( 字符 )
    private Float V10302;//极端最低本站气压 ( 单位：百帕 )
    private Integer V10302_040;//极端最低本站气压出现日数
    private String V10302_067;//极端最低本站气压出现月日 ( 字符 )
    private Float V10051_701;//平均海平面气压 ( 单位：百帕 )
    private Float V10301_avg;//年平均最高本站气压
    private Float V10302_avg;//年平均最低本站气压
    private Float V12001_701;//平均气温 ( 单位：摄氏度 )
    private Float V12306;//气温年较差 ( 单位：摄氏度 )
    private Float maxMonthTemp;//月平均最高气温
    private Float minMonthTemp;//月平均最低气温
    private Float V12011_701;//年平均日最高气温 ( 单位：摄氏度 )
    private Float V12012_701;//年平均日最低气温 ( 单位：摄氏度 )
    private Float V12011;//极端最高气温 ( 单位：摄氏度 )
    private Integer V12011_040;//极端最高气温出现日数 (  )
    private String V12011_067;//极端最高气温出现月日 ( 字符 )
    private Float V12012;//极端最低气温 ( 单位：摄氏度 )
    private Integer V12012_040;//极端最低气温出现日数 (  )
    private String V12012_067;//极端最低气温出现月日 ( 字符 )
    private Float V12303_701;//平均气温日较差 ( 单位：摄氏度 )
    private Float V12304;//最大气温日较差 ( 单位：摄氏度 )
    private Integer V12304_040;//最大气温日较差出现日数 (  )
    private String V12304_067;//最大气温日较差出现月日 ( 字符 )
    private Float V12305;//最小气温日较差 ( 单位：摄氏度 )
    private Integer V12305_040;//最小气温日较差出现日数 (  )
    private String V12305_067;//最小气温日较差出现月日 ( 字符 )
    private Integer V12605_30;//日最高气温≥30℃日数 ( 单位：日 )
    private Integer V12710_30;//日最高气温≥30.0℃最长连续日数 ( 单位：日 )
    private Integer V12712_30;//日最高气温≥30.0℃最长连续日数之止月 (  )
    private Integer V12711_30;//日最高气温≥30.0℃最长连续日数之止日 (  )
    private Integer V12605_35;//日最高气温≥35℃日数 ( 单位：日 )
    private Integer V12710_35;//日最高气温≥35.0℃最长连续日数 ( 单位：日 )
    private Integer V12712_35;//日最高气温≥35.0℃最长连续日数之止月 (  )
    private Integer V12711_35;//日最高气温≥35.0℃最长连续日数之止日 (  )
    private Integer V12605_40;//日最高气温≥40℃日数 ( 单位：日 )
    private Integer V12710_40;//日最高气温≥40.0℃最长连续日数 ( 单位：日 )
    private Integer V12712_40;//日最高气温≥40.0℃最长连续日数之止月 (  )
    private Integer V12711_40;//日最高气温≥40.0℃最长连续日数之止日 (  )
    private Integer V12607_02;//日最低气温＜2℃日数 ( 单位：日 )
    private Integer V12713_02;//日最低气温＜2℃最长连续日数 ( 单位：日 )
    private Integer V12715_02;//日最低气温＜2℃最长连续日数之止月 (  )
    private Integer V12714_02;//日最低气温＜2℃最长连续日数之止日 (  )
    private Integer V12603;//日最低气温＜0℃日数 ( 单位：日 )
    private Integer V12713_00;//日最低气温＜0℃最长连续日数 ( 单位：日 )
    private Integer V12715_00;//日最低气温＜0℃最长连续日数之止月 (  )
    private Integer V12714_00;//日最低气温＜0℃最长连续日数之止日 (  )
    private Integer V12606_02;//日最低气温＜-2℃日数 ( 单位：日 )
    private Integer V12716_02;//日最低气温＜-2℃最长连续日数 ( 单位：日 )
    private Integer V12718_02;//日最低气温＜-2℃最长连续日数之止月 (  )
    private Integer V12717_02;//日最低气温＜-2℃最长连续日数之止日 (  )
    private Integer V12606_05;//日最低气温＜-5℃日数 ( 单位：日 )
    private Integer V12716_05;//日最低气温＜-5℃最长连续日数 ( 单位：日 )
    private Integer V12718_05;//日最低气温＜-5℃最长连续日数之止月 (  )
    private Integer V12717_05;//日最低气温＜-5℃最长连续日数之止日 (  )
    private Integer V12606_10;//日最低气温＜-10℃日数 ( 单位：日 )
    private Integer V12716_10;//日最低气温＜-10℃最长连续日数 ( 单位：日 )
    private Integer V12718_10;//日最低气温＜-10℃最长连续日数之止月 (  )
    private Integer V12717_10;//日最低气温＜-10℃最长连续日数之止日 (  )
    private Integer V12606_15;//日最低气温＜-15℃日数 ( 单位：日 )
    private Integer V12716_15;//日最低气温＜-15℃最长连续日数 ( 单位：日 )
    private Integer V12718_15;//日最低气温＜-15℃最长连续日数之止月 (  )
    private Integer V12717_15;//日最低气温＜-15℃最长连续日数之止日 (  )
    private Integer V12606_30;//日最低气温＜-30℃日数 ( 单位：日 )
    private Integer V12716_30;//日最低气温＜-30℃最长连续日数 ( 单位：日 )
    private Integer V12718_30;//日最低气温＜-30℃最长连续日数之止月 (  )
    private Integer V12717_30;//日最低气温＜-30℃最长连续日数之止日 (  )
    private Integer V12606_40;//日最低气温＜-40℃日数 ( 单位：日 )
    private Integer V12716_40;//日最低气温＜-40℃最长连续日数 ( 单位：日 )
    private Integer V12718_40;//日最低气温＜-40℃最长连续日数之止月 (  )
    private Integer V12717_40;//日最低气温＜-40℃最长连续日数之止日 (  )
    private Integer V12705_0;//日平均气温稳定通过0.0℃日数
    private Integer V12705_00;//日平均气温稳定通过0.0℃起始月 (  )
    private Integer V12703_00;//日平均气温稳定通过0.0℃起始日 (  )
    private Integer V12706_00;//日平均气温稳定通过0.0℃止月 (  )
    private Integer V12704_00;//日平均气温稳定通过0.0℃止日 (  )
    private Integer V12700_00;//日平均气温稳定通过0.0℃之积温 ( 单位：摄氏度 )
    private Integer V12701_00;//日平均气温稳定通过0.0℃之降水量 ( 单位：毫米 )
    private Integer V12702_00;//日平均气温稳定通过0.0℃之日照时数 ( 单位：小时 )
    private Integer V12705_5;//日平均气温稳定通过5.0℃日数
    private Integer V12705_05;//日平均气温稳定通过5.0℃起始月 (  )
    private Integer V12703_05;//日平均气温稳定通过5.0℃起始日 (  )
    private Integer V12706_05;//日平均气温稳定通过5.0℃止月 (  )
    private Integer V12704_05;//日平均气温稳定通过5.0℃止日 (  )
    private Integer V12700_05;//日平均气温稳定通过5.0℃之积温 ( 单位：摄氏度 )
    private Integer V12701_05;//日平均气温稳定通过5.0℃之降水量 ( 单位：毫米 )
    private Integer V12702_05;//日平均气温稳定通过5.0℃之日照时数 ( 单位：小时 )
    private Integer V12705_10C;//日平均气温稳定通过10.0℃日数
    private Integer V12705_10;//日平均气温稳定通过10.0℃起始月 (  )
    private Integer V12703_10;//日平均气温稳定通过10.0℃起始日 (  )
    private Integer V12706_10;//日平均气温稳定通过10.0℃止月 (  )
    private Integer V12704_10;//日平均气温稳定通过10.0℃止日 (  )
    private Integer V12700_10;//日平均气温稳定通过10.0℃之积温 ( 单位：摄氏度 )
    private Integer V12701_10;//日平均气温稳定通过10.0℃之降水量 ( 单位：毫米 )
    private Integer V12702_10;//日平均气温稳定通过10.0℃之日照时数 ( 单位：小时 )
    private Integer V12705_15C;//日平均气温稳定通过15.0℃日数
    private Integer V12705_15;//日平均气温稳定通过15.0℃起始月 (  )
    private Integer V12703_15;//日平均气温稳定通过15.0℃起始日 (  )
    private Integer V12706_15;//日平均气温稳定通过15.0℃止月 (  )
    private Integer V12704_15;//日平均气温稳定通过15.0℃止日 (  )
    private Integer V12700_15;//日平均气温稳定通过15.0℃之积温 ( 单位：摄氏度 )
    private Integer V12701_15;//日平均气温稳定通过15.0℃之降水量 ( 单位：毫米 )
    private Integer V12702_15;//日平均气温稳定通过15.0℃之日照时数 ( 单位：小时 )
    private Integer V12705_20C;//日平均气温稳定通过20.0℃日数
    private Integer V12705_20;//日平均气温稳定通过20.0℃起始月 (  )
    private Integer V12703_20;//日平均气温稳定通过20.0℃起始日 (  )
    private Integer V12706_20;//日平均气温稳定通过20.0℃止月 (  )
    private Integer V12704_20;//日平均气温稳定通过20.0℃止日 (  )
    private Integer V12700_20;//日平均气温稳定通过20.0℃之积温 ( 单位：摄氏度 )
    private Integer V12701_20;//日平均气温稳定通过20.0℃之降水量 ( 单位：毫米 )
    private Integer V12702_20;//日平均气温稳定通过20.0℃之日照时数 ( 单位：小时 )
    private Integer V12705_22C;//日平均气温稳定通过0.0℃日数
    private Integer V12705_22;//日平均气温稳定通过22.0℃起始月 (  )
    private Integer V12703_22;//日平均气温稳定通过22.0℃起始日 (  )
    private Integer V12706_22;//日平均气温稳定通过22.0℃止月 (  )
    private Integer V12704_22;//日平均气温稳定通过22.0℃止日 (  )
    private Integer V12700_22;//日平均气温稳定通过22.0℃之积温 ( 单位：摄氏度 )
    private Integer V12701_22;//日平均气温稳定通过22.0℃之降水量 ( 单位：毫米 )
    private Integer V12702_22;//日平均气温稳定通过22.0℃之日照时数 ( 单位：小时 )
    private Integer V12610_26;//冷度日数（日平均气温≥26.0℃） ( 单位：度日 )
    private Integer V12611_18;//暖度日数（日平均气温≤18.0℃） ( 单位：度日 )
    private Float V13004_701;//平均水汽压 ( 单位：百帕 )
    private Float balltemp_avg;//年平均湿球温度
    private Float V13004_MAX;//年最大水汽压
    private Integer V13004_MAX_M;//年最大水汽压出现月份
    private Integer V13004_MAX_D;//年最大水汽压出现日期
    private Float V13004_MIN;//年最小水气压
    private Integer V13004_MIN_M;// 年最小水汽压出现月份
    private Integer V13004_MIN_D;// 年最小水汽压出现日期
    private Integer V13003_701;//平均相对湿度 ( 单位：% )
    private Integer V13007;//最小相对湿度 ( 单位：% )
    private Integer V13007_040;//最小相对湿度出现日数 (  )
    private String V13007_067;//最小相对湿度出现月日 ( 字符 )
    private Integer V20010_701;//平均总云量 ( 单位：% )
    private Integer V20051_701;//平均低云量 ( 单位：% )
    private Integer V20501_02;//日平均总云量< 2.0日数 ( 单位：日 )
    private Integer V20500_08;//日平均总云量> 8.0日数 ( 单位：日 )
    private Integer V20503_02;//日平均低云量< 2.0日数 ( 单位：日 )
    private Integer V20502_08;//日平均低云量> 8.0日数 ( 单位：日 )
    private Float V13305;//20-20时年降水量 ( 单位：毫米 )
    private Float V13306;//08-08时年降水量 ( 单位：毫米 )
    private Float V13305_SD;//20-20时年固态降水量 ( 单位：毫米 )
    private Float V13306_SD;//08-08时年固态降水量 ( 单位：毫米 )
    private Float V13052;//最大日降水量 ( 单位：毫米 )
    private Integer V13052_040;//最大日降水量出现日数 (  )
    private String V13052_067;//最大日降水量出现月日 ( 字符 )
    private Integer V13353;//日降水量≥0.1mm日数 ( 单位：日 )
    private Integer V13355_001;//日降水量≥1mm日数 ( 单位：日 )
    private Integer V13355_005;//日降水量≥5mm日数 ( 单位：日 )
    private Integer V13355_010;//日降水量≥10mm日数 ( 单位：日 )
    private Integer V13355_025;//日降水量≥25mm日数 ( 单位：日 )
    private Integer V13355_050;//日降水量≥50mm日数 ( 单位：日 )
    private Integer V13355_100;//日降水量≥100mm日数 ( 单位：日 )
    private Integer V13355_150;//日降水量≥150mm日数 ( 单位：日 )
    private Integer V13355_250;//日降水量≥250mm日数 ( 单位：日 )
    private Integer V20430;//最长连续降水日数 ( 单位：日 )
    private Integer V13380;//最长连续降水量 ( 单位：毫米 )
    private Integer V20438;//最长连续降水止月 (  )
    private Integer V20431;//最长连续降水止日 (  )
    private Integer V20432;//最长连续无降水日数 ( 单位：日 )
    private Integer V20437;//最长连续无降水止月 (  )
    private Integer V20433;//最长连续无降水止日 (  )
    private Integer V13381;//最大连续降水量 ( 单位：毫米 )
    private Integer V20434;//最大连续降水日数 ( 单位：日 )
    private Integer V20436;//最大连续降水止月 (  )
    private Integer V20435;//最大连续降水止日 (  )
    private Float V13302_01;//1小时最大降水量 ( 单位：毫米 )
    private Integer V13302_01_040;//1小时最大降水量出现日数  (  )
    private String V13302_01_067;//1小时最大降水量出现月日 (  )
    private Integer V13302_01_058;//1小时最大降水量出现月 (  )
    private String V13302_01_060;//1小时最大降水量出现日 (  )
    private Float V13382_005;//年最大5分钟降水量 ( 单位：毫米 )
    private Integer V13382_005_STIME;//年最大5分钟降水起始时间（MMddhhmm） ( 格式：MMddhhmm )
    private Float V13382_010;//年最大10分钟降水量 ( 单位：毫米 )
    private Integer V13382_010_STIME;//年最大10分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_015;//年最大15分钟降水量 ( 单位：毫米 )
    private Integer V13382_015_STIME;//年最大15分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_020;//年最大20分钟降水量 ( 单位：毫米 )
    private Integer V13382_020_STIME;//年最大20分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_030;//年最大30分钟降水量 ( 单位：毫米 )
    private Integer V13382_030_STIME;//年最大30分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_045;//年最大45分钟降水量 ( 单位：毫米 )
    private Integer V13382_045_STIME;//年最大45分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_060;//年最大60分钟降水量 ( 单位：毫米 )
    private Integer V13382_060_STIME;//年最大60分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_090;//年最大90分钟降水量 ( 单位：毫米 )
    private Integer V13382_090_STIME;//年最大90分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_120;//年最大120分钟降水量 ( 单位：毫米 )
    private Integer V13382_120_STIME;//年最大120分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_180;//年最大180分钟降水量 ( 单位：毫米 )
    private Integer V13382_180_STIME;//年最大180分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_240;//年最大240分钟降水量 ( 单位：毫米 )
    private Integer V13382_240_STIME;//年最大240分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_360;//年最大360分钟降水量 ( 单位：毫米 )
    private Integer V13382_360_STIME;//年最大360分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_540;//年最大540分钟降水量 ( 单位：毫米 )
    private Integer V13382_540_STIME;//年最大540分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_720;//年最大720分钟降水量 ( 单位：毫米 )
    private Integer V13382_720_STIME;//年最大720分钟降水起始时间 ( 格式：MMddhhmm )
    private Float V13382_144;//年最大1440分钟降水量 ( 单位：毫米 )
    private Integer V13382_144_STIME;//年最大1440分钟降水起始时间 ( 格式：MMddhhmm )
    private Integer V04330_060;//雨日数 ( 单位：日 )
    private Integer V04330_089;//冰雹日数 ( 单位：日 )
    private Integer V04330_010;//轻雾日数 ( 单位：日 )
    private Integer V04330_001;//露日数 ( 单位：日 )
    private Integer V04330_056;//雨凇日数 ( 单位：日 )
    private Integer V04330_048;//雾凇日数 ( 单位：日 )
    private Integer V04330_003;//结冰日数 ( 单位：日 )
    private Integer V04330_007;//扬沙日数 ( 单位：日 )
    private Integer V04330_006;//浮尘日数 ( 单位：日 )
    private Integer V04330_005;//霾日数 ( 单位：日 )
    private Integer V04330_008;//龙卷风日数 ( 单位：日 )
    private Integer V04330_015;//大风日数 ( 单位：日 )
    private Integer V04330_031;//沙尘暴日数 ( 单位：日 )
    private Integer V04330_042;//雾日数 ( 单位：日 )
    private Integer V04330_017;//雷暴日数 ( 单位：日 )
    private Integer V04330_002;//霜日数 ( 单位：日 )
    private Integer V04330_070;//降雪日数 ( 单位：日 )
    private Integer V04330_016;//积雪日数 ( 单位：日 )
    private Integer V20305_540;//电线积冰（雨凇+雾凇）日数 ( 单位：日 )
    private Integer V20309_010;//能见度＜≤10km出现频率 ( 单位：% )
    private Integer V20309_005;//能见度＜5km出现频率 ( 单位：% )
    private Integer V20309_001;//能见度＜≤1km出现频率 ( 单位：% )
    private Float V13032;//蒸发量(小型) ( 单位：毫米 )
    private Float V13033;//蒸发量(大型) ( 单位：毫米 )
    private Integer V13032_MAX;//年最大小型蒸发量
    private Integer V13032_MAX_M;// 年最大小型蒸发量出现月份
    private Integer V13032_MAX_D;// 年最大小型蒸发量出现日期
    private Integer V13033_MAX;//年最大大型蒸发量
    private Integer V13033_MAX_M;//年最大大型蒸发量出现月份
    private Integer V13033_MAX_D;//年最大大型蒸发量出现日期
    private Float V13334;//最大积雪深度 ( 单位：厘米 )
    private Integer V13334_040;//最大积雪深度出现日数 (  )
    private String V13334_067;//最大积雪深度出现月日 ( 字符 )
    private Float V13330;//最大雪压 ( 单位：克/平方厘米 )
    private Integer V13330_040;//最大雪压出现日数 (  )
    private String V13330_067;//最大雪压出现月日 ( 字符 )
    private Integer V13356_001;//积雪深度≥1cm日数 ( 单位：日 )
    private Integer V13356_005;//积雪深度≥5cm日数 ( 单位：日 )
    private Integer V13356_010;//积雪深度≥10cm日数 ( 单位：日 )
    private Integer V13356_020;//积雪深度≥20cm日数 ( 单位：日 )
    private Integer V13356_030;//积雪深度≥30cm日数 ( 单位：日 )
    private Integer V20440_NS;//电线积冰南北最大重量 ( 单位：克 )
    private Integer V20441_NS;//电线积冰南北最大重量的相应直径 ( 单位：毫米 )
    private Integer V20442_NS;//电线积冰南北最大重量的相应厚度 ( 单位：毫米 )
    private Integer V20440_WE;//电线积冰东西最大重量 ( 单位：克 )
    private Integer V20441_WE;//电线积冰东西最大重量的相应直径 ( 单位：毫米 )
    private Integer V20442_WE;//电线积冰东西最大重量的相应厚度 ( 单位：毫米 )
    private Integer V20440_040_NS;//电线积冰南北最大重量出现日数 ( 单位：日 )
    private String V20440_067_NS;//电线积冰南北最大重量出现月日 ( 格式：mmdd )
    private Integer V20440_058_NS;//电线积冰南北最大重量出现月 (  )
    private String V20440_060_NS;// 电线积冰南北最大重量出现日 (  )
    private Integer V20440_040_WE;//电线积冰东西最大重量出现日数 ( 单位：日 )
    private String V20440_067_WE;//电线积冰东西最大重量出现月日 ( 格式：mmdd )
    private Integer V20440_058_WE;//电线积冰东西最大重量出现月 (  )
    private String V20440_060_WE;//电线积冰东西最大重量出现日 (  )
    private Integer V11291_701;//平均风速（2分钟） ( 单位：米/秒 )
    private Float V11042;//最大风速 ( 单位：米/秒 )
    private String V11296_CHAR;//最大风速的风向 ( 代码表 )
    private Integer V11042_040;//最大风速之出现日数 (  )
    private String V11042_067;//最大风速之出现月日 ( 字符 )
    private Integer V11042_05;//日最大风速≥5.0m/s日数 ( 单位：日 )
    private Integer V11042_10;//日最大风速≥10.0m/s日数 ( 单位：日 )
    private Integer V11042_12;//日最大风速≥12.0m/s日数 ( 单位：日 )
    private Integer V11042_15;//日最大风速≥15.0m/s日数 ( 单位：日 )
    private Integer V11042_17;//日最大风速≥17.0m/s日数 ( 单位：日 )
    private Integer V11046;//极大风速 ( 单位：米/秒 )
    private String V11211_CHAR;//极大风速之风向 ( 代码表 )
    private Integer V11046_040;//极大风速之出现日数 (  )
    private String V11046_067_CHAR;//极大风速之出现月日 ( 字符 )
    private Integer V11350_NNE;//NNE风向出现频率 ( 单位：% )
    private Integer V11350_NE;//NE风向出现频率 ( 单位：% )
    private Integer V11350_ENE;//ENE风向出现频率 ( 单位：% )
    private Integer V11350_E;//E风向出现频率 ( 单位：% )
    private Integer V11350_ESE;//ESE风向出现频率 ( 单位：% )
    private Integer V11350_SE;//SE风向出现频率 ( 单位：% )
    private Integer V11350_SSE;//SSE风向出现频率 ( 单位：% )
    private Integer V11350_S;//S风向出现频率 ( 单位：% )
    private Integer V11350_SSW;//SSW风向出现频率 ( 单位：% )
    private Integer V11350_SW;//SW风向出现频率 ( 单位：% )
    private Integer V11350_WSW;//WSW风向出现频率 ( 单位：% )
    private Integer V11350_W;//W风向出现频率 ( 单位：% )
    private Integer V11350_WNW;//WNW风向出现频率 ( 单位：% )
    private Integer V11350_NW;//NW风向出现频率 ( 单位：% )
    private Integer V11350_NNW;//NNW风向出现频率 ( 单位：% )
    private Integer V11350_N;//N风向出现频率 ( 单位：% )
    private Integer V11350_C;//C风向（静风）出现频率 ( 单位：% )
    private String V11314_CHAR;//最多风向 ( 代码表 )
    private Integer V11314_061;//最多风向出现频率 ( 单位：% )
    private String V11315_CHAR;//次多风向 ( 代码表 )
    private Integer V11315_061;//次多风向出现频率 ( 单位：% )
    private Integer V11351_NNE;//NNE风的平均风速 ( 单位：米/秒 )
    private Integer V11351_NE;//NE风的平均风速 ( 单位：米/秒 )
    private Integer V11351_ENE;//ENE的平均风速 ( 单位：米/秒 )
    private Integer V11351_E;//E风的平均风速 ( 单位：米/秒 )
    private Integer V11351_ESE;//ESE风的平均风速 ( 单位：米/秒 )
    private Integer V11351_SE;//SE风的平均风速 ( 单位：米/秒 )
    private Integer V11351_SSE;//SSE风的平均风速 ( 单位：米/秒 )
    private Integer V11351_S;//S风的平均风速 ( 单位：米/秒 )
    private Integer V11351_SSW;//SSW风的平均风速 ( 单位：米/秒 )
    private Integer V11351_SW;//SW风的平均风速 ( 单位：米/秒 )
    private Integer V11351_WSW;//WSW风的平均风速 ( 单位：米/秒 )
    private Integer V11351_W;//W风的平均风速 ( 单位：米/秒 )
    private Integer V11351_WNW;//WNW风的平均风速 ( 单位：米/秒 )
    private Integer V11351_NW;//NW风的平均风速 ( 单位：米/秒 )
    private Integer V11351_NNW;//NNW风的平均风速 ( 单位：米/秒 )
    private Integer V11351_N;//N风的平均风速 ( 单位：米/秒 )
    private Integer V11042_NNE;//NNE风的最大风速 ( 单位：米/秒 )
    private Integer V11042_NE;//NE风的最大风速 ( 单位：米/秒 )
    private Integer V11042_ENE;//ENE的最大风速 ( 单位：米/秒 )
    private Integer V11042_E;//E风的最大风速 ( 单位：米/秒 )
    private Integer V11042_ESE;//ESE风的最大风速 ( 单位：米/秒 )
    private Integer V11042_SE;//SE风的最大风速 ( 单位：米/秒 )
    private Integer V11042_SSE;//SSE风的最大风速 ( 单位：米/秒 )
    private Integer V11042_S;//S风的最大风速 ( 单位：米/秒 )
    private Integer V11042_SSW;//SSW风的最大风速 ( 单位：米/秒 )
    private Integer V11042_SW;//SW风的最大风速 ( 单位：米/秒 )
    private Integer V11042_WSW;//WSW风的最大风速 ( 单位：米/秒 )
    private Integer V11042_W;//W风的最大风速 ( 单位：米/秒 )
    private Integer V11042_WNW;//WNW风的最大风速 ( 单位：米/秒 )
    private Integer V11042_NW;//NW风的最大风速 ( 单位：米/秒 )
    private Integer V11042_NNW;//NNW风的最大风速 ( 单位：米/秒 )
    private Integer V11042_N;//N风的最大风速 ( 单位：米/秒 )
    private Integer V12120_701;//平均地面温度 ( 单位：摄氏度 )
    private Integer V12311_701;//平均最高地面温度 ( 单位：摄氏度 )
    private Integer V12121_701;//平均最低地面温度 ( 单位：摄氏度 )
    private Integer V12620;//日最低地面温度≤0.0℃日数 ( 单位：日 )
    private Integer V12311;//极端最高地面温度 ( 单位：摄氏度 )
    private Integer V12311_040;//极端最高地面温度出现日数 (  )
    private String V12311_067;//极端最高地面温度出现月日 ( 字符 )
    private Integer V12121;//极端最低地面温度 ( 单位：摄氏度 )
    private Integer V12121_040;//极端最低地面温度出现日数 (  )
    private String V12121_067;//极端最低地面温度出现月日 ( 字符 )
    private Float V12030_701_005;//平均5cm地温 ( 单位：摄氏度 )
    private Float V12030_701_010;//平均10cm地温 ( 单位：摄氏度 )
    private Float V12030_701_015;//平均15cm地温 ( 单位：摄氏度 )
    private Float V12030_701_020;//平均20cm地温 ( 单位：摄氏度 )
    private Float V12030_701_040;//平均40cm地温 ( 单位：摄氏度 )
    private Float V12030_701_080;//平均80cm地温 ( 单位：摄氏度 )
    private Float V12030_701_160;//平均160cm地温 ( 单位：摄氏度 )
    private Float V12030_701_320;//平均320cm地温 ( 单位：摄氏度 )
    private Float V12314_701;//平均草面（雪面）温度 ( 单位：摄氏度 )
    private Float V12315_701;//平均最高草面（雪面）温度 ( 单位：摄氏度 )
    private Float V12316_701;//平均最低草面（雪面）温度 ( 单位：摄氏度 )
    private Float V12315;//极端最高草面（雪面）气温 ( 单位：摄氏度 )
    private Integer V12315_040;//极端最高草面（雪面）气温出现日数 (  )
    private String V12315_067;//极端最高草面（雪面）气温出现月日 ( 字符 )
    private Float V12316;//极端最低草面（雪面）气温 ( 单位：摄氏度 )
    private Integer V12316_040;//极端最低草面（雪面）气温出现日数 (  )
    private String V12316_067;//极端最低草面（雪面）气温出现月日 ( 字符 )
    private Float V20334;//最大冻土深度 ( 单位：厘米 )
    private Integer V20334_040;//最大冻土深度出现日数 (  )
    private String V20334_067;//最大冻土深度出现月日 ( 字符 )
    private Float V14032;//日照总时数 ( 单位：小时 )
    private Float sunTime;//日出总时长
    private Integer V14033;//日照百分率 ( 单位：% )
    private Integer V20302_171;//日照百分率≥60%日数 ( 单位：日 )
    private Integer V20302_172;//日照百分率≤20%日数 ( 单位：日 )
    private BigDecimal v14311;//总辐射辐照度
    private BigDecimal v14312;//净辐射辐照度
    private BigDecimal v14311_05;//日总辐射辐照度最大值
    private Integer v14311_05_04_002;//日总辐射辐照度最大出现月
    private Integer v14311_05_04_003;//日总辐射辐照度最大出现日
    private Integer v14311_05_04_004;//日总辐射辐照度最大出现时
    private BigDecimal v14312_05;//日净辐射辐照度最大值
    private Integer v14312_05_04_002;//日总辐射辐照度最大出现月
    private Integer v14312_05_04_003;//日总辐射辐照度最大出现日
    private Integer v14312_05_04_004;//日总辐射辐照度最大出现时

    public String getD_RECORD_ID() {
        return D_RECORD_ID;
    }

    public void setD_RECORD_ID(String d_RECORD_ID) {
        D_RECORD_ID = d_RECORD_ID;
    }

    public String getD_DATA_ID() {
        return D_DATA_ID;
    }

    public void setD_DATA_ID(String d_DATA_ID) {
        D_DATA_ID = d_DATA_ID;
    }

    public String getD_IYMDHM() {
        return D_IYMDHM;
    }

    public void setD_IYMDHM(String d_IYMDHM) {
        D_IYMDHM = d_IYMDHM;
    }

    public String getD_RYMDHM() {
        return D_RYMDHM;
    }

    public void setD_RYMDHM(String d_RYMDHM) {
        D_RYMDHM = d_RYMDHM;
    }

    public String getD_UPDATE_TIME() {
        return D_UPDATE_TIME;
    }

    public void setD_UPDATE_TIME(String d_UPDATE_TIME) {
        D_UPDATE_TIME = d_UPDATE_TIME;
    }

    public String getD_DATETIME() {
        return D_DATETIME;
    }

    public void setD_DATETIME(String d_DATETIME) {
        D_DATETIME = d_DATETIME;
    }

    public String getV01301() {
        return V01301;
    }

    public void setV01301(String v01301) {
        V01301 = v01301;
    }

    public String getV01300() {
        return V01300;
    }

    public void setV01300(String v01300) {
        V01300 = v01300;
    }

    public Float getV05001() {
        return V05001;
    }

    public void setV05001(Float v05001) {
        V05001 = v05001;
    }

    public Float getV06001() {
        return V06001;
    }

    public void setV06001(Float v06001) {
        V06001 = v06001;
    }

    public Float getV07001() {
        return V07001;
    }

    public void setV07001(Float v07001) {
        V07001 = v07001;
    }

    public Float getV07031() {
        return V07031;
    }

    public void setV07031(Float v07031) {
        V07031 = v07031;
    }

    public Integer getV02301() {
        return V02301;
    }

    public void setV02301(Integer v02301) {
        V02301 = v02301;
    }

    public Integer getV_ACODE() {
        return V_ACODE;
    }

    public void setV_ACODE(Integer v_ACODE) {
        V_ACODE = v_ACODE;
    }

    public Integer getV04001() {
        return V04001;
    }

    public void setV04001(Integer v04001) {
        V04001 = v04001;
    }

    public Float getV10004_701() {
        return V10004_701;
    }

    public void setV10004_701(Float v10004_701) {
        V10004_701 = v10004_701;
    }

    public Float getV10301() {
        return V10301;
    }

    public void setV10301(Float v10301) {
        V10301 = v10301;
    }

    public Integer getV10301_040() {
        return V10301_040;
    }

    public void setV10301_040(Integer v10301_040) {
        V10301_040 = v10301_040;
    }

    public String getV10301_067() {
        return V10301_067;
    }

    public void setV10301_067(String v10301_067) {
        V10301_067 = v10301_067;
    }

    public Float getV10302() {
        return V10302;
    }

    public void setV10302(Float v10302) {
        V10302 = v10302;
    }

    public Integer getV10302_040() {
        return V10302_040;
    }

    public void setV10302_040(Integer v10302_040) {
        V10302_040 = v10302_040;
    }

    public String getV10302_067() {
        return V10302_067;
    }

    public void setV10302_067(String v10302_067) {
        V10302_067 = v10302_067;
    }

    public Float getV10051_701() {
        return V10051_701;
    }

    public void setV10051_701(Float v10051_701) {
        V10051_701 = v10051_701;
    }

    public Float getV10301_avg() {
        return V10301_avg;
    }

    public void setV10301_avg(Float v10301_avg) {
        V10301_avg = v10301_avg;
    }

    public Float getV10302_avg() {
        return V10302_avg;
    }

    public void setV10302_avg(Float v10302_avg) {
        V10302_avg = v10302_avg;
    }

    public Float getV12001_701() {
        return V12001_701;
    }

    public void setV12001_701(Float v12001_701) {
        V12001_701 = v12001_701;
    }

    public Float getV12306() {
        return V12306;
    }

    public void setV12306(Float v12306) {
        V12306 = v12306;
    }

    public Float getMaxMonthTemp() {
        return maxMonthTemp;
    }

    public void setMaxMonthTemp(Float maxMonthTemp) {
        this.maxMonthTemp = maxMonthTemp;
    }

    public Float getMinMonthTemp() {
        return minMonthTemp;
    }

    public void setMinMonthTemp(Float minMonthTemp) {
        this.minMonthTemp = minMonthTemp;
    }

    public Float getV12011_701() {
        return V12011_701;
    }

    public void setV12011_701(Float v12011_701) {
        V12011_701 = v12011_701;
    }

    public Float getV12012_701() {
        return V12012_701;
    }

    public void setV12012_701(Float v12012_701) {
        V12012_701 = v12012_701;
    }

    public Float getV12011() {
        return V12011;
    }

    public void setV12011(Float v12011) {
        V12011 = v12011;
    }

    public Integer getV12011_040() {
        return V12011_040;
    }

    public void setV12011_040(Integer v12011_040) {
        V12011_040 = v12011_040;
    }

    public String getV12011_067() {
        return V12011_067;
    }

    public void setV12011_067(String v12011_067) {
        V12011_067 = v12011_067;
    }

    public Float getV12012() {
        return V12012;
    }

    public void setV12012(Float v12012) {
        V12012 = v12012;
    }

    public Integer getV12012_040() {
        return V12012_040;
    }

    public void setV12012_040(Integer v12012_040) {
        V12012_040 = v12012_040;
    }

    public String getV12012_067() {
        return V12012_067;
    }

    public void setV12012_067(String v12012_067) {
        V12012_067 = v12012_067;
    }

    public Float getV12303_701() {
        return V12303_701;
    }

    public void setV12303_701(Float v12303_701) {
        V12303_701 = v12303_701;
    }

    public Float getV12304() {
        return V12304;
    }

    public void setV12304(Float v12304) {
        V12304 = v12304;
    }

    public Integer getV12304_040() {
        return V12304_040;
    }

    public void setV12304_040(Integer v12304_040) {
        V12304_040 = v12304_040;
    }

    public String getV12304_067() {
        return V12304_067;
    }

    public void setV12304_067(String v12304_067) {
        V12304_067 = v12304_067;
    }

    public Float getV12305() {
        return V12305;
    }

    public void setV12305(Float v12305) {
        V12305 = v12305;
    }

    public Integer getV12305_040() {
        return V12305_040;
    }

    public void setV12305_040(Integer v12305_040) {
        V12305_040 = v12305_040;
    }

    public String getV12305_067() {
        return V12305_067;
    }

    public void setV12305_067(String v12305_067) {
        V12305_067 = v12305_067;
    }

    public Integer getV12605_30() {
        return V12605_30;
    }

    public void setV12605_30(Integer v12605_30) {
        V12605_30 = v12605_30;
    }

    public Integer getV12710_30() {
        return V12710_30;
    }

    public void setV12710_30(Integer v12710_30) {
        V12710_30 = v12710_30;
    }

    public Integer getV12712_30() {
        return V12712_30;
    }

    public void setV12712_30(Integer v12712_30) {
        V12712_30 = v12712_30;
    }

    public Integer getV12711_30() {
        return V12711_30;
    }

    public void setV12711_30(Integer v12711_30) {
        V12711_30 = v12711_30;
    }

    public Integer getV12605_35() {
        return V12605_35;
    }

    public void setV12605_35(Integer v12605_35) {
        V12605_35 = v12605_35;
    }

    public Integer getV12710_35() {
        return V12710_35;
    }

    public void setV12710_35(Integer v12710_35) {
        V12710_35 = v12710_35;
    }

    public Integer getV12712_35() {
        return V12712_35;
    }

    public void setV12712_35(Integer v12712_35) {
        V12712_35 = v12712_35;
    }

    public Integer getV12711_35() {
        return V12711_35;
    }

    public void setV12711_35(Integer v12711_35) {
        V12711_35 = v12711_35;
    }

    public Integer getV12605_40() {
        return V12605_40;
    }

    public void setV12605_40(Integer v12605_40) {
        V12605_40 = v12605_40;
    }

    public Integer getV12710_40() {
        return V12710_40;
    }

    public void setV12710_40(Integer v12710_40) {
        V12710_40 = v12710_40;
    }

    public Integer getV12712_40() {
        return V12712_40;
    }

    public void setV12712_40(Integer v12712_40) {
        V12712_40 = v12712_40;
    }

    public Integer getV12711_40() {
        return V12711_40;
    }

    public void setV12711_40(Integer v12711_40) {
        V12711_40 = v12711_40;
    }

    public Integer getV12607_02() {
        return V12607_02;
    }

    public void setV12607_02(Integer v12607_02) {
        V12607_02 = v12607_02;
    }

    public Integer getV12713_02() {
        return V12713_02;
    }

    public void setV12713_02(Integer v12713_02) {
        V12713_02 = v12713_02;
    }

    public Integer getV12715_02() {
        return V12715_02;
    }

    public void setV12715_02(Integer v12715_02) {
        V12715_02 = v12715_02;
    }

    public Integer getV12714_02() {
        return V12714_02;
    }

    public void setV12714_02(Integer v12714_02) {
        V12714_02 = v12714_02;
    }

    public Integer getV12603() {
        return V12603;
    }

    public void setV12603(Integer v12603) {
        V12603 = v12603;
    }

    public Integer getV12713_00() {
        return V12713_00;
    }

    public void setV12713_00(Integer v12713_00) {
        V12713_00 = v12713_00;
    }

    public Integer getV12715_00() {
        return V12715_00;
    }

    public void setV12715_00(Integer v12715_00) {
        V12715_00 = v12715_00;
    }

    public Integer getV12714_00() {
        return V12714_00;
    }

    public void setV12714_00(Integer v12714_00) {
        V12714_00 = v12714_00;
    }

    public Integer getV12606_02() {
        return V12606_02;
    }

    public void setV12606_02(Integer v12606_02) {
        V12606_02 = v12606_02;
    }

    public Integer getV12716_02() {
        return V12716_02;
    }

    public void setV12716_02(Integer v12716_02) {
        V12716_02 = v12716_02;
    }

    public Integer getV12718_02() {
        return V12718_02;
    }

    public void setV12718_02(Integer v12718_02) {
        V12718_02 = v12718_02;
    }

    public Integer getV12717_02() {
        return V12717_02;
    }

    public void setV12717_02(Integer v12717_02) {
        V12717_02 = v12717_02;
    }

    public Integer getV12606_05() {
        return V12606_05;
    }

    public void setV12606_05(Integer v12606_05) {
        V12606_05 = v12606_05;
    }

    public Integer getV12716_05() {
        return V12716_05;
    }

    public void setV12716_05(Integer v12716_05) {
        V12716_05 = v12716_05;
    }

    public Integer getV12718_05() {
        return V12718_05;
    }

    public void setV12718_05(Integer v12718_05) {
        V12718_05 = v12718_05;
    }

    public Integer getV12717_05() {
        return V12717_05;
    }

    public void setV12717_05(Integer v12717_05) {
        V12717_05 = v12717_05;
    }

    public Integer getV12606_10() {
        return V12606_10;
    }

    public void setV12606_10(Integer v12606_10) {
        V12606_10 = v12606_10;
    }

    public Integer getV12716_10() {
        return V12716_10;
    }

    public void setV12716_10(Integer v12716_10) {
        V12716_10 = v12716_10;
    }

    public Integer getV12718_10() {
        return V12718_10;
    }

    public void setV12718_10(Integer v12718_10) {
        V12718_10 = v12718_10;
    }

    public Integer getV12717_10() {
        return V12717_10;
    }

    public void setV12717_10(Integer v12717_10) {
        V12717_10 = v12717_10;
    }

    public Integer getV12606_15() {
        return V12606_15;
    }

    public void setV12606_15(Integer v12606_15) {
        V12606_15 = v12606_15;
    }

    public Integer getV12716_15() {
        return V12716_15;
    }

    public void setV12716_15(Integer v12716_15) {
        V12716_15 = v12716_15;
    }

    public Integer getV12718_15() {
        return V12718_15;
    }

    public void setV12718_15(Integer v12718_15) {
        V12718_15 = v12718_15;
    }

    public Integer getV12717_15() {
        return V12717_15;
    }

    public void setV12717_15(Integer v12717_15) {
        V12717_15 = v12717_15;
    }

    public Integer getV12606_30() {
        return V12606_30;
    }

    public void setV12606_30(Integer v12606_30) {
        V12606_30 = v12606_30;
    }

    public Integer getV12716_30() {
        return V12716_30;
    }

    public void setV12716_30(Integer v12716_30) {
        V12716_30 = v12716_30;
    }

    public Integer getV12718_30() {
        return V12718_30;
    }

    public void setV12718_30(Integer v12718_30) {
        V12718_30 = v12718_30;
    }

    public Integer getV12717_30() {
        return V12717_30;
    }

    public void setV12717_30(Integer v12717_30) {
        V12717_30 = v12717_30;
    }

    public Integer getV12606_40() {
        return V12606_40;
    }

    public void setV12606_40(Integer v12606_40) {
        V12606_40 = v12606_40;
    }

    public Integer getV12716_40() {
        return V12716_40;
    }

    public void setV12716_40(Integer v12716_40) {
        V12716_40 = v12716_40;
    }

    public Integer getV12718_40() {
        return V12718_40;
    }

    public void setV12718_40(Integer v12718_40) {
        V12718_40 = v12718_40;
    }

    public Integer getV12717_40() {
        return V12717_40;
    }

    public void setV12717_40(Integer v12717_40) {
        V12717_40 = v12717_40;
    }

    public Integer getV12705_0() {
        return V12705_0;
    }

    public void setV12705_0(Integer v12705_0) {
        V12705_0 = v12705_0;
    }

    public Integer getV12705_00() {
        return V12705_00;
    }

    public void setV12705_00(Integer v12705_00) {
        V12705_00 = v12705_00;
    }

    public Integer getV12703_00() {
        return V12703_00;
    }

    public void setV12703_00(Integer v12703_00) {
        V12703_00 = v12703_00;
    }

    public Integer getV12706_00() {
        return V12706_00;
    }

    public void setV12706_00(Integer v12706_00) {
        V12706_00 = v12706_00;
    }

    public Integer getV12704_00() {
        return V12704_00;
    }

    public void setV12704_00(Integer v12704_00) {
        V12704_00 = v12704_00;
    }

    public Integer getV12700_00() {
        return V12700_00;
    }

    public void setV12700_00(Integer v12700_00) {
        V12700_00 = v12700_00;
    }

    public Integer getV12701_00() {
        return V12701_00;
    }

    public void setV12701_00(Integer v12701_00) {
        V12701_00 = v12701_00;
    }

    public Integer getV12702_00() {
        return V12702_00;
    }

    public void setV12702_00(Integer v12702_00) {
        V12702_00 = v12702_00;
    }

    public Integer getV12705_5() {
        return V12705_5;
    }

    public void setV12705_5(Integer v12705_5) {
        V12705_5 = v12705_5;
    }

    public Integer getV12705_05() {
        return V12705_05;
    }

    public void setV12705_05(Integer v12705_05) {
        V12705_05 = v12705_05;
    }

    public Integer getV12703_05() {
        return V12703_05;
    }

    public void setV12703_05(Integer v12703_05) {
        V12703_05 = v12703_05;
    }

    public Integer getV12706_05() {
        return V12706_05;
    }

    public void setV12706_05(Integer v12706_05) {
        V12706_05 = v12706_05;
    }

    public Integer getV12704_05() {
        return V12704_05;
    }

    public void setV12704_05(Integer v12704_05) {
        V12704_05 = v12704_05;
    }

    public Integer getV12700_05() {
        return V12700_05;
    }

    public void setV12700_05(Integer v12700_05) {
        V12700_05 = v12700_05;
    }

    public Integer getV12701_05() {
        return V12701_05;
    }

    public void setV12701_05(Integer v12701_05) {
        V12701_05 = v12701_05;
    }

    public Integer getV12702_05() {
        return V12702_05;
    }

    public void setV12702_05(Integer v12702_05) {
        V12702_05 = v12702_05;
    }

    public Integer getV12705_10C() {
        return V12705_10C;
    }

    public void setV12705_10C(Integer v12705_10C) {
        V12705_10C = v12705_10C;
    }

    public Integer getV12705_10() {
        return V12705_10;
    }

    public void setV12705_10(Integer v12705_10) {
        V12705_10 = v12705_10;
    }

    public Integer getV12703_10() {
        return V12703_10;
    }

    public void setV12703_10(Integer v12703_10) {
        V12703_10 = v12703_10;
    }

    public Integer getV12706_10() {
        return V12706_10;
    }

    public void setV12706_10(Integer v12706_10) {
        V12706_10 = v12706_10;
    }

    public Integer getV12704_10() {
        return V12704_10;
    }

    public void setV12704_10(Integer v12704_10) {
        V12704_10 = v12704_10;
    }

    public Integer getV12700_10() {
        return V12700_10;
    }

    public void setV12700_10(Integer v12700_10) {
        V12700_10 = v12700_10;
    }

    public Integer getV12701_10() {
        return V12701_10;
    }

    public void setV12701_10(Integer v12701_10) {
        V12701_10 = v12701_10;
    }

    public Integer getV12702_10() {
        return V12702_10;
    }

    public void setV12702_10(Integer v12702_10) {
        V12702_10 = v12702_10;
    }

    public Integer getV12705_15C() {
        return V12705_15C;
    }

    public void setV12705_15C(Integer v12705_15C) {
        V12705_15C = v12705_15C;
    }

    public Integer getV12705_15() {
        return V12705_15;
    }

    public void setV12705_15(Integer v12705_15) {
        V12705_15 = v12705_15;
    }

    public Integer getV12703_15() {
        return V12703_15;
    }

    public void setV12703_15(Integer v12703_15) {
        V12703_15 = v12703_15;
    }

    public Integer getV12706_15() {
        return V12706_15;
    }

    public void setV12706_15(Integer v12706_15) {
        V12706_15 = v12706_15;
    }

    public Integer getV12704_15() {
        return V12704_15;
    }

    public void setV12704_15(Integer v12704_15) {
        V12704_15 = v12704_15;
    }

    public Integer getV12700_15() {
        return V12700_15;
    }

    public void setV12700_15(Integer v12700_15) {
        V12700_15 = v12700_15;
    }

    public Integer getV12701_15() {
        return V12701_15;
    }

    public void setV12701_15(Integer v12701_15) {
        V12701_15 = v12701_15;
    }

    public Integer getV12702_15() {
        return V12702_15;
    }

    public void setV12702_15(Integer v12702_15) {
        V12702_15 = v12702_15;
    }

    public Integer getV12705_20C() {
        return V12705_20C;
    }

    public void setV12705_20C(Integer v12705_20C) {
        V12705_20C = v12705_20C;
    }

    public Integer getV12705_20() {
        return V12705_20;
    }

    public void setV12705_20(Integer v12705_20) {
        V12705_20 = v12705_20;
    }

    public Integer getV12703_20() {
        return V12703_20;
    }

    public void setV12703_20(Integer v12703_20) {
        V12703_20 = v12703_20;
    }

    public Integer getV12706_20() {
        return V12706_20;
    }

    public void setV12706_20(Integer v12706_20) {
        V12706_20 = v12706_20;
    }

    public Integer getV12704_20() {
        return V12704_20;
    }

    public void setV12704_20(Integer v12704_20) {
        V12704_20 = v12704_20;
    }

    public Integer getV12700_20() {
        return V12700_20;
    }

    public void setV12700_20(Integer v12700_20) {
        V12700_20 = v12700_20;
    }

    public Integer getV12701_20() {
        return V12701_20;
    }

    public void setV12701_20(Integer v12701_20) {
        V12701_20 = v12701_20;
    }

    public Integer getV12702_20() {
        return V12702_20;
    }

    public void setV12702_20(Integer v12702_20) {
        V12702_20 = v12702_20;
    }

    public Integer getV12705_22C() {
        return V12705_22C;
    }

    public void setV12705_22C(Integer v12705_22C) {
        V12705_22C = v12705_22C;
    }

    public Integer getV12705_22() {
        return V12705_22;
    }

    public void setV12705_22(Integer v12705_22) {
        V12705_22 = v12705_22;
    }

    public Integer getV12703_22() {
        return V12703_22;
    }

    public void setV12703_22(Integer v12703_22) {
        V12703_22 = v12703_22;
    }

    public Integer getV12706_22() {
        return V12706_22;
    }

    public void setV12706_22(Integer v12706_22) {
        V12706_22 = v12706_22;
    }

    public Integer getV12704_22() {
        return V12704_22;
    }

    public void setV12704_22(Integer v12704_22) {
        V12704_22 = v12704_22;
    }

    public Integer getV12700_22() {
        return V12700_22;
    }

    public void setV12700_22(Integer v12700_22) {
        V12700_22 = v12700_22;
    }

    public Integer getV12701_22() {
        return V12701_22;
    }

    public void setV12701_22(Integer v12701_22) {
        V12701_22 = v12701_22;
    }

    public Integer getV12702_22() {
        return V12702_22;
    }

    public void setV12702_22(Integer v12702_22) {
        V12702_22 = v12702_22;
    }

    public Integer getV12610_26() {
        return V12610_26;
    }

    public void setV12610_26(Integer v12610_26) {
        V12610_26 = v12610_26;
    }

    public Integer getV12611_18() {
        return V12611_18;
    }

    public void setV12611_18(Integer v12611_18) {
        V12611_18 = v12611_18;
    }

    public Float getV13004_701() {
        return V13004_701;
    }

    public void setV13004_701(Float v13004_701) {
        V13004_701 = v13004_701;
    }

    public Float getBalltemp_avg() {
        return balltemp_avg;
    }

    public void setBalltemp_avg(Float balltemp_avg) {
        this.balltemp_avg = balltemp_avg;
    }

    public Float getV13004_MAX() {
        return V13004_MAX;
    }

    public void setV13004_MAX(Float v13004_MAX) {
        V13004_MAX = v13004_MAX;
    }

    public Integer getV13004_MAX_M() {
        return V13004_MAX_M;
    }

    public void setV13004_MAX_M(Integer v13004_MAX_M) {
        V13004_MAX_M = v13004_MAX_M;
    }

    public Integer getV13004_MAX_D() {
        return V13004_MAX_D;
    }

    public void setV13004_MAX_D(Integer v13004_MAX_D) {
        V13004_MAX_D = v13004_MAX_D;
    }

    public Float getV13004_MIN() {
        return V13004_MIN;
    }

    public void setV13004_MIN(Float v13004_MIN) {
        V13004_MIN = v13004_MIN;
    }

    public Integer getV13004_MIN_M() {
        return V13004_MIN_M;
    }

    public void setV13004_MIN_M(Integer v13004_MIN_M) {
        V13004_MIN_M = v13004_MIN_M;
    }

    public Integer getV13004_MIN_D() {
        return V13004_MIN_D;
    }

    public void setV13004_MIN_D(Integer v13004_MIN_D) {
        V13004_MIN_D = v13004_MIN_D;
    }

    public Integer getV13003_701() {
        return V13003_701;
    }

    public void setV13003_701(Integer v13003_701) {
        V13003_701 = v13003_701;
    }

    public Integer getV13007() {
        return V13007;
    }

    public void setV13007(Integer v13007) {
        V13007 = v13007;
    }

    public Integer getV13007_040() {
        return V13007_040;
    }

    public void setV13007_040(Integer v13007_040) {
        V13007_040 = v13007_040;
    }

    public String getV13007_067() {
        return V13007_067;
    }

    public void setV13007_067(String v13007_067) {
        V13007_067 = v13007_067;
    }

    public Integer getV20010_701() {
        return V20010_701;
    }

    public void setV20010_701(Integer v20010_701) {
        V20010_701 = v20010_701;
    }

    public Integer getV20051_701() {
        return V20051_701;
    }

    public void setV20051_701(Integer v20051_701) {
        V20051_701 = v20051_701;
    }

    public Integer getV20501_02() {
        return V20501_02;
    }

    public void setV20501_02(Integer v20501_02) {
        V20501_02 = v20501_02;
    }

    public Integer getV20500_08() {
        return V20500_08;
    }

    public void setV20500_08(Integer v20500_08) {
        V20500_08 = v20500_08;
    }

    public Integer getV20503_02() {
        return V20503_02;
    }

    public void setV20503_02(Integer v20503_02) {
        V20503_02 = v20503_02;
    }

    public Integer getV20502_08() {
        return V20502_08;
    }

    public void setV20502_08(Integer v20502_08) {
        V20502_08 = v20502_08;
    }

    public Float getV13305() {
        return V13305;
    }

    public void setV13305(Float v13305) {
        V13305 = v13305;
    }

    public Float getV13306() {
        return V13306;
    }

    public void setV13306(Float v13306) {
        V13306 = v13306;
    }

    public Float getV13305_SD() {
        return V13305_SD;
    }

    public void setV13305_SD(Float v13305_SD) {
        V13305_SD = v13305_SD;
    }

    public Float getV13306_SD() {
        return V13306_SD;
    }

    public void setV13306_SD(Float v13306_SD) {
        V13306_SD = v13306_SD;
    }

    public Float getV13052() {
        return V13052;
    }

    public void setV13052(Float v13052) {
        V13052 = v13052;
    }

    public Integer getV13052_040() {
        return V13052_040;
    }

    public void setV13052_040(Integer v13052_040) {
        V13052_040 = v13052_040;
    }

    public String getV13052_067() {
        return V13052_067;
    }

    public void setV13052_067(String v13052_067) {
        V13052_067 = v13052_067;
    }

    public Integer getV13353() {
        return V13353;
    }

    public void setV13353(Integer v13353) {
        V13353 = v13353;
    }

    public Integer getV13355_001() {
        return V13355_001;
    }

    public void setV13355_001(Integer v13355_001) {
        V13355_001 = v13355_001;
    }

    public Integer getV13355_005() {
        return V13355_005;
    }

    public void setV13355_005(Integer v13355_005) {
        V13355_005 = v13355_005;
    }

    public Integer getV13355_010() {
        return V13355_010;
    }

    public void setV13355_010(Integer v13355_010) {
        V13355_010 = v13355_010;
    }

    public Integer getV13355_025() {
        return V13355_025;
    }

    public void setV13355_025(Integer v13355_025) {
        V13355_025 = v13355_025;
    }

    public Integer getV13355_050() {
        return V13355_050;
    }

    public void setV13355_050(Integer v13355_050) {
        V13355_050 = v13355_050;
    }

    public Integer getV13355_100() {
        return V13355_100;
    }

    public void setV13355_100(Integer v13355_100) {
        V13355_100 = v13355_100;
    }

    public Integer getV13355_150() {
        return V13355_150;
    }

    public void setV13355_150(Integer v13355_150) {
        V13355_150 = v13355_150;
    }

    public Integer getV13355_250() {
        return V13355_250;
    }

    public void setV13355_250(Integer v13355_250) {
        V13355_250 = v13355_250;
    }

    public Integer getV20430() {
        return V20430;
    }

    public void setV20430(Integer v20430) {
        V20430 = v20430;
    }

    public Integer getV13380() {
        return V13380;
    }

    public void setV13380(Integer v13380) {
        V13380 = v13380;
    }

    public Integer getV20438() {
        return V20438;
    }

    public void setV20438(Integer v20438) {
        V20438 = v20438;
    }

    public Integer getV20431() {
        return V20431;
    }

    public void setV20431(Integer v20431) {
        V20431 = v20431;
    }

    public Integer getV20432() {
        return V20432;
    }

    public void setV20432(Integer v20432) {
        V20432 = v20432;
    }

    public Integer getV20437() {
        return V20437;
    }

    public void setV20437(Integer v20437) {
        V20437 = v20437;
    }

    public Integer getV20433() {
        return V20433;
    }

    public void setV20433(Integer v20433) {
        V20433 = v20433;
    }

    public Integer getV13381() {
        return V13381;
    }

    public void setV13381(Integer v13381) {
        V13381 = v13381;
    }

    public Integer getV20434() {
        return V20434;
    }

    public void setV20434(Integer v20434) {
        V20434 = v20434;
    }

    public Integer getV20436() {
        return V20436;
    }

    public void setV20436(Integer v20436) {
        V20436 = v20436;
    }

    public Integer getV20435() {
        return V20435;
    }

    public void setV20435(Integer v20435) {
        V20435 = v20435;
    }

    public Float getV13302_01() {
        return V13302_01;
    }

    public void setV13302_01(Float v13302_01) {
        V13302_01 = v13302_01;
    }

    public Integer getV13302_01_040() {
        return V13302_01_040;
    }

    public void setV13302_01_040(Integer v13302_01_040) {
        V13302_01_040 = v13302_01_040;
    }

    public String getV13302_01_067() {
        return V13302_01_067;
    }

    public void setV13302_01_067(String v13302_01_067) {
        V13302_01_067 = v13302_01_067;
    }

    public Integer getV13302_01_058() {
        return V13302_01_058;
    }

    public void setV13302_01_058(Integer v13302_01_058) {
        V13302_01_058 = v13302_01_058;
    }

    public String getV13302_01_060() {
        return V13302_01_060;
    }

    public void setV13302_01_060(String v13302_01_060) {
        V13302_01_060 = v13302_01_060;
    }

    public Float getV13382_005() {
        return V13382_005;
    }

    public void setV13382_005(Float v13382_005) {
        V13382_005 = v13382_005;
    }

    public Integer getV13382_005_STIME() {
        return V13382_005_STIME;
    }

    public void setV13382_005_STIME(Integer v13382_005_STIME) {
        V13382_005_STIME = v13382_005_STIME;
    }

    public Float getV13382_010() {
        return V13382_010;
    }

    public void setV13382_010(Float v13382_010) {
        V13382_010 = v13382_010;
    }

    public Integer getV13382_010_STIME() {
        return V13382_010_STIME;
    }

    public void setV13382_010_STIME(Integer v13382_010_STIME) {
        V13382_010_STIME = v13382_010_STIME;
    }

    public Float getV13382_015() {
        return V13382_015;
    }

    public void setV13382_015(Float v13382_015) {
        V13382_015 = v13382_015;
    }

    public Integer getV13382_015_STIME() {
        return V13382_015_STIME;
    }

    public void setV13382_015_STIME(Integer v13382_015_STIME) {
        V13382_015_STIME = v13382_015_STIME;
    }

    public Float getV13382_020() {
        return V13382_020;
    }

    public void setV13382_020(Float v13382_020) {
        V13382_020 = v13382_020;
    }

    public Integer getV13382_020_STIME() {
        return V13382_020_STIME;
    }

    public void setV13382_020_STIME(Integer v13382_020_STIME) {
        V13382_020_STIME = v13382_020_STIME;
    }

    public Float getV13382_030() {
        return V13382_030;
    }

    public void setV13382_030(Float v13382_030) {
        V13382_030 = v13382_030;
    }

    public Integer getV13382_030_STIME() {
        return V13382_030_STIME;
    }

    public void setV13382_030_STIME(Integer v13382_030_STIME) {
        V13382_030_STIME = v13382_030_STIME;
    }

    public Float getV13382_045() {
        return V13382_045;
    }

    public void setV13382_045(Float v13382_045) {
        V13382_045 = v13382_045;
    }

    public Integer getV13382_045_STIME() {
        return V13382_045_STIME;
    }

    public void setV13382_045_STIME(Integer v13382_045_STIME) {
        V13382_045_STIME = v13382_045_STIME;
    }

    public Float getV13382_060() {
        return V13382_060;
    }

    public void setV13382_060(Float v13382_060) {
        V13382_060 = v13382_060;
    }

    public Integer getV13382_060_STIME() {
        return V13382_060_STIME;
    }

    public void setV13382_060_STIME(Integer v13382_060_STIME) {
        V13382_060_STIME = v13382_060_STIME;
    }

    public Float getV13382_090() {
        return V13382_090;
    }

    public void setV13382_090(Float v13382_090) {
        V13382_090 = v13382_090;
    }

    public Integer getV13382_090_STIME() {
        return V13382_090_STIME;
    }

    public void setV13382_090_STIME(Integer v13382_090_STIME) {
        V13382_090_STIME = v13382_090_STIME;
    }

    public Float getV13382_120() {
        return V13382_120;
    }

    public void setV13382_120(Float v13382_120) {
        V13382_120 = v13382_120;
    }

    public Integer getV13382_120_STIME() {
        return V13382_120_STIME;
    }

    public void setV13382_120_STIME(Integer v13382_120_STIME) {
        V13382_120_STIME = v13382_120_STIME;
    }

    public Float getV13382_180() {
        return V13382_180;
    }

    public void setV13382_180(Float v13382_180) {
        V13382_180 = v13382_180;
    }

    public Integer getV13382_180_STIME() {
        return V13382_180_STIME;
    }

    public void setV13382_180_STIME(Integer v13382_180_STIME) {
        V13382_180_STIME = v13382_180_STIME;
    }

    public Float getV13382_240() {
        return V13382_240;
    }

    public void setV13382_240(Float v13382_240) {
        V13382_240 = v13382_240;
    }

    public Integer getV13382_240_STIME() {
        return V13382_240_STIME;
    }

    public void setV13382_240_STIME(Integer v13382_240_STIME) {
        V13382_240_STIME = v13382_240_STIME;
    }

    public Float getV13382_360() {
        return V13382_360;
    }

    public void setV13382_360(Float v13382_360) {
        V13382_360 = v13382_360;
    }

    public Integer getV13382_360_STIME() {
        return V13382_360_STIME;
    }

    public void setV13382_360_STIME(Integer v13382_360_STIME) {
        V13382_360_STIME = v13382_360_STIME;
    }

    public Float getV13382_540() {
        return V13382_540;
    }

    public void setV13382_540(Float v13382_540) {
        V13382_540 = v13382_540;
    }

    public Integer getV13382_540_STIME() {
        return V13382_540_STIME;
    }

    public void setV13382_540_STIME(Integer v13382_540_STIME) {
        V13382_540_STIME = v13382_540_STIME;
    }

    public Float getV13382_720() {
        return V13382_720;
    }

    public void setV13382_720(Float v13382_720) {
        V13382_720 = v13382_720;
    }

    public Integer getV13382_720_STIME() {
        return V13382_720_STIME;
    }

    public void setV13382_720_STIME(Integer v13382_720_STIME) {
        V13382_720_STIME = v13382_720_STIME;
    }

    public Float getV13382_144() {
        return V13382_144;
    }

    public void setV13382_144(Float v13382_144) {
        V13382_144 = v13382_144;
    }

    public Integer getV13382_144_STIME() {
        return V13382_144_STIME;
    }

    public void setV13382_144_STIME(Integer v13382_144_STIME) {
        V13382_144_STIME = v13382_144_STIME;
    }

    public Integer getV04330_060() {
        return V04330_060;
    }

    public void setV04330_060(Integer v04330_060) {
        V04330_060 = v04330_060;
    }

    public Integer getV04330_089() {
        return V04330_089;
    }

    public void setV04330_089(Integer v04330_089) {
        V04330_089 = v04330_089;
    }

    public Integer getV04330_010() {
        return V04330_010;
    }

    public void setV04330_010(Integer v04330_010) {
        V04330_010 = v04330_010;
    }

    public Integer getV04330_001() {
        return V04330_001;
    }

    public void setV04330_001(Integer v04330_001) {
        V04330_001 = v04330_001;
    }

    public Integer getV04330_056() {
        return V04330_056;
    }

    public void setV04330_056(Integer v04330_056) {
        V04330_056 = v04330_056;
    }

    public Integer getV04330_048() {
        return V04330_048;
    }

    public void setV04330_048(Integer v04330_048) {
        V04330_048 = v04330_048;
    }

    public Integer getV04330_003() {
        return V04330_003;
    }

    public void setV04330_003(Integer v04330_003) {
        V04330_003 = v04330_003;
    }

    public Integer getV04330_007() {
        return V04330_007;
    }

    public void setV04330_007(Integer v04330_007) {
        V04330_007 = v04330_007;
    }

    public Integer getV04330_006() {
        return V04330_006;
    }

    public void setV04330_006(Integer v04330_006) {
        V04330_006 = v04330_006;
    }

    public Integer getV04330_005() {
        return V04330_005;
    }

    public void setV04330_005(Integer v04330_005) {
        V04330_005 = v04330_005;
    }

    public Integer getV04330_008() {
        return V04330_008;
    }

    public void setV04330_008(Integer v04330_008) {
        V04330_008 = v04330_008;
    }

    public Integer getV04330_015() {
        return V04330_015;
    }

    public void setV04330_015(Integer v04330_015) {
        V04330_015 = v04330_015;
    }

    public Integer getV04330_031() {
        return V04330_031;
    }

    public void setV04330_031(Integer v04330_031) {
        V04330_031 = v04330_031;
    }

    public Integer getV04330_042() {
        return V04330_042;
    }

    public void setV04330_042(Integer v04330_042) {
        V04330_042 = v04330_042;
    }

    public Integer getV04330_017() {
        return V04330_017;
    }

    public void setV04330_017(Integer v04330_017) {
        V04330_017 = v04330_017;
    }

    public Integer getV04330_002() {
        return V04330_002;
    }

    public void setV04330_002(Integer v04330_002) {
        V04330_002 = v04330_002;
    }

    public Integer getV04330_070() {
        return V04330_070;
    }

    public void setV04330_070(Integer v04330_070) {
        V04330_070 = v04330_070;
    }

    public Integer getV04330_016() {
        return V04330_016;
    }

    public void setV04330_016(Integer v04330_016) {
        V04330_016 = v04330_016;
    }

    public Integer getV20305_540() {
        return V20305_540;
    }

    public void setV20305_540(Integer v20305_540) {
        V20305_540 = v20305_540;
    }

    public Integer getV20309_010() {
        return V20309_010;
    }

    public void setV20309_010(Integer v20309_010) {
        V20309_010 = v20309_010;
    }

    public Integer getV20309_005() {
        return V20309_005;
    }

    public void setV20309_005(Integer v20309_005) {
        V20309_005 = v20309_005;
    }

    public Integer getV20309_001() {
        return V20309_001;
    }

    public void setV20309_001(Integer v20309_001) {
        V20309_001 = v20309_001;
    }

    public Float getV13032() {
        return V13032;
    }

    public void setV13032(Float v13032) {
        V13032 = v13032;
    }

    public Float getV13033() {
        return V13033;
    }

    public void setV13033(Float v13033) {
        V13033 = v13033;
    }

    public Integer getV13032_MAX() {
        return V13032_MAX;
    }

    public void setV13032_MAX(Integer v13032_MAX) {
        V13032_MAX = v13032_MAX;
    }

    public Integer getV13032_MAX_M() {
        return V13032_MAX_M;
    }

    public void setV13032_MAX_M(Integer v13032_MAX_M) {
        V13032_MAX_M = v13032_MAX_M;
    }

    public Integer getV13032_MAX_D() {
        return V13032_MAX_D;
    }

    public void setV13032_MAX_D(Integer v13032_MAX_D) {
        V13032_MAX_D = v13032_MAX_D;
    }

    public Integer getV13033_MAX() {
        return V13033_MAX;
    }

    public void setV13033_MAX(Integer v13033_MAX) {
        V13033_MAX = v13033_MAX;
    }

    public Integer getV13033_MAX_M() {
        return V13033_MAX_M;
    }

    public void setV13033_MAX_M(Integer v13033_MAX_M) {
        V13033_MAX_M = v13033_MAX_M;
    }

    public Integer getV13033_MAX_D() {
        return V13033_MAX_D;
    }

    public void setV13033_MAX_D(Integer v13033_MAX_D) {
        V13033_MAX_D = v13033_MAX_D;
    }

    public Float getV13334() {
        return V13334;
    }

    public void setV13334(Float v13334) {
        V13334 = v13334;
    }

    public Integer getV13334_040() {
        return V13334_040;
    }

    public void setV13334_040(Integer v13334_040) {
        V13334_040 = v13334_040;
    }

    public String getV13334_067() {
        return V13334_067;
    }

    public void setV13334_067(String v13334_067) {
        V13334_067 = v13334_067;
    }

    public Float getV13330() {
        return V13330;
    }

    public void setV13330(Float v13330) {
        V13330 = v13330;
    }

    public Integer getV13330_040() {
        return V13330_040;
    }

    public void setV13330_040(Integer v13330_040) {
        V13330_040 = v13330_040;
    }

    public String getV13330_067() {
        return V13330_067;
    }

    public void setV13330_067(String v13330_067) {
        V13330_067 = v13330_067;
    }

    public Integer getV13356_001() {
        return V13356_001;
    }

    public void setV13356_001(Integer v13356_001) {
        V13356_001 = v13356_001;
    }

    public Integer getV13356_005() {
        return V13356_005;
    }

    public void setV13356_005(Integer v13356_005) {
        V13356_005 = v13356_005;
    }

    public Integer getV13356_010() {
        return V13356_010;
    }

    public void setV13356_010(Integer v13356_010) {
        V13356_010 = v13356_010;
    }

    public Integer getV13356_020() {
        return V13356_020;
    }

    public void setV13356_020(Integer v13356_020) {
        V13356_020 = v13356_020;
    }

    public Integer getV13356_030() {
        return V13356_030;
    }

    public void setV13356_030(Integer v13356_030) {
        V13356_030 = v13356_030;
    }

    public Integer getV20440_NS() {
        return V20440_NS;
    }

    public void setV20440_NS(Integer v20440_NS) {
        V20440_NS = v20440_NS;
    }

    public Integer getV20441_NS() {
        return V20441_NS;
    }

    public void setV20441_NS(Integer v20441_NS) {
        V20441_NS = v20441_NS;
    }

    public Integer getV20442_NS() {
        return V20442_NS;
    }

    public void setV20442_NS(Integer v20442_NS) {
        V20442_NS = v20442_NS;
    }

    public Integer getV20440_WE() {
        return V20440_WE;
    }

    public void setV20440_WE(Integer v20440_WE) {
        V20440_WE = v20440_WE;
    }

    public Integer getV20441_WE() {
        return V20441_WE;
    }

    public void setV20441_WE(Integer v20441_WE) {
        V20441_WE = v20441_WE;
    }

    public Integer getV20442_WE() {
        return V20442_WE;
    }

    public void setV20442_WE(Integer v20442_WE) {
        V20442_WE = v20442_WE;
    }

    public Integer getV20440_040_NS() {
        return V20440_040_NS;
    }

    public void setV20440_040_NS(Integer v20440_040_NS) {
        V20440_040_NS = v20440_040_NS;
    }

    public String getV20440_067_NS() {
        return V20440_067_NS;
    }

    public void setV20440_067_NS(String v20440_067_NS) {
        V20440_067_NS = v20440_067_NS;
    }

    public Integer getV20440_058_NS() {
        return V20440_058_NS;
    }

    public void setV20440_058_NS(Integer v20440_058_NS) {
        V20440_058_NS = v20440_058_NS;
    }

    public String getV20440_060_NS() {
        return V20440_060_NS;
    }

    public void setV20440_060_NS(String v20440_060_NS) {
        V20440_060_NS = v20440_060_NS;
    }

    public Integer getV20440_040_WE() {
        return V20440_040_WE;
    }

    public void setV20440_040_WE(Integer v20440_040_WE) {
        V20440_040_WE = v20440_040_WE;
    }

    public String getV20440_067_WE() {
        return V20440_067_WE;
    }

    public void setV20440_067_WE(String v20440_067_WE) {
        V20440_067_WE = v20440_067_WE;
    }

    public Integer getV20440_058_WE() {
        return V20440_058_WE;
    }

    public void setV20440_058_WE(Integer v20440_058_WE) {
        V20440_058_WE = v20440_058_WE;
    }

    public String getV20440_060_WE() {
        return V20440_060_WE;
    }

    public void setV20440_060_WE(String v20440_060_WE) {
        V20440_060_WE = v20440_060_WE;
    }

    public Integer getV11291_701() {
        return V11291_701;
    }

    public void setV11291_701(Integer v11291_701) {
        V11291_701 = v11291_701;
    }

    public Float getV11042() {
        return V11042;
    }

    public void setV11042(Float v11042) {
        V11042 = v11042;
    }

    public String getV11296_CHAR() {
        return V11296_CHAR;
    }

    public void setV11296_CHAR(String v11296_CHAR) {
        V11296_CHAR = v11296_CHAR;
    }

    public Integer getV11042_040() {
        return V11042_040;
    }

    public void setV11042_040(Integer v11042_040) {
        V11042_040 = v11042_040;
    }

    public String getV11042_067() {
        return V11042_067;
    }

    public void setV11042_067(String v11042_067) {
        V11042_067 = v11042_067;
    }

    public Integer getV11042_05() {
        return V11042_05;
    }

    public void setV11042_05(Integer v11042_05) {
        V11042_05 = v11042_05;
    }

    public Integer getV11042_10() {
        return V11042_10;
    }

    public void setV11042_10(Integer v11042_10) {
        V11042_10 = v11042_10;
    }

    public Integer getV11042_12() {
        return V11042_12;
    }

    public void setV11042_12(Integer v11042_12) {
        V11042_12 = v11042_12;
    }

    public Integer getV11042_15() {
        return V11042_15;
    }

    public void setV11042_15(Integer v11042_15) {
        V11042_15 = v11042_15;
    }

    public Integer getV11042_17() {
        return V11042_17;
    }

    public void setV11042_17(Integer v11042_17) {
        V11042_17 = v11042_17;
    }

    public Integer getV11046() {
        return V11046;
    }

    public void setV11046(Integer v11046) {
        V11046 = v11046;
    }

    public String getV11211_CHAR() {
        return V11211_CHAR;
    }

    public void setV11211_CHAR(String v11211_CHAR) {
        V11211_CHAR = v11211_CHAR;
    }

    public Integer getV11046_040() {
        return V11046_040;
    }

    public void setV11046_040(Integer v11046_040) {
        V11046_040 = v11046_040;
    }

    public String getV11046_067_CHAR() {
        return V11046_067_CHAR;
    }

    public void setV11046_067_CHAR(String v11046_067_CHAR) {
        V11046_067_CHAR = v11046_067_CHAR;
    }

    public Integer getV11350_NNE() {
        return V11350_NNE;
    }

    public void setV11350_NNE(Integer v11350_NNE) {
        V11350_NNE = v11350_NNE;
    }

    public Integer getV11350_NE() {
        return V11350_NE;
    }

    public void setV11350_NE(Integer v11350_NE) {
        V11350_NE = v11350_NE;
    }

    public Integer getV11350_ENE() {
        return V11350_ENE;
    }

    public void setV11350_ENE(Integer v11350_ENE) {
        V11350_ENE = v11350_ENE;
    }

    public Integer getV11350_E() {
        return V11350_E;
    }

    public void setV11350_E(Integer v11350_E) {
        V11350_E = v11350_E;
    }

    public Integer getV11350_ESE() {
        return V11350_ESE;
    }

    public void setV11350_ESE(Integer v11350_ESE) {
        V11350_ESE = v11350_ESE;
    }

    public Integer getV11350_SE() {
        return V11350_SE;
    }

    public void setV11350_SE(Integer v11350_SE) {
        V11350_SE = v11350_SE;
    }

    public Integer getV11350_SSE() {
        return V11350_SSE;
    }

    public void setV11350_SSE(Integer v11350_SSE) {
        V11350_SSE = v11350_SSE;
    }

    public Integer getV11350_S() {
        return V11350_S;
    }

    public void setV11350_S(Integer v11350_S) {
        V11350_S = v11350_S;
    }

    public Integer getV11350_SSW() {
        return V11350_SSW;
    }

    public void setV11350_SSW(Integer v11350_SSW) {
        V11350_SSW = v11350_SSW;
    }

    public Integer getV11350_SW() {
        return V11350_SW;
    }

    public void setV11350_SW(Integer v11350_SW) {
        V11350_SW = v11350_SW;
    }

    public Integer getV11350_WSW() {
        return V11350_WSW;
    }

    public void setV11350_WSW(Integer v11350_WSW) {
        V11350_WSW = v11350_WSW;
    }

    public Integer getV11350_W() {
        return V11350_W;
    }

    public void setV11350_W(Integer v11350_W) {
        V11350_W = v11350_W;
    }

    public Integer getV11350_WNW() {
        return V11350_WNW;
    }

    public void setV11350_WNW(Integer v11350_WNW) {
        V11350_WNW = v11350_WNW;
    }

    public Integer getV11350_NW() {
        return V11350_NW;
    }

    public void setV11350_NW(Integer v11350_NW) {
        V11350_NW = v11350_NW;
    }

    public Integer getV11350_NNW() {
        return V11350_NNW;
    }

    public void setV11350_NNW(Integer v11350_NNW) {
        V11350_NNW = v11350_NNW;
    }

    public Integer getV11350_N() {
        return V11350_N;
    }

    public void setV11350_N(Integer v11350_N) {
        V11350_N = v11350_N;
    }

    public Integer getV11350_C() {
        return V11350_C;
    }

    public void setV11350_C(Integer v11350_C) {
        V11350_C = v11350_C;
    }

    public String getV11314_CHAR() {
        return V11314_CHAR;
    }

    public void setV11314_CHAR(String v11314_CHAR) {
        V11314_CHAR = v11314_CHAR;
    }

    public Integer getV11314_061() {
        return V11314_061;
    }

    public void setV11314_061(Integer v11314_061) {
        V11314_061 = v11314_061;
    }

    public String getV11315_CHAR() {
        return V11315_CHAR;
    }

    public void setV11315_CHAR(String v11315_CHAR) {
        V11315_CHAR = v11315_CHAR;
    }

    public Integer getV11315_061() {
        return V11315_061;
    }

    public void setV11315_061(Integer v11315_061) {
        V11315_061 = v11315_061;
    }

    public Integer getV11351_NNE() {
        return V11351_NNE;
    }

    public void setV11351_NNE(Integer v11351_NNE) {
        V11351_NNE = v11351_NNE;
    }

    public Integer getV11351_NE() {
        return V11351_NE;
    }

    public void setV11351_NE(Integer v11351_NE) {
        V11351_NE = v11351_NE;
    }

    public Integer getV11351_ENE() {
        return V11351_ENE;
    }

    public void setV11351_ENE(Integer v11351_ENE) {
        V11351_ENE = v11351_ENE;
    }

    public Integer getV11351_E() {
        return V11351_E;
    }

    public void setV11351_E(Integer v11351_E) {
        V11351_E = v11351_E;
    }

    public Integer getV11351_ESE() {
        return V11351_ESE;
    }

    public void setV11351_ESE(Integer v11351_ESE) {
        V11351_ESE = v11351_ESE;
    }

    public Integer getV11351_SE() {
        return V11351_SE;
    }

    public void setV11351_SE(Integer v11351_SE) {
        V11351_SE = v11351_SE;
    }

    public Integer getV11351_SSE() {
        return V11351_SSE;
    }

    public void setV11351_SSE(Integer v11351_SSE) {
        V11351_SSE = v11351_SSE;
    }

    public Integer getV11351_S() {
        return V11351_S;
    }

    public void setV11351_S(Integer v11351_S) {
        V11351_S = v11351_S;
    }

    public Integer getV11351_SSW() {
        return V11351_SSW;
    }

    public void setV11351_SSW(Integer v11351_SSW) {
        V11351_SSW = v11351_SSW;
    }

    public Integer getV11351_SW() {
        return V11351_SW;
    }

    public void setV11351_SW(Integer v11351_SW) {
        V11351_SW = v11351_SW;
    }

    public Integer getV11351_WSW() {
        return V11351_WSW;
    }

    public void setV11351_WSW(Integer v11351_WSW) {
        V11351_WSW = v11351_WSW;
    }

    public Integer getV11351_W() {
        return V11351_W;
    }

    public void setV11351_W(Integer v11351_W) {
        V11351_W = v11351_W;
    }

    public Integer getV11351_WNW() {
        return V11351_WNW;
    }

    public void setV11351_WNW(Integer v11351_WNW) {
        V11351_WNW = v11351_WNW;
    }

    public Integer getV11351_NW() {
        return V11351_NW;
    }

    public void setV11351_NW(Integer v11351_NW) {
        V11351_NW = v11351_NW;
    }

    public Integer getV11351_NNW() {
        return V11351_NNW;
    }

    public void setV11351_NNW(Integer v11351_NNW) {
        V11351_NNW = v11351_NNW;
    }

    public Integer getV11351_N() {
        return V11351_N;
    }

    public void setV11351_N(Integer v11351_N) {
        V11351_N = v11351_N;
    }

    public Integer getV11042_NNE() {
        return V11042_NNE;
    }

    public void setV11042_NNE(Integer v11042_NNE) {
        V11042_NNE = v11042_NNE;
    }

    public Integer getV11042_NE() {
        return V11042_NE;
    }

    public void setV11042_NE(Integer v11042_NE) {
        V11042_NE = v11042_NE;
    }

    public Integer getV11042_ENE() {
        return V11042_ENE;
    }

    public void setV11042_ENE(Integer v11042_ENE) {
        V11042_ENE = v11042_ENE;
    }

    public Integer getV11042_E() {
        return V11042_E;
    }

    public void setV11042_E(Integer v11042_E) {
        V11042_E = v11042_E;
    }

    public Integer getV11042_ESE() {
        return V11042_ESE;
    }

    public void setV11042_ESE(Integer v11042_ESE) {
        V11042_ESE = v11042_ESE;
    }

    public Integer getV11042_SE() {
        return V11042_SE;
    }

    public void setV11042_SE(Integer v11042_SE) {
        V11042_SE = v11042_SE;
    }

    public Integer getV11042_SSE() {
        return V11042_SSE;
    }

    public void setV11042_SSE(Integer v11042_SSE) {
        V11042_SSE = v11042_SSE;
    }

    public Integer getV11042_S() {
        return V11042_S;
    }

    public void setV11042_S(Integer v11042_S) {
        V11042_S = v11042_S;
    }

    public Integer getV11042_SSW() {
        return V11042_SSW;
    }

    public void setV11042_SSW(Integer v11042_SSW) {
        V11042_SSW = v11042_SSW;
    }

    public Integer getV11042_SW() {
        return V11042_SW;
    }

    public void setV11042_SW(Integer v11042_SW) {
        V11042_SW = v11042_SW;
    }

    public Integer getV11042_WSW() {
        return V11042_WSW;
    }

    public void setV11042_WSW(Integer v11042_WSW) {
        V11042_WSW = v11042_WSW;
    }

    public Integer getV11042_W() {
        return V11042_W;
    }

    public void setV11042_W(Integer v11042_W) {
        V11042_W = v11042_W;
    }

    public Integer getV11042_WNW() {
        return V11042_WNW;
    }

    public void setV11042_WNW(Integer v11042_WNW) {
        V11042_WNW = v11042_WNW;
    }

    public Integer getV11042_NW() {
        return V11042_NW;
    }

    public void setV11042_NW(Integer v11042_NW) {
        V11042_NW = v11042_NW;
    }

    public Integer getV11042_NNW() {
        return V11042_NNW;
    }

    public void setV11042_NNW(Integer v11042_NNW) {
        V11042_NNW = v11042_NNW;
    }

    public Integer getV11042_N() {
        return V11042_N;
    }

    public void setV11042_N(Integer v11042_N) {
        V11042_N = v11042_N;
    }

    public Integer getV12120_701() {
        return V12120_701;
    }

    public void setV12120_701(Integer v12120_701) {
        V12120_701 = v12120_701;
    }

    public Integer getV12311_701() {
        return V12311_701;
    }

    public void setV12311_701(Integer v12311_701) {
        V12311_701 = v12311_701;
    }

    public Integer getV12121_701() {
        return V12121_701;
    }

    public void setV12121_701(Integer v12121_701) {
        V12121_701 = v12121_701;
    }

    public Integer getV12620() {
        return V12620;
    }

    public void setV12620(Integer v12620) {
        V12620 = v12620;
    }

    public Integer getV12311() {
        return V12311;
    }

    public void setV12311(Integer v12311) {
        V12311 = v12311;
    }

    public Integer getV12311_040() {
        return V12311_040;
    }

    public void setV12311_040(Integer v12311_040) {
        V12311_040 = v12311_040;
    }

    public String getV12311_067() {
        return V12311_067;
    }

    public void setV12311_067(String v12311_067) {
        V12311_067 = v12311_067;
    }

    public Integer getV12121() {
        return V12121;
    }

    public void setV12121(Integer v12121) {
        V12121 = v12121;
    }

    public Integer getV12121_040() {
        return V12121_040;
    }

    public void setV12121_040(Integer v12121_040) {
        V12121_040 = v12121_040;
    }

    public String getV12121_067() {
        return V12121_067;
    }

    public void setV12121_067(String v12121_067) {
        V12121_067 = v12121_067;
    }

    public Float getV12030_701_005() {
        return V12030_701_005;
    }

    public void setV12030_701_005(Float v12030_701_005) {
        V12030_701_005 = v12030_701_005;
    }

    public Float getV12030_701_010() {
        return V12030_701_010;
    }

    public void setV12030_701_010(Float v12030_701_010) {
        V12030_701_010 = v12030_701_010;
    }

    public Float getV12030_701_015() {
        return V12030_701_015;
    }

    public void setV12030_701_015(Float v12030_701_015) {
        V12030_701_015 = v12030_701_015;
    }

    public Float getV12030_701_020() {
        return V12030_701_020;
    }

    public void setV12030_701_020(Float v12030_701_020) {
        V12030_701_020 = v12030_701_020;
    }

    public Float getV12030_701_040() {
        return V12030_701_040;
    }

    public void setV12030_701_040(Float v12030_701_040) {
        V12030_701_040 = v12030_701_040;
    }

    public Float getV12030_701_080() {
        return V12030_701_080;
    }

    public void setV12030_701_080(Float v12030_701_080) {
        V12030_701_080 = v12030_701_080;
    }

    public Float getV12030_701_160() {
        return V12030_701_160;
    }

    public void setV12030_701_160(Float v12030_701_160) {
        V12030_701_160 = v12030_701_160;
    }

    public Float getV12030_701_320() {
        return V12030_701_320;
    }

    public void setV12030_701_320(Float v12030_701_320) {
        V12030_701_320 = v12030_701_320;
    }

    public Float getV12314_701() {
        return V12314_701;
    }

    public void setV12314_701(Float v12314_701) {
        V12314_701 = v12314_701;
    }

    public Float getV12315_701() {
        return V12315_701;
    }

    public void setV12315_701(Float v12315_701) {
        V12315_701 = v12315_701;
    }

    public Float getV12316_701() {
        return V12316_701;
    }

    public void setV12316_701(Float v12316_701) {
        V12316_701 = v12316_701;
    }

    public Float getV12315() {
        return V12315;
    }

    public void setV12315(Float v12315) {
        V12315 = v12315;
    }

    public Integer getV12315_040() {
        return V12315_040;
    }

    public void setV12315_040(Integer v12315_040) {
        V12315_040 = v12315_040;
    }

    public String getV12315_067() {
        return V12315_067;
    }

    public void setV12315_067(String v12315_067) {
        V12315_067 = v12315_067;
    }

    public Float getV12316() {
        return V12316;
    }

    public void setV12316(Float v12316) {
        V12316 = v12316;
    }

    public Integer getV12316_040() {
        return V12316_040;
    }

    public void setV12316_040(Integer v12316_040) {
        V12316_040 = v12316_040;
    }

    public String getV12316_067() {
        return V12316_067;
    }

    public void setV12316_067(String v12316_067) {
        V12316_067 = v12316_067;
    }

    public Float getV20334() {
        return V20334;
    }

    public void setV20334(Float v20334) {
        V20334 = v20334;
    }

    public Integer getV20334_040() {
        return V20334_040;
    }

    public void setV20334_040(Integer v20334_040) {
        V20334_040 = v20334_040;
    }

    public String getV20334_067() {
        return V20334_067;
    }

    public void setV20334_067(String v20334_067) {
        V20334_067 = v20334_067;
    }

    public Float getV14032() {
        return V14032;
    }

    public void setV14032(Float v14032) {
        V14032 = v14032;
    }

    public Float getSunTime() {
        return sunTime;
    }

    public void setSunTime(Float sunTime) {
        this.sunTime = sunTime;
    }

    public Integer getV14033() {
        return V14033;
    }

    public void setV14033(Integer v14033) {
        V14033 = v14033;
    }

    public Integer getV20302_171() {
        return V20302_171;
    }

    public void setV20302_171(Integer v20302_171) {
        V20302_171 = v20302_171;
    }

    public Integer getV20302_172() {
        return V20302_172;
    }

    public void setV20302_172(Integer v20302_172) {
        V20302_172 = v20302_172;
    }

    public BigDecimal getV14311() {
        return v14311;
    }

    public void setV14311(BigDecimal v14311) {
        this.v14311 = v14311;
    }

    public BigDecimal getV14312() {
        return v14312;
    }

    public void setV14312(BigDecimal v14312) {
        this.v14312 = v14312;
    }

    public BigDecimal getV14311_05() {
        return v14311_05;
    }

    public void setV14311_05(BigDecimal v14311_05) {
        this.v14311_05 = v14311_05;
    }

    public Integer getV14311_05_04_002() {
        return v14311_05_04_002;
    }

    public void setV14311_05_04_002(Integer v14311_05_04_002) {
        this.v14311_05_04_002 = v14311_05_04_002;
    }

    public Integer getV14311_05_04_003() {
        return v14311_05_04_003;
    }

    public void setV14311_05_04_003(Integer v14311_05_04_003) {
        this.v14311_05_04_003 = v14311_05_04_003;
    }

    public Integer getV14311_05_04_004() {
        return v14311_05_04_004;
    }

    public void setV14311_05_04_004(Integer v14311_05_04_004) {
        this.v14311_05_04_004 = v14311_05_04_004;
    }

    public BigDecimal getV14312_05() {
        return v14312_05;
    }

    public void setV14312_05(BigDecimal v14312_05) {
        this.v14312_05 = v14312_05;
    }

    public Integer getV14312_05_04_002() {
        return v14312_05_04_002;
    }

    public void setV14312_05_04_002(Integer v14312_05_04_002) {
        this.v14312_05_04_002 = v14312_05_04_002;
    }

    public Integer getV14312_05_04_003() {
        return v14312_05_04_003;
    }

    public void setV14312_05_04_003(Integer v14312_05_04_003) {
        this.v14312_05_04_003 = v14312_05_04_003;
    }

    public Integer getV14312_05_04_004() {
        return v14312_05_04_004;
    }

    public void setV14312_05_04_004(Integer v14312_05_04_004) {
        this.v14312_05_04_004 = v14312_05_04_004;
    }
}
