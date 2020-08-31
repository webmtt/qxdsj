package cma.cimiss2.dpc.indb.surf.dc_mul_tl.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Muldy;
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

public class MultlService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    static String type = StartConfig.sodCode();

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        MultlService.diQueues = diQueues;
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
    public static DataBaseAction processSuccessReport(ParseResult<Muldy> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Muldy> cliCsv = parseResult.getData();
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
    private static void insert_db(List<Muldy> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable() + "("
                    + "D_RECORD_ID, OBSERVATION_DATA_DATE, STATION_NUMBER, LONGITUDE_FV, LATITUDE_FV, OBSERVATION_PLACE_EVALUATION, TEMP, PRES, AVE_WD_2MIN, AVE_WS_2MIN, WEIGHT_ACC_RAIN_COUNT,RELA_HUMI,VISIBILITY," +
                    "SNOW_DEPTH,LAND_5CM_TEMP,LAND_10CM_TEMP,GR_I,T1,T4,T5,T6,AVE_WD_10MIN,AVE_WS_10MIN,AVE_WS_2MIN_15,AVE_WS_10MIN_15,LAND_IR_TEMP)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Muldy multl : parseResult) {
                int ii = 1;
                try {
                    String ID = UUID.randomUUID().toString().replace("-","");
                    ps.setString(ii++, ID);
                    ps.setTimestamp(ii++, multl.getObservationDataDate());
                    ps.setString(ii++, multl.getStationNumber());
                    ps.setDouble(ii++,multl.getLongitudeFv());
                    ps.setDouble(ii++, multl.getLatitudeFv());
                    ps.setDouble(ii++, multl.getObservationPlaceEvaluation());
                    ps.setDouble(ii++, multl.getTemp());
                    ps.setDouble(ii++, multl.getPres());
                    ps.setDouble(ii++, multl.getAveWd2min());
                    ps.setDouble(ii++, multl.getAveWs2min());
                    ps.setDouble(ii++, multl.getWeightAccRainCount());
                    ps.setDouble(ii++, multl.getRelaHumi());
                    ps.setDouble(ii++,multl.getVisibility());
                    ps.setString(ii++, multl.getSnowDepth());
                    ps.setDouble(ii++, multl.getLand5cmTemp());
                    ps.setDouble(ii++, multl.getLand10cmTemp());
                    ps.setString(ii++,multl.getGrI());
                    ps.setString(ii++, multl.getT1());
                    ps.setString(ii++, multl.getT4());
                    ps.setString(ii++, multl.getT5());
                    ps.setString(ii++, multl.getT6());
                    ps.setDouble(ii++, multl.getAveWd10min());
                    ps.setDouble(ii++, multl.getAveWs10min());
                    ps.setString(ii++, multl.getAveWs2min15());
                    ps.setString(ii++, multl.getAveWs10min15());
                    ps.setDouble(ii++,multl.getLandIrTemp());
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
