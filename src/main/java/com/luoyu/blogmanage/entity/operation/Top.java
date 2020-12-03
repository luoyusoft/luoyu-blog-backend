package com.luoyu.blogmanage.entity.operation;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * 推荐
 * </p>
 *
 * @author luoyu
 * @since 2019-02-22
 */
@Data
@ApiModel(value="Top对象", description="置顶")
@EqualsAndHashCode(callSuper = false)
@TableName("top")
public class Top extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "置顶链接id")
    @NotBlank(message="置顶链接id不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer linkId;

    @ApiModelProperty(value = "置顶类型")
    @NotBlank(message="置顶类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer type;

    @ApiModelProperty(value = "置顶顺序")
    @NotBlank(message="置顶顺序不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer orderNum;

    @ApiModelProperty(value = "标题")
    @TableField(exist = false)
    private String title;

}
