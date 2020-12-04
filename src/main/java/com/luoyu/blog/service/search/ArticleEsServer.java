package com.luoyu.blog.service.search;

import com.luoyu.blog.entity.article.dto.ArticleDTO;

import java.util.List;

public interface ArticleEsServer {

    boolean initArticle() throws Exception;

    List<ArticleDTO> searchArticleList(String keyword) throws Exception;


}
