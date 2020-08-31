package cma.cimiss2.dpc.decoder.surf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.QCElement;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfaceRD;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 
 * Main class of decode the road traffic data
 * 公路交通气象观测站实时地面气象要素资料解码<br>
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.surf.SurfaceRD}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 
 * S9926 20180115000000 311921 1042954 05400 119 000<br>
TH 0968 0968 2330 0972 2301 ---- 090 090 2336 ---<br>
RE 0000<br>
WI 001 005 001 005 000 008 2339 002 006 000 010 2336 --- ---<br>
RT ---- ---- ---- ---- ---- ----<br>
VV 03039 02547 2343<br>
RS ------ ----- ---- ---- ---- ---<br>
WW ////////////<br>
MR 000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000<br>
QC<br>
Q1 0000070007 0 00000000000077 777777 000 777777 8 000000000000000000000000000000000000000000000000000000000000<br>
Q2 0000070007 0 00000000000077 777777 000 777777 8 000000000000000000000000000000000000000000000000000000000000<br>
Q3 9999999999 9 99999999999999 999999 999 999999 9 999999999999999999999999999999999999999999999999999999999999=<br>
NNNN<br>

<strong>code:</strong><br>
 * DecodeRD decodeRD = new DecodeRD();<br>
 * ParseResult<SurfaceRD> parseResult = decodeRD.DecodeFile(new File(String filepath));<br>
 * List<ReportInfo> list = parseResult.getReports();<br>
 * System.out.println(parseResult.isSuccess());<br>
 * System.out.println(list.size());<br>
 * <strong>output:</strong><br>
 * true<br>
 * 1<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月24日 下午4:34:24   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeRD {
	/** 
	 * 解码结果
	 */
	private ParseResult<SurfaceRD> parseResult = null;
	/**
	 * 缺测时替代值
	 */
	private static int defaultInt1 = 999999; 
	/**
	 * 无需观测替代值
	 */
	private static int defaultInt2 = 999998;
	/**
	 * 代替观测文件中微量降水",,"
	 */
	private static int tinyInt = 999990;   
	/**
	 *  静风时，观测文件中记录为PPC，数据库中为数值格式，以999017代替
	 */
	private static int staticWind = 999017; 
	
	/**
	 * 公路交通气象观测站实时地面气象要素资料解码函数
	 * @param  file  待解析文件
	 * @return: ParseResult<SurfaceRD>   解码结果集
	 */
	@SuppressWarnings("resource")
	public ParseResult<SurfaceRD> DecodeFile(File file){
		parseResult = new ParseResult<SurfaceRD>(false);
		String encoding = "utf-8";
		if(file != null && file.exists() && file.isFile()){
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Scanner scanner = null;
			try{
				read = new InputStreamReader(new FileInputStream(file), encoding);
				scanner = new Scanner(read).useDelimiter("=");
				while (scanner.hasNext()) {
					// 获取一个完整的报文段
					String report = scanner.next();
					if(report.trim().equalsIgnoreCase("NNNN")) {
						// 报文结束
						break;
					}
					// 移除空行 
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String contents[] = report.split("\n");
					if(contents.length != 13){
						ReportError re = new ReportError();
						re.setMessage("Report format error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					for(int p = 0; p < contents.length; p ++)
						contents[p] = contents[p].trim();

					SurfaceRD surfaceRD = new SurfaceRD();
					// 测站基本信息段 contents[0]
					String baseInfo[] =contents[0].split("\\s+");
					if(baseInfo != null && baseInfo.length == 7){
						surfaceRD.setStationNumberChina(baseInfo[0].trim());
						try {
							surfaceRD.setObservationTime(sdf.parse(baseInfo[1].trim()));
							if(!TimeCheckUtil.checkTime(sdf.parse(baseInfo[1].trim()))){
								ReportError re = new ReportError();
								re.setMessage("DataTime out of range：time:"+sdf.parse(baseInfo[1].trim())+" stationCode:"+baseInfo[0].trim());
								re.setSegment(contents[0]);
								parseResult.put(re);
								continue;
							}
						} catch (ParseException e) {
							ReportError re = new ReportError();
							re.setMessage("DateTime error!");
							re.setSegment(contents[0]);
							parseResult.put(re);
							continue;
						}
//						if(baseInfo[2].trim().contains("/"))
//							surfaceRD.setLatitude((double)defaultInt1);
//						else if(baseInfo[2].trim().contains("--"))
//							surfaceRD.setLatitude((double)defaultInt2);
						if(baseInfo[2].trim().contains("/") || baseInfo[2].contains("--")){
							ReportError re = new ReportError();
							re.setMessage("Latitude can not be invalid!");
							re.setSegment(contents[0]);
							parseResult.put(re);
							continue;
						}
						else{
							//2019-4-10  纬度值判断
							if(baseInfo[2].trim().length() == 6){
								surfaceRD.setLatitude(ElementValUtil.getlatitude(baseInfo[2].trim()));
							}
							else{
								surfaceRD.setLatitude(999999.0);
							}
						}
//						if(baseInfo[3].trim().contains("/"))
//							surfaceRD.setLongitude((double)defaultInt1);
//						else if(baseInfo[3].trim().contains("--"))
//							surfaceRD.setLongitude((double)defaultInt2);
						if(baseInfo[3].contains("/") || baseInfo[3].contains("--")){
							ReportError re = new ReportError();
							re.setMessage("Longtitude can not be invalid!");
							re.setSegment(contents[0]);
							parseResult.put(re);
							continue;
						}
						else{
							//2019-4-10  经度值判断
							if(baseInfo[3].trim().length() == 7){
								surfaceRD.setLongitude(ElementValUtil.getLongitude(baseInfo[3].trim()));
							}
							else{
								surfaceRD.setLongitude(999999.0);
							}
						}
						surfaceRD.setHeightOfSationGroundAboveMeanSeaLevel(getHeightAboveSeaLevel(baseInfo[4]));
						surfaceRD.setQualityControl(baseInfo[5].trim());
						surfaceRD.setFileRevisionSign(baseInfo[6].trim());
						parseResult.put(new ReportInfo<SurfaceRD>(surfaceRD, report));
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("Station information error!");
						re.setSegment(contents[0]);
						parseResult.put(re);
						continue;
					}
					// 数据质量控制码（QC） contents[10~12]

					contents[10] = contents[10].replaceAll("[^0-9|^\\s*$|^Q1]", "9");
					contents[11] = contents[11].replaceAll("[^0-9|^\\s*$|^Q2]", "9");
					contents[12] = contents[12].replaceAll("[^0-9|^\\s*$|^Q3]", "9");

					String QCDevice[] = contents[10].split("\\s+");
					String QCProv[] = contents[11].split("\\s+");
					String QCCountry[] = contents[12].split("\\s+");
//					QCDevice[0] = "Q1";
//					QCProv[0] = "Q2";
//					QCCountry[0] = "Q3";
					if(QCDevice != null && QCProv != null && QCCountry != null && 
							QCDevice.length == 9 && QCProv.length == 9 && QCCountry.length == 9 &&
							QCDevice[0].trim().compareTo("Q1") == 0 && QCProv[0].trim().compareTo("Q2") == 0 && QCCountry[0].trim().compareTo("Q3") == 0){
						// 气温和湿度数据（TH） contents[1]
						int base = 1000;
						int factor = 10;
						int base2 = 0;
						String TH[] = contents[1].split("\\s+");
						if(TH != null && TH.length >= 11 && TH[0].trim().compareTo("TH") == 0 && QCDevice[1].trim().length() >= 10 && QCProv[1].trim().length() >= 10 && QCCountry[1].trim().length() >= 10){
							int i1 = 0, i2 = 0, i3 = 0;
							String qcDevice = QCDevice[1].trim();
							String qcProv = QCProv[1].trim();
							String qcCountry = QCCountry[1].trim();
							if(TH[1].length() == 4)
								surfaceRD.setAirTemperature(new QCElement<Float>(getFloat(TH[1], base, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setAirTemperature(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							//2019-5-22 长度校验，不进行容错 
							if(TH[2].length() == 4)
								surfaceRD.setMaxAirTemperature(new QCElement<Float>(getFloat(TH[2], base, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setMaxAirTemperature(new QCElement<Float>((float)defaultInt1,Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(TH[3].length() == 4)
								surfaceRD.setTimeOfMaxAirTemperature(new QCElement<Integer>(getInt(TH[3]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setTimeOfMaxAirTemperature(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(TH[4].length() == 4)
								surfaceRD.setMinAirTemperature(new QCElement<Float>(getFloat(TH[4], base, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setMinAirTemperature(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(TH[5].length() == 4)
								surfaceRD.setTimeOfMinAirTemperature(new QCElement<Integer>(getInt(TH[5]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setTimeOfMinAirTemperature(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(TH[6].length() == 4)
								surfaceRD.setDewpointTemperature(new QCElement<Float>(getFloat(TH[6], base, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setDewpointTemperature(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(TH[7].length() == 3)
								surfaceRD.setRelativeHumidity(new QCElement<Integer>(getInt(TH[7]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setRelativeHumidity(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(TH[8].length() == 3)
								surfaceRD.setMinRelativeHumidity(new QCElement<Integer>(getInt(TH[8]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setMinRelativeHumidity(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(TH[9].length() == 4)
								surfaceRD.setTimeOfMinRelativeHumidity(new QCElement<Integer>(getInt(TH[9]), Character.getNumericValue(qcDevice.charAt(i1++)),Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setTimeOfMinRelativeHumidity(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)),Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(TH[10].length() == 3)
								surfaceRD.setWaterVaporPressure(new QCElement<Float>(getFloat(TH[10], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setWaterVaporPressure(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Temperature and humidity data or its quality control code error!");
							re.setSegment(contents[1]+ " \n" + QCDevice[1] + " "+QCProv[1] +" " + QCCountry[1]);
							parseResult.put(re);
//							continue;
							surfaceRD.setAirTemperature(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setMaxAirTemperature(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setTimeOfMaxAirTemperature(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setMinAirTemperature(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setTimeOfMinAirTemperature(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setDewpointTemperature(new QCElement<Float>((float)defaultInt1,  9,9,9));
							surfaceRD.setRelativeHumidity(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setMinRelativeHumidity(new QCElement<Integer>(defaultInt1,  9,9,9));
							surfaceRD.setTimeOfMinRelativeHumidity(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setWaterVaporPressure(new QCElement<Float>((float)defaultInt1, 9,9,9));
						
						}
						// 累计降水数据(RE)contents[2]
						String RE[] = contents[2].split("\\s+");
						if(RE != null && RE.length >= 2 && RE[0].trim().compareTo("RE") == 0 && QCDevice[2].trim().length() >= 1 && QCProv[2].trim().length() >= 1 && QCCountry[2].trim().length() >= 1){
							//2019-5-22 加入格式校验
							if(RE[1].length() == 4)
								surfaceRD.setHourlyCumulativePrecipitation(new QCElement<Float>(getFloat(RE[1], base2, factor), Character.getNumericValue(QCDevice[2].trim().charAt(0)), Character.getNumericValue(QCProv[2].trim().charAt(0)), Character.getNumericValue(QCCountry[2].trim().charAt(0))));
							else 
								surfaceRD.setHourlyCumulativePrecipitation(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(QCDevice[2].trim().charAt(0)), Character.getNumericValue(QCProv[2].trim().charAt(0)), Character.getNumericValue(QCCountry[2].trim().charAt(0))));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Cumulative precipitation data or its quality control code error!");
							re.setSegment(contents[2]+ " \n" + QCDevice[2] + " "+QCProv[2] +" " + QCCountry[2]);
							parseResult.put(re);
//							continue;
							surfaceRD.setHourlyCumulativePrecipitation(new QCElement<Float>((float)defaultInt1, '9'));
							
						}
						// 风观测数据(WI) contents[3]
						String WI[] = contents[3].split("\\s+");
						if(WI != null && WI.length >= 15 && WI[0].trim().compareTo("WI") == 0  && QCDevice[3].trim().length() >= 14 && QCProv[3].trim().length() >= 14 && QCCountry[3].trim().length() >= 14){
							int i1 = 0, i2 = 0, i3 = 0;
							String qcDevice = QCDevice[3].trim();
							String qcProv = QCProv[3].trim();
							String qcCountry = QCCountry[3].trim();
							//2019-5-22 加入格式校验
							if(WI[1].length() == 3)
								surfaceRD.setTwoMinWindDirection(new QCElement<Integer>(getWindDirection(WI[1]),  Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setTwoMinWindDirection(new QCElement<Integer>(defaultInt1,  Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[2].length() == 3)
								surfaceRD.setTwoMinWindAvgSpeed(new QCElement<Float>(getFloat(WI[2], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setTwoMinWindAvgSpeed(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[3].length() == 3)
								surfaceRD.setTenMinWindDirection(new QCElement<Integer>(getWindDirection(WI[3]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setTenMinWindDirection(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[4].length() == 3)
								surfaceRD.setTenMinWindAvgSpeed(new QCElement<Float>(getFloat(WI[4], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)),Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setTenMinWindAvgSpeed(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)),Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[5].length() == 3)
								surfaceRD.setDirectionOfMaxWindSpeed(new QCElement<Integer>(getWindDirection(WI[5]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setDirectionOfMaxWindSpeed(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[6].length() == 3)
								surfaceRD.setMaxWindSpeed(new QCElement<Float>(getFloat(WI[6], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setMaxWindSpeed(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[7].length() == 4)
								surfaceRD.setTimeOfMaxWindSpeed(new QCElement<Integer>(getInt(WI[7]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setTimeOfMaxWindSpeed(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[8].length() == 3)
								surfaceRD.setInstantaneousWindDirection(new QCElement<Integer>(getWindDirection(WI[8]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setInstantaneousWindDirection(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[9].length() == 3)
								surfaceRD.setInstantaneousWindSpeed(new QCElement<Float>(getFloat(WI[9], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setInstantaneousWindSpeed(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[10].length() == 3)
								surfaceRD.setDirectionOfExtremWindSpeed(new QCElement<Integer>(getWindDirection(WI[10]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)),Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setDirectionOfExtremWindSpeed(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)),Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[11].length() == 3)
								surfaceRD.setExtremWindSpeed(new QCElement<Float>(getFloat(WI[11], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setExtremWindSpeed(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[12].length() == 4)
								surfaceRD.setTimeOfExtremWindSpeed(new QCElement<Integer>(getInt(WI[12]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setTimeOfExtremWindSpeed(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[13].length() == 3)
								surfaceRD.setDirectionOfExtremWindSpeedMinutely(new QCElement<Integer>(getWindDirection(WI[13]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setDirectionOfExtremWindSpeedMinutely(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(WI[14].length() == 3)
								surfaceRD.setExtremeWindSpeedMinutely(new QCElement<Float>(getFloat(WI[14], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setExtremeWindSpeedMinutely(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Wind observation data or its quality control code error!");
							re.setSegment(contents[3]+ " \n" + QCDevice[3] + " "+QCProv[3] +" " + QCCountry[3]);
							parseResult.put(re);
//							continue;
							surfaceRD.setTwoMinWindDirection(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setTwoMinWindAvgSpeed(new QCElement<Float>((float)defaultInt1, 9,9,9 ));
							surfaceRD.setTenMinWindDirection(new QCElement<Integer>(defaultInt1,  9,9,9));
							surfaceRD.setTenMinWindAvgSpeed(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setDirectionOfMaxWindSpeed(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setMaxWindSpeed(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setTimeOfMaxWindSpeed(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setInstantaneousWindDirection(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setInstantaneousWindSpeed(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setDirectionOfExtremWindSpeed(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setExtremWindSpeed(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setTimeOfExtremWindSpeed(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setDirectionOfExtremWindSpeedMinutely(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setExtremeWindSpeedMinutely(new QCElement<Float>((float)defaultInt1, 9,9,9));
						
						}
						// 路温数据(RT) contents[4]
						String RT[] = contents[4].split("\\s+");
						if(RT != null && RT.length >= 7 && RT[0].trim().compareTo("RT") == 0 && QCDevice[4].trim().length() >= 6 && QCProv[4].trim().length() >= 6 && QCCountry[4].trim().length() >= 6){
							int i1 = 0, i2 = 0, i3 = 0;
							String qcDevice = QCDevice[4].trim();
							String qcProv = QCProv[4].trim();
							String qcCountry = QCCountry[4].trim();
							// 2019-5-22  加入各市教研
							if(RT[1].length() == 4)
								surfaceRD.setRoadSurfTemperature(new QCElement<Float>(getFloat(RT[1], base, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setRoadSurfTemperature(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(RT[2].length() == 4)
								surfaceRD.setMaxRoadSurfTemperature(new QCElement<Float>(getFloat(RT[2], base, factor),Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setMaxRoadSurfTemperature(new QCElement<Float>((float)defaultInt1,Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(RT[3].length() == 4)
								surfaceRD.setTimeOfMaxRoadSurfTemperature(new QCElement<Integer>(getInt(RT[3]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setTimeOfMaxRoadSurfTemperature(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(RT[4].length() == 4)
								surfaceRD.setMinRoadSurfTemperature(new QCElement<Float>(getFloat(RT[4], base, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else 
								surfaceRD.setMinRoadSurfTemperature(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(RT[5].length() == 4)
								surfaceRD.setTimeOfMinRoadSurfTemperature(new QCElement<Integer>(getInt(RT[5]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setTimeOfMinRoadSurfTemperature(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(RT[6].length() == 4)
								surfaceRD.setRoadBaseTemperature(new QCElement<Float>(getFloat(RT[6], base, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setRoadBaseTemperature(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Road temperature data or its quality control code error!");
							re.setSegment(contents[4]+ " \n" + QCDevice[4] + " "+QCProv[4] +" " + QCCountry[4]);
							parseResult.put(re);
//							continue;
							surfaceRD.setRoadSurfTemperature(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setMaxRoadSurfTemperature(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setTimeOfMaxRoadSurfTemperature(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setMinRoadSurfTemperature(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setTimeOfMinRoadSurfTemperature(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setRoadBaseTemperature(new QCElement<Float>((float)defaultInt1, 9,9,9));
						
						}
						// 能见度数据(VV) contents[5]
						String VV[] = contents[5].split("\\s+");
						if(VV != null && VV.length >= 4 && VV[0].trim().compareTo("VV") == 0 && QCDevice[5].trim().length() >= 3 && QCProv[5].trim().length() >= 3 && QCCountry[5].trim().length() >= 3){
							int i1 = 0, i2 = 0, i3 = 0;
							String qcDevice = QCDevice[5].trim();
							String qcProv = QCProv[5].trim();
							String qcCountry = QCCountry[5].trim();
							
							// 2019-5-22 加入格式校验
							if(VV[1].length() == 5)
								surfaceRD.setOneMinAvgVisibility(new QCElement<Integer>(getInt(VV[1]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setOneMinAvgVisibility(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(VV[2].length() == 5)
								surfaceRD.setMinVisibility(new QCElement<Integer>(getInt(VV[2]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setMinVisibility(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(VV[3].length() == 4)
								surfaceRD.setTimeOfMinVisibility(new QCElement<Integer>(getInt(VV[3]), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setTimeOfMinVisibility(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Visibility data or its quality control code error!");
							re.setSegment(contents[5]+ " \n" + QCDevice[5] + " "+QCProv[5] +" " + QCCountry[5]);
							parseResult.put(re);
//							continue;
							surfaceRD.setOneMinAvgVisibility(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setMinVisibility(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setTimeOfMinVisibility(new QCElement<Integer>(defaultInt1, 9,9,9));
						}
						// 路面状况数据(RS) contents[6]
						String RS [] = contents[6].split("\\s+");
						if(RS != null && RS.length >= 7 && RS[0].trim().compareTo("RS") == 0 && RS[1].trim().length() >= 5
								&& QCDevice[6].trim().length() >= 6 && QCProv[6].trim().length() >= 6 && QCCountry[6].trim().length() >= 6){
							int i1 = 0, i2 = 0, i3 = 0;
							String qcDevice = QCDevice[6].trim();
							String qcProv = QCProv[6].trim();
							String qcCountry = QCCountry[6].trim();
							surfaceRD.setRoadSurfCondition(new ArrayList<QCElement<Integer>>());
							if(RS[1].trim().length() == 6){
								surfaceRD.getRoadSurfCondition().add(new QCElement<Integer>(getInt(RS[1].substring(0, 2)), Character.getNumericValue(qcDevice.charAt(i1)), Character.getNumericValue(qcProv.charAt(i2)), Character.getNumericValue(qcCountry.charAt(i3))));
								surfaceRD.getRoadSurfCondition().add(new QCElement<Integer>(getInt(RS[1].substring(2, 4)), Character.getNumericValue(qcDevice.charAt(i1)), Character.getNumericValue(qcProv.charAt(i2)), Character.getNumericValue(qcCountry.charAt(i3))));
								surfaceRD.getRoadSurfCondition().add(new QCElement<Integer>(getInt(RS[1].substring(4)), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							}
							else{
								surfaceRD.getRoadSurfCondition().add(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1)), Character.getNumericValue(qcProv.charAt(i2)), Character.getNumericValue(qcCountry.charAt(i3))));
								surfaceRD.getRoadSurfCondition().add(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1)), Character.getNumericValue(qcProv.charAt(i2)), Character.getNumericValue(qcCountry.charAt(i3))));
								surfaceRD.getRoadSurfCondition().add(new QCElement<Integer>(defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							}
							// 2019-5-22 加入格式教研
							if(RS[2].length() == 5)
								surfaceRD.setSnowThickness(new QCElement<Float>(getFloat(RS[2], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setSnowThickness(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(RS[3].length() == 4)
								surfaceRD.setWaterThickness(new QCElement<Float>(getFloat(RS[3], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setWaterThickness(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(RS[4].length() == 4)
								surfaceRD.setIceThickness(new QCElement<Float>(getFloat(RS[4], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setIceThickness(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(RS[5].length() == 4)
								surfaceRD.setFreezingPointTemperature(new QCElement<Float>(getFloat(RS[5], base, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setFreezingPointTemperature(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							
							if(RS[6].length() == 3)
								surfaceRD.setConcentrationOfSnowMeltAgent(new QCElement<Float>(getFloat(RS[6], base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else
								surfaceRD.setConcentrationOfSnowMeltAgent(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Road condition data or its quality control code error!");
							re.setSegment(contents[6]+ " \n" + QCDevice[6] + " "+QCProv[6] +" " + QCCountry[6]);
							parseResult.put(re);
//							continue;
							surfaceRD.setRoadSurfCondition(new ArrayList<QCElement<Integer>>());
							surfaceRD.getRoadSurfCondition().add(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.getRoadSurfCondition().add(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.getRoadSurfCondition().add(new QCElement<Integer>(defaultInt1, 9,9,9));
							surfaceRD.setSnowThickness(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setWaterThickness(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setIceThickness(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setFreezingPointTemperature(new QCElement<Float>((float)defaultInt1, 9,9,9));
							surfaceRD.setConcentrationOfSnowMeltAgent(new QCElement<Float>((float)defaultInt1, 9,9,9));
						}
						// 天气现象数据(WW) contents[7]
						String WW[] = contents[7].split("\\s+");
						if(WW != null && WW.length >= 2 && WW[0].trim().compareTo("WW") == 0 && WW[1].trim().length() >= 12 
								&& QCDevice[7].trim().length() >= 1 && QCProv[7].trim().length() >= 1 && QCCountry[7].trim().length() >= 1){
							String qcDevice = QCDevice[7].trim();
							String qcProv = QCProv[7].trim();
							String qcCountry = QCCountry[7].trim();
							surfaceRD.setWeatherPhenomenonCode(new ArrayList<QCElement<Integer>>());
							WW[1] = WW[1].trim();
							for(int i = 0; i < 6; i ++)
								surfaceRD.getWeatherPhenomenonCode().add(new QCElement<Integer>(
										getInt(WW[1].substring(i * 2, i * 2 + 2)), 
										Character.getNumericValue(qcDevice.charAt(0)), 
										Character.getNumericValue(qcProv.charAt(0)), 
										Character.getNumericValue(qcCountry.charAt(0))));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Weather phenomenon data or its quality control code error!");
							re.setSegment(contents[7]+ " \n" + QCDevice[7] + " "+QCProv[7] +" " + QCCountry[7]);
							parseResult.put(re);
//							continue;
							surfaceRD.setWeatherPhenomenonCode(new ArrayList<QCElement<Integer>>());
							WW[1] = WW[1].trim();
							for(int i = 0; i < 6; i ++)
								surfaceRD.getWeatherPhenomenonCode().add(new QCElement<Integer>(defaultInt1, 9,9,9));
						}
						// 小时内逐分钟降水量数据(MR) contents[8]
						String MR[] = contents[8].split("\\s+");
						if(MR != null && MR.length >= 2 && MR[0].trim().compareTo("MR") == 0 && (MR[1].trim().length() >= 120)
								&& QCDevice[8].trim().length() >= 60 && QCProv[8].trim().length() >= 60 && QCCountry[8].trim().length() >= 60){
							int i1 = 0, i2 = 0, i3 = 0;
							String qcDevice = QCDevice[8].trim();
							String qcProv = QCProv[8].trim();
							String qcCountry = QCCountry[8].trim();
							String tmp = "";
							surfaceRD.setMinutelyPrecipitation(new ArrayList<QCElement<Float>>());
							MR[1] = MR[1].trim();
							if(MR[1].trim().length() > 120)
								if(MR[1].startsWith("-"))
									for(int i = 0; i < 60; i ++)
										surfaceRD.getMinutelyPrecipitation().add(new QCElement<Float>((float)defaultInt2, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
								else
									for(int i = 0; i < 60; i ++)
										surfaceRD.getMinutelyPrecipitation().add(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							else{
								for(int i = 0; i < 60; i ++){
									tmp = MR[1].substring(i * 2, i * 2 + 2);
									if(tmp.compareTo(",,") == 0)
										surfaceRD.getMinutelyPrecipitation().add(new QCElement<Float>((float)tinyInt, Character.getNumericValue(qcDevice.charAt(i1++)),Character.getNumericValue (qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
									else if (tmp.compareTo("99") == 0) {
										surfaceRD.getMinutelyPrecipitation().add(new QCElement<Float>((float)99, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
									}
									else
										surfaceRD.getMinutelyPrecipitation().add(new QCElement<Float>(getFloat(tmp, base2, factor), Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
								}
							}
						}
						else if(MR != null && MR.length == 2 && MR[0].trim().compareTo("MR") == 0 && (MR[1].trim().length() == 60)
								&& appearNumber(MR[1].trim(), "-") == 60 && QCDevice[8].trim().length() >= 60 && QCProv[8].trim().length() >= 60 && QCCountry[8].trim().length() >= 60){
							// MR 字段全为------， 长度60
							int i1 = 0, i2 = 0, i3 = 0;
							String qcDevice = QCDevice[8].trim();
							String qcProv = QCProv[8].trim();
							String qcCountry = QCCountry[8].trim();
							surfaceRD.setMinutelyPrecipitation(new ArrayList<QCElement<Float>>());
							MR[1] = MR[1].trim();
							for(int i = 0; i < 60; i ++){
//								surfaceRD.getMinutelyPrecipitation().add(new QCElement<Float>((float)defaultInt2, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
								surfaceRD.getMinutelyPrecipitation().add(new QCElement<Float>((float)defaultInt1, Character.getNumericValue(qcDevice.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
								
							}
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Hour by minute precipitation data or its quality control code error!");
							re.setSegment(contents[8]+" \n" + QCDevice[8] + " "+QCProv[8] +" " + QCCountry[8]);
							parseResult.put(re);
//							continue;
							surfaceRD.setMinutelyPrecipitation(new ArrayList<QCElement<Float>>());
							for(int i = 0; i < 60; i ++)
								surfaceRD.getMinutelyPrecipitation().add(new QCElement<Float>((float)defaultInt1, 9, 9, 9));
						
						}
						parseResult.put(surfaceRD);
						parseResult.setSuccess(true);
					} 
					else{
						ReportError re = new ReportError();
						re.setMessage("Data quality control Code error!");
						re.setSegment(contents[10] + " "+ contents[11] + " " + contents[12]);
						parseResult.put(re);
						continue;
					}
				}
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			} catch (Exception e) {
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
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}

	/**
	 * 浮点值解析
	 * @param  str 待解析字符串
	 * @param  base 基值
	 * @param  factor 缩放因子    
	 * @return: float 解析结果  
	 */
	public static float getFloat(String str, int base, int factor){
		str = str.trim();
		if(str.contains("/") || (str.contains("-") && HasDigit(str)) ){
			return defaultInt1;
		}
		else if(str.contains("-"))
			return defaultInt2;
		else{ 
			try{
				if(base > 0)
					return (base - Float.parseFloat(str)) / factor;
				else
					return Float.parseFloat(str) / factor;
			}catch (Exception e) {
				return defaultInt1;
			}
		}
	}
	
	/** 
	 * 判断一个字符串是否含有数字
	 */
	public static boolean HasDigit(String content) {
	    boolean flag = false;
	    Pattern p = Pattern.compile(".*\\d+.*");
	    Matcher m = p.matcher(content);
	    if (m.matches()) {
	        flag = true;
	    }
	    return flag;
	}
	/**
	 * 解析得到测站海拔高度
	 * @param  str 待解析字符串     
	 * @return: double   以海平面为基准的高度
	 */
	public static double getHeightAboveSeaLevel(String str){
		str = str.trim();
		if(str.contains("/")){
			return defaultInt1;
		}
		else if(str.contains("-") && str.lastIndexOf("-") == str.length() - 1) //同时考虑对于海拔高度（占5Bytes）为负数值的情况
			return defaultInt2;
		else 
			return Float.parseFloat(str) * 0.1;
	}
	
	/**
	 * 整型值解析
	 * @param str 待解析字符串    
	 * @return: int  整型值  
	 */
	public static int getInt(String str){
		str = str.trim();
		if(str.contains("/")){
			return defaultInt1;
		}
		else if(str.contains("-"))
			return defaultInt2;
		else 
			return Integer.parseInt(str);
	}
	/**
	 * 解析风向值
	 * @param  str 待解析字符串   
	 * @return: int   风向值
	 */
	public static int getWindDirection(String str){
		str = str.trim();
		if(str.contains("/")){
			return defaultInt1;
		}
		else if(str.contains("-"))
			return defaultInt2;
		else if(str.equals("PPC"))
			return staticWind;
		else {
			try{
				int dir = Integer.parseInt(str);
				if(dir <= 360 && dir >= -180)
					return dir;
				else 
					return defaultInt1;
			}
			catch (Exception e) {
				return defaultInt1;
			}
		}
	}
	/**
	 * 获取指定字符串出现的次数
	 * @param srcText 源字符串
	 * @param findText 要查找的字符串
	 * @return int  指定字符串出现的次数
	 */
	public static int appearNumber(String srcText, String findText) {
	    int count = 0;
	    Pattern p = Pattern.compile(findText);
	    Matcher m = p.matcher(srcText);
	    while (m.find()) {
	        count++;
	    }
	    return count;
	}
	
	static List<File> filelist = new ArrayList<>();
	public static List<File> getFileList(String strPath){
		File dir = new File(strPath);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) { // 判断是文件还是文件夹
                    getFileList(files[i].getAbsolutePath()); // 获取文件绝对路径
                } else { // 判断文件名是否以.avi结尾
                    String strFileName = files[i].getAbsolutePath();
//                    System.out.println("---" + strFileName);
                    filelist.add(files[i]);
                } 
            }

        }
        return filelist;
	}
	
	/**
	 *测试 
	 * @param args void      
	 */
	public static void main(String[] args) {
		
//		double a = getFloat("-100-" , 0, 1);
		
//		List<File> files = getFileList("D:\\HUAXIN\\DataProcess\\A.0001.0027.R001\\Z_SURF_I_V0002-REG_20190408020000_O_AWS-RD_FTM.txt");
		File file = new File("D:\\HUAXIN\\DataProcess\\A.0001.0027.R001\\Z_SURF_I_V0002-REG_20190408020000_O_AWS-RD_FTM.txt");
//		System.out.println(files.size());
		DecodeRD decodeRD = new DecodeRD();
//		for (File file : files) {
			ParseResult<SurfaceRD> parseResult = decodeRD.DecodeFile(file);
			@SuppressWarnings("rawtypes")
			List<ReportInfo> list = parseResult.getReports();
			System.out.println(parseResult.isSuccess());
			System.out.println(list.size());
//		}
//		System.out.println(files.get(0).getAbsolutePath());
	}
}
