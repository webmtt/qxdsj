package cma.cimiss2.dpc.decoder.bean.agme;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Soilchn {

    private String dRecordId;//记录标识

    private Timestamp dIymdhm;//入库时间

    private String cname;//站名

    private String countryname;//国家名称

    private String provincename;//省名

    private String cityname;//地市名

    private String cntyname;//区县名

    private String townname;//乡镇名

    private BigDecimal q71110;//田间持水量质控码

    private BigDecimal q71655010;//10cm重量含水率质控码

    private BigDecimal q71655020;//20cm重量含水率质控码

    private BigDecimal q71655030;//30cm重量含水率质控码

    private BigDecimal q71655040;//40cm重量含水率质控码

    private Timestamp dRymdhm;//收到时间

    private BigDecimal q71655060;//60cm重量含水率质控码

    private Timestamp dUpdateTime;//更新时间

    private BigDecimal q71655080;//80cm重量含水率质控码

    private Timestamp dDatetime;//资料时间

    private String vFileNameSource;//原文件名

    private BigDecimal q71655100;//100cm重量含水率质控码

    private String qr71110;//田间持水量质控过程码

    private String v01301;//区站号/观测平台标识(字符)

    private String qr71655010;//10cm重量含水率质控过程码

    private BigDecimal v01300;//区站号/观测平台标识(数字)

    private String qr71655020;//20cm重量含水率质控过程码

    private BigDecimal v05001;//纬度

    private BigDecimal v06001;//经度

    private String qr71655030;//30cm重量含水率质控过程码

    private String qr71655040;//40cm重量含水率质控过程码

    private BigDecimal v07001;//测站高度

    private BigDecimal v07031;//气压传感器海拔高度

    private String qr71655060;//60cm重量含水率质控过程码

    private String qr71655080;//80cm重量含水率质控过程码

    private BigDecimal v02001;//测站类型

    private String qr71655100;//100cm重量含水率质控过程码

    private BigDecimal v02301;//测站级别

    private String vAcode;//中国行政区划代码

    private Short v04001;//年

    private Short v04002;//月

    private Short v04003;//日

    private Short v04004;//时

    private BigDecimal v71110;//田间持水量

    private BigDecimal v71655010;//10cm重量含水率

    private BigDecimal v71655020;//20cm重量含水率

    private BigDecimal v71655030;//30cm重量含水率

    private BigDecimal v71655040;//40cm重量含水率

    private BigDecimal v71655050;//60cm重量含水率

    private BigDecimal v71655080;//80cm重量含水率

    private BigDecimal v71655100;//100cm重量含水率

    private String vBbb;//更正报标志
}
