package com.luoyu.blog.entity.article.vo;

import com.luoyu.blog.entity.article.Article;
import com.luoyu.blog.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * ArticleVO
 *
 * @author luoyu
 * @date 2019/01/08 19:04
 * @description
 */
@Data
public class ArticleVO extends Article {

    private List<Tag> tagList;

}
