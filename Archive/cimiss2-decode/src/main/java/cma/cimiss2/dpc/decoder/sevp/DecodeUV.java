package cma.cimiss2.dpc.decoder.sevp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.UvMonitorForecastproduct;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
/**
 * 
 * <br>
 * @Title:  DecodeUV.java
 * @Package org.cimiss2.decode.z_sevp_uvra
 * @Description:    TODO(紫外线监测与预报产品资料解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月12日 上午10:11:17   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */



public class DecodeUV {
	
	private ParseResult<UvMonitorForecastproduct>  parseResult = new  ParseResult<UvMonitorForecastproduct>(false);
	/**
	 * 
	 * @Title: decodeFile
	 * @Description: TODO(紫外线监测与预报产品资料解码函数)
	 * @param file 文件对象
	 * @return ParseResult<UvMonitorForecastproduct> 解码结果集
	 * @throws：
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ParseResult<UvMonitorForecastproduct> decodeFile(File file){
		if(file != null && file.exists() && file.isFile()){
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			BufferedReader bufferedReader = null;
			try {
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();    
				@SuppressWarnings("static-access")
				String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while( (lineTxt = bufferedReader.readLine()) != null){	
					if ( !(lineTxt.trim().equals("")) ){
						if(lineTxt.length() >=5){
							try {
								UvMonitorForecastproduct  uv=new UvMonitorForecastproduct();
								String[] items = lineTxt.trim().split("\\s+");
								int length = items.length;//得到报文长度
								switch (length) {
								case 1://长度为1 只有站号,其余为缺测，转为999999
									uv.setStationNumberChina(items[0]); //站号
									parseResult.put(new ReportInfo(uv,lineTxt.toString()));
									parseResult.setSuccess(true);
									uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
									uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值
									uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值									
									parseResult.put(uv);
									parseResult.setSuccess(true);
									break;
                                case 2://长度为2，分3种情况，判断y的值为1，2，3
                                	uv.setStationNumberChina(items[0]);//站号
                                	parseResult.put(new ReportInfo(uv,lineTxt.toString()));
									parseResult.setSuccess(true);
									String i = items[1].substring(0, 1);
									String i1 = items[1].substring(1, 2);
									int int1 = Integer.parseInt(i1);
									if(("1").equals(i) ){//判断y=1，并且等级值X是1-5之间，19999为缺测，转成999999，否则为错误数据
										if (int1>=1&&int1<=5) {
											uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(items[1].substring(1, 2)));//观测值
											uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值
											uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
										}else if("9999".equals(items[1].substring(1))){
											uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
											uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值
											uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
										}else{
											ReportError re = new ReportError();
											re.setMessage("Value format error!");
											re.setSegment(lineTxt);
											parseResult.put(re);
											continue;
										}	
									}else if (("2").equals(i)) {//判断y=2，如果是29999(表示缺测数据)，转成999999
										if (("9999").equals(items[1].substring(1))) {
											uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
											uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值
											uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
										} else {
											uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
											uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(items[1].substring(1)));//观测平均值
											uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
										}
									}else if (("3").equals(i)) {//判断y=3，如果是39999(表示缺测数据)，转成999999
										if (("9999").equals(items[1].substring(1))) {
											uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
											uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值
											uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值					
										} else {
											uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
											uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值
											boolean result=items[3].matches("[0-9]+");
											if (result == true) {
												uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(items[3].substring(1)));//日观测最大值
											}else{
												uv.setUvDailyObserveMaximum(999999.0);
											}}									
									}
									parseResult.put(uv);
									parseResult.setSuccess(true);
									break;	
                                case 3://长度为3，也分三种情况,y值分别为1，2  1，3  2，3
                                	uv.setStationNumberChina(items[0]);//站号
                                	parseResult.put(new ReportInfo(uv,lineTxt.toString()));
									parseResult.setSuccess(true);
                                	String i2 = items[1].substring(1, 2);
									int int2 = Integer.parseInt(i2);
                                	String x = items[1].substring(0, 1);
                                	String y = items[2].substring(0, 1);
                                	//第二个数是1 两种情况1，2 1，3
                                	//当y=1  y=2时
                                	if(("1".equals(x) ) && ("2".equals(y))){
                                		if (int2>=1&&int2<=5) {//判断y=1，并且等级值X是1-5之间，19999为缺测，转成999999，否则为错误数据
                                			uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(items[1].substring(1, 2)));//观测值
                                			if ("9999".equals(items[2].substring(1))) {
                                				uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值   	
                                				uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
											} else {
												uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(items[2].substring(1)));//观测平均值   	
                                				uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
											}
										} else if ("9999".equals(items[1].substring(1))) {
											uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
											if ("9999".equals(items[2].substring(1))) {
                                				uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值  	
                                				uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
											} else {
												uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(items[2].substring(1)));//观测平均值     	
                                				uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
											}
										}else {
											ReportError re = new ReportError();
											re.setMessage("Value format error!");
											re.setSegment(lineTxt);
											parseResult.put(re);
											continue;
										}
                                	//当y=1 y=3时	
                                	}else if (("1".equals(x) ) && ("3".equals(y))) {
                                		if (int2>=1&&int2<=5) {//判断y=1，并且等级值X是1-5之间，19999为缺测，转成999999，否则为错误数据
                                			uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(items[1].substring(1, 2)));//观测值
                                			if ("9999".equals(items[2].substring(1))) {
                                				uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble("")); //观测平均值    	
                                				uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
											} else {
												uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值     	
												boolean result=items[3].matches("[0-9]+");
												if (result == true) {
													uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(items[3].substring(1)));//日观测最大值
												}else{
													uv.setUvDailyObserveMaximum(999999.0);
												}}
										} else if ("9999".equals(items[1].substring(1))) {
											uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
											if ("9999".equals(items[2].substring(1))) {
                                				uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble("")); //观测平均值   	
                                				uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
											} else {
												uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble("")); //观测平均值       	
												boolean result=items[3].matches("[0-9]+");
												if (result == true) {
													uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(items[3].substring(1)));//日观测最大值
												}else{
													uv.setUvDailyObserveMaximum(999999.0);
												}}
										}else {
											ReportError re = new ReportError();
											re.setMessage("Value format error!");
											re.setSegment(lineTxt);
											parseResult.put(re);
											continue;	
										}			
									}
                                	//当y=2 y=3 时  
                                	else{
                                		if ("9999".equals(items[1].substring(1))) {
                                			uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
											uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值  
											if ("9999".equals(items[2].substring(1))) {
	                                			uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
											} else {
												boolean result=items[3].matches("[0-9]+");
												if (result == true) {
													uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(items[3].substring(1)));//日观测最大值
												}else{
													uv.setUvDailyObserveMaximum(999999.0);
												}}
											
										}else{
											uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
											uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(items[1].substring(1)));//观测平均值  
											if ("9999".equals(items[2].substring(1))) {
	                                			uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值
											} else {
												boolean result=items[3].matches("[0-9]+");
												if (result == true) {
													uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(items[3].substring(1)));//日观测最大值
												}else{
													uv.setUvDailyObserveMaximum(999999.0);
												}}
										}
                                		
                                	}
                                	parseResult.put(uv);
    								parseResult.setSuccess(true);
									break;
                                case 4://长度为4,分别判断是否为错误数据、缺测值19999，29999，39999，如果是转成999999
                                	String i3 = items[1].substring(1, 2);
									int int3 = Integer.parseInt(i3);
                                	uv.setStationNumberChina(items[0]); //站号
                                	parseResult.put(new ReportInfo(uv,lineTxt.toString()));
									parseResult.setSuccess(true);
                                	if (int3>=1&&int3<=5) {//判断y=1，并且等级值X是1-5之间，19999为缺测，转成999999，否则为错误数据
                                		uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(items[1].substring(1, 2)));//观测值
									}else if ("9999".equals(items[1].substring(1))) {//缺测数据
										uv.setUvPredictedValue(ElementValUtil.ToBeValidDouble(""));//观测值
									}else {//错误格式数据
										ReportError re = new ReportError();
										re.setMessage("Value format error!");
										re.setSegment(lineTxt);
										parseResult.put(re);
										continue;
									}
                                	if (("9999").equals(items[2].substring(1))) {
                                		uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(""));//观测平均值     		
									}else {
										uv.setUvObserveAverageValue(ElementValUtil.ToBeValidDouble(items[2].substring(1)));//观测平均值
									}
                                	if (("9999").equals(items[3].substring(1))) {
                                		uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(""));//日观测最大值     		
									}else {
										boolean result=items[3].matches("[0-9]+");
										if (result == true) {
											uv.setUvDailyObserveMaximum(ElementValUtil.ToBeValidDouble(items[3].substring(1)));//日观测最大值
										}else{
											uv.setUvDailyObserveMaximum(999999.0);
										}
									}
                                	parseResult.put(uv);
    								parseResult.setSuccess(true);
									break;
								default:
									break;
								}
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digit error!");
								re.setSegment(lineTxt);
								parseResult.put(re);
								continue;
							}
						}else{
						// 报文长度不对
						ReportError re = new ReportError();
						re.setMessage("Report length error!");
						re.setSegment(lineTxt);
						parseResult.put(re);
					}

				}
				
			} 
			}catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
				e.printStackTrace();
			} catch (IOException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				e.printStackTrace();
			} catch (Exception e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				e.printStackTrace();
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
		}else{
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
		
	}
	
	public static void main(String[] args) {
	DecodeUV UVdec = new DecodeUV();
	ParseResult<UvMonitorForecastproduct> parseResult = UVdec.decodeFile(new File
			("D:\\UV\\2019022601\\QBZQ_YC022602.URP"));
	List<UvMonitorForecastproduct> list = parseResult.getData();
	System.out.println(list.size());
	System.out.println(list.get(0).getStationNumberChina());
}
}
