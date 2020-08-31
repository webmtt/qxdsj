package cma.cimiss2.dpc.indb.bufrsate.insat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.indb.bufrsate.insat.service.InsatServiceImpl;
import cma.cimiss2.dpc.indb.bufrsate.insat.service.OTSService;
import cma.cimiss2.dpc.indb.common.BufrConfig;


// TODO: Auto-generated Javadoc
/**
 * -------------------------------------------------------------------------------
 * <br>.
 *
 * @author liyamin 2019-4-11 14:52
 * ---------------------------------------------------------------------------------
 * @Title:  FileLoopThread.java
 * @Package cma.cimiss2.dpc.indb.sevp.foreignlive
 * @Description:    TODO(卫星云导风资料数据（BUFR格式）	大数据平台   文件目录轮询处理线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * </pre>
 */
public class FileLoopThread implements Runnable{
	
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
	public static boolean isProccessed;
	/**
	 * Instantiates a new file loop thread.
	 *
	 * @param tableSection the table section
	 * @param files the files
	 * @param diQueues the di queues
	 */
	public FileLoopThread(String tableSection,BlockingQueue<String> files, BlockingQueue<StatDi> diQueues ) {
		this.tableSection = tableSection;
		this.files = files;
		InsatServiceImpl.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while(true) {
			if(files.size() > 0) {
				isProccessed=true;
				String filepath = files.poll();
				messageLogger.info(filepath);
				File file = new File(filepath);
//				fileN = file.getName();

				if(file.exists() && file.isFile()) {
					
					List<String> tableList = new ArrayList<String>();
					tableList = BufrConfig.getTableMap().get(tableSection);
					
					boolean decode = false;
					try{
						if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
							InsatServiceImpl insatServiceImpl = new InsatServiceImpl();
							decode = insatServiceImpl.decode(filepath, tableSection, tableList);
						}
						else {
							OTSService otsService = new OTSService();
							decode = otsService.decode(filepath, tableSection, tableList);
						}
					}catch(Exception e){
						e.printStackTrace();
						
					}finally{
						if (decode) {
							// 入库成功确认消息
							infoLogger.info("ack message : " + filepath);
							FileUtil.moveFile(filepath);
							
						} else {
							// 入库失败，消息重新排队
							infoLogger.info("入库失败,重新排队 : " + filepath);
							
						}
					}

				}else {
					// 文件不存在
				}
			}else {//=0
				if(isProccessed){//处理过一次了
					if(StartConfig.getLoopQuitFlag()==true){
						break;//目录轮训完是否停止退出,不配置的话默认为false不退
					}else{
						try {
							double sleepSecond=StartConfig.getFileLoopSleepSecond();//不配置的话默认sleep为1秒
							long Millisecond=new Double(sleepSecond*1000).longValue();
							Thread.sleep(Millisecond);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}else{//还没处理过
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
}
