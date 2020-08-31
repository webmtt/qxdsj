package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class D05maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private static D05maxMinDaychinaCfg d05maxMinDaychinaCfg;
	private Map<String, MaxMinDaychinaInfo> d05maxMinDaychinaInfoMaps;
	private D05maxMinDaychinaCfg() {
		this.d05maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/D05maxMinDaychina.txt");
	}
	
	public static D05maxMinDaychinaCfg getD05maxMinDaychinaCfg() {
		if(d05maxMinDaychinaCfg == null) {
			d05maxMinDaychinaCfg = new D05maxMinDaychinaCfg();
		}
		return d05maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getD05maxMinDaychinaInfoMaps() {
		return d05maxMinDaychinaInfoMaps;
	}
	
	
	
}
