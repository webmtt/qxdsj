package cma.cimiss2.dpc.indb.agme.dc_agme_cli_csv.service;


import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.CliCsv;

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

public class CliCsvService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    static String type = StartConfig.sodCode();

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        CliCsvService.diQueues = diQueues;
    }


    /**
     *
     * @Title: processSuccessReport
     * @Description:标准农田小气候数据
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param fileN
     * @return DataBaseAction
     * @throws：
     */
    public static DataBaseAction processSuccessReport(ParseResult<CliCsv> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<CliCsv> cliCsv = parseResult.getData();
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
     * @Description:标准农田小气候数据
     * @param: @param list 数据结合
     * @param: @param recv_time  消息接收时间
     * @param: @param is_Batch  是否Batch提交
     * @param: @return
     * @return: DataBaseAction  返回处理状态
     * @throws
     */
    private static void insert_db(List<CliCsv> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S007_value_table_name") + "("
                    + "D_RECORD_ID, D_DATA_ID, D_IYMDHM, D_DATETIME, V01301, V01300, provinceNM, cropNM, V05001, V06001, V07001,V04001,V04002,V04003," +
                    "V04004,V04005,V10004,V12001_30,V12001_60,V12001_150,V12001_300,V12001_C,V13019,V11291_30,V11291_60,V11291_150,V11291_300,V11291_600," +
                    "V13003_030,V13003_060,V13003_150,V13003_300,V12030_000,V12030_005,V12030_010,V12030_015,V12030_020,V12030_030,V12030_040,V12030_080," +
                    "V71105_010,V71105_020,V71105_030,V71105_040,V71105_050,V71102_010,V71102_020,V71102_030,V71102_040,V71102_050) "
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;

            for (CliCsv cliCsv : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    // 年月日
                    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String strObservationTime = sf.format(cliCsv.getdDatetime());
                    String strYear = strObservationTime.substring(0, 4);
                    String strMonth = strObservationTime.substring(4, 6);
                    String strDate = strObservationTime.substring(6, 8);
                    String strShi = strObservationTime.substring(8, 10);
                    String strFen = strObservationTime.substring(10, 12);
                    Date obTime = new Date();
                    String stationNumber = cliCsv.getV01301();
                    String D_RECORD_ID = stationNumber + index;

                    // 记录主键	D_RECORD_ID	varchar(500)
                    ps.setString(ii++, D_RECORD_ID);
                    ps.setString(ii++, "E.3800.0004.S007");
                    ps.setDate(ii++, new java.sql.Date(obTime.getTime()));
                    ps.setDate(ii++, new java.sql.Date(cliCsv.getdDatetime().getTime()));
                    ps.setString(ii++, cliCsv.getV01301());
                    ps.setString(ii++, cliCsv.getV01300());
                    ps.setString(ii++, cliCsv.getProvinceNM());
                    ps.setString(ii++, cliCsv.getCropNM());
                    ps.setDouble(ii++, cliCsv.getV05001());
                    ps.setDouble(ii++, cliCsv.getV06001());
                    ps.setDouble(ii++, cliCsv.getV07001());
                    ps.setDouble(ii++, Double.parseDouble(strYear));
                    ps.setDouble(ii++, Double.parseDouble(strMonth));
                    ps.setDouble(ii++, Double.parseDouble(strDate));
                    ps.setDouble(ii++, Double.parseDouble(strShi));
                    ps.setDouble(ii++, Double.parseDouble(strFen));
                    ps.setDouble(ii++, cliCsv.getV10004());
                    ps.setString(ii++, cliCsv.getV1200130());
                    ps.setString(ii++, cliCsv.getV1200160());
                    ps.setDouble(ii++, cliCsv.getV12001150());
                    ps.setDouble(ii++, cliCsv.getV12001300());

                    ps.setString(ii++, cliCsv.getV12001C());
                    ps.setDouble(ii++, cliCsv.getV13019());

                    ps.setDouble(ii++, cliCsv.getV1129130());
                    ps.setDouble(ii++, cliCsv.getV1129160());
                    ps.setDouble(ii++, cliCsv.getV11291150());
                    ps.setDouble(ii++, cliCsv.getV11291300());
                    ps.setDouble(ii++, cliCsv.getV11291600());

                    ps.setDouble(ii++, cliCsv.getV13003030());
                    ps.setDouble(ii++, cliCsv.getV13003060());
                    ps.setDouble(ii++, cliCsv.getV13003150());
                    ps.setDouble(ii++, cliCsv.getV13003300());

                    ps.setDouble(ii++, cliCsv.getV12030000());
                    ps.setDouble(ii++, cliCsv.getV12030005());
                    ps.setDouble(ii++, cliCsv.getV12030010());
                    ps.setDouble(ii++, cliCsv.getV12030015());
                    ps.setDouble(ii++, cliCsv.getV12030020());
                    ps.setDouble(ii++, cliCsv.getV12030030());
                    ps.setDouble(ii++, cliCsv.getV12030040());
                    ps.setDouble(ii++, cliCsv.getV12030080());

                    ps.setDouble(ii++, cliCsv.getV71105010());
                    ps.setDouble(ii++, cliCsv.getV71105020());
                    ps.setDouble(ii++, cliCsv.getV71105030());
                    ps.setDouble(ii++, cliCsv.getV71105040());
                    ps.setDouble(ii++, cliCsv.getV71105050());

                    ps.setDouble(ii++, cliCsv.getV71102010());
                    ps.setDouble(ii++, cliCsv.getV71102020());
                    ps.setDouble(ii++, cliCsv.getV71102030());
                    ps.setDouble(ii++, cliCsv.getV71102040());
                    ps.setDouble(ii++, cliCsv.getV71102050());

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
