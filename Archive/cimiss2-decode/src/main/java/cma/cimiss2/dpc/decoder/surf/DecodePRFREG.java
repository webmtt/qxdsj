package cma.cimiss2.dpc.decoder.surf;

import java.io.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import cma.cimiss2.dpc.decoder.bean.*;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.surf.PrecipitationObservationDataReg;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil2;



/**
 * Main class of decode the normalfloor per hour data. <br>
 * 质控后区域站雨量数据解码类，解码函数为：DecodePRF（File file）
 * 返回结果集：ParseResult<PrecipitationObservationDataReg>
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 11/22/2017   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *           
 */
public class DecodePRFREG {
	// 存放数据解析的结果集
	private ParseResult<PrecipitationObservationDataReg> parseResult = new ParseResult<PrecipitationObservationDataReg>(false) ;
	
	/**函数名：DecodePRF
	 * @param file 文件对象
	 * 
	 * @return ParseResult<PrecipitationObservationDataReg> 解码结果包裹类
	 */
	public ParseResult<PrecipitationObservationDataReg> DecodePRF(File file){
		String encoding = "utf-8";
		InputStreamReader read = null;
		BufferedReader bufferedReader = null;
		if(file != null && file.exists() && file.isFile()){
			try {
				read = new InputStreamReader(new FileInputStream(file), encoding);
			    bufferedReader = new BufferedReader(read);
				StringBuilder sb = new StringBuilder();
				String lineTxt = null;
				List<String> reports = new ArrayList<>();
				// 读取文件
				StringBuffer reportBuffer = new StringBuffer();
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// 报文格式检查
					lineTxt = lineTxt.trim();

					if (!lineTxt.equals("")) {
						
						String lineTxtTemp = lineTxt.replace(" ", "");
						sb.append(lineTxtTemp.trim());
						reportBuffer.append(lineTxt+"\n");
						if(lineTxt.endsWith("=")) {
							reports.add(reportBuffer.toString());
							reportBuffer.setLength(0);
						}
					}
					
					
				}
				
				String file_content = sb.toString();
				// 判断文件是否正常结束
				if(file_content.length() > 4 && file_content.substring(file_content.length()-4, file_content.length()).equals("NNNN")){
					 String[] sections = file_content.split("=");
					 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
					 // 循环遍历每一段报文，最后一段为文件结束符
					for (int i = 0; i < sections.length-1; i++) {
						Map<String,String> headMap = new HashMap<String, String>();
						ReportInfo<Map<String, String>> reportInfo = new ReportInfo<>();
						 // 判断报文段 是否完整
						if(sections[i].length() == 234){
							PrecipitationObservationDataReg entity = new PrecipitationObservationDataReg();
							// 站号 5 字节
							entity.setStationNumberChina(sections[i].substring(0, 5));
							headMap.put("V01301", sections[i].substring(0, 5));
							DecimalFormat decimalFormat = new DecimalFormat("#.0000");
							
							String lat_str = sections[i].substring(5, 11);
							
							double lat = (double)Float.parseFloat(lat_str.substring(0, 2))
									+ (Float.parseFloat(lat_str.substring(2, 4)) / 60f)
									+ (Float.parseFloat(lat_str.substring(4, 6)) / 3600f);
							headMap.put("V5001",decimalFormat.format(lat));

							// 纬度6字节  度分秒
							//double lat = DecodeUtil.to_latlon(lat_str);
							//headMap.put("V5001", String.format("%.4f", lat));
							if(!Double.isNaN(lat)){
								entity.setLatitude(Double.valueOf(decimalFormat.format(lat)));
							}else {
								ReportError re = new ReportError();
								re.setMessage("纬度转换错误");
								re.setSegment(sections[i]);
								parseResult.put(re);
								continue;
							}
							// 经度 7字节  度分秒
							String lon_str = sections[i].substring(11, 18);
							
							double lon = (double)Float.parseFloat(lon_str.substring(0, 3))
									+ (Float.parseFloat(lon_str.substring(3, 5)) / 60f)
									+ (Float.parseFloat(lon_str.substring(5, 7)) / 3600f);
							headMap.put("V6001", decimalFormat.format(lon));
							
							//double lon = DecodeUtil.to_latlon(lon_str);
							//headMap.put("V6001", String.format("%.4f", lon));
							if(!Double.isNaN(lon)){
								entity.setLongitude(Double.valueOf(decimalFormat.format(lon)));
							}else {
								ReportError re = new ReportError();
								re.setMessage("经度转换错误");
								re.setSegment(sections[i]);
								parseResult.put(re);
								continue;
							}
							// 放大 10 倍
							String height_str = sections[i].substring(18, 23);
							if(DecodeUtil.is_number(height_str)){
								entity.setHeightOfSationGroundAboveMeanSeaLevel(Double.parseDouble(height_str)*0.1);
							}else {
								/*ReportError re = new ReportError();
								re.setMessage("高度转换错误");
								re.setSegment(sections[i]);
								parseResult.put(re);
								continue;*/
								entity.setHeightOfSationGroundAboveMeanSeaLevel(999999.0);
							}
							
							String v_ccc = sections[i].substring(23, 26);
							String time_str = sections[i].substring(26, 40);
							
							try {
								Date date_time = simpleDateFormat.parse(time_str);
								entity.setObservationTime(date_time);
								if(TimeCheckUtil2.timeCheckUtil != null && !TimeCheckUtil2.checkTime(date_time)){
									ReportError re = new ReportError();
									re.setMessage("DataTime out of range：time:"+date_time+" stationCode:"+sections[i].substring(0, 5));
//									re.setSegment(sections[i]);
									parseResult.put(re);
									continue;
								}
								headMap.put("D_DATETIME", time_str);
							} catch (ParseException e) {
								ReportError re = new ReportError();
								re.setMessage("时间格式转换错误");
								re.setSegment(sections[i]);
								parseResult.put(re);
								continue;
							}
							
							String precipitation1Hour_str = sections[i].substring(40, 45);
							String precipitation1Hour_qc_str = sections[i].substring(172, 173);
							if(precipitation1Hour_str.contains("/")){	
								// 数据缺测
								QCElement<Double> qcElement = new QCElement<Double>(999999.0, Integer.parseInt(precipitation1Hour_qc_str), 0, 0);
								entity.setPrecipitation1Hour(qcElement);
							}else {
								
								QCElement<Double> qcElement = new QCElement<Double>(Double.parseDouble(precipitation1Hour_str)/10, Integer.parseInt(precipitation1Hour_qc_str), 0, 0);
								entity.setPrecipitation1Hour(qcElement);
							}
							
							String precipitation1Day_str = sections[i].substring(45, 50);
							String precipitation1Day_qc_str = sections[i].substring(173, 174);
							if(precipitation1Day_str.contains("/")){	
								QCElement<Double> qcElement = new QCElement<Double>(999999.0, Integer.parseInt(precipitation1Day_qc_str), 0, 0);
								entity.setPrecipitation1Day(qcElement);
							}else {
								QCElement<Double> qcElement = new QCElement<Double>(Double.parseDouble(precipitation1Day_str)/10, Integer.parseInt(precipitation1Day_qc_str), 0, 0);
								entity.setPrecipitation1Day(qcElement);
							}
							List<QCElement<Double>> precipitationEveryMinutes = new ArrayList<QCElement<Double>>();
							// 循环读取 分钟雨量及其对应的质量控制码
							for (int j = 0; j < 60; j++) {
								String value = sections[i].substring(50+j*2, 50+(j+1)*2);
								String qc = sections[i].substring(174+j, 174+j+1);
								
								if(value.contains("/")){
									//
									QCElement<Double> qcElement = new QCElement<Double>(999999.0, Integer.parseInt(qc));
									precipitationEveryMinutes.add(qcElement);
								}else if (value.contains(",")) {
									// 降水微量
									QCElement<Double> qcElement = new QCElement<Double>(999990.0, Integer.parseInt(qc));
									precipitationEveryMinutes.add(qcElement);
								}else {
									QCElement<Double> qcElement = new QCElement<Double>(Double.parseDouble(value)/10, Integer.parseInt(qc), 0, 0);
									precipitationEveryMinutes.add(qcElement);
								}
								
							}
							headMap.put("V_BBB",  "000");
							reportInfo.setT(headMap);
							entity.setPrecipitationEveryMinutes(precipitationEveryMinutes);
							parseResult.put(entity);
							parseResult.setSuccess(true);
							reportInfo.setReport(reports.get(i));
							parseResult.put(reportInfo);
							
						}else {
							ReportError re = new ReportError();
							re.setMessage("报文总长度验证错误");
							re.setSegment(sections[i]);
							parseResult.put(re);
							reportInfo.setReport(reports.get(i));
							parseResult.put(reportInfo);
							continue;
						}
					}
				}else {
					parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				}
				
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			} catch (IOException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}
			finally{
				if(read != null){
					try{
						read.close();
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
				if(bufferedReader!=null){
					try{
						bufferedReader.close();
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		}
		
		return parseResult;
	}
	
	public static void main(String[] args) {
		DecodePRFREG decodePRFREG = new DecodePRFREG();
		ParseResult<PrecipitationObservationDataReg> result = decodePRFREG.DecodePRF(new File("D:\\CIMISS2\\Z_SURF_C_BEZZ-REG_20180202045547_O_AWS-PRF_FTM_PQC.txt"));
		List<ReportInfo> reports = result.getReports();
		for(int i=0;i<reports.size();i++){
			String report = reports.get(i).getReport();
			Map<String, Object> repmap = (Map<String, Object>) reports.get(i).getT();
			System.out.println(report);
		}
	}

}
