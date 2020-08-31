package cma.cimiss2.dpc.quickqc.surf.awshour;


import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cma.cimiss2.dpc.quickqc.bean.Constants.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.QualityCode.*;
import static cma.cimiss2.dpc.quickqc.util.CommonUtil.*;
import static java.lang.Math.abs;
import static java.lang.Math.max;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description: 内部一致性检查类  </b><br>
 * // 继承了BaseCheck 中默认实现的 default QualityCode internalCheck(Double maxValue, Double minValue, Double value, MeteoElement element)方法
 *
 * @author wuzuoqiang
 * @version 1.0
 * @Note <b>ProjectName:</b> cimiss2-quick-qc <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.surf.awshour <br><b>ClassName:</b> InternalCheck <br><b>Date:</b> 2019年8月1日 下午2:32:11
 */
public class InternalCheck implements BaseCheck {

    /**
     * 用于降水一致性检测
     * 将参与一致性检查的5个要素与其上一步质控码绑定在一起的对象
     * 调用set方法设值，如果该值不用参与质控，也可以不必设置
     *
     * @Author: When6passBye
     * @Date: 2019-09-03 10:09
     **/
    public static class PREInternalBean {

        public PREInternalBean() {
            setPre1h(LACK_NUM, Z_QC_NO_QC);
            setPre3h(LACK_NUM, Z_QC_NO_QC);
            setPre6h(LACK_NUM, Z_QC_NO_QC);
            setPre12h(LACK_NUM, Z_QC_NO_QC);
            setPre24h(LACK_NUM, Z_QC_NO_QC);
        }

        /**
         * 私有内部类，将质控码和观测元素以及观测值绑定起来
         *
         * @Author: When6passBye
         * @Date: 2019/9/3 3:09 PM
         **/
        private class CodeAndEle {

            /**
             * 观测值
             */
            private double value;
            /**
             * 质控码
             */
            private QualityCode code;
            /**
             * 观测元素
             */
            private MeteoElement element;

            CodeAndEle(double value, QualityCode code, MeteoElement element) {
                this.value = value;
                this.code = code;
                this.element = element;
            }

            double getValue() {
                return value;
            }

            void setValue(double value) {
                this.value = value;
            }

            QualityCode getCode() {
                return code;
            }

            void setCode(QualityCode code) {
                this.code = code;
            }

            MeteoElement getElement() {
                return element;
            }

            void setElement(MeteoElement element) {
                this.element = element;
            }
        }

        /**
         * 一小时降水
         */
        private CodeAndEle pre1h;
        /**
         * 三小时降水
         */
        private CodeAndEle pre3h;
        /**
         * 六小时降水
         */
        private CodeAndEle pre6h;
        /**
         * 十二小时降水
         */
        private CodeAndEle pre12h;
        /**
         * 二十四小时降水
         */
        private CodeAndEle pre24h;

        public CodeAndEle getPre1h() {
            return pre1h;
        }

        public void setPre1h(double value, QualityCode pre1code) {
            this.pre1h = new CodeAndEle(value, pre1code, PRE_1_HOUR);
        }

        public CodeAndEle getPre3h() {
            return pre3h;
        }

        public void setPre3h(double value, QualityCode pre3code) {
            this.pre3h = new CodeAndEle(value, pre3code, PRE_03_HOUR);
        }

        public CodeAndEle getPre6h() {
            return pre6h;
        }

        public void setPre6h(double value, QualityCode pre6code) {
            this.pre6h = new CodeAndEle(value, pre6code, PRE_06_HOUR);
        }

        public CodeAndEle getPre12h() {
            return pre12h;
        }

        public void setPre12h(double value, QualityCode pre12code) {
            this.pre12h = new CodeAndEle(value, pre12code, PRE_12_HOUR);
        }

        public CodeAndEle getPre24h() {
            return pre24h;
        }

        public void setPre24h(double value, QualityCode pre24code) {
            this.pre24h = new CodeAndEle(value, pre24code, PRE_24_HOUR);
        }
    }

    /**
     * 只有两个值的通用一致性检查
     * 供用户调用
     *
     * @param obsValue   : 值
     * @param compareVal : 比较值
     * @param element    : 参与质控的要素
     * @param obs        : 站点信息
     * @param dateTime   : 时间
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/9 10:49 AM
     */
    public static QualityCode internalCheck(double obsValue, double compareVal, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        QualityCode ret = Z_QC_NO_QC;
        if (BaseCheck.isEleLack(obsValue)) {
            setLogger(element, QC_TYPE_INTERNAL, ret, obsValue);
            return ret;
        }
        checkAndGetStationCode(dateTime, element, obs, QC_TYPE_INTERNAL);
        switch (element) {
            //风
            case F02:
            case F10:
            case FMAX:
            case FMOST:
                ret = windInnerCheck(obsValue, compareVal, element, dateTime, obs);
                break;
            //电线积冰
            case NS_DIA:
            case NS_PLY:
            case WE_DIA:
            case WE_PLY:
                ret = wireInnerCheck(obsValue, compareVal, element, dateTime, obs);
                break;
            //地温
            case GST_10cm:
                ret = gsInnerCheck(obsValue, compareVal, 16.0, element, dateTime, obs);
                break;
            case GST_15cm:
                ret = gsInnerCheck(obsValue, compareVal, 12.0, element, dateTime, obs);
                break;
            case GST_20cm:
            case GST_160cm:
                ret = gsInnerCheck(obsValue, compareVal, 10.0, element, dateTime, obs);
                break;
            case GST_40cm:
            case GST_320cm:
                ret = gsInnerCheck(obsValue, compareVal, 8.0, element, dateTime, obs);
                break;
            case GST_80cm:
                ret = gsInnerCheck(obsValue, compareVal, 10.0, element, dateTime, obs);
                break;
            //水汽压，露点温度的内部一致性
            case DPT:
                if (obsValue > DPT_LIMIT1) {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 1.2);
                } else if (obsValue > DPT_LIMIT2) {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 1.5);
                } else {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 1.7);
                }
                break;
            case VAP:
                if (obsValue > VAP_LIMIT) {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 0.8);
                } else {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 1.0);
                }
                break;
            case RHU_MIN:
            case RHU:
                ret = rhuInnerCheck(obsValue, compareVal, element, dateTime, obs);
                break;
            case PRE:
            case PRE_1_HOUR:
            case PRE_03_HOUR:
            case PRE_06_HOUR:
            case PRE_12_HOUR:
            case PRE_24_HOUR:
                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = innerCheck(obsValue, compareVal, element, dateTime, obs);
                }
                break;
            default:
                ret = innerCheck(obsValue, compareVal, element, dateTime, obs);
                break;

        }
        setLogger(element, QC_TYPE_INTERNAL, ret, obsValue);
        return ret;
    }

    /**
     * @param obsValue   : 值
     * @param compareVal : 比较值
     * @param element    : 参与质控的要素
     * @param dateTime   : 站点信息
     * @param obs        : 时间
     * @param preCode    : 上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 3:56 PM
     */
    public static QualityCode internalCheck(double obsValue, double compareVal, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret = Z_QC_NO_QC;
        if (!BaseCheck.isEleCheckable(preCode)) {
            //表示调用了，但是不满足质控条件，日志记录为未做质控，同时返回传入的质控码
            setLogger(element, QC_TYPE_INTERNAL, ret, obsValue);
            return preCode;
        }
        checkAndGetStationCode(dateTime, element, obs, QC_TYPE_INTERNAL);
        switch (element) {
            //风
            case F02:
            case F10:
            case FMAX:
            case FMOST:
                ret = windInnerCheck(obsValue, compareVal, element, dateTime, obs, preCode);
                break;
            //电线积冰
            case NS_DIA:
            case NS_PLY:
            case WE_DIA:
            case WE_PLY:
                ret = wireInnerCheck(obsValue, compareVal, element, dateTime, obs, preCode);
                break;
            //地温
            case GST_10cm:
                ret = gsInnerCheck(obsValue, compareVal, 16.0, element, dateTime, obs, preCode);
                break;
            case GST_15cm:
                ret = gsInnerCheck(obsValue, compareVal, 12.0, element, dateTime, obs, preCode);
                break;
            case GST_20cm:
            case GST_160cm:
                ret = gsInnerCheck(obsValue, compareVal, 10.0, element, dateTime, obs, preCode);
                break;
            case GST_40cm:
            case GST_320cm:
                ret = gsInnerCheck(obsValue, compareVal, 8.0, element, dateTime, obs, preCode);
                break;
            case GST_80cm:
                ret = gsInnerCheck(obsValue, compareVal, 10.0, element, dateTime, obs, preCode);
                break;
            //水汽压，露点温度的内部一致性
            case DPT:
                if (obsValue > DPT_LIMIT1) {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 1.2, element, dateTime, obs, preCode);
                } else if (obsValue > DPT_LIMIT2) {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 1.5, element, dateTime, obs, preCode);
                } else {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 1.7, element, dateTime, obs, preCode);
                }
                break;
            case VAP:
                if (obsValue > VAP_LIMIT) {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 0.8, element, dateTime, obs, preCode);
                } else {
                    ret = prsSeaDptVapInnerCheck(obsValue, compareVal, 1.0, element, dateTime, obs, preCode);
                }
                break;
            case RHU_MIN:
            case RHU:
                ret = rhuInnerCheck(obsValue, compareVal, element, dateTime, obs, preCode);
                break;
            case PRE:
            case PRE_1_HOUR:
            case PRE_03_HOUR:
            case PRE_06_HOUR:
            case PRE_12_HOUR:
            case PRE_24_HOUR:
                if (Math.abs(obsValue + TINY) >= EPTION) {
                    ret = innerCheck(obsValue, compareVal, element, dateTime, obs, preCode);
                }
                break;
            default:
                ret = innerCheck(obsValue, compareVal, element, dateTime, obs, preCode);
                break;

        }
        setLogger(element, QC_TYPE_INTERNAL, ret, obsValue);
        return ret;
    }

    /**
     * 降水需要R<R03<R06<R12<R24一起比较，若不通过，统一设置为可疑，通过就统一通过
     * 没有参与质控的，日志显示无记录或者no_qc ,参与记录的,正常记录
     *
     * @param bean     : 用于降水一致性检测,将参与一致性检查的5个要素与其上一步质控码绑定在一起的对象
     * @param dateTime :
     * @param obs      :
     * @return : cma.cimiss2.dpc.quickqc.surf.awshour.InternalCheck.PREInternalBean
     * @author : When6passBye
     * @date : 2019/9/3 10:26 AM
     */
    public static PREInternalBean innerCheckPRE(PREInternalBean bean, LocalDateTime dateTime, BaseStationInfo obs) {
        if (bean == null || dateTime == null || obs == null) {
            LOGGER.error(QC_TYPE_INTERNAL + PARAMS_INVALID);
            throw new IllegalArgumentException(QC_TYPE_INTERNAL + PARAMS_INVALID);
        }
        List<PREInternalBean.CodeAndEle> checks = new ArrayList<>();
        List<PREInternalBean.CodeAndEle> notChecks = new ArrayList<>();
        PREInternalBean.CodeAndEle[] vcs = {bean.getPre1h(), bean.getPre3h(), bean.getPre6h(), bean.getPre12h(), bean.getPre24h()};

        //检查数组参数是否符合要求,符合要求的放入checks
        for (PREInternalBean.CodeAndEle vc : vcs) {
            double value = vc.getValue();
            boolean isCheck = (!BaseCheck.isEleLack(value)) && (Math.abs(value + TINY) >= EPTION);
            if (isCheck) {
                checks.add(vc);
            } else {
                notChecks.add(vc);
            }
        }

        QualityCode ret = Z_QC_OK;
        //进行一致性检测
        if (checks.size() < 2) {
            if (checks.size() == 1) {
                ret = Z_QC_DOUBT;
            }
        } else {
            for (int i = 0; i < checks.size() - 1; i++) {
                Double a = checks.get(i).getValue();
                Double b = checks.get(i + 1).getValue();
                if (a > b) {
                    ret = Z_QC_DOUBT;
                    break;
                }
            }
        }
        //记录日志
        for (PREInternalBean.CodeAndEle codeAndEle : checks) {
            setLogger(codeAndEle.getElement(), QC_TYPE_INTERNAL, ret, codeAndEle.getValue());
            QualityCode finalRet = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, codeAndEle.getElement(), codeAndEle.getCode(), ret, INTERNAL);
            finalRet = BaseCheck.getResultQcCode(codeAndEle.getCode(), finalRet);
            codeAndEle.setCode(finalRet);
            setLogger(codeAndEle.getElement(), QC_TYPE_FILE, finalRet, codeAndEle.getValue());
        }
        for (PREInternalBean.CodeAndEle codeAndEle : notChecks) {
            setLogger(codeAndEle.getElement(), QC_TYPE_INTERNAL, Z_QC_NO_QC, codeAndEle.getValue());
            setLogger(codeAndEle.getElement(), QC_TYPE_FILE, codeAndEle.getCode(), codeAndEle.getValue());
        }

        return bean;
    }

    /**
     * 三个值的一致性检查
     *
     * @param obsValue : 观测值
     * @param minVal   : 小值,element为PRS_Sea的时候，传入算法值
     * @param maxVal   : 大值,element为PRS_Sea的时候，传入传感器海拔(米)
     * @param element  : 参与质控的要素
     * @param dateTime : 时间
     * @param obs      : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 3:58 PM
     */
    public static QualityCode innerCheck(double obsValue, double minVal, double maxVal, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        QualityCode ret = getInnerCheckCode(obsValue, minVal, maxVal, element, dateTime, obs);
        setLogger(element, QC_TYPE_INTERNAL, ret, obsValue);
        return ret;
    }

    /**
     * @param obsValue : 观测值
     * @param minVal   : 小值,element为PRS_Sea的时候，传入算法值
     * @param maxVal   : 大值,element为PRS_Sea的时候，传入传感器海拔(米)
     * @param element  : 参与质控的要素
     * @param dateTime : 时间
     * @param obs      : 观测站信息
     * @param preCode  : 上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 3:51 PM
     */
    public QualityCode innerCheck(double obsValue, double minVal, double maxVal, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        //缺测检查
        QualityCode ret = getInnerCheckCode(obsValue, minVal, maxVal, element, dateTime, obs);
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, INTERNAL);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        setLogger(element, QC_TYPE_INTERNAL, ret, obsValue);
        return ret;
    }

    /**
     * 提取出来的公用方法，三个值的一致性质控码
     *
     * @param obsValue : 观测值
     * @param minVal   : 小值,element为PRS_Sea的时候，传入算法值
     * @param maxVal   : 大值,element为PRS_Sea的时候，传入传感器海拔(米)
     * @param element  : 参与质控的要素
     * @param dateTime : 时间
     * @param obs      : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 3:51 PM
     */
    private static QualityCode getInnerCheckCode(double obsValue, double minVal, double maxVal, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        //缺测检查
        QualityCode ret = Z_QC_NO_QC;
        if (BaseCheck.isEleLack(obsValue)) {
            setLogger(element, QC_TYPE_INTERNAL, ret, obsValue);
            return ret;
        }
        //入参检查
        checkAndGetStationCode(dateTime, element, obs, QC_TYPE_INTERNAL);
        switch (element) {
            case PRS_Sea:
                ret = seaPressInnerCheck(obsValue, maxVal, maxVal, element, dateTime, obs);
                break;
            default:
                if (obsValue >= minVal && obsValue <= maxVal) {
                    ret = QualityCode.Z_QC_OK;
                } else {
                    ret = Z_QC_DOUBT;
                }
                break;
        }
        return ret;
    }

    /**
     * 内部一致性检查：日日照合计等于日日照之和
     * 直接供用户调用
     *
     * @param ssTimeData :小时日照时数数据,数组长度必须为24
     * @param ss24Data   :日照时数日合计
     * @param element    :小时日照时数观测元素或者日照时数日合计观测元素，传SS_TIME或者传SS24
     * @param dateTime   :时间
     * @param obs        :观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/21 11:09 AM
     */
    public QualityCode ssInnerCheck(double[] ssTimeData, double ss24Data, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        //入参检查
        QualityCode ret = getInnerQualityCode(ssTimeData, ss24Data, element, dateTime, obs);
        setLogger(element, QC_TYPE_INTERNAL, ret, ss24Data);
        return ret;
    }

    public QualityCode ssInnerCheck(double[] ssTimeData, double ss24Data, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret = getInnerQualityCode(ssTimeData, ss24Data, element, dateTime, obs);
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, INTERNAL);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        setLogger(element, QC_TYPE_INTERNAL, ret, ss24Data);
        return ret;
    }

    /**
     * 提取出来的公用方法，日照的一致性检查质控
     *
     * @param ssTimeData : 小时日照时数数据,数组长度必须为24
     * @param ss24Data   : 日照时数日合计
     * @param element    : 小时日照时数观测元素或者日照时数日合计观测元素，传SS_TIME或者传SS24
     * @param dateTime   : 时间
     * @param obs        : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 3:36 PM
     */
    private QualityCode getInnerQualityCode(double[] ssTimeData, double ss24Data, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        //入参检查
        if (ssTimeData == null || ssTimeData.length == 0 || ssTimeData.length != SSTIME) {
            LOGGER.error("ssInnerCheck: ssTimeData is invalid");
            throw new IllegalArgumentException("ssInnerCheck: ssTimeData is invalid");
        }
        checkAndGetStationCode(dateTime, element, obs, QC_TYPE_INTERNAL);
        QualityCode ret;
        double ssum = 0;
        for (int index = 0; index < SSTIME; index++) {
            ssum += ssTimeData[index];
        }
        if (abs(ss24Data - ssum) > -EPTION && abs(ss24Data - ssum) < EPTION) {
            ret = Z_QC_OK;
        } else {
            ret = Z_QC_DOUBT;
        }
        return ret;
    }

    /**
     * 两个值的一致性检查
     *
     * @param value       : 观测值
     * @param valueBigger :大一点的值
     * @param element     : 参与质控的元素
     * @param dateTime    : 时间
     * @param obs         : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 4:03 PM
     */
    private static QualityCode innerCheck(double value, double valueBigger, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        if (value <= valueBigger) {
            return QualityCode.Z_QC_OK;
        } else {
            return Z_QC_DOUBT;
        }
    }

    private static QualityCode innerCheck(double value, double valueBigger, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret;
        if (value <= valueBigger) {
            ret = QualityCode.Z_QC_OK;
        } else {
            ret = Z_QC_DOUBT;
        }
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, INTERNAL);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }


    /**
     * 检查海平面气压的内部一致性
     * 用户直接调用
     *
     * @param obsValue      : 观测值
     * @param calcValue     : 算法值
     * @param altitudePress : 海平面气压需要传入传感器海拔(米)
     * @param element       : 参与一致性检测的质控元素
     * @param dateTime      : 时间
     * @param obs           : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/21 10:29 AM
     */
    private static QualityCode seaPressInnerCheck(double obsValue, double calcValue, double altitudePress, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        if (element != PRS_Sea) {
            throw new IllegalArgumentException("element must be PRS_Sea");
        }
        if (altitudePress < SEAP_LIMIT1) {
            return prsSeaDptVapInnerCheck(obsValue, calcValue, 7.0);
        } else if (altitudePress < SEAP_LIMIT2) {
            return prsSeaDptVapInnerCheck(obsValue, calcValue, 13.0);
        } else {
            return prsSeaDptVapInnerCheck(obsValue, calcValue, 30.0);
        }
    }

    /**
     * 海平面，水汽压，露点温度使用
     *
     * @param obsValue  : 观测值
     * @param calcValue : 计算值
     * @param diffValue : 固定阈值
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/21 5:50 PM
     */
    private static QualityCode prsSeaDptVapInnerCheck(double obsValue, double calcValue, double diffValue) {
        if (obsValue - calcValue > diffValue || obsValue - calcValue > -diffValue) {
            //c里面返回的是这个，但是用户强调说不会返回这个，先留着
            // return Z_QC_ERROR;
            return Z_QC_DOUBT;
        } else {
            return Z_QC_OK;
        }
    }

    private static QualityCode prsSeaDptVapInnerCheck(double obsValue, double calcValue, double diffValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret;
        if (obsValue - calcValue > diffValue || obsValue - calcValue > -diffValue) {
            //c里面返回的是这个，但是用户强调说不会返回这个，先留着
            // return Z_QC_ERROR;
            ret = Z_QC_DOUBT;
        } else {
            ret = Z_QC_OK;
        }
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, INTERNAL);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }


    /**
     * 检查相对湿度,最小相对湿度内部一致性
     *
     * @param obsValueLow  : 最小相对湿度
     * @param obsValueHigh : 相对湿度
     * @param element      : 参与一致性检测的质控元素数组
     * @param dateTime     : 时间
     * @param obs          : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/21 9:24 AM
     */
    private static QualityCode rhuInnerCheck(double obsValueLow, double obsValueHigh, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        if (obsValueLow > obsValueHigh || obsValueHigh - obsValueLow > 50.0) {
            return Z_QC_DOUBT;
        } else {
            return Z_QC_OK;
        }
    }

    private static QualityCode rhuInnerCheck(double obsValueLow, double obsValueHigh, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret;
        if (obsValueLow > obsValueHigh || obsValueHigh - obsValueLow > 50.0) {
            ret = Z_QC_DOUBT;
        } else {
            ret = Z_QC_OK;
        }
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, INTERNAL);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }


    /**
     * 检查f02,f10风速的内部一致性 /检查fmax,fmost的内部一致性
     *
     * @param obsValue1 : 观测值1：2分钟平均风速 或 最大风fmax
     * @param obsValue2 : 观测值2：10分钟平均风速 或 极大风fmost
     * @param element   : 参与一致性检测的质控元素
     * @param dateTime  : 时间
     * @param obs       : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/21 9:21 AM
     */
    private static QualityCode windInnerCheck(double obsValue1, double obsValue2, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        if (abs(obsValue1 - obsValue2) <= max(6.0, max(obsValue1 / 3, obsValue2 / 3))) {
            return Z_QC_OK;
        } else {
            return Z_QC_DOUBT;
        }
    }

    private static QualityCode windInnerCheck(double obsValue1, double obsValue2, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret;
        if (abs(obsValue1 - obsValue2) <= max(6.0, max(obsValue1 / 3, obsValue2 / 3))) {
            ret = Z_QC_OK;
        } else {
            ret = Z_QC_DOUBT;
        }
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, INTERNAL);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }

    /**
     * 电线积冰直径和电线积冰厚度内部一致性
     *
     * @param obsValueDia :观测值1:电线积冰直径
     * @param obsValuePly :观测值2:电线积冰厚度
     * @param element     :参与一致性检测的质控元素
     * @param dateTime    :时间
     * @param obs         :观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/21 9:16 AM
     */
    private static QualityCode wireInnerCheck(double obsValueDia, double obsValuePly, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        if (obsValueDia >= obsValuePly) {
            return Z_QC_OK;
        } else {
            return Z_QC_DOUBT;
        }
    }

    private static QualityCode wireInnerCheck(double obsValueDia, double obsValuePly, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret;
        if (obsValueDia >= obsValuePly) {
            ret = Z_QC_OK;
        } else {
            ret = Z_QC_DOUBT;
        }
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, INTERNAL);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }

    /**
     * @param obsValue1 : 观测值1
     * @param obsValue2 : 观测值2
     * @param diffValue : 固定阈值
     * @param element   : 参与一致性检测的质控元素
     * @param dateTime  : 时间
     * @param obs       : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/21 9:12 AM
     */
    private static QualityCode gsInnerCheck(double obsValue1, double obsValue2, double diffValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        if (Math.abs(obsValue1 - obsValue2) <= diffValue) {
            return Z_QC_OK;
        } else {
            return Z_QC_DOUBT;
        }
    }

    /**
     * @param obsValue1 : 观测值1
     * @param obsValue2 : 观测值2
     * @param diffValue : 固定阈值
     * @param element   : 参与一致性检测的质控元素
     * @param dateTime  : 时间
     * @param obs       : 观测站信息
     * @param preCode   : 上一步质控码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/27 3:20 PM
     */
    private static QualityCode gsInnerCheck(double obsValue1, double obsValue2, double diffValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs, QualityCode preCode) {
        QualityCode ret;
        if (Math.abs(obsValue1 - obsValue2) <= diffValue) {
            ret = Z_QC_OK;
        } else {
            ret = Z_QC_DOUBT;
        }
        ret = BaseCheck.setSurfQcCode(obs.getStationCode(), dateTime, element, preCode, ret, INTERNAL);
        ret = BaseCheck.getResultQcCode(preCode, ret);
        return ret;
    }
}
