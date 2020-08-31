package com.piesat.kettlescheduler.model.vo;

import java.util.List;

/**
 * @title: kettle-scheduler->ResourceDetailsVO
 * @description: 文件资源交换详情
 * @author: YuWenjie
 * @date: 2020-05-07 09:30
 **/
public class ResourceDetailsVO {

    /**
     * 时间轴（年月日）
     **/
    private List<String> dateList;
    /**
     * 推送量
     **/
    private List<Long> pushDataList;
    /**
     * 接收量
     **/
    private List<Long> gainDataList;

    public List<String> getDateList() {
        return dateList;
    }

    public void setDateList(List<String> dateList) {
        this.dateList = dateList;
    }

    public List<Long> getPushDataList() {
        return pushDataList;
    }

    public void setPushDataList(List<Long> pushDataList) {
        this.pushDataList = pushDataList;
    }

    public List<Long> getGainDataList() {
        return gainDataList;
    }

    public void setGainDataList(List<Long> gainDataList) {
        this.gainDataList = gainDataList;
    }

    @Override
    public String toString() {
        return "ResourceDetailsVO{" +
                "dateList=" + dateList +
                ", pushDataList=" + pushDataList +
                ", gainDataList=" + gainDataList +
                '}';
    }
}
