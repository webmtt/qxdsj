package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class D80maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private static D80maxMinDaychinaCfg d80maxMinDaychinaCfg;
	private Map<String, MaxMinDaychinaInfo> d80maxMinDaychinaInfoMaps;
	private D80maxMinDaychinaCfg() {
		this.d80maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/D80maxMinDaychina.txt");
	}
	
	public static D80maxMinDaychinaCfg getD05maxMinDaychinaCfg() {
		if(d80maxMinDaychinaCfg == null) {
			d80maxMinDaychinaCfg = new D80maxMinDaychinaCfg();
		}
		return d80maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getD05maxMinDaychinaInfoMaps() {
		return d80maxMinDaychinaInfoMaps;
	}
	
	
	
}