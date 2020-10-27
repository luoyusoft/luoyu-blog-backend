package com.luoyu.blogmanage.entity.article.dto;

import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * ArticleDto
 *
 * @author luoyu
 * @date 2019/01/08 19:04
 * @description
 */
@Data
public class ArticleDTO extends Article {

    private List<Tag> tagList;

}
