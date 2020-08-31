package cma.cimiss2.dpc.decoder.upar;

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
import java.util.Scanner;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.upar.UparLBand;
import cma.cimiss2.dpc.decoder.bean.upar.UparMinute;
import cma.cimiss2.dpc.decoder.bean.upar.UparSecond;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * L波段探空系统秒级探测资料
 * @author liym
 */

public class DecodeLbandUparDetection {
	
	/**
	 * 解码结果封装
	 */
	private ParseResult<UparLBand> parseResult = new ParseResult<UparLBand>(false);

	SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public ParseResult<UparLBand> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			@SuppressWarnings("static-access")
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			InputStreamReader read = null;
			BufferedReader bufferedReader = null;
//			String fname = file.getName();
			 //获取文件流
			try{
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				Scanner scanner = null;
				scanner = new Scanner(read).useDelimiter("VERSION"); //用VERSION分割报文
				
				String report = "";	
				while (scanner.hasNext()) {
					
					UparLBand uparlband=new UparLBand();
					
					List<UparSecond> secList = new ArrayList<UparSecond>();
					List<UparMinute> minList= new ArrayList<UparMinute>();
					
					report = scanner.next();
					String reps[] = report.split("\n|\r\n|\r");//将报文所有行按回车分割
				
					String[] headerEle4=reps[4].trim().split("\\s+");//将头文件第5行按空格分组
				    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
				    Date dataTime=simpleDateFormat.parse(headerEle4[0]);
//				    Date dataTime = null;
//				    try {
//						dataTime = simpleDateFormat.parse(headerEle4[0]);
//						dataTime.setMinutes(0);
//						dataTime.setSeconds(0);
//						int hours = dataTime.getHours();
//						hours = ((hours + 23) % 24 / 3 + 1) * 3 % 24;
//						dataTime.setHours(hours);
//						if(hours == 0){
//							Calendar c = Calendar.getInstance();
//							c.setTime(dataTime);
//							c.add(c.DAY_OF_MONTH, 1);
//							dataTime = c.getTime();
//						}
////						value = simpleDateFormat2.format(dataTime.getTime());
//					} catch (Exception e) {
//						ReportError re = new ReportError();
//						re.setMessage("资料时间格式转换错误");
//						re.setSegment(reps[4]);
//						parseResult.put(re);
//						break;
//					}
					uparlband.setCastingUtcTime(dataTime);//施放时间（世界时）
					Date dataTime2=simpleDateFormat.parse(headerEle4[1]);
					uparlband.setCastingLocalTime(dataTime2);//施放时间（地方时）
					Date dataTime3=simpleDateFormat.parse(headerEle4[2]);
					uparlband.setSoundingTerminationUtcTime(dataTime3);//探空终止时间（世界时）
					Date dataTime4=simpleDateFormat.parse(headerEle4[3]);
					uparlband.setAnemometryTerminationUtcTime(dataTime4);//测风终止时间（世界时）
					uparlband.setReasonForSoundingTermination(getInt(headerEle4[4]));//探空终止原因
					uparlband.setReasonForAnemometryTermination(getInt(headerEle4[5]));//测风终止原因
					uparlband.setSoundingTerminationHeight(getDbl(headerEle4[6]));//探空终止高度
					uparlband.setAnemometryTerminationHeight(getDbl(headerEle4[7]));//测风终止高度
					uparlband.setSolarElevationAngle(getDbl(headerEle4[8]));//施放时太阳高度角
					uparlband.setInstantGroundTemperature(getDbl(headerEle4[9]));//施放瞬间地面气温
					uparlband.setInstantGroundPressure(getDbl(headerEle4[10]));//施放瞬间地面气压
					uparlband.setInstantGroundRelativeHumidity(getInt(headerEle4[11]));//施放瞬间地面相对湿度	
					uparlband.setInstantGroundWindDirection(getInt(headerEle4[12]));//施放瞬间地面风向
					uparlband.setInstantGroundWindSpeed(getDbl(headerEle4[13]));//施放瞬间地面风速
					uparlband.setInstantVisibility(getDbl(headerEle4[14]));//施放瞬间能见度
					uparlband.setInstantCloudGenus1(getInt(headerEle4[15]));//施放瞬间云属1
					uparlband.setInstantCloudGenus2(getInt(headerEle4[16]));//施放瞬间云属2
					uparlband.setInstantCloudGenus3(getInt(headerEle4[17]));//施放瞬间云属3
					
					String instantTotalCloudCloudCover=headerEle4[19];
					if("10-".equals(instantTotalCloudCloudCover)){
						uparlband.setInstantTotalCloudCover(99);//施放瞬间总云量
					}else{
						uparlband.setInstantTotalCloudCover(getInt(headerEle4[19]));//施放瞬间总云量
					}
					String InstantLowCloudCover=headerEle4[18];
					if("10-".equals(InstantLowCloudCover)){
						uparlband.setInstantLowCloudCover(99);//施放瞬间低云量
					}else{
						uparlband.setInstantLowCloudCover(getInt(headerEle4[18]));//施放瞬间低云量
					}
					uparlband.setInstantWeatherPhenomenon1(getInt(headerEle4[20]));//施放瞬间天气现象1
					uparlband.setInstantWeatherPhenomenon2(getInt(headerEle4[21]));//施放瞬间天气现象2
					uparlband.setInstantWeatherPhenomenon3(getInt(headerEle4[22]));//施放瞬间天气现象3
					uparlband.setCastPointAzimuth(getDbl(headerEle4[23]));//施放点方位角
					uparlband.setCastPointElevation(getDbl(headerEle4[24]));//施放点仰角
					uparlband.setCastPointDistance(getDbl(headerEle4[25]));//施放点距离（探测仪器与天线之间距离）
					
					String[] headerEle1=reps[1].trim().split("\\s+");//将头文件第2行按空格分组
					uparlband.setStationNumberChina(headerEle1[0]);//区站号
					uparlband.setLongitude(getDbl(headerEle1[1]));//经度
					uparlband.setLatitude(getDbl(headerEle1[2]));//纬度
					uparlband.setHeightOfSationGroundAboveMeanSeaLevel(getDbl(headerEle1[3]));//观测场拔海高度
					
					String[] headerEle2=reps[2].trim().split("\\s+");//将头文件第3行按空格分组
					uparlband.setTypeOfDetectionSystem(headerEle2[0]);//探测系统型号
					uparlband.setRadiosondeModel(headerEle2[2]);//探空仪型号
					uparlband.setInstrumentNumber(headerEle2[3]);//探空仪编号
					uparlband.setCastCount(getInt(headerEle2[4]));//施放计数
					uparlband.setBallWeight(getDbl(headerEle2[5]));//气球重量
					uparlband.setAntennaHeight(getDbl(headerEle2[1]));//探测系统天线高度
					uparlband.setAdditiveWeight(getDbl(headerEle2[6]));//附加物重量
					uparlband.setTotalForce(getDbl(headerEle2[7]));//总举力
					uparlband.setNetLifting(getDbl(headerEle2[8]));//净举力
					uparlband.setAverageSpeedOfLift(getDbl(headerEle2[9]));//平均升速
					
					
					String[] headerEle3=reps[3].trim().split("\\s+");//将头文件第4行按空格分组
					uparlband.setBaseTemperatureValue(getDbl(headerEle3[0]));//温度基测值
					uparlband.setInstrumentTemperatureValue(getDbl(headerEle3[1]));//温度仪器值
					uparlband.setTemperatureDeviation(getDbl(headerEle3[2]));//温度偏差
					uparlband.setBasePressureValue(getDbl(headerEle3[3]));//气压基测值
					uparlband.setInstrumentPressureValue(getDbl(headerEle3[4]));//气压仪器值
					uparlband.setPressureDeviation(getDbl(headerEle3[5]));//气压偏差
					uparlband.setBaseRelativeHumidityValue(getInt(headerEle3[6]));//相对湿度基测值
					uparlband.setInstrumentRelativeHumidityValue(getInt(headerEle3[7]));//相对湿度仪器值
					uparlband.setRelativeHumidityDeviation(getInt(headerEle3[8]));//相对湿度偏差
					uparlband.setInstrumentTestConclusion(getInt(headerEle3[9]));//仪器检测结论
					if(!TimeCheckUtil.checkTime(dataTime)){
						ReportError re = new ReportError();
						re.setMessage("Time out of range：time:"+dataTime+" stationCode:"+headerEle1[0]);
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}
					
					
					report = "";
					for(int i = 5; i < reps.length; i ++){
						report += reps[i] + "\n";    //将报文前5行去掉
					}
					
					String SecMins[] = report.split("NNNN");//以"NNNN"分割剩下所有报文，分为大约4组
					for(int j = 0; j < SecMins.length; j++){
						
						if(SecMins[j].contains("ZCZC SECOND")){
							String[] str=SecMins[j].trim().split("\n|\n\n");
							
							for(int i=0;i<str.length;i++){
									if(!str[i].contains("ZCZC SECOND")&&!str[i].contains("NNNN")){
										String[] datalineEle=str[i].trim().split("\\s+");
										if(datalineEle.length==12){
											try{
											UparSecond uparsecond=new UparSecond();
											uparsecond.setRelativeTime(getDbl(datalineEle[0]));
											uparsecond.setTemperature(getDbl(datalineEle[1]));
											uparsecond.setPressure(getDbl(datalineEle[2]));
											uparsecond.setRelativeHumidity(getInt(datalineEle[3]));
											uparsecond.setLookUpAngle(getDbl(datalineEle[4]));
											uparsecond.setBearing(getDbl(datalineEle[5]));
											uparsecond.setDistance(getDbl(datalineEle[6]));
											uparsecond.setLongitudeDev(getDbl(datalineEle[7]));
											uparsecond.setLatitudeeDev(getDbl(datalineEle[8]));
											uparsecond.setWindDir(getInt(datalineEle[9]));
											uparsecond.setWindSpeed(getInt(datalineEle[10]));
											uparsecond.setHeight(getInt(datalineEle[11]));
											
											secList.add(uparsecond);
											}
											catch(Exception e){
												System.out.print(e.getMessage());
											}
										}else{
											ReportError re = new ReportError();
											re.setMessage("File error:文件秒级数据长度不正确!");
											re.setSegment(str[i]);
											parseResult.put(re);
										}
									}
							}
						}else if(SecMins[j].contains("ZCZC MINUTE")){
							String[] str=SecMins[j].trim().split("\n\n");
							for(int i=0;i<str.length;i++){
									if(!str[i].contains("ZCZC MINUTE")&&!str[i].contains("NNNN")){
										String[] datalineEle=str[i].trim().split("\\s+");
										UparMinute uparminute = new UparMinute();
										uparminute.setRelativeTime(getDbl(datalineEle[0]));
										uparminute.setTemperature(getDbl(datalineEle[1]));
										uparminute.setPressure(getDbl(datalineEle[2]));
										uparminute.setRelativeHumidity(getInt(datalineEle[3]));
										uparminute.setWindDir(getInt(datalineEle[4]));
										uparminute.setWindSpeed(getInt(datalineEle[5]));
										uparminute.setHeight(getInt(datalineEle[6]));
										uparminute.setLongitudeDev(getDbl(datalineEle[7]));
										uparminute.setLatitudeeDev(getDbl(datalineEle[8]));
										minList.add(uparminute);
								}
							}
						}
					}
					uparlband.setUparsecond(secList);
					uparlband.setUparminute(minList);
					parseResult.setSuccess(true);
					parseResult.put(uparlband);
				} // end while
				
			}catch (UnsupportedEncodingException e) {
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
					if(bufferedReader != null)
						bufferedReader.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	
	public double getDbl(String datalineEle){
		double r = 999999;
		try{
			r = Double.parseDouble(datalineEle);
		}catch(Exception e){
			
		}
		return r;
	}
	public int getInt(String datalineEle){
		int r=999999;
		try{
			r=Integer.parseInt(datalineEle);
		}
		catch(Exception e){
			
		}
		return r;
	}
	
	public static void main(String[] args) {
		DecodeLbandUparDetection decodeLbandUparDetection = new DecodeLbandUparDetection();
		decodeLbandUparDetection.DecodeFile(new File("D:\\TEMP\\B.3.1.1\\9-16\\Z_UPAR_I_57067_20190915051518_O_TEMP-L.txt"));
		List<UparLBand> r = decodeLbandUparDetection.parseResult.getData();
		System.out.println(r.size());
		System.out.println(r.get(0).getUparminute().size());
		System.out.println(r.get(0).getUparsecond().size());
	}
}
