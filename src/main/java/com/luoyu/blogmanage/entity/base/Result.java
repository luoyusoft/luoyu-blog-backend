package com.luoyu.blogmanage.entity.base;


import com.luoyu.blogmanage.common.exception.enums.ErrorEnums;

import java.util.HashMap;

/**
 * Result
 *
 * @author luoyu
 * @date 2018/10/07 13:28
 * @description 通用返回类
 */
public class Result extends HashMap<String, Object> {

    public Result() {
        put("code", 200);
        put("msg", "success");
    }

    public static Result ok() {
        return new Result();
    }

    public static Result error() {
        return error(ErrorEnums.UNKNOWN);
    }

    public static Result error(ErrorEnums eEnum) {
        return new Result().put("code", eEnum.getCode()).put("msg", eEnum.getMsg());
    }

    public static Result error(String msg) {
        return new Result().put("msg",msg).put("code", ErrorEnums.UNKNOWN.getCode());
    }

    public static Result error(Integer code , String msg){
        return new Result().put("code",code).put("msg",msg);
    }

    public static Result exception() {
        return exception(ErrorEnums.UNKNOWN);
    }

    public static Result exception(ErrorEnums eEnum) {
        return new Result().put("code", eEnum.getCode()).put("msg", eEnum.getMsg());
    }

    /**
     * 封装业务数据
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Result put(String key, Object value) {
        super.put(key, value);
        //将HashMap对象本身返回
        return this;
    }

}
