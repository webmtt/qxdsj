package cma.cimiss2.dpc.decoder.fileDecode.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/4/9
 */
@Data
public class FirstData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String areacode; // 区站号
    private String jd; // 经度
    private String wd; // 纬度
    private String viewhight; // 观测场拔海高度
    private String quickhight; // 水银槽拔海高度
    private String ganshight; // 气压感应器拔海高度
    private String observehight; // 观测平台距地高度
    private String obversetype; // 观测方式和测站类别
    private String obsercecode; // 观测项目标识
    private String qualitycode; // 质量控制指示码
    private String arcaninehight; // 风速器距地高度
    private String year;//年
    private String month;//月

}
