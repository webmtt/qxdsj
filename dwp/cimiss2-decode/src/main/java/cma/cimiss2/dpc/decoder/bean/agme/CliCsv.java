package cma.cimiss2.dpc.decoder.bean.agme;

import java.util.Date;

public class CliCsv {

    private String dRecordId;//记录标识

    private String dDataId;//资料标识

    private Date dIymdhm;//入库时间

    private Date dRymdhm;//收到时间

    private Date dUpdateTime;//更新时间

    private String vBbb;//更正报标志

    private String v01301;//台站号

    private String v01300;//台站名

    private String ProvinceNM;//省名

    private String cropNM;//观测作物名称

    private Double v05001;//纬度

    private Double v06001;//经度

    private Double v07001;//测站海拔高度

    private Date dDatetime;//资料时间

    private Short v04001;//资料观测年

    private Short v04002;//资料观测月

    private Short v04003;//资料观测日

    private Short v04004;//资料观测时

    private Short v04005;//资料观测分

    private Double v10004;//本站气压

    private String v1200130;//30cm气温

    private String v1200160;//60cm气温

    private Double v12001150;//150cm气温

    private Double v12001300;//300cm气温

    private String v12001C;//冠层温度

    private Double v13019;//1小时降水量

    private Double v1129130;//30cm风速

    private Double v1129160;//60cm风速

    private Double v11291150;//150cm风速

    private Double v11291300;//300cm风速

    private Double v11291600;//600cm风速

    private Double v13003030;//30cm空气相对湿度

    private Double v13003060;//60cm空气相对湿度

    private Double v13003150;//150cm空气相对湿度

    private Double v13003300;//300cm空气相对湿度

    private Double v12030000;//0cm地温

    private Double v12030005;//5cm地温

    private Double v12030010;//10cm地温

    private Double v12030015;//15cm地温

    private Double v12030020;//20cm地温

    private Double v12030030;//30cm地温

    private Double v12030040;//40cm地温

    private Double v12030080;//80cm地温

    private Double v71105010;//10cm土壤体积含水率

    private Double v71105020;//20cm土壤体积含水率

    private Double v71105030;//30cm土壤体积含水率

    private Double v71105040;//40cm土壤体积含水率

    private Double v71105050;//50cm土壤体积含水率

    private Double v71102010;//10cm土壤相对湿度

    private Double v71102020;//20cm土壤相对湿度

    private Double v71102030;//30cm土壤相对湿度

    private Double v71102040;//40cm土壤相对湿度

    private Double v71102050;//50cm土壤相对湿度

    private Integer q71102020;//20cm土壤相对湿度质控码

    private Integer q13019;//1小时降水量质量标志

    private Integer q12030020;//20cm地温质量标志

    private Integer q71102050;//50cm土壤相对湿度质控码

    private Integer q71105030;//50cm土壤体积含水量质控码

    private Integer q12030010;//10cm地温质量标志

    private Integer q12030040;//40cm地温质量标志

    private Integer q12030000;//0cm地温质量标志

    private Integer q71105040;//40cm土壤体积含水量质控码

    private Integer q71102040;//40cm土壤相对湿度质控码

    private Integer q71105010;//10cm土壤体积含水量质控码

    private Integer q71105020;//20cm土壤体积含水量质控码

    private Integer q71102010;//10cm土壤相对湿度质控码

    private Integer q10004;//本站气压质量标志

    private Integer q12030005;//5cm地温质量标志

    private Integer q12030015;//15cm地温质量标志

    private Integer q12030080;//80cm地温质量标志

    private Integer q71102030;//30cm土壤相对湿度质控码

    private Integer q71105050;//50cm土壤体积含水量质控码

    private Integer q1200130;//30cm气温质量控制码

    private Integer q1200160;//300cm气温质量控制码

    private Integer q12001150;//150cm气温质量控制码

    private Integer q12001300;//300cm气温质量控制码

    private Integer q12001C;//冠层温度质量控制码

    private Integer q1129130;//30cm风速质量控制码

    private Integer q1129160;//60cm风速质量控制码

    private Integer q11291150;//150cm风速质量控制码

    private Integer q11291300;//300cm风速质量控制码

    private Integer q11291600;//600cm风速质量控制码

    private Integer q1300330;//30cm空气相对湿度质量控制码

    private Integer q1300360;//60cm空气相对湿度质量控制码

    private Integer q13003150;//150cm空气相对湿度质量控制码

    private Integer q13003300;//300cm空气相对湿度质量控制码

    private Integer q12030030;//30cm空气相对湿度质量控制码

    public String getCropNM() {
        return cropNM;
    }

    public void setCropNM(String cropNM) {
        this.cropNM = cropNM;
    }

    public String getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(String dRecordId) {
        this.dRecordId = dRecordId;
    }

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId;
    }

    public Date getdIymdhm() {
        return dIymdhm;
    }

    public void setdIymdhm(Date dIymdhm) {
        this.dIymdhm = dIymdhm;
    }

    public Date getdRymdhm() {
        return dRymdhm;
    }

    public void setdRymdhm(Date dRymdhm) {
        this.dRymdhm = dRymdhm;
    }

    public Date getdUpdateTime() {
        return dUpdateTime;
    }

    public void setdUpdateTime(Date dUpdateTime) {
        this.dUpdateTime = dUpdateTime;
    }

    public String getvBbb() {
        return vBbb;
    }

    public void setvBbb(String vBbb) {
        this.vBbb = vBbb;
    }

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301;
    }

    public String getV01300() {
        return v01300;
    }

    public void setV01300(String v01300) {
        this.v01300 = v01300;
    }

    public String getProvinceNM() {
        return ProvinceNM;
    }

    public void setProvinceNM(String provinceNM) {
        ProvinceNM = provinceNM;
    }

    public Double getV05001() {
        return v05001;
    }

    public void setV05001(Double v05001) {
        this.v05001 = v05001;
    }

    public Double getV06001() {
        return v06001;
    }

    public void setV06001(Double v06001) {
        this.v06001 = v06001;
    }

    public Double getV07001() {
        return v07001;
    }

    public void setV07001(Double v07001) {
        this.v07001 = v07001;
    }

    public Date getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(Date dDatetime) {
        this.dDatetime = dDatetime;
    }

    public Short getV04001() {
        return v04001;
    }

    public void setV04001(Short v04001) {
        this.v04001 = v04001;
    }

    public Short getV04002() {
        return v04002;
    }

    public void setV04002(Short v04002) {
        this.v04002 = v04002;
    }

    public Short getV04003() {
        return v04003;
    }

    public void setV04003(Short v04003) {
        this.v04003 = v04003;
    }

    public Short getV04004() {
        return v04004;
    }

    public void setV04004(Short v04004) {
        this.v04004 = v04004;
    }

    public Short getV04005() {
        return v04005;
    }

    public void setV04005(Short v04005) {
        this.v04005 = v04005;
    }

    public Double getV10004() {
        return v10004;
    }

    public void setV10004(Double v10004) {
        this.v10004 = v10004;
    }

    public String getV1200130() {
        return v1200130;
    }

    public void setV1200130(String v1200130) {
        this.v1200130 = v1200130;
    }

    public String getV1200160() {
        return v1200160;
    }

    public void setV1200160(String v1200160) {
        this.v1200160 = v1200160;
    }

    public Double getV12001150() {
        return v12001150;
    }

    public void setV12001150(Double v12001150) {
        this.v12001150 = v12001150;
    }

    public Double getV12001300() {
        return v12001300;
    }

    public void setV12001300(Double v12001300) {
        this.v12001300 = v12001300;
    }

    public String getV12001C() {
        return v12001C;
    }

    public void setV12001C(String v12001C) {
        this.v12001C = v12001C;
    }

    public Double getV13019() {
        return v13019;
    }

    public void setV13019(Double v13019) {
        this.v13019 = v13019;
    }

    public Double getV1129130() {
        return v1129130;
    }

    public void setV1129130(Double v1129130) {
        this.v1129130 = v1129130;
    }

    public Double getV1129160() {
        return v1129160;
    }

    public void setV1129160(Double v1129160) {
        this.v1129160 = v1129160;
    }

    public Double getV11291150() {
        return v11291150;
    }

    public void setV11291150(Double v11291150) {
        this.v11291150 = v11291150;
    }

    public Double getV11291300() {
        return v11291300;
    }

    public void setV11291300(Double v11291300) {
        this.v11291300 = v11291300;
    }

    public Double getV11291600() {
        return v11291600;
    }

    public void setV11291600(Double v11291600) {
        this.v11291600 = v11291600;
    }

    public Double getV13003030() {
        return v13003030;
    }

    public void setV13003030(Double v13003030) {
        this.v13003030 = v13003030;
    }

    public Double getV13003060() {
        return v13003060;
    }

    public void setV13003060(Double v13003060) {
        this.v13003060 = v13003060;
    }

    public Double getV13003150() {
        return v13003150;
    }

    public void setV13003150(Double v13003150) {
        this.v13003150 = v13003150;
    }

    public Double getV13003300() {
        return v13003300;
    }

    public void setV13003300(Double v13003300) {
        this.v13003300 = v13003300;
    }

    public Double getV12030000() {
        return v12030000;
    }

    public void setV12030000(Double v12030000) {
        this.v12030000 = v12030000;
    }

    public Double getV12030005() {
        return v12030005;
    }

    public void setV12030005(Double v12030005) {
        this.v12030005 = v12030005;
    }

    public Double getV12030010() {
        return v12030010;
    }

    public void setV12030010(Double v12030010) {
        this.v12030010 = v12030010;
    }

    public Double getV12030015() {
        return v12030015;
    }

    public void setV12030015(Double v12030015) {
        this.v12030015 = v12030015;
    }

    public Double getV12030020() {
        return v12030020;
    }

    public void setV12030020(Double v12030020) {
        this.v12030020 = v12030020;
    }

    public Double getV12030030() {
        return v12030030;
    }

    public void setV12030030(Double v12030030) {
        this.v12030030 = v12030030;
    }

    public Double getV12030040() {
        return v12030040;
    }

    public void setV12030040(Double v12030040) {
        this.v12030040 = v12030040;
    }

    public Double getV12030080() {
        return v12030080;
    }

    public void setV12030080(Double v12030080) {
        this.v12030080 = v12030080;
    }

    public Double getV71105010() {
        return v71105010;
    }

    public void setV71105010(Double v71105010) {
        this.v71105010 = v71105010;
    }

    public Double getV71105020() {
        return v71105020;
    }

    public void setV71105020(Double v71105020) {
        this.v71105020 = v71105020;
    }

    public Double getV71105030() {
        return v71105030;
    }

    public void setV71105030(Double v71105030) {
        this.v71105030 = v71105030;
    }

    public Double getV71105040() {
        return v71105040;
    }

    public void setV71105040(Double v71105040) {
        this.v71105040 = v71105040;
    }

    public Double getV71105050() {
        return v71105050;
    }

    public void setV71105050(Double v71105050) {
        this.v71105050 = v71105050;
    }

    public Double getV71102010() {
        return v71102010;
    }

    public void setV71102010(Double v71102010) {
        this.v71102010 = v71102010;
    }

    public Double getV71102020() {
        return v71102020;
    }

    public void setV71102020(Double v71102020) {
        this.v71102020 = v71102020;
    }

    public Double getV71102030() {
        return v71102030;
    }

    public void setV71102030(Double v71102030) {
        this.v71102030 = v71102030;
    }

    public Double getV71102040() {
        return v71102040;
    }

    public void setV71102040(Double v71102040) {
        this.v71102040 = v71102040;
    }

    public Double getV71102050() {
        return v71102050;
    }

    public void setV71102050(Double v71102050) {
        this.v71102050 = v71102050;
    }

    public Integer getQ71102020() {
        return q71102020;
    }

    public void setQ71102020(Integer q71102020) {
        this.q71102020 = q71102020;
    }

    public Integer getQ13019() {
        return q13019;
    }

    public void setQ13019(Integer q13019) {
        this.q13019 = q13019;
    }

    public Integer getQ12030020() {
        return q12030020;
    }

    public void setQ12030020(Integer q12030020) {
        this.q12030020 = q12030020;
    }

    public Integer getQ71102050() {
        return q71102050;
    }

    public void setQ71102050(Integer q71102050) {
        this.q71102050 = q71102050;
    }

    public Integer getQ71105030() {
        return q71105030;
    }

    public void setQ71105030(Integer q71105030) {
        this.q71105030 = q71105030;
    }

    public Integer getQ12030010() {
        return q12030010;
    }

    public void setQ12030010(Integer q12030010) {
        this.q12030010 = q12030010;
    }

    public Integer getQ12030040() {
        return q12030040;
    }

    public void setQ12030040(Integer q12030040) {
        this.q12030040 = q12030040;
    }

    public Integer getQ12030000() {
        return q12030000;
    }

    public void setQ12030000(Integer q12030000) {
        this.q12030000 = q12030000;
    }

    public Integer getQ71105040() {
        return q71105040;
    }

    public void setQ71105040(Integer q71105040) {
        this.q71105040 = q71105040;
    }

    public Integer getQ71102040() {
        return q71102040;
    }

    public void setQ71102040(Integer q71102040) {
        this.q71102040 = q71102040;
    }

    public Integer getQ71105010() {
        return q71105010;
    }

    public void setQ71105010(Integer q71105010) {
        this.q71105010 = q71105010;
    }

    public Integer getQ71105020() {
        return q71105020;
    }

    public void setQ71105020(Integer q71105020) {
        this.q71105020 = q71105020;
    }

    public Integer getQ71102010() {
        return q71102010;
    }

    public void setQ71102010(Integer q71102010) {
        this.q71102010 = q71102010;
    }

    public Integer getQ10004() {
        return q10004;
    }

    public void setQ10004(Integer q10004) {
        this.q10004 = q10004;
    }

    public Integer getQ12030005() {
        return q12030005;
    }

    public void setQ12030005(Integer q12030005) {
        this.q12030005 = q12030005;
    }

    public Integer getQ12030015() {
        return q12030015;
    }

    public void setQ12030015(Integer q12030015) {
        this.q12030015 = q12030015;
    }

    public Integer getQ12030080() {
        return q12030080;
    }

    public void setQ12030080(Integer q12030080) {
        this.q12030080 = q12030080;
    }

    public Integer getQ71102030() {
        return q71102030;
    }

    public void setQ71102030(Integer q71102030) {
        this.q71102030 = q71102030;
    }

    public Integer getQ71105050() {
        return q71105050;
    }

    public void setQ71105050(Integer q71105050) {
        this.q71105050 = q71105050;
    }

    public Integer getQ1200130() {
        return q1200130;
    }

    public void setQ1200130(Integer q1200130) {
        this.q1200130 = q1200130;
    }

    public Integer getQ1200160() {
        return q1200160;
    }

    public void setQ1200160(Integer q1200160) {
        this.q1200160 = q1200160;
    }

    public Integer getQ12001150() {
        return q12001150;
    }

    public void setQ12001150(Integer q12001150) {
        this.q12001150 = q12001150;
    }

    public Integer getQ12001300() {
        return q12001300;
    }

    public void setQ12001300(Integer q12001300) {
        this.q12001300 = q12001300;
    }

    public Integer getQ12001C() {
        return q12001C;
    }

    public void setQ12001C(Integer q12001C) {
        this.q12001C = q12001C;
    }

    public Integer getQ1129130() {
        return q1129130;
    }

    public void setQ1129130(Integer q1129130) {
        this.q1129130 = q1129130;
    }

    public Integer getQ1129160() {
        return q1129160;
    }

    public void setQ1129160(Integer q1129160) {
        this.q1129160 = q1129160;
    }

    public Integer getQ11291150() {
        return q11291150;
    }

    public void setQ11291150(Integer q11291150) {
        this.q11291150 = q11291150;
    }

    public Integer getQ11291300() {
        return q11291300;
    }

    public void setQ11291300(Integer q11291300) {
        this.q11291300 = q11291300;
    }

    public Integer getQ11291600() {
        return q11291600;
    }

    public void setQ11291600(Integer q11291600) {
        this.q11291600 = q11291600;
    }

    public Integer getQ1300330() {
        return q1300330;
    }

    public void setQ1300330(Integer q1300330) {
        this.q1300330 = q1300330;
    }

    public Integer getQ1300360() {
        return q1300360;
    }

    public void setQ1300360(Integer q1300360) {
        this.q1300360 = q1300360;
    }

    public Integer getQ13003150() {
        return q13003150;
    }

    public void setQ13003150(Integer q13003150) {
        this.q13003150 = q13003150;
    }

    public Integer getQ13003300() {
        return q13003300;
    }

    public void setQ13003300(Integer q13003300) {
        this.q13003300 = q13003300;
    }

    public Integer getQ12030030() {
        return q12030030;
    }

    public void setQ12030030(Integer q12030030) {
        this.q12030030 = q12030030;
    }
}
