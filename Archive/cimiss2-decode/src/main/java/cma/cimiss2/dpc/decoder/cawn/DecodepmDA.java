package cma.cimiss2.dpc.decoder.cawn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.pmDA;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;
/**
 * 
 * <br>
 * @Title:  DecodepmDA.java
 * @Package org.cimiss2.decode.z_cawn.pmDA
 * @Description:(气溶胶初级质控日值资料解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月11日 下午2:47:48   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class DecodepmDA {
	private ParseResult<pmDA> parseResult = new ParseResult<pmDA>(false);
	private final static String DEFAULT_MISS_VALUE = "999999";
	private String dataType =null;
	public final static String DATETIME_FORMAT_YMD = "yyyyMMdd";
	public ParseResult<pmDA> decodeFile(File file) {
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			try {
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
				fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
				String fileName = file.getName();
				String []  file_list =fileName.split("_");
				dataType = checkFileName(file_list);
				if (dataType.equals("PMMUL")) {
					this.decodePMMUL(file, fileCode,file_list[0]);//资料类型为PMMUL格式
				}else if (dataType.equals("PM25")) {
					this.decodePM25(file, fileCode,file_list[0]);//资料类型为PM25格式
				}else {
					this.decodePM10(file, fileCode,file_list[0]);//资料类型为PM10格式
				}
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}
		}else {
			// file not exsit
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
		
	}
	/**
	 * 
	 * @Title: decodePM10
	 * @Description:(解析格式为PM10的解码函数)
	 * @param file 文件
	 * @param fileCode 文件编码
	 * @param file_list 文件分割后集合
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException void
	 * @throws：
	 */
	private void decodePM10(File file, String fileCode, String file_list) throws UnsupportedEncodingException, FileNotFoundException {
		List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
		// 首先判断文件不是空的，然后需要判断最少有2行数据
		if (txtFileContent != null && txtFileContent.size() >= 2) {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME_FORMAT_YMD);
			String head = txtFileContent.get(0);
			String[] heads = head.split(",");
			if (heads.length==7) {
				String stationNumberChina = file_list;
				for (int i = 1; i < txtFileContent.size(); i++) {
					pmDA pmda = new pmDA();
					String[] pmda_list=txtFileContent.get(i).replace(",",", ").split(",");
					for(int idx=0;idx<pmda_list.length;idx++){
						   if(pmda_list[idx].equals(" ")){
							   pmda_list[idx]=DEFAULT_MISS_VALUE;
						   }
					   }
				    if (pmda_list.length==7) {
				    	try {
				    		pmda.setStationNumberChina(stationNumberChina);//站号
					    	try {
					    		Date observationTime = sdf.parse(pmda_list[0].trim());
								pmda.setObservationTime(observationTime);
								pmda.setReport_time(observationTime);
								
								//2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(observationTime)){
									ReportError reportError = new ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
								
							} catch (ParseException e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("Datatime error!");
								reportError.setSegment(txtFileContent.get(i));
								parseResult.put(reportError);
								continue;
							}
					    	pmda.setPM10_5min_AVG(ToBeValidDouble(pmda_list[1].trim()));//5分钟观测PM10日均值
					    	pmda.setPM10_1h_AVG(ToBeValidDouble(pmda_list[2].trim()));//1小时观测PM10日均值
					    	pmda.setPM10_24h_AVG(ToBeValidDouble(pmda_list[3].trim()));//24小时观测PM10日均值
					    	pmda.setPM10_5min_AVG_COUNT(ToBeValidDouble(pmda_list[4].trim()));//5分钟观测PM10日均值每日内有效小时样本数
					    	pmda.setPM10_1h_AVG_COUNT(ToBeValidDouble(pmda_list[5].trim()));//1小时观测PM10日均值每日内有效小时样本数
					    	pmda.setPM10_24h_AVG_COUNT(ToBeValidDouble(pmda_list[6].trim()));//24小时PM10均值每日内有效小时样本数
					    	parseResult.put(pmda);
							parseResult.put(new ReportInfo<AgmeReportHeader>(pmda,txtFileContent.get(0)+"\n"+ txtFileContent.get(i)));
							parseResult.setSuccess(true);
						} catch (NumberFormatException e) {
							ReportError re = new ReportError();
							re.setMessage("Digit error! ");
							re.setSegment(txtFileContent.get(i));
							parseResult.put(re);
							continue;
						}
				    }else{
				    	ReportError reportError = new ReportError();
						reportError.setMessage("Number of segments error!");
						reportError.setSegment(txtFileContent.get(i));
						parseResult.put(reportError);
						continue;
				    }
				}
			} else if (heads.length == 9) {
				String stationNumberChina = file_list;
				for (int i = 1; i < txtFileContent.size(); i++) {
					pmDA pmda = new pmDA();
					String[] pmda_list=txtFileContent.get(i).replace(",",", ").split(",");
					for(int idx=0;idx<pmda_list.length;idx++){
						   if(pmda_list[idx].equals(" ")){
							   pmda_list[idx]=DEFAULT_MISS_VALUE;
						   }
					   }
				    if (pmda_list.length==9) {
				    	try {
				    		pmda.setStationNumberChina(stationNumberChina);//站号
					    	try {
					    		Date observationTime = sdf.parse(pmda_list[0].trim());
								pmda.setObservationTime(observationTime);
								pmda.setReport_time(observationTime);
								
								//2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(observationTime)){
									ReportError reportError = new ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
								
							} catch (ParseException e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("DateTime error!");
								reportError.setSegment(txtFileContent.get(i));
								parseResult.put(reportError);
								continue;
							}
					    	pmda.setPM10_5min_AVG(ToBeValidDouble(pmda_list[1].trim()));//5分钟观测PM10日均值
					    	pmda.setPM10_30min_AVG(ToBeValidDouble(pmda_list[2].trim()));//30分钟观测PM10日均值
					    	pmda.setPM10_1h_AVG(ToBeValidDouble(pmda_list[3].trim()));//1小时观测PM10日均值
					    	pmda.setPM10_24h_AVG(ToBeValidDouble(pmda_list[4].trim()));//24小时观测PM10日均值
					    	pmda.setPM10_5min_AVG_COUNT(ToBeValidDouble(pmda_list[5].trim()));//5分钟观测PM10日均值每日内有效小时样本数
					    	pmda.setPM10_30min_AVG_COUNT(ToBeValidDouble(pmda_list[6].trim()));//30分钟观测PM10日均值每日内有效小时样本数
					    	pmda.setPM10_1h_AVG_COUNT(ToBeValidDouble(pmda_list[7].trim()));//1小时观测PM10日均值每日内有效小时样本数
					    	pmda.setPM10_24h_AVG_COUNT(ToBeValidDouble(pmda_list[8].trim()));//24小时PM10均值每日内有效小时样本数
					    	parseResult.put(pmda);
							parseResult.put(new ReportInfo<AgmeReportHeader>(pmda,txtFileContent.get(0)+"\n"+ txtFileContent.get(i)));
							parseResult.setSuccess(true);
						}catch (NumberFormatException e) {
							ReportError re = new ReportError();
							re.setMessage("Digit error!");
							re.setSegment(txtFileContent.get(i));
							parseResult.put(re);
							continue;
						}
				    }else{
				    	ReportError reportError = new ReportError();
						reportError.setMessage("Number of Segments error!");
						reportError.setSegment(txtFileContent.get(i));
						parseResult.put(reportError);
						continue;
				    }
				}
			}else {
				ReportError reportError = new ReportError();
				reportError.setMessage("Report header error!");
				reportError.setSegment(txtFileContent.get(0));
				parseResult.put(reportError);
			}
		}else {
			if (txtFileContent == null || txtFileContent.size() == 0) {
				// 空文件
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
			} else {
				// 数据只有一行，格式不正确
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
		}
		
	}
	/**
	 * 
	 * @Title: decodePM25
	 * @Description:(解析格式为PM25的解码函数)
	 * @param file 文件
	 * @param fileCode 文件编码
	 * @param file_list 文件分割后集合
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException void
	 * @throws：
	 */
	private void decodePM25(File file, String fileCode, String file_list) throws UnsupportedEncodingException, FileNotFoundException {
		List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
		// 首先判断文件不是空的，然后需要判断最少有2行数据
		if (txtFileContent != null && txtFileContent.size() >= 2) {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME_FORMAT_YMD);
			String head = txtFileContent.get(0);
			String[] heads = head.split(",");
			if (heads.length==7) {
				String stationNumberChina = file_list;
				for (int i = 1; i < txtFileContent.size(); i++) {
					pmDA pmda = new pmDA();
					String[] pmda_list=txtFileContent.get(i).replace(",",", ").split(",");
					for(int idx=0;idx<pmda_list.length;idx++){
						   if(pmda_list[idx].equals(" ")){
							   pmda_list[idx]=DEFAULT_MISS_VALUE;
						   }
					   }
				    if (pmda_list.length==7) {
				    	try {
				    		pmda.setStationNumberChina(stationNumberChina);//站号
					    	try {
					    		Date observationTime = sdf.parse(pmda_list[0].trim());
								pmda.setObservationTime(observationTime);
								pmda.setReport_time(observationTime);
								//2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(observationTime)){
									ReportError reportError = new ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
							} catch (ParseException e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("DateTime error!");
								reportError.setSegment(txtFileContent.get(i));
								parseResult.put(reportError);
								continue;
							}
					    	pmda.setPM2p5_5min_AVG(ToBeValidDouble(pmda_list[1].trim()));//5分钟观测PM2.5日均值
					    	pmda.setPM2p5_1h_AVG(ToBeValidDouble(pmda_list[2].trim()));//1小时观测PM2.5日均值
					    	pmda.setPM2p5_24h_AVG(ToBeValidDouble(pmda_list[3].trim()));//24小时观测PM2.5日均值
					    	pmda.setPM2p5_5min_AVG_COUNT(ToBeValidDouble(pmda_list[4].trim()));//5分钟观测PM25日均值每日内有效小时样本数
					    	pmda.setPM2p5_1h_AVG_COUNT(ToBeValidDouble(pmda_list[5].trim()));//1小时观测PM25日均值每日内有效小时样本数
					    	pmda.setPM2p5_24h_AVG_COUNT(ToBeValidDouble(pmda_list[6].trim()));//24小时PM25日均值每日内有效小时样本数
					    	parseResult.put(pmda);
							parseResult.put(new ReportInfo<AgmeReportHeader>(pmda,txtFileContent.get(0)+"\n"+ txtFileContent.get(i)));
							parseResult.setSuccess(true);
						}catch (NumberFormatException e) {
							ReportError re = new ReportError();
							re.setMessage("Digit error!");
							re.setSegment(txtFileContent.get(i));
							parseResult.put(re);
							continue;
						}
				    }else{
				    	ReportError reportError = new ReportError();
						reportError.setMessage("Number of segments error!");
						reportError.setSegment(txtFileContent.get(i));
						parseResult.put(reportError);
						continue;
				    }
				}
			} else{
				ReportError reportError = new ReportError();
				reportError.setMessage("Report header error!");
				reportError.setSegment(txtFileContent.get(0));
				parseResult.put(reportError);
			}
		}else {
			if (txtFileContent == null || txtFileContent.size() == 0) {
				// 空文件
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
			} else {
				// 数据只有一行，格式不正确
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
		}
		
	}
	/**
	 * 
	 * @Title: decodePMMUL
	 * @Description:(解析格式为PMMUL的解码函数)
	 * @param file 文件
	 * @param fileCode 文件编码
	 * @param file_list 文件分割后集合
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException void
	 * @throws：
	 */
	private void decodePMMUL(File file, String fileCode, String file_list)  throws UnsupportedEncodingException, FileNotFoundException{
		List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
		// 首先判断文件不是空的，然后需要判断最少有2行数据
		if (txtFileContent != null && txtFileContent.size() >= 2) {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME_FORMAT_YMD);
			String head = txtFileContent.get(0);
			String[] heads = head.split(",");
			if (heads.length==7) {
				String stationNumberChina = file_list;
				for (int i = 1; i < txtFileContent.size(); i++) {
					pmDA pmda = new pmDA();
					String[] pmda_list=txtFileContent.get(i).replace(",",", ").split(",");
					for(int idx=0;idx<pmda_list.length;idx++){
						   if(pmda_list[idx].equals(" ")){
							   pmda_list[idx]=DEFAULT_MISS_VALUE;
						   }
					   }
				    if (pmda_list.length==7) {
				    	try {
				    		pmda.setStationNumberChina(stationNumberChina);//站号
					    	try {
					    		Date observationTime = sdf.parse(pmda_list[0].trim());
								pmda.setObservationTime(observationTime);
								pmda.setReport_time(observationTime);
								
								//2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(observationTime)){
									ReportError reportError = new ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
							} catch (ParseException e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("DateTime error!");
								reportError.setSegment(txtFileContent.get(i));
								parseResult.put(reportError);
								continue;
							}
					    	pmda.setPM10_1h_AVG(ToBeValidDouble(pmda_list[1].trim()));//1小时观测PM10日均值
					    	pmda.setPM2p5_1h_AVG(ToBeValidDouble(pmda_list[2].trim()));//1小时观测PM25日均值
					    	pmda.setPM1_1h_AVG(ToBeValidDouble(pmda_list[3].trim()));//1小时观测PM1日均值
					    	pmda.setPM10_1h_AVG_COUNT(ToBeValidDouble(pmda_list[4].trim()));//1小时观测PM10日均值每日内有效小时样本数
					    	pmda.setPM2p5_1h_AVG_COUNT(ToBeValidDouble(pmda_list[5].trim()));//1小时观测PM25日均值每日内有效小时样本数
					    	pmda.setPM1_1h_AVG_COUNT(ToBeValidDouble(pmda_list[6].trim()));//1小时观测PM1日均值每日内有效小时样本数
					    	parseResult.put(pmda);
							parseResult.put(new ReportInfo<AgmeReportHeader>(pmda,txtFileContent.get(0)+"\n"+ txtFileContent.get(i)));
							parseResult.setSuccess(true);
						} catch (NumberFormatException e) {
							ReportError re = new ReportError();
							re.setMessage("Digit error!");
							re.setSegment(txtFileContent.get(i));
							parseResult.put(re);
							continue;
						}
				    }else{
				    	ReportError reportError = new ReportError();
						reportError.setMessage("Number of segments error!");
						reportError.setSegment(txtFileContent.get(i));
						parseResult.put(reportError);
						continue;
				    }
				}
			} else{
				ReportError reportError = new ReportError();
				reportError.setMessage("Report header error!");
				reportError.setSegment(txtFileContent.get(0));
				parseResult.put(reportError);
			}
		}else {
			if (txtFileContent == null || txtFileContent.size() == 0) {
				// 空文件
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
			} else {
				// 数据只有一行，格式不正确
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
		}
	}
	
	/**
	 * 
	 * @Title: checkFileName
	 * @Description: (检查文件名，判断数据类型 )
	 * @param file_list 文件名分割后集合
	 * @return String 返回值类型 
	 * @throws：
	 */
	private String checkFileName(String[] file_list) {
		
		if (file_list[1].equals("PMMUL")) {
			dataType="PMMUL";
		}else if (file_list[1].equals("PMM")) {
			dataType="PMMUL";
		}else if (file_list[1].equals("PM25")&& file_list[0].equals("54421")) {
			dataType ="PM25";
		}else if (file_list[1].equals("PM25") && file_list[0] != "54421") {
			dataType ="PMMUL";
		}else if (file_list[1].equals("PM10")) {
			dataType ="PM10";
		}else if (file_list[1].equals("P10")) {
			dataType ="PM10";
		}
		return dataType;
	}
	
	/**
	 * 
	 * @Title: ToBeValidDouble
	 * @Description:(缺测值解析)
	 * @param str 带解析字符串
	 * @return double 返回值类型
	 * @throws：
	 */
	public static double ToBeValidDouble(String str) {
		if (Double.parseDouble(str) == -999.9 || Double.parseDouble(str) == 999.0) {
			return 999999.0;			
		}
		else{
			return Double.parseDouble(str);
		}
	}
}
