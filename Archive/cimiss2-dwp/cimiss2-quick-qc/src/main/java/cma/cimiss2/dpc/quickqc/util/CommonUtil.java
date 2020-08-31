package cma.cimiss2.dpc.quickqc.util;

import cma.cimiss2.dpc.quickqc.base.BaseCheck;
import cma.cimiss2.dpc.quickqc.bean.BaseStationInfo;
import cma.cimiss2.dpc.quickqc.bean.MaxMinValues;
import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;
import cma.cimiss2.dpc.quickqc.bean.enums.QualityCode;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static cma.cimiss2.dpc.quickqc.base.BaseCheck.CONFIG_MAP;
import static cma.cimiss2.dpc.quickqc.bean.Constants.*;
import static cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement.PRS;

/**
 * 一些所有都要使用的公用方法类
 * @Author: When6passBye
 * @Date: 2019-08-20 11:02
 **/
public class CommonUtil {
    public static org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QC_LOGGER);
    public static org.slf4j.Logger MESSAGE_LOGGER = org.slf4j.LoggerFactory.getLogger(MESSAGE_INFO);
    /**
     * 线程安全，防止用户多线程调用
     */
    public static StringBuffer sb=new StringBuffer();

    /**
     * 检查各个参数和获取站点编码
     * @author : When6passBye
     * @date : 2019/8/20 10:38 AM
     * @param datetime :时间
     * @param element :质控元素
     * @param obs :观测站信息
     * @param qcType :质控步骤类型
     * @return : java.lang.String
     */
    public static String checkAndGetStationCode(LocalDateTime datetime, MeteoElement element, BaseStationInfo obs, String qcType) {
        if (datetime == null || element == null || obs == null) {
            LOGGER.error(qcType+PARAMS_INVALID);
            throw new IllegalArgumentException(qcType+PARAMS_INVALID);
        }
        String stationCode = obs.getStationCode();
        if (!StringUtils.isNotBlank(stationCode)) {
            LOGGER.error(qcType+STATIONCODE_INVALID);
            throw new IllegalArgumentException(qcType+STATIONCODE_INVALID);
        }
        return stationCode;
    }

    /**
     * 返回日志
     * @author : When6passBye
     * @date : 2019/8/24 4:51 PM
     * @param element : 观测元素
     * @param qcType : 质控步骤
     * @param ret : 质控结果
     * @param obsVal : 观测值
     * @return : java.lang.String
     */
    public static void setLogger(MeteoElement element, String qcType, QualityCode ret,double obsVal){
        sb.delete(0,sb.length());
        LOGGER.info(sb.append(element.toString()).append(SEG2).append(obsVal).append(SEG).append(qcType).append(SEG2).append(ret).toString());
    }


    /**
     * 从rangCheck提取出来的方法，用于获取阈值
     * @author : When6passBye
     * @date : 2019/8/20 2:19 PM
     * @param obsValue : 观测值
     * @param element : 观测元素
     * @param datetime : 时间
     * @param conf : 观测站配置信息
     * @return : cma.cimiss2.dpc.quickqc.bean.MaxMinValues
     */
    public static MaxMinValues getMaxMinValues(double obsValue, MeteoElement element, LocalDateTime datetime, BaseStationInfo conf, Map<String, ? extends BaseStationInfo> rangeMap){
        // 阈值配置
        MaxMinValues maxMinValues = SurfUtil.getAwsElementRange(conf, CONFIG_MAP, element, obsValue, datetime, rangeMap);
        if (maxMinValues == null) {
            LOGGER.error(QC_TYPE_RANGE + CONFIG_NULL);
            throw new RuntimeException(QC_TYPE_RANGE + CONFIG_NULL);
        }
        if (PRS == element) {
            // 台风情况
            maxMinValues.setMinValue(maxMinValues.getMinValue() - 26.0);
        }
        return maxMinValues;
    }

    /**
     * 从rangeCheck提取出来的方法，用于判断是否进行range质控
     * @author : When6passBye
     * @date : 2019/8/20 2:29 PM
     * @param obs : 观测站信息
     * @param maxMinValues : 阈值
     * @param conf : 配置信息
     * @return : boolean
     */
    public static boolean doRangeCheck(BaseStationInfo obs,MaxMinValues maxMinValues,BaseStationInfo conf){
        if(obs==null||maxMinValues==null||conf==null){
            LOGGER.error(QC_TYPE_RANGE+PARAMS_INVALID);
            throw new IllegalArgumentException(QC_TYPE_RANGE+PARAMS_INVALID);
        }
        return BaseCheck.isNeedRangeCheck(obs, conf)
                && maxMinValues.getExceedingThreshold() >= 0 /*小于0时不进行范围质控*/
                && maxMinValues.getMaxValue() >= maxMinValues.getMinValue();
    }

    /**
     * 世界时间转北京时间
     * @author : When6passBye
     * @date : 2019/8/22 10:38 AM
     * @param utc : 世界时间
     * @return : java.time.LocalDateTime
     */
    public static LocalDateTime utc2bj(LocalDateTime utc){
        return utc.plusHours(8);
    }

    /**
     * date转localdatetime
     * @author : When6passBye
     * @date : 2019/8/22 5:20 PM
     * @param date : 时间
     * @return : java.time.LocalDateTime
     */
    public static LocalDateTime date2LocalDateTime(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        return localDateTime;
    }


}
