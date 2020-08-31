package cma.cimiss2.dpc.indb.common;

import java.util.concurrent.BlockingQueue;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.FileUtil;

/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  LoopFolderThread.java   
 * @Package cma.cimiss2.dpc.indb.common   
 * @Description:    TODO(循环遍历文件夹)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年6月21日 下午10:43:50   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class LoopFolderThread implements Runnable{
	BlockingQueue<String> files;
	
	public LoopFolderThread(BlockingQueue<String> files) {
		this.files = files;
	}
	@Override
	public void run() {
		while(true) {
			if(files.size() > 10000) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else {
				FileUtil.traverseFolder(StartConfig.fileLoopPath(), files);
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
