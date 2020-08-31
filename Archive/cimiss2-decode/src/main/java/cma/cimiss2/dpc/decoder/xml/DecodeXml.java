package cma.cimiss2.dpc.decoder.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import cma.cimiss2.dpc.decoder.bean.xml.XmlIndexObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cma.cimiss2.dpc.decoder.bean.ParseResult;
import cma.cimiss2.dpc.decoder.bean.ReportError;
import cma.cimiss2.dpc.decoder.tools.FileEncodeUtil;
import cma.cimiss2.dpc.decoder.tools.common.Encoding;
import cma.cimiss2.dpc.decoder.tools.utils.TimeUtil;
import cma.cimiss2.dpc.decoder.tools.xml.XmlDecodeFile;

public class DecodeXml {
	
	public static final Logger infoLogger = LoggerFactory.getLogger("loggerInfo");
	
    /**
               *     读取文件信息
     * @param file
     * @return
     */
    private String readFileInfo(File file) {
        // 获取文件编码
        FileEncodeUtil fileEncodeUtil = new FileEncodeUtil();
        String fileCode = Encoding.javaname[fileEncodeUtil.detectEncoding(file)];
        return fileCode;
    }
    
    /**
               *    缺测值处理
     * @param items
     */
    private void cleanNullValue(String[] items,String defaultField) {
        for (int i = 0; i < items.length; i++) {
            if (items[i].contains(defaultField)) {
                items[i] = "999999";
            }
        }
    }


	@SuppressWarnings("unused")
    public boolean decode(String datafile,Date rece_time,ParseResult<HashMap<String,List<Map<String,Object>>>> parseResult,int startDataLine,int dataLinesCount) {
		File dataFile = new File(datafile);
		//ParseResult<HashMap<String,List<Map<String,Object>>>> parseResult = new ParseResult<HashMap<String,List<Map<String,Object>>>>(false);
		if(!(dataFile.exists() && dataFile.isFile())) {
			parseResult.setParseInfo(ParseResult.ParseInfo.FILE_NOT_EXSIT);
			return true;
		}	
		//Data line读取
		String fileCode = readFileInfo(dataFile);		
		InputStreamReader is = null;
		BufferedReader  reader = null;
		String line = null;
		int lineCount = 0;
		ArrayList<String[]> dataFields = new ArrayList<String[]>();
		ArrayList<String> lines = new ArrayList<String>();
		boolean readOver = true;
		try {
			is = new InputStreamReader(new FileInputStream(dataFile), fileCode);
			reader = new BufferedReader(is);
			while((line = reader.readLine()) != null) {
				if (StringUtils.isBlank(line)) {
					continue;
				}
				
				if(lineCount < XmlDecodeFile.headLinesCount || lineCount < startDataLine) { 
					lineCount++;
					continue;
				}
				
				if(lineCount >= startDataLine + dataLinesCount) {
					readOver = false;
					break;
				}
				lines.add(line);
				String[] values = line.trim().split(XmlDecodeFile.dataSpliter);
				// blank line deal
				if(XmlDecodeFile.defaultFiled != "") cleanNullValue(values, XmlDecodeFile.defaultFiled);
				//line data fields count is not equals to xml define
				if(values.length != XmlDecodeFile.lineFieldLength) {
					ReportError error = new ReportError();
                    error.setMessage("Parse Error:" + datafile + "\n" +
                            "Error reason:" + "Data field lack" + "\n" +
							"Split character:" + XmlDecodeFile.dataSpliter + ".\n" +
                            "Error Data line:" + line + "\n");
                    error.setSegment(dataFile.getPath());
                    parseResult.put(error);
					continue;
				}
				dataFields.add(values);
				lineCount++;
			}
		} catch (Exception e) {
			ReportError error = new ReportError();
			error.setMessage("File read unusual");
			error.setSegment(datafile);
			parseResult.put(error);
			parseResult.setSuccess(false);
			return true;
		}finally {
			if(reader != null) {
				try {
					 reader.close();
				}catch (IOException e) {
                    ReportError error = new ReportError(); 
                    error.setMessage("Stream close unusual" + e.getMessage());
                    parseResult.put(error);
                }
			}
			if(is != null) {
				try {
					is.close();
				}catch (IOException e) {
                    ReportError error = new ReportError(); 
                    error.setMessage("Stream close unusual" + e.getMessage());
                    parseResult.put(error);
                }
			}
		}	
		//记录Error 数据的行数，用于后面移除，防止入库
		Set<Integer> errorLines = new HashSet<>();
		ArrayList<Map<String,Object>> keyValuePairs = new ArrayList<Map<String,Object>>();
		for(String[] lineDataFields:dataFields) {
			HashMap<String,Object> keyValue = new HashMap<String,Object>();			
			keyValuePairs.add(keyValue);
		}
		for(XmlDecodeFile fieldHandle:XmlDecodeFile.handleFieldDecoder) {
			try {
				String content = fieldHandle.getContent(); 
				String dataType = fieldHandle.getDataType(); 
				String value = fieldHandle.getValue(); 
				String fieldDataLength = fieldHandle.getDataLength(); 
				List<XmlIndexObject> xmlIndexObjectList = fieldHandle.getIndex();
				String orignalIndex = fieldHandle.getOrignalIndex();
				String format = fieldHandle.getFormat(); 
				String expressionKey = fieldHandle.getExpressKey();
				Double expressionValue = fieldHandle.getExpressValue();
				boolean isExpressHandle = fieldHandle.isExpressHandle();
				int fieldHandleIndex = fieldHandle.getFieldHandleIndex();
				String elementValue = fieldHandle.getElmentValue();
				boolean errorHandle = fieldHandle.isParseErrorHandle();
				String errorDefaultValue = fieldHandle.getParseErrorDefaultValue();
				boolean defaultControl = fieldHandle.isDefaultControl();
				String defaultVal = fieldHandle.getDefaultVal();
				//<element content="default" dataType="string" index="D_RECORD_ID"> , use uuid
				if(value != null && value.contentEquals("D_RECORD_ID") && content != null && content.toLowerCase().contentEquals("default") && dataType != null && dataType.toLowerCase().contentEquals("string")) {
					int count = 0;
					for(String[] lineDataFields:dataFields) {					
						keyValuePairs.get(count).put(fieldHandleIndex + "",UUID.randomUUID().toString().replace("-", "").toLowerCase());
						count++;
					}
				//<element dataType="datetime" content="default" value="SYSDATE"> , use current system datetime
				}else if (content != null && content.toLowerCase().contentEquals("default") && dataType != null && dataType.toLowerCase().contentEquals("datetime") && value != null && value.contentEquals("SYSDATE")) {
					Date time = new Date(); 
					int count = 0;
					for(String[] lineDataFields:dataFields) {					
						keyValuePairs.get(count).put(fieldHandleIndex + "",TimeUtil.date2String(time,"yyyy-MM-dd HH:mm:ss"));
						count++;
					}	
				//<element dataType="datetime" content="default" value="SYSDATE"> , use data file last modified datetime
				}else if (content != null && content.toLowerCase().contentEquals("default") && dataType != null && dataType.toLowerCase().contentEquals("datetime") && value != null && value.contentEquals("D_RYMDHM")) {
					int count = 0;
					for(String[] lineDataFields:dataFields) {					
						keyValuePairs.get(count).put(fieldHandleIndex + "",TimeUtil.date2String(rece_time,"yyyy-MM-dd HH:mm:ss"));
						count++;
					}


				}else if (content != null && dataType != null && xmlIndexObjectList != null && xmlIndexObjectList.size() != 0) {
					int count = 0;
					boolean fieldsHandleSuccess = false;
					for(String[] lineDataFields:dataFields) {
						try {
							boolean isDefaultVal = false;
							String values = orignalIndex;
							for(XmlIndexObject xmlIndexObject:xmlIndexObjectList) {
								String valueOfOneIndex = "";
								if(content.toLowerCase().contentEquals("report"))
									valueOfOneIndex = lineDataFields[xmlIndexObject.getIndex()];
								else if (content.toLowerCase().contentEquals("filename"))
									valueOfOneIndex = dataFile.getName();
								if(defaultControl && valueOfOneIndex.equals(defaultVal)){
									values = values.replace("{" + xmlIndexObject.getIndex() + "}",valueOfOneIndex);
									fieldsHandleSuccess = true;
									isDefaultVal = true;
									break;
								}
								if(xmlIndexObject.isLengthControl()){
									if(xmlIndexObject.getValueLengh() > valueOfOneIndex.length()){
										for(int m = xmlIndexObject.getValueLengh() - valueOfOneIndex.length();m > 0 ;m--){
											if(xmlIndexObject.getSeatAt().equals("R")) valueOfOneIndex = valueOfOneIndex + xmlIndexObject.getSeatChar();
											else if(xmlIndexObject.getSeatAt().equals("L")) valueOfOneIndex = xmlIndexObject.getSeatChar() + valueOfOneIndex;
										}
									}
								}
								if(xmlIndexObject.isSubString()){
									if(xmlIndexObject.getEndCharAt() > valueOfOneIndex.length()){
										ReportError error = new ReportError();
										error.setMessage("ParseError:" + XmlDecodeFile.xmlfile + "\n" +
												"DataFile：" + datafile + "\n" +
												"DataLine：" + lines.get(count) + "\n" +
												"ErrorReason:SubString end position bigger than data line field value length,data line field value:" + lineDataFields[xmlIndexObject.getIndex()] + ",substring end position:" + xmlIndexObject.getEndCharAt() + "\n");
										error.setSegment(dataFile.getPath());
										parseResult.put(error);
										errorLines.add(count);
										break;
									}
									valueOfOneIndex = valueOfOneIndex.substring(xmlIndexObject.getStartCharAt(),xmlIndexObject.getEndCharAt());

								}
								if(xmlIndexObject.isParseToNum()){
									if(xmlIndexObject.getParseEndAt() > valueOfOneIndex.length()){
										ReportError error = new ReportError();
										error.setMessage("ParseError:" + XmlDecodeFile.xmlfile + "\n" +
												"DataFile：" + datafile + "\n" +
												"DataLine：" + lines.get(count) + "\n" +
												"ErrorReason:ParseToNum end position bigger than data line field value length,data line field value:" + lineDataFields[xmlIndexObject.getIndex()] + ",substring :" + valueOfOneIndex + ",parseToNum end position:" + xmlIndexObject.getEndCharAt() + "\n");
										error.setSegment(dataFile.getPath());
										parseResult.put(error);
										errorLines.add(count);
										break;
									}
									char[] dataValueCharArray = valueOfOneIndex.toCharArray();
									valueOfOneIndex = "";
									try {
										for (int j = 0; j < dataValueCharArray.length; j++) {
											if (j >= xmlIndexObject.getParseStartAt() && j < xmlIndexObject.getParseEndAt() && ((dataValueCharArray[j] >= 65 && dataValueCharArray[j] <= 90) || (dataValueCharArray[j] >= 97 && dataValueCharArray[j] <= 122))) {
												valueOfOneIndex += Integer.valueOf(dataValueCharArray[j]);
											} else valueOfOneIndex += dataValueCharArray[j];
										}
									}catch(Exception e){
										ReportError error = new ReportError();
										error.setMessage("ParseError:" + XmlDecodeFile.xmlfile + "\n" +
												"DataFile：" + datafile + "\n" +
												"DataLine：" + lines.get(count) + "\n" +
												"ParseToNum: line data field index:" + xmlIndexObject.getIndex() +  "[" + xmlIndexObject.getParseStartAt() + ":" + xmlIndexObject.getParseEndAt() + "]" + "\n" +
												"ErrorReason: " + e + "\n");
										error.setSegment(dataFile.getPath());
										parseResult.put(error);
										errorLines.add(count);
										break;
									}
								}
								
								values = values.replace("{" + xmlIndexObject.getIndex() + "}",valueOfOneIndex);
								fieldsHandleSuccess = true;
							}
							if(!fieldsHandleSuccess){
								//count++;
								continue;
							}
							values =  values.replace("{","").replace("}","");
							if(dataType.toLowerCase().equals("datetime")){
								try {
									Date datetime = TimeUtil.String2Date(values, "yyyyMMddHHmmss");//try to comparse datetime string
									values = TimeUtil.date2String(datetime,"yyyy-MM-dd HH:mm:ss");
								}catch(Exception e) {
									ReportError error = new ReportError();
									error.setMessage("Parse Error:" + XmlDecodeFile.xmlfile + "\n" +
											"Data file：" + datafile + "\n" +
											"Data line：" + lines.get(count) + "\n" +
											"Error reason:D_DATETIME field combine value is " + values + ",value do not standard date format：yyyyMMddHHmmss" + "\n");
									error.setSegment(dataFile.getPath());
									parseResult.put(error);
									errorLines.add(count);
									//count++;
									continue;
								}
								keyValuePairs.get(count).put(fieldHandleIndex + "",values);
							}else if(dataType.toLowerCase().equals("double")){
								double valueD = 999999f;
								try {
									valueD = Double.parseDouble(values);
								}catch(Exception e) {
									if(!errorHandle) {
										ReportError error = new ReportError();
										error.setMessage("Parse Error:" + XmlDecodeFile.xmlfile + "\n" +
												"Data file：" + datafile + "\n" +
												"Data line：" + lines.get(count) + "\n" +
												"Error reason:can't handle field result" + values + "to transform to double datatype" + "\n");
										error.setSegment(dataFile.getPath());
										parseResult.put(error);
										errorLines.add(count);
										//count++;
										continue;
									}else {
										values = errorDefaultValue;
										valueD = Double.parseDouble(values);
									}

								}
								if(isExpressHandle && !isDefaultVal){
									switch(expressionKey){
									case "/":valueD = valueD / expressionValue;break;
									case "*":valueD = valueD * expressionValue;break;
									case "+":valueD = valueD + expressionValue;break;
									case "-":valueD = valueD - expressionValue;break;
										
									}
								}
								keyValuePairs.get(count).put(fieldHandleIndex + "",valueD);
							}else if(dataType.toLowerCase().equals("int")){
								int valueI = 999999;
								try {
									valueI = Integer.parseInt(values);
								}catch(Exception e) {
									if(!errorHandle) {
										ReportError error = new ReportError();
										error.setMessage("Parse Error:" + XmlDecodeFile.xmlfile + "\n" +
												"Data file：" + datafile + "\n" +
												"Data line：" + lines.get(count) + "\n" +
												"Error reason:can't handle field result" + values + "to transform to int datatype" + "\n");
										error.setSegment(dataFile.getPath());
										parseResult.put(error);
										errorLines.add(count);
										//count++;
										continue;
									}else {
										values = errorDefaultValue;
										valueI = Integer.parseInt(values);
									}

								}
								keyValuePairs.get(count).put(fieldHandleIndex + "",valueI);
							}else if (dataType.toLowerCase().equals("string")){
								keyValuePairs.get(count).put(fieldHandleIndex + "",values);
							}

						}catch(Exception e){
							ReportError error = new ReportError();
		                    error.setMessage("Parse Error:" + XmlDecodeFile.xmlfile + "\n" +
									"Data file：" + datafile + "\n" +
			                        "Data line：" + lines.get(count) + "\n" +
			                        "Error reason:" + e + "\n");
		                    error.setSegment(dataFile.getPath());
		                    parseResult.put(error);
							errorLines.add(count);
						}finally {
							count++;
						}
					}

				//<element dataType="string" content="default" value="C.0008.0001.R001"> , use xml value
				}else if(content != null && content.toLowerCase().contentEquals("default") && dataType != null && value != null){
					int count = 0;
					for(String[] lineDataFields:dataFields) {					
						keyValuePairs.get(count).put(fieldHandleIndex + "",value);
						count++;
					}
				
				}else {
					ReportError error = new ReportError();
                    error.setMessage("Xml file:" + XmlDecodeFile.xmlfile + "\n" +
	                        "Exists cant be handled config：dataType=" + dataType + ",content=" + content + ",fieldname="+ elementValue + ",value=" + value + "\n");
                    error.setSegment(dataFile.getPath());
                    parseResult.put(error);
                    return true;
				}
			}catch(Exception e) {
				ReportError error = new ReportError();
                error.setMessage("Data transform error:" + XmlDecodeFile.xmlfile + "\n" +
						"Data file：" + datafile + "\n" +
                        "Error reason：" + e + "\n");
                error.setSegment(dataFile.getPath());
                parseResult.put(error);
				return true;
			}
		}
		int linesCount = keyValuePairs.size();
		//去除Error 的Data line
		Iterator<Map<String,Object>>  iterator = keyValuePairs.iterator();
		int num = 0;
		while (iterator.hasNext()) {
		    Map<String,Object> map = iterator.next();
		    if(errorLines.contains(num)) {
		        iterator.remove();
            }
            num++;
        }
		
		//infoLogger.info("Data file：" + datafile + ",Total Data line数:" + linesCount +",correct data line count:" + keyValuePairs.size() +  ",unusual Data line count:" + (linesCount - keyValuePairs.size()) + "\n");
		System.out.println("Data file：" + datafile + ",Total Data line数:" + linesCount +",correct data line count:" + keyValuePairs.size() +  ",unusual Data line count:" + (linesCount - keyValuePairs.size()) + "\n");
		HashMap<String,List<Map<String,Object>>> finalValue = new HashMap<String,List<Map<String,Object>>>();		
		for(String tableName :XmlDecodeFile.xmlDecoder.keySet()) {
			ArrayList<Map<String,Object>> tableKeyValuePairs = new ArrayList<Map<String,Object>>();
			for(int lineIndex = 0 ;lineIndex<keyValuePairs.size();lineIndex++) {
				tableKeyValuePairs.add(new HashMap<String,Object>());
			}
			
			ArrayList<XmlDecodeFile> tableXmlDecoder = XmlDecodeFile.xmlDecoder.get(tableName);
			for(int fieldIndex = 0;fieldIndex<tableXmlDecoder.size();fieldIndex++) {
				XmlDecodeFile fieldDecoder = tableXmlDecoder.get(fieldIndex);
				String elementName = fieldDecoder.getElmentValue();
				String fieldHandleIndex = fieldDecoder.getFieldHandleIndex() + "";
				for(int lineIndex = 0 ;lineIndex<keyValuePairs.size();lineIndex++) {
					Object value = keyValuePairs.get(lineIndex).get(fieldHandleIndex);
					tableKeyValuePairs.get(lineIndex).put(elementName, value);
				}
			}
			finalValue.put(tableName,tableKeyValuePairs);
		}
		parseResult.put(finalValue);
		parseResult.setSuccess(true);
		return readOver;
	}
	public static void main(String[] args) {
//	    XmlDecodeFile.XmlFileDecode(LoggerFactory.getLogger("loggerInfo"), "D:\\code4\\cimiss2-dwp\\cimiss2-dwp-multithread\\config\\common_xml\\OCEN_SHB_GLB_FTM_REAL.xml");
//	    new DecodeXml().decode("C:\\Users\\冯胜\\Desktop\\西北人影（刘雯霞）\\模板配置入库\\全球海表观测定时值数据集_测试样例\\样例数据\\1979010104.txt");

	    //XmlDecodeFile.XmlFileDecode(LoggerFactory.getLogger("loggerInfo"), "D:\\code4\\cimiss2-dwp\\cimiss2-dwp-multithread\\config\\common_xml\\UPAR_ALL.xml");
	    //new DecodeXml().decode("C:\\Users\\冯胜\\Desktop\\西北人影（刘雯霞）\\模板配置入库\\高空数据集\\高空数据样例\\Upar_ALL_2016010112_r.TXT");


	}
}