package cma.cimiss2.dpc.decoder.agme;

// TODO: Auto-generated Javadoc
/**
 * The Class ParseBean.
 */
public class ParseBean {
	
	/** The element name. */
	private String elementName;
	
	/** The data type. */
	private DataType dataType;
	
	/** The content. */
	private Content content;
	
	/** The index. */
	private String index;
	
	/** The value. */
	private String value;
	
	/** The Is calc. */
	private boolean IsCalc;
	
	/** The expression. */
	private String expression;
	
	/** The format. */
	private String format;
	
	/** The default value. */
	private String defaultValue;
	
	/**
	 * Instantiates a new parses the bean.
	 */
	public ParseBean() {
		super();
		this.IsCalc = false;
	}
	
	/**
	 * Gets the default value.
	 *
	 * @return the default value
	 */
	public String getDefaultValue(){
		return defaultValue;
	}
	
	/**
	 * Sets the default value.
	 *
	 * @param defaultValue the new default value
	 */
	public void setDefaultValue(String defaultValue){
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Gets the element name.
	 *
	 * @return the element name
	 */
	public String getElementName() {
		return elementName;
	}
	
	/**
	 * Sets the element name.
	 *
	 * @param elementName the new element name
	 */
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	
	/**
	 * Gets the data type.
	 *
	 * @return the data type
	 */
	public DataType getDataType() {
		return dataType;
	}
	
	/**
	 * Sets the data type.
	 *
	 * @param dataType the new data type
	 */
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}
	
	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public Content getContent() {
		return content;
	}
	
	/**
	 * Sets the content.
	 *
	 * @param content the new content
	 */
	public void setContent(Content content) {
		this.content = content;
	}
	
	/**
	 * Gets the index.
	 *
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}
	
	/**
	 * Sets the index.
	 *
	 * @param index the new index
	 */
	public void setIndex(String index) {
		this.index = index;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Checks if is checks if is calc.
	 *
	 * @return true, if is checks if is calc
	 */
	public boolean isIsCalc() {
		return IsCalc;
	}
	
	/**
	 * Sets the checks if is calc.
	 *
	 * @param isCalc the new checks if is calc
	 */
	public void setIsCalc(boolean isCalc) {
		IsCalc = isCalc;
	}
	
	/**
	 * Gets the expression.
	 *
	 * @return the expression
	 */
	public String getExpression() {
		return expression;
	}
	
	/**
	 * Sets the expression.
	 *
	 * @param expression the new expression
	 */
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	/**
	 * Gets the format.
	 *
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}
	
	/**
	 * Sets the format.
	 *
	 * @param format the new format
	 */
	public void setFormat(String format) {
		this.format = format;
	}
	
	

}
