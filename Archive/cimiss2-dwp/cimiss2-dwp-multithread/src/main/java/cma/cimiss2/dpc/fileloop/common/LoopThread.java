package cma.cimiss2.dpc.fileloop.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import cma.cimiss2.dpc.fileloop.mq.MQUtils;
import cma.cimiss2.dpc.fileloop.mq.QueueBean;

public class LoopThread implements Runnable {
	private static String target_path = "/space/dpc/work/data";
	private final static Logger logger = LoggerFactory.getLogger("messageInfo"); //消息日志
	private String dataPath;
	Connection connection = null;
	Channel channel = null;
	
	Map<String, QueueBean> map = DataRouteConfig.getConfigMap();
	public LoopThread(String dataPath) {
		try {
			connection = MQUtils.getConnection();
			channel = connection.createChannel();
		} catch (Exception e) {
			logger.error("connection rabbitmq failed : " + e.getMessage());
			e.printStackTrace();
		}
		
		this.dataPath = dataPath;
	}
	
	
	// 启用线程开始目录轮训
	@Override
	public void run() {
		loopFile(this.dataPath);
	}
	/**
	 * descriptionCN 目录轮训函数
	 * @param dataPath
	 */
	private void loopFile(String dataPath) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
		File file = new File(dataPath);
		// 判断轮训配置的目录是否存在,是否为有效的目录
		if(file.isDirectory() && file.exists()) {
			// 获取目录下面的文件夹, 这里的文件夹一般为  YYYYMMDDHH 时间文件夹
			File[] tempFiles = file.listFiles();
			
			if(tempFiles != null && tempFiles.length > 0) {
				// 对文件夹排序,在数据积压情况下处理最新时次的文件
				Arrays.sort(tempFiles);
				for (File fi : tempFiles) {
					// 判断是否为文件夹
					if(fi.isDirectory()) {
						loopFile(fi.getPath());
					}else {
						// 遍历到文件的处理逻辑
						String filename = fi.getName();
						if(filename.contains("~") && !filename.contains(".tmp") && !filename.contains(".temp")) {
							String[] fileNameItems = filename.split("~");
							if(fileNameItems[0].length() == 16) {
								
								String [] itemStrings = fi.getParent().split("/");
								if(itemStrings.length > 0) {
									try {
										simpleDateFormat.parse(itemStrings[itemStrings.length-1].substring(0, 10));
										// 目标路径
										String tempFileName = target_path + "/" + fileNameItems[0].substring(0, 1) + "/" + fileNameItems[0] + "/" + itemStrings[itemStrings.length-1].substring(0, 6)+"/" + itemStrings[itemStrings.length-1].substring(0,10)+"/"+ fileNameItems[1];
										
										String message = fileNameItems[0] + ":" + tempFileName;
										try {
											FileUtils.moveFile(fi, new File(tempFileName));
											if(map.containsKey(fileNameItems[0].trim())) {
												
												QueueBean queueBean = map.get(fileNameItems[0].trim());
												MQUtils.sendByDeclare(connection, channel,
														queueBean.getExchangeName(), queueBean.getRoutingKey(), queueBean.getQueueName(), message);
											}else {
												logger.error("not find " + fileNameItems[0].trim());
											}
											
											System.out.println(message);
										} catch (Exception e) {
											e.printStackTrace();
											logger.error(message);
											logger.error(e.getMessage());
											continue;
										}
									} catch (Exception e) {
										e.printStackTrace();
										logger.error("simpleDateFormat.parse(itemStrings[itemStrings.length-1]);");
										logger.error(" FILE ERROR : " + fi.getPath());
										continue;
									}
								}
								
								
							}else {
								logger.error(" FILE ERROR : " + fi.getPath());
								continue;
							}
						}else {
							logger.error(" FILE ERROR : " + fi.getPath());
							continue;
							
						}
					}
				}
			}
		}
	}


}
