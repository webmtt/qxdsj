package cma.cimiss2.dpc.decoder.bean.surf;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Mulhor {

    private String id;//id主键

    private String dDatetime;//记录标识

    private String layer;//层次

    private String v01301;//区站号(字符)

    private Timestamp dRymdhm;//收到时间

    private String h10300;//水泥路面

    private String h10301;//最高水泥路面

    private String h10301052;//最高水泥路面出现时间

    private String h10302;//最低水泥路面

    private String h10302052;//最低水泥路面出现时间

    private String h10310;//土面温度

    private String h10311;//最高土面温度

    private String h10311052;//最高土面温度出现时间

    private String h10312;//最低土面温度

    private String h10312052;//最低土面温度出现时间

    private String h10320;//地砖路面

    private String h10321;//最高地砖路面

    private String h10321052;//最高地砖路面出现时间

    private String h10322;//最低地砖路面

    private String h10322052;//最低地砖路面出现时间

    private Double v11290;//2分钟平均风向

    private Double v11291;//2分钟平均风速

    private Double v11292;//10分钟平均风向

    private Double v11293;//10分钟平均风速

    private Double v11296;//最大风速的风向

    private Double v11042;//小时内最大风速

    private String v11042052;//小时内最大风速出现时间

    private Double v11201;//瞬时风向

    private Double v11202;//瞬时风速

    private Double v11211;//小时内极大风速的风向

    private String v11046;//小时内极大风速

    private String v11046052;//小时内极大风速出现时间

    private Double v13011;//人工加密观测降水量

    private Double v12001;//气温

    private Double v12011;//小时内最高气温

    private String v12011052;//小时内最高气温出现时间

    private Double v13003;//环境相对湿度

    private Double v13007;//日最小相对湿度

    private String v13007052;//小时内最小相对湿度出现时间

    private Double v13004;//水汽压

    private Double v12003;//露点温度

    private Double v10004;//本站气压

    private Double v10301;//小时内最高本站气压

    private String v10301052;//小时内最高本站气压出现时间

    private Double v10302;//小时内最低本站气压

    private String v10302052;//小时内最低本站气压出现时间

    private Double v12314;//草面（雪面）温度

    private Double v12315;//小时内草面（雪面）最高温度

    private String v12315052;//小时内草面（雪面）最高温度出现时间

    private Double v12316;//小时内草面（雪面）最低温度

    private String v12316052;//小时内草面（雪面）最低温度出现时间

    private Double v12120;//地面温度

    private Double v12311;//小时内最高地面温度

    private String v12311052;//小时内最高地面温度出现时间

    private Double v12121;//小时内最低地面温度

    private String v12121052;//小时内最低地面温度出现时间

    private Double v12030040;//40cm地温

    private Double v12030050;//50cm地温

    private Double v12030080;//80cm地温

    private Double v12030160;//160cm地温

    private Double v12030320;//320cm地温

    private Double v2000170101;//1分钟平均水平能见度

    private Double v20059;//最小水平能见度

    private String v20059052;//最小水平能见度出现时间

    private Double v10051;//海平面气压

    private Double v13019;//1小时降水量

    private Double v13014;//分钟降水量

    private Double v13013;//积雪深度

    private String v12012052;//最低气温出现时间

    private Double v12012;//最低气温

    private String dDataId;//资料标识

    private String v13012;

    private String v130031;

    private Double v14042;

    private String v1301111;

    private String v14052;

    private String v130112;

    private String v1301121;

    private String v1301122;
}
