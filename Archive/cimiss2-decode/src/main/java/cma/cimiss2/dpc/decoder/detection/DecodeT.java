package cma.cimiss2.dpc.decoder.detection;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.detection.RadiMulChnPenTab;
import cma.cimiss2.dpc.decoder.bean.detection.RadiMulChnTenTab;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceObservationDataNation;

/**
 * *****************************************************************************************<br> 
 * <strong>All Rights Reserved By National Meteorological Information Centre (NMIC)</strong><br>
 * *****************************************************************************************<br>

 * Main class of decode the sunshine per day data. <br>
 * 常规地面日照日数据解码主类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.surf.SurfWeaChnSsdHor}。
 * </ol>
 * </li>
 * </ul>
 * <strong>sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 51810 385500 0773800 1 000<br>
20171208000000 00 00 00 00 00 00 00 02 10 10 10 10 10 10 10 05 00 00 00 00 00 00 00 00 077<br>
QC 0000000000000000000000000=<br>
NNNN<br>
<strong>code:</strong><br>
 * DecodeSS decoder = new DecodeSS();<br>
 * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");<br>
 * ParseResult<SurfWeaChnSsdHor> result = decoder.decode(file, new HashSet<String>());<br>
 * System.out.println("stationNumberChina="+result.getData().get(0).getStationNumberChina());<br>
 * System.out.println("observationTime="+sdf.format(result.getData().get(0).getObservationTime()));<br>
 *
 *<strong>output:</strong><br>
 * stationNumberChina=51810<br>
 * observationTime=20171208000000<br>
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
 */
public class DecodeT {

	/**
	 * 缺测默认值
	 */
	public String NANStr = "999999";

	/**
	 * 解码结果
	 */
	private ParseResult<RadiMulChnTenTab> parseResult = new ParseResult<RadiMulChnTenTab>(false) ;

	public String NiceForm(String STR, String form, float dev) {
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
	 * 日照数据数据解码方法，封装成ParseResult<T>
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

	/**
	 * 内部解码细节，不提供外部调用
	 * @param file     需解码的文件
	 * @param staSet   需要去重处理的站点信息，格式为：站点号_观测时间
	 * @param cla      解码后对应的实体类
	 * @return         返回一个List对象
	 */
	private List<?> decode(File file, HashSet<String> staSet, Class<?> cla) { // 国家站和区域站不分开
		List<Object> resultList = new ArrayList<Object>();
		try {
			String encoding = "utf-8";
			List<String> readList = new ArrayList<String>();
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (!lineTxt.equals("")) {
						readList.add(lineTxt);
					}
				}
				read.close();
			}
			
			if (readList.size()== 1) {
				parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
				System.out.println("ERROR IN FILE CONTENT");
				return null;
			}
			
			//循环所有数据
			for (int i = 1; i < readList.size(); i++) {
				boolean noErr = true;
				int lineNum = 0;
				StringBuffer baseBuf = new StringBuffer();
				ReportInfo<Map<String, String>> reportInfo = new ReportInfo<>();
				StringBuffer originalReport = new StringBuffer();
				originalReport.append(readList.get(i)).append("\r\n");
				String[] sec = readList.get(i).split(" ");
				List<String> list = new ArrayList<String>();
				for (int j = 0; j < sec.length; j++) {
					if(!"".equals(sec[j]) && null!=sec[j]){
						list.add(sec[j]);
					}
				}

				
				//转成实体类
				Object o = null;
				RadiMulChnTenTab data = new RadiMulChnTenTab();
				try {
					
					SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHH");
					data.setVersion(list.get(0));
					data.setModel(list.get(1));
					data.setFcstTime(Long.valueOf(list.get(2).substring(0,2)));
					
					data.setfBegTime(sdf2.parse(strtodate(list.get(3))));
					data.setfEndTime(sdf2.parse(strtodate(list.get(4))));
					data.setObsTime(Long.valueOf(list.get(5).substring(0,2)));
					data.setoBegTime(sdf2.parse(strtodate(list.get(6))));
					data.setoEndTime(sdf2.parse(strtodate(list.get(7))));
					data.setFcstVar(list.get(8));
					data.setFcstLev(list.get(9));
					data.setObsVar(list.get(10));
					data.setObsLev(list.get(11));
					data.setObtype(list.get(12));
					data.setVxMask(list.get(13));
					data.setMthd(list.get(14));
					data.setPnts(Double.valueOf(list.get(15)));
					data.setfThresh(list.get(16));
					data.setoThresh(list.get(17));
					data.setcThresh(list.get(18));
					data.setAlpha(list.get(19));
					data.setLineType(list.get(20));
					data.setMe(Double.valueOf(list.get(52)));
					data.setMae(Double.valueOf(list.get(65)));
					data.setMse(Double.valueOf(list.get(68)));
					data.setRmse(Double.valueOf(list.get(74)));
					data.setMe2(Double.valueOf(list.get(103)));
					
					
					reportInfo.setReport(originalReport.toString());
					parseResult.put(reportInfo);
					
					resultList.add(data);
					parseResult.put(data);
				}catch (Exception e) {
					ReportError re = new ReportError();
					re.setPositionx(i * 15 + 1);
					re.setSegment(readList.get(1 + i * 15));
					if(data!=null) {
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
		return resultList;
	}
	
	public String strtodate(String str){
		String[] split = str.split("_");
		String substring = StringUtils.substring(split[1], 0, 2);
		String data = split[0] + substring;
		return data;
	}
	
}
