package cma.cimiss2.dpc.decoder.grib.di_ei;
 
/**
 * @类名：Field_Info_Ei
 * @类功能：Restful接口信息
 * @作者：zhengbo
 * @版权：国家气象信息中心
 * @创建时间：2017-12-28
 * @备注：用于国家信息中心监视系统
 * @变更：
 * 	
 */

public class Field_Info_Ei
{
	String SYSTEM; //业务系统名称
	String GROUP_ID; //监视点的类别编号
	String ORG_TIME; //数据生成时间，格式： YYYYMMDDThhmmss
	String MSG_TYPE; //信息类型 03：告警事件信息；99：未知
	String COL_TYPE; //采集方式,02：消息
	String DATA_FROM; //数据来源,各省4位编码。国家级：BABJ
	String EVENT_TYPE; //事件编号
	String EVENT_LEVEL; //事件级别,0:正常，1:提醒，2:警告，3:紧急
	String EVENT_TITLE; //事件标题
	String KObject; //故障对象
	String KEvent; //故障内容
	
	public void setSYSTEM(String sYSTEM) {
		SYSTEM = sYSTEM;
	}
	public void setGROUP_ID(String gROUP_ID) {
		GROUP_ID = gROUP_ID; 
	}
	public void setORG_TIME(String oRG_TIME) {
		ORG_TIME = oRG_TIME;
	}
	public void setMSG_TYPE(String mSG_TYPE) {
		MSG_TYPE = mSG_TYPE;
	}
	public void setCOL_TYPE(String cOL_TYPE) {
		COL_TYPE = cOL_TYPE;
	}
	public void setDATA_FROM(String dATA_FROM) {
		DATA_FROM = dATA_FROM;
	}
	public void setEVENT_TYPE(String eVENT_TYPE) {
		EVENT_TYPE = eVENT_TYPE;
	}
	public void setEVENT_LEVEL(String eVENT_LEVEL) {
		EVENT_LEVEL = eVENT_LEVEL;
	}
	public void setEVENT_TITLE(String eVENT_TITLE) {
		EVENT_TITLE = eVENT_TITLE;
	}
	public void setKObject(String kObject) {
		KObject = kObject;
	}
	public void setKEvent(String kEvent) {
		KEvent = kEvent;
	}
	public void setKResult(String kResult) {
		KResult = kResult;
	}
	public void setKIndex(String kIndex) {
		KIndex = kIndex;
	}
	public void setKComment(String kComment) {
		KComment = kComment;
	}
	public void setEVENT_TIME(String eVENT_TIME) {
		EVENT_TIME = eVENT_TIME;
	}
	public void setEVENT_SUGGEST(String eVENT_SUGGEST) {
		EVENT_SUGGEST = eVENT_SUGGEST;
	}
	public void setEVENT_CONTROL(String eVENT_CONTROL) {
		EVENT_CONTROL = eVENT_CONTROL;
	}
	public void setEVENT_TRAG(String eVENT_TRAG) {
		EVENT_TRAG = eVENT_TRAG;
	}
	public void setEVENT_EXT1(String eVENT_EXT1) {
		EVENT_EXT1 = eVENT_EXT1;
	}
	public void setEVENT_EXT2(String eVENT_EXT2) {
		EVENT_EXT2 = eVENT_EXT2;
	}
	String KResult; //故障结果
	String KIndex; //关键信息
	String KComment; //备注信息
	String EVENT_TIME; //事件发生时间
	String EVENT_SUGGEST; //事件处理建议
	String EVENT_CONTROL; //事件处理导向
	String EVENT_TRAG; //事件发生可能原因
	String EVENT_EXT1; //气象告警文件名称
	String EVENT_EXT2; //事件发生节点名称
	
}