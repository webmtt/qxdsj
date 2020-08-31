package cma.cimiss2.dpc.indb.radi.dc_radi_dig_chn_mul_tab.service;


import cma.cimiss2.dpc.decoder.bean.radi.*;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.BlockingQueue;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/29 0029 14:26
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class DigChnMulTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        DigChnMulTabService.diQueues = diQueues;
    }
    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param hour_cts_code
     */
    public static void excuteByDataHourSingle(PreparedStatement ps, List<RadiDigChnMulHourTab> list, String hour_cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<RadiDigChnMulHourTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (RadiDigChnMulHourTab radiDigChnMulHourTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(radiDigChnMulHourTab);
                try {
                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getdRecordId());

                    ps.setString(ii++, hour_cts_code);

                    ps.setString(ii++, radiDigChnMulHourTab.getdIymdhm());

                    ps.setString(ii++, radiDigChnMulHourTab.getdRymdhm());

                    ps.setString(ii++, radiDigChnMulHourTab.getdUpdateTime());

                    ps.setString(ii++, radiDigChnMulHourTab.getdDatetime());

                    ps.setString(ii++, radiDigChnMulHourTab.getV01301());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV01300());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV05001());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV06001());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV07001());

                    ps.setShort(ii++, radiDigChnMulHourTab.getV04001());

                    ps.setShort(ii++, radiDigChnMulHourTab.getV04002());

                    ps.setShort(ii++, radiDigChnMulHourTab.getV04003());

                    ps.setShort(ii++, radiDigChnMulHourTab.getV04004());

                    ps.setShort(ii++, radiDigChnMulHourTab.getV04005());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV14311());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV14312());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV14313());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV14314());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV14315());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV14316());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14320());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431105());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1402105052());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14308());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431205());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431205052());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431206());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431206052());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14322());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431305());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431305052());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14309());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431405());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431405052());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14306());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431505());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431505052());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14307());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431605());

                    ps.setInt(ii++, radiDigChnMulHourTab.getV1431605052());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14032());

                    ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV15483());

                    ps.setString(ii++, "etl-file");

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
                            excuteByDataHourSingle(ps, list1, hour_cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=radiDigChnMulHourTab.getV01301()+" "+radiDigChnMulHourTab.getdDatetime();
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
     * @param day_cts_code
     */
    public static void excuteByDataDaySingle(PreparedStatement ps, List<RadiDigChnMulDayTab> list, String day_cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<RadiDigChnMulDayTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (RadiDigChnMulDayTab radiDigChnMulDayTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(radiDigChnMulDayTab);
                try {
                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getdRetainId());

                    ps.setString(ii++, day_cts_code);

                    ps.setString(ii++, radiDigChnMulDayTab.getdIymdhm());

                    ps.setString(ii++, radiDigChnMulDayTab.getdRymdhm());

                    ps.setString(ii++, radiDigChnMulDayTab.getdUpdateTime());

                    ps.setString(ii++, radiDigChnMulDayTab.getdDatetime());

                    ps.setString(ii++, radiDigChnMulDayTab.getV01301());

                    ps.setInt(ii++, radiDigChnMulDayTab.getV01300());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV05001());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV06001());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV07001());

                    ps.setShort(ii++, radiDigChnMulDayTab.getV04001());

                    ps.setShort(ii++, radiDigChnMulDayTab.getV04002());

                    ps.setShort(ii++, radiDigChnMulDayTab.getV04003());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14320Day());

                    ps.setInt(ii++, radiDigChnMulDayTab.getV1431105Day());

                    ps.setString(ii++, radiDigChnMulDayTab.getV1402105052Day());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14308Day());

                    ps.setInt(ii++, radiDigChnMulDayTab.getV1431205Day());

                    ps.setString(ii++, radiDigChnMulDayTab.getV1431205052Day());

                    ps.setInt(ii++, radiDigChnMulDayTab.getV1431206Day());

                    ps.setString(ii++, radiDigChnMulDayTab.getV1431206052Day());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14322Day());

                    ps.setInt(ii++, radiDigChnMulDayTab.getV1431305Day());

                    ps.setString(ii++, radiDigChnMulDayTab.getV1431305052Day());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14309Day());

                    ps.setInt(ii++, radiDigChnMulDayTab.getV1431405Day());

                    ps.setString(ii++, radiDigChnMulDayTab.getV1431405052Day());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14306Day());

                    ps.setInt(ii++, radiDigChnMulDayTab.getV1431505Day());

                    ps.setString(ii++, radiDigChnMulDayTab.getV1431505052Day());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14307());

                    ps.setInt(ii++, radiDigChnMulDayTab.getV1431605Day());

                    ps.setString(ii++, radiDigChnMulDayTab.getV1431605052Day());

                    ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14032());

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
                            excuteByDataDaySingle(ps, list1, day_cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=radiDigChnMulDayTab.getV01301()+" "+radiDigChnMulDayTab.getdDatetime();
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
     * @param recv_time
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(List<RadiDigChnMulHourTab> radiDigChnMulHourTabList, List<RadiDigChnMulDayTab> radiDigChnMulDayTabList, Date recv_time, String hour_cts_code,String day_cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        insertDB(radiDigChnMulHourTabList,radiDigChnMulDayTabList, connection, recv_time, loggerBuffer, filename, hour_cts_code,day_cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     * @param connection
     * @param recv_time    消息接收时间
     * @param loggerBuffer
     * @param filename
     */
    private static void insertDB(List<RadiDigChnMulHourTab> radiDigChnMulHourTabList, List<RadiDigChnMulDayTab> radiDigChnMulDayTabList, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String hour_cts_code, String day_cts_code) {
        PreparedStatement ps = null;
        try {
            if (radiDigChnMulHourTabList!=null && !radiDigChnMulHourTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("R011_hour_value_table_name") + "("
                        + "D_RETAIN_ID, D_DATA_ID, D_IYMDHM, \n" +
                        "      D_RYMDHM, D_UPDATE_TIME, D_DATETIME, \n" +
                        "      V01301, V01300, V05001, \n" +
                        "      V06001, V07001, V04001, \n" +
                        "      V04002, V04003, V04004, \n" +
                        "      V04005, V14311, V14312, \n" +
                        "      V14313, V14314, V14315, \n" +
                        "      V14316, V14320, V14311_05, \n" +
                        "      V14021_05_052, V14308, V14312_05, \n" +
                        "      V14312_05_052, V14312_06, V14312_06_052, \n" +
                        "      V14322, V14313_05, V14313_05_052, \n" +
                        "      V14309, V14314_05, V14314_05_052, \n" +
                        "      V14306, V14315_05, V14315_05_052, \n" +
                        "      V14307, V14316_05, V14316_05_052, \n" +
                        "      V14032, V15483,D_SOURCE_ID) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));

                int index = 0;
                List<RadiDigChnMulHourTab> list=new ArrayList();
                for (RadiDigChnMulHourTab radiDigChnMulHourTab : radiDigChnMulHourTabList) {
                    int ii = 1;
                    index += 1;
                    list.add(radiDigChnMulHourTab);
                    try {
                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getdRecordId());

                        ps.setString(ii++, hour_cts_code);

                        ps.setString(ii++, radiDigChnMulHourTab.getdIymdhm());

                        ps.setString(ii++, radiDigChnMulHourTab.getdRymdhm());

                        ps.setString(ii++, radiDigChnMulHourTab.getdUpdateTime());

                        ps.setString(ii++, radiDigChnMulHourTab.getdDatetime());

                        ps.setString(ii++, radiDigChnMulHourTab.getV01301());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV01300());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV05001());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV06001());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV07001());

                        ps.setShort(ii++, radiDigChnMulHourTab.getV04001());

                        ps.setShort(ii++, radiDigChnMulHourTab.getV04002());

                        ps.setShort(ii++, radiDigChnMulHourTab.getV04003());

                        ps.setShort(ii++, radiDigChnMulHourTab.getV04004());

                        ps.setShort(ii++, radiDigChnMulHourTab.getV04005());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV14311());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV14312());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV14313());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV14314());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV14315());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV14316());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14320());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431105());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1402105052());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14308());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431205());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431205052());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431206());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431206052());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14322());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431305());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431305052());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14309());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431405());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431405052());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14306());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431505());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431505052());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14307());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431605());

                        ps.setInt(ii++, radiDigChnMulHourTab.getV1431605052());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV14032());

                        ps.setBigDecimal(ii++, radiDigChnMulHourTab.getV15483());

                        ps.setString(ii++, "etl-file");

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
                            excuteByDataHourSingle(ps, list, hour_cts_code, connection);
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
                    excuteByDataHourSingle(ps, list, hour_cts_code, connection);
                }
                list.clear();
            }
            if (radiDigChnMulDayTabList!=null && !radiDigChnMulDayTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("R011_day_value_table_name") + "("
                        + "D_RETAIN_ID, D_DATA_ID, D_IYMDHM, \n" +
                        "      D_RYMDHM, D_UPDATE_TIME, D_DATETIME, \n" +
                        "      V01301, V01300, V05001, \n" +
                        "      V06001, V07001, V04001, \n" +
                        "      V04002, V04003,  \n" +
                        "      V14320_DAY, V14311_05_DAY, \n" +
                        "      V14021_05_052_DAY, V14308_DAY, V14312_05_DAY, \n" +
                        "      V14312_05_052_DAY, V14312_06_DAY, V14312_06_052_DAY, \n" +
                        "      V14322_DAY, V14313_05_DAY, V14313_05_052_DAY, \n" +
                        "      V14309_DAY, V14314_05_DAY, V14314_05_052_DAY, \n" +
                        "      V14306_DAY, V14315_05_DAY, V14315_05_052_DAY, \n" +
                        "      V14307, V14316_05_DAY, V14316_05_052_DAY,V14032) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);

                int index = 0;
                List<RadiDigChnMulDayTab> list=new ArrayList();
                for (RadiDigChnMulDayTab radiDigChnMulDayTab : radiDigChnMulDayTabList) {
                    int ii = 1;
                    index += 1;
                    list.add(radiDigChnMulDayTab);
                    try {
                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getdRetainId());

                        ps.setString(ii++, day_cts_code);

                        ps.setString(ii++, radiDigChnMulDayTab.getdIymdhm());

                        ps.setString(ii++, radiDigChnMulDayTab.getdRymdhm());

                        ps.setString(ii++, radiDigChnMulDayTab.getdUpdateTime());

                        ps.setString(ii++, radiDigChnMulDayTab.getdDatetime());

                        ps.setString(ii++, radiDigChnMulDayTab.getV01301());

                        ps.setInt(ii++, radiDigChnMulDayTab.getV01300());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV05001());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV06001());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV07001());

                        ps.setShort(ii++, radiDigChnMulDayTab.getV04001());

                        ps.setShort(ii++, radiDigChnMulDayTab.getV04002());

                        ps.setShort(ii++, radiDigChnMulDayTab.getV04003());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14320Day());

                        ps.setInt(ii++, radiDigChnMulDayTab.getV1431105Day());

                        ps.setString(ii++, radiDigChnMulDayTab.getV1402105052Day());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14308Day());

                        ps.setInt(ii++, radiDigChnMulDayTab.getV1431205Day());

                        ps.setString(ii++, radiDigChnMulDayTab.getV1431205052Day());

                        ps.setInt(ii++, radiDigChnMulDayTab.getV1431206Day());

                        ps.setString(ii++, radiDigChnMulDayTab.getV1431206052Day());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14322Day());

                        ps.setInt(ii++, radiDigChnMulDayTab.getV1431305Day());

                        ps.setString(ii++, radiDigChnMulDayTab.getV1431305052Day());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14309Day());

                        ps.setInt(ii++, radiDigChnMulDayTab.getV1431405Day());

                        ps.setString(ii++, radiDigChnMulDayTab.getV1431405052Day());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14306Day());

                        ps.setInt(ii++, radiDigChnMulDayTab.getV1431505Day());

                        ps.setString(ii++, radiDigChnMulDayTab.getV1431505052Day());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14307());

                        ps.setInt(ii++, radiDigChnMulDayTab.getV1431605Day());

                        ps.setString(ii++, radiDigChnMulDayTab.getV1431605052Day());

                        ps.setBigDecimal(ii++, radiDigChnMulDayTab.getV14032());

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
                            excuteByDataDaySingle(ps, list, day_cts_code, connection);
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
                    excuteByDataDaySingle(ps, list, day_cts_code, connection);
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
