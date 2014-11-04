package org.bnez.xiaoyue.lsfy.human.inter;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface TestableRemote extends Remote
{
	public boolean test() throws RemoteException;
}
