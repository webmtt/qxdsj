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
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sand.AveObservationSandTowerData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;


public class DecodeSandTower {
	/* 存放数据解析的结果集 */
	private ParseResult<AveObservationSandTowerData> parseResult = new ParseResult<AveObservationSandTowerData>(false);

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
	public ParseResult<AveObservationSandTowerData> DecodeFile(File file) {
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
				// 循环读取文件的行d
				int flagNum = 0;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					ArrayList<String> headList=new ArrayList<String>();
					// 判断文件是否正常结束
					if (flagNum == 0) {
						lineTxtHead=lineTxt;//将报文头保存到保文表
						String[] items = lineTxt.split("\\s+");
						if (items.length == 6 ) {
							stationNumberChina = items[0];
							longitude = items[1];
							latitude= items[2];
							heightSeaLevel = items[3];
							timeInterval= items[5];	
							flagNum = flagNum + 1;
							continue;// 第一行报文头跳过
							
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
					if (items.length == 19) {
//						if((!items[1].equals("999999")) || (!items[2].equals("999999"))|| (!items[3].equals("999999"))|| (!items[4].equals("999999"))|| (!items[5].equals("999999"))|| (!items[6].equals("999999"))||(!items[7].equals("999999")) || (!items[8].equals("999999"))|| (!items[9].equals("999999"))|| (!items[10].equals("999999"))|| (!items[11].equals("999999"))|| (!items[12].equals("999999"))||(!items[13].equals("999999")) || (!items[14].equals("999999"))|| (!items[15].equals("999999"))|| (!items[16].equals("999999"))|| (!items[17].equals("999999"))|| (!items[18].equals("999999"))){
							AveObservationSandTowerData aveTowerData = new AveObservationSandTowerData();
							try {
								String observationTime=items[0];
								/*if(observationTime.length()>14){
									observationTime=items[0].substring(1, 15);
								}*/
								headList.add(stationNumberChina);
								headList.add(longitude);
								headList.add(latitude);
								headList.add(observationTime);//将时间保存到报文表
								aveTowerData.setStationNumberChina(stationNumberChina);
								aveTowerData.setLongitude(Double.parseDouble(longitude));
								aveTowerData.setLatitude(Double.parseDouble(latitude));
								aveTowerData.setHeightOfSationGroundAboveMeanSeaLevel(Double.parseDouble(heightSeaLevel));
								aveTowerData.setTimeInterval(Double.valueOf(timeInterval));
								aveTowerData.setObservationTime(sdf.parse(observationTime));
								if(!TimeCheckUtil.checkTime(sdf.parse(observationTime))){
									ReportError re = new ReportError();
									re.setMessage("DataTime out of range：time:"+sdf.parse(observationTime));
									re.setSegment(lineTxt);
									parseResult.put(re);
									continue;
								}
								//五层的风速（1米、2米、4米、10米、20米），数据单位m/s。
								aveTowerData.setWindSpeed_1(Double.parseDouble(items[1]));
								aveTowerData.setWindSpeed_2(Double.parseDouble(items[2]));
								aveTowerData.setWindSpeed_4(Double.parseDouble(items[3]));
								aveTowerData.setWindSpeed_10(Double.parseDouble(items[4]));
								aveTowerData.setWindSpeed_20(Double.parseDouble(items[5]));
								//三层的风向（1米、4米、20米），数据单位DEC。
								// 修改： //三层的风向（1米、2米、20米），数据单位DEC。
								// 修改：三层的风向（1米、4米、20米），数据单位DEC。
								aveTowerData.setWindDirection_1(Double.parseDouble(items[6]));
								aveTowerData.setWindDirection_4(Double.parseDouble(items[7]));
								aveTowerData.setWindDirection_20(Double.parseDouble(items[8]));
								//五层的温度（1米、2米、4米、10米、20米），数据单位℃。
								aveTowerData.setTemperature_1(Double.parseDouble(items[9]));
								aveTowerData.setTemperature_2(Double.parseDouble(items[10]));
								aveTowerData.setTemperature_4(Double.parseDouble(items[11]));
								aveTowerData.setTemperature_10(Double.parseDouble(items[12]));
								aveTowerData.setTemperature_20(Double.parseDouble(items[13]));
								//五层的湿度（1米、2米、4米、10米、20米），数据单位%。
								aveTowerData.setHumidity_1(Double.parseDouble(items[14]));
								aveTowerData.setHumidity_2(Double.parseDouble(items[15]));
								aveTowerData.setHumidity_4(Double.parseDouble(items[16]));
								aveTowerData.setHumidity_10(Double.parseDouble(items[17]));
								aveTowerData.setHumidity_20(Double.parseDouble(items[18]));
								
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

	
}
