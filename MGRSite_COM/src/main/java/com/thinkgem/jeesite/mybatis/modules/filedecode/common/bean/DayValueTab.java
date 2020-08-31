package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;


import java.io.Serializable;
import java.math.BigDecimal;


/**
 * cimiss地面日值表信息
 */

public class DayValueTab implements Serializable {
    private static final long serialVersionUID = 1L;
    private String D_RETAIN_ID;// 记录标识 ( 系统自动生成的流水号 )
    private String D_DATA_ID;// 资料标识 ( 资料的4级编码 )
    private String D_IYMDHM;// 入库时间 ( 插表时的系统时间 )
    private String D_RYMDHM;// 收到时间 ( DPC消息生成时间 )
    private String D_UPDATE_TIME;// 更新时间 ( 根据CCx对记录更新时的系统时间 )
    private String D_DATETIME;// 资料时间 ( 由V04001、V04002、V04003组成 )
    private String V_BBB;// 更正报标志
    private String V01301;// 区站号(字符)
    private String V01300;// 区站号(数字)
    private Float V05001;// 纬度 ( 单位：度 )
    private Float V06001;// 经度 ( 单位：度 )
    private Float V07001;// 测站高度 ( 单位：米 )
    private Float V07031;// 气压传感器海拔高度 ( 单位：米 )
    private Integer V02301;// 台站级别 ( 代码表 )
    private Integer V_ACODE;// 中国行政区划代码 ( 代码表 )
    private Integer V04001;// 年
    private Integer V04002;// 月
    private Integer V04003;// 日
    private Float V10004_701;// 日平均本站气压 ( 单位：百帕 )
    private Float V10301;// 日最高本站气压 ( 单位：百帕 )
    private String V10301_052;// 日最高本站气压出现时间 ( 格式：时分 )
    private Float V10302;// 日最低本站气压 ( 单位：百帕 )
    private String V10302_052;// 日最低本站气压出现时间 ( 格式：时分 )
    private Float V10051_701;// 日平均海平面气压 ( 单位：百帕 )
    private Float V12001_701;// 日平均气温 ( 单位：摄氏度 )
    private Float V12011;// 日最高气温 ( 单位：摄氏度 )
    private String V12011_052;// 日最高气温出现时间 ( 格式：时分 )
    private Float V12012;// 日最低气温 ( 单位：摄氏度 )
    private	Float V12303_701;//日气温日较差（单位：摄氏度）
    private String V12012_052;// 日最低气温出现时间 ( 格式：时分 )
    private Float V13004_701;// 日平均水汽压 ( 单位：百帕 )
    private Float  V13004_MAX;//日最大水汽压
    private Float V13004_MIN;//日最小水气压
    private Integer V13003_701;// 日平均相对湿度 ( 单位：% )
    private Integer V13007;// 日最小相对湿度 ( 单位：% )
    private String V13007_052;// 日最小相对湿度出现时间 ( 格式：时分 )
    private Float htem100;//100cm深层地温
    private Float htem200;//200cm深层地温
    private Float htem300;//330cm深层地温
    private String obversetype;// 观测方式和测站类别
    private String obsercecode;// 观测项目标识
    private String qualitycode;// 质量控制指示码
    private String arcaninehight;// 风速器距地高度
    private Float rain1;//自记1小时降水
    private Float rain10;//自记10分钟最大降水
    private String wetherSymbol;//天气符号
    private String wetherStartTime;//天气开始时间-时+分
    private String wetherEndTime;//天气结束时间-时+分
    private Integer glazeNSDia;//雨凇南北直径
    private Integer glazeWEDia;//雨凇东西直径mm
    private Integer glazeNSHight;//雨凇南北厚度
    private Integer glazeWEHight;//雨凇东西厚度mm
    private Integer glazeNSWeight;//雨凇东西重量g/m
    private Integer glazeWEWeight;//雨凇东西重量
    private Integer rimeNSDia;//雾凇南北直径
    private Integer rimeWEDia;//雾凇东西直径
    private Integer rimeNSHight;//雾凇南北厚度
    private Integer rimeWEHight;//雾凇东西厚度
    private Integer rimeNSWeight;//雾凇东西重量
    private Integer rimeWEWeight;//雾凇东西重量
    private Integer V20010_701;// 日平均总云量 ( 单位：% )
    private Integer V20051_701;// 日平均低云量 ( 单位：% )
    private Float V20059;// 日最小水平能见度 ( 单位：米 )
    private String V20059_052;// 日最小水平能见度出现时间 ( 格式：时分 )
    private Float V13302_01;// 日小时最大降水量 ( 单位：毫米  )
    private Integer V13302_01_052;// 日小时最大降水量出现时间 ( 格式：时分 )
    private Float V13308;// 20-08时雨量筒观测降水量 ( 单位：毫米 )
    private Float V13309;// 08-20时雨量筒观测降水量 ( 单位：毫米 )
    private Float V13305;// 20-20时降水量 ( 单位：毫米 )
    private Float V13306;// 08-08时降水量 ( 单位：毫米 )
    private Float V13032;// 日蒸发量（小型） ( 单位：毫米 )
    private Float V13033;// 日蒸发量（大型） ( 单位：毫米 )
    private Float V13013;// 积雪深度 ( 单位：厘米 )
    private Float V13330;// 雪压 ( 单位：g/cm2 )
    private Integer V20305;// 电线积冰-现象 ( 代码表 )
    private Float V20326_NS;// 电线积冰-南北方向直径 ( 单位：毫米 )
    private Float V20306_NS;// 电线积冰-南北方向厚度 ( 单位：毫米 )
    private Float V20307_NS;// 电线积冰-南北方向重量 ( 单位：克 )
    private Float V20326_WE;// 电线积冰-东西方向直径 ( 单位：毫米 )
    private Float V20306_WE;// 电线积冰-东西方向厚度 ( 单位：毫米 )
    private Float V20307_WE;// 电线积冰-东西方向重量 ( 单位：克 )
    private Float V12001;// 电线积冰－温度 ( 单位：摄氏度 )
    private String V11001;// 电线积冰－风向 ( 单位：度 )
    private Float V11002;// 电线积冰－风速 ( 单位：米/秒 )
    private String V11290_CHAR;// 逐小时2分钟平均风向 ( 格式：字符串  )
    private Float V11291_701;// 日平均2分钟风速 ( 单位：米/秒 )
    private Float V11293_701;// 日平均10分钟风速 ( 单位：米/秒 )
    private String V11296;// 日最大风速的风向 ( 单位：度 )
    private Float V11042;// 日最大风速 ( 单位：米/秒 )
    private String V11042_052;// 日最大风速出现时间 ( 格式：时分 )
    private String V11211;// 日极大风速的风向 ( 单位：度 )
    private Float V11046;// 日极大风速 ( 单位：米/秒 )
    private String V11046_052;// 日极大风速出现时间 ( 格式：时分 )
    private Float V12120_701;// 日平均地面温度 ( 单位：摄氏度 )
    private Float V12311;// 日最高地面温度 ( 单位：摄氏度 )
    private String V12311_052;// 日最高地面温度出现时间  ( 格式：时分 )
    private Float V12121;// 日最低地面温度 ( 单位：摄氏度 )
    private String V12121_052;// 日最低地面温度出现时间 ( 格式：时分 )
    private Float V12030_701_005;// 日平均5cm地温 ( 单位：摄氏度 )
    private Float V12030_701_010;// 日平均10cm地温 ( 单位：摄氏度 )
    private Float V12030_701_015;// 日平均15cm地温 ( 单位：摄氏度 )
    private Float V12030_701_020;// 日平均20cm地温 ( 单位：摄氏度 )
    private Float V12030_701_040;// 日平均40cm地温 ( 单位：摄氏度 )
    private Float V12030_701_080;// 日平均80cm地温 ( 单位：摄氏度 )
    private Float V12030_701_160;// 日平均160cm地温 ( 单位：摄氏度 )
    private Float V12030_701_320;// 日平均320cm地温 ( 单位：摄氏度 )
    private Float V20331;//日最大冻土深度
    private Float V20330_01;// 第一冻土层上界值 ( 单位：厘米 )
    private Float V20331_01;// 第一冻土层下界值 ( 单位：厘米 )
    private Float V20330_02;// 第二冻土层上界值 ( 单位：厘米 )
    private Float V20331_02;// 第二冻土层下界值 ( 单位：厘米 )
    private Float V12003_avg;//日平均露点温度
    private Float balltemp_avg;//日平均湿球温度
    private Float balltemp_avg_24;//日平均湿球温度(24小时)
    private Float V14032;// 日总日照时数 ( 单位：小时 )
    private Integer V14033;//日照频率
    private String V20411;// 日出时间 ( 格式：时分 )
    private String V20412;// 日落时间 ( 格式：时分 )
    private Integer sunTime;//日出时长
    private Float V12314_701;// 日平均草面（雪面）温度 ( 单位：摄氏度 )
    private Float V12315;// 日草面（雪面）最高温度 ( 单位：摄氏度 )
    private String V12315_052;// 日草面（雪面）最高温度出现时间 ( 格式：时分 )
    private Float V12316;// 日草面（雪面）最低温度 ( 单位：摄氏度 )
    private String V12316_052;// 日草面（雪面）最低温度出现时间 ( 格式：时分 )
    private Integer V20062;// 地面状态（0-31数字） ( 见代码表020062 )
    private Integer V20302_060;// 雨 ( 代码表20302(下同) )
    private String V20302_060_052;// 雨出现时间 ( 格式：字符串 )
    private Integer V20302_070;// 雪 ( 代码表 )
    private String V20302_070_052;// 雪出现时间 ( 格式：字符串 )
    private Integer V20302_089;// 冰雹 ( 代码表 )
    private String V20302_089_052;// 冰雹出现时间 ( 格式：字符串 )
    private Integer V20302_042;// 雾 ( 代码表 )
    private String V20302_042_052;// 雾出现时间 ( 格式：字符串 )
    private Integer V20302_010;// 轻雾 ( 代码表 )
    private Integer V20302_001;// 露 ( 代码表 )
    private Integer V20302_002;// 霜 ( 代码表 )
    private Integer V20302_056;// 雨凇 ( 代码表 )
    private String V20302_056_052;// 雨凇出现时间 ( 格式：字符串 )
    private Integer V20302_048;// 雾凇 ( 代码表 )
    private String V20302_048_052;// 雾凇出现时间 ( 格式：字符串 )
    private Integer V20302_038;// 吹雪 ( 代码表 )
    private String V20302_038_052;// 吹雪出现时间 ( 格式：字符串 )
    private Integer V20302_039;// 雪暴 ( 代码表 )
    private String V20302_039_052;// 雪暴出现时间 ( 格式：字符串 )
    private Integer V20302_019;// 龙卷风 ( 代码表 )
    private String V20302_019_052;// 龙卷风出现时间 ( 格式：字符串 )
    private Integer V20302_016;// 积雪 ( 代码表 )
    private Integer V20302_003;// 结冰 ( 代码表 )
    private Integer V20302_031;// 沙尘暴 ( 代码表 )
    private String V20302_031_052;// 沙尘暴出现时间 ( 格式：字符串 )
    private Integer V20302_007;// 扬沙 ( 代码表 )
    private String V20302_007_052;// 扬沙出现时间 ( 格式：字符串 )
    private Integer V20302_006;// 浮尘 ( 代码表 )
    private String V20302_006_052;// 浮尘出现时间 ( 格式：字符串 )
    private Integer V20302_004;// 烟 ( 代码表 )
    private Integer V20302_005;// 霾 ( 代码表 )
    private Integer V20302_008;// 尘卷风 ( 代码表 )
    private Integer V20302_076;// 冰针 ( 代码表 )
    private Integer V20302_017;// 雷暴 ( 代码表 )
    private String V20302_017_052;// 雷暴出现时间 ( 格式：字符串 )
    private Integer V20302_013;// 闪电 ( 代码表 )
    private Integer V20302_014;// 极光 ( 代码表 )
    private String V20302_014_052;// 极光出现时间 ( 格式：字符串 )
    private Integer V20302_015;// 大风 ( 代码表 )
    private String V20302_015_052;// 大风出现时间 ( 格式：字符串 )
    private Integer V20302_018;// 飑 ( 代码表 )
    private String V20302_018_052;// 飑出现时间 ( 格式：字符串 )
    private String V20303;// 天气现象摘要 ( 字符 )
    private String V20304;// 天气现象记录 ( 字符 )
    private BigDecimal v14311;//日太阳总辐射(MJ/m2)
    private BigDecimal v14311_05;//日最大总辐射辐照度(W/m2)
    private Integer v14021_05_052;//日最大总辐射辐照度时间
    private BigDecimal v14312;//日净全辐射(MJ/m2)
    private BigDecimal v14312_05;//日最大净全辐射辐照度(W/m2)
    private Integer v14312_05_052;//日最大净全辐射辐照度时间
    private BigDecimal v14314;//日散射辐射曝辐量(MJ/m2)
    private BigDecimal v14308;//日净全辐射曝辐量(MJ/m2)
    private BigDecimal v14313;//日直接辐射曝辐量(MJ/m2)
    private Integer Q10004_701;// 日平均本站气压质控码 ( 代码表 )
    private Integer Q10301;// 日最高本站气压质控码 ( 代码表 )
    private Integer Q10301_052;// 日最高本站气压出现时间质控码 ( 代码表 )
    private Integer Q10302;// 日最低本站气压质控码 ( 代码表 )
    private Integer Q10302_052;// 日最低本站气压出现时间质控码 ( 代码表 )
    private Integer Q10051_701;// 日平均海平面气压质控码 ( 代码表 )
    private Integer Q12001_701;// 日平均气温质控码 ( 代码表 )
    private Integer Q12011;// 日最高气温质控码 ( 代码表 )
    private Integer Q12011_052;// 日最高气温出现时间质控码 ( 代码表 )
    private Integer Q12012;// 日最低气温质控码 ( 代码表 )
    private Integer Q12012_052;// 日最低气温出现时间质控码 ( 代码表 )
    private Integer Q13004_701;// 日平均水汽压质控码 ( 代码表 )
    private Integer Q13003_701;// 日平均相对湿度质控码 ( 代码表 )
    private Integer Q13007;// 日最小相对湿度质控码 ( 代码表 )
    private Integer Q13007_052;// 日最小相对湿度出现时间质控码 ( 代码表 )
    private Integer Q20010_701;// 日平均总云量质控码 ( 代码表 )
    private Integer Q20051_701;// 日平均低云量质控码 ( 代码表 )
    private Integer Q20059;// 日最小水平能见度质控码 ( 代码表 )
    private Integer Q20059_052;// 日最小水平能见度出现时间质控码 ( 代码表 )
    private Integer Q13302_01;// 日小时最大降水量质控码 ( 代码表 )
    private Integer Q13302_01_052;// 日小时最大降水量出现时间质控码 ( 代码表 )
    private Integer Q13308;// 20-08时雨量筒观测降水量质控码 ( 代码表 )
    private Integer Q13309;// 08-20时雨量筒观测降水量质控码 ( 代码表 )
    private Integer Q13305;// 20-20时降水量质控码 ( 代码表 )
    private Integer Q13306;// 08-08时降水量质控码 ( 代码表 )
    private Integer Q13032;// 日蒸发量（小型）质控码 ( 代码表 )
    private Integer Q13033;// 日蒸发量（大型）质控码 ( 代码表 )
    private Integer Q13013;// 积雪深度质控码 ( 代码表 )
    private Integer Q13330;// 雪压质控码 ( 代码表 )
    private Integer Q20305;// 电线积冰-现象质控码 ( 代码表 )
    private Integer Q20326_NS;// 电线积冰-南北方向直径质控码 ( 代码表 )
    private Integer Q20306_NS;// 电线积冰-南北方向厚度质控码( 代码表 )
    private Integer Q20307_NS;// 电线积冰-南北方向重量质控码 ( 代码表 )
    private Integer Q20326_WE;// 电线积冰-东西方向直径质控码 ( 代码表 )
    private Integer Q20306_WE;// 电线积冰-东西方向厚度质控码 ( 代码表 )
    private Integer Q20307_WE;// 电线积冰-东西方向重量质控码 ( 代码表 )
    private Integer Q12001;// 电线积冰－温度质控码 ( 代码表 )
    private Integer Q11001;// 电线积冰－风向质控码 ( 代码表 )
    private Integer Q11002;// 电线积冰－风速质控码 ( 代码表 )
    private Integer Q11290_CHAR;// 日平均2分钟风向质控码 ( 代码表 )
    private Integer Q11291_701;// 日平均2分钟风速质控码 ( 代码表 )
    private Integer Q11293_701;// 日平均10分钟风速质控码 ( 代码表 )
    private Integer Q11296;// 日最大风速的风向质控码 ( 代码表 )
    private Integer Q11042;// 日最大风速质控码 ( 代码表 )
    private Integer Q11042_052;// 日最大风速出现时间质控码 ( 代码表 )
    private Integer Q11211;// 日极大风速的风向质控码 ( 代码表 )
    private Integer Q11046;// 日极大风速质控码 ( 代码表 )
    private Integer Q11046_052;// 日极大风速出现时间质控码 ( 代码表 )
    private Integer Q12120_701;// 日平均地面温度质控码 ( 代码表 )
    private Integer Q12311;// 日最高地面温度质控码 ( 代码表 )
    private Integer Q12311_052;// 日最高地面温度出现时间质控码 ( 代码表 )
    private Integer Q12121;// 日最低地面温度质控码 ( 代码表 )
    private Integer Q12121_052;// 日最低地面温度出现时间质控码 ( 代码表 )
    private Integer Q12030_701_005;// 日平均5cm地温质控码 ( 代码表 )
    private Integer Q12030_701_010;// 日平均10cm地温质控码 ( 代码表 )
    private Integer Q12030_701_015;// 日平均15cm地温质控码 ( 代码表 )
    private Integer Q12030_701_020;// 日平均20cm地温质控码 ( 代码表 )
    private Integer Q12030_701_040;// 日平均40cm地温质控码 ( 代码表 )
    private Integer Q12030_701_080;// 日平均80cm地温质控码 ( 代码表 )
    private Integer Q12030_701_160;// 日平均160cm地温质控码 ( 代码表 )
    private Integer Q12030_701_320;// 日平均320cm地温质控码 ( 代码表 )
    private Integer Q20330_01;// 第一冻土层上界值质控码 ( 代码表 )
    private Integer Q20331_01;// 第一冻土层下界值质控码 ( 代码表 )
    private Integer Q20330_02;// 第二冻土层上界值质控码 ( 代码表 )
    private Integer Q20331_02;// 第二冻土层下界值质控码 ( 代码表 )
    private Integer Q14032;// 日总日照时数质控码 ( 代码表 )
    private Integer Q20411;// 日出时间质控码 ( 代码表 )
    private Integer Q20412;// 日落时间质控码 ( 代码表 )
    private Integer Q12314_701;// 日平均草面（雪面）温度质控码 ( 代码表 )
    private Integer Q12315;// 日草面（雪面）最高温度质控码 ( 代码表 )
    private Integer Q12315_052;// 日草面（雪面）最高温度出现时间质控码 ( 代码表 )
    private Integer Q12316;// 日草面（雪面）最低温度质控码 ( 代码表 )
    private Integer Q12316_052;// 日草面（雪面）最低温度出现时间质控码 ( 代码表 )
    private Integer Q20062;// 地面状态质控码 ( 代码表 )
    private Integer Q20302_060;// 雨质控码 ( 代码表 )
    private Integer Q20302_060_052;// 雨出现时间质控码 ( 代码表 )
    private Integer Q20302_070;// 雪质控码 ( 代码表 )
    private Integer Q20302_070_052;// 雪出现时间质控码 ( 代码表 )
    private Integer Q20302_089;// 冰雹质控码 ( 代码表 )
    private Integer Q20302_089_052;// 冰雹出现时间质控码 ( 代码表 )
    private Integer Q20302_042;// 雾质控码 ( 代码表 )
    private Integer Q20302_042_052;// 雾出现时间质控码 ( 代码表 )
    private Integer Q20302_010;// 轻雾质控码 ( 代码表 )
    private Integer Q20302_001;// 露质控码 ( 代码表 )
    private Integer Q20302_002;// 霜质控码 ( 代码表 )
    private Integer Q20302_056;// 雨凇质控码 ( 代码表 )
    private Integer Q20302_056_052;// 雨凇出现时间质控码 ( 代码表 )
    private Integer Q20302_048;// 雾凇质控码 ( 代码表 )
    private Integer Q20302_048_052;// 雾凇出现时间质控码 ( 代码表 )
    private Integer Q20302_038;// 吹雪质控码 ( 代码表 )
    private Integer Q20302_038_052;// 吹雪出现时间质控码 ( 代码表 )
    private Integer Q20302_039;// 雪暴质控码 ( 代码表 )
    private Integer Q20302_039_052;// 雪暴出现时间质控码 ( 代码表 )
    private Integer Q20302_019;// 龙卷风质控码 ( 代码表 )
    private Integer Q20302_019_052;// 龙卷风出现时间质控码 ( 代码表 )
    private Integer Q20302_016;// 积雪质控码 ( 代码表 )
    private Integer Q20302_003;// 结冰质控码 ( 代码表 )
    private Integer Q20302_031;// 沙尘暴质控码 ( 代码表 )
    private Integer Q20302_031_052;// 沙尘暴出现时间质控码 ( 代码表 )
    private Integer Q20302_007;// 扬沙质控码 ( 代码表 )
    private Integer Q20302_007_052;// 扬沙出现时间质控码 ( 代码表 )
    private Integer Q20302_006;// 浮尘质控码 ( 代码表 )
    private Integer Q20302_006_052;// 浮尘出现时间质控码 ( 代码表 )
    private Integer Q20302_004;// 烟质控码 ( 代码表 )
    private Integer Q20302_005;// 霾质控码 ( 代码表 )
    private Integer Q20302_008;// 尘卷风质控码 ( 代码表 )
    private Integer Q20302_076;// 冰针质控码 ( 代码表 )
    private Integer Q20302_017;// 雷暴质控码 ( 代码表 )
    private Integer Q20302_017_052;//雷暴出现时间质控码 ( 代码表 )
    private Integer Q20302_013;// 闪电质控码 ( 代码表 )
    private Integer Q20302_014;// 极光质控码 ( 代码表 )
    private Integer Q20302_014_052;// 极光出现时间质控码 ( 代码表 )
    private Integer Q20302_015;// 大风质控码 ( 代码表 )
    private Integer Q20302_015_052;// 大风出现时间质控码 ( 代码表 )
    private Integer Q20302_018;// 飑质控码 ( 代码表 )
    private Integer Q20302_018_052;// 飑出现时间质控码 ( 代码表 )
    private Integer Q20303;// 天气现象摘要质控码 ( 代码表 )
    private Integer Q20304;// 天气现象记录质控码 ( 代码表 )

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

    public String getV_BBB() {
        return V_BBB;
    }

    public void setV_BBB(String v_BBB) {
        V_BBB = v_BBB;
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

    public Integer getV04003() {
        return V04003;
    }

    public void setV04003(Integer v04003) {
        V04003 = v04003;
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

    public String getV10301_052() {
        return V10301_052;
    }

    public void setV10301_052(String v10301_052) {
        V10301_052 = v10301_052;
    }

    public Float getV10302() {
        return V10302;
    }

    public void setV10302(Float v10302) {
        V10302 = v10302;
    }

    public String getV10302_052() {
        return V10302_052;
    }

    public void setV10302_052(String v10302_052) {
        V10302_052 = v10302_052;
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

    public Float getV12011() {
        return V12011;
    }

    public void setV12011(Float v12011) {
        V12011 = v12011;
    }

    public String getV12011_052() {
        return V12011_052;
    }

    public void setV12011_052(String v12011_052) {
        V12011_052 = v12011_052;
    }

    public Float getV12012() {
        return V12012;
    }

    public void setV12012(Float v12012) {
        V12012 = v12012;
    }

    public Float getV12303_701() {
        return V12303_701;
    }

    public void setV12303_701(Float v12303_701) {
        V12303_701 = v12303_701;
    }

    public String getV12012_052() {
        return V12012_052;
    }

    public void setV12012_052(String v12012_052) {
        V12012_052 = v12012_052;
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

    public Float getV13004_MIN() {
        return V13004_MIN;
    }

    public void setV13004_MIN(Float v13004_MIN) {
        V13004_MIN = v13004_MIN;
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

    public String getV13007_052() {
        return V13007_052;
    }

    public void setV13007_052(String v13007_052) {
        V13007_052 = v13007_052;
    }

    public Float getHtem100() {
        return htem100;
    }

    public void setHtem100(Float htem100) {
        this.htem100 = htem100;
    }

    public Float getHtem200() {
        return htem200;
    }

    public void setHtem200(Float htem200) {
        this.htem200 = htem200;
    }

    public Float getHtem300() {
        return htem300;
    }

    public void setHtem300(Float htem300) {
        this.htem300 = htem300;
    }

    public String getObversetype() {
        return obversetype;
    }

    public void setObversetype(String obversetype) {
        this.obversetype = obversetype;
    }

    public String getObsercecode() {
        return obsercecode;
    }

    public void setObsercecode(String obsercecode) {
        this.obsercecode = obsercecode;
    }

    public String getQualitycode() {
        return qualitycode;
    }

    public void setQualitycode(String qualitycode) {
        this.qualitycode = qualitycode;
    }

    public String getArcaninehight() {
        return arcaninehight;
    }

    public void setArcaninehight(String arcaninehight) {
        this.arcaninehight = arcaninehight;
    }

    public Float getRain1() {
        return rain1;
    }

    public void setRain1(Float rain1) {
        this.rain1 = rain1;
    }

    public Float getRain10() {
        return rain10;
    }

    public void setRain10(Float rain10) {
        this.rain10 = rain10;
    }

    public String getWetherSymbol() {
        return wetherSymbol;
    }

    public void setWetherSymbol(String wetherSymbol) {
        this.wetherSymbol = wetherSymbol;
    }

    public String getWetherStartTime() {
        return wetherStartTime;
    }

    public void setWetherStartTime(String wetherStartTime) {
        this.wetherStartTime = wetherStartTime;
    }

    public String getWetherEndTime() {
        return wetherEndTime;
    }

    public void setWetherEndTime(String wetherEndTime) {
        this.wetherEndTime = wetherEndTime;
    }

    public Integer getGlazeNSDia() {
        return glazeNSDia;
    }

    public void setGlazeNSDia(Integer glazeNSDia) {
        this.glazeNSDia = glazeNSDia;
    }

    public Integer getGlazeWEDia() {
        return glazeWEDia;
    }

    public void setGlazeWEDia(Integer glazeWEDia) {
        this.glazeWEDia = glazeWEDia;
    }

    public Integer getGlazeNSHight() {
        return glazeNSHight;
    }

    public void setGlazeNSHight(Integer glazeNSHight) {
        this.glazeNSHight = glazeNSHight;
    }

    public Integer getGlazeWEHight() {
        return glazeWEHight;
    }

    public void setGlazeWEHight(Integer glazeWEHight) {
        this.glazeWEHight = glazeWEHight;
    }

    public Integer getGlazeNSWeight() {
        return glazeNSWeight;
    }

    public void setGlazeNSWeight(Integer glazeNSWeight) {
        this.glazeNSWeight = glazeNSWeight;
    }

    public Integer getGlazeWEWeight() {
        return glazeWEWeight;
    }

    public void setGlazeWEWeight(Integer glazeWEWeight) {
        this.glazeWEWeight = glazeWEWeight;
    }

    public Integer getRimeNSDia() {
        return rimeNSDia;
    }

    public void setRimeNSDia(Integer rimeNSDia) {
        this.rimeNSDia = rimeNSDia;
    }

    public Integer getRimeWEDia() {
        return rimeWEDia;
    }

    public void setRimeWEDia(Integer rimeWEDia) {
        this.rimeWEDia = rimeWEDia;
    }

    public Integer getRimeNSHight() {
        return rimeNSHight;
    }

    public void setRimeNSHight(Integer rimeNSHight) {
        this.rimeNSHight = rimeNSHight;
    }

    public Integer getRimeWEHight() {
        return rimeWEHight;
    }

    public void setRimeWEHight(Integer rimeWEHight) {
        this.rimeWEHight = rimeWEHight;
    }

    public Integer getRimeNSWeight() {
        return rimeNSWeight;
    }

    public void setRimeNSWeight(Integer rimeNSWeight) {
        this.rimeNSWeight = rimeNSWeight;
    }

    public Integer getRimeWEWeight() {
        return rimeWEWeight;
    }

    public void setRimeWEWeight(Integer rimeWEWeight) {
        this.rimeWEWeight = rimeWEWeight;
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

    public Float getV20059() {
        return V20059;
    }

    public void setV20059(Float v20059) {
        V20059 = v20059;
    }

    public String getV20059_052() {
        return V20059_052;
    }

    public void setV20059_052(String v20059_052) {
        V20059_052 = v20059_052;
    }

    public Float getV13302_01() {
        return V13302_01;
    }

    public void setV13302_01(Float v13302_01) {
        V13302_01 = v13302_01;
    }

    public Integer getV13302_01_052() {
        return V13302_01_052;
    }

    public void setV13302_01_052(Integer v13302_01_052) {
        V13302_01_052 = v13302_01_052;
    }

    public Float getV13308() {
        return V13308;
    }

    public void setV13308(Float v13308) {
        V13308 = v13308;
    }

    public Float getV13309() {
        return V13309;
    }

    public void setV13309(Float v13309) {
        V13309 = v13309;
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

    public Float getV13013() {
        return V13013;
    }

    public void setV13013(Float v13013) {
        V13013 = v13013;
    }

    public Float getV13330() {
        return V13330;
    }

    public void setV13330(Float v13330) {
        V13330 = v13330;
    }

    public Integer getV20305() {
        return V20305;
    }

    public void setV20305(Integer v20305) {
        V20305 = v20305;
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

    public Float getV12001() {
        return V12001;
    }

    public void setV12001(Float v12001) {
        V12001 = v12001;
    }

    public String getV11001() {
        return V11001;
    }

    public void setV11001(String v11001) {
        V11001 = v11001;
    }

    public Float getV11002() {
        return V11002;
    }

    public void setV11002(Float v11002) {
        V11002 = v11002;
    }

    public String getV11290_CHAR() {
        return V11290_CHAR;
    }

    public void setV11290_CHAR(String v11290_CHAR) {
        V11290_CHAR = v11290_CHAR;
    }

    public Float getV11291_701() {
        return V11291_701;
    }

    public void setV11291_701(Float v11291_701) {
        V11291_701 = v11291_701;
    }

    public Float getV11293_701() {
        return V11293_701;
    }

    public void setV11293_701(Float v11293_701) {
        V11293_701 = v11293_701;
    }

    public String getV11296() {
        return V11296;
    }

    public void setV11296(String v11296) {
        V11296 = v11296;
    }

    public Float getV11042() {
        return V11042;
    }

    public void setV11042(Float v11042) {
        V11042 = v11042;
    }

    public String getV11042_052() {
        return V11042_052;
    }

    public void setV11042_052(String v11042_052) {
        V11042_052 = v11042_052;
    }

    public String getV11211() {
        return V11211;
    }

    public void setV11211(String v11211) {
        V11211 = v11211;
    }

    public Float getV11046() {
        return V11046;
    }

    public void setV11046(Float v11046) {
        V11046 = v11046;
    }

    public String getV11046_052() {
        return V11046_052;
    }

    public void setV11046_052(String v11046_052) {
        V11046_052 = v11046_052;
    }

    public Float getV12120_701() {
        return V12120_701;
    }

    public void setV12120_701(Float v12120_701) {
        V12120_701 = v12120_701;
    }

    public Float getV12311() {
        return V12311;
    }

    public void setV12311(Float v12311) {
        V12311 = v12311;
    }

    public String getV12311_052() {
        return V12311_052;
    }

    public void setV12311_052(String v12311_052) {
        V12311_052 = v12311_052;
    }

    public Float getV12121() {
        return V12121;
    }

    public void setV12121(Float v12121) {
        V12121 = v12121;
    }

    public String getV12121_052() {
        return V12121_052;
    }

    public void setV12121_052(String v12121_052) {
        V12121_052 = v12121_052;
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

    public Float getV20331() {
        return V20331;
    }

    public void setV20331(Float v20331) {
        V20331 = v20331;
    }

    public Float getV20330_01() {
        return V20330_01;
    }

    public void setV20330_01(Float v20330_01) {
        V20330_01 = v20330_01;
    }

    public Float getV20331_01() {
        return V20331_01;
    }

    public void setV20331_01(Float v20331_01) {
        V20331_01 = v20331_01;
    }

    public Float getV20330_02() {
        return V20330_02;
    }

    public void setV20330_02(Float v20330_02) {
        V20330_02 = v20330_02;
    }

    public Float getV20331_02() {
        return V20331_02;
    }

    public void setV20331_02(Float v20331_02) {
        V20331_02 = v20331_02;
    }

    public Float getV12003_avg() {
        return V12003_avg;
    }

    public void setV12003_avg(Float v12003_avg) {
        V12003_avg = v12003_avg;
    }

    public Float getBalltemp_avg() {
        return balltemp_avg;
    }

    public void setBalltemp_avg(Float balltemp_avg) {
        this.balltemp_avg = balltemp_avg;
    }

    public Float getBalltemp_avg_24() {
        return balltemp_avg_24;
    }

    public void setBalltemp_avg_24(Float balltemp_avg_24) {
        this.balltemp_avg_24 = balltemp_avg_24;
    }

    public Float getV14032() {
        return V14032;
    }

    public void setV14032(Float v14032) {
        V14032 = v14032;
    }

    public Integer getV14033() {
        return V14033;
    }

    public void setV14033(Integer v14033) {
        V14033 = v14033;
    }

    public String getV20411() {
        return V20411;
    }

    public void setV20411(String v20411) {
        V20411 = v20411;
    }

    public String getV20412() {
        return V20412;
    }

    public void setV20412(String v20412) {
        V20412 = v20412;
    }

    public Integer getSunTime() {
        return sunTime;
    }

    public void setSunTime(Integer sunTime) {
        this.sunTime = sunTime;
    }

    public Float getV12314_701() {
        return V12314_701;
    }

    public void setV12314_701(Float v12314_701) {
        V12314_701 = v12314_701;
    }

    public Float getV12315() {
        return V12315;
    }

    public void setV12315(Float v12315) {
        V12315 = v12315;
    }

    public String getV12315_052() {
        return V12315_052;
    }

    public void setV12315_052(String v12315_052) {
        V12315_052 = v12315_052;
    }

    public Float getV12316() {
        return V12316;
    }

    public void setV12316(Float v12316) {
        V12316 = v12316;
    }

    public String getV12316_052() {
        return V12316_052;
    }

    public void setV12316_052(String v12316_052) {
        V12316_052 = v12316_052;
    }

    public Integer getV20062() {
        return V20062;
    }

    public void setV20062(Integer v20062) {
        V20062 = v20062;
    }

    public Integer getV20302_060() {
        return V20302_060;
    }

    public void setV20302_060(Integer v20302_060) {
        V20302_060 = v20302_060;
    }

    public String getV20302_060_052() {
        return V20302_060_052;
    }

    public void setV20302_060_052(String v20302_060_052) {
        V20302_060_052 = v20302_060_052;
    }

    public Integer getV20302_070() {
        return V20302_070;
    }

    public void setV20302_070(Integer v20302_070) {
        V20302_070 = v20302_070;
    }

    public String getV20302_070_052() {
        return V20302_070_052;
    }

    public void setV20302_070_052(String v20302_070_052) {
        V20302_070_052 = v20302_070_052;
    }

    public Integer getV20302_089() {
        return V20302_089;
    }

    public void setV20302_089(Integer v20302_089) {
        V20302_089 = v20302_089;
    }

    public String getV20302_089_052() {
        return V20302_089_052;
    }

    public void setV20302_089_052(String v20302_089_052) {
        V20302_089_052 = v20302_089_052;
    }

    public Integer getV20302_042() {
        return V20302_042;
    }

    public void setV20302_042(Integer v20302_042) {
        V20302_042 = v20302_042;
    }

    public String getV20302_042_052() {
        return V20302_042_052;
    }

    public void setV20302_042_052(String v20302_042_052) {
        V20302_042_052 = v20302_042_052;
    }

    public Integer getV20302_010() {
        return V20302_010;
    }

    public void setV20302_010(Integer v20302_010) {
        V20302_010 = v20302_010;
    }

    public Integer getV20302_001() {
        return V20302_001;
    }

    public void setV20302_001(Integer v20302_001) {
        V20302_001 = v20302_001;
    }

    public Integer getV20302_002() {
        return V20302_002;
    }

    public void setV20302_002(Integer v20302_002) {
        V20302_002 = v20302_002;
    }

    public Integer getV20302_056() {
        return V20302_056;
    }

    public void setV20302_056(Integer v20302_056) {
        V20302_056 = v20302_056;
    }

    public String getV20302_056_052() {
        return V20302_056_052;
    }

    public void setV20302_056_052(String v20302_056_052) {
        V20302_056_052 = v20302_056_052;
    }

    public Integer getV20302_048() {
        return V20302_048;
    }

    public void setV20302_048(Integer v20302_048) {
        V20302_048 = v20302_048;
    }

    public String getV20302_048_052() {
        return V20302_048_052;
    }

    public void setV20302_048_052(String v20302_048_052) {
        V20302_048_052 = v20302_048_052;
    }

    public Integer getV20302_038() {
        return V20302_038;
    }

    public void setV20302_038(Integer v20302_038) {
        V20302_038 = v20302_038;
    }

    public String getV20302_038_052() {
        return V20302_038_052;
    }

    public void setV20302_038_052(String v20302_038_052) {
        V20302_038_052 = v20302_038_052;
    }

    public Integer getV20302_039() {
        return V20302_039;
    }

    public void setV20302_039(Integer v20302_039) {
        V20302_039 = v20302_039;
    }

    public String getV20302_039_052() {
        return V20302_039_052;
    }

    public void setV20302_039_052(String v20302_039_052) {
        V20302_039_052 = v20302_039_052;
    }

    public Integer getV20302_019() {
        return V20302_019;
    }

    public void setV20302_019(Integer v20302_019) {
        V20302_019 = v20302_019;
    }

    public String getV20302_019_052() {
        return V20302_019_052;
    }

    public void setV20302_019_052(String v20302_019_052) {
        V20302_019_052 = v20302_019_052;
    }

    public Integer getV20302_016() {
        return V20302_016;
    }

    public void setV20302_016(Integer v20302_016) {
        V20302_016 = v20302_016;
    }

    public Integer getV20302_003() {
        return V20302_003;
    }

    public void setV20302_003(Integer v20302_003) {
        V20302_003 = v20302_003;
    }

    public Integer getV20302_031() {
        return V20302_031;
    }

    public void setV20302_031(Integer v20302_031) {
        V20302_031 = v20302_031;
    }

    public String getV20302_031_052() {
        return V20302_031_052;
    }

    public void setV20302_031_052(String v20302_031_052) {
        V20302_031_052 = v20302_031_052;
    }

    public Integer getV20302_007() {
        return V20302_007;
    }

    public void setV20302_007(Integer v20302_007) {
        V20302_007 = v20302_007;
    }

    public String getV20302_007_052() {
        return V20302_007_052;
    }

    public void setV20302_007_052(String v20302_007_052) {
        V20302_007_052 = v20302_007_052;
    }

    public Integer getV20302_006() {
        return V20302_006;
    }

    public void setV20302_006(Integer v20302_006) {
        V20302_006 = v20302_006;
    }

    public String getV20302_006_052() {
        return V20302_006_052;
    }

    public void setV20302_006_052(String v20302_006_052) {
        V20302_006_052 = v20302_006_052;
    }

    public Integer getV20302_004() {
        return V20302_004;
    }

    public void setV20302_004(Integer v20302_004) {
        V20302_004 = v20302_004;
    }

    public Integer getV20302_005() {
        return V20302_005;
    }

    public void setV20302_005(Integer v20302_005) {
        V20302_005 = v20302_005;
    }

    public Integer getV20302_008() {
        return V20302_008;
    }

    public void setV20302_008(Integer v20302_008) {
        V20302_008 = v20302_008;
    }

    public Integer getV20302_076() {
        return V20302_076;
    }

    public void setV20302_076(Integer v20302_076) {
        V20302_076 = v20302_076;
    }

    public Integer getV20302_017() {
        return V20302_017;
    }

    public void setV20302_017(Integer v20302_017) {
        V20302_017 = v20302_017;
    }

    public String getV20302_017_052() {
        return V20302_017_052;
    }

    public void setV20302_017_052(String v20302_017_052) {
        V20302_017_052 = v20302_017_052;
    }

    public Integer getV20302_013() {
        return V20302_013;
    }

    public void setV20302_013(Integer v20302_013) {
        V20302_013 = v20302_013;
    }

    public Integer getV20302_014() {
        return V20302_014;
    }

    public void setV20302_014(Integer v20302_014) {
        V20302_014 = v20302_014;
    }

    public String getV20302_014_052() {
        return V20302_014_052;
    }

    public void setV20302_014_052(String v20302_014_052) {
        V20302_014_052 = v20302_014_052;
    }

    public Integer getV20302_015() {
        return V20302_015;
    }

    public void setV20302_015(Integer v20302_015) {
        V20302_015 = v20302_015;
    }

    public String getV20302_015_052() {
        return V20302_015_052;
    }

    public void setV20302_015_052(String v20302_015_052) {
        V20302_015_052 = v20302_015_052;
    }

    public Integer getV20302_018() {
        return V20302_018;
    }

    public void setV20302_018(Integer v20302_018) {
        V20302_018 = v20302_018;
    }

    public String getV20302_018_052() {
        return V20302_018_052;
    }

    public void setV20302_018_052(String v20302_018_052) {
        V20302_018_052 = v20302_018_052;
    }

    public String getV20303() {
        return V20303;
    }

    public void setV20303(String v20303) {
        V20303 = v20303;
    }

    public String getV20304() {
        return V20304;
    }

    public void setV20304(String v20304) {
        V20304 = v20304;
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

    public BigDecimal getV14314() {
        return v14314;
    }

    public void setV14314(BigDecimal v14314) {
        this.v14314 = v14314;
    }

    public BigDecimal getV14308() {
        return v14308;
    }

    public void setV14308(BigDecimal v14308) {
        this.v14308 = v14308;
    }

    public BigDecimal getV14313() {
        return v14313;
    }

    public void setV14313(BigDecimal v14313) {
        this.v14313 = v14313;
    }

    public Integer getQ10004_701() {
        return Q10004_701;
    }

    public void setQ10004_701(Integer q10004_701) {
        Q10004_701 = q10004_701;
    }

    public Integer getQ10301() {
        return Q10301;
    }

    public void setQ10301(Integer q10301) {
        Q10301 = q10301;
    }

    public Integer getQ10301_052() {
        return Q10301_052;
    }

    public void setQ10301_052(Integer q10301_052) {
        Q10301_052 = q10301_052;
    }

    public Integer getQ10302() {
        return Q10302;
    }

    public void setQ10302(Integer q10302) {
        Q10302 = q10302;
    }

    public Integer getQ10302_052() {
        return Q10302_052;
    }

    public void setQ10302_052(Integer q10302_052) {
        Q10302_052 = q10302_052;
    }

    public Integer getQ10051_701() {
        return Q10051_701;
    }

    public void setQ10051_701(Integer q10051_701) {
        Q10051_701 = q10051_701;
    }

    public Integer getQ12001_701() {
        return Q12001_701;
    }

    public void setQ12001_701(Integer q12001_701) {
        Q12001_701 = q12001_701;
    }

    public Integer getQ12011() {
        return Q12011;
    }

    public void setQ12011(Integer q12011) {
        Q12011 = q12011;
    }

    public Integer getQ12011_052() {
        return Q12011_052;
    }

    public void setQ12011_052(Integer q12011_052) {
        Q12011_052 = q12011_052;
    }

    public Integer getQ12012() {
        return Q12012;
    }

    public void setQ12012(Integer q12012) {
        Q12012 = q12012;
    }

    public Integer getQ12012_052() {
        return Q12012_052;
    }

    public void setQ12012_052(Integer q12012_052) {
        Q12012_052 = q12012_052;
    }

    public Integer getQ13004_701() {
        return Q13004_701;
    }

    public void setQ13004_701(Integer q13004_701) {
        Q13004_701 = q13004_701;
    }

    public Integer getQ13003_701() {
        return Q13003_701;
    }

    public void setQ13003_701(Integer q13003_701) {
        Q13003_701 = q13003_701;
    }

    public Integer getQ13007() {
        return Q13007;
    }

    public void setQ13007(Integer q13007) {
        Q13007 = q13007;
    }

    public Integer getQ13007_052() {
        return Q13007_052;
    }

    public void setQ13007_052(Integer q13007_052) {
        Q13007_052 = q13007_052;
    }

    public Integer getQ20010_701() {
        return Q20010_701;
    }

    public void setQ20010_701(Integer q20010_701) {
        Q20010_701 = q20010_701;
    }

    public Integer getQ20051_701() {
        return Q20051_701;
    }

    public void setQ20051_701(Integer q20051_701) {
        Q20051_701 = q20051_701;
    }

    public Integer getQ20059() {
        return Q20059;
    }

    public void setQ20059(Integer q20059) {
        Q20059 = q20059;
    }

    public Integer getQ20059_052() {
        return Q20059_052;
    }

    public void setQ20059_052(Integer q20059_052) {
        Q20059_052 = q20059_052;
    }

    public Integer getQ13302_01() {
        return Q13302_01;
    }

    public void setQ13302_01(Integer q13302_01) {
        Q13302_01 = q13302_01;
    }

    public Integer getQ13302_01_052() {
        return Q13302_01_052;
    }

    public void setQ13302_01_052(Integer q13302_01_052) {
        Q13302_01_052 = q13302_01_052;
    }

    public Integer getQ13308() {
        return Q13308;
    }

    public void setQ13308(Integer q13308) {
        Q13308 = q13308;
    }

    public Integer getQ13309() {
        return Q13309;
    }

    public void setQ13309(Integer q13309) {
        Q13309 = q13309;
    }

    public Integer getQ13305() {
        return Q13305;
    }

    public void setQ13305(Integer q13305) {
        Q13305 = q13305;
    }

    public Integer getQ13306() {
        return Q13306;
    }

    public void setQ13306(Integer q13306) {
        Q13306 = q13306;
    }

    public Integer getQ13032() {
        return Q13032;
    }

    public void setQ13032(Integer q13032) {
        Q13032 = q13032;
    }

    public Integer getQ13033() {
        return Q13033;
    }

    public void setQ13033(Integer q13033) {
        Q13033 = q13033;
    }

    public Integer getQ13013() {
        return Q13013;
    }

    public void setQ13013(Integer q13013) {
        Q13013 = q13013;
    }

    public Integer getQ13330() {
        return Q13330;
    }

    public void setQ13330(Integer q13330) {
        Q13330 = q13330;
    }

    public Integer getQ20305() {
        return Q20305;
    }

    public void setQ20305(Integer q20305) {
        Q20305 = q20305;
    }

    public Integer getQ20326_NS() {
        return Q20326_NS;
    }

    public void setQ20326_NS(Integer q20326_NS) {
        Q20326_NS = q20326_NS;
    }

    public Integer getQ20306_NS() {
        return Q20306_NS;
    }

    public void setQ20306_NS(Integer q20306_NS) {
        Q20306_NS = q20306_NS;
    }

    public Integer getQ20307_NS() {
        return Q20307_NS;
    }

    public void setQ20307_NS(Integer q20307_NS) {
        Q20307_NS = q20307_NS;
    }

    public Integer getQ20326_WE() {
        return Q20326_WE;
    }

    public void setQ20326_WE(Integer q20326_WE) {
        Q20326_WE = q20326_WE;
    }

    public Integer getQ20306_WE() {
        return Q20306_WE;
    }

    public void setQ20306_WE(Integer q20306_WE) {
        Q20306_WE = q20306_WE;
    }

    public Integer getQ20307_WE() {
        return Q20307_WE;
    }

    public void setQ20307_WE(Integer q20307_WE) {
        Q20307_WE = q20307_WE;
    }

    public Integer getQ12001() {
        return Q12001;
    }

    public void setQ12001(Integer q12001) {
        Q12001 = q12001;
    }

    public Integer getQ11001() {
        return Q11001;
    }

    public void setQ11001(Integer q11001) {
        Q11001 = q11001;
    }

    public Integer getQ11002() {
        return Q11002;
    }

    public void setQ11002(Integer q11002) {
        Q11002 = q11002;
    }

    public Integer getQ11290_CHAR() {
        return Q11290_CHAR;
    }

    public void setQ11290_CHAR(Integer q11290_CHAR) {
        Q11290_CHAR = q11290_CHAR;
    }

    public Integer getQ11291_701() {
        return Q11291_701;
    }

    public void setQ11291_701(Integer q11291_701) {
        Q11291_701 = q11291_701;
    }

    public Integer getQ11293_701() {
        return Q11293_701;
    }

    public void setQ11293_701(Integer q11293_701) {
        Q11293_701 = q11293_701;
    }

    public Integer getQ11296() {
        return Q11296;
    }

    public void setQ11296(Integer q11296) {
        Q11296 = q11296;
    }

    public Integer getQ11042() {
        return Q11042;
    }

    public void setQ11042(Integer q11042) {
        Q11042 = q11042;
    }

    public Integer getQ11042_052() {
        return Q11042_052;
    }

    public void setQ11042_052(Integer q11042_052) {
        Q11042_052 = q11042_052;
    }

    public Integer getQ11211() {
        return Q11211;
    }

    public void setQ11211(Integer q11211) {
        Q11211 = q11211;
    }

    public Integer getQ11046() {
        return Q11046;
    }

    public void setQ11046(Integer q11046) {
        Q11046 = q11046;
    }

    public Integer getQ11046_052() {
        return Q11046_052;
    }

    public void setQ11046_052(Integer q11046_052) {
        Q11046_052 = q11046_052;
    }

    public Integer getQ12120_701() {
        return Q12120_701;
    }

    public void setQ12120_701(Integer q12120_701) {
        Q12120_701 = q12120_701;
    }

    public Integer getQ12311() {
        return Q12311;
    }

    public void setQ12311(Integer q12311) {
        Q12311 = q12311;
    }

    public Integer getQ12311_052() {
        return Q12311_052;
    }

    public void setQ12311_052(Integer q12311_052) {
        Q12311_052 = q12311_052;
    }

    public Integer getQ12121() {
        return Q12121;
    }

    public void setQ12121(Integer q12121) {
        Q12121 = q12121;
    }

    public Integer getQ12121_052() {
        return Q12121_052;
    }

    public void setQ12121_052(Integer q12121_052) {
        Q12121_052 = q12121_052;
    }

    public Integer getQ12030_701_005() {
        return Q12030_701_005;
    }

    public void setQ12030_701_005(Integer q12030_701_005) {
        Q12030_701_005 = q12030_701_005;
    }

    public Integer getQ12030_701_010() {
        return Q12030_701_010;
    }

    public void setQ12030_701_010(Integer q12030_701_010) {
        Q12030_701_010 = q12030_701_010;
    }

    public Integer getQ12030_701_015() {
        return Q12030_701_015;
    }

    public void setQ12030_701_015(Integer q12030_701_015) {
        Q12030_701_015 = q12030_701_015;
    }

    public Integer getQ12030_701_020() {
        return Q12030_701_020;
    }

    public void setQ12030_701_020(Integer q12030_701_020) {
        Q12030_701_020 = q12030_701_020;
    }

    public Integer getQ12030_701_040() {
        return Q12030_701_040;
    }

    public void setQ12030_701_040(Integer q12030_701_040) {
        Q12030_701_040 = q12030_701_040;
    }

    public Integer getQ12030_701_080() {
        return Q12030_701_080;
    }

    public void setQ12030_701_080(Integer q12030_701_080) {
        Q12030_701_080 = q12030_701_080;
    }

    public Integer getQ12030_701_160() {
        return Q12030_701_160;
    }

    public void setQ12030_701_160(Integer q12030_701_160) {
        Q12030_701_160 = q12030_701_160;
    }

    public Integer getQ12030_701_320() {
        return Q12030_701_320;
    }

    public void setQ12030_701_320(Integer q12030_701_320) {
        Q12030_701_320 = q12030_701_320;
    }

    public Integer getQ20330_01() {
        return Q20330_01;
    }

    public void setQ20330_01(Integer q20330_01) {
        Q20330_01 = q20330_01;
    }

    public Integer getQ20331_01() {
        return Q20331_01;
    }

    public void setQ20331_01(Integer q20331_01) {
        Q20331_01 = q20331_01;
    }

    public Integer getQ20330_02() {
        return Q20330_02;
    }

    public void setQ20330_02(Integer q20330_02) {
        Q20330_02 = q20330_02;
    }

    public Integer getQ20331_02() {
        return Q20331_02;
    }

    public void setQ20331_02(Integer q20331_02) {
        Q20331_02 = q20331_02;
    }

    public Integer getQ14032() {
        return Q14032;
    }

    public void setQ14032(Integer q14032) {
        Q14032 = q14032;
    }

    public Integer getQ20411() {
        return Q20411;
    }

    public void setQ20411(Integer q20411) {
        Q20411 = q20411;
    }

    public Integer getQ20412() {
        return Q20412;
    }

    public void setQ20412(Integer q20412) {
        Q20412 = q20412;
    }

    public Integer getQ12314_701() {
        return Q12314_701;
    }

    public void setQ12314_701(Integer q12314_701) {
        Q12314_701 = q12314_701;
    }

    public Integer getQ12315() {
        return Q12315;
    }

    public void setQ12315(Integer q12315) {
        Q12315 = q12315;
    }

    public Integer getQ12315_052() {
        return Q12315_052;
    }

    public void setQ12315_052(Integer q12315_052) {
        Q12315_052 = q12315_052;
    }

    public Integer getQ12316() {
        return Q12316;
    }

    public void setQ12316(Integer q12316) {
        Q12316 = q12316;
    }

    public Integer getQ12316_052() {
        return Q12316_052;
    }

    public void setQ12316_052(Integer q12316_052) {
        Q12316_052 = q12316_052;
    }

    public Integer getQ20062() {
        return Q20062;
    }

    public void setQ20062(Integer q20062) {
        Q20062 = q20062;
    }

    public Integer getQ20302_060() {
        return Q20302_060;
    }

    public void setQ20302_060(Integer q20302_060) {
        Q20302_060 = q20302_060;
    }

    public Integer getQ20302_060_052() {
        return Q20302_060_052;
    }

    public void setQ20302_060_052(Integer q20302_060_052) {
        Q20302_060_052 = q20302_060_052;
    }

    public Integer getQ20302_070() {
        return Q20302_070;
    }

    public void setQ20302_070(Integer q20302_070) {
        Q20302_070 = q20302_070;
    }

    public Integer getQ20302_070_052() {
        return Q20302_070_052;
    }

    public void setQ20302_070_052(Integer q20302_070_052) {
        Q20302_070_052 = q20302_070_052;
    }

    public Integer getQ20302_089() {
        return Q20302_089;
    }

    public void setQ20302_089(Integer q20302_089) {
        Q20302_089 = q20302_089;
    }

    public Integer getQ20302_089_052() {
        return Q20302_089_052;
    }

    public void setQ20302_089_052(Integer q20302_089_052) {
        Q20302_089_052 = q20302_089_052;
    }

    public Integer getQ20302_042() {
        return Q20302_042;
    }

    public void setQ20302_042(Integer q20302_042) {
        Q20302_042 = q20302_042;
    }

    public Integer getQ20302_042_052() {
        return Q20302_042_052;
    }

    public void setQ20302_042_052(Integer q20302_042_052) {
        Q20302_042_052 = q20302_042_052;
    }

    public Integer getQ20302_010() {
        return Q20302_010;
    }

    public void setQ20302_010(Integer q20302_010) {
        Q20302_010 = q20302_010;
    }

    public Integer getQ20302_001() {
        return Q20302_001;
    }

    public void setQ20302_001(Integer q20302_001) {
        Q20302_001 = q20302_001;
    }

    public Integer getQ20302_002() {
        return Q20302_002;
    }

    public void setQ20302_002(Integer q20302_002) {
        Q20302_002 = q20302_002;
    }

    public Integer getQ20302_056() {
        return Q20302_056;
    }

    public void setQ20302_056(Integer q20302_056) {
        Q20302_056 = q20302_056;
    }

    public Integer getQ20302_056_052() {
        return Q20302_056_052;
    }

    public void setQ20302_056_052(Integer q20302_056_052) {
        Q20302_056_052 = q20302_056_052;
    }

    public Integer getQ20302_048() {
        return Q20302_048;
    }

    public void setQ20302_048(Integer q20302_048) {
        Q20302_048 = q20302_048;
    }

    public Integer getQ20302_048_052() {
        return Q20302_048_052;
    }

    public void setQ20302_048_052(Integer q20302_048_052) {
        Q20302_048_052 = q20302_048_052;
    }

    public Integer getQ20302_038() {
        return Q20302_038;
    }

    public void setQ20302_038(Integer q20302_038) {
        Q20302_038 = q20302_038;
    }

    public Integer getQ20302_038_052() {
        return Q20302_038_052;
    }

    public void setQ20302_038_052(Integer q20302_038_052) {
        Q20302_038_052 = q20302_038_052;
    }

    public Integer getQ20302_039() {
        return Q20302_039;
    }

    public void setQ20302_039(Integer q20302_039) {
        Q20302_039 = q20302_039;
    }

    public Integer getQ20302_039_052() {
        return Q20302_039_052;
    }

    public void setQ20302_039_052(Integer q20302_039_052) {
        Q20302_039_052 = q20302_039_052;
    }

    public Integer getQ20302_019() {
        return Q20302_019;
    }

    public void setQ20302_019(Integer q20302_019) {
        Q20302_019 = q20302_019;
    }

    public Integer getQ20302_019_052() {
        return Q20302_019_052;
    }

    public void setQ20302_019_052(Integer q20302_019_052) {
        Q20302_019_052 = q20302_019_052;
    }

    public Integer getQ20302_016() {
        return Q20302_016;
    }

    public void setQ20302_016(Integer q20302_016) {
        Q20302_016 = q20302_016;
    }

    public Integer getQ20302_003() {
        return Q20302_003;
    }

    public void setQ20302_003(Integer q20302_003) {
        Q20302_003 = q20302_003;
    }

    public Integer getQ20302_031() {
        return Q20302_031;
    }

    public void setQ20302_031(Integer q20302_031) {
        Q20302_031 = q20302_031;
    }

    public Integer getQ20302_031_052() {
        return Q20302_031_052;
    }

    public void setQ20302_031_052(Integer q20302_031_052) {
        Q20302_031_052 = q20302_031_052;
    }

    public Integer getQ20302_007() {
        return Q20302_007;
    }

    public void setQ20302_007(Integer q20302_007) {
        Q20302_007 = q20302_007;
    }

    public Integer getQ20302_007_052() {
        return Q20302_007_052;
    }

    public void setQ20302_007_052(Integer q20302_007_052) {
        Q20302_007_052 = q20302_007_052;
    }

    public Integer getQ20302_006() {
        return Q20302_006;
    }

    public void setQ20302_006(Integer q20302_006) {
        Q20302_006 = q20302_006;
    }

    public Integer getQ20302_006_052() {
        return Q20302_006_052;
    }

    public void setQ20302_006_052(Integer q20302_006_052) {
        Q20302_006_052 = q20302_006_052;
    }

    public Integer getQ20302_004() {
        return Q20302_004;
    }

    public void setQ20302_004(Integer q20302_004) {
        Q20302_004 = q20302_004;
    }

    public Integer getQ20302_005() {
        return Q20302_005;
    }

    public void setQ20302_005(Integer q20302_005) {
        Q20302_005 = q20302_005;
    }

    public Integer getQ20302_008() {
        return Q20302_008;
    }

    public void setQ20302_008(Integer q20302_008) {
        Q20302_008 = q20302_008;
    }

    public Integer getQ20302_076() {
        return Q20302_076;
    }

    public void setQ20302_076(Integer q20302_076) {
        Q20302_076 = q20302_076;
    }

    public Integer getQ20302_017() {
        return Q20302_017;
    }

    public void setQ20302_017(Integer q20302_017) {
        Q20302_017 = q20302_017;
    }

    public Integer getQ20302_017_052() {
        return Q20302_017_052;
    }

    public void setQ20302_017_052(Integer q20302_017_052) {
        Q20302_017_052 = q20302_017_052;
    }

    public Integer getQ20302_013() {
        return Q20302_013;
    }

    public void setQ20302_013(Integer q20302_013) {
        Q20302_013 = q20302_013;
    }

    public Integer getQ20302_014() {
        return Q20302_014;
    }

    public void setQ20302_014(Integer q20302_014) {
        Q20302_014 = q20302_014;
    }

    public Integer getQ20302_014_052() {
        return Q20302_014_052;
    }

    public void setQ20302_014_052(Integer q20302_014_052) {
        Q20302_014_052 = q20302_014_052;
    }

    public Integer getQ20302_015() {
        return Q20302_015;
    }

    public void setQ20302_015(Integer q20302_015) {
        Q20302_015 = q20302_015;
    }

    public Integer getQ20302_015_052() {
        return Q20302_015_052;
    }

    public void setQ20302_015_052(Integer q20302_015_052) {
        Q20302_015_052 = q20302_015_052;
    }

    public Integer getQ20302_018() {
        return Q20302_018;
    }

    public void setQ20302_018(Integer q20302_018) {
        Q20302_018 = q20302_018;
    }

    public Integer getQ20302_018_052() {
        return Q20302_018_052;
    }

    public void setQ20302_018_052(Integer q20302_018_052) {
        Q20302_018_052 = q20302_018_052;
    }

    public Integer getQ20303() {
        return Q20303;
    }

    public void setQ20303(Integer q20303) {
        Q20303 = q20303;
    }

    public Integer getQ20304() {
        return Q20304;
    }

    public void setQ20304(Integer q20304) {
        Q20304 = q20304;
    }
}
