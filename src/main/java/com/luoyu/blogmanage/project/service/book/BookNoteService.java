package com.luoyu.blogmanage.project.service.book;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blogmanage.common.entity.book.BookNote;
import com.luoyu.blogmanage.common.entity.book.dto.BookNoteDTO;
import com.luoyu.blogmanage.common.util.PageUtils;

import java.util.Map;

/**
 * BookNoteAdminService
 *
 * @author bobbi
 * @date 2018/11/21 12:47
 * @email 571002217@qq.com
 * @description
 */
public interface BookNoteService extends IService<BookNote> {

    /**
     * 分页查询笔记列表
     * @param params
     * @return
     */
    PageUtils queryPage(Map<String, Object> params);

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
