package cma.cimiss2.dpc.quickqc.bean;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description:</b><br>
 * @author dengzhiheng
 * @version 1.0
 * @Note <b>ProjectName: 快速质量控制</b> cimiss2-quick-qc
 * <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc
 * <br><b>ClassName: 全局常量  </b> Constants
 * <br><b>Date:</b> 2019-07-19 14:54
 */
public final class Constants {

    private Constants() {
    }

    /**
     * 微量值界限
     *
     * */
    public static final double EPTION = 0.000001;
    /**
     * 微量值
     *
     * */
    public static final double TINY = 0.1;
    /**
     * 微量值
     *
     * */
    public static final double TINY2 = 0.2;
    public static final double T_K_ADD = 273.2;
    public static final int DES_LEN_MAX = 50;

    public static final int PP_ELE_NUM = 2;
    public static final int TH_ELE_NUM = 4;
    public static final int RE_ELE_NUM = 1;
    public static final int WI_ELE_NUM = 13;
    public static final int DT_ELE_NUM = 18;
    public static final int TG_ELE_NUM = 1;
    public static final int VV_ELE_NUM = 6;
    public static final int CW_ELE_NUM = 2;
    public static final int SP_ELE_NUM = 1;
    public static final int MW_ELE_NUM = 1;
    public static final int PP_DES_NUM = 2;
    public static final int TH_DES_NUM = 4;
    public static final int RE_DES_NUM = 1;
    public static final int WI_DES_NUM = 8;
    public static final int DT_DES_NUM = 5;
    public static final int TG_DES_NUM = 1;
    public static final int VV_DES_NUM = 5;
    public static final int CW_DES_NUM = 2;
    public static final int SP_DES_NUM = 1;
    public static final int MW_DES_NUM = 1;
    /**
     * 无意义的值
     *
     * */
    public static final int NO_MEANS = -99;
    /**
     * 表示缺测质控步骤，日志使用
     *
     * */
    public static final int LACK = 0;
    /**
     * 表示界限质控步骤，日志使用
     *
     * */
    public static final int LIMIT = 1;
    /**
     * 表示范围质控步骤，日志使用
     *
     * */
    public static final int RANGE = 2;
    /**
     * 表示一致性质控步骤，日志使用
     *
     * */
    public static final int INTERNAL = 3;
    /**
     * 表示文件级质控步骤，日志使用
     *
     * */
    public static final int FILE = 4;
    /**
     * 表示文件中缺测的值
     *
     * */
    public static final double LACK_NUM = 999999D;
    /**
     * 表示文件中应该有值但是缺测的值
     *
     * */
    public static final double NOTASK_NUM = 999998D;
    /**
     * 表示文件中应该有值但是是微量的值
     *
     * */
    public static final double TINY_NUM = 999990D;
    /**
     * 质控类型missingCheck
     *
     * */
    public static final String QC_TYPE_LACK = "missingCheck";
    /**
     * 质控类型limitCheck
     *
     * */
    public static final String QC_TYPE_LIMIT = "limitCheck";
    /**
     * 质控类型rangeCheck
     *
     * */
    public static final String QC_TYPE_RANGE = "rangeCheck";
    /**
     * 质控类型internalCheck
     *
     * */
    public static final String QC_TYPE_INTERNAL = "internalCheck";
    /**
     * 质控类型fileCheck
     *
     * */
    public static final String QC_TYPE_FILE = "fileCheck";
    /**
     * 分隔符1
     *
     * */
    public static final String SEG = " ==> ";
    /**
     * 分隔符2
     *
     * */
    public static final String SEG2 = " : ";
    /**
     * 分隔符3
     *
     * */
    public static final String SEG3 = " - ";
    /**
     * 表示文件级质控步骤，日志使用
     *
     * */
    public static final int WIRE1 = 48;
    /**
     * 表示文件级质控步骤，日志使用
     *
     * */
    public static final int WIRE2 = 56;
    /**
     * 表示一天中的小时数
     *
     * */
    public static final int SSTIME = 24;
    /**
     * 两站间的最大距离
     *
     * */
    public static final double MAX_DISTANCE = 99999.9;
    /**
     * 两站间的最大距离
     *
     * */
    public static final double RIGHT_ANGLE = 180.0;
    /**
     * 海平面气压一致性检测界限值1
     *
     * */
    public static final double SEAP_LIMIT1 = 1500.0;
    /**
     * 海平面气压一致性检测界限值2
     *
     * */
    public static final double SEAP_LIMIT2 = 2500.0;
    /**
     * 露点温度一致性检测界限值1
     *
     * */
    public static final double DPT_LIMIT1 = -15.0;
    /**
     * 露点温度一致性检测界限值2
     *
     * */
    public static final double DPT_LIMIT2 = -25.0;
    /**
     * 水汽压一致性检测界限值2
     *
     * */
    public static final double VAP_LIMIT = 8.0;
    /**
     * 进行limit界限传值的时候的一般除数
     *
     * */
    public static final double DIVISOR = 10.0;
    /**
     * 无观测任务
     *
     * */
    public static final int NOTASK = 0;
    /**
     * 自动观测
     *
     * */
    public static final int AUTO = 1;
    /**
     * 人工观测
     *
     * */
    public static final int HUMAN = 2;
    /**
     * 观测站类型：基准站
     *
     * */
    public static final int BENCHMARK = 1;
    /**
     * 观测站类型：基本站
     *
     * */
    public static final int BASIC = 2;
    /**
     * 观测站类型：一般站（4次）
     *
     * */
    public static final int NORMAL4T = 3;
    /**
     * 观测站类型：一般站（3次）
     *
     * */
    public static final int NORMAL3T = 4;
    /**
     * 观测站类型：无人自动站
     *
     * */
    public static final int NOMAN = 5;

    /**
     * 人工观测站定时时次：8
     *
     * */
    public static final int MAN_OBS_TIME1 = 8;
    /**
     * 人工观测站定时时次：11
     *
     * */
    public static final int MAN_OBS_TIME2 = 11;
    /**
     * 人工观测站定时时次：14
     *
     * */
    public static final int MAN_OBS_TIME3 = 14;
    /**
     * 人工观测站定时时次：17
     *
     * */
    public static final int MAN_OBS_TIME4 = 17;
    /**
     * 人工观测站定时时次：20
     *
     * */
    public static final int MAN_OBS_TIME5 = 20;
    /**
     * 日志:记录读取过的文件
     *
     * */
    public static final String MESSAGE_INFO = "messageInfo";
    /**
     * 日志:地面数据质控日志
     *
     * */
    public static final String QC_LOGGER = "qcLogger";
    /**
     * 日志:站点配置为null
     *
     * */
    public static final String CONFIG_NULL = ":get station config failure,config is null";
    /**
     * 日志:参数错误
     *
     * */
    public static final String PARAMS_INVALID = ":params is invalid.";
    /**
     * 日志:站点编号错误
     *
     * */
    public static final String STATIONCODE_INVALID = ":stationCode is invalid.";
}

