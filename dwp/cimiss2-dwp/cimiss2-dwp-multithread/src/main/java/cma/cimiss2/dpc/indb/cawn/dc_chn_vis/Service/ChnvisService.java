package cma.cimiss2.dpc.indb.cawn.dc_chn_vis.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.cawn.Chnvis;
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

public class ChnvisService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnvisService.diQueues = diQueues;
    }


    public static DataBaseAction processSuccessReport(ParseResult<Chnvis> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileName) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Chnvis> cliCsv = parseResult.getData();
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


    private static void insert_db(List<Chnvis> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable() + "("
                    + "D_RECORD_ID, V01301, V06001, V05001, V07001, V04001, V04002, V04003, VISIBITLITY02, VISIBITLITY08, VISIBITLITY14,VISIBITLITY20,Q_VISIBITLITY02,Q_VISIBITLITY08,Q_VISIBITLITY14,Q_VISIBITLITY20)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnvis chnvis : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnvis.getDRecordId());
                    ps.setString(ii++, chnvis.getV01301());
                    ps.setBigDecimal(ii++, chnvis.getV06001());
                    ps.setBigDecimal(ii++, chnvis.getV05001());
                    ps.setBigDecimal(ii++, chnvis.getV07001());
                    ps.setShort(ii++, chnvis.getV04001());
                    ps.setShort(ii++, chnvis.getV04002());
                    ps.setShort(ii++, chnvis.getV04003());
                    ps.setInt(ii++, chnvis.getVisibitlity02());
                    ps.setInt(ii++, chnvis.getVisibitlity08());
                    ps.setInt(ii++, chnvis.getVisibitlity14());
                    ps.setInt(ii++, chnvis.getVisibitlity20());
                    ps.setInt(ii++, chnvis.getQVisibitlity02());
                    ps.setInt(ii++, chnvis.getQVisibitlity08());
                    ps.setString(ii++, chnvis.getQVisibitlity14());
                    ps.setString(ii++, chnvis.getQVisibitlity20());
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
