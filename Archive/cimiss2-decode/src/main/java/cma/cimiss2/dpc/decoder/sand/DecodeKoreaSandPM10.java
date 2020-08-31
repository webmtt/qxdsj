package cma.cimiss2.dpc.decoder.sand;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sand.KoreaSand_PM10;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
/**
 * 
 * <br>
 * @Title:  DecodeKoreaSandPM10.java
 * @Package org.cimiss2.decode.z_sand.koreaPM10
 * @Description:(韩国沙尘暴PM10观测资料解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年4月8日 下午1:51:09   maxiyue   Initial creation.
 * </pre>
 * 
 * @author maxiyue
 */
public class DecodeKoreaSandPM10 {
	/* 存放数据解析的结果集 */
	private ParseResult<KoreaSand_PM10> parseResult = new ParseResult<KoreaSand_PM10>(false);
	private BufferedReader bufferedReader = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
	@SuppressWarnings("static-access")
	public ParseResult<KoreaSand_PM10> DecodeFile(File file) {
		// 判断是否为文件并且是否存在
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
		        String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
				// 获取文件流
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				// 循环读取文件的行
				while ((lineTxt = bufferedReader.readLine()) != null) {
					// 判断文件是否正常结束
					if (lineTxt.trim().endsWith("=")) {
						String[] items = lineTxt.trim().split("#");
						// 判断文件要素完整
						if (items.length == 6) {
							Date observationTime =null;	
							KoreaSand_PM10 pm10 = new KoreaSand_PM10();
							try {
								observationTime =sdf.parse(items[0]);
								pm10.setObservationTime(observationTime);//观测时间
								
								//2019-7-16 cuihongyan
								if(!TimeCheckUtil.checkTime(pm10.getObservationTime())){
									ReportError reportError = new  ReportError();
									reportError.setMessage("time check error!");
									reportError.setSegment(lineTxt);
									parseResult.put(reportError);
									continue;
								}
								
							} catch (Exception e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("Data time conversion error");
								reportError.setSegment(lineTxt);
								parseResult.put(reportError);
								continue;
							}	
							try {
								// 区站号
								pm10.setStationNumberChina(items[1]);
								pm10.setLatitude(Double.parseDouble(items[2]));//纬度
								pm10.setLongitude(Double.parseDouble(items[3]));//经度
								pm10.setAvgConcentration_24Hour(Double.parseDouble(items[4]));//24小时质量浓度平均值
								parseResult.put(pm10);
								parseResult.setSuccess(true);
							} catch (NumberFormatException e) {
								ReportError re = new ReportError();
								re.setMessage("Digital conversion exception");
								re.setSegment(lineTxt);
								parseResult.put(re);
								continue;
							}
						}else{
							// 报文中要素长度验证错误
							ReportError re = new ReportError();
							re.setMessage("report factor length validation error");
							re.setSegment(lineTxt);
							parseResult.put(re);
						}
					} else {
						// 报文中记录没有正常结束
						ReportError re = new ReportError();
						re.setMessage("report total length validation error");
						re.setSegment(lineTxt);
						parseResult.put(re);
					}
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
