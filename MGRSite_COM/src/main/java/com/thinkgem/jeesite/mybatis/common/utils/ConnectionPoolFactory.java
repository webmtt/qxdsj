package com.thinkgem.jeesite.mybatis.common.utils;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.thinkgem.jeesite.mybatis.common.config.Global;

public class ConnectionPoolFactory {
	public static final Logger logger = LoggerFactory.getLogger("ConnectionPoolFactory");
	private static DruidDataSource dataSourceXUGU = null;
	private static DruidDataSource dataSourceXUGU2 = null;
	private static ConnectionPoolFactory connectionFactory = null;
	private static JdbcTemplate jdbcTemplate = null;
	private static JdbcTemplate jdbcTemplate2 = null;
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
	

	public DruidPooledConnection getConnection(String dbname){
		DruidPooledConnection connection = null;
		switch (dbname.toUpperCase()) {
		case "XUGU":
			getDruidDataSourceXugu();
			try {
				connection = dataSourceXUGU.getConnection();
			} catch (SQLException e) {
				logger.error("\n 创建DruidPooledConnection失败:"+e.getMessage());
				return null;
			}
			break;
		case "XUGU2":
			getDruidDataSourceXugu2();
			try {
				connection = dataSourceXUGU.getConnection();
			} catch (SQLException e) {
				logger.error("\n 创建DruidPooledConnection失败:"+e.getMessage());
				return null;
			}
			break;
		default:
			break;
		}
		return connection;
	}
	
	
	public JdbcTemplate getJdbcTemplate(String dbsource){
		if("xugu".equals(dbsource)) {
			getDruidDataSourceXugu();
			if (jdbcTemplate == null) {
				jdbcTemplate = new JdbcTemplate(dataSourceXUGU);
			}
			return jdbcTemplate;
		}else{
			getDruidDataSourceXugu2();
			if(jdbcTemplate2==null){
				jdbcTemplate2=new JdbcTemplate(dataSourceXUGU2);
			}
			return jdbcTemplate2;
		}

	}

	private void getDruidDataSourceXugu2(){
		try {
			if(dataSourceXUGU2 == null) {
				dataSourceXUGU2 = new DruidDataSource();
				// 数据库连接驱动
				dataSourceXUGU2.setDriverClassName( Global.getConfig("xugu.driverClassName").trim());
				// 数据库连接  url
				dataSourceXUGU2.setUrl(Global.getConfig("xugu2.url").trim());
				// 数据库用户名
				dataSourceXUGU2.setUsername(Global.getConfig("xugu2.username").trim());
				// 数据库用户密码
				dataSourceXUGU2.setPassword(Global.getConfig("xugu2.password").trim());
				// 配置初始化大小、最小、最大
				dataSourceXUGU2.setInitialSize(Integer.parseInt(Global.getConfig("xugu.initialSize").trim()));
				dataSourceXUGU2.setMinIdle(Integer.parseInt(Global.getConfig("xugu.minIdle").trim()));
				dataSourceXUGU2.setMaxActive(Integer.parseInt(Global.getConfig("xugu.maxActive").trim()));
				// 配置获取连接等待超时的时间
				dataSourceXUGU2.setMaxWait(Integer.parseInt(Global.getConfig("xugu.maxWait").trim()));
				// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
				dataSourceXUGU2.setTimeBetweenEvictionRunsMillis(Integer.parseInt(Global.getConfig("xugu.timeBetweenEvictionRunsMillis")));
				// 配置一个连接在池中最小生存的时间，单位是毫秒
				dataSourceXUGU2.setMinEvictableIdleTimeMillis(Integer.parseInt(Global.getConfig("xugu.minEvictableIdleTimeMillis")));
				
//				dataSourceXUGU2.setFilters(Global.getConfig("xugu.filters").trim()); //xugu加此行报错

//				dataSourceXUGU2.setValidationQuery(Global.getConfig("xugu.validationQuery").trim());

				dataSourceXUGU2.setTestWhileIdle(Boolean.parseBoolean(Global.getConfig("xugu.testWhileIdle").trim()));
				// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,保险起见还是检测吧
				dataSourceXUGU2.setTestOnBorrow(Boolean.parseBoolean(Global.getConfig("xugu.testOnBorrow").trim()));
				// 归还连接时执行validationQuery检测连接是否有效
				dataSourceXUGU2.setTestOnReturn(Boolean.parseBoolean(Global.getConfig("xugu.testOnReturn").trim()));
				dataSourceXUGU2.setPoolPreparedStatements(Boolean.parseBoolean(Global.getConfig("xugu.poolPreparedStatements").trim()));
				dataSourceXUGU2.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(Global.getConfig("xugu.maxPoolPreparedStatementPerConnectionSize").trim()));
				//dataSourceXUGU.setDefaultAutoCommit(Boolean.parseBoolean(Global.getConfig("xugu.defaultAutoCommit").trim()));	
			}
		} catch (Exception e) {
			logger.error("\n 初始化DruidDataSource 失败:" +e.getMessage());
			dataSourceXUGU2 = null;
		}
	}
	private void getDruidDataSourceXugu(){
		try {
			if(dataSourceXUGU == null) {
				dataSourceXUGU = new DruidDataSource();
				// 数据库连接驱动
				dataSourceXUGU.setDriverClassName( Global.getConfig("xugu.driverClassName").trim());
				// 数据库连接  url
				dataSourceXUGU.setUrl(Global.getConfig("xugu.url").trim());
				// 数据库用户名
				dataSourceXUGU.setUsername(Global.getConfig("xugu.username").trim());
				// 数据库用户密码
				dataSourceXUGU.setPassword(Global.getConfig("xugu.password").trim());
				// 配置初始化大小、最小、最大
				dataSourceXUGU.setInitialSize(Integer.parseInt(Global.getConfig("xugu.initialSize").trim()));
				dataSourceXUGU.setMinIdle(Integer.parseInt(Global.getConfig("xugu.minIdle").trim()));
				dataSourceXUGU.setMaxActive(Integer.parseInt(Global.getConfig("xugu.maxActive").trim()));
				// 配置获取连接等待超时的时间
				dataSourceXUGU.setMaxWait(Integer.parseInt(Global.getConfig("xugu.maxWait").trim()));
				// 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
				dataSourceXUGU.setTimeBetweenEvictionRunsMillis(Integer.parseInt(Global.getConfig("xugu.timeBetweenEvictionRunsMillis")));
				// 配置一个连接在池中最小生存的时间，单位是毫秒
				dataSourceXUGU.setMinEvictableIdleTimeMillis(Integer.parseInt(Global.getConfig("xugu.minEvictableIdleTimeMillis")));

//				dataSourceXUGU.setFilters(Global.getConfig("xugu.filters").trim()); //xugu加此行报错

//				dataSourceXUGU.setValidationQuery(Global.getConfig("xugu.validationQuery").trim());

				dataSourceXUGU.setTestWhileIdle(Boolean.parseBoolean(Global.getConfig("xugu.testWhileIdle").trim()));
				// 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能,保险起见还是检测吧
				dataSourceXUGU.setTestOnBorrow(Boolean.parseBoolean(Global.getConfig("xugu.testOnBorrow").trim()));
				// 归还连接时执行validationQuery检测连接是否有效
				dataSourceXUGU.setTestOnReturn(Boolean.parseBoolean(Global.getConfig("xugu.testOnReturn").trim()));
				dataSourceXUGU.setPoolPreparedStatements(Boolean.parseBoolean(Global.getConfig("xugu.poolPreparedStatements").trim()));
				dataSourceXUGU.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(Global.getConfig("xugu.maxPoolPreparedStatementPerConnectionSize").trim()));
				//dataSourceXUGU.setDefaultAutoCommit(Boolean.parseBoolean(Global.getConfig("xugu.defaultAutoCommit").trim()));
			}
		} catch (Exception e) {
			logger.error("\n 初始化DruidDataSource 失败:" +e.getMessage());
			dataSourceXUGU = null;
		}
	}
	public static void main(String[] args) {
//		System.out.println(ConnectionPoolFactory.getInstance().getConnection("xugu"));
//		List<Map<String, Object>> list = ConnectionPoolFactory.getInstance().getJdbcTemplate().queryForList("select * from usr_sod.test_name");
//		  for(Map<String,Object> map : list){
//			  map.forEach((k,v)-> System.out.println("key:value = " + k + ":" + v));
//		  }
		DruidPooledConnection connect=ConnectionPoolFactory.getInstance().getConnection("xugu2");

	}

}
