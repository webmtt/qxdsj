package cma.cimiss2.dpc.indb.agme.dc_agme_nonsat_mic.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.NonsatMics;
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

public class NONSATService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");


    static String type = StartConfig.sodCode();


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
    public static DataBaseAction processSuccessReport(ParseResult<NonsatMics> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;


        connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
        List<NonsatMics> pollenLists = parseResult.getData();
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
    private static void insertDB(List<NonsatMics> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable() + "("
                    + "D_RECORD_ID, V04001, V04002, V04003, V04004, D_RYMDHM, D_DATETIME,AIR_T_U,AIR_H_M,AIR_T_D,V12030_000, "
                    + "V12030_005, V12030_010,RADI,CO2,PAR_M,SUR_H_U,SUR_H_M,SUR_H_D,COLLECT_V,COLLECT_T,MINUTE_H_M,V01301) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,"
                    + " ?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            // 文件时间 todo
//            String strObservationTime = parseResult.get(0).getFileDate();
//            String stationName = "";

            int index = 0;

            for (NonsatMics nonsatMics : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    // 年月日
                    String strObservationTime = nonsatMics.getdDatetime().toString();
                    // 时分
                    Date obTime = new SimpleDateFormat("yyyyMMddHHmmss").parse(strObservationTime);

                    String strYear = strObservationTime.substring(0, 4);
                    String strMonth = strObservationTime.substring(4, 6);
                    String strDate = strObservationTime.substring(6, 8);
                    String strHour = strObservationTime.substring(8, 10);

                    String stationNumber = nonsatMics.getV01301();
                    String D_RECORD_ID = strObservationTime + "_" + stationNumber + "_" + index;

                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, D_RECORD_ID);
                    // 资料观测年    V04001	decimal(4,0)
                    ps.setBigDecimal(ii++, new BigDecimal(strYear));
                    // 资料观测月    	V04002	decimal(2,0)
                    ps.setBigDecimal(ii++, new BigDecimal(strMonth));
                    // 资料观测日    	V04003	decimal(2,0)
                    ps.setBigDecimal(ii++, new BigDecimal(strDate));
                    // 资料观测时    	V04004	decimal(2,0)
                    ps.setBigDecimal(ii++, new BigDecimal(strHour));
                    ps.setString(ii++, nonsatMics.getdRymdhm());
                    ps.setString(ii++, nonsatMics.getdDatetime());
                    ps.setBigDecimal(ii++, nonsatMics.getAirTU());
                    ps.setBigDecimal(ii++, nonsatMics.getAirHM());
                    ps.setBigDecimal(ii++, nonsatMics.getAirTD());
                    ps.setBigDecimal(ii++, nonsatMics.getV12030000());
                    ps.setString(ii++, nonsatMics.getV12030005());
                    ps.setBigDecimal(ii++, nonsatMics.getV12030010());
                    ps.setBigDecimal(ii++, nonsatMics.getRadi());
                    ps.setBigDecimal(ii++, nonsatMics.getCo2());
                    ps.setBigDecimal(ii++, nonsatMics.getParM());
                    ps.setBigDecimal(ii++, nonsatMics.getSurHU());
                    ps.setBigDecimal(ii++, nonsatMics.getSurHM());
                    ps.setBigDecimal(ii++, nonsatMics.getSurHD());
                    ps.setBigDecimal(ii++, nonsatMics.getCollectV());
                    ps.setBigDecimal(ii++, nonsatMics.getCollectT());
                    ps.setString(ii++, nonsatMics.getMinuteHM());
                    ps.setString(ii++, nonsatMics.getV01301());
                    //ps.setString(ii++, stationNumber);
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
