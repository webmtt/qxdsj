package cma.cimiss2.dpc.decoder.grib.di_ei;

public class EiAttr 
{
	private String sequence; //序号
	private String SYSTEM; //业务系统名称
	private String GROUP_ID; //告警业务分组标识
	private String MSG_TYPE; //信息类型 03：告警事件信息；99：未知
	private String COL_TYPE; //采集方式  01:Rest API   02：消息
	private String DATA_FROM; //数据来源 各省4位编码。国家级：BABJ
	private String EVENT_TYPE; //事件类型
	private String EVENT_LEVEL; //事件级别  0:正常，1:提醒，2:警告，3:紧
	private String EVENT_TITLE; //事件标题
	private String KObject; //故障对象
	private String KEvent; //故障内容
	private String KResult; //故障结果
	private String KIndex; //关键信息：如发生告警的进程名称、资料编号或名称、算法名称等。
	private String KComment; //备注信息
	private String EVENT_SUGGEST; //事件处理建议
	private String EVENT_CONTROL; //事件处理导向
	private String EVENT_TRAG; //事件发生可能原因
	private String EVENT_EXT2; //事件发生节点名称，设备主机名称
	
	private String send; //是否发送
	
	public EiAttr(String sequence,String SYSTEM,String GROUP_ID,String MSG_TYPE,String COL_TYPE,String DATA_FROM
			,String EVENT_TYPE,String EVENT_LEVEL,String EVENT_TITLE,String KObject,String KEvent,String KResult
			,String KIndex,String KComment,String EVENT_SUGGEST,String EVENT_CONTROL,String EVENT_TRAG,String EVENT_EXT2
			,String send)
	{
		this.sequence = sequence;
		this.SYSTEM = SYSTEM;
		this.GROUP_ID = GROUP_ID;
		this.MSG_TYPE = MSG_TYPE;
		this.COL_TYPE = COL_TYPE;
		this.DATA_FROM = DATA_FROM;
		this.EVENT_TYPE = EVENT_TYPE;
		this.EVENT_LEVEL = EVENT_LEVEL;
		this.EVENT_TITLE = EVENT_TITLE;
		this.KObject = KObject;
		this.KEvent = KEvent;
		this.KResult = KResult;
		this.KIndex = KIndex;
		this.KComment = KComment;
		this.EVENT_SUGGEST = EVENT_SUGGEST;
		this.EVENT_CONTROL = EVENT_CONTROL;
		this.EVENT_TRAG = EVENT_TRAG;
		this.EVENT_EXT2 = EVENT_EXT2;
		this.send = send;
	}
	
	public String getsequence() 
	{
		return sequence;
	}
	
	public String getSYSTEM() 
	{
		return SYSTEM;
	}
	
	public String getGROUP_ID() 
	{
		return GROUP_ID;
	}
	
	public String getMSG_TYPE() 
	{
		return MSG_TYPE;
	}
	
	public String getCOL_TYPE() 
	{
		return COL_TYPE;
	}
	
	public String getDATA_FROM() 
	{
		return DATA_FROM;
	}
	
	public String getEVENT_TYPE() 
	{
		return EVENT_TYPE;
	}
	
	public String getEVENT_LEVEL() 
	{
		return EVENT_LEVEL;
	}
	
	public String getEVENT_TITLE() 
	{
		return EVENT_TITLE;
	}
	
	public String getKObject() 
	{
		return KObject;
	}
	
	public String getKEvent() 
	{
		return KEvent;
	}
	
	public String getKResult() 
	{
		return KResult;
	}
	
	public String getKIndex() 
	{
		return KIndex;
	}		
	
	public String getKComment() 
	{
		return KComment;
	}
	
	public String getEVENT_SUGGEST() 
	{
		return EVENT_SUGGEST;
	}
	
	public String getEVENT_CONTROL() 
	{
		return EVENT_CONTROL;
	}
	
	public String getEVENT_TRAG() 
	{
		return EVENT_TRAG;
	}
	
	public String getEVENT_EXT2() 
	{
		return EVENT_EXT2;
	}
	
	public String getsend() 
	{
		return send;
	}
}
