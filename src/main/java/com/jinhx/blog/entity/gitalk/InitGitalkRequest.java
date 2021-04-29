package com.jinhx.blog.entity.gitalk;

import lombok.Data;

@Data
public class InitGitalkRequest {

    // 地址的相对路径后面的id 例如：1
    private Integer id;
    // 地址的相对路径 例如：article
    private String type;
    // 标题
    private String title;

}
