package cma.cimiss2.dpc.decoder.bean.surf;

import java.util.HashMap;
import java.util.Map;

/**
 * ************************************
 * @ClassName: TwSiteData
 * @Auther: dangjinhu
 * @Date: 2019/8/7 15:11
 * @Description: 台湾站点资料
 * @Copyright: All rights reserver.
 * ************************************
 */

public class TwSiteData {
    public static Integer DEFAULT8 = 999998;
    public static Integer DEFAULT9 = 999999;
    public static Integer DEFAULTQC = 9;
    /**
     * 纬度
     * V05001
     */
    private Double latitude;

    /**
     * 经度
     * V06001
     */
    private Double longitude;

    /**
     * 测站名称
     * V01301
     * 由于存在包含1位以上字母的站号，因此不进行数字站号的转换
     */
    private String locationName;

    /**
     * 测站ID
     */
    private String stationId;

    /**
     * 观测时间
     * 报文中为北京时？
     * 需要转换为国际时存入V04001- V04004???[解码入库时关注一下，和系统时间比对一下]
     */
    private String obsTime;

    /**
     * resourceId
     */
    private String resourceId;

    /**
     * 配置文件success
     */
    private boolean success;

    /**
     * 天气现象要素map,key为要素名,value为观测值
     */
    private Map<String, Double> weatherElement = new HashMap<>();

    /**
     * 市/乡镇map,key为省市/乡镇名字,value为编号
     */
    private Map<String, String> parameter = new HashMap<>();


    /**
     * 初始化对象
     * @param success    配置文件一级标签<success>值</success>
     * @param resourceId 记录ID 一级标签<result><resource_id>值</resource_id></result>
     */
    public TwSiteData(boolean success, String resourceId) {
        this.success = success;
        this.resourceId = resourceId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getObsTime() {
        return obsTime;
    }

    public void setObsTime(String obsTime) {
        this.obsTime = obsTime;
    }

    public String getResourceId() {
        return resourceId;
    }

    public boolean isSuccess() {
        return success;
    }

    /**
     * 获取要素观测值
     * @param key 要素名称
     * @return 观测值
     */
    public Double getWeatherElement(String key) {
        Double aDouble = weatherElement.get(key);
        return aDouble == null ? DEFAULT8.doubleValue() : weatherElement.get(key);
    }

    public void setWeatherElement(String elementName, Double elementValue) {
        this.weatherElement.put(elementName, elementValue);
    }

    /**
     * 获取观测数据对应得市乡镇
     * @param key 市乡镇字段
     * @return 市乡镇名称
     */
    public String getParameter(String key) {
        return parameter.get(key);
    }

    public void setParameter(String parameterName, String parameterValue) {
        this.parameter.put(parameterName, parameterValue);
    }
}
