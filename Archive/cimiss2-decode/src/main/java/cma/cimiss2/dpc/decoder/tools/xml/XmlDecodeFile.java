package cma.cimiss2.dpc.decoder.tools.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cma.cimiss2.dpc.decoder.bean.xml.XmlIndexObject;
import cma.cimiss2.dpc.decoder.bean.xml.XmlParseResult;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.slf4j.Logger;

public class XmlDecodeFile {
	String content;
	String dataType;
	String value;
	String dataLength;
	List<XmlIndexObject> index;
	String orignalIndex;
	String format;
	String elementValue;
	int fieldHandleIndex;
	boolean parseErrorHandle;
	String parseErrorDefaultValue;
	boolean expressHandle;
	String expressKey;
	double expressValue;
	boolean defaultControl;
	String defaultVal;
	//table -- fields of this table
	public static HashMap<String,ArrayList<XmlDecodeFile>> xmlDecoder = null;
	//index -- all fields
	public static ArrayList<XmlDecodeFile> handleFieldDecoder = null;
	//confirm    data file--one line--data fields count
	public static int lineFieldLength = 0;
	//use which character to split data file--one line--data fields
	public static String dataSpliter = "";
	//file head lines count
	public static int headLinesCount = 0;
	//only when true can continue
	public static boolean isChecked = false;
	//data file use while special string to discrible default value
	public static String defaultFiled = "";
	//default field value data type
	public static String defaultType = "";
	//xml file name
	public static String xmlfile = "";
	
	
	
	public XmlDecodeFile() {
		content = null;
		dataType = null;
		value = null;
		dataLength = null;
		index = null;
		format = null;
		expressHandle = false;
		expressValue = 0.0f;
		fieldHandleIndex = 0;	
		expressKey = null;
		defaultControl = false;
		defaultVal = null;
		
	}

	public boolean isDefaultControl() {
		return defaultControl;
	}

	public void setDefaultControl(boolean defaultControl) {
		this.defaultControl = defaultControl;
	}

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public boolean isExpressHandle() {
		return expressHandle;
	}

	public void setExpressHandle(boolean expressHandle) {
		this.expressHandle = expressHandle;
	}

	public double getExpressValue() {
		return expressValue;
	}

	public void setExpressValue(double expressValue) {
		this.expressValue = expressValue;
	}

	public boolean isParseErrorHandle() {
		return parseErrorHandle;
	}

	public void setParseErrorHandle(boolean parseErrorHandle) {
		this.parseErrorHandle = parseErrorHandle;
	}

	public String getParseErrorDefaultValue() {
		return parseErrorDefaultValue;
	}

	public void setParseErrorDefaultValue(String parseErrorDefaultValue) {
		this.parseErrorDefaultValue = parseErrorDefaultValue;
	}


	public String getExpressKey() {
		return expressKey;
	}

	public void setExpressKey(String expressKey) {
		this.expressKey = expressKey;
	}

	//set field  content type:report,default,filename
	public void setContent(String content) {
		this.content = content;
	}
	//set field data type:int,double,string,datetime
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	//set field value,only can be use when content = default,value can be real value,or SYSDATE for current system datetime,or D_RYMDHM for file last modified time
	public void setValue(String value) {
		this.value = value;
	}
	//set field data length
	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}
	//set field index,only can be use when content = report,use {} to confirm data file index
	public void setIndex(List<XmlIndexObject> index) {
		this.index = index;
	}
	public String getOrignalIndex() {
		return orignalIndex;
	}

	public void setOrignalIndex(String orignalIndex) {
		this.orignalIndex = orignalIndex;
	}
	//index of handleFieldDecoder
	public void setFieldHandleIndex(int index) {
		this.fieldHandleIndex = index;
	}
	//set field format
	public void setFormat(String format) {
		this.format = format;
	}
	//set field name
	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}
	//return field name
	public String getElmentValue() {
		return elementValue;
	}
	//return field content type:report,default,filename
	public String getContent() {
		return this.content;
	}
	//return field data type:int,double,string,datetime
	public String getDataType() {
		return this.dataType;
	}
	//return field value,only can be use when content = default,value can be real value,or SYSDATE for current system datetime,or D_RYMDHM for file last modified time
	public String getValue() {
		return this.value;
	}
	//return index of handleFieldDecoder
	public int getFieldHandleIndex(){
		return this.fieldHandleIndex;
	}
	//return field data length
	public String getDataLength() {
		return this.dataLength;
	}
	//return field index
	public List<XmlIndexObject> getIndex() {
		return this.index;
	}
	//return field format
	public String getFormat() {
		return this.format;
	}
	//decode table config
	public static boolean XmlFileDecode(Logger log,String xmlfile) {
		log.info("helo");
		XmlDecodeFile.xmlfile = xmlfile;
		Element re = null;
		try {
			Document xml = XmlOper.getDoc(xmlfile);
			if(xml == null) {
				log.error("\n Parse Error:" + xmlfile + "\n" +
	                    " Error Reason:file content can't be correct read" + "\n");
				return false;
			}
			re = xml.getRootElement();
			if(re == null) {
				log.error("\n Parse Error:" + xmlfile + "\n" +
	                    " Error Reason:cant't found root element" + "\n");
				return false;
			}
			List<Attribute> rootAttrs=re.attributes();
			for(Attribute attr:rootAttrs){
				switch(attr.getName().toUpperCase()) {
					case "LENGTH":lineFieldLength = Integer.parseInt(attr.getValue());break;
					case "SPLIT":dataSpliter = attr.getValue();	break;
					case "HEADLINESCOUNT":headLinesCount = Integer.parseInt(attr.getValue());break;
					case "ISCHECKED":isChecked = (attr.getValue().toUpperCase().equals("TRUE"));break;
					case "DEFAULTFIELD":defaultFiled = attr.getValue();break;
					case "DEFAULTTYPE":defaultType = attr.getValue();break;
				}
			}
		}catch(Exception e) {
			log.error("\n Parse Error:" + xmlfile + "\n" +
                    " Error Reason:" + e + "\n");
			return false;
		}
		if(lineFieldLength == 0 || dataSpliter.equals("") || isChecked == false){
			log.error("\n xml framework config lack:" + xmlfile + "\n");
			return false;
		}
		
		//xml字段处理读取
		List<Element> childElements = re.elements();//tables
		xmlDecoder = new HashMap<String,ArrayList<XmlDecodeFile>>();
		int handleFieldIndex = 0;
		handleFieldDecoder = new ArrayList<XmlDecodeFile>();
		for(Element child:childElements) {
			if(!child.getName().toUpperCase().contentEquals("TABLE")) continue;
			String tableName = "";
			List<Attribute> tableAttrs = child.attributes();
			for(Attribute attr:tableAttrs){
				switch(attr.getName().toUpperCase()) {
					case "NAME":tableName = attr.getValue();break;
				}
			}
			if(tableName.equals("")){
				log.error("\n Parse Error:" + xmlfile + "\n" +
		                        " Error Reason:" + "table element lack" + "\n");
				return false;
			}
			
			List<Element> fieldsHandle = child.elements();
			if(fieldsHandle.size() == 0) {
				log.error("\n Parse Problem:" + xmlfile + "\n" +
		                        " Problen describe:" + tableName + " config do not has input field config" + "\n");
				continue;
			}
			
			ArrayList<XmlDecodeFile> tableHandle = new ArrayList<XmlDecodeFile>();
			try {
					for(Element fieldHandle:fieldsHandle) {
						List<Attribute> fieldAttrs = fieldHandle.attributes();
						XmlDecodeFile oneFieldHandle = new XmlDecodeFile();
						String index = "";
						for(Attribute fieldAttr:fieldAttrs) {
							switch(fieldAttr.getName().toLowerCase()) {
								case "content":oneFieldHandle.setContent(fieldAttr.getValue());break;
								case "datatype":oneFieldHandle.setDataType(fieldAttr.getValue());break;
								case "value":oneFieldHandle.setValue(fieldAttr.getValue());break;
								case "datalength":oneFieldHandle.setDataLength(fieldAttr.getValue());break;
								case "index":index = fieldAttr.getValue();break;
								case "format":oneFieldHandle.setFormat(fieldAttr.getValue());break;
								case "expression":
									if(fieldAttr.getValue().length() <= 1){
										log.error("\n Parse xml file failed:" + xmlfile + "\n"
												+ "expression config error:" + fieldAttr.getValue());
										return false;
									}
									oneFieldHandle.setExpressHandle(true);
									String expressValue = fieldAttr.getValue().substring(1,fieldAttr.getValue().length());
									double expressValueD = 0.0f;
									try{
										expressValueD = Double.parseDouble(expressValue);
									}catch(Exception e){
										log.error("\n Parse xml file failed:" + xmlfile + "\n"
												+ "expression config error:" + fieldAttr.getValue() + "\n");
										return false;
									}
									oneFieldHandle.setExpressValue(expressValueD);
									oneFieldHandle.setExpressKey(fieldAttr.getValue().substring(0,1));
									break;
								case "errorvalue":oneFieldHandle.setParseErrorHandle(true);oneFieldHandle.setParseErrorDefaultValue(fieldAttr.getValue());break;
								case "defaultval":oneFieldHandle.setDefaultControl(true);oneFieldHandle.setDefaultVal(fieldAttr.getValue());break;
							}
						}
						if(oneFieldHandle.isExpressHandle() && !oneFieldHandle.getDataType().toLowerCase().equals("double")){
							log.error("\n Parse xml file failed:" + xmlfile + "\n" +
										" Value have to be expression handle,so the datatype must be double,but the config datatype is:" + oneFieldHandle.getDataType());
							return false;
						}
						
						List<XmlIndexObject> xmlIndexObjes = new ArrayList<XmlIndexObject>();
						//String forOrignalIndex = new String("");
						XmlParseResult parseIndexResult = new XmlParseResult();
						if(!index.equals("")) {
							parseIndexResult = XmlConfigParseHandle.parseIndexesToXmlIndexObjectes(xmlIndexObjes, log, xmlfile, lineFieldLength, index);
							if(!parseIndexResult.isParseResult()){
								log.error("\n Parse xml file failed:" + xmlfile + "\n");
								return false;
							}
							oneFieldHandle.setOrignalIndex(parseIndexResult.getForOrignalIndex());
							oneFieldHandle.setIndex(parseIndexResult.getXmlIndexObjects());
							
						}
						if(oneFieldHandle.isDefaultControl() && oneFieldHandle.getIndex().size() > 1){
							log.error("\n Parse xml file failed:" + xmlfile + "\n" +
									" Configuration erro!Confirm default value,so index must be one" + index);
							return false;
						}
						oneFieldHandle.setElementValue(fieldHandle.getText());
						oneFieldHandle.setFieldHandleIndex(handleFieldIndex);
						handleFieldIndex++;
						handleFieldDecoder.add(oneFieldHandle);
					
//						if(handleFieldDecoder.size() == 0) {
//							oneFieldHandle.setFieldHandleIndex(handleFieldIndex);
//							handleFieldIndex++;
//							handleFieldDecoder.add(oneFieldHandle);
//						}else {
//							int flag = 0;
//							//find oneFieldHandle exists in handleFieldDecoder 
//							for(XmlDecodeFile fieldDecoder:handleFieldDecoder) {
//								if(XmlDecodeFile.equals(fieldDecoder,oneFieldHandle)) {
//									flag = fieldDecoder.getFieldHandleIndex();
//									break;
//								}
//							}
//							if(flag != 0) {
//								//find,set oneFieldHandle's fieldHandleIndex(index of handleFieldDecoder)
//								oneFieldHandle.setFieldHandleIndex(flag);
//							}else {
//								//not find, add to handleFieldDecoder
//								oneFieldHandle.setFieldHandleIndex(handleFieldIndex);
//								handleFieldIndex++;
//								handleFieldDecoder.add(oneFieldHandle);
//							}
//						}
					
						tableHandle.add(oneFieldHandle);
					}
			}catch(Exception e) {
				log.error("\n Xml config file parse error:" + xmlfile + "\n" +
			                        " Error Reason:" + e + "\n");
				return false;
			}
			xmlDecoder.put(tableName,tableHandle);
		}
		log.info("\n Xml config file parse complite:" + xmlfile + "\n");
		return true;
	}
	
	public static boolean equals(XmlDecodeFile a,XmlDecodeFile b) {
		//if(a.index == b.index && a.dataLength == b.dataLength && a.format == b.format && a.value == b.value && a.dataType == b.dataType && a.content == b.content)
		//	return true;
		//else return false;
		return false;
	}
}