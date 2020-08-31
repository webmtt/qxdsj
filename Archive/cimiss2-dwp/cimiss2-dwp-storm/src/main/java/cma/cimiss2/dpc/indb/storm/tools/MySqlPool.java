package cma.cimiss2.dpc.indb.storm.tools;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.util.StringUtil;

/**
 * <br>
 * @Title:  MySqlC.java
 * @Package com.hitec.dbsync.common.config.datasource
 * @Description:  Mysql数据库连接池
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月19日 下午2:01:18   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class MySqlPool {
	public static final Logger Log = LoggerFactory.getLogger("loggerInfo");
	/**
	 * @Fields DRIVER_CLASS_NAME_KEY : 数据库驱动名称
	 */
	private static final String DRIVER_CLASS_NAME_KEY = "driverClassName";
	/**
	 * @Fields URL_KEY : 数据库连接url
	 */
	private static final String URL_KEY = "url";
	/**
	 * @Fields USER_NAME_KEY : 数据库连接用户名
	 */
	private static final String USER_NAME_KEY = "username";
	/**
	 * @Fields PASSWORD_KEY : 数据库连接密码
	 */
	private static final String PASSWORD_KEY = "password";
	/**
	 * @Fields FILTERS_KEY : 数据库连接过滤器
	 */
	private static final String FILTERS_KEY = "filters";
	/**
	 * @Fields INITIAL_SIZE_KEY : 数据库连接初始化大小
	 */
	private static final String INITIAL_SIZE_KEY = "initialSize";
	/**
	 * @Fields MIN_IDLE_KEY : 数据库连接最小连接数
	 */
	private static final String MIN_IDLE_KEY = "minIdle";
	/**
	 * @Fields MAX_ACTIVE_KEY : 数据库最大连接数
	 */
	private static final String MAX_ACTIVE_KEY = "maxActive";
	/**
	 * @Fields MAX_WAIT_KEY : 数据库连接最大等待时间
	 */
	private static final String MAX_WAIT_KEY = "maxWait";
	private static final String TIME_BETWEEN_EVICTION_RUNS_MILLIS_KEY = "timeBetweenEvictionRunsMillis";
	private static final String MIN_EVICTABLE_IDLE_TIME_MILLIS_KEY = "minEvictableIdleTimeMillis";
	private static final String VALIDATION_QUERY_KEY = "validationQuery";
	private static final String TEST_WHILE_IDLE_KEY = "testWhileIdle";
	private static final String TEST_ON_BORROW_KEY = "testOnBorrow";
	private static final String TEST_ON_RETURN_KEY = "testOnReturn";
	private static final String POOL_PREPARED_STATEMENTS_KEY = "poolPreparedStatements";
	private static final String MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE_KEY = "maxPoolPreparedStatementPerConnectionSize";
	private static final String DEFAULT_AUTO_COMMIT_KEY = "defaultAutoCommit";

	private static Map<String, DruidDataSource> dataSource = new HashMap<String, DruidDataSource>();

	/**
	 * @Title:  MySqlC
	 * @Description: 私有化构造方法
	 */
	private MySqlPool() {
		this.initDataSource();
	}

	/**
	 * @Title: initDataSource
	 * @Description: 初始化目标数据库配置
	 */
	private void initDataSource() {
		InputStream is = null;
		try {
//			is = new FileInputStream(System.getProperty("user.dir") + "/config/bufr/mysql.ini");
			is = new FileInputStream(StringUtil.getConfigPath() + "bufr/mysql.ini");
			IniReader ir = new IniReader(is);
			Map<String, Properties> dbConfigMap = ir.get();
			Iterator<Entry<String, Properties>> dbConfigIt = dbConfigMap.entrySet().iterator();
			while (dbConfigIt.hasNext()) {
				Entry<String, Properties> next = dbConfigIt.next();
				String dbName = next.getKey();
				Properties dbConfig = next.getValue();
				DruidDataSource dds = new DruidDataSource();
				dds.setDriverClassName(dbConfig.getProperty(DRIVER_CLASS_NAME_KEY));
				dds.setUrl(dbConfig.getProperty(URL_KEY));
				dds.setUsername(dbConfig.getProperty(USER_NAME_KEY));
				dds.setPassword(dbConfig.getProperty(PASSWORD_KEY));
				dds.setFilters(dbConfig.getProperty(FILTERS_KEY));
				dds.setInitialSize(Integer.parseInt(dbConfig.getProperty(INITIAL_SIZE_KEY, DruidDataSource.DEFAULT_INITIAL_SIZE + "")));
				dds.setMinIdle(Integer.parseInt(dbConfig.getProperty(MIN_IDLE_KEY, DruidDataSource.DEFAULT_MIN_IDLE + "")));
				dds.setMaxActive(Integer.parseInt(dbConfig.getProperty(MAX_ACTIVE_KEY, DruidDataSource.DEFAULT_MAX_ACTIVE_SIZE + "")));
				dds.setMaxWait(Long.parseLong(dbConfig.getProperty(MAX_WAIT_KEY, DruidDataSource.DEFAULT_MAX_WAIT + "")));
				dds.setTimeBetweenEvictionRunsMillis(
						Long.parseLong(dbConfig.getProperty(TIME_BETWEEN_EVICTION_RUNS_MILLIS_KEY, DruidDataSource.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS + "")));
				dds.setMinEvictableIdleTimeMillis(
						Long.parseLong(dbConfig.getProperty(MIN_EVICTABLE_IDLE_TIME_MILLIS_KEY, DruidDataSource.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS + "")));
				dds.setValidationQuery(dbConfig.getProperty(VALIDATION_QUERY_KEY, DruidDataSource.DEFAULT_VALIDATION_QUERY));
				dds.setTestWhileIdle(Boolean.parseBoolean(dbConfig.getProperty(TEST_WHILE_IDLE_KEY, "true")));
				dds.setTestOnBorrow(Boolean.parseBoolean(dbConfig.getProperty(TEST_ON_BORROW_KEY, DruidDataSource.DEFAULT_TEST_ON_BORROW + "")));
				dds.setTestOnReturn(Boolean.parseBoolean(dbConfig.getProperty(TEST_ON_RETURN_KEY, DruidDataSource.DEFAULT_TEST_ON_RETURN + "")));
				dds.setPoolPreparedStatements(Boolean.parseBoolean(dbConfig.getProperty(POOL_PREPARED_STATEMENTS_KEY, "true")));
				dds.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(dbConfig.getProperty(MAX_POOL_PREPARED_STATEMENT_PER_CONNECTION_SIZE_KEY)));
				dds.setDefaultAutoCommit(Boolean.parseBoolean(dbConfig.getProperty(DEFAULT_AUTO_COMMIT_KEY, "true")));
				dataSource.put(dbName, dds);
			}
		} catch (IOException e) {
			Log.error("加载配置文件mysql.ini失败", e);
		} catch (SQLException e) {
			Log.error("配置数据库连接失败", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static synchronized DruidPooledConnection getConnection(String dbName) {
		DruidPooledConnection connection = null;
		if (dataSource == null || dataSource.size() == 0) {
			new MySqlPool();
		}
		DruidDataSource druidDataSource = dataSource.get(dbName);
		if (druidDataSource == null) {
			Log.error("无效的DBName:" + dbName);
		} else {
			try {
				connection = druidDataSource.getConnection();
			} catch (SQLException e) {
				Log.error("获取数据库连接失败:" + dbName, e);
			}
		}
		return connection;
	}

	
}

