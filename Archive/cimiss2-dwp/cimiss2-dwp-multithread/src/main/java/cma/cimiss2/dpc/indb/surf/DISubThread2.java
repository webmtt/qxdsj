package cma.cimiss2.dpc.indb.surf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.RestfulSendData2;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;

import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;

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
public class DISubThread2 implements Runnable {
	private BlockingQueue<StatDi> queue;
	private List<RestfulInfo> list = new ArrayList<>();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	public DISubThread2(BlockingQueue<StatDi> queue) {
		this.queue = queue;
	}
	
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
				StatDi statDi = queue.poll(1, TimeUnit.SECONDS);
				
				if(null != statDi) {
					dt2 = simpleDateFormat.parse(statDi.getPROCESS_START_TIME()).getTime();
					RestfulInfo diInfo = new RestfulInfo();
					diInfo.setType("RT.DPC.STATION.DI");
					diInfo.setName("地面资料处理详细信息");
					diInfo.setMessage("地面资料处理详细信息");
					diInfo.setOccur_time(TimeUtil.String2Date(statDi.getDATA_TIME(), "yyyy-MM-dd HH:mm").getTime());
//					statDi.setDATA_FLOW(StartConfig.getDataFlow());
					diInfo.setFields(statDi);
					list.add(diInfo);
					if(!StartConfig.isSendCimissDi()){
						list.clear();
					}
					else{
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
							System.out.println("Batch Send cimiss DI ..., BatchSize = " + list.size());
							RestfulSendData2.SendData(list, SendType.DI);
							list.clear();
							first = true;
						}
					}
				}else {
					if(StartConfig.isSendCimissDi() && list.size() >0){
						System.out.println("Batch Send Cimiss DI ..., BatchSize = " + list.size());
						RestfulSendData2.SendData(list, SendType.DI);
						list.clear();
						first = true;
					}
					Thread.sleep(1000*5);
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				continue;
			}catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}

}
