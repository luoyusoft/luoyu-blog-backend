package com.luoyu.blog.project.service.portal.article.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.entity.article.Article;
import com.luoyu.blog.common.entity.article.vo.ArticleVO;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.project.service.manage.operation.TagService;
import com.luoyu.blog.project.service.portal.article.ArticleService;
import com.luoyu.blog.project.mapper.manage.article.ArticleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文章 服务实现类
 * </p>
 *
 * @author bobbi
 * @since 2018-11-07
 */
@Service("ArticlePortalService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    private TagService tagService;

    /**
     * 分页分类获取列表
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageCondition(Map<String, Object> params) {
        Page<ArticleVO> page = new Query<ArticleVO>(params).getPage();
        List<ArticleVO> articleList = baseMapper.queryPageCondition(page, params);
        // 封装ArticleVo
        page.setRecords(articleList);
        return new PageUtils(page);
    }

    /**
     * 获取ArticleVo对象
     *
     * @param articleId
     * @return
     */
    @Override
    public ArticleVO getArticleVo(Integer articleId) {
        Article article = baseMapper.selectById(articleId);
        ArticleVO articleVo = new ArticleVO();
        BeanUtils.copyProperties(article,articleVo);
        articleVo.setTagList(tagService.listByLinkId(articleId, ModuleEnum.ARTICLE.getValue()));
        return articleVo;
    }

    /**
     * 获取简单的Article对象
     * @param articleId
     * @return
     */
    @Override
    public ArticleVO getSimpleArticleVo(Integer articleId) {
        ArticleVO articleVo = baseMapper.getSimpleArticleVo(articleId);
        articleVo.setTagList(tagService.listByLinkId(articleId,ModuleEnum.ARTICLE.getValue()));
        return articleVo;
    }

}
