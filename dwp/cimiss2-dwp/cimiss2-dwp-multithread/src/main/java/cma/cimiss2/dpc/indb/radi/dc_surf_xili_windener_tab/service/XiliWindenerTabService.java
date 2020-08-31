package cma.cimiss2.dpc.indb.radi.dc_surf_xili_windener_tab.service;

import cma.cimiss2.dpc.decoder.bean.radi.*;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.*;
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
 * @date ：Created in 2019/11/18 0018 11:14
 * @description：
 * @modified By：
 * @version: $ 1.0
 */
public class XiliWindenerTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    static String type = StartConfig.sodCode();
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        XiliWindenerTabService.diQueues = diQueues;
    }
  /**
     * 非标准格式农田小气候（中环天仪）的处理
     *
     * @param
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(List<SurfXiliWindenerHfsTab> surfXiliWindenerHfsTabList, List<SurfXiliWindenerMdeTab> surfXiliWindenerMdeTabList, List<SurfXiliWindenerMdoTab> surfXiliWindenerMdoTabList, List<SurfXiliWindenerTimTab> surfXiliWindenerTimTabList, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        insertDB(surfXiliWindenerHfsTabList,surfXiliWindenerMdeTabList,surfXiliWindenerMdoTabList,surfXiliWindenerTimTabList, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }
    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataHfsSingle(PreparedStatement ps, List<SurfXiliWindenerHfsTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<SurfXiliWindenerHfsTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (SurfXiliWindenerHfsTab surfXiliWindenerHfsTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(surfXiliWindenerHfsTab);
                try {
                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getdRecordId());//记录标识

                    ps.setString(ii++, StartConfig.ctsCode("S001_hfs_cts_code"));//四级编码

                    ps.setString(ii++, surfXiliWindenerHfsTab.getdDatetime());//资料时间

                    ps.setString(ii++, surfXiliWindenerHfsTab.getV01301());//区站号

                    ps.setShort(ii++, surfXiliWindenerHfsTab.getV04001());//年

                    ps.setShort(ii++, surfXiliWindenerHfsTab.getV04002());//月

                    ps.setShort(ii++, surfXiliWindenerHfsTab.getV04003());//日

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getV06001());//铁塔所在位置经度

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getV05001());//铁塔所在位置纬度

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getV07001());//梯度塔所处地（湖、海）面拔海高度

                    ps.setShort(ii++, surfXiliWindenerHfsTab.getTheins50());//50米超声风温仪安装角度

                    ps.setShort(ii++, surfXiliWindenerHfsTab.getTheins70());//70米超声风温仪安装角度

                    ps.setShort(ii++, surfXiliWindenerHfsTab.getTheins100());//100米超声风温仪安装角度

                    ps.setString(ii++, surfXiliWindenerHfsTab.getV02320());//采集器型号

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed50x());//50米高度水平风速（x轴）

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed50y());//50米高度水平风速（y轴）

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed50z());//50米高度垂向风速（z轴）

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getUltvirtem50());//50米高度超声虚温

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getDiavalue50());//50米高度传感器诊断值

                    ps.setString(ii++, surfXiliWindenerHfsTab.getHoriwindspeed70x());//70米高度水平风速（x轴）

                    ps.setString(ii++, surfXiliWindenerHfsTab.getHoriwindspeed70y());//70米高度水平风速（y轴）

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed70z());//70米高度垂向风速（z轴）

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getUltvirtem70());//70米高度超声虚温

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getDiavalue70());//70米高度传感器诊断值

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed100x());//100米高度水平风速（x轴）

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed100y());//100米高度水平风速（y轴）

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed100z());//100米高度垂向风速（z轴）

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getUltvirtem100());//100米高度超声虚温

                    ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getDiavalue100());//100米高度传感器诊断值

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
                            excuteByDataHfsSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=surfXiliWindenerHfsTab.getV01301()+" "+surfXiliWindenerHfsTab.getdDatetime();
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
    public static void excuteByDataMdoSingle(PreparedStatement ps, List<SurfXiliWindenerMdoTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<SurfXiliWindenerMdoTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (SurfXiliWindenerMdoTab surfXiliWindenerMdoTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(surfXiliWindenerMdoTab);
                try {
                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getdRecordId());

                    ps.setString(ii++, StartConfig.ctsCode("S001_mdo_cts_code"));//四级编码

                    ps.setString(ii++, surfXiliWindenerMdoTab.getdDatetime());//资料时间

                    ps.setString(ii++, surfXiliWindenerMdoTab.getV01301());

                    ps.setShort(ii++, surfXiliWindenerMdoTab.getV04001());

                    ps.setShort(ii++, surfXiliWindenerMdoTab.getV04002());

                    ps.setShort(ii++, surfXiliWindenerMdoTab.getV04003());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getV06001());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getV05001());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getV07001());

                    ps.setShort(ii++, surfXiliWindenerMdoTab.getTheins50());

                    ps.setShort(ii++, surfXiliWindenerMdoTab.getTheins70());

                    ps.setShort(ii++, surfXiliWindenerMdoTab.getTheins100());

//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwssensoriden50());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwdsensoriden50());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwssensoriden70());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwdsensoriden70());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwssensoriden100());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwdsensoriden100());

                    ps.setString(ii++, surfXiliWindenerMdoTab.getHourmin());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehowinspe10mihywi50x());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehowinspe10mihywi50y());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehowinspe10mihywi50z());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehosywispe10mihywi50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovesywispehywi50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getStade10minmehosywidirhy50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuxhigsuwi50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuyhigsuwi50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuzhigsuwi50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind70x());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind70y());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind70z());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehosywispe10mihywi70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi70s());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi70d());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getStade10minmehosywidirhy70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuxhigsuwi70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuyhigsuwi70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuzhigsuwi70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind100x());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind100y());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind100z());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehosywispe10mihywi100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi100s());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi100d());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getStade10minmehosywidirhy100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuxhigsuwi100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuyhigsuwi100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuzhigsuwi100());

                    ps.setShort(ii++, surfXiliWindenerMdoTab.getV04004());

                    ps.setShort(ii++, surfXiliWindenerMdoTab.getV04005());

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
                            excuteByDataMdoSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=surfXiliWindenerMdoTab.getV01301()+" "+surfXiliWindenerMdoTab.getdDatetime();
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
    public static void excuteByDataMdeSingle(PreparedStatement ps, List<SurfXiliWindenerMdeTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<SurfXiliWindenerMdeTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (SurfXiliWindenerMdeTab surfXiliWindenerMdeTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(surfXiliWindenerMdeTab);
                try {
                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getdRecordId());

                    ps.setString(ii++, StartConfig.ctsCode("S001_mde_cts_code"));//四级编码

                    ps.setString(ii++, surfXiliWindenerMdeTab.getdDatetime());//资料时间

                    ps.setString(ii++, surfXiliWindenerMdeTab.getV01301());

                    ps.setShort(ii++, surfXiliWindenerMdeTab.getV04001());

                    ps.setShort(ii++, surfXiliWindenerMdeTab.getV04002());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getV06001());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getV05001());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getV07001());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwssensoriden50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwdsensoriden50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwssensoriden70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwdsensoriden70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwssensoriden100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwdsensoriden100());

                    ps.setString(ii++, surfXiliWindenerMdeTab.getHourmin());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawspeed50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawdirection50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawspeed50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawdirection50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeed50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedwdir50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedt50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwspeed50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwdir50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhexwspeed50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getEhmaxwspeedwdir50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHhmaxwspeedt50());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawspeed70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawdirection70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawspeed70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawdirection70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeed70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedwdir70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedt70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwspeed70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwdir70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhexwspeed70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getEhmaxwspeedwdir70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHhmaxwspeedt70());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawspeed100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawdirection100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawspeed100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawdirection100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeed100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedwdir100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedt100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwspeed100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwdir100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhexwspeed100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getEhmaxwspeedwdir100());

                    ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHhmaxwspeedt100());

                    ps.setShort(ii++, surfXiliWindenerMdeTab.getV04003());

                    ps.setShort(ii++, surfXiliWindenerMdeTab.getV04004());

                    ps.setShort(ii++, surfXiliWindenerMdeTab.getV04005());

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
                            excuteByDataMdeSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=surfXiliWindenerMdeTab.getV01301()+" "+surfXiliWindenerMdeTab.getdDatetime();
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
    public static void excuteByDataTimSingle(PreparedStatement ps, List<SurfXiliWindenerTimTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<SurfXiliWindenerTimTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (SurfXiliWindenerTimTab surfXiliWindenerTimTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(surfXiliWindenerTimTab);
                try {
                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getdRecordId());

                    ps.setString(ii++, StartConfig.ctsCode("S001_tim_cts_code"));//四级编码

                    ps.setString(ii++, surfXiliWindenerTimTab.getdDatetime());//资料时间]

                    ps.setString(ii++, surfXiliWindenerTimTab.getV01301());

                    ps.setShort(ii++, surfXiliWindenerTimTab.getV04001());

                    ps.setShort(ii++, surfXiliWindenerTimTab.getV04002());

                    ps.setShort(ii++, surfXiliWindenerTimTab.getV04003());

                    ps.setShort(ii++, surfXiliWindenerTimTab.getV04004());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getV06001());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getV05001());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getV07001());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwssensoriden50());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwdsensoriden50());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwssensoriden70());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwdsensoriden70());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwssensoriden100());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwdsensoriden100());

                    ps.setString(ii++, surfXiliWindenerTimTab.getV02320());

                    ps.setString(ii++, surfXiliWindenerTimTab.getHourmin());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAvsiwmah50());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getMewdah50());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minavsiwmah50());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minmawisarho50());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhouravsiwmah50());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50time());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50maxs());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50maxf());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50maxt());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWavsiwmah70());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWmewdah70());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minavsiwmah70());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minmawisarho70());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhouravsiwmah70());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70time());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70maxs());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70maxf());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70maxt());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWavsiwmah100());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWmewdah100());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minavsiwmah100());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minmawisarho100());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhouravsiwmah100());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100time());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100maxs());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100maxf());

                    ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100maxt());
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
                            excuteByDataTimSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=surfXiliWindenerTimTab.getV01301()+" "+surfXiliWindenerTimTab.getdDatetime();
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
     * 入库
     */
    private static void insertDB(List<SurfXiliWindenerHfsTab> surfXiliWindenerHfsTabList, List<SurfXiliWindenerMdeTab> surfXiliWindenerMdeTabList, List<SurfXiliWindenerMdoTab> surfXiliWindenerMdoTabList, List<SurfXiliWindenerTimTab> surfXiliWindenerTimTabList, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            if (surfXiliWindenerHfsTabList!=null && !surfXiliWindenerHfsTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("S001_hfs_value_table_name") + "("
                        + "D_RETAIN_ID,D_DATA_ID,D_DATETIME, V01301, V04001, \n" +
                        "      V04002, V04003, V06001, \n" +
                        "      V05001, V07001, THEINS50, \n" +
                        "      THEINS70, THEINS100, V02320, \n" +
                        "      HORIWINDSPEED50X, HORIWINDSPEED50Y, HORIWINDSPEED50Z, \n" +
                        "      ULTVIRTEM50, DIAVALUE50, HORIWINDSPEED70X, \n" +
                        "      HORIWINDSPEED70Y, HORIWINDSPEED70Z, ULTVIRTEM70, \n" +
                        "      DIAVALUE70, HORIWINDSPEED100X, HORIWINDSPEED100Y, \n" +
                        "      HORIWINDSPEED100Z, ULTVIRTEM100, DIAVALUE100) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));

                int index = 0;

                List<SurfXiliWindenerHfsTab> list=new ArrayList<>();
                for (SurfXiliWindenerHfsTab surfXiliWindenerHfsTab : surfXiliWindenerHfsTabList) {
                    list.add(surfXiliWindenerHfsTab);
                    int ii = 1;
                    index += 1;
                    try {
                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getdRecordId());//记录标识

                        ps.setString(ii++, StartConfig.ctsCode("S001_hfs_cts_code"));//四级编码

                        ps.setString(ii++, surfXiliWindenerHfsTab.getdDatetime());//资料时间

                        ps.setString(ii++, surfXiliWindenerHfsTab.getV01301());//区站号

                        ps.setShort(ii++, surfXiliWindenerHfsTab.getV04001());//年

                        ps.setShort(ii++, surfXiliWindenerHfsTab.getV04002());//月

                        ps.setShort(ii++, surfXiliWindenerHfsTab.getV04003());//日

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getV06001());//铁塔所在位置经度

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getV05001());//铁塔所在位置纬度

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getV07001());//梯度塔所处地（湖、海）面拔海高度

                        ps.setShort(ii++, surfXiliWindenerHfsTab.getTheins50());//50米超声风温仪安装角度

                        ps.setShort(ii++, surfXiliWindenerHfsTab.getTheins70());//70米超声风温仪安装角度

                        ps.setShort(ii++, surfXiliWindenerHfsTab.getTheins100());//100米超声风温仪安装角度

                        ps.setString(ii++, surfXiliWindenerHfsTab.getV02320());//采集器型号

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed50x());//50米高度水平风速（x轴）

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed50y());//50米高度水平风速（y轴）

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed50z());//50米高度垂向风速（z轴）

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getUltvirtem50());//50米高度超声虚温

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getDiavalue50());//50米高度传感器诊断值

                        ps.setString(ii++, surfXiliWindenerHfsTab.getHoriwindspeed70x());//70米高度水平风速（x轴）

                        ps.setString(ii++, surfXiliWindenerHfsTab.getHoriwindspeed70y());//70米高度水平风速（y轴）

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed70z());//70米高度垂向风速（z轴）

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getUltvirtem70());//70米高度超声虚温

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getDiavalue70());//70米高度传感器诊断值

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed100x());//100米高度水平风速（x轴）

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed100y());//100米高度水平风速（y轴）

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getHoriwindspeed100z());//100米高度垂向风速（z轴）

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getUltvirtem100());//100米高度超声虚温

                        ps.setBigDecimal(ii++, surfXiliWindenerHfsTab.getDiavalue100());//100米高度传感器诊断值

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000== 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        }catch (Exception e){
                            ps.clearBatch();
                            //执行错误时，单条执行
                            excuteByDataHfsSingle(ps,list,cts_code,connection);
                        }
                        list.clear();
                    }
                }
                try {
                    ps.executeBatch();
                    connection.commit();
                }catch (Exception e){
                    ps.clearBatch();
                    //执行错误时，单条执行
                    excuteByDataHfsSingle(ps,list,cts_code,connection);
                }
                list.clear();
            }
            if (surfXiliWindenerMdoTabList!=null && !surfXiliWindenerMdoTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("S001_mdo_value_table_name") + "("
                        + "D_RETAIN_ID,D_DATA_ID,D_DATETIME, V01301, V04001, \n" +
                        "      V04002, V04003, V06001, \n" +
                        "      V05001, V07001,THEINS50,THEINS70,THEINS100,  \n" +
                        "      HOURMIN, \n" +
                        "      AVEHOWINSPE10MIHYWI50X, AVEHOWINSPE10MIHYWI50Y, \n" +
                        "      AVEHOWINSPE10MIHYWI50Z, AVEHOSYWISPE10MIHYWI50, \n" +
                        "      MIAVEHOVESYWISPEHYWI50, MIAVEHOVEWIDIRSYHYWI50, \n" +
                        "      STADE10MINMEHOSYWIDIRHY50, VAHOWISPEUXHIGSUWI50, \n" +
                        "      VAHOWISPEUYHIGSUWI50, VAHOWISPEUZHIGSUWI50, \n" +
                        "      AHWSP10MHWIND70X, AHWSP10MHWIND70Y, AHWSP10MHWIND70Z, \n" +
                        "      AVEHOSYWISPE10MIHYWI70, MIAVEHOVEWIDIRSYHYWI70S, \n" +
                        "      MIAVEHOVEWIDIRSYHYWI70D, STADE10MINMEHOSYWIDIRHY70, \n" +
                        "      VAHOWISPEUXHIGSUWI70, VAHOWISPEUYHIGSUWI70, \n" +
                        "      VAHOWISPEUZHIGSUWI70, AHWSP10MHWIND100X, \n" +
                        "      AHWSP10MHWIND100Y, AHWSP10MHWIND100Z, AVEHOSYWISPE10MIHYWI100, \n" +
                        "      MIAVEHOVEWIDIRSYHYWI100S, MIAVEHOVEWIDIRSYHYWI100D, \n" +
                        "      STADE10MINMEHOSYWIDIRHY100, VAHOWISPEUXHIGSUWI100, \n" +
                        "      VAHOWISPEUYHIGSUWI100, VAHOWISPEUZHIGSUWI100,V04004,V04005) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));
                int index = 0;
                List<SurfXiliWindenerMdoTab> list=new ArrayList<>();
                for (SurfXiliWindenerMdoTab surfXiliWindenerMdoTab : surfXiliWindenerMdoTabList) {
                    int ii = 1;
                    index += 1;
                    list.add(surfXiliWindenerMdoTab);
                    try {
                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getdRecordId());

                        ps.setString(ii++, StartConfig.ctsCode("S001_mdo_cts_code"));//四级编码

                        ps.setString(ii++, surfXiliWindenerMdoTab.getdDatetime());//资料时间

                        ps.setString(ii++, surfXiliWindenerMdoTab.getV01301());

                        ps.setShort(ii++, surfXiliWindenerMdoTab.getV04001());

                        ps.setShort(ii++, surfXiliWindenerMdoTab.getV04002());

                        ps.setShort(ii++, surfXiliWindenerMdoTab.getV04003());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getV06001());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getV05001());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getV07001());

                        ps.setShort(ii++, surfXiliWindenerMdoTab.getTheins50());

                        ps.setShort(ii++, surfXiliWindenerMdoTab.getTheins70());

                        ps.setShort(ii++, surfXiliWindenerMdoTab.getTheins100());

//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwssensoriden50());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwdsensoriden50());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwssensoriden70());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwdsensoriden70());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwssensoriden100());
//
//                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAwdsensoriden100());

                        ps.setString(ii++, surfXiliWindenerMdoTab.getHourmin());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehowinspe10mihywi50x());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehowinspe10mihywi50y());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehowinspe10mihywi50z());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehosywispe10mihywi50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovesywispehywi50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getStade10minmehosywidirhy50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuxhigsuwi50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuyhigsuwi50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuzhigsuwi50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind70x());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind70y());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind70z());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehosywispe10mihywi70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi70s());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi70d());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getStade10minmehosywidirhy70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuxhigsuwi70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuyhigsuwi70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuzhigsuwi70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind100x());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind100y());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAhwsp10mhwind100z());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getAvehosywispe10mihywi100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi100s());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getMiavehovewidirsyhywi100d());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getStade10minmehosywidirhy100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuxhigsuwi100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuyhigsuwi100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdoTab.getVahowispeuzhigsuwi100());

                        ps.setShort(ii++, surfXiliWindenerMdoTab.getV04004());

                        ps.setShort(ii++, surfXiliWindenerMdoTab.getV04005());

                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000== 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        }catch (Exception e){
                            ps.clearBatch();
                            //执行错误时，单条执行
                            excuteByDataMdoSingle(ps,list,cts_code,connection);
                        }
                        list.clear();
                    }
                }
                try {
                    ps.executeBatch();
                    connection.commit();
                }catch (Exception e){
                    ps.clearBatch();
                    //执行错误时，单条执行
                    excuteByDataMdoSingle(ps,list,cts_code,connection);
                }
                list.clear();
            }
            if (surfXiliWindenerMdeTabList!=null && !surfXiliWindenerMdeTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("S001_mde_value_table_name") + "("
                        + "D_RETAIN_ID,D_DATA_ID,D_DATETIME, V01301, V04001, \n" +
                        "      V04002, V06001, V05001, \n" +
                        "      V07001, AWSSENSORIDEN50, AWDSENSORIDEN50, \n" +
                        "      AWSSENSORIDEN70, AWDSENSORIDEN70, AWSSENSORIDEN100, \n" +
                        "      AWDSENSORIDEN100, HOURMIN, A1MAWSPEED50, \n" +
                        "      A1MAWDIRECTION50, A10MAWSPEED50, A10MAWDIRECTION50, \n" +
                        "      AHMAXWSPEED50, AHMAXWSPEEDWDIR50, AHMAXWSPEEDT50, \n" +
                        "      HCFMIWSPEED50, HCFMIWDIR50, AHEXWSPEED50, \n" +
                        "      EHMAXWSPEEDWDIR50, HHMAXWSPEEDT50, A1MAWSPEED70, \n" +
                        "      A1MAWDIRECTION70, A10MAWSPEED70, A10MAWDIRECTION70, \n" +
                        "      AHMAXWSPEED70, AHMAXWSPEEDWDIR70, AHMAXWSPEEDT70, \n" +
                        "      HCFMIWSPEED70, HCFMIWDIR70, AHEXWSPEED70, \n" +
                        "      EHMAXWSPEEDWDIR70, HHMAXWSPEEDT70, A1MAWSPEED100, \n" +
                        "      A1MAWDIRECTION100, A10MAWSPEED100, A10MAWDIRECTION100, \n" +
                        "      AHMAXWSPEED100, AHMAXWSPEEDWDIR100, AHMAXWSPEEDT100, \n" +
                        "      HCFMIWSPEED100, HCFMIWDIR100, AHEXWSPEED100, \n" +
                        "      EHMAXWSPEEDWDIR100, HHMAXWSPEEDT100,V04003,V04004,V04005) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));
                int index = 0;
                List<SurfXiliWindenerMdeTab> list=new ArrayList();
                for (SurfXiliWindenerMdeTab surfXiliWindenerMdeTab : surfXiliWindenerMdeTabList) {
                    list.add(surfXiliWindenerMdeTab);
                    int ii = 1;
                    index += 1;
                    try {
                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getdRecordId());

                        ps.setString(ii++, StartConfig.ctsCode("S001_mde_cts_code"));//四级编码

                        ps.setString(ii++, surfXiliWindenerMdeTab.getdDatetime());//资料时间

                        ps.setString(ii++, surfXiliWindenerMdeTab.getV01301());

                        ps.setShort(ii++, surfXiliWindenerMdeTab.getV04001());

                        ps.setShort(ii++, surfXiliWindenerMdeTab.getV04002());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getV06001());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getV05001());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getV07001());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwssensoriden50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwdsensoriden50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwssensoriden70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwdsensoriden70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwssensoriden100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAwdsensoriden100());

                        ps.setString(ii++, surfXiliWindenerMdeTab.getHourmin());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawspeed50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawdirection50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawspeed50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawdirection50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeed50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedwdir50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedt50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwspeed50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwdir50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhexwspeed50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getEhmaxwspeedwdir50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHhmaxwspeedt50());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawspeed70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawdirection70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawspeed70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawdirection70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeed70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedwdir70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedt70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwspeed70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwdir70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhexwspeed70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getEhmaxwspeedwdir70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHhmaxwspeedt70());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawspeed100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA1mawdirection100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawspeed100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getA10mawdirection100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeed100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedwdir100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhmaxwspeedt100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwspeed100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHcfmiwdir100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getAhexwspeed100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getEhmaxwspeedwdir100());

                        ps.setBigDecimal(ii++, surfXiliWindenerMdeTab.getHhmaxwspeedt100());

                        ps.setShort(ii++, surfXiliWindenerMdeTab.getV04003());

                        ps.setShort(ii++, surfXiliWindenerMdeTab.getV04004());

                        ps.setShort(ii++, surfXiliWindenerMdeTab.getV04005());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000== 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        }catch (Exception e){
                            ps.clearBatch();
                            //执行错误时，单条执行
                            excuteByDataMdeSingle(ps,list,cts_code,connection);
                        }
                        list.clear();
                    }
                }
                try {
                    ps.executeBatch();
                    connection.commit();
                }catch (Exception e){
                    ps.clearBatch();
                    //执行错误时，单条执行
                    excuteByDataMdeSingle(ps,list,cts_code,connection);
                }
                list.clear();
            }
            if (surfXiliWindenerTimTabList!=null && !surfXiliWindenerTimTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("S001_tim_value_table_name") + "("
                        + "D_RETAIN_ID,D_DATA_ID,D_DATETIME, V01301, V04001, \n" +
                        "      V04002, V04003, V04004, \n" +
                        "      V06001, V05001, V07001, \n" +
                        "      AWSSENSORIDEN50, AWDSENSORIDEN50, AWSSENSORIDEN70,AWDSENSORIDEN70,AWSSENSORIDEN100,AWDSENSORIDEN100, \n" +
                        "      V02320, HOURMIN, AVSIWMAH50, \n" +
                        "      MEWDAH50, W10MINAVSIWMAH50, W10MINMAWISARHO50, \n" +
                        "      WHOURAVSIWMAH50, WHOURMAWISARHO50, WHOURMAWISARHO50TIME, \n" +
                        "      WHOURMAWISARHO50MAXS, WHOURMAWISARHO50MAXF, \n" +
                        "      WHOURMAWISARHO50MAXT, WAVSIWMAH70, WMEWDAH70, \n" +
                        "      W10MINAVSIWMAH70, W10MINMAWISARHO70, WHOURAVSIWMAH70, \n" +
                        "      WHOURMAWISARHO70, WHOURMAWISARHO70TIME, \n" +
                        "      WHOURMAWISARHO70MAXS, WHOURMAWISARHO70MAXF, \n" +
                        "      WHOURMAWISARHO70MAXT, WAVSIWMAH100, WMEWDAH100, \n" +
                        "      W10MINAVSIWMAH100, W10MINMAWISARHO100, \n" +
                        "      WHOURAVSIWMAH100, WHOURMAWISARHO100, WHOURMAWISARHO100TIME, \n" +
                        "      WHOURMAWISARHO100MAXS, WHOURMAWISARHO100MAXF, \n" +
                        "      WHOURMAWISARHO100MAXT) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));
                int index = 0;
                List<SurfXiliWindenerTimTab> list=new ArrayList();
                for (SurfXiliWindenerTimTab surfXiliWindenerTimTab : surfXiliWindenerTimTabList) {
                    list.add(surfXiliWindenerTimTab);
                    int ii = 1;
                    index += 1;
                    try {
                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getdRecordId());

                        ps.setString(ii++, StartConfig.ctsCode("S001_tim_cts_code"));//四级编码

                        ps.setString(ii++, surfXiliWindenerTimTab.getdDatetime());//资料时间]

                        ps.setString(ii++, surfXiliWindenerTimTab.getV01301());

                        ps.setShort(ii++, surfXiliWindenerTimTab.getV04001());

                        ps.setShort(ii++, surfXiliWindenerTimTab.getV04002());

                        ps.setShort(ii++, surfXiliWindenerTimTab.getV04003());

                        ps.setShort(ii++, surfXiliWindenerTimTab.getV04004());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getV06001());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getV05001());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getV07001());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwssensoriden50());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwdsensoriden50());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwssensoriden70());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwdsensoriden70());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwssensoriden100());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAwdsensoriden100());

                        ps.setString(ii++, surfXiliWindenerTimTab.getV02320());

                        ps.setString(ii++, surfXiliWindenerTimTab.getHourmin());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getAvsiwmah50());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getMewdah50());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minavsiwmah50());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minmawisarho50());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhouravsiwmah50());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50time());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50maxs());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50maxf());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho50maxt());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWavsiwmah70());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWmewdah70());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minavsiwmah70());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minmawisarho70());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhouravsiwmah70());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70time());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70maxs());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70maxf());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho70maxt());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWavsiwmah100());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWmewdah100());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minavsiwmah100());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getW10minmawisarho100());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhouravsiwmah100());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100time());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100maxs());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100maxf());

                        ps.setBigDecimal(ii++, surfXiliWindenerTimTab.getWhourmawisarho100maxt());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("列号：" + ii + "有误");
                    }
                    ps.addBatch();
                    if (index % 1000== 0) {
                        try {
                            ps.executeBatch();
                            connection.commit();
                            ps.clearBatch();
                        }catch (Exception e){
                            ps.clearBatch();
                            //执行错误时，单条执行
                            excuteByDataTimSingle(ps,list,cts_code,connection);
                        }
                        list.clear();
                    }
                }
                try {
                    ps.executeBatch();
                    connection.commit();
                }catch (Exception e){
                    ps.clearBatch();
                    //执行错误时，单条执行
                    excuteByDataTimSingle(ps,list,cts_code,connection);
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
                if(connection!=null){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
