package com.luoyu.blog.project.service.applyservice.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.constant.AbstractConstant;
import com.luoyu.blog.common.exception.ExceptionFactory;
import com.luoyu.blog.project.mapper.applymapper.ArticleLabelMapper;
import com.luoyu.blog.project.service.applyservice.IArticleLabelService;
import com.luoyu.blog.project.pojo.apply.ArticleLabel;
import com.luoyu.blog.project.pojo.apply.request.UpdateArticleReq;
import com.luoyu.blog.common.response.ResponseFactory;
import com.luoyu.blog.common.response.ResponseMsg;
import com.luoyu.blog.common.response.ResponseResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 文章标签服务类
 */
@Service
public class ArticleLabelServiceImpl extends ServiceImpl<ArticleLabelMapper, ArticleLabel> implements IArticleLabelService {

    @Resource
    private ArticleLabelMapper articleLabelMapper;
    @Resource
    private ExceptionFactory exceptionFactory;

    /**
     * @author jinhaoxun
     * @description 新增文章标签
     * @param articleLabel 文章信息参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @Override
    public ResponseResult addArticleLabel(ArticleLabel articleLabel) throws Exception {
        int count = articleLabelMapper.insert(articleLabel);
        if(count != 1){
            throw exceptionFactory.build(ResponseMsg.ARTICLE_LABEL_ADD_FAIL.getCode(),ResponseMsg.ARTICLE_LABEL_ADD_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse("新增成功！");
    }

    /**
     * @author jinhaoxun
     * @description 删除文章标签
     * @param articleId 文章id
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @Override
    public ResponseResult deleteArticleLabelByArticleId(Long articleId) throws Exception {
        int count = articleLabelMapper.deleteLabelByArticleId(articleId);
        if(count != 1){
            throw exceptionFactory.build(ResponseMsg.ARTICLE_LABEL_DELETE_FAIL.getCode(),ResponseMsg.ARTICLE_LABEL_DELETE_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse("删除成功！");
    }

    /**
     * @author jinhaoxun
     * @description 更新文章标签
     * @param updateArticleReq 更新的文章信息参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @Override
    public ResponseResult updateArticleLabel(UpdateArticleReq updateArticleReq) throws Exception {
        UpdateWrapper<ArticleLabel> uw = new UpdateWrapper<>();
        Date date = new Date();
        ArticleLabel articleLabel = new ArticleLabel();
        articleLabel.setSecondaryCode(updateArticleReq.getSecondaryLabelCode());
        articleLabel.setPrimaryCode(updateArticleReq.getPrimaryLabelCode());
        articleLabel.setUpdateTime(date);
        articleLabel.setUpdaterId(updateArticleReq.getUpdaterId());
        uw.eq(AbstractConstant.ARTICLE_ID,updateArticleReq.getArticleId());
        int count = articleLabelMapper.update(articleLabel,uw);
        if(count != 1){
            throw exceptionFactory.build(ResponseMsg.ARTICLE_LABEL_UPDATE_FAIL.getCode(),ResponseMsg.ARTICLE_LABEL_UPDATE_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse("更新成功！");
    }

    /**
     * @author jinhaoxun
     * @description 获取文章标签
     * @param articleId 文章id
     * @return ResponseResult 获取的文章标签
     * @throws Exception
     */
    @Override
    public ResponseResult getArticleLabelByArticleId(Long articleId) throws Exception {
        ArticleLabel articleLabel = articleLabelMapper.getLabelByArticleId(articleId);
        if(articleLabel == null){
            throw exceptionFactory.build(ResponseMsg.ARTICLE_LABEL_GET_FAIL.getCode(),ResponseMsg.ARTICLE_LABEL_GET_FAIL.getMsg());
        }
        return ResponseFactory.buildSuccessResponse(articleLabel,"获取成功！");
    }
}
