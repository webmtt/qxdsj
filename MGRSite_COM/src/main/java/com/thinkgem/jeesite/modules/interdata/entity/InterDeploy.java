package com.thinkgem.jeesite.modules.interdata.entity;


import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;

import java.util.Date;

/**
 * API接口授权实体类
 * @author zhaoxiaojun
 * @version 2020-05-06
 */
public class InterDeploy extends DataEntity<InterDeploy> {

    private String id;
    private String dataClassId;
    private String otherID;
    private String data;
    private String apiList;
    private String dataRoleId;
    private Date times;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getDataClassId() {
        return dataClassId;
    }

    public void setDataClassId(String dataClassId) {
        this.dataClassId = dataClassId;
    }

    public String getOtherID() {
        return otherID;
    }

    public void setOtherID(String otherID) {
        this.otherID = otherID;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getApiList() {
        return apiList;
    }

    public void setApiList(String apiList) {
        this.apiList = apiList;
    }

    public String getDataRoleId() {
        return dataRoleId;
    }

    public void setDataRoleId(String dataRoleId) {
        this.dataRoleId = dataRoleId;
    }

    public Date getTimes() {
        return times;
    }

    public void setTimes(Date times) {
        this.times = times;
    }
}
