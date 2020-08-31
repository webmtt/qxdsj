package cma.cimiss2.dpc.indb.vo;

public class FileInfo {
	/**
	 * CTS资料四级编码
	 */
	private String cts_code;
	
	/**
	 * 文件全路径
	 */
	private String filepath;
	
	/**
	 * 文件分割符
	 */
	private String splitRegex;
	
	public FileInfo() {
		
	}
	
	public FileInfo(String cts_code, String filepath, String splitRegex) {
		this.cts_code = cts_code;
		this.filepath = filepath;
		this.splitRegex = splitRegex;
	}
	
	public String getCts_code() {
		return cts_code;
	}
	public void setCts_code(String cts_code) {
		this.cts_code = cts_code;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getSplitRegex() {
		return splitRegex;
	}

	public void setSplitRegex(String splitRegex) {
		this.splitRegex = splitRegex;
	}
}
