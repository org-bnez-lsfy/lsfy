package org.bnez.xiaoyue.lsfy.human;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.human.inter.HumanQuestion;
import org.bnez.xiaoyue.lsfy.human.inter.HumanAnswerServer;
import org.bnez.xiaoyue.lsfy.server.LsfyHandler;
import org.bnez.xiaoyue.lsfy.server.PushToClient;

public class LsfyHumanAnswerServer extends UnicastRemoteObject implements HumanAnswerServer
{
	private static final long serialVersionUID = -702848665928599675L;

	public LsfyHumanAnswerServer() throws RemoteException
	{
		super();
	}
	
	@Override
	public boolean test() throws RemoteException
	{
		return true;
	}

	@Override
	public HumanQuestion pick() throws RemoteException
	{
		return HumanQuestionList.getInstance().pick();
	}

	@Override
	public void answer(String ip, String question, String answer) throws RemoteException
	{
		new HumanAnswer(ip, question, answer).answer();
	}

	@Override
	public void ask(final String ip, String question) throws RemoteException
	{
		final XiaoyueResponse xr = LsfyHandler.getInstance().handle(question, ip);

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new PushToClient(ip).push(xr.getContent(), xr.getUrl());
			}
		}).start();
	}

}
