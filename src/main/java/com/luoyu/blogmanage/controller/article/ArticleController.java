package com.luoyu.blogmanage.controller.article;

import com.luoyu.blogmanage.common.constants.RedisCacheNames;
import com.luoyu.blogmanage.common.enums.ResponseEnums;
import com.luoyu.blogmanage.common.exception.MyException;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.validator.ValidatorUtils;
import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.article.dto.ArticleDTO;
import com.luoyu.blogmanage.entity.base.Response;
import com.luoyu.blogmanage.service.article.ArticleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Set;

/**
 * ArticleAdminController
 *
 * @author luoyu
 * @date 2018/11/20 20:25
 * @description
 */
@RestController
@RequestMapping("/admin/article")
@CacheConfig(cacheNames ={RedisCacheNames.RECOMMEND,RedisCacheNames.TAG,RedisCacheNames.ARTICLE,RedisCacheNames.TIMELINE})
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("article:list")
    public Response listArticle(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title) {
        PageUtils articlePage = articleService.queryPage(page, limit, title);
        return Response.success(articlePage);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{articleId}")
    @RequiresPermissions("article:list")
    public Response info(@PathVariable("articleId") Integer articleId) {
        ArticleDTO article = articleService.getArticle(articleId);
        return Response.success(article);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("article:save")
    @CacheEvict(allEntries = true)
    public Response saveArticle(@RequestBody ArticleDTO article){
        ValidatorUtils.validateEntity(article);
        articleService.saveArticle(article);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @RequiresPermissions("article:update")
    @CacheEvict(allEntries = true)
    public Response updateArticle(@RequestBody ArticleDTO article){
        ValidatorUtils.validateEntity(article);
        articleService.updateArticle(article);

        return Response.success();
    }

    /**
     * 更新状态
     *
     * @param article
     * @return
     */
    @PutMapping("/update/status")
    @RequiresPermissions("article:update")
    @CacheEvict(allEntries = true)
    public Response updateStatus(@RequestBody Article article) {
        if(article.getId() == null || (article.getPublish() == null
                && article.getRecommend() == null && article.getTop() == null)){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "id，recommend，publish，top不能同时为空");
        }
        articleService.updateStatus(article);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("article:delete")
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public Response deleteBatch(@RequestBody Integer[] articleIds) {
        articleService.deleteBatch(articleIds);
        return Response.success();
    }

    /**
     * 删除缓存
     */
    @DeleteMapping("/cache/refresh")
    @RequiresPermissions("article:cache:refresh")
    public Response flush() {
        Set<String> keys = redisTemplate.keys(RedisCacheNames.PROFIX + "*");
        redisTemplate.delete(keys);

        return Response.success();
    }

}
