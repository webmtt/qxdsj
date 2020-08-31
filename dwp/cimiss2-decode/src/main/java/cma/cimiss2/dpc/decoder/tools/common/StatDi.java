package cma.cimiss2.dpc.decoder.tools.common;

import java.io.Serializable;

/**
 * 
 * <br>
 * @Title:  StatDi.java   
 * @Package cma.cimiss2.dpc.decoder.tools.vo.common   
 * @Description:    TODO(台站级资料解码入库DI)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午5:49:55 wufy   Initial creation.
 * </pre>
 * 
 * @author wufy
 *
 *
 */
public class StatDi implements Serializable{
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = 1L;

	/**
	 * 资料编码	DATA_TYPE	
	 * 本环节处理后的资料编码
	 */
	private String DATA_TYPE;
	
	/**
	 * 父代资料编码	DATA_TYPE_1	
	 * 父代资料编码（若前后无变化同资料编码）
	 */
	private String DATA_TYPE_1;
	
	/**
	 * 报告类别	TT	
	 * 报文资料的TT项，若无此属性为空
	 */
	private String TT;
	
	/**
	 * 资料更正标识	DATA_UPDATE_FLAG	
	 * 站级资料的更正表示，如BBB项或文件名中的更正标识
	 */
	private String DATA_UPDATE_FLAG;
	
	/**
	 * 台站号	IIiii	
	 * 台站号
	 */
	private String IIiii;

	/**
	 * 经度 LONGTITUDE
	 */
	private String LONGTITUDE;
	/**
	 * 纬度 LATITUDE
	 */
	private String LATITUDE;
	/**
	 * 高度 HEIGHT
	 */
	private String HEIGHT ;
	/**
	 * 资料来源	RECEIVE	
	 * 当业务系统为CTS并且环节为收集时，为台站行政区划编码，其他业务系统及环节为上游系统编码
	 */
	private String RECEIVE;
	
	/**
	 * 资料去向	SEND	
	 * 下游系统编码
	 */
	private String SEND;
	
	/**
	 * 资料传输时次	TRAN_TIME	
	 * 资料生成传输时次
	 */
	private String TRAN_TIME;
	
	/**
	 * 资料业务时次	DATA_TIME	
	 * 观测时次或预报时次
	 */
	private String DATA_TIME;
	
	/**
	 * 业务流程标识	DATA_FLOW	
	 * BDMAIN:大数据平台主流程； BDBAK ：大数据平台备份流程；
	 */
	private String DATA_FLOW;
	/**
	 * 业务系统	SYSTEM	
	 * 业务系统名称， 
	 */
	private String SYSTEM;
	
	/**
	 * 处理环节	PROCESS_LINK	
	 * 业务系统关键业务环节
	 */
	private String PROCESS_LINK;
	
	/**
	 * 处理开始时间	PROCESS_START_TIME	
	 * 业务环节开始处理时间
	 */
	private String PROCESS_START_TIME;
	
	/**
	 * 处理结束时间	PROCESS_END_TIME	
	 * 业务环节结束处理时间
	 */
	private String PROCESS_END_TIME;
	
	/**
	 * 原始文件名	FILE_NAME_O	
	 * 处理前文件名
	 */
	private String FILE_NAME_O;
	
	/**
	 * 新文件名	FILE_NAME_N	
	 * 处理后文件名
	 */
	private String FILE_NAME_N;
	/**
	 * 文件大小 FILE_SIZE
	 */
	private String FILE_SIZE;
	/**
	 * 系统处理状态	PROCESS_STATE		
	 * 每个业务环节处理的系统运行状态
	 */
	private String PROCESS_STATE;
	
	/**
	 * 业务状态	BUSINESS_STATE	
	 * 业务环节处理的业务状态，不同环节的业务状态不同，如：
	 */
	private String BUSINESS_STATE;
	
	/**
	 * 记录时间	RECORD_TIME	
	 * DI记录时间
	 */
	private String RECORD_TIME;

	private String SEND_PHYS;

	public StatDi(){
		setSYSTEM("DPC");
		setRECEIVE("CTS2");
		setSEND("SOD");
		setPROCESS_LINK("1");
		
	}
	
	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	public void setDATA_TYPE_1(String dATA_TYPE_1) {
		DATA_TYPE_1 = dATA_TYPE_1;
	}
	public void setTT(String tT) {
		TT = tT;
	}
	public void setDATA_UPDATE_FLAG(String dATA_UPDATE_FLAG) {
		DATA_UPDATE_FLAG = dATA_UPDATE_FLAG;
	}
	public void setIIiii(String iIiii) {
		IIiii = iIiii;
	}
	public void setRECEIVE(String rECEIVE) {
		RECEIVE = rECEIVE;
	}
	public void setSEND(String sEND) {
		SEND = sEND;
	}
	public void setTRAN_TIME(String tRAN_TIME) {
		TRAN_TIME = tRAN_TIME;
	}
	public void setDATA_TIME(String dATA_TIME) {
		DATA_TIME = dATA_TIME;
	}
	public void setDATA_FLOW(String dATA_FLOW){
		DATA_FLOW = dATA_FLOW;
	}
	public void setSYSTEM(String sYSTEM) {
		SYSTEM = sYSTEM;
	}
	public void setPROCESS_LINK(String pROCESS_LINK) {
		PROCESS_LINK = pROCESS_LINK;
	}
	public void setPROCESS_START_TIME(String pROCESS_START_TIME) {
		PROCESS_START_TIME = pROCESS_START_TIME;
	}
	public void setPROCESS_END_TIME(String pROCESS_END_TIME) {
		PROCESS_END_TIME = pROCESS_END_TIME;
	}
	public void setFILE_NAME_O(String fILE_NAME_O) {
		FILE_NAME_O = fILE_NAME_O;
	}
	public void setFILE_NAME_N(String fILE_NAME_N) {
		FILE_NAME_N = fILE_NAME_N;
	}
	public void setPROCESS_STATE(String pROCESS_STATE) {
		PROCESS_STATE = pROCESS_STATE;
	}
	public void setBUSINESS_STATE(String bUSINESS_STATE) {
		BUSINESS_STATE = bUSINESS_STATE;
	}
	public void setRECORD_TIME(String rRECORD_TIME) {
		RECORD_TIME = rRECORD_TIME;
	}
	public String getDATA_TYPE() {
		return DATA_TYPE;
	}
	public String getDATA_TYPE_1() {
		return DATA_TYPE_1;
	}
	
	public String getTT() {
		return TT;
	}
	public String getDATA_UPDATE_FLAG() {
		return DATA_UPDATE_FLAG;
	}
	public String getIIiii() {
		return IIiii;
	}
	public String getRECEIVE() {
		return RECEIVE;
	}
	public String getSEND() {
		return SEND;
	}
	public String getTRAN_TIME() {
		return TRAN_TIME;
	}
	public String getDATA_TIME() {
		return DATA_TIME;
	}
	public String getDATA_FLOW(){
		return DATA_FLOW;
	}
	public String getSYSTEM() {
		return SYSTEM;
	}
	public String getPROCESS_LINK() {
		return PROCESS_LINK;
	}
	public String getPROCESS_START_TIME() {
		return PROCESS_START_TIME;
	}
	public String getPROCESS_END_TIME() {
		return PROCESS_END_TIME;
	}
	public String getFILE_NAME_O() {
		return FILE_NAME_O;
	}
	public String getFILE_NAME_N() {
		return FILE_NAME_N;
	}
	public String getPROCESS_STATE() {
		return PROCESS_STATE;
	}
	public String getBUSINESS_STATE() {
		return BUSINESS_STATE;
	}
	public String getRECORD_TIME() {
		return RECORD_TIME;
	}

	public String getLONGTITUDE() {
		return LONGTITUDE;
	}

	public void setLONGTITUDE(String LONGTITUDE) {
		this.LONGTITUDE = LONGTITUDE;
	}

	public String getLATITUDE() {
		return LATITUDE;
	}

	public void setLATITUDE(String LATITUDE) {
		this.LATITUDE = LATITUDE;
	}

	public String getHEIGHT() {
		return HEIGHT;
	}

	public void setHEIGHT(String HEIGHT) {
		this.HEIGHT = HEIGHT;
	}

	public String getFILE_SIZE() {
		return FILE_SIZE;
	}

	public void setFILE_SIZE(String FILE_SIZE) {
		this.FILE_SIZE = FILE_SIZE;
	}

	public String getSEND_PHYS() {
		return SEND_PHYS;
	}

	public void setSEND_PHYS(String sEND_PHYS) {
		SEND_PHYS = sEND_PHYS;
	}
}
