package org.bnez.xiaoyue.lsfy.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.bnez.xiaoyue.lsfy.common.Config;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class JobScheduler
{
	private static final Logger _logger = Logger.getLogger(JobScheduler.class);

	public void start()
	{
		try
		{

			SchedulerFactory sf = new StdSchedulerFactory();
			Scheduler sched = sf.getScheduler();

			// 每秒触发id卡扫描
			JobDetail job = JobBuilder.newJob(IdCardScanerJob.class).withIdentity("idcardScaner", "group1").build();
			String schedule = Config.getInstance().getString("job.idCard", "0/1 * * * * ?");
			CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger()
					.withSchedule(CronScheduleBuilder.cronSchedule(schedule)).build();
			Date ft = sched.scheduleJob(job, trigger);
			_logger.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
					+ trigger.getCronExpression());

			// 每天的0点5分触发报表查询
			schedule = Config.getInstance().getString("job.report", "0 5 0 * * ?");
			_logger.debug("report job " + schedule);
			job = JobBuilder.newJob(ReportJob.class).withIdentity("reportQuery", "group1").build();
			trigger = (CronTrigger) TriggerBuilder.newTrigger()
					.withSchedule(CronScheduleBuilder.cronSchedule(schedule)).build();
			ft = sched.scheduleJob(job, trigger);
			_logger.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
					+ trigger.getCronExpression());

			// 每天的0点5分触发瑕疵案件查询
			schedule = Config.getInstance().getString("job.xiaci", "0 5 0 * * ?");
			job = JobBuilder.newJob(XiaciQueryJob.class).withIdentity("xiaciQuery", "group1").build();
			trigger = (CronTrigger) TriggerBuilder.newTrigger()
					.withSchedule(CronScheduleBuilder.cronSchedule(schedule)).build();
			ft = sched.scheduleJob(job, trigger);
			_logger.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
					+ trigger.getCronExpression());

			sched.start();
			// try
			// {
			// Thread.sleep(300 * 1000L);
			// } catch (Exception e)
			// {
			// }
			//
			// sched.shutdown(true);
			// SchedulerMetaData metaData = sched.getMetaData();
			// _logger.info("Executed " + metaData.getNumberOfJobsExecuted() +
			// " jobs.");

		} catch (Exception e)
		{
		}
	}

	public static void main(String[] args)
	{
		new JobScheduler().start();
	}
}
