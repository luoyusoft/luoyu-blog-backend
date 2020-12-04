package com.luoyu.blog.entity.operation;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.entity.base.BaseEntity;
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
@ApiModel(value="Recommend对象", description="推荐")
@EqualsAndHashCode(callSuper = false)
@TableName("recommend")
public class Recommend extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "推荐链接id")
    @NotBlank(message="推荐链接id不能为空", groups = {AddGroup.class})
    private Integer linkId;

    @ApiModelProperty(value = "推荐类型")
    @NotBlank(message="推荐类型不能为空", groups = {AddGroup.class})
    private Integer type;

    @ApiModelProperty(value = "推荐顺序")
    @NotBlank(message="推荐顺序不能为空", groups = {AddGroup.class})
    private Integer orderNum;

    @ApiModelProperty(value = "推荐标题")
    @TableField(exist = false)
    private String title;

}
