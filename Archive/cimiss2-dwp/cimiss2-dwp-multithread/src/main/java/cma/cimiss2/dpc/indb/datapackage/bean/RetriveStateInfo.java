package cma.cimiss2.dpc.indb.datapackage.bean;
/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description: 归档回取返回处理完成后发送的信息</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-dwp-multithread
* <br><b>PackageName:</b> cma.cimiss2.dpc.indb.datapackage.bean
* <br><b>ClassName:</b> RetriveStateInfo
* <br><b>Date:</b> 2019年5月11日 上午9:58:47
 */
public class RetriveStateInfo {
	/** 归档回取请求号 */
	private String retrieveId;
	/** 资料类型,Cts四级编码 */
	private String dataType;
	/** 资料回取开始时间,格式YYYYMMDDHHMISS */
	private String dataStartTime;
	/** 资料回取结束时间,格式YYYYMMDDHHMISS */
	private String dataEndTime;
	/** 处理环节 */
	private int procLink; // 0-music 1-归档 2-解码入库 3-存储管理系统清除
	/** 处理状态 */
	private int procState; //0-成功 1-失败 2-处理中
	/** 环节开始时间,格式YYYYMMDDHHMISS */
	private String linkStartTime;
	/** 环节结束时间,格式YYYYMMDDHHMISS */
	private String linkEndTime;

	public String getRetrieveId() {
		return retrieveId;
	}

	public void setRetrieveId(String retrieveId) {
		this.retrieveId = retrieveId;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataStartTime() {
		return dataStartTime;
	}

	public void setDataStartTime(String dataStartTime) {
		this.dataStartTime = dataStartTime;
	}

	public String getDataEndTime() {
		return dataEndTime;
	}

	public void setDataEndTime(String dataEndTime) {
		this.dataEndTime = dataEndTime;
	}

	public int getProcLink() {
		return procLink;
	}

	public void setProcLink(int procLink) {
		this.procLink = procLink;
	}

	public int getProcState() {
		return procState;
	}

	public void setProcState(int procState) {
		this.procState = procState;
	}

	public String getLinkStartTime() {
		return linkStartTime;
	}

	public void setLinkStartTime(String linkStartTime) {
		this.linkStartTime = linkStartTime;
	}

	public String getLinkEndTime() {
		return linkEndTime;
	}

	public void setLinkEndTime(String linkEndTime) {
		this.linkEndTime = linkEndTime;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}