package com.luoyu.blog.service.article;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.article.Article;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.article.vo.ArticleVO;

import java.util.List;

/**
 * ArticleService
 *
 * @author luoyu
 * @date 2018/11/21 12:47
 * @description
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页查询文章列表
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
     * @param articleVO
     */
    void updateArticle(ArticleVO articleVO);

    /**
     * 更新文章状态
     * @param articleVO
     */
    void updateArticleStatus(ArticleVO articleVO);

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
     * @param page
     * @param limit
     * @param latest
     * @param categoryId
     * @param like
     * @param read
     * @return
     */
    PageUtils queryPageCondition(Integer page, Integer limit, Boolean latest, Integer categoryId, Boolean like, Boolean read);

    /**
     * 获取ArticleVo对象
     * @param articleId
     * @return
     */
    ArticleDTO getArticleDTOById(Integer articleId);

    /**
     * 获取热读榜
     * @return
     */
    List<ArticleVO> getHotReadList();

    /**
     * 更新点赞
     * @return
     */
    Boolean likeArticle(Integer id);

}
