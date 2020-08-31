package cma.cimiss2.dpc.decoder.cawn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.luaj.vm2.lib.PackageLib.require;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.CawnPM10;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;
/**
 * 
 * <br>
 * @Title:  DecodePm10.java
 * @Package org.cimiss2.decode.z_cawn.pm10
 * @Description:(大气成分气溶胶PM10质量浓度解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月23日 下午4:29:12   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class DecodePm10 {
	
	private ParseResult<CawnPM10> parseResult = new ParseResult<CawnPM10>(false);
	/**
	 * 
	 * @Title: decodeFile
	 * @Description:(大气成分气溶胶pm10质量浓度解码函数)
	 * @param file 文件对象
	 * @return ParseResult<CawnPM10> 解码结果集
	 * @throws：
	 */
	public ParseResult<CawnPM10> decodeFile(File file) {
		
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			try {
				// get file encode
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
				fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;
				
				List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
				// 首先判断文件不是空的，然后需要判断最少有一行数据
				if (txtFileContent != null && txtFileContent.size() >= 1) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					for (int i = 0; i < txtFileContent.size(); i++) {
						CawnPM10 pm10 = new CawnPM10();
						String[] pm10_list=txtFileContent.get(i).replaceAll("\\s+", "").split(",");
						if (pm10_list.length==21) {
							try {
								pm10.setStationNumberChina(pm10_list[0]);//站号
								pm10.setProjectCode(Double.parseDouble(pm10_list[1]));//项目代码
								// 年份
								String year = pm10_list[2];
								// 序日
								String julianDay = pm10_list[3];
								// 时分
								String hourMin = pm10_list[4];
								String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));
								try {
									Date observationTime = sdf.parse(ymd + hourMin + "00");
									pm10.setObservationTime(observationTime);//观测时间
									pm10.setReport_time(observationTime);//观测时间
									
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
									reportError.setMessage("DataTime error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
								parseResult.put(new ReportInfo<AgmeReportHeader>(pm10,txtFileContent.get(i)));
								parseResult.setSuccess(true);
								pm10.setAvgConcentration_5Min(Double.parseDouble(pm10_list[5]));//五分钟平均浓度
								pm10.setAvgConcentration_1Hour(Double.parseDouble(pm10_list[6]));//1 小时平均浓度
								pm10.setAvgConcentration_24Hour(Double.parseDouble(pm10_list[7]));//24小时平均浓度
								pm10.setTotalMass(Double.parseDouble(pm10_list[8]));//总质量
								pm10.setMainRoadFlow(Double.parseDouble(pm10_list[9]));//主路流量
								pm10.setSideRoadFlow(Double.parseDouble(pm10_list[10]));//旁路流量
								pm10.setLoadFactor(Double.parseDouble(pm10_list[11]));//负载率
								pm10.setFrequency(Double.parseDouble(pm10_list[12]));//频率
								pm10.setNoise(Double.parseDouble(pm10_list[13]));//噪音
								pm10.setAirTemperature(Double.parseDouble(pm10_list[14]));//空气温度
								pm10.setAirPressure(Double.parseDouble(pm10_list[15]));//气压
								pm10.setRunningState(Double.parseDouble(pm10_list[16]));//台风状态码
								pm10.setMainRoadTemperature(Double.parseDouble(pm10_list[17]));//主路温度
								pm10.setMainRoadRelativeHumidity(Double.parseDouble(pm10_list[18]));//主路相对温度
								pm10.setSideRoadRelativeHumidity(Double.parseDouble(pm10_list[19]));//旁路相对湿度
								pm10.setAirRelativeHumidity(Double.parseDouble(pm10_list[20]));//空气相对湿度
								parseResult.put(pm10);
								parseResult.setSuccess(true);
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digit error!");
								re.setSegment(txtFileContent.get(i));
								parseResult.put(re);
								continue;
							}
							
						} else {
							ReportError reportError = new ReportError();
							reportError.setMessage("Report length error!");
							reportError.setSegment(txtFileContent.toString());
							parseResult.put(reportError);
							continue;
						}
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
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}
		} else {
			// file not exsit
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
}
