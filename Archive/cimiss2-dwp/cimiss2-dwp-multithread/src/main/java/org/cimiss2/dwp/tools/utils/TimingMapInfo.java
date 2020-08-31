package org.cimiss2.dwp.tools.utils;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimingMapInfo {
	
	public static void  RefreshMapInfo() {
        ScheduledExecutorService task = Executors.newSingleThreadScheduledExecutor();
        task.scheduleAtFixedRate(StationInfo::setProMap, 0, 1, TimeUnit.DAYS);
	}

}
