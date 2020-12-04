package com.luoyu.blog.entity.oss;

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
 * 云存储资源表
 * </p>
 *
 * @author luoyu
 * @since 2018-11-30
 */
@Data
@ApiModel(value="OssResource对象", description="云存储资源表")
@EqualsAndHashCode(callSuper = false)
@TableName("oss_resource")
public class OssResource extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称")
    @NotBlank(message="名称不能为空", groups = {AddGroup.class})
    private String name;

    @ApiModelProperty(value = "url地址")
    @NotBlank(message="url地址不能为空", groups = {AddGroup.class})
    private String url;

    public OssResource (String url, String name){
        this.name=name;
        this.url=url;
    }

}
