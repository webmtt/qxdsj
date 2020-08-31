package cma.cimiss2.dpc.indb.sand.dc_cawn_sand_vis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.cimiss2.dwp.tools.utils.IniReader;

/**
 * <br>
 * @Title:  ReadIniPheno.java
 * @Package org.cimiss2.dwp.z_sand.vis
 * @Description:    TODO(沙尘暴大气能见度入库配置文件解析)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月8日 下午3:52:35   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class ReadIni extends IniReader {

	/**
	 * @Fields SECTION_PHENO_01 : z_agme_pheno.ini配置文件的section标签PHENO-01
	 */
	public static final String SECTION_VIS = "VIS";
	/**
	 * @Fields INSERT_SQL_KEY : insert语句的key
	 */
	public static final String INSERT_SQL_KEY = "insert_sql";
	/**
	 * @Fields INSERT_SQL_KEY : delete语句的key
	 */
	public static final String DELETE_SQL_KEY = "delete_sql";
	/**
	 * @Fields INSERT_SQL_KEY : update语句的key
	 */
	public static final String UPDATE_SQL_KEY = "update_sql";
	/**
	 * @Fields INSERT_SQL_KEY : select语句的key
	 */
	public static final String SELECT_SQL_KEY = "select_sql";
	/**
	 * @Fields INSERT_SQL_KEY : D_DATA_ID
	 */
	public static final String D_DATA_ID_KEY = "D_DATA_ID";
	
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
//				is = ReadIniPheno.class.getResourceAsStream("/agme_config/sql/z_agme_soil.ini");
				is = new FileInputStream(System.getProperty("user.dir") + "/config/sand_vis/z_sand_vis.ini");
				readIni = new ReadIni(is);
			} catch (IOException e) {
				System.out.println("加载配置文件z_sand_soi.ini失败");
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
	public static void main(String[] args) {
		String sql = getIni().getValue("SOI", "insert_sql");
		System.out.println(sql);
	}
}
