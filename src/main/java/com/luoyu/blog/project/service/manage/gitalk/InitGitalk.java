package com.luoyu.blog.project.service.manage.gitalk;

import com.luoyu.blog.common.constants.RabbitMqConstants;
import com.luoyu.blog.common.entity.gitalk.InitGitalkRequest;
import com.luoyu.blog.common.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InitGitalk {

    // git用戶名
    private static final String USER_NAME = "luoyusoft";
    // git博客的仓库名
    private static final String REPO = "luoyublog-gitalk";
    // blogUrl 博客首页地址
    private static final String BLOG_URL = "https://luoyublog.com";
    // 获取到的Token
    private static final String TOKEN = "5ef6ae6387d3ebabb035eeaaec0e1ca92b3e8f6b";

    /**
     * @param initGitalkRequest
     * @return
     * @throws Exception
     */
    public void init(InitGitalkRequest initGitalkRequest) throws Exception{
        String url = "https://api.github.com/repos/" + USER_NAME + "/" + REPO + "/issues";
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
        HttpResponse response = client.execute(post);
        log.info(response.getEntity().toString());
    }

    @RabbitListener(queues= RabbitMqConstants.INIT_LUOYUBLOG_GITALK_QUEUE)
    public void initGitalkConsumer(Message message){
        try {
            InitGitalkRequest initGitalkRequest = JsonUtils.toObj(new String(message.getBody()), InitGitalkRequest.class);
            this.init(initGitalkRequest);
            log.info("新增文章，进行 Gitalk 初始化：" + message.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
