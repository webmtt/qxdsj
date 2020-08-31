package cma.cimiss2.dpc.decoder.upar;

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
import java.util.Date;


import cma.cimiss2.dpc.decoder.bean.upar.LightingData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the ADTD radar locate data <br>
 * ADTD系统雷电定位数据解码类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.upar.LightingData}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br> 
 * 10146   2018-06-03   15:47:00.3759333   纬度=26.15946   经度=101.4626   强度=-23.65115   陡度=-6.306973   误差=0   定位方式:5   省:云南省   市:楚雄彝族自治州   县:永仁县<br> 
10147   2018-06-03   15:47:00.5512612   纬度=26.15865   经度=101.4627   强度=-25.20343   陡度=-7.868388   误差=0   定位方式:5   省:云南省   市:楚雄彝族自治州   县:永仁县<br> 
10148   2018-06-03   15:47:02.9572010   纬度=16.1301   经度=109.0179   强度=-51.73645   陡度=-16.55567   误差=0   定位方式:2   省:   市:   县:<br> 
10149   2018-06-03   15:47:03.9977330   纬度=31.09521   经度=101.7413   强度=-18.592   陡度=-5.534361   误差=0   定位方式:2   省:四川省   市:甘孜藏族自治州   县:丹巴县<br> 
10150   2018-06-03   15:47:04.5169024   纬度=53.52869   经度=125.0656   强度=-87.83617   陡度=-20.07684   误差=64.71865   定位方式:6   省:   市:   县:<br> 
10151   2018-06-03   15:47:04.5462383   纬度=53.53102   经度=125.0742   强度=-37.40854   陡度=-12.94133   误差=0   定位方式:5   省:   市:   县:<br> 
10152   2018-06-03   15:47:05.9969704   纬度=53.78299   经度=124.4683   强度=-29.37892   陡度=-10.44584   误差=0   定位方式:5   省:   市:   县:<br> 
......<br> 
 * <strong>code:</strong><br>
 * DecodeLDP decodeLDP = new DecodeLDP();<br> 
 * List<LightingData> list = decodeLDP.DecodeLPD(new File(String filepath)).getData();<br> 
 * System.out.println(list.get(0).getProvince() + " " + list.get(0).getCity());<br> 
   
 * <strong>output:</strong><br>
 * 云南省 楚雄彝族自治州<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 上午10:36:58   dengyongliang    Initial creation.
 * </pre>
 * 
 * @author dengyongliang
 * @version 0.0.1
 */
public class DecodeLDP {
	/**
	 *  存放数据解析的结果集 
	 */
	private ParseResult<LightingData> parseResult = new ParseResult<LightingData>(false);
	/**
	 * 读取文件流
	 */
	private BufferedReader bufferedReader = null;

	/**
	 * 函数名：DecodeLPD
	 * 
	 * @param file
	 *            文件对象
	 * 
	 * @return ParseResult<LightingData> 解码结果包裹类
	 */
	public ParseResult<LightingData> DecodeLPD(File file) {
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read=null;
			try {
				read = new InputStreamReader(new FileInputStream(file), "GBK");
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {

					String[] items = lineTxt.trim().split("\\s+");
					
					if (items.length > 11) {
						LightingData lightingData = new LightingData();

						try {

							lightingData.setLdpId(items[0]);

							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							// 观测日期 2017-11-15
							String[] hmsn = items[2].trim().split("[.]");
							String date_time = items[1].trim() + " " + hmsn[0];
							Date obsTime = simpleDateFormat.parse(date_time);
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(obsTime);
							calendar.add(Calendar.HOUR_OF_DAY, -8);
							lightingData.setObservationTime(calendar.getTime());
							if(!TimeCheckUtil.checkTime(calendar.getTime())){
								ReportError re = new ReportError();
								re.setMessage("Time out of range：time:"+calendar.getTime());
								re.setSegment(lineTxt);
								parseResult.put(re);
								continue;
							}
//							.setObservationDate(simpleDateFormat.parse(items[1]));
//							// 记录时间 08:57:16.5103260
//							lightingData.setObservationTime(items[2]);
							lightingData.setMillis(Integer.parseInt(hmsn[1]));
							lightingData.setLatitude(Double.parseDouble(DecodeLDP.getEqualValue(items[3])));
							lightingData.setLongitude(Double.parseDouble(DecodeLDP.getEqualValue(items[4])));
							lightingData.setIntensity(Double.parseDouble(DecodeLDP.getEqualValue(items[5])));
							lightingData.setSteepness(Double.parseDouble(DecodeLDP.getEqualValue(items[6])));
							lightingData.setErrorValue(Double.parseDouble(DecodeLDP.getEqualValue(items[7])));
							lightingData.setLocateMode(Integer.parseInt(DecodeLDP.getColonValue(items[8])));

							lightingData.setProvince(DecodeLDP.getColonValue(items[9]));
							lightingData.setCity(DecodeLDP.getColonValue(items[10]));
							lightingData.setCounty(DecodeLDP.getColonValue(items[11]));

							parseResult.put(lightingData);
							parseResult.setSuccess(true);

						} catch (NumberFormatException e) {
							ReportError re = new ReportError();
							re.setMessage("Digital conversion anomaly");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;
						} catch (ParseException e) {
							ReportError re = new ReportError();
							re.setMessage("Time format conversion exception");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;

						} catch (Exception e) {
							ReportError re = new ReportError();
							re.setMessage("extremely abnormal");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;

						}

					}
				}
				read.close();

			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
				e.printStackTrace();
			} catch (IOException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				e.printStackTrace();
			}catch(Exception e){
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				e.printStackTrace();
			}finally {
				try {
					if(read!=null){
						read.close();
					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if(bufferedReader!=null){
						bufferedReader.close();
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
	 * 取得字符串中":"后的子串   
	 * @param str   待解析字符串
	 * @return String      解析结果
	 */
	public static String getColonValue(String str) {
		int index = str.indexOf(":");
		String valStr = str.substring(index + 1, str.length());
		return valStr;
	}
	/**
	 * 取得字符串中"="的子串   
	 * @param str  待解析字符串
	 * @return String      解析结果
	 */
	public static String getEqualValue(String str) {
		int index = str.indexOf("=");
		String valStr = str.substring(index + 1, str.length());
		return valStr;
	}
	
//	public static void main(String[] args) {
//    	DecodeLDP decodeLDP = new DecodeLDP();
//    	List<LightingData> list = decodeLDP.DecodeLPD(new File("D:\\HUAXIN\\DataProcess\\B.0006.0004.R001Dst\\2018060308\\Z_P_LPD__C_BABJ_20180603080201_2018_06_03.txt")).getData();
//    	System.out.println(list.get(0).getProvince() + " " + list.get(0).getCity());
//    	
//    }
}
