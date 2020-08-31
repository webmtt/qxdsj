package com.thinkgem.jeesite.modules.recordquery.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户Entity
 */

public class OrderAccessInfoModel implements Serializable {
	/**
	 * the OrderAccessInfo.java
	 */
	private static final long serialVersionUID = 1L;
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
	public String getDatacode() {
		return datacode;
	}
	public void setDatacode(String datacode) {
		this.datacode = datacode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getFileNumber() {
		return fileNumber;
	}
	public void setFileNumber(int fileNumber) {
		this.fileNumber = fileNumber;
	}

	private String datacode;
	private String type;
	private int fileNumber;
	private Integer orderNumber;
	private Long fileQuantity;
	private Long downloadquantity;
    private String time;
    private Integer number;
    private int count;
    private String title;
    private Double downNumber;//下载质量
    private Double fileQy;//文件质量
    /** 自定义构造方法*/
    public OrderAccessInfoModel(String time,Double downNumber, int count) {
   	 this.setTime(time);
   	this.setDownNumber(downNumber);
   	 this.count = count;
    }
    public OrderAccessInfoModel(String time,int  number, Double fileQy) {
      	 this.setTime(time);
      	 this.setNumber(number);
      	this.setFileQy(fileQy);
     }
    public OrderAccessInfoModel(String time,int  number, int count) {
     	 this.setTime(time);
     	 this.setNumber(number);
     	 this.count = count;
    }
    public OrderAccessInfoModel(String datacode,int number) {
    	this.datacode = datacode;
    	this.setNumber(number);
    }
    public OrderAccessInfoModel(String datacode,Double downNumber) {
    	this.datacode = datacode;
    	this.setDownNumber(downNumber);
    }
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
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
	public Double getDownNumber() {
		return downNumber;
	}
	public void setDownNumber(Double downNumber) {
		this.downNumber = downNumber;
	}
	public Double getFileQy() {
		return fileQy;
	}
	public void setFileQy(Double fileQy) {
		this.fileQy = fileQy;
	}
	@Override
	public String toString() {
		return "OrderAccessInfoModel [Id=" + Id + ", d_datetime=" + d_datetime + ", datacode=" + datacode + ", type="
				+ type + ", fileNumber=" + fileNumber + ", orderNumber=" + orderNumber + ", fileQuantity="
				+ fileQuantity + ", downloadquantity=" + downloadquantity + ", time=" + time + ", number=" + number
				+ ", count=" + count + ", title=" + title + ", downNumber=" + downNumber + ", fileQy=" + fileQy + "]";
	}
    
    
}