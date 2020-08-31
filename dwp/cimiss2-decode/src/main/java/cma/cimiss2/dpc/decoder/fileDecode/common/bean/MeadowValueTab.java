package cma.cimiss2.dpc.decoder.fileDecode.common.bean;


import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * 旬值数据实体
 * @author yangkq
 * @version 1.0
 * @date 2020/4/15
 */
@Data
public class MeadowValueTab {
    private String D_RECORD_ID;//记录标识 （ 系统自动生成的流水号 ）
    private String D_DATA_ID;//资料标识 （ 资料的4级编码 ）
    private String D_IYMDHM;//入库时间 （ 插表时的系统时间 ）
    private String D_RYMDHM;//收到时间 （ DPC消息生成时间 ）
    private String D_UPDATE_TIME;//更新时间 （ 根据CCx对记录更新时的系统时间 ）
    private String D_DATETIME;//资料时间 （ 由V04001、V04002组成 ）
    private String V01301;//区站号(字符)
    private String V01300;//区站号(数字)
    private Float V05001;//纬度 （ 单位：度 ）
    private Float V06001;//经度 （ 单位：度 ）
    private Float V07001;//测站高度 （ 单位：米 ）
    private Float V07031;//气压传感器海拔高度 （ 单位：米 ）
    private Integer V02301;//台站级别 （ 代码表 ）
    private Integer V_ACODE;//中国行政区划代码 （ 代码表 ）
    private Integer V04001;//年
    private Integer V04002;//月
    private String V04290;//旬
    private Float V10004_701;//旬平均本站气压 （ 单位：百帕 ）
    private Float V10301_avg;// 旬平均最高本站气压 ( 单位：百帕 )
    private Float V10302_avg;// 旬平均最低本站气压 ( 单位：百帕 )
    private Float V10301;//旬极端最高本站气压 （ 单位：百帕 ）
    private Integer V10301_040;//旬极端最高本站气压出现日数
    private String V10301_060;//旬极端最高本站气压出现日 （ 字符 ）
    private Float V10302;//旬极端最低本站气压 （ 单位：百帕 ）
    private Integer V10302_040;//旬极端最低本站气压出现日数 （  ）
    private String V10302_060;//旬极端最低本站气压出现日 （ 字符 ）
    private Float V10051_701;//旬平均海平面气压 （ 单位：百帕 ）
    private Float V12001_701;//旬平均气温 （ 单位：摄氏度 ）
    private Float V12011_avg;// 旬平均最高气温 ( 单位：摄氏度 )
    private Float V12012_avg;// 旬平均最低气温 ( 单位：摄氏度 )
    private Float V12011;//旬极端最高气温 （ 单位：摄氏度 ）
    private Integer V12011_040;//旬极端最高气温出现日数 （  ）
    private String V12011_060_CHAR;//旬极端最高气温出现日 （ 字符 ）
    private Float V12012;//旬极端最低气温 （ 单位：摄氏度 ）
    private Integer V12012_040;//旬极端最低气温出现日数 （  ）
    private String V12012_060_CHAR;//旬极端最低气温出现日 （ 字符 ）
    private Float V13004_701;//旬平均水汽压 （ 单位：百帕 ）
    private Integer V13003_701;//旬平均相对湿度 （ 单位：% ）
    private Integer V20010_701;//旬平均总云量 （ 单位：% ）
    private Integer V20051_701;//旬平均低云量 （ 单位：% ）
    private Float V13305;//20-20时降水量 （ 单位：毫米 ）
    private Float V13306;//08-08时降水量 （ 单位：毫米 ）
    private Float V13305_SD;//20-20时固态降水量 （ 单位：毫米 ）
    private Float V13306_SD;//08-08时固态降水量 （ 单位：毫米 ）
    private Integer V13353;//旬内日降水量≥0.1mm的日数 （ 单位：日 ）
    private Integer V13355_025;//旬内日降水量≥25mm的日数 （ 单位：日 ）
    private Integer V13355_050;//旬内日降水量≥50mm的日数 （ 单位：日 ）
    private Float V12120_701;//旬平均地面温度 （ 单位：摄氏度 ）
    private Float V12311;//旬极端最高地面温度 （ 单位：摄氏度 ）
    private Integer V12311_040;//旬极端最高地面温度出现日数 （  ）
    private String V12311_060_CHAR;//旬极端最高地面温度出现日 （ 字符 ）
    private Float V12121;//旬极端最低地面温度 （ 单位：摄氏度 ）
    private Integer V12121_040;//旬极端最低地面温度出现日数 （  ）
    private String V12121_060_CHAR;//旬极端最低地面温度出现日 （ 字符 ）
    private Float V12311_avg;// 旬平均最高地面温度 ( 单位：摄氏度 )
    private Float V12121_avg;// 旬平均最低地面温度 ( 单位：摄氏度 )
    private Float V12314_701;//旬平均草面温度 （ 单位：摄氏度 ）
    private Float V12315;//旬极端最高草面（雪面）温度 （ 单位：摄氏度 ）
    private Float V12315_avg;//旬平均最高草面（雪面）温度 （ 单位：摄氏度 ）
    private Integer V12315_040;//旬极端最高草面（雪面）温度出现日数 （  ）
    private String V12315_060_CHAR;//旬极端最高草面（雪面）温度出现日 （ 字符 ）
    private Float V12316;//旬极端最低草面（雪面）温度 （ 单位：摄氏度 ）
    private Float V12316_avg;//旬平均最低草面（雪面）温度 （ 单位：摄氏度 ）
    private Integer V12316_040;//旬极端最低草面（雪面）温度出现日数 （  ）
    private String V12316_060_CHAR;//旬极端最低草面（雪面）温度出现日 （ 字符 ）
    private Float V12030_701_005;//旬平均5cm地温 （ 单位：摄氏度 ）
    private Float V12030_701_010;//旬平均10cm地温 （ 单位：摄氏度 ）
    private Float V12030_701_015;//旬平均15cm地温 （ 单位：摄氏度 ）
    private Float V12030_701_020;//旬平均20cm地温 （ 单位：摄氏度 ）
    private Float V12030_701_040;//旬平均40cm地温 （ 单位：摄氏度 ）
    private Float V12030_701_080;//旬平均80cm地温 （ 单位：摄氏度 ）
    private Float V12030_701_160;//旬平均160cm地温 （ 单位：摄氏度 ）
    private Float V12030_701_320;//旬平均320cm地温 （ 单位：摄氏度 ）
    private Float V11291_701;//旬平均2分钟风速 （ 单位：米/秒 ）
    private Float V11042;//旬最大风速 （ 单位：米/秒 ）
    private String V11296;//旬最大风速的风向 （ 单位：度 ）
    private Integer V11042_040;//旬最大风速出现日数 （  ）
    private String V11042_060;//旬最大风速之出现日 （ 字符 ）
    private Float V11046;//旬极大风速 （ 单位：米/秒 ）
    private String V11211;//旬极大风速的风向 （ 单位：度 ）
    private Integer V11046_040;//旬极大风速出现日数 （  ）
    private String V11211_060;//旬极大风速之出现日 （ 字符 ）
    private Float V11293_701;//旬平均10分钟风速 （ 单位：米/秒 ）
    private Integer V04330_015;//旬内大风日数 （ 单位：日 ）
    private Float V13334;//旬内最大积雪深度 （ 单位：厘米 ）
    private Integer V13334_040;//旬内最大积雪深度出现日数 （  ）
    private String V13334_060_CHAR;//旬内最大积雪深度出现日 （ 字符 ）
    private Float V13330;//旬内最大雪压 （ 单位：克/平方厘米 ）
    private Integer V13330_040;//旬内最大雪压出现日数 （  ）
    private String V13330_060;//旬内最大雪压出现日 （ 字符 ）
    private Float V13032;//旬蒸发量(小型) （ 单位：毫米 ）
    private Float V13033;//旬蒸发量(大型) （ 单位：毫米 ）
    private Float V14032;//旬日照时数 （ 单位：小时 ）
    private Integer sunTime;//日出时长
    private Integer V14033;//旬日照百分率 （ 单位：% ）
    private Float V12011_701;//旬平均日最高气温 （ 单位：摄氏度 ）
    private Float V12012_701;//旬平均日最低气温 （ 单位：摄氏度 ）
    private BigDecimal v14311;//旬太阳总辐射(MJ/m2)
    private BigDecimal v14312;//旬净全辐射(MJ/m2)
}
