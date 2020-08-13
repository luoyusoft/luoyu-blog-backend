package com.luoyu.blog.project.service.manage.book;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.entity.book.BookSense;

/**
 * <p>
 * 读后感 服务类
 * </p>
 *
 * @author bobbi
 * @since 2019-02-13
 */
public interface BookSenseService extends IService<BookSense> {

    /**
     * 获取读后感
     * @param bookId
     * @return
     */
    BookSense getBookSense(Integer bookId);
}
