package com.luoyu.blog.entity.messagewall.vo;

import com.luoyu.blog.entity.messagewall.MessageWall;
import lombok.Data;

/**
 * <p>
 * 留言墙
 * </p>
 *
 * @author luoyu
 * @since 2018-11-07
 */
@Data
public class MessageWallVO extends MessageWall {

    /**
     * 回复name
     */
    private String replyName;

}
