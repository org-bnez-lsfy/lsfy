package org.bnez.xiaoyue.lsfy.human.inter;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import org.apache.log4j.Logger;

public class RmiConnection
{
	private static final Logger _logger = Logger.getLogger(RmiConnection.class);
	private static final int CONNECT_TRY = 60;
	private String _url;
	private TestableRemote _remote;

	public RmiConnection(String rmiUrl)
	{
		_url = rmiUrl;
	}

	private void connect()
	{
		if(isConnected())
			return;
		
		for(int i=0; i<CONNECT_TRY; i++)
		{
			connectOnce();
			if(isConnected())
			{
				break;
			}
			_logger.error("FAILED to connect to [" + _url + "] after " + (i+1) + " times");
			
			try
			{
				Thread.sleep(1000);
			} catch (InterruptedException e)
			{
			}
		}
		if(!isConnected())
			_logger.error("FAILED to connect to [" + _url + "] after " + CONNECT_TRY + " times, stop more try");
	}

	private boolean isConnected()
	{
		if(_remote == null)
			return false;
		
		try
		{
			_remote.test();
			return true;
		} catch (RemoteException e)
		{
			_remote = null;
			_logger.error(e.getMessage());
			return false;
		}
	}

	private void connectOnce()
	{
		try
		{
			_remote = (TestableRemote)Naming.lookup(_url);
		} catch (MalformedURLException e)
		{
			_logger.error(e.getMessage());
		} catch (RemoteException e)
		{
			_logger.error(e.getMessage());
		} catch (NotBoundException e)
		{
			_logger.error(e.getMessage());
		}
	}

	public Remote getConnection()
	{
		connect();
		return _remote;
	}

}
