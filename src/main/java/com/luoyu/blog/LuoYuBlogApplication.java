package com.luoyu.blog;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@Slf4j
@EnableDiscoveryClient
@MapperScan({"com.luoyu.blog.mapper"})
@SpringBootApplication
public class LuoYuBlogApplication {
    public static void main(String[] args) {
        log.info("======================== 迈叽叽歪歪，担猛猛开始！========================");
        SpringApplication.run(LuoYuBlogApplication.class, args);
        log.info("========================= 兴死有神秀，无BUG哩！=========================");
    }
}