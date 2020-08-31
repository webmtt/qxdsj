package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class D15maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private static D15maxMinDaychinaCfg d15maxMinDaychinaCfg;
	private Map<String, MaxMinDaychinaInfo> d15maxMinDaychinaInfoMaps;
	private D15maxMinDaychinaCfg() {
		this.d15maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/D15maxMinDaychina.txt");
	}
	
	public static D15maxMinDaychinaCfg getD05maxMinDaychinaCfg() {
		if(d15maxMinDaychinaCfg == null) {
			d15maxMinDaychinaCfg = new D15maxMinDaychinaCfg();
		}
		return d15maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getD05maxMinDaychinaInfoMaps() {
		return d15maxMinDaychinaInfoMaps;
	}
	
	
}
