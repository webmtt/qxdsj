package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Arday {

    private String dRecordId;//记录标识

    private String cname;//站名

    private Integer v01300;//区站号(数字)

    private Short v04002;//资料观测月

    private String v04307;//降水开始时间

    private String v04308;//降水结束时间

    private BigDecimal v15532;//pH值

    private BigDecimal v22381;//电导率(微西门子/厘米)

    private BigDecimal waterV12001;//水样温度

    private BigDecimal v13011;//降水量

    private Integer remarks;//备注
}
