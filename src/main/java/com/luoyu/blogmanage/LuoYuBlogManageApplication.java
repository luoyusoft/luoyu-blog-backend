package com.luoyu.blogmanage;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@MapperScan({"com.luoyu.blogmanage.project.mapper"})
@SpringBootApplication
public class LuoYuBlogManageApplication {
    public static void main(String[] args) {
        log.info("======================== 迈叽叽歪歪，担猛猛开始！========================");
        SpringApplication.run(LuoYuBlogManageApplication.class, args);
        log.info("========================= 兴死有神秀，无BUG哩！=========================");
    }
}