package cma.cimiss2.dpc.decoder.upar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.agme.Content;
import cma.cimiss2.dpc.decoder.agme.DataType;
import cma.cimiss2.dpc.decoder.agme.DecodeUtil;
import cma.cimiss2.dpc.decoder.agme.PropertiesUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *
 * <p>
 * notes:
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《香港闪电》 </a>
 *      <li> <a href=" "> 《》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年3月4日 上午11:36:55   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeHKFlash {
	public static double defautValue = 999999;
	public static String defaultStr = "999999";
	SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	HKFlashParseConfig cfg = HKFlashParseConfig.getParaeConfig();
	/**
	 * 获取XML配置文件的内容
	 */
	List<HKFlashParseBean> parseBeans = cfg.getParseBeans();
	
	/**
	 * 存放数据解析的结果集
	 */
	private ParseResult<String> parseResult = new ParseResult<String>(false);
	
	public ParseResult<String> decodeFile(String filename, String regx, Date recv_time) {
//		List<String> sqls = new ArrayList<String>();
		File file = new File(filename);
		
		if(file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			try {
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
				fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
				List<String> lineTxt = FileUtil.getTxtFileContent(file, fileCode);
				// 首先判断文件不是空的，然后需要判断最少有一行数据
				if (lineTxt != null && lineTxt.size() >= 1) {
					for (int i = 0; i < lineTxt.size(); i++) {
						// 判断是否为空行
						StringBuffer sql = new StringBuffer();
						sql.append("insert into "+PropertiesUtil.getTableName()+" (");
						
						String[] items = lineTxt.get(i).trim().split(regx);
						List<String> eles = new ArrayList<String>();
						List<Object> values = new ArrayList<Object>();
						if(HKFlashParseConfig.IsCheck) {
							if(items.length >= HKFlashParseConfig.reportLenth) {
								for (HKFlashParseBean parseBean : parseBeans) {
									eles.add(parseBean.getElementName());
									if(parseBean.getElementName().toUpperCase().equals("D_RECORD_ID")){
										values.add("'"+parseBean.getValue()+"'");
									}
									else if(parseBean.getContent() == Content.DEFAULT) {
										parseDefaultElementValue(values, parseBean, recv_time);
									}else if(parseBean.getContent() == Content.UNDIFINE){
										values.add("'"+parseBean.getValue()+"'");
									}
									else {
										if(parseBean.isIsCalc()) {
											calcElementValue(values, parseBean, items, filename);
										}else {
											parseElementValue(values, parseBean, items, filename);
										}
									}
								}
							}
							else{
								ReportError reportError = new ReportError();
								reportError.setMessage("Report length error!");
								reportError.setSegment(lineTxt.get(i));
								parseResult.put(reportError);
								continue;
							}
						}
						else{
							ReportError reportError = new ReportError();
							reportError.setMessage("Report length check error!");
							parseResult.put(reportError);
							continue;
						}
						
						Date date = new Date();
						for(int idx = 0; idx < eles.size(); idx ++){
							
							// 2019-7-16 cuihongyuan
							if(eles.get(idx).equalsIgnoreCase("D_DATETIME")){
								try {
									String valuedate=values.get(idx).toString();
									if(valuedate.contains("'")){
										 valuedate=valuedate.replaceAll("'", "");
									}
									date = sFormat.parse(valuedate);
									if(!TimeCheckUtil.checkTime(date)){
										ReportError re = new ReportError();
										re.setMessage("time check error!");
										re.setSegment(lineTxt.get(i));
										parseResult.put(re);
										continue;
									}
									
								} catch (ParseException e) {
									e.printStackTrace();
								}
							}
							// 以上 2019-7-16
							
							if(idx == eles.size() - 1)
								sql.append(eles.get(idx));
							else
								sql.append(eles.get(idx) + ",");
						}
							
						sql.append(") values (");
						for(int idx = 0; idx < values.size(); idx ++){
							if(idx == values.size() - 1)
								sql.append(values.get(idx));
							else
								sql.append(values.get(idx) + ",");
						}
						sql.append(")");
						parseResult.put(sql.toString());
						parseResult.setSuccess(true);
//						sqls.add(sql.toString());
					}
					
				} else {
					if (lineTxt == null || lineTxt.size() == 0) {
						// 空文件
						parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
					} else {
						// 数据只有一行，格式不正确
						parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
					}
				}
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}
		}else{
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	
	private void parseElementValue(List<Object> values, HKFlashParseBean parseBean, String[] items, String fileName) {
		if(isNumeric(parseBean.getIndex())){
			int idx = Integer.parseInt(parseBean.getIndex());
			if("null".equalsIgnoreCase(items[idx].trim()))
				items[idx]="999999";
			if(parseBean.getDataType() == DataType.STRING) {
				values.add("'"+items[idx]+"'");
			}else if (parseBean.getDataType()==DataType.DOUBLE) {
				values.add(DecodeUtil.parseDouble(items[idx]));
			}else if (parseBean.getDataType()== DataType.INT) {
				if(parseBean.getElementName().equals("V11001") || parseBean.getElementName().equals("V11296")){
					values.add(field(items[idx]));
				}
				else{
					values.add(DecodeUtil.parseInt(items[idx]));
				}
			}else if(parseBean.getDataType() == DataType.DATETIME){
				String format = parseBean.getFormat();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
				try{
					Date date = simpleDateFormat.parse(items[idx]);
					values.add("'" + sFormat.format(date) + "'");
				}catch (Exception e) {
					System.out.println("D_DATETIME parse error!");
				}
			}else {
				values.add(items[idx]);
			}
		}
		else if(parseBean.getIndex().toLowerCase().startsWith("filename")){
			String sep = "\\" + File.separator;
			String sp[] = fileName.split(sep);
			if(sp != null && sp.length > 0){
				String pureName = sp[sp.length - 1];
				String pureNameSp[] = pureName.split("[-_\\.]");
				String idxStr[] = parseBean.getIndex().split("\\{|\\}");
				int idx = Integer.parseInt(idxStr[1].trim());
				// 文件名中应获取的值段
				if(idx < pureNameSp.length){
					String val = pureNameSp[idx];
					if(parseBean.getDataType() == DataType.DATETIME){
						String format = parseBean.getFormat();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
						try{
							Date date = simpleDateFormat.parse(val);
							values.add("'" + sFormat.format(date) + "'");
						}catch (Exception e) {
							System.out.println("D_DATETIME parse error!");
						}
					}
					else if(parseBean.getDataType() == DataType.STRING) {
						values.add("'"+val+"'");
					}else if (parseBean.getDataType()==DataType.DOUBLE) {
						values.add(DecodeUtil.parseDouble(val));
					}else if (parseBean.getDataType()== DataType.INT) {
						values.add(DecodeUtil.parseInt(val));
					}else {
						values.add(val);
					}
				}else{
					values.add("'" + defaultStr + "'");
				}
			}
			else{
				values.add("'" + defaultStr + "'");
			}
		}
		else{
			values.add("'" + defaultStr + "'");
		}
	}

	private void calcElementValue(List<Object> values, HKFlashParseBean parseBean, String[] items, String fileName) {
		if(parseBean.getContent() == Content.REPORT) {
			String temp_value = "";
			String expression = parseBean.getExpression(); 
			// temp_value 赋值
			if(isNumeric(parseBean.getIndex())){
				int idx = Integer.parseInt(parseBean.getIndex());
				if("null".equalsIgnoreCase(items[idx].trim())){
					items[idx]="999999";
				}
				temp_value = items[idx].trim();
			}
			else if(parseBean.getIndex().toLowerCase().startsWith("filename")){
				String sep = File.separator;
				String sp[] = fileName.split("\\" + sep);
				if(sp != null && sp.length > 0){
					String pureName = sp[sp.length - 1];
					String pureNameSp[] = pureName.split("[-_\\.]");
					String idxStr[] = parseBean.getIndex().split("\\{|\\}");
					int idx = Integer.parseInt(idxStr[1]);
					// 文件名中应获取的值段
					if(idx < pureNameSp.length)
						temp_value = pureNameSp[idx];
				}
			}
			if(temp_value.equals("")){
				values.add("'" + defaultStr + "'");
			}
			else{
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
				}else {
					values.add("'"+temp_value +"'");
				}
			} // end else
		} // end if
	}

	private void parseDefaultElementValue(List<Object> values, HKFlashParseBean parseBean, Date last_modifi_time) {
		if(parseBean.getValue().equalsIgnoreCase("SYSDATE")) {
			values.add("'" +sFormat.format(new Date()) + "'");
		}else if (parseBean.getValue().equalsIgnoreCase("FILE_LAST_MODIF")) {
			values.add("'"+sFormat.format(last_modifi_time)+"'");
		}else if (parseBean.getValue().equalsIgnoreCase("D_DATA_ID")) {
			values.add("'"+PropertiesUtil.getSODCode()+"'");
		}else if (parseBean.getValue().equalsIgnoreCase("D_SOURCE_ID")) {
			values.add("'"+PropertiesUtil.getCTSCode()+"'");
		}else if(parseBean.getValue().equalsIgnoreCase("D_DATA_DPCID")){
			values.add("'" + PropertiesUtil.getDPCCode()+"'");
		}else if(parseBean.getValue().equalsIgnoreCase("V_TT")){
			values.add("'" + PropertiesUtil.getTT() + "'");
		}else if(parseBean.getValue().equalsIgnoreCase("C_CCCC")){
			values.add("'" + PropertiesUtil.getCCCC() + "'");
		}else if(parseBean.getDataType() == DataType.STRING){
			values.add("'" + parseBean.getValue() + "'");
		}else if(parseBean.getDataType() == DataType.DOUBLE){
			values.add(Double.parseDouble(parseBean.getValue()));
		}else if(parseBean.getDataType() == DataType.INT){
			values.add(Integer.parseInt(parseBean.getValue()));
		}else{
			values.add(parseBean.getValue());
		}
	}

	public static boolean isNumeric(String str){
		str = str.trim();
	    Pattern pattern = Pattern.compile("[0-9]*");
	    return pattern.matcher(str).matches();   
	}
	/**
	 * 
	 * @Title: field
	 * @Description: TODO(风向方位转换函数)
	 * @param key
	 * @return Double
	 * @throws：
	 */
	public double field(String key){
		if(key.toUpperCase().equals("N")) 
			return  0; 
		else if(key.toUpperCase().equals("NNE"))
			return  22.5; 
		else if(key.toUpperCase().equals("NE"))
			return  45; 
		else if(key.toUpperCase().equals("ENE"))
			return 67.5; 
		else if(key.toUpperCase().equals("E"))
			return 90; 
		else if(key.toUpperCase().equals("ESE"))
			return 112.5;
		else if(key.toUpperCase().equals("SE"))
			return  135; 
		else if(key.toUpperCase().equals("SSE"))
			return 157.5; 
		else if(key.toUpperCase().equals("S"))
			return  180; 
		else if(key.toUpperCase().equals("SSW"))
			return 202.5; 
		else if(key.toUpperCase().equals("SW"))
			return  225; 
		else if(key.toUpperCase().equals("WSW"))
			return 247.5;
		else if(key.toUpperCase().equals("W"))
			return  270; 
		else if(key.toUpperCase().equals("WNW"))
			return  292.5; 
		else if(key.toUpperCase().equals("NW"))
			return  315; 
		else if(key.toUpperCase().equals("NNW"))
			return 337.5;
		else if(key.toUpperCase().equals("C") || key.toUpperCase().equals("CALM"))
			return 999017;
		else 
			return defautValue;
		
	}
	
	
	public static void main(String[] args) {
		HKFlashParseConfig.config = "config/Light_3333_TAB.xml";
		PropertiesUtil.setConfigFile("config/Light_3333_setups.properties");
		String cts = PropertiesUtil.getCTSCode();
		String sod = PropertiesUtil.getSODCode();
		DecodeHKFlash decodeHKFlash = new DecodeHKFlash();
//		List<String> sqlList = processFile.decodeFile("D:\\observ_Japan_20180801080000_20180801083827.txt", ",", new Date());
		ParseResult<String> rs = decodeHKFlash.decodeFile("D:\\HUAXIN\\DataProcess\\闪电\\201802210412_lightning_3min_accum.txt", " ", new Date());
		List<String> sqlList = rs.getData();
		System.out.println(sqlList.size());
//		JapanDAO.into_sql(sqlList);
		
	}
}
