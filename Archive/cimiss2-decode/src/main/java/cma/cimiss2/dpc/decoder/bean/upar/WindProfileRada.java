package cma.cimiss2.dpc.decoder.bean.upar;

import java.util.List;

/**
 * ************************************
 * @ClassName: WindRada
 * @Auther: dangjinhu
 * @Date: 2019/3/20 10:23
 * @Description: 实时、半小时平均、一小时平均的采样高度产品数据文件实体
 * @Copyright: All rights reserver.
 * ************************************
 */

public class WindProfileRada {

    // 1.版本信息

    /**
     * WND_OBS
     * 关键字,WND_(ROBS,HOBS,OOBS)分别对应实时、半小时平均、一小时平均数据
     */
    private String WND_OBS;

    /**
     * 文件版本号,两位整数,两位小数
     */
    private String versionNumber;

    // 2.测站基本参数

    /**
     * 区站号
     * 五位数字或第一位为字母,第二-五位为数
     * 出现字母转为ASCII码
     */
    private String staionId;

    /**
     * 经度
     * 以度为单位
     * 第一位为符号位
     * 东经取正,西经取负,三位整数,四位小数
     */
    private double longitude;

    /**
     * 纬度
     * 以度为单位
     * 第一位为符号位
     * 北纬取正,南纬取负,两位整数,四位小数
     */
    private double latitude;

    /**
     * 观测场拔海高度
     * 以米为单位
     * 第一位为符号位,四位整数,一位小数
     */
    private double altitude;

    /**
     * 风廓线雷达型号
     */
    private String radaModel;

    /**
     * 实时观测时为观测结束时间
     * 时间采用世界时
     * 四位年,两位月,两位日,两位时,两位分,两位秒
     */
    private String obeTime;

    /**
     * _OBS(包括R,H,O)
     * 观测数据开始标志
     */
    private String _OBS;

    public String get_OBS() {
        return _OBS;
    }

    public void set_OBS(String _OBS) {
        this._OBS = _OBS;
    }

    /**
     * 第三段数据
     */
    private List<WindProfileData> wData;
    /**
     * 结束标志
     */
    public static final String NNNN = "NNNN";
    public static final String NINE = "999999";

    public WindProfileRada() {
    }

    public String getWND_OBS() {
        return WND_OBS;
    }

    public void setWND_OBS(String WND_OBS) {
        this.WND_OBS = WND_OBS;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getStaionId() {
        return staionId;
    }

    public void setStaionId(String staionId) {
        this.staionId = staionId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public String getRadaModel() {
        return radaModel;
    }

    public void setRadaModel(String radaModel) {
        this.radaModel = radaModel;
    }

    public String getObeTime() {
        return obeTime;
    }

    public void setObeTime(String obeTime) {
        this.obeTime = obeTime;
    }

    public List<WindProfileData> getwData() {
        return wData;
    }

    public void setwData(List<WindProfileData> pData) {
        this.wData = pData;
    }

    @Override
    public String toString() {
        return "RadaOBS{" +
                "WND_OBS='" + WND_OBS + '\'' +
                ", versionNumber='" + versionNumber + '\'' +
                ", staionId='" + staionId + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", altitude=" + altitude +
                ", radaModel='" + radaModel + '\'' +
                ", obeTime='" + obeTime + '\'' +
                ", _OBS='" + _OBS + '\'' +
                ", wData=" + wData +
                '}';
    }
}
