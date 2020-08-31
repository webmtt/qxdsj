package cma.cimiss2.dpc.decoder.tools.enumeration;

public enum CloudType {
	
	CUU(31,"淡积云","Cu hum"),
	FCB(48,"碎积云","Fc"),
	CUO(32,"浓积云","Cu cong"),
	CBV(33,"秃积雨云","Cb calv"),
	CBP(39,"鬃积雨云","Cb cap"),
	SCR(50,"透光层积云","Sc tra"),
	SCP(51,"蔽光层积云","Sc op"),
	SCU(34,"积云性层积云","Sc cug"),
	SCA(52,"堡状层积云","Sc cast"),
	SCT(43,"荚状层积云","Sc lent"),
	STB(7,"层云","St"),
	FSB(37,"碎层云","Fs"),
	NSB(5,"雨层云","Ns"),
	FNB(44,"碎雨云","Fn"),
	ASR(45,"透光高层云","As tra"),
	ASP(46,"蔽光高层云","As op"),
	ACR(21,"透光高积云","Ac tra"),
	ACP(22,"蔽光高积云","Ac op"),
	ACE(24,"荚状高积云","Ac lent"),
	ACU(26,"积云性高积云","Ac cug"),
	ACL(28,"絮状高积云","Ac flo"),
	ACA(49,"堡状高积云","Ac cast"),
	CII(11,"毛卷云","Ci fil"),
	CIE(12,"密卷云","Ci dens"),
	CIO(47,"伪卷云","Ci not"),
	CIN(14,"钩卷云","Ci nuc"),
	CSI(16,"毛卷层云","Cs fil"),
	CSE(15,"薄幕卷层云","Cs nebu"),
	CCB(1,"卷积云","Cc");
	
	private int code;
	private String description;
	private String briefCode;
	
	CloudType(int code, String description, String briefCode) {
		this.code = code;
		this.description = description;
		this.briefCode = briefCode;
	}
	
	public CloudType getCloudTypeByCode(int code) {
		for (CloudType c : CloudType.values()) {
			if (c.getCode() == code) {
				return c;
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
	public String getBriefCode() {
		return briefCode;
	}
	public void setBriefCode(String briefCode) {
		this.briefCode = briefCode;
	}
	
}