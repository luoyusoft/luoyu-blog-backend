package com.luoyu.blog.controller.article;

import com.luoyu.blog.common.aop.annotation.LogView;
import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.article.vo.ArticleVO;
import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.service.article.ArticleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
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
@CacheConfig(cacheNames ={RedisCacheNames.ARTICLE})
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 列表
     */
    @GetMapping("/manage/article/list")
    @RequiresPermissions("article:list")
    public Response listArticle(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title) {
        PageUtils articlePage = articleService.queryPage(page, limit, title);
        return Response.success(articlePage);
    }

    /**
     * 信息
     */
    @GetMapping("/manage/article/info/{articleId}")
    @RequiresPermissions("article:list")
    public Response info(@PathVariable("articleId") Integer articleId) {
        ArticleVO article = articleService.getArticle(articleId);
        return Response.success(article);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/article/save")
    @RequiresPermissions("article:save")
    @CacheEvict(allEntries = true)
    public Response saveArticle(@RequestBody ArticleVO article){
        ValidatorUtils.validateEntity(article, AddGroup.class);
        articleService.saveArticle(article);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/manage/article/update")
    @RequiresPermissions("article:update")
    @CacheEvict(allEntries = true)
    public Response updateArticle(@RequestBody ArticleVO articleVO){
        articleService.updateArticle(articleVO);
        return Response.success();
    }

    /**
     * 修改状态
     */
    @PutMapping("/manage/article/update/status")
    @RequiresPermissions("article:update")
    @CacheEvict(allEntries = true)
    public Response updateArticleStatus(@RequestBody ArticleVO articleVO){
        articleService.updateArticleStatus(articleVO);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/article/delete")
    @RequiresPermissions("article:delete")
    @CacheEvict(allEntries = true)
    public Response deleteArticles(@RequestBody Integer[] ids) {
        if (ids == null || ids.length < 1){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能为空");
        }

        if (ids.length > 100){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "ids不能超过100个");
        }

        articleService.deleteArticles(ids);
        return Response.success();
    }

    /**
     * 删除缓存
     */
    @DeleteMapping("/manage/article/cache/refresh")
    @RequiresPermissions("article:cache:refresh")
    public Response flush() {
        Set<String> keys = redisTemplate.keys(RedisCacheNames.PROFIX + "*");
        redisTemplate.delete(keys);

        return Response.success();
    }

    /********************** portal ********************************/

    @GetMapping("/article/{articleId}")
    @LogView(module = 0)
    public Response getArticle(@PathVariable Integer articleId){
        ArticleDTO article = articleService.getArticleDTOById(articleId);
        return Response.success(article);
    }

    @PutMapping("/article/like/{id}")
    @LogView(module = 0)
    public Response likeArticle(@PathVariable Integer id) {
        return Response.success(articleService.likeArticle(id));
    }

    @GetMapping("/articles")
    @Cacheable
    @LogView(module = 0)
    public Response list(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                         @RequestParam("latest") Boolean latest, @RequestParam("categoryId") Integer categoryId,
                         @RequestParam("like") Boolean like, @RequestParam("read") Boolean read) {
        PageUtils queryPageCondition = articleService.queryPageCondition(page, limit, latest, categoryId, like, read);
        return Response.success(queryPageCondition);
    }

    /**
     * 分页获取首页列表
     * @param page 页码
     * @param limit 每页数量
     * @return 文章列表
     */
    @GetMapping("/articles/home")
    @Cacheable
    @LogView(module = 0)
    public Response listHome(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        PageUtils queryPageCondition = articleService.queryHomePageCondition(page, limit);
        return Response.success(queryPageCondition);
    }

    /**
     * 获取热读榜
     * @return 文章列表
     */
    @GetMapping("/articles/hotread")
    @Cacheable
    @LogView(module = 0)
    public Response getHotReadList(){
        return Response.success(articleService.getHotReadList());
    }

}
