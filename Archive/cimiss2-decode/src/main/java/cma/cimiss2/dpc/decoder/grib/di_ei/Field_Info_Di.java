package cma.cimiss2.dpc.decoder.grib.di_ei;
 
/**
 * @类名：Field_Info_Di
 * @类功能：Restful接口信息
 * @作者：zhengbo
 * @版权：国家气象信息中心
 * @创建时间：2017-11-03
 * @备注：用于国家信息中心监视系统
 * @变更：
 * 	
 */

public class Field_Info_Di
{
	String DATA_TYPE; //资料编码
	String DATA_TYPE_1; //父代资料编码
	String RECEIVE;  //资料来源
	String SEND;  //当入库时，为目标数据库的业务标识名称。如：BFDB、RADB、FIDB，见大数据云平台存储规范
	String SEND_PHYS; //当入库时，为目标数据库物理库名。如DRDS等，见大数据云平台存储规范 
	String TRAN_TIME;  //资料生成传输时次，可到分钟，不能到秒
	String DATA_TIME;  //观测时次或预报时次，可到分钟，不能到秒
	String DATA_FLOW; //BDMAIN:大数据平台主流程； BDBAK ：大数据平台备份流程；
	String SYSTEM;  //业务系统
	String PROCESS_LINK; //处理环节
	String PROCESS_START_TIME;  //业务环节开始处理时间
	String PROCESS_END_TIME;  //业务环节结束处理时间
	String FILE_NAME_O;  //处理前文件名
	String FILE_NAME_N;  //处理后文件名
	String PROCESS_STATE;  //系统处理状态,入库：1-正常，0-错误
	String BUSINESS_STATE;  //业务状态，当环节为1-解码、3-产品生成、4-全流程时，业务状态分为1-正常，0-错误
	String RECORD_TIME;  //记录时间
	String IIiii; //站号或文件名
	String FILE_SIZE; //文件大小
	String ELE_NAME; //要素名称：带层次类型，如：WIU_100
	String ELE_LEVEL; //层次
	
	String receive_time; //收到时间：收到CTS推送的文件的时间
	
	
	
	public void setDATA_TYPE(String DATA_TYPE) 
	{
		this.DATA_TYPE = DATA_TYPE;
	}
	
	public void setDATA_TYPE_1(String DATA_TYPE_1) 
	{
		this.DATA_TYPE_1 = DATA_TYPE_1;
	}
	
	public void setRECEIVE(String RECEIVE) 
	{
		this.RECEIVE = RECEIVE;
	}
	
	public void setSEND(String SEND) 
	{
		this.SEND = SEND;
	}
	
	public void setSEND_PHYS(String SEND_PHYS) 
	{
		this.SEND_PHYS = SEND_PHYS;
	}
	
	public void setTRAN_TIME(String TRAN_TIME) 
	{
		this.TRAN_TIME = TRAN_TIME;
	}
	
	public void setDATA_TIME(String DATA_TIME) 
	{
		this.DATA_TIME = DATA_TIME;
	}
	
	public void setDATA_FLOW(String DATA_FLOW) 
	{
		this.DATA_FLOW = DATA_FLOW;
	}
	
	public void setSYSTEM(String SYSTEM) 
	{
		this.SYSTEM = SYSTEM;
	}
	
	public void setPROCESS_LINK(String PROCESS_LINK) 
	{
		this.PROCESS_LINK = PROCESS_LINK;
	}
	
	public void setPROCESS_START_TIME(String PROCESS_START_TIME) 
	{
		this.PROCESS_START_TIME = PROCESS_START_TIME;
	}
	
	public void setPROCESS_END_TIME(String PROCESS_END_TIME) 
	{
		this.PROCESS_END_TIME = PROCESS_END_TIME;
	}
	
	public void setFILE_NAME_O(String FILE_NAME_O) 
	{
		this.FILE_NAME_O = FILE_NAME_O;
	}
	
	public void setFILE_NAME_N(String FILE_NAME_N) 
	{
		this.FILE_NAME_N = FILE_NAME_N;
	}
	
	public void setPROCESS_STATE(String PROCESS_STATE) 
	{
		this.PROCESS_STATE = PROCESS_STATE;
	}
	
	public void setBUSINESS_STATE(String BUSINESS_STATE) 
	{
		this.BUSINESS_STATE = BUSINESS_STATE;
	}
	
	public void setRECORD_TIME(String RECORD_TIME) 
	{
		this.RECORD_TIME = RECORD_TIME;
	}
	
	public void setIIiii(String IIiii) 
	{
		this.IIiii = IIiii;
	}
	
	public void setFILE_SIZE(String FILE_SIZE) 
	{
		this.FILE_SIZE = FILE_SIZE;
	}	
	
	public void setELE_NAME(String ELE_NAME) 
	{
		this.ELE_NAME = ELE_NAME;
	}
	
	public void setELE_LEVEL(String ELE_LEVEL) 
	{
		this.ELE_LEVEL = ELE_LEVEL;
	}
	
	public void setreceive_time(String receive_time) 
	{
		this.receive_time = receive_time;
	}
	
}