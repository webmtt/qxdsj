package com.thinkgem.jeesite.modules.sys.entity;

public class Deploy {

    private String id;//编号
    private String urlType;//访问类型
    private String productType;//产品子类型
    private String serviceType;//业务门类
    private String photoType;//图片展示类型
    private String fileType;//文件展示类型
    private String dataroleId;//角色id
    private String urlAdress;//生成路径

    public String getUrlAdress() {
        return urlAdress;
    }

    public void setUrlAdress(String urlAdress) {
        this.urlAdress = urlAdress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlType() {
        return urlType;
    }

    public void setUrlType(String urlType) {
        this.urlType = urlType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getDataroleId() {
        return dataroleId;
    }

    public void setDataroleId(String dataroleId) {
        this.dataroleId = dataroleId;
    }

    public Deploy(String id, String urlType, String productType, String serviceType, String photoType, String fileType, String dataroleId, String urlAdress) {
        this.id = id;
        this.urlType = urlType;
        this.productType = productType;
        this.serviceType = serviceType;
        this.photoType = photoType;
        this.fileType = fileType;
        this.dataroleId = dataroleId;
        this.urlAdress = urlAdress;
    }

    public Deploy(){}
}
