package com.luoyu.blog.common.exception;

import com.luoyu.blog.entity.base.Response;
import com.luoyu.blog.common.enums.ResponseEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * MyExceptionHandler
 *
 * @author luoyu
 * @date 2018/10/07 14:33
 * @description 统一异常处理器
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {

    /**
     * 处理自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    public Response handleMyException(MyException e){
        log.error(e.getMessage(), e);
        return Response.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Response handlerNoFoundException(Exception e){
        log.error(e.getMessage(),e);
        return Response.fail(ResponseEnums.PATH_NOT_FOUND);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public Response handleDuplicateKeyException(DuplicateKeyException e){
        log.error(e.getMessage(),e);
        return Response.fail(ResponseEnums.DUPLICATE_KEY);
    }

    @ExceptionHandler(AuthorizationException.class)
    public Response handleAuthorizationException(AuthorizationException e){
        log.error(e.getMessage(),e);
        return Response.fail(ResponseEnums.NO_AUTH);
    }

    @ExceptionHandler(Exception.class)
    public Response handleException(Exception e){
        log.error(e.getMessage(),e);
        return Response.fail();
    }

}
