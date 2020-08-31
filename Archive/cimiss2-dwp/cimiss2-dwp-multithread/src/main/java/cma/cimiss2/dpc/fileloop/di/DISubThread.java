package cma.cimiss2.dpc.fileloop.di;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import cma.cimiss2.dpc.fileloop.bean.FileDi;


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
public class DISubThread implements Runnable {
	private List<RestfulInfo> list = new ArrayList<>();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Override
	public void run() {
		int diff = 0; // 时间差，单位：秒
		long dt1 = 0; // 
	    long dt2 = 0; 
	    boolean first = true;
	    Date timerStart = new Date();
	    Date timerEnd = new Date();
		while (true) {
			try {
				timerEnd = new Date();
				FileDi statDi = DIQueues.queue.poll(1, TimeUnit.SECONDS);
				
				if(null != statDi) {
					RestfulInfo diInfo = new RestfulInfo();
					diInfo.setType("RT.DPC.SFILEDIR.DI");
					diInfo.setName("目录监视");
					diInfo.setMessage("目录中积压文件");
					diInfo.setFields(statDi);
					diInfo.setOccur_time(new Date().getTime());
					list.add(diInfo);
					
					if(first){
						// 批起始的判断
						timerStart = new Date();
						dt1 = dt2;
						first = false;
						diff = 0;
					}
					else{ 
						// 数据处理时间长度
						diff = (int) ((dt2 - dt1) / 1000);
					}
					// 如果DI个数达到200个、或者数据处理时间长度超过3s、或者处理时间与等待时间之和超过3秒，批量发送DI
					if(list.size() >= 200 || diff >= 3 || (timerEnd.getTime() - timerStart.getTime())/1000 >= 3){
						System.out.println("Batch Send DI ..., BatchSize = " + list.size());
						RestfulSendData.SendData(list);
						list.clear();
						first = true;
					}
				}else {
					
					Thread.sleep(1000*5);
				}
				
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
			
		}

	}

}
