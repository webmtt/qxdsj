package cma.cimiss2.dpc.indb.agme.dc_agme_soil_chn.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.Soilchn;
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

public class SoilchnService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        SoilchnService.diQueues = diQueues;
    }

    public static DataBaseAction processSuccessReport(ParseResult<Soilchn> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
        List<Soilchn> pollenLists = parseResult.getData();
        insertDB(pollenLists, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }


    private static void insertDB(List<Soilchn> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S022_value_table_name") + "("
                    + "D_RECORD_ID, D_IYMDHM, CNAME, D_RYMDHM, D_DATETIME, V71655_010, V71655_020, V71655_030, V71655_040, V71655_050, V04001, V04002,V04003) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Soilchn soilchn : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++,soilchn.getDRecordId());
                    ps.setTimestamp(ii++,soilchn.getDIymdhm());
                    ps.setString(ii++,soilchn.getCname());
                    ps.setTimestamp(ii++,soilchn.getDRymdhm());
                    ps.setTimestamp(ii++, soilchn.getDDatetime());
                    ps.setBigDecimal(ii++, soilchn.getV71655010());
                    ps.setBigDecimal(ii++, soilchn.getV71655020());
                    ps.setBigDecimal(ii++, soilchn.getV71655030());
                    ps.setBigDecimal(ii++, soilchn.getV71655040());
                    ps.setBigDecimal(ii++, soilchn.getV71655050());
                    ps.setShort(ii++, soilchn.getV04001());
                    ps.setShort(ii++, soilchn.getV04002());
                    ps.setShort(ii++, soilchn.getV04003());
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
