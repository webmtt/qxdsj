package cma.cimiss2.dpc.indb.framework.rmq;

import cma.cimiss2.dpc.indb.framework.rmq.common.DetailRes;
import cma.cimiss2.dpc.indb.framework.rmq.common.MessageBean;
/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: </b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName: 新增资料SDK</b> cimiss2-dwp-multithread
* <br><b>PackageName:</b> cma.cimiss2.dpc.indb.framework.rmq
* <br><b>ClassName: MessageProcess 消息处理接口类，自定义类实现该类的方法</b> MessageProcess
* <br><b>Date:</b> 2020年1月6日 下午6:06:42
 */
public interface MessageProcess {
	/**
	 * @param messageBean 消息实体类
	 * @return DetailRes 消息处理的状态
	 */
	DetailRes process(MessageBean messageBean);
}
