package com.luoyu.blog.framework.nottify.sms;

public class SmsSingleSenderResult {
/*
{
    "response": 0,
    "errmsg": "OK", 
    "ext": "", 
    "sid": "xxxxxxx", 
    "fee": 1
}
 */
	public int result;
	public String errMsg = "";
	public String ext = "";
	public String sid = "";
	public int fee;
	
	@Override
	public String toString() {
		return String.format(
				"SmsSingleSenderResult\nresponse %d\nerrMsg %s\next %s\nsid %s\nfee %d",
				result, errMsg, ext, sid, fee);		
	}
}
