package com.nmpiesat.idata.products.entity;

import java.util.Date;


/**
 * 产品库文件日志
 */
public class ProductLog {

    private String id;
    private String wordId;
    private String ip;
    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ProductLog(String id, String wordId, String ip, Date date) {
        this.id = id;
        this.wordId = wordId;
        this.ip = ip;
        this.date = date;
    }

    public ProductLog(){};
}
