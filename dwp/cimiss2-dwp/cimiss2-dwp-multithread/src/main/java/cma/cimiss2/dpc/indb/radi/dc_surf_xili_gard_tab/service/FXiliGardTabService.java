package cma.cimiss2.dpc.indb.radi.dc_surf_xili_gard_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.*;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author ：YCK
 * @date ：Created in 2019/11/7 0007 15:07
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class FXiliGardTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        FXiliGardTabService.diQueues = diQueues;
    }

    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataHourSingle(PreparedStatement ps, List<SurfXiliGardHourTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<SurfXiliGardHourTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (SurfXiliGardHourTab surfXiliGardHourTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(surfXiliGardHourTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getdRecordId());

                    ps.setString(ii++, StartConfig.ctsCode("R009_hour_cts_code"));

                    ps.setString(ii++, surfXiliGardHourTab.getdDatetime());

                    ps.setString(ii++, surfXiliGardHourTab.getV01301());

                    ps.setString(ii++, surfXiliGardHourTab.getV04001());

                    ps.setString(ii++, surfXiliGardHourTab.getV04002());

                    ps.setString(ii++, surfXiliGardHourTab.getV06001());

                    ps.setString(ii++, surfXiliGardHourTab.getV05001());

                    ps.setString(ii++, surfXiliGardHourTab.getV07001());

                    ps.setString(ii++, surfXiliGardHourTab.getV070010001());

                    ps.setString(ii++, surfXiliGardHourTab.getV070010002());

                    ps.setString(ii++, surfXiliGardHourTab.getV070010003());

                    ps.setString(ii++, surfXiliGardHourTab.getV070010004());

                    ps.setString(ii++, surfXiliGardHourTab.getV070010005());

                    ps.setString(ii++, surfXiliGardHourTab.getV01300());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getGroundDistance());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getInfraRed());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getDsrsensoriden());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getUsrsensoriden());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getDlwrsensoriden());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getUlwrsensoriden());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getParsensoriden());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden5());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden10());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden15());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden20());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden40());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden180());

                    ps.setString(ii++, surfXiliGardHourTab.getShflux1());

                    ps.setString(ii++, surfXiliGardHourTab.getShflux2());

                    ps.setString(ii++, surfXiliGardHourTab.getShflux3());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden1l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden1l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden2l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden2l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden10mh());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getWdsensoriden10mh());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden10mh());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden4l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden4l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden5l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden5l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAirpsensoriden());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getEvasensoriden());

                    ps.setString(ii++, surfXiliGardHourTab.getHourmin());

                    ps.setString(ii++, surfXiliGardHourTab.getMidsradiationhah());

                    ps.setString(ii++, surfXiliGardHourTab.getMaxhdsradiation());

                    ps.setString(ii++, surfXiliGardHourTab.getTmaxowhdswradiation());

                    ps.setString(ii++, surfXiliGardHourTab.getEhdswradiation());

                    ps.setString(ii++, surfXiliGardHourTab.getAwswradiationihah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxihousradiation());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtousradiationhi());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getEhuswradiation());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMidlwradiationhah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxihdlwradiation());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtowhdlwradiationi());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getEwhdlwradiation());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMiulwradiationhah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxihulwradiation());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtowhulwradiationi());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getEihulwradiation());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAnradiaihah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxihnradiai());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtowhnradiai());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMinhnradiai());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintowhnradiai());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getEwhnradiation());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAiparadiahah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getV143400560());

                    ps.setInt(ii++, surfXiliGardHourTab.getV1434005052());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getEparadiationih());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemphah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxgtemph());

                    ps.setInt(ii++, surfXiliGardHourTab.getMaxotgh());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMingtemph());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinotgh());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp5cmhah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp10cmhah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp15cmhah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp20cmhah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp40cmhah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getA10cmsvwchah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getA20cmsvwchah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getA50cmsvwchah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getA100cmsvwchah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getA180cmsvwchah());

                    ps.setInt(ii++, surfXiliGardHourTab.getAshfluxhah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah1l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getExwspeedh1l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedtimeh1l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh1l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTimemaxwvh1l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah2l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getExwspeedh2l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedtimeh2l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh2l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTimemaxwvh2l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah10m());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAvshwv10mhah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAvchwdir10mhah());

                    ps.setString(ii++, surfXiliGardHourTab.getSdwdirection());

                    ps.setString(ii++, surfXiliGardHourTab.getExwspeedh10m());

                    ps.setString(ii++, surfXiliGardHourTab.getMaxwdirwspeed10dh());

                    ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedt10dh());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh10m());

                    ps.setInt(ii++, surfXiliGardHourTab.getMaxwdirwspeed10mh());

                    ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedt10mh());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah4l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getExwspeedh4l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedtimeh4l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh4l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTimemaxwvh4l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah5l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getExwspeedh5l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedtimeh5l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh5l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTimemaxwvh5l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah1l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph1l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah1l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph1l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmintempah1l());

                    ps.setInt(ii++, surfXiliGardHourTab.getArhhah1l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhh1l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth1l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah1l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah2l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph2l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah2l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph2l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmintempah2l());

                    ps.setInt(ii++, surfXiliGardHourTab.getArhhah2l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhh2l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth2l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah2l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah10l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph10l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah10l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph10l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmintempah10l());

                    ps.setInt(ii++, surfXiliGardHourTab.getArhhah10l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhh10l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth10l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah10l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah4l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph4l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah4l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph4l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmintempah4l());

                    ps.setInt(ii++, surfXiliGardHourTab.getArhhah4l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhh4l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth4l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah4l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah5l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph5l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah5l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph5l());

                    ps.setInt(ii++, surfXiliGardHourTab.getTmintempah5l());

                    ps.setInt(ii++, surfXiliGardHourTab.getArhhah5l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhh5l());

                    ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth5l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah5l());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getAlpressurehah());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getV10301());

                    ps.setInt(ii++, surfXiliGardHourTab.getV10301052());

                    ps.setBigDecimal(ii++, surfXiliGardHourTab.getV10302());

                    ps.setInt(ii++, surfXiliGardHourTab.getV10302052());

                    ps.setInt(ii++, surfXiliGardHourTab.getAprecipitationh());

                    ps.setInt(ii++, surfXiliGardHourTab.getCumevah());

                    ps.setString(ii++, surfXiliGardHourTab.getV04003());

                    ps.setString(ii++, surfXiliGardHourTab.getV04004());

                    ps.setString(ii++, surfXiliGardHourTab.getV04005());

                    ps.addBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("列号：" + ii + "有误");
                }
                if(size>4) {
                    if (middle != 0 && indexSingle % middle == 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        } catch (Exception e) {
                            ps.clearBatch();
                            excuteByDataHourSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=surfXiliGardHourTab.getV01301()+" "+surfXiliGardHourTab.getdDatetime();
                        ps.executeBatch();
                        ps.clearBatch();
                    }catch (Exception e){
                        ps.clearBatch();
                        System.out.println("舍弃重复录入数据！"+data);
                    }
                    list1.clear();
                }
            }
            connection.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataMinSingle(PreparedStatement ps, List<SurfXiliGardMinTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<SurfXiliGardMinTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (SurfXiliGardMinTab surfXiliGardMinTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(surfXiliGardMinTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getdRecordId());

                    ps.setString(ii++, StartConfig.ctsCode("R009_min_cts_code"));

                    ps.setString(ii++, surfXiliGardMinTab.getdDatetime());

                    ps.setString(ii++, surfXiliGardMinTab.getV01301());

                    ps.setString(ii++, surfXiliGardMinTab.getV04001());

                    ps.setString(ii++, surfXiliGardMinTab.getV04002());

                    ps.setString(ii++, surfXiliGardMinTab.getV04003());

                    ps.setString(ii++, surfXiliGardMinTab.getV04004());

                    ps.setString(ii++, surfXiliGardMinTab.getV04005());

                    ps.setString(ii++, surfXiliGardMinTab.getV06001());

                    ps.setString(ii++, surfXiliGardMinTab.getV05001());

                    ps.setString(ii++, surfXiliGardMinTab.getV07001());

                    ps.setString(ii++, surfXiliGardMinTab.getV070010001());

                    ps.setString(ii++, surfXiliGardMinTab.getV070010002());

                    ps.setString(ii++, surfXiliGardMinTab.getV070010003());

                    ps.setString(ii++, surfXiliGardMinTab.getV070010004());

                    ps.setString(ii++, surfXiliGardMinTab.getV070010005());

                    ps.setString(ii++, surfXiliGardMinTab.getV01300());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getGroundDistance());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getInfraRed());

                    ps.setString(ii++, surfXiliGardMinTab.getHourmin());

                    ps.setInt(ii++, surfXiliGardMinTab.getV02001());

                    ps.setInt(ii++, surfXiliGardMinTab.getV02301());

                    ps.setString(ii++, surfXiliGardMinTab.getvAcode());

                    ps.setString(ii++, surfXiliGardMinTab.getvBbb());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV08010());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV07031());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV0703202());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12120());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030005());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030010());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030015());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030020());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030040());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105010());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105020());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105050());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105100());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105180());

                    ps.setString(ii++, surfXiliGardMinTab.getShflux1());

                    ps.setString(ii++, surfXiliGardMinTab.getShflux2());

                    ps.setString(ii++, surfXiliGardMinTab.getShflux3());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed1m1l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed10m1l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwspeed1l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed1m2l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed10m2l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwspeed2l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed1m10mh());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwdirection1m10mh());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed10m10mh());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwdirection10m10mh());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwspeed10mh());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed1m5l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed10m5l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwspeed5l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m1l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m1l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m1l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m2l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m2l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m2l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m10mh());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m10mh());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m10mh());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m4l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m4l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m4l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m5l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m5l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m5l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAlpressure1m());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getCumpre1m());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getCumeva1m());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwind10m());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAvgspeed1m4l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getAvgspeed10m4l());

                    ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwind4l());

                    ps.addBatch();
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("列号：" + ii + "有误");
                }
                if(size>4) {
                    if (middle != 0 && indexSingle % middle == 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        } catch (Exception e) {
                            ps.clearBatch();
                            excuteByDataMinSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=surfXiliGardMinTab.getV01301()+" "+surfXiliGardMinTab.getdDatetime();
                        ps.executeBatch();
                        ps.clearBatch();
                    }catch (Exception e){
                        ps.clearBatch();
                        System.out.println("舍弃重复录入数据！"+data);
                    }
                    list1.clear();
                }
            }
            connection.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 非标准格式农田小气候（中环天仪）的处理
     *
     */
    public static DataBaseAction processSuccessReport(List<SurfXiliGardMinTab> surfXiliGardMinTabList, List<SurfXiliGardHourTab> surfXiliGardHourTabList, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        insertDB(surfXiliGardMinTabList,surfXiliGardHourTabList, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     */
    private static void insertDB(List<SurfXiliGardMinTab> surfXiliGardMinTabList, List<SurfXiliGardHourTab> surfXiliGardHourTabList, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            if (surfXiliGardMinTabList!=null && !surfXiliGardMinTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("R009_min_value_table_name") + "("
                        + "D_RETAIN_ID, D_DATA_ID, D_DATETIME, \n" +
                        "      V01301, V04001, V04002,V04003,V04004, V04005, \n" +
                        "       V06001, V05001, \n" +
                        "      V07001, V07001_0001, V07001_0002, \n" +
                        "      V07001_0003, V07001_0004, V07001_0005, \n" +
                        "      V01300, GROUND_DISTANCE, INFRA_RED, \n" +
                        "      HOURMIN, V02001, V02301, \n" +
                        "      V_ACODE, V_BBB, V08010, \n" +
                        "      V07031, V07032_02, V12120, \n" +
                        "      V12030_005, V12030_010, V12030_015, \n" +
                        "      V12030_020, V12030_040, V71105_010, \n" +
                        "      V71105_020, V71105_050, V71105_100, \n" +
                        "      V71105_180, SHFLUX1, SHFLUX2, \n" +
                        "      SHFLUX3, AWSPEED1M1L, AWSPEED10M1L, \n" +
                        "      MAXWSPEED1L, AWSPEED1M2L, AWSPEED10M2L, \n" +
                        "      MAXWSPEED2L, AWSPEED1M10MH, AWDIRECTION1M10MH, \n" +
                        "      AWSPEED10M10MH, AWDIRECTION10M10MH, MAXWSPEED10MH, \n" +
                        "      AWSPEED1M5L, AWSPEED10M5L, MAXWSPEED5L, \n" +
                        "      ATEMP1M1L, ARELAHUM1M1L, AWVPRE1M1L, \n" +
                        "      ATEMP1M2L, ARELAHUM1M2L, AWVPRE1M2L, \n" +
                        "      ATEMP1M10MH, ARELAHUM1M10MH, AWVPRE1M10MH, \n" +
                        "      ATEMP1M4L, ARELAHUM1M4L, AWVPRE1M4L, \n" +
                        "      ATEMP1M5L, ARELAHUM1M5L, AWVPRE1M5L, \n" +
                        "      ALPRESSURE1M, CUMPRE1M, CUMEVA1M,MAXWIND10M,AVGSPEED1M4L,AVGSPEED10M4L,MAXWIND4L) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));

                int index = 0;
                List<SurfXiliGardMinTab> list=new ArrayList();
                for (SurfXiliGardMinTab surfXiliGardMinTab : surfXiliGardMinTabList) {
                    int ii = 1;
                    index += 1;
                    list.add(surfXiliGardMinTab);
                    try {
                        // 记录主键	D_RECORD_ID	varchar(500)
                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getdRecordId());

                        ps.setString(ii++, StartConfig.ctsCode("R009_min_cts_code"));

                        ps.setString(ii++, surfXiliGardMinTab.getdDatetime());

                        ps.setString(ii++, surfXiliGardMinTab.getV01301());

                        ps.setString(ii++, surfXiliGardMinTab.getV04001());

                        ps.setString(ii++, surfXiliGardMinTab.getV04002());

                        ps.setString(ii++, surfXiliGardMinTab.getV04003());

                        ps.setString(ii++, surfXiliGardMinTab.getV04004());

                        ps.setString(ii++, surfXiliGardMinTab.getV04005());

                        ps.setString(ii++, surfXiliGardMinTab.getV06001());

                        ps.setString(ii++, surfXiliGardMinTab.getV05001());

                        ps.setString(ii++, surfXiliGardMinTab.getV07001());

                        ps.setString(ii++, surfXiliGardMinTab.getV070010001());

                        ps.setString(ii++, surfXiliGardMinTab.getV070010002());

                        ps.setString(ii++, surfXiliGardMinTab.getV070010003());

                        ps.setString(ii++, surfXiliGardMinTab.getV070010004());

                        ps.setString(ii++, surfXiliGardMinTab.getV070010005());

                        ps.setString(ii++, surfXiliGardMinTab.getV01300());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getGroundDistance());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getInfraRed());

                        ps.setString(ii++, surfXiliGardMinTab.getHourmin());

                        ps.setInt(ii++, surfXiliGardMinTab.getV02001());

                        ps.setInt(ii++, surfXiliGardMinTab.getV02301());

                        ps.setString(ii++, surfXiliGardMinTab.getvAcode());

                        ps.setString(ii++, surfXiliGardMinTab.getvBbb());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV08010());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV07031());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV0703202());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12120());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030005());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030010());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030015());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030020());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV12030040());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105010());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105020());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105050());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105100());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getV71105180());

                        ps.setString(ii++, surfXiliGardMinTab.getShflux1());

                        ps.setString(ii++, surfXiliGardMinTab.getShflux2());

                        ps.setString(ii++, surfXiliGardMinTab.getShflux3());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed1m1l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed10m1l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwspeed1l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed1m2l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed10m2l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwspeed2l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed1m10mh());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwdirection1m10mh());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed10m10mh());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwdirection10m10mh());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwspeed10mh());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed1m5l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwspeed10m5l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwspeed5l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m1l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m1l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m1l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m2l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m2l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m2l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m10mh());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m10mh());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m10mh());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m4l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m4l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m4l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAtemp1m5l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getArelahum1m5l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAwvpre1m5l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAlpressure1m());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getCumpre1m());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getCumeva1m());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwind10m());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAvgspeed1m4l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getAvgspeed10m4l());

                        ps.setBigDecimal(ii++, surfXiliGardMinTab.getMaxwind4l());


                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000 == 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        } catch (Exception e) {
                            ps.clearBatch();
                            //执行错误时，单条执行
                            excuteByDataMinSingle(ps, list, cts_code, connection);
                        }
                        list.clear();
                    }
                }
                try {
                    ps.executeBatch();
                    connection.commit();
                } catch (Exception e) {
                    ps.clearBatch();
                    //执行错误时，单条执行
                    excuteByDataMinSingle(ps, list, cts_code, connection);
                }
                list.clear();
            }
            if (surfXiliGardHourTabList!=null && !surfXiliGardHourTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("R009_hour_value_table_name") + "("
                        + "D_RETAIN_ID, D_DATA_ID, D_DATETIME, \n" +
                        "      V01301, V04001, V04002, \n" +
                        "      V06001, V05001, V07001, \n" +
                        "      V07001_0001, V07001_0002, V07001_0003, \n" +
                        "      V07001_0004, V07001_0005, V01300, \n" +
                        "      GROUND_DISTANCE, INFRA_RED, DSRSENSORIDEN, \n" +
                        "      USRSENSORIDEN, DLWRSENSORIDEN, ULWRSENSORIDEN, \n" +
                        "      PARSENSORIDEN, GTEMPSENSORIDEN, GTEMPSENSORIDEN5, \n" +
                        "      GTEMPSENSORIDEN10, GTEMPSENSORIDEN15, GTEMPSENSORIDEN20, \n" +
                        "      GTEMPSENSORIDEN40,GTEMPSENSORIDEN180, \n" +
                        "      SHFLUX1, SHFLUX2, SHFLUX3, \n" +
                        "      WSSENSORIDEN1L, TAHSENSORIDEN1L, WSSENSORIDEN2L, \n" +
                        "      TAHSENSORIDEN2L, WSSENSORIDEN10MH, WDSENSORIDEN10MH, \n" +
                        "      TAHSENSORIDEN10MH, WSSENSORIDEN4L, TAHSENSORIDEN4L, \n" +
                        "      WSSENSORIDEN5L, TAHSENSORIDEN5L, AIRPSENSORIDEN, \n" +
                        "      EVASENSORIDEN, HOURMIN, MIDSRADIATIONHAH, \n" +
                        "      MAXHDSRADIATION, TMAXOWHDSWRADIATION, EHDSWRADIATION, \n" +
                        "      AWSWRADIATIONIHAH, MAXIHOUSRADIATION, MAXTOUSRADIATIONHI, \n" +
                        "      EHUSWRADIATION, MIDLWRADIATIONHAH, MAXIHDLWRADIATION, \n" +
                        "      MAXTOWHDLWRADIATIONI, EWHDLWRADIATION, \n" +
                        "      MIULWRADIATIONHAH, MAXIHULWRADIATION, MAXTOWHULWRADIATIONI, \n" +
                        "      EIHULWRADIATION, ANRADIAIHAH, MAXIHNRADIAI, \n" +
                        "      MAXTOWHNRADIAI, MINHNRADIAI, MINTOWHNRADIAI, \n" +
                        "      EWHNRADIATION, AIPARADIAHAH, V14340_05_60, \n" +
                        "      V14340_05_052, EPARADIATIONIH, AGTEMPHAH, \n" +
                        "      MAXGTEMPH, MAXOTGH, MINGTEMPH, \n" +
                        "      MINOTGH, AGTEMP5CMHAH, AGTEMP10CMHAH, \n" +
                        "      AGTEMP15CMHAH, AGTEMP20CMHAH, AGTEMP40CMHAH, \n" +
                        "      A10CMSVWCHAH, A20CMSVWCHAH, A50CMSVWCHAH, \n" +
                        "      A100CMSVWCHAH, A180CMSVWCHAH, ASHFLUXHAH, \n" +
                        "      AWSPEEDHAH1L, EXWSPEEDH1L, MAXWSPEEDTIMEH1L, \n" +
                        "      MAXWSPEEDH1L, TIMEMAXWVH1L, AWSPEEDHAH2L, \n" +
                        "      EXWSPEEDH2L, MAXWSPEEDTIMEH2L, MAXWSPEEDH2L, \n" +
                        "      TIMEMAXWVH2L, AWSPEEDHAH10M, AVSHWV10MHAH, \n" +
                        "      AVCHWDIR10MHAH, SDWDIRECTION, EXWSPEEDH10M, \n" +
                        "      MAXWDIRWSPEED10DH, MAXWSPEEDT10DH, MAXWSPEEDH10M, \n" +
                        "      MAXWDIRWSPEED10MH, MAXWSPEEDT10MH, AWSPEEDHAH4L, \n" +
                        "      EXWSPEEDH4L, MAXWSPEEDTIMEH4L, MAXWSPEEDH4L, \n" +
                        "      TIMEMAXWVH4L, AWSPEEDHAH5L, EXWSPEEDH5L, \n" +
                        "      MAXWSPEEDTIMEH5L, MAXWSPEEDH5L, TIMEMAXWVH5L, \n" +
                        "      ATEMPHAH1L, MAXTEMPH1L, TMAXTEMPAH1L, \n" +
                        "      MINTEMPH1L, TMINTEMPAH1L, ARHHAH1L, \n" +
                        "      MINRHH1L, MINRHOTH1L, AWVPHAH1L, \n" +
                        "      ATEMPHAH2L, MAXTEMPH2L, TMAXTEMPAH2L, \n" +
                        "      MINTEMPH2L, TMINTEMPAH2L, ARHHAH2L, \n" +
                        "      MINRHH2L, MINRHOTH2L, AWVPHAH2L, \n" +
                        "      ATEMPHAH10L, MAXTEMPH10L, TMAXTEMPAH10L, \n" +
                        "      MINTEMPH10L, TMINTEMPAH10L, ARHHAH10L, \n" +
                        "      MINRHH10L, MINRHOTH10L, AWVPHAH10L, \n" +
                        "      ATEMPHAH4L, MAXTEMPH4L, TMAXTEMPAH4L, \n" +
                        "      MINTEMPH4L, TMINTEMPAH4L, ARHHAH4L, \n" +
                        "      MINRHH4L, MINRHOTH4L, AWVPHAH4L, \n" +
                        "      ATEMPHAH5L, MAXTEMPH5L, TMAXTEMPAH5L, \n" +
                        "      MINTEMPH5L, TMINTEMPAH5L, ARHHAH5L, \n" +
                        "      MINRHH5L, MINRHOTH5L, AWVPHAH5L, \n" +
                        "      ALPRESSUREHAH, V10301, V10301_052, \n" +
                        "      V10302, V10302_052, APRECIPITATIONH, \n" +
                        "      CUMEVAH, V04003, V04004, V04005) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));

                int index = 0;
                List<SurfXiliGardHourTab> list=new ArrayList();
                for (SurfXiliGardHourTab surfXiliGardHourTab : surfXiliGardHourTabList) {
                    int ii = 1;
                    index += 1;
                    list.add(surfXiliGardHourTab);
                    try {
                        // 记录主键	D_RECORD_ID	varchar(500)
                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getdRecordId());

                        ps.setString(ii++, StartConfig.ctsCode("R009_hour_cts_code"));

                        ps.setString(ii++, surfXiliGardHourTab.getdDatetime());

                        ps.setString(ii++, surfXiliGardHourTab.getV01301());

                        ps.setString(ii++, surfXiliGardHourTab.getV04001());

                        ps.setString(ii++, surfXiliGardHourTab.getV04002());

                        ps.setString(ii++, surfXiliGardHourTab.getV06001());

                        ps.setString(ii++, surfXiliGardHourTab.getV05001());

                        ps.setString(ii++, surfXiliGardHourTab.getV07001());

                        ps.setString(ii++, surfXiliGardHourTab.getV070010001());

                        ps.setString(ii++, surfXiliGardHourTab.getV070010002());

                        ps.setString(ii++, surfXiliGardHourTab.getV070010003());

                        ps.setString(ii++, surfXiliGardHourTab.getV070010004());

                        ps.setString(ii++, surfXiliGardHourTab.getV070010005());

                        ps.setString(ii++, surfXiliGardHourTab.getV01300());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getGroundDistance());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getInfraRed());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getDsrsensoriden());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getUsrsensoriden());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getDlwrsensoriden());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getUlwrsensoriden());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getParsensoriden());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden5());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden10());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden15());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden20());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden40());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getGtempsensoriden180());

                        ps.setString(ii++, surfXiliGardHourTab.getShflux1());

                        ps.setString(ii++, surfXiliGardHourTab.getShflux2());

                        ps.setString(ii++, surfXiliGardHourTab.getShflux3());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden1l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden1l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden2l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden2l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden10mh());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getWdsensoriden10mh());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden10mh());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden4l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden4l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getWssensoriden5l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getTahsensoriden5l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAirpsensoriden());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getEvasensoriden());

                        ps.setString(ii++, surfXiliGardHourTab.getHourmin());

                        ps.setString(ii++, surfXiliGardHourTab.getMidsradiationhah());

                        ps.setString(ii++, surfXiliGardHourTab.getMaxhdsradiation());

                        ps.setString(ii++, surfXiliGardHourTab.getTmaxowhdswradiation());

                        ps.setString(ii++, surfXiliGardHourTab.getEhdswradiation());

                        ps.setString(ii++, surfXiliGardHourTab.getAwswradiationihah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxihousradiation());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtousradiationhi());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getEhuswradiation());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMidlwradiationhah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxihdlwradiation());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtowhdlwradiationi());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getEwhdlwradiation());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMiulwradiationhah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxihulwradiation());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtowhulwradiationi());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getEihulwradiation());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAnradiaihah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxihnradiai());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtowhnradiai());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMinhnradiai());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintowhnradiai());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getEwhnradiation());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAiparadiahah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getV143400560());

                        ps.setInt(ii++, surfXiliGardHourTab.getV1434005052());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getEparadiationih());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemphah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxgtemph());

                        ps.setInt(ii++, surfXiliGardHourTab.getMaxotgh());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMingtemph());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinotgh());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp5cmhah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp10cmhah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp15cmhah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp20cmhah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAgtemp40cmhah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getA10cmsvwchah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getA20cmsvwchah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getA50cmsvwchah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getA100cmsvwchah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getA180cmsvwchah());

                        ps.setInt(ii++, surfXiliGardHourTab.getAshfluxhah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah1l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getExwspeedh1l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedtimeh1l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh1l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTimemaxwvh1l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah2l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getExwspeedh2l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedtimeh2l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh2l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTimemaxwvh2l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah10m());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAvshwv10mhah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAvchwdir10mhah());

                        ps.setString(ii++, surfXiliGardHourTab.getSdwdirection());

                        ps.setString(ii++, surfXiliGardHourTab.getExwspeedh10m());

                        ps.setString(ii++, surfXiliGardHourTab.getMaxwdirwspeed10dh());

                        ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedt10dh());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh10m());

                        ps.setInt(ii++, surfXiliGardHourTab.getMaxwdirwspeed10mh());

                        ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedt10mh());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah4l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getExwspeedh4l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedtimeh4l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh4l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTimemaxwvh4l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwspeedhah5l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getExwspeedh5l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMaxwspeedtimeh5l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxwspeedh5l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTimemaxwvh5l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah1l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph1l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah1l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph1l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmintempah1l());

                        ps.setInt(ii++, surfXiliGardHourTab.getArhhah1l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhh1l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth1l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah1l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah2l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph2l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah2l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph2l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmintempah2l());

                        ps.setInt(ii++, surfXiliGardHourTab.getArhhah2l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhh2l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth2l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah2l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah10l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph10l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah10l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph10l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmintempah10l());

                        ps.setInt(ii++, surfXiliGardHourTab.getArhhah10l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhh10l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth10l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah10l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah4l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph4l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah4l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph4l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmintempah4l());

                        ps.setInt(ii++, surfXiliGardHourTab.getArhhah4l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhh4l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth4l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah4l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAtemphah5l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMaxtemph5l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmaxtempah5l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getMintemph5l());

                        ps.setInt(ii++, surfXiliGardHourTab.getTmintempah5l());

                        ps.setInt(ii++, surfXiliGardHourTab.getArhhah5l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhh5l());

                        ps.setInt(ii++, surfXiliGardHourTab.getMinrhoth5l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAwvphah5l());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getAlpressurehah());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getV10301());

                        ps.setInt(ii++, surfXiliGardHourTab.getV10301052());

                        ps.setBigDecimal(ii++, surfXiliGardHourTab.getV10302());

                        ps.setInt(ii++, surfXiliGardHourTab.getV10302052());

                        ps.setInt(ii++, surfXiliGardHourTab.getAprecipitationh());

                        ps.setInt(ii++, surfXiliGardHourTab.getCumevah());

                        ps.setString(ii++, surfXiliGardHourTab.getV04003());

                        ps.setString(ii++, surfXiliGardHourTab.getV04004());

                        ps.setString(ii++, surfXiliGardHourTab.getV04005());

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000 == 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        } catch (Exception e) {
                            ps.clearBatch();
                            //执行错误时，单条执行
                            excuteByDataHourSingle(ps, list, cts_code, connection);
                        }
                        list.clear();
                    }
                }
                try {
                    ps.executeBatch();
                    connection.commit();
                } catch (Exception e) {
                    ps.clearBatch();
                    //执行错误时，单条执行
                    excuteByDataHourSingle(ps, list, cts_code, connection);
                }
                list.clear();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            loggerBuffer.append("\n Database connection exception: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
