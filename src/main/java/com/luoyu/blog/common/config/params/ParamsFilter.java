package com.luoyu.blog.common.config.params;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ParamsFilter implements Filter {

    // 文件传输相关接口不需要二次获取参数
    private final static List<String> UPLOAD_URLS = Arrays.asList("/manage/file/resource/minio/upload", "/manage/file/resource/qiniuyun/upload", "/manage/file/resource/minio/chunkUpload");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        for (String item : UPLOAD_URLS){
            if (item.equals(request.getServletPath())){
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        //取Body数据
        ParamsHttpServletRequestWrapper requestWrapper = new ParamsHttpServletRequestWrapper(request);
        filterChain.doFilter(requestWrapper, servletResponse);
    }

}
