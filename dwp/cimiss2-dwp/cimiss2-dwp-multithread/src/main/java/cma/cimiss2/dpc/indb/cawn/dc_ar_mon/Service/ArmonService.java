package cma.cimiss2.dpc.indb.cawn.dc_ar_mon.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.cawn.Armon;
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

public class ArmonService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ArmonService.diQueues = diQueues;
    }


    public static DataBaseAction processSuccessReport(ParseResult<Armon> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileName) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Armon> cliCsv = parseResult.getData();
            insert_db(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
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


    private static void insert_db(List<Armon> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S034_value_table_name") + "("
                    + "D_RECORD_ID, CNAME, V01300, V04002, TOTAL_V13011, TOTAL_V13011_NUm, AVG_MONTHH_V15532, MAX_V15532, MIN_V15532, V15532_NUM_45, V15532_RATE_45,V15532_NUM_56,V15532_RATE_56,V15532_NUM_70," +
                    "V15532_RATE_10,V15532_NUM_M70,V15532_RATE_M70,V15532_FORECATS_RATE,AVG_MONTH_V22381,MAX_V22381,MIN_V22381,V22381_RATE," +
                    "V15532_NUM_40,V15532_RATE_40,V15532_RAIN,V22381_RAIN,V15532_NUM,V22381_NUM)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Armon armon : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++,armon.getDRecordId());
                    ps.setString(ii++,armon.getCname());
                    ps.setInt(ii++,armon.getV01300());
                    ps.setInt(ii++,armon.getV04002());
                    ps.setBigDecimal(ii++,armon.getTotalV13011());
                    ps.setBigDecimal(ii++,armon.getTotalV13011Num());
                    ps.setBigDecimal(ii++,armon.getAvgMonthhV15532());
                    ps.setBigDecimal(ii++,armon.getMaxV15532());
                    ps.setBigDecimal(ii++,armon.getMinV15532());
                    ps.setBigDecimal(ii++,armon.getV15532Num45());
                    ps.setBigDecimal(ii++,armon.getV15532Rate45());
                    ps.setBigDecimal(ii++,armon.getV15532Num56());
                    ps.setBigDecimal(ii++,armon.getV15532Rate56());
                    ps.setBigDecimal(ii++,armon.getV15532Num70());
                    ps.setBigDecimal(ii++,armon.getV15532Rate10());
                    ps.setBigDecimal(ii++,armon.getV15532NumM70());
                    ps.setBigDecimal(ii++,armon.getV15532RateM70());
                    ps.setBigDecimal(ii++,armon.getV15532ForecatsRate());
                    ps.setBigDecimal(ii++,armon.getAvgMonthV22381());
                    ps.setBigDecimal(ii++,armon.getMaxV22381());
                    ps.setBigDecimal(ii++,armon.getMinV22381());
                    ps.setBigDecimal(ii++,armon.getV22381Rate());
                    ps.setBigDecimal(ii++,armon.getV15532Num40());
                    ps.setBigDecimal(ii++,armon.getV15532Rate40());
                    ps.setBigDecimal(ii++,armon.getV15532Rain());
                    ps.setBigDecimal(ii++,armon.getV22381Rain());
                    ps.setInt(ii++,armon.getV15532Num());
                    ps.setInt(ii++,armon.getV22381Num());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("列号：" + ii + "有误");
                }
                ps.addBatch();
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
