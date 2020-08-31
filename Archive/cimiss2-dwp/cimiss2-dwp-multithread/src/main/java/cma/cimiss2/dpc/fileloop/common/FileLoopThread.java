package cma.cimiss2.dpc.fileloop.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import cma.cimiss2.dpc.fileloop.bean.FileDi;
import cma.cimiss2.dpc.fileloop.di.DIQueues;
import cma.cimiss2.dpc.fileloop.mq.MQUtils;
import cma.cimiss2.dpc.fileloop.mq.QueueBean;

/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre
 * (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * <b>Description:</b><br>
 * 
 * @author wuzuoqiang
 * @version 1.0
 * @Note <b>ProjectName:</b> cimiss2-dwp-fileloop <br>
 *       <b>PackageName:</b> cma.cimiss2.dpc.fileloop.common <br>
 *       <b>ClassName:</b> FileLoopThread <br>
 *       <b>Date:</b> 2019年4月25日 下午6:15:20
 */
public class FileLoopThread implements Job {
	public static final Logger messageLogger = LoggerFactory.getLogger("messageInfo");
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static String base_path = "/space/dpc/work/input";
	private static String target_path = "/space/dpc/work/data";
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");

	Map<String, QueueBean> map = DataRouteConfig.getConfigMap();

	public FileLoopThread() {
		Properties properties = new Properties();
		// 使用InPutStream流读取properties文件
		infoLogger.info("FileLoopThread  Properties properties = new Properties() ");
		try {
			properties.load(new FileInputStream(new File("config/dpc_file_loop.properties")));
			base_path = properties.getProperty("base_path").trim();
			target_path = properties.getProperty("target_path").trim();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			infoLogger.error("",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			infoLogger.error("",e);
		}
	}

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		Connection connection = null;
		Channel channel = null;
		try {
			connection = MQUtils.getConnection();
			channel = connection.createChannel();

			File baseFile = new File(base_path);
			System.out.println(base_path);
			if (baseFile != null && baseFile.exists() && baseFile.isDirectory()) {
				for (File subFile : baseFile.listFiles()) {
					String regEx = "[A-Z]";
					// 编译正则表达式
					Pattern pattern = Pattern.compile(regEx);
					Matcher matcher = pattern.matcher(subFile.getName());
					if (!matcher.matches()) {
						infoLogger.error("该文件夹为异常文件夹：" + subFile.getAbsolutePath());
						continue;
					}
					if (subFile.isDirectory()) {
						for (File dateFile : subFile.listFiles()) {
							try {
								if (dateFile.isFile()) {
									continue;
								}
								Date date = simpleDateFormat.parse(dateFile.getName());
								Calendar calendar = Calendar.getInstance();
								calendar.setTime(new Date());
								calendar.add(Calendar.HOUR_OF_DAY, -2);
								System.out.println(
										"folder time : " + date + "     calendar time : " + calendar.getTime());
								System.out.println(
										"date.before(calendar.getTime()) : " + date.before(calendar.getTime()));
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
								SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHH");
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH");
								if (date.before(calendar.getTime())) {
									if (dateFile.isDirectory()) {
										if (dateFile.listFiles().length == 0) {
											try {
												FileUtils.deleteDirectory(dateFile);

												// 空文件夹发送DI
												FileDi fileDi = new FileDi();
												fileDi.setRecord_time(sdf.format(new Date()));
												fileDi.setDir_name(dateFile.getPath());
												String[] filePathItems = dateFile.getPath().split(File.separator);
												fileDi.setData_class(filePathItems[filePathItems.length - 2]);
												fileDi.setData_date(sdf2
														.format(sdf1.parse(filePathItems[filePathItems.length - 1])));
												fileDi.setFile_num(0);
												fileDi.setFile_size(0);
												fileDi.setFile_list(new HashMap<>());
												DIQueues.queue.offer(fileDi);

											} catch (IOException e) {
												infoLogger.error("",e);
												e.printStackTrace();
											}
											System.out.println("delete : " + dateFile.getAbsolutePath());
										} else {
											int file_num = 0; // 记录文件个数
											int file_size = 0; // 记录文件夹总文件大小
											Map<String, Long> fileListMap = new HashMap<String, Long>();
											for (File file : dateFile.listFiles()) {
												if (file.isFile()) {
													String filenameString = file.getName();
													messageLogger.info(filenameString);
													String[] fileItems = filenameString.split("~");
													System.out.println(filenameString);
													if (fileItems != null && fileItems.length >= 2) {
														String tempFileName = target_path + "/"
																+ fileItems[0].substring(0, 1) + "/" + fileItems[0]
																+ "/" + dateFile.getName().substring(0, 6) + "/"
																+ dateFile.getName().substring(0, 10) + "/"
																+ fileItems[1];
														String message = fileItems[0] + ":" + tempFileName;
														try {
															file_size += file.length();
															file_num += 1;
															
															if(file_num < 20) {
																fileListMap.put(file.getName(), file.length());
															}
															infoLogger.info("sorce file: " + file.getAbsolutePath() + " \t"
																	+ "target: " + tempFileName);
															File tempFile = new File(tempFileName);
															if(tempFile.exists()) {
																tempFile.delete();
															}
															FileUtils.moveFile(file, tempFile);
															if (map.containsKey(fileItems[0].trim())) {
																QueueBean queueBean = map.get(fileItems[0].trim());
																MQUtils.sendByDeclare(connection, channel,
																		queueBean.getExchangeName(), queueBean.getRoutingKey(), queueBean.getQueueName(), message);
																System.out.println(message);
															}
														} catch (IOException e) {
															infoLogger.info("",e);
															e.printStackTrace();
															continue;
														}
													} else {
														infoLogger.error(file.getPath());
														continue;
													}

												}
											}

											FileDi fileDi = new FileDi();
											fileDi.setRecord_time(sdf.format(new Date()));
											fileDi.setDir_name(dateFile.getPath());
											String[] filePathItems = dateFile.getPath().split(File.separator);
											fileDi.setData_class(filePathItems[filePathItems.length - 2]);
											fileDi.setData_date(
													sdf2.format(sdf1.parse(filePathItems[filePathItems.length - 1])));
											fileDi.setFile_num(file_num);
											DecimalFormat df = new DecimalFormat("#.00");
											fileDi.setFile_size(Float.parseFloat(df.format(file_size / (float) 1024)));
											fileDi.setFile_list(fileListMap);
											DIQueues.queue.offer(fileDi);
										}

									}
								}
							} catch (ParseException e) {
								infoLogger.error("",e);
								e.printStackTrace();
								continue;
							}
						}
					}
				}
			}

		} catch (Exception e1) {
			infoLogger.error("",e1);
			e1.printStackTrace();
		} finally {
			if (channel != null) {
				try {
					channel.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

}
