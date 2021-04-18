package com.luoyu.blog.entity.log.vo;

import lombok.Data;

/**
 * HomeLogInfoVO
 *
 * @author luoyu
 * @date 2019/01/08 19:04
 * @description
 */
@Data
public class HomeLogInfoVO {

    /**
     * 总PV
     */
    private Integer allPV;

    /**
     * 今天PV
     */
    private Integer todayPV;

    /**
     * 总UV
     */
    private Integer allUV;

    /**
     * 今天UV
     */
    private Integer todayUV;

}
