package cma.cimiss2.dpc.decoder.bean.radi;

import java.math.BigDecimal;


public class SurfXiliGardTab {
    private String dRecordId;//记录标识

    private String v01301;//区站号

    private String v04001;//年

    private String v04002;//月

    private String v04003;//日

    private String dDatetime;//铁塔所在位置经度

    private String dSourceId;//铁塔所在位置纬度

    private String v04004;//梯度塔所处地（湖、海）面拔海高度

    private String v04005;//第1层距地（湖、海）面高度

    private String v04006;//第2层距地（湖、海）面高度

    private String v04007;//第3层距地（湖、海）面高度

    private String v04008;//第4层距地（湖、海）面高度

    private String v01302;//第5层距地（湖、海）面高度

    private BigDecimal v01300;//向下辐射传感器距地（湖、海）面高度

    private BigDecimal v05001;//光合有效辐射传感器距地（湖、海）面高度

    private BigDecimal v06001;//红外地面温度传感器距地面高度

    private String v07001;//时分（北京时）

    private Integer v02001;//向下短波辐射（总辐射）辐照度

    private Integer v02301;//向上短波辐射辐照度

    private String vAcode;//向下长波辐射辐照度

    private String vBbb;//向下长波辐射传感器腔件温度

    private BigDecimal v08010;//向上长波辐射辐照度

    private BigDecimal v07031;//向上长波辐射传感器腔件温度

    private BigDecimal v0703202;//光合有效辐射辐照度

    private BigDecimal v12120;//地面温度（红外）

    private BigDecimal v12030005;//5cm地温

    private BigDecimal v12030010;//10cm地温

    private BigDecimal v12030015;//15cm地温

    private BigDecimal v12030020;//20cm地温

    private BigDecimal v12030040;//40cm地温

    private BigDecimal v71105010;//10 cm土壤体积含水量

    private BigDecimal v71105020;//20 cm土壤体积含水量

    private BigDecimal v71105050;//50 cm土壤体积含水量

    private BigDecimal v71105100;//100 cm土壤体积含水量

    private BigDecimal v71105180;//180 cm土壤体积含水量

    private String shflux1;//土壤热通量1

    private String shflux2;//土壤热通量2

    private String shflux3;//土壤热通量3

    private BigDecimal awspeed1m1l;//第1层高度1分钟平均风速

    private BigDecimal awspeed10m1l;//第1层高度10分钟平均风速

    private BigDecimal maxwspeed1l;//第1层高度极大风速

    private BigDecimal awspeed1m2l;//第2层高度1分钟平均风速

    private BigDecimal awspeed10m2l;//第2层高度10分钟平均风速

    private BigDecimal maxwspeed2l;//第2层高度极大风速

    private BigDecimal awspeed1m10mh;//10米高度1分钟平均风速

    private BigDecimal awdirection1m10mh;//10米高度1分钟平均风向

    private BigDecimal awspeed10m10mh;//10米高度10分钟平均风速

    private BigDecimal awdirection10m10mh;//10米高度10分钟平均风向

    private BigDecimal maxwspeed10mh;//10米高度极大风速

    private BigDecimal awspeed1m5l;//第5层高度1分钟平均风速

    private BigDecimal awspeed10m5l;//第5层高度10分钟平均风速

    private BigDecimal maxwspeed5l;//第5层高度极大风速

    private BigDecimal atemp1m1l;//第1层高度1分钟平均气温

    private BigDecimal arelahum1m1l;//第1层高度1分钟平均相对湿度

    private BigDecimal awvpre1m1l;//第1层高度1分钟平均水汽压

    private BigDecimal atemp1m2l;//第2层高度1分钟平均气温

    private BigDecimal arelahum1m2l;//第2层高度1分钟平均相对湿度

    private BigDecimal awvpre1m2l;//第2层高度1分钟平均水汽压

    private BigDecimal atemp1m10mh;//10米高度1分钟平均气温

    private BigDecimal arelahum1m10mh;//10米高度1分钟平均相对湿度

    private BigDecimal awvpre1m10mh;//10米高度1分钟平均水汽压

    private BigDecimal atemp1m4l;//第4层高度1分钟平均气温

    private BigDecimal arelahum1m4l;//第4层高度1分钟平均相对湿度

    private BigDecimal awvpre1m4l;//第4层高度1分钟平均水汽压

    private BigDecimal atemp1m5l;//第5层高度1分钟平均气温

    private BigDecimal arelahum1m5l;//第5层高度1分钟平均相对湿度

    private BigDecimal awvpre1m5l;//第5层高度1分钟平均水汽压

    private BigDecimal alpressure1m;//1分钟平均本站气压

    private BigDecimal cumpre1m;//1分钟累积降水量

    private BigDecimal cumeva1m;//1分钟累积蒸发量

    private BigDecimal dsrsensoriden;//向下短波辐射传感器标识

    private BigDecimal usrsensoriden;//向上短波辐射传感器标识

    private BigDecimal dlwrsensoriden;//向下长波辐射传感器标识

    private BigDecimal ulwrsensoriden;//向上长波辐射传感器标识

    private BigDecimal parsensoriden;//光合有效辐射传感器标识

    private BigDecimal gtempsensoriden;//地面温度（红外）传感器标识

    private BigDecimal gtempsensoriden5;//5cm地温传感器标识

    private BigDecimal gtempsensoriden10;//10cm地温传感器标识

    private BigDecimal gtempsensoriden15;//15cm地温传感器标识

    private BigDecimal gtempsensoriden20;//20cm地温传感器标识

    private BigDecimal gtempsensoriden40;//40cm地温传感器标识

    private BigDecimal smsensoriden10;//10 cm土壤湿度传感器标识

    private BigDecimal smsensoriden20;//20 cm土壤湿度传感器标识

    private BigDecimal smsensoriden50;//50 cm土壤湿度传感器标识

    private BigDecimal smsensoriden100;//100 cm土壤湿度传感器标识

    private BigDecimal smsensoriden180;//180 cm土壤湿度传感器标识

    private BigDecimal shflux1iden;//土壤热通量1传感器标识

    private BigDecimal shflux2iden;//土壤热通量2传感器标识

    private BigDecimal shflux3iden;//土壤热通量3传感器标识

    private BigDecimal wssensoriden1l;//第1层风速传感器标识

    private BigDecimal tahsensoriden1l;//第1层温湿传感器标识

    private BigDecimal wssensoriden2l;//第2层风速传感器标识

    private BigDecimal tahsensoriden2l;//第2层温湿传感器传感器标识

    private BigDecimal wssensoriden10mh;//10米高度风速传感器标识

    private BigDecimal wdsensoriden10mh;//10米高度风向传感器标识

    private BigDecimal tahsensoriden10mh;//10米高度温湿传感器标识

    private BigDecimal wssensoriden4l;//第4层风速传感器标识

    private BigDecimal tahsensoriden4l;//第4层温湿传感器标识

    private BigDecimal wssensoriden5l;//第5层风速传感器标识

    private BigDecimal tahsensoriden5l;//第5层温湿传感器传感器标识

    private BigDecimal airpsensoriden;//气压传感器标识

    private BigDecimal evasensoriden;//蒸发传感器标识

    private String v04300017;//年月日时分（北京时）

    private BigDecimal midsradiationhah;//半小时内向下短波辐射（总辐射）辐照度平均值

    private BigDecimal maxhdsradiation;//向下短波辐射（总辐射）辐照度小时内的最大值

    private BigDecimal tmaxowhdswradiation;//向下短波辐射（总辐射）辐照度小时内的最大值出现时间

    private BigDecimal ehdswradiation;//向下短波辐射（总辐射）小时内的曝辐量

    private BigDecimal awswradiationihah;//半小时内向上短波辐射辐照度平均值

    private BigDecimal maxihousradiation;//向上短波辐射辐照度小时内的最大值

    private BigDecimal maxtousradiationhi;//向上短波辐射辐照度小时内的最大值出现时间

    private BigDecimal ehuswradiation;//向上短波辐射小时内的曝辐量

    private BigDecimal midlwradiationhah;//半小时内向下长波辐射辐照度平均值

    private BigDecimal maxihdlwradiation;//向下长波辐射辐照度小时内的最大值

    private BigDecimal maxtowhdlwradiationi;//向下长波辐射辐照度小时内的最大值出现时间

    private BigDecimal ewhdlwradiation;//向下长波辐射小时内的曝辐量

    private BigDecimal miulwradiationhah;//半小时内向上长波辐射辐照度平均值

    private BigDecimal maxihulwradiation;//向上长波辐射辐照度小时内的最大值

    private BigDecimal maxtowhulwradiationi;//向上长波辐射辐照度小时内的最大值出现时间

    private BigDecimal eihulwradiation;//向上长波辐射小时内的曝辐量

    private BigDecimal anradiaihah;//半小时内净辐射辐照度平均值

    private BigDecimal maxihnradiai;//净辐射辐照度小时内的最大值

    private BigDecimal maxtowhnradiai;//净辐射辐照度小时内的最大值出现时间

    private BigDecimal minhnradiai;//净辐射辐照度小时内的最小值

    private BigDecimal mintowhnradiai;//净辐射辐照度小时内的最小值出现时间

    private BigDecimal ewhnradiation;//净辐射小时内的曝辐量

    private BigDecimal aiparadiahah;//半小时内光合有效辐射辐照度平均值

    private BigDecimal v143400560;//光合有效辐射辐照度小时内的最大值

    private Integer v1434005052;//光合有效辐射辐照度小时内的最大值出现时间

    private Integer eparadiationih;//光合有效辐射小时内的曝辐量

    private Integer agtemphah;//半小时内平均地面温度（红外）

    private Integer maxgtemph;//小时内地面最高温度（红外）

    private Integer maxotgh;//小时内地面最高（红外）出现时间

    private Integer mingtemph;//小时内地面最低温度（红外）

    private Integer minotgh;//小时内地面最低（红外）出现时间

    private Integer agtemp5cmhah;//半小时内平均5cm地温

    private Integer agtemp10cmhah;//半小时内平均10cm地温

    private Integer agtemp15cmhah;//半小时内平均15cm地温

    private Integer agtemp20cmhah;//半小时内平均20cm地温

    private Integer agtemp40cmhah;//半小时内平均40cm地温

    private Integer a10cmsvwchah;//半小时内平均10 cm土壤体积含水量

    private Integer a20cmsvwchah;//半小时内平均20 cm土壤体积含水量

    private Integer a50cmsvwchah;//半小时内平均50 cm土壤体积含水量

    private Integer a100cmsvwchah;//半小时内平均100 cm土壤体积含水量

    private Integer a180cmsvwchah;//半小时内平均180 cm土壤体积含水量

    private Integer ashfluxhah;//半小时内平均土壤热通量

    private Integer awspeedhah1l;//第1层高度半小时内平均风速

    private Integer exwspeedh1l;//第1层高度小时内极大风速

    private Integer maxwspeedtimeh1l;//第1层高度小时内极大风速出现时间

    private Integer maxwspeedh1l;//第1层高度小时内最大风速

    private Integer timemaxwvh1l;//第1层高度小时内最大风速出现时间

    private Integer awspeedhah2l;//第2层高度半小时内平均风速

    private Integer exwspeedh2l;//第2层高度小时内极大风速

    private Integer maxwspeedtimeh2l;//第2层高度小时内极大风速出现时间

    private Integer maxwspeedh2l;//第2层高度小时内最大风速

    private Integer timemaxwvh2l;//第2层高度小时内最大风速出现时间

    private Integer awspeedhah10m;//10米高度半小时内平均风速

    private Integer avshwv10mhah;//10米高度半小时内平均矢量合成水平风速

    private Integer avchwdir10mhah;//10米高度半小时内平均矢量合成水平风向

    private String sdwdirection;//风向标准差

    private Integer exwspeedh10m;//10米高度小时内极大风速

    private Integer maxwdirwspeed11mh;//10米高度小时内极大风速的风向

    private Integer maxwspeedt10mh;//10米高度小时内极大风速出现时间

    private Integer maxwspeedh10m;//10米高度小时内最大风速

    private Integer maxwdirwspeed12mh;//10米高度小时内最大风速的风向

    private Integer maxwspeedt11mh;//10米高度小时内最大风速出现时间

    private Integer awspeedhah4l;//第4层高度半小时内平均风速

    private Integer exwspeedh4l;//第4层高度小时内极大风速

    private Integer maxwspeedtimeh4l;//第4层高度小时内极大风速出现时间

    private Integer maxwspeedh4l;//第4层高度小时内最大风速

    private Integer timemaxwvh4l;//第4层高度小时内最大风速出现时间

    private Integer awspeedhah5l;//第5层高度半小时内平均风速

    private Integer exwspeedh5l;//第5层高度小时内极大风速

    private Integer maxwspeedtimeh5l;//第5层高度小时内极大风速出现时间

    private Integer maxwspeedh5l;//第5层高度小时内最大风速

    private Integer timemaxwvh5l;//第5层高度小时内最大风速出现时间

    private Integer atemphah1l;//第1层高度半小时平均气温

    private Integer maxtemph1l;//第1层高度小时内最高气温

    private Integer tmaxtempah1l;//第1层高度小时内最高气温出现时间

    private Integer mintemph1l;//第1层高度小时内最低气温

    private Integer tmintempah1l;//第1层高度小时内最低气温出现时间

    private Integer arhhah1l;//第1层高度半小时平均相对湿度

    private Integer minrhh1l;//第1层高度小时内最小相对湿度

    private Integer minrhoth1l;//第1层高度小时内最小相对湿度出现时间

    private Integer awvphah1l;//第1层高度半小时平均水汽压

    private Integer atemphah2l;//第2层高度半小时平均气温

    private Integer maxtemph2l;//第2层高度小时内最高气温

    private Integer tmaxtempah2l;//第2层高度小时内最高气温出现时间

    private Integer mintemph2l;//第2层高度小时内最低气温

    private Integer tmintempah2l;//第2层高度小时内最低气温出现时间

    private Integer arhhah2l;//第2层高度半小时平均相对湿度

    private Integer minrhh2l;//第2层高度小时内最小相对湿度

    private Integer minrhoth2l;//第2层高度小时内最小相对湿度出现时间

    private Integer awvphah2l;//第2层高度半小时平均水汽压

    private Integer atemphah10l;//10米高度半小时平均气温

    private Integer maxtemph10l;//10米高度小时内最高气温

    private Integer tmaxtempah10l;//10米高度小时内最高气温出现时间

    private Integer mintemph10l;//10米高度小时内最低气温

    private Integer tmintempah10l;//10米高度小时内最低气温出现时间

    private Integer arhhah10l;//10米高度半小时平均相对湿度

    private Integer minrhh10l;//10米高度小时内最小相对湿度

    private Integer minrhoth10l;//10米高度小时内最小相对湿度出现时间

    private Integer awvphah10l;//10米高度半小时平均水汽压

    private Integer atemphah4l;//第4层高度半小时平均气温

    private Integer maxtemph4l;//第4层高度小时内最高气温

    private Integer tmaxtempah4l;//第4层高度小时内最高气温出现时间

    private Integer mintemph4l;//第4层高度小时内最低气温

    private Integer tmintempah4l;//第4层高度小时内最低气温出现时间

    private Integer arhhah4l;//第4层高度半小时平均相对湿度

    private Integer minrhh4l;//第4层高度小时内最小相对湿度

    private Integer minrhoth4l;//第4层高度小时内最小相对湿度出现时间

    private Integer awvphah4l;//第4层高度半小时平均水汽压

    private Integer atemphah5l;//第5层高度半小时平均气温

    private Integer maxtemph5l;//第5层高度小时内最高气温

    private Integer tmaxtempah5l;//第5层高度小时内最高气温出现时间

    private Integer mintemph5l;//第5层高度小时内最低气温

    private Integer tmintempah5l;//第5层高度小时内最低气温出现时间

    private Integer arhhah5l;//第5层高度半小时平均相对湿度

    private Integer minrhh5l;//第5层高度小时内最小相对湿度

    private Integer minrhoth5l;//第5层高度小时内最小相对湿度出现时间

    private Integer awvphah5l;//第5层高度半小时平均水汽压

    private Integer alpressurehah;//半小时内平均本站气压

    private BigDecimal v10301;//小时内最高本站气压

    private Integer v10301052;//小时内最高本站气压出现时间

    private BigDecimal v10302;//小时内最低本站气压

    private Integer v10302052;//小时内最低本站气压出现时间

    private Integer aprecipitationh;//小时内累积降水量

    private Integer cumevah;//小时内累积蒸发量

    private String version;//版本号

    public String getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(String dRecordId) {
        this.dRecordId = dRecordId == null ? null : dRecordId.trim();
    }

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301 == null ? null : v01301.trim();
    }

    public String getV04001() {
        return v04001;
    }

    public void setV04001(String v04001) {
        this.v04001 = v04001;
    }

    public String getV04002() {
        return v04002;
    }

    public void setV04002(String v04002) {
        this.v04002 = v04002;
    }

    public String getV04003() {
        return v04003;
    }

    public void setV04003(String v04003) {
        this.v04003 = v04003;
    }

    public String getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(String dDatetime) {
        this.dDatetime = dDatetime;
    }

    public String getdSourceId() {
        return dSourceId;
    }

    public void setdSourceId(String dSourceId) {
        this.dSourceId = dSourceId == null ? null : dSourceId.trim();
    }

    public String getV04004() {
        return v04004;
    }

    public void setV04004(String v04004) {
        this.v04004 = v04004;
    }

    public String getV04005() {
        return v04005;
    }

    public void setV04005(String v04005) {
        this.v04005 = v04005;
    }

    public String getV04006() {
        return v04006;
    }

    public void setV04006(String v04006) {
        this.v04006 = v04006;
    }

    public String getV04007() {
        return v04007;
    }

    public void setV04007(String v04007) {
        this.v04007 = v04007;
    }

    public String getV04008() {
        return v04008;
    }

    public void setV04008(String v04008) {
        this.v04008 = v04008;
    }

    public String getV01302() {
        return v01302;
    }

    public void setV01302(String v01302) {
        this.v01302 = v01302 == null ? null : v01302.trim();
    }

    public BigDecimal getV01300() {
        return v01300;
    }

    public void setV01300(BigDecimal v01300) {
        this.v01300 = v01300;
    }

    public BigDecimal getV05001() {
        return v05001;
    }

    public void setV05001(BigDecimal v05001) {
        this.v05001 = v05001;
    }

    public BigDecimal getV06001() {
        return v06001;
    }

    public void setV06001(BigDecimal v06001) {
        this.v06001 = v06001;
    }

    public String getV07001() {
        return v07001;
    }

    public void setV07001(String v07001) {
        this.v07001 = v07001;
    }

    public Integer getV02001() {
        return v02001;
    }

    public void setV02001(Integer v02001) {
        this.v02001 = v02001;
    }

    public Integer getV02301() {
        return v02301;
    }

    public void setV02301(Integer v02301) {
        this.v02301 = v02301;
    }

    public String getvAcode() {
        return vAcode;
    }

    public void setvAcode(String vAcode) {
        this.vAcode = vAcode == null ? null : vAcode.trim();
    }

    public String getvBbb() {
        return vBbb;
    }

    public void setvBbb(String vBbb) {
        this.vBbb = vBbb == null ? null : vBbb.trim();
    }

    public BigDecimal getV08010() {
        return v08010;
    }

    public void setV08010(BigDecimal v08010) {
        this.v08010 = v08010;
    }

    public BigDecimal getV07031() {
        return v07031;
    }

    public void setV07031(BigDecimal v07031) {
        this.v07031 = v07031;
    }

    public BigDecimal getV0703202() {
        return v0703202;
    }

    public void setV0703202(BigDecimal v0703202) {
        this.v0703202 = v0703202;
    }

    public BigDecimal getV12120() {
        return v12120;
    }

    public void setV12120(BigDecimal v12120) {
        this.v12120 = v12120;
    }

    public BigDecimal getV12030005() {
        return v12030005;
    }

    public void setV12030005(BigDecimal v12030005) {
        this.v12030005 = v12030005;
    }

    public BigDecimal getV12030010() {
        return v12030010;
    }

    public void setV12030010(BigDecimal v12030010) {
        this.v12030010 = v12030010;
    }

    public BigDecimal getV12030015() {
        return v12030015;
    }

    public void setV12030015(BigDecimal v12030015) {
        this.v12030015 = v12030015;
    }

    public BigDecimal getV12030020() {
        return v12030020;
    }

    public void setV12030020(BigDecimal v12030020) {
        this.v12030020 = v12030020;
    }

    public BigDecimal getV12030040() {
        return v12030040;
    }

    public void setV12030040(BigDecimal v12030040) {
        this.v12030040 = v12030040;
    }

    public BigDecimal getV71105010() {
        return v71105010;
    }

    public void setV71105010(BigDecimal v71105010) {
        this.v71105010 = v71105010;
    }

    public BigDecimal getV71105020() {
        return v71105020;
    }

    public void setV71105020(BigDecimal v71105020) {
        this.v71105020 = v71105020;
    }

    public BigDecimal getV71105050() {
        return v71105050;
    }

    public void setV71105050(BigDecimal v71105050) {
        this.v71105050 = v71105050;
    }

    public BigDecimal getV71105100() {
        return v71105100;
    }

    public void setV71105100(BigDecimal v71105100) {
        this.v71105100 = v71105100;
    }

    public BigDecimal getV71105180() {
        return v71105180;
    }

    public void setV71105180(BigDecimal v71105180) {
        this.v71105180 = v71105180;
    }

    public String getShflux1() {
        return shflux1;
    }

    public void setShflux1(String shflux1) {
        this.shflux1 = shflux1 == null ? null : shflux1.trim();
    }

    public String getShflux2() {
        return shflux2;
    }

    public void setShflux2(String shflux2) {
        this.shflux2 = shflux2 == null ? null : shflux2.trim();
    }

    public String getShflux3() {
        return shflux3;
    }

    public void setShflux3(String shflux3) {
        this.shflux3 = shflux3 == null ? null : shflux3.trim();
    }

    public BigDecimal getAwspeed1m1l() {
        return awspeed1m1l;
    }

    public void setAwspeed1m1l(BigDecimal awspeed1m1l) {
        this.awspeed1m1l = awspeed1m1l;
    }

    public BigDecimal getAwspeed10m1l() {
        return awspeed10m1l;
    }

    public void setAwspeed10m1l(BigDecimal awspeed10m1l) {
        this.awspeed10m1l = awspeed10m1l;
    }

    public BigDecimal getMaxwspeed1l() {
        return maxwspeed1l;
    }

    public void setMaxwspeed1l(BigDecimal maxwspeed1l) {
        this.maxwspeed1l = maxwspeed1l;
    }

    public BigDecimal getAwspeed1m2l() {
        return awspeed1m2l;
    }

    public void setAwspeed1m2l(BigDecimal awspeed1m2l) {
        this.awspeed1m2l = awspeed1m2l;
    }

    public BigDecimal getAwspeed10m2l() {
        return awspeed10m2l;
    }

    public void setAwspeed10m2l(BigDecimal awspeed10m2l) {
        this.awspeed10m2l = awspeed10m2l;
    }

    public BigDecimal getMaxwspeed2l() {
        return maxwspeed2l;
    }

    public void setMaxwspeed2l(BigDecimal maxwspeed2l) {
        this.maxwspeed2l = maxwspeed2l;
    }

    public BigDecimal getAwspeed1m10mh() {
        return awspeed1m10mh;
    }

    public void setAwspeed1m10mh(BigDecimal awspeed1m10mh) {
        this.awspeed1m10mh = awspeed1m10mh;
    }

    public BigDecimal getAwdirection1m10mh() {
        return awdirection1m10mh;
    }

    public void setAwdirection1m10mh(BigDecimal awdirection1m10mh) {
        this.awdirection1m10mh = awdirection1m10mh;
    }

    public BigDecimal getAwspeed10m10mh() {
        return awspeed10m10mh;
    }

    public void setAwspeed10m10mh(BigDecimal awspeed10m10mh) {
        this.awspeed10m10mh = awspeed10m10mh;
    }

    public BigDecimal getAwdirection10m10mh() {
        return awdirection10m10mh;
    }

    public void setAwdirection10m10mh(BigDecimal awdirection10m10mh) {
        this.awdirection10m10mh = awdirection10m10mh;
    }

    public BigDecimal getMaxwspeed10mh() {
        return maxwspeed10mh;
    }

    public void setMaxwspeed10mh(BigDecimal maxwspeed10mh) {
        this.maxwspeed10mh = maxwspeed10mh;
    }

    public BigDecimal getAwspeed1m5l() {
        return awspeed1m5l;
    }

    public void setAwspeed1m5l(BigDecimal awspeed1m5l) {
        this.awspeed1m5l = awspeed1m5l;
    }

    public BigDecimal getAwspeed10m5l() {
        return awspeed10m5l;
    }

    public void setAwspeed10m5l(BigDecimal awspeed10m5l) {
        this.awspeed10m5l = awspeed10m5l;
    }

    public BigDecimal getMaxwspeed5l() {
        return maxwspeed5l;
    }

    public void setMaxwspeed5l(BigDecimal maxwspeed5l) {
        this.maxwspeed5l = maxwspeed5l;
    }

    public BigDecimal getAtemp1m1l() {
        return atemp1m1l;
    }

    public void setAtemp1m1l(BigDecimal atemp1m1l) {
        this.atemp1m1l = atemp1m1l;
    }

    public BigDecimal getArelahum1m1l() {
        return arelahum1m1l;
    }

    public void setArelahum1m1l(BigDecimal arelahum1m1l) {
        this.arelahum1m1l = arelahum1m1l;
    }

    public BigDecimal getAwvpre1m1l() {
        return awvpre1m1l;
    }

    public void setAwvpre1m1l(BigDecimal awvpre1m1l) {
        this.awvpre1m1l = awvpre1m1l;
    }

    public BigDecimal getAtemp1m2l() {
        return atemp1m2l;
    }

    public void setAtemp1m2l(BigDecimal atemp1m2l) {
        this.atemp1m2l = atemp1m2l;
    }

    public BigDecimal getArelahum1m2l() {
        return arelahum1m2l;
    }

    public void setArelahum1m2l(BigDecimal arelahum1m2l) {
        this.arelahum1m2l = arelahum1m2l;
    }

    public BigDecimal getAwvpre1m2l() {
        return awvpre1m2l;
    }

    public void setAwvpre1m2l(BigDecimal awvpre1m2l) {
        this.awvpre1m2l = awvpre1m2l;
    }

    public BigDecimal getAtemp1m10mh() {
        return atemp1m10mh;
    }

    public void setAtemp1m10mh(BigDecimal atemp1m10mh) {
        this.atemp1m10mh = atemp1m10mh;
    }

    public BigDecimal getArelahum1m10mh() {
        return arelahum1m10mh;
    }

    public void setArelahum1m10mh(BigDecimal arelahum1m10mh) {
        this.arelahum1m10mh = arelahum1m10mh;
    }

    public BigDecimal getAwvpre1m10mh() {
        return awvpre1m10mh;
    }

    public void setAwvpre1m10mh(BigDecimal awvpre1m10mh) {
        this.awvpre1m10mh = awvpre1m10mh;
    }

    public BigDecimal getAtemp1m4l() {
        return atemp1m4l;
    }

    public void setAtemp1m4l(BigDecimal atemp1m4l) {
        this.atemp1m4l = atemp1m4l;
    }

    public BigDecimal getArelahum1m4l() {
        return arelahum1m4l;
    }

    public void setArelahum1m4l(BigDecimal arelahum1m4l) {
        this.arelahum1m4l = arelahum1m4l;
    }

    public BigDecimal getAwvpre1m4l() {
        return awvpre1m4l;
    }

    public void setAwvpre1m4l(BigDecimal awvpre1m4l) {
        this.awvpre1m4l = awvpre1m4l;
    }

    public BigDecimal getAtemp1m5l() {
        return atemp1m5l;
    }

    public void setAtemp1m5l(BigDecimal atemp1m5l) {
        this.atemp1m5l = atemp1m5l;
    }

    public BigDecimal getArelahum1m5l() {
        return arelahum1m5l;
    }

    public void setArelahum1m5l(BigDecimal arelahum1m5l) {
        this.arelahum1m5l = arelahum1m5l;
    }

    public BigDecimal getAwvpre1m5l() {
        return awvpre1m5l;
    }

    public void setAwvpre1m5l(BigDecimal awvpre1m5l) {
        this.awvpre1m5l = awvpre1m5l;
    }

    public BigDecimal getAlpressure1m() {
        return alpressure1m;
    }

    public void setAlpressure1m(BigDecimal alpressure1m) {
        this.alpressure1m = alpressure1m;
    }

    public BigDecimal getCumpre1m() {
        return cumpre1m;
    }

    public void setCumpre1m(BigDecimal cumpre1m) {
        this.cumpre1m = cumpre1m;
    }

    public BigDecimal getCumeva1m() {
        return cumeva1m;
    }

    public void setCumeva1m(BigDecimal cumeva1m) {
        this.cumeva1m = cumeva1m;
    }

    public BigDecimal getDsrsensoriden() {
        return dsrsensoriden;
    }

    public void setDsrsensoriden(BigDecimal dsrsensoriden) {
        this.dsrsensoriden = dsrsensoriden;
    }

    public BigDecimal getUsrsensoriden() {
        return usrsensoriden;
    }

    public void setUsrsensoriden(BigDecimal usrsensoriden) {
        this.usrsensoriden = usrsensoriden;
    }

    public BigDecimal getDlwrsensoriden() {
        return dlwrsensoriden;
    }

    public void setDlwrsensoriden(BigDecimal dlwrsensoriden) {
        this.dlwrsensoriden = dlwrsensoriden;
    }

    public BigDecimal getUlwrsensoriden() {
        return ulwrsensoriden;
    }

    public void setUlwrsensoriden(BigDecimal ulwrsensoriden) {
        this.ulwrsensoriden = ulwrsensoriden;
    }

    public BigDecimal getParsensoriden() {
        return parsensoriden;
    }

    public void setParsensoriden(BigDecimal parsensoriden) {
        this.parsensoriden = parsensoriden;
    }

    public BigDecimal getGtempsensoriden() {
        return gtempsensoriden;
    }

    public void setGtempsensoriden(BigDecimal gtempsensoriden) {
        this.gtempsensoriden = gtempsensoriden;
    }

    public BigDecimal getGtempsensoriden5() {
        return gtempsensoriden5;
    }

    public void setGtempsensoriden5(BigDecimal gtempsensoriden5) {
        this.gtempsensoriden5 = gtempsensoriden5;
    }

    public BigDecimal getGtempsensoriden10() {
        return gtempsensoriden10;
    }

    public void setGtempsensoriden10(BigDecimal gtempsensoriden10) {
        this.gtempsensoriden10 = gtempsensoriden10;
    }

    public BigDecimal getGtempsensoriden15() {
        return gtempsensoriden15;
    }

    public void setGtempsensoriden15(BigDecimal gtempsensoriden15) {
        this.gtempsensoriden15 = gtempsensoriden15;
    }

    public BigDecimal getGtempsensoriden20() {
        return gtempsensoriden20;
    }

    public void setGtempsensoriden20(BigDecimal gtempsensoriden20) {
        this.gtempsensoriden20 = gtempsensoriden20;
    }

    public BigDecimal getGtempsensoriden40() {
        return gtempsensoriden40;
    }

    public void setGtempsensoriden40(BigDecimal gtempsensoriden40) {
        this.gtempsensoriden40 = gtempsensoriden40;
    }

    public BigDecimal getSmsensoriden10() {
        return smsensoriden10;
    }

    public void setSmsensoriden10(BigDecimal smsensoriden10) {
        this.smsensoriden10 = smsensoriden10;
    }

    public BigDecimal getSmsensoriden20() {
        return smsensoriden20;
    }

    public void setSmsensoriden20(BigDecimal smsensoriden20) {
        this.smsensoriden20 = smsensoriden20;
    }

    public BigDecimal getSmsensoriden50() {
        return smsensoriden50;
    }

    public void setSmsensoriden50(BigDecimal smsensoriden50) {
        this.smsensoriden50 = smsensoriden50;
    }

    public BigDecimal getSmsensoriden100() {
        return smsensoriden100;
    }

    public void setSmsensoriden100(BigDecimal smsensoriden100) {
        this.smsensoriden100 = smsensoriden100;
    }

    public BigDecimal getSmsensoriden180() {
        return smsensoriden180;
    }

    public void setSmsensoriden180(BigDecimal smsensoriden180) {
        this.smsensoriden180 = smsensoriden180;
    }

    public BigDecimal getShflux1iden() {
        return shflux1iden;
    }

    public void setShflux1iden(BigDecimal shflux1iden) {
        this.shflux1iden = shflux1iden;
    }

    public BigDecimal getShflux2iden() {
        return shflux2iden;
    }

    public void setShflux2iden(BigDecimal shflux2iden) {
        this.shflux2iden = shflux2iden;
    }

    public BigDecimal getShflux3iden() {
        return shflux3iden;
    }

    public void setShflux3iden(BigDecimal shflux3iden) {
        this.shflux3iden = shflux3iden;
    }

    public BigDecimal getWssensoriden1l() {
        return wssensoriden1l;
    }

    public void setWssensoriden1l(BigDecimal wssensoriden1l) {
        this.wssensoriden1l = wssensoriden1l;
    }

    public BigDecimal getTahsensoriden1l() {
        return tahsensoriden1l;
    }

    public void setTahsensoriden1l(BigDecimal tahsensoriden1l) {
        this.tahsensoriden1l = tahsensoriden1l;
    }

    public BigDecimal getWssensoriden2l() {
        return wssensoriden2l;
    }

    public void setWssensoriden2l(BigDecimal wssensoriden2l) {
        this.wssensoriden2l = wssensoriden2l;
    }

    public BigDecimal getTahsensoriden2l() {
        return tahsensoriden2l;
    }

    public void setTahsensoriden2l(BigDecimal tahsensoriden2l) {
        this.tahsensoriden2l = tahsensoriden2l;
    }

    public BigDecimal getWssensoriden10mh() {
        return wssensoriden10mh;
    }

    public void setWssensoriden10mh(BigDecimal wssensoriden10mh) {
        this.wssensoriden10mh = wssensoriden10mh;
    }

    public BigDecimal getWdsensoriden10mh() {
        return wdsensoriden10mh;
    }

    public void setWdsensoriden10mh(BigDecimal wdsensoriden10mh) {
        this.wdsensoriden10mh = wdsensoriden10mh;
    }

    public BigDecimal getTahsensoriden10mh() {
        return tahsensoriden10mh;
    }

    public void setTahsensoriden10mh(BigDecimal tahsensoriden10mh) {
        this.tahsensoriden10mh = tahsensoriden10mh;
    }

    public BigDecimal getWssensoriden4l() {
        return wssensoriden4l;
    }

    public void setWssensoriden4l(BigDecimal wssensoriden4l) {
        this.wssensoriden4l = wssensoriden4l;
    }

    public BigDecimal getTahsensoriden4l() {
        return tahsensoriden4l;
    }

    public void setTahsensoriden4l(BigDecimal tahsensoriden4l) {
        this.tahsensoriden4l = tahsensoriden4l;
    }

    public BigDecimal getWssensoriden5l() {
        return wssensoriden5l;
    }

    public void setWssensoriden5l(BigDecimal wssensoriden5l) {
        this.wssensoriden5l = wssensoriden5l;
    }

    public BigDecimal getTahsensoriden5l() {
        return tahsensoriden5l;
    }

    public void setTahsensoriden5l(BigDecimal tahsensoriden5l) {
        this.tahsensoriden5l = tahsensoriden5l;
    }

    public BigDecimal getAirpsensoriden() {
        return airpsensoriden;
    }

    public void setAirpsensoriden(BigDecimal airpsensoriden) {
        this.airpsensoriden = airpsensoriden;
    }

    public BigDecimal getEvasensoriden() {
        return evasensoriden;
    }

    public void setEvasensoriden(BigDecimal evasensoriden) {
        this.evasensoriden = evasensoriden;
    }

    public String getV04300017() {
        return v04300017;
    }

    public void setV04300017(String v04300017) {
        this.v04300017 = v04300017;
    }

    public BigDecimal getMidsradiationhah() {
        return midsradiationhah;
    }

    public void setMidsradiationhah(BigDecimal midsradiationhah) {
        this.midsradiationhah = midsradiationhah;
    }

    public BigDecimal getMaxhdsradiation() {
        return maxhdsradiation;
    }

    public void setMaxhdsradiation(BigDecimal maxhdsradiation) {
        this.maxhdsradiation = maxhdsradiation;
    }

    public BigDecimal getTmaxowhdswradiation() {
        return tmaxowhdswradiation;
    }

    public void setTmaxowhdswradiation(BigDecimal tmaxowhdswradiation) {
        this.tmaxowhdswradiation = tmaxowhdswradiation;
    }

    public BigDecimal getEhdswradiation() {
        return ehdswradiation;
    }

    public void setEhdswradiation(BigDecimal ehdswradiation) {
        this.ehdswradiation = ehdswradiation;
    }

    public BigDecimal getAwswradiationihah() {
        return awswradiationihah;
    }

    public void setAwswradiationihah(BigDecimal awswradiationihah) {
        this.awswradiationihah = awswradiationihah;
    }

    public BigDecimal getMaxihousradiation() {
        return maxihousradiation;
    }

    public void setMaxihousradiation(BigDecimal maxihousradiation) {
        this.maxihousradiation = maxihousradiation;
    }

    public BigDecimal getMaxtousradiationhi() {
        return maxtousradiationhi;
    }

    public void setMaxtousradiationhi(BigDecimal maxtousradiationhi) {
        this.maxtousradiationhi = maxtousradiationhi;
    }

    public BigDecimal getEhuswradiation() {
        return ehuswradiation;
    }

    public void setEhuswradiation(BigDecimal ehuswradiation) {
        this.ehuswradiation = ehuswradiation;
    }

    public BigDecimal getMidlwradiationhah() {
        return midlwradiationhah;
    }

    public void setMidlwradiationhah(BigDecimal midlwradiationhah) {
        this.midlwradiationhah = midlwradiationhah;
    }

    public BigDecimal getMaxihdlwradiation() {
        return maxihdlwradiation;
    }

    public void setMaxihdlwradiation(BigDecimal maxihdlwradiation) {
        this.maxihdlwradiation = maxihdlwradiation;
    }

    public BigDecimal getMaxtowhdlwradiationi() {
        return maxtowhdlwradiationi;
    }

    public void setMaxtowhdlwradiationi(BigDecimal maxtowhdlwradiationi) {
        this.maxtowhdlwradiationi = maxtowhdlwradiationi;
    }

    public BigDecimal getEwhdlwradiation() {
        return ewhdlwradiation;
    }

    public void setEwhdlwradiation(BigDecimal ewhdlwradiation) {
        this.ewhdlwradiation = ewhdlwradiation;
    }

    public BigDecimal getMiulwradiationhah() {
        return miulwradiationhah;
    }

    public void setMiulwradiationhah(BigDecimal miulwradiationhah) {
        this.miulwradiationhah = miulwradiationhah;
    }

    public BigDecimal getMaxihulwradiation() {
        return maxihulwradiation;
    }

    public void setMaxihulwradiation(BigDecimal maxihulwradiation) {
        this.maxihulwradiation = maxihulwradiation;
    }

    public BigDecimal getMaxtowhulwradiationi() {
        return maxtowhulwradiationi;
    }

    public void setMaxtowhulwradiationi(BigDecimal maxtowhulwradiationi) {
        this.maxtowhulwradiationi = maxtowhulwradiationi;
    }

    public BigDecimal getEihulwradiation() {
        return eihulwradiation;
    }

    public void setEihulwradiation(BigDecimal eihulwradiation) {
        this.eihulwradiation = eihulwradiation;
    }

    public BigDecimal getAnradiaihah() {
        return anradiaihah;
    }

    public void setAnradiaihah(BigDecimal anradiaihah) {
        this.anradiaihah = anradiaihah;
    }

    public BigDecimal getMaxihnradiai() {
        return maxihnradiai;
    }

    public void setMaxihnradiai(BigDecimal maxihnradiai) {
        this.maxihnradiai = maxihnradiai;
    }

    public BigDecimal getMaxtowhnradiai() {
        return maxtowhnradiai;
    }

    public void setMaxtowhnradiai(BigDecimal maxtowhnradiai) {
        this.maxtowhnradiai = maxtowhnradiai;
    }

    public BigDecimal getMinhnradiai() {
        return minhnradiai;
    }

    public void setMinhnradiai(BigDecimal minhnradiai) {
        this.minhnradiai = minhnradiai;
    }

    public BigDecimal getMintowhnradiai() {
        return mintowhnradiai;
    }

    public void setMintowhnradiai(BigDecimal mintowhnradiai) {
        this.mintowhnradiai = mintowhnradiai;
    }

    public BigDecimal getEwhnradiation() {
        return ewhnradiation;
    }

    public void setEwhnradiation(BigDecimal ewhnradiation) {
        this.ewhnradiation = ewhnradiation;
    }

    public BigDecimal getAiparadiahah() {
        return aiparadiahah;
    }

    public void setAiparadiahah(BigDecimal aiparadiahah) {
        this.aiparadiahah = aiparadiahah;
    }

    public BigDecimal getV143400560() {
        return v143400560;
    }

    public void setV143400560(BigDecimal v143400560) {
        this.v143400560 = v143400560;
    }

    public Integer getV1434005052() {
        return v1434005052;
    }

    public void setV1434005052(Integer v1434005052) {
        this.v1434005052 = v1434005052;
    }

    public Integer getEparadiationih() {
        return eparadiationih;
    }

    public void setEparadiationih(Integer eparadiationih) {
        this.eparadiationih = eparadiationih;
    }

    public Integer getAgtemphah() {
        return agtemphah;
    }

    public void setAgtemphah(Integer agtemphah) {
        this.agtemphah = agtemphah;
    }

    public Integer getMaxgtemph() {
        return maxgtemph;
    }

    public void setMaxgtemph(Integer maxgtemph) {
        this.maxgtemph = maxgtemph;
    }

    public Integer getMaxotgh() {
        return maxotgh;
    }

    public void setMaxotgh(Integer maxotgh) {
        this.maxotgh = maxotgh;
    }

    public Integer getMingtemph() {
        return mingtemph;
    }

    public void setMingtemph(Integer mingtemph) {
        this.mingtemph = mingtemph;
    }

    public Integer getMinotgh() {
        return minotgh;
    }

    public void setMinotgh(Integer minotgh) {
        this.minotgh = minotgh;
    }

    public Integer getAgtemp5cmhah() {
        return agtemp5cmhah;
    }

    public void setAgtemp5cmhah(Integer agtemp5cmhah) {
        this.agtemp5cmhah = agtemp5cmhah;
    }

    public Integer getAgtemp10cmhah() {
        return agtemp10cmhah;
    }

    public void setAgtemp10cmhah(Integer agtemp10cmhah) {
        this.agtemp10cmhah = agtemp10cmhah;
    }

    public Integer getAgtemp15cmhah() {
        return agtemp15cmhah;
    }

    public void setAgtemp15cmhah(Integer agtemp15cmhah) {
        this.agtemp15cmhah = agtemp15cmhah;
    }

    public Integer getAgtemp20cmhah() {
        return agtemp20cmhah;
    }

    public void setAgtemp20cmhah(Integer agtemp20cmhah) {
        this.agtemp20cmhah = agtemp20cmhah;
    }

    public Integer getAgtemp40cmhah() {
        return agtemp40cmhah;
    }

    public void setAgtemp40cmhah(Integer agtemp40cmhah) {
        this.agtemp40cmhah = agtemp40cmhah;
    }

    public Integer getA10cmsvwchah() {
        return a10cmsvwchah;
    }

    public void setA10cmsvwchah(Integer a10cmsvwchah) {
        this.a10cmsvwchah = a10cmsvwchah;
    }

    public Integer getA20cmsvwchah() {
        return a20cmsvwchah;
    }

    public void setA20cmsvwchah(Integer a20cmsvwchah) {
        this.a20cmsvwchah = a20cmsvwchah;
    }

    public Integer getA50cmsvwchah() {
        return a50cmsvwchah;
    }

    public void setA50cmsvwchah(Integer a50cmsvwchah) {
        this.a50cmsvwchah = a50cmsvwchah;
    }

    public Integer getA100cmsvwchah() {
        return a100cmsvwchah;
    }

    public void setA100cmsvwchah(Integer a100cmsvwchah) {
        this.a100cmsvwchah = a100cmsvwchah;
    }

    public Integer getA180cmsvwchah() {
        return a180cmsvwchah;
    }

    public void setA180cmsvwchah(Integer a180cmsvwchah) {
        this.a180cmsvwchah = a180cmsvwchah;
    }

    public Integer getAshfluxhah() {
        return ashfluxhah;
    }

    public void setAshfluxhah(Integer ashfluxhah) {
        this.ashfluxhah = ashfluxhah;
    }

    public Integer getAwspeedhah1l() {
        return awspeedhah1l;
    }

    public void setAwspeedhah1l(Integer awspeedhah1l) {
        this.awspeedhah1l = awspeedhah1l;
    }

    public Integer getExwspeedh1l() {
        return exwspeedh1l;
    }

    public void setExwspeedh1l(Integer exwspeedh1l) {
        this.exwspeedh1l = exwspeedh1l;
    }

    public Integer getMaxwspeedtimeh1l() {
        return maxwspeedtimeh1l;
    }

    public void setMaxwspeedtimeh1l(Integer maxwspeedtimeh1l) {
        this.maxwspeedtimeh1l = maxwspeedtimeh1l;
    }

    public Integer getMaxwspeedh1l() {
        return maxwspeedh1l;
    }

    public void setMaxwspeedh1l(Integer maxwspeedh1l) {
        this.maxwspeedh1l = maxwspeedh1l;
    }

    public Integer getTimemaxwvh1l() {
        return timemaxwvh1l;
    }

    public void setTimemaxwvh1l(Integer timemaxwvh1l) {
        this.timemaxwvh1l = timemaxwvh1l;
    }

    public Integer getAwspeedhah2l() {
        return awspeedhah2l;
    }

    public void setAwspeedhah2l(Integer awspeedhah2l) {
        this.awspeedhah2l = awspeedhah2l;
    }

    public Integer getExwspeedh2l() {
        return exwspeedh2l;
    }

    public void setExwspeedh2l(Integer exwspeedh2l) {
        this.exwspeedh2l = exwspeedh2l;
    }

    public Integer getMaxwspeedtimeh2l() {
        return maxwspeedtimeh2l;
    }

    public void setMaxwspeedtimeh2l(Integer maxwspeedtimeh2l) {
        this.maxwspeedtimeh2l = maxwspeedtimeh2l;
    }

    public Integer getMaxwspeedh2l() {
        return maxwspeedh2l;
    }

    public void setMaxwspeedh2l(Integer maxwspeedh2l) {
        this.maxwspeedh2l = maxwspeedh2l;
    }

    public Integer getTimemaxwvh2l() {
        return timemaxwvh2l;
    }

    public void setTimemaxwvh2l(Integer timemaxwvh2l) {
        this.timemaxwvh2l = timemaxwvh2l;
    }

    public Integer getAwspeedhah10m() {
        return awspeedhah10m;
    }

    public void setAwspeedhah10m(Integer awspeedhah10m) {
        this.awspeedhah10m = awspeedhah10m;
    }

    public Integer getAvshwv10mhah() {
        return avshwv10mhah;
    }

    public void setAvshwv10mhah(Integer avshwv10mhah) {
        this.avshwv10mhah = avshwv10mhah;
    }

    public Integer getAvchwdir10mhah() {
        return avchwdir10mhah;
    }

    public void setAvchwdir10mhah(Integer avchwdir10mhah) {
        this.avchwdir10mhah = avchwdir10mhah;
    }

    public String getSdwdirection() {
        return sdwdirection;
    }

    public void setSdwdirection(String sdwdirection) {
        this.sdwdirection = sdwdirection;
    }

    public Integer getExwspeedh10m() {
        return exwspeedh10m;
    }

    public void setExwspeedh10m(Integer exwspeedh10m) {
        this.exwspeedh10m = exwspeedh10m;
    }

    public Integer getMaxwdirwspeed11mh() {
        return maxwdirwspeed11mh;
    }

    public void setMaxwdirwspeed11mh(Integer maxwdirwspeed11mh) {
        this.maxwdirwspeed11mh = maxwdirwspeed11mh;
    }

    public Integer getMaxwspeedt10mh() {
        return maxwspeedt10mh;
    }

    public void setMaxwspeedt10mh(Integer maxwspeedt10mh) {
        this.maxwspeedt10mh = maxwspeedt10mh;
    }

    public Integer getMaxwspeedh10m() {
        return maxwspeedh10m;
    }

    public void setMaxwspeedh10m(Integer maxwspeedh10m) {
        this.maxwspeedh10m = maxwspeedh10m;
    }

    public Integer getMaxwdirwspeed12mh() {
        return maxwdirwspeed12mh;
    }

    public void setMaxwdirwspeed12mh(Integer maxwdirwspeed12mh) {
        this.maxwdirwspeed12mh = maxwdirwspeed12mh;
    }

    public Integer getMaxwspeedt11mh() {
        return maxwspeedt11mh;
    }

    public void setMaxwspeedt11mh(Integer maxwspeedt11mh) {
        this.maxwspeedt11mh = maxwspeedt11mh;
    }

    public Integer getAwspeedhah4l() {
        return awspeedhah4l;
    }

    public void setAwspeedhah4l(Integer awspeedhah4l) {
        this.awspeedhah4l = awspeedhah4l;
    }

    public Integer getExwspeedh4l() {
        return exwspeedh4l;
    }

    public void setExwspeedh4l(Integer exwspeedh4l) {
        this.exwspeedh4l = exwspeedh4l;
    }

    public Integer getMaxwspeedtimeh4l() {
        return maxwspeedtimeh4l;
    }

    public void setMaxwspeedtimeh4l(Integer maxwspeedtimeh4l) {
        this.maxwspeedtimeh4l = maxwspeedtimeh4l;
    }

    public Integer getMaxwspeedh4l() {
        return maxwspeedh4l;
    }

    public void setMaxwspeedh4l(Integer maxwspeedh4l) {
        this.maxwspeedh4l = maxwspeedh4l;
    }

    public Integer getTimemaxwvh4l() {
        return timemaxwvh4l;
    }

    public void setTimemaxwvh4l(Integer timemaxwvh4l) {
        this.timemaxwvh4l = timemaxwvh4l;
    }

    public Integer getAwspeedhah5l() {
        return awspeedhah5l;
    }

    public void setAwspeedhah5l(Integer awspeedhah5l) {
        this.awspeedhah5l = awspeedhah5l;
    }

    public Integer getExwspeedh5l() {
        return exwspeedh5l;
    }

    public void setExwspeedh5l(Integer exwspeedh5l) {
        this.exwspeedh5l = exwspeedh5l;
    }

    public Integer getMaxwspeedtimeh5l() {
        return maxwspeedtimeh5l;
    }

    public void setMaxwspeedtimeh5l(Integer maxwspeedtimeh5l) {
        this.maxwspeedtimeh5l = maxwspeedtimeh5l;
    }

    public Integer getMaxwspeedh5l() {
        return maxwspeedh5l;
    }

    public void setMaxwspeedh5l(Integer maxwspeedh5l) {
        this.maxwspeedh5l = maxwspeedh5l;
    }

    public Integer getTimemaxwvh5l() {
        return timemaxwvh5l;
    }

    public void setTimemaxwvh5l(Integer timemaxwvh5l) {
        this.timemaxwvh5l = timemaxwvh5l;
    }

    public Integer getAtemphah1l() {
        return atemphah1l;
    }

    public void setAtemphah1l(Integer atemphah1l) {
        this.atemphah1l = atemphah1l;
    }

    public Integer getMaxtemph1l() {
        return maxtemph1l;
    }

    public void setMaxtemph1l(Integer maxtemph1l) {
        this.maxtemph1l = maxtemph1l;
    }

    public Integer getTmaxtempah1l() {
        return tmaxtempah1l;
    }

    public void setTmaxtempah1l(Integer tmaxtempah1l) {
        this.tmaxtempah1l = tmaxtempah1l;
    }

    public Integer getMintemph1l() {
        return mintemph1l;
    }

    public void setMintemph1l(Integer mintemph1l) {
        this.mintemph1l = mintemph1l;
    }

    public Integer getTmintempah1l() {
        return tmintempah1l;
    }

    public void setTmintempah1l(Integer tmintempah1l) {
        this.tmintempah1l = tmintempah1l;
    }

    public Integer getArhhah1l() {
        return arhhah1l;
    }

    public void setArhhah1l(Integer arhhah1l) {
        this.arhhah1l = arhhah1l;
    }

    public Integer getMinrhh1l() {
        return minrhh1l;
    }

    public void setMinrhh1l(Integer minrhh1l) {
        this.minrhh1l = minrhh1l;
    }

    public Integer getMinrhoth1l() {
        return minrhoth1l;
    }

    public void setMinrhoth1l(Integer minrhoth1l) {
        this.minrhoth1l = minrhoth1l;
    }

    public Integer getAwvphah1l() {
        return awvphah1l;
    }

    public void setAwvphah1l(Integer awvphah1l) {
        this.awvphah1l = awvphah1l;
    }

    public Integer getAtemphah2l() {
        return atemphah2l;
    }

    public void setAtemphah2l(Integer atemphah2l) {
        this.atemphah2l = atemphah2l;
    }

    public Integer getMaxtemph2l() {
        return maxtemph2l;
    }

    public void setMaxtemph2l(Integer maxtemph2l) {
        this.maxtemph2l = maxtemph2l;
    }

    public Integer getTmaxtempah2l() {
        return tmaxtempah2l;
    }

    public void setTmaxtempah2l(Integer tmaxtempah2l) {
        this.tmaxtempah2l = tmaxtempah2l;
    }

    public Integer getMintemph2l() {
        return mintemph2l;
    }

    public void setMintemph2l(Integer mintemph2l) {
        this.mintemph2l = mintemph2l;
    }

    public Integer getTmintempah2l() {
        return tmintempah2l;
    }

    public void setTmintempah2l(Integer tmintempah2l) {
        this.tmintempah2l = tmintempah2l;
    }

    public Integer getArhhah2l() {
        return arhhah2l;
    }

    public void setArhhah2l(Integer arhhah2l) {
        this.arhhah2l = arhhah2l;
    }

    public Integer getMinrhh2l() {
        return minrhh2l;
    }

    public void setMinrhh2l(Integer minrhh2l) {
        this.minrhh2l = minrhh2l;
    }

    public Integer getMinrhoth2l() {
        return minrhoth2l;
    }

    public void setMinrhoth2l(Integer minrhoth2l) {
        this.minrhoth2l = minrhoth2l;
    }

    public Integer getAwvphah2l() {
        return awvphah2l;
    }

    public void setAwvphah2l(Integer awvphah2l) {
        this.awvphah2l = awvphah2l;
    }

    public Integer getAtemphah10l() {
        return atemphah10l;
    }

    public void setAtemphah10l(Integer atemphah10l) {
        this.atemphah10l = atemphah10l;
    }

    public Integer getMaxtemph10l() {
        return maxtemph10l;
    }

    public void setMaxtemph10l(Integer maxtemph10l) {
        this.maxtemph10l = maxtemph10l;
    }

    public Integer getTmaxtempah10l() {
        return tmaxtempah10l;
    }

    public void setTmaxtempah10l(Integer tmaxtempah10l) {
        this.tmaxtempah10l = tmaxtempah10l;
    }

    public Integer getMintemph10l() {
        return mintemph10l;
    }

    public void setMintemph10l(Integer mintemph10l) {
        this.mintemph10l = mintemph10l;
    }

    public Integer getTmintempah10l() {
        return tmintempah10l;
    }

    public void setTmintempah10l(Integer tmintempah10l) {
        this.tmintempah10l = tmintempah10l;
    }

    public Integer getArhhah10l() {
        return arhhah10l;
    }

    public void setArhhah10l(Integer arhhah10l) {
        this.arhhah10l = arhhah10l;
    }

    public Integer getMinrhh10l() {
        return minrhh10l;
    }

    public void setMinrhh10l(Integer minrhh10l) {
        this.minrhh10l = minrhh10l;
    }

    public Integer getMinrhoth10l() {
        return minrhoth10l;
    }

    public void setMinrhoth10l(Integer minrhoth10l) {
        this.minrhoth10l = minrhoth10l;
    }

    public Integer getAwvphah10l() {
        return awvphah10l;
    }

    public void setAwvphah10l(Integer awvphah10l) {
        this.awvphah10l = awvphah10l;
    }

    public Integer getAtemphah4l() {
        return atemphah4l;
    }

    public void setAtemphah4l(Integer atemphah4l) {
        this.atemphah4l = atemphah4l;
    }

    public Integer getMaxtemph4l() {
        return maxtemph4l;
    }

    public void setMaxtemph4l(Integer maxtemph4l) {
        this.maxtemph4l = maxtemph4l;
    }

    public Integer getTmaxtempah4l() {
        return tmaxtempah4l;
    }

    public void setTmaxtempah4l(Integer tmaxtempah4l) {
        this.tmaxtempah4l = tmaxtempah4l;
    }

    public Integer getMintemph4l() {
        return mintemph4l;
    }

    public void setMintemph4l(Integer mintemph4l) {
        this.mintemph4l = mintemph4l;
    }

    public Integer getTmintempah4l() {
        return tmintempah4l;
    }

    public void setTmintempah4l(Integer tmintempah4l) {
        this.tmintempah4l = tmintempah4l;
    }

    public Integer getArhhah4l() {
        return arhhah4l;
    }

    public void setArhhah4l(Integer arhhah4l) {
        this.arhhah4l = arhhah4l;
    }

    public Integer getMinrhh4l() {
        return minrhh4l;
    }

    public void setMinrhh4l(Integer minrhh4l) {
        this.minrhh4l = minrhh4l;
    }

    public Integer getMinrhoth4l() {
        return minrhoth4l;
    }

    public void setMinrhoth4l(Integer minrhoth4l) {
        this.minrhoth4l = minrhoth4l;
    }

    public Integer getAwvphah4l() {
        return awvphah4l;
    }

    public void setAwvphah4l(Integer awvphah4l) {
        this.awvphah4l = awvphah4l;
    }

    public Integer getAtemphah5l() {
        return atemphah5l;
    }

    public void setAtemphah5l(Integer atemphah5l) {
        this.atemphah5l = atemphah5l;
    }

    public Integer getMaxtemph5l() {
        return maxtemph5l;
    }

    public void setMaxtemph5l(Integer maxtemph5l) {
        this.maxtemph5l = maxtemph5l;
    }

    public Integer getTmaxtempah5l() {
        return tmaxtempah5l;
    }

    public void setTmaxtempah5l(Integer tmaxtempah5l) {
        this.tmaxtempah5l = tmaxtempah5l;
    }

    public Integer getMintemph5l() {
        return mintemph5l;
    }

    public void setMintemph5l(Integer mintemph5l) {
        this.mintemph5l = mintemph5l;
    }

    public Integer getTmintempah5l() {
        return tmintempah5l;
    }

    public void setTmintempah5l(Integer tmintempah5l) {
        this.tmintempah5l = tmintempah5l;
    }

    public Integer getArhhah5l() {
        return arhhah5l;
    }

    public void setArhhah5l(Integer arhhah5l) {
        this.arhhah5l = arhhah5l;
    }

    public Integer getMinrhh5l() {
        return minrhh5l;
    }

    public void setMinrhh5l(Integer minrhh5l) {
        this.minrhh5l = minrhh5l;
    }

    public Integer getMinrhoth5l() {
        return minrhoth5l;
    }

    public void setMinrhoth5l(Integer minrhoth5l) {
        this.minrhoth5l = minrhoth5l;
    }

    public Integer getAwvphah5l() {
        return awvphah5l;
    }

    public void setAwvphah5l(Integer awvphah5l) {
        this.awvphah5l = awvphah5l;
    }

    public Integer getAlpressurehah() {
        return alpressurehah;
    }

    public void setAlpressurehah(Integer alpressurehah) {
        this.alpressurehah = alpressurehah;
    }

    public BigDecimal getV10301() {
        return v10301;
    }

    public void setV10301(BigDecimal v10301) {
        this.v10301 = v10301;
    }

    public Integer getV10301052() {
        return v10301052;
    }

    public void setV10301052(Integer v10301052) {
        this.v10301052 = v10301052;
    }

    public BigDecimal getV10302() {
        return v10302;
    }

    public void setV10302(BigDecimal v10302) {
        this.v10302 = v10302;
    }

    public Integer getV10302052() {
        return v10302052;
    }

    public void setV10302052(Integer v10302052) {
        this.v10302052 = v10302052;
    }

    public Integer getAprecipitationh() {
        return aprecipitationh;
    }

    public void setAprecipitationh(Integer aprecipitationh) {
        this.aprecipitationh = aprecipitationh;
    }

    public Integer getCumevah() {
        return cumevah;
    }

    public void setCumevah(Integer cumevah) {
        this.cumevah = cumevah;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}