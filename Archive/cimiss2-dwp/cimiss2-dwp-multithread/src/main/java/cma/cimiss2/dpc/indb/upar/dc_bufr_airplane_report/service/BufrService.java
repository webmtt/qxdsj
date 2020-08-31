package cma.cimiss2.dpc.indb.upar.dc_bufr_airplane_report.service;

import java.util.List;

public interface BufrService {

	boolean decode(String fileName, String tableSection, List<String> tables);
	boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables);
}