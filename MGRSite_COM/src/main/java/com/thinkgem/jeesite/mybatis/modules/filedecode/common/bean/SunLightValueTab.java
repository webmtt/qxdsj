package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;



import java.io.Serializable;
import java.sql.Date;

/**
 *  cimiss地面日照值表信息
 * @author yang.kq
 * @version 1.0
 * @date 2020/3/19 11:12
 */

public class SunLightValueTab implements Serializable {
    private static final long serialVersionUID = 1L;
    private String D_RECORD_ID;// 记录标识 ( 系统自动生成的流水号 )   
    private String D_DATA_ID;// 资料标识 ( 资料的4级编码 )   
    private Date D_IYMDHM;// 入库时间 ( 插表时的系统时间 )   
    private Date D_RYMDHM;// 收到时间 ( DPC消息生成时间 )   
    private Date D_UPDATE_TIME;// 更新时间 ( 根据CCx对记录更新时的系统时间 )   
    private Date D_DATETIME;// 资料时间 ( 由V04001、04002、V04003组成 )   
    private String V_BBB;// 更正报标志 ( 报头行BBB )   
    private String V01301;// 区站号(字符)
    private String V01300;// 区站号(数字)
    private Float V05001;// 纬度 ( 单位：度 )   
    private Float V06001;// 经度 ( 单位：度 )   
    private Float V07001;// 测站高度 ( 单位：米 )   
    private Integer V02301;// 台站级别 ( 代码表 )   
    private Integer V_ACODE;// 中国行政区划代码 ( 代码表 )   
    private Integer V14332;// 日照时制方式 ( 代码表 )   
    private Integer V04001;// 资料观测年  
    private Integer V04002;// 资料观测月  
    private Integer V04003;// 资料观测日  
    private Float V14032_001;// 00-01时日照时数 ( 单位：小时 )   
    private Float V14032_002;// 01-02时日照时数 ( 单位：小时 )   
    private Float V14032_003;// 02-03时日照时数 ( 单位：小时 )   
    private Float V14032_004;// 03-04时日照时数 ( 单位：小时 )   
    private Float V14032_005;// 04-05时日照时数 ( 单位：小时 )   
    private Float V14032_006;// 05-06时日照时数 ( 单位：小时 )   
    private Float V14032_007;// 06-07时日照时数 ( 单位：小时 )   
    private Float V14032_008;// 07-08时日照时数 ( 单位：小时 )   
    private Float V14032_009;// 08-09时日照时数 ( 单位：小时 )   
    private Float V14032_010;// 09-10时日照时数 ( 单位：小时 )   
    private Float V14032_011;// 10-11时日照时数 ( 单位：小时 )   
    private Float V14032_012;// 11-12时日照时数 ( 单位：小时 )   
    private Float V14032_013;// 12-13时日照时数 ( 单位：小时 )   
    private Float V14032_014;// 13_14时日照时数 ( 单位：小时 )   
    private Float V14032_015;// 14-15时日照时数 ( 单位：小时 )   
    private Float V14032_016;// 15-16时日照时数 ( 单位：小时 )   
    private Float V14032_017;// 16-17时日照时数 ( 单位：小时 )   
    private Float V14032_018;// 17-18时日照时数 ( 单位：小时 )   
    private Float V14032_019;// 18-19时日照时数 ( 单位：小时 )   
    private Float V14032_020;// 19-20时日照时数 ( 单位：小时 )   
    private Float V14032_021;// 20-21时日照时数 ( 单位：小时 )   
    private Float V14032_022;// 21-22时日照时数 ( 单位：小时 )   
    private Float V14032_023;// 22-23时日照时数 ( 单位：小时 )   
    private Float V14032_024;// 23-24时日照时数 ( 单位：小时 )   
    private Float V14032;// 日日照时数 ( 单位：小时 )   
    private Integer Q14032_001;// 00-01时日照时数质控码 ( 代码表 )   
    private Integer Q14032_002;// 01-02时日照时数质控码 ( 代码表 )   
    private Integer Q14032_003;// 02-03时日照时数质控码 ( 代码表 )   
    private Integer Q14032_004;// 03-04时日照时数质控码 ( 代码表 )   
    private Integer Q14032_005;// 04-05时日照时数质控码 ( 代码表 )   
    private Integer Q14032_006;// 05-06时日照时数质控码 ( 代码表 )   
    private Integer Q14032_007;// 06-07时日照时数质控码 ( 代码表 )   
    private Integer Q14032_008;// 07-08时日照时数质控码 ( 代码表 )   
    private Integer Q14032_009;// 08-09时日照时数质控码 ( 代码表 )   
    private Integer Q14032_010;// 09-10时日照时数质控码 ( 代码表 )   
    private Integer Q14032_011;// 10-11时日照时数质控码 ( 代码表 )   
    private Integer Q14032_012;// 11-12时日照时数质控码 ( 代码表 )   
    private Integer Q14032_013;// 12-13时日照时数质控码 ( 代码表 )   
    private Integer Q14032_014;// 13_14时日照时数质控码 ( 代码表 )   
    private Integer Q14032_015;// 14-15时日照时数质控码 ( 代码表 )   
    private Integer Q14032_016;// 15-16时日照时数质控码 ( 代码表 )   
    private Integer Q14032_017;// 16-17时日照时数质控码 ( 代码表 )   
    private Integer Q14032_018;// 17-18时日照时数质控码 ( 代码表 )   
    private Integer Q14032_019;// 18-19时日照时数质控码 ( 代码表 )   
    private Integer Q14032_020;// 19-20时日照时数质控码 ( 代码表 )   
    private Integer Q14032_021;// 20-21时日照时数质控码 ( 代码表 )   
    private Integer Q14032_022;// 21-22时日照时数质控码 ( 代码表 )   
    private Integer Q14032_023;// 22-23时日照时数质控码 ( 代码表 )   
    private Integer Q14032_024;// 23-24时日照时数质控码 ( 代码表 )
    private Integer Q14032;// 日日照时数质控码 ( 代码表 )

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

    public Date getD_IYMDHM() {
        return D_IYMDHM;
    }

    public void setD_IYMDHM(Date d_IYMDHM) {
        D_IYMDHM = d_IYMDHM;
    }

    public Date getD_RYMDHM() {
        return D_RYMDHM;
    }

    public void setD_RYMDHM(Date d_RYMDHM) {
        D_RYMDHM = d_RYMDHM;
    }

    public Date getD_UPDATE_TIME() {
        return D_UPDATE_TIME;
    }

    public void setD_UPDATE_TIME(Date d_UPDATE_TIME) {
        D_UPDATE_TIME = d_UPDATE_TIME;
    }

    public Date getD_DATETIME() {
        return D_DATETIME;
    }

    public void setD_DATETIME(Date d_DATETIME) {
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

    public Integer getV14332() {
        return V14332;
    }

    public void setV14332(Integer v14332) {
        V14332 = v14332;
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

    public Float getV14032_001() {
        return V14032_001;
    }

    public void setV14032_001(Float v14032_001) {
        V14032_001 = v14032_001;
    }

    public Float getV14032_002() {
        return V14032_002;
    }

    public void setV14032_002(Float v14032_002) {
        V14032_002 = v14032_002;
    }

    public Float getV14032_003() {
        return V14032_003;
    }

    public void setV14032_003(Float v14032_003) {
        V14032_003 = v14032_003;
    }

    public Float getV14032_004() {
        return V14032_004;
    }

    public void setV14032_004(Float v14032_004) {
        V14032_004 = v14032_004;
    }

    public Float getV14032_005() {
        return V14032_005;
    }

    public void setV14032_005(Float v14032_005) {
        V14032_005 = v14032_005;
    }

    public Float getV14032_006() {
        return V14032_006;
    }

    public void setV14032_006(Float v14032_006) {
        V14032_006 = v14032_006;
    }

    public Float getV14032_007() {
        return V14032_007;
    }

    public void setV14032_007(Float v14032_007) {
        V14032_007 = v14032_007;
    }

    public Float getV14032_008() {
        return V14032_008;
    }

    public void setV14032_008(Float v14032_008) {
        V14032_008 = v14032_008;
    }

    public Float getV14032_009() {
        return V14032_009;
    }

    public void setV14032_009(Float v14032_009) {
        V14032_009 = v14032_009;
    }

    public Float getV14032_010() {
        return V14032_010;
    }

    public void setV14032_010(Float v14032_010) {
        V14032_010 = v14032_010;
    }

    public Float getV14032_011() {
        return V14032_011;
    }

    public void setV14032_011(Float v14032_011) {
        V14032_011 = v14032_011;
    }

    public Float getV14032_012() {
        return V14032_012;
    }

    public void setV14032_012(Float v14032_012) {
        V14032_012 = v14032_012;
    }

    public Float getV14032_013() {
        return V14032_013;
    }

    public void setV14032_013(Float v14032_013) {
        V14032_013 = v14032_013;
    }

    public Float getV14032_014() {
        return V14032_014;
    }

    public void setV14032_014(Float v14032_014) {
        V14032_014 = v14032_014;
    }

    public Float getV14032_015() {
        return V14032_015;
    }

    public void setV14032_015(Float v14032_015) {
        V14032_015 = v14032_015;
    }

    public Float getV14032_016() {
        return V14032_016;
    }

    public void setV14032_016(Float v14032_016) {
        V14032_016 = v14032_016;
    }

    public Float getV14032_017() {
        return V14032_017;
    }

    public void setV14032_017(Float v14032_017) {
        V14032_017 = v14032_017;
    }

    public Float getV14032_018() {
        return V14032_018;
    }

    public void setV14032_018(Float v14032_018) {
        V14032_018 = v14032_018;
    }

    public Float getV14032_019() {
        return V14032_019;
    }

    public void setV14032_019(Float v14032_019) {
        V14032_019 = v14032_019;
    }

    public Float getV14032_020() {
        return V14032_020;
    }

    public void setV14032_020(Float v14032_020) {
        V14032_020 = v14032_020;
    }

    public Float getV14032_021() {
        return V14032_021;
    }

    public void setV14032_021(Float v14032_021) {
        V14032_021 = v14032_021;
    }

    public Float getV14032_022() {
        return V14032_022;
    }

    public void setV14032_022(Float v14032_022) {
        V14032_022 = v14032_022;
    }

    public Float getV14032_023() {
        return V14032_023;
    }

    public void setV14032_023(Float v14032_023) {
        V14032_023 = v14032_023;
    }

    public Float getV14032_024() {
        return V14032_024;
    }

    public void setV14032_024(Float v14032_024) {
        V14032_024 = v14032_024;
    }

    public Float getV14032() {
        return V14032;
    }

    public void setV14032(Float v14032) {
        V14032 = v14032;
    }

    public Integer getQ14032_001() {
        return Q14032_001;
    }

    public void setQ14032_001(Integer q14032_001) {
        Q14032_001 = q14032_001;
    }

    public Integer getQ14032_002() {
        return Q14032_002;
    }

    public void setQ14032_002(Integer q14032_002) {
        Q14032_002 = q14032_002;
    }

    public Integer getQ14032_003() {
        return Q14032_003;
    }

    public void setQ14032_003(Integer q14032_003) {
        Q14032_003 = q14032_003;
    }

    public Integer getQ14032_004() {
        return Q14032_004;
    }

    public void setQ14032_004(Integer q14032_004) {
        Q14032_004 = q14032_004;
    }

    public Integer getQ14032_005() {
        return Q14032_005;
    }

    public void setQ14032_005(Integer q14032_005) {
        Q14032_005 = q14032_005;
    }

    public Integer getQ14032_006() {
        return Q14032_006;
    }

    public void setQ14032_006(Integer q14032_006) {
        Q14032_006 = q14032_006;
    }

    public Integer getQ14032_007() {
        return Q14032_007;
    }

    public void setQ14032_007(Integer q14032_007) {
        Q14032_007 = q14032_007;
    }

    public Integer getQ14032_008() {
        return Q14032_008;
    }

    public void setQ14032_008(Integer q14032_008) {
        Q14032_008 = q14032_008;
    }

    public Integer getQ14032_009() {
        return Q14032_009;
    }

    public void setQ14032_009(Integer q14032_009) {
        Q14032_009 = q14032_009;
    }

    public Integer getQ14032_010() {
        return Q14032_010;
    }

    public void setQ14032_010(Integer q14032_010) {
        Q14032_010 = q14032_010;
    }

    public Integer getQ14032_011() {
        return Q14032_011;
    }

    public void setQ14032_011(Integer q14032_011) {
        Q14032_011 = q14032_011;
    }

    public Integer getQ14032_012() {
        return Q14032_012;
    }

    public void setQ14032_012(Integer q14032_012) {
        Q14032_012 = q14032_012;
    }

    public Integer getQ14032_013() {
        return Q14032_013;
    }

    public void setQ14032_013(Integer q14032_013) {
        Q14032_013 = q14032_013;
    }

    public Integer getQ14032_014() {
        return Q14032_014;
    }

    public void setQ14032_014(Integer q14032_014) {
        Q14032_014 = q14032_014;
    }

    public Integer getQ14032_015() {
        return Q14032_015;
    }

    public void setQ14032_015(Integer q14032_015) {
        Q14032_015 = q14032_015;
    }

    public Integer getQ14032_016() {
        return Q14032_016;
    }

    public void setQ14032_016(Integer q14032_016) {
        Q14032_016 = q14032_016;
    }

    public Integer getQ14032_017() {
        return Q14032_017;
    }

    public void setQ14032_017(Integer q14032_017) {
        Q14032_017 = q14032_017;
    }

    public Integer getQ14032_018() {
        return Q14032_018;
    }

    public void setQ14032_018(Integer q14032_018) {
        Q14032_018 = q14032_018;
    }

    public Integer getQ14032_019() {
        return Q14032_019;
    }

    public void setQ14032_019(Integer q14032_019) {
        Q14032_019 = q14032_019;
    }

    public Integer getQ14032_020() {
        return Q14032_020;
    }

    public void setQ14032_020(Integer q14032_020) {
        Q14032_020 = q14032_020;
    }

    public Integer getQ14032_021() {
        return Q14032_021;
    }

    public void setQ14032_021(Integer q14032_021) {
        Q14032_021 = q14032_021;
    }

    public Integer getQ14032_022() {
        return Q14032_022;
    }

    public void setQ14032_022(Integer q14032_022) {
        Q14032_022 = q14032_022;
    }

    public Integer getQ14032_023() {
        return Q14032_023;
    }

    public void setQ14032_023(Integer q14032_023) {
        Q14032_023 = q14032_023;
    }

    public Integer getQ14032_024() {
        return Q14032_024;
    }

    public void setQ14032_024(Integer q14032_024) {
        Q14032_024 = q14032_024;
    }

    public Integer getQ14032() {
        return Q14032;
    }

    public void setQ14032(Integer q14032) {
        Q14032 = q14032;
    }
}
