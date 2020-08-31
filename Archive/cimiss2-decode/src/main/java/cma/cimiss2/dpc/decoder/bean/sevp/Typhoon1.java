package cma.cimiss2.dpc.decoder.bean.sevp;

import lombok.Data;
import java.io.Serializable;

/**
 * ************************************
 * @ClassName: Typhoon
 * @Auther: dangjinhu
 * @Date: 2019/3/8 14:34
 * @Description: 全球台风实况和预报
 * 实况：区域，台风编号，台风名，观测时间，经度，纬度，强度
 * 预报：区域，台风编号，台风名，资料时间（起报时间），预报时效，经度，纬度，强度
 * @Copyright: All rights reserver.
 * ************************************
 */

public class Typhoon1 implements Serializable {

    private static final long serialVersionUID = -5452716273340580446L;

    /**
     * 区域
     */
    private String area;

    /**
     * 台风编号
     */
    private String TyphoonNumber;

    /**
     * 台风名
     */
    private String TyphoonName;

    /**
     * (资料|观测)时间
     */
    private String date;

    /**
     * 预报时效(预报数据)
     */
    private String forestTime;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 强度
     */
    private String strength;

    public Typhoon1() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTyphoonNumber() {
        return TyphoonNumber;
    }

    public void setTyphoonNumber(String typhoonNumber) {
        TyphoonNumber = typhoonNumber;
    }

    public String getTyphoonName() {
        return TyphoonName;
    }

    public void setTyphoonName(String typhoonName) {
        TyphoonName = typhoonName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getForestTime() {
        return forestTime;
    }

    public void setForestTime(String forestTime) {
        this.forestTime = forestTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    @Override
    public String toString() {
        return "Typhoon{" +
                "area='" + area + '\'' +
                ", TyphoonNumber='" + TyphoonNumber + '\'' +
                ", TyphoonName='" + TyphoonName + '\'' +
                ", date='" + date + '\'' +
                ", forestTime='" + forestTime + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", strength='" + strength + '\'' +
                '}';
    }
}
