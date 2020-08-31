package cma.cimiss2.dpc.decoder.grib;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 功能:数据库连接池管理类
 * @author 国家气象信息中心
 * @Date 2018-10-25
 *
 */
public class GribDBUtil {
	public final static String cimiss2_dwp_decode_rdb_cfg = "config/cimiss2_dwp_decode_grib_rdb.xml";
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	private HashMap<String, ComboPooledDataSource> rdbmsDataSourceCache = new HashMap<String, ComboPooledDataSource>();

	public static GribDBUtil INSTANCE = null;

	/**
	 * 私有构造函数
	 */
	private GribDBUtil() {
		initDataSourceCache();
	}

	/**
	 * 单例类
	 * @return
	 */
	public static synchronized GribDBUtil getINSTANCE() {
		if (null == INSTANCE) {
			INSTANCE = new GribDBUtil();
		}
		return INSTANCE;
	}

	/**
	 * 设定c3p0数据库连接池默认配置文件路径.
	 * 因为需要对数据库连接池配置参数进行设置，所有将c3p0连接池的默认配置文件设定为:config/cimiss2_dwp_decode_grib_rdb.xml
	 */
	private void initDataSourceCache() {
		System.setProperty("com.mchange.v2.c3p0.cfg.xml", GribDBUtil.cimiss2_dwp_decode_rdb_cfg);
	}

	/**
	 * 
	 * 用于多个数据库连接池的管理，首测获取连接池时进行初始化，并存放于缓存。
	 * @param configName c3p0配置的named-config值
	 * @return
	 */
	private DataSource getDataSource(String configName) {
		ComboPooledDataSource dataSource = null;
		if (!rdbmsDataSourceCache.containsKey(configName)) {
			try {
				dataSource = new ComboPooledDataSource(configName);
				logger.info("=================="+dataSource.getJdbcUrl() + "   " + dataSource.getUser() + " ");
				String decodePassword =  dataSource.getPassword();
				dataSource.setPassword(Base64Util.getPwd(decodePassword)); //modify,2020.5.28:将密码转换成明文
				//logger.info("=================="+dataSource.getJdbcUrl() + "   " + dataSource.getUser() + " " + dataSource.getPassword());
				dataSource.setLoginTimeout(20);
				java.sql.Connection con = dataSource.getConnection();
				System.out.println(con);
				con.close();
				rdbmsDataSourceCache.put(configName, dataSource);
				logger.debug("数据库连接池:named-config为{}的连接池初始化成功,放入缓存.",configName);			
			} catch (Exception e) {
				logger_e.error("数据库连接池:named-config为{}的连接池初始化失败,检查配置{}.",configName,GribDBUtil.cimiss2_dwp_decode_rdb_cfg,e);
			}
		} else {
			logger.debug("数据库连接池:named-config为{}的连接池已经缓存.",configName);
			dataSource = rdbmsDataSourceCache.get(configName);
		}
		return dataSource;
	}

	/**
	 * 获取数据库连接
	 * @param database_id c3p0配置的named-config值
	 * @return
	 */
	public Connection getConnection(String database_id) {
		java.sql.Connection conn = null;
		ComboPooledDataSource datasource = (ComboPooledDataSource) getDataSource(database_id);
		try {
			conn = datasource.getConnection();
		} catch (SQLException e) {
			logger_e.error("数据库连接池:named-config为{}的连接池无法分配数据库连接." + database_id,e);
		}
		return conn;
	}
	
	public static void main(String[] arges) {
		System.out.println(GribDBUtil.getINSTANCE().getDataSource("mysqldb"));
//		System.out.println(GribDBUtil.getINSTANCE().getDataSource("mysqldb"));
//		System.out.println(GribDBUtil.getINSTANCE().getDataSource("mysqldb"));
//		System.out.println(GribDBUtil.getINSTANCE().getDataSource("mysqldb"));
//		System.out.println(GribDBUtil.getINSTANCE().getDataSource("mysqldb1"));
	}
}
