package cma.cimiss2.dpc.decoder.ocen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.Tide;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.utils.*;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Tide data <br>
 * 全球潮位站观测资料解码类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.ocean.Tide}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 154.75556 -002.180<br>
154.75903 -002.172<br>
154.76250 -002.173<br>
 * <strong>code:</strong><br>
 * DecodeTide decodeTide = new DecodeTide();<br>
 * ParseResult<Tide> parseResult = decodeTide.DecodeFile(new File(String filepath));<br>
 * List<Tide> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");<br>
 * System.out.println(sdf.format(list.get(0).getObservationTime()));<br>
 * <strong>output:</strong><br>
 * 3<br>
 * 2017-06-03 18:08:00<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午4:41:33   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeTide{
	private ParseResult<Tide> parseResult = new ParseResult<Tide>(false);//解码结果集
	/**
	 * 全球潮位站观测资料解码函数  
	 * @param file 待解码文件
	 * @return ParseResult<Tide>     解码结果封装 
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	public ParseResult<Tide> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 解析文件名，获得台站号|资料年|月|日|传感器类型
			String fileN = file.getName();
			//文件名形式：Z_OCEN_C_CCCC_YYYYMMDDhhmmss_O_TIDE-abas_enc_019.txt
			String tmp1[];
			String tmp2[];
			String tmp3[];
			String stat = "";
			Date Mtime = null; //文件最后一次修改的时间
			String SensorType = "";
			int julianDay;
			int year;
			int month;
			int day;
			if(fileN != null && !fileN.isEmpty() && 
				(tmp1 = fileN.split("-")) != null && tmp1.length == 2 && 
				(tmp2 = tmp1[1].split("\\.")) != null &&  tmp2.length == 2 &&
				(tmp3 = tmp2[0].split("_")) != null &&  tmp3.length == 3 && isDigital(tmp3[2])){
					stat = tmp3[0];
					SensorType = tmp3[1];
					julianDay = Integer.parseInt(tmp3[2]);
					Mtime = new Date(file.lastModified());
					year = Mtime.getYear() + 1900;
					String dt = DateUtil.convertJulianDay2Date(year, julianDay); // 格式为：yyyyMMdd
					month = Integer.parseInt(dt.substring(4, 6));
					day = Integer.parseInt(dt.substring(6, 8));
			}else{
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				return parseResult;
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			InputStreamReader read = null;
			BufferedReader bufferedReader = null;
			 //获取文件流
			try{
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				String sp[];
				String secOfHour = "";
				while((lineTxt = bufferedReader.readLine()) != null){
					Tide tide = new Tide();
					tide.setStationNumberChina(stat);  // 台站号
					tide.setSensorType(SensorType);  // 传感器类型
					if((sp = lineTxt.split("\\s+")).length == 2 && isDigital(secOfHour = sp[0].substring(4, 9))){
						int secof = Integer.parseInt(secOfHour);
						int fsecof = (int)((secof / 100000.0) * 24 * 3600);
						int hour = fsecof / 3600;
						int min = (fsecof % 3600) / 60;
						int sec = (fsecof % 3600) % 60;
						Calendar calendar = Calendar.getInstance();
						calendar.set(year, month - 1, day, hour, min, sec);
						calendar.set(Calendar.MILLISECOND, 0);
						Date date = calendar.getTime(); 
						tide.setObservationTime(date); // 资料观测时间
						
						//2019-7-16 cuihongyuan
						if(!TimeCheckUtil.checkTime(tide.getObservationTime())){
							ReportError reportError = new ReportError();
							reportError.setMessage("time check error!");
							reportError.setSegment(sp[1]);
							parseResult.put(reportError);
							continue;
						}
						
						try{
							tide.setTidalHeightAboveChartDatum(Double.parseDouble(sp[1]));//本地海图基准面潮位高度
						}catch (Exception e) {
							tide.setTidalHeightAboveChartDatum(999999);
							ReportError re = new ReportError();
							re.setMessage("datetype error! put on 999999.");
							re.setSegment(lineTxt);
						}
						// 海温|自动水位检测|人工水位检测|气象残余潮位高度 已经在  new Tide() 时初始化为 999998，报文中无这些字段信息
						// 纬度|经度 已经在 new Tide() 时 初始化为 999999,报文中无这些字段信息
						parseResult.setSuccess(true);
						parseResult.put(tide);
						parseResult.put(new ReportInfo<Tide>(tide, lineTxt));
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("A number of errors in the message field or a non-numeric character except the decimal point");
						re.setSegment(lineTxt);
						parseResult.put(re);
						continue;
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
				try {
					if (bufferedReader != null) {
						bufferedReader.close();
					}
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
	 * @return: boolean   true: str为数值;false：str不为数值 
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
		DecodeTide decodeTide = new DecodeTide();
		
//		String aString = "001.2345".replaceAll(".", "");
//		boolean a = decodeTide.isDigital(aString);
//		
		ParseResult<Tide> parseResult = decodeTide.DecodeFile(new File("D:\\HUAXIN\\DataProcess\\dc_ocen_tide\\data\\Z_OCEN_C_HYJ_20170603183712_O_TIDE-zihu_flt_154.txt"));
		List<Tide> list = parseResult.getData();
		System.out.println(list.size());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(sdf.format(list.get(0).getObservationTime()));
	}
}