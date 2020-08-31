package org.cimiss2.dwp.tools;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


import org.cimiss2.dwp.tools.utils.ConfigurationManager;
import org.cimiss2.dwp.tools.utils.LoadPropertiesFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  ConnectionPoolFactory.java   
 * @Package org.cimiss2.dwp.tools   
 * @Description:    TODO(数据库连接池)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月13日 上午8:43:28   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class ConnectionPoolFactory {
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo");
	private static DruidDataSource dataSourceRDB = null;
	private static DruidDataSource dataSourceCIMISS = null;
	private static DruidDataSource dataSourceFILEINDEX = null;
	private static DruidDataSource dataSourceXUGU = null;
	private static DruidDataSource dataSourceMysql = null;
	private static ConnectionPoolFactory connectionFactory = null;
	private static Properties properties = null;
	static {
		if(properties == null) {
			properties = LoadPropertiesFile.getInstance().getGlobalProperties();
		}
	}
	
	/**
	 * @Title: getInstance 
	 * @Description: TODO(单例模式，返回唯一实例) 
	 * @return 
	 *         : ConnectionPool 连接池对象
	 * @throws：
	 */
	public static synchronized ConnectionPoolFactory getInstance() {
		if(null == connectionFactory) {
			connectionFactory = new ConnectionPoolFactory();
		}
		return connectionFactory;
	}
	
	/**
	 * @throws Exception 
	 * @Title: getConnection 
	 * @Description: TODO(返回数据库连接对象) 
	 * @return
	 * @throws SQLException 
	 *         : DruidPooledConnection    数据库连接对象
	 * @throws:
	 */
	public DruidPooledConnection getConnection(String dbname){
		DruidPooledConnection connection = null;
		if(dbname.equalsIgnoreCase("rdb")) {
			getDruidDataSourceRDB();
			try {
				connection = dataSourceRDB.getConnection();
				//logger.info("获取DruidPooledConnection");
				//connection.setAutoCommit(false);
			} catch (SQLException e) {
				logger.error("\n 创建DruidPooledConnection失败:"+e.getMessage());
				return null;
			}
		}else if (dbname.equalsIgnoreCase("cimiss")) {
			getDruidDataSourceCIMISS();
			try {
				connection = dataSourceCIMISS.getConnection();
				//connection.setAutoCommit(false);
			} catch (SQLException e) {
				logger.error("\n 创建DruidPooledConnection失败:"+e.getMessage());
				return null;
			}
		}else if (dbname.equalsIgnoreCase("fileindex")) {
			getDruidDataSourceFileIndex();
			try {
				connection = dataSourceFILEINDEX.getConnection();
				//logger.info("获取DruidPooledConnection");
			} catch (SQLException e) {
				logger.error("\n 创建DruidPooledConnection失败:"+e.getMessage());
				return null;
			}
			
		} else if (dbname.equalsIgnoreCase("xugu")){
			getDruidDataSourceXugu();
			try {
				connection = dataSourceXUGU.getConnection();
				//logger.info("获取DruidPooledConnection");
			} catch (SQLException e) {
				logger.error("\n 创建DruidPooledConnection失败:"+e.getMessage());
				return null;
			}
		}else if (dbname.equalsIgnoreCase("yanfa")){
			getDruidDataSourceYanfa();
			try {
				connection = dataSourceMysql.getConnection();
				//logger.info("获取DruidPooledConnection");
			} catch (SQLException e) {
				logger.error("\n 创建DruidPooledConnection失败:"+e.getMessage());
				return null;
			}
		}
		else {
			logger.error("\n 非法的"+dbname);
		}		
		
		return connection;
	}
	private void getDruidDataSourceYanfa(){
		try {
			if(dataSourceMysql == null && properties != null) {
				dataSourceMysql = new DruidDataSource();
				// 数据库连接驱动
				dataSourceMysql.setDriverClassName(properties.getProperty("jdbc.driverClassName").trim());
				// 数据库连接  url
				dataSourceMysql.setUrl(properties.getProperty("jdbc.url").trim());
				// 数据库用户名
				dataSourceMysql.setUsername(properties.getProperty("jdbc.username").trim());
				// 数据库用户密码
				dataSourceMysql.setPassword(properties.getProperty("jdbc.password").trim());
				// 配置初始化大小、最小、最大
				dataSourceMysql.setInitialSize(Integer.parseInt(properties.getProperty("jdbc.initialSize").trim()));
				dataSourceMysql.setMinIdle(Integer.parseInt(properties.getProperty("jdbc.minIdle").trim()));
				dataSourceMysql.setMaxActive(Integer.parseInt(properties.getProperty("jdbc.maxActive").trim()));
				// 配置获取连接等待超时的时间
				dataSourceMysql.setMaxWait(Integer.parseInt(properties.getProperty("jdbc.maxWait").trim()));
				// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
				dataSourceMysql.setTimeBetweenEvictionRunsMillis(Integer.parseInt(properties.getProperty("jdbc.timeBetweenEvictionRunsMillis")));
				// 配置一个连接在池中最小生存的时间，单位是毫秒
				dataSourceMysql.setMinEvictableIdleTimeMillis(Integer.parseInt(properties.getProperty("jdbc.minEvictableIdleTimeMillis")));

				dataSourceMysql.setFilters(properties.getProperty("jdbc.filters").trim());

//				dataSourceRDB.setValidationQuery(properties.getProperty("rdb.validationQuery").trim());
				//
				dataSourceMysql.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty("jdbc.testWhileIdle").trim()));
				// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,保险起见还是检测吧
				dataSourceMysql.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("jdbc.testOnBorrow").trim()));
				// 归还连接时执行validationQuery检测连接是否有效
				dataSourceMysql.setTestOnReturn(Boolean.parseBoolean(properties.getProperty("jdbc.testOnReturn").trim()));
				dataSourceMysql.setPoolPreparedStatements(Boolean.parseBoolean(properties.getProperty("jdbc.poolPreparedStatements").trim()));
				dataSourceMysql.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(properties.getProperty("jdbc.maxPoolPreparedStatementPerConnectionSize").trim()));
				dataSourceMysql.setDefaultAutoCommit(Boolean.parseBoolean(properties.getProperty("jdbc.defaultAutoCommit").trim()));

			}
		} catch (Exception e) {
			logger.error("\n 初始化DruidDataSource 失败:" +e.getMessage());
			dataSourceMysql = null;
		}
	}
	private void getDruidDataSourceCIMISS() {
		try {
			if(dataSourceCIMISS == null && properties != null) {
				dataSourceCIMISS = new DruidDataSource();
				// 数据库连接驱动
				dataSourceCIMISS.setDriverClassName(properties.getProperty("cimiss.driverClassName").trim());
				// 数据库连接  url
				dataSourceCIMISS.setUrl(properties.getProperty("cimiss.url").trim());
				// 数据库用户名
				dataSourceCIMISS.setUsername(properties.getProperty("cimiss.username").trim());
				// 数据库用户密码
				dataSourceCIMISS.setPassword(properties.getProperty("cimiss.password").trim());
				// 配置初始化大小、最小、最大
				dataSourceCIMISS.setInitialSize(Integer.parseInt(properties.getProperty("cimiss.initialSize").trim()));
				dataSourceCIMISS.setMinIdle(Integer.parseInt(properties.getProperty("cimiss.minIdle").trim()));
				dataSourceCIMISS.setMaxActive(Integer.parseInt(properties.getProperty("cimiss.maxActive").trim()));
				// 配置获取连接等待超时的时间
				dataSourceCIMISS.setMaxWait(Integer.parseInt(properties.getProperty("cimiss.maxWait").trim()));
				// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
				dataSourceCIMISS.setTimeBetweenEvictionRunsMillis(Integer.parseInt(properties.getProperty("cimiss.timeBetweenEvictionRunsMillis")));
				// 配置一个连接在池中最小生存的时间，单位是毫秒
				dataSourceCIMISS.setMinEvictableIdleTimeMillis(Integer.parseInt(properties.getProperty("cimiss.minEvictableIdleTimeMillis")));
				
				dataSourceCIMISS.setFilters(properties.getProperty("cimiss.filters").trim());

//				dataSourceCIMISS.setValidationQuery(properties.getProperty("cimiss.validationQuery").trim());
				// 
				dataSourceCIMISS.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty("cimiss.testWhileIdle").trim()));
				// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,保险起见还是检测吧
				dataSourceCIMISS.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("cimiss.testOnBorrow").trim()));
				// 归还连接时执行validationQuery检测连接是否有效
				dataSourceCIMISS.setTestOnReturn(Boolean.parseBoolean(properties.getProperty("cimiss.testOnReturn").trim()));
				dataSourceCIMISS.setPoolPreparedStatements(Boolean.parseBoolean(properties.getProperty("cimiss.poolPreparedStatements").trim()));
				dataSourceCIMISS.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(properties.getProperty("cimiss.maxPoolPreparedStatementPerConnectionSize").trim()));
				dataSourceCIMISS.setDefaultAutoCommit(Boolean.parseBoolean(properties.getProperty("cimiss.defaultAutoCommit").trim()));
				
			}
		} catch (Exception e) {
			logger.error("\n 初始化DruidDataSource 失败:" +e.getMessage());
			dataSourceCIMISS = null;
		}
	}
	
	private void getDruidDataSourceRDB(){
		try {
			if(dataSourceRDB == null && properties != null) {
				dataSourceRDB = new DruidDataSource();
				// 数据库连接驱动
				dataSourceRDB.setDriverClassName(properties.getProperty("rdb.driverClassName").trim());
				// 数据库连接  url
				dataSourceRDB.setUrl(properties.getProperty("rdb.url").trim());
				// 数据库用户名
				dataSourceRDB.setUsername(properties.getProperty("rdb.username").trim());
				// 数据库用户密码
				dataSourceRDB.setPassword(properties.getProperty("rdb.password").trim());
				// 配置初始化大小、最小、最大
				dataSourceRDB.setInitialSize(Integer.parseInt(properties.getProperty("rdb.initialSize").trim()));
				dataSourceRDB.setMinIdle(Integer.parseInt(properties.getProperty("rdb.minIdle").trim()));
				dataSourceRDB.setMaxActive(Integer.parseInt(properties.getProperty("rdb.maxActive").trim()));
				// 配置获取连接等待超时的时间
				dataSourceRDB.setMaxWait(Integer.parseInt(properties.getProperty("rdb.maxWait").trim()));
				// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
				dataSourceRDB.setTimeBetweenEvictionRunsMillis(Integer.parseInt(properties.getProperty("rdb.timeBetweenEvictionRunsMillis")));
				// 配置一个连接在池中最小生存的时间，单位是毫秒
				dataSourceRDB.setMinEvictableIdleTimeMillis(Integer.parseInt(properties.getProperty("rdb.minEvictableIdleTimeMillis")));
				
				dataSourceRDB.setFilters(properties.getProperty("rdb.filters").trim());

//				dataSourceRDB.setValidationQuery(properties.getProperty("rdb.validationQuery").trim());
				// 
				dataSourceRDB.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty("rdb.testWhileIdle").trim()));
				// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,保险起见还是检测吧
				dataSourceRDB.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("rdb.testOnBorrow").trim()));
				// 归还连接时执行validationQuery检测连接是否有效
				dataSourceRDB.setTestOnReturn(Boolean.parseBoolean(properties.getProperty("rdb.testOnReturn").trim()));
				dataSourceRDB.setPoolPreparedStatements(Boolean.parseBoolean(properties.getProperty("rdb.poolPreparedStatements").trim()));
				dataSourceRDB.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(properties.getProperty("rdb.maxPoolPreparedStatementPerConnectionSize").trim()));
				dataSourceRDB.setDefaultAutoCommit(Boolean.parseBoolean(properties.getProperty("rdb.defaultAutoCommit").trim()));
				
			}
		} catch (Exception e) {
			logger.error("\n 初始化DruidDataSource 失败:" +e.getMessage());
			dataSourceRDB = null;
		}
	}
	
	private void getDruidDataSourceFileIndex(){
		try {
			if(dataSourceFILEINDEX == null && properties != null) {
				dataSourceFILEINDEX = new DruidDataSource();
				// 数据库连接驱动
				dataSourceFILEINDEX.setDriverClassName(properties.getProperty("fileindex.driverClassName").trim());
				// 数据库连接  url
				dataSourceFILEINDEX.setUrl(properties.getProperty("fileindex.url").trim());
				// 数据库用户名
				dataSourceFILEINDEX.setUsername(properties.getProperty("fileindex.username").trim());
				// 数据库用户密码
				dataSourceFILEINDEX.setPassword(properties.getProperty("fileindex.password").trim());
				// 配置初始化大小、最小、最大
				dataSourceFILEINDEX.setInitialSize(Integer.parseInt(properties.getProperty("fileindex.initialSize").trim()));
				dataSourceFILEINDEX.setMinIdle(Integer.parseInt(properties.getProperty("fileindex.minIdle").trim()));
				dataSourceFILEINDEX.setMaxActive(Integer.parseInt(properties.getProperty("fileindex.maxActive").trim()));
				// 配置获取连接等待超时的时间
				dataSourceFILEINDEX.setMaxWait(Integer.parseInt(properties.getProperty("fileindex.maxWait").trim()));
				// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
				dataSourceFILEINDEX.setTimeBetweenEvictionRunsMillis(Integer.parseInt(properties.getProperty("fileindex.timeBetweenEvictionRunsMillis")));
				// 配置一个连接在池中最小生存的时间，单位是毫秒
				dataSourceFILEINDEX.setMinEvictableIdleTimeMillis(Integer.parseInt(properties.getProperty("fileindex.minEvictableIdleTimeMillis")));
				
				dataSourceFILEINDEX.setFilters(properties.getProperty("fileindex.filters").trim());

//				dataSourceFILEINDEX.setValidationQuery(properties.getProperty("fileindex.validationQuery").trim());
				// 
				dataSourceFILEINDEX.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty("fileindex.testWhileIdle").trim()));
				// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,保险起见还是检测吧
				dataSourceFILEINDEX.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("fileindex.testOnBorrow").trim()));
				// 归还连接时执行validationQuery检测连接是否有效
				dataSourceFILEINDEX.setTestOnReturn(Boolean.parseBoolean(properties.getProperty("fileindex.testOnReturn").trim()));
				dataSourceFILEINDEX.setPoolPreparedStatements(Boolean.parseBoolean(properties.getProperty("fileindex.poolPreparedStatements").trim()));
				dataSourceFILEINDEX.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(properties.getProperty("fileindex.maxPoolPreparedStatementPerConnectionSize").trim()));
				//dataSourceFILEINDEX.setDefaultAutoCommit(Boolean.parseBoolean(properties.getProperty("fileindex.defaultAutoCommit").trim()));
				
			}
		} catch (Exception e) {
			logger.error("\n 初始化DruidDataSource 失败:" +e.getMessage());
			dataSourceFILEINDEX = null;
		}
	}

	private void getDruidDataSourceXugu(){
		try {
			if(dataSourceXUGU == null && properties != null) {
				dataSourceXUGU = new DruidDataSource();
				// 数据库连接驱动
				dataSourceXUGU.setDriverClassName(properties.getProperty("xugu.driverClassName").trim());
				// 数据库连接  url
				dataSourceXUGU.setUrl(properties.getProperty("xugu.url").trim());
				// 数据库用户名
				dataSourceXUGU.setUsername(properties.getProperty("xugu.username").trim());
				// 数据库用户密码
				dataSourceXUGU.setPassword(properties.getProperty("xugu.password").trim());
				// 配置初始化大小、最小、最大
				dataSourceXUGU.setInitialSize(Integer.parseInt(properties.getProperty("xugu.initialSize").trim()));
				dataSourceXUGU.setMinIdle(Integer.parseInt(properties.getProperty("xugu.minIdle").trim()));
				dataSourceXUGU.setMaxActive(Integer.parseInt(properties.getProperty("xugu.maxActive").trim()));
				// 配置获取连接等待超时的时间
				dataSourceXUGU.setMaxWait(Integer.parseInt(properties.getProperty("xugu.maxWait").trim()));
				// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
				dataSourceXUGU.setTimeBetweenEvictionRunsMillis(Integer.parseInt(properties.getProperty("xugu.timeBetweenEvictionRunsMillis")));
				// 配置一个连接在池中最小生存的时间，单位是毫秒
				dataSourceXUGU.setMinEvictableIdleTimeMillis(Integer.parseInt(properties.getProperty("xugu.minEvictableIdleTimeMillis")));
				
//				dataSourceXUGU.setFilters(properties.getProperty("xugu.filters").trim()); //xugu加此行报错

//				dataSourceXUGU.setValidationQuery(properties.getProperty("xugu.validationQuery").trim());
				 
				dataSourceXUGU.setTestWhileIdle(Boolean.parseBoolean(properties.getProperty("xugu.testWhileIdle").trim()));
				// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,保险起见还是检测吧
				dataSourceXUGU.setTestOnBorrow(Boolean.parseBoolean(properties.getProperty("xugu.testOnBorrow").trim()));
				// 归还连接时执行validationQuery检测连接是否有效
				dataSourceXUGU.setTestOnReturn(Boolean.parseBoolean(properties.getProperty("xugu.testOnReturn").trim()));
				dataSourceXUGU.setPoolPreparedStatements(Boolean.parseBoolean(properties.getProperty("xugu.poolPreparedStatements").trim()));
				dataSourceXUGU.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(properties.getProperty("xugu.maxPoolPreparedStatementPerConnectionSize").trim()));
				//dataSourceXUGU.setDefaultAutoCommit(Boolean.parseBoolean(properties.getProperty("xugu.defaultAutoCommit").trim()));	
			}
		} catch (Exception e) {
			logger.error("\n 初始化DruidDataSource 失败:" +e.getMessage());
			dataSourceXUGU = null;
		}
	}
	
	public static void main(String[] args) throws Exception{
		System.out.println(ConnectionPoolFactory.getInstance().getConnection("xugu"));
		Connection connection = ConnectionPoolFactory.getInstance().getConnection("xugu");
		PreparedStatement pstm = connection.prepareStatement("select * from usr_sod.test_name");
		ResultSet rs = pstm.executeQuery();
		System.out.println(ConfigurationManager.getJarSuperPath());

		/*PreparedStatement ps = new LoggableStatement(connection,"select * from usr_sod.test_name");
		ResultSet rs = ps.executeQuery();*/
		while(rs.next()){
			String fname = rs.getNString("name");
			System.out.println("the fname is " + fname);
		}
	}

}
