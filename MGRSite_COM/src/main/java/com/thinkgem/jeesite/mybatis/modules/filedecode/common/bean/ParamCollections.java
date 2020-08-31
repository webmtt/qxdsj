package com.thinkgem.jeesite.mybatis.modules.filedecode.common.bean;



import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/4/9
 */

public class ParamCollections implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 日值
     */
    private ViewParamDayEntity dayEntity=new ViewParamDayEntity();
    /**
     * key：hours，Value：小时值
     */
    private Map<String, ViewParamsHourEntity> mapHour =
            new ConcurrentHashMap<>();
    private TimeEntity timeEntity=null;

    public ViewParamDayEntity getDayEntity() {
        return dayEntity;
    }

    public void setDayEntity(ViewParamDayEntity dayEntity) {
        this.dayEntity = dayEntity;
    }

    public Map<String, ViewParamsHourEntity> getMapHour() {
        return mapHour;
    }

    public void setMapHour(Map<String, ViewParamsHourEntity> mapHour) {
        this.mapHour = mapHour;
    }

    public TimeEntity getTimeEntity() {
        return timeEntity;
    }

    public void setTimeEntity(TimeEntity timeEntity) {
        this.timeEntity = timeEntity;
    }
}
