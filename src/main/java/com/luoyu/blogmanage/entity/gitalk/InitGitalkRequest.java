package com.luoyu.blogmanage.entity.gitalk;

import lombok.Data;

@Data
public class InitGitalkRequest {

    // 博客地址的相对路径后面的id 例如：1
    private Integer id;
    // 博客地址的相对路径 例如：article
    private String type;
    // 博客标题
    private String title;

}
