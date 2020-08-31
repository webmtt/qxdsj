package cma.cimiss2.dpc.decoder.grib.di_ei;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.grib.Grib_Struct_Data;
import cma.cimiss2.dpc.decoder.grib.GribConfig;
import cma.cimiss2.dpc.decoder.grib.DataAttr;

public class SendDiThread extends Thread
{
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	private Grib_Struct_Data grib_struc_data;
	private String db_type; //入库类型：fidb（索引库）或radb（实时库Cassandra）
	
	public SendDiThread(Grib_Struct_Data grib_struc_data,String db_type)
	{
		this.grib_struc_data = grib_struc_data;
		this.db_type = db_type;
	}
	
	@Override
	public void run()
	{
		super.run();
		
		//发送rest DI
		send_di();
	}
	
	//发送restful的di
	private void send_di()
	{	
		
		//logger.info("构造Rest_info的di");			
			
		//构造RestfulInfo
		RestfulInfo Rest_info = new RestfulInfo();
		Rest_info.setType("RT.DPC.STATION.DI");
		Rest_info.setName("文件级资料处理详细信息");
		Rest_info.setMessage("文件级资料处理详细信息");
		
		//构造Field_Info_Di信息
		Field_Info_Di filed_info = new Field_Info_Di();
		
		filed_info.setDATA_TYPE(grib_struc_data.getdata_id().replace("R001", "S001"));   //资料四级编码
		filed_info.setDATA_TYPE_1(grib_struc_data.getdata_id());  //资料上一级的四级编码
		
		DataAttr data_attributes = GribConfig.get_map_data_description().get(grib_struc_data.getdata_id());
		String original_filename_array[] = grib_struc_data.getV_FILE_NAME_SOURCE().split("/");
		String original_pure_name = original_filename_array[original_filename_array.length-1];
		String pure_name_array[] = original_pure_name.split("_");
		String tran_time = pure_name_array[data_attributes.gettime_index_of_filename()-1].substring(0, 10); 
		tran_time = tran_time.substring(0, 4) + "-" + tran_time.substring(4, 6) + "-" + tran_time.substring(6, 8)
					+ " " + tran_time.substring(8, 10) + ":00";
		filed_info.setTRAN_TIME(tran_time); //tran_time：原始文件中解析得到的时间,如：Z_NAFP_C_BABJ_20180725060000_P_CNPC-T639-GMFS-HNEHE-02100.grib2
		filed_info.setRECEIVE("CTS2");  //资料来源
		
		if(db_type.equalsIgnoreCase("fidb")) //
		{
			filed_info.setSEND(GribConfig.getSEND());  //资料去向，目标数据库的业务标识名称
			filed_info.setSEND_PHYS(GribConfig.getSEND_PHYS()); //DI发送： 物理数据库名称
		}
		else if(db_type.equalsIgnoreCase("radb"))
		{
			filed_info.setSEND(GribConfig.getSEND2());  //资料去向，目标实时数据库的业务标识名称
			filed_info.setSEND_PHYS(GribConfig.getSEND_PHYS2()); //DI发送： 实时物理数据库名称
		}
			
		
		filed_info.setDATA_FLOW(GribConfig.getDATA_FLOW()); //DI发送： BDMAIN:大数据平台主流程； BDBAK ：大数据平台备份流程
		
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String receive_time = dateFormat2.format(grib_struc_data.getrcv_time());
		filed_info.setreceive_time(receive_time);   //资料收到时间
		
		String datetime = grib_struc_data.getDATETIME().substring(0, 16); //只保留到分：yyyy-MM-dd HH:mm
		filed_info.setDATA_TIME(datetime);  //资料业务时次
		
		try
		{
			long occ_time1 = dateFormat2.parse(grib_struc_data.getDATETIME()).getTime(); 
			Rest_info.setoccur_time(String.valueOf(occ_time1));//occur_time：资料时次的时间戳			
		}
		catch (ParseException e) 
		{  
            e.printStackTrace();  
        }  
		
		filed_info.setIIiii(grib_struc_data.getV_FILE_NAME()); //IIiii：文件名
		
		filed_info.setSYSTEM("DPC"); //业务系统
		filed_info.setPROCESS_LINK("1"); //业务系统关键业务环节：1-入库	
		
		
		String start_time = dateFormat2.format(grib_struc_data.getstart_time());
		filed_info.setPROCESS_START_TIME(start_time); //处理开始时间
		
		String end_time = dateFormat2.format(grib_struc_data.getend_time());
		filed_info.setPROCESS_END_TIME(end_time); //处理结束时间
		
		filed_info.setFILE_NAME_O(grib_struc_data.getV_FILE_NAME_SOURCE()); //原文件名
		filed_info.setFILE_NAME_N(grib_struc_data.getV_FILE_NAME()); //新文件名
		
		filed_info.setFILE_SIZE(String.valueOf(grib_struc_data.getD_FILE_SIZE())); //文件大小
		
		String ret_state = "";
		String business_state = "";
		
		if(db_type.equalsIgnoreCase("fidb"))
		{
			if(grib_struc_data.getinsert_ret()==1)
			{
				ret_state = "1";
				business_state = "1";
			}
			else
			{
				ret_state = "0";
				business_state = "0";
			}
		}
		else if(db_type.equalsIgnoreCase("radb"))
		{
			if(grib_struc_data.getinsert_radb_ret()==1)
			{
				ret_state = "1";
				business_state = "1";
			}
			else
			{
				ret_state = "0";
				business_state = "0";
			}
		}
		
		
		filed_info.setPROCESS_STATE(ret_state);  //系统处理状态: DPC和SOD：1-正常，0-错误
		filed_info.setBUSINESS_STATE(business_state); //业务状态: 1-正常，0-错误
		filed_info.setRECORD_TIME(getSysTime()); //DI记录时间
		
		filed_info.setELE_NAME(grib_struc_data.getElement()+"_"+grib_struc_data.getLevelType()); //要素名称：带层次类型，如：WIU_100
		filed_info.setELE_LEVEL(grib_struc_data.getlevel1_orig()+""); //层次
		
		Rest_info.setFields(filed_info);
		
		//logger.info("send restinfo di data begin...");		
		RestfulSendData restful_send_data = new RestfulSendData();
		restful_send_data.SendData_single(Rest_info);
		//logger.info("send restinfo di data end...");
		logger.info("send restinfo di complete.");
	}
	
	/**
	   * 获得系统时间
	   * 
	   * @param
	*/
	public static String getSysTime()
	{
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = new Date();
	    dateFormat.setTimeZone(TimeZone.getTimeZone("GMT")); // 设置时区为国际时GMT
	    String currentSysTime = dateFormat.format(date);
	    return currentSysTime;
	}
}