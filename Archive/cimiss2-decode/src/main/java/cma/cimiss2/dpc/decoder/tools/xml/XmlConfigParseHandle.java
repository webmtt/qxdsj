package cma.cimiss2.dpc.decoder.tools.xml;

import cma.cimiss2.dpc.decoder.bean.xml.XmlIndexObject;
import cma.cimiss2.dpc.decoder.bean.xml.XmlParseResult;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XmlConfigParseHandle {

	private static String forOrignalIndex;
    /**
          * 描述:获取字符串中被两个字符（串）包含的所有数据
          * @param str 处理字符串
          * @param start 起始字符（串）
          * @param end 结束字符（串）
          * @param isSpecial 起始和结束字符是否是特殊字符
          * @return Set<String>
          */
    public static Set<String> getStrContainData(String str, String start, String end, boolean isSpecial){
        Set<String> result = new HashSet<>();
        if(isSpecial){
            start = "\\" + start;
            end = "\\" + end;
        }
        String regex = start + "(.*?)" + end;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            String key = matcher.group(1);
            if(!key.contains(start) && !key.contains(end)){
                result.add(key);
            }
        }
        return result;
    }

    public static boolean parseIndexSeat(XmlIndexObject xmlIndexObject, Logger log, String xmlfile, String xmlIndexes, String xmlIndex){
        boolean parseResult = false;
        xmlIndexObject.setLengthControl(true);
        String lengthControlString = xmlIndex.substring(xmlIndex.indexOf("=") + 1,xmlIndex.length());
        String lengthControlStr = lengthControlString.substring(lengthControlString.indexOf("[") + 1,lengthControlString.indexOf("]"));
        String[] lengthControlArgs = lengthControlStr.split(":");
        if(lengthControlArgs.length != 3){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "confirm seat config format must be [resultLengh:seatChar:seatCharPoisiton]" + "\n");
            return parseResult;
        }
        if(lengthControlArgs[1].trim().length() != 1){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "confirm seat config format[resultLengh:seatChar:seatCharPoisiton]seat char must be one character " + "\n");
            return parseResult;
        }
        try{
            xmlIndexObject.setValueLengh(Integer.parseInt(lengthControlArgs[0].trim()));
        }catch (Exception e){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "confirm seat config format[resultLengh:seatChar:seatCharPoisiton]control result legnth value must be int" + "\n");
            return parseResult;
        }
        xmlIndexObject.setSeatChar(lengthControlArgs[1].trim());
        if(lengthControlArgs[2].trim().equals("R") || lengthControlArgs[2].trim().equals("L")){
            xmlIndexObject.setSeatAt(lengthControlArgs[2]);
        }else{
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "confirm seat config format[resultLengh:seatChar:seatCharPoisiton]seat config must be R for right, or L for left " + "\n");
            return parseResult;
        }
        parseResult = true;
        return parseResult;
    }
    public static boolean parseIndexToNum(XmlIndexObject xmlIndexObject,Logger log,String xmlfile,String xmlIndexes,String xmlIndex){
        boolean parseResult = false;
        xmlIndexObject.setParseToNum(true);
        String parseToNumControlString = xmlIndex.substring(xmlIndex.indexOf(">N") + 2,xmlIndex.length());
        String parseToNumControlStr = parseToNumControlString.substring(parseToNumControlString.indexOf("[") + 1,parseToNumControlString.indexOf("]"));
        String[] parseToNumArgs = parseToNumControlStr.split(":");
        if(parseToNumArgs.length != 2 ){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "assign character transcate to number format[startParseCharAt:endParseCharAt]" + "\n");
            return parseResult;
        }
        try{
            xmlIndexObject.setParseStartAt(Integer.parseInt(parseToNumArgs[0]));
            xmlIndexObject.setParseEndAt(Integer.parseInt(parseToNumArgs[1]));
        }catch (Exception e){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "assign character transcate to number:formnat[startParseCharAt:endParseCharAt] must be number" + "\n");
            return parseResult;
        }
        if(xmlIndexObject.getParseStartAt() >= xmlIndexObject.getParseEndAt() || xmlIndexObject.getParseStartAt() < 0){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "assign character transcate to number:start and end index error" + "\n");
            return parseResult;
        }
        parseResult = true;
        return parseResult;
    }

    public static boolean parseIndexSubString(XmlIndexObject xmlIndexObject,Logger log,String xmlfile,String xmlIndexes,String xmlIndex){
        boolean parseResult = false;
        xmlIndexObject.setSubString(true);
        if(xmlIndex.equals("{72[0:1]}"))
        	System.out.println("hello");
        String parseSubstringControlString = xmlIndex.substring(xmlIndex.indexOf("[") ,xmlIndex.length());
        String parseSubstringControlStr = parseSubstringControlString.substring(parseSubstringControlString.indexOf("[") + 1,parseSubstringControlString.indexOf("]"));
        String[] parseSubstringArgs = parseSubstringControlStr.split(":");
        if(parseSubstringArgs.length != 2){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "confirm result substring format[startCharAt:endCharAt]" + "\n");
            return parseResult;
        }
        try{
            xmlIndexObject.setStartCharAt(Integer.parseInt(parseSubstringArgs[0]));
            xmlIndexObject.setEndCharAt(Integer.parseInt(parseSubstringArgs[1]));
        }catch (Exception e){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "confirm result substring[startCharAt:endCharAt] must be number" + "\n");
            return parseResult;
        }
        if(xmlIndexObject.getStartCharAt() >= xmlIndexObject.getEndCharAt() || xmlIndexObject.getStartCharAt() < 0){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "confirm result substring:start and end index error" + "\n");
            return parseResult;
        }
        parseResult = true;
        return parseResult;
    }

    public static boolean parseIndexToXmlIndexObject(XmlIndexObject xmlIndexObject,Logger log,String xmlfile,int dataLineLength,String xmlIndexes,String xmlIndex){
        boolean parseResult = false;
        String xmlIndexBeforeHandle = xmlIndex;
        try{
            if(xmlIndex.contains("=")){
                if(!parseIndexSeat(xmlIndexObject,log,xmlfile,xmlIndexes,xmlIndex)) return parseResult;
                xmlIndex = xmlIndex.substring(0,xmlIndex.indexOf("="));
            }
            if(xmlIndex.contains(">N")){
                if(!parseIndexToNum(xmlIndexObject,log,xmlfile,xmlIndexes,xmlIndex)) return parseResult;
                xmlIndex = xmlIndex.substring(0,xmlIndex.indexOf(">N"));
            }
            if(xmlIndex.contains("[")){
                if(!parseIndexSubString(xmlIndexObject,log,xmlfile,xmlIndexes,xmlIndex)) return parseResult;
                xmlIndex = xmlIndex.substring(0,xmlIndex.indexOf("["));
            }
            try{
                xmlIndexObject.setIndex(Integer.parseInt(xmlIndex));
                
            }catch (Exception e){
                log.error("\n Parse Error:" + xmlfile + "\n" +
                        " Error Reason:This xml config can't be corrent read" +
                        " Error Config:" + xmlIndexes + "confirm data field must be legal int" + "\n");
                return parseResult;
            }
            if(xmlIndexObject.getIndex() >= dataLineLength && ! (xmlIndexObject.getIndex() == 999999)){
                log.error("\n Parse Error:" + xmlfile + "\n" +
                        " Error Reason:This xml config can't be corrent read" +
                        " Error Config:" + xmlIndexes + "confirm data field must bigger than data line fields count" + "\n");
                return parseResult;
            }

        }catch (Exception e){
            log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "\n");
            return parseResult;
        }
        String finder = "{" + xmlIndexObject.getIndex();
        forOrignalIndex = forOrignalIndex.replace(forOrignalIndex.substring(forOrignalIndex.indexOf(finder),forOrignalIndex.indexOf("}",forOrignalIndex.indexOf(finder)) + 1), "{" + xmlIndexObject.getIndex() + "}");
        //forOrignalIndex += "{" + xmlIndexObject.getIndex() + "}";
        parseResult = true;
        return parseResult;
    }
    public static XmlParseResult parseIndexesToXmlIndexObjectes(List<XmlIndexObject> xmlIndexObjects,Logger log,String xmlfile,int dataLineLength,String xmlIndexes){
    	forOrignalIndex = xmlIndexes;
    	XmlParseResult xmlParseResult = new XmlParseResult();
        try{
            
            xmlIndexObjects = new ArrayList<XmlIndexObject>();
            Set<String> indexes = getStrContainData(xmlIndexes, "{", "}", true);
            for(String index:indexes){
                XmlIndexObject xmlIndexObject = new XmlIndexObject();
                if(parseIndexToXmlIndexObject(xmlIndexObject,log,xmlfile,dataLineLength,xmlIndexes,index)){
                    xmlIndexObjects.add(xmlIndexObject);
                }else return xmlParseResult;
            }

        }catch (Exception e){
            log.error("\n Prase Error:" + xmlfile + "\n" +
                    " Error Reason:This xml config can't be corrent read" +
                    " Error Config:" + xmlIndexes + "\n");
            return xmlParseResult;
        }
        xmlParseResult.setParseResult(true);
        xmlParseResult.setForOrignalIndex(forOrignalIndex);
        xmlParseResult.setXmlIndexObjects(xmlIndexObjects);
        return xmlParseResult;
    }


}
