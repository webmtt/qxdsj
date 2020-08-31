package cma.cimiss2.dpc.quickqc.surf.awshour;

import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
import cma.cimiss2.dpc.quickqc.util.CommonUtil;

import java.time.LocalDateTime;

import static cma.cimiss2.dpc.quickqc.bean.Constants.*;
import static cma.cimiss2.dpc.quickqc.util.CommonUtil.*;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description: 缺测值检查类  </b><br>
 * // 继承了 BaseCheck 中默认实现的default QualityCode missingCheck(BaseStationInfo stationInfo, LocalDateTime datetime, MeteoElement element) 方法
 *
 * @author wuzuoqiang
 * @version 1.0
 * @Note <b>ProjectName:</b> cimiss2-quick-qc
 * <br><b>PackageName:</b> cma.cimiss2.dpc.quickqc.surf.awshour
 * <br><b>ClassName:</b> MissingCheck
 * <br><b>Date:</b> 2019年8月1日 下午2:34:39
 */
public class MissingCheck implements BaseCheck {

    /**
     * 缺测质控通用方法，返回质控码
     * （1）观测任务根据台站参数表设置执行，无设置时所有要素缺省按照无观测任务处理。
     * （2）数据不为缺测，正常质控；数据缺测，有观测任务标注为8，无观测任务标注为7。
     * （3）要素为人工观测时，数据缺测仅定时时次（基准站、基本站：08、11、14、17、20；一般站：08、14、20（BJT））标注为8，其余时次标注为7。
     * （基准站：1；基本站：2；一般站（4次） ：3；一般站 （3次）：4；无人自动站：5）
     *
     * @param obsValue : 值(用作判断如果配置文件为缺测，如果有值，还是按照正常流程走)
     * @param element  : 质控元素
     * @param dateTime : 时间
     * @param obs      : 观测站信息
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/20 9:25 AM
     */
    public static QualityCode missingCheck(double obsValue, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        String stationCode = checkAndGetStationCode(dateTime, element, obs, QC_TYPE_LACK);
        QualityCode ret;
        //没有观测任务
        if (!BaseCheck.isTask(stationCode, element)) {
            //即使配置文件为缺测，如果有值的话，还是按照正常流程走
            if (obsValue != LACK_NUM && obsValue != NOTASK_NUM) {
                ret = QualityCode.Z_QC_OK;
            } else {
                ret = QualityCode.Z_QC_NO_TASK;
            }
        } //有观测任务
        else {
            if (obsValue == LACK_NUM || obsValue == NOTASK_NUM) {
                ret = getHumanStationLackCode(dateTime, element, obs, stationCode);
            } else {
                ret = QualityCode.Z_QC_OK;
            }
        }
        ret = BaseCheck.setSurfQcCode(stationCode, dateTime, element, QualityCode.Z_QC_NO_QC, ret, LACK);
        setLogger(element, QC_TYPE_LACK, ret, obsValue);
        return ret;
    }

    /**
     * 要素为人工观测时，数据缺测仅定时时次
     *
     * @param dateTime    :时间
     * @param element     :观测元素
     * @param obs         :观测站信息
     * @param stationCode :观测站编码
     * @return : cma.cimiss2.dpc.quickqc.bean.enums.QualityCode
     * @author : When6passBye
     * @date : 2019/8/22 11:16 AM
     */
    private static QualityCode getHumanStationLackCode(LocalDateTime dateTime, MeteoElement element, BaseStationInfo obs, String stationCode) {
        if (BaseCheck.getTaskType(stationCode, element) == HUMAN) {
            int stationClass = obs.getStationClass();
            //世界时间转北京时间
            dateTime = CommonUtil.utc2bj(dateTime);
            int hour = dateTime.getHour();
            //基准站、基本站：08、11、14、17、20（BJT））标注为8
            if (stationClass == BENCHMARK || stationClass == BASIC) {
                if (hour == MAN_OBS_TIME1 || hour == MAN_OBS_TIME2 || hour == MAN_OBS_TIME3 || hour == MAN_OBS_TIME4 || hour == MAN_OBS_TIME5) {
                    return QualityCode.Z_QC_LACK;
                }
                //一般站：08、14、20（BJT））标注为8
            } else if (stationClass == NORMAL3T || stationClass == NORMAL4T) {
                if (hour == MAN_OBS_TIME1 || hour == MAN_OBS_TIME3 || hour == MAN_OBS_TIME5) {
                    return QualityCode.Z_QC_LACK;
                }
            }
        }
        //其余标注为7
        return QualityCode.Z_QC_NO_TASK;
    }
}
