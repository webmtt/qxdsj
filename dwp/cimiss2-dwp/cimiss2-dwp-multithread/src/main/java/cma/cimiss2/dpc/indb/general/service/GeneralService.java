package cma.cimiss2.dpc.indb.general.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.tools.utils.GetPropertiesUtil;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.LoggableStatement;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author ：YCK
 * @date ：Created in 2019/11/27 0027 13:43
 * @description：
 * @modified By：
 * @version: $
 */
public class GeneralService {
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
    public static DataBaseAction processSuccessReport(ParseResult parseResult, Date recv_time, String cts_code, StringBuffer loggerBuffer, String filename) {
        Connection connection = null;
        String database_name = "";
        try {
            database_name = GetPropertiesUtil.getMessage("database_name");
        } catch (IOException e) {e.printStackTrace();

        }
        connection = ConnectionPoolFactory.getInstance().getConnection(database_name);
        List<Map> pollenLists = parseResult.getData();
        insertDB(pollenLists, connection, recv_time, loggerBuffer, filename, cts_code);
        return DataBaseAction.SUCCESS;

    }

    /**
     * 入库
     *
     * @param   数据
     * @param connection
     * @param recv_time    消息接收时间
     * @param loggerBuffer
     * @param filename
     * @param cts_code
     */
    private static void insertDB(List<Map> lista, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            //String field_names = GetPropertiesUtil.getMessage("field_name");
            String field_name = "";
            for (int i = 0; i < 1; i++) {
                field_name = String.valueOf(lista.get(i).keySet()).substring(1,String.valueOf(lista.get(i).keySet()).length()-1);
            }
            String[] fns =field_name.split(",");
            String fuhao = "";
            for (int i = 0; i < fns.length; i++) {
                fuhao += "?,";
            }
            fuhao = fuhao.substring(0, fuhao.length() - 1);
            String sql = " INSERT INTO " + StartConfig.valueTable() + "(" + field_name
                    + ")" + "VALUES (" + fuhao + ")";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            filename = filename.substring(0, filename.indexOf("."));

            int index = 0;

            for (int i = 0; i < lista.size(); i++) {
                Map datamap = lista.get(i);
                int ii = 1;
                index += 1;
                try {
                    Iterator iterator = datamap.keySet().iterator();
                    while (iterator.hasNext()) {
                        String string = (String) iterator.next();
                        ps.setString(ii++, datamap.get(string).toString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("列号：" + ii + "有误");
                }
                ps.addBatch();
                ps.addBatch();
                if (index % 2000 == 0) {
                    ps.executeBatch();
                    connection.commit();
                    ps.clearBatch();
                }
            }
            if (index % 2000 != 0) {
                ps.executeBatch();
                connection.commit();
                ps.clearBatch();
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
        }  finally {
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