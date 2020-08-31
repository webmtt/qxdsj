package cma.cimiss2.dpc.indb.cawn.dc_env_share.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.cawn.EnvShare;
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

public class EnvShareService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    static String type = StartConfig.sodCode();

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        EnvShareService.diQueues = diQueues;
    }



    public static DataBaseAction processSuccessReport(ParseResult<EnvShare> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<EnvShare> cliCsv = parseResult.getData();
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


    private static void insert_db(List<EnvShare> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S032_value_table_name") + "("
                    + "D_RECORD_ID,D_DATA_ID,CITYNAME,CITYCODE,STATIONNAME,V01301,STATIONATTRIBUTE,V06001,V05001,V_ACODE,V04001,V04002,V04003,V04004," +
                    "SO2,NO,NO2,NOX,CO,O3,PM10,PM25,V11202,V11201,V12001,V10004,V13003," +
                    "V13019,REMARK,D_DATETIME)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (EnvShare envShare : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++,envShare.getDRecordId());
                    ps.setString(ii++,StartConfig.ctsCode("S032_cts_code"));
                    ps.setString(ii++,envShare.getCityname());
                    ps.setString(ii++,envShare.getCitycode());
                    ps.setString(ii++,envShare.getStationname());
                    ps.setString(ii++,envShare.getV01301());
                    ps.setString(ii++,envShare.getStationattribute());
                    ps.setBigDecimal(ii++,envShare.getV06001());
                    ps.setBigDecimal(ii++,envShare.getV05001());
                    ps.setString(ii++,envShare.getVAcode());
                    ps.setShort(ii++,envShare.getV04001());
                    ps.setShort(ii++,envShare.getV04002());
                    ps.setShort(ii++,envShare.getV04003());
                    ps.setShort(ii++,envShare.getV04004());
                    ps.setString(ii++,envShare.getSo2());
                    ps.setInt(ii++,envShare.getNo());
                    ps.setBigDecimal(ii++,envShare.getNo2());
                    ps.setBigDecimal(ii++,envShare.getNox());
                    ps.setBigDecimal(ii++,envShare.getCo());
                    ps.setInt(ii++,envShare.getO3());
                    ps.setInt(ii++,envShare.getPm10());
                    ps.setString(ii++,envShare.getPm25());
                    ps.setBigDecimal(ii++,envShare.getV11202());
                    ps.setBigDecimal(ii++,envShare.getV11201());
                    ps.setBigDecimal(ii++,envShare.getV12001());
                    ps.setBigDecimal(ii++,envShare.getV10004());
                    ps.setBigDecimal(ii++,envShare.getV13003());
                    ps.setBigDecimal(ii++,envShare.getV13019());
                    ps.setString(ii++,envShare.getRemark());
                    ps.setTimestamp(ii++,envShare.getDDatetime());
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
