package cma.cimiss2.dpc.indb.cawn.dc_chn_dst.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.cawn.ChnDst;
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

public class ChnDstService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnDstService.diQueues = diQueues;
    }


    public static DataBaseAction processSuccessReport(ParseResult<ChnDst> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileName) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<ChnDst> cliCsv = parseResult.getData();
            if(fileName.contains("DSS")){
                insert_db_dss(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
            if(fileName.contains("VIS")){
                insert_db_vis(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
            if(fileName.contains("Wmax")){
                insert_db_wmax(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
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


    private static void insert_db_dss(List<ChnDst> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable() + "("
                    + "D_RECORD_ID, V01300, V06001, V05001, V07001, V04001, V04002, V04003, V20302_031, V20302_007, V20302_006,Q20302_031,Q20302_007,Q20302_006)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (ChnDst chnDst : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnDst.getDRecordId());
                    ps.setInt(ii++, chnDst.getV01300());
                    ps.setBigDecimal(ii++, chnDst.getV06001());
                    ps.setBigDecimal(ii++, chnDst.getV05001());
                    ps.setBigDecimal(ii++, chnDst.getV07001());
                    ps.setShort(ii++, chnDst.getV04001());
                    ps.setShort(ii++, chnDst.getV04002());
                    ps.setShort(ii++, chnDst.getV04003());
                    ps.setInt(ii++, chnDst.getV20302031());
                    ps.setInt(ii++, chnDst.getV20302007());
                    ps.setInt(ii++, chnDst.getV20302006());
                    ps.setShort(ii++, chnDst.getQ20302031());
                    ps.setShort(ii++, chnDst.getQ20302007());
                    ps.setShort(ii++, chnDst.getQ20302006());
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

    private static void insert_db_vis(List<ChnDst> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable() + "("
                    + "D_RECORD_ID, V01300, V06001, V05001, V07001, V04001, V04002, V04003, V05002, V06008, V07014,V02001,Q05001,Q06001,Q07001,Q02001)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (ChnDst chnDst : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnDst.getDRecordId());
                    ps.setInt(ii++, chnDst.getV01300());
                    ps.setBigDecimal(ii++, chnDst.getV06001());
                    ps.setBigDecimal(ii++, chnDst.getV05001());
                    ps.setBigDecimal(ii++, chnDst.getV07001());
                    ps.setShort(ii++, chnDst.getV04001());
                    ps.setShort(ii++, chnDst.getV04002());
                    ps.setShort(ii++, chnDst.getV04003());
                    ps.setBigDecimal(ii++, chnDst.getV05002());
                    ps.setBigDecimal(ii++, chnDst.getV06008());
                    ps.setBigDecimal(ii++, chnDst.getV07014());
                    ps.setInt(ii++, chnDst.getV02001());
                    ps.setShort(ii++, chnDst.getQ05001());
                    ps.setShort(ii++, chnDst.getQ06001());
                    ps.setShort(ii++, chnDst.getQ07001());
                    ps.setShort(ii++, chnDst.getQ02001());
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

    private static void insert_db_wmax(List<ChnDst> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable() + "("
                    + "D_RECORD_ID, V01300, V06001, V05001, V07001, V04001, V04002, V04003, V11042, V11296, V11046,V11211,Q11042,Q11296,Q11046,Q11211)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (ChnDst chnDst : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnDst.getDRecordId());
                    ps.setInt(ii++, chnDst.getV01300());
                    ps.setBigDecimal(ii++, chnDst.getV06001());
                    ps.setBigDecimal(ii++, chnDst.getV05001());
                    ps.setBigDecimal(ii++, chnDst.getV07001());
                    ps.setShort(ii++, chnDst.getV04001());
                    ps.setShort(ii++, chnDst.getV04002());
                    ps.setShort(ii++, chnDst.getV04003());
                    ps.setBigDecimal(ii++, chnDst.getV11042());
                    ps.setBigDecimal(ii++, chnDst.getV11296());
                    ps.setBigDecimal(ii++, chnDst.getV11046());
                    ps.setBigDecimal(ii++, chnDst.getV11211());
                    ps.setShort(ii++, chnDst.getQ11042());
                    ps.setShort(ii++, chnDst.getQ11296());
                    ps.setShort(ii++, chnDst.getQ11046());
                    ps.setShort(ii++, chnDst.getQ11211());
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
