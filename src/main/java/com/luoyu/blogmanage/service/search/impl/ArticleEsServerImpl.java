package com.luoyu.blogmanage.service.search.impl;

import com.luoyu.blogmanage.common.constants.ElasticSearchConstants;
import com.luoyu.blogmanage.common.constants.RabbitMqConstants;
import com.luoyu.blogmanage.common.util.ElasticSearchUtils;
import com.luoyu.blogmanage.entity.article.Article;
import com.luoyu.blogmanage.entity.article.vo.ArticleVO;
import com.luoyu.blogmanage.common.util.JsonUtils;
import com.luoyu.blogmanage.common.util.RabbitMqUtils;
import com.luoyu.blogmanage.entity.article.dto.ArticleDTO;
import com.luoyu.blogmanage.mapper.article.ArticleMapper;
import com.luoyu.blogmanage.service.search.ArticleEsServer;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class ArticleEsServerImpl implements ArticleEsServer {

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    @Resource
    private RabbitMqUtils rabbitmqUtils;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public boolean initArticle() throws Exception {
        if(elasticSearchUtils.deleteIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_INDEX)){
            if(elasticSearchUtils.createIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_INDEX)){
                List<ArticleDTO> articleDTOList = articleMapper.selectArticleList();
                if(articleDTOList != null && articleDTOList.size() > 0){
                    articleDTOList.forEach(x -> {
                        ArticleVO articleVO = new ArticleVO();
                        BeanUtils.copyProperties(x, articleVO);
                        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_ADD, JsonUtils.objectToJson(articleVO));
                    });
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 新增文章，rabbitmq监听器，添加到es中
     * @return
     */
    @RabbitListener(queues = RabbitMqConstants.LUOYUBLOG_ES_ADD_QUEUE)
    public void addListener(Message message, Channel channel){
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if(message != null && message.getBody() != null){
                Article article = JsonUtils.jsonToObject(new String(message.getBody()), Article.class);
                elasticSearchUtils.addDocument(ElasticSearchConstants.LUOYUBLOG_SEARCH_INDEX, article.getId().toString(), JsonUtils.objectToJson(article));
                log.info("新增文章，rabbitmq监听器，添加到es中成功：id:" + new String(message.getBody()));
            }else {
                log.info("新增文章，rabbitmq监听器，添加到es中失败：article:" + new String(message.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("新增文章，rabbitmq监听器，手动确认消息已经被消费失败，article:" + new String(message.getBody()));
        }
    }

    /**
     * 更新文章，rabbitmq监听器，更新到es
     * @return
     */
    @RabbitListener(queues = RabbitMqConstants.LUOYUBLOG_ES_UPDATE_QUEUE)
    public void updateListener(Message message, Channel channel){
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if(message != null && message.getBody() != null){
                Article article = JsonUtils.jsonToObject(new String(message.getBody()), Article.class);
                elasticSearchUtils.updateDocument(ElasticSearchConstants.LUOYUBLOG_SEARCH_INDEX, article.getId().toString(), JsonUtils.objectToJson(article));
                log.info("更新文章，rabbitmq监听器，更新到es成功：id:" + new String(message.getBody()));
            }else {
                log.info("更新文章，rabbitmq监听器，更新到es失败：article:" + new String(message.getBody()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("更新文章，rabbitmq监听器，手动确认消息已经被消费失败，article:" + new String(message.getBody()));
        }
    }

    /**
     * 删除文章，rabbitmq监听器，从es中删除
     * @return
     */
    @RabbitListener(queues = RabbitMqConstants.LUOYUBLOG_ES_DELETE_QUEUE)
    public void deleteListener(Message message, Channel channel){
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            StringBuffer articleIdsS = new StringBuffer();
            if(message != null && message.getBody() != null){
                Integer[] articleIds = JsonUtils.jsonToObject(new String(message.getBody()), Integer[].class);
                for (int i = 0; i < articleIds.length; i++) {
                    elasticSearchUtils.deleteDocument(ElasticSearchConstants.LUOYUBLOG_SEARCH_INDEX, articleIds[i].toString());
                    articleIdsS.append(articleIds[i] + ",");
                }
                log.info("删除文章，rabbitmq监听器，从es中删除成功：id:" + articleIdsS);
            }else {
                log.info("删除文章，rabbitmq监听器，从es中删除失败：article:" + articleIdsS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            StringBuffer articleIdsS = null;
            Integer[] articleIds = JsonUtils.jsonToObject(new String(message.getBody()), Integer[].class);
            for (int i = 0; i < articleIds.length; i++) {
                articleIdsS.append(i + ",");
            }
            log.info("删除文章，rabbitmq监听器，手动确认消息已经被消费失败，article:" + articleIdsS);
        }
    }

}
