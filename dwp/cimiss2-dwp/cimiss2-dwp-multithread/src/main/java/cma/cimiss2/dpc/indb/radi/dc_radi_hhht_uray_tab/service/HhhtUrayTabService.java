package cma.cimiss2.dpc.indb.radi.dc_radi_hhht_uray_tab.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.radi.RadiHhhtUrayTab;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author ：YCK
 * @date ：Created in 2019/10/24 0024 14:31
 * @description：
 * @modified By：
 * @version: 1.0$
 */
public class HhhtUrayTabService {
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        HhhtUrayTabService.diQueues = diQueues;
    }


    /**
     *
     * @Title: processSuccessReport
     * @Description:(3830飞机云气溶胶粒子探头CAS观测资料)
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param fileN
     * @return DataBaseAction
     * @throws：
     */
    public static DataBaseAction processSuccessReport(ParseResult<RadiHhhtUrayTab> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
            List<RadiHhhtUrayTab> cliCsv = parseResult.getData();
            insert_db(cliCsv, connection, recv_time, loggerBuffer,fileN,cts_code);
            return DataBaseAction.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            loggerBuffer.append("\n Database connection error");
            return DataBaseAction.CONNECTION_ERROR;
        }
        finally{
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    loggerBuffer.append("\n Database connection close error"+e.getMessage());
                }
            }

        }
    }

    /**
     * @param cts_code
     * @param loggerBuffer
     * @param cts_code
     *
     * @Title: insert_db
     * @Description:(3830飞机云气溶胶粒子探头CAS观测资料)
     * @param: @param list 数据结合
     * @param: @param recv_time  消息接收时间
     * @param: @param is_Batch  是否Batch提交
     * @param: @return
     * @return: DataBaseAction  返回处理状态
     * @throws
     */
    private static void insert_db(List<RadiHhhtUrayTab> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("R010_value_table_name") + "("
                    + "D_RETAIN_ID,D_DATA_ID, D_IYMDHM, D_DATETIME, \n" +
                    "      V04001, V04002, V04003, \n" +
                    "      V04004, V04005, V01301, \n" +
                    "      V01300, UVA, UVB, CMA_UVI, \n" +
                    "      INL_UVI)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            int index = 0;
            for (RadiHhhtUrayTab radiHhhtUrayTab : parseResult) {
                index += 1;
                int ii = 1;
                try {
                    ps.setString(ii++, radiHhhtUrayTab.getdRetainId());
                    ps.setString(ii++, cts_code);
                    ps.setString(ii++, radiHhhtUrayTab.getdIymdhm());
                    ps.setString(ii++, radiHhhtUrayTab.getdDatetime());
                    ps.setShort(ii++,radiHhhtUrayTab.getV04001());
                    ps.setShort(ii++,radiHhhtUrayTab.getV04002());
                    ps.setShort(ii++,radiHhhtUrayTab.getV04003());
                    ps.setShort(ii++,radiHhhtUrayTab.getV04004());
                    ps.setShort(ii++,radiHhhtUrayTab.getV04005());
                    ps.setString(ii++,radiHhhtUrayTab.getV01301());
                    ps.setInt(ii++,radiHhhtUrayTab.getV01300());
                    ps.setString(ii++,radiHhhtUrayTab.getUva());
                    ps.setString(ii++,radiHhhtUrayTab.getUvb());
                    ps.setString(ii++,radiHhhtUrayTab.getCmaUvi());
                    ps.setString(ii++,radiHhhtUrayTab.getInlUvi());
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
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
