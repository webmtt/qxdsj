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
import cma.cimiss2.dpc.decoder.bean.sevp.FineForecast;
import cma.cimiss2.dpc.decoder.bean.sevp.FineList;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * -------------------------------------------------------------------------------
 * <br>
 * @Title:  DecodeFineForecast.java   
 * @Package org.cimiss2.decode.z_sevp_fine   
 * @Description:
 * 	全国城镇精细化预报指导预报产品			大数据平台	M.0012.0001.R001
	全国城镇精细化预报产品－各省的订正预报产品	大数据平台	M.0012.0002.R001
	全国城镇精细化预报产品－全国共享预报产品		大数据平台	M.0012.0003.R001					  
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月1日 下午2:12:24   maxiyue    Initial creation.
 * </pre>
 * 
 * @author maxiyue
 *---------------------------------------------------------------------------------
 */
public class DecodeFineForecast {
	private  ParseResult<FineForecast> parseResult = new ParseResult<FineForecast>(false);
	/**
	 * 
	 * @Title: decodeFile
	 * @Description: TODO(解码函数)
	 * @param: file  文件对象
	 * @return: ParseResult<FineForecast> 解码结果包裹类
	 * @throws：
	 */
	@SuppressWarnings("resource")
	public  ParseResult<FineForecast> decodeFile(File file)  {
		// 判断文件是否存在
		if (file != null && file.exists() && file.isFile() ) {	
			// 判断文件是否为空
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			Scanner scanner = null;
			//获取文件流
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
				String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				scanner = new Scanner(read).useDelimiter("NNNN"); //用NNNN分割报文
				String report = "";	
				while (scanner.hasNext() && (report = scanner.next()).length()> 5) {
					java.util.Date forecastime=null;
					java.util.Date observetime=null;
					int i =5;//针对每一段分割报文从第五行（站号、经纬度开始解析要素）
					report = report.replaceAll("((\r\n)|\n)[\\s\t ]*(\\1)+", "$1").replaceAll("^((\r\n)|\n)", "");
					String[] reports = report.split("\n");		
					if(reports.length < 7) {
						ReportError re = new ReportError();
						re.setMessage("Report length error!");
						re.setSegment(report);
						parseResult.put(re);
						continue;
					}		
					int count=0;
					//从开始由带站号经纬度的行开始逐行遍历。以报文的长度结束
					for(;count<reports.length-6;count++){
						FineForecast  fine =new FineForecast();
						String correctsign=null;
						String[] rep_list1 = reports[1].trim().split("\\s+");//报头第二行，带（FSCI50 CCCC YYGG （BBB））的行
						String[] rep_list2 = reports[3].trim().split("\\s+");//报头第四行，带（产品代码、年月日时（世界时））的行
						String [] rep_list3 =reports[i].trim().split("\\s+");//首个带有站号，经纬度的行
						try {	
							reports[0] = reports[0].trim();
							//2.第二行进行判断
							if (rep_list1.length == 3) {
								fine.setBul_Center(rep_list1[1]);//编报中心
								correctsign="000";//设置更正标志
							}else if (rep_list1.length == 4) {
								fine.setBul_Center(rep_list1[1]);//编报中心
								fine.setCorrectSign(rep_list1[3]);//更正标志	
								correctsign=rep_list1[3];//设置更正标志
							}else {
								ReportError re = new ReportError();
								re.setMessage("Second line error(FSCI50 CCCC YYGG (BBB))!");
								re.setSegment(report);
								parseResult.put(re);
								//continue;	
								break;
							}
							//处理时间
							if (rep_list1[2].length()==6 && rep_list2[1].length()==10) {
							    List<Date> time;
							    try {
							    	time = ForecastUtil.time_transform(rep_list1[2],rep_list2[1]);
								} catch (Exception e) {
									ReportError re = new ReportError();
									re.setMessage("DateTime format error!");
									re.setSegment(report);
									parseResult.put(re);
									continue;	
								}
								
							    forecastime=time.get(0);
							    observetime=time.get(1);
//							    fine.setForecastTime(observetime);//预报时间
//							    fine.setObservationTime(forecastime);//观测时间
							    // exchange 2019-2-26 chy
							    fine.setForecastTime(forecastime);//预报时间
							    fine.setObservationTime(observetime);//观测时间
							    if(!TimeCheckUtil.checkTime(observetime)){
									ReportError re = new ReportError();
									re.setMessage("DataTime out of range：time:"+observetime);
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
						
							fine.setPROD_DESC(reports[2]);//产品描述
							
							//第四行进行判断						
							if(reports[3].trim().split("\\s+").length == 2){
								fine.setProd_Code(rep_list2[0]);//产品代码
							}else{
								ReportError re = new ReportError();
								re.setMessage("Fourth line error (productCode yyyyMMddHH(Universal Time))!");
								re.setSegment(report);
								parseResult.put(re);
								break;	
							}	
							if(reports[4].trim().split("\\s+").length != 1){
								ReportError re = new ReportError();
								re.setMessage("Fifth line error!");
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
						if(length==6){
							try {
								AgmeReportHeader agmeHeader = new AgmeReportHeader();
								agmeHeader.setStationNumberChina(rep_list3[0]);//站号
								agmeHeader.setLongitude(ForecastUtil.ToBeValidDouble(rep_list3[1]));//经度
								agmeHeader.setLatitude(ForecastUtil.ToBeValidDouble(rep_list3[2]));//纬度
								agmeHeader.setHeightOfSationGroundAboveMeanSeaLevel(ForecastUtil.ToBeValidDouble(rep_list3[3]));//海拔高度
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
								fine.setStationNumberChina(rep_list3[0]);//站号
								fine.setLongitude(ForecastUtil.ToBeValidDouble(rep_list3[1]));//经度
								fine.setLatitude(ForecastUtil.ToBeValidDouble(rep_list3[2]));//纬度
								fine.setHeightOfSationGroundAboveMeanSeaLevel(ForecastUtil.ToBeValidDouble(rep_list3[3]));//海拔高度
								fine.setValidtime_Count(ForecastUtil.ToBeValidDouble(rep_list3[4]));//预报时效个数
								fine.setV_PROD_NUM(ForecastUtil.ToBeValidDouble(rep_list3[5]));//预报产品个数	
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
							split = reports[i+j].trim().split("\\s+");						
							if(split.length==22){
								try {
									FineList list = new FineList();
									list.setValidtime(ForecastUtil.ToBeValidDouble(split[0]));//预报时效
									list.setTEM(ForecastUtil.ToBeValidDouble(split[1]));//温度/气温
									list.setRHU(ForecastUtil.ToBeValidDouble(split[2]));//相对湿度
									list.setWIN_D(ForecastUtil.ToBeValidDouble(split[3]));//风向
									list.setWIN_S(ForecastUtil.ToBeValidDouble(split[4]));//风速
									list.setPRS(ForecastUtil.ToBeValidDouble(split[5]));//气压
									list.setPRE_PRE_Fore(ForecastUtil.ToBeValidDouble(split[6]));//可降水分（预报降水量）
									list.setCLO_Cov(ForecastUtil.ToBeValidDouble(split[7]));//总云量
									list.setCLO_Cov_Low(ForecastUtil.ToBeValidDouble(split[8]));//低云量
									list.setWEP(ForecastUtil.ToBeValidDouble(split[9]));//天气现象
									list.setVIS(ForecastUtil.ToBeValidDouble(split[10]));//水平能见度(人工)
									list.setTEM_Max_24h(ForecastUtil.ToBeValidDouble(split[11]));//未来24小时最高气温
									list.setTEM_Min_24h(ForecastUtil.ToBeValidDouble(split[12]));//未来24小时最低气温
									list.setRHU_Max_24h(ForecastUtil.ToBeValidDouble(split[13]));//24小时最大相对湿度
									list.setRHU_Min_24h(ForecastUtil.ToBeValidDouble(split[14]));//24小时最小相对湿度
									list.setPRE_24h(ForecastUtil.ToBeValidDouble(split[15]));//未来24小时降水量
									list.setPRE_12h(ForecastUtil.ToBeValidDouble(split[16]));//未来12小时降水量
									list.setCLO_Cov_12h(ForecastUtil.ToBeValidDouble(split[17]));//12小时内总云量
									list.setCLO_Cov_Low_12h(ForecastUtil.ToBeValidDouble(split[18]));//12小时内低云量
									list.setWEP_Past_12h(ForecastUtil.ToBeValidDouble(split[19]));//12小时内天气现象
									list.setWIN_PD_12h(ForecastUtil.ToBeValidDouble(split[20]));//12小时内盛行风向
									list.setWIN_S_Max_12h(ForecastUtil.ToBeValidDouble(split[21]));//12小时内最大风速	
									fine.put(list);
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
						parseResult.put(fine);
						parseResult.setSuccess(true);
						j++;
						i=i+j-1;
					}
				}	
			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}catch (Exception e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			}finally {
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
	
	public static void main(String[] args) {
		DecodeFineForecast decodeFineForecast = new DecodeFineForecast();
		decodeFineForecast.decodeFile(new File("C:\\Users\\cuihongyuan\\Desktop\\对比\\服务类 对比验证\\Z_SEVP_C_BABJ_20190224175556_P_RFFC-SCMOC-201902250000-16812.TXT"));
	}
}
