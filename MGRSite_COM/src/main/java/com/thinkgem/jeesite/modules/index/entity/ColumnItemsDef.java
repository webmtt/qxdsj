package com.thinkgem.jeesite.modules.index.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "SUP_COLUMNITEMSDEF")
//@SequenceGenerator(name="my_seq", sequenceName="SEQ_STORE")
public class ColumnItemsDef {
    private Integer columnItemID;
    private String chnName;
    private String layerDescription;
    private String columnImageURL;
    private String linkURL;
    private Integer showType;
    private Integer orderNo;
    private Integer invalid;
    private String columnType;
    private String areaItem;


    @Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE ,generator="my_seq")
    public Integer getColumnItemID() {
        return columnItemID;
    }

    public void setColumnItemID(Integer columnItemID) {
        this.columnItemID = columnItemID;
    }


    public String getChnName() {
        return chnName;
    }

    public void setChnName(String chnName) {
        this.chnName = chnName;
    }

    public String getLayerDescription() {
        return layerDescription;
    }

    public void setLayerDescription(String layerDescription) {
        this.layerDescription = layerDescription;
    }

    public String getColumnImageURL() {
        return columnImageURL;
    }

    public void setColumnImageURL(String columnImageURL) {
        this.columnImageURL = columnImageURL;
    }

    public String getLinkURL() {
        return linkURL;
    }

    public void setLinkURL(String linkURL) {
        this.linkURL = linkURL;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public Integer getInvalid() {
        return invalid;
    }

    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }

    public String getAreaItem() {
        return areaItem;
    }

    public void setAreaItem(String areaItem) {
        this.areaItem = areaItem;
    }
}
