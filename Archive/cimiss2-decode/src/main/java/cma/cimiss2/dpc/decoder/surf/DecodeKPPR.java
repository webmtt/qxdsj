package cma.cimiss2.dpc.decoder.surf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationKppr;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;

import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
	Main class of decode the DPRK precipitation data<br>
	朝鲜降水资料解码主类
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cma.cimiss2.dpc.decoder.bean.surf.KPPR}。
 * </ol>
 * </li>
 * </ul>
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * ZCZC<br>
99216 06170 66000=<br>
99217 06170 66000=<br>
NNNN<br>
<strong>code:</strong><br>
 * DecodeKPPR decodeKPPR = new DecodeKPPR();<br>
 * ParseResult<KPPR> parseResult = decodeKPPR.DecodeFile(new File(String filepath));<br>
 * List<ReportInfo> list = parseResult.getReports();<br>
 * System.out.println(list.size());<br>
 * 
 * <strong>output:</strong><br>
 * 2<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 下午2:20:04   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeKPPR {
	/**
	 * 解码结果
	 */
	private ParseResult<SurfaceObservationKppr> parseResult = new ParseResult<SurfaceObservationKppr>(false); 
	/**
	 * 缺测默认值
	 */
	private static int defaultInt = 999999; 
	/**
	 * 日期时间格式
	 */
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHH");
	
	/**
	 * 朝鲜降水报文解码方法，封装成ParseResult  
	 * @param file 待解码的文件
	 * @return: ParseResult 解码结果集<KPPR>      
	 */
	@SuppressWarnings({ "resource", "static-access", "deprecation" })
	public ParseResult<SurfaceObservationKppr> DecodeFile(File file){
		// 判断文件是否存在
		if (file != null && file.exists() && file.isFile()) {
			// 判断是否为空文件
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			// 从文件名中获取时间
			Date dt = getDateFromFileName(file.getName());
			if(dt == null) {   // 从文件名中获取时间 错误
				return parseResult;
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
	        String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
	        Scanner scanner = null;
	        //获取文件流
	        try{
	        	read = new InputStreamReader(new FileInputStream(file), fileCode);
	        	scanner = new Scanner(read).useDelimiter("(\r\n)|\n"); // 用换行符读取文件
				while (scanner.hasNext()) {   // 文件会以“NNNN”结束，每行一个record，record以“=”结束
					// 获取一个完整的报文段
					String report = scanner.next();
					if(report.contains("ZCZC") || report.contains("NNNN"))
						continue;
					// 移除空行 
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					if(report.endsWith("=")){
						int len = report.length();
						report = report.substring(0, len - 1);
						String ele[] = report.trim().split("\\s+");
						if(ele != null && ele.length >= 3 && DecodeUtil.is_number(ele[1])){ // 每个record的要素至少有三段
							SurfaceObservationKppr kppr = new SurfaceObservationKppr();
							// group 1
							kppr.setStationNumberChina(ele[0]);
							// group 2
							if(ele[1].length() < 4 || Integer.parseInt(ele[1].substring(0, 2)) != dt.getDate() || Integer.parseInt(ele[1].substring(2, 4)) != dt.getHours()){
								ReportError re = new ReportError();
								re.setMessage("Datetime error!");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							}
							else{
								dt.setMinutes(0);
								dt.setSeconds(0);
								kppr.setObservationTime(dt);
								if(!TimeCheckUtil.checkTime(dt)){
									ReportError re = new ReportError();
									re.setMessage("DataTime out of range：time:"+dt+" stationCode:"+ele[0]);
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
								parseResult.put(new ReportInfo<SurfaceObservationKppr>(kppr, report));
							}
							// group 3
							kppr.setFKPPR_V13019(defaultInt);
							kppr.setFKPPR_V13023(defaultInt);
							if(ele[2].length() != 5){
								ReportError re = new ReportError();
								re.setMessage("Element length error!");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							}
							else{
								String timePre = ele[2].substring(0, 2); // 降雨时次
								int amount = defaultInt;
								if(DecodeUtil.is_number(ele[2]))
									amount = Integer.parseInt(ele[2].substring(2, 5));
								//小时降雨量缺省值
								
								if(timePre.equals("66")) // 6时到第二天6时
									kppr.setFKPPR_V13019(amount);
								else if(timePre.equals("77"))
									kppr.setFKPPR_V13023(amount);
								else{
									ReportError re = new ReportError();
									re.setMessage("Element error!");
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
							}
							// group 4
							if(ele.length == 4){
								if(ele[3].length() != 5){
									ReportError re = new ReportError();
									re.setMessage("Number of segments error!");
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
								else{
									String timePre = ele[3].substring(0, 2); //降雨时次
									int amount = defaultInt;
									if(DecodeUtil.is_number(ele[3]))
										amount = Integer.parseInt(ele[3].substring(2, 5));
									
									if(timePre.equals("66"))
										kppr.setFKPPR_V13019(amount);
									else if(timePre.equals("77"))
										kppr.setFKPPR_V13023(amount);
									else{
										ReportError re = new ReportError();
										re.setMessage("Element error!");
										re.setSegment(report);
										parseResult.put(re);
										continue;
									}
								}
							}
							parseResult.put(kppr);
							parseResult.setSuccess(true);
						} 
						else{
							ReportError re = new ReportError();
							re.setMessage("Report is short!");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("Report does not ends with '=' !");
						re.setSegment(report);
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
				try{
					if(scanner != null){
						scanner.close();
					}
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
	 *  从文件名获取资料时间  
	 * @param filename  解码文件名
	 * @return Date 文件名中的日期时间
	 */
	private Date getDateFromFileName(String filename) {
		// 获取文件名， 不带扩展名
		String fileName = FileUtil.getFileNameNoEx(filename);
		
		// 从文件名获取资料的 年、月、日、时
		String fileNameSplts[] = fileName.split("_", 5);
		if(fileNameSplts != null){
			try {
				Date date = simpleDateFormat.parse(fileNameSplts[4].trim()); 
				return date;
			} catch (ParseException e) {
				ReportError re = new ReportError();
				re.setMessage("filename simpleDateFormat error!");
				re.setSegment(filename);
				parseResult.put(re);
				return null;
			}
		}
		else{
			ReportError re = new ReportError();
			// 文件名格式错误
			re.setMessage("File name format error!");
			re.setSegment(fileName);
			parseResult.put(re);
			return null;
		}
	}
	
	/**
	 * 测试  
	 * @param args void      
	 */
	public static void main(String[] args) {
		DecodeKPPR decodeKPPR = new DecodeKPPR();
		ParseResult<SurfaceObservationKppr> parseResult = decodeKPPR.DecodeFile(new File("D:\\HUAXIN\\DataProcess\\A.0001.0036.R001\\2018110812\\W_kp-NMHS-pyongyang,MET_C_DKPY_2018110812.txt"));
		@SuppressWarnings("rawtypes")
		List<ReportInfo> list = parseResult.getReports();
		System.out.println(list.size());
	}

}