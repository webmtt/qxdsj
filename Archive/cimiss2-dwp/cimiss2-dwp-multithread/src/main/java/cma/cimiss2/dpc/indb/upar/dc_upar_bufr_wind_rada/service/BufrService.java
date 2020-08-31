package cma.cimiss2.dpc.indb.upar.dc_upar_bufr_wind_rada.service;

import java.util.List;

public interface BufrService {

	boolean decode(String fileName, String tableSection, List<String> tables);
	boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables);
}
