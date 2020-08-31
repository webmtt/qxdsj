package cma.cimiss2.dpc.decoder.tools.enumeration;

public enum QualityControl {
	SOFTWARE(1, "软件自动作过质量控制"),
	MANUAL(0, "由人机交互进一步作过质量控制"),
	NO_QC(9, "没有进行任何质量控制");

	private int code;
	private String description;
	
	QualityControl(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public QualityControl getQualityControlByCode(int code) {
		for (QualityControl q : QualityControl.values()) {
			if (q.getCode() == code) {
				return q;
			}
		}
		throw new IllegalArgumentException();
	}
	

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}