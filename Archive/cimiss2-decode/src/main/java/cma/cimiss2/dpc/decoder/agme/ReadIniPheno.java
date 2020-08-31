package cma.cimiss2.dpc.decoder.agme;

import java.io.IOException;
import java.io.InputStream;

import cma.cimiss2.dpc.decoder.tools.utils.IniReader;


// TODO: Auto-generated Javadoc
/**
 * The Class ReadIniPheno.
 */
public class ReadIniPheno extends IniReader {

	/**
	 * Instantiates a new read ini pheno.
	 *
	 * @param is the is
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private ReadIniPheno(InputStream is) throws IOException {
		super(is);
	}

	/** The read ini. */
	private static ReadIniPheno readIni = null;

	/**
	 * Gets the ini.
	 *
	 * @return the ini
	 */
	public static ReadIniPheno getIni() {
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
				is = ReadIniPheno.class.getResourceAsStream("/model/agme_pheno.ini");
//				is = new FileInputStream(System.getProperty("user.dir") + "/config/agme_soil/z_agme_soil.ini");
				readIni = new ReadIniPheno(is);
			} catch (IOException e) {
				System.out.println("load profile analysis.ini failed");
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
