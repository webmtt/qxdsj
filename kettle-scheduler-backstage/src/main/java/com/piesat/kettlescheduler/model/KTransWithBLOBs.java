package com.piesat.kettlescheduler.model;

import java.io.Serializable;

/**
 * k_trans
 * @author 
 */
public class KTransWithBLOBs extends KTrans implements Serializable {
    /**
     * 转换描述
     */
    private String transDescription;

    /**
     * 转换保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）
     */
    private String transPath;

    private static final long serialVersionUID = 1L;

    public String getTransDescription() {
        return transDescription;
    }

    public void setTransDescription(String transDescription) {
        this.transDescription = transDescription;
    }

    public String getTransPath() {
        return transPath;
    }

    public void setTransPath(String transPath) {
        this.transPath = transPath;
    }
}