package cma.cimiss2.dpc.indb.radi.radi_chn_mut_yer_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutTenTab;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutYerTab;
import cma.cimiss2.dpc.decoder.bean.upar.UparChnMulFtmMadTab;
import cma.cimiss2.dpc.decoder.fileDecode.common.bean.HourValueTab;
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

public class ChnMutYerTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");

    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnMutYerTabService.diQueues = diQueues;
    }
    /**
     * 执行错误时，可能数据冲突，分批执行
     * @param ps
     * @param cts_code
     */
    public static void excuteByDataSingle(PreparedStatement ps, List<RadiChnMutYerTab> list, String cts_code, Connection connection)  {
        int size=list.size();
        int middle=size/2;
        List<RadiChnMutYerTab> list1=new ArrayList<>();
        try {
            int indexSingle=0;
            for (RadiChnMutYerTab radiChnMutYerTab : list) {
                int ii = 1;
                indexSingle+=1;
                list1.add(radiChnMutYerTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, radiChnMutYerTab.getdRecordId());
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, radiChnMutYerTab.getV01301());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV05001());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV06001());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV07001());
                    ps.setShort(ii++, radiChnMutYerTab.getV04001());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14320());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14308());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14309());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14302());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14306());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV143St());
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
                        data=radiChnMutYerTab.getV01301()+" "+radiChnMutYerTab.getV04001();
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
    public static DataBaseAction processSuccessReport(ParseResult<RadiChnMutYerTab> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        List<RadiChnMutYerTab> pollenLists = parseResult.getData();
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
    private static void insertDB(List<RadiChnMutYerTab> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("R007_value_table_name") + "("
                    + "D_RETAIN_ID,D_DATA_ID, V01301, V05001, \n" +
                    "      V06001, V07001, V04001, \n" +
                    "      V14320, V14308, V14309, \n" +
                    "      V14302, V14306, V143_st) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;
            List<RadiChnMutYerTab> list=new ArrayList();
            for (RadiChnMutYerTab radiChnMutYerTab : parseResult) {
                int ii = 1;
                index += 1;
                list.add(radiChnMutYerTab);
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, radiChnMutYerTab.getdRecordId());
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, radiChnMutYerTab.getV01301());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV05001());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV06001());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV07001());
                    ps.setShort(ii++, radiChnMutYerTab.getV04001());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14320());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14308());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14309());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14302());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV14306());
                    ps.setBigDecimal(ii++, radiChnMutYerTab.getV143St());
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
