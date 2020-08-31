package com.thinkgem.jeesite.modules.products.entity;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;

import java.util.Date;

public class Products extends DataEntity<Products> {

    private String id;
    private String  product;
    /*private String userid;*/
    private Date create;
    private Date beginCreateDate;		// 开始 创建时间
    private Date endCreateDate;		// 结束 创建时间
    private String username;
    private String password;
    private String url;
    private String prodname;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBeginCreateDate() {
        return beginCreateDate;
    }

    public void setBeginCreateDate(Date beginCreateDate) {
        this.beginCreateDate = beginCreateDate;
    }

    public Date getEndCreateDate() {
        return endCreateDate;
    }

    public void setEndCreateDate(Date endCreateDate) {
        this.endCreateDate = endCreateDate;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    /*public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }



}
