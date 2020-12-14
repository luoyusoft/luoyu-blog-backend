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
public enum FileModuleEnum {

    /**
     * 文章模块
     */
    ARTICLE(0, "article"),
    /**
     * 视频模块
     */
    VIDEO(1, "video"),
    /**
     * 友链模块
     */
    LINK(2, "link");

    private int code;
    private String name;

}
