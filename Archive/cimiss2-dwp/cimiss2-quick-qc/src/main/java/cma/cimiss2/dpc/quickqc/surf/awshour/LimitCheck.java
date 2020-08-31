package cma.cimiss2.dpc.quickqc.surf.awshour;

import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
import cma.cimiss2.dpc.quickqc.bean.enums.ElementBound;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;

import java.time.LocalDateTime;

import static cma.cimiss2.dpc.quickqc.bean.Constants.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.CommonValue.DDDINS_MAX;
import static cma.cimiss2.dpc.quickqc.bean.enums.CommonValue.DDDINS_MIN;
import static cma.cimiss2.dpc.quickqc.bean.enums.ElementBound.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.QualityCode.Z_QC_NO_QC;
import static cma.cimiss2.dpc.quickqc.util.CommonUtil.LOGGER;
import static cma.cimiss2.dpc.quickqc.util.CommonUtil.checkAndGetStationCode;
import static cma.cimiss2.dpc.quickqc.util.CommonUtil.setLogger;


/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description: limit检查类  </b><br>
 * // 继承了BaseCheck 中默认实现的 default void limitCheck(double value, double left, double right, QCEle<Double>  MeteoElement obsElement, BaseStationInfo obs, LocalDateTime datetime)方法
 *
 * @author dengzhiheng
 * @version 1.0
 * @Note <b>ProjectName:</b> cimiss2-quick-qc <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.surf.awshour <br><b>ClassName:</b> LimitCheck <br><b>Date:</b> 2019年8月1日 下午2:32:11
 */
public class LimitCheck implements BaseCheck {


    /**
     * 小时地面要素Limit质控共用方法
     *
     * @param obsValue 观测值
     * @param element  观测要素
     * @param dateTime 资料时间
     * @param obs      站点信息
     */
    public static QualityCode limitCheck(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        //缺测检查
        QualityCode ret = Z_QC_NO_QC;
        if (BaseCheck.isEleLack(obsValue)) {
            setLogger(element, QC_TYPE_LIMIT, ret, obsValue);
            return ret;
        }
        //入参检查
        checkAndGetStationCode(dateTime, element, obs, QC_TYPE_LIMIT);
        //微量元素赋值0
        if (obsValue == TINY_NUM) {
            obsValue = 0;
        }
        //所有没写break的继续执行到break之前
        switch (element) {
            //继续执行
            //电线结冰,
            case NS_DIA:
            case WE_DIA:
            case NS_PLY:
            case WE_PLY:
                ret = limitCheck(obsValue, BOUND_WIREICEINGDIA_LOW.getValue(), BOUND_WIREICEINGDIA_DOUBT.getValue(), dateTime, element, obs);
                break;
            case NS_WEIGHT:
            case WE_WEIGHT:
                ret = limitCheck(obsValue, BOUND_WIREICINGWEIGHT_LOW.getValue(), BOUND_WIREICINGWEIGHT_HIGH.getValue(), dateTime, element, obs);
                break;
            case T_WIREICING:
                ret = limitCheck(obsValue, BOUND_WIREICINGT_LOW.getValue() / DIVISOR, BOUND_WIREICINGT_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case DDD_WIREICING:
                ret = limitCheck(obsValue, BOUND_WIREICINGDDD_LOW.getValue(), BOUND_WIREICINGDDD_LOW.getValue(), dateTime, element, obs);
                break;
            case F_WIREICING:
                ret = limitCheck(obsValue, BOUND_WIREICINGFFF_LOW.getValue() / DIVISOR, BOUND_WIREICINGFFF_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            //风
            case FINS:
            case FMOST:
            case FINS06:
            case FINS12:
                ret = limitCheck(obsValue, BOUND_FFF_LOW.getValue() / DIVISOR, BOUND_FFF_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case F02:
            case F10:
            case FMAX:
                ret = limitCheck(obsValue, BOUND_FFF_LOW.getValue() / DIVISOR, BOUND_FFF_HIGH.getValue() / DIVISOR / 2.0, dateTime, element, obs);
                break;
            case DDDINS:
            case DDD02:
            case DDD10:
            case DDDMAX:
            case DDDMOST:
            case DDDINS06:
            case DDDINS12:
                ret = limitCheck(obsValue, DDDINS_MIN.getValue(), DDDINS_MAX.getValue(), dateTime, element, obs);
                break;
            case FMAX_TIME:
            case FMOST_TIME:
            case TIME_HOUR_VMIN:
            case TEM_MIN_TIME:
            case TEM_MAX_TIME:
            case RHU_MIN_TIME:
            case TIME_HOUR_TGMAX:
            case TIME_HOUR_TGMIN:
            case TIME_HOUR_RTMAX:
            case TIME_HOUR_RTMIN:
            case TIME_MINUTE_RTMAX:
            case TIME_MINUTE_RTMIN:
            case PRS_MIN_TIME:
            case PRS_MAX_TIME:
            case GST_MIN_TIME:
            case GST_MAX_TIME:
                ret = extrenumTimeLimitCheck((int) obsValue, dateTime, element, obs);
                break;
            //能见度
            case BACK_ATR:
                ret = limitCheck(obsValue, BOUND_V_BACK_ATR_LOW.getValue(), BOUND_V_BACK_ATR_HIGH.getValue(), dateTime, element, obs);
                break;
            case TIME_MEAN_V0:
                ret = limitCheck(obsValue, 2, 2, dateTime, element, obs);
                break;
            case TIME_MEAN_DEF0:
                ret = limitCheck(obsValue, BOUND_TIME_MEAN_LOW.getValue(), BOUND_TIME_MEAN_HIGH.getValue(), dateTime, element, obs);
                break;
            case FIRST_STA_VMIN:
                ret = limitCheck(obsValue, BOUND_FIRST_STA_MIN_LOW.getValue(), BOUND_FIRST_STA_MIN_HIGH.getValue(), dateTime, element, obs);
                break;
            case V:
            case V01:
            case V10:
            case VMIN:
                ret = limitCheck(obsValue, BOUND_V_LOW.getValue(), BOUND_V_HIGH.getValue(), dateTime, element, obs);
                break;
            //温压湿
            case TEM:
            case TEM_MIN:
            case TEM_MAX:
            case TEM_MIN_24:
            case TEM_MAX_24:
                ret = limitCheck(obsValue, BOUND_T_LOW.getValue(), BOUND_T_HIGH.getValue(), dateTime, element, obs);
                break;
            case RHU:
            case RHU_MIN:
                ret = limitCheck(obsValue, BOUND_U_LOW.getValue(), BOUND_U_HIGH.getValue(), dateTime, element, obs);
                break;
            case VAP_PRS:
                ret = limitCheck(obsValue, BOUND_E_LOW.getValue(), BOUND_E_HIGH.getValue(), dateTime, element, obs);
                break;
            //草温
            case TG:
            case TGMAX:
            case TGMIN:
                ret = limitCheck(obsValue, BOUND_TG_LOW.getValue() / DIVISOR, BOUND_TG_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            //冻土深度
            case FRS:
            case SOIL_FROZEN1_1:
            case SOIL_FROZEN1_2:
            case SOIL_FROZEN2_1:
            case SOIL_FROZEN2_2:
                ret = limitCheck(obsValue, BOUND_SOILDEPTH_LOW.getValue(), BOUND_SOILDEPTH_HIGH.getValue(), dateTime, element, obs);
                break;
            //雪深雪压
            case SNOW_METHOD:
                ret = limitCheck(obsValue, ElementBound.BOUND_DEC_METHOD_SNOWDEPTH_LOW.getValue(), ElementBound.BOUND_DEC_METHOD_SNOWDEPTH_HIGH.getValue(), dateTime, element, obs);
                break;
            case SNOW_DEPTH:
                ret = limitCheck(obsValue, ElementBound.BOUND_SNOWDEPTH_LOW.getValue(), ElementBound.BOUND_SNOWDEPTH_HIGH.getValue(), dateTime, element, obs);
                break;
            case SNOW_PRS:
                ret = limitCheck(obsValue, ElementBound.BOUND_SNOWPRESSURE_LOW.getValue(), ElementBound.BOUND_SNOWPRESSURE_HIGH.getValue(), dateTime, element, obs);
                break;
            //路面温度,10cm路面温度,最高路面温度,最低路温
            case RT:
            case RT10:
            case RT_Max:
            case RT_MIN:
                ret = limitCheck(obsValue, BOUND_RT_LOW.getValue() / DIVISOR, BOUND_RT_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            //一阶统计
            case FIRST_STA_RTMAX:
                ret = limitCheck(obsValue, BOUND_FIRST_STA_MAX_LOW.getValue(), BOUND_FIRST_STA_MAX_HIGH.getValue(), dateTime, element, obs);
                break;
            //一阶统计(缺省)1，2
            case FIRST_STA_DEF0:
            case FIRST_STA_DEF1:
                ret = limitCheck(obsValue, BOUND_FIRST_STA_LOW.getValue(), BOUND_FIRST_STA_HIGH.getValue(), dateTime, element, obs);
                break;
            //一阶统计（最小值）
            case FIRST_STA_RTMIN:
                ret = limitCheck(obsValue, BOUND_FIRST_STA_MIN_LOW.getValue(), BOUND_FIRST_STA_MIN_HIGH.getValue(), dateTime, element, obs);
                break;
            //路面状态
            case ROAD_STATE:
                ret = limitCheck(obsValue, BOUND_ROAD_STATE_LOW.getValue(), BOUND_ROAD_STATE_HIGH.getValue(), dateTime, element, obs);
                break;
            case DEPTH_SNOW:
                ret = limitCheck(obsValue, BOUND_DEPTH_SNOW_LOW.getValue(), BOUND_DEPTH_SNOW_HIGH.getValue(), dateTime, element, obs);
                break;
            case DEPTH_WATER:
                ret = limitCheck(obsValue, BOUND_DEPTH_WATER_LOW.getValue(), BOUND_DEPTH_WATER_HIGH.getValue(), dateTime, element, obs);
                break;
            case DEPTH_ICE:
                ret = limitCheck(obsValue, BOUND_DEPTH_ICE_LOW.getValue(), BOUND_DEPTH_ICE_HIGH.getValue(), dateTime, element, obs);
                break;
            case T_ICEPOINT:
                ret = limitCheck(obsValue, BOUND_T_ICEPOINT_LOW.getValue() / DIVISOR, BOUND_T_ICEPOINT_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case SNOWMELT_CON:
                ret = limitCheck(obsValue, BOUND_SNOWMELT_CON_LOW.getValue(), BOUND_SNOWMELT_CON_HIGH.getValue(), dateTime, element, obs);
                break;
            //气压
            case PRS:
            case PRS_Sea:
            case PRS_MAX:
            case PRS_MIN:
                ret = limitCheck(obsValue, BOUND_P_LOW.getValue() / DIVISOR, BOUND_P_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            //降水
            case PRE_1_HOUR:

                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = limitCheck(obsValue, ElementBound.BOUND_R_LOW.getValue() / DIVISOR, ElementBound.BOUND_R_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                }
                break;
            case PRE_MANUAL:
                ret = limitCheck(obsValue, ElementBound.BOUND_R_TOTAL_LOW.getValue(), ElementBound.BOUND_R_TOTAL_HIGH.getValue(), dateTime, element, obs);
                break;
            //下面两个一致的
            case PRE_03_HOUR:
            case PRE_06_HOUR:
                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = limitCheck(obsValue, ElementBound.BOUND_R03_06_LOW.getValue() / DIVISOR, ElementBound.BOUND_R03_06_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                }
                break;
            case PRE_12_HOUR:
                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = limitCheck(obsValue, ElementBound.BOUND_R12_LOW.getValue() / DIVISOR, ElementBound.BOUND_R12_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                }
                break;
            case PRE_24_HOUR:
                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = limitCheck(obsValue, ElementBound.BOUND_R24_LOW.getValue() / DIVISOR, ElementBound.BOUND_R24_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                }
                break;
            //天气现象
            case TORNADO_DISTANCE:
                ret = limitCheck(obsValue, BOUND_TORNADO_DISTANCE_LOW.getValue(), BOUND_TORNADO_DISTANCE_HIGH.getValue(), dateTime, element, obs);
                break;
            case TORNADO_POSITION:
                ret = limitCheck(obsValue, BOUND_TORNADO_POSITION_LOW.getValue(), BOUND_TORNADO_POSITION_HIGH.getValue(), dateTime, element, obs);
                break;
            case WIREICINGDIA:
                ret = limitCheck(obsValue, BOUND_WIREICEINGDIA_LOW.getValue(), BOUND_WIREICEINGDIA_HIGH.getValue(), dateTime, element, obs);
                break;
            case MAXHAILDIA:
                ret = limitCheck(obsValue, BOUND_HAILDIA_LOW.getValue(), BOUND_HAILDIA_HIGH.getValue(), dateTime, element, obs);
                break;
            case PHENOMENACODE:
                ret = limitCheck(obsValue, BOUND_PHENOMENACODE_LOW.getValue(), BOUND_PHENOMENACODE_HIGH.getValue(), dateTime, element, obs);
                break;
            case PASTWEATHER_01:
            case PASTWEATHER_02:
                ret = limitCheck(obsValue, BOUND_PASTWEATHER_LOW.getValue(), BOUND_PASTWEATHER_HIGH.getValue(), dateTime, element, obs);
                break;
            //蒸发
            case DEV_TYPE_L:
                ret = limitCheck(obsValue, ElementBound.BOUND_DEV_TYPE_L_LOW.getValue(), ElementBound.BOUND_DEV_TYPE_L_HIGH.getValue(), dateTime, element, obs);
                break;
            case L:
                ret = limitCheck(obsValue, ElementBound.BOUND_L_LOW.getValue() / DIVISOR, ElementBound.BOUND_L_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case L_WL:
                ret = limitCheck(obsValue, ElementBound.BOUND_L_WL_LOW.getValue(), ElementBound.BOUND_L_WL_HIGH.getValue(), dateTime, element, obs);
                break;
            case L24:
                ret = limitCheck(obsValue, ElementBound.BOUND_L_WL_LOW.getValue() / DIVISOR, ElementBound.BOUND_L_WL_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            //地表温度
            case GST:
            case GST_MIN:
            case GST_MAX:
            case GST_MIN_12:
                ret = limitCheck(obsValue, BOUND_D0_LOW.getValue() / DIVISOR, BOUND_D0_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case GST_5cm:
                ret = limitCheck(obsValue, BOUND_D05_LOW.getValue() / DIVISOR, BOUND_D05_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case GST_10cm:
                ret = limitCheck(obsValue, BOUND_D10_LOW.getValue() / DIVISOR, BOUND_D10_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case GST_15cm:
                ret = limitCheck(obsValue, BOUND_D15_LOW.getValue() / DIVISOR, BOUND_D15_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case GST_20cm:
                ret = limitCheck(obsValue, BOUND_D20_LOW.getValue() / DIVISOR, BOUND_D20_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case GST_40cm:
                ret = limitCheck(obsValue, BOUND_D40_LOW.getValue() / DIVISOR, BOUND_D40_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case GST_80cm:
                ret = limitCheck(obsValue, BOUND_D80_LOW.getValue() / DIVISOR, BOUND_D80_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case GST_160cm:
                ret = limitCheck(obsValue, BOUND_D160_LOW.getValue() / DIVISOR, BOUND_D160_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            case GST_320cm:
                ret = limitCheck(obsValue, BOUND_D320_LOW.getValue() / DIVISOR, BOUND_D320_HIGH.getValue() / DIVISOR, dateTime, element, obs);
                break;
            //地面状态
            case GROUND_STATE:
                ret = limitCheck(obsValue, BOUND_GROUDSTATE_LOW.getValue(), BOUND_GROUDSTATE_HIGH.getValue(), dateTime, element, obs);
                break;
            //云
            case CLO_SYS:
                ret = limitCheck(obsValue, BOUND_CLOUD_SYSTEM_LOW.getValue(), BOUND_CLOUD_SYSTEM_HIGH.getValue(), dateTime, element, obs);
                break;
            case VER_MEAN0:
            case VER_MEAN1:
                ret = limitCheck(obsValue, BOUND_VER_MEAN_LOW.getValue(), BOUND_VER_MEAN_HIGH.getValue(), dateTime, element, obs);
                break;
            case CLO_Cov:
            case CLO_Cov_Low:
                ret = limitCheck(obsValue, BOUND_NN_LOW.getValue(), BOUND_NN_HIGH.getValue(), dateTime, element, obs);
                break;
            //编报云量无limit检测
            case CLO_Height:
                ret = limitCheck(obsValue, BOUND_CLOUDHIGH_LOW.getValue(), BOUND_CLOUDHIGH_HIGH.getValue(), dateTime, element, obs);
                break;
            case CLO_FOME:
            case CLO_CLASS:
                ret = limitCheck(obsValue, BOUND_CLOUD_CLASS_LOW.getValue(), BOUND_CLOUD_CLASS_LOW.getValue(), dateTime, element, obs);
                break;
            default:
                //没有进行质控的
                ret = QualityCode.Z_QC_NO_QC;
                break;
        }
        setLogger(element, QC_TYPE_LIMIT, ret, obsValue);
        return ret;
    }

    /**
     * 小时地面要素Limit质控共用方法，入参带上一步质控码
     *
     * @param obsValue :观测值
     * @param element  :观测要素
     * @param dateTime :资料时间
     * @param obs      :站点信息
     * @param preCode  :上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 10:10 AM
     */
    public static QualityCode limitCheck(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret = Z_QC_NO_QC;
        //缺测检查
        if (!BaseCheck.isEleCheckable(preCode)) {
            //表示调用了，但是不满足质控条件，日志记录为未做质控，同时返回传入的质控码
            setLogger(element, QC_TYPE_LIMIT, ret, obsValue);
            return preCode;
        }
        //入参检查
        checkAndGetStationCode(dateTime, element, obs, QC_TYPE_LIMIT);
        //微量元素赋值0
        if (obsValue == TINY_NUM) {
            obsValue = 0;
        }
        switch (element) {
            //继续执行
            //电线结冰,
            case NS_DIA:
            case WE_DIA:
            case NS_PLY:
            case WE_PLY:
                ret = limitCheck(obsValue, BOUND_WIREICEINGDIA_LOW.getValue(), BOUND_WIREICEINGDIA_DOUBT.getValue(), dateTime, element, obs, preCode);
                break;
            case NS_WEIGHT:
            case WE_WEIGHT:
                ret = limitCheck(obsValue, BOUND_WIREICINGWEIGHT_LOW.getValue(), BOUND_WIREICINGWEIGHT_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case T_WIREICING:
                ret = limitCheck(obsValue, BOUND_WIREICINGT_LOW.getValue() / DIVISOR, BOUND_WIREICINGT_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case DDD_WIREICING:
                ret = limitCheck(obsValue, BOUND_WIREICINGDDD_LOW.getValue(), BOUND_WIREICINGDDD_LOW.getValue(), dateTime, element, obs, preCode);
                break;
            case F_WIREICING:
                ret = limitCheck(obsValue, BOUND_WIREICINGFFF_LOW.getValue() / DIVISOR, BOUND_WIREICINGFFF_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            //风
            case FINS:
            case FMOST:
            case FINS06:
            case FINS12:
                ret = limitCheck(obsValue, BOUND_FFF_LOW.getValue() / DIVISOR, BOUND_FFF_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case F02:
            case F10:
            case FMAX:
                ret = limitCheck(obsValue, BOUND_FFF_LOW.getValue() / DIVISOR, BOUND_FFF_HIGH.getValue() / DIVISOR / 2.0, dateTime, element, obs, preCode);
                break;
            case DDDINS:
            case DDD02:
            case DDD10:
            case DDDMAX:
            case DDDMOST:
            case DDDINS06:
            case DDDINS12:
                ret = limitCheck(obsValue, DDDINS_MIN.getValue(), DDDINS_MAX.getValue(), dateTime, element, obs, preCode);
                break;
            case FMAX_TIME:
            case FMOST_TIME:
            case TIME_HOUR_VMIN:
            case TEM_MIN_TIME:
            case TEM_MAX_TIME:
            case RHU_MIN_TIME:
            case TIME_HOUR_TGMAX:
            case TIME_HOUR_TGMIN:
            case TIME_HOUR_RTMAX:
            case TIME_HOUR_RTMIN:
            case TIME_MINUTE_RTMAX:
            case TIME_MINUTE_RTMIN:
            case PRS_MIN_TIME:
            case PRS_MAX_TIME:
            case GST_MIN_TIME:
            case GST_MAX_TIME:
                ret = extrenumTimeLimitCheck((int) obsValue, dateTime, element, obs, preCode);
                break;
            //能见度
            case BACK_ATR:
                ret = limitCheck(obsValue, BOUND_V_BACK_ATR_LOW.getValue(), BOUND_V_BACK_ATR_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case TIME_MEAN_V0:
                ret = limitCheck(obsValue, 2, 2, dateTime, element, obs, preCode);
                break;
            case TIME_MEAN_DEF0:
                ret = limitCheck(obsValue, BOUND_TIME_MEAN_LOW.getValue(), BOUND_TIME_MEAN_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case FIRST_STA_VMIN:
                ret = limitCheck(obsValue, BOUND_FIRST_STA_MIN_LOW.getValue(), BOUND_FIRST_STA_MIN_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case V:
            case V01:
            case V10:
            case VMIN:
                ret = limitCheck(obsValue, BOUND_V_LOW.getValue(), BOUND_V_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //温压湿
            case TEM:
            case TEM_MIN:
            case TEM_MAX:
            case TEM_MIN_24:
            case TEM_MAX_24:
                ret = limitCheck(obsValue, BOUND_T_LOW.getValue(), BOUND_T_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case RHU:
            case RHU_MIN:
                ret = limitCheck(obsValue, BOUND_U_LOW.getValue(), BOUND_U_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case VAP_PRS:
                ret = limitCheck(obsValue, BOUND_E_LOW.getValue(), BOUND_E_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //草温
            case TG:
            case TGMAX:
            case TGMIN:
                ret = limitCheck(obsValue, BOUND_TG_LOW.getValue() / DIVISOR, BOUND_TG_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            //冻土深度
            case FRS:
            case SOIL_FROZEN1_1:
            case SOIL_FROZEN1_2:
            case SOIL_FROZEN2_1:
            case SOIL_FROZEN2_2:
                ret = limitCheck(obsValue, BOUND_SOILDEPTH_LOW.getValue(), BOUND_SOILDEPTH_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //雪深雪压
            case SNOW_METHOD:
                ret = limitCheck(obsValue, ElementBound.BOUND_DEC_METHOD_SNOWDEPTH_LOW.getValue(), ElementBound.BOUND_DEC_METHOD_SNOWDEPTH_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case SNOW_DEPTH:
                ret = limitCheck(obsValue, ElementBound.BOUND_SNOWDEPTH_LOW.getValue(), ElementBound.BOUND_SNOWDEPTH_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case SNOW_PRS:
                ret = limitCheck(obsValue, ElementBound.BOUND_SNOWPRESSURE_LOW.getValue(), ElementBound.BOUND_SNOWPRESSURE_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //路面温度,10cm路面温度,最高路面温度,最低路温
            case RT:
            case RT10:
            case RT_Max:
            case RT_MIN:
                ret = limitCheck(obsValue, BOUND_RT_LOW.getValue() / DIVISOR, BOUND_RT_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            //一阶统计
            case FIRST_STA_RTMAX:
                ret = limitCheck(obsValue, BOUND_FIRST_STA_MAX_LOW.getValue(), BOUND_FIRST_STA_MAX_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //一阶统计(缺省)1，2
            case FIRST_STA_DEF0:
            case FIRST_STA_DEF1:
                ret = limitCheck(obsValue, BOUND_FIRST_STA_LOW.getValue(), BOUND_FIRST_STA_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //一阶统计（最小值）
            case FIRST_STA_RTMIN:
                ret = limitCheck(obsValue, BOUND_FIRST_STA_MIN_LOW.getValue(), BOUND_FIRST_STA_MIN_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //路面状态
            case ROAD_STATE:
                ret = limitCheck(obsValue, BOUND_ROAD_STATE_LOW.getValue(), BOUND_ROAD_STATE_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case DEPTH_SNOW:
                ret = limitCheck(obsValue, BOUND_DEPTH_SNOW_LOW.getValue(), BOUND_DEPTH_SNOW_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case DEPTH_WATER:
                ret = limitCheck(obsValue, BOUND_DEPTH_WATER_LOW.getValue(), BOUND_DEPTH_WATER_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case DEPTH_ICE:
                ret = limitCheck(obsValue, BOUND_DEPTH_ICE_LOW.getValue(), BOUND_DEPTH_ICE_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case T_ICEPOINT:
                ret = limitCheck(obsValue, BOUND_T_ICEPOINT_LOW.getValue() / DIVISOR, BOUND_T_ICEPOINT_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case SNOWMELT_CON:
                ret = limitCheck(obsValue, BOUND_SNOWMELT_CON_LOW.getValue(), BOUND_SNOWMELT_CON_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //气压
            case PRS:
            case PRS_Sea:
            case PRS_MAX:
            case PRS_MIN:
                ret = limitCheck(obsValue, BOUND_P_LOW.getValue() / DIVISOR, BOUND_P_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            //降水
            case PRE_1_HOUR:

                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = limitCheck(obsValue, ElementBound.BOUND_R_LOW.getValue() / DIVISOR, ElementBound.BOUND_R_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                }
                break;
            case PRE_MANUAL:
                ret = limitCheck(obsValue, ElementBound.BOUND_R_TOTAL_LOW.getValue(), ElementBound.BOUND_R_TOTAL_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //下面两个一致的
            case PRE_03_HOUR:
            case PRE_06_HOUR:
                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = limitCheck(obsValue, ElementBound.BOUND_R03_06_LOW.getValue() / DIVISOR, ElementBound.BOUND_R03_06_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                }
                break;
            case PRE_12_HOUR:
                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = limitCheck(obsValue, ElementBound.BOUND_R12_LOW.getValue() / DIVISOR, ElementBound.BOUND_R12_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                }
                break;
            case PRE_24_HOUR:
                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = limitCheck(obsValue, ElementBound.BOUND_R24_LOW.getValue() / DIVISOR, ElementBound.BOUND_R24_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                }
                break;
            //天气现象
            case TORNADO_DISTANCE:
                ret = limitCheck(obsValue, BOUND_TORNADO_DISTANCE_LOW.getValue(), BOUND_TORNADO_DISTANCE_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case TORNADO_POSITION:
                ret = limitCheck(obsValue, BOUND_TORNADO_POSITION_LOW.getValue(), BOUND_TORNADO_POSITION_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case WIREICINGDIA:
                ret = limitCheck(obsValue, BOUND_WIREICEINGDIA_LOW.getValue(), BOUND_WIREICEINGDIA_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case MAXHAILDIA:
                ret = limitCheck(obsValue, BOUND_HAILDIA_LOW.getValue(), BOUND_HAILDIA_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case PHENOMENACODE:
                ret = limitCheck(obsValue, BOUND_PHENOMENACODE_LOW.getValue(), BOUND_PHENOMENACODE_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case PASTWEATHER_01:
            case PASTWEATHER_02:
                ret = limitCheck(obsValue, BOUND_PASTWEATHER_LOW.getValue(), BOUND_PASTWEATHER_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //蒸发
            case DEV_TYPE_L:
                ret = limitCheck(obsValue, ElementBound.BOUND_DEV_TYPE_L_LOW.getValue(), ElementBound.BOUND_DEV_TYPE_L_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case L:
                ret = limitCheck(obsValue, ElementBound.BOUND_L_LOW.getValue() / DIVISOR, ElementBound.BOUND_L_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case L_WL:
                ret = limitCheck(obsValue, ElementBound.BOUND_L_WL_LOW.getValue(), ElementBound.BOUND_L_WL_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case L24:
                ret = limitCheck(obsValue, ElementBound.BOUND_L_WL_LOW.getValue() / DIVISOR, ElementBound.BOUND_L_WL_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            //地表温度
            case GST:
            case GST_MIN:
            case GST_MAX:
            case GST_MIN_12:
                ret = limitCheck(obsValue, BOUND_D0_LOW.getValue() / DIVISOR, BOUND_D0_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case GST_5cm:
                ret = limitCheck(obsValue, BOUND_D05_LOW.getValue() / DIVISOR, BOUND_D05_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case GST_10cm:
                ret = limitCheck(obsValue, BOUND_D10_LOW.getValue() / DIVISOR, BOUND_D10_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case GST_15cm:
                ret = limitCheck(obsValue, BOUND_D15_LOW.getValue() / DIVISOR, BOUND_D15_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case GST_20cm:
                ret = limitCheck(obsValue, BOUND_D20_LOW.getValue() / DIVISOR, BOUND_D20_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case GST_40cm:
                ret = limitCheck(obsValue, BOUND_D40_LOW.getValue() / DIVISOR, BOUND_D40_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case GST_80cm:
                ret = limitCheck(obsValue, BOUND_D80_LOW.getValue() / DIVISOR, BOUND_D80_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case GST_160cm:
                ret = limitCheck(obsValue, BOUND_D160_LOW.getValue() / DIVISOR, BOUND_D160_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            case GST_320cm:
                ret = limitCheck(obsValue, BOUND_D320_LOW.getValue() / DIVISOR, BOUND_D320_HIGH.getValue() / DIVISOR, dateTime, element, obs, preCode);
                break;
            //地面状态
            case GROUND_STATE:
                ret = limitCheck(obsValue, BOUND_GROUDSTATE_LOW.getValue(), BOUND_GROUDSTATE_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //云
            case CLO_SYS:
                ret = limitCheck(obsValue, BOUND_CLOUD_SYSTEM_LOW.getValue(), BOUND_CLOUD_SYSTEM_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case VER_MEAN0:
            case VER_MEAN1:
                ret = limitCheck(obsValue, BOUND_VER_MEAN_LOW.getValue(), BOUND_VER_MEAN_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case CLO_Cov:
            case CLO_Cov_Low:
                ret = limitCheck(obsValue, BOUND_NN_LOW.getValue(), BOUND_NN_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            //编报云量无limit检测
            case CLO_Height:
                ret = limitCheck(obsValue, BOUND_CLOUDHIGH_LOW.getValue(), BOUND_CLOUDHIGH_HIGH.getValue(), dateTime, element, obs, preCode);
                break;
            case CLO_FOME:
            case CLO_CLASS:
                ret = limitCheck(obsValue, BOUND_CLOUD_CLASS_LOW.getValue(), BOUND_CLOUD_CLASS_LOW.getValue(), dateTime, element, obs, preCode);
                break;
            default:
                //没有进行质控的
                ret = QualityCode.Z_QC_NO_QC;
                break;
        }
        setLogger(element, QC_TYPE_LIMIT, ret, obsValue);
        return ret;


    }


    /**
     * limit(界限值的通用质控方法，返回质控码)
     *
     * @param obsValue : 观测值
     * @param left     : 左值(小值)
     * @param right    : 右值(大值)
     * @param datetime : 时间
     * @param element  : 质控元素
     * @param obs      : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 10:11 AM
     */
    private static QualityCode limitCheck(double obsValue, double left, double right, LocalDateTime datetime, MeteoElement element, BaseStationInfo obs) {

        return getLimitQualityCode(obsValue, left, right, element);

    }

    /**
     * limit(界限值的通用质控方法,重载加了上一步质控码作为入参，返回质控码)
     *
     * @param obsValue : 观测值
     * @param left     : 左值(小值)
     * @param right    : 右值(大值)
     * @param datetime : 时间
     * @param element  : 质控元素
     * @param obs      : 观测站信息
     * @param preCode  : 上一步的质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 10:01 AM
     */
    private static QualityCode limitCheck(double obsValue, double left, double right, LocalDateTime datetime, MeteoElement element, BaseStationInfo obs, QualityCode preCode) {
        //下面两种情况直接OK
        QualityCode ret = getLimitQualityCode(obsValue, left, right, element);
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), datetime, element, preCode, ret, LIMIT);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;

    }

    private static QualityCode getLimitQualityCode(double obsValue, double left, double right, MeteoElement element) {
        QualityCode ret;
        if (SNOW_DEPTH == element && Math.abs(obsValue + TINY) <= EPTION && Math.abs(obsValue + TINY2) <= EPTION) {
            ret = QualityCode.Z_QC_OK;
        } else if (obsValue < left) {
            ret = QualityCode.Z_QC_ERROR;
        } else if (obsValue > right) {
            boolean isGoOn = WIREICING == element || NS_DIA == element || NS_PLY == element || NS_WEIGHT == element || WE_DIA == element || WE_PLY == element ||
                    WE_WEIGHT == element || T_WIREICING == element || DDD_WIREICING == element || F_WIREICING == element;
            if (isGoOn) {
                ret = QualityCode.Z_QC_DOUBT;
            } else {
                ret = QualityCode.Z_QC_ERROR;
            }
        } else {
            ret = QualityCode.Z_QC_OK;
        }
        return ret;
    }

    /**
     * 极值出现时间范围检查，在观测小时时间段范围内，正确
     *
     * @param hourMinute : 极值出现时分
     * @param dateTime   : 资料时次
     * @param element    : 观测元素
     * @param obs        : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 4:10 PM
     */
    private static QualityCode extrenumTimeLimitCheck(int hourMinute, LocalDateTime dateTime, MeteoElement element, BaseStationInfo obs) {
        return getExLimitQualityCode(hourMinute, dateTime, element, obs);
    }

    /**
     * 极值出现时间范围检查，在观测小时时间段范围,正确,新增一个上一步质控码作为入参
     *
     * @param hourMinute : 极值出现时
     * @param dateTime   :  资料时次
     * @param element    :  观测元素
     * @param obs        :  观测站信息
     * @param preCode    : 上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 10:29 AM
     */
    private static QualityCode extrenumTimeLimitCheck(int hourMinute, LocalDateTime dateTime, MeteoElement element, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret = getExLimitQualityCode(hourMinute, dateTime, element, obs);
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, LIMIT);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }

    /**
     * 获取极值出现时间范围检查质控结果
     *
     * @param hourMinute : 极值出现时
     * @param dateTime   :  资料时次
     * @param element    :  观测元素
     * @param obs        :  观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 10:36 AM
     */
    private static QualityCode getExLimitQualityCode(int hourMinute, LocalDateTime dateTime, MeteoElement element, BaseStationInfo obs) {
        if (element == null || obs == null || dateTime == null) {
            LOGGER.error(QC_TYPE_LIMIT + PARAMS_INVALID);
            throw new IllegalArgumentException(QC_TYPE_LIMIT + PARAMS_INVALID);
        }
        QualityCode ret;
        // 极值出现时间
        String hourMinuteStr = String.format("%04d", hourMinute);
        LocalDateTime time1 = LocalDateTime.of(
                dateTime.getYear(),
                dateTime.getMonthValue(),
                dateTime.getDayOfMonth(),
                dateTime.getHour(),
                dateTime.getMinute(),
                dateTime.getSecond());
        time1 = time1.plusHours(-1);
        LocalDateTime time3 = LocalDateTime.of(
                dateTime.getYear(),
                dateTime.getMonthValue(),
                dateTime.getDayOfMonth(),
                Integer.parseInt(hourMinuteStr.substring(0, 2)),
                Integer.parseInt(hourMinuteStr.substring(2, 4)),
                dateTime.getSecond());
        if (Integer.parseInt(hourMinuteStr.substring(0, 2)) > dateTime.getHour()) {
            time3 = time3.plusDays(-1);
        }
        boolean timeSuit = (time3.isBefore(dateTime) && time3.isAfter(time1)) || time3.isEqual(dateTime);
        if (timeSuit) {
            ret = QualityCode.Z_QC_OK;
        } else {
            ret = QualityCode.Z_QC_ERROR;
        }
        return ret;
    }


}
