package cma.cimiss2.dpc.decoder.tools.enumeration;

public enum CorrectionIndicator {
	NO_CORRECTION("000"),
	CCA("CCA"),
	CCB("CCB"),
	CCC("CCC"),
	CCD("CCD"),
	CCE("CCE"),
	CCF("CCF"),
	CCG("CCG"), 
	CCH("CCH"),
	CCI("CCI"),
	CCJ("CCJ"),
	CCK("CCK"),
	CCL("CCL"),
	CCM("CCM"),
	CCN("CCN"), 
	CCO("CCO"),
	CCP("CCP"), 
	CCQ("CCQ"),
	CCR("CCR"),
	CCS("CCS"),
	CCT("CCT"),
	CCU("CCU"),
	CCV("CCV"),
	CCW("CCW"),
	CCX("CCX");
	
	private String code;
	
	CorrectionIndicator(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public CorrectionIndicator getCorrectionIndicatorByCode(String code) {
		for (CorrectionIndicator c : CorrectionIndicator.values()) {
			if (c.getCode().equals(code)) {
				return c;
			}
		}
		throw new IllegalArgumentException();
	}
}