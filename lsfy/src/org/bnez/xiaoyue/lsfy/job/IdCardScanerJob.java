package org.bnez.xiaoyue.lsfy.job;

import org.bnez.xiaoyue.lsfy.idcard.IdCardRecordScaner;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class IdCardScanerJob implements Job
{
	//private static final Logger _logger = Logger.getLogger(IdCardScanerJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException
	{
		//_logger.debug("id card scaner job start");
		IdCardRecordScaner.getInstance().scan();
		//_logger.debug("id card scaner job end");
	}

}
