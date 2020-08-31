package cma.cimiss2.dpc.indb.radi.cd_radi_bmb_dat_tab.service;



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
 * @date ：Created in 2019/10/31 0031 17:11
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class BmbDatTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        BmbDatTabService.diQueues = diQueues;
    }

    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param hour_cts_code
     */
    public static void excuteByDataHourSingle(PreparedStatement ps, List<RadiBmbHourDatTab> list, String hour_cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<RadiBmbHourDatTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (RadiBmbHourDatTab radiBmbHourDatTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(radiBmbHourDatTab);
                try {
                    ps.setString(ii++, radiBmbHourDatTab.getdRetainId());

                    ps.setString(ii++, radiBmbHourDatTab.getdDatetime());

                    ps.setString(ii++, radiBmbHourDatTab.getV01301());

                    ps.setShort(ii++, radiBmbHourDatTab.getV04001());

                    ps.setShort(ii++, radiBmbHourDatTab.getV04002());

                    ps.setShort(ii++, radiBmbHourDatTab.getV04003());

                    ps.setShort(ii++, radiBmbHourDatTab.getV04004());

                    ps.setShort(ii++, radiBmbHourDatTab.getV04005());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getV06001());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getV05001());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getV07001());

                    ps.setShort(ii++, radiBmbHourDatTab.getSitelevel());

//                        ps.setString(ii++, radiBmbHourDatTab.getdDatetime());

                    ps.setString(ii++, hour_cts_code);

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getTriHp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getTreHp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getTriMaxHp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getTriHpDate());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getLatitudeAltitude());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getLatitudeTriHp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getLatitudeMaxAltitude());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getLatitudeAltitudeDate());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getStreRfp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiotprRfp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getStreMaxRfp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getStreRfpDate());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getScatriRfp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getScatreRfp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getScatriMaxRfp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getScatriRfpDate());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiotprtRfp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiopdrRfp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiotprtMaxRfp());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiotprtRfpDate());

                    ps.setBigDecimal(ii++, radiBmbHourDatTab.getSunNum());
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
                        data=radiBmbHourDatTab.getV01301()+" "+radiBmbHourDatTab.getdDatetime();
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
     * @param min_cts_code
     */
    public static void excuteByDataMinSingle(PreparedStatement ps, List<RadiBmbMinDatTab> list, String min_cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<RadiBmbMinDatTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (RadiBmbMinDatTab radiBmbMinDatTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(radiBmbMinDatTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, radiBmbMinDatTab.getdRetainId());

                    ps.setString(ii++, min_cts_code);

                    ps.setString(ii++, radiBmbMinDatTab.getdDatetime());

                    ps.setString(ii++, radiBmbMinDatTab.getV01301());

                    ps.setShort(ii++, radiBmbMinDatTab.getV04001());

                    ps.setShort(ii++, radiBmbMinDatTab.getV04002());

                    ps.setShort(ii++, radiBmbMinDatTab.getV04003());

                    ps.setShort(ii++, radiBmbMinDatTab.getV04004());

                    ps.setShort(ii++, radiBmbMinDatTab.getV04005());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getV06001());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getV05001());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getV07001());

                    ps.setShort(ii++, radiBmbMinDatTab.getSitelevel());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getTriHeight());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getTriHp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getTreHp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getRotaryHeight());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudePolysilicon());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudeMonocrystalline());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudeAltitude());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudeTriHp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudeTreHp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getStreRfp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getScatriRfp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getScatreRfp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getMiotprRfp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getMiotprtRfp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getMiopdrRfp());

                    ps.setBigDecimal(ii++, radiBmbMinDatTab.getSunNum());

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
                            excuteByDataMinSingle(ps, list1, min_cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=radiBmbMinDatTab.getV01301()+" "+radiBmbMinDatTab.getdDatetime();
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
    public static DataBaseAction processSuccessReport(List<RadiBmbHourDatTab> radiBmbHourDatTabList, List<RadiBmbMinDatTab> radiBmbMinDatTabList, Date recv_time, String hour_cts_code, String min_cts_code,StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        insertDB(radiBmbHourDatTabList,radiBmbMinDatTabList, connection, recv_time, loggerBuffer, filename, hour_cts_code,min_cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     * @param connection
     * @param recv_time    消息接收时间
     * @param loggerBuffer
     * @param filename
     */
    private static void insertDB(List<RadiBmbHourDatTab> radiBmbHourDatTabList, List<RadiBmbMinDatTab> radiBmbMinDatTabList, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String hour_cts_code, String min_cts_code) {
        PreparedStatement ps = null;
        try {
            if (radiBmbHourDatTabList!=null && !radiBmbHourDatTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("R016_hour_value_table_name") + "("
                        + "D_RETAIN_ID,D_DATETIME, V01301, V04001, \n" +
                        "      V04002, V04003, V04004, \n" +
                        "      V04005, V06001, V05001, \n" +
                        "      V07001, SITELEVEL, \n" +
                        "      D_DATA_ID, TRI_HP, \n" +
                        "      TRE_HP, TRI_MAX_HP, TRI_HP_DATE, \n" +
                        "      LATITUDE_ALTITUDE, LATITUDE_TRI_HP, LATITUDE_MAX_ALTITUDE, \n" +
                        "      LATITUDE_ALTITUDE_DATE, STRE_RFP, MIOTPR_RFP, \n" +
                        "      STRE_MAX_RFP, STRE_RFP_DATE, SCATRI_RFP, \n" +
                        "      SCATRE_RFP, SCATRI_MAX_RFP, SCATRI_RFP_DATE, \n" +
                        "      MIOTPRT_RFP, MIOPDR_RFP, MIOTPRT_MAX_RFP, \n" +
                        "      MIOTPRT_RFP_DATE, SUN_NUM) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));

                int index = 0;
                List<RadiBmbHourDatTab> list=new ArrayList();
                for (RadiBmbHourDatTab radiBmbHourDatTab : radiBmbHourDatTabList) {
                    int ii = 1;
                    index += 1;
                    list.add(radiBmbHourDatTab);
                    try {
                        ps.setString(ii++, radiBmbHourDatTab.getdRetainId());

                        ps.setString(ii++, radiBmbHourDatTab.getdDatetime());

                        ps.setString(ii++, radiBmbHourDatTab.getV01301());

                        ps.setShort(ii++, radiBmbHourDatTab.getV04001());

                        ps.setShort(ii++, radiBmbHourDatTab.getV04002());

                        ps.setShort(ii++, radiBmbHourDatTab.getV04003());

                        ps.setShort(ii++, radiBmbHourDatTab.getV04004());

                        ps.setShort(ii++, radiBmbHourDatTab.getV04005());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getV06001());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getV05001());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getV07001());

                        ps.setShort(ii++, radiBmbHourDatTab.getSitelevel());

//                        ps.setString(ii++, radiBmbHourDatTab.getdDatetime());

                        ps.setString(ii++, hour_cts_code);

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getTriHp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getTreHp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getTriMaxHp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getTriHpDate());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getLatitudeAltitude());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getLatitudeTriHp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getLatitudeMaxAltitude());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getLatitudeAltitudeDate());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getStreRfp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiotprRfp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getStreMaxRfp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getStreRfpDate());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getScatriRfp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getScatreRfp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getScatriMaxRfp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getScatriRfpDate());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiotprtRfp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiopdrRfp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiotprtMaxRfp());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getMiotprtRfpDate());

                        ps.setBigDecimal(ii++, radiBmbHourDatTab.getSunNum());

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
            if (radiBmbMinDatTabList!=null && !radiBmbMinDatTabList.isEmpty()) {
                String sql = " INSERT INTO " + StartConfig.valueTable("R016_min_value_table_name") + "("
                        + "D_RETAIN_ID,D_DATA_ID,D_DATETIME, V01301, V04001, \n" +
                        "      V04002, V04003, V04004, \n" +
                        "      V04005, V06001, V05001, \n" +
                        "      V07001, SITELEVEL, \n" +
                        "      TRI_HEIGHT, TRI_HP, TRE_HP, \n" +
                        "      ROTARY_HEIGHT, LATITUDE_POLYSILICON, LATITUDE_MONOCRYSTALLINE, \n" +
                        "      LATITUDE_ALTITUDE, LATITUDE_TRI_HP, LATITUDE_TRE_HP, \n" +
                        "      STRE_RFP, SCATRI_RFP, SCATRE_RFP, \n" +
                        "      MIOTPR_RFP, MIOTPRT_RFP, MIOPDR_RFP, \n" +
                        "      SUN_NUM) "
                        + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                ps = new LoggableStatement(connection, sql);
                connection.setAutoCommit(false);
                filename = filename.substring(0, filename.indexOf("."));

                int index = 0;
                List<RadiBmbMinDatTab> list=new ArrayList();
                for (RadiBmbMinDatTab radiBmbMinDatTab : radiBmbMinDatTabList) {
                    int ii = 1;
                    index += 1;
                    list.add(radiBmbMinDatTab);
                    try {
                        // 记录主键	D_RECORD_ID	varchar(500)
                        ps.setString(ii++, radiBmbMinDatTab.getdRetainId());

                        ps.setString(ii++, min_cts_code);

                        ps.setString(ii++, radiBmbMinDatTab.getdDatetime());

                        ps.setString(ii++, radiBmbMinDatTab.getV01301());

                        ps.setShort(ii++, radiBmbMinDatTab.getV04001());

                        ps.setShort(ii++, radiBmbMinDatTab.getV04002());

                        ps.setShort(ii++, radiBmbMinDatTab.getV04003());

                        ps.setShort(ii++, radiBmbMinDatTab.getV04004());

                        ps.setShort(ii++, radiBmbMinDatTab.getV04005());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getV06001());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getV05001());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getV07001());

                        ps.setShort(ii++, radiBmbMinDatTab.getSitelevel());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getTriHeight());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getTriHp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getTreHp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getRotaryHeight());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudePolysilicon());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudeMonocrystalline());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudeAltitude());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudeTriHp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getLatitudeTreHp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getStreRfp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getScatriRfp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getScatreRfp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getMiotprRfp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getMiotprtRfp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getMiopdrRfp());

                        ps.setBigDecimal(ii++, radiBmbMinDatTab.getSunNum());

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
                            excuteByDataMinSingle(ps, list, hour_cts_code, connection);
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
                    excuteByDataMinSingle(ps, list, hour_cts_code, connection);
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
