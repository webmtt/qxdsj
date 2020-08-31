package cma.cimiss2.dpc.indb.surf.dc_chn_pre.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.ChnPre;
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

public class ChnpreService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    static String type = StartConfig.sodCode();

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnpreService.diQueues = diQueues;
    }


    /**
     *
     * @Title: processSuccessReport
     * @Description:数字化-降水自记纸提取数据
     * @param parseResult
     * @param recv_time
     * @param cts_code
     * @param loggerBuffer
     * @param fileN
     * @return DataBaseAction
     * @throws：
     */
    public static DataBaseAction processSuccessReport(ParseResult<ChnPre> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<ChnPre> cliCsv = parseResult.getData();
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
     * @Description:数字化-降水自记纸提取数据
     * @param: @param list 数据结合
     * @param: @param recv_time  消息接收时间
     * @param: @param is_Batch  是否Batch提交
     * @param: @return
     * @return: DataBaseAction  返回处理状态
     * @throws
     */
    private static void insert_db(List<ChnPre> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S008_value_table_name") + "("
                    + "D_RECORD_ID, D_DATA_ID, D_IYMDHM, D_RYMDHM, D_UPDATE_TIME, D_DATETIME, V_BBB, V01301, V01300, V05001, V06001,V07001,V02001,V02301,V_ACODE," +
                    "V04001,V04002,V04003,V04004,V04005,V13011,Q13011)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;
            String D_DATA_ID = StartConfig.ctsCode("S008_cts_code");
            for (ChnPre chnPre : parseResult) {
                int ii = 1;
                index += 1;
                try {
                    ps.setString(ii++, chnPre.getDRECORDID());
                    ps.setString(ii++, D_DATA_ID);
                    ps.setTimestamp(ii++, chnPre.getDIYMDHM());
                    ps.setTimestamp(ii++, chnPre.getDRYMDHM());
                    ps.setTimestamp(ii++, chnPre.getDUPDATETIME());
                    ps.setTimestamp(ii++, chnPre.getDDATETIME());
                    ps.setString(ii++, chnPre.getVBBB());
                    ps.setString(ii++, chnPre.getV01301());
                    ps.setInt(ii++, chnPre.getV01300());
                    ps.setDouble(ii++, chnPre.getV05001());
                    ps.setDouble(ii++, chnPre.getV06001());
                    ps.setDouble(ii++, chnPre.getV07001());
                    ps.setInt(ii++, chnPre.getV02001());
                    ps.setInt(ii++, chnPre.getV02301());
                    ps.setInt(ii++,chnPre.getVACODE());

                    ps.setInt(ii++, chnPre.getV04001());
                    ps.setInt(ii++, chnPre.getV04002());
                    ps.setInt(ii++, chnPre.getV04003());
                    ps.setInt(ii++, chnPre.getV04004());
                    ps.setInt(ii++, chnPre.getV04005());
                    ps.setDouble(ii++,chnPre.getV13011());
                    ps.setInt(ii++,chnPre.getQ13011());
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
