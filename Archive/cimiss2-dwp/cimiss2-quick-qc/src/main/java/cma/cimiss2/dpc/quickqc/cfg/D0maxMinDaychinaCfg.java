package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class D0maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private static D0maxMinDaychinaCfg d0maxMinDaychinaCfg;
	private Map<String, MaxMinDaychinaInfo> d0maxMinDaychinaInfoMaps;
	private D0maxMinDaychinaCfg() {
		this.d0maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/D0maxMinDaychina.txt");
	}

	public static D0maxMinDaychinaCfg getD0maxMinDaychinaCfg() {
		if(d0maxMinDaychinaCfg == null) {
			d0maxMinDaychinaCfg = new D0maxMinDaychinaCfg();
		}
		return d0maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getD0maxMinDaychinaInfoMaps() {
		return d0maxMinDaychinaInfoMaps;
	}
	

}
