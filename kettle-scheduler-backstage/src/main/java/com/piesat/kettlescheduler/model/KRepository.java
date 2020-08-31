package com.piesat.kettlescheduler.model;

import java.io.Serializable;
import java.util.Date;

/**
 * k_repository
 * @author 
 */
public class KRepository implements Serializable {
    /**
     * ID
     */
    private Integer repositoryId;

    /**
     * 资源库名称
     */
    private String repositoryName;

    /**
     * 登录用户名
     */
    private String repositoryUsername;

    /**
     * 登录密码
     */
    private String repositoryPassword;

    /**
     * 资源库数据库类型（MYSQL、ORACLE）
     */
    private String repositoryType;

    /**
     * 资源库数据库访问模式（"Native", "ODBC", "OCI", "Plugin", "JNDI")
     */
    private String databaseAccess;

    /**
     * 资源库数据库主机名或者IP地址
     */
    private String databaseHost;

    /**
     * 资源库数据库端口号
     */
    private String databasePort;

    /**
     * 资源库数据库名称
     */
    private String databaseName;

    /**
     * 数据库登录账号
     */
    private String databaseUsername;

    /**
     * 数据库登录密码
     */
    private String databasePassword;

    /**
     * 添加时间
     */
    private Date addTime;

    /**
     * 添加者
     */
    private Integer addUser;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 编辑者
     */
    private Integer editUser;

    /**
     * 是否删除（1：存在；0：删除）
     */
    private Integer delFlag;

    private static final long serialVersionUID = 1L;

    public Integer getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(Integer repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

    public String getRepositoryUsername() {
        return repositoryUsername;
    }

    public void setRepositoryUsername(String repositoryUsername) {
        this.repositoryUsername = repositoryUsername;
    }

    public String getRepositoryPassword() {
        return repositoryPassword;
    }

    public void setRepositoryPassword(String repositoryPassword) {
        this.repositoryPassword = repositoryPassword;
    }

    public String getRepositoryType() {
        return repositoryType;
    }

    public void setRepositoryType(String repositoryType) {
        this.repositoryType = repositoryType;
    }

    public String getDatabaseAccess() {
        return databaseAccess;
    }

    public void setDatabaseAccess(String databaseAccess) {
        this.databaseAccess = databaseAccess;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(String databasePort) {
        this.databasePort = databasePort;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getAddUser() {
        return addUser;
    }

    public void setAddUser(Integer addUser) {
        this.addUser = addUser;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Integer getEditUser() {
        return editUser;
    }

    public void setEditUser(Integer editUser) {
        this.editUser = editUser;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "KRepository{" +
                "repositoryId=" + repositoryId +
                ", repositoryName='" + repositoryName + '\'' +
                ", repositoryUsername='" + repositoryUsername + '\'' +
                ", repositoryPassword='" + repositoryPassword + '\'' +
                ", repositoryType='" + repositoryType + '\'' +
                ", databaseAccess='" + databaseAccess + '\'' +
                ", databaseHost='" + databaseHost + '\'' +
                ", databasePort='" + databasePort + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", databaseUsername='" + databaseUsername + '\'' +
                ", databasePassword='" + databasePassword + '\'' +
                ", addTime=" + addTime +
                ", addUser=" + addUser +
                ", editTime=" + editTime +
                ", editUser=" + editUser +
                ", delFlag=" + delFlag +
                '}';
    }
}