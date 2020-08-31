package cma.cimiss2.dpc.decoder.fileDecode.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/4/9
 */
@Data
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
}
