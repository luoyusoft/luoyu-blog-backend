package com.luoyu.blog.service.search.impl;

import com.luoyu.blog.common.constants.ModuleTypeConstants;
import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.entity.article.vo.ArticleVO;
import com.luoyu.blog.entity.operation.vo.TopVO;
import com.luoyu.blog.entity.search.vo.SearchListVO;
import com.luoyu.blog.entity.video.vo.VideoVO;
import com.luoyu.blog.service.operation.TopService;
import com.luoyu.blog.service.search.ArticleEsServer;
import com.luoyu.blog.service.search.SearchServer;
import com.luoyu.blog.service.search.VideoEsServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class SearchServerImpl implements SearchServer {

    @Autowired
    private ArticleEsServer articleEsServer;

    @Autowired
    private VideoEsServer videoEsServer;

    @Autowired
    private TopService topService;

    @Cacheable(value = RedisKeyConstants.SEARCHS, key = "#keyword")
    @Override
    public SearchListVO searchList(String keyword) throws Exception {
        // 处理文章
        List<ArticleVO> articleVOList = articleEsServer.searchArticleList(keyword);
        List<TopVO> articleTopVOs = topService.listTopVO(ModuleTypeConstants.ARTICLE);
        ArticleVO[] articleVOTopArray = new ArticleVO[articleVOList.size()];
        Queue<ArticleVO> articleVONoTopQueue = new LinkedList<>();
        List<ArticleVO> articleVOResultList = new ArrayList<>();

        Set<Integer> articleVOTopSet = new HashSet<>();
        Set<Integer> articleVONoToSet = new HashSet<>();

        if (!CollectionUtils.isEmpty(articleTopVOs)){
            articleVOList.forEach(articleDTOListItem -> {
                articleTopVOs.forEach(topVOsItem -> {
                    if(topVOsItem.getLinkId().equals(articleDTOListItem.getId())){
                        if (!articleVOTopSet.contains(articleDTOListItem.getId()) && !articleVONoToSet.contains(articleDTOListItem.getId())) {
                            articleDTOListItem.setTop(true);
                            articleVOTopArray[topVOsItem.getOrderNum()-1] = articleDTOListItem;
                            articleVOTopSet.add(articleDTOListItem.getId());
                        }
                    }else {
                        if (!articleVOTopSet.contains(articleDTOListItem.getId()) && !articleVONoToSet.contains(articleDTOListItem.getId())) {
                            articleVONoTopQueue.add(articleDTOListItem);
                            articleVONoToSet.add(articleDTOListItem.getId());
                        }
                    }
                });
            });
            for (int i = 0; i < articleVOTopArray.length; i++) {
                if (articleVOTopArray[i] == null){
                    articleVOTopArray[i] = articleVONoTopQueue.poll();
                }
            }
            articleVOResultList.addAll(Arrays.asList(articleVOTopArray));
        }else {
            articleVOResultList.addAll(articleVOList);
        }

        // 处理视频
        List<VideoVO> videoVOList = videoEsServer.searchVideoList(keyword);
        List<TopVO> videoTopVOs = topService.listTopVO(ModuleTypeConstants.VIDEO);
        VideoVO[] videoVOTopArray = new VideoVO[videoVOList.size()];
        Queue<VideoVO> videoVONoTopQueue = new LinkedList<>();
        List<VideoVO> videoVOResultList = new ArrayList<>();

        Set<Integer> videoVOTopSet = new HashSet<>();
        Set<Integer> videoVONoToSet = new HashSet<>();

        if (!CollectionUtils.isEmpty(videoTopVOs)){
            videoVOList.forEach(videoVOListItem -> {
                videoTopVOs.forEach(topVOsItem -> {
                    if(topVOsItem.getLinkId().equals(videoVOListItem.getId())){
                        if (!videoVOTopSet.contains(videoVOListItem.getId()) && !videoVONoToSet.contains(videoVOListItem.getId())) {
                            videoVOListItem.setTop(true);
                            videoVOTopArray[topVOsItem.getOrderNum() - 1] = videoVOListItem;
                            videoVOTopSet.add(videoVOListItem.getId());
                        }
                    }else {
                        if (!videoVOTopSet.contains(videoVOListItem.getId()) && !videoVONoToSet.contains(videoVOListItem.getId())) {
                            videoVONoTopQueue.add(videoVOListItem);
                            videoVONoToSet.add(videoVOListItem.getId());
                        }
                    }
                });
            });
            for (int i = 0; i < videoVOTopArray.length; i++) {
                if (videoVOTopArray[i] == null){
                    videoVOTopArray[i] = videoVONoTopQueue.poll();
                }
            }
            videoVOResultList.addAll(Arrays.asList(videoVOTopArray));
        }else {
            videoVOResultList.addAll(videoVOList);
        }

        SearchListVO searchListVO = new SearchListVO();
        searchListVO.setVideoList(videoVOResultList);
        searchListVO.setArticleList(articleVOResultList);
        return searchListVO;
    }
}
