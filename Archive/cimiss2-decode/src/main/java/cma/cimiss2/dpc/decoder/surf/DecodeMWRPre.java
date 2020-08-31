package cma.cimiss2.dpc.decoder.surf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationMwrpre;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.surf.Field_Array_MwrPre;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Precipitation of Ministry of water resources data<br>
 * 水利部降水资料解码<br>
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double进行基本运算。
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cma.cimiss2.dpc.decoder.bean.surf.MWRPre}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 
 * 时间     雨量     纬度     经度<br>
2018060100     0     4769.92     8684.898<br>
2018060100     0     4800.8361     8566.3028<br>
2018060100     0     4733.3333     8968.3333<br>
2018060100     0     4785.0278     8812.75<br>
2018060100     0     4810     8713.3333<br>
2018060100     8     4605     9015<br>
2018060100     0     4699.6666     8319.8611<br>
2018060100     0     4361.67     8180<br>
2018060100     0     4383.33     8065<br>
..........<br>
 * 
<strong>code:</strong><br>
 * DecodeMWRPre decodeMWRPre = new DecodeMWRPre();<br>
 * ParseResult<MWRPre> parseResult = decodeMWRPre.DecodeFile(new File(String filepath));<br>
 * List<ReportInfo> list = parseResult.getReports();<br>
 * System.out.println(list.size());<br>
 * 
 * <strong>output:</strong><br>
 * 34896<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 下午5:19:59   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeMWRPre{
	private ParseResult<SurfaceObservationMwrpre> parseResult = new ParseResult<SurfaceObservationMwrpre>(false);
	/**
	 * 缺测时替代值
	 */
	private static float defaultF = 999998f; 
	/**
	 * 台站缺省值
	 */
	private static String StationC = "999998";
	/**
	 * 水利部降水资料解码函数
	 * @param file   待解析的文件
	 * @return: ParseResult<MWRPre>  解码结果集
	 */
	@SuppressWarnings("resource")
	public ParseResult<SurfaceObservationMwrpre> DecodeFile(File file){
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
			String fileName = file.getName();
			Scanner scanner = null;
			try{
				 read = new InputStreamReader(new FileInputStream(file), fileCode);
				 scanner = new Scanner(read).useDelimiter("(\r\n)|\n"); // 用换行符读取文件
				 if(fileName.contains("PAD") || fileName.contains("P02")  || fileName.contains("P08")
						 || fileName.contains("P14") || fileName.contains("P20") || fileName.contains("pad") 
						 || fileName.contains("p02") || fileName.contains("p08") || fileName.contains("p14") 
						 || fileName.contains("p20")){				
					 //获取文件流
					 parseResult = decodeHasStation(scanner, fileName);
				 }else if(fileName.contains("DAY") ||fileName.contains("HOUR")){//日雨量与小时雨量的格式
					 parseResult = decodeDayOrHour(scanner, fileName);
				 }
		         else{
		        	 parseResult = decodeNoStation(scanner, fileName);
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
	 * @param str   待处理的字符
	 * @param hasStation 是否包含台站号
	 * @return: boolean      字段名称是否正确
	 */
	private boolean IsFieldNameLinemwr(String str, boolean hasStation){
		String sp[] = str.trim().split("\\s+");
		int i = 0;
		if(hasStation){
			if(sp.length == Field_Array_MwrPre.FIELD_ARRAY_SIZE){
				for(i = 0; i < Field_Array_MwrPre.FIELD_ARRAY_SIZE; i ++){
					sp[i] = sp[i].trim();  
					if(!sp[i].equals(Field_Array_MwrPre.getFieldArray(i).getDescription())){
						break;
					}
				}
				if(i == Field_Array_MwrPre.FIELD_ARRAY_SIZE)
					return true;
				else
					return false;
			}
		}
		else{
			if(sp.length == Field_Array_2.FIELD_ARRAY_SIZE){
				for(i = 0; i < Field_Array_2.FIELD_ARRAY_SIZE; i ++){
					sp[i] = sp[i].trim();  
					if(!sp[i].equals(Field_Array_2.getFieldArray(i).getDescription())){
						break;
					}
				}
				if(i == Field_Array_2.FIELD_ARRAY_SIZE)
					return true;
				else
					return false;
			}
		}
		return false;
	}
	/**
	 * 判断日雨量或小时雨量文件中的字段名自否正确
	 * @param str   待处理的字符
	 * @param hasStation 是否包含台站号
	 * @return: boolean      字段名称是否正确
	 */
	private boolean IsFieldNameLineDayOrHour(String str){
		String sp[] = str.trim().split("\\s+");
		int i = 0;
			if(sp.length == Field_Array_DayOrHour.FIELD_ARRAY_SIZE){
				for(i = 0; i < Field_Array_DayOrHour.FIELD_ARRAY_SIZE; i ++){
					sp[i] = sp[i].trim();  
					if(!sp[i].equals(Field_Array_DayOrHour.getFieldArray(i).getDescription())){
						break;
					}
				}
				if(i == Field_Array_DayOrHour.FIELD_ARRAY_SIZE)
					return true;
				else
					return false;
			}
		return false;
	}
	/**
	 * 有站号的水利部降水资料的解码
	 * @param scanner  输入Scanner对象
	 * @param fileName  待处理文件名
	 * @return ParseResult<MWRPre> 解码结果
	 */
	public ParseResult<SurfaceObservationMwrpre> decodeHasStation(Scanner scanner, String fileName){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
		if(scanner.hasNext()){  
			String fieldNames = scanner.next(); // 跳过第一行，为字段名称  
			//  站号     日期    雨量
			boolean isFieldLine = IsFieldNameLinemwr(fieldNames, true); // 判断字段名称行是否正确
			if(!isFieldLine){  //如果没有字段名称行或错误，不进行后续的数据解析
				ReportError re = new ReportError();
				re.setMessage("Element name error!");
				re.setSegment(fieldNames);
				parseResult.put(re);
				return parseResult;
			}
		} 
		else{
			parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
			return parseResult;
		}
		while (scanner.hasNext()) { 
			// 报文要素解析
			String report = scanner.next();
			// 移除空行 
//			report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
			if(!report.equals("")){
				SurfaceObservationMwrpre mwrPre = new SurfaceObservationMwrpre();
				String sp[] = report.split(",");
				if(sp.length == Field_Array_MwrPre.FIELD_ARRAY_SIZE){
					for(int i = 0; i < Field_Array_MwrPre.FIELD_ARRAY_SIZE; i ++){
						sp[i] = sp[i].trim(); // 去除多余引号和空格
					}
					mwrPre.setStationNumberChina(sp[0]);
					try{
						mwrPre.setObservationTime(simpleDateFormat.parse(sp[1]));
						if(!TimeCheckUtil.checkTime(simpleDateFormat.parse(sp[1]))){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+simpleDateFormat.parse(sp[1])+" stationCode:"+sp[0]);
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
					}catch(ParseException e){
						ReportError re = new ReportError();
						re.setMessage("DateTime formate error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					float rainfall = 0;
					parseResult.put(new ReportInfo<SurfaceObservationMwrpre>(mwrPre, report)); 
					try{
						rainfall = Float.parseFloat(sp[2]);
					}catch(Exception e){
						ReportError re = new ReportError();
						re.setMessage("Precipitation error!");
						re.setSegment(report);
						parseResult.put(re);
					}
					mwrPre.setDailyRainfall(defaultF);
					mwrPre.setHourlyRainfall(defaultF);
					if(fileName.contains("PAD") || fileName.contains("pad")){
						mwrPre.setDailyRainfall(rainfall / 10);
						mwrPre.setTimeScale("DAY");
					}
					else{
						mwrPre.setHourlyRainfall(rainfall / 10);
						mwrPre.setTimeScale("HOUR");
					}
					parseResult.put(mwrPre);
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
		} // end while loop
		return parseResult;
	}
	
	/**
	 * 没有站号的水利部降水资料的解码
	 * @param scanner  输入的Scanner对象
	 * @param fileName 待处理文件名
	 * @return ParseResult<MWRPre>    解码结果   
	 */
	public ParseResult<SurfaceObservationMwrpre> decodeNoStation(Scanner scanner, String fileName){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
		if(scanner.hasNext()){  
			String fieldNames = scanner.next(); // 跳过第一行，为字段名称  
			//  站号     日期    雨量
			boolean isFieldLine = IsFieldNameLinemwr(fieldNames, false); // 判断字段名称行是否正确
			if(!isFieldLine){  //如果没有字段名称行或错误，不进行后续的数据解析
				ReportError re = new ReportError();
				re.setMessage("Element name error!");
				re.setSegment(fieldNames);
				parseResult.put(re);
				return parseResult;
			}
		} 
		else{
			parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
			return parseResult;
		}
		while (scanner.hasNext()) { 
			// 报文要素解析
			String report = scanner.next();
			// 移除空行 
//			report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
			if(!report.equals("")){
				SurfaceObservationMwrpre mwrPre = new SurfaceObservationMwrpre();
				String sp[] = report.split("\\s+");
				mwrPre.setStationNumberChina(StationC);
				if(sp.length == Field_Array_2.FIELD_ARRAY_SIZE){
					for(int i = 0; i < Field_Array_2.FIELD_ARRAY_SIZE; i ++){
						sp[i] = sp[i].trim(); // 去除多余引号和空格
					}
					try{
						mwrPre.setObservationTime(simpleDateFormat.parse(sp[0]));
						if(!TimeCheckUtil.checkTime(simpleDateFormat.parse(sp[0]))){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+simpleDateFormat.parse(sp[0])+" stationCode:"+StationC);
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
					}catch(ParseException e){
						ReportError re = new ReportError();
						re.setMessage("DateTime formate error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					float rainfall = 0;
					parseResult.put(new ReportInfo<SurfaceObservationMwrpre>(mwrPre, report)); 
					try{
						rainfall = Float.parseFloat(sp[1]);
					}catch(Exception e){
						ReportError re = new ReportError();
						re.setMessage("Precipitation error!");
						re.setSegment(report);
						parseResult.put(re);
					}
					mwrPre.setDailyRainfall(defaultF);
					mwrPre.setHourlyRainfall(defaultF);
					
					if(fileName.contains("D") || fileName.contains("d")){
						mwrPre.setDailyRainfall(rainfall / 10);
						mwrPre.setTimeScale("DAY");
					}
					else{
						mwrPre.setHourlyRainfall(rainfall / 10);
						mwrPre.setTimeScale("HOUR");
					}
					try{
						mwrPre.setLatitude(Double.parseDouble(sp[2]) * 0.01);
						mwrPre.setLongtitude(Double.parseDouble(sp[3]) * 0.01);
						if(mwrPre.getLatitude() > 90.0){
							ReportError re = new ReportError();
							re.setMessage("Latitude is over 90, wrong!");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
					}catch (Exception e) {
						ReportError re = new ReportError();
						re.setMessage("Latitude or Longtitude error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					parseResult.put(mwrPre);
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
		} // end while loop
		return parseResult;
	}
	/**
	 * 
	  * 日雨量或小时雨量的水利部降水资料的解码
	 * @param scanner  输入Scanner对象
	 * @param fileName  待处理文件名
	 * @return ParseResult<MWRPre> 解码结果
	 */
	public ParseResult<SurfaceObservationMwrpre> decodeDayOrHour(Scanner scanner, String fileName){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
		if(scanner.hasNext()){  
			String fieldNames = scanner.next(); // 跳过第一行，为字段名称  
			//  时间           站号      站名      雨量     纬度     经度
			boolean isFieldLine = IsFieldNameLineDayOrHour(fieldNames); // 判断字段名称行是否正确
			if(!isFieldLine){  //如果没有字段名称行或错误，不进行后续的数据解析
				ReportError re = new ReportError();
				re.setMessage("Element name error!");
				re.setSegment(fieldNames);
				parseResult.put(re);
				return parseResult;
			}
		} 
		else{
			parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
			return parseResult;
		}
		while (scanner.hasNext()) { 
			// 报文要素解析
			String report = scanner.next();
			// 移除空行 
//			report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
			if(!report.equals("")){
				SurfaceObservationMwrpre mwrPre = new SurfaceObservationMwrpre();
				String sp[] = report.split(",");
				if(sp.length == Field_Array_DayOrHour.FIELD_ARRAY_SIZE){
					for(int i = 0; i < Field_Array_DayOrHour.FIELD_ARRAY_SIZE; i ++){
						sp[i] = sp[i].trim(); // 去除多余引号和空格
					}
					
					try{
						mwrPre.setObservationTime(simpleDateFormat.parse(sp[0]));//时间
						if(!TimeCheckUtil.checkTime(simpleDateFormat.parse(sp[0]))){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+simpleDateFormat.parse(sp[0])+" stationCode:"+sp[1]);
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
					}catch(ParseException e){
						ReportError re = new ReportError();
						re.setMessage("DateTime formate error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					mwrPre.setStationNumberChina(sp[1]);//站号
					
					parseResult.put(new ReportInfo<SurfaceObservationMwrpre>(mwrPre, report)); 
					float rainfall = 0;
					try{
						rainfall = Float.parseFloat(sp[3]);//雨量
					}catch(Exception e){
						ReportError re = new ReportError();
						re.setMessage("Precipitation error!");
						re.setSegment(report);
						parseResult.put(re);
					}
//					mwrPre.setDailyRainfall(defaultF);
//					mwrPre.setHourlyRainfall(defaultF);
					
					if(fileName.contains("DAY")){
						mwrPre.setDailyRainfall(rainfall / 10);
						mwrPre.setTimeScale("DAY");
					}
					else if(fileName.contains("HOUR")){
						mwrPre.setOneHourRainfall(rainfall / 10);
						mwrPre.setTimeScale("HOUR");
					}
					try{
						mwrPre.setLatitude(Double.parseDouble(sp[4]) * 0.01);
						mwrPre.setLongtitude(Double.parseDouble(sp[5]) * 0.01);
						if(mwrPre.getLatitude() > 90.0){
							ReportError re = new ReportError();
							re.setMessage("Latitude is over 90, wrong!");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
					}catch (Exception e) {
						ReportError re = new ReportError();
						re.setMessage("Latitude or Longtitude error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					parseResult.put(mwrPre);
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
		} // end while loop
		return parseResult;
	}
	
//	public static void main(String[] args) {
//		DecodeMWRPre decodeMWRPre = new DecodeMWRPre();
//		ParseResult<MWRPre> parseResult = decodeMWRPre.DecodeFile(new File("D:\\HUAXIN\\DataProcess\\mwrpre\\data\\2018060101\\2018060100D.txt"));
//		@SuppressWarnings("rawtypes")
//		List<ReportInfo> list = parseResult.getReports();
//		System.out.println(list.size());
//	}
}