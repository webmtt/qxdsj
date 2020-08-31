package cma.cimiss2.dpc.quickqc.bean;

/**
 * 站点参数值
 */
public class StationData {

    private double latitude; // 纬度(度)
    private double longitude; // 经度(度)
    private double altitude; // 台站海拔高度(米)

    public StationData() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }
}