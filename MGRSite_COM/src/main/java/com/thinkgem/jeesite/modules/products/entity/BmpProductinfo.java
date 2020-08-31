package com.thinkgem.jeesite.modules.products.entity;

import com.thinkgem.jeesite.mybatis.common.persistence.DataEntity;
import java.util.Date;

/**
 * bmp_productinfo
 * @author 
 */
public class BmpProductinfo extends DataEntity<BmpProductinfo> {
    private String productsubcode;

    private String productcode;

    private String productname;

    private String shortname;

    private String productlevel;

    private String productdesc;

    private String tokensname;

    private String tokenscode;

    private String unitname;

    private String unitcode;

    private String typename;

    private String typecode;

    private String contentname;

    private String contentcode;

    private String altitudename;

    private String altitudecode;

    private String coverareaname;

    private String coverareacode;

    private String startendtime;

    private String publishattrname;

    private String publishattrcode;

    private String publishfreq;

    /**
     * 产品状态（0:无、1:业务应用、2:已退出）
     */
    private String publishstate;

    private String publishmode;

    private Date created;

    private String createdby;

    private Date updated;

    private String updatedby;

    private Short invalid;

    private String formatname;

    private String formatcode;

    /**
     * 命名规则
     */
    private String namerule;

    /**
     * 栏目Id
     */
    private String columnclassid;

    /**
     * 产品审核员（即后台用户ID，但仅限于产品审核员角色）
     */
    private String verifyerId;

    /**
     * 审核状态（2:未审核，1:审核通过，0:未通过）
     */
    private Short verifystatus;

    /**
     * 审核时间
     */
    private Date verifydate;

    /**
     * 退出时间
     */
    private Date quitdate;

    /**
     * 附属文件
     */
    private String enclosure;

    /**
     * 附件路径
     */
    private String enclosureurl;

    private static final long serialVersionUID = 1L;

    public String getProductsubcode() {
        return productsubcode;
    }

    public void setProductsubcode(String productsubcode) {
        this.productsubcode = productsubcode;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getProductlevel() {
        return productlevel;
    }

    public void setProductlevel(String productlevel) {
        this.productlevel = productlevel;
    }

    public String getProductdesc() {
        return productdesc;
    }

    public void setProductdesc(String productdesc) {
        this.productdesc = productdesc;
    }

    public String getTokensname() {
        return tokensname;
    }

    public void setTokensname(String tokensname) {
        this.tokensname = tokensname;
    }

    public String getTokenscode() {
        return tokenscode;
    }

    public void setTokenscode(String tokenscode) {
        this.tokenscode = tokenscode;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getContentname() {
        return contentname;
    }

    public void setContentname(String contentname) {
        this.contentname = contentname;
    }

    public String getContentcode() {
        return contentcode;
    }

    public void setContentcode(String contentcode) {
        this.contentcode = contentcode;
    }

    public String getAltitudename() {
        return altitudename;
    }

    public void setAltitudename(String altitudename) {
        this.altitudename = altitudename;
    }

    public String getAltitudecode() {
        return altitudecode;
    }

    public void setAltitudecode(String altitudecode) {
        this.altitudecode = altitudecode;
    }

    public String getCoverareaname() {
        return coverareaname;
    }

    public void setCoverareaname(String coverareaname) {
        this.coverareaname = coverareaname;
    }

    public String getCoverareacode() {
        return coverareacode;
    }

    public void setCoverareacode(String coverareacode) {
        this.coverareacode = coverareacode;
    }

    public String getStartendtime() {
        return startendtime;
    }

    public void setStartendtime(String startendtime) {
        this.startendtime = startendtime;
    }

    public String getPublishattrname() {
        return publishattrname;
    }

    public void setPublishattrname(String publishattrname) {
        this.publishattrname = publishattrname;
    }

    public String getPublishattrcode() {
        return publishattrcode;
    }

    public void setPublishattrcode(String publishattrcode) {
        this.publishattrcode = publishattrcode;
    }

    public String getPublishfreq() {
        return publishfreq;
    }

    public void setPublishfreq(String publishfreq) {
        this.publishfreq = publishfreq;
    }

    public String getPublishstate() {
        return publishstate;
    }

    public void setPublishstate(String publishstate) {
        this.publishstate = publishstate;
    }

    public String getPublishmode() {
        return publishmode;
    }

    public void setPublishmode(String publishmode) {
        this.publishmode = publishmode;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public Short getInvalid() {
        return invalid;
    }

    public void setInvalid(Short invalid) {
        this.invalid = invalid;
    }

    public String getFormatname() {
        return formatname;
    }

    public void setFormatname(String formatname) {
        this.formatname = formatname;
    }

    public String getFormatcode() {
        return formatcode;
    }

    public void setFormatcode(String formatcode) {
        this.formatcode = formatcode;
    }

    public String getNamerule() {
        return namerule;
    }

    public void setNamerule(String namerule) {
        this.namerule = namerule;
    }

    public String getColumnclassid() {
        return columnclassid;
    }

    public void setColumnclassid(String columnclassid) {
        this.columnclassid = columnclassid;
    }

    public String getVerifyerId() {
        return verifyerId;
    }

    public void setVerifyerId(String verifyerId) {
        this.verifyerId = verifyerId;
    }

    public Short getVerifystatus() {
        return verifystatus;
    }

    public void setVerifystatus(Short verifystatus) {
        this.verifystatus = verifystatus;
    }

    public Date getVerifydate() {
        return verifydate;
    }

    public void setVerifydate(Date verifydate) {
        this.verifydate = verifydate;
    }

    public Date getQuitdate() {
        return quitdate;
    }

    public void setQuitdate(Date quitdate) {
        this.quitdate = quitdate;
    }

    public String getEnclosure() {
        return enclosure;
    }

    public void setEnclosure(String enclosure) {
        this.enclosure = enclosure;
    }

    public String getEnclosureurl() {
        return enclosureurl;
    }

    public void setEnclosureurl(String enclosureurl) {
        this.enclosureurl = enclosureurl;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BmpProductinfo other = (BmpProductinfo) that;
        return (this.getProductsubcode() == null ? other.getProductsubcode() == null : this.getProductsubcode().equals(other.getProductsubcode()))
            && (this.getProductcode() == null ? other.getProductcode() == null : this.getProductcode().equals(other.getProductcode()))
            && (this.getProductname() == null ? other.getProductname() == null : this.getProductname().equals(other.getProductname()))
            && (this.getShortname() == null ? other.getShortname() == null : this.getShortname().equals(other.getShortname()))
            && (this.getProductlevel() == null ? other.getProductlevel() == null : this.getProductlevel().equals(other.getProductlevel()))
            && (this.getProductdesc() == null ? other.getProductdesc() == null : this.getProductdesc().equals(other.getProductdesc()))
            && (this.getTokensname() == null ? other.getTokensname() == null : this.getTokensname().equals(other.getTokensname()))
            && (this.getTokenscode() == null ? other.getTokenscode() == null : this.getTokenscode().equals(other.getTokenscode()))
            && (this.getUnitname() == null ? other.getUnitname() == null : this.getUnitname().equals(other.getUnitname()))
            && (this.getUnitcode() == null ? other.getUnitcode() == null : this.getUnitcode().equals(other.getUnitcode()))
            && (this.getTypename() == null ? other.getTypename() == null : this.getTypename().equals(other.getTypename()))
            && (this.getTypecode() == null ? other.getTypecode() == null : this.getTypecode().equals(other.getTypecode()))
            && (this.getContentname() == null ? other.getContentname() == null : this.getContentname().equals(other.getContentname()))
            && (this.getContentcode() == null ? other.getContentcode() == null : this.getContentcode().equals(other.getContentcode()))
            && (this.getAltitudename() == null ? other.getAltitudename() == null : this.getAltitudename().equals(other.getAltitudename()))
            && (this.getAltitudecode() == null ? other.getAltitudecode() == null : this.getAltitudecode().equals(other.getAltitudecode()))
            && (this.getCoverareaname() == null ? other.getCoverareaname() == null : this.getCoverareaname().equals(other.getCoverareaname()))
            && (this.getCoverareacode() == null ? other.getCoverareacode() == null : this.getCoverareacode().equals(other.getCoverareacode()))
            && (this.getStartendtime() == null ? other.getStartendtime() == null : this.getStartendtime().equals(other.getStartendtime()))
            && (this.getPublishattrname() == null ? other.getPublishattrname() == null : this.getPublishattrname().equals(other.getPublishattrname()))
            && (this.getPublishattrcode() == null ? other.getPublishattrcode() == null : this.getPublishattrcode().equals(other.getPublishattrcode()))
            && (this.getPublishfreq() == null ? other.getPublishfreq() == null : this.getPublishfreq().equals(other.getPublishfreq()))
            && (this.getPublishstate() == null ? other.getPublishstate() == null : this.getPublishstate().equals(other.getPublishstate()))
            && (this.getPublishmode() == null ? other.getPublishmode() == null : this.getPublishmode().equals(other.getPublishmode()))
            && (this.getCreated() == null ? other.getCreated() == null : this.getCreated().equals(other.getCreated()))
            && (this.getCreatedby() == null ? other.getCreatedby() == null : this.getCreatedby().equals(other.getCreatedby()))
            && (this.getUpdated() == null ? other.getUpdated() == null : this.getUpdated().equals(other.getUpdated()))
            && (this.getUpdatedby() == null ? other.getUpdatedby() == null : this.getUpdatedby().equals(other.getUpdatedby()))
            && (this.getInvalid() == null ? other.getInvalid() == null : this.getInvalid().equals(other.getInvalid()))
            && (this.getFormatname() == null ? other.getFormatname() == null : this.getFormatname().equals(other.getFormatname()))
            && (this.getFormatcode() == null ? other.getFormatcode() == null : this.getFormatcode().equals(other.getFormatcode()))
            && (this.getNamerule() == null ? other.getNamerule() == null : this.getNamerule().equals(other.getNamerule()))
            && (this.getColumnclassid() == null ? other.getColumnclassid() == null : this.getColumnclassid().equals(other.getColumnclassid()))
            && (this.getVerifyerId() == null ? other.getVerifyerId() == null : this.getVerifyerId().equals(other.getVerifyerId()))
            && (this.getVerifystatus() == null ? other.getVerifystatus() == null : this.getVerifystatus().equals(other.getVerifystatus()))
            && (this.getVerifydate() == null ? other.getVerifydate() == null : this.getVerifydate().equals(other.getVerifydate()))
            && (this.getQuitdate() == null ? other.getQuitdate() == null : this.getQuitdate().equals(other.getQuitdate()))
            && (this.getEnclosure() == null ? other.getEnclosure() == null : this.getEnclosure().equals(other.getEnclosure()))
            && (this.getEnclosureurl() == null ? other.getEnclosureurl() == null : this.getEnclosureurl().equals(other.getEnclosureurl()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getProductsubcode() == null) ? 0 : getProductsubcode().hashCode());
        result = prime * result + ((getProductcode() == null) ? 0 : getProductcode().hashCode());
        result = prime * result + ((getProductname() == null) ? 0 : getProductname().hashCode());
        result = prime * result + ((getShortname() == null) ? 0 : getShortname().hashCode());
        result = prime * result + ((getProductlevel() == null) ? 0 : getProductlevel().hashCode());
        result = prime * result + ((getProductdesc() == null) ? 0 : getProductdesc().hashCode());
        result = prime * result + ((getTokensname() == null) ? 0 : getTokensname().hashCode());
        result = prime * result + ((getTokenscode() == null) ? 0 : getTokenscode().hashCode());
        result = prime * result + ((getUnitname() == null) ? 0 : getUnitname().hashCode());
        result = prime * result + ((getUnitcode() == null) ? 0 : getUnitcode().hashCode());
        result = prime * result + ((getTypename() == null) ? 0 : getTypename().hashCode());
        result = prime * result + ((getTypecode() == null) ? 0 : getTypecode().hashCode());
        result = prime * result + ((getContentname() == null) ? 0 : getContentname().hashCode());
        result = prime * result + ((getContentcode() == null) ? 0 : getContentcode().hashCode());
        result = prime * result + ((getAltitudename() == null) ? 0 : getAltitudename().hashCode());
        result = prime * result + ((getAltitudecode() == null) ? 0 : getAltitudecode().hashCode());
        result = prime * result + ((getCoverareaname() == null) ? 0 : getCoverareaname().hashCode());
        result = prime * result + ((getCoverareacode() == null) ? 0 : getCoverareacode().hashCode());
        result = prime * result + ((getStartendtime() == null) ? 0 : getStartendtime().hashCode());
        result = prime * result + ((getPublishattrname() == null) ? 0 : getPublishattrname().hashCode());
        result = prime * result + ((getPublishattrcode() == null) ? 0 : getPublishattrcode().hashCode());
        result = prime * result + ((getPublishfreq() == null) ? 0 : getPublishfreq().hashCode());
        result = prime * result + ((getPublishstate() == null) ? 0 : getPublishstate().hashCode());
        result = prime * result + ((getPublishmode() == null) ? 0 : getPublishmode().hashCode());
        result = prime * result + ((getCreated() == null) ? 0 : getCreated().hashCode());
        result = prime * result + ((getCreatedby() == null) ? 0 : getCreatedby().hashCode());
        result = prime * result + ((getUpdated() == null) ? 0 : getUpdated().hashCode());
        result = prime * result + ((getUpdatedby() == null) ? 0 : getUpdatedby().hashCode());
        result = prime * result + ((getInvalid() == null) ? 0 : getInvalid().hashCode());
        result = prime * result + ((getFormatname() == null) ? 0 : getFormatname().hashCode());
        result = prime * result + ((getFormatcode() == null) ? 0 : getFormatcode().hashCode());
        result = prime * result + ((getNamerule() == null) ? 0 : getNamerule().hashCode());
        result = prime * result + ((getColumnclassid() == null) ? 0 : getColumnclassid().hashCode());
        result = prime * result + ((getVerifyerId() == null) ? 0 : getVerifyerId().hashCode());
        result = prime * result + ((getVerifystatus() == null) ? 0 : getVerifystatus().hashCode());
        result = prime * result + ((getVerifydate() == null) ? 0 : getVerifydate().hashCode());
        result = prime * result + ((getQuitdate() == null) ? 0 : getQuitdate().hashCode());
        result = prime * result + ((getEnclosure() == null) ? 0 : getEnclosure().hashCode());
        result = prime * result + ((getEnclosureurl() == null) ? 0 : getEnclosureurl().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", productsubcode=").append(productsubcode);
        sb.append(", productcode=").append(productcode);
        sb.append(", productname=").append(productname);
        sb.append(", shortname=").append(shortname);
        sb.append(", productlevel=").append(productlevel);
        sb.append(", productdesc=").append(productdesc);
        sb.append(", tokensname=").append(tokensname);
        sb.append(", tokenscode=").append(tokenscode);
        sb.append(", unitname=").append(unitname);
        sb.append(", unitcode=").append(unitcode);
        sb.append(", typename=").append(typename);
        sb.append(", typecode=").append(typecode);
        sb.append(", contentname=").append(contentname);
        sb.append(", contentcode=").append(contentcode);
        sb.append(", altitudename=").append(altitudename);
        sb.append(", altitudecode=").append(altitudecode);
        sb.append(", coverareaname=").append(coverareaname);
        sb.append(", coverareacode=").append(coverareacode);
        sb.append(", startendtime=").append(startendtime);
        sb.append(", publishattrname=").append(publishattrname);
        sb.append(", publishattrcode=").append(publishattrcode);
        sb.append(", publishfreq=").append(publishfreq);
        sb.append(", publishstate=").append(publishstate);
        sb.append(", publishmode=").append(publishmode);
        sb.append(", created=").append(created);
        sb.append(", createdby=").append(createdby);
        sb.append(", updated=").append(updated);
        sb.append(", updatedby=").append(updatedby);
        sb.append(", invalid=").append(invalid);
        sb.append(", formatname=").append(formatname);
        sb.append(", formatcode=").append(formatcode);
        sb.append(", namerule=").append(namerule);
        sb.append(", columnclassid=").append(columnclassid);
        sb.append(", verifyerId=").append(verifyerId);
        sb.append(", verifystatus=").append(verifystatus);
        sb.append(", verifydate=").append(verifydate);
        sb.append(", quitdate=").append(quitdate);
        sb.append(", enclosure=").append(enclosure);
        sb.append(", enclosureurl=").append(enclosureurl);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}