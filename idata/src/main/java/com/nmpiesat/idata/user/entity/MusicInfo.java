package com.nmpiesat.idata.user.entity;

import java.io.Serializable;

/**
 * music用户信息
 * @author yang.kq
 * @version 1.0
 * @date 2020/3/20 17:14
 */
public class MusicInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name; // 用户名
    private String password; // 用户密码
    private String orgid; // 工作单位

    public MusicInfo() {
        super();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }
}
