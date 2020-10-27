package com.luoyu.blogmanage.entity.book.dto;

import com.luoyu.blogmanage.entity.book.Book;
import com.luoyu.blogmanage.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * ReadBookDto
 *
 * @author luoyu
 * @date 2019/01/28 16:44
 * @description
 */
@Data
public class BookDTO extends Book {

    List<Tag> tagList;
}
