package com.luoyu.blog.framework.redis.jedisutil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description Jedis配置
 */
@Slf4j
@Configuration
public class JedisConfig extends CachingConfigurerSupport {

    /**
     * Redis服务器地址
     */
    @Value("${spring.redis.host}")
    private String host;
    /**
     * Redis服务器连接密码（默认为空）
     */
    @Value("${spring.redis.password}")
    private String password;
    /**
     * Redis数据库索引（默认为0）
     */
    @Value("${spring.redis.database}")
    private int database;
    /**
     * Redis服务器连接端口
     */
    @Value("${spring.redis.port}")
    private int port;
    /**
     * Redis连接超时时间（毫秒）
     */
    @Value("${spring.redis.timeout}")
    private int timeout;

    /**
     * @author  jinhaoxun
     * @description JedisPool配置文件
     * @return JedisPoolConfig
     */
    @Bean(name = "jedisPoolConfig")
    @ConfigurationProperties(prefix = "spring.redis.pool-config")
    public JedisPoolConfig getRedisConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    /**
     * @author  jinhaoxun
     * @description jedisPool连接配置，默认为首字母小写的方法名jedisPool
     * @return JedisPool
     */
    @Bean(name = "jedisPool")
    public JedisPool jedisPool(@Qualifier(value = "jedisPoolConfig") final JedisPoolConfig jedisPoolConfig) {
        log.info("Jedis工具开始创建...");
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
        log.info("Jedis工具创建成功，host：{},port：{}！", host, port);
        return jedisPool;
    }
}