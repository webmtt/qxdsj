package cma.cimiss2.dpc.decoder.bean.radi;

import java.math.BigDecimal;

public class RadiChnMutMyerTab {
    private String dRecordId;//记录标识

    private String dDataId;

    private String v01301;//区站号

    private BigDecimal v05001;//纬度

    private BigDecimal v06001;//经度

    private BigDecimal v07001;//拔海高度

    private BigDecimal v14320;//总辐射曝辐量

    private BigDecimal v14308;//净全辐射曝辐量

    private BigDecimal v14309;//散射辐射曝辐量

    private BigDecimal v14302;//水平面直接辐射曝辐量

    private BigDecimal v14306;//反射辐射曝辐量

    private BigDecimal v143St;//垂直面直接辐射曝辐量

    private BigDecimal v14027;//反射比

    private Integer v143201;//总辐射最大辐照度

    private Integer v14320Stime;//总辐射最大辐照度出现时间

    private Integer v14320Etime;//总辐射最大辐照度出现时间

    private Integer v143081;//净全辐射最大辐照度

    private Integer v14308Stime;//净全辐射最大辐照度出现时间

    private Integer v14308Etime;//净全辐射最大辐照度出现时间

    private Integer v143St1;//垂直直接辐射最大辐照度

    private Integer v143StStime;//垂直直接辐射最大辐照度出现时间

    private Integer v143StEtim;//垂直直接辐射最大辐照度出现时间

    public String getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(String dRecordId) {
        this.dRecordId = dRecordId == null ? null : dRecordId.trim();
    }

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId;
    }

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301 == null ? null : v01301.trim();
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

    public BigDecimal getV07001() {
        return v07001;
    }

    public void setV07001(BigDecimal v07001) {
        this.v07001 = v07001;
    }

    public BigDecimal getV14320() {
        return v14320;
    }

    public void setV14320(BigDecimal v14320) {
        this.v14320 = v14320;
    }

    public BigDecimal getV14308() {
        return v14308;
    }

    public void setV14308(BigDecimal v14308) {
        this.v14308 = v14308;
    }

    public BigDecimal getV14309() {
        return v14309;
    }

    public void setV14309(BigDecimal v14309) {
        this.v14309 = v14309;
    }

    public BigDecimal getV14302() {
        return v14302;
    }

    public void setV14302(BigDecimal v14302) {
        this.v14302 = v14302;
    }

    public BigDecimal getV14306() {
        return v14306;
    }

    public void setV14306(BigDecimal v14306) {
        this.v14306 = v14306;
    }

    public BigDecimal getV143St() {
        return v143St;
    }

    public void setV143St(BigDecimal v143St) {
        this.v143St = v143St;
    }

    public BigDecimal getV14027() {
        return v14027;
    }

    public void setV14027(BigDecimal v14027) {
        this.v14027 = v14027;
    }

    public Integer getV143201() {
        return v143201;
    }

    public void setV143201(Integer v143201) {
        this.v143201 = v143201;
    }

    public Integer getV14320Stime() {
        return v14320Stime;
    }

    public void setV14320Stime(Integer v14320Stime) {
        this.v14320Stime = v14320Stime;
    }

    public Integer getV14320Etime() {
        return v14320Etime;
    }

    public void setV14320Etime(Integer v14320Etime) {
        this.v14320Etime = v14320Etime;
    }

    public Integer getV143081() {
        return v143081;
    }

    public void setV143081(Integer v143081) {
        this.v143081 = v143081;
    }

    public Integer getV14308Stime() {
        return v14308Stime;
    }

    public void setV14308Stime(Integer v14308Stime) {
        this.v14308Stime = v14308Stime;
    }

    public Integer getV14308Etime() {
        return v14308Etime;
    }

    public void setV14308Etime(Integer v14308Etime) {
        this.v14308Etime = v14308Etime;
    }

    public Integer getV143St1() {
        return v143St1;
    }

    public void setV143St1(Integer v143St1) {
        this.v143St1 = v143St1;
    }

    public Integer getV143StStime() {
        return v143StStime;
    }

    public void setV143StStime(Integer v143StStime) {
        this.v143StStime = v143StStime;
    }

    public Integer getV143StEtim() {
        return v143StEtim;
    }

    public void setV143StEtim(Integer v143StEtim) {
        this.v143StEtim = v143StEtim;
    }
}