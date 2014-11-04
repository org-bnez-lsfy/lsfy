package org.bnez.xiaoyue.lsfy.human.inter;

import java.io.Serializable;

public class HumanQuestion implements Serializable
{

	private static final long serialVersionUID = 7095484502899023891L;

	private String _ip;
	private String _question;

	public HumanQuestion(String ip, String question)
	{
		_ip = ip;
		_question = question;
	}

	public String getIp()
	{
		return _ip;
	}
	
	public String getQuestion()
	{
		return _question;
	}
}
