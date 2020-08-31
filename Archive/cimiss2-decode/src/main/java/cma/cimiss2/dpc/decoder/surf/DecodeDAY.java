package cma.cimiss2.dpc.decoder.surf;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataDay;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;


/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>

 * Main class of decode the var per day data. <br>
 * 常规地面日值日数据解码主类
 *  <p>
 * note:
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataDay}。
 * </ol>
 * </li>
 * </ul>
 * 
 *
 * <strong>sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 54024 442700 1191800 000<br>
20171111120000 00000 00000 /// //// /// /// ///// /// /// ///// //// /// ///<br>
(02,03,)02,03,.<br>
QC 0077777777777 0=<br>
NNNN<br>
 * <strong>code:</strong><br>
 * DecodeDAY decoder = new DecodeDAY();<br>
 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");<br>
 * ParseResult<SurfaceObservationDataDay> result = decoder.decode(file, new HashSet<String>());<br>
 * System.out.println("stationNumberChina="+result.getData().get(0).getStationNumberChina());<br>
 * System.out.println("observationTime="+sdf.format(result.getData().get(0).getObservationTime()));<br>
 *
 * <strong>output:</strong><br>
 * stationNumberChina=54024<br>
 * observationTime=20171111120000<br>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 11/20/2017   lihanjie    Initial creation.
 * </pre>
 * 
 * @author lihanjie<br>
 *
 *
 */
public class DecodeDAY {


	/**
	 * 缺测默认值
	 */
	public String NANStr = "999999";

	/**
	 * 解码结构
	 */
	private ParseResult<SurfaceObservationDataDay> parseResult = new ParseResult<SurfaceObservationDataDay>(false) ;

	/**
	 * 解码校验方法，用于校验报文每一个字段数据的合法性。
	 * @param STR   报文原始数据
	 * @param form  目标数据格式
	 * @param dev   比例尺
	 * @return      返回格式化后的结果
	 */
	private String NiceForm(String STR, String form, float dev) {
		String num;

		// String regEx="r'^[-+]?[0-9]+[\\.]?[0-9]+$'";
		String regEx = "^-?\\d+$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(STR);
		if (matcher.matches()) {
			num = String.format(form, Float.parseFloat(STR) / dev);
		} else if (STR.trim().equals("PPC")) {
			num = "999017";
		} else if (STR.trim().contains(",")) {
			num = "999990";
		} else {
			num = NANStr;
		}
		return num;
	}


	/**
	 * 国家站日值数据解码方法，封装成ParseResult<T>
	 * @param file   传入文件对象
	 * @param staSet 传入已解码数据中站点的Set对象
	 * @return
	 */
	public ParseResult<?> decode(File file, HashSet<String> staSet){
		this.decode(file, staSet, SurfaceObservationDataNation.class);
		if (this.parseResult.getData().size()>0) {
			parseResult.setSuccess(true);
		}else {
			parseResult.setSuccess(false);
		}
		return parseResult;
	}
	public ParseResult<?> decodeNoQC(File file, HashSet<String> staSet){
		this.decodeNoQC(file, staSet, SurfaceObservationDataNation.class);
		if (this.parseResult.getData().size()>0) {
			parseResult.setSuccess(true);
		}else {
			parseResult.setSuccess(false);
		}
		return parseResult;
	}

	/**
	 * 内部解码细节，不提供外部调用
	 * @param file     需解码的文件
	 * @param staSet   需要去重处理的站点信息，格式为：站点号_观测时间
	 * @param cla      解码后对应的实体类
	 * @return         返回一个List对象
	 */
	private List<?> decode(File file, HashSet<String> staSet, Class<?> cla) { // 国家站和区域站不分开
		List<Object> resultList = new ArrayList<Object>();
		BufferedReader bufferedReader = null;
		InputStreamReader read = null;
		try {
			int N = 4;
			String encoding = "utf-8";
			List<String> readList = new ArrayList<String>();
			if (file.isFile() && file.exists()) {
				read = new InputStreamReader(
						new FileInputStream(file), encoding);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (!lineTxt.equals("")) {
						readList.add(lineTxt);
					}
				}
			}
			if (readList.size() % N != 1) {
				parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
				System.out.println("ERROR IN FILE CONTENT");
				return null;
			}
			if (!readList.get(readList.size() - 1).equals("NNNN")) {
				parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
				System.out.println("ERROR IN FILE CONTENT");
				return null;
			}
			int nsta = readList.size() / N;
			//循环所有数据
			for (int i = 0; i < nsta; i++) {
				boolean noErr = true;
				int lineNum = 0;
				StringBuffer baseBuf = new StringBuffer();
				ReportInfo<Map<String, String>> reportInfo = new ReportInfo<>();
				StringBuffer originalReport = new StringBuffer();
				Map<String,String> headMap = new HashMap<String, String>();
				String[] basicInfo = readList.get(0 + i * N).split(" "); // 第一行
				String[] sec = readList.get(1 + i * N).split(" ");
				sec[0] = sec[0].substring(0, 8)+"000000";
				originalReport.append(readList.get(0 + i * N)).append("\r\n");
				try {
					baseBuf.append(
							basicInfo[0].contains("/") ? NANStr : basicInfo[0])
							.append(" ");// station ID
					headMap.put("V01301", basicInfo[0]);
					headMap.put("D_DATETIME", sec[0]);
					/*try{
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					    Date dataTime=simpleDateFormat.parse(sec[0]);
						if(!TimeCheckUtil.checkTime(dataTime)){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+dataTime+" stationCode:"+basicInfo[0]);
							re.setSegment(readList.get(lineNum + i * N));
							parseResult.put(re);
							noErr = false;
						}
					}catch (Exception e) {
						// TODO: handle exception
					}*/
//					if (staSet.contains(basicInfo[0]+"_"+sec[0])) {
//						System.out.println("站点重复：" + basicInfo[0]);
//						continue;
//					}
//					staSet.add(basicInfo[0]+"_"+sec[0]);
					// System.out.println(basicInfo[0]);
					DecimalFormat decimalFormat = new DecimalFormat("#.0000");
					double lat = (double)Float.parseFloat(basicInfo[1].substring(0, 2))
							+ (Float.parseFloat(basicInfo[1].substring(2, 4)) / 60f)
							+ (Float.parseFloat(basicInfo[1].substring(4, 6)) / 3600f);
					baseBuf.append(decimalFormat.format(lat)).append(" ");// lat
					headMap.put("V5001", decimalFormat.format(lat));
					double lon = (double)Float.parseFloat(basicInfo[2].substring(0, 3))
							+ (Float.parseFloat(basicInfo[2].substring(3, 5)) / 60f)
							+ (Float.parseFloat(basicInfo[2].substring(5, 7)) / 3600f);
					baseBuf.append(decimalFormat.format(lon)).append(" ");// lon
					headMap.put("V6001", decimalFormat.format(lon));
					baseBuf.append(
							basicInfo[3].contains("/") ? "000" : basicInfo[3])
							.append(" "); // modify flag

					headMap.put("V_BBB",basicInfo[3].contains("/") ? "000" : basicInfo[3]);

					reportInfo.setT(headMap);
					lineNum++;
				} catch (RuntimeException e) {
					ReportError re = new ReportError();
					re.setPositionx(i * N + lineNum);
					re.setSegment(readList.get(lineNum + i * N));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * N + lineNum));
					noErr = false;
				}

				//第二行
				try {
					originalReport.append(readList.get(1 + i * N)).append("\r\n");
					if (sec.length!=14) {
						ReportError re = new ReportError();
						re.setPositionx(i * N + lineNum);
						re.setSegment(readList.get(lineNum + i * N));
						parseResult.put(re);
						re.setMessage("ERROR FORMAT");
						System.out.println("ERROR IN FILE CONTENT");
//						continue;
						noErr = false;
					}

					baseBuf.append(
							sec[0].contains("/") ? NANStr : sec[0])
							.append(" "); // observe time

					for (int j = 1; j < sec.length; j++) {
						if(j==4) {//天气现象符号代码
							baseBuf.append(
									sec[j].contains("/") ? NANStr : sec[j])
									.append(" ");
						}else if(j>4&&j<11) {
							baseBuf.append(NiceForm(sec[j], "%.0f", 1f)).append(
									" ");
						}else if(j==12) { //风速
							baseBuf.append(NiceForm(sec[j], "%.0f", 1f)).append(
									" ");
						}else {
							baseBuf.append(NiceForm(sec[j], "%.1f", 10f))
									.append(" ");
						}
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * N + lineNum);
					re.setSegment(readList.get(lineNum + i * N));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * N + lineNum));
					noErr = false;
				}
				//天气现象 不定长 A格式  (03,)03,.
				String WE = readList.get(2 + i * N);
				originalReport.append(readList.get(2 + i * N)).append("\r\n");
				baseBuf.append(WE.trim().replaceAll(" ", "\t")).append(" ");
				//质控
				try {
					String[] QC = readList.get(3 + i * N).split(" ");
					originalReport.append(readList.get(3 + i * N)).append("\r\n");
					if (!(QC[0].equals("QC"))) {
						ReportError re = new ReportError();
						re.setPositionx(i * N + lineNum);
						re.setSegment(readList.get(lineNum + i * N));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN FILE CONTENT");
//						continue;
						noErr = false;
					}

					for (int j = 0; j < QC[1].length(); j++) {
						baseBuf.append(QC[1].charAt(j))
								.append(" ");
					}

					baseBuf.append(QC[2].charAt(0))
							.append(" ");
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * N + lineNum);
					re.setSegment(readList.get(lineNum + i * N));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * N + lineNum));
					noErr = false;
				}
			
				//转成实体类
				Object o = null;
				try {
					reportInfo.setReport(originalReport.toString());
					parseResult.put(reportInfo);
					if(noErr) {
						o = EntityFillUtil
								.fillVal_day(SurfaceObservationDataDay.class,
										/*"output.txt",*/ baseBuf.toString());
					SurfaceObservationDataDay entity = (SurfaceObservationDataDay) o;
					resultList.add(entity);
						parseResult.put(entity);
					}
				}catch (Exception e) {
					ReportError re = new ReportError();
					re.setPositionx(i * 15 + 1);
					re.setSegment(readList.get(1 + i * 15));
					if(o!=null) {
						re.setMessage(o.toString());
					}else {
						re.setMessage(e.getMessage());
					}
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + 1));
				}
			}
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(bufferedReader != null){
				try{
					bufferedReader.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(read != null){
				try{
					read.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return resultList;
	}

	/**
	 * 没有质控码的版本
	 * 内部解码细节，不提供外部调用
	 * @param file     需解码的文件
	 * @param staSet   需要去重处理的站点信息，格式为：站点号_观测时间
	 * @param cla      解码后对应的实体类
	 * @return         返回一个List对象
	 */
	private List<?> decodeNoQC(File file, HashSet<String> staSet, Class<?> cla) { // 国家站和区域站不分开
		List<Object> resultList = new ArrayList<Object>();
		BufferedReader bufferedReader = null;
		InputStreamReader read = null;
		try {
			int N = 3;
			String encoding = "utf-8";
			List<String> readList = new ArrayList<String>();
			if (file.isFile() && file.exists()) {
				read = new InputStreamReader(
						new FileInputStream(file), encoding);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (!lineTxt.equals("")) {
						readList.add(lineTxt);
					}
				}
			}
			if (readList.size() % N != 1) {
				parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
				System.out.println("ERROR IN FILE CONTENT");
				return null;
			}
			if (!readList.get(readList.size() - 1).equals("NNNN")) {
				parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
				System.out.println("ERROR IN FILE CONTENT");
				return null;
			}
			String fileName = file.getName();
		    String[] fileNameSplit = fileName.split("-");
			int nsta = readList.size() / N;
			//循环所有数据
			for (int i = 0; i < nsta; i++) {
				boolean noErr = true;
				int lineNum = 0;
				StringBuffer baseBuf = new StringBuffer();
				ReportInfo<Map<String, String>> reportInfo = new ReportInfo<>();
				StringBuffer originalReport = new StringBuffer();
				Map<String,String> headMap = new HashMap<String, String>();
				String[] basicInfo = readList.get(0 + i * N).split(" "); // 第一行
				String[] sec = readList.get(1 + i * N).split(" ");
				sec[0] = sec[0].substring(0, 8)+"000000";
				originalReport.append(readList.get(0 + i * N)).append("\r\n");
				try {
					baseBuf.append(
							basicInfo[0].contains("/") ? NANStr : basicInfo[0])
							.append(" ");// station ID
					headMap.put("V01301", basicInfo[0]);
					headMap.put("D_DATETIME", sec[0]);
//					if (staSet.contains(basicInfo[0]+"_"+sec[0])) {
//						System.out.println("站点重复：" + basicInfo[0]);
//						continue;
//					}
//					staSet.add(basicInfo[0]+"_"+sec[0]);
					// System.out.println(basicInfo[0]);
					DecimalFormat decimalFormat = new DecimalFormat("#.0000");
					double lat = (double)Float.parseFloat(basicInfo[1].substring(0, 2))
							+ (Float.parseFloat(basicInfo[1].substring(2, 4)) / 60f)
							+ (Float.parseFloat(basicInfo[1].substring(4, 6)) / 3600f);
					baseBuf.append(decimalFormat.format(lat)).append(" ");// lat
					headMap.put("V5001", decimalFormat.format(lat));
					double lon = (double)Float.parseFloat(basicInfo[2].substring(0, 3))
							+ (Float.parseFloat(basicInfo[2].substring(3, 5)) / 60f)
							+ (Float.parseFloat(basicInfo[2].substring(5, 7)) / 3600f);
					baseBuf.append(decimalFormat.format(lon)).append(" ");// lon
					headMap.put("V6001", decimalFormat.format(lon));
					
					String V_BBB = "000";
					if(fileNameSplit != null && fileNameSplit.length > 1) {
						if(fileNameSplit[1].substring(0, 3).startsWith("CC")) {
							V_BBB = fileNameSplit[1].substring(0, 3);
						}
					}
					baseBuf.append(V_BBB).append(" ");
					headMap.put("V_BBB",V_BBB);
					
					reportInfo.setT(headMap);
					lineNum++;
				} catch (RuntimeException e) {
					ReportError re = new ReportError();
					re.setPositionx(i * N + lineNum);
					re.setSegment(readList.get(lineNum + i * N));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * N + lineNum));
					noErr = false;
				}

				//第二行
				try {
					originalReport.append(readList.get(1 + i * N)).append("\r\n");
					if (sec.length!=14) {
						ReportError re = new ReportError();
						re.setPositionx(i * N + lineNum);
						re.setSegment(readList.get(lineNum + i * N));
						parseResult.put(re);
						re.setMessage("ERROR FORMAT");
						System.out.println("ERROR IN FILE CONTENT");
//						continue;
						noErr = false;
					}

					baseBuf.append(
							sec[0].contains("/") ? NANStr : sec[0])
							.append(" "); // observe time

					for (int j = 1; j < sec.length; j++) {
						if(j==4) {//天气现象符号代码
							baseBuf.append(
									sec[j].contains("/") ? NANStr : basicInfo[0])
									.append(" ");
						}else if(j>4&&j<11) {
							baseBuf.append(NiceForm(sec[j], "%.0f", 1f)).append(
									" ");
						}else if(j==12) { //风速
							baseBuf.append(NiceForm(sec[j], "%.0f", 1f)).append(
									" ");
						}else {
							baseBuf.append(NiceForm(sec[j], "%.1f", 10f))
									.append(" ");
						}
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * N + lineNum);
					re.setSegment(readList.get(lineNum + i * N));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * N + lineNum));
					noErr = false;
				}
				//第三行  天气现象 不定长 A格式  (03,)03,.
				String WE = readList.get(2 + i * N);
				WE = WE.substring(0, WE.length() -1 );
				originalReport.append(readList.get(2 + i * N)).append("\r\n");
				baseBuf.append(WE.trim().replaceAll(" ", "\t")).append(" ");
				//质控
				/*try {
					String[] QC = readList.get(3 + i * N).split(" ");
					originalReport.append(readList.get(3 + i * N)).append("\r\n");
					if (!(QC[0].equals("QC"))) {
						ReportError re = new ReportError();
						re.setPositionx(i * N + lineNum);
						re.setSegment(readList.get(lineNum + i * N));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN FILE CONTENT");
						noErr = false;
					}

					for (int j = 0; j < QC[1].length(); j++) {
						baseBuf.append(QC[1].charAt(j))
								.append(" ");
					}

					baseBuf.append(QC[2].charAt(0))
							.append(" ");
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * N + lineNum);
					re.setSegment(readList.get(lineNum + i * N));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * N + lineNum));
					noErr = false;
				}*/
				//手动给QC
				for (int j = 0; j < 15; j++) {
					baseBuf.append(0)
							.append(" ");
				}
				
				//转成实体类
				Object o = null;
				try {
					reportInfo.setReport(originalReport.toString());
					parseResult.put(reportInfo);
					if(noErr) {
						o = EntityFillUtil
								.fillVal_day(SurfaceObservationDataDay.class,
										/*"output.txt",*/ baseBuf.toString());
					SurfaceObservationDataDay entity = (SurfaceObservationDataDay) o;
					resultList.add(entity);
						parseResult.put(entity);
					}
				}catch (Exception e) {
					ReportError re = new ReportError();
					re.setPositionx(i * 15 + 1);
					re.setSegment(readList.get(1 + i * 15));
					if(o!=null) {
						re.setMessage(o.toString());
					}else {
						re.setMessage(e.getMessage());
					}
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + 1));
				}
			}
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		finally{
			if(bufferedReader != null){
				try{
					bufferedReader.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(read != null){
				try{
					read.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return resultList;
	}
}