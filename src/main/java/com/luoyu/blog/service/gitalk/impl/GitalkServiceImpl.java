package com.luoyu.blog.service.gitalk.impl;

import com.luoyu.blog.common.api.GitalkApi;
import com.luoyu.blog.common.constants.GitalkConstants;
import com.luoyu.blog.common.constants.RabbitMQConstants;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.RabbitMQUtils;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.gitalk.InitGitalkRequest;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.mapper.video.VideoMapper;
import com.luoyu.blog.service.gitalk.GitalkService;
import com.rabbitmq.client.Channel;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class GitalkServiceImpl implements GitalkService {

    @Autowired
    private GitalkApi gitalkApi;

    @Resource
    private RabbitMQUtils rabbitmqUtils;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private VideoMapper videoMapper;

    /**
     * @return
     */
    @Override
    public boolean initArticleList(){
        List<ArticleDTO> articleDTOList = articleMapper.selectArticleDTOList();
        XxlJobLogger.log("初始化gitalk文章数据，查到个数：{}", articleDTOList.size());
        log.info("初始化gitalk文章数据，查到个数：{}", articleDTOList.size());
        if (!CollectionUtils.isEmpty(articleDTOList)){
            articleDTOList.forEach(x -> {
                InitGitalkRequest initGitalkRequest = new InitGitalkRequest();
                initGitalkRequest.setId(x.getId());
                initGitalkRequest.setType(GitalkConstants.GITALK_TYPE_ARTICLE);
                initGitalkRequest.setTitle(x.getTitle());
                rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_GITALK_INIT_ROUTINGKEY, JsonUtils.objectToJson(initGitalkRequest));
            });
        }
        return true;
    }

    /**
     * @return
     */
    @Override
    public boolean initVideoList(){
        List<VideoDTO> videoDTOList = videoMapper.selectVideoDTOList();
        XxlJobLogger.log("初始化gitalk视频数据，查到个数：{}", videoDTOList.size());
        log.info("初始化gitalk视频数据，查到个数：{}", videoDTOList.size());
        if (!CollectionUtils.isEmpty(videoDTOList)){
            videoDTOList.forEach(x -> {
                InitGitalkRequest initGitalkRequest = new InitGitalkRequest();
                initGitalkRequest.setId(x.getId());
                initGitalkRequest.setType(GitalkConstants.GITALK_TYPE_VIDEO);
                initGitalkRequest.setTitle(x.getTitle());
                rabbitmqUtils.sendByRoutingKey(RabbitMQConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE, RabbitMQConstants.TOPIC_GITALK_INIT_ROUTINGKEY, JsonUtils.objectToJson(initGitalkRequest));
            });
        }
        return true;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMQConstants.LUOYUBLOG_GITALK_INIT_QUEUE, durable = "true"),
            exchange = @Exchange(
                    value = RabbitMQConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {RabbitMQConstants.TOPIC_GITALK_ROUTINGKEY}))
    public void initGitalkConsumer(Message message, Channel channel){
        try {
            InitGitalkRequest initGitalkRequest = JsonUtils.jsonToObject(new String(message.getBody()), InitGitalkRequest.class);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if (gitalkApi.initArticle(initGitalkRequest)){
                //手动确认消息已经被消费
                log.info("新增或更新标题，进行Gitalk初始化：" + message.toString() + "成功！");
            }else {
                log.info("新增或更新标题，进行Gitalk初始化：" + message.toString() + "失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("新增或更新标题，进行Gitalk初始化：" + message.toString() + "失败！");
            log.info("手动确认Gitalk初始化消息已经被消费失败！");
        }
    }

}
