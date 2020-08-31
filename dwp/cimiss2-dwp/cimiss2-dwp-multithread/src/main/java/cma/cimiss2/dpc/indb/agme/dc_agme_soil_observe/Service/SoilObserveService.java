package cma.cimiss2.dpc.indb.agme.dc_agme_soil_observe.Service;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.SoilObserve;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;

public class SoilObserveService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    static String type = StartConfig.sodCode();
    public static BlockingQueue<StatDi> diQueues;
    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        SoilObserveService.diQueues = diQueues;
    }

    public static DataBaseAction processSuccessReport(ParseResult<SoilObserve> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
        List<SoilObserve> pollenLists = parseResult.getData();
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
    private static void insertDB(List<SoilObserve> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S019_value_table_name") + "("
                    + "D_RECORD_ID, D_DATA_ID,D_IYMDHM,CNAME,V01301,V01300,V06001,V05001,D_RYMDHM,d_datetime,V04001,D_UPDATE_TIME,A30000,A30006," +
                    "A30001,A30002,A30003,A30004,A30005)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (SoilObserve soilObserve : parseResult) {
                int ii = 1;
                try {
                    String D_RECORD_ID = UUID.randomUUID().toString().replace("-","");
                    Date obTime = new Date();
                    ps.setString(ii++, D_RECORD_ID);
                    ps.setString(ii++, StartConfig.valueTable("S019_cts_code"));
                    ps.setTimestamp(ii++,new Timestamp(obTime.getTime()));
                    ps.setString(ii++, soilObserve.getCname());
                    String sqll="select * from mv_station_info where cname=?";
                    PreparedStatement pstm = connection.prepareStatement(sqll);
                    pstm.setString(1,soilObserve.getCname());
                    ResultSet rs = pstm.executeQuery();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int conut = rsmd.getColumnCount();
                    List<String> list = new ArrayList<>();
                    while (rs.next()) {
                        for (int i = 1; i <= conut; i++) {
                            list.add(rs.getObject(i).toString());
                        }
                    }
                    ps.setString(ii++,list.get(0));
                    ps.setInt(ii++, Integer.parseInt(list.get(0)));
                    ps.setBigDecimal(ii++, new BigDecimal(list.get(7)));
                    ps.setBigDecimal(ii++, new BigDecimal(list.get(8)));
                    ps.setTimestamp(ii++,new Timestamp(soilObserve.getDDatetime().getTime()));
                    ps.setTimestamp(ii++,new Timestamp(soilObserve.getDDatetime().getTime()));
                    ps.setBigDecimal(ii++, new BigDecimal(soilObserve.getV04001()));
                    ps.setTimestamp(ii++,new Timestamp(obTime.getTime()));
                    ps.setString(ii++, soilObserve.getA30000());
                    ps.setString(ii++, soilObserve.getA30006());
                    ps.setString(ii++, soilObserve.getA30001());
                    ps.setString(ii++, soilObserve.getA30002());
                    ps.setString(ii++, soilObserve.getA30003());
                    ps.setString(ii++, soilObserve.getA30004());
                    ps.setString(ii++, soilObserve.getA30005());
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
