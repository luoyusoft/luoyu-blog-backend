package com.luoyu.blog.project.controller.manage.book;

import com.luoyu.blog.common.base.Result;
import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.entity.book.BookNote;
import com.luoyu.blog.common.entity.book.dto.BookNoteDTO;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.common.validator.ValidatorUtils;
import com.luoyu.blog.project.service.manage.book.BookNoteService;
import com.luoyu.blog.project.service.manage.operation.RecommendService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * BookNoteAdminController
 *
 * @author bobbi
 * @date 2018/11/20 20:25
 * @email 571002217@qq.com
 * @description
 */
@RestController
@RequestMapping("/admin/book/note")
@CacheConfig(cacheNames ={RedisCacheNames.RECOMMEND,RedisCacheNames.TAG,RedisCacheNames.BOOK,RedisCacheNames.BOOKNOTE,RedisCacheNames.TIMELINE})
public class BookNoteController {

    @Resource
    private BookNoteService bookNoteService;

    @Resource
    private RecommendService recommendService;

    @GetMapping("/list")
    @RequiresPermissions("book:note:list")
    public Result listBookNote(@RequestParam Map<String, Object> params) {
        PageUtils page = bookNoteService.queryPage(params);
        return Result.ok().put("page",page);
    }

    @GetMapping("/info/{bookNoteId}")
    @RequiresPermissions("book:note:list")
    public Result info(@PathVariable Integer bookNoteId) {
        BookNoteDTO bookNote = bookNoteService.getBookNote(bookNoteId);
        return Result.ok().put("bookNote",bookNote);
    }

    @PostMapping("/save")
    @RequiresPermissions("book:note:save")
    @CacheEvict(allEntries = true)
    public Result saveBookNote(@RequestBody BookNoteDTO bookNote){
        ValidatorUtils.validateEntity(bookNote);
        bookNoteService.saveBookNote(bookNote);
        return Result.ok();
    }

    @PutMapping("/update")
    @RequiresPermissions("book:note:update")
    @CacheEvict(allEntries = true)
    public Result updateBookNote(@RequestBody BookNoteDTO bookNote){
        ValidatorUtils.validateEntity(bookNote);
        bookNoteService.updateBookNote(bookNote);
        return Result.ok();
    }

    @PutMapping("/update/status")
    @RequiresPermissions("book:note:update")
    @CacheEvict(allEntries = true)
    public Result updateStatus(@RequestBody BookNote bookNote) {
        bookNoteService.updateById(bookNote);
        return Result.ok();
    }


    @DeleteMapping("/delete")
    @RequiresPermissions("book:note:delete")
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public Result deleteBatch(@RequestBody Integer[] bookNoteIds){
        recommendService.deleteBatchByLinkId(bookNoteIds, ModuleEnum.BOOK_NOTE.getValue());
        bookNoteService.deleteBatch(bookNoteIds);
        return Result.ok();
    }

}
