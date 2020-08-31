package cma.cimiss2.dpc.indb.surf.dc_surf_global_h_bufr.service;

import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Interface BufrService.
 */
public interface BufrService {

	/**
	 * Decode.
	 *
	 * @param fileName the file name
	 * @param tableSection the table section
	 * @param tables the tables
	 * @return true, if successful
	 */
	boolean decode(String fileName, String tableSection, List<String> tables);
	
	/**
	 * Decode.
	 *
	 * @param name the name
	 * @param dataBytes the data bytes
	 * @param tableSection the table section
	 * @param tables the tables
	 * @return true, if successful
	 */
	boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables);
}
