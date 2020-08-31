package cma.cimiss2.dpc.indb.radi.dc_radi_chn_mut_mten_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMtenTab;
import cma.cimiss2.dpc.decoder.bean.radi.RadiChnMutMyerTab;
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
 * @date ：Created in 2019/10/28 0028 16:38
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class ChnMutMtenTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnMutMtenTabService.diQueues = diQueues;
    }

    public static List<RadiChnMutMtenTab> deleteRepeatData(List<RadiChnMutMtenTab> pollenLists, Connection connection){
        Iterator<RadiChnMutMtenTab> iterable = pollenLists.iterator();
        while (iterable.hasNext()){
            RadiChnMutMtenTab s = iterable.next();
            String V01300=s.getV01301();
            Short V04002=s.getV04002();
            Short V04290=s.getV04290();
            String sql = "select count(V01301) from "+ StartConfig.valueTable("R013_value_table_name")+" where V01301 = '"+V01300+"' and V04002="+V04002+" and V04290="+V04290;
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
    public static DataBaseAction processSuccessReport(ParseResult<RadiChnMutMtenTab> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        List<RadiChnMutMtenTab> pollenLists = parseResult.getData();
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
    private static void insertDB(List<RadiChnMutMtenTab> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("R013_value_table_name") + "("
                    + "D_RETAIN_ID,D_DATA_ID, V01301, V05001, \n" +
                    "      V06001, V07001, V04002, \n" +
                    "      V04290, V14320, V14308, \n" +
                    "      V14309, V14302, V14306, \n" +
                    "      V143_st, V14027, V14320_1, \n" +
                    "      V14320_stime, V14320_etime, V14308_1, \n" +
                    "      V14308_stime, V14308_etime, V143_st_1, \n" +
                    "      V143_st_stime, V143_st_etim) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;

            for (RadiChnMutMtenTab radiChnMutMtenTab : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, radiChnMutMtenTab.getdRecordId());
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, radiChnMutMtenTab.getV01301());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV05001());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV06001());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV07001());
                    ps.setShort(ii++, radiChnMutMtenTab.getV04002());
                    ps.setShort(ii++, radiChnMutMtenTab.getV04290());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV14320());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV14308());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV14309());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV14302());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV14306());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV143St());
                    ps.setBigDecimal(ii++, radiChnMutMtenTab.getV14027());
                    ps.setInt(ii++, radiChnMutMtenTab.getV143201());
                    ps.setInt(ii++, radiChnMutMtenTab.getV14320Stime());
                    ps.setInt(ii++, radiChnMutMtenTab.getV14320Etime());
                    ps.setInt(ii++, radiChnMutMtenTab.getV143081());
                    ps.setInt(ii++, radiChnMutMtenTab.getV14308Stime());
                    ps.setInt(ii++, radiChnMutMtenTab.getV14308Etime());
                    ps.setInt(ii++, radiChnMutMtenTab.getV143St1());
                    ps.setInt(ii++, radiChnMutMtenTab.getV143StStime());
                    ps.setInt(ii++, radiChnMutMtenTab.getV143StEtim());
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
