package cma.cimiss2.dpc.decoder.fileDecode.Rfile.bean;

import lombok.Data;

/**
 * R文件日结果值
 */
@Data
public class RViewParamsDayEntity {
    private String activeState;//作用层状态
    private String activeQuality;//作用层状态质控码
    private String global;//日总辐射曝辐量
    private String globalQuality;//日总辐射曝辐量质控码
    private String maxGlobal;// 日最大总辐射辐照度
    private String maxGlobalQuality;// 日最大总辐射辐照度质控码
    private String maxGlobalTime;// 日最大总辐射辐照度出现时间
    private String maxGlobalTimeQuality;// 日最大总辐射辐照度出现时间质控码
    private String sunHours;// 日照时数
    private String sunHoursQuality;// 日照时数质控码
    private String net;//日净全辐射曝辐量
    private String netQuality;//日净全辐射曝辐量质控码
    private String maxNet;// 日最大净全辐射辐照度
    private String maxNetQuality;// 日最大净全辐射辐照度质控码
    private String maxNetTime;// 日最大净全辐射辐照度出现时间
    private String maxNetTimeQuality;// 日最大净全辐射辐照度出现时间质控码
    private String minNet;// 日最小净全辐射辐照度
    private String minNetQuality;// 日最小净全辐射辐照度质控码
    private String minNetTime;// 日最小净全辐射辐照度出现时间
    private String minNetTimeQuality;// 日最小净全辐射辐照度出现时间质控码
    private String projective;//日直接辐射曝辐量
    private String projectivelyQualify;//日直接辐射曝辐量质控码
    private String maxProjectively;//日最大直接辐射辐照度
    private String maxProjectivelyQualify;//日最大直接辐射辐照度质控码
    private String maxProjectivelyTime;//日最大直接辐射辐照度出现时间
    private String maxProjectiveTimeQuilts;//日最大直接辐射辐照度出现时间质控码
    private String platProjective;//水平面直接辐射
    private String platProjectivelyQualify;//水平面直接辐射质控码
    private String reflect;//日反射辐射曝辐量
    private String reflectQuality;//日反射辐射曝辐量质控码
    private String reflectPercent;//日反射比
    private String reflectPercentQuality;//日反射比质控码
    private String maxReflect;//日最大反射辐射辐照度
    private String maxReflectQuality;//日最大反射辐射辐照度质控码
    private String maxReflectTime;//日最大反射辐射辐照度出现时间
    private String maxReflectTimeQuality;//日最大反射辐射辐照度出现时间质控码
    private String scatter;//日散射辐射曝辐量
    private String scatterQuality;//日散射辐射曝辐量质控码
    private String maxScatter;//日最大散射辐射辐照度
    private String maxScatterQuality;//日最大散射辐射辐照度质控码
    private String maxScatterTime;//日最大散射辐射辐照度出现时间
    private String maxScatterTimeQuality;//日最大散射辐射辐照度出现时间质控码
    private String time; // 年-月-日
}
