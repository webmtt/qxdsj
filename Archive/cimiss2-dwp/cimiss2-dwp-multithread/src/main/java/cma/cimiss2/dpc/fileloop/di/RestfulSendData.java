package cma.cimiss2.dpc.fileloop.di;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import org.nutz.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ---------------------------------------------------------------------------------
 * 类描述：
 * 
 * @ClassName: RestfulSendData 
 * @date： 2017年12月27日 下午5:36:27
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
	private final static Logger logger = LoggerFactory.getLogger("messageInfo"); //消息日志
	private static String targetEIURL = null;
	private static String targetDIURL = null;

	public RestfulSendData() {

	}

	/**
	 * @Title: loadConf 
	 * @Description: TODO(加载配置文件) 
	 * @param: 
	 * @return: void 
	 * @throws
	 */
	private static void loadConf() {
		File file = new File("config/RestfulInfo.js");
		if (file.isFile() && file.exists()) {
			if (targetDIURL == null || targetEIURL == null) {
				targetDIURL = (String) Json.fromJsonFile(HashMap.class, file).get("targetDIURL");
				targetEIURL = (String) Json.fromJsonFile(HashMap.class, file).get("targetEIURL");
			}
		} else {
			logger.error("config file not fond:" + "config/RestfulInfo.js");
		}

	}

	/**
	 * 调用restful API 发送DI和EI信息 
	 * @Title: SendData 
	 * @Description: TODO(这里用一句话描述这个方法的作用) 
	 * @param: obj 
	 * @return: int 
	 * @throws
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
			String input = Json.toJson(obj);
			System.out.println(input);
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
			if (httpConnection.getResponseCode() != 200) {
				logger.error("send DI or EI ERROR ：" + input);
				logger.error("Failed : HTTP error code : " + httpConnection.getResponseCode());
			} else {
				logger.info("send DI or EI successfully, HTTP code = 200：\n" + input);
			}

		} catch (IOException e) {
			e.printStackTrace();
			iRet = -1;
			logger.error("EI AND DI ERROR ：" + targetEIURL);
		}

		finally {
			if (httpConnection != null)
				httpConnection.disconnect();
		}

		return iRet;
	}

}
