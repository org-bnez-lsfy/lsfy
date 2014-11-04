package org.bnez.xiaoyue.lsfy.server;

import java.util.Date;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.XiaoyueResponse;
import org.bnez.xiaoyue.lsfy.util.HibernateUtil;
import org.hibernate.Session;

public class Recorder
{

	private static final Logger _logger = Logger.getLogger(Recorder.class);

	public static void record(final Object received, final XiaoyueResponse rsp, final String ip, final long tu)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new Recorder().record(received == null ? null:received.toString(), rsp == null ? null : rsp.toString(), ip , tu);
			}
		}).start();
	}

	private void record(String received, String response, String ip, long tu)
	{
		Session s = HibernateUtil.currentSession();
		s.beginTransaction();
		try
		{
			Trace t = new Trace();
			t.setReceived(received);
			t.setResponse(response);
			t.setIp(ip);
			t.setTu(tu);
			t.setCreateAt(new Date());
			s.save(t);
			s.getTransaction().commit();
			
		} catch (Exception e)
		{
			_logger.error(e.getMessage(), e);
			s.getTransaction().rollback();
		}
		HibernateUtil.closeSession();
	}
	
	public static void main(String[] args)
	{
		new Recorder().record("asdf", "jjj", "1.2.3.4", 123);
	}

}
