package com.luoyu.blogmanage.entity.operation;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class Recommend implements Serializable {

    private static int TOP_NO = 0;
    private static int TOP_YES = 1;

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "推荐的文章Id")
    private Integer linkId;

    @ApiModelProperty(value = "推荐类型")
    private Integer type;

    @ApiModelProperty(value = "顺序")
    private Integer orderNum;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "置顶")
    private Boolean top;

    @ApiModelProperty(value = "发布状态")
    private Boolean publish;

}
