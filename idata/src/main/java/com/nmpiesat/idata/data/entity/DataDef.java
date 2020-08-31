package com.nmpiesat.idata.data.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class DataDef implements Serializable {
    private static final long serialVersionUID = 1L;
    private String datacode;
    private String cdatacode;
    private String udatacode;
    private String dataclasscode;
    private String chnname;
    private String churl;
    private String shortchnname;
    private String engname;
    private String shortengname;
    private String chndescription;
    private String engdescription;
    private String keywords;
    private String updatefreq;
    private String spatialresolution;
    private String refersystem;
    private String producetime;
    private String areascope;
    private String westlon;
    private String eastlon;
    private String northlat;
    private String southlat;
    private String databegintime;
    private String dataendtime;
    private String obsfreq;
    private Integer dataendflag;
    private String producer;
    private String productionunit;
    private String contactinfo;
    private Integer storagetype;
    private String searchurl;
    private Integer insertmode;
    private Integer servicemode;
    private String datasourcecode;
    private Integer isincludesub;
    private Integer userrankid;
    private Integer orderno;
    private Integer invalid;
    private String imageurl;
    private String verticallowest;
    private String verticalhighest;
    private String verticalunit;
    private String verticalbase;
    private String optionmetagroup;
    private String publishtime;
    private Integer ftpaccesstype;
    private Integer isoffline;
    private String offlininfo;
    private Integer timezone;

    public String getDatacode() {
        return datacode;
    }
    public void setDatacode(String datacode) {
        this.datacode = datacode;
    }
    public String getCdatacode() {
        return cdatacode;
    }
    public void setCdatacode(String cdatacode) {
        this.cdatacode = cdatacode;
    }
    public String getUdatacode() {
        return udatacode;
    }
    public void setUdatacode(String udatacode) {
        this.udatacode = udatacode;
    }
    public String getDataclasscode() {
        return dataclasscode;
    }
    public void setDataclasscode(String dataclasscode) {
        this.dataclasscode = dataclasscode;
    }
    public String getChnname() {
        return chnname;
    }
    public void setChnname(String chnname) {
        this.chnname = chnname;
    }
    public String getShortchnname() {
        return shortchnname;
    }
    public void setShortchnname(String shortchnname) {
        this.shortchnname = shortchnname;
    }
    public String getEngname() {
        return engname;
    }
    public void setEngname(String engname) {
        this.engname = engname;
    }
    public String getShortengname() {
        return shortengname;
    }
    public void setShortengname(String shortengname) {
        this.shortengname = shortengname;
    }
    public String getChndescription() {
        return chndescription;
    }
    public void setChndescription(String chndescription) {
        this.chndescription = chndescription;
    }
    public String getEngdescription() {
        return engdescription;
    }
    public void setEngdescription(String engdescription) {
        this.engdescription = engdescription;
    }
    public String getKeywords() {
        return keywords;
    }
    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
    public String getUpdatefreq() {
        return updatefreq;
    }
    public void setUpdatefreq(String updatefreq) {
        this.updatefreq = updatefreq;
    }
    public String getSpatialresolution() {
        return spatialresolution;
    }
    public void setSpatialresolution(String spatialresolution) {
        this.spatialresolution = spatialresolution;
    }
    public String getRefersystem() {
        return refersystem;
    }
    public void setRefersystem(String refersystem) {
        this.refersystem = refersystem;
    }
    public String getProducetime() {
        return producetime;
    }
    public void setProducetime(String producetime) {
        this.producetime = producetime;
    }
    public String getAreascope() {
        return areascope;
    }
    public void setAreascope(String areascope) {
        this.areascope = areascope;
    }
    public String getWestlon() {
        return westlon;
    }
    public void setWestlon(String westlon) {
        this.westlon = westlon;
    }
    public String getEastlon() {
        return eastlon;
    }
    public void setEastlon(String eastlon) {
        this.eastlon = eastlon;
    }
    public String getNorthlat() {
        return northlat;
    }
    public void setNorthlat(String northlat) {
        this.northlat = northlat;
    }
    public String getSouthlat() {
        return southlat;
    }
    public void setSouthlat(String southlat) {
        this.southlat = southlat;
    }
    public String getDatabegintime() {
        return databegintime;
    }
    public void setDatabegintime(String databegintime) {
        this.databegintime = databegintime;
    }
    public String getDataendtime() {
        return dataendtime;
    }
    public void setDataendtime(String dataendtime) {
        this.dataendtime = dataendtime;
    }
    public Integer getDataendflag() {
        return dataendflag;
    }
    public void setDataendflag(Integer dataendflag) {
        this.dataendflag = dataendflag;
    }
    public String getProducer() {
        return producer;
    }
    public void setProducer(String producer) {
        this.producer = producer;
    }
    public String getProductionunit() {
        return productionunit;
    }
    public void setProductionunit(String productionunit) {
        this.productionunit = productionunit;
    }
    public String getContactinfo() {
        return contactinfo;
    }
    public void setContactinfo(String contactinfo) {
        this.contactinfo = contactinfo;
    }
    public Integer getStoragetype() {
        return storagetype;
    }
    public void setStoragetype(Integer storagetype) {
        this.storagetype = storagetype;
    }
    public String getSearchurl() {
        return searchurl;
    }
    public void setSearchurl(String searchurl) {
        this.searchurl = searchurl;
    }
    public Integer getInsertmode() {
        return insertmode;
    }
    public void setInsertmode(Integer insertmode) {
        this.insertmode = insertmode;
    }
    public Integer getServicemode() {
        return servicemode;
    }
    public void setServicemode(Integer servicemode) {
        this.servicemode = servicemode;
    }
    public String getDatasourcecode() {
        return datasourcecode;
    }
    public void setDatasourcecode(String datasourcecode) {
        this.datasourcecode = datasourcecode;
    }
    public Integer getIsincludesub() {
        return isincludesub;
    }
    public void setIsincludesub(Integer isincludesub) {
        this.isincludesub = isincludesub;
    }
    public Integer getUserrankid() {
        return userrankid;
    }
    public void setUserrankid(Integer userrankid) {
        this.userrankid = userrankid;
    }
    public Integer getOrderno() {
        return orderno;
    }
    public void setOrderno(Integer orderno) {
        this.orderno = orderno;
    }
    public Integer getInvalid() {
        return invalid;
    }
    public void setInvalid(Integer invalid) {
        this.invalid = invalid;
    }
    public String getImageurl() {
        return imageurl;
    }
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
    public String getObsfreq() {
        return obsfreq;
    }
    public void setObsfreq(String obsfreq) {
        this.obsfreq = obsfreq;
    }

    public String getVerticallowest() {
        return verticallowest;
    }
    public void setVerticallowest(String verticallowest) {
        this.verticallowest = verticallowest;
    }
    public String getVerticalhighest() {
        return verticalhighest;
    }
    public void setVerticalhighest(String verticalhighest) {
        this.verticalhighest = verticalhighest;
    }
    public String getVerticalunit() {
        return verticalunit;
    }
    public void setVerticalunit(String verticalunit) {
        this.verticalunit = verticalunit;
    }
    public String getVerticalbase() {
        return verticalbase;
    }
    public void setVerticalbase(String verticalbase) {
        this.verticalbase = verticalbase;
    }


    public String getOptionmetagroup() {
        return optionmetagroup;
    }
    public void setOptionmetagroup(String optionmetagroup) {
        this.optionmetagroup = optionmetagroup;
    }

    public String getPublishtime() {
        return publishtime;
    }
    public void setPublishtime(String publishtime) {
        this.publishtime = publishtime;
    }
    public Integer getFtpaccesstype() {
        return ftpaccesstype;
    }
    public void setFtpaccesstype(Integer ftpaccesstype) {
        this.ftpaccesstype = ftpaccesstype;
    }

    public Integer getIsoffline() {
        return isoffline;
    }
    public void setIsoffline(Integer isoffline) {
        this.isoffline = isoffline;
    }
    public String getOfflininfo() {
        return offlininfo;
    }
    public void setOfflininfo(String offlininfo) {
        this.offlininfo = offlininfo;
    }
    public Integer getTimezone() {
        return timezone;
    }
    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "DataDef [datacode=" + datacode + ", cdatacode=" + cdatacode + ", udatacode=" + udatacode
                + ", dataclasscode=" + dataclasscode + ", chnname=" + chnname + ", shortchnname=" + shortchnname
                + ", engname=" + engname + ", shortengname=" + shortengname + ", chndescription=" + chndescription
                + ", engdescription=" + engdescription + ", keywords=" + keywords + ", updatefreq=" + updatefreq
                + ", spatialresolution=" + spatialresolution + ", refersystem=" + refersystem + ", producetime="
                + producetime + ", areascope=" + areascope + ", westlon=" + westlon + ", eastlon=" + eastlon
                + ", northlat=" + northlat + ", southlat=" + southlat + ", databegintime=" + databegintime
                + ", dataendtime=" + dataendtime + ", obsfreq=" + obsfreq + ", dataendflag=" + dataendflag
                + ", producer=" + producer + ", productionunit=" + productionunit + ", contactinfo=" + contactinfo
                + ", storagetype=" + storagetype + ", searchurl=" + searchurl + ", insertmode=" + insertmode
                + ", servicemode=" + servicemode + ", datasourcecode=" + datasourcecode + ", isincludesub="
                + isincludesub + ", userrankid=" + userrankid + ", orderno=" + orderno + ", invalid=" + invalid
                + ", imageurl=" + imageurl + ", verticallowest=" + verticallowest + ", verticalhighest="
                + verticalhighest + ", verticalunit=" + verticalunit + ", verticalbase=" + verticalbase
                + ", optionmetagroup=" + optionmetagroup + ", publishtime=" + publishtime + ", ftpaccesstype="
                + ftpaccesstype + ", isoffline=" + isoffline + ", offlininfo=" + offlininfo + ", timezone=" + timezone
                + "]";
    }






}
