package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class EnvShare {

    private String dRecordId;//记录标识

    private String cityname;//城市名称

    private String citycode;//城市代码

    private String stationname;//点位名称

    private String v01301;//区站号(字符)

    private String stationattribute;//点位属性

    private BigDecimal v06001;//经度

    private BigDecimal v05001;//纬度

    private String vAcode;//中国行政区划代码

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private Short v04004;//资料观测时

    private String so2;//二氧化硫

    private Integer no;//一氧化氮

    private BigDecimal no2;//二氧化氮

    private BigDecimal nox;//氮氧化合物

    private BigDecimal co;//钴

    private Integer o3;//CO

    private Integer pm10;//O3

    private String pm25;//PM10

    private BigDecimal v11202;//瞬时风速

    private BigDecimal v11201;//瞬时风向

    private BigDecimal v12001;//气温

    private BigDecimal v10004;//本站气压

    private BigDecimal v13003;//相对湿度

    private BigDecimal v13019;//1小时降水量

    private String remark;//备注

    private String dDataId;//资料标识

    private Timestamp dDatetime;//资料时间
}
