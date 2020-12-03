package com.luoyu.blogmanage.entity.operation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blogmanage.common.validator.group.AddGroup;
import com.luoyu.blogmanage.common.validator.group.UpdateGroup;
import com.luoyu.blogmanage.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
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
    @NotBlank(message="标签链接id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer linkId;

    @ApiModelProperty(value = "标签id")
    @NotBlank(message="标签id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer tagId;

    @ApiModelProperty(value = "标签所属类型：0文章，1阅读")
    @NotBlank(message="所属类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer type;

    public TagLink() {
    }

    public TagLink(Integer linkId, Integer tagId, Integer type) {
        this.linkId =  linkId;
        this.tagId = tagId;
        this.type = type;
    }

}
