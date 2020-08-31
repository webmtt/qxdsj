package cma.cimiss2.dpc.decoder.sand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.language.bm.Rule.RPattern;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.AtmosphericTurbidityData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

public class DecodeAtmosphTurbidity {
	/* 存放数据解析的结果集 */
	private ParseResult<AtmosphericTurbidityData> parseResult = new ParseResult<AtmosphericTurbidityData>(false);

	/**
	 * 大气浑浊度观测资料数据
	 * 函数名：DecodeFile
	 * dengyonglaing
	 * @param file
	 *            文件对象
	 * 
	 * @return ParseResult<PrecipitationObservationDataReg> 解码结果包裹类
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ParseResult<AtmosphericTurbidityData>  DecodeFile(File file) {

		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			try {
				// get file encode
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
				String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
				fileCode = fileCode.equals("ISO8859_1") ? "GBK" : fileCode;

				List<String> txtFileContent = FileUtil.getTxtFileContent(file, fileCode);
				// 首先判断文件不是空的，然后需要判断最少有两行数据
				if (txtFileContent != null && txtFileContent.size() >= 2) {
					
					String head = txtFileContent.get(0);
					String[] heads = head.split("\\s+");
					//SandChnSoi sandChnSoi = new SandChnSoi();
					if(heads.length == 6) {
						String stationNumberChina = null;
						String latitude = null;
						String longitude = null;
						String heightSeaLevel = null;
						String  timeInterval = null;
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
						
						stationNumberChina = heads[0];//区站号
						longitude = heads[1];//经度
						latitude= heads[2];//纬度
						heightSeaLevel = heads[3];//海拔高度
						timeInterval= heads[5];//浊度计采样时间间隔
						for(int i=1;i<txtFileContent.size();i++){
							ArrayList<String> headList=new ArrayList<String>();
							String data = txtFileContent.get(i);
							data = data.replaceAll("\\/+", "999999");
							String[] items = data.split("\\s+");
							
							if (items.length ==7) {
								AtmosphericTurbidityData turbidityBean = new AtmosphericTurbidityData();
								if((!items[1].equals("999999")) || (!items[2].equals("999999"))|| (!items[3].equals("999999"))|| (!items[4].equals("999999"))|| (!items[5].equals("999999"))|| (!items[6].equals("999999"))){
									try {
										String observationTime=items[0];
										if (items[0].substring(0, 4).compareTo("1900")<0 || observationTime.length() !=14) {
											ReportError re = new ReportError();
					   						re.setMessage("time error");
					   						re.setSegment(head);
					   						parseResult.put(re);	
											continue;
										}
										headList.add(stationNumberChina);
										headList.add(longitude);
										headList.add(latitude);
										headList.add(observationTime);//将时间保存到报文表
										turbidityBean.setStationNumberChina(stationNumberChina);
										turbidityBean.setLongitude(Double.parseDouble(longitude));
										turbidityBean.setLatitude(Double.parseDouble(latitude));
										turbidityBean.setHeightOfSationGroundAboveMeanSeaLevel(Double.parseDouble(heightSeaLevel));
										turbidityBean.setTimeInterval(Double.valueOf(timeInterval));
										try {
											turbidityBean.setObservationTime(sdf.parse(observationTime));
											
											//2019-7-16 cuihongyan
											if(!TimeCheckUtil.checkTime(turbidityBean.getObservationTime())){
												ReportError reportError = new  ReportError();
												reportError.setMessage("time check error!");
												reportError.setSegment(data);
												parseResult.put(reportError);
												continue;
											}
											
										}catch (ParseException e) {
											ReportError reportError = new ReportError();
											reportError.setMessage("File time error");
											reportError.setSegment(txtFileContent.toString());
											parseResult.put(reportError);
											continue;
										}
										
										//数据识别码。
										turbidityBean.setData_code(items[1]);
										//粒子散射系数，数据单位m。
										turbidityBean.setParticleScatterCoefficient(ElementValUtil.ToBeValidDouble(items[2]));
										//环境温度，数据单位℃。
										turbidityBean.setTemperature(ElementValUtil.ToBeValidDouble(items[3]));
										//环境相对湿度，数据单位%。
										turbidityBean.setHumidity(ElementValUtil.ToBeValidDouble(items[4]));
										//环境气压，数据单位hPa。
										turbidityBean.setPressure(ElementValUtil.ToBeValidDouble(items[5]));
										//腔体温度，数据单位℃。
										turbidityBean.setCavityTemperature(ElementValUtil.ToBeValidDouble(items[6]));
															
										parseResult.put(new ReportInfo(headList,head+"\n"+data));
										parseResult.put(turbidityBean);
										parseResult.setSuccess(true);

									} catch (NumberFormatException e) {
										parseResult.put(new ReportInfo(headList,head+"\n"+data));
										ReportError re = new ReportError();
										re.setMessage("Digital conversion exception");
										re.setSegment(data);
										parseResult.put(re);
										continue;
									}
								}else{
									ReportError re = new ReportError();
									re.setMessage("All the elements are missing");
									re.setSegment(data);
									parseResult.put(re);
									continue;
								}
								
							}else{
								ReportError reportError = new ReportError();
								reportError.setMessage("Element length wrong");
								reportError.setSegment(head+data);
								parseResult.put(reportError);
								
							}
						}
							
					}else {
						ReportError reportError = new ReportError();
						reportError.setMessage("report header error");
						reportError.setSegment(head);
						parseResult.put(reportError);
					}

				} else {
					if (txtFileContent == null || txtFileContent.size() == 0) {
						// 空文件
						parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
					} else {
						// 数据只有一行，格式不正确
						parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
					}
				}
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}
		} else {
			// file not exsit
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}

}
