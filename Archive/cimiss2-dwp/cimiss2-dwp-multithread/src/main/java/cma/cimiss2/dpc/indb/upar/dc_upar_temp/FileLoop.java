package cma.cimiss2.dpc.indb.upar.dc_upar_temp;

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
import cma.cimiss2.dpc.indb.upar.dc_upar_temp.service.OTSUparBufrServiceImpl;
import cma.cimiss2.dpc.indb.upar.dc_upar_temp.service.UparBufrServiceImpl3;


/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  FileLoopThread.java   
 * @Package cma.cimiss2.dpc.indb.sevp.foreignlive
 * @Description:    TODO(文件目录轮询处理线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
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
		UparBufrServiceImpl3.setDiQueues(diQueues);
		OTSUparBufrServiceImpl.setDiQueues(diQueues);
	}

	@Override
	public void run() {
		while(true) {
			if(files.size() > 0) {
				String filepath = files.poll();
				messageLogger.info(filepath);
				File file = new File(filepath);
//				fileN = file.getName(); // 文件名
				if(file != null && file.exists() && file.isFile()) {
					StringBuffer loggerBuffer = new StringBuffer();
					loggerBuffer.append(" : " + simpleDateFormat.format(new Date(file.lastModified())) +" "+ file.getPath() + "\n");

//					List<String> tableList = new ArrayList<String>();
//					tableList = BufrConfig.getTableMap().get(tableSection);
//					int length = filepath.indexOf(":");
//					String cts_code = filepath.substring(0, length);
					boolean decode = false;
					try{
						if(StartConfig.getDatabaseType() == 0 || StartConfig.getDatabaseType() == 1){
							UparBufrServiceImpl3 uparBufrServiceImpl3 = new UparBufrServiceImpl3();
							decode = uparBufrServiceImpl3.decodeFile(filepath, tableSection);
						}
						else {
							OTSUparBufrServiceImpl otsUparBufrServiceImpl = new OTSUparBufrServiceImpl();
							decode = otsUparBufrServiceImpl.decodeFile(filepath, tableSection);
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
