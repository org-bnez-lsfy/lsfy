package org.bnez.xiaoyue.lsfy.human.inter;

import java.rmi.RemoteException;

public interface HumanAnswerServer extends TestableRemote
{
	HumanQuestion pick() throws RemoteException;
	void answer(String ip, String question, String answer) throws RemoteException;
	void ask(String ip, String question) throws RemoteException;
}
