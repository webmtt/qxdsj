package cma.cimiss2.dpc.quickqc.bean;

import java.util.HashMap;
import java.util.Map;

public class MaxMinDaychinaInfo extends BaseStationInfo {
	// map中 key为 日序 value 为最大超阀值、最小超阀值、要素最大值、要素最小值的实体类
	private Map<Integer, MaxMinValues> maxMinValueOfDays;

	public MaxMinDaychinaInfo() {
			maxMinValueOfDays = new HashMap<Integer, MaxMinValues>();
		}

	public Map<Integer, MaxMinValues> getMaxMinValueOfDays() {
		return maxMinValueOfDays;
	}

	public void setMaxMinValueOfDays(Map<Integer, MaxMinValues> maxMinValueOfDays) {
		this.maxMinValueOfDays = maxMinValueOfDays;
	}

	public void put(int key, MaxMinValues maxMinValues) {
		maxMinValueOfDays.put(key, maxMinValues);
	}
}
