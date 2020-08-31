package cma.cimiss2.dpc.indb.upar.dc_upar_light_adtd_mul_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMyerTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparDigNmStlFtmTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparLightAdtdMulTab;
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
 * @date ：Created in 2019/10/12 0012 14:34
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class LightAdtdMulTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        LightAdtdMulTabService.diQueues = diQueues;
    }
    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataSingle(PreparedStatement ps, List<UparLightAdtdMulTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<UparLightAdtdMulTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (UparLightAdtdMulTab uparLightAdtdMulTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(uparLightAdtdMulTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, uparLightAdtdMulTab.getdRecordId());
                    ps.setString(ii++, "etl-file");
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, uparLightAdtdMulTab.getdIymdhm());
                    ps.setString(ii++, uparLightAdtdMulTab.getdRymdhm());
                    ps.setString(ii++,  uparLightAdtdMulTab.getdUpdateTime());
                    ps.setString(ii++, uparLightAdtdMulTab.getdDatetime());
                    ps.setBigDecimal(ii++,uparLightAdtdMulTab.getV05001());
                    ps.setBigDecimal(ii++, uparLightAdtdMulTab.getV06001());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04001());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04002());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04003());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04004());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04005());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04006());
                    ps.setInt(ii++,uparLightAdtdMulTab.getV04007());
                    ps.setInt(ii++,999999);
                    ps.setBigDecimal(ii++, uparLightAdtdMulTab.getV73011());
                    ps.setBigDecimal(ii++, uparLightAdtdMulTab.getV73016());
                    ps.setBigDecimal(ii++, uparLightAdtdMulTab.getV73023());
                    ps.setString(ii++, uparLightAdtdMulTab.getV73110());
                    ps.setString(ii++, uparLightAdtdMulTab.getV010151());
                    ps.setString(ii++, uparLightAdtdMulTab.getV010152());
                    ps.setString(ii++, uparLightAdtdMulTab.getV010153());
                    ps.setString(ii++, uparLightAdtdMulTab.getvBbb());
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
                            excuteByDataSingle(ps, list1, cts_code, connection);
                        }
                        list1.clear();
                    }
                }else{
                    String data=null;
                    //单条执行
                    try {
                        data=uparLightAdtdMulTab.getdDatetime()+"  etl-file";
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
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(ParseResult<UparLightAdtdMulTab> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        List<UparLightAdtdMulTab> pollenLists = parseResult.getData();
        insertDB(pollenLists, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     *
     * @param parseResult  数据
     * @param connection
     * @param recv_time    消息接收时间
     * @param loggerBuffer
     * @param filename
     * @param cts_code
     */
    private static void insertDB(List<UparLightAdtdMulTab> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("R020_value_table_name") + "("
                    + "D_RETAIN_ID, D_SOURCE_ID, D_DATA_ID, D_IYMDHM, D_RYMDHM, D_UPDATE_TIME, D_DATETIME, V05001, V06001, V04001, V04002, V04003, V04004, V04005, V04006, V04007, V08300, V73011, V73016, V73023, V73110, V01015_1, V01015_2, V01015_3, V_BBB) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;
            List<UparLightAdtdMulTab> list=new ArrayList();
            for (UparLightAdtdMulTab uparLightAdtdMulTab : parseResult) {
                int ii = 1;
                index += 1;
                list.add(uparLightAdtdMulTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, uparLightAdtdMulTab.getdRecordId());
                    ps.setString(ii++, "etl-file");
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, uparLightAdtdMulTab.getdIymdhm());
                    ps.setString(ii++, uparLightAdtdMulTab.getdRymdhm());
                    ps.setString(ii++,  uparLightAdtdMulTab.getdUpdateTime());
                    ps.setString(ii++, uparLightAdtdMulTab.getdDatetime());
                    ps.setBigDecimal(ii++,uparLightAdtdMulTab.getV05001());
                    ps.setBigDecimal(ii++, uparLightAdtdMulTab.getV06001());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04001());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04002());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04003());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04004());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04005());
                    ps.setShort(ii++, uparLightAdtdMulTab.getV04006());
                    ps.setInt(ii++,uparLightAdtdMulTab.getV04007());
                    //ps.setInt(ii++,uparLightAdtdMulTab.getV08300());
                    ps.setInt(ii++,999999);
                    ps.setBigDecimal(ii++, uparLightAdtdMulTab.getV73011());
                    ps.setBigDecimal(ii++, uparLightAdtdMulTab.getV73016());
                    ps.setBigDecimal(ii++, uparLightAdtdMulTab.getV73023());
                    ps.setString(ii++, uparLightAdtdMulTab.getV73110());
                    ps.setString(ii++, uparLightAdtdMulTab.getV010151());
                    ps.setString(ii++, uparLightAdtdMulTab.getV010152());
                    ps.setString(ii++, uparLightAdtdMulTab.getV010153());
                    ps.setString(ii++, uparLightAdtdMulTab.getvBbb());
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
                        excuteByDataSingle(ps,list,cts_code,connection);
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
                excuteByDataSingle(ps,list,cts_code,connection);
            }
            list.clear();
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
