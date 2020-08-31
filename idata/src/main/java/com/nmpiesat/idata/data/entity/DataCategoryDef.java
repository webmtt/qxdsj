package com.nmpiesat.idata.data.entity;

import java.io.Serializable;
import java.util.List;

public class DataCategoryDef implements Serializable {
    private static final long serialVersionUID = 1L;
    private int categoryid;
    private String chnname;
    private String shortchnname;
    private String engname;
    private String shortengname;
    private String chndescription;
    private String engdescription;
    private String imageurl;
    private String timeseq;//时间序列
    private String imagechntitle;
    private String imageengtitle;
    private Integer categorylayer;
    private int parentid;
    private Integer orderno;
    private Integer invalid;
    private Integer showtype;
    private String templatefile;
    private String linkurl;
    private Integer datacount;
    private Integer showuserrankid;
    private String iconurl;
    private String largeiconurl;
    private String middleiconurl;

    private List<DataCategoryDef> list;

    public String getTimeseq() {
        return timeseq;
    }

    public void setTimeseq(String timeseq) {
        this.timeseq = timeseq;
    }

    public int getCategoryid() {
        return categoryid;
    }
    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
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
    public String getImageurl() {
        return imageurl;
    }
    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
    public String getImagechntitle() {
        return imagechntitle;
    }
    public void setImagechntitle(String imagechntitle) {
        this.imagechntitle = imagechntitle;
    }
    public String getImageengtitle() {
        return imageengtitle;
    }
    public void setImageengtitle(String imageengtitle) {
        this.imageengtitle = imageengtitle;
    }
    public Integer getCategorylayer() {
        return categorylayer;
    }
    public void setCategorylayer(Integer categorylayer) {
        this.categorylayer = categorylayer;
    }
    public int getParentid() {
        return parentid;
    }
    public void setParentid(int parentid) {
        this.parentid = parentid;
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
    public Integer getShowtype() {
        return showtype;
    }
    public void setShowtype(Integer showtype) {
        this.showtype = showtype;
    }
    public String getTemplatefile() {
        return templatefile;
    }
    public void setTemplatefile(String templatefile) {
        this.templatefile = templatefile;
    }
    public String getLinkurl() {
        return linkurl;
    }
    public void setLinkurl(String linkurl) {
        this.linkurl = linkurl;
    }
    public Integer getDatacount() {
        return datacount;
    }
    public void setDatacount(Integer datacount) {
        this.datacount = datacount;
    }
    public Integer getShowuserrankid() {
        return showuserrankid;
    }
    public void setShowuserrankid(Integer showuserrankid) {
        this.showuserrankid = showuserrankid;
    }
    public List<DataCategoryDef> getList() {
        return list;
    }
    public void setList(List<DataCategoryDef> list) {
        this.list = list;
    }
    public String getIconurl() {
        return iconurl;
    }
    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }
    public String getLargeiconurl() {
        return largeiconurl;
    }
    public void setLargeiconurl(String largeiconurl) {
        this.largeiconurl = largeiconurl;
    }

    public String getMiddleiconurl() {
        return middleiconurl;
    }
    public void setMiddleiconurl(String middleiconurl) {
        this.middleiconurl = middleiconurl;
    }
    @Override
    public String toString() {
        return "DataCategoryDef [categoryid=" + categoryid + ", chnname="
                + chnname + ", shortchnname=" + shortchnname + ", engname="
                + engname + ", shortengname=" + shortengname
                + ", chndescription=" + chndescription + ", engdescription="
                + engdescription + ", imageurl=" + imageurl
                + ", imagechntitle=" + imagechntitle + ", imageengtitle="
                + imageengtitle + ", categorylayer=" + categorylayer
                + ", parentid=" + parentid + ", orderno=" + orderno
                + ", invalid=" + invalid + ", showtype=" + showtype
                + ", templatefile=" + templatefile + ", linkurl=" + linkurl
                + ", datacount=" + datacount + ", showuserrankid="
                + showuserrankid + ", iconurl=" + iconurl + ", largeiconurl="
                + largeiconurl + ", list=" + list + "]";
    }
}
