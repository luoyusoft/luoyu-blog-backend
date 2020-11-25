package com.luoyu.blogmanage.service.book;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.entity.book.Book;
import com.luoyu.blogmanage.entity.book.dto.BookDTO;

/**
 * <p>
 * 图书表 服务类
 * </p>
 *
 * @author luoyu
 * @since 2019-01-27
 */
public interface BookService extends IService<Book> {

    /**
     * 分页查询
     * @param page
     * @param limit
     * @param title
     * @return
     */
    PageUtils queryPage(Integer page, Integer limit, String title);

    /**
     * 保存图书
      * @param book
     */
    void saveBook(BookDTO book);

    /**
     * 获取图书对象
     * @param id
     * @return
     */
    BookDTO getBook(String id);

    /**
     * 更新图书
     * @param book
     */
    void updateBook(BookDTO book);

    /**
     * 批量删除
     * @param bookIds
     */
    void deleteBatch(Integer[] bookIds);

    /**
     *
     * @param categoryId
     * @return
     */
    boolean checkByCategory(Integer categoryId);

}
