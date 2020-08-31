package cma.cimiss2.dpc.indb.surf.dc_hysj_hor.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Hysjhor;
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

public class HysjhorService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    static String type = StartConfig.sodCode();

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        HysjhorService.diQueues = diQueues;
    }


    /**
     *
     * @Title: processSuccessReport
     * @Description:行业水利数据
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param fileN
     * @return DataBaseAction
     * @throws：
     */
    public static DataBaseAction processSuccessReport(ParseResult<Hysjhor> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Hysjhor> cliCsv = parseResult.getData();
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
     * @Description:行业水利数据
     * @param: @param list 数据结合
     * @param: @param recv_time  消息接收时间
     * @param: @param is_Batch  是否Batch提交
     * @param: @return
     * @return: DataBaseAction  返回处理状态
     * @throws
     */
    private static void insert_db(List<Hysjhor> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S012_value_table_name") + "("
                    + "D_RECORD_ID, StationNum, Latitude, " +
                    "      Longitude, Ground_Height, Barometer_Height, " +
                    "      Manual_or_Automatic, Observe_Time, F_2_Dir, " +
                    "      F_2_Speed, F_10_Dir, F_10_Speed, " +
                    "      F_Max_Dir, F_Max_Speed, F_Max_Speed_Time, " +
                    "      F_0_Dir, F_0_Speed, F_Great_Dir, " +
                    "      F_Great_Speed, F_Great_Speed_Time, R, " +
                    "      T, T_Max, T_Max_Time, " +
                    "      T_Min, T_Min_Time, U, " +
                    "      U_Min, U_Min_Time, E, " +
                    "      Td, P, P_Max, P_Max_Time, " +
                    "      P_Min, P_Min_Time, B, " +
                    "      B_Max, B_Max_Time, B_Min, " +
                    "      B_Min_Time, D0, D0_Max, " +
                    "      D0_Max_Time, D0_Min, D0_Min_Time, " +
                    "      D5, D10, D15, D20, " +
                    "      D40, D80, D160, D320, " +
                    "      L, P_Sea, V, V_Time, " +
                    "      V_Min_Time)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Hysjhor hysjhor : parseResult) {
                int ii = 1;
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    ps.setString(ii++,id);
                    ps.setString(ii++, hysjhor.getStationnum());
                    ps.setDouble(ii++, hysjhor.getLatitude());
                    ps.setDouble(ii++, hysjhor.getLongitude());
                    ps.setInt(ii++, hysjhor.getGroundHeight());
                    ps.setInt(ii++, hysjhor.getBarometerHeight());
                    ps.setInt(ii++, hysjhor.getManualOrAutomatic());
                    ps.setString(ii++, hysjhor.getObserveTime());
                    ps.setDouble(ii++, hysjhor.getF2Dir());
                    ps.setDouble(ii++, hysjhor.getF2Speed());
                    ps.setDouble(ii++, hysjhor.getF10Dir());
                    ps.setDouble(ii++, hysjhor.getF10Speed());
                    ps.setDouble(ii++, hysjhor.getFMaxDir());
                    ps.setDouble(ii++, hysjhor.getFMaxSpeed());

                    ps.setInt(ii++, hysjhor.getFMaxSpeedTime());
                    ps.setDouble(ii++, hysjhor.getF0Dir());
                    ps.setDouble(ii++, hysjhor.getF0Speed());
                    ps.setDouble(ii++, hysjhor.getFGreatDir());
                    ps.setDouble(ii++, hysjhor.getFGreatSpeed());
                    ps.setInt(ii++, hysjhor.getFGreatSpeedTime());

                    ps.setDouble(ii++, hysjhor.getR());
                    ps.setDouble(ii++, hysjhor.getT());
                    ps.setDouble(ii++, hysjhor.getTMax());
                    ps.setInt(ii++, hysjhor.getTMaxTime());
                    ps.setDouble(ii++, hysjhor.getTMin());
                    ps.setInt(ii++, hysjhor.getTMinTime());

                    ps.setDouble(ii++, hysjhor.getU());
                    ps.setDouble(ii++, hysjhor.getUMin());
                    ps.setInt(ii++, hysjhor.getUMinTime());
                    ps.setDouble(ii++, hysjhor.getE());
                    ps.setDouble(ii++, hysjhor.getTd());
                    ps.setDouble(ii++, hysjhor.getP());

                    ps.setDouble(ii++, hysjhor.getPMax());
                    ps.setInt(ii++, hysjhor.getPMaxTime());
                    ps.setDouble(ii++, hysjhor.getPMin());
                    ps.setInt(ii++, hysjhor.getPMinTime());
                    ps.setDouble(ii++, hysjhor.getB());
                    ps.setDouble(ii++, hysjhor.getBMax());

                    ps.setInt(ii++, hysjhor.getBMaxTime());
                    ps.setDouble(ii++, hysjhor.getBMin());
                    ps.setInt(ii++, hysjhor.getBMinTime());
                    ps.setDouble(ii++, hysjhor.getD0());
                    ps.setDouble(ii++, hysjhor.getD0Max());
                    ps.setInt(ii++, hysjhor.getD0MaxTime());
                    ps.setDouble(ii++, hysjhor.getD0Min());
                    ps.setInt(ii++, hysjhor.getD0MinTime());

                    ps.setDouble(ii++, hysjhor.getD5());
                    ps.setDouble(ii++, hysjhor.getD10());
                    ps.setDouble(ii++, hysjhor.getD15());
                    ps.setDouble(ii++, hysjhor.getD20());
                    ps.setDouble(ii++, hysjhor.getD40());
                    ps.setDouble(ii++, hysjhor.getD80());
                    ps.setDouble(ii++, hysjhor.getD160());
                    ps.setDouble(ii++, hysjhor.getD320());

                    ps.setDouble(ii++, hysjhor.getL());
                    ps.setDouble(ii++, hysjhor.getPSea());
                    ps.setInt(ii++, hysjhor.getV());
                    ps.setInt(ii++, hysjhor.getVTime());
                    ps.setInt(ii++, hysjhor.getVMinTime());
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
