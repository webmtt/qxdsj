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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sand.AveObservationSandTowData;
import cma.cimiss2.dpc.decoder.bean.sand.AveObservationSandTowerData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;


public class DecodeSandTow {
	/* 存放数据解析的结果集 */
	private ParseResult<AveObservationSandTowData> parseResult = new ParseResult<AveObservationSandTowData>(false);

	/**
	 * 铁塔平均场观测资料数据
	 * 函数名：DecodeFile
	 * 
	 * @param file
	 *            文件对象
	 * 
	 * @return ParseResult<PrecipitationObservationDataReg> 解码结果包裹类
	 */
	@SuppressWarnings({ "unchecked", "resource" })
	public ParseResult<AveObservationSandTowData> DecodeFile(File file) {
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
				String startTime=null;
				String endTime=null;
				String datetime=null;
			    String filename=file.getName();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				// 循环读取文件的行d
				int flagNum = 0;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					ArrayList<String> headList=new ArrayList<String>();
					// 判断文件是否正常结束
					if (flagNum%17 == 0) {
						lineTxtHead=lineTxt;//将报文头保存到保文表
						String[] items = lineTxt.split("\\s+");
						stationNumberChina = filename.split("_")[4];
						String year=filename.split("_")[5].substring(0, 4);
						String month=filename.split("_")[5].substring(4, 6);
						String day=filename.split("_")[5].substring(6, 8);
						datetime=year+"-"+month+"-"+day+" "+filename.split("_")[5].substring(8, 10)+":"+filename.split("_")[5].substring(10, 12)+":"+filename.split("_")[5].substring(12, 14);
						if (items.length == 4 ) {
							startTime=year+"-"+month+"-"+day+" "+items[1];
							endTime=year+"-"+month+"-"+day+" "+items[3];
							flagNum = flagNum + 1;
							continue;// 第一行报文头跳过,跳过第二行
							
						} else {
							ReportError reportError = new ReportError();
							reportError.setMessage("report header error");
							reportError.setSegment(lineTxt);
							parseResult.put(reportError);
							return parseResult;
						}
					}
					String strTxt=lineTxt.replaceAll("\\/+", "999999");
					String[] items = strTxt.split("\\s+");
				
					// 判断文件要素完整
					if (items.length == 7) {
						if(items[1].contains("RT")) {
							flagNum = flagNum + 1;
							continue;
						}
//						if((!items[1].equals("999999")) || (!items[2].equals("999999"))|| (!items[3].equals("999999"))|| (!items[4].equals("999999"))|| (!items[5].equals("999999"))|| (!items[6].equals("999999"))||(!items[7].equals("999999")) || (!items[8].equals("999999"))|| (!items[9].equals("999999"))|| (!items[10].equals("999999"))|| (!items[11].equals("999999"))|| (!items[12].equals("999999"))||(!items[13].equals("999999")) || (!items[14].equals("999999"))|| (!items[15].equals("999999"))|| (!items[16].equals("999999"))|| (!items[17].equals("999999"))|| (!items[18].equals("999999"))){
							AveObservationSandTowData aveTowerData = new AveObservationSandTowData();
							try {
								String observationTime=startTime;
								/*if(observationTime.length()>14){
									observationTime=items[0].substring(1, 15);
								}*/
								headList.add(stationNumberChina);
								headList.add(observationTime);//将时间保存到报文表
								aveTowerData.setStationNumberChina(stationNumberChina);
//								if(!TimeCheckUtil.checkTime(sdf.parse(observationTime))){
//									ReportError re = new ReportError();
//									re.setMessage("DataTime out of range：time:"+sdf.parse(observationTime));
//									re.setSegment(lineTxt);
//									parseResult.put(re);
//									continue;
//								}
								aveTowerData.setObservationTime(sdf1.parse(datetime));
								aveTowerData.setStartTimeOfobservation(sdf1.parse(startTime));
								aveTowerData.setEndTimeOfobservation(sdf1.parse(endTime));
								aveTowerData.setNumberOfSensorLayers(Integer.parseInt(items[1]));
								aveTowerData.setSensorHeight(Double.parseDouble(items[2]));
								aveTowerData.setMeanWindSpeed(Double.parseDouble(items[3]));
								aveTowerData.setMeanWindSpod(Double.parseDouble(items[4]));
								aveTowerData.setRelativeHumidity(Double.parseDouble(items[5]));
								aveTowerData.setTemperature(Double.parseDouble(items[6]));
								parseResult.put(new ReportInfo(headList,lineTxtHead+"\n"+lineTxt));
								parseResult.put(aveTowerData);
								parseResult.setSuccess(true);
								
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digital conversion exception");
								re.setSegment(lineTxt);
								parseResult.put(re);
								continue;
							}
							flagNum = flagNum + 1;
						}else{
							ReportError re = new ReportError();
							re.setMessage("All the elements are missing");
							re.setSegment(lineTxt);
							parseResult.put(re);
							flagNum = flagNum + 1;
							continue;
						}
					// 2019-4-3 改为入库，不忽略
//					} else { // 报文头信息错误
//						// 报文中记录没有正常结束
//						ReportError re = new ReportError();
//						re.setMessage("report total length validation error");
//						re.setSegment(lineTxt);
//						parseResult.put(re);
//					}
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
public static void main(String[] args) {
	DecodeSandTow decode=new DecodeSandTow();
	File file=new File("C:\\BaiduNetdiskDownload\\test\\G.0001.0014.R001\\Z_SAND_TOW_C5_54517_20200202155001.txt");
	ParseResult<AveObservationSandTowData> parseResult=decode.DecodeFile(file);
	List<AveObservationSandTowData> list = parseResult.getData();
	System.out.println(list.size());
	for(AveObservationSandTowData lists:list) {
		System.out.println(lists.toString());
	}
}
	
}
