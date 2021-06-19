package com.jinhx.blog.service.search;

import com.jinhx.blog.entity.article.vo.ArticleVO;

import java.util.List;

public interface ArticleEsServer {

    boolean initArticleList() throws Exception;

    /**
     * 搜索文章
     * @param keyword 关键字
     * @return 搜索结果
     */
    List<ArticleVO> searchArticleList(String keyword) throws Exception;

}
