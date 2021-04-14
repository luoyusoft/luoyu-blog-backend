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

    /**
     * 判断类别下是否有文章
     * @param categoryId
     * @return
     */
    boolean checkByCategory(Integer categoryId);

    /**
     * 判断上传文件下是否有文章
     * @param url
     * @return
     */
    boolean checkByFile(String url);

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
     * 分页获取首页列表
     * @param page 页码
     * @param limit 每页数量
     * @return 文章列表
     */
    PageUtils queryHomePageCondition(Integer page, Integer limit);

    /**
     * 获取ArticleVo对象
     * @param articleId
     * @return
     */
    ArticleDTO getArticleDTOById(Integer articleId);

    /**
     * 获取热读榜
     * @return 文章列表
     */
    List<ArticleVO> getHotReadList();

    /**
     * 更新点赞
     * @return
     */
    Boolean likeArticle(Integer id);

}
