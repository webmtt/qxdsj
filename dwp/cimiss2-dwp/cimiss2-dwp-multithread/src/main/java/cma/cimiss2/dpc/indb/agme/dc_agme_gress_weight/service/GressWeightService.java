package cma.cimiss2.dpc.indb.agme.dc_agme_gress_weight.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.GressWeight;
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

public class GressWeightService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    static String type = StartConfig.sodCode();
    public static BlockingQueue<StatDi> diQueues;
    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        GressWeightService.diQueues = diQueues;
    }


    /**
     * 牧草干鲜比
     *
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(ParseResult<GressWeight> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
        List<GressWeight> pollenLists = parseResult.getData();
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
    private static void insertDB(List<GressWeight> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S003_value_table_name") + "("
                    + "D_RECORD_ID, D_DATA_ID, D_IYMDHM, D_UPDATE_TIME, d_datetime, V01301, V01300, prefecture_name, A02500, A02501, A02502, A02503,A02504,A02505,V04001,V04002) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;

            for (GressWeight gressWeight : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    // 年月日
                    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
                    Date obTime = new Date();
                    String stationNumber = String.valueOf(gressWeight.getV01300().intValue());
                    String D_RECORD_ID = stationNumber +""+ index;

                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setDouble(ii++, Double.parseDouble(D_RECORD_ID));
                    ps.setString(ii++,"E.3800.0004.S003");
                    ps.setDate(ii++,new java.sql.Date(obTime.getTime()));
                    ps.setDate(ii++,new java.sql.Date(sf.parse(gressWeight.getV04001().toString()+"-"+gressWeight.getV04002().toString()).getTime()));
                    ps.setDate(ii++,new java.sql.Date(sf.parse(gressWeight.getV04001().toString()+"-"+gressWeight.getV04002().toString()).getTime()));
                    ps.setString(ii++, gressWeight.getV01301());
                    ps.setDouble(ii++, gressWeight.getV01300());
                    ps.setString(ii++, gressWeight.getPrefectureName());
                    ps.setString(ii++, gressWeight.getA02500());
                    ps.setString(ii++, gressWeight.getA02501());
                    ps.setString(ii++, gressWeight.getA02502());
                    ps.setString(ii++, gressWeight.getA02503());
                    ps.setString(ii++, gressWeight.getA02504());
                    ps.setString(ii++, gressWeight.getA02505());
                    ps.setDouble(ii++, gressWeight.getV04001());
                    ps.setDouble(ii++, gressWeight.getV04002());
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
