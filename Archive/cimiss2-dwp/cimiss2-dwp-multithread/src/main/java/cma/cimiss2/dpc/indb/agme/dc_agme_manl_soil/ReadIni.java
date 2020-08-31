package cma.cimiss2.dpc.indb.agme.dc_agme_manl_soil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.cimiss2.dwp.tools.utils.IniReader;

public class ReadIni extends IniReader {

	/**
	 * @Fields SECTION_SOIL_01 : z_agme_soil.ini配置文件的section标签SOIL-01
	 */
	public static final String SECTION_SOIL_01 = "SOIL-01";
	/**
	 * @Fields SECTION_SOIL_01 : z_agme_soil.ini配置文件的section标签SOIL-02
	 */
	public static final String SECTION_SOIL_02 = "SOIL-02";
	/**
	 * @Fields SECTION_SOIL_01 : z_agme_soil.ini配置文件的section标签SOIL-03
	 */
	public static final String SECTION_SOIL_03 = "SOIL-03";
	/**
	 * @Fields SECTION_SOIL_01 : z_agme_soil.ini配置文件的section标签SOIL-04
	 */
	public static final String SECTION_SOIL_04 = "SOIL-04";
	/**
	 * @Fields SECTION_SOIL_01 : z_agme_soil.ini配置文件的section标签SOIL-05
	 */
	public static final String SECTION_SOIL_05 = "SOIL-05";
	/**
	 * @Fields SECTION_SOIL_01 : z_agme_soil.ini配置文件的section标签SOIL-06
	 */
	public static final String SECTION_SOIL_06 = "SOIL-06";
	/**
	 * @Fields SECTION_SOIL_01 : z_agme_soil.ini配置文件的section标签SOIL-07
	 */
	public static final String SECTION_SOIL_07 = "SOIL-07";
	/**
	 * @Fields SECTION_SOIL_01 : z_agme_soil.ini配置文件的section标签SOIL-08
	 */
	public static final String SECTION_SOIL_08 = "SOIL-08";
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
	/**
	 * CTS_CODE
	 */
	public static final String CTS_CODE_KEY = "CTS_CODE";
	
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
//				is = new FileInputStream(System.getProperty("user.dir") + "/config/agme_soil/z_agme_soil.ini");
				is = new FileInputStream(System.getProperty("user.dir") + "/config/z_agme_soil.ini");
				readIni = new ReadIni(is);
			} catch (IOException e) {
				System.out.println("加载配置文件analysis.ini失败");
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
		String sql = getIni().getValue("AGME_SOIL_01", "insert_sql");
		System.out.println(sql);
	}
}
