package com.luoyu.blogmanage.mapper.article;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.article.vo.ArticleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author luoyu
 * @since 2018-11-07
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询列表
     *
     * @param page
     * @param params
     * @return
     */
    List<ArticleVO> listArticleVo(Page<ArticleVO> page, @Param("params") Map<String, Object> params);

    /**
     * 根据条件查询分页
     * @param page
     * @param params
     * @return
     */
    List<ArticleVO> queryPageCondition(Page<ArticleVO> page, @Param("params") Map<String, Object> params);

    /**
     * 更新阅读记录
     * @param id
     */
    void updateReadNum(Integer id);

    /**
     * 获取简单的对象
     * @param id
     * @return
     */
    ArticleVO getSimpleArticleVo(Integer id);

    /**
     * 更新点赞记录
     * @param id
     */
    void updateLikeNum(int id);

    /**
     * 判断类别下是否有文章
     * @param categoryId
     * @return
     */
    int checkByCategory(Integer categoryId);

    /**
     * 查询所有文章列表
     * @return
     */
    List<ArticleVO> selectArticleList();

    /**
     * 查询所有文章列表
     * @return
     */
    List<Article> selectArticleListByTitle(String title);

    /**
     * 更新文章
     * @return
     */
    Boolean updateArticle(Article article);

    /**
     * 查询已发布文章
     * @return
     */
    Article selectArticleById(Integer id);

}
