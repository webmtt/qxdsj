package cma.cimiss2.dpc.quickqc.surf.awshour;

import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
import cma.cimiss2.dpc.quickqc.bean.MaxMinValues;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
import cma.cimiss2.dpc.quickqc.util.SurfUtil;

import java.time.LocalDateTime;

import static cma.cimiss2.dpc.quickqc.bean.Constants.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.ElementRange.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.QualityCode.*;
import static cma.cimiss2.dpc.quickqc.util.CommonUtil.*;


/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description: range检查类  </b><br>
 * // 继承了BaseCheck 中默认实现的 default void rangeCheck(MeteoElement element,
 * double obsValue,
 * LocalDateTime datetime,
 * BaseStationInfo obs,
 * double factorDoubt,
 * double factorError,
 * QCEle<Double> obsCode) 方法
 *
 * @author dengzhiheng
 * @version 1.0
 * @Note <b>ProjectName:</b> cimiss2-quick-qc
 * <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.surf.awshour
 * <br><b>ClassName:</b> RangeCheck
 * <br><b>Date:</b> 2019年8月1日 下午2:32:11
 */
public class RangeCheck implements BaseCheck {
    /**
     * 压温湿要素一致性检查
     *
     * @param obsValue : 观测值
     * @param element  :质控要素
     * @param dateTime : 时次
     * @param obs      :台站信息
     */
    public static QualityCode rangeCheck(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        //缺测检查
        QualityCode ret = Z_QC_NO_QC;
        if (BaseCheck.isEleLack(obsValue)) {
            setLogger(element, QC_TYPE_RANGE, ret,obsValue);
            return ret;
        }
        //入参检查
        checkAndGetStationCode(dateTime, element, obs, QC_TYPE_LIMIT);
        //微量元素赋值0
        if (obsValue == TINY_NUM) {
            obsValue = 0;
        }
        // 台站配置配置
        BaseStationInfo conf = CONFIG_MAP.get(obs.getStationCode());
        if (conf == null) {
            LOGGER.error(QC_TYPE_RANGE + CONFIG_NULL);
            throw new RuntimeException(QC_TYPE_RANGE + CONFIG_NULL);
        }

        switch (element) {
            // 温压湿
            case TEM:
            case TEM_MAX:
            case TEM_MIN:
            case TEM_MAX_24:
            case TEM_MIN_24:
                // 阈值配置
                MaxMinValues maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, TEM, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_T_LOW.getValue(), RANGE_T_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case RHU:
            case RHU_MIN:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, RHU, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_U_LOW.getValue(), RANGE_U_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            // 气压
            case PRS:
            case PRS_MAX:
            case PRS_MIN:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, PRS, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_P_LOW.getValue(), RANGE_P_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            // 降雨
            case PRE_1_HOUR:
                ret = preRangeCheck(obsValue, RANGE_R_LOW.getValue(), RANGE_R_HIGH.getValue(), element, dateTime, obs);
                if (BaseCheck.isEleCheckable(ret)) {
                    maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                    ret = windPreRangeCheck(obsValue, RANGE_fR_LOW.getValue(), RANGE_FR_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                }
                break;
            case PRE_03_HOUR:
                maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_R03_LOW.getValue(), RANGE_R03_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case PRE_06_HOUR:
                maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_R06_LOW.getValue(), RANGE_R06_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case PRE_12_HOUR:
                maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_R12_LOW.getValue(), RANGE_R12_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case PRE_24_HOUR:
                maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_R24_LOW.getValue(), RANGE_R24_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            // 风向、风速
            case FINS:
            case FINS06:
            case FINS12:
            case FMOST:
                maxMinValues = getMaxMinValues(obsValue, WIN_D, dateTime, conf, BaseCheck.F_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_FFF_LOW.getValue(), RANGE_FFF_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case F02:
            case F10:
                maxMinValues = getMaxMinValues(obsValue, WIN_D, dateTime, conf, BaseCheck.F_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_F02_F10_LOW.getValue(), RANGE_F02_F10_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case FMAX:
                maxMinValues = getMaxMinValues(obsValue, WIN_D, dateTime, conf, BaseCheck.F_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_FMAX_LOW.getValue(), RANGE_FMAX_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            // 土壤温度
            case GST:
            case GST_MAX:
            case GST_MIN:
            case GST_MIN_12:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, GST, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D0_LOW.getValue(), RANGE_D0_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case GST_5cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D05_LOW.getValue(), RANGE_D05_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case GST_10cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D10_LOW.getValue(), RANGE_D10_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case GST_15cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D15_LOW.getValue(), RANGE_D15_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case GST_20cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D20_LOW.getValue(), RANGE_D20_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case GST_40cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D40_LOW.getValue(), RANGE_D40_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case GST_80cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D80_LOW.getValue(), RANGE_D80_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case GST_160cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D160_LOW.getValue(), RANGE_D160_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case GST_320cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D320_LOW.getValue(), RANGE_D320_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            // 草面温度
            case TG:
            case TGMAX:
            case TGMIN:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, TG, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_TG_LOW.getValue(), RANGE_TG_HIGH.getValue(), element, dateTime, obs, maxMinValues);
                break;
            case PHENOMENACODE:
                ret = checkPheRange(obsValue, element, dateTime, obs);
                break;
            case WIREICING:
                ret = wireRangeCheck(obsValue, element, dateTime, obs);
                break;
            case SSH:
                ret = ssRangeCheck(obsValue, element, dateTime, obs);
                break;
            default:
                ret = Z_QC_NO_QC;
                break;
        }
        setLogger(element, QC_TYPE_RANGE, ret,obsValue);
        return ret;
    }


    public static QualityCode rangeCheck(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        //缺测检查
        QualityCode ret = Z_QC_NO_QC;
        if (!BaseCheck.isEleCheckable(preCode)) {
            //表示调用了，但是不满足质控条件，日志记录为未做质控，同时返回传入的质控码
            setLogger(element, QC_TYPE_RANGE, ret,obsValue);
            return preCode;
        }
        //入参检查
        checkAndGetStationCode(dateTime, element, obs, QC_TYPE_LIMIT);
        //微量元素赋值0
        if (obsValue == TINY_NUM) {
            obsValue = 0;
        }
        // 台站配置配置
        BaseStationInfo conf = CONFIG_MAP.get(obs.getStationCode());
        if (conf == null) {
            LOGGER.error(QC_TYPE_RANGE + CONFIG_NULL);
            throw new RuntimeException(QC_TYPE_RANGE + CONFIG_NULL);
        }

        switch (element) {
            // 温压湿
            case TEM:
            case TEM_MAX:
            case TEM_MIN:
            case TEM_MAX_24:
            case TEM_MIN_24:
                // 阈值配置
                MaxMinValues maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, TEM, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_T_LOW.getValue(), RANGE_T_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case RHU:
            case RHU_MIN:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, RHU, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_U_LOW.getValue(), RANGE_U_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            // 气压
            case PRS:
            case PRS_MAX:
            case PRS_MIN:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, PRS, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_P_LOW.getValue(), RANGE_P_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            // 降雨
            case PRE_1_HOUR:
                ret = preRangeCheck(obsValue, RANGE_R_LOW.getValue(), RANGE_R_HIGH.getValue(), element, dateTime, obs, preCode);
                if (BaseCheck.isEleCheckable(ret)) {
                    maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                    ret = windPreRangeCheck(obsValue, RANGE_fR_LOW.getValue(), RANGE_FR_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                }
                break;
            case PRE_03_HOUR:
                maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_R03_LOW.getValue(), RANGE_R03_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case PRE_06_HOUR:
                maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_R06_LOW.getValue(), RANGE_R06_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case PRE_12_HOUR:
                maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_R12_LOW.getValue(), RANGE_R12_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case PRE_24_HOUR:
                maxMinValues = getMaxMinValues(obsValue, PRE, dateTime, conf, BaseCheck.R_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_R24_LOW.getValue(), RANGE_R24_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            // 风向、风速
            case FINS:
            case FINS06:
            case FINS12:
            case FMOST:
                maxMinValues = getMaxMinValues(obsValue, WIN_D, dateTime, conf, BaseCheck.F_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_FFF_LOW.getValue(), RANGE_FFF_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case F02:
            case F10:
                maxMinValues = getMaxMinValues(obsValue, WIN_D, dateTime, conf, BaseCheck.F_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_F02_F10_LOW.getValue(), RANGE_F02_F10_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case FMAX:
                maxMinValues = getMaxMinValues(obsValue, WIN_D, dateTime, conf, BaseCheck.F_MAX_MON);
                ret = windPreRangeCheck(obsValue, RANGE_FMAX_LOW.getValue(), RANGE_FMAX_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            // 土壤温度
            case GST:
            case GST_MAX:
            case GST_MIN:
            case GST_MIN_12:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, GST, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D0_LOW.getValue(), RANGE_D0_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case GST_5cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D05_LOW.getValue(), RANGE_D05_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case GST_10cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D10_LOW.getValue(), RANGE_D10_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case GST_15cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D15_LOW.getValue(), RANGE_D15_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case GST_20cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D20_LOW.getValue(), RANGE_D20_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case GST_40cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D40_LOW.getValue(), RANGE_D40_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case GST_80cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D80_LOW.getValue(), RANGE_D80_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case GST_160cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D160_LOW.getValue(), RANGE_D160_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case GST_320cm:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_D320_LOW.getValue(), RANGE_D320_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            // 草面温度
            case TG:
            case TGMAX:
            case TGMIN:
                // 阈值配置
                maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, TG, obsValue, dateTime, T_MAX_MIN);
                ret = rangeCheck(obsValue, RANGE_TG_LOW.getValue(), RANGE_TG_HIGH.getValue(), element, dateTime, obs, maxMinValues, preCode);
                break;
            case PHENOMENACODE:
                ret = checkPheRange(obsValue, element, dateTime, obs, preCode);
                break;
            case WIREICING:
                ret = wireRangeCheck(obsValue, element, dateTime, obs, preCode);
                break;
            case SSH:
                ret = ssRangeCheck(obsValue, element, dateTime, obs, preCode);
                break;
            default:
                ret = Z_QC_NO_QC;
                break;
        }
        setLogger(element, QC_TYPE_RANGE, ret,obsValue);
        return ret;
    }

    /**
     * 范围质控通用方法，返回质控码，如果方法内部没有进行质控，就返回no_QC
     *
     * @param obsValue     : 观测值
     * @param factorDoubt  :可疑因子
     * @param factorError  :错误因子
     * @param element      :观测元素
     * @param dateTime     :时间
     * @param obs          :观测站信息
     * @param maxMinValues :阈值
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 11:18 AM
     */
    private static QualityCode rangeCheck(double obsValue, double factorDoubt, double factorError, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, MaxMinValues maxMinValues) {
        return getRangeQualityCode(obsValue, factorDoubt, factorError, element, maxMinValues);
    }


    /**
     * @param obsValue     :  观测值
     * @param factorDoubt  : 可疑因子
     * @param factorError  : 错误因子
     * @param element      : 观测元素
     * @param dateTime     : 时间
     * @param obs          : 观测站信息
     * @param maxMinValues : 阈值
     * @param preCode      : 上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 11:31 AM
     */
    private static QualityCode rangeCheck(double obsValue, double factorDoubt, double factorError, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, MaxMinValues maxMinValues, QualityCode preCode) {
        QualityCode ret = getRangeQualityCode(obsValue, factorDoubt, factorError, element, maxMinValues);
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, RANGE);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }

    /**
     * @param obsValue     :  观测值
     * @param factorDoubt  : 可疑因子
     * @param factorError  : 错误因子
     * @param element      : 观测元素
     * @param maxMinValues : 时间
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 11:30 AM
     */
    private static QualityCode getRangeQualityCode(double obsValue, double factorDoubt, double factorError, MeteoElement element, MaxMinValues maxMinValues) {
        double errorStdev = factorError * maxMinValues.getExceedingThreshold();
        double doubtStdev = factorDoubt * maxMinValues.getExceedingThreshold();
        QualityCode ret;
        /*湿度不做最大值质控*/
        if (obsValue > maxMinValues.getMaxValue() + errorStdev && RHU != element) {
            ret = Z_QC_ERROR;
        } else if (obsValue > maxMinValues.getMaxValue() + doubtStdev && RHU != element) {
            ret = QualityCode.Z_QC_DOUBT;
        } else if (obsValue < maxMinValues.getMinValue() - errorStdev) {
            ret = QualityCode.Z_QC_ERROR_NEG;
        } else if (obsValue < maxMinValues.getMinValue() - factorDoubt) {
            ret = QualityCode.Z_QC_DOUBT_NEG;
        } else {
            ret = QualityCode.Z_QC_OK;
        }

        return ret;
    }


    /**
     * 日出之前和日出之后无日照range检查
     *
     * @param obsValue : 观测值
     * @param element  : 观测元素
     * @param dateTime : 时间
     * @param obs      : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 5:48 PM
     */
    private static QualityCode ssRangeCheck(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        if ((obsValue > -EPTION) && (obsValue < EPTION)) {
            return Z_QC_OK;
        } else {
            return Z_QC_ERROR;
        }
    }

    private static QualityCode ssRangeCheck(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret;
        if ((obsValue > -EPTION) && (obsValue < EPTION)) {
            ret = Z_QC_OK;
        } else {
            ret = Z_QC_ERROR;
        }
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, RANGE);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }


    /**
     * 电线积冰范围检查
     * 检测电线积冰天气现象范围值0/48/56，没做质控返回no_QC
     *
     * @param obsValue : 观测值
     * @param element  : 观测要素
     * @param datetime : 时间
     * @param obs      : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 5:51 PM
     */
    private static QualityCode wireRangeCheck(double obsValue, MeteoElement element, LocalDateTime datetime, BaseStationInfo obs) {
        //不为0/48或56
        if (Math.abs(obsValue - 0) > EPTION && Math.abs(obsValue - WIRE1) > EPTION && Math.abs(obsValue - WIRE2) > EPTION) {
            return Z_QC_ERROR;
        } else {
            return Z_QC_NO_QC;
        }
    }

    /**
     * 电线积冰范围检查
     * 检测电线积冰天气现象范围值0/48/56，没做质控返回no_QC
     *
     * @param obsValue :观测值
     * @param element  : 观测要素
     * @param dateTime :时间
     * @param obs      : 观测站信息
     * @param preCode  : 上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 3:00 PM
     */
    private static QualityCode wireRangeCheck(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret;
        //不为0/48或56
        if (Math.abs(obsValue - 0) > EPTION && Math.abs(obsValue - WIRE1) > EPTION && Math.abs(obsValue - WIRE2) > EPTION) {
            ret = Z_QC_ERROR;
        } else {
            ret = Z_QC_NO_QC;
        }
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, RANGE);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }

    /**
     * 针对降水的range质控
     *
     * @param obsValue    : 观测值
     * @param factorDoubt : 可疑因子
     * @param factorError : 错误因子
     * @param element     : 观测要素
     * @param dateTime    : 时间
     * @param obs         : 观测值信息
     * @return : void
     * @author : When6passBye
     * @date : 2019/8/20 5:42 PM
     */
    private static QualityCode preRangeCheck(double obsValue, double factorDoubt, double factorError, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        return setRangeCheckCode(obsValue, element, factorError, factorDoubt, dateTime, obs);
    }

    /**
     * 针对降水的range质控
     *
     * @param obsValue    : 观测值
     * @param factorDoubt : 可疑因子
     * @param factorError : 错误因子
     * @param element     : 观测要素
     * @param dateTime    : 时间
     * @param obs         : 观测值信息
     * @param preCode     : 上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 3:08 PM
     */
    private static QualityCode preRangeCheck(double obsValue, double factorDoubt, double factorError, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret = setRangeCheckCode(obsValue, element, factorError, factorDoubt, dateTime, obs);
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, RANGE);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }


    /**
     * 风、雨的范围值检查
     *
     * @param obsValue      : 观测值
     * @param factorDoubt   : 可疑因子
     * @param factorError   : 错误因子
     * @param element       : 观测要素
     * @param dateTime      : 时间
     * @param obs           : 观测站信息
     * @param maxMinValues: 观测站阈值
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 5:58 PM
     */
    private static QualityCode windPreRangeCheck(double obsValue, double factorDoubt, double factorError, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, MaxMinValues maxMinValues) {

        double errorStdev = factorError * maxMinValues.getExceedingThreshold();
        double doubtStdev = factorDoubt * maxMinValues.getExceedingThreshold();
        return setRangeCheckCode(obsValue, element, errorStdev, doubtStdev, dateTime, obs);
    }

    private static QualityCode windPreRangeCheck(double obsValue, double factorDoubt, double factorError, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, MaxMinValues maxMinValues, QualityCode preCode) {

        double errorStdev = factorError * maxMinValues.getExceedingThreshold();
        double doubtStdev = factorDoubt * maxMinValues.getExceedingThreshold();
        System.out.println(element.toString()+" ==> errorStdev="+"factorError:"+factorError+"*"+"ExceedingThreshold:"+maxMinValues.getExceedingThreshold());
        System.out.println(element.toString()+" ==> doubtStdev="+"factorDoubt:"+factorError+"*"+"ExceedingThreshold:"+maxMinValues.getExceedingThreshold());
        return setRangeCheckCode(obsValue, element, errorStdev, doubtStdev, dateTime, obs, preCode);
    }


    /**
     * 设置rangecheck的质控码，返回质控码结果
     *
     * @param obsValue   : 观测值
     * @param element    : 质控要素
     * @param errorStdev : 错误因子
     * @param doubtStdev : 可疑因子
     * @param datetime   : 时间
     * @param obs        : 观测台站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 5:40 PM
     */
    private static QualityCode setRangeCheckCode(double obsValue, MeteoElement element, double errorStdev, double doubtStdev, LocalDateTime datetime, BaseStationInfo obs) {
        return setRangeCode(obsValue, errorStdev, doubtStdev);
    }

    /**
     * 设置rangecheck的质控码，返回质控码结果
     *
     * @param obsValue   : 观测值
     * @param element    : 质控要素
     * @param errorStdev :错误因子
     * @param doubtStdev :可疑因子
     * @param datetime   : 时间
     * @param obs        : 观测台站信息
     * @param preCode    : 上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 11:42 AM
     */
    private static QualityCode setRangeCheckCode(double obsValue, MeteoElement element, double errorStdev, double doubtStdev, LocalDateTime datetime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret = setRangeCode(obsValue, errorStdev, doubtStdev);
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), datetime, element, preCode, ret, RANGE);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }

    /**
     * @param obsValue   :
     * @param errorStdev :
     * @param doubtStdev :
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 11:42 AM
     */
    private static QualityCode setRangeCode(double obsValue, double errorStdev, double doubtStdev) {
        if (obsValue > errorStdev) {
            return Z_QC_ERROR;
        } else if (obsValue > doubtStdev) {
            return Z_QC_DOUBT;
        } else {
            return Z_QC_OK;
        }
    }

    /**
     * 现在天气现象编码范围值检查，正点检查
     *
     * @param element  : 观测要素
     * @param obsValue : 观测值
     * @param dateTime : 时间
     * @param obs      : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 6:32 PM
     */
    private static QualityCode checkPheRange(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        return getPheRangeQualityCode(obsValue);
    }

    /**
     * 现在天气现象编码范围值检查，正点检查，多一个上一步质控码入参
     *
     * @param obsValue : 观测值
     * @param element  : 观测要素
     * @param dateTime : 时间
     * @param obs      : 观测站信息
     * @param preCode  : 上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 11:40 AM
     */
    private static QualityCode checkPheRange(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret = getPheRangeQualityCode(obsValue);
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, RANGE);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;

    }

    /**
     * 天气现象Range质控
     *
     * @param obsValue : 观测值
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 11:37 AM
     */
    private static QualityCode getPheRangeQualityCode(double obsValue) {
        if (obsValue >= 0 && obsValue <= 90) {
            //不为4/8/13/17等值，编码正确
            if (Math.abs(obsValue - 4) > EPTION && Math.abs(obsValue - 8) > EPTION
                    && Math.abs(obsValue - 13) > EPTION && Math.abs(obsValue - 17) > EPTION
                    && Math.abs(obsValue - 18) > EPTION && Math.abs(obsValue - 19) > EPTION
                    && Math.abs(obsValue - 29) > EPTION && Math.abs(obsValue - 38) > EPTION
                    && Math.abs(obsValue - 39) > EPTION && Math.abs(obsValue - 76) > EPTION
                    && Math.abs(obsValue - 77) > EPTION && Math.abs(obsValue - 79) > EPTION
                    && Math.abs(obsValue - 87) > EPTION && Math.abs(obsValue - 88) > EPTION
            ) {
                return Z_QC_OK;
            } else {
                return Z_QC_ERROR;
            }
        } else {
            return Z_QC_NO_QC;
        }
    }

    /**
     * Is ss range check able boolean.
     * 对日出之后且日落之前的的元素不进行非零检查
     *
     * @param index : 时序（角标）
     * @param trise :
     * @param tset  :
     * @return : bool值,true表示需要检查
     * @author : When6passBye
     * @date : 2019/7/30 5:25 PM
     */
    private boolean isSsRangeCheckAble(int index, double trise, double tset) {
        int ret = 1;
        int tr = (int) trise;
        int ts = (int) tset;
        if (index >= tr && index <= ts) {
            ret = 0;
        }
        return ret > 0;
    }
}
