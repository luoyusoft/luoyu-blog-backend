package com.luoyu.blog.project.service.search;

import com.luoyu.blog.common.entity.article.dto.ArticleDTO;

import java.util.List;

public interface ArticleEsServer {

    List<ArticleDTO> searchArticleList(String keyword) throws Exception;

    boolean initArticle() throws Exception;

}
