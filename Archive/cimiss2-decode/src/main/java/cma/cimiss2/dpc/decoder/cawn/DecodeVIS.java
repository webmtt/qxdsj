package cma.cimiss2.dpc.decoder.cawn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.VIS;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the aerosol visibility data <br>
 * 气溶胶可见度VIS解码类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.cawn.VIS}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 54084,3337,2018,71,000008,00,15261,19384,-3<br>
54084,3337,2018,71,000023,00,15853,19492,0<br>
54084,3337,2018,71,000038,00,16532,19577,2<br>
54084,3337,2018,71,000053,00,17013,19505,5<br>
54084,3337,2018,71,000108,00,17809,19461,-1<br>
54084,3337,2018,71,000123,00,18426,19129,1<br>
......<br>
 * 
 * <strong>code:</strong><br>
 * DecodeVIS decodeVIS = new DecodeVIS();<br>
 * ParseResult<VIS> parseResult = decodeVIS.decodeFile(new File(String filepath));<br>
 * List<VIS> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).getVisibility_10min());<br>
 * <strong>output:</strong><br>
 * 240<br>
 * 19384.0<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午3:20:56   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeVIS{
	/**
	 * 封装解码结果
	 */
	private ParseResult<VIS> parseResult = new ParseResult<VIS>(false);
	/**
	 * 气溶胶可见度VIS解码函数
	 * @param file  待解码文件
	 * @return ParseResult<VIS> 解码结果      
	 */
	public ParseResult<VIS> decodeFile(File file){
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
			 //获取文件流
			try{
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				String sp[];
				String tmp;
				int month = 0;
				int date = 0;
				int hour = 0;
				int min = 0;
				int sec = 0;
				while((lineTxt = bufferedReader.readLine()) != null){
					lineTxt = lineTxt.trim();
					VIS vis = new VIS();
					if(lineTxt.isEmpty()) continue;
					lineTxt=lineTxt.replaceAll(",", ", ");
					if((sp = lineTxt.split(",")).length >= 5){
						// 1台站号
						if((tmp = sp[0].trim()).length() == 5)
							vis.setStationNumberChina(tmp);
						else{
							ReportError re = new ReportError();
							re.setMessage("Station format error!");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;
						}
						// 2项目代码
						if((tmp = sp[1].trim()).length() == 4 && isDigital(tmp))
							vis.setItemCode(Integer.parseInt(tmp));
						// 3观测资料年
						if((tmp = sp[2].trim()).length() == 4 && isDigital(tmp)){
							vis.setDataObservationYear(Integer.parseInt(tmp));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Observation year error!");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;
						}
						// 4观测序日
						if(isDigital(tmp = sp[3].trim()))
							vis.setJulianday(Integer.parseInt(tmp));
						else{
							ReportError re = new ReportError();
							re.setMessage("Julianday error!");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;
						}
						// 5观测时分秒
						if((tmp = sp[4].trim()).length() == 6 && isDigital(tmp)){
							vis.setObserHHmmss(tmp);
							String dt = DateUtil.convertJulianDay2Date(vis.getDataObservationYear(), vis.getJulianday()); // 格式为：yyyyMMdd
							month = Integer.parseInt(dt.substring(4, 6));
							date = Integer.parseInt(dt.substring(6, 8));
							hour = Integer.parseInt(tmp.substring(0, 2));
							min = Integer.parseInt(tmp.substring(2, 4));
							sec = Integer.parseInt(tmp.substring(4, 6));
							Calendar calendar = Calendar.getInstance();
							calendar.set(vis.getDataObservationYear(), month - 1, date, hour, min, sec);
							calendar.set(Calendar.MILLISECOND, 0);
							Date oTime = calendar.getTime(); 
							vis.setDataObservationTime(oTime);
							
							//2019-7-16 cuihongyuan
							if(!TimeCheckUtil.checkTime(vis.getDataObservationTime())){
								ReportError reportError = new ReportError();
								reportError.setMessage("time check error!");
								reportError.setSegment(lineTxt);
								parseResult.put(reportError);
								continue;
							}
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Obervation time error!");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;
						}
						// 6状态码
						if(sp.length >= 6 && isDigital(tmp = sp[5].trim())){
							vis.setStateCode(Integer.parseInt(tmp));
						}else{
							vis.setStateCode(999999);
						}
						// 1分钟平均能见度（米）
						if(sp.length >= 7 && isNumeric(tmp = sp[6].trim())){
							vis.setVisibility_1min(Double.parseDouble(tmp));
						}else{
							vis.setVisibility_1min(999999);
						}
						//10分钟平均能见度（米）
						if(sp.length >= 8 && isNumeric(tmp = sp[7].trim())){
							vis.setVisibility_10min(Double.parseDouble(tmp));
						}else{
							vis.setVisibility_10min(999999);
						}
						// 能见度变化趋势
						if(sp.length >= 9 && isNumeric(tmp = sp[8].trim())){
							vis.setTrend(Double.parseDouble(tmp));
						}else{
							vis.setTrend(999999);
						}
						parseResult.setSuccess(true);
						parseResult.put(vis);
						parseResult.put(new ReportInfo<VIS>(vis, lineTxt));
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("Report header, ignore this line!");
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
					if(bufferedReader != null)
						bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	/**
	 * 判断字符串是否均为数字组成   
	 * @param str 待判断字符串     
	 * @return: boolean      true 或者 false
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
	/**
	 * 判断字符串是否为数字，包括整数和小数   
	 * @param str 待检验字符串
	 * @return Boolean     true 或者 flase 
	 */
	static Boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("^(\\-)?\\d+(.\\d+)?$");  
		Matcher isNum = pattern.matcher(str);  
		if (!isNum.matches()) {  
	        return false;  
	    }  
	    return true;  
	}
	
	public static void main(String[] args) {
		DecodeVIS decodeVIS = new DecodeVIS();
		ParseResult<VIS> parseResult = decodeVIS.decodeFile(new File
				("D:\\TEMP\\G.2.9.1\\Z_MAWN_I_44304_20190315000000_O_AER-FLD-VIS.TXT"));
		List<VIS> list = parseResult.getData();
		System.out.println(list.size());
		System.out.println(list.get(0).getVisibility_10min());
	}
}