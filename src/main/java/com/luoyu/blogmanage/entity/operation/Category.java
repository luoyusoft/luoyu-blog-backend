package com.luoyu.blogmanage.entity.operation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blogmanage.entity.base.BaseEntity;
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
    @NotBlank(message = "名称不能为空")
    private String name;

    @ApiModelProperty(value = "类型")
    @NotNull(message = "类型不能为空")
    private Integer type;

    @ApiModelProperty(value = "级别")
    @NotNull(message = "级别不能为空")
    @TableField(value = "`rank`")
    private Integer rank;

    @ApiModelProperty(value = "父主键")
    @NotNull(message = "父主键不能为空")
    private Integer parentId;

    @ApiModelProperty(value = "父主键名称")
    @TableField(exist = false)
    private String parentName;

}
