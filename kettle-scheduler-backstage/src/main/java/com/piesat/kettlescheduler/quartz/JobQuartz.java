package com.piesat.kettlescheduler.quartz;

import com.piesat.kettlescheduler.common.Constant;
import com.piesat.kettlescheduler.kettle.model.TransStatisticsInfo;
import com.piesat.kettlescheduler.kettle.repository.RepositoryUtil;
import com.piesat.kettlescheduler.model.KJobMonitor;
import com.piesat.kettlescheduler.model.KJobRecord;
import com.piesat.kettlescheduler.model.KRepository;
import com.piesat.kettlescheduler.model.KmKettleLog;
import com.piesat.kettlescheduler.quartz.model.DBConnectionModel;
import org.beetl.sql.core.*;
import org.beetl.sql.core.db.DBStyle;
import org.beetl.sql.core.db.MySqlStyle;
import org.beetl.sql.ext.DebugInterceptor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.pentaho.di.core.ProgressNullMonitorListener;
import org.pentaho.di.core.ResultFile;
import org.pentaho.di.core.RowMetaAndData;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleMissingPluginsException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.logging.LoggingBuffer;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.RepositoryDirectoryInterface;
import org.pentaho.di.repository.kdr.KettleDatabaseRepository;
import org.quartz.*;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@DisallowConcurrentExecution
public class JobQuartz implements InterruptableJob {
    private org.pentaho.di.job.Job job;

    public void execute(JobExecutionContext context) throws JobExecutionException {

        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        Object KRepositoryObject = jobDataMap.get(Constant.REPOSITORYOBJECT);
        Object DbConnectionObject = jobDataMap.get(Constant.DBCONNECTIONOBJECT);
        String jobName_str = context.getJobDetail().getKey().getName();
        String[] names = jobName_str.split("@");
        String jobId = String.valueOf(jobDataMap.get(Constant.JOBID));
        String categoryId = String.valueOf(jobDataMap.get(Constant.CATEGORY_ID));
        String jobPath = String.valueOf(jobDataMap.get(Constant.JOBPATH));
        String jobName = String.valueOf(jobDataMap.get(Constant.JOBNAME));
        String userId = String.valueOf(jobDataMap.get(Constant.USERID));
        String logLevel = String.valueOf(jobDataMap.get(Constant.LOGLEVEL));
        String logFilePath = String.valueOf(jobDataMap.get(Constant.LOGFILEPATH));
        Date lastExecuteTime = context.getFireTime();
        Date nexExecuteTime = context.getNextFireTime();

        if (null != DbConnectionObject && DbConnectionObject instanceof DBConnectionModel) {// 首先判断数据库连接对象是否正确
            // 判断作业类型
            if (null != KRepositoryObject && KRepositoryObject instanceof KRepository) {// 证明该作业是从资源库中获取到的
                try {
                    runRepositoryJob(KRepositoryObject, DbConnectionObject, jobId, categoryId, jobPath, jobName, userId, logLevel,
                            logFilePath, lastExecuteTime, nexExecuteTime);
                } catch (KettleException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    runFileJob(DbConnectionObject, jobId, jobPath, jobName, userId, logLevel, logFilePath, lastExecuteTime, nexExecuteTime);
                } catch (KettleXMLException | KettleMissingPluginsException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param KRepositoryObject 数据库连接对象
     * @param KRepositoryObject 资源库对象
     * @param jobId             作业ID
     * @param jobPath           作业在资源库中的路径信息
     * @param jobName           作业名称
     * @param userId            作业归属者ID
     * @param logLevel          作业的日志等级
     * @param logFilePath       作业日志保存的根路径
     * @return void
     * @throws KettleException
     * @Title runRepositoryJob
     * @Description 运行资源库中的作业
     */
    public void runRepositoryJob(Object KRepositoryObject, Object DbConnectionObject, String jobId, String categoryId,
                                 String jobPath, String jobName, String userId, String logLevel, String logFilePath, Date executeTime, Date nexExecuteTime) throws KettleException {
        KRepository kRepository = (KRepository) KRepositoryObject;
        Integer repositoryId = kRepository.getRepositoryId();
        KettleDatabaseRepository kettleDatabaseRepository = null;
        if (RepositoryUtil.KettleDatabaseRepositoryCatch.containsKey(repositoryId)) {
            kettleDatabaseRepository = RepositoryUtil.KettleDatabaseRepositoryCatch.get(repositoryId);
        } else {
            kettleDatabaseRepository = RepositoryUtil.connectionRepository(kRepository);
        }
        if (null != kettleDatabaseRepository) {
            RepositoryDirectoryInterface directory = kettleDatabaseRepository.loadRepositoryDirectoryTree()
                    .findDirectory(jobPath);
            JobMeta jobMeta = kettleDatabaseRepository.loadJob(jobName, directory, new ProgressNullMonitorListener(),
                    null);
            job = new org.pentaho.di.job.Job(kettleDatabaseRepository, jobMeta);
            job.setDaemon(true);
            job.setLogLevel(LogLevel.DEBUG);
            if (StringUtils.isNotEmpty(logLevel)) {
                job.setLogLevel(Constant.logger(logLevel));
            }
            String exception = null;
            Integer recordStatus = 1;
//            Date jobStartDate = null;
            Date jobStopDate = null;
            String logText = null;
            try {
//                jobStartDate = new Date();
                job.run();
                job.waitUntilFinished();
                jobStopDate = new Date();
            } catch (Exception e) {
                exception = e.getMessage();
                recordStatus = 2;
            } finally {
                if (job.isFinished()) {
                    if (job.getErrors() > 0) {
                        recordStatus = 2;
                        if(null == job.getResult().getLogText() || "".equals(job.getResult().getLogText())){
                            logText = exception;
                        }
                    }
                    // 写入作业执行结果
                    StringBuilder allLogFilePath = new StringBuilder();
                    allLogFilePath.append(logFilePath).append("/").append(userId).append("/")
                            .append(StringUtils.remove(jobPath, "/")).append("@").append(jobName).append("-log")
                            .append("/").append(new Date().getTime()).append(".").append("txt");
                    String logChannelId = job.getLogChannelId();
                    LoggingBuffer appender = KettleLogStore.getAppender();
                    logText = appender.getBuffer(logChannelId, true).toString();
                    try {
                        KJobRecord kJobRecord = new KJobRecord();
                        kJobRecord.setRecordJob(Integer.parseInt(jobId));
                        kJobRecord.setAddUser(Integer.parseInt(userId));
                        kJobRecord.setLogFilePath(allLogFilePath.toString());
                        kJobRecord.setRecordStatus(recordStatus);
                        kJobRecord.setStartTime(executeTime);
                        kJobRecord.setStopTime(jobStopDate);
                        Integer recordId = writeToDBAndFile(DbConnectionObject, kJobRecord, logText, executeTime, nexExecuteTime);

                        // 获取转换步骤信息统计 并插入统计日志
                        List<TransStatisticsInfo> statisticsInfoList = getTransStatisticsInfoList();
                        for (TransStatisticsInfo transStatisticsInfo : statisticsInfoList) {
                            KmKettleLog kmKettleLog = new KmKettleLog();
                            kmKettleLog.setJobId(Integer.parseInt(jobId));
                            kmKettleLog.setJobRecordId(recordId);
                            kmKettleLog.setCategoryId(Integer.parseInt(categoryId));
                            kmKettleLog.setType(1);
                            kmKettleLog.setCount(transStatisticsInfo.getStepOutput() + transStatisticsInfo.getStepUpdated());
                            kmKettleLog.setDate(executeTime);
                            writeToDB(DbConnectionObject, kmKettleLog);
                        }

                        // 获取文件执行结果 并插入统计日志
                        Map<String, ResultFile> resultFiles = job.getResult().getResultFiles();
                        if(resultFiles != null && resultFiles.size() > 0){
                            KmKettleLog kmKettleLog = new KmKettleLog();
                            kmKettleLog.setJobId(Integer.parseInt(jobId));
                            kmKettleLog.setJobRecordId(recordId);
                            kmKettleLog.setCategoryId(Integer.parseInt(categoryId));
                            kmKettleLog.setType(1);
                            kmKettleLog.setCount((long) resultFiles.size());
                            kmKettleLog.setDate(executeTime);
                            writeToDB(DbConnectionObject, kmKettleLog);
                        }
                    } catch (IOException | SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 写入转换步骤信息统计记录到数据库
     *
     * @param DbConnectionObject
     * @param kmKettleLog
     * @return void
     * @author YuWenjie
     **/
    private void writeToDB(Object DbConnectionObject, KmKettleLog kmKettleLog) throws SQLException {
        DBConnectionModel DBConnectionModel = (DBConnectionModel) DbConnectionObject;
        ConnectionSource source = ConnectionSourceHelper.getSimple(DBConnectionModel.getConnectionDriveClassName(),
                DBConnectionModel.getConnectionUrl(), DBConnectionModel.getConnectionUser(), DBConnectionModel.getConnectionPassword());
        DBStyle mysql = new MySqlStyle();
        SQLLoader loader = new ClasspathLoader("/");
        UnderlinedNameConversion nc = new UnderlinedNameConversion();
        SQLManager sqlManager = new SQLManager(mysql, loader,
                source, nc, new Interceptor[]{new DebugInterceptor()});
        DSTransactionManager.start();
        sqlManager.insert(kmKettleLog);
        DSTransactionManager.commit();
    }

    /**
     * 获取转换步骤信息统计
     *
     * @param
     * @return java.util.List<com.piesat.kettlescheduler.common.kettle.TransStatisticsInfo>
     * @author YuWenjie
     **/
    private List<TransStatisticsInfo> getTransStatisticsInfoList() {
        List<RowMetaAndData> rowMetaAndDataList = job.getResult().getRows();
        List<TransStatisticsInfo> statisticsInfoList = new ArrayList<>();
        for (RowMetaAndData rowMetaAndData : rowMetaAndDataList) {
            Object[] data = rowMetaAndData.getData();
            TransStatisticsInfo transStatisticsInfo = new TransStatisticsInfo();
            // 步骤名称
            transStatisticsInfo.setStepName((String) data[0]);
            // StepId
            transStatisticsInfo.setStepId((String) data[1]);
            // 输入行数
            transStatisticsInfo.setStepInput((Long) data[2]);
            // 输出行数
            transStatisticsInfo.setStepOutput((Long) data[3]);
            // 读取行数
            transStatisticsInfo.setStepRead((Long) data[4]);
            // 更新行数
            transStatisticsInfo.setStepUpdated((Long) data[5]);
            // 写行数
            transStatisticsInfo.setStepWrittent((Long) data[6]);
            // 舍弃行数
            transStatisticsInfo.setStepErrors((Long) data[7]);
            //  持续时间
            transStatisticsInfo.setStepSeconds((Long) data[8]);
            statisticsInfoList.add(transStatisticsInfo);
        }
        return statisticsInfoList;
    }

    public void runFileJob(Object DbConnectionObject, String jobId, String jobPath, String jobName,
                           String userId, String logLevel, String logFilePath, Date lastExecuteTime, Date nexExecuteTime) throws KettleXMLException, KettleMissingPluginsException {
        JobMeta jobMeta = new JobMeta(jobPath, null);
        job = new org.pentaho.di.job.Job(null, jobMeta);
        job.setDaemon(true);
        job.setLogLevel(LogLevel.DEBUG);
        if (StringUtils.isNotEmpty(logLevel)) {
            job.setLogLevel(Constant.logger(logLevel));
        }
        String exception = null;
        Integer recordStatus = 1;
        Date jobStartDate = null;
        Date jobStopDate = null;
        String logText = null;
        try {
            jobStartDate = new Date();
            job.run();
            job.waitUntilFinished();
            jobStopDate = new Date();
        } catch (Exception e) {
            exception = e.getMessage();
            recordStatus = 2;
        } finally {
            if (null != job && job.isFinished()) {
                if (job.getErrors() > 0
                        && (null == job.getResult().getLogText() || "".equals(job.getResult().getLogText()))) {
                    logText = exception;
                }
                // 写入作业执行结果
                StringBuilder allLogFilePath = new StringBuilder();
                allLogFilePath.append(logFilePath).append("/").append(userId).append("/")
                        .append(StringUtils.remove(jobPath, "/")).append("@").append(jobName).append("-log").append("/")
                        .append(new Date().getTime()).append(".").append("txt");
                String logChannelId = job.getLogChannelId();
                LoggingBuffer appender = KettleLogStore.getAppender();
                logText = appender.getBuffer(logChannelId, true).toString();
                try {
                    KJobRecord kJobRecord = new KJobRecord();
                    kJobRecord.setRecordJob(Integer.parseInt(jobId));
                    kJobRecord.setAddUser(Integer.parseInt(userId));
                    kJobRecord.setLogFilePath(allLogFilePath.toString());
                    kJobRecord.setRecordStatus(recordStatus);
                    kJobRecord.setStartTime(jobStartDate);
                    kJobRecord.setStopTime(jobStopDate);
                    writeToDBAndFile(DbConnectionObject, kJobRecord, logText, lastExecuteTime, nexExecuteTime);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param DbConnectionObject 数据库连接对象
     * @param kJobRecord         作业记录信息
     * @param logText            日志信息
     * @return void
     * @throws IOException
     * @throws SQLException
     * @Title writeToDBAndFile
     * @Description 保存作业运行日志信息到文件和数据库
     */
    private Integer writeToDBAndFile(Object DbConnectionObject, KJobRecord kJobRecord, String logText, Date lastExecuteTime, Date nextExecuteTime)
            throws IOException, SQLException {
        // 将日志信息写入文件
        FileUtils.writeStringToFile(new File(kJobRecord.getLogFilePath()), logText, Constant.DEFAULT_ENCODING, false);
        // 写入转换运行记录到数据库
        DBConnectionModel DBConnectionModel = (DBConnectionModel) DbConnectionObject;

        ConnectionSource source = ConnectionSourceHelper.getSimple(DBConnectionModel.getConnectionDriveClassName(),
                DBConnectionModel.getConnectionUrl(), DBConnectionModel.getConnectionUser(), DBConnectionModel.getConnectionPassword());
        DBStyle mysql = new MySqlStyle();
        SQLLoader loader = new ClasspathLoader("/");
        UnderlinedNameConversion nc = new UnderlinedNameConversion();
        SQLManager sqlManager = new SQLManager(mysql, loader,
                source, nc, new Interceptor[]{new DebugInterceptor()});
        DSTransactionManager.start();
        sqlManager.insertTemplate(kJobRecord, true);
        KJobMonitor template = new KJobMonitor();
        template.setAddUser(kJobRecord.getAddUser());
        template.setMonitorJob(kJobRecord.getRecordJob());
        KJobMonitor templateOne = sqlManager.templateOne(template);
        templateOne.setLastExecuteTime(lastExecuteTime);
        //在监控表中增加下一次执行时间
        templateOne.setNextExecuteTime(nextExecuteTime);
        if (kJobRecord.getRecordStatus() == 1) {// 证明成功
            //成功次数加1
            templateOne.setMonitorSuccess(templateOne.getMonitorSuccess() + 1);
            sqlManager.updateById(templateOne);
        } else if (kJobRecord.getRecordStatus() == 2) {// 证明失败
            //失败次数加1
            templateOne.setMonitorFail(templateOne.getMonitorFail() + 1);
            sqlManager.updateById(templateOne);
        }
        DSTransactionManager.commit();
        return kJobRecord.getRecordId();
    }

    @Override
    public void interrupt() throws UnableToInterruptJobException {
        //stop the running job
        this.job.stopAll();
    }
}
