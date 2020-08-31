package cma.cimiss2.dpc.indb.common;

import java.util.concurrent.BlockingQueue;

import org.apache.tools.ant.types.resources.First;
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
public class LoopFolderThread2 implements Runnable{
	BlockingQueue<String> files;
	
	public LoopFolderThread2(BlockingQueue<String> files) {
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
				if(files.size()==0){
					if(StartConfig.getLoopQuitFlag()==true){//轮询完目录即停止程序，不配置默认为false
						break;
					}else{//轮询完目录不停程序
						try {
							double sleepSecond=StartConfig.getFileLoopSleepSecond();//不配置的话默认sleep为1秒
							long Millisecond=new Double(sleepSecond*1000).longValue();
							Thread.sleep(Millisecond);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
