package cma.cimiss2.dpc.decoder.sevp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.Typh;
import cma.cimiss2.dpc.decoder.bean.sevp.TyphEle;
import cma.cimiss2.dpc.decoder.bean.sevp.TyphKey;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Typhoon Live and Forecast data <br>
 * 台风实况与预报数据解码类
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double 进行基本运算。
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.sevp.Typh}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * ZCZC 641<br>

WTKO20 RKSL 150600<br>

KMA TROPICAL CYCLONE ADVISORY NO. 10<br>

NAME 1512 HALOLA<br>

ANALYSIS<br>

POSITION 150600UTC 17.7N 171.0E<br>

MOVEMENT NW 9KT<br>

PRES/VMAX 965HPA 72KT<br>

FORECAST<br>

24HR<br>

POSITION 160600UTC 18.9N 166.9E WITHIN 75NM<br>

PRES/VMAX 960HPA 76KT<br>

48HR<br>

POSITION 170600UTC 19.5N 161.3E WITHIN 125NM<br>

PRES/VMAX 955HPA 78KT<br>

72HR<br>

POSITION 180600UTC 20.1N 155.8E WITHIN 175NM<br>

PRES/VMAX 955HPA 78KT<br>

96HR<br>

POSITION 190600UTC 21.3N 151.6E WITHIN 250NM<br>

PRES/VMAX 950HPA 84KT<br>

120HR<br>

POSITION 200600UTC 23.1N 147.2E WITHIN 295NM<br>

PRES/VMAX 950HPA 84KT<br>

KOREA METEOROLOGICAL ADMINISTRATION.<br>

NNNN<br>
 * <strong>code:</strong><br>
 * DecodeTyph decodeTyph = new DecodeTyph();<br>
 * ParseResult<Typh> parseResult = decodeTyph.DecodeFile(new File(String filepath));<br>
 * List<Typh> list = parseResult.getData();<br>
 * TyphKey key = list.get(0).getTyphKey();<br>
 * List<TyphEle> eles = list.get(0).getTyphEles();<br>
 * System.out.println("台风名称、台风级别、台风国际编号： " + key.getTyphName() + " " + key.getTyphLevel() + " " + key.getInternalCode());<br>
 * System.out.println("纬度、经度：" + eles.get(0).getLatitude() + " " + eles.get(0).getLongtitude());<br>
 * <strong>output:</strong><br>
 * 台风名称、台风级别、台风国际编号： HALOLA 999999 1512<br>
 * 纬度、经度：17.7 171.0<br>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午4:42:43   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeTyph{
	/**
	 * 解码结果集
	 */
	private ParseResult<Typh> parseResult = new ParseResult<Typh>(false);
	/**
	 * 缺测替代值
	 */
	private double defaultF = 999999.0;
	/**
	 * 观测资料年月
	 */
	String yyyyMM = "";  
	/**
	 * 阿拉伯数字0至9以及小数点的英文表示
	 */
	static String Adigit[] = {"ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "POINT"};
	/**
	 * 阿拉伯数字0至9以及小数点
	 */
	static String Ddigit[] = {"0","1","2","3","4","5","6","7","8","9","."};
	/**
	 * 1至12月的英文简写
	 */
	static String Amonth[] = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
	/**
	 * 1值12月的数字表示
	 */
	static String Dmonth[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
	/**
	 * 1至12月的天数
	 */
	static int maxd[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
	/**
	 * 台风实况与预报数据解码主函数   
	 * @param file 待解码文件
	 * @return ParseResult<Typh>     解码结果封装  
	 */
	@SuppressWarnings({ "resource", "deprecation", "static-access" })
	public ParseResult<Typh> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获得资料日期
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//			String filePath = file.getPath();
//			String sp[] = filePath.split("\\"+System.getProperty("file.separator"));//filePath.split("\\\\"); 
//			String sp[] = filePath.split("_");
			Date oDate = new Date(file.lastModified()); // 观测时间
			// 从文件路径、文件名解析信息
//			if(sp.length >= 2){
//				try{
//					oDate = simpleDateFormat.parse(sp[sp.length - 2]);
//				}catch (ParseException e) {
//					ReportError re = new ReportError();
//					re.setMessage("DateTime format error!");
//					re.setSegment(filePath);
//					parseResult.put(re);
//					return parseResult;
//				}
//			}
//			else{ // 无法获取资料日期，返回
//				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
//				return parseResult;
//			}
//			yyyyMM = sp[sp.length - 2].substring(0, 6);
			yyyyMM = simpleDateFormat.format(oDate).substring(0, 6);
			if(file.getParent().toLowerCase().contains("his")){
				try{
					yyyyMM = file.getName().split("_")[4].substring(0, 6);
				}
				catch (Exception e) {
					
				}
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			InputStreamReader read = null;
			Scanner scanner = null;
			 // 获取文件流
			 try{
	        	read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("NNNN"); // 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
				String report = "";
				while (scanner.hasNext() && (report = scanner.next().trim()).length() > 0) {  
					String headerline= "";
					// 移除空行 
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String reports[] = report.split("\n|(\r\n)");
					if(report == null || reports.length < 3) {
						ReportError re = new ReportError();
						re.setMessage("Report too short, format error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					BullHeader bullHeader = new BullHeader();
					Date dataTime;
					int ret = -1;
					// 1. 一个编报中心的资料，首行标识判断
					reports[0] = reports[0].trim();
					if(reports[0].substring(0, 4).compareTo("ZCZC") == 0 
							|| reports[0].substring(0, 3).compareTo("ZCZ") == 0
							|| reports[0].substring(0, 3).compareTo("CZC") == 0){
						reports[1] = reports[1].trim();
						headerline = reports[1];
						ret = BullHeader.decodeHeader(reports[1], bullHeader);
						if(ret < 0){ // 报头格式错误
							ReportError re = new ReportError();
							re.setMessage("Report header format error or null!");
							re.setSegment(reports[1]);
							parseResult.put(re);
							continue;
						}
						 // 资料时间
						try { 
							dataTime = sdf.parse(yyyyMM + bullHeader.getYYGGgg());
							dataTime.setSeconds(0);
							if(!TimeCheckUtil.checkTime(dataTime)){
								ReportError re = new ReportError();
								re.setMessage("DataTime out of range：time:"+dataTime);
								re.setSegment(reports[1]);
								parseResult.put(re);
								continue;
							}
						} catch (ParseException e) {
							ReportError re = new ReportError();
							re.setMessage("DateTime format error!");
							re.setSegment(reports[1]);
							parseResult.put(re);
							continue;
						}
						report = "";
						for(int i = 2; i < reports.length; i ++)
							report += (reports[i] + " ");
					}
					else{
						// 2. 一个编报中心的资料, 第二行标识判断
						reports[2] = reports[2].trim();
						headerline = reports[2];
						ret = BullHeader.decodeHeader(reports[2], bullHeader);
						if(ret < 0){ // 报头格式错误
							ReportError re = new ReportError();
							re.setMessage("Report header format error or null!");
							re.setSegment(reports[2]);
							parseResult.put(re);
							continue;
						}
						 // 资料时间
						try { 
							dataTime = sdf.parse(yyyyMM + bullHeader.getYYGGgg());
							dataTime.setSeconds(0);
							if(!TimeCheckUtil.checkTime(dataTime)){
								ReportError re = new ReportError();
								re.setMessage("DataTime out of range：time:"+dataTime);
								re.setSegment(reports[2]);
								parseResult.put(re);
								continue;
							}
						} catch (ParseException e){
							ReportError re = new ReportError();
							re.setMessage("DateTime format error!");
							re.setSegment(reports[2]);
							parseResult.put(re);
							continue;
						}
						report = "";
						for(int i = 3; i < reports.length; i ++)
							report += (reports[i] + " ");
					}
					if((bullHeader.getCccc().equals("RJTD")||bullHeader.getCccc().equals("VHHH")) && bullHeader.getTt().equals("FX"))
						report = report.replaceAll("T=", "T ");
					reports = report.trim().split("=");
					TyphKey typhKey = null;
					List<TyphEle> typhEles = null;
					Typh typh = null;
					for(int i = 0; i < reports.length; i++){
						reports[i] = reports[i].replaceAll("(\r\n)|\n", " ");
						reports[i] = reports[i].replaceAll("\\s+", " "); 
						typhKey = new TyphKey();
						typhEles = new ArrayList<TyphEle>();
						typhKey.setV_BBB(bullHeader.getBbb());
						typhKey.setV_TT(bullHeader.getTt());
						typhKey.setV_AA(bullHeader.getAa());
						typhKey.setV_II(bullHeader.getIi());
						typhKey.setObservationTime(dataTime);
						// 判断发报中心，根据不同的发报中心进行不同 方式的解码
						String cccc = bullHeader.getCccc();
						reports[i] = reports[i].replaceAll("=", " ");
						if(cccc.equals("BABJ"))
							ret = ProcBABJ(reports[i], typhKey, typhEles);
						else if(cccc.equals("RJTD"))
							ret = ProcRJTD(reports[i], typhKey, typhEles);
						else if(cccc.equals("PGTW"))
							ret = ProcPGTW(reports[i], typhKey, typhEles);
						else if(cccc.equals("RKSL"))
//							if(dataTime.getDate()==11 && dataTime.getHours() == 18)
							ret = ProcRKSL_1(reports[i], typhKey, typhEles);
						else if(cccc.equals("RPMM"))
							ret = ProcRPMM(reports[i], typhKey, typhEles);
						else if(cccc.equals("VHHH"))
							ret = ProcVHHH(reports[i], typhKey, typhEles);
						else if(cccc.equals("EGRR"))
							ret = ProcEGRR(reports[i], typhKey, typhEles);
						else if(cccc.equals("DEMS")){
							System.out.println("DEMS 发报中心报文: " + file);
							ret = ProcDEMS(reports[i], typhKey, typhEles);
						}
						
						else { // 发报中心不在解码范围内
							//	parseResult.setSuccess(true);
//							return parseResult;
							System.out.println(cccc + ": \t" + file);
							continue;
						}
						if(ret == 0 && typhKey.getNumOfForecastEfficiency() != 0){ 
							if(!cccc.equals("EGRR")){ // cccc.equals("EGRR") 在ProcEGRR()添加至parseResult，因为有多个台风的情况
								typh = new Typh();
								typh.setTyphEles(typhEles);
								typh.setTyphKey(typhKey);
								parseResult.put(typh);
								parseResult.put(new ReportInfo<Typh>(typh, headerline + " " + reports[i]));
								parseResult.setSuccess(true);
							}
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Report too short, format error!");
							re.setSegment(reports[i]);
							parseResult.put(re);
						}
					}
				}
		    } catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}  catch (Exception e) {
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
					if(scanner != null){
						scanner.close();
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}

/**
 * 	
 * @Title: ProcDEMS   
 * @Description: 新德里台风解析  
 * @param rep
 * @param typhkey
 * @param typhEles
 * @return int      
 * @throws：
 */
	private int ProcDEMS(String rep, TyphKey typhkey, List<TyphEle> typhEles) {
		Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
		// 要素表条数 （预报时效个数）  V04320_041
		typhkey.setNumOfForecastEfficiency(0);
		// 产品类型  V01398
		Boolean globalModel = Pattern.matches("[\\s\\S]*GLOBAL[\\s\\S]*MODEL[\\s\\S]*", rep);
		Boolean typhonnModel = Pattern.matches("[\\s\\S]*TYPHOON[\\s\\S]*MODEL[\\s\\S]*", rep);
		if(globalModel)
			typhkey.setProductType(1);
		else{
			if(typhonnModel)
				typhkey.setProductType(2);
			else
				typhkey.setProductType(3);
		}
		// 编报中心
		typhkey.setReportCenter("DEMS");
		// 编报中心代码
		typhkey.setReportCenterCode(999); // 北印度洋 编报中心代码，未给定
		
		rep = rep.replaceAll("(\r\n)|\n|\r", " ");
		// 台风名、国内编号、国际编号
		Pattern reg = Pattern.compile("(DEPRESSION|DEEP\\s+DEPRESSION|CYCLONIC\\s+STORM|SEVERE\\s+CYCLONIC\\s+STORM|VERY\\s+SEVERE\\s+CYCLONIC\\s+STORM|SUPER\\s+CYCLONIC\\s+STORM)(\\s+[A-Z0-9a-z|-|\\\\]+)(.*?)(\\s+OVER)");
		Matcher matcher = reg.matcher(rep);
		int start = 0;
		int end = 0;
		Boolean isPrevFound = false;
		String curr = "";
		String sp [];
		String tmp = "";
		// 台风级别，台风名称
		if(matcher.find()){
			isPrevFound = true;
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			typhkey.setTyphLevel(matcher.group(1).trim());
			typhkey.setTyphName(matcher.group(2).trim());
		}
		else{
			ReportError re = new ReportError();
			re.setMessage("Typhoon decode error!");
			re.setSegment(rep);
			parseResult.put(re);
			return -2;
		}
		if(isPrevFound)
			rep = rep.substring(end);
		// 3类报文中的哪一类
		// 以下为台风预报类型
		reg = Pattern.compile("(FORECAST\\s+IS\\s+PREPARED\\s*:\\s+)");
		matcher = reg.matcher(rep);
		int type = 0;
		if(matcher.find()){
			isPrevFound = true;
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			type = 1;
		}
		else{
			// 另外其他两种报文类型 type = 2 或 3
		}
		if(isPrevFound)
			rep = rep.substring(end);
		// 去除乱码
		byte [] bs = rep.getBytes();
		for(int i = 0; i < bs.length; i ++){
			if(bs[i] < 0) // 乱码
				bs[i] = 32;  // 空格符
		}
		try {
			rep = new String(bs, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TyphEle typhEle = new TyphEle();
		typhEle.setForecastEfficiency(0);
		if(type == 1){
			//预报的初始时间的中心位置
			reg = Pattern.compile("(\\s*:\\s+)([0-9]+\\.?[0-9|\\s]+\\s*(N{0,1}|S{0,1}))(\\/)([0-9]+\\.[0-9|\\s|\\.]+\\s*(W|E))");
			if((matcher = reg.matcher(rep.replaceAll("O", "0"))).find()){
				isPrevFound = true;
				start = matcher.start();
				end = matcher.end();
				curr = rep.substring(start, end);
				double lat = defaultF;
				double lon = defaultF;
				tmp = matcher.group(2); // 实况纬度
				tmp = tmp.replaceAll("\\s+", "");
				lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
				lat = getLatOrLon(lat, tmp);
				tmp = matcher.group(5); // 实况经度
				tmp = tmp.replaceAll("\\s+", "");
				try{
					lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
					lon = getLatOrLon(lon, tmp);
				}
				catch (Exception e) {
					lon = 999999;
				}
				typhEle.setLatitude(lat);
				typhEle.setLongtitude(lon);
			}
			else
				isPrevFound = false;
			if(isPrevFound)
				rep = rep.substring(end);
			// 过去6小时的移向（单位：度）,过去6小时的移速（单位：海里/小时）: 330/04KT
			reg = Pattern.compile("(PRESENT\\s+MOVEMENT\\s+\\(DDD\\/FF\\)\\s+PAST\\s+SIX\\s+HOURS\\s*:\\s+)([0-9]+\\.?[0-9]?\\/[0-9]+\\.?[0-9]?)(\\s?KT)");
			if((matcher = reg.matcher(rep)).find()){
				isPrevFound = true;
				start = matcher.start();
				end = matcher.end();
				curr = rep.substring(start, end);
				tmp = matcher.group(2);
				sp = tmp.split("\\/");
				typhEle.setMovingDir(Double.parseDouble(sp[0]));
				typhEle.setMovingSpeed(Double.parseDouble(sp[1]) * 1852.0 / 3600.0); // 海里/小时 转化为 m/s
			}
			else
				isPrevFound = false;
			
			//近中心的最大风速（单位：海里/小时）
			reg = Pattern.compile("(MAX\\s+SUSTAINED\\s+WINDS:\\s+)([0-9]+\\.?[0-9]?)(\\s?KT\\s*,?)");
			if((matcher = reg.matcher(rep)).find()){
				isPrevFound = true;
				start = matcher.start();
				end = matcher.end();
				curr = rep.substring(start, end);
				tmp = matcher.group(2);
				typhEle.setMaxSustainedWind(Double.parseDouble(tmp) * 1852.0 / 3600.0);// 海里/小时 转化为 m/s
			}
			else
				isPrevFound = false;
			if(isPrevFound == true)
				rep = rep.substring(end);
			
			//近中心的极大风速（单位：海里/小时）
			reg = Pattern.compile("^(\\s*GUSTS\\s+)([0-9]+\\.?[0-9]?)(\\s?KT)");
			if((matcher = reg.matcher(rep)).find()){
				isPrevFound = true;
				start = matcher.start();
				end = matcher.end();
				curr = rep.substring(start, end);
				tmp = matcher.group(2);
				typhEle.setGustSpeed(Double.parseDouble(tmp) * 1852.0 / 3600.0);
			}
			else 
				isPrevFound = false;
			
			if(isPrevFound)
				rep = rep.substring(end);
			//最大风圈半径（单位：海里）
			reg = Pattern.compile("^(\\s*RADIUS\\s+OF\\s+MAXIMUM\\s+WIND\\s+)([0-9]+\\.?[0-9]?)(\\s?NM)");
			if((matcher = reg.matcher(rep)).find()){
				isPrevFound = true;
				end = matcher.end();
				tmp = matcher.group(2);
			}
			else
				isPrevFound = false;
			if(isPrevFound)
				rep = rep.substring(end);
//			int num = typhkey.getNumOfForecastEfficiency() + 1;
//			typhkey.setNumOfForecastEfficiency(num);
//			typhEles.add(typhEle);
			elementMap.put(typhEle.getForecastEfficiency(), typhEle);
			// 小时预报
			Pattern pos = Pattern.compile("^(\\s*\\d+Z\\s+)([0-9]+\\.?[0-9|\\s+]+\\s*(N{0,1}|S{0,1}))(\\s*\\/\\s*)([0-9]+\\.[0-9|\\s+\\.]+\\s*(W|E))");
			Pattern maxSustainedWinds = Pattern.compile("^(\\s*MAX\\s+SUSTAINED\\s+WINDS\\s*:\\s+)([0-9]+\\.?[0-9]?)(\\s?KT)(\\s*,?)");
			Pattern gusts = Pattern.compile("^(\\s*GUSTS\\s+)([0-9]+\\.?[0-9]?)(\\s?KT)");
			Pattern windRadius = Pattern.compile("^(\\s*RADIUS\\s+OF\\s+)([0-9]+)(\\s+KT\\s+WINDS\\s*:?)");
			Pattern radiusAzimuth = Pattern.compile("^(\\s*[0-9]+)(\\s?NM\\s+)([A-Za-z]+)(\\s+QUADRANT)");
			reg = Pattern.compile("([0-9]+)(\\s+HRS,\\s+VALID\\s+AT\\s*:\\s+)");
			while((matcher = reg.matcher(rep)).find()){
				typhEle = new TyphEle();
				typhEle.setForecastEfficiency(Integer.parseInt(matcher.group(1)));
				start = matcher.start();
				end = matcher.end();	
				curr = rep.substring(start, end);
				rep = rep.substring(end);
				// 位置
				if((matcher = pos.matcher(rep.replaceAll("O", "0"))).find()){
					end = matcher.end();
					isPrevFound = true;
					double lat = defaultF;
					double lon = defaultF;
					tmp = matcher.group(2); // 实况纬度
					tmp = tmp.replaceAll("\\s+", "");
					if(!tmp.endsWith("N") && !tmp.endsWith("S"))
						tmp = tmp + "N";
					lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
					lat = getLatOrLon(lat, tmp);
					tmp = matcher.group(5); // 实况经度
					tmp = tmp.replaceAll("\\s+", "");
					if(!tmp.endsWith("W") && !tmp.endsWith("E"))
						tmp = tmp + "E";
					try{
						lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
						lon = getLatOrLon(lon, tmp);
					}
					catch (Exception e) {
						lon = 999999;
					}
					typhEle.setLatitude(lat);
					typhEle.setLongtitude(lon);
				}
				else
					isPrevFound = false;
				if(isPrevFound)
					rep = rep.substring(end);
				//最大持续风
				if((matcher = maxSustainedWinds.matcher(rep)).find()){
					end = matcher.end();
					isPrevFound = true;
					tmp = matcher.group(2);
					typhEle.setMaxSustainedWind(Double.parseDouble(tmp) * 1852.0 / 3600.0);// 海里/小时 转化为 m/s
				}
				else 
					isPrevFound = false;
				if(isPrevFound)
					rep = rep.substring(end);
				// 阵风
				if((matcher = gusts.matcher(rep)).find()){
					end = matcher.end();
					isPrevFound = true;
					tmp = matcher.group(2);
					typhEle.setGustSpeed(Double.parseDouble(tmp) * 1852.0 / 3600.0);
				}
				else
					isPrevFound = false;
				if(isPrevFound)
					rep = rep.substring(end);
				// 7级风圈 28KT、8级风圈34KT、10级风圈 50KT、12级风圈 64KT
				double radius = defaultF;
				double azimuth = defaultF;
				while((matcher = windRadius.matcher(rep)).find()){
					int level = Integer.parseInt(matcher.group(2));
					int count = 0;
					end = matcher.end();
					rep = rep.substring(end);
					while((matcher = radiusAzimuth.matcher(rep)).find()){
						end = matcher.end();
						 radius = Integer.parseInt(matcher.group(1).trim()) * 1.852; // 海里转化为公里
						 azimuth = GetWindAzimuth(matcher.group(3).trim());
						 if(level == 28){   // 7级风圈半径
							 if(count == 0){
								 typhEle.setWindCircleRadiusL7_01(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL7_01(azimuth);
							 }
							 else if(count == 1){
								 typhEle.setWindCircleRadiusL7_02(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL7_02(azimuth);
							 }
							 else if(count == 2){
								 typhEle.setWindCircleRadiusL7_03(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL7_03(azimuth);
							 }
							 else if(count == 3){
								 typhEle.setWindCircleRadiusL7_04(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL7_04(azimuth);
							 }
							 count ++;
						 }
						 else if(level == 50){ // 10级风圈半径
							 if(count == 0){
								 typhEle.setWindCircleRadiusL10_01(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL10_01(azimuth);
							 }
							 else if(count == 1){
								 typhEle.setWindCircleRadiusL10_02(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL10_02(azimuth);
							 }
							 else if(count == 2){
								 typhEle.setWindCircleRadiusL10_03(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL10_03(azimuth);
							 }
							 else if(count == 3){
								 typhEle.setWindCircleRadiusL10_04(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL10_04(azimuth);
							 }
							 count ++;
						 }
						 else if(level == 64){ // 12级风圈半径
							 if(count == 0){
								 typhEle.setWindCircleRadiusL12_01(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL12_01(azimuth);
							 }
							 else if(count == 1){
								 typhEle.setWindCircleRadiusL12_02(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL12_02(azimuth);
							 }
							 else if(count == 2){
								 typhEle.setWindCircleRadiusL12_03(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL12_03(azimuth);
							 }
							 else if(count == 3){
								 typhEle.setWindCircleRadiusL12_04(radius);
								 if(azimuth >= 0)
									 typhEle.setAzimuthOfWindBeyondL12_04(azimuth);
							 }
							 count ++;
						 }
						 else{
							// 其它级别的风圈半径
						 }
						 rep = rep.substring(end);
					}
				} // end while 风圈半径
//				num = typhkey.getNumOfForecastEfficiency() + 1;
//				typhEles.add(typhEle);
//				typhkey.setNumOfForecastEfficiency(num);
				elementMap.put(typhEle.getForecastEfficiency(), typhEle);
			}
			// end while loop 小时预报
			typhkey.setNumOfForecastEfficiency(elementMap.size());
			typhEles.addAll(elementMap.values());
		} // end if type == 1, 一种报文类型解析结束
		return 0;
	}

	/**
	 * 解析编报中心BABJ(北京综合预报)发出的报文   
	 * @param rep 待解码报文
	 * @param typhkey 存储解码键表数据
	 * @param typhEles  存储解码要素表数据
	 * @return int  解码成功，返回0；解码失败，返回结果<0  
	 */
	int ProcBABJ(String rep, TyphKey typhkey, List<TyphEle> typhEles){
		int ret = -1;
		Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
		// 要素表条数 （预报时效个数）  V04320_041
		typhkey.setNumOfForecastEfficiency(0);
		// 产品类型  V01398
		Boolean globalModel = Pattern.matches("[\\s\\S]*GLOBAL[\\s\\S]*MODEL[\\s\\S]*", rep);
		Boolean typhonnModel = Pattern.matches("[\\s\\S]*TYPHOON[\\s\\S]*MODEL[\\s\\S]*", rep);
		if(globalModel)
			typhkey.setProductType(1);
		else{
			if(typhonnModel)
				typhkey.setProductType(2);
			else
				typhkey.setProductType(3);
		}
		// 编报中心
		typhkey.setReportCenter("BABJ");
		// 编报中心代码
		typhkey.setReportCenterCode(138);
		// 台风等级
//		typhkey.setTyphLevel(99);
		// 台风名、国内编号、国际编号
		Pattern reg = Pattern.compile("(Tc|TC|TD|TS|STS|TY|STY|SuperTY|SUPERTY|SUPER\\s+TY|Super\\s+TY)(.*)(UTC)");
		Matcher matcher = reg.matcher(rep);
		int start = 0;
		int end = 0;
		Boolean isPrevFound = false;
		String curr = "";
		String sp [];
		if(matcher.find()){
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			typhkey.setTyphLevel(matcher.group(1));
			sp = matcher.group(2).trim().split("\\s+");
			if(sp.length >= 4 && sp[0].trim().length() > 0){
				typhkey.setTyphName(sp[0].trim());
				if(isNumeric(sp[1]))
					typhkey.setInternalCode(Integer.parseInt(sp[1]));
				else if(isNumeric(sp[0]))
					typhkey.setInternalCode(Integer.parseInt(sp[0]));
				if(sp[2].length() >= 6 && isNumeric(sp[2].substring(1, 5)))
					typhkey.setInternationalCode(Integer.parseInt(sp[2].substring(1, 5)));
				else if(sp[2].length() >= 4 && isNumeric(sp[2].substring(1, 3)))
					typhkey.setInternationalCode(Integer.parseInt(sp[2].substring(1, 3)));
			}
			else{
				ReportError re = new ReportError();
				re.setMessage("Typhoon name is empty!");
				re.setSegment(rep);
				parseResult.put(re);
				return -2;
			}
		}
		else {
			ReportError re = new ReportError();
			re.setMessage("Report too short!");
			re.setSegment(rep);
			parseResult.put(re);
			return -2;
		}
		// 时效（实况）、实况纬度、实况经度、实况近中心气压、实况近中心风速（持续风）
		rep = rep.substring(end);
		rep = rep.trim();  // chy: 2019-10-15
		reg = Pattern.compile("(\\d+HR)(.*?HPA)(.*?M/S)");
		matcher = reg.matcher(rep);
		TyphEle typhEle = new TyphEle();
		String tmp;
		double lat = defaultF;
		double lon = defaultF;
		if(matcher.find()){
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end); // 00HR 20.7N 125.2E 955HPA 40M/S
			if((sp = curr.split("\\s+")).length == 5){
				if(isNumeric(tmp = sp[0].substring(0, sp[0].length() - 2)))
					typhEle.setForecastEfficiency(Integer.parseInt(tmp));
				if(isNumeric(tmp = sp[1].substring(0, sp[1].length() - 1)))
					lat = Double.parseDouble(tmp);
				if(isNumeric(tmp = sp[2].substring(0, sp[2].length() - 1)))
					lon = Double.parseDouble(tmp);
				lat = getLatOrLon(lat, sp[1]);
				lon = getLatOrLon(lon, sp[2]);
				typhEle.setLatitude(lat);
				typhEle.setLongtitude(lon);
				if(isNumeric(tmp = sp[3].substring(0, sp[3].length() - 3)))
					typhEle.setCenterPressure(Double.parseDouble(tmp));
				if(isNumeric(tmp = sp[4].substring(0, sp[4].length() - 3)))
					typhEle.setMaxSustainedWind(Double.parseDouble(tmp));
			}
			isPrevFound = true;
		}
		else isPrevFound = false;
		// 7级风圈半径,例如：
		// 30KTS WINDS 100KM NORTHEAST
		// 100KM SOUTHEAST
		// 150KM SOUTHWEST
		// 200KM NORTHWEST
		rep = rep.replaceAll("(\r\n)|\n|\r", " ");
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(30KTS.*?)(\\d+)(KM.*?)(\\d+)(KM.*?)(\\d+)(KM.*?)(\\d+)(KM)");
		matcher = reg.matcher(rep);
		if(matcher.find()){ 
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			typhEle.setWindCircleRadiusL7_01(Double.parseDouble(matcher.group(2)));
			typhEle.setWindCircleRadiusL7_02(Double.parseDouble(matcher.group(4)));
			typhEle.setWindCircleRadiusL7_03(Double.parseDouble(matcher.group(6)));
			typhEle.setWindCircleRadiusL7_04(Double.parseDouble(matcher.group(8)));
			
			typhEle.setAzimuthOfWindBeyondL7_01(45);
			typhEle.setAzimuthOfWindBeyondL7_02(135);
			typhEle.setAzimuthOfWindBeyondL7_03(225);
			typhEle.setAzimuthOfWindBeyondL7_04(315);
			isPrevFound = true;
		}
		else isPrevFound = false;
		// 10级风圈半径 ，格式同7级风圈
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(50KTS.*?)(\\d+)(KM.*?)(\\d+)(KM.*?)(\\d+)(KM.*?)(\\d+)(KM)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			typhEle.setWindCircleRadiusL10_01(Double.parseDouble(matcher.group(2)));
			typhEle.setWindCircleRadiusL10_02(Double.parseDouble(matcher.group(4)));
			typhEle.setWindCircleRadiusL10_03(Double.parseDouble(matcher.group(6)));
			typhEle.setWindCircleRadiusL10_04(Double.parseDouble(matcher.group(8)));
			typhEle.setAzimuthOfWindBeyondL10_01(45);
			typhEle.setAzimuthOfWindBeyondL10_02(135);
			typhEle.setAzimuthOfWindBeyondL10_03(225);
			typhEle.setAzimuthOfWindBeyondL10_04(315);
			isPrevFound = true;
		}
		else isPrevFound = false;
		// 12级风圈半径 ，格式同7级风圈
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(64KTS.*?)(\\d+)(KM.*?)(\\d+)(KM.*?)(\\d+)(KM.*?)(\\d+)(KM)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			typhEle.setWindCircleRadiusL12_01(Double.parseDouble(matcher.group(2)));
			typhEle.setWindCircleRadiusL12_02(Double.parseDouble(matcher.group(4)));
			typhEle.setWindCircleRadiusL12_03(Double.parseDouble(matcher.group(6)));
			typhEle.setWindCircleRadiusL12_04(Double.parseDouble(matcher.group(8)));
			typhEle.setAzimuthOfWindBeyondL12_01(45);
			typhEle.setAzimuthOfWindBeyondL12_02(135);
			typhEle.setAzimuthOfWindBeyondL12_03(225);
			typhEle.setAzimuthOfWindBeyondL12_04(315);
			isPrevFound = true;
		}
		else isPrevFound = false;
		// 热带气旋中心移动方向、移动速度  例如：MOVE WNW 23KM/H
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(MOVE)(.*?)(\\d+KM/H|\\d+km/h|SLOWLY|ALMOST\\s+STATIONARY)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			double azimuth = GetWindAzimuth(matcher.group(2).trim());
			if(azimuth >= 0) typhEle.setMovingDir(azimuth);
			if(typhEle.getMovingDir() != 0){
				tmp = matcher.group(3);
				if(tmp.compareTo("SLOWLY") == 0) typhEle.setMovingSpeed(1);
				else if(isNumeric(tmp = tmp.substring(0, tmp.length() - 4))) 
					typhEle.setMovingSpeed(Double.parseDouble(tmp) * 1000.0 / 3600); // km/h 转换为m/s
				else
					typhEle.setMovingSpeed(0); //静风
			}else if(typhEle.getMovingDir() == 0)
				typhEle.setMovingSpeed(0);
			isPrevFound = true;
		}
		else isPrevFound = false;
//		int n = typhkey.getNumOfForecastEfficiency() + 1;
		// 要素中中加入一条
//		typhkey.setNumOfForecastEfficiency(n);
//		typhEles.add(typhEle);
		elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		// 预报时效 、实况纬度、实况经度、进中心气压、中心风速
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(P.*?HR)(.*?HPA.*?)(M/S)");
		while((matcher = reg.matcher(rep)).find()){
			lat = defaultF;
			lon = defaultF;
			typhEle = new TyphEle();
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			sp = curr.trim().split("\\s+");
			// 预测时效
			tmp = sp[0].substring(1, sp[0].length() -2);
			typhEle.setForecastEfficiency(Integer.parseInt(tmp));
			if(isNumeric(tmp = sp[1].substring(0, sp[1].length() - 1))) // 纬度
				lat = Double.parseDouble(tmp);
			if(isNumeric(tmp = sp[2].substring(0, sp[2].length() - 1))) // 经度
				lon = Double.parseDouble(tmp);
			lat = getLatOrLon(lat, sp[1]);
			lon = getLatOrLon(lon, sp[2]);
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
			// 近中心气压
			tmp = sp[3].substring(0, sp[3].length() - 3);
			if(isNumeric(tmp)) typhEle.setCenterPressure(Double.parseDouble(tmp));
			// 中心持续风速
			tmp = sp[4].substring(0, sp[4].length() - 3);
			if(isNumeric(tmp)) typhEle.setMaxSustainedWind(Double.parseDouble(tmp));
			
			if(Math.abs(typhEle.getLatitude() - defaultF) > 1e-5  || 
					Math.abs(typhEle.getLongtitude() - defaultF) > 1e-5 || 
					Math.abs(typhEle.getCenterPressure() - defaultF) > 1e-5 || 
					Math.abs(typhEle.getMaxSustainedWind() - defaultF) > 1e-5){
//				n = n + 1;
//				typhkey.setNumOfForecastEfficiency(n);
//				typhEles.add(typhEle);
				elementMap.put(typhEle.getForecastEfficiency(), typhEle);
			}
			rep = rep.substring(end);
		}//  end while
		
		typhkey.setNumOfForecastEfficiency(elementMap.size());
		typhEles.addAll(elementMap.values());
		ret = 0;
		return ret;
	}
	/**
	 * 解析编报中心RJTD发出的报文   
	 * @param rep 待解码报文
	 * @param typhkey 存储解码键表数据
	 * @param typhEles  存储解码要素表数据
	 * @return int  解码成功，返回0；解码失败，返回结果<0  
	 */
	int ProcRJTD_1(String rep, TyphKey typhkey, List<TyphEle> typhEles){
		int ret = -1;
		Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
		// 要素表条数 （预报时效个数）  V04320_041
		typhkey.setNumOfForecastEfficiency(0);
		TyphEle typhEle = null;
		// 编报中心
		typhkey.setReportCenter("RJTD");
		// 编报中心代码
		typhkey.setReportCenterCode(134);
		// 台风等级
//		typhkey.setTyphLevel(99);
		
		rep = rep.replaceAll("(\r\n)|\n|\r", " ");
		Boolean isPrevFound = false;
		int end = 0;
		String tmp;
		//国内编号
		Pattern reg = Pattern.compile("(RSMC\\s+GUIDANCE\\s+FOR\\s+FORECAST\\s+NAME.*?)(\\d+)");
		Matcher matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			typhkey.setInternalCode(Integer.parseInt(matcher.group(2)));
		}
		else{
			isPrevFound = false;
//			return -2; // 报文太短或台风编号解析错误
		}
		if(isPrevFound)
			rep = rep.substring(end);
		// 台风名、国际编号
		reg = Pattern.compile("(.*?)(\\s+\\(\\d+\\))");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			if(matcher.group(1).trim().length() > 0)
				typhkey.setTyphName(matcher.group(1).trim());
			else{
				ReportError reportError = new ReportError();
				reportError.setMessage("Typhoon name is empty!");
				reportError.setSegment(rep);
				parseResult.put(reportError);
				return -2;
			}
			tmp = matcher.group(2).trim();
			typhkey.setInternationalCode(Integer.parseInt(tmp.substring(1, tmp.length() - 1)));
		}
		else {
			isPrevFound = false;
			return -2; // 台风名或国际编号解析错误
		}
		if(typhkey.getInternalCode() == (int)defaultF)
			typhkey.setInternalCode(typhkey.getInternationalCode());
		typhEle = new TyphEle();
		typhEle.setForecastEfficiency(0);
		
		//实况时间、实况纬度、实况经度
		double lat = defaultF;
		double lon = defaultF;
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(UTC\\s+)([0-9]+\\.?[0-9]?(N|S))(\\s+)([0-9]+\\.?[0-9]?(W|E))");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			tmp = matcher.group(2); // 实况纬度
			lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lat = getLatOrLon(lat, tmp);
			tmp = matcher.group(5); // 实况经度
			lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lon = getLatOrLon(lon, tmp);
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
		}
		else isPrevFound = false;
		if(isPrevFound)
			rep = rep.substring(end);
		rep = predict(rep, typhkey, typhEle);//实况近中心气压和风速解码
//		int num = typhkey.getNumOfForecastEfficiency() + 1;
//		typhkey.setNumOfForecastEfficiency(num);
//		typhEles.add(typhEle);
		elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		//预报模式
		reg = Pattern.compile("(FORECAST.*?MODEL)");
		// 另外一种模式GLOBAL MODEL不处理
		if(!(matcher = reg.matcher(rep)).find())
			return -2; // 不解码
		end = matcher.end();
		rep = rep.substring(end);
		// 预报时效、未来纬度、经度预报、未来近中心气压、实况实况近中心最大风速（持续风）*(1852.0 / 3600.0)
		reg = Pattern.compile("(T\\s+\\d+\\s+)([0-9]+\\.?[0-9]?(N|S))(\\s+)([0-9]+\\.?[0-9]?(W|E))(\\s+)(\\-?\\+?\\d+HPA)(\\s+)(\\-?\\+?\\d+KT)");
		//(\\s+)(\\-?\\d+HPA)(\\-?\\d+KT)
		double pressure0 = typhEle.getCenterPressure(); // 实况气压
		double windSpeed0 = typhEle.getMaxSustainedWind(); // 实况风速
		while((matcher = reg.matcher(rep)).find()){
			end = matcher.end();
			typhEle = new TyphEle();
			tmp = matcher.group(1).trim();// 预报时效
			typhEle.setForecastEfficiency(Integer.parseInt(tmp.substring(2, tmp.length())));
			tmp = matcher.group(2).trim(); // 纬度预报
			lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lat = getLatOrLon(lat, tmp);
			tmp = matcher.group(5).trim(); // 经度预报
			lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lon = getLatOrLon(lon, tmp);
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
			tmp = matcher.group(8).trim(); // 近中心气压
			typhEle.setCenterPressure( Double.parseDouble(tmp.substring(0, tmp.length() - 3)) + pressure0);
			tmp = matcher.group(10).trim(); // 近中心最大风速（持续风）
			typhEle.setMaxSustainedWind(Double.parseDouble(tmp.substring(0, tmp.length() - 2)) + windSpeed0);
			
//			num += 1;
//			typhkey.setNumOfForecastEfficiency(num);
//			typhEles.add(typhEle);
			elementMap.put(typhEle.getForecastEfficiency(), typhEle);
			
			rep = rep.substring(end);
		}// end while
		typhkey.setNumOfForecastEfficiency(elementMap.size());
		typhEles.addAll(elementMap.values());
		
		ret = 0;
		return ret;
	}
	/**
	 * 解析编报中心RJTD（日本综合预报）发出的报文  
	 * @param rep 待解码报文
	 * @param typhkey 存储解码键表数据
	 * @param typhEles  存储解码要素表数据
	 * @return int  解码成功，返回0；解码失败，返回结果<0  
	 */
	int ProcRJTD(String rep, TyphKey typhkey, List<TyphEle> typhEles){
		int ret = -1;
		Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
		// 要素表条数 （预报时效个数）  V04320_041
		typhkey.setNumOfForecastEfficiency(0);
		TyphEle typhEle = null;
		// 产品类型  V01398
		Boolean globalModel = Pattern.matches("[\\s\\S]*GLOBAL[\\s\\S]*MODEL[\\s\\S]*", rep);
		Boolean typhonnModel = Pattern.matches("[\\s\\S]*TYPHOON[\\s\\S]*MODEL[\\s\\S]*", rep);
		if(globalModel){
			typhkey.setProductType(1);
			ret = ProcRJTD_1(rep, typhkey, typhEles);
			return ret;
		}
		else{
			if(typhonnModel){
				typhkey.setProductType(2);
				ret = ProcRJTD_1(rep, typhkey, typhEles);
				return ret;
			}
			else
				typhkey.setProductType(3);  //这种情况时，进入以下的吃力过程，此时报文类型为WT，以上两种为FX
		}
		// 编报中心
		typhkey.setReportCenter("RJTD");
		// 编报中心代码
		typhkey.setReportCenterCode(134);
		// 台风等级
//		typhkey.setTyphLevel(99);
		rep = rep.replace("(\r\n)|\n|\r", " ");
		int end = 0;
		String tmp;
		Boolean isPrevFound = false;
		// 台风国内编号
		Pattern reg = Pattern.compile("(RSMC\\s+TROPICAL\\s+CYCLONE\\s+ADVISORY\\s+NAME.*?)(\\d+)");
		Matcher matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			typhkey.setInternalCode(Integer.parseInt(matcher.group(2)));
		}
		else{
			isPrevFound = false;
			return -2; // 报文太短或台风编号解析错误
		}
		// 台风名、国际编号
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(.*?)(\\s+\\(\\d+\\))");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			if(matcher.group(1).trim().length() > 0)
				typhkey.setTyphName(matcher.group(1).trim());
			else{
				ReportError reportError = new ReportError();
				reportError.setMessage("Typhoon name is empty!");
				reportError.setSegment(rep);
				parseResult.put(reportError);
				return -2;
			}
			tmp = matcher.group(2).trim();
			typhkey.setInternationalCode(Integer.parseInt(tmp.substring(1, tmp.length() - 1)));
		}
		else {
			isPrevFound = false;
			return -2; // 台风名或国际编号解析错误
		}
		if(typhkey.getInternalCode() == (int)defaultF)
			typhkey.setInternalCode(typhkey.getInternationalCode());
		
		typhEle = new TyphEle();
		typhEle.setForecastEfficiency(0);
		//实况时间、实况纬度、实况经度
		double lat = defaultF;
		double lon = defaultF;
		if(isPrevFound)
			rep = rep.substring(end);
		rep = rep.replace("\r", "");
		reg = Pattern.compile("(.*?UTC\\s+)(\\d+(\\.\\d+)?(N|S))(\\s+)(\\d+(\\.\\d+)?(W|E))");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			tmp = matcher.group(2); // 实况纬度
			lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lat = getLatOrLon(lat, tmp);
			tmp = matcher.group(6); // 实况经度
			lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lon = getLatOrLon(lon, tmp);
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
		}
		else isPrevFound = false;
		if(isPrevFound)
			rep = rep.substring(end);
		rep = predict(rep, typhkey, typhEle);//过去6小时移向、移速、实况近中心气压和风速解码
		// 风圈半径（*1.852） 50KT->10级风圈，风圈方位
		reg = Pattern.compile("(\\s+50KT\\s+)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			rep = rep.substring(end);
			reg = Pattern.compile("^(\\s?[0-9]+.?[0-9]?)(NM\\s+)(.*?\\s+)");
			double radius = defaultF;
			double azimuth = defaultF;
			int count = 0;
			while((matcher = reg.matcher(rep)).find() && count <= 3){ // 风圈半径1~4，风方位角1~4
				count ++;
				end = matcher.end();
				azimuth = GetWindAzimuth(matcher.group(3).trim());
				if(count == 1){
					typhEle.setWindCircleRadiusL10_01(Double.parseDouble(matcher.group(1)) * 1.852); 
					if(azimuth >= 0)//方位1
						typhEle.setAzimuthOfWindBeyondL10_01(azimuth);
					else
						typhEle.setAzimuthOfWindBeyondL10_01(45);
				}
				else if(count == 2){
					typhEle.setWindCircleRadiusL10_02(Double.parseDouble(matcher.group(1))* 1.852);
					if(azimuth >= 0) // 方位2
						typhEle.setAzimuthOfWindBeyondL10_02(azimuth);
					else
						typhEle.setAzimuthOfWindBeyondL10_02(135);
				}
				else if(count == 3){
					typhEle.setWindCircleRadiusL10_03(Double.parseDouble(matcher.group(1))* 1.852);
					if(azimuth >= 0)
						typhEle.setAzimuthOfWindBeyondL10_03(azimuth);
					else
						typhEle.setAzimuthOfWindBeyondL10_03(225);
				}
				else{ 
					typhEle.setWindCircleRadiusL10_04(Double.parseDouble(matcher.group(1))* 1.852);
					if(azimuth >= 0)
						typhEle.setAzimuthOfWindBeyondL10_04(azimuth);
					else
						typhEle.setAzimuthOfWindBeyondL10_04(315);
					
				}
				if(azimuth < 0)
					break;
				rep = rep.substring(end);
			}
			if(count < 2){
				radius = typhEle.getWindCircleRadiusL10_01();
				typhEle.setWindCircleRadiusL10_02(radius);
				typhEle.setWindCircleRadiusL10_03(radius);
				typhEle.setWindCircleRadiusL10_04(radius);
				typhEle.setAzimuthOfWindBeyondL10_01(45);
				typhEle.setAzimuthOfWindBeyondL10_02(135);
				typhEle.setAzimuthOfWindBeyondL10_03(225);
				typhEle.setAzimuthOfWindBeyondL10_04(315);
			}
		}
		else isPrevFound = false;
		// 风圈半径（*1.852）30KT->7级风圈，风圈方位
		reg = Pattern.compile("(30KT\\s+)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			rep = rep.substring(end);
			reg = Pattern.compile("([0-9]+.?[0-9]?)(NM\\s+)([a-zA-Z]+)(\\s+)");
			double radius = defaultF;
			double azimuth = defaultF;
			int count = 0;
//			typhEle.setAzimuthOfWindBeyondL7_01(45);
//			typhEle.setAzimuthOfWindBeyondL7_02(135);
//			typhEle.setAzimuthOfWindBeyondL7_03(225);
//			typhEle.setAzimuthOfWindBeyondL7_04(315);
			while((matcher = reg.matcher(rep)).find() && count <= 3){ // 风圈半径1~4，风方位角1~4
				count ++;
				end = matcher.end();
				azimuth = GetWindAzimuth(matcher.group(3).trim());
				if(count == 1){
					typhEle.setWindCircleRadiusL7_01(Double.parseDouble(matcher.group(1)) * 1.852); 
					if(azimuth >= 0)//方位1
						typhEle.setAzimuthOfWindBeyondL7_01(azimuth);
				}
				else if(count == 2){
					typhEle.setWindCircleRadiusL7_02(Double.parseDouble(matcher.group(1))* 1.852);
					if(azimuth >= 0) // 方位2
						typhEle.setAzimuthOfWindBeyondL7_02(azimuth);
				}
				else if(count == 3){
					typhEle.setWindCircleRadiusL7_03(Double.parseDouble(matcher.group(1))* 1.852);
					if(azimuth >= 0)
						typhEle.setAzimuthOfWindBeyondL7_03(azimuth);
				}
				else{ 
					typhEle.setWindCircleRadiusL7_04(Double.parseDouble(matcher.group(1))* 1.852);
					if(azimuth >= 0)
						typhEle.setAzimuthOfWindBeyondL7_04(azimuth);
					
				}
				rep = rep.substring(end);
			}
			if(count < 2){
				radius = typhEle.getWindCircleRadiusL7_01();
				typhEle.setWindCircleRadiusL7_02(radius);
				typhEle.setWindCircleRadiusL7_03(radius);
				typhEle.setWindCircleRadiusL7_04(radius);
				typhEle.setAzimuthOfWindBeyondL7_01(45);
				typhEle.setAzimuthOfWindBeyondL7_02(135);
				typhEle.setAzimuthOfWindBeyondL7_03(225);
				typhEle.setAzimuthOfWindBeyondL7_04(315);
			}
		}
		else isPrevFound = false;
		
//		int num = typhkey.getNumOfForecastEfficiency() + 1;
//		typhkey.setNumOfForecastEfficiency(num);
//		typhEles.add(typhEle);
		elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		reg = Pattern.compile("(\\d+)(HF\\s+\\d+UTC\\s+)([0-9]+\\.?[0-9]?(N|S))(\\s+)([0-9]+\\.?[0-9]?(W|E))");
		while((matcher = reg.matcher(rep)).find()){
			typhEle = new TyphEle();
			end = matcher.end();
			typhEle.setForecastEfficiency(Integer.parseInt(matcher.group(1))); // 预报时效
			tmp = matcher.group(3); // 预报纬度
			lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lat = getLatOrLon(lat, tmp);
			tmp = matcher.group(6); // 预报经度
			lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lon = getLatOrLon(lon, tmp);
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
			// 未来的移向、移速、近中心气压、风速
			rep = rep.substring(end);
			rep = predict(rep, typhkey, typhEle);
			if(Math.abs(typhEle.getLatitude() - defaultF) > 1e-5 ||
					Math.abs(typhEle.getLongtitude() - defaultF) > 1e-5 ||
					Math.abs(typhEle.getCenterPressure() - defaultF) > 1e-5 || 
					Math.abs(typhEle.getMaxSustainedWind() - defaultF) > 1e-5 ||
					Math.abs(typhEle.getMovingDir() - defaultF) > 1e-5 || 
					Math.abs(typhEle.getMovingSpeed() - defaultF) > 1e-5){
//				num += 1;
//				typhkey.setNumOfForecastEfficiency(num);
//				typhEles.add(typhEle);
				elementMap.put(typhEle.getForecastEfficiency(), typhEle);
			}
		}// end while
		
		typhkey.setNumOfForecastEfficiency(elementMap.size());
		typhEles.addAll(elementMap.values());
		ret = 0;
		return ret;
	}
	
	
	/**
	 * 解析编报中心EGRR(英国全球模式预报)发出的报文   
	 * @param rep  待解码报文
	 * @param typhkey  解码结果键值表对象封装
	 * @param typhEles   解码结果要素表对象封装
	 * @return int      解码成功，返回0；解码失败，返回结果<0 
	 */
	int ProcEGRR(String rep, TyphKey typhkey, List<TyphEle> typhElesO){
		String repO = rep;
		int ret = -1;
		// 要素表条数 （预报时效个数）  V04320_041
		
		typhkey.setNumOfForecastEfficiency(0);
		TyphEle typhEle = null;
//		Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
		// 产品类型  V01398
		Boolean globalModel = Pattern.matches("[\\s\\S]*GLOBAL[\\s\\S]*MODEL[\\s\\S]*", rep);
		Boolean typhonnModel = Pattern.matches("[\\s\\S]*TYPHOON[\\s\\S]*MODEL[\\s\\S]*", rep);
		if(globalModel)
			typhkey.setProductType(1);
		else{
			if(typhonnModel)
				typhkey.setProductType(2);
			else
				typhkey.setProductType(3);
		}
		// 编报中心
		typhkey.setReportCenter("EGRR");
		// 编报中心代码
		typhkey.setReportCenterCode(174);
		// 台风等级
//		typhkey.setTyphLevel(99);
		// 预报初始的时、日、月、年
		Pattern reg = Pattern.compile("(DATA\\s+TIME\\s+)(\\d+)(UTC\\s+)(\\d+\\.\\d+\\.\\d+)");
		//例如： DATA TIME 00UTC 23.08.2004 
		Matcher matcher = reg.matcher(rep);
		int end = 0;
		String tmp;
		int startTime[] = new int[4];
		int endTime[] = new int[4];
		Boolean isPrevFound = false;
		String sp[];
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			startTime[3] = Integer.parseInt(matcher.group(2));
			tmp = matcher.group(4);
			sp = tmp.split("\\.");
			startTime[2] = Integer.parseInt(sp[0]);
			startTime[1] = Integer.parseInt(sp[1]);
			startTime[0] = Integer.parseInt(sp[2]);
		}
		else {
			isPrevFound = true;
			return -2; //预报初始的年\月\日\时解析出错
		}
		
		//国内编号、国际编号 按照默认值赋值，已完成初始化
		// 台风名
		if(isPrevFound)
			rep = rep.substring(end);
//		reg = Pattern.compile("(TYPHOON|TROPICAL\\s+CYCLONE|TROPICAL\\s+STORM|TROPICAL\\s+DEPRESSION)(\\s+[0-9a-zA-Z]+\\s+)");
		while(!rep.isEmpty()){
			TyphKey typhkeyInstance = new TyphKey();
			typhkeyInstance.setV_BBB(typhkey.getV_BBB());
			typhkeyInstance.setV_TT(typhkey.getV_TT());
			typhkeyInstance.setV_AA(typhkey.getV_AA());
			typhkeyInstance.setV_II(typhkey.getV_II());
			typhkeyInstance.setObservationTime(typhkey.getObservationTime());
			typhkeyInstance.setNumOfForecastEfficiency(typhkey.getNumOfForecastEfficiency());
			typhkeyInstance.setProductType(typhkey.getProductType());
			typhkeyInstance.setReportCenter(typhkey.getReportCenter());
			typhkeyInstance.setReportCenterCode(typhkey.getReportCenterCode());
			
			List<TyphEle> typhEles = new ArrayList<>();
			
			Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
			reg = Pattern.compile("(TYPHOON|TROPICAL\\s+CYCLONE|TROPICAL\\s+STORM|TROPICAL\\s+DEPRESSION)(\\s+[0-9a-zA-Z]+\\s+)");
			if((matcher = reg.matcher(rep)).find()){
				isPrevFound = true;
				end = matcher.end();
				if(matcher.group(2).trim().length() > 0)
					typhkeyInstance.setTyphName(matcher.group(2).trim());
				else{
					ReportError reportError = new ReportError();
					reportError.setMessage("Typhoon name is empty!");
					reportError.setSegment(rep);
					parseResult.put(reportError);
					return -2;
				}
			}
			else {
				isPrevFound = false;
				return -2; // 台风名称解析错误
			}
			// 预报时效、未来纬度、未来经度、台风强度
			if(isPrevFound)
				rep = rep.substring(end);
			rep = rep.replaceAll("\r\n|\n|\r", " ");
			reg = Pattern.compile("(\\d+)(UTC\\s+)(\\d+\\.\\d+\\.\\d+)(\\s+[0-9]+.?[0-9]?(N|S))(\\s+[0-9]+.?[0-9]?(W|E))");
			Pattern reg2 = Pattern.compile("(\\d+)(UTC\\s+)(\\d+\\.\\d+\\.\\d+)");
			Pattern strength = Pattern.compile("(\\s+WEAK|MODERATE|INTENSE|STRONG)"); // 风强度
			// 未来趋势
			Pattern trend = Pattern.compile("(\\s+LITTLE\\s+CHANGE|\\s+INTENSIFYING\\s+RAPIDLY|\\s+INTENSIFYING\\s+SLIGHTLY|\\s+WEAKENING\\s+RAPIDLY|\\s+WEAKENING\\s+SLIGHTLY|\\s+)");
			int difHours = 0;
			double lat = defaultF;
			double lon = defaultF;
			
			while((matcher = reg.matcher(rep)).find()){
				end = matcher.end();
				typhEle = new TyphEle();
				// 预报时效
				endTime[3] = Integer.parseInt(matcher.group(1));
				tmp = matcher.group(3);
				sp = tmp.split("\\.");
				endTime[2] = Integer.parseInt(sp[0]);
				endTime[1] = Integer.parseInt(sp[1]);
				endTime[0] = Integer.parseInt(sp[2]);
				difHours = getDifHours(startTime, endTime);
				typhEle.setForecastEfficiency(difHours);
				// 未来的纬度预报
				tmp = matcher.group(4).trim();
				lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1).trim());
				lat = getLatOrLon(lat, tmp);
				// 未来的经度预报
				tmp = matcher.group(6).trim();
				lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1).trim());
				lon = getLatOrLon(lon, tmp);
				typhEle.setLatitude(lat);
				typhEle.setLongtitude(lon);
				
				// 台风强度
				rep = rep.substring(end);
				if((matcher = strength.matcher(rep)).find()){
					isPrevFound = true;
					end = matcher.end();
					tmp = matcher.group(1);
					if(tmp.contains("WEAK")) typhEle.setTyphIntensity(0);
					else if(tmp.contains("MODERATE")) typhEle.setTyphIntensity(1);
					else if(tmp.contains("INTENSE")) typhEle.setTyphIntensity(2);
					else if(tmp.contains("STRONG")) typhEle.setTyphIntensity(3);
					else ; // 初始化的默认值
				}
				else isPrevFound = false;
				// 未来趋势
				if(isPrevFound)
					rep = rep.substring(end);
				if((matcher = trend.matcher(rep)).find()){
					isPrevFound = true;
					end = matcher.end();
					tmp = matcher.group(1);
					if(tmp.contains("LITTLE")) typhEle.setTrend(0);
					else if(tmp.contains("INTENSIFYING RAPIDLY")) typhEle.setTrend(1);
					else if(tmp.contains("INTENSIFYING SLIGHTLY")) typhEle.setTrend(2);
					else if(tmp.contains("WEAKENING RAPIDLY")) typhEle.setTrend(3);
					else if(tmp.contains("WEAKENING SLIGHTLY")) typhEle.setTrend(4);
					else ;// 初始化的默认值
				} 
				else isPrevFound = false;
				if(isPrevFound){
					rep = rep.substring(end);
					rep = rep.trim();
				}
				elementMap.put(typhEle.getForecastEfficiency(), typhEle);
	//			num = typhkey.getNumOfForecastEfficiency() + 1;
	//			typhkey.setNumOfForecastEfficiency(num);
	//			typhEles.add(typhEle);
				if(!rep.substring(0, 20).contains("UTC "))
					break;
				
			}
			
			if(!reg.matcher(rep).find() && (matcher = reg2.matcher(rep)).find() == true){
				end = matcher.end();
				typhEle = new TyphEle();
				// 预报时效
				endTime[3] = Integer.parseInt(matcher.group(1));
				tmp = matcher.group(3);
				sp = tmp.split("\\.");
				endTime[2] = Integer.parseInt(sp[0]);
				endTime[1] = Integer.parseInt(sp[1]);
				endTime[0] = Integer.parseInt(sp[2]);
				difHours = getDifHours(startTime, endTime);
				typhEle.setForecastEfficiency(difHours);
				
				// 未来的纬度预报
				typhEle.setLatitude(defaultF);
				typhEle.setLongtitude(defaultF);//根据韩反馈，此时赋值 999999
				
				// 台风强度
				 typhEle.setTyphIntensity(4); //根据韩反馈，此时赋值 代码4
				// 未来趋势
				 typhEle.setTrend((int)defaultF); //根据韩反馈，此时赋值 999999
				 
				 elementMap.put(typhEle.getForecastEfficiency(), typhEle);
				 isPrevFound = true;
				 if(isPrevFound){
					rep = rep.substring(end);
				}
			}
			
			typhkeyInstance.setNumOfForecastEfficiency(elementMap.size());
			typhEles.addAll(elementMap.values());
			
			if(typhkeyInstance.getNumOfForecastEfficiency() != 0){ 
				Typh typh = new Typh();
				typh.setTyphEles(typhEles);
				typh.setTyphKey(typhkeyInstance);
				parseResult.put(typh);
				parseResult.put(new ReportInfo<Typh>(typh, repO));
				parseResult.setSuccess(true);
			}			
			ret = 0;
		}//end while(!rep.isEmpty())
		return ret;
	}
	/**
	 * 解析编报中心RKSL(韩国综合预报)发出的报文 
	 * @param rep 待解析报文
	 * @param typhkey 存储解析结果中的键表信息
	 * @param typhEles 存储解析结果中的要素表信息
	 * @return int  解析错误，返回<0；解析正确，返回0
	 */
	int ProcRKSL_1(String rep, TyphKey typhkey, List<TyphEle> typhEles){
		int ret = -1;
		Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
		// 要素表条数 （预报时效个数）  V04320_041
		typhkey.setNumOfForecastEfficiency(0);
		TyphEle typhEle = null;
		// 产品类型  V01398
		Boolean globalModel = Pattern.matches("[\\s\\S]*GLOBAL[\\s\\S]*MODEL[\\s\\S]*", rep);
		Boolean typhonnModel = Pattern.matches("[\\s\\S]*TYPHOON[\\s\\S]*MODEL[\\s\\S]*", rep);
		if(globalModel)
			typhkey.setProductType(1);
		else{
			if(typhonnModel)
				typhkey.setProductType(2);
			else
				typhkey.setProductType(3);
		}
		// 编报中心
		typhkey.setReportCenter("RKSL");
		// 编报中心代码
		typhkey.setReportCenterCode(140);
		// 台风等级
//		typhkey.setTyphLevel(99);
		rep = rep.replaceAll("(\r\n)|\n|\r", " ");
		Pattern reg = Pattern.compile("(KMA\\s+TROPICAL\\s+CYCLONE\\s+ADVISORY\\s+NO\\.\\s+\\d+\\s+NAME)(.*?)(\\d+)(\\s+.*?)(\\s+)");
		Matcher matcher = reg.matcher(rep);
		int end = 0;
		String tmp;
		Boolean isPrevFound = false;
		// 台风名、 国内编号、国际编号
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			tmp = matcher.group(3).trim(); // 台风国际编号
			typhkey.setInternalCode(Integer.parseInt(tmp));
			typhkey.setInternationalCode(Integer.parseInt(tmp));
			if(matcher.group(4).trim().length() > 0)
				typhkey.setTyphName( matcher.group(4).trim()); // 台风名
			else{
				ReportError reportError = new ReportError();
				reportError.setMessage("Typhoon name is empty!");
				reportError.setSegment(rep);
				parseResult.put(reportError);
				return -2;
			}
		}
		else{
			isPrevFound = false;
			return -2; //报文太短、或者台风名、台风编号解码错误
		}
		// 实况的日、实况纬度、实况经度
		if(isPrevFound)
			rep = rep.substring(end);
		double lat = defaultF;
		double lon = defaultF;
		reg = Pattern.compile("(ANALYSIS\\s+POSITION\\s+\\d+UTC\\s+)([0-9]+\\.?[0-9]?(N|S))(\\s+)([0-9]+\\.?[0-9]?(W|E))");
		typhEle = new TyphEle();
		typhEle.setForecastEfficiency(0); // 预报时效
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			tmp = matcher.group(2).trim(); //实况纬度
			lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lat = getLatOrLon(lat, tmp);
			tmp = matcher.group(5).trim();// 实况经度
			lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lon = getLatOrLon(lon, tmp);
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
		}
		else{
			isPrevFound = false;
			return -2; //报文太短，或实况纬度、经度解码错误
		}
		// 移向
		if(isPrevFound)
			rep = rep.substring(end);
		double azimuth = defaultF;
		reg = Pattern.compile("(MOVEMENT\\s+)(.*?)(\\s+)"); // 移向
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			azimuth = GetWindAzimuth(matcher.group(2).trim()); // 移向
			if(azimuth < 0) azimuth = defaultF;
			typhEle.setMovingDir(azimuth);
		}
		else{
			isPrevFound = false;
		}
		// 移速
		if(Math.abs(azimuth - 0.0) < 0.001)
			typhEle.setMovingSpeed(0);
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("([0-9]+\\.?[0-9]?)(KT)"); // 移速
		if((matcher = reg.matcher(rep)).find()){
			end = matcher.end();
			isPrevFound = true;
			typhEle.setMovingSpeed(Double.parseDouble(matcher.group(1))*(1852.0 / 3600.0)); //单位转换
		}
		else {
			isPrevFound = false;
		}
		// 实况近中心气压、实况近中心最大风速 *（1852/3600）
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(PRES.*?)(\\s+)([0-9]+\\.?[0-9]?)(HPA\\s+)([0-9]+\\.?[0-9]?)(KT)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			typhEle.setCenterPressure(Double.parseDouble(matcher.group(3)));
			typhEle.setMaxSustainedWind(Double.parseDouble(matcher.group(5)) * (1852.0 / 3600.0));
		}
		else isPrevFound = false;
//		int num = typhkey.getNumOfForecastEfficiency() + 1;
//		typhkey.setNumOfForecastEfficiency(num);
//		typhEles.add(typhEle);
		elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		// 预报时效、未来的纬度预报、未来的经度预报
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(\\s+)(\\d+)(HR\\s+POS.*?UTC\\s+)([0-9]+\\.?[0-9]?(N|S))(\\s+)([0-9]+\\.?[0-9]?(W|E))");
		Pattern centPresAndSpeed = Pattern.compile("(PRES.*?)(\\s+)([0-9]+\\.?[0-9]?)(HPA\\s+)([0-9]+\\.?[0-9]?)(KT)");
		while((matcher = reg.matcher(rep)).find()){
			typhEle = new TyphEle();
			end = matcher.end();
			typhEle.setForecastEfficiency(Integer.parseInt(matcher.group(2))); // 预报时效
			tmp = matcher.group(4); // 未来纬度预报
			lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lat = getLatOrLon(lat, tmp);
			tmp = matcher.group(7); // 未来经度预报
			lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lon = getLatOrLon(lon, tmp);
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
			// 未来的近中心气压、未来近中心最大风速 *（1852/3600）
			rep = rep.substring(end);
			if((matcher = centPresAndSpeed.matcher(rep)).find()){
				isPrevFound = true;
				end = matcher.end();
				typhEle.setCenterPressure(Double.parseDouble(matcher.group(3)));
				typhEle.setMaxSustainedWind(Double.parseDouble(matcher.group(5)) * (1852.0 / 3600.0));
			}
			else isPrevFound = false;
			if(isPrevFound)
				rep = rep.substring(end);
//			num = typhkey.getNumOfForecastEfficiency() + 1;
//			typhkey.setNumOfForecastEfficiency(num);
//			typhEles.add(typhEle);
			elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		}
		typhkey.setNumOfForecastEfficiency(elementMap.size());
		typhEles.addAll(elementMap.values());
		ret = 0;
		return ret;
	}
	/**
	 * 解析编报中心VHHH(香港综合预报)发出的报文  
	 * @param rep 待解析报文
	 * @param typhkey 存储解析结果中的键表信息
	 * @param typhEles 存储解析结果中的要素表信息
	 * @return int  解析错误，返回<0；解析正确，返回0
	 */
	@SuppressWarnings("deprecation")
	int ProcVHHH(String rep, TyphKey typhkey, List<TyphEle> typhEles){
		int ret = -1;
		Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
		// 要素表条数 （预报时效个数）  V04320_041
		typhkey.setNumOfForecastEfficiency(0);
		TyphEle typhEle = null;
		// 产品类型  V01398
		Boolean globalModel = Pattern.matches("[\\s\\S]*GLOBAL[\\s\\S]*MODEL[\\s\\S]*", rep);
		Boolean typhonnModel = Pattern.matches("[\\s\\S]*TYPHOON[\\s\\S]*MODEL[\\s\\S]*", rep);
		if(globalModel)
			typhkey.setProductType(1);
		else{
			if(typhonnModel)
				typhkey.setProductType(2);
			else
				typhkey.setProductType(3);
		}
		// 编报中心
		typhkey.setReportCenter("VHHH");
		// 编报中心代码
		typhkey.setReportCenterCode(110);
		// 台风等级
//		typhkey.setTyphLevel(99);
		// 实况的日、时、分
		rep = rep.replaceAll("(\r\n)|\n|\r", " ");
		int startTime[] = new int[4];
		int endTime[] = new int[4];
		String tmp;
		Boolean isPrevFound = false;
		Pattern reg = Pattern.compile("(TROPI.*?WARN.*?AT)(\\s+)(\\d+)(\\s+UTC)");
		Matcher matcher = reg.matcher(rep);
		int end = 0;
		if(matcher.find()){
			end = matcher.end();
			isPrevFound = true;
			tmp = matcher.group(3);
			if(tmp.length() == 6){
				startTime[2] = Integer.parseInt(tmp.substring(0, 2)); // 日
				startTime[3] = Integer.parseInt(tmp.substring(2, 4)); // 时
				startTime[0] = typhkey.getObservationTime().getYear() + 1900;
				startTime[1] = typhkey.getObservationTime().getMonth() + 1;
				typhkey.getObservationTime().setDate(startTime[2]);
				typhkey.getObservationTime().setHours(startTime[3]);
				typhkey.getObservationTime().setMinutes(Integer.parseInt(tmp.substring(4)));
			}
			else{
				return -2;// 实况的日、时、分解析错误
			}
		}
		else{
			isPrevFound = false;
			return -2;
		}
		// 台风名、 国内编号、国际编号
//		if(isPrevFound)
//			rep = rep.substring(end);
		reg = Pattern.compile("(.*?)(\\s+\\(\\d+\\))");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			tmp = matcher.group(1);
			if(tmp != null && !tmp.isEmpty()){
				String sp [] = tmp.split("\\s+");
				if(sp.length >= 2 && sp[sp.length - 1].trim().length() > 0) // 至少包含台风级别和台风名称两段
					typhkey.setTyphName(sp[sp.length - 1].trim()); 
				else 
					return -2; // 台风名解析错误
			}
			else
				return -2; // 台风名解析错误
			tmp = matcher.group(2);
			int i1 = tmp.indexOf("(");
			int i2 = tmp.indexOf(")");
			typhkey.setInternationalCode(Integer.parseInt(tmp.substring(i1 + 1, i2)));
			if(typhkey.getInternalCode() == 999999)
				typhkey.setInternalCode(typhkey.getInternationalCode());
			
		}
		else{
			isPrevFound = false;
			return -2;
		}
		// 实况近中心气压
		if(isPrevFound)
			rep = rep.substring(end);
		typhEle = new TyphEle();
		typhEle.setForecastEfficiency(0);
		reg = Pattern.compile("(CENT.*?PRES.*?)(\\d+)(.*?HECTOP)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			tmp = matcher.group(2);
			typhEle.setCenterPressure(Double.parseDouble(tmp));
		}
		else
			isPrevFound = false;
		// 实况纬度、 实况经度
		double lat = defaultF;
		double lon = defaultF;
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(DEGREES\\s+(SOUTH|NORTH)\\s+\\()([0-9]+\\.?[0-9]?\\s+N|S)(\\).*?DEGREES\\s+(EAST|WEST)\\s+\\()([0-9]+\\.?[0-9]?\\s+E|W)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			tmp = matcher.group(3);
			lat = Double.parseDouble(tmp.split("\\s+")[0]);
			lat = getLatOrLon(lat, tmp);  // 实况纬度
			
			tmp = matcher.group(6);
			lon = Double.parseDouble(tmp.split("\\s+")[0]);
			lon = getLatOrLon(lon, tmp);
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
		}
		else
			isPrevFound = false;
		// 未来24小时移向
		double azimuth = defaultF;
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(MOVE)(.*?)(AT)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			azimuth = GetWindAzimuth(matcher.group(2));
			if(azimuth < 0) azimuth = defaultF;
			typhEle.setMovingDir(azimuth); // 未来移向
			if(Math.abs(azimuth - 0.0) < 0.0001)  // 如果azimuth为0
				typhEle.setMovingSpeed(0);// 未来移速
		}
		else
			isPrevFound = false;
		//未来24小时移速（*1852/3600）
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(.*?)([0-9]+\\.?[0-9]?)(\\s+KNOTS)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			typhEle.setMovingSpeed(Double.parseDouble(matcher.group(2)) * (1852.0 / 3600.0));//单位换算
		}
		else
			isPrevFound = false;
	    //实况近中心最大风速（持续风）（*1852/3600）
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(MAX.*?CENT.*?BE\\s+)(\\d+)(\\s+KNOTS)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			typhEle.setMaxSustainedWind(Double.parseDouble(matcher.group(2)) * (1852.0 / 3600.0));
		}
		else
			isPrevFound = false;
		// 风圈半径（*1.852）
		// 33表示7级风
		if(isPrevFound)
			rep = rep.substring(end);
		double radius = defaultF;
		reg = Pattern.compile("(RADIUS.*?33\\s+KNOT\\s+WINDS\\s+)(\\d+)(.*?MILES)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			typhEle.setWindCircleRadiusL7_01(Double.parseDouble(matcher.group(2)) * 1.852);
			radius = typhEle.getWindCircleRadiusL7_01();
			typhEle.setWindCircleRadiusL7_02(radius);
			typhEle.setWindCircleRadiusL7_03(radius);
			typhEle.setWindCircleRadiusL7_04(radius);
			typhEle.setAzimuthOfWindBeyondL7_01(45);
			typhEle.setAzimuthOfWindBeyondL7_02(135);
			typhEle.setAzimuthOfWindBeyondL7_03(225);
			typhEle.setAzimuthOfWindBeyondL7_04(315);
		}
		else isPrevFound = false;
		// 47表示10级风
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(RADIUS.*?47\\s+KNOT\\s+WINDS\\s+)(\\d+)(.*?MILES)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end= matcher.end();
			typhEle.setWindCircleRadiusL10_01(Double.parseDouble(matcher.group(2)) * 1.852);
			radius = typhEle.getWindCircleRadiusL10_01();
			typhEle.setWindCircleRadiusL10_02(radius);
			typhEle.setWindCircleRadiusL10_03(radius);
			typhEle.setWindCircleRadiusL10_04(radius);
			typhEle.setAzimuthOfWindBeyondL10_01(45);
			typhEle.setAzimuthOfWindBeyondL10_02(135);
			typhEle.setAzimuthOfWindBeyondL10_03(225);
			typhEle.setAzimuthOfWindBeyondL10_04(315);
		}
		else isPrevFound = false;
		// 63表示12级风
		if(isPrevFound) 
			rep = rep.substring(end);
		reg = Pattern.compile("(RADIUS.*?63\\s+KNOT\\s+WINDS\\s+)(\\d+)(.*?MILES)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			typhEle.setWindCircleRadiusL12_01(Double.parseDouble(matcher.group(2)) * 1.852);
			radius = typhEle.getWindCircleRadiusL12_01();
			typhEle.setWindCircleRadiusL12_02(radius);
			typhEle.setWindCircleRadiusL12_03(radius);
			typhEle.setWindCircleRadiusL12_04(radius);
			typhEle.setAzimuthOfWindBeyondL12_01(45);
			typhEle.setAzimuthOfWindBeyondL12_02(135);
			typhEle.setAzimuthOfWindBeyondL12_03(225);
			typhEle.setAzimuthOfWindBeyondL12_04(315);
		}
		else isPrevFound = false;
		
//		int num = typhkey.getNumOfForecastEfficiency() + 1;
//		typhkey.setNumOfForecastEfficiency(num);
//		typhEles.add(typhEle);
		elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		// 预报时效、未来的纬度预报、未来的经度预报、未来近中心风速（持续风）预报 (*（1852/3600）)
		if(isPrevFound) 
			rep = rep.substring(end);
		reg = Pattern.compile("(FORECAST\\s+POSITION.*?AT\\s+)(\\d+)(\\s+UTC)");
		Pattern latlon = Pattern.compile("(DEGREES\\s+(SOUTH|NORTH)\\s+\\()([0-9]+\\.?[0-9]?\\s+N|S)(\\).*?DEGREES\\s+(EAST|WEST)\\s+\\()([0-9]+\\.?[0-9]?\\s+E|W)");
		Pattern windSpeed = Pattern.compile("(MAXIMUM\\s+WINDS\\s+)(\\d+)(\\s+KNOTS)"); // 中心风速（持续风预报）
		int difHours = 0;
		while((matcher = reg.matcher(rep)).find()){
			end = matcher.end();
			typhEle = new TyphEle();
			//预报时效
			if((tmp = matcher.group(2)).length() == 6){
				endTime[2] = Integer.parseInt(tmp.substring(0, 2)); // 日
				endTime[3] = Integer.parseInt(tmp.substring(2, 4)); // 时
				difHours = getDifHours(startTime, endTime);
				if(difHours >= 0)
					typhEle.setForecastEfficiency(difHours);
			}
			rep = rep.substring(end);
		    // 未来纬度、经度
			lat = defaultF;
			lon = defaultF;
			if((matcher = latlon.matcher(rep)).find()){
				isPrevFound = true;
				end = matcher.end();
				tmp = matcher.group(3);
				lat = Double.parseDouble(tmp.split("\\s+")[0]);
				lat = getLatOrLon(lat, tmp);
				tmp = matcher.group(6);
				lon = Double.parseDouble(tmp.split("\\s+")[0]);
				lon = getLatOrLon(lon, tmp);
				typhEle.setLatitude(lat);
				typhEle.setLongtitude(lon);
			}
			else isPrevFound = false;
		// 近中心风速（持续风）预报
			if(isPrevFound) 
				rep = rep.substring(end);
			if((matcher = windSpeed.matcher(rep)).find()){
				isPrevFound = true;
				end = matcher.end();  
				typhEle.setMaxSustainedWind(Double.parseDouble(matcher.group(2)) * (1852.0 / 3600.0));//单位换算 *（1852/3600）
			}
			else isPrevFound = false;
//			num = typhkey.getNumOfForecastEfficiency() + 1;
//			typhkey.setNumOfForecastEfficiency(num);
//			typhEles.add(typhEle);
			elementMap.put(typhEle.getForecastEfficiency(), typhEle);
			if(isPrevFound) 
				rep = rep.substring(end);
		}
		typhkey.setNumOfForecastEfficiency(elementMap.size());
		typhEles.addAll(elementMap.values());
		ret = 0;
		return ret;
	}
	
	/**
	 * 解析编报中心PGTW(美国台风联合警报中心综合预报)发出的报文   
	 * @param rep 待解码报文
	 * @param typhkey 存储解码键表数据
	 * @param typhEles  存储解码要素表数据
	 * @return int  解码成功，返回0；解码失败，返回结果<0  
	 */
	int ProcPGTW(String rep, TyphKey typhkey, List<TyphEle> typhEles){
		int ret = -1;
		// 要素表条数 （预报时效个数）  V04320_041
		typhkey.setNumOfForecastEfficiency(0);
		TyphEle typhEle = null;
		Map<Integer, TyphEle> elementMap = new HashMap<>();
		rep = rep.replace("\r", "");
		// 产品类型  V01398
		Boolean globalModel = Pattern.matches("[\\s\\S]*GLOBAL[\\s\\S]*MODEL[\\s\\S]*", rep);
		Boolean typhonnModel = Pattern.matches("[\\s\\S]*TYPHOON[\\s\\S]*MODEL[\\s\\S]*", rep);
		if(globalModel)
			typhkey.setProductType(1);
		else{
			if(typhonnModel)
				typhkey.setProductType(2);
			else
				typhkey.setProductType(3);
		}
		// 编报中心
		typhkey.setReportCenter("PGTW");
		// 编报中心代码
		typhkey.setReportCenterCode(158);
		// 台风等级
//		typhkey.setTyphLevel(99);
		// 台风名、 国内编号、国际编号
		Pattern reg = Pattern.compile("(SUPER\\s+TYPHOON|TROPICAL|TYPHOON)(.*?\\(.*?\\)\\sWARNING\\sNR)");
		Matcher matcher = reg.matcher(rep);
		int start = 0;
		int end = 0;
		String tmp;
		Boolean isPrevFound = false;
		String curr = ""; 
		if(matcher.find()){
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			isPrevFound = true;
			int i1 = curr.indexOf("(");
			int i2 = curr.lastIndexOf(")");
			if(i1 >= 0 && i2 >= 0 && i2 - i1 > 1 && curr.substring(i1 + 1, i2).trim().length() > 0)
				typhkey.setTyphName(curr.substring(i1 + 1, i2).trim()); // 台风名
			else{
				ReportError reportError = new ReportError();
				reportError.setMessage("Typhoon name is empty!");
				reportError.setSegment(rep);
				parseResult.put(reportError);
				return -2;
			}
		}
		else{
			isPrevFound = false;
			ReportError re = new ReportError();
			re.setMessage("Typhoon name is empty!");
			re.setSegment(rep);
			parseResult.put(re);
			return -2;
		}
		typhkey.setInternalCode((int)defaultF); // 国内编号
		typhkey.setInternationalCode((int)defaultF);  // 国际编号
		// 时况的日、时、分|实况纬度|实况经度
		double lat = defaultF;
		double lon = defaultF;
		typhEle = new TyphEle();
		typhEle.setForecastEfficiency(0); // 预报时效
		rep = rep.substring(end);
		rep = rep.replace("\r", "");
		reg = Pattern.compile("(WARNING\\s+POSITION.*?NEAR\\s?)([0-9]+\\.?[0-9]?(N|S))(.*?)([0-9]+\\.?[0-9]?(W|E))");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			start = matcher.start();
			end = matcher.end();
			curr = rep.substring(start, end);
			tmp = matcher.group(2);
			lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lat = getLatOrLon(lat, tmp);
			tmp = matcher.group(5);
			lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lon = getLatOrLon(lon, tmp);
			typhEle.setLatitude(lat); // 实况纬度
			typhEle.setLongtitude(lon); // 实况经度
		}
		else{
			isPrevFound = false;
		}
		// 未来移向、未来移速(过去6小时移向，过去6小时移速)
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(MOVEMENT\\s+PAST\\s+SIX\\s+HOURS\\s-\\s)([0-9]+\\.?[0-9]?)(\\s+DEGREES\\s+AT\\s+)([0-9]+\\.?[0-9]?)(\\s+KTS)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			typhEle.setMovingDir(Double.parseDouble(matcher.group(2)));
			typhEle.setMovingSpeed(Double.parseDouble(matcher.group(4)) * 1852.0 / 3600.0); //转化为m/s
		}
		else
			isPrevFound = false;
		// 中心气压 
		typhEle.setCenterPressure(defaultF);
		// 近中心持续风速、阵风速、7、10、12级风圈半径1~4、方位角1~4
		if(isPrevFound) 
			rep = rep.substring(end);
		rep = windRadiusAndAzimuth(rep, typhkey, typhEle);
//		int numOfForecastEfficiency = typhkey.getNumOfForecastEfficiency() + 1;
//		typhkey.setNumOfForecastEfficiency(numOfForecastEfficiency);
//		typhEles.add(typhEle);
		elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		// 预报时效、未来的纬度预报、未来的经度预报、未来近中心
//		reg = Pattern.compile("(FORECAST)");
//		if((matcher = reg.matcher(rep)).find()){
//			isPrevFound = true;
//			start = matcher.start();
//			end = matcher.end();
//		}
//		else isPrevFound = false;
//		if(isPrevFound)
//			rep = rep.substring(end);
		reg = Pattern.compile("([0-9]+)(\\s+HRS,\\s+VALID\\s+AT:.*?)([0-9]+\\.?[0-9]?(N|S))(.*?)([0-9]+\\.?[0-9]?(W|E))");
		Pattern moveDir = Pattern.compile("(VECTOR\\s+TO\\s+)(\\d+)(\\s+HR\\s+POSIT:\\s+)([0-9]+.?[0-9]?)(\\s+DEG/\\s+)");
		Pattern moveSpeed = Pattern.compile("^([0-9]+\\.?[0-9]?)(\\s+KTS)");
		while((matcher = reg.matcher(rep)).find()){
			lat = defaultF;
			lon = defaultF;
			start = matcher.start();
			end = matcher.end();
			typhEle = new TyphEle();
			typhEle.setForecastEfficiency(Integer.parseInt(matcher.group(1)));
			tmp = matcher.group(3);
			lat = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lat = getLatOrLon(lat, tmp);
			tmp = matcher.group(6);
			lon = Double.parseDouble(tmp.substring(0, tmp.length() - 1));
			lon = getLatOrLon(lon, tmp);
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
			
			// 近中心持续风速、阵风速、7、10、12级风圈半径1~4、方位角1~4
			rep = rep.substring(end);
			rep = windRadiusAndAzimuth(rep, typhkey, typhEle);
			
			if((matcher = moveDir.matcher(rep)).find()){
				isPrevFound = true;
				start = matcher.start();
				end  = matcher.end();
				typhEle.setMovingDir(Double.parseDouble(matcher.group(4))); // 未来移向
			}
			else
				isPrevFound = false;
			if(isPrevFound)
				rep = rep.substring(end);
			if((matcher = moveSpeed.matcher(rep)).find()){
				isPrevFound = true;
				start = matcher.start();
				end  = matcher.end();
				typhEle.setMovingSpeed(Double.parseDouble(matcher.group(1)) * 1852.0 / 3600.0); // 未来移速（持续风）
			}
			else{
				if(tmp.length() > 20)
					tmp = rep.substring(0, 20);
				else
					tmp = rep;
				if(!tmp.contains("REMARKS:") && tmp.contains("SLOWLY"))
					typhEle.setMovingSpeed(1);
				else if(!tmp.contains("REMARKS:") && tmp.contains("ALMOST\\s+STATIONARY"))
					typhEle.setMovingSpeed(0);
				isPrevFound = false;
			}
			if(Math.abs(typhEle.getLatitude() - defaultF) > 1e-5 || 
			   Math.abs(typhEle.getLongtitude() - defaultF) > 1e-5 ||
			   Math.abs(typhEle.getMaxSustainedWind() - defaultF) > 1e-5 || 
			   Math.abs(typhEle.getGustSpeed() - defaultF) > 1e-5 ||
			   Math.abs(typhEle.getMovingDir() - defaultF) > 1e-5 || 
			   Math.abs(typhEle.getMovingSpeed() - defaultF) > 1e-5){
//				numOfForecastEfficiency = typhkey.getNumOfForecastEfficiency() + 1;
//				typhkey.setNumOfForecastEfficiency(numOfForecastEfficiency);
//				typhEles.add(typhEle);
				elementMap.put(typhEle.getForecastEfficiency(), typhEle);
			}
			if(isPrevFound)
				rep = rep.substring(end);
		}
		typhkey.setNumOfForecastEfficiency(elementMap.size());
		typhEles.addAll(elementMap.values());
		ret = 0;
		return ret;
	}
	
	/**
	 * 解析编报中心RPMM(菲律宾综合预报)发出的报文  
	 * @param rep 待解码报文
	 * @param typhkey 存储解码键表数据
	 * @param typhEles  存储解码要素表数据
	 * @return int  解码成功，返回0；解码失败，返回结果<0  
	 */
	@SuppressWarnings("deprecation")
	int ProcRPMM(String rep, TyphKey typhkey, List<TyphEle> typhEles){
		int ret = -1;
		Map<Integer, TyphEle> elementMap = new HashMap<Integer, TyphEle>();
		// 要素表条数 （预报时效个数）  V04320_041
		typhkey.setNumOfForecastEfficiency(0);
		TyphEle typhEle = null;
		// 产品类型  V01398
		Boolean globalModel = Pattern.matches("[\\s\\S]*GLOBAL[\\s\\S]*MODEL[\\s\\S]*", rep);
		Boolean typhonnModel = Pattern.matches("[\\s\\S]*TYPHOON[\\s\\S]*MODEL[\\s\\S]*", rep);
		if(globalModel)
			typhkey.setProductType(1);
		else{
			if(typhonnModel)
				typhkey.setProductType(2);
			else
				typhkey.setProductType(3);
		}
		// 编报中心
		typhkey.setReportCenter("RPMM");
		// 编报中心代码
		typhkey.setReportCenterCode(111);
		// 台风等级
//		typhkey.setTyphLevel(99);
		
		// 实况的月、日、时、分，台风名，台风国际编号
		Pattern reg = Pattern.compile("(AT\\s+)(\\d+)(\\s+)(\\d+)(.*?)(TYPHO|TROPI)");
		Matcher matcher = reg.matcher(rep);
		int end = 0;
		String tmp;
		Boolean isPrevFound;
		//开始时间、结束时间，年、月、日、时
		int startTime[] = new int[4];
		int endTime[] = new int[4];
		if(matcher.find()){
			end = matcher.end();
			isPrevFound = true;
			if((tmp = matcher.group(2)).length() == 4){
				startTime[3] = Integer.parseInt(tmp.substring(0, 2));
			}
			startTime[2] = Integer.parseInt(matcher.group(4));
			startTime[1] = getMonth(matcher.group(5));
			startTime[0] = typhkey.getObservationTime().getYear() + 1900;
		}
		else{
			isPrevFound = false;
			ReportError re = new ReportError();
			re.setMessage("Report too short!");
			re.setSegment(rep);
			parseResult.put(re);
			return -2;
		}
		// 台风名、 国内编号、国际编号
		rep = rep.replaceAll("(\r\n)|\n|\r", " ");
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(\\{.*?\\})(\\s?)(\\(\\d+\\))");
		Pattern reg2 = Pattern.compile("(\\(.*?\\))(\\s?)(\\(\\d+\\))");
		if((matcher = reg.matcher(rep)).find() || (matcher = reg2.matcher(rep)).find()){
			end = matcher.end();
			isPrevFound = true;
			tmp = matcher.group(1);
			if(tmp.substring(1, tmp.length() - 1).trim().length() > 0)
				typhkey.setTyphName(tmp.substring(1, tmp.length() - 1).trim()); //台风名
			else{
				ReportError reportError = new ReportError();
				reportError.setMessage("Typhoon name is empty!");
				reportError.setSegment(rep);
				parseResult.put(reportError);
				return -2;
			}
			tmp = matcher.group(3); 
			typhkey.setInternalCode(Integer.parseInt(tmp.substring(1, 5))); //国内编号
			typhkey.setInternationalCode(typhkey.getInternalCode()); // 国际编号
		}
		else {
			isPrevFound = false;
			ReportError re = new ReportError();
			re.setMessage("Report too short!");
			re.setSegment(rep);
			parseResult.put(re);
			return -2;
		}
		// 实况纬度、实况经度
		typhEle = new TyphEle();  //初始化台风要素对象
		typhEle.setForecastEfficiency(0);
		double lat = defaultF;
		double lon = defaultF;
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(AT\\s+)(.*?)(((NORTH)|(SOUTH))(.*?)((EAST)|(WEST)))");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			lat = transPhraseToRealNumber(matcher.group(2));
			lon = transPhraseToRealNumber(matcher.group(3).replaceAll("(EAST)|(SOUTH)|(WEST)|(NORTH)", ""));
			lat = getLatOrLon(lat, matcher.group(3));
			lon = getLatOrLon(lon, matcher.group(3));
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
		}
		else{
			isPrevFound = false;
			ReportError re = new ReportError();
			re.setMessage("Report too short!");
			re.setSegment(rep);
			parseResult.put(re);
			return -2;
		}
		//未来移向、未来移速
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(MOVE\\s+)(.*?)(AT\\s+)(.*?)(M.*?P.*?S.*?)");
		if((matcher = reg.matcher(rep)).find()){
			end = matcher.end();
			isPrevFound = true;
			double movDir = GetWindAzimuth(matcher.group(2).replaceAll("\\s+", ""));
			if(movDir > 0)
				typhEle.setMovingDir(movDir);
			if(movDir == 0)
				typhEle.setMovingSpeed(0);
			double movSpeed = transPhraseToRealNumber(matcher.group(4));
			if(movSpeed >= 0)
				typhEle.setMovingSpeed(movSpeed);
		}
		else
			isPrevFound = false;
		// 实况近中心气压
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(CENT.*?)(PRESSURE\\s+)(.*?)(HECTOP)");
		if((matcher = reg.matcher(rep)).find()){
			end = matcher.end();
			isPrevFound = true;
			double press = transPhraseToRealNumber(matcher.group(3));
			if(press <= 0) press = defaultF;
			typhEle.setCenterPressure(press);
		}
		else
			isPrevFound = false;
		// 实况近中心最大风速（持续风） 风圈半径 移向 移速  预报时效 
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(MAX.*?)(WINDS\\s+)(.*?)(M.*?P.*?S.*?)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			double wind = transPhraseToRealNumber(matcher.group(3));
			if(wind <= 0) wind = defaultF;
			// 是持续风速还是阵风速，书上写的持续风速，C代码写的阵风速
			//typhEle.setMaxSustainedWind(wind); 
			typhEle.setGustSpeed(wind); 
		}
		else isPrevFound = false;
		// 12级风圈半径
		if(isPrevFound)
			rep = rep.substring(end); 
		double radius = defaultF;
		reg = Pattern.compile("(THREE\\s+THREE\\s+METERS\\s+PER\\s+SECOND\\s+WITHIN\\s+)(.*?)(\\s+KILOMETER)");
		if((matcher = reg.matcher(rep)).find()){
			end = matcher.end();
			isPrevFound = true;
			radius = transPhraseToRealNumber(matcher.group(2));
			if(radius < 0) radius = defaultF;
			typhEle.setWindCircleRadiusL12_01(radius);
			typhEle.setWindCircleRadiusL12_02(radius);
			typhEle.setWindCircleRadiusL12_03(radius);
			typhEle.setWindCircleRadiusL12_04(radius);
			typhEle.setAzimuthOfWindBeyondL12_01(45);
			typhEle.setAzimuthOfWindBeyondL12_02(135);
			typhEle.setAzimuthOfWindBeyondL12_03(225);
			typhEle.setAzimuthOfWindBeyondL12_04(315);
		}
		else isPrevFound = false;
		// 10级风圈半径
		if(isPrevFound)
			rep = rep.substring(end); 
		reg = Pattern.compile("(TWO\\s+FIVE\\s+METERS\\s+PER\\s+SECOND\\s+WITHIN\\s+)(.*?)(\\s+KILOMETER)");
		if((matcher = reg.matcher(rep)).find()){
			end = matcher.end();
			isPrevFound = true;
			radius = transPhraseToRealNumber(matcher.group(2));
			if(radius < 0) radius = defaultF;
			typhEle.setWindCircleRadiusL10_01(radius);
			typhEle.setWindCircleRadiusL10_02(radius);
			typhEle.setWindCircleRadiusL10_03(radius);
			typhEle.setWindCircleRadiusL10_04(radius);
			typhEle.setAzimuthOfWindBeyondL10_01(45);
			typhEle.setAzimuthOfWindBeyondL10_02(135);
			typhEle.setAzimuthOfWindBeyondL10_03(225);
			typhEle.setAzimuthOfWindBeyondL10_04(315);
		}
		else isPrevFound = false;
		
//		int num = typhkey.getNumOfForecastEfficiency() + 1;
//		typhkey.setNumOfForecastEfficiency(num);
//		typhEles.add(typhEle);
		elementMap.put(typhEle.getForecastEfficiency(), typhEle);
		// 未来的纬度预报、未来的精度预报
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(FORECAST\\s+POSITIONS\\s+)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
		}
		else {
			isPrevFound = false;
			return 0;
		}
		if(isPrevFound) 
			rep = rep.substring(end);
		reg = Pattern.compile("(AT\\s+)(\\d+)(\\s+)(.*?)(NORTH|SOUTH)(.*?)(WEST|EAST)");
		int difHours = 0;
		while((matcher = reg.matcher(rep)).find()){
			typhEle = new TyphEle();
			end = matcher.end();
			tmp = matcher.group(2);
			if(tmp.length() == 6){
				endTime[2] = Integer.parseInt(tmp.substring(0, 2)); // 日
				endTime[3] = Integer.parseInt(tmp.substring(2, 4)); // 时
				difHours = getDifHours(startTime, endTime);
				if(difHours >= 0) typhEle.setForecastEfficiency(difHours);  // 预报时效
			}
			lat = transPhraseToRealNumber(matcher.group(4));// 未来的纬度预报
			lon = transPhraseToRealNumber(matcher.group(6)); // 未来的经度预报
			lat = getLatOrLon(lat, matcher.group(5));
			lon = getLatOrLon(lon, matcher.group(7));
			typhEle.setLatitude(lat);
			typhEle.setLongtitude(lon);
//			num = typhkey.getNumOfForecastEfficiency() + 1;
//			typhkey.setNumOfForecastEfficiency(num);
//			typhEles.add(typhEle);
			if(typhEle.getForecastEfficiency() != 0){
//				num = typhkey.getNumOfForecastEfficiency() + 1;
//				typhkey.setNumOfForecastEfficiency(num);
//				typhEles.add(typhEle);
				elementMap.put(typhEle.getForecastEfficiency(), typhEle);
			}
			rep = rep.substring(end);
		}
		typhkey.setNumOfForecastEfficiency(elementMap.size());
		typhEles.addAll(elementMap.values());
		ret = 0;
		return ret;
	}
	
	
	
	/**
	 * 报文解码获得持续风速、阵风速、12级、10级、7级风圈方位角和半径  
	 * @param rep 待解码的字符串，函数返回时，rep中解码过的部分会消耗掉
	 * @param typhKey 存储解码结果
	 * @param typhEle 存储解码结果
	 * @return String  待解码字符串
	 */
	String windRadiusAndAzimuth(String rep, TyphKey typhKey, TyphEle typhEle){
		// 实况近中心持续风速，阵风风速
		Boolean isPrevFound = false;
		int end = 0;
		Pattern reg = Pattern.compile("(MAX\\s+SUSTAINED\\s+WINDS\\s-\\s)([0-9]+\\.?[0-9]?)(\\s+KT,\\s+GUSTS\\s+)([0-9]+\\.?[0-9]?)(\\s+KT)");
		Matcher matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			if(!rep.substring(0, matcher.start()).contains("---")){
				typhEle.setMaxSustainedWind(Double.parseDouble(matcher.group(2)) * 1852.0 / 3600.0);
				typhEle.setGustSpeed(Double.parseDouble(matcher.group(4)) * 1852.0 / 3600.0);
			}
			else{
				end = 0;
			}
		}
		else
			isPrevFound = false;
		// 大于等于12级大风的方位、风圈半径 (64表示12级风，50表示10级风，34表示7级风)
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(RADIUS\\s+OF\\s+064\\s+KT\\s+WINDS\\s-\\s)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			if(!rep.substring(0, matcher.start()).contains("---")){
				end = matcher.end();
				rep = rep.substring(end);
				reg = Pattern.compile("([0-9]+\\.?[0-9]?)(\\s+NM\\s+)(.*?QUADRANT)"); 
				int count = 0;
				double azimuth = defaultF;
				while((matcher = reg.matcher(rep)).find() && count <= 3){ // 风圈半径1~4，风方位角1~4
					count ++;
					end = matcher.end();
					azimuth = GetWindAzimuth(matcher.group(3).split(" ")[0]);
					if(count == 1){
						typhEle.setWindCircleRadiusL12_01(Double.parseDouble(matcher.group(1)) * 1.852); 
						if(azimuth >= 0)//方位1
							typhEle.setAzimuthOfWindBeyondL12_01(azimuth);
					}
					else if(count == 2){
						typhEle.setWindCircleRadiusL12_02(Double.parseDouble(matcher.group(1))* 1.852);
						if(azimuth >= 0) // 方位2
							typhEle.setAzimuthOfWindBeyondL12_02(azimuth);
					}
					else if(count == 3){
						typhEle.setWindCircleRadiusL12_03(Double.parseDouble(matcher.group(1))* 1.852);
						if(azimuth >= 0)
							typhEle.setAzimuthOfWindBeyondL12_03(azimuth);
					}
					else{ 
						typhEle.setWindCircleRadiusL12_04(Double.parseDouble(matcher.group(1))* 1.852);
						if(azimuth >= 0)
							typhEle.setAzimuthOfWindBeyondL12_04(azimuth);
						
					}
					rep = rep.substring(end);
				}
				if(count < 2){
					double radius = typhEle.getWindCircleRadiusL12_01();
					typhEle.setWindCircleRadiusL12_02(radius);
					typhEle.setWindCircleRadiusL12_03(radius);
					typhEle.setWindCircleRadiusL12_04(radius);
					typhEle.setAzimuthOfWindBeyondL12_01(45);
					typhEle.setAzimuthOfWindBeyondL12_02(135);
					typhEle.setAzimuthOfWindBeyondL12_03(225);
					typhEle.setAzimuthOfWindBeyondL12_04(315);
				}
			}
		}
		else isPrevFound = false;
		// 大于等于10级大风的方位、风圈半径 (64表示12级风，50表示10级风，34表示7级风)
		reg = Pattern.compile("(RADIUS\\s+OF\\s+050\\s+KT\\s+WINDS\\s-\\s)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			if(!rep.substring(0, matcher.start()).contains("---")){
				end = matcher.end();
				rep = rep.substring(end);
				int count = 0;
				double azimuth = defaultF;
				reg = Pattern.compile("([0-9]+\\.?[0-9]?)(\\s+NM\\s+)(.*?QUADRANT)"); 
				while((matcher = reg.matcher(rep)).find() && count <= 3){ // 风圈半径1~4，风方位角1~4
					count ++;
					end = matcher.end();
					azimuth = GetWindAzimuth(matcher.group(3).split(" ")[0]);
					if(count == 1){
						typhEle.setWindCircleRadiusL10_01(Double.parseDouble(matcher.group(1)) * 1.852); 
						if(azimuth >= 0)//方位1
							typhEle.setAzimuthOfWindBeyondL10_01(azimuth);
					}
					else if(count == 2){
						typhEle.setWindCircleRadiusL10_02(Double.parseDouble(matcher.group(1))* 1.852);
						if(azimuth >= 0) // 方位2
							typhEle.setAzimuthOfWindBeyondL10_02(azimuth);
					}
					else if(count == 3){
						typhEle.setWindCircleRadiusL10_03(Double.parseDouble(matcher.group(1))* 1.852);
						if(azimuth >= 0) // 方位3
							typhEle.setAzimuthOfWindBeyondL10_03(azimuth);
					}
					else{ 
						typhEle.setWindCircleRadiusL10_04(Double.parseDouble(matcher.group(1))* 1.852);
						if(azimuth >= 0) // 方位4
							typhEle.setAzimuthOfWindBeyondL10_04(azimuth);
						
					}
					rep = rep.substring(end);
				}
				if(count < 2){
					double radius = typhEle.getWindCircleRadiusL10_01();
					typhEle.setWindCircleRadiusL10_02(radius);
					typhEle.setWindCircleRadiusL10_03(radius);
					typhEle.setWindCircleRadiusL10_04(radius);
					typhEle.setAzimuthOfWindBeyondL10_01(45);
					typhEle.setAzimuthOfWindBeyondL10_02(135);
					typhEle.setAzimuthOfWindBeyondL10_03(225);
					typhEle.setAzimuthOfWindBeyondL10_04(315);
				}
			}
		}
		else isPrevFound = false;
		// 大于等于7级大风的方位、风圈半径 (64表示12级风，50表示10级风，34表示7级风)
		reg = Pattern.compile("(RADIUS\\s+OF\\s+034\\s+KT\\s+WINDS\\s-\\s)");
		matcher = reg.matcher(rep);
		if(matcher.find()){
			isPrevFound = true;
			end = matcher.end();
			if(!rep.substring(0, matcher.start()).contains("---")){
				rep = rep.substring(end);
				reg = Pattern.compile("([0-9]+\\.?[0-9]?)(\\s+NM\\s+)(.*?QUADRANT)"); 
				int count = 0;
				double azimuth = defaultF;
				while((matcher = reg.matcher(rep)).find() && count <= 3){ // 风圈半径1~4，风方位角1~4
					count ++;
					end = matcher.end();
					azimuth = GetWindAzimuth(matcher.group(3).split(" ")[0]);
					if(count == 1){
						typhEle.setWindCircleRadiusL7_01(Double.parseDouble(matcher.group(1)) * 1.852); 
						if(azimuth >= 0)//方位1
							typhEle.setAzimuthOfWindBeyondL7_01(azimuth);
					}
					else if(count == 2){
						typhEle.setWindCircleRadiusL7_02(Double.parseDouble(matcher.group(1))* 1.852);
						if(azimuth >= 0) // 方位2
							typhEle.setAzimuthOfWindBeyondL7_02(azimuth);
					}
					else if(count == 3){
						typhEle.setWindCircleRadiusL7_03(Double.parseDouble(matcher.group(1))* 1.852);
						if(azimuth >= 0) // 方位3
							typhEle.setAzimuthOfWindBeyondL7_03(azimuth);
					}
					else{ 
						typhEle.setWindCircleRadiusL7_04(Double.parseDouble(matcher.group(1))* 1.852);
						if(azimuth >= 0) // 方位4
							typhEle.setAzimuthOfWindBeyondL7_04(azimuth);
						
					}
					rep = rep.substring(end);
				}
				if(count < 2){
					double radius = typhEle.getWindCircleRadiusL7_01();
					typhEle.setWindCircleRadiusL7_02(radius);
					typhEle.setWindCircleRadiusL7_03(radius);
					typhEle.setWindCircleRadiusL7_04(radius);
					typhEle.setAzimuthOfWindBeyondL7_01(45);
					typhEle.setAzimuthOfWindBeyondL7_02(135);
					typhEle.setAzimuthOfWindBeyondL7_03(225);
					typhEle.setAzimuthOfWindBeyondL7_04(315);
				}
			}
		}
		else isPrevFound = false;
		return rep;
	}
	/**
	 * 解析风的方向角   
	 * @param str 待解析的字符串
	 * @return float  解析结果：>=0解码成功；<0 解码有错误，失败
	 */
	float GetWindAzimuth(String str){
		float azimuth = 0;
		str = str.trim();
		if(str.compareTo("N") == 0 || str.compareTo("NORTH") == 0)
			azimuth = 0;
		else if(str.compareTo("NNE") == 0 || str.compareTo("NORTHNORTHEAST") == 0 || str.compareTo("NORTH-NORTHEAST") == 0)
			azimuth = (float) 22.5;
		else if(str.compareTo("NE") == 0 || str.compareTo("NORTHEAST") == 0)
			azimuth = 45;
		else if(str.compareTo("ENE") == 0 || str.compareTo("EASTNORTHEAST") == 0 || str.compareTo("EAST-NORTHEAST") == 0)
			azimuth = (float) 67.5;
		else if(str.compareTo("E") == 0 || str.compareTo("EAST") == 0)
			azimuth = 90;
		else if(str.compareTo("ESE") == 0 || str.compareTo("EASTSOUTHEAST") == 0 || str.compareTo("EAST-SOUTHEAST") == 0)
			azimuth = (float) 112.5;
		else if(str.compareTo("SE") == 0 || str.compareTo("SOUTHEAST") == 0)
			azimuth = 135;
		else if(str.compareTo("SSE") == 0 || str.compareTo("SOUTHSOUTHEAST") == 0 || str.compareTo("SOUTH-SOUTHEAST") == 0)
			azimuth = (float) 157.5;
		else if(str.compareTo("S") == 0 || str.compareTo("SOUTH") == 0)
			azimuth = 180;
		else if(str.compareTo("SSW") == 0 || str.compareTo("SOUTHSOUTHWEST") == 0 || str.compareTo("SOUTH-SOUTHWEST") == 0)
			azimuth = (float) 202.5;
		else if(str.compareTo("SW") == 0 || str.compareTo("SOUTHWEST") == 0)
			azimuth = 225;
		else if(str.compareTo("WSW") == 0 || str.compareTo("WESTSOUTHWEST") == 0 || str.compareTo("WEST-SOUTHWEST") == 0)
			azimuth = (float) 247.5;
		else if(str.compareTo("W") == 0 || str.compareTo("WEST") == 0)
			azimuth = 270;
		else if(str.compareTo("WNW") == 0 || str.compareTo("WESTNORTHWEST") == 0 || str.compareTo("WEST-NORTHWEST") == 0)
			azimuth = (float) 292.5;
		else if(str.compareTo("NW") == 0 || str.compareTo("NORTHWEST") == 0)
			azimuth = 315;
		else if(str.compareTo("NNW") == 0 || str.compareTo("NORTHNORTHWEST") == 0 || str.compareTo("NORTH-NORTHWEST") == 0)
			azimuth = (float) 337.5;
		else if(str.compareTo("ALMOST") == 0) 
			azimuth = 0;
		else azimuth = -1;
		return azimuth;
	}
	
	/**
	 * 解析移向、移速、近中心气压和风速  
	 * @param rep 待解码字符串，函数返回时，rep中解码过的部分会消耗掉
	 * @param typhKey 存储解码结果
	 * @param typhEle 存储解码结果
	 * @return String  待解码字符串
	 */
	String predict(String rep, TyphKey typhKey, TyphEle typhEle){
		Boolean isPrevFound = false;
		double azimuth = defaultF;
		int end = 0;
		Pattern reg = Pattern.compile("(MOVE\\s+)(.*?)(\\s+)"); // 移向
		Matcher matcher = null;
		String tmp;
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			azimuth = GetWindAzimuth(matcher.group(2));
			if(azimuth < 0) azimuth = defaultF;
			typhEle.setMovingDir(azimuth);
			if(Math.abs(azimuth - 0.0) < 0.001)
				typhEle.setMovingSpeed(0);
			else{
				rep = rep.substring(end);
				Pattern speed = Pattern.compile("(([0-9]+.?[0-9]?KT)|(SLOWLY)|(ALMOST\\s+STATIONARY))(\\s+)"); // 移速
				if((matcher = speed.matcher(rep)).find()){
					isPrevFound = true;
					end = matcher.end();
					tmp = matcher.group(1).trim();
					if(tmp.equals("SLOWLY"))
						typhEle.setMovingSpeed(1);
					else if(tmp.contains("ALMOST"))
						typhEle.setMovingSpeed(0);
					else 
						typhEle.setMovingSpeed(Double.parseDouble(tmp.substring(0,  tmp.length() - 2)) * (1852.0 / 3600.0));
				}
				else isPrevFound = false;
			}
		}
		else{
			isPrevFound = false;
		}
		// 实况近中心气压
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(PRES\\s+)([0-9]+.?[0-9]?)(HPA)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			typhEle.setCenterPressure(Double.parseDouble(matcher.group(2)));
		}
		else isPrevFound = false;
		// 实况近中心风速（持续风） * (1852.0 / 3600)
		if(isPrevFound)
			rep = rep.substring(end);
		reg = Pattern.compile("(MXWD\\s+)([0-9]+.?[0-9]?)(KT)");
		if((matcher = reg.matcher(rep)).find()){
			isPrevFound = true;
			end = matcher.end();
			typhEle.setMaxSustainedWind(Double.parseDouble(matcher.group(2)) * (1852.0 / 3600));
		}
		else isPrevFound = false;
		if(isPrevFound)
			rep = rep.substring(end);
		return rep;
	}
	/**
	 * 判断字符串是否为数字，包括整数和小数  
	 * @param str  待判断字符串
	 * @return Boolean    true 或者 false  
	 */
	static Boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("^(\\-|\\+)?\\d+(\\.\\d+)?$");  
		Matcher isNum = pattern.matcher(str);  
		if (!isNum.matches()) {  
	        return false;  
	    }  
	    return true;  
	}
	
	/**
	 * 计算两个时间相差的小时数，预报时效   
	 * @param startTime 开始时间 年、月、日、时
	 * @param endTime 结束时间 年、月、日、时
	 * @return int 失败时，返回值<0，；成功时，返回小时差，非负数      
	 */
	int getDifHours(int[] startTime, int[] endTime){
		int difHours = -1;
		if(startTime[2] <= endTime[2]){ // 此时，年、月应相同
			endTime[0] = startTime[0];
			endTime[1] = startTime[1];
		}
		else if(startTime[2] > endTime[2]) { // 日期有差异(此时，endTime的月份应比startTime的月份大1，差若干天)
			if(startTime[1] == 12){ 
				endTime[1] = 1;
				endTime[0] = startTime[0] + 1; // 下一年
 			}
			else{
				endTime[1] = startTime[1] + 1;
				endTime[0] = startTime[0];
			}
		}
		else return difHours;
		String st = startTime[0] + "-" + startTime[1] + "-" + startTime[2] + " " + startTime[3];
		String ed = endTime[0] + "-" + endTime[1] + "-" + endTime[2] + " " + endTime[3];
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
		try{
			Date start = simpleDateFormat.parse(st);
			Date end = simpleDateFormat.parse(ed);
			difHours = (int)((end.getTime() - start.getTime()) / (1000 * 60 * 60));
		}catch(ParseException e){ // 时间解析异常
			return -1;
		}
		return difHours;
	}
	
	/**
	 * 由英文月份获得数字月份   
	 * @return int   失败时返回-1；成功时，返回月份
	 */
	int getMonth(String str){
		int mon = -1;
		for(int i = 0; i < 12; i ++){
			if(str.contains(Amonth[i])){
				mon = Integer.parseInt(Dmonth[i]);
				break;
			}
		}
		return mon;
	}
	/**
	 * 获取纬度或经度值   
	 * @param latOrlon 纬度、经度预备值
	 * @param latOrLonDir 指示南、北纬度；东、西经度
	 * @return double   返回的纬度或经度值
	 */
	double getLatOrLon(double latOrlon, String latOrLonDir){
		double ret = latOrlon;
		if(latOrlon < 0 || Math.abs(latOrlon - defaultF) < 0.0001)
			ret = defaultF;
		else{
			if(latOrLonDir.contains("SOUTH") || latOrLonDir.endsWith("S") || 
			latOrLonDir.contains("WEST") || latOrLonDir.endsWith("W"))
				ret = -latOrlon;
		}
		return ret;
	}
	/**
	 * 把短语转化为实数   
	 * @param str 待转化的字符串短语
	 * @return double    转化结果，非负实数
	 */
	double transPhraseToRealNumber(String str){
		String rs = "";
		String sp[]; 
		double ret = -1.0;
		Boolean isFound = false;
		if(str != null && !(str = str.trim()).isEmpty()){
			sp = str.split("\\s+");
			for(int i = 0; i < sp.length; i ++){
				isFound = false;
				for(int j = 0; j < 11; j ++){
					if(sp[i].equals(Adigit[j])){
						rs += Ddigit[j];
						isFound = true;
						break;
					}
				}
				if(isFound == false)
					return ret; 
			}
			if(isFound == true)
				try{
					ret = Double.parseDouble(rs);
				}catch (Exception e) {
					ReportError reportError = new ReportError();
					reportError.setMessage("Digit error!");
					reportError.setSegment(str);
					parseResult.put(reportError);
				}
		}
		return ret;
	}
	static double getLatLon(String latOrLonStr){
		String up = latOrLonStr.toUpperCase().trim();
		String dir = up.substring(0, 1);
		String val = up.substring(1);
		double returnVal = 999999;
		if(dir.equals("N") || dir.equals("S")){
			try{
				returnVal = Double.parseDouble(val.substring(0, 2)) + Double.parseDouble(val.substring(2)) / 60;
				if(dir.equals("S"))
					returnVal = -returnVal;
			}
			catch (Exception e) {
				
			}
		}
		else if(dir.equals("E") || dir.equals("W")){
			try{
				returnVal = Double.parseDouble(val.substring(0, 3)) + Double.parseDouble(val.substring(3)) / 60;
				if(dir.equals("W"))
					returnVal = -returnVal;
			}catch (Exception e) {
				
			}
		}
		return returnVal;
	}
	public static void main(String[] args) {
		DecodeTyph decodeTyph = new DecodeTyph();
		ParseResult<Typh> parseResult = decodeTyph.DecodeFile(new File
//				("D:\\HUAXIN\\DataProcess\\TYPHDATA\\DEMS\\能解析的-WT_MSG__WX150940.ABJ"));
//				("D:\\HUAXIN\\DataProcess\\TYPHDATA\\A_WTIN31DEMS311010_C_BABJ_20151031101335_45997_whitespace.txt"));
				("D:\\tmp\\WT_MSG__wp270657.abj"));
		List<Typh> list = parseResult.getData();
		TyphKey key = list.get(0).getTyphKey();
		List<TyphEle> eles = list.get(0).getTyphEles();
		System.out.println("台风名称、台风级别、台风国际编号： " + key.getTyphName() + " " + key.getTyphLevel() + " " + key.getInternalCode());
//		System.out.println("纬度、经度：" + eles.get(0).getLatitude() + " " + eles.get(0).getLongtitude());
	}
}