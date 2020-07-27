package com.luoyu.blog;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
//@EnableAsync
//@MapperScan({"com.luoyu.blog.project.mapper.applymapper","com.luoyu.blog.project.mapper.shiromapper","com.luoyu.blog.project.mapper.quartzmapper"})
//@EnableScheduling
//@ServletComponentScan
@SpringBootApplication
public class LuoYuBlogApplication {
    public static void main(String[] args) {
        log.info("======================== 迈叽叽歪歪，担猛猛开始！========================");
        /**
         * ElasticSearch 所需的临时设置，待解决
         */
        System.setProperty("es.set.netty.runtime.available.processors","false");
        SpringApplication.run(LuoYuBlogApplication.class, args);
        log.info("========================= 兴死有神秀，无BUG哩！=========================");
    }
}