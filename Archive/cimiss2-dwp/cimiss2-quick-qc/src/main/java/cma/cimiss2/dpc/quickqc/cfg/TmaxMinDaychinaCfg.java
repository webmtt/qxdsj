package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class TmaxMinDaychinaCfg extends MaxMinDaychinaCfg {
	private Map<String, MaxMinDaychinaInfo> tmaxMinDaychinaInfoMaps;
	
	private static TmaxMinDaychinaCfg tmaxMinDaychinaCfg;
	
	private TmaxMinDaychinaCfg() {
		this.tmaxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/TmaxMinDaychina.txt");
	}

	public static TmaxMinDaychinaCfg getTmaxMinDaychinaCfg() {
		if(tmaxMinDaychinaCfg == null) {
			tmaxMinDaychinaCfg = new TmaxMinDaychinaCfg();
		}
		
		return tmaxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getTmaxMinDaychinaInfoMaps() {
		return tmaxMinDaychinaInfoMaps;
	}
	
	
	

}
