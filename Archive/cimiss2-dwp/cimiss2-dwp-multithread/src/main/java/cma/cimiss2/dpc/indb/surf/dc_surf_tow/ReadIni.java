package cma.cimiss2.dpc.indb.surf.dc_surf_tow;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.cimiss2.dwp.tools.utils.IniReader;

public class ReadIni extends IniReader {

	/**
	 * @Fields SECTION_PHENO_01 : z_agme_pheno.ini配置文件的section标签PHENO-01
	 */
	public static final String SECTION_TOWER = "TOWER";
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
				is = new FileInputStream(System.getProperty("user.dir") + "/config/surf_tow/z_surf_tow.ini");
				readIni = new ReadIni(is);
			} catch (IOException e) {
				System.out.println("load configfile z_surf_tow.ini failed!");
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
		String sql = getIni().getValue("TOWER", "insert_sql");
		System.out.println(sql);
	}
}
