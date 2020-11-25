package com.luoyu.blogmanage.controller.book;

import com.luoyu.blogmanage.common.constants.RedisCacheNames;
import com.luoyu.blogmanage.common.enums.ModuleEnum;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.validator.ValidatorUtils;
import com.luoyu.blogmanage.entity.base.Result;
import com.luoyu.blogmanage.entity.book.BookNote;
import com.luoyu.blogmanage.entity.book.dto.BookNoteDTO;
import com.luoyu.blogmanage.service.book.BookNoteService;
import com.luoyu.blogmanage.service.operation.RecommendService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * BookNoteAdminController
 *
 * @author luoyu
 * @date 2018/11/20 20:25
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

    /**
     * 列表
     */
    @GetMapping("/list")
    @RequiresPermissions("book:note:list")
    public Result listBookNote(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title) {
        PageUtils notePage = bookNoteService.queryPage(page, limit, title);
        return Result.ok().put("page",notePage);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{bookNoteId}")
    @RequiresPermissions("book:note:list")
    public Result info(@PathVariable("bookNoteId") Integer bookNoteId) {
        BookNoteDTO bookNote = bookNoteService.getBookNote(bookNoteId);
        return Result.ok().put("bookNote",bookNote);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("book:note:save")
    @CacheEvict(allEntries = true)
    public Result saveBookNote(@RequestBody BookNoteDTO bookNote){
        ValidatorUtils.validateEntity(bookNote);
        bookNoteService.saveBookNote(bookNote);

        return Result.ok();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @RequiresPermissions("book:note:update")
    @CacheEvict(allEntries = true)
    public Result updateBookNote(@RequestBody BookNoteDTO bookNote){
        ValidatorUtils.validateEntity(bookNote);
        bookNoteService.updateBookNote(bookNote);

        return Result.ok();
    }

    /**
     * 更新状态
     *
     * @param bookNote
     * @return
     */
    @PutMapping("/update/status")
    @RequiresPermissions("book:note:update")
    @CacheEvict(allEntries = true)
    public Result updateStatus(@RequestBody BookNote bookNote) {
        bookNoteService.updateById(bookNote);
        return Result.ok();
    }

    /**
     * 删除
     */
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
