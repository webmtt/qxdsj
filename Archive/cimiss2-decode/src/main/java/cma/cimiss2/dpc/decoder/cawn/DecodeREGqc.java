package cma.cimiss2.dpc.decoder.cawn;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.REGQuality;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the reactive gas quality control information data <br>
 * 大气成分反应性气体质量控制信息解码类
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.cawn.REGQuality}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * 57494,AAP,201804090000,201804092359,OKOKOK00,么俊峰,[END]<br>
57494,PUMML,201804090000,201804092359,OKOKOK00,么俊峰,[END]<br>
57494,NSD,201804090000,201804092359,OKOKOK00,么俊峰,[END]<br>
 * 
 * <strong>code:</strong><br>
 * DecodeREGqc decodeREGqc = new DecodeREGqc();<br>
 * ParseResult<REGQuality> parseResult = decodeREGqc.DecodeFile(new File(String filepath));<br>
 * List<REGQuality> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).getStationNumberChina());<br>
 * <strong>output:</strong><br>
 * 3<br>
 * 57494<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午3:05:02   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeREGqc{
	/**
	 * 解码结果封装
	 */
	private ParseResult<REGQuality> parseResult = new ParseResult<REGQuality>(false);
	/**
	 * 大气成分反应性气体质量控制信息解码函数   
	 * @param file  待解码文件
	 * @return ParseResult<REGQuality> 解码结果      
	 */
	public ParseResult<REGQuality> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			// 获取文件的编码
			// get file encode
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
			String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
			if(fileCode.equals("ISO8859_1") || fileCode.toUpperCase().startsWith("EUC")){ // fileCode.equals("EUC_JP") || fileCode.endsWith("EUC-TW")
				fileCode = "GBK";
			}
			InputStreamReader read = null;
			BufferedReader bufferedReader = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
			 //获取文件流
			try{
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				String sp[];
				String tmp = "";
				while((lineTxt = bufferedReader.readLine()) != null){
					lineTxt = lineTxt.trim();
					char s =lineTxt.charAt(0);   
					if(s == 65279){    //65279是空字符   
					  if(lineTxt.length() > 1){   
						  lineTxt = lineTxt.substring(1); 
					  }   
					}
					sp = lineTxt.split(",");
					if(sp.length == 7){
						REGQuality regQuality = new REGQuality();
						regQuality.setStationNumberChina(sp[0]);
						regQuality.setObserveItem(sp[1]);
						
						try{
							Date date = new Date();
							date = simpleDateFormat.parse(sp[2]);
							simpleDateFormat.parse(sp[3]);
							regQuality.setObservationTime(date);
							regQuality.setQcInfoStartTime(sp[2]);
							regQuality.setQcInfoEndTime(sp[3]);
						}catch (Exception e) {
							ReportError re = new ReportError();
							re.setMessage("Quality control info start/end time format error!");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;
						}
						
						//2019-7-16 cuihongyuan
						if(!TimeCheckUtil.checkTime(regQuality.getObservationTime())){
							ReportError reportError = new ReportError();
							reportError.setMessage("time check error!");
							reportError.setSegment(lineTxt);
							parseResult.put(reportError);
							continue;
						}
						
						if((tmp = sp[4]).length() == 8){
							regQuality.setRunningState(tmp.substring(0, 2));
							regQuality.setMaintenance(tmp.substring(2, 4));
							regQuality.setPeripheralCondition(tmp.substring(4, 6));
							regQuality.setWeatherPhone(tmp.substring(6, 8));
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Quality control info encode format error!");
							re.setSegment(lineTxt);
							parseResult.put(re);
						}
						regQuality.setAttendantName(sp[5]);
						regQuality.setTextNote(sp[6]);
						parseResult.put(regQuality);
						parseResult.setSuccess(true);
						parseResult.put(new ReportInfo<REGQuality>(regQuality, lineTxt));
					}
					else{
						ReportError re = new ReportError();
						re.setMessage("Number of report segments error!");
						re.setSegment(lineTxt);
						parseResult.put(re);
					}
				}
			}catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			}  catch (Exception e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} finally {
				try {
					if (read != null) {
						read.close();
					}
					if(bufferedReader != null)
						bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	
	public static void main(String[] args) {
		DecodeREGqc decodeREGqc = new DecodeREGqc();
		ParseResult<REGQuality> parseResult = decodeREGqc.DecodeFile(new File
				("D:\\HUAXIN\\Z_CAWN_I_57687_20190824000000_L_LOG-FLD.TXT"));
		List<REGQuality> list = parseResult.getData();
		System.out.println(list.size());
		System.out.println(list.get(0).getStationNumberChina());
	}
}