package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class D320maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private static D320maxMinDaychinaCfg d320maxMinDaychinaCfg;
	private Map<String, MaxMinDaychinaInfo> d320maxMinDaychinaInfoMaps;
	private D320maxMinDaychinaCfg() {
		this.d320maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/D320maxMinDaychina.txt");
	}
	
	public static D320maxMinDaychinaCfg getD05maxMinDaychinaCfg() {
		if(d320maxMinDaychinaCfg == null) {
			d320maxMinDaychinaCfg = new D320maxMinDaychinaCfg();
		}
		return d320maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getD05maxMinDaychinaInfoMaps() {
		return d320maxMinDaychinaInfoMaps;
	}
	
	
	
}
