package com.nmpiesat.idata.products.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * pro_dataindex
 * @author 
 */
public class ProDataindex implements Serializable {
    private String productsubcode;

    private String productcode;

    private String tokenscode;

    private String unitcode;

    private String typecode;

    private String contentcode;

    private String altitudecode;

    private String coverareacode;

    private String startendtime;

    private String dDatetime;

    private String filetype;

    private Date prodate;

    private String filename;

    private String filepath;

    private String url;

    private BigDecimal filesize;

    /**
     * 1:删除 2：使用中
     */
    private Short ispublish;

    private String createdby;

    private Date created;

    private String updatedby;

    private Date updated;

    private String stationnum;

    private String id;

    private String fileshowname;

    private BigDecimal hits;

    private static final long serialVersionUID = 1L;

    public BigDecimal getHits() {
        return hits;
    }

    public void setHits(BigDecimal hits) {
        this.hits = hits;
    }

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

    public String getTokenscode() {
        return tokenscode;
    }

    public void setTokenscode(String tokenscode) {
        this.tokenscode = tokenscode;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getContentcode() {
        return contentcode;
    }

    public void setContentcode(String contentcode) {
        this.contentcode = contentcode;
    }

    public String getAltitudecode() {
        return altitudecode;
    }

    public void setAltitudecode(String altitudecode) {
        this.altitudecode = altitudecode;
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

    public String getdDatetime() {
        return dDatetime;
    }

    public void setdDatetime(String dDatetime) {
        this.dDatetime = dDatetime;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public Date getProdate() {
        return prodate;
    }

    public void setProdate(Date prodate) {
        this.prodate = prodate;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getFilesize() {
        return filesize;
    }

    public void setFilesize(BigDecimal filesize) {
        this.filesize = filesize;
    }

    public Short getIspublish() {
        return ispublish;
    }

    public void setIspublish(Short ispublish) {
        this.ispublish = ispublish;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getUpdatedby() {
        return updatedby;
    }

    public void setUpdatedby(String updatedby) {
        this.updatedby = updatedby;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getStationnum() {
        return stationnum;
    }

    public void setStationnum(String stationnum) {
        this.stationnum = stationnum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileshowname() {
        return fileshowname;
    }

    public void setFileshowname(String fileshowname) {
        this.fileshowname = fileshowname;
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
        ProDataindex other = (ProDataindex) that;
        return (this.getProductsubcode() == null ? other.getProductsubcode() == null : this.getProductsubcode().equals(other.getProductsubcode()))
            && (this.getProductcode() == null ? other.getProductcode() == null : this.getProductcode().equals(other.getProductcode()))
            && (this.getTokenscode() == null ? other.getTokenscode() == null : this.getTokenscode().equals(other.getTokenscode()))
            && (this.getUnitcode() == null ? other.getUnitcode() == null : this.getUnitcode().equals(other.getUnitcode()))
            && (this.getTypecode() == null ? other.getTypecode() == null : this.getTypecode().equals(other.getTypecode()))
            && (this.getContentcode() == null ? other.getContentcode() == null : this.getContentcode().equals(other.getContentcode()))
            && (this.getAltitudecode() == null ? other.getAltitudecode() == null : this.getAltitudecode().equals(other.getAltitudecode()))
            && (this.getCoverareacode() == null ? other.getCoverareacode() == null : this.getCoverareacode().equals(other.getCoverareacode()))
            && (this.getStartendtime() == null ? other.getStartendtime() == null : this.getStartendtime().equals(other.getStartendtime()))
            && (this.getdDatetime() == null ? other.getdDatetime() == null : this.getdDatetime().equals(other.getdDatetime()))
            && (this.getFiletype() == null ? other.getFiletype() == null : this.getFiletype().equals(other.getFiletype()))
            && (this.getProdate() == null ? other.getProdate() == null : this.getProdate().equals(other.getProdate()))
            && (this.getFilename() == null ? other.getFilename() == null : this.getFilename().equals(other.getFilename()))
            && (this.getFilepath() == null ? other.getFilepath() == null : this.getFilepath().equals(other.getFilepath()))
            && (this.getUrl() == null ? other.getUrl() == null : this.getUrl().equals(other.getUrl()))
            && (this.getFilesize() == null ? other.getFilesize() == null : this.getFilesize().equals(other.getFilesize()))
            && (this.getIspublish() == null ? other.getIspublish() == null : this.getIspublish().equals(other.getIspublish()))
            && (this.getCreatedby() == null ? other.getCreatedby() == null : this.getCreatedby().equals(other.getCreatedby()))
            && (this.getCreated() == null ? other.getCreated() == null : this.getCreated().equals(other.getCreated()))
            && (this.getUpdatedby() == null ? other.getUpdatedby() == null : this.getUpdatedby().equals(other.getUpdatedby()))
            && (this.getUpdated() == null ? other.getUpdated() == null : this.getUpdated().equals(other.getUpdated()))
            && (this.getStationnum() == null ? other.getStationnum() == null : this.getStationnum().equals(other.getStationnum()))
            && (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getFileshowname() == null ? other.getFileshowname() == null : this.getFileshowname().equals(other.getFileshowname()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getProductsubcode() == null) ? 0 : getProductsubcode().hashCode());
        result = prime * result + ((getProductcode() == null) ? 0 : getProductcode().hashCode());
        result = prime * result + ((getTokenscode() == null) ? 0 : getTokenscode().hashCode());
        result = prime * result + ((getUnitcode() == null) ? 0 : getUnitcode().hashCode());
        result = prime * result + ((getTypecode() == null) ? 0 : getTypecode().hashCode());
        result = prime * result + ((getContentcode() == null) ? 0 : getContentcode().hashCode());
        result = prime * result + ((getAltitudecode() == null) ? 0 : getAltitudecode().hashCode());
        result = prime * result + ((getCoverareacode() == null) ? 0 : getCoverareacode().hashCode());
        result = prime * result + ((getStartendtime() == null) ? 0 : getStartendtime().hashCode());
        result = prime * result + ((getdDatetime() == null) ? 0 : getdDatetime().hashCode());
        result = prime * result + ((getFiletype() == null) ? 0 : getFiletype().hashCode());
        result = prime * result + ((getProdate() == null) ? 0 : getProdate().hashCode());
        result = prime * result + ((getFilename() == null) ? 0 : getFilename().hashCode());
        result = prime * result + ((getFilepath() == null) ? 0 : getFilepath().hashCode());
        result = prime * result + ((getUrl() == null) ? 0 : getUrl().hashCode());
        result = prime * result + ((getFilesize() == null) ? 0 : getFilesize().hashCode());
        result = prime * result + ((getIspublish() == null) ? 0 : getIspublish().hashCode());
        result = prime * result + ((getCreatedby() == null) ? 0 : getCreatedby().hashCode());
        result = prime * result + ((getCreated() == null) ? 0 : getCreated().hashCode());
        result = prime * result + ((getUpdatedby() == null) ? 0 : getUpdatedby().hashCode());
        result = prime * result + ((getUpdated() == null) ? 0 : getUpdated().hashCode());
        result = prime * result + ((getStationnum() == null) ? 0 : getStationnum().hashCode());
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getFileshowname() == null) ? 0 : getFileshowname().hashCode());
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
        sb.append(", tokenscode=").append(tokenscode);
        sb.append(", unitcode=").append(unitcode);
        sb.append(", typecode=").append(typecode);
        sb.append(", contentcode=").append(contentcode);
        sb.append(", altitudecode=").append(altitudecode);
        sb.append(", coverareacode=").append(coverareacode);
        sb.append(", startendtime=").append(startendtime);
        sb.append(", dDatetime=").append(dDatetime);
        sb.append(", filetype=").append(filetype);
        sb.append(", prodate=").append(prodate);
        sb.append(", filename=").append(filename);
        sb.append(", filepath=").append(filepath);
        sb.append(", url=").append(url);
        sb.append(", filesize=").append(filesize);
        sb.append(", ispublish=").append(ispublish);
        sb.append(", createdby=").append(createdby);
        sb.append(", created=").append(created);
        sb.append(", updatedby=").append(updatedby);
        sb.append(", updated=").append(updated);
        sb.append(", stationnum=").append(stationnum);
        sb.append(", id=").append(id);
        sb.append(", fileshowname=").append(fileshowname);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}