package cma.cimiss2.dpc.decoder.bean.radi;

import java.math.BigDecimal;

public class SurfXiliWindenerTab {
    private String dRecordId;//记录标识

    private String v01301;//区站号

    private Short v04001;//年

    private Short v04002;//月

    private Short v04003;//日

    private Short v04004;//时

    private BigDecimal v06001;//铁塔所在位置经度

    private BigDecimal v05001;//铁塔所在位置纬度

    private BigDecimal v07001;//梯度塔所处地（湖、海）面拔海高度

    private Short theins50;//50米超声风温仪安装角度

    private Short theins70;//70米超声风温仪安装角度

    private Short theins100;//100米超声风温仪安装角度

    private String v02320;//采集器型号

    private BigDecimal horiwindspeed50x;//50米高度水平风速（x轴）

    private BigDecimal horiwindspeed50y;//50米高度水平风速（y轴）

    private BigDecimal horiwindspeed50z;//50米高度垂向风速（z轴）

    private String ultvirtem50;//50米高度超声虚温

    private Integer diavalue50;//50米高度传感器诊断值

    private String horiwindspeed70x;//70米高度水平风速（x轴）

    private String horiwindspeed70y;//70米高度水平风速（y轴）

    private String horiwindspeed70z;//70米高度垂向风速（z轴）

    private BigDecimal ultvirtem70;//70米高度超声虚温

    private BigDecimal diavalue70;//70米高度传感器诊断值

    private BigDecimal horiwindspeed100x;//100米高度水平风速（x轴）

    private BigDecimal horiwindspeed100y;//100米高度水平风速（y轴）

    private BigDecimal horiwindspeed100z;//100米高度垂向风速（z轴）

    private BigDecimal ultvirtem100;//100米高度超声虚温

    private BigDecimal diavalue100;//100米高度传感器诊断值

    private BigDecimal avehowinspe10mihywi50x;//50米高度超声风10分钟平均水平风速（x轴）

    private BigDecimal avehowinspe10mihywi50y;//50米高度超声风10分钟平均水平风速（y轴）

    private BigDecimal avehowinspe10mihywi50z;//50米高度超声风10分钟平均垂直风速（z轴）

    private BigDecimal avehosywispe10mihywi50;//50米高度超声风10分钟平均水平合成风速

    private BigDecimal miavehovesywispehywi50;//50米高度超声风10分钟平均水平矢量合成风速

    private BigDecimal miavehovewidirsyhywi50;//50米高度超声风10分钟平均水平矢量合成风向

    private BigDecimal stade10minmehosywidirhy50;//50米高度超声风10分钟平均水平合成风向标准差

    private BigDecimal vahowispeuxhigsuwi50;//50米高度超声风水平风速Ux的方差

    private BigDecimal vahowispeuyhigsuwi50;//50米高度超声风水平风速Uy的方差

    private BigDecimal vahowispeuzhigsuwi50;//50米高度超声风水平风速Uz的方差

    private BigDecimal ahwsp10mhwind70x;//70米高度超声风10分钟平均水平风速（x轴）

    private BigDecimal ahwsp10mhwind70y;//70米高度超声风10分钟平均水平风速（y轴）

    private BigDecimal ahwsp10mhwind70z;//70米高度超声风10分钟平均垂直风速（z轴）

    private BigDecimal avehosywispe10mihywi70;//70米高度超声风10分钟平均水平合成风速

    private BigDecimal miavehovewidirsyhywi70;//70米高度超声风10分钟平均水平矢量合成风速

    private BigDecimal miavehovewidirsyhywi71;//70米高度超声风10分钟平均水平矢量合成风向

    private BigDecimal stade10minmehosywidirhy70;//70米高度超声风10分钟平均水平合成风向标准差

    private BigDecimal vahowispeuxhigsuwi70;//70米高度超声风水平风速Ux的方差

    private BigDecimal vahowispeuyhigsuwi70;//70米高度超声风水平风速Uy的方差

    private BigDecimal vahowispeuzhigsuwi70;//70米高度超声风水平风速Uz的方差

    private BigDecimal ahwsp10mhwind100x;//100米高度超声风10分钟平均水平风速（x轴）

    private BigDecimal ahwsp10mhwind100y;//100米高度超声风10分钟平均水平风速（y轴）

    private BigDecimal ahwsp10mhwind100z;//100米高度超声风10分钟平均垂直风速（z轴）

    private BigDecimal avehosywispe10mihywi100;//100米高度超声风10分钟平均水平合成风速

    private BigDecimal miavehovewidirsyhywi100;//100米高度超声风10分钟平均水平矢量合成风速

    private BigDecimal miavehovewidirsyhywi101;//100米高度超声风10分钟平均水平矢量合成风向

    private BigDecimal stade10minmehosywidirhy100;//100米高度超声风10分钟平均水平合成风向标准差

    private BigDecimal vahowispeuxhigsuwi100;//100米高度超声风水平风速Ux的方差

    private BigDecimal vahowispeuyhigsuwi100;//100米高度超声风水平风速Uy的方差

    private BigDecimal vahowispeuzhigsuwi100;//100米高度超声风水平风速Uz的方差

    private BigDecimal awssensoriden50;//50米高度风速传感器标识

    private BigDecimal awdsensoriden50;//50米高度风向传感器标识

    private BigDecimal awssensoriden70;//70米高度风速传感器标识

    private BigDecimal awdsensoriden70;//70米高度风向传感器标识

    private BigDecimal awssensoriden100;//100米高度风速传感器标识

    private BigDecimal awdsensoriden100;//100米高度风向传感器标识

    private BigDecimal a1mawspeed50;//50米高度1分钟平均风速

    private BigDecimal a1mawdirection50;//50米高度1分钟平均风向

    private BigDecimal a10mawspeed50;//50米高度10分钟平均风速

    private BigDecimal a10mawdirection50;//50米高度10分钟平均风向

    private BigDecimal ahmaxwspeed50;//50米高度小时内最大风速

    private BigDecimal ahmaxwspeedwdir50;//50米高度小时内最大风速的风向

    private BigDecimal ahmaxwspeedt50;//50米高度小时内最大风速出现时间

    private BigDecimal hcfmiwspeed50;//50米高度当前整分钟时的瞬时风速

    private BigDecimal hcfmiwdir50;//50米高度当前整分钟时的瞬时风向

    private BigDecimal ahexwspeed50;//50米高度小时内极大风速

    private BigDecimal ehmaxwspeedwdir50;//50米高度小时内极大风速的风向

    private BigDecimal hhmaxwspeedt50;//50米高度小时内极大风速出现时间

    private BigDecimal a1mawspeed70;//70米高度1分钟平均风速

    private BigDecimal a1mawdirection70;//70米高度1分钟平均风向

    private BigDecimal a10mawspeed70;//70米高度10分钟平均风速

    private BigDecimal a10mawdirection70;//70米高度10分钟平均风向

    private BigDecimal ahmaxwspeed70;//70米高度小时内最大风速

    private BigDecimal ahmaxwspeedwdir70;//70米高度小时内最大风速的风向

    private BigDecimal ahmaxwspeedt70;//70米高度小时内最大风速出现时间

    private BigDecimal hcfmiwspeed70;//70米高度当前整分钟时的瞬时风速

    private BigDecimal hcfmiwdir70;//70米高度当前整分钟时的瞬时风向

    private BigDecimal ahexwspeed70;//70米高度小时内极大风速

    private BigDecimal ehmaxwspeedwdir70;//70米高度小时内极大风速的风向

    private BigDecimal hhmaxwspeedt70;//70米高度小时内极大风速出现时间

    private BigDecimal a1mawspeed100;//100米高度1分钟平均风速

    private BigDecimal a1mawdirection100;//100米高度1分钟平均风向

    private BigDecimal a10mawspeed100;//100米高度10分钟平均风速

    private BigDecimal a10mawdirection100;//100米高度10分钟平均风向

    private BigDecimal ahmaxwspeed100;//100米高度小时内最大风速

    private BigDecimal ahmaxwspeedwdir100;//100米高度小时内最大风速的风向

    private BigDecimal ahmaxwspeedt100;//100米高度小时内最大风速出现时间

    private BigDecimal hcfmiwspeed100;//100米高度当前整分钟时的瞬时风速

    private BigDecimal hcfmiwdir100;//100米高度当前整分钟时的瞬时风向

    private BigDecimal ahexwspeed100;//100米高度小时内极大风速

    private BigDecimal ehmaxwspeedwdir100;//100米高度小时内极大风速的风向

    private BigDecimal hhmaxwspeedt100;//100米高度小时内极大风速出现时间

    private BigDecimal iamawspeed50;//50米高度整点分钟平均风速

    private BigDecimal iamawdirection50;//50米高度整点分钟平均风向

    private BigDecimal iamawspeed70;//70米高度整点分钟平均风速

    private BigDecimal iamawdirection70;//70米高度整点分钟平均风向

    private BigDecimal iamawspeed100;//100米高度整点分钟平均风速

    private BigDecimal iamawdirection100;//100米高度整点分钟平均风向

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

    public Short getV04004() {
        return v04004;
    }

    public void setV04004(Short v04004) {
        this.v04004 = v04004;
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

    public Short getTheins50() {
        return theins50;
    }

    public void setTheins50(Short theins50) {
        this.theins50 = theins50;
    }

    public Short getTheins70() {
        return theins70;
    }

    public void setTheins70(Short theins70) {
        this.theins70 = theins70;
    }

    public Short getTheins100() {
        return theins100;
    }

    public void setTheins100(Short theins100) {
        this.theins100 = theins100;
    }

    public String getV02320() {
        return v02320;
    }

    public void setV02320(String v02320) {
        this.v02320 = v02320 == null ? null : v02320.trim();
    }

    public BigDecimal getHoriwindspeed50x() {
        return horiwindspeed50x;
    }

    public void setHoriwindspeed50x(BigDecimal horiwindspeed50x) {
        this.horiwindspeed50x = horiwindspeed50x;
    }

    public BigDecimal getHoriwindspeed50y() {
        return horiwindspeed50y;
    }

    public void setHoriwindspeed50y(BigDecimal horiwindspeed50y) {
        this.horiwindspeed50y = horiwindspeed50y;
    }

    public BigDecimal getHoriwindspeed50z() {
        return horiwindspeed50z;
    }

    public void setHoriwindspeed50z(BigDecimal horiwindspeed50z) {
        this.horiwindspeed50z = horiwindspeed50z;
    }

    public String getUltvirtem50() {
        return ultvirtem50;
    }

    public void setUltvirtem50(String ultvirtem50) {
        this.ultvirtem50 = ultvirtem50;
    }

    public Integer getDiavalue50() {
        return diavalue50;
    }

    public void setDiavalue50(Integer diavalue50) {
        this.diavalue50 = diavalue50;
    }

    public String getHoriwindspeed70x() {
        return horiwindspeed70x;
    }

    public void setHoriwindspeed70x(String horiwindspeed70x) {
        this.horiwindspeed70x = horiwindspeed70x == null ? null : horiwindspeed70x.trim();
    }

    public String getHoriwindspeed70y() {
        return horiwindspeed70y;
    }

    public void setHoriwindspeed70y(String horiwindspeed70y) {
        this.horiwindspeed70y = horiwindspeed70y == null ? null : horiwindspeed70y.trim();
    }

    public String getHoriwindspeed70z() {
        return horiwindspeed70z;
    }

    public void setHoriwindspeed70z(String horiwindspeed70z) {
        this.horiwindspeed70z = horiwindspeed70z;
    }

    public BigDecimal getUltvirtem70() {
        return ultvirtem70;
    }

    public void setUltvirtem70(BigDecimal ultvirtem70) {
        this.ultvirtem70 = ultvirtem70;
    }

    public BigDecimal getDiavalue70() {
        return diavalue70;
    }

    public void setDiavalue70(BigDecimal diavalue70) {
        this.diavalue70 = diavalue70;
    }

    public BigDecimal getHoriwindspeed100x() {
        return horiwindspeed100x;
    }

    public void setHoriwindspeed100x(BigDecimal horiwindspeed100x) {
        this.horiwindspeed100x = horiwindspeed100x;
    }

    public BigDecimal getHoriwindspeed100y() {
        return horiwindspeed100y;
    }

    public void setHoriwindspeed100y(BigDecimal horiwindspeed100y) {
        this.horiwindspeed100y = horiwindspeed100y;
    }

    public BigDecimal getHoriwindspeed100z() {
        return horiwindspeed100z;
    }

    public void setHoriwindspeed100z(BigDecimal horiwindspeed100z) {
        this.horiwindspeed100z = horiwindspeed100z;
    }

    public BigDecimal getUltvirtem100() {
        return ultvirtem100;
    }

    public void setUltvirtem100(BigDecimal ultvirtem100) {
        this.ultvirtem100 = ultvirtem100;
    }

    public BigDecimal getDiavalue100() {
        return diavalue100;
    }

    public void setDiavalue100(BigDecimal diavalue100) {
        this.diavalue100 = diavalue100;
    }

    public BigDecimal getAvehowinspe10mihywi50x() {
        return avehowinspe10mihywi50x;
    }

    public void setAvehowinspe10mihywi50x(BigDecimal avehowinspe10mihywi50x) {
        this.avehowinspe10mihywi50x = avehowinspe10mihywi50x;
    }

    public BigDecimal getAvehowinspe10mihywi50y() {
        return avehowinspe10mihywi50y;
    }

    public void setAvehowinspe10mihywi50y(BigDecimal avehowinspe10mihywi50y) {
        this.avehowinspe10mihywi50y = avehowinspe10mihywi50y;
    }

    public BigDecimal getAvehowinspe10mihywi50z() {
        return avehowinspe10mihywi50z;
    }

    public void setAvehowinspe10mihywi50z(BigDecimal avehowinspe10mihywi50z) {
        this.avehowinspe10mihywi50z = avehowinspe10mihywi50z;
    }

    public BigDecimal getAvehosywispe10mihywi50() {
        return avehosywispe10mihywi50;
    }

    public void setAvehosywispe10mihywi50(BigDecimal avehosywispe10mihywi50) {
        this.avehosywispe10mihywi50 = avehosywispe10mihywi50;
    }

    public BigDecimal getMiavehovesywispehywi50() {
        return miavehovesywispehywi50;
    }

    public void setMiavehovesywispehywi50(BigDecimal miavehovesywispehywi50) {
        this.miavehovesywispehywi50 = miavehovesywispehywi50;
    }

    public BigDecimal getMiavehovewidirsyhywi50() {
        return miavehovewidirsyhywi50;
    }

    public void setMiavehovewidirsyhywi50(BigDecimal miavehovewidirsyhywi50) {
        this.miavehovewidirsyhywi50 = miavehovewidirsyhywi50;
    }

    public BigDecimal getStade10minmehosywidirhy50() {
        return stade10minmehosywidirhy50;
    }

    public void setStade10minmehosywidirhy50(BigDecimal stade10minmehosywidirhy50) {
        this.stade10minmehosywidirhy50 = stade10minmehosywidirhy50;
    }

    public BigDecimal getVahowispeuxhigsuwi50() {
        return vahowispeuxhigsuwi50;
    }

    public void setVahowispeuxhigsuwi50(BigDecimal vahowispeuxhigsuwi50) {
        this.vahowispeuxhigsuwi50 = vahowispeuxhigsuwi50;
    }

    public BigDecimal getVahowispeuyhigsuwi50() {
        return vahowispeuyhigsuwi50;
    }

    public void setVahowispeuyhigsuwi50(BigDecimal vahowispeuyhigsuwi50) {
        this.vahowispeuyhigsuwi50 = vahowispeuyhigsuwi50;
    }

    public BigDecimal getVahowispeuzhigsuwi50() {
        return vahowispeuzhigsuwi50;
    }

    public void setVahowispeuzhigsuwi50(BigDecimal vahowispeuzhigsuwi50) {
        this.vahowispeuzhigsuwi50 = vahowispeuzhigsuwi50;
    }

    public BigDecimal getAhwsp10mhwind70x() {
        return ahwsp10mhwind70x;
    }

    public void setAhwsp10mhwind70x(BigDecimal ahwsp10mhwind70x) {
        this.ahwsp10mhwind70x = ahwsp10mhwind70x;
    }

    public BigDecimal getAhwsp10mhwind70y() {
        return ahwsp10mhwind70y;
    }

    public void setAhwsp10mhwind70y(BigDecimal ahwsp10mhwind70y) {
        this.ahwsp10mhwind70y = ahwsp10mhwind70y;
    }

    public BigDecimal getAhwsp10mhwind70z() {
        return ahwsp10mhwind70z;
    }

    public void setAhwsp10mhwind70z(BigDecimal ahwsp10mhwind70z) {
        this.ahwsp10mhwind70z = ahwsp10mhwind70z;
    }

    public BigDecimal getAvehosywispe10mihywi70() {
        return avehosywispe10mihywi70;
    }

    public void setAvehosywispe10mihywi70(BigDecimal avehosywispe10mihywi70) {
        this.avehosywispe10mihywi70 = avehosywispe10mihywi70;
    }

    public BigDecimal getMiavehovewidirsyhywi70() {
        return miavehovewidirsyhywi70;
    }

    public void setMiavehovewidirsyhywi70(BigDecimal miavehovewidirsyhywi70) {
        this.miavehovewidirsyhywi70 = miavehovewidirsyhywi70;
    }

    public BigDecimal getMiavehovewidirsyhywi71() {
        return miavehovewidirsyhywi71;
    }

    public void setMiavehovewidirsyhywi71(BigDecimal miavehovewidirsyhywi71) {
        this.miavehovewidirsyhywi71 = miavehovewidirsyhywi71;
    }

    public BigDecimal getStade10minmehosywidirhy70() {
        return stade10minmehosywidirhy70;
    }

    public void setStade10minmehosywidirhy70(BigDecimal stade10minmehosywidirhy70) {
        this.stade10minmehosywidirhy70 = stade10minmehosywidirhy70;
    }

    public BigDecimal getVahowispeuxhigsuwi70() {
        return vahowispeuxhigsuwi70;
    }

    public void setVahowispeuxhigsuwi70(BigDecimal vahowispeuxhigsuwi70) {
        this.vahowispeuxhigsuwi70 = vahowispeuxhigsuwi70;
    }

    public BigDecimal getVahowispeuyhigsuwi70() {
        return vahowispeuyhigsuwi70;
    }

    public void setVahowispeuyhigsuwi70(BigDecimal vahowispeuyhigsuwi70) {
        this.vahowispeuyhigsuwi70 = vahowispeuyhigsuwi70;
    }

    public BigDecimal getVahowispeuzhigsuwi70() {
        return vahowispeuzhigsuwi70;
    }

    public void setVahowispeuzhigsuwi70(BigDecimal vahowispeuzhigsuwi70) {
        this.vahowispeuzhigsuwi70 = vahowispeuzhigsuwi70;
    }

    public BigDecimal getAhwsp10mhwind100x() {
        return ahwsp10mhwind100x;
    }

    public void setAhwsp10mhwind100x(BigDecimal ahwsp10mhwind100x) {
        this.ahwsp10mhwind100x = ahwsp10mhwind100x;
    }

    public BigDecimal getAhwsp10mhwind100y() {
        return ahwsp10mhwind100y;
    }

    public void setAhwsp10mhwind100y(BigDecimal ahwsp10mhwind100y) {
        this.ahwsp10mhwind100y = ahwsp10mhwind100y;
    }

    public BigDecimal getAhwsp10mhwind100z() {
        return ahwsp10mhwind100z;
    }

    public void setAhwsp10mhwind100z(BigDecimal ahwsp10mhwind100z) {
        this.ahwsp10mhwind100z = ahwsp10mhwind100z;
    }

    public BigDecimal getAvehosywispe10mihywi100() {
        return avehosywispe10mihywi100;
    }

    public void setAvehosywispe10mihywi100(BigDecimal avehosywispe10mihywi100) {
        this.avehosywispe10mihywi100 = avehosywispe10mihywi100;
    }

    public BigDecimal getMiavehovewidirsyhywi100() {
        return miavehovewidirsyhywi100;
    }

    public void setMiavehovewidirsyhywi100(BigDecimal miavehovewidirsyhywi100) {
        this.miavehovewidirsyhywi100 = miavehovewidirsyhywi100;
    }

    public BigDecimal getMiavehovewidirsyhywi101() {
        return miavehovewidirsyhywi101;
    }

    public void setMiavehovewidirsyhywi101(BigDecimal miavehovewidirsyhywi101) {
        this.miavehovewidirsyhywi101 = miavehovewidirsyhywi101;
    }

    public BigDecimal getStade10minmehosywidirhy100() {
        return stade10minmehosywidirhy100;
    }

    public void setStade10minmehosywidirhy100(BigDecimal stade10minmehosywidirhy100) {
        this.stade10minmehosywidirhy100 = stade10minmehosywidirhy100;
    }

    public BigDecimal getVahowispeuxhigsuwi100() {
        return vahowispeuxhigsuwi100;
    }

    public void setVahowispeuxhigsuwi100(BigDecimal vahowispeuxhigsuwi100) {
        this.vahowispeuxhigsuwi100 = vahowispeuxhigsuwi100;
    }

    public BigDecimal getVahowispeuyhigsuwi100() {
        return vahowispeuyhigsuwi100;
    }

    public void setVahowispeuyhigsuwi100(BigDecimal vahowispeuyhigsuwi100) {
        this.vahowispeuyhigsuwi100 = vahowispeuyhigsuwi100;
    }

    public BigDecimal getVahowispeuzhigsuwi100() {
        return vahowispeuzhigsuwi100;
    }

    public void setVahowispeuzhigsuwi100(BigDecimal vahowispeuzhigsuwi100) {
        this.vahowispeuzhigsuwi100 = vahowispeuzhigsuwi100;
    }

    public BigDecimal getAwssensoriden50() {
        return awssensoriden50;
    }

    public void setAwssensoriden50(BigDecimal awssensoriden50) {
        this.awssensoriden50 = awssensoriden50;
    }

    public BigDecimal getAwdsensoriden50() {
        return awdsensoriden50;
    }

    public void setAwdsensoriden50(BigDecimal awdsensoriden50) {
        this.awdsensoriden50 = awdsensoriden50;
    }

    public BigDecimal getAwssensoriden70() {
        return awssensoriden70;
    }

    public void setAwssensoriden70(BigDecimal awssensoriden70) {
        this.awssensoriden70 = awssensoriden70;
    }

    public BigDecimal getAwdsensoriden70() {
        return awdsensoriden70;
    }

    public void setAwdsensoriden70(BigDecimal awdsensoriden70) {
        this.awdsensoriden70 = awdsensoriden70;
    }

    public BigDecimal getAwssensoriden100() {
        return awssensoriden100;
    }

    public void setAwssensoriden100(BigDecimal awssensoriden100) {
        this.awssensoriden100 = awssensoriden100;
    }

    public BigDecimal getAwdsensoriden100() {
        return awdsensoriden100;
    }

    public void setAwdsensoriden100(BigDecimal awdsensoriden100) {
        this.awdsensoriden100 = awdsensoriden100;
    }

    public BigDecimal getA1mawspeed50() {
        return a1mawspeed50;
    }

    public void setA1mawspeed50(BigDecimal a1mawspeed50) {
        this.a1mawspeed50 = a1mawspeed50;
    }

    public BigDecimal getA1mawdirection50() {
        return a1mawdirection50;
    }

    public void setA1mawdirection50(BigDecimal a1mawdirection50) {
        this.a1mawdirection50 = a1mawdirection50;
    }

    public BigDecimal getA10mawspeed50() {
        return a10mawspeed50;
    }

    public void setA10mawspeed50(BigDecimal a10mawspeed50) {
        this.a10mawspeed50 = a10mawspeed50;
    }

    public BigDecimal getA10mawdirection50() {
        return a10mawdirection50;
    }

    public void setA10mawdirection50(BigDecimal a10mawdirection50) {
        this.a10mawdirection50 = a10mawdirection50;
    }

    public BigDecimal getAhmaxwspeed50() {
        return ahmaxwspeed50;
    }

    public void setAhmaxwspeed50(BigDecimal ahmaxwspeed50) {
        this.ahmaxwspeed50 = ahmaxwspeed50;
    }

    public BigDecimal getAhmaxwspeedwdir50() {
        return ahmaxwspeedwdir50;
    }

    public void setAhmaxwspeedwdir50(BigDecimal ahmaxwspeedwdir50) {
        this.ahmaxwspeedwdir50 = ahmaxwspeedwdir50;
    }

    public BigDecimal getAhmaxwspeedt50() {
        return ahmaxwspeedt50;
    }

    public void setAhmaxwspeedt50(BigDecimal ahmaxwspeedt50) {
        this.ahmaxwspeedt50 = ahmaxwspeedt50;
    }

    public BigDecimal getHcfmiwspeed50() {
        return hcfmiwspeed50;
    }

    public void setHcfmiwspeed50(BigDecimal hcfmiwspeed50) {
        this.hcfmiwspeed50 = hcfmiwspeed50;
    }

    public BigDecimal getHcfmiwdir50() {
        return hcfmiwdir50;
    }

    public void setHcfmiwdir50(BigDecimal hcfmiwdir50) {
        this.hcfmiwdir50 = hcfmiwdir50;
    }

    public BigDecimal getAhexwspeed50() {
        return ahexwspeed50;
    }

    public void setAhexwspeed50(BigDecimal ahexwspeed50) {
        this.ahexwspeed50 = ahexwspeed50;
    }

    public BigDecimal getEhmaxwspeedwdir50() {
        return ehmaxwspeedwdir50;
    }

    public void setEhmaxwspeedwdir50(BigDecimal ehmaxwspeedwdir50) {
        this.ehmaxwspeedwdir50 = ehmaxwspeedwdir50;
    }

    public BigDecimal getHhmaxwspeedt50() {
        return hhmaxwspeedt50;
    }

    public void setHhmaxwspeedt50(BigDecimal hhmaxwspeedt50) {
        this.hhmaxwspeedt50 = hhmaxwspeedt50;
    }

    public BigDecimal getA1mawspeed70() {
        return a1mawspeed70;
    }

    public void setA1mawspeed70(BigDecimal a1mawspeed70) {
        this.a1mawspeed70 = a1mawspeed70;
    }

    public BigDecimal getA1mawdirection70() {
        return a1mawdirection70;
    }

    public void setA1mawdirection70(BigDecimal a1mawdirection70) {
        this.a1mawdirection70 = a1mawdirection70;
    }

    public BigDecimal getA10mawspeed70() {
        return a10mawspeed70;
    }

    public void setA10mawspeed70(BigDecimal a10mawspeed70) {
        this.a10mawspeed70 = a10mawspeed70;
    }

    public BigDecimal getA10mawdirection70() {
        return a10mawdirection70;
    }

    public void setA10mawdirection70(BigDecimal a10mawdirection70) {
        this.a10mawdirection70 = a10mawdirection70;
    }

    public BigDecimal getAhmaxwspeed70() {
        return ahmaxwspeed70;
    }

    public void setAhmaxwspeed70(BigDecimal ahmaxwspeed70) {
        this.ahmaxwspeed70 = ahmaxwspeed70;
    }

    public BigDecimal getAhmaxwspeedwdir70() {
        return ahmaxwspeedwdir70;
    }

    public void setAhmaxwspeedwdir70(BigDecimal ahmaxwspeedwdir70) {
        this.ahmaxwspeedwdir70 = ahmaxwspeedwdir70;
    }

    public BigDecimal getAhmaxwspeedt70() {
        return ahmaxwspeedt70;
    }

    public void setAhmaxwspeedt70(BigDecimal ahmaxwspeedt70) {
        this.ahmaxwspeedt70 = ahmaxwspeedt70;
    }

    public BigDecimal getHcfmiwspeed70() {
        return hcfmiwspeed70;
    }

    public void setHcfmiwspeed70(BigDecimal hcfmiwspeed70) {
        this.hcfmiwspeed70 = hcfmiwspeed70;
    }

    public BigDecimal getHcfmiwdir70() {
        return hcfmiwdir70;
    }

    public void setHcfmiwdir70(BigDecimal hcfmiwdir70) {
        this.hcfmiwdir70 = hcfmiwdir70;
    }

    public BigDecimal getAhexwspeed70() {
        return ahexwspeed70;
    }

    public void setAhexwspeed70(BigDecimal ahexwspeed70) {
        this.ahexwspeed70 = ahexwspeed70;
    }

    public BigDecimal getEhmaxwspeedwdir70() {
        return ehmaxwspeedwdir70;
    }

    public void setEhmaxwspeedwdir70(BigDecimal ehmaxwspeedwdir70) {
        this.ehmaxwspeedwdir70 = ehmaxwspeedwdir70;
    }

    public BigDecimal getHhmaxwspeedt70() {
        return hhmaxwspeedt70;
    }

    public void setHhmaxwspeedt70(BigDecimal hhmaxwspeedt70) {
        this.hhmaxwspeedt70 = hhmaxwspeedt70;
    }

    public BigDecimal getA1mawspeed100() {
        return a1mawspeed100;
    }

    public void setA1mawspeed100(BigDecimal a1mawspeed100) {
        this.a1mawspeed100 = a1mawspeed100;
    }

    public BigDecimal getA1mawdirection100() {
        return a1mawdirection100;
    }

    public void setA1mawdirection100(BigDecimal a1mawdirection100) {
        this.a1mawdirection100 = a1mawdirection100;
    }

    public BigDecimal getA10mawspeed100() {
        return a10mawspeed100;
    }

    public void setA10mawspeed100(BigDecimal a10mawspeed100) {
        this.a10mawspeed100 = a10mawspeed100;
    }

    public BigDecimal getA10mawdirection100() {
        return a10mawdirection100;
    }

    public void setA10mawdirection100(BigDecimal a10mawdirection100) {
        this.a10mawdirection100 = a10mawdirection100;
    }

    public BigDecimal getAhmaxwspeed100() {
        return ahmaxwspeed100;
    }

    public void setAhmaxwspeed100(BigDecimal ahmaxwspeed100) {
        this.ahmaxwspeed100 = ahmaxwspeed100;
    }

    public BigDecimal getAhmaxwspeedwdir100() {
        return ahmaxwspeedwdir100;
    }

    public void setAhmaxwspeedwdir100(BigDecimal ahmaxwspeedwdir100) {
        this.ahmaxwspeedwdir100 = ahmaxwspeedwdir100;
    }

    public BigDecimal getAhmaxwspeedt100() {
        return ahmaxwspeedt100;
    }

    public void setAhmaxwspeedt100(BigDecimal ahmaxwspeedt100) {
        this.ahmaxwspeedt100 = ahmaxwspeedt100;
    }

    public BigDecimal getHcfmiwspeed100() {
        return hcfmiwspeed100;
    }

    public void setHcfmiwspeed100(BigDecimal hcfmiwspeed100) {
        this.hcfmiwspeed100 = hcfmiwspeed100;
    }

    public BigDecimal getHcfmiwdir100() {
        return hcfmiwdir100;
    }

    public void setHcfmiwdir100(BigDecimal hcfmiwdir100) {
        this.hcfmiwdir100 = hcfmiwdir100;
    }

    public BigDecimal getAhexwspeed100() {
        return ahexwspeed100;
    }

    public void setAhexwspeed100(BigDecimal ahexwspeed100) {
        this.ahexwspeed100 = ahexwspeed100;
    }

    public BigDecimal getEhmaxwspeedwdir100() {
        return ehmaxwspeedwdir100;
    }

    public void setEhmaxwspeedwdir100(BigDecimal ehmaxwspeedwdir100) {
        this.ehmaxwspeedwdir100 = ehmaxwspeedwdir100;
    }

    public BigDecimal getHhmaxwspeedt100() {
        return hhmaxwspeedt100;
    }

    public void setHhmaxwspeedt100(BigDecimal hhmaxwspeedt100) {
        this.hhmaxwspeedt100 = hhmaxwspeedt100;
    }

    public BigDecimal getIamawspeed50() {
        return iamawspeed50;
    }

    public void setIamawspeed50(BigDecimal iamawspeed50) {
        this.iamawspeed50 = iamawspeed50;
    }

    public BigDecimal getIamawdirection50() {
        return iamawdirection50;
    }

    public void setIamawdirection50(BigDecimal iamawdirection50) {
        this.iamawdirection50 = iamawdirection50;
    }

    public BigDecimal getIamawspeed70() {
        return iamawspeed70;
    }

    public void setIamawspeed70(BigDecimal iamawspeed70) {
        this.iamawspeed70 = iamawspeed70;
    }

    public BigDecimal getIamawdirection70() {
        return iamawdirection70;
    }

    public void setIamawdirection70(BigDecimal iamawdirection70) {
        this.iamawdirection70 = iamawdirection70;
    }

    public BigDecimal getIamawspeed100() {
        return iamawspeed100;
    }

    public void setIamawspeed100(BigDecimal iamawspeed100) {
        this.iamawspeed100 = iamawspeed100;
    }

    public BigDecimal getIamawdirection100() {
        return iamawdirection100;
    }

    public void setIamawdirection100(BigDecimal iamawdirection100) {
        this.iamawdirection100 = iamawdirection100;
    }
}