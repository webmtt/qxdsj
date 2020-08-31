package cma.cimiss2.dpc.decoder.fileDecode.Rfile.bean;

import lombok.Data;

/**
 * R观测参数-时值
 */
@Data
public class RViewParamsHourEntity {

    private String global;//总辐射
    private String net;//净全辐射
    private String netQuality;//净全辐射控制码
    private String globalQuality;//总辐射控制码
    private String fm;//封面
    private String apparatusType;//仪器类型
    private String logger;//记录器
    private String netChart;//净辐射表类型
    private String globalChart;//总辐射表类型
    private String site;//场地周围环境变化
    private String projective;//直射辐射
    private String projectiveQuilts;//直射辐射
    private String reflect;//反射辐射
    private String reflectQuality;//反射辐射
    private String scatter;//散射辐射
    private String scatterQuality;//散射辐射
    private String projectiveType;//直射表类型
    private String reflectType;//反射表类型
    private String scatterType;//散射表类型
    private String beiZhu;//备注
    private String sunProjectivelyHour;//9、12、15时太阳直接辐射辐照度
    private String sunProjectivelyHourQualify;//9、12、15时太阳直接辐射辐照度质控码
    private String gasTurbidity;// 9、12、15时大气浑浊度指标
    private String gasTurbidityQuality;// 9、12、15时大气浑浊度指标质控码
    private String time; // 数据时间-时

}
