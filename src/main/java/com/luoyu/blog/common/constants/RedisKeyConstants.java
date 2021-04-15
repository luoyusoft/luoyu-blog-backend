package com.luoyu.blog.common.constants;

/**
 * RedisKeyConstants
 *
 * @author luoyu
 * @date 2018/10/20 13:44
 * @description redis Key常量
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

    /********************** portal ********************************/

    public final static String PROFIX = "luoyublog:";

    /**
     * 文章缓存空间名称
     */
    public final static String ARTICLE = PROFIX + "article";

    /**
     * 文章列表缓存空间名称
     */
    public final static String ARTICLES = PROFIX + "articles";

    /**
     * 视频缓存空间名称
     */
    public final static String VIDEO = PROFIX + "video";

    /**
     * 视频列表缓存空间名称
     */
    public final static String VIDEOS = PROFIX + "videos";

    /**
     * 友情链接列表
     */
    public final static String FRIENDLINKS = PROFIX + "friendlinks";

    /**
     * 推荐列表
     */
    public final static String RECOMMENDS = PROFIX + "recommends";

    /**
     * 标签列表
     */
    public final static String TAGS = PROFIX + "tags";

    /**
     * 分类列表
     */
    public final static String CATEGORYS = PROFIX + "categorys";

    /**
     * 时光轴
     */
    public static final String TIMELINES =  PROFIX + "timelines";

    /**
     * 搜索
     */
    public final static String SEARCHS = PROFIX + "search";

    /**
     * 用户数据 Key前缀标识
     */
    public final static String CHAT_USER_PREFIX = "luoyublog:chat:user:";

    /**
     * 用户名集合 Key
     */
    public final static String CHAT_NAME = "luoyublog:chat:name";

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
