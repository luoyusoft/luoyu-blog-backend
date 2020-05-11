package com.jinhaoxun.blog.common.com.exception;

import com.jinhaoxun.blog.common.com.response.ResponseFactory;
import com.jinhaoxun.blog.common.com.response.ResponseMsg;
import com.jinhaoxun.blog.common.com.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 统一的异常处理
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {

	/**
	 * @author jinhaoxun
	 * @description 统一的异常处理方法
	 * @param e 抛出的异常
	 * @return ResponseResult 返回给前端的错误信息提示
	 */
	@ExceptionHandler(value = Exception.class)
	public ResponseResult handleException(Exception e){
		if(e instanceof CustomException) {
			CustomException ex = (CustomException)e;
			log.info("自定义业务异常：msg：" + ex.getMessage() + ",log：" + ex.getLog(), e);
			return ResponseFactory.buildCustomResponse(ex.getCode(),ex.getMessage(), null);
		}else if(e instanceof CustomRuntimeException) {
			CustomRuntimeException ex = (CustomRuntimeException)e;
			log.error("自定义运行时异常：msg：" + ex.getMessage() + ",log：" + ex.getLog(), e);
			return ResponseFactory.buildCustomResponse(ex.getCode(),ex.getMessage(), null);
		}else if(e instanceof BindException) {
			BindException ex = (BindException)e;
			log.error("参数校验异常：msg：" + ex.getBindingResult().getFieldError().getDefaultMessage());
			return ResponseFactory.buildCustomResponse(ResponseMsg.WRONG_PARAM.getCode(),
					ResponseMsg.WRONG_PARAM.getMsg() + "："
							+ ex.getBindingResult().getFieldError().getDefaultMessage(), null);
		}else{
			log.error("统一系统异常：msg：" + e.getMessage(), e);
			return ResponseFactory.buildCustomResponse(ResponseMsg.EXCEPTION.getCode(), ResponseMsg.EXCEPTION.getMsg(), null);
		}
	}
}
