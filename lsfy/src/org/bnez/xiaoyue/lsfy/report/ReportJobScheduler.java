package org.bnez.xiaoyue.lsfy.report;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.common.Config;

public class ReportJobScheduler
{
	private static final Logger _logger = Logger.getLogger(ReportJobScheduler.class);
	private static ReportJobScheduler _instance = null;
	private List<ReportJob> _list;

	public static synchronized ReportJobScheduler getInstance()
	{
		if (_instance == null)
			_instance = new ReportJobScheduler();
		return _instance;
	}

	private ReportJobScheduler()
	{
		_list = new ArrayList<ReportJob>();
	}

	public void add(ReportJob job)
	{
		_logger.debug(job.getName());
		_list.add(job);
	}

	private synchronized ReportJob pick()
	{
		if (_list.size() == 0)
			return null;

		ReportJob job = _list.remove(0);
		return job;
	}

	protected synchronized void putback(ReportJob job)
	{
		_logger.debug(job.getName() + " pub back");
		_list.add(job);
	}

	public void start()
	{
		int ts = Config.getInstance().getInt("report.threads", 20);
		CountDownLatch cd = new CountDownLatch(ts);
		
		List<Thread> threads = new ArrayList<Thread>();
		for (int i = 0; i < ts; i++)
			threads.add(buildJobThread(cd));

		for (Thread t : threads)
			t.start();
		
		try
		{
			cd.await();
		} catch (InterruptedException e)
		{
			_logger.error(e.getMessage(), e);
		}
		_logger.debug("all report job done");
	}

	public int getJobs()
	{
		return _list.size();
	}

	private Thread buildJobThread(final CountDownLatch cd)
	{
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while (true)
				{
					ReportJob job = ReportJobScheduler.getInstance().pick();
					if (job == null)
					{
						_logger.debug("no more report job");
						break;
					}

					_logger.debug(job.getName() + " is picked");
					long st = System.currentTimeMillis();
					try
					{
						job.run();
					} catch (Exception e)
					{
						//ReportJobScheduler.getInstance().putback(job);
						_logger.error(job.getName(), e);
					}
					_logger.debug(job.getName() + " tu [" + (System.currentTimeMillis()-st) + "] ");
				}
				cd.countDown();
			}
		});
		return t;
	}
}
