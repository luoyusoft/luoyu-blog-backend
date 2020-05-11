package com.jinhaoxun.blog.project.tt.pojo.rocketmq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 新增RocketMq消费者请求实体类
 */
@Setter
@Getter
@ToString
public class AddConsumerReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "消费者组名不能为空")
    private String consumerGroup;

    @NotNull(message = "Topic不能为空")
    private String topic;

}
