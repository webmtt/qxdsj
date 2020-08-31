package com.thinkgem.jeesite.modules.products.entity;

import java.util.Date;

public class UploadProduct {
    private String id;
    private String url;
    private Date creats;
    private String link;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreats() {
        return creats;
    }

    public void setCreats(Date creats) {
        this.creats = creats;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public UploadProduct(String id, String url, Date creats, String link) {
        this.id = id;
        this.url = url;
        this.creats = creats;
        this.link = link;
    }

    public UploadProduct(){}
}
