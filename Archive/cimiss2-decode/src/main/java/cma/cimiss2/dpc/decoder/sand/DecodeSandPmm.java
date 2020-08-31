package cma.cimiss2.dpc.decoder.sand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.soap.Text;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.sand.SandAerosolPmm;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;
/**
 * 
 * <br>
 * @Title:  DecodeSandPmm.java
 * @Package org.cimiss2.decode.z_sand.pmm
 * @Description:    TODO(沙尘暴气溶胶质量浓度（PMM)资料解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月15日 下午4:10:21   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class DecodeSandPmm {
	private ParseResult<SandAerosolPmm> parseResult = new ParseResult<SandAerosolPmm>(false);
	private final static double DEFAULT_MISS_VALUE = -999;
	/**
	 * 
	 * @Title: decodeFile
	 * @Description: TODO(沙尘暴气溶胶质量浓度（PMM)资料解码函数)
	 * @param file 文件对象
	 * @return ParseResult<SandAerosolPmm> 解码结果集
	 * @throws：
	 */
	public ParseResult<SandAerosolPmm> decodeFile(File file) {
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
				SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.DATETIME_FORMAT_YMDHMS);
				// 首先判断文件不是空的，然后需要判断最少有一行数据
				if (txtFileContent != null && txtFileContent.size() >= 1) {	
					for(int i = 0;i<txtFileContent.size();i++){
						String[] pmm_list=txtFileContent.get(i).replaceAll("\\s+", "").split(",");
						if (pmm_list.length==38) {
							SandAerosolPmm sand_pmm = new SandAerosolPmm();
							sand_pmm.setStationNumberChina(pmm_list[0]);//站号
							sand_pmm.setProjectCode(ElementValUtil.CheckValidValue2Double(pmm_list[1],DEFAULT_MISS_VALUE));//项目代码
							// 年份
							String year = pmm_list[2];
							// 序日
							String julianDay = pmm_list[3];
							// 时分
							String hourMin = pmm_list[4];

							String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));
							try {
								Date observationTime = sdf.parse(ymd + hourMin + "00");
								sand_pmm.setObservationTime(observationTime);//观测时间
								sand_pmm.setReport_time(observationTime);//观测时间
								
								//2019-7-16 cuihongyan
								if(!TimeCheckUtil.checkTime(sand_pmm.getObservationTime())){
									ReportError reportError = new  ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
								
							} catch (ParseException e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("Time format conversion exception");
								reportError.setSegment(txtFileContent.get(i));
								parseResult.put(reportError);
								continue;
							}
							sand_pmm.setStorageLocation(ElementValUtil.CheckValidValue2Double(pmm_list[5],DEFAULT_MISS_VALUE));//存储位置
							sand_pmm.setWeightFactor(ElementValUtil.CheckValidValue2Double(pmm_list[6],DEFAULT_MISS_VALUE));//重量因数
							sand_pmm.setErrorCode(ElementValUtil.CheckValidValue2Double(pmm_list[7],DEFAULT_MISS_VALUE));//错误代码
							sand_pmm.setBatteryVoltageCode(ElementValUtil.CheckValidValue2Double(pmm_list[8],DEFAULT_MISS_VALUE));//电池电压代码
							sand_pmm.setValveCurrent(ElementValUtil.CheckValidValue2Double(pmm_list[9],DEFAULT_MISS_VALUE));//阀电流
							sand_pmm.setComprehensiveCorrectionCount(ElementValUtil.CheckValidValue2Double(pmm_list[10],DEFAULT_MISS_VALUE));//综合订证计数
							sand_pmm.setBarometricCount(ElementValUtil.CheckValidValue2Double(pmm_list[11],DEFAULT_MISS_VALUE));//气压计数
							sand_pmm.setHumidityCount(ElementValUtil.CheckValidValue2Double(pmm_list[13],DEFAULT_MISS_VALUE));//湿度计数
							sand_pmm.setTemperatureCount(ElementValUtil.CheckValidValue2Double(pmm_list[14],DEFAULT_MISS_VALUE));//温度计数
							sand_pmm.setTimeInterval(ElementValUtil.CheckValidValue2Double(pmm_list[15],DEFAULT_MISS_VALUE));//时间间隔
							sand_pmm.setWindSpeedMeasurementFactor(ElementValUtil.CheckValidValue2Double(pmm_list[16],DEFAULT_MISS_VALUE));//风速计量因子
							sand_pmm.setWindDirectionMeasurementFactor(ElementValUtil.CheckValidValue2Double(pmm_list[17],DEFAULT_MISS_VALUE));//风向计量因子
							sand_pmm.setPrecipitationMeasurementFactor(ElementValUtil.CheckValidValue2Double(pmm_list[18],DEFAULT_MISS_VALUE));//降水计量因子
							sand_pmm.setTemperatureLinearSlopeRate(ElementValUtil.CheckValidValue2Double(pmm_list[19],DEFAULT_MISS_VALUE));//温度斜率订证
							sand_pmm.setHumidityLinearSlopeRate(ElementValUtil.CheckValidValue2Double(pmm_list[20],DEFAULT_MISS_VALUE));//湿度斜率订证
							sand_pmm.setPressureLinearSlopeRate(ElementValUtil.CheckValidValue2Double(pmm_list[21],DEFAULT_MISS_VALUE));//气压斜率订证
							sand_pmm.setTemperatureLinearIntercept(ElementValUtil.CheckValidValue2Double(pmm_list[22],DEFAULT_MISS_VALUE));//温度偏移订证
							sand_pmm.setHumidityLinearIntercept(ElementValUtil.CheckValidValue2Double(pmm_list[23],DEFAULT_MISS_VALUE));//湿度偏移订证
							sand_pmm.setPressureLinearIntercept(ElementValUtil.CheckValidValue2Double(pmm_list[24],DEFAULT_MISS_VALUE));//气压偏移订证
							sand_pmm.setWindSpeedSensitivity(ElementValUtil.CheckValidValue2Double(pmm_list[25],DEFAULT_MISS_VALUE));//风速灵敏度
							sand_pmm.setWindDirectionAngle(ElementValUtil.CheckValidValue2Double(pmm_list[26],DEFAULT_MISS_VALUE));//风向倾角
							sand_pmm.setPrecipitationSensorCorrectionFactor(ElementValUtil.CheckValidValue2Double(pmm_list[27],DEFAULT_MISS_VALUE));//降水传感器订证因子
							sand_pmm.setPressure(ElementValUtil.CheckValidValue2Double(pmm_list[28],DEFAULT_MISS_VALUE));//气压
							sand_pmm.setRelativeHumidity(ElementValUtil.CheckValidValue2Double(pmm_list[30],DEFAULT_MISS_VALUE));//湿度
							sand_pmm.setTemperature(ElementValUtil.CheckValidValue2Double(pmm_list[31],DEFAULT_MISS_VALUE));//温度
							sand_pmm.setWindSpeed(ElementValUtil.CheckValidValue2Double(pmm_list[32],DEFAULT_MISS_VALUE));//风速
							sand_pmm.setWindDirection(ElementValUtil.CheckValidValue2Double(pmm_list[33],DEFAULT_MISS_VALUE));//风向
							sand_pmm.setPrecipitation(ElementValUtil.CheckValidValue2Double(pmm_list[34],DEFAULT_MISS_VALUE));//降水
							sand_pmm.setMassConcentrationOfPM10(ElementValUtil.CheckValidValue2Double(pmm_list[35],DEFAULT_MISS_VALUE));//PM10质量浓度
							sand_pmm.setMassConcentrationOfPM2p5(ElementValUtil.CheckValidValue2Double(pmm_list[36],DEFAULT_MISS_VALUE));//PM10质量浓度
							sand_pmm.setMassConcentrationOfPM1(ElementValUtil.CheckValidValue2Double(pmm_list[37],DEFAULT_MISS_VALUE));//PM10质量浓度
							parseResult.put(sand_pmm);
							parseResult.put(new ReportInfo<AgmeReportHeader>(sand_pmm, txtFileContent.get(i)));
							parseResult.setSuccess(true);
						} else {
							ReportError re = new ReportError();
							re.setMessage("report length error");
							re.setSegment(txtFileContent.get(i));
							parseResult.put(re);
							continue;
						}
					}
				} else {			
						parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
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



