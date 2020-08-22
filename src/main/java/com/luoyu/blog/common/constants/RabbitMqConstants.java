package com.luoyu.blog.common.constants;

/**
 * RabbitMqConstants
 *
 * @author luoyu
 * @date 2019/03/16 22:12
 * @email 571002217@qq.com
 * @description
 */
public class RabbitMqConstants {

    public static final String REFRESH_ES_INDEX_QUEUE = "luoyublog-es-index-queue";

    public static final String INIT_LUOYUBLOG_GITALK_QUEUE = "luoyublog-gitalk-queue";

    public static final String EXCHANGE_NAME = "topic.exchange";

    public static final String REFRESH_ES_INDEX_ROUTINGKEY = "topic.es.#";

    public static final String REFRESH_ES_INDEX_ROUTINGKEY_TEST = "topic.es.test";

    public static final String INIT_LUOYUBLOG_GITALK_ROUTINGKEY = "topic.gitalk.#";

    public static final String INIT_LUOYUBLOG_GITALK_ROUTINGKEY_TEST = "topic.gitalk.test";


}
