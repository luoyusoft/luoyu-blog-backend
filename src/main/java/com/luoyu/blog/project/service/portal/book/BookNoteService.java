package com.luoyu.blog.project.service.portal.book;

import com.baomidou.mybatisplus.extension.service.IService;
import com.luoyu.blog.common.entity.book.BookNote;
import com.luoyu.blog.common.entity.book.vo.BookNoteVO;
import com.luoyu.blog.common.util.PageUtils;

import java.util.List;
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
     * 分页分类获取列表
     * @param params
     * @return
     */
    PageUtils queryPageCondition(Map<String, Object> params);

    /**
     * 获取简单对象
     * @param bookNoteId
     * @return
     */
    BookNoteVO getSimpleBookNoteVo(Integer bookNoteId);

    /**
     * 获取简单List
     * @param bookId
     * @return
     */
    List<BookNote> listSimpleBookNote(Integer bookId);
}
