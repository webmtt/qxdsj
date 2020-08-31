package com.nmpiesat.idata.subject.entity;

import java.sql.Timestamp;

/**
 * 图片数据
 * @author yangkq
 * @version 1.0
 * @date 2020/3/2
 */
public class ProductImgDef {
    private String d_file_id;//文件标识
    private String d_data_id;//资料标识-四级编码
    private String v_file_name;//图片名称
    private String d_storage_site;//图片路径
    private long d_file_size;//图片大小
    private String d_file_format;//图片类型
    private Timestamp d_iymdhm;//入库时间
    private Timestamp d_rymdhm;//创建时间
    private Timestamp d_update_time;// 更新时间
    private Timestamp d_datetime;//产品时间
    private String prodate;//图片日期
    private Integer V04004;//图片时间-时
    private int ispublish;//发布状态，0-未发布，1-发布
    private String v_prod_code;//产品代码
    private String v_ele_kind;//要素种类

    public String getD_file_id() {
        return d_file_id;
    }

    public void setD_file_id(String d_file_id) {
        this.d_file_id = d_file_id;
    }

    public String getD_data_id() {
        return d_data_id;
    }

    public void setD_data_id(String d_data_id) {
        this.d_data_id = d_data_id;
    }

    public String getV_file_name() {
        return v_file_name;
    }

    public void setV_file_name(String v_file_name) {
        this.v_file_name = v_file_name;
    }

    public String getD_storage_site() {
        return d_storage_site;
    }

    public void setD_storage_site(String d_storage_site) {
        this.d_storage_site = d_storage_site;
    }

    public long getD_file_size() {
        return d_file_size;
    }

    public void setD_file_size(long d_file_size) {
        this.d_file_size = d_file_size;
    }

    public String getD_file_format() {
        return d_file_format;
    }

    public void setD_file_format(String d_file_format) {
        this.d_file_format = d_file_format;
    }

    public Timestamp getD_iymdhm() {
        return d_iymdhm;
    }

    public void setD_iymdhm(Timestamp d_iymdhm) {
        this.d_iymdhm = d_iymdhm;
    }

    public Timestamp getD_rymdhm() {
        return d_rymdhm;
    }

    public void setD_rymdhm(Timestamp d_rymdhm) {
        this.d_rymdhm = d_rymdhm;
    }

    public Timestamp getD_update_time() {
        return d_update_time;
    }

    public void setD_update_time(Timestamp d_update_time) {
        this.d_update_time = d_update_time;
    }

    public Timestamp getD_datetime() {
        return d_datetime;
    }

    public void setD_datetime(Timestamp d_datetime) {
        this.d_datetime = d_datetime;
    }

    public String getProdate() {
        return prodate;
    }

    public void setProdate(String prodate) {
        this.prodate = prodate;
    }

    public Integer getV04004() {
        return V04004;
    }

    public void setV04004(Integer v04004) {
        V04004 = v04004;
    }

    public int getIspublish() {
        return ispublish;
    }

    public void setIspublish(int ispublish) {
        this.ispublish = ispublish;
    }

    public String getV_prod_code() {
        return v_prod_code;
    }

    public void setV_prod_code(String v_prod_code) {
        this.v_prod_code = v_prod_code;
    }

    public String getV_ele_kind() {
        return v_ele_kind;
    }

    public void setV_ele_kind(String v_ele_kind) {
        this.v_ele_kind = v_ele_kind;
    }
}