package com.piesat.kettlescheduler.model.vo;

/**
 * @title: kettle-scheduler->DataDirectoryExchangeVO
 * @description: 数据目录交换情况（累计值）
 * @author: YuWenjie
 * @date: 2020-04-29 11:38
 **/

public class DataDirectoryExchangeVO {

    /**
     * 推送-文件目录（类）
     **/
    private Long pushFileCount;
    /**
     * 推送-库表目录（类）
     **/
    private Long pushTableCount;
    /**
     * 推送-接口目录（类）
     **/
    private Long pushInterfaceCount;

    /**
     * 获取-文件目录（类）
     **/
    private Long gainFileCount;
    /**
     * 获取-库表目录（类）
     **/
    private Long gainTableCount;
    /**
     * 获取-接口目录（类）
     **/
    private Long gainInterfaceCount;

    public Long getPushFileCount() {
        return pushFileCount;
    }

    public void setPushFileCount(Long pushFileCount) {
        this.pushFileCount = pushFileCount;
    }

    public Long getPushTableCount() {
        return pushTableCount;
    }

    public void setPushTableCount(Long pushTableCount) {
        this.pushTableCount = pushTableCount;
    }

    public Long getPushInterfaceCount() {
        return pushInterfaceCount;
    }

    public void setPushInterfaceCount(Long pushInterfaceCount) {
        this.pushInterfaceCount = pushInterfaceCount;
    }

    public Long getGainFileCount() {
        return gainFileCount;
    }

    public void setGainFileCount(Long gainFileCount) {
        this.gainFileCount = gainFileCount;
    }

    public Long getGainTableCount() {
        return gainTableCount;
    }

    public void setGainTableCount(Long gainTableCount) {
        this.gainTableCount = gainTableCount;
    }

    public Long getGainInterfaceCount() {
        return gainInterfaceCount;
    }

    public void setGainInterfaceCount(Long gainInterfaceCount) {
        this.gainInterfaceCount = gainInterfaceCount;
    }

    @Override
    public String toString() {
        return "DataDirectoryExchangeVO{" +
                "pushFileCount=" + pushFileCount +
                ", pushTableCount=" + pushTableCount +
                ", pushInterfaceCount=" + pushInterfaceCount +
                ", gainFileCount=" + gainFileCount +
                ", gainTableCount=" + gainTableCount +
                ", gainInterfaceCount=" + gainInterfaceCount +
                '}';
    }
}
