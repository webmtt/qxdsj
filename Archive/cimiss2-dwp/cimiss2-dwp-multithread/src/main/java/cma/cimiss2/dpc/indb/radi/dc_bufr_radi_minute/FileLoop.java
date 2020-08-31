package cma.cimiss2.dpc.indb.radi.dc_bufr_radi_minute;

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
import cma.cimiss2.dpc.indb.radi.dc_bufr_radi_minute.service.OTSRadiServiceImpl;
import cma.cimiss2.dpc.indb.radi.dc_bufr_radi_minute.service.RadiServiceImpl2;


public class FileLoop implements Runnable{
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static String fileN = null;
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	BlockingQueue<String> files;
	private String tableSection;
	public FileLoop(String tableSection,BlockingQueue<String> files, BlockingQueue<StatDi> diQueues) {
		this.tableSection = tableSection;
		this.files = files;
		RadiServiceImpl2.setDiQueues(diQueues);
		OTSRadiServiceImpl.setDiQueues(diQueues);
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
					
//					boolean decode = false;
					try{
						if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
							RadiServiceImpl2 radiServiceImpl = new RadiServiceImpl2();
							 radiServiceImpl.decodeFile(filepath, tableSection, (int)file.length(), new Date(file.lastModified()));
						}
						else {
							OTSRadiServiceImpl otsServiceImpl = new OTSRadiServiceImpl();
							otsServiceImpl.decodeFile(filepath, tableSection);
						}
					}catch(Exception e){
						e.printStackTrace();
						
					}finally{
						
							// 入库成功确认消息
							infoLogger.info("ack message : " + filepath);
							FileUtil.moveFile(filepath);
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
