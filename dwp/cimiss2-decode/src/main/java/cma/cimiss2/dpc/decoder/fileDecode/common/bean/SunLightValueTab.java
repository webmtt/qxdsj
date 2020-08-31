package cma.cimiss2.dpc.decoder.fileDecode.common.bean;

import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 *  cimiss地面日照值表信息
 * @author yang.kq
 * @version 1.0
 * @date 2020/3/19 11:12
 */
@Data
public class SunLightValueTab implements Serializable {
    private static final long serialVersionUID = 1L;
    private String D_RECORD_ID;// 记录标识 ( 系统自动生成的流水号 )   
    private String D_DATA_ID;// 资料标识 ( 资料的4级编码 )   
    private String D_IYMDHM;// 入库时间 ( 插表时的系统时间 )
    private String D_RYMDHM;// 收到时间 ( DPC消息生成时间 )
    private String D_UPDATE_TIME;// 更新时间 ( 根据CCx对记录更新时的系统时间 )
    private String D_DATETIME;// 资料时间 ( 由V04001、04002、V04003组成 )
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
}
