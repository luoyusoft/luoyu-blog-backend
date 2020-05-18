package com.luoyu.blog.project.mapper.applymapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.luoyu.blog.project.pojo.apply.Article;
import org.apache.ibatis.annotations.Param;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 文章Mapper接口
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * @author jinhaoxun
     * @description 查询文章是否存在
     * @param title 文章标题
     * @return ResponseResult 获取数据条数
     */
    int getArticleByTitle(@Param("title") String title);

}
