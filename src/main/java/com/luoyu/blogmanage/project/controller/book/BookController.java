package com.luoyu.blogmanage.project.controller.book;

import com.luoyu.blogmanage.common.base.Result;
import com.luoyu.blogmanage.common.base.AbstractController;
import com.luoyu.blogmanage.common.constants.RedisCacheNames;
import com.luoyu.blogmanage.common.entity.book.Book;
import com.luoyu.blogmanage.common.entity.book.dto.BookDTO;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.validator.ValidatorUtils;
import com.luoyu.blogmanage.project.service.book.BookService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 图书表 前端控制器
 * </p>
 *
 * @author bobbi
 * @since 2019-01-27
 */
@RestController
@RequestMapping("/admin/book")
@CacheConfig(cacheNames ={RedisCacheNames.RECOMMEND,RedisCacheNames.TAG,RedisCacheNames.BOOK,RedisCacheNames.TIMELINE})
public class BookController extends AbstractController {
    @Resource
    private BookService bookService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("book:list")
    public Result list(@RequestParam Map<String, Object> params) {
        PageUtils page = bookService.queryPage(params);

        return Result.ok().put("page", page);
    }

    /**
     * 列表
     */
    @GetMapping("/select")
    @RequiresPermissions("book:list")
    public Result select() {
        List<Book> bookList = bookService.list(null);
        return Result.ok().put("bookList", bookList);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{id}")
    @RequiresPermissions("book:info")
    public Result info(@PathVariable("id") String id) {
        BookDTO book = bookService.getBook(id);
        return Result.ok().put("book", book);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @CacheEvict(allEntries = true)
    @RequiresPermissions("book:save")
    public Result save(@RequestBody BookDTO book) {
        ValidatorUtils.validateEntity(book);
        bookService.saveBook(book);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @CacheEvict(allEntries = true)
    @RequiresPermissions("book:update")
    public Result update(@RequestBody BookDTO book) {
        ValidatorUtils.validateEntity(book);
        bookService.updateBook(book);
        return Result.ok();
    }

    /**
     * 更新状态
     *
     * @param readBook
     * @return
     */
    @PutMapping("/update/status")
    @CacheEvict(allEntries = true)
    @RequiresPermissions("book:update")
    public Result updateStatus(@RequestBody Book readBook) {
        bookService.updateById(readBook);
        return Result.ok();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @CacheEvict(allEntries = true)
    @RequiresPermissions("book:delete")
    public Result delete(@RequestBody Integer[] ids) {
        bookService.deleteBatch(ids);

        return Result.ok();
    }


}
