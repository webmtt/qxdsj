package cma.cimiss2.dpc.decoder.cawn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericAerosolPmmul;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;
/**
 * 
 * <br>
 * @Title:  DecodePm25.java
 * @Package org.cimiss2.decode.z_cawn.pm25
 * @Description:(大气成分气溶胶PM25质量浓度（PM25）解码类	)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月9日 下午3:48:09   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class DecodePm25 {
	private ParseResult<AtmosphericAerosolPmmul> parseResult = new ParseResult<AtmosphericAerosolPmmul>(false);

	private final static String NORMAL_FILENAME_REG = "Z_CAWN_\\w+_\\d{14}_O_AER-FLD-PM25.TXT";
	private final static String BAM$TED_FILENAME_REG = "Z_CAWN_\\w+_\\d{14}_O_AER-FLD-PM25-((BAM)|(TED)).TXT";
	private final static double DEFAULT_MISS_VALUE = -999.9;
	/**
	 * 
	 * @Title: decodeFile
	 * @Description: (大气成分气溶胶PM25质量浓度（PM25）解码函数)
	 * @param file 文件对象 
	 * @return ParseResult<AtmosphericAerosolPmmul> 解码结果集
	 * @throws：
	 */
	public ParseResult<AtmosphericAerosolPmmul> decodeFile(File file) {

		
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			try {
				// get file encode
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
		        String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
				fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;

				String fileName = file.getName();
				// BAM或者TED格式解析，共21个字段
				if (Pattern.compile(BAM$TED_FILENAME_REG, Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					this.decodeBAMTED(file, fileCode);
				} else if (Pattern.compile(NORMAL_FILENAME_REG, Pattern.CASE_INSENSITIVE).matcher(fileName).find()) {
					// 无仪器类型格式解析，共8个字段
					this.decodePM25(file, fileCode);
				} 

			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}catch (Exception e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
		} else {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}

	/**
	 * 
	 * @Title: decodeBAMTED
	 * @Description: (大气成分气溶胶PM25质量浓度（PM25） BAM/TED格式解析)
	 * @param file
	 * @param fileCode
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException void
	 * @throws：
	 */
	private void decodeBAMTED(File file, String fileCode) throws UnsupportedEncodingException, FileNotFoundException {
		List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
		// 首先判断文件不是空的，然后需要判断最少有1行数据
		if (txtFileContent != null && txtFileContent.size() >= 1) {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME_FORMAT_YMDHMS);
			for (String dataLine : txtFileContent) {
				String[] elementDatas = (dataLine.replaceAll("\\s+", "") + " ").split(",");
				if (elementDatas.length == 8) {
					AtmosphericAerosolPmmul pm25 = new AtmosphericAerosolPmmul();
					// 观测站区站号
					String stationNumberChina = elementDatas[0];
					pm25.setStationNumberChina(stationNumberChina);
					// 项目代码
					String projectCode= elementDatas[1];
					pm25.setProjectCode(projectCode);
					// 年份
					String year = elementDatas[2];
					// 序日
					String julianDay = elementDatas[3];
					// 时分
					String hourMin = elementDatas[4];

					String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));

					try {
						Date d_datetime = sdf.parse(ymd + hourMin + "00");
						pm25.setD_datetime(d_datetime);
						pm25.setReport_time(d_datetime);
						
						//2019-7-16 cuihongyuan
						if(!TimeCheckUtil.checkTime(d_datetime)){
							ReportError reportError = new ReportError();
							reportError.setMessage("time check error!");
							reportError.setSegment(dataLine);
							parseResult.put(reportError);
							continue;
						}
						
					} catch (ParseException e) {
						ReportError reportError = new ReportError();
						reportError.setMessage("DataTime error!");
						reportError.setSegment(txtFileContent.toString());
						parseResult.put(reportError);
						continue;
					}

					// PM10质量浓度
					String massConcentrationOfPM10 = elementDatas[5];
					pm25.setMassConcentrationOfPM10(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM10, DEFAULT_MISS_VALUE));
					// PM2.5质量浓度
					String massConcentrationOfPM2p5 = elementDatas[6];
					pm25.setMassConcentrationOfPM2p5(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5, DEFAULT_MISS_VALUE));
					// PM1质量浓度
					String massConcentrationOfPM1 = elementDatas[7].trim();
					pm25.setMassConcentrationOfPM1(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM1, DEFAULT_MISS_VALUE));

					parseResult.put(pm25);
					parseResult.put(new ReportInfo<AgmeReportHeader>(pm25, dataLine));

				} else {
					ReportError reportError = new ReportError();
					reportError.setMessage("Report error!");
					reportError.setSegment(dataLine);
					parseResult.put(reportError);
				}
				parseResult.setSuccess(true);
			}
		} else {
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
	 * @Description:(大气成分气溶胶PM25质量浓度（PM25）无观测仪器格式解析)
	 * @param file
	 * @param fileCode
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException void
	 * @throws：
	 */
	private void decodePM25(File file, String fileCode) throws UnsupportedEncodingException, FileNotFoundException {
		List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
		// 首先判断文件不是空的，然后需要判断最少有两行数据
		if (txtFileContent != null && txtFileContent.size() >= 1) {
			SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME_FORMAT_YMDHMS);
			for (String dataLine : txtFileContent) {
				String[] elementDatas = (dataLine.replaceAll("\\s+", "") + " ").split(",");
				if (elementDatas.length == 21) {
					AtmosphericAerosolPmmul pm25 = new AtmosphericAerosolPmmul();
					// 观测站区站号
					String stationNumberChina = elementDatas[0];
					pm25.setStationNumberChina(stationNumberChina);
					// 项目代码
					String projectCode = elementDatas[1];
					pm25.setProjectCode(projectCode);
					// 年份
					String year = elementDatas[2];
					// 序日
					String julianDay = elementDatas[3];
					// 时分
					String hourMin = elementDatas[4];

					String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));

					try {
						Date d_datetime = sdf.parse(ymd + hourMin + "00");
						pm25.setD_datetime(d_datetime);
						pm25.setReport_time(d_datetime);
					} catch (ParseException e) {
						ReportError reportError = new ReportError();
						reportError.setMessage("DataTime error!");
						reportError.setSegment(txtFileContent.toString());
						parseResult.put(reportError);
						continue;
					}
					// PM2.5质量浓度
					String massConcentrationOfPM2p5 = elementDatas[5];
					pm25.setMassConcentrationOfPM2p5(ElementValUtil.CheckValidValue2Double(massConcentrationOfPM2p5, DEFAULT_MISS_VALUE));

					parseResult.put(pm25);
					parseResult.put(new ReportInfo<AgmeReportHeader>(pm25, dataLine));
				} else {
					ReportError reportError = new ReportError();
					reportError.setMessage("Report error!");
					reportError.setSegment(dataLine);
					parseResult.put(reportError);
				}
				parseResult.setSuccess(true);
			}
		} else {
			if (txtFileContent == null || txtFileContent.size() == 0) {
				// 空文件
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
			} else {
				// 数据只有一行，格式不正确
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
		}
	}

}
