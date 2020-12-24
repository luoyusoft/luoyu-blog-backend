package com.luoyu.blog.entity.log;

import com.baomidou.mybatisplus.annotation.TableName;
import com.luoyu.blog.entity.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 阅读日志
 * </p>
 *
 * @author luoyu
 * @since 2019-02-15
 */
@Data
@ApiModel(value="ViewLog对象", description="浏览日志")
@TableName("log_view")
public class LogView extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "浏览类型")
    private String type;

    @ApiModelProperty(value = "请求方法")
    private String method;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "执行时长(毫秒)")
    private Long time;

    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "国家")
    private String country;

    @ApiModelProperty(value = "省份")
    private String region;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "浏览器名字")
    private String borderName;

    @ApiModelProperty(value = "浏览器版本")
    private String borderVersion;

    @ApiModelProperty(value = "设备生产厂商")
    private String deviceManufacturer;

    @ApiModelProperty(value = "设备类型")
    private String deviceType;

    @ApiModelProperty(value = "操作系统的版本号")
    private String osVersion;

}
