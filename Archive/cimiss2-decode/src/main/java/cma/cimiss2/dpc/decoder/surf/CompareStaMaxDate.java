package cma.cimiss2.dpc.decoder.surf;
//package cma.cimiss2.dpc.decoder.surf.ncdc_gsod;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Properties;
//
//public class CompareStaMaxDate {
//	public static Map<String, String> staMap = new HashMap<String, String>();
//	public static String fileProperties = "config/suf_ncdc_gsod/StationTools/surf_gsodStations_maxDate.properties";
//	private static Properties props = new Properties();
//	/**
//	 * 静态代码块将文件的初始化数据加载到缓存中
//	 */
//	static {
//		try {
//			// 判断文件是否存在，如果存在去把文件中的数据加载到Map中
//			boolean flag = createFile(fileProperties);
//			if (flag) {
//				staMap = readValue(fileProperties);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	public static Map<String, String> getStaMap() {
//		return staMap;
//	}
//
//	/**
//	 * 创建文件
//	 * 
//	 * @param fileName
//	 * @return
//	 */
//	public static boolean createFile(String filePath) throws Exception {
//		boolean flag = false;
//		try {
//			File fileName = new File(filePath);
//			if (!fileName.exists()) {
//				flag =fileName.createNewFile();
//			}else{
//				flag=true;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return flag;
//	}
//
//	/**
//	 * 根据主键key读取主键的值value
//	 * 
//	 * @param filePath
//	 *            属性文件路径
//	 * @param key
//	 *            键名
//	 */
//	public static Map<String, String> readValue(String filePath) {
//		Properties props = new Properties();
//		InputStream in = null;
//		try {
//			in = new BufferedInputStream(new FileInputStream(filePath));
//			props.load(in);
//			staMap = (Map) props;
//			in.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (null != in) {
//					in.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return staMap;
//	}
//
//	/**
//	 * 更新properties文件的键值对 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插件一对键值。
//	 * 
//	 * @param keyname
//	 *            键名
//	 * @param keyvalue
//	 *            键值
//	 */
//	public static void AddOrUpdateProperties(ArrayList<Map<String, String>> list) {
//		OutputStream fos = null;
//		try {
//			Map<String, String> tempMap = new HashMap<String, String>();
//			props.load(new FileInputStream(fileProperties));
//			// 调用 Hashtable 的方法 put，使用 getProperty 方法提供并行性。
//			// 强制要求为属性的键和值使用字符串。返回值是 Hashtable 调用 put 的结果。
//			fos = new FileOutputStream(fileProperties);
//			for (int i = 0; list.size() > i; i++) {
//				tempMap = (HashMap<String, String>) list.get(i);
//				Iterator it = tempMap.keySet().iterator();
//				while (it.hasNext()) {
//					String key;
//					String value;
//					key = it.next().toString();
//					value = tempMap.get(key);
//					props.setProperty(key, value);
//				}
//
//			}
//			// 以适合使用 load 方法加载到 Properties 表中的格式，
//			// 将此 Properties 表中的属性列表（键和元素对）写入输出流
//			props.store(fos, "Update value");
//			fos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (null != fos) {
//					fos.close();
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}