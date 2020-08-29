package com.luoyu.blog.project.controller.portal.book;

import com.luoyu.blog.common.base.Result;
import com.luoyu.blog.common.constants.RedisCacheNames;
import com.luoyu.blog.common.entity.book.BookNote;
import com.luoyu.blog.common.util.PageUtils;
import com.luoyu.blog.project.service.portal.book.BookNoteService;
import com.luoyu.blog.framework.aop.portal.annotation.LogLike;
import com.luoyu.blog.framework.aop.portal.annotation.LogView;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;


/**
 * BookNoteNoteAdminController
 *
 * @author bobbi
 * @date 2018/11/20 20:25
 * @email 571002217@qq.com
 * @description
 */
@RestController("bookNotePortalController")
@CacheConfig(cacheNames = {RedisCacheNames.BOOKNOTE})
public class BookNoteController {

    @Resource
    private BookNoteService bookNoteService;
   

    @GetMapping("/bookNote/{bookNoteId}")
    @LogView(type = "bookNote")
    public Result getBookNote(@PathVariable Integer bookNoteId){
        BookNote bookNote = bookNoteService.getById(bookNoteId);
        return Result.ok().put("bookNote",bookNote);
    }

    @GetMapping("/bookNotes")
    @Cacheable
    public Result list(@RequestParam Map<String, Object> params){
        PageUtils page = bookNoteService.queryPageCondition(params);
        return Result.ok().put("page",page);
    }

    @PutMapping("/bookNote/like/{id}")
    @LogLike(type = "bookNote")
    public Result likeBookNote(@PathVariable Integer id) {
        return Result.ok();
    }

}
