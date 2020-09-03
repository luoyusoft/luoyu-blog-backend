package com.luoyu.blogmanage.project.service.book.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blogmanage.common.entity.book.BookSense;
import com.luoyu.blogmanage.project.service.book.BookSenseService;
import com.luoyu.blogmanage.project.mapper.book.BookSenseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 读后感 服务实现类
 * </p>
 *
 * @author luoyu
 * @since 2019-02-13
 */
@Service
@Slf4j
public class BookSenseServiceImpl extends ServiceImpl<BookSenseMapper, BookSense> implements BookSenseService {


    /**
     * 获取读后感
     *
     * @param bookId
     * @return
     */
    @Override
    public BookSense getBookSense(Integer bookId) {
        BookSense readSense = this.baseMapper.selectOne(new QueryWrapper<BookSense>().lambda()
                .eq(bookId != null, BookSense::getBookId, bookId));
        return Optional.ofNullable(readSense)
                .orElse(null);
    }
}
