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
import com.luoyu.blog.entity.operation.dto.RecommendDTO;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.mapper.operation.RecommendMapper;
import com.luoyu.blog.service.operation.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * 分页查询
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
        IPage<Recommend> recommendPage = baseMapper.selectPage(new Query<Recommend>(params).getPage(),
                new QueryWrapper<Recommend>().lambda()
                        .like(StringUtils.isNotEmpty(title),Recommend::getTitle,title));
        return new PageUtils(recommendPage);
    }

    /**
     * 获取推荐列表
     *
     * @return
     */
    @Override
    public List<RecommendDTO> select(Integer type, String title) {
        List<RecommendDTO> recommendDTOList = new ArrayList<>();

        if (ModuleEnum.ARTICLE.getCode() == type){
            List<Article> articleList = articleMapper.selectArticleListByTitle(title);
            if (articleList != null && articleList.size() > 0){
                articleList.forEach(articleListItem -> {
                    RecommendDTO recommendDTO = new RecommendDTO();
                    recommendDTO.setTitle(articleListItem.getTitle());
                    recommendDTO.setLinkId(articleListItem.getId());
                    recommendDTO.setType(type);
                    recommendDTOList.add(recommendDTO);
                });
            }
        }

        return recommendDTOList;
    }

    /**
     * 批量删除
     *
     * @param linkIds
     * @param type
     */
    @Override
    public void deleteRecommendsByLinkIdsAndType(List<Integer> linkIds, int type) {
        for (Integer linkId : linkIds) {
            baseMapper.delete(new QueryWrapper<Recommend>().lambda()
                    .eq(Recommend::getLinkId,linkId)
                    .eq(Recommend::getType,type));
        }
    }

    /**
     * 新增
     * @param recommend
     */
    @Override
    public void insertRecommend(Recommend recommend) {
        if (ModuleEnum.ARTICLE.getCode() == recommend.getType()){
            Article article = articleMapper.selectById(recommend.getLinkId());
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }
            Recommend oldRecommend = baseMapper.selectRecommendByLinkIdAndType(recommend.getLinkId(), recommend.getType());
            if(oldRecommend == null){
                baseMapper.insert(recommend);
            }else {
                baseMapper.updateRecommendByLinkIdAndType(recommend);
            }
        }

        // todo 没时间写了
        if (ModuleEnum.BOOK.getCode() == recommend.getType()){

        }

        if (ModuleEnum.BOOK_NOTE.getCode() == recommend.getType()){

        }
    }

    /**
     * 更新
     * @param recommend
     */
    @Override
    public void updateRecommend(Recommend recommend) {
        if (ModuleEnum.ARTICLE.getCode() == recommend.getType()){
            Article article = articleMapper.selectById(recommend.getLinkId());
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }
            Recommend oldRecommend = baseMapper.selectRecommendByLinkIdAndType(recommend.getLinkId(), recommend.getType());
            if(oldRecommend == null){
                baseMapper.insert(recommend);
            }else {
                baseMapper.updateRecommendByLinkIdAndType(recommend);
            }
        }

        // todo 没时间写了
        if (ModuleEnum.BOOK.getCode() == recommend.getType()){

        }

        if (ModuleEnum.BOOK_NOTE.getCode() == recommend.getType()){

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
     * @param type
     */
    @Override
    public Recommend selectRecommendByLinkIdAndType(Integer linkId, Integer type) {
        return baseMapper.selectRecommendByLinkIdAndType(linkId, type);
    }

    /********************** portal ********************************/

    @Override
    public List<RecommendDTO> listRecommendDTO() {
        List<RecommendDTO> recommendList =this.baseMapper.listRecommendDTO();
        return genRecommendList(recommendList);
    }

    private List<RecommendDTO> genRecommendList(List<RecommendDTO> recommendList) {
        recommendList.forEach(recommendDTO -> {
            if(ModuleEnum.ARTICLE.getCode() == recommendDTO.getType()){
                ArticleDTO simpleArticleDTO = articleMapper.getSimpleArticleDTO(recommendDTO.getLinkId());
                BeanUtils.copyProperties(simpleArticleDTO,recommendDTO);
                recommendDTO.setUrlType("article");
            }
        });
        return recommendList;
    }

}
