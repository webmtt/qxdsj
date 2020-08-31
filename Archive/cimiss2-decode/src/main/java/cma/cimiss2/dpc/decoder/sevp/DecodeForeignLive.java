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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.ForeignLive;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Urban meteorological stations abroad data <br>
 * 国外城市气象站实况解码类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.sevp.ForeignLive}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
01TX_01 -99.14032 30.04743 496.086548 33 48 21 4 5.0 1015 01 16000 95 37<br>
02GA_01 -83.78906 31.1799 100.30952 30 55 20 6 1.9 1018 01 16000 82 32<br>
02G_01 -80.57729 40.61868 218.67456 26 67 20 3 3.9 1018 01 6000 84 27<br>
02KY_01 -84.17965 37.99008 303.32744 28 71 23 6 3.3 1017 01 11000 87 32<br>
02PR_01 -65.91637 18.22652 82.272112 30 62 23 3 7.5 1018 00 16000 51 34<br>
02PR_02 -65.966 18.18885 98.3455 30 62 23 3 7.5 1018 00 16000 51 34<br>
02PR_03 -65.81933 18.15253 80.218 30 62 23 3 7.5 1018 00 16000 51 34<br>
03PR_01 -66.27112 18.46815 -3144.96124 32 49 21 3 7.2 1018 00 16000 51 35<br>
......<br>
 * <strong>code:</strong><br>
 * DecodeForeignLive decodeForeignLive = new DecodeForeignLive();<br>
 * ParseResult<ForeignLive> parseResult = decodeForeignLive.DecodeFile(new File(String filepath));<br>
 * List<ForeignLive> list = parseResult.getData();<br>
 * System.out.println("报文条数：" + list.size());<br>
 * System.out.println("云覆盖率：" + list.get(0).getCloudCoverageRate() + "%");<br>
 * 
 * <strong>output:</strong><br>
 * 报文条数：5000<br>
 * 云覆盖率：95.0%<br>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月29日 上午9:47:29   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeForeignLive{
	/**
	 * 解码结果封装
	 */
	private ParseResult<ForeignLive> parseResult = new ParseResult<ForeignLive>(false);
	/**
	 * 缺省浮点值
	 */
	public static double defaultF = 999999;
	/**
	 * 缺省字符型值
	 */
	private String default1 = "999999";
	/**
	 * 报文缺省替代值
	 */
	private String default2 = "9999.9";
	/**
	 * 国外城市气象站实况资料解码  
	 * @param file  待解码文件
	 * @return ParseResult<ForeignLive>      解码结果封装
	 */
	public ParseResult<ForeignLive> DecodeFile(File file){
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
			if(sp != null && sp.length >= 1 && (ymdh = sp[lenSP - 1]).contains("GSL-") && ymdh.length() == 14){
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
				while((lineTxt = bufferedReader.readLine()) != null && !(lineTxt = lineTxt.trim()).equals("NNNN")){
					sp = lineTxt.split("\\s+");
					if(sp.length == 14){
						ForeignLive foreignLive = new ForeignLive();
						foreignLive.setObservationTime(ReleaseTime);
						foreignLive.setStationNumberChina(sp[0]);
						if(isNumeric(sp[1])) foreignLive.setLongtitude(Double.parseDouble(sp[1]));
						if(isNumeric(sp[2])) foreignLive.setLatitude(Double.parseDouble(sp[2]));
						if(isNumeric(sp[3])) foreignLive.setHeight(Double.parseDouble(sp[3]));
						if(!sp[4].equals(default2) && isNumeric(sp[4])) foreignLive.setTemperature(Double.parseDouble(sp[4]));
						if(!sp[5].equals(default1) && isNumeric(sp[5])) foreignLive.setHumidity(Double.parseDouble(sp[5]));
						if(!sp[6].equals(default2) && isNumeric(sp[6])) foreignLive.setDewpoint(Double.parseDouble(sp[6]));
						if(!sp[7].equals(default1)) foreignLive.setWindDir(sp[7]);
						if(!sp[8].equals(default2) && isNumeric(sp[8])) foreignLive.setWindSpeed(Double.parseDouble(sp[8]));
						if(!sp[9].equals(default1) && isNumeric(sp[9])) foreignLive.setAirPressure(Double.parseDouble(sp[9]));
						if(!sp[10].equals(default1) && isDigital(sp[10])) foreignLive.setWeatherPheno(Integer.parseInt(sp[10]));
						if(!sp[11].equals(default1) && isNumeric(sp[11])) foreignLive.setVisibility(Double.parseDouble(sp[11]));
						if(!sp[12].equals(default1) && isNumeric(sp[12])) foreignLive.setCloudCoverageRate(Double.parseDouble(sp[12]));
						if(!sp[13].equals(default2) && isNumeric(sp[13])) foreignLive.setSurfaceTemperature(Double.parseDouble(sp[13]));
						parseResult.put(foreignLive);
						parseResult.setSuccess(true);
						parseResult.put(new ReportInfo<ForeignLive>(foreignLive, lineTxt));
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("Report content format error!");
						re.setSegment(lineTxt);
						parseResult.put(re);
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
	 * @param str  待解码字符串
	 * @return Boolean     true或者false 
	 */
	static Boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("^(\\-)?\\d+(.\\d+)?$");  
		Matcher isNum = pattern.matcher(str);  
		if (!isNum.matches()) {  
	        return false;  
	    }  
	    return true;  
	}
	/**
	 * 判断字符串是否均为数字组成   
	 * @param str 待判断字符串     
	 * @return: boolean    true或者false  
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
	
//	public static void main(String[] args) {
//		DecodeForeignLive decodeForeignLive = new DecodeForeignLive();
//		ParseResult<ForeignLive> parseResult = decodeForeignLive.DecodeFile(new File
//				("D:\\HUAXIN\\DataProcess\\M.0049.0001.R001\\M.0049.0001.R001Dst\\2018061219\\Z_SEVP_C_BABJ_20180612192000_P_GSL-2018061219.TXT"));
//		List<ForeignLive> list = parseResult.getData();
//		System.out.println("报文条数：" + list.size());
//		System.out.println("云覆盖率：" + list.get(0).getCloudCoverageRate() + "%");
//	}
}