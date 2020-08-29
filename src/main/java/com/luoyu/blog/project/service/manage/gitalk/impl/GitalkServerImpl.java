package com.luoyu.blog.project.service.manage.gitalk.impl;

import com.luoyu.blog.common.constants.RabbitMqConstants;
import com.luoyu.blog.common.entity.gitalk.InitGitalkRequest;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.project.service.manage.gitalk.GitalkServer;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class GitalkServerImpl implements GitalkServer {

    // 请求地址前缀
    private static final String GITHUB_REPOS_URL = "https://api.github.com/repos/";
    // git用戶名
    private static final String USER_NAME = "luoyusoft";
    // git博客的仓库名
    private static final String REPO = "luoyublog-gitalk";
    // blogUrl 博客首页地址
    private static final String BLOG_URL = "https://luoyublog.com";
    // 获取到的Token
    private static final String TOKEN = "5ef6ae6387d3ebabb035eeaaec0e1ca92b3e8f6b";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * @param initGitalkRequest
     * @return
     * @throws Exception
     */
    public boolean init(InitGitalkRequest initGitalkRequest){
        String url = GITHUB_REPOS_URL + USER_NAME + "/" + REPO + "/issues";

//        String param = String.format("{\"title\":\"%s\",\"labels\":[\"%s\", \"%s\"],\"body\":\"%s%s\\n\\n\"}"
//                , initGitalkRequest.getTitle() + " | LuoYu Blog", initGitalkRequest.getId(),
//                initGitalkRequest.getType().substring(0, 1).toUpperCase() + initGitalkRequest.getType().substring(1),
//                BLOG_URL, "/" + initGitalkRequest.getType() + "/" + initGitalkRequest.getId());
//        HttpClient client = HttpClients.createDefault();
//        HttpPost post = new HttpPost(url);
//        StringEntity entity = new StringEntity(param, HTTP.UTF_8);
//        post.setHeader("accept", "*/*");
//        post.setHeader("connection", "Keep-Alive");
//        post.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
//        post.setHeader("Authorization", "token " + TOKEN);
//        post.setEntity(entity);
//        HttpResponse response = client.execute(post);
//        log.info(response.getEntity().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "*/*");
        headers.set("connection", "Keep-Alive");
        headers.set("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
        headers.set("Authorization", "token " + TOKEN);

        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("title", initGitalkRequest.getTitle() + " | LuoYu Blog");
        multiValueMap.add("body",  BLOG_URL + "/" + initGitalkRequest.getType() + "/" + initGitalkRequest.getId() + "\\n\\n");

        List<String> labels = new ArrayList<>();
        labels.add(String.valueOf(initGitalkRequest.getId()));
        labels.add(initGitalkRequest.getType().substring(0, 1).toUpperCase() + initGitalkRequest.getType().substring(1));
        multiValueMap.add("labels", labels);

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multiValueMap, headers);

        log.info("请求Github进行Gitalk初始化接口，请求参数：{}", request.toString());
        //构建返回参数
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, request, String.class);
        log.info("请求Github进行Gitalk初始化接口，响应参数：{}", responseEntity.toString());

        if(responseEntity.getStatusCodeValue() != 200){
            return false;
        }
        return true;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConstants.LUOYUBLOG_INIT_GITALK_QUEUE, durable = "true"),
            exchange = @Exchange(
                    value = RabbitMqConstants.LUOYUBLOG_TOPIC_EXCHANGE,
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {RabbitMqConstants.TOPIC_GITALK_ROUTINGKEY}))
    public void initGitalkConsumer(Message message, Channel channel){
        try {
            InitGitalkRequest initGitalkRequest = JsonUtils.jsonToObject(new String(message.getBody()), InitGitalkRequest.class);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            if (this.init(initGitalkRequest)){
                //手动确认消息已经被消费
                log.info("新增文章，进行Gitalk初始化：" + message.toString() + "成功！");
            }else {
                log.info("新增文章，进行Gitalk初始化：" + message.toString() + "失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("新增文章，进行Gitalk初始化：" + message.toString() + "失败！");
            log.info("手动确认Gitalk初始化消息已经被消费失败！");
        }
    }

}
