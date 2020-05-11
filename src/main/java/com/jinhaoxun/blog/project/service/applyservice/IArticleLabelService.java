package com.jinhaoxun.blog.project.tt.service.applyservice;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinhaoxun.blog.project.tt.pojo.apply.ArticleLabel;
import com.jinhaoxun.blog.project.tt.pojo.apply.request.UpdateArticleReq;
import com.jinhaoxun.blog.common.response.ResponseResult;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 文章标签服务接口
 */
public interface IArticleLabelService extends IService<ArticleLabel> {

    /**
     * @author jinhaoxun
     * @description 新增文章标签
     * @param articleLabel 文章信息参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    ResponseResult addArticleLabel(ArticleLabel articleLabel) throws Exception;

    /**
     * @author jinhaoxun
     * @description 删除文章标签
     * @param articleId 文章id
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    ResponseResult deleteArticleLabelByArticleId(Long articleId) throws Exception;

    /**
     * @author jinhaoxun
     * @description 更新文章标签
     * @param updateArticleReq 更新的文章信息参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    ResponseResult updateArticleLabel(UpdateArticleReq updateArticleReq) throws Exception;

    /**
     * @author jinhaoxun
     * @description 获取文章标签
     * @param articleId 文章id
     * @return ResponseResult 获取的文章标签
     * @throws Exception
     */
    ResponseResult getArticleLabelByArticleId(Long articleId) throws Exception;

}
