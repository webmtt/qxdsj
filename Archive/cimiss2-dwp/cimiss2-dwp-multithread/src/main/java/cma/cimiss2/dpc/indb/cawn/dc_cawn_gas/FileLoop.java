package cma.cimiss2.dpc.indb.cawn.dc_cawn_gas;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.cawn.dc_cawn_gas.service.GasBufrServiceImpl2;
import cma.cimiss2.dpc.indb.common.BufrConfig;
import cma.cimiss2.dpc.indb.surf.dc_surf_global_mon.service.OTSServiceImpl;

// TODO: Auto-generated Javadoc
/**
 * The Class FileLoop.
 */
public class FileLoop implements Runnable{
	
	/** The Constant infoLogger. */
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
	/** The Constant messageLogger. */
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	
	/** The file N. */
	public static String fileN = null;
	
	/** The simple date format. */
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** The files. */
	BlockingQueue<String> files;
	
	/** The table section. */
	private String tableSection;
	
	/**
	 * Instantiates a new file loop.
	 *
	 * @param tableSection the table section
	 * @param files the files
	 * @param diQueues the di queues
	 */
	public FileLoop(String tableSection, BlockingQueue<String> files, BlockingQueue<StatDi> diQueues) {
//		System.out.println("开始轮训！！！！！！！！！！！！！！！！！！");
		this.tableSection = tableSection;
		this.files = files;
		GasBufrServiceImpl2.setDiQueues(diQueues);
//		OTSServiceImpl.setDiQueues(diQueues);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	/*
	public static void main(String[] args) {
		List<String> tables = new ArrayList<String>();
		BufrConfig.init();
		tables.add("SURF_WEA_CBF_MUL_HOR_TAB");
		tables.add("SURF_WEA_GBF_MUL_HOR_TAB");
		tables.add("SURF_WEA_CBF_SSD_HOR_TAB");
		String path = "D:\\CIMISS2\\data\\A.0001.0044.R001.2018091212";
		
	}
	*/
	@Override
	public void run() {
		while(true) {
			if(files.size() > 0) {
				String filepath = files.poll();
				messageLogger.info(filepath);
				File file = new File(filepath);
				fileN = file.getName(); // 文件名
				if(file != null && file.exists() && file.isFile()) {
					StringBuffer loggerBuffer = new StringBuffer();
					loggerBuffer.append(" : " + simpleDateFormat.format(new Date(file.lastModified())) +" "+ file.getPath() + "\n");
					boolean decode = false;
					List<String> tableList = new ArrayList<String>();
					tableList = BufrConfig.getTableMap().get(tableSection);
					try{
						if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
							GasBufrServiceImpl2 bufrServiceImpl = new GasBufrServiceImpl2();
							decode = bufrServiceImpl.decode(filepath, tableSection, tableList);
						}
						else {
							OTSServiceImpl otsServiceImpl = new OTSServiceImpl();
							decode = otsServiceImpl.decode(filepath, tableSection, tableList);
						}
					}catch (Exception e) {
						infoLogger.error(filepath + " " + e.getMessage());
						e.printStackTrace();
					}
					finally{
						if (decode) {
							// 入库成功确认消息
							infoLogger.info("ack message : " + filepath);
						} else {
							// 入库失败，消息重新排队
							infoLogger.info("入库失败,重新排队 : " + filepath);
						}
					}
						
					infoLogger.info(loggerBuffer.toString());
					// 数据库连接错误
					if(decode == false){					
						// 文件不移走
					}else {
						FileUtil.moveFile(filepath);
					}					
						
				}
			}
			else {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}
