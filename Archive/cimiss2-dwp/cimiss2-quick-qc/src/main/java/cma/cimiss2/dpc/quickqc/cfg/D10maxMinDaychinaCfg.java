package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class D10maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private static D10maxMinDaychinaCfg d10maxMinDaychinaCfg;
	private Map<String, MaxMinDaychinaInfo> d10maxMinDaychinaInfoMaps;
	private D10maxMinDaychinaCfg() {
		this.d10maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/D10maxMinDaychina.txt");
	}
	
	public static D10maxMinDaychinaCfg getD05maxMinDaychinaCfg() {
		if(d10maxMinDaychinaCfg == null) {
			d10maxMinDaychinaCfg = new D10maxMinDaychinaCfg();
		}
		return d10maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getD05maxMinDaychinaInfoMaps() {
		return d10maxMinDaychinaInfoMaps;
	}
	
	
	
}
