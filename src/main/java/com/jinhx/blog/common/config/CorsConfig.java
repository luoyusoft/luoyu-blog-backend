package com.jinhx.blog.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CorsConfig
 *
 * @author luoyu
 * @date 2018/10/20 17:12
 * @description 跨域配置
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 允许任何域名使用
                .allowedHeaders("*") // 允许任何头
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600L); // 解决跨域请求两次，预检请求的有效期，单位为秒
    }

}
