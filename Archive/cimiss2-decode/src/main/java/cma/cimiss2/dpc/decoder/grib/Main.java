package cma.cimiss2.dpc.decoder.grib;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cimiss.core.ATS;
import org.cimiss.core.ATSClient;
import org.cimiss.core.bean.GridData;
import org.cimiss.core.model.ATSBatchResult;

public class Main
{ 
	public static void main(String[] args)
	{
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
			
		//String filename = "测试数据/美国KWBC_单场/NAFP_KWBC_1_FTM-7-SHEM-RHU-1250X999998-116-7680-999998-999998-999998-2017112000-6.GRB";
		//String filename = "测试数据/美国KWBC_单场/NAFP_KWBC_1_FTM-7-NHEJ-PRS-1250X999998-6-0-999998-999998-999998-2017112000-36.GRB";
		//String filename = "测试数据/美国KWBC_单场/NAFP_KWBC_1_FTM-7-NHEK-PRS-1250X999998-7-0-999998-999998-999998-2017112000-6.GRB";
		//String filename = "测试数据/美国KWBC_单场/NAFP_KWBC_1_FTM-7-NHEK-RHU-1250X999998-116-7680-999998-999998-999998-2017120618-36.GRB";
		
		//String filename = "测试数据/德国_高分_单场/NAFP_EDZW_1_FTM-78-GLB-SOTM-250X250-106-0-0-002-999998-2017111700-0.GRB";	
		
		
		
		//String filename = "测试数据/EC_高分_原始多场/W_NAFP_C_ECMF_20171228060042_P_C1D12280000122800011";
		//String filename = "测试数据/EC_高分_原始多场/W_NAFP_C_ECMF_20171228060243_P_C1D12280000122803001";
		
		//String filename = "data_service/F.0010.0002.S001/EC_高/field_file/W_NAFP_C_ECMF_20171228060042_P_C1D12280000122800011.295";
		//String filename = "data_service/F.0010.0002.S001/EC_高/field_file/W_NAFP_C_ECMF_20171228060243_P_C1D12280000122803001-副本.546";
		
		//String filename = "测试数据/GRAPES_GFS_单场/Z_NAFP_C_BABJ_20171221120000_P_NWPC-GRAPES-GFS-HNEHE-02100.grib2.531";
		
		//String filename = "测试数据/Z_NAFP_C_BABJ_20171229000000_P_NWPC-GRAPES-GFS-HNEHE-16200.grib2.39";
		//String filename = "测试数据/A_HPXA89ECMG160000_C_BABJ_20180116054201_52798.bin.1";
		//String filename = "测试数据/Z_NAFP_C_BABJ_20180115000000_P_NWPC-GRAPES-GFS-HNEHE-11400.grib2";
		//String filename = "测试数据_测试数据/A_HVBI98KWBC220000_C_BABJ_20180122044437_90859.bin";
		//String filename = "测试数据_测试数据/Z_NAFP_C_BABJ_20180201000000_P_CNPC-GRAPES-RMFS-FCSTER-03900.grib2";
		//String filename = "测试数据_测试数据/Z_NAFP_C_BABJ_20180201000000_P_CNPC-GRAPES-RMFS-FCSTER-07200.grib2";
		//String filename = "测试数据_测试数据/Z_NAFP_C_BABJ_20180202000000_P_CNPC-GRAPES-RMFS-FCSTER-03300.grib2";
		//String filename = "测试数据_测试数据/Z_NAFP_C_BABJ_20180202000000_P_CNPC-GRAPES-RMFS-FCSTER-03600.grib2";
		//String filename = "测试数据_测试数据/Z_NAFP_C_BABJ_20180205000000_P_NWPC-GRAPES-GFS-HNEHE-15000.grib2";
		//String filename = "测试数据_测试数据/W_NAFP_C_ECMF_20171228060243_P_C1D12280000122803001.546";
		//String filename = "测试数据_测试数据/NAFP_ECMF_1_FTM-98-GLB-SST-125X125-1-0-999998-999998-999998-2018010600-0.GRB";
		//String filename = "测试数据_测试数据/NAFP_ECMF_1_FTM-98-GLB-SST-125X125-1-0-999998-999998-999998-2018010612-69.GRB";
		//String filename = "测试数据_测试数据/W_NAFP_C_ECMF_20180204055947_P_C1D02040000020506001";
		//String filename = "测试数据_测试数据/NAFP_ECMF_1_FTM-98-GLB-SST-125X125-1-0-999998-999998-999998-2018020400-30.GRB";
		//String filename = "测试数据_测试数据/NAFP_ECMF_0_FTM-98-GLB-SST-125X125-1-0-999998-999998-999998-2018020400-0.GRB";
		//String filename = "测试数据_测试数据/NAFP_ECMF_1_FTM-98-GLB-SST-125X125-1-0-999998-999998-999998-2018020400-0.GRB";
		
		//String filename = "D:/DPC-气象资料处理/广东-接入GRIB2资料/Z_NAFP_C_BCGZ_20180117120000_P_LARP-GRAPES-MARS9KM-13500.grib2";
		//String filename = "D:/DPC-气象资料处理/广东-接入GRIB2资料/Z_NAFP_C_BCGZ_20180117180000_P_LARP-GRAPES-TRAMS18KM-00000.grib2";
		//String filename = "D:/DPC-气象资料处理/广东-接入GRIB2资料/Z_NAFP_C_BCGZ_20180117230000_P_LARP-GRAPES-CHAF-00400.grib2";
		
		//String filename = "测试数据/EC_高分_原始多场/W_NAFP_C_ECMF_20171119061826_P_C1D11190000112218001.611";
		
		//String filename = "测试数据_测试数据/A_HUXK85ECMG070000_C_BABJ_20180707060746_75610.bin";
		
		//String filename = "测试数据/实况-CLDAS/Z_NAFP_C_BABJ_20180730070951_P_CLDAS_RT_CHN_0P05_DAY-MXWIN-2018073008.GRB2";
		//String filename = "ECMF_TEM_2018071400_GLB.grib2";
		//String filename = "测试数据_测试数据/W_NAFP_C_RJTD_20180807120000_GSM_GPV_RRA2_GLL0P5DEG_LSURF_FD0118_GRIB2.BIN";
		//String filename = "data_service/F/F.0011.0002.S001/20180807/W_NAFP_C_RJTD_20180807120000_GSM_GPV_RRA2_GLL0P5DEG_LSURF_FD0118_GRIB2.BIN.8";
		
		//String filename = "测试数据_测试数据/Z_NAFP_C_BABJ_20180822060000_P_CNPC-T639-GMFS-HNEHE-05100.grib2"; //T639		
		
		//String filename = "测试数据_测试数据/F.0013.0001.R001/EDZW_TPRA_1_2019022500_GLB_1_2.grib2";
		
		//String filename = "D:/大数据平台-存储设计-2017/数据模式解码-大数据平台/dwp2/cimiss2-decode/测试数据_测试数据/F.0011.0001.R001/A_HHCA70RJTD041200_C_BABJ_20190704153235_72070.bin"; //F.0011.0001.R001 
		
		//String filename = "测试数据_测试数据/F.0010.0004.S001/W_NAFP_C_ECMF_20190701071046_P_C3E07010000070206001";
		
		//String filename = "测试数据_测试数据/Z_NWGD_C_BEPK_20190520155738_P_RFFC_SPCC-EDA10_201905202000_24003.GRB2";
		
		//String filename = "测试数据_测试数据/F.0011.0002.R001/W_NAFP_C_RJTD_20190729060000_GSM_GPV_Rra2_Gll0p5deg_Lp20_FD0100_grib2.bin"; //F.0011.0002.R001
		//String filename = "测试数据_测试数据/F.0011.0002.R001/W_NAFP_C_RJTD_20190729060000_GSM_GPV_Rra2_Gll0p5deg_Lp20_FD0121_grib2.bin"; //F.0011.0002.R001
		//String filename = "测试数据_测试数据/F.0011.0002.R001/W_NAFP_C_RJTD_20190729060000_GSM_GPV_Rra2_Gll0p5deg_Lsurf_FD0103_grib2.bin"; //F.0011.0002.R001
		//String filename = "测试数据_测试数据/F.0011.0002.R001/W_NAFP_C_RJTD_20190729060000_GSM_GPV_Rra2_Gll0p5deg_Lsurf_FD0118_grib2.bin"; //F.0011.0002.R001
		//String filename = "测试数据_测试数据/F.0011.0002.R001/W_NAFP_C_RJTD_20190729060000_GSM_GPV_Rra2_Gll0p25deg_Lsurf_FD0000_grib2.bin"; //F.0011.0002.R001
		//String filename = "测试数据_测试数据/F.0011.0002.R001/W_NAFP_C_RJTD_20190729060000_GSM_GPV_Rra2_Gll0p25deg_Lsurf_FD0003_grib2.bin"; //F.0011.0002.R001
		//String filename = "测试数据_测试数据/F.0011.0002.R001/W_NAFP_C_RJTD_20190729060000_GSM_GPV_Rra2_Gll0p25deg_Lsurf_FD0006_grib2.bin"; //F.0011.0002.R001
		
		
		String filename = "D:/模式数据处理-2019/GRAPES_REPS区域集合预报数据接入/Z_NAFP_C_BABJ_20191030000000_P_NWPC-GRAPES-REPS-CN-08400-m007.grib2";
		
		String d_data_id = "F.0007.0002.R001"; //四级编码
		
		GribConfig.readConfig();
		
		GribConfigAreaMapping.readConfig();
		
				
		//System.out.println("1111111111");
		/*
		GribDecode grib_decode = new GribDecode();
		Map<String,List<Grib_Struct_Data>> map_grib_decode_result1 = new HashMap<String,List<Grib_Struct_Data>>();
		Map<String,List<Grib_Struct_Data>> map_grib_decode_result2 = new HashMap<String,List<Grib_Struct_Data>>();
				
		
		map_grib_decode_result1.clear();	
		map_grib_decode_result1 = grib_decode.read_grib1_data(filename,d_data_id,new Date());
		System.out.println("map_grib_decode_result1.size:" +map_grib_decode_result1.size());
		//int sucess_num = grib_decode.into_cassandra_map(map_grib_decode_result);
		//System.out.println("sucess_num="+sucess_num);
		
		
		//System.out.println("22222222");
		
		map_grib_decode_result2.clear();	
		map_grib_decode_result2 = grib_decode.read_grib2_data(filename,d_data_id,new Date());
			
		System.out.println("map_grib_decode_result1.size:" +map_grib_decode_result1.size());
		for (String area : map_grib_decode_result1.keySet()) 
		{  			  
		    System.out.println("area = " + area); 
		   
		    List<Grib_Struct_Data> grib_record_list = map_grib_decode_result1.get(area);
		    System.out.println("grib_record_list.size:" + grib_record_list.size());	
		}
		
		System.out.println("map_grib_decode_result2.size:" +map_grib_decode_result2.size());
		for (String area : map_grib_decode_result2.keySet()) 
		{  			  
		    System.out.println("area = " + area); 
		   
		    List<Grib_Struct_Data> grib_record_list = map_grib_decode_result2.get(area);
		    System.out.println("grib_record_list.size:" + grib_record_list.size());	
		}
		*/
		
		/*
		CassandraDbOperation cassadraOper  = new CassandraDbOperation();
		int sucess_list_num = cassadraOper.into_cassandra_map(map_grib_decode_result2);
		cassadraOper.close();	
		System.out.println("sucess_list_num:" + sucess_list_num);
		*/
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		/*
		CassandraDbOperation cassadraOper = new CassandraDbOperation();		
		for(int ttt=0;ttt<30;ttt++)
		{
			System.out.println("第" +ttt + "次入库:");
			int sucess_num = cassadraOper.into_cassandra_map(map_grib_decode_result);
			System.out.println("sucess_num:" +sucess_num);
		}
		cassadraOper.close();	
		
		map_grib_decode_result.clear();
		map_grib_decode_result = null;
		cassadraOper = null;		
		*/
		
		//CassandraDbOperation cassadraOper = new CassandraDbOperation();
		CassandraDbOperation cassadraOper = null;
		GribDecode grib_decode = new GribDecode();
		
		DataAttr data_attributes = (DataAttr) GribConfig.get_map_data_description().get(d_data_id);
		String field_organize = data_attributes.getfieldorganize();
		
		Date rcv_time = new Date();
		int sucess_sum = 0;
		String ret_str = "";

		System.out.println("wenjian:"+filename);

		if("common".compareToIgnoreCase(field_organize)==0) //将原始多场文件拆分成单场文件：GRIB...7777...GRIB...7777
		{			
			//倒数第3个参数如果是null，不入Cassandra；
			//倒数第2个参数是true，入单场信息索引表
			//最后1参数是false，不发送DI
			//sucess_sum = grib_decode.split_grib_record_use_S0Lengh(filename, d_data_id, rcv_time,null,true);
			//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
			ret_str = grib_decode.split_grib_record_use_S0Lengh(filename, d_data_id, rcv_time,null,null,true,false);
		}
		else if("other".compareToIgnoreCase(field_organize)==0) //将原始多场文件拆分成单场文件：循环嵌套（如日本高分）
		{
			//倒数第2个参数如果是null，不入Cassandra；
			//最后1个参数是true，入单场信息索引表
			//sucess_sum = grib_decode.split_grib_record_use_S0Lengh(filename, d_data_id, rcv_time,null,true);
			//返回：解码成功场数_入Cassandra场数_入索引表场数_文件读取错误
			ret_str = grib_decode.split_grib_record_other(filename, d_data_id, rcv_time,null,null,true,false);
		}
		//System.out.println("sucess_sum=" + sucess_sum);	
		System.out.println("ret_str:"+ret_str);
		//cassadraOper.close();	
				
		
		//into_cassa();
	}
	
	private static void into_cassa()
	{
		GridData gridData = new GridData();
		gridData.setProdCode("RHU");
		//System.out.println("Element:" +grib_record_list.get(i).getElement());
		gridData.setProductDescription("org.cimiss2.decode.grib.DataAttr@7946e1f4 RHU");
		//System.out.println("Description:" +grib_record_list.get(i).getProductDescription());
		gridData.setLevel(0);
		//System.out.println("Level1:"+grib_record_list.get(i).getLevel1());
		Date date = null;
		try 
		{
			date = new SimpleDateFormat("yyyyMMddHHmmss").parse("20171215160000");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gridData.setTime(date);
		//System.out.println("Time:" +date.getTime());
		gridData.setValidtime(48);
		gridData.setValuePrecision(100);
		gridData.setValueByteNum(4);
		//System.out.println("Validtime:" + grib_record_list.get(i).getV04320());
		float[] data = new float[720*361];
		for(int i=0;i<2;i++)
		{
			data[i] = 84.0f;
		}
		gridData.setValue(data);
		int len = data.length;
		//System.out.println("data.length:" + grib_record_list.get(i).getdata().length);			
		//System.out.println("data:" + grib_record_list.get(i).getdata()[0] + "," + grib_record_list.get(i).getdata()[len-1]);
		gridData.setStartX(8.0f);
		//System.out.println("StartX:" + grib_record_list.get(i).getStartX());
		gridData.setStartY(0.0f);
		//System.out.println("StartY:" + grib_record_list.get(i).getStartY());
		gridData.setEndX(359.0f);
		//System.out.println("EndX:" + grib_record_list.get(i).getEndX());
		gridData.setEndY(180.0f);
		//System.out.println("EndY:" + grib_record_list.get(i).getEndY());
		gridData.setXStep(1.25f);
		//System.out.println("XStep:" + grib_record_list.get(i).getXStep());
		gridData.setYStep(1.25f);
		//System.out.println("YStep:" + grib_record_list.get(i).getYStep());
		gridData.setXCount(135);
		//System.out.println("XCount" + grib_record_list.get(i).getXCount());
		gridData.setYCount(120);
		//System.out.println("YCount:" + grib_record_list.get(i).getYCount());
		gridData.setValueByteNum(4);
		//System.out.println("ValueByteNum:" + grib_record_list.get(i).getValueByteNum());
		gridData.setValuePrecision(10);
		//System.out.println("ValuePrecision:" + grib_record_list.get(i).getValuePrecision());
		gridData.setGridUnits(0);
		//System.out.println("GridUnits:" + grib_record_list.get(i).getGridUnits());
		gridData.setGridProject(0);
		//System.out.println("GridProject:" + grib_record_list.get(i).getGridProject());
		gridData.setDataId("F.0011.0002.S001");
		//System.out.println("data_id:" + grib_record_list.get(i).getdata_id());
		gridData.setHeightCount(1);
		//System.out.println("HeightCount:" + grib_record_list.get(i).getHeightCount());
		float[] heights = {0};
		gridData.setHeights(heights);
		//System.out.println("Heights:" + grib_record_list.get(i).getHeights());
		gridData.setHeightType(100);
		//System.out.println("LevelType:" + grib_record_list.get(i).getLevelType());
		gridData.setEventTime(new Date());
		
		gridData.setProductCenter("34");
		//System.out.println("ProductCenter:" + grib_record_list.get(i).getProductCenter());
		gridData.setProductMethod("0");
		//System.out.println("ProductMethod:" + grib_record_list.get(i).getProductMethod());
		gridData.setIYMDHM("20171215190612");
		
		
		try 
		{
			ATS ats = new ATSClient();			
			//"NAFP_FOR_FTM_RJTD_LOW_GLB";
			String table_name = "NAFP_FOR_FTM_AREA";
			System.out.println("table_name:"+table_name);
			int result = ats.writeGridData(table_name, gridData);
			System.out.println("result: [" + result + "]");
			ats.close();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}