package com.luoyu.blog.project.service.portal.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.entity.article.Article;
import com.luoyu.blog.common.entity.article.vo.ArticleVO;
import com.luoyu.blog.common.util.PageUtils;

import java.util.Map;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author bobbi
 * @since 2018-11-07
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页分类获取列表
     * @param params
     * @return
     */
    PageUtils queryPageCondition(Map<String, Object> params);

    /**
     * 获取ArticleVo对象
     * @param articleId
     * @return
     */
    ArticleVO getArticleVo(Integer articleId);

    /**
     * 获取简单的Article对象
     * @param articleId
     * @return
     */
     ArticleVO getSimpleArticleVo(Integer articleId);

}
