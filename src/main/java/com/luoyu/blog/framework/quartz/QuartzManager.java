package com.luoyu.blog.framework.quartz;

import com.luoyu.blog.common.exception.ExceptionFactory;
import com.luoyu.blog.project.pojo.quartz.AddCronJobReq;
import com.luoyu.blog.project.pojo.quartz.AddSimpleJobReq;
import com.luoyu.blog.common.response.ResponseMsg;
import com.luoyu.blog.common.util.datautil.TimeUtil;
import org.quartz.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

import static org.quartz.DateBuilder.futureDate;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description Quartz管理操作类
 */
@Service
public class QuartzManager {

    private Scheduler scheduler;

    @Resource
    private ExceptionFactory exceptionFactory;

    /**
     * @author jinhaoxun
     * @description 构造器
     * @param scheduler 调度器
     */
    public QuartzManager(Scheduler scheduler){
        this.scheduler = scheduler;
    }

    /**
     * quartz任务类包路径
     */
    private static String jobUri = "com.luoyu.blog.framework.quartz.job.";

    /**
     * @author jinhaoxun
     * @description 添加一个Simple定时任务
     * @param addSimpleJobReq 参数对象
     * @param taskId 任务ID
     * @throws RuntimeException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addSimpleJob(AddSimpleJobReq addSimpleJobReq,String taskId) throws Exception{
        String jobUrl = jobUri + addSimpleJobReq.getJobClass();
        try {
            Class<? extends Job> aClass = (Class<? extends Job>) Class.forName(jobUrl).newInstance().getClass();
            // 任务名，任务组，任务执行类
            JobDetail job = JobBuilder.newJob(aClass).withIdentity(taskId,
                    "JobGroup").build();
            //增加任务ID参数
            addSimpleJobReq.getParams().put("taskId",taskId);
            // 添加任务参数
            job.getJobDataMap().putAll(addSimpleJobReq.getParams());
            // 转换为时间差，秒单位
            int time = TimeUtil.getTimeDifferenceSeconds(addSimpleJobReq.getDate(),new Date());

            SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger()
                    .withIdentity(taskId, taskId+"TiggerGroup")
                    .startAt(futureDate(time, DateBuilder.IntervalUnit.SECOND))
                    .build();
            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(job, trigger);
            if (!scheduler.isShutdown()) {
                // 启动
                scheduler.start();
            }
        } catch (Exception e) {
            throw exceptionFactory.build(ResponseMsg.QUARTZ_ADD_JOB_FAIL.getCode(),e.getMessage());
        }
    }

    /**
     * @author jinhaoxun
     * @description 添加一个Cron定时任务
     * @param addCronJobReq 参数对象
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addCronJob(AddCronJobReq addCronJobReq) throws Exception{
        String jobUrl = jobUri + addCronJobReq.getJobClass();
        try {
            Class<? extends Job> aClass = (Class<? extends Job>) Class.forName(jobUrl).newInstance().getClass();
            // 任务名，任务组，任务执行类
            JobDetail job = JobBuilder.newJob(aClass).withIdentity(addCronJobReq.getJobName(),
                    addCronJobReq.getJobGroupName()).build();
            // 添加任务参数
            job.getJobDataMap().putAll(addCronJobReq.getParams());
            // 创建触发器
            CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger()
                    // 触发器名,触发器组
                    .withIdentity(addCronJobReq.getTriggerName(), addCronJobReq.getTriggerGroupName())
                    // 触发器时间设定
                    .withSchedule(CronScheduleBuilder.cronSchedule(addCronJobReq.getDate()))
                    .build();
            // 调度容器设置JobDetail和Trigger
            scheduler.scheduleJob(job, trigger);

            if (!scheduler.isShutdown()) {
                // 启动
                scheduler.start();
            }
        } catch (Exception e) {
            throw exceptionFactory.build(ResponseMsg.QUARTZ_ADD_JOB_FAIL.getCode(),e.getMessage());
        }
    }

    /**
     * @author jinhaoxun
     * @description 修改一个任务的触发时间
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     * @param cron              时间设置，参考quartz说明文档
     * @throws Exception
     */
    public void modifyJobTime(String triggerName, String triggerGroupName, String cron) throws Exception{
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(cron)) {
                // 触发器
                TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
                // 触发器名,触发器组
                triggerBuilder.withIdentity(triggerName, triggerGroupName);
                triggerBuilder.startNow();
                // 触发器时间设定
                triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cron));
                // 创建Trigger对象
                trigger = (CronTrigger) triggerBuilder.build();
                // 方式一 ：修改一个任务的触发时间
                scheduler.rescheduleJob(triggerKey, trigger);
            }
        } catch (Exception e) {
            throw exceptionFactory.build(ResponseMsg.QUARTZ_UPDATE_JOB_FAIL.getCode(),e.getMessage());
        }
    }
    /**
     * @author jinhaoxun
     * @description 移除一个任务
     * @param jobName           任务名
     * @param jobGroupName      任务组名
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     * @throws Exception
     */
    public void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) throws Exception{
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroupName);
            // 停止触发器
            scheduler.pauseTrigger(triggerKey);
            // 移除触发器
            scheduler.unscheduleJob(triggerKey);
            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        } catch (Exception e) {
            throw exceptionFactory.build(ResponseMsg.QUARTZ_DELETE_JOB_FAIL.getCode(),e.getMessage());
        }
    }

    /**
     * @author jinhaoxun
     * @description 获取任务是否存在
     * @param triggerName       触发器名
     * @param triggerGroupName  触发器组名
     * @return Boolean 返回操作结果
     * 获取任务是否存在
     * STATE_BLOCKED 4 阻塞
     * STATE_COMPLETE 2 完成
     * STATE_ERROR 3 错误
     * STATE_NONE -1 不存在
     * STATE_NORMAL 0 正常
     * STATE_PAUSED 1 暂停
     * @throws Exception
     */
    public  Boolean notExists(String triggerName, String triggerGroupName) throws Exception{
        try {
            return scheduler.getTriggerState(TriggerKey.triggerKey(triggerName, triggerGroupName)) == Trigger.TriggerState.NONE;
        } catch (Exception e) {
            throw exceptionFactory.build(ResponseMsg.QUARTZ_EXISTS_JOB_FAIL.getCode(),e.getMessage());
        }
    }

    /**
     * @author jinhaoxun
     * @description 关闭调度器
     * @throws RuntimeException
     */
    public void shutdown() throws Exception{
        try {
            if(scheduler.isStarted()){
                scheduler.shutdown(true);
            }
        } catch (Exception e) {
            throw exceptionFactory.build(ResponseMsg.QUARTZ_SHUTDOWN_SCHEDULER_FAIL.getCode(),e.getMessage());
        }
    }

}