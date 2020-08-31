package cma.cimiss2.dpc.decoder.bean.surf;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class Digchn {

    private String dRecordId;//记录主键

    private String v01301;//区站号(字符)

    private Integer v01300;//区站号(数字)

    private Date dDatetime;//时间

    private Integer v04001;//年

    private Integer let;//纬度

    private Integer lat;//经度

    private Integer aslHigh;//海拔高度

    private Integer v04002;//出现的月

    private Integer v04003;//出现的日

    private Integer v04004;//出现的时

    private Integer v04005;//出现的分钟

    private Integer rain;//降水量总量

    private Integer rmax;//一日最大降水量

    private String timeRmax;//一日最大降水量出现日期

    private Integer daysRExceed1;//≥0.1降水日数

    private Integer daysRExceed10;//≥1.0降水日数

    private Integer daysRExceed50;//≥5.0降水日数

    private Integer daysRExceed100;//≥10.0降水日数

    private Integer daysRExceed250;//≥25.0降水日数

    private Integer daysRExceed500;//≥50.0降水日数

    private Integer daysRExceed1000;//≥100.0降水日数

    private Integer daysRExceed1500;//≥150.0降水日数

    private Integer daysRContinue;//年最长连续降水量日数

    private Integer rContinue;//年最长连续降水量

    private String timeStartRContinue;//年最长连续降水量开始日期

    private String timeEndRContinue;//年最长连续降水量结束日期

    private Integer daysNonrContinue;//年最长无连续降水量日数

    private String timeStartNonrContinue;//年最长无连续降水量开始日期

    private String timeEndNonrContinue;//年最长无连续降水量结束日期

    private Double rmax05minutes;//5时段最大降水量

    private Timestamp timeRmax05minutes;//5分钟年最大降水量出现时间

    private Double rmax10minutes;//10分钟年最大降水量

    private Timestamp timeRmax10minutes;//10分钟年最大降水量出现时间

    private Double rmax15minutes;//15分钟年最大降水量

    private Timestamp timeRmax15minutes;//15分钟年最大降水量出现时间

    private Double rmax20minutes;//20分钟年最大降水量

    private Timestamp timeRmax20minutes;//20分钟年最大降水量出现时间

    private Double rmax30minutes;//30分钟年最大降水量

    private Timestamp timeRmax30minutes;//30分钟年最大降水量出现时间

    private Double rmax45minutes;//45分钟年最大降水量

    private Timestamp timeRmax45minutes;//45分钟年最大降水量出现时间

    private Double rmax60minutes;//60分钟年最大降水量

    private Timestamp timeRmax60minutes;//60分钟年最大降水量出现时间

    private Double rmax90minutes;//90分钟年最大降水量

    private Timestamp timeRmax90minutes;//90分钟年最大降水量出现时间

    private Double rmax120minutes;//120分钟年最大降水量

    private Timestamp timeRmax120minutes;//120分钟年最大降水量出现时间

    private Double rmax180minutes;//180分钟年最大降水量

    private Timestamp timeRmax180minutes;//180分钟年最大降水量出现时间

    private Double rmax240minutes;//240分钟年最大降水量

    private Timestamp timeRmax240minutes;//240分钟年最大降水量出现时间

    private Double rmax360minutes;//360分钟年最大降水量

    private Timestamp timeRmax360minutes;//360分钟年最大降水量出现时间

    private Double rmax540minutes;//540分钟年最大降水量

    private Timestamp timeRmax540minutes;//540分钟年最大降水量出现时间

    private Double rmax720minutes;//720分钟年最大降水量

    private Timestamp timeRmax720minutes;//720分钟年最大降水量出现时间

    private Double rmax1440minutes;//1440分钟年最大降水量

    private Timestamp timeRmax1440minutes;//1440分钟年最大降水量出现时间
}
