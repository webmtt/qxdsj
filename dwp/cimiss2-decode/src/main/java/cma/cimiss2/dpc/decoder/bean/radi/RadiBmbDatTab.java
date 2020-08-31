package cma.cimiss2.dpc.decoder.bean.radi;

import java.math.BigDecimal;

public class RadiBmbDatTab {
    private String dRecordId;//记录标识

    private String dDataId;

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId;
    }

    private String v01301;//区站号

    private Short v04001;//年

    private Short v04002;//月

    private Short v04003;//日

    private BigDecimal v06001;//经度

    private BigDecimal v05001;//纬度

    private BigDecimal v07001;//观测场或平台拔海高度

    private Short sitelevel;//站点级别

    private Short v07032Water;//水平面光热总辐射表距地高度

    private Short v070323Lat;//纬度光热总辐射表距地高度

    private Short v070323LatN15;//纬度+15°光热总辐射表距地高度

    private Short v070323LatS15;//纬度-15°光热总辐射表距地高度

    private String v070323E;//东垂直面光热总辐射表距地高度

    private Integer v070323S;//南垂直面光热总辐射表距地高度

    private BigDecimal v070323W;//西垂直面光热总辐射表距地高度

    private BigDecimal v07032Diss;//水平面光热散射辐射表距地高度

    private BigDecimal v07032Biaxial;//双轴跟踪光热总辐射表距地高度

    private Integer v07032Uniaxial;//单轴跟踪光热总辐射表距地高度

    private Integer v07032Ref;//水平面光热反射辐射表距地高度

    private String v07032LatPhoto;//纬度光电总辐射表距地高度

    private String v07032WaterPhoto;//水平面光电反射辐射表距地高度

    private BigDecimal v07032RotatePhoto;//旋转架光电总辐射表距地高度

    private BigDecimal v07032BiaxialPhoto;//双轴跟踪光热直接辐射表距地高度

    private BigDecimal v070324;//紫外辐射表(UV)距地高度

    private BigDecimal v070326;//紫外辐射表(UVA)距地高度

    private BigDecimal v070327;//紫外辐射表(UVB)距地高度

    private BigDecimal v070325;//大气长波辐射表距地高度

    private BigDecimal v02183;//地面长波辐射表距地高度

    private BigDecimal v070328;//光合有效辐射表距地高度

    private Short heighHorizPolysilicon;//水平面多晶硅电池距地高度

    private BigDecimal heighHorizMonocry;//水平面单晶硅电池距地高度

    private BigDecimal heighHorizNon;//水平面非晶硅电池距地高度

    private BigDecimal heighLatPolysilicon;//纬度多晶硅电池距地高度

    private BigDecimal heighLatMonocry;//纬度单晶硅电池距地高度

    private BigDecimal heighLatNon;//纬度非晶硅电池距地高度

    private BigDecimal heighBiaxialPolysilicon;//双轴跟踪多晶硅电池距地高度

    private BigDecimal heighBiaxialMonocry;//双轴跟踪单晶硅电池距地高度

    private BigDecimal heighBiaxialNon;//双轴跟踪非晶硅电池距地高度

    private BigDecimal heighUniaxialNon1;//单轴多晶硅电池距地高度

    private BigDecimal heighUniaxialNon2;//单轴单晶硅电池距地高度

    private BigDecimal heighUniaxialNon3;//单轴非晶硅电池距地高度

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

    public BigDecimal getV06001() {
        return v06001;
    }

    public void setV06001(BigDecimal v06001) {
        this.v06001 = v06001;
    }

    public BigDecimal getV05001() {
        return v05001;
    }

    public void setV05001(BigDecimal v05001) {
        this.v05001 = v05001;
    }

    public BigDecimal getV07001() {
        return v07001;
    }

    public void setV07001(BigDecimal v07001) {
        this.v07001 = v07001;
    }

    public Short getSitelevel() {
        return sitelevel;
    }

    public void setSitelevel(Short sitelevel) {
        this.sitelevel = sitelevel;
    }

    public Short getV07032Water() {
        return v07032Water;
    }

    public void setV07032Water(Short v07032Water) {
        this.v07032Water = v07032Water;
    }

    public Short getV070323Lat() {
        return v070323Lat;
    }

    public void setV070323Lat(Short v070323Lat) {
        this.v070323Lat = v070323Lat;
    }

    public Short getV070323LatN15() {
        return v070323LatN15;
    }

    public void setV070323LatN15(Short v070323LatN15) {
        this.v070323LatN15 = v070323LatN15;
    }

    public Short getV070323LatS15() {
        return v070323LatS15;
    }

    public void setV070323LatS15(Short v070323LatS15) {
        this.v070323LatS15 = v070323LatS15;
    }

    public String getV070323E() {
        return v070323E;
    }

    public void setV070323E(String v070323E) {
        this.v070323E = v070323E == null ? null : v070323E.trim();
    }

    public Integer getV070323S() {
        return v070323S;
    }

    public void setV070323S(Integer v070323S) {
        this.v070323S = v070323S;
    }

    public BigDecimal getV070323W() {
        return v070323W;
    }

    public void setV070323W(BigDecimal v070323W) {
        this.v070323W = v070323W;
    }

    public BigDecimal getV07032Diss() {
        return v07032Diss;
    }

    public void setV07032Diss(BigDecimal v07032Diss) {
        this.v07032Diss = v07032Diss;
    }

    public BigDecimal getV07032Biaxial() {
        return v07032Biaxial;
    }

    public void setV07032Biaxial(BigDecimal v07032Biaxial) {
        this.v07032Biaxial = v07032Biaxial;
    }

    public Integer getV07032Uniaxial() {
        return v07032Uniaxial;
    }

    public void setV07032Uniaxial(Integer v07032Uniaxial) {
        this.v07032Uniaxial = v07032Uniaxial;
    }

    public Integer getV07032Ref() {
        return v07032Ref;
    }

    public void setV07032Ref(Integer v07032Ref) {
        this.v07032Ref = v07032Ref;
    }

    public String getV07032LatPhoto() {
        return v07032LatPhoto;
    }

    public void setV07032LatPhoto(String v07032LatPhoto) {
        this.v07032LatPhoto = v07032LatPhoto == null ? null : v07032LatPhoto.trim();
    }

    public String getV07032WaterPhoto() {
        return v07032WaterPhoto;
    }

    public void setV07032WaterPhoto(String v07032WaterPhoto) {
        this.v07032WaterPhoto = v07032WaterPhoto == null ? null : v07032WaterPhoto.trim();
    }

    public BigDecimal getV07032RotatePhoto() {
        return v07032RotatePhoto;
    }

    public void setV07032RotatePhoto(BigDecimal v07032RotatePhoto) {
        this.v07032RotatePhoto = v07032RotatePhoto;
    }

    public BigDecimal getV07032BiaxialPhoto() {
        return v07032BiaxialPhoto;
    }

    public void setV07032BiaxialPhoto(BigDecimal v07032BiaxialPhoto) {
        this.v07032BiaxialPhoto = v07032BiaxialPhoto;
    }

    public BigDecimal getV070324() {
        return v070324;
    }

    public void setV070324(BigDecimal v070324) {
        this.v070324 = v070324;
    }

    public BigDecimal getV070326() {
        return v070326;
    }

    public void setV070326(BigDecimal v070326) {
        this.v070326 = v070326;
    }

    public BigDecimal getV070327() {
        return v070327;
    }

    public void setV070327(BigDecimal v070327) {
        this.v070327 = v070327;
    }

    public BigDecimal getV070325() {
        return v070325;
    }

    public void setV070325(BigDecimal v070325) {
        this.v070325 = v070325;
    }

    public BigDecimal getV02183() {
        return v02183;
    }

    public void setV02183(BigDecimal v02183) {
        this.v02183 = v02183;
    }

    public BigDecimal getV070328() {
        return v070328;
    }

    public void setV070328(BigDecimal v070328) {
        this.v070328 = v070328;
    }

    public Short getHeighHorizPolysilicon() {
        return heighHorizPolysilicon;
    }

    public void setHeighHorizPolysilicon(Short heighHorizPolysilicon) {
        this.heighHorizPolysilicon = heighHorizPolysilicon;
    }

    public BigDecimal getHeighHorizMonocry() {
        return heighHorizMonocry;
    }

    public void setHeighHorizMonocry(BigDecimal heighHorizMonocry) {
        this.heighHorizMonocry = heighHorizMonocry;
    }

    public BigDecimal getHeighHorizNon() {
        return heighHorizNon;
    }

    public void setHeighHorizNon(BigDecimal heighHorizNon) {
        this.heighHorizNon = heighHorizNon;
    }

    public BigDecimal getHeighLatPolysilicon() {
        return heighLatPolysilicon;
    }

    public void setHeighLatPolysilicon(BigDecimal heighLatPolysilicon) {
        this.heighLatPolysilicon = heighLatPolysilicon;
    }

    public BigDecimal getHeighLatMonocry() {
        return heighLatMonocry;
    }

    public void setHeighLatMonocry(BigDecimal heighLatMonocry) {
        this.heighLatMonocry = heighLatMonocry;
    }

    public BigDecimal getHeighLatNon() {
        return heighLatNon;
    }

    public void setHeighLatNon(BigDecimal heighLatNon) {
        this.heighLatNon = heighLatNon;
    }

    public BigDecimal getHeighBiaxialPolysilicon() {
        return heighBiaxialPolysilicon;
    }

    public void setHeighBiaxialPolysilicon(BigDecimal heighBiaxialPolysilicon) {
        this.heighBiaxialPolysilicon = heighBiaxialPolysilicon;
    }

    public BigDecimal getHeighBiaxialMonocry() {
        return heighBiaxialMonocry;
    }

    public void setHeighBiaxialMonocry(BigDecimal heighBiaxialMonocry) {
        this.heighBiaxialMonocry = heighBiaxialMonocry;
    }

    public BigDecimal getHeighBiaxialNon() {
        return heighBiaxialNon;
    }

    public void setHeighBiaxialNon(BigDecimal heighBiaxialNon) {
        this.heighBiaxialNon = heighBiaxialNon;
    }

    public BigDecimal getHeighUniaxialNon1() {
        return heighUniaxialNon1;
    }

    public void setHeighUniaxialNon1(BigDecimal heighUniaxialNon1) {
        this.heighUniaxialNon1 = heighUniaxialNon1;
    }

    public BigDecimal getHeighUniaxialNon2() {
        return heighUniaxialNon2;
    }

    public void setHeighUniaxialNon2(BigDecimal heighUniaxialNon2) {
        this.heighUniaxialNon2 = heighUniaxialNon2;
    }

    public BigDecimal getHeighUniaxialNon3() {
        return heighUniaxialNon3;
    }

    public void setHeighUniaxialNon3(BigDecimal heighUniaxialNon3) {
        this.heighUniaxialNon3 = heighUniaxialNon3;
    }
}