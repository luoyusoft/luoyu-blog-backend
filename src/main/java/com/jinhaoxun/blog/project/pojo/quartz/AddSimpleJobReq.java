package com.jinhaoxun.blog.project.tt.pojo.quartz;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 新增Simple定时任务请求实体类
 */
@Setter
@Getter
@ToString
public class AddSimpleJobReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "任务类不能为空")
    private String jobClass;

    @NotNull(message = "任务执行时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date date;

    @NotNull(message = "参数不能为空")
    private Map<String, String> params = new HashMap<>();

    @NotNull(message = "操作人id不能为空")
    private Long operatorId;

    @NotNull(message = "任务类型不能为空")
    private Integer type;

}
