package org.cimiss2.dpc.indb.grib;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
 

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;


public class SendKafkaTest {
	
	private final KafkaProducer<String, byte[]> producer;
	
	private Map<String, Object> propProducer = new HashMap<>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SendKafkaTest produce = new SendKafkaTest();
		// 发送发字节数组
	
		//produce.testGrib("M.0002.0005.S001","d:\\data\\meofis\\SEVP_NMC_RFFC_SCMOC_EME_ACHN_L88_P9_20170901000016812.TXT");
 	
		//produce.testMw("B.0030.0002.S001","d:\\data\\mw\\54406_2018-05-25_00-00-00_lv1.csv");
		//B.0007.0003.S001 A.0012.0001.S001.nation
		//produce.testUpr();
		//
		//produce.testNcepRamp();
		//produce.testSurfDay();
	 

		//F.2007.5101.S001 F.2007.5103.S001
	//	produce.testNcep("F.2007.5101.S001","d:\\data\\ncep\\hgt.2016.nc");		
				//produce.testNcep("F.0030.0006.S001","d:\\data\\ncep\\slp.2016.nc");
		//		produce.testNcep("F.2007.5103.S001","d:\\data\\ncep\\hgt.mon.mean.nc");	
				//produce.testNcep("F.0030.0006.S002","d:\\data\\ncep\\slp.mon.mean.nc");
	/*	 
		//produce.testNcep("F.0030.0006.S001","d:\\data\\ncep\\hgt.2016.nc");		
		produce.testNcep("F.0030.0006.S001","d:\\data\\ncep\\slp.2016.nc");
		//produce.testNcep("F.0030.0006.S002","d:\\data\\ncep\\hgt.mon.mean.nc");	
		produce.testNcep("F.0030.0006.S002","d:\\data\\ncep\\slp.mon.mean.nc");	
		
		
		produce.testNcep("F.0047.0001.S001","d:\\data\\RMAPSIN\\ANA-2019030416_201903041600_201903041559.nc");	//rmapssin
		produce.testNcep("F.0047.0002.S001","d:\\data\\rmapsnow\\rmapsnow_anal_init-20190420161000_2d.nc");	//rmapssnow
		produce.testNcep("F.0048.0001.S001","d:\\data\\derf\\Z_NAFP_C_BAQH_20181110000000_P_BCC_AGCM2.2_LSPR_1.0_MN_00.nc");	//derf
		
		produce.testGrib("F.0010.0003.S001","d:\\data\\ecmf\\W_NAFP_C_ECMF_20190120055430_P_C1D01200000012006001");
		produce.testGrib("F.0010.0004.S001","d:\\data\\ecmf\\W_NAFP_C_ECMF_20190120190635_P_C3E01201200012018001");
		 
		produce.testGrib("F.0044.0010.S001","d:\\data\\bjgrib\\Z_NWGD_C_BEPK_20190519080447_P_RFFC_SPCC-TMP_201905190600_02601.GRB2"); //bj
		produce.testGrib("F.0009.0001.S001","d:\\data\\bjgrib\\Z_NAFP_C_BABJ_20180913120000_P_CNPC-GRAPES-RMFS-FCSTER-00000.grib2");
		
		produce.testGrib("F.0006.0001.S001","d:\\data\\grapes\\Z_NAFP_C_BABJ_20181230000000_P_NWPC-GRAPES-GFS-GLB-24000.grib2");
		produce.testGrib("F.0006.0002.S001","d:\\data\\grapes\\Z_NAFP_C_BABJ_20181230000000_P_NWPC-GRAPES-GFS-HNEHE-00000.grib2");
		
		produce.testGrib("F.0011.0002.S001","d:\\data\\japan\\Z__C_RJTD_20190115120000_GSM_GPV_Rra2_Gll0p5deg_Lp10_FD0109_grib2.bin");
		produce.testGrib("F.0011.0002.S001","d:\\data\\japan\\Z__C_RJTD_20190115120000_GSM_GPV_Rgl_Gll0p5deg_L-pall_FD0009_grib2.bin");

		produce.testGrib("M.0002.0005.S001","d:\\data\\meofis\\SEVP_NMC_RFFC_SCMOC_EME_ACHN_L88_P9_20170901000016812.TXT"); // 
		*/
		//Z_NAFP_C_BABJ_YYYYMMDDhhmmss_P_NWPC-GRAPES-MESO-CN-FFFMM.grib2  GRAPES_MESO中尺度模式系统产品  下载平台
		//Z_NAFP_C_BABJ_YYYYMMDDhh0000_P_CNPC-GRAPES-RMFS-FCSTER-FFFMM.grib2 GRAPES_MESO中尺度模式数值预报产品
	 
		//produce.testGrib("F.0010.0004.S001","d:\\data\\ecmf\\W_NAFP_C_ECMF_20190120190635_P_C3E01201200012018001");
		//produce.testNcep("F.2007.5101.S001","d:\\data\\ncep\\hgt.2016.nc");		
		//produce.testNcep("F.2010.0004.S001","d:\\data\\bcc\\20110901.atm.TSMX.201109-201209_sfc_member.nc");	 //bcc
	//	produce.testNcep("F.2010.0004.S003","d:\\data\\bcc\\19941001.ocn.temp.199410-199510_dpt005_member.nc");	 //bcc
	//	produce.testNcep("F.2010.0004.S004","d:\\data\\bcc\\20180701.atm.PREC.20180701-20180831_sfc_member.nc");	 //bcc
		
	//	produce.testGrib("F.0040.0009.S001","d:\\data\\sateproduct\\Z_SATE_C_BARY_20190104043000_P_FY2-CPP.DAT"); //sate2e
	//	produce.testUpr("B.0007.0003.S001","d:\\data\\wind\\Z_RADA_I_53399_20160201000520_O_WPRD_PB_FFT.BIN"); //wprd
	// 	produce.testNcep("F.0047.0004.S001","D:\\data\\chem\\2019052612\\aqi_2019052612_d01.grd");	//rmapschem
	//	produce.testNcep("F.0047.0004.S001","D:\\data\\chem\\2019052612\\surf_2019052612_d02.grd");	//rmapschem
		
 	//	produce.testGrib("F.0044.0001.S001","d:\\data\\nwgd\\Z_NAFP_C_BABJ_20190612000905_P_CLDAS_RT_CHN_0P05_HOR-TCDC-2019061200.GRB2"); //cctv
 //		produce.testGrib("F.0044.0001.S001","d:\\data\\cma\\Z_NAFP_C_BABJ_20181201191007_P_CLDAS_RT_CHN_0P05_DAY-MXT-2018120120.GRB2"); //cctv
//		produce.testGrib("F.0044.0001.S001","d:\\data\\nwgd\\Z_NAFP_C_BABJ_20190612101006_P_CLDAS_RT_CHN_0P05_HOR-VIS-2019061210.GRB2"); //cctv
//		produce.testGrib("F.0044.0002.S001","d:/data/nwgd/Z_NWGD_C_BABJ_20190428104111_P_RFFC_SMERGE-EDA10_201904280800_24003.GRB2"); //cctv
// 		produce.testGrib("F.0044.0001.S001","d:\\data\\nwgd\\Z_SURF_C_BABJ_20190612011145_P_CMPA_FAST_CHN_0P05_3HOR-PRE-2019061201.GRB2"); //cctv
//  	    produce.testGrib("F.0044.0010.S001","d:\\data\\bjgrib\\Z_NWGD_C_BEPK_20190519080447_P_RFFC_SPCC-TMP_201905190600_02601.GRB2"); //bj
//		 produce.testGrib("F.0044.0011.S001","d:\\data\\qxt\\Z_NWGD_C_BEPK_20190703135410_P_RFFC_SPCC-TMP_201907031100_02101.GRB2"); //qxt
		
	//    produce.testNcep("F.0047.0002.S001","d:\\data\\rmapsnow\\rmapsnow_anal_init-20190420161000_2d.nc");	//rmapssnow
		//  produce.testNcep("F.0047.0002.S001","d:\\data\\rmapsnow\\rmapsnow_fcst_init-20190703053000_valid-20190703073000_3d.nc");	//rmapssnow	
	 	//  produce.testNcep("F.0047.0002.S001","d:\\data\\rmapsnow\\rmapsnow_anal_init-20190703053000_3d.nc");	//duanshilinjin
 //   produce.testNcep("A.0042.0005.S001","d:\\data\\pre\\Z_SURF_C_BABJ_20181123050932_P_CMPA_NRT_CHN_0P01_HOR-PRE-2018112122.nc");	//duoyuanjiangshui多源降水		
 //   produce.testNcep("A.0042.0005.S001","d:\\data\\pre\\Z_SURF_C_BABJ_20181123052220_P_CMPA_NRT_CHN_0P05_HOR-PRE-2018112123.nc");	//duoyuanjiangshui多源降水		
//    produce.testNcep("A.0042.0005.S001","d:\\data\\pre\\Z_SURF_C_BABJ_20181123052220_P_CMPA_NRT_CHN_0P01_HOR-PRE-2018112123.png");	//duoyuanjiangshui多源降水		
//     
//		produce.testNcep("F.0047.0001.S001","d:\\data\\RMAPSIN\\ANA-2019030416_201903041600_201903041559.nc");
 //		produce.testNcep("F.0047.0001.S001","d:\\data\\RMAPSIN\\FINAL-2019042016_201904201600_201904201600.nc");
	 
  //		produce.testGrib("F.0044.0001.S001.CMA","d:\\data\\nwgd\\Z_SURF_C_BABJ_20190612011145_P_CMPA_FAST_CHN_0P05_3HOR-PRE-2019061201.GRB2"); //CMA
 // 		produce.testGrib("F.0044.0002.S001.CMA","d:/data/nwgd/Z_NWGD_C_BABJ_20190428104111_P_RFFC_SMERGE-EDA10_201904280800_24003.GRB2"); //CMA
//		produce.testGrib("F.0044.0001.S001.CMA","d:\\data\\cma\\Z_NAFP_C_BABJ_20181112042213_P_CLDAS_RT_ASI_0P0625_HOR-GST005-2018111203.nc"); //CMA
//		produce.testGrib("F.0044.0001.S001.CMA","d:\\data\\cma\\Z_NAFP_C_BABJ_20181120220422_P_CLDAS_NRT_ASI_0P0625_HOR-PRS-2018111822.jpg"); //CMA
 //		produce.testGrib("F.0044.0001.S001.CMA","d:\\data\\cma\\Z_NAFP_C_BABJ_20181201191007_P_CLDAS_RT_CHN_0P05_DAY-MXT-2018120120.GRB2"); //CMA
		
		
 //		produce.testGrib("F.0044.1000.S001","d:\\data\\index\\SAFETY20190703102000.nc"); //index
//		produce.testGrib("F.0044.1000.S001","d:\\data\\index\\DRESS20190703102000.nc"); //index
//		produce.testGrib("F.0044.1000.S001","d:\\data\\index\\TOUR20190703102000.nc"); //index
//		produce.testGrib("F.0044.1000.S001","d:\\data\\index\\UV20190703102000.nc"); //index
		
		
 		//produce.testGrib("M.0035.0002.S001","d:\\data\\strong\\Z_SEVP_C_BABJ_20190121120456_P_CSPI-SFER-ESCP-ACHN-LNO-P9-201901212000-01206.MIC"); //cspi
  //   	produce.testGrib("F.0010.0001.S001","d:\\data\\ecmf\\A_HEXE10ECEP150000_C_BABJ_20190115074211_86778.bin"); //ecmf
// 		produce.testGrib("F.0010.0001.S001","D:\\data\\10.20.2：欧洲中心细网格模式产品（0.5°×0.5°）\\A_HEXE20ECEP150000_C_BABJ_20190115074211_86760.bin"); //ecmf
 //	 	produce.testGrib("F.0010.0002.S001","D:\\data\\ecmf\\W_NAFP_C_ECMF_20190120055430_P_C1D01200000012006001"); //ecmf
 		
 	//	produce.testGrib("F.0010.0004.S001","d:\\data\\ecmf\\W_NAFP_C_ECMF_20190120190635_P_C3E01201200012018001"); //ecmf
 	//	produce.testGrib("F.0010.0004.S001","d:\\data\\ecmf\\W_NAFP_C_ECMF_20190121070535_P_C3E01200000012000001-ACHN");
 		
		//produce.testGrib("F.0009.0001.S001","d:\\data\\bjgrib\\Z_NAFP_C_BABJ_20180913120000_P_CNPC-GRAPES-RMFS-FCSTER-00000.grib2");
		
 //		produce.testGrib("F.0049.0001.S001","D:\\GRADS\\Z_NAFP_C_BABJ_2018031600_P_CAMS-GRAPES-ACHN.grd"); 
		  
 	//    produce.testGrib("K.0716.0001.S001","D:/data/FY4A L2/CLM/20190501/FY4A-_AGRI--_N_DISK_1047E_L2-_CLM-_MULT_NOM_20190501000000_20190501001459_4000M_V0001.NC"); //FY4A
		 
	//      produce.testGrib("K.0716.0001.S001","D:/data/FY4A L2/CPD/20190501/FY4A-_AGRI--_N_DISK_1047E_L2-_CPD-_MULT_NOM_20190501234500_20190501235959_4000M_V0001.NC"); //FY4A
		 
	// 	  produce.testMw("M.0002.0005.S001","d:\\data\\meofis\\SEVP_NMC_RFFC_SCMOC_EME_ACHN_L88_P9_20170901000016812.TXT"); //meofis
	// 	  produce.testMw("M.0002.0005.S001","d:\\data\\meofis\\SEVP_NMC_RFFC_SCMOC_EME_ACHN_L88_P9_20170910000016812.TXT"); //meofis
	// 	  produce.testMw("M.0002.0005.S001","d:\\data\\meofis\\SEVP_NMC_RFFC_SCMOC_EME_ACHN_L88_P9_20170906000016812.TXT"); //meofis
		  
	 	
  //		produce.testUpr("B.0007.0003.S001","D:\\data\\wind\\Z_RADA_I_53399_20160201000520_P_WPRD_PB_ROBS.txt");
 	//  	produce.testGrib("F.0044.0011.S001","d:/htht/tools/test/data/nwgd/Z_NWGD_C_BEPK_20190703134040_P_RFFC_SPVT-EDA10_201907031400_01801.GRB2"); //qixiangtai duanlin
    //	produce.testGrib("F.0044.0011.S001","/htht/tools/test/data/nwgd/Z_NWGD_C_BEPK_20190703134040_P_RFFC_SPVT-EDA10_201907031400_01801.GRB2"); //qixiangtai duanlin
 //	produce.testGrib("F.0009.0001.S001","/htht/tools/test/data/Z_NAFP_C_BABJ_20180913120000_P_CNPC-GRAPES-RMFS-FCSTER-00000.grib2");
//		produce.testGrib("F.0044.0010.S001","/htht/tools/test/data/Z_NWGD_C_BEPK_20190519080447_P_RFFC_SPCC-TMP_201905190600_02601.GRB2"); 
		
		//for cts interface test 
//		produce.testNcep("J.0012.0000.R000","d:\\data\\bcc\\20110901.atm.TSMX.201109-201209_sfc_member.nc");	 //bcc
//		produce.testNcep("J.0012.0000.R000","d:\\data\\bcc\\20180701.atm.PREC.20180701-20180831_sfc_member.nc");	 //bcc
//		produce.testNcep("J.0012.0000.R000","d:\\data\\bcc\\19941001.ocn.temp.199410-199510_dpt005_member.nc");	 //bcc
		produce.testNcep("J.0012.0000.R000","d:\\data\\bcc\\20180701.atm.Z3.20180701-20180831_prs0500_member.nc");	 //bcc
		
		produce.close();
	}
	
	/**
	 * 
	 * @param path "resources/env.properties"
	 * @return
	 */
	public Properties getProperties(String path) {
		Properties p = null;
		
		try {
			p = new Properties();
			InputStream in = this.getClass().getClassLoader()
					.getResourceAsStream(path);
			p.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return p;
	}
	
	private SendKafkaTest() {

		/*
		 * Properties dbprop = new Properties(); InputStream ins=
		 * SendKafkaTest.class.getClassLoader().getResourceAsStream("kafka.properties");
		 * try { dbprop.load(ins); } catch (IOException e) { e.printStackTrace(); }
		 * propProducer.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
		 * dbprop.getProperty("kafka.bootstrap.servers"));
		 * propProducer.put(ProducerConfig.BATCH_SIZE_CONFIG,
		 * dbprop.getProperty("kafka.BATCH_SIZE_CONFIG"));
		 * propProducer.put(ProducerConfig.LINGER_MS_CONFIG,
		 * dbprop.getProperty("kafka.LINGER_MS_CONFIG"));
		 * propProducer.put(ProducerConfig.BUFFER_MEMORY_CONFIG,
		 * dbprop.getProperty("kafka.BUFFER_MEMORY_CONFIG"));
		 * propProducer.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
		 * dbprop.getProperty("kafka.KEY_SERIALIZER_CLASS_CONFIG"));
		 * propProducer.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
		 * dbprop.getProperty("kafka.VALUE_SERIALIZER_CLASS_CONFIG"));
		 * propProducer.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,
		 * dbprop.getProperty("kafka.ENABLE_IDEMPOTENCE_CONFIG"));
		 * 
		 * propProducer.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
		 */

		Properties properties = new Properties();
	/*	properties.put("bootstrap.servers", "10.224.47.203:9092");
		properties.put("acks", "all");
		properties.put("retries", 0);
		properties.put("batch.size", 16384);
		properties.put("linger.ms", 1);
		properties.put("buffer.memory", 33554432);
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");
	*/	
	 
		properties.put("bootstrap.servers", "10.224.47.203:9092,10.224.47.204:9092,10.224.47.205:9092");
		properties.put("acks", "all");
		properties.put("retries", 3);
		properties.put("batch.size", 1024);
		properties.put("max.request.size", 500000000);
		properties.put("linger.ms", 100);
		properties.put("buffer.memory", 500000000);
		properties.put("request.timeout.ms", 2000000);
		properties.put("client.id", "DemoProducer");
		properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("value.serializer", "org.apache.kafka.common.serialization.ByteArraySerializer");

		producer = new KafkaProducer<String, byte[]>(properties);
	}

    public  void close() {
    	   producer.close(); // 主动关闭生产者
	        
    }
	
	public void testGrib(String topic,String fullName) {
	/* 
		JSONObject js = new JSONObject(true);
		js.put("messageID", fullName);
		js.put("datetype", "NAT_HOR");
		js.put("fromto", "CTS");
		js.put("sendto", "SOD");
		js.put("datatime", "20190501000125");
		
		js.put("sendtime", "20190501000125");
		js.put("version", "1.0");
		js.put("prio", "1");
		js.put("length", "920");
		js.put("encoding", "text/plain");
		
		//js.put("data", getBytes("D:\\data\\Z_SURF_C_BEPK_20181001120000_O_AWS_DAY.txt"));
		js.put("data",  fullName);
		js.put("checknum", "861f942e8993b09076bfc2d524284f10");
		
		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);  
		
		//F.0045.0001.S001 RAMPSIN
      
        ProducerRecord<String, byte[]> msgtar = new ProducerRecord<String, byte[]>(topic,fullName, str.getBytes());
        producer.send(msgtar);
     */
	   
        try {
        	
        	File file = new File(fullName);
       	 
        	// 消息的value
            String messageValue = "F:/dongao/data/H8/HS_H08_20181106_2310_B10_FLDK_R20_S0510.DAT";
            String filename = "HS_H08_20181106_2310_B10_FLDK_R20_S0510.DAT";
            Map<String, Object> js = new LinkedHashMap<>();
    		js.put("TypeTag", 1);
    		js.put("TYPE", topic);
    		js.put("ProjType", null);
    		js.put("STime", "20190610120000");
    		js.put("DataType", null);
    		js.put("InTime", null);
    		js.put("OTime", null);
    		js.put("FileName", file.getName());
    		js.put("NasPath", fullName);
    		js.put("Format", "DAT");
    		js.put("FileSize", "696");
    		js.put("MD5", " 3b00b1249bbe3551");
    		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("filePath", messageValue);
//            map.put("filename", filename);
//            String js = JSONObject.toJSONString(map);
           
			ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, str.getBytes());
			producer.send(record);
            
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//producer.close();
		}
		
	}
	
	public void testSurfDay() {
		
		JSONObject js = new JSONObject(true);
		js.put("messageID", "Z_SURF_C_BCCD_20190501000125_O_AWS_FTM_PQC");
		js.put("datetype", "NAT_HOR");
		js.put("fromto", "CTS");
		js.put("sendto", "SOD");
		js.put("datatime", "20190501000125");
		
		js.put("sendtime", "20190501000125");
		js.put("version", "1.0");
		js.put("prio", "1");
		js.put("length", "920");
		js.put("encoding", "text/plain");
		
		//js.put("data", getBytes("D:\\data\\Z_SURF_C_BEPK_20181001120000_O_AWS_DAY.txt"));
		js.put("data",  "D:\\data\\Z_SURF_C_BEPK_20181001120000_O_AWS_DAY.txt");
		js.put("checknum", "861f942e8993b09076bfc2d524284f10");
		
		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);  
     
		
        ProducerRecord<String, byte[]> msgtar = new ProducerRecord<String, byte[]>("A.0012.0001.S001","", str.getBytes());
       
        producer.send(msgtar);
       
	}
	
	public void testUpr(String topic,String fullName) {
	 
        BufferedReader bufr = null;
        try {
        	 
        	File file = new File(fullName);
        	String line = null;
            StringBuffer sb = new StringBuffer();
            bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while((line = bufr.readLine())!=null){
            	sb.append(line+"\r\n");
            }
            byte[] buf = sb.toString().getBytes();
        	// kafka消息key(模拟)
        	Map<String, Object> map = new LinkedHashMap<String, Object>();//用于存储kafka消息key的集合
        	
        	int TypeTag = 1;//是否为四级资料编码：1 是；0否（必填）
        	String TYPE = topic;//TYPETag =1四级资料编码;TYPETag = 0 自定义资料编码（必填）
        	String IIIII = null;//台站编号：单一台站填写台站编号，多个或者无台站信息填null
        	String CCCC = null;//编报中心：台站上级单位，如果没有填null;
        	String OTime = "yyyy-MM-dd HH:mm:ss";//资料时间（必填）
        	String InTime = "yyyy-MM-dd HH:mm:ss.SSS";//资料接入时间（必填）
        	String STime = "yyyy-MM-dd HH:mm:ss.SSS";//接口发送时间（必填）
        	String FileType = "O";//O为观测类；R为状态类
        	String DataType = null;//AWS-自动气象站地面气象要素资料；AWS-SS_DAY日照观日值测资料；AWS-PRF：降水观测资料; AWS_DAY;日值观测表；不确定或没有填
        	String FileName = file.getName();//资料文件名（必填）
        	String BBB = null;//更正标识eg：CCX。数据更正标识，可选标志，CC为固定代码；x取值为A～X；如果资料不是更正报
        	int PQC = 0;//质量控制标识 =1 已质控 =0 未质控
            String NasPath = fullName;//原始文件所在路径（必填）
            String Format = "TXT";//文件格式
            long FileSize = 944746;//文件大小
            String MD5 = null;//校验码
            int length = 1;//数据块长度，有必要时填写具体数值
            byte[] Data = buf;
            
            map.put("TypeTag", TypeTag);
            map.put("TYPE", TYPE);
            map.put("IIIII", IIIII);
            map.put("CCCC", CCCC);
            map.put("OTime", OTime);
            map.put("InTime", InTime);
            map.put("STime", STime);
            map.put("FileType", FileType);
            map.put("DataType", DataType);
            map.put("FileName", FileName);
            map.put("BBB", BBB);
            map.put("PQC", PQC);
            map.put("NasPath", NasPath);
            map.put("Format", Format);
            map.put("FileSize", FileSize);
            map.put("MD5", MD5);
            map.put("length", length);
//            map.put("Data", Data);
            
            
            String js = JSONObject.toJSONString(map,SerializerFeature.WriteMapNullValue);//获取json字符串
            
            ProducerRecord<String,byte[]> record = new ProducerRecord<String, byte[]>(TYPE,js,Data);
            producer.send(record);
            
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		 
			try {
				bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        
		
	}
	
	public void testNcep(String topic,String fullName ) {
		  try {

			File file = new File(fullName);

			// 消息的value
			String messageValue = "F:/dongao/data/H8/HS_H08_20181106_2310_B10_FLDK_R20_S0510.DAT";
			String filename = "HS_H08_20181106_2310_B10_FLDK_R20_S0510.DAT";
			Map<String, Object> js = new LinkedHashMap<>();
			js.put("TypeTag", 1);
			js.put("TYPE", topic);
			js.put("ProjType", null);
			js.put("STime", "20190610120000");
			js.put("DataType", null);
			js.put("InTime", null);
			js.put("OTime", null);
			js.put("FileName", file.getName());
			js.put("NasPath", fullName);
			js.put("Format", "DAT");
			js.put("FileSize", "696");
			js.put("MD5", " 3b00b1249bbe3551");
			String str = JSONObject.toJSONString(js, SerializerFeature.WriteMapNullValue);
//	            Map<String, String> map = new HashMap<String, String>();
//	            map.put("filePath", messageValue);
//	            map.put("filename", filename);
//	            String js = JSONObject.toJSONString(map);

			ProducerRecord<String, byte[]> record = new ProducerRecord<>(topic, str.getBytes());
			producer.send(record);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// producer.close();
		}
      
		
	}
	
	public void testNcepRamp( ) {
		SendKafkaTest produce = new SendKafkaTest();
		// 发送发字节数组
		
		JSONObject js = new JSONObject(true);
		js.put("messageID", "Z_SURF_C_BCCD_20190501000125_O_AWS_FTM_PQC");
		js.put("datetype", "NAT_HOR");
		js.put("fromto", "CTS");
		js.put("sendto", "SOD");
		js.put("datatime", "20190501000125");
		
		js.put("sendtime", "20190501000125");
		js.put("version", "1.0");
		js.put("prio", "1");
		js.put("length", "920");
		js.put("encoding", "text/plain");
		
		//js.put("data", getBytes("D:\\data\\Z_SURF_C_BEPK_20181001120000_O_AWS_DAY.txt"));
		js.put("data",  "D:\\data\\RAMPSIN\\ANA-2019030416_201903041600_201903041559.nc");
		js.put("checknum", "861f942e8993b09076bfc2d524284f10");
		
		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);  
		
		//F.0045.0001.S001 RAMPSIN
      
        ProducerRecord<String, byte[]> msgtar = new ProducerRecord<String, byte[]>("F.0045.0001.S001","ANA-2019030416_201903041600_201903041559.nc", str.getBytes());
        producer.send(msgtar);
       
	}
	
	//A.0012.0001.S001.nation
	public void testBabj(String topic) {
		SendKafkaTest produce = new SendKafkaTest();
		// 发送发字节数组
		
		JSONObject js = new JSONObject(true);
		js.put("messageID", "Z_SURF_C_BCCD_20190501000125_O_AWS_FTM_PQC");
		js.put("datetype", "NAT_HOR");
		js.put("fromto", "CTS");
		js.put("sendto", "SOD");
		js.put("datatime", "20190501000125");
		
		js.put("sendtime", "20190501000125");
		js.put("version", "1.0");
		js.put("prio", "1");
		js.put("length", "920");
		js.put("encoding", "text/plain");
		
		//js.put("data", getBytes("D:\\data\\Z_SURF_C_BEPK_20181001120000_O_AWS_DAY.txt"));
		js.put("data",  "D:\\data\\RAMPSIN\\ANA-2019030416_201903041600_201903041559.nc");
		js.put("checknum", "861f942e8993b09076bfc2d524284f10");
		
		String str = JSONObject.toJSONString(js,SerializerFeature.WriteMapNullValue);  
		
		//F.0045.0001.S001 RAMPSIN
      
        ProducerRecord<String, byte[]> msgtar = new ProducerRecord<String, byte[]>(topic,"ANA-2019030416_201903041600_201903041559.nc", str.getBytes());
        producer.send(msgtar);
      
		
	}
	
	
	   
	public void testMw(String topic, String fullName) {

		BufferedReader bufr = null;
		try {
//	        	String filePath = "/Users/zm/data/54406_2018-05-25_00-00-00_lv1.csv";//原始文件所在路径（必填）
			File file = new File(fullName);
			String line = null;
			StringBuffer sb = new StringBuffer();
			bufr = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			while ((line = bufr.readLine()) != null) {
				sb.append(line + "\r\n");
			}
			byte[] buf = sb.toString().getBytes();
			// kafka消息key(模拟)
			Map<String, Object> map = new LinkedHashMap<String, Object>();// 用于存储kafka消息key的集合

			int TypeTag = 1;// 是否为四级资料编码：1 是；0否（必填）
			String TYPE = "B.0030.0002.S001";// TYPETag =1四级资料编码;TYPETag = 0 自定义资料编码（必填）
			String IIIII = null;// 台站编号：单一台站填写台站编号，多个或者无台站信息填null
			String CCCC = null;// 编报中心：台站上级单位，如果没有填null;
			String OTime = "yyyy-MM-dd HH:mm:ss";// 资料时间（必填）
			String InTime = "yyyy-MM-dd HH:mm:ss.SSS";// 资料接入时间（必填）
			String STime = "yyyy-MM-dd HH:mm:ss.SSS";// 接口发送时间（必填）
			String FileType = "O";// O为观测类；R为状态类
			String DataType = null;// AWS-自动气象站地面气象要素资料；AWS-SS_DAY日照观日值测资料；AWS-PRF：降水观测资料; AWS_DAY;日值观测表；不确定或没有填
			String FileName = file.getName();// 资料文件名（必填）
			String BBB = null;// 更正标识eg：CCX。数据更正标识，可选标志，CC为固定代码；x取值为A～X；如果资料不是更正报
			int PQC = 0;// 质量控制标识 =1 已质控 =0 未质控
			String NasPath = fullName;// 原始文件所在路径（必填）
			String Format = "CSV";// 文件格式
			long FileSize = file.length();// 文件大小
			String MD5 = null;// 校验码
			int length = 1;// 数据块长度，有必要时填写具体数值
			byte[] Data = buf;

			map.put("TypeTag", TypeTag);
			map.put("TYPE", TYPE);
			map.put("IIIII", IIIII);
			map.put("CCCC", CCCC);
			map.put("OTime", OTime);
			map.put("InTime", InTime);
			map.put("STime", STime);
			map.put("FileType", FileType);
			map.put("DataType", DataType);
			map.put("FileName", FileName);
			map.put("BBB", BBB);
			map.put("PQC", PQC);
			map.put("NasPath", NasPath);
			map.put("Format", Format);
			map.put("FileSize", FileSize);
			map.put("MD5", MD5);
			map.put("length", length);
//	            map.put("Data", Data);

			String js = JSONObject.toJSONString(map, SerializerFeature.WriteMapNullValue);// 获取json字符串

			ProducerRecord<String, byte[]> record = new ProducerRecord<String, byte[]>(topic, js, Data);
			producer.send(record);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// producer.close();
			try {
				bufr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static byte[] getBytes(String filePath){

        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

}
