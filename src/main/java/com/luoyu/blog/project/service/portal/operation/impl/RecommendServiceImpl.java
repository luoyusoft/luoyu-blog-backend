package com.luoyu.blog.project.service.portal.operation.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.entity.article.vo.ArticleVO;
import com.luoyu.blog.common.entity.book.vo.BookNoteVO;
import com.luoyu.blog.common.entity.operation.Recommend;
import com.luoyu.blog.common.entity.operation.vo.RecommendVO;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.project.mapper.manage.operation.RecommendMapper;
import com.luoyu.blog.project.service.portal.article.ArticleService;
import com.luoyu.blog.project.service.portal.book.BookNoteService;
import com.luoyu.blog.project.service.portal.operation.RecommendService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * RecommendServiceImpl
 *
 * @author bobbi
 * @date 2019/02/22 13:42
 * @email 571002217@qq.com
 * @description
 */
@Service("recommendPortalService")
public class RecommendServiceImpl extends ServiceImpl<RecommendMapper, Recommend> implements RecommendService {

    @Resource
    private ArticleService articleService;

    @Resource
    private BookNoteService bookNoteService;


    @Override
    public List<RecommendVO> listRecommendVo() {
        List<RecommendVO> recommendList =this.baseMapper.listRecommendVo();
        return genRecommendList(recommendList);
    }

    @Override
    public List<RecommendVO> listHotRead() {
        List<RecommendVO> hotReadList =this.baseMapper.listHotRead();
        genRecommendList(hotReadList);
        hotReadList.get(0).setTop(true);
        return hotReadList;
    }

    private List<RecommendVO> genRecommendList(List<RecommendVO> recommendList) {
        recommendList.forEach(recommendVo -> {
            if(ModuleEnum.ARTICLE.getValue() == recommendVo.getType()){
                ArticleVO simpleArticleVo = articleService.getSimpleArticleVo(recommendVo.getLinkId());
                BeanUtils.copyProperties(simpleArticleVo,recommendVo);
                recommendVo.setUrlType("article");
            }else if(ModuleEnum.BOOK_NOTE.getValue() == recommendVo.getType()) {
                BookNoteVO simpleBookNoteVo = bookNoteService.getSimpleBookNoteVo(recommendVo.getLinkId());
                recommendVo.setUrlType("bookNote");
                BeanUtils.copyProperties(simpleBookNoteVo,recommendVo);
            }
        });
        return recommendList;
    }
}
