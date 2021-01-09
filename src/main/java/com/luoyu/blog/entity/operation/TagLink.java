package com.luoyu.blog.entity.operation;

import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 文章标签多对多维护表
 * </p>
 *
 * @author luoyu
 * @since 2019-01-07
 */
@Data
@ApiModel(value="TagLink对象", description="标签多对多维护表")
@EqualsAndHashCode(callSuper = false)
@TableName("tag_link")
public class TagLink extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签链接id")
    @NotNull(message="标签链接id不能为空", groups = {AddGroup.class})
    private Integer linkId;

    @ApiModelProperty(value = "标签id")
    @NotNull(message="标签id不能为空", groups = {AddGroup.class})
    private Integer tagId;

    @ApiModelProperty(value = "标签所属模块：0文章，1阅读")
    @NotNull(message="所属模块不能为空", groups = {AddGroup.class})
    private Integer module;

    public TagLink() {
    }

    public TagLink(Integer linkId, Integer tagId, Integer module) {
        this.linkId =  linkId;
        this.tagId = tagId;
        this.module = module;
    }

}
