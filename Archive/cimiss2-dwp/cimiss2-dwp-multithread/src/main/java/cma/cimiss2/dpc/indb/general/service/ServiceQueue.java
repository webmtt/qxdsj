package cma.cimiss2.dpc.indb.general.service;

import java.util.ArrayList;
import java.util.List;

import cma.cimiss2.dpc.indb.general.vo.FileInfo;
/**
 * <br>
 * @Title:  ServiceQueue.java   
 * @Package org.cimiss2.dwp.RADAR.service   
 * @Description:    TODO(内存数据交换)
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月14日 上午8:55:31   wuzuoqiang    Initial creation.
 * </pre>
 * @author wuzuoqiang
 */
public class ServiceQueue {
	@SuppressWarnings("rawtypes")
	private List<FileInfo> fileInfos;
	
	private static ServiceQueue serviceQueue ;
	
	@SuppressWarnings("rawtypes")
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
	@SuppressWarnings("rawtypes")
	public void add(FileInfo fileInfo) {
		this.fileInfos.add(fileInfo);
	}
	
	public int getCount() {
		return this.fileInfos.size();
	}
	
	/**
	 * 获取固定大小的文件存入到ATS
	 * @param num
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<FileInfo> get(int num){
		List<FileInfo> results = new ArrayList<>();
		synchronized (fileInfos) {
			try {
				if(fileInfos.size() >= num) {
					System.out.println("List<FileInfo> get(int num)=======================> "+fileInfos.size());
					for (int i = 0; i < num; i++) {
						results.add(fileInfos.get(0));
						fileInfos.remove(0);
					}
				}else {
					for (int i = 0; i < fileInfos.size(); i++) {
						results.add(fileInfos.get(0));
						fileInfos.remove(0);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return results;
	}

}
