package cma.cimiss2.dpc.fileloop.common;

import static org.quartz.JobBuilder.newJob;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuartzManager {
	private final static Logger logger = LoggerFactory.getLogger("messageInfo"); //消息日志
	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	
	/**
     * @Description: 添加一个定时任务
     *
     * @param jobName 任务名
     * @param jobGroupName  任务组名
     * @param triggerName 触发器名
     * @param triggerGroupName 触发器组名
     * @param jobClass  任务
     * @param cron   时间设置，参考quartz说明文档
     */
    public static void addJob(String jobName, String jobGroupName,
                              String triggerName, String triggerGroupName, @SuppressWarnings("rawtypes") Class jobClass, String cron) {
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            // 任务名，任务组，任务执行类
            @SuppressWarnings("unchecked")
			JobDetail jobDetail= newJob(jobClass).withIdentity(jobName, jobGroupName).build();

            // 触发器
            TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
            // 触发器名,触发器组
            triggerBuilder.withIdentity(triggerName, triggerGroupName);
            triggerBuilder.startNow();
            // 触发器时间设定
            triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
            // 创建Trigger对象
            CronTrigger trigger = (CronTrigger) triggerBuilder.build();

            // 调度容器设置JobDetail和Trigger
            sched.scheduleJob(jobDetail, trigger);
            
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
            
            System.out.println("++++++++++++++++++++++++++++++++");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    /**
     *  安全关闭
     * @throws SchedulerException
     */
    public static void safeShutdown() throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        int executingJobSize = scheduler.getCurrentlyExecutingJobs().size();
        logger.info("安全关闭 当前还有" + executingJobSize + "个任务正在执行，等待完成后关闭");
        //等待任务执行完后安全关闭
        scheduler.shutdown(true);

        logger.info("安全关闭 成功");
    }
}
