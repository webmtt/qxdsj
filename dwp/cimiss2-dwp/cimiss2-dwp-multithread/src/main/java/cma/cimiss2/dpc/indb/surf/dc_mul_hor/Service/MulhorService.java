package cma.cimiss2.dpc.indb.surf.dc_mul_hor.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.Mulhor;
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

public class MulhorService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;
    static String type = StartConfig.sodCode();

    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        MulhorService.diQueues = diQueues;
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
    public static DataBaseAction processSuccessReport(ParseResult<Mulhor> parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer , String fileN) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            List<Mulhor> cliCsv = parseResult.getData();
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
    private static void insert_db(List<Mulhor> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable() + "("
                    + "id,D_DATETIME,LAYER,V01301,D_RYMDHM,H10300,H10301,H10301_052,H10302,H10302_052,H10310,H10311,H10311_052," +
                    "H10312,H10312_052,H10320,H10321,H10321_052,H10322,H10322_052,V11290,V11291,V11292,V11293,V11296,V11042," +
                    "V11042_052,V11201,V11202,V11211,V11046,V11046_052,V13011,V13012,V12001,V12011,V12011_052,V13003_1," +
                    "V13003,V13007,V13007_052,V13004,V12003,V10004,V10301,V10301_052,V10302,V10302_052,V12314,V12315," +
                    "V12315_052,V12316,V12316_052,V12120,V12311,V12311_052,V12121,V12121_052,V12030_040,V12030_050," +
                    "V12030_080,V12030_160,V12030_320,V14042,V20001_701_01,V20059,V20059_052,V10051,V13019,V13011_11,V14052," +
                    "V13011_2,V13011_21,V13011_22,V13013,V12012_052,V12012,D_DATA_ID)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?," +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Mulhor mulhor : parseResult) {
                int ii = 1;
                try {
                    String id = UUID.randomUUID().toString().replace("-","");
                    ps.setString(ii++,id);
                    ps.setString(ii++, mulhor.getDDatetime());
                    ps.setString(ii++, mulhor.getLayer());
                    ps.setString(ii++, mulhor.getV01301());
                    ps.setTimestamp(ii++, mulhor.getDRymdhm());
                    ps.setString(ii++, mulhor.getH10300());
                    ps.setString(ii++, mulhor.getH10301());
                    ps.setString(ii++, mulhor.getH10301052());
                    ps.setString(ii++, mulhor.getH10302());
                    ps.setString(ii++, mulhor.getH10302052());
                    ps.setString(ii++, mulhor.getH10310());
                    ps.setString(ii++, mulhor.getH10311());
                    ps.setString(ii++, mulhor.getH10311052());
                    ps.setString(ii++, mulhor.getH10312());
                    ps.setString(ii++, mulhor.getH10312052());

                    ps.setString(ii++, mulhor.getH10320());
                    ps.setString(ii++, mulhor.getH10321());
                    ps.setString(ii++, mulhor.getH10321052());
                    ps.setString(ii++, mulhor.getH10322());
                    ps.setString(ii++, mulhor.getH10322052());
                    ps.setDouble(ii++, mulhor.getV11290());
                    ps.setDouble(ii++, mulhor.getV11291());
                    ps.setDouble(ii++, mulhor.getV11292());
                    ps.setDouble(ii++, mulhor.getV11293());
                    ps.setDouble(ii++, mulhor.getV11296());
                    ps.setDouble(ii++, mulhor.getV11042());
                    ps.setString(ii++, mulhor.getV11042052());
                    ps.setDouble(ii++, mulhor.getV11201());
                    ps.setDouble(ii++, mulhor.getV11202());

                    ps.setDouble(ii++, mulhor.getV11211());
                    ps.setString(ii++, mulhor.getV11046());
                    ps.setString(ii++, mulhor.getV11046052());
                    ps.setDouble(ii++, mulhor.getV13011());
                    ps.setString(ii++, mulhor.getV13012());
                    ps.setDouble(ii++, mulhor.getV12001());
                    ps.setDouble(ii++, mulhor.getV12011());
                    ps.setString(ii++, mulhor.getV12011052());
                    ps.setString(ii++, mulhor.getV130031());
                    ps.setDouble(ii++, mulhor.getV13003());
                    ps.setDouble(ii++, mulhor.getV13007());
                    ps.setString(ii++, mulhor.getV13007052());
                    ps.setDouble(ii++, mulhor.getV13004());

                    ps.setDouble(ii++, mulhor.getV12003());
                    ps.setDouble(ii++, mulhor.getV10004());
                    ps.setDouble(ii++, mulhor.getV10301());
                    ps.setString(ii++, mulhor.getV10301052());
                    ps.setDouble(ii++, mulhor.getV10302());
                    ps.setString(ii++, mulhor.getV10302052());
                    ps.setDouble(ii++, mulhor.getV12314());
                    ps.setDouble(ii++, mulhor.getV12315());
                    ps.setString(ii++, mulhor.getV12315052());
                    ps.setDouble(ii++, mulhor.getV12316());
                    ps.setString(ii++, mulhor.getV12316052());
                    ps.setDouble(ii++, mulhor.getV12120());
                    ps.setDouble(ii++, mulhor.getV12311());
                    ps.setString(ii++, mulhor.getV12311052());

                    ps.setDouble(ii++, mulhor.getV12121());
                    ps.setString(ii++, mulhor.getV12121052());
                    ps.setDouble(ii++, mulhor.getV12030040());
                    ps.setDouble(ii++, mulhor.getV12030050());
                    ps.setDouble(ii++, mulhor.getV12030080());
                    ps.setDouble(ii++, mulhor.getV12030160());
                    ps.setDouble(ii++, mulhor.getV12030320());
                    ps.setDouble(ii++, mulhor.getV14042());
                    ps.setDouble(ii++, mulhor.getV2000170101());
                    ps.setDouble(ii++, mulhor.getV20059());
                    ps.setString(ii++, mulhor.getV20059052());
                    ps.setDouble(ii++, mulhor.getV10051());
                    ps.setDouble(ii++, mulhor.getV13019());
                    ps.setString(ii++, mulhor.getV1301111());

                    ps.setString(ii++, mulhor.getV14052());
                    ps.setString(ii++, mulhor.getV130112());
                    ps.setString(ii++, mulhor.getV1301121());
                    ps.setString(ii++, mulhor.getV1301122());
                    ps.setDouble(ii++, mulhor.getV13013());
                    ps.setString(ii++, mulhor.getV12012052());
                    ps.setDouble(ii++, mulhor.getV12012());
                    ps.setString(ii++, mulhor.getDDataId());
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
