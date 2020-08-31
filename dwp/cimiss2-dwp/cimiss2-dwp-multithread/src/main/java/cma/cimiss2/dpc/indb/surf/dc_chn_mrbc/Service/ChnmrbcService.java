package cma.cimiss2.dpc.indb.surf.dc_chn_mrbc.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Chnmrbc;
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

public class ChnmrbcService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    static String type = StartConfig.sodCode();

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnmrbcService.diQueues = diQueues;
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
    public static DataBaseAction processSuccessReport(ParseResult<Chnmrbc> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Chnmrbc> cliCsv = parseResult.getData();
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
    private static void insert_db(List<Chnmrbc> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S013_value_table_name") + "("
                    + "D_RECORD_ID, D_DATA_ID, D_IYMDHM, D_DATETIME, V04001, V04002, V01301,V01300, V05001, V06001, V07001, V02001,V02301,V_ACODE,V07031,V07032_04," +
                    "ADDRESS,ARCHIVESNUM,GEOENVIRONMENT,STATIONNAME,INPUTMES,PROOFREADING,PRETRIAL,TRANSMISSION,TRANSMISSIONTIME)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnmrbc chnmrbc : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnmrbc.getDRecordId());
                    ps.setString(ii++, StartConfig.ctsCode("S013_cts_code"));
                    ps.setTimestamp(ii++, chnmrbc.getDIymdhm());
                    ps.setTimestamp(ii++, chnmrbc.getDDatetime());
                    ps.setInt(ii++, chnmrbc.getV04001());
                    ps.setInt(ii++, chnmrbc.getV04002());
                    ps.setString(ii++, chnmrbc.getV01301());
                    ps.setInt(ii++, chnmrbc.getV01300());

                    ps.setDouble(ii++, chnmrbc.getV05001());
                    ps.setDouble(ii++, chnmrbc.getV06001());
                    ps.setDouble(ii++, chnmrbc.getV07001());
                    ps.setInt(ii++, chnmrbc.getV02001());
                    ps.setInt(ii++, chnmrbc.getV02301());
                    ps.setString(ii++, chnmrbc.getVAcode());
                    ps.setDouble(ii++, chnmrbc.getV07031());

                    ps.setDouble(ii++, chnmrbc.getV0703204());
                    ps.setString(ii++, chnmrbc.getAddress());
                    ps.setString(ii++, chnmrbc.getArchivesnum());
                    ps.setString(ii++, chnmrbc.getGeoenvironment());
                    ps.setString(ii++, chnmrbc.getStationname());
                    ps.setString(ii++, chnmrbc.getInputmes());
                    ps.setString(ii++, chnmrbc.getProofreading());

                    ps.setString(ii++, chnmrbc.getPretrial());
                    ps.setString(ii++, chnmrbc.getTransmission());
                    ps.setTimestamp(ii++, chnmrbc.getTransmissiontime());
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
