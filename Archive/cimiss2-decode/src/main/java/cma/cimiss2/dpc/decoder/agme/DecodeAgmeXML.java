package cma.cimiss2.dpc.decoder.agme;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_XML_Bean;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import ucar.nc2.iosp.bufr.writer.MessageWriter;
// TODO: Auto-generated Javadoc
/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	农气观测资料（XML格式）解码类
 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《数据库逻辑结构设计-农气分册-新流程_20180108.docx》 </a>
 *      <li> <a href=" "> 《附件：1．农业气象观测站XML上传数据文件内容与传输规定（暂行） v1.0.doc》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年10月11日 上午10:28:17   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeAgmeXML {
	
	/** 缺测替代值. */
	public static double defautValue = 999999;
	
	/** 缺测替代值. */
	public static String defaultStr = "999999";
	
	/** The s format. */
	SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** The cfg. */
	ParseConfig cfg = ParseConfig.getParaeConfig();
	
	/** 获取XML配置文件的内容. */
	Map<String, List<ParseBean>> parseBeans = cfg.getParseBeans();
	
	/** 存放数据解析的结果集. */
	private ParseResult<Agme_XML_Bean> parseResult = new ParseResult<Agme_XML_Bean>(false);
	
	/**
	 * Decode file.
	 *
	 * @param filename 待解析文件
	 * @param recv_time 资料接收时间
	 * @return ParseResult<Agme_XML_Bean>
	 * @Title: decodeFile
	 * @Description: 农气XML格式资料解码方法
	 */
	public ParseResult<Agme_XML_Bean> decodeFile(String filename, Date recv_time) {
		File file = new File(filename);
		if(file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			Date last_modifi_time = new Date(file.lastModified());
	        SAXReader reader = null;
	        FileInputStream in = null;
	        Reader read = null;
	        Document doc = null;
			try {
				//2020-3-19 chy
				reader = new SAXReader();
				in = new FileInputStream(new File(filename));
				read = new InputStreamReader(in, "GBK");  // GBK,GB2312
				doc = reader.read(read);
				
				if(ParseConfig.IsCheck) {
//					Map<String, AgmeType> agmeDataMap = PropertiesUtil.getAgmeDataMap();
					Set<String> dataKey = parseBeans.keySet();
					for(Iterator<String> key = dataKey.iterator(); key.hasNext();){
						String type_Ppath = key.next();
						String sp[] = type_Ppath.split(",");
						String type = sp[0];
						String parent = sp[1];
						@SuppressWarnings("rawtypes")
						List list = doc.selectNodes(parent);
						if(list != null && list.size() > 0){
							// 报文中，CROP01 List, 等
							List<Map<String, Object>> sqls = new ArrayList<>();
							for(Iterator<?> node = list.iterator(); node.hasNext(); ){
								List<Object> eles = new ArrayList<Object>();
								List<Object> values = new ArrayList<Object>();
								Element element = (Element) node.next();
								String col = "";
								for(ParseBean parseBean : parseBeans.get(type_Ppath)){
									// 判断，数据库字段有相同前缀(_符号拼接),之后为数字，则表示出现了分层观测字段
									String colSplit[] = col.split("_");
									if(colSplit.length == 2){
										String t1 = colSplit[0];
										String t2 = colSplit[1];
										if(parseBean.getElementName().startsWith(t1) && isNumeric(t2)){
											eles.add(parseBean.getElementName());
											if(eles.size() > values.size()){
												values.add(defaultStr);
											}
											continue;
										}
									}
									eles.add(parseBean.getElementName());
									if(parseBean.getContent() == Content.DEFAULT) {
										parseDefaultElementValue(type, values, parseBean, recv_time, last_modifi_time);
									}else if(parseBean.getContent() == Content.UNDIFINE){
										values.add("'"+parseBean.getValue()+"'");
									}
									else {
										@SuppressWarnings("rawtypes")
										List vals = null;
										if(parseBean.getIndex().startsWith("/"))
										   vals = doc.selectNodes(parseBean.getIndex());
										else{
											vals = element.selectNodes(parseBean.getIndex());
										}
										if(vals != null && vals.size() > 0) {
											col = parseBean.getElementName();
											for(@SuppressWarnings("rawtypes")Iterator val = vals.iterator(); val.hasNext();){
												Element tmp = (Element) val.next();
												Map<String, String> items = new HashMap<String, String>();
												items.put(parseBean.getIndex(), tmp.getStringValue());
												if(parseBean.isIsCalc()) {
													calcElementValue(values, parseBean, items);
												}else {
													parseElementValue(values, parseBean, items);
												}
											}
										}else{
											if(parseBean.getDefaultValue() != null){
												values.add("'" + parseBean.getDefaultValue() + "'");
											}else{
												values.add("'" + defaultStr + "'");
											}
										}
									}	
								}
								Map<String, Object> data = new HashMap<>();
								for(int p = 0; p < eles.size(); p ++){
									data.put(eles.get(p).toString(), values.get(p).toString());
								}
								
								
								//2019-7-16 cui hongyuan
								String string = data.get("D_DATETIME").toString().replaceAll("\'|\"", "");
								Date date = sFormat.parse(string);
								if(!TimeCheckUtil.checkTime(date)){
									ReportError reportError = new ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(data.get("D_DATETIME").toString());
									parseResult.put(reportError);
									continue;
								}
								
								sqls.add(data);
							}
							Agme_XML_Bean agme_XML_Bean = new Agme_XML_Bean();
							agme_XML_Bean.setType(type);
							agme_XML_Bean.setSql(sqls);
							parseResult.put(agme_XML_Bean);
							parseResult.setSuccess(true);
						}// end if
					} // end for
				} // end if
				else{
					ReportError reportError = new ReportError();
					reportError.setMessage("XML config file is not checked!");
					parseResult.put(reportError);
				}
			} catch (Exception e) {
				e.printStackTrace();
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}finally{
				try {
					read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		else{
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	/**
	 * Decode file.
	 *
	 * @param filename 待解析文件
	 * @param recv_time 资料接收时间
	 * @return ParseResult<Agme_XML_Bean>
	 * @Title: decodeFile
	 * @Description: 农气XML格式资料解码方法--实时消息
	 */
	public ParseResult<Agme_XML_Bean> decodeFile(String filename, Date recv_time,String fileContent) {
//		File file = new File(filename);
		if(!fileContent.isEmpty()) {
//			if(file.length() <= 0){
//				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
//				return parseResult;
//			}
			String [] times=filename.split("_");
			String time=times[4].substring(0, 4)+"-"+times[4].substring(4, 6)+"-"+times[4].substring(6, 8)+" "+
					times[4].substring(8, 10)+":"+times[4].substring(10, 12)+":"+times[4].substring(12, 14);
			Date last_modifi_time=null;
			try {
				last_modifi_time = sFormat.parse(time);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
//	        SAXReader reader = null;
	        FileInputStream in = null;
//	        Reader read = null;
	        Document doc = null;
			try {
				//2020-3-19 chy
//				reader = new SAXReader();
//				in = new FileInputStream(new File(filename));
//				read = new InputStreamReader(in, "GBK");  // GBK,GB2312
				doc = DocumentHelper.parseText(fileContent);
				
				if(ParseConfig.IsCheck) {
//					Map<String, AgmeType> agmeDataMap = PropertiesUtil.getAgmeDataMap();
					Set<String> dataKey = parseBeans.keySet();
					for(Iterator<String> key = dataKey.iterator(); key.hasNext();){
						String type_Ppath = key.next();
						String sp[] = type_Ppath.split(",");
						String type = sp[0];
						String parent = sp[1];
						@SuppressWarnings("rawtypes")
						List list = doc.selectNodes(parent);
						if(list != null && list.size() > 0){
							// 报文中，CROP01 List, 等
							List<Map<String, Object>> sqls = new ArrayList<>();
							for(Iterator<?> node = list.iterator(); node.hasNext(); ){
								List<Object> eles = new ArrayList<Object>();
								List<Object> values = new ArrayList<Object>();
								Element element = (Element) node.next();
								String col = "";
								for(ParseBean parseBean : parseBeans.get(type_Ppath)){
									// 判断，数据库字段有相同前缀(_符号拼接),之后为数字，则表示出现了分层观测字段
									String colSplit[] = col.split("_");
									if(colSplit.length == 2){
										String t1 = colSplit[0];
										String t2 = colSplit[1];
										if(parseBean.getElementName().startsWith(t1) && isNumeric(t2)){
											eles.add(parseBean.getElementName());
											if(eles.size() > values.size()){
												values.add(defaultStr);
											}
											continue;
										}
									}
									eles.add(parseBean.getElementName());
									if(parseBean.getContent() == Content.DEFAULT) {
										parseDefaultElementValue(type, values, parseBean, recv_time, last_modifi_time);
									}else if(parseBean.getContent() == Content.UNDIFINE){
										values.add("'"+parseBean.getValue()+"'");
									}
									else {
										@SuppressWarnings("rawtypes")
										List vals = null;
										if(parseBean.getIndex().startsWith("/"))
										   vals = doc.selectNodes(parseBean.getIndex());
										else{
											vals = element.selectNodes(parseBean.getIndex());
										}
										if(vals != null && vals.size() > 0) {
											col = parseBean.getElementName();
											for(@SuppressWarnings("rawtypes")Iterator val = vals.iterator(); val.hasNext();){
												Element tmp = (Element) val.next();
												Map<String, String> items = new HashMap<String, String>();
												items.put(parseBean.getIndex(), tmp.getStringValue());
												if(parseBean.isIsCalc()) {
													calcElementValue(values, parseBean, items);
												}else {
													parseElementValue(values, parseBean, items);
												}
											}
										}else{
											if(parseBean.getDefaultValue() != null){
												values.add("'" + parseBean.getDefaultValue() + "'");
											}else{
												values.add("'" + defaultStr + "'");
											}
										}
									}	
								}
								Map<String, Object> data = new HashMap<>();
								for(int p = 0; p < eles.size(); p ++){
									data.put(eles.get(p).toString(), values.get(p).toString());
								}
								
								
								//2019-7-16 cui hongyuan
								String string = data.get("D_DATETIME").toString().replaceAll("\'|\"", "");
								Date date = sFormat.parse(string);
								if(!TimeCheckUtil.checkTime(date)){
									ReportError reportError = new ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(data.get("D_DATETIME").toString());
									parseResult.put(reportError);
									continue;
								}
								
								sqls.add(data);
							}
							Agme_XML_Bean agme_XML_Bean = new Agme_XML_Bean();
							agme_XML_Bean.setType(type);
							agme_XML_Bean.setSql(sqls);
							parseResult.put(agme_XML_Bean);
							parseResult.setSuccess(true);
						}// end if
					} // end for
				} // end if
				else{
					ReportError reportError = new ReportError();
					reportError.setMessage("XML config file is not checked!");
					parseResult.put(reportError);
				}
			} catch (Exception e) {
				e.printStackTrace();
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
		}
		else{
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}

	
	/**
	 * Checks if is numeric.
	 *
	 * @param str the str
	 * @return boolean
	 * @Title: isNumeric
	 * @Description: 判断一个字符串是否是整型
	 */
	public static boolean isNumeric(String str){
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	}
	
	/**
	 * Parses the element value.
	 *
	 * @param values 解析结果加入 List<values>
	 * @param parseBean XML配置文件的一行对应的bean
	 * @param items 要解析的要素编码-要素值对
	 * @Title: parseElementValue
	 * @Description: 要素值的解析
	 */
	private void parseElementValue(List<Object> values, ParseBean parseBean, Map<String, String> items) {
		if(parseBean.getDataType() == DataType.STRING) {
			values.add("'"+items.get(parseBean.getIndex())+"'");
		}else if (parseBean.getDataType()==DataType.DOUBLE) {
			values.add(DecodeUtil.parseDouble((String) items.get(parseBean.getIndex())));
		}else if (parseBean.getDataType()== DataType.INT) {
			// 如果涉及风向，进行编码与数值的转换
			if(parseBean.getElementName().equals("V11001") || parseBean.getElementName().equals("V11296")){
				values.add(field(items.get(parseBean.getIndex())));
			}
			else{
				values.add(DecodeUtil.parseInt(items.get(parseBean.getIndex())));
			}
		}else if(parseBean.getDataType() == DataType.DATETIME){
			String format = parseBean.getFormat();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			try{
				Date date = simpleDateFormat.parse(items.get(parseBean.getIndex()));
				values.add("'" + sFormat.format(date) + "'");
			}catch (Exception e) {
				System.out.println("D_DATETIME parse error!");
			}
		}else {
			values.add(items.get(parseBean.getIndex()));
		}
	}
	
	/**
	 * Calc element value.
	 *
	 * @param values 解析结果存放在List<values>中
	 * @param parseBean XML配置文件中一行的bean
	 * @param items 要解析的要素编码-要素值对
	 * @Title: calcElementValue
	 * @Description: 如果要素值要通过表达式解析，调用此方法
	 */
	private void calcElementValue(List<Object> values, ParseBean parseBean, Map<String, String> items) {
		if(parseBean.getContent() == Content.REPORT && items.containsKey(parseBean.getIndex())) {
			String temp_value = items.get(parseBean.getIndex()).trim();
			String expression = parseBean.getExpression();
			if(expression.startsWith("[") && expression.endsWith("]")) {
				int index = expression.indexOf(":");
				int start_index = Integer.parseInt(expression.substring(1, index));
				int end_index = Integer.parseInt(expression.substring(index+1, expression.length()-1));
				if(parseBean.getDataType() == DataType.INT) {
					values.add(Integer.parseInt(temp_value.substring(start_index, end_index)));
				}else if (parseBean.getDataType() == DataType.DOUBLE) {
					values.add(Double.parseDouble(temp_value.substring(start_index, end_index)));
				}
			}else if (expression.startsWith("+")) {
				try{
					double tmp = Double.parseDouble(temp_value);
					values.add("'"+String.valueOf(tmp + Double.parseDouble(expression.substring(1))) +"'");
				}catch (Exception e) {
					values.add("'" + defaultStr + "'");
				}
			}else if (expression.startsWith("-")) {
				try{
					double tmp = Double.parseDouble(temp_value);
					values.add("'"+String.valueOf(tmp - Double.parseDouble(expression.substring(1))) +"'");
				}catch (Exception e) {
					values.add("'" + defaultStr + "'");
				}
			}else if (expression.startsWith("*")) {
				try{
					double tmp = Double.parseDouble(temp_value);
					values.add("'"+String.valueOf(tmp * Double.parseDouble(expression.substring(1))) +"'");
				}catch (Exception e) {
					values.add("'" + defaultStr + "'");
				}
			}else if (expression.startsWith("/")) {
				try{
					double tmp = Double.parseDouble(temp_value);
					values.add("'"+String.valueOf(tmp / Double.parseDouble(expression.substring(1))) +"'");
				}catch (Exception e) {
					values.add("'" + defaultStr + "'");
				}
			}
			else {
				values.add("'"+temp_value +"'");
			}
		}
		else{
			values.add("'"+defaultStr +"'");
		}
	}
	
	/**
	 * Parses the default element value.
	 *
	 * @param type 当前解析的资料的类型
	 * @param values 解析结果存放处
	 * @param parseBean XML配置文件中当前用到的bean
	 * @param recv_time 资料的接收时间
	 * @param last_modifi_time   资料的最后修改时间
	 * @Title: parseDefaultElementValue
	 * @Description: 要素配置为default时的处理方法
	 */
	private void parseDefaultElementValue(String type, List<Object> values, ParseBean parseBean, Date recv_time, Date last_modifi_time) {
		if(parseBean.getValue().equalsIgnoreCase("SYSDATE")) {
			values.add("'" +sFormat.format(new Date()) + "'");
		}else if (parseBean.getValue().equalsIgnoreCase("FILE_LAST_MODIF")) {
			values.add("'"+sFormat.format(last_modifi_time)+"'");
		}else if (parseBean.getValue().equalsIgnoreCase("D_DATA_ID")) {
			values.add("'"+PropertiesUtil.getAgmeDataMap().get(type).getSod_type()+"'");
		}else if(parseBean.getValue().equalsIgnoreCase("D_DATA_DPCID")){
			values.add("'" + PropertiesUtil.getAgmeDataMap().get(type).getDpc_type()+"'");
		}else if(parseBean.getValue().equalsIgnoreCase("V_TT")){
			values.add("'" + PropertiesUtil.getTT() + "'");
		}else if(parseBean.getValue().equalsIgnoreCase("C_CCCC")){
			values.add("'" + PropertiesUtil.getCCCC() + "'");
		}
		else {
			values.add(parseBean.getValue());
		}
	}

	/**
	 * Field.
	 *
	 * @param key the key
	 * @return Double
	 * @Title: field
	 * @Description: 风向方位转换函数
	 */
	public double field(String key){
		if(key.equals("PPN")) 
			return  0; 
		else if(key.equals("NNE"))
			return  22.5; 
		else if(key.equals("PNE"))
			return  45; 
		else if(key.equals("ENE"))
			return 67.5; 
		else if(key.equals("PPE"))
			return 90; 
		else if(key.equals("ESE"))
			return 112.5;
		else if(key.equals("PSE"))
			return  135; 
		else if(key.equals("SSE"))
			return 157.5; 
		else if(key.equals("PPS"))
			return  180; 
		else if(key.equals("SSW"))
			return 202.5; 
		else if(key.equals("PSW"))
			return  225; 
		else if(key.equals("WSW"))
			return 247.5;
		else if(key.equals("PPW"))
			return  270; 
		else if(key.equals("WNW"))
			return  292.5; 
		else if(key.equals("PNW"))
			return  315; 
		else if(key.equals("NNW"))
			return 337.5;
		else 
			return defautValue;
		
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ParseConfig.config = "config/Agme_2222_TAB.xml";
		PropertiesUtil.setConfigFileXml("config/Agme_2222_setups.xml");
		DecodeAgmeXML decodeAgmeXML = new DecodeAgmeXML();
//		decodeAgmeXML.decodeFile("D:\\HUAXIN\\Z_AGME_I_58321_20200301002600_O_MAT.xml", new Date());
//		System.out.print(decodeAgmeXML.parseResult.size());
		String msg=null;
		SAXReader saxReader=new SAXReader();
		try {
			FileInputStream in =new FileInputStream(new File("C:\\BaiduNetdiskDownload\\test\\E.0021.0002.R001\\Z_AGME_I_53505_20200410081100_O_MAT.xml"));
			 Reader read =new InputStreamReader(in, "GBK");  // GBK,GB2312
			 Document document=saxReader.read(read);
			msg=document.asXML();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(msg);
		decodeAgmeXML.decodeFile("Z_AGME_I_53505_20200410081100_O_MAT.xml", new Date(), msg);
		System.out.print(decodeAgmeXML.parseResult.size());
		
	}
}
