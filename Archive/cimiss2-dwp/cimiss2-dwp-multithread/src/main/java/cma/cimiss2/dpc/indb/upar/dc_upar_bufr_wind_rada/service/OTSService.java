package cma.cimiss2.dpc.indb.upar.dc_upar_bufr_wind_rada.service;

import java.io.File;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.cimiss2.dwp.tools.ConnectionPoolFactory;
import org.cimiss2.dwp.tools.RestfulSendData;
import org.cimiss2.dwp.tools.SendType;
import org.cimiss2.dwp.tools.config.StartConfig;
import org.cimiss2.dwp.tools.ots.OTSDbHelper;
import org.cimiss2.dwp.tools.utils.EIConfig;
import org.cimiss2.dwp.tools.utils.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.hitec.bufr.bean.BufrBean;
import com.hitec.bufr.bean.EntityBean;
import com.hitec.bufr.bean.XmlBean;
import com.hitec.bufr.decoder.BufrUparWindDecoder2;
import com.hitec.bufr.util.StringUtil;
import cma.cimiss2.dpc.decoder.tools.common.EI;
import cma.cimiss2.dpc.decoder.tools.common.RestfulInfo;
import cma.cimiss2.dpc.decoder.tools.common.StatDi;
import cma.cimiss2.dpc.decoder.tools.enumeration.EIEventType;
import cma.cimiss2.dpc.indb.common.BufrConfig;

public class OTSService implements BufrService {
	int batchSize = 500;
	public static BlockingQueue<StatDi> diQueues;
	private final static String COLUMN_EXP_REGEX = "\\$\\{col:(.*?)\\}";
	private final static String THIS_VALUE_EXP = "\\$\\{this\\}";
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	private static List<StatDi> DI = new ArrayList<StatDi>();
	public BufrUparWindDecoder2 decoder = new BufrUparWindDecoder2();

	public static BlockingQueue<StatDi> getDiQueues() {
		return diQueues;
	}
	public static void setDiQueues(BlockingQueue<StatDi> diQueues) {
		BufrWindRadaServiceImpl.diQueues = diQueues;
	}
	@Override
	public boolean decode(String name, byte[] dataBytes, String tableSection, List<String> tables) {
//		try {
//			BufrUparWindDecoder2 bufrDecoder = new BufrUparWindDecoder2();
//			boolean isRead = bufrDecoder.bufrDecoder(name, dataBytes);
//			if (isRead) {
//				return this.decode(bufrDecoder, name, tableSection, tables);
//			} else {
//				infoLogger.error("\n Read file error: "+ name);
//				String event_type = EIEventType.OP_FILE_ERROR.getCode();
//				EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
//				if(ei == null) {
//					infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
//				}else {
//					if(StartConfig.isSendEi()) {
//						ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
//						ei.setKObject("cma.cimiss2.dpc.indb.upar.dc_upar_bufr_wind_rada.service.BufrWindRadaServiceImpl");
//						ei.setKEvent("解码入库源文件异常：" + name);
//						ei.setKIndex("详细信息：" + name);
//						ei.setEVENT_EXT1(name);
//						RestfulInfo restfulInfo = new RestfulInfo();
//						restfulInfo.setType("SYSTEM.ALARM.EI ");
//						restfulInfo.setName("数据解码入库EI告警信息");
//						restfulInfo.setMessage("数据解码入库EI告警信息");
//						
//						restfulInfo.setFields(ei);
//						List<RestfulInfo> restfulInfos = new ArrayList<>();
//						restfulInfos.add(restfulInfo);
//						RestfulSendData.SendData(restfulInfos, SendType.EI);
//					}
//				}
//				return false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
		try{
			decoder.decodeFile(name, dataBytes);
			return this.decode(decoder, name, tableSection, tables);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
			infoLogger.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 风廓线BUFR
	 */
	@Override
	public boolean decode(String fileName, String tableSection, List<String> tables) {
//		BufrUparWindDecoder2 bufrDecoder = new BufrUparWindDecoder2();
//		decoder.decodeFile(filename);
//		boolean isRead = bufrDecoder.bufrDecoder(fileName);
//		if (isRead) {
//			return this.decode(bufrDecoder, fileName, tableSection, tables);
//		} else {			
//			infoLogger.error("\n Read file error: "+ fileName);
//			String event_type = EIEventType.OP_FILE_ERROR.getCode();
//			EI ei = EIConfig.getEiConfig().getEiMaps().get(event_type);
//			if(ei == null) {
//				infoLogger.error("\n IN EI CONFIGURATION FILE, THIS EVENT DOSE NOT EXIST: "+event_type);
//			}else {
//				if(StartConfig.isSendEi()) {
//					ei.setEVENT_TIME(TimeUtil.date2String(new Date(), "yyyy-MM-dd HH:mm:ss"));
//					ei.setKObject("cma.cimiss2.dpc.indb.upar.dc_upar_bufr_wind_rada.service.BufrWindRadaServiceImpl");
//					ei.setKEvent("解码入库源文件异常：" + fileName);
//					ei.setKIndex("详细信息：" + fileName);
//					ei.setEVENT_EXT1(fileName);
//					RestfulInfo restfulInfo = new RestfulInfo();
//					restfulInfo.setType("SYSTEM.ALARM.EI ");
//					restfulInfo.setName("数据解码入库EI告警信息");
//					restfulInfo.setMessage("数据解码入库EI告警信息");
//					
//					restfulInfo.setFields(ei);
//					List<RestfulInfo> restfulInfos = new ArrayList<>();
//					restfulInfos.add(restfulInfo);
//					RestfulSendData.SendData(restfulInfos, SendType.EI);
//				}
//			}
//			return false;
//		}
		try{
			decoder.decodeFile(fileName);
			return this.decode(decoder, fileName, tableSection, tables);
			
		}catch (Exception e) {
			System.out.println(e.getMessage());
			infoLogger.error(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public boolean decode(BufrUparWindDecoder2 bufrDecoder, String fileName, String tableSection, List<String> tables) {
		File file = new File(fileName);
		// 获取数据库连接
		DruidPooledConnection connection = null;
		Statement statement = null;
		String enter=bufrDecoder.entrance+":"+bufrDecoder.local_version+"_"+bufrDecoder.master_table+"_"+bufrDecoder.subcenter_id+"_"+bufrDecoder.center_id;
		String xmlKey1="3-1-1:5_13_255_255,3-1-1:0_11_0_85,3-1-1:4_11_0_218,3-1-1:6_11_255_255,3-1-1:4_11_255_-9999,3-1-1:0_12_0_86,0-1-18:0_14_10_78";
		String xmlKey2="3-1-32:0_9_0_1,3-1-32:3_9_0_85";
		String xmlKey3="3-1-32:0_9_0_59,3-1-32:0_9_0_89,3-1-32:0_9_0_78";
		if(xmlKey1.contains(enter)){
			tableSection+="_V1";
		}else if(xmlKey2.contains(enter)){
			tableSection+="_V2";
		}else if(xmlKey3.contains(enter)){
			tableSection+="_V3";
		}else {
			infoLogger.error("Table XML file error!");
		}
		
		Map<String, XmlBean> configMap = BufrConfig.get(tableSection);
		try {
			connection = ConnectionPoolFactory.getInstance().getConnection("rdb");
			statement = connection.createStatement();
			List<String> sqlList = new ArrayList<>();
			if(connection.getAutoCommit() == false)
				connection.setAutoCommit(true);
//			statement.execute("select last_txc_xid()");
			for(Map<String, Object> members: bufrDecoder.parseList){
				for (String tableName : tables) {
					tableName = tableName.trim();
					XmlBean xmlBean = configMap.get(tableName);
					
					Map<String, EntityBean> entityMap = xmlBean.getEntityMap();
					StatDi di = null;
					
					//**********************************
					// 用于存储入库的字段
					StringBuffer keyBuffer0 = new StringBuffer();
					// 用于存储入库的字段值
					StringBuffer valueBuffer0 = new StringBuffer();
					//用于存D_RECORD_ID的值
					String recordvalue="";
					Map<String, Object> row0 = new HashMap<>();
					// 遍历entityMap的配置信息
					for(String key : entityMap.keySet()){
						EntityBean entityBean = entityMap.get(key);// 配置的每个字段属性
						String fxy = entityBean.getFxy();
						String datatype = entityBean.getDatatype();// 字段类型
						String column = entityBean.getColumn();// 配置文件配置的字段名称
						String expression = entityBean.getExpression(); // 此字段的表达式
						String defaultValue = entityBean.getDefaultValue(); // 缺省值
						Object obj = members.get(fxy);
						Object value = null;
						if("D_RECORD_ID".equals(column)){
							System.out.println();
						}
						if (obj == null) {
							
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
								value = entityBean.getDefaultValue();
							}
							
							if(column.equalsIgnoreCase("D_SOURCE_ID"))
								value =value+"_"+file.getName();
							if(column.equalsIgnoreCase("D_RYMDHM"))
								value =TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss");
							
							if("D_RECORD_ID".equals(column)){//D_RECORD_ID需要在group中的V07002赋值后拼接
								recordvalue=String.valueOf(value);
							}else{
								keyBuffer0.append(",`").append(column).append("`");
								valueBuffer0.append(",'").append(value.toString().trim()).append("'");
								row0.put(column, value);
							}
								
							continue;
						}
						// 如果expression不为空根据其表达式计算相应结果，因上面将文件中取到的值为null的去掉了，此不无须再判断值是否为空了
						value = calExpressionValue(obj, members, entityMap, entityBean);
						
						keyBuffer0.append(",`").append(column).append("`");
						this.appendValue(valueBuffer0, datatype, value.toString().trim());
						row0.put(column, value);
					}
					//遍历GroupMap的配置信息
					Map<String, Map<String, EntityBean>> groupMapList = xmlBean.getGroupMap();
					for(String groupKey : groupMapList.keySet()){
						Map<String, EntityBean> group = groupMapList.get(groupKey);
						String groupFXY = groupKey.split("_")[0];
						List<Map<String, BufrBean>> BufrBeanList = (List<Map<String, BufrBean>>) members.get(groupFXY);
						if(BufrBeanList==null){
							continue;
						}
						
//						for (Map<String, BufrBean> map : BufrBeanList) {
						Map<String, BufrBean> map = null;
						for (int i = 0; i < BufrBeanList.size(); i ++) {//循环多少条数据
							di = new StatDi();
							di.setPROCESS_START_TIME(TimeUtil.getSysTime());
							di.setTT("BUFR");
							di.setDATA_TYPE_1(StartConfig.ctsCode());
							di.setFILE_NAME_N(fileName);
							di.setFILE_NAME_O(fileName);
							di.setTRAN_TIME(TimeUtil.date2String(new Date(file.lastModified()), "yyyy-MM-dd HH:mm:ss"));
							di.setFILE_SIZE(String.valueOf((new File(fileName)).length()));
							di.setBUSINESS_STATE("1");
							di.setPROCESS_STATE("1");
							
							map = BufrBeanList.get(i);
							StringBuffer keyBuffer = new StringBuffer();
							StringBuffer valBuffer = new StringBuffer();
							Map<String, Object> row = new HashMap<>();
							
							for(String key : group.keySet()){
								EntityBean bean = group.get(key);
								String col = bean.getColumn();
								String fxy = bean.getFxy();
								String datatype = bean.getDatatype(); 
								String exp = bean.getExpression();
								Object val = null;
								if(bean.isQc()){
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null && bufrBean.getQcValues() != null){
										val = bufrBean.getQcValues().get(0);
									}
									else
										val = bean.getDefaultValue();
									
									keyBuffer.append(",`").append(col).append("`");
									valBuffer.append(",'").append(val.toString().trim()).append("'");
									row.put(col, val);
								}else{
									
									BufrBean bufrBean = map.get(fxy);
									if(bufrBean != null){
										val = bufrBean.getValue();
									}
									else
										val = bean.getDefaultValue();
									if("V07002".equals(col)){
										recordvalue+="_"+val;
										keyBuffer.append(",`D_RECORD_ID`");
										valBuffer.append(",'").append(recordvalue.toString().trim()).append("'");
										
										row.put("D_RECORD_ID", recordvalue);
										recordvalue=recordvalue.substring(0,recordvalue.lastIndexOf("_"));
										
									}
									keyBuffer.append(",`").append(col).append("`");
									valBuffer.append(",'").append(val.toString().trim()).append("'");
									row.put(col, val);
								}
							}
							keyBuffer.append(keyBuffer0.toString());
							valBuffer.append(valueBuffer0.toString().trim());
							
							row.putAll(row0);
							
							di.setPROCESS_END_TIME(TimeUtil.getSysTime());
							di.setRECORD_TIME(TimeUtil.getSysTime());
							DI.add(di);

							String sql = "insert into " + tableName //
									+ " (" //
									+ keyBuffer.toString().substring(1) //
									+ ") values ("//
									+ valBuffer.toString().substring(1) //
									+ ")";
							
							try{
//								statement.execute(sql);
								OTSDbHelper.getInstance().insert(tableName, row);
								infoLogger.info("插入成功："+fileName+": "+ sql);
							}catch (Exception e) {
								infoLogger.error("插入失败：" +fileName+": "+ sql + "\n" + e.getMessage());
							}
						}//结束循环入一个groupsize条数据
					} // 结束循环groupMapList
					
				} // 结束table循环 
				if(DI != null){
					for (int j = 0; j < DI.size(); j++) {
						diQueues.offer(DI.get(j));
					}
					DI.clear();
				}
			}//结束ParseList循环
			return true;
		} catch (SQLException e) {
			infoLogger.error("数据库异常: " + e.getMessage());
			return false;
		} finally {
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Object calExpressionValue(Object obj, Map<String, Object> members, Map<String, EntityBean> entityMap, EntityBean entityBean) {
		String value = null;
		
		String expression = entityBean.getExpression();
		if("D_RECORD_ID".equals(entityBean.getColumn())){
			expression=expression.substring(0, expression.lastIndexOf("_"));
		}
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
				String cExpReg = StringUtil.string2Regex(cExp);
				if (obj2 == null) {
					// 如果表达式获取字段值为空，直接返回默认值
					expression = expression.replaceFirst(cExpReg, entityBean.getDefaultValue());
//					return entityBean.getDefaultValue();
				} else {
					// 如果不为空，获取其对应的值
					String columnValue = getColumnValue(obj2, ebean).toString();
					
//					if (columnValue.equals(ebean.getDefaultValue())) {
//						expression = expression.replaceFirst(cExpReg, columnValue);
//					}
					if(columnValue.startsWith("999999.0") && ebean.getDefaultValue() != null)
						columnValue = ebean.getDefaultValue();
					if(ebean.getDatatype().equals("int")){
						int idx = columnValue.indexOf(".");
						if(idx >= 1)
							columnValue = columnValue.substring(0, idx);
					}
//					System.out.println(expression + "====" + cExp);
					
					String afterText = expression.substring(expression.indexOf(cExp));
//					String cExpReg = StringUtil.string2Regex(cExp);
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
						String sp [] = columnValue.split("[+\\*]");
						sp[0] = sp[0].replaceAll("\\$|\\{", "");
						try{
							if(!"string".equals(ebean.getDatatype()))
							sp[0] = String.format("%d", Integer.parseInt(sp[0]));
						}
						catch (Exception e1) {
							try{
								sp[0] = String.format("%.3f", Double.parseDouble(sp[0]));
							}
							catch (Exception e2) {
								System.out.println(expression);
							}
						}
						expression = expression.replaceFirst(cExpReg, sp[0]);
					}
				}
			}

			// 替换所有默认表达式
//			System.out.println(expression);
			expression = StringUtil.formatExpressionContent(expression);
			// StringUtil.replaceNumberExp(expression, texts);
			// 替换${this}
			Matcher thisMatcher = Pattern.compile(THIS_VALUE_EXP).matcher(expression);
			while (thisMatcher.find()) {
				if (StringUtils.isBlank(value)) {
					return entityBean.getDefaultValue();
				}
				if(StringUtil.string2Regex(thisMatcher.group(0)) != null && value != null)
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
						value = entityBean.getDefaultValue();
						if(!bufr.getValue().toString().equals("NaN"))
							value = bufr.getValue();
						ts.add(value.toString());
						value = StringUtil.replaceNumberExp(expression, ts.toArray(new String[ts.size()]));
					}else {
						infoLogger.error("配置有误,应在expression属性中配置相应的索引序号: " + entityBean.getColumn());
					}
				}else {
					value = bufr.getValue();
				}
			}
		}
		else if(obj instanceof String){
			value = String.valueOf(obj);
		}
		else { // 不是bufrBean对象，那就是存储对象的list集合
			String expression = entityBean.getExpression();
			if(entityBean.getFxy().substring(0, 1).equals("1") && Integer.parseInt(entityBean.getFxy().substring(2, 3)) > 1){
				@SuppressWarnings("unchecked")
				List<Map<String, BufrBean>> bufrList = (List<Map<String, BufrBean>> ) obj;
				String []expSp = expression.split("_");
				BufrBean bean = new BufrBean();
				if(expSp.length == 2){
					int idx = Integer.parseInt(expSp[0]);
					String fxy = expSp[1];
					bean = bufrList.get(idx).get(fxy);
					value = entityBean.getDefaultValue();
					if(!bean.getValue().toString().equals("NaN"))
						value = bean.getValue();
				}
			}
			else{
				@SuppressWarnings("unchecked")
				List<BufrBean> bufrList = (List<BufrBean>) obj;
				
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
								ts.add(value2.toString());
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
	public static void main(String[] args) {
		BufrWindRadaServiceImpl bufrWindRadaServiceImpl=new BufrWindRadaServiceImpl();
		String fileName="D:\\TEMP\\fengkuoxianBufrData\\testdatanew\\7\\A_IUPD43EDZW081338_C_BABJ_20190508134125_79982.bin";
		String tableSection="B.0001.0015.R001";
		List<String> tableList = new ArrayList<String>();
		 tableList.add("UPAR_WPF_CHN_TAB");
		  BlockingQueue<StatDi> diQueues = new LinkedBlockingQueue<StatDi>();
		  bufrWindRadaServiceImpl.setDiQueues(diQueues);
		 
		Boolean decode = bufrWindRadaServiceImpl.decode(fileName, tableSection, tableList);
	}


}
