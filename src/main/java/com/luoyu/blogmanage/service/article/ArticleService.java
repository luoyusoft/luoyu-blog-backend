package com.luoyu.blogmanage.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.article.dto.ArticleDTO;
import com.luoyu.blogmanage.common.util.PageUtils;

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
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

    /**
     * 保存博文文章
     * @param blogArticle
     */
    void saveArticle(ArticleDTO blogArticle);

    /**
     * 批量删除
     * @param articleIds
     */
    void deleteBatch(Integer[] articleIds);

    /**
     * 更新博文
     * @param blogArticle
     */
    void updateArticle(ArticleDTO blogArticle);

    /**
     * 获取文章对象
     * @param articleId
     * @return
     */
    ArticleDTO getArticle(Integer articleId);

    boolean checkByCategory(Integer id);

}
