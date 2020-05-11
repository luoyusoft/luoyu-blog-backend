package com.jinhaoxun.blog.project.tt.pojo.quartz;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 任务表实体类
 */
public class Task extends Model<Task> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("TASK_ID")
    private Long taskId;

    /**
     * 类型（0：系统，1：用户）
     */
    @TableField("TYPE")
    private Integer type;

    /**
     * 参数
     */
    @TableField("PARAMS")
    private String params;

    /**
     * 执行时间
     */
    @TableField("EXECUTION_TIME")
    private Date executionTime;

    /**
     * 执行状态（false：未执行，true：已执行）
     */
    @TableField("EXECUTION_STATUS")
    private Boolean executionStatus;

    /**
     * 任务类名
     */
    @TableField("JOB_CLASS")
    private String jobClass;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

    /**
     * 更新人ID
     */
    @TableField("UPDATER_ID")
    private Long updaterId;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public Boolean getExecutionStatus() {
        return executionStatus;
    }

    public void setExecutionStatus(Boolean executionStatus) {
        this.executionStatus = executionStatus;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Long updaterId) {
        this.updaterId = updaterId;
    }

    @Override
    protected Serializable pkVal() {
        return this.taskId;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", type=" + type +
                ", params='" + params + '\'' +
                ", executionTime=" + executionTime +
                ", executionStatus=" + executionStatus +
                ", jobClass='" + jobClass + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", updaterId=" + updaterId +
                '}';
    }
}
