package com.jinhx.blog.entity.operation;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jinhx.blog.entity.base.BaseEntity;
import com.jinhx.blog.common.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 类别表
 * </p>
 *
 * @author luoyu
 * @since 2018-12-17
 */
@Data
@ApiModel(value="Category对象", description="类别")
@EqualsAndHashCode(callSuper = false)
@TableName("category")
public class Category extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class})
    private String name;

    @ApiModelProperty(value = "模块")
    @NotNull(message = "模块不能为空", groups = {AddGroup.class})
    private Integer module;

    @ApiModelProperty(value = "级别")
    @NotNull(message = "级别不能为空", groups = {AddGroup.class})
    @TableField(value = "`rank`")
    private Integer rank;

    @ApiModelProperty(value = "父主键")
    @NotNull(message = "父主键不能为空", groups = {AddGroup.class})
    private Integer parentId;

    @ApiModelProperty(value = "父主键名称")
    @TableField(exist = false)
    private String parentName;

}
