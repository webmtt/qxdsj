package com.thinkgem.jeesite.modules.recordquery.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户Entity
 */
@Entity
@Table(name = "STAT_ORDERACCESSINFO")
public class OrderAccessInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private int Id;
	private String d_datetime;
	public int getId() {
		return Id;
	}
	public void setId(int id) {
		Id = id;
	}
	public String getD_datetime() {
		return d_datetime;
	}
	public void setD_datetime(String d_datetime) {
		this.d_datetime = d_datetime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getDataCode() {
		return dataCode;
	}
	public void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}
	public Integer getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(Integer fileNumber) {
		this.fileNumber = fileNumber;
	}
	public Long getFileQuantity() {
		return fileQuantity;
	}
	public void setFileQuantity(Long fileQuantity) {
		this.fileQuantity = fileQuantity;
	}
	public Long getDownloadquantity() {
		return downloadquantity;
	}
	public void setDownloadquantity(Long downloadquantity) {
		this.downloadquantity = downloadquantity;
	}
	private String dataCode;
	private String type;
	private Integer fileNumber;
	private Integer orderNumber;
	private Long fileQuantity;
	private Long downloadquantity;
}
