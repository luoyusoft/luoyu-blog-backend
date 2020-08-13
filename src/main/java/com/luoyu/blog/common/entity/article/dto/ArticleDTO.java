package com.luoyu.blog.common.entity.article.dto;

import com.luoyu.blog.common.entity.article.Article;
import com.luoyu.blog.common.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * ArticleDto
 *
 * @author bobbi
 * @date 2019/01/08 19:04
 * @email 571002217@qq.com
 * @description
 */
@Data
public class ArticleDTO extends Article {

    private List<Tag> tagList;

}
