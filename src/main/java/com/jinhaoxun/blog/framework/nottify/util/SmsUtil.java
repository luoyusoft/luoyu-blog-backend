package com.jinhaoxun.blog.framework.tt.nottify.util;

import com.jinhaoxun.blog.common.exception.ExceptionFactory;
import com.jinhaoxun.blog.common.response.ResponseFactory;
import com.jinhaoxun.blog.common.response.ResponseMsg;
import com.jinhaoxun.blog.common.response.ResponseResult;
import com.jinhaoxun.blog.framework.tt.nottify.sms.SmsSingleSender;
import com.jinhaoxun.blog.framework.tt.nottify.sms.SmsSingleSenderResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 发短信工具类
 */
@Component
public class SmsUtil {

	@Resource
	private ExceptionFactory exceptionFactory;

	/**
	 * @author jinhaoxun
	 * @description 生成随机数字方法
	 * @param n 生成的随机位数
	 * @return String 返回生成的随机数
	 */
	private String getCode(int n){
		StringBuilder code = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			//每次随机出一个数字（0-9）
			int r = random.nextInt(10);
			//把每次随机出的数字拼在一起
			code.append(r);
		}
		return code.toString();
	}

	/**
	 * @author jinhaoxun
	 * @description 组装发送的短信内容方法
	 * @param code 组装发送的短信内容
	 * @return String 组装后的短信内容
	 */
	public String getMessage(String code){
		String head = "【ApeCome】尊敬的用户：您本次的验证码为：";
		String end = "，工作人员不会索取，请勿泄漏。";
		return head+code+end;
	}

	/**
	 * @author jinhaoxun
	 * @description 发送短信方法
	 * @param phone 要发送的目标手机
	 * @return ResponseResult 秒数
	 * @throws Exception
	 */
	@Async(value = "taskExecutor")
    public ResponseResult getSms(String phone) throws Exception{
		//请根据实际 accesskey 和 secretkey 进行开发
		String accesskey = "5b238f330cf2af10d90aaa49";
		String secretkey ="43c6de83a68e47abbff4e78f0b93d606";
		//初始化单发
		SmsSingleSender singleSender = new SmsSingleSender(accesskey, secretkey);
		SmsSingleSenderResult singleSenderResult;
		String code = getCode(6);
		String message = getMessage(code);
		//普通单发,注意前面必须为【】符号包含，置于头或者尾部。
		singleSenderResult = singleSender.send(0, "86", phone, message, "", "");
		//result为0时发送成功
		if(singleSenderResult.result != 0){
			throw exceptionFactory.build(ResponseMsg.GET_PHONE_CODE_FAIL.getCode(),ResponseMsg.GET_PHONE_CODE_FAIL.getMsg());
		}
		return ResponseFactory.buildSuccessResponse(code);
		//语音验证码发送
		/*SmsVoiceVerifyCodeSender smsVoiceVerifyCodeSender = new SmsVoiceVerifyCodeSender(accesskey,secretkey);
		SmsVoiceVerifyCodeSenderResult smsVoiceVerifyCodeSenderResult = smsVoiceVerifyCodeSender.send("86",phoneNumber, "444144",2,"");
		System.out.println(smsVoiceVerifyCodeSenderResult);*/
    }
}
