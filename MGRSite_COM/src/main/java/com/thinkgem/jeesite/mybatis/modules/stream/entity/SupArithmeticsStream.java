/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.mybatis.modules.stream.entity;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;
import org.hibernate.validator.constraints.Length;

/**
 * 算法池基本信息Entity
 * @author yck
 * @version 2019-12-13
 */
public class SupArithmeticsStream extends DataEntity<SupArithmeticsStream> {
	
	private static final long serialVersionUID = 1L;
	private String streamName;		// 算法池名称
	private String purpose;		// 算法池用途
	private String sapId;		// 算法名称
	private String sequence;		// 算法顺序
	private String status;		// 当前状态
	private String params; // 方法参数信息
	private String pridictValue; // 预期值

	public void setPridictValue(String pridictValue) {
		this.pridictValue = pridictValue;
	}

	public String getPridictValue() {
		return pridictValue;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
	public SupArithmeticsStream() {
		super();
	}

	public SupArithmeticsStream(String id){
		super(id);
	}

	@Length(min=1, max=100, message="算法池名称长度必须介于 1 和 100 之间")
	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	
	@Length(min=1, max=500, message="算法池用途长度必须介于 1 和 255 之间")
	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	@Length(min=1, max=255, message="算法名称长度必须介于 1 和 255 之间")
	public String getSapId() {
		return sapId;
	}

	public void setSapId(String sapId) {
		this.sapId = sapId;
	}
	
	@Length(min=1, max=255, message="算法顺序长度必须介于 1 和 255 之间")
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	@Length(min=1, max=10, message="当前状态长度必须介于 1 和 10 之间")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}