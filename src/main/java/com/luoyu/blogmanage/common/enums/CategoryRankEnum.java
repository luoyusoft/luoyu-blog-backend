package com.luoyu.blogmanage.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CategoryRankEnum
 *
 * @author luoyu
 * @date 2019/01/08 20:25
 * @description 分类级别枚举
 */
@Getter
@AllArgsConstructor
public enum CategoryRankEnum {

    /**
     * 负一级
     */
    ROOT(-1, "负一级"),
    /**
     * 一级
     */
    FIRST(0, "一级"),
    /**
     * 二级
     */
    SECOND(1, "二级"),
    /**
     * 三级
     */
    THIRD(2, "三级");

    private int code;
    private String name;

}
