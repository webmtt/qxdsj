package cma.cimiss2.dpc.indb.agme.dc_agme_chn_soil.Service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.agme.Chnsoil;
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

public class ChnsoilService {

    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    static String type = StartConfig.sodCode();
    public static BlockingQueue<StatDi> diQueues;
    public static BlockingQueue<StatDi> getDiQueues(){
        return diQueues;
    }
    public static void setDiQueues(BlockingQueue<StatDi> diQueues){
        ChnsoilService.diQueues = diQueues;
    }



    public static DataBaseAction processSuccessReport(ParseResult<Chnsoil> parseResult, Date recv_time, String fileName, StringBuffer loggerBuffer,String cts_code ) {
        Connection connection = null;

        try {
            connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
            List<Chnsoil> cliCsv = parseResult.getData();
            if (fileName.contains("SWP")){
                insert_dbswp(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
            if (fileName.contains("WT")){
                insert_dbwt(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
            if (fileName.contains("FWC")){
                insert_dbfwc(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
            if (fileName.contains("SVW")){
                insert_dbsvw(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
            if (fileName.contains("FH")){
                insert_dbfh(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
            if (fileName.contains("PAI")){
                insert_dbpai(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
            if (fileName.contains("TFAT")){
                insert_dbtfat(cliCsv, connection, recv_time, loggerBuffer,fileName,cts_code);
            }
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


    private static void insert_dbswp(List<Chnsoil> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S017_value_table_name") + "("
                    + "D_RECORD_ID, V01301, V04001, V04002, V04003, D_DATETIME, V71104_010, V71104_020, V71104_030, V71104_040,V71104_050,V71104_060,V71104_070," +
                    "V71104_080,V71104_090,V71104_100,Q05DATA,Q0510DATA,Q1020DATA,Q2030DATA,Q3040DATA,Q4050DATA,Q5060DATA,Q6070DATA,Q7080DATA,Q8090DATA,Q90100DATA)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnsoil chnsoil : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnsoil.getDRecordId());
                    ps.setString(ii++, chnsoil.getV01301());
                    ps.setShort(ii++, chnsoil.getV04001());
                    ps.setShort(ii++, chnsoil.getV04002());
                    ps.setShort(ii++, chnsoil.getV04003());
                    ps.setBigDecimal(ii++, chnsoil.getDDatetime());
                    ps.setBigDecimal(ii++, chnsoil.getV71104010());
                    ps.setBigDecimal(ii++, chnsoil.getV71104020());
                    ps.setBigDecimal(ii++, chnsoil.getV71104030());
                    ps.setBigDecimal(ii++, chnsoil.getV71104040());
                    ps.setBigDecimal(ii++, chnsoil.getV71104050());
                    ps.setBigDecimal(ii++, chnsoil.getV71104060());
                    ps.setBigDecimal(ii++, chnsoil.getV71104070());
                    ps.setBigDecimal(ii++, chnsoil.getV71104080());
                    ps.setBigDecimal(ii++, chnsoil.getV71104090());
                    ps.setBigDecimal(ii++, chnsoil.getV71104100());
                    ps.setBigDecimal(ii++, chnsoil.getQ05data());
                    ps.setBigDecimal(ii++, chnsoil.getQ0510data());
                    ps.setBigDecimal(ii++, chnsoil.getQ1020data());
                    ps.setBigDecimal(ii++, chnsoil.getQ2030data());
                    ps.setBigDecimal(ii++, chnsoil.getQ3040data());
                    ps.setBigDecimal(ii++, chnsoil.getQ4050data());
                    ps.setBigDecimal(ii++, chnsoil.getQ5060data());
                    ps.setBigDecimal(ii++, chnsoil.getQ6070data());
                    ps.setBigDecimal(ii++, chnsoil.getQ7080data());
                    ps.setBigDecimal(ii++, chnsoil.getQ8090data());
                    ps.setBigDecimal(ii++, chnsoil.getQ90100data());
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

    private static void insert_dbwt(List<Chnsoil> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S017_value_table_name") + "("
                    + "D_RECORD_ID, V01301, V04001, V04002, V04003, V07061, Q_V07061)"
                    + "VALUES (?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnsoil chnsoil : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnsoil.getDRecordId());
                    ps.setString(ii++, chnsoil.getV01301());
                    ps.setShort(ii++, chnsoil.getV04001());
                    ps.setShort(ii++, chnsoil.getV04002());
                    ps.setShort(ii++, chnsoil.getV04003());
                    ps.setBigDecimal(ii++, chnsoil.getV07061());
                    ps.setBigDecimal(ii++, chnsoil.getQV07061());
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

    private static void insert_dbfwc(List<Chnsoil> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S017_value_table_name") + "("
                    + "D_RECORD_ID, V01301, V04001, V711100005, V711100510, V711101020, V711102030, V711103040, V711104050, V711105060,V711106070,V711107080,V711108090," +
                    "V7111090100,PARAMENAPDATE,Q_PARAMENAPDATE,Q05DATA,Q0510DATA,Q1020DATA,Q2030DATA,Q3040DATA,Q4050DATA,Q5060DATA,Q6070DATA,Q7080DATA,Q8090DATA,Q90100DATA)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnsoil chnsoil : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnsoil.getDRecordId());
                    ps.setString(ii++, chnsoil.getV01301());
                    ps.setShort(ii++, chnsoil.getV04001());
                    ps.setBigDecimal(ii++, chnsoil.getV711100005());
                    ps.setBigDecimal(ii++, chnsoil.getV711100510());
                    ps.setBigDecimal(ii++, chnsoil.getV711101020());
                    ps.setBigDecimal(ii++, chnsoil.getV711102030());
                    ps.setBigDecimal(ii++, chnsoil.getV711103040());
                    ps.setBigDecimal(ii++, chnsoil.getV711104050());
                    ps.setBigDecimal(ii++, chnsoil.getV711105060());
                    ps.setBigDecimal(ii++, chnsoil.getV711106070());
                    ps.setBigDecimal(ii++, chnsoil.getV711107080());
                    ps.setBigDecimal(ii++, chnsoil.getV711108090());
                    ps.setBigDecimal(ii++, chnsoil.getV7111090100());
                    ps.setBigDecimal(ii++, chnsoil.getParamenapdate());
                    ps.setBigDecimal(ii++, chnsoil.getQParamenapdate());
                    ps.setBigDecimal(ii++, chnsoil.getQ05data());
                    ps.setBigDecimal(ii++, chnsoil.getQ0510data());
                    ps.setBigDecimal(ii++, chnsoil.getQ1020data());
                    ps.setBigDecimal(ii++, chnsoil.getQ2030data());
                    ps.setBigDecimal(ii++, chnsoil.getQ3040data());
                    ps.setBigDecimal(ii++, chnsoil.getQ4050data());
                    ps.setBigDecimal(ii++, chnsoil.getQ5060data());
                    ps.setBigDecimal(ii++, chnsoil.getQ6070data());
                    ps.setBigDecimal(ii++, chnsoil.getQ7080data());
                    ps.setBigDecimal(ii++, chnsoil.getQ8090data());
                    ps.setBigDecimal(ii++, chnsoil.getQ90100data());
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

    private static void insert_dbsvw(List<Chnsoil> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S017_value_table_name") + "("
                    + "D_RECORD_ID, V01301, V04001, V711090005, V711090010, V711091020, V711092030, V711093040, V711094050, V711095060,V711096070,V711097080,V711098090," +
                    "V7110990100,PARAMENAPDATE,Q_PARAMENAPDATE,Q05DATA,Q0510DATA,Q1020DATA,Q2030DATA,Q3040DATA,Q4050DATA,Q5060DATA,Q6070DATA,Q7080DATA,Q8090DATA,Q90100DATA)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnsoil chnsoil : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnsoil.getDRecordId());
                    ps.setString(ii++, chnsoil.getV01301());
                    ps.setShort(ii++, chnsoil.getV04001());
                    ps.setBigDecimal(ii++, chnsoil.getV711090005());
                    ps.setBigDecimal(ii++, chnsoil.getV711090010());
                    ps.setBigDecimal(ii++, chnsoil.getV711091020());
                    ps.setBigDecimal(ii++, chnsoil.getV711092030());
                    ps.setBigDecimal(ii++, chnsoil.getV711093040());
                    ps.setBigDecimal(ii++, chnsoil.getV711094050());
                    ps.setBigDecimal(ii++, chnsoil.getV711095060());
                    ps.setBigDecimal(ii++, chnsoil.getV711096070());
                    ps.setBigDecimal(ii++, chnsoil.getV711097080());
                    ps.setBigDecimal(ii++, chnsoil.getV711098090());
                    ps.setBigDecimal(ii++, chnsoil.getV7110990100());
                    ps.setBigDecimal(ii++, chnsoil.getParamenapdate());
                    ps.setBigDecimal(ii++, chnsoil.getQParamenapdate());
                    ps.setBigDecimal(ii++, chnsoil.getQ05data());
                    ps.setBigDecimal(ii++, chnsoil.getQ0510data());
                    ps.setBigDecimal(ii++, chnsoil.getQ1020data());
                    ps.setBigDecimal(ii++, chnsoil.getQ2030data());
                    ps.setBigDecimal(ii++, chnsoil.getQ3040data());
                    ps.setBigDecimal(ii++, chnsoil.getQ4050data());
                    ps.setBigDecimal(ii++, chnsoil.getQ5060data());
                    ps.setBigDecimal(ii++, chnsoil.getQ6070data());
                    ps.setBigDecimal(ii++, chnsoil.getQ7080data());
                    ps.setBigDecimal(ii++, chnsoil.getQ8090data());
                    ps.setBigDecimal(ii++, chnsoil.getQ90100data());
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

    private static void insert_dbfh(List<Chnsoil> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S017_value_table_name") + "("
                    + "D_RECORD_ID, V01301, V04001, V711080005, V711080510, V711081020, V711082030, V711083040, V711084050, V711085060,V711086070,V711087080,V711088090," +
                    "V7110890100,PARAMENAPDATE,Q_PARAMENAPDATE,Q05DATA,Q0510DATA,Q1020DATA,Q2030DATA,Q3040DATA,Q4050DATA,Q5060DATA,Q6070DATA,Q7080DATA,Q8090DATA,Q90100DATA)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnsoil chnsoil : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnsoil.getDRecordId());
                    ps.setString(ii++, chnsoil.getV01301());
                    ps.setShort(ii++, chnsoil.getV04001());
                    ps.setBigDecimal(ii++, chnsoil.getV711080005());
                    ps.setBigDecimal(ii++, chnsoil.getV711080510());
                    ps.setBigDecimal(ii++, chnsoil.getV711081020());
                    ps.setBigDecimal(ii++, chnsoil.getV711082030());
                    ps.setBigDecimal(ii++, chnsoil.getV711083040());
                    ps.setBigDecimal(ii++, chnsoil.getV711084050());
                    ps.setBigDecimal(ii++, chnsoil.getV711085060());
                    ps.setBigDecimal(ii++, chnsoil.getV711086070());
                    ps.setBigDecimal(ii++, chnsoil.getV711087080());
                    ps.setBigDecimal(ii++, chnsoil.getV711088090());
                    ps.setBigDecimal(ii++, chnsoil.getV7110890100());
                    ps.setBigDecimal(ii++, chnsoil.getParamenapdate());
                    ps.setBigDecimal(ii++, chnsoil.getQParamenapdate());
                    ps.setBigDecimal(ii++, chnsoil.getQ05data());
                    ps.setBigDecimal(ii++, chnsoil.getQ0510data());
                    ps.setBigDecimal(ii++, chnsoil.getQ1020data());
                    ps.setBigDecimal(ii++, chnsoil.getQ2030data());
                    ps.setBigDecimal(ii++, chnsoil.getQ3040data());
                    ps.setBigDecimal(ii++, chnsoil.getQ4050data());
                    ps.setBigDecimal(ii++, chnsoil.getQ5060data());
                    ps.setBigDecimal(ii++, chnsoil.getQ6070data());
                    ps.setBigDecimal(ii++, chnsoil.getQ7080data());
                    ps.setBigDecimal(ii++, chnsoil.getQ8090data());
                    ps.setBigDecimal(ii++, chnsoil.getQ90100data());
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

    private static void insert_dbpai(List<Chnsoil> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S017_value_table_name") + "("
                    + "D_RECORD_ID, V01301, V04001, V04002, V04003, PRECIPITIONCODE, PRECIPITIONDATA, Q_PRECIPITIONDATA)"
                    + "VALUES (?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnsoil chnsoil : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnsoil.getDRecordId());
                    ps.setString(ii++, chnsoil.getV01301());
                    ps.setShort(ii++, chnsoil.getV04001());
                    ps.setShort(ii++, chnsoil.getV04002());
                    ps.setShort(ii++, chnsoil.getV04003());
                    ps.setBigDecimal(ii++, chnsoil.getPrecipitioncode());
                    ps.setBigDecimal(ii++, chnsoil.getPrecipitiondata());
                    ps.setBigDecimal(ii++, chnsoil.getQPrecipitiondata());
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

    private static void insert_dbtfat(List<Chnsoil> parseResult, Connection connection, Date recv_time, StringBuffer loggerBuffer, String filename, String cts_code) {
        PreparedStatement ps = null;
        try {
            String sql = " INSERT INTO " + StartConfig.valueTable("S017_value_table_name") + "("
                    + "D_RECORD_ID, V01301, V04001, FREEZEMARK, FREEZEDATE, FREEZEDATE10, FREEZEDATE20, FREEZEDATE0, Q_FREEZEDATE10, Q_FREEZEDATE20)"
                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";
            ps = new LoggableStatement(connection, sql);
            connection.setAutoCommit(false);
            for (Chnsoil chnsoil : parseResult) {
                int ii = 1;
                try {
                    ps.setString(ii++, chnsoil.getDRecordId());
                    ps.setString(ii++, chnsoil.getV01301());
                    ps.setShort(ii++, chnsoil.getV04001());
                    ps.setBigDecimal(ii++, chnsoil.getFreezemark());
                    ps.setBigDecimal(ii++, chnsoil.getFreezedate());
                    ps.setBigDecimal(ii++, chnsoil.getFreezedate10());
                    ps.setBigDecimal(ii++, chnsoil.getFreezedate20());
                    ps.setBigDecimal(ii++, chnsoil.getFreezedate0());
                    ps.setBigDecimal(ii++, chnsoil.getQFreezedate10());
                    ps.setBigDecimal(ii++, chnsoil.getQFreezedate20());
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
