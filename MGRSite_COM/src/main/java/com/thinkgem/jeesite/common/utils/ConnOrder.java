package com.thinkgem.jeesite.common.utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Logger;

public class ConnOrder {
	/*String mysql_ftp_jdbc_driver;
	String mysql_ftp_jdbc_url;
	String mysql_ftp_jdbc_user;
	String mysql_ftp_jdbc_pwd;*/
	private static Logger logger = Logger.getLogger("ConnOrder.class");
	
	public static void main(String[] args) {
		ConnOrder connOrder=new ConnOrder();
		System.out.println(connOrder.getProperties("oracle_CDC.jdbc.url"));
	}
	
	public Connection getFTPConnection() {
		/*if(Mysql_ftp_jdbc.mysql_ftp_jdbc_driver==null||Mysql_ftp_jdbc.mysql_ftp_jdbc_pwd==null||Mysql_ftp_jdbc.mysql_ftp_jdbc_url==null||Mysql_ftp_jdbc.mysql_ftp_jdbc_user==null){
			Mysql_ftp_jdbc.mysql_ftp_jdbc_driver = getProperties("mysql_ftp.jdbc.driver");
			Mysql_ftp_jdbc.mysql_ftp_jdbc_url = getProperties("mysql_ftp.jdbc.url");
			Mysql_ftp_jdbc.mysql_ftp_jdbc_user = getProperties("mysql_ftp.jdbc.user");
			Mysql_ftp_jdbc.mysql_ftp_jdbc_pwd = getProperties("mysql_ftp.jdbc.pwd");
		}*/
		String driver = getProperties("mysql_ftp.jdbc.driver");
		String url = getProperties("mysql_ftp.jdbc.url");
		String user = getProperties("mysql_ftp.jdbc.user");
		String pwd = getProperties("mysql_ftp.jdbc.pwd");
		//Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.86.117:3306/ftp?useUnicode=true&characterEncoding=utf-8","ftp","nmc601ABC");
		return getConn(driver,url,user,pwd);
		//return getConn(Mysql_ftp_jdbc.mysql_ftp_jdbc_driver,Mysql_ftp_jdbc.mysql_ftp_jdbc_url,Mysql_ftp_jdbc.mysql_ftp_jdbc_user,Mysql_ftp_jdbc.mysql_ftp_jdbc_pwd);
	}
	
	public  Connection getConnection() {
		String driver = getProperties("mysql.jdbc.driver");
		String url = getProperties("mysql.jdbc.url");
		String user = getProperties("mysql.jdbc.user");
		String pwd = getProperties("mysql.jdbc.pwd");
		//Connection connection = DriverManager.getConnection("jdbc:mysql://10.0.86.117:3306/cdcv4?useServerPrepStmts=true&cachePrepStmts=true&prepStmtCacheSize=30&prepStmtCacheSqlLimit=256&useUnicode=true&characterEncoding=utf-8","cdc","cdc@2015");
		return getConn(driver,url,user,pwd);
	}
	
	public  Connection getCIMISSOracleConnection() {
		String driver = getProperties("oracle.jdbc.driver");
		String url = getProperties("oracle.jdbc.url");
		String user = getProperties("oracle.jdbc.user");
		String pwd = getProperties("oracle.jdbc.pwd");
		return getConn(driver,url,user,pwd);
	}
	
	public  Connection getCDCOracleConnection() {
		String driver = getProperties("oracle_CDC.jdbc.driver");
		String url = getProperties("oracle_CDC.jdbc.url");
		String user = getProperties("oracle_CDC.jdbc.user");
		String pwd = getProperties("oracle_CDC.jdbc.pwd");
		return getConn(driver,url,user,pwd);
	}
	
	public static Connection getTestOracleConnection() {
		String driver = getProperties("oracle_test.jdbc.driver");
		String url = getProperties("oracle_test.jdbc.url");
		String user = getProperties("oracle_test.jdbc.user");
		String pwd = getProperties("oracle_test.jdbc.pwd");
		return getConn(driver,url,user,pwd);
	}
	public static Connection getMcpOracleConnection() {
		String driver = getProperties("jdbc.driver_mdb");
		String url = getProperties("jdbc.url_mdb");
		String user = getProperties("jdbc.username_mdb");
		String pwd = getProperties("jdbc.password_mdb");
		return getConn(driver,url,user,pwd);
	}
	
	public static Connection getConn(String driver,String url,String user,String pwd){
		try {
			Class.forName(driver);
			Connection connection = DriverManager.getConnection(url,user,pwd);
			return connection;
		} catch (Exception e) {
			logger.info("get connection error");
			e.printStackTrace();
		}
		return null;
	}
	
	private static String getProperties(String key){
		Properties prop = new Properties();
		try {
			logger.info("===================read Properties=====================");
			prop.load(ConnOrder.class.getResourceAsStream("/jdbc.properties"));
		} catch (IOException e) {
			logger.info("load jdbc.properties occur error,please check it");
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}
	
}
