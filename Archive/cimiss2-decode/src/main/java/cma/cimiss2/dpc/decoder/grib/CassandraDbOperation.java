package cma.cimiss2.dpc.decoder.grib;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/*
import org.cimiss.core.ATS;
import org.cimiss.core.ATSClient;
import org.cimiss.core.bean.GridData;
import org.cimiss.core.model.ATSBatchResult;
*/

import org.cmadaas.ats.core.ATS;
import org.cmadaas.ats.core.ATSClient;
import org.cmadaas.ats.core.bean.GridData;
import org.cmadaas.ats.core.bean.micaps.MicapsData;
import org.cmadaas.ats.core.model.ATSBatchResult;

//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.grib.di_ei.SendEiProcess;

public class CassandraDbOperation
{
	//private static final Logger logger = LoggerFactory.getLogger(CassandraDbOperation.class);
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	private ATS ats = null;	
	
	public CassandraDbOperation()
	{
		ats = new ATSClient("config/");
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
	//返回：入库是否全部数据都成功
	//1：数据全部入库成功，0：不是全部都成功
	public int into_cassandra_map(Map<String,List<Grib_Struct_Data>> map_grib_decode_result)
	{
		int sucess = 1; //入库是否全部数据都成功
		for (String area_fieldType_genProcessType : map_grib_decode_result.keySet()) 
		{  			  
		    //logger.info("area_fieldType_genProcessType = " + area_fieldType_genProcessType);
		    List<Grib_Struct_Data> grib_record_list = map_grib_decode_result.get(area_fieldType_genProcessType);
		  
		    ATSBatchResult atsBatchResult = into_cassandra_list_batch(area_fieldType_genProcessType,grib_record_list); //list批量入库
		    
		    //如果有记录入库失败，返回：0	    
		    if(atsBatchResult==null||atsBatchResult.getFailedRowCount()>0)
		    {
		    	sucess = 0;
		    }
		    
		    //into_cassandra_list_single(grib_record_list); //list单条入库
		}  
		
		return sucess;
		
	}
	
	//将list入库，批量入库
	private ATSBatchResult into_cassandra_list_batch(String area_fieldType_genProcessType,List<Grib_Struct_Data> grib_record_list)
	{
		List<GridData>  datas = new ArrayList<GridData>();
		datas.clear();	
		
		//add,2020.6.8：裁剪区域后的数据
		List<GridData>  datas_split = new ArrayList<GridData>();
		datas_split.clear();
		
		ATSBatchResult atsBatchResult = null;
		for(int i=0;i<grib_record_list.size();i++)
		{
			//System.out.println("batch data " + i+":");
			//logger.info("batch data " + i+":");
			
			GridData gridData = new GridData();
			gridData.setProdCode(grib_record_list.get(i).getElement()+"_"+grib_record_list.get(i).getLevelType()); //要素加层次类型，如：TEM_100
			rebuildEnsembleNafpProdCode(gridData,grib_record_list.get(i));					
			//System.out.println("ProdCode:" + grib_record_list.get(i).getElement());
			
			//对index_type=1(解析文件名+入Cassandra)和3(只入Cassandra)的情况，入Cassandra的要素不带层次类型
			String data_id = grib_record_list.get(i).getdata_id();	//四级编码
			DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(data_id);
			int index_type = data_attributes.getindex_type();
			if(index_type==1||index_type==3) //index_type=1或3，只带要素名
			{
				gridData.setProdCode(grib_record_list.get(i).getElement());
			}		
			logger.info("gridData.getProdCode:" + gridData.getProdCode());	
			
			gridData.setProductDescription(grib_record_list.get(i).getProductDescription());
			//System.out.println("ProductDescription:" + grib_record_list.get(i).getProductDescription());
			gridData.setLevel(grib_record_list.get(i).getlevel1_orig());
			logger.info("gridData.getLevel:" + gridData.getLevel());
			//System.out.println("Level:" + grib_record_list.get(i).getLevel1());
			//System.out.println("time:" + grib_record_list.get(i).getTime());
			Date date = null;
			//grib_record_list.get(i).setTime("20180714000000");
			try 
			{
				date = new SimpleDateFormat("yyyyMMddHHmmss").parse(grib_record_list.get(i).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String log_data = "error parse DateTime in into_cassandra_list_batch," + grib_record_list.get(i).getV_FILE_NAME_SOURCE() + ",第" + i + "个," + getSysTime() + ",Exception:" + e.getMessage();
				logger_e.error(log_data);
				String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
				GribConfig.write_log_to_file(error_log_file, log_data);
			}
			gridData.setTime(date);
			//logger.info("date:" + gridData.getTime().getTime() + "," + grib_record_list.get(i).getTime());
			//System.out.println("date:" + gridData.getTime().getTime() + "," + grib_record_list.get(i).getTime());
			gridData.setValidtime(grib_record_list.get(i).getV04320());
			//logger.info("Validtime:" + gridData.getValidtime());
			//System.out.println("Validtime:" + gridData.getValidtime());
			
			gridData.setStartX(grib_record_list.get(i).getStartX());
			logger.info("StartX:" + gridData.getStartX());
			gridData.setStartY(grib_record_list.get(i).getStartY());
			logger.info("StartY:" + gridData.getStartY());
			gridData.setEndX(grib_record_list.get(i).getEndX());
			logger.info("EndX:" + gridData.getEndX());
			gridData.setEndY(grib_record_list.get(i).getEndY());
			logger.info("EndY:" + gridData.getEndY());
			gridData.setXStep(grib_record_list.get(i).getXStep());
			logger.info("XStep:" + gridData.getXStep());
			gridData.setYStep(grib_record_list.get(i).getYStep());
			logger.info("YStep:" + gridData.getYStep());
			gridData.setXCount(grib_record_list.get(i).getXCount());
			logger.info("XCount:" + gridData.getXCount());
			gridData.setYCount(grib_record_list.get(i).getYCount());
			logger.info("YCount:" + gridData.getYCount());
			gridData.setValueByteNum(grib_record_list.get(i).getValueByteNum());
			//System.out.println("ValueByteNum:" + grib_record_list.get(i).getValueByteNum());
			gridData.setValuePrecision(grib_record_list.get(i).getValuePrecision());
			System.out.println("ValuePrecision:" + grib_record_list.get(i).getValuePrecision());			
			gridData.setGridUnits(grib_record_list.get(i).getGridUnits());
			//System.out.println("GridUnits:" + grib_record_list.get(i).getGridUnits());
			gridData.setGridProject(grib_record_list.get(i).getGridProject());
			//System.out.println("GridProject:" + grib_record_list.get(i).getGridProject());
			gridData.setDataId(grib_record_list.get(i).getdata_id().replace(".R", ".S"));
			//System.out.println("data_id:" + grib_record_list.get(i).getdata_id());
			gridData.setHeightCount(grib_record_list.get(i).getHeightCount());
			//System.out.println("HeightCount:" + grib_record_list.get(i).getHeightCount());
			gridData.setHeights(grib_record_list.get(i).getHeights());
			gridData.setHeightType(grib_record_list.get(i).getLevelType());
			//System.out.println("LevelType:" + grib_record_list.get(i).getLevelType());
			//gridData.setEventTime(new Date());
			//gridData.setEventTime(grib_record_list.get(i).getevent_time());
			
			gridData.setProductCenter(grib_record_list.get(i).getProductCenter());
			//System.out.println("ProductCenter:" + grib_record_list.get(i).getProductCenter());
			gridData.setProductMethod(grib_record_list.get(i).getProductMethod());
			//System.out.println("ProductMethod:" + grib_record_list.get(i).getProductMethod());
			gridData.setIYMDHM(getSysTime2());
			
			gridData.setValue(grib_record_list.get(i).getdata());
			
			int len = grib_record_list.get(i).getdata().length;
			//System.out.println("data.length:" + grib_record_list.get(i).getdata().length);		
			logger.info("data.length:" + grib_record_list.get(i).getdata().length);
			//System.out.println("data:" + grib_record_list.get(i).getdata()[0] + "," + grib_record_list.get(i).getdata()[len-1]);
			logger.info("data:" + grib_record_list.get(i).getdata()[0] + "," + grib_record_list.get(i).getdata()[len-1]);
			
			datas.add(gridData);
			
			GridData gridData_split = new GridData();
			try {
				BeanUtils.copyProperties(gridData_split, gridData); //拷贝对象
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String log_data = "copy griddata error," + e.getMessage() + "," + getSysTime();
				logger_e.error(log_data);
				String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
				GribConfig.write_log_to_file(error_log_file, log_data);	
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String log_data = "copy griddata error," + e.getMessage() + "," + getSysTime();
				logger_e.error(log_data);
				String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
				GribConfig.write_log_to_file(error_log_file, log_data);	
			}
			
			
			//add,2020.6.8：模式区域裁剪数据
			DataAttr data_attributes_area_split = (DataAttr)GribConfigAreaMapping.get_map_area_split_description().get(data_id);
			if(data_attributes_area_split!=null)
			{
				String area_src = data_attributes_area_split.getarea_src(); //原始区域
				String area_des = data_attributes_area_split.getarea_des(); //裁剪后的区域
				String Lat_lon = data_attributes_area_split.getLat_Lon(); //裁剪出的经纬度范围，格式：起始纬度，终止纬度，起始经度，终止经度，如：60,-10,60,150
				
				String[] Lat_lon_array = Lat_lon.split(",");
				float start_lat = Float.parseFloat(Lat_lon_array[0]); //起始纬度
				float end_lat = Float.parseFloat(Lat_lon_array[1]); //终止纬度
				float start_lon = Float.parseFloat(Lat_lon_array[2]); //起始经度
				float end_lon = Float.parseFloat(Lat_lon_array[3]); //终止经度
				
				logger.info("start_lat:" + start_lat + "," +"end_lat:" + end_lat + "," +"start_lon:" + start_lon + "," +"end_lon:" + end_lon);
				
				boolean first_lat = true; //找第一个纬度
				boolean first_lon = true; //找第一个经度
				
				float start_lat_split = 0; //起始纬度（裁剪后）
				float end_lat_split = 0; //终止纬度（裁剪后）
				float start_lon_split = 0; //起始经度（裁剪后）
				float end_lon_split = 0; //终止经度（裁剪后）
				
				List<Float>  data_split_list = new ArrayList<Float>(); //保存裁剪后的数据
				data_split_list.clear();
				
				//int src_index = 0; //原始数据的索引
				
				if(grib_record_list.get(i).getV_AREACODE().equalsIgnoreCase(area_src)) //如果是该资料的需要裁剪的区域，才进行裁剪
				{
					//先纬度循环
					//logger.info("grib_record_list.get(" + i + ").getStartY()："+grib_record_list.get(i).getStartY());
					//logger.info("grib_record_list.get(" + i + ").getYStep()："+grib_record_list.get(i).getYStep());
					int ycount = grib_record_list.get(i).getYCount(); 
					float y=grib_record_list.get(i).getStartY();
					for(int t=0;t<ycount;t++)
					{
						y = grib_record_list.get(i).getStartY() + t*grib_record_list.get(i).getYStep();
						if((y<start_lat&&y<end_lat)||(y>start_lat&&y>end_lat)) //如果纬度不在范围，则排除
						{							
							continue;
						}
						
						if(first_lat) //找第一个纬度
						{
							start_lat_split = y;
							first_lat = false;
						}
						
						end_lat_split = y; //最后一个纬度
						
						//再经度循环
						//logger.info("grib_record_list.get(" + i + ").getStartX()："+grib_record_list.get(i).getStartX());
						//logger.info("grib_record_list.get(" + i + ").getXStep()："+grib_record_list.get(i).getXStep());
						int xcount = grib_record_list.get(i).getXCount();
						float x=grib_record_list.get(i).getStartX();
						for(int q=0;q<xcount;q++)
						{
							x = grib_record_list.get(i).getStartX() + q*grib_record_list.get(i).getXStep();
							if((x<start_lon&&x<end_lon)||(x>start_lon&&x>end_lon)) //如果经度不在范围，排除
							{
								continue;
							}
							
							if(first_lon) //找第一个经度
							{
								start_lon_split = x;
								first_lon = false;
							}
							
							end_lon_split = x; //最后一个经度
							
							data_split_list.add(grib_record_list.get(i).getdata()[t*grib_record_list.get(i).getXCount()+q]); //纬度：第t个，经度：第q个，的索引
						}
						
						
					}
					
					//生成float数组
					float[] data_array = new float[data_split_list.size()]; 
					for(int t=0;t<data_split_list.size();t++)
					{
						data_array[t] = data_split_list.get(t);
					}
					
					gridData_split.setStartX(start_lon_split);
					logger.info("split StartX:" + gridData_split.getStartX());
					gridData_split.setStartY(start_lat_split);
					logger.info("split StartY:" + gridData_split.getStartY());
					gridData_split.setEndX(end_lon_split);
					logger.info("split EndX:" + gridData_split.getEndX());
					gridData_split.setEndY(end_lat_split);
					logger.info("split EndY:" + gridData_split.getEndY());
					//gridData_split.setXStep(grib_record_list.get(i).getXStep());
					logger.info("split XStep:" + gridData_split.getXStep());
					//gridData_split.setYStep(grib_record_list.get(i).getYStep());
					logger.info("split YStep:" + gridData_split.getYStep());
					int xcount_split = (int) ((end_lon_split-start_lon_split)/grib_record_list.get(i).getXStep()) + 1;
					gridData_split.setXCount(xcount_split);
					logger.info("split XCount:" + gridData_split.getXCount());
					int ycount_split = (int) ((end_lat_split-start_lat_split)/grib_record_list.get(i).getYStep()) + 1;
					gridData_split.setYCount(ycount_split);
					logger.info("split XCount:" + gridData_split.getYCount());
					
					gridData_split.setValue(data_array);
					
					datas_split.add(gridData_split);
				}
			}
			
		}
		String d_data_id = grib_record_list.get(0).getdata_id();
		
		//入cassandra库：原始数据
		atsBatchResult = write_cassandra(d_data_id,grib_record_list,area_fieldType_genProcessType,datas);
		datas.clear();
		
		//入cassandra库：裁剪数据
		DataAttr data_attributes_area_split = (DataAttr)GribConfigAreaMapping.get_map_area_split_description().get(d_data_id);
		if(data_attributes_area_split!=null&&datas_split.size()>0) //如果需要裁剪区域，且datas_split不为空
		{
			String area_src = data_attributes_area_split.getarea_src(); //原始区域
			String area_des = data_attributes_area_split.getarea_des(); //裁剪后的区域
			String area_fieldType_genProcessType_split = area_fieldType_genProcessType.replace(area_src, area_des);
			
			ATSBatchResult atsBatchResult_split = write_cassandra(d_data_id,grib_record_list,area_fieldType_genProcessType_split,datas_split);
			datas_split.clear();
		}		 
		
		return atsBatchResult;
	}
	
	private ATSBatchResult write_cassandra(String d_data_id,List<Grib_Struct_Data> grib_record_list,String area_fieldType_genProcessType,List<GridData>  datas)
	{
		logger.info("StartX:" + datas.get(0).getStartX());		
		logger.info("StartY:" + datas.get(0).getStartY());		
		logger.info("EndX:" + datas.get(0).getEndX());		
		logger.info("EndY:" + datas.get(0).getEndY());
		
		ATSBatchResult atsBatchResult = null;
		String table_name = "";
		try 
		{			
			DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
			//"NAFP_FOR_FTM_RJTD_LOW_GLB";
			table_name = "NAFP_FOR_FTM_" + data_attributes.getdescription() + "_" 
					+ data_attributes.getprecision() + "_" +area_fieldType_genProcessType;
			
			//对于实况降水（CMPA）：A.0042.0003,A.0042.0004,A.0042.0005,A.0042.0006,A.0042.0007,表名做特殊处理
			if(grib_record_list.get(0).getdata_id().startsWith("A.0042.0003")||grib_record_list.get(0).getdata_id().startsWith("A.0042.0004")||
					grib_record_list.get(0).getdata_id().startsWith("A.0042.0005")||grib_record_list.get(0).getdata_id().startsWith("A.0042.0006")||
					grib_record_list.get(0).getdata_id().startsWith("A.0042.0007"))
			{
				String sour_filename_path = grib_record_list.get(0).getV_FILE_NAME_SOURCE();
				String pure_name = sour_filename_path.substring(sour_filename_path.lastIndexOf("/")+1); //获取纯文件名				
				
				//Z_SURF_C_BABJ_20191114081154_P_CMPA_FAST_CHN_0P05_HOR-PRE-2019111408.GRB2
				//Z_SURF_C_BABJ_20191114081154_P_CMPA_FAST_CHN_0P05_3HOR-PRE-2019111408.GRB2
				//Z_SURF_C_BABJ_20191114081154_P_CMPA_FRT_CHN_0P05_HOR-PRE-2019111408.GRB2
				//Z_SURF_C_BABJ_20191114081154_P_CMPA_FRT_CHN_0P05_3HOR-PRE-2019111408.GRB2
				pure_name = pure_name.replace("-", "_");
				String[] pure_name_array = pure_name.split("_");
				if(pure_name_array.length>=13)
				{	//NAFP_FOR_FTM_CMPA_FAST-3HOR_CHN_0_0
					table_name = "NAFP_FOR_FTM_" + data_attributes.getdescription() + "_" 
							+ data_attributes.getprecision() + "_" + pure_name_array[10] + "_" +area_fieldType_genProcessType;
				}
			}
			
			System.out.println("table_name:"+table_name);
			logger.info("table_name:"+table_name);			
			
			atsBatchResult = ats.batchWriteGridDataForAll(table_name, datas);//与池大小无关
			
			if(atsBatchResult==null) //如果入库全失败
			{
				String log_data = "error into_cassandra_list_batch全部失败,atsBatchResult=null,size=" + grib_record_list.size() + "," + 
						          "filename=" + grib_record_list.get(0).getV_FILE_NAME_SOURCE() + ",area_fieldType_genProcessType=" + area_fieldType_genProcessType + ",table_name=" + table_name + "," + getSysTime();
				logger_e.error(log_data);
				String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
				GribConfig.write_log_to_file(error_log_file, log_data);	
			}
			else				
			{
				System.out.println("成功: [" + atsBatchResult.getSuccessRowCount()+"],失败: [" + atsBatchResult.getFailedRowCount()
			       		+ "]， 耗时："+atsBatchResult.getConsumedTime()+"毫秒");
				logger.info("成功: [" + atsBatchResult.getSuccessRowCount()+"],失败: [" + atsBatchResult.getFailedRowCount()
			   			+ "]， 耗时："+atsBatchResult.getConsumedTime()+"毫秒");	
				
				if(atsBatchResult.getFailedRowCount()>0) //如果有部分失败
				{
					String log_data = "error into_cassandra_list_batch," + "成功: [" + atsBatchResult.getSuccessRowCount()+"],失败: [" + atsBatchResult.getFailedRowCount() + "]," + 
							"filename=" + grib_record_list.get(0).getV_FILE_NAME_SOURCE() + ",area_fieldType_genProcessType=" + area_fieldType_genProcessType + ",table_name=" + table_name + 
							"," + atsBatchResult.getMessage() + "," + getSysTime();
					logger_e.error(log_data);
					
					//使用单独的线程池来入Rest EI
					if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
					{
						SendEiProcess sendEiProcess = SendEiProcess.getInstance();
						String KEvent = "入Cassandra表" + table_name  + "失败, 失败记录数=" +  atsBatchResult.getFailedRowCount() + "条";
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
			String log_data = "error insert in into_cassandra_list_batch,filename=" + grib_record_list.get(0).getV_FILE_NAME_SOURCE() + 
					",area_fieldType_genProcessType=" + area_fieldType_genProcessType + ",table_name=" + table_name + "," + getSysTime() + ",Exception:" + e.getMessage();
			logger_e.error(log_data);
			
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KEvent = "入Cassandra表异常：" + table_name  + "," + e.getMessage();
				sendEiProcess.process_ei("OP_DPC_A_2_33_05", KEvent, "", grib_record_list.get(0).getdata_id());
			}
			
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);			
		}		
		finally
		{
			datas.clear();			
		}
		
		return atsBatchResult;
	}
	
	//将list入库，单条入库
	//返回：入库成功的条数
	private int into_cassandra_list_single(List<Grib_Struct_Data> grib_record_list)
	{		
		int sucess_num = 0; //入成功的条数
		for(int i=0;i<grib_record_list.size();i++)
		{
			System.out.println("single data " + i+":");
			logger.info("single data " + i+":");
			
			GridData gridData = new GridData();
			gridData.setProdCode(grib_record_list.get(i).getElement()+"_"+grib_record_list.get(i).getLevelType()); //要素加层次类型，如：TEM_100
			rebuildEnsembleNafpProdCode(gridData,grib_record_list.get(i));
			//System.out.println("Element:" +grib_record_list.get(i).getElement());
			gridData.setProductDescription(grib_record_list.get(i).getProductDescription());
			//System.out.println("Description:" +grib_record_list.get(i).getProductDescription());
			gridData.setLevel(grib_record_list.get(i).getlevel1_orig());
			//System.out.println("Level1:"+grib_record_list.get(i).getLevel1());
			Date date = null;
			try 
			{
				date = new SimpleDateFormat("yyyyMMddHHmmss").parse(grib_record_list.get(i).getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String log_data = "error parse DateTime in into_cassandra_list_single," + grib_record_list.get(i).getV_FILE_NAME_SOURCE() + ",第" + i + "个," + getSysTime() + ",Exception:" + e.getMessage();
				logger_e.error(log_data);
				String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
				GribConfig.write_log_to_file(error_log_file, log_data);
			}
			gridData.setTime(date);
			//System.out.println("Time:" +date.getTime());
			gridData.setValidtime(grib_record_list.get(i).getV04320());
			//System.out.println("Validtime:" + grib_record_list.get(i).getV04320());
			gridData.setValue(grib_record_list.get(i).getdata());
			int len = grib_record_list.get(i).getdata().length;
			//System.out.println("data.length:" + grib_record_list.get(i).getdata().length);			
			//System.out.println("data:" + grib_record_list.get(i).getdata()[0] + "," + grib_record_list.get(i).getdata()[len-1]);
			gridData.setStartX(grib_record_list.get(i).getStartX());
			//System.out.println("StartX:" + grib_record_list.get(i).getStartX());
			gridData.setStartY(grib_record_list.get(i).getStartY());
			//System.out.println("StartY:" + grib_record_list.get(i).getStartY());
			gridData.setEndX(grib_record_list.get(i).getEndX());
			//System.out.println("EndX:" + grib_record_list.get(i).getEndX());
			gridData.setEndY(grib_record_list.get(i).getEndY());
			//System.out.println("EndY:" + grib_record_list.get(i).getEndY());
			gridData.setXStep(grib_record_list.get(i).getXStep());
			//System.out.println("XStep:" + grib_record_list.get(i).getXStep());
			gridData.setYStep(grib_record_list.get(i).getYStep());
			//System.out.println("YStep:" + grib_record_list.get(i).getYStep());
			gridData.setXCount(grib_record_list.get(i).getXCount());
			//System.out.println("XCount" + grib_record_list.get(i).getXCount());
			gridData.setYCount(grib_record_list.get(i).getYCount());
			//System.out.println("YCount:" + grib_record_list.get(i).getYCount());
			gridData.setValueByteNum(grib_record_list.get(i).getValueByteNum());
			//System.out.println("ValueByteNum:" + grib_record_list.get(i).getValueByteNum());
			gridData.setValuePrecision(grib_record_list.get(i).getValuePrecision());
			//System.out.println("ValuePrecision:" + grib_record_list.get(i).getValuePrecision());
			gridData.setGridUnits(grib_record_list.get(i).getGridUnits());
			//System.out.println("GridUnits:" + grib_record_list.get(i).getGridUnits());
			gridData.setGridProject(grib_record_list.get(i).getGridProject());
			//System.out.println("GridProject:" + grib_record_list.get(i).getGridProject());
			gridData.setDataId(grib_record_list.get(i).getdata_id());
			//System.out.println("data_id:" + grib_record_list.get(i).getdata_id());
			gridData.setHeightCount(grib_record_list.get(i).getHeightCount());
			//System.out.println("HeightCount:" + grib_record_list.get(i).getHeightCount());
			gridData.setHeights(grib_record_list.get(i).getHeights());
			//System.out.println("Heights:" + grib_record_list.get(i).getHeights());
			gridData.setHeightType(grib_record_list.get(i).getLevelType());
			//System.out.println("LevelType:" + grib_record_list.get(i).getLevelType());
			//gridData.setEventTime(new Date());
			//gridData.setEventTime(grib_record_list.get(i).getevent_time());
			
			gridData.setProductCenter(grib_record_list.get(i).getProductCenter());
			//System.out.println("ProductCenter:" + grib_record_list.get(i).getProductCenter());
			gridData.setProductMethod(grib_record_list.get(i).getProductMethod());
			//System.out.println("ProductMethod:" + grib_record_list.get(i).getProductMethod());
			gridData.setIYMDHM(getSysTime2());
			
			String d_data_id = grib_record_list.get(i).getdata_id();
			String area_fieldType_genProcessType = grib_record_list.get(i).getarea_fieldType_genProcessType();
			try 
			{				
				DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
				//"NAFP_FOR_FTM_RJTD_LOW_GLB";
				String table_name = "NAFP_FOR_FTM_" + data_attributes.getdescription() + "_" 
						+ data_attributes.getprecision() + "_" +area_fieldType_genProcessType;
				System.out.println("table_name:"+table_name);
				logger.info("table_name:"+table_name);
				int result = ats.writeGridData(table_name, gridData);
				sucess_num = sucess_num + result;
				System.out.println("result: [" + result + "]");
				logger.info("result: [" + result + "]");
				
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();				
				String log_data = "error insert in into_cassandra_list_single," + getSysTime() + ",Exception:" + e.getMessage();
				logger_e.error(log_data);
				String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
				GribConfig.write_log_to_file(error_log_file, log_data);
				logger.info(e.getMessage());
			}
			finally
			{				
			}
			
		}
		
		return sucess_num;
		
	}
	
	/**
	 * 	对于集合预报数据，则判断member要素是否为null，否则将成员变量添加到要素名称之后。例如：tmp_00,tmp_01......
	 * @param gridData GridData数据对象
	 * @param grib_struct_data
	 */
	private void rebuildEnsembleNafpProdCode(GridData gridData,Grib_Struct_Data grib_struct_data) {
		String member = grib_struct_data.getmember();
		if(!org.apache.commons.lang3.StringUtils.isBlank(member)) {
			if(member.length()<2) {
				//member = "0" + member;
			}
			gridData.setProdCode(gridData.getProdCode()+"_"+member);
		}
	}
	
	//关闭ats
	public void close()
	{
		if(ats!=null)
		{
			ats.close();
		}
		ats = null;		
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
	
	/**
	 * 获得系统时间
	 * 
	 * @param
	 */
	private String getSysTime2()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String currentSysTime = dateFormat.format(date);
		return currentSysTime;
	}
	
	/**
	 * 获得系统日期
	 * 
	 * @param
	 */
	private String getSysDate()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String currentSysTime = dateFormat.format(date);
		return currentSysTime;
	}
}
