package cma.cimiss2.dpc.decoder.fileDecode.Rfile.bean;

import lombok.Data;

/**
 * 仪器信息
 */
@Data
public class RInstrumentInfo {
    private String model;//辐射表型号组
    private String number;//辐射表号码组
    private String sensitivity;//辐射表灵敏度K值,单位为0.01w-1m2
    private String resTime;//辐射表响应时间t值，单位为s
    private String resistance;//辐射表电阻R值组，单位为0.1Ω
    private String checkTime;//辐射表检定时间组
    private String startWorkTime;//开始工作时间组
    private String type;//仪器类型
}