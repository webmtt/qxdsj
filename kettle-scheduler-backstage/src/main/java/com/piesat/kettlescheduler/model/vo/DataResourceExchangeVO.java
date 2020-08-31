package com.piesat.kettlescheduler.model.vo;

/**
 * @title: kettle-scheduler->DataResourceExchangeVO
 * @description: 数据资源交换情况（统计值）
 * @author: YuWenjie
 * @date: 2020-05-06 11:14
 **/

public class DataResourceExchangeVO {
    /********************* 文件 *********************/
    /**
     * 文件-今日推送（个）
     **/
    private Long pushFileToday;
    /**
     * 文件-今日接收（个）
     **/
    private Long gainFileToday;
    /**
     * 文件-累计推送（个）
     **/
    private Long pushFileAll;
    /**
     * 文件-累计接收（个）
     **/
    private Long gainFileAll;

    /********************* 库表 *********************/
    /**
     * 库表-今日推送（条）
     **/
    private Long pushTableToday;
    /**
     * 库表-今日接收（条）
     **/
    private Long gainTableToday;
    /**
     * 库表-累计推送（条）
     **/
    private Long pushTableAll;
    /**
     * 库表-累计接收（条）
     **/
    private Long gainTableAll;

    /********************* 接口 *********************/
    /**
     * 接口-今日推送（次）
     **/
    private Long pushInterfaceToday;
    /**
     * 接口-今日接收（次）
     **/
    private Long gainInterfaceToday;
    /**
     * 接口-累计推送（次）
     **/
    private Long pushInterfaceAll;
    /**
     * 接口-累计接收（次）
     **/
    private Long gainInterfaceAll;

    public Long getPushFileToday() {
        return pushFileToday;
    }

    public void setPushFileToday(Long pushFileToday) {
        this.pushFileToday = pushFileToday;
    }

    public Long getGainFileToday() {
        return gainFileToday;
    }

    public void setGainFileToday(Long gainFileToday) {
        this.gainFileToday = gainFileToday;
    }

    public Long getPushFileAll() {
        return pushFileAll;
    }

    public void setPushFileAll(Long pushFileAll) {
        this.pushFileAll = pushFileAll;
    }

    public Long getGainFileAll() {
        return gainFileAll;
    }

    public void setGainFileAll(Long gainFileAll) {
        this.gainFileAll = gainFileAll;
    }

    public Long getPushTableToday() {
        return pushTableToday;
    }

    public void setPushTableToday(Long pushTableToday) {
        this.pushTableToday = pushTableToday;
    }

    public Long getGainTableToday() {
        return gainTableToday;
    }

    public void setGainTableToday(Long gainTableToday) {
        this.gainTableToday = gainTableToday;
    }

    public Long getPushTableAll() {
        return pushTableAll;
    }

    public void setPushTableAll(Long pushTableAll) {
        this.pushTableAll = pushTableAll;
    }

    public Long getGainTableAll() {
        return gainTableAll;
    }

    public void setGainTableAll(Long gainTableAll) {
        this.gainTableAll = gainTableAll;
    }

    public Long getPushInterfaceToday() {
        return pushInterfaceToday;
    }

    public void setPushInterfaceToday(Long pushInterfaceToday) {
        this.pushInterfaceToday = pushInterfaceToday;
    }

    public Long getGainInterfaceToday() {
        return gainInterfaceToday;
    }

    public void setGainInterfaceToday(Long gainInterfaceToday) {
        this.gainInterfaceToday = gainInterfaceToday;
    }

    public Long getPushInterfaceAll() {
        return pushInterfaceAll;
    }

    public void setPushInterfaceAll(Long pushInterfaceAll) {
        this.pushInterfaceAll = pushInterfaceAll;
    }

    public Long getGainInterfaceAll() {
        return gainInterfaceAll;
    }

    public void setGainInterfaceAll(Long gainInterfaceAll) {
        this.gainInterfaceAll = gainInterfaceAll;
    }

    @Override
    public String toString() {
        return "DataResourceExchangeVO{" +
                "pushFileToday=" + pushFileToday +
                ", gainFileToday=" + gainFileToday +
                ", pushFileAll=" + pushFileAll +
                ", gainFileAll=" + gainFileAll +
                ", pushTableToday=" + pushTableToday +
                ", gainTableToday=" + gainTableToday +
                ", pushTableAll=" + pushTableAll +
                ", gainTableAll=" + gainTableAll +
                ", pushInterfaceToday=" + pushInterfaceToday +
                ", gainInterfaceToday=" + gainInterfaceToday +
                ", pushInterfaceAll=" + pushInterfaceAll +
                ", gainInterfaceAll=" + gainInterfaceAll +
                '}';
    }
}
