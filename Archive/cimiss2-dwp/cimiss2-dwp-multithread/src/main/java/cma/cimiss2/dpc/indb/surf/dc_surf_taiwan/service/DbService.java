package cma.cimiss2.dpc.indb.surf.dc_surf_taiwan.service;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.surf.TwSiteData;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.DataBaseAction;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import static cma.cimiss2.dpc.decoder.bean.surf.TwSiteData.*;
import static cma.cimiss2.dpc.indb.surf.dc_surf_taiwan.MainThread.HourA2Count;
import static cma.cimiss2.dpc.indb.surf.dc_surf_taiwan.MainThread.updateCount;

/**
 * ************************************
 * @ClassName: DbService
 * @Auther: dangjinhu
 * @Date: 2019/8/8 09:54
 * @Description: 台湾站点资料db入库
 * @Copyright: All rights reserver.
 * ************************************
 */

public class DbService {
    private static List<StatDi> listDi = new ArrayList<StatDi>();
    public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
    public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
    public static BlockingQueue<StatDi> diQueues;

    public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}

	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		DbService.diQueues = diQueues;
	}

	/**
     * db入库接口
     * @param log         存放日志信息
     * @param parseResult 解析结果集
     * @param receTime    资料接受时间
     * @param filePath    文件收到时间
     * @return DataBaseAction
     */
    public static DataBaseAction processSuccessReport(StringBuffer log, ParseResult<TwSiteData> parseResult, Date receTime, String filePath) {
        DruidPooledConnection connection = null;
        try {
            if (StartConfig.getDatabaseType() == 1) {
                connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
            } else if (StartConfig.getDatabaseType() == 0) {
                connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
            }
            File file = new File(filePath);
            String fileN = file.getName();
            List<TwSiteData> data = parseResult.getData();
            DataBaseAction action = insertDB(fileN, connection, data, receTime, file, log);
            if (DataBaseAction.SUCCESS == action) {
                return action;
            } else {
                return DataBaseAction.INSERT_ERROR;
            }
        } catch (Exception e) {
            log.append("create connection or insert datas error!").append(e.getMessage()).append(filePath);
            return DataBaseAction.CONNECTION_ERROR;
        } finally {
            for (StatDi aListDi : listDi) {
                diQueues.offer(aListDi);
            }
            listDi.clear();
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    log.append("\n close db connection failure!").append(e.getMessage());
                }
            }
        }
    }

    /**
     * 入库详细
     * @param fileN       文件名
     * @param connection  数据库连接
     * @param twSiteDatas 解析对象结果集
     * @param receTime    文件收到时间
     * @param file        文件
     * @param log         日志
     * @return DataBaseAction
     */
    private static DataBaseAction insertDB(String fileN, Connection connection, List<TwSiteData> twSiteDatas, Date receTime, File file, StringBuffer log) {
        String reportTable = StartConfig.reportTable();
        String keyTable = StartConfig.keyTable();
        String sodCode_Hour = StartConfig.sodCodes()[0];
        String sodCode_Min = StartConfig.sodCodes()[1];
        String ctsCode = StartConfig.ctsCode();
        Statement statement = null;
        List<String> sqls = new ArrayList<>();
        List<String> records = new ArrayList<>();
        List<Boolean> result = new ArrayList<>();
        // 单独存放A0002资料整点数据sqlsA2存放sql,recordA2存放主键
        List<String> sqlsA2 = new ArrayList<>();
        List<String> recordsA2 = new ArrayList<>();
        // 判断标识,如果A002资料为整点,则为true
        boolean flagA2 = false;
        try {
            if (connection.getAutoCommit()) {
                connection.setAutoCommit(false);
            }
            statement = connection.createStatement();
            if (StartConfig.getDatabaseType() == 1) {
                // ps.execute("select last_txc_xid()");
            }
            String config = StartConfig.stationInfoPath();
            File configPath = new File(config);
            if (!configPath.exists()) {
                if (!configPath.mkdirs()) {
                    throw new RuntimeException("cannot create configPath!" + configPath);
                }
            }
            // 读取文件名日期 Z_SURF_C_RCTP_YYYYMMDDhhmmss_O_AWS_A0001.XML
//            String name;
//            String[] filenames = fileN.split("_");
//            if (filenames.length == 8) {
//                String datetime = filenames[4];
//                if (datetime.length() == 14) {
//                    name = config + TimeUtil.date2String(TimeUtil.String2Date(datetime, "yyyyMMddHHmmss"), "yyyy-MM-dd") + ".txt";
//                } else {
//                    infoLogger.error("this filename time format error: time length not equals 14!" + datetime);
//                    return DataBaseAction.INSERT_ERROR;
//                }
//            } else {
//                infoLogger.error("this filename format error!" + fileN);
//                return DataBaseAction.INSERT_ERROR;
//            }
            String name = config + "TaiwanStationInfo"+TimeUtil.date2String(new Date(), "yyyy-MM-dd") + ".txt";

            File configFile = new File(name);
            if (!configFile.exists()) {
                try {
                    configFile.createNewFile();
                } catch (IOException e) {
                    infoLogger.error("createNewFile exception!" + e.getMessage());
                    return DataBaseAction.INSERT_ERROR;
                }
            }
            StringBuffer stationInfos = new StringBuffer();
            for (int i = 0; i < twSiteDatas.size(); i++) {
                TwSiteData twSiteData = twSiteDatas.get(i);
                // di
                StatDi di = new StatDi();
                di.setFILE_NAME_O(fileN);
                di.setDATA_TYPE(sodCode_Hour);
                di.setDATA_TYPE_1(ctsCode);
                di.setTT("台湾站点资料");
                di.setTRAN_TIME(TimeUtil.date2String(receTime, "yyyy-MM-dd HH:mm:ss"));
                di.setPROCESS_START_TIME(TimeUtil.getSysTime());
                di.setFILE_NAME_N(fileN);
                di.setBUSINESS_STATE("1"); //1成功,0失败
                di.setPROCESS_STATE("1");  //1成功,0失败
                di.setIIiii(twSiteData.getStationId());
                di.setLONGTITUDE(String.valueOf(twSiteData.getLongitude()));
                di.setLATITUDE(String.valueOf(twSiteData.getLatitude()));
                di.setFILE_SIZE(String.valueOf(file.length()));
                di.setDATA_UPDATE_FLAG("000");
                di.setHEIGHT(String.valueOf(twSiteData.getWeatherElement("ELEV")));
                di.setSEND_PHYS("DRDS");
                String obsTime = twSiteData.getObsTime();
                Date date = null;
                try {
                    date = TimeUtil.String2Date(obsTime, "yyyy-MM-dd HH:mm:ss");
                    date = obsTime(date, obsTime, twSiteData, twSiteDatas, i);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.HOUR, -8);
                    date = calendar.getTime();
                } catch (Exception e) {
                    infoLogger.error("Time conversion exception:" + twSiteData.getObsTime());
                    continue;
                }
//                stationInfos.append(twSiteData.getStationId()).append(",").
//                        append(twSiteData.getLocationName()).append(",").
//                        append(twSiteData.getLatitude()).append(",").
//                        append(twSiteData.getLongitude()).append(",").
//                        append(twSiteData.getWeatherElement("ELEV")).append(",").
//                        append(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss")).append(System.lineSeparator());
                int indx=fileN.lastIndexOf(".");
                String type=fileN.substring(indx-5,indx);//A0001或A0002或A0003
                String attribute=twSiteData.getParameter("ATTRIBUTE")==null?"999999":twSiteData.getParameter("ATTRIBUTE");
                stationInfos.append(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm:ss")).append(",").
                append(type).append(",").
                append(twSiteData.getStationId()).append(",").
                append(twSiteData.getLocationName()).append(",").
                append(twSiteData.getLongitude()).append(",").
                append(twSiteData.getLatitude()).append(",").
                append(twSiteData.getWeatherElement("ELEV")).append(",").
                append(twSiteData.getParameter("CITY")).append(",").
                append(twSiteData.getParameter("TOWN")).append(",").
                append(attribute).
                append(System.lineSeparator());
                StringBuffer sql = new StringBuffer();
                String recordId = TimeUtil.date2String(date, "yyyyMMddHHmm") + "_" + twSiteData.getStationId();
                // 先判断数据是是否是A0002资料,若是入库keyTable
                if (twSiteData.getResourceId().equals("O-A0002-001")) {
                    sql.append(initSqlKeyTable(keyTable));//分钟表
                    setSqlValuesKeyTable(receTime, twSiteData, date, sql, recordId, sodCode_Min, ctsCode);
                    di.setDATA_TYPE(sodCode_Min);
                    if (TimeUtil.getMinute(date) == 0) {//小时表
                        // 若观测时间整点,还需入库表reportTable
                        flagA2 = true;
                        StringBuffer sqlA2 = new StringBuffer(initSqlReportTable(reportTable));
                        setSqlValuesReportTable(receTime, twSiteData, date, sqlA2, recordId, sodCode_Hour, ctsCode);
                        sqlA2.append(")");
                        sqlsA2.add(sqlA2.toString());
                        recordsA2.add(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
                    }
                } else {//小时表
                    // 入库表reportTable
                    sql.append(initSqlReportTable(reportTable));
                    setSqlValuesReportTable(receTime, twSiteData, date, sql, recordId, sodCode_Hour, ctsCode);
                }
                sql.append(")");
                sqls.add(sql.toString());
                records.add(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
                statement.addBatch(sql.toString());
                // 补全di
                di.setDATA_TIME(TimeUtil.date2String(date, "yyyy-MM-dd HH:mm"));
                di.setPROCESS_END_TIME(TimeUtil.getSysTime());
                di.setRECORD_TIME(TimeUtil.getSysTime());
                listDi.add(di);
                if (i > 0 && i % 500 == 0) {
                    try {
                        statement.executeBatch();
                        connection.commit();
                        result.add(true);
                    } catch (SQLException e) {
                        Boolean flag = clearBatchRollback(connection, statement, reportTable, sqls, records, twSiteDatas, log, fileN, i);
                        result.add(flag);
                        // 大于500进行下一轮批量
                    }
                }
            }
            writeStationInfoToFile(stationInfos, configFile);
            try {
                statement.executeBatch();
                connection.commit();
                // 插入成功
                return DataBaseAction.SUCCESS;
            } catch (SQLException e) {
                boolean flag = clearBatchRollback(connection, statement, reportTable, sqls, records, twSiteDatas, log, fileN, 0);
                result.add(flag);
                // 插入失败
                return result.stream().anyMatch(s -> s) ? DataBaseAction.SUCCESS : DataBaseAction.BATCH_ERROR;
            }
        } catch (SQLException e) {
            log.append("\n create Statement error").append(e.getMessage());
            return DataBaseAction.CONNECTION_ERROR;
        }catch (Exception e) {
            
            e.printStackTrace();
            return DataBaseAction.INSERT_ERROR;
        }
        finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.append("\n close Statement error").append(e.getMessage());
                }
            }
            // 返回之前插入A2数据
            if (flagA2) {
                insertsqlA2(connection, sqlsA2, recordsA2, twSiteDatas, reportTable, result, log, sodCode_Hour, ctsCode, file, fileN, receTime);
            }
        }
    }

    /**
     * 将站点信息写入文件
     * @param content 站点信息
     * @param file 文件路径
     */
    private static void writeStationInfoToFile(StringBuffer content, File file) {
        RandomAccessFile rw = null;
        FileLock fileLock = null;
        try {
            rw = new RandomAccessFile(file, "rw");
            fileLock = rw.getChannel().lock();
            rw.seek(rw.length());
            rw.write(content.toString().getBytes());
        } catch (Exception e) {
            infoLogger.error(e.getMessage());
        } finally {
            try {
                if (fileLock != null) fileLock.release();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (rw != null) rw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 降雨小时资料入库(A0002)入库
     * @param connection  数据库连接
     * @param sqlsA2      sql集合
     * @param recordsA2   观测资料世界集合
     * @param twSiteDatas 资料数据
     * @param reportTable 表名
     * @param result      存放结果集合
     * @param log         日志
     * @param sodCode     sodCode
     * @param ctsCode     ctsCode
     * @param file        文件
     * @param fileName    文件名
     * @param receTime    资料接受时间
     */
    private static void insertsqlA2(Connection connection, List<String> sqlsA2, List<String> recordsA2, List<TwSiteData> twSiteDatas, String reportTable, List<Boolean> result, StringBuffer log, String sodCode_Hour, String ctsCode, File file, String fileName, Date receTime) {
        Statement statement = null;
        try {
            if (!connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
            statement = connection.createStatement();
            if (StartConfig.getDatabaseType() == 1) {
                // ps.execute("select last_txc_xid()");
            }
            //
            int insert = 0, update = 0;
            int size = sqlsA2.size();
            for (int index = 0; index < size; index++) {
                TwSiteData twSiteData = twSiteDatas.get(index);
                StatDi di = new StatDi();
                di.setFILE_NAME_O(fileName);
                di.setDATA_TYPE(sodCode_Hour);
                di.setDATA_TYPE_1(ctsCode);
                di.setTT("台湾站点资料");
                di.setTRAN_TIME(TimeUtil.date2String(receTime, "yyyy-MM-dd HH:mm"));
                di.setPROCESS_START_TIME(TimeUtil.getSysTime());
                di.setFILE_NAME_N(fileName);
                di.setBUSINESS_STATE("1"); //1成功,0失败
                di.setPROCESS_STATE("1");  //1成功,0失败
                di.setIIiii(twSiteData.getStationId());
                di.setLONGTITUDE(String.valueOf(twSiteData.getLongitude()));
                di.setLATITUDE(String.valueOf(twSiteData.getLatitude()));
                di.setFILE_SIZE(String.valueOf(file.length()));
                di.setDATA_UPDATE_FLAG("000");
                di.setHEIGHT(String.valueOf(twSiteData.getWeatherElement("ELEV")));
                di.setSEND_PHYS("DRDS");
                String sql = sqlsA2.get(index);
                try {
                    statement.execute(sql);
                    insert++;
                    result.add(true);
                } catch (SQLException e) {
                    // 判断是否为主键冲突异常
                    if (e.getErrorCode() == 13001 || e.getErrorCode() == 1062) {
                        StringBuffer sqlField = new StringBuffer();
                        sqlField.append("V13019=").append(twSiteData.getWeatherElement("RAIN")).append(",");
                        sqlField.append("V13020=").append(twSiteData.getWeatherElement("HOUR_3")).append(",");
                        sqlField.append("V13021=").append(twSiteData.getWeatherElement("HOUR_6")).append(",");
                        sqlField.append("V13022=").append(twSiteData.getWeatherElement("HOUR_12")).append(",");
                        sqlField.append("V13023=").append(twSiteData.getWeatherElement("HOUR_24")).append(",");
                        String sqlFields = sqlField.substring(0, sqlField.length() - 1);
                        String updateSql = "update " + reportTable + " set " + sqlFields + " where D_DATETIME='" + recordsA2.get(index) + "'" + " and V01301='" + twSiteData.getStationId() + "'";
                        try {
                            statement.execute(updateSql);
                            result.add(true);
                            update++;
                            log.append("\nsql update success!").append("stationId is ").append(twSiteData.getStationId());
                        } catch (SQLException ex) {
                            di.setPROCESS_STATE("0");
                            log.append("\nsql update failure!").append("stationId is ").append(twSiteData.getStationId());
                        }
                    } else {
                        di.setPROCESS_STATE("0");
                        log.append("\ninsert sql error! ").append(e.getMessage()).append(sql);
                    }
                }
                // 补全di
                di.setDATA_TIME(recordsA2.get(index));
                di.setPROCESS_END_TIME(TimeUtil.getSysTime());
                di.setRECORD_TIME(TimeUtil.getSysTime());
                listDi.add(di);
            }
            updateCount += update;
            HourA2Count += size;
            log.append("\nA0002 hour data total:").append(size).append(",single insert success:").append(insert).append(",single update success:").append(update).append(",error sql :").append(size - insert - update);
        } catch (SQLException e) {
            log.append("db access error occurs!").append(e.getMessage());
            result.add(false);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.append("close Statement error!").append(e.getMessage());
                }
            }
        }
    }

    /**
     * 批量插入异常处理
     * @param connection  连接
     * @param statement   statement
     * @param tableName   表名
     * @param sqls        sql集合
     * @param records     观测时间集合(世界时)
     * @param twSiteDatas 观测数据集合
     * @param log         日志信息
     * @param fileN       文件名
     * @param index       记录下标
     * @return
     */
    private static boolean clearBatchRollback(Connection connection, Statement statement, String tableName, List<String> sqls, List<String> records, List<TwSiteData> twSiteDatas, StringBuffer log, String fileN, int index) {
        // 统计批量插入失败后,单条插入成功和更新成功的条数
        List<Boolean> count = new ArrayList<>();
        try {
            statement.clearBatch();
            connection.rollback();
            String[] columns;
            String[] elements;
            if (fileN.contains("A0001")) {
                elements = new String[]{"ELEV", "WDIR", "WDSD", "TEMP", "HUMD", "PRES", "H_FX", "H_XD", "H_FXT"};
                columns = new String[]{"V07001", "V11201", "V11202", "V12001", "V13003", "V10004", "V11046", "V11211", "V11046_052"};
            } else if (fileN.contains("A0002")) {
                tableName = StartConfig.keyTable();
                elements = new String[]{"ELEV", "MIN_10"};
                columns = new String[]{"V07001", "V13392_010"};
            } else if (fileN.contains("A0003")) {
                elements = new String[]{"ELEV", "WDIR", "WDSD", "TEMP", "HUMD", "PRES", "H_FX", "H_XD", "H_FXT", "H_F10", "H_10D", "H_F10T", "H_VIS"};
                columns = new String[]{"V07001", "V11201", "V11202", "V12001", "V13003", "V10004", "V11046", "V11211", "V11046_052", "V11042", "V11296", "V11042_052", "V20001"};
            } else {
                log.append("this is data fileName error !");
                return false;
            }
            if (!connection.getAutoCommit()) {
                connection.setAutoCommit(true);
            }
            int i, size;
            // 批量处理大于500和小于500两种情况,计算获取listDi存放的对应di下标开始到结束
            if (index == 0) {
                i = sqls.size() > 500 ? (sqls.size() / 500 * 500 + 1) : 0;
                size = sqls.size() - 1;
            } else {
                // 500倍数
                i = index > 500 ? index - 500 + 1 : index - 500;
                size = index;
            }
            int insert = 0, update = 0;
            for (; i <= size; i++) {
                String sql = sqls.get(i);
                TwSiteData twSiteData = twSiteDatas.get(i);
                try {
                    statement.execute(sql);
                    insert++;
                    // 单条插入成功
                    count.add(true);
                } catch (SQLException e) {
                    // 判断是否为主键冲突异常
                    if (e.getErrorCode() == 13001 || e.getErrorCode() == 1062) {
                        StringBuffer sqlField = new StringBuffer();
                        for (int j = 0; j < columns.length; j++) {
                            // H_F10T、H_FXT转换为世界时
                            Double ele = twSiteData.getWeatherElement(elements[j]);
                            if (elements[j].equals("HUMD") && ele != 999999) {
                                // 计算相对湿度值
                                sqlField.append(columns[j]).append("=").append((int) Math.round(ele * 100)).append(",");
                            } else if (elements[j].equals("ELEV")) {
                                // 针对A0002(雨量数据文件)高度值不做修改
                                if (!twSiteData.getResourceId().equals("O-A0002-001")) {
                                    sqlField.append(columns[j]).append("=").append(ele).append(",");
                                }
                            } else {
                                sqlField.append(columns[j]).append("=").append(ele).append(",");
                            }
                        }
                        String sqlFields = sqlField.substring(0, sqlField.length() - 1);
                        String updateSql = "update " + tableName + " set " + sqlFields + " where D_DATETIME='" + records.get(i) + "'" + " and V01301='" + twSiteData.getStationId() + "'";
                        try {
                            statement.execute(updateSql);
                            update++;
                            count.add(true);
                            log.append("\nsql update success!").append("stationId is ").append(twSiteData.getStationId());
                        } catch (SQLException ex) {
                            count.add(false);
                            listDi.get(i).setPROCESS_STATE("0");
                            log.append("\nsql update failure!").append("stationId is ").append(twSiteData.getStationId());
                        }

                    } else {
                        count.add(false);
                        listDi.get(i).setPROCESS_STATE("0");
                        log.append("\ninsert sql error! ").append(e.getMessage()).append(sql);
                    }
                }
            }
            long countTrue = count.stream().filter(s -> s).count();
            updateCount += update;
            log.append("\nbatch insert failure！total:").append(count.size()).append(",single insert success:").append(insert).append(",single update success:").append(update).append(",error sql :").append(count.size() - insert - update);
            connection.setAutoCommit(false);
            return countTrue > 0;
        } catch (SQLException e) {
            log.append("db access error occurs!");
            return count.stream().anyMatch(s -> s);
        }
    }

    /**
     * @param receTime   资料接受时间
     * @param twSiteData 观测数据对象
     * @param date       观测资料时间(世界时)
     * @param sql        sql
     * @param recordId   记录标识
     * @param sodCode    sodCode
     * @param ctsCode    ctsCode
     */
    private static void setSqlValuesReportTable(Date receTime, TwSiteData twSiteData, Date date, StringBuffer sql, String recordId, String sodCode_Hour, String ctsCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sql.append("'").append(recordId).append("'"); // 记录标识
        sql.append("'").append(sodCode_Hour).append("'"); // 资料标识
        sql.append(",'").append(sdf.format(new Date())).append("'"); // 入库时间
        sql.append(",'").append(sdf.format(receTime.getTime())).append("'");  // 收到时间
        sql.append(",'").append(sdf.format(new Date().getTime())).append("'"); // 更新时间
        sql.append(",'").append(sdf.format(date.getTime())).append("'"); // 资料时间
        sql.append(",'").append(ctsCode).append("'"); // 数据来源
        sql.append(",").append(TimeUtil.getYear(date));// 年
        sql.append(",").append(TimeUtil.getMonth(date));// 月
        sql.append(",").append(TimeUtil.getDayOfMonth(date));// 日
        sql.append(",").append(TimeUtil.getHourOfDay(date));// 时
        sql.append(",'").append(twSiteData.getStationId()).append("'");// 区站号/观测平台标识(字符)
        sql.append(",").append(DEFAULT9);// 区站号/观测平台标识(数字)
        sql.append(",").append(String.format("%.4f",twSiteData.getLatitude()));// 纬度
        sql.append(",").append(String.format("%.4f",twSiteData.getLongitude()));// 经度
        sql.append(",").append(twSiteData.getWeatherElement("ELEV"));// 测站高度
        sql.append(",").append(DEFAULT8);// 气压传感器海拔高度
        sql.append(",").append(DEFAULT8);// 风速传感器距地面高度
        sql.append(",").append(DEFAULT8);// 温湿传感器离地面高度
        sql.append(",").append(DEFAULT8);// 能见度传感器离地面高度
        sql.append(",").append(DEFAULT8);// 测站类型
        sql.append(",").append(DEFAULT8);// 测站级别
        sql.append(",'").append("710000").append("'");// 中国行政区划代码
        sql.append(",").append(DEFAULT8);// 地面限定符（温度数据）
        sql.append(",").append(DEFAULT8);// 云探测系统
        sql.append(",'").append("000").append("'");// 更正报标志
        sql.append(",").append(twSiteData.getWeatherElement("PRES"));// 气压
        sql.append(",").append(DEFAULT8);// 海平面气压
        sql.append(",").append(DEFAULT8);// 3小时变压
        sql.append(",").append(DEFAULT8);// 24小时变压
        sql.append(",").append(DEFAULT8);// 最高本站气压
        sql.append(",").append(DEFAULT8);// 最高本站气压出现时间
        sql.append(",").append(DEFAULT8);// 最低本站气压
        sql.append(",").append(DEFAULT8);// 最低本站气压出现时间
        sql.append(",").append(twSiteData.getWeatherElement("TEMP"));// 温度/气温
        sql.append(",").append(DEFAULT8);// 最高气温
        sql.append(",").append(DEFAULT8);// 最高气温出现时间
        sql.append(",").append(DEFAULT8);// 最低气温
        sql.append(",").append(DEFAULT8);// 最低气温出现时间
        sql.append(",").append(DEFAULT8);// 过去24小时变温
        sql.append(",").append(DEFAULT8);// 过去24小时最高气温
        sql.append(",").append(DEFAULT8);// 过去24小时最低气温
        sql.append(",").append(DEFAULT8);// 露点温度
        if (twSiteData.getWeatherElement("HUMD") == 999999 || twSiteData.getWeatherElement("HUMD") == 999998) {
            sql.append(",").append(twSiteData.getWeatherElement("HUMD"));// 相对湿度
        } else {
            sql.append(",").append((int) Math.round(twSiteData.getWeatherElement("HUMD") * 100));// 相对湿度
        }
        sql.append(",").append(DEFAULT8);// 最小相对湿度
        sql.append(",").append(DEFAULT8);// 最小相对湿度出现时间
        sql.append(",").append(DEFAULT8);// 水汽压
        sql.append(",").append(twSiteData.getWeatherElement("RAIN"));// 过去1小时降水量
        sql.append(",").append(twSiteData.getWeatherElement("HOUR_3"));// 过去3小时降水量
        sql.append(",").append(twSiteData.getWeatherElement("HOUR_6"));// 过去6小时降水量
        sql.append(",").append(twSiteData.getWeatherElement("HOUR_12"));// 过去12小时降水量
        sql.append(",").append(twSiteData.getWeatherElement("HOUR_24"));// 过去24小时降水量
        sql.append(",").append(DEFAULT8);// 人工加密观测降水量描述周期
        sql.append(",").append(DEFAULT8);// 降水量
        sql.append(",").append(DEFAULT8);// 蒸发(大型)
        sql.append(",").append(DEFAULT8);// 2分钟平均风向(角度)
        sql.append(",").append(DEFAULT8);// 2分钟平均风速
        sql.append(",").append(DEFAULT8);// 10分钟平均风向(角度)
        sql.append(",").append(DEFAULT8);// 10分钟平均风速
        sql.append(",").append(twSiteData.getWeatherElement("H_10D"));// 日最大风速的风向(角度)
        sql.append(",").append(twSiteData.getWeatherElement("H_F10"));// 最大风速
        // 时间转为世界时
        if (twSiteData.getWeatherElement("H_F10T") == 999999 || twSiteData.getWeatherElement("H_F10T") == 999998) {
            sql.append(",").append(twSiteData.getWeatherElement("H_F10T"));// 最大风速出现时间
        } else {
            String hourMinute = hourMinuteToUT(twSiteData.getWeatherElement("H_F10T"));
            sql.append(",").append(hourMinute);// 最大风速出现时间
            // 同时修改值
            twSiteData.setWeatherElement("H_F10T", Double.parseDouble(hourMinute));
        }
        sql.append(",").append(twSiteData.getWeatherElement("WDIR"));// 瞬时风向(角度)
        sql.append(",").append(twSiteData.getWeatherElement("WDSD"));// 瞬时风速
        sql.append(",").append(twSiteData.getWeatherElement("H_XD"));// 极大风速的风向(角度)
        sql.append(",").append(twSiteData.getWeatherElement("H_FX"));// 极大风速
        // 时间转为世界时
        if (twSiteData.getWeatherElement("H_FXT") == 999999 || twSiteData.getWeatherElement("H_FXT") == 999998) {
            sql.append(",").append(twSiteData.getWeatherElement("H_FXT"));// 极大风速出现时间
        } else {
            String hourMinute = hourMinuteToUT(twSiteData.getWeatherElement("H_FXT"));
            sql.append(",").append(hourMinute);// 极大风速出现时间
            // 同时修改值
            twSiteData.setWeatherElement("H_FXT", Double.parseDouble(hourMinute));
        }
        sql.append(",").append(DEFAULT8);// 过去6小时极大瞬时风向
        sql.append(",").append(DEFAULT8);// 过去6小时极大瞬时风速
        sql.append(",").append(DEFAULT8);// 过去12小时极大瞬时风向
        sql.append(",").append(DEFAULT8);// 过去12小时极大瞬时风速
        sql.append(",").append(DEFAULT8);// 地面温度
        sql.append(",").append(DEFAULT8);// 最高地面温度
        sql.append(",").append(DEFAULT8);// 最高地面温度出现时间
        sql.append(",").append(DEFAULT8);// 最低地面温度
        sql.append(",").append(DEFAULT8);// 最低地面温度出现时间
        sql.append(",").append(DEFAULT8);// 过去12小时地面最低温度
        sql.append(",").append(DEFAULT8);// 5cm地温
        sql.append(",").append(DEFAULT8);// 10cm地温
        sql.append(",").append(DEFAULT8);// 15cm地温
        sql.append(",").append(DEFAULT8);// 20cm地温
        sql.append(",").append(DEFAULT8);// 40cm地温
        sql.append(",").append(DEFAULT8);// 80cm地温
        sql.append(",").append(DEFAULT8);// 160cm地温
        sql.append(",").append(DEFAULT8);// 320cm地温
        sql.append(",").append(DEFAULT8);// 草面(雪面)温度
        sql.append(",").append(DEFAULT8);// 草面(雪面)最高温度
        sql.append(",").append(DEFAULT8);// 草面(雪面)最高温度出现时间
        sql.append(",").append(DEFAULT8);// 草面(雪面)最低温度
        sql.append(",").append(DEFAULT8);// 草面(雪面)最低温度出现时间
        sql.append(",").append(DEFAULT8);// 1分钟平均能见度
        sql.append(",").append(DEFAULT8);// 10分钟平均能见度
        sql.append(",").append(DEFAULT8);// 最小水平能见度
        sql.append(",").append(DEFAULT8);// 最小水平能见度出现时间
        sql.append(",").append(twSiteData.getWeatherElement("H_VIS"));// 水平能见度(人工)
        sql.append(",").append(DEFAULT8);// 总云量
        sql.append(",").append(DEFAULT8);// 低云量
        sql.append(",").append(DEFAULT8);// 云量(低云或中云)
        sql.append(",").append(DEFAULT8);// 云底高度
        sql.append(",").append(DEFAULT8);// 云状1
        sql.append(",").append(DEFAULT8);// 云状2
        sql.append(",").append(DEFAULT8);// 云状3
        sql.append(",").append(DEFAULT8);// 云状4
        sql.append(",").append(DEFAULT8);// 云状5
        sql.append(",").append(DEFAULT8);// 云状6
        sql.append(",").append(DEFAULT8);// 云状7
        sql.append(",").append(DEFAULT8);// 云状8
        sql.append(",").append(DEFAULT8);// 低云状
        sql.append(",").append(DEFAULT8);// 中云状
        sql.append(",").append(DEFAULT8);// 高云状
        sql.append(",").append(DEFAULT8);// 现在天气
        sql.append(",").append(DEFAULT8);// 过去天气描述事件周期
        sql.append(",").append(DEFAULT8);// 过去天气1
        sql.append(",").append(DEFAULT8);// 过去天气2
        sql.append(",").append(DEFAULT8);// 地面状态
        sql.append(",").append(DEFAULT8);// 积雪深度
        sql.append(",").append(DEFAULT8);// 雪压
        sql.append(",").append(DEFAULT8);// 第一冻土层上界值
        sql.append(",").append(DEFAULT8);// 第一冻土层下界值
        sql.append(",").append(DEFAULT8);// 第二冻土层上界值
        sql.append(",").append(DEFAULT8);// 第二冻土层下界值
        sql.append(",").append(DEFAULTQC);// 气压质控码
        sql.append(",").append(DEFAULTQC);// 海平面气压质量控制标志
        sql.append(",").append(DEFAULTQC);// 3小时变压质控码
        sql.append(",").append(DEFAULTQC);// 24小时变压质控码
        sql.append(",").append(DEFAULTQC);// 日最高本站气压质控码
        sql.append(",").append(DEFAULTQC);// 日最高本站气压出现时间质控码
        sql.append(",").append(DEFAULTQC);// 日最低本站气压质控码
        sql.append(",").append(DEFAULTQC);// 日最低本站气压出现时间质控码
        sql.append(",").append(DEFAULTQC);// 温度/气温质控码
        sql.append(",").append(DEFAULTQC);// 日最高气温质控码
        sql.append(",").append(DEFAULTQC);// 日最高气温出现时间质控码
        sql.append(",").append(DEFAULTQC);// 1小时内最低气温质控码
        sql.append(",").append(DEFAULTQC);// 小时内最低气温出现时间质控码
        sql.append(",").append(DEFAULTQC);// 24小时变温质控码
        sql.append(",").append(DEFAULTQC);// 过去24小时最高气温质控码
        sql.append(",").append(DEFAULTQC);// 过去24小时最低气温质控码
        sql.append(",").append(DEFAULTQC);// 露点温度质控码
        sql.append(",").append(DEFAULTQC);// 相对湿度质控码
        sql.append(",").append(DEFAULTQC);// 最小相对湿度质控码
        sql.append(",").append(DEFAULTQC);// 最小相对湿度出现时间质控码
        sql.append(",").append(DEFAULTQC);// 水汽压质控码
        sql.append(",").append(DEFAULTQC);// 小时降水量质控码
        sql.append(",").append(DEFAULTQC);// 过去3小时降水量质控码
        sql.append(",").append(DEFAULTQC);// 过去6小时降水量质控码
        sql.append(",").append(DEFAULTQC);// 过去12小时降水量质控码
        sql.append(",").append(DEFAULTQC);// 24小时降水量质控码
        sql.append(",").append(DEFAULTQC);// 人工加密观测降水量描述时间周期质控码
        sql.append(",").append(DEFAULTQC);// 分钟降水量质控码
        sql.append(",").append(DEFAULTQC);// 日蒸发量（大型）质控码
        sql.append(",").append(DEFAULTQC);// 2分钟平均风向质控码值
        sql.append(",").append(DEFAULTQC);// 2分钟平均风速成质控码值
        sql.append(",").append(DEFAULTQC);// 10分钟风向质控码
        sql.append(",").append(DEFAULTQC);// 10分钟平均风速质控码
        sql.append(",").append(DEFAULTQC);// 日最大风速的风向质控码
        sql.append(",").append(DEFAULTQC);// 日最大风速质控码
        sql.append(",").append(DEFAULTQC);// 日最大风速出现时间质控码
        sql.append(",").append(DEFAULTQC);// 瞬时风向(角度)质控码
        sql.append(",").append(DEFAULTQC);// 瞬时风速质控码
        sql.append(",").append(DEFAULTQC);// 日极大风速的风向质控码
        sql.append(",").append(DEFAULTQC);// 日极大风速质控码
        sql.append(",").append(DEFAULTQC);// 日极大风速出现时间质控码
        sql.append(",").append(DEFAULTQC);// 过去6小时极大瞬时风向质控码
        sql.append(",").append(DEFAULTQC);// 过去6小时极大瞬时风速质控码
        sql.append(",").append(DEFAULTQC);// 过去12小时极大瞬时风向质控码
        sql.append(",").append(DEFAULTQC);// 过去12小时极大瞬时风速质控码
        sql.append(",").append(DEFAULTQC);// 地面温度质控码
        sql.append(",").append(DEFAULTQC);// 日最高地面温度质控码
        sql.append(",").append(DEFAULTQC);// 日最高地面温度出现时间质控码
        sql.append(",").append(DEFAULTQC);// 日最低地面温度质控码
        sql.append(",").append(DEFAULTQC);// 日最低地面温度出现时间质控码
        sql.append(",").append(DEFAULTQC);// 过去12小时最低地面温度质控码
        sql.append(",").append(DEFAULTQC);// 5cm地温质控码
        sql.append(",").append(DEFAULTQC);// 10cm地温质控码
        sql.append(",").append(DEFAULTQC);// 15cm地温质控码
        sql.append(",").append(DEFAULTQC);// 20cm地温质控码
        sql.append(",").append(DEFAULTQC);// 40cm地温质控码
        sql.append(",").append(DEFAULTQC);// 80cm地温质控码
        sql.append(",").append(DEFAULTQC);// 160cm地温质控码
        sql.append(",").append(DEFAULTQC);// 320cm地温质控码
        sql.append(",").append(DEFAULTQC);// 草面（雪面）温度质控码
        sql.append(",").append(DEFAULTQC);// 日草面（雪面）最高温度质控码
        sql.append(",").append(DEFAULTQC);// 日草面（雪面）最高温度出现时间质控码
        sql.append(",").append(DEFAULTQC);// 日草面（雪面）最低温度质控码
        sql.append(",").append(DEFAULTQC);// 日草面（雪面）最低温度出现时间质控码
        sql.append(",").append(DEFAULTQC);// 1分钟平均水平能见度质控码
        sql.append(",").append(DEFAULTQC);// 10分钟平均水平能见度质控码
        sql.append(",").append(DEFAULTQC);// 日最小水平能见度质控码
        sql.append(",").append(DEFAULTQC);// 日最小水平能见度出现时间质控码
        sql.append(",").append(DEFAULTQC);// 水平能见度质控码
        sql.append(",").append(DEFAULTQC);// 总云量质控码
        sql.append(",").append(DEFAULTQC);// 低云量质控码
        sql.append(",").append(DEFAULTQC);// 低云或中云的云量质控码
        sql.append(",").append(DEFAULTQC);// 云底高度质控码
        sql.append(",").append(DEFAULTQC);// 云状1质控码
        sql.append(",").append(DEFAULTQC);// 云状2质控码
        sql.append(",").append(DEFAULTQC);// 云状3质控码
        sql.append(",").append(DEFAULTQC);// 云状4质控码
        sql.append(",").append(DEFAULTQC);// 云状5质控码
        sql.append(",").append(DEFAULTQC);// 云状6质控码
        sql.append(",").append(DEFAULTQC);// 云状7质控码
        sql.append(",").append(DEFAULTQC);// 云状8质控码
        sql.append(",").append(DEFAULTQC);// 低云状质控码
        sql.append(",").append(DEFAULTQC);// 中云状质控码
        sql.append(",").append(DEFAULTQC);// 高云状质控码
        sql.append(",").append(DEFAULTQC);// 现在天气质控码
        sql.append(",").append(DEFAULTQC);// 过去天气描述时间周期质控码
        sql.append(",").append(DEFAULTQC);// 过去天气1质控码
        sql.append(",").append(DEFAULTQC);// 过去天气2质控码
        sql.append(",").append(DEFAULTQC);// 地面状态质控码
        sql.append(",").append(DEFAULTQC);// 路面雪层厚度质控码
        sql.append(",").append(DEFAULTQC);// 雪压质控码
        sql.append(",").append(DEFAULTQC);// 第一冻土层上界值质控码
        sql.append(",").append(DEFAULTQC);// 第一冻土层下界值质控码
        sql.append(",").append(DEFAULTQC);// 第二冻土层上界值质控码
        sql.append(",").append(DEFAULTQC);// 第二冻土层下界值质控码
        sql.append(",").append(DEFAULT8);// 保留字段1
        sql.append(",").append(DEFAULT8);// 保留字段2
        sql.append(",").append(DEFAULT8);// 保留字段3
        sql.append(",").append(DEFAULT8);// 保留字段4
        sql.append(",").append(DEFAULT8);// 保留字段5
        sql.append(",").append(DEFAULT8);// 保留字段6
        sql.append(",").append(DEFAULT8);// 保留字段7
        sql.append(",").append(DEFAULT8);// 保留字段8
        sql.append(",").append(DEFAULT8);// 保留字段9
        sql.append(",").append(DEFAULT8);// 保留字段10
    }

    /**
     * 报文中的时间转为世界时
     * @param value 观测时间,格式 "0000"小时分钟
     * @return 观测世界时, 格式"0000"
     */
    private static String hourMinuteToUT(Double value) {
        DecimalFormat format = new DecimalFormat("0000");
        String hourMinute = format.format(value);
        int hour = Integer.parseInt(hourMinute.substring(0, 2));
        if (hour - 8 < 0) {
            hour += 16;
        } else {
            hour -= 8;
        }
        return hour + hourMinute.substring(2, 4);
    }

    /**
     * @param receTime   资料接受时间
     * @param twSiteData 观测数据对象
     * @param date       观测资料时间时
     * @param sql        sql
     * @param recordId   记录标识
     * @param sodCode    sodcode
     * @param ctsCode    ctscode
     */
    private static void setSqlValuesKeyTable(Date receTime, TwSiteData twSiteData, Date date, StringBuffer sql, String recordId, String sodCode_Min, String ctsCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        sql.append("'").append(recordId).append("'"); // 记录标识
        sql.append("'").append(sodCode_Min).append("'"); // 资料标识
        sql.append(",'").append(sdf.format(new Date())).append("'"); // 入库时间
        sql.append(",'").append(sdf.format(receTime.getTime())).append("'");  // 收到时间
        sql.append(",'").append(sdf.format(new Date().getTime())).append("'"); // 更新时间
        sql.append(",'").append(sdf.format(date.getTime())).append("'"); // 资料时间
        sql.append(",'").append(twSiteData.getStationId()).append("'");// 区站号/观测平台标识(字符)
        sql.append(",").append(String.format("%.4f",twSiteData.getLatitude()));// 纬度
        sql.append(",").append(String.format("%.4f",twSiteData.getLongitude()));// 经度
        sql.append(",").append(twSiteData.getWeatherElement("ELEV"));// 测站高度
        sql.append(",").append(DEFAULT8);// 水平能见度(人工)
        sql.append(",").append(DEFAULT8);// 测站级别
        sql.append(",").append(710000);// 中国行政区划代码
        sql.append(",").append(TimeUtil.getYear(date));// 年
        sql.append(",").append(TimeUtil.getMonth(date));// 月
        sql.append(",").append(TimeUtil.getDayOfMonth(date));// 日
        sql.append(",").append(TimeUtil.getHourOfDay(date));// 时
        sql.append(",").append(TimeUtil.getMinute(date));// 分
        sql.append(",").append(DEFAULT8);// 5分钟雨量
        sql.append(",").append(twSiteData.getWeatherElement("MIN_10"));// 10分钟雨量
        sql.append(",").append(DEFAULTQC);// 5分钟雨量质控码
        sql.append(",").append(DEFAULTQC);// 10分钟雨量质控码
        sql.append(",'").append(ctsCode).append("'");// D_SOURCE_ID
        sql.append(",'").append("000").append("'");//V_BBB
    }

    /**
     * 获取键表头部字段
     * @param tableName 表名
     * @return {@code sql}
     */
    private static String initSqlReportTable(String tableName) {
        String sql;
        sql = "insert into " + tableName
                + " (D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,D_SOURCE_ID,V04001,V04002,V04003,V04004,V01301,V01300,V05001,V06001,V07001,V07031,V07032_04,V07032_01,V07032_02,V02001,V02301,V_ACODE,V08010,V02183,V_BBB,V10004,V10051,V10061,V10062,V10301,V10301_052,V10302,V10302_052,V12001,V12011,V12011_052,V12012,V12012_052,V12405,V12016,V12017,V12003,V13003,V13007,V13007_052,V13004,V13019,V13020,V13021,V13022,V13023,V04080_04,V13011,V13033,V11290,V11291,V11292,V11293,V11296,V11042,V11042_052,V11201,V11202,V11211,V11046,V11046_052,V11503_06,V11504_06,V11503_12,V11504_12,V12120,V12311,V12311_052,V12121,V12121_052,V12013,V12030_005,V12030_010,V12030_015,V12030_020,V12030_040,V12030_080,V12030_160,V12030_320,V12314,V12315,V12315_052,V12316,V12316_052,V20001_701_01,V20001_701_10,V20059,V20059_052,V20001,V20010,V20051,V20011,V20013,V20350_01,V20350_02,V20350_03,V20350_04,V20350_05,V20350_06,V20350_07,V20350_08,V20350_11,V20350_12,V20350_13,V20003,V04080_05,V20004,V20005,V20062,V13013,V13330,V20330_01,V20331_01,V20330_02,V20331_02,Q10004,Q10051,Q10061,Q10062,Q10301,Q10301_052,Q10302,Q10302_052,Q12001,Q12011,Q12011_052,Q12012,Q12012_052,Q12405,Q12016,Q12017,Q12003,Q13003,Q13007,Q13007_052,Q13004,Q13019,Q13020,Q13021,Q13022,Q13023,Q04080_04,Q13011,Q13033,Q11290,Q11291,Q11292,Q11293,Q11296,Q11042,Q11042_052,Q11201,Q11202,Q11211,Q11046,Q11046_052,Q11503_06,Q11504_06,Q11503_12,Q11504_12,Q12120,Q12311,Q12311_052,Q12121,Q12121_052,Q12013,Q12030_005,Q12030_010,Q12030_015,Q12030_020,Q12030_040,Q12030_080,Q12030_160,Q12030_320,Q12314,Q12315,Q12315_052,Q12316,Q12316_052,Q20001_701_01,Q20001_701_10,Q20059,Q20059_052,Q20001,Q20010,Q20051,Q20011,Q20013,Q20350_01,Q20350_02,Q20350_03,Q20350_04,Q20350_05,Q20350_06,Q20350_07,Q20350_08,Q20350_11,Q20350_12,Q20350_13,Q20003,Q04080_05,Q20004,Q20005,Q20062,Q13013,Q13330,Q20330_01,Q20331_01,Q20330_02,Q20331_02,V_RETAIN1,V_RETAIN2,V_RETAIN3,V_RETAIN4,V_RETAIN5,V_RETAIN6,V_RETAIN7,V_RETAIN8,V_RETAIN9,V_RETAIN10)"
                + " VALUES (";
        return sql;
    }

    /**
     * 获取键表头部字段
     * @param tableName 表名
     * @return {@code sql}
     */
    private static String initSqlKeyTable(String tableName) {
        String sql;
        sql = "insert into " + tableName
                + " (D_DATA_ID,D_IYMDHM,D_RYMDHM,D_UPDATE_TIME,D_DATETIME,V01301,V05001,V06001,V07001,V02001,V02301,V_ACODE,V04001,V04002,V04003,V04004,V04005,V13392_005,V13392_010,Q13392_005,Q13392_010,D_SOURCE_ID,V_BBB)"
                + " VALUES (";
        return sql;
    }
    
    /**
     * 处理时间异常报文
     * @param date i下标对应报文观测时间
     * @param obsTime i下标对应报文观测时间(字符串)
     * @param twSiteData i下标对应报文观测对象
     * @param twSiteDatas 报文观测对象集合
     * @param i i下标
     * @return 针对错误的日期格式,从集合中移除。直到返回正确报文观测时间
     */
    private static Date obsTime(Date date, String obsTime, TwSiteData twSiteData, List<TwSiteData> twSiteDatas, int i) {
        if (date == null) {
            twSiteDatas.remove(i);
            infoLogger.error("this station" + twSiteData.getStationId() + " obsTime exception:" + obsTime);
            if (i < twSiteDatas.size()) {
                twSiteData = twSiteDatas.get(i);
                date = TimeUtil.String2Date(twSiteData.getObsTime(), "yyyy-MM-dd HH:mm:ss");
                obsTime(date, obsTime, twSiteData, twSiteDatas, i);
            }
        }
        return date;
    }
}
