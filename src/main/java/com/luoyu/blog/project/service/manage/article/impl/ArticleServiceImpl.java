package com.luoyu.blog.project.service.manage.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.constants.GitalkConstants;
import com.luoyu.blog.common.constants.RabbitMqConstants;
import com.luoyu.blog.common.entity.article.Article;
import com.luoyu.blog.common.entity.article.dto.ArticleDTO;
import com.luoyu.blog.common.entity.article.vo.ArticleVO;
import com.luoyu.blog.common.entity.gitalk.InitGitalkRequest;
import com.luoyu.blog.common.entity.operation.Category;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.common.util.RabbitMqUtils;
import com.luoyu.blog.project.service.manage.article.ArticleService;
import com.luoyu.blog.project.service.manage.operation.CategoryService;
import com.luoyu.blog.project.service.manage.operation.TagService;
import com.luoyu.blog.project.mapper.article.ArticleMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * articleAdminServiceImpl
 *
 * @author bobbi
 * @date 2018/11/21 12:48
 * @email 571002217@qq.com
 * @description
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Resource
    private RabbitMqUtils rabbitmqUtils;

    /**
     * 分页查询博文列表
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<ArticleVO> page = new Query<ArticleVO>(params).getPage();
        List<ArticleVO> articleList = baseMapper.listArticleVo(page, params);
        // 查询所有分类
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().lambda().eq(Category::getType,ModuleEnum.ARTICLE.getValue()));
        // 封装ArticleVo
        Optional.ofNullable(articleList).ifPresent((articleVos ->
                articleVos.forEach(articleVo -> {
                // 设置类别
                articleVo.setCategoryListStr(categoryService.renderCategoryArr(articleVo.getCategoryId(),categoryList));
                // 设置标签列表
                articleVo.setTagList(tagService.listByLinkId(articleVo.getId(), ModuleEnum.ARTICLE.getValue()));
            })));
        page.setRecords(articleList);
        return new PageUtils(page);
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
        tagService.saveTagAndNew(article.getTagList(),article.getId(),ModuleEnum.ARTICLE.getValue());
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
    public void updateArticle(ArticleDTO article) {
        // 删除多对多所属标签
        tagService.deleteTagLink(article.getId(),ModuleEnum.ARTICLE.getValue());
        // 更新所属标签
        tagService.saveTagAndNew(article.getTagList(),article.getId(), ModuleEnum.ARTICLE.getValue());
        // 更新博文
        baseMapper.updateById(article);
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_UPDATE, JsonUtils.objectToJson(article));
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
        articleDto.setTagList(tagService.listByLinkId(articleId,ModuleEnum.ARTICLE.getValue()));
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
            tagService.deleteTagLink(articleId,ModuleEnum.ARTICLE.getValue());
        });
        this.baseMapper.deleteBatchIds(Arrays.asList(articleIds));
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_DELETE, JsonUtils.objectToJson(articleIds));
    }

}
