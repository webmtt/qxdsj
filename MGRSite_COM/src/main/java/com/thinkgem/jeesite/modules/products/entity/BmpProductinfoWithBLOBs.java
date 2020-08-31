package com.thinkgem.jeesite.modules.products.entity;


/**
 * bmp_productinfo
 * @author 
 */
public class BmpProductinfoWithBLOBs extends BmpProductinfo {
    private String mgruser;

    /**
     * 审核不通过原因
     */
    private String verifyfailreason;

    private static final long serialVersionUID = 1L;

    public String getMgruser() {
        return mgruser;
    }

    public void setMgruser(String mgruser) {
        this.mgruser = mgruser;
    }

    public String getVerifyfailreason() {
        return verifyfailreason;
    }

    public void setVerifyfailreason(String verifyfailreason) {
        this.verifyfailreason = verifyfailreason;
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
        BmpProductinfoWithBLOBs other = (BmpProductinfoWithBLOBs) that;
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
            && (this.getEnclosureurl() == null ? other.getEnclosureurl() == null : this.getEnclosureurl().equals(other.getEnclosureurl()))
            && (this.getMgruser() == null ? other.getMgruser() == null : this.getMgruser().equals(other.getMgruser()))
            && (this.getVerifyfailreason() == null ? other.getVerifyfailreason() == null : this.getVerifyfailreason().equals(other.getVerifyfailreason()));
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
        result = prime * result + ((getMgruser() == null) ? 0 : getMgruser().hashCode());
        result = prime * result + ((getVerifyfailreason() == null) ? 0 : getVerifyfailreason().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", mgruser=").append(mgruser);
        sb.append(", verifyfailreason=").append(verifyfailreason);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}