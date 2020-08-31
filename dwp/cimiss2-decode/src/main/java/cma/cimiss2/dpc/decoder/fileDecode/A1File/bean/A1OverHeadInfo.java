package cma.cimiss2.dpc.decoder.fileDecode.A1File.bean;

import lombok.Data;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/14
 */
@Data
public class A1OverHeadInfo {
    private String code;//档案号
    private String provinceName;//省
    private String stationName;//台站名称
    private String address;//地址
    private String geographicalEnvironment;//地理环境
    private String stationLeader;//台（站）长
    private String entryName;//录入人员姓名
    private String checkName;//校对人员姓名
    private String inquireName;//预审人员姓名
    private String auditName;//审核人员姓名
    private String sendName;//传输人员姓名
    private String sendTime;//传输日期
    private String fileType;//文件类型
    private String describe;//场地周围环境描述
    private String summary;//纪要信息
    private String climateProbably;//本月气候概况
    private String other;//其他
    private String note;//备注
    private String year;//年
    private String month;//月
}
