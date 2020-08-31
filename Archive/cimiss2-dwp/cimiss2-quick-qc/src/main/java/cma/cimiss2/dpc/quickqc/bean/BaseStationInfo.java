package cma.cimiss2.dpc.quickqc.bean;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description: 台站基本信息 基类 </b><br>
 *
 * @author wuzuoqiang
 * @version 1.0
 * @Note <b>ProjectName:</b> cimiss2-quick-qc
 * <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.bean
 * <br><b>ClassName:</b> BaseStationInfo
 * <br><b>Date:</b> 2019年6月18日 下午12:24:56
 */
public class BaseStationInfo implements Cloneable {
    /**
     * 区站号
     */
    private String stationCode;
    /**
     * 经度
     */
    private double longitude;
    /**
     * 纬度
     */
    private double latitude;
    /**
     * 海拔高度
     */
    private double altitude;
    /**
     * 气压传感器高度
     */
    private double altitudeP;
    /**
     * 台站级别
     */
    private int stationClass;

    public String getStationCode() {
        return stationCode;
    }

    public void setStationCode(String stationCode) {
        this.stationCode = stationCode;
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

    public double getAltitudeP() {
        return altitudeP;
    }

    public void setAltitudeP(double altitudeP) {
        this.altitudeP = altitudeP;
    }

    public int getStationClass() {
        return stationClass;
    }

    public void setStationClass(int stationClass) {
        this.stationClass = stationClass;
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
