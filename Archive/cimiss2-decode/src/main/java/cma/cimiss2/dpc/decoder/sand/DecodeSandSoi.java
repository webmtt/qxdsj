package cma.cimiss2.dpc.decoder.sand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.sand.SandChnSoi;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

/**
 * <br>
 * @Title:  DecodeSandSoi.java
 * @Package org.cimiss2.decode.z_sand.soi
 * @Description:(沙尘暴土壤湿度解析)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月8日 下午3:45:48   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class DecodeSandSoi {


	private ParseResult<SandChnSoi> parseResult = new ParseResult<SandChnSoi>(false);


	/**
	 * 
	 * @Title: decode
	 * @Description: 土壤湿度观测资料
	 * @param file
	 * @return ParseResult<SandChnSoi>
	 * @throws:
	 */
	public ParseResult<SandChnSoi> decode(File file) {

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
					// 区站号、经度、纬度、海拔高度、文件创建时间、仪器采样时间间隔，平均场采样时间间隔规定为10秒
					// 57131 108.5 34.2 410.0 20171218093000
					// 20171218093000 16.0 20.6 22.5 22.2 19.5
					String head = txtFileContent.get(0);
					String[] heads = head.split("\\s+");
					SandChnSoi sandChnSoi = new SandChnSoi();
					if(heads.length == 5) {
						
						
						String stationNumberC = heads[0];
						String longitude = heads[1];
						String latitude = heads[2];
						String elevationAltitude = heads[3];
						for(int i=1;i<txtFileContent.size();i++){
							String data = txtFileContent.get(i);
							String[] datas = data.split("\\s+");
							
							if (datas.length ==6) {
								String dataTime = datas[0];
								String soilWeightMoistureContent_10 = datas[1];
								String soilWeightMoistureContent_20 = datas[2];
								String soilWeightMoistureContent_30 = datas[3];
								String soilWeightMoistureContent_40 = datas[4];
								String soilWeightMoistureContent_50 = datas[5];
								
								sandChnSoi.setStationNumberC(stationNumberC);
								sandChnSoi.setLongitude(Double.parseDouble(longitude));
								sandChnSoi.setLatitude(Double.parseDouble(latitude));
								sandChnSoi.setElevationAltitude(Double.parseDouble(elevationAltitude));
								sandChnSoi.setObservationTimeInterval(999998.0);
								SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
								try {
									Date d_datetime = sdf.parse(dataTime);
									sandChnSoi.setDataTime(d_datetime);
									sandChnSoi.setReport_time(d_datetime);
									if(!TimeCheckUtil.checkTime(d_datetime)){
										ReportError re = new ReportError();
										re.setMessage("DataTime out of range：time:"+d_datetime);
										re.setSegment(txtFileContent.toString());
										parseResult.put(re);
										continue;
									}
								} catch (ParseException e) {
									ReportError reportError = new ReportError();
									reportError.setMessage("File time error");
									reportError.setSegment(txtFileContent.toString());
									parseResult.put(reportError);
									return parseResult;
								}
								sandChnSoi.setSoilWeightMoistureContent_10(Double.parseDouble(soilWeightMoistureContent_10));
								sandChnSoi.setSoilWeightMoistureContent_20(Double.parseDouble(soilWeightMoistureContent_20));
								sandChnSoi.setSoilWeightMoistureContent_30(Double.parseDouble(soilWeightMoistureContent_30));
								sandChnSoi.setSoilWeightMoistureContent_40(Double.parseDouble(soilWeightMoistureContent_40));
								sandChnSoi.setSoilWeightMoistureContent_50(Double.parseDouble(soilWeightMoistureContent_50));
								
								sandChnSoi.setStationNumberChina(stationNumberC);
								
								parseResult.put(sandChnSoi);
								parseResult.setSuccess(true);
								
								parseResult.put(new ReportInfo<AgmeReportHeader>(sandChnSoi, txtFileContent.get(0) + "\n" + txtFileContent.get(1)));
								
							}else{
								ReportError reportError = new ReportError();
								reportError.setMessage("Element length wrong");
								reportError.setSegment(head+"\n"+data);
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
