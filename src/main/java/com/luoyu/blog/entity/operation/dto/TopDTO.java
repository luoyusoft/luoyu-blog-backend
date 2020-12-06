package com.luoyu.blog.entity.operation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.luoyu.blog.entity.operation.Tag;
import com.luoyu.blog.entity.operation.Top;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * TopDTO
 *
 * @author luoyu
 * @date 2019/02/22 10:49
 * @description
 */
@Data
public class TopDTO extends Top {

    private String description;

    private Long readNum;

    private Long commentNum;

    private Long likeNum;

    private String urlType;

    private String cover;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createTime;

    private List<Tag> tagList;

}
