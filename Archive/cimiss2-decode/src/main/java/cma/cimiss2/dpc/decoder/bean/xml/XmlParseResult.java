package cma.cimiss2.dpc.decoder.bean.xml;

import java.util.ArrayList;
import java.util.List;

public class XmlParseResult {
	boolean parseResult;
	List<XmlIndexObject> xmlIndexObjects;
	String forOrignalIndex;
	
	public boolean isParseResult() {
		return parseResult;
	}
	public void setParseResult(boolean parseResult) {
		this.parseResult = parseResult;
	}
	public List<XmlIndexObject> getXmlIndexObjects() {
		return xmlIndexObjects;
	}
	public void setXmlIndexObjects(List<XmlIndexObject> xmlIndexObjects) {
		this.xmlIndexObjects = xmlIndexObjects;
	}
	public String getForOrignalIndex() {
		return forOrignalIndex;
	}
	public void setForOrignalIndex(String forOrignalIndex) {
		this.forOrignalIndex = forOrignalIndex;
	}
	public XmlParseResult(){
		parseResult = false;
		xmlIndexObjects = new ArrayList<XmlIndexObject>();
		forOrignalIndex = "";
	}

}
