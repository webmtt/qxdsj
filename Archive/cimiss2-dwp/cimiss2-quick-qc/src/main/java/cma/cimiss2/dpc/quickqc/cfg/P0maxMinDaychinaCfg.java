package cma.cimiss2.dpc.quickqc.cfg;
import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMinDaychinaInfo;

/**
* *******************************************************************************************<br>
* <strong> All Rights Reserved By National Meteorological Information Centre (NMIC) </strong><br>
* *******************************************************************************************<br>
* <b>Description:地面质量控制参数文件  气压</b><br> 
* @author wuzuoqiang 
* @version 1.0
* @Note
* <b>ProjectName:</b> cimiss2-dwp-qc
* <br><b>PackageName:</b> cmadaas.dpc.dc.cfg
* <br><b>ClassName:</b> P0maxMinDaychinaCfg
* <br><b>Date:</b> 2019年5月11日 下午2:33:45
 */
public class P0maxMinDaychinaCfg extends MaxMinDaychinaCfg{
	private Map<String, MaxMinDaychinaInfo> p0maxMinDaychinaInfoMaps;
	private static P0maxMinDaychinaCfg p0maxMinDaychinaCfg;
	
	private P0maxMinDaychinaCfg() {
		this.p0maxMinDaychinaInfoMaps = loadMaxMinDaychinaCfg("config/P0maxMinDaychina.txt");
	}
	
	public static P0maxMinDaychinaCfg geTgmaxMinDaychinaCfg() {
		if(p0maxMinDaychinaCfg == null) {
			p0maxMinDaychinaCfg = new P0maxMinDaychinaCfg();
		}
		return p0maxMinDaychinaCfg;
	}

	public Map<String, MaxMinDaychinaInfo> getP0maxMinDaychinaInfoMaps() {
		return p0maxMinDaychinaInfoMaps;
	}
}