package org.cimiss2.dwp.util;

import org.cimiss2.dwp.tools.LoggableStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: cimiss2-dwp
 * @描述
 * @创建人 zzj
 * @创建时间 2019/6/25 10:57
 */
public class JdbcUtil {
    private static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

    public static int executeBatch(String dbname,String sql, List<List<Object>> list) {
        PreparedStatement pStatement = null;
        Connection connection = null;
        try {
            connection = SqlConnUtil.getConnection(dbname);
            connection.setAutoCommit(false);
            pStatement = new LoggableStatement(connection, sql);
            if (null != list && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    for(int j=0;j<list.get(i).size();j++){
                        List<Object> obj=list.get(i);
                        pStatement.setObject(j + 1, obj.get(j));
                    }
                    pStatement.addBatch();
                }

            }else{
                return 1;
            }
            pStatement.executeBatch();
            connection.commit();
            return 1;


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                pStatement.clearParameters();
                pStatement.clearBatch();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            close(connection, pStatement, null);

        }
    }

    public static int execute(String dbname,String sql, List<Object> list) {
        PreparedStatement pStatement = null;
        Connection connection = null;
        try {
            connection =  SqlConnUtil.getConnection(dbname);
            pStatement = new LoggableStatement(connection, sql);
            if (null != list && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                        pStatement.setObject(i+1, list.get(i));
                }

            }else{
                return 0;
            }
            pStatement.executeUpdate();
            return 1;


        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            close(connection, pStatement, null);

        }
    }

    public static List<Map<String, Object>> queryMapList(String dbname,String sql, List<Object> list) {
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        Connection conn = null;
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            conn = SqlConnUtil.getConnection(dbname);

            preStmt = conn.prepareStatement(sql);
            if (null != list && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    preStmt.setObject(i + 1, list.get(i));// 下标从1开始
                }
            }
            rs = preStmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            //列的个数
            int colCount=rsmd.getColumnCount();
            //存放列名
            List<String> colNameList=new ArrayList<String>();
            for(int i=0;i<colCount;i++){
                colNameList.add(rsmd.getColumnName(i+1).toLowerCase());
            }
            while (null != rs && rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for(int i=0;i<colCount;i++){
                    String key=colNameList.get(i);
                    String value=rs.getString(key);
                    map.put(key, value);
                }
                lists.add(map);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(conn, preStmt, rs);
        }
        return lists;
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (null != rs) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (null != stmt) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (null != conn) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
