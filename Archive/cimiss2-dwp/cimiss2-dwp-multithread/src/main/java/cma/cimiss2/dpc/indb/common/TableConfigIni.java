package cma.cimiss2.dpc.indb.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import com.hitec.bufr.util.StringUtil;

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
public class TableConfigIni extends IniReader {
	
	public static final String TABLE_NAME_KEY = "tableName";
	public static final String DATA_TEMPLATE_KEY = "dataTemplate";

	private TableConfigIni(InputStream is) throws IOException {
		super(is);
	}

	private static TableConfigIni readIni = null;

	public static TableConfigIni getIni() {
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
				is = new FileInputStream(StringUtil.getConfigPath() + "bufr/table_config.ini");
				readIni = new TableConfigIni(is);
			} catch (IOException e) {
				System.out.println("加载配置文件table_config.ini失败");

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
	public static void main(String[] args){
		TableConfigIni tableIni = TableConfigIni.getIni();
		Map<String, Properties> map = tableIni.get();
		System.out.println(map);
	}
}
