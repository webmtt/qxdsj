package cma.cimiss2.dpc.decoder.forestfire;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.ReportInfo;
import cma.cimiss2.dpc.decoder.bean.ParseResult.ParseInfo;
import cma.cimiss2.dpc.decoder.bean.cawn.ForestFire;

/**
 * 
 * <br>
 * @Title:  DecodeForestFire.java   
 * @Package cma.cimiss2.dpc.decoder.forestfire   
 * @Description:    TODO(森林火情要素数据解码类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年3月9日 上午10:25:57   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class DecodeForestFire {
	/* 存放数据解析的结果集 */
	private ParseResult<ForestFire> parseResult = new ParseResult<ForestFire>(false);
	double defaultD = 999999;
	/**
	 * 
	 * @Title: DecodeFile   
	 * @Description: TODO(森林火情要素数据解码函数)   
	 * @param file
	 * @return ParseResult<ForestFire>      
	 * @throws：
	 */
	public ParseResult<ForestFire> DecodeFile(File file){
		if(file != null && file.exists() && file.isFile()){
			SAXReader reader = null;
			Element obser;
			Element record;
			String year;
			String month;
			String day;
			String hour;
			String minute;
			String second;
			String prov;
			String city;
			String county;
			String address;
			String details;
			String filler;
			ForestFire forestFire = null;
			try{
				reader = new SAXReader();
				Document doc = reader.read(file);
				Element root = doc.getRootElement();
				if((obser = root).getName().equals("ObservationData")){
					for(@SuppressWarnings("rawtypes") Iterator iterator = obser.elementIterator("Record"); iterator.hasNext();){
						forestFire = new ForestFire();
						record = (Element)iterator.next();
						// 报文各要素：年、月、日、时、分、秒、省、时、区、地址、详情、填报人
						year = record.elementText("V04001");
						month = record.elementText("V04002");
						day = record.elementText("V04003");
						hour = record.elementText("V04004");
						minute = record.elementText("V04005");
						second = record.elementText("V04006");
						prov = record.elementText("V_PPP");
						city = record.elementText("V_CITY");
						county = record.elementText("V_COUNTY");
						address = record.elementText("V_ADDRESS");
						details = record.elementText("V_DES");
						filler = record.elementText("V_WP");
						try{
							forestFire.setYear(Integer.parseInt(year.trim()));
							forestFire.setMonth(Integer.parseInt(month.trim()));
							forestFire.setDay(Integer.parseInt(day.trim()));
							forestFire.setHour(Integer.parseInt(hour.trim()));
							forestFire.setMinute(Integer.parseInt(minute.trim()));
							forestFire.setSecond(Integer.parseInt(second.trim()));
							forestFire.setProv(prov);
							forestFire.setCity(city);
							forestFire.setCounty(county);
							forestFire.setAddress(address);
							forestFire.setFireDetail(details);
							forestFire.setFiller(filler);
							
							parseResult.setSuccess(true);
							parseResult.put(forestFire);
						}catch(Exception e){
							ReportError re = new ReportError();
							re.setMessage("报文记录异常，解码错误");
							re.setSegment(record.toString());
							parseResult.put(re);
						}
					}
				}else{
					// 报文根节点错误，必须有且仅有一个根要素，根要素标签为：<ObservationData>
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
		DecodeForestFire decodeForestFire = new DecodeForestFire();
		ParseResult<ForestFire> parseResult = decodeForestFire.DecodeFile(new File("D:\\华信\\数据加工处理\\接收文档\\fire.xml"));
		@SuppressWarnings("rawtypes")
		List<ReportInfo> list = parseResult.getReports();
		System.out.println(list.size());
	}
}
