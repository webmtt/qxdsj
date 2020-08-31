package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Aryer {

    private String dRecordId;//记录标识

    private String cname;//站名

    private Integer v01300;//区站号(数字)

    private String v04002;//资料观测月

    private BigDecimal v13011Year;//年降雨量(mm)

    private Integer totalV13011Num;//总降水次数

    private BigDecimal avgYearV15532;//pH年均值

    private BigDecimal maxYearV15532;//年pH最大值

    private BigDecimal minYearV15532;//年pH最小值

    private BigDecimal v15532Rate45;//pH<4.5频率

    private BigDecimal v15532Rate56;//pH<5.6频率

    private BigDecimal v15532Rain;//pH观测雨量

    private Integer v15532Num;//pH观测次数

    private Integer v22381Num;//电导率观测次数

    private BigDecimal avgYearV22381;//年平均电导率

    private BigDecimal maxYearV22381;//年最大电导率(微西门子/厘米) 

    private BigDecimal minYearV22381;//年最小电导率(微西门子/厘米) 
}
