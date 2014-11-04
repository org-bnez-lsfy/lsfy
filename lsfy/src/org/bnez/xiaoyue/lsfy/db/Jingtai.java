package org.bnez.xiaoyue.lsfy.db;

import java.util.Date;

public class Jingtai
{
	private Long id;
	private String combName;
	private String combText;
	private String answer;
	private String url;
	private Date createAt;
	
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getCombName()
	{
		return combName;
	}
	public void setCombName(String combName)
	{
		this.combName = combName;
	}
	public String getCombText()
	{
		return combText;
	}
	public void setCombText(String combText)
	{
		this.combText = combText;
	}
	public String getAnswer()
	{
		return answer;
	}
	public void setAnswer(String answer)
	{
		this.answer = answer;
	}
	public Date getCreateAt()
	{
		return createAt;
	}
	public void setCreateAt(Date createAt)
	{
		this.createAt = createAt;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	
}
