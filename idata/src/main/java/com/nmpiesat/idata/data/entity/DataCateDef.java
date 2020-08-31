package com.nmpiesat.idata.data.entity;

import java.io.Serializable;

/**
 * @author yangkq
 * @version 1.0
 * @date 2020/3/3
 */
public class DataCateDef implements Serializable {
    private static final long serialVersionUID = 1L;
    private String dataCode;//资料代号
    private String chnname;//资料名称
    private int categoryid;//pid

    public void setDataCode(String dataCode) {
        this.dataCode = dataCode;
    }

    public void setChnname(String chnname) {
        this.chnname = chnname;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public String getDataCode() {
        return dataCode;
    }

    public String getChnname() {
        return chnname;
    }

}
