package com.piesat.kettlescheduler.model;

import java.io.Serializable;

/**
 * k_job
 * @author 
 */
public class KJobWithBLOBs extends KJob implements Serializable {
    /**
     * 任务描述
     */
    private String jobDescription;

    /**
     * 作业保存路径（可以是资源库中的路径也可以是服务器中保存作业文件的路径）
     */
    private String jobPath;

    private static final long serialVersionUID = 1L;

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobPath() {
        return jobPath;
    }

    public void setJobPath(String jobPath) {
        this.jobPath = jobPath;
    }
}