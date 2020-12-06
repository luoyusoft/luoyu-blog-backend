package com.luoyu.blog.entity.operation.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.luoyu.blog.entity.operation.Recommend;
import com.luoyu.blog.entity.operation.Tag;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * RecommendDTO
 *
 * @author luoyu
 * @date 2019/02/22 10:49
 * @description
 */
@Data
public class RecommendDTO extends Recommend {

    private String description;

    private Long readNum;

    private Long commentNum;

    private Long likeNum;

    private String urlType;

    private String cover;

    private List<Tag> tagList;

}
