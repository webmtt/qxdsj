package cma.cimiss2.dpc.indb.common;

import cma.cimiss2.dpc.indb.vo.PathInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * 加载雷达卫星文件的存储目录策略配置文件
 * 返回结果集：Map<String, List<PathInfo>>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 12/7/2017   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 * 
 */
public class LoadRetWeetConfig {
	public static final Logger logger = LoggerFactory.getLogger(LoadRetWeetConfig.class);
	private String m_RootPath = "";	//资料数据存储根目录
	private Map<String, List<PathInfo>> m_SubPath; //资料数据存储子目录策略(动态创建),Map<cts四级编码,子目录创建策略>

	/**
	 * 函数名：LoadConfig
	 * 
	 * @param fileName
	 * 		配置文件路径
	 * 
	 * @return Map<String, List<PathInfo>> m_TypePath 各类资料数据存储目录策略
	 */
	public boolean LoadConfig(String fileName){
		//加载资料存储目录配置文件
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
			m_SubPath = new HashMap<String, List<PathInfo>>();
			String strLine = "";
			while((strLine = br.readLine()) != null){
				// 判断文件是否结束
				if(!strLine.isEmpty()){
					
					if(strLine.startsWith("/")) //判断文件是否为注释行
						continue;
					//获取数据列表信息字符串
					int index_f1 = strLine.indexOf("=");
					int index_f2 = strLine.indexOf(";", index_f1 + 1);
					if(index_f1 == -1 || index_f2 == -1) {
						System.out.println("Load Config File " + fileName + "Error,Error Line:" + strLine + " Not Contain '=' or ';'");
						logger.error("Load Config File " + fileName + "Error,Error Line:" + strLine + " Not Contain '=' or ';'");
						continue;
					}
					else{
						String key = strLine.substring(0, index_f1);
						String value = strLine.substring(index_f1 + 1, index_f2);
						// 配置内容为资料目录
						if(key.equalsIgnoreCase("RootPath")) {
							m_RootPath = value;
						}	
						else { //配置内容为资料目录处理策略
							List<PathInfo> PathInfoList = new ArrayList<PathInfo>();
							String[] path_items = value.split("/");
							for (int i = 0; i < path_items.length; i++) {
								try {
									PathInfo pathInfo = new PathInfo();
									if(!isPos(path_items[i])) { //处理固定路径字符串,如RADA
										pathInfo.setType(0);
										pathInfo.setStr(path_items[i]);
									}
									else { //处理动态路径
										if(isCatPos(path_items[i])){ //处理截断路径，如[4(0,4)]
											int z1 = path_items[i].indexOf("[");
											int g1 = path_items[i].indexOf("(");
											int d1 = path_items[i].indexOf(",");
											int g2 = path_items[i].indexOf(")");
											if (d1==-1) { //截断位置信息(4)
												pathInfo.setType(2);
												pathInfo.setPos(Integer.parseInt(path_items[i].substring(z1 + 1, g1)));
												pathInfo.setStart(0);
												pathInfo.setEnd(Integer.parseInt(path_items[i].substring(g1 + 1, g2)));
											} else { //双截断位置信息(0,4)
												pathInfo.setType(3);
												pathInfo.setPos(Integer.parseInt(path_items[i].substring(z1 + 1, g1)));
												pathInfo.setStart(Integer.parseInt(path_items[i].substring(g1 + 1, d1)));
												pathInfo.setEnd(Integer.parseInt(path_items[i].substring(d1 + 1, g2)));
											}
										}
										else { //处理[index],如[8]
											int z1 = path_items[i].indexOf("[");
											int z2 = path_items[i].indexOf("]");
											pathInfo.setType(1); //位置信息
											pathInfo.setPos(Integer.parseInt(path_items[i].substring(z1 + 1, z2)));
										}
									}
									PathInfoList.add(pathInfo);
								} catch (Exception e) {
									logger.error(e.getMessage());
									break;
								}
							}
							m_SubPath.put(key, PathInfoList);
						}
					}
				}
			}	
		} catch (FileNotFoundException e) {
			logger.error("\n 配置文件不存在："+ fileName + " : " + e.getMessage());
			return false;
		} catch (IOException e) {
			logger.error("\n 读取配置文件错误："+ fileName + " : " +e.getMessage());
			return false;
		} finally {
			try {
				br.close();
				br = null;
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return true;
	}
	
	/**
	 * 函数名：isCatPos
	 * 判断判断该路径字符串  是否为文件名中分割出来的 整段内容的部分
	 * @param v
	 *            字符串对象
	 * 
	 * @return true/false
	 */
	public boolean isCatPos(String v){
		if(!isPos(v))
			return false;
		if(v.contains("(") && v.contains(")"))
			return true;
		
		return false;
	}
		
	/**
	 * 函数名：isPos
	 * 判断该路径字符串  是否为文件名中分割出来的 整段内容
	 * @param v 
	 *            字符串
	 * 
	 * @return true/false
	 * 
	 */
	public boolean isPos(String v){
		String str = v;
		int len = v.length();
		if(str.charAt(0) == '[' && str.charAt(len - 1) == ']')
			return true;
		else
			return false;
	}

	public String getRootPath() {
		return m_RootPath;
	}

	public void setRootPath(String rootPath) {
		this.m_RootPath = rootPath;
	}

	public Map<String, List<PathInfo>> getSubPath() {
		return m_SubPath;
	}

	public void setSubPath(Map<String, List<PathInfo>> SubPath) {
		this.m_SubPath = SubPath;
	}

}
