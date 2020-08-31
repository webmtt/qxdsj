package cma.cimiss2.dpc.indb.radi.dc_radi_chn_mut_mmon_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMmonTab;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMpenTab;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/29 0029 10:06
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class ChnMutMmonTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnMutMmonTabService.diQueues = diQueues;
    }

    public static List<RadiChnMutMmonTab> deleteRepeatData(List<RadiChnMutMmonTab> pollenLists, Connection connection){
        Iterator<RadiChnMutMmonTab> iterable = pollenLists.iterator();
        while (iterable.hasNext()){
            RadiChnMutMmonTab s = iterable.next();
            String V01300=s.getV01301();
            Short V04002=s.getV04002();
            String sql = "select count(V01301) from "+ StartConfig.valueTable("R015_value_table_name")+" where V01301 = '"+V01300+"' and V04002="+V04002;
            try {
                Statement sta = connection.createStatement();
                ResultSet rs = sta.executeQuery(sql);
                int count = 0;
                while (rs.next()) {
                    count = rs.getInt(1);
                }
                if(count != 0) {
                    //有同样的记录
                    iterable.remove();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return pollenLists;
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
    public static DataBaseAction processSuccessReport(ParseResult<RadiChnMutMmonTab> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        List<RadiChnMutMmonTab> pollenLists = parseResult.getData();
        if(pollenLists!=null) {
            //删除重复数据
            pollenLists = deleteRepeatData(pollenLists, connection);
        }
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
    private static void insertDB(List<RadiChnMutMmonTab> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("R015_value_table_name") + "("
                    + "D_RETAIN_ID,D_DATA_ID, V01301, V05001, \n" +
                    "      V06001, V07001, V04002, \n" +
                    "       V14320, V14308, \n" +
                    "      V14309, V14302, V14306, \n" +
                    "      V143_st, V14027, V14320_1, \n" +
                    "      V14320_stime, V14320_etime, V14308_1, \n" +
                    "      V14308_stime, V14308_etime, V143_st_1, \n" +
                    "      V143_st_stime, V143_st_etim) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;

            for (RadiChnMutMmonTab radiChnMutMmonTab : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, radiChnMutMmonTab.getdRecordId());
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, radiChnMutMmonTab.getV01301());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV05001());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV06001());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV07001());
                    ps.setShort(ii++, radiChnMutMmonTab.getV04002());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV14320());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV14308());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV14309());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV14302());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV14306());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV143St());
                    ps.setBigDecimal(ii++, radiChnMutMmonTab.getV14027());
                    ps.setInt(ii++, radiChnMutMmonTab.getV143201());
                    ps.setInt(ii++, radiChnMutMmonTab.getV14320Stime());
                    ps.setInt(ii++, radiChnMutMmonTab.getV14320Etime());
                    ps.setInt(ii++, radiChnMutMmonTab.getV143081());
                    ps.setInt(ii++, radiChnMutMmonTab.getV14308Stime());
                    ps.setInt(ii++, radiChnMutMmonTab.getV14308Etime());
                    ps.setInt(ii++, radiChnMutMmonTab.getV143St1());
                    ps.setInt(ii++, radiChnMutMmonTab.getV143StStime());
                    ps.setInt(ii++, radiChnMutMmonTab.getV143StEtim());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("列号：" + ii + "有误");
                }
                ps.addBatch();
                if (index % 1000 == 0) {
                    ps.executeBatch();
                    connection.commit();
                    ps.clearBatch();
                }
            }
            if (index % 1000 != 0) {
                ps.executeBatch();
                connection.commit();
                ps.clearBatch();
            }
            try {
                ps.executeBatch();
                connection.commit();
                loggerBuffer.append("\n Batch submission successful：" + filename);

            } catch (Exception e) {
                e.printStackTrace();
                connection.rollback();
                loggerBuffer.append("\n Batch submission error" + filename);
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
