package org.bnez.xiaoyue.lsfy.job;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.report.ReportAll;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ReportJob implements Job
{

	private static final Logger _logger = Logger.getLogger(ReportJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		_logger.debug("report job start");
		new ReportAll().run();
		_logger.debug("report job end");
	}

}
