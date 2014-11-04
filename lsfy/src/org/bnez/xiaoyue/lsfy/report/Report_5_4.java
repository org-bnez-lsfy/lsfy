package org.bnez.xiaoyue.lsfy.report;

import org.apache.log4j.Logger;
import org.bnez.lsfy.service.ReportPeriod;

public class Report_5_4 implements ReportJobBuilder
{
	private static final Logger _logger = Logger.getLogger(Report_5_4.class);
	
	public void buildJob()
	{
		String[] zb = { "结案率", "实际执结率", "执结标的到位率" };
		for (String z : zb)
		{

			ReportPeriodBuilder rdb = ReportPeriodBuilder.getInstance();
			for (ReportPeriod rp : rdb.build())
			{
				ReportCondition rc = new ReportCondition();
				rc.setFolderName("5、执行分析主题");
				rc.setReportName("5.4最高院执行相关指标");
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
		new Report_5_4().buildJob();
		ReportJobScheduler.getInstance().start();
	}
}
