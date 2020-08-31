package com.nmpiesat.idata.products.entity;


import java.io.Serializable;
import java.util.Date;

public class ProductesConfig implements Serializable {

    private String id;
    private String  product;
    private String userid;
    private String delFlag;
    private Date create;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getCreate() {
        return create;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public ProductesConfig() {
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public ProductesConfig(String id, String product, String userid, String delFlag, Date create, String username, String password, String url, String prodname) {
        this.id = id;
        this.product = product;
        this.userid = userid;
        this.delFlag = delFlag;
        this.create = create;
        this.username = username;
        this.password = password;
        this.url = url;
        this.prodname = prodname;
    }
}
