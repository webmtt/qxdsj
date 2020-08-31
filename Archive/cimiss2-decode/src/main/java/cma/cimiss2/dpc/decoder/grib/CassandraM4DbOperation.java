package cma.cimiss2.dpc.decoder.grib;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.cmadaas.ats.core.ATS;
import org.cmadaas.ats.core.ATSClient;
import org.cmadaas.ats.core.bean.GridData;
import org.cmadaas.ats.core.bean.micaps.MicapsData;
import org.cmadaas.ats.core.exceptions.ATSException;
import org.cmadaas.ats.core.model.ATSBatchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.grib.di_ei.SendEiProcess;

public class CassandraM4DbOperation 
{
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	private ATS ats = null;	
	private final String m4TableSpace = "micapsdataserver"; //M4的空间名，固定
	//private final String m4TableSpace = "usr_sod"; //M4的空间名，固定
	
	public CassandraM4DbOperation()
	{
		//ats = new ATSClient("config/");
		//ats = new ATSClient("D:/config/");
		try
		{
			ats = new ATSClient("/space/cmadaas/dpc/CMADAAS_DPC_GRIB/bin/config/");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 将解码结果map入库
	 * 
	 * @param map_grib_decode_result
	 *            解码结果map 
	 * @return 入库是否全部数据都成功
	 *         1：数据全部入库成功，0：不是全部都成功
	 */
	//
	public int into_m4cassandra_map(Map<String,List<Grib_Struct_Data>> map_grib_decode_result)
	{
		int sucess = 1; //入库是否全部数据都成功
		for (String area_fieldType_genProcessType : map_grib_decode_result.keySet()) 
		{  			  
		    logger.info("area_fieldType_genProcessType = " + area_fieldType_genProcessType);
		    List<Grib_Struct_Data> grib_record_list = map_grib_decode_result.get(area_fieldType_genProcessType);
		  
		    ATSBatchResult atsBatchResult = into_m4cassandra_list_batch(area_fieldType_genProcessType,grib_record_list); //list批量入库
		    
		    //如果有记录入库失败，返回：0	    
		    if(atsBatchResult==null||atsBatchResult.getFailedRowCount()>0)
		    {
		    	sucess = 0;
		    }
		    
		    //into_m4cassandra_list_single(grib_record_list); //list单条入库
		}  
		
		return sucess;
		
	}
	
	//将list入库，批量入库
	private ATSBatchResult into_m4cassandra_list_batch(String area_fieldType_genProcessType,List<Grib_Struct_Data> grib_record_list)
	{
		List<MicapsData>  micapsDataList = new ArrayList<MicapsData>();
		micapsDataList.clear();		
		ATSBatchResult atsBatchResult = null;
		for(int i=0;i<grib_record_list.size();i++)
		{
			//将云平台解码要素与M4存储要素做映射
			setM4Element(grib_record_list.get(i));	
			logger.info("M4Element:" + grib_record_list.get(i).getM4Element());
			
			//对区域进行过滤，比如：只需要东北半球(NEHE)区域
			//判断该模式场的区域，是否在配置文件中
			String data_id = grib_record_list.get(i).getdata_id();
			boolean area_valid = isAreaValid(grib_record_list.get(i).getV_AREACODE(),data_id);
			if(!area_valid)
			{
				continue;
			}			
			
			MicapsData micapsData = new MicapsData();
			//MicapsData dataPath为主键之一，如UGRD/0
			//MicapsData dataName为主键之一，如19040414.024
			micapsData.setDataPath(grib_record_list.get(i).getM4Element()+"/"+grib_record_list.get(i).getLevel1()); //dataPath：要素/高度。如：TEM/850
			logger.info("datapath:"+micapsData.getDataPath());
			micapsData.setDataName(grib_record_list.get(i).getTime().substring(2,10)+"."+grib_record_list.get(i).getV04320()); //dataName：时次.时效。如：16083120.000
			logger.info("dataname:"+micapsData.getDataName());
			micapsData.setElement(grib_record_list.get(i).getM4Element()); //M4的element要素
						
			micapsData.setDescription(grib_record_list.get(i).getProductDescription()); //description
			
			micapsData.setLevel(grib_record_list.get(i).getLevel1()); //level
			logger.info("M4Level:" + micapsData.getLevel());
						
			micapsData.setYear(grib_record_list.get(i).getV04001()); //year
			micapsData.setMonth(grib_record_list.get(i).getV04002()); //Month
			micapsData.setDay(grib_record_list.get(i).getV04003()); //Day
			micapsData.setHour(grib_record_list.get(i).getV04004()); //Hour
			micapsData.setMinute((short) grib_record_list.get(i).getV04005()); //Minute
			micapsData.setSecond((short) grib_record_list.get(i).getV04006()); //Second
			
			micapsData.setPeriod(micapsData.getHour()); //时次
			
			micapsData.setStartLongitude(grib_record_list.get(i).getStartX()); //开始经度
			micapsData.setEndLongitude(grib_record_list.get(i).getEndX()); //结束经度
			micapsData.setStartLatitude(grib_record_list.get(i).getStartY()); //开始纬度
			micapsData.setEndLatitude(grib_record_list.get(i).getEndY()); //结束纬度
			
			micapsData.setLongitudeGridSpace(grib_record_list.get(i).getXStep()); //经度网格间距
			micapsData.setLatitudeGridSpace(grib_record_list.get(i).getYStep()); //纬度网格间距
			
			micapsData.setLongitudeGridNumber(grib_record_list.get(i).getXCount()); //经向格点数
			micapsData.setLatitudeGridNumber(grib_record_list.get(i).getYCount()); //纬向格点数
			
			//获取该要素、高度的等值线值
			String isoline_str = get_isoline_value(micapsData.getElement(),(int)micapsData.getLevel()+"");
			try
			{
				String[] isoline_str_array = isoline_str.split("_");
				grib_record_list.get(i).setIsolineStartValue(Float.parseFloat(isoline_str_array[0]));
				grib_record_list.get(i).setIsolineEndValue(Float.parseFloat(isoline_str_array[1]));
				grib_record_list.get(i).setIsolineSpace(Float.parseFloat(isoline_str_array[2]));
			}
			catch ( Exception e ) 
			{
				String log_data = "获取等值线值失败，element=" + micapsData.getElement() + ",level=" + (int)micapsData.getLevel()+ ",Exception:" + e.getMessage();
				logger_e.error(log_data);
				String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
				GribConfig.write_log_to_file(error_log_file, log_data);
			}
			
			micapsData.setIsolineStartValue(grib_record_list.get(i).getIsolineStartValue()); //等值线开始值
			micapsData.setIsolineEndValue(grib_record_list.get(i).getIsolineEndValue()); //等值线结束值
			micapsData.setIsolineSpace(grib_record_list.get(i).getIsolineSpace()); //等值线间隔
			
			logger.info("IsolineStartValue:" + micapsData.getIsolineStartValue());
			logger.info("IsolineEndValue:" + micapsData.getIsolineEndValue());
			logger.info("IsolineSpace:" + micapsData.getIsolineSpace());
			
			//风的u、v分量要素，作为11类数据，其它为4类数据
			if(grib_record_list.get(i).getM4Element().equalsIgnoreCase("UGRD")||grib_record_list.get(i).getM4Element().equalsIgnoreCase("VGRD"))
			{
				micapsData.setType((short) 11); //type
				micapsData.setAngleArray(grib_record_list.get(i).getdata()); //经纬场数据
			}
			else
			{
				micapsData.setType((short) 4); //type
				micapsData.setDataArray(grib_record_list.get(i).getdata()); //经纬场数据，4类数据时，只需填这项				
			}
						
			int len = grib_record_list.get(i).getdata().length;
			//System.out.println("data.length:" + grib_record_list.get(i).getdata().length);		
			logger.info("data.length:" + grib_record_list.get(i).getdata().length);
			//System.out.println("data:" + grib_record_list.get(i).getdata()[0] + "," + grib_record_list.get(i).getdata()[len-1]);
			logger.info("data:" + grib_record_list.get(i).getdata()[0] + "," + grib_record_list.get(i).getdata()[len-1]);
			
			micapsDataList.add(micapsData);			
				
		}
		String d_data_id = grib_record_list.get(0).getdata_id();
		
		//micapsDataList为空，不需要入m4的Cassnadra，直接返回
		if(micapsDataList.size()==0) 
		{
			return null;
		}
		
		String table_name = "";
		try 
		{			
			DataAttr data_attributes = (DataAttr)GribM4Config.get_map_data_description().get(d_data_id);
			//表名：micapsdataserver."JAPAN_HR";
			table_name = m4TableSpace + ".\"" + data_attributes.getm4TableName() + "\"";
					
			//System.out.println("table_name:"+table_name);
			logger.info("M4_table_name:"+table_name);			
			
			atsBatchResult = ats.batchWriteMicapsData(table_name, micapsDataList, true); //写MicapsData到Cassandra库
			
			if(atsBatchResult==null) //如果入库全失败
			{
				String log_data = "error into_m4cassandra_list_batch全部失败,atsBatchResult=null,size=" + grib_record_list.size() + "," + 
						          "filename=" + grib_record_list.get(0).getV_FILE_NAME_SOURCE() + ",area_fieldType_genProcessType=" + area_fieldType_genProcessType + ",table_name=" + table_name + "," + getSysTime();
				logger_e.error(log_data);
				String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
				GribConfig.write_log_to_file(error_log_file, log_data);	
			}
			else				
			{
				System.out.println("M4成功: [" + atsBatchResult.getSuccessRowCount()+"],失败: [" + atsBatchResult.getFailedRowCount()
			       		+ "]， 耗时："+atsBatchResult.getConsumedTime()+"毫秒");
				logger.info("M4成功: [" + atsBatchResult.getSuccessRowCount()+"],失败: [" + atsBatchResult.getFailedRowCount()
			   			+ "]， 耗时："+atsBatchResult.getConsumedTime()+"毫秒");	
				
				if(atsBatchResult.getFailedRowCount()>0) //如果有部分失败
				{
					String log_data = "error into_m4cassandra_list_batch," + "成功: [" + atsBatchResult.getSuccessRowCount()+"],失败: [" + atsBatchResult.getFailedRowCount() + "]," + 
							"filename=" + grib_record_list.get(0).getV_FILE_NAME_SOURCE() + ",area_fieldType_genProcessType=" + area_fieldType_genProcessType + ",table_name=" + table_name + 
							"," + atsBatchResult.getMessage() + "," + getSysTime();
					logger_e.error(log_data);
					
					//使用单独的线程池来入Rest EI
					if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
					{
						SendEiProcess sendEiProcess = SendEiProcess.getInstance();
						String KEvent = "入m4Cassandra表" + table_name  + "失败, 失败记录数=" +  atsBatchResult.getFailedRowCount() + "条";
						sendEiProcess.process_ei("OP_DPC_A_2_33_05", KEvent, "", d_data_id);
					}
					
					String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
					GribConfig.write_log_to_file(error_log_file, log_data);	
				}
			}
			
			//Thread.sleep(1000);
			
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String log_data = "error insert in into_m4cassandra_list_batch,filename=" + grib_record_list.get(0).getV_FILE_NAME_SOURCE() + 
					",area_fieldType_genProcessType=" + area_fieldType_genProcessType + ",table_name=" + table_name + "," + getSysTime() + ",Exception:" + e.getMessage();
			logger_e.error(log_data);
			
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KEvent = "入m4Cassandra表异常：" + table_name  + "," + e.getMessage();
				sendEiProcess.process_ei("OP_DPC_A_2_33_05", KEvent, "", grib_record_list.get(0).getdata_id());
			}
			
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);			
		}		
		finally
		{
			micapsDataList.clear();			
		}
		
		return atsBatchResult;
	}
	
	/**
	 * 获得系统时间
	 * 
	 * @param
	 */
	private String getSysTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String currentSysTime = dateFormat.format(date);
		return currentSysTime;
	}
	
	//将云平台解码要素与M4存储要素做映射
	//如果存在，就做映射；如果不存在，保留云平台的
	private void setM4Element(Grib_Struct_Data grib_struct_data) 
	{		
		grib_struct_data.setM4Element(grib_struct_data.getElement());
		String element_m4 = GribM4Config.get_map_element_m4().get(grib_struct_data.getElement());
		if(element_m4!=null&&element_m4!="")
		{
			grib_struct_data.setM4Element(element_m4);
		}
	}
	
	//判断该模式场的区域，是否在配置文件中
	private boolean isAreaValid(String area,String data_id)
	{
		DataAttr data_attributes = (DataAttr)GribM4Config.get_map_data_description().get(data_id);		
		
		String area_str = data_attributes.getm4Area(); //配置区域，如：NEHE,NHE，或：ALL
		
		if(area_str.equalsIgnoreCase("ALL")) //ALL表示所有区域都入
		{
			return true;
		}
		
		String area_str_array[] = area_str.split(","); //NEHE,NHE
		for(int i=0;i<area_str_array.length;i++)
		{
			if(area.equalsIgnoreCase(area_str_array[i].trim())) //找到相同区域编码
			{
				return true;
			}
		}
		
		return false;		
	}
	
	/**
	 * 根据要素、高度，读取等值线值
	 * 
	 * @param element
	 *            要素
	 * @param level
	 *            高度
	* @return
	 * 			 isolineStartValue_isolineEndValue_isolineSpace
	 */
	private String get_isoline_value(String element,String level)
	{
		String isolineStartValue = "0";
		String isolineEndValue = "0";
		String isolineSpace = "0";
		
		logger.info("element="+element+",level="+level);
		
		//如果配置了NULL，则该要素不分高度，都是同样的等值线值
		String isoline_str = GribM4Config.get_map_isoline_m4().get(element+"_NULL");
		if(isoline_str!=null&&isoline_str!="")
		{
			return isoline_str;
		}
		
		//找该要素对应高度的等值线值
		isoline_str = GribM4Config.get_map_isoline_m4().get(element+"_"+level);
		if(isoline_str!=null&&isoline_str!="")
		{
			return isoline_str;
		}
		
		return "0_0_0";
	}
}
