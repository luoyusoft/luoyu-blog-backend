package com.luoyu.blog.service.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.constants.GitalkConstants;
import com.luoyu.blog.common.constants.RabbitMQConstants;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.common.util.RabbitMQUtils;
import com.luoyu.blog.entity.article.Article;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.article.vo.ArticleVO;
import com.luoyu.blog.entity.gitalk.InitGitalkRequest;
import com.luoyu.blog.entity.operation.Category;
import com.luoyu.blog.entity.operation.Recommend;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.service.article.ArticleService;
import com.luoyu.blog.service.operation.CategoryService;
import com.luoyu.blog.service.operation.RecommendService;
import com.luoyu.blog.service.operation.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
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

    @Resource
    private RabbitMQUtils rabbitmqUtils;

    /**
     * 分页查询文章列表
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

        Page<ArticleDTO> articleDTOPage = new Query<ArticleDTO>(params).getPage();
        List<ArticleDTO> articleDTOList = baseMapper.listArticleDTO(articleDTOPage, params);
        // 查询所有分类
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().lambda().eq(Category::getModule,ModuleEnum.ARTICLE.getCode()));
        // 封装ArticleVo
        List<ArticleVO> articleVOList = new ArrayList<>();
        Optional.ofNullable(articleDTOList).ifPresent((articleDTOs ->
                articleDTOs.forEach(articleDTO -> {
                    // 设置类别
                    articleDTO.setCategoryListStr(categoryService.renderCategoryArr(articleDTO.getCategoryId(), categoryList));
                    // 设置标签列表
                    articleDTO.setTagList(tagService.listByLinkId(articleDTO.getId(), ModuleEnum.ARTICLE.getCode()));

                    ArticleVO articleVO = new ArticleVO();
                    BeanUtils.copyProperties(articleDTO, articleVO);
                    if (recommendService.selectRecommendByLinkIdAndType(articleDTO.getId(), ModuleEnum.ARTICLE.getCode()) != null) {
                        articleVO.setRecommend(true);
                    } else {
                        articleVO.setRecommend(false);
                    }
                    articleVOList.add(articleVO);
                })));
        Page<ArticleVO> articleVOPage = new Page<>();
        BeanUtils.copyProperties(articleDTOPage, articleVOPage);
        articleVOPage.setRecords(articleVOList);
        return new PageUtils(articleVOPage);
    }

    /**
     * 保存文章
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
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_GITALK_INIT_ROUTINGKEY, JsonUtils.objectToJson(initGitalkRequest));
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_ARTICLE_ADD_ROUTINGKEY, JsonUtils.objectToJson(article));
    }

    /**
     * 更新文章
     *
     * @param articleVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleVO articleVO) {
        // 删除多对多所属标签
        tagService.deleteTagLink(articleVO.getId(),ModuleEnum.ARTICLE.getCode());
        // 更新所属标签
        tagService.saveTagAndNew(articleVO.getTagList(),articleVO.getId(), ModuleEnum.ARTICLE.getCode());
        // 更新博文
        baseMapper.updateArticleById(articleVO);
        if (articleVO.getRecommend() != null){
            if (articleVO.getRecommend()){
                if (recommendService.selectRecommendByLinkIdAndType(articleVO.getId(), ModuleEnum.ARTICLE.getCode()) == null){
                    Integer maxOrderNum = recommendService.selectRecommendMaxOrderNum();
                    Recommend recommend = new Recommend();
                    LocalDateTime now = LocalDateTime.now();
                    recommend.setModule(ModuleEnum.ARTICLE.getCode());
                    recommend.setLinkId(articleVO.getId());
                    recommend.setOrderNum(maxOrderNum + 1);
                    recommend.setCreateTime(now);
                    recommend.setUpdateTime(now);
                    recommendService.insertRecommend(recommend);
                }
            }else {
                if (recommendService.selectRecommendByLinkIdAndType(articleVO.getId(), ModuleEnum.ARTICLE.getCode()) != null){
                    recommendService.deleteRecommendsByLinkIdsAndType(Arrays.asList(articleVO.getId()), ModuleEnum.ARTICLE.getCode());
                }
            }
        }
        // 发送rabbitmq消息同步到es
        InitGitalkRequest initGitalkRequest = new InitGitalkRequest();
        initGitalkRequest.setId(articleVO.getId());
        initGitalkRequest.setTitle(articleVO.getTitle());
        initGitalkRequest.setType(GitalkConstants.GITALK_TYPE_ARTICLE);
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_GITALK_INIT_ROUTINGKEY, JsonUtils.objectToJson(initGitalkRequest));
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_ARTICLE_UPDATE_ROUTINGKEY, JsonUtils.objectToJson(articleVO));
    }

    /**
     * 更新文章状态
     *
     * @param articleVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticleStatus(ArticleVO articleVO) {
        if (articleVO.getPublish() != null){
            // 更新发布状态
            baseMapper.updateArticleById(articleVO);
            if (articleVO.getPublish()){
                rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_ARTICLE_ADD_ROUTINGKEY, JsonUtils.objectToJson(baseMapper.selectArticleById(articleVO.getId())));
            }else {
                Integer[] ids = {articleVO.getId()};
                rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_ARTICLE_DELETE_ROUTINGKEY, JsonUtils.objectToJson(ids));
            }
        }
        if (articleVO.getRecommend() != null){
            // 更新推荐状态
            if (articleVO.getRecommend()){
                if (recommendService.selectRecommendByLinkIdAndType(articleVO.getId(), ModuleEnum.ARTICLE.getCode()) == null){
                    Integer maxOrderNum = recommendService.selectRecommendMaxOrderNum();
                    Recommend recommend = new Recommend();
                    LocalDateTime now = LocalDateTime.now();
                    recommend.setModule(ModuleEnum.ARTICLE.getCode());
                    recommend.setLinkId(articleVO.getId());
                    recommend.setOrderNum(maxOrderNum + 1);
                    recommend.setCreateTime(now);
                    recommend.setUpdateTime(now);
                    recommendService.insertRecommend(recommend);
                }
            }else {
                if (recommendService.selectRecommendByLinkIdAndType(articleVO.getId(), ModuleEnum.ARTICLE.getCode()) != null){
                    recommendService.deleteRecommendsByLinkIdsAndType(Arrays.asList(articleVO.getId()), ModuleEnum.ARTICLE.getCode());
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
    public ArticleVO getArticle(Integer articleId) {
        ArticleVO articleVO = new ArticleVO();
        Article article = baseMapper.selectById(articleId);
        BeanUtils.copyProperties(article, articleVO);
        // 查询所属标签
        articleVO.setTagList(tagService.listByLinkId(articleId, ModuleEnum.ARTICLE.getCode()));
        Recommend recommend = recommendService.selectRecommendByLinkIdAndType(articleId, ModuleEnum.ARTICLE.getCode());
        if (recommend != null){
            articleVO.setRecommend(true);
        }else {
            articleVO.setRecommend(false);
        }
        return articleVO;
    }

    /**
     * 判断类别下是否有文章
     * @param categoryId
     * @return
     */
    @Override
    public boolean checkByCategory(Integer categoryId) {
        return baseMapper.checkByCategory(categoryId) > 0;
    }

    /**
     * 判断上传文件下是否有文章
     * @param url
     * @return
     */
    @Override
    public boolean checkByFile(String url) {
        return baseMapper.checkByFile(url) > 0;
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
        baseMapper.deleteBatchIds(Arrays.asList(ids));

        recommendService.deleteRecommendsByLinkIdsAndType(Arrays.asList(ids), ModuleEnum.ARTICLE.getCode());
        // 发送rabbitmq消息同步到es
        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_ARTICLE_DELETE_ROUTINGKEY, JsonUtils.objectToJson(ids));
    }

    /********************** portal ********************************/

    /**
     * 分页分类获取列表
     *
     * @param page
     * @param limit
     * @param latest
     * @param categoryId
     * @param like
     * @param read
     * @return
     */
    @Override
    public PageUtils queryPageCondition(Integer page, Integer limit, Boolean latest, Integer categoryId, Boolean like, Boolean read) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        params.put("latest", latest);
        params.put("like", like);
        params.put("read", read);
        if (categoryId != null){
            params.put("categoryId", String.valueOf(categoryId));
        }

        Page<ArticleDTO> articleDTOPage = new Query<ArticleDTO>(params).getPage();
        List<ArticleDTO> articleList = baseMapper.queryPageCondition(articleDTOPage, params);
        if (articleList == null){
            articleList = new ArrayList<>();
        }
        // 封装ArticleVo
        articleDTOPage.setRecords(articleList);
        return new PageUtils(articleDTOPage);
    }

    /**
     * 获取ArticleVo对象
     *
     * @param articleId
     * @return
     */
    @Override
    public ArticleDTO getArticleDTOById(Integer articleId) {
        Article article = baseMapper.selectArticleById(articleId);
        if (article == null){
            return null;
        }
        ArticleDTO articleDTO = new ArticleDTO();
        BeanUtils.copyProperties(article,articleDTO);
        articleDTO.setTagList(tagService.listByLinkId(articleId, ModuleEnum.ARTICLE.getCode()));
        // 浏览数量
        baseMapper.updateReadNum(articleId);
        return articleDTO;
    }

    /**
     * 获取热读榜
     * @return
     */
    @Override
    public List<ArticleVO> getHotReadList() {
        List<ArticleDTO> articleDTOList = baseMapper.getHotReadList();
        List<ArticleVO> articleVOList = new ArrayList<>();
        articleDTOList.forEach(articleDTOListItem -> {
            ArticleVO articleVO = new ArticleVO();
            BeanUtils.copyProperties(articleDTOListItem, articleVO);
            articleVOList.add(articleVO);
        });
        return articleVOList;
    }

    @Override
    public Boolean likeArticle(Integer id) {
        return baseMapper.updateLikeNum(id);
    }

}
