package com.luoyu.blog.project.controller.search;

import com.google.common.collect.Lists;
import com.luoyu.blog.common.base.Result;
import com.luoyu.blog.common.constants.RabbitMqConstants;
import com.luoyu.blog.common.entity.article.Article;
import com.luoyu.blog.project.service.manage.article.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * ArticleEsController
 *
 * @author luoyu
 * @date 2019/03/13 15:04
 * @description
 */
@RestController
@Slf4j
public class ArticleEsController {

    @Resource
    private ArticleService articleService;

    /**
     * 搜索标题，描述，内容
     * @param keywords
     * @return
     */
    @GetMapping("articles/search")
    public Result search(@RequestParam("keywords") String keywords){
        // 对所有索引进行搜索
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(keywords);

//        Iterable<Article> listIt =  articleRepository.search(queryBuilder);

        //Iterable转list
//        List<Article> articleList= Lists.newArrayList(listIt);

        return Result.ok().put("articleList",null);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstants.REFRESH_ES_INDEX_QUEUE, durable = "true"),
            exchange = @Exchange(
                    value = RabbitMqConstants.EXCHANGE_NAME,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {RabbitMqConstants.REFRESH_ES_INDEX_ROUTINGKEY}))
    public void refresh(Message message){
        //手动确认消息已经被消费
//        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info(message.toString());
    }

}
