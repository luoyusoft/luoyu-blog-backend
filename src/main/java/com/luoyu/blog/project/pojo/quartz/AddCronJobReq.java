package com.luoyu.blog.project.pojo.quartz;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 新增Cron定时任务请求实体类
 */
@Setter
@Getter
@ToString
public class AddCronJobReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "任务名不能为空")
    private String jobName;

    @NotNull(message = "任务组名不能为空")
    private String jobGroupName;

    @NotNull(message = "触发器名不能为空")
    private String triggerName;

    @NotNull(message = "触发器组名不能为空")
    private String triggerGroupName;

    @NotNull(message = "任务类不能为空")
    private String jobClass;

    @NotNull(message = "任务执行时间不能为空")
    private String date;

    @NotNull(message = "参数不能为空")
    private Map<String, String> params = new HashMap<>();

}
