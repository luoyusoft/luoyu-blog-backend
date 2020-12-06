package com.luoyu.blog.entity.log;

import com.baomidou.mybatisplus.annotation.*;
import com.luoyu.blog.common.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 阅读日志
 * </p>
 *
 * @author luoyu
 * @since 2019-02-15
 */
@Data
@ApiModel(value="LogLike对象", description="点赞日志")
@TableName("log_like")
public class LogLike implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    @Id
    private Integer id;

    @ApiModelProperty(value = "类型")
    @NotBlank(message="类型不能为空", groups = {AddGroup.class})
    private String type;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "执行时长(毫秒)")
    @NotNull(message = "执行时长(毫秒)不能为空", groups = {AddGroup.class})
    private Long time;

    @ApiModelProperty(value = "ip地址")
    @NotBlank(message = "ip地址不能为空", groups = {AddGroup.class})
    private String ip;

    @ApiModelProperty(value = "创建时间")
    @Field(type = FieldType.Date, format = DateFormat.none)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
