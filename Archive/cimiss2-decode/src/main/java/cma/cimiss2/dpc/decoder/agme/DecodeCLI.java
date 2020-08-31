package cma.cimiss2.dpc.decoder.agme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.CroplandMicroclimateData;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

// TODO: Auto-generated Javadoc
/**
 * -------------------------------------------------------------------------------
 * <br>.
 *
 * @author wuzuoqiang
 * ---------------------------------------------------------------------------------
 * @Title:  DecodeCLI.java
 * @Package org.cimiss2.decode.z_agme_cli
 * @Description:    TODO(农田小气候数据解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月18日 下午1:45:23   wuzuoqiang    Initial creation.
 * </pre>
 */
public class DecodeCLI {
	
	/** The parse result. */
	/* 存放数据解析的结果集 */
	private ParseResult<CroplandMicroclimateData> parseResult = new ParseResult<CroplandMicroclimateData>(false);
	
	/** The buffered reader. */
	private BufferedReader bufferedReader = null;

	/**
	 * 函数名：DecodeFile .
	 *
	 * @param file            文件对象
	 * @return ParseResult<PrecipitationObservationDataReg> 解码结果包裹类
	 */
	public ParseResult<CroplandMicroclimateData> DecodeFile(File file) {
		// 判断是否为文件并且是否存在
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
		        @SuppressWarnings("static-access")
				String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
				// 获取文件流
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				// 循环读取文件的行
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// 判断是否为空行
					if(lineTxt.trim().equals("")) {
						continue;
					}
					// 判断文件是否正常结束
					if (lineTxt.trim().endsWith("=")) {
						String[] items = lineTxt.trim().split(",");
						if(items[0].trim().equalsIgnoreCase("StationID")) {
							continue;
						}
						// 判断文件要素完整
						if (items.length==43) {
							CroplandMicroclimateData microclimateData = new CroplandMicroclimateData();
							// 区站号
							microclimateData.setStationNumberChina(items[0]);
							// 台站名
							microclimateData.setStationNameChina(items[1]);
							// 省名
							microclimateData.setProvinceNameChina(items[2]);
							try {
								// 经度
								double lon = Double.parseDouble(items[3]);
								if(lon >= 60 && lon <=180)
									microclimateData.setLongitude(lon);
								else {
									microclimateData.setLongitude(lon/10000.0);
								}
								
								double lat = Double.parseDouble(items[4]);
								if(lat >= 5 && lat <=80)
									microclimateData.setLatitude(lat);
								else {
									// 纬度
									microclimateData.setLatitude( lat/ 10000.0);
								}
								
								
								// 海拔高度
								microclimateData.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(items[5]));
								// 观测作物名称
								microclimateData.setCropName(items[6]);
								// 记录时间
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								SimpleDateFormat sdfs = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
								
								if(items[7].contains("-") && items[7].contains(":")){
									microclimateData.setObservationTime(sdf.parse(items[7]));
								}else if (items[7].contains("/") && items[7].contains(":")) {
									microclimateData.setObservationTime(sdfs.parse(items[7]));
								}
								else {
									if(items[7].trim().length() == 12) {
										items[7] = items[7] + "00";
									}
									microclimateData.setObservationTime(simpleDateFormat.parse(items[7]));
								}
								Calendar afterCalendar = Calendar.getInstance();
								afterCalendar.add(Calendar.HOUR_OF_DAY, 1);
								
								Calendar beforeCalendar = Calendar.getInstance();
								beforeCalendar.add(Calendar.HOUR_OF_DAY, -2);
								if(microclimateData.getObservationTime().after(afterCalendar.getTime())) {
									ReportError re = new ReportError();
									re.setMessage("时间格式检查错误");
									re.setSegment(lineTxt);
									parseResult.put(re);
									continue;
								}else if (microclimateData.getObservationTime().before(beforeCalendar.getTime())) {
									ReportError re = new ReportError();
									re.setMessage("时间格式检查错误");
									re.setSegment(lineTxt);
									parseResult.put(re);
									continue;
								}
								// 本站气压
								microclimateData.setStationPressure(ElementValUtil.ToBeValidDouble(items[8]));
								// 30cm气温
								microclimateData.setTemperature_30(ElementValUtil.ToBeValidDouble(items[9]));
								microclimateData.setTemperature_60(ElementValUtil.ToBeValidDouble(items[10]));
								microclimateData.setTemperature_150(ElementValUtil.ToBeValidDouble(items[11]));
								microclimateData.setTemperature_300(ElementValUtil.ToBeValidDouble(items[12]));
								// 冠层温度
								microclimateData.setTemperatureCanopy(ElementValUtil.ToBeValidDouble(items[13]));
								// 小时降水
								microclimateData.setHourlyPrecipitation(ElementValUtil.ToBeValidDouble(items[14]));
								// 30cm风速
								microclimateData.setWindSpeed_30(ElementValUtil.ToBeValidDouble(items[15]));
								microclimateData.setWindSpeed_60(ElementValUtil.ToBeValidDouble(items[16]));
								microclimateData.setWindSpeed_150(ElementValUtil.ToBeValidDouble(items[17]));
								microclimateData.setWindSpeed_300(ElementValUtil.ToBeValidDouble(items[18]));
								microclimateData.setWindSpeed_600(ElementValUtil.ToBeValidDouble(items[19]));
								// 30cm空气相对湿度
								microclimateData.setRelativeHumidity_Air_30(ElementValUtil.ToBeValidDouble(items[20]));
								microclimateData.setRelativeHumidity_Air_60(ElementValUtil.ToBeValidDouble(items[21]));
								microclimateData.setRelativeHumidity_Air_150(ElementValUtil.ToBeValidDouble(items[22]));
								microclimateData.setRelativeHumidity_Air_300(ElementValUtil.ToBeValidDouble(items[23]));
								// 0cm地温

								microclimateData.setSurfaceTemperature_0(ElementValUtil.ToBeValidDouble(items[24]));
								microclimateData.setSurfaceTemperature_5(ElementValUtil.ToBeValidDouble(items[25]));
								microclimateData.setSurfaceTemperature_10(ElementValUtil.ToBeValidDouble(items[26]));
								microclimateData.setSurfaceTemperature_15(ElementValUtil.ToBeValidDouble(items[27]));
								microclimateData.setSurfaceTemperature_20(ElementValUtil.ToBeValidDouble(items[28]));
								microclimateData.setSurfaceTemperature_30(ElementValUtil.ToBeValidDouble(items[29]));
								microclimateData.setSurfaceTemperature_40(ElementValUtil.ToBeValidDouble(items[30]));
								microclimateData.setSurfaceTemperature_50(ElementValUtil.ToBeValidDouble(items[31]));
								// 10cm土壤体积含水率

								microclimateData.setMoisture_Soil_10(ElementValUtil.ToBeValidDouble(items[32]));
								microclimateData.setMoisture_Soil_20(ElementValUtil.ToBeValidDouble(items[33]));
								microclimateData.setMoisture_Soil_30(ElementValUtil.ToBeValidDouble(items[34]));
								microclimateData.setMoisture_Soil_40(ElementValUtil.ToBeValidDouble(items[35]));
								microclimateData.setMoisture_Soil_50(ElementValUtil.ToBeValidDouble(items[36]));

								// 10cm土壤相对湿度

								microclimateData.setRelativeHumidity_Soil_10(ElementValUtil.ToBeValidDouble(items[37]));
								microclimateData.setRelativeHumidity_Soil_20(ElementValUtil.ToBeValidDouble(items[38]));
								microclimateData.setRelativeHumidity_Soil_30(ElementValUtil.ToBeValidDouble(items[39]));
								microclimateData.setRelativeHumidity_Soil_40(ElementValUtil.ToBeValidDouble(items[40]));
								
								if (items[41].contains("=") && items[41].length() > 1) {
									items[41] = items[41].replace("=", "").trim();
									microclimateData.setRelativeHumidity_Soil_50(ElementValUtil.ToBeValidDouble(items[41]));
								}else if (items[41].trim().equals("=")) {
									microclimateData.setRelativeHumidity_Soil_50(999999);
								}else {
									microclimateData.setRelativeHumidity_Soil_50(ElementValUtil.ToBeValidDouble(items[41]));
								}
								
								parseResult.put(microclimateData);
								parseResult.setSuccess(true);

							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("数字转换异常");
								re.setSegment(lineTxt);
								parseResult.put(re);
								continue;
							} catch (ParseException e) {
								ReportError re = new ReportError();
								re.setMessage("时间格式转换异常");
								re.setSegment(lineTxt);
								parseResult.put(re);
								e.printStackTrace();
								continue;

							}
						}
					} else {
						// 报文中记录没有正常结束
						ReportError re = new ReportError();
						re.setMessage("报文总长度验证错误");
						re.setSegment(lineTxt);
						parseResult.put(re);
					}
				}

			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			} catch (IOException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} finally {
				try {
					if (read != null) {
						read.close();
					}

				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		} else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}

		return parseResult;

	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		DecodeCLI decodeCLI = new DecodeCLI();
		decodeCLI.DecodeFile(new File("D:\\Z_AGME_C_BESZ_20190320084543_O_CLI_FTM.csv"));
		System.out.println(decodeCLI.parseResult.getData().size());
	}

}
