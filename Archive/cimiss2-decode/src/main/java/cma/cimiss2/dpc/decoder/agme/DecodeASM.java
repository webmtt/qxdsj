package cma.cimiss2.dpc.decoder.agme;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.agme.AgmeReportHeader;
import cma.cimiss2.dpc.decoder.bean.cawn.ASMLevel;
import cma.cimiss2.dpc.decoder.bean.cawn.ZAgmeASM;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;
import cma.cimiss2.dpc.decoder.tools.ElementValUtil;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.enumeration.ObservationMethod;
// TODO: Auto-generated Javadoc

/**
 * -------------------------------------------------------------------------------
 * <br>.
 *
 * @author wuzuoqiang
 * ---------------------------------------------------------------------------------
 * @Title:  DecodeASM.java
 * @Package org.cimiss2.decode.z_agme_asm
 * @Description:    TODO(自动土壤水分观测站上传数据解析)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年1月18日 上午11:23:20   wuzuoqiang    Initial creation.
 * </pre>
 */
public class DecodeASM {
	
	/** The parse result. */
	/* 存放数据解析的结果集 */
	private ParseResult<ZAgmeASM> parseResult = new ParseResult<ZAgmeASM>(false);
	
	/**
	 * Decode file.
	 *
	 * @param file 文件对象
	 * @return         ParseResult<ZAgmeASM>  解码结果集
	 * @Title: decodeFile 解码函数
	 * @Description: TODO(自动土壤水分观测站上传数据解析函数)
	 */
	public ParseResult<ZAgmeASM> decodeFile(File file){
		if (file != null && file.exists() && file.isFile()) {
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			InputStreamReader read = null;
			BufferedReader bufferedReader = null;
			try {
				// 获取文件的编码
				FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();     
		        @SuppressWarnings("static-access")
				String fileCode = fileEncodeUtil.javaname[fileEncodeUtil.detectEncoding(file)];
				// 获取文件流
				read = new InputStreamReader(new FileInputStream(file), fileCode);
				bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				String lineTxtTemp = null;
				// 逐行读取报文内容
				while( (lineTxt = bufferedReader.readLine()) != null){	
					// 判断报文内容是否为结束行
					if (lineTxt.trim().equalsIgnoreCase("NNNN")) {
						break;
					}
					if(lineTxt.trim().equalsIgnoreCase("")) {
						continue;
					}
					lineTxtTemp = lineTxt;
					if (lineTxt.contains("=")) {
						lineTxtTemp = lineTxt.substring(0, lineTxt.length()-1);
					}
					
					if(lineTxt.contains("NNNN")) {
						lineTxtTemp = lineTxt.replace("NNNN", "");
					}
					lineTxtTemp = lineTxtTemp.replace("/", "9");
					String[] items = lineTxtTemp.trim().split("\\s+");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
					if (items.length >=11) {
						try {
							/*
							 去掉最后一个 = 
							if (items[items.length-1].contains("=")) {
								items[items.length-1] = items[items.length-1].substring(0, items[items.length-1].length()-1);
							}*/
							ZAgmeASM asm =new ZAgmeASM();
							AgmeReportHeader agmeHeader = new AgmeReportHeader();
							asm.setStationNumberChina(items[0].toUpperCase());//区站号
							asm.setLatitude(ElementValUtil.getlatitude(items[1]));//测量地纬度
							asm.setLongitude(ElementValUtil.getLongitude(items[2]));//测量地经度
							asm.setHeightOfSationGroundAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(items[3], 0.1));//测量地拔海高度
							asm.setMeasureLocationIndication(items[4]);//测量地段标示
							asm.setObservationTime(sdf.parse(items[5]));//观测时间
							
							if(!TimeCheckUtil.checkTime(asm.getObservationTime())){
								ReportError re = new ReportError();
								re.setMessage("Time check error!");
								re.setSegment(lineTxt);
								parseResult.put(re);
								continue;
							}
							
							int level = 0;	
							while ((level*5 + 6) <= items.length-1) {
								int index = level*5+5;
								ASMLevel asmLevel = new ASMLevel();
								asmLevel.setSoilLevelLabeling(ElementValUtil.ToBeValidDouble(items[index+1].substring(1, 4)));//土壤深度
								asmLevel.setSoilVolumeMoistureContent(ElementValUtil.ToBeValidDouble(items[index+2], 0.1));//土壤体积含水量
								asmLevel.setSoilRelativeHumidity(ElementValUtil.ToBeValidDouble(items[index+3], 0.1));//土壤相对湿度
								asmLevel.setSoilWeightMoistureContent(ElementValUtil.ToBeValidDouble(items[index+4], 0.1));//土壤重量含水率
								asmLevel.setSoilAavailableWaterContent(ElementValUtil.ToBeValidInt(items[index+5]));//土壤有效水分贮存量	
								asm.put(asmLevel);
								level = level +1;
							}
							agmeHeader.setStationNumberChina(items[0]);//区站号
							agmeHeader.setLatitude(ElementValUtil.getlatitude(items[1]));//纬度
							agmeHeader.setLongitude(ElementValUtil.getLongitude(items[2]));//经度
							agmeHeader.setHeightOfBarometerAboveMeanSeaLevel(ElementValUtil.ToBeValidDouble(items[3], 0.1));//测量海拔高度
							agmeHeader.setHeightOfBarometerAboveMeanSeaLevel(null);//气压传感器拔海高度
							agmeHeader.setObservationMethod(ObservationMethod.MANUAL);//观测方式
							agmeHeader.setReport_time(sdf.parse(items[5]));//时间
							agmeHeader.setCropType("ASM");//类型		
							parseResult.put(new ReportInfo<AgmeReportHeader>(agmeHeader, lineTxt.toString()));
							parseResult.put(asm);
							parseResult.setSuccess(true);
							
						} catch (NumberFormatException e) {
							ReportError re = new ReportError();
							re.setMessage("数字转换异常");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;
						} catch (ParseException e) {
							ReportError re = new ReportError();
							re.setMessage("时间格式转换异常");
							re.setSegment(lineTxt);
							parseResult.put(re);
							continue;
						}					
					} else {
						// 报文中记录没有正常结束
						ReportError re = new ReportError();
						re.setMessage("报文总长度验证错误");
						re.setSegment(lineTxt);
						parseResult.put(re);
						continue;
					}
				}

			} catch (UnsupportedEncodingException e) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch (FileNotFoundException e) {
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			} catch (IOException e) {
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
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		DecodeASM decodeASM = new DecodeASM();
		decodeASM.decodeFile(new File("D:\\cmadass\\testdata\\Z_AGME_I_58027_20190226120000_O_ASM-FTM.txt"));
	}
}



