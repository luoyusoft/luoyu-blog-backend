package com.luoyu.blog.entity.video;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * <p>
 * 视频
 * </p>
 *
 * @author luoyu
 * @since 2018-11-07
 */
@Data
@ApiModel(value="Video对象", description="视频表")
@EqualsAndHashCode(callSuper = false)
@TableName("video")
public class Video extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "视频标题")
    @NotBlank(message="视频标题不能为空", groups = {AddGroup.class})
    private String title;

    @ApiModelProperty(value = "视频又名")
    private String alternateName;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "视频地址")
    @NotBlank(message="视频地址不能为空", groups = {AddGroup.class})
    private String videoUrl;

    @ApiModelProperty(value = "上传者")
    @NotBlank(message="上传者不能为空", groups = {AddGroup.class})
    private String author;

    @ApiModelProperty(value = "视频分类类别（存在多级分类，用逗号隔开）")
    @NotBlank(message="视频分类类别不能为空", groups = {AddGroup.class})
    private String categoryId;

    @ApiModelProperty(value = "制片国家/地区")
    @NotBlank(message="制片国家/地区不能为空", groups = {AddGroup.class})
    private String productionRegion;

    @ApiModelProperty(value = "导演")
    @NotBlank(message="导演不能为空", groups = {AddGroup.class})
    private String director;

    @ApiModelProperty(value = "上映日期")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message="上映日期不能为空", groups = {AddGroup.class})
    private LocalDate releaseTime;

    @ApiModelProperty(value = "片长（格式：HH:mm:ss）")
    @NotBlank(message="片长不能为空", groups = {AddGroup.class})
    private String duration;

    @ApiModelProperty(value = "语言")
    @NotBlank(message="语言不能为空", groups = {AddGroup.class})
    private String language;

    @ApiModelProperty(value = "主演")
    @NotBlank(message="主演不能为空", groups = {AddGroup.class})
    private String toStar;

    @ApiModelProperty(value = "评分")
    private String score;

    @ApiModelProperty(value = "编剧")
    @NotBlank(message="编剧不能为空", groups = {AddGroup.class})
    private String screenwriter;

    @ApiModelProperty(value = "剧情简介")
    @NotBlank(message="剧情简介不能为空", groups = {AddGroup.class})
    private String synopsis;

    @ApiModelProperty(value = "发布状态")
    private Boolean publish;

    @ApiModelProperty(value = "观看量")
    private Long watchNum;

    @ApiModelProperty(value = "点赞量")
    private Long likeNum;

    @ApiModelProperty(value = "观看量")
    private Long commentNum;

}
