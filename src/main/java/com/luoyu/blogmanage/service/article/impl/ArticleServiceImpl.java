package com.luoyu.blogmanage.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blogmanage.common.constants.GitalkConstants;
import com.luoyu.blogmanage.common.constants.RabbitMqConstants;
import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.article.vo.ArticleVO;
import com.luoyu.blogmanage.entity.article.dto.ArticleDTO;
import com.luoyu.blogmanage.entity.gitalk.InitGitalkRequest;
import com.luoyu.blogmanage.entity.operation.Category;
import com.luoyu.blogmanage.common.enums.ModuleEnum;
import com.luoyu.blogmanage.common.util.JsonUtils;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.util.Query;
import com.luoyu.blogmanage.common.util.RabbitMqUtils;
import com.luoyu.blogmanage.entity.operation.Recommend;
import com.luoyu.blogmanage.mapper.article.ArticleMapper;
import com.luoyu.blogmanage.mapper.operation.RecommendMapper;
import com.luoyu.blogmanage.service.article.ArticleService;
import com.luoyu.blogmanage.service.operation.CategoryService;
import com.luoyu.blogmanage.service.operation.RecommendService;
import com.luoyu.blogmanage.service.operation.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * articleAdminServiceImpl
 *
 * @author luoyu
 * @date 2018/11/21 12:48
 * @description
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RecommendService recommendService;

    @Autowired
    private RecommendMapper recommendMapper;

    @Resource
    private RabbitMqUtils rabbitmqUtils;

    /**
     * 分页查询博文列表
     *
     * @param page
     * @param limit
     * @param title
     * @return
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit, String title) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("title", title);

        Page<ArticleDTO> articlePage = new Query<ArticleDTO>(params).getPage();
        List<ArticleDTO> articleList = baseMapper.listArticleVo(articlePage, params);
        // 查询所有分类
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().lambda().eq(Category::getType,ModuleEnum.ARTICLE.getCode()));
        // 封装ArticleVo
        Optional.ofNullable(articleList).ifPresent((articleVos ->
                articleVos.forEach(articleDTO -> {
                // 设置类别
                articleDTO.setCategoryListStr(categoryService.renderCategoryArr(articleDTO.getCategoryId(),categoryList));
                // 设置标签列表
                articleDTO.setTagList(tagService.listByLinkId(articleDTO.getId(), ModuleEnum.ARTICLE.getCode()));
            })));
        articlePage.setRecords(articleList);
        return new PageUtils(articlePage);
    }

    /**
     * 保存博文文章
     *
     * @param article
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveArticle(ArticleVO article) {
        baseMapper.insert(article);
        tagService.saveTagAndNew(article.getTagList(),article.getId(),ModuleEnum.ARTICLE.getCode());
        InitGitalkRequest initGitalkRequest = new InitGitalkRequest();
        initGitalkRequest.setId(article.getId());
        initGitalkRequest.setTitle(article.getTitle());
        initGitalkRequest.setType(GitalkConstants.GITALK_TYPE_ARTICLE);
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_GITALK_ROUTINGKEY_INIT, JsonUtils.objectToJson(initGitalkRequest));
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_ADD, JsonUtils.objectToJson(article));
    }

    /**
     * 更新博文
     *
     * @param article
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleVO article) {
        // 删除多对多所属标签
        tagService.deleteTagLink(article.getId(),ModuleEnum.ARTICLE.getCode());
        // 更新所属标签
        tagService.saveTagAndNew(article.getTagList(),article.getId(), ModuleEnum.ARTICLE.getCode());
        // 更新博文
        baseMapper.updateArticleById(article);
        // 发送rabbitmq消息同步到es
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_UPDATE, JsonUtils.objectToJson(article));
    }

    /**
     * 获取文章对象
     *
     * @param articleId
     * @return
     */
    @Override
    public ArticleVO getArticle(Integer articleId) {
        ArticleVO articleVO = new ArticleVO();
        Article article = this.baseMapper.selectById(articleId);
        BeanUtils.copyProperties(article, articleVO);
        // 查询所属标签
        articleVO.setTagList(tagService.listByLinkId(articleId,ModuleEnum.ARTICLE.getCode()));
        return articleVO;
    }

    @Override
    public boolean checkByCategory(Integer categoryId) {
        return baseMapper.checkByCategory(categoryId) > 0;
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArticles(Integer[] ids) {
        //先删除博文标签多对多关联
        Arrays.stream(ids).forEach(articleId -> {
            tagService.deleteTagLink(articleId, ModuleEnum.ARTICLE.getCode());
        });
        this.baseMapper.deleteBatchIds(Arrays.asList(ids));

        recommendService.deleteRecommendsByLinkIdsAndType(Arrays.asList(ids), ModuleEnum.ARTICLE.getCode());
        // 发送rabbitmq消息同步到es
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_DELETE, JsonUtils.objectToJson(ids));
    }

}
