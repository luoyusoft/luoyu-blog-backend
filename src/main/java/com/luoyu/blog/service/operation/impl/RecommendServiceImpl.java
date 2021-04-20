package com.luoyu.blog.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.constants.ModuleTypeConstants;
import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.entity.article.Article;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.operation.Recommend;
import com.luoyu.blog.entity.operation.vo.HomeRecommendInfoVO;
import com.luoyu.blog.entity.operation.vo.RecommendVO;
import com.luoyu.blog.entity.video.Video;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.mapper.operation.RecommendMapper;
import com.luoyu.blog.mapper.video.VideoMapper;
import com.luoyu.blog.service.cache.CacheServer;
import com.luoyu.blog.service.operation.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 推荐 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
@Service
@Slf4j
public class RecommendServiceImpl extends ServiceImpl<RecommendMapper, Recommend> implements RecommendService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private VideoMapper videoMapper;

    @Autowired
    private CacheServer cacheServer;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 获取首页信息
     * @return 首页信息
     */
    @Override
    public HomeRecommendInfoVO getHomeRecommendInfoVO() {
        HomeRecommendInfoVO homeRecommendInfoVO = new HomeRecommendInfoVO();
        homeRecommendInfoVO.setCount(baseMapper.selectCount());
        return homeRecommendInfoVO;
    }

    /**
     * 分页查询
     * @param page 页码
     * @param limit 每页数量
     * @return 推荐列表
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        IPage<Recommend> recommendPage = baseMapper.selectPage(new Query<Recommend>(params).getPage(),
                new QueryWrapper<Recommend>().orderByAsc("order_num"));
        recommendPage.getRecords().forEach(recommendPageItem -> {
            if (ModuleTypeConstants.ARTICLE.equals(recommendPageItem.getModule())){
                Article article = articleMapper.selectById(recommendPageItem.getLinkId());
                if (article != null){
                    recommendPageItem.setTitle(article.getTitle());
                }
            }
            if (ModuleTypeConstants.VIDEO.equals(recommendPageItem.getModule())){
                Video video = videoMapper.selectById(recommendPageItem.getLinkId());
                if (video != null){
                    recommendPageItem.setTitle(video.getTitle());
                }
            }
        });

        return new PageUtils(recommendPage);
    }

    /**
     * 获取推荐列表
     *
     * @return 推荐列表
     */
    @Override
    public List<RecommendVO> select(Integer module, String title) {
        List<RecommendVO> recommendVOList = new ArrayList<>();

        if (ModuleTypeConstants.ARTICLE.equals(module)){
            List<Article> articleList = articleMapper.selectArticleListByTitle(title);
            if (!CollectionUtils.isEmpty(articleList)){
                articleList.forEach(articleListItem -> {
                    RecommendVO recommendVO = new RecommendVO();
                    recommendVO.setTitle(articleListItem.getTitle());
                    recommendVO.setLinkId(articleListItem.getId());
                    recommendVO.setModule(module);
                    recommendVOList.add(recommendVO);
                });
            }
        }

        if (ModuleTypeConstants.VIDEO.equals(module)){
            List<Video> videoList = videoMapper.selectVideoListByTitle(title);
            if (!CollectionUtils.isEmpty(videoList)){
                videoList.forEach(videoListItem -> {
                    RecommendVO recommendVO = new RecommendVO();
                    recommendVO.setTitle(videoListItem.getTitle());
                    recommendVO.setLinkId(videoListItem.getId());
                    recommendVO.setModule(module);
                    recommendVOList.add(recommendVO);
                });
            }
        }

        return recommendVOList;
    }

    /**
     * 批量删除
     *
     * @param linkIds
     * @param module
     */
    @Override
    public void deleteRecommendsByLinkIdsAndType(List<Integer> linkIds, int module) {
        for (Integer linkId : linkIds) {
            baseMapper.delete(new QueryWrapper<Recommend>().lambda()
                    .eq(Recommend::getLinkId,linkId)
                    .eq(Recommend::getModule,module));
        }
        cleanRecommendAllCache();
    }

    /**
     * 新增
     * @param recommend
     */
    @Override
    public void insertRecommend(Recommend recommend) {
        if (baseMapper.selectRecommendByOrderNum(recommend.getOrderNum()) != null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该顺序已被占用");
        }
        if (ModuleTypeConstants.ARTICLE.equals(recommend.getModule())){
            Article article = articleMapper.selectById(recommend.getLinkId());
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }
            Recommend oldRecommend = baseMapper.selectRecommendByLinkIdAndType(recommend.getLinkId(), recommend.getModule());
            if(oldRecommend == null){
                baseMapper.insert(recommend);
            }else {
                baseMapper.updateRecommendOrderNumByLinkIdAndType(recommend);
            }
        }

        if (ModuleTypeConstants.VIDEO.equals(recommend.getModule())){
            Video video = videoMapper.selectById(recommend.getLinkId());
            if(video == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }
            Recommend oldRecommend = baseMapper.selectRecommendByLinkIdAndType(recommend.getLinkId(), recommend.getModule());
            if(oldRecommend == null){
                baseMapper.insert(recommend);
            }else {
                baseMapper.updateRecommendOrderNumByLinkIdAndType(recommend);
            }
        }
        cleanRecommendAllCache();
    }

    /**
     * 更新
     * @param recommend
     */
    @Override
    public void updateRecommend(Recommend recommend) {
        Recommend existRecommend = baseMapper.selectRecommendByOrderNum(recommend.getOrderNum());
        if (existRecommend != null && !existRecommend.getId().equals(recommend.getId())){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该顺序已被占用");
        }
        if (ModuleTypeConstants.ARTICLE.equals(recommend.getModule())){
            Article article = articleMapper.selectById(recommend.getLinkId());
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }
            Recommend oldRecommend = baseMapper.selectRecommendByLinkIdAndType(recommend.getLinkId(), recommend.getModule());
            if(oldRecommend == null){
                baseMapper.insert(recommend);
            }else {
                baseMapper.updateRecommendOrderNumByLinkIdAndType(recommend);
            }
        }

        if (ModuleTypeConstants.VIDEO.equals(recommend.getModule())){
            Video video = videoMapper.selectById(recommend.getLinkId());
            if(video == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }
            Recommend oldRecommend = baseMapper.selectRecommendByLinkIdAndType(recommend.getLinkId(), recommend.getModule());
            if(oldRecommend == null){
                baseMapper.insert(recommend);
            }else {
                baseMapper.updateRecommendOrderNumByLinkIdAndType(recommend);
            }
        }
        cleanRecommendAllCache();
    }

    /**
     * 推荐置顶
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRecommendTop(Integer id) {
        if(baseMapper.selectRecommendByOrderNum(Recommend.ORDER_NUM_TOP) != null){
            List<Recommend> recommends = baseMapper.selectRecommends();
            recommends.forEach(recommendsItem -> {
                recommendsItem.setOrderNum(recommendsItem.getOrderNum() + 1);
            });
            // 批量修改顺序，注意从大的开始，不然会有唯一索引冲突
            baseMapper.updateRecommendsOrderNumById(recommends);
        }
        if(!baseMapper.updateRecommendOrderNumById(Recommend.ORDER_NUM_TOP, id)){
            throw new MyException(ResponseEnums.UPDATE_FAILR.getCode(), "更新数据失败");
        }
        cleanRecommendAllCache();
    }

    /**
     * 删除
     * @param ids
     */
    @Override
    public void deleteRecommendsByIds(List<Integer> ids) {
        for (Integer id : ids) {
            baseMapper.delete(new QueryWrapper<Recommend>().lambda()
                    .eq(Recommend::getId, id));
        }
        cleanRecommendAllCache();
    }

    /**
     * 查找
     *
     * @param linkId
     * @param module
     */
    @Override
    public Recommend selectRecommendByLinkIdAndType(Integer linkId, Integer module) {
        return baseMapper.selectRecommendByLinkIdAndType(linkId, module);
    }

    /**
     * 查找最大顺序
     */
    @Override
    public Integer selectRecommendMaxOrderNum() {
        return baseMapper.selectRecommendMaxOrderNum();
    }

    /**
     * 清除缓存
     */
    private void cleanRecommendAllCache(){
        taskExecutor.execute(() ->{
            cacheServer.cleanRecommendAllCache();
        });
    }

    /********************** portal ********************************/

    @Cacheable(value = RedisKeyConstants.RECOMMENDS, key = "#module")
    @Override
    public List<RecommendVO> listRecommendVO(Integer module) {
        List<RecommendVO> recommendList =baseMapper.listRecommendDTO(module);
        if (CollectionUtils.isEmpty(recommendList)){
            return Collections.emptyList();
        }
        return genRecommendList(recommendList);
    }

    private List<RecommendVO> genRecommendList(List<RecommendVO> recommendList) {
        recommendList.forEach(recommendVO -> {
            if(ModuleTypeConstants.ARTICLE.equals(recommendVO.getModule())){
                ArticleDTO simpleArticleDTO = articleMapper.getSimpleArticleDTO(recommendVO.getLinkId());
                if (simpleArticleDTO != null){
                    BeanUtils.copyProperties(simpleArticleDTO, recommendVO);
                }
            }
            if(ModuleTypeConstants.VIDEO.equals(recommendVO.getModule())){
                VideoDTO simpleVideoDTO = videoMapper.getSimpleVideoDTO(recommendVO.getLinkId());
                if (simpleVideoDTO != null){
                    BeanUtils.copyProperties(simpleVideoDTO, recommendVO);
                }
            }
        });
        return recommendList;
    }

}
