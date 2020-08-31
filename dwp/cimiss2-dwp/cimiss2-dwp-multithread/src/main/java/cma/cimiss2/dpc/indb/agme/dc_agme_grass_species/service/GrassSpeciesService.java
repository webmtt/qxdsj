package cma.cimiss2.dpc.indb.agme.dc_agme_grass_species.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.GrassSpecies;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
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

public class GrassSpeciesService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    static String type = StartConfig.sodCode();
    public static BlockingQueue<StatDi> diQueues;
    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        GrassSpeciesService.diQueues = diQueues;
    }


    /**
     * 天然草场多样性
     *
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param filename
     * @return
     */
    public static DataBaseAction processSuccessReport(ParseResult<GrassSpecies> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
        List<GrassSpecies> pollenLists = parseResult.getData();
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
    private static void insertDB(List<GrassSpecies> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S002_value_table_name") + "("
                    + "D_DATA_ID, D_IYMDHM,D_RYMDHM,D_DATETIME,V01300,A00002,V71501,A00006,V04001, V04002, V04003 )"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
            for (GrassSpecies grassSpecies : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    // 年月日
                    String strObservationTime = sf.format(grassSpecies.getdDatetime());
                    String[] times = strObservationTime.split("-");
                    String strYear = times[0];
                    String strMonth = times[1];
                    String strDate = times[2];
                    String stationNumber = String.valueOf(grassSpecies.getV01300().intValue());
                    String D_RECORD_ID = stationNumber + "_" + index;
                    Date obTime = new Date();
                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, D_RECORD_ID);
                    ps.setDate(ii++,new java.sql.Date(obTime.getTime()));
                    ps.setDate(ii++,new java.sql.Date(grassSpecies.getdRymdhm().getTime()));
                    ps.setDate(ii++,new java.sql.Date(grassSpecies.getdDatetime().getTime()));
                    ps.setDouble(ii++, grassSpecies.getV01300());
                    ps.setString(ii++, grassSpecies.getA00002());
                    ps.setString(ii++, grassSpecies.getV71501());
                    ps.setInt(ii++, grassSpecies.getA00006());
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
