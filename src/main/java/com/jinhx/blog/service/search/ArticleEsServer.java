package com.jinhx.blog.service.search;

import com.jinhx.blog.entity.article.vo.ArticleVO;

import java.util.List;

public interface ArticleEsServer {

    boolean initArticleList() throws Exception;

    List<ArticleVO> searchArticleList(String keyword) throws Exception;

}
