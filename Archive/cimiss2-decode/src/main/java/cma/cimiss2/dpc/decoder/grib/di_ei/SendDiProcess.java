package cma.cimiss2.dpc.decoder.grib.di_ei;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import cma.cimiss2.dpc.decoder.grib.Grib_Struct_Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SendDiProcess
{
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	
	private ExecutorService threadPool;
	private static SendDiProcess sendDiProcess = null;
	
	public static SendDiProcess getInstance() 
	{
		if (sendDiProcess == null )
			sendDiProcess = new SendDiProcess();
		return sendDiProcess;
	}
	
	private SendDiProcess() 
	{
		threadPool = Executors.newFixedThreadPool(20);
	}
	
	public void process_di(Grib_Struct_Data grib_struc_data,String db_type)
	{
		//logger.info("使用线程池来处理rest DI");
		SendDiThread sendDiThread = new SendDiThread(grib_struc_data,db_type);
		threadPool.execute(sendDiThread);		
		
	}
	
}