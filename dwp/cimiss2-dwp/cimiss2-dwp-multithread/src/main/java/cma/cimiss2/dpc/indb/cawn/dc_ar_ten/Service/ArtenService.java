package cma.cimiss2.dpc.indb.cawn.dc_ar_ten.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.cawn.Arten;
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

public class ArtenService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ArtenService.diQueues = diQueues;
    }


    public static DataBaseAction processSuccessReport(ParseResult<Arten> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileName) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
            List<Arten> cliCsv = parseResult.getData();
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


    private static void insert_db(List<Arten> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S035_value_table_name") + "("
                    + "D_RECORD_ID, V04001, V04002, V04003, V13011, V15532, V22381, Na, K, Mg, Ca,Cl,NH4,NO3,SO4,F)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Arten arten : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++,arten.getDRecordId());
                    ps.setInt(ii++,arten.getV04001());
                    ps.setShort(ii++,arten.getV04002());
                    ps.setShort(ii++,arten.getV04003());
                    ps.setBigDecimal(ii++,arten.getV13011());
                    ps.setBigDecimal(ii++,arten.getV15532());
                    ps.setBigDecimal(ii++,arten.getV22381());
                    ps.setBigDecimal(ii++,arten.getNa());
                    ps.setBigDecimal(ii++,arten.getK());
                    ps.setBigDecimal(ii++,arten.getMg());
                    ps.setBigDecimal(ii++,arten.getCa());
                    ps.setBigDecimal(ii++,arten.getCl());
                    ps.setBigDecimal(ii++,arten.getNh4());
                    ps.setBigDecimal(ii++,arten.getNo3());
                    ps.setBigDecimal(ii++,arten.getSo4());
                    ps.setBigDecimal(ii++,arten.getF());
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
