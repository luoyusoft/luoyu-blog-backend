package com.luoyu.blog.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.entity.article.Article;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.article.vo.ArticleVO;
import com.luoyu.blog.common.util.PageUtils;

import java.util.Map;

/**
 * ArticleService
 *
 * @author luoyu
 * @date 2018/11/21 12:47
 * @description
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页查询博文列表
     * @param page
     * @param limit
     * @param title
     * @return
     */
    PageUtils queryPage(Integer page, Integer limit, String title);

    /**
     * 保存文章
     * @param article
     */
    void saveArticle(ArticleVO article);

    /**
     * 批量删除
     * @param ids
     */
    void deleteArticles(Integer[] ids);

    /**
     * 更新文章
     * @param article
     */
    void updateArticle(ArticleVO article);

    /**
     * 获取文章对象
     * @param articleId
     * @return
     */
    ArticleVO getArticle(Integer articleId);

    boolean checkByCategory(Integer id);

    /********************** portal ********************************/

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
    ArticleDTO getArticleDTOById(Integer articleId);

    /**
     * 获取简单的Article对象
     * @param articleId
     * @return
     */
    ArticleDTO getSimpleArticleDTO(Integer articleId);

}
