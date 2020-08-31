package cma.cimiss2.dpc.indb.general.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.indb.general.vo.Pair;

/**
 * 
 * <br>
 * 
 * @Title: IndexConf.java
 * @Package org.cimiss2.dwp.rada_sate.common
 * @Description: TODO(读取入库策略配置信息)
 * 
 *               <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月14日 上午9:01:14   wuzuoqiang    Initial creation.
 *               </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class IndexConf {
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo"); // 消息处理日志
	// 存储索引表配置 <表名, vector<Pair<字段名 取值表达式>>>
	public static HashMap<String, Vector<Pair<String, String>>> sm_indexConf;

	/**
	 * 函数名：ReadConfig
	 * 
	 * @param fileName 文件对象
	 * 
	 * @return HashMap < String, Vector < Pair<String, String> > > sm_indexConf
	 *         索引配置信息
	 */
	public static boolean ReadConfig(String fileName) {
		Vector<Pair<String, String>> vecFields = null;
		Pair<String, String> pairFieldVal;
		// 加载配置文件
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			sm_indexConf = new HashMap<String, Vector<Pair<String, String>>>();
			String strLine = "";
			String strCtsType = "";
			while ((strLine = br.readLine()) != null) { // 判断文件是否结束
				strLine = strLine.trim();
				if (!strLine.isEmpty()) { // 不为空
					if (strLine.startsWith("#")) {// 判断文件是否为注释行
						continue;
					} else if (strLine.startsWith("[") && strLine.endsWith("]")) {
						// 获取CTS四级编码
						strCtsType = strLine.substring(1, strLine.length() - 1);
						vecFields = new Vector<Pair<String, String>>();
					} else if (strLine.equalsIgnoreCase("NNNN")){
						sm_indexConf.put(strCtsType, vecFields);
					} else {
						String[] strFieldVal = strLine.split("=");
						pairFieldVal = Pair.valueOf(strFieldVal[0], strFieldVal[1]);
						vecFields.add(pairFieldVal);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(fileName + "配置文件不存在:" + e.getMessage());
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(fileName + "配置文件打开失败:" + e.getMessage());
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(fileName + "配置文件解析失败:" + e.getMessage());
			return false;
		} finally {
			if (br != null) {
				try {
					br.close();
					br = null;
				} catch (IOException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
		return true;
	}
	
	
	public static void main(String[] args) {
		System.out.println(IndexConf.ReadConfig("config/index.txt"));
		for (String string : IndexConf.sm_indexConf.keySet()) {
			System.out.println(string);
		}
	}
}
