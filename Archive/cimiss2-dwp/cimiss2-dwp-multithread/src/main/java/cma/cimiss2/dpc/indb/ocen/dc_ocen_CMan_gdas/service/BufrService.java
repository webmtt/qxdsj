package cma.cimiss2.dpc.indb.ocen.dc_ocen_CMan_gdas.service;

import java.util.List;

public interface BufrService {

	boolean decode(String fileName, String tableSection, List<String> tables);
	boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables);
}
