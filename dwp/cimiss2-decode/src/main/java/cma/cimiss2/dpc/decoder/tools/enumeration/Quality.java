package cma.cimiss2.dpc.decoder.tools.enumeration;

public enum Quality {
	
	Zero(0, "通过质量控制，未发现数据异常；或数据虽异常，但最终确认数据正确"), 
	One(1, "通过质量控制，发现数据异常，且未明确数据正确还是错误"), 
	Two(2, "通过质量控制，确认数据错误"), 
	Three(3, "原数据明显偏离真值，但在一定范围内可参照使用。在原数据基础上通过偏差订正等方式重新获取的更正数据"), 
	Four(4, "原数据因错误或缺测而完全不可用，通过与原数据完全无关的替代方式重新获取的更正数据"),
	five(5,"临时添加，文档没有，解码出来有"),
	six(6,"临时添加，文档没有，解码出来有"),
	Seven(7, "按规定，台站无相应要素数据观测任务"),
	Eight(8, "该项数据应观测，但因各种原因数据缺测"), 
	Nine(9, "该数据未进行质量控制");

	private int code;
	private String description;

	Quality(int code, String description) {
		this.code = code;
		this.description = description;
	}

	public static Quality getQualityByCode(int code) {
		for (Quality q : Quality.values()) {
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