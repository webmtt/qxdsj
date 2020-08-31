package cma.cimiss2.dpc.decoder.grib.di_ei;

/**
 * @类名：RestfulSendData
 * @类功能：调用restful接口发送定时任务运行详情和定时数据加工处理详情信息(DI)
 * @作者：zhengbo
 * @版权：国家气象信息中心
 * @创建时间：2017-11-03
 * @变更：
 *
 */


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import org.apache.log4j.Logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.grib.GribConfig;

import org.nutz.json.Json;


public class RestfulSendData {
	//private static String  targetURL = (String)Json.fromJsonFile(HashMap.class, new File(getResourcePath("RestfulInfo.js"))).get("targetURL");
	private static String  targetURL_single = "http://smart-view.nmic.cma/store/openapi/v2/logs/push?apikey=e10adc3949ba59abbe56e057f2gg88dd";
	private static String  targetURL = "http://smart-view.nmic.cma/store/openapi/v2/logs/push_batch?apikey=e10adc3949ba59abbe56e057f2gg88dd";
	
	protected static final Logger logger = LoggerFactory.getLogger("gribLoggerInfo");
	protected static final Logger logger_e = LoggerFactory.getLogger("gribErrorInfo");
	
	public RestfulSendData() {
		
	}
	
	/*
	public static void main( String args[] )
	{
		RestfulInfo Rest_info = new RestfulInfo();
		Rest_info.setType("RT.DPC.FILE.DI");
		Rest_info.setName("文件级资料处理详细信息");
		Rest_info.setMessage("文件级资料处理详细信息");
		
		//测试文件：Z_NWGD_C_BABJ_20171027034907_P_RFFC_SCMOC-SSM_201710270800_07203.GRB2
		Field_Info filed_info = new Field_Info();
		filed_info.setDATA_TYPE("F.0044.0001.S001");
		filed_info.setDATA_TYPE_1("F.0044.0001.S001");
		filed_info.setRECEIVE("BABJ");
		filed_info.setSEND("NWFD");
		filed_info.setTRAN_TIME("2017-10-27 03:49:07");
		filed_info.setDATA_TIME("2017-10-27 08:00");
		filed_info.setSYSTEM("NWFD");
		filed_info.setPROCESS_LINK("4");
		filed_info.setPROCESS_START_TIME("2017-10-27 04:10:05");
		filed_info.setPROCESS_END_TIME("2017-10-27 04:10:25");
		filed_info.setFILE_NAME_O("Z_NWGD_C_BABJ_20171027034907_P_RFFC_SCMOC-SSM_201710270800_07203.GRB2");
		filed_info.setFILE_NAME_N("Z_NWGD_C_BABJ_20171027034907_P_RFFC_SCMOC-SSM_201710270800_07203.GRB2");
		filed_info.setPROCESS_STATE("0");
		filed_info.setBUSINESS_STATE("1");
		filed_info.setRECORD_TIME("2017-10-27 04:10:35");
		
		Rest_info.setFields(filed_info);
		
		List<RestfulInfo> rest_info_array = new ArrayList<RestfulInfo>();
		rest_info_array.add(Rest_info);
		rest_info_array.add(Rest_info);
		
		System.out.println("send restinfo data begin...");
		
		//SendData(filed_info);
		
		System.out.println("send restinfo data end...");
	}
	*/
	
	//批量发送
	public int SendData(Object obj)
	{
		int iRet = 0;
		HttpURLConnection httpConnection = null;
		try {			
			//logger.info("targetURL:" + GribConfig.gettargetURL() + "***");			
			URL target_Url = new URL(GribConfig.gettargetURL());			
			httpConnection = (HttpURLConnection) target_Url.openConnection();			
			//httpConnection.setDoInput(false);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			//httpConnection.setRequestProperty("Connection", "Keep-Alive");
			httpConnection.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
			//httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			
			httpConnection.setRequestProperty("Content-Type", "application/json");
			String input = Json.toJson(obj);
			//logger.info(input);
			OutputStream outputStream = httpConnection.getOutputStream();
			//System.out.println("111");
			outputStream.write(input.getBytes());
			outputStream.flush();
			outputStream.close();
			//System.out.println("222");
			
			logger.info("ResponseCode:" + httpConnection.getResponseCode());
			
			/*
			byte byte1[] = new byte[1024];
			InputStream instream = httpConnection.getInputStream();
			instream.read(byte1, 0, instream.available());
			System.out.println(new String(byte1,"utf-8"));
			*/
			
			if (httpConnection.getResponseCode() != 200) {
				//BufferedReader reader = new BufferedReader( new InputStreamReader(httpConnection.getInputStream()));
                //System.out.println(reader.readLine() + "***");
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}
			} catch(RuntimeException e) {
				iRet = -1;
				e.printStackTrace();
			} catch (MalformedURLException e) {
				iRet = -1;
				e.printStackTrace();
			} catch (IOException e) {
				iRet = -1;
				e.printStackTrace();
			} catch (Exception e) {
				iRet = -1;
				e.printStackTrace();
			} 
		finally {
			if(httpConnection!=null)httpConnection.disconnect();
		}
		
		return iRet;
	}
	
	//单条发送
	public int SendData_single(Object obj)
	{
		int iRet = 0;
		HttpURLConnection httpConnection = null;
		try {			
			//logger.info("targetURL_single:" + GribConfig.gettargetURL_single() + "***");			
			URL target_Url = new URL(GribConfig.gettargetURL_single());			
			httpConnection = (HttpURLConnection) target_Url.openConnection();			
			//httpConnection.setDoInput(false);
			httpConnection.setRequestMethod("POST");
			httpConnection.setDoOutput(true);
			//httpConnection.setRequestProperty("Connection", "Keep-Alive");
			httpConnection.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
			//httpConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
			
			httpConnection.setRequestProperty("Content-Type", "application/json");
			String input = Json.toJson(obj);
			//logger.info(input);
			OutputStream outputStream = httpConnection.getOutputStream();
			//System.out.println("111");
			outputStream.write(input.getBytes());
			outputStream.flush();
			outputStream.close();
			//System.out.println("222");
			
			logger.info("ResponseCode:" + httpConnection.getResponseCode());
			
			/*
			byte byte1[] = new byte[1024];
			InputStream instream = httpConnection.getInputStream();
			instream.read(byte1, 0, instream.available());
			System.out.println(new String(byte1,"utf-8"));
			*/
			
			if (httpConnection.getResponseCode() != 200) {
				//BufferedReader reader = new BufferedReader( new InputStreamReader(httpConnection.getInputStream()));
                //System.out.println(reader.readLine() + "***");
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}
			} catch(RuntimeException e) {
				iRet = -1;
				e.printStackTrace();
			} catch (MalformedURLException e) {
				iRet = -1;
				e.printStackTrace();
			} catch (IOException e) {
				iRet = -1;
				e.printStackTrace();
			} catch (Exception e) {
				iRet = -1;
				e.printStackTrace();
			} 
		finally {
			if(httpConnection!=null)httpConnection.disconnect();
		}
		
		return iRet;
	}
	
	public static String getResourcePath(String path)
	{
		if(!new File(path).exists()){
			if(new File("bin/" + path).exists()){
				path = "bin/" + path;
			}else if(new File("lib/" + path).exists()){
				path = "lib/" + path;
			}else if(new File("../lib/" + path).exists()){
				path = "../lib/" + path;
			}else if(new File("et/lib/" + path).exists()){
				path = "et/lib/" + path;
			}
		}
		return path;
	}
}

