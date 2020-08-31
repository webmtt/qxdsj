package cma.cimiss2.dpc.decoder.bean.surf;

import lombok.Data;

@Data
public class Hysjhor {

    private String dRecordId;//记录标识

    private String stationnum;//区站号

    private Double latitude;//纬度

    private Double longitude;//经度

    private Integer groundHeight;//观测场拔海高度

    private Integer barometerHeight;//气压传感器拔海高度

    private Integer manualOrAutomatic;//观测方式

    private String observeTime;//观测时间

    private Double f2Dir;//2分钟风向

    private Double f2Speed;//2分钟平均风速

    private Double f10Dir;//10分钟风向

    private Double f10Speed;//10分钟平均风速

    private Double fMaxDir;//最大风速的风向

    private Double fMaxSpeed;//最大风速

    private Integer fMaxSpeedTime;//最大风速出现时间

    private Double f0Dir;//瞬时风向

    private Double f0Speed;//瞬时风速

    private Double fGreatDir;//极大风速的风向

    private Double fGreatSpeed;//极大风速

    private Integer fGreatSpeedTime;//极大风速出现时间

    private Double r;//小时降水量

    private Double t;//气温

    private Double tMax;//最高气温

    private Integer tMaxTime;//最高气温出现时间

    private Double tMin;//最低气温

    private Integer tMinTime;//最低气温出现时间

    private Double u;//相对湿度

    private Double uMin;//最小相对湿度

    private Integer uMinTime;//最小相对湿度出现时间

    private Double e;//水汽压

    private Double td;//露点温度

    private Double p;//本站气压

    private Double pMax;//最高本站气压

    private Integer pMaxTime;//最高本站气压出现时间

    private Double pMin;//最低本站气压

    private Integer pMinTime;//最低本站气压出现时间

    private Double b;//草面（雪面）温度

    private Double bMax;//草面（雪面）最高温度

    private Integer bMaxTime;//草面（雪面）最高出现时间

    private Double bMin;//草面（雪面）最低温度

    private Integer bMinTime;//草面（雪面）最低出现时间

    private Double d0;//地面温度

    private Double d0Max;//地面最高温度

    private Integer d0MaxTime;//地面最高出现时间

    private Double d0Min;//地面最低温度

    private Integer d0MinTime;//地面最低出现时间

    private Double d5;//5厘米地温

    private Double d10;//10厘米地温

    private Double d15;//15厘米地温

    private Double d20;//20厘米地温

    private Double d40;//40厘米地温

    private Double d80;//80厘米地温

    private Double d160;//160厘米地温

    private Double d320;//320厘米地温 

    private Double l;//蒸发量

    private Double pSea;//海平面气压

    private Integer v;//能见度

    private Integer vTime;//最小能见度

    private Integer vMinTime;//最小能见度出现时间

    private Double rMinutes;//分钟降水量
}
