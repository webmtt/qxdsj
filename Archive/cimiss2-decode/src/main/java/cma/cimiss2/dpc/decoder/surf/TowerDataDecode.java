package cma.cimiss2.dpc.decoder.surf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.surf.Tower;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.FileUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main Class of decode the Iron tower data <br>
 * 北京、香河铁塔解码实体类
 *
 * <p>
 * note:
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.surf.Tower}。
 * </ol>
 * </li>
 * </ul>
 * 
 * *<strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 *18 10 06      14 55 13<br>
   2.9   3.5 101  58  22.90<br>
   4.3   4.2 114  55  23.13<br>
   3.1   3.8 117  52  23.30<br>
   2.9   3.5 146  53  24.45<br>
   4.4   4.1 145  51  23.88<br>
   3.1   3.5 151  50  24.38<br>
   1.2   1.9 219  50  24.50<br>
   2.2   3.6 129  49  24.70<br>
   2.5   3.1 126  48  25.03<br>
   1.7   1.5 116  46  26.00<br>
   1.6   2.7 118  46  25.72<br>
   2.9   3.5 140  47  25.43<br>
   3.9   3.6 148  44  26.50<br>
   2.3   2.3  85  45  27.78<br>
   1.7   0.0  62  44  28.58<br>
<strong>code:</strong><br>
 * File file = new File(new File(String filepath));<br>
 * TowerDataDecode fdd = new TowerDataDecode();<br>
 * List<Tower> towers = fdd.decode(file).getData();<br>
 * System.out.println(towers.get(0).getStationNumberC());<br>
 *
 * <strong>output:</strong><br>
 * 1.7   0.0  62  44  28.58===<br>
2.3   2.3  85  45  27.78===<br>
3.9   3.6 148  44  26.50===<br>
2.9   3.5 140  47  25.43===<br>
1.6   2.7 118  46  25.72===<br>
1.7   1.5 116  46  26.00===<br>
2.5   3.1 126  48  25.03===<br>
2.2   3.6 129  49  24.70===<br>
1.2   1.9 219  50  24.50===<br>
3.1   3.5 151  50  24.38===<br>
4.4   4.1 145  51  23.88===<br>
2.9   3.5 146  53  24.45===<br>
3.1   3.8 117  52  23.30===<br>
4.3   4.2 114  55  23.13===<br>
2.9   3.5 101  58  22.90===<br>
CASBJ<br>

 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月27日 上午11:00:29   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author wangzunpeng
 * @version 0.0.1
 */
public class TowerDataDecode {

//	public static void main(String[] args) {
//		File file = new File("D:\\HUAXIN\\DataProcess\\中科院铁塔\\A.0043.0001.R001\\2018061007\\Z_SURF_I_CASBJ_20180610145500_O_ATW-MM_FTM.txt");
//		TowerDataDecode fdd = new TowerDataDecode();
//		List<Tower> towers = fdd.decode(file).getData();
//		System.out.println(towers.get(0).getStationNumberC());
//	}
	/**
	 * 解码结果集封装
	 */
	private ParseResult<Tower> parseResult = null;

	/**
	 * 北京铁塔观测高度
	 */
	private final double[] hights = {8, 15, 32, 47, 65, 80, 100, 120, 140, 160, 180, 200, 240, 280, 320};
	/**
	 * 香河铁塔观测高度
	 */
	private final double[] hightsXH = {8, 15, 32, 47, 65, 80, 100};
	/**
	 * 气象塔数据文件解码
	 * @param file 待解码报文文件
	 * @return ParseResult<Tower> 解码结果封装
	 */
	public ParseResult<Tower> decode(File file) {
		parseResult = new ParseResult<Tower>(false);
		if (file != null && file.exists() && file.isFile()) {
			//Z_SURF_I_CASBJ_20180116181500_O_ATW-MM_FTM.txt
			String fileName = file.getName();
			String[] fs = fileName.split("_");
			String stationNumberC = fs[3];	//字符站号取文件名的站点简称
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
				//首先判断文件不是空的，然后需要判断最少有两行数据，第一行必须为时间，第二行为数据
				if(txtFileContent != null && txtFileContent.size() > 2) {
					//文件第一行为时间：yy dd MM  HH mm ss
					String timeLine = txtFileContent.get(0);
					SimpleDateFormat sdf = new SimpleDateFormat("yy dd MM HH mm ss");
					Date obsTime = null;
				
					if(stationNumberC.equals("CASBJ")){
						try {
							obsTime = sdf.parse(timeLine);
							if(!TimeCheckUtil.checkTime(obsTime)){
								ReportError re = new ReportError();
								re.setMessage("DataTime out of range：time:"+obsTime);
								re.setSegment(txtFileContent.toString());
								parseResult.put(re);
								return parseResult;
							}
						} catch (ParseException e) {
							ReportError reportError = new ReportError();
							reportError.setMessage("DateTime format error!");
							reportError.setSegment(txtFileContent.toString());
							parseResult.put(reportError);
							return parseResult;
						}
						txtFileContent.remove(0);	//去掉第一行数据
						Collections.reverse(txtFileContent);	//将list反转，观测高度转换为从低到高
						for(int i = 0; i < txtFileContent.size(); i ++) {
							String dataLine = txtFileContent.get(i).trim();	//去掉前后空格，否则如果前边有空格分隔的数据不正确
							System.out.println(dataLine + "===");
							double hight = hights[i];	//数据对应的高度层
							
							String[] datas = dataLine.split("\\s+");
							try{
								Tower tower = new Tower();
								tower.setHight(hight);//观测平台距地面的高度。单位-米
								tower.setObsTime(obsTime);//
								tower.setStationNumberC(stationNumberC); //字符站号
								tower.setWindSpeedNW(ToBeValidDouble(datas[0]));	//西北方向伸臂上风速传感器观测值。单位-米/秒
								tower.setWindSpeedSE(ToBeValidDouble(datas[1]));	// 东南方向伸臂上风速传感器观测值。单位-米/秒
								tower.setWindDirection(ToBeValidDouble(datas[2]));	//风向传感器观测值。单位－度
								tower.setRelativeHumidity(ToBeValidDouble(datas[3]));// 相对湿度传感器观测值。单位-百分比
								tower.setTemperature(ToBeValidDouble(datas[4]));	// 大气温度传感器观测值。单位-摄氏度
								
								parseResult.put(tower);
								parseResult.setSuccess(true);
							}catch (Exception e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("Digit error!");
								reportError.setSegment(dataLine.toString());
								parseResult.put(reportError);
								return parseResult;
							}
						}
					}
					else if(stationNumberC.equals("CASXH")){
						while(txtFileContent != null && txtFileContent.size() > 0){
							timeLine = txtFileContent.get(0);
							try {
								obsTime = sdf.parse(timeLine);
								if(!TimeCheckUtil.checkTime(obsTime)){
									ReportError re = new ReportError();
									re.setMessage("DataTime out of range：time:"+obsTime);
									re.setSegment(txtFileContent.toString());
									parseResult.put(re);
									return parseResult;
								}
							} catch (ParseException e) {
								ReportError reportError = new ReportError();
								reportError.setMessage("DateTime format error!");
								reportError.setSegment(txtFileContent.toString());
								parseResult.put(reportError);
								return parseResult;
							}
							txtFileContent.remove(0);	//去掉第一行数据
							if(txtFileContent.size() >= 7){
								List<String> lines = txtFileContent.subList(0, 7);
								Collections.reverse(lines);	//将list反转，观测高度转换为从低到高
								for(int i = 0; i < lines.size(); i ++) {
									String dataLine = lines.get(i).trim();	//去掉前后空格，否则如果前边有空格分隔的数据不正确
									System.out.println(dataLine + "===");
									double hight = hightsXH[i];	//数据对应的高度层
									String[] datas = dataLine.split("\\s+");
									try{
										Tower tower = new Tower();
										tower.setHight(hight);//观测平台距地面的高度。单位-米
										tower.setObsTime(obsTime);//
										tower.setStationNumberC(stationNumberC); //字符站号
										tower.setWindSpeedNW(ToBeValidDouble(datas[0]));	//西北方向伸臂上风速传感器观测值。单位-米/秒
										tower.setWindSpeedSE(ToBeValidDouble(datas[1]));	// 东南方向伸臂上风速传感器观测值。单位-米/秒
										tower.setWindDirection(ToBeValidDouble(datas[2]));	//风向传感器观测值。单位－度
										tower.setRelativeHumidity(ToBeValidDouble(datas[3]));// 相对湿度传感器观测值。单位-百分比
										tower.setTemperature(ToBeValidDouble(datas[4]));	// 大气温度传感器观测值。单位-摄氏度
										
										parseResult.put(tower);
										parseResult.setSuccess(true);
									}catch (Exception e) {
										ReportError reportError = new ReportError();
										reportError.setMessage("Digit error!");
										reportError.setSegment(dataLine.toString());
										parseResult.put(reportError);
										return parseResult;
									}
								}
								for(int i = 0; i < 7; i ++)
									txtFileContent.remove(0);
							}
						}
					}
				}else {
					if(txtFileContent == null || txtFileContent.size() == 0) {
						//空文件
						parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
					}else {
						//数据只有一行，格式不正确
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
	 * 浮点值解析
	 * @param str 待解析字符串
	 * @return double    解析结果  
	 */
	public static double ToBeValidDouble(String str) {
		String field =str.trim();
		if (field.equals("999")) {
			return 999999;		
		}
		else
			return Double.parseDouble(field);
	}

}
