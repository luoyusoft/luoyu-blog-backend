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
    ARTICLE(0, "文章模块"),
    /**
     * 图书模块
     */
    BOOK(1, "文章模块"),
    /**
     * 图书笔记模块
     */
    BOOK_NOTE(2, "文章模块");

    private int code;
    private String name;

}
