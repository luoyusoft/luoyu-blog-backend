package com.luoyu.blog.project.pojo.quartz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 删除定时任务请求实体类
 */
@Setter
@Getter
@ToString
public class DeleteJobReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "任务名不能为空")
    private String jobName;

    @NotNull(message = "任务组名不能为空")
    private String jobGroupName;

    @NotNull(message = "调度器名不能为空")
    private String triggerName;

    @NotNull(message = "调度器组名不能为空")
    private String triggerGroupName;

}
