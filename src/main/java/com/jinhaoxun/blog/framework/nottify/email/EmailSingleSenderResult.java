package com.jinhaoxun.blog.framework.tt.nottify.email;

public class EmailSingleSenderResult {
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
	public String errmsg ;
	public Integer surplus;
	public String sequenceId;
	@Override
	public String toString() {
		return "EmailSingleSenderResult [response=" + result + ", errmsg=" + errmsg + ", surplus=" + surplus
				+ ", sequenceId=" + sequenceId + "]";
	}

}
