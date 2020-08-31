package cma.cimiss2.dpc.indb.storm.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadMqIni extends IniReader {

	public ReadMqIni(InputStream is) throws IOException {
		super(is);
	}
	
	private static ReadMqIni readIni = null;

	public static ReadMqIni getIni() {
		if (readIni != null) {
			return readIni;
		} else {
			makeInstance();
			return readIni;
		}
	}
	
	private static String path = System.getProperty("user.dir") + "/config/";

	public static void init(String cPath) {
		path = cPath;
	}
	
	private static void makeInstance() {
		if (readIni == null) {
			InputStream is = null;
			try {
				is = new FileInputStream(path + "bufr/storm_rabbitmq.ini");
				readIni = new ReadMqIni(is);
			} catch (IOException e) {
				System.out.println("加载配置文件config.ini失败");

				e.printStackTrace();
			} finally {
				if (is != null) {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
