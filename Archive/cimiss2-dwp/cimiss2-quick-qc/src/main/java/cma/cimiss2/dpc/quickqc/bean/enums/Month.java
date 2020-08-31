package cma.cimiss2.dpc.quickqc.bean.enums;

public enum Month {
	NULL(0),
	JAN(1),
	FEB(2),
	MAR(3),
	APR(4),
	MAY(5),
	JUN(6),
	JUL(7),
	AUG(8),
	SEP(9),
	OCT(10),
	NOV(11),
	DEC(12),
	NAN(Integer.MAX_VALUE);
	
	private int month;
	private Month(int month) {
		this.month = month;
	}
	
	public static Month getQualityByCode(int month) {
		for (Month q : Month.values()) {
			if (q.getMonth() == month) {
				return q;
			}
		}
		throw new IllegalArgumentException();
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	
}
