package com.luoyu.blog.project.controller.rabbitmqcontroller;

import com.luoyu.blog.project.service.rabbitmqservice.IRabbitmqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description RabbitMQ前端控制器
 */
@RequestMapping("/rabbitmq")
@RestController
@Api("RabbitMQ接口")
public class RabbitmqController {

    @Resource
    private IRabbitmqService iRabbitmqService;

    /**
     * @author jinhaoxun
     * @description 发送消息方法
     * @param content 发送的消息
     */
    @PostMapping(value="/consumer", produces = "application/json; charset=UTF-8")
    @ApiOperation("发送消息")
    public void addConsumer(String content){
        iRabbitmqService.sendMsg(content);
    }
}

