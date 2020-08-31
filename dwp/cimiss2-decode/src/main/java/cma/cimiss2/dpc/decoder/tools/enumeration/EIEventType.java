package cma.cimiss2.dpc.decoder.tools.enumeration;
/**
 * 
 * <br>
 * @Title:  EIEventType.java   
 * @Package cma.cimiss2.dpc.decoder.tools.vo.enumeration   
 * @Description:    TODO(EI类型枚举)
 * 
 * <pre>
 * SOFTWARE HISTORY
 * Date         Engineer    Description
 * ------------ ----------- --------------------------
 * 2017年12月20日 下午6:24:22   wuzuoqiang    Initial creation.
 * </pre>
 * 
 * @author wuzuoqiang
 *
 *
 */
public enum EIEventType {
	OP_FILE_ERROR("OP_DPC_A_1-10-01"),
	DATABASE_CONNECT_ERROR("OP_DPC_A_1-10-02"),
	RABBIT_CONNECT_ERROR("OP_DPC_A_1-10-03"),
	CONFIG_FILE_ERROR("OP_DPC_A_1-10-04"),
	FILE_DECODE_ERROR("OP_DPC_A_1-10-05"),
	KAFKA_CONNECT_ERROR("OP_DPC_A_1-10-06");

	
	private String code;
	
	EIEventType(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public EIEventType getEIEventTypeByCode(String code) {
		for (EIEventType c : EIEventType.values()) {
			if (c.getCode().equals(code)) {
				return c;
			}
		}
		throw new IllegalArgumentException();
	}

}
