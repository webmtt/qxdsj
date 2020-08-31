package cma.cimiss2.dpc.indb.service;

import cma.cimiss2.dpc.indb.vo.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * @Title:  ServiceQueue.java   
 * @Package org.cimiss2.dwp.RADAR.service   
 * @Description:    TODO(内存数据交换)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月14日 上午8:55:31   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class ServiceQueue {
	private List<FileInfo> fileInfos;
	
	private static ServiceQueue serviceQueue = null;
	
	private ServiceQueue() {
		fileInfos = new ArrayList<FileInfo>();
	}
	
	public static ServiceQueue getServerQueue() {
		if(serviceQueue == null) {
			serviceQueue = new ServiceQueue();
		}
		return serviceQueue;
	}
	/**
	 * 将文件信息放入内存
	 * @param fileInfo
	 */
	public void add(FileInfo fileInfo) {
		this.fileInfos.add(fileInfo);
	}
	
	/**
	 * 获取固定大小的文件存入到ATS
	 * @param num
	 * @return
	 */
	public List<FileInfo> get(int num){
		List<FileInfo> results = new ArrayList<>();
		synchronized (fileInfos) {
			if(fileInfos.size() >= num) {
				for (int i = 0; i < num; i++) {
					results.add(fileInfos.get(0));
					fileInfos.remove(0);
				}
			}
		}
		return results;
	}

}
