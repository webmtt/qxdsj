package cma.cimiss2.dpc.decoder.upar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;

import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.upar.Upar_zkyLight;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the ZKY Lighting data<br>
 * 中科院闪电数据解码
 *
 * <p>
 * notes:
 * 解码程序分为两个步骤:数据读取及预处理、实体对象构建及赋值。
 * <ul>
 * <li>数据读取及预处理过程：
 * <ol>
 * <li>所有运算采用java double进行基本运算。
 * <li>无特殊说明时，所有包含”/”的数据全部转化成999999。
 * <li>未通过数据校验时，该条数据将会被当成错误报文处理。
 * </ol>
 * </li>
 * <li>实体对象构建及赋值：
 * <ol>
 * <li>校验所有段的数据的数据类型是否合法。
 * <li>所有的赋值失败都会导致解码错误，该条数据将被当成错误报文处理。
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.upar.Upar_zkyLight}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * [{"LATITUDE":"11.3410","LONGITUDE":"91.4860","INTENS":"155.70","SLOPE":"0","ERROR":"0","LOCATION":"6","DATETIME":"2018/2/5 5:19:21","HOUR":"5","MINUTE":"19","SECOND":"21","MINISECOND":"130506","PROVINCE":"","DISTRICT":"","COUNTRY":"","INPUTTIME":"2018/2/5 5:19:31","PROCESSFLAG":"1","USEDIDS":"190,188,156,157,200,150,185,171","CG_IC":"IC","HEIGHT":"7.2310"},{"LATITUDE":"8.3840","LONGITUDE":"96.1320","INTENS":"179.40","SLOPE":"0","ERROR":"0","LOCATION":"6","DATETIME":"2018/2/5 5:20:32","HOUR":"5","MINUTE":"20","SECOND":"32","MINISECOND":"5787642","PROVINCE":"","DISTRICT":"","COUNTRY":"","INPUTTIME":"2018/2/5 5:20:43","PROCESSFLAG":"1","USEDIDS":"189,188,186,156,200,157,152,187","CG_IC":"IC","HEIGHT":"8.4750"}]<br>
 * 
 * <strong>code:</strong><br>
 * DecodeZKYLight decodeZKYLight = new DecodeZKYLight();<br>
 * ParseResult<Upar_zkyLight> parseResult = decodeZKYLight.DecodeFile(new File(String filepath));<br>
 * List<Upar_zkyLight> list = parseResult.getData();<br>
 * System.out.println(list.size());<br>
 * System.out.println(list.get(0).getLatitude());<br>
 * 
 * <strong>output:</strong><br>
 * 2<br>
 * 11.341<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 下午5:38:23   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeZKYLight{
	/**
	 * 解码结果集
	 */
	private ParseResult<Upar_zkyLight> parseResult = new ParseResult<Upar_zkyLight>(false);
	/**
	 * 缺省值
	 */
	private int defaultValue = 999999;
	/**
	 * 中科院闪电数据解码函数   
	 * @param file 待解析文件
	 * @return ParseResult<Upar_zkyLight>    解码结果集  
	 */
	public ParseResult<Upar_zkyLight> DecodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			
			// 获取文件的编码
			FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
			String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			String content= "";
			try{
				content = FileUtils.readFileToString(file, fileCode);
				JSONArray jsonArray = JSONArray.parseArray(content);
				int size = jsonArray.size();
				for(int i = 0; i < size; i ++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					//{"LATITUDE":"9.75","LONGITUDE":"88.4840","INTENS":"189.10","SLOPE":"0","ERROR":"0","LOCATION":"6",
					//"DATETIME":"2018/2/4 23:16:56","HOUR":"23","MINUTE":"16","SECOND":"56","MINISECOND":"5979240",
					//"PROVINCE":"","DISTRICT":"","COUNTRY":"","INPUTTIME":"2018/2/4 23:17:07","PROCESSFLAG":"1",
					//"USEDIDS":"190,150,154,185","CG_IC":"IC","HEIGHT":"7.2740"}
					Upar_zkyLight upar_zkyLight = new Upar_zkyLight();
					try{
						upar_zkyLight.setLatitude(getValueF(jsonObject.get("LATITUDE").toString()));
						upar_zkyLight.setLongtitude(getValueF(jsonObject.get("LONGITUDE").toString()));
						upar_zkyLight.setCurrentIntensity(getValueF(jsonObject.get("INTENS").toString()));
						upar_zkyLight.setMaxSlope(getValueF(jsonObject.get("SLOPE").toString()));
						upar_zkyLight.setError(getValueF(jsonObject.get("ERROR").toString()));
						upar_zkyLight.setLocationType(getValueStr(jsonObject.get("LOCATION").toString()));
						Date date = new Date();
						date = simpleDateFormat.parse((jsonObject.get("DATETIME").toString()));
						if(date != null){
							upar_zkyLight.setObservationTime(date);
							if(!TimeCheckUtil.checkTime(date)){
								ReportError re = new ReportError();
								re.setMessage("DataTime out of range：time:"+date);
								re.setSegment(jsonObject.toString());
								parseResult.put(re);
								continue;
							}
						}
						else{
							ReportError re = new ReportError();
							re.setMessage("Time conversion error");
							re.setSegment(jsonObject.toString());
							parseResult.put(re);
							continue;
						}
						upar_zkyLight.setMillisecond(getValueI(jsonObject.get("MINISECOND").toString()));
						upar_zkyLight.setProv(getValueStr(jsonObject.get("PROVINCE").toString()));
						upar_zkyLight.setDistrict(getValueStr(jsonObject.get("DISTRICT").toString()));
						upar_zkyLight.setCountry(getValueStr(jsonObject.get("COUNTRY").toString()));
						upar_zkyLight.setProcessFlag(getValueI(jsonObject.get("PROCESSFLAG").toString()));
						upar_zkyLight.setUsedIDs(getValueStr(jsonObject.get("USEDIDS").toString()));
						upar_zkyLight.setCG_IC(getValueStr(jsonObject.get("CG_IC").toString()));
						upar_zkyLight.setHeight(getValueF(jsonObject.get("HEIGHT").toString()));
					}catch(Exception e){
						ReportError re = new ReportError();
						re.setMessage("Error in element format error when converted to numeric value");
						re.setSegment(jsonObject.toString());
						parseResult.put(re);
						continue;
					}
					parseResult.put(upar_zkyLight);
					parseResult.setSuccess(true);
				} //end for 
				
			}catch(IOException e){
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			} 
			catch(JSONException e){
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (Exception e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} 
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	/**
	 * 解析浮点型数值   
	 * @param str 待解析字符串
	 * @return double   解码结果   
	 */
	double getValueF(String str){
		if(str == null)
			return defaultValue;
		str = str.trim();
		if(str.isEmpty())
			return defaultValue;
		return Double.parseDouble(str);
	}
	/**
	 * 解析整型数值   
	 * @param str 待解析字符串
	 * @return int   解码结果   
	 */
	int getValueI(String str){
		if(str == null)
			return defaultValue;
		str = str.trim();
		if(str.isEmpty())
			return defaultValue;
		return Integer.parseInt(str);
	}
	/**
	 * 解析字符型数值   
	 * @param str 待解析字符串
	 * @return String   解码结果   
	 */
	String getValueStr(String str){
		if(str == null)
			return "999999";
		str = str.trim();
		if(str.isEmpty())
			return "999999";
		return str;
	}
	
//	public static void main(String[] args) {
//		DecodeZKYLight decodeZKYLight = new DecodeZKYLight();
//		ParseResult<Upar_zkyLight> parseResult = decodeZKYLight.DecodeFile(new File(
//				"D:\\HUAXIN\\DataProcess\\ZKYLight\\B.0030.0001.R001\\201802050521.json"));
//		List<Upar_zkyLight> list = parseResult.getData();
//		System.out.println(list.size());
//		System.out.println(list.get(0).getLatitude());
//	}
}