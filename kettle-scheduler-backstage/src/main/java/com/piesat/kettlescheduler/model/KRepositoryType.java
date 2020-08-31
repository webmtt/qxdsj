package com.piesat.kettlescheduler.model;

import java.io.Serializable;

/**
 * k_repository_type
 * @author 
 */
public class KRepositoryType implements Serializable {
    private Integer repositoryTypeId;

    private String repositoryTypeCode;

    private String repositoryTypeDes;

    private static final long serialVersionUID = 1L;

    public Integer getRepositoryTypeId() {
        return repositoryTypeId;
    }

    public void setRepositoryTypeId(Integer repositoryTypeId) {
        this.repositoryTypeId = repositoryTypeId;
    }

    public String getRepositoryTypeCode() {
        return repositoryTypeCode;
    }

    public void setRepositoryTypeCode(String repositoryTypeCode) {
        this.repositoryTypeCode = repositoryTypeCode;
    }

    public String getRepositoryTypeDes() {
        return repositoryTypeDes;
    }

    public void setRepositoryTypeDes(String repositoryTypeDes) {
        this.repositoryTypeDes = repositoryTypeDes;
    }

    @Override
    public String toString() {
        return "KRepositoryType{" +
                "repositoryTypeId=" + repositoryTypeId +
                ", repositoryTypeCode='" + repositoryTypeCode + '\'' +
                ", repositoryTypeDes='" + repositoryTypeDes + '\'' +
                '}';
    }
}