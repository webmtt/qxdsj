package cma.cimiss2.dpc.decoder.surf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.upar.WAFS;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre
 * (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Airport report <br>
 * 机场天气报告METAR解码
 *
 * <p>
 * notes: 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.upar.WAFS}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * ZCZC 722<br>
 * 
 * SAAB31 LATI 080120<br>
 * 
 * METAR LATI 080120Z 18002KT CAVOK 20/18 Q1014 NOSIG=<br>
 * 
 * NNNN<br>
 * <strong>code:</strong><br>
 * DecodeWAFS decodeWAFS = new DecodeWAFS();<br>
 * ParseResult<WAFS> parseResult = decodeWAFS.decodeWAFS(new File(String
 * filepath));<br>
 * List<WAFS> wafs = parseResult.getData();<br>
 * System.out.println(wafs.get(0).getTemperature());<br>
 * <strong>output:</strong><br>
 * 20.0<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 上午10:10:10   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeWAFS {
	/**
	 * 解码结果
	 */
	private ParseResult<WAFS> parseResult = new ParseResult<WAFS>(false);
	/**
	 * 要素缺省值
	 */
	private int defaultInt = 999999;

	/**
	 * 机场天气报告METAR解码主函数,Decode WAFS, report.TT='SA' or 'SP'
	 * 
	 * @param file 待解析文件
	 * @return: ParseResult<WAFS> 解码结果封装
	 */
	public ParseResult<WAFS> decodeWAFS(File file) {
		if (file != null && file.exists() && file.isFile()) {
			if (file.length() <= 0) {
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
			@SuppressWarnings("static-access")
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			Pattern pattern = Pattern.compile("SAZ[1-7][0-9]{12}");
			Matcher matcher = pattern.matcher(file.getName());
			if (matcher.find()) {
				decodeSA(file, fileCode);
			} else {
				Date oDate = new Date(file.lastModified());

				// 从文件路径、文件名解析信息
//				if(sp.length >= 2){ 
//					try{
//						oDate = simpleDateFormat.parse(sp[sp.length - 2]);
//					}catch(ParseException e){
//						ReportError re = new ReportError();
//						re.setMessage("DataTime format error or null!");
//						re.setSegment(filePath);
//						parseResult.put(re);
//						return parseResult;
//					}
//				}
//				else{ // 无法资料时间，返回
//					parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
//					return parseResult;
//				}
				decodeZCZC(file, fileCode, oDate);

			}
		} else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}

	/**
	 * 解析以ZCZC开始的报文，报文类型SA和SP
	 * 
	 * @param file     待解码文件
	 * @param fileCode 文件编码类型
	 * @param oDate    资料时间
	 * @return: void
	 */
	@SuppressWarnings("resource")
	private void decodeZCZC(File file, String fileCode, Date oDate) {
		// 获取文件流
		Scanner scanner = null;
		InputStreamReader read = null;
		// 获得资料日期
		@SuppressWarnings("deprecation")
		int date = oDate.getDate();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(oDate);
		try {
			read = new InputStreamReader(new FileInputStream(file), fileCode);
			scanner = new Scanner(read).useDelimiter("NNNN"); //
			SimpleDateFormat sdf = new SimpleDateFormat("ddHHmm");
			Date time = new Date();
			String report = "";
			while (scanner.hasNext() && (report = scanner.next().trim()).length() > 3) {
				// 一个编报中心的资料，包含0或多个航站的数据
				// 移除空行
				report = report.replaceAll("\\?", "");
				report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
				String reports[] = report.split("\n|(\r\n)");
				if (reports.length < 3) {
					ReportError re = new ReportError();
					re.setMessage("Report format error!");
					re.setSegment(report);
					parseResult.put(re);
					continue;
				}
				// 1. 一个编报中心的资料，首行标识判断
				reports[0] = reports[0].trim();
				if (reports[0].substring(0, 4).compareTo("ZCZC") != 0
						&& reports[0].substring(0, 3).compareTo("ZCZ") != 0
						&& reports[0].substring(0, 3).compareTo("CZC") != 0) {
					// 报文标识错误
					continue;
				}
				// 2. 一个编报中心的资料, 第二行标识判断
				reports[1] = reports[1].trim();
				BullHeader bullHeader = new BullHeader();
				int ret = decodeHeader(reports[1], bullHeader);
				if (ret < 0) { // 报头格式错误
					ReportError re = new ReportError();
					re.setMessage("Report header format error or null!");
					re.setSegment(reports[1]);
					parseResult.put(re);
					continue;
				}
				// 移除前两行， 取得以“=”分隔的报文
				report = "";
				for (int i = 2; i < reports.length; i++)
					report += (reports[i] + " ");
				reports = report.trim().split("=");
				String tmp = "";
				for (int i = 0; i < reports.length; i++) {
					reports[i] = reports[i].replaceAll("(\r\n)|\n|\r", " ").trim();
					reports[i] = reports[i].replaceAll("\\s+", " ").trim();
					if (!reports[i].equals("")) {
						String type = "";
						report = reports[i].trim();
						if (report.contains("COR "))
							report = report.replaceAll("COR ", "");
						WAFS wafs = new WAFS();
						wafs.setCorrectSign(bullHeader.getBbb()); // V_BBB
						wafs.setReportCenter(bullHeader.getCccc()); // CCCC
						if (report.startsWith("NIL")) {
							ReportError re = new ReportError();
							re.setMessage("Empty report!");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
						if (i == 0)
							tmp = report.substring(0, 5) + " ";
						if (!report.startsWith("SPECI") && !report.startsWith("METAR"))
							report = tmp + report;
						int start = 0;
						String ele[] = report.split("\\s+");
						int len = ele.length;
						while (start < len && !ele[start].equals("SPECI") && !ele[start].equals("METAR"))
							start++;
						if (start == len)
							continue;
						boolean isRepeated = false;
						// ele[start] == SPECI or METAR
						// wafs.setReportType(ele[start]); // 1. 报文类别,2018-07-04
						type = ele[start];
						if (bullHeader.getTt().contains("SA"))
							wafs.setReportType("METAR");
						else
							wafs.setReportType("SPECI");
						if (ele[++start].length() != 4) {
							start++; // 针对SO（索马里编报的处理）
							if (ele.length > start && (ele[start].equals("METAR") || ele[start].equals("SPECI"))) {
//								wafs.setReportType(ele[start]);    // 注释：2018-07-04
								type = ele[start];
								if (ele[++start].length() == 4) {
									wafs.setTerminalSign(ele[start]);
									isRepeated = true;
								} else {
									ReportError re = new ReportError();
									re.setMessage("Terminal number error!");
									re.setSegment(report);
									parseResult.put(re);
									continue;
								}
							} else {
								ReportError re = new ReportError();
								re.setMessage("Terminal number error!");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							}
						}
						wafs.setTerminalSign(ele[start]);// 2. 航班号
						try {
							time = sdf.parse(ele[++start].substring(0, 6));
							if (time.getDate() > date) {
								calendar.add(Calendar.MONTH, -1);
							}
							calendar.set(Calendar.DAY_OF_MONTH, time.getDate());
							calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
							calendar.set(Calendar.MINUTE, time.getMinutes());
							calendar.set(Calendar.SECOND, 0);
							calendar.set(Calendar.MILLISECOND, 0);
						} catch (Exception e) {
							ReportError re = new ReportError();
							re.setMessage("DataTime format error or null!");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}

						wafs.setObservationTime(calendar.getTime()); // 3. 资料时间： 日+时+分
						if(!TimeCheckUtil.checkTime(calendar.getTime())){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+calendar.getTime()+" TerminalSign:"+ele[start]);
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
						if (isRepeated)
							report = report.substring(32);
						else
							report = report.substring(19);
						parseResult.put(new ReportInfo<WAFS>(wafs, report));
						if (!report.startsWith("NIL")) {
							if (!report.contains(type))
								ret = DecodeElements(report, wafs);
							else { // 当报文中漏掉符号“=”
								String rep = report.substring(0, report.indexOf(type));
								ret = DecodeElements(rep, wafs);
								reports[i] = type + report.substring(report.indexOf(type) + 5);
								i--;
							}
						} else
							ret = -1;
						if (ret < 1) {
							ReportError re = new ReportError();
							re.setMessage("Report element format error");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
						parseResult.put(wafs);
						parseResult.setSuccess(true);
						if ((!wafs.getReportType().equals(type)) && wafs.getCorrectSign().compareTo("000") > 0) {
							WAFS wafs1 = (WAFS) wafs.clone();
							wafs1.setReportType(type);
							wafs1.setCorrectSign("000");
							parseResult.put(wafs1);
						}
//						parseResult.put(new ReportInfo<WAFS> (wafs, report));
					}
				}
			}
		} catch (UnsupportedEncodingException e) {
			parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
		} catch (FileNotFoundException e) {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		} catch (Exception e) {
//			System.out.println("未定位的错误，文件：" + file);
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
				if (scanner != null)
					scanner.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 按行解析报文，报文类型SA
	 * 
	 * @param file     待解码报文文件
	 * @param fileCode 文件编码格式
	 * @return void
	 */
	private void decodeSA(File file, String fileCode) {
		BufferedReader bufferedReader = null;
		InputStreamReader read = null;
		try {
			read = new InputStreamReader(new FileInputStream(file), fileCode);
			bufferedReader = new BufferedReader(read);
			String lineTxt = null;
			String split[];
			SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			while ((lineTxt = bufferedReader.readLine()) != null) {
				lineTxt = lineTxt + " ";
				split = lineTxt.split(",");
				if (split.length == 16) {
					WAFS wafs = new WAFS();
//					try{
					// 1报文类型
					wafs.setReportType("SA");
					// 2航站号
					wafs.setTerminalSign(split[1].trim());
					// 2019-5-10 cuihongyuan
//					wafs.setReportCenter(split[1].trim());
					wafs.setReportCenter("ZBBB");
					
					// 3更正标识
					if (!split[2].trim().equals(""))
						wafs.setCorrectSign(split[2].trim());
					// 4观测时间
					wafs.setObservationTime(simpleDateFormat2.parse(split[3]));
					if(!TimeCheckUtil.checkTime(simpleDateFormat2.parse(split[3]))){
						ReportError re = new ReportError();
						re.setMessage("DataTime out of range：time:"+simpleDateFormat2.parse(split[3])+" TerminalSign:"+split[1].trim());
						re.setSegment(lineTxt);
						parseResult.put(re);
						continue;
					}
					parseResult.put(new ReportInfo<WAFS>(wafs, lineTxt));
					// 5风向
					if (!split[4].trim().equals(""))
						try {
							double winddir = Double.parseDouble(split[4]);
							if(winddir == -1.0)
								wafs.setWindDirection(999997); // 不定风向
							else
								wafs.setWindDirection(winddir);
							
						} catch (Exception e) {
							wafs.setWindDirection(defaultInt);
						}
					// 6风速
					if (!split[5].trim().equals(""))
						try {
							wafs.setWindSpeed(Double.parseDouble(split[5]));
						} catch (Exception e) {
							wafs.setWindSpeed(defaultInt);
						}
					// 7阵风 赋值给最大风速
					if(!split[6].trim().equals(""))
						try{
							wafs.setMaxWindSpeed(Double.parseDouble(split[6]));
						}catch (Exception e) {
							wafs.setMaxWindSpeed(defaultInt);
						}
					// 8主导能见度（水平）
					if (!split[7].trim().equals(""))
						try {
							wafs.setHorizontalVisibility(Float.parseFloat(split[7]));
						} catch (Exception e) {
							wafs.setHorizontalVisibility(defaultInt);
						}
					// 9 若CAVOK则为1，此时主导能见度为9999；否则为空；
					if (split[8].trim().equals("1"))
						wafs.setHorizontalVisibility(9999);
					// 10现在天气现象
					wafs.setWeatherPhenomenons(new ArrayList<>());
					for (int q = 0; q < 3; q++)
						wafs.getWeatherPhenomenons().add("");
					if (!split[9].trim().equals("")) {
						String temp[] = split[9].trim().split("&");
						int groups = temp.length >= 3 ? 3 : temp.length;
						for (int x = 0; x < groups; x++)
							wafs.getWeatherPhenomenons().set(x, temp[x].trim());
					}
					// 11云组信息
					wafs.setCloudShapeAndAmount(new ArrayList<>());
					wafs.setHeightOfCloudBase(new ArrayList<>());
					for (int q = 0; q < 3; q++) {
						wafs.getCloudShapeAndAmount().add("");
						wafs.getHeightOfCloudBase().add((float) defaultInt);
					}
					if (!split[10].trim().equals("") && !split[10].trim().contains("//")) {
						String temp[] = split[10].trim().split("&");
						// 例如：FEW010&BKN023&FEW023TCU
//						if (temp.length <= 3) {
						int groups = temp.length >= 3 ? 3: temp.length;
							for (int x = 0; x < groups; x++) {
								temp[x] = temp[x].trim();
								if (temp[x].length() >= 6) {
									wafs.getCloudShapeAndAmount().set(x, temp[x].substring(0, 3));
									try {
										// 修改于2019年1月23日
										if (temp[x].substring(3, 6).equals("///")) {
											wafs.getHeightOfCloudBase().set(x, (float) defaultInt);
										} else if (temp[x].substring(3, 6).equals("999")) {
											wafs.getHeightOfCloudBase().set(x, (float) 30000);
										} else {
											float t = Float.parseFloat(temp[x].substring(3, 6));
											if (t == 0)
												wafs.getHeightOfCloudBase().set(x, (float) 15);
											else if (t >= 1 && t <= 990) {
												wafs.getHeightOfCloudBase().set(x, t * 30);
											} else {
												wafs.getHeightOfCloudBase().set(x, (float) defaultInt);
											}
										}
//											wafs.getHeightOfCloudBase().set(x, Float.parseFloat(temp[x].substring(3, 6)));
									} catch (Exception e) {
										wafs.getHeightOfCloudBase().set(x, (float) defaultInt);
									}
								}
								else if (temp[x].length() == 3) {
									wafs.getCloudShapeAndAmount().set(x, temp[x]);
								}
							}
//						}
					}
					// 12垂直能见度
					if (!split[11].trim().equals(""))
						try {
							wafs.setVerticalVisibility(Float.parseFloat(split[11]));
						} catch (Exception e) {
							wafs.setVerticalVisibility(defaultInt);
						}
					// 13气温
					if (!split[12].trim().equals(""))
						try {
							wafs.setTemperature(Float.parseFloat(split[12]));
						} catch (Exception e) {
							wafs.setTemperature(defaultInt);
						}
					// 14露点温度
					if (!split[13].trim().equals(""))
						try {
							wafs.setDewPoint(Float.parseFloat(split[13]));
						} catch (Exception e) {
							wafs.setDewPoint(defaultInt);
						}
					// 15气压值
					if (!split[14].trim().equals(""))
						try {
							wafs.setPressureAboveSeaLevel(Double.parseDouble(split[14]));
						} catch (Exception e) {
							wafs.setPressureAboveSeaLevel(defaultInt);
						}

					// initialization
					wafs.setRunwayNumbers(new ArrayList<String>());
					wafs.setRunwayVisualRange(new ArrayList<Float>());
					wafs.setMaxRunwayVisualRangeEvery10mins(new ArrayList<Float>());
					wafs.setTrendsOfRunwayViusalRange(new ArrayList<Character>());
					for (int ini = 0; ini < 5; ini++) {
						wafs.getRunwayNumbers().add("");
						wafs.getRunwayVisualRange().add((float) defaultInt);
						wafs.getMaxRunwayVisualRangeEvery10mins().add((float) defaultInt);
						wafs.getTrendsOfRunwayViusalRange().add('\0');
					}
					wafs.setRecentWeatherPhenomenons(new ArrayList<>());
					wafs.setWindShearRunwayNumbers(new ArrayList<>());
					for (int ini = 0; ini < 3; ini++) {
						wafs.getRecentWeatherPhenomenons().add("");
						wafs.getWindShearRunwayNumbers().add("");
					}
					parseResult.put(wafs);
					parseResult.setSuccess(true);
//					}catch (Exception e) {
//						ReportError reportError = new ReportError();
//						reportError.setMessage("Element format error!");
//						reportError.setSegment(lineTxt);
//						parseResult.put(reportError);
//					}
				} else {
					ReportError reportError = new ReportError();
					reportError.setMessage("Number of elements error!");
					reportError.setSegment(lineTxt);
					parseResult.put(reportError);
				}
			} // while
		} catch (UnsupportedEncodingException e) {
			parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
		} catch (FileNotFoundException e) {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		} catch (Exception e) {
			parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (read != null)
					read.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * do basic syntax check of TT,AA,ii,CCCC,YYGGgg,BBB if no error,get
	 * bull->TT,AA,ii,CCCC,YYGGgg,BBB
	 * 
	 * @param header     待解析报文头
	 * @param bullheader 解码结果封装
	 * @return int 解码失败返回-1；成功返回1
	 */
	private int decodeHeader(String header, BullHeader bullheader) {
		if (header == null || header.equals(""))
			return -1;
		String sp[] = header.split("\\s+");

		if (sp.length == 3 || sp.length == 4) {
			// 1. TTAAII 例如 SAAU31(资料类型+国家+号码)
			if (sp[0].length() == 6 || sp[0].length() == 5) {
				bullheader.setTt(sp[0].substring(0, 2));
				bullheader.setAa(sp[0].substring(2, 4));
				bullheader.setIi(sp[0].substring(4));
			} else
				return -1; // TTAAII 格式错误
			// 2. CCCC编报中心 例如：AMMC、RJTD
			if (sp[1].length() == 4) {
				bullheader.setCccc(sp[1]);
			} else
				return -1; // CCCC 格式错误
			// 3. YYGGgg, (日期+时+分，分别占两位)
			if (sp[2].length() == 6) {
				int i = 0;
				for (i = 0; i < 6; i++) {
					if (!Character.isDigit(sp[2].charAt(i)))
						break;
				}
				if (i == 6)
					bullheader.setYYGGgg(sp[2]);
				else {
					return -1; // YYGGgg 格式错误
				}
			} else
				return -1; // YYGGgg 格式错误
			// 4. V_BBB: 更正标识，例如：CCA
			bullheader.setBbb("000");
			if (sp.length == 4) {
				String tmp_val = sp[3];
				char s = tmp_val.charAt(0);
				if (tmp_val.length() == 3 && (s == 'A' || s == 'R' || s == 'C' || s == 'P')) {
					bullheader.setBbb(sp[3]);
				} else
					return -1; // V_BBB 格式错误
			}
		} else {
			return -1;// 报头格式错误
		}
		return 1; // 报头解析成功
	}

	/**
	 * 报文要素解码 函数
	 * 
	 * @param rep  待解析报文
	 * @param wafs 解码结果封装
	 * @return: int 1：解码成功； 其他：报文格式异常，解码失败
	 */
	private int DecodeElements(String rep, WAFS wafs) {
		int ret = 0; // 失败,没有找到下一个要素
		String sp[] = rep.split("\\s+");
		int cursor = 0; // 当前位置
		String group = "";
		int sz = 0;
		sz = sp.length;
		if (sz < 1)
			return -1; // 没有要素数据
		// initialization
		wafs.setRunwayNumbers(new ArrayList<String>());
		wafs.setRunwayVisualRange(new ArrayList<Float>());
		wafs.setMaxRunwayVisualRangeEvery10mins(new ArrayList<Float>());
		wafs.setTrendsOfRunwayViusalRange(new ArrayList<Character>());
		for (int ini = 0; ini < 5; ini++) {
			wafs.getRunwayNumbers().add("");
			wafs.getRunwayVisualRange().add((float) defaultInt);
			wafs.getMaxRunwayVisualRangeEvery10mins().add((float) defaultInt);
			wafs.getTrendsOfRunwayViusalRange().add('\0');
		}
		wafs.setWeatherPhenomenons(new ArrayList<>());
		wafs.setCloudShapeAndAmount(new ArrayList<>());
		wafs.setHeightOfCloudBase(new ArrayList<>());
		wafs.setRecentWeatherPhenomenons(new ArrayList<>());
		wafs.setWindShearRunwayNumbers(new ArrayList<>());
		for (int ini = 0; ini < 3; ini++) {
			wafs.getWeatherPhenomenons().add("");
			wafs.getCloudShapeAndAmount().add("");
			wafs.getHeightOfCloudBase().add((float) defaultInt);
			wafs.getRecentWeatherPhenomenons().add("");
			wafs.getWindShearRunwayNumbers().add("");
		}
		// 1. AUTO
		group = sp[0];
		while (IsValidGrp(group) == 0) {
			if (++cursor == sz)
				return 1; // return -1; 2018-07-03
			group = sp[cursor];
		}
		if (isTrendSign(group) == -2)
			return 1;
		if (group.compareTo("AUTO") != 0) {
			if (++cursor == sz)
				return 1; // 遍历结束，只有一个要素字段有效,// return -1; 2018-07-03
			group = sp[cursor];
		}
		if (group.compareTo("AUTO") == 0) {
			wafs.setAutoStationMark(0);
			if (++cursor == sz)
				return 1;
		} else {
			wafs.setAutoStationMark(1);
			cursor--;
		}
		// 2. dddffGfmfm(KMH|KT|MPS)（风向、风速、最大风速、 风向反时针\顺时针变化极值 ）
		int j = 0;
		int flag_dddff = 0;
		group = sp[cursor];
		while (IsValidGrp(group) == 0) {
			if (++cursor == sz)
				return 1; // return -1; 2018-07-03
			group = sp[cursor];
		}
		if (isTrendSign(group) == -2)
			return 1;
		if (!group.contains("KMH") && !group.contains("KT") && !group.contains("MPS")) {
			if (++cursor == sz)
				return 1; // 遍历结束，找不到更多有效的要素,return -1; 2018-07-03
			group = sp[cursor];
		}
		String temp_val = "";
		if (group.contains("KMH") || group.contains("KT") || group.contains("MPS")) {
			for (j = 0; j < group.length(); j++) {
				if (!Character.isDigit(group.charAt(j)))
					break;
			}
			if (j == 5 || (group.length() >= 3 && group.substring(0, 3).equals("VRB"))) { // 数字占5位
				if (!group.substring(0, 3).equals("VRB")) { // 不是不定风向
					temp_val = group.substring(0, 3); // 风向
					try {
						wafs.setWindDirection(Integer.parseInt(temp_val));
					} catch (Exception e) {
						wafs.setWindDirection(defaultInt);
					}
					temp_val = group.substring(3, 5); // 风速
				} else { // 不定风向
					wafs.setWindDirection(999997);
					if (group.contains("VRB/"))
						temp_val = group.substring(4, 6);// 风速值
					else
						temp_val = group.substring(3, 5);// 风速值
				}

				try {
					if (group.contains("MPS"))
						wafs.setWindSpeed(Integer.parseInt(temp_val));
					else if (group.contains("KT"))
//						wafs.setWindSpeed((Integer.parseInt(temp_val) + 1) * 0.5);
						// 2019年1月23日改为
						wafs.setWindSpeed(Integer.parseInt(temp_val) * 0.51);
					else if (group.contains("KMH"))
						wafs.setWindSpeed(Integer.parseInt(temp_val) * 0.278);
				} catch (Exception e) {
					wafs.setWindSpeed(defaultInt);
				}
			}
			if (group.length() >= 6 && group.charAt(5) == 'G') {
				for (j = 6; j < group.length(); j++) {
					if (!Character.isDigit(group.charAt(j)))
						break;
				}
				if (j > 6) {
					temp_val = group.substring(6, j);
					try {
						if (group.contains("MPS"))
							wafs.setMaxWindSpeed(Integer.parseInt(temp_val));
						else if (group.contains("KT"))
//							wafs.setMaxWindSpeed((Integer.parseInt(temp_val) + 1) * 0.5);
							// 2019年1月23日更改为
							wafs.setMaxWindSpeed(Integer.parseInt(temp_val) * 0.51);
						else if (group.contains("KMH"))
							wafs.setMaxWindSpeed(Integer.parseInt(temp_val) * 0.278);
					} catch (Exception e) {
						wafs.setMaxWindSpeed(defaultInt);
					}
				}
			}
			if (++cursor == sz)
				return 1; // 要素遍历结束
			flag_dddff = 1;
		} else {
			cursor--;
		}

		// 3. dndndnVdxdxdx (风向反时针\顺时针变化极值 )
		if (flag_dddff == 1) {
			group = sp[cursor];
			while (IsValidGrp(group) == 0) {
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
				group = sp[cursor];
			}
			if (isTrendSign(group) == -2)
				return 1;
			if (group.length() != 7 || (group.length() > 3 && group.charAt(3) != 'V')) {
				if (++cursor == sz)
					return 1;
				group = sp[cursor];
			}
			if (group.length() == 7 && group.charAt(3) == 'V') {
				temp_val = group.substring(0, 3);
				if (Character.isDigit(temp_val.charAt(0)))
					try {
						if (Integer.parseInt(temp_val) > defaultInt)
							wafs.setExtremeChangeValueOfWindDireInCounterClockwise(defaultInt);
						else {
							wafs.setExtremeChangeValueOfWindDireInCounterClockwise(Integer.parseInt(temp_val));
						}
					} catch (Exception e) {
						wafs.setExtremeChangeValueOfWindDireInCounterClockwise(defaultInt);
					}
				temp_val = group.substring(4, 7);
				if (Character.isDigit(temp_val.charAt(0)))
					try {
						if (Integer.parseInt(temp_val) > defaultInt)
							wafs.setExtremeChangeValueOfWindDireInClockwise(defaultInt);
						else {
							wafs.setExtremeChangeValueOfWindDireInClockwise(Integer.parseInt(temp_val));
						}

					} catch (Exception e) {
						wafs.setExtremeChangeValueOfWindDireInClockwise(defaultInt);
					}
				if (++cursor == sz)
					return 1;
			} else {
				cursor--;
			}
		}
		// 4. VVVVDv
		int flag_vvvv = 0;
		wafs.setHorizontalVisibility(defaultInt);
		wafs.setDirectionOfHorizontalVisibility("//");
		int part1 = 0;
		group = sp[cursor];
		while (IsValidGrp(group) == 0 || sp[cursor].contains("TCU") || sp[cursor].contains("CB")
				|| sp[cursor].contains("NCD")) {
			if (++cursor == sz)
				return 1; // return -1; 2018-07-03
			group = sp[cursor];
		}
		if (group.endsWith("KM")) {
			int index = group.indexOf("KM");
			group = group.substring(0, index) + "000";
		}
		if (isTrendSign(group) == -2)
			return 1;
		for (j = 0; j < group.length(); j++) {
			if (!Character.isDigit(group.charAt(j)))
				break;
		}
		if (j != 4) {
			if (group.compareTo("CAVOK") != 0 && !group.contains("SM") && !group.contains("/")
					&& SearchCloud(group) != 1) {
				try {
					part1 = Integer.parseInt(group);
				} catch (Exception e) {
					// 2019-2-15
//					return 1;
				}
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
				group = sp[cursor];
				for (j = 0; j < group.length(); j++) {
					if (!Character.isDigit(group.charAt(j)))
						break;
				}
			}
		}
		if (j == 4) {
			try {
				wafs.setHorizontalVisibility(Integer.parseInt(group.substring(0, 4)));
			} catch (Exception e) {
				wafs.setHorizontalVisibility(defaultInt);
			}

			if (group.length() - j != 0) {
				int k = strcspn(group.substring(4), "DV");
				wafs.setDirectionOfHorizontalVisibility(group.substring(4, 4 + k));
			}
			if (++cursor == sz)
				return 1;
			flag_vvvv = 1;
		} else if (group.compareTo("CAVOK") == 0) {
			wafs.setHorizontalVisibility(9999);
			if (++cursor == sz)
				return 1;
		} else if (group.contains("SM")) { // 包含SM，但不以SM开始
			String vvvv = group.split("S")[0];
			if (vvvv.isEmpty())
				vvvv = String.valueOf(defaultInt);
			if (vvvv.contains("/")) {
				String tokens[] = vvvv.split("/");
				try {
					int part2 = Integer.parseInt(tokens[0]);
					int part3 = Integer.parseInt(tokens[1]);
					wafs.setHorizontalVisibility((float) (part1 * 1609.3 + part2 * 1609.3 / part3));
				} catch (Exception e) {
					return 1;
				}
			} else {
				if (vvvv.equals(String.valueOf(defaultInt)))
					wafs.setHorizontalVisibility(defaultInt);
				else {
					try {
						part1 = Integer.parseInt(vvvv);
						wafs.setHorizontalVisibility((float) (part1 * 1609.3));
					} catch (Exception e) {
						wafs.setHorizontalVisibility(defaultInt);
					}
				}
			}
		} else {
			cursor--;
		}
		// 5. VxVxVxVxDv
		wafs.setMaxHorizontalVisibility(defaultInt);
		wafs.setDirOfMaxHorizontalVisibility("//");
		if (flag_vvvv == 1) {
			group = sp[cursor];
			while (IsValidGrp(group) == 0 || sp[cursor].contains("TCU") || sp[cursor].contains("CB")
					|| sp[cursor].contains("NCD")) {
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
				group = sp[cursor];
			}
			if (isTrendSign(group) == -2)
				return 1;
			for (j = 0; j < group.length(); j++)
				if (!Character.isDigit(group.charAt(j)))
					break;
			if (j != 4) {
				if (group.compareTo("CAVOK") != 0 && !group.contains("/") && SearchCloud(group) != 1) {
					if (++cursor == sz)
						return 1;
					group = sp[cursor];
					for (j = 0; j < group.length(); j++)
						if (!Character.isDigit(group.charAt(j)))
							break;
				}
			}
			if (j == 4) {
				try {
					wafs.setMaxHorizontalVisibility(Integer.parseInt(group.substring(0, 4)));
				} catch (Exception e) {
					wafs.setMaxHorizontalVisibility(defaultInt);
				}
				if (group.length() - j != 0) {
					int k = strcspn(group.substring(4), "DV");
					wafs.setDirOfMaxHorizontalVisibility(group.substring(4, 4 + k));
				}
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
			} else if (group.compareTo("CAVOK") == 0) {
				wafs.setMaxHorizontalVisibility(9999);
				if (++cursor == sz)
					return 1;
			} else {
				cursor--;
			}
		}

		// 6. RDrDr/VrVrVrVri or
		// RDrDr/VrVrVrVrVVrVrVrVri,例如R07/1300VP2000U，跑道/视程/10跑道最大视程/视程变化趋势
		for (j = 0; j < 5; j++) { // 最多五个
			if (cursor < 0)
				cursor = 0;
			group = sp[cursor];
			while (IsValidGrp(group) == 0) {
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
				group = sp[cursor];
			}
			if (isTrendSign(group) == -2)
				return 1;
			if (group.charAt(0) != 'R' || group.charAt(1) == 'A' || !group.contains("/")) {
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
				group = sp[cursor];
			}
			if (group.charAt(0) == 'R' && group.charAt(1) != 'A' && group.contains("/")) {
				int k = strcspn(group, "/");
				wafs.getRunwayNumbers().set(j, group.substring(1, k));
				temp_val = group.substring(k + 1);
				if (Character.isDigit(temp_val.charAt(0)))
					try {
						wafs.getRunwayVisualRange().set(j, Float.parseFloat(temp_val.substring(0, 4)));
					} catch (Exception e) {
						wafs.getRunwayVisualRange().set(j, (float) defaultInt);
					}
				// M**** means min or P**** means max
				else if (temp_val.charAt(0) == 'M') {
					int idx = 1;
					for (idx = 1; idx < temp_val.length();)
						if (Character.isDigit(temp_val.charAt(idx)))
							idx++;
						else
							break;
					try {
						wafs.getRunwayVisualRange().set(j, Float.parseFloat(temp_val.substring(1, idx)));
					} catch (Exception e) {
						wafs.getRunwayVisualRange().set(j, (float) defaultInt);
					}
				} else if (temp_val.charAt(0) == 'P') {
					int idx = 1;
					for (idx = 1; idx < temp_val.length();)
						if (Character.isDigit(temp_val.charAt(idx)))
							idx++;
						else
							break;
					try {
						wafs.getMaxRunwayVisualRangeEvery10mins().set(j, Float.parseFloat(temp_val.substring(1, idx)));
					} catch (Exception e) {
						wafs.getMaxRunwayVisualRangeEvery10mins().set(j, (float) defaultInt);
					}
				}

				// find if char 'V' exits
				int jj = strcspn(group, "V");
				if (jj < group.length()) {
					temp_val = group.substring(jj + 1);
					try {
						if (temp_val.charAt(0) == 'P')
							wafs.getMaxRunwayVisualRangeEvery10mins().set(j,
									Float.parseFloat(temp_val.substring(1, 5)));
						else
							wafs.getMaxRunwayVisualRangeEvery10mins().set(j,
									Float.parseFloat(temp_val.substring(0, 4)));
					} catch (Exception e) {
						wafs.getMaxRunwayVisualRangeEvery10mins().set(j, (float) defaultInt);
					}
				}
				jj = strcspn(group, "UDN");
				if (jj < group.length())
					wafs.getTrendsOfRunwayViusalRange().set(j, group.charAt(jj));
				if (++cursor == sz)
					return 1;
			} else {
				cursor--;
				break;
			}
		}
		// 7. w'w' （天气现象）
		for (j = 0; j < 3; j++) { // 最多三组
			group = sp[cursor];
			while (IsValidGrp(group) == 0) {
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
				group = sp[cursor];
			}
			if (isTrendSign(group) == -2)
				return 1;
			ret = SearchWW(group);
			if (ret != 1) {
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
				group = sp[cursor];
			}
			if (isTrendSign(group) == -2)
				return 1;
			ret = SearchWW(group);
			if (ret == 1) {
				wafs.getWeatherPhenomenons().set(j, group);
				if (++cursor == sz)
					return 1;
			} else {
				cursor--;
				break;
			}
		}
		// 8. SKC\CLR\NSC or FEW\SCT\BKN\OVC or VV （云状、云量、垂直可见度）,重要的对流云 TCU、CB，没有探测到云
		// NCD 跳过
		for (j = 0; j < 3; j++) { // 最多三组
			group = sp[cursor];
			while (IsValidGrp(group) == 0 || group.contains("TCU") || group.contains("CB")
					|| sp[cursor].contains("NCD")) { //
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
				group = sp[cursor];
			}
			if (isTrendSign(group) == -2)
				return 1;
			ret = SearchCloud(group);
			if (ret != 1) {
				if (++cursor == sz)
					return 1; // return -1; 2018-07-03
				group = sp[cursor];
			}
			if (isTrendSign(group) == -2)
				return 1;
			ret = SearchCloud(group);
			if (ret == 1) {
				if (group.length() >= 2 && group.substring(0, 2).compareTo("VV") == 0) {
					// 注释 2019-02-14 崔红元
//					group = group.replaceAll("/", "9");
					try {
						wafs.setVerticalVisibility(Integer.parseInt(group.substring(2)));
					} catch (Exception e) {
						wafs.setVerticalVisibility(defaultInt);
					}
				} else {
					// 2019-5-14 cuihongyuan
					if(group.substring(0, 3).contains("/"))
						wafs.getCloudShapeAndAmount().set(j, "999999");
					else
						wafs.getCloudShapeAndAmount().set(j, group.substring(0, 3));
//					wafs.getCloudShapeAndAmount().set(j, group.substring(0, 3));
					
					// end by cuihongyuan
					
					if (group.length() > 3) {
						if (group.length() == 5 || group.length() == 4)
							temp_val = group.substring(3);// 长度为4或5时
						else
							temp_val = group.substring(3, 6); // 长度大于6时

						// 增加2行，2019年1月23日
						if (temp_val.contains("///")) {
							wafs.getHeightOfCloudBase().set(j, (float) 999999);
						} else if (temp_val.equals("999")) // temp_val.equals("///") 2018-07-03
							wafs.getHeightOfCloudBase().set(j, (float) 30000);
						else
							try {
								// 更改 于 2019年1月23日
								float t = Float.parseFloat(temp_val);
								if (t == 0) {
									wafs.getHeightOfCloudBase().set(j, (float) 15);
								} else if (t >= 1 && t <= 990) {
									wafs.getHeightOfCloudBase().set(j, t * 30);
								}
							} catch (Exception e) {
								wafs.getHeightOfCloudBase().set(j, (float) 999999);
							}
					}
				}
				if (++cursor == sz)
					return 1;
			} else {
				cursor--;
				break;
			}
		}
		while (sp[cursor].contains("TCU") || sp[cursor].contains("CB") || sp[cursor].contains("NCD")
				|| sp[cursor].contains("NLC")) { // 重要的对流云 TCU、CB，没有探测到云NCD 跳过
			++cursor;
		}
		// 9. TT/TdTd (温度、露点)
		group = sp[cursor];
		// add if on 2019-2-19
		while (group.indexOf("SKC") >= 0 || group.indexOf("CLR") >= 0 || group.indexOf("NSC") >= 0
				|| group.indexOf("FEW") >= 0 || group.indexOf("SCT") >= 0 || group.indexOf("BKN") >= 0
				|| group.indexOf("OVC") >= 0 || group.indexOf("VV") >= 0) { // 有时，云状、云高组数大于3，过滤掉
			cursor++;
			group = sp[cursor];
		}
		if (sp[cursor + 1].startsWith("/") && sp[cursor + 1].length() == 3) { // 有时一个要素值组未结束而换行，此时，将这个要素拼接起来
			group = group + sp[cursor + 1];
			cursor++;
		}
		// end adding

		if (isTrendSign(group) == -2)
			return 1;
		String[] s = { group };
		ret = checkTT(s);
		group = s[0];
		if (ret != 1) {
			if (++cursor == sz)
				return 1; // return -1; 2018-07-03
			group = sp[cursor];
			// added on 2019-2-19
			s[0] = group;
			// end adding
		}
		if (isTrendSign(group) == -2)
			return 1;
		ret = checkTT(s);
		group = s[0];
		if (ret == 1) {
			int k = strcspn(group, "/");
			temp_val = group.substring(0, k);
			try {
				wafs.setTemperature(Integer.parseInt(temp_val));
			} catch (Exception e) {
				wafs.setTemperature(defaultInt);
			}
			temp_val = group.substring(k + 1);
			try {
				wafs.setDewPoint(Integer.parseInt(temp_val));
			} catch (Exception e) {
				wafs.setDewPoint(defaultInt);

			}
			if (++cursor == sz)
				return 1;
		} else {
			cursor--;
		}
		// 10. QPhPhPhPh or APhPhPhPh
		group = sp[cursor];
		if (isTrendSign(group) == -2)
			return 1;
		ret = checkPH(group);
		if (ret != 1) {
			if (++cursor == sz)
				return 1;
			group = sp[cursor];
		}
		if (isTrendSign(group) == -2)
			return 1;
		ret = checkPH(group);
		if (ret == 1) {
			if (group.length() < 5 || group.contains("/"))
				wafs.setPressureAboveSeaLevel(defaultInt);
			else {
				temp_val = group.substring(1, 5);
				try {
					if (group.charAt(0) == 'Q')
						wafs.setPressureAboveSeaLevel(Double.parseDouble(temp_val));
					else
						wafs.setPressureAboveSeaLevel(Double.parseDouble(temp_val) * 0.338);
				} catch (Exception e) {
					wafs.setPressureAboveSeaLevel(defaultInt);
				}
			}
			if (++cursor == sz)
				return 1;
		} else {
			cursor--;
		}

		// 11. REw'w' (近时天气现象1 ~ 3)
		for (j = 0; j < 3; j++) { // 最多三组
			group = sp[cursor];
			if (isTrendSign(group) == -2)
				return 1;
			if (group.length() >= 2 && group.substring(0, 2).compareTo("RE") != 0) {
				if (++cursor == sz)
					return 1; // return -1; 2018-07-02
				group = sp[cursor];
//				group = group.substring(2);
//				if(rep.endsWith("RE")) return -1;
			}
			if (isTrendSign(group) == -2)
				return 1;
			if (group.length() >= 2 && group.substring(0, 2).compareTo("RE") == 0) {
				ret = SearchWW(group);
				if (ret == 1) {
					wafs.getRecentWeatherPhenomenons().set(j, group.substring(2));
					if (++cursor == sz)
						return 1;
				} else {
					cursor--;
					break;
				}
			} else {
				cursor--;
			}
		}
		// 12. WS RWYDrDr or WS ALL RWY (风切变跑道号1 ~ 3)
		group = sp[cursor];
		if (isTrendSign(group) == -2)
			return 1;
		if (group.length() >= 2 && group.substring(0, 2).compareTo("WS") != 0) {
			if (++cursor == sz)
				return 1; // return -1; 2018-07-03
			group = sp[cursor];
		}
		if (isTrendSign(group) == -2)
			return 1;
		if (group.length() >= 2 && group.substring(0, 2).compareTo("WS") == 0) {
			if (++cursor == sz)
				return 1;
			for (j = 0; j < 4; j++) {
				group = sp[cursor];
				if (group.length() >= 3 && group.substring(0, 3).compareTo("RWY") != 0) {
					// all RWY
					if (group.length() >= 3 && group.substring(0, 3).compareTo("ALL") == 0) {
						wafs.getWindShearRunwayNumbers().set(j, "ALL");
						if (cursor++ == sz)
							return 1;
						group = sp[cursor];
					}
					if (++cursor == sz)
						return 1;
					group = sp[cursor];
				}
				if (group.length() >= 3 && group.substring(0, 3).compareTo("RWY") == 0) {
					wafs.getWindShearRunwayNumbers().set(j, group.substring(3));
					if (++cursor == sz)
						return 1;
				} else {
					cursor--;
					break;
				}
			}
		} else {
			cursor--;
		}
		// 13. RMK 附注组
		group = sp[cursor];
		if (isTrendSign(group) == -2)
			return 1;
		if (group.length() >= 3 && group.substring(0, 3).compareTo("RMK") != 0) {
			if (++cursor == sz)
				return 1; // return -1; 2018-07-03
			group = sp[cursor];
		}
		if (isTrendSign(group) == -2)
			return 1;
		if (group.length() >= 3 && group.substring(0, 3).compareTo("RMK") == 0) {
			String annotation = "";
//			for (int c = cursor; c < sp.length; c++)
//				annotation += (" " + sp[c]);
//			wafs.setAnnotations(annotation.substring(1));
			
			for (int c = cursor; c < sp.length; c++)
				annotation += (" " + sp[c]);
			if(annotation.length() >= 6){
				wafs.setAnnotations(annotation.substring(5));
			}
			else{
				wafs.setAnnotations("");
			}
		}
		return 1;
	}

	/**
	 * 字符串 str1 开头连续有 n 个字符都不含字符串 str2 中的字符,则返回的数值为 n
	 * 
	 * @param str1
	 * @param str2
	 * @return: int
	 */
	private int strcspn(String str1, String str2) {
		int len1 = 0, len2 = 0, min = 0, i = 0, j = 0;
		if (str1 != null && str2 != null) {
			len1 = str1.length();
			len2 = str2.length();
			min = len1;
			for (i = 0; i < len2; i++) {
				for (j = 0; j < len1;) {
					if (str2.charAt(i) == str1.charAt(j))
						break;
					j++;
				}
				if (j < min)
					min = j;
			}
		}
		return min;
	}

	/**
	 * 搜索视程障碍现象，与WW编码对应
	 * 
	 * @param group 待解析字符串
	 * @return: int 1：格式正确；其他：格式不正确
	 */
	private int SearchWW(String group) {
		int i = 0;
		int ret = 0; // 失败
		// 30*2
		String ww[] = { "MI", "BC", "PR", "DR", "BL", "SH", "TS", "FZ", "DZ", "RA", "SN", "SG", "IC", "PL", "GR", "GS",
				"BR", "FG", "FU", "VA", "DU", "SA", "HZ", "PO", "SQ", "FC", "SS", "DS", "UP", "VC" };
		for (i = 0; i < 30; i++) {
			if (group.contains(ww[i])) {
				if (group.contains("OVC"))
					return ret; // 失败
				ret = 1; // 成功
				break;
			}
		}
		return ret;
	}

	/**
	 * Search cloud group
	 * 
	 * @param group 待解析字符串
	 * @return: int 1：格式正确；其他：格式不正确
	 */
	private int SearchCloud(String group) {
		int i = 0, j = 0;
		int ret = 0;
		String cloud[] = { "SKC", "CLR", "NSC", "FEW", "SCT", "BKN", "OVC", "VV", "///"};
		for (i = 0; i < 9; i++) {
			if (group.startsWith(cloud[i])) {
				ret = 1;
				if (group.length() > 3) {
					for (j = 2; j < group.length(); j++) {
						if (group.charAt(0) != 'V' && j == 2)
							continue;
						if (!Character.isDigit(group.charAt(j)) && group.charAt(j) != '/') {
							ret = 0; // 失败
							break;
						}
					}
				}
				break;
			}
		}
		return ret;
	}

	/**
	 * Search TT/TdTd group
	 * 
	 * @param group 待解析字符串
	 * @return: int 1：格式正确；其他：格式不正确
	 */
	private int checkTT(String[] s) {
		int i = 0;
		String temp_val = "";
		int ret = 1; // 成功
		String group = s[0];
		if (group.indexOf("/") == -1)
			return 0;
		temp_val = group.substring(group.indexOf('/') + 1);
		if (temp_val.contains("/"))
			return 0;
		for (i = 0; i < group.length(); i++) {
			// change 'M' to '-' for conversion
			if (!Character.isDigit(group.charAt(i)) && group.charAt(i) != '/' && group.charAt(i) != 'M'
					&& group.charAt(i) != '-') {
				ret = 0;
				break;
			}
			if (group.charAt(i) == 'M')
				group = group.substring(0, i) + "-" + group.substring(i + 1);
		}
		s[0] = group;
		return ret;
	}

	/**
	 * Search sea level air pressure group.
	 * 
	 * @param group 待解析字符串
	 * @return: int 1：格式正确；其他：不正确
	 */
	private int checkPH(String group) {
		int i = 0;
		int ret = 1; // 成功
		// 正确格式为 Qdddd或Adddd, d代表一个数字
		if (group.charAt(0) != 'Q' && group.charAt(0) != 'A')
			return 0;
		if (group.length() < 5)
			return 1;
		group = group.replaceAll("/", "9");
		for (i = 1; i < 5; i++) {
			if (!Character.isDigit(group.charAt(i))) {
//				group = group.substring(0, i) + "9" + group.substring(i + 1);
				ret = 0;
				break;
			}
		}
		return ret;// 1;
	}

	/**
	 * 当前的数据是否有效
	 * 
	 * @param group 要判断的字符串
	 * @return: int 1：有效；其他：无效
	 */
	private int IsValidGrp(String group) {
		int i = 0;
		int ret = 0;// 失败
		// 如果group的字符不全是'/'，则为有效
		for (i = 0; i < group.length(); i++) {
			if (group.charAt(i) != '/') {
				ret = 1;
				break;
			}
		}
		return ret;
	}

	/**
	 * 是否检测到趋势报告符号
	 * 
	 * @param group 要判断的字符串
	 * @return: int 1：是，其他：否
	 */
	private int isTrendSign(String group) {
		// 变化指示符号：BECMG或TEMPO；天气状况：NOSIG 无重要天气变化
		if (group.equals("BECMG") || group.equals("TEMPO") || group.equals("INTER") || group.equals("NOSIG")) {
			return -2;
		} else
			return 1;
	}

	/**
	 * 
	 * *******************************************************************************************<br>
	 * <strong> All Rights Reserved By National Meteorological Information Centre
	 * (NMIC) </strong><br>
	 * *******************************************************************************************<br>
	 * 报文头实体类
	 *
	 * <p>
	 * notes:
	 * <ul>
	 * <li>定义参考以下文档
	 * <ol>
	 * <li><a href=" "> 《民航气象中心传输资料格式说明--for气象局PA3.docx》 </a>
	 * </ol>
	 * </li>
	 * </ul>
	 * 
	 * <pre>
	 * SOFTWARE HISTORY
	 * Date         Engineer    Description
	 * ------------ ----------- --------------------------
	 * 2018年8月27日 上午9:40:31   cuihongyuan    Initial creation.
	 * </pre>
	 * 
	 * @author cuihongyuan
	 * @version 0.0.1
	 */
	private class BullHeader {
		/**
		 * 资料类型
		 */
		private String tt = "";
		/**
		 * 发报中心
		 */
		private String cccc = "";
		/**
		 * 更正报标识
		 */
		private String bbb = "";

		public String getTt() {
			return tt;
		}

		public void setTt(String tt) {
			this.tt = tt;
		}

		public void setAa(String aa) {
		}

		public void setIi(String ii) {
		}

		public String getCccc() {
			return cccc;
		}

		public void setCccc(String cccc) {
			this.cccc = cccc;
		}

		public void setYYGGgg(String yYGGgg) {
		}

		public String getBbb() {
			return bbb;
		}

		public void setBbb(String bbb) {
			this.bbb = bbb;
		}

	}

	public static void main(String[] args) {
		DecodeWAFS decodeWAFS = new DecodeWAFS();
		ParseResult<WAFS> parseResult = decodeWAFS
				.decodeWAFS(new File("D:\\HUAXIN\\对比\\地面资料验证\\wafs-metar-抽检\\A_SANL33EHDB190455_C_BABJ_20190519050406_61703.MSG"));
//		@SuppressWarnings("rawtypes")
//		List<ReportInfo> list = parseResult.getReports();
//		System.out.println(list.size());
		List<WAFS> wafs = parseResult.getData();
		System.out.println(wafs.get(0).getTemperature());
	}

}