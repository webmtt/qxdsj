package cma.cimiss2.dpc.quickqc.bean;

import cma.cimiss2.dpc.quickqc.bean.enums.MeteoElement;

public class MeteoElementObsStateInfo {
	private int flag;
	private MeteoElement element;
	private SSTime ssTime;
	
	public MeteoElementObsStateInfo(int flag, MeteoElement element) {
		super();
		this.flag = flag;
		this.element = element;
	}
	
	public MeteoElementObsStateInfo(SSTime ssTime, MeteoElement element) {
		super();
		this.element = element;
		this.ssTime = ssTime;
	}

	public MeteoElementObsStateInfo() {
		
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public MeteoElement getElement() {
		return element;
	}
	public void setElement(MeteoElement element) {
		this.element = element;
	}
	public SSTime getSsTime() {
		return ssTime;
	}
	public void setSsTime(SSTime ssTime) {
		this.ssTime = ssTime;
	}
	
	
}
