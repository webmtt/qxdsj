package cma.cimiss2.dpc.decoder.bean.surf;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Chnmrbc {

    private String dRecordId;//记录主键

    private String dDataId;//资料标识

    private Timestamp dIymdhm;//入库时间

    private Timestamp dDatetime;//资料时间

    private Integer v04001;//资料观测年

    private Integer v04002;//资料观测月

    private Integer v04003;//资料观测日

    private Integer v04004;//资料观测时

    private Integer v04005;//资料观测分

    private String v01301;//区站号(字符)

    private Integer v01300;//区站号(数字)

    private Double v05001;//纬度

    private Double v06001;//经度

    private Double v07001;//测站高度（观测场海拔高度）

    private Integer v02001;//省名

    private Integer v02301;//台站名称

    private String vAcode;//中国行政区划代码

    private Double v07031;//气压传感器海拔高度

    private Double v0703204;//风速传感器距地面高度

    private String address;//地址

    private String archivesnum;//档案号

    private String geoenvironment;//地理环境

    private String stationname;//台（站）长

    private String inputmes;//输入

    private String proofreading;//校对

    private String pretrial;//预审

    private String transmission;//传输

    private Timestamp transmissiontime;//传输时间

    private String noteid;//备注ID

    private String noteGeneralEventid;//备注项一般事件标识

    private String noteMonth;//备注项一般事件起始时间

    private String noteGeneralEvent;//备注项一般事件

    private String eventTypeIdentification;//事件类型标识码

    private String climatcid;//天气气候概况标识

    private String climateCharacterisitics;//气候特点

    private String majorDisaster;//主要天气过程

    private String weatherProcess;//关键性天气

    private String longAdverseWeather;//较长时间不利天气

    private String overallMerit;//综合评价

    private String summatyMonth;//纪要时间

    private String summaryEventid;//纪要编号

    private String summaryEventStopTime;//纪要结束时间

    private String summaryEvent;//纪要内容

    private String eventTypeIdentification1;//纪要事件类型标识
}
