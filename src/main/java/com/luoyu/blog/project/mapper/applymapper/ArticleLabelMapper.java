package com.luoyu.blog.project.mapper.applymapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.project.pojo.apply.ArticleLabel;
import org.apache.ibatis.annotations.Param;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 文章标签Mapper接口
 */
public interface ArticleLabelMapper extends BaseMapper<ArticleLabel> {

    /**
     * @author jinhaoxun
     * @description 删除文章标签（根据文章ID）
     * @param articleId 文章ID
     * @return ResponseResult 删除成功条数
     */
    int deleteLabelByArticleId(@Param("articleId") Long articleId);

    /**
     * @author jinhaoxun
     * @description 获取文章标签（根据文章ID）
     * @param articleId 文章ID
     * @return ResponseResult 文章标签
     */
    ArticleLabel getLabelByArticleId(@Param("articleId") Long articleId);
}
