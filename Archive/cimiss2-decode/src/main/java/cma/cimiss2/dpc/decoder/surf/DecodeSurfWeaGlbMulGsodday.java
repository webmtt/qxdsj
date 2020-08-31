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
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.surf.SurfWeaGlbMulGsoddayData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.UncompressFileGZIP;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 	Main Class of decode the NCDC-GSOD data <br>
 *	NCDC-GSOD全球地面日值资料解码类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.surf.SurfWeaGlbMulGsoddayData}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * STN--- WBAN   YEARMODA    TEMP       DEWP      SLP        STP       VISIB      WDSP     MXSPD   GUST    MAX     MIN   PRCP   SNDP   FRSHTT<br>
999999 00480  20180102    31.0 12    19.8 12   992.7 12   883.5 12   10.0 12    4.3 12    7.0  999.9    42.1*   14.0*  0.00I 999.9  000000<br>
999999 00480  20180103    17.4 13    12.9 13   991.4 13   882.2 13    7.6 13    0.6 13    5.1  999.9    27.0*   12.0*  0.00I 999.9  000000<br>
999999 00480  20180105    14.2  5  9999.9  0   985.9  5   876.7  5    6.8  5    0.0  5    6.0  999.9    25.0*    1.0*  0.00I 999.9  000000<br>

 * <strong>code:</strong><br>
 * 	DecodeSurfWeaGlbMulGsodday ds=new DecodeSurfWeaGlbMulGsodday();<br>
 * 	File file =new File(New file(String filepath) ;
 *  List<SurfWeaGlbMulGsoddayData> list = ds.DecodeFile(file).getData();<br>
 * 	System.out.println(list.get(0).getDailyAvgTemperature());<br>
 * 
 * <strong>output:</strong><br>
 * -0.5555555555555556<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午1:34:27   dengyongliang    Initial creation.
 * </pre>
 * 
 * @author dengyongliang
 * @version 0.0.1
 */
public class DecodeSurfWeaGlbMulGsodday {
	/**
	 * 存放数据解析的结果集 
	 */
	private ParseResult<SurfWeaGlbMulGsoddayData> parseResult = new ParseResult<SurfWeaGlbMulGsoddayData>(false);
	/**
	 * 温度值标识
	 */
	private static final String TEMP= "TEMP";
	/**
	 * 气压值标识
	 */
	private static final String PRESS= "PRESS";
	/**
	 * 能见度标识
	 */
	private static final String VISI = "VISI";
	/**
	 * 风速标识
	 */
	private static final String WIND="WIND";
	/**
	 * 雪深度值标识
	 */
	private static final String SNOW ="SNOW";
	/**
	 * 降水量标识
	 */
	private static final String RAIN="RAIN";
	/**
	 * 解码函数
	 * 
	 * @param file
	 *            文件对象
	 * 
	 * @return ParseResult<PrecipitationObservationDataReg> 解码结果封装
	 */
	public ParseResult<SurfWeaGlbMulGsoddayData> DecodeFile(String  filename) {
		//因为是gz压缩文件，需要先解压
		filename=UncompressFileGZIP.doUncompressFile(filename);
		File file=new File(filename);
		BufferedReader bufferedReader = null;
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
				int flag=0;//第一行跳过
				// 循环读取文件的行d
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if(flag==0){
						flag=flag+1;
						continue;//第一行跳过
					}
					// 判断文件是否正常结束
					if (lineTxt.length()==138) {
						String[] items = lineTxt.split("\\s+");
						// 判断文件要素完整
						if(items.length == 22) {
							SurfWeaGlbMulGsoddayData surfWeaGlbMulGsoddayData = new SurfWeaGlbMulGsoddayData();
							try {
								ArrayList<String> surfList=new ArrayList<String>();
//								String datatTime= staMap.get(items[0]+"—"+items[1]);
								//3.判断是不是是最新的日期，如果是最新的数据才解码入库
//								if(null!=datatTime&&Integer.valueOf(items[2])<=Integer.valueOf(datatTime)){
//									continue;
//								}
								surfList.add(items[0]);
								surfList.add(items[1]);
								surfList.add(items[2]);
								SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
							    Date dataTime=simpleDateFormat.parse(items[2]);
//								if(!TimeCheckUtil.checkTime(dataTime)){
//									ReportError re = new ReportError();
//									re.setMessage("DataTime out of range：time:"+dataTime);
//									re.setSegment(lineTxt);
//									parseResult.put(re);
//									continue;
//								}
								surfWeaGlbMulGsoddayData.setStationNumberLocation(items[0]);//原始STN站号
								surfWeaGlbMulGsoddayData.setWeatherBureauAirForceNavy(items[1]);//原始WBAN站号
								surfWeaGlbMulGsoddayData.setYearMoDa(items[2]);//年月日						
								surfWeaGlbMulGsoddayData.setDailyAvgTemperature(ToBeValidDouble(items[3],TEMP));//日平均气温 （华氏度）
								surfWeaGlbMulGsoddayData.setDailyAvgTemperatureCount(Integer.parseInt(items[4]));//个数
								surfWeaGlbMulGsoddayData.setDewPointTemperature(ToBeValidDouble(items[5],TEMP));//日平均露点温度
								surfWeaGlbMulGsoddayData.setDewPointTemperatureCount(Integer.parseInt(items[6]));//日露点温度记录数
								surfWeaGlbMulGsoddayData.setSeaLevelPressure(ToBeValidDouble(items[7],PRESS));//日平均海平面气压（毫巴）
								surfWeaGlbMulGsoddayData.setSeaLevelPressureCount(Integer.parseInt(items[8]));//日海平面气压记录数
								surfWeaGlbMulGsoddayData.setStationPressure(ToBeValidDouble(items[9],PRESS));//日平均本站气压
								surfWeaGlbMulGsoddayData.setStationPressureCount(Integer.parseInt(items[10]));//日本站气压记录数
								surfWeaGlbMulGsoddayData.setVisibility(ToBeValidDouble(items[11],VISI));//日平均能见度
								surfWeaGlbMulGsoddayData.setVisibilityCount(Integer.parseInt(items[12]));//日能见度记录数
								surfWeaGlbMulGsoddayData.setWindSpeed(ToBeValidDouble(items[13],WIND)); // 日平均风速
								surfWeaGlbMulGsoddayData.setWindSpeedCount(Integer.parseInt(items[14]));//日风速记录数
								surfWeaGlbMulGsoddayData.setMaxSustainedWindSpeed(ToBeValidDouble(items[15],WIND));//日最大持续风速
								surfWeaGlbMulGsoddayData.setMaxWindGust(ToBeValidDouble(items[16],WIND));//日最大阵风风速
								String  maxTem= items[17];
								if(maxTem.contains("*")){
									surfWeaGlbMulGsoddayData.setMaxTemperature(ToBeValidDouble(items[17].substring(0, maxTem.length()-1),TEMP));//日最高气温
									surfWeaGlbMulGsoddayData.setMaxFlag("*");//日最高气温取值标志
								}else{
									surfWeaGlbMulGsoddayData.setMaxTemperature(ToBeValidDouble(maxTem,TEMP));//日最高气温
								}
								String  minTem= items[18];
								if(null!=minTem&&minTem.contains("*")){
									surfWeaGlbMulGsoddayData.setMinTemperature(ToBeValidDouble(items[18].substring(0, minTem.length()-1),TEMP));//日最低气温
									surfWeaGlbMulGsoddayData.setMinFlag("*");//日最低气温取值标志
								}else{
									surfWeaGlbMulGsoddayData.setMinTemperature(ToBeValidDouble(minTem,TEMP));//日最低气温
								}
								String precipitation=items[19];
							
								if(null!=precipitation&&precipitation.substring(precipitation.length()-1, precipitation.length()).compareTo("A")>=0){
									surfWeaGlbMulGsoddayData.setPrecipitationDaily(ToBeValidDouble(precipitation.substring(0, precipitation.length()-1),RAIN));//日降水量 (英寸---》毫米)
									surfWeaGlbMulGsoddayData.setPrecipitationDailyFlag(precipitation.substring(precipitation.length()-1, precipitation.length()));
								}else{
									surfWeaGlbMulGsoddayData.setPrecipitationDaily(ToBeValidDouble(precipitation,RAIN));//日降水量 (英寸---》毫米)
								}
								
								
								surfWeaGlbMulGsoddayData.setSnowDepth(ToBeValidDouble(items[20],SNOW));//日(总)雪深
								surfWeaGlbMulGsoddayData.setFRSHTT(Integer.parseInt(items[21]));

								if(null!=items[21]&&items[21].length()==6){
									String[] frshtt=items[21].split("");
									surfWeaGlbMulGsoddayData.setFog(Integer.parseInt(frshtt[0]));
									surfWeaGlbMulGsoddayData.setRain(Integer.parseInt(frshtt[1]));
									surfWeaGlbMulGsoddayData.setSnow(Integer.parseInt(frshtt[2]));
									surfWeaGlbMulGsoddayData.setHail(Integer.parseInt(frshtt[3]));
									surfWeaGlbMulGsoddayData.setThunder(Integer.parseInt(frshtt[4]));
									surfWeaGlbMulGsoddayData.setTornado(Integer.parseInt(frshtt[5]));
								}	
						    parseResult.put(new ReportInfo(surfList, lineTxt));
							parseResult.put(surfWeaGlbMulGsoddayData);
//							System.out.println(parseResult.getData().size());
								parseResult.setSuccess(true);

							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digit parse error!");
								re.setSegment(lineTxt);
								parseResult.put(re);
								continue;
							}
						}else {  // 报文头信息错误
							ReportError reportError = new ReportError();
							reportError.setMessage("Number of segments error!");
							reportError.setSegment(lineTxt);
							parseResult.put(reportError);
						}
						
					} else {
						try {
						    String[] items = lineTxt.split("\\s+");
						    ArrayList<String> surfList = new ArrayList<String>();
//						    String datatTime = staMap.get(items[0]+"—"+items[1]);
//						  //3.判断是不是是最新的日期，如果是最新的数据才解码入库
//						   if(null != datatTime && Integer.valueOf(items[2]) <= Integer.valueOf(datatTime)){
//							  continue;
//						    }
						   surfList.add(items[0]);
						   surfList.add(items[1]);
						   surfList.add(items[2]);
						 parseResult.put(new ReportInfo(surfList, lineTxt));
						} catch (Exception e) {
							ReportError re = new ReportError();
							re.setMessage("DataTime format error!");
							re.setSegment(lineTxt);
							parseResult.put(re);
						} 
						// 报文中记录没有正常结束
						ReportError re = new ReportError();
						re.setMessage("Report format error!");
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
			} catch (Exception e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);

			} finally {
				try {
					if(bufferedReader != null){
						bufferedReader.close();
					}
					if (read != null) {
						read.close();
					}
				} catch (IOException e) {
					parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
				}
			}
		} else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		//最后需要将解压出来的文件删掉
		file.delete();
		return parseResult;

	}
	/**
	 * 判断是否为缺测值，如果是转成999999
	 * @param str 待解析字符串
	 * @param flag 要素值标识（气温、气压、降水、风速等）
	 * @return 解析结果值
	 */
	public static double ToBeValidDouble(String str,String flag){
		if(null==str||str.equals("")){
			return 999999;
		}
		
		switch (flag) {
		case "TEMP":
			if(str.equals("9999.9"))
				return 999999;
			else {
				//摄氏度＝（华氏度－32）÷1.8
				return (Double.parseDouble(str)-32)/1.8;
		      }
		case "PRESS":
			if(str.equals("9999.9"))
				return 999999;
			else {
				return (Double.parseDouble(str));
			}
		case "VISI":
			if(str.equals("999.9"))
				return 999999;
			else {
				//1 英 里 ＝1760 码 ＝1.6093 千 米 
				return (Double.parseDouble(str));
			}
		case "RAIN":
			if(str.equals("99.99"))
				return 999999;
			else {
				//1英寸=25.4毫米
				return (Double.parseDouble(str)*25.4);
			}
		case "SNOW":
			if(str.equals("999.9"))
				return 999999;
			else {
				//1英寸=2.54厘米
				return (Double.parseDouble(str)*2.54);
			}
		case "WIND":
			if(str.equals("999.9"))
				return 999999;
			else {
				//knot 节 1节=1海里/小时=1852/3600米/秒=0.514444米/秒。
				return Double.parseDouble(str)*1852/3600;
			}
		default:
			return 999999;

		}
	}

	public static void main(String[] args) {
		DecodeSurfWeaGlbMulGsodday ds=new DecodeSurfWeaGlbMulGsodday();
		String file ="C:\\BaiduNetdiskDownload\\test\\A.0017.0001.R001\\W_NAFP_C_BABJ_20200508023210_P_027380-99999-2020.op.gz";
		ParseResult<SurfWeaGlbMulGsoddayData> result= ds.DecodeFile(file);
		List<SurfWeaGlbMulGsoddayData> list=result.getData();
		for(SurfWeaGlbMulGsoddayData data:list) {
			System.out.println(data.toString());
		}
	}

}
