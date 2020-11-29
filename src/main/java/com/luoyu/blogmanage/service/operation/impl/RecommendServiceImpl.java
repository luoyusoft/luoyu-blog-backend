package com.luoyu.blogmanage.service.operation.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blogmanage.common.enums.ModuleEnum;
import com.luoyu.blogmanage.common.enums.ResponseEnums;
import com.luoyu.blogmanage.common.exception.MyException;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.util.Query;
import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.book.Book;
import com.luoyu.blogmanage.entity.book.BookNote;
import com.luoyu.blogmanage.entity.operation.Recommend;
import com.luoyu.blogmanage.entity.operation.vo.RecommendVO;
import com.luoyu.blogmanage.mapper.article.ArticleMapper;
import com.luoyu.blogmanage.mapper.book.BookMapper;
import com.luoyu.blogmanage.mapper.book.BookNoteMapper;
import com.luoyu.blogmanage.mapper.operation.RecommendMapper;
import com.luoyu.blogmanage.service.operation.RecommendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookNoteMapper bookNoteMapper;

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
    public List<RecommendVO> listSelect(Integer type, String title) {
        List<RecommendVO> recommendVOList = new ArrayList<>();

        if (ModuleEnum.ARTICLE.getCode() == type){
            List<Article> articleList = articleMapper.selectArticleListByTitle(title);
            if (articleList != null && articleList.size() > 0){
                articleList.forEach(articleListItem -> {
                    RecommendVO recommendVO = new RecommendVO();
                    recommendVO.setTitle(articleListItem.getTitle());
                    recommendVO.setLinkId(articleListItem.getId());
                    recommendVO.setType(type);
                    recommendVOList.add(recommendVO);
                });
            }
        }

        if (ModuleEnum.BOOK.getCode() == type){
            List<Book> bookList = bookMapper.selectBookListByTitle(title);
            if (bookList != null && bookList.size() > 0){
                bookList.forEach(bookListItem -> {
                    RecommendVO recommendVO = new RecommendVO();
                    recommendVO.setTitle(bookListItem.getTitle());
                    recommendVO.setLinkId(bookListItem.getId());
                    recommendVO.setType(type);
                    recommendVOList.add(recommendVO);
                });
            }
        }

        if (ModuleEnum.BOOK_NOTE.getCode() == type){
            List<BookNote> bookNoteList = bookNoteMapper.selectBookNoteListByTitle(title);
            if (bookNoteList != null && bookNoteList.size() > 0){
                bookNoteList.forEach(bookNoteListItem -> {
                    RecommendVO recommendVO = new RecommendVO();
                    recommendVO.setTitle(bookNoteListItem.getTitle());
                    recommendVO.setLinkId(bookNoteListItem.getId());
                    recommendVO.setType(type);
                    recommendVOList.add(recommendVO);
                });
            }
        }

        return recommendVOList;
    }

    /**
     * 更新置顶状态
     *
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
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

        if (ModuleEnum.ARTICLE.getCode() == recommend.getType()){
            Article article = articleMapper.selectById(recommend.getLinkId());
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }
            article.setTop(false);
            article.setUpdateTime(new Date());
            articleMapper.updateArticle(article);
        }

        // todo 没时间写了
        if (ModuleEnum.BOOK.getCode() == recommend.getType()){

        }

        if (ModuleEnum.BOOK_NOTE.getCode() == recommend.getType()){

        }
    }

    /**
     * 从主删除过来批量删除
     *
     * @param linkIds
     * @param type
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBatchByLinkIdsAndType(List<Integer> linkIds, int type) {
        for (Integer linkId : linkIds) {
            baseMapper.delete(new QueryWrapper<Recommend>().lambda()
                    .eq(Recommend::getLinkId,linkId)
                    .eq(Recommend::getType,type));
        }
    }

    /**
     * 从主新增过来新增
     *
     * @param linkId
     * @param type
     */
    @Override
    public void insertRecommend(Integer linkId, int type) {
        if (ModuleEnum.ARTICLE.getCode() == type){
            Article article = articleMapper.selectById(linkId);
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }

            Recommend recommend = baseMapper.selectRecommendByLinkIdAndType(linkId, type);
            if(recommend == null){
                int count = baseMapper.selectCount();

                recommend = new Recommend();
                recommend.setLinkId(linkId);
                recommend.setType(type);
                recommend.setOrderNum(count + 1);
                recommend.setTop(article.getTop());
                recommend.setTitle(article.getTitle());
                recommend.setPublish(article.getPublish());
                baseMapper.insert(recommend);
            }else {
                recommend = new Recommend();
                recommend.setTop(article.getTop());
                recommend.setTitle(article.getTitle());
                recommend.setPublish(article.getPublish());
                baseMapper.updateRecommendByLinkIdAndType(recommend);
            }
        }

        // todo 没时间写了
        if (ModuleEnum.BOOK.getCode() == type){

        }

        if (ModuleEnum.BOOK_NOTE.getCode() == type){

        }

    }

    /**
     * 新增
     * @param recommend
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertRecommend(Recommend recommend) {
        if (ModuleEnum.ARTICLE.getCode() == recommend.getType()){
            Article article = articleMapper.selectById(recommend.getLinkId());
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }
            article.setRecommend(true);
            article.setUpdateTime(new Date());
            articleMapper.updateArticle(article);
            Recommend oldRecommend = baseMapper.selectRecommendByLinkIdAndType(recommend.getLinkId(), recommend.getType());
            if(oldRecommend == null){
                recommend.setTop(article.getTop());
                recommend.setTitle(article.getTitle());
                recommend.setPublish(article.getPublish());
                baseMapper.insert(recommend);
            }else {
                recommend.setTop(article.getTop());
                recommend.setTitle(article.getTitle());
                recommend.setPublish(article.getPublish());
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRecommend(Recommend recommend) {
        if (ModuleEnum.ARTICLE.getCode() == recommend.getType()){
            Article article = articleMapper.selectById(recommend.getLinkId());
            if(article == null) {
                throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
            }
            article.setRecommend(true);
            article.setTop(recommend.getTop());
            article.setPublish(recommend.getPublish());
            article.setUpdateTime(new Date());
            articleMapper.updateArticle(article);
            Recommend oldRecommend = baseMapper.selectRecommendByLinkIdAndType(recommend.getLinkId(), recommend.getType());
            if(oldRecommend == null){
                recommend.setTop(article.getTop());
                recommend.setTitle(article.getTitle());
                recommend.setPublish(article.getPublish());
                baseMapper.insert(recommend);
            }else {
                recommend.setTop(article.getTop());
                recommend.setTitle(article.getTitle());
                recommend.setPublish(article.getPublish());
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRecommend(List<Integer> ids) {
        for (Integer id : ids) {
            Recommend recommend = baseMapper.selectById(id);
            baseMapper.delete(new QueryWrapper<Recommend>().lambda()
                    .eq(Recommend::getId,id));

            if (ModuleEnum.ARTICLE.getCode() == recommend.getType()){
                Article article = articleMapper.selectById(recommend.getLinkId());
                if(article == null) {
                    throw new MyException(ResponseEnums.PARAM_ERROR.getCode(), "推荐内容不存在");
                }
                article.setRecommend(false);
                article.setUpdateTime(new Date());
                articleMapper.updateArticle(article);
            }

            // todo 没时间写了
            if (ModuleEnum.BOOK.getCode() == recommend.getType()){

            }

            if (ModuleEnum.BOOK_NOTE.getCode() == recommend.getType()){

            }
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

}
