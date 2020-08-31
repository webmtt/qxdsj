package cma.cimiss2.dpc.indb.core.tools;

import org.nutz.json.Json;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: DIEI工具
 * @Aouthor:
 * @create:
 */
public class RestfulSendData {
	/**
	 * di的url
	 */
	private static String targetDIURL = null;

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RestfulSendData.class);
	/**
	 * ei的url
	 */
	private static String targetEIURL = null;
	private volatile static RestfulSendData instance = null;

	private RestfulSendData(String path) {
		loadConf(path);
	}

	// 双层检测，在1.5以后Instance对象开始提供<span style="color:rgb(192, 80, 77);
	// font-size:14px">volatile</span><span
	// style="font-size:14px">关键字修饰变量来达到稳定效果</span>
	public static RestfulSendData getInstance(String path) {
		if (instance == null) {
			synchronized (RestfulSendData.class) {
				if (instance == null) {
					instance = new RestfulSendData(path);
				}
			}
		}
		return instance;
	}

	/**
	 * 读配置文件获取ei和di的url
	 *
	 * @param path
	 * @return
	 */
	private void loadConf(String path) {
		String jarWholePath = "";
		try {
			if ("".equals(path)) {
				jarWholePath = RestfulSendData.class.getProtectionDomain().getCodeSource().getLocation().getPath();
				try {
					jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					System.out.println(e.toString());
				}
				String jarCurrentPath = new File(jarWholePath).getParentFile().getAbsolutePath();
				jarCurrentPath = jarCurrentPath.replace("\\", "/");
				int i = jarCurrentPath.lastIndexOf("/") + 1;
				jarWholePath = jarCurrentPath.substring(0, i);
				jarWholePath = jarWholePath + "config/RestfulInfo.js";
			}
			else if(path.contains("RestfulInfo")){
				jarWholePath = path;
			}
			else {

				jarWholePath = path + "RestfulInfo.js";
			}
			File file = new File(jarWholePath);
			if (file.isFile() && file.exists()) {
				targetDIURL = (String) Json.fromJsonFile(HashMap.class, file).get("targetDIURL");
				targetEIURL = (String) Json.fromJsonFile(HashMap.class, file).get("targetEIURL");
			} else {
				System.out.print(jarWholePath + "配置文件不存在:"+"config/RestfulInfo.js");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 多线程调用
	 *
	 * @param obj   diei 集合
	 * @param judge 0 di , 1 ei
	 * @return
	 */
	public int SendData(Object obj, int judge) {
		int i = 0;
		if (judge == 0) {
			if (DIEISender.GLOBAL_DI_OPTION && DIEISender.LOCAL_DI_OPTION) {
				i = SendDataBatch(obj);
				return i;
			}
		}
		if (judge == 1) {
			if (DIEISender.GLOBAL_EI_OPTION && DIEISender.LOCAL_EI_OPTION) {
				i = SendDataBatch(obj);
				return i;
			}
		}

		return i;
	}

	/**
	 * 
	 *
	 * @param obj diei数据集
	 * @return 0成功 -1失败
	 */
	private int SendDataBash(Object obj) {
		int iRet = 0;
		HttpURLConnection httpConnection = null;
		try {
			String input = Json.toJson(obj);
			URL targetUrl = new URL(targetDIURL);
			try {
				List<RestfulInfo> obj1 = (List<RestfulInfo>) obj;
				if (obj1 != null && obj1.size() > 0 && obj1.get(0).getType().contains("DI")) {
					targetUrl = new URL(targetDIURL);
				} else if (obj1 != null && obj1.size() > 0 && obj1.get(0).getType().contains("EI")) {
					targetUrl = new URL(targetEIURL);
					 logger.info(input);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
//			URL targetUrl = new URL("http://10.20.67.178/store/openapi/v2/logs/push_batch?apikey=e10adc3949ba59abbe56e057f2gg88dd");
			httpConnection = (HttpURLConnection) targetUrl.openConnection();
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod("POST");
			httpConnection.setRequestProperty("Content-Type", "application/json");
//			String input = Json.toJson(obj);
			// 测试打印
//            System.out.println(input);
//            logger.info(input);
			OutputStream outputStream = httpConnection.getOutputStream();
			outputStream.write(input.getBytes());
			outputStream.flush();
			if (httpConnection.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
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

		return iRet;
	}

	private int SendDataBatch(Object obj) {
		int batchSize = 200;
		int iRet = 0;
		List<RestfulInfo> obj1 = (List<RestfulInfo>) obj;
		if (obj1 != null) {
			if (obj1.size() <= batchSize) {
				iRet = SendDataBash(obj);
			} else {
				int ceil = (int) Math.ceil(obj1.size() / Double.valueOf(batchSize));
				for (int i = 0; i < ceil; i++) {
					if (i == ceil - 1) {
						List<RestfulInfo> subList = obj1.subList(i * batchSize, obj1.size());
						iRet = SendDataBash(subList);
						continue;
					}
					List<RestfulInfo> subList = obj1.subList(i * batchSize, (i + 1) * (batchSize));
					iRet = SendDataBash(subList);
				}
			}
		}
		return iRet;
	}

	public static void main(String[] args) {
		System.out.println(RestfulSendData.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	}

}
