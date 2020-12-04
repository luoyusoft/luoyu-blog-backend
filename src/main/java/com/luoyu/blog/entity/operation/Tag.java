package com.luoyu.blog.entity.operation;

import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blog.common.validator.group.AddGroup;
import com.luoyu.blog.common.validator.group.UpdateGroup;
import com.luoyu.blog.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 标签
 * </p>
 *
 * @author luoyu
 * @since 2018-11-07
 */
@Data
@ApiModel(value="Tag对象", description="标签")
@EqualsAndHashCode(callSuper = false)
@TableName("tag")
public class Tag extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "标签名字")
    @NotBlank(message="标签名字不能为空", groups = {AddGroup.class})
    private String name;

    @ApiModelProperty(value = "标签所属类型：0文章，1阅读")
    @NotNull(message="标签所属类型不能为空", groups = {AddGroup.class})
    private Integer type;

}
