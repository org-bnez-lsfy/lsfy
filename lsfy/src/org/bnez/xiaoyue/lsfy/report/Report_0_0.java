package org.bnez.xiaoyue.lsfy.report;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_0_0 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_0_0.class);

	public void buildJob()
	{
		ReportCondition rc = new ReportCondition();
		rc.setFolderName("cube首页");
		rc.setReportName("首页-中院");
		rc.setZhibiao("结案数");
		rc.setTableTag(4);
		rc.setType(2);

		ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
		for (ReportPeriod rp : rdb.build())
		{
			rc.setTo(rp.getTo());
			String[] froms = rc.getTo().split("-");
			Integer from = Integer.parseInt(froms[0]) - 1;
			rc.setFrom(from.toString() + "-12-23");

			try
			{
				ReportJobScheduler.getInstance().add(new ZhibiaoOnSelect(rc));
			} catch (Exception e)
			{
				_logger.error(e.getMessage(), e);
			}
		}
	}

	public static void main(String[] args)
	{
		new Report_0_0().buildJob();
		ReportJobScheduler.getInstance().start();
		System.out.println("done");
	}
}
