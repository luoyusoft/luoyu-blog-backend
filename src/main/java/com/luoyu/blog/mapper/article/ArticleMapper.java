package com.luoyu.blog.mapper.article;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luoyu.blog.entity.article.Article;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
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
    List<ArticleDTO> listArticleDTO(Page<ArticleDTO> page, @Param("params") Map<String, Object> params);

    /**
     * 更新阅读记录
     * @param id
     */
    Boolean updateReadNum(Integer id);

    /**
     * 更新点赞记录
     * @param id
     */
    Boolean updateLikeNum(Integer id);

    /**
     * 判断类别下是否有文章
     * @param categoryId
     * @return
     */
    int checkByCategory(Integer categoryId);

    /**
     * 判断上传文件下是否有文章
     * @param url
     * @return
     */
    int checkByFile(String url);

    /**
     * 查询所有文章列表
     * @return
     */
    List<ArticleDTO> selectArticleDTOList();

    /**
     * 查询所有文章列表
     * @return
     */
    List<Article> selectArticleListByTitle(String title);

    /**
     * 更新文章
     * @return
     */
    Boolean updateArticleById(Article article);

    /********************** portal ********************************/

    /**
     * 根据条件查询分页
     * @param page
     * @param params
     * @return
     */
    List<ArticleDTO> queryPageCondition(Page<ArticleDTO> page, @Param("params") Map<String, Object> params);

    /**
     * 获取简单的对象
     * @param id
     * @return
     */
    ArticleDTO getSimpleArticleDTO(Integer id);

    /**
     * 获取热读榜
     * @return
     */
    List<ArticleDTO> getHotReadList();

    /**
     * 查询已发布文章
     * @return
     */
    Article selectArticleById(Integer id);

}
