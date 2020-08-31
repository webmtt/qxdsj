package cma.cimiss2.dpc.decoder.bean.radi;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class RadiDigChnMulTab {
    private String dRecordId;//记录标识

    private String countryname;//国家名称

    private String provincename;//省名

    private String cityname;//地市名

    private String cntyname;//区县名

    private String townname;//乡镇名

    private String dSourceId;//数据来源

    private String cname;//站名

    private String dDataId;//资料标识

    private String dIymdhm;//入库时间

    private String dRymdhm;//收到时间

    private String dUpdateTime;//更新时间

    private String dDatetime;//资料时间

    private String vBbb;//更正报标志

    private String v01301;//区站号/观测平台标识(字符)

    private String v01300;//区站号/观测平台标识(数字)

    private BigDecimal v05001;//纬度

    private BigDecimal v06001;//经度

    private BigDecimal v07001;//测站高度

    private Short v04001;//年

    private Short v04002;//月

    private Short v04003;//日

    private Short v04004;//时

    private Short v04005;//分

    private BigDecimal v14311;//总辐射辐照度

    private BigDecimal v14312;//净辐射辐照度

    private BigDecimal v14313;//直接辐射辐照度

    private BigDecimal v14314;//散射辐射辐照度

    private Integer v14315;//反射辐射辐照度

    private Integer v14316;//紫外辐射辐照度

    private BigDecimal v14320;//总辐射曝辐量

    private BigDecimal v1431105;//总辐射辐照度最大值

    private Integer v1402105052;//总辐射辐照度最大出现时间

    private BigDecimal v14308;//净辐射曝辐量

    private BigDecimal v1431205;//净辐射辐照度最大值

    private Integer v1431205052;//净辐射辐照度最大出现时间

    private Integer v1431206;//净辐射辐照度最小值

    private Integer v1431206052;//净辐射辐照度最小出现时间

    private BigDecimal v14322;//直接辐射曝辐量

    private Integer v1431305;//直接辐射辐照度最大值

    private Integer v1431305052;//直接辐射辐照度最大出现时间

    private BigDecimal v14309;//散射辐射曝辐量

    private BigDecimal v1431405;//散射辐射辐照度最大值

    private Integer v1431405052;//散射辐射辐照度最大出现时间

    private BigDecimal v14306;//反射辐射曝辐量

    private Integer v1431505;//反射辐射辐照度最大值

    private Integer v1431505052;//反射辐射辐照度最大出现时间

    private BigDecimal v14307;//紫外辐射曝辐量

    private Integer v1431605;//紫外辐射辐照度最大值

    private Integer v1431605052;//紫外辐射辐照度最大出现时间

    private BigDecimal v14032;//日照时数（直接辐射计算值）

    private BigDecimal v15483;//大气浑浊度

    private Short q14311;//总辐射辐照度质控码

    private Short q14312;//净全辐射辐照度质控码

    private Short q14313;//直射辐射辐照度质控码

    private Short q14314;//散射辐射辐照度质控码

    private Short q14315;//反射辐射辐照度质控码

    private Short q14316;//紫外辐射辐照度质控码

    private Short q14320;//总辐射曝辐量质控码

    private Short q1431105;//总辐射辐照度最大值质控码

    private Short q1402105052;//总辐射辐照度最大出现时间质控码

    private Short q14308;//净辐射曝辐量质控码

    private Short q1431205;//净辐射辐照度最大值质控码

    private Short q1431205052;//净辐射辐照度最大出现时间质控码

    private Short q1431206;//净辐射辐照度最小值质控码

    private Short q1431206052;//净辐射辐照度最小出现时间质控码

    private Short q14322;//直接辐射曝辐量质控码

    private Short q1431305;//直接辐射辐照度最大值质控码

    private Short q1431305052;//直接辐射辐照度最大出现时间质控码

    private Short q14309;//散射辐射曝辐量质控码

    private Short q1431405;//散射辐射辐照度最大值质控码

    private Short q1431405052;//散射辐射辐照度最大出现时间质控码

    private Short q14306;//反射辐射曝辐量质控码

    private Short q1431505;//反射辐射辐照度最大值质控码

    private Short q1431505052;//反射辐射辐照度最大出现时间质控码

    private Short q14307;//紫外辐射曝辐量质控码

    private Short q1431605;//紫外辐射辐照度最大值质控码

    private Short q1431605052;//紫外辐射辐照度最大出现时间质控码

    private Short q14032;//日总日照时数质控码

    private Short q15483;//大气浑浊度质控码

    public String getdRecordId() {
        return dRecordId;
    }

    public void setdRecordId(String dRecordId) {
        this.dRecordId = dRecordId == null ? null : dRecordId.trim();
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname == null ? null : countryname.trim();
    }

    public String getProvincename() {
        return provincename;
    }

    public void setProvincename(String provincename) {
        this.provincename = provincename == null ? null : provincename.trim();
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname == null ? null : cityname.trim();
    }

    public String getCntyname() {
        return cntyname;
    }

    public void setCntyname(String cntyname) {
        this.cntyname = cntyname == null ? null : cntyname.trim();
    }

    public String getTownname() {
        return townname;
    }

    public void setTownname(String townname) {
        this.townname = townname == null ? null : townname.trim();
    }

    public String getdSourceId() {
        return dSourceId;
    }

    public void setdSourceId(String dSourceId) {
        this.dSourceId = dSourceId == null ? null : dSourceId.trim();
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname == null ? null : cname.trim();
    }

    public String getdDataId() {
        return dDataId;
    }

    public void setdDataId(String dDataId) {
        this.dDataId = dDataId == null ? null : dDataId.trim();
    }

    public String getdIymdhm() {
        return dIymdhm;
    }

    public void setdIymdhm(String dIymdhm) {
        this.dIymdhm = dIymdhm;
    }

    public String getdRymdhm() {
        return dRymdhm;
    }

    public void setdRymdhm(String dRymdhm) {
        this.dRymdhm = dRymdhm;
    }

    public String getdUpdateTime() {
        return dUpdateTime;
    }

    public void setdUpdateTime(String dUpdateTime) {
        this.dUpdateTime = dUpdateTime;
    }

    public String getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(String dDatetime) {
        this.dDatetime = dDatetime;
    }

    public String getvBbb() {
        return vBbb;
    }

    public void setvBbb(String vBbb) {
        this.vBbb = vBbb == null ? null : vBbb.trim();
    }

    public String getV01301() {
        return v01301;
    }

    public void setV01301(String v01301) {
        this.v01301 = v01301 == null ? null : v01301.trim();
    }

    public String getV01300() {
        return v01300;
    }

    public void setV01300(String v01300) {
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

    public BigDecimal getV07001() {
        return v07001;
    }

    public void setV07001(BigDecimal v07001) {
        this.v07001 = v07001;
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

    public BigDecimal getV14311() {
        return v14311;
    }

    public void setV14311(BigDecimal v14311) {
        this.v14311 = v14311;
    }

    public BigDecimal getV14312() {
        return v14312;
    }

    public void setV14312(BigDecimal v14312) {
        this.v14312 = v14312;
    }

    public BigDecimal getV14313() {
        return v14313;
    }

    public void setV14313(BigDecimal v14313) {
        this.v14313 = v14313;
    }

    public BigDecimal getV14314() {
        return v14314;
    }

    public void setV14314(BigDecimal v14314) {
        this.v14314 = v14314;
    }

    public Integer getV14315() {
        return v14315;
    }

    public void setV14315(Integer v14315) {
        this.v14315 = v14315;
    }

    public Integer getV14316() {
        return v14316;
    }

    public void setV14316(Integer v14316) {
        this.v14316 = v14316;
    }

    public BigDecimal getV14320() {
        return v14320;
    }

    public void setV14320(BigDecimal v14320) {
        this.v14320 = v14320;
    }

    public BigDecimal getV1431105() {
        return v1431105;
    }

    public void setV1431105(BigDecimal v1431105) {
        this.v1431105 = v1431105;
    }

    public Integer getV1402105052() {
        return v1402105052;
    }

    public void setV1402105052(Integer v1402105052) {
        this.v1402105052 = v1402105052;
    }

    public BigDecimal getV14308() {
        return v14308;
    }

    public void setV14308(BigDecimal v14308) {
        this.v14308 = v14308;
    }

    public BigDecimal getV1431205() {
        return v1431205;
    }

    public void setV1431205(BigDecimal v1431205) {
        this.v1431205 = v1431205;
    }

    public Integer getV1431205052() {
        return v1431205052;
    }

    public void setV1431205052(Integer v1431205052) {
        this.v1431205052 = v1431205052;
    }

    public Integer getV1431206() {
        return v1431206;
    }

    public void setV1431206(Integer v1431206) {
        this.v1431206 = v1431206;
    }

    public Integer getV1431206052() {
        return v1431206052;
    }

    public void setV1431206052(Integer v1431206052) {
        this.v1431206052 = v1431206052;
    }

    public BigDecimal getV14322() {
        return v14322;
    }

    public void setV14322(BigDecimal v14322) {
        this.v14322 = v14322;
    }

    public Integer getV1431305() {
        return v1431305;
    }

    public void setV1431305(Integer v1431305) {
        this.v1431305 = v1431305;
    }

    public Integer getV1431305052() {
        return v1431305052;
    }

    public void setV1431305052(Integer v1431305052) {
        this.v1431305052 = v1431305052;
    }

    public BigDecimal getV14309() {
        return v14309;
    }

    public void setV14309(BigDecimal v14309) {
        this.v14309 = v14309;
    }

    public BigDecimal getV1431405() {
        return v1431405;
    }

    public void setV1431405(BigDecimal v1431405) {
        this.v1431405 = v1431405;
    }

    public Integer getV1431405052() {
        return v1431405052;
    }

    public void setV1431405052(Integer v1431405052) {
        this.v1431405052 = v1431405052;
    }

    public BigDecimal getV14306() {
        return v14306;
    }

    public void setV14306(BigDecimal v14306) {
        this.v14306 = v14306;
    }

    public Integer getV1431505() {
        return v1431505;
    }

    public void setV1431505(Integer v1431505) {
        this.v1431505 = v1431505;
    }

    public Integer getV1431505052() {
        return v1431505052;
    }

    public void setV1431505052(Integer v1431505052) {
        this.v1431505052 = v1431505052;
    }

    public BigDecimal getV14307() {
        return v14307;
    }

    public void setV14307(BigDecimal v14307) {
        this.v14307 = v14307;
    }

    public Integer getV1431605() {
        return v1431605;
    }

    public void setV1431605(Integer v1431605) {
        this.v1431605 = v1431605;
    }

    public Integer getV1431605052() {
        return v1431605052;
    }

    public void setV1431605052(Integer v1431605052) {
        this.v1431605052 = v1431605052;
    }

    public BigDecimal getV14032() {
        return v14032;
    }

    public void setV14032(BigDecimal v14032) {
        this.v14032 = v14032;
    }

    public BigDecimal getV15483() {
        return v15483;
    }

    public void setV15483(BigDecimal v15483) {
        this.v15483 = v15483;
    }

    public Short getQ14311() {
        return q14311;
    }

    public void setQ14311(Short q14311) {
        this.q14311 = q14311;
    }

    public Short getQ14312() {
        return q14312;
    }

    public void setQ14312(Short q14312) {
        this.q14312 = q14312;
    }

    public Short getQ14313() {
        return q14313;
    }

    public void setQ14313(Short q14313) {
        this.q14313 = q14313;
    }

    public Short getQ14314() {
        return q14314;
    }

    public void setQ14314(Short q14314) {
        this.q14314 = q14314;
    }

    public Short getQ14315() {
        return q14315;
    }

    public void setQ14315(Short q14315) {
        this.q14315 = q14315;
    }

    public Short getQ14316() {
        return q14316;
    }

    public void setQ14316(Short q14316) {
        this.q14316 = q14316;
    }

    public Short getQ14320() {
        return q14320;
    }

    public void setQ14320(Short q14320) {
        this.q14320 = q14320;
    }

    public Short getQ1431105() {
        return q1431105;
    }

    public void setQ1431105(Short q1431105) {
        this.q1431105 = q1431105;
    }

    public Short getQ1402105052() {
        return q1402105052;
    }

    public void setQ1402105052(Short q1402105052) {
        this.q1402105052 = q1402105052;
    }

    public Short getQ14308() {
        return q14308;
    }

    public void setQ14308(Short q14308) {
        this.q14308 = q14308;
    }

    public Short getQ1431205() {
        return q1431205;
    }

    public void setQ1431205(Short q1431205) {
        this.q1431205 = q1431205;
    }

    public Short getQ1431205052() {
        return q1431205052;
    }

    public void setQ1431205052(Short q1431205052) {
        this.q1431205052 = q1431205052;
    }

    public Short getQ1431206() {
        return q1431206;
    }

    public void setQ1431206(Short q1431206) {
        this.q1431206 = q1431206;
    }

    public Short getQ1431206052() {
        return q1431206052;
    }

    public void setQ1431206052(Short q1431206052) {
        this.q1431206052 = q1431206052;
    }

    public Short getQ14322() {
        return q14322;
    }

    public void setQ14322(Short q14322) {
        this.q14322 = q14322;
    }

    public Short getQ1431305() {
        return q1431305;
    }

    public void setQ1431305(Short q1431305) {
        this.q1431305 = q1431305;
    }

    public Short getQ1431305052() {
        return q1431305052;
    }

    public void setQ1431305052(Short q1431305052) {
        this.q1431305052 = q1431305052;
    }

    public Short getQ14309() {
        return q14309;
    }

    public void setQ14309(Short q14309) {
        this.q14309 = q14309;
    }

    public Short getQ1431405() {
        return q1431405;
    }

    public void setQ1431405(Short q1431405) {
        this.q1431405 = q1431405;
    }

    public Short getQ1431405052() {
        return q1431405052;
    }

    public void setQ1431405052(Short q1431405052) {
        this.q1431405052 = q1431405052;
    }

    public Short getQ14306() {
        return q14306;
    }

    public void setQ14306(Short q14306) {
        this.q14306 = q14306;
    }

    public Short getQ1431505() {
        return q1431505;
    }

    public void setQ1431505(Short q1431505) {
        this.q1431505 = q1431505;
    }

    public Short getQ1431505052() {
        return q1431505052;
    }

    public void setQ1431505052(Short q1431505052) {
        this.q1431505052 = q1431505052;
    }

    public Short getQ14307() {
        return q14307;
    }

    public void setQ14307(Short q14307) {
        this.q14307 = q14307;
    }

    public Short getQ1431605() {
        return q1431605;
    }

    public void setQ1431605(Short q1431605) {
        this.q1431605 = q1431605;
    }

    public Short getQ1431605052() {
        return q1431605052;
    }

    public void setQ1431605052(Short q1431605052) {
        this.q1431605052 = q1431605052;
    }

    public Short getQ14032() {
        return q14032;
    }

    public void setQ14032(Short q14032) {
        this.q14032 = q14032;
    }

    public Short getQ15483() {
        return q15483;
    }

    public void setQ15483(Short q15483) {
        this.q15483 = q15483;
    }
}