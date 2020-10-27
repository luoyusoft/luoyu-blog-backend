package com.luoyu.blogmanage.entity.book.vo;

import com.luoyu.blogmanage.entity.book.Book;
import com.luoyu.blogmanage.entity.book.BookNote;
import com.luoyu.blogmanage.entity.book.BookSense;
import com.luoyu.blogmanage.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * ReadBookVo
 *
 * @author luoyu
 * @date 2019/01/29 14:17
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
