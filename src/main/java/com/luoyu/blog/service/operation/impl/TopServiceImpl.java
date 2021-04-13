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
import com.luoyu.blog.entity.operation.Top;
import com.luoyu.blog.entity.operation.vo.TopVO;
import com.luoyu.blog.entity.video.Video;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.mapper.operation.TopMapper;
import com.luoyu.blog.mapper.video.VideoMapper;
import com.luoyu.blog.service.operation.TopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 置顶 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
@Service
@Slf4j
public class TopServiceImpl extends ServiceImpl<TopMapper, Top> implements TopService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
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
        IPage<Top> TopPage = baseMapper.selectPage(new Query<Top>(params).getPage(),
                new QueryWrapper<Top>().orderByAsc("order_num"));
        TopPage.getRecords().forEach(TopPageItem -> {
            if (ModuleEnum.ARTICLE.getCode() == TopPageItem.getModule()){
                Article article = articleMapper.selectById(TopPageItem.getLinkId());
                if (article != null){
                    TopPageItem.setTitle(article.getTitle());
                }
            }
            if (ModuleEnum.VIDEO.getCode() == TopPageItem.getModule()){
                Video video = videoMapper.selectById(TopPageItem.getLinkId());
                if (video != null){
                    TopPageItem.setTitle(video.getTitle());
                }
            }
        });

        return new PageUtils(TopPage);
    }

    /**
     * 获取置顶列表
     *
     * @return
     */
    @Override
    public List<TopVO> select(Integer module, String title) {
        List<TopVO> TopVOList = new ArrayList<>();

        if (ModuleEnum.ARTICLE.getCode() == module){
            List<Article> articleList = articleMapper.selectArticleListByTitle(title);
            if (!CollectionUtils.isEmpty(articleList)){
                articleList.forEach(articleListItem -> {
                    TopVO TopVO = new TopVO();
                    TopVO.setTitle(articleListItem.getTitle());
                    TopVO.setLinkId(articleListItem.getId());
                    TopVO.setModule(module);
                    TopVOList.add(TopVO);
                });
            }
        }

        if (ModuleEnum.VIDEO.getCode() == module){
            List<Video> videoList = videoMapper.selectVideoListByTitle(title);
            if (videoList != null && videoList.size() > 0){
                videoList.forEach(videoListItem -> {
                    TopVO TopVO = new TopVO();
                    TopVO.setTitle(videoListItem.getTitle());
                    TopVO.setLinkId(videoListItem.getId());
                    TopVO.setModule(module);
                    TopVOList.add(TopVO);
                });
            }
        }

        return TopVOList;
    }

    /**
     * 批量删除
     *
     * @param linkIds
     * @param module
     */
    @Override
    public void deleteTopsByLinkIdsAndType(List<Integer> linkIds, int module) {
        for (Integer linkId : linkIds) {
            baseMapper.delete(new QueryWrapper<Top>().lambda()
                    .eq(Top::getLinkId,linkId)
                    .eq(Top::getModule,module));
        }
    }

    /**
     * 新增
     * @param top
     */
    @Override
    public void insertTop(Top top) {
        if (baseMapper.selectTopByOrderNum(top.getOrderNum()) != null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该顺序已被占用");
        }
        if (ModuleEnum.ARTICLE.getCode() == top.getModule()){
            Article article = articleMapper.selectById(top.getLinkId());
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "置顶内容不存在");
            }
            Top oldTop = baseMapper.selectTopByLinkIdAndType(top.getLinkId(), top.getModule());
            if(oldTop == null){
                baseMapper.insert(top);
            }else {
                baseMapper.updateTopOrderNumByLinkIdAndType(top);
            }
        }

        if (ModuleEnum.VIDEO.getCode() == top.getModule()){
            Video video = videoMapper.selectById(top.getLinkId());
            if(video == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "置顶内容不存在");
            }
            Top oldTop = baseMapper.selectTopByLinkIdAndType(top.getLinkId(), top.getModule());
            if(oldTop == null){
                baseMapper.insert(top);
            }else {
                baseMapper.updateTopOrderNumByLinkIdAndType(top);
            }
        }
    }

    /**
     * 更新
     * @param top
     */
    @Override
    public void updateTop(Top top) {
        Top existTop = baseMapper.selectTopByOrderNum(top.getOrderNum());
        if (existTop != null && !existTop.getId().equals(top.getId())){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该顺序已被占用");
        }
        if (ModuleEnum.ARTICLE.getCode() == top.getModule()){
            Article article = articleMapper.selectById(top.getLinkId());
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "置顶内容不存在");
            }
            Top oldTop = baseMapper.selectTopByLinkIdAndType(top.getLinkId(), top.getModule());
            if(oldTop == null){
                baseMapper.insert(top);
            }else {
                baseMapper.updateTopOrderNumByLinkIdAndType(top);
            }
        }

        if (ModuleEnum.VIDEO.getCode() == top.getModule()){
            Video video = videoMapper.selectById(top.getLinkId());
            if(video == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "置顶内容不存在");
            }
            Top oldTop = baseMapper.selectTopByLinkIdAndType(top.getLinkId(), top.getModule());
            if(oldTop == null){
                baseMapper.insert(top);
            }else {
                baseMapper.updateTopOrderNumByLinkIdAndType(top);
            }
        }
    }

    /**
     * 置顶
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateTopTop(Integer id) {
        if(baseMapper.selectTopByOrderNum(Top.ORDER_NUM_TOP) != null){
            List<Top> Tops = baseMapper.selectTops();
            Tops.forEach(TopsItem -> {
                TopsItem.setOrderNum(TopsItem.getOrderNum() + 1);
            });
            // 批量修改顺序，注意从大的开始，不然会有唯一索引冲突
            baseMapper.updateTopsOrderNumById(Tops);
        }
        if(!baseMapper.updateTopOrderNumById(Top.ORDER_NUM_TOP, id)){
            throw new MyException(ResponseEnums.UPDATE_FAILR.getCode(), "更新数据失败");
        }
    }

    /**
     * 删除
     * @param ids
     */
    @Override
    public void deleteTopsByIds(List<Integer> ids) {
        for (Integer id : ids) {
            baseMapper.delete(new QueryWrapper<Top>().lambda()
                    .eq(Top::getId,id));
        }
    }

    /**
     * 查找
     *
     * @param linkId
     * @param module
     */
    @Override
    public Top selectTopByLinkIdAndType(Integer linkId, Integer module) {
        return baseMapper.selectTopByLinkIdAndType(linkId, module);
    }

    /**
     * 查找最大顺序
     */
    @Override
    public Integer selectTopMaxOrderNum() {
        return baseMapper.selectTopMaxOrderNum();
    }

    /********************** portal ********************************/

    @Override
    public List<TopVO> listTopVO(Integer module) {
        List<TopVO> topList =baseMapper.listTopDTO(module);
        if (CollectionUtils.isEmpty(topList)){
            return Collections.emptyList();
        }
        return genTopList(topList);
    }

    private List<TopVO> genTopList(List<TopVO> topList) {
        topList.forEach(topVO -> {
            if(ModuleEnum.ARTICLE.getCode() == topVO.getModule()){
                ArticleDTO simpleArticleDTO = articleMapper.getSimpleArticleDTO(topVO.getLinkId());
                if (simpleArticleDTO != null){
                    BeanUtils.copyProperties(simpleArticleDTO, topVO);
                }
            }
            if(ModuleEnum.VIDEO.getCode() == topVO.getModule()){
                VideoDTO simpleVideoDTO = videoMapper.getSimpleVideoDTO(topVO.getLinkId());
                if (simpleVideoDTO != null){
                    BeanUtils.copyProperties(simpleVideoDTO, topVO);
                }
            }
        });
        return topList;
    }

}
