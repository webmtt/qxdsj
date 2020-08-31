package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;


import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 月值表字段信息
 * @author yangkq
 * @version 1.0
 * @Date 2020/3/23
 */

public class MonthValueTab implements Serializable {
    private static final long serialVersionUID = 1L;
    private String D_RETAIN_ID;//记录标识
    private String D_DATA_ID;//资料标识 
    private String D_IYMDHM;//入库时间
    private String D_RYMDHM;//收到时间
    private String D_UPDate_TIME;//更新时间
    private String D_DateTIME;//资料时间
    private String V01301;//区站号字符
    private String V01300;//区站号数字
    private Float V05001;//纬度
    private Float V06001;//经度
    private Float V07001;//测站高度
    private Float V07031;//气压传感器海拔高度
    private Integer V02301;//台站级别 
    private Integer V_ACODE;//中国行政区划代码 
    private Integer V04001;//年 
    private Integer V04002;//月 
    private Float V10004_701;//月平均本站气压
    private Float V10301;//月极端最高本站气压
    private Integer V10301_040;//月极端最高本站气压出现日数 
    private String V10301_060_CHAR;//月极端最高本站气压出现日 
    private Float V10302;//月极端最低本站气压
    private Integer V10302_040;//月极端最低本站气压出现日数 
    private String V10302_060_CHAR;//月极端最低本站气压出现日
    private Float V10301_avg;//月平均最高本站气压
    private Float V10302_avg;//月平均最低本站气压
    private Float V10051_701;//月平均海平面气压
    private Float V12001_701;//月平均气温
    private Float V12011_701;//月平均日最高气温
    private Float V12012_701;//月平均日最低气温
    private Float V12011;//本月极端最高气温
    private Integer V12011_040;//本月极端最高气温出现日数 
    private String V12011_060_CHAR;//极端最高气温出现日 
    private Float V12012;//本月极端最低气温
    private Integer V12012_040;//本月极端最低气温出现日数 
    private String V12012_060_CHAR;//极端最低气温出现日 
    private Float V12303_701;//月平均气温日较差
    private Float V12304;//月最大气温日较差
    private Integer V12304_040;//月最大气温日较差出现日数 
    private String V12304_060_CHAR;//月最大气温日较差出现日 
    private Float V12305;//月最小气温日较差
    private Integer V12305_040;//月最小气温日较差出现日数 
    private String V12305_060_CHAR;//月最小气温日较差出现日 
    private Integer V12605_30;//日最高气温≥30℃日数 
    private Integer V12605_35;//日最高气温≥35℃日数 
    private Integer V12605_40;//日最高气温≥40℃日数 
    private Integer V12607_02;//日最低气温＜2℃日数 
    private Integer V12603;//日最低气温＜0℃日数 
    private Integer V12606_02;//日最低气温＜-2℃日数
    private Integer V12606_05;//日最低气温＜-5℃日数 ( 单位：日 )
    private Integer V12606_10;//日最低气温＜-10℃日数 ( 单位：日 )
    private Integer V12606_15;//日最低气温＜-15℃日数 
    private Integer V12606_30;//日最低气温＜-30℃日数 
    private Integer V12606_40;//日最低气温＜-40℃日数 
    private Integer V12610_26;//冷度日数（日平均气温＞26.0℃） 
    private Integer V12611_18;//暖度日数（日平均气温＜18.0℃）
    private Integer V13003_701;//月平均相对湿度
    private Integer V13007;//月最小相对湿度 
    private Integer V13007_040;//月最小相对湿度出现日数 
    private String V13007_060_CHAR;//月最小相对湿度出现日 
    private Integer V20010_701;//月平均总云量 
    private Integer V20051_701;//月平均低云量 
    private Integer V20501_02;//日平均总云量< 2.0日数 
    private Integer V20500_08;//日平均总云量> 8.0日数 
    private Integer V20503_02;//日平均低云量< 2.0日数 
    private Integer V20502_08;//日平均低云量> 8.0日数 
    private Float V13305;//20-20时月总降水量
    private Float V13306;//08-08时月总降水量
    private Float V13052;//月最大日降水量
    private Integer V13052_040;//月最大日降水量出现日数 
    private String V13052_060_CHAR;//月最大日降水量出现日 
    private Integer V13353;//日总降水量≥0.1mm日数
    private Integer V13355_001;//日总降水量≥1mm日数
    private Integer V13355_005;//日总降水量≥5mm日数
    private Integer V13355_010;//日总降水量≥10mm日数
    private Integer V13355_025;//日总降水量≥25mm日数
    private Integer V13355_050;//日总降水量≥50mm日数
    private Integer V13355_100;//日总降水量≥100mm日数
    private Integer V13355_150;//日总降水量≥150mm日数
    private Integer V13355_250;//日降水量≥250mm日数 ( 单位：日 )
    private Integer V20430;//月最长连续降水日数 
    private Integer V13380;//月最长连续降水量 
    private Integer V20431;//月最长连续降水止日 
    private Integer V20432;//月最长连续无降水日数 
    private Integer V20433;//月最长连续无降水止日 
    private Integer V13381;//月最大连续降水量 
    private Integer V20434;//月最大连续降水日数 
    private Integer V20435;//月最大连续降水止日 
    private Integer V13302_01;//1小时最大降水量 
    private Integer V13302_01_040;//1小时最大降水量出现日数 
    private String V13302_01_060_CHAR;//1小时最大降水量出现日 
    private Integer V04330_089;//冰雹日数 
    private Integer V04330_007;//扬沙日数 
    private Integer V04330_006;//浮尘日数 
    private Integer V04330_005;//霾日数 
    private Integer V04330_008;//龙卷风日数 
    private Integer V04330_015;//大风日数 
    private Integer V04330_031;//沙尘暴日数 
    private Integer V04330_042;//雾日数 
    private Integer V04330_017;//雷暴日数 
    private Integer V04330_002;//霜日数 
    private Integer V04330_070;//降雪日数 
    private Integer V04330_016;//积雪日数 
    private Integer V20305_540;//电线积冰（雨凇+雾凇）日数
    private Integer V20309_010;//能见度≤10km出现频率
    private Integer V20309_001;//能见度≤1km出现频率
    private Integer V20309_005;//能见度≤5km出现频率
    private Integer V20309_008;//月能见度<800米出现次数
    private Integer V20309_100;//能见度≤1km出现次数
    private Integer V20309_150;//能见度≤1.5km出现次数
    private Integer V20309_300;//能见度≤3km出现次数
    private Integer V20309_011;//能见度>1km出现次数
    private Float V13032;//月总蒸发量小型
    private Float V13033;//月总蒸发量大型
    private Float V13334;//最大积雪深度
    private Integer V13334_040;//最大积雪深度日数 
    private String V13334_060_CHAR;//最大积雪深度出现日 
    private Float V13330;//最大雪压
    private Integer V13330_040;//最大雪压出现日数 
    private String V13330_060;//最大雪压出现日 
    private Integer V13356_001;//积雪深度≥1cm日数 
    private Integer V13356_005;//积雪深度≥5cm日数 
    private Integer V13356_010;//积雪深度≥10cm日数 
    private Integer V13356_020;//积雪深度≥20cm日数 
    private Integer V13356_030;//积雪深度≥30cm日数
    private Float V20326_NS;//电线积冰-南北方向最大重量的相应直径( 单位：毫米 )
    private Float V20306_NS;//电线积冰-南北方向最大重量的相应厚度( 单位：毫米 )
    private Float V20307_NS;//电线积冰-南北方向最大重量 ( 单位：克 )
    private Float V20326_WE;//电线积冰-东西方向最大重量的相应直径( 单位：毫米 )
    private Float V20306_WE;//电线积冰-东西方向最大重量的相应厚度 ( 单位：毫米 )
    private Float V20307_WE;//电线积冰-东西方向最大重量 ( 单位：克 )
    private Integer V20440_040_NS;//电线积冰-南北最大重量出现日数
    private String V20440_060_NS_CHAR;//电线积冰-南北最大重量出现日
    private Integer V20440_040_WE;//电线积冰-东西最大重量出现日数
    private String V20440_060_WE_CHAR;//电线积冰-东西最大重量出现日
    private Integer V11291_701;//月平均风速（2分钟） 
    private Float V11042;//月最大风速
    private String V11296_CHAR;//最大风速的风向 
    private Integer V11042_040;//最大风速出现日数 
    private String V11042_060_CHAR;//月最大风速之出现日
    private Integer V11042_05;//日最大风速≥5.0m/s日数 
    private Integer V11042_10;//日最大风速≥10.0m/s日数 
    private Integer V11042_12;//日最大风速≥12.0m/s日数 
    private Integer V11042_15;//日最大风速≥15.0m/s日数 
    private Integer V11042_17;//日最大风速≥17.0m/s日数 
    private Integer V11046;//月极大风速 
    private String V11211;//月极大风速之风向
    private Integer V11046_040;//月极大风速出现日数 
    private String V11046_060_CHAR;//月极大风速之出现日 
    private Integer V11350_NNE;//NNE风向出现频率 
    private Integer V11350_NE;//NE风向出现频率 
    private Integer V11350_ENE;//ENE风向出现频率 
    private Integer V11350_E;//E风向出现频率 
    private Integer V11350_ESE;//ESE风向出现频率 
    private Integer V11350_SE;//SE风向出现频率 
    private Integer V11350_SSE;//SSE风向出现频率 
    private Integer V11350_S;//S风向出现频率 
    private Integer V11350_SSW;//SSW风向出现频率 
    private Integer V11350_SW;//SW风向出现频率 
    private Integer V11350_WSW;//WSW风向出现频率 
    private Integer V11350_W;//W风向出现频率 
    private Integer V11350_WNW;//WNW风向出现频率 
    private Integer V11350_NW;//NW风向出现频率 
    private Integer V11350_NNW;//NNW风向出现频率 
    private Integer V11350_N;//N风向出现频率 
    private Integer V11350_C;//C风向（静风）出现频率 
    private String V11314_CHAR;//月最多风向 
    private Integer V11314_061;//月最多风向出现频率 
    private String V11315_CHAR;//月次多风向 
    private Integer V11315_061;//月次多风向出现频率 
    private Integer V11351_NNE;//NNE风的平均风速 
    private Integer V11351_NE;//NE风的平均风速 
    private Integer V11351_ENE;//ENE的平均风速 
    private Integer V11351_E;//E风的平均风速 
    private Integer V11351_ESE;//ESE风的平均风速 
    private Integer V11351_SE;//SE风的平均风速 
    private Integer V11351_SSE;//SSE风的平均风速 
    private Integer V11351_S;//S风的平均风速 
    private Integer V11351_SSW;//SSW风的平均风速 
    private Integer V11351_SW;//SW风的平均风速 
    private Integer V11351_WSW;//WSW风的平均风速 
    private Integer V11351_W;//W风的平均风速 
    private Integer V11351_WNW;//WNW风的平均风速 
    private Integer V11351_NW;//NW风的平均风速 
    private Integer V11351_NNW;//NNW风的平均风速 
    private Integer V11351_N;//N风的平均风速 
    private Integer V11042_NNE;//NNE风的最大风速 
    private Integer V11042_NE;//NE风的最大风速 
    private Integer V11042_ENE;//ENE的最大风速 
    private Integer V11042_E;//E风的最大风速 
    private Integer V11042_ESE;//ESE风的最大风速 
    private Integer V11042_SE;//SE风的最大风速 
    private Integer V11042_SSE;//SSE风的最大风速 
    private Integer V11042_S;//S风的最大风速 
    private Integer V11042_SSW;//SSW风的最大风速 
    private Integer V11042_SW;//SW风的最大风速 
    private Integer V11042_WSW;//WSW风的最大风速 
    private Integer V11042_W;//W风的最大风速 
    private Integer V11042_WNW;//WNW风的最大风速 
    private Integer V11042_NW;//NW风的最大风速 
    private Integer V11042_NNW;//NNW风的最大风速 
    private Integer V11042_N;//N风的最大风速 
    private Float V12120_701;//月平均地面温度
    private Float V12311_701;//月平均最高地面温度
    private Float V12121_701;//月平均最低地面温度
    private Float V12620;//月内日最低地面温度≤0.0℃日数
    private Float V12311;//月极端最高地面温度
    private Integer V12311_040;//月极端最高地面温度出现日数 
    private String V12311_060_CHAR;//月极端最高地面温度出现日 
    private Float V12121;//月极端最低地面温度
    private Integer V12121_040;//月极端最低地面温度出现日数 
    private String V12121_060_CHAR;//月极端最低地面温度出现日 
    private Float V12030_701_005;//月平均5cm地温
    private Float V12030_701_010;//月平均10cm地温
    private Float V12030_701_015;//月平均15cm地温
    private Float V12030_701_020;//月平均20cm地温
    private Float V12030_701_040;//月平均40cm地温
    private Float V12030_701_080;//月平均80cm地温
    private Float V12030_701_160;//月平均160cm地温
    private Float V12030_701_320;//月平均320cm地温
    private Float balltemp_avg;//月平均湿球温度
    private Float V13004_701;//月平均水汽压
    private Float V13004_MAX;//月最大水汽压
    private Integer V13004_MAX_D;//月最大水汽压出现日期
    private Float V13004_MIN;//月最小水气压
    private Integer V13004_MIN_D;// 月最小水汽压出现日期
    private Float V12314_701;//平均草面（雪面）温度
    private Float V12315_701;//平均最高草面（雪面）温度
    private Float V12316_701;//平均最低草面（雪面）温度
    private Float V12315;//极端最高草面（雪面）温度
    private Integer V12315_040;//极端最高草面（雪面）温度出现日数 
    private String V12315_060_CHAR;//极端最高草面（雪面）温度出现日 
    private Float V12316;//极端最低草面（雪面）温度
    private Integer V12316_040;//极端最低草面（雪面）温度出现日数 
    private String V12316_060_CHAR;//极端最低草面（雪面）温度出现日 
    private Float V20334;//最大冻土深度
    private Integer V20334_040;//最大冻土深度出现日数 
    private String V20334_060_CHAR;//最大冻土深度出现日 
    private Float V14032;//月日照总时数
    private Integer sunTime;//月日出总时长
    private Integer V14033;//月日照百分率 
    private Integer V20302_171;//日照百分率≥60%日数
    private Integer V20302_172;//日照百分率≤20%日数
    private BigDecimal v14311;//月太阳总辐射(MJ/m2)
    private BigDecimal v14311_05;// 月最大日总辐射辐照度(W/m2)
    private Integer v14021_05_052;//月最大日总辐射辐照度时间
    private BigDecimal v14312;//月净全辐射(MJ/m2)
    private BigDecimal v14312_05;//月最大日净全辐射辐照度(W/m2)
    private Integer v14312_05_052;//月最大日净全辐射辐照度时间
    private BigDecimal v14314_avg;// 月日散射辐射曝辐量平均
    private BigDecimal v14314;// 月日散射辐射曝辐量合计
    private BigDecimal v14314_05;//最大日散射辐射辐照度
    private BigDecimal v14322_avg;//月日直接辐射曝辐量平均
    private BigDecimal v14322;//月日直接辐射曝辐量合计

    public String getD_RECORD_ID() {
        return D_RETAIN_ID;
    }

    public void setD_RECORD_ID(String d_RECORD_ID) {
        D_RETAIN_ID = d_RECORD_ID;
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

    public String getD_UPDate_TIME() {
        return D_UPDate_TIME;
    }

    public void setD_UPDate_TIME(String d_UPDate_TIME) {
        D_UPDate_TIME = d_UPDate_TIME;
    }

    public String getD_DateTIME() {
        return D_DateTIME;
    }

    public void setD_DateTIME(String d_DateTIME) {
        D_DateTIME = d_DateTIME;
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

    public Integer getV04002() {
        return V04002;
    }

    public void setV04002(Integer v04002) {
        V04002 = v04002;
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

    public String getV10301_060_CHAR() {
        return V10301_060_CHAR;
    }

    public void setV10301_060_CHAR(String v10301_060_CHAR) {
        V10301_060_CHAR = v10301_060_CHAR;
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

    public String getV10302_060_CHAR() {
        return V10302_060_CHAR;
    }

    public void setV10302_060_CHAR(String v10302_060_CHAR) {
        V10302_060_CHAR = v10302_060_CHAR;
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

    public Float getV10051_701() {
        return V10051_701;
    }

    public void setV10051_701(Float v10051_701) {
        V10051_701 = v10051_701;
    }

    public Float getV12001_701() {
        return V12001_701;
    }

    public void setV12001_701(Float v12001_701) {
        V12001_701 = v12001_701;
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

    public String getV12011_060_CHAR() {
        return V12011_060_CHAR;
    }

    public void setV12011_060_CHAR(String v12011_060_CHAR) {
        V12011_060_CHAR = v12011_060_CHAR;
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

    public String getV12012_060_CHAR() {
        return V12012_060_CHAR;
    }

    public void setV12012_060_CHAR(String v12012_060_CHAR) {
        V12012_060_CHAR = v12012_060_CHAR;
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

    public String getV12304_060_CHAR() {
        return V12304_060_CHAR;
    }

    public void setV12304_060_CHAR(String v12304_060_CHAR) {
        V12304_060_CHAR = v12304_060_CHAR;
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

    public String getV12305_060_CHAR() {
        return V12305_060_CHAR;
    }

    public void setV12305_060_CHAR(String v12305_060_CHAR) {
        V12305_060_CHAR = v12305_060_CHAR;
    }

    public Integer getV12605_30() {
        return V12605_30;
    }

    public void setV12605_30(Integer v12605_30) {
        V12605_30 = v12605_30;
    }

    public Integer getV12605_35() {
        return V12605_35;
    }

    public void setV12605_35(Integer v12605_35) {
        V12605_35 = v12605_35;
    }

    public Integer getV12605_40() {
        return V12605_40;
    }

    public void setV12605_40(Integer v12605_40) {
        V12605_40 = v12605_40;
    }

    public Integer getV12607_02() {
        return V12607_02;
    }

    public void setV12607_02(Integer v12607_02) {
        V12607_02 = v12607_02;
    }

    public Integer getV12603() {
        return V12603;
    }

    public void setV12603(Integer v12603) {
        V12603 = v12603;
    }

    public Integer getV12606_02() {
        return V12606_02;
    }

    public void setV12606_02(Integer v12606_02) {
        V12606_02 = v12606_02;
    }

    public Integer getV12606_05() {
        return V12606_05;
    }

    public void setV12606_05(Integer v12606_05) {
        V12606_05 = v12606_05;
    }

    public Integer getV12606_10() {
        return V12606_10;
    }

    public void setV12606_10(Integer v12606_10) {
        V12606_10 = v12606_10;
    }

    public Integer getV12606_15() {
        return V12606_15;
    }

    public void setV12606_15(Integer v12606_15) {
        V12606_15 = v12606_15;
    }

    public Integer getV12606_30() {
        return V12606_30;
    }

    public void setV12606_30(Integer v12606_30) {
        V12606_30 = v12606_30;
    }

    public Integer getV12606_40() {
        return V12606_40;
    }

    public void setV12606_40(Integer v12606_40) {
        V12606_40 = v12606_40;
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

    public String getV13007_060_CHAR() {
        return V13007_060_CHAR;
    }

    public void setV13007_060_CHAR(String v13007_060_CHAR) {
        V13007_060_CHAR = v13007_060_CHAR;
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

    public String getV13052_060_CHAR() {
        return V13052_060_CHAR;
    }

    public void setV13052_060_CHAR(String v13052_060_CHAR) {
        V13052_060_CHAR = v13052_060_CHAR;
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

    public Integer getV20435() {
        return V20435;
    }

    public void setV20435(Integer v20435) {
        V20435 = v20435;
    }

    public Integer getV13302_01() {
        return V13302_01;
    }

    public void setV13302_01(Integer v13302_01) {
        V13302_01 = v13302_01;
    }

    public Integer getV13302_01_040() {
        return V13302_01_040;
    }

    public void setV13302_01_040(Integer v13302_01_040) {
        V13302_01_040 = v13302_01_040;
    }

    public String getV13302_01_060_CHAR() {
        return V13302_01_060_CHAR;
    }

    public void setV13302_01_060_CHAR(String v13302_01_060_CHAR) {
        V13302_01_060_CHAR = v13302_01_060_CHAR;
    }

    public Integer getV04330_089() {
        return V04330_089;
    }

    public void setV04330_089(Integer v04330_089) {
        V04330_089 = v04330_089;
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

    public Integer getV20309_001() {
        return V20309_001;
    }

    public void setV20309_001(Integer v20309_001) {
        V20309_001 = v20309_001;
    }

    public Integer getV20309_005() {
        return V20309_005;
    }

    public void setV20309_005(Integer v20309_005) {
        V20309_005 = v20309_005;
    }

    public Integer getV20309_008() {
        return V20309_008;
    }

    public void setV20309_008(Integer v20309_008) {
        V20309_008 = v20309_008;
    }

    public Integer getV20309_100() {
        return V20309_100;
    }

    public void setV20309_100(Integer v20309_100) {
        V20309_100 = v20309_100;
    }

    public Integer getV20309_150() {
        return V20309_150;
    }

    public void setV20309_150(Integer v20309_150) {
        V20309_150 = v20309_150;
    }

    public Integer getV20309_300() {
        return V20309_300;
    }

    public void setV20309_300(Integer v20309_300) {
        V20309_300 = v20309_300;
    }

    public Integer getV20309_011() {
        return V20309_011;
    }

    public void setV20309_011(Integer v20309_011) {
        V20309_011 = v20309_011;
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

    public String getV13334_060_CHAR() {
        return V13334_060_CHAR;
    }

    public void setV13334_060_CHAR(String v13334_060_CHAR) {
        V13334_060_CHAR = v13334_060_CHAR;
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

    public String getV13330_060() {
        return V13330_060;
    }

    public void setV13330_060(String v13330_060) {
        V13330_060 = v13330_060;
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

    public Float getV20326_NS() {
        return V20326_NS;
    }

    public void setV20326_NS(Float v20326_NS) {
        V20326_NS = v20326_NS;
    }

    public Float getV20306_NS() {
        return V20306_NS;
    }

    public void setV20306_NS(Float v20306_NS) {
        V20306_NS = v20306_NS;
    }

    public Float getV20307_NS() {
        return V20307_NS;
    }

    public void setV20307_NS(Float v20307_NS) {
        V20307_NS = v20307_NS;
    }

    public Float getV20326_WE() {
        return V20326_WE;
    }

    public void setV20326_WE(Float v20326_WE) {
        V20326_WE = v20326_WE;
    }

    public Float getV20306_WE() {
        return V20306_WE;
    }

    public void setV20306_WE(Float v20306_WE) {
        V20306_WE = v20306_WE;
    }

    public Float getV20307_WE() {
        return V20307_WE;
    }

    public void setV20307_WE(Float v20307_WE) {
        V20307_WE = v20307_WE;
    }

    public Integer getV20440_040_NS() {
        return V20440_040_NS;
    }

    public void setV20440_040_NS(Integer v20440_040_NS) {
        V20440_040_NS = v20440_040_NS;
    }

    public String getV20440_060_NS_CHAR() {
        return V20440_060_NS_CHAR;
    }

    public void setV20440_060_NS_CHAR(String v20440_060_NS_CHAR) {
        V20440_060_NS_CHAR = v20440_060_NS_CHAR;
    }

    public Integer getV20440_040_WE() {
        return V20440_040_WE;
    }

    public void setV20440_040_WE(Integer v20440_040_WE) {
        V20440_040_WE = v20440_040_WE;
    }

    public String getV20440_060_WE_CHAR() {
        return V20440_060_WE_CHAR;
    }

    public void setV20440_060_WE_CHAR(String v20440_060_WE_CHAR) {
        V20440_060_WE_CHAR = v20440_060_WE_CHAR;
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

    public String getV11042_060_CHAR() {
        return V11042_060_CHAR;
    }

    public void setV11042_060_CHAR(String v11042_060_CHAR) {
        V11042_060_CHAR = v11042_060_CHAR;
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

    public String getV11211() {
        return V11211;
    }

    public void setV11211(String v11211) {
        V11211 = v11211;
    }

    public Integer getV11046_040() {
        return V11046_040;
    }

    public void setV11046_040(Integer v11046_040) {
        V11046_040 = v11046_040;
    }

    public String getV11046_060_CHAR() {
        return V11046_060_CHAR;
    }

    public void setV11046_060_CHAR(String v11046_060_CHAR) {
        V11046_060_CHAR = v11046_060_CHAR;
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

    public Float getV12120_701() {
        return V12120_701;
    }

    public void setV12120_701(Float v12120_701) {
        V12120_701 = v12120_701;
    }

    public Float getV12311_701() {
        return V12311_701;
    }

    public void setV12311_701(Float v12311_701) {
        V12311_701 = v12311_701;
    }

    public Float getV12121_701() {
        return V12121_701;
    }

    public void setV12121_701(Float v12121_701) {
        V12121_701 = v12121_701;
    }

    public Float getV12620() {
        return V12620;
    }

    public void setV12620(Float v12620) {
        V12620 = v12620;
    }

    public Float getV12311() {
        return V12311;
    }

    public void setV12311(Float v12311) {
        V12311 = v12311;
    }

    public Integer getV12311_040() {
        return V12311_040;
    }

    public void setV12311_040(Integer v12311_040) {
        V12311_040 = v12311_040;
    }

    public String getV12311_060_CHAR() {
        return V12311_060_CHAR;
    }

    public void setV12311_060_CHAR(String v12311_060_CHAR) {
        V12311_060_CHAR = v12311_060_CHAR;
    }

    public Float getV12121() {
        return V12121;
    }

    public void setV12121(Float v12121) {
        V12121 = v12121;
    }

    public Integer getV12121_040() {
        return V12121_040;
    }

    public void setV12121_040(Integer v12121_040) {
        V12121_040 = v12121_040;
    }

    public String getV12121_060_CHAR() {
        return V12121_060_CHAR;
    }

    public void setV12121_060_CHAR(String v12121_060_CHAR) {
        V12121_060_CHAR = v12121_060_CHAR;
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

    public Float getBalltemp_avg() {
        return balltemp_avg;
    }

    public void setBalltemp_avg(Float balltemp_avg) {
        this.balltemp_avg = balltemp_avg;
    }

    public Float getV13004_701() {
        return V13004_701;
    }

    public void setV13004_701(Float v13004_701) {
        V13004_701 = v13004_701;
    }

    public Float getV13004_MAX() {
        return V13004_MAX;
    }

    public void setV13004_MAX(Float v13004_MAX) {
        V13004_MAX = v13004_MAX;
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

    public Integer getV13004_MIN_D() {
        return V13004_MIN_D;
    }

    public void setV13004_MIN_D(Integer v13004_MIN_D) {
        V13004_MIN_D = v13004_MIN_D;
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

    public String getV12315_060_CHAR() {
        return V12315_060_CHAR;
    }

    public void setV12315_060_CHAR(String v12315_060_CHAR) {
        V12315_060_CHAR = v12315_060_CHAR;
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

    public String getV12316_060_CHAR() {
        return V12316_060_CHAR;
    }

    public void setV12316_060_CHAR(String v12316_060_CHAR) {
        V12316_060_CHAR = v12316_060_CHAR;
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

    public String getV20334_060_CHAR() {
        return V20334_060_CHAR;
    }

    public void setV20334_060_CHAR(String v20334_060_CHAR) {
        V20334_060_CHAR = v20334_060_CHAR;
    }

    public Float getV14032() {
        return V14032;
    }

    public void setV14032(Float v14032) {
        V14032 = v14032;
    }

    public Integer getSunTime() {
        return sunTime;
    }

    public void setSunTime(Integer sunTime) {
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

    public BigDecimal getV14311_05() {
        return v14311_05;
    }

    public void setV14311_05(BigDecimal v14311_05) {
        this.v14311_05 = v14311_05;
    }

    public Integer getV14021_05_052() {
        return v14021_05_052;
    }

    public void setV14021_05_052(Integer v14021_05_052) {
        this.v14021_05_052 = v14021_05_052;
    }

    public BigDecimal getV14312() {
        return v14312;
    }

    public void setV14312(BigDecimal v14312) {
        this.v14312 = v14312;
    }

    public BigDecimal getV14312_05() {
        return v14312_05;
    }

    public void setV14312_05(BigDecimal v14312_05) {
        this.v14312_05 = v14312_05;
    }

    public Integer getV14312_05_052() {
        return v14312_05_052;
    }

    public void setV14312_05_052(Integer v14312_05_052) {
        this.v14312_05_052 = v14312_05_052;
    }

    public BigDecimal getV14314_avg() {
        return v14314_avg;
    }

    public void setV14314_avg(BigDecimal v14314_avg) {
        this.v14314_avg = v14314_avg;
    }

    public BigDecimal getV14314() {
        return v14314;
    }

    public void setV14314(BigDecimal v14314) {
        this.v14314 = v14314;
    }

    public BigDecimal getV14314_05() {
        return v14314_05;
    }

    public void setV14314_05(BigDecimal v14314_05) {
        this.v14314_05 = v14314_05;
    }

    public BigDecimal getV14322_avg() {
        return v14322_avg;
    }

    public void setV14322_avg(BigDecimal v14322_avg) {
        this.v14322_avg = v14322_avg;
    }

    public BigDecimal getV14322() {
        return v14322;
    }

    public void setV14322(BigDecimal v14322) {
        this.v14322 = v14322;
    }
}
