package com.luoyu.blog.common.api;

import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.entity.sys.IPInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * DateUtils
 *
 * @author luoyu
 * @date 2018/10/20 13:26
 * @description IP工具类
 */
@Slf4j
@Component
public class IPApi {

    private static final String IP_URL = "http://ip-api.com/json/";

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取IP信息
     */
    public IPInfo getIpInfo(String ip) {
        log.info("请求查询ip信息接口，请求参数：{}", ip);
        //构建返回参数
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(IP_URL + ip + "?lang=zh-CN", String.class);
        log.info("请求查询ip信息接口，响应参数：{}", responseEntity.toString());
        
        if (responseEntity.getStatusCodeValue() != 200){
            return null;
        }
        
        if (responseEntity.getBody() == null){
            return null;
        }

        return JsonUtils.jsonToObject(responseEntity.getBody(), IPInfo.class);
    }
	
}
