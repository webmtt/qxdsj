package com.thinkgem.jeesite.modules.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 统计电话数
 * @author cuijingtao
 * @version 2016-07-11
 */
@Entity
@Table(name = "cdr_ex")
public class Cdrex implements Serializable{
	private static final long serialVersionUID = 1L;
	private Double ID;
	private String callDate;//呼入时间
	private String dstId;//被叫的工号
	private String disPosition;//状态 ANSWERED  NO ANSWER
	private String billSec;//通话时长
	private String src;//主叫号码
	private String srcid;//座席的工号
	private String dst;//被叫号码
	private String clid;//用于判断电话是来电还是外呼
	private String duration;//电话时长，单位秒(包含通话时长和振铃时长)
	private int srvid;
	private String uniqueid;
	//@Id
	public String getUniqueid() {
		return uniqueid;
	}
	public void setUniqueid(String uniqueid) {
		this.uniqueid = uniqueid;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getSrcid() {
		return srcid;
	}
	public void setSrcid(String srcid) {
		this.srcid = srcid;
	}
	public String getDst() {
		return dst;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public String getClid() {
		return clid;
	}
	public void setClid(String clid) {
		this.clid = clid;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public int getSrvid() {
		return srvid;
	}
	public void setSrvid(int srvid) {
		this.srvid = srvid;
	}
	@Id
	public Double getID() {
		return ID;
	}
	public void setID(Double iD) {
		ID = iD;
	}
	//@Id
	@Column(name="calldate")
	public String getCallDate() {
		return callDate;
	}
	public void setCallDate(String callDate) {
		this.callDate = callDate;
	}
	public String getDstId() {
		return dstId;
	}
	public void setDstId(String dstId) {
		this.dstId = dstId;
	}
	public String getDisPosition() {
		return disPosition;
	}
	public void setDisPosition(String disPosition) {
		this.disPosition = disPosition;
	}
	public String getBillSec() {
		return billSec;
	}
	public void setBillSec(String billSec) {
		this.billSec = billSec;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
