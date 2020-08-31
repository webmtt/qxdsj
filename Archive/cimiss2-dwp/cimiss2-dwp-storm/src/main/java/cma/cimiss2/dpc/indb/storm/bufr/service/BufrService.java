package cma.cimiss2.dpc.indb.storm.bufr.service;

import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

public interface BufrService {
//	boolean decode(Date recv_time, String fileName, String tableSection, List<String> tables, String Priority);
//	boolean decode(Date recv_time, String name, byte[] dataBytes, String tableSection, List<String> tables, String Priority);
	boolean decode(Date recv_time, String fileName, String CCCC, String tableSection, List<String> tables, List<StatDi> DI, List<RestfulInfo> EI,int useBBB);
	boolean decode(Date recv_time, String name, String CCCC,byte[] dataBytes, String tableSection, List<String> tables, List<StatDi> DI, List<RestfulInfo> EI,int useBBB);
}
