package cma.cimiss2.dpc.decoder.fileDecode.Rfile.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * R文件台站参数
 * @author yangkq
 * @version 1.0
 * @date 2020/3/11
 */
@Data
public class RFirstData implements Serializable {
    private static final long serialVersionUID = 1L;
    private String areacode; // 区站号
    private String jd; // 经度
    private String wd; // 纬度
    private String viewhight; // 观测场拔海高度
    private String depoyrank; // 测站级别
    private String qualitycode; // 质量控制指示码
    private String year;
    private String month;
}
