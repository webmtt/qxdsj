package cma.cimiss2.dpc.indb.cawn.dc_chn_win.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.cawn.Chnwin;
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

public class ChnwinService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnwinService.diQueues = diQueues;
    }


    public static DataBaseAction processSuccessReport(ParseResult<Chnwin> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileName) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Chnwin> cliCsv = parseResult.getData();
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


    private static void insert_db(List<Chnwin> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable() + "("
                    + "D_RECORD_ID, V01301, V06001, V05001, V07001, V04001, V04002, V04003, V11001_02, V11002_02, V11001_08,V11002_08," +
                    "V11001_14,V11002_14,V11001_20,V11002_20,Q11001_02,Q11002_02,Q11001_08,Q11002_08,Q11001_14,Q11002_14,Q11001_20,Q11002_20)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnwin chnwin : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnwin.getDRecordId());
                    ps.setString(ii++, chnwin.getV01301());
                    ps.setBigDecimal(ii++, chnwin.getV06001());
                    ps.setBigDecimal(ii++, chnwin.getV05001());
                    ps.setBigDecimal(ii++, chnwin.getV07001());
                    ps.setShort(ii++, chnwin.getV04001());
                    ps.setShort(ii++, chnwin.getV04002());
                    ps.setShort(ii++, chnwin.getV04003());
                    ps.setBigDecimal(ii++, chnwin.getV1100102());
                    ps.setBigDecimal(ii++, chnwin.getV1100202());
                    ps.setBigDecimal(ii++, chnwin.getV1100108());
                    ps.setBigDecimal(ii++, chnwin.getV1100208());
                    ps.setBigDecimal(ii++, chnwin.getV1100114());
                    ps.setBigDecimal(ii++, chnwin.getV1100214());
                    ps.setBigDecimal(ii++, chnwin.getV1100120());
                    ps.setBigDecimal(ii++, chnwin.getV1100220());
                    ps.setBigDecimal(ii++, chnwin.getQ1100102());
                    ps.setBigDecimal(ii++, chnwin.getQ1100202());
                    ps.setBigDecimal(ii++, chnwin.getQ1100108());
                    ps.setBigDecimal(ii++, chnwin.getQ1100208());
                    ps.setBigDecimal(ii++, chnwin.getQ1100114());
                    ps.setBigDecimal(ii++, chnwin.getQ1100214());
                    ps.setBigDecimal(ii++, chnwin.getQ1100120());
                    ps.setBigDecimal(ii++, chnwin.getQ1100220());
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
