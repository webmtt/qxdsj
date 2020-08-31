package com.nmpiesat.idata.products.entity;

import java.util.Date;

public class UploadProduct {
    private String id;
    private String url;
    private String link;
    private Date creats;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getCreats() {
        return creats;
    }

    public void setCreats(Date creats) {
        this.creats = creats;
    }

    public UploadProduct(String id, String url, String link, Date creats) {
        this.id = id;
        this.url = url;
        this.link = link;
        this.creats = creats;
    }

    public UploadProduct(){}
}
