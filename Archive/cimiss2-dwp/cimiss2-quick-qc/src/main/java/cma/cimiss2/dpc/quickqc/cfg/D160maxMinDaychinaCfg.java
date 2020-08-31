package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class D160maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private static D160maxMinDaychinaCfg d160maxMinDaychinaCfg;
	private Map<String, MaxMinDaychinaInfo> d160maxMinDaychinaInfoMaps;
	private D160maxMinDaychinaCfg() {
		this.d160maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/D160maxMinDaychina.txt");
	}
	
	public static D160maxMinDaychinaCfg getD05maxMinDaychinaCfg() {
		if(d160maxMinDaychinaCfg == null) {
			d160maxMinDaychinaCfg = new D160maxMinDaychinaCfg();
		}
		return d160maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getD05maxMinDaychinaInfoMaps() {
		return d160maxMinDaychinaInfoMaps;
	}
	
}
