package org.bnez.xiaoyue.lsfy.human;

import org.bnez.xiaoyue.lsfy.server.PushToClient;

public class HumanAnswer
{
	private String _ip;
	@SuppressWarnings("unused")
	private String _q;
	private String _a;

	public HumanAnswer(String ip, String question, String answer)
	{
		_ip = ip;
		_q = question;
		_a = answer;
	}

	public void answer()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new PushToClient(_ip).push(_a, null);
			}
		}).start();
	}

}
