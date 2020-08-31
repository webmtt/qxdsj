package cma.cimiss2.dpc.indb.upar.dc_upar_chn_mul_ftm_mad_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.SurfXiliTurbHcTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmMadTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmSigTab;
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
 * @date ：Created in 2019/10/23 0023 9:09
 * @description：中国高空规定等压面定时值数据入库类
 * @modified By：
 * @version: 1.0$
 */
public class ChnMulFtmMadTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnMulFtmMadTabService.diQueues = diQueues;
    }

    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataSingle(PreparedStatement ps, List<UparChnMulFtmMadTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<UparChnMulFtmMadTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (UparChnMulFtmMadTab uparChnMulFtmMadTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(uparChnMulFtmMadTab);
                try {
                    ps.setString(ii++, uparChnMulFtmMadTab.getdRecordId());
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, uparChnMulFtmMadTab.getdDatetime());
                    ps.setShort(ii++,uparChnMulFtmMadTab.getV04001());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getV04002());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getV04003());
                    ps.setString(ii++, uparChnMulFtmMadTab.getV01301());
                    ps.setInt(ii++, uparChnMulFtmMadTab.getV01300());
                    ps.setBigDecimal(ii++,uparChnMulFtmMadTab.getvTimeLevel());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV10004());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV10009());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV12001());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV12003());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV11001());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV11002());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getQ10009());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getQ12001());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getQ12003());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getQ11001());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getQ11002());
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
                        data=uparChnMulFtmMadTab.getV01301()+" "+uparChnMulFtmMadTab.getdDatetime()+"  "+uparChnMulFtmMadTab.getV10004();
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
     * 中国高空等压面定时值数据集
     *
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(ParseResult<UparChnMulFtmMadTab> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        List<UparChnMulFtmMadTab> pollenLists = parseResult.getData();
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
    private static void insertDB(List<UparChnMulFtmMadTab> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("R017_value_table_name") + "("
                    + "D_RETAIN_ID,D_DATA_ID, D_DATETIME, V04001, \n" +
                    "      V04002,V04003,V01301, V01300, \n" +
                    "      TIMES, V10004, V10009, \n" +
                    "      V12001, V12003, V11001, \n" +
                    "      V11002, Q10009, Q12001, \n" +
                    "      Q12003, Q11001, Q11002) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;
            List<UparChnMulFtmMadTab> list=new ArrayList();
            for (UparChnMulFtmMadTab uparChnMulFtmMadTab : parseResult) {
                int ii = 1;
                index += 1;
                list.add(uparChnMulFtmMadTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, uparChnMulFtmMadTab.getdRecordId());
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, uparChnMulFtmMadTab.getdDatetime());
                    ps.setShort(ii++,uparChnMulFtmMadTab.getV04001());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getV04002());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getV04003());
                    ps.setString(ii++, uparChnMulFtmMadTab.getV01301());
                    ps.setInt(ii++, uparChnMulFtmMadTab.getV01300());
                    ps.setBigDecimal(ii++,uparChnMulFtmMadTab.getvTimeLevel());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV10004());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV10009());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV12001());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV12003());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV11001());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getV11002());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getQ10009());
                    ps.setBigDecimal(ii++, uparChnMulFtmMadTab.getQ12001());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getQ12003());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getQ11001());
                    ps.setShort(ii++, uparChnMulFtmMadTab.getQ11002());
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
