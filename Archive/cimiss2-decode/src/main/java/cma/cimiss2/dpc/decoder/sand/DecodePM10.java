package cma.cimiss2.dpc.decoder.sand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.sand.AtmosphericAerosolMassConcentration_PM10;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
/**
 * 
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  DecodePM10.java   
 * @Package org.cimiss2.decode.z_sand_pm10   
 * @Description:(沙尘暴气溶胶质量浓度PM10资料解码类)    
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月6日 上午9:57:27   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 *---------------------------------------------------------------------------------
 */

public class DecodePM10 {
	/* 存放数据解析的结果集 */

	private ParseResult<AtmosphericAerosolMassConcentration_PM10> parseResult = new ParseResult<AtmosphericAerosolMassConcentration_PM10>(false);
	/**
	 * 
	 * @Title: DecodeFile
	 * @Description:(沙尘暴气溶胶质量浓度PM10资料解码函数)
	 * @param file
	 * @return ParseResult<AtmosphericAerosolMassConcentration_PM10> 解码结果集
	 * @throws：
	 */
	@SuppressWarnings({ "unchecked", "rawtypes", "static-access" })
	public ParseResult<AtmosphericAerosolMassConcentration_PM10> DecodeFile(File file) {
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			BufferedReader bufferedReader = null;
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];

				// 获取文件流
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxtHead = null;
				String lineTxt = null;
				String stationNumberChina = null;
				String latitude = null;
				String longitude = null;
				String heightSeaLevel = null;
				String  timeInterval = null;
			
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				
				//循环读取文件的行
				int flagNum = 0;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// 判断文件是否正常结束
					if (flagNum == 0) {
						lineTxtHead=lineTxt;//将报文头保存到保文表
						lineTxt = lineTxt.replaceAll("\\/+", "999999");
						String[] items = lineTxt.split("\\s+");
						if (items.length == 6) {
							stationNumberChina = items[0];//区站号
							longitude = items[1];//经度
							latitude= items[2];//纬度
							heightSeaLevel = items[3];//海拔高度
							timeInterval= items[5];//仪器采样时间间隔	
							flagNum = flagNum + 1;
							continue;
						} else {
							ReportError re = new ReportError();
	   						re.setMessage("report header length validation error");
	   						re.setSegment(lineTxtHead);
	   						parseResult.put(re);			
							break;					
						}
					}
					String strTxt=lineTxt.replaceAll("\\/+", "999999");
					String[] items = strTxt.split("\\s+");
					AgmeReportHeader agmeHeader = new AgmeReportHeader();
					// 判断文件要素完整
					if (items.length == 9) {
						if((!items[1].equals("999999")) || (!items[2].equals("999999"))|| (!items[3].equals("999999"))|| (!items[4].equals("999999"))|| (!items[5].equals("999999"))|| (!items[6].equals("999999"))||(!items[7].equals("999999"))||(!items[8].equals("999999"))){
							AtmosphericAerosolMassConcentration_PM10 pm10 = new AtmosphericAerosolMassConcentration_PM10();    
							try {
								String observationTime=items[0];
								if (items[0].substring(0, 4).compareTo("1900")<0) {
									ReportError re = new ReportError();
			   						re.setMessage("time error");
			   						re.setSegment(lineTxt);
			   						parseResult.put(re);	
									continue;
								}
								
								agmeHeader.setStationNumberChina(stationNumberChina);//站号
								agmeHeader.setLongitude(ElementValUtil.ToBeValidDouble(longitude));//经度
								agmeHeader.setLatitude(ElementValUtil.ToBeValidDouble(latitude));//纬度
								agmeHeader.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(heightSeaLevel));//海拔高度
								agmeHeader.setReport_time(sdf.parse(observationTime));//观测时间
								parseResult.put(new ReportInfo(agmeHeader,lineTxtHead+"\n"+lineTxt));
								parseResult.setSuccess(true);
								pm10.setStationNumberChina(stationNumberChina);//站号
								pm10.setLongitude(ElementValUtil.ToBeValidDouble(longitude));//经度
								pm10.setLatitude(ElementValUtil.ToBeValidDouble(latitude));//纬度
								pm10.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(heightSeaLevel));//测站高度
								pm10.setTimeInterval(ElementValUtil.ToBeValidDouble(timeInterval));//采样间隔时间
								pm10.setObservationTime(sdf.parse(observationTime));//观测时间
								
								//2019-7-16 cuihongyan
								if(!TimeCheckUtil.checkTime(pm10.getObservationTime())){
									ReportError reportError = new  ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(lineTxt);
									parseResult.put(reportError);
									continue;
								}
								
								pm10.setInstrumentStatusCode(items[1]);//仪器状态码
								pm10.setMassConcentration(ElementValUtil.ToBeValidDouble(items[2]));//质量浓度
								pm10.setTotalMass(ElementValUtil.ToBeValidDouble(items[3]));//总质量
								pm10.setNormalFlow(ElementValUtil.ToBeValidDouble(items[4]));//标准流量
								pm10.setAverageMassConcentration_1(ElementValUtil.ToBeValidDouble(items[5]));//1小时质量浓度平均值
								pm10.setAverageMassConcentration_24(ElementValUtil.ToBeValidDouble(items[6]));//24小时质量浓度平均值
								pm10.setEnvironmentTemperature(ElementValUtil.ToBeValidDouble(items[7]));//环境温度
								pm10.setEnvironmentPressure(ElementValUtil.ToBeValidDouble(items[8]));//环境气压
								parseResult.put(pm10);
								parseResult.setSuccess(true);

							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digital conversion exception");
								re.setSegment(lineTxt);
								parseResult.put(re);
								continue;
							}
							catch (ParseException e) {
								ReportError re = new ReportError();
								re.setMessage("Time format conversion exception");
								re.setSegment(lineTxt);
								parseResult.put(re);
								continue;

							}	
						}else{
							ReportError re = new ReportError();
							re.setMessage("All the elements are missing");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;
						}
						
					} 
					else {
						
						/*String observationTime=lineTxt.split("\\s+")[0];//获取观测时间
					    if(observationTime.length()>14){
						    observationTime=observationTime.substring(1, 15);
					     }
						agmeHeader.setStationNumberChina(stationNumberChina);//站号
						agmeHeader.setLongitude(ElementValUtil.ToBeValidDouble(longitude));//经度
						agmeHeader.setLatitude(ElementValUtil.ToBeValidDouble(latitude));//纬度
						agmeHeader.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(heightSeaLevel));//海拔高度
						agmeHeader.setReport_time(sdf.parse(observationTime));//观测时间
					    parseResult.put(new ReportInfo(agmeHeader,lineTxtHead+"\n"+lineTxt));
					    parseResult.setSuccess(true);*/
						 
						// 报文中记录没有正常结束
   						ReportError re = new ReportError();
   						re.setMessage("report total length validation error");
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
		} else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}

		return parseResult;
	}
}
