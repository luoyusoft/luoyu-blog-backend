package com.luoyu.blog.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TypeEnum
 *
 * @author luoyu
 * @date 2019/01/08 20:21
 * @description 模块枚举
 */
@Getter
@AllArgsConstructor
public enum ModuleEnum {

    /**
     * 文章模块
     */
    ARTICLE(0, "article"),
    /**
     * 视频模块
     */
    VIDEO(1, "video"),
    /**
     * 聊天室模块
     */
    CHAT(2, "chat");


    private int code;
    private String name;

}
