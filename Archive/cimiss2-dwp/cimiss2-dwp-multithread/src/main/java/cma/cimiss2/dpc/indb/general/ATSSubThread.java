package cma.cimiss2.dpc.indb.general;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import cma.cimiss2.dpc.decoder.tools.common.FileDi;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
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
 * ATSSubThread 雷达卫星消息处理线程，主要功能为ATS
 * 返回结果集：无
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 12/6/2017   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 * 
 */
public class ATSSubThread implements Runnable {
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo");
	private ATS ats = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");
	private RandomAccessFile randomAccessFile;
	
	
	private BlockingQueue<FileDi> diQueues;
	public ATSSubThread(BlockingQueue<FileDi> diQueues) {
		this.diQueues = diQueues;
	}
	@SuppressWarnings("rawtypes")
	@Override
	public void run() {
		
		while (true) {
			try {
				ats = new ATSClient("config/");
				
				if (ats != null) {
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					Thread.sleep(1000 * 5);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				logger.error("\n ATSClient：连接错误 " + e.getMessage());
				continue;
			}
		}
		try {
			
			while(true) {
				
				while (true) {
					if (ats == null) {
						try {
							ats = new ATSClient();
							if (ats != null) {
								break;
							}
						} catch (Exception e) {
							Thread.sleep(1000 * 5);
							logger.error("\n ATSClient：连接错误 " + e.getMessage());
							continue;
						}
					} else {
						break;
					}
				}
				List<FileInfo> fileInfos = ServiceQueue.getServerQueue().get(1);
				if(fileInfos != null && fileInfos.size() > 0) {
					for (int i = 0; i < fileInfos.size(); i++) {
						Long t2 = System.currentTimeMillis();
						// 获取cts资料四级编码
						String cts_code = fileInfos.get(i).getCts_code();
						// 获取文件的绝对路径
						String filePath = fileInfos.get(i).getFilepath();
						// 判断入库策略是否配置
						if(ATSConfig.getInstance().getAtsTableInfos().containsKey(cts_code)) {
							AtsTableInfo tableInfo = ATSConfig.getInstance().getAtsTableInfos().get(cts_code);
							File file = new File(filePath);
							if(file.exists() && file.isFile()) {
								String [] sp = StringSplit.split(file.getName(), fileInfos.get(i).getSplitRegex());
								
								CommonData fileData = new CommonData();
								// 设置 文件名
								fileData.setDataName(file.getName());
								//设置文件格式
								fileData.setDataFormat(file.getName().substring(file.getName().lastIndexOf(".")+1));
								byte[] filebytes = getContent(filePath);
								if(filebytes == null) {
									continue;
								}
								fileData.setData(filebytes);
								
								String ath = file.getName().split("_")[file.getName().split("_").length-2];
								fileData.setReserveKeyOne(ath);
								// 文件大小
//								fileData.setFileSize(file.length());
								// 入库时间
								fileData.setIYMDHM(sdf.format(new Date()));
								// 四级编码
								fileData.setDataId(tableInfo.getDataId().getStr());
								//资料时间
								if(tableInfo.getDataTime().getType() == 1) {
									fileData.setTime(sdf.parse(sp[tableInfo.getDataTime().getPos()]));
								}
								//站号
								if(tableInfo.getStation().getType() == 1) {
									fileData.setProdCode(sp[tableInfo.getStation().getPos()]);
								}else if (tableInfo.getStation().getType()==2) {
									fileData.setProdCode(sp[tableInfo.getStation().getPos()].substring(tableInfo.getStation().getStart(), tableInfo.getStation().getEnd()));
								}else {
									fileData.setProdCode(tableInfo.getStation().getStr());
								}
								
								if(tableInfo.getProdCode().getType() == 1) {
									fileData.setReserveKeyTwo(sp[tableInfo.getProdCode().getPos()]);
								}else if (tableInfo.getProdCode().getType()==2) {
									fileData.setReserveKeyTwo(sp[tableInfo.getProdCode().getPos()].substring(tableInfo.getProdCode().getStart(), tableInfo.getProdCode().getEnd()));
								}else {
									fileData.setReserveKeyTwo(tableInfo.getProdCode().getStr());
								}
								
								if(tableInfo.getProductCenter().getType() == 1) {
									fileData.setProductCenter(sp[tableInfo.getProductCenter().getPos()]);
								}else if (tableInfo.getProductCenter().getType()==2) {
									fileData.setProductCenter(sp[tableInfo.getProductCenter().getPos()].substring(tableInfo.getProductCenter().getStart(), tableInfo.getProdCode().getEnd()));
								}else {
									fileData.setProductCenter(tableInfo.getProductCenter().getStr());
								}
								
								if(tableInfo.getProductDescription().getType() == 1) {
									fileData.setProductDescription(sp[tableInfo.getProductDescription().getPos()]);
								}else if (tableInfo.getProductDescription().getType()==2) {
									fileData.setProductDescription(sp[tableInfo.getProductDescription().getPos()].substring(tableInfo.getProductDescription().getStart(), tableInfo.getProdCode().getEnd()));
								}else {
									fileData.setProductDescription(tableInfo.getProductDescription().getStr());
								}
								
								if(tableInfo.getProductMethod().getType() == 1) {
									fileData.setProductMethod(sp[tableInfo.getProductMethod().getPos()]);
								}else if (tableInfo.getProductMethod().getType()==2) {
									fileData.setProductMethod(sp[tableInfo.getProductMethod().getPos()].substring(tableInfo.getProductMethod().getStart(), tableInfo.getProdCode().getEnd()));
								}else {
									fileData.setProductMethod(tableInfo.getProductMethod().getStr());
								}
								
								TableConfig tableConfig = LoadTableConfig.getInstance().getTablesMaps().get(cts_code);
								FileDi fileDI = ((FileDi) fileInfos.get(i).getFileDI()).clone();
								fileDI.setSEND("RADB");
//								String input = Json.toJson(fileData);
//								System.out.println(input);
								if(tableConfig != null && !tableConfig.getAtsTable().isEmpty()) {
//									boolean bret=ats.createStaFileDataTable(tableConfig.getAtsTable(),30*24*60*60);
									int result = 0;
									try {
										result = ats.writeCommonData(tableConfig.getAtsTable(), fileData);
									} catch (Exception e) {
										logger.error("\n ATSClient：连接错误 " + e.getMessage() + "---" + result);
										ats = null;
									} 
//									int result = ats.writeStaFileData(, fileData);
									if(result > 0) {
										fileDI.setPROCESS_STATE("0");
										sendEi(EIEventType.CASSANDRA_CONNECT_ERROE);
										logger.info("\n 数据正常文件入Cassandra："+filePath +"\n 入库时间为:"+(System.currentTimeMillis()-t2)+"毫秒");
									} else {
										logger.error("\n 数据文件入Cassandra异常：" + filePath+"\n 返回结果为：" + result);
									}
								} else {
									logger.error("\n 没有配置：" + cts_code+ "：入Cassandra的表名 ： "+ filePath);
								}
								
								fileDI.setPROCESS_END_TIME(TimeUtil.getSysTime());
								this.diQueues.offer(fileDI);
							} else {
								logger.error("\n 文件不存在："+filePath);
							}
						}else {
							logger.error("\n 没有配置：" + cts_code+ "：入Cassandra的入库策略 ： "+ filePath);
						}
					}
				}else {
					Thread.sleep(1000);
					continue;
				}
				
//				if(ats != null) {
//					ats.close();
//				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("\n ATSClient：连接错误 " + e.getMessage());
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
	/**
	 * @Title: getContent 
	 * @Description: TODO(获取文件的字节数组) 
	 * @param filePath 文件全路径
	 * @return  
	 *    byte[]  字节数组
	 * @throws
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
	        while (offset < buffer.length  
	        && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {  
	            offset += numRead;  
	        }  
	        // 确保所有数据均被读取  
	        if (offset != buffer.length) {  
	        	logger.error("\n Could not completely read file " + file.getName());  
	        }  
	        fi.close();  
	        return buffer;  
		} catch (IOException e) {
			logger.error("\n 读取文件错误:"+filePath+" \n " +e.getMessage());
			return null;
		}  
        
    }  
	/**
	 * 函数名：getBytes
	 * 消息处理函数
	 * @param ：filePath
	 *            文件全路径
	 *
	 * @return 文件的byte数组
	 */
	 public byte[] toByteArray(String filename){  
		  	
	        FileChannel fc = null;  
	        try {  
	            randomAccessFile = new RandomAccessFile(filename, "r");
				fc = randomAccessFile.getChannel();  
	            MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0,  
	                    fc.size()).load(); 
	            byte[] result = new byte[(int) fc.size()];  
	            if (byteBuffer.remaining() > 0) {   
	                byteBuffer.get(result, 0, byteBuffer.remaining());  
	            }  
	            return result;  
	        } catch (IOException e) {  
	            logger.error("\n 读取文件错误:"+filename+" \n " +e.getMessage());  
	            return null;
	        } finally {  
	            try {  
	            	if(fc != null)
	            		fc.close(); 
	                if(randomAccessFile != null)
	                	randomAccessFile.close();
	                randomAccessFile = null;
	            } catch (IOException e) {  
	            	logger.error("关闭文件错误:"+filename+":" +e.getMessage());  
	            }  
	        }  
	    }
}
