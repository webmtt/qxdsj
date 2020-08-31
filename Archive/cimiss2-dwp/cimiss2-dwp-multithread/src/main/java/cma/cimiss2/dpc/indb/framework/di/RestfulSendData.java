package cma.cimiss2.dpc.indb.framework.di;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
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
public class RestfulSendData {
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static String targetEIURL = null;
	private static String targetDIURL = null;

	public RestfulSendData() {

	}

	/**
	 * @Title: loadConf @Description: TODO(加载配置文件) @param: @return: void @throws
	 */
	private static void loadConf() {
		File file = new File("config/RestfulInfo.js");
		if (file.isFile() && file.exists()) {
			if (targetDIURL == null || targetEIURL == null) {
				targetDIURL = (String) Json.fromJsonFile(HashMap.class, file).get("targetDIURL");
				targetEIURL = (String) Json.fromJsonFile(HashMap.class, file).get("targetEIURL");
			}
		} else {
			infoLogger.error("config file not fond:" + "config/RestfulInfo.js");
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
			if(StartConfig.isDILog()) {
				infoLogger.info(input);
			}
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
			if (httpConnection.getResponseCode() != 200) {
				infoLogger.error("send DI or EI ERROR ：" + input);
				infoLogger.error("Failed : HTTP error code : " + httpConnection.getResponseCode());
			}else {
				infoLogger.info("send DI success : " + httpConnection.getResponseCode());
			}

		} catch (IOException e) {
			e.printStackTrace();
			iRet = -1;
			infoLogger.error("EI AND DI ERROR ：" + targetEIURL);
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



}
