package com.jinhx.blog.common.constants;

/**
 * RabbitMqConstants
 *
 * @author luoyu
 * @date 2019/03/16 22:12
 * @description
 */
public class RabbitMQConstants {

    /********************** rabbitmq-exchange ********************************/

    public static final String BLOG_ARTICLE_TOPIC_EXCHANGE = "blog.article.topic.exchange";

    public static final String BLOG_VIDEO_TOPIC_EXCHANGE = "blog.video.topic.exchange";

    public static final String BLOG_GITALK_TOPIC_EXCHANGE = "blog.gitalk.topic.exchange";

    /********************** rabbitmq-queue ********************************/

    public static final String BLOG_ES_ARTICLE_ADD_QUEUE = "blog-es-article-add-queue";

    public static final String BLOG_ES_ARTICLE_UPDATE_QUEUE = "blog-es-article-update-queue";

    public static final String BLOG_ES_ARTICLE_DELETE_QUEUE = "blog-es-article-delete-queue";


    public static final String BLOG_ES_VIDEO_ADD_QUEUE = "blog-es-video-add-queue";

    public static final String BLOG_ES_VIDEO_UPDATE_QUEUE = "blog-es-video-update-queue";

    public static final String BLOG_ES_VIDEO_DELETE_QUEUE = "blog-es-video-delete-queue";


    public static final String BLOG_GITALK_INIT_QUEUE = "blog-gitalk-init-queue";

    /********************** rabbitmq-routingkey ********************************/

    public static final String TOPIC_ES_ARTICLE_ROUTINGKEY = "topic.es.article.#";

    public static final String TOPIC_ES_ARTICLE_ADD_ROUTINGKEY = "topic.es.article.add";

    public static final String TOPIC_ES_ARTICLE_DELETE_ROUTINGKEY = "topic.es.article.delete";

    public static final String TOPIC_ES_ARTICLE_UPDATE_ROUTINGKEY = "topic.es.article.update";


    public static final String TOPIC_ES_VIDEO_ROUTINGKEY = "topic.es.video.#";

    public static final String TOPIC_ES_VIDEO_ADD_ROUTINGKEY = "topic.es.video.add";

    public static final String TOPIC_ES_VIDEO_DELETE_ROUTINGKEY = "topic.es.video.delete";

    public static final String TOPIC_ES_VIDEO_UPDATE_ROUTINGKEY = "topic.es.video.update";


    public static final String TOPIC_GITALK_ROUTINGKEY = "topic.gitalk.#";

    public static final String TOPIC_GITALK_INIT_ROUTINGKEY = "topic.gitalk.init";

}
