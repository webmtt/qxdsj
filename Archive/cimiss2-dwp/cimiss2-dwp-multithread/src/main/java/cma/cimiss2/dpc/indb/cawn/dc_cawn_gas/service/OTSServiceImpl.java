package cma.cimiss2.dpc.indb.cawn.dc_cawn_gas.service;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.StationInfo;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alicloud.openservices.tablestore.ClientException;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrSurfGlobalDecoder;
import com.hitec.bufr.decoder.BufrSurfGlobalMonDecoder;
import com.hitec.bufr.util.StringUtil;

import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;
public class OTSServiceImpl implements BufrService{
	static Map<String, Object> proMap = StationInfo.getProMap();
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> DI = new ArrayList<StatDi>();
	SimpleDateFormat ymdhm = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public static BlockingQueue<StatDi> getDiQueues(){
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueue){
		OTSServiceImpl.diQueues = diQueue;
	}
	
	public static void main(String[] args) {
		List<String> tableList = new ArrayList<String>();
		String tableSection = "A.0001.0022.R001";
		tableList.add("SURF_WEA_GLB_MUL_MON_TAB");
		String fileName = "C:\\Users\\huaxin\\Documents\\testFigure\\A_ISCD01LIIB050000RRA_C_BABJ_20190907160821_34013.bin";
		BufrServiceImpl bufrServiceImpl = new BufrServiceImpl();
		boolean decode = bufrServiceImpl.decode(fileName, tableSection, tableList);
		if(decode){
			System.out.println("入库成功！");
		}else
			System.err.println("入库失败！");
	}
	
	Map<String, List<Map<String, Object>>> ResultSet = new HashMap<String, List<Map<String, Object>>>();
	
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
		try {
			BufrSurfGlobalMonDecoder bufrDecoder = new BufrSurfGlobalMonDecoder();
			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
			if (isRead) {
				return this.decode(bufrDecoder, name, tableSection, tables);
			} else {
				infoLogger.error("\n Read file error: "+ name);
				String event_type = EIEventType.OP_FILE_ERROR.getCode();
				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
				if(ei == null) {
					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
				}else {
					if(StartConfig.isSendEi()) {
						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
						ei.setKObject("cma.cimiss2.dpc.indb.surf.bufr.service.OTSServiceImpl");
						ei.setKEvent("解码入库源文件异常：" + name);
						ei.setKIndex("详细信息：" + name);
						ei.setEVENT_EXT1(name);
						RestfulInfo restfulInfo = new RestfulInfo();
						restfulInfo.setType("SYSTEM.ALARM.EI ");
						restfulInfo.setName("数据解码入库EI告警信息");
						restfulInfo.setMessage("数据解码入库EI告警信息");
						
						restfulInfo.setFields(ei);
						List<RestfulInfo> restfulInfos = new ArrayList<>();
						restfulInfos.add(restfulInfo);
						RestfulSendData.SendData(restfulInfos, SendType.EI);
					}
				}
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
		BufrSurfGlobalMonDecoder bufrDecoder = new BufrSurfGlobalMonDecoder();
		boolean isRead = bufrDecoder.bufrDecoder(fileName);
		if (isRead) {
			return this.decode(bufrDecoder, fileName, tableSection, tables);
		} else {
			infoLogger.error("\n Read file error: "+ fileName);
			String event_type = EIEventType.OP_FILE_ERROR.getCode();
			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
			if(ei == null) {
				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
			}else {
				if(StartConfig.isSendEi()) {
					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
					ei.setKObject("cma.cimiss2.dpc.indb.surf.bufr.service.OTSServiceImpl");
					ei.setKEvent("解码入库源文件异常：" + fileName);
					ei.setKIndex("详细信息：" + fileName);
					ei.setEVENT_EXT1(fileName);
					RestfulInfo restfulInfo = new RestfulInfo();
					restfulInfo.setType("SYSTEM.ALARM.EI ");
					restfulInfo.setName("数据解码入库EI告警信息");
					restfulInfo.setMessage("数据解码入库EI告警信息");
					
					restfulInfo.setFields(ei);
					List<RestfulInfo> restfulInfos = new ArrayList<>();
					restfulInfos.add(restfulInfo);
					RestfulSendData.SendData(restfulInfos, SendType.EI);
				}
			}
			return false;
		}
	}

	public boolean decode(BufrSurfGlobalMonDecoder bufrDecoder, String fileName, String tableSection, List<String> tables) {
		ResultSet.clear();
		int successRowCount = 0;
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		File file = new File(fileName);
		String fileSimple = file.getName();
		boolean dateTimeOK = true;
		if(OTSServiceImpl.getDiQueues() == null)
			diQueues = new LinkedBlockingQueue<StatDi>();
		for(Map<String, Object> members: bufrDecoder.getParseList()){
			dateTimeOK = true;
			try{
				// 日照，全部调整为 过去24小时在前，过去1小时在后
				List<BufrBean> sunBufrBeans = (List<BufrBean>) members.get("1-1-2-0");
				if(fileSimple.indexOf("EEMH") > -1)
					sunBufrBeans = (List<BufrBean>) members.get("1-1-2-1");
//				else if(fileSimple.indexOf("ISNG") > -1 && sunBufrBeans == null)
//					sunBufrBeans = null;
				if(sunBufrBeans != null && sunBufrBeans.size() == 4 && !sunBufrBeans.get(0).getValue().toString().equals("-24.0") && !sunBufrBeans.get(2).getValue().toString().equals("-1.0")) {
					List<BufrBean> suBeans = new ArrayList<>(sunBufrBeans.subList(0, 2));
					List<BufrBean> suBeans2 = new ArrayList<>(sunBufrBeans.subList(2, 4));
					suBeans2.addAll(suBeans);
					if(fileSimple.indexOf("EEMH") > -1)
						members.replace("1-1-2-1", suBeans2);
					else
						members.replace("1-1-2-0", suBeans2);
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			try{
				// 辐射，调整为过去1小时在前，过去24小时在后 
				List<BufrBean> radiBufrBeans = (List<BufrBean>) members.get("1-1-2-1");
				if(fileSimple.indexOf("EEMH") > -1){
					radiBufrBeans = (List<BufrBean>) members.get("1-1-2-2");
				}
				else if(fileSimple.indexOf("ISNG") > -1 && (radiBufrBeans == null)){
					radiBufrBeans = (List<BufrBean>) members.get("1-1-2-0");
					members.replace("1-1-2-1", radiBufrBeans);
				}
				if(radiBufrBeans != null && radiBufrBeans.size() == 14 && !radiBufrBeans.get(0).getValue().toString().equals("-1.0") && !radiBufrBeans.get(7).getValue().toString().equals("-24.0")) {
					List<BufrBean> radiBeans = new ArrayList<>(radiBufrBeans.subList(0, 7));
					List<BufrBean> radiBeans2 = new ArrayList<>(radiBufrBeans.subList(7, 14));
					radiBeans2.addAll(radiBeans);
					if(fileSimple.indexOf("EEMH") > -1)
						members.replace("1-1-2-2", radiBeans2);
					else 
						members.replace("1-1-2-1", radiBeans2);
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			String sta = "";
			for (String tableName : tables) {
				dateTimeOK = true;
				StatDi di = new StatDi();
				di.setPROCESS_START_TIME(TimeUtil.getSysTime());
				di.setDATA_TYPE_1(StartConfig.ctsCode());
				
				XmlBean xmlBean = configMap.get(tableName.trim());
				Map<String, EntityBean> entityMap = null; //xmlBean.getEntityMap();
				Map<String, Map<String, EntityBean>> specialFileMapList = xmlBean.getSpecialFileList();
				int keyLength = 0; // 选取最长作为缺省key
				String selectedKeyMaker = ""; 
				int makerLength = 0;
				boolean isFounded = false;
				if(specialFileMapList != null && specialFileMapList.size() > 0){
					for(String key : specialFileMapList.keySet()){
						String []sp = key.split(",");
						if(sp.length > keyLength){
							keyLength = sp.length;
							selectedKeyMaker = key;
						}
						for(String keySP : sp){
							// 如果有1个以上匹配，找最长的匹配 ISN 和 ISND 都匹配 选 ISND
							if(fileSimple.indexOf(keySP.trim()) > -1 && keySP.trim().length() >= makerLength){
								entityMap = specialFileMapList.get(key);
								makerLength = keySP.trim().length();
								if(keySP.trim().equals("LFPW") || keySP.trim().equals("ENMI") || keySP.trim().equals("EEMH") || 
										(keySP.trim().equals("SBBR") && fileSimple.indexOf("ISMX") < 0 && fileSimple.indexOf("ISIX") < 0 && fileSimple.indexOf("ISAI") < 0) || 
										(keySP.trim().equals("ISMX") && fileSimple.indexOf("SBBR") >= 0) ||
										keySP.trim().equals("EKMI") || keySP.trim().equals("TFFF") ){
									isFounded = true;
									break;
								}
							}
						}
						if(isFounded == true)
							break;
					}
					if(entityMap == null || entityMap.size() == 0)
						entityMap = specialFileMapList.get(selectedKeyMaker);
				}
				else 
					continue;
				
				try{
					sta = ((BufrBean)(members.get("0-1-1-0"))).getValue().toString();
				}catch (Exception e) {
					infoLogger.error("台站号获取失败！");
				}
				String info = (String) proMap.get(sta + "+01");
				
				boolean is012001 = false; // 此时温度为取值-273.02
				boolean is012003 = false; //此时温度为取值-273.02
				// 用于存储入库的字段
				StringBuffer keyBuffer = new StringBuffer();
				// 用于存储入库的字段值
				StringBuffer valueBuffer = new StringBuffer();
				// 遍历配置信息
				Iterator<Entry<String, EntityBean>> it = entityMap.entrySet().iterator();
				Map<String, Object> row = new HashMap<>();
				//获取每个字段的值
				while (it.hasNext()) {
					Entry<String, EntityBean> next = it.next();
					String column = next.getKey(); // 配置文件配置的字段名称
					EntityBean entityBean = next.getValue(); // 配置的每个字段属性
					String datatype = entityBean.getDatatype(); // 字段类型
					String expression = entityBean.getExpression(); // 此字段的表达式
					String fxy = entityBean.getFxy(); // 解码时存储数据的key
					String defaultValue = entityBean.getDefaultValue(); // 缺省值

					Object obj = members.get(fxy); // 根据fxy获取每一行数据
					// 如果获取的对象为null
					Object value = null;
					
					if(column.equals("V01301")){
						System.out.println();
					}
					
					if(obj == null){
						if(fxy.equals("1-2-2-0"))
							fxy = "1-2-5-0";
						else if(fxy.equals("1-2-5-0"))
							fxy = "1-2-2-0"; 
						obj = members.get(fxy);
						if(obj == null && fxy.equals("1-2-2-0")){
							fxy = "1-2-2-1";
							obj = members.get(fxy);
						}
						entityBean.setFxy(fxy);
					}
					if(obj == null && fxy.equals("0-12-101-0")){
						fxy = "0-12-1-0";
						entityBean.setFxy(fxy);
						is012001 = true;
						obj = members.get(fxy);
					}else if(obj == null && fxy.equals("0-12-103-0")){
						fxy = "0-12-3-0";
						entityBean.setFxy(fxy);
						is012003 = true;
						obj = members.get(fxy);
					}
					if (obj == null) {
						if(fxy.equals("1-2-2-0") || fxy.equals("1-2-2-1") || fxy.equals("1-2-5-0")){
							value = defaultValue;
						}else{
							// 如果表达式不为空说明此值需要根据表达式进行计算value
							if (StringUtils.isNotBlank(expression)) {
								// 因对象为null所以此对象中不存在可以以index取值的对象，如果存在以index取值配置,直接配置其值为默认值
								Matcher em = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
								if (em.find()) {
									value = defaultValue;
								} else {// 如果表达式存在，且其中没有以index取值
									value = calExpressionValue(obj, members, entityMap, entityBean);
								}
							} else {
								value = defaultValue;
							}
						}
						if(column.equalsIgnoreCase("V_CCCC") || column.equalsIgnoreCase("C_CCCC"))
							value = members.get("CCCC");
						else if(column.equalsIgnoreCase("V_STT") || column.equalsIgnoreCase("V_TT"))
							value = members.get("TTAAii");
						else if(column.equalsIgnoreCase("V_BBB"))
							value = members.get("BBB");
						
						if(column.equalsIgnoreCase("D_SOURCE_ID"))
							value =value+"_"+file.getName();
						
						keyBuffer.append(",`").append(column).append("`");
						valueBuffer.append(",'").append(value.toString().trim()).append("'");
						
						row.put(column, value);
						
						if(column.equals("D_DATETIME")){
							int idx01 = value.toString().lastIndexOf(":");
							if(idx01 > 16){
								dateTimeOK = false;
								infoLogger.error("资料时间错误！  " + fileName);
								break;
							}
							else{
								di.setDATA_TIME(value.toString().substring(0, idx01));
							}
						}
						else if(column.equals("V01301"))
							di.setIIiii(value.toString());
						else if(column.equals("V05001"))
							di.setLATITUDE(value.toString());
						else if(column.equals("V06001"))
							di.setLONGTITUDE(value.toString());
						else if(column.equals("V07001"))
							di.setHEIGHT(value.toString());
						else if(column.equals("D_DATA_ID"))
							di.setDATA_TYPE(value.toString());
						else if(column.equals("V_BBB"))
							di.setDATA_UPDATE_FLAG(value.toString());
						continue;
					}
					else if(fxy.equals("1-2-2-0") || fxy.equals("1-2-2-1") ||  fxy.equals("1-2-5-0")){
						value = getCycleValue(expression, obj, entityBean);
					}
					else{
//							if(column.equals("V12001") || column.equals("V12003"))
//								System.out.println(column);
						// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
						value = calExpressionValue(obj, members, entityMap, entityBean);
					}
					//降水量的处理
					if((column.equalsIgnoreCase("V13019") || column.equalsIgnoreCase("V13020")
							|| column.equalsIgnoreCase("V13021") || column.equalsIgnoreCase("V13022")
							|| column.equalsIgnoreCase("V13023") || column.equalsIgnoreCase("V13011"))&& value.toString().startsWith("-0.1"))
						value = "999990";
					else if(column.equalsIgnoreCase("V13013") && value.toString().startsWith("-0.02")){
						value = "999990";
					}
					else if(value.toString().startsWith("99999") && ((column.toUpperCase().equals("V12001") && is012001) || (column.toUpperCase().equals("V12003") && is012003))){
						try{
							double temp = Double.parseDouble(value.toString());
							temp -= 0.05;
							value = temp;
						}catch (Exception e) {
							infoLogger.error("温度值数据类型错误");
						}
					}
					// （低或中云的）云量（对应编报云量）Nh、云属1、2、3的云量 单位换算
					else if(column.toUpperCase().startsWith("V20011")){
						value = getLowOrMidCloudAmount(value.toString());
					}
					else if(column.toUpperCase().equals("V05001") && value.toString().startsWith("999999")){
						if(info == null) {
							infoLogger.error("\n In configuration file, this station does not exist!" + sta);
						}
						else{
							String[] infos = info.split(",");
							if(infos[2].equals("null")){
								infoLogger.error("\n In configuration file, latitude is null!");
							}
							else
								value = Double.parseDouble(infos[2]);
						}
					}
					else if(column.toUpperCase().equals("V06001") && value.toString().startsWith("999999")){
						if(info == null) {
							infoLogger.error("\n In configuration file, this station does not exist!" + sta);
						}
						else{
							String[] infos = info.split(",");
							if(infos[1].equals("null")){
								infoLogger.error("\n In configuration file, longtitude is null!");
							}
							else
								value = Double.parseDouble(infos[1]);
						}
					}
					
					// 风向的处理,（风向、风速均为0时，静风999017）
//						if(column.equals("V11001")){
//							System.out.println("");
//							BufrBean bean12 = ((BufrBean)members.get("0-11-2-0"));
//							List<BufrBean> sBeans = ((List<BufrBean>)(members.get("1-18-0-0")));
//							System.out.println();
//						}
					try{ // 风向 判断
						if((column.equalsIgnoreCase("V11001")) && Double.parseDouble(value.toString()) == 0){
//								if(((BufrBean)members.get("0-11-2-0")) != null && ((BufrBean)members.get("0-11-2-0")).getValue().toString().equals("0.0"))
//									value = "999017";
//								else if(((List<BufrBean>)(members.get("1-18-0-0"))) != null && ((List<BufrBean>)(members.get("1-18-0-0"))).get(19).getValue().toString().equals("0.0"))
//									value = "999017";
							// 2019-02-14修改 
//								// 风向为0，可能的情况：不定向风（风速不为0时），入库999997；静风（风速为0，且风向单位不是度，即不是degree true），入库999017
							//处理的报文中,风向单位为degree true（度），因此，只考虑不定向风的情况
							// 风向为0，风速不为0->静风
//								BufrBean bean = (BufrBean)members.get("0-11-2-0");
							if( ((BufrBean)members.get("0-11-2-0")) != null && !((BufrBean)members.get("0-11-2-0")).getValue().toString().equals("0.0")){
								
								value = "999997";
							}
							else if(((List<BufrBean>)(members.get("1-18-0-0"))) != null && !((List<BufrBean>)(members.get("1-18-0-0"))).get(19).getValue().toString().equals("0.0"))
								value = "999997";
						}
					}catch (Exception e) {
						System.err.println("风向值不为数值型，转换错误！");
					}
					try{
						if(datatype.equalsIgnoreCase("double") && Double.parseDouble(value.toString()) > 999999)
							value = defaultValue;
					}catch (Exception e) {
						infoLogger.error("字段:" + column + "是double类型，数值超过 999999，存入999999！");
					}
					if(column.equalsIgnoreCase("V01015") && value.toString().contains("'")){
						String tmp = value.toString();
						tmp = tmp.replaceAll("'", "''");
						value = tmp;
					}
					
					
					
					keyBuffer.append(",`").append(column).append("`");
					this.appendValue(valueBuffer, datatype, value.toString().trim());

					row.put(column, value);
					
					if(column.equals("D_DATETIME")){
						int idx01 = value.toString().lastIndexOf(":");
						di.setDATA_TIME(value.toString().substring(0, idx01));
					}
					else if(column.equals("V01301"))
						di.setIIiii(value.toString());
					else if(column.equals("V05001"))
						di.setLATITUDE(value.toString());
					else if(column.equals("V06001"))
						di.setLONGTITUDE(value.toString());
					else if(column.equals("V07001"))
						di.setHEIGHT(value.toString());
					else if(column.equals("D_DATA_ID"))
						di.setDATA_TYPE(value.toString());
					else if(column.equals("V_BBB"))
						di.setDATA_UPDATE_FLAG(value.toString());
					
				} // end while
				
				if(dateTimeOK == false)
					continue;
				
				//台站号缺测 时，不入库
				if(di.getIIiii().startsWith("999999") || di.getDATA_TIME().startsWith("9999")){
					infoLogger.error("台站号缺测 或者 资料时间缺测！ " + fileName);
					break;  // 站号缺测，跳出for table loop，不入库
				}
				
				di.setFILE_SIZE(String.valueOf(file.length()));
				di.setSEND("BFDB");
				di.setSEND_PHYS("DRDS");
				di.setTRAN_TIME(ymdhm.format(new Date()));
				di.setTT("");
				di.setPROCESS_END_TIME(TimeUtil.getSysTime());
				di.setRECORD_TIME(TimeUtil.getSysTime());
				di.setFILE_NAME_N(fileName);
				di.setFILE_NAME_O(fileName);
				di.setBUSINESS_STATE("1");
				di.setPROCESS_STATE("1");
				DI.add(di);
				
				try {
					OTSDbHelper.getInstance().insert(tableName, row);
					infoLogger.info(tableName + ": Insert one line successfully!\n");
					diQueues.offer(di);
					successRowCount ++;
				}catch (Exception e) {
					di.setPROCESS_STATE("0");
					diQueues.offer(di);
					infoLogger.error(fileName + ": " + e.getMessage() + "\n");
					infoLogger.error(row + "\n");
					if(e.getClass() == ClientException.class) {
						infoLogger.error("DataBase connection error!\n");
						return false;
					}
				}
			} // for table
		} // for memset
		ResultSet.clear();
		infoLogger.info(fileName + ": Insert records number: " + successRowCount);
		
		return true;
	}
	
	String getLowOrMidCloudAmount(String string){
		String str = string;
		switch(string){
			case "0.0":
				str = "0";
				break;
			case "1.0":
				str = "10";
				break;
			case "2.0":
				str = "25";
				break;
			case "3.0":
				str = "40";
				break;
			case "4.0":
				str = "50";
				break;
			case "5.0":
				str = "60";
				break;
			case "6.0":
				str = "75";
				break;
			case "7.0":
				str = "90";
				break;
			case "8.0":
				str = "100";
				break;
			case "999999.0":
				str = "999999";
			    break;
			default:
				str = "999998";
		}
		return str;
	}
	private Object getCycleValue(String expression, Object obj, EntityBean entityBean) {
		List<BufrBean> bufrList = (List<BufrBean>) obj;
		int index = Integer.parseInt(expression);
		Object value = entityBean.getDefaultValue();
		for(int i = 0; i < bufrList.size(); i += 2){ // 拿到时间周期数
			int cycle = (int)Double.parseDouble(bufrList.get(i).getValue().toString());
			if(index == cycle){
				if(!entityBean.isQc())
					value = bufrList.get(i + 1).getValue();
				else
					if(bufrList.get(i + 1).getQcValues() != null) // 省级质控码
						value = bufrList.get(i + 1).getQcValues().get(0);
				break;
			}
		}
		return value;
	}

	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean) {
		String value = null;
		String expression = entityBean.getExpression();
		if (obj != null) {
			expression = getColumnValue(obj, entityBean).toString();
		}
		if (StringUtils.isNotBlank(expression)) {

			// ${col:V01301}
			// 将带有col的表达式替换掉
			Matcher columnMatcher = Pattern.compile(COLUMN_EXP_REGEX).matcher(expression);
			while (columnMatcher.find()) {
				String cExp = columnMatcher.group(0); // 带有${col:}的表达式
				String expCol = columnMatcher.group(1); // 字段
				// 获取表达式代表的配置
				EntityBean ebean = entityMap.get(expCol);
				
				String fxy = ebean.getFxy();
				// 获取表达式代表的对象
				Object obj2 = members.get(fxy);
				if (obj2 == null) {
					// 如果表达式获取字段值为空，直接返回默认值
					return entityBean.getDefaultValue();
				} else {
					// 如果不为空，获取其对应的值
					String columnValue = getColumnValue(obj2, ebean).toString();
					if (columnValue.equals(ebean.getDefaultValue())) {
						return columnValue;
					}
					if(ebean.getDatatype().equals("int")){
						int idx = columnValue.indexOf(".");
						if(idx >= 1)
							columnValue = columnValue.substring(0, idx);
					}
					String afterText = expression.substring(expression.indexOf(cExp));
					String cExpReg = StringUtil.string2Regex(cExp);
					afterText = afterText.replaceFirst(cExpReg, "");
					if (afterText.startsWith(StringUtil.SPLIT_FLAG) || afterText.startsWith(StringUtil.SUBSTRING_FLAG)) {
						// 存储整串表达式代表的值,list最后一个值即为最终结果
						List<String> res = new ArrayList<String>();
						// 用于存储一整串substring和split表达式，最后替换直值用
						StringBuffer regSBuffer = new StringBuffer();
						
						StringUtil.formatString(columnValue, afterText, res, regSBuffer);
						String resByStation = res.get(res.size() - 1); // 最终结果
						expression = expression.replaceFirst(StringUtil.string2Regex(cExp + regSBuffer.toString()), resByStation);
					} else {
						expression = expression.replaceFirst(cExpReg, columnValue);
					}

				}
			}

			// 替换所有默认表达式
			expression = StringUtil.formatExpressionContent(expression);
			// StringUtil.replaceNumberExp(expression, texts);
			// 替换${this}
			Matcher thisMatcher = Pattern.compile(THIS_VALUE_EXP).matcher(expression);
			while (thisMatcher.find()) {
				if (StringUtils.isBlank(value)) {
					return entityBean.getDefaultValue();
				}
				if(value != null)
					expression = expression.replaceFirst(StringUtil.string2Regex(thisMatcher.group(0)), value); // 如果value为null怎么办？
			}
			//转换字符串为ascii码
			expression = StringUtil.formatStringAsciiExp(expression);

		}
		
		value = StringUtil.calculteExp(expression);
		if(value.equalsIgnoreCase("null"))
			value = "999999";
		return value;
	}

	private Object getColumnValue(Object obj, EntityBean entityBean) throws IndexOutOfBoundsException {
		boolean isQc = entityBean.isQc();
		// int index = entityBean.getIndex();
		Object value = null;
		// 如果是bufrBean对象，直接强制转换
		if (obj instanceof BufrBean) {
			BufrBean bufr = (BufrBean) obj;
			if (isQc) {
				if(bufr.getQcValues() != null)
					value = bufr.getQcValues().get(0);
				else 
					value = 9;
				int parseValue = Integer.parseInt(value.toString());
				value = parseValue > 9 ? 9 : parseValue;
			} else {
				// commented by cuihongyuan
//				value = bufr.getValue();
				String expression = entityBean.getExpression();
				if (StringUtils.isNotBlank(expression)) {
					Matcher numMatcher = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
					if (numMatcher.find()) {
						List<String> ts = new ArrayList<String>();
						String v = bufr.getValue().toString();
						
						if(entityBean.getDatatype().equalsIgnoreCase("double") ){
							BigDecimal bigDecimal = new BigDecimal(v);
							bufr.setValue(bigDecimal.toPlainString());
						}
						ts.add(bufr.getValue().toString());
						value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
					}else {
						infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
					}
				}else {
					value = bufr.getValue();
				}
			}
		} else { // 不是bufrBean对象，那就是存储对象的list集合
			@SuppressWarnings("unchecked")
			List<BufrBean> bufrList = (List<BufrBean>) obj;

			String expression = entityBean.getExpression();
			if (StringUtils.isNotBlank(expression)) {
				Matcher numMatcher = Pattern.compile(StringUtil.SERIAL_NUMBER_REGEX).matcher(expression);
				if (numMatcher.find()) {

					String _index = numMatcher.group(1);
					int index = Integer.parseInt(_index);
					// 如果list大小小于index，说明index代表的值不存在，直接返回默认值即可
					if ((index + 1) > bufrList.size()) {
						return entityBean.getDefaultValue();
					}

					List<String> ts = new ArrayList<String>();
					if (isQc) {
						for (BufrBean bufrBean : bufrList) {
							Object value2 = null;
							if(bufrBean.getQcValues() != null)
								value2 = bufrBean.getQcValues().get(0);
							else 
								value2 = 9;
							int parseValue = Integer.parseInt(value2.toString());
							value2 = parseValue > 9 ? 9 : parseValue;
							ts.add(value2.toString());
						}
					} else {
						for (BufrBean bufrBean : bufrList) {
							Object value2 = bufrBean.getValue();						
							if(entityBean.getDatatype().equalsIgnoreCase("double") ){
								BigDecimal bigDecimal = new BigDecimal(value2.toString());
								bufrBean.setValue(bigDecimal.toPlainString());
							}
							ts.add(bufrBean.getValue().toString());
						}
					}

					value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
				} else {
					infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
				}
			} else {
				infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
			}

		}
		return value;
	}

	private void appendValue(StringBuffer buffer, String dataType, Object value) {
		if ("int".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("short".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("byte".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("double".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("float".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("long".equalsIgnoreCase(dataType)) {
			buffer.append(",").append(value);
		} else if ("date".equalsIgnoreCase(dataType)) {
			buffer.append(",'").append(value).append("'"); // TODO
		} else {
			buffer.append(",'").append(value).append("'");
		}
	}

	
}
