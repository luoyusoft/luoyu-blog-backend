package com.luoyu.blogmanage.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blogmanage.common.constants.GitalkConstants;
import com.luoyu.blogmanage.common.constants.RabbitMqConstants;
import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.article.dto.ArticleDTO;
import com.luoyu.blogmanage.entity.article.vo.ArticleVO;
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

        Page<ArticleVO> articlePage = new Query<ArticleVO>(params).getPage();
        List<ArticleVO> articleList = baseMapper.listArticleVo(articlePage, params);
        // 查询所有分类
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().lambda().eq(Category::getType,ModuleEnum.ARTICLE.getCode()));
        // 封装ArticleVo
        Optional.ofNullable(articleList).ifPresent((articleVos ->
                articleVos.forEach(articleVo -> {
                // 设置类别
                articleVo.setCategoryListStr(categoryService.renderCategoryArr(articleVo.getCategoryId(),categoryList));
                // 设置标签列表
                articleVo.setTagList(tagService.listByLinkId(articleVo.getId(), ModuleEnum.ARTICLE.getCode()));
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
    public void saveArticle(ArticleDTO article) {
        baseMapper.insert(article);
        tagService.saveTagAndNew(article.getTagList(),article.getId(),ModuleEnum.ARTICLE.getCode());
        InitGitalkRequest initGitalkRequest = new InitGitalkRequest();
        initGitalkRequest.setId(article.getId());
        initGitalkRequest.setTitle(article.getTitle());
        initGitalkRequest.setType(GitalkConstants.GITALK_TYPE_ARTICLE);
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_GITALK_ROUTINGKEY_INIT, JsonUtils.objectToJson(initGitalkRequest));
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_ADD, JsonUtils.objectToJson(article));
        if (article.getRecommend()){
            Recommend recommend = new Recommend();
            int count = recommendMapper.selectCount();

            recommend.setLinkId(article.getId());
            recommend.setType(ModuleEnum.ARTICLE.getCode());
            recommend.setOrderNum(count + 1);
            recommend.setTop(article.getTop());
            recommend.setTitle(article.getTitle());
            recommend.setPublish(article.getPublish());
            recommendMapper.insert(recommend);
        }
    }

    /**
     * 更新博文
     *
     * @param article
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleDTO article) {
        // 删除多对多所属标签
        tagService.deleteTagLink(article.getId(),ModuleEnum.ARTICLE.getCode());
        // 更新所属标签
        tagService.saveTagAndNew(article.getTagList(),article.getId(), ModuleEnum.ARTICLE.getCode());
        // 更新博文
        baseMapper.updateArticle(article);
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_UPDATE, JsonUtils.objectToJson(article));

        if(article.getRecommend()) {
            recommendService.insertRecommend(article.getId(), ModuleEnum.ARTICLE.getCode());
        }else {
            Integer[] articleIds = {article.getId()};
            recommendService.deleteBatchByLinkIdsAndType(Arrays.asList(articleIds), ModuleEnum.ARTICLE.getCode());
        }
    }

    /**
     * 更新状态
     * @param article
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(Article article) {
        if(baseMapper.updateArticle(article)){
            if (article.getPublish() == null){
                if(article.getRecommend()){
                    recommendService.insertRecommend(article.getId(), ModuleEnum.ARTICLE.getCode());
                }else {
                    Integer[] articleIds = {article.getId()};
                    recommendService.deleteBatchByLinkIdsAndType(Arrays.asList(articleIds), ModuleEnum.ARTICLE.getCode());
                }
            }
        }

    }

    /**
     * 获取文章对象
     *
     * @param articleId
     * @return
     */
    @Override
    public ArticleDTO getArticle(Integer articleId) {
        ArticleDTO articleDto = new ArticleDTO();
        Article article = this.baseMapper.selectById(articleId);
        BeanUtils.copyProperties(article,articleDto);
        // 查询所属标签
        articleDto.setTagList(tagService.listByLinkId(articleId,ModuleEnum.ARTICLE.getCode()));
        return articleDto;
    }

    @Override
    public boolean checkByCategory(Integer categoryId) {
        return baseMapper.checkByCategory(categoryId) > 0;
    }

    /**
     * 批量删除
     *
     * @param articleIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Integer[] articleIds) {
        //先删除博文标签多对多关联
        Arrays.stream(articleIds).forEach(articleId -> {
            tagService.deleteTagLink(articleId,ModuleEnum.ARTICLE.getCode());
        });
        this.baseMapper.deleteBatchIds(Arrays.asList(articleIds));

        recommendService.deleteBatchByLinkIdsAndType(Arrays.asList(articleIds), ModuleEnum.ARTICLE.getCode());

        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_DELETE, JsonUtils.objectToJson(articleIds));
    }

}
