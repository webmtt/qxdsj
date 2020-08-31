package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class EmaxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private Map<String, MaxMinDaychinaInfo> emaxMinDaychinaInfoMaps;
	private static EmaxMinDaychinaCfg emaxMinDaychinaCfg;
	
	private EmaxMinDaychinaCfg() {
		this.emaxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/EmaxMinDaychina.txt");
	}
	
	public static EmaxMinDaychinaCfg geTgmaxMinDaychinaCfg() {
		if(emaxMinDaychinaCfg == null) {
			emaxMinDaychinaCfg = new EmaxMinDaychinaCfg();
		}
		return emaxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getP0maxMinDaychinaInfoMaps() {
		return emaxMinDaychinaInfoMaps;
	}
}
