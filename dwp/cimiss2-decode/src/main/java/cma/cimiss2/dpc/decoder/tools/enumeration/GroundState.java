package cma.cimiss2.dpc.decoder.tools.enumeration;

public enum GroundState {
	S0(0, "地表干燥（无裂缝，无明显尘土或散沙）"),
	S1(1, "地表潮湿"),
	S2(2, "地表积水（地面上小的或大的洼中有积水）"),
	S3(3, "被水淹"),
	S4(4, "地表冻结"),
	S5(5, "地面有雨淞"),
	S6(6, "散沙或干的尘土没有完全覆盖地面"),
	S7(7, "一层薄的散沙或干的尘土覆盖整个地面"),
	S8(8, "一层中等或较大厚度的散沙或干的尘土覆盖整个地面"),
	S9(9, "极干燥并出现裂缝"),
	S10(10, "地面主要由冰覆盖"),
	S11(11, "密实雪或湿雪（有或无冰）覆盖不足地面的一半"),
	S12(12, "密实雪或湿雪（有或无水）覆盖地面一半以上，但还未及整个地面"),
	S13(13, "均匀密实雪或湿雪层完全覆盖地面"),
	S14(14, "非均匀密实雪或湿雪层完全覆盖地面"),
	S15(15, "松散的干雪覆盖不足地面的一半"),
	S16(16, "松散的干雪覆盖地面一半以上，但未覆盖整个地面"),
	S17(17, "均匀的松散干雪层覆盖整个地面"),
	S18(18, "非均匀的松散干雪层覆盖整个地面"),
	S19(19, "雪覆盖全部地面；深雪堆"),
	S999999(999999, "缺测值");
	
	private int code;
	private String description;
	
	GroundState(int code, String description) {
		this.code = code;
		this.description = description;
	}
	
	public GroundState getGroundStateByCode(int code) {
		for (GroundState g : GroundState.values()) {
			if (g.getCode() == code) {
				return g;
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
