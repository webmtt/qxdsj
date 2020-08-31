package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Armon {

    private String dRecordId;//记录标识

    private String cname;//站名

    private Integer v01300;//区站号(数字)

    private Short v04002;//资料观测月

    private BigDecimal totalV13011;//总降水量

    private BigDecimal totalV13011Num;//总降水次数

    private BigDecimal avgMonthhV15532;//月平均pH值

    private BigDecimal maxV15532;//pH最大值

    private BigDecimal minV15532;//pH最小值

    private BigDecimal v15532Num45;//pH<4.5频数

    private BigDecimal v15532Rate45;//pH<4.5频率

    private BigDecimal v15532Num56;//pH<5.6频数

    private BigDecimal v15532Rate56;//pH<5.6频率

    private BigDecimal v15532Num70;//5.6<pH<7.0 

    private BigDecimal v15532Rate10;//5.6<pH<7.0 

    private BigDecimal v15532NumM70;//pH>7.0频数

    private BigDecimal v15532RateM70;//pH>7.0频率

    private BigDecimal v15532ForecatsRate;//pH测报率

    private BigDecimal avgMonthV22381;//月平均电导率(微西门子/厘米)

    private BigDecimal maxV22381;//最大电导率(微西门子/厘米)

    private BigDecimal minV22381;//最小电导率(微西门子/厘米)

    private BigDecimal v22381Rate;//电导率测报率

    private BigDecimal v15532Num40;//pH<4.0频数

    private BigDecimal v15532Rate40;//pH<4.0频率

    private BigDecimal v15532Rain;//pH观测雨量

    private BigDecimal v22381Rain;//电导率观测雨量

    private Integer v15532Num;//pH观测次数

    private Integer v22381Num;//电导率观测次数
}
