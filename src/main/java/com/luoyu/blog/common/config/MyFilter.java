package com.luoyu.blog.common.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.FilterConfig;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Configuration
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //TODO something
    }

    @Override
    public void destroy() {
        //TODO something
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //取Body数据
        MyRequestWrapper requestWrapper = new MyRequestWrapper(request);
        String body = requestWrapper.getBody();
        //TODO something
        filterChain.doFilter(requestWrapper != null ? requestWrapper :request,servletResponse);
    }

}
