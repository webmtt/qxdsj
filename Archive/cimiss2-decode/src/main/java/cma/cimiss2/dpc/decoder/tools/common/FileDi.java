package cma.cimiss2.dpc.decoder.tools.common;

import java.io.Serializable;

import cma.cimiss2.dpc.decoder.tools.utils.TimeUtil;

/**
 * 
 * <br>
 * @Title:  FileDi.java   
 * @Package org.cimiss2.dwp.RADAR.bean   
 * @Description:    TODO(文件解码入库DI信息)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午5:11:26   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class FileDi  implements Serializable,Cloneable {
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = 1L;
	
	/**
	 * 台站号	IIiii	台站号
	 */
	private String IIiii;

	/**
	 * 资料编码	DATA_TYPE	本环节处理后的资料编码 及  SOD四级编码
	 */
	private String DATA_TYPE;
	
	/**
	 * 父代资料编码	DATA_TYPE_1	父代资料编码（若前后无变化同资料编码）
	 */
	private String DATA_TYPE_1;
	
	/**
	 * 资料来源	RECEIVE	
	 * 当业务系统为CTS并且环节为收集时，
	 * 为台站行政区划编码，其他业务系统及环节为上游系统编码
	 */
	private String RECEIVE;
	
	/**
	 * 资料去向	SEND	
	 * 当入库时，为目标数据库的业务标识名称。必填
	 * 如：BFDB、RADB、FIDB，见大数据云平台存储规范
	 */
	private String SEND;
	
	/**
	 * 当入库时，为目标数据库物理库名。如DRDS等，见大数据云平台存储规范
	 */
	private String SEND_PHYS;
	
	
	/**
	 * 资料传输时次	TRAN_TIME	资料生成传输时次，可到分钟，不能到秒
	 */
	private String TRAN_TIME;
	
	/**
	 * 资料业务时次	DATA_TIME	观测时次或预报时次，可到分钟，不能到秒
	 */
	private String DATA_TIME;
	
	/**
	 * 业务流程标识	DATA_FLOW	BDMAIN:大数据平台主流程； BDBAK ：大数据平台备份流程；
	 */
	private String DATA_FLOW;
	
	/**
	 * 业务系统	SYSTEM	业务系统名称 
	 */
	private String SYSTEM;
	
	/**
	 * 处理环节	PROCESS_LINK	业务系统关键业务环节
	 */
	private String PROCESS_LINK;
	
	/**
	 * 处理开始时间	PROCESS_START_TIME	业务环节开始处理时间
	 */
	private String PROCESS_START_TIME;
	
	/**
	 * 处理结束时间	PROCESS_END_TIME	业务环节结束处理时间
	 */
	private String PROCESS_END_TIME;
	
	/**
	 * 原始文件名	FILE_NAME_O	处理前文件名
	 */
	private String FILE_NAME_O;
	
	/**
	 * 新文件名	FILE_NAME_N	处理后文件名
	 */
	private String FILE_NAME_N;

	/**
	 * 文件大小	FILE_SIZE
	 */
	private String FILE_SIZE;
	
	/**
	 * 系统处理状态	PROCESS_STATE		每个业务环节处理的系统运行状态
	 */
	private String PROCESS_STATE;
	
	/**
	 * 业务状态	BUSINESS_STATE	业务环节处理的业务状态
	 */
	private String BUSINESS_STATE;
	
	/**
	 * 记录时间	RECORD_TIME	DI记录时间
	 */
	private String RECORD_TIME;
	/**
	 * 
	 * @Title:  FileDi   
	 * @Description:    TODO(这里用一句话描述这个方法的作用)   
	 * @param:    
	 * @throws
	 */
	public FileDi()
	{
		DATA_TYPE="";
		DATA_TYPE_1="";
		setRECEIVE("CTS2");
		setSEND("FIDB");
		TRAN_TIME="";
		DATA_TIME="";
		DATA_FLOW = "BDMAIN";
		setSYSTEM("DPC");
		setPROCESS_LINK("1");
		SEND_PHYS = "DRDS";
		PROCESS_START_TIME="";
		PROCESS_END_TIME="";
		FILE_NAME_O="";
		FILE_NAME_N="";
		PROCESS_STATE="";
		BUSINESS_STATE="";
	}
	
	
	
	public String getIIiii() {
		return IIiii;
	}

	public void setIIiii(String iIiii) {
		IIiii = iIiii;
	}

	public String getSEND_PHYS() {
		return SEND_PHYS;
	}

	public void setSEND_PHYS(String sEND_PHYS) {
		SEND_PHYS = sEND_PHYS;
	}

	public String getDATA_FLOW() {
		return DATA_FLOW;
	}

	public void setDATA_FLOW(String dATA_FLOW) {
		DATA_FLOW = dATA_FLOW;
	}

	public void setDATA_TYPE(String dATA_TYPE) {
		DATA_TYPE = dATA_TYPE;
	}
	
	public void setDATA_TYPE_1(String dATA_TYPE_1) {
		DATA_TYPE_1 = dATA_TYPE_1;
	}
	
	public void setRECEIVE(String rECEIVE) {
		RECEIVE = rECEIVE;
	}
	
	public void setSEND(String sEND) {
		SEND = sEND;
	}
	
	public void setTRAN_TIME(String tRAN_TIME) {
		
		TRAN_TIME = TimeUtil.date2String(TimeUtil.String2Date(tRAN_TIME, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm");
	}
	
	public void setDATA_TIME(String dATA_TIME) {
		DATA_TIME = TimeUtil.date2String(TimeUtil.String2Date(dATA_TIME, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm");;
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
	
	public void setRECORD_TIME(String rECORD_TIME) {
		RECORD_TIME = rECORD_TIME;
	}
	
	public String getDATA_TYPE() {
		return DATA_TYPE;
	}
	
	public String getDATA_TYPE_1() {
		return DATA_TYPE_1;
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

	public String getFILE_SIZE() {
		return FILE_SIZE;
	}

	public void setFILE_SIZE(String fILE_SIZE) {
		FILE_SIZE = fILE_SIZE;
	}
	
	@Override
	public FileDi clone() throws CloneNotSupportedException {
		FileDi fileDi = null;  
        try{  
        	fileDi = (FileDi)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return fileDi;  

	}
	
	
}
