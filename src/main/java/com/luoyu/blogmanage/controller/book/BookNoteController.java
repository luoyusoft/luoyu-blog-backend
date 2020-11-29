package com.luoyu.blogmanage.controller.book;

import com.luoyu.blogmanage.common.constants.RedisCacheNames;
import com.luoyu.blogmanage.common.enums.ModuleEnum;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.common.validator.ValidatorUtils;
import com.luoyu.blogmanage.entity.base.Response;
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
import java.util.Arrays;


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
    public Response listBookNote(@RequestParam("page") Integer page, @RequestParam("limit") Integer limit, @RequestParam("title") String title) {
        PageUtils notePage = bookNoteService.queryPage(page, limit, title);
        return Response.success(notePage);
    }

    /**
     * 信息
     */
    @GetMapping("/info/{bookNoteId}")
    @RequiresPermissions("book:note:list")
    public Response info(@PathVariable("bookNoteId") Integer bookNoteId) {
        BookNoteDTO bookNote = bookNoteService.getBookNote(bookNoteId);
        return Response.success(bookNote);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    @RequiresPermissions("book:note:save")
    @CacheEvict(allEntries = true)
    public Response saveBookNote(@RequestBody BookNoteDTO bookNote){
        ValidatorUtils.validateEntity(bookNote);
        bookNoteService.saveBookNote(bookNote);

        return Response.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    @RequiresPermissions("book:note:update")
    @CacheEvict(allEntries = true)
    public Response updateBookNote(@RequestBody BookNoteDTO bookNote){
        ValidatorUtils.validateEntity(bookNote);
        bookNoteService.updateBookNote(bookNote);

        return Response.success();
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
    public Response updateStatus(@RequestBody BookNote bookNote) {
        bookNoteService.updateById(bookNote);
        return Response.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/delete")
    @RequiresPermissions("book:note:delete")
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(allEntries = true)
    public Response deleteBatch(@RequestBody Integer[] bookNoteIds){
        recommendService.deleteBatchByLinkIdsAndType(Arrays.asList(bookNoteIds), ModuleEnum.BOOK_NOTE.getCode());
        bookNoteService.deleteBatch(bookNoteIds);

        return Response.success();
    }

}
