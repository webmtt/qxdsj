package cma.cimiss2.dpc.indb.agme.dc_agme_zsl_observe.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.ZslObserve;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ZslObserveService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    static String type = StartConfig.sodCode();
    public static BlockingQueue<StatDi> diQueues;
    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ZslObserveService.diQueues = diQueues;
    }


    /**
     * 非标准格式农田小气候（中环天仪）的处理
     *
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(ParseResult<ZslObserve> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
        List<ZslObserve> pollenLists = parseResult.getData();
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
    private static void insertDB(List<ZslObserve> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S006_value_table_name") + "("
                    + "D_RECORD_ID, D_DATA_ID, D_IYMDHM, D_UPDATE_TIME, D_DATETIME,D_RYMDHM, V01301, V01300, station_name, A28000, A00007,A00008,V04001,V04002,V04003) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;

            for (ZslObserve zslObserve : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    // 年月日
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
                    //String strObservationTime = sf.format(zslObserve.getdUpdateTime());
                    /*String strYear = strObservationTime.substring(0, 4);
                    String strMonth = strObservationTime.substring(4, 6);
                    String strDate = strObservationTime.substring(6, 8);*/
                    Date obTime = new Date();
                    String stationNumber = String.valueOf(zslObserve.getV01300().intValue());
                    String D_RECORD_ID = stationNumber + index;

                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setBigDecimal(ii++, zslObserve.getdRecordId());
                    ps.setString(ii++,"E.3800.0001.S007");
                    ps.setDate(ii++,new java.sql.Date(obTime.getTime()));
                    ps.setDate(ii++,new java.sql.Date(obTime.getTime()));
                    ps.setDate(ii++,new java.sql.Date(zslObserve.getdDatetime().getTime()));
                    ps.setDate(ii++,new java.sql.Date(zslObserve.getdDatetime().getTime()));
                    ps.setString(ii++, zslObserve.getV01301());
                    ps.setDouble(ii++, zslObserve.getV01300());
                    ps.setString(ii++, zslObserve.getStationName());
                    ps.setDouble(ii++, zslObserve.getA28000());
                    ps.setString(ii++, zslObserve.getA00007());
                    ps.setString(ii++, zslObserve.getA00007());
                    ps.setDouble(ii++, zslObserve.getV04001());
                    ps.setDouble(ii++, zslObserve.getV04002());
                    ps.setDouble(ii++, zslObserve.getV04003());
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
