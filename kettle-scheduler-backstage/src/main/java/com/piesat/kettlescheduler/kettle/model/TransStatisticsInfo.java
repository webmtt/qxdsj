package com.piesat.kettlescheduler.kettle.model;

/**
 * @title: kettle-scheduler->TransStatisticsInfo
 * @description: 转换步骤信息统计
 * @author: YuWenjie
 * @date: 2020-05-21 13:50
 **/
public class TransStatisticsInfo {

    /**
     * 步骤名称
     */
    private String stepName;

    /**
     * StepId
     */
    private String StepId;

    /**
     * 输入行数
     * 不是从上一个步骤读取，是从文件、数据库等地方读取，一般输入控件才有
     */
    private Long stepInput;

    /**
     * 输出行数
     * 不是输出到下一个步骤，是输出到文件、数据库等，一般输出控件才有
     */
    private Long stepOutput;

    /**
     * 读取行数
     * 从前面步聚读取的记录数
     */
    private Long stepRead;

    /**
     * 更新行数
     * 向后面步骤输出的记录数
     */
    private Long stepUpdated;

    /**
     * 写行数
     * 更新的记录数
     */
    private Long stepWrittent;

    /**
     * 舍弃行数
     * 出错的记录数
     */
    private Long stepErrors;

    /**
     * 持续时间(毫秒)
     */
    private Long stepSeconds;

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepId() {
        return StepId;
    }

    public void setStepId(String stepId) {
        StepId = stepId;
    }

    public Long getStepInput() {
        return stepInput;
    }

    public void setStepInput(Long stepInput) {
        this.stepInput = stepInput;
    }

    public Long getStepOutput() {
        return stepOutput;
    }

    public void setStepOutput(Long stepOutput) {
        this.stepOutput = stepOutput;
    }

    public Long getStepRead() {
        return stepRead;
    }

    public void setStepRead(Long stepRead) {
        this.stepRead = stepRead;
    }

    public Long getStepUpdated() {
        return stepUpdated;
    }

    public void setStepUpdated(Long stepUpdated) {
        this.stepUpdated = stepUpdated;
    }

    public Long getStepWrittent() {
        return stepWrittent;
    }

    public void setStepWrittent(Long stepWrittent) {
        this.stepWrittent = stepWrittent;
    }

    public Long getStepErrors() {
        return stepErrors;
    }

    public void setStepErrors(Long stepErrors) {
        this.stepErrors = stepErrors;
    }

    public Long getStepSeconds() {
        return stepSeconds;
    }

    public void setStepSeconds(Long stepSeconds) {
        this.stepSeconds = stepSeconds;
    }

    @Override
    public String toString() {
        return "TransStatisticsInfo{" +
                "stepName='" + stepName + '\'' +
                ", StepId='" + StepId + '\'' +
                ", stepInput=" + stepInput +
                ", stepOutput=" + stepOutput +
                ", stepRead=" + stepRead +
                ", stepUpdated=" + stepUpdated +
                ", stepWrittent=" + stepWrittent +
                ", stepErrors=" + stepErrors +
                ", stepSeconds=" + stepSeconds +
                '}';
    }
}
