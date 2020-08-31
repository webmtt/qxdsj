package cma.cimiss2.dpc.decoder.bean.agme;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class GrassNutrition {

    private String dDataId;//资料标识

    private Timestamp dIymdhm;//入库时间

    private Timestamp dRymdhm;//收到时间

    private Timestamp dUpdateTime;//更新时间

    private Timestamp dDatetime;//资料时间

    private String v01301;//区站号(字符)

    private Integer v01300;//区站号(数字)

    private Double v05001;//纬度

    private Double v06001;//经度

    private Double v07001;//测站高度

    private Double v07031;//气压传感器海拔高度

    private Integer v04001;//资料测定年

    private Integer v04002;//资料测定月

    private Integer v04003;//资料测定日

    private String v71501;//牧草名称

    private Double a00100;//粗蛋白（%）

    private Double a00101;//粗脂肪（%）

    private Double a00102;//粗纤维（%）

    private Double a00103;//粗灰分（%）

    private Double a00105;//吸附水（%）

    private Double a00106;//钙含量（%）

    private Double a00107;//磷含量（%）

    private Double a00108;//无氮浸出物含量（%）

    private String a00007;//观测员

    private String a00008;//校对员

    private String a00009;//备注

}
