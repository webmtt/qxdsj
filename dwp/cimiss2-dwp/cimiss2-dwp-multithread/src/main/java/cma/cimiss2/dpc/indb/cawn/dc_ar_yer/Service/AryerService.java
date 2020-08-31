package cma.cimiss2.dpc.indb.cawn.dc_ar_yer.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.cawn.Aryer;
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

public class AryerService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        AryerService.diQueues = diQueues;
    }


    public static DataBaseAction processSuccessReport(ParseResult<Aryer> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileName) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Aryer> cliCsv = parseResult.getData();
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


    private static void insert_db(List<Aryer> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S036_value_table_name") + "("
                    + "D_RECORD_ID, CNAME, V01300, V04002, V13011_YEAR, TOTAL_V13011_NUm, AVG_YEAR_V15532, MAX_YEAR_V15532, MIN_YEAR_V15532, V15532_RATE_45, V15532_RATE_56,V15532_RAIN,V15532_NUM,V22381_NUM," +
                    "AVG_YEAR_V22381,MAX_YEAR_V22381,MIN_YEAR_V22381)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Aryer aryer : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++,aryer.getDRecordId());
                    ps.setString(ii++,aryer.getCname());
                    ps.setInt(ii++,aryer.getV01300());
                    ps.setString(ii++,aryer.getV04002());
                    ps.setBigDecimal(ii++,aryer.getV13011Year());
                    ps.setInt(ii++,aryer.getTotalV13011Num());
                    ps.setBigDecimal(ii++,aryer.getAvgYearV15532());
                    ps.setBigDecimal(ii++,aryer.getMaxYearV15532());
                    ps.setBigDecimal(ii++,aryer.getMinYearV15532());
                    ps.setBigDecimal(ii++,aryer.getV15532Rate45());
                    ps.setBigDecimal(ii++,aryer.getV15532Rate56());
                    ps.setBigDecimal(ii++,aryer.getV15532Rain());
                    ps.setInt(ii++,aryer.getV15532Num());
                    ps.setInt(ii++,aryer.getV22381Num());
                    ps.setBigDecimal(ii++,aryer.getAvgYearV22381());
                    ps.setBigDecimal(ii++,aryer.getMaxYearV22381());
                    ps.setBigDecimal(ii++,aryer.getMinYearV22381());
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
