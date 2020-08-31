/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.aboutus.entity;

import org.hibernate.validator.constraints.Length;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;

/**
 * 关于我们信息维护Entity
 * @author songyan
 * @version 2020-02-27
 */
public class AboutUs extends DataEntity<AboutUs> {
	
	private static final long serialVersionUID = 1L;
	private String platformIntroduction;		// 平台简介
	private String telephone;		// 电话
	private String postcode;		// 邮编
	private String email;		// 邮箱
	
	public AboutUs() {
		super();
	}

	public AboutUs(String id){
		super(id);
	}

	public String getPlatformIntroduction() {
		return platformIntroduction;
	}

	public void setPlatformIntroduction(String platformIntroduction) {
		this.platformIntroduction = platformIntroduction;
	}
	
	@Length(min=0, max=255, message="电话长度必须介于 0 和 255 之间")
	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@Length(min=0, max=255, message="邮编长度必须介于 0 和 255 之间")
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	@Length(min=0, max=255, message="邮箱长度必须介于 0 和 255 之间")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}