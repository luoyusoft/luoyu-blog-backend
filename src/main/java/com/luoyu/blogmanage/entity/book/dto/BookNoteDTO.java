package com.luoyu.blogmanage.entity.book.dto;

import com.luoyu.blogmanage.entity.book.BookNote;
import com.luoyu.blogmanage.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * BookNote
 *
 * @author luoyu
 * @date 2019/01/08 19:04
 * @description
 */
@Data
public class BookNoteDTO extends BookNote {

    private List<Tag> tagList;

}
