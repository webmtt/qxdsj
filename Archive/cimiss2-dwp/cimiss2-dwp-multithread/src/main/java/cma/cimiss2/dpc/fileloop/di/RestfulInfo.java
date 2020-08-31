package cma.cimiss2.dpc.fileloop.di;

import java.io.Serializable;

/**
 * 
 * <br>
 * @Title:  RestfulInfo.java   
 * @Package cma.cimiss2.dpc.decoder.tools.vo.common   
 * @Description:    TODO(Di和Ei包裹类型)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月15日 下午5:30:50   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public class RestfulInfo  implements Serializable {
	/**   
	 * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)   
	 */ 
	private static final long serialVersionUID = 1L;
	String type;	///接口类型
	String name;
	String message;
	Object fields;
	long occur_time;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getFields() {
		return fields;
	}

	public void setFields(Object fields) {
		this.fields = fields;
	}
	public Object getOccur_time() {
		return occur_time;
	}

	public void setOccur_time(long occur_time) {
		this.occur_time = occur_time;
	}
}
