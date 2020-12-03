package com.luoyu.blogmanage.entity.base;


import com.luoyu.blogmanage.common.enums.ResponseEnums;
import lombok.Data;

import javax.validation.Valid;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Response
 *
 * @author luoyu
 * @date 2018/10/07 13:28
 * @description 通用请求类
 */
@Data
public class Request implements Serializable {

    private String channel;
    private Object data;
    private String time;

    /**
     * 请求的密文，如果该接口需要加密上送，
     * 则将sdt的密文反序化到data，
     * sdt和action至少有一个为空
     */
    private String sdt;

}
