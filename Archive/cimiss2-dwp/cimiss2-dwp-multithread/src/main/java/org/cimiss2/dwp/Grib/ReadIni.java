package org.cimiss2.dwp.Grib;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.cimiss2.dwp.tools.utils.IniReader;

/**
 * <br>
 * @Title:  ReadIniPheno.java
 * @Package org.cimiss2.dwp.z_sand.soi
 * @Description:   ini decode
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月8日 下午3:49:52   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class ReadIni extends IniReader {

	
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
				is = new FileInputStream("config/index.txt");
				readIni = new ReadIni(is);
			} catch (IOException e) {
				System.out.println("加载配置文件index.ini失败");
				if (is != null) {
					try {
						is.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				e.printStackTrace();
			}
		}
	}
}
