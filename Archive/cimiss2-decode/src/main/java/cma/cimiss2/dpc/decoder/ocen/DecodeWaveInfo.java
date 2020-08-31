package cma.cimiss2.dpc.decoder.ocen;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.ocean.WaveInfo;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * <br>
 * @Title:  DecodeWaveInfo.java   
 * @Package cma.cimiss2.dpc.decoder.ocen.waveInfo   
 * @Description:    TODO(海上测站或远程平台（飞机或卫星）的波浪信息报告解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年7月30日 下午2:54:06   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class DecodeWaveInfo{
	private ParseResult<WaveInfo> parseResult = new ParseResult<WaveInfo>(false);
	private static int defaultInt = 999999;
	
	public static void main(String[] args) {
//		String string = "940// ";
//		Pattern sect0_2 = Pattern.compile("([/0-9]{5}\\s+)");
//		Matcher matcher = null;
//		if((matcher = sect0_2.matcher(string)).find()){
//			String tmp = matcher.group(1);
//		}
		DecodeWaveInfo decodeWaveInfo = new DecodeWaveInfo();
		ParseResult<WaveInfo> parseResult = decodeWaveInfo.decodeWaveInfo(new File("D:\\HUAXIN\\DataProcess\\SM201705230218.58507401"));
		// 
		@SuppressWarnings("rawtypes")
		List<ReportInfo> list = parseResult.getReports();
		System.out.println(list.size());
		
	}
	
	
	/**
	 * 
	 * @Title: decodeWaveInfo   
	 * @Description: TODO(海上测站或远程平台（飞机或卫星）的波浪信息报告解码函数)   
	 * @param file
	 * @return ParseResult<WaveInfo>      
	 * @throws：
	 */
	@SuppressWarnings({ "resource", "deprecation" })
	public ParseResult<WaveInfo> decodeWaveInfo(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0) {
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			@SuppressWarnings("static-access")
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			 // 获取文件流
			Scanner scanner = null;
			InputStreamReader read = null;
			try{
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("NNNN"); // 
				String report = "";
				while (scanner.hasNext() && (report = scanner.next().trim()).length() > 3) {  
					// 移除空行
					report = report.replaceAll("((\r\n)|(\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String reports[] = report.split("\n|(\r\n)");
					if(reports.length < 3) {
						ReportError re = new ReportError();
						re.setMessage("Report format error, too short!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					// 1. 一个编报中心的资料，首行标识判断
					reports[0] = reports[0].trim();
					if(reports[0].substring(0, 4).compareTo("ZCZC") != 0 
							&& reports[0].substring(0, 3).compareTo("ZCZ") != 0
							&& reports[0].substring(0, 3).compareTo("CZC") != 0){
						// 报文标识错误
						continue;
					}
					// 2. 一个编报中心的资料, 第二行标识判断
					reports[1] = reports[1].trim();
					BullHeader bullHeader = new BullHeader();
					int ret = decodeHeader(reports[1], bullHeader);
					if(ret < 0){ // 报头格式错误
						ReportError re = new ReportError();
						re.setMessage("Report header format error or null!");
						re.setSegment(reports[1]);
						parseResult.put(re);
						continue;
					}
					// 移除前两行， 取得以“=”分隔的报文
					report = "";
					for(int i = 2; i < reports.length; i ++)
						report += (reports[i] + "\n");
					reports = report.trim().split("=");
					String tmp = "";
					String sp [] = null;
					for(int i = 0; i < reports.length; i ++){
						String sects[] = getSections(reports[i]);
						for(int j = 0; j < sects.length; j ++){
							if(sects[j] != null){
								sects[j] = sects[j].replaceAll("\r|\r\n|\n", " ");
							}
						}
						WaveInfo waveInfo = new WaveInfo();
						waveInfo.setCorrectSign(bullHeader.getBbb());   // V_BBB
						waveInfo.setReportCenter(bullHeader.getCccc()); // CCCC
						Matcher matcher = null;
						Pattern sect0_1 = Pattern.compile("(MMXX\\s+)([a-zA-Z0-9]+\\s+)([0-9]{5}\\s+)([0-9]{4}\\/?\\s+)([0-9]{5}\\s+[0-9]{5}\\s+)");
						// SECTION 0:
						if(sects[0] != null && (matcher = sect0_1.matcher(sects[0])).find()){
							// 报文类型
							waveInfo.setType("MMXX");
							// 呼号或海上观测平台编号
							tmp = matcher.group(2).trim();
							waveInfo.setBuoyIdentifier(tmp);
							// 资料时间    //** YYMMJ GGgg/   日月年 时分
							tmp = matcher.group(3).trim();
							Date date = new Date();
							date.setYear(Integer.parseInt(tmp.substring(4, 5)) + 2010 - 1900);
							date.setMonth(Integer.parseInt(tmp.substring(2, 4)) - 1);
							date.setDate(Integer.parseInt(tmp.substring(0, 2)));
							tmp = matcher.group(4).trim().replaceAll("/", "");
							date.setHours(Integer.parseInt(tmp.substring(0, 2)));
							date.setMinutes(Integer.parseInt(tmp.substring(2, 4)));
							date.setSeconds(0);
							waveInfo.setObservationTime(date);
							
							//2019-7-16 cuihongyuan
							if(!TimeCheckUtil.checkTime(waveInfo.getObservationTime())){
								ReportError reportError = new ReportError();
								reportError.setMessage("time check error!");
								reportError.setSegment(sp[1]);
								parseResult.put(reportError);
								continue;
							}
							
							// 经纬度
							tmp = matcher.group(5).trim();
							sp = tmp.split("\\s+");
							waveInfo.setLatitude(Double.parseDouble(sp[0].substring(1, 3)) + Double.parseDouble(sp[0].substring(3, 5)) / 60);
							waveInfo.setLongtitude(Double.parseDouble(sp[1].substring(0, 3)) + Double.parseDouble(sp[1].substring(3, 5)) / 60);
							
							sects[0] = sects[0].substring(matcher.end());
							Pattern sect0_2 = Pattern.compile("([/0-9]{5}\\s+)");
							while(sects[0] != null && (matcher = sect0_2.matcher(sects[0])).find()){
								tmp = matcher.group(1).trim();
								char C = tmp.charAt(0);
								switch (C) {
								case '0':
									//Ia    波数的频率指示码 、 Im   频谱数据的计算方法指示码、  Ip    平台类型指示码码
									waveInfo.setIa(getIntVal(tmp.substring(2, 3)));
									waveInfo.setIm(getIntVal(tmp.substring(3, 4)));
									waveInfo.setIp(getIntVal(tmp.substring(4, 5)));
									break;
								case '1':
									//hhhh       水深，单位：米
									waveInfo.setDepthOfWater(getIntVal(tmp.substring(1, 5)));
									break;
								case '2':
									//HsHsHsHs  有效波高。单位：厘米
									waveInfo.setSignificantWaveHeight(getIntVal(tmp.substring(1, 5)));
									break;
								case '3':
									//PpPpPpPp  频谱峰值周期
									waveInfo.setCycleOfPeakSpectrum(getIntVal(tmp.substring(1, 5)));
									break;
								case '4':
									//(HmHmHmHm)  最大浪高，单位：位势十米
									waveInfo.setMaxWaveHeight(getIntVal(tmp.substring(1, 5)));
									break;
								case '5':
								    //  (PaPaPaPa)     平均波周期，（十分之一秒，或以米为单位的平均波长）
									waveInfo.setCycleOfAvgWave(getIntVal(tmp.substring(1, 5)));
									break;
								case '6':
									//	 (HseHseHseHse)  估算的斜率传感器的有效波高，以厘米计算
									waveInfo.setWaveHeightOfSensor(getIntVal(tmp.substring(1, 5)));
									break;
								case '7':	
									// (PspPspPspPsp)   由斜率传感器得获得的光谱峰值周期，或者波长的频谱峰值，以米计算
									waveInfo.setCycleOfPeakSpectrum(getIntVal(tmp.substring(1, 5)));
									break;
								case '8':
									//	 (PsaPsaPsaPsa)   来源于斜率传感器的平均周期，（十分之一秒，或以米为单位的平均波长）
									waveInfo.setAvgCycleOfSensor(getIntVal(tmp.substring(1, 5)));
									break;
								case '9':
									//	 (dddddsds) dddd：以4度为单位的真实方向，； dsds；真实方向，十进制度
									waveInfo.setTrueDir(getIntVal(tmp.substring(1, 3)));
									break;
								default:
									break;
								}
								sects[0] = sects[0].substring(matcher.end());
								
							} // end while
						}//end if 
						else{
							ReportError re = new ReportError();
							re.setMessage("SECTION 0 format error!");
							re.setSegment(reports[i]);
							parseResult.put(re);
						}
						Pattern sect1 = Pattern.compile("([/0-9]{5}\\s+)");
						// SECTION 1:
						List<Integer> subBand = new ArrayList<>();
						List<Double> CenterFreq = new ArrayList<>();
						List<Double> increment = new ArrayList<>();
						List<Double> exp = new ArrayList<>();
						int cursor = 0;
						int group = 0;
						while(sects[1] != null && sects[1].length() > 0 && (matcher = sect1.matcher(sects[1])).find()){
							tmp = matcher.group(1).trim();
							if(cursor < 3){
								switch(cursor){
								case 0:
									// 频段总数
									waveInfo.setBandsNumber(getIntVal(tmp.substring(3, 5)));
									cursor ++;
									break;	
								case 1:
									//SSSS/   ：采样间隔，十分位秒或米
									waveInfo.setSampleInterval(getIntVal(tmp.substring(0, 4)));
									cursor ++;
									break;
								case 2:
									// D´D´D´D´/  波浪的持续时间（秒），或者波浪长度（十米）
									waveInfo.setWaveDuration(getIntVal(tmp.substring(0, 4)));
									cursor ++;
									break;
								default:
									break;
								}
							}
							else{
								if(cursor % 3 == 0){
									group ++;
									// BB///       subBand 元素之和 为 BandsNumber(总频段数)
									// 例如：11164 0007/ 1600/ 15/// 12504 15003 01/// 21015 27503 48/// 31105 31004
									// 64 = 15 + 01 + 48
									// the number(BB) of bands described in the next two groups
									subBand.add(getIntVal(tmp.substring(0, 2)));
								}
								//f1f1f1，fnfnfn ：序列中的第一个中心频率(Hz)，或第一个中心波数(m –1 )，幂由x给定。
								//x:      波浪谱数据的幂 (Exponent for spectral wave data)
								else if((getIntVal(tmp.substring(0, 1)) == group)){
									if(cursor % 3 == 1){
										CenterFreq.add((double)(getIntVal(tmp.substring(1, 4))));
										exp.add((double)(getIntVal(tmp.substring(4, 5))));
									}
									//fdfdfd : 增加到之前的中心频率(Hz)，或第一个中心波数(m –1 )的增量，以此获得此序列
									//x:      波浪谱数据的幂 (Exponent for spectral wave data)
									else if(cursor % 3 == 2){
										increment.add((double)(getIntVal(tmp.substring(1, 4))));
										exp.add((double)(getIntVal(tmp.substring(4, 5))));
									}
								}
								else{
									ReportError re = new ReportError();
									re.setMessage("SECTION 1: format error!");
									re.setSegment(reports[i]);
									parseResult.put(re);
									break;
								}
								cursor ++;
							}
							sects[1] = sects[1].substring(matcher.end());
						}// end while
						waveInfo.setSubBandNum(subBand);
						waveInfo.setCenterFrequency(CenterFreq);
						waveInfo.setIncrement(increment);
						waveInfo.setExpOfWaveSpectrumData(exp);
						
						// SECTION 2：
						Pattern sect2 = Pattern.compile("([/0-9]{5}\\s+)");
						List<Double> ratio_heaveSensor = new ArrayList<>();
						cursor = 0;
						while(sects[2] != null && sects[2].length() > 0 && (matcher = sect2.matcher(sects[2])).find()){
							tmp = matcher.group(1).trim();
							// 跳过第一个要素2222x
							if(cursor == 0){
								cursor ++;
								sects[2] = sects[2].substring(matcher.end());
								continue;
							}
							else if(cursor == 1){
								//CmCmCm :    从起伏传感器（heave sensor）推导出的最大无向波密度。   
								//nmnm :      由起伏传感器（heave sensor）确定的最大无向波密度所在的频段号。
								waveInfo.setMaxUndirectedWaveDensity_HeaveSensor(getIntVal(tmp.substring(0, 3)));
								waveInfo.setFrequencyBandOfMaxUndirectedWaveDensity_HeaveSensor(getIntVal(tmp.substring(3, 5)));
							}
							else {
								//c1c1c2c2 , c3c3c4c4 .. cncn  在给定频段，由起伏传感器（heave sensor）推导出的最大无向波密度同CmCmCm 给定的最大无向波密度的比率。
								//ratio_heaveSensor  最终size 大小为频带总数
								ratio_heaveSensor.add((double)(getIntVal(tmp.substring(1, 3))));
								ratio_heaveSensor.add((double)getIntVal(tmp.substring(3,  5)));
							}
							cursor ++;
							sects[2] = sects[2].substring(matcher.end());
						}// end while
						waveInfo.setRatio_HeaveSensor(ratio_heaveSensor);
						
						// SECTION 3:
						Pattern sect3 = Pattern.compile("([/0-9]{5}\\s+)");
						List<Double> ratio_slopeSensor = new ArrayList<>();
						cursor = 0;
						while (sects[3] != null && sects[3].length() > 0 && (matcher = sect3.matcher(sects[3])).find()) {
							tmp = matcher.group().trim();
							// 跳过第一个要素3333x
							if(cursor == 0){
								cursor ++;
								sects[3] = sects[3].substring(matcher.end());
								continue;
							}
							else if(cursor == 1){
								// CsmCsmCsm :    倾斜传感器推导出的最大无向波密度，频率为m 2 Hz –1，波数为m 3。
								// nsmnsm :          由倾斜传感器确定的最大无向波密度所在的频段号。
								waveInfo.setMaxUndirectedWaveDensity_SlopeSensor(getIntVal(tmp.substring(0, 3)));
								waveInfo.setFrequencyBandOfMaxUndirectedWaveDensity_SlopeSensor(getIntVal(tmp.substring(3, 5)));
							}
							else{
								//cs1cs1cs2cs2, cs3cs3cs4cs4 ,csncsn :在给定频段，由倾斜传感器推导出的最大无向波密度同C sm C sm C sm 给定的最大无向波密度的比率。
								//ratio_slopeSensor 最终size 大小为频带总数
								ratio_slopeSensor.add((double)getIntVal(tmp.substring(1, 3)));
								ratio_slopeSensor.add((double)(getIntVal(tmp.substring(3, 5))));
							}
							cursor ++;
							sects[3] = sects[3].substring(matcher.end());
							
						}// end while
						waveInfo.setRatio_SlopeSensor(ratio_slopeSensor);
						
						// SECTION 4:
						Pattern sect4 = Pattern.compile("([/0-9]{5}\\s+)");
						List<Double> meanDirs = new ArrayList<>();
						List<Double> principalDirs = new ArrayList<>();
						List<Double> firstNormalizedPolarCoordinates = new ArrayList<>();
						List<Double> secondNormalizedPolarCoordinates = new ArrayList<>();
						cursor = 1;
						while(sects[4] != null && sects[4].length() > 0 && (matcher = sect4.matcher(sects[4])).find()){
							tmp = matcher.group().trim();
							//da1da1:  平均方向，以4度为单位，均为指定波段波浪相对于真北的来向(Code table 0880)。
							//da2da2: 主方向，以4度为单位，均为指定波段波浪相对于真北的来向(Code table 0880)
							if(cursor >= 0 && cursor % 2 == 1){
								group = getIntVal(tmp.substring(0, 1));
								meanDirs.add((double)getIntVal(tmp.substring(1, 3)));
								principalDirs.add((double)getIntVal(tmp.substring(3, 5)));
							}
							else {
								if(getIntVal(tmp.substring(0, 1)) == group){
									//r1r1: 第一个导自傅里叶系数的归一化的极坐标。
									//r2r2: 第二个导自傅里叶系数的归一化的极坐标。
									//firstNormalizedPolarCoordinates和secondNormalizedPolarCoordinates最终 size() 大小均为频带总数
									firstNormalizedPolarCoordinates.add((double)getIntVal(tmp.substring(1, 3)));
									secondNormalizedPolarCoordinates.add((double)getIntVal(tmp.substring(3, 5)));
								}
								else{
									ReportError re = new ReportError();
									re.setMessage("SECTION 4: format error!");
									re.setSegment(reports[i]);
									parseResult.put(re);
									break;
								}
							}
						
							cursor ++;
							sects[4] = sects[4].substring(matcher.end());
						}// end while
						waveInfo.setMeanDir(meanDirs);
						waveInfo.setPrincipalDir(principalDirs);
						waveInfo.setFirstNormalizedPolarCoordinate(firstNormalizedPolarCoordinates);
						waveInfo.setSecondNormalizedPolarCoordinate(secondNormalizedPolarCoordinates);
						
						// SECTION 5:
						Pattern sect5 = Pattern.compile("([/0-9]{5}\\s+)");
						List<Double> spectralEstimations = new ArrayList<>();
						List<Double> trueDir_ComingOfWaves = new ArrayList<>();
						List<Double> directionalSpreadOfDominantWaves = new ArrayList<>();
						cursor = 0;
						group = 0;
						int sz = sects[5].split("\\s+").length;
						while(sects[5] != null && sects[5].length() > 0 && (matcher = sect5.matcher(sects[5])).find()){
							tmp = matcher.group();
							if(cursor == 0){
								waveInfo.setIndicatorOfDirectionalOrNonDirectionalSpectralWaveData(getIntVal(tmp.substring(4, 5)));
							}
							else{
								//A1A1A1, A2A2A2, AnAnAn : 从第一个到第n个频率（波数，如果指定为波数）谱估计值
								//AnAnAn: 使用频率还是波数，由Ia 指定。
								if(cursor >= 0 && cursor % 2 == 1){
									if(getIntVal(tmp.substring(0, 1)) == group){
										trueDir_ComingOfWaves.add((double)getIntVal(tmp.substring(1, 3)));
										directionalSpreadOfDominantWaves.add((double)getIntVal(tmp.substring(3,  5)));
									}
									else if(getIntVal(tmp.substring(0, 1)) == group + 1){
										spectralEstimations.add((double)getIntVal(tmp.substring(1, 4)));
										group += 1;
										if(cursor == sz -1 || spectralEstimations.size() - trueDir_ComingOfWaves.size() == 2){
											trueDir_ComingOfWaves.add((double)defaultInt);
											directionalSpreadOfDominantWaves.add((double)defaultInt);
										}
									}
									else{
										ReportError re = new ReportError();
										re.setMessage("SECTION 4: format error!");
										re.setSegment(reports[i]);
										parseResult.put(re);
										break;
									}
								}
								else{
									if(getIntVal(tmp.substring(0, 1)) == group){
										//d1d1,d2d2,dndn: 真实方向，以4度为单位，均为波浪的来向
										//dsds: 全方位的主波的传播方向
										trueDir_ComingOfWaves.add((double)getIntVal(tmp.substring(1, 3)));
										directionalSpreadOfDominantWaves.add((double)getIntVal(tmp.substring(3,  5)));
									}
									else if(getIntVal(tmp.substring(0,  1)) == (group + 1)){
										group += 1;
										spectralEstimations.add((double) getIntVal(tmp.substring(1,  4)));
										if(cursor == sz - 1 || spectralEstimations.size() - trueDir_ComingOfWaves.size() == 2){
											trueDir_ComingOfWaves.add((double)defaultInt);
											directionalSpreadOfDominantWaves.add((double)defaultInt);
										}
									}
									else{
										ReportError re = new ReportError();
										re.setMessage("SECTION 4: format error!");
										re.setSegment(reports[i]);
										parseResult.put(re);
										break;
									}
								}	
							}
							
							cursor ++;
							sects[5] = sects[5].substring(matcher.end());
						}// end while
						waveInfo.setSpectralEstimation(spectralEstimations);
						waveInfo.setTrurDir_ComingOfWave(trueDir_ComingOfWaves);
						waveInfo.setDirectionalSpreadOfDominantWave(directionalSpreadOfDominantWaves);
						
						parseResult.put(waveInfo);
						parseResult.setSuccess(true);
						parseResult.put(new ReportInfo<WaveInfo>(waveInfo, reports[i]));
					}// end for
				} // end while
				
			}catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}  catch (Exception e) {
	//			System.out.println("未定位的错误，文件：" + file);
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
		}
		else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	/**
	 * 
	 * @Title: isDigits   
	 * @Description: TODO(输入字符串是否只含有数字)   
	 * @param str
	 * @return boolean      
	 * @throws：
	 */
	static boolean isDigits(String str){
		if(str == null)
			return false;
		else{
			str = str.trim();
			for(int i = 0; i < str.length(); i ++){
				if(str.charAt(i) < '0' || str.charAt(i) > '9'){
					return false;
				}
			}
			return true;
		}
	}
	/**
	 * 
	 * @Title: getIntVal   
	 * @Description: TODO(解析出整数值)   
	 * @param str
	 * @return int      
	 * @throws：
	 */
	static int  getIntVal(String str){
		if(isDigits(str))
			return Integer.parseInt(str);
		else
			return defaultInt;
	}
	/**
	 * 
	 * @Title: getSections   
	 * @Description: TODO(将报文分段0~6段)   
	 * @param report
	 * @return String[]      
	 * @throws：
	 */
	static String[] getSections(String report){
		String ret[] = new String[6];
		String sp[] = report.split("\n");
		int len = sp.length;
		int i = 0;
		String tmp = "";
		for(int l = 0; l < 6; l ++)
			ret[l] = "";
		while(i < len && !(tmp = sp[i]).startsWith("111") && !tmp.startsWith("2222") && !tmp.startsWith("3333") && !tmp.startsWith("4444") && !tmp.startsWith("5555")){
			ret[0] += (tmp + "\n");
			i ++;
		}
		if(i < len && (tmp = sp[i]).startsWith("111")){
			while(i < len && !(tmp = sp[i]).startsWith("2222") && !tmp.startsWith("3333") && !tmp.startsWith("4444") && !tmp.startsWith("5555")){
				ret[1] += (tmp + "\n");
				i ++;
			}
		}
		if(i < len && (tmp = sp[i]).startsWith("2222")){
			while(i < len  && !(tmp = sp[i]).startsWith("3333") && !tmp.startsWith("4444") && !tmp.startsWith("5555")){
				ret[2] += (tmp + "\n");
				i ++;
			}
		}
		if(i < len && (tmp = sp[i]).startsWith("3333")){
			while(i < len  && !(tmp = sp[i]).startsWith("4444") && !tmp.startsWith("5555")){
				ret[3] += (tmp + "\n");
				i ++;
			}
		}
		if(i < len && (tmp = sp[i]).startsWith("4444")){
			while(i < len && !(tmp = sp[i]).startsWith("5555")){
				ret[4] += (tmp + "\n");
				i ++;
			}
		}
		if(i < len && (tmp = sp[i]).startsWith("5555")){
			while(i < len ){
				ret[5] += (tmp + "\n");
				i ++;
			}
		}
		return ret;
	}
	/**
	 * 
	 * <br>
	 * @Title:  DecodeWaveInfo.java   
	 * @Package cma.cimiss2.dpc.decoder.ocen.waveInfo   
	 * @Description:    TODO(报文头实体类)
	 * 
	 * <pre>
	 * SOFTWARE HISTORY
	 * Date         Engineer    Description
	 * ------------ ----------- --------------------------
	 * 2018年7月31日 下午3:50:07   cuihongyuan    Initial creation.
	 * </pre>
	 * 
	 * @author cuihongyuan
	 *
	 *
	 */
	private class BullHeader{
		private String tt = "";
		private String aa = "";
		private String ii = "";
		private String cccc = "";
		private String YYGGgg = "";
		private String bbb = "";
		public String getTt() {
			return tt;
		}
		public void setTt(String tt) {
			this.tt = tt;
		}
		public String getAa() {
			return aa;
		}
		public void setAa(String aa) {
			this.aa = aa;
		}
		public String getIi() {
			return ii;
		}
		public void setIi(String ii) {
			this.ii = ii;
		}
		public String getCccc() {
			return cccc;
		}
		public void setCccc(String cccc) {
			this.cccc = cccc;
		}
		public String getYYGGgg() {
			return YYGGgg;
		}
		public void setYYGGgg(String yYGGgg) {
			YYGGgg = yYGGgg;
		}
		public String getBbb() {
			return bbb;
		}
		public void setBbb(String bbb) {
			this.bbb = bbb;
		}
	}
	/**
	 * 
	 * @Title: decodeHeader   
	 * @Description: TODO(do basic syntax check of TT,AA,ii,CCCC,YYGGgg,BBB
*                       if no error,get bull->TT,AA,ii,CCCC,YYGGgg,BBB)   
	 * @param  header 待解析报文头
	 * @return int   解码失败返回-1；成功返回1
	 * @throws：
	 */
	 private int decodeHeader(String header, BullHeader bullheader){
		if(header == null || header.equals(""))
			return -1;
		String sp[] = header.split("\\s+");
		
		if(sp.length == 3 || sp.length == 4){
			// 1. TTAAII 例如 SAAU31(资料类型+国家+号码)
			if(sp[0].length() == 6 || sp[0].length() == 5){
				bullheader.setTt(sp[0].substring(0, 2));
				bullheader.setAa(sp[0].substring(2, 4));
				bullheader.setIi(sp[0].substring(4));
			}
			else
				return -1; // TTAAII 格式错误
			// 2. CCCC编报中心  例如：AMMC、RJTD
			if(sp[1].length() == 4){
				bullheader.setCccc(sp[1]);
			}
			else
				return -1; // CCCC 格式错误
			// 3. YYGGgg, (日期+时+分，分别占两位)
			if(sp[2].length() == 6){
				int i = 0;
				for(i = 0; i < 6; i ++){
					if(!Character.isDigit(sp[2].charAt(i)))
						break;
				}
				if(i == 6)
					bullheader.setYYGGgg(sp[2]);
				else{
					return -1; // YYGGgg 格式错误
				}
			}
			else
				return -1; //YYGGgg 格式错误
			// 4. V_BBB: 更正标识，例如：CCA
			bullheader.setBbb("000");
			if(sp.length == 4){
				String tmp_val = sp[3];
				char s = tmp_val.charAt(0);
				if(tmp_val.length() == 3 && (s == 'A' || s == 'R' ||s == 'C' ||s == 'P')){
					bullheader.setBbb(sp[3]);
				}
				else
					return -1; // V_BBB 格式错误
			}
		} 
		else{
			return -1;// 报头格式错误
		}
		return 1; // 报头解析成功
	}
}