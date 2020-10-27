package com.luoyu.blogmanage.common.exception;

import com.luoyu.blogmanage.common.exception.enums.ErrorEnums;
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
        super(ErrorEnums.UNKNOWN.getMsg());
        this.msg= ErrorEnums.UNKNOWN.getMsg();
    }


    public MyException(ErrorEnums eEnum, Throwable e){
        super(eEnum.getMsg(),e);
        this.msg=eEnum.getMsg();
        this.code=eEnum.getCode();
    }

    public MyException(ErrorEnums eEnum){
        this.msg=eEnum.getMsg();
        this.code=eEnum.getCode();
    }

    public MyException(String exception){
       this.msg=exception;
    }

}
