package cma.cimiss2.dpc.decoder.sevp;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.sevp.Travel;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;

/**
 * 
 * *******************************************************************************************<br>
 * <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
 * *******************************************************************************************<br>
 * Main class of decode the Meteorological service products in tourist attractions <br>
 * 旅游景区气象服务产品解码类
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
 * <li>对应实体类及解码字段的详细说明参考{@link cma.cimiss2.dpc.decoder.bean.sevp.Travel}。
 * </ol>
 * </li>
 * </ul>
 * 
 * <strong> sample:</strong><br>
 * <strong>input:</strong> the file content is as follows<br>
 * <?xml version="1.0" encoding="utf-8"?>
<MEFTRV> <p>
  <SVCO>201505190734003421431055958370002</SVCO>
  <PT>20150519073400</PT>
  <PU>
    <PUNA>气象服务中心</PUNA>
    <PUPROCO>GS</PUPROCO>
    <PUCO>MSC</PUCO>
  </PU>
  <SP>
    <SPNA>花期</SPNA>
    <SPCO>BLMVW</SPCO>
  </SP>
  <VA>3700</VA>
  <IT>
    <ITST>20150519120000</ITST>
    <ITEN>20150520120000</ITEN>
  </IT>
  <IV>
    <IVNA>麦积山景区</IVNA>
    <IVLAT>342143</IVLAT>
    <IVLON>1055958</IVLON>
    <IVSN>麦积山石窟,仙人崖,石门</IVSN>
    <IVSLAT>342143,342409,342735</IVSLAT>
    <IVSLON>1055958,1060221,1060548</IVSLON>
  </IV>
  <PD>三级盛开，开花率在65%以上，花开繁盛</PD>
  <ET>01</ET>
  <PS>适合观赏</PS>
  <WA>02</WA>
  <PUER>王小龙</PUER>
</MEFTRV>
 * <p>
 * <strong>code:</strong><br>
 * DecodeTravel decodeTravel = new DecodeTravel();<br>
 * ParseResult<Travel> parseResult = decodeTravel.DecodeFile(new File(String filepath));<br>
 * List<Travel> list = parseResult.getData();<br>
 * System.out.println("ProductID: " + list.get(0).getProductID());<br>
 * System.out.println("ScenicSpotName: " + list.get(0).getScenicSpotName());<br>
 * <strong>output:</strong><br>
 * ProductID: 201505190734003421431055958370002<br>
 * ScenicSpotName: 麦积山景区<br>
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年8月28日 下午5:11:40   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 * @version 0.0.1
 */
public class DecodeTravel{
	/**
	 * 存储解码结果集
	 */
	private ParseResult<Travel> parseResult = new ParseResult<Travel>(false);
	/**
	 * 旅游景区气象服务产品解码函数  
	 * @param file  待解码文件
	 * @return ParseResult<Travel>  解码结果封装      
	 */
	public ParseResult<Travel> DecodeFile(File file){
		if(file != null && file.exists() && file.isFile()){
			if(file.length() <= 0){
				parseResult.setParseInfo(ParseInfo.EMPTY_FILE);
				return parseResult;
			}
			SAXReader reader = null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			Element MEFTRV;
			String SVCO = "";
			String PT = "";
		
			Element PU;
			String PUNA = "";
			String PUPROCO = "";
			String PUCO = "";
		
			Element SP;
			String SPNA = "";
			String SPCO = "";
			String VA = "";
			Element IT;
			String ITST = "";
			String ITEN = "";
			
			Element IV;
			String IVNA = "";
			String IVLAT = "";
			String IVLON = "";
			String IVSN = "";
			String IVSLAT = "";
			String IVSLON = "";
			String PD = "";
			String ET = "";
			String PS = "";
			String WA = "";
			String PUER = "";
			try{
				reader = new SAXReader();
				Document doc = reader.read(file);
				Element root = doc.getRootElement();
				if((MEFTRV = root).getName().equals("MEFTRV")){
					Travel travel = new Travel();
					try{
						SVCO = MEFTRV.elementText("SVCO");
						travel.setProductID(SVCO);
					}catch(Exception e){
						ReportError re = new ReportError();
						re.setMessage("Sevp ID error!");
						re.setSegment(SVCO);
						parseResult.put(re);
						return parseResult;
					}
					try{
						PT = MEFTRV.elementText("PT");
						Date dt = new Date();
						dt = simpleDateFormat.parse(PT);
						travel.setReleaseTime(dt);
						if(!TimeCheckUtil.checkTime(dt)){
							ReportError re = new ReportError();
							re.setMessage("DataTime out of range：time:"+dt);
							re.setSegment(PT);
							parseResult.put(re);
							return parseResult;
						}
					}catch (ParseException e) {
						ReportError re = new ReportError();
						re.setMessage("Publish time error!");
						re.setSegment(PT);
						parseResult.put(re);
						return parseResult;
					}
					PU = MEFTRV.element("PU");
					if(PU != null){
						PUNA = PU.elementText("PUNA");
						PUPROCO = PU.elementText("PUPROCO");
						PUCO = PU.elementText("PUCO");
						if(PUNA != null)
							travel.setReleaseOrgName(PUNA);
						if(PUPROCO != null)
							travel.setProvCode(PUPROCO);
						if(PUCO != null)
							travel.setReleaseOrgCode(PUCO);
					}
					SP = MEFTRV.element("SP");
					if(SP != null){
						SPNA = SP.elementText("SPNA");
						SPCO = SP.elementText("SPCO");
						if(SPNA != null)
							travel.setSceneryName(SPNA);
						if(SPCO != null)
							travel.setSceneryCode(SPCO);
					}
					VA = MEFTRV.elementText("VA");
					try{
						int tmp = Integer.parseInt(VA);
						travel.setSceneryProductInfo(tmp);
					}catch (Exception e) {
						ReportError re = new ReportError();
						re.setMessage("Scenic Warning Level/Landscape product Info Format error!");
						re.setSegment(VA);
						parseResult.put(re);
					}
					IT = MEFTRV.element("IT");
					if(IT != null){
						ITST = IT.elementText("ITST");
						ITEN = IT.elementText("ITEN");
						try{
							simpleDateFormat.parse(ITST);
							simpleDateFormat.parse(ITEN);
							travel.setAffectedStartTime(ITST);
							travel.setAffectedEndTime(ITEN);
						}catch (Exception e) {
							ReportError re = new ReportError();
							re.setMessage("Affect start/end time format error!");
							re.setSegment(ITST + " " + ITEN);
							parseResult.put(re);
						}
					}
					IV = MEFTRV.element("IV");
					if(IV != null){
						IVNA = IV.elementText("IVNA");
						IVLAT = IV.elementText("IVLAT");
						IVLON = IV.elementText("IVLON");
						IVSN = IV.elementText("IVSN");
						IVSLAT = IV.elementText("IVSLAT");
						IVSLON = IV.elementText("IVSLON");
						if(IVNA != null)
							travel.setScenicSpotName(IVNA);
						if(IVLAT != null)
							try{
								if(Double.valueOf(IVLAT)/10000>=-90&&Double.valueOf(IVLAT)/10000<=90){
								travel.setScenicSpotLat(Double.valueOf(IVLAT)/10000);
//								travel.setScenicSpotLat(ElementValUtil.getlatitude(IVLAT));
								}
							}catch (Exception e) {
								ReportError re = new ReportError();
								re.setMessage("Scenic Latitude Format error!");
								re.setSegment(IVLAT);
								parseResult.put(re);
							}
						if(IVLON != null)
							try{
								if(Double.valueOf(IVLON)/10000>=-180&&Double.valueOf(IVLON)/10000<=180){
								travel.setScenicSpotLon(Double.valueOf(IVLON)/10000);
//								travel.setScenicSpotLon(ElementValUtil.getLongitude(IVLON));
								}
							}catch (Exception e) {
								ReportError re = new ReportError();
								re.setMessage("Scenic Longtitude Format error!");
								re.setSegment(IVLON);
								parseResult.put(re);
							}
						if(IVSN != null)
							travel.setAffectedScenicSpotName(IVSN);
						if(IVSLAT != null)
							travel.setAffectedScenicSportLat(IVSLAT);
						if(IVSLON != null)
							travel.setAffectedScenicSportLon(IVSLON);
					}
					PD = MEFTRV.elementText("PD");
					if(PD != null)
						travel.setDisaOrProdContent(PD);
					ET = MEFTRV.elementText("ET");
					if(ET != null)
						try{
							travel.setEvolutionTrend(Integer.parseInt(ET));
						}catch (Exception e) {
							ReportError re = new ReportError();
							re.setMessage("Evolution Trend Field Format error!");
							re.setSegment(ET);
							parseResult.put(re);
						}
					PS = MEFTRV.elementText("PS");
					if(PS != null)
						travel.setSuggestions(PS);
					WA = MEFTRV.elementText("WA");
					if(WA != null)
						try{
							travel.setSignalReleaseAndCancelCode(Integer.parseInt(WA));
						}catch (Exception e) {
							ReportError re = new ReportError();
							re.setMessage("");
							re.setSegment(WA);
							parseResult.put(re);
						}
					PUER = MEFTRV.elementText("PUER");
					if(PUER != null)
						travel.setPublisherName(PUER);
					parseResult.put(travel);
					parseResult.setSuccess(true);
				}
				else{
					parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
				}
			}catch (DocumentException e1) {
				parseResult.setParseInfo(ParseInfo.ILLEGAL_FORM);
			} catch(Exception e2){
				parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
			} 
		}else {
			parseResult.setParseInfo(ParseInfo.FILE_NOT_EXSIT);
		}
		return parseResult;
	}
	
	
	public static void main(String[] args) {
		DecodeTravel decodeTravel = new DecodeTravel();
		ParseResult<Travel> parseResult = decodeTravel.DecodeFile(new File
				("D:\\nega\\Z_SEVP_C_BCLZ_20190625035500_P_MSP_TRAVEL_BLMVW.xml"));
		List<Travel> list = parseResult.getData();
		System.out.println("ProductID: " + list.get(0).getProductID());
		System.out.println("ScenicSpotName: " + list.get(0).getScenicSpotName());
	}
	
}