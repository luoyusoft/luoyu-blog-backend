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
    @NotBlank(message="标签名字不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;

    @ApiModelProperty(value = "标签所属类型：0文章，1阅读")
    @NotBlank(message="标签所属类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer type;

}
