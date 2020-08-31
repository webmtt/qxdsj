package cma.cimiss2.dpc.decoder.surf;

import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.bean.surf.TwSiteData;
import cma.cimiss2.dpc.decoder.check.util.TimeCheckUtil;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * ************************************
 * @ClassName: DecodeTwSiteDataXml
 * @Auther: dangjinhu
 * @Date: 2019/8/7 12:01
 * @Description: 台湾站点资料
 * @Copyright: All rights reserver.
 * ************************************
 */

public class DecodeTwSiteDataXml {

    /**
     * 对外接口
     * @param fileName 文件路径
     * @return ParseResult, 文件解析结果集
     */
    public ParseResult<TwSiteData> decodeFile(String fileName) {
        ParseResult<TwSiteData> parseResult = new ParseResult<>(false);
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.length() <= 0) {
                parseResult.setParseInfo(ParseResult.ParseInfo.EMPTY_FILE);
            } else {
                readFileInfo(file, parseResult);
            }
        } else {
            parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
        }
        return parseResult;
    }

    /**
     * 读配置文件数据放入结果集
     * @param file        文件
     * @param parseResult 结果集对象
     */
    private void readFileInfo(File file, ParseResult<TwSiteData> parseResult) {
        SAXReader reader;
        boolean flag = false;
        try {
            reader = new SAXReader();
            Document document = reader.read(file);
            Element rootElement = document.getRootElement();
            // success标签
            Element success = rootElement.element("success");
            if (success.getText().equals("true")) {
                flag = true;
            }
            // result标签
            Element result = rootElement.element("result");
            List<Element> resultElements = result.elements();
            // records标签
            Element record = rootElement.element("records");
            List<Element> recordElements = record.elements();
            List<TwSiteData> twSiteDatas = new ArrayList<>();
            for (Element element : recordElements) {
                // location标签
                List<Element> locationElements = element.elements();
                TwSiteData twSiteData = new TwSiteData(flag, resultElements.get(0).getText());
                // 遍历records标签下观测值赋值给对象
                for (Element locationElement : locationElements) {
                    String elementName = locationElement.getName();
                    try {
                        setValue(parseResult, twSiteData, locationElement, elementName);
                    } catch (NumberFormatException e) {
                        ReportError error = new ReportError();
                        error.setMessage(elementName + "Conversion exception!");
                        error.setSegment(locationElement.getPath());
                        parseResult.put(error);
                    }
                }
                twSiteDatas.add(twSiteData);
                // 有一条记录成功
                parseResult.setSuccess(true);
            }
            parseResult.setData(twSiteDatas);
        } catch (DocumentException e) {
            parseResult.setParseInfo(ParseResult.ParseInfo.ILLEGAL_FORM);
        }
    }

    /**
     * 赋值
     * @param parseResult     结果集
     * @param twSiteData      观测值对象
     * @param locationElement location标签名称
     * @param elementName     location子标签名称
     */
    private void setValue(ParseResult<TwSiteData> parseResult, TwSiteData twSiteData, Element locationElement, String elementName) {
        String elementValue = locationElement.getText();
        if (StringUtils.isBlank(elementName)) {
            elementName = "999999";
        }
        switch (elementName) {
            case "lat":
                // 纬度
                twSiteData.setLatitude(Double.parseDouble(elementValue));
                break;
            case "lon":
                // 经度
                twSiteData.setLongitude(Double.parseDouble(elementValue));
                break;
            case "locationName":
                // 测站名称
                twSiteData.setLocationName(elementValue);
                break;
            case "stationId":
                // 测站ID
                if (elementValue.length() == 6) {
                    twSiteData.setStationId(elementValue);
                } else {
                    ReportError error = new ReportError();
                    error.setMessage(elementName + "stationId length not equals 6 !");
                    error.setSegment(locationElement.getPath());
                    parseResult.put(error);
                }
                break;
            case "time":
                // 资料观测时间
                try{
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				    Date dataTime=simpleDateFormat.parse(locationElement.element("obsTime").getText().toString());
					if(!TimeCheckUtil.checkTime(dataTime)){
						ReportError error = new ReportError();
	                    error.setMessage(elementName + "time Check Error !"+dataTime);
	                    error.setSegment(locationElement.getPath());
	                    parseResult.put(error);
					}else{
						twSiteData.setObsTime(locationElement.element("obsTime").getText());
					}
				}catch (Exception e) {
					ReportError error = new ReportError();
                    error.setMessage(elementName + "time format error!");
                    error.setSegment(locationElement.getPath());
                    parseResult.put(error);
				}
                break;
            case "weatherElement":
                // 天气现象要素及观测值
                twSiteData.setWeatherElement(locationElement.element("elementName").getText(), isLacking(locationElement.element("elementValue").getText()));
                break;
            case "parameter":
                // 市乡镇名称及编号
                twSiteData.setParameter(locationElement.element("parameterName").getText(), locationElement.element("parameterValue").getText());
                break;
            default:
                ReportError error = new ReportError();
                error.setMessage(elementName + "No corresponding tag found");
                error.setSegment(locationElement.getPath());
                parseResult.put(error);
        }
    }

    /**
     * 对缺测-99、null、空观测值重新赋值
     * @param value 观测值
     * @return result结果
     */
    private static double isLacking(String value) {
        double result;
        if (value.equals("null") || value.equals("-99") || StringUtils.isBlank(value) || value.equals("-999.00")) {
            result = 999999.0D;
        } else if (value.equals("-998.00") ) {
            result = 0.0D;
        } else {
            result = Double.parseDouble(value);
        }
        return result;
    }

}
