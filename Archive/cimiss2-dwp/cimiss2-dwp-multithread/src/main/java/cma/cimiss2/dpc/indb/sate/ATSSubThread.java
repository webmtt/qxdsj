package cma.cimiss2.dpc.indb.sate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.cmadaas.ats.core.ATS;
import org.cmadaas.ats.core.ATSClient;
import org.cmadaas.ats.core.bean.CommonData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.SateFileDI;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.general.common.ATSConfig;
import cma.cimiss2.dpc.indb.general.common.LoadTableConfig;
import cma.cimiss2.dpc.indb.general.common.StringSplit;
import cma.cimiss2.dpc.indb.general.service.ServiceQueue;
import cma.cimiss2.dpc.indb.general.vo.AtsTableInfo;
import cma.cimiss2.dpc.indb.general.vo.FileInfo;
import cma.cimiss2.dpc.indb.general.vo.TableConfig;

/**
 * <br>
 * ATSSubThread 雷达卫星消息处理线程，主要功能为ATS 返回结果集：无
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 12/6/2017   wuzuoqiang    Initial creation.
 * </pre>
 * @author wuzuoqiang
 */
public class ATSSubThread implements Runnable {
	private BlockingQueue<SateFileDI> diQueues;
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo"); // 消息处理日志
	private RandomAccessFile randomAccessFile;
	private ATS ats = null;

	public ATSSubThread(BlockingQueue<SateFileDI> diQueues ) {
		this.diQueues=diQueues;
	}

	@Override
	public void run() {
		initAtsClient();

		try {
			while (true) {

				@SuppressWarnings("rawtypes")
				List<FileInfo> fileInfos = ServiceQueue.getServerQueue().get(1);
				if (fileInfos != null && fileInfos.size() > 0) {
					for (int i = 0; i < fileInfos.size(); i++) {
						// 获取cts资料四级编码
						String cts_code = fileInfos.get(i).getCts_code();
						// 获取文件的绝对路径
						String filePath = fileInfos.get(i).getFilepath();
						// 判断入库策略是否配置
						logger.info(
								"start ATSConfig.getInstance().getAtsTableInfos().containsKey by cts_code " + cts_code);
						if (ATSConfig.getInstance().getAtsTableInfos().containsKey(cts_code)) {

							AtsTableInfo tableInfo = ATSConfig.getInstance().getAtsTableInfos().get(cts_code);

							File file = new File(filePath);
							if (file.exists() && file.isFile()) {
								String[] sp = cts_code.equalsIgnoreCase("K.0734.0001.R001")
										|| cts_code.equalsIgnoreCase("K.0715.0001.R001")
										|| cts_code.equalsIgnoreCase("K.0716.0001.R001")
										|| cts_code.equalsIgnoreCase("K.0747.0001.R001")
												? StringSplit.split(file.getName(), fileInfos.get(i).getSplitRegex(),
														false)
												: StringSplit.split(file.getName(), fileInfos.get(i).getSplitRegex());

								CommonData fileData = new CommonData();
								// 设置 文件名
								fileData.setDataName(file.getName());
								// 设置文件格式
								fileData.setDataFormat(file.getName().substring(file.getName().lastIndexOf(".") + 1));
								byte[] filebytes = getContent(filePath);
								if (filebytes == null) {
									continue;
								}
								fileData.setData(filebytes);

//								String ath = file.getName().split("_")[file.getName().split("_").length - 2];
								fileData.setReserveKeyTwo(cts_code+"~" +file.getName());
								// 文件大小
//								fileData.setFileSize(file.length());
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
								// 入库时间
								fileData.setIYMDHM(sdf.format(new Date()));
								// 四级编码
								fileData.setDataId(tableInfo.getDataId().getStr());
								setFileDataTime(tableInfo, sp, fileData);
								setFileDataStationID(tableInfo, sp, fileData);
								
								if (tableInfo.getProdCode().getType() == 0) {
									fileData.setProdCode(tableInfo.getProdCode().getStr());
								} else if (tableInfo.getProdCode().getType() == 1) {
									fileData.setProdCode(sp[tableInfo.getProdCode().getPos()]);
								} else if (tableInfo.getProdCode().getType() == 2) {
									fileData.setProdCode(sp[tableInfo.getProdCode().getPos()].substring(
											tableInfo.getProdCode().getStart(), tableInfo.getProdCode().getEnd()));
								} else {
									setProdCode(tableInfo, sp, fileData);
//									fileData.setProdCode(tableInfo.getProdCode().getStr());
								}

								if (tableInfo.getProductCenter().getType() == 0) {
									fileData.setProductCenter(tableInfo.getProductCenter().getStr());
								} else if (tableInfo.getProductCenter().getType() == 1) {
									fileData.setProductCenter(sp[tableInfo.getProductCenter().getPos()]);
								} else if (tableInfo.getProductCenter().getType() == 2) {
									fileData.setProductCenter(sp[tableInfo.getProductCenter().getPos()].substring(
											tableInfo.getProductCenter().getStart(), tableInfo.getProdCode().getEnd()));
								} else {
									fileData.setProductCenter(tableInfo.getProductCenter().getStr());
								}

								if (tableInfo.getProductDescription().getType() == 0) {
									fileData.setProductDescription(tableInfo.getProductDescription().getStr());
								} else if(tableInfo.getProductDescription().getType() == 1) {
									fileData.setProductDescription(sp[tableInfo.getProductDescription().getPos()]);
								} else if (tableInfo.getProductDescription().getType() == 2) {
									fileData.setProductDescription(sp[tableInfo.getProductDescription().getPos()]
											.substring(tableInfo.getProductDescription().getStart(),
													tableInfo.getProdCode().getEnd()));
								} else {
									fileData.setProductDescription(tableInfo.getProductDescription().getStr());
								}

								if (tableInfo.getProductMethod().getType() == 0) {
									fileData.setProductMethod(tableInfo.getProductMethod().getStr());
								} else if (tableInfo.getProductMethod().getType() == 1) {
									fileData.setProductMethod(sp[tableInfo.getProductMethod().getPos()]);
								} else if (tableInfo.getProductMethod().getType() == 2) {
									fileData.setProductMethod(sp[tableInfo.getProductMethod().getPos()].substring(
											tableInfo.getProductMethod().getStart(), tableInfo.getProdCode().getEnd()));
								} else {
									fileData.setProductMethod(tableInfo.getProductMethod().getStr());
								}

								TableConfig tableConfig = LoadTableConfig.getInstance().getTablesMaps().get(cts_code);

								if (tableConfig != null && !tableConfig.getAtsTable().isEmpty()) {
									logger.info("find  " + cts_code + "    " + tableConfig.getAtsTable());
//									boolean bret=ats.createStaFileDataTable(tableConfig.getAtsTable(),30*24*60*60);
									SateFileDI fileDI = ((SateFileDI) fileInfos.get(i).getFileDI()).clone();
									fileDI.setSEND("RADB");
									int result = 0;
									try {
										
										result = ats.writeCommonData(tableConfig.getAtsTable(), fileData);
									} catch (Exception e) {
										sendEi(EIEventType.CASSANDRA_CONNECT_ERROE);
										logger.error("\n ATSClient：连接错误 " + e.getMessage() + "---" + result);
										ats = null;
										initAtsClient();
									} 
//									int result = ats.writeStaFileData(, fileData);
									if (result <= 0) {
										fileDI.setPROCESS_STATE("0");
										sendEi(EIEventType.CASSANDRA_CONNECT_ERROE);
										logger.error("\n file insert Cassandra error ：" + filePath + "@result ：" + result);
									} else {
										logger.info(" insert into cassandra sucess!  table name:  "+ tableConfig.getAtsTable());
									}
									fileDI.setPROCESS_END_TIME(TimeUtil.getSysTime());
									this.diQueues.offer(fileDI);
								} else {
									logger.error("\n table.xml File AtsTable is null ,  this data cannot insert Cassandra"+ cts_code + "@" + filePath);
								}
							} else {
								logger.error("\n ATSSubThread file not find ：" + filePath);
							}
						} else {
							logger.info("\n ATSConfig.xml File not find " + cts_code + "this data cannot insert Cassandra");
						}
					}
				} else {
					Thread.sleep(1000);
					continue;
				}

				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("insert into cassandra error " ,e);
		}
	}
	
	

	/**
	 * 初始化AtsClient
	 */
	private void initAtsClient() {
		while (true) {
			if (ats == null) {
				try {
					ats = new ATSClient("config/");
					if (ats != null) {
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					sendEi(EIEventType.CASSANDRA_CONNECT_ERROE);
					try {
						Thread.sleep(1000 * 5);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					logger.error("\n ATSClient：连接错误 ：  ", e);
					continue;
				}
			} else {
				break;
			}
		}

	}
	
	private void sendEi(EIEventType eiEventType) {
		EI ei = EIConfig.getEiConfig().getEiMaps().get(eiEventType.getCode());
		if(ei == null) {
			logger.error("\n EI配置文件中没有事件类型：" + eiEventType.getCode());
		}else {
			
			if(StartConfig.isSendEi()) {
				if(eiEventType == EIEventType.CASSANDRA_CONNECT_ERROE) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.rada.ATSSubThread");
					ei.setKEvent("Cassandra 数据库连接异常");
					ei.setKIndex("dpc_rada_frename.jar");
					RestfulInfo restfulInfo = new RestfulInfo();
					restfulInfo.setType("SYSTEM.ALARM.EI ");
					restfulInfo.setName("数据解码入库EI告警信息");
					restfulInfo.setMessage("数据解码入库EI告警信息");
					restfulInfo.setFields(ei);
					List<RestfulInfo> restfulInfos = new ArrayList<>();
					restfulInfos.add(restfulInfo);
					RestfulSendData.SendData(restfulInfos);
				}
			}else {
				logger.error("\n 配置为不发送EI：StartConfig.isSendEi()=" + StartConfig.isSendEi());
			}
			
		}
		
		
	}

	private void setProdCode(AtsTableInfo tableInfo, String[] sp, CommonData fileData) {
		if (tableInfo.getProdCode().getType() == 3) {
			String tempString = tableInfo.getProdCode().getStr();
			String[] itemStrings = tempString.split("\\[");
			String datetimeString = "";
			for (int i = 0; i < itemStrings.length; i++) {
				
				String tempdateString = itemStrings[i].replace("]", "").trim();
				if(tempdateString.equalsIgnoreCase("") || tempdateString == "") {
					continue;
				}
				if (tempdateString.contains(":")) {
					String[] dateSps = tempdateString.split(":");
					datetimeString += sp[Integer.parseInt(dateSps[0].trim())].substring(
							Integer.parseInt(dateSps[1].trim()),
							Integer.parseInt(dateSps[1].trim()) + Integer.parseInt(dateSps[2].trim()));
				} else {
					datetimeString += sp[Integer.parseInt(tempdateString.trim())];
				}
			}
			
			fileData.setProdCode(datetimeString);
			
		}
		
	}
	
	private void setFileDataStationID(AtsTableInfo tableInfo, String[] sp, CommonData fileData) {
		// 站号
		if (tableInfo.getStation().getType() == 0) {
			fileData.setReserveKeyOne(tableInfo.getStation().getStr());
		} else if(tableInfo.getStation().getType() == 1) {
			fileData.setReserveKeyOne(sp[tableInfo.getStation().getPos()]);
		} else if (tableInfo.getStation().getType() == 2) {
			fileData.setReserveKeyOne(sp[tableInfo.getStation().getPos()].substring(tableInfo.getStation().getStart(),
					tableInfo.getStation().getEnd()));
		} else {
			setReserveKeyOne(tableInfo, sp, fileData);
		}
	}
	
	private void setReserveKeyOne(AtsTableInfo tableInfo, String[] sp, CommonData fileData) {
		if (tableInfo.getStation().getType() == 3) {
			String tempString = tableInfo.getStation().getStr();
			String[] itemStrings = tempString.split("\\[");
			String datetimeString = "";
			for (int i = 0; i < itemStrings.length; i++) {
				
				String tempdateString = itemStrings[i].replace("]", "").trim();
				if(tempdateString.equalsIgnoreCase("") || tempdateString == "") {
					continue;
				}
				if (tempdateString.contains(":")) {
					String[] dateSps = tempdateString.split(":");
					datetimeString += sp[Integer.parseInt(dateSps[0].trim())].substring(
							Integer.parseInt(dateSps[1].trim()),
							Integer.parseInt(dateSps[1].trim()) + Integer.parseInt(dateSps[2].trim()));
				} else {
					datetimeString += sp[Integer.parseInt(tempdateString.trim())];
				}
			}
			
			fileData.setReserveKeyOne(datetimeString);
			
		}
	}

	/**
	 * 设置资料时间
	 * 
	 * @param tableInfo 配置信息
	 * @param sp        文件名信息
	 * @param fileData  CommonData 实体类
	 * @throws ParseException
	 */
	private void setFileDataTime(AtsTableInfo tableInfo, String[] sp, CommonData fileData) throws ParseException {
		// 资料时间
		if (tableInfo.getDataTime().getType() == 1) {
			if (sp[tableInfo.getDataTime().getPos()].length() == 14) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
				fileData.setTime(sdf1.parse(sp[tableInfo.getDataTime().getPos()]));
			} else if (sp[tableInfo.getDataTime().getPos()].length() == 12) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmm");
				fileData.setTime(sdf1.parse(sp[tableInfo.getDataTime().getPos()]));
			} else if (sp[tableInfo.getDataTime().getPos()].length() == 10) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhh");
				fileData.setTime(sdf1.parse(sp[tableInfo.getDataTime().getPos()]));
			} else {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
				fileData.setTime(sdf1.parse(sp[tableInfo.getDataTime().getPos()]));
			}
		} else if (tableInfo.getDataTime().getType() == 2) {
			if (sp[tableInfo.getDataTime().getPos()]
					.substring(tableInfo.getDataTime().getStart(), tableInfo.getDataTime().getEnd()).length() == 14) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
				fileData.setTime(sdf1.parse(sp[tableInfo.getDataTime().getPos()]));
			} else if (sp[tableInfo.getDataTime().getPos()]
					.substring(tableInfo.getDataTime().getStart(), tableInfo.getDataTime().getEnd()).length() == 12) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmm");
				fileData.setTime(sdf1.parse(sp[tableInfo.getDataTime().getPos()]));
			} else if (sp[tableInfo.getDataTime().getPos()]
					.substring(tableInfo.getDataTime().getStart(), tableInfo.getDataTime().getEnd()).length() == 10) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhh");
				fileData.setTime(sdf1.parse(sp[tableInfo.getDataTime().getPos()]));
			} else {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
				fileData.setTime(sdf1.parse(sp[tableInfo.getDataTime().getPos()]));
			}
		} else if (tableInfo.getDataTime().getType() == 3) {
			String tempString = tableInfo.getDataTime().getStr();
			String[] itemStrings = tempString.split("\\[");
			String datetimeString = "";
			for (int i = 0; i < itemStrings.length; i++) {
				
				String tempdateString = itemStrings[i].replace("]", "").trim();
				if(tempdateString.equalsIgnoreCase("") || tempdateString == "") {
					continue;
				}
				if (tempdateString.contains(":")) {
					String[] dateSps = tempdateString.split(":");
					datetimeString += sp[Integer.parseInt(dateSps[0].trim())].substring(
							Integer.parseInt(dateSps[1].trim()),
							Integer.parseInt(dateSps[1].trim()) + Integer.parseInt(dateSps[2].trim()));
				} else {
					datetimeString += sp[Integer.parseInt(tempdateString.trim())];
				}
			}

			if (datetimeString.length() == 14) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmmss");
				fileData.setTime(sdf1.parse(datetimeString));
			} else if (datetimeString.length() == 12) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhhmm");
				fileData.setTime(sdf1.parse(datetimeString));
			} else if (datetimeString.length() == 10) {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddhh");
				fileData.setTime(sdf1.parse(datetimeString));
			} else {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
				fileData.setTime(sdf1.parse(datetimeString));
			}
		} else if (tableInfo.getDataTime().getType() == 4) {
			// 配置年顺日的转换规则
			String tempString = tableInfo.getDataTime().getStr().replace("{", "").replace("}", "").replace("YYYYDDD",
					"");
			String[] items = tempString.substring(1).split("[|]");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			for (int i = 0; i < items.length; i++) {
				String tempStr = items[i].replace("[", "").replace("]", "");
				if (i == 0) {
					if (tempStr.contains(":")) {
						String[] tempStrings = tempStr.split(":");
						calendar.set(Calendar.YEAR,
								Integer.parseInt(sp[Integer.parseInt(tempStrings[0].trim())].substring(
										Integer.parseInt(tempStrings[1].trim()), Integer.parseInt(tempStrings[1].trim())
												+ Integer.parseInt(tempStrings[2].trim()))));
					} else {
						calendar.set(Calendar.YEAR, Integer.parseInt(sp[Integer.parseInt(tempStr.trim())]));
					}
				} else if (i == 1) {
					if (tempStr.contains(":")) {
						String[] tempStrings = tempStr.split(":");
						calendar.set(Calendar.DAY_OF_YEAR,
								Integer.parseInt(sp[Integer.parseInt(tempStrings[0].trim())].substring(
										Integer.parseInt(tempStrings[1].trim()), Integer.parseInt(tempStrings[1].trim())
												+ Integer.parseInt(tempStrings[2].trim()))));
					} else {
						calendar.set(Calendar.DAY_OF_YEAR, Integer.parseInt(sp[Integer.parseInt(tempStr.trim())]));
					}
				} else if (i == 2) {
					if (tempStr.contains(":")) {
						String[] tempStrings = tempStr.split(":");
						calendar.set(Calendar.HOUR_OF_DAY,
								Integer.parseInt(sp[Integer.parseInt(tempStrings[0].trim())].substring(
										Integer.parseInt(tempStrings[1].trim()), Integer.parseInt(tempStrings[1].trim())
												+ Integer.parseInt(tempStrings[2].trim()))));
					} else {
						calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(sp[Integer.parseInt(tempStr.trim())]));
					}
				} else if (i == 3) {
					if (tempStr.contains(":")) {
						String[] tempStrings = tempStr.split(":");
						calendar.set(Calendar.MINUTE,
								Integer.parseInt(sp[Integer.parseInt(tempStrings[0].trim())].substring(
										Integer.parseInt(tempStrings[1].trim()), Integer.parseInt(tempStrings[1].trim())
												+ Integer.parseInt(tempStrings[2].trim()))));
					} else {
						calendar.set(Calendar.MINUTE, Integer.parseInt(sp[Integer.parseInt(tempStr.trim())]));
					}
				} else if (i == 4) {
					if (tempStr.contains(":")) {
						String[] tempStrings = tempStr.split(":");
						calendar.set(Calendar.SECOND,
								Integer.parseInt(sp[Integer.parseInt(tempStrings[0].trim())].substring(
										Integer.parseInt(tempStrings[1].trim()), Integer.parseInt(tempStrings[1].trim())
												+ Integer.parseInt(tempStrings[2].trim()))));
					} else {
						calendar.set(Calendar.SECOND, Integer.parseInt(sp[Integer.parseInt(tempStr.trim())]));
					}
				}
			}

			fileData.setTime(calendar.getTime());
		}

	}

	/**
	 * @Title: getContent @Description: TODO(获取文件的字节数组) @param filePath
	 * 文件全路径 @return byte[] 字节数组 @throws
	 */
	public byte[] getContent(String filePath) {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		FileInputStream fi;
		try {
			fi = new FileInputStream(file);
			byte[] buffer = new byte[(int) fileSize];
			int offset = 0;
			int numRead = 0;
			while (offset < buffer.length && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
				offset += numRead;
			}
			// 确保所有数据均被读取
			if (offset != buffer.length) {
				logger.error("\n Could not completely read file " + file.getName());
			}
			fi.close();
			return buffer;
		} catch (IOException e) {
			logger.error("\n 读取文件错误:" + filePath + " \n " + e.getMessage());
			return null;
		}

	}

	/**
	 * 函数名：getBytes 消息处理函数
	 * 
	 * @param ：filePath 文件全路径
	 *
	 * @return 文件的byte数组
	 */
	public byte[] toByteArray(String filename) {

		FileChannel fc = null;
		try {
			randomAccessFile = new RandomAccessFile(filename, "r");
			fc = randomAccessFile.getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0) {
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		} catch (IOException e) {
			logger.error("\n 读取文件错误:" + filename + " \n " + e.getMessage());
			return null;
		} finally {
			try {
				if (fc != null)
					fc.close();
				if (randomAccessFile != null)
					randomAccessFile.close();
				randomAccessFile = null;
			} catch (IOException e) {
				logger.error("关闭文件错误:" + filename + ":" + e.getMessage());
			}
		}
	}
}
