package com.luoyu.blogmanage.service.book.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blogmanage.common.constants.GitalkConstants;
import com.luoyu.blogmanage.common.constants.RabbitMqConstants;
import com.luoyu.blogmanage.entity.book.BookNote;
import com.luoyu.blogmanage.entity.book.dto.BookNoteDTO;
import com.luoyu.blogmanage.entity.book.vo.BookNoteVO;
import com.luoyu.blogmanage.entity.gitalk.InitGitalkRequest;
import com.luoyu.blogmanage.entity.operation.Category;
import com.luoyu.blogmanage.common.enums.ModuleEnum;
import com.luoyu.blogmanage.common.util.JsonUtils;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.util.Query;
import com.luoyu.blogmanage.common.util.RabbitMqUtils;
import com.luoyu.blogmanage.mapper.book.BookNoteMapper;
import com.luoyu.blogmanage.service.book.BookNoteService;
import com.luoyu.blogmanage.service.book.BookService;
import com.luoyu.blogmanage.service.operation.CategoryService;
import com.luoyu.blogmanage.service.operation.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * bookNoteAdminServiceImpl
 *
 * @author luoyu
 * @date 2018/11/21 12:48
 * @description
 */
@Service
public class BookNoteServiceImpl extends ServiceImpl<BookNoteMapper, BookNote> implements BookNoteService {

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BookService bookService;

    @Resource
    private RabbitMqUtils rabbitmqUtils;

    /**
     * 分页查询笔记列表
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

        Page<BookNoteVO> notePage = new Query<BookNoteVO>(params).getPage();
        List<BookNoteVO> bookNoteList = baseMapper.listBookNoteVo(notePage, params);
        // 查询所有分类
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().lambda().eq(Category::getType, ModuleEnum.BOOK.getValue()));
        // 封装BookNoteVo
        Optional.ofNullable(bookNoteList).ifPresent((bookNoteVos ->
                bookNoteVos.forEach(bookNoteVo -> {
                    // 设置所属书本
                    bookNoteVo.setBook(bookService.getById(bookNoteVo.getBookId()));
                    // 设置类别
                    bookNoteVo.setCategoryListStr(categoryService.renderCategoryArr(bookNoteVo.getCategoryId(), categoryList));
                    // 设置标签列表
                    bookNoteVo.setTagList(tagService.listByLinkId(bookNoteVo.getId(), ModuleEnum.BOOK_NOTE.getValue()));
                })));
        notePage.setRecords(bookNoteList);
        return new PageUtils(notePage);
    }


    /**
     * 保存笔记笔记
     *
     * @param bookNote
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBookNote(BookNoteDTO bookNote) {
        baseMapper.insert(bookNote);
        tagService.saveTagAndNew(bookNote.getTagList(), bookNote.getId(), ModuleEnum.BOOK_NOTE.getValue());
        InitGitalkRequest initGitalkRequest = new InitGitalkRequest();
        initGitalkRequest.setId(bookNote.getId());
        initGitalkRequest.setTitle(bookNote.getTitle());
        initGitalkRequest.setType(GitalkConstants.GITALK_TYPE_BOOKNOTE);
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_GITALK_ROUTINGKEY_INIT, JsonUtils.objectToJson(initGitalkRequest));
    }

    /**
     * 更新笔记
     *
     * @param bookNote
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBookNote(BookNoteDTO bookNote) {
        // 删除多对多所属标签
        tagService.deleteTagLink(bookNote.getId(), ModuleEnum.BOOK_NOTE.getValue());
        // 更新所属标签
        tagService.saveTagAndNew(bookNote.getTagList(), bookNote.getId(), ModuleEnum.BOOK_NOTE.getValue());
        // 更新笔记
        baseMapper.updateById(bookNote);
    }

    /**
     * 获取笔记对象
     *
     * @param bookNoteId
     * @return
     */
    @Override
    public BookNoteDTO getBookNote(Integer bookNoteId) {
        BookNoteDTO bookNoteDto = new BookNoteDTO();
        BookNote bookNote = this.baseMapper.selectById(bookNoteId);
        BeanUtils.copyProperties(bookNote, bookNoteDto);
        // 查询所属标签
        bookNoteDto.setTagList(tagService.listByLinkId(bookNoteId, ModuleEnum.BOOK_NOTE.getValue()));
        return bookNoteDto;
    }

    /**
     * 判断该类别下是否有笔记
     *
     * @param categoryId
     * @return
     */
    @Override
    public boolean checkByCategory(Integer categoryId) {
        return baseMapper.checkByCategory(categoryId) > 0;
    }

    /**
     * 批量删除
     *
     * @param bookNoteIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Integer[] bookNoteIds) {
        //先删除笔记标签多对多关联
        Arrays.stream(bookNoteIds).forEach(bookNoteId -> {
            tagService.deleteTagLink(bookNoteId, ModuleEnum.BOOK_NOTE.getValue());
        });
        this.baseMapper.deleteBatchIds(Arrays.asList(bookNoteIds));
    }

}
