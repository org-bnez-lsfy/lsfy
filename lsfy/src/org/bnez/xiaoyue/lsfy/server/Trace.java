package org.bnez.xiaoyue.lsfy.server;

import java.util.Date;

public class Trace
{
	private long id;
	private String received;
	private String response;
	private String ip;
	private long tu;
	private Date createAt;

	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getReceived()
	{
		return received;
	}

	public void setReceived(String received)
	{
		this.received = received;
	}

	public String getResponse()
	{
		return response;
	}

	public void setResponse(String response)
	{
		this.response = response;
	}

	public long getTu()
	{
		return tu;
	}

	public void setTu(long tu)
	{
		this.tu = tu;
	}

	public Date getCreateAt()
	{
		return createAt;
	}

	public void setCreateAt(Date createAt)
	{
		this.createAt = createAt;
	}

	public String getIp()
	{
		return ip;
	}

	public void setIp(String ip)
	{
		this.ip = ip;
	}

}
