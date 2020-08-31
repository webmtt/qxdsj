package cma.cimiss2.dpc.decoder.sevp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.QCElement;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.NegOxygenIon;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Negative oxygen ions data in tourist attractions <br>
 * 旅游景区负氧离子观测资料解码类
 *
 * <p>
 * notes:
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.sevp.NegOxygenIon}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
Q2933 20180612190000 312945 1095943 17520 0000 199<br>
NEO 000001 000001 0230 000001 0230<br>
AT 0096 0103 0210 0096 0300<br>
HM 098 098 0300 //// ////<br>
QC<br>
Q1 11111 11111 11111<br>
Q2 99999 99999 99999<br>
Q3 99999 99999 99999=<br>
NNNN<br>
 * <strong>code:</strong><br>
 * DecodeNegOxyIon decodeNegOxyIon = new DecodeNegOxyIon();<br>
 * ParseResult<NegOxygenIon> parseResult = decodeNegOxyIon.DecodeFile(new File(String filepath));<br>
 * List<NegOxygenIon> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).getStationNumberChina());<br>
 * <strong>output:</strong><br>
 * 1<br>
 * Q2933<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月29日 上午9:25:47   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeNegOxyIon{
	/**
	 *  存储解码结果
	 */
	private ParseResult<NegOxygenIon> parseResult = new ParseResult<NegOxygenIon>(false);
	/**
	 * 缺测时替代值
	 */
	private static int defaultInt1 = 999999; 
	/**
	 * 旅游景区负氧离子观测资料解码函数   
	 * @param file  待解码文件
	 * @return ParseResult<NegOxygenIon>     存储解码结果  
	 */
	@SuppressWarnings("resource")
	public ParseResult<NegOxygenIon> DecodeFile(File file){
		if(file != null && file.exists() && file.isFile()){
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			@SuppressWarnings("static-access")
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			InputStreamReader read = null;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Scanner scanner = null;
			try{
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("=");
				int lines = 0;
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
					lines = contents.length;
					for(int p = 0; p < lines; p ++)
						contents[p] = contents[p].trim();
					NegOxygenIon negOxygenIon = new NegOxygenIon();
					// 测站基本信息段 contents[0]
					String baseInfo[] =contents[0].split("\\s+");
					if(baseInfo != null && baseInfo.length == 7){
						negOxygenIon.setStationNumberChina(baseInfo[0].trim());
						try {
							negOxygenIon.setObservationTime(sdf.parse(baseInfo[1].trim()));
							if(!TimeCheckUtil.checkTime(sdf.parse(baseInfo[1].trim()))){
								ReportError re = new ReportError();
								re.setMessage("DataTime out of range：time:"+sdf.parse(baseInfo[1].trim()));
								re.setSegment(report);
								parseResult.put(re);
								continue;
							}
						}catch (ParseException e) {
							ReportError re = new ReportError();
							re.setMessage("Datatime format error!");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}
						// 纬度
						if(baseInfo[2].trim().contains("/"))
							negOxygenIon.setLatitude(defaultInt1);
						else
							negOxygenIon.setLatitude(ElementValUtil.getlatitude(baseInfo[2].trim()));
						// 经度
						if(baseInfo[3].trim().contains("/"))
							negOxygenIon.setLongtitude(defaultInt1);
						else
							negOxygenIon.setLongtitude(ElementValUtil.getLongitude(baseInfo[3].trim()));
						// 测站高度
						negOxygenIon.setHeight(getHeightAboveSeaLevel(baseInfo[4].trim()));
						// 测站地段标识
						if(baseInfo[5].contains("/"))
							negOxygenIon.setSiteMarkOfStation(defaultInt1);
						else
							negOxygenIon.setSiteMarkOfStation(Integer.parseInt(baseInfo[5].trim()));
						// 质量控制标识
						if(baseInfo[6].contains("/"))
							negOxygenIon.setQualityContorl(999);
						else negOxygenIon.setQualityContorl(Integer.parseInt(baseInfo[6].trim()));
						parseResult.put(new ReportInfo<NegOxygenIon>(negOxygenIon, report));
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("Station information error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					// 数据质量控制码（QC） contents[5~7]
					if(lines >= 6)
						contents[5] = contents[5].replaceAll("/", "9");
					if(lines >= 7)
						contents[6] =contents[6].replaceAll("/", "9");
					if(lines >= 8)
						contents[7] = contents[7].replaceAll("/", "9");
					String QCStation[] = contents[5].split("\\s+");
					String QCProv[] = contents[6].split("\\s+");
					String QCCountry[] = contents[7].split("\\s+");
					if(QCStation != null && QCStation.length == 4 && QCStation[0].trim().compareTo("Q1") == 0 &&
						QCProv != null && QCProv.length == 4 && QCProv[0].trim().compareTo("Q2") == 0 &&
						QCCountry != null && QCCountry.length == 4 && QCCountry[0].trim().compareTo("Q3") == 0){
						// 大气负氧离子数据  (NEO)
						String NEO[] = contents[1].split("\\s+");
						if(NEO != null && NEO.length == 6 && NEO[0].trim().compareTo("NEO") == 0 && QCStation[1].trim().length() == 5 && QCProv[1].trim().length() == 5 && QCCountry[1].trim().length() == 5){
							int i1 = 0, i2 = 0, i3 = 0;
							String qcStation = QCStation[1].trim();
							String qcProv = QCProv[1].trim();
							String qcCountry = QCCountry[1].trim();
							// 负氧离子浓度
							negOxygenIon.setNegOxyIonConcentration(new QCElement<Double>(getFloat(NEO[1], 1), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//最高负氧离子浓度
							negOxygenIon.setMaxNegOxyIonConcentration(new QCElement<Double>(getFloat(NEO[2], 1), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//最高负氧离子浓度出现时间
							negOxygenIon.setTimeOfMaxNegOxyIonConcentration(new QCElement<Integer>(getInt(NEO[3]), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//最低负氧离子浓度
							negOxygenIon.setMinNegOxyIonConcentration(new QCElement<Double>(getFloat(NEO[4], 1), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//最低负氧离子浓度出现时间
							negOxygenIon.setTimeOfMinNegOxyIonConcentration(new QCElement<Integer>(getInt(NEO[5]), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
						}else{
							ReportError re = new ReportError();
							re.setMessage("Negative oxygen ion data or its quality control code error!");
							re.setSegment(contents[1]+ " " + QCStation[1] + " "+QCProv[1] +" " + QCCountry[1]);
							parseResult.put(re);
						}
						// 气温数据(AT) contents[2]
						String AT[] = contents[2].split("\\s+");
						if(AT != null && AT.length == 6 && AT[0].trim().compareTo("AT") == 0 && QCStation[2].trim().length() == 5 && QCProv[2].trim().length() == 5 && QCCountry[2].trim().length() == 5){
							int i1 = 0, i2 = 0, i3 = 0;
							String qcStation = QCStation[2].trim();
							String qcProv = QCProv[2].trim();
							String qcCountry = QCCountry[2].trim();
							//气温
							negOxygenIon.setTemperature(new QCElement<Double>(getFloat(AT[1], 10), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							// 最高气温
							negOxygenIon.setMaxTemperature(new QCElement<Double>(getFloat(AT[2], 10), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//最高气温出现时间
							negOxygenIon.setTimeOfMaxTemperature(new QCElement<Integer>(getInt(AT[3]), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//最低气温
							negOxygenIon.setMinTemperature(new QCElement<Double>(getFloat(AT[4], 10), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//最低气温出现时间
							negOxygenIon.setTimeOfMinTemperature(new QCElement<Integer>(getInt(AT[5]), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("temperature data or its quality control code error!");
							re.setSegment(contents[2]+ " " + QCStation[2] + " "+QCProv[2] +" " + QCCountry[2]);
							parseResult.put(re);
						}
						//  湿度数据 （HM）
						String HM[] = contents[3].split("\\s+");
						if(HM != null && HM.length == 6 && HM[0].trim().compareTo("HM") == 0 && QCStation[3].trim().length() == 5 && QCProv[3].trim().length() == 5 && QCCountry[3].trim().length() == 5){
							int i1 = 0, i2 = 0, i3 = 0;
							String qcStation = QCStation[3].trim();
							String qcProv = QCProv[3].trim();
							String qcCountry = QCCountry[3].trim();
							//相对湿度
							negOxygenIon.setRelativeHumidity(new QCElement<Double>(getFloat(HM[1], 1), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							// 最大相对湿度
							negOxygenIon.setMaxRelativeHumidity(new QCElement<Double>(getFloat(HM[2], 1), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//最大相对湿度出现时间
							negOxygenIon.setTimeOfMaxRelativeHumidity(new QCElement<Integer>(getInt(HM[3]), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//露点温度
							negOxygenIon.setDewPoint(new QCElement<Double>(getFloat(HM[4], 10), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
							//水汽压
							negOxygenIon.setWaterVaporPressure(new QCElement<Double>(getFloat(HM[5], 10), Character.getNumericValue(qcStation.charAt(i1++)), Character.getNumericValue(qcProv.charAt(i2++)), Character.getNumericValue(qcCountry.charAt(i3++))));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Humidity data or its quality control code error!");
							re.setSegment(contents[3]+ " " + QCStation[3] + " "+QCProv[3] +" " + QCCountry[3]);
							parseResult.put(re);
						}
						parseResult.put(negOxygenIon);
						parseResult.setSuccess(true);
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("Data quality control Code error");
						re.setSegment(contents[5] + " "+ contents[6] + " " + contents[7]);
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
			} finally {
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
	 * @return float      解码结果
	 */
	public static double getFloat(String str, int factor){
		str = str.trim();
		if(str.contains("/")){
			return defaultInt1;
		}
		else 
			return Double.parseDouble(str) / factor;
	}
	/**
	 * 整型值解析   
	 * @param str 待解析字符串    
	 * @return int      解码结果
	 */
	public static int getInt(String str){
		str = str.trim();
		if(str.contains("/")){
			return defaultInt1;
		}
		else 
			return Integer.parseInt(str);
	}
	/**
	 * 解析得到测站海拔高度   
	 * @param  str 待解析字符串     
	 * @return double      解码结果
	 */
	public static double getHeightAboveSeaLevel(String str){
		str = str.trim();
		if(str.contains("/")){
			return defaultInt1;
		}
		else 
			return Float.parseFloat(str) * 0.1;
	}
	
	public static void main(String[] args) {
		DecodeNegOxyIon decodeNegOxyIon = new DecodeNegOxyIon();
		ParseResult<NegOxygenIon> parseResult = decodeNegOxyIon.DecodeFile(new File
				("C:\\Users\\cuihongyuan\\Desktop\\对比\\服务类 对比验证\\2轮\\省级紫外线\\201904\\2019040100\\Z_CAWN_C_BECS_20190401002032_O_AWS-NEO_FTM.txt"));
		List<NegOxygenIon> list = parseResult.getData();
		System.out.println(list.size());
		System.out.println(list.get(0).getStationNumberChina());
	}
}