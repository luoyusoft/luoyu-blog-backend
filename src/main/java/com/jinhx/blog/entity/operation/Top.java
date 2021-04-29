package com.jinhx.blog.entity.operation;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jinhx.blog.entity.base.BaseEntity;
import com.jinhx.blog.common.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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

    public static final Integer ORDER_NUM_TOP = 1;

    @ApiModelProperty(value = "置顶链接id")
    @NotNull(message="置顶链接id不能为空", groups = {AddGroup.class})
    private Integer linkId;

    @ApiModelProperty(value = "置顶模块")
    @NotNull(message="置顶模块不能为空", groups = {AddGroup.class})
    private Integer module;

    @ApiModelProperty(value = "置顶顺序")
    @NotNull(message="置顶顺序不能为空", groups = {AddGroup.class})
    private Integer orderNum;

    @ApiModelProperty(value = "置顶标题")
    @TableField(exist = false)
    private String title;

}
