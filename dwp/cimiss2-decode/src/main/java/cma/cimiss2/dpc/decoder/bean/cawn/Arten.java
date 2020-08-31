package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Arten {

    private String dRecordId;//记录标识

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private BigDecimal v13011;//降水量

    private BigDecimal v15532;//pH值

    private BigDecimal v22381;//电导率(微西门子/厘米)

    private BigDecimal na;//钠离子

    private BigDecimal k;//钾离子

    private BigDecimal mg;//镁离子

    private BigDecimal ca;//钙离子

    private BigDecimal cl;//氯离子

    private BigDecimal nh4;//铵离子

    private BigDecimal no3;//硝酸根离子

    private BigDecimal so4;//硫酸根离子

    private BigDecimal f;//氟离子
}
