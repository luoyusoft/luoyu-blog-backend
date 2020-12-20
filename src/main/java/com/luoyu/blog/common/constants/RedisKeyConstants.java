package com.luoyu.blog.common.constants;

/**
 * RedisKeyConstants
 *
 * @author luoyu
 * @date 2018/10/20 13:44
 * @description redis baseKey管理常量
 */
public class RedisKeyConstants {

    /**
     * 后台管理验证码key
     */
    public final static String MANAGE_SYS_CAPTCHA = "manage:sys:captcha:";

    /**
     * 后台管理用户token key
     */
    public final static String MANAGE_SYS_USER_TOKEN = "manage:sys:user:token:";

    /**
     * 最热阅读
     */
    public final static String HOST_READ_LIST = "host:read:list:";

    /**
     * 用户数据 Key前缀标识
     */
    public final static String CHAT_USER_PREFIX = "luoyublog:chat:user:";

    /**
     * 群发消息Session Key前缀标识
     */
    public final static String CHAT_COMMON_PREFIX = "luoyublog:chat:common:";

    /**
     * 推送至指定用户消息
     *      推送方Session Key前缀标识
     */
    public final static String CHAT_FROM_PREFIX = "luoyublog:chat:from:";

    /**
     * 推送至指定用户消息
     *      接收方Session Key前缀标识
     */
    public final static String CHAT_TO_PREFIX = "luoyublog:chat:to:";

    /**
     * RedisTemplate 根据Key模糊匹配查询前缀
     */
    public final static String REDIS_MATCH_PREFIX = "*";

}
