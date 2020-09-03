package com.luoyu.blogmanage.common.entity.book.dto;

import com.luoyu.blogmanage.common.entity.book.BookNote;
import com.luoyu.blogmanage.common.entity.operation.Tag;
import lombok.Data;

import java.util.List;

/**
 * BookNote
 *
 * @author bobbi
 * @date 2019/01/08 19:04
 * @email 571002217@qq.com
 * @description
 */
@Data
public class BookNoteDTO extends BookNote {

    private List<Tag> tagList;

}
