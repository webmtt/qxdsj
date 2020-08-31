package out.ret2excel;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
import cma.cimiss2.dpc.quickqc.bean.Constants;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;

import java.text.SimpleDateFormat;
import java.util.*;

import static cma.cimiss2.dpc.quickqc.bean.Constants.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.QualityCode.*;

/**
 * 自定义的日志输出类
 * 用于输出到excel文件
 *
 * @Author: When6passBye
 * @Date: 2019-08-29 09:57
 **/
public class MyAppender extends AppenderBase<LoggingEvent> {

    /**
     * 因为缺测的元素和接受其他质控步骤的元素往往不一样，所以需要一个另外的容器
     */
    private static List<Integer> LACK_RETS = new ArrayList<>();

    /**
     * key:元素，value:观测值和质控码数组，最后一位是观测值
     */
    private static Map<String, double[]> qcResultMap = new HashMap();

    /**
     * 清空map
     *
     * @return : void
     * @author : When6passBye
     * @date : 2019/8/29 5:19 PM
     */
    private static void reSet() {
        qcResultMap.clear();
        LACK_RETS.clear();
    }

    /**
     * 根据质控码获取质控码对应的数字
     *
     * @param qcStr : 质控码字符串
     * @return : java.lang.Integer
     * @author : When6passBye
     * @date : 2019/8/29 3:15 PM
     */
    private static Integer getQcNum(String qcStr) {
        switch (qcStr) {
            case "Z_QC_OK":
                return Z_QC_OK.getCode();
            case "Z_QC_DOUBT":
                return Z_QC_DOUBT.getCode();
            case "Z_QC_DOUBT_NEG":
                return Z_QC_DOUBT_NEG.getCode();
            case "Z_QC_ERROR":
                return Z_QC_ERROR.getCode();
            case "Z_QC_ERROR_NEG":
                return Z_QC_ERROR_NEG.getCode();
            case "Z_QC_CORRECT":
                return Z_QC_CORRECT.getCode();
            case "Z_QC_MOD":
                return Z_QC_MOD.getCode();
            case "Z_QC_NO_TASK":
                return Z_QC_NO_TASK.getCode();
            case "Z_QC_LACK":
                return Z_QC_LACK.getCode();
            case "Z_QC_NO_QC":
                return Z_QC_NO_QC.getCode();
            default:
                return 9;
        }
    }

    /**
     * 根据质控步骤的字符串获取是第几步质控
     *
     * @param qcStep : 质控步骤的字符串
     * @return : java.lang.Integer
     * @author : When6passBye
     * @date : 2019/8/29 3:05 PM
     */
    private static Integer getQcIndex(String qcStep) {
        switch (qcStep) {
            case QC_TYPE_LACK:
                return LACK;
            case QC_TYPE_LIMIT:
                return LIMIT;
            case QC_TYPE_RANGE:
                return RANGE;
            case QC_TYPE_INTERNAL:
                return INTERNAL;
            case QC_TYPE_FILE:
                return FILE;
            default:
                throw new RuntimeException("qc step is not contain " + qcStep);
        }
    }


    /**
     * 将日志中的内容整理记录到测试Map内存
     *
     * @param eventObject :日志事件
     * @return : void
     * @author : When6passBye
     * @date : 2019/9/3 2:50 PM
     */
    private static synchronized void log4Test(LoggingEvent eventObject) {
        String message = eventObject.getMessage();
        String[] strings = message.trim().split(SEG);
        String[] eleAndVal = strings[0].split(SEG2);
        String element = eleAndVal[0];
        String obsval = eleAndVal[1];
        String[] qcRet = strings[1].trim().split(Constants.SEG2);
        double[] qcResultCodes;
        //如果是缺测质控
        if(qcRet[0].equals(QC_TYPE_LACK)){
            LACK_RETS.add(getQcNum(qcRet[1]));
        }
        if (!qcResultMap.containsKey(element)) {
            qcResultCodes = new double[]{9, 9, 9, 9, 9, Double.parseDouble(obsval)};
        } else {
            qcResultCodes = qcResultMap.get(element);
        }
        //如果不是noqc才放进去
        if (!qcRet[1].equalsIgnoreCase(Z_QC_NO_QC.toString())) {
            qcResultCodes[getQcIndex(qcRet[0])] = getQcNum(qcRet[1]);
        }
        qcResultMap.put(element, qcResultCodes);
    }

    /**
     * 将内存日志中的内容全部一次性写到javabean
     *
     * @author : When6passBye
     * @date : 2019/9/4 3:32 PM
     * @param index : 数据所在组序号
     * @param mes : 参与质控的元素
     * @param fileName : 文件名
     * @param obs : 观测站信息
     * @param obj : 地面数据信息
     * @param time : 时间
     * @return : java.util.List<out.ret2excel.ResultBean>
     */
    public static List<ResultBean> pollAll(int index, MeteoElement[] mes,String fileName, BaseStationInfo obs, SurfaceObservationDataNation obj, long time) {

        List<ResultBean> list = new ArrayList<>();
        for (int i = 0; i<mes.length; i++) {
            double[] doubles = qcResultMap.get(mes[i].toString());
            if (null == doubles || doubles.length < 6) {
                throw new RuntimeException("doubles is wrong or element is null");
            }

            ResultBean bean = new ResultBean();
            bean.setId(index + "");
            bean.setFileName(fileName);
            bean.setStationCode(obs.getStationCode());
            Date observationTime = obj.getObservationTime();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String dateTime = format.format(observationTime);
            bean.setObsTime(dateTime);
            bean.setElement(mes[i].toString());
            bean.setObsVal(doubles[5] + "");
            bean.setMissing(String.valueOf(LACK_RETS.get(i).doubleValue()));
            bean.setLimit(doubles[LIMIT] + "");
            bean.setRange(doubles[RANGE] + "");
            bean.setInternal(doubles[INTERNAL] + "");
            bean.setFileCode(doubles[FILE] + "");
            bean.setCompareResult("");
            bean.setSpendTime(time + "");
            list.add(bean);
        }
        reSet();
        return list;

    }

    @Override
    protected void append(LoggingEvent eventObject) {
        log4Test(eventObject);
    }
}
