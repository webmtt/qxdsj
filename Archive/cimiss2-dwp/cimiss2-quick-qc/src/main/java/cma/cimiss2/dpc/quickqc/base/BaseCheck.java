
package cma.cimiss2.dpc.quickqc.base;

import cma.cimiss2.dpc.quickqc.bean.*;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
import cma.cimiss2.dpc.quickqc.cfg.AwsNatStationInfoCfg;
import cma.cimiss2.dpc.quickqc.cfg.FmaxMonthParaCfg;
import cma.cimiss2.dpc.quickqc.cfg.RHourParamCfg;
import cma.cimiss2.dpc.quickqc.cfg.TmaxMinDaychinaCfg;
import cma.cimiss2.dpc.quickqc.util.SurfUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.Map;

import static cma.cimiss2.dpc.quickqc.bean.Constants.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.MdosStationClass.AWS_NATION_BASE;
import static cma.cimiss2.dpc.quickqc.bean.enums.MdosStationClass.AWS_NATION_STD;
import static cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement.V01;
import static cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement.VVVV;
import static cma.cimiss2.dpc.quickqc.bean.enums.QualityCode.*;
import static cma.cimiss2.dpc.quickqc.util.SurfUtil.isLackOrNoTask;
import static cma.cimiss2.dpc.quickqc.util.SurfUtil.isNoTask;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description: 快速质控公共算法接口  </b><br>
 *
 * @author dengzhiheng
 * @version 1.0
 * @Note <b>ProjectName:</b> cimiss2-quick-qc <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.base <br><b>ClassName:</b> BaseCheck <br><b>Date:</b> 2019年8月1日 下午2:32:11
 */
public interface BaseCheck {

    /**
     * 配置文件初始化
     */
    Map<String, AwsNatStationInfo> CONFIG_MAP = AwsNatStationInfoCfg.getAwsNatStationInfoCfg().getAwsNatStationInfoMaps();
    /**
     * The Map max min.
     */
    Map<String, MaxMinDaychinaInfo> T_MAX_MIN = TmaxMinDaychinaCfg.getTmaxMinDaychinaCfg().getTmaxMinDaychinaInfoMaps();
    /**
     * The Map max mon.
     */
    Map<String, MaxMonthParaInfo> F_MAX_MON = FmaxMonthParaCfg.getFmaxMonthParaCfg().getFmaxMonthParaInfoMaps();
    /**
     * The Map max mon.
     */
    Map<String, MaxMonthParaInfo> R_MAX_MON = RHourParamCfg.getRHourParamCfg().getRHourParamInfoMaps();


    /**
     * setcode方法, 每个质控方法调用
     * 讲在方法内部对每个元素应该设置怎么样的质控码做详细判断
     *
     * @param stationCode : 站点编号
     * @param datetime    : 时间
     * @param element     : 标识质控元素的枚举
     * @param preCode     : 之前的质控码
     * @param newCode     : 新的质控码
     * @param qcType      : 质控步骤
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/19 6:24 PM
     */
    static QualityCode setSurfQcCode(String stationCode, LocalDateTime datetime, MeteoElement element, QualityCode preCode, QualityCode newCode, int qcType) {

        AwsNatStationInfo conf = CONFIG_MAP.get(stationCode);
        if (conf == null) {
            throw new NullPointerException("conf is null");
        }
        MeteoElementObsStateInfo meos = conf.findMeteoElementObsStateInfo(element);
        if (preCode == Z_QC_CORRECT || preCode == Z_QC_MOD) {
            //修正数据不做质控，认为是可用的
            return preCode;
        } else if (preCode != Z_QC_LACK) {
            //有实际观测值时，不考虑观测任务
            if (preCode == Z_QC_NO_QC || newCode.ordinal() > preCode.ordinal()) {
                return newCode;
            }
        } else {
            //缺测综合判断
            if (!isTask(stationCode, element)) {
                //无观测任务
                if (element == VVVV && isTask(stationCode, V01)) {
                    //有自动能见度时必须有人工能见度, 无此关联性临时将v改为vvvv规避此判断
                    return Z_QC_LACK;
                } else {
                    return Z_QC_NO_TASK;
                }
            } else if (isNoTask(element)) {
                return Z_QC_NO_TASK;
            }
            //不是正点，且属于人工观测，并且是下列元素
            else if (isNoTask(element, datetime, meos)) {
                //非正点不观测字段：观测任务为2或部分观测项
                return Z_QC_NO_TASK;
            }
            //有加盖 时间的观测任务非0特殊项
            else if (isLackOrNoTask(element)) {
                return SurfUtil.lackOrNoTask(datetime, meos);
            } else if (meos.getFlag() == AUTO) {
                return Z_QC_LACK;

            } //一般的观测任务为2的观测项，按定时时次
            else if (meos.getFlag() == HUMAN) {
                //能见度、云量、云高
                boolean isDoCheck = (conf.getStationClass() == AWS_NATION_STD.getCode() || conf.getStationClass() == AWS_NATION_BASE.getCode()) &&
                        datetime.getHour() == 8 || datetime.getHour() == 11 || datetime.getHour() == 14 || datetime.getHour() == 17 || datetime.getHour() == 20;
                //一般站三次定时
                if (isDoCheck) {
                    return Z_QC_LACK;
                } else {
                    return Z_QC_LACK;
                }
            } else {
                //观测任务标志在0,1,2之外的值，设置为不质控
                return Z_QC_NO_QC;
            }
        }
        //都没有进，说明也没有设置新的
        return preCode;

    }

    /**
     * Is task boolean.
     * 根据配置文件读取该站点是否有此要素的观测任务
     *
     * @param stationCode : 站点编号
     * @param element     : 质控要素
     * @return : bool值,true表示有观测任务
     * @author : When6passBye
     * @date : 2019/7/24 2:51 PM
     */
    static boolean isTask(String stationCode, MeteoElement element) {
        if (StringUtils.isNotBlank(stationCode)) {
            // 台站及观测方式（无观测0，自动观测1，人工观测2）
            AwsNatStationInfo info = BaseCheck.CONFIG_MAP.get(stationCode);
            if (info == null) {
                return false;
            }
            MeteoElementObsStateInfo meteoElementObsStateInfo = info.findMeteoElementObsStateInfo(element);
            if (meteoElementObsStateInfo == null) {
                return false;
            }
            return meteoElementObsStateInfo.getFlag() != NOTASK;
        }
        return false;
    }


    /**
     * Gets task type.
     * 根据配置文件读取该站点此要素的观测任务, 如果没有读取到相应的配置文件，一律返回0
     *
     * @param stationCode : 站点编号
     * @param element     : 质控要素
     * @return : bool值,true表示有观测任务
     * @author : When6passBye
     * @date : 2019/7/24 2:51 PM
     */
    static int getTaskType(String stationCode, MeteoElement element) {
        if (StringUtils.isNotBlank(stationCode)) {
            // 台站及观测方式（无观测0，自动观测1，人工观测2）
            AwsNatStationInfo info = BaseCheck.CONFIG_MAP.get(stationCode);
            if (info == null) {
                return 0;
            }
            MeteoElementObsStateInfo meteoElementObsStateInfo = info.findMeteoElementObsStateInfo(element);
            if (meteoElementObsStateInfo == null) {
                return 0;
            }
            return meteoElementObsStateInfo.getFlag();
        }
        return 0;
    }


    /**
     * Is ele checkable boolean.
     * 当前质控码是否可以更新 ，根据质控码优先级判断
     *
     * @param qc : 上一步质控码
     * @return : bool值,true表示可进入下一步检查
     * @author : When6passBye
     * @date : 2019/7/24 4:44 PM
     */
    static boolean isEleCheckable(QualityCode qc) {
        if (qc == Z_QC_OK || qc == Z_QC_DOUBT || qc == Z_QC_CORRECT || qc == Z_QC_MOD) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Is need range check boolean.
     * 检测一个台站是否需要进行范围值控制  1. 经纬度差大于1°，或者海拔差大于80m;
     *
     * @param obsv : 观测结果
     * @param conf : 配置结果（参考结果）
     * @return :bool值,true表示需要进行范围检查
     * @author : When6passBye
     * @date : 2019/7/24 3:46 PM
     */
    static boolean isNeedRangeCheck(BaseStationInfo obsv, BaseStationInfo conf) {
        return Math.abs(conf.getLongitude() - obsv.getLongitude()) > 1
                || Math.abs(conf.getLatitude() - obsv.getLatitude()) > 1
                || Math.abs(conf.getAltitude() - obsv.getAltitude()) > 80;
    }

    /**
     * Gets result qc code.
     * 获取合适的质控码 ，用于生成更正报标记
     *
     * @param newCode : 新code
     * @param oldCode : 旧code
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.Quality
     * @author : When6passBye
     * @date : 2019/7/25 9:25 AM
     */
    static QualityCode getResultQcCode(QualityCode newCode, QualityCode oldCode) {
        if (newCode == Z_QC_NO_QC || newCode == Z_QC_NO_TASK) {
            return oldCode;
        } else if (oldCode == Z_QC_MOD) {
            return oldCode;
        } else if (oldCode == Z_QC_CORRECT && newCode != Z_QC_MOD) {
            return oldCode;
        } else if (oldCode.getCode() > newCode.getCode()) {
            return oldCode;
        } else {
            return newCode;
        }
    }

    /**
     * 融合质控码，返回文件级的质控码
     *
     * @param qcMissing  :
     * @param qcLimit    :
     * @param qcRange    :
     * @param qcInternal :
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 4:56 PM
     */
    default QualityCode getQcResult(QualityCode qcMissing, QualityCode qcLimit, QualityCode qcRange, QualityCode qcInternal) {
        QualityCode qcResult = qcMissing;
        // 0,7,8
        if (qcMissing == Z_QC_NO_TASK || qcMissing == Z_QC_LACK) {
            // 无观测任务或者缺测，返回
            return qcResult;
        } // 有效值
        else if (qcMissing == Z_QC_OK) {
            //  0,1,2,9
            qcResult = qcLimit;
            //如果是ERROR才将其拦截下来
            if (qcLimit == Z_QC_ERROR) {
                return qcResult;
            } else { // 界限检查正确，下一步范围检查
                // -2  -1  0 1 2 9
                qcResult = qcRange;
                // -2 2 9
                if (qcRange == Z_QC_ERROR || qcRange == Z_QC_ERROR_NEG) {
                    return qcResult;
                } else {
                    // 一致性检查    0,1,9
                    if (qcInternal == Z_QC_ERROR) {
                        qcResult = qcInternal;
                        return qcResult;
                    } else { // 0， range检查是否为1或-1 ， 不可更新
                        if (qcResult == Z_QC_DOUBT || qcResult == Z_QC_DOUBT_NEG) {
                            return qcResult;
                        } else {  // 范围检查返回0， 返回内部一致性结果
                            //返回不是noQc的结果
                            qcResult = qcInternal;
                            if (qcResult == Z_QC_NO_QC) {
                                if (qcRange == Z_QC_NO_QC) {
                                    if (qcLimit == Z_QC_NO_QC) {
                                        return qcMissing;
                                    } else {
                                        return qcLimit;
                                    }
                                } else {
                                    return qcRange;
                                }
                            } else {
                                return qcResult;
                            }
                        }
                    }
                }
            }
        }

        return qcResult;
    }

    /**
     * 根据观测值判断要素是否缺测
     *
     * @param obsValue :
     * @return : boolean
     * @author : When6passBye
     * @date : 2019/8/22 11:33 AM
     */
    static boolean isEleLack(double obsValue) {
        return obsValue == LACK_NUM || obsValue == NOTASK_NUM;
    }

}