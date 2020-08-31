package cma.cimiss2.dpc.decoder.sevp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.ForeignForecast;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the foreign city forecast data<br>
 * 国外城市预报资料	大数据平台	M.0049.0002.R001 *
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.sevp.ForeignForecast}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 01TX_01 -99.14032 30.04743 496.086548 15 15<br>
 1 33 21 3 3 1 1 00 01 54 39 0.00 0.00 3 3<br>
 2 33 22 3 3 1 1 00 01 44 71 0.00 0.00 7 7<br>
 3 33 21 3 3 1 1 00 01 65 47 0.00 0.00 25 25<br>
 4 34 22 3 3 1 0 00 01 68 70 1.37 1.32 40 54<br>
 5 31 21 3 3 1 0 00 03 71 89 0.00 3.45 25 61<br>
 6 32 21 3 3 1 0 01 08 94 92 0.00 6.55 35 65<br>
 7 31 22 3 3 1 2 02 08 99 99 0.00 20.95 35 99<br>
 8 30 22 3 3 1 1 02 03 100 98 0.00 2.51 25 55<br>
 9 32 19 4 5 1 0 00 00 90 20 0.00 0.00 3 1<br>
 10 33 21 4 4 1 1 00 01 41 44 0.00 0.00 3 7<br>
 11 31 20 4 4 1 1 00 01 65 22 0.00 0.00 3 5<br>
 12 31 20 4 4 0 1 00 00 44 1 0.00 0.00 9 7<br>
 13 32 20 4 5 0 0 00 00 3 0 0.00 0.00 5 3<br>
 14 34 22 3 5 0 0 00 00 3 9 0.00 0.00 2 14<br>
 15 33 21 4 3 1 1 00 00 6 6 0.00 0.00 5 5<br>
02GA_01 -83.78906 31.1799 100.30952 15 15<br>
 1 31 22 6 5 0 0 04 04 62 80 2.51 1.27 57 51<br>
......<br>
 * 
 *<strong>code:</strong><br>
 *DecodeForeignForecast decodeForeignForecast = new DecodeForeignForecast();<br>
 *ParseResult<ForeignForecast> parseResult = decodeForeignForecast.DecodeFile(new File(String filepath));<br>
 *List<ForeignForecast> list = parseResult.getData();<br>
 *System.out.println("报文条数：" + list.size());<br>
 *System.out.println("记录1，经度、纬度：" + list.get(0).getLongtitude() + " " + list.get(0).getLatitude());<br>
		
 * <strong>output:</strong><br>
 * 报文条数：75000<br>
 * 记录1，经度、纬度：-99.14032 30.04743<br>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月29日 上午10:13:45   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeForeignForecast{
	/**
	 * 解码结果封装
	 */
	private ParseResult<ForeignForecast> parseResult = new ParseResult<ForeignForecast>(false);
	/**
	 * 浮点缺省值
	 */
	public static double defaultF = 999999;
	/**
	 * 字符型缺省值
	 */
	private String default1 = "999999";
	/** 
	 * 报文字符型缺省值
	 */
	private String default2 = "9999.9";
	/**
	 * 国外城市预报资料解码   
	 * @param file 待解码文件
	 * @return ParseResult<ForeignForecast>      解码结果
	 */
	public ParseResult<ForeignForecast> DecodeFile(File file){
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
			BufferedReader bufferedReader = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
			String fname = file.getName().split("\\.")[0]; // 去除扩展名
			String sp [] = fname.split("_");
			int lenSP = sp.length;
			String ymdh;
			Date ReleaseTime = new Date();
			
			if(sp != null && sp.length >= 1 && (ymdh = sp[lenSP - 1]).contains("GSF-") && ymdh.length() == 14){
				try{
					ReleaseTime = simpleDateFormat.parse(ymdh.substring(4, 14));
					if(!TimeCheckUtil.checkTime(ReleaseTime)){
						ReportError re = new ReportError();
						re.setMessage("DataTime out of range：time:"+ReleaseTime);
						re.setSegment(fname);
						parseResult.put(re);
						return parseResult;
					}
				}catch (Exception e) {
					ReportError re = new ReportError();
					re.setMessage("Publish time format error(yyyyMMddHH)!");
					re.setSegment(fname);
					parseResult.put(re);
					return parseResult;
				}
			}
			else{
				ReportError re = new ReportError();
				re.setMessage("Filename error: cannot find publish time(yyyyMMddHH) or beyond process scope!");
				re.setSegment(fname);
				parseResult.put(re);
				return parseResult;
			}
			 //获取文件流
			try{
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				String sta = "";
				double longtitude = defaultF;
				double latitude = defaultF;
				double height = defaultF;
				int numOfEfficiency = 0;
				int eleNum = 0;
				int cursor = 0;
				String report = "";
				while((lineTxt = bufferedReader.readLine()) != null && !(lineTxt = lineTxt.trim()).equals("NNNN")){
					report = "";
					cursor = 0;
					eleNum = 0;
					report += (lineTxt + "\n");
					if(!lineTxt.isEmpty() && (sp = lineTxt.split("\\s+")).length == 6){
						sta = sp[0];
						if(isNumeric(sp[1])) longtitude = Double.parseDouble(sp[1]); 
						if(isNumeric(sp[2])) latitude = Double.parseDouble(sp[2]);
						if(isNumeric(sp[3])) height = Double.parseDouble(sp[3]);
						if(isDigital(sp[4])) numOfEfficiency = Integer.parseInt(sp[4]);
						if(isDigital(sp[5])) eleNum = Integer.parseInt(sp[5]);
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("Report header format error!");
						re.setSegment(lineTxt);
						parseResult.put(re);
						continue;
					}
					ForeignForecast forecast = null;
					while(cursor < eleNum && (lineTxt = bufferedReader.readLine()) != null){
						cursor ++;
						lineTxt = lineTxt.trim();
						report += (lineTxt + "\n");
						forecast = new ForeignForecast();
						forecast.setStationNumberChina(sta);
						forecast.setObservationTime(ReleaseTime);
						forecast.setLongtitude(longtitude);
						forecast.setLatitude(latitude);
						forecast.setHeight(height);
						forecast.setNumberOfForecastEfficiency(numOfEfficiency);
						forecast.setEleNum(eleNum);
						sp = lineTxt.split("\\s+");
						if(sp.length == 15){
							if(isDigital(sp[0])) forecast.setForecastNumberOfDays(Integer.parseInt(sp[0])); // 预报天数
							if(!sp[1].equals(default2) && isNumeric(sp[1])) forecast.setDayTemperature(Double.parseDouble(sp[1])); //白天温度
							if(!sp[2].equals(default2) && isNumeric(sp[2])) forecast.setNightTemperature(Double.parseDouble(sp[2]));//晚上温度
							if(!sp[3].equals(default1) && isNumeric(sp[3])) forecast.setDayWindDir(Double.parseDouble(sp[3])); //白天风向
							if(!sp[4].equals(default1) && isNumeric(sp[4])) forecast.setNightWindDir(Double.parseDouble(sp[4])); //晚上风向
							if(!sp[5].equals(default1) && isNumeric(sp[5])) forecast.setDayWindPower(Double.parseDouble(sp[5])); // 白天风力
							if(!sp[6].equals(default1) && isNumeric(sp[6])) forecast.setNightWindPower(Double.parseDouble(sp[6])); //晚上风力
							if(!sp[7].equals(default1) &&isDigital(sp[7])) forecast.setDayWeatherPheno(Integer.parseInt(sp[7])); // 白天天气现象
							if(!sp[8].equals(default1) && isDigital(sp[8])) forecast.setNightWeatherPheno(Integer.parseInt(sp[8])); // 晚上天气现象
							if(!sp[9].equals(default1) && isNumeric(sp[9])) forecast.setDayCloudCoverageRate(Double.parseDouble(sp[9])); // 白天云覆盖率
							if(!sp[10].equals(default1) &&isNumeric(sp[10])) forecast.setNightCloudCoverageRate(Double.parseDouble(sp[10])); //晚上云覆盖率
							if(!sp[11].equals(default2) && isNumeric(sp[11])) forecast.setDayRainfall(Double.parseDouble(sp[11]));//白天降水量
							if(!sp[12].equals(default2) && isNumeric(sp[12])) forecast.setNightRainfall(Double.parseDouble(sp[12])); // 晚上降水量
							if(!sp[13].equals(default1) && isDigital(sp[13])) forecast.setDayPrecipitationProbability(Integer.parseInt(sp[13])); // 白天降水概率
							if(!sp[14].equals(default1) && isDigital(sp[14])) forecast.setNightPrecipitationProbability(Integer.parseInt(sp[14])); //晚上降水概率
 						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Report content format error!");
							re.setSegment(lineTxt);
							parseResult.put(re);
						}
						parseResult.put(forecast);
						parseResult.setSuccess(true);
					}
					parseResult.put(new ReportInfo<ForeignForecast>(forecast, report));
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
					if(bufferedReader != null)
						bufferedReader.close();
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
	 * 判断字符串是否为数字，包括整数和小数   
	 * @param str 待检验字符串
	 * @return Boolean    true或者false  
	 */
	Boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("^(\\-)?\\d+(\\.\\d+)?$");  
		Matcher isNum = pattern.matcher(str);  
		if (!isNum.matches()) {  
	        return false;  
	    }  
	    return true;  
	}
	/**
	 * 判断字符串是否均为数字组成   
	 * @param str 待判断字符串     
	 * @return boolean      true或者false
	 */
	boolean isDigital(String str){
		int i = 0;
		for(i = 0; i < str.length(); i ++){
			if(str.charAt(i) > '9' || str.charAt(i) < '0')
				break;
		}
		if(i == str.length())
			return true;
		else return false;
	}
	
	public static void main(String[] args) {
		DecodeForeignForecast decodeForeignForecast = new DecodeForeignForecast();
		ParseResult<ForeignForecast> parseResult = decodeForeignForecast.DecodeFile(new File
				("D:\\DataTest\\M.0049.0002.R001\\Z_SEVP_C_BABJ_20190107110300_P_GSF-2019010712.TXT"));
		List<ForeignForecast> list = parseResult.getData();
		System.out.println("报文条数：" + list.size());
		System.out.println("记录1，经度、纬度：" + list.get(0).getLongtitude() + " " + list.get(0).getLatitude());
		
	}
	
}