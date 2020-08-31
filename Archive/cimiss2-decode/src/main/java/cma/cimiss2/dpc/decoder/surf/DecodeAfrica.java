package cma.cimiss2.dpc.decoder.surf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.TIMEOUT;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.surf.AfricaAWS;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>

 *
 * <p>
 * notes: 非洲援建站资料解码类
 * <ul>
 *   <li> 定义参考以下文档
 *    <ol>
 *      <li> <a href=" "> 《》 </a>
 *      <li> <a href=" "> 《》 </a>
 *    </ol>
 *   </li>
 * </ul>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2019年9月17日 上午9:39:51   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeAfrica {
	/**
	 * 解码结果
	 */
	private ParseResult<AfricaAWS> parseResult = new ParseResult<AfricaAWS>(false); 
	/**
	 * 日期时间格式
	 */
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private final static String DEFAULT_MISS_VALUE_STR = "999999";
	
	private final static int DEFAULT_MISS_VALUE_INT = 999999;
	private final static int STATIC_WIND = 999017;
	/**
	 * 云状转换
	 */
	static Map<String, Integer> map = null;
	/**
	 * @Title: DecodeFile   
	 * @Description: 报文解析方法
	 * @param file
	 * @return ParseResult<SurfaceObservationKppr>      
	 * @throws：
	 */
	public ParseResult<AfricaAWS> DecodeFile(File file){
		parseResult.getReports().clear();
		parseResult.getData().clear();
		parseResult.getError().clear();
		String encoding = "utf-8";
		InputStreamReader read = null;
		Scanner scanner = null;
		try{
			String bbb = "000";
			String[] segs = file.getName().split("-|_|\\.");
//			if(segs != null && segs.length == 11 && segs[9].toUpperCase().startsWith("CC")){
			if(segs != null && segs[segs.length - 2].toUpperCase().startsWith("CC")){
				bbb = segs[segs.length - 2];
			}
			read = new InputStreamReader(new FileInputStream(file), encoding);
			scanner = new Scanner(read).useDelimiter("=");
			cloudShape();
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
				// 报文长度 至少两行,最多四行
				if(contents.length >= 2){
					AfricaAWS surfAfricaModel = new AfricaAWS();
					surfAfricaModel.setCorrectSign(bbb);
					
//					区站号	5字节	5位数字或第1位为字母，第2-5位为数字
//					纬度	6字节	按度分秒记录，均为2位，高位不足补“0”，台站纬度未精确到秒时，秒固定记录“00”
//					经度	7字节	按度分秒记录，度为3位，分秒为2位，高位不足补“0”，台站经度未精确到秒时，秒固定记录“00”
//					观测场拔海高度	5字节	保留一位小数，扩大10倍记录，高位不足补“0”
//					气压传感器拔海高度	5字节	保留一位小数，扩大10倍记录，高位不足补“0”，无气压传感器时，录入“/////”
//					观测方式	1字节	固定存入4					 
					String header = contents[0].trim();
					String header_infos[] = header.split("\\s+");
					if(header_infos.length == 6) {
						int ret = parsePart1(header_infos, report, surfAfricaModel);
						if(ret < 0){
							ReportError re = new ReportError();
							re.setSegment(report);
							re.setMessage("Station info is error!");
							parseResult.put(re);
							continue; // 台站号有误
						}
						else{
							// do nothing
						}
					}else{
						ReportError re = new ReportError();
						re.setSegment(report);
						re.setMessage("Report format error, header elements number does not equal 6! ");
						parseResult.put(re);
						continue;
					}
					//第二段，第2条记录共52个要素值，每组用1个半角空格分隔
					String part2 = contents[1].trim();
					String part2_infos[] = part2.split("\\s+");
					if(part2_infos.length == 52){
						int ret = parsePart2(part2_infos, report, surfAfricaModel);
						if(ret < 0){
							ReportError re = new ReportError();
							re.setSegment(report);
							re.setMessage("Datetime is error! ");
							parseResult.put(re);
							continue; // 资料时间有误
						}else{					
//							try{
//								TimeCheckUtil.checkTime(surfAfricaModel.getObserDateTime());
//							}catch (Exception e) {
//								e.printStackTrace();
//							}
//							if(TimeCheckUtil.timeCheckUtil != null && 
//									!TimeCheckUtil.checkTime(surfAfricaModel.getObserDateTime())){
							if(!TimeCheckUtil.checkTime(surfAfricaModel.getObserDateTime())){
								ReportError re = new ReportError();
								re.setMessage("DataTime out of range：time:"+surfAfricaModel.getObserDateTime());
								parseResult.put(re);
								continue;
							}else{
//								System.out.print("timeCheckUtil: " + TimeCheckUtil.timeCheckUtil);
								parseResult.put(surfAfricaModel);
								parseResult.setSuccess(true);
								parseResult.put(new ReportInfo<AfricaAWS>(surfAfricaModel, report));
							}
						}
					}else{
						ReportError re = new ReportError();
						re.setSegment(report);
						re.setMessage("Report format error, the second line's elements number does not equal 52! ");
						parseResult.put(re);
						// 赋值 999998
					}
					
					// 第三段  120个字节，每分钟2个字节。每分钟内无降水时存入“00”，微量存入“,,”，降水量≥10.0mm时，一律存入99，缺测存入“//”。
					if(contents.length >= 3){
						String part3_infos = contents[2].trim();
						if(part3_infos.length() == 120){
							parsePart3(part3_infos, surfAfricaModel);
						}
						else{
							ReportError re = new ReportError();
							re.setSegment(report);
							re.setMessage("Part 3 length is error! ");
							parseResult.put(re);
						}
					}
					
					// 第四段
					if(contents.length == 4){
						String part4_infos [] = contents[3].trim().split("\\s+");
						if(part4_infos.length == 23){
							parsePart4(part4_infos, surfAfricaModel);
						}else{
							ReportError re = new ReportError();
							re.setSegment(report);
							re.setMessage("Part 4 length is error! ");
							parseResult.put(re);
						}
					}
				}
				else{
					ReportError re = new ReportError();
					re.setSegment(report);
					re.setMessage("Report format error, less than 2 lines! ");
					parseResult.put(re);
				}
				
			}

		}
		catch (UnsupportedEncodingException e1) {
			parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
		} 
		catch (FileNotFoundException e) {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}catch (Exception e) {
			parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
		} finally {
			if(scanner != null){
				try{
					scanner.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(read != null){
				try{
					read.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return parseResult;
	}
	
	
	/**
	 * @Title: parsePart4   
	 * @Description: 第四段 云、天气现象等的解析  
	 * @param part4_infos
	 * @param surfAfricaModel void      
	 */
	private static void parsePart4(String[] part4_infos, AfricaAWS surfAfricaModel) {
//		某时次不需要观测或编码的项目，相应记录或编码用相应位长的“/”填充，例如：9时无编报云量，编报云量记录为///，不需编云、天编码，则云状编码记录为24个“/”、天气现象编码记录为////，6小时降水量组编6///1
		Pattern reg2 = Pattern.compile("[0-9]{2}");
		Pattern reg3 = Pattern.compile("[0-9]{3}");
		Pattern reg3Str = Pattern.compile("[0-9A-Z]{3}");
		Pattern reg4 = Pattern.compile("[0-9]{4}");
		Pattern reg24 = Pattern.compile("[A-Z|\\/]{24}");
		Pattern reg5 = Pattern.compile("[0-9]{5}");
		//300 010 003 003 0600 
//		1.	能见度	3字节	正点的能见度，由人工输入
//		if(reg3.matcher(part4_infos[0]).matches()){
//			surfAfricaModel.setVisibilityHourly(Integer.parseInt(part4_infos[0]));
//		}else{
//			surfAfricaModel.setVisibilityHourly(DEFAULT_MISS_VALUE_INT);
//		}
//		2.	总云量	3字节	正点的总云量，由人工输入
		if(reg3.matcher(part4_infos[1]).matches()){
			surfAfricaModel.setCloudAmountHourly(Integer.parseInt(part4_infos[1]));
		}else{
			surfAfricaModel.setCloudAmountHourly(DEFAULT_MISS_VALUE_INT);
		}
//		3.	低云量	3字节	正点的低云量，由人工输入
		if(reg3.matcher(part4_infos[2]).matches()){
			surfAfricaModel.setLowCloudAmountHourly(Integer.parseInt(part4_infos[2]));
		}else{
			surfAfricaModel.setLowCloudAmountHourly(DEFAULT_MISS_VALUE_INT);
		}
//		4.	编报云量	3字节	正点的低云状或中云状云量，由人工输入
		if(reg3.matcher(part4_infos[3]).matches()){
			surfAfricaModel.setLowOrMidCloudAmountHourly(Integer.parseInt(part4_infos[3]));
		}else{
			surfAfricaModel.setLowOrMidCloudAmountHourly(DEFAULT_MISS_VALUE_INT);
		}
//		5.	云高	4字节	正点的低（中）云状云高，由人工输入  
		if(reg4.matcher(part4_infos[4]).matches()){
			surfAfricaModel.setHeightOfLowOrMidCloudHourly(Integer.parseInt(part4_infos[4]));
		}else{
			surfAfricaModel.setHeightOfLowOrMidCloudHourly(DEFAULT_MISS_VALUE_INT);
		}
//		6.	云状	24字节	最多8种云，按简码编  //////////////////ASPFNB
		// 初始化
		int cloudshapes[] = new int[8];
		for(int i = 0; i < 8; i ++){
			cloudshapes[i] = DEFAULT_MISS_VALUE_INT;
		}
		if(reg24.matcher(part4_infos[5]).matches()){
			cloudshapes = cloudShapeGroup(part4_infos[5]);
			surfAfricaModel.setCloudShape(cloudshapes);
		}else{
			surfAfricaModel.setCloudShape(cloudshapes);
		}
		
//		7.	云状编码   	3字节	正点的云状编码，由人工输入
		//72X 
		if(reg3Str.matcher(part4_infos[6]).matches()){
			surfAfricaModel.setCloudShapeHouly(part4_infos[6]);
		}else{
			surfAfricaModel.setCloudShapeHouly(DEFAULT_MISS_VALUE_STR);
		}
//		8.	天气现象编码	4字节	正点的天气现象编码，由人工输入
		// 2166 
		if(reg4.matcher(part4_infos[7]).matches()){
			surfAfricaModel.setWeatherPheno(Integer.parseInt(part4_infos[7]));
		}else{
			surfAfricaModel.setWeatherPheno(DEFAULT_MISS_VALUE_INT);
		}
//		9.	6小时或12小时降水量组编码	5字节	18、0、6、12时（国际时，下同）定时天气报中，编报6RRR1或6RRR2组
		//   60151 
//		RRR取值规则如下：
//		RRR≤989：取RRR电码值；
//		RRR＞990：取（RRR-990）/10；
//		RRR=990：取微量。
//		备注：
//		tR=1 为观测前6小时降水；
//		tR=2 为观测前12小时降水；
//		tR=3 为观测前18小时降水；
//		tR=4 为观测前24小时降水；
//		tR=5 为观测前1小时降水；
//		tR=6 为观测前2小时降水；
//		tR=7 为观测前3小时降水；
//		tR=8 为观测前9小时降水；
//		tR=9 为观测前15小时降水。
		if(reg5.matcher(part4_infos[8]).matches()){
			int t = Integer.parseInt(part4_infos[8].substring(4));
			int rain = Integer.parseInt(part4_infos[8].substring(1, 4));
			if(t == 1){ // 6小时降水
				if(rain <= 989)
					surfAfricaModel.setRainAmount_6Hour(rain);
				else if(rain == 990){
					surfAfricaModel.setRainAmount_6Hour(999990);
				}else if(rain > 990){
					surfAfricaModel.setRainAmount_6Hour((rain - 990) / 10.0);
				}
				else{
					// do nothing
				}
			}
			else if(t == 2){
				if(rain <= 989)
					surfAfricaModel.setRainAmount_12Hour(rain);
				else if(rain == 990){
					surfAfricaModel.setRainAmount_12Hour(999990);
				}else if(rain > 990){
					surfAfricaModel.setRainAmount_12Hour((rain - 990) / 10.0);
				}
				else{
					// do nothing
				}
			}
			else{
				surfAfricaModel.setRainAmount_6Hour(DEFAULT_MISS_VALUE_INT);
			}
		}else{
			surfAfricaModel.setRainAmount_6Hour(DEFAULT_MISS_VALUE_INT);
			surfAfricaModel.setRainAmount_12Hour(DEFAULT_MISS_VALUE_INT);
		}
//		10.	24小时变压变温组	5字节	0、3、6、9、12、15、18、21时（国际时，下同）定时天气报中，编报0P24P24 T24T24组
		// 00153 
		// 24小时变压   (国内3段0组）		P24P24 ＜ 50：取电码值； P24P24 ≥ 50：（电码值-50）×（-1）。
		if(reg2.matcher(part4_infos[9].substring(1, 3)).matches()){
			int t = Integer.parseInt(part4_infos[9].substring(1, 3));
			if(t >= 50)
				surfAfricaModel.setPressChange_24Hour(50 - t);
			else
				surfAfricaModel.setPressChange_24Hour(t);
		}else{
			surfAfricaModel.setPressChange_24Hour(DEFAULT_MISS_VALUE_INT);
		}
		// 24小时变温 (国内3段0组)		T24T24 ＜ 50：取电码值；	T24T24 ≥ 50：（电码值-50）×（-1）。
		if(reg2.matcher(part4_infos[9].subSequence(3, 5)).matches()){
			int t = Integer.parseInt(part4_infos[9].substring(3, 5));
			if(t >= 50)
				surfAfricaModel.setTemChange_24Hour(50 - t);
			else
				surfAfricaModel.setTemChange_24Hour(t);
		}else{
			surfAfricaModel.setTemChange_24Hour(DEFAULT_MISS_VALUE_INT);
		}
//		11.	24小降水量组编码	5字节	21、0时定时天气报中，编报7R24R24R24R24组
		//24小时降水		=9999，取微量; 	否则取R24R24R24R24/10。
		//70234 
		if(reg5.matcher(part4_infos[10]).matches()){
			int t = Integer.parseInt(part4_infos[10].substring(1));
			if(t == 9999)
				surfAfricaModel.setRainAmount_24Hour(999990);
			else
				surfAfricaModel.setRainAmount_24Hour(t / 10.0);
		}else{
			surfAfricaModel.setRainAmount_24Hour(DEFAULT_MISS_VALUE_INT);
		}
//		12.	过去24小时最高气温组	5字节	18、0时定时天气报中，编报1SnTxTxTx组  最高气温	Sn=0：取（/10）；Sn =1：取（-/10）。
		//;   1//// 2//// 3//// /// /// /// // 
		if(reg3.matcher(part4_infos[11]).matches()){
			int t = Integer.parseInt(part4_infos[11].substring(1, 2));
			if(t == 0)
				surfAfricaModel.setMaxTemperature_24Hour(Integer.parseInt(part4_infos[11].substring(2)) / 10.0);
			else if(t == 1)
				surfAfricaModel.setMaxTemperature_24Hour(-Integer.parseInt(part4_infos[11].substring(2)) / 10.0);
			else 
				surfAfricaModel.setMaxTemperature_24Hour(DEFAULT_MISS_VALUE_INT);
		}else{
			surfAfricaModel.setMaxTemperature_24Hour(DEFAULT_MISS_VALUE_INT);
		}
//		13.	过去24小时最低气温组	5字节	0、6时定时天气报中，编报1SnTnTnTn组
		if(reg3.matcher(part4_infos[12].substring(2)).matches()){
			int t = Integer.parseInt(part4_infos[12].substring(1, 2));
			int temp = Integer.parseInt(part4_infos[12].substring(2));
			if(t == 0)
				surfAfricaModel.setMinTemperature_24Hour(temp / 10.0);
			else if(t == 1)
				surfAfricaModel.setMinTemperature_24Hour(-temp / 10.0);
			else 
				surfAfricaModel.setMinTemperature_24Hour(DEFAULT_MISS_VALUE_INT);
		}else{
			surfAfricaModel.setMinTemperature_24Hour(DEFAULT_MISS_VALUE_INT);
		}
//		14.	过去12小时最低地面温度	5字节	0时定时天气报中，编报1SnTgTgTg组
		//  SnTgTgTg	同最高气温
		if(reg3.matcher(part4_infos[13].substring(2)).matches()){
			int t = Integer.parseInt(part4_infos[13].substring(1, 2));
			int temp = Integer.parseInt(part4_infos[13].substring(2));
			if(t == 0)
				surfAfricaModel.setMinTemperature_12Hour(temp / 10.0);
			else if(t== 1)
				surfAfricaModel.setMinTemperature_12Hour(-temp / 10.0);
			else {
				surfAfricaModel.setMinTemperature_12Hour(DEFAULT_MISS_VALUE_INT);
			}
		}else{
			surfAfricaModel.setMinTemperature_12Hour(DEFAULT_MISS_VALUE_INT);
		}
//		15.	积雪深度	3字节	0时或6、12时的观测值，由人工输入
		//  积雪深度		sss=997，取微量  sss=998，取无数据； sss=999，取缺测；	其他取电码值。
		if(reg3.matcher(part4_infos[14]).matches()){
			int t = Integer.parseInt(part4_infos[14]);
			if(t == 997){
				surfAfricaModel.setSnowDepth_0_6_12Hour(999990);
			}else if(t== 998){
				surfAfricaModel.setSnowDepth_0_6_12Hour(999998);
			}else if(t == 999){
				surfAfricaModel.setSnowDepth_0_6_12Hour(999999);
			}else{
				surfAfricaModel.setSnowDepth_0_6_12Hour(t);
			}
		}else{
			surfAfricaModel.setSnowDepth_0_6_12Hour(DEFAULT_MISS_VALUE_INT);
		}
//		16.	雪压	3字节	0时或6、12时的观测值，由人工输入
		if(reg3.matcher(part4_infos[15]).matches()){
			surfAfricaModel.setSnowPress_0_6_12Hour(Integer.parseInt(part4_infos[15]));
		}else{
			surfAfricaModel.setSnowPress_0_6_12Hour(DEFAULT_MISS_VALUE_INT);
		}
//		17.	冻土深度	3字节	0时最大下限值，由人工输入
		if(reg3.matcher(part4_infos[16]).matches()){
			surfAfricaModel.setFrozenSoilDepth(Integer.parseInt(part4_infos[16]));
		}else{
			surfAfricaModel.setFrozenSoilDepth(DEFAULT_MISS_VALUE_INT);
		}
//		18.	地面状态	2字节	6时观测值，由人工输入
		if(reg2.matcher(part4_infos[17]).matches()){
			surfAfricaModel.setGroundState_6Hour(Integer.parseInt(part4_infos[17]));
		}else{
			surfAfricaModel.setGroundState_6Hour(DEFAULT_MISS_VALUE_INT);
		}
		//  911// 915// 919// 934// 939//
//		19.	重要天气极大风速	5字节	18、0、6、12时定时天气报中，编报的911fxfx组。  911表示≥17.0米/秒的极大瞬间风速，fxfx为极大瞬间风速，单位为m/s。取编码值。
		if(reg5.matcher(part4_infos[18]).matches()){
			surfAfricaModel.setExtremWindSpdOfImportantWeathre(Integer.parseInt(part4_infos[18].substring(3)));
		}else{
			surfAfricaModel.setExtremWindSpdOfImportantWeathre(DEFAULT_MISS_VALUE_INT);
		}
//		20.	重要天气极大风速之风向	5字节	18、0、6、12时定时天气报中，编报的915dd组 。。。915表示风向资料，dd为风向，10度为单位编报。	取编码值*10。
		if(reg5.matcher(part4_infos[19]).matches()){
			surfAfricaModel.setDirOfExtremMaxWindOfImportantWeathre(Integer.parseInt(part4_infos[19].substring(3)) * 10);
		}
		else if(part4_infos[19].equalsIgnoreCase("PPC")){
			surfAfricaModel.setDirOfExtremMaxWindOfImportantWeathre(STATIC_WIND);
		}
		else{
			surfAfricaModel.setDirOfExtremMaxWindOfImportantWeathre(DEFAULT_MISS_VALUE_INT);
		}
//		21.	重要天气尘（龙）卷	5字节	18、0、6、12时定时天气报中，编报的919MwDa组。。。
		//919表示过去6小时内观测到的龙卷或尘卷风；Mw为海龙卷、陆龙卷或尘卷风；取编码值。见代码表2555。
		if(reg5.matcher(part4_infos[20]).matches()){
			surfAfricaModel.setTornadoOfImportantWeather(Integer.parseInt(part4_infos[20].substring(3)));
		}else{
			surfAfricaModel.setTornadoOfImportantWeather(DEFAULT_MISS_VALUE_INT);
		}
//		22.	重要天气雨凇	5字节	18、0、6、12时定时天气报中，编报的934RR组
		// 934表示雨淞冻结情况；RR为电线积冰直径，单位mm。
//		电码值为00～26、91～97、99时，取空白；
//		电码值为27时，取微量；
//		电码值为28～55时，取电码值；
//		电码值为56～90时，取（电码值-50）×10；
//		电码值=98，取400；
		if(reg5.matcher(part4_infos[21]).matches()){
			int t = Integer.parseInt(part4_infos[21].substring(3));
			if ((t >= 0 && t <= 26) || (t >= 91 && t <= 97) || t == 99)
				surfAfricaModel.setGlazeOfImportantWeather(DEFAULT_MISS_VALUE_INT);
			else if(t== 27)
				surfAfricaModel.setGlazeOfImportantWeather(999990);
			else if(t >= 28 && t <= 55)
				surfAfricaModel.setGlazeOfImportantWeather(t);
			else if(t >= 56 &&t <= 90)
				surfAfricaModel.setGlazeOfImportantWeather((t - 50) * 10);
			else if (t== 90)
				surfAfricaModel.setGlazeOfImportantWeather(400);
			else {
				// do nothing
			}
		}else{
			surfAfricaModel.setGlazeOfImportantWeather(DEFAULT_MISS_VALUE_INT);
		}
//		23.	重要天气冰雹直径	5字节	18、0、6、12时定时天气报中，编报的939nn组
		//939表示冰雹资料；nn为最大冰雹的最大直径，单位mm。 取电码值。
		if(reg5.matcher(part4_infos[22]).matches()){
			surfAfricaModel.setHailDiameterOfImportantWeather(Integer.parseInt(part4_infos[22].substring(3)));
		}else{
			surfAfricaModel.setHailDiameterOfImportantWeather(DEFAULT_MISS_VALUE_INT);
		}
	}

	/**
	 * @Title: parsePart3   
	 * @Description: 第三段（分钟降水解析） 
	 * @param part3_infos
	 * @param report
	 * @param surfAfricaModel      
	 */
	private static void parsePart3(String part3_infos, AfricaAWS surfAfricaModel) {
		//小时内分钟降水量，120个字节，每分钟2个字节。每分钟内无降水时存入“00”，微量存入“,,”，降水量≥10.0mm时，一律存入99，缺测存入“//”。
		double rains[] = new double[60];
		String tmp = "";
		for(int i = 0; i < 120; i += 2){
			tmp = part3_infos.substring(i, i + 2);
			if(tmp.equals("99")){
				rains[i / 2] = 99;
			}else if(tmp.equals(",,")){
				rains[i / 2] = 999990;
			}else{
				try{
					rains[i / 2] = Integer.parseInt(tmp) / 10.0;
				}catch (Exception e) {
					rains[i / 2] = 999999;
				}
			}
		}
		surfAfricaModel.setRainMinutely(rains);
	}

	/**
	 * @Title: cloudShapeGroup   
	 * @Description: 解析云状   
	 * @param shapes  24位，8组
	 * @return int[]      
	 * @throws：
	 */
	private static int[] cloudShapeGroup(String shapes){
		int []shapeArray = new int[8];
		String tmp = "";
		for(int i = 0; i < 24; i += 3){
			tmp = shapes.substring(i, i + 3);
		    if(tmp.indexOf("/") >= 0){ // 包含缺测
		    	shapeArray[i / 3] = DEFAULT_MISS_VALUE_INT;
		    }
		    else{
		    	if(map.get(tmp) != null)
		    		shapeArray[i / 3] = map.get(tmp);
		    	else
		    		shapeArray[i / 3] = DEFAULT_MISS_VALUE_INT;
		    }
		}
		return shapeArray;
	}
	/**
	 * 
	 * @Title: parsePart2   
	 * @Description: 解析报文第二行 
	 * @param part2_infos
	 * @param report
	 * @param surfAfricaModel void      
	 */
	private int parsePart2(String[] part2_infos, String report, AfricaAWS surfAfricaModel) {
		Pattern reg3 = Pattern.compile("[0-9]{3}");
		Pattern reg4 = Pattern.compile("[0-9]{4}");
		Pattern reg5 = Pattern.compile("[0-9]{5}");
//		1	观测时间	14字节	年月日时分秒（国际时，yyyyMMddhhmmss），其中：秒固定为“00”，为正点观测资料时，分记录为“00”
		Pattern reg = Pattern.compile("[0-9]{14}");
		if(reg.matcher(part2_infos[0]).matches()){
			try {
				Date date = simpleDateFormat.parse(part2_infos[0]);
				surfAfricaModel.setObserDateTime(date);
			} catch (ParseException e) {
				ReportError re = new ReportError();
				re.setSegment(report);
				re.setMessage("Datetime error! " );
				parseResult.put(re);
				return -1;
			}
		}else{
			ReportError re = new ReportError();
			re.setSegment(report);
			re.setMessage("Datetime error! ");
			parseResult.put(re);
			return -1;
		}
//		2	2分钟风向	3字节	当前时刻的2分钟风向 
		if(reg3.matcher(part2_infos[1]).matches()){
			surfAfricaModel.setWindDir_2min(Integer.parseInt(part2_infos[1]));
		}
		else if(part2_infos[1].equalsIgnoreCase("PPC")){
			surfAfricaModel.setWindDir_2min(STATIC_WIND);
		}
		else{
			surfAfricaModel.setWindDir_2min(DEFAULT_MISS_VALUE_INT);
		}
//		3	2分钟平均风速	3字节	当前时刻的2分钟平均风速
		if(reg3.matcher(part2_infos[2]).matches()){
			surfAfricaModel.setWindSpd_2min(Integer.parseInt(part2_infos[2]) / 10.0);
		}else{
			surfAfricaModel.setWindSpd_2min(DEFAULT_MISS_VALUE_INT);
		}
		// 2019-11-5 chy
		if(surfAfricaModel.getWindSpd_2min() == 0){
			surfAfricaModel.setWindDir_2min(999017);
		}
		if(surfAfricaModel.getWindDir_2min() == 360)
			surfAfricaModel.setWindDir_2min(0);
		
//		4	10分钟风向	3字节	当前时刻的10分钟风向
		if(reg3.matcher(part2_infos[3]).matches()){
			surfAfricaModel.setWindDir_10min(Integer.parseInt(part2_infos[3]));
		}
		else if(part2_infos[3].equalsIgnoreCase("PPC")){
			surfAfricaModel.setWindDir_10min(STATIC_WIND);
		}
		else{
			surfAfricaModel.setWindDir_10min(DEFAULT_MISS_VALUE_INT);
		}
//		5	10分钟平均风速	3字节	当前时刻的10分钟平均风速
		if(reg3.matcher(part2_infos[4]).matches()){
			surfAfricaModel.setWindSpd_10min(Integer.parseInt(part2_infos[4]) / 10.0);
		}else{
			surfAfricaModel.setWindSpd_10min(DEFAULT_MISS_VALUE_INT);
		}
		// 2019-11-5 chy
		if(surfAfricaModel.getWindSpd_10min() == 0)
			surfAfricaModel.setWindDir_10min(999017);
		if(surfAfricaModel.getWindDir_10min() == 360)
			surfAfricaModel.setWindDir_10min(0);
			
		
//		6	最大风速的风向	3字节	每1小时内10分钟最大风速的风向
		if(reg3.matcher(part2_infos[5]).matches()){
			surfAfricaModel.setWindDirOfMaxSpd(Integer.parseInt(part2_infos[5]));
		}
		else if(part2_infos[5].equalsIgnoreCase("PPC")){
			surfAfricaModel.setWindDirOfMaxSpd(STATIC_WIND);
		}
		else{
			surfAfricaModel.setWindDirOfMaxSpd(DEFAULT_MISS_VALUE_INT);
		}
//		7	最大风速	3字节	每1小时内10分钟最大风速
		if(reg3.matcher(part2_infos[6]).matches()){
			surfAfricaModel.setMaxWindSpd(Integer.parseInt(part2_infos[6]) / 10.0);
		}else{
			surfAfricaModel.setMaxWindSpd(DEFAULT_MISS_VALUE_INT);
		}
//		8	最大风速出现时间	4字节	每1小时内10分钟最大风速出现时间，时分各两位，下同
		if(reg4.matcher(part2_infos[7]).matches()){
			surfAfricaModel.setOccurTimeOfMaxWind(Integer.parseInt(part2_infos[7]));
		}else{
			surfAfricaModel.setOccurTimeOfMaxWind(DEFAULT_MISS_VALUE_INT);
		}
//		9	瞬时风向	3字节	当前时刻的瞬时风向
		if(reg3.matcher(part2_infos[8]).matches()){
			surfAfricaModel.setWindDir(Integer.parseInt(part2_infos[8]));
		}
		else if(part2_infos[8].equalsIgnoreCase("PPC")){
			surfAfricaModel.setWindDir(STATIC_WIND);
		}
		else{
			surfAfricaModel.setWindDir(DEFAULT_MISS_VALUE_INT);
		}
//		10	瞬时风速	3字节	当前时刻的瞬时风速
		if(reg3.matcher(part2_infos[9]).matches()){
			surfAfricaModel.setWindSpd(Integer.parseInt(part2_infos[9]) / 10.0);
		}else{
			surfAfricaModel.setWindSpd(DEFAULT_MISS_VALUE_INT);
		}
//		11	极大风速的风向	3字节	每1小时内的极大风速的风向
		if(reg3.matcher(part2_infos[10]).matches()){
			surfAfricaModel.setWindDirOfExtremMaxWind(Integer.parseInt(part2_infos[10]));
		}
		else if(part2_infos[10].equalsIgnoreCase("PPC")){
			surfAfricaModel.setWindDirOfExtremMaxWind(STATIC_WIND);
		}
		else{
			surfAfricaModel.setWindDirOfExtremMaxWind(DEFAULT_MISS_VALUE_INT);
		}
//		12	极大风速	3字节	每1小时内的极大风速
		if(reg3.matcher(part2_infos[11]).matches()){
			surfAfricaModel.setExtremMaxWindSpd(Integer.parseInt(part2_infos[11]) / 10.0);
		}else{
			surfAfricaModel.setExtremMaxWindSpd(DEFAULT_MISS_VALUE_INT);
		}
//		13	极大风速出现时间	4字节	每1小时内极大风速出现时间
		if(reg4.matcher(part2_infos[12]).matches()){
			surfAfricaModel.setOccurTimeOfExtremMaxWind(Integer.parseInt(part2_infos[12]));
		}else{
			surfAfricaModel.setOccurTimeOfExtremMaxWind(DEFAULT_MISS_VALUE_INT);
		}
//		14	小时降水量	4字节	每1小时内的雨量累计值
		if(reg4.matcher(part2_infos[13]).matches()){
			surfAfricaModel.setRainAmountHourly(Integer.parseInt(part2_infos[13]) / 10.0);
		}else{
			surfAfricaModel.setRainAmountHourly(DEFAULT_MISS_VALUE_INT);
		}
//		15	气温	4字节	当前时刻的空气温度
		if(reg4.matcher(part2_infos[14]).matches()){
			surfAfricaModel.setTemperature(Integer.parseInt(part2_infos[14]) / 10.0);
		}
		// 2020-3-25 chy 首位为符号
		else if(reg3.matcher(part2_infos[14].substring(1)).matches() && part2_infos[14].substring(0, 1).equals("-")){
			surfAfricaModel.setTemperature(Integer.parseInt(part2_infos[14]) / 10.0);
		}
		else{
			surfAfricaModel.setTemperature(DEFAULT_MISS_VALUE_INT);
		}
//		16	最高气温	4字节	每1小时内的最高气温
		if(reg4.matcher(part2_infos[15]).matches()){
			surfAfricaModel.setMaxTemperature(Integer.parseInt(part2_infos[15]) / 10.0);
		}
		// 2020-3-24 chy 首位为符号
		else if(reg3.matcher(part2_infos[15].substring(1)).matches() && part2_infos[15].substring(0, 1).equals("-")){
			surfAfricaModel.setMaxTemperature(Integer.parseInt(part2_infos[15]) / 10.0);
		}
		else{
			surfAfricaModel.setMaxTemperature(DEFAULT_MISS_VALUE_INT);
		}
//		17	最高气温出现时间	4字节	每1小时内的最高气温出现时间
		if(reg4.matcher(part2_infos[16]).matches()){
			surfAfricaModel.setOccurTimeOfMaxTemp(Integer.parseInt(part2_infos[16]));
		}
		else{
			surfAfricaModel.setOccurTimeOfMaxTemp(DEFAULT_MISS_VALUE_INT);
		}
//		18	最低气温	4字节	每1小时内的最低气温
		if(reg4.matcher(part2_infos[17]).matches()){
			surfAfricaModel.setMinTemperature(Integer.parseInt(part2_infos[17]) / 10.0);
		}
		// 2020-3-25 chy
		else if(reg3.matcher(part2_infos[17].substring(1)).matches() && part2_infos[17].substring(0, 1).equals("-")){
			surfAfricaModel.setMinTemperature(Integer.parseInt(part2_infos[17]) / 10.0);
		}
		else{
			surfAfricaModel.setMinTemperature(DEFAULT_MISS_VALUE_INT);
		}
//		19	最低气温出现时间	4字节	每1小时内的最低气温出现时间
		if(reg4.matcher(part2_infos[18]).matches()){
			surfAfricaModel.setOccurTimeOfMinTemp(Integer.parseInt(part2_infos[18]));
		}
		
		else{
			surfAfricaModel.setOccurTimeOfMinTemp(DEFAULT_MISS_VALUE_INT);
		}
//		20	相对湿度	3字节	当前时刻的相对湿度
		if(reg3.matcher(part2_infos[19]).matches()){
			surfAfricaModel.setRelativeHumid(Integer.parseInt(part2_infos[19]));
		}else{
			surfAfricaModel.setRelativeHumid(DEFAULT_MISS_VALUE_INT);
		}
//		21	最小相对湿度	3字节	每1小时内的最小相对湿度值
		if(reg3.matcher(part2_infos[20]).matches()){
			surfAfricaModel.setMinRelativeHumid(Integer.parseInt(part2_infos[20]));
		}else{
			surfAfricaModel.setMinRelativeHumid(DEFAULT_MISS_VALUE_INT);
		}
//		22	最小相对湿度出现时间	4字节	每1小时内的最小相对湿度出现时间
		if(reg4.matcher(part2_infos[21]).matches()){
			surfAfricaModel.setOccurTimeOfMinRelativeHumid(Integer.parseInt(part2_infos[21]));
		}else{
			surfAfricaModel.setOccurTimeOfMinRelativeHumid(DEFAULT_MISS_VALUE_INT);
		}
//		23	水汽压	3字节	当前时刻的水汽压值
		if(reg3.matcher(part2_infos[22]).matches()){
			surfAfricaModel.setVaporPress(Integer.parseInt(part2_infos[22]) / 10.0);
		}else{
			surfAfricaModel.setVaporPress(DEFAULT_MISS_VALUE_INT);
		}
//		24	露点温度	4字节	当前时刻的露点温度值
		if(reg4.matcher(part2_infos[23]).matches()){
			surfAfricaModel.setDewPoint(Integer.parseInt(part2_infos[23]) / 10.0);
		}
		//2020-3-25 chy 负温度
		else if(reg3.matcher(part2_infos[23].substring(1)).matches() && part2_infos[23].substring(0, 1).equals("-")){
			surfAfricaModel.setDewPoint(Integer.parseInt(part2_infos[23]) / 10.0);
		}
		
		else{
			surfAfricaModel.setDewPoint(DEFAULT_MISS_VALUE_INT);
		}
//		25	本站气压	5字节	当前时刻的本站气压值
		if(reg5.matcher(part2_infos[24]).matches()){
			surfAfricaModel.setStationPress(Integer.parseInt(part2_infos[24]) / 10.0);
		}else{
			surfAfricaModel.setStationPress(DEFAULT_MISS_VALUE_INT);
		}
//		26	最高本站气压	5字节	每1小时内的最高本站气压值
		if(reg5.matcher(part2_infos[25]).matches()){
			surfAfricaModel.setMaxStationPress(Integer.parseInt(part2_infos[25]) / 10.0);
		}else{
			surfAfricaModel.setMaxStationPress(DEFAULT_MISS_VALUE_INT);
		}
//		27	最高本站气压出现时间	4字节	每1小时内的最高本站气压出现时间
		if(reg4.matcher(part2_infos[26]).matches()){
			surfAfricaModel.setOccurTimeOfMaxStaPress(Integer.parseInt(part2_infos[26]));
		}else{
			surfAfricaModel.setOccurTimeOfMaxStaPress(DEFAULT_MISS_VALUE_INT);
		}
//		28	最低本站气压	5字节	每1小时内的最低本站气压值
		if(reg5.matcher(part2_infos[27]).matches()){
			surfAfricaModel.setMinStationPress(Integer.parseInt(part2_infos[27]) / 10.0);
		}else{
			surfAfricaModel.setMinStationPress(DEFAULT_MISS_VALUE_INT);
		}
//		29	最低本站气压出现时间	4字节	每1小时内的最低本站气压出现时间
		if(reg4.matcher(part2_infos[28]).matches()){
			surfAfricaModel.setOccurTimeOfMinStaPress(Integer.parseInt(part2_infos[28]) );
		}else{
			surfAfricaModel.setOccurTimeOfMinStaPress(DEFAULT_MISS_VALUE_INT);
		}
//		30	草面（雪面）温度	4字节	当前时刻的草面（雪面）温度值
		if(reg4.matcher(part2_infos[29]).matches()){
			surfAfricaModel.setGrassGroundTemp(Integer.parseInt(part2_infos[29]) / 10.0);
		}else{
			surfAfricaModel.setGrassGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		31	草面（雪面）最高温度	4字节	每1小时内的草面（雪面）最高温度
		if(reg4.matcher(part2_infos[30]).matches()){
			surfAfricaModel.setMaxGrassGroundTemp(Integer.parseInt(part2_infos[30]) / 10.0);
		}
		// 2020-3-25 chy 负值
		else if(reg3.matcher(part2_infos[30].substring(1)).matches() && part2_infos[30].substring(0, 1).equals("-")){
			surfAfricaModel.setMaxGrassGroundTemp(Integer.parseInt(part2_infos[30]) / 10.0);
		}
		
		else{
			surfAfricaModel.setMaxGrassGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		32	草面（雪面）最高出现时间	4字节	每1小时内的草面（雪面）最高温度出现时间
		if(reg4.matcher(part2_infos[31]).matches()){
			surfAfricaModel.setOccurTimeOfMaxGrassGroundTemp(Integer.parseInt(part2_infos[31]));
		}else{
			surfAfricaModel.setOccurTimeOfMaxGrassGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		33	草面（雪面）最低温度	4字节	每1小时内的草面（雪面）最低温度
		if(reg4.matcher(part2_infos[32]).matches()){
			surfAfricaModel.setMinGrassGroundTemp(Integer.parseInt(part2_infos[32]) / 10.0);
		}
		// 2020-3-25 chy 负值
		else if(reg3.matcher(part2_infos[32].substring(1)).matches() && part2_infos[32].substring(0, 1).equals("-")){
			surfAfricaModel.setMinGrassGroundTemp(Integer.parseInt(part2_infos[32]) / 10.0);
		}
		
		else{
			surfAfricaModel.setMinGrassGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		34	草面（雪面）最低出现时间	4字节	每1小时内的草面（雪面）最低温度出现时间
		if(reg4.matcher(part2_infos[33]).matches()){
			surfAfricaModel.setOccurTimeOfMinGrassGroundTemp(Integer.parseInt(part2_infos[33]));
		}else{
			surfAfricaModel.setOccurTimeOfMinGrassGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		35	地面温度	4字节	当前时刻的地面温度值
		if(reg4.matcher(part2_infos[34]).matches()){
			surfAfricaModel.setGroundTemp(Integer.parseInt(part2_infos[34]) / 10.0);
		}else{
			surfAfricaModel.setGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		36	地面最高温度	4字节	每1小时内的地面最高温度
		if(reg4.matcher(part2_infos[35]).matches()){
			surfAfricaModel.setMaxGroundTemp(Integer.parseInt(part2_infos[35]) / 10.0);
		}else{
			surfAfricaModel.setMaxGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		37	地面最高出现时间	4字节	每1小时内的地面最高温度出现时间
		if(reg4.matcher(part2_infos[36]).matches()){
			surfAfricaModel.setOccurTimeOfMaxGroundTemp(Integer.parseInt(part2_infos[36]));
		}else{
			surfAfricaModel.setOccurTimeOfMaxGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		38	地面最低温度	4字节	每1小时内的地面最低温度
		if(reg4.matcher(part2_infos[37]).matches()){
			surfAfricaModel.setMinGroundTemp(Integer.parseInt(part2_infos[37]) / 10.0);
		}else{
			surfAfricaModel.setMinGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		39	地面最低出现时间	4字节	每1小时内的地面最低温度出现时间
		if(reg4.matcher(part2_infos[38]).matches()){
			surfAfricaModel.setOccurTimeOfMinGroundTemp(Integer.parseInt(part2_infos[38]));
		}else{
			surfAfricaModel.setOccurTimeOfMinGroundTemp(DEFAULT_MISS_VALUE_INT);
		}
//		40	5厘米地温	4字节	当前时刻的5厘米地温值
		if(reg4.matcher(part2_infos[39]).matches()){
			surfAfricaModel.setGroundTemp_5cm(Integer.parseInt(part2_infos[39]) / 10.0);
		}else{
			surfAfricaModel.setGroundTemp_5cm(DEFAULT_MISS_VALUE_INT);
		}
//		41	10厘米地温	4字节	当前时刻的10厘米地温值
		if(reg4.matcher(part2_infos[40]).matches()){
			surfAfricaModel.setGroundTemp_10cm(Integer.parseInt(part2_infos[40]) / 10.0);
		}else{
			surfAfricaModel.setGroundTemp_10cm(DEFAULT_MISS_VALUE_INT);
		}
//		42	15厘米地温	4字节	当前时刻的15厘米地温值
		if(reg4.matcher(part2_infos[41]).matches()){
			surfAfricaModel.setGroundTemp_15cm(Integer.parseInt(part2_infos[41]) / 10.0);
		}else{
			surfAfricaModel.setGroundTemp_15cm(DEFAULT_MISS_VALUE_INT);
		}
//		43	20厘米地温	4字节	当前时刻的20厘米地温值
		if(reg4.matcher(part2_infos[42]).matches()){
			surfAfricaModel.setGroundTemp_20cm(Integer.parseInt(part2_infos[42]) / 10.0);
		}else{
			surfAfricaModel.setGroundTemp_20cm(DEFAULT_MISS_VALUE_INT);
		}
//		44	40厘米地温	4字节	当前时刻的40厘米地温值
		if(reg4.matcher(part2_infos[43]).matches()){
			surfAfricaModel.setGroundTemp_40cm(Integer.parseInt(part2_infos[43]) / 10.0);
		}else{
			surfAfricaModel.setGroundTemp_40cm(DEFAULT_MISS_VALUE_INT);
		}
//		45	80厘米地温	4字节	当前时刻的80厘米地温值
		if(reg4.matcher(part2_infos[44]).matches()){
			surfAfricaModel.setGroundTemp_80cm(Integer.parseInt(part2_infos[44]) / 10.0);
		}else{
			surfAfricaModel.setGroundTemp_80cm(DEFAULT_MISS_VALUE_INT);
		}
//		46	160厘米地温	4字节	当前时刻的160厘米地温值
		if(reg4.matcher(part2_infos[45]).matches()){
			surfAfricaModel.setGroundTemp_160cm(Integer.parseInt(part2_infos[45]) / 10.0);
		}else{
			surfAfricaModel.setGroundTemp_160cm(DEFAULT_MISS_VALUE_INT);
		}
//		47	320厘米地温		4字节	当前时刻的320厘米地温值
		if(reg4.matcher(part2_infos[46]).matches()){
			surfAfricaModel.setGroundTemp_320cm(Integer.parseInt(part2_infos[46]) / 10.0);
		}else{
			surfAfricaModel.setGroundTemp_320cm(DEFAULT_MISS_VALUE_INT);
		}
//		48	蒸发量	4字节	每1小时内的蒸发累计量
		if(reg4.matcher(part2_infos[47]).matches()){
			surfAfricaModel.setVaporHourly(Integer.parseInt(part2_infos[47]) / 10.0);
		}else{
			surfAfricaModel.setVaporHourly(DEFAULT_MISS_VALUE_INT);
		}
//		49	海平面气压	5字节	当前时刻的海平面气压值
		if(reg5.matcher(part2_infos[48]).matches()){
			surfAfricaModel.setSeaLevelPress(Integer.parseInt(part2_infos[48]) / 10.0);
		}else{
			surfAfricaModel.setSeaLevelPress(DEFAULT_MISS_VALUE_INT);
		}
//		50	能见度	5字节	当前时刻的能见度
		if(reg5.matcher(part2_infos[49]).matches()){
			surfAfricaModel.setVisibility(Integer.parseInt(part2_infos[49]));
		}else{
			surfAfricaModel.setVisibility(DEFAULT_MISS_VALUE_INT);
		}
//		51	最小能见度	5字节	每1小时内的最小能见度
		if(reg5.matcher(part2_infos[50]).matches()){
			surfAfricaModel.setMinVisibility(Integer.parseInt(part2_infos[50]));
		}else{
			surfAfricaModel.setMinVisibility(DEFAULT_MISS_VALUE_INT);
		}
//		52	最小能见度出现时间	4字节	每1小时内的最小能见度出现时间
		if(reg4.matcher(part2_infos[51]).matches()){
			surfAfricaModel.setOccurTimeOfMinVisibility(Integer.parseInt(part2_infos[51]));
		}else{
			surfAfricaModel.setOccurTimeOfMinVisibility(DEFAULT_MISS_VALUE_INT);
		}
		
		return 1;
	}

	/**
	 * @Title: parsePart1   
	 * @Description: 解析台站第一段 
	 * @param header_infos
	 * @param report
	 * @param surfAfricaModel
	 * @return int      
	 */
	private int parsePart1 (String []header_infos, String report, AfricaAWS surfAfricaModel){
		String tmp = header_infos[0];
		//1  区站号	5字节	5位数字或第1位为字母，第2-5位为数字
//		Pattern reg = Pattern.compile("[A-Z0-9][0-9]{4}");
		Pattern reg = Pattern.compile("[A-Z0-9]{5}");
		Matcher matcher = reg.matcher(tmp);
		if(matcher.matches()){
			surfAfricaModel.setStationNumberChina(tmp);
		}else{
			ReportError re = new ReportError();
			re.setSegment(report);
			re.setMessage("Station format is error!");
			parseResult.put(re);
			return -1;
		}
		// 纬度	6字节	按度分秒记录，均为2位，高位不足补“0”，台站纬度未精确到秒时，秒固定记录“00”
		tmp = header_infos[1];
		reg = Pattern.compile("[0-9]{6}");
		matcher = reg.matcher(tmp);
		if(matcher.matches()){
			surfAfricaModel.setLatitude(Integer.parseInt(tmp.substring(0, 2)) + Integer.parseInt(tmp.substring(2, 4)) / 60.0 + 
			Integer.parseInt(tmp.substring(4, 6)) / 3600.0);
		}
		else{
			surfAfricaModel.setLatitude(DEFAULT_MISS_VALUE_INT);
		}		
		//经度	7字节	按度分秒记录，度为3位，分秒为2位，高位不足补“0”，台站经度未精确到秒时，秒固定记录“00”
		tmp = header_infos[2];
		reg = Pattern.compile("[0-9]{7}");
		matcher = reg.matcher(tmp);
		if(matcher.matches()){
			surfAfricaModel.setLongitude(Integer.parseInt(tmp.substring(0, 3)) + Integer.parseInt(tmp.substring(3, 5)) / 60.0 + 
					Integer.parseInt(tmp.substring(5, 7)) / 3600.0);
		}
		else{
			surfAfricaModel.setLongitude(DEFAULT_MISS_VALUE_INT);
		}
		//观测场拔海高度	5字节	保留一位小数，扩大10倍记录，高位不足补“0”
		tmp = header_infos[3];
		reg = Pattern.compile("[0-9]{5}");
		matcher = reg.matcher(tmp);
		if(matcher.matches()){
			surfAfricaModel.setHeightOfSationGroundAboveMeanSeaLevel(Integer.parseInt(tmp) / 10.0);
		}else{
			surfAfricaModel.setHeightOfSationGroundAboveMeanSeaLevel(DEFAULT_MISS_VALUE_INT);
		}
		//气压传感器拔海高度	5字节	保留一位小数，扩大10倍记录，高位不足补“0”，无气压传感器时，录入“/////”
		tmp = header_infos[4];
		matcher = reg.matcher(tmp);
		if(matcher.matches()){
			surfAfricaModel.setHeightOfBaromSensor(Integer.parseInt(tmp) / 10.0);
		}else{
			surfAfricaModel.setHeightOfBaromSensor(DEFAULT_MISS_VALUE_INT);
		}
		//观测方式	1字节	固定存入4
		surfAfricaModel.setObserMethod(4);
		return 1;
	}
	/**
	 * @Title: cloudShape   
	 * @Description: 云状字符到数值的转换   
	 * @param cloudShapeStr
	 * @return Map[]      
	 * @throws：
	 */
	private static void cloudShape (){
		map = new HashMap<String, Integer>();
		String cloudshapeS[] = {
				"CUU", "FCB", "CUO", "CBV", "CBP", "SCR", "SCP", "SCU", "SCA", "SCT", "STB", "FSB", 
				"NSB", "FNB", "ASR", "ASP", "ACR", "ACP", "ACE", "ACU", "ACL", "ACA", "CII", "CIE",
				"CIO", "CIN", "CSI", "CSE", "CCB"}; //29个
		int cloudshapeI[] = {
				31, 48, 32, 33, 39, 40, 41, 34, 42, 43, 32, 37, 
				5, 44, 45, 46, 21, 22, 24, 26, 28, 49, 11, 12, 
				47, 14, 16, 15, 1};
		int mapsize = cloudshapeI.length;
		for(int i = 0; i < mapsize; i ++){
			map.put(cloudshapeS[i], cloudshapeI[i]);
		}
	}
//	public static void main(String[] args) {
//		SurfAfricaParse parse = new SurfAfricaParse();
//		parse.parseFile("D:\\HUAXIN\\DataProcess\\非洲援建站\\非洲援建站\\非洲样例数据\\test.txt");
//	}
	
}
