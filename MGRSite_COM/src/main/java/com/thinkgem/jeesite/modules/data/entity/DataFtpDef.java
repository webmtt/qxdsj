package com.thinkgem.jeesite.modules.data.entity;

import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
 * 
 * 描述：数据Ftp下载
 *
 * @author Administrator
 * @version 1.0 2016年10月9日
 */
@Entity
@Table(name = "BMD_DATAFTPDEF")
public class DataFtpDef {
	private String datacode;
	private String ftpurls;
	private String ftpnames;
	private String ftpurl01;
	private String ftpurl02;
	private String ftpurl03;
	private String ftpurl04;
	private String ftpurl05;
	private String ftpurl06;
	private String ftpurl07;
	private String ftpurl08;
	private String ftpurl09;
	private String ftpurl10;
	private Integer invalid;
	@Id
	public String getDatacode() {
		return datacode;
	}
	public void setDatacode(String datacode) {
		this.datacode = datacode;
	}
	public String getFtpurls() {
		return ftpurls;
	}
	public void setFtpurls(String ftpurls) {
		this.ftpurls = ftpurls;
	}
	public String getFtpnames() {
		return ftpnames;
	}
	public void setFtpnames(String ftpnames) {
		this.ftpnames = ftpnames;
	}
	public String getFtpurl01() {
		return ftpurl01;
	}
	public void setFtpurl01(String ftpurl01) {
		this.ftpurl01 = ftpurl01;
	}
	public String getFtpurl02() {
		return ftpurl02;
	}
	public void setFtpurl02(String ftpurl02) {
		this.ftpurl02 = ftpurl02;
	}
	public String getFtpurl03() {
		return ftpurl03;
	}
	public void setFtpurl03(String ftpurl03) {
		this.ftpurl03 = ftpurl03;
	}
	public String getFtpurl04() {
		return ftpurl04;
	}
	public void setFtpurl04(String ftpurl04) {
		this.ftpurl04 = ftpurl04;
	}
	public String getFtpurl05() {
		return ftpurl05;
	}
	public void setFtpurl05(String ftpurl05) {
		this.ftpurl05 = ftpurl05;
	}
	public String getFtpurl06() {
		return ftpurl06;
	}
	public void setFtpurl06(String ftpurl06) {
		this.ftpurl06 = ftpurl06;
	}
	public String getFtpurl07() {
		return ftpurl07;
	}
	public void setFtpurl07(String ftpurl07) {
		this.ftpurl07 = ftpurl07;
	}
	public String getFtpurl08() {
		return ftpurl08;
	}
	public void setFtpurl08(String ftpurl08) {
		this.ftpurl08 = ftpurl08;
	}
	public String getFtpurl09() {
		return ftpurl09;
	}
	public void setFtpurl09(String ftpurl09) {
		this.ftpurl09 = ftpurl09;
	}
	public String getFtpurl10() {
		return ftpurl10;
	}
	public void setFtpurl10(String ftpurl10) {
		this.ftpurl10 = ftpurl10;
	}
	public Integer getInvalid() {
		return invalid;
	}
	public void setInvalid(Integer invalid) {
		this.invalid = invalid;
	}
	
}
