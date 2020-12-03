package com.luoyu.blogmanage.entity.article;

import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blogmanage.entity.base.BaseEntity;
import com.luoyu.blogmanage.common.validator.group.AddGroup;
import com.luoyu.blogmanage.common.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author luoyu
 * @since 2018-11-07
 */
@Data
@ApiModel(value="BlogArticle对象", description="文章")
@EqualsAndHashCode(callSuper = false)
@TableName("article")
public class Article extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章标题")
    @NotBlank(message="文章标题不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String title;

    @ApiModelProperty(value = "文章描述")
    private String description;

    @ApiModelProperty(value = "文章作者")
    @NotBlank(message="文章作者不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String author;

    @ApiModelProperty(value = "文章内容")
    private String content;

    @ApiModelProperty(value = "阅读量")
    private Long readNum;

    @ApiModelProperty(value = "点赞量")
    private Long likeNum;

    @ApiModelProperty(value = "评论量")
    private Long commentNum;

    @ApiModelProperty(value = "封面")
    private String cover;

    @ApiModelProperty(value = "文章展示类别,0:普通，1：大图片，2：无图片")
    private Integer coverType;

    @ApiModelProperty(value = "文章分类类别")
    @NotBlank(message="文章分类类别不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String categoryId;

    @ApiModelProperty(value = "发布状态")
    private Boolean publish;

    @ApiModelProperty(value = "格式化后的内容")
    @NotBlank(message="文章内容不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String contentFormat;

}
