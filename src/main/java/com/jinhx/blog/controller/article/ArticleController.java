package com.jinhx.blog.controller.article;

import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.aop.annotation.LogView;
import com.jinhx.blog.common.validator.ValidatorUtils;
import com.jinhx.blog.common.validator.group.AddGroup;
import com.jinhx.blog.entity.article.dto.ArticleDTO;
import com.jinhx.blog.entity.article.vo.ArticleVO;
import com.jinhx.blog.entity.article.vo.HomeArticleInfoVO;
import com.jinhx.blog.entity.base.Response;
import com.jinhx.blog.service.article.ArticleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * ArticleController
 * @author jinhx
 * @date 2018/11/20 20:25
 * @description
 */
@RestController
public class ArticleController {

    @Resource
    private ArticleService articleService;

    /**
     * 获取首页信息
     */
    @GetMapping("/manage/article/homeinfo")
    @RequiresPermissions("article:list")
    public Response getHomeArticleInfoVO() {
        HomeArticleInfoVO homeArticleInfoVO = articleService.getHomeArticleInfoVO();
        return Response.success(homeArticleInfoVO);
    }

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
        ArticleVO articleVO = articleService.getArticle(articleId);
        return Response.success(articleVO);
    }

    /**
     * 保存
     */
    @PostMapping("/manage/article/save")
    @RequiresPermissions("article:save")
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
    public Response updateArticle(@RequestBody ArticleVO articleVO){
        articleService.updateArticle(articleVO);
        return Response.success();
    }

    /**
     * 修改状态
     */
    @PutMapping("/manage/article/update/status")
    @RequiresPermissions("article:update")
    public Response updateArticleStatus(@RequestBody ArticleVO articleVO){
        articleService.updateArticleStatus(articleVO);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/manage/article/delete")
    @RequiresPermissions("article:delete")
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

    /********************** portal ********************************/

    /**
     * 获取ArticleDTO对象
     * @param id id
     * @return ArticleDTO
     */
    @GetMapping("/article/{id}")
    @LogView(module = 0)
    public Response getArticle(@PathVariable Integer id){
        ArticleDTO article = articleService.getArticleDTO(id);
        return Response.success(article);
    }

    /**
     * 文章点赞
     * @param id id
     * @return 点赞结果
     */
    @PutMapping("/article/{id}")
    @LogView(module = 0)
    public Response updateArticle(@PathVariable Integer id) throws Exception {
        if (id == null) {
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "id不能为空");
        }
        return Response.success(articleService.updateArticle(id));
    }

    /**
     * 分页获取文章列表
     * @param page 页码
     * @param limit 每页数量
     * @param categoryId 分类
     * @param latest 时间排序
     * @param like 点赞量排序
     * @param read 阅读量排序
     * @return 文章列表
     */
    @GetMapping("/article/listarticles")
    @LogView(module = 0)
    public Response listArticles(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit,
                         @RequestParam("latest") Boolean latest, @RequestParam("categoryId") Integer categoryId,
                         @RequestParam("like") Boolean like, @RequestParam("read") Boolean read) {
        PageUtils queryPageCondition = articleService.listArticles(page, limit, latest, categoryId, like, read);
        return Response.success(queryPageCondition);
    }

    /**
     * 分页获取首页文章列表
     * @param page 页码
     * @param limit 每页数量
     * @return 首页文章列表
     */
    @GetMapping("/article/listhomearticles")
    @LogView(module = 0)
    public Response listHomeArticles(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        PageUtils queryPageCondition = articleService.listHomeArticles(page, limit);
        return Response.success(queryPageCondition);
    }

    /**
     * 获取热读榜
     * @return 热读文章列表
     */
    @GetMapping("/article/listhotreadarticles")
    @LogView(module = 0)
    public Response listHotReadArticles(){
        return Response.success(articleService.listHotReadArticles());
    }

}
