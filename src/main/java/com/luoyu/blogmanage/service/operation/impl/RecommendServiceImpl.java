package com.luoyu.blogmanage.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.operation.Recommend;
import com.luoyu.blogmanage.entity.operation.vo.RecommendVO;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.util.Query;
import com.luoyu.blogmanage.mapper.article.ArticleMapper;
import com.luoyu.blogmanage.mapper.operation.RecommendMapper;
import com.luoyu.blogmanage.service.operation.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<RecommendVO> listSelect() {
        return baseMapper.listSelect();
    }

    /**
     * 更新置顶状态
     *
     * @param id
     */
    @Override
    public void updateTop(Integer id) {
        // 更新本条
        Recommend recommend = new Recommend();
        recommend.setTop(true);
        recommend.setId(id);
        this.baseMapper.updateById(recommend);
        //批量更新其他
        recommend.setTop(false);
        recommend.setId(null);
        this.baseMapper.update(recommend,new QueryWrapper<Recommend>().lambda()
                .ne(Recommend::getId,id));
    }

    /**
     * 批量删除
     *
     * @param linkIds
     * @param type
     */
    @Override
    public void deleteBatchByLinkId(Integer[] linkIds, int type) {
        for (int i = 0; i < linkIds.length; i++) {
            int linkId = linkIds[i];
            baseMapper.delete(new QueryWrapper<Recommend>().lambda()
                    .eq(Recommend::getLinkId,linkId)
                    .eq(Recommend::getType,type));
        }
    }

    /**
     * 新增
     *
     * @param linkId
     * @param type
     */
    @Override
    public void insertRecommend(Integer linkId, int type) {
        Article article = articleMapper.selectById(linkId);
        if(article != null){
            int i = baseMapper.selectCount();
            Recommend recommend = new Recommend();
            recommend.setLinkId(linkId);
            recommend.setType(type);
            recommend.setOrderNum(i + 1);
            recommend.setTop(article.getTop());
            recommend.setTitle(article.getTitle());
            baseMapper.insert(recommend);
        }
    }

}
