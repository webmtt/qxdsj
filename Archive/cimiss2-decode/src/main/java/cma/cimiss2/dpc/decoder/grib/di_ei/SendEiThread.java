package cma.cimiss2.dpc.decoder.grib.di_ei;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.grib.Grib_Struct_Data;

public class SendEiThread extends Thread
{
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	private String EVENT_TYPE;
	private String KEvent;
	private String DATETIME;
	private String data_id;
	
	public SendEiThread(String EVENT_TYPE, String KEvent, String DATETIME, String data_id)
	{
		this.EVENT_TYPE = EVENT_TYPE;
		this.KEvent = KEvent;
		this.DATETIME = DATETIME;
		this.data_id = data_id;
	}
	
	public void run()
	{
		//发送rest EI
		send_ei();
	}
	
	//发送restful的ei
	private void send_ei()
	{
		logger.info("construct ei message");
		//构造RestfulInfo
		RestfulInfo Rest_info = new RestfulInfo();
		Rest_info.setType("SYSTEM.ALARM.EI");
		Rest_info.setName("EI告警信息");
		Rest_info.setMessage("EI告警信息");
		
		String sys_time = getSysTime(); //yyyy-MM-dd HH:mm:ss
		String org_time = sys_time.replaceAll("-", "");
		org_time = org_time.replaceAll(":", "");
		org_time = org_time.replaceAll(" ", "T"); //yyyyMMddHHmmss
		
		EiAttr eiAttr = EiConfig.get_map_data_description().get(EVENT_TYPE);
		
		//构造Field_Info_Ei信息
		Field_Info_Ei filed_info = new Field_Info_Ei();
		filed_info.setSYSTEM(eiAttr.getSYSTEM()); //SYSTEM
		filed_info.setGROUP_ID(eiAttr.getGROUP_ID()); //GROUP_ID
		filed_info.setORG_TIME(org_time); //ORG_TIME
		filed_info.setMSG_TYPE(eiAttr.getMSG_TYPE()); //MSG_TYPE
		filed_info.setCOL_TYPE(eiAttr.getCOL_TYPE()); //COL_TYPE
		filed_info.setDATA_FROM(eiAttr.getDATA_FROM()); //DATA_FROM
		filed_info.setEVENT_TYPE(EVENT_TYPE); //EVENT_TYPE
		filed_info.setEVENT_LEVEL(eiAttr.getEVENT_LEVEL()); //EVENT_LEVEL
		
		String event_title = eiAttr.getEVENT_TITLE().replace("${数据时次}", DATETIME);
		filed_info.setEVENT_TITLE(event_title); //EVENT_TITLE
		
		filed_info.setKObject(eiAttr.getKObject()); //KObject
		filed_info.setKEvent(KEvent); //KEvent
		filed_info.setKResult(eiAttr.getKResult()); //KResult
		filed_info.setKIndex(data_id.replace("R001", "S001")); //KIndex：四级编码
		filed_info.setKComment(eiAttr.getKComment()); //KComment
		filed_info.setEVENT_TIME(sys_time); //EVENT_TIME
		filed_info.setEVENT_SUGGEST(eiAttr.getEVENT_SUGGEST()); //EVENT_SUGGEST
		filed_info.setEVENT_CONTROL(eiAttr.getEVENT_CONTROL()); //EVENT_CONTROL
		filed_info.setEVENT_TRAG(eiAttr.getEVENT_TRAG()); //EVENT_TRAG
		//filed_info.setEVENT_EXT1("");
		filed_info.setEVENT_EXT2(EiConfig.getlocal_ip()); //EVENT_EXT2：本机ip
		
		Rest_info.setFields(filed_info);
		
		logger.info("eiAttr.getsend()=" + eiAttr.getsend());
		if(eiAttr.getsend().equalsIgnoreCase("1"))
		{		
			logger.info("send restinfo ei data begin...");		
			RestfulSendData restful_send_data = new RestfulSendData();
			restful_send_data.SendData_single(Rest_info);
			logger.info("send restinfo ei data end...");
			logger.info("send restinfo ei complete.");
		}
	}
	
	/**
	* 获得系统时间
	* 
	* @param
	*/
	public String getSysTime()
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = new Date();
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为国际时GMT
	    String currentSysTime = dateFormat.format(date);
	    return currentSysTime;
	}	
	
}
