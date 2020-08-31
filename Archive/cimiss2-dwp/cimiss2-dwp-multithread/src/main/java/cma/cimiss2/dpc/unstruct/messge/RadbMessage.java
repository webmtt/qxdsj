package cma.cimiss2.dpc.unstruct.messge;

public class RadbMessage {
	private String fileName;
	private String fullPath;
	private byte[] fileContents;
	private String ctsCode;
	private String sodCode;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFullPath() {
		return fullPath;
	}
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}
	public byte[] getFileContents() {
		return fileContents;
	}
	public void setFileContents(byte[] fileContents) {
		this.fileContents = fileContents;
	}
	public String getCtsCode() {
		return ctsCode;
	}
	public void setCtsCode(String ctsCode) {
		this.ctsCode = ctsCode;
	}
	public String getSodCode() {
		return sodCode;
	}
	public void setSodCode(String sodCode) {
		this.sodCode = sodCode;
	}

}
