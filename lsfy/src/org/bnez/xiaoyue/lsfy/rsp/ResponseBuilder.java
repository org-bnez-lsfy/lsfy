package org.bnez.xiaoyue.lsfy.rsp;

import org.bnez.xiaoyue.lsfy.XiaoyueResponse;

public class ResponseBuilder 
{
	public static final XiaoyueResponse NULL = new XiaoyueResponse();

	private static final String[] _original = {"台州"};
	private static final String[] _adjust = {"胎州"};
	
	public static XiaoyueResponse build(String content, String url, String act)
	{
		XiaoyueResponse rsp = new XiaoyueResponse();
		rsp.setSpeak(buildSpeak(content));
		rsp.setContent(content);
		rsp.setUrl(url);
		rsp.setAct(act);
		return rsp;
	}

	private static String buildSpeak(String content)
	{
		if(content == null)
			return "";
		String cc = toneAdjust(content);
		cc = removeSepcial(cc);
		return cc;
	}

	private static String removeSepcial(String cc)
	{
		return cc.replaceAll("<br/>", "");
	}

	public static XiaoyueResponse build(String content, String url)
	{
		return build(content, url, null);
	}

	public static XiaoyueResponse build(String content)
	{
		return build(content, null, null);
	}

	public static XiaoyueResponse buildEmpty()
	{
		return build("", null, null);
	}

	private static String toneAdjust(String c)
	{
		String a = c;
		for(int i=0; i<_original.length; i++)
			if(a.contains(_original[i]))
				a = a.replaceAll(_original[i], _adjust[i]);
		return a;
	}
	
	public static void main(String[] args)
	{
		XiaoyueResponse r = ResponseBuilder.build("2013-07-23 > 2014-07-22, 台州的结案率是多少");
		System.out.println(r.toString());
	}
}
