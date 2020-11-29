package com.luoyu.blogmanage.common.exception;

import com.luoyu.blogmanage.common.enums.ResponseEnums;
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
        this.msg = ResponseEnums.UNKNOWN.getMsg();
    }

    public MyException(ResponseEnums responseEnums, Throwable e){
        super(responseEnums.getMsg(), e);
        this.msg = responseEnums.getMsg();
        this.code = responseEnums.getCode();
    }

    public MyException(ResponseEnums responseEnums){
        this.msg = responseEnums.getMsg();
        this.code = responseEnums.getCode();
    }

    public MyException(int code, String msg){
        this.msg = msg;
        this.code = code;
    }

}
