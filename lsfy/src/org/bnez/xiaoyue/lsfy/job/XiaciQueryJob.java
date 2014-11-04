package org.bnez.xiaoyue.lsfy.job;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.tdh.ReportQueryXiaci;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class XiaciQueryJob implements Job
{
	private static final Logger _logger = Logger.getLogger(XiaciQueryJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{

		_logger.debug("xiaci query job start");
		try
		{
			new ReportQueryXiaci();
		} catch (Exception e)
		{
			_logger.error(e.getMessage(), e);
		} 
		_logger.debug("xiaci query job done");
		
	}

}
