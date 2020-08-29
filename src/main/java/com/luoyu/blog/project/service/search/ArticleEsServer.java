package com.luoyu.blog.project.service.search;

import com.luoyu.blog.common.entity.article.Article;

import java.util.List;

public interface ArticleEsServer {

    List<Article> search(String keyword) throws Exception;

}
