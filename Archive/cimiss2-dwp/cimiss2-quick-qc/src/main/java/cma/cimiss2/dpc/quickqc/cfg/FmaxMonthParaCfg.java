package cma.cimiss2.dpc.quickqc.cfg;

import java.util.Map;

import cma.cimiss2.dpc.quickqc.bean.MaxMonthParaInfo;

public class FmaxMonthParaCfg extends MaxMinDaychinaCfg{

    private Map<String, MaxMonthParaInfo> fmaxMonthParaInfoMaps;
    private static FmaxMonthParaCfg fmaxMonthParaCfg;

    private FmaxMonthParaCfg() {
        this.fmaxMonthParaInfoMaps = loadMaxMonthCfg("config/FMaxMonthPara.txt");
    }

    public static FmaxMonthParaCfg getFmaxMonthParaCfg() {
        if(fmaxMonthParaCfg == null) {
            fmaxMonthParaCfg = new FmaxMonthParaCfg();
        }
        return fmaxMonthParaCfg;
    }

    public Map<String, MaxMonthParaInfo> getFmaxMonthParaInfoMaps() {
        return fmaxMonthParaInfoMaps;
    }


}
