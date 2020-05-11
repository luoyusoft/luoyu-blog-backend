package com.jinhaoxun.blog.project.tt.service.applyservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhaoxun.blog.project.tt.pojo.apply.Article;
import com.jinhaoxun.accommon.pojo.apply.request.*;
import com.jinhaoxun.blog.common.response.ResponseResult;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 文章服务接口
 */
public interface IArticleService extends IService<Article> {

    /**
     * @author jinhaoxun
     * @description 获取文章列表
     * @param getArticleListReq 筛选条件参数
     * @return ResponseResult 获取到的文章列表
     */
    ResponseResult getArticleList(GetArticleListReq getArticleListReq);

    /**
     * @author jinhaoxun
     * @description 新增文章
     * @param addArticleReq 文章信息参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    ResponseResult addArticle(AddArticleReq addArticleReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 删除文章
     * @param deleteArticleReq 删除文章请求参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    ResponseResult deleteArticle(DeleteArticleReq deleteArticleReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 更新文章
     * @param updateArticleReq 更新文章请求参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    ResponseResult updateArticle(UpdateArticleReq updateArticleReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 获取文章信息
     * @param getArticleReq 获取文章请求参数
     * @return ResponseResult 是否获取成功
     * @throws Exception
     */
    ResponseResult getArticle(GetArticleReq getArticleReq) throws Exception;

}
