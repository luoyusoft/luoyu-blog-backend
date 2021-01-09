package com.luoyu.blog.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.enums.ResponseEnums;
import com.luoyu.blog.common.exception.MyException;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.entity.article.Article;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.operation.Recommend;
import com.luoyu.blog.entity.operation.vo.RecommendVO;
import com.luoyu.blog.entity.video.Video;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.mapper.operation.RecommendMapper;
import com.luoyu.blog.mapper.video.VideoMapper;
import com.luoyu.blog.service.operation.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private VideoMapper videoMapper;

    /**
     * 分页查询
     * @param page
     * @param limit
     * @return
     */
    @Override
    public PageUtils queryPage(Integer page, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        IPage<Recommend> recommendPage = baseMapper.selectPage(new Query<Recommend>(params).getPage(),
                new QueryWrapper<Recommend>().orderByAsc("order_num"));
        recommendPage.getRecords().forEach(recommendPageItem -> {
            if (ModuleEnum.ARTICLE.getCode() == recommendPageItem.getModule()){
                Article article = articleMapper.selectById(recommendPageItem.getLinkId());
                if (article != null){
                    recommendPageItem.setTitle(article.getTitle());
                }
            }
            if (ModuleEnum.VIDEO.getCode() == recommendPageItem.getModule()){
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
     * @return
     */
    @Override
    public List<RecommendVO> select(Integer module, String title) {
        List<RecommendVO> recommendVOList = new ArrayList<>();

        if (ModuleEnum.ARTICLE.getCode() == module){
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

        if (ModuleEnum.VIDEO.getCode() == module){
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
        if (ModuleEnum.ARTICLE.getCode() == recommend.getModule()){
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

        if (ModuleEnum.VIDEO.getCode() == recommend.getModule()){
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
        if (ModuleEnum.ARTICLE.getCode() == recommend.getModule()){
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

        if (ModuleEnum.VIDEO.getCode() == recommend.getModule()){
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
    }

    /**
     * 删除
     * @param ids
     */
    @Override
    public void deleteRecommendsByIds(List<Integer> ids) {
        for (Integer id : ids) {
            baseMapper.delete(new QueryWrapper<Recommend>().lambda()
                    .eq(Recommend::getId,id));
        }
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

    /********************** portal ********************************/

    @Override
    public List<RecommendVO> listRecommendVO(Integer module) {
        List<RecommendVO> recommendList =this.baseMapper.listRecommendDTO(module);
        return genRecommendList(recommendList);
    }

    private List<RecommendVO> genRecommendList(List<RecommendVO> recommendList) {
        recommendList.forEach(recommendVO -> {
            if(ModuleEnum.ARTICLE.getCode() == recommendVO.getModule()){
                ArticleDTO simpleArticleDTO = articleMapper.getSimpleArticleDTO(recommendVO.getLinkId());
                BeanUtils.copyProperties(simpleArticleDTO, recommendVO);
            }
            if(ModuleEnum.VIDEO.getCode() == recommendVO.getModule()){
                VideoDTO simpleVideoDTO = videoMapper.getSimpleVideoDTO(recommendVO.getLinkId());
                BeanUtils.copyProperties(simpleVideoDTO, recommendVO);
            }
        });
        return recommendList;
    }

}
