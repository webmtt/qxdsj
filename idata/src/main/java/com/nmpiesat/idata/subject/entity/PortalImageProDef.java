package com.nmpiesat.idata.subject.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/2/27
 */
public class PortalImageProDef implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private int isStatic;
    private String productCode;
    private String dataCode;
    private String imageurl;
    private String linkurl;
    private String dataSourse;
    private int showType;
    private Date created;
    private String interFaceId;
    private String conditions;
    private int isPlay;
    private String elements;
    private int defaultTime;
    private int defaultCount;
    private String tableName;
    public String getInterFaceId() {
        return interFaceId;
    }
    public void setInterFaceId(String interFaceId) {
        this.interFaceId = interFaceId;
    }
    public String getConditions() {
        return conditions;
    }
    public void setConditions(String conditions) {
        this.conditions = conditions;
    }
    public int getIsPlay() {
        return isPlay;
    }
    public void setIsPlay(int isPlay) {
        this.isPlay = isPlay;
    }
    public String getElements() {
        return elements;
    }
    public void setElements(String elements) {
        this.elements = elements;
    }
    public int getDefaultTime() {
        return defaultTime;
    }
    public void setDefaultTime(int defaultTime) {
        this.defaultTime = defaultTime;
    }
    public int getDefaultCount() {
        return defaultCount;
    }
    public void setDefaultCount(int defaultCount) {
        this.defaultCount = defaultCount;
    }
    public String getTableName() {
        return tableName;
    }
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getProductCode() {
        return productCode;
    }
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    public String getDataCode() {
        return dataCode;
    }
    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }
    public String getDataSourse() {
        return dataSourse;
    }
    public void setDataSourse(String dataSourse) {
        this.dataSourse = dataSourse;
    }
    private int orderno;
    private String invalid;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getImageurl() {
        return imageurl;
    }
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
    public String getLinkurl() {
        return linkurl;
    }
    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }
    public int getOrderno() {
        return orderno;
    }
    public void setOrderno(int orderno) {
        this.orderno = orderno;
    }
    public String getInvalid() {
        return invalid;
    }
    public void setInvalid(String invalid) {
        this.invalid = invalid;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getIsStatic() {
        return isStatic;
    }
    public void setIsStatic(int isStatic) {
        this.isStatic = isStatic;
    }
    public int getShowType() {
        return showType;
    }
    public void setShowType(int showType) {
        this.showType = showType;
    }
    public Date getCreated() {
        return created;
    }
    public void setCreated(Date created) {
        this.created = created;
    }
}
