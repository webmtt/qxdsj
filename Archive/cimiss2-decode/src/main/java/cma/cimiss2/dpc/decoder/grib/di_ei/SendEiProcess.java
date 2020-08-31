package cma.cimiss2.dpc.decoder.grib.di_ei;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.grib.Grib_Struct_Data;

public class SendEiProcess 
{
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	
	private ExecutorService threadPool_EI;
	private static SendEiProcess sendEiProcess = null;
	
	public static SendEiProcess getInstance() 
	{
		if (sendEiProcess == null )
			sendEiProcess = new SendEiProcess(); 
		return sendEiProcess;
	}
	
	private SendEiProcess() 
	{
		threadPool_EI = Executors.newFixedThreadPool(10);
	}
	
	
	/**
	 * 使用线程池发送REST EI告警信息	 
	 * @param EVENT_TYPE
	 *            EVENT_TYPE事件编号
	 * @param KEvent
	 *            KEvent事件内容
	 * @param DATETIME
	 *            数据时间
	 * @param data_id
	 *            四级编码
	 */
	public void process_ei(String EVENT_TYPE, String KEvent, String DATETIME, String data_id)
	{
		logger.info("使用线程池来处理rest EI");
		SendEiThread sendEiThread = new SendEiThread(EVENT_TYPE,KEvent, DATETIME, data_id);
		threadPool_EI.execute(sendEiThread);		
		
	}
}
