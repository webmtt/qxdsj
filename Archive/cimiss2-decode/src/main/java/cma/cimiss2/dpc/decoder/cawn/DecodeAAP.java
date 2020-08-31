package cma.cimiss2.dpc.decoder.cawn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.cawnAAP;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre
 * (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * 
 * Main class of decode the black carbon aerosol data<br>
 * 黑碳气溶胶资料解码类
 *
 * <p>
 * notes: 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.cawn.cawnAAP}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 52267,2224,2018,162,2300,2.9,79,332,389,267,515,877,789,0.0212,.5624,0.0212,1.4720,.60,-.377,0.0212,1.1185,0.0212,1.8481,.60,.839,0.0212,.9163,0.0212,1.8013,.60,-.477,0.0212,1.1662,0.0212,2.4824,.60,-.934,0.0212,.6514,0.0212,.8917,.60,.192,0.0212,1.5295,0.0212,2.4386,.60,-1.122,0.0212,1.0852,0.0212,2.0520,.60,-.511<br>
 * 
 * <strong>code:</strong><br>
 * DecodeAAP decodeAAP = new DecodeAAP();<br>
 * List<cawnAAP> list = decodeAAP.DecodeFile(new File(filepath)).getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).getSampleZoneMeasureSignal370());<br>
 * <strong>output:</strong><br>
 * 1<br>
 * 0.5624<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 上午11:39:10   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeAAP {
	/**
	 * 解码结果
	 */
	private ParseResult<cawnAAP> parseResult = new ParseResult<cawnAAP>(false);

	/**
	 * 黑碳气溶胶解码函数
	 * 
	 * @param file 待解码文件
	 * @return ParseResult<cawnAAP> 解码结果封装
	 */
	public ParseResult<cawnAAP> DecodeFile(File file) {
		if (file != null && file.exists() && file.isFile()) {
			if (file.length() <= 0) {
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			double defaultF = 999999;
			int defaultI = 999999;
			BufferedReader bufferedReader = null;
			InputStreamReader read = null;
			try {
				// 打开文件
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				@SuppressWarnings("static-access")
				String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);

				String lineTxt0 = bufferedReader.readLine();// 读取一行数据
			    if(lineTxt0 != null && !lineTxt0.equalsIgnoreCase("")) {
			    	String[] items0 = lineTxt0.split(",");
			    	
					if (items0.length == 55) {// 若数据为55列

						// 循环读一行记录
						String lineTxt = lineTxt0;
						while (lineTxt != null && lineTxt.trim().compareTo("") != 0) {
							char s = lineTxt.trim().charAt(0);
							if (s == 65279) { // 65279是空字符
								if (lineTxt.length() > 1) {
									lineTxt = lineTxt.substring(1);
								}
							}
							String[] items = lineTxt.split(",");
							try {
								cawnAAP cawnAAPModel = new cawnAAP();
								cawnAAPModel.setV01301(items[0]); // 区站号
								cawnAAPModel.setItemCode(NumerUtil.to_int(items[1], defaultI)); // 项目代码
								cawnAAPModel.setYear(Integer.parseInt(items[2])); // 年
								cawnAAPModel.setJulianDay(Integer.parseInt(items[3])); // 年序日
								cawnAAPModel.setHhmm(Integer.parseInt(items[4])); // 时分
								int year = cawnAAPModel.getYear();
								String dt = DateUtil.convertJulianDay2Date(year, cawnAAPModel.getJulianDay()); // 格式为：yyyyMMdd
								int month = Integer.parseInt(dt.substring(4, 6));
								int day = Integer.parseInt(dt.substring(6, 8));
								int hour = Integer.parseInt(items[4].substring(0, 2));
								int minute = Integer.parseInt(items[4].substring(2, 4));
								Calendar calendar = Calendar.getInstance();
								calendar.set(year, month - 1, day, hour, minute, 0);
								calendar.set(Calendar.MILLISECOND, 0);
								Date date = calendar.getTime();
								cawnAAPModel.setObservationTime(date); // 资料时间

								//  2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(cawnAAPModel.getObservationTime())){
									ReportError re = new ReportError();
									re.setMessage("check time error!");
									re.setSegment(lineTxt);
									parseResult.put(re);
									lineTxt = bufferedReader.readLine();
									continue;
								}

//							double flow=NumerUtil.to_double(items[5], defaultF);
//	 						if(flow!=999999){
//	 							flow=flow*0.001;
//	 						}
								cawnAAPModel.setFlow(NumerUtil.to_double(items[5], defaultF)); // 空气流量
								cawnAAPModel.setConcentration370(NumerUtil.to_double(items[6], defaultF)); // 370nm浓度
								cawnAAPModel.setConcentration470(NumerUtil.to_double(items[7], defaultF)); // 470nm浓度
								cawnAAPModel.setConcentration520(NumerUtil.to_double(items[8], defaultF)); // 520nm浓度
								cawnAAPModel.setConcentration590(NumerUtil.to_double(items[9], defaultF)); // 590nm浓度
								cawnAAPModel.setConcentration660(NumerUtil.to_double(items[10], defaultF)); // 660nm浓度
								cawnAAPModel.setConcentration880(NumerUtil.to_double(items[11], defaultF)); // 880nm浓度
								cawnAAPModel.setConcentration950(NumerUtil.to_double(items[12], defaultF)); // 950nm浓度

								cawnAAPModel.setSampleZoneZeroPointSignal370(NumerUtil.to_double(items[13], defaultF));
								cawnAAPModel.setSampleZoneMeasureSignal370(NumerUtil.to_double(items[14], defaultF));
								cawnAAPModel.setReferZoneZeroPointSignal370(NumerUtil.to_double(items[15], defaultF));
								cawnAAPModel.setReferZoneMeasureSignal370(NumerUtil.to_double(items[16], defaultF));
								cawnAAPModel.setSplitRatio370(NumerUtil.to_double(items[17], defaultF));
								cawnAAPModel.setLightLossRate370(NumerUtil.to_double(items[18], defaultF));

								cawnAAPModel.setSampleZoneZeroPointSignal470(NumerUtil.to_double(items[19], defaultF));
								cawnAAPModel.setSampleZoneMeasureSignal470(NumerUtil.to_double(items[20], defaultF));
								cawnAAPModel.setReferZoneZeroPointSignal470(NumerUtil.to_double(items[21], defaultF));
								cawnAAPModel.setReferZoneMeasureSignal470(NumerUtil.to_double(items[22], defaultF));
								cawnAAPModel.setSplitRatio470(NumerUtil.to_double(items[23], defaultF));
								cawnAAPModel.setLightLossRate470(NumerUtil.to_double(items[24], defaultF));

								cawnAAPModel.setSampleZoneZeroPointSignal520(NumerUtil.to_double(items[25], defaultF));
								cawnAAPModel.setSampleZoneMeasureSignal520(NumerUtil.to_double(items[26], defaultF));
								cawnAAPModel.setReferZoneZeroPointSignal520(NumerUtil.to_double(items[27], defaultF));
								cawnAAPModel.setReferZoneMeasureSignal520(NumerUtil.to_double(items[28], defaultF));
								cawnAAPModel.setSplitRatio520(NumerUtil.to_double(items[29], defaultF));
								cawnAAPModel.setLightLossRate520(NumerUtil.to_double(items[30], defaultF));

								cawnAAPModel.setSampleZoneZeroPointSignal590(NumerUtil.to_double(items[31], defaultF));
								cawnAAPModel.setSampleZoneMeasureSignal590(NumerUtil.to_double(items[32], defaultF));
								cawnAAPModel.setReferZoneZeroPointSignal590(NumerUtil.to_double(items[33], defaultF));
								cawnAAPModel.setReferZoneMeasureSignal590(NumerUtil.to_double(items[34], defaultF));
								cawnAAPModel.setSplitRatio590(NumerUtil.to_double(items[35], defaultF));
								cawnAAPModel.setLightLossRate590(NumerUtil.to_double(items[36], defaultF));

								cawnAAPModel.setSampleZoneZeroPointSignal660(NumerUtil.to_double(items[37], defaultF));
								cawnAAPModel.setSampleZoneMeasureSignal660(NumerUtil.to_double(items[38], defaultF));
								cawnAAPModel.setReferZoneZeroPointSignal660(NumerUtil.to_double(items[39], defaultF));
								cawnAAPModel.setReferZoneMeasureSignal660(NumerUtil.to_double(items[40], defaultF));
								cawnAAPModel.setSplitRatio660(NumerUtil.to_double(items[41], defaultF));
								cawnAAPModel.setLightLossRate660(NumerUtil.to_double(items[42], defaultF));

								cawnAAPModel.setSampleZoneZeroPointSignal880(NumerUtil.to_double(items[43], defaultF));
								cawnAAPModel.setSampleZoneMeasureSignal880(NumerUtil.to_double(items[44], defaultF));
								cawnAAPModel.setReferZoneZeroPointSignal880(NumerUtil.to_double(items[45], defaultF));
								cawnAAPModel.setReferZoneMeasureSignal880(NumerUtil.to_double(items[46], defaultF));
								cawnAAPModel.setSplitRatio880(NumerUtil.to_double(items[47], defaultF));
								cawnAAPModel.setLightLossRate880(NumerUtil.to_double(items[48], defaultF));

								cawnAAPModel.setSampleZoneZeroPointSignal950(NumerUtil.to_double(items[49], defaultF));
								cawnAAPModel.setSampleZoneMeasureSignal950(NumerUtil.to_double(items[50], defaultF));
								cawnAAPModel.setReferZoneZeroPointSignal950(NumerUtil.to_double(items[51], defaultF));
								cawnAAPModel.setReferZoneMeasureSignal950(NumerUtil.to_double(items[52], defaultF));
								cawnAAPModel.setSplitRatio950(NumerUtil.to_double(items[53], defaultF));
								cawnAAPModel.setLightLossRate950(NumerUtil.to_double(items[54], defaultF));

								parseResult.put(cawnAAPModel);
								parseResult.setSuccess(true);
								parseResult.put(new ReportInfo<cawnAAP>(cawnAAPModel, lineTxt));
							} catch (Exception e) {
								ReportError re = new ReportError();
								re.setMessage("Format error!");
								re.setSegment(lineTxt);
								parseResult.put(re);
								lineTxt = bufferedReader.readLine();
								continue;
							}
							lineTxt = bufferedReader.readLine();
						} // end while
					} else if (items0.length == 70) {

						// 循环读一行记录
						String lineTxt = lineTxt0;
						while (lineTxt != null && lineTxt.trim().compareTo("") != 0) {
							char s = lineTxt.trim().charAt(0);
							if (s == 65279) { // 65279是空字符
								if (lineTxt.length() > 1) {
									lineTxt = lineTxt.substring(1);
								}
							}
							String[] items = lineTxt.split(",");
							try {
								cawnAAP cawnAAPModel = new cawnAAP();
								cawnAAPModel.setV01301(items[0]); // 区站号
								cawnAAPModel.setItemCode(NumerUtil.to_int(items[1], defaultI)); // 项目代码
								cawnAAPModel.setYear(Integer.parseInt(items[2])); // 年
								cawnAAPModel.setJulianDay(Integer.parseInt(items[3])); // 年序日
								cawnAAPModel.setHhmm(Integer.parseInt(items[4])); // 时分
								int year = cawnAAPModel.getYear();
								String dt = DateUtil.convertJulianDay2Date(year, cawnAAPModel.getJulianDay()); // 格式为：yyyyMMdd
								int month = Integer.parseInt(dt.substring(4, 6));
								int day = Integer.parseInt(dt.substring(6, 8));
								int hour = Integer.parseInt(items[4].substring(0, 2));
								int minute = Integer.parseInt(items[4].substring(2, 4));
								Calendar calendar = Calendar.getInstance();
								calendar.set(year, month - 1, day, hour, minute, 0);
								calendar.set(Calendar.MILLISECOND, 0);
								Date date = calendar.getTime();
								cawnAAPModel.setObservationTime(date); // 资料时间
								
							//  2019-7-16 cuihongyuan
								if(!TimeCheckUtil.checkTime(cawnAAPModel.getObservationTime())){
									ReportError re = new ReportError();
									re.setMessage("check time error!");
									re.setSegment(lineTxt);
									parseResult.put(re);
									lineTxt = bufferedReader.readLine();
									continue;
								}
								
								cawnAAPModel.setTimebase(NumerUtil.to_double(items[5], defaultF)); // Timebase

								cawnAAPModel.setReferenceSignal370(NumerUtil.to_double(items[6], defaultF)); // 370nm参考信号
								cawnAAPModel.setFirstSampleSignal370(NumerUtil.to_double(items[7], defaultF)); // 370nm第1采样点信号
								cawnAAPModel.setSecondSampleSignal370(NumerUtil.to_double(items[8], defaultF)); // 370nm第2采样点信号

								cawnAAPModel.setReferenceSignal470(NumerUtil.to_double(items[9], defaultF)); // 470nm参考信号
								cawnAAPModel.setFirstSampleSignal470(NumerUtil.to_double(items[10], defaultF)); // 470nm第1采样点信号
								cawnAAPModel.setSecondSampleSignal470(NumerUtil.to_double(items[11], defaultF)); // 470nm第2采样点信号

								cawnAAPModel.setReferenceSignal520(NumerUtil.to_double(items[12], defaultF)); // 520nm参考信号
								cawnAAPModel.setFirstSampleSignal520(NumerUtil.to_double(items[13], defaultF)); // 520nm第1采样点信号
								cawnAAPModel.setSecondSampleSignal520(NumerUtil.to_double(items[14], defaultF)); // 520nm第2采样点信号

								cawnAAPModel.setReferenceSignal590(NumerUtil.to_double(items[15], defaultF)); // 590nm参考信号
								cawnAAPModel.setFirstSampleSignal590(NumerUtil.to_double(items[16], defaultF)); // 590nm第1采样点信号
								cawnAAPModel.setSecondSampleSignal590(NumerUtil.to_double(items[17], defaultF)); // 590nm第2采样点信号

								cawnAAPModel.setReferenceSignal660(NumerUtil.to_double(items[18], defaultF)); // 660nm参考信号
								cawnAAPModel.setFirstSampleSignal660(NumerUtil.to_double(items[19], defaultF)); // 660nm第1采样点信号
								cawnAAPModel.setSecondSampleSignal660(NumerUtil.to_double(items[20], defaultF)); // 660nm第2采样点信号

								cawnAAPModel.setReferenceSignal880(NumerUtil.to_double(items[21], defaultF)); // 880nm参考信号
								cawnAAPModel.setFirstSampleSignal880(NumerUtil.to_double(items[22], defaultF)); // 880nm第1采样点信号
								cawnAAPModel.setSecondSampleSignal880(NumerUtil.to_double(items[23], defaultF)); // 880nm第2采样点信号

								cawnAAPModel.setReferenceSignal950(NumerUtil.to_double(items[24], defaultF)); // 950nm参考信号
								cawnAAPModel.setFirstSampleSignal950(NumerUtil.to_double(items[25], defaultF)); // 950nm第1采样点信号
								cawnAAPModel.setSecondSampleSignal950(NumerUtil.to_double(items[26], defaultF)); // 950nm第2采样点信号

								double flow1 = NumerUtil.to_double(items[27], defaultF);
								if (flow1 != 999999) {
									flow1 = flow1 * 0.001;
								}
								cawnAAPModel.setFlow1(flow1); // 第1采样点流量

								double flow2 = NumerUtil.to_double(items[28], defaultF);
								if (flow2 != 999999) {
									flow2 = flow2 * 0.001;
								}
								cawnAAPModel.setFlow2(flow2); // FlowC与Flow1之差

								double flowC = NumerUtil.to_double(items[29], defaultF);
								if (flowC != 999999) {
									flowC = flowC * 0.001;
								}
								cawnAAPModel.setFlow(flowC); // 通过光腔的总流量

								cawnAAPModel.setPressure(NumerUtil.to_double(items[30], defaultF)); // 气压
								cawnAAPModel.setTemperature(NumerUtil.to_double(items[31], defaultF)); // 温度
								cawnAAPModel.setBB(NumerUtil.to_double(items[32], defaultF)); // 通过Sandradewi
																								// model得到的生物质燃烧黑碳比例
								cawnAAPModel.setContTemp(NumerUtil.to_double(items[33], defaultF)); // 控制板温度
								cawnAAPModel.setSupplyTemp(NumerUtil.to_double(items[34], defaultF)); // 供电板温度
								cawnAAPModel.setStatus(NumerUtil.to_double(items[35], defaultF)); // 状态码
								cawnAAPModel.setContStatus(NumerUtil.to_double(items[36], defaultF)); // 状态码
								cawnAAPModel.setDetectStatus(NumerUtil.to_double(items[37], defaultF)); // 状态码
								cawnAAPModel.setLedStatus(NumerUtil.to_double(items[38], defaultF)); // 状态码
								cawnAAPModel.setValveStatus(NumerUtil.to_double(items[39], defaultF)); // 状态码
								cawnAAPModel.setLedTemp(NumerUtil.to_double(items[40], defaultF)); // 显示板温度

								cawnAAPModel.setFirstSanmpleCon370(NumerUtil.to_double(items[41], defaultF)); // 370nm在第1采样点测得的未补偿黑碳浓度
								cawnAAPModel.setSecondSanmpleCon370(NumerUtil.to_double(items[42], defaultF)); // 370nm在第2采样点测得的未补偿黑碳浓度
								cawnAAPModel.setConcentration370(NumerUtil.to_double(items[43], defaultF)); // 370nm黑碳浓度

								cawnAAPModel.setFirstSanmpleCon470(NumerUtil.to_double(items[44], defaultF)); // 470nm在第1采样点测得的未补偿黑碳浓度
								cawnAAPModel.setSecondSanmpleCon470(NumerUtil.to_double(items[45], defaultF)); // 470nm在第2采样点测得的未补偿黑碳浓度
								cawnAAPModel.setConcentration470(NumerUtil.to_double(items[46], defaultF)); // 470nm黑碳浓度

								cawnAAPModel.setFirstSanmpleCon520(NumerUtil.to_double(items[47], defaultF)); // 520nm在第1采样点测得的未补偿黑碳浓度
								cawnAAPModel.setSecondSanmpleCon520(NumerUtil.to_double(items[48], defaultF)); // 520nm在第2采样点测得的未补偿黑碳浓度
								cawnAAPModel.setConcentration520(NumerUtil.to_double(items[49], defaultF)); // 520nm黑碳浓度

								cawnAAPModel.setFirstSanmpleCon590(NumerUtil.to_double(items[50], defaultF)); // 590nm在第1采样点测得的未补偿黑碳浓度
								cawnAAPModel.setSecondSanmpleCon590(NumerUtil.to_double(items[51], defaultF)); // 590nm在第2采样点测得的未补偿黑碳浓度
								cawnAAPModel.setConcentration590(NumerUtil.to_double(items[52], defaultF)); // 590nm黑碳浓度

								cawnAAPModel.setFirstSanmpleCon660(NumerUtil.to_double(items[53], defaultF)); // 660nm在第1采样点测得的未补偿黑碳浓度
								cawnAAPModel.setSecondSanmpleCon660(NumerUtil.to_double(items[54], defaultF)); // 660nm在第2采样点测得的未补偿黑碳浓度
								cawnAAPModel.setConcentration660(NumerUtil.to_double(items[55], defaultF)); // 660nm黑碳浓度

								cawnAAPModel.setFirstSanmpleCon880(NumerUtil.to_double(items[56], defaultF)); // 880nm在第1采样点测得的未补偿黑碳浓度
								cawnAAPModel.setSecondSanmpleCon880(NumerUtil.to_double(items[57], defaultF)); // 880nm在第2采样点测得的未补偿黑碳浓度
								cawnAAPModel.setConcentration880(NumerUtil.to_double(items[58], defaultF)); // 880nm黑碳浓度

								cawnAAPModel.setFirstSanmpleCon950(NumerUtil.to_double(items[59], defaultF)); // 950nm在第1采样点测得的未补偿黑碳浓度
								cawnAAPModel.setSecondSanmpleCon950(NumerUtil.to_double(items[60], defaultF)); // 950nm在第2采样点测得的未补偿黑碳浓度
								cawnAAPModel.setConcentration950(NumerUtil.to_double(items[61], defaultF)); // 950nm黑碳浓度

								cawnAAPModel.setCompCoeffi370(NumerUtil.to_double(items[62], defaultF)); // 370nm补偿系数
								cawnAAPModel.setCompCoeffi470(NumerUtil.to_double(items[63], defaultF)); // 470nm补偿系数
								cawnAAPModel.setCompCoeffi520(NumerUtil.to_double(items[64], defaultF)); // 520nm补偿系数
								cawnAAPModel.setCompCoeffi590(NumerUtil.to_double(items[65], defaultF)); // 590nm补偿系数
								cawnAAPModel.setCompCoeffi660(NumerUtil.to_double(items[66], defaultF)); // 660nm补偿系数
								cawnAAPModel.setCompCoeffi880(NumerUtil.to_double(items[67], defaultF)); // 880nm补偿系数
								cawnAAPModel.setCompCoeffi950(NumerUtil.to_double(items[68], defaultF)); // 950nm补偿系数

								cawnAAPModel.setTapeAdvCount(NumerUtil.to_double(items[69], defaultF)); // 采样带自观测开始的前进量

								parseResult.put(cawnAAPModel);
								parseResult.setSuccess(true);
								parseResult.put(new ReportInfo<cawnAAP>(cawnAAPModel, lineTxt));
							} catch (Exception e) {
								ReportError re = new ReportError();
								re.setMessage("Format error!");
								re.setSegment(lineTxt);
								parseResult.put(re);
								lineTxt = bufferedReader.readLine();
								continue;
							}
							lineTxt = bufferedReader.readLine();
						} // end While

					} else {
						System.out.println("非规范文件，暂不解码！");
						parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
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
					if (read != null)
						read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					if (bufferedReader != null)
						bufferedReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
//	public static void main(String args[]){
//		DecodeAAP decodeAAP = new DecodeAAP();
//		String filepath = "D:\\HUAXIN\\DataProcess\\黑碳气溶胶\\G.0002.0001.R001\\201806\\2018061123\\Z_CAWN_I_52267_20180611230000_O_AER-FLD-AAP.TXT";
//		List<cawnAAP> list = decodeAAP.DecodeFile(new File(filepath)).getData();
//		System.out.println(list.size());
//		System.out.println(list.get(0).getSampleZoneMeasureSignal370());
//	}

}