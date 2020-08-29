package com.luoyu.blog.project.service.manage.book.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.common.constants.GitalkConstants;
import com.luoyu.blog.common.constants.RabbitMqConstants;
import com.luoyu.blog.common.entity.book.Book;
import com.luoyu.blog.common.entity.book.dto.BookDTO;
import com.luoyu.blog.common.entity.book.vo.BookVO;
import com.luoyu.blog.common.entity.gitalk.InitGitalkRequest;
import com.luoyu.blog.common.entity.operation.Category;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.util.Query;
import com.luoyu.blog.common.util.RabbitMqUtils;
import com.luoyu.blog.project.mapper.book.BookMapper;
import com.luoyu.blog.project.service.manage.book.BookService;
import com.luoyu.blog.project.service.manage.operation.CategoryService;
import com.luoyu.blog.project.service.manage.operation.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图书表 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2019-01-27
 */
@Service
@Slf4j
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoryService;

    @Resource
    private RabbitMqUtils rabbitmqUtils;

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<BookVO> page = new Query<BookVO>(params).getPage();
        List<BookVO> bookList = this.baseMapper.listBookVo(page, params);
        // 查询所有分类
        List<Category> categoryList = categoryService.list(new QueryWrapper<Category>().lambda().eq(Category::getType, ModuleEnum.BOOK.getValue()));
        // 封装BookVo
        bookList.forEach(bookVo -> {
            // 设置类别
            bookVo.setCategoryListStr(categoryService.renderCategoryArr(bookVo.getCategoryId(), categoryList));
            // 设置标签列表
            bookVo.setTagList(tagService.listByLinkId(bookVo.getId(), ModuleEnum.BOOK.getValue()));
        });
        page.setRecords(bookList);
        return new PageUtils(page);
    }

    /**
     * 保存图书
     *
     * @param book
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBook(BookDTO book) {
        this.baseMapper.insert(book);
        tagService.saveTagAndNew(book.getTagList(), book.getId(), ModuleEnum.BOOK.getValue());
        InitGitalkRequest initGitalkRequest = new InitGitalkRequest();
        initGitalkRequest.setId(book.getId());
        initGitalkRequest.setTitle(book.getTitle());
        initGitalkRequest.setType(GitalkConstants.GITALK_TYPE_BOOK);
        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_GITALK_ROUTINGKEY_INIT, JsonUtils.objectToJson(initGitalkRequest));
    }

    /**
     * 获取图书对象
     *
     * @param id
     * @return
     */
    @Override
    public BookDTO getBook(String id) {
        Book readBook = this.baseMapper.selectById(id);
        BookDTO readBookDto = new BookDTO();
        BeanUtils.copyProperties(readBook, readBookDto);
        readBookDto.setTagList(tagService.listByLinkId(readBook.getId(), ModuleEnum.BOOK.getValue()));
        return readBookDto;
    }

    /**
     * 更新图书
     *
     * @param book
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBook(BookDTO book) {
        // 删除多对多所属标签
        tagService.deleteTagLink(book.getId(), ModuleEnum.BOOK.getValue());
        // 更新所属标签
        tagService.saveTagAndNew(book.getTagList(), book.getId(), ModuleEnum.BOOK.getValue());
        // 更新图书
        baseMapper.updateById(book);
    }

    /**
     * 批量删除
     *
     * @param bookIds
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Integer[] bookIds) {
        //先删除标签多对多关联
        Arrays.stream(bookIds).forEach(bookId -> {
            tagService.deleteTagLink(bookId, ModuleEnum.BOOK.getValue());
        });
        this.baseMapper.deleteBatchIds(Arrays.asList(bookIds));
    }

    /**
     * @param categoryId
     * @return
     */
    @Override
    public boolean checkByCategory(Integer categoryId) {
        return baseMapper.checkByCategory(categoryId) > 0;
    }

}
