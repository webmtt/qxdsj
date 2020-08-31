package cma.cimiss2.dpc.indb.storm.tools;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <br>
 * 
 * @Title: ReadIni.java
 * @Package org.cimiss2.dwp.z_sand.soi
 * @Description: ini decode
 * 
 *               <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月8日 下午3:49:52   wangzunpeng    Initial creation.
 *               </pre>
 * 
 * @author wangzunpeng
 */
public class ReadIni extends IniReader {
	
	public static final String TABLE_NAME_KEY = "tableName";
	public static final String DATA_TEMPLATE_KEY = "dataTemplate";

	private ReadIni(InputStream is) throws IOException {
		super(is);
	}

	private static ReadIni readIni = null;

	public static ReadIni getIni() {
		if (readIni != null) {
			return readIni;
		} else {
			makeInstance();
			return readIni;
		}
	}

	private static void makeInstance() {
		if (readIni == null) {
			InputStream is = null;
			try {
				is = new FileInputStream(System.getProperty("user.dir") + "/config/config.ini");
				readIni = new ReadIni(is);
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
