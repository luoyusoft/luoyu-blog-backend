package com.jinhaoxun.blog.common.com.response;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2019-08-09
 * @description 响应结果生成工具类
 */
public class ResponseFactory {

    /**
     * @author jinhaoxun
     * @description 返回成功结果（请求成功,自定义 data）方法
     * @param data 响应数据
     * @return ResponseResult
     */
    public static ResponseResult buildSuccessResponse(Object data) {
        return new ResponseResult(ResponseMsg.SUCCESS.getCode(),ResponseMsg.SUCCESS.msg,data);
    }

    /**
     * @author jinhaoxun
     * @description 返回成功结果（请求成功，自定义 msg）方法
     * @param msg 响应信息
     * @return ResponseResult
     */
    public static ResponseResult buildSuccessResponse(String msg) {
        return new ResponseResult(ResponseMsg.SUCCESS.getCode(),msg,null);
    }

    /**
     * @author jinhaoxun
     * @description 返回成功结果（请求成功，自定义 msg,data）方法
     * @param data 响应数据
     * @param msg 响应信息
     * @return ResponseResult
     */
    public static ResponseResult buildSuccessResponse(Object data, String msg) {
        return new ResponseResult(ResponseMsg.SUCCESS.getCode(),msg,data);
    }

    /**
     * @author jinhaoxun
     * @description 返回自定义结果（其他情况）方法
     * @param resultCode 响应状态码
     * @param msg 响应信息
     * @param data 响应数据
     * @return ResponseResult
     */
    public static ResponseResult buildCustomResponse(Integer resultCode, String msg, Object data) {
        return new ResponseResult(resultCode,msg,data);
    }
}
