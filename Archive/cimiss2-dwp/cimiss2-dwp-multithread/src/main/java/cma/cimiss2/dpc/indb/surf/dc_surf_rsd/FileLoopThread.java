package cma.cimiss2.dpc.indb.surf.dc_surf_rsd;

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
import cma.cimiss2.dpc.indb.common.BufrConfig;
import cma.cimiss2.dpc.indb.surf.dc_surf_rsd.service.OTSService;
import cma.cimiss2.dpc.indb.surf.dc_surf_rsd.service.RsdServiceImpl2;

/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  FileLoopThread.java   
 * @Package cma.cimiss2.dpc.indb.surf.dc_surf_rsd
 * @Description:    TODO(文件目录轮询处理线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * </pre>
 * 
 * @author liyamin
 *---------------------------------------------------------------------------------
 */
public class FileLoopThread implements Runnable{
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static String fileN = null;
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	BlockingQueue<String> files;
	private String tableSection;
	public FileLoopThread(String tableSection,BlockingQueue<String> files, BlockingQueue<StatDi> diQueues) {
		this.tableSection = tableSection;
		this.files = files;
		RsdServiceImpl2.setDiQueues(diQueues);
		OTSService.setDiQueues(diQueues);
	}

	@Override
	public void run() {
		while(true) {
			if(files.size() > 0) {
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
							RsdServiceImpl2 rsdserviceImpl = new RsdServiceImpl2();
							decode = rsdserviceImpl.decode(filepath, tableSection, tableList);
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
			}else {
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
