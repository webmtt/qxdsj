package cma.cimiss2.dpc.decoder.grib;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//grib解码
import ucar.nc2.grib.grib1.Grib1Gds;
import ucar.nc2.grib.grib1.Grib1Record;
import ucar.nc2.grib.grib1.Grib1RecordScanner;
import ucar.nc2.grib.grib1.Grib1SectionBinaryData;
import ucar.nc2.grib.grib1.Grib1SectionGridDefinition;
import ucar.nc2.grib.grib1.Grib1SectionIndicator;
import ucar.nc2.grib.grib1.Grib1SectionProductDefinition;
import ucar.nc2.grib.grib2.Grib2Gds;
import ucar.nc2.grib.grib2.Grib2Pds;
import ucar.nc2.grib.grib2.Grib2Record;
import ucar.nc2.grib.grib2.Grib2RecordScanner;
import ucar.nc2.grib.grib2.Grib2SectionDataRepresentation;
import ucar.nc2.grib.grib2.Grib2SectionGridDefinition;
import ucar.nc2.grib.grib2.Grib2SectionIdentification;
import ucar.nc2.grib.grib2.Grib2SectionIndicator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cma.cimiss2.dpc.decoder.grib.di_ei.SendDiProcess;
import cma.cimiss2.dpc.decoder.grib.di_ei.SendEiProcess;
import cma.cimiss2.dpc.decoder.grib.DataAttr;
import cma.cimiss2.dpc.decoder.grib.GribConfig;
import cma.cimiss2.dpc.decoder.grib.GribDecoderConfigureHelper;
import cma.cimiss2.dpc.decoder.grib.Grib_Struct_Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;


public class GribDecode 
{
	//private final static Logger logger = LoggerFactory.getLogger(GribDecode.class);
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	//private static String SystemDate = GribConfig.getSysDate();
	private static String LATEST_DIR = "LATEST";
	
	public GribDecode()
	{
	}
	
	public static void main(String[] args)
	{
		//PropertyConfigurator.configure("resources/log4j.properties");

		//System.out.println("args.length = " + args.length);
		logger.info("args.length = " + args.length);

		if(args.length!=2)
		{
			//return;
		}
		//String filename = args[0];
		//String d_data_id = args[1];
		//EC
		//String filename = "测试数据/EC_低分_原始多场/A_HJXN02ECEP190000_C_BABJ_20171119074216_03136.bin";
		//String filename = "测试数据/EC_低分_原始多场/A_HLXE88ECMG190000_C_BABJ_20171119055326_58954.bin";

		//String filename = "测试数据/EC_低分_单场/NAFP_ECMF_0_FTM-98-GLB-MCWS-500X500-101-0-0-000-999998-2017111900-0.GRB";


		//String filename = "测试数据/EC_高分_原始多场/W_NAFP_C_ECMF_20171119061826_P_C1D11190000112218001";

		//String filename = "测试数据/EC_高分_单场/NAFP_ECMF_1_FTM-98-GLB-TCWV-125X125-1-0-999998-999998-999998-2017112000-42.GRB";
		//String filename = "测试数据/EC_高分_单场/NAFP_ECMF_0_FTM-98-ANEA-FCCO-250X250-105-25-0-000-999998-2017110112-0.GRB";
		//String filename = "测试数据/EC_高分_单场/NAFP_ECMF_1_FTM-98-GLB-TTSP-125X125-1-0-999998-999998-999998-2017112000-174.GRB";



		//String filename = "测试数据/日本_高分_原始多场/W_NAFP_C_RJTD_20171119000000_GSM_GPV_RGL_GLL0P5DEG_L-PALL_FD0000_GRIB2.BIN";
		//String filename = "测试数据/日本_高分_原始多场/W_NAFP_C_RJTD_20171119000000_GSM_GPV_RGL_GLL0P5DEG_L-PALL_FD0003_GRIB2.BIN";
		//String filename = "测试数据/日本_高分_原始多场/W_NAFP_C_RJTD_20171206060000_GSM_GPV_RRA2_GLL0P5DEG_LP10_FD0000_GRIB2.BIN";

		//String filename = "测试数据/日本_高分_单场/NAFP_RJTD_1_FTM-34-GLB-GPH-500X500-100-10-0-002-999998-2017111900-6.GRB";
		//String filename = "测试数据/日本_高分_单场/NAFP_RJTD_1_FTM-34-GLB-RHU-500X500-100-100-0-002-999998-2017111900-42.GRB";


		//String filename = "测试数据/日本_低分_单场/NAFP_RJTD_1_FTM-34-NHEL-RHU-1250X999998-1-0-999998-999998-999998-2017112000-48.GRB";
		//String filename = "测试数据/日本_低分_单场/NAFP_RJTD_1_FTM-34-GRNC-WIV-1250X1250-100-1000-999998-999998-999998-2017111906-84.GRB";
		//String filename = "测试数据/日本_低分_单场/NAFP_RJTD_1_FTM-34-GRNC-WIV-1250X1250-100-1000-999998-999998-999998-2017111906-84.GRB";


		//String filename = "测试数据/T639_高分_单场/NAFP_T639_1_FTM-38-NEHE-SME-281X281-1-0-0-002-999998-2017111900-6.GRB";

		//String filename = "测试数据/T639_低分_单场/NAFP_T639_1_FTM-38-NEHE-WIV-1000X1000-105-10-999998-999998-999998-2017111918-57.GRB";

		//String filename = "测试数据/Grapes_低分_单场/NAFP_GRAPES_1_FTM-38-CHN-UPHE-100X100-103-2000-5000-002-999998-2017112000-66.GRB";

		String filename = "测试数据/GRAPES_GFS_单场/Z_NAFP_C_BABJ_20171221120000_P_NWPC-GRAPES-GFS-HNEHE-02100.grib2.531";

		//String filename = "测试数据/美国KWBC_单场/NAFP_KWBC_1_FTM-7-SHEM-RHU-1250X999998-116-7680-999998-999998-999998-2017112000-6.GRB";
		//String filename = "测试数据/美国KWBC_单场/NAFP_KWBC_1_FTM-7-NHEJ-PRS-1250X999998-6-0-999998-999998-999998-2017112000-36.GRB";
		//String filename = "测试数据/美国KWBC_单场/NAFP_KWBC_1_FTM-7-NHEK-PRS-1250X999998-7-0-999998-999998-999998-2017112000-6.GRB";
		//String filename = "测试数据/美国KWBC_单场/NAFP_KWBC_1_FTM-7-NHEK-RHU-1250X999998-116-7680-999998-999998-999998-2017120618-36.GRB";

		//String filename = "测试数据/德国_单场/NAFP_EDZW_1_FTM-78-GLB-SOTM-250X250-106-0-0-002-999998-2017111700-0.GRB";

		//String filename = "A_HHIE15RJTD190000_C_BABJ_20171119034735_06321.bin";
		String d_data_id = "F.0006.0001.R001"; //四级编码

		GribConfig.readConfig();

		//System.out.println("1111111111");
		GribDecode gribdecode = new GribDecode();
		Map<String,List<Grib_Struct_Data>> map_grib_decode_result = new HashMap<String,List<Grib_Struct_Data>>();

		map_grib_decode_result.clear();
		//map_grib_decode_result = read_grib1_data(filename,d_data_id);
		//into_cassandra_map(map_grib_decode_result);

		//System.out.println("22222222");

		map_grib_decode_result.clear();
		map_grib_decode_result = gribdecode.read_grib2_data(filename,d_data_id,new Date());
		//into_cassandra_map(map_grib_decode_result);

		System.out.println("3333333");

		//将原始多场文件拆分成单场文件：GRIB...7777...GRIB...7777
		//split_grib_record(filename);

		//将原始多场文件拆分成单场文件：循环嵌套（如日本高分）
		//split_grib_record_other(filename);

	}






	//读取文件中的grib1场数据
	public Map<String,List<Grib_Struct_Data>> read_grib1_data(String filename,String d_data_id,Date rcv_time)
	{
		//System.out.println("文件名：" + filename +",四级编码："+d_data_id);
		//logger.info("文件名：" + filename +",四级编码："+d_data_id);
		//map:<"GLB",List1>,<"ANEA",List2>... 根据区域分List
		
		
		//如果是集合预报，读取集合预报的成员变量
		/*
		DataAttr data_attributes2 = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
		String ens_member = "";
		if(data_attributes2.getIsEnsemble()) //如果是集合预报
		{
			ens_member = read_ensmember(filename);
			logger.info("ens_member:" + ens_member);			
		}
		*/
		
		Map<String,List<Grib_Struct_Data>> map_grib_decode_result = new HashMap<String,List<Grib_Struct_Data>>(); 		
		map_grib_decode_result.clear();
		ucar.unidata.io.RandomAccessFile randomAccessFile = null;
		try 
		{
			randomAccessFile = new ucar.unidata.io.RandomAccessFile( filename, "r" );
			Grib1RecordScanner grib1RecordScanner = new Grib1RecordScanner( randomAccessFile );
			int grib1_count = 0; //grib1记录数
			while ( grib1RecordScanner.hasNext() )
			{
				//GridData gridData = new GridData();
				Grib_Struct_Data grib_struct_data = new Grib_Struct_Data();	
				grib_struct_data.setgrib_version(1);
				
				try {
					Grib1Record grib1_Record = grib1RecordScanner.next(); //grib1单场数据
					
					grib1_count++; //grib1记录数
					//System.out.println("第" + grib1_count + "条grib1 record:");
					logger.info("第" + grib1_count + "条grib1 record:");
					
					Grib1SectionIndicator grib1SectionIndecator_0 =  grib1_Record.getIs();
					Grib1SectionProductDefinition grib1SectionProductDefinition_1 = grib1_Record.getPDSsection(); //1段：产品定义段
					Grib1SectionGridDefinition grib1SectionGridDefinition_2 = grib1_Record.getGDSsection(); //2段：网格描述段
					Grib1SectionBinaryData grib1SectionBinaryData_4 = grib1_Record.getDataSection();
					
					Long mesageLength = grib1SectionIndecator_0.getMessageLength();
					//logger.info("mesageLength:" + mesageLength);
					
					//四级编码
					grib_struct_data.setdata_id(d_data_id);
					
					//获取Datetime
					String Datetime = grib1SectionProductDefinition_1.getReferenceDate().toString();
					String year = Datetime.substring(0, 4);
					String month = Datetime.substring(5, 7);
					String day = Datetime.substring(8, 10);
					String hour = Datetime.substring(11, 13);
					String minute = Datetime.substring(14, 16);
					String second = Datetime.substring(17, 19);
					String datetime1 = year+month+day+hour+minute+second;
					String datetime2 = Datetime.substring(0, 10) + " " + Datetime.substring(11, 19);
					//System.out.println("Datetime:" + Datetime);
					//System.out.println("datatime1:" + datetime1);
					logger.info("datatime1:" + datetime1);
					//System.out.println("datatime2:" + datetime2);
					grib_struct_data.setTime(datetime1);
					grib_struct_data.setDATETIME(datetime2);	
					
					//获取层次类型
					int level_type = grib1SectionProductDefinition_1.getLevelType();
					//System.out.println("level_type:" + level_type);
					//logger.info("level_type:" + level_type);
					grib_struct_data.setLevelType(level_type);
					
					//获取要素名称：由参数指示符决定
					int ParameterNumber = grib1SectionProductDefinition_1.getParameterNumber(); //参数指示符
					//System.out.println("ParameterNumber:" + ParameterNumber);
					//logger.info("ParameterNumber:" + ParameterNumber);
					
					GribDecoderConfigureHelper decoderConfigureHelper = GribDecoderConfigureHelper.instance();
					//如果是EC高分：F.0010.0002.R001,F.0010.0003.R001,F.0010.0004.R001,用GRIB表格版本号作为入口
					String d_data_table_id = d_data_id; 
					int TableVersion = grib1SectionProductDefinition_1.getTableVersion();
					if(d_data_id.compareToIgnoreCase("F.0010.0002.R001")==0
					        ||d_data_id.compareToIgnoreCase("F.0010.0003.R001")==0
					        ||d_data_id.compareToIgnoreCase("F.0010.0004.R001")==0
					        ||d_data_id.contains("F.0010")) //modify,2019.8.9：所有EC的grib1格式都用表格号来匹配
					{
						d_data_table_id = d_data_id.substring(0, 7) + "T" + TableVersion + ".R001"; //F.0010.T228.R001
					}
					String element = decoderConfigureHelper.getGrib1ElementShortName(d_data_table_id,
					        ParameterNumber, level_type);					
					if(element==null)	
					{
						element = ParameterNumber+"";
						//记录此条为null的element
						String log_data = "grib1:"+d_data_table_id+","+ParameterNumber+","+level_type+","+filename + "," + getSysTime();
						logger_e.error("element not found," + log_data);
						
						//使用单独的线程池来入Rest EI
						if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
						{
							SendEiProcess sendEiProcess = SendEiProcess.getInstance();
							String KEvent = "模式解码要素名不存在：" + "grib1:"+d_data_table_id+","+ParameterNumber+","+level_type+","+filename;
							sendEiProcess.process_ei("OP_DPC_A_1_41_03", KEvent, grib_struct_data.getDATETIME(), grib_struct_data.getdata_id());
						} 
						
						String element_log_file = GribConfig.getLogFilePath()+"element_null_"+GribConfig.getSysDate()+".log";
						GribConfig.write_log_to_file(element_log_file, log_data);
						//System.out.println("element not found," + log_data);
						
						continue;
					}
					grib_struct_data.setElement(element);
					//System.out.println("element:" + element);
					//logger.info("element:" + element);
					String temp_file = GribConfig.getLogFilePath()+"temp_element_" + GribConfig.getSysDate() + ".log";
					GribConfig.write_log_to_file(temp_file, element);
					
					//设置要素服务代码，如：TEM_100
					grib_struct_data.setelement_serv(element+"_"+level_type);
					
					
					//获取层次高度（层次1、层次2）：某些层次类型，要把层次1和层次2合起来表示?
					int level1 = grib1SectionProductDefinition_1.getLevelValue1(); //层次1：只有层次1
					int level2 = grib1SectionProductDefinition_1.getLevelValue2(); //
					//System.out.println("level1:" + level1 + ",level2:" + level2);
					//logger.info("level1:" + level1 + ",level2:" + level2);
					
					if(level_type==101||level_type==104||level_type==106   //层次类型为116时(kwbc资料)：level1=30,level=0
							||level_type==108||level_type==112||level_type==114 //有两层时，level1,level2的取值不变
							||level_type==116||level_type==120||level_type==121
							||level_type==128||level_type==141)   			
					{                   				 									
						level1 = level1;
						level2 = level2;
					}
					else //只有一层时，将level1,level2合起来表示一层
					{
						level1 = level1*256 + level2;
						level2 = 999998;
					}
					grib_struct_data.setLevel1(level1);
					grib_struct_data.setLevel2(level2);
					grib_struct_data.setlevel1_orig(level1);
					grib_struct_data.setlevel2_orig(level2);
					//System.out.println("level1:" + level1 + ",level2:" + level2);
					logger.info("level1:" + level1 + ",level2:" + level2);					
					
					//获取预报时效：有疑问?
					int timeUnit = grib1SectionProductDefinition_1.getTimeUnit(); //时效单位：0:分，1：时，2：日
					int timevalue1 = grib1SectionProductDefinition_1.getTimeValue1(); //时效1
					int timevalue2 = grib1SectionProductDefinition_1.getTimeValue2(); //时效2
					int timeRangeIndicator = grib1SectionProductDefinition_1.getTimeRangeIndicator(); //时效范围指示符
					//要根据时效范围指示符(Psec01（20）)来判断时效的取值：
					//先根据Psec01（20）判断赋值，
					//如果Psec01（20）=0、1，时效=P1；
					//如果Psec01（20）=2，时效=P2；
					//如果Psec01（20）=3、4、5、10，时效=P2。
					int time_value = 0;
					if(timeRangeIndicator==0||timeRangeIndicator==1)
					{
						time_value = timevalue1;
					}
					else if(timeRangeIndicator==2||timeRangeIndicator==3||timeRangeIndicator==4
					        ||timeRangeIndicator==5||timeRangeIndicator==10)
					{
						time_value = timevalue2;
					}	
					//转换时效单位
					if(timeUnit==0)
					{
						time_value = time_value/60;
					}
					else if(timeUnit==2)
					{
						time_value = time_value*24;
					}				

					//System.out.println("timeUnit:" + timeUnit + ",timevalue1:" + timevalue1 + ",timevalue2:" + timevalue2 + ",timeRangeIndicator:" + timeRangeIndicator);
					//System.out.println("time_value:" + time_value);
					logger.info("time_value:" + time_value);
					grib_struct_data.setValidTime(time_value + "");
					
					grib_struct_data.setValueByteNum(4);
					grib_struct_data.setValuePrecision(1); //1表示不变，10表示值乘10
					grib_struct_data.setGridUnits(0);
					
					//格点投影类型：Grib1:第2段的数据表示类型
					int datapresentation = grib1SectionGridDefinition_2.getGridTemplate();
					//System.out.println("datapresentation:" + datapresentation);
					grib_struct_data.setGridProject(datapresentation);
					
					//获取经纬度
					Grib1Gds.LatLon grib1Gds_LatLon = (Grib1Gds.LatLon)grib1SectionGridDefinition_2.getGDS(); //经纬度
					float start_lo = (float)(Math.round(grib1Gds_LatLon.lo1*1000))/1000; //保留小数点后3位
					float end_lo = (float)(Math.round(grib1Gds_LatLon.lo2*1000))/1000;	
					if(end_lo>360)  //为了保证end_lo>start_lo，nc库的读取会将end_lo在原始值上加360
					{
						end_lo = end_lo - 360;
					}
					float lo_space = (float)(Math.round(grib1Gds_LatLon.getDx()*1000))/1000;
					int lo_number = grib1Gds_LatLon.getNx();
					float start_la = (float)(Math.round(grib1Gds_LatLon.la1*1000))/1000;
					float end_la = (float)(Math.round(grib1Gds_LatLon.la2*1000))/1000;
					float la_space = (float)(Math.round(grib1Gds_LatLon.getDy()*1000))/1000;
					int la_number = grib1Gds_LatLon.getNy();	
										
					//modify,2019.5.29：区域转换，针对美国模式区域差错情况
					String area_src = start_la + "_" + end_la + "_"+ start_lo + "_"+ end_lo;
					String area_des = GribConfigAreaMapping.get_map_data_description().get(area_src);
					if(area_des!=null&&area_des!="")
					{
						area_src = area_des;
						
						String[] area_des_array = area_des.split("_");
						start_la =  Float.parseFloat(area_des_array[0]);
						end_la =  Float.parseFloat(area_des_array[1]);
						start_lo =  Float.parseFloat(area_des_array[2]);
						end_lo =  Float.parseFloat(area_des_array[3]);
					}
					
					//System.out.println("start_lo:" + start_lo);
					//System.out.println("end_lo:" + end_lo);
					//System.out.println("lo_space:" + lo_space);
					//System.out.println("la_number:" + la_number);
					//System.out.println("start_la:" + start_la);
					//System.out.println("end_la:" + end_la);
					//System.out.println("la_space:" + la_space);
					//System.out.println("lo_number:" + lo_number);	
					//logger.info("start_lo:" + start_lo);
					//logger.info("end_lo:" + end_lo);
					//logger.info("lo_space:" + lo_space);
					//logger.info("la_number:" + la_number);
					//logger.info("start_la:" + start_la);
					//logger.info("end_la:" + end_la);
					//logger.info("la_space:" + la_space);
					//logger.info("lo_number:" + lo_number);
					
					grib_struct_data.setStartX(start_lo);
					grib_struct_data.setStartY(start_la);
					grib_struct_data.setEndX(end_lo);
					grib_struct_data.setEndY(end_la);
					grib_struct_data.setXStep(lo_space);
					grib_struct_data.setYStep(la_space);
					grib_struct_data.setXCount(lo_number);
					grib_struct_data.setYCount(la_number);
				
					grib_struct_data.setHeightCount(1);
					float[] heights={0};
					grib_struct_data.setHeights(heights);
					
					//加工中心
					int GeneratingCentrer = grib1SectionProductDefinition_1.getCenter();
					//System.out.println("GeneratingCentrer:" + GeneratingCentrer);
					grib_struct_data.setProductCenter(GeneratingCentrer+"");
					
					//制作方法，如无时用字符串0
					grib_struct_data.setProductMethod("0");
					
					//event_time
					grib_struct_data.setevent_time(rcv_time);
					
					//区域
					BigDecimal start_la_1 = new BigDecimal(String.valueOf(start_la));
					//logger.info("start_la_1:" + start_la_1);
					BigDecimal end_la_1 = new BigDecimal(String.valueOf(end_la));
					//logger.info("end_la_1:" + end_la_1);
					BigDecimal start_lo_1 = new BigDecimal(String.valueOf(start_lo));
					//logger.info("start_lo_1:" + start_lo_1);
					BigDecimal end_lo_1 = new BigDecimal(String.valueOf(end_lo));
					//logger.info("end_lo_1:" + end_lo_1);
					String area = decoderConfigureHelper.getGribArea(start_la_1, end_la_1, start_lo_1, end_lo_1);
					if(area==null)
					{
						area = start_la + "_" + end_la + "_"+ start_lo + "_"+ end_lo;
						//记录此条为null的area
						String log_data = "grib1:"+d_data_id+","+start_la+","+end_la+","+start_lo+","+end_lo+","+filename+ "," + getSysTime();
						logger_e.error("area not found," + log_data);
						
						//使用单独的线程池来入Rest EI
						if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
						{
							SendEiProcess sendEiProcess = SendEiProcess.getInstance();
							String KEvent = "模式解码区域名不存在：" + area;
							sendEiProcess.process_ei("OP_DPC_A_1_41_04", KEvent, grib_struct_data.getDATETIME(), grib_struct_data.getdata_id());
						}
						
						String area_log_file = GribConfig.getLogFilePath()+"area_null_"+GribConfig.getSysDate()+".log";
						GribConfig.write_log_to_file(area_log_file, log_data);
						//System.out.println("area not found," + log_data);
						
						continue;
					}
					grib_struct_data.setV_AREACODE(area);
					//logger.info("area:" + area);					
					
					//场文件收到时间
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String D_RYMDHM = dateFormat.format(rcv_time);
					grib_struct_data.setD_RYMDHM(D_RYMDHM);
					
					grib_struct_data.setV04001(Integer.parseInt(year)); //年
					grib_struct_data.setV04002(Integer.parseInt(month)); //月
					grib_struct_data.setV04003(Integer.parseInt(day)); //日
					grib_struct_data.setV04004(Integer.parseInt(hour)); //时
					grib_struct_data.setV04005(Integer.parseInt(minute)); //分
					grib_struct_data.setV04006(Integer.parseInt(second)); //秒
					
					//加工中心
					grib_struct_data.setV_CCCC_N(GeneratingCentrer);
					
					//子中心
					int subCenter = grib1SectionProductDefinition_1.getSubCenter();
					grib_struct_data.setV_CCCC_SN(subCenter);
					
					//资料加工过程标识
					int generatingProcess = grib1SectionProductDefinition_1.getGenProcess();
					//System.out.println("generatingProcess:" + generatingProcess);
					
					//场类型
					int type_field = 1;
					//加工过程类型
					int type_process = 2;
					if(timevalue1==0&&timevalue2==0&&timeRangeIndicator==0)
					{
						type_field = 0;
						type_process = 0;
					}
					else 
					{
						type_field = 1;
						type_process = 2;
					}
					grib_struct_data.setV_FIELD_TYPE(type_field);
					grib_struct_data.settypeOfGeneratingProcess(type_process);
					
					//D_FILE_ID:四级编码+要素+层次类型+资料时间+区域+场类型+加工过程类型（grib1都一致）
					String d_file_id = d_data_id + "_" + element + "_" + level_type + "_" + datetime1 + "_" + area + "_" + type_field + "_" + type_process;
					grib_struct_data.setD_FILE_ID(d_file_id);
					
					//logger.info("d_file_id="+d_file_id);
					
					//区域+场类型+加工过程类型
					grib_struct_data.setarea_fieldType_genProcessType(area + "_" + type_field + "_" + type_process);
					
					//NAS文件存储路径：完整路径(包含NAS文件名)
					//String data_dir = d_data_id.substring(0, 6); //F.0010
					String day_dir = datetime1.substring(0, 8); //日期：20170802
					String data_time = datetime1.substring(0,10); //时次：2017080216 
					DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
					String data_dir = data_attributes.getdata_dir();
					String nas_filename = data_attributes.getdescription() + "_" + element +"_" + level_type + "_" + data_time +"_" + area + "_" + type_field + ".grib1";
					/*
					if(data_attributes.getIsEnsemble()) //如果是集合预报
					{
						nas_filename = data_attributes.getdescription() + "ENS" + "_" + element + "_" + data_time +"_" + area + ".grib1";
					}
					*/
					String storage_site = GribConfig.getPathNASFile() + "/" + data_dir + "/" + day_dir + "/" + nas_filename;
					//System.out.println("storage_site:" + storage_site);					
					grib_struct_data.setD_STORAGE_SITE(storage_site);
					
					grib_struct_data.setV_FILE_NAME(nas_filename);
					
					//产品描述?
					String Description = data_attributes.getdescription()+" "+element;
					grib_struct_data.setProductDescription(Description);
					//System.out.println("Description:" + Description);
					
					//文件大小
					grib_struct_data.setD_FILE_SIZE(0);
					
					//是否归档：0表示未归档
					grib_struct_data.setD_ARCHIVE_FLAG(0);
					
					//文件格式
					grib_struct_data.setV_FILE_FORMAT("grib1");
					
					//存储状态：4：实时存储
					grib_struct_data.setD_FILE_SAVE_HIERARCHY(4);
					
					//时效
					grib_struct_data.setV04320(time_value);
					
					//通信系统发过来的原始文件名
					grib_struct_data.setV_FILE_NAME_SOURCE(filename);
					
					//保留字段1
					grib_struct_data.setV_RETAIN1(999998);
					
					//保留字段2
					grib_struct_data.setV_RETAIN2(999998);
					
					//保留字段3
					grib_struct_data.setV_RETAIN3(999998);					
					
					
					//网格定义段模板
					int grid_template_num = grib1SectionGridDefinition_2.getGridTemplate();
					grib_struct_data.setgrid_template_num(grid_template_num);
					
					//产品定义模板
					int prod_template_num = grib1SectionProductDefinition_1.getGridDefinition();
					grib_struct_data.setprod_template_num(prod_template_num);
					
					//数据表示模板
					//grib1SectionBinaryData_4
					grib_struct_data.setdatarep_template_num(999998);
					
					//如果是集合预报，读取集合预报的成员变量
					if(data_attributes.getIsEnsemble()) //如果是集合预报
					{
						String ens_member = read_ensmember(filename);
						logger.info("ens_member:" + ens_member);
						grib_struct_data.setmember(ens_member);
					}
					
					//格点场数据:data数组
					float dataArray[] = grib1_Record.readData(randomAccessFile);
					logger.info("dataArray:" + dataArray.length);
					
					grib_struct_data.setdata(dataArray);
					//List<Grib_Struct_Data> grib_record_list = new ArrayList<Grib_Struct_Data>();
					//area_fieldType_genProcessType，按区域_场类型_加工过程类型，如：GLB_1_2
					String area_fieldType_genProcessType = grib_struct_data.getarea_fieldType_genProcessType();
					if(map_grib_decode_result.get(area_fieldType_genProcessType)==null)
					{
						List<Grib_Struct_Data> grib_record_list = new ArrayList<Grib_Struct_Data>();
						grib_record_list.clear();
						grib_record_list.add(grib_struct_data);
						map_grib_decode_result.put(area_fieldType_genProcessType, grib_record_list);
					}
					else
					{
						List<Grib_Struct_Data> grib_record_list = map_grib_decode_result.get(area_fieldType_genProcessType);
						grib_record_list.add(grib_struct_data);
						map_grib_decode_result.put(area_fieldType_genProcessType, grib_record_list);
					}
					
					
				}//end big try
				catch ( Exception e ) {
					String log_data = "解码失败，filename=" + filename + ",第" + grib1_count + "个grib1场," + getSysTime() + ",Exception:" + e.getMessage();
					logger_e.error(log_data);
					String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
					GribConfig.write_log_to_file(error_log_file, log_data);
				}
			}//end while
			
		}//end try
		catch ( IOException ioException )
		{	
			//ioException.getStackTrace();
			String log_data = "读取文件失败，filename=" + filename + "," + getSysTime() + ",Exception:" + ioException.getMessage();
			logger_e.error(log_data);
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);
		}
		finally
		{
			try 
			{
				if(randomAccessFile!=null)
				{
					randomAccessFile.flush();
					randomAccessFile.close();//关闭Grib文件
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return map_grib_decode_result;
	}
	
	//读取文件中的grib2场数据
	public Map<String,List<Grib_Struct_Data>> read_grib2_data(String filename,String d_data_id,Date rcv_time)
	{
		//System.out.println("文件名：" + filename +",四级编码："+d_data_id);
		//logger.info("文件名：" + filename +",四级编码："+d_data_id);
		//map:<"GLB",List1>,<"ANEA",List2>... 根据区域分List
		Map<String,List<Grib_Struct_Data>> map_grib_decode_result = new HashMap<String,List<Grib_Struct_Data>>(); 
		
		ucar.unidata.io.RandomAccessFile randomAccessFile = null;
		try {				
			randomAccessFile = new ucar.unidata.io.RandomAccessFile( filename, "r" );
			Grib2RecordScanner grib2RecordScanner = new Grib2RecordScanner( randomAccessFile );			
			
			int grib2_count = 0; //grib2记录数
			while ( grib2RecordScanner.hasNext() )
			{
				Grib_Struct_Data grib_struct_data = new Grib_Struct_Data();	
				grib_struct_data.setgrib_version(2);
				
				try {
					Grib2Record grib2_Record = grib2RecordScanner.next();
					
					grib2_count++; //grib2记录数
					//System.out.println("第" + grib2_count + "条grib2 record:");
					logger.info("第" + grib2_count + "条grib2 record:");
					
					Grib2SectionIndicator grib2SectionIndicator_0 = grib2_Record.getIs(); //0段：指示符段
					Grib2SectionIdentification grib2SectionIdentification_1 = grib2_Record.getId(); //1段：产品标识段
					Grib2SectionGridDefinition grib2SectionGridDefinition_3 = grib2_Record.getGDSsection(); //3段：网格定义段
					Grib2Pds grib2Pds_4 = grib2_Record.getPDS(); //4段：产品定义段					
					Grib2SectionDataRepresentation grib2SectionDataRepresentation_5 = grib2_Record.getDataRepresentationSection(); //5段：数据表示段
					
					//四级编码
					grib_struct_data.setdata_id(d_data_id);	
					
					//获取Datetime
					int year = grib2SectionIdentification_1.getYear();
					int month = grib2SectionIdentification_1.getMonth();
					int day = grib2SectionIdentification_1.getDay();
					int hour = grib2SectionIdentification_1.getHour();
					int minute = grib2SectionIdentification_1.getMinute();
					int second = grib2SectionIdentification_1.getSecond();
					String year_str = String.format("%02d",year);
					String month_str = String.format("%02d",month);
					String day_str = String.format("%02d",day);
					String hour_str = String.format("%02d",hour);
					String minute_str = String.format("%02d",minute);
					String second_str = String.format("%02d",second);
					String datetime1 = year_str+month_str+day_str
							+hour_str+minute_str+second_str;
					String datetime2 = year_str + "-" + month_str + "-" + day_str + " " 
					+ hour_str +":"+minute_str + ":" + second_str;
					//System.out.println("datatime1:" + datetime1);
					//logger.info("datatime1:" + datetime1);
					//System.out.println("datatime2:" + datetime2);	
					grib_struct_data.setTime(datetime1);
					grib_struct_data.setDATETIME(datetime2);
					
					//获取层次类型
					int level_type1 = grib2Pds_4.getLevelType1(); //层次类型1
					int level_type2 = grib2Pds_4.getLevelType2(); //层次类型2
					//System.out.println("level_type1:" + level_type1 + ",level_type2:" + level_type2);
					logger.info("level_type1:" + level_type1 + ",level_type2:" + level_type2);
					int levelType = level_type1;
					grib_struct_data.setLevelType(levelType);
					
					//获取要素名称：由学科、参数种类、参数编号决定
					int discipline = grib2SectionIndicator_0.getDiscipline();
					int parameterCategory = grib2Pds_4.getParameterCategory() ;
					int parameterNumber = grib2Pds_4.getParameterNumber();
					//System.out.println("discipline:" + discipline +",parameterCategory:" + parameterCategory + ",parameterNumber:" + parameterNumber);
					//logger.info("discipline:" + discipline +",parameterCategory:" + parameterCategory + ",parameterNumber:" + parameterNumber);
					GribDecoderConfigureHelper decoderConfigureHelper = GribDecoderConfigureHelper.instance();
					String element = decoderConfigureHelper.getGrib2ElementShortName(d_data_id, discipline, parameterCategory, parameterNumber, levelType);				
					if(element==null)	
					{
						element = discipline+"_"+parameterCategory+"_"+parameterNumber;
						//记录此条为null的element
						String log_data = "grib2:"+d_data_id+","+discipline+","+parameterCategory+","+parameterNumber+","+levelType+","+filename+ "," + getSysTime();
						logger_e.error("element not found," + log_data);
						
						//使用单独的线程池来入Rest EI
						if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
						{
							SendEiProcess sendEiProcess = SendEiProcess.getInstance();
							String KEvent = "模式解码要素名不存在：" + "grib2:"+d_data_id+","+discipline+","+parameterCategory+","+parameterNumber+","+levelType+","+filename;
							sendEiProcess.process_ei("OP_DPC_A_1_41_03", KEvent, grib_struct_data.getDATETIME(), grib_struct_data.getdata_id());
						}
						
						String element_log_file = GribConfig.getLogFilePath()+"element_null_"+GribConfig.getSysDate()+".log";
						GribConfig.write_log_to_file(element_log_file, log_data);
						System.out.println("element not found," + log_data);
						
						continue;
					}
					grib_struct_data.setElement(element);
					//System.out.println("element:" + element);
					//logger.info("element:" + element);
					String temp_file = GribConfig.getLogFilePath()+"temp_element_" + GribConfig.getSysDate() + ".log";
					GribConfig.write_log_to_file(temp_file, element);
					
					//设置要素服务代码，如：TEM_100
					grib_struct_data.setelement_serv(element+"_"+levelType);
					
					
					//获取层次高度（层次1、层次2）: 
					float level1 = (float)grib2Pds_4.getLevelValue1(); //层次1
					float level1_orig = level1;
					float level2 = (float)grib2Pds_4.getLevelValue2(); //层次2	
					float level2_orig = level2;
					if(level_type1==100||level_type1==101||level_type1==108) //把原始数据的单位pa转化为hpa
					{
						level1 = level1/100;
						level2 = level2/100;
					}
					else if(level_type1==106) //把单位进行转化，比如：0.28变为28
					{
						level1 = level1*100;
						level2 = level2*100;
					}
					//System.out.println("level1:" + level1 + ",level2:" + level2);
					//logger.info("level1:" + level1 + ",level2:" + level2);
					if(Float.isInfinite(level1)) //如果这个值是Infinity，转换成999998。这种情况在之前CIMISS中都赋值为0
					{
						//level1 = 999998;
						level1 = 0;
						level1_orig = 0;
					}
					if(Float.isInfinite(level2)) //如果这个值是Infinity，转换成999998
					{
						level2 = 999998;
						level2_orig = 999998;
					}
					//System.out.println("level1:" + level1 + ",level2:" + level2);
					logger.info("level1:" + level1 + ",level2:" + level2);
					grib_struct_data.setLevel1((int)level1);
					grib_struct_data.setLevel2((int)level2);
					grib_struct_data.setlevel1_orig((int)level1_orig);
					grib_struct_data.setlevel2_orig((int)level2_orig);
					
					//获取预报时效
					int timevalue = grib2Pds_4.getForecastTime();					
					int timeunit = grib2Pds_4.getTimeUnit(); //时效单位：0:分，1：时，2：日
					//如果是第四段是模板4.8，其预报时效是第50-53个八位组（根据之前在CIMISS中的解码方法）
					if(grib2Pds_4.getTemplateNumber()==8)
					{
						int timevalue_ret = read_timevalue_template48(filename);
						//System.out.println("timevalue_ret:" + timevalue_ret);
						//logger.info("timevalue_ret:" + timevalue_ret);
						if(timevalue_ret!=-1)
						{
							timevalue = timevalue_ret;
						}
					}
					else //只转换时效单位
					{						
						if(timeunit==0)
						{
							timevalue = timevalue/60;
						}
						else if(timeunit==2)
						{
							timevalue = timevalue*24;
						}
					}
					//System.out.println("timevalue:" + timevalue);
					logger.info("timevalue:" + timevalue);
					//System.out.println("timeunit:" + timeunit);
					//logger.info("timeunit:" + timeunit); 
					grib_struct_data.setValidTime(timevalue + "");
					
					grib_struct_data.setValueByteNum(4);					
					grib_struct_data.setValuePrecision(1); //1表示不变，10表示值乘10
					grib_struct_data.setGridUnits(0);
					
					//格点投影类型：Grib2:第3段的模板号
					int TemplateNumber = grib2SectionGridDefinition_3.getGDSTemplateNumber();
					//System.out.println("TemplateNumber:" + TemplateNumber);
					grib_struct_data.setGridProject(TemplateNumber);
					
					//获取经纬度
					Grib2Gds.LatLon grib2Gds_LatLon = ( Grib2Gds.LatLon )grib2SectionGridDefinition_3.getGDS(); //经纬度
					float start_lo = (float)(Math.round(grib2Gds_LatLon.lo1*1000))/1000;
					float end_lo = (float)(Math.round(grib2Gds_LatLon.lo2*1000))/1000;
					if(end_lo>360)  //为了保证end_lo>start_lo，nc库的读取会将end_lo在原始值上加360
					{
						end_lo = end_lo - 360;
					}
					float lo_space = (float)(Math.round(grib2Gds_LatLon.deltaLon*1000))/1000;
					int lo_number = grib2Gds_LatLon.getNx();
					float start_la = (float)(Math.round(grib2Gds_LatLon.la1*1000))/1000;
					float end_la = (float)(Math.round(grib2Gds_LatLon.la2*1000))/1000;
					float la_space = (float)(Math.round(grib2Gds_LatLon.deltaLat*1000))/1000;
					int la_number = grib2Gds_LatLon.getNy();	
					
					//modify,2019.5.29：区域转换，针对美国模式区域差错情况
					String area_src = start_la + "_" + end_la + "_"+ start_lo + "_"+ end_lo;
					String area_des = GribConfigAreaMapping.get_map_data_description().get(area_src);
					if(area_des!=null&&area_des!="")
					{
						area_src = area_des;
						
						String[] area_des_array = area_des.split("_");
						start_la =  Float.parseFloat(area_des_array[0]);
						end_la =  Float.parseFloat(area_des_array[1]);
						start_lo =  Float.parseFloat(area_des_array[2]);
						end_lo =  Float.parseFloat(area_des_array[3]);
					}
					
					//System.out.println("start_lo:" + start_lo);
					//System.out.println("end_lo:" + end_lo);
					//System.out.println("lo_space:" + lo_space);
					//System.out.println("la_number:" + la_number);
					//System.out.println("start_la:" + start_la);
					//System.out.println("end_la:" + end_la);
					//System.out.println("la_space:" + la_space);
					//System.out.println("lo_number:" + lo_number);
					//logger.info("start_lo:" + start_lo);
					//logger.info("end_lo:" + end_lo);
					//logger.info("lo_space:" + lo_space);
					//logger.info("la_number:" + la_number);
					//logger.info("start_la:" + start_la);
					//logger.info("end_la:" + end_la);
					//logger.info("la_space:" + la_space);
					//logger.info("lo_number:" + lo_number);
					
					grib_struct_data.setStartX(start_lo);
					grib_struct_data.setStartY(start_la);
					grib_struct_data.setEndX(end_lo);
					grib_struct_data.setEndY(end_la);
					grib_struct_data.setXStep(lo_space);
					grib_struct_data.setYStep(la_space);
					grib_struct_data.setXCount(lo_number);
					grib_struct_data.setYCount(la_number);
					
					grib_struct_data.setHeightCount(1);
					float[] heights={0};
					grib_struct_data.setHeights(heights);
					
					//加工中心
					int GeneratingCentrer = grib2SectionIdentification_1.getCenter_id();
					//System.out.println("GeneratingCentrer:" + GeneratingCentrer);
					grib_struct_data.setProductCenter(GeneratingCentrer+"");
					
					//制作方法，如无时用字符串0
					grib_struct_data.setProductMethod("0");
								
					
					//event_time
					grib_struct_data.setevent_time(rcv_time);
					
					//区域					
					BigDecimal start_la_1 = new BigDecimal(String.valueOf(start_la));					
					BigDecimal end_la_1 = new BigDecimal(String.valueOf(end_la));
					BigDecimal start_lo_1 = new BigDecimal(String.valueOf(start_lo));
					BigDecimal end_lo_1 = new BigDecimal(String.valueOf(end_lo));
					String area = decoderConfigureHelper.getGribArea(start_la_1, end_la_1, start_lo_1, end_lo_1);					
					if(area==null)
					{						
						area = start_la + "_" + end_la + "_"+ start_lo + "_"+ end_lo;
						//记录此条为null的area
						String log_data = "grib2:"+d_data_id+","+start_la+","+end_la+","+start_lo+","+end_lo+","+filename+ "," + getSysTime();
						logger_e.error("area not found," + log_data);
						
						//使用单独的线程池来入Rest EI
						if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
						{
							SendEiProcess sendEiProcess = SendEiProcess.getInstance();
							String KEvent = "模式解码区域名不存在：" + area;
							sendEiProcess.process_ei("OP_DPC_A_1_41_04", KEvent, grib_struct_data.getDATETIME(), grib_struct_data.getdata_id());
						}
						
						String area_log_file = GribConfig.getLogFilePath()+"area_null_"+GribConfig.getSysDate()+".log";
						GribConfig.write_log_to_file(area_log_file, log_data);
						//System.out.println("area not found," + log_data);
						
						continue;
					}
					grib_struct_data.setV_AREACODE(area);
					//System.out.println("area:" + area);
					//logger.info("area:" + area);					
					
					//第一个场文件收到时间
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String D_RYMDHM = dateFormat.format(rcv_time);
					grib_struct_data.setD_RYMDHM(D_RYMDHM);
					
					grib_struct_data.setV04001(year); //年
					grib_struct_data.setV04002(month); //月
					grib_struct_data.setV04003(day); //日
					grib_struct_data.setV04004(hour); //时
					grib_struct_data.setV04005(minute); //分
					grib_struct_data.setV04006(second); //秒
					
					//加工中心
					grib_struct_data.setV_CCCC_N(GeneratingCentrer);
					
					//子中心
					int subCenter = grib2SectionIdentification_1.getSubcenter_id();
					grib_struct_data.setV_CCCC_SN(subCenter);
					//System.out.println("subCenter:" + subCenter);
					
					//资料加工过程标识：
					int generatingProcess = grib2Pds_4.getGenProcessType();
					//System.out.println("generatingProcess:" + generatingProcess);
					
					//场类型
					int ifieldtype = grib2SectionIdentification_1.getTypeOfProcessedData();
					grib_struct_data.setV_FIELD_TYPE(ifieldtype);
					
					//D_FILE_ID:四级编码+要素+层次类型+资料时间+区域+场类型+加工过程类型
					String d_file_id = d_data_id + "_" + element + "_" + levelType + "_" + datetime1 + "_" + area + "_" + ifieldtype + "_" + generatingProcess;
					grib_struct_data.setD_FILE_ID(d_file_id);	
					
					//区域+场类型+加工过程类型
					grib_struct_data.setarea_fieldType_genProcessType(area + "_" + ifieldtype + "_" + generatingProcess);
					
					
					//NAS文件存储路径：完整路径(包含NAS文件名)
					//String data_dir = d_data_id.substring(0, 6); //F.0010
					String day_dir = datetime1.substring(0, 8); //日期：20170802
					String data_time = datetime1.substring(0,10); //时次：2017080216 
					DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
					String data_dir = data_attributes.getdata_dir();
					String nas_filename = data_attributes.getdescription() + "_" + element + "_" + levelType + "_" + data_time +"_" + area + "_" + ifieldtype + "_" + generatingProcess + ".grib2";
					/*
					if(data_attributes.getIsEnsemble()) //如果是集合预报
					{
						nas_filename = data_attributes.getdescription() + "ENS" + "_" + element + "_" + data_time +"_" + area + ".grib2";
					}
					*/
					String storage_site = GribConfig.getPathNASFile() + "/" + data_dir + "/" + day_dir + "/" + nas_filename;
					//System.out.println("storage_site:" + storage_site);					
					grib_struct_data.setD_STORAGE_SITE(storage_site);
					
					grib_struct_data.setV_FILE_NAME(nas_filename);
					//System.out.println("nas_filename:" + nas_filename);	
					
					//产品描述?
					String Description = data_attributes.getdescription()+" "+element;
					grib_struct_data.setProductDescription(Description);
					//System.out.println("Description:" + Description);	
					
					//文件大小
					grib_struct_data.setD_FILE_SIZE(0);
					
					//是否归档：0表示未归档
					grib_struct_data.setD_ARCHIVE_FLAG(0);
					
					//文件格式
					grib_struct_data.setV_FILE_FORMAT("grib2");
					
					//存储状态：4：实时存储
					grib_struct_data.setD_FILE_SAVE_HIERARCHY(4);
					
					//时效
					grib_struct_data.setV04320(timevalue);
					
					
					
					//通信系统发过来的原始文件名
					grib_struct_data.setV_FILE_NAME_SOURCE(filename);
					
					//拆分后的单场文件名
					
					//加工过程类型：006，补足0到3位
					int typeOfGeneratingProcess = grib2Pds_4.getGenProcessType();
					grib_struct_data.settypeOfGeneratingProcess(typeOfGeneratingProcess);
					
					//保留字段1
					grib_struct_data.setV_RETAIN1(typeOfGeneratingProcess);
					
					//保留字段2
					grib_struct_data.setV_RETAIN2(999998);
					
					//保留字段3
					grib_struct_data.setV_RETAIN3(999998);
					
					//网格定义段模板号
					int grid_template_num = grib2SectionGridDefinition_3.getGDSTemplateNumber();
					grib_struct_data.setgrid_template_num(grid_template_num);
					
					//产品定义模板号
					int prod_template_num = grib2Pds_4.getTemplateNumber();
					grib_struct_data.setprod_template_num(prod_template_num);
					
					//数据表示段模板号
					int datarep_template_num = grib2SectionDataRepresentation_5.getDataTemplate();
					grib_struct_data.setdatarep_template_num(datarep_template_num);
					
					//如果是集合预报，读取集合预报的成员变量
					if(data_attributes.getIsEnsemble()) //如果是集合预报
					{
						String ens_member = read_ensmember(filename);
						logger.info("ens_member:" + ens_member);
						grib_struct_data.setmember(ens_member);
					}
					
					//格点场数据:data数组
					float dataArray[] = grib2_Record.readData(randomAccessFile);
					grib_struct_data.setdata(dataArray);					
					logger.info("dataArray:" + dataArray.length);
					
					String area_fieldType_genProcessType = grib_struct_data.getarea_fieldType_genProcessType();
					if(map_grib_decode_result.get(area_fieldType_genProcessType)==null)
					{
						List<Grib_Struct_Data> grib_record_list = new ArrayList<Grib_Struct_Data>();
						grib_record_list.clear();
						grib_record_list.add(grib_struct_data);
						map_grib_decode_result.put(area_fieldType_genProcessType, grib_record_list);
					}
					else
					{
						List<Grib_Struct_Data> grib_record_list = map_grib_decode_result.get(area_fieldType_genProcessType);
						grib_record_list.add(grib_struct_data);
						map_grib_decode_result.put(area_fieldType_genProcessType, grib_record_list);
					}
					
					
				}//end big try
				catch ( Exception e ) {
					/*
					StackTraceElement[] traceElements = e.getStackTrace();
					StringBuffer buffer = new StringBuffer();//用于拼接异常方法调用堆栈信息
					for ( int i = 0; i < traceElements.length; i++ )
					{
						StackTraceElement currentElement = traceElements[ i ];
						buffer.append( "\nClassName: " + currentElement.getClassName() 
								+ " \tFileName: " + currentElement.getFileName()
								+ " \tLineNumber: " + currentElement.getLineNumber() 
								+ " \tMethodName: " + currentElement.getMethodName() );
					}
					*/
					//logger_e.error( "捕获解码单个网格数据异常并尝试继续解码: " + fileName + "  " + e.getClass().getName() + ": " + e.getMessage() + " 下面打印异常方法调用堆栈信息:" + buffer.toString() );
					e.printStackTrace();;
					String log_data = "解码失败，filename=" + filename + ",第" + grib2_count + "个grib2场," + getSysTime() + ",Exception:" + e.getMessage();
					logger_e.error(log_data);
					String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
					GribConfig.write_log_to_file(error_log_file, log_data);
				}
			}//end while
						
		}//end try
		catch ( IOException ioException )
		{
			//ioException.getStackTrace();
			String log_data = "读取文件失败，filename=" + filename + "," + getSysTime() + ",Exception:" + ioException.getMessage();
			logger_e.error(log_data);
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);
		}
		finally
		{
			try 
			{
				if(randomAccessFile!=null)
				{
					randomAccessFile.flush();
					randomAccessFile.close();//关闭Grib文件
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		return map_grib_decode_result;
	}
	
	//读取grib2的模板4.8的时效值
	//返回：-1表示出错，大于0表示正常
	private int read_timevalue_template48(String filename)
	{
		DataInputStream dataInStream = null;  //读数据的输入流
		
		int time_value = -1; //时效值
		int time_unit = 1; //时效单位
		try 
	    { 
			dataInStream = new DataInputStream(new BufferedInputStream(new FileInputStream(filename)));
			
			//读取第0段：16个字节
			byte[] b0 = new byte[16];
			dataInStream.read(b0, 0, 16);
			
			//读取第1段
			int len1 = dataInStream.readInt();
			byte[] b1 = new byte[len1-4];
			dataInStream.read(b1, 0, len1-4);
			
			//读取第2段
			int len2 = dataInStream.readInt();
			int num_2 = dataInStream.read(); //第2段段号
			
			if(num_2==2) //如果第2段存在
			{
				byte[] b2 = new byte[len2-5];
				dataInStream.read(b2, 0, len2-5);
				
				//读取第3段
				int len3 = dataInStream.readInt();
				byte[] b3 = new byte[len3-4];
				dataInStream.read(b3, 0, len3-4);
				
				//读取第4段的时效：第49字节为时效单位，第50-53字节为时效
				byte[] b4 = new byte[48];
				dataInStream.read(b4, 0, 48); //读取前48个字节
				time_unit = dataInStream.read(); //时效单位：0:分，1：时，2：日
				time_value = dataInStream.readInt(); //时效
			}
			else //如果第2段不存在，直接读取第3段
			{
				byte[] b3 = new byte[len2-5];
				dataInStream.read(b3, 0, len2-5);
				
				//读取第4段的时效：第49字节为时效单位，第50-53字节为时效
				byte[] b4 = new byte[48];
				dataInStream.read(b4, 0, 48); //读取前48个字节
				time_unit = dataInStream.read(); //时效单位：0:分，1：时，2：日
				time_value = dataInStream.readInt(); //时效
			}			

			//转换时效单位
			if(time_unit==0)
			{
				time_value = time_value/60;
			}
			else if(time_unit==2)
			{
				time_value = time_value*24;
			}		
			
			
	    }
		catch (IOException ex1) 
	    { 	 
	    	 //ex1.printStackTrace(); 
	    	 String log_data = "读取文件失败（为了获取模板4.8的时效值），filename=" + filename + "," + getSysTime() + ",Exception:" + ex1.getMessage();	    	 
	    	 logger_e.error(log_data);
	    	 String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
	    	 GribConfig.write_log_to_file(error_log_file, log_data);
	    } 
	    finally
	    { 
	    	try 
	    	{ 	    		 
	    		if(dataInStream!=null)
	    		{	    	
	    			dataInStream.close(); 
	    		}	
	    	} 
	    	catch (IOException ex2) 
	    	{ 
	    		ex2.printStackTrace(); 
	    	} 
	     }
		
		return time_value;
	}
	
	//读取集合预报的成员变量值
	//@ 返回：集合成员变量值
	private String read_ensmember(String filename)
	{
		logger.info("读取成员变量，filename=" + filename);
		String ens0 = "";
		ucar.nc2.NetcdfFile ncFile = null;		
		try
		{
			//ncFile = ucar.nc2.NetcdfFile.open(filename);
			//ncFile = ucar.nc2.NetcdfFile.openInMemory(filename);
			//ncFile = ucar.nc2.NetcdfFile.open(randomAccessFile1, null, null, null);
			ncFile = ucar.nc2.dataset.NetcdfDataset.openFile(filename, null);	
			//ncFile = ucar.nc2.dataset.NetcdfDataset.openInMemory(filename);
			
			//logger.info("open file");
			int[] ens_array = (int[]) ncFile.findVariable("ens").read().copyTo1DJavaArray();			
			logger.info("ens length:" + ens_array.length);
			if(ens_array.length>=1)
			{
				ens0 = String.valueOf(ens_array[0]);	
			}
			/*
			for(int z=0;z<ens_array.length;z++)
			{
				logger.info("z:" + ens_array[z] + ",");
			}
			*/
		}
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			String log_data = "读取文件集合成员变量失败，filename=" + filename + "," + getSysTime() + ",Exception:" + e.getMessage();
			logger_e.error(log_data);
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);
		}
		finally
		{
			if(ncFile!=null)
			{
				try {
					ncFile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}		
			
		}
		
		//删除读取集合变量后生成的临时文件:*.gbx,*.ncx
		//delete_gbx_ncx_file(filename);
		
		return ens0;
	}
		
	//删除读取集合变量后生成的临时文件:*.gbx,*.ncx
	private void delete_gbx_ncx_file(String filename)
	{
		String cmd_delete = "rm -rf " + filename + ".gbx* " + filename + ".ncx*";
		logger.info("cmd_delete=" + cmd_delete);
		
		Process process = null;
		Runtime runtime = Runtime.getRuntime();
		int result_transfer_grid_simple = 1;
		try 
		{			
			process = runtime.exec(cmd_delete);
					
			// 采用字符流读取缓冲池内容，腾出空间
			steamout(process.getErrorStream(),filename,"error_delete_gbx_ncx_file");
			steamout(process.getInputStream(),filename,"output_delete_gbx_ncx_file");
					
			//result_transfer_grid_simple = process.waitFor();			
				
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			
			String log_data = "delete_gbx_ncx_file函数异常,filename="+filename + "," + getSysTime()+",Exception:" + e.getMessage();
	        logger_e.error(log_data);
	        String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
	        GribConfig.write_log_to_file(error_log_file, log_data);
		}
	}
	
	//将原始的多场文件，拆分grib数据，拆成单场
	//多场的数据有两种组织方式：GRIB...7777...GRIB...7777	）
	//每个场数据长度：使用第0段的标识长度来确定。存在问题：EC的SST的文件，0段标识的长度不包含末尾的“7777”
	//@cassadraOper:cassadraOper对象，如果为null，则不入cassadraOper
	//@intoDb_fieldInfo:是否入单场信息表，如：nafp_ecmf_for_ftm_file_tab
	//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
	public String split_grib_record_use_S0Lengh(String original_filename,String d_data_id,
	        Date rcv_time,CassandraDbOperation cassadraOper,CassandraM4DbOperation m4cassadraOper,
	        boolean intoDb_fieldInfo,boolean di)
	{
		System.out.println("split_grib_record_use_S0Lengh.original_filename:"+original_filename);
		logger.info("begin to process file: " + original_filename + ", #not complete#");
		
	     DataInputStream dataInStream = null;  //读数据的输入流
	     DataOutputStream dataOutStream = null; //写数据的输出流     
	     
	     int ret_num = 0;
	     
	     //modify,2019.7.11
	     int decodeSum = 0; //解码成功的场数
	     int cassandraSum = 0; //入Cassandra成功的场数
	     int indexSum = 0; //入索引表成功的场数
	     int fileError = 0; //是否文件读取错误
	     try 
	     { 
	    	 dataInStream = new DataInputStream(new BufferedInputStream(new FileInputStream(original_filename))); 
	    	 
	    	 //fileOutStream = new FileOutputStream("new_file.bin");
	    	 //dataOutStream = new DataOutputStream((new FileOutputStream("new_file.bin")));  
	    	 	    	 
	    	 int size = dataInStream.available();
	    	 System.out.println("size:" + size);
	    	 
	    	 int field_count = 0; //记录是第几个场
	    	 long byte_count = 0; //记录读了多少个字节
	    	//先定位到开头的“GRIB”
	    	 int begin_a = dataInStream.read(); //记录四个开头的字符
	    	 int begin_b = dataInStream.read();
	    	 int begin_c = dataInStream.read();
	    	 int begin_d = dataInStream.read();
	    	 byte_count = byte_count + 4;
	    	 
	    	 while (begin_a!=-1&&begin_b!=-1&&begin_c!=-1&&begin_d!=-1)  //如果读到结尾，跳出
	    	 { 
	    		 //System.out.printf("begin_a:%x\n",begin_a);
	    		 //System.out.printf("begin_b:%x\n",begin_b);
	    		 //System.out.printf("begin_c:%x\n",begin_c);
	    		 //System.out.printf("begin_d:%x\n",begin_d);
	    		 
	    		 if(begin_a==0x47&&begin_b==0x52&&begin_c==0x49&&begin_d==0x42) //读到“GRIB”
	    		 {	    			 
	    			 field_count++; //第几个场数据	    			 
	    			 System.out.println("The " + field_count + " field record");
	    			 System.out.println("byte_count=" + byte_count);
	    			 //GribConfig.write_log_to_file("run_test.log", "The " + field_count + " field record," + "byte_count=" + byte_count);
	    			 
	    			 int byte_5 = dataInStream.read(); //第5个字节
	    			 int byte_6 = dataInStream.read();
	    			 int byte_7 = dataInStream.read();
	    			 int version = dataInStream.read(); //版本号
	    			 byte_count = byte_count + 4;
	    			 
	    			 System.out.println("version:" + version);
	    			 
	    			 if(dataOutStream!=null) //先关闭之前写的数据流
	    			 {
	    				 dataOutStream.flush();
	    				 dataOutStream.close();
	    			 }
	    			 
	    			 //original_filename:/CIMISS2/data/F/F.0013.0001.R001/201712/2017121515/
	    			 //A_HHXO25EDZW151200_C_BABJ_20171215153725_09209.bin
	    			 //field_filename:.../F.0013.0001.S001/20171215/A_HHXO25EDZW151200_C_BABJ_20171215153725_09209.bin.1
	    			 //System.out.println("original_filename:" + original_filename);
	    			 String original_filename_array[] = original_filename.split("/");
	    			 String l_dir = original_filename_array[original_filename_array.length-2]; //上一级目录，往往是时间目录
	    			 if(l_dir.length()>=8)
	    			 {
	    				 l_dir = l_dir.substring(0, 8);
	    			 }
	    			 String field_filepath = GribConfig.getPathNASFile() + "/field_file/" + d_data_id.replace("R001", "S001") + 
	    					 "/" + l_dir + "/";
	    			 String field_filename = field_filepath + original_filename_array[original_filename_array.length-1]+"."+field_count;
	    			
	    			 //System.out.println("field_filepath:" + field_filepath);
	    			 //System.out.println("field_filename:" + field_filename);
	    			 
	    			 //logger.info("field_filepath:" + field_filepath);
	    			 logger.info("field_filename:" + field_filename);
	    			 
	    			 //如果目录不存在，建目录
	    			 File file_path = new File(field_filepath);
	    			 if(!file_path.exists())
	    			 {
	    				 logger.info(field_filepath + " not exist, construct this dir");
	    				 boolean r1 = file_path.mkdirs();
	    				 logger.info("construct dir result:" + r1);
	    			 }	    			 
	    			 
	    			 if(dataOutStream!=null) //先关闭之前写的数据流
	    			 {
	    				 dataOutStream.flush();
	    				 dataOutStream.close();
	    			 }
	    			 
	    			 dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(field_filename))); //单场文件名
	    			 //dataOutStream = new DataOutputStream((new FileOutputStream("new_file.bin")));
	    			 if(version==1) //grib版本1
	    			 {
	    				 dataOutStream.write(begin_a);
	    				 dataOutStream.write(begin_b);
	    				 dataOutStream.write(begin_c);
	    				 dataOutStream.write(begin_d);
	    				 dataOutStream.write(byte_5);
	    				 dataOutStream.write(byte_6);
	    				 dataOutStream.write(byte_7);
	    				 dataOutStream.write(version);
	    				 
	    				 int field_lengh = byte_5*65536 + byte_6*256 + byte_7; //单场的长度
	    				 //System.out.println("field_lengh:" + field_lengh);
	    				 byte[] b = new byte[field_lengh-8];
	    				 dataInStream.read(b, 0, field_lengh-8); //读取剩余的字节
	    				 byte_count = byte_count + field_lengh-8;
	    				 dataOutStream.write(b, 0, field_lengh-8); //写剩余的字节
	    				 
	    				 if(dataOutStream!=null) //关闭写的数据流
		    			 {
		    				 dataOutStream.flush();
		    				 dataOutStream.close();
		    			 }
	    				 
	    				 //处理这个单场文件
	    				 logger.info("split complete.");
	    				 //int ret = process_field_file(original_filename,field_filename,"1",d_data_id,rcv_time,cassadraOper,intoDb_fieldInfo);
	    				 String ret = process_field_file(original_filename,field_filename,"1",d_data_id,rcv_time,cassadraOper,m4cassadraOper,intoDb_fieldInfo,di);
	    				 logger.info("process_field_file complete, " + field_filename + ",ret="+ret);
	    				 String[] ret_array = ret.split("_"); //返回：解码成功场数_入Cassandra场数_入索引表场数，如：1_0_1
	    				 decodeSum = decodeSum + Integer.parseInt(ret_array[0]);
	    				 cassandraSum = cassandraSum + Integer.parseInt(ret_array[1]);
	    				 indexSum = indexSum + Integer.parseInt(ret_array[2]);	    				 
	    				 //ret_num = ret_num + ret;
	    			 }
	    			 else if(version==2) //grib版本2
	    			 {
	    				 //前8个字节
	    				 dataOutStream.write(begin_a);
	    				 dataOutStream.write(begin_b);
	    				 dataOutStream.write(begin_c);
	    				 dataOutStream.write(begin_d);
	    				 dataOutStream.write(byte_5);
	    				 dataOutStream.write(byte_6);
	    				 dataOutStream.write(byte_7);
	    				 dataOutStream.write(version);
	    				 
	    				 byte[] byte_field_lengh = new byte[8];
	    				 dataInStream.read(byte_field_lengh, 0, 8); //读取8个字节的场长度
	    				 byte_count = byte_count + 8;
	    				 dataOutStream.write(byte_field_lengh, 0, 8); //写
	    				 
	    				 long field_lengh = bytesToLong(byte_field_lengh); //单场数据的长度
	    				 //System.out.println("field_lengh:" + field_lengh);
	    				 for(long t=0;t<field_lengh-16;t++) //读取剩余的字节
	    				 {
	    					 int byte_temp = dataInStream.read(); 
	    					 byte_count++;
	    					 dataOutStream.write(byte_temp); //写完剩下的字节
	    				 } 
	    				 
	    				 if(dataOutStream!=null) //关闭写的数据流
		    			 {
		    				 dataOutStream.flush();
		    				 dataOutStream.close();
		    			 }
	    				 
	    				 //处理这个单场文件
	    				 logger.info("split complete.");
	    				 //int ret = process_field_file(original_filename,field_filename,"2",d_data_id,rcv_time,cassadraOper,intoDb_fieldInfo);
	    				 String ret = process_field_file(original_filename,field_filename,"2",d_data_id,rcv_time,cassadraOper,m4cassadraOper,intoDb_fieldInfo,di);
	    				 logger.info("process_field_file complete, " + field_filename + ",ret="+ret);
	    				 String[] ret_array = ret.split("_"); //返回：解码成功场数_入Cassandra场数_入索引表场数，如：1_0_1
	    				 decodeSum = decodeSum + Integer.parseInt(ret_array[0]);
	    				 cassandraSum = cassandraSum + Integer.parseInt(ret_array[1]);
	    				 indexSum = indexSum + Integer.parseInt(ret_array[2]);	    				 
	    				 //ret_num = ret_num + ret;
	    			 }
	    			 
	    			
	    		 }
	    		 
	    		 //往下读一个字节
	    		 begin_a = begin_b;
	    		 begin_b = begin_c;
	    		 begin_c = begin_d;
	    		 begin_d = dataInStream.read();	   
	    		 byte_count++;
	    		 
	    	 } 
	       
	    	 //System.out.println("");	    	
	    	 	    	
	     } 
	     catch (IOException ex1) 
	     { 
	    	 //ret_num = -1;
	    	 fileError = -1;
	    	 
	    	 //ex1.printStackTrace(); 
	    	 String log_data = "读取文件失败，filename=" + original_filename + "," + getSysTime() + ",Exception:" + ex1.getMessage();
	    	 logger_e.error(log_data);
	    	 String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
	    	 GribConfig.write_log_to_file(error_log_file, log_data);
	     } 
	     finally
	     { 
	    	 try 
	    	 { 	    		 
	    		 if(dataInStream!=null)
	    		 {	
	    			 dataInStream.close(); 
	    		 }	    		 
	    		 if(dataOutStream!=null)
	    		 {
	    			 dataOutStream.flush();
	    			 dataOutStream.close(); 
	    		 }
	    	 } 
	    	 catch (IOException ex2) 
	    	 { 
	    		 ex2.printStackTrace(); 
	    	 } 
	     }
	     
	     //return ret_num;
	     
	     //modify,2019.7.11：更新返回值：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
	     String ret_str = decodeSum + "_" + cassandraSum + "_" + indexSum + "_" + fileError;
	     logger.info("split_grib_record_use_S0Lengh ret_str:" + ret_str);
	     return ret_str;
	}
	
	//将原始的多场文件，拆分grib数据，拆成单场
	//多场的数据有两种组织方式：GRIB...7777...GRIB...7777	）
	//每个场数据长度：使用从GRIB到下一个GRIB之间的数据
	//可能出现一个场：GRIB...7777...7777...7777...GRIB的情况，如：W_NAFP_C_ECMF_20171228060042_P_C1D12280000122800011的第295个场文件
	//返回：写NAS文件并入库成功的场数
	//@cassadraOper:cassadraOper对象，如果为null，则不入cassadraOper
	//@intoDb_fieldInfo:是否入单场信息表，如：nafp_ecmf_for_ftm_file_tab
	//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
	public String split_grib_record_readGRIB(String original_filename,String d_data_id,Date rcv_time,
			CassandraDbOperation cassadraOper,CassandraM4DbOperation m4cassadraOper,
			boolean intoDb_fieldInfo,boolean di)
	{
		logger.info("begin to process file: " + original_filename + ", #not complete#");
		
		//将欧洲模式资料进行复杂压缩转简单压缩的操作
		/*
		if(d_data_id.startsWith("F.0010"))
		{
			transfer_to_grid_simple(original_filename);
		}
		*/
		
		 //FileInputStream fileInStream = null; 
	     DataInputStream dataInStream = null;  //读数据的输入流
	     //FileOutputStream fileOutStream = null; 
	     DataOutputStream dataOutStream = null; //写数据的输出流
	     
	     int ret_num = 0;
	     
	     //modify,2019.7.11
	     int decodeSum = 0; //解码成功的场数
	     int cassandraSum = 0; //入Cassandra成功的场数
	     int indexSum = 0; //入索引表成功的场数
	     int fileError = 0; //是否文件读取错误
	     
	     int temp ; 
	     try 
	     { 
	    	 dataInStream = new DataInputStream(new BufferedInputStream(new FileInputStream(original_filename))); 
	    	 
	    	 //fileOutStream = new FileOutputStream("new_file.bin");
	    	 //dataOutStream = new DataOutputStream((new FileOutputStream("new_file.bin")));  
		    	 	    	 
	    	 int size = dataInStream.available();
	    	 //System.out.println("size:" + size);
	    	 
	    	 int field_count = 0; //记录是第几个场
	    	 long byte_count = 0; //记录读了多少个字节
	    	 //记录开头的5个字节
	    	 int data = 0;
	    	 int begin_a = 0; //记录四个开头的字符
	    	 int begin_b = 0;
	    	 int begin_c = 0;
	    	 int begin_d = 0;
	    	 
	    	 String field_filename = ""; //场文件名
	    	 int version = 0; //版本号
	    	 boolean write_sign = false; //是否开始写数据的标志
	    	 while (data!=-1||begin_a!=-1||begin_b!=-1||begin_c!=-1||begin_d!=-1)  //如果读到结尾，才跳出
	    	 { 
	    		 //往下读一个字节
	    		 data = begin_a;
	    		 begin_a = begin_b;
	    		 begin_b = begin_c;
	    		 begin_c = begin_d;
	    		 begin_d = dataInStream.read();	   
	    		 byte_count++;
	    		 //System.out.printf("begin_a:%x\n",begin_a);
	    		 //System.out.printf("begin_b:%x\n",begin_b);
	    		 //System.out.printf("begin_c:%x\n",begin_c);
	    		 //System.out.printf("begin_d:%x\n",begin_d);
	    		 
	    		 if(write_sign) //等读到了“GRIB”后才开始写数据
	    		 {
	    			 dataOutStream.write(data);
	    		 }
	    		 
	    		 if(begin_a==0x47&&begin_b==0x52&&begin_c==0x49&&begin_d==0x42) //读到“GRIB”
	    		 {	
	    			 if(dataOutStream!=null) //先关闭之前写的数据流
	    			 {
	    				 dataOutStream.flush();
	    				 dataOutStream.close();
	    			 }
	    			 
	    			 //读到了场数据后，进行处理
	    			 if(field_count>=1)
	    			 {
		    			 //处理这个单场文件
		    			 //int ret = process_field_file(original_filename,field_filename,version+"",d_data_id,rcv_time,cassadraOper,intoDb_fieldInfo);	
	    				 String ret = process_field_file(original_filename,field_filename,version+"",d_data_id,rcv_time,cassadraOper,m4cassadraOper,intoDb_fieldInfo,di);
	    				 logger.info("process_field_file complete, " + field_filename + ",ret="+ret);
	    				 String[] ret_array = ret.split("_"); //返回：解码成功场数_入Cassandra场数_入索引表场数，如：1_0_1
	    				 decodeSum = decodeSum + Integer.parseInt(ret_array[0]);
	    				 cassandraSum = cassandraSum + Integer.parseInt(ret_array[1]);
	    				 indexSum = indexSum + Integer.parseInt(ret_array[2]);	    				 
		    			 //ret_num = ret_num + ret;
	    			 }	    			 
	    			 
	    			 field_count++; //第几个场数据	    			 
	    			 //System.out.println("The " + field_count + " field record");
	    			 //System.out.println("byte_count=" + byte_count);
	    			 //GribConfig.write_log_to_file("run_test.log", "The " + field_count + " field record," + "byte_count=" + byte_count);
	    			 
	    			 
	    			 //original_filename:/CIMISS2/data/F/F.0012.0002.R001/201712/2017121515/A_HHXO25EDZW151200_C_BABJ_20171215153725_09209.bin
	    			 //field_filename:.../F.0012.0002.S001/20171215/A_HHXO25EDZW151200_C_BABJ_20171215153725_09209.bin.1
	    			 //System.out.println("original_filename:" + original_filename);
	    			 String original_filename_array[] = original_filename.split("/");
	    			 String l_dir = original_filename_array[original_filename_array.length-2]; //上一级目录，往往是时间目录
	    			 if(l_dir.length()>=8)
	    			 {
	    				 l_dir = l_dir.substring(0, 8);
	    			 }
	    			 String field_filepath = GribConfig.getPathNASFile() + "/field_file/" + d_data_id.replace("R001", "S001") + 
	    					 "/" + l_dir + "/";
	    			 field_filename = field_filepath + original_filename_array[original_filename_array.length-1]+"."+field_count;
	    			
	    			 //System.out.println("field_filepath:" + field_filepath);
	    			 //logger.info("field_filename:" + field_filename);
	    			 
	    			 //如果目录不存在，建目录
	    			 File file_path = new File(field_filepath);
	    			 if(!file_path.exists())
	    			 {
	    				 file_path.mkdirs();
	    			 }
		    			 
	    			 dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(field_filename))); //单场文件名
	    			 
	    			 write_sign = true; //写数据的标志
	    			 
	    			 //写“GRIB”
	    			 dataOutStream.write(begin_a);
	    			 dataOutStream.write(begin_b);
	    			 dataOutStream.write(begin_c);
	    			 dataOutStream.write(begin_d);
	    			 
	    			 //往下读到版本号
	    			 data = begin_d; 
	    			 begin_a = dataInStream.read(); 
	    			 begin_b = dataInStream.read();
	    			 begin_c = dataInStream.read();
	    			 begin_d = dataInStream.read(); //版本号
	    			 
	    			 version = begin_d; //版本号    			 
	    			 	    			
	    			 
	    		 }	    		 
	    		 
	    	 } 
	    	 
	    	//处理最后一个场文件
			if(field_count>=1)
			{
				//处理这个单场文件
				//int ret = process_field_file(original_filename,field_filename,version+"",d_data_id,rcv_time,cassadraOper,intoDb_fieldInfo);
				String ret = process_field_file(original_filename,field_filename,version+"",d_data_id,rcv_time,cassadraOper,m4cassadraOper,intoDb_fieldInfo,di);
				logger.info("process_field_file complete, " + field_filename + ",ret="+ret);
				String[] ret_array = ret.split("_"); //返回：解码成功场数_入Cassandra场数_入索引表场数，如：1_0_1
				decodeSum = decodeSum + Integer.parseInt(ret_array[0]);
				cassandraSum = cassandraSum + Integer.parseInt(ret_array[1]);
				indexSum = indexSum + Integer.parseInt(ret_array[2]);				
				//ret_num = ret_num + ret;
    		 
			}	
		       
	    	 //System.out.println("");	    	
	    	 	    	
	     } 
	     catch (IOException ex1) 
	     { 
	    	 //ret_num = -1;
	    	 fileError = -1;
	    	 
	    	 //ex1.printStackTrace(); 
	    	 String log_data = "读取文件失败，filename=" + original_filename + "," + getSysTime() + ",Exception:" + ex1.getMessage();
	    	 logger_e.error(log_data);
	    	 String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
	    	 GribConfig.write_log_to_file(error_log_file, log_data);
	     } 
	     finally
	     { 
	    	 try 
	    	 { 	    		 
	    		 if(dataInStream!=null)
	    		 {	    	
	    			 dataInStream.close(); 
	    		 }	    		 
	    		 if(dataOutStream!=null)
	    		 {
	    			 dataOutStream.flush();
	    			 dataOutStream.close(); 
	    		 }
	    	 } 
	    	 catch (IOException ex2) 
	    	 { 
	    		 ex2.printStackTrace(); 
	    	 } 
	     }
	     
	     //return ret_num;
	     String ret_str = decodeSum + "_" + cassandraSum + "_" + indexSum + "_" + fileError;
	     logger.info("split_grib_record_readGRIB ret_str:" + ret_str);
	     return ret_str;
	}
	
	//2017.12.6：测试日本高分多场拆分后为什么报：Wrong message length
	//原因：写单场的总长度用的是原来多场的长度
	private void test(String filename)
	{
		DataInputStream dataInStream = null;  //读数据的输入流
	     
		DataInputStream dataInStream2 = null;  //读数据的输入流
		DataInputStream dataInStream3 = null;  //读数据的输入流
		
		String file1 = "测试数据/日本_高分_原始多场/W_NAFP_C_RJTD_20171206060000_GSM_GPV_RRA2_GLL0P5DEG_LP10_FD0000_GRIB2.BIN.1";
		String file2 = "测试数据/日本_高分_原始多场/W_NAFP_C_RJTD_20171206060000_GSM_GPV_RRA2_GLL0P5DEG_LP10_FD0000_GRIB2-0.BIN";
		String file3 = "测试数据/日本_高分_原始多场/W_NAFP_C_RJTD_20171206060000_GSM_GPV_RRA2_GLL0P5DEG_LP10_FD0000_GRIB2.BIN";
	     
	     try
	     {
	    	 dataInStream = new DataInputStream(new BufferedInputStream(new FileInputStream(file1))); 
	    	 dataInStream2 = new DataInputStream(new BufferedInputStream(new FileInputStream(file2))); 
	    	 dataInStream3 = new DataInputStream(new BufferedInputStream(new FileInputStream(file3)));
	    	 
	    	 int i=0;
	    	 for(int j=0;j<0x17320;j++)
	    	 {
	    		 i++;
	    		 int temp = dataInStream.read();
	    		 int temp2 = dataInStream2.read();
	    		 int temp3 = dataInStream3.read();
	    		 
	    		 //System.out.printf("i:%d,temp:%x,temp2:%x\n",i,temp,temp2); 
	    		 
	    		 if(temp!=temp2)
	    		 {
	    			 System.out.printf("i:%d,temp:%x,temp2:%x\n",i,temp,temp2); 
	    		 }	    		 
	    		 
	    	 }
	     }
	     catch (IOException ex2) 
    	 { 
    		 ex2.printStackTrace(); 
    	 } 
	}
	
	//将原始的多场文件，拆分grib数据，拆成单场
	//多场数据组织方式：循环嵌套（如日本高分）
	//@cassadraOper:cassadraOper对象，如果为null，则不入cassadraOper
	//@intoDb_fieldInfo:是否入单场信息表，如：nafp_ecmf_for_ftm_file_tab
	//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
	public String split_grib_record_other(String original_filename,String d_data_id,Date rcv_time,
	        CassandraDbOperation cassadraOper,CassandraM4DbOperation m4cassadraOper,
	        boolean intoDb_fieldInfo,boolean di)
	{
		logger.info("begin to process file: " + original_filename + ", #not complete#");
		
	     DataInputStream dataInStream = null;  //读数据的输入流
	     DataOutputStream dataOutStream = null; //写数据的输出流
	     int ret_num = 0;
	     
	     //modify,2019.7.11
	     int decodeSum = 0; //解码成功的场数
	     int cassandraSum = 0; //入Cassandra成功的场数
	     int indexSum = 0; //入索引表成功的场数
	     int fileError = 0; //是否文件读取错误
	     
	     int temp ; 
	     
	     try 
	     { 
	    	 dataInStream = new DataInputStream(new BufferedInputStream(new FileInputStream(original_filename))); 
	    	 int size = dataInStream.available();
	    	 System.out.println("size:" + size);
	    	 
	    	 int field_count = 0; //记录是第几个场
	    	//先定位到开头的“GRIB”
	    	 int begin_a = dataInStream.read(); //记录四个开头的字符
	    	 int begin_b = dataInStream.read();
	    	 int begin_c = dataInStream.read();
	    	 int begin_d = dataInStream.read();
	    	 while (begin_a!=-1&&begin_b!=-1&&begin_c!=-1&&begin_d!=-1)  //如果读到结尾，跳出
	    	 {
	    		 if(begin_a==0x47&&begin_b==0x52&&begin_c==0x49&&begin_d==0x42) //读到“GRIB”
	    		 {
	    			 int byte_5 = dataInStream.read(); //第5个字节
	    			 int byte_6 = dataInStream.read();
	    			 int byte_7 = dataInStream.read();
	    			 int version = dataInStream.read(); //版本号
	    			 	    			 	    			 
	    			 if(version==2)
	    			 {
	    				 long file_lengh = dataInStream.readLong(); //读取8个字节的文件（原始多场）长度
	    				 System.out.println("file_lengh:" + file_lengh);
	    				 
	    				 /*先读取第一个单场的每一段的数据及长度*/
	    				 int lenth_section_1 = dataInStream.readInt(); //读取4个字节的第一段长度	    				 
	    				 byte[] array_section_1 = new byte[lenth_section_1-4]; 
	    				 dataInStream.read(array_section_1, 0, lenth_section_1-4); //读取第一段的数据
	    				 
	    				 int lenth_section_2 = dataInStream.readInt(); //读取4个字节的第二段长度	 
	    				 int number_section_2 = dataInStream.read(); //读取第二段编号
	    				 byte[] array_section_2 = null;
	    				 boolean exist_section_2 = true; //第二段是否存在
	    				 int lenth_section_3 = 0;
	    				 int number_section_3 = 3;
	    				 byte[] array_section_3 = null;
	    				 if(number_section_2==2) //存在第二段
	    				 {
	    					 exist_section_2 = true;
	    					 
	    					 //读取第二段数据
		    				 array_section_2 = new byte[lenth_section_2-5]; 
		    				 dataInStream.read(array_section_2, 0, lenth_section_2-5); //读取第二段的数据
		    				 
		    				 //读取第三段
		    				 lenth_section_3 = dataInStream.readInt(); //读取3个字节的第三段长度	
		    				 number_section_3 = dataInStream.read(); //读取第三段编号
		    				 array_section_3 = new byte[lenth_section_3-5]; 
		    				 dataInStream.read(array_section_3, 0, lenth_section_3-5); //读取第三段的数据
	    				 }
	    				 else //没有第二段，直接到第三段
	    				 {
	    					 exist_section_2 = false;    					 
	    					 
	    					 lenth_section_3 = lenth_section_2;
	    					 number_section_3 = number_section_2;
	    					 array_section_3 = new byte[lenth_section_3-5]; 
		    				 dataInStream.read(array_section_3, 0, lenth_section_3-5); //读取第三段的数据
		    				 
		    				 lenth_section_2 = 0;
	    				 }	    				 
	    				 
	    				 
	    				 int lenth_section_4 = dataInStream.readInt(); //读取4个字节的第四段长度	    				 
	    				 byte[] array_section_4 = new byte[lenth_section_4-4]; 
	    				 dataInStream.read(array_section_4, 0, lenth_section_4-4); //读取第四段的数据
	    				 
	    				 int lenth_section_5 = dataInStream.readInt(); //读取4个字节的第五段长度	    				 
	    				 byte[] array_section_5 = new byte[lenth_section_5-4]; 
	    				 dataInStream.read(array_section_5, 0, lenth_section_5-4); //读取第五段的数据
	    				 
	    				 int lenth_section_6 = dataInStream.readInt(); //读取4个字节的第六段长度	    				 
	    				 byte[] array_section_6 = new byte[lenth_section_6-4]; 
	    				 dataInStream.read(array_section_6, 0, lenth_section_6-4); //读取第六段的数据
	    				 
	    				 int lenth_section_7 = dataInStream.readInt(); //读取4个字节的第七段长度	    				 
	    				 byte[] array_section_7 = new byte[lenth_section_7-4]; 
	    				 dataInStream.read(array_section_7, 0, lenth_section_7-4); //读取第七段的数据
	    				 
	    				 //System.out.println("lenth_section_7:" + lenth_section_7);
	    				 //System.out.println("array_section_7.length:" + array_section_7.length);
	    				 
	    				 field_count++;
	    				 
		    			 
		    			 
		    			 if(dataOutStream!=null) //先关闭之前写的数据流
		    			 {
		    				 dataOutStream.flush();
		    				 dataOutStream.close();
		    			 }		    			 
		    			 
		    			 //original_filename:/CIMISS2/data/F/F.0011.0002.R001/201712/2017121515/A_HHXO25EDZW151200_C_BABJ_20171215153725_09209.bin
		    			 //field_filename:.../F.0011.0002.S001/20171215/A_HHXO25EDZW151200_C_BABJ_20171215153725_09209.bin.1
		    			 String original_filename_array[] = original_filename.split("/");
		    			 String l_dir = original_filename_array[original_filename_array.length-2]; //上一级目录，往往是时间目录
		    			 if(l_dir.length()>=8)
		    			 {
		    				 l_dir = l_dir.substring(0, 8);
		    			 }
		    			 String field_filepath = GribConfig.getPathNASFile() + "/field_file/" + d_data_id.replace("R001", "S001") + 
		    					 "/" + l_dir + "/";
		    			 String field_filename = field_filepath + original_filename_array[original_filename_array.length-1]+"."+field_count;
		    			 
		    			 //System.out.println("field_filepath:" + field_filepath);
		    			 //System.out.println("field_filename:" + field_filename);
		    			 
		    			 //如果目录不存在，建目录
		    			 File file_path = new File(field_filepath);
		    			 if(!file_path.exists())
		    			 {
		    				 file_path.mkdirs();
		    			 }
		    			 
		    			 dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(field_filename))); //单场文件名
	    				 
	    				 //输出第一个单场数据
	    				 dataOutStream.write(begin_a);
	    				 dataOutStream.write(begin_b);
	    				 dataOutStream.write(begin_c);
	    				 dataOutStream.write(begin_d);
	    				 dataOutStream.write(byte_5);
	    				 dataOutStream.write(byte_6);
	    				 dataOutStream.write(byte_7);
	    				 dataOutStream.write(version);
	    				 long field_lengh = 16 + lenth_section_1 + lenth_section_2 + lenth_section_3 + lenth_section_4 
	    						 + lenth_section_5 + lenth_section_6 + lenth_section_7 + 4; //单场长度：等于各段长度之和
	    				 dataOutStream.writeLong(field_lengh);
	    				 dataOutStream.writeInt(lenth_section_1);
	    				 dataOutStream.write(array_section_1,0,array_section_1.length);
	    				 
	    				 //第2段数据
	    				 if(exist_section_2)
	    				 {
	    					 dataOutStream.writeInt(lenth_section_2);
	    					 dataOutStream.write(number_section_2);
	    					 dataOutStream.write(array_section_2,0,array_section_2.length);
	    				 }
	    				 
	    				 //第3段数据
	    				 dataOutStream.writeInt(lenth_section_3);
	    				 dataOutStream.write(number_section_3);
	    				 dataOutStream.write(array_section_3,0,array_section_3.length);
	    				 
	    				 //第4段数据
	    				 dataOutStream.writeInt(lenth_section_4);
	    				 dataOutStream.write(array_section_4,0,array_section_4.length);
	    				 
	    				 //第5段数据
	    				 dataOutStream.writeInt(lenth_section_5);
	    				 dataOutStream.write(array_section_5,0,array_section_5.length);
	    				 
	    				 //第6段数据
	    				 dataOutStream.writeInt(lenth_section_6);
	    				 dataOutStream.write(array_section_6,0,array_section_6.length);
	    				 
	    				 //第7段数据
	    				 dataOutStream.writeInt(lenth_section_7);
	    				 dataOutStream.write(array_section_7,0,array_section_7.length);	 
	    				
	    				 
	    				 //第8段的“7777”
	    				 dataOutStream.write(0x37);
	    				 dataOutStream.write(0x37);
	    				 dataOutStream.write(0x37);
	    				 dataOutStream.write(0x37);
	    				 
	    				 if(dataOutStream!=null) //先关闭之前写的数据流
		    			 {
		    				 dataOutStream.flush();
		    				 dataOutStream.close();
		    			 }
	    				 
	    				 //处理这个单场文件
	    				 logger.info("split complete.");
	    				 //int ret = process_field_file(original_filename,field_filename,"2",d_data_id,rcv_time,cassadraOper,intoDb_fieldInfo);
	    				 String ret = process_field_file(original_filename,field_filename,"2",d_data_id,rcv_time,cassadraOper,m4cassadraOper,intoDb_fieldInfo,di);
	    				 logger.info("process_field_file complete, " + field_filename + ",ret="+ret);
	    				 String[] ret_array = ret.split("_"); //返回：解码成功场数_入Cassandra场数_入索引表场数，如：1_0_1
	    				 decodeSum = decodeSum + Integer.parseInt(ret_array[0]);
	    				 cassandraSum = cassandraSum + Integer.parseInt(ret_array[1]);
	    				 indexSum = indexSum + Integer.parseInt(ret_array[2]);	    				 
	    				 //ret_num = ret_num + ret;
	    				 
	    				 boolean first = true; //第一次进入循环时，得到循环的段号（从第几段开始循环）
	    				 int loop_number = 0; //循环的段号
	    				 while(true)
	    				 {
		    				 //再往前读4个字节
		    				 int forward_a = dataInStream.read();
		    				 int forward_b = dataInStream.read();
		    				 int forward_c = dataInStream.read();
		    				 int forward_d = dataInStream.read();
		    				 
		    				 //System.out.println("forward_a:" + forward_a);
		    				 //System.out.println("forward_b:" + forward_b);
		    				 //System.out.println("forward_c:" + forward_c);
		    				 //System.out.println("forward_d:" + forward_d);
		    				 
		    				 if((forward_a==0x37&&forward_b==0x37&&forward_c==0x37&&forward_d==0x37) //读到结尾“7777”，跳出循环结束
		    						 ||forward_a==-1||forward_b==-1||forward_c==-1||forward_d==-1) //或读到文件结束
		    				 {	
		    					 break;
		    				 }
		    				 else //没有结束，开始循环读取下一个场
		    				 {
		    					 int number;
		    					 
		    					 if(first)
		    					 {
			    					 number = dataInStream.read(); //得到循环的段号
			    					 loop_number = number;
			    					 //logger.info("循环起始段号number:" + number);
			    					 first = false;
		    					 }
		    					 else //找到下一个循环段号
		    					 {
		    						 number = dataInStream.read();
		    						 while(true)
		    						 {
		    							 if((forward_a==0x37&&forward_b==0x37&&forward_c==0x37&&forward_d==0x37) //读到结尾“7777”，跳出循环结束
		    		    						 ||forward_a==-1||forward_b==-1||forward_c==-1||forward_d==-1) //或读到文件结束
		    		    				 {	
		    		    					 break;
		    		    				 }
		    							 
		    							 if(number!=loop_number)
		    							 {
		    								//往下读一个字节		    								 
		    								 forward_d = number;
		    								 forward_c = forward_d;
		    								 forward_b = forward_c;
		    								 forward_a = forward_b;
		    								 number = dataInStream.read();
		    							 }
		    							 else
		    							 {
		    								 break;
		    							 }
		    						 }
		    					 }
		    					 
		    					 if(dataOutStream!=null) //先关闭之前写的数据流
				    			 {
				    				 dataOutStream.flush();
				    				 dataOutStream.close();
				    			 }
		    					 
		    					 if(number==loop_number) //找到一个新循环段
		    					 {
			    					 field_count++;
			    					 
					    			 //original_filename:/CIMISS2/data/F/F.0013.0001.R001/201712/2017121515/A_HHXO25EDZW151200_C_BABJ_20171215153725_09209.bin
					    			 //field_filename:.../F.0013.0001.S001/20171215/A_HHXO25EDZW151200_C_BABJ_20171215153725_09209.bin.1
			    					 l_dir = original_filename_array[original_filename_array.length-2];
			    	    			 if(l_dir.length()>=8)
			    	    			 {
			    	    				 l_dir = l_dir.substring(0, 8);
			    	    			 }
					    			 field_filepath =GribConfig.getPathNASFile() + "/field_file/" + d_data_id.replace("R001", "S001") + 
					    					 "/" + l_dir + "/";
					    			 field_filename = field_filepath + original_filename_array[original_filename_array.length-1]+"."+field_count;
					    			 
					    			 //System.out.println("field_filepath:" + field_filepath);
					    			 //System.out.println("field_filename:" + field_filename);
					    			 
					    			 //如果目录不存在，建目录
					    			 File file_path_2 = new File(field_filepath);
					    			 if(!file_path_2.exists())
					    			 {
					    				 file_path_2.mkdirs();
					    			 }
					    			 
					    			 dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(field_filename))); //单场文件名
					    			 
					    			 //写第0段数据
					    			 dataOutStream.write(begin_a);
				    				 dataOutStream.write(begin_b);
				    				 dataOutStream.write(begin_c);
				    				 dataOutStream.write(begin_d);
				    				 dataOutStream.write(byte_5);
				    				 dataOutStream.write(byte_6);
				    				 dataOutStream.write(byte_7);
				    				 dataOutStream.write(version);
			    					 
			    					 if(number==2) //从第2段开始循环：2段，3段，...，7段...2段，3段，...，7段
			    					 {
			    						 //logger.info("从第2段开始循环");
			    						 
			    						 //读取第二段数据
			    						 //段长由forward_a，forward_b，forward_c，forward_d决定
			    						 lenth_section_2 = (int)(forward_d*1 + forward_c*Math.pow(16, 2) + forward_b*Math.pow(16, 4) + forward_a*Math.pow(16, 6));
			    						 array_section_2 = new byte[lenth_section_2-5]; 
					    				 dataInStream.read(array_section_2, 0, lenth_section_2-5); //读取第二段的数据
					    				 
					    				 //读取第三段数据
					    				 lenth_section_3 = dataInStream.readInt(); //读取3个字节的第三段长度	
					    				 number_section_3 = dataInStream.read(); //读取第三段编号
					    				 array_section_3 = new byte[lenth_section_3-5]; 
					    				 dataInStream.read(array_section_3, 0, lenth_section_3-5); //读取第三段的数据
					    				 
					    				 lenth_section_4 = dataInStream.readInt(); //读取4个字节的第四段长度	    				 
					    				 array_section_4 = new byte[lenth_section_4-4]; 
					    				 dataInStream.read(array_section_4, 0, lenth_section_4-4); //读取第四段的数据
					    				 
					    				 lenth_section_5 = dataInStream.readInt(); //读取4个字节的第五段长度	    				 
					    				 array_section_5 = new byte[lenth_section_5-4]; 
					    				 dataInStream.read(array_section_5, 0, lenth_section_5-4); //读取第五段的数据
					    				 
					    				 lenth_section_6 = dataInStream.readInt(); //读取4个字节的第六段长度	    				 
					    				 array_section_6 = new byte[lenth_section_6-4]; 
					    				 dataInStream.read(array_section_6, 0, lenth_section_6-4); //读取第六段的数据
					    				 
					    				 lenth_section_7 = dataInStream.readInt(); //读取4个字节的第七段长度	    				 
					    				 array_section_7 = new byte[lenth_section_7-4]; 
					    				 dataInStream.read(array_section_7, 0, lenth_section_7-4); //读取第七段的数据
					    				 
					    				 //该场的总长度
					    				 field_lengh = 16 + lenth_section_1 + lenth_section_2 + lenth_section_3 + lenth_section_4 
					    						 + lenth_section_5 + lenth_section_6 + lenth_section_7 + 4; //单场长度：等于各段长度之和
					    				 
					    				 //写第1段的数据
					    				 dataOutStream.writeLong(field_lengh);
					    				 dataOutStream.writeInt(lenth_section_1);
					    				 dataOutStream.write(array_section_1,0,array_section_1.length);
					    				 
			    						 //写第2段的数据
			    						 dataOutStream.write(forward_a);
					    				 dataOutStream.write(forward_b);
					    				 dataOutStream.write(forward_c);
					    				 dataOutStream.write(forward_d);
					    				 dataOutStream.write(number);
					    				 dataOutStream.write(array_section_2,0,array_section_2.length);
					    				 
					    				 //写第3段的数据
					    				 dataOutStream.writeInt(lenth_section_3);
					    				 dataOutStream.write(number_section_3);
					    				 dataOutStream.write(array_section_3,0,array_section_3.length);
					    				 
					    				 //第4段数据
					    				 dataOutStream.writeInt(lenth_section_4);
					    				 dataOutStream.write(array_section_4,0,array_section_4.length);
					    				 
					    				 //第5段数据
					    				 dataOutStream.writeInt(lenth_section_5);
					    				 dataOutStream.write(array_section_5,0,array_section_5.length);
					    				 
					    				 //第6段数据
					    				 dataOutStream.writeInt(lenth_section_6);
					    				 dataOutStream.write(array_section_6,0,array_section_6.length);
					    				 
					    				 //第7段数据
					    				 dataOutStream.writeInt(lenth_section_7);
					    				 dataOutStream.write(array_section_7,0,array_section_7.length);	 
					    				
					    				 /*
			    						 long rest_length = (long)lenth_section_2 + (long)lenth_section_3 + lenth_section_4 + lenth_section_5 + lenth_section_6 + lenth_section_7;
			    						 for(long t=0;t<rest_length-5;t++) //读取剩余的字节
			    	    				 {
			    	    					 int byte_temp = dataInStream.read(); 
			    	    					 dataOutStream.write(byte_temp); //写完剩下的字节
			    	    				 } 
			    	    				 */
			    						 
			    						 //第8段的“7777”
			    	    				 dataOutStream.write(0x37);
			    	    				 dataOutStream.write(0x37);
			    	    				 dataOutStream.write(0x37);
			    	    				 dataOutStream.write(0x37);
			    	    				 
			    	    				 if(dataOutStream!=null) //先关闭之前写的数据流
						    			 {
						    				 dataOutStream.flush();
						    				 dataOutStream.close();
						    			 }	
			    						 
					    				 //处理这个单场文件
					    				 logger.info("split complete.");
					    				 ret = process_field_file(original_filename,field_filename,"2",d_data_id,rcv_time,cassadraOper,m4cassadraOper,intoDb_fieldInfo,di);
					    				 logger.info("process_field_file complete, " + field_filename + ",ret="+ret);
					    				 ret_array = ret.split("_"); //返回：解码成功场数_入Cassandra场数_入索引表场数，如：1_0_1
					    				 decodeSum = decodeSum + Integer.parseInt(ret_array[0]);
					    				 cassandraSum = cassandraSum + Integer.parseInt(ret_array[1]);
					    				 indexSum = indexSum + Integer.parseInt(ret_array[2]);					    				 
					    				 //ret_num = ret_num + ret;
			    					 }
			    					 else if(number==3) //从第3段开始循环：3段，...，7段...3段，...，7段
			    					 {
			    						 //logger.info("从第3段开始循环");
			    						 
			    						 //读取第三段数据
			    						 //段长由forward_a，forward_b，forward_c，forward_d决定			    						 
					    				 lenth_section_3 = (int)(forward_d*1 + forward_c*Math.pow(16, 2) + forward_b*Math.pow(16, 4) + forward_a*Math.pow(16, 6)); //读取4个字节的第三段长度	
					    				 array_section_3 = new byte[lenth_section_3-5]; 
					    				 dataInStream.read(array_section_3, 0, lenth_section_3-5); //读取第三段的数据
					    				 
					    				 lenth_section_4 = dataInStream.readInt(); //读取4个字节的第四段长度	    				 
					    				 array_section_4 = new byte[lenth_section_4-4]; 
					    				 dataInStream.read(array_section_4, 0, lenth_section_4-4); //读取第四段的数据
					    				 
					    				 lenth_section_5 = dataInStream.readInt(); //读取4个字节的第五段长度	    				 
					    				 array_section_5 = new byte[lenth_section_5-4]; 
					    				 dataInStream.read(array_section_5, 0, lenth_section_5-4); //读取第五段的数据
					    				 
					    				 lenth_section_6 = dataInStream.readInt(); //读取4个字节的第六段长度	    				 
					    				 array_section_6 = new byte[lenth_section_6-4]; 
					    				 dataInStream.read(array_section_6, 0, lenth_section_6-4); //读取第六段的数据
					    				 
					    				 lenth_section_7 = dataInStream.readInt(); //读取4个字节的第七段长度	    				 
					    				 array_section_7 = new byte[lenth_section_7-4]; 
					    				 dataInStream.read(array_section_7, 0, lenth_section_7-4); //读取第七段的数据
					    				 
					    				 //该场的总长度
					    				 field_lengh = 16 + lenth_section_1 + lenth_section_2 + lenth_section_3 + lenth_section_4 
					    						 + lenth_section_5 + lenth_section_6 + lenth_section_7 + 4; //单场长度：等于各段长度之和
					    				 
					    				 //写第1段的数据
					    				 dataOutStream.writeLong(field_lengh);
					    				 dataOutStream.writeInt(lenth_section_1);
					    				 dataOutStream.write(array_section_1,0,array_section_1.length);
					    				 
			    						 //写第2段的数据					    				
			    						 if(exist_section_2)
			    	    				 {
			    	    					 dataOutStream.writeInt(lenth_section_2);
			    	    					 dataOutStream.write(number_section_2);
			    	    					 dataOutStream.write(array_section_2,0,array_section_2.length);
			    	    				 }
			    						 
			    						 //写第3段的数据
			    						 dataOutStream.write(forward_a);
					    				 dataOutStream.write(forward_b);
					    				 dataOutStream.write(forward_c);
					    				 dataOutStream.write(forward_d);
					    				 dataOutStream.write(number);					    				 
					    				 dataOutStream.write(array_section_3,0,array_section_3.length);
					    				 
					    				 //第4段数据
					    				 dataOutStream.writeInt(lenth_section_4);
					    				 dataOutStream.write(array_section_4,0,array_section_4.length);
					    				 
					    				 //第5段数据
					    				 dataOutStream.writeInt(lenth_section_5);
					    				 dataOutStream.write(array_section_5,0,array_section_5.length);
					    				 
					    				 //第6段数据
					    				 dataOutStream.writeInt(lenth_section_6);
					    				 dataOutStream.write(array_section_6,0,array_section_6.length);
					    				 
					    				 //第7段数据
					    				 dataOutStream.writeInt(lenth_section_7);
					    				 dataOutStream.write(array_section_7,0,array_section_7.length);	 				    				 
			    						 
					    				 
					    				 //第8段的“7777”
					    				 dataOutStream.write(0x37);
					    				 dataOutStream.write(0x37);
					    				 dataOutStream.write(0x37);
					    				 dataOutStream.write(0x37);
					    				 
					    				 if(dataOutStream!=null) //先关闭之前写的数据流
						    			 {
						    				 dataOutStream.flush();
						    				 dataOutStream.close();
						    			 }	
			    						 
					    				 //处理这个单场文件
					    				 logger.info("split complete.");
					    				 ret = process_field_file(original_filename,field_filename,"2",d_data_id,rcv_time,cassadraOper,m4cassadraOper,intoDb_fieldInfo,di);
					    				 logger.info("process_field_file complete, " + field_filename + ",ret="+ret);
					    				 ret_array = ret.split("_"); //返回：解码成功场数_入Cassandra场数_入索引表场数，如：1_0_1
					    				 decodeSum = decodeSum + Integer.parseInt(ret_array[0]);
					    				 cassandraSum = cassandraSum + Integer.parseInt(ret_array[1]);
					    				 indexSum = indexSum + Integer.parseInt(ret_array[2]);					    				 
					    				 //ret_num = ret_num + ret;
			    					 }
			    					 else if(number==4) //从第4段开始循环：4段，...，7段...4段，...，7段
			    					 {
			    						 //logger.info("从第4段开始循环");
			    						 //读取第4段数据
			    						 //段长由forward_a，forward_b，forward_c，forward_d决定			    						 
					    				 lenth_section_4 = (int)(forward_d*1 + forward_c*Math.pow(16, 2) + forward_b*Math.pow(16, 4) + forward_a*Math.pow(16, 6)); //读取4个字节的第四段长度	    				 
					    				 array_section_4 = new byte[lenth_section_4-5]; 
					    				 dataInStream.read(array_section_4, 0, lenth_section_4-5); //读取第四段的数据
					    				 
					    				 lenth_section_5 = dataInStream.readInt(); //读取4个字节的第五段长度	    				 
					    				 array_section_5 = new byte[lenth_section_5-4]; 
					    				 dataInStream.read(array_section_5, 0, lenth_section_5-4); //读取第五段的数据
					    				 
					    				 lenth_section_6 = dataInStream.readInt(); //读取4个字节的第六段长度	    				 
					    				 array_section_6 = new byte[lenth_section_6-4]; 
					    				 dataInStream.read(array_section_6, 0, lenth_section_6-4); //读取第六段的数据
					    				 
					    				 lenth_section_7 = dataInStream.readInt(); //读取4个字节的第七段长度	    				 
					    				 array_section_7 = new byte[lenth_section_7-4]; 
					    				 dataInStream.read(array_section_7, 0, lenth_section_7-4); //读取第七段的数据
					    				 
					    				 //该场的总长度
					    				 field_lengh = 16 + lenth_section_1 + lenth_section_2 + lenth_section_3 + lenth_section_4 
					    						 + lenth_section_5 + lenth_section_6 + lenth_section_7 + 4; //单场长度：等于各段长度之和
					    				 
					    				 //写第1段的数据
					    				 dataOutStream.writeLong(field_lengh);
					    				 dataOutStream.writeInt(lenth_section_1);
					    				 dataOutStream.write(array_section_1,0,array_section_1.length);
					    				 
			    						 //写第2段的数据					    				
			    						 if(exist_section_2)
			    	    				 {
			    	    					 dataOutStream.writeInt(lenth_section_2);
			    	    					 dataOutStream.write(number_section_2);
			    	    					 dataOutStream.write(array_section_2,0,array_section_2.length);
			    	    				 }
			    						 
			    						 //写第3段的数据
			    						 dataOutStream.writeInt(lenth_section_3);
					    				 dataOutStream.write(number_section_3);
					    				 dataOutStream.write(array_section_3,0,array_section_3.length);
			    						 
					    				 //第4段数据
			    						 dataOutStream.write(forward_a);
					    				 dataOutStream.write(forward_b);
					    				 dataOutStream.write(forward_c);
					    				 dataOutStream.write(forward_d);
					    				 dataOutStream.write(number);	
					    				 dataOutStream.write(array_section_4,0,array_section_4.length);
					    				 
					    				 //第5段数据
					    				 dataOutStream.writeInt(lenth_section_5);
					    				 dataOutStream.write(array_section_5,0,array_section_5.length);
					    				 
					    				 //第6段数据
					    				 dataOutStream.writeInt(lenth_section_6);
					    				 dataOutStream.write(array_section_6,0,array_section_6.length);
					    				 
					    				 //第7段数据
					    				 dataOutStream.writeInt(lenth_section_7);
					    				 dataOutStream.write(array_section_7,0,array_section_7.length);	 					    				 
					    				 
					    				 //第8段的“7777”
					    				 dataOutStream.write(0x37);
					    				 dataOutStream.write(0x37);
					    				 dataOutStream.write(0x37);
					    				 dataOutStream.write(0x37);
					    				 
					    				 if(dataOutStream!=null) //先关闭之前写的数据流
						    			 {
						    				 dataOutStream.flush();
						    				 dataOutStream.close();
						    			 }	
			    						 
					    				 //处理这个单场文件
					    				 logger.info("split complete.");
					    				 ret = process_field_file(original_filename,field_filename,"2",d_data_id,rcv_time,cassadraOper,m4cassadraOper,intoDb_fieldInfo,di);
					    				 logger.info("process_field_file complete, " + field_filename + ",ret="+ret);
					    				 ret_array = ret.split("_"); //返回：解码成功场数_入Cassandra场数_入索引表场数，如：1_0_1
					    				 decodeSum = decodeSum + Integer.parseInt(ret_array[0]);
					    				 cassandraSum = cassandraSum + Integer.parseInt(ret_array[1]);
					    				 indexSum = indexSum + Integer.parseInt(ret_array[2]);					    				 
					    				 //ret_num = ret_num + ret;
			    					 }
		    					 }
		    				 }
	    				 }
	    				 
	    			 }
	    		 }
	    		 
	    		//往下读一个字节
	    		begin_a = begin_b;
	    		begin_b = begin_c;
	    		begin_c = begin_d;
	    		begin_d = dataInStream.read();	 
	    	 }
	       
	    	 //System.out.println("");	    	
	    	 	    	
	     } 
	     catch (IOException ex1) 
	     { 
	    	 //ret_num = -1;
	    	 fileError = -1;
	    	 
	    	 //ex1.printStackTrace(); 
	    	 String log_data = "读取文件失败，filename=" + original_filename + "," + getSysTime() + ",Exception:" + ex1.getMessage();
	    	 logger_e.error(log_data);
	    	 String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
	    	 GribConfig.write_log_to_file(error_log_file, log_data);
	     } 
	     finally
	     { 
	    	 try 
	    	 { 	    		 
	    		 if(dataInStream!=null)
	    		 {	    	
	    			 dataInStream.close(); 
	    		 }	    		 
	    		 if(dataOutStream!=null)
	    		 {
	    			 dataOutStream.flush();
	    			 dataOutStream.close(); 
	    		 }
	    	 } 
	    	 catch (IOException ex2) 
	    	 { 
	    		 ex2.printStackTrace(); 
	    	 } 
	     }
	     
	     //return ret_num;
	     String ret_str = decodeSum + "_" + cassandraSum + "_" + indexSum + "_" + fileError;
	     //logger.info("split_grib_record_other ret_str:" + ret_str);
	     return ret_str;
	}
	
	//处理拆分后的单场文件
	//解析单场文件——〉写NAS大文件——〉写索引表
	//communi_source_filename:通信发过来的原始文件名
	//field_filename:拆出的单场文件名
	//gribversion:grib版本号
	//d_data_id:四级编码
	//rcv_time:消息收到时间
	//cassadraOper:入cassadraOper的对象
	//intoDb_fieldInfo:是否入单场信息表，如：nafp_ecmf_for_ftm_file_tab
	//解码和入库：0：失败  1：成功
	//返回：解码成功场数_入Cassandra场数_入索引表场数
	public String process_field_file(String communi_source_filename,String field_filename,
	        String gribversion,String d_data_id,Date rcv_time,CassandraDbOperation cassadraOper,CassandraM4DbOperation m4cassadraOper,
	        boolean intoDb_fieldInfo,boolean di)
	{
		logger.info("处理场文件:"+field_filename);
		
		Date start_time = new Date(); //场文件开始处理时间	
		
		int ret = 1;
		
		//modify,2019.7.11
		int decodeSum = 0; //解码成功的场数
	    int cassandraSum = 0; //入Cassandra成功的场数
	    int indexSum = 0; //入索引表成功的场数
		
		Map<String,List<Grib_Struct_Data>> map_grib_decode_result = new HashMap<String,List<Grib_Struct_Data>>();
		
		//解析单场文件
		if(gribversion.compareToIgnoreCase("1")==0) //grib1文件
		{			
			
			map_grib_decode_result.clear();
			map_grib_decode_result = read_grib1_data(field_filename,d_data_id,rcv_time);			
			
		}
		else if(gribversion.compareToIgnoreCase("2")==0) //grib2文件
		{
			map_grib_decode_result.clear();
			map_grib_decode_result = read_grib2_data(field_filename,d_data_id,rcv_time);
		}
		
		if(map_grib_decode_result.size()==0) //如果单场文件解码结果为空，输出错误
		{
			//记录此单场文件
			String log_data = "单场文件" + field_filename + ",解码结果为空," + getSysTime();
			logger_e.error(log_data);
			
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KEvent = "模式文件无法解码：" + field_filename;
				sendEiProcess.process_ei("OP_DPC_A_1_41_02", KEvent, "", d_data_id);
			}
			
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);
			//ret = 0;
			//return ret;
			String ret_str = decodeSum + "_" + cassandraSum + "_" + indexSum;
			//logger.info("process_field_file ret_str:" + ret_str);
			return ret_str;
		}
		
		//解码成功数+1
		decodeSum++;
		
		//在拆分完后单场，如果传入cassadraOper不为null，则入Cassandra
		int ret_cassandra = 0;
		if(cassadraOper!=null)
		{
			ret_cassandra = cassadraOper.into_cassandra_map(map_grib_decode_result);			
		}
		
		//在拆分完后单场，如果传入m4cassadraOper不为null，且该资料需要入M4Cassandra，则入M4Cassandra
		int ret_cassandraM4 = 0;
		DataAttr M4_dataAttr = (DataAttr) GribM4Config.get_map_data_description().get(d_data_id);
		if(m4cassadraOper!=null&&M4_dataAttr!=null&&M4_dataAttr.getM4Cassandra())
		{
			ret_cassandraM4 = m4cassadraOper.into_m4cassandra_map(map_grib_decode_result);			
		}
		
		cassandraSum = cassandraSum + ret_cassandra;
		
		//写NAS大文件		
		boolean nas_file_exit = true;
		for (String area_fieldType_genProcessType : map_grib_decode_result.keySet()) 
		{ 	  
			List<Grib_Struct_Data> grib_record_list = map_grib_decode_result.get(area_fieldType_genProcessType);
			for(int i=0;i<grib_record_list.size();i++)  //
			{
				Grib_Struct_Data grib_struc_data = grib_record_list.get(i);					
				
				String nas_file_name = grib_struc_data.getD_STORAGE_SITE();
				
				//判断nas文件是否存在
				File f_nasfile = new File(nas_file_name);
				if(!f_nasfile.exists())
				{
					//不存在，表示第一个收到的场文件，设置其收到时间
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String D_RYMDHM = dateFormat.format(rcv_time); 
					grib_struc_data.setD_RYMDHM(D_RYMDHM);
					
					nas_file_exit = false; 
					
				}					
				
				//写NAS大文件
				int write_nas = append_file_to_antherfile(field_filename,nas_file_name,grib_struc_data.getDATETIME(),grib_struc_data.getdata_id());
				
				//写nas文件成功，则写索引表
				if(write_nas==1)
				{
					File f_nasfile_new = new File(nas_file_name);
					if(f_nasfile_new!=null)
					{							
						grib_struc_data.setD_FILE_SIZE(f_nasfile_new.length()); //设置新的nas文件大小
					}
					logger.info("D_FILE_SIZE:" + grib_struc_data.getD_FILE_SIZE() + "," +nas_file_name); //add:2020.4.12
					grib_struc_data.setV_FILE_NAME_SOURCE(communi_source_filename); //设置通信原始文件名
					grib_struc_data.setV_FIELD_FILE_NAME_SOURCE(field_filename); //设置拆分后单场文件名
					
					DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
					String nas_table_name = "nafp_" + data_attributes.gettable_id() + "_for_ftm_file_k_tab"; //nas文件索引表
					String field_table_name = "nafp_" + data_attributes.gettable_id() + "_for_ftm_file_tab"; //field场文件索引表
					
					//logger.info("nas_table_name=" + nas_table_name + ",field_table_nmae=" + field_table_name);
					
					//写索引表
					//首先判断
					//logger.info("nas_file_exit:"+nas_file_exit);
					//logger.info("grib_struc_data.getD_FILE_ID:"+grib_struc_data.getD_FILE_ID());
					
					IndexDbOperation indexDbOper = new IndexDbOperation(nas_table_name,field_table_name);					
					try
					{
						indexDbOper.start("cmadass_db");
						boolean FILE_ID_exist = indexDbOper.exist_FILE_ID(grib_struc_data);
						//logger.info("FILE_ID_exist:" + FILE_ID_exist);
						
						//insert or update大nas索引表
						try
						{
							if(!FILE_ID_exist) //FILE_ID不存在，insert大nas索引表
							{
								//logger.info("insert index into");
								ret = indexDbOper.insertIndexInfo_nasInfo(grib_struc_data);
							}
							else //FILE_ID存在，update大nas索引表
							{
								//logger.info("update index into");
								ret = indexDbOper.UpdateIndexInfo_nafInfo(grib_struc_data);
							}
						}
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
							String log_data = "error insert or update " + " to " + nas_table_name + "," + getSysTime() + ",Exception:" + e.getMessage();
							logger_e.error(log_data);
							String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
							GribConfig.write_log_to_file(error_log_file, log_data);
							
							ret = 0;
						}
						
						//insert场文件filed索引表
						try
						{
							if(intoDb_fieldInfo) //如果intoDb_fieldInfo为true，入单场信息表
							{
								//logger.info("insert field into");
								indexDbOper.insertIndexInfo_fieldInfo(grib_struc_data);
							}
							
						}
						catch (Exception e) 
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
							String log_data = "error insert " + " to " + field_table_name + "," + getSysTime() + ",Exception:" + e.getMessage();
							logger_e.error(log_data);
							String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
							GribConfig.write_log_to_file(error_log_file, log_data);
						}
					}
					catch (Exception e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
						String log_data = "error connect " + " to cmadass_db " + getSysTime() + ",Exception:" + e.getMessage();
						logger_e.error(log_data);
						String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
						GribConfig.write_log_to_file(error_log_file, log_data);
					}
					finally
					{
						indexDbOper.close();
					}
				}					
				else //写NAS文件失败
				{
					ret = 0;
				}
				
			
				/*
				if(ret_cassandra==0) //如果入Cassandra失败，也返回失败
				{
					ret = 0;
				}
				*/
				
				indexSum = indexSum + ret;
				
				Date end_time = new Date(); //场文件处理结束时间
				
				grib_struc_data.setrcv_time(rcv_time);					
				
				grib_struc_data.setstart_time(start_time);
				
				grib_struc_data.setend_time(end_time);
				
				grib_struc_data.setinsert_ret(ret);	//入索引表返回值
				
				grib_struc_data.setinsert_radb_ret(ret_cassandra); //入实时库返回值
				
				//如果是实时流程，且配置了发DI，使用单独的线程池来入Rest DI
				if(di&&GribConfig.getRestfulDi().equalsIgnoreCase("1"))
				{
					//发送入实时库（Cassandra）di
					if(cassadraOper!=null) 
					{
						logger.info("发送入radb的di");
						SendDiProcess sendDiProcess = SendDiProcess.getInstance();
						sendDiProcess.process_di(grib_struc_data,"radb");
					}
					
					//发送入索引库di
					logger.info("发送入fidb的di");
					SendDiProcess sendDiProcess = SendDiProcess.getInstance();
					sendDiProcess.process_di(grib_struc_data,"fidb");
				}
			
			}
		    
		}
		
		//return ret;
		String ret_str = decodeSum + "_" + cassandraSum + "_" + indexSum;
		//logger.info("process_field_file ret_str:" + ret_str);
		return ret_str;
	}
	
	//处理智能网格类模式数据	
	/**
	 * 将网格文件进行解码入库
	 * @param filename 文件名
	 * @param d_data_id 四级编码
	 * @param rcv_time 接收时间
	 * @return 解码入库是否成功：解码成功数_入库成功数。
	 *         1：成功，0：失败
	 */
	public String process_nwfd_file(String filename,String d_data_id,Date rcv_time)
	{
		//解析
		List<Grib_Struct_Data> list_grib_struct_data = null;
		try 
		{
			list_grib_struct_data = copy_and_parse_nwfd_filename(filename,d_data_id,rcv_time);
		} 
		catch (Exception e1) 
		{
			// TODO Auto-generated catch block
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KEvent = "网格文件解析出错：" + filename + ",Exception:" + e1.getMessage();
				sendEiProcess.process_ei("OP_DPC_A_1_41_02", KEvent, "", d_data_id);
			}
			
			e1.printStackTrace();
			String log_data = "网格文件" + filename + ",解析出错,"  + getSysTime() + ",Exception:" + e1.getMessage();
			logger_e.error(log_data);
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);
			
			return "0_0";
		}
		
		int ret_decode = 1;		
		if(list_grib_struct_data==null||list_grib_struct_data.size()==0) //如果解码结果为空，输出错误
		{
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KEvent = "网格文件解析出错：" + filename;
				sendEiProcess.process_ei("OP_DPC_A_1_41_02", KEvent, "", d_data_id);
			}
			
			//记录此文件
			String log_data = "网格文件" + filename + ",解码结果为空," + getSysTime();
			logger_e.error(log_data);
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);
			ret_decode = 0;
			
			return "0_0";
		}
		
		//入库
		int ret_index = 0;
		for(int i=0;i<list_grib_struct_data.size();i++)
		{
			Grib_Struct_Data grib_struc_data =list_grib_struct_data.get(i);
			
			IndexDbOperation indexDbOper = new IndexDbOperation("","");					
			try
			{
				indexDbOper.start("cmadass_db");	 			
				
				logger.info("insert index into");
				ret_index = indexDbOper.Insert_Nwfd_Index(grib_struc_data);
			}
			catch (Exception e) 
			{
				ret_index = 0;
				// TODO Auto-generated catch block
				e.printStackTrace();
				String log_data = "error access " + " to cmadass_db " + getSysTime() + ",Exception:" + e.getMessage();
				logger_e.error(log_data);
				String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
				GribConfig.write_log_to_file(error_log_file, log_data);
			}
			finally
			{
				indexDbOper.close();
			}
		}
		
		return ret_decode+"_"+ret_index;
	}
	
	//解析智能网格类模式文件名
	private List<Grib_Struct_Data> copy_and_parse_nwfd_filename(String filename,String d_data_id,Date rcv_time) throws Exception
	{
		List<Grib_Struct_Data> grib_record_list = new ArrayList<Grib_Struct_Data>();	
		grib_record_list.clear();
		
		//拷贝文件到产品目录
		DataAttr data_attributes = GribConfig.get_map_data_description().get(d_data_id);
		String data_dir = data_attributes.getdata_dir();
		String original_filename_array[] = filename.split("/");
		String pure_name = original_filename_array[original_filename_array.length-1];
		String pure_name_1 = pure_name.replace("-", "_");
		String pure_name_array[] = pure_name_1.split("_");
		//logger.info("time_index_of_filenam:" + data_attributes.gettime_index_of_filename());
		String day_dir = pure_name_array[data_attributes.gettime_index_of_filename()-1].substring(0, 8);
		//获取四位省级编码,如果pure_name用“.”分成三组，则第三组为四位省级编码，否则截取文件名中CCCC字段作为省级编码
		//logger.info("pure_name:"+pure_name);
		String pure_name_arry1[] = pure_name.split("\\."); //"."是特殊字符，需要转义
		String cccc = "";
		//logger.info("pure_name_arry1.length="+pure_name_arry1.length);
		if(pure_name_arry1.length == 3)
		{
			cccc = pure_name_arry1[pure_name_arry1.length-1];
			pure_name = pure_name.substring(0, pure_name.lastIndexOf("."));
		}
		else{
		 cccc = pure_name_array[3];
		}		
		//logger.info("cccc="+cccc);
		logger.info("pure_name"+pure_name);
        
		//String target_filename = GribConfig.getPathNASFile() + "/" + data_dir + "/" + day_dir + "/" + pure_name;
		//nas存储路径模型为：/CMADAAS/DATA/NAFP//NWFD/SCMOC/BABJ/2020/20200120/Z_NWGD_C_BABJ_20200120044108_P_RFFC_SCMOC-FOG_202001200800_07203.GRB2
		String target_filename = GribConfig.getPathNASFile() + "/" + data_dir + "/"+cccc+"/" + day_dir.substring(0,4) + "/" +day_dir+"/"+ pure_name;
		logger.info("target_filename:" +target_filename);
		int write_nas = copy_file_to_antherfile(filename,target_filename,d_data_id);
		logger.info("写入日期目录状态write_nas："+write_nas);
		//LATEST 目录
		String latest_file_dir = GribConfig.getPathNASFile() + "/" + data_dir + "/"+cccc+"/"+LATEST_DIR;
		//将文件写入
		int write_latest_nas1=copy_file_to_latest_dir(filename,latest_file_dir,d_data_id,pure_name);
		logger.info("写入LATEST目录状态write_latest_nas1："+write_latest_nas1);
		//按文件名规则，解析文件名
		if(write_nas==1)
		{
			Grib_Struct_Data grib_struct_data = new Grib_Struct_Data();	
			
			grib_struct_data.setdata_id(d_data_id); //四级编码
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String D_RYMDHM = dateFormat.format(rcv_time);
			grib_struct_data.setD_RYMDHM(D_RYMDHM);
			grib_struct_data.setD_FILE_ID(pure_name);
			grib_struct_data.setD_SOURCE_ID(d_data_id);
			grib_struct_data.setD_STORAGE_SITE(target_filename); //存储路径

			grib_struct_data.setV_FILE_NAME(pure_name); //文件名 
			//System.out.println("pure_name:" +pure_name);
            String suffix = "";
            if(pure_name_arry1.length == 3)
            {
                suffix = pure_name_arry1[pure_name_arry1.length-2];
            }
            else{
                suffix = pure_name.substring(pure_name.lastIndexOf(".")+1);
            }

			grib_struct_data.setV_FILE_FORMAT(suffix); //后缀
			
			File f_nasfile_new = new File(target_filename);
			if(f_nasfile_new!=null)
			{							
				grib_struct_data.setD_FILE_SIZE(f_nasfile_new.length()); //文件大小
			}
			
			
			//文件名匹配规则：Z_NWGD_C_[cccc]_[maketime]_P_[area]_[prodSort]_[prodContent]_[datetime]_[3-v04320,2-internal].GRB2
			String filename_match = data_attributes.getfilename_match(); 
			//Z_NWGD_C_BABJ_20190219043416_P_RFFC_SCMOC-FOG_201902190800_07203.GRB2
			pure_name = pure_name.replace("-", "_"); 
			pure_name = pure_name.substring(0, pure_name.lastIndexOf("."));
			
			String filename_match_array[] = filename_match.split("_");
			String pure_name_array2[] = pure_name.split("_");
			
			for(int i=0;i<filename_match_array.length;i++)
			{		
				String match1 = filename_match_array[i];
				if(match1.startsWith("[")) 
				{
					if(match1.contains(",")) //如：[3-v04320,2-internal]
					{
						match1 = match1.substring(1, match1.length()-1);
						String match1_array[] = match1.split(",");
						int st = 0;
						int ed = 0;
						for(int k=0;k<match1_array.length;k++)
						{
							String array1[] = match1_array[k].split("-"); //如：3-v04320
							
							st = ed;
							ed = ed + Integer.parseInt(array1[0]);
							
							if(array1[1].contains("v04320"))
							{
								grib_struct_data.setv04320_c(pure_name_array2[i].substring(st, ed));
							}
							else if(array1[1].contains("internal"))
							{
								grib_struct_data.setinternal(pure_name_array2[i].substring(st, ed));
							}
							
						}
					}
					else //如：[cccc]
					{
						if(match1.contains("cccc"))
						{
							grib_struct_data.setv_cccc(pure_name_array2[i]);
						}
						else if(match1.contains("maketime"))
						{
							grib_struct_data.setmaketime(pure_name_array2[i]);
						}
						else if(match1.contains("area"))
						{
							grib_struct_data.setV_AREACODE(pure_name_array2[i]);
						}						
						else if(match1.contains("density"))
						{
							grib_struct_data.setdensity(pure_name_array2[i]);
						}
						else if(match1.contains("prodSort"))
						{
							grib_struct_data.setprodSort(pure_name_array2[i]);
						}
						else if(match1.contains("prodContent"))
						{
							grib_struct_data.setprodContent(pure_name_array2[i]);
						}
						else if(match1.contains("prodSystem"))
						{
							grib_struct_data.setV_PROD_SYSTEM(pure_name_array2[i]);
						}
						else if(match1.contains("datetime"))
						{
							grib_struct_data.setfntime(pure_name_array2[i]);
							
							String DATETIME = pure_name_array2[i].substring(0, 4) + "-" + pure_name_array2[i].substring(4,6) + "-" 
										+ pure_name_array2[i].substring(6,8) + " " + pure_name_array2[i].substring(8,10) + ":00:00";
							grib_struct_data.setDATETIME(DATETIME);
						}
						else if(match1.contains("v04320"))
						{
							grib_struct_data.setv04320_c(pure_name_array2[i]);
						}
						else if(match1.contains("internal"))
						{
							grib_struct_data.setinternal(pure_name_array2[i]);
						}
						else if(match1.contains("retain1"))
						{
							grib_struct_data.setretain1(pure_name_array2[i]);
						}
						else if(match1.contains("retain2"))
						{
							grib_struct_data.setretain2(pure_name_array2[i]);
						}
						else if(match1.contains("retain3"))
						{
							grib_struct_data.setretain3(pure_name_array2[i]);
						}
					}
				}
			}
			grib_struct_data.setretain1(cccc); //add,2020.5.29:retain1字段赋值分省cccc或BABJ（国家级）
			grib_record_list.add(grib_struct_data);
			
		}
		
		return grib_record_list;
	}
	
	//将一个文件写到另一个文件的末尾，二进制文件
	//返回1：成功，0：失败
	private int append_file_to_antherfile(String filed_filename,String target_filename, String DATETIME, String data_id)
	{
		DataInputStream dataInStream = null;  //读数据的输入流
		DataOutputStream dataOutStream = null; //写数据的输出流
		
		FileChannel wfilechannel = null;
		FileLock wfilelock = null;
		
		FileOutputStream fileOutStream = null;
		
		int ret = 0;
		try
		{
			dataInStream = new DataInputStream(new BufferedInputStream(new FileInputStream(filed_filename))); 
			
			logger.info("target_filename=" + target_filename);
			File file_path = new File(target_filename);
			logger.info("file_path.getParent=" + file_path.getParent());
			if(!file_path.getParentFile().exists()) //如果目录不存在，创建目录
			{
				file_path.getParentFile().mkdirs();
			}
			
			//add:2020.4.11：对写文件加锁，防止其它线程同时写，独占锁
			//modify:2020.4.12：先等待加锁，再读数据
			fileOutStream = new FileOutputStream(target_filename,true); //追加到文件末尾
			/*
			wfilechannel = fileOutStream.getChannel();
			wfilelock = wfilechannel.lock(); //写文件加锁	
			while(wfilelock==null)
			{
				logger.info("wfilelock==null," +"filed_filename="+filed_filename+",target_filename="+target_filename+"," + getSysTime() );
				try 
				{
					wfilelock = wfilechannel.lock();
				}
				catch (Exception e) 
				{
					e.printStackTrace(); 
					String log_data = "加文件锁失败,filed_filename="+filed_filename+",target_filename="+target_filename+"," + getSysTime() + ",Exception:" + e.getMessage();
					logger_e.error(log_data);
				}
			}
			logger.info("write file lock," + target_filename);
			*/
	
			dataOutStream = new DataOutputStream(new BufferedOutputStream(fileOutStream)); //追加到文件末尾
			
			int size = dataInStream.available();
			byte[] all_data = new byte[size];
			dataInStream.read(all_data, 0, size);
			
			dataOutStream.write(all_data,0,size);
			
			ret = 1;
		}
		catch (IOException e) 
	    { 
			e.printStackTrace(); 
			String log_data = "写NAS文件失败,filed_filename="+filed_filename+",target_filename="+target_filename+"," + getSysTime() + ",Exception:" + e.getMessage();
			logger_e.error(log_data);
			
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KEvent = "写NAS文件失败：" + target_filename + "," + e.getMessage();
				sendEiProcess.process_ei("OP_DPC_A_2_33_07", KEvent, DATETIME, data_id);
			}
			
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);
			ret = 0;
	    } 
		finally
	    { 
			try 
	    	{	
				//关闭读写流
				if(dataInStream!=null)
	    		{	
					dataInStream.close(); 
	    		}
				
	    		if(dataOutStream!=null)
	    		{
	    			dataOutStream.flush();
	    			dataOutStream.close(); 
	    		}
	    		
	    	} 
	    	catch (IOException ex2) 
	    	{ 
	    		ex2.printStackTrace(); 
	    		String log_data = "关闭读写流失败,filed_filename="+filed_filename+",target_filename="+target_filename+"," + getSysTime() + ",Exception:" + ex2.getMessage();
				logger_e.error(log_data);	    		
	    	} 
			
			/*
			try 
	    	{ 	 
				//释放文件锁
	    		if(wfilelock!=null)
	    		{
	    			//wfilelock.release();
	    			//wfilelock.close();
	    		}
	    		
	    		if(wfilechannel!=null)
	    		{
	    			wfilechannel.close();	    			
	    		}	
	    		
	    		logger.info("release write file lock," + target_filename); 
	    		
	    	} 
	    	catch (IOException ex2) 
	    	{ 
	    		ex2.printStackTrace(); 
	    		String log_data = "释放文件锁失败,filed_filename="+filed_filename+",target_filename="+target_filename+"," + getSysTime() + ",Exception:" + ex2.getMessage();
				logger_e.error(log_data);	    		
	    	} 
	    	*/
			
			try 
	    	{	
				//关闭文件流				
	    		if(fileOutStream!=null)
	    		{
	    			fileOutStream.flush();
	    			fileOutStream.close(); 
	    		}
	    		
	    	} 
	    	catch (IOException ex2) 
	    	{ 
	    		ex2.printStackTrace(); 
	    		String log_data = "关闭文件流失败,filed_filename="+filed_filename+",target_filename="+target_filename+"," + getSysTime() + ",Exception:" + ex2.getMessage();
				logger_e.error(log_data);	    		
	    	} 
			
	     }
		
		return ret;
	}
	
	//将一个文件拷贝到另一个文件，覆盖文件内容，二进制文件
	//返回1：成功，0：失败
	private int copy_file_to_antherfile(String source_filename,String target_filename,String data_id)
	{
		DataInputStream dataInStream = null;  //读数据的输入流
		DataOutputStream dataOutStream = null; //写数据的输出流
		int ret = 0;
		try
		{
			dataInStream = new DataInputStream(new BufferedInputStream(new FileInputStream(source_filename))); 
			int size = dataInStream.available();
			byte[] all_data = new byte[size];
			dataInStream.read(all_data, 0, size);
			
			//logger.info("target_filename=" + target_filename);
			File file_path = new File(target_filename);
			//logger.info("file_path.getParent=" + file_path.getParent());
			if(!file_path.getParentFile().exists()) //如果目录不存在，创建目录
			{
				file_path.getParentFile().mkdirs();
			}
			
			dataOutStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(target_filename,false))); //覆盖到新文件
			dataOutStream.write(all_data,0,size);
			
			ret = 1;
		}
		catch (IOException e) 
	    { 
			e.printStackTrace(); 
			String log_data = "写NAS文件失败,filed_filename="+source_filename+",target_filename="+target_filename+"," + getSysTime() + ",Exception:" + e.getMessage();
			logger_e.error(log_data);
			
			//使用单独的线程池来入Rest EI
			if(GribConfig.getRestfulEi().equalsIgnoreCase("1"))
			{
				SendEiProcess sendEiProcess = SendEiProcess.getInstance();
				String KEvent = "写NAS文件失败：" + target_filename + "," + e.getMessage();
				sendEiProcess.process_ei("OP_DPC_A_2_33_07", KEvent, "", data_id);
			}			
			
			String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
			GribConfig.write_log_to_file(error_log_file, log_data);
			ret = 0;
	    } 
		finally
	    { 
			try 
	    	{ 	    		 
				if(dataInStream!=null)
	    		{	    	
					dataInStream.close(); 
	    		}	    		 
	    		if(dataOutStream!=null)
	    		{
	    			dataOutStream.flush();
	    			dataOutStream.close(); 
	    		}
	    	} 
	    	catch (IOException ex2) 
	    	{ 
	    		ex2.printStackTrace(); 
	    	} 
	     }
		
		return ret;
	}
	
	//将欧洲模式资料从复杂压缩转化为简单压缩
	//absoluteFileName：源文件
	//转化后的结果覆盖源文件
	//返回0：表示成功
	private int transfer_to_grid_simple(String absoluteFileName)
	{
		//复杂压缩转简单压缩命令
		String cmd_transfer = String.format("grib_set -r -s packingType=grid_simple %s %s", absoluteFileName,absoluteFileName);
		logger.info("cmd_transfer=" + cmd_transfer);
		
		Process process = null;
		Runtime runtime = Runtime.getRuntime();
		int result_transfer_grid_simple = 1;
		try 
		{			
			process = runtime.exec(cmd_transfer);
					
			// 采用字符流读取缓冲池内容，腾出空间
			steamout(process.getErrorStream(),absoluteFileName,"error_transfer_grid_simple");
			steamout(process.getInputStream(),absoluteFileName,"output_transfer_grid_simple");
					
			result_transfer_grid_simple = process.waitFor();			
				
		} 
		catch (InterruptedException e)
		{
			e.printStackTrace();
			
			String log_data = "transfer_to_grid_simple函数异常,filename="+absoluteFileName + "," + getSysTime()+",Exception:" + e.getMessage();
	        logger_e.error(log_data);
	        String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
	        GribConfig.write_log_to_file(error_log_file, log_data);
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			String log_data = "transfer_to_grid_simple函数异常,filename="+absoluteFileName + "," + getSysTime()+",Exception:" + e.getMessage();
	        logger_e.error(log_data);
	        String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
	        GribConfig.write_log_to_file(error_log_file, log_data);
				
		}
		
		logger.info("result_transfer_grid_simple=" + result_transfer_grid_simple); 
		return result_transfer_grid_simple;
	}
	
	//输出流
	private void steamout(InputStream is,String absoluteFileName,String type)
	{
		try 
		{  
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null)			
			{
			    //System.out.println(line);				
				logger.info(type + ":" + line);
				
			    //如果转化为简单压缩出错
			    if(type.compareToIgnoreCase("error_transfer_grid_simple")==0)			    
			    {
			    	logger_e.error(type + ":" + line);
			    	
			    	String log_data = "使用grib_api转化文件压缩方式失败,filename="+absoluteFileName + ","+ getSysTime() + ",Exception:将复杂压缩转化为简单压缩失败";
					logger_e.error(log_data);
					String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
					GribConfig.write_log_to_file(error_log_file, type + ":" + line);
					GribConfig.write_log_to_file(error_log_file, log_data);
			    }			    
			    
			}			  
	            
	       }		
		catch (IOException ioe) {  
	           ioe.printStackTrace();  
	           String log_data = "steamout函数异常,filename="+absoluteFileName + "," + getSysTime()+",Exception:" + ioe.getMessage();
	           logger_e.error(log_data);
	           String error_log_file = GribConfig.getLogFilePath()+"error_"+GribConfig.getSysDate()+".log";
	           GribConfig.write_log_to_file(error_log_file, log_data);
	      }  
	        
	}
	
	//byte数组转long
	public long bytesToLong(byte[] bytes) 
	{  
		ByteBuffer buffer = ByteBuffer.allocate(8);  
	  	buffer.put(bytes, 0, bytes.length);  
	   	buffer.flip();//need flip   
	  	return buffer.getLong();  
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

	//文件写到相应的LATEST目录 latest_file_dir
	/******解析产品要素，然后删除LATEST目录下删除同一个要素之前的产品：Z_NWGD_C_BABJ_YYYYMMDDhhmmss_P_RFFC_SCMOC-ER03_YYYYMMDDhhmm_24003.GRB2**/

	private int copy_file_to_latest_dir(String filename,String latest_file_dir,String data_id,String pure_name){
		//String original_filename_array[] = filename.split("/");
		//String pure_name = original_filename_array[original_filename_array.length-1];
		String target_latest_filename=latest_file_dir+"/"+pure_name;
		logger.info("target_filename:" +target_latest_filename);
		//将文件复制到LATEST目录下
		int write_latest_nas = copy_file_to_antherfile(filename,target_latest_filename,data_id);
		logger.info("write_latest_nas=" + write_latest_nas);
		String[] str_array = pure_name.split("_");
		String element = str_array[str_array.length-3]; //产品要素：比如：SCMOC-ER03
		element = element + "_"; //因为可能出现：SCMOC-ERH_和SCMOC-ERHI_
		String date_time = str_array[str_array.length-2]; //起报时次
		String forecast_aging = str_array[str_array.length-1]; //预报时效最大间隔时次，比如：24003.GRB2
		String forecast_aging_new =date_time.substring(8, 12) + "_"+forecast_aging ;//加上起报时次，如：2000_07203

		//logger.info("element="+element);
		//logger.info("forecast_aging="+forecast_aging);
		File file=new File(latest_file_dir);
		File[] tempList = file.listFiles();
		ArrayList<String> filename_list = new ArrayList<String>();
		int ret = 0;
		filename_list.clear();
       //文件成功复制到LATEST目录后，删除原来同要素，时次等条件下旧的文件，SPCC类型文件只保留最新时次的最新制作时间文件，其他文件保留每个时次最新文件
			if(write_latest_nas==1){

				if(element.contains("SPCC")){
					for (int i = 0; i < tempList.length; i++)
					{
						if(tempList[i].isFile()&&tempList[i].getName().contains(element)&&tempList[i].getName().contains(forecast_aging)) //同一个要素和预报时效间隔时次的产品文件
						{
							//logger.info("latest目录下文件："+tempList[i].getName());
							filename_list.add(tempList[i].getName());
							if(filename_list.size()==2) //在list中放入2个同产品的文件名时，进行比较，并删除更早的那个
							{
								//第一个文件名的制作时间和起报时间
								String filename1_array[] = filename_list.get(0).toString().split("_");
								String maketime1 = filename1_array[4]; //制作时间
								String forecasttime1 = filename1_array[filename1_array.length-2]; //起报时间

								//第二个文件名的制作时间和起报时间
								String filename2_array[] = filename_list.get(1).toString().split("_");
								String maketime2 = filename2_array[4]; //制作时间
								String forecasttime2 = filename2_array[filename2_array.length-2]; //起报时间

								//logger.info("forecasttime1="+forecasttime1+",maketime1="+maketime1);
								//logger.info("forecasttime2="+forecasttime2+",maketime2="+maketime2);

								if(forecasttime1.compareToIgnoreCase(forecasttime2)<0) //起报时间第一个文件是旧的，删除
								{
									String absolute_filename = latest_file_dir + "/" + filename_list.get(0).toString();
									int exitVal = del_file(absolute_filename);
									filename_list.remove(0);
									//logger.info("del file:"+absolute_filename + ",exitVal=" + exitVal);
									ret=1;
								}
								else if(forecasttime1.compareToIgnoreCase(forecasttime2)>0) //起报时间第二个文件是旧的，删除
								{
									String absolute_filename = latest_file_dir + "/" + filename_list.get(1).toString();
									int exitVal = del_file(absolute_filename);
									filename_list.remove(1);
									//logger.info("del file:"+absolute_filename + ",exitVal=" + exitVal);
									ret=1;
								}
								else //起报时间一致
								{
									if(maketime1.compareToIgnoreCase(maketime2)<0) //制作时间第一个文件是旧的，删除
									{
										String absolute_filename = latest_file_dir + "/" + filename_list.get(0).toString();
										int exitVal = del_file(absolute_filename);
										filename_list.remove(0);
										//logger.info("del file:"+absolute_filename + ",exitVal=" + exitVal);
										ret=1;
									}
									else if(maketime1.compareToIgnoreCase(maketime2)>0) //制作时间第二个文件是旧的，删除
									{
										String absolute_filename = latest_file_dir + "/" + filename_list.get(1).toString();
										int exitVal = del_file(absolute_filename);
										filename_list.remove(1);
										//logger.info("del file:"+absolute_filename + ",exitVal=" + exitVal);
										ret=1;
									}
								}
							}
						}
					}

				}else{

					for (int i = 0; i < tempList.length; i++){
						if(tempList[i].isFile()&&tempList[i].getName().contains(element)
								&&tempList[i].getName().contains(forecast_aging_new)) //同一个要素和预报时效间隔时次、同一起报时次的产品文件
						{
							filename_list.add(tempList[i].getName());
							if(filename_list.size()==2) //在list中放入2个同产品的文件名时，进行比较，并删除更早的那个
							{
								//第一个文件名的制作时间和起报时间
								String filename1_array[] = filename_list.get(0).toString().split("_");
								String maketime1 = filename1_array[4]; //制作时间
								String forecasttime1 = filename1_array[filename1_array.length-2]; //起报时间

								//第二个文件名的制作时间和起报时间
								String filename2_array[] = filename_list.get(1).toString().split("_");
								String maketime2 = filename2_array[4]; //制作时间
								String forecasttime2 = filename2_array[filename2_array.length-2]; //起报时间

								//logger.info("forecasttime1="+forecasttime1+",maketime1="+maketime1);
								//logger.info("forecasttime2="+forecasttime2+",maketime2="+maketime2);

								if(forecasttime1.compareToIgnoreCase(forecasttime2)<0) //起报时间第一个文件是旧的，删除
								{
									String absolute_filename = latest_file_dir + "/" + filename_list.get(0).toString();
									int exitVal = del_file(absolute_filename);
									filename_list.remove(0);
									//logger.info("del file:"+absolute_filename + ",exitVal=" + exitVal);
									ret=1;
								}
								else if(forecasttime1.compareToIgnoreCase(forecasttime2)>0) //起报时间第二个文件是旧的，删除
								{
									String absolute_filename = latest_file_dir + "/" + filename_list.get(1).toString();
									int exitVal = del_file(absolute_filename);
									filename_list.remove(1);
									//logger.info("del file:"+absolute_filename + ",exitVal=" + exitVal);
									ret=1;
								}
								else //起报时间一致
								{
									if(maketime1.compareToIgnoreCase(maketime2)<0) //制作时间第一个文件是旧的，删除
									{
										String absolute_filename = latest_file_dir + "/" + filename_list.get(0).toString();
										int exitVal = del_file(absolute_filename);
										filename_list.remove(0);
										//logger.info("del file:"+absolute_filename + ",exitVal=" + exitVal);
										ret=1;
									}
									else if(maketime1.compareToIgnoreCase(maketime2)>0) //制作时间第二个文件是旧的，删除
									{
										String absolute_filename = latest_file_dir + "/" + filename_list.get(1).toString();
										int exitVal = del_file(absolute_filename);
										filename_list.remove(1);
										//logger.info("del file:"+absolute_filename + ",exitVal=" + exitVal);
										ret=1;
									}
								}
							}

						}
					}
				}
			}

		return ret;
	}


	private  int del_file(String target_latest_filename)
	{
		File file = new File(target_latest_filename);
		int exitVal = 1;
		// 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
		if (file.exists() && file.isFile()) {
			if (file.delete()) {
				System.out.println("删除单个文件" + target_latest_filename + "成功！");
				return exitVal;
			} else {
				System.out.println("删除单个文件" + target_latest_filename + "失败！");
				exitVal=0;
				return exitVal;
			}
		} else {
			System.out.println("删除单个文件失败：" + target_latest_filename + "不存在！");
			exitVal=0;
			return exitVal;
		}
	}
}