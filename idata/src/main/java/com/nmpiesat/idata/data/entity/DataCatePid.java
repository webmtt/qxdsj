package com.nmpiesat.idata.data.entity;

import java.io.Serializable;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/3
 */
public class DataCatePid implements Serializable {
    private static final long serialVersionUID = 1L;
    private int categoryid;//pid

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getCategoryid() {
        return categoryid;
    }
}
