package cma.cimiss2.dpc.decoder.tools.common;

import java.io.Serializable;

/**
 * 
 * <br>
 * @Title:  EI.java   
 * @Package org.cimiss2.dwp.RADAR.bean   
 * @Description:    TODO(EI实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午5:05:15   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class EI  implements Serializable {
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = 1L;

	/**
	 * 业务系统名称	业务系统名称，如CTS
	 */
	private String SYSTEM;
	
	/**
	 * 告警业务分组标识	事件业务分类（监视点类别属性编号），如OP_DPC_Z_99
	 */
	private String GROUP_ID;
	
	/**
	 * 文本生成时间	数据生成时间。包含年、月、日、小时、分、秒。时间格式为 YYYYMMDDThhmmss
	 */
	private String ORG_TIME;
	
	/**
	 * 内容类型	用来标示信息类型 03：告警事件信息；99：未知。
	 */
	private String MSG_TYPE;
	
	/**
	 * 采集方式	01:Rest API   02：消息；
	 */
	private String COL_TYPE;
	
	/**
	 * 数据来源	各省4位编码。国家级：BABJ
	 */
	private String DATA_FROM;
	
	/**
	 * 事件类型	事件编号，命名规则详见第2章，如OP_DPC_Z-1-25-09
	 */
	private String EVENT_TYPE;
	
	/**
	 * 事件级别	0:正常，1:提醒，2:警告，3:紧急
	 */
	private String EVENT_LEVEL;
	
	/**
	 * 事件标题	最大长度为255字节
	 */
	private String EVENT_TITLE;
	
	/**
	 * 故障对象	发生告警的具体对象，例如某个业务环节、进程、设备等等
	 */
	private String KObject;
	
	/**
	 * 故障内容	发生的故障内容
	 */
	private String KEvent;
	
	/**
	 * 故障结果	导致的结果，例如某些业务异常
	 */
	private String KResult;
	
	/**
	 * 关键信息	关键索引信息。例如发生告警的进程名称、资料编号或名称、算法名称等。
	 */
	private String KIndex;
	
	/**
	 * 备注信息	备注信息。用于对该条告警进行补充说明。
	 */
	private String KComment;
	
	/**
	 * 事件发生时间	2017-06-16 12:33:00
	 */
	private String EVENT_TIME;
	
	/**
	 * 事件处理建议	事件处理建议
	 */
	private String EVENT_SUGGEST;
	
	/**
	 * 事件处理导向	事件处理导向
	 */
	private String EVENT_CONTROL;
	
	/**
	 * 事件发生可能原因	事件发生可能原因
	 */
	private String EVENT_TRAG;
	
	/**
	 * 气象告警文件名称	当事件类型为气象告警时，使用该字段描述气象告警的文件名称
	 */
	private String EVENT_EXT1;
	
	/**
	 * 事件发生节点名称	告警发生所在的设备主机名称
	 */
	private String EVENT_EXT2;
	
	/**
	 * @Title: setSYSTEM   
	 * @Description: TODO(当前业务系统名称)   
	 * @param: @param sYSTEM      
	 * @return: void      
	 * @throws
	 */
	public void setSYSTEM(String sYSTEM) {
		SYSTEM = sYSTEM;
	}
	
	/**
	 * @Title: setGROUP_ID   
	 * @Description: TODO(告警业务分组标识	事件业务分类（监视点类别属性编号）)   
	 * @param: @param gROUP_ID      
	 * @return: void      
	 * @throws
	 */
	public void setGROUP_ID(String gROUP_ID) {
		GROUP_ID = gROUP_ID;
	}
	
	/**
	 * @Title: setORG_TIME   
	 * @Description: TODO(文本生成时间	数据生成时间。包含年、月、日、小时、分、秒。
	 *                    时间格式为 YYYYMMDDThhmmss)   
	 * @param: @param oRG_TIME      
	 * @return: void      
	 * @throws
	 */
	public void setORG_TIME(String oRG_TIME) {
		ORG_TIME = oRG_TIME;
	}
	
	/**
	 * @Title: setMSG_TYPE   
	 * @Description: TODO(内容类型	用来标示信息类型 03：告警事件信息；99：未知。)   
	 * @param: @param mSG_TYPE      
	 * @return: void      
	 * @throws
	 */
	public void setMSG_TYPE(String mSG_TYPE) {
		MSG_TYPE = mSG_TYPE;
	}
	
	/**
	 * @Title: setCOL_TYPE   
	 * @Description: TODO( 采集方式	01:Rest API   02：消息；)   
	 * @param: @param cOL_TYPE      
	 * @return: void      
	 * @throws
	 */
	public void setCOL_TYPE(String cOL_TYPE) {
		COL_TYPE = cOL_TYPE;
	}
	
	/**
	 * @Title: setDATA_FROM   
	 * @Description: TODO(数据来源	各省4位编码。国家级：BABJ)   
	 * @param: @param dATA_FROM      
	 * @return: void      
	 * @throws
	 */
	public void setDATA_FROM(String dATA_FROM) {
		DATA_FROM = dATA_FROM;
	}
	
	/**
	 * @Title: setEVENT_TYPE   
	 * @Description: TODO(事件类型	事件编号，如OP_DPC_Z-1-25-09)   
	 * @param: @param eVENT_TYPE      
	 * @return: void      
	 * @throws
	 */
	public void setEVENT_TYPE(String eVENT_TYPE) {
		EVENT_TYPE = eVENT_TYPE;
	}
	/**
	 * @Title: setEVENT_LEVEL   
	 * @Description: TODO(事件级别	0:正常，1:提醒，2:警告，3:紧急)   
	 * @param: @param eVENT_LEVEL      
	 * @return: void      
	 * @throws
	 */
	public void setEVENT_LEVEL(String eVENT_LEVEL) {
		EVENT_LEVEL = eVENT_LEVEL;
	}
	
	/**
	 * 
	 * @Title: setEVENT_TITLE   
	 * @Description: TODO(事件标题)   
	 * @param: @param eVENT_TITLE      
	 * @return: void      
	 * @throws
	 */
	public void setEVENT_TITLE(String eVENT_TITLE) {
		EVENT_TITLE = eVENT_TITLE;
	}
	
	/**
	 * @Title: setKObject   
	 * @Description: TODO(故障对象	发生告警的具体对象，例如某个业务环节、进程、设备等等)   
	 * @param: @param kObject      
	 * @return: void      
	 * @throws
	 */
	public void setKObject(String kObject) {
		KObject = kObject;
	}
	
	/**
	 * @Title: setKEvent   
	 * @Description: TODO(故障内容	发生的故障内容)   
	 * @param: @param kEvent      
	 * @return: void      
	 * @throws
	 */
	public void setKEvent(String kEvent) {
		KEvent = kEvent;
	}
	
	/**
	 * @Title: setKResult   
	 * @Description: TODO(故障结果	导致的结果，例如某些业务异常)   
	 * @param: @param kResult      
	 * @return: void      
	 * @throws
	 */
	public void setKResult(String kResult) {
		KResult = kResult;
	}
	
	/**
	 * @Title: setKIndex   
	 * @Description: TODO(关键信息	关键索引信息。例如发生告警的进程名称、资料编号或名称、算法名称等。)   
	 * @param: @param kIndex      
	 * @return: void      
	 * @throws
	 */
	public void setKIndex(String kIndex) {
		KIndex = kIndex;
	}
	
	/**
	 * @Title: setKComment   
	 * @Description: TODO(备注信息	备注信息。用于对该条告警进行补充说明。)   
	 * @param: @param kComment      
	 * @return: void      
	 * @throws
	 */
	public void setKComment(String kComment) {
		KComment = kComment;
	}
	
	/**
	 * @Title: setEVENT_TIME   
	 * @Description: TODO(事件发生时间	2017-06-16 12:33:00)   
	 * @param: @param eVENT_TIME      
	 * @return: void      
	 * @throws
	 */
	public void setEVENT_TIME(String eVENT_TIME) {
		EVENT_TIME = eVENT_TIME;
	}
	
	/**
	 * @Title: setEVENT_SUGGEST   
	 * @Description: TODO(事件处理建议	事件处理建议)   
	 * @param: @param eVENT_SUGGEST      
	 * @return: void      
	 * @throws
	 */
	public void setEVENT_SUGGEST(String eVENT_SUGGEST) {
		EVENT_SUGGEST = eVENT_SUGGEST;
	}
	
	/**
	 * @Title: setEVENT_CONTROL   
	 * @Description: TODO(事件处理导向	事件处理导向)   
	 * @param: @param eVENT_CONTROL      
	 * @return: void      
	 * @throws
	 */
	public void setEVENT_CONTROL(String eVENT_CONTROL) {
		EVENT_CONTROL = eVENT_CONTROL;
	}
	
	/**
	 * @Title: setEVENT_TRAG   
	 * @Description: TODO(事件发生可能原因	事件发生可能原因)   
	 * @param: @param eVENT_TRAG      
	 * @return: void      
	 * @throws
	 */
	public void setEVENT_TRAG(String eVENT_TRAG) {
		EVENT_TRAG = eVENT_TRAG;
	}
	
	/**
	 * @Title: setEVENT_EXT1   
	 * @Description: TODO(气象告警文件名称	当事件类型为气象告警时，使用该字段描述气象告警的文件名称)   
	 * @param: @param eVENT_EXT1      
	 * @return: void      
	 * @throws
	 */
	public void setEVENT_EXT1(String eVENT_EXT1) {
		EVENT_EXT1 = eVENT_EXT1;
	}
	
	/**
	 * @Title: setEVENT_EXT2   
	 * @Description: TODO(事件发生节点名称	告警发生所在的设备主机名称)   
	 * @param: @param eVENT_EXT2      
	 * @return: void      
	 * @throws
	 */
	public void setEVENT_EXT2(String eVENT_EXT2) {
		EVENT_EXT2 = eVENT_EXT2;
	}
	public String getSYSTEM() {
		return SYSTEM;
	}
	public String getGROUP_ID() {
		return GROUP_ID;
	}
	public String getORG_TIME() {
		return ORG_TIME;
	}
	public String getMSG_TYPE() {
		return MSG_TYPE;
	}
	public String getCOL_TYPE() {
		return COL_TYPE;
	}
	public String getDATA_FROM() {
		return DATA_FROM;
	}
	public String getEVENT_TYPE() {
		return EVENT_TYPE;
	}
	public String getEVENT_LEVEL() {
		return EVENT_LEVEL;
	}
	public String getEVENT_TITLE() {
		return EVENT_TITLE;
	}
	public String getKObject() {
		return KObject;
	}
	public String getKEvent() {
		return KEvent;
	}
	public String getKResult() {
		return KResult;
	}
	public String getKIndex() {
		return KIndex;
	}
	public String getKComment() {
		return KComment;
	}
	public String getEVENT_TIME() {
		return EVENT_TIME;
	}
	public String getEVENT_SUGGEST() {
		return EVENT_SUGGEST;
	}
	public String getEVENT_CONTROL() {
		return EVENT_CONTROL;
	}
	public String getEVENT_TRAG() {
		return EVENT_TRAG;
	}
	public String getEVENT_EXT1() {
		return EVENT_EXT1;
	}
	public String getEVENT_EXT2() {
		return EVENT_EXT2;
	}
	
	

}
