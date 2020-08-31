package com.nmpiesat.idata.data.entity;


public class Comparas {
	private String keyID;
	private String description;
	private String type;
	private String boolEanValue;
	private Integer intValue;
	private String stringValue;
	private String invalid;

	public String getKeyID() {
		return keyID;
	}

	public void setKeyID(String keyID) {
		this.keyID = keyID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBoolEanValue() {
		return boolEanValue;
	}

	public void setBoolEanValue(String boolEanValue) {
		this.boolEanValue = boolEanValue;
	}

	public Integer getIntValue() {
		return intValue;
	}

	public void setIntValue(Integer intValue) {
		this.intValue = intValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public String getInvalid() {
		return invalid;
	}

	public void setInvalid(String invalid) {
		this.invalid = invalid;
	}

	@Override
	public String toString() {
		return "Comparas [keyID=" + keyID + ", description=" + description + ", type=" + type + ", boolEanValue=" + boolEanValue + ", intValue=" + intValue + ", stringValue=" + stringValue + ", invalid=" + invalid + "]";
	}

}
