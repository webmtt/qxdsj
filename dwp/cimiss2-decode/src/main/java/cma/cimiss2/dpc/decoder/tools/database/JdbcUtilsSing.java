package cma.cimiss2.dpc.decoder.tools.database;

import org.dom4j.Document;

import cma.cimiss2.dpc.decoder.tools.xml.XmlOper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class JdbcUtilsSing {
	private String url;
	private String user;
	private String password;

	// private static JdbcUtilsSing3 instance = new JdbcUtilsSing3();
	private static JdbcUtilsSing instance = null;

	private JdbcUtilsSing(String path) {
		Document doc = XmlOper.getDoc(path);
		url = XmlOper.getElementV(doc, "//config//mysql//url");
		user = XmlOper.getElementV(doc, "//config//mysql//user");
		password = XmlOper.getElementV(doc, "//config//mysql//password");
	}

	public static JdbcUtilsSing getInstance(String path) {
		if (instance == null) {
			synchronized (JdbcUtilsSing.class) {
				instance = new JdbcUtilsSing(path);
				System.out.println("========获取单例对象==================");
			}
		}
		return instance;
	}

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Class.forName("com.mysql.jdbc.Driver");
			System.out.println("=================加载驱动类=====================");
		} catch (ClassNotFoundException e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}

	public void free(PreparedStatement st, Connection conn) {

		try {
			if (st != null)
				st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
}