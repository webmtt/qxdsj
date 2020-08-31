package com.nmpiesat.idata.user.entity;

import java.io.Serializable;

/**
 * @author yang.kq
 * @version 1.0
 * @date 2020/5/8 10:52
 */
public class UserExamInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String email;
    private String code;
    private String reason;
    private String remarks;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}

