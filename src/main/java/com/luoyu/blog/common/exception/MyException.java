package com.luoyu.blog.common.exception;

import com.luoyu.blog.common.enums.ResponseEnums;
import lombok.Data;

/**
 * MyException
 *
 * @author luoyu
 * @date 2018/10/07 13:54
 * @description 自定义异常
 */
@Data
public class MyException extends RuntimeException {

    private String msg;
    private int code = 500;

    public MyException(){
        super(ResponseEnums.UNKNOWN.getMsg());
        msg = ResponseEnums.UNKNOWN.getMsg();
    }

    public MyException(ResponseEnums responseEnums, Throwable e){
        super(responseEnums.getMsg(), e);
        msg = responseEnums.getMsg();
        code = responseEnums.getCode();
    }

    public MyException(ResponseEnums responseEnums){
        msg = responseEnums.getMsg();
        code = responseEnums.getCode();
    }

    public MyException(int code, String msg){
        this.msg = msg;
        this.code = code;
    }

}
