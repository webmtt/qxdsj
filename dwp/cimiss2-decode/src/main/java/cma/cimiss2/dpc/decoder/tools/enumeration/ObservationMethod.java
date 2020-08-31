package cma.cimiss2.dpc.decoder.tools.enumeration;

public enum ObservationMethod {
	MANUAL(1), EQUIPMENT(4);
	
	private int code;
	
	ObservationMethod(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	public ObservationMethod getObservationMethodByCode(int code) {
		for (ObservationMethod o : ObservationMethod.values()) {
			if (o.getCode() == code ) {
				return o;
			}
		}
		throw new IllegalArgumentException();
	}
}