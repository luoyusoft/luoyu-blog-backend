package com.luoyu.blog.framework.nottify.email;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

// org.json 第三方库请自行下载编译，或者在以下链接下载使用 jdk 1.7 的版本
// http://share.weiyun.com/630a8c65e9fd497f3687b3546d0b839e

public class EmailSingleSender {
	String accesskey;
	String secretkey;
	//同时支持http和https两种协议，具体根据自己实际情况使用。
    String url = "https://live.kewail.com/directmail/v1/singleSendMail";
	//String url = "http://127.0.0.1:8080/live.kewail.com/directmail/v1/singleSendMail";

	EmailSenderUtil util = new EmailSenderUtil();

	public EmailSingleSender(String accesskey,String secretkey) throws Exception {
		this.accesskey = accesskey;
		this.secretkey = secretkey;
	}
	
	/**
	 * 单个邮件发送
	 * @param type 邮件类型，0 事务投递，其他的为商业投递量
	 * @param fromEmail 用户管理控制台中配置的发信地址
	 * @param toEmail  邮件接收地址
	 * @param needToReply  是否需要回复邮件
	 * @param replyEmail 回复邮件的邮件地址
	 * @param fromAlias  发信人昵称
	 * @param subject  邮件主题
	 * @param htmlBody 邮件内容
	 * @param clickTrace 数据跟踪功能  1 为打开数据跟踪功能; 0 为关闭数据跟踪功能。该参数默认值为0。
	 * @param ext  拓展字段
	 * @return
	 * @throws Exception
	 */
	public EmailSingleSenderResult send(
			int type,
			String fromEmail,
			String toEmail,
			String fromAlias,
			boolean needToReply,
			String replyEmail,
			String subject,
			String htmlBody,
			String clickTrace,
			String ext) throws Exception {
		if(type<0||type>1) {
			type=0;
		}

		if (null == ext) {
			ext = "";
		}

		// 按照协议组织 post 请求包体
        long random = util.getRandom();
        long curTime = System.currentTimeMillis()/1000;

		JSONObject data = new JSONObject();

        data.put("sig", util.strToHash(String.format("secretkey=%s&random=%d&time=%d&fromEmail=%s",
        		secretkey, random, curTime, fromEmail)));
        data.put("time", curTime);
        data.put("type", type);
        data.put("fromEmail", fromEmail);
        data.put("needToReply", needToReply);
        data.put("toEmail", toEmail);
        data.put("fromAlias", fromAlias);
        data.put("subject", subject);
        data.put("htmlBody", htmlBody);
        data.put("clickTrace", clickTrace);
        data.put("ext", ext);
        if(needToReply) {
        	 data.put("replyEmail", replyEmail);
        }else {
        	 data.put("replyEmail", "");
        }

        // 与上面的 random 必须一致
		String wholeUrl = String.format("%s?accesskey=%s&random=%d", url, accesskey, random);
        HttpURLConnection conn = util.getPostHttpConn(wholeUrl);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream(), "utf-8");
        wr.write(data.toString());
        wr.flush();
        
        System.out.println(data.toString());

        // 显示 POST 请求返回的内容
        StringBuilder sb = new StringBuilder();
        int httpRspCode = conn.getResponseCode();
        EmailSingleSenderResult result;
        if (httpRspCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            System.out.println(sb.toString());
            
            JSONObject json = new JSONObject(sb.toString());
            result = util.jsonToSmsSingleSenderResult(json);
        } else {
        	result = new EmailSingleSenderResult();
        	result.result = httpRspCode;
        	result.errmsg = "http error " + httpRspCode + " " + conn.getResponseMessage();
        }
        return result;
	}

}
