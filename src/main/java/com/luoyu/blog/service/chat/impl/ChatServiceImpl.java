package com.luoyu.blog.service.chat.impl;

import com.luoyu.blog.common.constants.RedisKeyConstants;
import com.luoyu.blog.common.util.DateUtils;
import com.luoyu.blog.common.util.JsonUtils;
import com.luoyu.blog.entity.chat.Message;
import com.luoyu.blog.entity.chat.User;
import com.luoyu.blog.entity.chat.vo.UserVO;
import com.luoyu.blog.service.chat.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author luoyu
 * @date 2019-06-14
 */
@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    private static final Long EXPIRES_TIME = 1000 * 60 * 60 * 24 * 30L;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public User findById(String id) {
        if (id != null) {
            String value = null;
            if (id.startsWith(RedisKeyConstants.CHAT_USER_PREFIX)) {
                value = redisTemplate.boundValueOps(id).get();
            } else {
                value = redisTemplate.boundValueOps(RedisKeyConstants.CHAT_USER_PREFIX + id).get();
            }
            if (value != null) {
                return JsonUtils.jsonToObject(value, User.class);
            }
        }
        return null;
    }

    @Override
    public void pushMessage(String fromId, String toId, String message) {
        Message entity = new Message();
        entity.setMessage(message);
        entity.setFrom(this.findById(fromId));
        entity.setCreateTime(DateUtils.getNowTimeString());
        if (toId != null) {
            //查询接收方信息
            entity.setTo(this.findById(toId));
            //单个用户推送
            this.push(entity, RedisKeyConstants.CHAT_FROM_PREFIX + fromId + RedisKeyConstants.CHAT_TO_PREFIX + toId);
        } else {
            //公共消息 -- 群组
            entity.setTo(null);
            this.push(entity, RedisKeyConstants.CHAT_COMMON_PREFIX + fromId);
        }
    }

    /**
     * 推送消息
     *
     * @param entity Session value
     * @param key    Session key
     */
    private void push(Message entity, String key) {
        //这里按照 PREFIX_ID 格式，作为KEY储存消息记录
        //但一个用户可能推送很多消息，VALUE应该是数组
        List<Message> list = new ArrayList<>();
        String value = redisTemplate.boundValueOps(key).get();
        if (value == null) {
            //第一次推送消息
            list.add(entity);
        } else {
            //第n次推送消息
            list = Objects.requireNonNull(JsonUtils.jsonToList(value, Message.class));
            list.add(entity);
        }
        redisTemplate.boundValueOps(key).set(JsonUtils.objectToJson(list));
    }

    @Override
    public List<UserVO> onlineList() {
        List<UserVO> list = new ArrayList<>();
        Set<String> keys = redisTemplate.keys(RedisKeyConstants.CHAT_USER_PREFIX + RedisKeyConstants.REDIS_MATCH_PREFIX);
        if (keys != null && keys.size() > 0) {
            keys.forEach(key -> {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(this.findById(key), userVO);
                list.add(userVO);
            });
        }
        return list;
    }

    @Override
    public List<Message> commonList() {
        List<Message> list = new ArrayList<>();
        Set<String> keys = redisTemplate.keys(RedisKeyConstants.CHAT_COMMON_PREFIX + RedisKeyConstants.REDIS_MATCH_PREFIX);
        if (keys != null && keys.size() > 0) {
            keys.forEach(key -> {
                String value = redisTemplate.boundValueOps(key).get();
                List<Message> messageList = Objects.requireNonNull(JsonUtils.jsonToList(value, Message.class));
                list.addAll(messageList);
            });
        }
        this.sort(list);
        return list;
    }

    @Override
    public List<Message> selfList(String fromId, String toId) {
        List<Message> list = new ArrayList<>();
        //A -> B
        String fromTo = redisTemplate.boundValueOps(RedisKeyConstants.CHAT_FROM_PREFIX + fromId + RedisKeyConstants.CHAT_TO_PREFIX + toId).get();
        //B -> A
        String toFrom = redisTemplate.boundValueOps(RedisKeyConstants.CHAT_FROM_PREFIX + toId + RedisKeyConstants.CHAT_TO_PREFIX + fromId).get();

        List<Message> fromToList = JsonUtils.jsonToList(fromTo, Message.class);
        List<Message> toFromList = JsonUtils.jsonToList(toFrom, Message.class);
        if (!CollectionUtils.isEmpty(fromToList)) {
            JsonUtils.jsonToList(fromTo, Message.class);
            list.addAll(fromToList);
        }
        if (!CollectionUtils.isEmpty(toFromList)) {
            list.addAll(toFromList);
        }

        if (list.size() > 0) {
            this.sort(list);
            return list;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void delete(String id) {
        if (id != null) {
            log.info("从Redis中删除此Key: " + id);
            redisTemplate.delete(RedisKeyConstants.CHAT_USER_PREFIX + id);
        }
    }

    @Override
    public void clearUser() {
        log.info("清除注册时间超过30分钟的账户，以及其会话信息");
        List<UserVO> userVOList = this.onlineList();
        userVOList.forEach(user -> {
            if ((DateUtils.getNowTimeLong() - DateUtils.convertTimeToLong(user.getCreateTime())) >= EXPIRES_TIME) {
                this.delete(user.getId());
                if (redisTemplate.boundValueOps(RedisKeyConstants.CHAT_COMMON_PREFIX + user.getId()).get() != null) {
                    redisTemplate.delete(RedisKeyConstants.CHAT_COMMON_PREFIX + user.getId());
                }
                if (redisTemplate.boundValueOps(RedisKeyConstants.CHAT_FROM_PREFIX + user.getId()).get() != null) {
                    redisTemplate.delete(RedisKeyConstants.CHAT_FROM_PREFIX + user.getId());
                }
            }
        });
    }

    /**
     * 对List集合中的数据按照时间顺序排序
     *
     * @param list List<Message>
     */
    private void sort(List<Message> list) {
        list.sort(Comparator.comparing(Message::getCreateTime));
    }

}
