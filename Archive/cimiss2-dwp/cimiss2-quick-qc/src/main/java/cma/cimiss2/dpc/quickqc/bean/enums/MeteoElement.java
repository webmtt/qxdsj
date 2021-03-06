package cma.cimiss2.dpc.quickqc.bean.enums;

public enum MeteoElement {  /**
     * 本站气压观测标识 无观测: 0 自动观测：1；人工观测：2
     */
    PRS,

    /**
     * 海平面气压标识 无观测：0；自动观测：1；人工观测：2
     */
    PRS_Sea,

    /**
     * 气温观测标识 无观测0 ：自动观测：1；人工观测：2
     */
    TEM,
    
    TG,

    /**
     * 气温观测标识 无观测0 ：自动观测：1；人工观测：2
     */
    TEM_EXT_TIME,

    /**
     * 露点温度
     */
    DPT,

    /**
     * // 水汽压
     */
    VAP,

    /**
     * 相对湿度观测标识 无观测：0；自动观测：1；人工观测：2
     */
    RHU,

    /**
     * 总云量观测标识 无观测：0；自动观测：1；人工观测：2
     */
    CLO_Cov,

    /**
     * 低云量观测标识 无观测：0；自动观测：1；人工观测：2
     */
    CLO_Cov_Low,

    /**
     * 云状观测标识 无观测：0；自动观测：1；人工观测：2
     */
    CLO_FOME,

    /**
     * 云高观测标识 无观测：0；自动观测：1；人工观测：2
     */
    CLO_Height,

    /**
     * 人工能见度观测标识 无观测：0；人工观测：2；自动观测：1
     */
    VIS,

    /**
     * 自动能见度观测标识 无观测：0；自动观测：1
     */
    VIS_AUTO,

    /**
     * 人工定时降水观测标识 无观测：0；人工观测：2
     */
    PRE,

    /**
     * 自记降水观测标识 无观测：0；自动观测：1
     */
    PRE_AUTO,

    /**
     * 自记降水开始停用及启动时间 开始停用月份：启用月份（11，02]
     */
    PRE_AUTO_TIME,

    /**
     * 天气现象观测标识 无观测：0；自动观测：1；人工观测：2
     */
    PHENOMENA,

    /**
     * 小型蒸发观测标识 无观测：0；人工观测：2
     */
    EVP_SMALL,

    /**
     * 大型蒸发(人工)观测标识 无观测：0；人工观测：2
     */
    EVP_BIG,

    /**
     * 大型蒸发(人工)开始停用及启动时间 开始停用月份+半角逗号+启用月份（比如，11月份停用，2月份启用则填写11,02，若无此项目填写半角逗号
     */
    EVP_BIG_TIME,

    /**
     * 自动蒸发观测标识 无观测：0；自动观测：1
     */
    EVP_AUTO,

    /**
     * 自动蒸发开始停用及启动时间 开始停用月份+半角逗号+启用月份（比如，11月份停用，2月份启用则填写11,02，若无此项目填写半角逗号
     */
    EVP_AUTO_TIME,

    /**
     * 雪深观测标识 无观测：0；自动观测：1；人工观测：2
     */
    SNOW_DEPTH,

    /**
     * 雪压观测标识 无观测：0；自动观测：1；人工观测：2
     */
    SNOW_PRS,

    /**
     * 电线积冰观测标识 无观测：0；自动观测：1；人工观测：2
     */
    EICE,

    /**
     * 定时风2分钟风观测标识无观测：0；自动观测：1；人工观测：2
     */
    WIN_2mi,

    /**
     * 自记风（10分钟风）观测标识 无观测：0；自动观测：1；人工观测：2
     */
    WIN_10mi,

    /**
     * 极大风观测标识 无观测：0；自动观测：1；人工观测：2
     */
    WIN_INST_Max,

    /**
     * 最大风观测标识 无观测：0；自动观测：1；人工观测：2
     */
    WIN_Max,

    /**
     * 地表温度观测标识 无观测：0；自动观测：1；人工观测：2
     */
    GST,

    /**
     * *cm地温观测标识 无观测：0；自动观测：1；人工观测：2
     */
    GST_5cm,
    GST_10cm,
    GST_15cm,
    GST_20cm,
    GST_40cm,
    GST_80cm,
    GST_160cm,
    GST_320cm,

    /**
     * 冻土深度观测标识 无观测：0；自动观测：1；人工观测：2
     */
    FRS,

    /**
     * 日照观测标识 无观测：0；自动观测：1；人工观测：2
     */
    SSH,

    /**
     * 草面（雪面）温度观测标识 无观测：0；自动观测：1；人工观测：2
     */
    LGST,

    /**
     * 地面状态观测标识 无观测：0；自动观测：1；人工观测：2
     */
    SCO,

    /**
     * 湿球温度观测标识	item_wett	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
     */
    WETT,

    /**
     * 地面状态观测标识	item_groundstate	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
     */
    GROUND_STATE,

    /**
     * 总辐射观测标识	item_totalradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
     */
    TOTAL_RADIA,

    /**
     * 净辐射观测标识	item_netradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
     */
    NET_DADIA,

    /**
     * 散射观测标识	item_scatterradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
     */
    SCATTER_RADIA,

    /**
     * 直接辐射观测标识	item_directradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
     */
    DIRECT_RADIA,

    /**
     * 反射观测标识	item_reflectradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
     */
    REFLECT_RADIA,

    /**
     * 紫外线状态观测标识	item_alstatusradia	nvarchar(1)	无观测：0；自动观测：1；人工观测：2
     */
    ALSTATUS_RADIA,

    /**
     * 风速
     */
    WIN_D,

    /**
     * 风向
     */
    WIN_S,

    /*********降水开始******************/
    /**
     * 小时雨量
     */
    PRE_1_HOUR,

    /**
     * 总降水量
     */
    PRE_MANUAL,

    /**
     * 总降水量
     */
    PRE_03_HOUR,

    /**
     * 总降水量
     */
    PRE_06_HOUR,

    /**
     * 总降水量
     */
    PRE_12_HOUR,

    /**
     * 总降水量
     */
    PRE_24_HOUR,
    /*********降水结束******************/

    /*********地温开始******************/
    /**
     * 每1小时内地面最低温度出现时间
     */
    GST_MIN_TIME,

    /**
     * 每1小时内地面最高温度出现时间
     */
    GST_MAX_TIME,

    /**
     * 每1小时内的地面最低温度
     */
    GST_MIN,

    /**
     * 每1小时内的地面最高温度
     */
    GST_MAX,

    /**
     * 过去12小时最低地面温度
     */
    GST_MIN_12,
    /*********地温结束******************/

    /*********电线积冰开始******************/
    /**
     * 电线积冰-现象
     */
    WIREICING,

    /**
     * 电线积冰-南北方向直径
     */
    NS_DIA,

    /**
     * 电线积冰-南北方向厚度
     */
    NS_PLY,

    /**
     * 电线积冰-南北方向重量
     */
    NS_WEIGHT,

    /**
     * 电线积冰-东西方向直径
     */
    WE_DIA,

    /**
     * 电线积冰-东西方向厚度
     */
    WE_PLY,

    /**
     * 电线积冰-东西方向重量
     */
    WE_WEIGHT,

    /**
     * 电线积冰-温度
     */
    T_WIREICING,

    /**
     * 电线积冰-风向
     */
    DDD_WIREICING,

    /**
     * 电线积冰-风速
     */
    F_WIREICING,
    /*********电线积冰结束******************/

    /*********温压湿开始******************/
    /**
     * 最低气温
     */
    TEM_MIN,

    /**
     * 最高气温
     */
    TEM_MAX,

    /**
     * 最低气温出现时间
     */
    TEM_MIN_TIME,

    /**
     * 最高气温出现时间
     */
    TEM_MAX_TIME,

    /**
     * 24小时变温
     */
    TEM_24,

    /**
     * 过去24小时最低气温
     */
    TEM_MIN_24,

    /**
     * 过去24小时最高气温
     */
    TEM_MAX_24,

    /**
     * 最小相对湿度
     */
    RHU_MIN,

    /**
     * 最小相对湿度出现时间
     */
    RHU_MIN_TIME,

    /**
     * 水汽压
     */
    VAP_PRS,

    /**
     * 露点温度
     */
    DEW_TEM,
    /*********温压湿结束******************/

    /*********气压开始******************/
    /**
     * 最低本站气压出现时间
     */
    PRS_MIN_TIME,

    /**
     * 最高本站气压出现时间
     */
    PRS_MAX_TIME,

    /**
     * 海平面气压
     */
    PRS_SEA,

    /**
     * 3小时变压
     */
    PRS_03,

    /**
     * 24小时变压
     */
    PRS_24,

    /**
     * 最高本站气压
     */
    PRS_MAX,

    /**
     * 最低本站气压
     */
    PRS_MIN,
    /*********气压结束******************/

    /*********冻土开始******************/
    /**
     * 冻土深度第1栏上限值
     */
    SOIL_FROZEN1_1,

    /**
     * 冻土深度第1栏下限值
     */
    SOIL_FROZEN1_2,

    /**
     * 冻土深度第2栏上限值
     */
    SOIL_FROZEN2_1,

    /**
     * 冻土深度第2栏下限值
     */
    SOIL_FROZEN2_2,
    /*********冻土结束******************/

    /*********蒸发开始******************/
    /**
     * 蒸发时间周期
     */
    TIME_PERIOD,

    /**
     * 测量蒸发的仪器类型或作物类型
     */
    DEV_TYPE_L,

    /**
     * 小时蒸发量
     */
    L,

    /**
     * 日蒸发量
     */
    L24,

    /**
     * 蒸发水位
     */
    L_WL,
    /*********蒸发结束******************/

    /*********风开始******************/
    /**
     * 2分钟风向
     */
    DDD02,

    /**
     * 2分钟平均风速
     */
    F02,

    /**
     * 10分钟风向
     */
    DDD10,

    /**
     * 10分钟平均风速
     */
    F10,

    /**
     * 最大风速的风向
     */
    DDDMAX,

    /**
     * 最大（10分钟）风速
     */
    FMAX,

    /**
     * 小时最大风速的时间
     */
    FMAX_TIME,

    /**
     * 瞬时风向
     */
    DDDINS,

    /**
     * 瞬时风速
     */
    FINS,

    /**
     * 极大风速的风向
     */
    DDDMOST,

    /**
     * 极大风速
     */
    FMOST,

    /**
     * 小时极大风速的时间
     */
    FMOST_TIME,

    /**
     * 6小时最大瞬时风速的风向
     */
    DDDINS06,

    /**
     * 6小时最大瞬时风速
     */
    FINS06,

    /**
     * 12小时最大瞬时风速的风向
     */
    DDDINS12,

    /**
     * 12小时最大瞬时风速
     */
    FINS12,
    /*********风结束******************/

    /*********路温开始******************/
    /**
     * 路面温度
     */
    RT,

    /**
     * 10cm路基温度
     */
    RT10,

    /**
     * 小时最高路面温度
     */
    RT_Max,

    /**
     * 小时最高路面温度时间
     */
    TIME_HOUR_RTMAX,

    /**
     * 分钟最高路面温度时间
     */
    TIME_MINUTE_RTMAX,

    /**
     * 一阶统计
     */
    FIRST_STA_RTMAX,

    /**
     * 一阶统计（缺省）0
     */
    FIRST_STA_DEF0,

    /**
     * 一阶统计（最小值）
     */
    FIRST_STA_RTMIN,

    /**
     * 最低路温
     */
    RT_MIN,

    /**
     * 一阶统计（缺省）1
     */
    FIRST_STA_DEF1,

    /**
     * 小时最低路面温度时间
     */
    TIME_HOUR_RTMIN,

    /**
     * 分钟最高路面温度时间
     */
    TIME_MINUTE_RTMIN,

    /*********路温结束******************/

    /*********能见度开始******************/
    /**
     * 后面值的属性
     */
    BACK_ATR,

    /**
     * 能见度
     */
    V,

    /**
     * 能见度,这个是拿来规避判断的出自"有自动能见度时必须有人工能见度, 无此关联性临时将v改为vvvv规避此判断"
     */
    VVVV,

    /**
     * 时间意义
     */
    TIME_MEAN_V0,

    /**
     * 时间意义(缺省）
     */
    TIME_MEAN_DEF0,

    /**
     * 1分钟、10分钟平均能见度时间意义
     */
    TIME_PERIOD_V01_10,

    /**
     * 1分钟平均水平能见度
     */
    V01,

    /**
     * 10分钟平均水平能见度
     */
    V10,

    /**
     * 时间周期
     */
    TIME_PERIOD_VMIN,

    /**
     * 小时内最小水平能见度
     */
    FIRST_STA_VMIN,

    /**
     * 小时最小能见度
     */
    VMIN,

    /**
     * 小时最小能见度时间
     */
    TIME_HOUR_VMIN,

    /**
     * 分钟最小能见度时间
     */
    TIME_MINUTE_VMIN,
    /*********能见度结束******************/

    /*********草面温度开始******************/
    /***/
    FIRST_STA_TGMAX,

    /**
     *
     */
    TIME_PERIOD_TGMAX,

    /**
     * 小时最高草面温度
     */
    TGMAX,

    /**
     * 小时最高草面温度时间
     */
    TIME_HOUR_TGMAX,

    /**
     * 小时最高草面温度时间
     */
    TIME_MINUTE_TGMAX,

    /***/
    FIRST_SAT_TGMIN,

    /***/
    TIME_PERIOD_TGMIN,

    /**
     * 小时最低草面温度
     */
    TGMIN,

    /**
     * 小时最低草面温度时间
     */
    TIME_HOUR_TGMIN,

    /**
     * 小时最低草面温度时间
     */
    TIME_MINUTE_TGMIN,
    /*********草面温度结束******************/

    /*********天气现象开始******************/
    /**
     * 龙卷尘卷风距测站距离编码
     */
    TORNADO_DISTANCE,

    /**
     * 龙卷尘卷风距测站方位编码
     */
    TORNADO_POSITION,

    /**
     * 电线积冰（雨凇）直径
     */
    WIREICINGDIA,

    /**
     * 最大冰雹直径
     */
    MAXHAILDIA,

    /**
     * 现在天气现象编码
     */
    PHENOMENACODE,

    /**
     * 过去天气描述时间周期
     */
    TIMECYCLE,

    /**
     * 过去天气01
     */
    PASTWEATHER_01,

    /**
     * 过去天气02
     */
    PASTWEATHER_02,
    /*********气现象结束******************/

    /*********路面状态开始******************/
    /**
     * 路面状态
     */
    ROAD_STATE,

    /**
     * 路面雪层厚度
     */
    DEPTH_SNOW,

    /**
     * 路面水层厚度
     */
    DEPTH_WATER,

    /**
     * 路面冰层厚度
     */
    DEPTH_ICE,

    /**
     * 路面冰点温度
     */
    T_ICEPOINT,

    /**
     * 融雪剂浓度
     */
    SNOWMELT_CON,

    /*********路面状态结束******************/

    /*********云开始******************/
    /**
     * 云探测系统
     */
    CLO_SYS,

    /**
     * 编报云量
     */
    CLOUD,

    /**
     * 垂直探测意义0
     */
    VER_MEAN0,

    /**
     * 垂直探测意义1
     */
    VER_MEAN1,


    /**
     * 云类型
     */
    CLO_CLASS,


    /*********云结束******************/

    /**
     * 雪压观测标识 无观测：0；自动观测：1；人工观测：2
     */
    SNOW_METHOD,

    /*********日照开始******************/
    /**
     * 日照年
     */
    SS_YEAR,

    /**
     * 日照月
     */
    SS_MON,

    /**
     * 日照日
     */
    SS_DAY,

    /**
     * 日照时间周期1
     */
    TIME_PERIOD_SS0,

    /**
     * 日照时间周期2
     */
    TIME_PERIOD_SS1,

    /**
     * 小时日照时数
     */
    SS_TIME,

    /**
     * 总日照时间周期
     */
    TIME_PERIOD_SS24,

    /**
     * 日照时数日合计
     */
    SS24,
    /*********日照结束******************/
}
