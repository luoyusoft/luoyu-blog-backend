package com.luoyu.blog.project.controller.portal.book;

import com.luoyu.blog.common.base.Result;
import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.entity.book.vo.BookVO;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.project.service.portal.book.BookService;
import com.luoyu.blog.framework.aop.portal.annotation.LogLike;
import com.luoyu.blog.framework.aop.portal.annotation.LogView;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * <p>
 * 文章 前端控制器
 * </p>
 *
 * @author bobbi
 * @since 2018-11-07
 */
@RestController("bookPortalController")
@CacheConfig(cacheNames = {RedisCacheNames.BOOK})
public class BookController {

    @Resource
    private BookService bookService;

    @GetMapping("/book/{bookId}")
    @LogView(type = "book")
    public Result getBook(@PathVariable Integer bookId){
        BookVO book = bookService.getBookVo(bookId);
        return Result.ok().put("book",book);
    }

    @GetMapping("/books")
    @Cacheable
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = bookService.queryPageCondition(params);
        return Result.ok().put("page",page);
    }

    @PutMapping("/book/like/{id}")
    @LogLike(type = "book")
    public Result likeBook(@PathVariable Integer id) {
        return Result.ok();
    }

}
