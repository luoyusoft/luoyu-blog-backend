package com.jinhaoxun.blog.project.service.applyservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinhaoxun.blog.project.dao.applymapper.MessageMapper;
import com.jinhaoxun.blog.project.pojo.apply.Message;
import com.jinhaoxun.blog.project.service.applyservice.IMessageService;
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
