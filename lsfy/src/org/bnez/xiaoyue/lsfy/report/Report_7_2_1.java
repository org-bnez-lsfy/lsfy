package org.bnez.xiaoyue.lsfy.report;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_7_2_1 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_7_2_1.class);
	public void buildJob()
	{
		String[] zb = { "收案数", "结案数", "未结案数" };
		for (String z : zb)
		{
			ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
			for (ReportPeriod rp : rdb.build())
			{
				ReportCondition rc = new ReportCondition();
				rc.setFolderName("7、质效评估报表");
				rc.setReportName("7.2.1审判调研数据（收结存）");
				rc.setZhibiao(z);
				rc.setFrom(rp.getFrom());
				rc.setTo(rp.getTo());

				try
				{
					ReportJobScheduler.getInstance().add(new ZhibiaoOnSelect(rc));
				} catch (Exception e)
				{
					_logger.error(e.getMessage(), e);
				}
			}
		}
	}

	public static void main(String[] args)
	{
		new Report_7_2_1().buildJob();
		ReportJobScheduler.getInstance().start();
	}
}
