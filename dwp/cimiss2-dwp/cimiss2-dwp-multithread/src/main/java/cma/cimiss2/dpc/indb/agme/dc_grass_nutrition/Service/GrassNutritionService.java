package cma.cimiss2.dpc.indb.agme.dc_grass_nutrition.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.GrassNutrition;
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

public class GrassNutritionService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    static String type = StartConfig.sodCode();
    public static BlockingQueue<StatDi> diQueues;
    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        GrassNutritionService.diQueues = diQueues;
    }


    /**
     * 天然牧草营养成分
     *
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(ParseResult<GrassNutrition> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
        List<GrassNutrition> pollenLists = parseResult.getData();
        insertDB(pollenLists, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     *
     * @param parseResult  数据
     * @param connection
     * @param recv_time    消息接收时间
     * @param loggerBuffer
     * @param filename
     * @param cts_code
     */
    private static void insertDB(List<GrassNutrition> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S023_value_table_name") + "("
                    + "D_DATA_ID, D_RYMDHM,D_UPDATE_TIME,D_IYMDHM,D_DATETIME,V01301,V04001,V71501,A00100,A00101,A00102,A00103,A00105,A00106,A00107,A00108,A00009)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));
            for (GrassNutrition grassNutrition : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, grassNutrition.getDDataId());
                    ps.setTimestamp(ii++,grassNutrition.getDDatetime());
                    ps.setTimestamp(ii++,grassNutrition.getDIymdhm());
                    ps.setTimestamp(ii++,grassNutrition.getDIymdhm());
                    ps.setTimestamp(ii++,grassNutrition.getDDatetime());
                    ps.setString(ii++,grassNutrition.getV01301());
                    ps.setInt(ii++, grassNutrition.getV04001());
                    ps.setString(ii++, grassNutrition.getV71501());
                    ps.setDouble(ii++, grassNutrition.getA00100());
                    ps.setDouble(ii++, grassNutrition.getA00101());
                    ps.setDouble(ii++, grassNutrition.getA00102());
                    ps.setDouble(ii++, grassNutrition.getA00103());
                    ps.setDouble(ii++, grassNutrition.getA00105());
                    ps.setDouble(ii++, grassNutrition.getA00106());
                    ps.setDouble(ii++, grassNutrition.getA00107());
                    ps.setDouble(ii++, grassNutrition.getA00108());
                    ps.setString(ii++, grassNutrition.getA00009());
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
