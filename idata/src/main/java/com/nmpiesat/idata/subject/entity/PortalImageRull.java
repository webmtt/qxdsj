package com.nmpiesat.idata.subject.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/2/27
 */
public class PortalImageRull implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private Integer tid;
    private Integer orderNo;
    private int invalid;
    private String type;
    private String typeName;
    private Date startTime;
    private String area;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Integer getTid() {
        return tid;
    }
    public void setTid(Integer tid) {
        this.tid = tid;
    }
    public Integer getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
    public int getInvalid() {
        return invalid;
    }
    public void setInvalid(int invalid) {
        this.invalid = invalid;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }


}
