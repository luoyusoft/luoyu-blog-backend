package com.luoyu.blog.common.constants;

/**
 * RabbitMqConstants
 *
 * @author luoyu
 * @date 2019/03/16 22:12
 * @description
 */
public class RabbitMqConstants {

    public static final String LUOYUBLOG_ES_ADD_QUEUE = "luoyublog-es-add-queue";

    public static final String LUOYUBLOG_ES_UPDATE_QUEUE = "luoyublog-es-update-queue";

    public static final String LUOYUBLOG_ES_DELETE_QUEUE = "luoyublog-es-delete-queue";

    public static final String LUOYUBLOG_INIT_GITALK_QUEUE = "luoyublog-init-gitalk-queue";

    public static final String LUOYUBLOG_TOPIC_EXCHANGE = "luoyublog.topic.exchange";

    public static final String TOPIC_ES_ROUTINGKEY = "topic.es.#";

    public static final String TOPIC_ES_ROUTINGKEY_ADD = "topic.es.add";

    public static final String TOPIC_ES_ROUTINGKEY_DELETE = "topic.es.delete";

    public static final String TOPIC_ES_ROUTINGKEY_UPDATE = "topic.es.update";

    public static final String TOPIC_GITALK_ROUTINGKEY = "topic.gitalk.#";

    public static final String TOPIC_GITALK_ROUTINGKEY_INIT = "topic.gitalk.init";

}
