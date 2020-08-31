package cma.cimiss2.dpc.indb.upar.dc_upar_chn_mul_ftm_sig_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmMadTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmSigTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmWewTab;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/23 0023 11:11
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class ChnMulFtmSigTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnMulFtmSigTabService.diQueues = diQueues;
    }

    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataSingle(PreparedStatement ps, List<UparChnMulFtmSigTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<UparChnMulFtmSigTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (UparChnMulFtmSigTab uparChnMulFtmSigTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(uparChnMulFtmSigTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, uparChnMulFtmSigTab.getdRecordId());
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, uparChnMulFtmSigTab.getV01301());
                    ps.setShort(ii++, uparChnMulFtmSigTab.getV04001());
                    ps.setShort(ii++,uparChnMulFtmSigTab.getV04002());
                    ps.setString(ii++, uparChnMulFtmSigTab.getdDatetime());
                    ps.setString(ii++, uparChnMulFtmSigTab.getdUpdateTime());
                    ps.setString(ii++, uparChnMulFtmSigTab.getvLevelC());
                    ps.setBigDecimal(ii++, uparChnMulFtmSigTab.getV10004());
                    ps.setBigDecimal(ii++, uparChnMulFtmSigTab.getV12001());
                    ps.setBigDecimal(ii++, uparChnMulFtmSigTab.getV12003());
                    ps.setShort(ii++, uparChnMulFtmSigTab.getqBasicparameter());
                    ps.setShort(ii++, uparChnMulFtmSigTab.getQ07004());
                    ps.setShort(ii++, uparChnMulFtmSigTab.getQ12001());
                    ps.setBigDecimal(ii++, uparChnMulFtmSigTab.getQ12003());
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
                        data=uparChnMulFtmSigTab.getV01301()+" "+uparChnMulFtmSigTab.getdDatetime()+"  "+uparChnMulFtmSigTab.getV10004();
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
    public static DataBaseAction processSuccessReport(ParseResult<UparChnMulFtmSigTab> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        List<UparChnMulFtmSigTab> pollenLists = parseResult.getData();
        insertDB(pollenLists, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     *  @param parseResult  数据
     * @param connection
     * @param recv_time    消息接收时间
     * @param loggerBuffer
     * @param filename
     * @param cts_code
     */
    private static void insertDB(List<UparChnMulFtmSigTab> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("R018_value_table_name") + "("
                    + "D_RETAIN_ID,D_DATA_ID, V01301,V04001, V04002, \n" +
                    "      D_DATETIME, TIMES, V_LEVEL_C, \n" +
                    "      V10004, V12001, V12003, \n" +
                    "      Q_BASICPARAMETER, Q07004, Q12001, \n" +
                    "      Q12003) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;
            List<UparChnMulFtmSigTab> list=new ArrayList();
            for (UparChnMulFtmSigTab uparChnMulFtmSigTab : parseResult) {
                int ii = 1;
                index += 1;
                list.add(uparChnMulFtmSigTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, uparChnMulFtmSigTab.getdRecordId());
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, uparChnMulFtmSigTab.getV01301());
                    ps.setShort(ii++, uparChnMulFtmSigTab.getV04001());
                    ps.setShort(ii++,uparChnMulFtmSigTab.getV04002());
                    ps.setString(ii++, uparChnMulFtmSigTab.getdDatetime());
                    ps.setString(ii++, uparChnMulFtmSigTab.getdUpdateTime());
                    ps.setString(ii++, uparChnMulFtmSigTab.getvLevelC());
                    ps.setBigDecimal(ii++, uparChnMulFtmSigTab.getV10004());
                    ps.setBigDecimal(ii++, uparChnMulFtmSigTab.getV12001());
                    ps.setBigDecimal(ii++, uparChnMulFtmSigTab.getV12003());
                    ps.setShort(ii++, uparChnMulFtmSigTab.getqBasicparameter());
                    ps.setShort(ii++, uparChnMulFtmSigTab.getQ07004());
                    ps.setShort(ii++, uparChnMulFtmSigTab.getQ12001());
                    ps.setBigDecimal(ii++, uparChnMulFtmSigTab.getQ12003());
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
