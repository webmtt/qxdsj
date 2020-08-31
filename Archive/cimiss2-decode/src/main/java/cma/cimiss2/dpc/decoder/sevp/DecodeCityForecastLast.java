package cma.cimiss2.dpc.decoder.sevp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.sevp.CityForeList;
import cma.cimiss2.dpc.decoder.bean.sevp.CityForecastLast;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Latest City Forecast data<br> 
 * 全国城镇预报指导预报产品	大数据平台	M.0012.0004.R001
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.sevp.CityForecastLast}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * ZCZC<br>
FSC150 BABJ 071200<br>
2018060712时中央台指导产品<br>
SCMOC  2018060712<br>
2599<br>
P5599   112.9    28.2    44.9 28 21<br>
3   24.4   95.6   99.0    1.5   -0.2    1.7  100.0   11.8    8.0  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9<br>
6   24.2   96.9   32.3    1.2   -1.8    0.3  100.0    9.2    7.0  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9<br>
9   24.0   97.8   14.6    0.9   -2.3    0.4  100.0    9.1    7.0  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9  999.9<br>
......<br>
 * 
 * <strong>code:</strong><br>
 * DecodeCityForecastLast decodeCityForecastLast = new DecodeCityForecastLast();<br>
 * ParseResult<CityForecastLast> parseResult = decodeCityForecastLast.DecodeFile(new File(String filepath)));<br>
 * List<CityForecastLast> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * List<CityForeList> cityForeLists = list.get(0).getEle();<br>
 * System.out.println(cityForeLists.size());<br>
 * 
 * <strong>output:</strong><br>
 * 2599<br>
 * 28<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午4:23:50   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeCityForecastLast{
	/**
	 * 解码结果封装
	 */
	private  ParseResult<CityForecastLast> parseResult = new ParseResult<CityForecastLast>(false);
	/**
	 * 城市预报最终预报报文解码函数   
	 * @param file 待解码文件
	 * @return ParseResult<CityForecastLast>      解码结果
	 */
	@SuppressWarnings("resource")
	public ParseResult<CityForecastLast> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {		
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			Scanner scanner = null;
			String fileN = file.getName();
			String [] filen = fileN.split("-");
			int forecast_max = 0;
			int interval_max = 0;
			int len = 0;
			if(filen != null && (len = filen.length) > 0 && filen[len - 1].length() >= 5){
				if(isDigital(filen[len - 1].substring(0, 5))){
					forecast_max = Integer.parseInt(filen[len - 1].substring(0, 3));//最大预报时效
					interval_max = Integer.parseInt(filen[len - 1].substring(3, 5));//最大预报间隔
				}
			}
			//获取文件流
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
				@SuppressWarnings("static-access")
				String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("NNNN"); //用NNNN分割报文
				String report = "";	
				while (scanner.hasNext() && (report = scanner.next()).length()> 5) {
					java.util.Date forecastime = null;
					java.util.Date observetime = null;
					int count = 5;//针对每一段分割报文从第五行（站号、经纬度开始解析要素）
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String[] reports = report.split("\n");		
					if(reports.length < 7) {
						ReportError re = new ReportError();
						re.setMessage("Report length error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}		
					//从开始由带站号经纬度的行开始逐行遍历。以报文的长度结束
					for(; count < reports.length; count++){
						CityForecastLast cityForecastlast = new CityForecastLast();
						cityForecastlast.setValidtime_Max(forecast_max);
						cityForecastlast.setValidtime_Max_Intr(interval_max);
						String correctsign = null;
						reports[1] = reports[1].trim();
						reports[3] = reports[3].trim();
						reports[count] = reports[count].trim();
						String [] rep_list1 = reports[1].split("\\s+");//报头第二行，带（FSCI50 CCCC YYGG （BBB））的行
						String [] rep_list2 = reports[3].split("\\s+");//报头第四行，带（产品代码、年月日时（世界时））的行
						String [] rep_list3 = reports[count].split("\\s+");//首个带有站号，经纬度的行
						try {	
							reports[0] = reports[0].trim();
							//2.第二行进行判断
							if (rep_list1.length == 3) {
								cityForecastlast.setBul_Center(rep_list1[1]);//编报中心
								correctsign="000";//设置更正标志
							}else if (rep_list1.length == 4) {
								cityForecastlast.setBul_Center(rep_list1[1]);//编报中心
								cityForecastlast.setCorrectSign(rep_list1[3]);//更正标志	
								correctsign = rep_list1[3];//设置更正标志
							}else {
								ReportError re = new ReportError();
								re.setMessage("The second line error!");
								re.setSegment(report);
								parseResult.put(re);
								//continue;	
								break;
							}
							//处理时间
							if (rep_list1[2].length() == 6 && rep_list2[1].length() == 10) {
							    List<Date> time;
							    try {
							    	time = ForecastUtil.time_transform(rep_list1[2],rep_list2[1]);
								} catch (Exception e) {
									ReportError re = new ReportError();
									re.setMessage("DataTime error!");
									re.setSegment(report);
									parseResult.put(re);
									continue;	
								}
								
							    forecastime = time.get(0);
							    observetime = time.get(1);
							    cityForecastlast.setForecastTime(observetime);//预报时间
							    cityForecastlast.setObservationTime(forecastime);//观测时间
							    if(!TimeCheckUtil.checkTime(forecastime)){
									ReportError re = new ReportError();
									re.setMessage("DataTime out of range：time:"+forecastime);
									re.setSegment(report);
									parseResult.put(re);
									break;
								}
							}else{
								ReportError re = new ReportError();
								re.setMessage("Report header time error!");
								re.setSegment(report);
								parseResult.put(re);
								break;		
							}
							//第三行进行判断
							reports[2] = reports[2].trim();
							if(reports[2].split("\\s+").length == 1){
								cityForecastlast.setPROD_DESC(reports[2]);//产品描述
							}else{
								ReportError re = new ReportError();
								re.setMessage("Header information length error!");
								re.setSegment(report);
								parseResult.put(re);
								break;	
							}
							//第四行进行判断		
							if(reports[3].split("\\s+").length == 2){
								cityForecastlast.setProd_Code(rep_list2[0]);//产品代码
							}else{
								ReportError re = new ReportError();
								re.setMessage("The fourth line error!");
								re.setSegment(report);
								parseResult.put(re);
								break;	
							}	
							reports[4] = reports[4].trim();
							if(reports[4].split("\\s+").length != 1){
								ReportError re = new ReportError();
								re.setMessage("The fifth line error!");
								re.setSegment(report);
								parseResult.put(re);
								break;
							}				
						} catch (NumberFormatException e) {
							ReportError re = new ReportError();
							re.setMessage("Digit error!");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}			
						String[] split = new String[10000];
					    int length = rep_list3.length;//判断带有站号、经纬度的行的长度
						if(length == 6){
							try {
								AgmeReportHeader agmeHeader = new AgmeReportHeader();
								agmeHeader.setStationNumberChina(rep_list3[0]);//站号
								agmeHeader.setLongitude(ForecastUtil.ToBeValidDouble(rep_list3[1]));//经度
								agmeHeader.setLatitude(ForecastUtil.ToBeValidDouble(rep_list3[2]));//纬度
								agmeHeader.setHeightOfSationGroundAboveMeanSeaLevel(ForecastUtil.ToBeValidDouble(rep_list3[3]));//海拔高度
								agmeHeader.setReport_time(observetime);//观测时间
								agmeHeader.setForecast_time(forecastime);//预报时间
								agmeHeader.setCropType(rep_list2[0]);//报告类别
								agmeHeader.setCorrectsign(correctsign);//更正标志
								agmeHeader.setBul_center(rep_list1[1]);//编报中心
								agmeHeader.setTTAAii(rep_list1[0]);//TTAAii
								//调用保存报文的方法  
								String savereport = ForecastUtil.savereport(count, Integer.parseInt(rep_list3[4]),reports);
								parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader,savereport.toString()));
								parseResult.setSuccess(true);
								cityForecastlast.setStationNumberChina(rep_list3[0]);//站号
								cityForecastlast.setLongitude(ForecastUtil.ToBeValidDouble(rep_list3[1]));//经度
								cityForecastlast.setLatitude(ForecastUtil.ToBeValidDouble(rep_list3[2]));//纬度
								cityForecastlast.setHeightOfSationGroundAboveMeanSeaLevel(ForecastUtil.ToBeValidDouble(rep_list3[3]));//海拔高度
								cityForecastlast.setValidtime_Count(ForecastUtil.ToBeValidDouble(rep_list3[4]));//预报时效个数
								cityForecastlast.setV_PROD_NUM(ForecastUtil.ToBeValidDouble(rep_list3[5]));//预报产品个数	
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digit error!");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							} 
						}else{
							ReportError re = new ReportError();
							re.setMessage("Report header error!");
							re.setSegment(report);
							parseResult.put(re);
							continue;			
						}
						//遍历预报时效个数
						int Validtime_Count = Integer.parseInt(rep_list3[4]);
						int j = 1;				
						for( ; j <= Validtime_Count; j++){
							reports[count + j] = reports[count + j].trim();
							split = reports[count + j].split("\\s+");						
							if(split.length == 22){
								try {
									CityForeList list = new CityForeList();
									list.setValidtime(ForecastUtil.ToBeValidDouble(split[0]));//预报时效
									list.setTEM(ForecastUtil.ToBeValidDouble(split[1]));//温度/气温
									list.setRHU(ForecastUtil.ToBeValidDouble(split[2]));//相对湿度
									list.setWIN_D(ForecastUtil.ToBeValidDouble(split[3]));//风向
									list.setWIN_S(ForecastUtil.ToBeValidDouble(split[4]));//风速
									list.setPRS(ForecastUtil.ToBeValidDouble(split[5]));//气压
									list.setPRE_PRE_Fore(ForecastUtil.ToBeValidDouble(split[6]));//可降水分（预报降水量）
									list.setCLO_Cov(ForecastUtil.ToBeValidDouble(split[7]));//总云量
									list.setCLO_Cov_Low(ForecastUtil.ToBeValidDouble(split[8]));//低云量
									list.setWEP(ForecastUtil.ToBeValidDouble(split[9]));//天气现象
									list.setVIS(ForecastUtil.ToBeValidDouble(split[10]));//水平能见度(人工)
									list.setTEM_Max_24h(ForecastUtil.ToBeValidDouble(split[11]));//未来24小时最高气温
									list.setTEM_Min_24h(ForecastUtil.ToBeValidDouble(split[12]));//未来24小时最低气温
									list.setRHU_Max_24h(ForecastUtil.ToBeValidDouble(split[13]));//24小时最大相对湿度
									list.setRHU_Min_24h(ForecastUtil.ToBeValidDouble(split[14]));//24小时最小相对湿度
									list.setPRE_24h(ForecastUtil.ToBeValidDouble(split[15]));//未来24小时降水量
									list.setPRE_12h(ForecastUtil.ToBeValidDouble(split[16]));//未来12小时降水量
									list.setCLO_Cov_12h(ForecastUtil.ToBeValidDouble(split[17]));//12小时内总云量
									list.setCLO_Cov_Low_12h(ForecastUtil.ToBeValidDouble(split[18]));//12小时内低云量
									list.setWEP_Past_12h(ForecastUtil.ToBeValidDouble(split[19]));//12小时内天气现象
									list.setWIN_PD_12h(ForecastUtil.ToBeValidDouble(split[20]));//12小时内盛行风向
									list.setWIN_S_Max_12h(ForecastUtil.ToBeValidDouble(split[21]));//12小时内最大风速	
									cityForecastlast.put(list);
								} catch (NumberFormatException e) {
									ReportError re = new ReportError();
									re.setMessage("Digit error!");
									re.setSegment(report);
									parseResult.put(re);
									continue;
								} 
							}else{
								ReportError re = new ReportError();
								re.setMessage("Number of fields error!");
								re.setSegment(report);
								parseResult.put(re);	
								continue;								
							}			
						}//for循环结束
						parseResult.put(cityForecastlast);
						parseResult.setSuccess(true);
						count += Validtime_Count;
					}
				}	
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}catch (Exception e) {
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
		}else{
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);	
		}		
		return parseResult;
	}
	/**
	 * 判断字符串是否均为数字组成  
	 * @param str 待判断字符串     
	 * @return: boolean      true 或者false
	 */
	boolean isDigital(String str){
		int i = 0;
		for(i = 0; i < str.length(); i ++){
			if(str.charAt(i) > '9' || str.charAt(i) < '0')
				break;
		}
		if(i == str.length())
			return true;
		else return false;
	}
	
	public static void main(String[] args) {
		DecodeCityForecastLast decodeCityForecastLast = new DecodeCityForecastLast();
		ParseResult<CityForecastLast> parseResult = decodeCityForecastLast.DecodeFile(new File
				("D:\\TEMP\\M.12.4.1\\7-16\\Z_SEVP_C_BABJ_20190605211036_P_RFFC-SNWFD-EME-ACHN-L88-P9-201906060600-07212.TXT"));
		List<CityForecastLast> list = parseResult.getData();
		System.out.println(list.size());
		List<CityForeList> cityForeLists = list.get(0).getEle();
		System.out.println(cityForeLists.size());
		
	}
}