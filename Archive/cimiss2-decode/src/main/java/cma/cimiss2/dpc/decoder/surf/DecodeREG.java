package cma.cimiss2.dpc.decoder.surf;


import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataReg;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil2;

/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>

 * Main class of decode the area per hour data. <br>
 * 常规地面区域级数据解码主类
 * 
 * 
 * * <p>
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataReg}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong>sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * O3246 360302 1142339 00640 00648 4 000<br>
20171226151000 249 004 226 005 250 007 1501 247 005 224 009 1504 0000 -028 -028 1501 -029 1505 056 055 1501 028 -104 10225 10226 1501 10225 1502 //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// //// 10308 ///// ///// ////<br>
00000000000000000000////////////////////////////////////////////////////////////////////////////////////////////////////<br>
/// /// /// /// //// //////////////////////// /// //// ///// ///// ///// ///// ///// ///// /// /// /// // ///// ///// ///// ///// /////<br>
QC 000000000000000000000000000077777777777777777770777 000000000077777777777777777777777777777777777777777777777777=<br>

NNNN<br>
<strong>code:</strong><br>
 * DecodeREG decoder = new DecodeREG();<br>
 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");<br>
 * ParseResult<SurfaceObservationDataReg> result = decoder.decode(file, new HashSet<String>());<br>
 * System.out.println("stationNumberChina="+result.getData().get(0).getStationNumberChina());<br>
 * System.out.println("observationTime="+sdf.format(result.getData().get(0).getObservationTime()));<br>
 *
 * <strong>output:</strong><br>
 * stationNumberChina=O3246<br>
 * observationTime=20171226151000<br>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 11/23/2017   lihanjie    Initial creation.
 * </pre>
 * 
 * @author lihanjie
 *
 * 
 */
public class DecodeREG {

	/**
	 * 缺测默认值
	 */
	public String NANStr = "999999";

	/**
	 * 解码结果
	 */
	private ParseResult<SurfaceObservationDataReg> parseResult = new ParseResult<SurfaceObservationDataReg>(false);
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	/**
	 * 解码校验方法，用于校验报文每一个字段数据的合法性。
	 * @param STR   报文原始数据
	 * @param form  目标数据格式
	 * @param dev   比例尺
	 * @return      返回格式化后的结果
	 */
	public String NiceForm(String STR, String form, float dev) {
		String num;

		// String regEx="r'^[-+]?[0-9]+[\\.]?[0-9]+$'";
		String regEx = "^-?\\d+$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(STR);
		/*if (STR.trim().equals("000")&&form.equals("wind")) { // 风向特殊处理
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
	public String NewNiceForm(String STR, float in, String form, float dev) {
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
	 * 区域站站数据解码方法，封装成ParseResult<T>
	 *
	 * @param file
	 *            传入文件对象
	 * @param staSet
	 *            传入已解码数据中站点的Set对象
	 * @return    返回ParseResult<T>对象
	 */
	public ParseResult<SurfaceObservationDataReg> decode(File file, HashSet<String> staSet) {
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
	 * @param file     需解码的文件
	 * @param staSet   需要去重处理的站点信息，格式为：站点号_观测时间
	 * @param cla      解码后对应的实体类
	 * @return         返回一个List对象
	 */
	private List<?> Decode(File file, HashSet<String> staSet, Class<?> cla) { // 区域站
		List<Object> resultList = new ArrayList<Object>();
		// HashSet<String> staSet= new HashSet<String>();
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		try {
			int N = 5;
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
			}
			if (readList.size() == 0) {

				parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
				System.out.println("ERROR IN FILE CONTENT");
				return null;
			}

			if (readList.size() % N != 1) {

				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				System.out.println("ERROR IN FILE CONTENT");
				return null;
			}
			if (!readList.get(readList.size() - 1).equals("NNNN")) {

				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
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
				String[] PP = readList.get(1 + i * N).split(" ");
				originalReport.append(readList.get(1 + i * N)).append("\r\n");
				Map<String,String> headMap = new HashMap<String, String>();

				try {
					baseBuf.append(basicInfo[0].contains("/") ? NANStr : basicInfo[0]).append(" ");// station ID
//					if (staSet.contains(basicInfo[0]+"_"+PP[0])) {
//						System.out.println("站点重复：" + basicInfo[0]);
//						continue;
//					}
//					staSet.add(basicInfo[0]+"_"+PP[0]);
					headMap.put("V01301", basicInfo[0]);
					headMap.put("D_DATETIME", PP[0]);
					// added by cuihongyuan 2019-10-29
					try{
						Date date = new Date(simpleDateFormat.parse(PP[0]).getTime());
						if(TimeCheckUtil2.timeCheckUtil!= null && !TimeCheckUtil2.checkTime(date)){
							ReportError re = new ReportError();
							re.setMessage("check time error!");
//							re.setSegment(readList.get(0 + i * N));
							parseResult.put(re);
							continue;
						}
					}catch (Exception e) {
						ReportError re = new ReportError();
						re.setMessage("Time parse error!");
						re.setSegment(readList.get(0 + i * N));
						parseResult.put(re);
						e.printStackTrace();
					}
					// ended by cuihongyuan 2019-10-29
					
					// System.out.println(basicInfo[0]);
					if(Float.parseFloat(basicInfo[1].substring(2, 4)) >= 60f || 
							   Float.parseFloat(basicInfo[1].substring(4, 6)) >= 60f || 
							   Float.parseFloat(basicInfo[2].substring(3, 5)) >= 60f || 
							   Float.parseFloat(basicInfo[2].substring(5, 7)) >= 60f){
								ReportError re = new ReportError();
								re.setPositionx(i * N + lineNum);
								re.setSegment(readList.get(lineNum + i * N));
								re.setMessage("ERROR FORMAT");
								parseResult.put(re);
								System.err.println("文件读取错误，行号：" + (i * N + lineNum));
//								continue;
								noErr = false;
					}
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
					// System.out.println(basicInfo[1]);
					baseBuf.append(NiceForm(basicInfo[3], "%.1f", 10f))
							.append(" "); // heightS
					baseBuf.append(NiceForm(basicInfo[4], "%.1f", 10f))
							.append(" "); // heightP
					baseBuf.append(basicInfo[5].contains("/") ? NANStr : basicInfo[5]).append(" "); // ovserve mode
					baseBuf.append(basicInfo[6].contains("/") ? "000" : basicInfo[6]).append(" "); // modify flag

					headMap.put("V_BBB", basicInfo[6].contains("/") ? "000" : basicInfo[6]);
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

				/**
				 * 2 - Get The SENCON PARA
				 */
				try {
					List<Integer> temIndex = Arrays.asList(14, 15, 17, 23, 29, 30, 32, 34, 35, 37, 39, 40, 41, 42, 43,
							44, 45, 46);
					List<Integer> winIndex = Arrays.asList(2, 4, 6, 9, 11, 22, 24, 25, 27, 47, 48);
					List<Integer> winDir = Arrays.asList(1, 3, 5, 8, 10);
					for (int j = 0; j < PP.length; j++) {
						/*
						 * if(temIndex.contains(j)) { baseBuf.append(NewNiceForm(PP[j], 1000f, "%.1f",
						 * 10f)).append(" "); }else
						 */if (winIndex.contains(j) || temIndex.contains(j)) {
							baseBuf.append(NiceForm(PP[j], "%.1f", 10f)).append(" ");
						} else if (winDir.contains(j)) {
							baseBuf.append(NiceForm(PP[j], "wind", 1f)).append(" ");
						} else if (j == 13) { // 降水量
							baseBuf.append(NiceForm(PP[j], "%.1f", 10f)).append(" ");
						} else {
							//baseBuf.append(PP[j].contains("/") ? NANStr : PP[j]).append(" ");
							baseBuf.append(PP[j].contains("/") ? NANStr : ((PP[j].contains("-") ? NANStr : PP[j]))).append(" ");
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
				/**
				 * 3 - Get The precipitationEveryMinutes 120 byte
				 */
				try {
					String[] TH = readList.get(2 + i * N).split(" ");
					originalReport.append(readList.get(2 + i * N)).append("\r\n");
					baseBuf.append(TH[0]).append(" ");
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
				/**
				 * 4 - Get The 4th PARA
				 */
				try {
					String[] RE = readList.get(3 + i * N).split(" ");
					originalReport.append(readList.get(3 + i * N)).append("\r\n");
					for (int j = 0; j < RE.length; j++) {
						if (j == 6 || j == 5) { // 云状，云状编码
							baseBuf.append(RE[j]).append(" ");
						} else if (j == 0) { // 人工能见度
							baseBuf.append(NiceForm(RE[j], "%.1f", 10f)).append(" ");
						} else {
							baseBuf.append(RE[j].contains("/") ? NANStr : RE[j]).append(" ");
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

				// 质控1
				try {
					String[] QC1 = readList.get(4 + i * N).split(" ");
					originalReport.append(readList.get(4 + i * N)).append("\r\n");
					if (!QC1[0].equals("QC")) {
						ReportError re = new ReportError();
						re.setPositionx(i * N + lineNum);
						re.setSegment(readList.get(lineNum + i * N));
						re.setMessage("ERROR FORMAT");
						parseResult.put(re);
						System.out.println("ERROR IN GET QC");
						continue;
					}
					for (int k = 0; k < QC1[1].length(); k++) { // 第二段的质控
						baseBuf.append(QC1[1].charAt(k)).append(" ");
					}
					baseBuf.append(QC1[2]).append(" "); // 第三段的质控

					// 第四段没有质控
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
				Object o =  null;
				try {
					reportInfo.setReport(originalReport.toString());
					parseResult.put(reportInfo);
					if(noErr) {
						o = EntityFillUtil
								.fillVal_REG(SurfaceObservationDataReg.class, /* "output.txt", */ baseBuf.toString());
						SurfaceObservationDataReg entity = (SurfaceObservationDataReg) o;
						resultList.add(entity);
						parseResult.put(entity);
					}
				} catch (Exception e) {
					ReportError re = new ReportError();
					re.setPositionx(i * N + 1);
					re.setSegment(readList.get(1 + i * N));
					if(o!=null) {
						re.setMessage(o.toString());
					}else {
						re.setMessage(e.getMessage());
					}
					parseResult.put(re);
					System.err.println("文件读取错误，行号：" + (i * N + 1));
				}
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
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
				}catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return resultList;
	}
	public static void main(String []args){
		DecodeREG decodeREG = new DecodeREG();
		String filen = "D:\\HUAXIN\\对比\\dongao\\Z_SURF_C_BEPK-REG_20200402013735_O_AWS_FTM_PQC.txt";
		File file = new File(filen);
        ParseResult<SurfaceObservationDataReg> ss =
        		(ParseResult<SurfaceObservationDataReg>) decodeREG.decode(file, new HashSet<String>());
        System.out.println(ss.getData().size());
	}
}
