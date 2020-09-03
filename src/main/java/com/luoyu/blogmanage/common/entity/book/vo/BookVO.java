package com.luoyu.blogmanage.common.entity.book.vo;

import com.luoyu.blogmanage.common.entity.book.Book;
import com.luoyu.blogmanage.common.entity.book.BookNote;
import com.luoyu.blogmanage.common.entity.book.BookSense;
import com.luoyu.blogmanage.common.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * ReadBookVo
 *
 * @author bobbi
 * @date 2019/01/29 14:17
 * @email 571002217@qq.com
 * @description
 */
@Data
public class BookVO extends Book {

    /**
     * 所属分类，以逗号分隔
     */
    private String categoryListStr;

    /**
     * 所属标签
     */
    private List<Tag> tagList;

    /**
     * 所属笔记
     */
    private List<BookNote> bookNoteList;

    /**
     * 读后感
     */
    private BookSense bookSense;
}
