package cma.cimiss2.dpc.indb.upar.dc_upar_temp.service;

import java.util.List;

public interface BufrService {

	boolean decode(String fileName, String tableSection, List<String> tables, String Priority);
	boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables, String Priority);
}
