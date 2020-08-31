package cma.cimiss2.dpc.decoder.sevp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.sevp.Forecast6h;
import cma.cimiss2.dpc.decoder.bean.sevp.List6h;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
/**
 * 
 * <br>
 * @Title:  DecodeForecast6h.java
 * @Package org.cimiss2.decode.z_sevp_forecast6h
 * @Description:    
 * 	大城市逐6小时精细化气象要素指导预报产品				大数据平台	M.0032.0001.R001
	大城市逐6小时精细化气象要素预报产品－各省的订正预报产品	大数据平台	M.0032.0002.R001
	大城市逐6小时精细化气象要素预报产品－全国共享预报产品		大数据平台	M.0032.0003.R001
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月8日 下午5:36:38   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class DecodeForecast6h {
	private  ParseResult<Forecast6h> parseResult = new ParseResult<Forecast6h>(false);
	/**
	 * 
	 * @Title: decodeFile
	 * @Description: TODO(解码函数)
	 * @param: file  文件对象
	 * @return: ParseResult<Forecast6h> 解码结果包裹类
	 * @throws：
	 */
	@SuppressWarnings("resource")
	public  ParseResult<Forecast6h> decodeFile(File file)  {
		if (file != null && file.exists() && file.isFile()) {	
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			Scanner scanner = null;
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
				String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
				//获取文件流
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("NNNN"); //用NNNN分割报文
				String report = "";	
				while (scanner.hasNext() && (report = scanner.next()).length()> 5) {
					java.util.Date forecastime=null;
					java.util.Date observetime=null;
					int i =7;//针对每一段分割报文从第五行（站号、经纬度开始解析要素）
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String[] reports = report.split("\n");		
					if(reports.length < 9) {
						ReportError re = new ReportError();
						re.setMessage("Report length error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}		
					int count=0;
					//从开始由带站号经纬度的行开始逐行遍历。以报文的长度结束
					for(;count<reports.length-8;count++){
						Forecast6h  forecast6h =new Forecast6h();
						String correctsign=null;
						String[] rep_list1 = reports[1].split("\\s+");//报头第二行，带（FSCI50 CCCC YYGG （BBB））的行
						String[] rep_list2 = reports[3].split("\\s+");//报头第四行，带（产品代码、年月日时（世界时））的行
						String[] rep_list3 = reports[i].split("\\s+");//首个带有站号，经纬度的行
						String[] rep_list4 = reports[4].split("\\s+");//报头第五行。
						try {	
							reports[0] = reports[0].trim();
							//2.第二行进行判断
							if (rep_list1.length == 3) {
								forecast6h.setBul_Center(rep_list1[1]);//编报中心
								correctsign="000";//设置更正标志
							}else if (rep_list1.length == 4) {
								forecast6h.setBul_Center(rep_list1[1]);//编报中心
								forecast6h.setCorrectSign(rep_list1[3]);//更正标志	
								correctsign=rep_list1[3];//设置更正标志
							}else {
								ReportError re = new ReportError();
								re.setMessage("Second line error(FSCI50 CCCC YYGGgg (BBB))!");
								re.setSegment(report);
								parseResult.put(re);
								break;	
							}
							//处理时间
							if (rep_list1[2].length()==6 && rep_list2[1].length()==10) {
							    List<Date> time;
							    try {
							    	time = ForecastUtil.time_transform(rep_list1[2],rep_list2[1]);
								} catch (Exception e) {
									ReportError re = new ReportError();
									re.setMessage("Datatime error!");
									re.setSegment(report);
									parseResult.put(re);
									continue;	
								}
							    forecastime=time.get(0);
							    observetime=time.get(1);
							    forecast6h.setForecastTime(observetime);//预报时间
							    forecast6h.setObservationTime(forecastime);//观测时间
							    if(!TimeCheckUtil.checkTime(forecastime)){
									ReportError re = new ReportError();
									re.setMessage("DataTime out of range：time:"+forecastime);
									re.setSegment(report);
									parseResult.put(re);
									break;
								}
							}else{
								ReportError re = new ReportError();
								re.setMessage("Report header time error!");
								re.setSegment(report);
								parseResult.put(re);
								break;	
							}
							//第三行进行判断
							forecast6h.setPROD_DESC(reports[2]);//产品描述
							//第四行进行判断						
							if(reports[3].split("\\s+").length == 2){
								forecast6h.setProd_Code(rep_list2[0]);//产品代码
							}else{
								ReportError re = new ReportError();
								re.setMessage("Fourth line error(ProductCode yyyyMMddHH(UT))!");
								re.setSegment(report);
								parseResult.put(re);
								break;	
							}	
							if(reports[4].split("\\s+").length != 1){
								ReportError re = new ReportError();
								re.setMessage("Fifth line error!");
								re.setSegment(report);
								parseResult.put(re);
								break;
							}
							if(reports[5].split("\\s+").length != Integer.parseInt(rep_list4[0])){
								ReportError re = new ReportError();
								re.setMessage("Sixth line error!");
								re.setSegment(report);
								parseResult.put(re);
							    break;
							}
							if(reports[6].split("\\s+").length != 1){
								ReportError re = new ReportError();
								re.setMessage("Seventh line error!");
								re.setSegment(report);
								parseResult.put(re);
								break;
							}
						} catch (NumberFormatException e) {
							ReportError re = new ReportError();
							re.setMessage("Digit error!");
							re.setSegment(report);
							parseResult.put(re);
							continue;
						}			
						String[] split = new String[10000];
					    int length =rep_list3.length;//判断带有站号、经纬度的行的长度
						if(length==5){
							try {
								AgmeReportHeader agmeHeader = new AgmeReportHeader();
								agmeHeader.setStationNumberChina(rep_list3[0]);//站号
								agmeHeader.setLongitude(ElementValUtil.ToBeValidDouble(rep_list3[1]));//经度
								agmeHeader.setLatitude(ElementValUtil.ToBeValidDouble(rep_list3[2]));//纬度
								agmeHeader.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(rep_list3[3]));//海拔高度
								agmeHeader.setReport_time(observetime);//观测时间
								agmeHeader.setForecast_time(forecastime);//预报时间
								agmeHeader.setCropType(rep_list2[0]);//报告类别
								agmeHeader.setCorrectsign(correctsign);//更正标志
								agmeHeader.setBul_center(rep_list1[1]);//编报中心
								agmeHeader.setTTAAii(rep_list1[0]);//TTAAii
								//调用保存报文的方法  
								String savereport = ForecastUtil.savereport(i,Integer.parseInt(rep_list3[4]),reports);
								parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader,savereport.toString()));
								parseResult.setSuccess(true);
								forecast6h.setStationNumberChina(rep_list3[0]);//站号
								forecast6h.setLongitude(ElementValUtil.ToBeValidDouble(rep_list3[1]));//经度
								forecast6h.setLatitude(ElementValUtil.ToBeValidDouble(rep_list3[2]));//纬度
								forecast6h.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(rep_list3[3]));//海拔高度
								forecast6h.setValidtime_Count(ElementValUtil.ToBeValidDouble2(rep_list3[4]));//预报时效个数
								forecast6h.setV_PROD_NUM(ElementValUtil.ToBeValidDouble2(rep_list4[0])+1);//预报产品个数	
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digit error!");
								re.setSegment(report);
								parseResult.put(re);
								continue;
							} 
						}else{
							ReportError re = new ReportError();
							re.setMessage("Report header format error!");
							re.setSegment(report);
							parseResult.put(re);
							i++;
							continue;			
						}
						//遍历预报时效个数
						int Validtime_Count = Integer.parseInt(rep_list3[4]);
						int j=1;				
						for( ;j<=Validtime_Count;j++){
							split = reports[i+j].split("\\s+");						
							if(split.length==7){
								try {
									List6h list = new List6h();
									list.setValidtime(ForecastUtil.ToBeValidDouble(split[0]));//预报时效
									list.setWEP_Past_6h(ForecastUtil.ToBeValidDouble(split[1]));//6小时内天气现象
									list.setTEM_Max_6h(ForecastUtil.ToBeValidDouble(split[2]));//6小时最高温度
									list.setTEM_Min_6h(ForecastUtil.ToBeValidDouble(split[3]));//6小时最低温度
									list.setWIN_PD_6h(ForecastUtil.ToBeValidDouble(split[4]));//6小时内盛行风向
									list.setWIN_S_Max_6h(ForecastUtil.ToBeValidDouble(split[5]));//6小时内最大风速
									
									if (Double.parseDouble(split[6])==0.0){//过去6小时降水量解码规则：编码值=0.0，取微量（999990）；编码值=999.9，取0.0。
										list.setPRE_6h(new Double(999990));//过去6小时降水量
									}else if(Double.parseDouble(split[6])==999.9){
										list.setPRE_6h(new Double(0.0));//过去6小时降水量
									}else{
										list.setPRE_6h(ForecastUtil.ToBeValidDouble(split[6]));
									}
									
									forecast6h.put(list);
									count++;
								} catch (NumberFormatException e) {
									ReportError re = new ReportError();
									re.setMessage("Digit error!");
									re.setSegment(report);
									parseResult.put(re);
									continue;
								} 
							}else{
								ReportError re = new ReportError();
								re.setMessage("Element length error!");
								re.setSegment(report);
								parseResult.put(re);	
								continue;								
							}			
						}//for循环结束
						parseResult.put(forecast6h);
						parseResult.setSuccess(true);
						j++;
						i=i+j-1;		
					}
				}	
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
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
					if(scanner != null)
						scanner.close();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else{
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);	
		}		
		return parseResult;
			
	}
}
