package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class D20maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private static D20maxMinDaychinaCfg d20maxMinDaychinaCfg;
	private Map<String, MaxMinDaychinaInfo> d20maxMinDaychinaInfoMaps;
	private D20maxMinDaychinaCfg() {
		this.d20maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/D20maxMinDaychina.txt");
	}
	
	public static D20maxMinDaychinaCfg getD05maxMinDaychinaCfg() {
		if(d20maxMinDaychinaCfg == null) {
			d20maxMinDaychinaCfg = new D20maxMinDaychinaCfg();
		}
		return d20maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getD05maxMinDaychinaInfoMaps() {
		return d20maxMinDaychinaInfoMaps;
	}
	
	
	
}
