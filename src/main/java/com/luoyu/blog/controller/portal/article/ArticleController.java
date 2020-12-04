package com.luoyu.blog.controller.portal.article;

import com.luoyu.blog.common.aop.annotation.LogLike;
import com.luoyu.blog.common.aop.annotation.LogView;
import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.service.article.ArticleService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author luoyu
 * @since 2018-11-07
 */
@RestController("articlePortalController")
@CacheConfig(cacheNames = {RedisCacheNames.ARTICLE})
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @GetMapping("/article/{articleId}")
    @LogView(type = "article")
    public Response getArticle(@PathVariable Integer articleId){
        ArticleDTO article = articleService.getArticleDTOById(articleId);
        return Response.success(article);
    }

    @PutMapping("/article/like/{id}")
    @LogLike(type = "article")
    public Response likeArticle(@PathVariable Integer id) {
        return Response.success();
    }

    @GetMapping("/articles")
    @Cacheable
    public Response list(@RequestParam Map<String, Object> params){
        PageUtils page = articleService.queryPageCondition(params);
        return Response.success(page);
    }

}
