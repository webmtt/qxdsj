package com.thinkgem.jeesite.modules.recordquery.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "STAT_FTPURLCONF")
public class FtpUrlConf implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dataCode;
	private String chnName;
	private String ftpUrls;
	private String ftpUrl01;
	private String ftpUrl02;
	private String matchUrl;
	private String getDataUrl;
	public FtpUrlConf(){
		super();
	}
	
	public FtpUrlConf(String dataCode){
		this();
		this.dataCode = dataCode;
	}
	@Id
	public String getDataCode() {
		return dataCode;
	}
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}
	public String getChnName() {
		return chnName;
	}
	public void setChnName(String chnName) {
		this.chnName = chnName;
	}
	public String getFtpUrls() {
		return ftpUrls;
	}
	public void setFtpUrls(String ftpUrls) {
		this.ftpUrls = ftpUrls;
	}
	public String getFtpUrl01() {
		return ftpUrl01;
	}
	public void setFtpUrl01(String ftpUrl01) {
		this.ftpUrl01 = ftpUrl01;
	}
	public String getFtpUrl02() {
		return ftpUrl02;
	}
	public void setFtpUrl02(String ftpUrl02) {
		this.ftpUrl02 = ftpUrl02;
	}
	public String getMatchUrl() {
		return matchUrl;
	}
	public void setMatchUrl(String matchUrl) {
		this.matchUrl = matchUrl;
	}
	public String getGetDataUrl() {
		return getDataUrl;
	}
	public void setGetDataUrl(String getDataUrl) {
		this.getDataUrl = getDataUrl;
	}
}
