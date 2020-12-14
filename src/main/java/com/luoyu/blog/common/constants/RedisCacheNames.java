package com.luoyu.blog.common.constants;

/**
 * RedisCacheNames
 *
 * @author luoyu
 * @date 2019/07/20 18:09
 * @description
 */
public class RedisCacheNames {

    public final static String PROFIX = "luoyublog:";

    /**
     * 文章缓存空间名称
     */
    public final static String ARTICLE = PROFIX + "article";
    /**
     * 图书缓存空间名称
     */
    public final static String VIDEO = PROFIX + "video";

    /**
     * 友情链接列表
     */
    public final static String LINK = PROFIX + "link";

    /**
     * 推荐列表
     */
    public final static String RECOMMEND = PROFIX + "recommend";

    /**
     * 置顶列表
     */
    public final static String TOP = PROFIX + "top";

    /**
     * 标签列表
     */
    public final static String TAG = PROFIX + "tag";

    /**
     * 分类列表
     */
    public final static String CATEGORY = PROFIX + "category";

    /**
     * 时光轴
     */
    public static final String TIMELINE =  PROFIX + "timeline";

}
