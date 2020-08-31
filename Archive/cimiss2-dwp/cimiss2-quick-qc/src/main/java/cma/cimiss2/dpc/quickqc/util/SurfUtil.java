package cma.cimiss2.dpc.quickqc.util;

import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.*;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static cma.cimiss2.dpc.quickqc.bean.Constants.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.ElementBound.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement.*;
import static java.lang.Math.*;
import static java.util.Calendar.DAY_OF_YEAR;

/**
 * ************************************
 *
 * @ClassName: SurfUtil
 * @Auther: dangjinhu
 * @Date: 2019/7/22 20:32
 * @Description: 地面算法util
 * @Copyright: All rights reserver.
 * ************************************
 */

public class SurfUtil {

    /**
     * 返回该元素是否包含在里面，如果包含在里面就返回true
     *
     * @param element : 需要进行判断的元素
     * @return : boolean
     * @author : When6passBye
     * @date : 2019/8/19 10:49 AM
     */
    public static boolean isNoTask(MeteoElement element) {
        return
                element == PHENOMENACODE || element == TIMECYCLE || element == PASTWEATHER_01 || element == PASTWEATHER_02 || element == PRE_MANUAL ||
                        element == SNOW_DEPTH || element == SNOW_PRS || element == SOIL_FROZEN1_1 || element == SOIL_FROZEN1_2 || element == SOIL_FROZEN2_1 || element == SOIL_FROZEN2_2 ||
                        element == TORNADO_POSITION || element == TORNADO_DISTANCE || element == WIREICINGDIA || element == WIREICING || element == MAXHAILDIA || element == NS_DIA ||
                        element == NS_PLY || element == NS_WEIGHT || element == WE_DIA || element == WE_PLY || element == T_WIREICING || element == DDD_WIREICING || element == F_WIREICING ||
                        element == CLO_FOME || element == CLOUD;
    }

    public static boolean isNoTask(MeteoElement element, LocalDateTime datetime, MeteoElementObsStateInfo meos) {

        boolean a = element == PRS_03 || element == PRS_24 || element == TEM_24 || element == TEM_MAX_24 || element == TEM_MIN_24 || element == PRE_03_HOUR || element == PRE_06_HOUR ||
                element == PRE_12_HOUR || element == PRE_24_HOUR || element == DDDINS06 || element == DDDINS12 || element == FINS12 || element == GST_MIN_12;
        boolean b = (datetime.getMinute() != 0 && (meos.getFlag() == HUMAN));
        return a && b;
    }

    public static boolean isLackOrNoTask(MeteoElement element) {

        boolean a = element == PRE_1_HOUR || element == PRE_03_HOUR || element == PRE_06_HOUR || element == PRE_12_HOUR ||
                element == PRE_24_HOUR ||
                element == L;

        return a;
    }

    /**
     * dangjinhu
     * 计算纯水平液面饱和水汽压
     *
     * @param tem 温度
     * @return 饱和水汽压
     */
    private static double smbhsqy(double tem) {
        double ew;
        double T1 = 273.16;
        double T = 273.15 + tem;
        ew = 10.79574 * (1 - T1 / T) - 5.02800 * log10(T / T1)
                + 1.50475 / 10000.00 * (1.0 - pow(10.0, -8.2969 * (T / T1 - 1.0)))
                + 0.42873 / 1000.0 * (pow(10.0, 4.76955 * (1.0 - T1 / T) - 1.0))
                + 0.78614;
        return pow(10.0, ew);
    }


    /**
     * 计算纯水平冰面饱和水汽压ei
     *
     * @param tem : 温度
     * @return : double
     * @author : When6passBye
     * @date : 2019/8/21 3:40 PM
     */
    private static double bmbhsqy(double tem) {
        double temp = 273.16 / (273.15 + tem);
        double calcEwB = 10.75981 - 9.09685 * temp
                - 3.56654 * log10(temp) / log10(10.0) - 0.87682 / temp;
        return pow(10.0, calcEwB);
    }

    /**
     * dangjinhu
     * 计算海平面气压
     *
     * @param prs      气压
     * @param tem      温度
     * @param tem12    温度
     * @param altitude 气压传感器高度
     * @return 海平面气压
     */
    public static double calcSeaLevelPressure(double prs, double tem, double tem12, double altitude) {
        double tm = (tem + tem12) / 2.0 + altitude / 400.0;
        double power = altitude / (18400.0 * (1 + tm / 273.0));
        return prs * pow(10, power);
    }

    /**
     * dangjinhu
     * 地面温度所有条件成立
     *
     * @param depth 地温值
     * @return true 否合条件 false 不否合条件
     */
    public static boolean gstDepthIsIn(Double depth) {
        List<Boolean> list = new ArrayList<>();
        double[] ds = new double[]{0.05, 0.10, 0.15, 0.2, 0.4, 0.8, 1.6, 3.2};
        for (double d : ds) {
            if (abs(depth - d) > EPTION) {
                list.add(true);
            } else {
                list.add(false);
            }
        }
        return list.stream().filter(s -> s).count() == ds.length;
    }


    /**
     * cuihongyuan
     * 根据基础配置数据aws,查找参考站配置ref，获取记录所在时次对应要素element的范围质控参数数据
     * 需要根据当前数据对质控参数进行修订，T_MAX_MIN 为 对
     *
     * @param awsConf      原始站
     * @param stationInfos 站点数据集
     * @param element      要素
     * @param obsValue     值（邓智恒新增）
     * @param datetime     资料时间
     * @param mapMaxMin    台站-年序日-阈值
     * @return T_MAX_MIN
     */
    public static MaxMinValues getAwsElementRange(BaseStationInfo awsConf, Map<String, AwsNatStationInfo> stationInfos, MeteoElement element,
                                                  double obsValue, LocalDateTime datetime, Map<String, ? extends BaseStationInfo> mapMaxMin) {
        BaseStationInfo ref = getRefAwsByElement(stationInfos, mapMaxMin, awsConf);
        if (ref == null) {
            System.out.println("Element has no configuration: " + element);
            return null;
        }
        // 根据参考站初始化指定日期的最大最小值
        MaxMinValues range = setAwsConfByRefDayRangeValue(awsConf, ref, element, datetime.getDayOfYear(), mapMaxMin);

        // 根据参考站初始化指定月份的最大最小值（邓智恒新增）
        MaxMonthParaInfo monthParaInfo = setAwsConfByRefMonRangeValue(awsConf, ref, element, datetime.getMonth().getValue());

        double bgc = awsConf.getAltitude() - ref.getAltitude();
        InterpolateSeq interpolateSeq;

        LocalDateTime datetimeTemp = LocalDateTime.of(
                datetime.getYear(),
                datetime.getMonthValue(),
                datetime.getDayOfMonth(),
                datetime.getHour(),
                datetime.getMinute(),
                datetime.getSecond());
        //新增判断
        if (range != null) {
            switch (element) {
                case PRS:
                    datetimeTemp = datetimeTemp.plusHours(8);
                    //24小时插值出本小时的最高最低
                    interpolateSeq = getMaxMinOf24hour(element, awsConf, datetimeTemp, bgc,
                            range.getMaxValue(), range.getMinValue());
                    range.setMaxValue(interpolateSeq.getMaxHour());
                    range.setMinValue(interpolateSeq.getMinHour());
                    //根据气温修正最高最低
                    double t;
                    if (Math.abs(obsValue - 99999) > EPTION) {
                        t = obsValue - T_K_ADD;
                    } else {
//				温度缺测时使用范围质控的平均值代替
                        MaxMinValues tRange = getAwsElementRange(awsConf, stationInfos, TEM, 99999, datetime, mapMaxMin);
                        if (tRange == null) {
                            throw new RuntimeException("tRange is null");
                        }
                        t = (tRange.getMaxValue() + tRange.getMinValue()) / 2.0;
                    }
                    calcPRange(range, ref.getAltitudeP(), t);
                    break;

                case TEM:
                case TG:
                case GST:
                case GST_5cm:
                case GST_10cm:
                    // 24小时插值出本小时的最高最低
                    datetimeTemp = datetimeTemp.plusHours(8);
                    interpolateSeq = getMaxMinOf24hour(element, awsConf, datetimeTemp, bgc,
                            range.getMaxValue(), range.getMinValue());
                    range.setMaxValue(interpolateSeq.getMaxHour());
                    range.setMinValue(interpolateSeq.getMinHour());
                    break;
                case GST_15cm:
                case GST_20cm:
                case GST_40cm:
                case GST_80cm:
                case GST_160cm:
                case GST_320cm:
                case VAP_PRS:
                    //什么都不做
                    break;
                case RHU:
                    //根据气温修正最高最低
                    if (Math.abs(obsValue - 99999) >= EPTION) {
                        t = obsValue - T_K_ADD;
                        calcURange(range, t);
                    }
                    break;
                case PRE:
                case WIN_D:
                    //只设置一个最大值
                    range.setMaxValue(monthParaInfo.getMonMaxValue(datetime.getMonth().getValue()));
                    break;
                default:
                    break;

            }

        } else {// 无参考配置值， 则采用界限值
            range = new MaxMinValues();

            switch (element) {
                case PRS:
                    range.setMaxValue(BOUND_P_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_P_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case TEM:
                    range.setMaxValue(BOUND_T_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_T_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case TG:
                    range.setMaxValue(BOUND_TG_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_TG_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case GST:
                    range.setMaxValue(BOUND_D0_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_D0_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case GST_5cm:
                    range.setMaxValue(BOUND_D05_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_D05_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case GST_10cm:
                    range.setMaxValue(BOUND_D10_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_D10_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case GST_15cm:
                    range.setMaxValue(BOUND_D15_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_D15_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case GST_20cm:
                    range.setMaxValue(BOUND_D20_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_D20_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case GST_40cm:
                    range.setMaxValue(BOUND_D40_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_D40_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case GST_80cm:
                    range.setMaxValue(BOUND_D80_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_D80_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case GST_160cm:
                    range.setMaxValue(BOUND_D160_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_D160_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case GST_320cm:
                    range.setMaxValue(BOUND_D320_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_D320_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case RHU:
                    range.setMaxValue(BOUND_U_HIGH.getValue());
                    range.setMinValue(BOUND_U_LOW.getValue());
                    range.setExceedingThreshold(0);
                    break;
                case VAP_PRS:
                    range.setMaxValue(BOUND_E_HIGH.getValue());
                    range.setMinValue(BOUND_E_LOW.getValue());
                    range.setExceedingThreshold(0);
                    break;
                case PRE:
                    range.setMaxValue(BOUND_R_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_R_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                case WIN_D:
                    range.setMaxValue(BOUND_FFF_HIGH.getValue() / 10.0);
                    range.setMinValue(BOUND_FFF_LOW.getValue() / 10.0);
                    range.setExceedingThreshold(0);
                    break;
                default:
                    break;

            }

        }

        return range;
    }

    /**
     * cuihongyuan
     * 日值参数赋值
     *
     * @param awsConf     原始站
     * @param stationInfo 参考站
     * @param element     要素
     * @param dayOfYear   资料时间
     * @param mapMaxMin   界限值配置
     * @return 界限值
     */
    private static MaxMinValues setAwsConfByRefDayRangeValue(BaseStationInfo awsConf, BaseStationInfo stationInfo,
                                                             MeteoElement element, int dayOfYear, Map<String, ? extends BaseStationInfo> mapMaxMin) {
        // 初始化
        MaxMinValues range0;
        BaseStationInfo baseStationInfo = mapMaxMin.get(awsConf.getStationCode());
        if (baseStationInfo == null) {
            return null;
            //range0 = new MaxMinValues();邓智恒修改，如果没有获取到，直接返回null
        } else {
            if (baseStationInfo instanceof MaxMinDaychinaInfo) {
                range0 = ((MaxMinDaychinaInfo) baseStationInfo).getMaxMinValueOfDays().get(dayOfYear);
                if (null == range0) {
                    CommonUtil.LOGGER.error(QC_TYPE_RANGE + " : get range config is null");
                    throw new IllegalArgumentException(QC_TYPE_RANGE + " : get range config is null");
                }
            } else if(element==PRE||element==WIN_D){
                return new MaxMinValues();
            }else {
                return null;
            }

        }

        // 计算
        MaxMinValues range = new MaxMinValues();
        range.setMaxValue(range0.getMaxValue());
        range.setMinValue(range0.getMinValue());
        range.setExceedingThreshold(range0.getExceedingThreshold());

        BaseStationInfo obs = mapMaxMin.get(stationInfo.getStationCode());
        if (obs == null) {
            return null;
        }
        //向下转型
        MaxMinDaychinaInfo maxMin;
        if (obs instanceof MaxMinDaychinaInfo) {
            maxMin = (MaxMinDaychinaInfo) obs;
        } else {
            CommonUtil.LOGGER.error(QC_TYPE_RANGE + " : class type is not MaxMinDaychinaInfo.");
            throw new IllegalArgumentException(QC_TYPE_RANGE + " : class type is not MaxMinDaychinaInfo.");
        }

        MaxMinValues refRange = maxMin.getMaxMinValueOfDays().get(dayOfYear);
        if (refRange == null) {
            return null;
        }
        double thresh = maxMin.getMaxMinValueOfDays().get(dayOfYear).getExceedingThreshold();
        switch (element) {
            case GST:
                range.setMaxValue(refRange.getMaxValue());
                range.setMinValue(refRange.getMinValue());
                range.setExceedingThreshold(refRange.getExceedingThreshold());
                if (range.getExceedingThreshold() > 10.0) {
                    range.setExceedingThreshold(10.0);
                }
                if (range.getExceedingThreshold() < 5.0) {
                    range.setExceedingThreshold(5.0);
                }
                break;
            case GST_5cm:
                range.setMaxValue(refRange.getMaxValue());
                range.setMinValue(refRange.getMinValue());
                range.setExceedingThreshold(thresh);
                if (range.getExceedingThreshold() > 8.0) {
                    range.setExceedingThreshold(8.0);
                }
                if (range.getExceedingThreshold() < 4.0) {
                    range.setExceedingThreshold(4.0);
                }
                break;
            case GST_10cm:
                range.setMaxValue(refRange.getMaxValue());
                range.setMinValue(refRange.getMinValue());
                range.setExceedingThreshold(thresh);
                if (range.getExceedingThreshold() > 5.5) {
                    range.setExceedingThreshold(5.5);
                }
                if (range.getExceedingThreshold() < 3.5) {
                    range.setExceedingThreshold(3.5);
                }
                break;
            case GST_15cm:
            case GST_20cm:
            case GST_40cm:
            case GST_80cm:
            case GST_160cm:
            case GST_320cm:
                range.setMaxValue(refRange.getMaxValue());
                range.setMinValue(refRange.getMinValue());
                range.setExceedingThreshold(thresh);
                double mBgc = (awsConf.getAltitude() > 10000.0 ? awsConf.getAltitude() - 10000.0 : awsConf.getAltitude()) - stationInfo.getAltitude();
                switch (element) {
                    case GST_15cm:
                        range.setMaxValue(range.getMaxValue() - 25.0 * mBgc * 0.038 / 100.0 + 2.0);
                        range.setMinValue(range.getMinValue() - 25.0 * mBgc * 0.03 / 100.0 - 1.0);
                        if (range.getExceedingThreshold() < 3.5) {
                            range.setExceedingThreshold(3.5);
                        }
                        if (range.getExceedingThreshold() > 5.0) {
                            range.setExceedingThreshold(5.0);
                        }
                        break;
                    case GST_20cm:
                        range.setMaxValue(range.getMaxValue() - 15.0 * mBgc * 0.035 / 100.0 + 0.5);
                        range.setMinValue(range.getMinValue() - 15.0 * mBgc * 0.03 / 100.0 - 0.0);
                        if (range.getExceedingThreshold() < 2.8) {
                            range.setExceedingThreshold(2.8);
                        }
                        if (range.getExceedingThreshold() > 4.0) {
                            range.setExceedingThreshold(4.0);
                        }
                        break;
                    case GST_40cm:
                        range.setMaxValue(range.getMaxValue() - 10.0 * mBgc * 0.06 / 100.0 + 0.5);
                        range.setMinValue(range.getMinValue() - 10.0 * mBgc * 0.04 / 100.0 - 0.5);
                        if (range.getExceedingThreshold() < 2.5) {
                            range.setExceedingThreshold(2.5);
                        }
                        if (range.getExceedingThreshold() > 3.5) {
                            range.setExceedingThreshold(3.5);
                        }
                        break;
                    case GST_80cm:
                        range.setMaxValue(range.getMaxValue() - 10.0 * mBgc * 0.03 / 100.0);
                        range.setMinValue(range.getMinValue() - 10.0 * mBgc * 0.03 / 100.0);
                        double fff = -0.6;
                        if (range.getMaxValue() - range.getMinValue() < 2.0) {
                            fff = -1.2;
                        }
                        if (range.getMaxValue() - range.getMinValue() < 4.0) {
                            fff = -1.0;
                        }
                        double Max = range.getMaxValue() + 1.0 - fff;
                        double Min = range.getMinValue() - 1.0 + fff;

                        range.setMaxValue(Max + 1.0);
                        range.setMinValue(Min - 1.0);

                        if (range.getExceedingThreshold() < 2.0) {
                            range.setExceedingThreshold(2.0);
                        }
                        if (range.getExceedingThreshold() > 3.0) {
                            range.setExceedingThreshold(3.0);
                        }
                        break;
                    case GST_160cm:
                        range.setMaxValue(range.getMaxValue() - 10.0 * mBgc * 0.03 / 100.0);
                        range.setMinValue(range.getMinValue() - 10.0 * mBgc * 0.03 / 100.0);
                        fff = -0.3;
                        if (range.getMaxValue() - range.getMinValue() < 4.0) {
                            fff = -0.6;
                        }
                        if (range.getMaxValue() - range.getMinValue() < 2.0) {
                            fff = -1.0;
                        }
                        Max = range.getMaxValue() + 0.8 - fff;
                        Min = range.getMinValue() - 0.8 + fff;

                        range.setMaxValue(Max + 1.0);
                        range.setMinValue(Min - 1.0);

                        if (range.getExceedingThreshold() < 2.0) {
                            range.setExceedingThreshold(2.0);
                        }
                        if (range.getExceedingThreshold() > 3.0) {
                            range.setExceedingThreshold(3.0);
                        }
                        break;
                    case GST_320cm:
                        range.setMaxValue(range.getMaxValue() - 10.0 * mBgc * 0.03 / 100.0);
                        range.setMinValue(range.getMinValue() - 10.0 * mBgc * 0.03 / 100.0);
                        fff = -0.2;
                        if (range.getMaxValue() - range.getMinValue() < 4.0) {
                            fff = -0.8;
                        }
                        if (range.getMaxValue() - range.getMinValue() < 2.0) {
                            fff = -1.0;
                        }
                        Max = range.getMaxValue() + 0.5 - fff;
                        Min = range.getMinValue() - 0.5 + fff;

                        range.setMaxValue(Max + 1.0);
                        range.setMinValue(Min - 1.0);

                        if (range.getExceedingThreshold() < 1.8) {
                            range.setExceedingThreshold(1.8);
                        }
                        if (range.getExceedingThreshold() > 3.0) {
                            range.setExceedingThreshold(3.0);
                        }
                        break;
                    default:
                        break;

                }
                break;
            case PRS:
                range.setMaxValue(refRange.getMaxValue());
                //考虑台风影响，修正范围最低值
                range.setMinValue(refRange.getMinValue() - 3.5);
                range.setExceedingThreshold(refRange.getExceedingThreshold());
                if (range.getExceedingThreshold() > 8.0) {

                    range.setExceedingThreshold(8.0);
                }
                if (range.getExceedingThreshold() < 2.6) {

                    range.setExceedingThreshold(2.6);
                }
                break;
            case TG:
            case VAP:
                range.setMaxValue(refRange.getMaxValue());
                range.setMinValue(refRange.getMinValue());
                range.setExceedingThreshold(refRange.getExceedingThreshold());
                break;
            case TEM:
                range.setMaxValue(refRange.getMaxValue());
                range.setMinValue(refRange.getMinValue() - 2);
                /*考虑大幅降温天气*/
                range.setExceedingThreshold(refRange.getExceedingThreshold());
                if (range.getExceedingThreshold() > 6.0) {

                    range.setExceedingThreshold(6.0);
                }
                if (range.getExceedingThreshold() < 2.5) {

                    range.setExceedingThreshold(2.5);
                }
                break;

            case RHU:
                double eMax, eMin;    //水汽压的最大值和最小值
                //E
                double maxDay, minDay;
                maxDay = 3.0 + refRange.getMaxValue();
                minDay = -3.0 + refRange.getMinValue();
                range.setExceedingThreshold(refRange.getExceedingThreshold());
                mBgc = (awsConf.getAltitude() > 10000.0 ? awsConf.getAltitude() - 10000.0 : awsConf.getAltitude()) - stationInfo.getAltitude();
                maxDay -= mBgc * 15.0 * 0.035 / 100.0;
                minDay -= mBgc * 15.0 * 0.035 / 100.0;
                if (maxDay < 0.0) {
                    maxDay = 0.1;
                }
                if (minDay < 0.0) {
                    minDay = 0.1;
                }
                eMax = maxDay * 1.1 + 1.0;
                if (minDay < 10.0) {
                    eMin = minDay * .88;
                } else {
                    eMin = minDay * .92;
                }
                if (eMin < 0.0) {
                    eMin = 0.1;
                }
                range.setMaxValue(eMax);
                range.setMinValue(eMin);
                range.setExceedingThreshold(refRange.getExceedingThreshold());
                break;
            default:
                break;
        }
        return range;
    }


    /**
     * cuihongyuan
     * 日值参数赋值
     *
     * @param awsConf     原始站
     * @param stationInfo 参考站
     * @param element     要素
     * @param monOfYear   资料时间
     * @return 界限值
     */
    private static MaxMonthParaInfo setAwsConfByRefMonRangeValue(BaseStationInfo awsConf, BaseStationInfo stationInfo,
                                                                 MeteoElement element, int monOfYear) {

        // 初始化
        Map<String, MaxMonthParaInfo> maxMon;
        if (element==PRE) {
            maxMon = BaseCheck.R_MAX_MON;
        } else if(element==WIN_D){
            maxMon = BaseCheck.F_MAX_MON;
        } else{
            return null;
        }
        MaxMonthParaInfo info = maxMon.get(stationInfo.getStationCode());
        if (info == null) {
            throw new RuntimeException("get max month config failure MaxMonthParaInfo is null");
        }
        double refMonMaxValue = info.getMonMaxValue(monOfYear);
        MaxMonthParaInfo monthParaInfo = maxMon.get(awsConf.getStationCode());
        MaxMonthParaInfo clone = (MaxMonthParaInfo)monthParaInfo.clone();

        double bg = awsConf.getAltitude();
        if (bg > 10000.0) {
            bg = bg - 10000.0;
        }
        if (element==PRE) {
            double r1 = 4.5 + refMonMaxValue;
            if (r1 < 3.0) {
                r1 = 3.0;
            } else if (r1 > 110.0) {
                r1 = 110.0;
            }
            clone.setMonMaxValue(monOfYear, 2 * r1);
        } else if (element == WIN_D) {
            double bgDiff = bg - stationInfo.getAltitude();
            double fMax = 5.0 + refMonMaxValue + bgDiff * 0.5 / 100.0;
            if (fMax < 8.0) {
                fMax = 8.0;
            }
            if (fMax > 45.0) {
                fMax = 45.0;
            }
            clone.setMonMaxValue(monOfYear, fMax);
        }
        return clone;

    }

    /**
     * 检查元素是否属于降水
     *
     * @param element :
     * @return : boolean
     * @author : When6passBye
     * @date : 2019/8/24 6:10 PM
     */
    private static boolean isRainEle(MeteoElement element) {
        return element == PRE || element == PRE_1_HOUR || element == PRE_03_HOUR ||
                element == PRE_06_HOUR || element == PRE_12_HOUR || element == PRE_24_HOUR || element == PRE_MANUAL;
    }

    /**
     * cuihongyuan
     * 获取由要素element的配置数据的aws的参考临近站。在限制的海拔差范围内，与本站距离最小的站点为本站的临近站
     * 若最近的站点距离本站超过距离限制，则返回NULL
     * getAwsConfEx和getAwsConfEx2、getAwsConfRecord中调用
     *
     * @param stationInfos 站点信息集
     * @param mapMaxMin    温度界限值
     * @param awsConf      原始站点
     * @return 匹配站点信息
     */
    private static BaseStationInfo getRefAwsByElement(Map<String, AwsNatStationInfo> stationInfos, Map<String, ? extends BaseStationInfo> mapMaxMin, BaseStationInfo awsConf) {
        /*本站与参考站海拔差限制范围*/
        double altitudeDiffLimit;
        /*本站与参考站海拔差*/
        double altitudeDiff;
        /*两站间最大距离*/
        double distanceLimit = MAX_DISTANCE;
        /*参考站与与本站最小的距离*/
        double distanceMin = distanceLimit;
        double distance;
        altitudeDiffLimit = getAltitudeDiffLimit(awsConf.getAltitude());
        BaseStationInfo stationInfo = null;
        Set<Map.Entry<String, AwsNatStationInfo>> entrySet = stationInfos.entrySet();
        BaseStationInfo tmpStationInfo;
        for (Map.Entry<String, AwsNatStationInfo> sta : entrySet) {
            String key = sta.getKey();
            tmpStationInfo = stationInfos.get(key);
            if (tmpStationInfo == null) {
                throw new RuntimeException("getRefAwsByElement failure ," + sta + " get null stationInfo");
            }
            altitudeDiff = Math.abs(awsConf.getAltitude() - tmpStationInfo.getAltitude());
            /*海拔差在限制范围内，且站点中该要素已配置*/
            if (altitudeDiff < altitudeDiffLimit && mapMaxMin.get(key) != null) {
                distance = GetDistance(awsConf.getLongitude(), awsConf.getLatitude(),
                        tmpStationInfo.getLongitude(), tmpStationInfo.getLatitude());
                if (distance < distanceMin) {
                    distanceMin = distance;
                    stationInfo = (BaseStationInfo) tmpStationInfo.clone();
                }
            }
        }
        if (distanceMin >= distanceLimit) {
            // 最近站点距离超限
            stationInfo = null;
        }
        return stationInfo;
    }

    /**
     * cuihongyuan
     * 获取地球上两点之间的距离:km
     *
     * @param lon1 经度1
     * @param lat1 纬度1
     * @param lon2 经度2
     * @param lat2 纬度2
     * @return 两点距离
     */
    private static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double R = 6378.0;
        lon1 = PI * lon1 / RIGHT_ANGLE;
        lon2 = PI * lon2 / RIGHT_ANGLE;
        lat1 = PI * lat1 / RIGHT_ANGLE;
        lat2 = PI * lat2 / RIGHT_ANGLE;
        return R * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1));
    }

    /**
     * cuihongyuan
     * 获取计算参考站时的海拔差限制
     *
     * @param altitude 参考站海拔
     * @return 海拔差限制
     */
    private static double getAltitudeDiffLimit(double altitude) {
        double altitudeDiffLimit;
        if (altitude < 1200) {
            altitudeDiffLimit = 1000;
        } else {
            altitudeDiffLimit = 4500;
        }
        return altitudeDiffLimit;
    }

    /**
     * cuihonhyuan
     * 根据日极值，插值计算24小时的小时极值
     * hour(0-23)
     * element (D0,D05,D10,P,TG,T)
     *
     * @param element  要素
     * @param awsConf  原始站
     * @param datetime 资料时间
     * @param bgc      原始站 - 参考站海拔
     * @param maxDay   最大值
     * @param minDay   最小值
     * @return 要素插值需要的x，y的序列
     */
    private static InterpolateSeq getMaxMinOf24hour(MeteoElement element, BaseStationInfo awsConf,
                                                    LocalDateTime datetime, double bgc, double maxDay, double minDay) {
        SunRiseSetTime sunRiseSetTime;
        sunRiseSetTime = getSunRiseH12(awsConf, datetime);

        InterpolateSeq interpolateSeq;
        interpolateSeq = getXYSeq(element, maxDay, minDay, bgc, sunRiseSetTime);

        interpolateSeq.setMaxHour(enlg3(interpolateSeq.getX(), interpolateSeq.getyMax(), datetime.getHour()));
        interpolateSeq.setMinHour(enlg3(interpolateSeq.getX(), interpolateSeq.getyMin(), datetime.getHour()));
        return interpolateSeq;

    }

    /**
     * cuihonhyuan
     * 获取要素插值需要的x，y的序列
     *
     * @param element        要素
     * @param MaxDay         最大值
     * @param MinDay         最小值
     * @param bgc            海拔差
     * @param sunRiseSetTime 日出时间和太阳高度角最大时间
     * @return 要素插值需要的x，y的序列
     */
    private static InterpolateSeq getXYSeq(MeteoElement element, double MaxDay, double MinDay, double bgc, SunRiseSetTime sunRiseSetTime) {
        InterpolateSeq interpolateSeq = new InterpolateSeq();
        if (element == MeteoElement.GST_5cm) {
            double dayDiff = (MaxDay - MinDay);
            double fff = dayDiff / 18.0;
            if (dayDiff < 13.0) {
                fff = 0.0;
            }
            if (dayDiff < 10.0) {
                fff = -0.6;
            }
            if (dayDiff < 8.0) {

                //如果日较差太小，扩大它
                fff = -0.9;
            }
            double Max, Min;
            //拔高相差很大的站还是有问题 20130702？？？？
            Max = MaxDay - fff - 0.85 * bgc / 100.0;
            Min = MinDay + fff - 0.6 * bgc / 100.0;

            //20130626 added! 因为阈值为90%
            Max += 3.0;
            //20130626 added!
            Min -= 1.0;

            interpolateSeq.getX()[0] = 0.0;
            //5cm地温最低出现在日出后1小时
            interpolateSeq.getX()[1] = sunRiseSetTime.getSunRiseTime() + 1.0;
            interpolateSeq.getX()[2] = sunRiseSetTime.getTimeOfMaxSolaAltitudeAngle() + 2.0 + 2.5;
            //x的值表示时间 (时间长度为25小时)
            interpolateSeq.getX()[3] = 24.0;

            double Max_Min = (Max - Min) / 3.8;
            double Min_Max = (Max - Min) / 5.0;
            interpolateSeq.getyMax()[2] = Max;
            interpolateSeq.getyMax()[1] = interpolateSeq.getyMax()[2] - Max_Min;
            interpolateSeq.getyMax()[0] = interpolateSeq.getyMax()[3] = interpolateSeq.getyMax()[1] / 2.0 + interpolateSeq.getyMax()[2] / 2.0 - 1.8;
            interpolateSeq.getyMin()[1] = Min;
            interpolateSeq.getyMin()[2] = interpolateSeq.getyMin()[1] + Min_Max;
            interpolateSeq.getyMin()[0] = interpolateSeq.getyMin()[3] = interpolateSeq.getyMin()[1] / 2.0 + interpolateSeq.getyMin()[2] / 2.0 - 1.8;

        } else if (element == MeteoElement.GST) {
            double Max, Min;
            double fff = (MaxDay - MinDay) / 8.0;
            Max = MaxDay - fff - 0.5 * bgc / 100.0;
            Min = MinDay + fff * .95 - 0.5 * bgc / 100.0;
            //由于0厘米最高、最低来自于长年代的资料，而长年代人工观测资料最高观测是雪面温度（有积雪时），而自动站为雪中温度，为此，必须对该情况做一定调整
            if (Min < -35.0) {
                Max += 4.0;
            }
            interpolateSeq.getX()[0] = 0.0;
            interpolateSeq.getX()[1] = sunRiseSetTime.getSunRiseTime();
            interpolateSeq.getX()[2] = sunRiseSetTime.getTimeOfMaxSolaAltitudeAngle() + 2.0;
            //x的值表示时间 (时间长度为25小时)
            interpolateSeq.getX()[3] = 24.0;

            double Max_Min = (Max - Min) / 5.2;
            double Min_Max = (Max - Min) / 7.1;

            interpolateSeq.getyMax()[2] = Max;
            interpolateSeq.getyMax()[1] = interpolateSeq.getyMax()[2] - Max_Min;
            interpolateSeq.getyMax()[0] = interpolateSeq.getyMax()[3] = interpolateSeq.getyMax()[1] / 2.0 + interpolateSeq.getyMax()[2] / 2.0 - 1.8;

            interpolateSeq.getyMin()[1] = Min;
            interpolateSeq.getyMin()[2] = interpolateSeq.getyMin()[1] + Min_Max;
            interpolateSeq.getyMin()[0] = interpolateSeq.getyMin()[3] = interpolateSeq.getyMin()[1] / 2.0 + interpolateSeq.getyMin()[2] / 2.0 - 1.8;

        } else if (element == MeteoElement.GST_10cm) {
            double Max, Min;
            double fff = (MaxDay - MinDay) / 15.0;
            if (MaxDay - MinDay < 11.0) {
                fff = -0.5;
            }
            if (MaxDay - MinDay < 8.0) {
                fff = -0.9;
            }

            Max = MaxDay - fff * 1.0 - 0.8 * bgc / 100.0;
            Min = MinDay + fff * 1.0 - 0.5 * bgc / 100.0;

            //20130626 added! 因为阈值为90%
            Max += 1;
            Min -= 1;

            interpolateSeq.getX()[0] = 0.0;
            //10cm地温最低出现在日出后2小时
            interpolateSeq.getX()[1] = sunRiseSetTime.getSunRiseTime() + 2.0;
            interpolateSeq.getX()[2] = sunRiseSetTime.getTimeOfMaxSolaAltitudeAngle() + 2.0 + 3.5;
            //x的值表示时间 (时间长度为25小时)
            interpolateSeq.getX()[3] = 24.0;

            double Max_Min = (Max - Min) / 3.5;
            double Min_Max = (Max - Min) / 4.2;

            interpolateSeq.getyMax()[2] = Max;
            interpolateSeq.getyMax()[1] = interpolateSeq.getyMax()[2] - Max_Min;
            interpolateSeq.getyMax()[0] = interpolateSeq.getyMax()[3] = interpolateSeq.getyMax()[1] / 2.0 + interpolateSeq.getyMax()[2] / 2.0 - 1.8;
            interpolateSeq.getyMin()[1] = Min;
            interpolateSeq.getyMin()[2] = interpolateSeq.getyMin()[1] + Min_Max;
            interpolateSeq.getyMin()[0] = interpolateSeq.getyMin()[3] = interpolateSeq.getyMin()[1] / 2.0 + interpolateSeq.getyMin()[2] / 2.0 - 1.8;

        } else if (element == MeteoElement.PRS) {
            double Max, Min;

            double fff = (MaxDay - MinDay) / 16.0;
            Max = MaxDay - fff;
            Min = MinDay + 2.0 * fff;

            interpolateSeq.getX()[0] = 0.0;
            interpolateSeq.getX()[1] = sunRiseSetTime.getSunRiseTime();
            interpolateSeq.getX()[2] = sunRiseSetTime.getTimeOfMaxSolaAltitudeAngle() + 2.0;
            //x的值表示时间
            interpolateSeq.getX()[3] = 23.0;

            double Max_Min = (Max - Min) / 4.2;
            double Min_Max = (Max - Min) / 4.5;

            interpolateSeq.getyMax()[1] = Max;
            interpolateSeq.getyMax()[2] = interpolateSeq.getyMax()[1] - Max_Min;
            interpolateSeq.getyMax()[0] = interpolateSeq.getyMax()[3] = interpolateSeq.getyMax()[1] / 2.0 + interpolateSeq.getyMax()[2] / 2.0 - 1.8;
            interpolateSeq.getyMin()[2] = Min;
            interpolateSeq.getyMin()[1] = interpolateSeq.getyMin()[2] + Min_Max;
            interpolateSeq.getyMin()[0] = interpolateSeq.getyMin()[3] = interpolateSeq.getyMin()[1] / 2.0 + interpolateSeq.getyMin()[2] / 2.0 - 1.8;

        } else if (element == MeteoElement.LGST) {
            double Max, Min;

            double fff = (MaxDay - MinDay) / 8.0;
            Max = MaxDay - fff * 1.5 - 0.2 * bgc / 100.0;
            Min = MinDay + fff * 1.0 - 0.6 * bgc / 100.0;

            //由于0厘米最高、最低来自于长年代的资料，而长年代人工观测资料最高观测是雪面温度（有积雪时），而自动站为雪中温度，为此，必须对该情况做一定调整
            //而草温资料的极值来源于0cm地温
            if (Min < -28.0) {
                Max += 1.0;
            }
            if (Min < -32.0) {
                Max += 1.5;
            }
            if (Min < -38.0) {
                Max += 1.5;
            }

            interpolateSeq.getX()[0] = 0.0;
            interpolateSeq.getX()[1] = sunRiseSetTime.getSunRiseTime();
            interpolateSeq.getX()[2] = sunRiseSetTime.getTimeOfMaxSolaAltitudeAngle() + 2.0;
            //x的值表示时间 (时间长度为25小时)
            interpolateSeq.getX()[3] = 24.0;

            double Max_Min = (Max - Min) / 5.0;
            double Min_Max = (Max - Min) / 8.6;

            interpolateSeq.getyMax()[2] = Max;
            interpolateSeq.getyMax()[1] = interpolateSeq.getyMax()[2] - Max_Min;
            interpolateSeq.getyMax()[0] = interpolateSeq.getyMax()[3] = interpolateSeq.getyMax()[1] / 2.0 + interpolateSeq.getyMax()[2] / 2.0 - 1.8;
            interpolateSeq.getyMin()[1] = Min;
            interpolateSeq.getyMin()[2] = interpolateSeq.getyMin()[1] + Min_Max;
            interpolateSeq.getyMin()[0] = interpolateSeq.getyMin()[3] = interpolateSeq.getyMin()[1] / 2.0 + interpolateSeq.getyMin()[2] / 2.0 - 1.8;

        } else if (element == MeteoElement.TEM) {
            double Max, Min;
            double fff = (MaxDay - MinDay) / 4.0;
            Max = MaxDay - fff - 0.6 * bgc / 100.0;
            Min = MinDay + fff - 0.6 * bgc / 100.0;
            // 当温度很低时，Tqc数据一般偏高，所以做些许调整 20130629
            if (MinDay < -35.0) {
                Max += 0.2;
            }
            // 逐步增加
            if (MinDay < -40.0) {
                Max += 0.5;
            }
            // 逐步增加
            if (MinDay < -42.0) {
                Max += 0.8;
            }
            // 逐步增加
            if (MinDay < -45.0) {
                Max += 0.5;
            }
            interpolateSeq.getX()[0] = 0.0;
            interpolateSeq.getX()[1] = sunRiseSetTime.getSunRiseTime();
            interpolateSeq.getX()[2] = sunRiseSetTime.getTimeOfMaxSolaAltitudeAngle() + 2.0;
            // x的值表示时间 (时间长度为25小时)
            interpolateSeq.getX()[3] = 24.0;
            double Max_Min = (Max - Min) / 2.8;
            double Min_Max = (Max - Min) / 4.0;
            interpolateSeq.getyMax()[2] = Max;
            interpolateSeq.getyMax()[1] = interpolateSeq.getyMax()[2] - Max_Min;
            interpolateSeq.getyMax()[0] = interpolateSeq.getyMax()[3] = interpolateSeq.getyMax()[1] / 2.0 + interpolateSeq.getyMax()[2] / 2.0 - 1.8;
            interpolateSeq.getyMin()[1] = Min;
            interpolateSeq.getyMin()[2] = interpolateSeq.getyMin()[1] + Min_Max;
            interpolateSeq.getyMin()[0] = interpolateSeq.getyMin()[3] = interpolateSeq.getyMin()[1] / 2.0 + interpolateSeq.getyMin()[2] / 2.0 - 1.8;
        }
        return interpolateSeq;
    }


    /**
     * 一元三点不等距插值
     *
     * @param x : 插值x序列
     * @param y : 插值y序列
     * @param t : 时间小时值
     * @return : double,24小时的小时极值
     * @author : When6passBye
     * @date : 2019/8/21 2:26 PM
     */
    private static double enlg3(double[] x, double[] y, double t) {
        int n = 4;
        int i, j, k, m;
        double z, s;
        if (t <= x[1]) {
            k = 0;
            m = 2;
        } else if (t >= x[n - 2]) {
            k = n - 3;
            m = n - 1;
        } else {
            k = 1;
            m = n;
            while (m - k != 1) {
                i = (k + m) / 2;
                if (t < x[i - 1]) {
                    m = i;
                } else {
                    k = i;
                }
            }
            k = k - 1;
            m = m - 1;
            if (Math.abs(t - x[k]) < Math.abs(t - x[m])) {
                k = k - 1;
            } else {
                m = m + 1;
            }
        }
        z = 0.0;
        for (i = k; i <= m; i++) {
            s = 1.0;
            for (j = k; j <= m; j++) {
                if (j != i) {
                    s = s * (t - x[j]) / (x[i] - x[j]);
                }
            }
            z = z + s * y[i];
        }
        return z;
    }

    /**
     * cuihongyuan
     * 计算日出时间和太阳高度角最大时间
     * 年月日为北京时间
     * cuihongyuan
     *
     * @param awsConf  原始站
     * @param dateTime 时间
     * @return 该日日出时间和太阳高度角最大时间
     */
    private static SunRiseSetTime getSunRiseH12(BaseStationInfo awsConf, LocalDateTime dateTime) {
        SunRiseSetTime sunTime = sunriseAndSunsetLocalTime(dateTime, awsConf);
        //（太阳时）日出时间 整数部分
        int sunrisetime = (int) sunTime.getSunRiseTime();
        int sunsettime = (int) sunTime.getSunSetTime();
        // 获取浮点字符串的小数部分，小数点后2位
        // 日出时间小数部分
        double decimalPart = sunTime.getSunRiseTime() - (int) sunTime.getSunRiseTime();
        // 日落时间小数部分
        double decimalPartSet = sunTime.getSunSetTime() - (int) sunTime.getSunSetTime();
        BigDecimal bigD = new BigDecimal(decimalPart);
        BigDecimal bigDSet = new BigDecimal(decimalPartSet);
        // 小数部分，四舍五入两位
        double decimalPartP2 = bigD.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        double decimalPartSet2 = bigDSet.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        // 开始分钟
        int startMin = (int) (decimalPartP2 * 60.0);
        // 结束分钟
        int endMin = (int) (decimalPartSet2 * 60);
        double lon = awsConf.getLongitude();
        // 经度值小数点后两位
        double lonDecimalP2 = (new BigDecimal(lon - (int) lon)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
        int iLon;
        if (Math.round(lonDecimalP2) >= 30) {
            // 经度整数部分加1
            iLon = 1 + (int) lon;
        } else {
            iLon = (int) lon;
        }
        int year = dateTime.getYear();
        int month = dateTime.getMonthValue();
        int dayOfMonth = dateTime.getDayOfMonth();
        // 日出时间北京时
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, sunrisetime, startMin + (-4 * (iLon - 120)), 0);
        LocalDateTime bjtimeSunraise = LocalDateTime.of(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND)
        );
        calendar.clear();
        //日出时间北京时
        sunTime.setSunRiseTime(bjtimeSunraise.getHour() + bjtimeSunraise.getMinute() / 60.0);
        // 日落时间 北京时
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(year, month, dayOfMonth, sunsettime, endMin + (-4 * (iLon - 120)), 0);
        LocalDateTime bjtimeSunset = LocalDateTime.of(
                calendar2.get(Calendar.YEAR),
                calendar2.get(Calendar.MONTH) + 1,
                calendar2.get(Calendar.DAY_OF_MONTH),
                calendar2.get(Calendar.HOUR_OF_DAY),
                calendar2.get(Calendar.MINUTE),
                calendar2.get(Calendar.SECOND)
        );
        calendar2.clear();
        // 日落时间北京时
        sunTime.setSunSetTime(bjtimeSunset.getHour() + bjtimeSunset.getMinute() / 60.0);
        // 太阳高度角北京时
        sunTime.setTimeOfMaxSolaAltitudeAngle(sunTime.getSunRiseTime()
                + sunTime.getTimeOfMaxSolaAltitudeAngle() / 2.0);
        return sunTime;
    }

    /**
     * cuihongyuan
     * 地平时日出日落计算
     *
     * @param datetime 资料时间
     * @param awsConf  原始站
     * @return SunRiseSetTime
     */
    private static SunRiseSetTime sunriseAndSunsetLocalTime(LocalDateTime datetime, BaseStationInfo awsConf) {
        SunRiseSetTime setTime = new SunRiseSetTime();
        //时差
        double dblEq;
        //修正值
        double dblN0;
        //赤纬
        double dblD;
        double dblQ;
        double dblTemp;
        double ArcSin;
        //半日可照时数
        float Tb;
        //按天数排列的积日
        long lnN;
        int year = datetime.getYear();
        // getDayOfYear 比c++的值大1
        lnN = datetime.getDayOfYear() - 1;
        dblTemp = (new BigDecimal(0.25 * (year - 1985))).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        dblN0 = 79.6764 + 0.2422 * (year - 1985) - Math.floor(dblTemp);
        //按正午12时考虑
        dblQ = 2 * Math.PI * 57.3 * (lnN + (12 - awsConf.getLongitude() / 15) / 24 - dblN0)
                / 365.2422;
        dblD = 0.3723 + 23.2567 * Math.sin(dblQ * Math.PI / RIGHT_ANGLE) + 0.1149 * Math.sin(2 * dblQ * Math.PI / RIGHT_ANGLE)
                - 0.1712 * Math.sin(3 * dblQ * Math.PI / RIGHT_ANGLE) - 0.758 * Math.cos(dblQ * Math.PI / RIGHT_ANGLE)
                + 0.3656 * Math.cos(2 * dblQ * Math.PI / RIGHT_ANGLE) + 0.0201 * Math.cos(3 * dblQ * Math.PI / RIGHT_ANGLE);
        dblEq = (0.0028 - 1.9857 * Math.sin(dblQ * Math.PI / RIGHT_ANGLE) + 9.9059 * Math.sin(2 * dblQ * Math.PI / RIGHT_ANGLE)
                - 7.0924 * Math.cos(dblQ * Math.PI / RIGHT_ANGLE) - 0.6882 * Math.cos(2 * dblQ * Math.PI / RIGHT_ANGLE)) / 60;
        dblTemp = Math.sin(
                (45 + (awsConf.getLatitude() - dblD + (float) 34 / (float) 60) / 2) * Math.PI
                        / RIGHT_ANGLE) * Math.sin((45 - (awsConf.getLatitude() - dblD - (float) 34 / (float) 60) / 2)
                * Math.PI / RIGHT_ANGLE);
        if (dblTemp < 0) {
            setTime.setSunRiseTime(12);
            setTime.setSunSetTime(12);
            setTime.setTimeOfMaxSolaAltitudeAngle(0);
        }
        dblTemp = Math.sqrt(dblTemp / (Math.cos(awsConf.getLatitude() * Math.PI / RIGHT_ANGLE) * Math.cos(dblD * Math.PI / RIGHT_ANGLE)));
        if (dblTemp >= 1) {
            setTime.setSunRiseTime(0);
            setTime.setSunSetTime(24);
            setTime.setTimeOfMaxSolaAltitudeAngle(24);
        } else {
            ArcSin = Math.atan(dblTemp / Math.sqrt(1 - dblTemp * dblTemp));
            Tb = (float) (2 * ArcSin * RIGHT_ANGLE / Math.PI / 15);
            BigDecimal bgSunRise = new BigDecimal(12 - Tb - dblEq);
            double rise = bgSunRise.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            BigDecimal bgSunSet = new BigDecimal(12 + Tb - dblEq);
            double set = bgSunSet.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            setTime.setSunRiseTime(rise);
            setTime.setSunSetTime(set);
            setTime.setTimeOfMaxSolaAltitudeAngle(2 * Tb);
        }
        return setTime;
    }

    /**
     * dangjinhu
     * 关闭流
     *
     * @param bw  BufferedWriter流
     * @param osw OutputStreamWriter流
     * @param out FileOutputStream流
     */
    public static void closeStream(BufferedWriter bw, OutputStreamWriter osw, FileOutputStream out) {
        if (bw != null) {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (osw != null) {
            try {
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 提取出来的重复方法，根据日期判断是缺测还是没有此观测任务
     *
     * @param datetime :
     * @param conf     :
     * @return : int
     * @author : When6passBye
     * @date : 2019/6/28 4:09 PM
     */
    public static QualityCode lackOrNoTask(LocalDateTime datetime, MeteoElementObsStateInfo conf) {
        //降水加盖标志
        int lCapFlag = 0;
        //年月日时分
        String timeObserv;
        String stopTimeNow;
        String startTimeNow;
        String stopTimePhe;
        String startTimePhe;
        SSTime ssTime = conf.getSsTime();
        int _start = ssTime.getStartMonth().getMonth();
        int _stop = ssTime.getEndMonth().getMonth();
        StringBuilder sb = new StringBuilder();
        if (_stop != 0 && _start != 0) {
            timeObserv = String.format("%04d%02d%02d%02d%02d", datetime.getYear(), datetime.getMonth().getValue(), datetime.getDayOfMonth(), datetime.getHour(), datetime.getMinute());
            //观测加盖时间
            stopTimeNow = String.format("%04d%02d%02d%02d%02d", datetime.getYear(), _stop, 1, 0, 0);
            startTimeNow = String.format("%04d%02d%02d%02d%02d", datetime.getYear(), _start, 1, 0, 0);

            stopTimePhe = pheDay(stopTimeNow);
            startTimePhe = pheDay(startTimeNow);

            String year = timeObserv.substring(0, 4);
            //复制年份
            sb.append(stopTimePhe);
            sb.replace(0, 4, year);
            stopTimePhe = sb.toString();
            sb.setLength(0);

            sb.append(startTimePhe);
            sb.replace(0, 4, year);
            startTimePhe = sb.toString();
            sb.setLength(0);

            //转换成日期做比较
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
            try {
                Date stopTimePheDate = sdf.parse(stopTimePhe);
                Date startTimePheDate = sdf.parse(startTimePhe);
                Date timeObservDate = sdf.parse(timeObserv);
                //开始时间早于结束时间
                if (startTimePheDate.before(stopTimePheDate)) {
                    if (stopTimePheDate.before(timeObservDate) || timeObservDate.before(startTimePheDate)) {
                        lCapFlag = 1;
                    }
                } else {
                    if (stopTimePheDate.before(timeObservDate) && timeObservDate.before(startTimePheDate)) {
                        lCapFlag = 1;
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        //debug日志先不管
        if (_start != 0 && _stop != 0 && lCapFlag == 1) {
            return QualityCode.Z_QC_NO_TASK;
        } else {
            return QualityCode.Z_QC_LACK;
        }

    }

    /**
     * 要解析成mmddhh的格式，只有一位的需要在前面补0
     *
     * @param time :
     * @return : java.lang.String
     * @author : When6passBye
     * @date : 2019/6/28 5:28 PM
     */
    public static String ifContain0(int time) {
        StringBuilder sb = new StringBuilder();
        if (time < 10) {
            sb.append("0").append(time);
        } else {
            sb.append(time);
        }
        return sb.toString();

    }

    /**
     * 气象日界时间是从前天北京时间20:00到当天的20:00，世界时间12:00-12:00，此函数按北京气象时间是：年月日时分，外部内存至少为12
     *
     * @param now :传入的时间
     * @return : java.lang.String
     * @author : When6passBye
     * @date : 2019/6/28 3:17 PM
     */
    private static String pheDay(String now) {
        if (null == now || "".equals(now)) {
            return null;
        }
        int[] month_day = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int iYear;
        int iMonth;
        int iDay;
        int iHour = 20;
        int iMinute;
        iYear = Integer.parseInt(now.substring(0, 4));
        iMonth = Integer.parseInt(now.substring(4, 6));
        iDay = Integer.parseInt(now.substring(6, 8));
        iMinute = Integer.parseInt(now.substring(10, 12));
        if (iDay == 1) {
            //前一个月是2月
            if ((iMonth - 1) == 2) {
                //判断是否闰年
                int x = ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0)) ? 0 : 1;
                iDay = month_day[2] + x;
                iMonth -= 1;
            } //前一个月是12月
            else if ((iMonth - 1) == 0) {
                iDay = month_day[12];
                iMonth = 12;
                iYear -= 1;
            } else {
                iDay = month_day[iMonth - 1];
                iMonth -= 1;
            }
        } else {
            iDay -= 1;
        }
        return String.format("%04d%02d%02d%02d%02d", iYear, iMonth, iDay, iHour, iMinute);
    }

    /**
     * 日照日出日落
     *
     * @author : When6passBye
     * @date : 2019/7/30 3:53 PM
     */
    public static class SunRiseAndSetTime {

        double Trise = 0, Tset = 0, Ta = 0;

        public SunRiseAndSetTime() {
        }

        public SunRiseAndSetTime(double trise, double tset, double ta) {
            Trise = trise;
            Tset = tset;
            Ta = ta;
        }

        public double getTrise() {
            return Trise;
        }

        public void setTrise(double trise) {
            Trise = trise;
        }

        public double getTset() {
            return Tset;
        }

        public void setTset(double tset) {
            Tset = tset;
        }

        public double getTa() {
            return Ta;
        }

        public void setTa(double ta) {
            Ta = ta;
        }
    }


    /**
     * 地平时日出日落计算
     *
     * @param year   :
     * @param month  :
     * @param day    :
     * @param strLon :
     * @param strLat :
     * @param srs    :
     * @return : void
     * @author : When6passBye
     * @date : 2019/7/4 9:23 AM
     */
    public static void sunriseAndSunsetLocalTime(int year, int month, int day, double strLon, double strLat, SunRiseAndSetTime srs) {
        double dblEq;    //'时差
        double dblN0;    //修正值
        double dblD;    //赤纬
        double dblQ;
        double dblTemp;
        double ArcSin;
        float Tb;    //半日可照时数
        long lnN;   //按天数排列的积日
        lnN = getYday(year, month, day);
        dblTemp = Double.parseDouble(String.format("%.2f", 0.25 * (year - 1985)));
        dblN0 = 79.6764 + 0.2422 * (year - 1985) - floor(dblTemp);
        dblQ = 2 * Math.PI * 57.3 * (lnN + (12 - strLon / 15) / 24 - dblN0)
                / 365.2422;   //按正午12时考虑
        dblD = 0.3723 + 23.2567 * sin(dblQ * Math.PI / RIGHT_ANGLE) + 0.1149 * sin(2 * dblQ * Math.PI / RIGHT_ANGLE)
                - 0.1712 * sin(3 * dblQ * Math.PI / RIGHT_ANGLE) - 0.758 * cos(dblQ * Math.PI / RIGHT_ANGLE)
                + 0.3656 * cos(2 * dblQ * Math.PI / RIGHT_ANGLE) + 0.0201 * cos(3 * dblQ * Math.PI / RIGHT_ANGLE);
        dblEq = (0.0028 - 1.9857 * sin(dblQ * Math.PI / RIGHT_ANGLE) + 9.9059 * sin(2 * dblQ * Math.PI / RIGHT_ANGLE)
                - 7.0924 * cos(dblQ * Math.PI / RIGHT_ANGLE) - 0.6882 * cos(2 * dblQ * Math.PI / RIGHT_ANGLE)) / 60;
        dblTemp = sin(
                (45 + (strLat - dblD + (float) 34 / (float) 60) / 2) * Math.PI
                        / RIGHT_ANGLE)
                * sin(
                (45 - (strLat - dblD - (float) 34 / (float) 60) / 2)
                        * Math.PI / RIGHT_ANGLE);
        if (dblTemp < 0) {
            srs.setTrise(12);
            srs.setTset(12);
            srs.setTa(0);
        }
        dblTemp = sqrt(
                dblTemp
                        / (cos(strLat * Math.PI / RIGHT_ANGLE)
                        * cos(dblD * Math.PI / RIGHT_ANGLE)));
        if (dblTemp >= 1) {
            srs.setTrise(0);
            srs.setTset(24);
            srs.setTa(24);
        } else {
            ArcSin = atan(dblTemp / sqrt(1 - dblTemp * dblTemp));
            Tb = (float) (2 * ArcSin * RIGHT_ANGLE / Math.PI / 15);
            srs.setTrise((float) DRound((12 - Tb - dblEq), 0.5, 2));
            srs.setTset((float) DRound((12 + Tb - dblEq), 0.5, 2));
            srs.setTa(2 * Tb);
        }

    }

    /**
     * 浮点数四舍五入，n小数,data为0.5，如果需要特殊处理，data等于0.9则为下一位是1就往前进一位。
     *
     * @param x    :
     * @param data :
     * @param n    :
     * @return : double
     * @author : When6passBye
     * @date : 2019/7/4 9:21 AM
     */
    public static double DRound(double x, double data, int n) {
        //不是负数
        int isN = 0;
        if (x < 0) {
            isN = 1;
            x = -x;
        }
        int IValue = 1;
        int i = 0;
        for (i = 1; i <= n; i++) {
            IValue = IValue * 10;
        }
        int z;
        double v;
        z = (int) (x * IValue + data);
        v = (double) z / IValue;
        if (isN != 0) {
            v = -v;
        }
        return v;
    }

    /**
     * 获取这是一年中的第几天
     *
     * @param year  :
     * @param month :
     * @param day   :
     * @return : int
     * @author : When6passBye
     * @date : 2019/7/3 6:10 PM
     */
    private static int getYday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year - 1900, month - 1, day);
        return calendar.get(DAY_OF_YEAR);
    }

    /**
     * 根据气温修正气压质控范围
     *
     * @param range    : 阈值
     * @param altitude :
     * @param t        :
     * @return : void
     * @author : When6passBye
     * @date : 2019/7/3 3:19 PM
     */
    public static void calcPRange(MaxMinValues range, double altitude, double t) {
        double pMax = range.getMaxValue();
        double pMin = range.getMinValue();
        double b = altitude
                / (18400.00000
                * (1.00000
                + (t + altitude / 400.00000)
                / 273.00000));
        double max = pMax
                / Math.pow(10.00000,
                b);
        double min = pMin
                / Math.pow(10.00000,
                b);
        range.setMaxValue(max);
        range.setMinValue(min);

    }


    /**
     * 根据气温修正相对湿度质控范围
     * stdr = Stdev
     * emax,emin为当前已存储的umax、umin
     * umax、umin为修正后的正确值
     *
     * @param range :
     * @param t     :
     * @return : void
     * @author : When6passBye
     * @date : 2019/7/3 3:44 PM
     */
    private static void calcURange(MaxMinValues range, double t) {
        //饱和水汽压
        double bhsyq;
        double emax = range.getMaxValue();
        double emin = range.getMinValue();
        double stdr = range.getExceedingThreshold();
        double umax;
        double umin;
        //气温大于0用水面 小于等于0用冰面
        if (t > 0.0) {
            bhsyq = smbhsqy(t);
        } else {
            bhsyq = bmbhsqy(t);
        }
        //利用饱和水汽压订正置信域
        stdr = stdr * 100.0 / bhsyq;

        umax = 100.0 * emax / bhsyq;
        umin = 100.0 * emin / bhsyq;
        if (umax > 100.0) {
            umax = 100.0;
        }
        if (umin > 65.0) {
            umin = 65.0;
        }

        //计算置信域
        if (stdr < 8) {
            stdr = 8;
        }
        if (stdr > 12) {
            stdr = 15;
        }

        range.setMaxValue(umax);
        range.setMinValue(umin);
        range.setExceedingThreshold(stdr);

    }


}
