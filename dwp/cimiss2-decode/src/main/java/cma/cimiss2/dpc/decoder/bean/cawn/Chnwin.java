package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Chnwin {

    private String dRecordId;//记录标识

    private String v01301;//区站号(字符)

    private BigDecimal v06001;//经度

    private BigDecimal v05001;//纬度

    private BigDecimal v07001;//测站高度

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private BigDecimal v1100102;//02时风向

    private BigDecimal v1100202;//02时风速

    private BigDecimal v1100108;//08时风向

    private BigDecimal v1100208;//08时风速

    private BigDecimal v1100114;//14时风向

    private BigDecimal v1100214;//14时风速

    private BigDecimal v1100120;//20时风向

    private BigDecimal v1100220;//20时风速

    private BigDecimal q1100102;//02时风向质量控制码

    private BigDecimal q1100202;//02时风速质量控制码

    private BigDecimal q1100108;//08时风向质量控制码

    private BigDecimal q1100208;//08时风速质量控制码

    private BigDecimal q1100114;//14时风向质量控制码

    private BigDecimal q1100214;//14时风速质量控制码

    private BigDecimal q1100120;//20时风向质量控制码

    private BigDecimal q1100220;//20时风速质量控制码
}
