package cma.cimiss2.dpc.decoder.surf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationMwrsoil;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 *  Main class of decode the soil moisture content of Ministry of water resources data<br>
 *	水利部土壤墒情资料解码
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java float进行基本运算。
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cma.cimiss2.dpc.decoder.bean.surf.MWRSoil}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 
 * 1600<br>
"测站编码","                      测站名称","                时间","深度含水率10cm","深度含水率20cm","深度含水率30cm","深度含水率40cm","深度含水率60cm","深度含水率80cm","深度含水率100cm"<br>
"50903421","                        何巷闸","    2018-05-14 08:00","26.7","26.9","","26.5","","",""<br>
"50830601","                          河溜","    2018-05-14 08:00","24","24.9","","24.9","","",""<br>
"50922332","                          皇庙","    2018-05-14 08:00","25.7","25.7","","25.8","","",""<br>
"50922331","                          皇庙","    2018-05-11 08:00","22.3","25.5","","24.9","","",""<br>
"50903176","                        五道沟","    2018-05-14 08:00","24","23.8","","23.9","","",""<br>
"50903175","                        五道沟","    2018-05-11 08:00","22.7","24.3","","21.8","","",""<br>
....................<br>
 * <strong>code:</strong><br>
 * DecodeMWRSoil decodeMWRSoil = new DecodeMWRSoil();<br>
 * ParseResult<MWRSoil> parseResult = decodeMWRSoil.DecodeFile(new File(String filepath));<br>
 * List<ReportInfo> list = parseResult.getReports();<br>
 * System.out.println(list.size());<br>
 * 
 * <strong>output:</strong><br>
 * 1280<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 下午4:56:45   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeMWRSoil{
	/**
	 * 存储解码结果
	 */
	private ParseResult<SurfaceObservationMwrsoil> parseResult = new ParseResult<SurfaceObservationMwrsoil>(false); 
	/**
	 *  缺测时替代值
	 */
	private static float defaultF = 999999f; 
	
	/**
	 * 水利部土壤墒情资料解码函数
	 * @param  file     待解析文件
	 * @return: ParseResult<MWRSoil>  解码结果集
	 */
	@SuppressWarnings("resource")
	public ParseResult<SurfaceObservationMwrsoil> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			@SuppressWarnings("static-access")
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			InputStreamReader read = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String reportAmount = "";
			String fieldNames = "";
			boolean isFieldLine = false;
			Scanner scanner = null;
			 //获取文件流
	        try{
	        	read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("(\r\n)|\n"); // 用换行符读取文件
				if(scanner.hasNext()){
					reportAmount = scanner.next(); // 跳过第一行，第一行为报文条数
				}
				if(scanner.hasNext()){  
					fieldNames = scanner.next(); // 跳过第二行，为字段名称  
					// "测站编码","                      测站名称","                时间","深度含水率10cm","深度含水率20cm","深度含水率30cm","深度含水率40cm","深度含水率60cm","深度含水率80cm","深度含水率100cm"
					isFieldLine = IsFieldNameLinemwr(fieldNames); // 判断字段名称行是否正确
					if(!isFieldLine){  //如果没有字段名称行，不进行后续的数据解析
						ReportError re = new ReportError();
						re.setMessage("Element name error!");
						re.setSegment(fieldNames);
						parseResult.put(re);
						return parseResult;
					}
				}
				while (scanner.hasNext()) { 
					// 报文要素解析
					String report = scanner.next();
					// 移除空行 
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					if(!report.equals("")){
						SurfaceObservationMwrsoil mwrSoil = new SurfaceObservationMwrsoil();
						String sp[] = report.split(",");
						if(sp.length == Field_Array_MwrSoil.FIELD_ARRAY_SIZE){
							for(int i = 0; i < Field_Array_MwrSoil.FIELD_ARRAY_SIZE; i ++){
								sp[i] = DropUselessSymbol(sp[i]); // 去除多余引号和空格
							}
							mwrSoil.setStationCode(sp[0]); // 测站编码
							mwrSoil.setStationName(sp[1]); // 测站名称
							Date date = new Date();
							try{
								date = simpleDateFormat.parse(sp[2]); // 时间
								Calendar calendar = Calendar.getInstance();   // 减去8小时
								calendar.setTime(date);
								calendar.set(10, calendar.get(10) - 8);
								date = calendar.getTime();
								mwrSoil.setObservationTime(date);
								if(!TimeCheckUtil.checkTime(date)){
									ReportError re = new ReportError();
									re.setMessage("DataTime out of range：time:"+date+" stationCode:"+sp[0]);
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
								parseResult.put(new ReportInfo<SurfaceObservationMwrsoil>(mwrSoil, report));  
							}catch (ParseException e) {
								ReportError re = new ReportError();
								re.setMessage("DateTime format error!");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							}
							int j = 3;
							if(sp[j].isEmpty())
								mwrSoil.setRateOfWaterContent_10cm(defaultF);
							else
								mwrSoil.setRateOfWaterContent_10cm(Float.parseFloat(sp[j]));
							j++;
							if(sp[j].isEmpty())
								mwrSoil.setRateOfWaterContent_20cm(defaultF);
							else
								mwrSoil.setRateOfWaterContent_20cm(Float.parseFloat(sp[j]));
							j++;
							if(sp[j].isEmpty())
								mwrSoil.setRateOfWaterContent_30cm(defaultF);
							else
								mwrSoil.setRateOfWaterContent_30cm(Float.parseFloat(sp[j]));
							j++;
							if(sp[j].isEmpty())
								mwrSoil.setRateOfWaterContent_40cm(defaultF);
							else
								mwrSoil.setRateOfWaterContent_40cm(Float.parseFloat(sp[j]));
							j++;
							if(sp[j].isEmpty())
								mwrSoil.setRateOfWaterContent_60cm(defaultF);
							else
								mwrSoil.setRateOfWaterContent_60cm(Float.parseFloat(sp[j]));
							j++;
							if(sp[j].isEmpty())
								mwrSoil.setRateOfWaterContent_80cm(defaultF);
							else
								mwrSoil.setRateOfWaterContent_80cm(Float.parseFloat(sp[j]));
							j++;
							if(sp[j].isEmpty())
								mwrSoil.setRateOfWaterContent_100cm(defaultF);
							else
								mwrSoil.setRateOfWaterContent_100cm(Float.parseFloat(sp[j]));
							
							parseResult.put(mwrSoil);
							parseResult.setSuccess(true);
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Number of segments error!");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
					}
				} 
	        }catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}  catch (Exception e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} finally {
				try {
					if (read != null) {
						read.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try{
					if(scanner != null)
						scanner.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	
	/**
	 * 判断字段名自否正确
	 * @param str  要判断的字符串
	 * @return: boolean    字段名称是否正确
	 */
	private boolean IsFieldNameLinemwr(String str){
		String sp[] = str.split(",");
		int i = 0;
		if(sp.length == Field_Array_MwrSoil.FIELD_ARRAY_SIZE){
			for(i = 0; i < Field_Array_MwrSoil.FIELD_ARRAY_SIZE; i ++){
				sp[i] = DropUselessSymbol(sp[i]);  //删除多余引号，获得字段名称
				if(!sp[i].equals(Field_Array_MwrSoil.getFieldArray(i).getDescription())){
					break;
				}
			}
			if(i == Field_Array_MwrSoil.FIELD_ARRAY_SIZE)
				return true;
			else
				return false;
		}
		else
			return false;
	}
	
	/**
	 * 删除多余的引号和空格
	 * @param  str 要处理的字符串   
	 * @return: String      处理后的字符串
	 */
	private String DropUselessSymbol(String str){
		int i = 0;
		if(str == null)
			return "";
		if(str.charAt(0) == '"') 
			str = str.substring(1);
		while(str.charAt(0) == ' ')
			str = str.substring(1);
		while(str.charAt(i) != '"')
			i ++;
		str = str.substring(0, i);
		return str;
	}
	/**
	 * 测试   
	 * @param args void      
	 */
//	public static void main(String[] args) {
//		DecodeMWRSoil decodeMWRSoil = new DecodeMWRSoil();
//		ParseResult<MWRSoil> parseResult = decodeMWRSoil.DecodeFile(new File("D:\\HUAXIN\\DataProcess\\mwrsoil\\data\\2018051408\\slbsoil2018051415.txt"));
//		@SuppressWarnings("rawtypes")
//		List<ReportInfo> list = parseResult.getReports();
//		System.out.println(list.size());
//	}
}