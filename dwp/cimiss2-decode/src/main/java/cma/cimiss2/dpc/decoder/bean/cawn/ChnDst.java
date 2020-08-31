package cma.cimiss2.dpc.decoder.bean.cawn;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ChnDst {

    private String dRecordId;//记录标识

    private Integer v01300;//区站号(数字)

    private BigDecimal v06001;//经度

    private BigDecimal v05001;//纬度

    private BigDecimal v07001;//测站高度

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private Integer v20302031;//沙尘暴

    private Integer v20302007;//扬沙

    private Integer v20302006;//浮尘

    private Short q20302031;//沙尘暴质控码

    private Short q20302007;//扬沙质控码

    private Short q20302006;//浮尘质控码

    private BigDecimal v05002;//02时能见度

    private BigDecimal v06008;//08时能见度

    private BigDecimal v07014;//14时能见度

    private Integer v02001;//20时能见度

    private Short q05001;//02时能见度质量控制码

    private Short q06001;//08时能见度质量控制码

    private Short q07001;//14时能见度质量控制码

    private Short q02001;//20时能见度质量控制码

    private BigDecimal v11042;//日最大风速

    private BigDecimal v11296;//日最大风速的风向

    private BigDecimal v11046;//日极大风速

    private BigDecimal v11211;//日极大风速的风向

    private Short q11042;//日最大风速质控码

    private Short q11296;//日最大风速的风向质控码

    private Short q11046;//日极大风速质控码

    private Short q11211;//日极大风速的风向质控码
}
