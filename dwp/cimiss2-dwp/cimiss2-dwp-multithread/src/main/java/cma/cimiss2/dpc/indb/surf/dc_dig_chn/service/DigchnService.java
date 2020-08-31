package cma.cimiss2.dpc.indb.surf.dc_dig_chn.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Digchn;
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
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class DigchnService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    static String type = StartConfig.sodCode();

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        DigchnService.diQueues = diQueues;
    }


    /**
     *
     * @Title: processSuccessReport
     * @Description:数字化-15时段最大降水
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param fileN
     * @return DataBaseAction
     * @throws：
     */
    public static DataBaseAction processSuccessReport(ParseResult<Digchn> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Digchn> cliCsv = parseResult.getData();
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
     * @Description:数字化-15时段最大降水
     * @param: @param list 数据结合
     * @param: @param recv_time  消息接收时间
     * @param: @param is_Batch  是否Batch提交
     * @param: @return
     * @return: DataBaseAction  返回处理状态
     * @throws
     */
    private static void insert_db(List<Digchn> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S009_value_table_name") + "("
                    + "D_RECORD_ID, V01301, V01300, D_DATETIME, V04001, RMAX_05MINUTES, TIME_RMAX_05MINUTES, RMAX_10MINUTES, TIME_RMAX_10MINUTES, RMAX_15MINUTES, TIME_RMAX_15MINUTES,RMAX_20MINUTES,TIME_RMAX_20MINUTES," +
                    "RMAX_30MINUTES,TIME_RMAX_30MINUTES,RMAX_45MINUTES,TIME_RMAX_45MINUTES,RMAX_60MINUTES,TIME_RMAX_60MINUTES,RMAX_90MINUTES,TIME_RMAX_90MINUTES," +
                    "RMAX_120MINUTES,TIME_RMAX_120MINUTES,RMAX_180MINUTES,TIME_RMAX_180MINUTES,RMAX_240MINUTES,TIME_RMAX_240MINUTES,RMAX_360MINUTES,TIME_RMAX_360MINUTES," +
                    "RMAX_540MINUTES,TIME_RMAX_540MINUTES,RMAX_720MINUTES,TIME_RMAX_720MINUTES,RMAX_1440MINUTES,TIME_RMAX_1440MINUTES)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);

            int index = 0;
            for (Digchn digchn : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    String D_RECORD_ID = UUID.randomUUID().toString().replace("-","");
                    ps.setString(ii++, D_RECORD_ID);
                    ps.setString(ii++, digchn.getV01301());
                    ps.setInt(ii++, digchn.getV01300());
                    ps.setDate(ii++, new java.sql.Date(digchn.getDDatetime().getTime()));
                    ps.setInt(ii++, digchn.getV04001());
                    ps.setDouble(ii++, digchn.getRmax05minutes());
                    ps.setTimestamp(ii++, digchn.getTimeRmax05minutes());
                    ps.setDouble(ii++, digchn.getRmax10minutes());
                    ps.setTimestamp(ii++, digchn.getTimeRmax10minutes());
                    ps.setDouble(ii++, digchn.getRmax15minutes());
                    ps.setTimestamp(ii++, digchn.getTimeRmax15minutes());
                    ps.setDouble(ii++, digchn.getRmax20minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax20minutes());
                    ps.setDouble(ii++,digchn.getRmax30minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax30minutes());
                    ps.setDouble(ii++,digchn.getRmax45minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax45minutes());
                    ps.setDouble(ii++,digchn.getRmax60minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax60minutes());
                    ps.setDouble(ii++,digchn.getRmax90minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax90minutes());
                    ps.setDouble(ii++,digchn.getRmax120minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax120minutes());
                    ps.setDouble(ii++,digchn.getRmax180minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax180minutes());
                    ps.setDouble(ii++,digchn.getRmax240minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax240minutes());
                    ps.setDouble(ii++,digchn.getRmax360minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax360minutes());
                    ps.setDouble(ii++,digchn.getRmax540minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax540minutes());
                    ps.setDouble(ii++,digchn.getRmax720minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax720minutes());
                    ps.setDouble(ii++,digchn.getRmax1440minutes());
                    ps.setTimestamp(ii++,digchn.getTimeRmax1440minutes());
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
