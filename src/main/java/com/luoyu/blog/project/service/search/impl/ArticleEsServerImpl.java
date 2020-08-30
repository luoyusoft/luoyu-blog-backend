package com.luoyu.blog.project.service.search.impl;

import com.luoyu.blog.common.constants.ElasticSearchConstants;
import com.luoyu.blog.common.constants.RabbitMqConstants;
import com.luoyu.blog.common.entity.article.Article;
import com.luoyu.blog.common.entity.article.dto.ArticleDTO;
import com.luoyu.blog.common.entity.article.vo.ArticleVO;
import com.luoyu.blog.common.util.ElasticSearchUtils;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.RabbitMqUtils;
import com.luoyu.blog.project.mapper.article.ArticleMapper;
import com.luoyu.blog.project.service.search.ArticleEsServer;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public List<ArticleDTO> searchArticleList(String keyword) throws Exception {
        List<Map<String, Object>> searchRequests = elasticSearchUtils.searchRequest(ElasticSearchConstants.LUOYUBLOG_SEARCH_INDEX, keyword);
        List<ArticleDTO> articleDTOList = new ArrayList<>();
        for(Map<String, Object> x : searchRequests){
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(Integer.valueOf(x.get("id").toString()));
            articleDTO.setCover(x.get("cover").toString());
            articleDTO.setCoverType(Integer.valueOf(x.get("coverType").toString()));
            articleDTO.setTop(Boolean.valueOf(x.get("top").toString()));
            articleDTO.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(x.get("createTime").toString()));
            articleDTO.setReadNum(Long.valueOf(x.get("readNum").toString()));
            articleDTO.setTitle(x.get("title").toString());
            articleDTO.setAuthor(x.get("author").toString());
            articleDTO.setDescription(x.get("description").toString());
            articleDTO.setLikeNum(Long.valueOf(x.get("likeNum").toString()));
            articleDTOList.add(articleDTO);
        }
        return articleDTOList;
    }

    @Override
    public boolean initArticle() throws Exception {
        if(elasticSearchUtils.deleteIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_INDEX)){
            if(elasticSearchUtils.createIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_INDEX)){
                List<ArticleVO> articleVOList = articleMapper.selectArticleList();
                if(articleVOList != null && articleVOList.size() > 0){
                    articleVOList.forEach(x -> {
                        ArticleDTO articleDTO = new ArticleDTO();
                        BeanUtils.copyProperties(x, articleDTO);
                        rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_ES_ROUTINGKEY_ADD, JsonUtils.objectToJson(articleDTO));
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
