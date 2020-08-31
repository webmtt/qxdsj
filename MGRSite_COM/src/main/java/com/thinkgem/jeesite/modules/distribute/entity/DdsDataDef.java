package com.thinkgem.jeesite.modules.distribute.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "dds_datadef")
public class DdsDataDef {
	
	private String dataId;//数据id
	private String interfaceType;//接口类型
	private String dataFormat;//数据格式
	private String seParator;//分隔符
	private String nameFormat;//命名格式
	private int invalid;//是否无效
	private Integer orderNo;//排序号
	private Date created;//记录创建时间
	private String createdBy;//记录创建主机名
	private Date updated;//记录更新时间
	private String updatedBy;//记录更新主机名
	private String timeFormat;//时间格式
	private String pushTargetPath;//推送目标路径
	private int searchType;//检索类型
	private String searchTargetPath;//检索文件路径
	private String hostName;//主机名
	private Integer timeSpan;//时间跨度
	private Integer delay;//时间延迟
	private String spanUnit;//时间跨度单位
	private String delayUnit;//时间延迟单位
	private Integer timeRate;//执行频率时间
	private String rateUnit;//执行频率时间单位
	private Integer delayMinute;//延迟分钟
	private String timeCondBegin;//时间条件开始
	private String timeCondEnd;//时间条件结束
	private int isRedo;//是否重做  0 默认值 不重做， 1 重做
	private Integer id;//id  自增
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getInterfaceType() {
		return interfaceType;
	}
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	public String getDataFormat() {
		return dataFormat;
	}
	public void setDataFormat(String dataFormat) {
		this.dataFormat = dataFormat;
	}
	public String getSeParator() {
		return seParator;
	}
	public void setSeParator(String seParator) {
		this.seParator = seParator;
	}
	public String getNameFormat() {
		return nameFormat;
	}
	public void setNameFormat(String nameFormat) {
		this.nameFormat = nameFormat;
	}
	public int getInvalid() {
		return invalid;
	}
	public void setInvalid(int invalid) {
		this.invalid = invalid;
	}
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getUpdated() {
		return updated;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getTimeFormat() {
		return timeFormat;
	}
	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}
	public String getPushTargetPath() {
		return pushTargetPath;
	}
	public void setPushTargetPath(String pushTargetPath) {
		this.pushTargetPath = pushTargetPath;
	}
	public int getSearchType() {
		return searchType;
	}
	public void setSearchType(int searchType) {
		this.searchType = searchType;
	}
	public String getSearchTargetPath() {
		return searchTargetPath;
	}
	public void setSearchTargetPath(String searchTargetPath) {
		this.searchTargetPath = searchTargetPath;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public Integer getTimeSpan() {
		return timeSpan;
	}
	public void setTimeSpan(Integer timeSpan) {
		this.timeSpan = timeSpan;
	}
	public Integer getDelay() {
		return delay;
	}
	public void setDelay(Integer delay) {
		this.delay = delay;
	}
	public String getSpanUnit() {
		return spanUnit;
	}
	public void setSpanUnit(String spanUnit) {
		this.spanUnit = spanUnit;
	}
	public String getDelayUnit() {
		return delayUnit;
	}
	public void setDelayUnit(String delayUnit) {
		this.delayUnit = delayUnit;
	}
	public Integer getTimeRate() {
		return timeRate;
	}
	public void setTimeRate(Integer timeRate) {
		this.timeRate = timeRate;
	}
	public String getRateUnit() {
		return rateUnit;
	}
	public void setRateUnit(String rateUnit) {
		this.rateUnit = rateUnit;
	}
	public Integer getDelayMinute() {
		return delayMinute;
	}
	public void setDelayMinute(Integer delayMinute) {
		this.delayMinute = delayMinute;
	}
	public String getTimeCondBegin() {
		return timeCondBegin;
	}
	public void setTimeCondBegin(String timeCondBegin) {
		this.timeCondBegin = timeCondBegin;
	}
	public String getTimeCondEnd() {
		return timeCondEnd;
	}
	public void setTimeCondEnd(String timeCondEnd) {
		this.timeCondEnd = timeCondEnd;
	}
	public int getIsRedo() {
		return isRedo;
	}
	public void setIsRedo(int isRedo) {
		this.isRedo = isRedo;
	}
	@Id
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	
	
	
	
	
	
	
}
