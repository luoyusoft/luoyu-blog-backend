package com.jinhaoxun.blog.framework.tt.nottify.util;

import com.jinhaoxun.blog.common.exception.ExceptionFactory;
import com.jinhaoxun.blog.common.response.ResponseFactory;
import com.jinhaoxun.blog.common.response.ResponseMsg;
import com.jinhaoxun.blog.common.response.ResponseResult;
import com.jinhaoxun.blog.framework.tt.nottify.email.EmailSingleSender;
import com.jinhaoxun.blog.framework.tt.nottify.email.EmailSingleSenderResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Random;
/*import com.kewail.sdk.email.yun.EmailSingleSender;
import com.kewail.sdk.email.yun.EmailSingleSenderResult;*/

/**
 * @version 1.0
 * @author jinhaoxun
 * @date 2018-05-09
 * @description 发邮件工具类
 */
@Component
public class EmailUtil {

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
	 * @description 组装发送的邮件内容方法
	 * @param code 组装发送的邮件内容
	 * @return String 组装后的邮件内容
	 */
	private String getMessage(String code){
		String head = "<h1>尊敬的用户：您本次的验证码为：";
		String end = "，工作人员不会索取，请勿泄漏。</h1>";
		return head+code+end;
	}

	/**
	 * @author jinhaoxun
	 * @description 发送邮件方法
	 * @param toEmail 要发送的目标邮箱
	 * @return ResponseResult 秒数
	 * @throws Exception
	 */
	@Async(value = "taskExecutor")
    public ResponseResult getEmail(String toEmail) throws Exception{
		//请根据实际 accesskey 和 secretkey 进行开发
		String accesskey = "5b238f330cf2af10d90aaa49";
		String secretkey = "43c6de83a68e47abbff4e78f0b93d606";
		// 邮件类型，0 事务投递，其他的为商业投递量
		int type=0;
		// 拓展字段
		String ext="";
		// 必须 管理控制台中配置的发信地址(登陆后台查看发信地址)
		String fromEmail="mail@applyservice.shuojianghu.com";
		// 必须 ,如果为true是的时候，replyEmail必填，false的时候replyEmail可以为空
		Boolean needToReply=false;
		// 如果needToReply为true是的时候,则为必填,false的时候replyEmail可以为空
		String replyEmail="";
		// 可选 发信人昵称
		String fromAlias="【ApeCome】";
		// 必须 邮件主题。
		String subject="账号注册验证码";
		// 必须 邮件 html 正文。
		String code = getCode(6);
		String message = getMessage(code);
		// 可选 取值范围 0~1: 1 为打开数据跟踪功能; 0 为关闭数据跟踪功能。该参数默认值为
		String clickTrace="0";

		EmailSingleSender singleSender = new EmailSingleSender(accesskey, secretkey);
		EmailSingleSenderResult singleSenderResult=singleSender.send(type, fromEmail, toEmail, fromAlias, needToReply, replyEmail, subject, message, clickTrace, ext);
		System.out.println(singleSenderResult);
		if (singleSenderResult.result != 0){
			throw exceptionFactory.build(ResponseMsg.GET_EMAIL_CODE_FAIL.getCode(),ResponseMsg.GET_EMAIL_CODE_FAIL.getMsg());
		}
		return ResponseFactory.buildSuccessResponse(code);
    }
}
