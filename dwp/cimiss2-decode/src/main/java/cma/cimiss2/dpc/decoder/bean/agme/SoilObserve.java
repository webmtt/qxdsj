package cma.cimiss2.dpc.decoder.bean.agme;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class SoilObserve {

    private String dRecordId;//记录标识

    private String dDataId;//资料标识

    private Timestamp dIymdhm;//入库时间

    private Timestamp dRymdhm;//收到时间

    private String v01301;//区站号(字符)

    private Integer v01300;//区站号(数字)

    private String cname;//站名

    private BigDecimal v05001;//纬度

    private BigDecimal v06001;//经度

    private Timestamp dUpdateTime;//更新时间

    private Timestamp dDatetime;//监测时间

    private Short v04001;//资料测定年

    private Short v04002;//资料测定月

    private Short v04003;//资料测定日

    private String a30000;//层次

    private String a30006;//磷（g/Kg)

    private String a30001;//钾（g/Kg)

    private String a30002;//全氮（g/Kg)

    private String a30003;//有机质

    private String a30004;//电导率

    private String a30005;//PH值

    private String a00007;//观测员

    private String a00008;//校对员

    private String a00009;//备注
}
