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
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil2;

/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>
 * 
 * Main class of decode the nation per hour data. <br>
 * 常规地面国家级数据解码主类
 * <p>
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 54902 2017121703000 355700 1150900 00493 00503 4 119 000<br>
PP 15102 ///// -379 -376 15102 //// 15102 ////<br>
TH 0961 0961 0300 0994 0201 0964 0959 1081 1116 031 031 0251 025<br>
RE 0000 00000 00000 00000 00000 // ///// ////<br>
WI 203 023 209 027 209 029 0238 205 027 229 047 0210 047 229 047 229<br>
DT 0911 0911 0300 0965 0201 1114 1018 1018 1006 0994 0981 0927 0877 0832 0873 0873 0300 0921 0201<br>
VV 15102 15102 15102 ////<br>
CW 0138 /// /// /// ///// //////////////////////// /// 00 // / / //<br>
SP //// /// /// /// /// /// / / /// ///<br>
MR 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000<br>
MW (05,03,02,)03,02,05,10,.<br>
QC<br>
Q1 03000303 0000000000000 00000333 0000000000000000 0000000000000000000 0003 033333303003 3333333333 000000000000000000000000000000000000000000000000000000000000<br>
Q2 27002828 0000000000000 00000777 0000000000000000 0000000000000000000 0008 077777707777 7777777777 000000000000000000000000000000000000000000000000000000000000<br>
Q3 99999999 9999999999999 99999999 9999999999999999 9999999999999999999 9999 999999999999 9999999999 999999999999999999999999999999999999999999999999999999999999=<br>
NNNN<br>
<strong>code:</strong><br>
 * DecodeBABJ decoder = new DecodeBABJ();<br>
 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");<br>
 * ParseResult<SurfaceObservationDataNation> result = decoder.decode(file, new HashSet<String>());<br>
 * System.out.println("stationNumberChina="+result.getData().get(0).getStationNumberChina());<br>
 * System.out.println("observationTime="+sdf.format(result.getData().get(0).getObservationTime()));<br>
 *
 * <strong>output:</strong><br>
 * stationNumberChina=54902<br>
 * observationTime=2017121703000<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 11/20/2017   lihanjie    Initial creation.
 * </pre>
 * 
 * @author lihanjie
 *
 *
 * 
 */
public class DecodeBABJ {
	/**
	 * 缺测默认值
	 */
	public static String NANStr = "999999";
	
	/**
	 * 解码结果
	 */
	private ParseResult<SurfaceObservationDataNation> parseResult = new ParseResult<SurfaceObservationDataNation>(
			false);

	/**
	 * 解码校验方法，用于校验报文每一个字段数据的合法性。
	 * @param STR   报文原始数据
	 * @param form  目标数据格式
	 * @param dev   比例尺
	 * @return      返回格式化后的结果
	 */
	public static String NiceForm(String STR, String form, float dev) {
		String num;

		// String regEx="r'^[-+]?[0-9]+[\\.]?[0-9]+$'";
		String regEx = "^-?\\d+$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(STR);
		/*if(STR.trim().equals("000")&&form.equals("wind")) {  //风向特殊处理
			return "360";
		}*/
		if(form.equals("wind")) {
			form = "%.0f";//"%.1f"--xzh
		}
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
	 * 解码校验方法，用于校验报文特殊字段数据的合法性。
	 * @param STR   报文原始数据
	 * @param in    编报基值
	 * @param form  目标数据格式
	 * @param dev   比例尺
	 * @return      返回格式化后的结果,计算公式为：(in-STR)/dev
	 */
	private String NewNiceForm(String STR, float in, String form, float dev) {
		String num;

		// String regEx="r'^[-+]?[0-9]+[\\.]?[0-9]+$'";
		String regEx = "^-?\\d+$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(STR);
		if (matcher.matches()) {
			num = String.format(form, (in - (Float.parseFloat(STR))) / dev);
		} else {
			num = NANStr;
		}
		return num;
	}

	/**
	 * 国家站数据解码方法，封装成ParseResult<T>
	 *
	 * @param file
	 *            传入文件对象
	 * @param staSet
	 *            传入已解码数据中站点的Set对象
	 * @return   返回ParseResult<T>对象
	 */
	public ParseResult<SurfaceObservationDataNation> decode(File file, HashSet<String> staSet) {
		this.Decode(file, staSet, SurfaceObservationDataNation.class);
		if (this.parseResult.getData().size()>0) {
			parseResult.setSuccess(true);
		}else {
			parseResult.setSuccess(false);
		}
		return parseResult;
	}

	/**
	 * 内部解码细节，不提供外部调用
	 *
	 * @param file     需要解码的文件
	 * @param staSet   需要去重的站点信息，格式为：站点号_观测时间
	 * @param cla      解码后对应的实体类
	 * @return         返回List对象
	 */
	private List<?> Decode(File file, HashSet<String> staSet, Class<?> cla) { // 国家站
		List<Object> resultList = new ArrayList<Object>();
		// HashSet<String> staSet= new HashSet<String>();
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			int N = 15;
			String encoding = "utf-8";
			// File file=new File(fileName);
			List<String> readList = new ArrayList<String>();
			if (file.isFile() && file.exists()) {
				 read = new InputStreamReader(new FileInputStream(file), encoding);
				 bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (!lineTxt.equals("")) {
						readList.add(lineTxt);
					}

				}
				
			}else{
				parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
				System.out.println("ERROR IN FILE CONTENT");
				return null;
			}

			if (readList.size() == 0) {

				parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
				System.out.println("ERROR IN FILE CONTENT");
				return null;
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
			
			for (int i = 0; i < nsta; i++) {
				boolean noErr = true;
				int lineNum = 0;
				StringBuffer baseBuf = new StringBuffer();
				ReportInfo<Map<String, String>> reportInfo = new ReportInfo<>();
				StringBuffer originalReport = new StringBuffer();
				String[] basicInfo = readList.get(0 + i * N).split(" "); // 第一行
				originalReport.append(readList.get(0 + i * N)).append("\r\n");
				Map<String,String> headMap = new HashMap<String, String>();
				try {
					baseBuf.append(basicInfo[0].contains("/") ? NANStr : basicInfo[0]).append(" ");// station ID
					
//					if (staSet.contains(basicInfo[0]+"_"+basicInfo[1])) {
//						System.out.println("站点重复：" + basicInfo[0]);
//						continue;
//					}

//					staSet.add(basicInfo[0]+"_"+basicInfo[1]);
					headMap.put("V01301", basicInfo[0]);
					baseBuf.append(basicInfo[1].contains("/") ? NANStr : basicInfo[1]).append(" "); // observe time
					// System.out.println(basicInfo[0]);
					headMap.put("D_DATETIME", basicInfo[1]);
					
					// added by cuihongyuan 2019-10-29
					try{
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					    Date dataTime=simpleDateFormat.parse(basicInfo[1]);
						if(TimeCheckUtil2.timeCheckUtil != null && !TimeCheckUtil2.checkTime(dataTime)){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+dataTime+" stationCode:"+basicInfo[0]);
//							re.setSegment(readList.get(lineNum + i * 15));
							parseResult.put(re);
							noErr = false;
							continue;
						}
					}
					catch (Exception e) {
						// TODO: handle exception
					}
					// ended by cuihongyuan 2019-10-29
					
					if(Float.parseFloat(basicInfo[2].substring(2, 4)) >= 60f || 
					   Float.parseFloat(basicInfo[2].substring(4, 6)) >= 60f || 
					   Float.parseFloat(basicInfo[3].substring(3, 5)) >= 60f || 
					   Float.parseFloat(basicInfo[3].substring(5, 7)) >= 60f){
						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//						continue;
						noErr = false;
					}
					DecimalFormat decimalFormat = new DecimalFormat("#.0000");
					double lat = (double)Float.parseFloat(basicInfo[2].substring(0, 2))
							+ (Float.parseFloat(basicInfo[2].substring(2, 4)) / 60f)
							+ (Float.parseFloat(basicInfo[2].substring(4, 6)) / 3600f);
					baseBuf.append(decimalFormat.format(lat)).append(" ");// lat
					headMap.put("V5001",decimalFormat.format(lat));
					double lon = (double)Float.parseFloat(basicInfo[3].substring(0, 3))
							+ (Float.parseFloat(basicInfo[3].substring(3, 5)) / 60f)
							+ (Float.parseFloat(basicInfo[3].substring(5, 7)) / 3600f);
					baseBuf.append(decimalFormat.format(lon)).append(" ");// lon
					headMap.put("V6001", decimalFormat.format(lon));
					// System.out.println(basicInfo[1]);
					baseBuf.append(NiceForm(basicInfo[4].contains("/") ? NANStr : basicInfo[4], "%.1f", 10f))
							.append(" "); // heightS
					baseBuf.append(NiceForm(basicInfo[5].contains("/") ? NANStr : basicInfo[5], "%.1f", 10f))
							.append(" "); // heightP
					baseBuf.append(basicInfo[6].contains("/") ? NANStr : basicInfo[6]).append(" "); // ovserve mode
					baseBuf.append(basicInfo[7].contains("/") ? NANStr : basicInfo[7]).append(" "); // quality code
					baseBuf.append(basicInfo[8].contains("/") ? "000" : basicInfo[8]).append(" "); // modify flag
					headMap.put("V_BBB", basicInfo[8].contains("/") ? "000" : basicInfo[8]);

					reportInfo.setT(headMap);
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}

				/**
				 * 2 - Get The Pressure Information
				 */
				try {
					String[] PP = readList.get(1 + i * N).split(" ");
					originalReport.append(readList.get(1 + i * N)).append("\r\n");
					if (!PP[0].equals("PP")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET PP");
//						continue;
						noErr = false;
					}
					// int[] ppArray = new int[]{1, 2, 3, 4, 5, 7};
					for (int j = 1; j < PP.length; j++) {
						if (j == 6 || j == 8) { // highest time and lowest time
							if(PP[j].length() == 4){
								baseBuf.append(PP[j].contains("/") ? NANStr : PP[j]).append(" ");
							}else{
								baseBuf.append("999999").append(" ");
							}
						} else if (j == 3 || j == 4) { // 3h
							if(PP[j].length() == 4){
								baseBuf.append(NewNiceForm(PP[j], 1000f, "%.1f", 10f)).append(" ");
							}else{
								baseBuf.append("999999").append(" ");
							}
						} else {
							if(PP[j].length() == 5){
								baseBuf.append(NiceForm(PP[j], "%.1f", 10f)).append(" ");
							}else{
								baseBuf.append("999999").append(" ");
							}
						}
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				/**
				 * 3 - Get The Temperture and Humidity Information
				 */
				try {
					String[] TH = readList.get(2 + i * N).split(" ");
					originalReport.append(readList.get(2 + i * N)).append("\r\n");
					if (!TH[0].equals("TH")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET TH");
//						continue;
						noErr = false;
					}
					// int[] thArray = new int[]{1, 2, 4, 6, 7, 8, 9};
					for (int j = 1; j < 14; j++) {
						if (j == 3 || j == 5 || j == 12) {
							baseBuf.append(TH[j].contains("/") ? NANStr : TH[j]).append(" ");
						} else if (j == 10 || j == 11) {
							baseBuf.append(NiceForm(TH[j], "%.0f", 1f)).append(" ");
						} else if (j == 13) {
							baseBuf.append(NiceForm(TH[13], "%.1f", 10f)).append(" ");
						} else {
							baseBuf.append(NewNiceForm(TH[j], 1000f, "%.1f", 10f)).append(" ");
						}
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				/**
				 * 4 - Get The Evaporation and precipitation Information
				 */
				try {
					String[] RE = readList.get(3 + i * N).split(" ");
					originalReport.append(readList.get(3 + i * N)).append("\r\n");
					if (!RE[0].equals("RE")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET RE");
//						continue;
						noErr = false;
					}
					int[] reArray = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
					for (int j = 0; j < reArray.length; j++) {
						if (j == 2 || j == 3) { // 3 6
							if (RE[reArray[j]].trim().contains("990")) {
								baseBuf.append("999990 ");
								continue;
							}
						}
						if (j == 4) { // 24
							if (RE[reArray[j]].trim().contains("9999")) {
								baseBuf.append("999990 ");
								continue;
							}
						}
						if (j == 5) { // 人工加密观测降水量时间周期
							baseBuf.append(NiceForm(RE[reArray[j]], "%.0f", 1f)).append(" ");
							continue;
						}
						//如果没有特殊处理，统一进行下面的处理
						baseBuf.append(NiceForm(RE[reArray[j]], "%.1f", 10f)).append(" ");
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				/**
				 * 5 - Get The Wind Information
				 */
				try {
					String[] WI = readList.get(4 + i * N).split(" ");
					originalReport.append(readList.get(4 + i * N)).append("\r\n");
					if (!WI[0].equals("WI")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET WI");
//						continue;
						noErr = false;
					}
					int[] wiArray = new int[] { 1, 3, 5, 8, 10, 13, 15 }; // 7&12
					for (int j = 0; j < wiArray.length; j++) {
						if (wiArray[j] == 13) {
							baseBuf.append(WI[12].contains("/") ? NANStr : WI[12]).append(" ");
						}
						if (wiArray[j] >= 13) {
							baseBuf.append(NiceForm(WI[wiArray[j]], "%.1f", 10f)).append(" ");
							baseBuf.append(NiceForm(WI[wiArray[j] + 1], "wind", 1f)).append(" ");
						} else {
							baseBuf.append(NiceForm(WI[wiArray[j]], "wind", 1f)).append(" ");
							baseBuf.append(NiceForm(WI[wiArray[j] + 1], "%.1f", 10f)).append(" ");
						}
						if (wiArray[j] == 5) {
							baseBuf.append(WI[7].contains("/") ? NANStr : WI[7]).append(" ");
						}
						// System.out.println(NiceForm(WI[wiArray[j]],"%.0f",1f)+"==="+NiceForm(WI[wiArray[j]+1],"%.1f",10f));
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				/**
				 * 6 - Get The DiMian Temperture Information
				 */
				try {
					String[] DT = readList.get(5 + i * N).split(" ");
					originalReport.append(readList.get(5 + i * N)).append("\r\n");
					if (!DT[0].equals("DT")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET DT");
//						continue;
						noErr = false;
					}
					for (int j = 1; j < 20; j++) {
						if (j == 3 || j == 5 || j == 17 || j == 19) {
							baseBuf.append(DT[j].contains("/") ? NANStr : DT[j]).append(" ");
						} else {
							baseBuf.append(NewNiceForm(DT[j], 1000f, "%.1f", 10f)).append(" ");
						}
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				/**
				 * 7 - Get The voluntarily visibility Information
				 */
				try {
					String[] VV = readList.get(6 + i * N).split(" ");
					originalReport.append(readList.get(6 + i * N)).append("\r\n");
					if (!VV[0].equals("VV")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET VV");
//						continue;
						noErr = false;
					}
					for (int j = 1; j < VV.length; j++) {
						baseBuf.append(VV[j].contains("/") ? NANStr : VV[j]).append(" ");
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				/**
				 * 8 - Get The CW Information
				 */
				try {
					String[] CW = readList.get(7 + i * N).split(" ");
					originalReport.append(readList.get(7 + i * N)).append("\r\n");
					if (!CW[0].equals("CW")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET CW");
//						continue;
						noErr = false;
					}
					baseBuf.append(NiceForm(CW[1], "%.1f", 0.01f)).append(" ");
					int[] cwArray = new int[] { 2, 3, 4, 5 }; // 6-12
					for (int j = 0; j < cwArray.length; j++) {
						if (cwArray[j] != 5) {
							if (CW[cwArray[j]].equals("10-")) {
								baseBuf.append(100).append(" ");
							} else {
								baseBuf.append(NiceForm(CW[cwArray[j]], "%.0f", 0.1f)).append(" ");
							}
						} else {
							baseBuf.append(NiceForm(CW[cwArray[j]], "%.0f", 1f)).append(" ");
						}
					}
					baseBuf.append(CW[6]).append(" ");   //云状
					baseBuf.append(CW[7]).append(" ");	 //云状编码
					int[] cwArray1 = new int[] { 8, 9, 10, 11, 12 };
					for (int j = 0; j < cwArray1.length; j++) {
						baseBuf.append(CW[cwArray1[j]].contains("/") ? NANStr : CW[cwArray1[j]]).append(" ");
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				/**
				 * 9 - Get The SP Information
				 */
				try {
					String[] SP = readList.get(8 + i * N).split(" ");
					originalReport.append(readList.get(8 + i * N)).append("\r\n");
					if (!SP[0].equals("SP")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET CW");
//						continue;
						noErr = false;
					}
//					baseBuf.append(SP[1].contains(",") ? 0 : NiceForm(SP[1], "%.1f", 10f)).append(" ");
					baseBuf.append(NiceForm(SP[1], "%.1f", 10f)).append(" ");  //积雪深度
					baseBuf.append(NiceForm(SP[2], "%.1f", 10f)).append(" ");

					int[] spArray1 = new int[] { 3, 4, 5, 6, 7, 8 };
					for (int j = 0; j < spArray1.length; j++) {
//						if (SP[spArray1[j]].contains(",")) { //为啥..积雪深度的逗号代表啥?
//							baseBuf.append(0).append(" ");
//						} else {
							baseBuf.append(NiceForm(SP[spArray1[j]], "%.0f", 1f)).append(" ");
//						}
					}
					String tt = SP[9].substring(1);  //电线积冰直径
					int aa = -1;
					try {
						aa = Integer.parseInt(tt);
					}catch (Exception e) {
						// TODO: handle exception
					}
					if(tt.contains("/")||aa==-1) {
						baseBuf.append(NANStr).append(" ");
					}else if(aa<=26||(aa>=91&&aa<=97)||aa==99) {
						baseBuf.append("").append(" ");
					}else if(aa==27) {
						baseBuf.append("999990").append(" ");
					}else if(aa>=28&&aa<=55) {
						baseBuf.append(aa).append(" ");
					}else if(aa>=56&&aa<=90) {
						baseBuf.append((aa-50)*10).append(" ");
					}else if(aa==98) {
						baseBuf.append(400).append(" ");
					}else {
						baseBuf.append(NANStr).append(" ");
					}
					
					String tt1 = SP[10].substring(1);  //最大冰雹直径
					baseBuf.append(NiceForm(tt1, "%.0f", 1f)).append(" ");
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				/**
				 * 10 - Get The SP Information
				 */
				try {
					String[] MR = readList.get(9 + i * N).split(" ");
					originalReport.append(readList.get(9 + i * N)).append("\r\n");
					if (!MR[0].equals("MR")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET MR");
//						continue;
						noErr = false;
					}

					for (int j = 1; j < MR.length; j++) {
						if (MR[j].contains(",")) {
							baseBuf.append(0).append(" ");
						} else {
							baseBuf.append(MR[j]).append(" ");
						}
					}
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				/**
				 * 11 - Get The MW Information
				 */
				try {
					String[] MW = readList.get(10 + i * N).split(" ");
					originalReport.append(readList.get(10 + i * N)).append("\r\n");
					if (!MW[0].equals("MW")) {

						ReportError re = new ReportError();
						re.setPositionx(i * 15 + lineNum);
						re.setSegment(readList.get(lineNum + i * 15));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET CW");
//						continue;
						noErr = false;
					}

					String ss = "";
					for (int j = 1; j < MW.length; j++) {
						ss+=MW[j]+"\t";
					}
					baseBuf.append(ss).append(" ");
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				originalReport.append("QC").append("\r\n");
				// 质控1
				try {
					String[] QC1 = readList.get(12 + i * N).split(" ");
					originalReport.append(readList.get(12 + i * N)).append("\r\n");
					for (int j = 1; j < QC1.length - 1; j++) { // 最后一段不需要
						/*if (j == 4) { // 风
							int[] order = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 12, 15, 14 };
							for (int y = 0; y < order.length; y++) {
								baseBuf.append(QC1[j].charAt(order[y])).append(" ");
							}
						}else {
							for (int k = 0; k < QC1[j].length(); k++)
								baseBuf.append(QC1[j].charAt(k)).append(" ");
						}*/
						for (int k = 0; k < QC1[j].length(); k++)
							baseBuf.append(QC1[j].charAt(k)).append(" ");
					}
					baseBuf.append(QC1[QC1.length - 1]).append(" ");

					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}

				// 国家级质控
				try {
					String[] QC = readList.get(13 + i * N).split(" ");
					originalReport.append(readList.get(13 + i * N)).append("\r\n");
					for (int j = 1; j < QC.length - 1; j++) { // 最后一段不需要
						/*if (j == 4) { // 风
							int[] order = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 12, 15, 14 };
							for (int y = 0; y < order.length; y++) {
								baseBuf.append(QC[j].charAt(order[y])).append(" ");
							}
						}else {
							for (int k = 0; k < QC[j].length(); k++)
								baseBuf.append(QC[j].charAt(k)).append(" ");
						}*/
						for (int k = 0; k < QC[j].length(); k++)
							baseBuf.append(QC[j].charAt(k)).append(" ");
					}

					baseBuf.append(QC[QC.length - 1]).append(" ");
					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				// 质控2

				try {
					String[] QC2 = readList.get(14 + i * N).split(" ");
					originalReport.append(readList.get(14 + i * N)).append("\r\n");
					for (int j = 1; j < QC2.length - 1; j++) { // 最后一段不需要
						/*if (j == 4) { // 风
							int[] order = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 13, 12, 15, 14 };
							for (int y = 0; y < order.length; y++) {
								baseBuf.append(QC2[j].charAt(order[y])).append(" ");
							}
						}else {
							for (int k = 0; k < QC2[j].length(); k++)
								baseBuf.append(QC2[j].charAt(k)).append(" ");
						}*/
						for (int k = 0; k < QC2[j].length(); k++)
							baseBuf.append(QC2[j].charAt(k)).append(" ");
					}
					baseBuf.append(QC2[QC2.length - 1]).append(" "); // 最后有一个"="号

					lineNum++;
				} catch (RuntimeException e) {

					ReportError re = new ReportError();
					re.setPositionx(i * 15 + lineNum);
					re.setSegment(readList.get(lineNum + i * 15));
					re.setMessage(e.getMessage());
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + lineNum));
//					continue;
					noErr = false;
				}
				if(baseBuf.toString().split(" ").length!=374) {
					ReportError re = new ReportError();
					re.setPositionx(i * 15 + 1);
					re.setSegment(readList.get(1 + i * 15));
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * 15 + 1));
//					continue;
					noErr = false;
				}
				Object o = null;
				try {
					reportInfo.setReport(originalReport.toString());
					parseResult.put(reportInfo);
					if(noErr) {
						o=EntityFillUtil
								.fillVal(SurfaceObservationDataNation.class, /* "output.txt", */ baseBuf.toString());
					SurfaceObservationDataNation entity = (SurfaceObservationDataNation) o;
					resultList.add(entity);
						parseResult.put(entity);
					}
				}catch(Exception e) {
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

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			if(read != null){
				try{
					read.close();
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
			if(bufferedReader != null){
				try{
					bufferedReader.close();
				}catch(Exception e){
					
				}
			}
		}
		return resultList;
	}

}
