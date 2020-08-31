package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;

import java.io.Serializable;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/4/9
 */

public class ViewParamsHourEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Float gans; // 本站气压*0.1hPa（24时）
    private Float seagans; // 海平面气压*0.1hPa（02、08、14、20时）
    private Float temp; // 气温
    private Float balltemp; // 湿球温度*0.1℃
    private Float ballIceTemp;//结冰湿球温度
    private Float watergans; // 水汽压0.1hPa
    private Integer wetness; // 相对湿度
    private Float rain; // 降水量
    private Integer cloudt;//总云量
    private Integer cloudl;//低云量
    private String  shapeCloud;//云状
    private Float visibility1;//能见度km
    private Integer visibility2;//能见度级别
    private String wind;//风向
    private Float windSpeed;//风速
    private Float dtem;//浅层地温-地面
    private Float dtem5;//浅层地温-5cm
    private Float dtem10;//浅层地温-10cm
    private Float dtem15;//浅层地温-15cm
    private Float dtem20;//浅层地温-20cm
    private Float dtem40;//浅层地温-40cm
    private Float dtem80;//浅层地温-80cm
    private Float dtem160;//浅层地温-160cm
    private Float dtem320;//浅层地温-320cm
    private Float seaGans; // 海平面气压*0.1hPa（02、08、14、20时）
    private Float ballTemp; // 湿球温度*0.1℃
    private Float ballICETemp; // 结冰湿球温度*0.1℃
    private Float pointTemp; // 露点温度*0.1℃
    private Float waterGans; // 水汽压0.1hPa
    private Integer cloudT;//总云量
    private Integer cloudL;//低云量
    private String cloudShade;//云状
    private Float evaporationBig;//小时蒸发量
    private String wind2;//2分钟平均风向
    private Float wind2Speed;//2分钟平均风速
    private String wind10;//10分钟平均风向
    private Float wind10Speed;//10分钟平均风速
    private Float htem80;//80cm深层地温
    private Float htem160;//160cm深层地温
    private Float htem320;//320cm深层地温
    private Float sunLightHour;//日照时数
    private Float grassTemp;//草面温度
    private String year;//年
    private String month;//月
    private String day;//日
    private String hour; // 数据时间-时

    public Float getBallICETemp() {
        return ballICETemp;
    }

    public void setBallICETemp(Float ballICETemp) {
        this.ballICETemp = ballICETemp;
    }

    public void setGans(Float gans) {
        this.gans = gans;
    }

    public void setSeagans(Float seagans) {
        this.seagans = seagans;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public void setBalltemp(Float balltemp) {
        this.balltemp = balltemp;
    }

    public void setBallIceTemp(Float ballIceTemp) {
        this.ballIceTemp = ballIceTemp;
    }

    public void setWatergans(Float watergans) {
        this.watergans = watergans;
    }

    public void setWetness(Integer wetness) {
        this.wetness = wetness;
    }

    public void setRain(Float rain) {
        this.rain = rain;
    }

    public void setCloudt(Integer cloudt) {
        this.cloudt = cloudt;
    }

    public void setCloudl(Integer cloudl) {
        this.cloudl = cloudl;
    }

    public void setShapeCloud(String shapeCloud) {
        this.shapeCloud = shapeCloud;
    }

    public void setVisibility1(Float visibility1) {
        this.visibility1 = visibility1;
    }

    public void setVisibility2(Integer visibility2) {
        this.visibility2 = visibility2;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setDtem(Float dtem) {
        this.dtem = dtem;
    }

    public void setDtem5(Float dtem5) {
        this.dtem5 = dtem5;
    }

    public void setDtem10(Float dtem10) {
        this.dtem10 = dtem10;
    }

    public void setDtem15(Float dtem15) {
        this.dtem15 = dtem15;
    }

    public void setDtem20(Float dtem20) {
        this.dtem20 = dtem20;
    }

    public void setDtem40(Float dtem40) {
        this.dtem40 = dtem40;
    }

    public void setDtem80(Float dtem80) {
        this.dtem80 = dtem80;
    }

    public void setDtem160(Float dtem160) {
        this.dtem160 = dtem160;
    }

    public void setDtem320(Float dtem320) {
        this.dtem320 = dtem320;
    }

    public void setSeaGans(Float seaGans) {
        this.seaGans = seaGans;
    }

    public void setBallTemp(Float ballTemp) {
        this.ballTemp = ballTemp;
    }

    public void setPointTemp(Float pointTemp) {
        this.pointTemp = pointTemp;
    }

    public void setWaterGans(Float waterGans) {
        this.waterGans = waterGans;
    }

    public void setCloudT(Integer cloudT) {
        this.cloudT = cloudT;
    }

    public void setCloudL(Integer cloudL) {
        this.cloudL = cloudL;
    }

    public void setCloudShade(String cloudShade) {
        this.cloudShade = cloudShade;
    }

    public void setEvaporationBig(Float evaporationBig) {
        this.evaporationBig = evaporationBig;
    }

    public void setWind2(String wind2) {
        this.wind2 = wind2;
    }

    public void setWind2Speed(Float wind2Speed) {
        this.wind2Speed = wind2Speed;
    }

    public void setWind10(String wind10) {
        this.wind10 = wind10;
    }

    public void setWind10Speed(Float wind10Speed) {
        this.wind10Speed = wind10Speed;
    }

    public void setHtem80(Float htem80) {
        this.htem80 = htem80;
    }

    public void setHtem160(Float htem160) {
        this.htem160 = htem160;
    }

    public void setHtem320(Float htem320) {
        this.htem320 = htem320;
    }

    public void setSunLightHour(Float sunLightHour) {
        this.sunLightHour = sunLightHour;
    }

    public void setGrassTemp(Float grassTemp) {
        this.grassTemp = grassTemp;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Float getGans() {
        return gans;
    }

    public Float getSeagans() {
        return seagans;
    }

    public Float getTemp() {
        return temp;
    }

    public Float getBalltemp() {
        return balltemp;
    }

    public Float getBallIceTemp() {
        return ballIceTemp;
    }

    public Float getWatergans() {
        return watergans;
    }

    public Integer getWetness() {
        return wetness;
    }

    public Float getRain() {
        return rain;
    }

    public Integer getCloudt() {
        return cloudt;
    }

    public Integer getCloudl() {
        return cloudl;
    }

    public String getShapeCloud() {
        return shapeCloud;
    }

    public Float getVisibility1() {
        return visibility1;
    }

    public Integer getVisibility2() {
        return visibility2;
    }

    public String getWind() {
        return wind;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public Float getDtem() {
        return dtem;
    }

    public Float getDtem5() {
        return dtem5;
    }

    public Float getDtem10() {
        return dtem10;
    }

    public Float getDtem15() {
        return dtem15;
    }

    public Float getDtem20() {
        return dtem20;
    }

    public Float getDtem40() {
        return dtem40;
    }

    public Float getDtem80() {
        return dtem80;
    }

    public Float getDtem160() {
        return dtem160;
    }

    public Float getDtem320() {
        return dtem320;
    }

    public Float getSeaGans() {
        return seaGans;
    }

    public Float getBallTemp() {
        return ballTemp;
    }

    public Float getPointTemp() {
        return pointTemp;
    }

    public Float getWaterGans() {
        return waterGans;
    }

    public Integer getCloudT() {
        return cloudT;
    }

    public Integer getCloudL() {
        return cloudL;
    }

    public String getCloudShade() {
        return cloudShade;
    }

    public Float getEvaporationBig() {
        return evaporationBig;
    }

    public String getWind2() {
        return wind2;
    }

    public Float getWind2Speed() {
        return wind2Speed;
    }

    public String getWind10() {
        return wind10;
    }

    public Float getWind10Speed() {
        return wind10Speed;
    }

    public Float getHtem80() {
        return htem80;
    }

    public Float getHtem160() {
        return htem160;
    }

    public Float getHtem320() {
        return htem320;
    }

    public Float getSunLightHour() {
        return sunLightHour;
    }

    public Float getGrassTemp() {
        return grassTemp;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }
}
