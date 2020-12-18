package com.luoyu.blog.service.gitalk.impl;

import com.luoyu.blog.common.constants.GitalkConstants;
import com.luoyu.blog.common.constants.RabbitMqConstants;
import com.luoyu.blog.entity.article.dto.ArticleDTO;
import com.luoyu.blog.entity.gitalk.InitGitalkRequest;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.common.util.RabbitMqUtils;
import com.luoyu.blog.entity.video.dto.VideoDTO;
import com.luoyu.blog.mapper.article.ArticleMapper;
import com.luoyu.blog.mapper.video.VideoMapper;
import com.luoyu.blog.service.gitalk.GitalkService;
import com.rabbitmq.client.Channel;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RefreshScope
public class GitalkServiceImpl implements GitalkService {

    // 请求地址前缀
    @Value("${gitalk.repos-url}")
    private String GITHUB_REPOS_URL;

    // git用戶名
    @Value("${gitalk.username}")
    private String USERNAME;

    // git博客的仓库名
    @Value("${gitalk.repo}")
    private String REPO;

    // blogUrl 博客首页地址
    @Value("${gitalk.blog-url}")
    private String BLOG_URL;

    // 获取到的Token
    @Value("${gitalk.token}")
    private String TOKEN;

    @Resource
    private RabbitMqUtils rabbitmqUtils;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private VideoMapper videoMapper;

    /**
     * @param initGitalkRequest
     * @return
     */
    public boolean initArticle(InitGitalkRequest initGitalkRequest) throws Exception {
        String url = GITHUB_REPOS_URL + USERNAME + "/" + REPO + "/issues";

        String param = String.format("{\"title\":\"%s\",\"labels\":[\"%s\", \"%s\"],\"body\":\"%s%s\\n\\n\"}"
                , initGitalkRequest.getTitle() + " | LuoYu Blog", initGitalkRequest.getId(),
                initGitalkRequest.getType().substring(0, 1).toUpperCase() + initGitalkRequest.getType().substring(1),
                BLOG_URL, "/" + initGitalkRequest.getType() + "/" + initGitalkRequest.getId());
        HttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        StringEntity entity = new StringEntity(param, HTTP.UTF_8);
        post.setHeader("accept", "*/*");
        post.setHeader("connection", "Keep-Alive");
        post.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
        post.setHeader("Authorization", "token " + TOKEN);
        post.setEntity(entity);
        log.info("请求Github进行Gitalk初始化接口，请求参数：{}", post.getEntity().toString());
        HttpResponse response = client.execute(post);
        log.info("请求Github进行Gitalk初始化接口，响应参数：{}", response.getEntity().toString());

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("accept", "*/*");
//        headers.set("connection", "Keep-Alive");
//        headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
//        headers.set("Authorization", "token " + TOKEN);
//
//        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
//        multiValueMap.add("title", initGitalkRequest.getTitle() + " | LuoYu Blog");
//        multiValueMap.add("body",  BLOG_URL + "/" + initGitalkRequest.getType() + "/" + initGitalkRequest.getId() + "\\n\\n");
//
//        List<String> labels = new ArrayList<>();
//        labels.add(String.valueOf(initGitalkRequest.getId()));
//        labels.add(initGitalkRequest.getType().substring(0, 1).toUpperCase() + initGitalkRequest.getType().substring(1));
//        multiValueMap.add("labels", labels);
//
//        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap, headers);
//
//        log.info("请求Github进行Gitalk初始化接口，请求参数：{}", request.toString());
//        //构建返回参数
//        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
//        log.info("请求Github进行Gitalk初始化接口，响应参数：{}", responseEntity.toString());

        if(response.getStatusLine().getStatusCode() != 200){
            return false;
        }
        return true;
    }

    /**
     * @return
     */
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
                rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_GITALK_INIT_ROUTINGKEY, JsonUtils.objectToJson(initGitalkRequest));
            });
        }
        return true;
    }

    /**
     * @return
     */
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
                rabbitmqUtils.sendByRoutingKey(RabbitMqConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE, RabbitMqConstants.TOPIC_GITALK_INIT_ROUTINGKEY, JsonUtils.objectToJson(initGitalkRequest));
            });
        }
        return true;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstants.LUOYUBLOG_GITALK_INIT_QUEUE, durable = "true"),
            exchange = @Exchange(
                    value = RabbitMqConstants.LUOYUBLOG_GITALK_TOPIC_EXCHANGE,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {RabbitMqConstants.TOPIC_GITALK_ROUTINGKEY}))
    public void initGitalkConsumer(Message message, Channel channel){
        try {
            InitGitalkRequest initGitalkRequest = JsonUtils.jsonToObject(new String(message.getBody()), InitGitalkRequest.class);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if (this.initArticle(initGitalkRequest)){
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
