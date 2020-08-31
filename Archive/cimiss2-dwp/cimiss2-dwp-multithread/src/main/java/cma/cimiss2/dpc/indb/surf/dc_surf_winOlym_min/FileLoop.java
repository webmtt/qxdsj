package cma.cimiss2.dpc.indb.surf.dc_surf_winOlym_min;

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
import cma.cimiss2.dpc.indb.common.BufrConfig;
import cma.cimiss2.dpc.indb.surf.dc_surf_winOlym_min.service.BufrServiceImpl4;



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
	public FileLoop(String tableSection, BlockingQueue<String> files, BlockingQueue<StatDi> diQueues, BlockingQueue<StatDi> diQueues2) {
		this.tableSection = tableSection;
		this.files = files;
		BufrServiceImpl4.setDiQueues(diQueues);
		BufrServiceImpl4.setDiQueues2(diQueues2);
	}
	
	@Override
	public void run() {
		while(true) {
			if(files.size() > 0) {
				String filepath = files.poll();
				messageLogger.info(filepath);
				File file = new File(filepath);
				Date recv_time = new Date(file.lastModified());
				fileN = file.getName(); // 文件名
				if(file != null && file.exists() && file.isFile()) {
					StringBuffer loggerBuffer = new StringBuffer();
					loggerBuffer.append(" : " + simpleDateFormat.format(new Date(file.lastModified())) +" "+ file.getPath() + "\n");
					boolean decode = false;
					List<String> tableList = new ArrayList<String>();
					tableList = BufrConfig.getTableMap().get(tableSection);
					try{
						if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
							BufrServiceImpl4 bufrServiceImpl = new BufrServiceImpl4();
							decode = bufrServiceImpl.decode(recv_time,filepath, tableSection, tableList);
						}
						else {
							// OTS							
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
