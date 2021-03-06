package com.jinhx.blog.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhx.blog.common.constants.ModuleTypeConstants;
import com.jinhx.blog.common.enums.ResponseEnums;
import com.jinhx.blog.common.exception.MyException;
import com.jinhx.blog.common.util.PageUtils;
import com.jinhx.blog.common.util.Query;
import com.jinhx.blog.mapper.video.VideoMapper;
import com.jinhx.blog.entity.article.Article;
import com.jinhx.blog.entity.article.dto.ArticleDTO;
import com.jinhx.blog.entity.operation.Top;
import com.jinhx.blog.entity.operation.vo.TopVO;
import com.jinhx.blog.entity.video.Video;
import com.jinhx.blog.entity.video.dto.VideoDTO;
import com.jinhx.blog.mapper.article.ArticleMapper;
import com.jinhx.blog.mapper.operation.TopMapper;
import com.jinhx.blog.service.cache.CacheServer;
import com.jinhx.blog.service.operation.TopService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
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

    @Autowired
    private CacheServer cacheServer;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor taskExecutor;

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
            if (ModuleTypeConstants.ARTICLE.equals(TopPageItem.getModule())){
                Article article = articleMapper.selectById(TopPageItem.getLinkId());
                if (article != null){
                    TopPageItem.setTitle(article.getTitle());
                }
            }
            if (ModuleTypeConstants.VIDEO.equals(TopPageItem.getModule())){
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

        if (ModuleTypeConstants.ARTICLE.equals(module)){
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

        if (ModuleTypeConstants.VIDEO.equals(module)){
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
     * 新增
     * @param top
     */
    @Override
    public void insertTop(Top top) {
        if (baseMapper.selectTopByOrderNum(top.getOrderNum()) != null){
            throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "该顺序已被占用");
        }
        if (ModuleTypeConstants.ARTICLE.equals(top.getModule())){
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

        if (ModuleTypeConstants.VIDEO.equals(top.getModule())){
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

        cleanListAllCache();
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
        if (ModuleTypeConstants.ARTICLE.equals(top.getModule())){
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

        if (ModuleTypeConstants.VIDEO.equals(top.getModule())){
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

        cleanListAllCache();
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

        cleanListAllCache();
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

        cleanListAllCache();
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

    /**
     * 清除缓存
     */
    private void cleanListAllCache(){
        taskExecutor.execute(() ->{
            cacheServer.cleanListAllCache();
        });
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
            if(ModuleTypeConstants.ARTICLE.equals(topVO.getModule())){
                ArticleDTO simpleArticleDTO = articleMapper.getSimpleArticleDTO(topVO.getLinkId());
                if (simpleArticleDTO != null){
                    BeanUtils.copyProperties(simpleArticleDTO, topVO);
                }
            }
            if(ModuleTypeConstants.VIDEO.equals(topVO.getModule())){
                VideoDTO simpleVideoDTO = videoMapper.getSimpleVideoDTO(topVO.getLinkId());
                if (simpleVideoDTO != null){
                    BeanUtils.copyProperties(simpleVideoDTO, topVO);
                }
            }
        });
        return topList;
    }

}
