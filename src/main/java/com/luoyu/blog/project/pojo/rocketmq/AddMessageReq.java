package com.luoyu.blog.project.pojo.rocketmq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 发送RocketMQ消息请求实体类
 */
@Setter
@Getter
@ToString
public class AddMessageReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Topic不能为空")
    private String topic;

    @NotNull(message = "Tag不能为空")
    private String tag;

    @NotNull(message = "发送消息不能为空")
    private String message;

}
