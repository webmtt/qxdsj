package cma.cimiss2.dpc.quickqc.bean;

import cma.cimiss2.dpc.quickqc.bean.enums.Month;
import cma.cimiss2.dpc.quickqc.util.NumericUtil;

public class SSTime{
	private Month startMonth;
	private Month endMonth;

	public SSTime(String str) {
		String[] itemString = str.split(",");
		if(itemString.length == 2) {
			this.startMonth = Month.getQualityByCode(NumericUtil.isNumeric(itemString[0]) ? Integer.parseInt(itemString[0].trim()) : Integer.MAX_VALUE);
			this.endMonth = Month.getQualityByCode(NumericUtil.isNumeric(itemString[1]) ? Integer.parseInt(itemString[1].trim()) : Integer.MAX_VALUE);
		}else {
			this.startMonth = Month.NAN;
			this.endMonth = Month.NAN;
		}
	}
	
	public Month getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(Month startMonth) {
		this.startMonth = startMonth;
	}
	public Month getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(Month endMonth) {
		this.endMonth = endMonth;
	}
	
	
}
