package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;



import java.io.Serializable;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/4/9
 */

public class ViewParamDayEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private Float ganshight; // 气压最高值*0.1hPa
    private Float ganslow; // 气压最低值
    private String ganshighthour; // 气压最高值出现时间
    private String ganslowHour; // 气压最低值出现时间
    private Float temphight; // 气温最高值
    private Float templow; // 气温最低值
    private String temphighthour; // 气温最高值出现时间
    private String templowhour; // 气温最低值出现时间
    private Integer wetness; // 相对湿度最小值
    private String wetlowhour; // 相对湿度最小值时间hhmm
    private String wetherSymbol;//天气符号
    private String wetherStartTime;//天气开始时间-时+分
    private String wetherEndTime;//天气结束时间-时+分
    private Float evaporatl;//小型蒸发量
    private Float evaporath;//大型蒸发量
    private String bigwind;//自记最大风向
    private Float bigwindSpeed;//自记最大风速
    private String bigwindTime;//自记最大风速出现时间
    private String maxwind;//自记极大风向
    private Float maxwindSpeed;//自记极大风速
    private String maxwindTime;//自记最大风速出现时间
    private Float dtemL;//最低浅层地温-地面
    private Float dtemH;//最高浅层地温-地面
    private Float sunshine;//日日照时数
    private Float snowStress;//日雪压
    private Float snowHight;//日雪深
    private Integer glazeNSDia;//雨凇南北直径
    private Integer glazeWEDia;//雨凇东西直径mm
    private Integer glazeNSHight;//雨凇南北厚度
    private Integer glazeWEHight;//雨凇东西厚度mm
    private Integer glazeNSWeight;//雨凇东西重量g/m
    private Integer glazeWEWeight;//雨凇东西重量
    private Integer rimeNSDia;//雾凇南北直径
    private Integer rimeWEDia;//雾凇东西直径
    private Integer rimeNSHight;//雾凇南北厚度
    private Integer rimeWEHight;//雾凇东西厚度
    private Integer rimeNSWeight;//雾凇东西重量
    private Integer rimeWEWeight;//雾凇东西重量
    private Float iceTemp;//积冰气温
    private String iceWind;//积冰风向
    private Float iceWindSpeed;//积冰风速0.1m/s
    private Float windMax1;//最大风速
    private String windMax1Time;//最大风速出现时间
    private Float windMax2;//极大风速
    private String windMax2Time;//极大风速出现时间
    private Float permafrost1Ceiling;//第1冻土层上限
    private Float permafrost1Floor;//第1冻土层下限
    private Float permafrost2Ceiling;//第2冻土层上限
    private Float permafrost2Floor;//第2冻土层下限
    private Float rain1;//自记1小时降水
    private Float rain10;//自记10分钟最大降水
    private Float rain20 ; // 20-08时雨量筒观测降水量 ( 单位：毫米 )
    private Float rain08 ; // 08-20时雨量筒观测降水量 ( 单位：毫米 )
    private Float rain202 ; // 20-20时降水量 ( 单位：毫米 )
    private Float evaporationSmall;//小型蒸发量日总量
    private Float evaporationBig;//大型蒸发量日总量
    private Float snowPressure;//雪压
    private String dtemLTime;//最低浅层地温出现时间-地面
    private String dtemHTime;//最低浅层地温出现时间-地面
    private Float htem80;//80cm深层地温
    private Float htem160;//160cm深层地温
    private Float htem320;//320cm深层地温
    private Float htem50;//50cm深层地温
    private Float htem100;//100cm深层地温
    private Float htem200;//200cm深层地温
    private Float htem300;//330cm深层地温
    private Float sunlightTotal;//日照总时数
    private String sunRise;//日出时间
    private String sunFall;//日落时间
    private Float grassMaxHour;//草面温度最高值
    private String grassMaxTime;//草面温度最高值出现时间
    private Float grassMinHour;//草面温度最低值
    private String grassMinTime;//草面温度最低值出现时间
    private String year; // 年
    private String month;//月
    private String day;//日

    public Float getGanshight() {
        return ganshight;
    }

    public void setGanshight(Float ganshight) {
        this.ganshight = ganshight;
    }

    public String getBigwindTime() {
        return bigwindTime;
    }

    public void setBigwindTime(String bigwindTime) {
        this.bigwindTime = bigwindTime;
    }

    public String getMaxwindTime() {
        return maxwindTime;
    }

    public void setMaxwindTime(String maxwindTime) {
        this.maxwindTime = maxwindTime;
    }

    public Float getGanslow() {
        return ganslow;
    }

    public void setGanslow(Float ganslow) {
        this.ganslow = ganslow;
    }

    public String getGanshighthour() {
        return ganshighthour;
    }

    public void setGanshighthour(String ganshighthour) {
        this.ganshighthour = ganshighthour;
    }

    public String getGanslowHour() {
        return ganslowHour;
    }

    public void setGanslowHour(String ganslowHour) {
        this.ganslowHour = ganslowHour;
    }

    public Float getTemphight() {
        return temphight;
    }

    public void setTemphight(Float temphight) {
        this.temphight = temphight;
    }

    public Float getTemplow() {
        return templow;
    }

    public void setTemplow(Float templow) {
        this.templow = templow;
    }

    public String getTemphighthour() {
        return temphighthour;
    }

    public void setTemphighthour(String temphighthour) {
        this.temphighthour = temphighthour;
    }

    public String getTemplowhour() {
        return templowhour;
    }

    public void setTemplowhour(String templowhour) {
        this.templowhour = templowhour;
    }

    public Integer getWetness() {
        return wetness;
    }

    public void setWetness(Integer wetness) {
        this.wetness = wetness;
    }

    public String getWetlowhour() {
        return wetlowhour;
    }

    public void setWetlowhour(String wetlowhour) {
        this.wetlowhour = wetlowhour;
    }

    public String getWetherSymbol() {
        return wetherSymbol;
    }

    public void setWetherSymbol(String wetherSymbol) {
        this.wetherSymbol = wetherSymbol;
    }

    public String getWetherStartTime() {
        return wetherStartTime;
    }

    public void setWetherStartTime(String wetherStartTime) {
        this.wetherStartTime = wetherStartTime;
    }

    public String getWetherEndTime() {
        return wetherEndTime;
    }

    public void setWetherEndTime(String wetherEndTime) {
        this.wetherEndTime = wetherEndTime;
    }

    public Float getEvaporatl() {
        return evaporatl;
    }

    public void setEvaporatl(Float evaporatl) {
        this.evaporatl = evaporatl;
    }

    public Float getEvaporath() {
        return evaporath;
    }

    public void setEvaporath(Float evaporath) {
        this.evaporath = evaporath;
    }

    public String getBigwind() {
        return bigwind;
    }

    public void setBigwind(String bigwind) {
        this.bigwind = bigwind;
    }

    public Float getBigwindSpeed() {
        return bigwindSpeed;
    }

    public void setBigwindSpeed(Float bigwindSpeed) {
        this.bigwindSpeed = bigwindSpeed;
    }

    public String getMaxwind() {
        return maxwind;
    }

    public void setMaxwind(String maxwind) {
        this.maxwind = maxwind;
    }

    public Float getMaxwindSpeed() {
        return maxwindSpeed;
    }

    public void setMaxwindSpeed(Float maxwindSpeed) {
        this.maxwindSpeed = maxwindSpeed;
    }

    public Float getDtemL() {
        return dtemL;
    }

    public void setDtemL(Float dtemL) {
        this.dtemL = dtemL;
    }

    public Float getDtemH() {
        return dtemH;
    }

    public void setDtemH(Float dtemH) {
        this.dtemH = dtemH;
    }

    public Float getSunshine() {
        return sunshine;
    }

    public void setSunshine(Float sunshine) {
        this.sunshine = sunshine;
    }

    public Float getSnowStress() {
        return snowStress;
    }

    public void setSnowStress(Float snowStress) {
        this.snowStress = snowStress;
    }

    public Float getSnowHight() {
        return snowHight;
    }

    public void setSnowHight(Float snowHight) {
        this.snowHight = snowHight;
    }

    public Integer getGlazeNSDia() {
        return glazeNSDia;
    }

    public void setGlazeNSDia(Integer glazeNSDia) {
        this.glazeNSDia = glazeNSDia;
    }

    public Integer getGlazeWEDia() {
        return glazeWEDia;
    }

    public void setGlazeWEDia(Integer glazeWEDia) {
        this.glazeWEDia = glazeWEDia;
    }

    public Integer getGlazeNSHight() {
        return glazeNSHight;
    }

    public void setGlazeNSHight(Integer glazeNSHight) {
        this.glazeNSHight = glazeNSHight;
    }

    public Integer getGlazeWEHight() {
        return glazeWEHight;
    }

    public void setGlazeWEHight(Integer glazeWEHight) {
        this.glazeWEHight = glazeWEHight;
    }

    public Integer getGlazeNSWeight() {
        return glazeNSWeight;
    }

    public void setGlazeNSWeight(Integer glazeNSWeight) {
        this.glazeNSWeight = glazeNSWeight;
    }

    public Integer getGlazeWEWeight() {
        return glazeWEWeight;
    }

    public void setGlazeWEWeight(Integer glazeWEWeight) {
        this.glazeWEWeight = glazeWEWeight;
    }

    public Integer getRimeNSDia() {
        return rimeNSDia;
    }

    public void setRimeNSDia(Integer rimeNSDia) {
        this.rimeNSDia = rimeNSDia;
    }

    public Integer getRimeWEDia() {
        return rimeWEDia;
    }

    public void setRimeWEDia(Integer rimeWEDia) {
        this.rimeWEDia = rimeWEDia;
    }

    public Integer getRimeNSHight() {
        return rimeNSHight;
    }

    public void setRimeNSHight(Integer rimeNSHight) {
        this.rimeNSHight = rimeNSHight;
    }

    public Integer getRimeWEHight() {
        return rimeWEHight;
    }

    public void setRimeWEHight(Integer rimeWEHight) {
        this.rimeWEHight = rimeWEHight;
    }

    public Integer getRimeNSWeight() {
        return rimeNSWeight;
    }

    public void setRimeNSWeight(Integer rimeNSWeight) {
        this.rimeNSWeight = rimeNSWeight;
    }

    public Integer getRimeWEWeight() {
        return rimeWEWeight;
    }

    public void setRimeWEWeight(Integer rimeWEWeight) {
        this.rimeWEWeight = rimeWEWeight;
    }

    public Float getIceTemp() {
        return iceTemp;
    }

    public void setIceTemp(Float iceTemp) {
        this.iceTemp = iceTemp;
    }

    public String getIceWind() {
        return iceWind;
    }

    public void setIceWind(String iceWind) {
        this.iceWind = iceWind;
    }

    public Float getIceWindSpeed() {
        return iceWindSpeed;
    }

    public void setIceWindSpeed(Float iceWindSpeed) {
        this.iceWindSpeed = iceWindSpeed;
    }

    public Float getWindMax1() {
        return windMax1;
    }

    public void setWindMax1(Float windMax1) {
        this.windMax1 = windMax1;
    }

    public String getWindMax1Time() {
        return windMax1Time;
    }

    public void setWindMax1Time(String windMax1Time) {
        this.windMax1Time = windMax1Time;
    }

    public Float getWindMax2() {
        return windMax2;
    }

    public void setWindMax2(Float windMax2) {
        this.windMax2 = windMax2;
    }

    public String getWindMax2Time() {
        return windMax2Time;
    }

    public void setWindMax2Time(String windMax2Time) {
        this.windMax2Time = windMax2Time;
    }

    public Float getPermafrost1Ceiling() {
        return permafrost1Ceiling;
    }

    public void setPermafrost1Ceiling(Float permafrost1Ceiling) {
        this.permafrost1Ceiling = permafrost1Ceiling;
    }

    public Float getPermafrost1Floor() {
        return permafrost1Floor;
    }

    public void setPermafrost1Floor(Float permafrost1Floor) {
        this.permafrost1Floor = permafrost1Floor;
    }

    public Float getPermafrost2Ceiling() {
        return permafrost2Ceiling;
    }

    public void setPermafrost2Ceiling(Float permafrost2Ceiling) {
        this.permafrost2Ceiling = permafrost2Ceiling;
    }

    public Float getPermafrost2Floor() {
        return permafrost2Floor;
    }

    public void setPermafrost2Floor(Float permafrost2Floor) {
        this.permafrost2Floor = permafrost2Floor;
    }

    public Float getRain1() {
        return rain1;
    }

    public void setRain1(Float rain1) {
        this.rain1 = rain1;
    }

    public Float getRain10() {
        return rain10;
    }

    public void setRain10(Float rain10) {
        this.rain10 = rain10;
    }

    public Float getRain20() {
        return rain20;
    }

    public void setRain20(Float rain20) {
        this.rain20 = rain20;
    }

    public Float getRain08() {
        return rain08;
    }

    public void setRain08(Float rain08) {
        this.rain08 = rain08;
    }

    public Float getRain202() {
        return rain202;
    }

    public void setRain202(Float rain202) {
        this.rain202 = rain202;
    }

    public Float getEvaporationSmall() {
        return evaporationSmall;
    }

    public void setEvaporationSmall(Float evaporationSmall) {
        this.evaporationSmall = evaporationSmall;
    }

    public Float getEvaporationBig() {
        return evaporationBig;
    }

    public void setEvaporationBig(Float evaporationBig) {
        this.evaporationBig = evaporationBig;
    }

    public Float getSnowPressure() {
        return snowPressure;
    }

    public void setSnowPressure(Float snowPressure) {
        this.snowPressure = snowPressure;
    }

    public String getDtemLTime() {
        return dtemLTime;
    }

    public void setDtemLTime(String dtemLTime) {
        this.dtemLTime = dtemLTime;
    }

    public String getDtemHTime() {
        return dtemHTime;
    }

    public void setDtemHTime(String dtemHTime) {
        this.dtemHTime = dtemHTime;
    }

    public Float getHtem80() {
        return htem80;
    }

    public void setHtem80(Float htem80) {
        this.htem80 = htem80;
    }

    public Float getHtem160() {
        return htem160;
    }

    public void setHtem160(Float htem160) {
        this.htem160 = htem160;
    }

    public Float getHtem320() {
        return htem320;
    }

    public void setHtem320(Float htem320) {
        this.htem320 = htem320;
    }

    public Float getHtem50() {
        return htem50;
    }

    public void setHtem50(Float htem50) {
        this.htem50 = htem50;
    }

    public Float getHtem100() {
        return htem100;
    }

    public void setHtem100(Float htem100) {
        this.htem100 = htem100;
    }

    public Float getHtem200() {
        return htem200;
    }

    public void setHtem200(Float htem200) {
        this.htem200 = htem200;
    }

    public Float getHtem300() {
        return htem300;
    }

    public void setHtem300(Float htem300) {
        this.htem300 = htem300;
    }

    public Float getSunlightTotal() {
        return sunlightTotal;
    }

    public void setSunlightTotal(Float sunlightTotal) {
        this.sunlightTotal = sunlightTotal;
    }

    public String getSunRise() {
        return sunRise;
    }

    public void setSunRise(String sunRise) {
        this.sunRise = sunRise;
    }

    public String getSunFall() {
        return sunFall;
    }

    public void setSunFall(String sunFall) {
        this.sunFall = sunFall;
    }

    public Float getGrassMaxHour() {
        return grassMaxHour;
    }

    public void setGrassMaxHour(Float grassMaxHour) {
        this.grassMaxHour = grassMaxHour;
    }

    public String getGrassMaxTime() {
        return grassMaxTime;
    }

    public void setGrassMaxTime(String grassMaxTime) {
        this.grassMaxTime = grassMaxTime;
    }

    public Float getGrassMinHour() {
        return grassMinHour;
    }

    public void setGrassMinHour(Float grassMinHour) {
        this.grassMinHour = grassMinHour;
    }

    public String getGrassMinTime() {
        return grassMinTime;
    }

    public void setGrassMinTime(String grassMinTime) {
        this.grassMinTime = grassMinTime;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
