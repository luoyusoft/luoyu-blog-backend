package com.luoyu.blog.project.service.applyservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luoyu.blog.project.mapper.applymapper.MessageMapper;
import com.luoyu.blog.project.pojo.apply.Message;
import com.luoyu.blog.project.service.applyservice.IMessageService;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 消息服务类
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
