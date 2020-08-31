package com.thinkgem.jeesite.mybatis.modules.report.entity;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;

import java.io.Serializable;

/**
 * 查询功能日志信息
 * @author yang.kq
 * @version 1.0
 * @date 2020/7/22 11:10
 */
public class ReportLogInfo  extends DataEntity<ReportLogInfo> implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     *    主键
     */
    private String id;
    /**
     * 区站信息
     */
    private String stationInfo;
    /**
     * 操作类型（1：入库，2：查询，3：下载）
     */
    private String operitorType;
    /**
     * 数据大小
     */
    private String dataNum;
    /**
     * 数据类型（1：定时值，2：日值，3：旬值，4：月值，5：年值）
     */
    private String dataType;
    /**
     * ip
     */
    private String addr;
    /**
     * 更新时间
     */
    private String time;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getStationInfo() {
        return stationInfo;
    }

    public void setStationInfo(String stationInfo) {
        this.stationInfo = stationInfo;
    }

    public String getOperitorType() {
        return operitorType;
    }

    public void setOperitorType(String operitorType) {
        this.operitorType = operitorType;
    }

    public String getDataNum() {
        return dataNum;
    }

    public void setDataNum(String dataNum) {
        this.dataNum = dataNum;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
