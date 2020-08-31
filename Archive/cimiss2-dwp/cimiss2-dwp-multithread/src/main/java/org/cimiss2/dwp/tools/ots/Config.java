package org.cimiss2.dwp.tools.ots;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.cimiss.core.model.ClientConfiguration;




public class Config {

	//此处变量使用静态代码块中的内容
	public static String endpoint = "http://rdb.cn-beijing-gjqx-d01.ots.mcloud.nmic.cn";
	public static String accessId = "RtnMdDl1SUdv0L7r";
	public static String accessKey = "WiuzAUiEjSQrOpWmycTpEFOnMG9Kof";
	public static String instance = "rdb";
	public static String dataTable = "TestTable";
	public static String streamStatusTable = "StreamStatus1";

	public static ClientConfiguration configuration = null;
	static {
		FileInputStream fis = null;
		Properties prop = null;
		try {
			fis = new FileInputStream("config/atsconfig.properties");
			prop = new Properties();
			prop.load(fis);
			String datasource = prop.getProperty("ats.datasource");
			String ip = prop.getProperty("ats.ip");
			endpoint = ip;
			String port = prop.getProperty("ats.port");
			String username = prop.getProperty("ats.username");
			accessId = username;
			String password = prop.getProperty("ats.password");
			accessKey = password;
			String space = prop.getProperty("ats.space");
			instance = space;
			String initialpoolsize = prop.getProperty("ats.initialpoolsize");
			String minacquireincrement = prop.getProperty("ats.minacquireincrement");
			String maxacquireincrement = prop.getProperty("ats.maxacquireincrement");
			String maxattrcolumnsize = prop.getProperty("ats.maxattrcolumnsize");
			// String initslicemethod = prop.getProperty("ats.initslicemethod");
			// String batchredocount = prop.getProperty("ats.batchredocount");
			configuration = new ClientConfiguration(datasource, ip, Integer.parseInt(port), username, password, space, Integer.parseInt(maxacquireincrement),
					Integer.parseInt(minacquireincrement), Integer.parseInt(initialpoolsize), Integer.parseInt(maxattrcolumnsize));

		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
