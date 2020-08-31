package cma.cimiss2.dpc.quickqc.awshour.surf;

import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.quickqc.Util;
import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
import cma.cimiss2.dpc.quickqc.surf.awshour.InternalCheck;
import cma.cimiss2.dpc.quickqc.surf.awshour.InternalCheck.PREInternalBean;
import cma.cimiss2.dpc.quickqc.surf.awshour.LimitCheck;
import cma.cimiss2.dpc.quickqc.surf.awshour.MissingCheck;
import cma.cimiss2.dpc.quickqc.surf.awshour.RangeCheck;
import cma.cimiss2.dpc.quickqc.util.CommonUtil;
import org.junit.Test;
import out.ret2excel.LogRet2Excel;
import out.ret2excel.MyAppender;
import out.ret2excel.ResultBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static cma.cimiss2.dpc.quickqc.Util.*;
import static cma.cimiss2.dpc.quickqc.bean.Constants.NORMAL3T;
import static cma.cimiss2.dpc.quickqc.bean.Constants.QC_TYPE_FILE;
import static cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement.*;
import static cma.cimiss2.dpc.quickqc.util.CommonUtil.LOGGER;
import static cma.cimiss2.dpc.quickqc.util.CommonUtil.setLogger;

/**
 * 降水质控测试
 *
 * @Author: When6passBye
 * @Date: 2019-08-22 15:05
 **/

public class PreTest {

    @Test
    public void preTest() {
        int id = 0;
        ResultBean bean = new ResultBean();
        Properties properties = Util.getConfPro();
        List<SurfaceObservationDataNation> objs = Util.dataRead(properties.getProperty(SOURCE_DIR), bean);
        assert objs != null;
        MeteoElement[] mes = {PRE_1_HOUR, PRE_03_HOUR, PRE_06_HOUR, PRE_12_HOUR, PRE_24_HOUR, PRE_MANUAL};
        List<ResultBean> list = new ArrayList<>(1);
        for (SurfaceObservationDataNation obj : objs) {
            double[] values = getData(obj);
            BaseStationInfo obs;
            obs = Util.getStationInfo(obj);
            QualityCode[] qualityCodes = new QualityCode[6];
            LocalDateTime dateTime = CommonUtil.date2LocalDateTime(obj.getObservationTime());
            //记录质控时间
            long start = System.currentTimeMillis();
            for (int j = 0; j < values.length; j++) {
                qualityCodes[j] = preMlrQc(values[j], mes[j], dateTime, obs);
            }
            long end = System.currentTimeMillis();
            long time = end - start;
            PREInternalBean internalBean = new PREInternalBean();
            internalBean.setPre1h(values[0], qualityCodes[0]);
            internalBean.setPre3h(values[1], qualityCodes[1]);
            internalBean.setPre6h(values[2], qualityCodes[2]);
            internalBean.setPre12h(values[3], qualityCodes[3]);
            internalBean.setPre24h(values[4], qualityCodes[4]);
            InternalCheck.innerCheckPRE(internalBean, dateTime, obs);
            setLogger(PRE_MANUAL, QC_TYPE_FILE, qualityCodes[5], values[5]);
            LOGGER.info("==========PRE==========spend time ========:" + time);
            //一个SurfaceObservationDataNation(报文完毕)
            //将日志中的内容输出到javaBean
            List<ResultBean> beans = MyAppender.pollAll(++id, mes,bean.getFileName(), obs, obj, time);
            list.addAll(beans);
        }
        LogRet2Excel.readAndWriteExcel(properties.getProperty(EXCEL_DIR), list, properties.getProperty(RESULT_SHEET), properties.getProperty(EXCEPT_SHEET));

    }


    /**
     * 获取降水值数组
     *
     * @param obj : 数据bean SurfaceObservationDataNation
     * @return : double[]
     * @author : When6passBye
     * @date : 2019/8/23 2:28 PM
     */
    private double[] getData(SurfaceObservationDataNation obj) {
        double obsPreH = obj.getPrecipitation1Hour().getValue();
        double obsPreTal = obj.getPrecipitationAdditionalManualObservational().getValue();
        double obsPre03 = obj.getPrecipitation3Hours().getValue();
        double obsPre06 = obj.getPrecipitation6Hours().getValue();
        double obsPre12 = obj.getPrecipitation12Hours().getValue();
        double obsPre24 = obj.getPrecipitation24Hours().getValue();
        return new double[]{obsPreH, obsPre03, obsPre06, obsPre12, obsPre24, obsPreTal};
    }

    /**
     * 降水前三步质控，不包含一致性检测
     *
     * @param obsVal   :观测值
     * @param element  :观测元素
     * @param dateTime :时间
     * @param obs      :观测站点信息
     * @return : void
     * @author : When6passBye
     * @date : 2019/8/23 2:35 PM
     */
    private QualityCode preMlrQc(double obsVal, MeteoElement element, LocalDateTime dateTime, BaseStationInfo obs) {
        //缺测检查，所有都会进行这一步
        int stationClass = obs.getStationClass();
        QualityCode retCode;
        if(stationClass==NORMAL3T){
            retCode= MissingCheck.missingCheck(obsVal, PRE_AUTO, dateTime, obs);

        }else{
            retCode= MissingCheck.missingCheck(obsVal, PRE, dateTime, obs);
        }

        //界限检查
        retCode = LimitCheck.limitCheck(obsVal, element, dateTime, obs, retCode);

        //范围检查

        retCode = RangeCheck.rangeCheck(obsVal, element, dateTime, obs, retCode);

        return retCode;
    }

}
