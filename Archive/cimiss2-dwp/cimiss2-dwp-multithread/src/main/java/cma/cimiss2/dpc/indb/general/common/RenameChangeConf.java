package cma.cimiss2.dpc.indb.general.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * <br>
 * @Title:  RenameChangeConf.java   
 * @Package org.cimiss2.dwp.rada_sate.common   
 * @Description:    TODO(卫星重命名文件名转换配置信息)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年5月21日 下午14:52:14   wufy    Initial creation.
 * </pre>
 * 
 * @author wufy
 *
 *
 */
public class RenameChangeConf {
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo"); //消息处理日志
	//文件名字段转换配置  <转换名称, HashMap<原值,转换值>>
	public static HashMap < String, HashMap <String, String> > sm_changeConf; //文件名中特殊转换配置
	
	/**
	 * 函数名：ReadConfig
	 * @param fileName 文件对象     
	 * 
	 * @return HashMap < String, HashMap <String, String> > sm_changeConf 文件名中特殊转换配置
	 */
	public static boolean ReadConfig(String fileName)
	{
		HashMap <String, String> mapValChange = null; // HashMap<原值、转换后值>
		//加载配置文件
		BufferedReader br = null;
		try {
			br  = new BufferedReader(new FileReader(fileName));
			sm_changeConf = new HashMap<String, HashMap <String, String>>();
			String strLine = "";
			String strChangeName = "";
			while((strLine = br.readLine()) != null){ // 判断文件是否结束
				strLine = strLine.trim();
				if(!strLine.isEmpty()){ //不为空
					if(strLine.startsWith("//") || strLine.startsWith("#")) {//判断文件是否为注释行
						continue;
					}
					else if(strLine.startsWith("[") && strLine.endsWith("]"))
					{
						//获取转换名称
						strChangeName = strLine.substring(1, strLine.length()-1);
						mapValChange = new HashMap <String, String>();
					} 
					else if(strLine.equalsIgnoreCase("NNNN")) //某类四级编码结束
					{
						sm_changeConf.put(strChangeName,mapValChange);
					}
					else {
						String [] strValChange = strLine.split("	");
						mapValChange.put(strValChange[0].trim(),strValChange[1].trim());
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
		} catch(Exception e) {
			e.printStackTrace();
			logger.error(fileName + "配置文件解析失败:" + e.getMessage());
			return false;
		} finally {
			if(br != null) {
				try {
					br.close();
					br = null;
				} catch(IOException e) {
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
		}
		return true;
	}

	public static void main(String [] args)
	{
		boolean bSuc = RenameChangeConf.ReadConfig("config/ChangeFile.txt");
		System.out.println(RenameChangeConf.sm_changeConf.get("M"));
		if (bSuc) {
			System.out.println("Read ChangeFile.txt success!");
		} else {
			System.out.println("Read ChangeFile.txt error!");
		}
	}
}
