package com.luoyu.blogmanage.service.book;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.common.util.PageUtils;
import com.luoyu.blogmanage.entity.book.BookNote;
import com.luoyu.blogmanage.entity.book.dto.BookNoteDTO;

/**
 * BookNoteAdminService
 *
 * @author luoyu
 * @date 2018/11/21 12:47
 * @description
 */
public interface BookNoteService extends IService<BookNote> {

    /**
     * 分页查询笔记列表
     * @param page
     * @param limit
     * @param title
     * @return
     */
    PageUtils queryPage(Integer page, Integer limit, String title);

    /**
     * 保存笔记笔记
     * @param blogBookNote
     */
    void saveBookNote(BookNoteDTO blogBookNote);

    /**
     * 批量删除
     * @param bookNoteIds
     */
    void deleteBatch(Integer[] bookNoteIds);

    /**
     * 更新笔记
     * @param blogBookNote
     */
    void updateBookNote(BookNoteDTO blogBookNote);

    /**
     * 获取笔记对象
     * @param bookNoteId
     * @return
     */
    BookNoteDTO getBookNote(Integer bookNoteId);

    /**
     * 判断该类别下是否有笔记
     * @param categoryId
     * @return
     */
    boolean checkByCategory(Integer categoryId);

}
