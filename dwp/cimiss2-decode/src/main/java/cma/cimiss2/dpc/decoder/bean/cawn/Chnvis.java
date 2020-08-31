package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Chnvis {

    private String dRecordId;//记录标识

    private String v01301;//区站号(字符)

    private BigDecimal v06001;//经度

    private BigDecimal v05001;//纬度

    private BigDecimal v07001;//测站高度

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private Integer visibitlity02;//02时能见度

    private Integer visibitlity08;//08时能见度

    private Integer visibitlity14;//14时能见度

    private Integer visibitlity20;//20时能见度

    private Integer qVisibitlity02;//02时能见度质量控制码

    private Integer qVisibitlity08;//08时能见度质量控制码

    private String qVisibitlity14;//14时能见度质量控制码

    private String qVisibitlity20;//20时能见度质量控制码
}
