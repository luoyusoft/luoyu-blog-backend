package com.luoyu.blog.project.controller.rocketmqcontroller;

import com.luoyu.blog.project.pojo.rocketmq.AddConsumerReq;
import com.luoyu.blog.project.pojo.rocketmq.AddMessageReq;
import com.luoyu.blog.common.response.ResponseResult;
import com.luoyu.blog.project.service.rocketmqservice.IRocketmqService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description RocketMQ前端控制器
 */
@RequestMapping("/rocketmq")
@RestController
@Api("RocketMQ接口")
public class RocketmqController {

    @Autowired
    private IRocketmqService iRocketmqService;

    /**
     * @author jinhaoxun
     * @description 开启消费者
     * @param addConsumerReq 消费者信息参数
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @PostMapping(value="/consumer", produces = "application/json; charset=UTF-8")
    @ApiOperation("开启消费者")
    public ResponseResult addConsumer(@Validated @RequestBody AddConsumerReq addConsumerReq) throws Exception{
        return iRocketmqService.addConsumer(addConsumerReq);
    }

    /**
     * @author jinhaoxun
     * @description 关闭消费者
     * @return ResponseResult 成功提示信息
     */
    @DeleteMapping(value="/consumer", produces = "application/json; charset=UTF-8")
    @ApiOperation("关闭消费者")
    public ResponseResult deleteConsumer(){
        return iRocketmqService.deleteConsumer();
    }

    /**
     * @author jinhaoxun
     * @description 开启生产者
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @PostMapping(value="/producer", produces = "application/json; charset=UTF-8")
    @ApiOperation("开启生产者")
    public ResponseResult addProducer() throws Exception{
        return iRocketmqService.addProducer();
    }

    /**
     * @author jinhaoxun
     * @description 发送消息
     * @param addMessageReq 发送的消息
     * @return ResponseResult 成功提示信息
     * @throws Exception
     */
    @PostMapping(value="/message", produces = "application/json; charset=UTF-8")
    @ApiOperation("发送消息")
    public ResponseResult addMessage(@Validated @RequestBody AddMessageReq addMessageReq) throws Exception{
        return iRocketmqService.addMessage(addMessageReq);
    }

    /**
     * @author jinhaoxun
     * @description 关闭生产者
     * @return ResponseResult 成功提示信息
     */
    @DeleteMapping(value="/producer", produces = "application/json; charset=UTF-8")
    @ApiOperation("关闭生产者")
    public ResponseResult deleteProducer(){
        return iRocketmqService.deleteProducer();
    }
}

