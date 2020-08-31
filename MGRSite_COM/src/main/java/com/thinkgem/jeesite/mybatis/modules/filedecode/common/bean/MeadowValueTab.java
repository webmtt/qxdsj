package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;


import java.math.BigDecimal;

/**
 * 旬值数据实体
 * @author yangkq
 * @version 1.0
 * @date 2020/4/15
 */

public class MeadowValueTab {
    private String D_RETAIN_ID;//记录标识 （ 系统自动生成的流水号 ）
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

    public String getV04290() {
        return V04290;
    }

    public void setV04290(String v04290) {
        V04290 = v04290;
    }

    public Float getV10004_701() {
        return V10004_701;
    }

    public void setV10004_701(Float v10004_701) {
        V10004_701 = v10004_701;
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

    public String getV10301_060() {
        return V10301_060;
    }

    public void setV10301_060(String v10301_060) {
        V10301_060 = v10301_060;
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

    public String getV10302_060() {
        return V10302_060;
    }

    public void setV10302_060(String v10302_060) {
        V10302_060 = v10302_060;
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

    public Float getV12011_avg() {
        return V12011_avg;
    }

    public void setV12011_avg(Float v12011_avg) {
        V12011_avg = v12011_avg;
    }

    public Float getV12012_avg() {
        return V12012_avg;
    }

    public void setV12012_avg(Float v12012_avg) {
        V12012_avg = v12012_avg;
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

    public Float getV13004_701() {
        return V13004_701;
    }

    public void setV13004_701(Float v13004_701) {
        V13004_701 = v13004_701;
    }

    public Integer getV13003_701() {
        return V13003_701;
    }

    public void setV13003_701(Integer v13003_701) {
        V13003_701 = v13003_701;
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

    public Integer getV13353() {
        return V13353;
    }

    public void setV13353(Integer v13353) {
        V13353 = v13353;
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

    public Float getV12311_avg() {
        return V12311_avg;
    }

    public void setV12311_avg(Float v12311_avg) {
        V12311_avg = v12311_avg;
    }

    public Float getV12121_avg() {
        return V12121_avg;
    }

    public void setV12121_avg(Float v12121_avg) {
        V12121_avg = v12121_avg;
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

    public Float getV12315_avg() {
        return V12315_avg;
    }

    public void setV12315_avg(Float v12315_avg) {
        V12315_avg = v12315_avg;
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

    public Float getV12316_avg() {
        return V12316_avg;
    }

    public void setV12316_avg(Float v12316_avg) {
        V12316_avg = v12316_avg;
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

    public Float getV11291_701() {
        return V11291_701;
    }

    public void setV11291_701(Float v11291_701) {
        V11291_701 = v11291_701;
    }

    public Float getV11042() {
        return V11042;
    }

    public void setV11042(Float v11042) {
        V11042 = v11042;
    }

    public String getV11296() {
        return V11296;
    }

    public void setV11296(String v11296) {
        V11296 = v11296;
    }

    public Integer getV11042_040() {
        return V11042_040;
    }

    public void setV11042_040(Integer v11042_040) {
        V11042_040 = v11042_040;
    }

    public String getV11042_060() {
        return V11042_060;
    }

    public void setV11042_060(String v11042_060) {
        V11042_060 = v11042_060;
    }

    public Float getV11046() {
        return V11046;
    }

    public void setV11046(Float v11046) {
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

    public String getV11211_060() {
        return V11211_060;
    }

    public void setV11211_060(String v11211_060) {
        V11211_060 = v11211_060;
    }

    public Float getV11293_701() {
        return V11293_701;
    }

    public void setV11293_701(Float v11293_701) {
        V11293_701 = v11293_701;
    }

    public Integer getV04330_015() {
        return V04330_015;
    }

    public void setV04330_015(Integer v04330_015) {
        V04330_015 = v04330_015;
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
}
