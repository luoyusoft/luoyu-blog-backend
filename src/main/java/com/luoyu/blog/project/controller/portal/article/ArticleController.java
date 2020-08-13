package com.luoyu.blog.project.controller.portal.article;

import com.luoyu.blog.common.base.Result;
import com.luoyu.blog.common.entity.article.vo.ArticleVO;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.project.service.portal.article.ArticleService;
import com.luoyu.blog.common.portal.annotation.LogLike;
import com.luoyu.blog.common.portal.annotation.LogView;
import com.luoyu.blog.common.constants.RedisCacheNames;
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
 * @author bobbi
 * @since 2018-11-07
 */
@RestController("articlePortalController")
@CacheConfig(cacheNames = {RedisCacheNames.ARTICLE})
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @GetMapping("/article/{articleId}")
    @LogView(type = "article")
    public Result getArticle(@PathVariable Integer articleId){
        ArticleVO article = articleService.getArticleVo(articleId);
        return Result.ok().put("article",article);
    }

    @PutMapping("/article/like/{id}")
    @LogLike(type = "article")
    public Result likeArticle(@PathVariable Integer id) {
        return Result.ok();
    }

    @GetMapping("/articles")
    @Cacheable
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = articleService.queryPageCondition(params);
        return Result.ok().put("page",page);
    }


}
