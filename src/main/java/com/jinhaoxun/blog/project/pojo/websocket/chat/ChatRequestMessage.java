package com.jinhaoxun.blog.project.tt.pojo.websocket.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description webSocket单发消息体
 */
@Setter
@Getter
@ToString
public class ChatRequestMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息发送者ID
     */
    @NotNull(message = "消息发送者id不能为空")
    private String senderId;

    /**
     * 消息接收者ID
     */
    @NotNull(message = "消息接收者id不能为空")
    private String recipientId;

    /**
     * 消息类型
     */
    @NotNull(message = "消息类型不能为空")
    private Integer type;

    /**
     * 消息内容
     */
    @NotNull(message = "消息内容不能为空")
    private String content;

    /**
     * 发送时间
     */
    @NotNull(message = "发送时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date sendTime;

}
