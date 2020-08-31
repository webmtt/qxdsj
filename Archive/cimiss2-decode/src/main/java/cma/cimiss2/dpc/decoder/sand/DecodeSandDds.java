package cma.cimiss2.dpc.decoder.sand;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.sand.SandDds;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

/**
 * 
 * <br>
 * @Title:  DecodeSandDds.java
 * @Package org.cimiss2.decode.z_sand.dds
 * @Description:    (沙尘暴大气降尘总量（DDS）解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月19日 上午10:58:58   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class DecodeSandDds {
	
	private ParseResult<SandDds> parseResult = new ParseResult<SandDds>(false);

	/**
	 * 
	 * @Title: decodeFile
	 * @Description: (沙尘暴大气降尘总量（DDS）资料解码函数)
	 * @param file 文件对象
	 * @return ParseResult<SandDds> 解码结果集
	 * @throws：
	 */
	public ParseResult<SandDds> decodeFile(File file) {

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
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					// 区站号，经度，纬度，海拔高度，文件创建时间
					String head = txtFileContent.get(0);
					String[] heads = head.split("\\s+");
					if (heads.length == 5) {			
						String stationNumberC = heads[0]; // 区站号
						String longitude = heads[1]; // 经度
						String latitude = heads[2]; // 纬度
						String heightOfSationGroundAboveMeanSeaLevel = heads[3]; // 海拔高度
						Date observationTime =null;	
						// txtFileContent.remove(0); //去掉头信息
						for (int i = 1; i < txtFileContent.size(); i++) {
							SandDds sandDds = new SandDds();

							String data = txtFileContent.get(i);
							String[] datas = data.split("\\s+");
							if (datas.length==11) {
								try {
									sandDds.setStationNumberChina(stationNumberC);//区站号
									sandDds.setLongitude(ToBeValidDouble(longitude));//经度
									sandDds.setLatitude(ToBeValidDouble(latitude));//纬度
									sandDds.setHeightOfSationGroundAboveMeanSeaLevel(ToBeValidDouble(heightOfSationGroundAboveMeanSeaLevel));//测站高度
									try {
										observationTime =sdf.parse(datas[2]);
										sandDds.setObservationTime(observationTime);//观测时间
										sandDds.setReport_time(observationTime);//报文时间
										
										//2019-7-16 cuihongyan
										if(!TimeCheckUtil.checkTime(sandDds.getObservationTime())){
											ReportError reportError = new  ReportError();
											reportError.setMessage("time check error!");
											reportError.setSegment(data);
											parseResult.put(reportError);
											continue;
										}
										
									} catch (Exception e) {
										ReportError reportError = new ReportError();
										reportError.setMessage("Time conversion error");
										reportError.setSegment(txtFileContent.toString());
										parseResult.put(reportError);
										continue;
									}						
									sandDds.setSampMe_Num(ToBeValidDouble(datas[0]));//采样膜编号
									sandDds.setStime(datas[1]);//开始时间
									sandDds.setEtime(datas[2]);//结束时间
									sandDds.setDustCol_Num(ToBeValidDouble(DustCol_Num(datas[3])));//集尘缸编号
									sandDds.setDustCol_Area(ToBeValidDouble(datas[4]));//集尘缸口面积
									sandDds.setCUSO4_Wei(ToBeValidDouble(datas[5]));//硫酸铜加入量
									sandDds.setAccumulateDays(ToBeValidDouble(datas[6]));//采样累积天数
									sandDds.setPRS_Avg(ToBeValidDouble(datas[7]));//平均气压
									sandDds.setTEM_Avg(ToBeValidDouble(datas[8]));//平均气温
									sandDds.setSamp_Wei(ToBeValidDouble(datas[9]));//最终样品重量
									sandDds.setDustDep_Atsph(ToBeValidDouble(datas[10]));//大气降尘量
									parseResult.put(sandDds);
									parseResult.put(new ReportInfo<AgmeReportHeader>(sandDds, head + "\n" + data));
									parseResult.setSuccess(true);
								}
								catch (NumberFormatException e) {
									ReportError re = new ReportError();
									re.setMessage("Digital conversion exception");
									re.setSegment(txtFileContent.get(i));
									parseResult.put(re);
									continue;
								}
							} else {
								ReportError reportError = new ReportError();
								reportError.setMessage("Factor length error");
								reportError.setSegment(txtFileContent.toString());
								parseResult.put(reportError);
								continue;
							}
							
						}
						
					} else {
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
	/**
	 * 
	 * @Title: ToBeValidDouble
	 * @Description: (缺测（///）转换函数)
	 * @param str
	 * @return double
	 * @throws：
	 */
	public static double ToBeValidDouble(String str) {
		if (str.contains("/")) {
			return 999999;			
		}
		else{
			return Double.parseDouble(str);
		}
	}
	
	/**
	 * 
	 * @Title: DustCol_Num
	 * @Description: (集尘缸编号转换函数)
	 * @param str
	 * @return String
	 * @throws：
	 */
	public static String DustCol_Num (String str) {
		String[] strTemp = str.split("-");//用-分割，如果长度为1，str直接返回，否则，返回数组第一个值。
		if (strTemp.length==1) {
			return str;
			
		}else {
			return strTemp[0];
		}
		
	}	
}
