package cma.cimiss2.dpc.decoder.agme;

import java.io.IOException;
import java.io.InputStream;

import cma.cimiss2.dpc.decoder.tools.utils.IniReader;


// TODO: Auto-generated Javadoc
/**
 * The Class ReadIniSoil.
 */
public class ReadIniSoil extends IniReader {

	/**
	 * Instantiates a new read ini soil.
	 *
	 * @param is the is
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ReadIniSoil(InputStream is) throws IOException {
		super(is);
	}

	/** The read ini. */
	private static ReadIniSoil readIni = null;

	/**
	 * Gets the ini.
	 *
	 * @return the ini
	 */
	public static ReadIniSoil getIni() {
		if (readIni != null) {
			return readIni;
		} else {
			makeInstance();
			return readIni;
		}
	}

	/**
	 * Make instance.
	 */
	private static void makeInstance() {
		if (readIni == null) {
			InputStream is = null;
			try {
				is = ReadIniSoil.class.getResourceAsStream("/model/agme_soil.ini");
//				is = new FileInputStream(System.getProperty("user.dir") + "/config/agme_soil/z_agme_soil.ini");
				readIni = new ReadIniSoil(is);
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
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		String sql = getIni().getValue("Head", "elements");
		System.out.println(sql);
	}
}
