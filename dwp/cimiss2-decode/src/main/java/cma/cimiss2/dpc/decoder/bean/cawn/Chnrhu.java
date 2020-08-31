package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Chnrhu {

    private String dRecordId;//记录标识

    private String v01301;//区站号(字符)

    private BigDecimal v06001;//经度

    private BigDecimal v05001;//纬度

    private BigDecimal v07001;//测站高度

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private Integer v1300302;//02时相对湿度

    private Integer v1300308;//08时相对湿度

    private Integer v1300314;//14时相对湿度

    private Integer v1300320;//20时相对湿度

    private String q1300302;//02时相对湿度质量控制码

    private String q1300308;//08时相对湿度质量控制码

    private String q1300314;//14时相对湿度质量控制码

    private String q1300320;//20时相对湿度质量控制码
}
