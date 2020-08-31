package cma.cimiss2.dpc.decoder.ocen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
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
public class DecodeOcen {
	public static double defautValue = 999999;
	public static String defaultStr = "999999";
	SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	OcenParseConfig cfg = OcenParseConfig.getParaeConfig();
	public static Map<String, Object> proMap=new HashMap<String, Object>();
	/**
	 * 获取XML配置文件的内容
	 */
	List<OcenParseBean> parseBeans = cfg.getParseBeans();
	
	/**
	 * 存放数据解析的结果集
	 */
	private ParseResult<String> parseResult = new ParseResult<String>(false);
	
	@SuppressWarnings("unlikely-arg-type")
	public ParseResult<String> decodeFile(String filename, String regx, Date recv_time,Map<String, Object> stationMap) {
//		List<String> sqls = new ArrayList<String>();
		proMap=stationMap;
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
						//判断是否存在汉字
						boolean isWords=checkcountname(items[0]);
						if(isWords) {
							continue;
						}
						if(filename.contains("HYZ")) {
							//从配置文件获取经纬度
							 String longtitude =
					                    getStationInfoFromConfig(items[3], "36", "longtitude");// 经度
					            String latitude =
					                    getStationInfoFromConfig(items[3], "36", "latitude");// 纬度
							if(longtitude.startsWith("999999")||latitude.startsWith("999999")) {
								continue;
							}
						}
						List<String> eles = new ArrayList<String>();
						List<Object> values = new ArrayList<Object>();
						if(OcenParseConfig.IsCheck) {
							if(items.length <= OcenParseConfig.reportLenth) {
								for (OcenParseBean parseBean : parseBeans) {
									
									eles.add(parseBean.getElementName());
									if(parseBean.getElementName().toUpperCase().equals("D_RECORD_ID")){
										values.add("'"+parseBean.getValue()+"'");
									}
									else if(parseBean.getContent() == Content.DEFAULT) {
										parseDefaultElementValue(values, parseBean, recv_time,filename,items[3]);
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
	//判断字符串中是否存在汉字
	public boolean checkcountname(String countname)
    {
         Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(countname);
            if (m.find()) {
                return true;
            }
            return false;
    }
	/**
     * 获取经纬度信息
     * 
     * @param stationNumberChina
     * @param stationTypeNo
     * @param key
     * @return String
     */
    private static String getStationInfoFromConfig(String stationNumberChina, String stationTypeNo,
            String key) {
        String resValue = "999999";
        try {
//            Map<String, Object> proMap = StationInfo.getProMap();
//        	 Map<String, Object> proMap =getStationMap();
            String info = (String) proMap.get(stationNumberChina + "+" + stationTypeNo);
            if(info != null){
	            String[] infos = info.split(",");
	           
	            if (infos.length < 10) {
	                return resValue;
	            } else {
	                key = key.toLowerCase();
	                if(key.equals("longtitude")) {
	                	 resValue = "null".equals(infos[1]) ? "999999" : infos[1];
	                }else if(key.equals("latitude")) {
	                	 resValue = "null".equals(infos[2]) ? "999999" : infos[2];
	                }
	            }
            }
            else
            	System.out.println("Station " + stationNumberChina + " not exist in StationInfo_Config.lua");
        } catch (Exception e) {
        	System.err.println("获取StationInfo_Config.lua 配置信息失败:"+e.getMessage());
        }
        return resValue;
    }
	private void parseElementValue(List<Object> values, OcenParseBean parseBean, String[] items, String fileName) {
		if(isNumeric(parseBean.getIndex())){
			int idx = Integer.parseInt(parseBean.getIndex());
			if(idx<items.length) {
				if(items[idx].equalsIgnoreCase("null"))
					items[idx] = "999998";
				if(items[idx].equalsIgnoreCase("—"))
					items[idx] = "999999";
				if(parseBean.getDataType() == DataType.STRING) {
					if(parseBean.getElementName().equals("V01301")&&items[idx].trim().length()==4&&!fileName.contains("_ZYC_")) {
						values.add("'0"+items[idx]+"'");
					}else {
					values.add("'"+items[idx]+"'");
					}
				}else if (parseBean.getDataType()==DataType.DOUBLE) {
					if(parseBean.getElementName().equals("V20001")&&items[idx].trim().length()>0){
						values.add(DecodeUtil.parseDouble(items[idx])*1000);
					}else if(parseBean.getElementName().equals("V11001")&&!fileName.contains("_HYZ_")) {
						if(items[idx].trim().startsWith("999.9")) {
							values.add(999999);
						}else 	if(items[idx].trim().equals("0")) {
							values.add(360);
						}else if(items[idx].trim().equals("361")) {
							values.add(999017);
						}else if(items[idx].trim().equals("362")) {
							values.add(999997);
						}else {
							values.add(DecodeUtil.parseDouble(items[idx]));
						}
					}
					else if(parseBean.getElementName().equals("V11001")&&items[idx].trim().startsWith("999.9")&&!fileName.contains("_ZYC_")){
						values.add(999999);
					}
					else {
					values.add(DecodeUtil.parseDouble(items[idx]));
					}
				}else if (parseBean.getDataType()== DataType.INT) {
					if(parseBean.getElementName().equals("V11001") || parseBean.getElementName().equals("V11296")){
						values.add(field(items[idx]));
					}
					else{
						values.add(DecodeUtil.parseInt(items[idx].replace("-", "")));
					}
				}else if(parseBean.getDataType() == DataType.DATETIME){
					String format = parseBean.getFormat();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
					try{
						Date date = simpleDateFormat.parse(items[idx]);
						Calendar cal = Calendar.getInstance();
				        cal.setTime(date);//date 换成已经已知的Date对象
				        cal.add(Calendar.HOUR_OF_DAY, -8);// before 8 hour
				        if(parseBean.getElementName().equals("D_DATETIME")) {
				        	values.add("'" + sFormat.format(cal.getTime()) + "'");
				        }else {
				        	values.add("'" + sFormat.format(date) + "'");
				        }
						
					}catch (Exception e) {
						if(parseBean.getElementName().equalsIgnoreCase("D_UPDATE_TIME")){
							Date date = new Date();
							values.add("'" + sFormat.format(date) + "'");
							System.out.println("D_UPDATE_TIME parse error, use system time!");
						}
						System.out.println("D_DATETIME parse error!");
					}
				}else {
					values.add(items[idx]);
				}
				}else {
					values.add("'" + 999999 + "'");
				}
		}
		else if(parseBean.getIndex().toLowerCase().startsWith("filename")){
//			System.out.println(parseBean.getElementName());
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
					if(val.length()==10) {
						val=val.substring(0, 4)+"-"+val.substring(4, 6)+"-"+val.substring(6, 8)+" "+val.substring(8, 10)+":00:00";
					}
					if(parseBean.getDataType() == DataType.DATETIME){
						String format = parseBean.getFormat();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
						try{
							Date date = simpleDateFormat.parse(val);
							Calendar cal = Calendar.getInstance();
					        cal.setTime(date);//date 换成已经已知的Date对象
					        cal.add(Calendar.HOUR_OF_DAY, -8);// before 8 hour
							values.add("'" + sFormat.format(cal.getTime()) + "'");
						}catch (Exception e) {
							System.out.println("D_DATETIME parse error!");
						}
					}
					else if(parseBean.getDataType() == DataType.STRING) {
						values.add("'"+val+"'");
					}else if (parseBean.getDataType()==DataType.DOUBLE) {
						values.add(DecodeUtil.parseDouble(val));
					}else if (parseBean.getDataType()== DataType.INT) {
						values.add(DecodeUtil.parseInt(val.replace("-", "")));
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

	private void calcElementValue(List<Object> values, OcenParseBean parseBean, String[] items, String fileName) {
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
//				System.out.println(parseBean.getElementName().toUpperCase());
				if(expression.startsWith("[") && expression.endsWith("]")) {
					int index = expression.indexOf(":");
					int start_index = Integer.parseInt(expression.substring(1, index));
					int end_index = Integer.parseInt(expression.substring(index+1, expression.length()-1));
					if(parseBean.getDataType() == DataType.INT) {
						values.add(Integer.parseInt(temp_value.substring(start_index, end_index).replace("-", "")));
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
			} // end else
		} // end if
	}

	private void parseDefaultElementValue(List<Object> values, OcenParseBean parseBean, Date last_modifi_time, String filename, String stationNumberChina) {
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
		}else if(filename.contains("HYZ")&&parseBean.getElementName().equalsIgnoreCase("V05001")) {
			values.add("'" + getStationInfoFromConfig(stationNumberChina, "36", "latitude")+ "'");
		}else if(filename.contains("HYZ")&&parseBean.getElementName().equalsIgnoreCase("V06001")) {
			values.add("'" + getStationInfoFromConfig(stationNumberChina, "36", "longtitude")+ "'");
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
		OcenParseConfig.config = "config/OCEN_HYZ_TAB.xml";
		PropertiesUtil.setConfigFile("config/OCEN_HYZ_setups.properties");
		String cts = PropertiesUtil.getCTSCode();
		String sod = PropertiesUtil.getSODCode();
		DecodeOcen decodeHKFlash = new DecodeOcen();
//		List<String> sqlList = processFile.decodeFile("D:\\observ_Japan_20180801080000_20180801083827.txt", ",", new Date());
		ParseResult<String> rs = decodeHKFlash.decodeFile("/Users/zhangliang/Documents/test/Z_OCEN_C_HYJ_20200527130010_O_HYZ_2020052717_2020052719.csv", ",", new Date(),null);
		List<String> sqlList = rs.getData();
		System.out.println(sqlList.size());
//		JapanDAO.into_sql(sqlList);
		for(int i=0;i<sqlList.size();i++) {
			System.out.println(sqlList.get(i));
		}
	}
}
