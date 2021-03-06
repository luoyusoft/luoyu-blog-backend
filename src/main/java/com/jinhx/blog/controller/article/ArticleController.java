package com.jinhx.blog.controller.article;

import com.jinhx.blog.common.aop.annotation.LogView;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.validator.ValidatorUtils;
import com.jinhx.blog.common.validator.group.AddGroup;
import com.jinhx.blog.entity.article.vo.ArticleVO;
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
        return Response.success(articleService.getHomeArticleInfoVO());
    }

    /**
     * 列表
     */
    @GetMapping("/manage/article/list")
    @RequiresPermissions("article:list")
    public Response listArticle(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title) {
        return Response.success(articleService.queryPage(page, limit, title));
    }

    /**
     * 信息
     */
    @GetMapping("/manage/article/info/{articleId}")
    @RequiresPermissions("article:list")
    public Response info(@PathVariable("articleId") Integer articleId) {
        return Response.success(articleService.getArticle(articleId));
    }

    /**
     * 保存文章
     * @param articleVO 文章信息
     */
    @PostMapping("/manage/article/save")
    @RequiresPermissions("article:save")
    public Response saveArticle(@RequestBody ArticleVO articleVO){
        ValidatorUtils.validateEntity(articleVO, AddGroup.class);
        articleService.saveArticle(articleVO);

        return Response.success();
    }

    /**
     * 更新文章
     * @param articleVO 文章信息
     */
    @PutMapping("/manage/article/update")
    @RequiresPermissions("article:update")
    public Response updateArticle(@RequestBody ArticleVO articleVO){
        articleService.updateArticle(articleVO);
        return Response.success();
    }

    /**
     * 更新文章状态
     * @param articleVO 文章信息
     */
    @PutMapping("/manage/article/update/status")
    @RequiresPermissions("article:update")
    public Response updateArticleStatus(@RequestBody ArticleVO articleVO){
        articleService.updateArticleStatus(articleVO);
        return Response.success();
    }

    /**
     * 批量删除
     * @param ids 文章id列表
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
        return Response.success(articleService.listArticles(page, limit, latest, categoryId, like, read));
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
        return Response.success(articleService.listHomeArticles(page, limit));
    }

    /**
     * 获取ArticleDTO对象
     * @param id id
     * @param password password
     * @return ArticleDTO
     */
    @GetMapping("/article/{id}")
    @LogView(module = 0)
    public Response getArticle(@PathVariable Integer id, @RequestParam(value = "password", required = false, defaultValue = "") String password){
        return Response.success(articleService.getArticleDTO(id, password));
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
     * 获取热读榜
     * @return 热读文章列表
     */
    @GetMapping("/article/listhotreadarticles")
    @LogView(module = 0)
    public Response listHotReadArticles(){
        return Response.success(articleService.listHotReadArticles());
    }

    /**
     * 根据文章id获取公开状态
     * @param id 文章id
     * @return 公开状态
     */
    @GetMapping("/article/open")
    @LogView(module = 0)
    public Response getArticleOpenById(@RequestParam("id") Integer id){
        return Response.success(articleService.getArticleOpenById(id));
    }

}
