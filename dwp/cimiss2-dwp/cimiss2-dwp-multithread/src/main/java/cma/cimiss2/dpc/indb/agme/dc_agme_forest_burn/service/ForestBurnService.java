package cma.cimiss2.dpc.indb.agme.dc_agme_forest_burn.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.ForestBurn;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.utils.ReadCsv;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ForestBurnService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    public static BlockingQueue<StatDi> diQueues;
    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ForestBurnService.diQueues = diQueues;
    }


    /**
     * 森林可燃物
     *
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(ParseResult<ForestBurn> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
        List<ForestBurn> pollenLists = parseResult.getData();
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
    private static void insertDB(List<ForestBurn> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("ES008_value_table_name") + "("
                    + "D_RECORD_ID,D_DATA_ID,D_UPDATE_TIME,D_IYMDHM,D_RYMDHM,D_DATETIME,V01300,V01301,A00201,V06001,V05001,V00200,A00202,A00203," +
                    "A00204,A00205,A00212,A00213,A00214,A00206,A00211,A00009,A00007,A00008,V04001,V04002,V04003 )"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (ForestBurn forestBurn : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    // 年月日
                    String strObservationTime = sf.format(forestBurn.getdDatetime());
                    String[] timess = strObservationTime.split(" ");
                    String[] times = timess[0].split("-");
                    String strYear = times[0];
                    String strMonth = times[1];
                    String strDate = times[2];
                    String stationNumber = String.valueOf(forestBurn.getV01300().intValue());
                    String D_RECORD_ID = stationNumber + index;
                    Date obTime = new Date();
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setDouble(ii++, Double.parseDouble(D_RECORD_ID));
                    ps.setString(ii++,"E.3800.0001.S008");
                    ps.setDate(ii++,new java.sql.Date(new Date().getTime()));
                    ps.setDate(ii++,new java.sql.Date(obTime.getTime()));
                    ps.setDate(ii++,new java.sql.Date(forestBurn.getdRymdhm().getTime()));
                    ps.setDate(ii++,new java.sql.Date(forestBurn.getdDatetime().getTime()));
                    ps.setDouble(ii++, forestBurn.getV01300());
                    ps.setString(ii++, forestBurn.getV01301());
                    ps.setDouble(ii++, forestBurn.getA00201());
                    ps.setString(ii++, ReadCsv.Dms2D(forestBurn.getV06001()));
                    ps.setString(ii++, ReadCsv.Dms2D(forestBurn.getV05001()));
                    ps.setString(ii++, forestBurn.getV00200());
                    ps.setString(ii++, forestBurn.getA00202());
                    ps.setDouble(ii++, forestBurn.getA00203());
                    ps.setDouble(ii++, forestBurn.getA00204());
                    ps.setDouble(ii++, forestBurn.getA00205());
                    ps.setDouble(ii++, forestBurn.getA00212());
                    ps.setDouble(ii++, forestBurn.getA00213());
                    ps.setDouble(ii++, forestBurn.getA00214());
                    ps.setDouble(ii++, forestBurn.getA00206());
                    ps.setDouble(ii++, forestBurn.getA00211());
                    ps.setString(ii++, forestBurn.getA00009());
                    ps.setString(ii++, forestBurn.getA00007());
                    ps.setString(ii++, forestBurn.getA00007());
                    ps.setBigDecimal(ii++, new BigDecimal(strYear));
                    ps.setBigDecimal(ii++, new BigDecimal(strMonth));
                    ps.setBigDecimal(ii++, new BigDecimal(strDate));
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
