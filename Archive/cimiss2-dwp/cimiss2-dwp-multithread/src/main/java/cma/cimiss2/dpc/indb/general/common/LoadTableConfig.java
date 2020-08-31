package cma.cimiss2.dpc.indb.general.common;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cma.cimiss2.dpc.indb.general.vo.PathInfo;
import cma.cimiss2.dpc.indb.general.vo.TableConfig;

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
	        	Element tableElement = childList.get(i);
	        	@SuppressWarnings("unchecked")
				List<Element> tableElements = tableElement.elements();
	        	TableConfig tableConfig = new TableConfig();
	        	for (int j = 0; j < tableElements.size(); j++) {
	        		
		        	if(tableElements.get(j).getName().equalsIgnoreCase("dataType")) {
		        		Element dataType = tableElements.get(j);
			        	tableConfig.setDataType(dataType.getStringValue());
			        	
		        	}else if (tableElements.get(j).getName().equalsIgnoreCase("storeType")) {
		        		Element storeType = tableElements.get(j);
			        	tableConfig.setStoreType(storeType.getStringValue());
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("indexTable")) {
						Element indexTable = tableElements.get(j);
			        	tableConfig.setIndexTable(indexTable.getStringValue());
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("AtsTable")) {
						Element AtsTable = tableElements.get(j);
			        	tableConfig.setAtsTable(AtsTable.getStringValue());
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("sodDataType")) {
						Element sodDataType = tableElements.get(j);
			        	tableConfig.setSodDataType(sodDataType.getStringValue());
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("ctsDataType")) {
						Element ctsDataType = tableElements.get(j);
			        	tableConfig.setCtsDataType(ctsDataType.getStringValue());
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("splitRegex")) {
						Element splitRegex = tableElements.get(j);
			        	tableConfig.setSplitRegex(splitRegex.getStringValue());
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("retweetDir")) {
						Element retweetDir = tableElements.get(j);
			        	tableConfig.setRetweetDir(retweetDir.getStringValue());
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("stationCode")) {
						Element stationCode = tableElements.get(j);
			        	tableConfig.setStationCode(stationCode.getStringValue());
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("storyPath")) {
						Element storyPath = tableElements.get(j);
			        	List<PathInfo> pathInfoList = parsePath(storyPath.getStringValue());
			        	if (pathInfoList != null) {
			        		tableConfig.setStoryPath(pathInfoList);
						} else {
							logger.error("\n 解析资料" + tableConfig.getSodDataType() +"存储路径失败!");
							continue;
						}
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("newFileName")) {
						Element newFileName = tableElements.get(j);
			        	tableConfig.setNewFileName(newFileName.getStringValue());
			        	
					}else if (tableElements.get(j).getName().equalsIgnoreCase("diSendFlag")) {		        	
			        	Element diSendFlag = tableElements.get(j);
			        	tableConfig.setDiSendFlag(diSendFlag.getStringValue());
					}
		        	
				}
	        	if(tablesMaps.containsKey(tableConfig.getDataType())) {
	        		System.out.println(tableConfig.getDataType());
	        	}
//	        	System.out.println(tableConfig.getDataType());
	        	tablesMaps.put(tableConfig.getDataType(), tableConfig);
	        	
			}      
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.error("读取配置文件错误：" + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("读取配置文件错误：" + e.getMessage());
		}
	}
	

	public static LoadTableConfig getInstance() {
		if(loadTableConfig == null) {
			String resource = "config/tables.xml";
//			String resource = "D:\\cmadass\\testdata\\2019042512\\tables.xml";
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
		System.out.println(IndexConf.ReadConfig("config/index.txt"));
		for (String key : LoadTableConfig.getInstance().getTablesMaps().keySet()) {
			if(!IndexConf.sm_indexConf.containsKey(key)) {
				System.out.println(key + "              " + LoadTableConfig.getInstance().getTablesMaps().get(key).getIndexTable() );
			}
			
		}
//		System.out.println(LoadTableConfig.getInstance().getTablesMaps().containsKey("F.0027.0003.S003"));
	}

	public Map<String, TableConfig> getTablesMaps() {
		return tablesMaps;
	}
}
