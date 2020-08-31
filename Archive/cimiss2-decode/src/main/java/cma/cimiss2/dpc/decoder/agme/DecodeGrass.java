package cma.cimiss2.dpc.decoder.agme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_01;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_02;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_03;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_04;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_05;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_06;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_07;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_08;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_09;
import cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass_10;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.enumeration.ObservationMethod;
import cma.cimiss2.dpc.decoder.tools.utils.StringUtil;

// TODO: Auto-generated Javadoc
/**
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 类描述： 畜牧要素数据文件解码 <br>
 * 1 牧草发育期 GRASS-01 4 <br>
 * 2 牧草生长高度 GRASS-02 3 <br>
 * 3 牧草产量 GRASS-03 5 <br>
 * 4 覆盖度及草层采食度 GRASS-04 5 <br>
 * 5 灌木、半灌木密度 GRASS-05 4 <br>
 * 6 家畜膘情等级调查 GRASS-06 4 <br>
 * 7 家畜羯羊重调查GRASS-07 7 <br>
 * 8 畜群基本情况调查 GRASS-08 16 <br>
 * 9 牧事活动调查 GRASS-09 4 <br>
 * 10 草层高度测量 GRASS-10 4<br>
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.agme.Agme_Grass}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 53821,363400,1071800,12556,99999,1,1@<br>
GRASS-01,1@<br>
20180426000000,02010003,01,9999@<br>
END_GRASS-01@<br>
=<br>
53821,363400,1071800,12556,99999,1,1@<br>
GRASS-01,1@<br>
20180428000000,02010003,01,9999@<br>
END_GRASS-01@<br>
=<br>
NNNN<br>
 * <strong>code:</strong><br>
 * 	DecodeGrass decodeGrass = new DecodeGrass();<br>
 * ParseResult<Agme_Grass> parseResult = decodeGrass.decodeFile(new File(String filepath));<br>
 * List<Agme_Grass> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).grassTypes);<br>
 * System.out.println(list.get(0).getAgmeGrass_01().get(0).getStationNumberChina());<br>
 * <strong>output:</strong><br>
 * 1<br>
 * [GRASS-01]<br>
 * 53821<br>
 * 
 *  <pre>
* SOFTWARE HISTORY
* Date         Engineer    Description
* ------------ ----------- --------------------------
* 2017年12月21日 下午11:58:05   wuzuoqiang    Initial creation.
 *  </pre>
 * 
 * @author wuzuoqiang
 * @version 0.0.1 
 */
public class DecodeGrass {
	
	/**  存放数据解析的结果集. */
	private ParseResult<Agme_Grass> parseResult = new ParseResult<Agme_Grass>(false);
	
	/** 牧草解码结果对象封装. */
	private Agme_Grass agmeGrass = new Agme_Grass();
    
    /**
     * 畜牧资料解码函数.
     *
     * @param file 待解码文件
     * @return ParseResult<Agme_Grass>       解码结果封装
     */
	@SuppressWarnings("resource")
	public ParseResult<Agme_Grass> decodeFile(File file) {
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			Scanner scanner = null;
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
				fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
				fileCode = fileCode.equals("GB2312") ? "GBK" : fileCode;
				fileCode = fileCode.equals("ASCII") ? "GBK" : fileCode;
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("\n=");
				while (scanner.hasNext()) {
					// 获取一个完整的报文段
					String report = scanner.next();
					char s = report.trim().charAt(0);
					if (s == 65279) { // 65279是空字符
						if (report.length() > 1) {
							report = report.substring(1);
						}
					}
					if (report.trim().equalsIgnoreCase("NNNN")) {
						// 报文结束
						break;
					}
					// 去掉无效的空格换行
					Pattern p = Pattern.compile("\\s*|\t|\r|\n");
					Matcher m = p.matcher(report);
					report = m.replaceAll("");
					// 数据第一行为报文头信息
					String[] reports = report.split("@");
					if (reports.length < 4) {
						ReportError reportError = new ReportError();
						reportError.setMessage("Empty message segment");
						reportError.setSegment(report);
						parseResult.put(reportError);
						continue;
					}

					String report_header = reports[0];
					String[] headers = report_header.split(",");
					if (headers.length == 7) {

						// 解析报文段头信息
						AgmeReportHeader agmeHeader = new AgmeReportHeader();
						try {
							// 区站号
							agmeHeader.setStationNumberChina(headers[0]);
							// 纬度 
							agmeHeader.setLatitude(ElementValUtil.getlatitude(headers[1]));
							// 经度
							agmeHeader.setLongitude(ElementValUtil.getLongitude(headers[2]));
							// 测站高度
							agmeHeader.setHeightOfSationGroundAboveMeanSeaLevel(
									ElementValUtil.ToBeValidDouble(headers[3], 0.1));
							// 气压传感器高度
							agmeHeader.setHeightOfBarometerAboveMeanSeaLevel(
									ElementValUtil.ToBeValidDouble(headers[4], 0.1));
							// 观测方式
							agmeHeader.setObservationMethod(ObservationMethod.MANUAL);
							// 报文记录数
							agmeHeader.setReporetCount(Integer.parseInt(headers[6]));
							reports = ArrayUtils.remove(reports, 0);
							if (processReports(reports, agmeHeader, report_header)) {
								parseResult.setSuccess(true);
							}

						} catch (Exception e) {
							ReportError reportError = new ReportError();
							reportError.setMessage("report header error");
							reportError.setSegment(report);
							parseResult.put(reportError);
							continue;
						}

					} else { // 报文头信息错误
						ReportError reportError = new ReportError();
						reportError.setMessage("report header error");
						reportError.setSegment(report);
						parseResult.put(reportError);
					}
				}

				parseResult.put(agmeGrass);
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}finally {
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

		} else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}

		return parseResult;
	}
    
    /**
     * 报文段解码原函数   .
     *
     * @param reports  待解码报文段
     * @param agmeHeaders  封装解码的报文头
     * @param report_header   报文头
     * @return boolean   是否成功解码
     */
	private boolean processReports(String[] reports, AgmeReportHeader agmeHeaders, String report_header) {
		AgmeReportHeader agmeHeader = new AgmeReportHeader(agmeHeaders);
		
		
		StringBuilder sb = new StringBuilder();
		sb.append(report_header + "@\n");
		try {
			String[] grassTypes = reports[0].split(",");
			sb.append(reports[0] + "@\n");
//			int grass_count = Integer.parseInt(grassTypes[1]);
			
			int ii = 0;
			while(ii < reports.length){
				if(!reports[ii].toUpperCase().startsWith("END_GRASS"))
					ii ++;
				else
					break;
			}
			int grass_count = ii - 1;
			
			reports = ArrayUtils.remove(reports, 0);
			String[] tempGrassS;
			switch (grassTypes[0]) {
			case "GRASS-01":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass01(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;
			case "GRASS-02":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass02(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;

			case "GRASS-03":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass03(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;

			case "GRASS-04":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass04(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;

			case "GRASS-05":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass05(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;

			case "GRASS-06":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass06(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;

			case "GRASS-07":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass07(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;

			case "GRASS-08":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass08(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;
			case "GRASS-09":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass09(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;

			case "GRASS-10":
				tempGrassS = ArrayUtils.subarray(reports, 0, grass_count);
				if (processGrass10(tempGrassS, agmeHeader) > 0) {
					if (!agmeGrass.grassTypes.contains(grassTypes[0])) {
						agmeGrass.grassTypes.add(grassTypes[0]);
					}
				}
				for (int i = 0; i < tempGrassS.length; i++) {
					sb.append(tempGrassS[i] + "@\n");
				}
				sb.append(reports[grass_count] + "@\n");
				reports = ArrayUtils.subarray(reports, grass_count + 1, reports.length);
				agmeHeader.setCropType(grassTypes[0]);
				parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, sb.toString()));
				if (reports.length >= 3) {
					processReports(reports, agmeHeaders, report_header);
				}
				break;

			default:
				ReportError reportError = new ReportError();
				reportError.setMessage("Unrecognized report");
				reportError.setSegment(ArrayUtils.toString(reports));
				parseResult.put(reportError);
				break;

			}
			return true;
		} catch (Exception e) {
			ReportError reportError = new ReportError();
			reportError.setMessage("report segment format conversion error");
			reportError.setSegment(ArrayUtils.toString(reports));
			parseResult.put(reportError);
			return false;
		}
	}

	/**
	 *  草层高度子要素 GRASS-10 解析函数.
	 *
	 * @param reports 待解析报文段
	 * @param agmeHeader  报文头封装
	 * @return int 解析出对象个数
	 */
	private int processGrass10(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			try {
				Agme_Grass_10 agme_Grass_10 = new Agme_Grass_10();
				// 观测时间 14 日期 年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				agme_Grass_10.setObservationTime(sdf.parse(items[0]));
				
				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(agme_Grass_10.getObservationTime())){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 草层类型
				agme_Grass_10.setGrassLayerType(ElementValUtil.ToBeValidInt(items[1]));
				// 测量场地
				agme_Grass_10.setMeasurementSite(ElementValUtil.ToBeValidInt(items[2]));
				// 草层高度
				agme_Grass_10.setGrassLayerHeight(ElementValUtil.ToBeValidDouble(items[3]));

				// 观测场拔海高度,
				agme_Grass_10.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_10.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_10.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_10.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_10.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_10().add(agme_Grass_10);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 牧事活动调查  GRASS-09 解码函数.
	 *
	 * @param reports 报文字符串
	 * @param agmeHeader  该报文段的报文头信息及站点基本信息
	 * @return int 解析成功的记录个数
	 */
	private int processGrass09(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			try {
				Agme_Grass_09 agme_Grass_09 = new Agme_Grass_09();
				// 观测时间 14 日期 年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 调查起始时间
				agme_Grass_09.setObservationStarTime(items[0]);
				

				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(sdf.parse(agme_Grass_09.getObservationStarTime()))){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 调查终止时间
				agme_Grass_09.setObservationEndTime(items[1]);
				// 牧事活动名称
				agme_Grass_09.setAnimalHusbandryName(ElementValUtil.ToBeValidInt(items[2]));
				// 生产性能
				agme_Grass_09.setProductPerformance(items[3]);

				// 观测场拔海高度,
				agme_Grass_09.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_09.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_09.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_09.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_09.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_09().add(agme_Grass_09);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 畜群基本情况调查 GRASS-08 解码函数.
	 *
	 * @param reports 报文字符串
	 * @param agmeHeader  该报文段的报文头信息及站点基本信息
	 * @return int 解析成功的记录个数
	 */
	private int processGrass08(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			// String[] items = reports[i].split(",");
			try {
				Agme_Grass_08 agme_Grass_08 = new Agme_Grass_08();
				// 观测时间 14 日期 年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				agme_Grass_08.setObservationTime(sdf.parse(items[0]));
				

				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(agme_Grass_08.getObservationTime())){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 春季日平均放牧时数
				agme_Grass_08.setAvgSpringDailyGrazHours(ElementValUtil.ToBeValidInt(items[1]));
				// 夏季日平均放牧时数
				agme_Grass_08.setAvgSummerDailyGrazHours(ElementValUtil.ToBeValidInt(items[2]));
				// 秋季日平均放牧时数
				agme_Grass_08.setAvgFallDailyGrazHours(ElementValUtil.ToBeValidInt(items[3]));
				// 冬季日平均放牧时数
				agme_Grass_08.setAvgWinterDailyGrazHours(ElementValUtil.ToBeValidInt(items[4]));
				// 有无棚舍
				agme_Grass_08.setIsHaveSuccah(ElementValUtil.ToBeValidInt(items[5]));
				// 棚舍数量
				agme_Grass_08.setSuccahNum(ElementValUtil.ToBeValidInt(items[6]));
				// 棚舍长
				agme_Grass_08.setSuccahLong(ElementValUtil.ToBeValidDouble(items[7], 0.1));
				// 棚舍宽
				agme_Grass_08.setSuccahWide(ElementValUtil.ToBeValidDouble(items[8], 0.1));
				// 棚舍高
				agme_Grass_08.setSuccahHigh(ElementValUtil.ToBeValidDouble(items[9], 0.1));
				// 棚舍结构
				agme_Grass_08.setSuccahFrame(items[10]);
				// 棚舍型式
				agme_Grass_08.setSuccahType(items[11]);
				// 棚舍门窗开向
				agme_Grass_08.setSuccahWinDirection(items[12]);
				// 畜群家畜名称
				agme_Grass_08.setLivestockName(items[13]);
				// 家畜品种
				agme_Grass_08.setLivestockBreeds(items[14]);
				// 畜群所属单位
				agme_Grass_08.setLivestockUnit(items[15]);

				// 观测场拔海高度,
				agme_Grass_08.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_08.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_08.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_08.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_08.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_08().add(agme_Grass_08);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 家畜羯羊重调查 GRASS-07 解码函数.
	 *
	 * @param reports 报文字符串
	 * @param agmeHeader  该报文段的报文头信息及站点基本信息
	 * @return int 解析成功的记录个数
	 */
	private int processGrass07(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			try {
				Agme_Grass_07 agme_Grass_07 = new Agme_Grass_07();
				// 观测时间 14 日期 年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				agme_Grass_07.setObservationTime(sdf.parse(items[0]));

				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(agme_Grass_07.getObservationTime())){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 羯羊_1体重
				agme_Grass_07.setRamWeight_1(ElementValUtil.ToBeValidDouble(items[1], 0.1));
				// 羯羊_2体重
				agme_Grass_07.setRamWeight_2(ElementValUtil.ToBeValidDouble(items[2], 0.1));
				// 羯羊_3体重
				agme_Grass_07.setRamWeight_3(ElementValUtil.ToBeValidDouble(items[3], 0.1));
				// 羯羊_4体重
				agme_Grass_07.setRamWeight_4(ElementValUtil.ToBeValidDouble(items[4], 0.1));
				// 羯羊_5体重
				agme_Grass_07.setRamWeight_5(ElementValUtil.ToBeValidDouble(items[5], 0.1));
				// 平均
				agme_Grass_07.setAvgWeight(ElementValUtil.ToBeValidDouble(items[6], 0.1));

				// 观测场拔海高度,
				agme_Grass_07.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_07.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_07.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_07.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_07.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_07().add(agme_Grass_07);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 家畜膘情等级与羯羊重调查 GRASS-06 解码函数.
	 *
	 * @param reports 报文字符串
	 * @param agmeHeader  该报文段的报文头信息及站点基本信息
	 * @return int 解析成功的记录个数
	 */
	private int processGrass06(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			try {
				Agme_Grass_06 agme_Grass_06 = new Agme_Grass_06();
				// 观测时间 14 日期 年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				agme_Grass_06.setObservationTime(sdf.parse(items[0]));
				

				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(agme_Grass_06.getObservationTime())){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 膘情等级
				agme_Grass_06.setRankCondition(ElementValUtil.ToBeValidInt(items[1]));
				// 成畜头数
				agme_Grass_06.setAdultAnimalNum(ElementValUtil.ToBeValidInt(items[2]));
				// 幼畜头数
				agme_Grass_06.setBabyHeadNum(ElementValUtil.ToBeValidInt(items[3]));

				// 观测场拔海高度,
				agme_Grass_06.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_06.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_06.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_06.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_06.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_06().add(agme_Grass_06);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 灌木半灌木密度 GRASS-05 解码函数.
	 *
	 * @param reports 报文字符串
	 * @param agmeHeader  该报文段的报文头信息及站点基本信息
	 * @return int 解析成功的记录个数
	 */
	private int processGrass05(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			try {
				Agme_Grass_05 agme_Grass_05 = new Agme_Grass_05();
				// 观测时间 14 日期 年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				agme_Grass_05.setObservationTime(sdf.parse(items[0]));

				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(agme_Grass_05.getObservationTime())){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 牧草名称
				agme_Grass_05.setHerbageName(ElementValUtil.ToBeValidInt(items[1]));
				// 每公顷株丛数
				agme_Grass_05.setNumPlantPerHectare(ElementValUtil.ToBeValidDouble(items[2]));
				// 每公顷总株丛数
				agme_Grass_05.setTotalNumPlantPerHectare(ElementValUtil.ToBeValidDouble(items[3]));

				// 观测场拔海高度,
				agme_Grass_05.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_05.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_05.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_05.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_05.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_05().add(agme_Grass_05);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 牧草覆盖度及草层采食度 GRASS-04 解码函数.
	 *
	 * @param reports 报文字符串
	 * @param agmeHeader  该报文段的报文头信息及站点基本信息
	 * @return int 解析成功的记录个数
	 */
	private int processGrass04(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			try {
				Agme_Grass_04 agme_Grass_04 = new Agme_Grass_04();
				// 观测时间 14 日期 年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				agme_Grass_04.setObservationTime(sdf.parse(items[0].trim()));

				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(agme_Grass_04.getObservationTime())){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 覆盖度
				agme_Grass_04.setCoverDegree(ElementValUtil.ToBeValidDouble(items[1]));
				// 草层状况评价
				agme_Grass_04.setEvaluaGrassCondition(ElementValUtil.ToBeValidInt(items[2]));
				// 采食度
				agme_Grass_04.setFeedingDegree(ElementValUtil.ToBeValidInt(items[3]));
				// 采食率
				agme_Grass_04.setFeedingRate(ElementValUtil.ToBeValidDouble(items[4]));

				// 观测场拔海高度,
				agme_Grass_04.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_04.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_04.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_04.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_04.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_04().add(agme_Grass_04);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}
	
	/**
	 * 牧草产量 GRASS-03解码函数.
	 *
	 * @param reports 报文字符串
	 * @param agmeHeader  该报文段的报文头信息及站点基本信息
	 * @return int 解析成功的记录个数
	 */
	private int processGrass03(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			try {
				Agme_Grass_03 agme_Grass_03 = new Agme_Grass_03();
				// 观测时间 14 日期 年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				// 观测时间
				agme_Grass_03.setObservationTime(sdf.parse(items[0]));

				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(agme_Grass_03.getObservationTime())){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 牧草名
				agme_Grass_03.setHerbageName(ElementValUtil.ToBeValidInt(items[1]));
				// 干重
				agme_Grass_03.setDryWeight(ElementValUtil.ToBeValidDouble(items[2], 0.1));
				// 鲜重
				agme_Grass_03.setFreshWeight(ElementValUtil.ToBeValidDouble(items[3], 0.1));
				// 干鲜比
				agme_Grass_03.setDryFreshRatio(ElementValUtil.ToBeValidDouble(items[4]));

				// 观测场拔海高度,
				agme_Grass_03.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_03.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_03.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_03.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_03.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_03().add(agme_Grass_03);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}

	/**
	 * 牧草生长高度 GRASS-02解析函数.
	 *
	 * @param reports 报文字符串
	 * @param agmeHeader  该报文段的报文头信息及站点基本信息
	 * @return int 解析成功的记录个数
	 */
	private int processGrass02(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				Agme_Grass_02 agme_Grass_02 = new Agme_Grass_02();
				// 观测时间
				agme_Grass_02.setObservationTime(sdf.parse(items[0]));

				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(agme_Grass_02.getObservationTime())){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 牧草名称
				agme_Grass_02.setHerbageName(ElementValUtil.ToBeValidInt(items[1]));
				// 生长高度
				agme_Grass_02.setHeightGrowth(ElementValUtil.ToBeValidDouble(items[2]));

				// 观测场拔海高度,
				agme_Grass_02.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_02.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_02.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_02.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_02.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_02().add(agme_Grass_02);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}

	/**
	 * 1 牧草发育期 GRASS-01解析函数.
	 *
	 * @param reports 报文字符串
	 * @param agmeHeader  该报文段的报文头信息及站点基本信息
	 * @return int 解析成功的记录个数
	 */
	private int processGrass01(String[] reports, AgmeReportHeader agmeHeader) {
		int message_count = reports.length;
		for (int i = 0; i < reports.length; i++) {
			String[] items = StringUtil.split(reports[i], ",", StringUtil.DOUBLE_QUOTATION_ARBITRARILY_REG);
			try {
				Agme_Grass_01 agme_Grass_01 = new Agme_Grass_01();
				// 观测时间 14 日期 年月日时分秒
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				agme_Grass_01.setObservationTime(sdf.parse(items[0]));

				//  2019-7-16 cuihongyuan
				if(!TimeCheckUtil.checkTime(agme_Grass_01.getObservationTime())){
					ReportError re = new ReportError();
					re.setMessage("check time error!");
					re.setSegment(reports[i]);
					parseResult.put(re);
					continue;
				}
				agmeHeader.setReport_time(sdf.parse(items[0]));
				// 牧草名称 8 编码 详见《编码》牧草名称部分
				agme_Grass_01.setHerbageName(ElementValUtil.ToBeValidInt(items[1]));
				// 发育期 2 编码 编码，详见《编码》作物发育期部分
				agme_Grass_01.setDevelopmentalPeriod(ElementValUtil.ToBeValidInt(items[2]));
				// 发育期百分率 4 % 进入发育期的株（茎）数比例
				agme_Grass_01.setPerDevlopmentPeriod(ElementValUtil.ToBeValidDouble(items[3]));

				// 观测场拔海高度,
				agme_Grass_01.setHeightOfSationGroundAboveMeanSeaLevel(
						agmeHeader.getHeightOfSationGroundAboveMeanSeaLevel());
				// 气压传感器拔海高度,观测方式
				agme_Grass_01.setHeightOfBarometerAboveMeanSeaLevel(agmeHeader.getHeightOfBarometerAboveMeanSeaLevel());
				// 纬度
				agme_Grass_01.setLatitude(agmeHeader.getLatitude());
				// 经度
				agme_Grass_01.setLongitude(agmeHeader.getLongitude());
				// 区站号
				agme_Grass_01.setStationNumberChina(agmeHeader.getStationNumberChina());
				agmeGrass.getAgmeGrass_01().add(agme_Grass_01);
			} catch (Exception e) {
				ReportError reportError = new ReportError();
				reportError.setMessage("report segment format conversion error");
				reportError.setSegment(reports[i]);
				parseResult.put(reportError);
				message_count = message_count - 1;
				continue;
			}
		}
		return message_count;
	}

	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		DecodeGrass decodeGrass = new DecodeGrass();
		ParseResult<Agme_Grass> parseResult = decodeGrass.decodeFile(new File("D:\\CIMISS2\\AGME\\E.0003.0004.R001\\201810\\2018101100\\Z_AGME_C_BCLZ_20181011003900_O_GRASS.TXT"));
		List<Agme_Grass> list = parseResult.getData();
		System.out.println(list.size());
		System.out.println(list.get(0).grassTypes);
		System.out.println(list.get(0).getAgmeGrass_01().get(0).getStationNumberChina());
	}

}
