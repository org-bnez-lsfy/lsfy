package org.bnez.xiaoyue.lsfy;

import java.io.Serializable;

import net.sf.json.JSONObject;

public class XiaoyueResponse implements Serializable
{

	private static final long serialVersionUID = -6641498765618236303L;

	public static final XiaoyueResponse NULL = new XiaoyueResponse();

	private String content;
	private String speak;
	private String url;
	private String act;
	
	public String toString()
	{
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}

	public String getSpeak()
	{
		return speak;
	}

	public void setSpeak(String speak)
	{
		this.speak = speak;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getContent()
	{
		return content;
	}

	public void setContent(String content)
	{
		this.content = content;
	}

	public String getAct()
	{
		return act;
	}

	public void setAct(String act)
	{
		this.act = act;
	}
}
