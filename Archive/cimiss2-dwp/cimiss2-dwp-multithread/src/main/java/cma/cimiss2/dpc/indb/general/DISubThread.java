package cma.cimiss2.dpc.indb.general;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import cma.cimiss2.dpc.decoder.tools.common.FileDi;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.SateFileDI;

/**
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  DISubThread.java   
 * @Package org.cimiss2.dwp.z_agme   
 * @Description:    TODO(DI发送子线程)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月25日 下午3:49:21   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *---------------------------------------------------------------------------------
 */
public class DISubThread<T> implements Runnable {
	private BlockingQueue<T> queue;
	private List<RestfulInfo> list = new ArrayList<>();
	public DISubThread(BlockingQueue<T> queue) {
		this.queue = queue;
	}
	@Override
	public void run() {
		while (true) {
			try {
				
				T fileDi = queue.poll(1, TimeUnit.SECONDS);
				System.out.println("T fileDi = queue.poll(1, TimeUnit.SECONDS)--------------------->>");
				if(null != fileDi) {
					RestfulInfo diInfo = new RestfulInfo();
					diInfo.setType("RT.DPC.STATION.DI");
					diInfo.setName("文件级资料处理详细信息");
					diInfo.setMessage("文件级资料处理详细信息");
					
					try {
//						System.out.println(((SateFileDI)fileDi).getDATA_TIME()+"=================");
						if(fileDi instanceof SateFileDI) {
							
							diInfo.setOccur_time(TimeUtil.String2Date(((SateFileDI)fileDi).getDATA_TIME(), "yyyy-MM-dd HH:mm").getTime());
						}else if (fileDi instanceof FileDi) {
							diInfo.setOccur_time(TimeUtil.String2Date(((FileDi)fileDi).getDATA_TIME(), "yyyy-MM-dd HH:mm").getTime());
						}else {
							diInfo.setOccur_time(new Date().getTime());
						}
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
					
					diInfo.setFields(fileDi);
					
					list.add(diInfo);
					//DI线程启动
			        if (!StartConfig.isSendDi()) {
			        	list.clear();
					}else {
						RestfulSendData.SendData(list);
						list.clear();
					}
					
				}else {
					System.out.println("Thread.sleep(1000*5)===============================>>");
					Thread.sleep(1000*5);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}		
		}
	}
}
