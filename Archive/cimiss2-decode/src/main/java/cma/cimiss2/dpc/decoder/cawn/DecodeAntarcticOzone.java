package cma.cimiss2.dpc.decoder.cawn;

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
import cma.cimiss2.dpc.decoder.bean.cawn.AntarcticOzone;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

/**
 * 
 * <br>
 * @Title:  DecodeAntarcticOzone.java
 * @Package org.cimiss2.decode.z_cawn.ozone
 * @Description:(南极臭氧资料解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月12日 下午6:12:52   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class DecodeAntarcticOzone {
	private ParseResult<AntarcticOzone> parseResult = new ParseResult<AntarcticOzone>(false);
	/**
	 * 
	 * @Title: decodeFile
	 * @Description:(南极臭氧资料解码函数)
	 * @param file
	 * @return ParseResult<AntarcticOzone>
	 * @throws：
	 */
	public ParseResult<AntarcticOzone> decodeFile(File file) {
		
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
				// 首先判断文件不是空的，然后需要判断最少有一行数据
				if (txtFileContent != null && txtFileContent.size() >= 5) {
					String stationNumberChina=null;
					double  latitude=999999;
					double  lontitude=999999;
					String year="999999";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
					String[] line_one=txtFileContent.get(0).trim().split("\\s+");//获取报文第一行
					if (line_one.length!=5 || line_one[0].compareTo("Ozone")!=0 || line_one[3].compareTo("Zhongshan")!=0 ) {
						ReportError reportError = new ReportError();
						reportError.setMessage("First line error!");
						reportError.setSegment(txtFileContent.get(0));
						parseResult.put(reportError);
					}
					String[] line_two=txtFileContent.get(1).trim().replaceAll("\\(|\\)", "").split("\\s+");//获取报文第二行
					if (line_two.length == 5) {
						 stationNumberChina=line_two[0];//站号
					     latitude = change_latorlon(line_two[1]);//纬度
					     lontitude = change_latorlon(line_two[2]);//经度
					}else{
						ReportError reportError = new ReportError();
						reportError.setMessage("Second line error!");
						reportError.setSegment(txtFileContent.get(1));
						parseResult.put(reportError);
					}
					String[] line_three=txtFileContent.get(2).trim().split("\\s+");//获取报文第三行
					if (line_three.length == 4) {
						 year = line_three[3];//获取年
					}else{
						ReportError reportError = new ReportError();
						reportError.setMessage("Third line error!");
						reportError.setSegment(txtFileContent.get(2));
						parseResult.put(reportError);
					}
					for (int i = 4; i < txtFileContent.size(); i++) {
						AntarcticOzone ozone = new AntarcticOzone();
						String[] list=txtFileContent.get(i).trim().split("\\s+");
						if (list.length==4) {
							try {
								String month = change_month(list[0]);//获得月
								String day = list[1];//获得日
								if(day.length()==1){
									day=0+day;
								}//如果日期是1-9，转成01-09
								ozone.setStationNumberChina(stationNumberChina);//站号
								ozone.setLatitude(latitude);//纬度
								ozone.setLongitude(lontitude);//经度
								ozone.setO3_Ds(Double.parseDouble(list[2]));//Ds臭氧总量
								ozone.setO3_Zs(Double.parseDouble(list[3]));//Zs臭氧总量
								try {
									Date observationTime = sdf.parse(year+month+day);
									ozone.setObservationTime(observationTime);//观测时间
									ozone.setReport_time(observationTime);//观测时间
									
								//  2019-7-16 cuihongyuan
									if(!TimeCheckUtil.checkTime(ozone.getObservationTime())){
										ReportError re = new ReportError();
										re.setMessage("check time error!");
										re.setSegment(txtFileContent.get(i));
										parseResult.put(re);
										continue;
									}
									
									
								} catch (ParseException e) {
									ReportError reportError = new ReportError();
									reportError.setMessage("DataTime error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
								parseResult.put(ozone);
								parseResult.put(new ReportInfo<AgmeReportHeader>(ozone,txtFileContent.get(1)+"\n"+txtFileContent.get(i)));
								parseResult.setSuccess(true);
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digit error!");
								re.setSegment(txtFileContent.get(i));
								parseResult.put(re);
								continue;
							}
						}else if (list.length==3) {
							try {
								String month = change_month(list[0]);//获得月
								String day = list[1];//获得日
								if(day.length()==1){
									day=0+day;
								}//如果日期是1-9，转成01-09
								ozone.setStationNumberChina(stationNumberChina);//站号
								ozone.setLatitude(latitude);//纬度
								ozone.setLongitude(lontitude);//经度
								String temp =txtFileContent.get(i).substring(txtFileContent.get(i).indexOf(list[1])+1);
								char tmp = temp.charAt(2);
								Boolean aBoolean = Character.isDigit(tmp);
								if (aBoolean) {
									ozone.setO3_Ds(Double.parseDouble(list[2]));//Ds臭氧总量
								}else{
									ozone.setO3_Zs(Double.parseDouble(list[2]));//Zs臭氧总量
								}
								try {
									Date observationTime = sdf.parse(year+month+day);
									ozone.setObservationTime(observationTime);//观测时间
									ozone.setReport_time(observationTime);//观测时间
								//  2019-7-16 cuihongyuan
									if(!TimeCheckUtil.checkTime(ozone.getObservationTime())){
										ReportError re = new ReportError();
										re.setMessage("check time error!");
										re.setSegment(txtFileContent.get(i));
										parseResult.put(re);
										continue;
									}
								} catch (ParseException e) {
									ReportError reportError = new ReportError();
									reportError.setMessage("DataTime error!");
									reportError.setSegment(txtFileContent.get(i));
									parseResult.put(reportError);
									continue;
								}
								parseResult.put(ozone);
								parseResult.put(new ReportInfo<AgmeReportHeader>(ozone,txtFileContent.get(1)+"\n"+txtFileContent.get(i)));
								parseResult.setSuccess(true);
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digit error!");
								re.setSegment(txtFileContent.get(i));
								parseResult.put(re);
								continue;
							}
						} else {
							ReportError reportError = new ReportError();
							reportError.setMessage("Number of segments error!");
							reportError.setSegment(txtFileContent.toString());
							parseResult.put(reportError);
							continue;
						}
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
	/**
	 * 
	 * @Title: field
	 * @Description:(月份转换)
	 * @param key
	 * @return String
	 * @throws：
	 */
	public String change_month(String month){
		switch(month) 
		{ 
		case "Jan": 
			return  "01"; 
		
		case "Feb": 
			return  "02"; 
		
		case "Mar": 
			return  "03"; 
			
		case "Apr": 
			
			return "04"; 
		case "May": 
			return "05"; 
			
		case "Jun": 
			return "06";
		case "Jul": 
			return "07"; 
		
		case "Aug": 
			return "08"; 
		
		case "Sep": 
			return "09"; 
			
		case "Oct": 
			return "10"; 
			
		case "Nov": 
			return "11"; 
			
		case "Dec": 
			return "12";
		default: 
			return "999999";
		}
	}
	/**
	 * 
	 * @Title: change_latorlon
	 * @Description:(经纬度转换函数)
	 * @param latorlon
	 * @return double
	 * @throws：
	 */
	private double change_latorlon(String latorlon) {
		double lat_lon = 0;
		String latorlon_1 = latorlon.substring(0,2);
		String latorlon_2 = latorlon.substring(3,6);
		String latorlon_3 = latorlon_1+"."+latorlon_2;
		if (latorlon.endsWith("S")) {
		    lat_lon = -(Double.parseDouble(latorlon_3));
		}else{
			lat_lon = Double.parseDouble(latorlon_3);
		}
		return lat_lon;
		
	}
}
