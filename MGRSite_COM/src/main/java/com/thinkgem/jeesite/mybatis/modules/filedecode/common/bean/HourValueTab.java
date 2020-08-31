package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;


import java.io.Serializable;
import java.math.BigDecimal;


/**
 *  cimiss地面小时值表信息
 * @author yang.kq
 * @version 1.0
 * @date 2020/3/19 11:11
 */

public class HourValueTab implements Serializable {
    private static final long serialVersionUID = 1L;
    private String D_RETAIN_ID;//记录标识 ( 系统自动生成的流水号 )
    private String D_DATA_ID;//资料标识 ( 资料的4级编码 )
    private String D_IYMDHM;//入库时间 ( 插表时的系统时间 )
    private String D_RYMDHM;//收到时间 ( DPC消息生成时间 )
    private String D_UPDATE_TIME;//更新时间 ( 根据CCx对记录更新时的系统时间 )
    private String D_DATETIME;//资料时间 ( 由V04001、V04002、V04003、V04004 )
    private String V_BBB;//更正标志 ( 组成 )
    private String V01301;//区站号(字符)
    private String V01300;//区站号(数字)
    private Float V05001;//纬度
    private Float V06001;//经度 ( 单位：度 )
    private Float V07001;//测站高度 ( 单位：度 )
    private Float V07031;//气压传感器海拔高度 ( 单位：米 )
    private Float V07032_04;//风速传感器距地面高度 ( 单位：米 )
    private String V02001;//测站类型 ( 代码表 )
    private Integer V02301;//台站级别 ( 代码表 )
    private Integer V_ACODE;//中国行政区划代码 ( 代码表 )
    private Integer V04001;//资料观测年 ( 代码表 )
    private Integer V04002;//资料观测月
    private Integer V04003;//资料观测日
    private Integer V04004;//资料观测时
    private Float V10004;//本站气压
    private Float V10051;//海平面气压 ( 单位：百帕 )
    private Float V10061;//3小时变压 ( 单位：百帕 )
    private Float V10062;//24小时变压 ( 单位：百帕 )
    private Float V10301;//小时内最高本站气压 ( 单位：百帕 )
    private Integer V10301_052;//小时内最高本站气压出现时间 ( 格式：hhmm )
    private Float V10302;//小时内最低本站气压 ( 单位：百帕  )
    private Integer V10302_052;//小时内最低本站气压出现时间 ( 格式：hhmm )
    private Float V12001;//气温 ( 单位：摄氏度 )
    private Float V12011;//小时内最高气温 ( 单位：摄氏度 )
    private Integer V12011_052;//小时内最高气温出现时间 ( 单位：摄氏度 )
    private Float V12012;//小时内最低气温 (单位：摄氏度  )
    private Integer V12012_052;//小时内最低气温出现时间 ( 格式：hhmm )
    private Float V12405;//24小时变温 (单位：摄氏度  )
    private Float V12016;//过去24小时最高气温 ( 单位：摄氏度 )
    private Float V12017;//过去24小时最低气温 ( 单位：摄氏度 )
    private Float balltemp;//湿球温度
    private Float V12003;//露点温度 ( 单位：摄氏度 )
    private Integer V13003;//相对湿度 (单位：% )
    private Float V13007;//小时内最小相对湿度 ( 单位：% )
    private Integer V13007_052;//小时内最小相对湿度出现时间 (格式：hhmm)
    private Float V13004;//水汽压 (单位：百帕 )
    private Float V13019;//1小时降水量 ( 单位：毫米   )
    private Float V13020;//过去3小时降水量 ( 单位：毫米 )
    private Float V13021;//过去6小时降水量 ( 单位：毫米 )
    private Float V13022;//过去12小时降水量 ( 单位：毫米 )
    private Float V13023;//过去24小时降水量 ( 单位：毫米 )
    private Integer V04080_04;//人工加密观测降水量描述时间周期 ( 单位：毫米 )
    private Float V13011;//人工加密观测降水量 ( 单位：小时 )
    private Float V13033;//小时蒸发量 ( 单位：毫米 )
    private String V11290;//2分钟平均风向 ( 单位：度 )
    private Float V11291;//2分钟平均风速 ( 单位：米/秒  )
    private String V11292;//10分钟平均风向 ( 单位：度  )
    private Float V11293;//10分钟平均风速 (单位：米/秒)
    private String V11296;//小时内最大风速的风向 ( 单位：度 )
    private Float V11042;//小时内最大风速 (单位：米/秒)
    private Integer V11042_052;//小时内最大风速出现时间 ( 格式：hhmm )
    private String V11201;//瞬时风向 ( 单位：度 )
    private Float V11202;//瞬时风速 ( 单位：米/秒 )
    private Float V11211;//小时内极大风速的风向 ( 单位：米/秒 )
    private Float V11046;//小时内极大风速 ( 单位：度 )
    private Integer V11046_052;//小时内极大风速出现时间 (格式：hhmm  )
    private Float V11503_06;//过去6小时极大瞬时风向 (单位：度  )
    private Float V11504_06;//过去6小时极大瞬时风速 (单位：米/秒  )
    private Float V11503_12;//过去12小时极大瞬时风向 ( 单位：度  )
    private Float V11504_12;//过去12小时极大瞬时风速 ( 单位：米/秒)
    private Float V12120;//地面温度 ( 单位：摄氏度 )
    private Float V12311;//小时内最高地面温度 ( 单位：摄氏度 )
    private Integer V12311_052;//小时内最高地面温度出现时间 (  格式：hhmm)
    private Float V12121;//小时内最低地面温度 (单位：摄氏度  )
    private Integer V12121_052;//小时内最低地面温度出现时间 ( 格式：hhmm)
    private Float V12013;//过去12小时最低地面温度 (单位：摄氏度  )
    private Float V12030_005;//5cm地温 ( 单位：摄氏度 )
    private Float V12030_010;//10cm地温 ( 单位：摄氏度 )
    private Float V12030_015;//15cm地温 ( 单位：摄氏度 )
    private Float V12030_020;//20cm地温 ( 单位：摄氏度 )
    private Float V12030_040;//40cm地温 ( 单位：摄氏度 )
    private Float V12030_080;//80cm地温 ( 单位：摄氏度 )
    private Float V12030_160;//160cm地温 ( 单位：摄氏度 )
    private Float V12030_320;//320cm地温 ( 单位：摄氏度 )
    private Float V12314;//草面（雪面）温度 ( 单位：摄氏度 )
    private Float V12315;//小时内草面（雪面）最高温度 ( 单位：摄氏度 )
    private Integer V12315_052;//小时内草面（雪面）最高温度出现时间 ( 格式：hhmm )
    private Float V12316;//小时内草面（雪面）最低温度 ( 单位：摄氏度 )
    private Integer V12316_052;//小时内草面（雪面）最低温度出现时间 ( 格式：hhmm  )
    private Float V20001_701_01;//1分钟平均水平能见度 ( 单位：米 )
    private Float V20001_701_10;//10分钟平均水平能见度 ( 单位：米 )
    private Float V20059;//最小水平能见度 ( 单位：米 )
    private String V20059_052;//最小水平能见度出现时间 ( 格式：hhmm  )
    private Float V20001;//水平能见度（人工） ( 单位：千米 )
    private Integer visibility2;//能见度级别
    private Integer V20010;//总云量 (单位：%  )
    private Integer V20051;//低云量 ( 单位：% )
    private Float V20011;//低云或中云的云量 ( 单位：% )
    private Float V20013;//低云或中云的云高 ( 单位：% )
    private Integer V20350_01;//云状1 ( 单位：米 )
    private String V20350_02;//云状2 ( 代码表 )
    private Integer V20350_03;//云状3 ( 代码表 )
    private Integer V20350_04;//云状4 ( 代码表 )
    private Integer V20350_05;//云状5 ( 代码表 )
    private Integer V20350_06;//云状6 ( 代码表 )
    private Integer V20350_07;//云状7 ( 代码表 )
    private Integer V20350_08;//云状8 ( 代码表 )
    private Integer V20350_11;//低云状 ( 代码表 )
    private Integer V20350_12;//中云状 ( 代码表 )
    private Integer V20350_13;//高云状 ( 代码表 )
    private Integer V20003;//现在天气 ( 代码表 )
    private Integer V04080_05;//过去天气描述时间周期 ( 代码表 )
    private Integer V20004;//过去天气1 ( 单位:小时 )
    private Integer V20005;//过去天气2 ( 代码表 )
    private Integer V20062;//地面状态 ( 代码表 )
    private Float V13013;//积雪深度 ( 代码表 )
    private Float V13330;//雪压 ( 单位：厘米 )
    private Float V20330_01;//冻土第1冻结层上限值 ( 单位：g/cm2 )
    private Float V20331_01;//冻土第1冻结层下限值 ( 单位：厘米 )
    private Float V20330_02;//冻土第2冻结层上限值 ( 单位：厘米 )
    private Float V20331_02;//冻土第2冻结层下限值 ( 单位：厘米 )
    private BigDecimal v14320;//总辐射曝辐量
    private BigDecimal v14308;//净辐射曝辐量
    private Integer Q10004;//本站气压质量标志 ( 单位：厘米 )
    private Integer Q10051;//海平面气压质量标志 ( 代码表 )
    private Integer Q10061;//3小时变压质量标志 ( 代码表 )
    private Integer Q10062;//24小时变压质质量标志 ( 代码表 )
    private Integer Q10301;//小时内最高本站气压质量标志 ( 代码表 )
    private Integer Q10301_052;//小时内最高本站气压出现时间质量标志 ( 代码表 )
    private Integer Q10302;//小时内最低本站气压质量标志 ( 代码表 )
    private Integer Q10302_052;//小时内最低本站气压出现时间质量标志 ( 代码表 )
    private Integer Q12001;//气温质量标志 ( 代码表 )
    private Integer Q12011;//小时内最高气温质量标志 ( 代码表 )
    private Integer Q12011_052;//小时内最高气温出现时间质量标志 ( 代码表 )
    private Integer Q12012;//小时内最低气温质量标志 ( 代码表 )
    private Integer Q12012_052;//小时内最低气温出现时间质量标志 ( 代码表 )
    private Integer Q12405;//24小时变温质量标志 ( 代码表 )
    private Integer Q12016;//过去24小时最高气温质量标志 ( 代码表 )
    private Integer Q12017;//过去24小时最低气温质量标志 ( 代码表 )
    private Integer Q12003;//露点温度质量标志 ( 代码表 )
    private Integer Q13003;//相对湿度质量标志 ( 代码表 )
    private Integer Q13007;//小时内最小相对湿度质量标志 ( 代码表 )
    private Integer Q13007_052;//小时内最小相对湿度出现时间质量标志 ( 代码表 )
    private Integer Q13004;//水汽压质量标志 ( 代码表 )
    private Integer Q13019;//1小时降水量质量标志 ( 代码表 )
    private Integer Q13020;//过去3小时降水量质量标志 ( 代码表 )
    private Integer Q13021;//过去6小时降水量质量标志 ( 代码表 )
    private Integer Q13022;//过去12小时降水量质量标志 ( 代码表 )
    private Integer Q13023;//24小时降水量质 ( 代码表 )
    private Integer Q04080_04;//人工加密观测降水量描述时间周期质量标志 ( 代码表 )
    private Integer Q13011;//人工加密观测降水量质量标志 ( 代码表 )
    private Integer Q13033;//小时蒸发量质量标志 ( 代码表 )
    private Integer Q11290;//2分钟平均风向质量标志 ( 代码表 )
    private Integer Q11291;//2分钟平均风速质量标志 ( 代码表 )
    private Integer Q11292;//10分钟平均风向质量标志 ( 代码表 )
    private Integer Q11293;//10分钟平均风速质量标志 ( 代码表 )
    private Integer Q11296;//小时内最大风速的风向质量标志 ( 代码表 )
    private Integer Q11042;//小时内最大风速质量标志 ( 代码表 )
    private Integer Q11042_052;//小时内最大风速出现时间质量标志 ( 代码表 )
    private Integer Q11201;//瞬时风向质量标志 ( 代码表 )
    private Integer Q11202;//瞬时风速质量标志 ( 代码表 )
    private Integer Q11211;//小时内极大风速的风向质量标志 ( 代码表 )
    private Integer Q11046;//小时内极大风速质量标志 ( 代码表 )
    private Integer Q11046_052;//小时内极大风速出现时间质量标志 ( 代码表 )
    private Integer Q11503_06;//过去6小时极大瞬时风向质量标志 ( 代码表 )
    private Integer Q11504_06;//过去6小时极大瞬时风速质量标志 ( 代码表 )
    private Integer Q11503_12;//过去12小时极大瞬时风向质量标志 ( 代码表 )
    private Integer Q11504_12;//过去12小时极大瞬时风速质量标志 ( 代码表 )
    private Integer Q12120;//地面温度质量标志 ( 代码表 )
    private Integer Q12311;//小时内最高地面温度质量标志 ( 代码表 )
    private Integer Q12311_052;//小时内最高地面温度出现时间质量标志 ( 代码表 )
    private Integer Q12121;//小时内最低地面温度质量标志 ( 代码表 )
    private Integer Q12121_052;//小时内最低地面温度出现时间质量标志 ( 代码表 )
    private Integer Q12013;//过去12小时最低地面温度 ( 代码表 )
    private Integer Q12030_005;//5cm地温质量标志 ( 代码表 )
    private Integer Q12030_010;//10cm地温质量标志 ( 代码表 )
    private Integer Q12030_015;//15cm地温质量标志 ( 代码表 )
    private Integer Q12030_020;//20cm地温质量标志 ( 代码表 )
    private Integer Q12030_040;//40cm地温质量标志 ( 代码表 )
    private Integer Q12030_080;//80cm地温质量标志 ( 代码表 )
    private Integer Q12030_160;//160cm地温质量标志 ( 代码表 )
    private Integer Q12030_320;//320cm地温质量标志 ( 代码表 )
    private Integer Q12314;//草面（雪面）温度质量标志 ( 代码表 )
    private Integer Q12315;//小时内草面（雪面）最高温度质量标志 ( 代码表 )
    private Integer Q12315_052;//小时内草面（雪面）最高温度出现时间质量标志 ( 代码表 )
    private Integer Q12316;//小时内草面（雪面）最低温度质量标志 ( 代码表 )
    private Integer Q12316_052;//小时内草面（雪面）最低温度出现时间质量标志 ( 代码表 )
    private Integer Q20001_701_01;//1分钟平均水平能见度质量标志 ( 代码表 )
    private Integer Q20001_701_10;//10分钟平均水平能见度质量标志 ( 代码表 )
    private Integer Q20059;//最小水平能见度质量标志 ( 代码表 )
    private Integer Q20059_052;//最小水平能见度出现时间质量标志 ( 代码表 )
    private Integer Q20001;//水平能见度（人工）质量标志 ( 代码表 )
    private Integer Q20010;//总云量质量标志 ( 代码表 )
    private Integer Q20051;//低云量质量标志 ( 代码表 )
    private Integer Q20011;//低云或中云的云量质量标志 ( 代码表 )
    private Integer Q20013;//低云或中云的云高质量标志 ( 代码表 )
    private Integer Q20350_01;//云状1质量标志 ( 代码表 )
    private Integer Q20350_02;//云状2质量标志 ( 代码表 )
    private Integer Q20350_03;//云状3质量标志 ( 代码表 )
    private Integer Q20350_04;//云状4质量标志 ( 代码表 )
    private Integer Q20350_05;//云状5质量标志 ( 代码表 )
    private Integer Q20350_06;//云状6质量标志 ( 代码表 )
    private Integer Q20350_07;//云状7质量标志 ( 代码表 )
    private Integer Q20350_08;//云状8质量标志 ( 代码表 )
    private Integer Q20350_11;//低云状质量标志 ( 代码表 )
    private Integer Q20350_12;//中云状质量标志 ( 代码表 )
    private Integer Q20350_13;//高云状质量标志 ( 代码表 )
    private Integer Q20003;//现在天气质量标志 ( 代码表 )
    private Integer Q04080_05;//过去天气描述时间周期质量标志 ( 代码表 )
    private Integer Q20004;//过去天气1质量标志 ( 代码表 )
    private Integer Q20005;//过去天气2质量标志 ( 代码表 )
    private Integer Q20062;//地面状态质量标志 ( 代码表 )
    private Integer Q13013;//积雪深度质量标志 ( 代码表 )
    private Integer Q13330;//雪压质量标志 ( 代码表 )
    private Integer Q20330_01;//冻土第1冻结层上限值质量标志 ( 代码表 )
    private Integer Q20331_01;//冻土第1冻结层下限值质量标志 ( 代码表 )
    private Integer Q20330_02;//冻土第2冻结层上限值质量标志 ( 代码表 )
    private Integer Q20331_02;//冻土第2冻结层下限值质量标志 ( 代码表 )

    public String getD_MAIN_ID() {
        return D_RETAIN_ID;
    }

    public void setD_MAIN_ID(String d_MAIN_ID) {
        D_RETAIN_ID = d_MAIN_ID;
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

    public Float getV07032_04() {
        return V07032_04;
    }

    public void setV07032_04(Float v07032_04) {
        V07032_04 = v07032_04;
    }

    public String getV02001() {
        return V02001;
    }

    public void setV02001(String v02001) {
        V02001 = v02001;
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

    public Integer getV04004() {
        return V04004;
    }

    public void setV04004(Integer v04004) {
        V04004 = v04004;
    }

    public Float getV10004() {
        return V10004;
    }

    public void setV10004(Float v10004) {
        V10004 = v10004;
    }

    public Float getV10051() {
        return V10051;
    }

    public void setV10051(Float v10051) {
        V10051 = v10051;
    }

    public Float getV10061() {
        return V10061;
    }

    public void setV10061(Float v10061) {
        V10061 = v10061;
    }

    public Float getV10062() {
        return V10062;
    }

    public void setV10062(Float v10062) {
        V10062 = v10062;
    }

    public Float getV10301() {
        return V10301;
    }

    public void setV10301(Float v10301) {
        V10301 = v10301;
    }

    public Integer getV10301_052() {
        return V10301_052;
    }

    public void setV10301_052(Integer v10301_052) {
        V10301_052 = v10301_052;
    }

    public Float getV10302() {
        return V10302;
    }

    public void setV10302(Float v10302) {
        V10302 = v10302;
    }

    public Integer getV10302_052() {
        return V10302_052;
    }

    public void setV10302_052(Integer v10302_052) {
        V10302_052 = v10302_052;
    }

    public Float getV12001() {
        return V12001;
    }

    public void setV12001(Float v12001) {
        V12001 = v12001;
    }

    public Float getV12011() {
        return V12011;
    }

    public void setV12011(Float v12011) {
        V12011 = v12011;
    }

    public Integer getV12011_052() {
        return V12011_052;
    }

    public void setV12011_052(Integer v12011_052) {
        V12011_052 = v12011_052;
    }

    public Float getV12012() {
        return V12012;
    }

    public void setV12012(Float v12012) {
        V12012 = v12012;
    }

    public Integer getV12012_052() {
        return V12012_052;
    }

    public void setV12012_052(Integer v12012_052) {
        V12012_052 = v12012_052;
    }

    public Float getV12405() {
        return V12405;
    }

    public void setV12405(Float v12405) {
        V12405 = v12405;
    }

    public Float getV12016() {
        return V12016;
    }

    public void setV12016(Float v12016) {
        V12016 = v12016;
    }

    public Float getV12017() {
        return V12017;
    }

    public void setV12017(Float v12017) {
        V12017 = v12017;
    }

    public Float getBalltemp() {
        return balltemp;
    }

    public void setBalltemp(Float balltemp) {
        this.balltemp = balltemp;
    }

    public Float getV12003() {
        return V12003;
    }

    public void setV12003(Float v12003) {
        V12003 = v12003;
    }

    public Integer getV13003() {
        return V13003;
    }

    public void setV13003(Integer v13003) {
        V13003 = v13003;
    }

    public Float getV13007() {
        return V13007;
    }

    public void setV13007(Float v13007) {
        V13007 = v13007;
    }

    public Integer getV13007_052() {
        return V13007_052;
    }

    public void setV13007_052(Integer v13007_052) {
        V13007_052 = v13007_052;
    }

    public Float getV13004() {
        return V13004;
    }

    public void setV13004(Float v13004) {
        V13004 = v13004;
    }

    public Float getV13019() {
        return V13019;
    }

    public void setV13019(Float v13019) {
        V13019 = v13019;
    }

    public Float getV13020() {
        return V13020;
    }

    public void setV13020(Float v13020) {
        V13020 = v13020;
    }

    public Float getV13021() {
        return V13021;
    }

    public void setV13021(Float v13021) {
        V13021 = v13021;
    }

    public Float getV13022() {
        return V13022;
    }

    public void setV13022(Float v13022) {
        V13022 = v13022;
    }

    public Float getV13023() {
        return V13023;
    }

    public void setV13023(Float v13023) {
        V13023 = v13023;
    }

    public Integer getV04080_04() {
        return V04080_04;
    }

    public void setV04080_04(Integer v04080_04) {
        V04080_04 = v04080_04;
    }

    public Float getV13011() {
        return V13011;
    }

    public void setV13011(Float v13011) {
        V13011 = v13011;
    }

    public Float getV13033() {
        return V13033;
    }

    public void setV13033(Float v13033) {
        V13033 = v13033;
    }

    public String getV11290() {
        return V11290;
    }

    public void setV11290(String v11290) {
        V11290 = v11290;
    }

    public Float getV11291() {
        return V11291;
    }

    public void setV11291(Float v11291) {
        V11291 = v11291;
    }

    public String getV11292() {
        return V11292;
    }

    public void setV11292(String v11292) {
        V11292 = v11292;
    }

    public Float getV11293() {
        return V11293;
    }

    public void setV11293(Float v11293) {
        V11293 = v11293;
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

    public Integer getV11042_052() {
        return V11042_052;
    }

    public void setV11042_052(Integer v11042_052) {
        V11042_052 = v11042_052;
    }

    public String getV11201() {
        return V11201;
    }

    public void setV11201(String v11201) {
        V11201 = v11201;
    }

    public Float getV11202() {
        return V11202;
    }

    public void setV11202(Float v11202) {
        V11202 = v11202;
    }

    public Float getV11211() {
        return V11211;
    }

    public void setV11211(Float v11211) {
        V11211 = v11211;
    }

    public Float getV11046() {
        return V11046;
    }

    public void setV11046(Float v11046) {
        V11046 = v11046;
    }

    public Integer getV11046_052() {
        return V11046_052;
    }

    public void setV11046_052(Integer v11046_052) {
        V11046_052 = v11046_052;
    }

    public Float getV11503_06() {
        return V11503_06;
    }

    public void setV11503_06(Float v11503_06) {
        V11503_06 = v11503_06;
    }

    public Float getV11504_06() {
        return V11504_06;
    }

    public void setV11504_06(Float v11504_06) {
        V11504_06 = v11504_06;
    }

    public Float getV11503_12() {
        return V11503_12;
    }

    public void setV11503_12(Float v11503_12) {
        V11503_12 = v11503_12;
    }

    public Float getV11504_12() {
        return V11504_12;
    }

    public void setV11504_12(Float v11504_12) {
        V11504_12 = v11504_12;
    }

    public Float getV12120() {
        return V12120;
    }

    public void setV12120(Float v12120) {
        V12120 = v12120;
    }

    public Float getV12311() {
        return V12311;
    }

    public void setV12311(Float v12311) {
        V12311 = v12311;
    }

    public Integer getV12311_052() {
        return V12311_052;
    }

    public void setV12311_052(Integer v12311_052) {
        V12311_052 = v12311_052;
    }

    public Float getV12121() {
        return V12121;
    }

    public void setV12121(Float v12121) {
        V12121 = v12121;
    }

    public Integer getV12121_052() {
        return V12121_052;
    }

    public void setV12121_052(Integer v12121_052) {
        V12121_052 = v12121_052;
    }

    public Float getV12013() {
        return V12013;
    }

    public void setV12013(Float v12013) {
        V12013 = v12013;
    }

    public Float getV12030_005() {
        return V12030_005;
    }

    public void setV12030_005(Float v12030_005) {
        V12030_005 = v12030_005;
    }

    public Float getV12030_010() {
        return V12030_010;
    }

    public void setV12030_010(Float v12030_010) {
        V12030_010 = v12030_010;
    }

    public Float getV12030_015() {
        return V12030_015;
    }

    public void setV12030_015(Float v12030_015) {
        V12030_015 = v12030_015;
    }

    public Float getV12030_020() {
        return V12030_020;
    }

    public void setV12030_020(Float v12030_020) {
        V12030_020 = v12030_020;
    }

    public Float getV12030_040() {
        return V12030_040;
    }

    public void setV12030_040(Float v12030_040) {
        V12030_040 = v12030_040;
    }

    public Float getV12030_080() {
        return V12030_080;
    }

    public void setV12030_080(Float v12030_080) {
        V12030_080 = v12030_080;
    }

    public Float getV12030_160() {
        return V12030_160;
    }

    public void setV12030_160(Float v12030_160) {
        V12030_160 = v12030_160;
    }

    public Float getV12030_320() {
        return V12030_320;
    }

    public void setV12030_320(Float v12030_320) {
        V12030_320 = v12030_320;
    }

    public Float getV12314() {
        return V12314;
    }

    public void setV12314(Float v12314) {
        V12314 = v12314;
    }

    public Float getV12315() {
        return V12315;
    }

    public void setV12315(Float v12315) {
        V12315 = v12315;
    }

    public Integer getV12315_052() {
        return V12315_052;
    }

    public void setV12315_052(Integer v12315_052) {
        V12315_052 = v12315_052;
    }

    public Float getV12316() {
        return V12316;
    }

    public void setV12316(Float v12316) {
        V12316 = v12316;
    }

    public Integer getV12316_052() {
        return V12316_052;
    }

    public void setV12316_052(Integer v12316_052) {
        V12316_052 = v12316_052;
    }

    public Float getV20001_701_01() {
        return V20001_701_01;
    }

    public void setV20001_701_01(Float v20001_701_01) {
        V20001_701_01 = v20001_701_01;
    }

    public Float getV20001_701_10() {
        return V20001_701_10;
    }

    public void setV20001_701_10(Float v20001_701_10) {
        V20001_701_10 = v20001_701_10;
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

    public Float getV20001() {
        return V20001;
    }

    public void setV20001(Float v20001) {
        V20001 = v20001;
    }

    public Integer getVisibility2() {
        return visibility2;
    }

    public void setVisibility2(Integer visibility2) {
        this.visibility2 = visibility2;
    }

    public Integer getV20010() {
        return V20010;
    }

    public void setV20010(Integer v20010) {
        V20010 = v20010;
    }

    public Integer getV20051() {
        return V20051;
    }

    public void setV20051(Integer v20051) {
        V20051 = v20051;
    }

    public Float getV20011() {
        return V20011;
    }

    public void setV20011(Float v20011) {
        V20011 = v20011;
    }

    public Float getV20013() {
        return V20013;
    }

    public void setV20013(Float v20013) {
        V20013 = v20013;
    }

    public Integer getV20350_01() {
        return V20350_01;
    }

    public void setV20350_01(Integer v20350_01) {
        V20350_01 = v20350_01;
    }

    public String getV20350_02() {
        return V20350_02;
    }

    public void setV20350_02(String v20350_02) {
        V20350_02 = v20350_02;
    }

    public Integer getV20350_03() {
        return V20350_03;
    }

    public void setV20350_03(Integer v20350_03) {
        V20350_03 = v20350_03;
    }

    public Integer getV20350_04() {
        return V20350_04;
    }

    public void setV20350_04(Integer v20350_04) {
        V20350_04 = v20350_04;
    }

    public Integer getV20350_05() {
        return V20350_05;
    }

    public void setV20350_05(Integer v20350_05) {
        V20350_05 = v20350_05;
    }

    public Integer getV20350_06() {
        return V20350_06;
    }

    public void setV20350_06(Integer v20350_06) {
        V20350_06 = v20350_06;
    }

    public Integer getV20350_07() {
        return V20350_07;
    }

    public void setV20350_07(Integer v20350_07) {
        V20350_07 = v20350_07;
    }

    public Integer getV20350_08() {
        return V20350_08;
    }

    public void setV20350_08(Integer v20350_08) {
        V20350_08 = v20350_08;
    }

    public Integer getV20350_11() {
        return V20350_11;
    }

    public void setV20350_11(Integer v20350_11) {
        V20350_11 = v20350_11;
    }

    public Integer getV20350_12() {
        return V20350_12;
    }

    public void setV20350_12(Integer v20350_12) {
        V20350_12 = v20350_12;
    }

    public Integer getV20350_13() {
        return V20350_13;
    }

    public void setV20350_13(Integer v20350_13) {
        V20350_13 = v20350_13;
    }

    public Integer getV20003() {
        return V20003;
    }

    public void setV20003(Integer v20003) {
        V20003 = v20003;
    }

    public Integer getV04080_05() {
        return V04080_05;
    }

    public void setV04080_05(Integer v04080_05) {
        V04080_05 = v04080_05;
    }

    public Integer getV20004() {
        return V20004;
    }

    public void setV20004(Integer v20004) {
        V20004 = v20004;
    }

    public Integer getV20005() {
        return V20005;
    }

    public void setV20005(Integer v20005) {
        V20005 = v20005;
    }

    public Integer getV20062() {
        return V20062;
    }

    public void setV20062(Integer v20062) {
        V20062 = v20062;
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

    public BigDecimal getV14320() {
        return v14320;
    }

    public void setV14320(BigDecimal v14320) {
        this.v14320 = v14320;
    }

    public BigDecimal getV14308() {
        return v14308;
    }

    public void setV14308(BigDecimal v14308) {
        this.v14308 = v14308;
    }

    public Integer getQ10004() {
        return Q10004;
    }

    public void setQ10004(Integer q10004) {
        Q10004 = q10004;
    }

    public Integer getQ10051() {
        return Q10051;
    }

    public void setQ10051(Integer q10051) {
        Q10051 = q10051;
    }

    public Integer getQ10061() {
        return Q10061;
    }

    public void setQ10061(Integer q10061) {
        Q10061 = q10061;
    }

    public Integer getQ10062() {
        return Q10062;
    }

    public void setQ10062(Integer q10062) {
        Q10062 = q10062;
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

    public Integer getQ12001() {
        return Q12001;
    }

    public void setQ12001(Integer q12001) {
        Q12001 = q12001;
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

    public Integer getQ12405() {
        return Q12405;
    }

    public void setQ12405(Integer q12405) {
        Q12405 = q12405;
    }

    public Integer getQ12016() {
        return Q12016;
    }

    public void setQ12016(Integer q12016) {
        Q12016 = q12016;
    }

    public Integer getQ12017() {
        return Q12017;
    }

    public void setQ12017(Integer q12017) {
        Q12017 = q12017;
    }

    public Integer getQ12003() {
        return Q12003;
    }

    public void setQ12003(Integer q12003) {
        Q12003 = q12003;
    }

    public Integer getQ13003() {
        return Q13003;
    }

    public void setQ13003(Integer q13003) {
        Q13003 = q13003;
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

    public Integer getQ13004() {
        return Q13004;
    }

    public void setQ13004(Integer q13004) {
        Q13004 = q13004;
    }

    public Integer getQ13019() {
        return Q13019;
    }

    public void setQ13019(Integer q13019) {
        Q13019 = q13019;
    }

    public Integer getQ13020() {
        return Q13020;
    }

    public void setQ13020(Integer q13020) {
        Q13020 = q13020;
    }

    public Integer getQ13021() {
        return Q13021;
    }

    public void setQ13021(Integer q13021) {
        Q13021 = q13021;
    }

    public Integer getQ13022() {
        return Q13022;
    }

    public void setQ13022(Integer q13022) {
        Q13022 = q13022;
    }

    public Integer getQ13023() {
        return Q13023;
    }

    public void setQ13023(Integer q13023) {
        Q13023 = q13023;
    }

    public Integer getQ04080_04() {
        return Q04080_04;
    }

    public void setQ04080_04(Integer q04080_04) {
        Q04080_04 = q04080_04;
    }

    public Integer getQ13011() {
        return Q13011;
    }

    public void setQ13011(Integer q13011) {
        Q13011 = q13011;
    }

    public Integer getQ13033() {
        return Q13033;
    }

    public void setQ13033(Integer q13033) {
        Q13033 = q13033;
    }

    public Integer getQ11290() {
        return Q11290;
    }

    public void setQ11290(Integer q11290) {
        Q11290 = q11290;
    }

    public Integer getQ11291() {
        return Q11291;
    }

    public void setQ11291(Integer q11291) {
        Q11291 = q11291;
    }

    public Integer getQ11292() {
        return Q11292;
    }

    public void setQ11292(Integer q11292) {
        Q11292 = q11292;
    }

    public Integer getQ11293() {
        return Q11293;
    }

    public void setQ11293(Integer q11293) {
        Q11293 = q11293;
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

    public Integer getQ11201() {
        return Q11201;
    }

    public void setQ11201(Integer q11201) {
        Q11201 = q11201;
    }

    public Integer getQ11202() {
        return Q11202;
    }

    public void setQ11202(Integer q11202) {
        Q11202 = q11202;
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

    public Integer getQ11503_06() {
        return Q11503_06;
    }

    public void setQ11503_06(Integer q11503_06) {
        Q11503_06 = q11503_06;
    }

    public Integer getQ11504_06() {
        return Q11504_06;
    }

    public void setQ11504_06(Integer q11504_06) {
        Q11504_06 = q11504_06;
    }

    public Integer getQ11503_12() {
        return Q11503_12;
    }

    public void setQ11503_12(Integer q11503_12) {
        Q11503_12 = q11503_12;
    }

    public Integer getQ11504_12() {
        return Q11504_12;
    }

    public void setQ11504_12(Integer q11504_12) {
        Q11504_12 = q11504_12;
    }

    public Integer getQ12120() {
        return Q12120;
    }

    public void setQ12120(Integer q12120) {
        Q12120 = q12120;
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

    public Integer getQ12013() {
        return Q12013;
    }

    public void setQ12013(Integer q12013) {
        Q12013 = q12013;
    }

    public Integer getQ12030_005() {
        return Q12030_005;
    }

    public void setQ12030_005(Integer q12030_005) {
        Q12030_005 = q12030_005;
    }

    public Integer getQ12030_010() {
        return Q12030_010;
    }

    public void setQ12030_010(Integer q12030_010) {
        Q12030_010 = q12030_010;
    }

    public Integer getQ12030_015() {
        return Q12030_015;
    }

    public void setQ12030_015(Integer q12030_015) {
        Q12030_015 = q12030_015;
    }

    public Integer getQ12030_020() {
        return Q12030_020;
    }

    public void setQ12030_020(Integer q12030_020) {
        Q12030_020 = q12030_020;
    }

    public Integer getQ12030_040() {
        return Q12030_040;
    }

    public void setQ12030_040(Integer q12030_040) {
        Q12030_040 = q12030_040;
    }

    public Integer getQ12030_080() {
        return Q12030_080;
    }

    public void setQ12030_080(Integer q12030_080) {
        Q12030_080 = q12030_080;
    }

    public Integer getQ12030_160() {
        return Q12030_160;
    }

    public void setQ12030_160(Integer q12030_160) {
        Q12030_160 = q12030_160;
    }

    public Integer getQ12030_320() {
        return Q12030_320;
    }

    public void setQ12030_320(Integer q12030_320) {
        Q12030_320 = q12030_320;
    }

    public Integer getQ12314() {
        return Q12314;
    }

    public void setQ12314(Integer q12314) {
        Q12314 = q12314;
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

    public Integer getQ20001_701_01() {
        return Q20001_701_01;
    }

    public void setQ20001_701_01(Integer q20001_701_01) {
        Q20001_701_01 = q20001_701_01;
    }

    public Integer getQ20001_701_10() {
        return Q20001_701_10;
    }

    public void setQ20001_701_10(Integer q20001_701_10) {
        Q20001_701_10 = q20001_701_10;
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

    public Integer getQ20001() {
        return Q20001;
    }

    public void setQ20001(Integer q20001) {
        Q20001 = q20001;
    }

    public Integer getQ20010() {
        return Q20010;
    }

    public void setQ20010(Integer q20010) {
        Q20010 = q20010;
    }

    public Integer getQ20051() {
        return Q20051;
    }

    public void setQ20051(Integer q20051) {
        Q20051 = q20051;
    }

    public Integer getQ20011() {
        return Q20011;
    }

    public void setQ20011(Integer q20011) {
        Q20011 = q20011;
    }

    public Integer getQ20013() {
        return Q20013;
    }

    public void setQ20013(Integer q20013) {
        Q20013 = q20013;
    }

    public Integer getQ20350_01() {
        return Q20350_01;
    }

    public void setQ20350_01(Integer q20350_01) {
        Q20350_01 = q20350_01;
    }

    public Integer getQ20350_02() {
        return Q20350_02;
    }

    public void setQ20350_02(Integer q20350_02) {
        Q20350_02 = q20350_02;
    }

    public Integer getQ20350_03() {
        return Q20350_03;
    }

    public void setQ20350_03(Integer q20350_03) {
        Q20350_03 = q20350_03;
    }

    public Integer getQ20350_04() {
        return Q20350_04;
    }

    public void setQ20350_04(Integer q20350_04) {
        Q20350_04 = q20350_04;
    }

    public Integer getQ20350_05() {
        return Q20350_05;
    }

    public void setQ20350_05(Integer q20350_05) {
        Q20350_05 = q20350_05;
    }

    public Integer getQ20350_06() {
        return Q20350_06;
    }

    public void setQ20350_06(Integer q20350_06) {
        Q20350_06 = q20350_06;
    }

    public Integer getQ20350_07() {
        return Q20350_07;
    }

    public void setQ20350_07(Integer q20350_07) {
        Q20350_07 = q20350_07;
    }

    public Integer getQ20350_08() {
        return Q20350_08;
    }

    public void setQ20350_08(Integer q20350_08) {
        Q20350_08 = q20350_08;
    }

    public Integer getQ20350_11() {
        return Q20350_11;
    }

    public void setQ20350_11(Integer q20350_11) {
        Q20350_11 = q20350_11;
    }

    public Integer getQ20350_12() {
        return Q20350_12;
    }

    public void setQ20350_12(Integer q20350_12) {
        Q20350_12 = q20350_12;
    }

    public Integer getQ20350_13() {
        return Q20350_13;
    }

    public void setQ20350_13(Integer q20350_13) {
        Q20350_13 = q20350_13;
    }

    public Integer getQ20003() {
        return Q20003;
    }

    public void setQ20003(Integer q20003) {
        Q20003 = q20003;
    }

    public Integer getQ04080_05() {
        return Q04080_05;
    }

    public void setQ04080_05(Integer q04080_05) {
        Q04080_05 = q04080_05;
    }

    public Integer getQ20004() {
        return Q20004;
    }

    public void setQ20004(Integer q20004) {
        Q20004 = q20004;
    }

    public Integer getQ20005() {
        return Q20005;
    }

    public void setQ20005(Integer q20005) {
        Q20005 = q20005;
    }

    public Integer getQ20062() {
        return Q20062;
    }

    public void setQ20062(Integer q20062) {
        Q20062 = q20062;
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
}
