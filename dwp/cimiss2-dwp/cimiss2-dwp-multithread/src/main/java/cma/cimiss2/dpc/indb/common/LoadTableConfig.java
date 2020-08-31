package cma.cimiss2.dpc.indb.common;

import cma.cimiss2.dpc.indb.vo.PathInfo;
import cma.cimiss2.dpc.indb.vo.TableConfig;
import org.cimiss2.dwp.tools.utils.ConfigurationManager;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <br>
 * @Title:  LoadTableConfig.java   
 * @Package org.cimiss2.dwp.RADAR.config   
 * @Description:    TODO(读取入表策略配置)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月14日 上午9:01:49   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class LoadTableConfig {
	public static final Logger logger = LoggerFactory.getLogger("loggerInfo");
	private static LoadTableConfig loadTableConfig = null;
	private Map<String, TableConfig> tablesMaps = null;
	private LoadTableConfig(String confPath) {
		tablesMaps = new HashMap<String, TableConfig>();
		
		SAXReader saxReader = new SAXReader();
		try {
			Document document = saxReader.read(new File(confPath));
		
	        Element root = document.getRootElement();
	        
	        @SuppressWarnings("unchecked")
			List<Element> childList = root.elements();
	        for (int i = 0; i < childList.size(); i++)
	        {
	        	TableConfig tableConfig = new TableConfig();
	        	Element dataType = childList.get(i).element("dataType");
	        	tableConfig.setDataType(dataType.getStringValue());

	        	/**
	        	  * RabbitMQ.queueName
	        	  **/
	        	Element queueName = childList.get(i).element("queueName");
	        	tableConfig.setQueueName(queueName.getStringValue());
	   
	        	Element moveFileType = childList.get(i).element("moveFileType");
	        	tableConfig.setMoveFileType(moveFileType.getStringValue());

	        	Element fileloop = childList.get(i).element("fileloop");
	        	tableConfig.setFileloop(fileloop.getStringValue());

	        	Element storeType = childList.get(i).element("storeType");
	        	tableConfig.setStoreType(storeType.getStringValue());
	        	
	        	Element indexTable = childList.get(i).element("indexTable");
	        	tableConfig.setIndexTable(indexTable.getStringValue());
	        	
	        	// fixme 不清楚配置信息作用，暂时注释 2020/8/14 17:01
//	        	Element AtsTable = childList.get(i).element("AtsTable");
//	        	tableConfig.setAtsTable(AtsTable.getStringValue());
	        	
	        	Element sodDataType = childList.get(i).element("sodDataType");
	        	tableConfig.setSodDataType(sodDataType.getStringValue());
	        	
	        	Element splitRegex = childList.get(i).element("splitRegex");
	        	tableConfig.setSplitRegex(splitRegex.getStringValue());
	        	
	        	Element retweetDir = childList.get(i).element("retweetDir");
	        	tableConfig.setRetweetDir(retweetDir.getStringValue());
	        	
	        	Element storyPath = childList.get(i).element("storyPath");
	        	tableConfig.setStoryPath(storyPath.getStringValue());
	        	
	        	/*Element storyPath = childList.get(i).element("storyPath");
	        	List<PathInfo> pathInfoList = parsePath(storyPath.getStringValue());
	        	if (pathInfoList != null) {
	        		tableConfig.setStoryPath(pathInfoList);
				} else {
					logger.error("\n 解析资料" + dataType +"存储路径失败!");
					continue;
				}*/
	        	
	        	Element newFileName = childList.get(i).element("newFileName");
	        	tableConfig.setNewFileName(newFileName.getStringValue());
	        	
	        	Element diSendFlag = childList.get(i).element("diSendFlag");
	        	tableConfig.setDiSendFlag(diSendFlag.getStringValue());
	        	
	        	tablesMaps.put(dataType.getStringValue(), tableConfig);
			}      
		} catch (DocumentException e) {
			logger.error("读取配置文件错误：" + e.getMessage());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("读取配置文件错误：" + e.getMessage());
		}
	}

	public static LoadTableConfig getInstance() {
		if(loadTableConfig == null) {
			String resource = ConfigurationManager.getJarSuperPath()+"config/tables.xml";
			loadTableConfig = new LoadTableConfig(resource);
		}
		return loadTableConfig;
	}
	
	private List<PathInfo> parsePath(String strFilePath) {
		List<PathInfo> PathInfoList = new ArrayList<PathInfo>();
		try {
			if(!strFilePath.isEmpty()) {
				//配置内容为资料目录处理策略
				String[] path_items = strFilePath.split("/");
				for (int i = 1; i < path_items.length; i++) {
					PathInfo pathInfo = new PathInfo();
					if(!isPos(path_items[i])) { //处理固定路径字符串,如RADA
						pathInfo.setType(0);
						pathInfo.setStr(path_items[i]);
					}
					else
					{ //处理动态路径
						if(isCatPos(path_items[i])) { //处理截断路径，如[4(0,4)]
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
				}
			} else {
				logger.error("\n 资料存储路径为null");
				return null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("\n 解析资料存储路径失败："+ strFilePath + " : " + e.getMessage());
			return null;
		}
		return PathInfoList;
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
	public boolean isPos(String v) {
		String str = v;
		int len = v.length();
		if(str.charAt(0) == '[' && str.charAt(len - 1) == ']')
			return true;
		else
			return false;
	}
	
	public static void main(String[] args) {
		LoadTableConfig.getInstance();
	}

	public Map<String, TableConfig> getTablesMaps() {
		return tablesMaps;
	}
}
