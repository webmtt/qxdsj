package org.cimiss2.dwp.util;

import cma.cimiss2.dpc.decoder.tools.utils.DecodeConstant;
import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: cimiss2-decode
 * @描述
 * @创建人 zzj
 * @创建时间 2019/7/5 15:16
 */
public class SqlConnUtil {

    private static final Logger logger = LoggerFactory.getLogger(SqlConnUtil.class);
    //存放DruidDataSource
    public static ConcurrentHashMap<String, DruidDataSource> connMap = new ConcurrentHashMap<>(10);
    private static Properties prop = PropertiesUtil.getInstance().getProperties(DecodeConstant.PATH + "config/db.properties");

    public static  Connection getConnection(String  identify) {
        Connection conn = null;
        try {
            if(null!=connMap.get(identify)){
                DruidDataSource druidDataSource=connMap.get(identify);
                conn=druidDataSource.getConnection();
            }else {
                synchronized (identify){
                    if(DecodeConstant.RDB.equals(identify)){
                        DruidDataSource druidDataSource=getRdbDataSource();
                        conn=druidDataSource.getConnection();
                    }
                    if(DecodeConstant.FILE_INEX.equals(identify)){
                        DruidDataSource druidDataSource=getFileIndexdruidDataSource();
                        conn=druidDataSource.getConnection();
                    }
                }

            }
        } catch (SQLException e) {
            logger.info("获取数据库链接错误{}",ExceptionUtil.getException(e));
            throw new RuntimeException(e);
        }
        return conn;

    }

        //获取结构化数据
    public static DruidDataSource getRdbDataSource() {
        try {
            DruidDataSource druidDataSource = new DruidDataSource();
            // 数据库连接驱动
            druidDataSource.setDriverClassName(prop.getProperty("rdb.driverClassName"));
            // 数据库连接  url
            druidDataSource.setUrl(prop.getProperty("rdb.url").trim());
            // 数据库用户名
            druidDataSource.setUsername(prop.getProperty("rdb.username").trim());
            // 数据库用户密码
            druidDataSource.setPassword(prop.getProperty("rdb.password").trim());
            // 配置初始化大小、最小、最大
            druidDataSource.setInitialSize(Integer.parseInt(prop.getProperty("rdb.initialSize").trim()));
            druidDataSource.setMinIdle(Integer.parseInt(prop.getProperty("rdb.minIdle").trim()));
            druidDataSource.setMaxActive(Integer.parseInt(prop.getProperty("rdb.maxActive").trim()));
            // 配置获取连接等待超时的时间
            druidDataSource.setMaxWait(Integer.parseInt(prop.getProperty("rdb.maxWait").trim()));
            // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
            druidDataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(prop.getProperty("rdb.timeBetweenEvictionRunsMillis")));
            // 配置一个连接在池中最小生存的时间，单位是毫秒
            druidDataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(prop.getProperty("rdb.minEvictableIdleTimeMillis")));

            druidDataSource.setFilters(prop.getProperty("rdb.filters").trim());

            //druidDataSource.setValidationQuery(prop.getProperty("rdb.validationQuery").trim());
            //
            druidDataSource.setTestWhileIdle(Boolean.parseBoolean(prop.getProperty("rdb.testWhileIdle").trim()));
            // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,保险起见还是检测吧
            druidDataSource.setTestOnBorrow(Boolean.parseBoolean(prop.getProperty("rdb.testOnBorrow").trim()));
            // 归还连接时执行validationQuery检测连接是否有效
            druidDataSource.setTestOnReturn(Boolean.parseBoolean(prop.getProperty("rdb.testOnReturn").trim()));
            druidDataSource.setPoolPreparedStatements(Boolean.parseBoolean(prop.getProperty("rdb.poolPreparedStatements").trim()));
            druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(prop.getProperty("rdb.maxPoolPreparedStatementPerConnectionSize").trim()));
            druidDataSource.setDefaultAutoCommit(Boolean.parseBoolean(prop.getProperty("rdb.defaultAutoCommit").trim()));
            connMap.put(DecodeConstant.RDB,druidDataSource);
            return druidDataSource;
        } catch (Exception e) {
            logger.error("初始化结构化数据库失败:{}" + ExceptionUtil.getException(e));
            throw new RuntimeException(e);

        }
    }

    public static DruidDataSource getFileIndexdruidDataSource(){
        try {
            DruidDataSource druidDataSource = new DruidDataSource();

                druidDataSource = new DruidDataSource();
                // 数据库连接驱动
                druidDataSource.setDriverClassName(prop.getProperty("fileindex.driverClassName").trim());
                // 数据库连接  url
                druidDataSource.setUrl(prop.getProperty("fileindex.url").trim());
                // 数据库用户名
                druidDataSource.setUsername(prop.getProperty("fileindex.username").trim());
                // 数据库用户密码
                druidDataSource.setPassword(prop.getProperty("fileindex.password").trim());
                // 配置初始化大小、最小、最大
                druidDataSource.setInitialSize(Integer.parseInt(prop.getProperty("fileindex.initialSize").trim()));
                druidDataSource.setMinIdle(Integer.parseInt(prop.getProperty("fileindex.minIdle").trim()));
                druidDataSource.setMaxActive(Integer.parseInt(prop.getProperty("fileindex.maxActive").trim()));
                // 配置获取连接等待超时的时间
                druidDataSource.setMaxWait(Integer.parseInt(prop.getProperty("fileindex.maxWait").trim()));
                // 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
                druidDataSource.setTimeBetweenEvictionRunsMillis(Integer.parseInt(prop.getProperty("fileindex.timeBetweenEvictionRunsMillis")));
                // 配置一个连接在池中最小生存的时间，单位是毫秒
                druidDataSource.setMinEvictableIdleTimeMillis(Integer.parseInt(prop.getProperty("fileindex.minEvictableIdleTimeMillis")));

                druidDataSource.setFilters(prop.getProperty("fileindex.filters").trim());

                //druidDataSource.setValidationQuery(prop.getProperty("fileindex.validationQuery").trim());
                //
                druidDataSource.setTestWhileIdle(Boolean.parseBoolean(prop.getProperty("fileindex.testWhileIdle").trim()));
                // 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,保险起见还是检测吧
                druidDataSource.setTestOnBorrow(Boolean.parseBoolean(prop.getProperty("fileindex.testOnBorrow").trim()));
                // 归还连接时执行validationQuery检测连接是否有效
                druidDataSource.setTestOnReturn(Boolean.parseBoolean(prop.getProperty("fileindex.testOnReturn").trim()));
                druidDataSource.setPoolPreparedStatements(Boolean.parseBoolean(prop.getProperty("fileindex.poolPreparedStatements").trim()));
                druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(prop.getProperty("fileindex.maxPoolPreparedStatementPerConnectionSize").trim()));
            connMap.put(DecodeConstant.FILE_INEX,druidDataSource);
            return druidDataSource;

        } catch (Exception e) {
            logger.error("初始化文件索引数据库失败:{}" + ExceptionUtil.getException(e));
            throw new RuntimeException(e);
        }
    }



}
