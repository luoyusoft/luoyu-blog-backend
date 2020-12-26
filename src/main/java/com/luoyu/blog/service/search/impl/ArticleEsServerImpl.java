package com.luoyu.blog.service.search.impl;

import com.luoyu.blog.common.constants.ElasticSearchConstants;
import com.luoyu.blog.common.constants.RabbitMQConstants;
import com.luoyu.blog.common.enums.ModuleEnum;
import com.luoyu.blog.common.util.ElasticSearchUtils;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.RabbitMQUtils;
import com.luoyu.blog.entity.article.Article;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.article.vo.ArticleVO;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.service.operation.TagService;
import com.luoyu.blog.service.search.ArticleEsServer;
import com.rabbitmq.client.Channel;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
public class ArticleEsServerImpl implements ArticleEsServer {

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;

    @Resource
    private RabbitMQUtils rabbitmqUtils;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private TagService tagService;

    @Override
    public boolean initArticleList() throws Exception {
        if(elasticSearchUtils.deleteIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX)){
            if(elasticSearchUtils.createIndex(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX)){
                List<ArticleDTO> articleDTOList = articleMapper.selectArticleDTOList();
                XxlJobLogger.log("初始化es文章数据，查到个数：{}", articleDTOList.size());
                log.info("初始化es文章数据，查到个数：{}", articleDTOList.size());
                if(!CollectionUtils.isEmpty(articleDTOList)){
                    articleDTOList.forEach(x -> {
                        ArticleVO articleVO = new ArticleVO();
                        BeanUtils.copyProperties(x, articleVO);
                        rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_ARTICLE_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_ES_ARTICLE_ADD_ROUTINGKEY, JsonUtils.objectToJson(articleVO));
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
    @RabbitListener(queues = RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_ADD_QUEUE)
    public void addListener(Message message, Channel channel){
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if(message != null && message.getBody() != null){
                Article article = JsonUtils.jsonToObject(new String(message.getBody()), Article.class);
                elasticSearchUtils.addDocument(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX, article.getId().toString(), JsonUtils.objectToJson(article));
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
    @RabbitListener(queues = RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_UPDATE_QUEUE)
    public void updateListener(Message message, Channel channel){
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if(message != null && message.getBody() != null){
                Article article = JsonUtils.jsonToObject(new String(message.getBody()), Article.class);
                elasticSearchUtils.updateDocument(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX, article.getId().toString(), JsonUtils.objectToJson(article));
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
    @RabbitListener(queues = RabbitMQConstants.LUOYUBLOG_ES_ARTICLE_DELETE_QUEUE)
    public void deleteListener(Message message, Channel channel){
        try {
            //手动确认消息已经被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            StringBuffer articleIdsS = new StringBuffer();
            if(message != null && message.getBody() != null){
                Integer[] articleIds = JsonUtils.jsonToObject(new String(message.getBody()), Integer[].class);
                for (int i = 0; i < articleIds.length; i++) {
                    elasticSearchUtils.deleteDocument(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX, articleIds[i].toString());
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

    @Override
    public List<ArticleVO> searchArticleList(String keyword) throws Exception {
        List<String> highlightBuilderList = Arrays.asList("title", "description");
        List<Map<String, Object>> searchRequests = elasticSearchUtils.searchRequest(ElasticSearchConstants.LUOYUBLOG_SEARCH_ARTICLE_INDEX, keyword, highlightBuilderList, highlightBuilderList);
        List<ArticleVO> articleVOList = new ArrayList<>();
        for(Map<String, Object> x : searchRequests){
            ArticleVO articleVO = new ArticleVO();
            articleVO.setId(Integer.valueOf(x.get("id").toString()));
            articleVO.setCover(x.get("cover").toString());
            articleVO.setCoverType(Integer.valueOf(x.get("coverType").toString()));
            articleVO.setCreateTime(LocalDateTime.parse(x.get("createTime").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            articleVO.setUpdateTime(LocalDateTime.parse(x.get("updateTime").toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            articleVO.setReadNum(Long.valueOf(x.get("readNum").toString()));
            articleVO.setCommentNum(Long.valueOf(x.get("commentNum").toString()));
            articleVO.setTitle(x.get("title").toString());
            articleVO.setAuthor(x.get("author").toString());
            articleVO.setDescription(x.get("description").toString());
            articleVO.setLikeNum(Long.valueOf(x.get("likeNum").toString()));
            articleVO.setTop(false);
            articleVO.setTagList(tagService.listByLinkId(articleVO.getId(), ModuleEnum.ARTICLE.getCode()));
            articleVOList.add(articleVO);
        }
        return articleVOList;
    }

}
