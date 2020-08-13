package com.luoyu.blog.project.service.portal.book;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.entity.book.Book;
import com.luoyu.blog.common.entity.book.vo.BookVO;
import com.luoyu.blog.common.util.PageUtils;

import java.util.Map;


/**
 * <p>
 * 图书表 服务类
 * </p>
 *
 * @author bobbi
 * @since 2019-01-27
 */
public interface BookService extends IService<Book> {

    /**
     * 分页分类获取列表
     * @param params
     * @return
     */
    PageUtils queryPageCondition(Map<String, Object> params);

    /**
     * 获取Book对象
     * @param id
     * @return
     */
    BookVO getBookVo(Integer id);
}
