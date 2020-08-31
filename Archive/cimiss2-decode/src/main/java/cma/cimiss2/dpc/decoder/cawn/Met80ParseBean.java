package cma.cimiss2.dpc.decoder.cawn;

import cma.cimiss2.dpc.decoder.agme.Content;
import cma.cimiss2.dpc.decoder.agme.DataType;

public class Met80ParseBean {
	private String elementName;
	private DataType dataType;
	private Content content;
	private String index;
	private String value;
	private boolean IsCalc;
	private String expression;
	private String format;
	private String split;
	public Met80ParseBean() {
		super();
		this.IsCalc = false;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public DataType getDataType() {
		return dataType;
	}
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	public String getIndex() {
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isIsCalc() {
		return IsCalc;
	}
	public void setIsCalc(boolean isCalc) {
		IsCalc = isCalc;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getSplit() {
		return split;
	}
	public void setSplit(String split) {
		this.split = split;
	}
	
	

}
