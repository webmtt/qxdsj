package org.cimiss2.dwp.tools.config;

import java.util.List;
/**
 * <br>
 * @Title:  CTSCodeMap.java   
 * @Package org.cimiss2.dwp.tools.config   
 * @Description:    TODO(资料CTS、SOD、报文SOD编码的实体类)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2018年7月20日 下午2:35:07   cuihongyuan    Initial creation.
 * </pre>
 * 
 * @author cuihongyuan
 *
 *
 */
public class CTSCodeMap {
	private String cts_code;
	private String sod_code;
	private String report_sod_code;
	private String value_table_name;
	private String report_table_name;
	/**
	 * @Title: findSodByCTS   
	 * @Description: TODO(根据资料的CTS查找其SOD编码)   
	 * @param cts
	 * @param ctsCodeMaps
	 * @return String      
	 * @throws：
	 */
	public static String findSodByCTS(String cts, List<CTSCodeMap> ctsCodeMaps){
		String sod = "";
		for(int i = 0; i < ctsCodeMaps.size(); i ++){
			if(ctsCodeMaps.get(i).getCts_code().equals(cts)){
				sod = ctsCodeMaps.get(i).getSod_code();
				break;
			}
		}
		return sod;
	}
	/**
	 * @Title: findReportSodByCTS   
	 * @Description: TODO(根据资料的CTS查找其报文SOD编码)   
	 * @param cts
	 * @param ctsCodeMaps
	 * @return String      
	 * @throws：
	 */
	public static String findReportSodByCTS(String cts, List<CTSCodeMap> ctsCodeMaps){
		String reportSod = "";
		for(int i = 0; i < ctsCodeMaps.size(); i ++){
			if(ctsCodeMaps.get(i).getCts_code().equals(cts)){
				reportSod = ctsCodeMaps.get(i).getReport_sod_code();
				break;
			}
		}
		return reportSod;
	}
	
	public String getCts_code() {
		return cts_code;
	}
	public void setCts_code(String cts_code) {
		this.cts_code = cts_code;
	}
	public String getSod_code() {
		return sod_code;
	}
	public void setSod_code(String sod_code) {
		this.sod_code = sod_code;
	}
	public String getReport_sod_code() {
		return report_sod_code;
	}
	public void setReport_sod_code(String report_sod_code) {
		this.report_sod_code = report_sod_code;
	}
	public String getValue_table_name() {
		return value_table_name;
	}
	public void setValue_table_name(String value_table_name) {
		this.value_table_name = value_table_name;
	}
	public String getReport_table_name() {
		return report_table_name;
	}
	public void setReport_table_name(String report_table_name) {
		this.report_table_name = report_table_name;
	}
	
	
}
