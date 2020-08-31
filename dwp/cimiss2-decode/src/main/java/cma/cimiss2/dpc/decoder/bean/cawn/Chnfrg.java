package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Chnfrg {

    private String dRecordId;//记录标识

    private String v01301;//区站号(字符)

    private BigDecimal v06001;//经度

    private BigDecimal v05001;//纬度

    private BigDecimal v07001;//测站高度

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private Integer v20302010;//轻雾

    private Integer v20302042;//雾

    private Short q20302010;//轻雾质控码

    private Short q20302042;//雾质控码

}
