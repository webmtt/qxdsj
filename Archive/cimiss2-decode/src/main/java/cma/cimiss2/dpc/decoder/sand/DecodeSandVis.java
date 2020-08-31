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
import cma.cimiss2.dpc.decoder.bean.sand.SandChnVis;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.DateUtil;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

/**
 * <br>
 * @Title:  DecodeSandVis.java
 * @Package org.cimiss2.decode.z_sand.vis
 * @Description:    TODO(沙尘暴大气能见度解析)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年2月8日 下午3:47:58   wangzunpeng    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 */
public class DecodeSandVis {
	private ParseResult<SandChnVis> parseResult = new ParseResult<SandChnVis>(false);
	private final static String DEFAULT_MISS_VALUE = "999999";
	/**
	 * 
	 * @Title: decode
	 * @Description: 土壤湿度观测资料
	 * @param file
	 * @return ParseResult<SandChnSoi>
	 * @throws:
	 */
	public ParseResult<SandChnVis> decode(File file) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		
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
				if (txtFileContent != null && txtFileContent.size() >= 1) {
					for (int i = 0; i < txtFileContent.size(); i++) {
						// 区站号，经度，纬度，海拔高度，文件创建时间，仪器采样时间间隔
						String head = txtFileContent.get(i).replace(",",", ");
						String[] heads= null;
						if (head.contains(",")) {
							heads = head.split(",");
							for(int idx=0;idx<heads.length;idx++){
								   if(heads[idx].equals(" ")){
									   heads[idx]=DEFAULT_MISS_VALUE;
								   }
							   }
						}else{
							heads = head.split("\\s+");
						}
						if (heads.length == 6) {
							String stationNumberC = heads[0]; // 区站号
							String longitude = heads[1]; // 经度
							String latitude = heads[2]; // 纬度
							String elevationAltitude = heads[3]; // 海拔高度

							String observationTimeInterval = heads[5]; // 采样时间间隔

							// txtFileContent.remove(0); //去掉头信息
							for (int j = 1; j < txtFileContent.size(); j++) {
								SandChnVis sandChnVis = new SandChnVis();

								String data = txtFileContent.get(j).replaceAll("\\/+", "999999");
								String[] datas = data.split("\\s+");
								if (datas.length == 4) {
									
									String dataTime = datas[0]; // 资料观测时间
									String averageVisibility_1min = datas[1]; // 1分钟平均能见度，数据单位m
									String averageVisibility_10min = datas[2]; // 10分钟平均能见度，数据单位m
									String trendOfVisibilityChange = datas[3]; // 能见度变化趋势，数据单位%

									sandChnVis.setStationNumberChina(stationNumberC);
									sandChnVis.setLongitude(Double.parseDouble(longitude));
									sandChnVis.setLatitude(Double.parseDouble(latitude));
									sandChnVis.setElevationAltitude(Double.parseDouble(elevationAltitude));
									sandChnVis.setObservationTimeInterval(Double.parseDouble(observationTimeInterval));
									if(dataTime.trim().length() != 14){
										ReportError reportError = new ReportError();
										reportError.setMessage("file  time error");
										reportError.setSegment(txtFileContent.toString());
										parseResult.put(reportError);
										continue;
									}
										
									try {
										Date d_datetime = sdf.parse(dataTime);
										sandChnVis.setD_dateTime(d_datetime);
										sandChnVis.setReport_time(d_datetime);
										if(!TimeCheckUtil.checkTime(d_datetime)){
											ReportError re = new ReportError();
											re.setMessage("DataTime out of range：time:"+d_datetime);
											re.setSegment(txtFileContent.toString());
											parseResult.put(re);
											continue;
										}
									} catch (ParseException e) {
										ReportError reportError = new ReportError();
										reportError.setMessage("file  time error");
										reportError.setSegment(txtFileContent.toString());
										parseResult.put(reportError);
										continue;
//										return parseResult;
									}
									sandChnVis.setAverageVisibility_1min(Double.parseDouble(averageVisibility_1min));
									sandChnVis.setAverageVisibility_10min(Double.parseDouble(averageVisibility_10min));
									sandChnVis.setTrendOfVisibilityChange(Double.parseDouble(trendOfVisibilityChange));

									parseResult.put(sandChnVis);

									parseResult.put(new ReportInfo<AgmeReportHeader>(sandChnVis, head + "\n" + data));
									parseResult.setSuccess(true);
								} else{
									ReportError reportError = new ReportError();
									reportError.setMessage("report element field length error");
									reportError.setSegment(txtFileContent.get(0)+"\n"+txtFileContent.get(j));
									parseResult.put(reportError);
									continue;
								}
							}
							i=txtFileContent.size();
						}else if (heads.length == 9) {
							try {
								SandChnVis Vis = new SandChnVis();
								Vis.setStationNumberChina(heads[0].trim());//站号
								Vis.setProjectCode(Double.parseDouble(heads[1].trim()));//项目代码
								// 年份
								String year = heads[2].trim();
								// 序日
								String julianDay = heads[3].trim();
								// 时分
								String hourMin = heads[4].trim();
								String ymd = DateUtil.convertJulianDay2Date(Integer.parseInt(year), Integer.parseInt(julianDay));
								try {
									Date observationTime = sdf.parse(ymd + hourMin);
									Vis.setD_dateTime(observationTime);//观测时间
									Vis.setReport_time(observationTime);//观测时间
									if(!TimeCheckUtil.checkTime(observationTime)){
										ReportError re = new ReportError();
										re.setMessage("DataTime out of range：time:"+observationTime);
										re.setSegment(txtFileContent.get(i));
										parseResult.put(re);
										continue;
									}
								} catch (ParseException e) {
									ReportError reportError = new ReportError();
									reportError.setMessage("Data time conversion error");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
								Vis.setStateCode(Double.parseDouble(heads[5].trim()));
								Vis.setAverageVisibility_1min(Double.parseDouble(heads[6].trim()));
								Vis.setAverageVisibility_10min(Double.parseDouble(heads[7].trim()));
								Vis.setTrendOfVisibilityChange(Double.parseDouble(heads[8].trim()));
								parseResult.put(Vis);

								parseResult.put(new ReportInfo<AgmeReportHeader>(Vis, txtFileContent.get(i)));
								parseResult.setSuccess(true);
								
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digital conversion exception");
								re.setSegment(txtFileContent.get(i));
								parseResult.put(re);
								continue;
							}
						}else {
							ReportError reportError = new ReportError();
							reportError.setMessage("report header error");
							reportError.setSegment(head);
							parseResult.put(reportError);
						}
					}
				}else {
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
	public static void main(String[] args) {
		DecodeSandVis decodeSandVis = new DecodeSandVis();
		ParseResult<SandChnVis> parseResult = decodeSandVis.decode(new File
				("D:\\Z_SAND_VIS_C5_54857_20190312070000.TXT"));
		List<SandChnVis> list = parseResult.getData();
		System.out.println(list.size());
		System.out.println(list.get(0).getAverageVisibility_10min());
	}
}
