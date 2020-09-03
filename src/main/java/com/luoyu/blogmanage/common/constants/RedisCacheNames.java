package com.luoyu.blogmanage.common.constants;

/**
 * RedisCacheNames
 *
 * @author luoyu
 * @date 2019/07/20 18:09
 * @description
 */
public class RedisCacheNames {

    public final static String PROFIX = "LUOYUBLOG:";

    /**
     * 文章缓存空间名称
     */
    public final static String ARTICLE = PROFIX + "ARTICLE";
    /**
     * 图书缓存空间名称
     */
    public final static String BOOK = PROFIX + "BOOK";

    /**
     * 笔记缓存空间名称
     */
    public final static String BOOKNOTE = PROFIX + "BOOKNOTE";

    /**
     * 友情链接列表
     */
    public final static String LINK = PROFIX + "LINK";

    /**
     * 推荐列表
     */
    public final static String RECOMMEND = PROFIX + "RECOMMEND";

    /**
     * 标签列表
     */
    public final static String TAG = PROFIX + "TAG";

    /**
     * 分类列表
     */
    public final static String CATEGORY = PROFIX +"CATEGORY";

    /**
     * 时光轴
     */
    public static final String TIMELINE =  PROFIX +"TIMELINE";

}
