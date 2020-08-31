package cma.cimiss2.dpc.decoder.grib.di_ei;

/**
 * @类名：RestfulInfo
 * @类功能：Restful接口信息
 * @作者：zhengbo
 * @版权：国家气象信息中心
 * @创建时间：2017-11-03
 * @备注：用于国家信息中心监视系统
 * @变更：
 * 	
 */
public class RestfulInfo {
	String type;	///接口类型
	String name;	///
	String message;
	String occur_time; //资料时间，以时间戳的形式
	
	Object fields;
	
	public RestfulInfo() {
		
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public void setoccur_time(String occur_time) {
		this.occur_time = occur_time;
	}

	public void setFields(Object fields) {
		this.fields = fields;
	}
	
	
}

