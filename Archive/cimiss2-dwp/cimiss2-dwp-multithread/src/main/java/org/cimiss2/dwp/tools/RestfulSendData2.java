package org.cimiss2.dwp.tools;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import org.cimiss2.dwp.tools.config.StartConfig;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ---------------------------------------------------------------------------------
 * 类描述：
 * 
 * @ClassName: RestfulSendData @date： 2017年12月27日 下午5:36:27
 * @version 1.0
 * 
 * 
 *          Version Date ModifiedBy Content -------- --------- ----------
 *          ------------------------ 1.0 2017年12月27日 wuzuoqiang
 *
 *
 * @author wuzuoqiang
 *         ---------------------------------------------------------------------------------
 */
public class RestfulSendData2 {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static String targetEIURL = null;
	private static String targetDIURL = null;

	public RestfulSendData2() {

	}

	/**
	 * @Title: loadConf @Description: TODO(加载配置文件) @param: @return: void @throws
	 */
	private static void loadConf() {
		File file = new File("config/RestfulInfo2.js");
		if (file.isFile() && file.exists()) {
			if (targetDIURL == null || targetEIURL == null) {
				targetDIURL = (String) Json.fromJsonFile(HashMap.class, file).get("targetDIURL");
				targetEIURL = (String) Json.fromJsonFile(HashMap.class, file).get("targetEIURL");
			}
		} else {
			infoLogger.error("config file not fond:" + "config/RestfulInfo2.js");
		}

	}

	/**
	 * 调用restful API 发送DI和EI信息 @Title: SendData @Description:
	 * TODO(这里用一句话描述这个方法的作用) @param: @param obj @param: @return @return: int @throws
	 */
	public static int SendData(Object obj) {
		int iRet = 0;
		HttpURLConnection httpConnection = null;
		try {

			if (targetEIURL == null) {
				loadConf();
			}
			URL targetUrl = new URL(targetEIURL);

			httpConnection = (HttpURLConnection) targetUrl.openConnection();

			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

//			httpConnection = HttpUrlConnector.getConnection();

			String input = Json.toJson(obj);
			if(StartConfig.isCimissDILog()) {
				infoLogger.info(input);
			}
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
			if (httpConnection.getResponseCode() != 200) {
				infoLogger.error("send Cimiss DI or EI ERROR ：" + input);
				infoLogger.error("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}else {
				infoLogger.info("send Cimiss DI success : " + httpConnection.getResponseCode());
			}

		} catch (IOException e) {
			e.printStackTrace();
			iRet = -1;
			infoLogger.error("Cimiss EI AND DI ERROR ：" + targetEIURL);
		}
//		catch (Exception e) {
//				iRet = -1;
//				infoLogger.error(e.getMessage() + targetEIURL);
//			}
		finally {
			if (httpConnection != null)
				httpConnection.disconnect();
//			HttpUrlConnector.releaseConnection(httpConnection);
		}

		return iRet;
	}

	/**
	 * @Title: SendData
	 * @Description: TODO(发送DI和EI信息)
	 * @param obj      需要发送的内容
	 * @param sendType 发送的类型
	 * @return int
	 * @throws:
	 */
	public static int SendData(Object obj, SendType sendType) {
		int iRet = 0;
		String targetURL = null;
		HttpURLConnection httpConnection = null;
		try {
			loadConf();

			if (sendType == SendType.DI)
				targetURL = targetDIURL;
			else {
				targetURL = targetEIURL;
			}
			URL targetUrl = new URL(targetURL);

			httpConnection = (HttpURLConnection) targetUrl.openConnection();

			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

//			httpConnection = HttpUrlConnector.getConnection();

			String input = Json.toJson(obj);
			if(StartConfig.isCimissDILog()) {
				infoLogger.info(input);
			}
//			System.out.println(input);
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
			infoLogger.info("Cimiss HTTP code : " + httpConnection.getResponseCode());
			
		} catch (IOException e) {
			e.printStackTrace();
			iRet = -1;
			infoLogger.error("Cimiss EI AND DI ERROR ：" + targetURL + e.getMessage());
		}
//		catch (Exception e) {
//			iRet = -1;
//			infoLogger.error(e.getMessage() + targetEIURL);
//		}
		finally {
			if (httpConnection != null)
				httpConnection.disconnect();
//			HttpUrlConnector.releaseConnection(httpConnection);
		}

		return iRet;
	}

	public static void main(String[] args) {
		int iRet = 0;
		Object object = null;
		SendData(object);
		HttpURLConnection httpConnection = null;
		try {
			URL targetUrl = new URL(
					"http://10.20.67.139/store/openapi/v2/logs/push_batch?apikey=e10adc3949ba59abbe56e057f2gg88dd");

			httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");

			// 测试打印
			String input = "{ \"type\": \"RT.DPC.STATION.DI\",\"name\":\"abc\"}";
			input = "";
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException(
						"Failed : HTTP error code : " + httpConnection.getResponseCode() + "\n" + input);
			} else {
				infoLogger.info("HTTP code: ", httpConnection.getResponseCode());
			}
		} catch (RuntimeException e) {
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
		} finally {
			if (httpConnection != null)
				httpConnection.disconnect();
		}
	}
}
