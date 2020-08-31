package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;


public class TgmaxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private Map<String, MaxMinDaychinaInfo> tgmaxMinDaychinaInfoMaps;
	private static TgmaxMinDaychinaCfg tgmaxMinDaychinaCfg;
	
	private TgmaxMinDaychinaCfg() {
		this.tgmaxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/TgmaxMinDaychina.txt");
	}
	
	public static TgmaxMinDaychinaCfg geTgmaxMinDaychinaCfg() {
		if(tgmaxMinDaychinaCfg == null) {
			tgmaxMinDaychinaCfg = new TgmaxMinDaychinaCfg();
		}
		return tgmaxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getTgmaxMinDaychinaInfoMaps() {
		return tgmaxMinDaychinaInfoMaps;
	}
	

}
