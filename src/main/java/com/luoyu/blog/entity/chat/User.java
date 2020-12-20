package com.luoyu.blog.entity.chat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author luoyu
 * @date 2019-06-13
 */
@Data
@ApiModel(description = "websocket用户")
public class User implements Serializable {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "avatar")
    private String avatar;

    public void setName(String name) {
        this.name = name.trim();
    }

}
