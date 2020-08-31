package cma.cimiss2.dpc.decoder.fileDecode.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;


/**
 * 月值表字段信息
 * @author yangkq
 * @version 1.0
 * @Date 2020/3/23
 */
@Data
public class MonthValueTab implements Serializable {
    private static final long serialVersionUID = 1L;
    private String D_RECORD_ID;//记录标识 
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
}
