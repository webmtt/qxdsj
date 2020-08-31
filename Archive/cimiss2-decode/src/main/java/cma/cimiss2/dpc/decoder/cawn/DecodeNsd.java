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
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericCompositionAerosolConcentration;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;
/**
 * 
 * <br>
 * @Title:  DecodeNsd.java
 * @Package org.cimiss2.decode.z_cawn_nsd
 * @Description:(大气成分气溶胶数浓度（NSD）资料解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月30日 上午10:17:37   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */


public class DecodeNsd {
	/* 存放数据解析的结果集 */
	private ParseResult<AtmosphericCompositionAerosolConcentration> parseResult = new ParseResult<AtmosphericCompositionAerosolConcentration>(false);
	private final static String DEFAULT_MISS_VALUE = "999999";
	/**
	 * 
	 * @Title: decodeFile
	 * @Description:(大气成分气溶胶数浓度（NSD）资料解码函数)
	 * @param file 文件对象
	 * @return ParseResult<AtmosphericCompositionAerosolConcentration> 解码结果集
	 * @throws：
	 */
	public ParseResult<AtmosphericCompositionAerosolConcentration> decodeFile(File file) {
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
						if (txtFileContent.get(i).contains("MAWN")) {
							continue;
						}
						AtmosphericCompositionAerosolConcentration nsd = new AtmosphericCompositionAerosolConcentration();
						String[] nsd_list=txtFileContent.get(i).replace(",",", ").split(",");
					    if (nsd_list.length==67) {
						   try {
							   for(int idx=0;idx<nsd_list.length;idx++){
								   if(nsd_list[idx].equals(" ")){
									   nsd_list[idx]=DEFAULT_MISS_VALUE;
								   }
							   }
								nsd.setStationNumberChina(nsd_list[0].trim());//站号
								nsd.setItemCode(Double.parseDouble(nsd_list[1].trim()));//项目代码
								// 年份
								String year = nsd_list[2].trim();
								// 序日
								String julianDay = nsd_list[3].trim();
								// 时分
								String hourMin = nsd_list[4].trim();
								String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));
								try {
									Date observationTime = sdf.parse(ymd + hourMin + "00");
									nsd.setObservationTime(observationTime);//观测时间
									nsd.setReport_time(observationTime);//观测时间
									
								//  2019-7-16 cuihongyuan
									if(!TimeCheckUtil.checkTime(nsd.getObservationTime())){
										ReportError re = new ReportError();
										re.setMessage("check time error!");
										re.setSegment(txtFileContent.get(i));
										parseResult.put(re);
										continue;
									}
									
								} catch (ParseException e) {
									ReportError reportError = new ReportError();
									reportError.setMessage("DataTime error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
								nsd.setStoragePlace(Double.parseDouble(nsd_list[5].trim()));//存贮位置
								nsd.setWeightFactor(Double.parseDouble(nsd_list[6].trim()));//重量因数
								nsd.setErrorCode(Double.parseDouble(nsd_list[7].trim()));//错误代码
								nsd.setBatteryVoltageCode(Double.parseDouble(nsd_list[8].trim()));//电池电压代码
								nsd.setValveCurrent(Double.parseDouble(nsd_list[9].trim()));//阀电流
								nsd.setCorrectCount(Double.parseDouble(nsd_list[10].trim()));//综合订证计数
								nsd.setPressureCount(Double.parseDouble(nsd_list[11].trim()));//气压计数
								nsd.setBackUp1(Double.parseDouble(nsd_list[12].trim()));//备用
								nsd.setHumidityCount(Double.parseDouble(nsd_list[13].trim()));//湿度计数
								nsd.setTemperatureCount(Double.parseDouble(nsd_list[14].trim()));//温度计数
								nsd.setTimeInterval(Double.parseDouble(nsd_list[15].trim()));//时间间隔
								nsd.setWindSpeedMeteringFactor(Double.parseDouble(nsd_list[16].trim()));//风速计量因子
								nsd.setWindDirectionMeteringFactor(Double.parseDouble(nsd_list[17].trim()));//风向计量因子
								nsd.setPrecipitationMeteringFactor(Double.parseDouble(nsd_list[18].trim()));//降水计量因子
								nsd.setTemperatureSlopeCorrect(Double.parseDouble(nsd_list[19].trim()));//温度斜率订证
								nsd.setHumiditySlopeCorrect(Double.parseDouble(nsd_list[20].trim()));//湿度斜率订证
								nsd.setPressureSlopeCorrect(Double.parseDouble(nsd_list[21].trim()));//气压斜率订证
								nsd.setTemperatureBiasCorrect(Double.parseDouble(nsd_list[22].trim()));//温度偏移订证
								nsd.setHumidityBiasCorrect(Double.parseDouble(nsd_list[23].trim()));//湿度偏移订证
								nsd.setPressureBiasCorrect(Double.parseDouble(nsd_list[24].trim()));//气压偏移订证
								nsd.setWindSpeedSensitivity(Double.parseDouble(nsd_list[25].trim()));//风速灵敏度
								nsd.setWindDirectionDip(Double.parseDouble(nsd_list[26].trim()));//风向倾角
								nsd.setPrecipitationSensorCorrectFactor(Double.parseDouble(nsd_list[27].trim()));//降水传感器订证因子
								nsd.setAirPressure(Double.parseDouble(nsd_list[28].trim()));//气压
								nsd.setBackUp2(Double.parseDouble(nsd_list[29].trim()));//备用
								nsd.setHumidity(Double.parseDouble(nsd_list[30].trim()));//湿度
								nsd.setTemperature(Double.parseDouble(nsd_list[31].trim()));//温度
								nsd.setWindSpeed(Double.parseDouble(nsd_list[32].trim()));//风速
								nsd.setWindDirection(Double.parseDouble(nsd_list[33].trim()));//风向
								nsd.setPrecipitation(Double.parseDouble(nsd_list[34].trim()));//降水
								nsd.setChannelConcentration_C1(Double.parseDouble(nsd_list[35].trim()));//C1通道数浓度
								nsd.setChannelConcentration_C2(Double.parseDouble(nsd_list[36].trim()));//C2通道数浓度
								nsd.setChannelConcentration_C3(Double.parseDouble(nsd_list[37].trim()));//C3通道数浓度
								nsd.setChannelConcentration_C4(Double.parseDouble(nsd_list[38].trim()));//C4通道数浓度
								nsd.setChannelConcentration_C5(Double.parseDouble(nsd_list[39].trim()));//C5通道数浓度
								nsd.setChannelConcentration_C6(Double.parseDouble(nsd_list[40].trim()));//C6通道数浓度
								nsd.setChannelConcentration_C7(Double.parseDouble(nsd_list[41].trim()));//C7通道数浓度
								nsd.setChannelConcentration_C8(Double.parseDouble(nsd_list[42].trim()));//C8通道数浓度
								nsd.setChannelConcentration_C9(Double.parseDouble(nsd_list[43].trim()));//C9通道数浓度
								nsd.setChannelConcentration_C10(Double.parseDouble(nsd_list[44].trim()));//C10通道数浓度
								nsd.setChannelConcentration_C11(Double.parseDouble(nsd_list[45].trim()));//C11通道数浓度
								nsd.setChannelConcentration_C12(Double.parseDouble(nsd_list[46].trim()));//C12通道数浓度
								nsd.setChannelConcentration_C13(Double.parseDouble(nsd_list[47].trim()));//C13通道数浓度
								nsd.setChannelConcentration_C14(Double.parseDouble(nsd_list[48].trim()));//C14通道数浓度
								nsd.setChannelConcentration_C15(Double.parseDouble(nsd_list[49].trim()));//C15通道数浓度
								nsd.setChannelConcentration_C16(Double.parseDouble(nsd_list[50].trim()));//C16通道数浓度
								nsd.setChannelConcentration_C17(Double.parseDouble(nsd_list[51].trim()));//C17通道数浓度
								nsd.setChannelConcentration_C18(Double.parseDouble(nsd_list[52].trim()));//C18通道数浓度
								nsd.setChannelConcentration_C19(Double.parseDouble(nsd_list[53].trim()));//C19通道数浓度
								nsd.setChannelConcentration_C20(Double.parseDouble(nsd_list[54].trim()));//C20通道数浓度
								nsd.setChannelConcentration_C21(Double.parseDouble(nsd_list[55].trim()));//C21通道数浓度
								nsd.setChannelConcentration_C22(Double.parseDouble(nsd_list[56].trim()));//C22通道数浓度
								nsd.setChannelConcentration_C23(Double.parseDouble(nsd_list[57].trim()));//C23通道数浓度
								nsd.setChannelConcentration_C24(Double.parseDouble(nsd_list[58].trim()));//C24通道数浓度
								nsd.setChannelConcentration_C25(Double.parseDouble(nsd_list[59].trim()));//C25通道数浓度
								nsd.setChannelConcentration_C26(Double.parseDouble(nsd_list[60].trim()));//C26通道数浓度
								nsd.setChannelConcentration_C27(Double.parseDouble(nsd_list[61].trim()));//C27通道数浓度
								nsd.setChannelConcentration_C28(Double.parseDouble(nsd_list[62].trim()));//C28通道数浓度
								nsd.setChannelConcentration_C29(Double.parseDouble(nsd_list[63].trim()));//C29通道数浓度
								nsd.setChannelConcentration_C30(Double.parseDouble(nsd_list[64].trim()));//C30通道数浓度
								nsd.setChannelConcentration_C31(Double.parseDouble(nsd_list[65].trim()));//C31通道数浓度
								nsd.setChannelConcentration_C32(Double.parseDouble(nsd_list[66].trim()));//C32通道数浓度
								parseResult.put(new ReportInfo<AgmeReportHeader>(nsd,txtFileContent.get(i)));
								parseResult.put(nsd);
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
							reportError.setMessage("Number of elements error!");
							reportError.setSegment(txtFileContent.get(i));
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