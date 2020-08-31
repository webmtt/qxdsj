package cma.cimiss2.dpc.decoder.bean.agme;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Chnsoil {

    private String dRecordId;//记录标识

    private String v01301;//区站号(字符)

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private BigDecimal dDatetime;//0-5cm土壤重量含水率

    private BigDecimal v71104010;//5-10/0-10cm土壤重量含水率*

    private BigDecimal v71104020;//10-20cm土壤重量含水率

    private BigDecimal v71104030;//20-30cm土壤重量含水率

    private BigDecimal v71104040;//30-40cm土壤重量含水率

    private BigDecimal v71104050;//40-50cm土壤重量含水率

    private BigDecimal v71104060;//50-60cm土壤重量含水率

    private BigDecimal v71104070;//60-70cm土壤重量含水率

    private BigDecimal v71104080;//70-80cm土壤重量含水率

    private BigDecimal v71104090;//80-90cm土壤重量含水率

    private BigDecimal v71104100;//90-100cm土壤重量含水率

    private BigDecimal q05data;//0-5cm层数据质量控制码

    private BigDecimal q0510data;//5-10/0-10cm层数据质量控制码

    private BigDecimal q1020data;//10-20cm层数据质量控制码

    private BigDecimal q2030data;//20-30cm层数据质量控制码

    private BigDecimal q3040data;//30-40cm层数据质量控制码

    private BigDecimal q4050data;//40-50cm层数据质量控制码

    private BigDecimal q5060data;//50-60cm层数据质量控制码

    private BigDecimal q6070data;//60-70cm层数据质量控制码

    private BigDecimal q7080data;//70-80cm层数据质量控制码

    private BigDecimal q8090data;//80-90cm层数据质量控制码

    private BigDecimal q90100data;//90-100cm层数据质量控制码

    private BigDecimal v07061;//地下水位深度

    private BigDecimal qV07061;//地下水位深度质量控制码

    private BigDecimal v711100005;//0-5cm田间持水量

    private BigDecimal v711100510;//5-10/0-10cm田间持水量*

    private BigDecimal v711101020;//10-20cm田间持水量

    private BigDecimal v711102030;//20-30cm田间持水量

    private BigDecimal v711103040;//30-40cm田间持水量

    private BigDecimal v711104050;//40-50cm田间持水量

    private BigDecimal v711105060;//50-60cm田间持水量

    private BigDecimal v711106070;//60-70cm田间持水量

    private BigDecimal v711107080;//70-80cm田间持水量

    private BigDecimal v711108090;//80-90cm田间持水量

    private BigDecimal v7111090100;//90-100cm田间持水量

    private BigDecimal paramenapdate;//参数启用日期#

    private BigDecimal qParamenapdate;//参数启用日期质量控制码

    private BigDecimal v711090005;//0-5cm土壤容重

    private BigDecimal v711090010;//5-10/0-10cm土壤容重*

    private BigDecimal v711091020;//10-20cm土壤容重

    private BigDecimal v711092030;//20-30cm土壤容重

    private BigDecimal v711093040;//30-40cm土壤容重

    private BigDecimal v711094050;//40-50cm土壤容重

    private BigDecimal v711095060;//50-60cm土壤容重

    private BigDecimal v711096070;//60-70cm土壤容重

    private BigDecimal v711097080;//70-80cm土壤容重

    private BigDecimal v711098090;//80-90cm土壤容重

    private BigDecimal v7110990100;//90-100cm土壤容重

    private BigDecimal v711080005;//0-5cm凋萎湿度

    private BigDecimal v711080510;//5-10/0-10cm凋萎湿度*

    private BigDecimal v711081020;//10-20cm凋萎湿度

    private BigDecimal v711082030;//20-30cm凋萎湿度

    private BigDecimal v711083040;//30-40cm凋萎湿度

    private BigDecimal v711084050;//40-50cm凋萎湿度

    private BigDecimal v711085060;//50-60cm凋萎湿度

    private BigDecimal v711086070;//60-70cm凋萎湿度

    private BigDecimal v711087080;//70-80cm凋萎湿度

    private BigDecimal v711088090;//80-90cm凋萎湿度

    private BigDecimal v7110890100;//90-100cm凋萎湿度

    private BigDecimal precipitioncode;//降水/灌溉标识

    private BigDecimal precipitiondata;//降水/灌溉数值

    private BigDecimal qPrecipitiondata;//降水/灌溉数值质量控制码

    private BigDecimal freezemark;//冻结/解冻标识

    private BigDecimal freezedate;//0cm冻结/解冻日期

    private BigDecimal freezedate10;//10cm冻结/解冻日期

    private BigDecimal freezedate20;//20cm冻结/解冻日期

    private BigDecimal freezedate0;//0cm冻结/解冻日期质量控制码

    private BigDecimal qFreezedate10;//10cm冻结/解冻日期质量控制码

    private BigDecimal qFreezedate20;//20cm冻结/解冻日期质量控制码
}
