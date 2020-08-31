package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class D40maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private static D40maxMinDaychinaCfg d40maxMinDaychinaCfg;
	private Map<String, MaxMinDaychinaInfo> d40maxMinDaychinaInfoMaps;
	private D40maxMinDaychinaCfg() {
		this.d40maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/D40maxMinDaychina.txt");
	}
	
	public static D40maxMinDaychinaCfg getD05maxMinDaychinaCfg() {
		if(d40maxMinDaychinaCfg == null) {
			d40maxMinDaychinaCfg = new D40maxMinDaychinaCfg();
		}
		return d40maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getD05maxMinDaychinaInfoMaps() {
		return d40maxMinDaychinaInfoMaps;
	}
	
	
	
}
