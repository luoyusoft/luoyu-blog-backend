package com.luoyu.blog.project.service.search;

import com.luoyu.blog.common.entity.article.Article;

import java.util.List;

public interface ArticleEsServer {

    List<Article> searchArticle(String keyword) throws Exception;

    boolean initArticle() throws Exception;

}
