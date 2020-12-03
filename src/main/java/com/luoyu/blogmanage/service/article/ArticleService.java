package com.luoyu.blogmanage.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.article.vo.ArticleVO;
import com.luoyu.blogmanage.common.util.PageUtils;

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
     * 保存博文文章
     * @param article
     */
    void saveArticle(ArticleVO article);

    /**
     * 批量删除
     * @param ids
     */
    void deleteArticles(Integer[] ids);

    /**
     * 更新博文
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

}
